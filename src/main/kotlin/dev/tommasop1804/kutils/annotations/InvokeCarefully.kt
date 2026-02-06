package dev.tommasop1804.kutils.annotations

/**
 * Indicates that the annotated element should be used with caution, as improper usage might lead to
 * unintended or harmful behaviors. This annotation is typically applied to APIs or components that require
 * a higher level of attentiveness or understanding for proper and safe utilization.
 *
 * Usage of elements marked with this annotation will generate a compiler warning unless explicitly opted into.
 *
 * @property reason Describes why this element requires careful usage. Optional and defaults to an empty string.
 * @property since Specifies the version since the annotation or the designation became applicable. Defaults to an empty string.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Suppress("unused")
annotation class InvokeCarefully(
    val reason: String = "",
    val since: String = ""
)

