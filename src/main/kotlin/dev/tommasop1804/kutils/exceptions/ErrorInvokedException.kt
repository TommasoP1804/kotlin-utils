/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.classes.functional.*

/**
 * Exception indicating that an error has been invoked in an unexpected or invalid state.
 * Extends IllegalStateException to represent illegal state scenarios specifically related to errors.
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
class ErrorInvokedException : IllegalStateException {
    /**
     * Creates an instance of [ErrorInvokedException] with a default error message.
     * The default message is "Error invoked".
     * @since 6.1.0
     */
    constructor() : super("Error invoked")
    /**
     * Constructs an [ErrorInvokedException] with a message indicating the specified error type.
     *
     * @param error The class type of the error that was invoked.
     * @since 6.1.0
     */
    constructor(error: Either.Left<*>) : super("Invoked error: ${(error.value ?: Unit)::class.simpleName}")
    /**
     * Constructs an instance of the exception with a custom error message.
     *
     * @param message The detail message describing the exception.
     * @since 6.1.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an instance of ErrorInvokedException with the specified cause.
     *
     * @param cause The cause of the exception, which can be retrieved later
     *              using the [Throwable.cause] property. A null value is permitted
     *              and indicates that the cause is nonexistent or unknown.
     * @since 6.1.0
     */
    constructor(cause: Throwable?) : super(cause)
    /**
     * Constructs a new [ErrorInvokedException] with the specified detail message and cause.
     *
     * @param message the detail message, which can be retrieved later by the [Throwable.message] property.
     * @param cause the cause of the exception, which can be retrieved later by the [Throwable.cause] property.
     * @since 6.1.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}