package dev.tommasop1804.kutils.annotations

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory

/**
 * Marks an API or component as being in the alpha stage of development. This designation implies that the API or component
 * is still under development and may undergo significant changes before it becomes stable. Usage of APIs annotated
 * with this marker should be limited to experimental or internal purposes, as compatibility is not guaranteed.
 *
 * @property reason Explains the rationale for marking the API as alpha. Optional and defaults to an empty string.
 * @property since Specifies the version since when this annotation or alpha API is applicable. Default is an empty string.
 * @property expectedBetaVersion Indicates the version when this API is expected to transition to beta. Optional and defaults to an empty string.
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
annotation class Alpha(
    val reason: String = "",
    val since: String = "",
    val expectedBetaVersion: String = ""
) {
    @Aspect
    class AlphaAspect {
        private val logger = LoggerFactory.getLogger(AlphaAspect::class.java)

        @Before("execution(@Alpha * *(..))")
        fun before(joinPoint: JoinPoint) {
            val method = joinPoint.signature.toShortString()
            logger.warn("Function $method is annotated with @Alpha. Usage of this should be limited to experimental or internal purposes.")
        }
    }
}