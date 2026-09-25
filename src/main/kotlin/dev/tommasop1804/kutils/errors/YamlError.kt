/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.exceptions.*

/**
 * Represents an error related to YAML parsing or validation.
 * This interface is a specialization of the base `Error` type,
 * providing a structure for handling YAML-specific error scenarios.
 *
 * Implementers of this interface encapsulate details about specific
 * issues encountered in YAML processing, such as missing paths
 * or schema validation failures.
 *
 * @author Tommaso Pastorelli
 * @since 6.1.0
 */
@Suppress("unused")
interface YamlError : Error {
    /**
     * Represents an error indicating that a specific YAML path was not found.
     *
     * This class is used to provide detailed context about a missing path during
     * YAML processing or validation. It encapsulates the path that could not be located.
     *
     * @property path The YAML path that was not found.
     * @author Tommaso Pastorelli
     * @since 6.1.0
     */
    data class PathNotFound(val path: String) : YamlError {
        override val linkedException get() = NoSuchYamlPathException(path)
    }
    /**
     * Represents an error that occurs when schema validation fails.
     * This error provides a list of schema-specific errors encountered during validation.
     *
     * @property errors A collection of errors that describe the schema validation issues.
     * @author Tommaso Pastorelli
     * @since 6.1.0
     */
    data class SchemaValidationFailed(val errors: List<JsonSchema.SchemaError>) : YamlError, ValidationError {
        override val linkedException get() = YamlSchemaValidationException(errors)
    }
}