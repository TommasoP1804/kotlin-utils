package dev.tommasop1804.kutils.annotations

/**
 * Annotation used to mark APIs, components, or code elements that could benefit from optimization.
 * This annotation serves as a marker to indicate areas of the codebase where improvements,
 * such as performance enhancements or resource usage optimizations, are recommended.
 *
 * Developers may use this annotation to track and document specific elements that require
 * attention for optimization purposes.
 *
 * @property reason Specifies an explanation or justification for marking the element as needing optimization. Defaults to an empty string.
 * @property since Indicates the version since when this annotation or the marked element has been relevant. Defaults to an empty string.
 * @property expectedOptimizationVersion Specifies the version where the optimization is expected to be completed or addressed. Optional and defaults to an empty string.
 * @author Tommaso Pastorelli
 * @since 1.0.0
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
annotation class ToBeOptimized(
    val reason: String = "",
    val since: String = "",
    val expectedOptimizationVersion: String = ""
)