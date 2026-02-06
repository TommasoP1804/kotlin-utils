package dev.tommasop1804.kutils.annotations

/**
 * Annotation used to mark APIs, components, or code elements as potentially unsafe for usage
 * under certain circumstances. Elements annotated with this class should be approached with
 * caution, as they may introduce undesirable behavior or instability if improperly used.
 *
 * This annotation requires explicit opt-in to indicate the developer's acknowledgment of the
 * potential risks associated with the annotated element.
 *
 * @property reason A description explaining why the annotated element is considered unsafe.
 *                  Defaults to an empty string.
 * @property since Specifies the version since when this annotation is applicable
 *                 or when the annotated element was marked as unsafe. Defaults to an empty string.
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
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
annotation class UnsafeUsage(
    val reason: String = "",
    val since: String = ""
)
