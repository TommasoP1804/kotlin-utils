/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.constants

import dev.tommasop1804.kutils.*

/**
 * Represents the sign of a number as an enumeration.
 *
 * This enum provides three values (`Positive`, `Negative`, and `Zero`)
 * to denote the mathematical sign of a numeric value.
 * @author Tommaso Pastorelli
 * @since 6.1.0
 */
enum class NumberSign {
    /**
     * Represents the positive value in the `NumberSign` enumeration.
     *
     * This constant is used to identify numbers with a positive sign.
     *
     * @see NumberSign
     * @since 6.1.0
     */
    Positive,
    /**
     * Represents the negative sign in the `NumberSign` enum.
     *
     * This constant is used to indicate that a number is negative.
     *
     * @see NumberSign
     * @since 6.1.0
     */
    Negative,
    /**
     * Represents the numeric value zero in the `NumberSign` enumeration.
     *
     * This value is used to signify a neutral numeric state where the number equals zero.
     *
     * `Zero` is one of the constants within `NumberSign`, alongside `Positive` and `Negative`,
     * which collectively categorize numbers based on their sign.
     *
     * @since 6.1.0
     */
    Zero,
    /**
     * Represents a Not-a-Number (NaN) state within the `NumberSign` enumeration.
     *
     * This constant is used to identify cases where numeric values are undefined or
     * cannot be assigned a specific sign (positive, negative, or zero).
     *
     * `NaN` extends the standard options in the `NumberSign` enum to handle edge
     * cases involving undefined or invalid numeric states, such as calculations
     * resulting in a `Double.NaN`.
     *
     * It is included alongside `Positive`, `Negative`, and `Zero` as part of the
     * complete set of values in `NumberSign`.
     *
     * @since 6.1.0
     */
    NaN;

    companion object {
        /**
         * Determines the sign of the given number and maps it to a corresponding `NumberSign` value.
         *
         * @param number the numeric value whose sign is to be determined.
         * @return `NumberSign.Positive` if the number is positive,
         *         `NumberSign.Negative` if it is negative,
         *         or `NumberSign.Zero` if it is neither.
         * @since 6.1.0
         */
        infix fun from(number: Number) = when (number.toDouble().signum) {
            -1.0 -> Negative
            0.0 -> Zero
            1.0 -> Positive
            else -> NaN
        }
    }
}