/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

/**
 * Represents errors related to numeric operations or interpretations.
 * This interface is a specialization of the `Error` type, designed for
 * categorizing issues that arise in scenarios involving numbers.
 *
 * It acts as a base type for more specific numeric-related errors and
 * provides a structured approach for error handling in these contexts.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
interface NumberError : Error {
    /**
     * Represents an error that indicates the provided radix is invalid or unsupported.
     *
     * This type of error typically arises when attempting to perform
     * operations that require a valid numerical radix, but the provided
     * radix falls outside the acceptable range or is otherwise considered invalid.
     *
     * @property radix The invalid radix value that triggered this error.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class InvalidRadix(val radix: Int) : NumberError
}