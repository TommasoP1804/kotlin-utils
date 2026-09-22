/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

/**
 * Represents an error associated with an enumeration value (`enum`).
 * This class serves as the base for describing problems related to specific
 * enumerations, providing a structured way to handle cases where enumeration
 * values are involved.
 *
 * @property enum The enumeration instance that is the source or context of the error.
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class EnumError(open val enum: Enum<*>) {
    /**
     * Represents an error occurring when a specified entry is not found within an enumeration.
     *
     * This data class is a specialized type of [EnumError] designed to provide additional context
     * about a missing entry in an enum-based operation. It includes the enumeration type and the
     * specific entry that could not be located.
     *
     * @property enum The enumeration type in which the entry lookup was attempted.
     * @property entry The name of the entry that was not found.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class NoSuchEntry(override val enum: Enum<*>, val entry: String) : EnumError(enum), ParsingError
}