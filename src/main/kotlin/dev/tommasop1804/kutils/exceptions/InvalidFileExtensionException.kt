/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.exceptions

import java.io.File
import java.io.IOException
import java.nio.file.Path

/**
 * Exception thrown to indicate that a file has an invalid or unsupported file extension.
 *
 * This exception can be used to handle cases where the file type is not allowed or does not meet the expected format.
 *
 * @since 6.1.3
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
class InvalidFileExtensionException : IOException {
    /**
     * Constructs a new instance of InvalidFileExtensionException using the extension of the provided file.
     *
     * @param file The file whose extension is used to construct the exception message.
     * @since 6.1.3
     */
    constructor(file: File) : super(file.extension)
    /**
     * Constructs an instance of `InvalidFileExtensionException` using the extension of the provided `Path`.
     *
     * @param file the file path whose extension is used in the exception message
     * @since 6.1.3
     */
    constructor(file: Path) : super(file.toFile().extension)
    /**
     * Constructs a new instance of InvalidFileExtensionException with the specified detail message.
     *
     * @param message The detail message saved for later retrieval by the Throwable.message property.
     * @since 6.1.3
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new InvalidFileExtensionException with the specified detail message and cause.
     *
     * @param message The detail message, which can be retrieved later by the [Throwable.message] property.
     * @param cause The cause of the exception, which can be retrieved later by the [Throwable.cause] property.
     *              A null value indicates that the cause is nonexistent or unknown.
     * @since 6.1.3
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new `InvalidFileExtensionException` with the specified cause.
     *
     * @param cause The cause of the exception, which can later be retrieved using the [Throwable.cause] method.
     *              A null value indicates that the cause is nonexistent or unknown.
     * @since 6.1.3
     */
    constructor(cause: Throwable?) : super(cause)
}