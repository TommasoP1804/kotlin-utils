/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.exceptions

/**
 * Thrown to indicate that there is a mismatch between expected and actual versions
 * in a version-dependent operation or context.
 *
 * This exception is typically used to signal version-related inconsistencies, such as
 * when incompatible versions of data, APIs, or configurations are detected.
 * @since 4.7.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class VersionMismatchException : RuntimeException {
    /**
     * Creates an instance of VersionMismatchException with no detailed message or cause.
     * @since 4.7.0
     */
    constructor() : super()
    /**
     * Constructs a VersionMismatchException with the specified detail message.
     *
     * @param message The detail message, which can be null.
     * @since 4.7.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new VersionMismatchException with the specified detail message and cause.
     *
     * @param message the detail message, which can be null.
     * @param cause the cause of the exception, which can be null.
     * @since 4.7.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Creates a new instance of the exception with the specified cause.
     *
     * @param cause The cause of this exception, or null if the cause is nonexistent or unknown.
     * @since 4.7.0
     */
    constructor(cause: Throwable?) : super(cause)
}