@file:JvmName("RangesWithExclusionsKt")
@file:Suppress("unused", "unchecked_cast")
@file:Since("1.0.0")

package dev.tommasop1804.kutils.classes.range

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.Since

/**
 * Represents a closed range `[start, endInclusive]` with additional exclusions.
 * This class extends the `ClosedRange` functionality, allowing certain elements
 * within the range to be excluded explicitly.
 *
 * @param T The type of elements in the range. It must implement the `Comparable` interface.
 * @property start The start boundary of the range, inclusive.
 * @property endInclusive The end boundary of the range, inclusive.
 * @property excluded A mutable list of elements explicitly excluded from the range.
 * @since 1.0.0
 * @author Tomaso Pastorelli
 */
class ClosedRangeWithExclusions<T : Comparable<T>>(
    override val start: T,
    override val endInclusive: T,
    val excluded: MList<T> = emptyMList()
) : ClosedRange<T> {

    companion object {
        /**
         * Creates a new `ClosedRangeWithExclusions` by excluding the specified element from the current range.
         *
         * This function generates a `ClosedRangeWithExclusions` instance based on the given `ClosedRange` and
         * the element to be excluded, ensuring the element is not considered within the range when queried.
         *
         * @param T the type of elements within the range, constrained to be `Comparable`.
         * @param element the element to be excluded from the range.
         * @since 1.0.0
         */
        infix fun <T: Comparable<T>> ClosedRange<T>.exclude(element: T) =
            ClosedRangeWithExclusions(start, endInclusive, element.asSingleMList())

        /**
         * Creates a new ClosedRangeWithExclusions instance by excluding the specified elements
         * from the current range. The excluded elements are stored and will not be considered
         * as part of the range during operations like `contains`.
         *
         * @param T the type of elements in the range, must implement Comparable.
         * @param elements a variable number of elements to be excluded from the range.
         * @since 1.0.0
         */
        fun <T: Comparable<T>> ClosedRange<T>.exclude(vararg elements: T) =
            ClosedRangeWithExclusions(start, endInclusive, elements.toMList() as MList<T>)

        /**
         * Creates a new `ClosedRangeWithExclusions` object by excluding specific elements from the current `ClosedRange`.
         * The resulting range retains the original boundaries but excludes the specified elements, which will not be
         * members of the range when checking using the `contains` operator.
         *
         * @param elements An iterable collection of elements to be excluded from the range.
         * @since 1.0.0
         */
        infix fun <T: Comparable<T>> ClosedRange<T>.exclude(elements: Iterable<T>) =
            ClosedRangeWithExclusions(start, endInclusive, elements.toMList())
    }

    /**
     * Checks whether the specified value lies within the closed range, excluding any explicitly
     * defined excluded values. The method validates if the value is greater than or equal to
     * the range start, less than or equal to the range end, and not contained in the excluded values.
     *
     * @param value The value to check for inclusion in the range.
     * @return `true` if the value is within the range and not in the excluded values, otherwise `false`.
     * @since 1.0.0
     */
    override operator fun contains(value: T) = value in start..endInclusive && value !in excluded

    /**
     * Checks if the range is empty based on its `start` and `endInclusive` values.
     *
     * This method determines if the range has no valid elements by comparing `start` and `endInclusive`.
     * Note that it does not account for any excluded elements that might invalidate the range.
     *
     * @return `true` if the range is considered empty; `false` otherwise.
     * @since 1.0.0
     */
    @CannotCheckExcludedElements
    override fun isEmpty() = start > endInclusive

    /**
     * Adds the specified elements to the list of exclusions in the range. These elements will be
     * excluded from inclusion checks when verifying containment within the range.
     *
     * @param elements The elements to be added to the exclusions list.
     * @since 1.0.0
     */
    fun exclude(vararg elements: T) {
        excluded.addAll(elements)
    }
    /**
     * Adds all the specified elements to the exclusions list of the range.
     *
     * This method ensures that the provided elements will be excluded when checking
     * for membership within the range. The specified elements are added to the internal
     * list of exclusions.
     *
     * @param elements The elements to be added to the exclusions list.
     * @since 1.0.0
     */
    infix fun exlude(elements: Iterable<T>) {
        excluded.addAll(elements)
    }
    /**
     * Removes the specified element from the exclusions of the current range.
     *
     * @param element The element to be excluded from the range.
     * @since 1.0.0
     */
    operator fun minus(element: T) {
        excluded.add(element)
    }
    /**
     * Removes the specified elements from the exclusions of the current range.
     *
     * @param elements The elements to be excluded from the range. These elements will
     *                 no longer be considered part of the range.
     * @since 1.0.0
     */
    operator fun minus(elements: Iterable<T>) {
        excluded.addAll(elements)
    }

    /**
     * Removes the specified element from the list of excluded elements.
     *
     * @param element The element to be removed from the exclusions list.
     * @since 1.0.0
     */
    infix fun removeExclusion(element: T) {
        excluded.remove(element)
    }
    /**
     * Removes the specified elements from the set of excluded items in the range.
     *
     * @param elements The elements to be removed from the exclusions.
     * @since 1.0.0
     */
    fun removeExclusions(vararg elements: T) {
        excluded.removeAll(elements.toSet())
    }
    /**
     * Removes the specified elements from the exclusions list.
     *
     * @param elements The elements to remove from the list of exclusions.
     * @since 1.0.0
     */
    infix fun removeExclusions(elements: Iterable<T>) {
        excluded.removeAll(elements.toSet())
    }

    /**
     * Clears all elements from the `excluded` list, effectively removing all exclusions
     * from the current range. After invoking this method, the range will contain all values
     * between `start` and `endInclusive`.
     *
     * @since 1.0.0
     */
    fun clearExclusions() {
        excluded.clear()
    }
}

/**
 * A class representing a closed range of comparable values that includes additional conditions
 * to exclude specific values from the range.
 *
 * The `ClosedRangeWithConditions` class extends Kotlin's `ClosedRange` interface, but adds the ability
 * to filter out values that meet certain conditions, using a collection of predicates. This is useful
 * when working with ranges where some values need to be explicitly excluded.
 *
 * @param T The type of the elements in the range, which must implement the `Comparable` interface.
 * @property start The lower boundary of the range (inclusive).
 * @property endInclusive The upper boundary of the range (inclusive).
 * @property conditions A modifiable collection of predicates used to define additional conditions for excluding
 * specific values from the range.
 * @since 1.0.0
 * @author Tomaso Pastorelli
 */
class ClosedRangeWithConditions<T : Comparable<T>>(
    override val start: T,
    override val endInclusive: T,
    val conditions: MList<Predicate<T>> = emptyMList()
) : ClosedRange<T> {

    companion object {
        /**
         * Excludes elements from the ClosedRange based on the specified condition.
         *
         * This function creates and returns a new instance of `ClosedRangeWithConditions` containing
         * the range [start, endInclusive], while marking some elements within the range as excluded
         * based on the provided predicate condition.
         *
         * @param T the type of elements in the ClosedRange. Must implement `Comparable<T>`.
         * @param condition the predicate to determine which elements should be excluded
         *                  from the range.
         * @since 1.0.0
         */
        infix fun <T: Comparable<T>> ClosedRange<T>.exclude(condition: Predicate<T>) =
            ClosedRangeWithConditions(start, endInclusive, condition.asSingleMList())

        /**
         * Creates a new `ClosedRangeWithConditions` instance by excluding the specified conditions
         * within the current range.
         *
         * @param conditions Vararg array of predicates that define the conditions to exclude
         *                   from the new range.
         * @since 1.0.0
         */
        fun <T: Comparable<T>> ClosedRange<T>.exclude(vararg conditions: Predicate<T>) =
            ClosedRangeWithConditions(start, endInclusive, conditions.toMList() as MList<Predicate<T>>)

        /**
         * Extends the functionality of a `ClosedRange` by creating a `ClosedRangeWithConditions`
         * instance that excludes certain elements based on specified predicates.
         *
         * The method takes the start and end points of the range and applies the given
         * conditions (predicates) to exclude elements from the resulting range object.
         *
         * @param T The type of elements in the range, which must implement the `Comparable` interface.
         * @param conditions A collection of predicates that specify the conditions for excluding
         * elements within this range.
         * @since 1.0.0
         */
        infix fun <T: Comparable<T>> ClosedRange<T>.exclude(conditions: Iterable<Predicate<T>>) =
            ClosedRangeWithConditions(start, endInclusive, conditions.toMList())
    }

    /**
     * Checks if the specified value is contained within the range, considering additional conditions.
     *
     * The method verifies if the value lies between `start` and `endInclusive` (inclusive) of the range
     * and that none of the conditions specified in the range exclude the value.
     *
     * @param value The value to be checked for existence within the range.
     * @return `true` if the value is within the range and not excluded by any condition; `false` otherwise.
     * @since 1.0.0
     */
    override operator fun contains(value: T) = value in start..endInclusive && conditions.none { it(value) }

    /**
     * Checks whether the range has no valid elements based on its `start` and `endInclusive` bounds.
     *
     * A range is considered empty if its `start` value is greater than its `endInclusive` value.
     * Note that this method does not take additional conditions into account and solely
     * relies on the bounds to determine emptiness.
     *
     * @return `true` if the range is empty, `false` otherwise.
     * @since 1.0.0
     */
    @CannotCheckExcludedElements
    override fun isEmpty() = start > endInclusive

    /**
     * Adds one or more conditions as predicates to the current range.
     *
     * This method allows you to directly add multiple conditions, represented as predicates,
     * to the collection of conditions within this range. These conditions will be evaluated
     * to determine membership within the range.
     *
     * @param conditions One or more predicate conditions to add to the range.
     * @since 1.0.0
     */
    fun conditions(vararg conditions: Predicate<T>) {
        this.conditions.addAll(conditions)
    }
    /**
     * Adds the given iterable of conditions to the existing set of conditions.
     *
     * @param conditions An iterable collection of conditions represented as `Predicate<T>`
     * that will be added to the current list of conditions.
     * @since 1.0.0
     */
    infix fun conditions(conditions: Iterable<Predicate<T>>) {
        this.conditions.addAll(conditions)
    }
    /**
     * Removes the specified condition from the collection of conditions.
     *
     * @param condition The condition to be removed from the current set of conditions.
     * @since 1.0.0
     */
    operator fun minus(condition: Predicate<T>) {
        conditions.add(condition)
    }
    /**
     * Removes the specified iterable collection of conditions from the current set of conditions.
     *
     * @param condition an iterable collection of predicates to be removed from the conditions.
     * @since 1.0.0
     */
    operator fun minus(condition: Iterable<Predicate<T>>) {
        conditions.addAll(condition)
    }

    /**
     * Removes the specified condition from the list of conditions.
     *
     * @param condition The condition to be removed from the collection of conditions.
     * @since 1.0.0
     */
    infix fun removeCondition(condition: Predicate<T>) {
        conditions.remove(condition)
    }
    /**
     * Removes the specified conditions from the current set of conditions.
     *
     * This method takes a variable number of predicates (conditions) and removes them
     * from the internal set of conditions stored in the object.
     *
     * @param conditions The conditions (predicates) to be removed.
     * @since 1.0.0
     */
    fun removeConditions(vararg conditions: Predicate<T>) {
        this.conditions.removeAll(conditions.toSet())
    }
    /**
     * Removes the specified conditions from the current set of conditions.
     *
     * @param conditions An iterable collection of predicates to be removed from the current set of conditions.
     * @since 1.0.0
     */
    infix fun removeConditions(conditions: Iterable<Predicate<T>>) {
        this.conditions.removeAll(conditions.toSet())
    }

    /**
     * Clears all conditions from the current range.
     *
     * This method empties the collection that holds the conditions associated with the range,
     * effectively removing all constraints.
     *
     * @since 1.0.0
     */
    fun clearConditions() {
        conditions.clear()
    }
}

/**
 * Represents an open-ended range with exclusions. This class extends `OpenEndRange` and adds
 * support for excluding specific elements from the set of values covered by the range.
 *
 * The range is defined by a `start` value (inclusive) and an `endExclusive` value (exclusive),
 * with an additional list of explicitly excluded elements that fall within the range boundaries.
 *
 * The type `T` must be comparable.
 *
 * @param T The type of the elements in the range, which must implement the `Comparable` interface.
 * @param start The start value of the range (inclusive).
 * @param endExclusive The end value of the range (exclusive).
 * @param excluded A mutable list of elements that are excluded from the range. Defaults to an empty list.
 * @since 1.0.0
 * @author Tomaso Pastorelli
 */
class OpenEndRangeWithExclusions<T : Comparable<T>>(
    override val start: T,
    override val endExclusive: T,
    val excluded: MList<T> = emptyMList(),
) : OpenEndRange<T> {

    companion object {
        /**
         * Creates a new instance of `OpenEndRangeWithExclusions` from the current `OpenEndRange` by excluding
         * the specified element. The resulting range will retain the same start and end values, but will exclude
         * the provided element from its set of valid values.
         *
         * @param T the type of the elements in the range, which must be comparable.
         * @param element the element to exclude from the range.
         * @since 1.0.0
         */
        infix fun <T: Comparable<T>> OpenEndRange<T>.exclude(element: T) =
            OpenEndRangeWithExclusions(start, endExclusive, element.asSingleMList())

        /**
         * Creates a new instance of `OpenEndRangeWithExclusions` that excludes the specified elements
         * from the range. The excluded elements will not be considered as part of the range during
         * operations such as containment checks.
         *
         * @param elements The elements to exclude from the range. These elements must
         *                 implement the `Comparable` interface.
         * @since 1.0.0
         */
        fun <T: Comparable<T>> OpenEndRange<T>.exclude(vararg elements: T) =
            OpenEndRangeWithExclusions(start, endExclusive, elements.toMList() as MList<T>)

        /**
         * Creates a new `OpenEndRangeWithExclusions` by excluding the specified elements from the current range.
         *
         * The method returns a new range excluding the provided elements, ensuring that these elements are not part
         * of the resulting range even if they would normally fall within the unmodified range.
         *
         * @param elements The elements to be excluded from the range.
         * @since 1.0.0
         */
       infix fun <T: Comparable<T>> OpenEndRange<T>.exclude(elements: Iterable<T>) =
            OpenEndRangeWithExclusions(start, endExclusive, elements.toMList())
    }

    /**
     * Checks whether the provided value is within the range of [start] (inclusive) and [endExclusive] (exclusive),
     * and not contained within the [excluded] list of elements.
     *
     * @param value The value to check for inclusion in the range.
     * @return `true` if the value is in the range and not excluded, `false` otherwise.
     * @since 1.0.0
     */
    override operator fun contains(value: T) = value in start..<endExclusive && value !in excluded

    /**
     * Determines whether the range is empty.
     *
     * A range is considered empty when the `start` value is greater than or equal to the
     * `endExclusive` value. Note that this method does not account for any exclusions that might
     * have been defined for the range.
     *
     * @return `true` if the range is empty; `false` otherwise.
     * @since 1.0.0
     */
    @CannotCheckExcludedElements
    override fun isEmpty() = start >= endExclusive

    /**
     * Removes the specified element from the exclusion list of the range.
     *
     * This method modifies the internal `excluded` collection by removing the provided element.
     *
     * @param element The element to remove from the exclusion list.
     * @since 1.0.0
     */
    infix fun exclude(element: T) {
        excluded.remove(element)
    }
    /**
     * Adds the specified elements to the exclusions list for the range.
     *
     * The elements passed to this method are added to the internal `excluded` list,
     * ensuring they are excluded when determining membership within the range.
     *
     * @param elements The elements to exclude from the range.
     * @since 1.0.0
     */
    fun exclude(vararg elements: T) {
        excluded.addAll(elements)
    }
    /**
     * Adds the elements from the given iterable collection to the list of excluded elements
     * within the range. Once added, these elements will be considered excluded and
     * will not be included in the range checks or other operations.
     *
     * @param elements The iterable collection of elements to add to the exclusion list.
     * @since 1.0.0
     */
    infix fun exlude(elements: Iterable<T>) {
        excluded.addAll(elements)
    }
    /**
     * Excludes the specified element from the range.
     *
     * @param element The element to exclude from the range.
     * @since 1.0.0
     */
    operator fun minus(element: T) {
        excluded.add(element)
    }
    /**
     * Removes the specified iterable collection of elements from the exclusion list of the range.
     *
     * @param elements The iterable collection of elements to be excluded from the range.
     * @since 1.0.0
     */
    operator fun minus(elements: Iterable<T>) {
        excluded.addAll(elements)
    }

    /**
     * Removes the specified element from the list of excluded elements in the range.
     *
     * @param element The element to remove from the list of exclusions.
     * @since 1.0.0
     */
    infix fun removeExclusion(element: T) {
        excluded.remove(element)
    }
    /**
     * Removes the specified elements from the exclusions list.
     *
     * @param elements the elements to be removed from the exclusions
     * @since 1.0.0
     */
    fun removeExclusions(vararg elements: T) {
        excluded.removeAll(elements.toSet())
    }
    /**
     * Removes the specified elements from the exclusion list.
     *
     * This method modifies the current exclusion list by removing all elements
     * provided in the input iterable. Any element in the input collection that
     * is present in the exclusion list will be removed.
     *
     * @param elements The collection of elements to be removed from the exclusion list.
     * @since 1.0.0
     */
    infix fun removeExclusions(elements: Iterable<T>) {
        excluded.removeAll(elements.toSet())
    }

    /**
     * Removes all excluded elements from the range. After calling this method, the range will no
     * longer have any exclusions, effectively resetting it to an inclusive state (excluding only
     * elements outside the start and endExclusive bounds).
     *
     * This method modifies the internal `excluded` collection in place.
     *
     * @since 1.0.0
     */
    fun clearExclusions() {
        excluded.clear()
    }
}

/**
 * Represents an open-ended range with conditions that can dynamically exclude certain values
 * based on provided predicates. This enhances the functionality of an open-ended range
 * by introducing conditional exclusions.
 *
 * @param T The type of elements in the range, which must be comparable.
 * @param start The inclusive starting point of the range.
 * @param endExclusive The exclusive ending point of the range.
 * @param conditions A mutable list of predicates used to define exclusion conditions. Defaults to an empty list.
 * @since 1.0.0
 * @author Tomaso Pastorelli
 */
class OpenEndRangeWithConditions<T : Comparable<T>>(
    override val start: T,
    override val endExclusive: T,
    val conditions: MList<Predicate<T>> = emptyMList()
) : OpenEndRange<T> {

    companion object {
        /**
         * Applies an exclusion condition to the current open-ended range, creating a new
         * range that excludes elements satisfying the specified predicate condition.
         *
         * @param T the type of elements in the range, which must be comparable.
         * @param condition the predicate defining the exclusion criteria for elements.
         * @since 1.0.0
         */
        infix fun <T: Comparable<T>> OpenEndRange<T>.exclude(condition: Predicate<T>) =
            OpenEndRangeWithConditions(start, endExclusive, condition.asSingleMList())

        /**
         * Adds exclusion conditions to the current open-ended range, creating a new range that
         * excludes the specified conditions.
         *
         * @param conditions vararg conditions to be excluded from the open-ended range. Each condition
         *                   is represented as a `Predicate<T>` which determines whether a specific
         *                   element is excluded from the range.
         * @since 1.0.0
         */
        fun <T: Comparable<T>> OpenEndRange<T>.exclude(vararg conditions: Predicate<T>) =
            OpenEndRangeWithConditions(start, endExclusive, conditions.toMList() as MList<Predicate<T>>)

        /**
         * Creates a new `OpenEndRangeWithConditions` instance that represents the current range
         * excluding elements that satisfy the specified conditions.
         *
         * @param conditions An iterable collection of predicates representing the conditions to exclude
         * elements from the range. Each predicate is evaluated to determine if a value should be excluded.
         * @since 1.0.0
         */
        infix fun <T: Comparable<T>> OpenEndRange<T>.exclude(conditions: Iterable<Predicate<T>>) =
            OpenEndRangeWithConditions(start, endExclusive, conditions.toMList())
    }

    /**
     * Checks if the provided value is within the range defined by `start` and `endExclusive`
     * and satisfies all specified conditions.
     *
     * @param value the value to check for containment in the range and conditions.
     * @return `true` if the value is greater than or equal to `start`, less than `endExclusive`,
     * and does not meet any of the negative conditions, otherwise `false`.
     * @since 1.0.0
     */
    override operator fun contains(value: T) = value in start..<endExclusive && conditions.none { it(value) }

    /**
     * Checks if the range is empty.
     *
     * A range is considered empty when its starting index is greater than or equal to
     * its end-exclusive index. This method does not account for any additional conditions
     * that may exclude elements or alter the range's effective content.
     *
     * @return `true` if the range is empty based on its starting and ending bounds, otherwise `false`.
     * @since 1.0.0
     */
    @CannotCheckExcludedElements
    override fun isEmpty() = start >= endExclusive

    /**
     * Adds the provided conditions to the existing list of conditions for this instance.
     *
     * @param conditions A variable number of `Predicate<T>` objects to be added as conditions.
     * @since 1.0.0
     */
    fun conditions(vararg conditions: Predicate<T>) {
        this.conditions.addAll(conditions)
    }
    /**
     * Adds all the provided conditions to the existing conditions of the range.
     *
     * @param conditions An iterable of predicates to be added as conditions to the range.
     * @since 1.0.0
     */
    infix fun conditions(conditions: Iterable<Predicate<T>>) {
        this.conditions.addAll(conditions)
    }
    /**
     * Removes the specified condition from the range's conditions.
     *
     * @param condition The predicate to be removed from the conditions of this range.
     * @since 1.0.0
     */
    operator fun minus(condition: Predicate<T>) {
        conditions.add(condition)
    }
    /**
     * Removes the specified collection of conditions from this range.
     *
     * This operator function allows the removal of multiple predicates
     * from the range's conditions in a single operation.
     *
     * @param condition An iterable collection of predicates to be removed
     *                  from the range's conditions.
     * @since 1.0.0
     */
    operator fun minus(condition: Iterable<Predicate<T>>) {
        conditions.addAll(condition)
    }

    /**
     * Removes the specified condition from the collection of conditions.
     *
     * @param condition The condition to be removed, defined as a `Predicate<T>`.
     * @since 1.0.0
     */
    infix fun removeCondition(condition: Predicate<T>) {
        conditions.remove(condition)
    }
    /**
     * Removes the specified conditions from the current collection of conditions.
     *
     * @param conditions A variable number of conditions to be removed, provided as predicates of type `T`.
     * @since 1.0.0
     */
    fun removeConditions(vararg conditions: Predicate<T>) {
        this.conditions.removeAll(conditions.toSet())
    }
    /**
     * Removes all specified conditions from the existing set of conditions.
     *
     * @param conditions an iterable collection of predicates to be removed
     * @since 1.0.0
     */
    infix fun removeConditions(conditions: Iterable<Predicate<T>>) {
        this.conditions.removeAll(conditions.toSet())
    }

    /**
     * Clears all the conditions from the current list or collection of conditions.
     *
     * This method removes all entries from the `conditions`, resetting it to an empty state.
     *
     * @since 1.0.0
     */
    fun clearConditions() {
        conditions.clear()
    }
}

/**
 * Represents a closed integer range with the ability to exclude specific values.
 *
 * This class extends `ClosedRange<Int>` and implements `Iterable<Int>`, allowing it to represent
 * a range of integers while excluding specific elements that are explicitly defined. It provides
 * various utility methods to exclude values, check membership, remove exclusions, and clear all exclusions.
 *
 * @property start The lower bound (inclusive) of the range.
 * @property endInclusive The upper bound (inclusive) of the range.
 * @property endExclusive The upper bound (exclusive) of the range.
 * @property excluded A mutable list of integers that are explicitly excluded from the range. (deprecated)
 * @since 1.0.0
 * @author Tomaso Pastorelli
 */
class IntRangeWithExclusions(
    override val start: Int,
    override val endInclusive: Int,
    val excluded: IntMList = emptyMList()
) : ClosedRange<Int>, OpenEndRange<Int>, Iterable<Int> {

    /**
     * Returns the exclusive upper bound of the range.
     *
     * The property throws an exception if the value of `endInclusive` is equal to `Int.MAX_VALUE`,
     * as it would be impossible to represent the exclusive bound in this case. It is recommended
     * to use the `endInclusive` property directly to avoid this issue.
     *
     * @throws IllegalStateException if the range includes `Int.MAX_VALUE`.
     * @deprecated Use `endInclusive` instead to avoid exceptions and ensure better safety.
     * @since 1.0.0
     */
    @Deprecated("Can throw an exception when it's impossible to represent the value with Int type, for example, when the range includes MAX_VALUE. It's recommended to use 'endInclusive' property that doesn't throw.")
    override val endExclusive: Int
        get() {
            if (endInclusive == Int.MAX_VALUE) error("Cannot return the exclusive upper bound of a range that includes MAX_VALUE.")
            return endInclusive + 1
        }

    companion object {
        /**
         * Returns a new `IntClosedRangeWithExclusions` instance representing the current `IntRange`
         * with the specified element excluded.
         *
         * This function enables the creation of a range that excludes a single element
         * within the original range.
         *
         * @param element The integer to be excluded from the range.
         * @since 1.0.0
         */
        infix fun IntRange.exclude(element: Int) =
            IntRangeWithExclusions(start, endInclusive, element.asSingleMList())

        /**
         * Creates a new `IntClosedRangeWithExclusions` instance from the current `IntRange` and excludes the specified elements.
         * The resulting range will maintain the same start and end values but will exclude the given elements.
         *
         * @param elements The integers to be excluded from the range.
         * @since 1.0.0
         */
        fun IntRange.exclude(vararg elements: Int) =
            IntRangeWithExclusions(start, endInclusive, elements.toMList())

        /**
         * Creates a new `IntClosedRangeWithExclusions` from the current `IntRange` by
         * excluding a specified collection of integer elements.
         *
         * @param elements An iterable collection of integers to exclude from the range.
         * @since 1.0.0
         */
        infix fun IntRange.exclude(elements: Iterable<Int>) =
            IntRangeWithExclusions(start, endInclusive, elements.toMList())
    }

    /**
     * Provides an iterator for traversing the range while excluding specific elements.
     *
     * The iterator skips elements that are marked as excluded and ensures only valid elements
     * within the defined range are returned.
     *
     * @return an `IntIterator` that iterates through elements in the range, excluding any specified exclusions.
     * @since 1.0.0
     */
    override fun iterator(): IntIterator = object : IntIterator() {
        /**
         * Represents the current position in the range iteration process.
         *
         * This variable holds the value of the current element being referenced by the iterator.
         * It starts at the beginning of the range (`start`) and advances during each iteration,
         * skipping over elements excluded from the range.
         *
         * The value is incremented as the iterator progresses, ensuring elements defined as
         * excluded are not returned. When no more valid elements are available within the
         * range, the value is set beyond the `endInclusive` limit.
         *
         * @see hasNext
         * @see nextInt
         * @since 1.0.0
         */
        private var current = start

        /**
         * Determines if there is a next element in the iteration, considering exclusions.
         *
         * @return true if there is a valid next element that is not excluded and within the range; false otherwise.
         * @since 1.0.0
         */
        override fun hasNext(): Boolean {
            var next = current
            while (next <= endInclusive && next in excluded) {
                next++
            }
            return next <= endInclusive
        }

        /**
         * Returns the next integer in the range that is not part of the excluded set.
         * Advances the current pointer in the process. If there are no more
         * integers to return within the range, a `NoSuchElementException` is thrown.
         *
         * @return the next integer in the range that is not excluded.
         * @throws NoSuchElementException if there are no more elements left to iterate.
         * @since 1.0.0
         */
        override fun nextInt(): Int {
            while (current <= endInclusive && current in excluded) {
                current++
            }
            if (current > endInclusive)
                throw NoSuchElementException()
            return current++
        }
    }

    /**
     * Checks if the specified integer value is within the defined range of the current instance
     * and ensures it is not part of the excluded values.
     *
     * @param value the integer to check.
     * @since 1.0.0
     */
    override operator fun contains(value: Int) = value in start..endInclusive && value !in excluded

    /**
     * Determines if the range is empty, i.e., if the starting value is greater than the ending value.
     *
     * Note that this method does not account for excluded elements, and its behavior is restricted
     * to the comparison of the `start` and `endInclusive` properties only. Any excluded elements
     * defined in the range are ignored.
     *
     * @return true if the range is empty based on the comparison of `start` and `endInclusive`,
     *         false otherwise.
     * @since 1.0.0
     */
    @CannotCheckExcludedElements
    override fun isEmpty() = start > endInclusive

    /**
     * Excludes the specified integer element from the current range.
     *
     * The provided element is added to the list of exclusions, making it no longer
     * part of the range during iteration or containment checks.
     *
     * @param element The integer value to be excluded from the current range.
     * @since 1.0.0
     */
    infix fun exclude(element: Int) {
        excluded.remove(element)
    }
    /**
     * Excludes the specified integer elements from the current range.
     * The provided elements are added to the list of exclusions.
     *
     * @param elements The integers to be excluded from the current range.
     * @since 1.0.0
     */
    fun exclude(vararg elements: Int) {
        excluded.addAll(elements.toList())
    }
    /**
     * Adds the specified iterable of integers to the excluded range.
     *
     * This method appends all the elements in the provided iterable to the exclusions
     * of the current range, effectively marking them as excluded from the range.
     *
     * @param elements An iterable collection of integers to be excluded from the range.
     * @since 1.0.0
     */
    infix fun exlude(elements: Iterable<Int>) {
        excluded.addAll(elements)
    }
    /**
     * Removes the specified element from the list of exclusions in this range.
     *
     * @param element The element to be removed from the exclusions.
     * @since 1.0.0
     */
    operator fun minus(element: Int) {
        excluded.add(element)
    }
    /**
     * Subtracts the specified elements from the current range exclusions.
     *
     * This operator function allows excluding multiple integers from the range
     * by adding them to the list of already excluded elements.
     *
     * @param elements The collection of integers to exclude from the range.
     * @since 1.0.0
     */
    operator fun minus(elements: Iterable<Int>) {
        excluded.addAll(elements)
    }

    /**
     * Removes the specified element from the list of excluded integers.
     *
     * This operation ensures that the given element is no longer excluded from the range.
     *
     * @param element The integer value to be removed from the list of exclusions.
     * @since 1.0.0
     */
    infix fun removeExclusion(element: Int) {
        excluded.remove(element)
    }
    /**
     * Removes the specified elements from the set of exclusions.
     *
     * @param elements The integers to be removed from the exclusions.
     * @since 1.0.0
     */
    fun removeExclusions(vararg elements: Int) {
        excluded.removeAll(elements.toList())
    }
    /**
     * Removes the specified elements from the exclusions list of the range.
     *
     * @param elements An iterable collection of integers to be removed from the exclusions.
     * @since 1.0.0
     */
    infix fun removeExclusions(elements: Iterable<Int>) {
        excluded.removeAll(elements.toSet())
    }

    /**
     * Clears all excluded elements, resetting the exclusions for the range.
     *
     * This method removes all elements currently marked as excluded, effectively restoring
     * the range to its original state without any exclusions.
     *
     * @since 1.0.0
     */
    fun clearExclusions() {
        excluded.clear()
    }
}

/**
 * Defines a closed range of integers with additional exclusion conditions.
 *
 * This class represents a closed range of integers, but with the added ability to exclude
 * certain integers from the range based on provided conditions. The exclusion conditions
 * are defined as predicates that determine whether specific integers should be excluded.
 *
 * It provides an iterator implementation that respects the exclusion conditions and skips
 * over excluded elements.
 *
 * @property start The start of the range, inclusive.
 * @property endInclusive The end of the range, inclusive.
 * @property endExclusive The end of the range, exclusive. (deprecated)
 * @property conditions A mutable list of predicates representing exclusion conditions.
 * @since 1.0.0
 * @author Tomaso Pastorelli
 */
class IntRangeWithConditions(
    override val start: Int,
    override val endInclusive: Int,
    val conditions: MList<Predicate<Int>> = emptyMList(),
) : ClosedRange<Int>, OpenEndRange<Int>, Iterable<Int> {
    /**
     * Represents the exclusive upper bound of the range.
     *
     * The `endExclusive` property is computed and derived from the `endInclusive` value.
     * It returns the smallest integer greater than `endInclusive`.
     * If `endInclusive` is set to `Int.MAX_VALUE`, accessing this property results in an exception,
     * as the exclusive upper bound cannot be computed in such scenarios.
     *
     * @throws IllegalStateException if `endInclusive` equals `Int.MAX_VALUE`.
     * @deprecated Use `endInclusive` instead to avoid exceptions and ensure better safety.
     * @since 1.0.0
     */
    @Deprecated("Can throw an exception when it's impossible to represent the value with Int type, for example, when the range includes MAX_VALUE. It's recommended to use 'endInclusive' property that doesn't throw.")
    override val endExclusive: Int
        get() {
            if (endInclusive == Int.MAX_VALUE) error("Cannot return the exclusive upper bound of a range that includes MAX_VALUE.")
            return endInclusive + 1
        }

    companion object {
        /**
         * Creates a new `IntClosedRangeWithConditions` instance from the provided `IntRange` by excluding
         * integers satisfying the specified condition. The condition is represented as a `Predicate` that
         * determines whether an integer should be excluded from the range.
         *
         * @param element a `Predicate` representing the condition to exclude integers from the range.
         * @since 1.0.0
         */
        infix fun IntRange.exclude(element: Predicate<Int>) =
            IntRangeWithConditions(start, endInclusive, element.asSingleMList())

        /**
         * Excludes specific elements from the range based on the provided predicates.
         *
         * This function creates a new instance of `IntClosedRangeWithConditions`, which represents the
         * same range but excludes the elements that satisfy any of the given predicates.
         *
         * @param elements The predicates to determine which elements should be excluded from the range.
         * @since 1.0.0
         */
        fun IntRange.exclude(vararg elements: Predicate<Int>) =
            IntRangeWithConditions(start, endInclusive, elements.toMList() as MList<Predicate<Int>>)

        /**
         * Returns a new instance of `IntClosedRangeWithConditions` by excluding integers from this range
         * that satisfy any of the given conditions (predicates).
         *
         * The resulting range retains the original start and end bounds but applies the provided exclusion
         * conditions to filter out specific integers during its usage.
         *
         * @param elements An iterable collection of predicates that define the exclusion conditions.
         *                 Each predicate represents a condition for excluding integers from the range.
         * @since 1.0.0
         */
        infix fun IntRange.exclude(elements: Iterable<Predicate<Int>>) =
            IntRangeWithConditions(start, endInclusive, elements.toMList())
    }

    /**
     * Returns an iterator over the elements of this range, skipping elements that match any condition
     * from the `conditions` property.
     *
     * The iterator provides sequential access to all integers in the range [start, endInclusive]
     * that do not satisfy any of the excluded conditions.
     *
     * @return An `IntIterator` that iterates over non-excluded elements in the range.
     * @since 1.0.0
     */
    override fun iterator(): IntIterator = object : IntIterator() {
        /**
         * Represents the current value being iterated in the integer range.
         * Initially set to the start value of the range and updated during iteration.
         *
         * Used internally to track the current position as iteration progresses through the range.
         *
         * @since 1.0.0
         */
        private var current = start

        /**
         * Determines whether the given integer value is excluded based on predefined conditions.
         *
         * This method checks the provided integer against a list of conditions, and if any of the
         * conditions evaluates to `true`, the value is considered excluded.
         *
         * @param value The integer value to be checked against exclusion conditions.
         * @return `true` if the value satisfies any exclusion conditions; `false` otherwise.
         * @since 1.0.0
         */
        private fun isExcluded(value: Int): Boolean =
            conditions.any { it(value) }

        /**
         * Determines if there are any more elements in the range that are not excluded
         * based on the provided conditions.
         *
         * @return `true` if there are more elements in the range that satisfy the conditions;
         *         `false` otherwise.
         * @since 1.0.0
         */
        override fun hasNext(): Boolean {
            var next = current
            while (next <= endInclusive && isExcluded(next)) {
                next++
            }
            return next <= endInclusive
        }

        /**
         * Returns the next integer in the range that is not excluded by the specified conditions.
         * If no such integer exists, throws a `NoSuchElementException`.
         *
         * @return The next integer in the range that satisfies the conditions.
         * @since 1.0.0
         */
        override fun nextInt(): Int {
            while (current <= endInclusive && isExcluded(current)) {
                current++
            }
            if (current > endInclusive)
                throw NoSuchElementException()
            return current++
        }
    }

    /**
     * Checks whether the specified value is within the range defined by `start` and `endInclusive`,
     * and is not excluded by any of the conditions in the range.
     *
     * @param value The integer value to check for inclusion within the range.
     * @return `true` if the value is within the range and not excluded; `false` otherwise.
     * @since 1.0.0
     */
    override operator fun contains(value: Int) = value in start..endInclusive && conditions.none { it(value) }

    /**
     * Checks whether the range defined by `start` and `endInclusive` is empty.
     *
     * A range is considered empty if the starting value is greater than the ending value.
     * Note that this method does not take into account excluded values or custom conditions.
     *
     * @return `true` if the range is empty, `false` otherwise.
     * @since 1.0.0
     */
    @CannotCheckExcludedElements
    override fun isEmpty() = start > endInclusive

    /**
     * Adds the specified predicates to the list of conditions.
     *
     * This method allows adding one or more conditions represented as predicates
     * to the current set of conditions. Each condition is a function that evaluates
     * an `Int` and returns a boolean value indicating whether the condition is met.
     *
     * @param conditions A variable number of `Predicate<Int>` representing the conditions to add.
     * @since 1.0.0
     */
    fun conditions(vararg conditions: Predicate<Int>) {
        this.conditions.addAll(conditions)
    }
    /**
     * Adds the specified iterable of conditions to the current list of conditions.
     *
     * This function appends all predicates from the given iterable to the `conditions` collection
     * of the current instance. The conditions are used to specify constraints or filters
     * for the range of integers represented by this class.
     *
     * @param conditions An iterable collection of predicates, where each predicate is a condition
     *                   to be applied to the range.
     * @since 1.0.0
     */
    infix fun conditions(conditions: Iterable<Predicate<Int>>) {
        this.conditions.addAll(conditions)
    }
    /**
     * Removes the specified condition from the list of conditions in the range.
     *
     * @param condition A predicate to be removed from the range's conditions.
     * @since 1.0.0
     */
    operator fun minus(condition: Predicate<Int>) {
        conditions.add(condition)
    }
    /**
     * Removes an iterable collection of predicates from the range's current conditions.
     *
     * @param condition the iterable collection of predicates to be removed from the conditions.
     * @since 1.0.0
     */
    operator fun minus(condition: Iterable<Predicate<Int>>) {
        conditions.addAll(condition)
    }

    /**
     * Removes the specified condition from the collection of conditions.
     *
     * @param condition the predicate condition to be removed from the collection
     * @since 1.0.0
     */
    infix fun removeCondition(condition: Predicate<Int>) {
        conditions.remove(condition)
    }
    /**
     * Removes the specified conditions from the current set of conditions.
     *
     * @param conditions The conditions to be removed from the existing set. Each condition
     *                   is defined as a `Predicate<Int>` which evaluates an integer value.
     * @since 1.0.0
     */
    fun removeConditions(vararg conditions: Predicate<Int>) {
        this.conditions.removeAll(conditions.toSet())
    }
    /**
     * Removes a collection of conditions from the current list of conditions.
     *
     * @param conditions an iterable collection of conditions to be removed, where each condition is represented as a predicate of type `Predicate<Int>`.
     * @since 1.0.0
     */
    infix fun removeConditions(conditions: Iterable<Predicate<Int>>) {
        this.conditions.removeAll(conditions.toSet())
    }

    /**
     * Clears all conditions from the current instance. This removes any previously defined
     * predicates or filters applied to the range, effectively resetting it to its full range
     * without any additional constraints.
     *
     * @since 1.0.0
     */
    fun clearConditions() {
        conditions.clear()
    }
}