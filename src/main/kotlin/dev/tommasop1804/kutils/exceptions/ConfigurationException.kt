package dev.tommasop1804.kutils.exceptions

/**
 * Represents an exception that is thrown when a configuration error occurs.
 *
 * This exception is a runtime exception intended to signal issues related
 * to improper or failed configurations during the execution of a program.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused")
open class ConfigurationException : RuntimeException {
    /**
     * Initializes a new instance of the ConfigurationException class with no detail message.
     *
     * This constructor can be used to create an exception without providing additional context
     * or a specific error message.
     *
     * @since 1.0.0
     */
    constructor() : super()
    
    /**
     * Constructs a ConfigurationException with a specified detail message.
     *
     * This constructor allows for initializing this exception with
     * a custom message providing additional context or details about the error.
     *
     * @param message The detail message associated with this exception. Can be null.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    
    /**
     * Constructs a new ConfigurationException with a specified detail message and cause.
     *
     * This constructor allows the provision of an error message describing the details of the exception
     * and the underlying cause, represented as a Throwable.
     *
     * @param message The detail message associated with this exception or null if not specified.
     * @param cause The underlying cause of this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    
    /**
     * Constructs a new ConfigurationException with the specified cause.
     *
     * This constructor allows providing a throwable cause that led to this exception,
     * enabling exception chaining and root cause analysis.
     *
     * @param cause the underlying cause of this exception, or null if no cause is specified
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}