package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.classes.coding.Language

/**
 * Represents an exception that is thrown when an error occurs during the compilation process.
 *
 * This exception provides various constructors to accommodate different levels of detail
 * about the compilation error, such as the programming language, specific error message,
 * or an underlying cause.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused")
open class CompilationException : Exception {
    /**
     * Initializes a new instance of the CompilationException class with a default error message.
     *
     * @since 1.0.0
     */
    constructor() : super("Error while compiling.")
    /**
     * Constructs a CompilationException with a message indicating an Error while compiling
     * in the context of the specified programming language.
     *
     * @param language the programming language in which the compilation error occurred
     * @since 1.0.0
     */
    constructor(language: Language) : super("Error while compiling ${language.displayName}.")
    /**
     * Constructs a `CompilationException` with the specified detail message.
     *
     * @param message The detail message describing the error. Can be null.
     * @since 1.0.0
     */
    constructor(message: String?) : super("Error while compiling. $message")
    /**
     * Constructs a new CompilationException with the specified language and message.
     *
     * @param language the programming language related to the compilation error
     * @param message an optional error message providing additional details
     * @since 1.0.0
     */
    constructor(language: Language, message: String?) : super("Error while compiling ${language.displayName}. $message")
    /**
     * Constructs a new CompilationException with a specified cause.
     *
     * This constructor allows the initialization of a CompilationException
     * instance by passing in a Throwable that caused this exception.
     *
     * @param cause the Throwable that triggered this exception, or null if no cause is specified
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super("Error while compiling.", cause)
    /**
     * Constructs a new [CompilationException] with a specified programming language and a cause.
     *
     * This constructor allows specifying a [Language] that triggered the compilation error
     * and the underlying cause of the exception.
     *
     * @param language the programming language associated with the exception
     * @param cause the underlying cause of the exception
     * @since 1.0.0
     */
    constructor(language: Language, cause: Throwable?) : super("Error while compiling ${language.displayName}.", cause)
    /**
     * Constructs a `CompilationException` with a detailed message and a cause.
     *
     * The provided message will be appended to a predefined error message.
     * The cause can be used for capturing and chaining exceptions that led to this error.
     *
     * @param message Additional details about the error.
     * @param cause The underlying cause of the exception.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super("Error while compiling. $message", cause)
    /**
     * Constructs a [CompilationException] with the specified language, error message, and cause.
     *
     * @param language The programming language in which the error occurred.
     * @param message The detail message, which may provide more context about the error.
     * @param cause The underlying cause of the error, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(language: Language, message: String?, cause: Throwable?) : super("Error while compiling ${language.displayName}. $message", cause)
}