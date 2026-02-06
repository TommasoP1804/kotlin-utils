package dev.tommasop1804.kutils.annotations

import dev.tommasop1804.kutils.isNotNull
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory

/**
 * An annotation used to mark elements in the codebase that require fixing or further attention.
 * This could be used to explicitly indicate that the marked element has known issues or identified improvements,
 * providing essential context such as the reason, since which version it was identified, and the expected fix version.
 * Developers are encouraged to address the issue as soon as possible.
 *
 * An optional aspect (`ToBeImplementedAspect`) is provided to log warnings during runtime whenever
 * a function annotated with `@ToBeFixed` is executed, enhancing visibility of issues within the system.
 *
 * @property reason The reason or rationale behind marking the element as requiring a fix.
 *                  This field is optional and defaults to an empty string.
 * @property since Specifies the version in which the issue or required fix was identified.
 *                 Optional and defaults to an empty string.
 * @property expectedFixVersion Indicates the version by which this issue is expected to be fixed.
 *                              Optional and defaults to an empty string.
 * @since 1.0.0
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
annotation class ToBeFixed(
    val reason: String = "",
    val since: String = "",
    val expectedFixVersion: String = ""
) {
    @Aspect
    class ToBeImplementedAspect {
        private val logger = LoggerFactory.getLogger(ToBeFixed::class.java)

        @Before("execution(@ToBeFixed * *(..))")
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
                    targetMethod?.getAnnotation(ToBeFixed::class.java)
                } else {
                    // Handle top-level functions by searching in the declaring class
                    val declaringClass = joinPoint.signature.declaringType
                    val methods = declaringClass.declaredMethods
                    val targetMethod = methods.find {
                        it.name == joinPoint.signature.name &&
                                it.parameterTypes.contentEquals(joinPoint.args.filterNotNull().map { arg -> arg.javaClass }.toTypedArray())
                    }
                    targetMethod?.getAnnotation(ToBeFixed::class.java)
                }
            } catch (e: Exception) {
                logger.warn("Could not retrieve @ToBeFixed annotation for method {}: {}", method, e.message)
                null
            }

            if (annotation.isNotNull()) {
                logger.warn("Function $method has to be fixed.")
                if (annotation.reason.isNotEmpty())
                    logger.warn("Reason: ${annotation.reason}")
                if (annotation.since.isNotEmpty())
                    logger.warn("Marked as to be fixed since version: ${annotation.since}")
                if (annotation.expectedFixVersion.isNotEmpty())
                    logger.warn("Expected fixing version: ${annotation.expectedFixVersion}")
            }
        }
    }
}