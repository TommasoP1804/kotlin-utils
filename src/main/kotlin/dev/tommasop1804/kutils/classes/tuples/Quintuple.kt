/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.tuples

import java.io.Serializable

/**
 * A data class representing a tuples of five elements.
 *
 * This can be useful for grouping five related values together and passing them
 * as a single object. The class provides component functions to destructure
 * the elements and is also Serializable.
 *
 * @param A the type of the first element
 * @param B the type of the second element
 * @param C the type of the third element
 * @param D the type of the fourth element
 * @param E the type of the fifth element
 * @property first the first element of the quintuple
 * @property second the second element of the quintuple
 * @property third the third element of the quintuple
 * @property fourth the fourth element of the quintuple
 * @property fifth the fifth element of the quintuple
 * @author Tommaso Pastorelli
 * @since 1.1.0
 */
@MustUseReturnValues
data class Quintuple<out A, out B, out C, out D, out E> (val first: A, val second: B, val third: C, val fourth: D, val fifth: E) : Serializable {
    /**
     * Returns a string representation of the quintuple in the format (first, second, third, fourth, fifth).
     *
     * @return a string representation of the quintuple
     * @since 1.1.0
     */
    override fun toString(): String = "($first, $second, $third, $fourth, $fifth)"

    /**
     * Retrieves the element at the specified index from the quintuple.
     *
     * The valid index range is 0 to 4 inclusive:
     * - 0 corresponds to the first element.
     * - 1 corresponds to the second element.
     * - 2 corresponds to the third element.
     * - 3 corresponds to the fourth element.
     * - 4 corresponds to the fifth element.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is outside the range 0..4
     * @since 1.1.0
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <R> get(index: Int): R = when(index) {
        0 -> first as R
        1 -> second as R
        2 -> third as R
        3 -> fourth as R
        4 -> fifth as R
        else -> throw IndexOutOfBoundsException("Invalid index $index, valid range is 0..4")
    }
}