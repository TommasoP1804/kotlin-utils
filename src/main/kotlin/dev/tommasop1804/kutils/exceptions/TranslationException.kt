package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.isNotNull
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Represents an exception that is thrown during the translation process when an error occurs.
 *
 * This exception is used to signal issues encountered while performing translation operations.
 * It may occur due to invalid input, unsupported languages, or other translation-related errors.
 * The class provides multiple constructors to allow flexibility in specifying error details,
 * such as a descriptive message, a cause, or both.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class TranslationException : RuntimeException {
    /**
     * Initializes a new instance of the TranslationException class with no detail message.
     *
     * This constructor creates a generic exception instance indicating a translation-related error,
     * without providing additional context or specific error details.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `TranslationException` with a message that describes the inability
     * to translate from one value to another, optionally including the type of the object.
     *
     * This constructor generates a contextual error message that incorporates the values
     * of `from`, `to`, and `type` parameters, providing detailed information about the translation failure.
     *
     * @param from The source object that could not be translated.
     * @param to The target object or type to which the translation failed.
     * @param type An optional `KClass` representing the type involved in the translation process, or null if not specified.
     *
     * @since 1.0.0
     */
    constructor(from: Any?, to: Any?, type: KClass<*>? = null) : super(
        "Unable to translate ${if (type.isNotNull()) "${type.simpleName} " else ""}from $from to $to."
    )
    /**
     * Constructs a TranslationException with a message indicating the failure
     * to translate an object from one type or value to another.
     *
     * The generated message includes the type being translated (if specified
     * and not null) and the "from" and "to" values involved in the translation.
     *
     * @param from The source object being translated, or null if unspecified.
     * @param to The target object or type for the translation, or null if unspecified.
     * @param type The type involved in the translation, or null if not specified.
     * @since 1.0.0
     */
    constructor(from: Any?, to: Any?, type: KType? = null) : super(
        "Unable to translate ${if (type.isNotNull()) "$type " else ""}from $from to $to."
    )
    /**
     * Constructs a `TranslationException` with specified source, target, optional type, and cause.
     *
     * This constructor initializes a `TranslationException` instance to represent an error that occurs
     * when an object of one type cannot be translated to another type. It provides additional context
     * by incorporating the source, target, an optional type, and the underlying cause, if available.
     *
     * The exception message is dynamically generated based on whether the provided type is non-null.
     *
     * @param from The source object that was being translated, or null if unspecified.
     * @param to The target object to which the translation was being attempted, or null if unspecified.
     * @param type An optional `KClass` representing the type involved in the translation operation.
     * @param cause The underlying cause of this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(from: Any?, to: Any?, type: KClass<*>? = null, cause: Throwable?) : super(
        "Unable to translate ${if (type.isNotNull()) "${type.simpleName} " else ""}from $from to $to.",
        cause
    )
    /**
     * Constructs a `TranslationException` indicating a failure to translate a value from a source to a target type.
     *
     * This constructor allows specifying the source value, target value, an optional type information, and the root cause.
     * It generates a message describing the translation failure, including the type if provided.
     *
     * @param from The source value to be translated, or null if unspecified.
     * @param to The target value for the translation, or null if unspecified.
     * @param type The optional `KType` describing the type involved in the translation, or null if not applicable.
     * @param cause The underlying cause of the translation failure, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(from: Any?, to: Any?, type: KType? = null, cause: Throwable?) : super(
        "Unable to translate ${if (type.isNotNull()) "$type " else ""}from $from to $to.",
        cause
    )
    /**
     * Constructs a `TranslationException` with the specified detail message.
     *
     * This constructor allows providing additional context about
     * a translation-related error. The detail message can help in identifying
     * issues related to translation failures during execution.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `TranslationException` with the specified detail message and cause.
     *
     * This constructor enables the creation of an exception instance that provides both
     * an explanatory message and the underlying cause of a translation failure. It is useful
     * for debugging complex issues where a message and a chained exception are required to
     * fully understand the context of the error.
     *
     * @param message A detailed message describing the translation error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `TranslationException` with the specified cause.
     *
     * This constructor creates a `TranslationException` instance by encapsulating
     * the underlying cause of the error. This is useful for exception chaining and
     * for providing additional context about the root cause of translation-related issues.
     *
     * @param cause The underlying cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}