package dev.tommasop1804.kutils.classes.constants

import dev.tommasop1804.kutils.unaryMinus

/**
 * Enum class representing the direction of sorting order.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
enum class SortDirection(vararg val operators: String) {
    /**
     * Represents an ascending sorting order within the SortDirection enumeration.
     *
     * This value is typically used to indicate that items should be sorted
     * in increasing order, from the smallest or earliest value to the largest or latest value.
     *
     * @since 1.0.0
     */
    ASCENDING("as", "asc", "ascending"),
    /**
     * Represents the descending sorting order.
     *
     * This enum value is used for operations that require data to be sorted
     * in a descending manner, i.e., from largest to smallest or most recent to oldest.
     *
     * @since 1.0.0
     */
    DESCENDING("de", "des", "desc", "descending");

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
    }
}
