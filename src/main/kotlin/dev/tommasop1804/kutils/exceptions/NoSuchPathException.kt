/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.exceptions

/**
 * Exception thrown when a specified path cannot be found.
 *
 * This exception typically indicates that an attempt to access or resolve
 * a specific path has failed due to its absence or unreachability.
 * @since 3.2.0
 * @author Tommaso Pastorelli
 */
open class NoSuchPathException : ValidationFailedException {
    /**
     * Default constructor for NoSuchPathException.
     * Initializes an instance of the exception without any specific path or cause.
     * @since 3.2.0
     */
    constructor() : super()
    /**
     * Constructs a `NoSuchPathException` with a message indicating the specified path was not found.
     *
     * @param path The path that could not be found.
     * @since 3.2.0
     */
    constructor(path: Any) : super("Path not found: $path")
    /**
     * Constructs a new `NoSuchPathException` with a specific path and cause.
     *
     * @param path The path that was not found.
     * @param cause The underlying cause of the exception.
     * @since 3.2.0
     */
    constructor(path: Any, cause: Throwable?) : super("Path not found: $path", cause)
    /**
     * Constructs a NoSuchPathException with the specified cause.
     *
     * @param cause The throwable cause of this exception.
     * @since 3.2.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Exception thrown when a specific JSON path cannot be found during processing.
 * This class extends the [NoSuchPathException] to provide more context
 * specific to JSON path not found scenarios.
 * @since 3.2.0
 * @author Tommaso Pastorelli
 */
open class NoSuchJsonPathException : NoSuchPathException {
    /**
     * Default constructor for the NoSuchJsonPathException exception.
     * Calls the default constructor of the superclass NoSuchPathException.
     * @since 3.2.0
     */
    constructor() : super()
    /**
     * Constructs a new instance of the NoSuchJsonPathException exception with the specified path.
     *
     * @param path The path associated with the exception.
     * @since 3.2.0
     */
    constructor(path: Any) : super(path)
    /**
     * Constructs a new `NoSuchJsonPathException` exception with the specified path and cause.
     *
     * @param path The path that was not found.
     * @param cause The cause of the exception.
     * @since 3.2.0
     */
    constructor(path: Any, cause: Throwable?) : super(path, cause)
    /**
     * Constructs a NoSuchJsonPathException exception with the specified cause.
     *
     * @param cause the throwable that caused this exception to be thrown
     * @since 3.2.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that is thrown when a specific YAML path cannot be found.
 * This class extends from [NoSuchPathException], providing additional context
 * relevant to YAML processing.
 * @since 3.2.0
 * @author Tommaso Pastorelli
 */
open class NoSuchYamlPathException : NoSuchPathException {
    /**
     * Constructs an instance of NoSuchYamlPathException with no additional information.
     * @since 3.2.0
     */
    constructor() : super()
    /**
     * Constructs a new instance of the NoSuchYamlPathException exception with the specified path.
     *
     * @param path The path that could not be found.
     * @since 3.2.0
     */
    constructor(path: Any) : super(path)
    /**
     * Constructs a NoSuchYamlPathException exception with the specified path and cause.
     *
     * @param path The path that could not be found.
     * @param cause The cause of the exception.
     * @since 3.2.0
     */
    constructor(path: Any, cause: Throwable?) : super(path, cause)
    /**
     * Constructs a new NoSuchYamlPathException exception with the specified cause.
     *
     * @param cause the underlying cause of the exception.
     * @since 3.2.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Exception thrown when a specified TOML path cannot be found.
 *
 * This exception is a specialized version of `NoSuchPathException` and is
 * used to indicate that a specific path in a TOML configuration file
 * does not exist or cannot be accessed. It can encapsulate additional
 * details about the path or the underlying cause of the issue.
 * @since 3.11.0
 * @author Tommaso Pastorelli
 */
open class NoSuchTomlPathException : NoSuchPathException {
    /**
     * Constructs a new instance of `NoSuchTomlPathException`.
     *
     * This default constructor initializes the exception with no specific
     * path or cause, serving as a general indication of a missing TOML path.
     * @since 3.11.0
     */
    constructor() : super()
    /**
     * Constructs a new `NoSuchTomlPathException` with the specified path.
     *
     * @param path The TOML path that could not be found. This can be of any type
     *             that provides information about the unreachable path.
     * @since 3.11.0
     */
    constructor(path: Any) : super(path)
    /**
     * Constructs a new `NoSuchTomlPathException` with the specified path and an optional cause.
     *
     * @param path The TOML path that was not found.
     * @param cause The underlying cause of the exception, or null if there is no specific cause.
     * @since 3.11.0
     */
    constructor(path: Any, cause: Throwable?) : super(path, cause)
    /**
     * Constructs a new instance of `NoSuchTomlPathException` with the specified cause.
     *
     * This constructor allows the creation of an exception instance by wrapping another
     * throwable as the cause, providing additional context for debugging and error handling.
     *
     * @param cause The throwable that caused this exception to be thrown, or null if there is no specific cause.
     * @since 3.11.0
     */
    constructor(cause: Throwable?) : super(cause)
}
