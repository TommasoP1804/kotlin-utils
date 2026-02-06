package dev.tommasop1804.kutils.annotations

/**
 * Annotation used to indicate the presence of a translation key that should be verified.
 * Typically, elements annotated with this are intended to ensure correctness and accuracy
 * when dealing with internationalization and localization keys.
 *
 * The `correctKey` parameter specifies the expected key that should be used. This can help
 * identify discrepancies between the actual and intended translation keys during runtime
 * or static analysis.
 *
 * This annotation is a marker that enforces strict correctness, as denoted by
 * the `RequiresOptIn.Level.ERROR` opt-in requirement.
 *
 * @property correctKey The expected correct key for translation. Defaults to an empty string,
 *                      which indicates no specific key is enforced.
 * @since 1.0.0
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE
)
@Retention(AnnotationRetention.RUNTIME)
@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
@Suppress("unused")
annotation class CheckTranslationKey(
    val correctKey: String = ""
)