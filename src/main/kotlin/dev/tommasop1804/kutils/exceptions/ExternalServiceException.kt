package dev.tommasop1804.kutils.exceptions

/**
 * Represents an exception that is thrown when an error occurs while interacting with an external service.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused")
open class ExternalServiceException : RuntimeException {
    /**
     * Initializes a new instance of the ExternalServiceException class with no detail message.
     *
     * This constructor creates an exception instance without providing a specific error message
     * or additional context, indicating a generic failure while interacting with an external service.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an `ExternalServiceException` with the specified detail message.
     *
     * This constructor can be used to provide additional context about
     * an error encountered while interacting with an external service.
     * The detail message can help in debugging or logging the specifics
     * of the exception.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an `ExternalServiceException` with the specified detail message and cause.
     *
     * This constructor allows for creating an `ExternalServiceException` instance
     * with a specific error message and the root cause of the exception. It is
     * useful for providing detailed context and exception chaining in cases
     * where issues arise during interactions with external services.
     *
     * @param message A detailed message describing the error, or null if no specific message is to be provided.
     * @param cause The underlying cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs an `ExternalServiceException` with the specified cause.
     *
     * This constructor is used to create an exception instance that encapsulates
     * the underlying cause of the external service error. It facilitates exception
     * chaining by linking the current exception to the root cause, allowing for
     * better context and debugging.
     *
     * @param cause The underlying cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}