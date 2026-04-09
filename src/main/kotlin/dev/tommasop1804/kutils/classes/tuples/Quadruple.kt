/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.tuples

import java.io.Serializable

/**
 * A data class representing a tuples of four elements.
 *
 * This can be useful for grouping four related values together and passing them
 * as a single object. The class provides component functions to destructure
 * the elements and is also Serializable.
 *
 * @param A the type of the first element
 * @param B the type of the second element
 * @param C the type of the third element
 * @param D the type of the fourth element
 * @property first the first element of the quadruple
 * @property second the second element of the quadruple
 * @property third the third element of the quadruple
 * @property fourth the fourth element of the quadruple
 * @since 1.1.0
 * @author Tommaso Pastorelli
 */
data class Quadruple<out A, out B, out C, out D> (val first: A, val second: B, val third: C, val fourth: D) : Serializable {
    /**
     * Returns a string representation of the quadruple in the format (first, second, third, fourth).
     *
     * @return a string representation of the quadruple
     * @since 1.1.0
     */
    override fun toString(): String = "($first, $second, $third, $fourth)"

    /**
     * Retrieves an element of the quadruple by its index.
     *
     * Valid indices are:
     * - 0 for the first element
     * - 1 for the second element
     * - 2 for the third element
     * - 3 for the fourth element
     *
     * Throws an [IndexOutOfBoundsException] if the provided index is out of range.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is not in the range 0..3
     * @since 1.1.0
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <R> get(index: Int) = when(index) {
        0 -> first as R
        1 -> second as R
        2 -> third as R
        3 -> fourth as R
        else -> throw IndexOutOfBoundsException("Invalid index $index, valid range is 0..3")
    }
}