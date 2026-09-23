/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.errors

import java.io.File
import java.nio.file.Path

/**
 * Represents an error related to file operations or file handling.
 *
 * This class serves as a base for defining structured errors that occur
 * in the context of file-related operations. It provides information about
 * the file involved and an optional reason to describe the specific error scenario.
 *
 * @property file The file associated with the error.
 * @property reason An optional message detailing the specific reason or context of the error.
 *
 * @constructor Creates a new instance of `FileError` using a `File` object and an optional reason.
 * @constructor Creates a new instance of `FileError` using a `Path` object and an optional reason.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
open class FileError(open val file: File, open val reason : String? = null) : Error {
    /**
     * Secondary constructor for the FileError class, allowing initialization using a Path instance
     * instead of a File instance. Converts the given Path to a File and delegates to the primary constructor.
     *
     * @param path The file path object to be converted to a File.
     * @param reason An optional string providing additional information about the error.
     * Defaults to null if not specified.
     * @since 6.1.0
     */
    constructor(path: Path, reason: String? = null) : this(path.toFile(), reason)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileError

        if (file != other.file) return false
        if (reason != other.reason) return false

        return true
    }

    override fun hashCode(): Int {
        var result = file.hashCode()
        result = 31 * result + reason.hashCode()
        return result
    }

    override fun toString(): String {
        return "FileError(file=$file, reason=$reason)"
    }

    /**
     * Represents an error caused by a file not being found.
     *
     * This class is a specific type of [FileError], tailored to provide precise
     * handling for scenarios where a requested file is missing from the expected
     * location. It encapsulates the file object related to the error.
     *
     * @constructor Creates an instance of [FileNotFound] with the specified file.
     * @param file The file that could not be found.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class FileNotFound(override val file: File) : FileError(file)
    /**
     * Represents an error caused by an invalid file extension.
     *
     * This exception is thrown when a file operation encounters a file with an
     * unsupported or unexpected extension. It provides information about the
     * file that triggered the error and includes the invalid extension as part of
     * its error context.
     *
     * @property file The file with the invalid extension that caused the error.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class InvalidExtension(override val file: File) : FileError(file, "Invalid ${file.extension} extension")
}