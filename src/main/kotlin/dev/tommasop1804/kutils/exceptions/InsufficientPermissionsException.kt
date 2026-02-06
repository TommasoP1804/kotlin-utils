package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.EMPTY
import dev.tommasop1804.kutils.before

/**
 * Exception thrown to indicate that an operation could not be completed
 * due to insufficient permissions.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused")
open class InsufficientPermissionsException : RuntimeException {
    /**
     * Extracts the internal error code from the associated message by retrieving the substring
     * that precedes the delimiter " @@@ ". If the extracted substring is blank, the result is null.
     *
     * This property provides additional context for errors, particularly when specific internal
     * codes are embedded within the message field.
     *
     * @since 1.0.0
     */
    val internalErrorCode: String?
        get() = message?.before(" @@@ ")?.ifBlank { null }

    /**
     * Constructs an instance of InsufficientPermissionsException with no detail message or cause.
     * This constructor calls the superclass [SecurityException] default constructor.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new `InsufficientPermissionsException` with the specified detail message.
     *
     * @param message the detail message, which provides more information about the reason for the exception.
     * @param internalErrorCode the internal error code associated with the exception.
     * @since 1.0.0
     */
    constructor(message: String?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + message)
    /**
     * Constructs a new `InsufficientPermissionsException` with the specified cause.
     *
     * @param cause the cause of the exception, which can be retrieved using Throwable.getCause.
     * @param internalErrorCode the internal error code associated with the exception.
     * @since 1.0.0
     */
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super(internalErrorCode?.plus(" @@@ "), cause)
    /**
     * Constructs a new InsufficientPermissionsException with the specified detail message and cause.
     *
     * @param message the detail message, which can be retrieved using Throwable.getMessage.
     * @param cause the cause of the exception, which can be retrieved using Throwable.getCause.
     * @param internalErrorCode the internal error code associated with the exception.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + message, cause)
}