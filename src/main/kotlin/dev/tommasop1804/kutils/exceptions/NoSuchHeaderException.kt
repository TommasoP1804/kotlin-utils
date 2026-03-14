package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.EMPTY
import dev.tommasop1804.kutils.isNotNullOrBlank

/**
 * Exception thrown to indicate that a specified HTTP header was not found.
 *
 * This exception provides detailed context about the missing header, including
 * its name and an optional message or cause, helping developers trace the source
 * of the issue more effectively during debugging.
 *
 * It extends [NoSuchElementException], specializing it for scenarios related to
 * missing headers in HTTP interactions or other similar use cases.
 *
 * @constructor Creates an exception with no additional details.
 * @since 2.2.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
class NoSuchHeaderException : NoSuchElementException {
    /**
     * Constructs a new instance of `NoSuchHeaderException` with no additional message or cause.
     *
     * @since 2.2.0
     */
    constructor() : super()
    /**
     * Constructs a new `NoSuchHeaderException` with a custom message indicating the header name that was not found.
     *
     * @param headerName The name of the header that could not be found.
     * @param message An optional additional message providing more context about the exception.
     *                If this parameter is not null or blank, it will be appended to the exception's message.
     * @since 2.2.0
     */
    constructor(headerName: String, message: String? = null) : super("Header $headerName not found." + if (message.isNotNullOrBlank()) " $message" else String.EMPTY)
    /**
     * Constructs a new instance of `NoSuchHeaderException` with a detailed error message
     * and an optional cause. The error message includes the specified header name and an
     * optional additional message if provided.
     *
     * @param headerName The name of the header that could not be found.
     * @param message An optional additional message providing more context about the exception.
     * @param cause The underlying cause of the exception, or `null` if none exists.
     * @since 2.2.0
     */
    constructor(headerName: String, message: String? = null, cause: Throwable) : super("Header $headerName not found." + if (message.isNotNullOrBlank()) " $message" else String.EMPTY, cause)
    /**
     * Constructs a new NoSuchHeaderException with the specified cause.
     *
     * This constructor allows for wrapping another throwable that caused this exception.
     *
     * @param cause The cause of this exception, which can be retrieved later by the [Throwable.cause] property.
     * @since 2.2.0
     */
    constructor(cause: Throwable) : super(cause)
}