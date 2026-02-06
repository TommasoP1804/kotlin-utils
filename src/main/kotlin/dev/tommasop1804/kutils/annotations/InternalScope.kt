package dev.tommasop1804.kutils.annotations

/**
 * Marks an API, component, or code element as being internal to the scope specified.
 * This annotation serves as an indicator that the annotated element is intended for internal use
 * only and should not be considered part of the public API.
 *
 * @property reason Explanation for why the element is marked as internal. Defaults to an empty string.
 * @property since Specifies the version since when this annotation or the internal scope designation is applicable. Defaults to an empty string.
 * @property reference References or related resources for clarification, such as documentation or issue links. Defaults to an empty array.
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
@Suppress("unused")
annotation class InternalScope(
    val reason: String = "",
    val since: String = ""
)
