@file:Suppress("unused")

package dev.tommasop1804.kutils.exceptions

/**
 * Represents an exception that is thrown when an unsupported type is encountered.
 *
 * This exception is used to signal that a particular type is not supported 
 * in the current context. It provides various constructors to accommodate 
 * different use cases, including specifying a detailed error message or 
 * chaining an underlying cause.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
open class UnsupportedTypeException : IllegalArgumentException {
    /**
     * Constructs an `UnsupportedTypeException` with no detail message.
     *
     * This constructor is used to create an instance of the `UnsupportedTypeException`
     * without providing additional details about the error. It represents a generic
     * unsupported type exception.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an `UnsupportedTypeException` with the specified detail message.
     *
     * This constructor allows creating an exception instance by providing a specific
     * error message describing the unsupported type. It helps in identifying the cause
     * of the issue and aids in debugging or logging unsupported type errors.
     *
     * @param message A detailed message explaining the unsupported type, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an `UnsupportedTypeException` with the specified detail message and cause.
     *
     * This constructor provides the ability to specify both a detailed error message
     * and the underlying cause of the exception. It is useful for identifying and
     * debugging issues related to unsupported types in operations or processes.
     *
     * @param message A detailed message describing the unsupported type error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs an `UnsupportedTypeException` with the specified cause.
     *
     * This constructor allows creating an exception instance that is directly derived from
     * another throwable cause, indicating an unsupported type scenario. It is useful for
     * exception chaining when the underlying cause of the issue requires propagation.
     *
     * @param cause The underlying cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that is thrown when an unsupported JSON type is encountered.
 *
 * This exception is typically raised during JSON processing operations when a data type
 * that is not supported for encoding or decoding is found. It provides constructors
 * for specifying additional context, such as error details and cause of the failure.
 *
 * This class extends the `UnsupportedTypeException` to specialize it for JSON-specific cases.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
open class UnsupportedJSONTypeException : UnsupportedTypeException {
    /**
     * Initializes a new instance of the `UnsupportedJSONTypeException` class
     * with no detail message.
     *
     * This constructor creates an exception instance that represents an unsupported
     * JSON type without providing any additional context or specific error details.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an `UnsupportedJSONTypeException` with the specified detail message.
     *
     * This constructor can be used to provide additional context about
     * an issue where a JSON type is not supported. The detail message can assist 
     * in identifying the specific type or scenario that caused the exception.
     *
     * @param message The detail message describing the unsupported JSON type, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an `UnsupportedJSONTypeException` with the specified detail message and cause.
     *
     * This constructor allows creating an `UnsupportedJSONTypeException` instance to provide
     * detailed information about the encountered error, including the error message and the
     * root cause. It facilitates exception chaining and provides context for issues related
     * to unsupported JSON data types.
     *
     * @param message A detailed message describing the error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs an `UnsupportedJSONTypeException` with the specified cause.
     *
     * This constructor is used to create an `UnsupportedJSONTypeException` instance
     * that encapsulates the underlying cause of the unsupported JSON type error.
     * It allows for exception chaining and provides additional context about the
     * root cause of the issue.
     *
     * @param cause The underlying cause of this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Exception thrown when an unsupported JYAML type is encountered.
 *
 * This exception is designed to indicate that a specific type is not supported
 * during JYAML processing operations. It extends `UnsupportedTypeException`,
 * allowing additional context in scenarios involving unsupported types.
 *
 * This class provides multiple constructors to support flexibility in specifying
 * error details, including a message, a cause, or both.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
open class UnsupportedYAMLTypeException : UnsupportedTypeException {
    /**
     * Initializes a new instance of the UnsupportedJYAMLTypeException class with no detail message.
     *
     * This constructor creates an UnsupportedJYAMLTypeException instance without any specific error
     * message or cause, indicating a generic issue related to unsupported JYAML types.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an `UnsupportedJYAMLTypeException` with the specified detail message.
     *
     * This constructor is used to create an exception instance by providing a
     * detailed error message. It helps in identifying the nature of the issue
     * related to unsupported JYAML types.
     *
     * @param message The detail message describing the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an `UnsupportedJYAMLTypeException` with the specified detail message and cause.
     *
     * This constructor allows for the creation of an `UnsupportedJYAMLTypeException` instance by 
     * specifying an error message and the underlying cause. It can be used to provide more 
     * detailed context about errors caused by unsupported JYAML data types, and enables exception chaining.
     *
     * @param message A detailed message describing the error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs an `UnsupportedJYAMLTypeException` with the specified cause.
     *
     * This constructor allows creating an instance of `UnsupportedJYAMLTypeException`
     * using another exception as the cause. It facilitates exception chaining and provides
     * additional context about the root cause of the error related to unsupported JYAML types.
     *
     * @param cause The underlying cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}