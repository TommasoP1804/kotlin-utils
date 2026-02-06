package dev.tommasop1804.kutils.annotations

import dev.tommasop1804.kutils.isNotNull
import dev.tommasop1804.kutils.isNull
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory

/**
 * Marks an API, component, or type as scheduled for deprecation and eventual removal.
 * This annotation serves as an indicator that the annotated element is intended to be removed
 * in a future version, allowing developers to plan accordingly.
 *
 * @property reason Explains the rationale behind marking the element for removal. Optional and defaults to an empty string.
 * @property since Specifies the version since when the annotated element has been marked for removal. Defaults to an empty string.
 * @property expectedRemovalVersion Indicates the version where the element is expected to be removed. Optional and defaults to an empty string.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.VALUE_PARAMETER
)
@MustBeDocumented
@Suppress("unused")
@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
annotation class ToBeRemoved(
    val reason: String = "",
    val since: String = "",
    val expectedRemovalVersion: String = ""
) {
    @Aspect
    class ToBeRemovedAspect {
        private val logger = LoggerFactory.getLogger(ToBeRemovedAspect::class.java)

        @Before("execution(@ToBeRemoved * *(..))")
        fun before(joinPoint: JoinPoint) {
            val method = joinPoint.signature.toShortString()

            val annotation = try {
                // Try to get annotation from the target method (for instance methods)
                if (joinPoint.target.isNotNull()) {
                    val targetMethod = try {
                        joinPoint.target.javaClass.getDeclaredMethod(
                            joinPoint.signature.name,
                            *joinPoint.args.filterNotNull().map { it.javaClass }.toTypedArray()
                        )
                    } catch (e: NoSuchMethodException) {
                        joinPoint.target.javaClass.declaredMethods.find { it.name == joinPoint.signature.name }
                    }
                    targetMethod?.getAnnotation(ToBeRemoved::class.java)
                } else {
                    // Handle top-level functions by searching in the declaring class
                    val declaringClass = joinPoint.signature.declaringType
                    val methods = declaringClass.declaredMethods
                    val targetMethod = methods.find {
                        it.name == joinPoint.signature.name &&
                                it.parameterTypes.contentEquals(joinPoint.args.filterNotNull().map { arg -> arg.javaClass }.toTypedArray())
                    }
                    targetMethod?.getAnnotation(ToBeRemoved::class.java)
                }
            } catch (e: Exception) {
                logger.warn("Could not retrieve @ToBeRemoved annotation for method {}: {}", method, e.message)
                null
            }

            if (annotation.isNull()) { return }
            logger.warn("Function {} is annotated with @ToBeRemoved. It will be removed in future version.", method)

            if (annotation.reason.isNotEmpty())
                logger.warn("Reason: {}", annotation.reason)

            if (annotation.since.isNotEmpty())
                logger.warn("Marked for removal since version: {}", annotation.since)

            if (annotation.expectedRemovalVersion.isNotEmpty())
                logger.warn("Expected removal version: {}", annotation.expectedRemovalVersion)
        }
    }
}