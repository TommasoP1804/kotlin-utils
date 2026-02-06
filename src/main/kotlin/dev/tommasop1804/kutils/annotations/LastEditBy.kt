package dev.tommasop1804.kutils.annotations

/**
 * Annotation used to specify the last editor of a program element along with an optional timestamp.
 * This is primarily intended for documentation purposes, allowing developers to track the last modification
 * and the individual responsible for the last edit.
 *
 * @property value Represents the name of the person or entity that last edited the program element.
 *                  It defaults to an empty string.
 * @property timestamp Represents the timestamp of the last edit in string format.
 *                     It defaults to an empty string.
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
annotation class LastEditBy(
    val value: String = "",
    val timestamp: String = ""
)
