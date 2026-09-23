/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

import dev.tommasop1804.kutils.classes.coding.*

/**
 * Represents errors that occur during JSON processing within the system.
 * This interface is a subtype of the `Error` marker interface, and implementations
 * provide specific details about different kinds of JSON-related errors.
 *
 * @author Tommaso Pastorelli
 * @since 6.1.0
 */
@Suppress("unused")
interface JsonError : Error {
    /**
     * Represents an error indicating that a specific path could not be found.
     *
     * This error is used in contexts where a particular path is expected to exist
     * but is missing or unavailable. The `path` parameter provides the specific
     * location that caused the error.
     *
     * @property path The path that was not found.
     * @author Tommaso Pastorelli
     * @since 6.1.0
     */
    data class PathNotFound(val path: String) : JsonError
    /**
     * Represents an error that indicates schema validation has failed.
     *
     * This error encapsulates a list of schema errors encountered during validation.
     * Each schema error provides details about what caused the validation to fail,
     * such as specific constraints or rules that were violated.
     *
     * It implements both the `JsonError` and `ValidationError` interfaces,
     * signifying it as a specific type of JSON-related validation issue.
     *
     * @property errors A list of schema errors that detail the validation failures.
     * @author Tommaso Pastorelli
     * @since 6.1.0
     */
    data class SchemaValidationFailed(val errors: List<JsonSchema.SchemaError>) : JsonError, ValidationError
}