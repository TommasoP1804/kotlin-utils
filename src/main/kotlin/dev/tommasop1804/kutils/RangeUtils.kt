/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("RangeUtilsKt")
@file:Suppress("unused")
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.numbers.*
import dev.tommasop1804.kutils.exceptions.*

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

/**
 * Extracts the starting value of the `ClosedRange` as the first component.
 * This function is commonly used in destructuring declaration syntax.
 *
 * @receiver The `ClosedRange` from which the starting value is extracted.
 * @return The start value of the `ClosedRange`.
 * @since 3.1.0
 */
operator fun <T : Comparable<T>> ClosedRange<T>.component1() = start
/**
 * Destructuring operator function that retrieves the upper bound of the range.
 *
 * This function is used as part of a destructuring declaration to access the `endInclusive` property
 * of a `ClosedRange` instance, which represents the inclusive upper limit of the range.
 *
 * @receiver A `ClosedRange` object from which the upper bound will be extracted.
 * @return The `endInclusive` value of the given range.
 * @since 3.1.0
 */
operator fun <T : Comparable<T>> ClosedRange<T>.component2() = endInclusive
/**
 * Retrieves the starting value of the `OpenEndRange`.
 * 
 * This operator function allows destructuring of an `OpenEndRange` instance, 
 * providing direct access to its starting element as the first component.
 * 
 * @receiver The range from which the starting value is extracted.
 * @return The starting value of the `OpenEndRange`.
 * @since 3.1.0
 */
operator fun <T : Comparable<T>> OpenEndRange<T>.component1() = start
/**
 * Operator function that provides destructuring support for the endExclusive property
 * of an OpenEndRange. When used in a destructuring declaration, this function allows
 * the endExclusive value to be retrieved as the second component.
 *
 * @receiver An OpenEndRange of a type that implements Comparable.
 * @return The endExclusive value of the OpenEndRange.
 * @since 3.1.0
 */
operator fun <T : Comparable<T>> OpenEndRange<T>.component2() = endExclusive
/**
 * Returns the first value of the IntRange.
 * 
 * This function allows destructuring declarations to access
 * the first element of an integer range.
 *
 * @receiver The integer range from which the first element is extracted.
 * @return The first integer in the range.
 * @since 3.1.0
 */
operator fun IntRange.component1() = first
/**
 * Returns the upper bound of the IntRange.
 *
 * This operator function allows destructuring declaration 
 * to extract the last element of the IntRange as the second component.
 *
 * @receiver An instance of IntRange.
 * @return The last value of the IntRange.
 * @since 3.1.0
 */
operator fun IntRange.component2() = last
/**
 * Returns the first value of the `LongRange`.
 * This function allows destructuring declarations to be used with `LongRange`,
 * where the first component corresponds to the starting value of the range.
 *
 * @receiver The `LongRange` from which the first value is extracted.
 * @return The starting value of the range (`first`).
 * @since 3.1.0
 */
operator fun LongRange.component1() = first
/**
 * Returns the upper bound (inclusive) of the range.
 *
 * This function is a component operator function, allowing destructuring
 * declarations to retrieve the last value of the range.
 *
 * @receiver The range from which the last value is extracted.
 * @return The last value of the range.
 * @since 3.1.0
 */
operator fun LongRange.component2() = last