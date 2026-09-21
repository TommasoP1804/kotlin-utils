/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

import dev.tommasop1804.kutils.classes.coding.*

interface JsonError : Error {
    data class PathNotFound(val path: String) : JsonError
    data class SchemaValidationFailed(val errors: List<JsonSchema.SchemaError>) : JsonError, ValidationError
}