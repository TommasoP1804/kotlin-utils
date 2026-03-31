@file:Suppress("unused")

package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.*

/**
 * Represents an exception that is thrown when a configuration error occurs.
 *
 * This exception is a runtime exception intended to signal issues related
 * to improper or failed configurations during the execution of a program.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
open class ConfigurationException : RuntimeException {
    /**
     * Provides the internal error code extracted from the associated message.
     * The error code is determined by obtaining the substring of the message
     * that precedes a predefined delimiter `@@@`. If the message does not
     * contain the delimiter or the extracted value is blank, the result is `null`.
     *
     * @return The extracted internal error code as a nullable string, or `null` if not present.
     * @since 1.0.2
     */
    val internalErrorCode: String?
        get() = message?.before(" @@@ ")?.ifBlank { null }
    
    /**
     * Creates a new instance of the [ConfigurationException] with no additional details.
     *
     * This constructor initializes the exception without any message or cause, 
     * intended to serve as a simple, default initializer.
     *
     * @since 1.0.2
     */
    constructor() : super()
    /**
     * Constructs a new ConfigurationException with the specified detail message and an optional internal error code.
     *
     * The internal error code, if provided, is prefixed to the message using the delimiter " @@@ ", 
     * allowing for structured error messages that include both user-facing and internal debugging information.
     *
     * @param message The detail message explaining the reason for the exception, or null if none is provided.
     * @param internalErrorCode An optional internal error code for identifying specific error cases, or null if none is provided.
     * @since 1.0.2
     */
    constructor(message: String?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + message)
    /**
     * Constructs a new instance of the exception with the specified cause and an optional internal error code.
     * 
     * Combines the internal error code (if provided) with a delimiter " @@@ " before passing it
     * to the parent exception. This allows the exception to encapsulate additional error details
     * for internal debugging or logging purposes.
     * 
     * @param cause The cause of the exception, or null if the cause is not known or nonexistent.
     * @param internalErrorCode Optional internal error code that provides additional context about the exception.
     * @since 1.0.2
     */
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super(internalErrorCode?.plus(" @@@ "), cause)
    /**
     * Constructs a new `ConfigurationException` instance with a detailed message, a cause, 
     * and an optional internal error code.
     *
     * @param message The detail message providing additional information about the exception.
     *                If an internal error code is provided, it will be prefixed to the message.
     * @param cause The cause of the exception, or null if the cause is unknown or not specified.
     * @param internalErrorCode An optional error code providing additional context about the 
     *                          exception. If provided, it will be prefixed to the message, 
     *                          separated by ` @@@ `.
     * @since 1.0.2
     */
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + message, cause)
}

/**
 * Represents an exception that is thrown when an invalid environment property is encountered.
 *
 * This exception is a specialized form of [ConfigurationException] and is used
 * to signal issues related to incorrectly configured or unresolvable environment properties
 * during application configuration or runtime.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.2
 */
open class InvalidEnvPropertyException : ConfigurationException {
    /**
     * Initializes a new instance of the InvalidEnvPropertyException class with no specific details.
     *
     * This primary constructor delegates to the superclass ConfigurationException's no-arguments constructor.
     *
     * @since 1.0.2
     */
    constructor() : super()
    /**
     * Constructs an instance of `InvalidEnvPropertyException` with detailed error information about an
     * invalid configuration property.
     *
     * The exception message is dynamically built based on provided parameters to include specific
     * details such as the property name, its value, optional custom message, and an internal error code.
     *
     * @param propertyName The name of the invalid environment property, or null if not specified.
     * @param propertyValue The value of the invalid environment property, or null if not specified.
     * @param message An optional custom error message, or null if not specified.
     * @param internalErrorCode An optional internal error code to categorize the error, or null if not specified.
     * @since 1.0.2
     */
    constructor(propertyName: String?, propertyValue: String? = null, message: String? = null, internalErrorCode: String? = null) : super(
        (internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + "Invalid environment property${if (propertyName.isNotNull()) " `$propertyName` " else String.EMPTY}${if (propertyValue.isNotNull()) " with value `$propertyValue`" else String.EMPTY}${if (message.isNotNull()) ": $message" else String.EMPTY}",
        internalErrorCode
    )
    /**
     * Constructs an instance of the exception with a specified error message and an optional internal error code.
     *
     * @param message The detail message associated with the exception. May be null.
     * @param internalErrorCode An optional internal error code that provides additional context about the exception. Default is null.
     * @since 1.0.2
     */
    constructor(message: String?, internalErrorCode: String? = null) : super(message, internalErrorCode)
    /**
     * Constructs an instance of [InvalidEnvPropertyException] with the specified cause and an optional internal error code.
     *
     * @param cause The underlying cause of this exception, or null if there is no specific cause.
     * @param internalErrorCode A string representing an optional internal error code for diagnostics, or null if not specified.
     * @since 1.0.2
     */
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super(cause, internalErrorCode)
    /**
     * Constructs an instance of `InvalidEnvPropertyException` with the specified detail message, cause, 
     * and an optional internal error code.
     *
     * @param message The detail message explaining the exception. Can be null.
     * @param cause The underlying cause of the exception. Can be null.
     * @param internalErrorCode An optional code representing the internal error. Defaults to null.
     * 
     * @since 1.0.2
     */
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super(message, cause, internalErrorCode)
}

/**
 * Represents an exception that is thrown when an attempt is made to access a non-existent
 * or undefined environment property in the system configuration.
 *
 * This exception is a subtype of [ConfigurationException] and is designed to handle
 * cases where environment-specific properties are expected but not found.
 * @since 3.3.2
 */
open class NoSuchEnvPropertyException : ConfigurationException {
    /**
     * Constructs a new instance of [NoSuchEnvPropertyException] with no specific details.
     *
     * This constructor creates a default instance of the exception, which can be used
     * in scenarios where detailed context about the missing environment property is not available
     * or necessary.
     * @since 3.3.2
     */
    constructor() : super()
    /**
     * Constructs a new instance of the exception with additional details.
     *
     * This constructor allows specifying the name of the missing property, an optional message
     * providing additional context, and an optional internal error code to assist in error tracking.
     *
     * The generated exception message is formatted to include:
     * - The internal error code (if provided).
     * - The name of the missing property (if provided).
     * - The additional message (if provided).
     *
     * @param propertyName The name of the environment property that was not found. Can be null.
     * @param message An optional message providing additional details about the exception. Can be null.
     * @param internalErrorCode An optional internal error code for diagnostic purposes. Can be null.
     * @since 3.3.2
     */
    constructor(propertyName: String?, message: String? = null, internalErrorCode: String? = null) : super(
        (internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + "Property${if (propertyName.isNotNull()) " `$propertyName` " else String.EMPTY}not found${if (message.isNotNull()) ": $message" else String.EMPTY}",
        internalErrorCode
    )
    /**
     * Creates an instance of [NoSuchEnvPropertyException] with a specified error message and an optional internal error code.
     *
     * @param message The detail message associated with the exception. Can be null.
     * @param internalErrorCode An optional internal error code used for categorizing the exception.
     * @since 3.3.2
     */
    constructor(message: String?, internalErrorCode: String? = null) : super(message, internalErrorCode)
    /**
     * Constructs a new instance of NoSuchEnvPropertyException with the given cause and optional internal error code.
     *
     * @param cause The underlying exception that caused this exception to be thrown, or null if no cause is specified.
     * @param internalErrorCode An optional identifier for the error, which may be used for tracking or debugging purposes.
     * @since 3.3.2
     */
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super(cause, internalErrorCode)
    /**
     * Constructs an instance of [NoSuchEnvPropertyException] with the specified detail
     * message, cause, and internal error code.
     *
     * @param message The detail message to describe the exception, or null if no message is provided.
     * @param cause The cause of the exception, represented as a [Throwable], or null if no cause is provided.
     * @param internalErrorCode An optional error code that can be used to represent internal errors,
     *        or null if no error code is specified.
     * @since 3.3.2
     */
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super(message, cause, internalErrorCode)
}