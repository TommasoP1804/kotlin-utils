/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.constants

import org.jetbrains.exposed.v1.core.Table

/**
 * Represents the biological classification of sex, commonly used to define male and female.
 *
 * This enum provides a symbolic representation for each category.
 *
 * @constructor Defines a Sex with its respective symbol.
 * @param symbol The Unicode character symbol representing the sex type.
 * @since 1.0.0
 */
@MustUseReturnValues
@Suppress("unused")
enum class Sex(val symbol: Char) {
    /**
     * Represents the male sex with its associated symbol.
     *
     * This enum constant is a member of the `Sex` enumeration.
     * It is typically used to distinguish male gender in contexts
     * where a symbol or gender specification is required.
     *
     * @since 4.0.0
     */
    Male('♂'),
    /**
     * Represents the female sex with the corresponding symbol.
     *
     * @since 4.0.0
     */
    Female('♀');

    companion object {
        /**
         * Maps a given character to its corresponding `Sex` enum constant.
         *
         * This method evaluates the input character and returns the `MALE` or `FEMALE` constant
         * based on predefined mappings. If the character does not match any of the specified symbols,
         * the function returns `null`.
         *
         * @param char The character to map to a `Sex` enum constant. Acceptable values include:
         * - '♂', 'M', 'm' for `MALE`
         * - '♀', 'F', 'f' for `FEMALE`
         * @return The corresponding `Sex` enum constant (`MALE` or `FEMALE`), or `null` if no match is found.
         * @since 1.0.0
         */
        infix fun of(char: Char) = when (char) {
            '♂', 'M', 'm' -> Male
            '♀', 'F', 'f' -> Female
            else -> null
        }

        /**
         * Maps a table column to the `Sex` enum using either the enum's name
         * or ordinal for database storage, based on the provided configuration.
         *
         * @param name The name of the table column to map to the `Sex` enum.
         * @param byName A flag indicating whether the enum should be mapped using its name.
         *               If `true`, the mapping uses the enum's name.
         *               If `false`, the mapping uses the enum's ordinal instead.
         * @since 5.5.0
         */
        fun Table.sex(name: String, byName: Boolean = true) =
            if (byName) enumerationByName<Sex>(name, 6) else enumeration<Sex>(name)
    }

    /**
     * Provides the symbol representation of the `Sex` enum as a component function for destructuring declarations.
     *
     * This function allows instances of the `Sex` enum to be destructured into their `symbol` property values,
     * enabling a more concise way to access the internal representation.
     *
     * @return The Unicode character symbol representing the `Sex` type.
     * @since 3.1.0
     */
    operator fun component1() = symbol
}