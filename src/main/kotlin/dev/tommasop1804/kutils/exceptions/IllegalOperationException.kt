/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.exceptions

/**
 * Signals that an illegal or forbidden operation has been attempted.
 *
 * This exception is a subclass of [RuntimeException], typically used
 * to indicate that a method or operation is not allowed for the current
 * state of an object or context.
 *
 * Is different from [UnsupportedOperationException] in that it is not
 * intended to be used for cases where the operation is supported but not
 * available due to the current state of the object or context. Is used to
 * indicate that the operation is absolutely not allowed in the current context,
 * or, in general, forbidden.
 *
 * @author Tommaso Pastorelli
 * @since 2.3.0
 */
@Suppress("unused")
open class IllegalOperationException : RuntimeException {
    /**
     * Creates a new instance of IllegalOperationException with no detailed message or cause.
     * @since 2.3.0
     */
    constructor() : super()
    /**
     * Constructs a new instance of IllegalOperationException with the specified detail message.
     *
     * @param message The detail message, which provides additional information about the exception.
     * @since 2.3.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an instance of IllegalOperationException with the specified detail message and cause.
     *
     * @param message The detail message, or `null` if none is provided.
     * @param cause The cause of the exception, or `null` if none is provided.
     * @since 2.3.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new [IllegalOperationException] with the specified cause.
     *
     * @param cause the cause of the exception, which can be null.
     * @since 2.3.0
     */
    constructor(cause: Throwable?) : super(cause)
}