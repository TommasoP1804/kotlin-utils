package dev.tommasop1804.kutils.exceptions

/**
 * Represents an exception that is thrown for errors related to geometric computations or operations.
 *
 * This exception is intended to signal issues or failures encountered during geometry-related
 * processes, such as invalid inputs, unsupported operations, or computational errors.
 * The class provides multiple constructors to accommodate different use cases, enabling
 * specification of error details, messages, or causes of the exception.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class GeometryException : RuntimeException {
    /**
     * Initializes a new instance of the GeometryException class with no detail message.
     *
     * This constructor creates an exception instance without providing additional context
     * or specific error details, indicating a generic geometry-related failure.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `GeometryException` with the specified detail message.
     *
     * This constructor initializes an instance of `GeometryException` with a detail message
     * describing the specific issue encountered. The message provides additional context
     * about the geometric error that occurred.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `GeometryException` with the specified detail message and cause.
     *
     * This constructor allows for providing a detailed error message and the
     * underlying cause of the exception. It supports exception chaining and
     * helps in understanding the root cause of the error in geometry-related operations.
     *
     * @param message A detailed message describing the error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `GeometryException` with the specified cause.
     *
     * This constructor allows creating an instance of `GeometryException`
     * that encapsulates and links to the underlying cause of a geometric error.
     * It aids in exception chaining, providing context about the root cause of the issue.
     *
     * @param cause The root exception or error that caused this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}