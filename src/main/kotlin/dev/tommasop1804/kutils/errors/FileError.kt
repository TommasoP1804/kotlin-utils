/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.errors

import dev.tommasop1804.kutils.exceptions.*
import java.io.File
import java.io.FileNotFoundException

/**
 * Represents an error related to file operations or file handling.
 *
 * This interface serves as a base for defining structured errors that occur
 * in the context of file-related operations. It provides information about
 * the file involved and an optional reason to describe the specific error scenario.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
interface FileError : Error {
    /**
     * Represents the file associated with a file-related error.
     *
     * This property provides access to the file involved in the error scenario,
     * allowing for detailed inspection or further processing related to the file.
     *
     * Typical use cases include identifying the file that caused the error, retrieving
     * metadata about the file, or performing operations to resolve the error condition.
     *
     * @since 6.1.3
     */
    val file: File

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
    data class FileNotFound(override val file: File) : FileError {
        override val linkedException = FileNotFoundException(file.path)
    }
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
    data class InvalidExtension(override val file: File) : FileError {
        override val linkedException = InvalidFileExtensionException(file)
    }
}