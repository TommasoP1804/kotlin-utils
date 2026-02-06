@file:JvmName("RangeUtilsKt")
@file:Suppress("unused")
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.numbers.Percentage
import dev.tommasop1804.kutils.exceptions.ValidationFailedException

/**
 * Calculates a value within the range based on the provided fraction.
 * The position in the range is determined by multiplying the range's span
 * by the given fraction and adding it to the start of the range.
 *
 * @param percentage A fraction between 0.0 and 1.0 (inclusive) representing the position within the range.
 * @return A value within the range corresponding to the specified fraction.
 * @throws IllegalArgumentException If the fraction is not between 0.0 and 1.0.
 * @since 1.0.0
 */
operator fun ClosedRange<Double>.invoke(percentage: Percentage): Double {
    if (percentage.isOverflowing) throw ValidationFailedException("Fraction should be between 0.0 and 100.0")
    val span = endInclusive - start
    return start + (span * percentage.toDouble(true))
}