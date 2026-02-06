package dev.tommasop1804.kutils.exceptions

/**
 * Represents an exception that is thrown when an encoding operation fails.
 *
 * This exception is designed to signal issues encountered during data encoding
 * processes. It may occur due to unsupported algorithms, invalid input data, or
 * other encoding-related errors. The class provides multiple constructors to
 * accommodate various use cases, including specifying a detailed message,
 * a cause, or the particular algorithm that caused the encoding failure.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused")
open class EncodeException : RuntimeException {
    /**
     * Initializes a new instance of the EncodeException class with no detail message.
     *
     * This constructor creates an exception instance without providing any additional
     * context or specific error message, indicating a generic encoding failure.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an `EncodeException` with the specified detail message.
     *
     * This constructor can be used to provide additional context about
     * an encoding-related error. The detail message can assist in debugging
     * or logging the specifics of the exception.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an `EncodeException` with the specified detail message and cause.
     *
     * This constructor allows for creating an `EncodeException` instance by specifying
     * an error message and the underlying cause. It is useful for providing additional
     * context about encoding-related issues and for enabling exception chaining.
     *
     * @param message A detailed message describing the encoding error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs an `EncodeException` with the specified cause.
     *
     * This constructor allows creating an exception instance based on another exception
     * that caused the encoding failure. It facilitates exception chaining and provides
     * additional context to the encoding error.
     *
     * @param cause The underlying cause of the encoding failure, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that is thrown when a decoding operation fails.
 *
 * This exception is raised when a decoding process encounters an error, such as
 * invalid data input, unsupported algorithms, or other decoding-related issues.
 * It provides multiple constructors to allow flexibility in specifying
 * error details, including a message, a cause, or the specific hashing algorithm
 * associated with the decoding failure.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused")
open class DecodeException : RuntimeException {
    /**
     * Initializes a new instance of the DecodeException class with no detail message.
     *
     * This constructor creates an exception instance without providing additional context
     * or specific error details, indicating a generic decoding failure.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `DecodeException` with the specified detail message.
     *
     * This constructor can be used to provide additional context about
     * a decoding-related error. The detail message can assist in debugging
     * or logging the specifics of the exception.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `DecodeException` with the specified detail message and cause.
     *
     * This constructor allows for providing a detailed error message alongside the
     * underlying cause of the decoding failure. It enables exception chaining
     * and helps in identifying the root cause of the issue during the decoding process.
     *
     * @param message A detailed message describing the decoding error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `DecodeException` with the specified cause.
     *
     * This constructor is used to create an exception instance that encapsulates
     * the underlying cause of the decoding failure. It allows for exception chaining
     * and helps provide more context about the root cause of the error.
     *
     * @param cause The underlying cause of this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}