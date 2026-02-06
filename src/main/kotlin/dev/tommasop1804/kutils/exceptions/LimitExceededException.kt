package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.classes.time.Duration

/**
 * Represents an exception that is thrown when an operation exceeds a predefined limit.
 *
 * This exception is designed to signal cases where a specific maximum operational limit,
 * such as the number of allowed attempts or other thresholds, has been surpassed.
 * It provides multiple constructors to allow flexibility in specifying the nature of the exception,
 * including the maximum limit that was exceeded, custom error messages, and chained exception causes.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class LimitExceededException : RuntimeException {
    /**
     * Initializes a new instance of the LimitExceededException class with no detail message.
     *
     * This constructor creates an exception instance without providing additional 
     * context or specific details, indicating a generic limit exceeded error.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `LimitExceededException` with the specified maximum times as an unsigned integer.
     *
     * This constructor is used to create an exception instance when a limit is exceeded.
     * The maximum value provided indicates the threshold that was surpassed.
     *
     * @param maxTimes The maximum allowed times as an unsigned integer.
     * @since 1.0.0
     */
    constructor(maxTimes: UInt) : super("Exceeded the maximum times of $maxTimes")
    /**
     * Constructs a `LimitExceededException` with a specified maximum limit in the form of an unsigned long.
     *
     * This constructor allows for the creation of an exception instance when an operation or process exceeds
     * the specified maximum limit. The limit is defined in terms of an unsigned long value (`ULong`).
     * It conveys clear context via the exception message about the constraint that has been violated.
     *
     * @param maxTimes The maximum allowable limit that has been exceeded, expressed as a `ULong`.
     * @since 1.0.0
     */
    constructor(maxTimes: ULong) : super("Exceeded the maximum times of $maxTimes")
    /**
     * Constructs a `LimitExceededException` with the specified detail message.
     *
     * This constructor can be used to provide additional context about
     * a limit-exceeding error. The detail message helps in debugging
     * or understanding the specifics of why the limit was exceeded.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `LimitExceededException` with the specified detail message and cause.
     *
     * This constructor allows for specifying both a detailed error message and the underlying
     * cause of the exception. It is useful for providing additional context about the
     * circumstances leading to the exception and supports exception chaining to identify the
     * root cause of the issue.
     *
     * @param message A detailed message describing the exception, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `LimitExceededException` with the specified cause.
     *
     * This constructor is used to create an instance of `LimitExceededException` by specifying
     * the underlying cause of the exception. It provides additional context about the root cause
     * of the error, enabling exception chaining and better error reporting.
     *
     * @param cause The underlying cause of this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that is thrown when an operation exceeds the allowed retry limit.
 * 
 * This exception provides a mechanism to signal that an operation has been attempted 
 * beyond an acceptable threshold of retry attempts. It supports constructors for specifying
 * detailed error messages, the maximum number of retries allowed, and optional exception chaining.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class RetryLimitExceededException : LimitExceededException {
    /**
     * Initializes a new instance of the `RetryLimitExceededException` class with no detail message.
     *
     * This constructor creates an exception instance without a message or additional context,
     * indicating a generic case where the retry limit has been exceeded.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `RetryLimitExceededException` with the specified maximum number of retries.
     *
     * This constructor allows for creating a specific exception to indicate that an operation
     * has exceeded the allowed maximum number of retries. This can be helpful for managing
     * retry limits in processes that require controlled repetitions.
     *
     * @param maxTimes The maximum number of times an operation is allowed before the exception is thrown.
     * @since 1.0.0
     */
    constructor(maxTimes: Int) : super("The operation exceeded the maximum times of $maxTimes")
    /**
     * Constructs a `RetryLimitExceededException` with the specified maximum number of retry attempts.
     *
     * This constructor allows the creation of a `RetryLimitExceededException` instance with a custom
     * message indicating that the operation has exceeded the provided maximum number of retry attempts.
     *
     * @param maxTimes The maximum number of allowed retry attempts.
     * @since 1.0.0
     */
    constructor(maxTimes: Long) : super("The operation exceeded the maximum times of $maxTimes")
    /**
     * Constructs a `RetryLimitExceededException` with the specified detail message.
     *
     * This constructor allows creating an exception instance with a custom message
     * to provide additional context for an operation exceeding its retry limit.
     *
     * @param message The detail message associated with this exception, or null if no specific message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `RetryLimitExceededException` with the specified detail message and cause.
     *
     * This constructor enables providing a detailed error message along with a cause for 
     * the exception when the retry limit is exceeded. It supports exception chaining and 
     * provides additional context about the failure.
     *
     * @param message A detailed message describing the retry limit exceedance, or null if no message is provided.
     * @param cause The underlying cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `RetryLimitExceededException` with the specified cause.
     *
     * This constructor is used to create an instance of the exception by specifying 
     * the underlying cause of the failure. It enables exception chaining and provides 
     * additional context about the root cause of the limit-exceeding condition.
     *
     * @param cause The underlying cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception thrown when an operation exceeds the allocated time limit.
 *
 * This exception is used to signal a situation where a process or task has failed to complete
 * within the expected or defined time constraints. The class provides multiple constructors
 * to allow the specification of error details, such as a message, a cause, or advanced options
 * like suppression and writable stack trace.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class OutOfTimeException : LimitExceededException {
    /**
     * Initializes an instance of the `OutOfTimeException` class with no detail message.
     *
     * This constructor creates an exception instance without providing additional
     * information, which indicates a generic timeout-related error.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an `OutOfTimeException` with a message indicating that the operation
     * exceeded the allocated time.
     *
     * @param maxTime The maximum duration allowed for the operation before this exception is thrown.
     * @since 1.0.0
     */
    constructor(maxTime: Duration) : super("The operation exceeded the allocated time of $maxTime")
    /**
     * Constructs an `OutOfTimeException` with the specified detail message.
     *
     * This constructor allows for creating an `OutOfTimeException` instance by providing
     * a custom error message. This message can be used to convey additional context
     * regarding the exception, such as explaining the reason for the time-related failure.
     *
     * @param message The detail message describing the exception, or null if no specific message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an `OutOfTimeException` with the specified detail message and cause.
     *
     * This constructor can be used to provide a detailed description of the error and the
     * underlying cause that triggered the exception. It is useful for exception chaining
     * and for providing additional context when debugging or diagnosing the issue.
     *
     * @param message A detailed message explaining the cause of this exception, or null if no specific message is provided.
     * @param cause The cause of the exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs an `OutOfTimeException` with the specified cause.
     *
     * This constructor is used to create an exception instance encapsulating
     * the underlying cause of the timing-related failure. It enables exception chaining
     * and provides more context about the root cause of the exception.
     *
     * @param cause The underlying cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}