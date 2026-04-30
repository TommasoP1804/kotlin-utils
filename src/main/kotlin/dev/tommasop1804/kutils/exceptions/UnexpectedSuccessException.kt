/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.exceptions

/**
 * An exception that is thrown to indicate an unexpected success condition in a scenario where
 * failure was anticipated or required. This can be useful in test scenarios or guarded
 * operations where a specific failure path is expected.
 * @since 3.11.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
class UnexpectedSuccessException : IllegalStateException {
    /**
     * Creates a new instance of the UnexpectedSuccessException with no specific message or cause.
     * @since 3.11.0
     */
    constructor() : super()
    /**
     * Constructs an instance of UnexpectedSuccessException with a specified detail message.
     *
     * @param message The detail message that explains the exception.
     * @since 3.11.0
     */
    constructor(message: String) : super(message)
    /**
     * Constructs a new [UnexpectedSuccessException] with the specified detail message and cause.
     *
     * @param message The detail message, which provides more information about the exception.
     * @param cause The cause of the exception, which can be used to provide a nested exception.
     * @since 3.11.0
     */
    constructor(message: String, cause: Throwable) : super(message, cause)
    /**
     * Constructs an instance of UnexpectedSuccessException with the specified cause.
     *
     * @param cause The throwable cause that led to this exception.
     * @since 3.11.0
     */
    constructor(cause: Throwable) : super(cause)
}