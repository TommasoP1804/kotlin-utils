package dev.tommasop1804.kutils.annotations

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory

/**
 * Indicates that a function, constructor, or type is performance-critical.
 * This annotation serves as a marker to highlight areas of code highly sensitive
 * to performance considerations. Code marked with this annotation should be
 * carefully reviewed and optimized for efficiency.
 *
 * @property reason Explanation for why the code is marked as performance-critical. Defaults to an empty string.
 * @property since Specifies the version since when the performance-critical designation is applicable. Defaults to an empty string.
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MustBeDocumented
@Suppress("unused")
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
annotation class PerformanceCritical(
    val reason: String = "",
    val since: String = ""
) {
    @Aspect
    class PerformanceCriticalAspect {
        private val logger = LoggerFactory.getLogger(PerformanceCriticalAspect::class.java)

        @Before("execution(@PerformanceCritical * *(..))")
        fun before(joinPoint: JoinPoint) {
            val method = joinPoint.signature.toShortString()
            logger.warn("Function $method is annotated with @PerformanceCritical. Usage of this should be limited to experimental or internal purposes, due to performance considerations.")
        }
    }
}
