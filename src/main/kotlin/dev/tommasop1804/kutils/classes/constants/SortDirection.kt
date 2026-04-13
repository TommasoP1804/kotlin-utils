/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.constants

import dev.tommasop1804.kutils.*

/**
 * Enum class representing the direction of sorting order.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
enum class SortDirection(val operators: Set<String>, val symbol: Char) {
    /**
     * Represents an ascending sorting order within the SortDirection enumeration.
     *
     * This value is typically used to indicate that items should be sorted
     * in increasing order, from the smallest or earliest value to the largest or latest value.
     *
     * @since 1.0.0
     */
    ASCENDING(setOf("as", "asc", "ascending"), symbol = '↑'),
    /**
     * Represents the descending sorting order.
     *
     * This enum value is used for operations that require data to be sorted
     * in a descending manner, i.e., from largest to smallest or most recent to oldest.
     *
     * @since 1.0.0
     */
    DESCENDING(setOf("de", "des", "desc", "descending"), symbol = '↓');

    companion object {
        /**
         * Finds and returns an entry within the enum's `entries` collection
         * where the given operator matches one of the available `operators`
         * for that entry. The comparison is case-insensitive.
         *
         * @param operator The string operator to search for within the `operators`
         * list of the entries in the enum. The operator will be converted to lowercase
         * during the comparison.
         * @return The matching enum entry, or `null` if no match is found.
         * @since 1.0.0
         */
        infix fun ofOperator(operator: String) = entries
            .find { -operator in it.operators }

        /**
         * Finds and returns an entry within the enum's `entries` collection where the symbol matches
         * the given `symbol` parameter.
         *
         * @param symbol The character symbol to search for within the `entries` of the enum.
         * @return The matching enum entry, or `null` if no match is found.
         * @since 3.0.2
         */
        infix fun ofSymbol(symbol: Char) = entries.find { it.symbol == symbol }
    }
    
    /**
     * Provides the first component of the `SortDirection` class for destructuring declarations.
     *
     * This function enables the `operators` property of `SortDirection` to be accessed 
     * in a concise manner during destructuring. It is particularly useful when 
     * working with tuples or extracting specific data from the `SortDirection` class.
     *
     * @return The `operators` property of the `SortDirection` class.
     * @since 3.1.0
     */
    operator fun component1() = operators
    /**
     * Provides the symbol representation of the `SortDirection` class as the second component in destructuring declarations.
     *
     * This function is an operator function that supports destructuring of `SortDirection` instances,
     * allowing access to the `symbol` property as the second component.
     *
     * @return The symbol representing the `SortDirection` instance.
     * @since 3.1.0
     */
    operator fun component2() = symbol
}
