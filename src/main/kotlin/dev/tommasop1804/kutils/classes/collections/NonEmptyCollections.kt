/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("NonEmptyCollectionsKt")
@file:Since("5.2.0")
@file:MustUseReturnValues
@file:Suppress("unused", "JavaDefaultMethodsNotOverriddenByDelegation")

package dev.tommasop1804.kutils.classes.collections

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.maps.*
import dev.tommasop1804.kutils.exceptions.*
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.module.SimpleModule

/**
 * A guarded implementation of the [MIterator] interface that enforces additional constraints
 * when performing certain modification operations. Specifically, it ensures that the `remove`
 * operation cannot be used to remove the last remaining element of a `NonEmptyMList`.
 *
 * This iterator delegates all operations to another iterator but intercepts and adjusts
 * behavior for specific cases where constraints must be applied.
 *
 * @constructor Creates a new instance of [GuardedIterator] wrapping a given delegate iterator.
 * @param delegate The [MIterator] to which all operations are delegated.
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
internal class GuardedIterator<E>(
    private val delegate: MIterator<E>,
    private val guard: RemovalGuard,
) : MIterator<E> by delegate {
    override fun remove() {
        guard.check(1)
        delegate.remove()
    }
}

/**
 * A wrapper for an `MListIterator` that enforces additional constraints during mutation operations.
 * Specifically, it prevents removal of the last remaining element in a `NonEmptyMList`.
 *
 * @property delegate The underlying `MListIterator` that handles core iteration logic.
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
internal class GuardedListIterator<E>(
    private val delegate: MListIterator<E>,
    private val guard: RemovalGuard,
) : MListIterator<E> by delegate {
    override fun remove() {
        guard.check(1)
        delegate.remove()
    }
}

/**
 * A utility class to safeguard against removing too many elements from a collection or iterable structure.
 *
 * The `RemovalGuard` ensures that a specified number of elements can be removed without violating
 * constraints based on a dynamic size. If the removal would leave fewer than the required number of
 * elements, a `TooFewElementsException` is thrown with a provided error message.
 *
 * This is useful for enforcing collection or iterable size constraints in scenarios where safe
 * removal operations are critical.
 *
 * @constructor Creates an instance of `RemovalGuard` with a function to compute the current size
 * and a custom error message to display in case of failure.
 * @param rootSize A function that computes the current size of the collection or iterable structure.
 * @param message The error message to be displayed when the removal constraint is violated.
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
@PublishedApi
internal class RemovalGuard(
    private val rootSize: Supplier<Int>,
    private val message: String,
) {
    /**
     * Validates if the number of elements to be removed does not exceed the available number of elements.
     *
     * @param removing The number of elements intended to be removed. Must not cause the total remaining elements to be less than or equal to zero.
     * @throws TooFewElementsException if the removal operation would result in too few remaining elements.
     * @since 5.2.0
     */
    fun check(removing: Int) {
        rootSize() - removing > 0 || throw TooFewElementsException(message)
    }
}

/**
 * A guarded mutable collection that enforces constraints on element removal operations.
 *
 * This class wraps around an existing mutable collection (`delegate`) and ensures that certain
 * constraints, defined by a `RemovalGuard`, are respected during element removal operations.
 * The constraints are applied to avoid removing too many elements from the collection in a way
 * that would violate its invariants or constraints.
 *
 * @param E The type of elements contained in the collection.
 * @property delegate The underlying mutable collection being managed and guarded.
 * @property guard The removal guard that enforces constraints during certain operations.
 * @author Tommaso Pastorelli
 * @since 5.2.0
 */
internal open class GuardedMCollection<E>(
    protected val delegate: MCollection<E>,
    protected val guard: RemovalGuard,
) : MCollection<E> by delegate {

    /**
     * Removes all elements from the underlying collection, ensuring compliance with the removal guard's constraints.
     *
     * This method delegates the clearing operation to the underlying collection after verifying that
     * the removal does not violate any constraints defined by the associated [RemovalGuard].
     *
     * @throws TooFewElementsException if clearing the collection would result in an invalid state as determined by the guard.
     * @since 5.2.0
     */
    override fun clear() {
        guard.check(delegate.size)
        delegate.clear()
    }

    /**
     * Removes the specified element from the collection if it is present.
     *
     * @param element The element to be removed from the collection.
     * @return `true` if the element was successfully removed, `false` if the element was not found.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun remove(element: E): Boolean {
        if (element !in delegate) return false
        guard.check(1)
        return delegate.remove(element)
    }

    /**
     * Removes all elements in the specified collection from this collection.
     * The removal operation is validated using the guard to ensure constraints are not violated.
     *
     * @param elements The collection of elements to be removed.
     * @return `true` if this collection was modified as a result of the removal, or `false` otherwise.
     * @throws TooFewElementsException if the removal would result in too few remaining elements.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun removeAll(elements: Collection<E>): Boolean {
        val victims = elements.toSet()
        guard.check(delegate.count { it in victims })
        return delegate.removeAll(victims)
    }

    /**
     * Retains only the elements in this collection that are contained in the specified collection.
     * Removes all other elements from the collection that are not present in the given collection.
     *
     * @param elements The collection of elements to retain in this collection.
     * @return `true` if the collection was modified as a result of the operation, `false` otherwise.
     * @throws TooFewElementsException if the removal operation would result in too few remaining elements,
     * as enforced by the associated `guard`.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun retainAll(elements: Collection<E>): Boolean {
        val survivors = elements.toSet()
        guard.check(delegate.count { it !in survivors })
        return delegate.retainAll(survivors)
    }

    /**
     * Removes all elements of this collection that match the specified predicate.
     *
     * @param filter A predicate that determines whether an element should be removed.
     *               Elements for which the predicate returns `true` will be removed.
     * @return `true` if any elements were removed as a result of this operation; `false` otherwise.
     * @throws TooFewElementsException if removing the elements would result in too few remaining elements in the collection.
     * @since 5.2.0
     */
    override fun removeIf(filter: java.util.function.Predicate<in E>): Boolean {
        guard.check(delegate.count { filter.test(it) })
        return delegate.removeIf(filter)
    }

    /**
     * Returns an iterator over the elements of the collection. The iterator
     * ensures compliance with a guard condition while providing access to
     * the underlying collection's elements.
     *
     * @return An `MIterator` instance that iterates over the elements of
     * the collection with an applied guard condition.
     * @since 5.2.0
     */
    override fun iterator(): MIterator<E> = GuardedIterator(delegate.iterator(), guard)

    /**
     * Compares this object with the specified object for equality.
     *
     * @param other The object to be compared for equality with this instance.
     * @return `true` if the specified object is equal to this instance, `false` otherwise.
     * @since 5.2.0
     */
    override fun equals(other: Any?) = delegate == other
    /**
     * Returns the hash code value for this collection. The hash code is
     * determined by delegating the calculation to the underlying `delegate` collection.
     *
     * @return The hash code of the delegate collection.
     * @since 5.2.0
     */
    override fun hashCode() = delegate.hashCode()
    /**
     * Returns a string representation of the object by delegating the call
     * to the `toString` method of the underlying `delegate` object.
     *
     * @return A string representation of the `delegate` object.
     * @since 5.2.0
     */
    override fun toString() = delegate.toString()
}

/**
 * A decorator for the `MSet` interface that enforces removal constraints through a `RemovalGuard`.
 *
 * The `GuardedMSet` class wraps an existing `MSet` implementation and ensures that removal operations
 * comply with the rules specified by the provided `RemovalGuard`. This includes operations like
 * removing individual elements, removing multiple elements, or clearing the set entirely.
 *
 * Attempts to remove elements in violation of the guard's constraints will result in a
 * `TooFewElementsException`.
 *
 * @param E The type of elements maintained by this set.
 * @constructor Creates a new instance of `GuardedMSet` that wraps the provided `MSet` delegate.
 * @param delegate The underlying `MSet` implementation to be guarded.
 * @param guard The `RemovalGuard` enforcing constraints during removal operations.
 * @since 5.2.0
 * @since Tommaso Pastorelli
 */
internal class GuardedMSet<E>(
    delegate: MSet<E>,
    guard: RemovalGuard,
) : GuardedMCollection<E>(delegate, guard), MSet<E>

/**
 * A guarded implementation of the `MList` interface that integrates a removal guard.
 *
 * This class provides a wrapper over a mutable list and uses a `RemovalGuard` to enforce
 * safety constraints during element removal operations. It ensures that operations removing items
 * from the list do not leave it in an invalid state, as defined by the `RemovalGuard` rules.
 *
 * The class delegates most list operations to the underlying mutable list but intercepts
 * removal-specific functionalities to apply the guard checks.
 *
 * @constructor Creates a new `GuardedMList` with the specified list and guard.
 * @param list The underlying mutable list that this class wraps.
 * @param guard An instance of `RemovalGuard` to apply constraints on removal operations.
 * @since 5.2.0
 */
@Suppress("RedundantOverride")
internal class GuardedMList<E>(
    private val list: MList<E>,
    guard: RemovalGuard,
) : GuardedMCollection<E>(list, guard), MList<E> by list {

    /**
     * Provides the current size of the list.
     * This represents the total number of elements in the list.
     * The value is fetched dynamically whenever accessed.
     * @since 5.2.0
     */
    override val size get() = list.size
    /**
     * Checks if the collection is empty.
     *
     * This method determines whether the underlying list contains no elements.
     *
     * @return `true` if the list is empty, `false` otherwise.
     * @since 5.2.0
     */
    override fun isEmpty() = list.isEmpty()
    /**
     * Checks if the specified element is present in the list.
     *
     * @param element The element to be checked for presence in the list.
     * @return `true` if the element is found, `false` otherwise.
     * @since 5.2.0
     */
    override fun contains(element: E) = list.contains(element)
    /**
     * Checks if the collection contains all elements of the given collection.
     *
     * @param elements the collection of elements to check for containment
     * @return `true` if all elements in the specified collection are contained in this collection, `false` otherwise
     * @since 5.2.0
     */
    override fun containsAll(elements: Collection<E>) = list.containsAll(elements)
    /**
     * Provides an iterator over the elements in the collection.
     *
     * @return An instance of [MIterator] to iterate over the collection's elements.
     * @since 5.2.0
     */
    override fun iterator(): MIterator<E> = super.iterator()
    /**
     * Removes all elements from the list. This operation ensures that the associated guard is
     * checked before clearing the list to verify if the removal operation is allowed based
     * on the current state and logic defined in the guard.
     *
     * Delegates the clear operation to the underlying collection after performing the guard check.
     * @since 5.2.0
     */
    override fun clear() = super.clear()
    /**
     * Removes a single instance of the specified element from this collection, if it is present.
     *
     * @param element The element to be removed from the collection.
     * @return `true` if an element was successfully removed, or `false` if the element was not found in the collection.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun remove(element: E) = super.remove(element)
    /**
     * Removes all elements from the collection that are present in the specified collection.
     *
     * @param elements the collection of elements to be removed from this collection.
     * @return `true` if the collection was modified as a result of the operation.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun removeAll(elements: Collection<E>) = super.removeAll(elements)
    /**
     * Retains only the elements in this collection that are contained in the specified collection.
     * Removes all other elements from this collection.
     *
     * @param elements The collection containing elements to be retained in this collection.
     * @return `true` if this collection was modified as a result of the operation, `false` otherwise.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun retainAll(elements: Collection<E>) = super.retainAll(elements)

    /**
     * Removes the element at the specified position in the list and returns it.
     *
     * This operation is guarded by a removal check to ensure it doesn't violate
     * constraints on the number of remaining elements in the collection.
     *
     * @param index The position of the element to remove. Must be within the range of the list indices.
     * @return The element that was removed from the list at the specified position.
     * @throws TooFewElementsException If the removal would result in too few remaining elements, as determined by the guard.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun removeAt(index: Int): E {
        guard.check(1)
        return list.removeAt(index)
    }

    /**
     * Returns a guarded list iterator over the elements in this list.
     * The iterator ensures that any modifications comply with the specified guard.
     *
     * @return a guarded list iterator over the elements in this list.
     * @since 5.2.0
     */
    override fun listIterator(): MListIterator<E> = GuardedListIterator(list.listIterator(), guard)
    /**
     * Returns a list iterator over the elements in this list starting at the specified position.
     * The returned iterator respects the constraints of the associated guard.
     *
     * @param index the index of the first element to be returned by the iterator.
     *              Must be between 0 and the size of the list (inclusive).
     * @return a `MListIterator` instance that starts iterating at the specified index.
     * @since 5.2.0
     */
    override fun listIterator(index: Int): MListIterator<E> =
        GuardedListIterator(list.listIterator(index), guard)

    /**
     * Returns a view of the portion of this list between the specified `fromIndex`, inclusive,
     * and `toIndex`, exclusive. The returned list is still guarded by the associated `RemovalGuard`.
     *
     * @param fromIndex the starting index of the sublist, inclusive.
     * @param toIndex the ending index of the sublist, exclusive.
     * @return a guarded sublist view of the specified range within this list.
     * @since 5.2.0
     */
    override fun subList(fromIndex: Int, toIndex: Int): MList<E> =
        GuardedMList(list.subList(fromIndex, toIndex), guard)

    /**
     * Adds the specified element to the list.
     *
     * This method delegates the addition of the element to the underlying list.
     *
     * @param element The element to be added to the list.
     * @return `true` if the list was modified as a result of this operation, `false` otherwise.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun add(element: E) = list.add(element)

    /**
     * Adds all elements from the specified collection to this collection.
     *
     * This method delegates the addition of elements to the underlying list implementation.
     *
     * @param elements The collection of elements to be added. Each element in this collection
     *                 will be appended to the current list.
     * @return `true` if the list was modified as a result of the addition, `false` otherwise.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun addAll(elements: Collection<E>) = list.addAll(elements)
}

/**
 * Represents an iterable collection that guarantees there is at least one element.
 *
 * This interface extends the [Iterable] interface and imposes a contract that
 * the backing collection will never be empty. Implementations of this interface
 * must ensure that calling [iterator] will always provide an iterator with at
 * least one element.
 *
 * @param E the type of elements in the collection.
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
interface NonEmptyIterable<out E> : Iterable<E>
/**
 * Represents a mutable iterable collection that guarantees to contain at least one element.
 *
 * This interface extends the [MIterable] interface and ensures that the iterable is non-empty.
 * The type parameter [E] specifies the type of elements contained in the collection.
 *
 * Implementations of this interface provide traversal and iteration capabilities
 * while ensuring that the underlying collection always contains at least one element.
 *
 * @param E The type of elements in the collection.
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
interface NonEmptyMIterable<out E> : MIterable<E>

/**
 * A collection that guarantees at least one element is present.
 *
 * This interface inherits from both [Collection] and [NonEmptyIterable],
 * ensuring that the collection is iterable and never empty.
 *
 * @param E the type of elements contained in the collection.
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
interface NonEmptyCollection<out E> : Collection<E>, NonEmptyIterable<E> {
    /**
     * Checks if the collection is empty.
     *
     * This method always returns `false` because a `NonEmptyCollection`
     * guarantees that it cannot be empty.
     *
     * @since 5.2.0
     * @return `false` to indicate the collection is not empty.
     */
    override fun isEmpty() = false
}
/**
 * A collection interface that extends both [MCollection] and [NonEmptyMIterable]. This interface
 * guarantees that the collection will never be empty. The [isEmpty] method is overridden
 * to always return `false`.
 *
 * @param E the type of elements contained in the collection.
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
interface NonEmptyMCollection<E> : MCollection<E>, NonEmptyMIterable<E> {
    /**
     * Checks if the collection is empty.
     *
     * This method always returns `false`, as the collection is guaranteed
     * to be non-empty by definition.
     *
     * @return `false`, indicating the collection is not empty.
     * @since 5.2.0
     */
    override fun isEmpty() = false
}

/**
 * A specialized list that guarantees to contain at least one element. It provides additional methods
 * specific to non-empty collections while delegating standard list operations to an underlying list.
 *
 * @param E the type of elements contained in the list
 * @property elements the internal list of elements that this class wraps, validated to be non-empty
 * @author Tommaso Pastorelli
 * @since 5.2.0
 */
open class NonEmptyList<out E>(@PublishedApi internal val elements: List<E>) : List<E> by elements, NonEmptyCollection<E> {

    /**
     * Secondary constructor for creating a [NonEmptyList] instance from a vararg of elements.
     *
     * @param elements The elements to initialize the [NonEmptyList].
     * Converts the given vararg of elements into a [List] which is passed
     * to the primary constructor.
     * @since 5.2.0
     */
    constructor(vararg elements: E) : this(elements.toList())

    /**
     * Retrieves the first element of the list.
     *
     * This property provides direct access to the head of the `NonEmptyList`,
     * which is guaranteed to exist since the list cannot be empty.
     *
     * @return The first element of the list.
     * @since 5.2.0
     */
    open val head get() = elements.first()
    /**
     * Retrieves all elements of the list except the first one.
     *
     * This property returns a sublist of the elements, starting from the second element
     * (index `1`) to the end of the list. The returned list preserves the order of elements
     * as in the original list.
     *
     * Note: The property assumes the list is not empty since it is part of a `NonEmptyList`.
     * @since 5.2.0
     */
    open val tail get() = elements.subList(1, elements.size)

    init { elements.isEmpty() && throw TooFewElementsException("Elements are empty.") }

    companion object {
        /**
         * Creates a `NonEmptyList` instance from the provided elements.
         *
         * @param elements Vararg parameter representing the elements of the list.
         * @return A `NonEmptyList` object containing the provided elements.
         * @since 5.2.0
         */
        operator fun <E> of(vararg elements: E) = NonEmptyList(elements.toList())

        /**
         * Converts the current [Iterable] into a [NonEmptyList].
         *
         * This function transforms the elements of the iterable into a [List] and wraps it in a [NonEmptyList].
         * It is expected that the current [Iterable] is non-empty; otherwise, a runtime exception will occur.
         *
         * @receiver The source [Iterable] to be converted into a [NonEmptyList].
         * @return A [NonEmptyList] containing the elements of the given [Iterable].
         * @throws TooFewElementsException if the receiver iterable is empty.
         * @since 5.2.0
         */
        fun <E> Iterable<E>.toNonEmptyList() = NonEmptyList(this.toList())
        /**
         * Converts the current [Iterable] instance into a [NonEmptyList] if it contains at least one element,
         * or returns `null` if the iterable is empty or if an exception occurs during the conversion process.
         *
         * @receiver [Iterable] to be converted into a [NonEmptyList].
         * @return A [NonEmptyList] containing the elements of the iterable, or `null` if the iterable is empty
         *         or an exception is thrown.
         * @since 5.2.0
         */
        fun <E> Iterable<E>.toNonEmptyListOrNull() = tryOrNull { NonEmptyList(this.toList()) }
        /**
         * Converts the iterable to a `NonEmptyList` if it contains at least one element, otherwise returns a default `NonEmptyList` provided by the given supplier.
         *
         * @param E The type of the elements in the iterable and the `NonEmptyList`.
         * @param default A supplier function that provides a default `NonEmptyList` to be returned if the iterable is empty or an exception occurs during conversion.
         * @return A `NonEmptyList` created from the elements of this iterable, or a default `NonEmptyList` supplied by the `default` parameter if the iterable is empty or an error
         *  occurs.
         * @since 5.2.0
         */
        fun <E> Iterable<E>.toNonEmptyListOrDefault(default: Supplier<NonEmptyList<E>>) =
            tryOr({ default() }) { NonEmptyList(this.toList()) }

        /**
         * Converts the current array into a [NonEmptyList].
         *
         * This function transforms the elements of the array into a [List] and wraps it in a [NonEmptyList].
         * It is expected that the array is non-empty; otherwise, a runtime exception will occur.
         *
         * @receiver The source array to be converted into a [NonEmptyList].
         * @return A [NonEmptyList] containing the elements of the given array.
         * @throws TooFewElementsException if the receiver array is empty.
         * @since 5.2.0
         */
        fun <T> Array<T>.toNonEmptyList() = toList().toNonEmptyList()
        /**
         * Converts the current [IntArray] into a [NonEmptyList].
         *
         * Transforms the elements of the array into a [List] and subsequently wraps it in a [NonEmptyList].
         * It is assumed that the current [IntArray] is non-empty; otherwise, a runtime exception will be thrown.
         *
         * @receiver The source [IntArray] to be converted into a [NonEmptyList].
         * @return A [NonEmptyList] containing the elements of the given [IntArray].
         * @throws TooFewElementsException if the array is empty.
         * @since 5.2.0
         */
        fun IntArray.toNonEmptyList() = toList().toNonEmptyList()
        /**
         * Converts the `LongArray` to a `NonEmptyList`.
         *
         * This function first transforms the `LongArray` into a `List` and then wraps it in a `NonEmptyList`.
         * It is expected that the `LongArray` contains at least one element; otherwise, a runtime exception
         * will be thrown during the conversion.
         *
         * @receiver The `LongArray` to be converted into a `NonEmptyList`.
         * @return A `NonEmptyList` containing the elements of the `LongArray`.
         * @throws TooFewElementsException if the `LongArray` is empty.
         * @since 5.2.0
         */
        fun LongArray.toNonEmptyList() = toList().toNonEmptyList()
        /**
         * Converts the `DoubleArray` into a `NonEmptyList`.
         *
         * This function transforms the elements of the `DoubleArray` into a `List` and then wraps it in a `NonEmptyList`.
         * The input array is expected to be non-empty; otherwise, a runtime exception will occur.
         *
         * @receiver The source `DoubleArray` to be converted into a `NonEmptyList`.
         * @return A `NonEmptyList` containing the elements of the `DoubleArray`.
         * @throws TooFewElementsException if the array is empty.
         * @since 5.2.0
         */
        fun DoubleArray.toNonEmptyList() = toList().toNonEmptyList()
        /**
         * Converts the current `CharArray` into a `NonEmptyList`.
         *
         * This function transforms the elements of the `CharArray` into a `List` and wraps them in a `NonEmptyList`.
         * It is expected that the `CharArray` is non-empty; otherwise, a runtime exception will occur during conversion.
         *
         * @receiver The source `CharArray` to be converted into a `NonEmptyList`.
         * @return A `NonEmptyList` containing the elements of the given `CharArray`.
         * @throws TooFewElementsException if the `CharArray` is empty.
         * @since 5.2.0
         */
        fun CharArray.toNonEmptyList() = toList().toNonEmptyList()
    }

    /**
     * Compares the specified object with this object for equality.
     *
     * @param other The object to be compared for equality with this object.
     * @return `true` if the specified object is equal to this object, `false` otherwise.
     * @since 5.2.0
     */
    override fun equals(other: Any?) = elements == other
    /**
     * Computes the hash code of the `NonEmptyList` instance based on its elements.
     *
     * This method ensures that the hash code is consistent with the `equals` method,
     * meaning that two `NonEmptyList` instances that are considered equal will have the same hash code.
     *
     * @return The hash code value calculated using the hash codes of the elements within the list.
     * @since 5.2.0
     */
    override fun hashCode() = elements.hashCode()
    /**
     * Returns a string representation of the object.
     *
     * This method is overridden to provide a custom string representation
     * based on the contents of the `elements` property. The resulting string
     * is typically useful for debugging or logging purposes.
     *
     * @return A string that represents the current state or contents of the object.
     * @since 5.2.0
     */
    override fun toString() = elements.toString()

    /**
     * Applies the given transformation function to each element in the list and returns a new NonEmptyList of the transformed elements.
     *
     * @param transform a transformer function to apply to each element of the list.
     * @return a new NonEmptyList containing the transformed elements.
     * @since 5.2.0
     */
    inline fun <R> map(transform: Transformer<E, R>) =
        NonEmptyList(elements.map(transform))
    /**
     * Returns a new `NonEmptyList` containing the results of applying the given [transform] function
     * to each element in the list, providing sequential index and the element itself as parameters to [transform].
     *
     * @param transform A lambda function that takes the index of an element and the element itself as arguments,
     *                  and returns the transformed value of type `R`.
     * @return A new `NonEmptyList` containing the transformed elements.
     * @since 5.2.0
     */
    inline fun <R> mapIndexed(transform: (index: Int, E) -> R) =
        NonEmptyList(elements.mapIndexed(transform))

    /**
     * Returns a new `NonEmptyList` by appending the specified [element] to the current `NonEmptyList`.
     *
     * @param element The element to be added to the list.
     * @return A new `NonEmptyList` containing the elements of the current list followed by the new [element].
     * @since 5.2.0
     */
    operator fun plus(element: @UnsafeVariance E) = NonEmptyList(elements + element)
    /**
     * Returns a new `NonEmptyList` by appending the elements from the specified `other` iterable
     * to the current `NonEmptyList`.
     *
     * @param other The iterable whose elements will be added to the current `NonEmptyList`.
     * @return A new `NonEmptyList` containing the elements of the current list followed by the elements of `other`.
     * @since 5.2.0
     */
    operator fun plus(other: Iterable<@UnsafeVariance E>) = NonEmptyList(elements + other)
    /**
     * Returns a new `NonEmptyList` containing the elements of this list in reversed order.
     *
     * This method ensures that the resulting list is still a `NonEmptyList`, guaranteeing
     * the non-empty property is preserved.
     *
     * @return A new `NonEmptyList` with the elements in reverse order.
     * @since 5.2.0
     */
    fun reversed() = NonEmptyList(elements.reversed())
    /**
     * Returns a new `NonEmptyList` containing all elements of the current list
     * sorted according to the specified [comparator].
     *
     * @param comparator The comparator used to determine the order of the elements.
     *                   A null argument is not allowed and must be a valid comparator.
     * @return A `NonEmptyList` where the elements are sorted in the order defined by the [comparator].
     * @since 5.2.0
     */
    fun sorted(comparator: Comparator<in E>) = NonEmptyList(elements.sortedWith(comparator))

    /**
     * Indicates whether the collection is empty.
     *
     * This implementation always returns `false` because a `NonEmptyList` is guaranteed
     * to contain at least one element.
     *
     * @return `false` as a `NonEmptyList` can never be empty.
     * @since 5.2.0
     */
    override fun isEmpty() = false
}

/**
 * Creates a `NonEmptyList` containing the provided head element and additional tail elements.
 *
 * This function guarantees that the resulting list is non-empty by requiring at least one
 * `head` parameter. The `tail` parameter allows specifying additional elements.
 *
 * @param E the type of elements contained in the resulting `NonEmptyList`
 * @param head the first element of the `NonEmptyList`, which is mandatory
 * @param tail the optional additional elements to be added to the `NonEmptyList`
 * @return a `NonEmptyList` containing the `head` element followed by the elements in `tail`
 * @since 5.2.0
 */
fun <E> nonEmptyListOf(head: E, vararg tail: E) =
    NonEmptyList(head.asSingleList() + tail)

/**
 * Represents a mutable list that is guaranteed to always have at least one element.
 * This class enforces the non-empty constraint by preventing operations that would
 * result in an empty list.
 *
 * @param E the type of elements contained in this list.
 * @property mElements the internal representation of the list elements.
 * @property head the first element in the list.
 * @property tail the rest of the elements in the list after the head.
 * @property size the total number of elements in the list.
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
@Suppress("UNCHECKED_CAST")
class NonEmptyMList<E>(private val mElements: MList<E>) : MList<E> by mElements, NonEmptyList<E>(mElements), NonEmptyMCollection<E> {

    /**
     * A guard to prevent operations that would leave the NonEmptyMList empty.
     *
     * @since 5.2.0
     */
    private val guard = RemovalGuard(
        { elements.size },
        "Operation would leave NonEmptyMList empty",
    )

    /**
     * Retrieves the first element of the non-empty mutable list.
     *
     * This property provides access to the "head" of the list, which is guaranteed
     * to exist since the list cannot be empty. The "head" corresponds to the first
     * element in the list's underlying storage structure.
     * @since 5.2.0
     */
    override val head get() = mElements.first()
    /**
     * Represents the sublist of elements in the list, excluding the first element.
     *
     * This property provides access to all elements in the list except the head. It ensures that the
     * list remains non-empty by always including at least one element in the original list. The
     * resulting sublist is backed by the original list, so changes in the sublist are reflected
     * in the original list and vice versa.
     *
     * @return A sublist containing all elements of the list except the first one.
     * @since 5.2.0
     */
    override val tail: MList<E> get() = mElements.subList(1, mElements.size)

    /**
     * Represents the number of elements contained within the collection.
     * This property provides the current count of elements stored.
     * @since 5.2.0
     */
    override val size: Int get() = mElements.size

    /**
     * Secondary constructor for the NonEmptyMList class, allowing initialization
     * with a variable number of elements. The provided elements are converted
     * into a mutable list internally.
     *
     * @param elements the list of elements to initialize the NonEmptyMList with.
     * @since 5.2.0
     */
    constructor(vararg elements: E) : this(elements.toMList() as MList<E>)

    init { mElements.isEmpty() && throw TooFewElementsException("Elements are empty.") }

    companion object {
        /**
         * Creates a new instance of a `NonEmptyMList` containing the provided elements.
         *
         * @param E The type of the elements in the list.
         * @param elements A vararg of elements to be included in the `NonEmptyMList`.
         * @return A `NonEmptyMList` containing the provided elements.
         * @since 5.2.0
         */
        operator fun <E> of(vararg elements: E): NonEmptyMList<E> = NonEmptyMList(elements.toMList() as MList<E>)

        /**
         * Converts the current iterable to a `NonEmptyMList`, which is a mutable list
         * guaranteed to always contain at least one element.
         *
         * This method assumes that the current iterable is non-empty. The elements
         * of the iterable are first converted into a mutable list using `toMList`,
         * and then wrapped into a `NonEmptyMList`.
         *
         * @receiver The iterable to be converted into a `NonEmptyMList`.
         * @return A newly created `NonEmptyMList` containing the elements of the
         * original iterable.
         * @throws TooFewElementsException if the receiver iterable is empty, as
         * `NonEmptyMList` requires at least one element.
         */
        fun <E> Iterable<E>.toNonEmptyMList() = NonEmptyMList(this.toMList())
        /**
         * Converts the current iterable into a `NonEmptyMList` if it contains at least one element,
         * or returns `null` if the iterable is empty or an exception occurs during the conversion process.
         *
         * The conversion leverages the `tryOrNull` function to handle exceptions that may arise
         * when attempting to create a `NonEmptyMList`, ensuring a null-safe operation. This is useful
         * when working with collections where non-emptiness must be enforced at runtime.
         *
         * @receiver The iterable collection to be converted.
         * @return An instance of `NonEmptyMList` if the conversion is successful and the iterable is non-empty,
         * or `null` otherwise.
         * @since 5.2.0
         */
        fun <E> Iterable<E>.toNonEmptyMListOrNull() = tryOrNull { NonEmptyMList(this.toMList()) }
        /**
         * Converts the current iterable into a NonEmptyMList. If the iterable is empty or if an exception occurs during the
         * conversion, it falls back to a provided default NonEmptyMList.
         *
         * @param default A supplier function that provides the default NonEmptyMList to be used in case of an empty iterable
         *                or when an exception is encountered during conversion.
         * @return A NonEmptyMList containing the elements of the current iterable, or the supplied default NonEmptyMList if
         *         the iterable is empty or a failure occurs.
         */
        fun <E> Iterable<E>.toNonEmptyMListOrDefault(default: Supplier<NonEmptyMList<E>>) =
            tryOr({ default() }) { NonEmptyMList(this.toMList()) }

        /**
         * Converts an array of elements into a `NonEmptyMList`. The resulting list is mutable and
         * guaranteed to contain at least one element.
         *
         * This method first converts the array into a mutable list and then transforms it into a
         * `NonEmptyMList`. The operation assumes that the array is non-empty and will fail if the
         * array has no elements.
         *
         * @receiver The input array to be converted into a `NonEmptyMList`.
         * @return A mutable `NonEmptyMList` containing the elements of the array.
         * @throws TooFewElementsException if the array is empty, as creating a `NonEmptyMList`
         * requires at least one element.
         * @since 5.2.0
         */
        fun <T> Array<T>.toNonEmptyMList() = toMutableList().toNonEmptyMList()
        /**
         * Converts the current `IntArray` to a `NonEmptyMList`.
         *
         * This function first transforms the `IntArray` into a mutable list.
         * Then, it converts the mutable list into a `NonEmptyMList`, which is a specialized list
         * that guarantees at least one element is always present.
         *
         * @receiver The `IntArray` to be converted.
         * @return A `NonEmptyMList` containing the elements of the original `IntArray`.
         * @throws TooFewElementsException If the `IntArray` is empty, as a `NonEmptyMList` cannot be empty.
         * @since 5.2.0
         */
        fun IntArray.toNonEmptyMList() = toMutableList().toNonEmptyMList()
        /**
         * Converts the current `LongArray` into a `NonEmptyMList`.
         *
         * The method first converts the array into a mutable list, then transforms
         * it into a `NonEmptyMList`. The resulting `NonEmptyMList` is guaranteed
         * to contain at least one element if the original array is not empty.
         *
         * @receiver The `LongArray` to be converted into a `NonEmptyMList`.
         * @return A `NonEmptyMList` containing all elements of the original array.
         * @throws TooFewElementsException if the array is empty, as a `NonEmptyMList`
         * requires at least one element.
         * @since 5.2.0
         */
        fun LongArray.toNonEmptyMList() = toMutableList().toNonEmptyMList()
        /**
         * Converts a `DoubleArray` into a `NonEmptyMList` after transforming it into a mutable list.
         *
         * This extension function ensures that the resulting list is non-empty by leveraging
         * the `toNonEmptyMList` method of the mutable list. If the `DoubleArray` is empty, an exception
         * or error may be thrown, as `NonEmptyMList` enforces a constraint that it must always contain
         * at least one element.
         *
         * @receiver The `DoubleArray` to be converted into a `NonEmptyMList`.
         * @return A `NonEmptyMList` containing all the elements of the original `DoubleArray`.
         * @throws IllegalStateException or a similar exception if the `DoubleArray` is empty.
         * @since 5.2.0
         */
        fun DoubleArray.toNonEmptyMList() = toMutableList().toNonEmptyMList()
        /**
         * Converts this `CharArray` to a `NonEmptyMList`, ensuring that the resulting list is non-empty.
         *
         * @return A `NonEmptyMList` containing the elements of this `CharArray` in the same order.
         * @throws TooFewElementsException if the `CharArray` is empty, as a `NonEmptyMList` must
         *         always contain at least one element.
         * @since 5.2.0
         */
        fun CharArray.toNonEmptyMList() = toMutableList().toNonEmptyMList()
    }

    /**
     * Compares this object with the specified object for equality.
     *
     * @param other The object to be compared with this instance.
     * @return `true` if the specified object is equal to this instance, `false` otherwise.
     * @since 5.2.0
     */
    override fun equals(other: Any?) = mElements == other
    /**
     * Computes the hash code for the current object based on the `elements` property.
     *
     * This method overrides the default `hashCode` implementation to provide
     * a hash code that is consistent with the `equals` method. The hash code
     * is derived from the `elements` property, ensuring that objects with
     * the same content in `elements` produce the same hash code.
     *
     * @return The hash code value for the object.
     */
    override fun hashCode() = mElements.hashCode()
    /**
     * Returns a string representation of the object.
     * The specific format and content of the returned string depend on the elements contained.
     *
     * @return A string representation of the elements in the object.
     * @since 5.2.0
     */
    override fun toString() = mElements.toString()

    /**
     * Checks if the list is empty.
     *
     * @return always returns `false` as this list is guaranteed to be non-empty.
     * @since 5.2.0
     */
    override fun isEmpty() = false

    /**
     * Removes the element at the specified position in this list and returns it.
     * The list must contain at least two elements, as removing the last remaining
     * element is not allowed.
     *
     * @param index the position of the element to be removed, which must be within the bounds of the list.
     * @return the element that was removed from the list.
     * @throws TooFewElementsException if the list contains only one element.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun removeAt(index: Int): E {
        guard.check(1)
        return mElements.removeAt(index)
    }

    /**
     * Removes the specified element from the list if it is present.
     *
     * This method ensures that the last element of the list cannot be removed,
     * maintaining the invariant that the list always contains at least one element.
     *
     * @param element The element to be removed from the list.
     * @return `true` if the element was successfully removed, `false` otherwise.
     *         Returns `false` if the element was not present in the list.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun remove(element: E): Boolean {
        if (element !in mElements) return false
        guard.check(1)
        return mElements.remove(element)
    }

    /**
     * Removes all elements in the specified collection from this list. If the operation would remove
     * the last remaining element of the list, a TooFewElementsException is thrown.
     *
     * @param elements The collection of elements to be removed from the list.
     * @return `true` if the list was modified as a result of this operation, `false` otherwise.
     * @throws TooFewElementsException if the operation would leave the list empty.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun removeAll(elements: Collection<E>): Boolean {
        val victims = elements.toSet()
        guard.check(this.mElements.count { it in victims })
        return this.mElements.removeAll(victims)
    }

    /**
     * Retains only the elements in this list that are also contained in the specified collection.
     * Any elements not contained in the specified collection are removed from this list.
     * If the operation would leave the list empty, a TooFewElementsException is thrown.
     *
     * @param elements the collection containing elements to be retained in this list
     * @return `true` if the list was modified as a result of this operation, `false` otherwise
     * @throws TooFewElementsException if the operation would result in the list leaving its non-empty state
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun retainAll(elements: Collection<E>): Boolean {
        val survivors = elements.toSet()
        guard.check(this.mElements.count { it !in survivors })
        return this.mElements.retainAll(survivors)
    }

    /**
     * Clears all elements from the list.
     *
     * This operation is not supported for `NonEmptyMList` as the list must always contain at least 
     * one element. Attempting to clear the list will result in an `UnsupportedOperationException`.
     *
     * @throws UnsupportedOperationException always, as clearing a `NonEmptyMList` is not allowed.
     * @since 5.2.0
     */
    override fun clear() = throw UnsupportedOperationException("Cannot clear a NonEmptyMList")

    /**
     * Provides an iterator to traverse through the elements in the collection.
     * The iterator ensures guarded access to the underlying elements.
     *
     * @return An instance of MIterator<E> for safely iterating over the elements.
     * @since 5.2.0
     */
    override fun iterator(): MIterator<E> = GuardedIterator(mElements.iterator(), guard)
    /**
     * Provides a list iterator for traversing the elements of the `NonEmptyMList`.
     * The iterator ensures that operations maintain the invariants of the `NonEmptyMList`,
     * such as preventing removal of the last remaining element.
     *
     * @return An `MListIterator<E>` instance for guarded iteration over the list elements.
     * @since 5.2.0
     */
    override fun listIterator(): MListIterator<E> = GuardedListIterator(mElements.listIterator(), guard)
    /**
     * Returns a list iterator over the elements in this list, starting at the specified position.
     * The returned iterator allows bidirectional traversal of the list and provides guarded access 
     * to ensure the list's invariants are maintained.
     *
     * @param index The index of the first element to be returned by the list iterator, 
     *              where `0` corresponds to the first element in the list.
     * @return An instance of `MListIterator<E>` for traversing the elements in the list.
     * @throws IndexOutOfBoundsException if the specified index is out of range 
     *                                   (`index < 0 || index > size`).
     * @since 5.2.0
     */
    override fun listIterator(index: Int): MListIterator<E> = GuardedListIterator(mElements.listIterator(index), guard)

    /**
     * Checks if the current collection contains all elements of the specified collection.
     *
     * @param elements The collection of elements to check for containment.
     * @return True if all elements in the specified collection are present in the current collection, false otherwise.
     * @since 5.2.0
     */
    override fun containsAll(elements: Collection<E>) = this.mElements.containsAll(elements)

    /**
     * Returns the index of the first occurrence of the specified element in the list,
     * or -1 if the element is not present.
     *
     * @param element The element to search for in the list.
     * @return The index of the first occurrence of the element, or -1 if not found.
     */
    override fun indexOf(element: E) = mElements.indexOf(element)

    /**
     * Finds the last occurrence of the specified element in the list.
     *
     * @param element The element to locate in the list.
     * @return The index of the last occurrence of the specified element in the list,
     * or -1 if the element is not found.
     * @since 5.2.0
     */
    override fun lastIndexOf(element: E) = mElements.lastIndexOf(element)

    /**
     * Checks if the specified element is present in the collection.
     *
     * @param element The element to check for its presence in the collection.
     * @return `true` if the element is found in the collection, otherwise `false`.
     * @since 5.2.0
     */
    override fun contains(element: E) = mElements.contains(element)

    /**
     * Retrieves the element at the specified index from the collection.
     *
     * @param index The position of the element to retrieve, where 0 is the first element.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size).
     * @since 5.2.0
     */
    override fun get(index: Int) = mElements[index]

    /**
     * Returns a view of the portion of this list between the specified `fromIndex`, inclusive,
     * and `toIndex`, exclusive. The returned sublist is backed by this list, so changes in
     * the returned sublist are reflected in this list, and vice versa.
     *
     * @param fromIndex the starting index of the sublist (inclusive).
     * @param toIndex the ending index of the sublist (exclusive).
     * @return a view of the specified range within this list.
     * @throws IndexOutOfBoundsException if `fromIndex` or `toIndex` is out of range
     * (`fromIndex < 0`, `toIndex > size`, or `fromIndex > toIndex`).
     * @since 5.2.0
     */
    override fun subList(fromIndex: Int, toIndex: Int): MList<E> = GuardedMList(mElements.subList(fromIndex, toIndex), guard)

    /**
     * Removes all elements from the collection that satisfy the given predicate.
     *
     * @param predicate A condition that determines whether an element should be removed.
     *                  Elements for which the predicate returns `true` will be removed.
     * @return `true` if any elements were removed as a result of this operation, `false` otherwise.
     * @since 5.2.0
     */
    override fun removeIf(predicate: java.util.function.Predicate<in E>): Boolean {
        guard.check(mElements.count { predicate.test(it) })
        return mElements.removeIf(predicate)
    }
    /**
     * Removes all elements from the collection that match the given predicate.
     *
     * @param predicate A condition used to determine which elements to remove.
     * @since 5.2.0
     */
    operator fun minusAssign(predicate: Predicate<E>) {
        val _ = removeIf(predicate)
    }
}

/**
 * Creates a `NonEmptyMList` using the specified `head` element and optional `tail` elements.
 *
 * The `NonEmptyMList` is a mutable list that guarantees it will always contain at least one element.
 *
 * @param head The first element of the `NonEmptyMList`, which is required and ensures non-emptiness.
 * @param tail The optional additional elements to be included in the `NonEmptyMList`.
 * @return A `NonEmptyMList` containing the specified `head` and `tail` elements.
 * @since 5.2.0
 */
fun <E> nonEmptyMListOf(head: E, vararg tail: E) =
    NonEmptyMList((head.asSingleList() + tail).toMList())

/**
 * Represents a non-empty set, ensuring that at least one element is always present.
 * This class delegates most of its functionality to a standard [Set], while adding the
 * constraint for non-emptiness and providing additional utilities for manipulation.
 *
 * @param E The type of elements in the set.
 * @property elements The backing [Set] that contains the elements of this instance.
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
open class NonEmptySet<out E>(@PublishedApi internal val elements: Set<E>) : Set<E> by elements, NonEmptyCollection<E> {
    /**
     * Secondary constructor for creating a [NonEmptySet] from a variable number of elements.
     *
     * @param elements The elements to initialize the non-empty set with.
     * @throws TooFewElementsException If the provided elements array is empty.
     */
    constructor(vararg elements: E) : this(elements.toSet())

    init { elements.isEmpty() && throw TooFewElementsException("Elements are empty.") }

    /**
     * Provides utility functions and factory methods for creating and manipulating instances of `NonEmptySet`.
     * @since 5.2.0
     */
    companion object {
        /**
         * Creates an instance of `NonEmptySet` containing the provided elements.
         *
         * @param elements The elements to include in the `NonEmptySet`.
         * @return A `NonEmptySet` containing the specified elements.
         * @since 5.2.0
         */
        operator fun <E> of(vararg elements: E) = NonEmptySet(elements.toSet())

        /**
         * Converts the elements of the iterable into a [NonEmptySet].
         *
         * This method assumes that the iterable contains at least one element.
         * If the resulting set is empty, an exception will be thrown.
         *
         * @return A [NonEmptySet] containing all unique elements from the iterable.
         * @throws TooFewElementsException if the resulting set has no elements.
         * @since 5.2.0
         */
        fun <E> Iterable<E>.toNonEmptySet() = NonEmptySet(this.toSet())
        /**
         * Converts the iterable into a [NonEmptySet] if it contains at least one element, or returns `null` if the result is empty.
         *
         * This function attempts to create a [NonEmptySet] from the elements of the iterable. If the iterable is empty or any exception
         * occurs during the creation process, `null` is returned.
         *
         * @receiver The iterable to be converted into a [NonEmptySet].
         * @return A [NonEmptySet] containing the elements of the iterable, or `null` if the iterable is empty or an exception occurs.
         * @since 5.2.0
         */
        fun <E> Iterable<E>.toNonEmptySetOrNull() = tryOrNull { NonEmptySet(this.toSet()) }
        /**
         * Converts the elements of the iterable into a [NonEmptySet]. If the iterable is empty or cannot be converted,
         * the provided default supplier is used to generate a fallback [NonEmptySet].
         *
         * @param E The type of elements in the iterable.
         * @param default A supplier function that provides a fallback [NonEmptySet] if the conversion fails.
         * @return A [NonEmptySet] containing the elements of the iterable or the result of the default supplier.
         * @since 5.2.0
         */
        fun <E> Iterable<E>.toNonEmptySetOrDefault(default: Supplier<NonEmptySet<E>>) =
            tryOr({ default() }) { NonEmptySet(this.toSet()) }

        /**
         * Converts the elements of the array into a [NonEmptySet].
         *
         * This function assumes that the array contains at least one element.
         * If the resulting set is empty, an exception will be thrown.
         *
         * @receiver The array whose elements will be converted into a [NonEmptySet].
         * @return A [NonEmptySet] containing all unique elements from the array.
         * @throws TooFewElementsException if the resulting set has no elements.
         * @since 5.2.0
         */
        fun <T> Array<T>.toNonEmptySet() = toSet().toNonEmptySet()
        /**
         * Converts the elements of the `IntArray` into a [NonEmptySet].
         *
         * This method assumes that the `IntArray` contains at least one element.
         * If the resulting set is empty, an exception will be thrown.
         *
         * @return A [NonEmptySet] containing all unique elements from the `IntArray`.
         * @throws TooFewElementsException if the resulting set has no elements.
         * @since 5.2.0
         */
        fun IntArray.toNonEmptySet() = toSet().toNonEmptySet()
        /**
         * Converts the elements of the `LongArray` into a [NonEmptySet].
         *
         * This method ensures the resulting set is never empty. If the `LongArray`
         * does not contain any elements, an exception will be thrown.
         *
         * @receiver The `LongArray` to be converted into a [NonEmptySet].
         * @return A [NonEmptySet] containing all unique elements from the `LongArray`.
         * @throws TooFewElementsException if the resulting set has no elements.
         * @since 5.2.0
         */
        fun LongArray.toNonEmptySet() = toSet().toNonEmptySet()
        /**
         * Converts the elements of the `DoubleArray` into a [NonEmptySet].
         *
         * This function processes the array by first converting it into a regular `Set`
         * and then attempts to create a [NonEmptySet] from the resulting collection of unique elements.
         *
         * @return A [NonEmptySet] containing all unique elements from the `DoubleArray`.
         * @throws TooFewElementsException if the resulting set has no elements.
         * @since 5.2.0
         */
        fun DoubleArray.toNonEmptySet() = toSet().toNonEmptySet()
        /**
         * Converts the elements of the `CharArray` into a `NonEmptySet`.
         *
         * This extension function first transforms the `CharArray` into a standard `Set`,
         * and subsequently wraps it in a `NonEmptySet`. It assumes that the original
         * `CharArray` contains at least one element, as a `NonEmptySet` requires
         * at least one element to function correctly.
         *
         * @return A `NonEmptySet` containing all unique characters from the `CharArray`.
         * @throws TooFewElementsException if the resulting set contains no elements.
         * @since 5.2.0
         */
        fun CharArray.toNonEmptySet() = toSet().toNonEmptySet()
    }

    /**
     * Checks if the current object is equal to the specified object.
     *
     * @param other The object to compare with the current instance.
     * @return `true` if the objects are considered equal based on their elements, `false` otherwise.
     * @since 5.2.0
     */
    override fun equals(other: Any?) = elements == other
    /**
     * Computes the hash code for this `NonEmptySet` instance based on its underlying elements.
     *
     * This method ensures that the hash code is consistent with the `equals` implementation
     * for instances of `NonEmptySet`. The hash code is derived from the hash code of the `elements`
     * property, which represents the contents of the set.
     *
     * @return The hash code of the underlying `elements` property.
     * @since 5.2.0
     */
    override fun hashCode() = elements.hashCode()
    /**
     * Returns a string representation of the object.
     *
     * This method is overridden to provide a textual representation of the
     * `elements` property, delegating to its `toString` implementation.
     *
     * @return The string representation of the `elements` property.
     * @since 5.2.0
     */
    override fun toString() = elements.toString()

    /**
     * Always returns `false`, indicating that this set is never empty.
     *
     * This method overrides the `isEmpty` function in the `Set` interface,
     * as `NonEmptySet` guarantees that it always contains at least one element.
     *
     * @return `false`
     * @since 5.2.0
     */
    override fun isEmpty() = false

    /**
     * Applies the given transformation function to each element in the list and returns a new `NonEmptyList` containing the transformed elements.
     *
     * @param R The type of the elements in the resulting `NonEmptyList`.
     * @param transform A function that maps each element of the current list to a value of type `R`.
     * @return A new `NonEmptyList` containing the transformed elements.
     * @since 5.2.0
     */
    inline fun <R> map(transform: Transformer<E, R>) =
        NonEmptyList(elements.map(transform))

    /**
     * Transforms each element in the current `NonEmptySet` using the provided `transform` function
     * and returns a new `NonEmptySet` with the transformed elements.
     *
     * @param transform A function that takes an element of type `E` from the current set
     * and maps it to a new element of type `R`.
     * @return A new `NonEmptySet` containing the transformed elements.
     * @since 5.2.0
     */
    inline fun <R> mapToSet(transform: Transformer<E, R>) =
        NonEmptySet(elements.map(transform).toSet())

    /**
     * Creates a new `NonEmptySet` by adding the specified element to the current set.
     * If the element already exists in the set, the set remains unchanged.
     *
     * @param element The element to be added to the set.
     * @return A new `NonEmptySet` containing all elements of the current set and the specified element.
     * @since 5.2.0
     */
    operator fun plus(element: @UnsafeVariance E): NonEmptySet<E> =
        NonEmptySet(elements + element)
    /**
     * Returns a new `NonEmptySet` that contains all the elements of the current set
     * combined with the elements from the provided iterable.
     *
     * @param other An iterable containing elements to be added to the set.
     * @return A new `NonEmptySet` containing the elements of the current set and the elements of the given iterable.
     * @since 5.2.0
     */
    operator fun plus(other: Iterable<@UnsafeVariance E>): NonEmptySet<E> =
        NonEmptySet(elements + other)
}

/**
 * Creates a `NonEmptySet` containing the given head element followed by elements from the provided vararg tail.
 *
 * @param head the element to be used as the first and mandatory element of the resulting set.
 * @param tail the remaining elements to be included in the set. Can be an empty array.
 * @return a `NonEmptySet` containing the head element and all elements from the tail.
 * @since 5.2.0
 */
fun <E> nonEmptySetOf(head: E, vararg tail: E) =
    NonEmptySet(head.asSingleSet() + tail)

/**
 * A class representing a non-empty mutable set (MSet) that enforces the invariant of always
 * containing at least one element. This class provides various operations to manage elements
 * while ensuring the non-empty property of the set is maintained.
 *
 * @param E The type of elements contained in the set.
 * @property mElements The underlying mutable set (MSet) that stores the elements of this NonEmptyMSet.
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
@Suppress("UNCHECKED_CAST")
class NonEmptyMSet<E>(private val mElements: MSet<E>) : MSet<E> by mElements, NonEmptySet<E>(mElements), NonEmptyMCollection<E> {

    /**
     * Returns the total number of elements in the collection.
     * This property is a read-only override and retrieves its value
     * based on the current size of the `elements` collection.
     * @since 5.2.0
     */
    override val size: Int get() = mElements.size

    /**
     * A guard mechanism to prevent operations that would leave the `NonEmptySet` empty.
     *
     * The `guard` ensures that removing elements from the `NonEmptySet` does not violate
     * the invariant that the set must always contain at least one element. It utilizes the
     * size of `elements` and throws an exception with a descriptive message if the removal
     * would result in an empty set.
     *
     * @see RemovalGuard
     * @since 5.2.0
     */
    private val guard = RemovalGuard(
        { elements.size },
        "Operation would leave NonEmptyMSet empty",
    )

    /**
     * Constructs a NonEmptyMSet from the provided elements.
     *
     * This constructor creates a new instance of NonEmptyMSet by converting the provided vararg
     * elements into a mutable set (`MSet`) using the `toMSet` extension function. The resulting
     * `MSet` is then used to initialize the instance.
     *
     * @param elements The elements to initialize the NonEmptyMSet with. Must contain at least one element.
     * @since 5.2.0
     */
    constructor(vararg elements: E) : this(elements.toMSet() as MSet<E>)

    init { mElements.isEmpty() && throw TooFewElementsException("Elements are empty.") }

    companion object {
        /**
         * Creates a `NonEmptyMSet` with the provided elements.
         *
         * @param E the type of elements in the set.
         * @param elements the elements to be included in the `NonEmptyMSet`.
         * @return a new instance of `NonEmptyMSet` containing the given elements.
         * @since 5.2.0
         */
        operator fun <E> of(vararg elements: E): NonEmptyMSet<E> = NonEmptyMSet(elements.toMSet() as MSet<E>)

        /**
         * Converts the current [Iterable] into a [NonEmptyMSet], ensuring that the resulting set contains
         * at least one element. The conversion process involves transforming the [Iterable] into a mutable set
         * and wrapping it into a [NonEmptyMSet].
         *
         * @receiver The [Iterable] to be converted into a non-empty mutable set.
         * @return A [NonEmptyMSet] containing the elements of the original [Iterable].
         * @throws TooFewElementsException If the original [Iterable] is empty.
         * @since 5.2.0
         */
        fun <E> Iterable<E>.toNonEmptyMSet() = NonEmptyMSet(this.toMSet())
        /**
         * Converts an `Iterable` into a `NonEmptyMSet` if possible, or returns `null` if the conversion fails.
         *
         * This function attempts to create a `NonEmptyMSet` from the elements of the current `Iterable`.
         * If the conversion fails (e.g., if the `Iterable` is empty or an exception occurs during conversion),
         * it will catch the exception and return `null` instead.
         *
         * @return A `NonEmptyMSet` containing the elements of the current `Iterable` if successful, or `null` otherwise.
         * @since 5.2.0
         */
        fun <E> Iterable<E>.toNonEmptyMSetOrNull() = tryOrNull { NonEmptyMSet(this.toMSet()) }
        /**
         * Converts the iterable to a `NonEmptyMSet`. If the iterable is empty or an exception occurs during the process,
         * a default `NonEmptyMSet` provided by the supplier is returned.
         *
         * @param E The type of elements contained in the iterable and the resulting `NonEmptyMSet`.
         * @param default A supplier function that provides a default `NonEmptyMSet` to be used if the iterable is empty
         *                or if an exception is thrown during the conversion.
         * @return A `NonEmptyMSet` containing the elements from the iterable, or the default `NonEmptyMSet` if applicable.
         * @since 5.2.0
         */
        fun <E> Iterable<E>.toNonEmptyMSetOrDefault(default: Supplier<NonEmptyMSet<E>>) =
            tryOr({ default() }) { NonEmptyMSet(this.toMSet()) }

        /**
         * Converts the current array into a [NonEmptyMSet], ensuring that the resulting set contains
         * at least one element. This method transforms the array into a mutable set and wraps it
         * into a [NonEmptyMSet].
         *
         * @receiver The array to be converted into a non-empty mutable set.
         * @return A [NonEmptyMSet] containing the elements of the original array.
         * @throws TooFewElementsException If the array is empty.
         * @since 5.2.0
         */
        fun <T> Array<T>.toNonEmptyMSet() = toMutableSet().toNonEmptyMSet()
        /**
         * Converts the current [IntArray] into a [NonEmptyMSet], ensuring that the resulting set contains
         * at least one element. This method transforms the [IntArray] into a mutable set and then wraps it
         * into a [NonEmptyMSet].
         *
         * @receiver The [IntArray] to be converted into a non-empty mutable set.
         * @return A [NonEmptyMSet] containing the elements of the original [IntArray].
         * @throws TooFewElementsException If the original [IntArray] is empty.
         * @since 5.2.0
         */
        fun IntArray.toNonEmptyMSet() = toMutableSet().toNonEmptyMSet()
        /**
         * Converts the current [LongArray] into a [NonEmptyMSet], ensuring that the resulting set contains
         * at least one element. The conversion process involves transforming the [LongArray] into a mutable set
         * and wrapping it into a [NonEmptyMSet].
         *
         * @receiver The [LongArray] to be converted into a non-empty mutable set.
         * @return A [NonEmptyMSet] containing the elements of the original [LongArray].
         * @throws TooFewElementsException If the original [LongArray] is empty.
         * @since 5.2.0
         */
        fun LongArray.toNonEmptyMSet() = toMutableSet().toNonEmptyMSet()
        /**
         * Converts the current [DoubleArray] into a [NonEmptyMSet], ensuring the resulting set
         * contains at least one element. The conversion involves transforming the array into
         * a mutable set and wrapping it into a [NonEmptyMSet].
         *
         * @receiver The [DoubleArray] to be converted into a non-empty mutable set.
         * @return A [NonEmptyMSet] containing the elements of the original [DoubleArray].
         * @throws TooFewElementsException If the original [DoubleArray] is empty.
         * @since 5.2.0
         */
        fun DoubleArray.toNonEmptyMSet() = toMutableSet().toNonEmptyMSet()
        /**
         * Converts the current [CharArray] into a [NonEmptyMSet], ensuring that the resulting set contains
         * at least one element. The conversion involves transforming the [CharArray] into a mutable set
         * and wrapping it into a [NonEmptyMSet].
         *
         * @receiver The [CharArray] to be converted into a non-empty mutable set.
         * @return A [NonEmptyMSet] containing the elements of the original [CharArray].
         * @throws TooFewElementsException If the original [CharArray] is empty.
         * @since 5.2.0
         */
        fun CharArray.toNonEmptyMSet() = toMutableSet().toNonEmptyMSet()
    }

    /**
     * Compares this object with the specified object for equality.
     *
     * @param other The object to be compared with this instance for equality.
     * @return `true` if the specified object is equal to this instance, `false` otherwise.
     * @since 5.2.0
     */
    override fun equals(other: Any?) = mElements == other
    /**
     * Computes the hash code for the `NonEmptyMSet` instance.
     *
     * The hash code is derived from the underlying collection of elements,
     * ensuring consistency with the `equals` implementation. It reflects
     * the current state of the `mElements` property.
     *
     * @return The hash code value for this collection.
     * @since 5.2.0
     */
    override fun hashCode() = mElements.hashCode()
    /**
     * Returns a string representation of the object.
     *
     * This method overrides the default `toString` implementation to
     * provide a string representation based on the `mElements` property.
     *
     * @return A string that represents the contents of `mElements`.
     * @since 5.2.0
     */
    override fun toString() = mElements.toString()

    /**
     * Checks if the collection is empty.
     *
     * This implementation always returns `false`, indicating that the collection
     * is non-empty by definition.
     *
     * @return `false`, as the collection is never empty.
     * @since 5.2.0
     */
    override fun isEmpty() = false

    /**
     * Checks if the collection contains all the elements present in the specified collection.
     *
     * @param elements The collection of elements to check for containment.
     * @return `true` if all elements in the specified collection are present, otherwise `false`.
     * @since 5.2.0
     */
    override fun containsAll(elements: Collection<E>) = this.mElements.containsAll(elements)
    /**
     * Checks if the specified element is present in the collection.
     *
     * @param element The element to check for its presence in the collection.
     * @return `true` if the element is found in the collection, otherwise `false`.
     * @since 5.2.0
     */
    override fun contains(element: E) = mElements.contains(element)

    /**
     * Removes the specified element from the collection.
     * Ensures that the collection remains non-empty after the operation by validating the minimum size.
     *
     * @param element the element to be removed from the collection
     * @return `true` if the element was successfully removed, `false` if the element was not found
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun remove(element: E): Boolean {
        if (element !in mElements) return false
        guard.check(1)
        return mElements.remove(element)
    }

    /**
     * Removes all of the elements in the specified collection from this collection.
     * Ensures that the collection remains non-empty after the operation by validating the state.
     *
     * @param elements The collection containing elements to be removed from this collection.
     * @return `true` if the collection was modified as a result of this operation, `false` otherwise.
     * @throws TooFewElementsException if the operation would leave the collection empty.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun removeAll(elements: Collection<E>): Boolean {
        val victims = elements.toSet()
        guard.check(this.mElements.count { it in victims })
        return this.mElements.removeAll(victims)
    }

    /**
     * Retains only the elements in the current collection that are also contained in the specified collection.
     *
     * If this operation would result in the set becoming empty, an exception is thrown to ensure
     * that the collection remains non-empty.
     *
     * @param elements the collection containing elements to be retained in this set.
     * @return `true` if the set was modified as a result of this operation, `false` otherwise.
     * @throws TooFewElementsException if the operation would leave the collection empty.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun retainAll(elements: Collection<E>): Boolean {
        val survivors = elements.toSet()
        guard.check(this.mElements.count { it !in survivors })
        return this.mElements.retainAll(survivors)
    }

    /**
     * Throws an exception to indicate that the `clear` operation is not supported for the `NonEmptyMSet` collection.
     *
     * As `NonEmptyMSet` guarantees that it cannot be empty, this operation is intentionally not allowed.
     *
     * @throws UnsupportedOperationException always, as the collection cannot be cleared.
     * @since 5.2.0
     */
    override fun clear() = throw UnsupportedOperationException("Cannot clear a NonEmptyMSet")

    /**
     * Removes all elements from the collection that satisfy the given predicate.
     *
     * @param predicate A condition that determines whether an element should be removed.
     *                  Elements for which the predicate returns `true` will be removed.
     * @return `true` if any elements were removed as a result of this operation, `false` otherwise.
     * @since 5.2.0
     */
    override fun removeIf(predicate: java.util.function.Predicate<in E>): Boolean {
        guard.check(mElements.count { predicate.test(it) })
        return mElements.removeIf(predicate)
    }
    /**
     * Removes all elements from the collection that match the given predicate.
     *
     * @param predicate A condition used to determine which elements to remove.
     * @since 5.2.0
     */
    operator fun minusAssign(predicate: Predicate<E>) {
        val _ = removeIf(predicate)
    }

    /**
     * Returns an iterator over the elements of this collection.
     *
     * @return an iterator of type `MIterator<E>` that allows traversal and optional element removal.
     * @since 5.2.0
     */
    override fun iterator(): MIterator<E> = GuardedIterator(mElements.iterator(), guard)
}

/**
 * Creates a `NonEmptyMSet` from a mandatory head element and an optional vararg of additional elements.
 *
 * @param E The type of elements contained in the set.
 * @param head The first, mandatory element of the set. This ensures that the resulting set is never empty.
 * @param tail A vararg of additional elements to be included in the set. These elements are optional and will
 *             be added alongside the head element.
 * @return A `NonEmptyMSet` containing the `head` element and all elements from the `tail` vararg.
 * @since 5.2.0
 */
fun <E> nonEmptyMSetOf(head: E, vararg tail: E) =
    NonEmptyMSet((head.asSingleSet() + tail).toMSet())

internal class NonEmptyCollectionDeserializer<T : Any>(
    private val targetType: Class<T>,
    private val bufferType: Class<out Collection<*>>,
    private val wrap: (Collection<Any?>) -> T,
    private val contentType: tools.jackson.databind.JavaType? = null,
) : ValueDeserializer<T>() {

    override fun createContextual(ctxt: tools.jackson.databind.DeserializationContext, property: tools.jackson.databind.BeanProperty?): ValueDeserializer<*> {
        val wrapper = property?.type ?: ctxt.contextualType ?: return this
        return NonEmptyCollectionDeserializer(
            targetType, bufferType, wrap,
            wrapper.containedTypeOrUnknown(0),
        )
    }

    override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: tools.jackson.databind.DeserializationContext): T {
        val element = contentType ?: ctxt.constructType(Any::class.java)
        val buffer: Collection<Any?> =
            ctxt.readValue(p, ctxt.typeFactory.constructCollectionType(bufferType, element))

        return try {
            wrap(buffer)
        } catch (e: TooFewElementsException) {
            ctxt.reportInputMismatch<T>(
                targetType,
                "Cannot deserialize %s from an empty array: at least one element is required",
                targetType.simpleName,
            )
        }
    }
}

internal class NonEmptyCollectionOldDeserializer<T : Any>(
    private val targetType: Class<T>,
    private val bufferType: Class<out Collection<*>>,
    private val wrap: (Collection<Any?>) -> T,
    private val contentType: JavaType? = null,
) : JsonDeserializer<T>(), ContextualDeserializer {

    override fun createContextual(ctxt: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
        val wrapper = property?.type ?: ctxt.contextualType
        val content = wrapper?.containedTypeOrUnknown(0) ?: ctxt.constructType(Any::class.java)
        return NonEmptyCollectionOldDeserializer(targetType, bufferType, wrap, content)
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        val element = contentType ?: ctxt.constructType(Any::class.java)
        val buffer: Collection<Any?> =
            ctxt.readValue(p, ctxt.typeFactory.constructCollectionType(bufferType, element))

        return try {
            wrap(buffer)
        } catch (e: TooFewElementsException) {
            throw MismatchedInputException.from(
                p, targetType,
                "Cannot deserialize ${targetType.simpleName} from an empty array: at least one element is required",
            ).also { it.initCause(e) }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class NonEmptyCollectionsModule : SimpleModule("kutils-non-empty-collections") {

    init {
        val _ = register(NonEmptyList::class.java, ArrayList::class.java) { NonEmptyList(it.toList()) }
        val _ = register(NonEmptyMList::class.java, ArrayList::class.java) { NonEmptyMList(it.toMutableList()) }
        val _ = register(NonEmptySet::class.java, LinkedHashSet::class.java) { NonEmptySet(it.toSet()) }
        val _ = register(NonEmptyMSet::class.java, LinkedHashSet::class.java) { NonEmptyMSet(it.toMutableSet()) }

        val _ = registerMap(NonEmptyMap::class.java) { NonEmptyMap(it.toMap()) }
        val _ = registerMap(NonEmptyMMap::class.java) { NonEmptyMMap(it.toMutableMap()) }
    }

    private fun <T : Any> register(
        target: Class<out T>,
        buffer: Class<out Collection<*>>,
        wrap: (Collection<Any?>) -> Any,
    ) = addDeserializer(
        target as Class<T>,
        NonEmptyCollectionDeserializer(target, buffer, { wrap(it) as T }) as ValueDeserializer<T>
    )

    private fun <T : Any> registerMap(
        target: Class<out T>,
        wrap: (Map<Any?, Any?>) -> Any,
    ) = addDeserializer(
        target as Class<T>,
        NonEmptyMapDeserializer(target,  { wrap(it) as T }) as ValueDeserializer<T>
    )
}

@Suppress("UNCHECKED_CAST")
class NonEmptyCollectionsOldModule : com.fasterxml.jackson.databind.module.SimpleModule("kutils-non-empty-collections") {

    init {
        val _ = register(NonEmptyList::class.java, ArrayList::class.java) { NonEmptyList(it.toList()) }
        val _ = register(NonEmptyMList::class.java, ArrayList::class.java) { NonEmptyMList(it.toMutableList()) }
        val _ = register(NonEmptySet::class.java, LinkedHashSet::class.java) { NonEmptySet(it.toSet()) }
        val _ = register(NonEmptyMSet::class.java, LinkedHashSet::class.java) { NonEmptyMSet(it.toMutableSet()) }

        val _ = registerMap(NonEmptyMap::class.java) { NonEmptyMap(it.toMap()) }
        val _ = registerMap(NonEmptyMMap::class.java) { NonEmptyMMap(it.toMutableMap()) }
    }

    private fun <T : Any> register(
        target: Class<out T>,
        buffer: Class<out Collection<*>>,
        wrap: (Collection<Any?>) -> Any,
    ) = addDeserializer(
        target as Class<T>,
        NonEmptyCollectionOldDeserializer(target, buffer, { wrap(it) as T }) as JsonDeserializer<T>
    )

    private fun <T : Any> registerMap(
        target: Class<out T>,
        wrap: (Map<Any?, Any?>) -> Any,
    ) = addDeserializer(
        target as Class<T>,
        NonEmptyMapOldDeserializer(target, { wrap(it) as T }) as JsonDeserializer<T>
    )
}