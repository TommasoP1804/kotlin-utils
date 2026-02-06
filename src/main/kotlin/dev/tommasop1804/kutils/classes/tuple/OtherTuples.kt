@file:JvmName("OtherTuplesKt")
@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.tuple

import dev.tommasop1804.kutils.*
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * Checks if either the first or the second component of the pair is null.
 *
 * This function evaluates whether the pair contains at least one null value.
 *
 * @receiver The pair to be checked for null values.
 * @return `true` if either the first or the second component is null; `false` otherwise.
 * @since 1.0.0
 */
val Pair<*, *>.containsNullValues
    get() = first.isNull() || second.isNull()
/**
 * Checks if any of the components of the Triple are null.
 *
 * This method evaluates whether the first, second, or third value
 * in the Triple is null and returns true if at least one of them is null.
 *
 * @receiver Triple whose components are checked for null values.
 * @return `true` if any component of the Triple is null, `false` otherwise.
 * @since 1.0.0
 */
val Triple<*, *, *>.containsNullValues
    get() = first.isNull() || second.isNull() || third.isNull()

/**
 * Converts a [Pair] into a [Map.Entry].
 *
 * This function creates a singleton map with the pair as an entry and then returns the first entry
 * from the map. It is useful when a [Pair] needs to be represented in the form of a [Map.Entry].
 *
 * @receiver The [Pair] to be converted into a [Map.Entry].
 * @return A [Map.Entry] representing the given [Pair].
 * @since 1.0.0
 */
fun <K, V> Pair<K, V>.toMapEntry() = mapOf(this).entries.first()

/**
 * Transforms the components of this pair by applying the given functions.
 *
 * @param f1 A function to transform the first component of the pair.
 * @param f2 A function to transform the second component of the pair.
 * @return A new pair with the first component transformed by [f1] and the second component transformed by [f2].
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <A1, B1, A2, B2> Pair<A1, B1>.map(f1: Transformer<A1, A2> = { it as A2 }, f2: Transformer<B1, B2> = { it as B2 }): Pair<A2, B2> = Pair(f1(first), f2(second))
/**
 * Transforms each component of the Triple using the provided mapping functions.
 *
 * @param f1 a function to transform the first component of the Triple.
 * @param f2 a function to transform the second component of the Triple.
 * @param f3 a function to transform the third component of the Triple.
 * @return a new Triple with each component mapped using the corresponding function.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <A1, B1, C1, A2, B2, C2> Triple<A1, B1, C1>.map(f1: Transformer<A1, A2> = { it as A2 }, f2: Transformer<B1, B2> = { it as B2 }, f3: Transformer<C1, C2> = { it as C2 }): Triple<A2, B2, C2> =
    Triple(f1(first), f2(second), f3(third))

/**
 * Converts the `Pair` into a `Map` with specified keys.
 *
 * The resulting map will contain two entries where the `first` and `second`
 * elements of the pair are associated with the provided keys.
 *
 * @param keys A `Pair` of `String` specifying the keys to be used
 * for the `first` and `second` elements, respectively. The default
 * value is a pair of "first" to "second".
 * @return A Map with the specified keys and the corresponding values
 * from the `Pair`.
 * @since 1.0.0
 */
fun <T> MonoPair<T>.toMap(keys: String2 = "first" to "second"): Map<String, T> = mapOf(keys.first to first, keys.second to second)
/**
 * Converts a [Pair] into a [Map] using the provided keys for mapping its components.
 *
 * @param keys A pair of keys where the first key maps to the `first` component of the pair
 * and the second key maps to the `second` component of the pair.
 * @return A [Map] containing the provided keys mapped to the respective elements of the pair.
 * @since 1.0.0
 */
@JvmName("pairToMapGenericKeyType")
fun <T, K> MonoPair<T>.toMap(keys: MonoPair<K>) = mapOf(keys.first to first, keys.second to second)
/**
 * Converts the given pair into a mutable map with keys specified by the provided string pair.
 *
 * @param keys A pair of strings representing the keys to use in the resulting map.
 *             By default, the keys are "first" for the first component and "second" for the second component of the pair.
 * @since 1.0.0
 */
fun <T> MonoPair<T>.toMMap(keys: String2 = "first" to "second"): MMap<String, T> = mMapOf(keys.first to first, keys.second to second)
/**
 * Converts a `Pair` of values into a mutable map (`MMap`) using the provided keys.
 *
 * This function takes a pair of values and a pair of keys, and constructs a mutable map where
 * the first key is associated with the first value, and the second key is associated with the second value.
 *
 * @param keys a `Pair` of keys to associate with the elements of the original pair
 * @return a new `MMap` containing the key-value pairs derived from the input pair and keys
 * @since 1.0.0
 */
@JvmName("pairToMMapGenericKeyType")
fun <T, K> MonoPair<T>.toMMap(keys: MonoPair<K>) = mMapOf(keys.first to first, keys.second to second)
/**
 * Converts a [Triple] into a [Map] with specified keys for each component.
 *
 * @param keys A [String3] representing the keys to associate with the `first`, `second`, and `third` elements of the triple. By default, it uses
 * ("first" to "second") + "third".
 * @return A [Map] containing the keys mapped to their respective elements in the [Triple].
 * @since 1.0.0
 */
fun <T> MonoTriple<T>.toMap(keys: String3 = ("first" to "second") + "third"): Map<String, T> = mapOf(keys.first to first, keys.second to second, keys.third to third)
/**
 * Converts a [Triple] into a [Map] using the provided keys for each component of the triple.
 *
 * @param keys A [Triple] containing the keys to associate with the `first`, `second`, and `third` components of this triple.
 * The first element of the `keys` triple maps to the `first` component, the second to the `second`, and the third to the `third`.
 * @return A [Map] where the provided keys are associated with their respective values from the triple.
 * @since 1.0.0
 */
@JvmName("tripleToMapGenericKeyType")
fun <T, K> MonoTriple<T>.toMap(keys: MonoTriple<K>) = mapOf(keys.first to first, keys.second to second, keys.third to third)
/**
 * Converts the receiver [Triple] into a [MutableMap] with the keys defined by the provided [keys].
 * By default, the keys are "first", "second", and "third" mapped to the corresponding components of the triple.
 *
 * @param keys A [Triple] of strings representing the desired keys for the resulting map.
 * The default values are ("first" to "second") + "third".
 * @since 1.0.0
 */
fun <T> MonoTriple<T>.toMMap(keys: String3 = ("first" to "second") + "third"): MMap<String, T> = mMapOf(keys.first to first, keys.second to second, keys.third to third)
/**
 * Converts a [Triple] of values into a mutable map ([MMap]) using the provided [Triple] of keys.
 *
 * This function associates each element of the receiver [Triple] with a corresponding key from
 * the provided [Triple] of keys, creating a mutable map where the keys are defined by the input
 * `keys` parameter, and the values come from the original receiver `Triple`.
 *
 * @param keys a [Triple] containing keys to associate the elements of the receiver [Triple] with
 * @return a mutable map ([MMap]) where the elements of the receiver are paired with the provided keys
 * @since 1.0.0
 */
@JvmName("tripleToMMapGenericKeyType")
fun <T, K> MonoTriple<T>.toMMap(keys: MonoTriple<K>) = mMapOf(keys.first to first, keys.second to second, keys.third to third)

/**
 * Provides a mechanism to delegate property access to a value in a map created
 * from the current `Pair`. The property name is used as the key to fetch the
 * corresponding value from the map.
 *
 * - `first`
 * - `second`
 *
 * @param thisRef The reference to the instance on which the property is accessed.
 * @param property Metadata for the property being delegated, including its name.
 * @return The value corresponding to the property name in the map representation
 * of the `Pair`, cast to the specified type `R`.
 * @throws NoSuchElementException If the property name does not exist as a key in the map.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
operator fun <A, B, R> Pair<A, B>.getValue(thisRef: Any?, property: KProperty<*>) = toMap().getValue(property.name) as R

/**
 * Converts a Pair into a MutableList containing its two elements.
 *
 * This function creates a new MutableList and adds the `first` and `second`
 * components of the Pair as elements in the same order.
 *
 * @receiver The Pair to be converted into a MutableList.
 * @return A MutableList containing the `first` and `second` elements of the Pair.
 * @since 1.0.0
 */
fun <T> MonoPair<T>.toMList() = mListOf(first, second)
/**
 * Converts the components of a [Triple] into a [MutableList].
 *
 * This function creates a new mutable list containing the `first`, `second`,
 * and `third` values of the [Triple] in respective order.
 *
 * @return A [MutableList] containing the elements of the [Triple].
 * @since 1.0.0
 */
fun <T> MonoTriple<T>.toMList() = mListOf(first, second, third)

/**
 * Converts the Pair into a MutableList containing the first and second elements of the Pair.
 *
 * @receiver The Pair to convert into a MutableList.
 * @return A MutableList containing the first and second elements of the Pair.
 * @since 1.0.0
 */
@JvmName("toMutableListDifferentType")
fun <A, B> Pair<A, B>.toMList() = mListOf(first, second)
/**
 * Converts the components of the Triple into a MutableList.
 *
 * @receiver The Triple instance to be converted.
 * @return A MutableList containing the three components of the Triple
 *         in the order: first, second, and third.
 * @since 1.0.0
 */
@JvmName("toMutableListDifferentType")
fun <A, B, C> Triple<A, B, C>.toMList() = mListOf(first, second, third)

/**
 * Converts the components of this pair into a [Set].
 *
 * This function creates a set containing the first and second elements of the pair.
 *
 * @receiver The pair whose components are to be converted to a set.
 * @return A set containing the first and second elements of the pair.
 * @since 1.0.0
 */
fun <T> MonoPair<T>.toSet() = setOf(first, second)
/**
 * Converts the components of the Triple into a [Set].
 *
 * The resulting set contains all three elements of the Triple.
 * Duplicates will be removed as per the behavior of a [Set].
 *
 * @return A [Set] containing the unique elements of the Triple.
 * @since 1.0.0
 */
fun <T> MonoTriple<T>.toSet() = setOf(first, second, third)

/**
 * Converts a [Pair] of two elements into a [MutableSet] containing the two elements.
 *
 * This function creates a new [MutableSet] and adds the `first` and `second` elements of the pair
 * into it. The resulting set may contain one or two elements depending on whether the elements
 * are the same or different. The order of elements in the returned set is not guaranteed.
 *
 * @receiver The pair of elements to be converted into a [MutableSet].
 * @return A new [MutableSet] containing the two elements of the pair.
 * @since 1.0.0
 */
fun <T> MonoPair<T>.toMSet() = mSetOf(first, second)
/**
 * Converts the elements of the triple into a [MutableSet].
 *
 * This method creates a new [MutableSet] containing the three elements of the triple:
 * the first, second, and third values. The resulting set ensures that there are no
 * duplicate entries if the values of the triple contain duplicates.
 *
 * @return A [MutableSet] containing the elements of the triple.
 * @since 1.0.0
 */
fun <T> MonoTriple<T>.toMSet() = mSetOf(first, second, third)

/**
 * Retrieves an element from the `Pair` based on the given index.
 *
 * @param index The index of the element to retrieve. Valid values are:
 *              - `0` to retrieve the first element of the pair.
 *              - `1` to retrieve the second element of the pair.
 * @return The element at the specified index in the pair.
 * @throws IndexOutOfBoundsException If the index is not 0 or 1.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
operator fun <A, B, R> Pair<A, B>.get(index: Int): R = when(index) {
    0 -> first as R
    1 -> second as R
    else -> throw IndexOutOfBoundsException("Invalid index $index, valid range is 0..1")
}
/**
 * Retrieves the component at the specified index from the triple.
 *
 * @param index The position of the element to retrieve. Valid indices are 0, 1, and 2.
 * 0 corresponds to the first element, 1 corresponds to the second element, and 2 corresponds to the third element.
 * @return The element at the specified index.
 * @throws IndexOutOfBoundsException If the index is outside the valid range (0..2).
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
operator fun <A, B, C, R> Triple<A, B, C>.get(index: Int): R = when(index) {
    0 -> first as R
    1 -> second as R
    2 -> third as R
    else -> throw IndexOutOfBoundsException("Invalid index $index, valid range is 0..2")
}

/**
 * Creates a new [Pair] by swapping the first and second elements of the original [Pair].
 *
 * @receiver The [Pair] whose elements are to be swapped.
 * @return A new [Pair] where the first element is set to the original second element,
 *         and the second element is set to the original first element.
 * @since 1.0.0
 */
fun <A, B> Pair<A, B>.swap() = Pair(second, first)

/**
 * Combines this pair with an additional element to create a [Triple].
 *
 * @receiver The pair of elements to be extended.
 * @param other The additional element to be combined with the pair.
 * @return A [Triple] containing the first and second elements of the pair, and the additional element.
 * @since 1.0.0
 */
operator fun <A, B, C> Pair<A, B>.plus(other: C) = Triple(first, second, other)
/**
 * Combines this pair with another pair to create a quadruple.
 *
 * This function allows for merging two pairs into a single [`Quadruple`][Quadruple],
 * where the first two elements come from this pair, and the last two elements come
 * from the provided pair.
 *
 * @param A the type of the first element in this pair
 * @param B the type of the second element in this pair
 * @param C the type of the first element in the provided pair
 * @param D the type of the second element in the provided pair
 * @param other the pair to combine with this pair
 * @return a quadruple containing the elements of this pair followed by the elements of the provided pair
 * @since 1.0.0
 */
operator fun <A, B, C, D> Pair<A, B>.plus(other: Pair<C, D>) = Quadruple(first, second, other.first, other.second)
/**
 * Combines a `Pair` and a `Triple` into a `Quintuple` by merging their elements.
 *
 * The resulting `Quintuple` contains the two elements of the `Pair` followed
 * by the three elements of the `Triple`.
 *
 * @param A the type of the first element in the `Pair`
 * @param B the type of the second element in the `Pair`
 * @param C the type of the first element in the `Triple`
 * @param D the type of the second element in the `Triple`
 * @param E the type of the third element in the `Triple`
 * @param other the `Triple` to be combined with this `Pair`
 * @return a `Quintuple` containing the elements of the `Pair` and the `Triple`
 * @since 1.0.0
 */
operator fun <A, B, C, D, E> Pair<A, B>.plus(other: Triple<C, D, E>) = Quintuple(first, second, other.first, other.second, other.third)

/**
 * Subtracts an element from the pair by its index, returning the corresponding value cast to the specified type.
 *
 * @param index The index of the element to subtract. Must be 0 or 1, where 0 represents the first element and 1 represents the second element.
 * @return The element at the specified index, cast to the specified type.
 * @throws IndexOutOfBoundsException If the index is outside the valid range of 0..1.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
operator fun <A, B, R> Pair<A, B>.minus(index: Int): R = when (index) {
    0 -> first as R
    1 -> second as R
    else -> throw IndexOutOfBoundsException("Invalid index $index, valid range is 0..1")
}

/**
 * Extends a [Triple] with an additional element, creating a [Quadruple].
 *
 * This function allows combining a [Triple] of three elements and another
 * single value into a [Quadruple], effectively adding a fourth element.
 *
 * @receiver the original [Triple] of three elements
 * @param other the fourth element to be added to the [Triple]
 * @since 1.0.0
 */
operator fun <A, B, C, D> Triple<A, B, C>.plus(other: D) = Quadruple(first, second, third, other)
/**
 * Combines a [Triple] of three elements and a [Pair] of two elements into a [Quintuple] of five elements.
 *
 * @param A the type of the first element of the triple
 * @param B the type of the second element of the triple
 * @param C the type of the third element of the triple
 * @param D the type of the first element of the pair
 * @param E the type of the second element of the pair
 * @param other the pair to combine with this triple
 * @return a [Quintuple] containing all five elements from the triple and the pair
 * @since 1.0.0
 */
operator fun <A, B, C, D, E> Triple<A, B, C>.plus(other: Pair<D, E>) = Quintuple(first, second, third, other.first, other.second)

/**
 * Removes the element at the specified index from the triple and returns a pair consisting
 * of the remaining elements cast to the specified types.
 *
 * @param index The index of the element to remove from this triple. Must be in the range 0..2.
 * @return A pair where:
 * - If index is `0`, the first component is the second element of the triple, and the second component is the third element.
 * - If index is `1`, the first component is the first element of the triple, and the second component is the third element.
 * - If index is `2`, the first component is the first element of the triple, and the second component is the second element.
 * @throws IndexOutOfBoundsException if the index is not in the range 0..2.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
operator fun <A, B, C, RA, RB> Triple<A, B, C>.minus(index: Int) = when (index) {
    0 -> second as RA to third as RB
    1 -> first as RA to third as RB
    2 -> first as RA to second as RB
    else -> throw IndexOutOfBoundsException("Invalid index $index, valid range is 0..2")
}

/**
 * Swaps the components of the current `Pair`.
 * The first component becomes the second, and the second becomes the first.
 *
 * @receiver A `Pair` of two elements.
 * @return A new `Pair` with its elements swapped.
 *
 * @since 1.0.0
 */
operator fun <A, B> Pair<A, B>.not() = second to first
/**
 * Provides a custom `not` operator function for a `Triple` instance.
 * Reverses and reorders the elements of the `Triple` by swapping the third
 * and second elements, pairing them as a `Pair`, and appending the first element.
 *
 * @receiver Triple of generic types A, B, C
 * @return A new object representing the transformation of the original Triple.
 * @since 1.0.0
 */
operator fun <A, B, C> Triple<A, B, C>.not() = (third to second) + first

/**
 * Converts the first two elements of the array into a Pair.
 * If there are fewer than two elements in the array, `null` will be used for the missing values.
 *
 * @receiver the array of elements to convert to a Pair
 * @return a Pair containing the first and second elements of the array, or `null` if the elements are not present.
 * @since 1.0.0
 */
fun <E> Array<E>.toPair(): MonoPair<E?> = getOrNull(0) to getOrNull(1)
/**
 * Converts the first two elements of a list into a Pair. If the list has fewer than two elements,
 * null will be used for the missing values in the Pair.
 *
 * @receiver the list of elements to convert to a Pair.
 * @return A Pair containing the first and second elements of the list, or null for any missing elements.
 * @since 1.0.0
 */
fun <E> List<E>.toPair(): MonoPair<E?> = getOrNull(0) to getOrNull(1)

/**
 * Converts the first three elements of the array into a [Triple].
 * If the array size is less than three, the missing elements in the [Triple] will be `null`.
 *
 * @receiver the array of elements to convert to a Triple.
 * @return A [Triple] containing the first, second, and third elements of the array, or `null` for missing elements.
 * @since 1.0.0
 */
fun <E> Array<E>.toTriple(): MonoTriple<E?> = Triple(getOrNull(0), getOrNull(1), getOrNull(2))
/**
 * Converts the first three elements of the list into a [Triple]. If the list has fewer than three elements,
 * the missing values in the [Triple] will be `null`.
 *
 * @receiver the list of elements to convert to a Triple.
 * @return A [Triple] containing the first three elements of the list, or `null` for missing elements.
 * @since 1.0.0
 */
fun <E> List<E>.toTriple(): MonoTriple<E?> = Triple(getOrNull(0), getOrNull(1), getOrNull(2))

/**
 * A data class representing a tuple of four elements.
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
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
data class Quadruple<out A, out B, out C, out D> (val first: A, val second: B, val third: C, val fourth: D) : Serializable {
    /**
     * Returns a string representation of the quadruple in the format (first, second, third, fourth).
     *
     * @return a string representation of the quadruple
     * @since 1.0.0
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
     * @since 1.0.0
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

/**
 * Checks if the `Quadruple` contains any `null` values in any of its elements.
 *
 * This function evaluates each element of the `Quadruple` in order and
 * returns `true` if any of the elements (`first`, `second`, `third`, or `fourth`) is `null`.
 *
 * @receiver the `Quadruple` instance to check for null values
 * @return `true` if any element in the `Quadruple` is `null`; otherwise, `false`
 * @since 1.0.0
 */
val Quadruple<*, *, *, *>.containsNullValues
    get() = first.isNull() || second.isNull() || third.isNull() || fourth.isNull()

/**
 * Transforms the elements of this Quadruple into another Quadruple by applying the given mapping functions
 * to each element.
 *
 * @param f1 a function to transform the first element of the Quadruple
 * @param f2 a function to transform the second element of the Quadruple
 * @param f3 a function to transform the third element of the Quadruple
 * @param f4 a function to transform the fourth element of the Quadruple
 * @return a new Quadruple where each element is the result of applying the respective mapping function to the corresponding element of this Quadruple
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <A1, B1, C1, D1, A2, B2, C2, D2> Quadruple<A1, B1, C1, D1>.map(
    f1: Transformer<A1, A2> = { it as A2 },
    f2: Transformer<B1, B2> = { it as B2 },
    f3: Transformer<C1, C2> = { it as C2 },
    f4: Transformer<D1, D2> = { it as D2 }
): Quadruple<A2, B2, C2, D2> = Quadruple(f1(first), f2(second), f3(third), f4(fourth))

/**
 * Converts the array into a Quadruple containing up to the first four elements of the array.
 * If the array contains fewer than four elements, the missing positions in the Quadruple are filled with `null`.
 *
 * @receiver the array of elements to convert to a Quadruple.
 * @return A Quadruple containing the first four elements of the array or `null` for missing elements.
 * @since 1.0.0
 */
fun<E> Array<E>.toQuadruple(): MonoQuadruple<E?> = Quadruple(
    getOrNull(0), getOrNull(1), getOrNull(2), getOrNull(3)
)
/**
 * Converts the first four elements of the list into a [Quadruple].
 * If the list has less than four elements, the missing elements in the [Quadruple] will be filled with null.
 *
 * @receiver the list of elements to convert to a Quadruple.
 * @return A [Quadruple] containing the first four elements of the list, or null for missing elements.
 * @since 1.0.0
 */
fun <E> List<E>.toQuadruple(): MonoQuadruple<E?> = Quadruple(
    getOrNull(0), getOrNull(1), getOrNull(2), getOrNull(3)
)

/**
 * Converts this `Quadruple` into a map using the specified keys.
 *
 * Each element of the `Quadruple` is mapped to the corresponding key in the provided
 * `keys` quadruple. The resulting map will contain four key-value pairs where:
 * - keys.first is mapped to first
 * - keys.second is mapped to second
 * - keys.third is mapped to third
 * - keys.fourth is mapped to fourth
 *
 * @param keys a `Quadruple` of strings to be used as keys in the resulting map
 * @return a map containing the elements of this `Quadruple` paired with the provided keys
 * @since 1.0.0
 */
fun <T> MonoQuadruple<T>.toMap(keys: String4 = Quadruple("first", "second", "third", "fourth")): Map<String, T> = mapOf(
    keys.first to first,
    keys.second to second,
    keys.third to third,
    keys.fourth to fourth
)
/**
 * Converts a `Quadruple` into a `Map` by associating each element of the `Quadruple`
 * with the corresponding key provided in another `Quadruple`.
 *
 * @param T the type of the elements in the source `Quadruple`
 * @param K the type of the keys in the key `Quadruple`
 * @param keys a `Quadruple` containing the keys that will be mapped to the `first`, `second`, `third`, and `fourth` elements
 * @return a `Map` where the keys from the `keys` `Quadruple` are mapped to the corresponding elements of this `Quadruple`
 * @since 1.0.0
 */
@JvmName("quadrupleToMapGenericKey")
fun <T, K> MonoQuadruple<T>.toMap(keys: MonoQuadruple<K>) = mapOf(
    keys.first to first,
    keys.second to second,
    keys.third to third,
    keys.fourth to fourth
)
/**
 * Converts a `Quadruple` into a mutable map by associating its elements with the keys provided
 * in the given `Quadruple` of keys.
 *
 * @param keys a `Quadruple` of keys used to map the values of the receiver `Quadruple`.
 * Each key in the provided `Quadruple` is associated with its corresponding value in the receiver.
 * @return a mutable map where the provided keys are mapped to the values of the receiver `Quadruple`.
 * @since 1.0.0
 */
fun <T> MonoQuadruple<T>.toMMap(keys: String4 = Quadruple("first", "second", "third", "fourth")): MMap<String, T> = mMapOf(
    keys.first to first,
    keys.second to second,
    keys.third to third,
    keys.fourth to fourth
)
/**
 * Converts a `Quadruple` of values to a mutable map (`MMap`) using the provided `Quadruple` of keys.
 *
 * This function pairs each element in the receiver `Quadruple` with the corresponding element
 * in the provided `keys` `Quadruple`, creating a mutable map where the keys and values are
 * drawn from the respective `Quadruple` instances.
 *
 * @param keys a `Quadruple` containing the keys to use for creating the map, where each key
 * corresponds to an element in the receiver `Quadruple`.
 * @return a mutable map (`MMap`) containing key-value pairs created from the receiver `Quadruple` and the `keys` `Quadruple`.
 * @since 1.0.0
 */
@JvmName("quadrupleToMMapGenericKey")
fun <T, K> MonoQuadruple<T>.toMMap(keys: MonoQuadruple<K>) = mMapOf(
    keys.first to first,
    keys.second to second,
    keys.third to third,
    keys.fourth to fourth
)
/**
 * Converts the components of this Quadruple into a list containing the four elements in the order they appear.
 *
 * @return A list containing the elements of this Quadruple in the order of first, second, third, and fourth.
 * @since 1.0.0
 */
fun <T> MonoQuadruple<T>.toList(): List<T> = listOf(first, second, third, fourth)
/**
 * Converts the elements of the [Quadruple] into a [MutableList].
 *
 * The resulting list contains the elements of the quadruple in the following order:
 * - `first`
 * - `second`
 * - `third`
 * - `fourth`
 *
 * This function provides a convenient way to work with the quadruple elements as a mutable list.
 *
 * @receiver the [Quadruple] whose elements will be converted
 * @return a [MutableList] containing the elements of the quadruple
 * @since 1.0.0
 */
fun <T> MonoQuadruple<T>.toMList() = mListOf(first, second, third, fourth)

/**
 * Converts the elements of the receiver [Quadruple] into a [Set].
 *
 * This function transforms all four elements of the [Quadruple] into a [Set],
 * ensuring no duplicate elements are present. The order of elements in the
 * resulting set is not guaranteed.
 *
 * @receiver the [Quadruple] whose elements are to be converted into a set
 * @return a [Set] containing the unique elements of the receiver
 * @since 1.0.0
 */
fun <T> MonoQuadruple<T>.toSet() = setOf(first, second, third, fourth)
/**
 * Converts the elements of this `Quadruple` into a [MutableSet].
 *
 * The resulting [MutableSet] will contain the `first`, `second`, `third`, and `fourth` elements
 * of the `Quadruple`. Duplicate elements, if any, will be eliminated as per the properties of a set.
 *
 * @receiver the instance of `Quadruple` whose elements are to be converted into a mutable set
 * @return a [MutableSet] containing the unique elements of the `Quadruple`
 * @since 1.0.0
 */
fun <T> MonoQuadruple<T>.toMSet() = mSetOf(first, second, third, fourth)

/**
 * Provides delegated property access by fetching the value from a map representation
 * of the `Quadruple` using the property name as the key.
 *
 * The `Quadruple` is converted into a map where property names are used as keys,
 * and the corresponding values are retrieved and cast to the generic return type.
 *
 * - `first`
 * - `second`
 * - `third`
 * - `fourth`
 *
 * @param A the type of the first element in the `Quadruple`
 * @param B the type of the second element in the `Quadruple`
 * @param C the type of the third element in the `Quadruple`
 * @param D the type of the fourth element in the `Quadruple`
 * @param R the expected return type of the value fetched from the map
 * @param thisRef the reference to the owner of the delegated property (can be `null`)
 * @param property the property metadata for the delegated property
 * @return the value corresponding to the property name in the map, cast to type `R`
 * @throws NoSuchElementException if the property name is not found in the map
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
operator fun <A, B, C, D, R> Quadruple<A, B, C, D>.getValue(thisRef: Any?, property: KProperty<*>) = toMap().getValue(property.name) as R

/**
 * Combines a `Quadruple` with an additional element to create a `Quintuple`.
 *
 * This function extends a `Quadruple` by appending an additional value, resulting
 * in a new `Quintuple` that holds all five elements.
 *
 * @receiver the original quadruple of four elements
 * @param other the fifth element to be added to the resulting quintuple
 * @return a `Quintuple` containing the four elements of the receiver and the additional fifth element
 * @since 1.0.0
 */
operator fun <A, B, C, D, E> Quadruple<A, B, C, D>.plus(other: E) = Quintuple(
    first, second, third, fourth, other
)

/**
 * Removes an element from the [Quadruple] based on the specified index and returns a [Triple] containing
 * the remaining three elements.
 *
 * Valid indices are:
 * - 0, which removes the first element
 * - 1, which removes the second element
 * - 2, which removes the third element
 * - 3, which removes the fourth element
 *
 * Throws an [IndexOutOfBoundsException] if the provided index is out of range (0..3).
 *
 * @param index the index of the element to remove from the quadruple
 * @return a [Triple] containing the remaining elements after removal
 * @throws IndexOutOfBoundsException if the index is not in the range 0..3
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
operator fun <A, B, C, D, RA, RB, RC> Quadruple<A, B, C, D>.minus(index: Int) = when (index) {
    0 -> Triple(second as RA, third as RB, fourth as RC)
    1 -> Triple(first as RA, third as RB, fourth as RC)
    2 -> Triple(first as RA, second as RB, fourth as RC)
    3 -> Triple(first as RA, second as RB, third as RC)
    else -> throw IndexOutOfBoundsException("Invalid index $index, valid range is 0..3")
}

/**
 * Reverses the order of the elements in this `Quadruple`, returning a new `Quadruple`
 * where the elements are swapped as follows: the first becomes the fourth, the second becomes the third,
 * the third becomes the second, and the fourth becomes the first.
 *
 * The operation does not modify the original instance and creates a new object to represent the reversed order.
 *
 * @receiver The `Quadruple` whose elements are to be reversed.
 * @return A new `Quadruple` with the elements in reverse order.
 * @since 1.0.0
 */
operator fun <A, B, C, D> Quadruple<A, B, C, D>.not() = Quadruple(fourth, third, second, first)

/**
 * A data class representing a tuple of five elements.
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
 * @since 1.0.0
 */
data class Quintuple<out A, out B, out C, out D, out E> (val first: A, val second: B, val third: C, val fourth: D, val fifth: E) : Serializable {
    /**
     * Returns a string representation of the quintuple in the format (first, second, third, fourth, fifth).
     *
     * @return a string representation of the quintuple
     * @since 1.0.0
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
     * @since 1.0.0
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

/**
 * Indicates whether any of the elements in the quintuple are `null`.
 *
 * This property evaluates to `true` if at least one of the elements (`first`, `second`,
 * `third`, `fourth`, or `fifth`) in the quintuple is `null`; otherwise, it evaluates to `false`.
 *
 * @receiver a quintuple containing five elements
 * @return `true` if any element in the quintuple is `null`, `false` otherwise
 * @since 1.0.0
 */
val Quintuple<*, *, *, *, *>.containsNullValues
    get() = first.isNull() || second.isNull() || third.isNull() || fourth.isNull() || fifth.isNull()

@Suppress("UNCHECKED_CAST")
fun <A1, B1, C1, D1, E1, A2, B2, C2, D2, E2> Quintuple<A1, B1, C1, D1, E1>.map(
    f1: Transformer<A1, A2> = { it as A2 },
    f2: Transformer<B1, B2> = { it as B2 },
    f3: Transformer<C1, C2> = { it as C2 },
    f4: Transformer<D1, D2> = { it as D2 },
    f5: Transformer<E1, E2> = { it as E2 }
): Quintuple<A2, B2, C2, D2, E2> = Quintuple(f1(first), f2(second), f3(third), f4(fourth), f5(fifth))

/**
 * Converts the array into a [Quintuple] containing up to the first five elements of the array.
 * If the array has fewer than five elements, the missing values are replaced with `null`.
 *
 * @receiver the array of elements to convert to a [Quintuple].
 * @return a [Quintuple] containing the first five elements of the array, or `null` for any missing elements.
 * @since 1.0.0
 */
fun <E> Array<E>.toQuintuple(): MonoQuintuple<E?> = Quintuple(
    getOrNull(0), getOrNull(1), getOrNull(2), getOrNull(3), getOrNull(4)
)
/**
 * Converts the first five elements of the list into a [Quintuple]. If the list contains fewer than
 * five elements, the additional elements in the [Quintuple] will be `null`.
 *
 * @receiver the list of elements to convert to a [Quintuple].
 * @return a [Quintuple] containing up to the first five elements of the list, or `null` for missing elements.
 * @since 1.0.0
 */
fun <E> List<E>.toQuintuple(): MonoQuintuple<E?> = Quintuple(
    getOrNull(0), getOrNull(1), getOrNull(2), getOrNull(3), getOrNull(4)
)

/**
 * Converts a [Quintuple] to a map by associating its elements with the corresponding keys
 * from another [Quintuple] of keys.
 *
 * @param keys a [Quintuple] containing the keys for the map
 * @return a map where the keys are taken from the `keys` quintuple and the values
 *         are the corresponding elements of the original quintuple
 * @since 1.0.0
 */
fun <T> MonoQuintuple<T>.toMap(keys: String5 = Quintuple("first", "second", "third", "fourth", "fifth")): Map<String, T> = mapOf(
    keys.first to first,
    keys.second to second,
    keys.third to third,
    keys.fourth to fourth,
    keys.fifth to fifth
)
/**
 * Converts a [Quintuple] of values into a [Map] using the specified keys.
 *
 * Each element in the quintuple is associated with a corresponding key
 * from the provided `keys` quintuple, creating key-value pairs in the resulting map.
 *
 * @param keys A [Quintuple] containing the keys to associate with the elements in this [Quintuple].
 * The order of keys corresponds to the order of the elements: `first`, `second`, `third`, `fourth`, and `fifth`.
 * @return A [Map] where each key from the provided `keys` is associated with its respective element in this [Quintuple].
 * @since 1.0.0
 */
@JvmName("quintupleToMapGenericKeyType")
fun <T, K> MonoQuintuple<T>.toMap(keys: MonoQuintuple<K>) = mapOf(
    keys.first to first,
    keys.second to second,
    keys.third to third,
    keys.fourth to fourth,
    keys.fifth to fifth
)
/**
 * Converts a [Quintuple] to a mutable map using the given keys.
 *
 * The function requires a [Quintuple] of keys that will be used as map keys,
 * pairing each key with the corresponding value from the original quintuple.
 *
 * @param keys a quintuple of strings representing the keys for the resulting map
 * @return a [MutableMap] where each element of the quintuple is mapped to its corresponding key
 * @since 1.0.0
 */
fun <T> MonoQuintuple<T>.toMuMap(keys: String5 = Quintuple("first", "second", "third", "fourth", "fifth")): MMap<String, T> = mMapOf(
    keys.first to first,
    keys.second to second,
    keys.third to third,
    keys.fourth to fourth,
    keys.fifth to fifth
)
/**
 * Transforms a Quintuple of values into an MMap using the provided Quintuple of keys.
 *
 * This function pairs each value from the invoking Quintuple with a corresponding
 * key from the provided Quintuple, creating an MMap containing these key-value pairs.
 *
 * @param keys a Quintuple containing the keys to be paired with the values of this Quintuple
 * @return an MMap where each key in the provided Quintuple is paired with the corresponding value
 *         from this Quintuple
 * @since 1.0.0
 */
fun <T, K> MonoQuintuple<T>.toMMap(keys: MonoQuintuple<K>): MMap<K, T> = mMapOf(
    keys.first to first,
    keys.second to second,
    keys.third to third,
    keys.fourth to fourth,
    keys.fifth to fifth
)
/**
 * Converts the components of this Quintuple into a list containing the five elements
 * in the order they appear.
 *
 * @receiver the [Quintuple] to convert to a list.
 * @return A list containing the elements of this Quintuple in the order of first, second, third, fourth, and fifth.
 * @since 1.0.0
 */
fun <T> MonoQuintuple<T>.toList(): List<T> = listOf(first, second, third, fourth, fifth)
/**
 * Converts the elements of this `Quintuple` into a `MutableList`.
 *
 * The resulting list contains the first, second, third, and fourth elements
 * of the quintuple in that order.
 *
 * @return a `MutableList` containing the elements of the quintuple
 * @since 1.0.0
 */
fun <T> MonoQuintuple<T>.toMList() = mListOf(first, second, third, fourth)

/**
 * Converts a [Quintuple] instance into a [Set] containing its unique elements.
 *
 * The returned set will include the elements `first`, `second`, `third`,
 * and `fourth` from the [Quintuple]. Duplicate elements, if any, will
 * be removed as per the properties of a set.
 *
 * @return a [Set] containing the unique elements of this [Quintuple]
 * @since 1.0.0
 */
fun <T> MonoQuintuple<T>.toSet() = setOf(first, second, third, fourth)
/**
 * Converts the first four elements of the `Quintuple` into a mutable set.
 *
 * This function creates a new `MutableSet` instance containing the values of the
 * `first`, `second`, `third`, and `fourth` elements of the quintuple. If any of the
 * values are duplicates, the set will automatically handle them and only store unique elements.
 *
 * @return a `MutableSet` containing up to four unique elements from the quintuple.
 * @since 1.0.0
 */
fun <T> MonoQuintuple<T>.toMSet() = mSetOf(first, second, third, fourth)

/**
 * Retrieves the value corresponding to the property name from a quintuple converted to a map.
 *
 * This function is used as a delegate to dynamically retrieve a value of type `R` from the quintuple
 * using the property's name as the key. The quintuple is implicitly converted to a map internally.
 *
 * - `first`
 * - `second`
 * - `third`
 * - `fourth`
 * - `fifth`
 *
 * @param thisRef the object for which the value is being retrieved, or `null` if the property is not associated with an object
 * @param property the property whose name is used as the key to look up the value in the map
 * @return the value of type `R` associated with the property name in the map
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
operator fun <A, B, C, D, E, R> Quintuple<A, B, C, D, E>.getValue(thisRef: Any?, property: KProperty<*>) = toMap().getValue(property.name) as R

/**
 * Removes the element at the specified index from the quintuple and returns a quadruple
 * containing the remaining elements.
 *
 * The index determines which element is removed:
 * - Index 0: Removes the first element.
 * - Index 1: Removes the second element.
 * - Index 2: Removes the third element.
 * - Index 3: Removes the fourth element.
 * - Index 4: Removes the fifth element.
 *
 * @param index the index of the element to remove (range 0..4)
 * @return a quadruple containing the remaining elements after the removal
 * @throws IndexOutOfBoundsException if the index is outside the range 0..4
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
operator fun <A, B, C, D, E, RA, RB, RC, RD> Quintuple<A, B, C, D, E>.minus(index: Int) = when (index) {
    0 -> Quadruple(second as RA, third as RB, fourth as RC, fifth as RD)
    1 -> Quadruple(first as RA, third as RB, fourth as RC, fifth as RD)
    2 -> Quadruple(first as RA, second as RB, fourth as RC, fifth as RD)
    3 -> Quadruple(first as RA, second as RB, third as RC, fifth as RD)
    4 -> Quadruple(first as RA, second as RB, third as RC, fourth as RD)
    else -> throw IndexOutOfBoundsException("Invalid index $index, valid range is 0..4")
}

/**
 * Reverses the order of the elements in the Quintuple. The `fifth` element becomes the `first`,
 * the `fourth` becomes the `second`, the `third` remains in the middle, and so on.
 *
 * @receiver The Quintuple whose elements' order will be reversed.
 * @return A new Quintuple with the reversed order of elements.
 * @since 1.0.0
 */
operator fun <A, B, C, D, E> Quintuple<A, B, C, D, E>.not() = Quintuple(fifth, fourth, third, second, first)