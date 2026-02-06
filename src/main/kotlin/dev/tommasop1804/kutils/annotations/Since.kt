package dev.tommasop1804.kutils.annotations

/**
 * Specifies the version since when a particular program element or construct has been introduced
 * or became applicable. This annotation is used as a marker to provide versioning context
 * for APIs, components, or other code elements.
 *
 * @property value Specifies the version since when the annotated element is applicable or introduced. Defaults to an empty string.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPE,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
    AnnotationTarget.FILE,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.VALUE_PARAMETER
)
@MustBeDocumented
@Suppress("unused")
annotation class Since(
    val value: String = "",
)
