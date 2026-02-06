package dev.tommasop1804.kutils.annotations

import dev.tommasop1804.kutils.isNotNull
import dev.tommasop1804.kutils.isNotNullOrEmpty
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory

/**
 * Annotation used to mark a specific code element as "to be implemented" in the future.
 * This could include classes, functions, or other program elements that are placeholders
 * or are planned for further development. It helps communicate to developers that the
 * annotated element is not yet complete or optimized.
 *
 * @property since Specifies the version since when this annotation or the annotated element has been marked
 *                 as "to be implemented." Defaults to an empty string.
 * @property expectedImplementationVersion Indicates the version in which the annotated element is expected
 *                                         to be fully implemented or optimized. Defaults to an empty string.
 * @property throwError Indicates whether the annotated element should throw an error when invoked. Defaults to `true`.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.CONSTRUCTOR
)
@MustBeDocumented
@Suppress("unused")
@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
annotation class ToBeImplemented(
    val since: String = "",
    val expectedImplementationVersion: String = "",
    val throwError: Boolean = true
) {
    @Aspect
    class ToBeImplementedAspect {
        private val logger = LoggerFactory.getLogger(ToBeImplementedAspect::class.java)

        @Before("execution(@ToBeImplemented * *(..))")
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
                    targetMethod?.getAnnotation(ToBeImplemented::class.java)
                } else {
                    // Handle top-level functions by searching in the declaring class
                    val declaringClass = joinPoint.signature.declaringType
                    val methods = declaringClass.declaredMethods
                    val targetMethod = methods.find {
                        it.name == joinPoint.signature.name &&
                                it.parameterTypes.contentEquals(joinPoint.args.filterNotNull().map { arg -> arg.javaClass }.toTypedArray())
                    }
                    targetMethod?.getAnnotation(ToBeImplemented::class.java)
                }
            } catch (e: Exception) {
                logger.warn("Could not retrieve @ToBeRemoved annotation for method {}: {}", method, e.message)
                null
            }

            if (annotation?.throwError != false)
                throw NotImplementedError("Function $method is not implemented." +
                        (if (annotation?.since.isNotNullOrEmpty()) " | Since: ${annotation.since}" else "") +
                        if (annotation?.expectedImplementationVersion.isNotNullOrEmpty()) " | Expected implementating version: ${annotation.expectedImplementationVersion}" else ""
                )
            logger.warn("Function $method is not implemented.")
            if (annotation.since.isNotEmpty())
                logger.warn("Marked as to be implemented since version: ${annotation.since}")
            if (annotation.expectedImplementationVersion.isNotEmpty())
                logger.warn("Expected implementing version: ${annotation.expectedImplementationVersion}")
        }
    }
}