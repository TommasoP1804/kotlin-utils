/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.exceptions.*

/**
 * Represents a TOML-related error within the system.
 * This interface serves as a specialized subset of the generic error model
 * for issues arising during TOML processing.
 *
 * Implementers of this interface define specific types of errors that can occur
 * while interacting with TOML data.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
interface TomlError : Error {
    /**
     * Represents an error that occurs when a specific path within a TOML structure is not found.
     *
     * This data class implements the [TomlError] interface, providing a structured representation
     * of a missing path error in the context of TOML errors. The `path` property indicates
     * the specific path that could not be located in the TOML structure.
     *
     * @property path The TOML path that could not be found.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class PathNotFound(val path: String) : TomlError {
        override val linkedException = NoSuchTomlPathException(path)
    }
    /**
     * Represents an error that occurs when schema validation fails.
     *
     * This class contains a list of schema errors that describe the specific issues
     * encountered during the validation process.
     *
     * @property errors A list of schema errors detailing the issues found during validation.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class SchemaValidationFailed(val errors: List<JsonSchema.SchemaError>) : TomlError, ValidationError {
        override val linkedException = TomlSchemaValidationException(errors)
    }
}