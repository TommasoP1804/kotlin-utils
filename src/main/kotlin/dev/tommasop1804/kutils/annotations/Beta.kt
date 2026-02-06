package dev.tommasop1804.kutils.annotations

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory

/**
 * Marks an API or component as being in the beta stage of development. This designation indicates that the API
 * or component is more stable compared to an alpha release but may still undergo changes before reaching
 * full stability. Usage of APIs annotated with this marker should be done with caution, as minor incompatibilities
 * may still be introduced.
 *
 * @property reason Explanation for marking the API as beta. Optional and defaults to an empty string.
 * @property since Specifies the version since when this annotation or beta API is applicable. Default is an empty string.
 * @property expectedStableVersion Indicates the version where the API is expected to reach full stability. Optional and defaults to an empty string.
 * @author Tommaso Pastorelli
 * @since 1.0.0
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
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
annotation class Beta(
    val reason: String = "",
    val since: String = "",
    val expectedStableVersion: String = ""
) {
    @Aspect
    class BetaAspect {
        private val logger = LoggerFactory.getLogger(BetaAspect::class.java)

        @Before("execution(@Beta * *(..))")
        fun before(joinPoint: JoinPoint) {
            val method = joinPoint.signature.toShortString()
            logger.warn("Function $method is annotated with @Beta. Usage of this should be limited to experimental or internal purposes.")
        }
    }
}