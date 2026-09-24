/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

import kotlin.reflect.KClass

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
open class EnumError(open val enum: KClass<out Enum<*>>) {
    /**
     * Represents an error that occurs when a specific entry is not found within an enumeration.
     *
     * This data class provides detailed information about the missing entry, associating it with
     * the corresponding enumeration (`enum`) from which the entry was expected. It extends the
     * [EnumError] class to categorize this as an error related to enumeration handling. Additionally,
     * it implements the [ParsingError] interface, indicating that this error qualifies as a specific
     * parsing issue.
     *
     * @property enum The enumeration associated with the missing entry.
     * @property entry The name of the entry within the enumeration that was not found.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class NoSuchEntry(override val enum: KClass<out Enum<*>>, val entry: String) : EnumError(enum), ParsingError
}