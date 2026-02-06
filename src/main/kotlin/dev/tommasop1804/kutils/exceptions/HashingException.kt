package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.HashingAlgorithm

/**
 * Represents an exception that is thrown when a hashing operation fails.
 *
 * This exception is used to signal issues encountered during cryptographic
 * hashing processes. Such issues may include unsupported algorithms, errors during
 * the hashing computation, or other hashing-related failures. The exception
 * provides constructors for various use cases, enabling flexibility in specifying
 * detailed error messages, underlying causes, or the specific algorithm
 * associated with the hashing failure.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused")
open class HashingException : RuntimeException {
    /**
     * Initializes a new instance of the HashingException class with no detail message.
     *
     * This constructor creates an exception without providing any specific details
     * about the hashing error. It is used to signal a general failure in hashing operations.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `HashingException` specifying the hashing algorithm that caused the error.
     *
     * This constructor is used when an error occurs during a cryptographic operation involving the specified
     * hashing algorithm. The exception message will include the name of the algorithm that triggered the failure,
     * providing detailed context for debugging and logging purposes.
     *
     * @param algorithm The hashing algorithm which caused the exception.
     * @since 1.0.0
     */
    constructor(algorithm: HashingAlgorithm) : super("Unable to hash data with ${algorithm.name} algorithm")
    /**
     * Constructs a `HashingException` specifying the hashing algorithm that caused the error.
     *
     * This constructor is used when an error occurs during a cryptographic operation involving the specified
     * hashing algorithm. The exception message will include the name of the algorithm that triggered the failure,
     * providing detailed context for debugging and logging purposes.
     *
     * @param algorithm The hashing algorithm which caused the exception.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(algorithm: HashingAlgorithm, cause: Throwable?) : super("Unable to hash data with ${algorithm.name} algorithm", cause)
    /**
     * Constructs a `HashingException` with the specified detail message.
     *
     * This constructor allows creating an instance of `HashingException` with a message
     * that provides additional context regarding the hashing-related error. The detail
     * message can be used for debugging or logging the specifics of the failure.
     *
     * @param message The detail message describing the hashing error, or null if no specific message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `HashingException` with the specified detail message and cause.
     *
     * This constructor enables the creation of a `HashingException` instance by providing
     * both a descriptive error message and an underlying cause. It supports exception
     * chaining and allows for better debugging by associating the hashing-related error
     * with its root cause.
     *
     * @param message A detailed message describing the hashing error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `HashingException` with the specified cause.
     *
     * This constructor allows creating an exception instance based on another exception
     * that caused the hashing failure. It facilitates exception chaining and provides
     * additional context related to the error.
     *
     * @param cause The underlying cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}