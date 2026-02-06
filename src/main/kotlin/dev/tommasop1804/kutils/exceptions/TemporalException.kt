package dev.tommasop1804.kutils.exceptions

/**
 * Represents an exception that is thrown when a temporal-related operation fails.
 *
 * This exception is intended to signal problems that occur in temporal operations,
 * such as those related to time-based computations, scheduling, or datetime processing.
 * Also can signal an unsupported operation, like trying to get an end from an infinite interval.
 * It provides multiple constructors to allow flexibility in specifying error details,
 * including a message or a cause.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class TemporalException : RuntimeException {
    /**
     * Initializes a new instance of the TemporalException class with no detail message.
     *
     * This constructor creates an exception instance with no specific error message
     * or additional context, representing a general case of a temporal-related failure.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `TemporalException` with the specified detail message.
     *
     * This constructor can be used to create a `TemporalException` instance with a detailed
     * error message, providing more context about the runtime error encountered and aiding
     * in debugging or logging the specifics of the issue.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `TemporalException` with the specified detail message and cause.
     *
     * This constructor allows the creation of a `TemporalException` instance by specifying
     * a detailed error message and the underlying cause. It helps in providing additional
     * context for debugging and supports exception chaining to identify the root cause
     * of the runtime issue.
     *
     * @param message A detailed message describing the exception, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `TemporalException` with the specified cause.
     *
     * This constructor allows for creating a `TemporalException` instance by encapsulating
     * the root cause of the temporal-related issue. It supports exception chaining, enabling
     * better context for diagnosing the source of failure.
     *
     * @param cause The underlying cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}