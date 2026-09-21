/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("IterableKt")
@file:Suppress("unused", "kutils_collection_declaration", "kutils_map_declaration", "RedundantSuppression",
    "kutils_null_check", "kutils_take_as_int_invoke", "kutils_drop_as_int_invoke", "kutils_empty_check", "deprecation"
)
@file:Since("1.0.0")
@file:OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
@file:MustUseReturnValues

package dev.tommasop1804.kutils

import Break
import Continue
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.constants.*
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.classes.numbers.*
import dev.tommasop1804.kutils.errors.IterableError.*
import dev.tommasop1804.kutils.exceptions.*
import java.util.stream.Collector
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.collections.map as kMap
import kotlin.collections.sortBy as kSortBy
import kotlin.collections.sortByDescending as kSortByDescending
import kotlin.collections.sortedBy as kSortedBy

/**
 * Extension property that checks if the collection is empty.
 *
 * @return `true` if the collection contains no elements, otherwise `false`.
 * @since 5.0.0
 */
val <E> Collection<E>.isEmpty get() = isEmpty()
/**
 * Extension property for `Collection` that checks if the collection is not empty.
 *
 * @return `true` if the collection contains at least one element, `false` otherwise.
 * @since 5.0.0
 */
val <E> Collection<E>.isNotEmpty get() = isNotEmpty()
/**
 * Extension property for collections that checks if the collection is either null or empty.
 *
 * @receiver A nullable collection of type E.
 * @return `true` if the collection is null or contains no elements, otherwise `false`.
 * @since 5.0.0
 */
val <E> Collection<E>?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return isNullOrEmpty()
}
/**
 * Extension property to check whether a nullable collection is not null and not empty.
 *
 * This property is a shorthand for simultaneously verifying that a collection is neither null nor empty.
 *
 * @return `true` if the collection is not null and contains at least one element, `false` otherwise.
 * @since 5.0.0
 */
val <E> Collection<E>?.isNotNullOrEmpty: Boolean get() {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return !isNullOrEmpty()
}

/**
 * Extension property for `Iterable<E>` that checks if the elements in the collection
 * are sorted in ascending order according to their natural ordering.
 *
 * This property evaluates lazily by iterating through the collection and comparing
 * each element with the next one. If all elements satisfy `x <= y` for consecutive
 * elements `x` and `y`, it returns `true`. If any pair of consecutive elements violates
 * this condition, it returns `false`.
 *
 * Note that an empty collection or a collection with a single element is considered sorted.
 *
 * @return `true` if the elements are sorted in ascending order, otherwise `false`.
 * @since 5.0.0
 */
val <E : Comparable<E>> Iterable<E>.isSorted get() = isSorted()
/**
 * Extension property for `Iterable` that determines if the collection is not sorted.
 *
 * Checks whether the elements of the iterables are not in natural order.
 *
 * @receiver Iterable of elements implementing `Comparable`.
 * @return `true` if the elements are not sorted, otherwise `false`.
 * @since 5.0.0
 */
val <E : Comparable<E>> Iterable<E>.isNotSorted get() = !isSorted()
/**
 * Extension property for [Iterable] that checks if the elements are sorted in descending order.
 *
 * This property evaluates whether the elements in the iterables are arranged in strictly
 * non-increasing order based on their natural ordering, as defined by the [Comparable] interface.
 *
 * @receiver Iterable of elements implementing [Comparable].
 * @return `true` if the elements are sorted in descending order or if the iterables is empty; `false` otherwise.
 * @since 5.0.0
 */
val <E : Comparable<E>> Iterable<E>.isSortedDescending get() = isSortedDescending()
/**
 * Extension property for `Iterable<E>` that checks whether the collection is not sorted in descending order.
 *
 * This property returns `true` if the elements of the iterables are not in strictly descending order
 * based on their natural ordering as defined by `Comparable`. If the elements are sorted in
 * descending order, the property will return `false`.
 * @since 5.0.0
 */
val <E : Comparable<E>> Iterable<E>.isNotSortedDescending get() = !isSortedDescending()

/**
 * Generates a map that represents the cardinality (frequency) of elements in the given iterables.
 * Each key in the resultant map corresponds to a unique element from the iterables,
 * and its value represents the number of times that element appears in the iterables.
 *
 * If the iterables is null, an empty map is returned.
 *
 * @receiver the iterables of elements to be processed; nullable.
 * @return a map where keys are the elements from the iterables and values are their respective frequencies.
 * @since 1.0.0
 */
val <E> Iterable<E>?.cardinalityMap: CountMap<E>
    get() {
        val count = mutableMapOf<E, Int>()
        for (element in this ?: emptyList()) {
            count[element] = count.getOrDefault(element, 0) + 1
        }
        return count
    }

/**
 * Extension property for iterables collections that evaluates whether all elements
 * in the collection are equal to each other.
 *
 * @receiver The iterables collection whose elements are to be compared.
 * @return `true` if all elements in the collection are equal or if the collection
 * is empty. Returns `false` if at least one pair of elements in the collection
 * are not equal.
 * @since 4.8.0
 */
val <E> Iterable<E>.allEqual: Boolean get() {
    val iterator = iterator()
    iterator.hasNext().ifFalse { return true }
    iterator.next().let {
        while (iterator.hasNext())
            if (it != iterator.next()) return false
    }
    return true
}

/**
 * Extension property that checks whether all elements in an [Iterable] are distinct.
 *
 * This property returns `true` if all elements in the [Iterable] are unique.
 * If the [Iterable] is empty or has only one element, the result is always `true`.
 * @since 4.8.0
 */
val <E> Iterable<E>.allDistinct: Boolean get() {
    val iterator = iterator()
    iterator.hasNext().ifFalse { return true }
    val first = iterator.next()
    iterator.hasNext().ifFalse { return true }
    val seen = HashSet<E>()
    seen.add(first)
    do seen.add(iterator.next()).ifFalse { return false } while (iterator.hasNext())
    return true
}

/**
 * Checks if the iterables contains duplicate elements.
 *
 * This function returns `true` if any element in the iterables appears more than once,
 * otherwise, it returns `false`.
 *
 * @return `true` if the iterables contains duplicate elements, `false` otherwise.
 * @since 1.0.0
 */
val <E> Iterable<E>.containsDuplicates
    get() = cardinalityMap.any { it.value > 1 }

/**
 * Checks if the collection contains exactly one element.
 *
 * @return true if the collection contains exactly one element, false otherwise.
 * @since 1.0.0
 */
val Collection<*>.isSingleElement get() = size == 1
/**
 * Checks if the collection does not contain exactly one element.
 *
 * This function verifies whether the size of the collection is not equal to one.
 *
 * @return `true` if the collection contains zero elements or more than one element, otherwise `false`.
 * @since 1.0.0
 */
val Collection<*>.isNotSingleElement get() = size != 1

/**
 * Returns `null` if the collection is empty, otherwise returns the collection itself.
 *
 * This method is a convenient way to handle empty collections by converting them to `null`,
 * which can be useful in cases where `null` signifies an absence of elements or is easier
 * to handle in the application's logic.
 *
 * @receiver The collection to be checked.
 * @return The original collection if it is not empty, or `null` if it is empty.
 * @since 5.4.0
 */
fun <C : Collection<E>, E> C.orNullIfEmpty() = ifEmpty { null }

/**
 * Merges the current collection with one or more additional collections.
 * If the current collection is null or empty, the resulting collection will contain elements from the provided collections.
 * If all collections are empty or none are provided, the result will be an empty collection of the same type as the current collection.
 *
 * @receiver the collection of elements to be processed; nullable.
 * @param collections Additional collections to merge with the current collection.
 * @return A new collection of the same type containing merged elements, or null if the current collection and all additional collections are null or empty.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <T : Collection<E>, E> T?.merge(vararg collections: Collection<E>): T {
    val isList = this is List<*>
    if (isNullOrEmpty()) {
        if (collections.isEmpty()) return (if (isList) emptyList() else emptySet<E>()) as T
        return collections.reduce { acc, collection -> acc.plus(collection) } as T
    }
    if (collections.isEmpty()) return this
    val mutable = if (isList) toMutableList() else toMutableSet()
    collections.forEach { mutable.addAll(it) }
    return (if (isList) mutable.toList() else mutable.toSet()) as T
}

/**
 * Adds the specified element to the list if it is not already present.
 * If the element already exists in the list, the method does nothing.
 *
 * @param element The element to be added to the list if absent.
 * @since 1.0.0
 */
fun <E> MutableList<E>.addIfAbsent(element: E) {
    if (!contains(element)) add(element)
}
/**
 * Adds the specified element to the list if it is not already present.
 * If the element already exists in the list, the method does nothing.
 *
 * @param element The element to be added to the list if absent.
 * @since 1.0.0
 */
@JvmName("addIfAbsentNullable")
fun <E> MutableList<E?>.addIfAbsent(element: E?) {
    if (element == null) {
        for (e in this)
            if (e == null) return
        add(null)
    }
    if (!contains(element)) add(element)
}

/**
 * Checks if there is any intersection between the current iterables and another iterables.
 *
 * @param other The iterables to check for intersection with the current iterables.
 * @return True if there is at least one common element between the two iterables, false otherwise.
 * @since 1.1.0
 */
infix fun <E> Iterable<E>.intersects(other: Iterable<E>) = (this intersect other.toSet()).isNotEmpty()

/**
 * Inserts the specified separator element between each element of the list.
 *
 * Creates a new list where the separator is placed between all the elements
 * of the original list. The separator is not added at the beginning or end
 * of the list.
 *
 * @param separator The element to intersperse between the elements of the list.
 * @return A new list containing the elements of the original list with the separator
 *         inserted between each element.
 * @since 1.0.0
 */
infix fun <E> List<E>.intersperseWith(separator: E): List<E> =
    flatMapIndexed { index, item ->
        if (index == lastIndex) listOf(item)
        else listOf(item, separator)
    }

/**
 * Checks if the iterables contains any of the specified elements.
 *
 * This function returns `true` if at least one element from the `iterables` array
 * is present in the iterables.
 *
 * @receiver the iterables of elements to be processed; nullable.
 * @param elements The elements to check for in the iterables.
 * @return `true` if the iterables contains any of the specified elements, `false` otherwise.
 * @since 1.0.0
 */
fun <E> Iterable<E>.containsAny(vararg elements: E) = any { it in elements }

/**
 * Checks if the specified iterables does not contain any of the given elements.
 * Returns `true` if none of its elements are present
 * in the provided elements. Otherwise, returns `false`.
 *
 * @receiver the iterables of elements to be processed; nullable.
 * @param elements The elements to check against the iterables.
 * @return `true` if the iterables does not contain any of the specified elements, `false` otherwise.
 * @since 1.0.0
 */
fun <E> Iterable<E>.containsNone(vararg elements: E) = none { it in elements }

/**
 * Checks if any element in the iterables satisfies the given predicate.
 *
 * @param predicate A condition that each element will be tested against.
 * @return `true` if at least one element satisfies the predicate, otherwise `false`.
 * @since 1.1.0
 */
operator fun <E> Iterable<E>.contains(predicate: Predicate<E>) = any { predicate(it) }

/**
 * Returns the first element of the iterables if it exists, otherwise returns the value produced by the provided [default] function.
 *
 * @receiver the iterables to retrieve the first element from
 * @param default a lambda function that produces a default value if the iterables is empty
 * @return the first element of the iterables or the value produced by the [default] function
 * @since 1.0.0
 */
fun <E> Iterable<E>.firstOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { first() } catch (e: NoSuchElementException) { default() }
}
/**
 * Returns the first element of the iterables if it exists, or throws an exception created
 * by the provided lambda function if the iterables is null or empty.
 *
 * @receiver the iterables on which the operation is applied
 * @param lazyException a lambda function that provides the exception to be thrown
 * if the iterables is null or empty
 * @return the first element of the iterables
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <E> Iterable<E>.firstOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { first() } catch (e: NoSuchElementException) { throw lazyException() }
}
/**
 * Extension function for `Iterable` that returns the first element of the collection
 * wrapped in an `Either` context, or raises a `NoSuchElement` error if the collection is empty.
 *
 * The function utilizes the `either` construct to handle errors functionally. It calls the
 * `catching` function to attempt fetching the first element of the iterables. If a
 * `NoSuchElementException` is thrown due to the collection being empty,
 * it transforms the exception into a `NoSuchElement` error with an `index` of 0.
 *
 * @receiver An instance of `Iterable<E>`.
 * @param E The type of elements contained in the iterables.
 * @return An `Either` value:
 *         - A `Right` containing the first element of the iterables if it is non-empty.
 *         - A `Left` containing a `Empty` error if the iterables is empty.
 * @since 6.1.0
 */
fun <E> Iterable<E>.firstOrError() = either {
    catching({ first() }) { _: NoSuchElementException -> Empty }
}
/**
 * Returns the first element of the iterables that matches the given predicate.
 *
 * The function evaluates all elements in the iterables to find the first one
 * that satisfies the provided predicate condition. If the iterables is empty,
 * a `NoSuchElementException` is thrown. If no element matches the predicate,
 * a `NoResultsException` is thrown.
 *
 * @param E the type of elements in the iterables.
 * @param predicate a function that takes an element of type `E` and returns a boolean
 * indicating whether the condition is satisfied.
 * @return the first element in the iterables that matches the predicate.
 * @throws NoSuchElementException if the iterables is empty.
 * @throws NoResultsException if no element satisfies the predicate condition.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Iterable<E>.findFirst(predicate: Predicate<E>): E {
    val list = toList()
    if (list.isEmpty()) throw NoSuchElementException()
    val filtered = list.filter(predicate)
    if (filtered.isEmpty()) throw NoResultsException()
    return filtered.first()
}
/**
 * Returns the first element matching the given [predicate], or the result of the [default] function
 * if no such element is found.
 *
 * @receiver the iterables to search for the element.
 * @param default a function that provides a fallback value if no element matches the [predicate].
 * @param predicate a function that determines whether an element matches the given condition.
 * @return the first matching element if found, otherwise the result of the [default] function.
 * @since 1.0.0
 */
fun <E> Iterable<E>.findFirstOr(default: Supplier<E>, predicate: Predicate<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { first(predicate) } catch (e: NoSuchElementException) { default() }
}
/**
 * Returns the first element in the iterables that matches the given [predicate].
 * If no such element is found, throws an exception provided by [lazyException].
 *
 * @receiver the iterables to search for the element
 * @param lazyException a lambda providing the exception to be thrown if no element matches the predicate
 * @param predicate a function that defines the condition to match the element
 * @return the first element that matches the predicate
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Iterable<E>.findFirstOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { first(predicate) } catch (e: NoSuchElementException) { throw lazyException() }
}
/**
 * Returns the first element in the iterables that matches the given [predicate], or raises a [NoSuchElement]
 * error if no such element is found.
 *
 * The method leverages the `either` context to safely handle the absence of matching elements by raising
 * a domain-specific error rather than throwing an exception.
 *
 * @param E The type of elements in the iterables.
 * @param predicate A function that evaluates whether an element matches a specific condition.
 *                  It returns `true` if the element satisfies the condition, otherwise `false`.
 * @return An `Either` containing:
 *         - The first element matching the [predicate], wrapped in `Right`, if found.
 *         - A `NoResult` error, wrapped in `Left`, if no matching element is found.
 * @since 6.1.0
 */
fun <E> Iterable<E>.findFirstOrError(predicate: Predicate<E>): Either<NotFirstResultError, E> = either {
    catching({ findFirst(predicate) }) { e: Exception -> when (e) {
        is NoResultsException -> NoResults
        is NoSuchElementException -> Empty
        else -> throw IllegalStateException()
    } }
}

/**
 * Returns the last element of the iterables or the result of invoking the specified default supplier
 * if the iterables is empty.
 *
 * @param default A supplier function that is invoked to provide a default value if the iterables is empty.
 * @return The last element of the iterables or the result of the default supplier if the iterables is empty.
 * @since 5.3.0
 */
fun <E> Iterable<E>.lastOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { last() } catch (_: NoSuchElementException) { default() }
}
/**
 * Returns the last element of the iterables if it exists, or throws an exception provided by the given supplier.
 *
 * @param lazyException A supplier that provides the exception to be thrown if the iterables is empty.
 * @return The last element of the iterables.
 * @throws Throwable If the iterables is empty, the exception provided by the supplier is thrown.
 * @since 5.3.0
 */
@IgnorableReturnValue
fun <E> Iterable<E>.lastOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { last() } catch (_: NoSuchElementException) { throw lazyException() }
}
/**
 * Returns the last element of the iterables or raises a `NoSuchElement` error if the iterables is empty.
 *
 * The method operates within an `either` context and uses `catching` to handle the
 * `NoSuchElementException` that is thrown when attempting to retrieve the last element of an empty collection.
 * If the exception occurs, it is mapped to a `NoSuchElement` error with an index of 0.
 *
 * @receiver The iterables from which the last element is retrieved.
 * @return An `Either` instance where:
 *         - `Right` contains the last element of the iterables if it exists.
 *         - `Left` contains a `Empty` error if the iterables is empty.
 * @param E The type of elements in the iterables.
 * @since 6.1.0
 */
fun <E> Iterable<E>.lastOrError() = either {
    catching({ last() }) { _: NoSuchElementException -> Empty }
}
/**
 * Returns the last element in the iterables that matches the specified predicate.
 *
 * If the iterables is empty, a [NoSuchElementException] is thrown. If no elements match the predicate,
 * a [NoResultsException] is thrown.
 *
 * @param predicate The condition to evaluate each element against.
 * @return The last element that satisfies the predicate.
 * @throws NoSuchElementException If the iterables is empty.
 * @throws NoResultsException If no elements match the predicate.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Iterable<E>.findLast(predicate: Predicate<E>): E {
    val list = toList()
    if (list.isEmpty()) throw NoSuchElementException()
    val filtered = list.filter(predicate)
    if (filtered.isEmpty()) throw NoResultsException()
    return filtered.last()
}
/**
 * Returns the last element in the collection that matches the specified [predicate].
 * If no such element is found, returns the result of the [default] supplier.
 *
 * @param default A supplier function that provides a default value if no element matches the [predicate].
 * @param predicate A predicate function to test elements of the collection.
 * @return The last element matching the [predicate], or the result of the [default] supplier if none match.
 * @since 6.1.0
 */
fun <E> Iterable<E>.findLastOr(default: Supplier<E>, predicate: Predicate<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { last(predicate) } catch (_: NoSuchElementException) { default() }

}
/**
 * Returns the last element matching the given [predicate] from the iterables, or throws an exception
 * provided by the [lazyException] supplier if no such element is found.
 *
 * @param lazyException A supplier function that provides the exception to be thrown if no matching element is found.
 * @param predicate A condition to determine which element to find as the last match.
 * @return The last element that matches the given predicate.
 * @throws Throwable The exception provided by [lazyException] if no matching element is found.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Iterable<E>.findLastOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { last(predicate) } catch (_: NoSuchElementException) { throw lazyException() }
}
/**
 * Retrieves the last element in the iterables that matches the given [predicate].
 * If no such element is found, raises a [NotLastResultsError] error.
 *
 * @param E The type of the elements in the iterables.
 * @param predicate A predicate function to test elements.
 * @return Either the last matching element, or a [NotLastResultsError] error if no element matches.
 * @since 6.1.0
 */
fun <E> Iterable<E>.findLastOrError(predicate: Predicate<E>): Either<NotLastResultsError, E> = either {
    catching({ findLast(predicate) }) { e: Exception -> when (e) {
        is NoResultsException -> NoResults
        is NoSuchElementException -> Empty
        else -> throw IllegalStateException()
    } }
}

/**
 * Returns the second element of the list.
 *
 * @receiver The list from which the second element is to be accessed.
 * @return The second element of the list.
 * @throws NoSuchElementException If the list contains fewer than two elements.
 * @since 2.1.0
 */
fun <E> List<E>.second() = if (size < 2) throw NoSuchElementException("List size $size doesn't allow to get second element.") else this[1]
/**
 * Returns the second element of the list or `null` if the list contains
 * fewer than two elements.
 *
 * This function is a safe way to access the second element of a list
 * without causing an `IndexOutOfBoundsException`. If the list is empty
 * or contains only one element, it will return `null`.
 *
 * @receiver The list from which the second element is to be accessed.
 * @return The second element of the list, or `null` if the list contains
 * fewer than two elements.
 * @since 2.1.0
 */
fun <E> List<E>.secondOrNull() = if (size < 2) null else this[1]
/**
 * Returns the second element of the list if it exists; otherwise, returns the value supplied by the given default supplier.
 *
 * @param default A supplier function that provides a default value when the list does not contain at least two elements.
 * @return The second element of the list, or the result of invoking the default supplier if the list has less than two elements.
 * @since 2.1.0
 */
fun <E> List<E>.secondOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return if (size < 2) default() else this[1]
}
/**
 * Returns the second element of the list if it exists, or throws the exception provided by the
 * given `lazyException` supplier if the list contains fewer than two elements.
 *
 * @param lazyException A supplier function that produces the exception to be thrown if the list has fewer than two elements.
 * @since 2.1.0
 */
@IgnorableReturnValue
fun <E> List<E>.secondOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    if (size < 2) throw lazyException()
    return this[1]
}
/**
 * Returns the second element of the iterables as an `Either` value. If the iterables does not contain
 * at least two elements, raises a [NoSuchElement] error wrapped in a `Left`.
 *
 * This function uses `either` to handle errors in a functional style, capturing any exception raised
 * when attempting to retrieve the second element of the iterables, specifically a [NoSuchElementException].
 *
 * @receiver The iterables collection to retrieve the second element from.
 * @param E The type of elements in the iterables.
 * @return An `Either` where:
 *         - `Right<E>` contains the second element of the iterables if it exists.
 *         - `Left<NotInnerElementErrors>` represents an error if the second element cannot be retrieved.
 * @since 6.1.0
 */
fun <E> List<E>.secondOrError(): Either<NotInnerElementError, E> = either {
    catching({ second() }) { _: NoSuchElementException -> if (isEmpty()) Empty else NoSuchElement(1) }
}
/**
 * Returns the second element of a list that matches the given predicate.
 *
 * Filters the list based on the provided predicate and retrieves the second element
 * from the filtered list. If there are fewer than two elements matching the predicate,
 * this function will throw an exception or result in a runtime error depending on
 * underlying implementations.
 *
 * @param predicate a condition to filter the elements in the list.
 * @return the second element that matches the predicate.
 * @throws NoSuchElementException if list is empty.
 * @throws NoResultsException if there are no elements matching the predicate.
 * @throws TooFewResultsException if there are fewer than two elements matching the predicate.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> List<E>.findSecond(predicate: Predicate<E>): E {
    val list = toList()
    if (list.isEmpty()) throw NoSuchElementException()
    val filtered = list.filter(predicate)
    if (filtered.isEmpty()) throw NoResultsException()
    if (filtered.size < 2) throw TooFewResultsException(filtered.size)
    return filtered.second()
}
/**
 * Returns the second element in the list that matches the specified [predicate],
 * or `null` if no such element is found or if there are fewer than two matching elements.
 *
 * @param predicate a function that defines the condition to filter the elements of the list.
 * @return the second element satisfying the given predicate, or `null` if no such element exists.
 * @since 6.1.0
 */
fun <E> List<E>.findSecondOrNull(predicate: Predicate<E>) = filter(predicate).secondOrNull()
/**
 * Returns the second element in the list that matches the given predicate, or the value provided by
 * the default supplier if no such element exists or there are less than two elements.
 *
 * @param default A supplier function that provides a default value if the list does not contain
 *                a valid second element matching the predicate.
 * @param predicate A function that determines whether a given element in the list matches the criteria.
 * @since 6.1.0
 */
fun <E> List<E>.findSecondOr(default: Supplier<E>, predicate: Predicate<E>) = filter(predicate).secondOr(default)
/**
 * Returns the second element in the list that matches the given predicate or throws an exception
 * provided by the given `lazyException` supplier if no such element exists.
 *
 * @param lazyException a supplier for the exception to throw if there are not enough matching elements
 * @param predicate a condition to filter elements of the list
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> List<E>.findSecondOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>) = filter(predicate).secondOrThrow(lazyException)
/**
 * Returns the second element of the iterables matching the given [predicate], wrapped in an
 * `Either` context.
 *
 * This method attempts to find the second element in the iterables that satisfies the specified
 * [predicate]. If no such element is found, or if the iterables does not have at least
 * two elements that match, a `NoSuchElement` error is raised with the index `1` (indicating the
 * failure to find the second element).
 *
 * @param E The type of the elements in the iterables.
 * @param predicate A function that evaluates each element of the iterables to determine
 *                  whether it satisfies the condition.
 * @return An `Either` value where:
 *         - `Right<E>` contains the second element matching the predicate.
 *         - `Left<NoSuchResult>` contains the error information if the second matching element
 *           does not exist.
 * @since 6.1.0
 */
fun <E> List<E>.findSecondOrError(predicate: Predicate<E>): Either<NotInnerResultError, E> = either {
    catching({ findSecond(predicate) }) { e: Exception -> when (e) {
        is NoResultsException -> NoResults
        is NoSuchElementException -> Empty
        is TooFewResultsException -> TooFewResults
        else -> throw IllegalStateException()
    } }
}

/**
 * Returns the third element of the list.
 *
 * This function retrieves the element at index 2 of the list if the list
 * contains at least three elements. If the size of the list is less than
 * three, a [NoSuchElementException] is thrown.
 *
 * @throws NoSuchElementException if the list contains fewer than three elements.
 * @return the third element of the list.
 * @since 2.1.0
 */
fun <E> List<E>.third() = if (size < 3) throw NoSuchElementException("List size $size doesn't allow to get third element.") else this[2]
/**
 * Returns the third element of the list if the list contains at least three elements, or `null` otherwise.
 *
 * This function is a safe way to access the third element without risking an `IndexOutOfBoundsException`.
 *
 * @receiver The list from which the third element is accessed.
 * @return The third element of the list, or `null` if the list has fewer than three elements.
 * @since 2.1.0
 */
fun <E> List<E>.thirdOrNull() = if (size < 3) null else this[2]
/**
 * Returns the third element of the list if it exists; otherwise, evaluates and returns the
 * result of the provided default supplier.
 *
 * @param default A supplier function that provides a default value to return if the list
 * has fewer than three elements.
 * @return The third element of the list if present, or the default value provided by the
 * supplier.
 * @since 2.1.0
 */
fun <E> List<E>.thirdOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return if (size < 3) default() else this[2]
}
/**
 * Returns the third element of the list if it exists, otherwise throws an exception provided by the given supplier.
 *
 * @param lazyException A supplier that provides the exception to be thrown if the list has fewer than three elements.
 * @throws Throwable The exception provided by the supplier if the list size is less than three.
 * @return The third element of the list.
 * @since 2.1.0
 */
@IgnorableReturnValue
fun <E> List<E>.thirdOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    if (size < 3) throw lazyException()
    return this[2]
}
/**
 * Safely retrieves the third element of the list as an `Either`.
 *
 * This function attempts to retrieve the third element of the list using the `third` extension function.
 * If the list contains fewer than three elements, a `NoSuchElement` error is returned instead of throwing an exception.
 *
 * @receiver The list from which the third element is to be retrieved.
 * @param E The type of elements in the list.
 * @return An `Either` where:
 *         - `Right` contains the third element of the list if it exists.
 *         - `Left` contains a `NoSuchElement` error if the list contains fewer than three elements.
 * @since 6.1.0
 */
fun <E> List<E>.thirdOrError(): Either<NotInnerElementError, E> = either {
    catching({ third() }) { _: NoSuchElementException -> if (isEmpty()) Empty else NoSuchElement(2) }
}
/**
 * Returns the third element in the list matching the given predicate after filtering.
 *
 * The method first filters the elements of the list using the provided predicate and
 * then attempts to retrieve the third element from the filtered result. If the filtered
 * list has less than three elements, this method will result in an exception.
 *
 * @param predicate A predicate to filter the elements of the list.
 * @since 6.1.0
 */
fun <E> List<E>.findThird(predicate: Predicate<E>): E {
    val list = toList()
    if (list.isEmpty()) throw NoSuchElementException()
    val filtered = list.filter(predicate)
    if (filtered.isEmpty()) throw NoResultsException()
    if (filtered.size < 3) throw TooFewResultsException(filtered.size)
    return filtered.third()
}
/**
 * Returns the third element that matches the given [predicate], or `null` if no such element exists.
 *
 * The search for the matching element is performed by filtering the list based on the given [predicate].
 *
 * @param predicate the condition used to filter the elements of the list.
 * @return the third element matching the [predicate], or `null` if there are less than three matching elements.
 * @since 6.1.0
 */
fun <E> List<E>.findThirdOrNull(predicate: Predicate<E>) = filter(predicate).thirdOrNull()
/**
 * Returns the third element of the list that matches the given predicate if it exists; otherwise, returns the value supplied by the provided default supplier.
 * The matching elements are determined by filtering the list based on the given predicate.
 *
 * @param default A supplier function that provides a default value when the list does not contain at least three elements
 *                matching the given predicate.
 * @param predicate A predicate function used to filter the list.
 * @since 6.1.0
 */
fun <E> List<E>.findThirdOr(default: Supplier<E>, predicate: Predicate<E>) = filter(predicate).thirdOr(default)
/**
 * Returns the third element in the list that matches the given predicate or throws an exception
 * provided by the lazyException supplier if there are less than three matching elements.
 *
 * @param lazyException a supplier that provides the exception to be thrown if the conditions are not met
 * @param predicate a condition to filter elements in the list
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> List<E>.findThirdOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>) = filter(predicate).thirdOrThrow(lazyException)
/**
 * Attempts to retrieve the third element of the list that matches the given predicate.
 * If the third matching element is not found, raises a `NoSuchElement` error with the index set
 * to `2`.
 *
 * @param predicate A predicate used to filter and identify matching elements from the list.
 * @return An `Either` result where:
 *         - `Right` represents the successfully retrieved third matching element.
 *         - `Left` represents a `NoSuchResult` error if the third matching element is not found.
 * @since 6.1.0
 */
fun <E> List<E>.findThirdOrError(predicate: Predicate<E>): Either<NotInnerResultError, E> = either {
    catching({ findThird(predicate) }) { e: Exception -> when (e) {
        is NoResultsException -> NoResults
        is NoSuchElementException -> Empty
        is TooFewResultsException -> TooFewResults
        else -> throw IllegalStateException()
    } }
}

/**
 * Returns the single element in the iterables if it contains exactly one element.
 * Throws an exception otherwise:
 * - [NoSuchElementException] if the iterables is empty.
 * - [TooManyElementsException] if the iterables contains more than one element.
 *
 * @receiver An iterables collection of type [E].
 * @return The single element in the iterables.
 * @throws NoSuchElementException if the iterables is empty.
 * @throws TooManyElementsException if the iterables contains more than one element.
 * @since 1.0.0
 */
fun <E> Iterable<E>.onlyElement() = toList().run {
    if (isEmpty()) throw NoSuchElementException() 
    else if (size == 1) first() else throw TooManyElementsException(size)
}
/**
 * Returns the single element in the iterables if it contains exactly one element,
 * or `null` if the iterables is empty or contains more than one element.
 *
 * @receiver an iterables of elements
 * @return the single element in the iterables or `null` if the conditions are not met
 * @since 1.0.0
 */
fun <E> Iterable<E>.onlyElementOrNull() = toList().run { if (size == 1) first() else null }
/**
 * Returns the single element in the iterables if it contains only one element; otherwise,
 * it returns the value supplied by the provided [default] supplier.
 *
 * @param E the type of elements in the iterables
 * @param default a supplier that provides a default value if the iterables does not contain exactly one element
 * @return the single element in the iterables if there is exactly one, or the value supplied by [default]
 * @since 1.0.0
 */
infix fun <E> Iterable<E>.onlyElementOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return toList().run { if (size == 1) first() else default() }
}
/**
 * Returns the single element of the iterables if it contains exactly one element; otherwise, throws an exception provided by the given supplier.
 *
 * @param lazyException a supplier function that provides the exception to be thrown when the iterables does not contain exactly one element
 * @return the single element of the iterables if there is exactly one element
 * @throws Throwable the exception supplied by the `lazyException` if the iterables does not contain exactly one element
 * @since 1.0.0
 */
@IgnorableReturnValue
infix fun <E> Iterable<E>.onlyElementOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return toList().run { if (size == 1) first() else throw lazyException() }
}
/**
 * Returns an `Either` containing the single element of the iterables if it contains exactly one element,
 * or an error if the iterables is empty or contains more than one element.
 *
 * The method internally uses the `onlyElement` function to retrieve the single element.
 * It captures exceptions raised by `onlyElement` and transforms them into domain-specific error types:
 * - If the iterables is empty, a `NoSuchElement` error is returned.
 * - If the iterables contains multiple elements, a `TooManyElement` error is returned,
 *   with the size of the iterables included in the error.
 *
 * This method facilitates more functional error handling through the use of the `either` API,
 * allowing consumers to handle errors separately from normal results.
 *
 * @receiver An iterables collection of type [E].
 * @param E The type of elements in the iterables.
 * @return An `Either` containing:
 *         - The single element as `Right<E>` if the iterables contains only one element.
 *         - An error as `Left<NotOnlyElementErrors>` if the iterables is empty or contains more than one element.
 * @since 6.1.0
 */
fun <E> Iterable<E>.onlyElementOrError(): Either<NotOnlyElementError, E> = either {
    catching({ onlyElement() }) { e: Exception -> when (e) {
        is NoSuchElementException -> Empty
        is TooManyElementsException -> TooManyElements
        else -> throw IllegalStateException()
    } }
}

/**
 * Filters the elements of an iterables based on a predicate and ensures that exactly one element
 * satisfies the predicate. If no elements or more than one element satisfy the predicate, an exception is thrown.
 *
 * @param predicate a predicate to filter the elements of the iterables
 * @return the single element that satisfies the predicate
 * @throws NoSuchElementException if list is empty
 * @throws NoResultsException if no elements satisfy the predicate
 * @throws TooManyResultsException if more than one element satisfies the predicate
 * @since 6.1.0
 */
infix fun <E> Iterable<E>.findOnlyElement(predicate: Predicate<E>) = toList()
    .requireOrThrow({ NoSuchElementException() }, { it.isNotEmpty() })
    .filter(predicate).run {
        if (isEmpty()) throw NoResultsException()
        if (size == 1) first()
        else throw TooManyResultsException(size)
    }
/**
 * Returns the single element matching the given [predicate], or `null` if no such element exists
 * or if there is more than one matching element in the iterables.
 *
 * @param predicate A lambda function used to filter elements in the iterables. The function should
 * return `true` for elements you want to include in the operation.
 * @since 6.1.0
 */
infix fun <E> Iterable<E>.findOnlyElementOrNull(predicate: Predicate<E>) = filter(predicate).run { if (size == 1) first() else null }
/**
 * Returns the single element that matches the given predicate if exactly one element matches,
 * otherwise returns the result from the default supplier.
 *
 * @param default A supplier function that provides a default value when no element
 * or more than one element matches the predicate.
 * @param predicate A predicate to filter the elements in the iterables.
 * @since 6.1.0
 */
fun <E> Iterable<E>.findOnlyElementOr(default: Supplier<E>, predicate: Predicate<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return filter(predicate).run { if (size == 1) first() else default() }
}
/**
 * Returns the single element matching the given [predicate] from the iterables or throws an exception
 * provided by [lazyException] if the condition is not met. The method ensures that exactly one element
 * matches the predicate.
 *
 * @param lazyException a supplier for the exception to be thrown if the number of matching elements
 * is not exactly one.
 * @param predicate a condition to be checked for each element in the iterables.
 * @return the single element that matches the [predicate].
 * @throws Throwable the exception supplied by [lazyException] if no element or more than one element matches.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Iterable<E>.findOnlyElementOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return filter(predicate).run { if (size == 1) first() else throw lazyException() }
}
/**
 * Retrieves the only element from the iterables that satisfies the specified predicate, or returns an error
 * encapsulated in an `Either` type if there are zero or multiple elements that match the predicate.
 *
 * @param predicate The predicate function used to filter the elements of the iterables.
 * @return An `Either` instance:
 *         - `Either.Right` wrapping the element that satisfies the predicate if there is exactly one match.
 *         - `Either.Left` wrapping a `NotOnlyElementErrors` instance if there are zero or multiple matches.
 * @since 6.1.0
 */
fun <E> Iterable<E>.findOnlyElementOrError(predicate: Predicate<E>): Either<NotOnlyResultError, E> = either {
    catching({ findOnlyElement(predicate) }) { e: Exception ->
        when (e) {
            is NoResultsException -> NoResults
            is NoSuchElementException -> Empty
            is TooManyResultsException -> TooManyResults
            else -> throw IllegalStateException()
        }
    }
}

/**
 * Splits the elements of this list into chunks where the consecutive elements in each chunk satisfy the given predicate.
 * A new chunk is started when the predicate is not satisfied.
 *
 * @param predicate a function that takes an element and returns `true` to keep it in the current chunk or `false` to start a new chunk
 * @return a list of chunks, where each chunk is a list of consecutive elements satisfying the predicate
 * @since 1.0.0
 */
infix fun <E> Iterable<E>.chunkedWhile(predicate: Predicate<E>): List<List<E>> = toList().run {
    if (isEmpty()) return@run emptyList()
    val result = mutableListOf<MutableList<E>>()
    var current = mutableListOf<E>()
    for (i in indices) {
        current.add(this[i])
        if (predicate(this[i])) {
            result.add(current)
            current = mutableListOf()
        }
    }
    result.add(current)
    if (result.last().isEmpty()) result.dropLast(1) else result
}

/**
 * Creates a new list in which each element of the original list is repeated the specified number of times.
 *
 * @receiver The original list of elements to be repeated.
 * @param n The number of times each element in the list should be repeated.
 * @since 1.0.0
 */
infix fun <E> Iterable<E>.repeatEach(n: Int): List<E> {
    val resultList = mutableListOf<E>()
    forEach {
        { resultList += it } * n
    }

    return resultList
}

/**
 * Finds the mode (the most frequently occurring element) in the given iterables.
 *
 * If there are multiple elements with the same frequency, the result will be the first one
 * encountered in the iteration. If the iterables is empty, null is returned.
 *
 * @return The most frequent element in the iterables or null if the iterables is empty.
 * @since 1.0.0
 */
fun <E> Iterable<E>.mode(): E? = groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

/**
 * Returns the index of the given element in the iterables or throws an exception if the element is not found.
 *
 * @param element The element whose index is to be determined.
 * @param lazyException A supplier that provides the exception to be thrown if the element is not found.
 * @throws Throwable The exception provided by [lazyException] if the element is not found in the iterables.
 * @return The index of the specified element in the iterables if it exists.
 * @since 6.1.0
 */
fun <E> Iterable<E>.indexOfOrThrow(element: E, lazyException: ThrowableSupplier) =
    indexOf(element).expectNot(INDEX_NOT_FOUND, causeOf = { lazyException() })
/**
 * Finds the last index of the specified element in the iterables, or throws an exception if the element is not found.
 *
 * @param element The element whose last index is to be located in the iterables.
 * @param lazyException A supplier function that provides a throwable to be thrown if the element is not found.
 * This supplier function is invoked only if the element is not found.
 * @throws Throwable The exception returned by [lazyException] if the element is not found.
 * @since 6.1.0
 */
fun <E> Iterable<E>.lastIndexOfOrThrow(element: E, lazyException: ThrowableSupplier) =
    lastIndexOf(element).expectNot(INDEX_NOT_FOUND, causeOf = { lazyException() })
/**
 * Returns the index of the first element in the iterables that matches the given [predicate].
 * If no such element is found, an exception provided by [lazyException] is thrown.
 *
 * @param E The type of elements in the iterables.
 * @param lazyException A supplier function that provides the exception to be thrown when no matching element is found.
 * @param predicate A predicate function to test elements of the iterables.
 * @return The index of the first element that matches the [predicate], or throws the exception provided by [lazyException] if none is found.
 * @throws Throwable The exception generated by [lazyException] if no element matches the [predicate].
 * @since 6.1.0
 */
fun <E> Iterable<E>.indexOfFirstOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>) =
    indexOfFirst(predicate).expectNot(INDEX_NOT_FOUND, causeOf = { lazyException() })
/**
 * Finds the index of the last element in the iterables that matches the given predicate.
 * If no such element is found, the method throws an exception provided by the given throwable supplier.
 *
 * @param lazyException A supplier function that provides the exception to be thrown if no matching element is found.
 * @param predicate A predicate function used to determine whether an element satisfies the condition.
 * @return The index of the last element that matches the predicate.
 * @throws Throwable The exception provided by the lazyException supplier if no matching element is found.
 * @since 6.1.0
 */
fun <E> Iterable<E>.indexOfLastOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>) =
    indexOfLast(predicate).expectNot(INDEX_NOT_FOUND, causeOf = { lazyException() })
/**
 * Finds the index of a given element in the iterables or raises a `NotFound` error if the element is not found.
 *
 * This function uses the `either` block to handle the result:
 * - Returns the index if the element is found.
 * - Raises an error of type `NotFound` if the element is not present in the iterables.
 *
 * @param E The type of the elements in the iterables.
 * @param element The element to be searched for in the iterables.
 * @return The index of the element if found.
 * @since 6.1.0
 */
fun <E> Iterable<E>.indexOfOrError(element: E) = either {
    val index = indexOf(element)
    ensure(index != INDEX_NOT_FOUND) { NotFound(element) }
    index
}
/**
 * Returns the last index of the specified element in the iterables or raises an error if the element
 * is not found. This function short-circuits errors using the `either` context.
 *
 * @param E The type of elements in the iterables.
 * @param element The element whose last occurrence index is to be found.
 * @return The last index of the specified element.
 * @since 6.1.0
 */
fun <E> Iterable<E>.lastIndexOfOrError(element: E) = either {
    val index = lastIndexOf(element)
    ensure(index != INDEX_NOT_FOUND) { NotFound(element) }
    index
}
/**
 * Returns the index of the first element matching the given [predicate] in the iterables.
 * If no element matches, raises an error with the specified [NoResults] error type.
 *
 * This function uses the `either` construct to handle the result or raise an error in a functional style.
 * It ensures that the computation short-circuits when no match is found.
 *
 * @param E The type of elements in the iterables.
 * @param predicate A functional interface `Predicate<E>` that is used to test the elements.
 *                  The predicate returns `true` for the desired match and `false` otherwise.
 * @return An `Either` containing the index of the matching element as `Right`, or an error as `Left`.
 *         Specifically, raises the [NoResults] error if no element satisfies the predicate.
 * @since 6.1.0
 */
fun <E> Iterable<E>.indexOfFirstOrError(predicate: Predicate<E>) = either {
    val index = indexOfFirst(predicate)
    ensure(index != INDEX_NOT_FOUND) { NoResults }
    index
}
/**
 * Finds the index of the last element in the iterables that matches the given predicate.
 * If no element matches the predicate, raises an error encapsulated in an `Either`.
 *
 * @param E The type of elements in the iterables.
 * @param predicate A condition to evaluate for each element. The function will return the index
 *                  of the last element for which this predicate evaluates to `true`.
 * @return The index of the last matching element if found; otherwise, raises an error.
 * @since 6.1.0
 */
fun <E> Iterable<E>.indexOfLastOrError(predicate: Predicate<E>) = either {
    val index = indexOfLast(predicate)
    ensure(index != INDEX_NOT_FOUND) { NoResults }
    index
}

/**
 * Returns the original collection if it is not null and not empty; otherwise,
 * returns the result of `defaultValue` function.
 *
 * This function ensures lazy evaluation of the `defaultValue` function by
 * invoking it only when the collection is null or empty.
 *
 * @param defaultValue A lambda function that provides the default collection to return
 * if the original collection is null or empty.
 * @return The original collection if it is not null and not empty; otherwise, the
 * collection returned by the `defaultValue` function.
 * @since 1.0.0
 */
inline fun <C : Collection<E>?, E> C.ifNullOrEmpty(defaultValue: Supplier<C>): C {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
    }
    return if (isNullOrEmpty()) defaultValue() else this
}
/**
 * Executes the given action if the collection is not empty and returns the collection itself.
 *
 * @param action The action to be executed if the collection is not empty.
 * @return The original collection.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <C : Collection<E>, E> C.ifNotEmpty(action: Consumer<C>): C {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNotEmpty()) action(this)
    return this
}
/**
 * Executes the given block if the collection is not null and not empty.
 *
 * This function allows for safe invocation of a supplied block of code,
 * which will only be executed if the collection is both non-null and contains elements.
 *
 * @param block The block of code to execute if the collection satisfies the not-null and not-empty condition.
 * @return The original collection, regardless of whether the block was executed or not.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <C : Collection<E>, E> C?.ifNotNullOrEmpty(block: Consumer<C>): C? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        (this@ifNotNullOrEmpty != null) holdsIn block
        (this@ifNotNullOrEmpty != null) implies returnsNotNull()
    }
    if (isNotNullOrEmpty) block(this)
    return this
}

/**
 * Executes the given action on the iterables if it contains the specified element.
 *
 * @param element The element to check for in the iterables.
 * @param action The action to be performed if the element is found.
 * @return The original iterables.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <C : Iterable<E>, E> C.ifContains(element: E, action: Consumer<C>): C {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (contains(element)) action(this)
    return this
}
/**
 * Executes the given action if the collection does not contain the specified element.
 *
 * @param element The element whose absence in the collection is to be checked.
 * @param action The action to be executed if the element is not present in the collection.
 * @return The same collection on which the method was invoked.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <C : Iterable<E>, E> C.ifNotContains(element: E, action: Consumer<C>): C {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (!contains(element)) action(this)
    return this
}

/**
 * Executes the given action on the collection if its size is equal to the specified value.
 *
 * @param size The size to compare with the collection's size.
 * @param action The action to be invoked if the collection's size matches the specified value.
 * @return The original collection.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <C : Collection<E>, E> C.ifSize(size: Int, action: Consumer<C>): C {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this.size == size) action(this)
    return this
}
/**
 * Executes the provided action if the collection size is not equal to the specified size.
 *
 * @param size The size to compare against the collection's size.
 * @param action The action to execute if the collection size does not match the specified size.
 * @return The original collection.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <C : Collection<E>, E> C.ifNotSize(size: Int, action: Consumer<C>): C {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this.size != size) action(this)
    return this
}
/**
 * Executes the provided action if the collection contains exactly one element.
 *
 * @param action A consumer that will be executed if the collection has a single element.
 * @return The original collection.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <C : Collection<E>, E> C.ifSingleElement(action: Consumer<C>): C {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (size == 1) action(this)
    return this
}

/**
 * Filters elements in the iterables based on their index, keeping only those at positions
 * that are multiples of the specified step value.
 *
 * @param step The interval between indices to consider for filtering. Must be greater than 0.
 * @return A list of elements from the original iterables, selected at the specified step interval.
 * @since 1.0.0
 */
infix fun <E> Iterable<E>.step(step: Int) =  filterIndexed { index, c -> index % step == 0 }

/**
 * Negates a nullable collection by returning `true` if the collection is `null` or empty,
 * and `false` otherwise. This operator function allows for intuitive use of negation
 * on nullable collections.
 *
 * Note: A non-null and non-empty collection will return `false`, while a `null` or empty
 * collection will return `true`.
 *
 * @return `true` if the collection is `null` or empty, `false` otherwise.
 * @since 1.0.0
 */
@JvmName("nullableNot")
operator fun <E> Collection<E>?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty()
}
/**
 * Checks whether the collection is empty.
 *
 * This operator function allows using the negation operator `!`
 * as shorthand for checking if a collection contains no elements.
 *
 * @receiver The collection to check.
 * @return `true` if the collection is empty, `false` otherwise.
 * @since 5.0.0
 */
operator fun <E> Collection<E>.not() = isEmpty()

/**
 * Repeats the elements of the list a specified number of times and returns the resulting list.
 *
 * @param n The number of times the elements of the list should be repeated.
 * @return A new list containing the elements of the original list repeated n times in order.
 * @since 1.0.0
 */
operator fun <E> List<E>.times(n: Int) = (1..n).flatMap { this }
/**
 * Repeats the elements of the mutable list a specified number of times and returns a new mutable list.
 *
 * @param n the number of times to repeat the elements of the mutable list.
 * @return a new mutable list containing the elements of the original list repeated `n` times.
 * @since 1.0.0
 */
@JvmName("mutableListTimes")
operator fun <E> MutableList<E>.times(n: Int): MList<E> = (1..n).flatMap { this }.toMutableList()

/**
 * Partitions the list into two lists based on the given predicate.
 * The operator function `div` allows you to use the `/` operator for partitioning.
 *
 * The result consists of a pair of lists where:
 * - The first list contains elements for which the predicate returns true.
 * - The second list contains elements for which the predicate returns false.
 *
 * @param predicate A predicate that determines how the list will be partitioned.
 * @since 1.0.0
 */
operator fun <E> List<E>.div(predicate: Predicate<E>) = partition(predicate)
/**
 * Divides the elements of a mutable list into two separate mutable lists based on the provided predicate.
 * The original list is partitioned into two parts: one that satisfies the predicate and one that does not.
 *
 * @param predicate A function that evaluates each element in the list to determine its partition.
 *                  Elements for which the predicate returns `true` are included in the first partition,
 *                  and the rest are included in the second partition.
 * @since 1.0.0
 */
@JvmName("mutableListDiv")
operator fun <E> MutableList<E>.div(predicate: Predicate<E>) = partition(predicate).map({ it.toMList() }, { it.toMList() })
/**
 * Divides the set into two lists based on the given predicate.
 *
 * The function applies the provided predicate to each element of the set and partitions
 * it into two collections. The first collection contains elements that match the predicate
 * and the second contains elements that do not.
 *
 * @param predicate the predicate to evaluate each element of the set.
 * @return a pair of lists where the first list contains elements that match the predicate
 * and the second list contains elements that do not.
 * @since 1.0.0
 */
operator fun <E> Set<E>.div(predicate: Predicate<E>) = partition(predicate)
/**
 * Partitions the mutable set into two lists based on a provided predicate,
 * transforming each resulting part into a mutable list.
 *
 * @param predicate The predicate function that decides how to partition the elements of the set.
 * @return A pair of mutable lists, where the first list contains elements matching the predicate,
 *         and the second contains elements not matching the predicate.
 * @since 1.0.0
 */
@JvmName("mutableSetDiv")
operator fun <E> MutableSet<E>.div(predicate: Predicate<E>) = partition(predicate).map({ it.toMList() }, { it.toMList() })

/**
 * Divides the list into chunks of the given size.
 *
 * @param chunkSize the size of each chunk in the resulting list. Must be greater than 0.
 * @return a list of lists, where each inner list contains at most `chunkSize` elements.
 * @throws IllegalArgumentException if `chunkSize` is less than or equal to 0.
 * @since 1.0.0
 */
operator fun <E> List<E>.rem(chunkSize: Int): MultiList<E> = chunked(chunkSize)
/**
 * Divides the mutable list into multiple sublists, each containing up to the specified chunk size.
 * The divided sublists are returned as a list of mutable lists.
 *
 * @param chunkSize The maximum number of elements each sublist should contain.
 * @return A list of mutable lists where each sublist has at most the specified number of elements.
 * @throws IllegalArgumentException If the chunk size is not a positive integer.
 * @since 1.0.0
 */
@JvmName("mutableListDiv")
operator fun <E> MutableList<E>.rem(chunkSize: Int): MultiMList<E> = chunked(chunkSize).kMap { it.toMList() }.toMList()
/**
 * Splits the set into a list of lists, each of the specified size.
 * The last list may have fewer elements if the total number of elements in the set
 * is not evenly divisible by the specified chunk size.
 *
 * @param chunkSize the size of each chunk. Must be greater than 0.
 * @return a list of lists, each containing a chunk of the original set.
 * @throws IllegalArgumentException if the chunk size is less than or equal to 0.
 * @since 1.0.0
 */
operator fun <E> Set<E>.rem(chunkSize: Int): MultiList<E> = chunked(chunkSize)
/**
 * Divides the elements of the mutable set into chunks of the specified size, creating a list of new mutable sets.
 *
 * @param chunkSize the number of elements each resulting mutable set should contain
 * @return a list of mutable sets, each containing up to `chunkSize` elements
 * @since 1.0.0
 */
@JvmName("mutableSetDiv")
operator fun <E> MutableSet<E>.rem(chunkSize: Int): MList<MSet<E>> = chunked(chunkSize).kMap { it.toMSet() }.toMList()
/**
 * Divides the elements of this list into chunks based on the specified predicate, using the remainder operator.
 * A new chunk is started when the provided predicate is satisfied.
 *
 * @param predicate a function that takes an element and returns `true` to start a new chunk
 * @since 1.0.0
 */
operator fun <E> List<E>.rem(predicate: Predicate<E>): MultiList<E> = chunkedWhile(predicate)
/**
 * Splits the elements of this mutable list into chunks where the consecutive elements in each chunk satisfy
 * the given predicate, and maps each chunk into a mutable list.
 *
 * This operator function allows for separating a mutable list's elements into smaller mutable lists based on
 * a specified condition provided by the predicate.
 *
 * @param predicate a function that takes an element and determines whether it should be included in the current
 * chunk (`true`) or if a new chunk should be started (`false`).
 * @return a list of mutable lists (chunks) where each chunk contains consecutive elements satisfying the predicate.
 * @since 1.0.0
 */
@JvmName("mutableListDiv")
operator fun <E> MutableList<E>.rem(predicate: Predicate<E>): MultiMList<E> = chunkedWhile(predicate).kMap { it.toMList() }.toMList()
/**
 * Splits the elements of this set into chunks where the consecutive elements in each chunk satisfy the given predicate.
 * A new chunk is started when the predicate is not satisfied.
 *
 * @param predicate a function that takes an element and returns `true` to keep it in the current chunk or `false` to start a new chunk
 * @return a list of chunks, where each chunk is a list of consecutive elements satisfying the predicate
 * @since 1.0.0
 */
operator fun <E> Set<E>.rem(predicate: Predicate<E>) = chunkedWhile(predicate).kMap { it.toSet() }.toList()
/**
 * Divides the elements of this mutable set into chunks based on the provided predicate and returns a list of mutable sets,
 * where each set contains elements from the original set that satisfy the predicate sequentially.
 *
 * @param predicate a function to determine how to group elements into chunks.
 *                   The predicate evaluates each element and starts a new subset when its condition is satisfied.
 * @return a list of mutable sets, representing grouped chunks of the original set.
 * @since 1.0.0
 */
@JvmName("mutableSetDiv")
operator fun <E> MutableSet<E>.rem(predicate: Predicate<E>): MList<MSet<E>> = chunkedWhile(predicate).kMap { it.toMSet() }.toMList()

/**
 * Decrements the MutableList by returning a sublist that excludes the last element.
 *
 * This operator function removes the last element from the list
 * and returns a view of the list without modifying the original collection.
 * It operates on the range of [0, lastIndex).
 *
 * @return A sublist containing all elements of the original list except the last one.
 * @since 1.0.0
 */
operator fun <E> MutableList<E>.dec(): MList<E> = subList(0, lastIndex)

/**
 * Returns a sublist from the start of the list up to and including the specified end index.
 *
 * @param endIndex The index marking the end of the range to be included in the sublist.
 *                 Must be within the bounds of the list.
 * @since 1.0.0
 */
operator fun <E> List<E>.rangeTo(endIndex: Int) = subList(0, endIndex + 1)
/**
 * Creates a sublist from the beginning of this mutable list up to the specified end index, inclusive.
 *
 * @param endIndex the index that marks the end (inclusive) of the range to be included in the resulting sublist.
 * Must be a valid index within the list bounds.
 * @since 1.0.0
 */
@JvmName("mutableListRangeTo")
operator fun <E> MutableList<E>.rangeTo(endIndex: Int): MList<E> = subList(0, endIndex + 1).toMutableList()

/**
 * Invokes a range of elements from a list in a circular manner, allowing for custom stepping.
 *
 * @param circularStartIndex The starting index in a circular context.
 * @param circularEndIndex The ending index in a circular context.
 * @param step The step size for iteration, must be greater than zero. Default is 1.
 * @return A list of elements corresponding to the specified range and step.
 * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the step value is not greater than zero.
 * @throws NoSuchElementException If the list is empty.
 * @since 1.0.0
 */
operator fun <E> List<E>.invoke(circularStartIndex: Int, circularEndIndex: Int, step: Int = 1): List<E> {
    validate(step > 0) { "The step value must be greater than zero." }
    if (isEmpty()) throw NoSuchElementException("List cannot be empty.")

    val result = mutableListOf<E>()
    if (circularEndIndex >= circularStartIndex)
    for (i in circularStartIndex until circularEndIndex step step) {
        result.add(invoke(i))
    } else {
        for (i in circularStartIndex until (circularStartIndex + size) step step) {
            val index = i % size
            result.add(invoke(index))
            if ((i + 1) % size == circularEndIndex % size) break
        }
    }
    return result
}
/**
 * Invokes a sub-list operation on the list using the specified circular range and step value.
 * This allows selection of elements from the list within a given range, and the list is treated circularly,
 * meaning it wraps around when the range exceeds the bounds of the list.
 *
 * @param circularRange the range of indices to select from the list, treated as circular.
 * @param step the step value used to iterate through the list based on the range. Defaults to 1.
 * @since 1.0.0
 */
operator fun <E> List<E>.invoke(circularRange: IntRange, step: Int = 1) = invoke(circularRange.first, circularRange.last + 1, step)
/**
 * Invokes elements of the list based on the given circular progression and
 * returns a new list containing the selected elements.
 *
 * @param E the type of elements in the list.
 * @param circularProgression the progression of integers used to select elements in a circular manner.
 * @return a list containing elements selected based on the provided progression.
 * @throws IndexOutOfBoundsException if the list is empty.
 * @since 1.0.0
 */
operator fun <E> List<E>.invoke(circularProgression: IntProgression): List<E> {
    if (isEmpty()) throw IndexOutOfBoundsException("List cannot be null.")

    val result = mutableListOf<E>()
    for (i in circularProgression) result.add(invoke(i))
    return result
}
/**
 * Provides a utility to access elements from the list using a circular index.
 * The index is wrapped around the list size, allowing access even for out-of-bound indices.
 *
 * @param circularIndex the index to access, which can be outside the bounds of the list.
 * The index is adjusted using modulo operation.
 * @return the element at the computed valid index within the list.
 * @throws IndexOutOfBoundsException if the list is empty.
 * @since 1.0.0
 */
operator fun <E> List<E>.invoke(circularIndex: Int): E {
    if (isEmpty()) throw IndexOutOfBoundsException("List cannot be null.")
    val actualIndex = circularIndex.mod(size)
    return this[actualIndex]
}

/**
 * Filters the elements of the iterables based on the provided predicate.
 *
 * This operator function allows the use of the `invoke` function to apply a filter directly to an iterables collection.
 *
 * @param filter the predicate used to test each element in the iterables. Only elements that satisfy the predicate will be included in the resulting collection.
 * @since 1.0.0
 */
inline operator fun <E> Iterable<E>.invoke(filter: Predicate<E>): List<E> {
    val destination = ArrayList<E>()
    for (element in this) if (filter(element)) destination.add(element)
    return destination
}

/**
 * Retrieves the first element in the iterables that matches the given predicate.
 *
 * @param E The type of elements in the iterables.
 * @param find A predicate used to determine the matching element.
 * @return The first element that satisfies the predicate.
 * @since 1.0.0
 */
inline operator fun <E> Iterable<E>.get(find: Predicate<E>): E? {
    for (element in this) if (find(element)) return element
    return null
}
/**
 * Retrieves the first element in the iterables that matches the given predicate.
 *
 * @param E The type of elements in the iterables.
 * @param find A predicate used to determine the matching element.
 * @param lazyException A supplier for the exception to be thrown if no element is found.
 * @return The first element that satisfies the predicate.
 * @since 1.0.0
 */
operator fun <E> Iterable<E>.get(find: Predicate<E>, lazyException: ThrowableSupplier): E {
    for (element in this) if (find(element)) return element
    throw lazyException()
}

/**
 * Returns a new list containing elements at indices specified by the given progression.
 *
 * Extracts elements from the original list as defined by the start, end (inclusive),
 * and step values of the provided range.
 *
 * @param range the progression of indices specifying the subset of the original list.
 * @since 1.0.0
 */
@Suppress("deprecation")
operator fun <E> List<E>.get(range: IntProgression) = subList(range)
/**
 * Returns a new mutable list containing elements from the specified range of indices in the original list,
 * selected at intervals determined by the step of the range.
 *
 * The returned list includes elements starting from the `range.start` index
 * up to and including the `range.endInclusive` index, but only picks elements
 * based on the step value specified in the range.
 *
 * @param range the progression of indices to select, defined by a start, end, and step
 * @return a new mutable list containing elements corresponding to the given range and step
 * @throws IndexOutOfBoundsException if start or end indices are out of bounds of this list
 * @since 1.0.0
 */
@JvmName("mutableListGetIntProgression")
@Suppress("deprecation")
operator fun <E> MutableList<E>.get(range: IntProgression) = subList(range)
/**
 * Returns the element at the position corresponding to the given percentage of the list's size.
 *
 * This method calculates the index in the list based on the specified percentage and returns
 * the element at that index. The list should not be empty, and the percentage must be within
 * the range of 0 to 100 inclusive.
 *
 * @param percentage The percentage of the list's size (0 to 100 inclusive).
 * @return The element at the calculated index based on the percentage.
 * @throws IndexOutOfBoundsException If the list is empty.
 * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the percentage is not within the 0 to 100 range.
 * @since 4.6.1
 */
@Suppress("deprecation")
operator fun <E> List<E>.get(percentage: Percentage) = percent(percentage)

/**
 * Retrieves the element at the specified index of the list or throws an exception if the index is out of bounds
 * or the element at the index is null.
 *
 * @param index the position of the element to retrieve.
 * @param lazyException a lambda function that provides the exception to be thrown if the element is null
 * or the index is invalid.
 * @return the element at the specified index if it exists and is not null.
 * @since 1.0.0
 */
operator fun <E> List<E>.get(index: Int, lazyException: ThrowableSupplier = { IndexOutOfBoundsException("Index $index not present") }): E =
    try { this[index] } catch (_: Exception) { throw lazyException() }

/**
 * Sorts the list based on the specified sorting direction and a selector function.
 *
 * @param E the type of elements in the list.
 * @param R the type of the property to sort by, which must be comparable.
 * @param direction the sorting direction, either [SortDirection.Ascending] for increasing order or
 * [SortDirection.Descending] for decreasing order.
 * @param selector a function that extracts the property to sort by from each element in the list.
 *
 * @since 1.0.0
 */
inline fun <E, R : Comparable<R>> MList<E>.sortBy(direction: SortDirection, crossinline selector: Transformer<E, R?>) = when (direction) {
    SortDirection.Ascending -> kSortBy(selector)
    SortDirection.Descending -> kSortByDescending(selector)
}

/**
 * Sorts the elements of the list in the specified direction.
 *
 * @param direction The sorting order to apply. It can be either [SortDirection.Ascending] or [SortDirection.Descending], indicating whether the list should be sorted in ascending or
 *  descending order, respectively.
 * @since 1.0.0
 */
infix fun <E : Comparable<E>> MList<E>.sort(direction: SortDirection) = when (direction) {
    SortDirection.Ascending -> sort()
    SortDirection.Descending -> sortDescending()
}

/**
 * Sorts the elements of an [Iterable] based on the specified sorting direction.
 *
 * @param direction The sorting order defined by the [SortDirection] enumeration.
 *                  Use [SortDirection.Ascending] for ascending order and
 *                  [SortDirection.Descending] for descending order.
 * @return A list of elements sorted in the specified order.
 * @since 1.0.0
 */
infix fun <E: Comparable<E>> Iterable<E>.sorted(direction: SortDirection) = when (direction) {
    SortDirection.Ascending -> sorted()
    SortDirection.Descending -> sortedDescending()
}

/**
 * Sorts the elements of an [Iterable] based on a specified sorting order and a selector function.
 *
 * The method applies the given [direction] to sort the elements in either ascending or descending order,
 * determined by the provided [selector] function. Items are sorted in the order defined by the
 * optional transformation [selector], which maps each element to a [Comparable] type.
 *
 * @param E The type of elements in the collection being sorted.
 * @param R The type of the property by which elements are compared; must implement [Comparable].
 * @param direction The sorting direction, either [SortDirection.Ascending] or [SortDirection.Descending].
 * @param selector The selector function used to map elements to their comparable values.
 *
 * @since 1.0.0
 */
inline fun <E, R : Comparable<R>> Iterable<E>.sortedBy(direction: SortDirection, crossinline selector: Transformer<E, R?>) = when (direction) {
    SortDirection.Ascending -> kSortedBy(selector)
    SortDirection.Descending -> sortedByDescending(selector)
}

/**
 * Returns a new list containing elements of the original list within the specified range and filtered by the step value.
 *
 * The method extracts a sublist from the original list based on the provided range's start and end values
 * and applies a step filter to include only the elements at indexes that are divisible by the step value.
 *
 * @param range the range of indexes, consisting of a start, end (inclusive), and step value, to define the portion of the original list to extract.
 * @since 1.0.0
 */
@Deprecated("Use this[range] instead", ReplaceWith("this.get(range)", "dev.tommasop1804.kutils.get"))
fun <E> List<E>.subList(range: IntProgression): List<E> {
    val list = mutableListOf<E>()
    for (i in range) list.add(this[i])
    return list
}
/**
 * Returns a new mutable list containing elements from the specified range of indices in the original list,
 * selected at intervals determined by the step of the range.
 *
 * The returned list includes elements starting from the `range.start` index
 * up to and including the `range.endInclusive` index, but only picks elements
 * based on the step value specified in the range.
 *
 * @param range the range of indices to select, including a step
 * @return a new mutable list containing elements corresponding to the given range and step
 * @throws IndexOutOfBoundsException if start or end indices are out of bounds of this list
 * @since 1.0.0
 */
@Deprecated("Use this[range] instead", ReplaceWith("this.get(range)", "dev.tommasop1804.kutils.get"))
@JvmName("mutableListSubList")
fun <E> MutableList<E>.subList(range: IntProgression): List<E> {
    val list = mutableListOf<E>()
    for (i in range) list.add(this[i])
    return list
}

/**
 * Creates a sublist from the current list or returns an error if the specified indices
 * are out of bounds.
 *
 * @param E The type of elements in the list.
 * @param fromIndex The starting index (inclusive) for the sublist.
 *                   Must be greater than or equal to 0 and less than or equal to the size of the list.
 * @param toIndex The ending index (exclusive) for the sublist.
 *                 Defaults to the size of the list if not provided.
 *                 Must be greater than or equal to `fromIndex` and less than or equal to the size of the list.
 * @return An `Either` object containing the resulting sublist if valid indices are provided,
 *         or an `IndexOutOfBounds` error if the indices are invalid.
 * @since 6.1.0
 */
fun <E> List<E>.subListOrError(fromIndex: Int, toIndex: Int = size) = either {
    catching({ this@subListOrError.subList(fromIndex, toIndex) }) { e: IndexOutOfBoundsException ->
        IndexOutOfBounds(tryOrNull { e.message.orEmpty().let { (it / Char.SPACE).second().toInt() } })
    }
}
/**
 * Extracts a sublist of elements from a list using the given range or raises an error if the indices are out of bounds.
 * The retrieved sublist is determined by the specified range of indices, and any `IndexOutOfBoundsException`
 * is transformed into a custom `IndexOutOfBounds` error.
 *
 * @param range The progression of indices specifying the range of elements to extract from the list.
 *              If the range is out of bounds for the list, an `IndexOutOfBounds` error is raised.
 * @return Either a successful sublist (`Right`) or an error (`Left`) in case of an invalid range.
 * @since 6.1.0
 */
fun <E> List<E>.subListOrError(range: IntProgression) = either {
    catching({ this@subListOrError[range] }) { e: IndexOutOfBoundsException ->
        IndexOutOfBounds(tryOrNull { e.message.orEmpty().let { (it / Char.SPACE).second().toInt() } })
    }
}

/**
 * Returns a sublist from the beginning of the list up to, but not including, the first occurrence of the given element.
 * If the element is not found in the list, an empty list is returned.
 *
 * @param element the element before which the sublist ends.
 * @since 1.0.0
 */
infix fun <E> List<E>.before(element: E) = if (contains(element)) get(0..<indexOf(element)) else emptyList()
/**
 * Returns a sublist of elements from the beginning of the list up to and including the specified element.
 *
 * The method locates the specified element in the list and creates a new sublist
 * that contains all elements from the start of the list through the specified element, inclusive.
 * If the element is not found, an IndexOutOfBoundsException will be thrown.
 *
 * @param element the element up to and including which the sublist should be created.
 * @since 1.0.0
 */
infix fun <E> List<E>.beforeIncluding(element: E) = if (contains(element)) get(0..indexOf(element)) else emptyList()
/**
 * Returns a sublist of the original list containing all elements before the last occurrence
 * of the specified element. If the element is not found, the resulting sublist will be empty.
 *
 * @param element the element to locate within the list. The sublist will end at the index before the last occurrence of this element.
 * @since 1.0.0
 */
infix fun <E> List<E>.beforeLast(element: E) = if (contains(element)) get(0..<lastIndexOf(element)) else emptyList()
/**
 * Returns a sublist of elements from the beginning of the list up to and including
 * the last occurrence of the specified element.
 *
 * This function searches for the last occurrence of the given element within the list
 * and returns a new list containing all elements from the start of the original list
 * up to and including that occurrence. If the element is not found, it will throw an
 * exception.
 *
 * @param element the element up to and including which the sublist is created.
 * @return a new list containing elements from the start up to the last occurrence of the element.
 * @throws IndexOutOfBoundsException if the element does not exist in the list.
 * @since 1.0.0
 */
infix fun <E> List<E>.beforeLastIncluding(element: E) = if (contains(element)) get(0..lastIndexOf(element)) else emptyList()
/**
 * Returns a sublist of the original list that contains all the elements appearing after
 * the first occurrence of the specified [element].
 * If the [element] is not present in the list, an empty list is returned.
 *
 * @param element the element after which the sublist starts.
 * @since 1.0.0
 */
infix fun <E> List<E>.after(element: E) = if (contains(element)) get(indexOf(element) + 1..<size) else emptyList()
/**
 * Returns a new list containing all elements in the original list starting from the specified element,
 * including the element itself.
 *
 * This function finds the first occurrence of the specified element in the list and creates a sublist
 * starting from that position to the end of the list. If the element is not found, the behavior may
 * result in an exception.
 *
 * @param element the element from which the sublist starts, inclusive.
 * @since 1.0.0
 */
infix fun <E> List<E>.afterIncluding(element: E) = if (contains(element)) get(indexOf(element)..<size) else emptyList()
/**
 * Returns a sublist of this list containing all elements that appear after the last occurrence of the specified [element].
 * If the [element] is not found in the list, or is the last element of the list, returns an empty list.
 *
 * @param element the element after which the sublist is formed.
 * @since 1.0.0
 */
infix fun <E> List<E>.afterLast(element: E) = if (contains(element)) get(lastIndexOf(element) + 1..<size) else emptyList()
/**
 * Returns a sublist containing all elements starting from and including the last occurrence
 * of the specified element to the end of the original list.
 *
 * If the element is not found, throws an `IndexOutOfBoundsException`.
 *
 * @param element the element to locate and start the sublist from, including the element itself.
 * @since 1.0.0
 */
infix fun <E> List<E>.afterLastIncluding(element: E) = if (contains(element)) get(lastIndexOf(element)..<size) else emptyList()

/**
 * Returns the element at the position corresponding to the given percentage of the list's size.
 *
 * This method calculates the index in the list based on the specified percentage and returns
 * the element at that index. The list should not be empty, and the percentage must be within
 * the range of 0 to 100 inclusive.
 *
 * @param p The percentage of the list's size (0 to 100 inclusive).
 * @return The element at the calculated index based on the percentage.
 * @throws IndexOutOfBoundsException If the list is empty.
 * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the percentage is not within the 0 to 100 range.
 * @since 1.0.0
 */
@Deprecated("Use get operator instead", replaceWith = ReplaceWith("this[p]"))
infix fun <E> List<E>.percent(p: Percentage): E {
    isNotEmpty() || throw IndexOutOfBoundsException("List is empty.")
    validate(p.isNotOverflowing) { "Percentage must be between 0 and 100." }
    val index = if (p.isFull) size - 1 else (p.toDouble() / 100 * size).toInt()
    return this[index]
}
/**
 * Retrieves an element from the list based on the given percentage.
 * The percentage is used to calculate the index of the element to retrieve.
 * Returns the element wrapped in an `Either` structure or an error if the operation fails.
 *
 * @param p The percentage value used to determine the element's position. It must be between 0 and 100.
 * @return Either an `IndexOutOfBoundsErrors` if the index is invalid or the element at the calculated position.
 * @since 6.1.0
 */
infix fun <E> List<E>.percentOrError(p: Percentage): Either<IndexOutOfBoundsError, E> = either {
    ensure(isNotEmpty()) { Empty }
    validate(p.isNotOverflowing) { "Percentage must be between 0 and 100." }
    val index = if (p.isFull) size - 1 else (p.toDouble() / 100 * size).toInt()
    catching({ this@percentOrError[index] }) { e: IndexOutOfBoundsException ->
        IndexOutOfBounds(tryOrNull { e.message.orEmpty().let { (it / Char.SPACE).second().toInt() } })
    }
}

/**
 * Returns a Map containing key-value pairs provided by transforming
 * each element of the original Iterable using the given key and value functions.
 *
 * @param E the type of elements in the original Iterable
 * @param K the type of keys in the resulting Map
 * @param V the type of values in the resulting Map
 * @param key the function that provides the key for each element
 * @param value the function that provides the value for each element
 * @since 1.0.0
 */
inline fun <E, K, V> Iterable<E>.associate(key: Transformer<E, K>, value: Transformer<E, V>) =
    associate { key(it) to value(it) }

/**
 * Populates the given [destination] mutable map with key-value pairs where keys and values are
 * generated by applying the provided [key] and [value] functions to elements of the original collection.
 * The [key] function is used to calculate the key for each element, and the [value] function is
 * used to calculate the value.
 *
 * @param E the type of elements in the collection
 * @param K the type of keys in the resulting map
 * @param V the type of values in the resulting map
 * @param M the type of the mutable map that serves as the destination
 * @param destination the mutable map where the resulting key-value pairs are added
 * @param key a function to derive the key from each element
 * @param value a function to derive the value from each element
 * @since 1.0.0
 */
inline fun <E, K, V, M : MutableMap<in K, in V>> Iterable<E>.associateTo(
    destination: M,
    key: Transformer<E, K>,
    value: Transformer<E, V>
) = associateTo(destination) { key(it) to value(it) }

/**
 * Filters the elements of the given iterables where the value of the specified property is not null.
 *
 * @param element The property accessor used to evaluate each element's property for null-checking.
 * @return A list containing only the elements where the specified property is not null.
 * @since 1.0.0
 */
inline infix fun <E, R> Iterable<E>.filterNotNull(element: Transformer<E, R>) =
    filter { element(it) != null }

/**
 * Filters the elements of the given iterables where the value of the specified property is null.
 *
 * @param element The property accessor used to evaluate each element's property for null-checking.
 * @return A list containing only the elements where the specified property is null.
 * @since 1.0.0
 */
inline infix fun <E, R> Iterable<E>.filterNull(element: Transformer<E, R>) =
    filter { element(it) == null }

/**
 * Removes elements from the collection that match the specified predicate.
 *
 * @param removeIf A predicate used to test elements. Elements that satisfy this predicate will be removed from the collection.
 * @since 3.5.1
 */
operator fun <E> MCollection<E>.minusAssign(removeIf: Predicate<E>) {
    removeIf(removeIf)
}

/**
 * Returns a list containing elements of the original iterables excluding those that match the given predicate.
 *
 * @param filterNot The predicate used to test whether an element should be excluded.
 * @return A list of elements excluding those that match the predicate.
 * @since 3.5.1
 */
operator fun <E> Iterable<E>.minus(filterNot: Predicate<E>) = filterNot(filterNot)

/**
 * Stands for `controlledEach`. You can use [continueLoop] and [breakLoop].
 *
 * Iterates through each element in the iterables collection and applies the given block of code to it.
 * Returns the original iterables collection after the operation.
 *
 * @param E the type of elements in the iterables collection.
 * @param block the action to be performed on each element.
 * @return the original iterables collection after applying the block to each element.
 * @since 2.0.0
 */
inline fun <T : Iterable<E>, E> T.cForEach(block: ReceiverBiConsumer<LoopContext, E>) = apply {
    with(LoopContext()) {
        for (element in this@cForEach) {
            try {
                block(element)
            } catch (b: Break) {
                break
            } catch (c: Continue) {
                continue
            }
        }
    }
}
/**
 * Stands for `controlledEachIndexed`. You can use [continueLoop] and [breakLoop].
 *
 * Iterates over each element in the iterables along with its index, allowing processing within a loop context.
 *
 * This method provides a custom loop control mechanism using `LoopContext`. It supports breaking or continuing
 * the loop execution through the utilization of `Break` and `Continue` exceptions respectively.
 *
 * @param E the type of elements in the iterables.
 * @param block a lambda function that accepts the `LoopContext`, the index of the current element, and the current element itself.
 *              The `block` is executed for each element in the iterables.
 * @since 2.0.0
 */
inline fun <T : Iterable<E>, E> T.cForEachIndexed(block: ReceiverTriConsumer<LoopContext, Int, E>) = apply {
    with(LoopContext()) {
        for ([index, element] in withIndex()) {
            try {
                block(index, element)
            } catch (b: Break) {
                break
            } catch (c: Continue) {
                continue
            }
        }
    }
}

/**
 * Stands for `eachWithReturn`. You can use [continueLoop] and [breakLoop] to return a value.
 *
 * Performs the given [action] on each element of the iterables. If a `Break` exception is thrown during
 * the iteration, the result encapsulated within the `Break` is returned. If no `Break` is thrown,
 * the method returns `null`.
 *
 * @param action The action to be performed on each element in the iterables. The action can throw a
 * `Break` to provide a custom result and terminate the iteration early.
 * @return The result enclosed in the `Break` exception if thrown during the iteration, or `null`
 * if the entire iterables is processed without interruption.
 * @since 2.0.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <T : Iterable<E>, E, R> T.rForEach(action: ReceiverBiConsumer<LoopContext, E>): R? {
    with(LoopContext()) {
        for (element in this@rForEach) {
            try {
                action(element)
            } catch (b: Break) {
                return b.result as? R
            } catch (c: Continue) {
                continue
            }
        }
    }
    return null
}
/**
 * Stands for `eachWithReturn`. You can use [continueLoop] and [breakLoop] to return a value.
 *
 * Iterates through elements of the `Iterable` along with their index, invoking the given `action`
 * for each element, and allowing for a custom result to be returned when a `Break` exception is thrown.
 *
 * @param action A function that is invoked with the index of an element and the element itself
 *               during iteration. Throwing a `Break` exception from this function allows for
 *               interrupting the iteration and returning a result.
 * @return The result passed within the thrown `Break` exception if caught, or `null` if the iteration completes without any `Break` being thrown.
 * @since 2.0.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <T : Iterable<E>, E, R> T.rForEachIndexed(action: ReceiverTriConsumer<LoopContext, Int, E>): R? {
    with(LoopContext()) {
        for ([index, element] in withIndex()) {
            try {
                action(index, element)
            } catch (b: Break) {
                return b.result as? R
            } catch (c: Continue) {
                continue
            }
        }
    }
    return null
}

/**
 * Converts the elements in the iterables into a string, separated by the specified separator,
 * starting with the given prefix and ending with the given postfix.
 *
 * This method allows customization of how elements are represented through a transform function
 * and supports limiting the number of elements included in the resulting string.
 *
 * @param separator The character used to separate the elements in the resulting string.
 * @param prefix The character sequence added to the beginning of the resulting string. Default is an empty string.
 * @param postfix The character sequence added to the end of the resulting string. Default is an empty string.
 * @param limit The maximum number of elements to include in the resulting string. A negative value means no limit. Default is -1.
 * @param truncated The character sequence that replaces omitted elements if the limit is applied. Default is "...".
 * @param transform An optional lambda function for transforming each element in the iterables to a CharSequence. If null, default string conversion is used.
 * @since 1.0.0
 */
fun <E> Iterable<E>.joinToString(separator: Char, prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: Transformer<E, CharSequence>? = null) =
    joinTo(StringBuilder(), separator.toString(), prefix, postfix, limit, truncated, transform).toString()

/**
 * Processes the elements of the iterables using the provided collector,
 * combining elements into a single result as defined by the collector implementation.
 *
 * @param E the type of elements in the iterables
 * @param A an intermediate accumulation type used by the collector
 * @param R the final result type produced by the collector
 * @param collector the collector that defines the logic for accumulation and final result construction
 * @return the result generated by the collector after processing all elements of the iterables
 * @since 1.0.0
 */
fun <E, A, R> Iterable<E>.legacyCollect(collector: Collector<E, A, R>): R = toList().stream().collect(collector)
/**
 * Processes the elements of the iterables using a mutable result container. Elements are supplied,
 * accumulated, and combined using the provided functions.
 *
 * @param supplier a function that provides a new result container. This is responsible for creating
 *                 the result object where elements will be accumulated.
 * @param accumulator a function that incorporates an element into a result container. This function
 *                    defines how each item of the iterables should modify the result container.
 * @param combiner a function that combines two result containers into one. This is used to merge results
 *                 when parallel processing or splitting of tasks occurs.
 * @return the final result obtained by accumulating all elements of the iterables, potentially combining
 *         multiple intermediate results into one.
 * @since 1.0.0
 */
fun <E, R> Iterable<E>.legacyCollect(
    supplier: Supplier<R>,
    accumulator: (resultContainer: R, element: E) -> Unit,
    combiner: (resultContainer: R, partialContainer: R) -> Unit
): R = toList().stream().collect(supplier, accumulator, combiner)

/**
 * Converts the current collection into a mutable list of the same type.
 *
 * @return A new mutable list containing all elements of the original collection.
 * @since 1.0.0
 */
fun <E> Iterable<E>.toMList(): MList<E> = toMutableList()
/**
 * Converts the given collection into a mutable set, preserving unique elements and discarding duplicates.
 *
 * @return A mutable set containing the distinct elements of the original collection.
 * @since 1.0.0
 */
fun <E> Iterable<E>.toMSet(): MSet<E> = toMutableSet()

/**
 * Creates a new instance of an MList containing the specified elements.
 *
 * @param E the type of elements in the list.
 * @param elements the elements to be included in the created MList.
 * @return a new MList containing the specified elements.
 * @since 1.0.0
 */
fun <E> mListOf(vararg elements: E): MList<E> = mutableListOf(*elements)
/**
 * Creates a mutable set containing the specified elements.
 *
 * @param elements The elements to be included in the mutable set.
 * @return A mutable set containing the specified elements.
 * @since 1.0.0
 */
fun <E> mSetOf(vararg elements: E): MSet<E> = mutableSetOf(*elements)

/**
 * Converts the array into a mutable list.
 *
 * @return a mutable list containing all the elements of the array in the same order.
 * @since 1.0.0
 */
fun <T> Array<T>.toMList(): MList<T> = toMutableList()
/**
 * Converts the given IntArray to an IntMList.
 *
 * @return a mutable list containing all elements of the original IntArray.
 * @since 1.0.0
 */
fun IntArray.toMList(): MList<Int> = toMutableList()
/**
 * Converts the LongArray to a LongMList, which is a mutable list structure.
 *
 * @return A LongMList containing elements from the original LongArray.
 * @since 1.0.0
 */
fun LongArray.toMList(): MList<Long> = toMutableList()
/**
 * Converts the DoubleArray to a DoubleMList, which is a mutable list representation
 * of the array's elements.
 *
 * @return A DoubleMList containing all the elements of the DoubleArray.
 * @since 1.0.0
 */
fun DoubleArray.toMList(): MList<Double> = toMutableList()
/**
 * Converts the CharArray to a mutable list of characters (CharMList).
 *
 * This function transforms the array of characters into a mutable list
 * while preserving the element order.
 *
 * @return a mutable list of characters (CharMList) representing the input CharArray
 * @since 1.0.0
 */
fun CharArray.toMList(): MList<Char> = toMutableList()

/**
 * Converts the array into a mutable set.
 *
 * This function transforms the elements of the array into a mutable set of type MSet<T>.
 * Duplicates are removed, and the order of the elements is not guaranteed after the transformation.
 *
 * @return a mutable set containing the unique elements of the array
 * @since 1.0.0
 */
fun <T> Array<T>.toMSet(): MSet<T> = toMutableSet()
/**
 * Converts the given IntArray into an IntMSet, which represents a mutable set of integers.
 *
 * @return a mutable set containing all unique integers from the original array.
 * @since 1.0.0
 */
fun IntArray.toMSet(): MSet<Int> = toMutableSet()
/**
 * Converts the `LongArray` to a `LongMSet` by transforming it into a mutable set.
 *
 * @return a `LongMSet` containing the unique elements of the original `LongArray`.
 * @since 1.0.0
 */
fun LongArray.toMSet(): MSet<Long> = toMutableSet()
/**
 * Converts a DoubleArray into a DoubleMSet. The resulting set will contain
 * all unique elements from the original array.
 *
 * @return a DoubleMSet containing all unique elements from the DoubleArray.
 * @since 1.0.0
 */
fun DoubleArray.toMSet(): MSet<Double> = toMutableSet()
/**
 * Converts the array of characters into a mutable set of characters.
 *
 * @return A mutable set containing the unique characters from the array.
 * @since 1.0.0
 */
fun CharArray.toMSet(): MSet<Char> = toMutableSet()

/**
 * Creates and returns an empty mutable list of type `MList<E>`.
 *
 * This utility function is used to create a new, empty mutable list of the specified generic type.
 *
 * @return A newly created, empty mutable list of type `MList<E>`.
 * @since 1.0.0
 */
fun <E> emptyMList(): MList<E> = mutableListOf()
/**
 * Creates and returns a new, empty mutable set.
 *
 * This function provides a type-safe way to create an empty mutable set without specifying the element type explicitly.
 *
 * @return a new, empty [MutableSet] with the specified generic type.
 * @since 1.0.0
 */
fun <E> emptyMSet(): MSet<E> = mutableSetOf()