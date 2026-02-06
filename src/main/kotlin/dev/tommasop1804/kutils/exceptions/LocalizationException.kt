package dev.tommasop1804.kutils.exceptions

/**
 * Represents an exception that is thrown when a localization process fails.
 *
 * This exception is used to signal issues related to localization operations, such as
 * missing translations, invalid locale-specific data, or other localization-related errors.
 * The class provides multiple constructors to allow flexible handling of errors, including
 * specifying a message, a cause, or both. It helps to encapsulate and propagate localization
 * issues effectively during runtime.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class LocalizationException : RuntimeException {
    /**
     * Initializes a new instance of the LocalizationException class with no detail message.
     *
     * This constructor creates a generic instance of LocalizationException without
     * providing a specific error context or message.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `LocalizationException` with the specified detail message.
     *
     * This constructor can be used to provide a descriptive message about the localization-related error.
     * The detail message can offer additional context or insight into the nature of the exception.
     *
     * @param message A detail message providing context about the exception, or null if no specific message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `LocalizationException` with the specified detail message and cause.
     *
     * This constructor allows providing both an error message and the underlying cause
     * of the exception. It is typically used for exception chaining, enabling more
     * detailed context about the nature of the error or failure.
     *
     * @param message A description of the error, or null if no specific message is provided.
     * @param cause The cause of the exception, or null if there is no specific underlying cause.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `LocalizationException` with the specified cause.
     *
     * This constructor is used to create an exception instance that encapsulates
     * the underlying cause of the localization-related failure. It aids in
     * exception chaining and helps trace the root cause of the issue.
     *
     * @param cause The underlying cause of the localization failure, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}