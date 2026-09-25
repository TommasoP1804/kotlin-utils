/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

import dev.tommasop1804.kutils.classes.constants.*
import dev.tommasop1804.kutils.exceptions.*

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
    val number: Number

    /**
     * Represents an error condition where a numeric value has an invalid sign.
     *
     * This data class is used to capture scenarios in numerical operations
     * where a specific sign is expected but an invalid sign is encountered.
     *
     * @property number The numeric value whose sign is invalid in the given context, or `null` if no number is specified.
     * @property validSignes A set of valid `NumberSign` states applicable to the current context, or `null` if not applicable.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class InvalidSign(override val number: Number, val validSignes: Set<NumberSign> = emptySet()) : NumberError, ValidationError {
        override val linkedException = NumberSignException(number, validSignes)
    }

    /**
     * Represents an error that occurs when an invalid radix is provided for operations
     * involving numerical processing or conversion.
     *
     * @property invalidRadix The radix value that is deemed invalid.
     * @property validRange The optional range of valid radix values, if applicable.
     *                      This provides additional context about the acceptable values.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class InvalidRadix(val invalidRadix: Int, val validRange: IntRange? = null) : NumberError, ValidationError {
        override val linkedException = NumberOutOfRangeException(invalidRadix, validRange)
        override val number = invalidRadix
    }

    /**
     * Represents an error condition where a percentage value exceeds expected bounds.
     *
     * This interface specializes in handling scenarios where percentages, typically
     * expressed as [0-100%], overflow their valid range and result in numerical errors.
     * Subtypes of this interface capture whether the overflow occurs in the
     * positive or negative direction.
     *
     * @property percentage The percentage value that caused the overflow condition.
     *                      This can help provide insight into the magnitude
     *                      and context of the overflow.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    interface PercentageOverflow : NumberError, ValidationError {
        /**
         * Represents the percentage value that caused an overflow in calculations.
         *
         * This property holds a numeric value, expressed as a [Double], which exceeds
         * the acceptable or defined range. It is typically used in conjunction with
         * errors or exceptions that handle cases of numeric overflow.
         *
         * @see PercentageOverflow
         * @since 6.1.0
         */
        val percentage: Double
    }
    /**
     * Represents an overflow error indicating that a percentage value exceeds
     * the allowable range, specifically for positive percentages.
     *
     * This class is a specialized type of [PercentageOverflow], capturing
     * errors where the percentage value is positive but surpasses the
     * permissible limits.
     *
     * @property percentage The positive percentage value that caused the overflow error.
     * @see PercentageOverflow
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class PositivePercentageOverflow(override val percentage: Double) : PercentageOverflow {
        override val linkedException = MalformedInputException("The percentage must be lower than or equal to 100")
        override val number = percentage
    }
    /**
     * Represents an error condition where a negative percentage exceeds an acceptable limit.
     *
     * This data class is a specific implementation of the `PercentageOverflow` interface,
     * designed to capture scenarios in which a percentage value being processed is
     * excessively negative, violating expected constraints.
     *
     * @property percentage The negative percentage value that caused the overflow error.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class NegativePercentageOverflow(override val percentage: Double) : PercentageOverflow {
        override val linkedException = MalformedInputException("The percentage must be greater than or equal to 0")
        override val number = percentage
    }
}