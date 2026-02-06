package dev.tommasop1804.kutils.annotations

/**
 * Annotation to specify the creator or author of a particular program element along with an optional timestamp.
 * It is primarily used for documentation purposes, providing traceability regarding who created an element
 * and when it was created. This annotation can be applied to a variety of Kotlin targets, including functions,
 * classes, properties, and files.
 *
 * @property value Represents the name of the creator or author. Defaults to an empty string.
 * @property timestamp Specifies the creation timestamp in string format. Defaults to an empty string.
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
annotation class CreatedBy(
    val value: String = "",
    val timestamp: String = ""
)
