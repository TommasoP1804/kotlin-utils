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
import dev.tommasop1804.kutils.classes.numbers.*
import dev.tommasop1804.kutils.exceptions.*
import java.util.stream.Collector
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.collections.map as kMap
import kotlin.collections.sortBy as kSortBy
import kotlin.collections.sortByDescending as kSortByDescending
import kotlin.collections.sortedBy as kSortedBy

/**
 * Generates a map that represents the cardinality (frequency) of elements in the given iterable.
 * Each key in the resultant map corresponds to a unique element from the iterable,
 * and its value represents the number of times that element appears in the iterable.
 *
 * If the iterable is null, an empty map is returned.
 *
 * @receiver the iterable of elements to be processed; nullable.
 * @return a map where keys are the elements from the iterable and values are their respective frequencies.
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
 * Checks if the iterable contains duplicate elements.
 *
 * This function returns `true` if any element in the iterable appears more than once,
 * otherwise, it returns `false`.
 *
 * @return `true` if the iterable contains duplicate elements, `false` otherwise.
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
    if (element.isNull()) {
        for (e in this)
            if (e.isNull()) return
        add(null)
    }
    if (!contains(element)) add(element)
}

/**
 * Checks if there is any intersection between the current iterable and another iterable.
 *
 * @param other The iterable to check for intersection with the current iterable.
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
 * Checks if the iterable contains any of the specified elements.
 *
 * This function returns `true` if at least one element from the `iterable` array
 * is present in the iterable.
 *
 * @receiver the iterable of elements to be processed; nullable.
 * @param elements The elements to check for in the iterable.
 * @return `true` if the iterable contains any of the specified elements, `false` otherwise.
 * @since 1.0.0
 */
fun <E> Iterable<E>.containsAny(vararg elements: E) = any { it in elements }

/**
 * Checks if the specified iterable does not contain any of the given elements.
 * Returns `true` if none of its elements are present
 * in the provided elements. Otherwise, returns `false`.
 *
 * @receiver the iterable of elements to be processed; nullable.
 * @param elements The elements to check against the iterable.
 * @return `true` if the iterable does not contain any of the specified elements, `false` otherwise.
 * @since 1.0.0
 */
fun <E> Iterable<E>.containsNone(vararg elements: E) = none { it in elements }

/**
 * Checks if any element in the iterable satisfies the given predicate.
 *
 * @param predicate A condition that each element will be tested against.
 * @return `true` if at least one element satisfies the predicate, otherwise `false`.
 * @since 1.1.0
 */
operator fun <E> Iterable<E>.contains(predicate: Predicate<E>) = any { predicate(it) }

/**
 * Returns the first element of the iterable if it exists, or throws an exception created
 * by the provided lambda function if the iterable is null or empty.
 *
 * @receiver the iterable on which the operation is applied
 * @param lazyException a lambda function that provides the exception to be thrown
 * if the iterable is null or empty
 * @return the first element of the iterable
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <E> Iterable<E>.firstOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return firstOrNull() ?: throw lazyException()
}
/**
 * Returns the first element in the iterable that matches the given [predicate].
 * If no such element is found, throws an exception provided by [lazyException].
 *
 * @receiver the iterable to search for the element
 * @param lazyException a lambda providing the exception to be thrown if no element matches the predicate
 * @param predicate a function that defines the condition to match the element
 * @return the first element that matches the predicate
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <E> Iterable<E>.firstOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return firstOrNull(predicate) ?: throw lazyException()
}
/**
 * Returns the first element of the iterable if it exists, otherwise returns the value produced by the provided [default] function.
 *
 * @receiver the iterable to retrieve the first element from
 * @param default a lambda function that produces a default value if the iterable is empty
 * @return the first element of the iterable or the value produced by the [default] function
 * @since 1.0.0
 */
fun <E> Iterable<E>.firstOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return firstOrNull() ?: default()
}
/**
 * Returns the first element matching the given [predicate], or the result of the [default] function
 * if no such element is found.
 *
 * @receiver the iterable to search for the element.
 * @param default a function that provides a fallback value if no element matches the [predicate].
 * @param predicate a function that determines whether an element matches the given condition.
 * @return the first matching element if found, otherwise the result of the [default] function.
 * @since 1.0.0
 */
fun <E> Iterable<E>.firstOr(default: Supplier<E>, predicate: Predicate<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return firstOrNull(predicate) ?: default()
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
 * Returns the second element of a list that matches the given predicate.
 *
 * Filters the list based on the provided predicate and retrieves the second element
 * from the filtered list. If there are fewer than two elements matching the predicate,
 * this function will throw an exception or result in a runtime error depending on
 * underlying implementations.
 *
 * @param predicate a condition to filter the elements in the list.
 * @return the second element that matches the predicate.
 * @throws NoSuchElementException if there are fewer than two elements matching the predicate.
 * @since 2.1.0
 */
fun <E> List<E>.second(predicate: Predicate<E>) = filter(predicate).second()
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
 * Returns the second element in the list that matches the specified [predicate],
 * or `null` if no such element is found or if there are fewer than two matching elements.
 *
 * @param predicate a function that defines the condition to filter the elements of the list.
 * @return the second element satisfying the given predicate, or `null` if no such element exists.
 * @since 2.1.0
 */
fun <E> List<E>.secondOrNull(predicate: Predicate<E>) = filter(predicate).secondOrNull()
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
 * Returns the second element in the list that matches the given predicate or throws an exception
 * provided by the given `lazyException` supplier if no such element exists.
 *
 * @param lazyException a supplier for the exception to throw if there are not enough matching elements
 * @param predicate a condition to filter elements of the list
 * @since 2.1.0
 */
@IgnorableReturnValue
fun <E> List<E>.secondOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>) = filter(predicate).secondOrThrow(lazyException)
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
 * Returns the second element in the list that matches the given predicate, or the value provided by
 * the default supplier if no such element exists or there are less than two elements.
 *
 * @param default A supplier function that provides a default value if the list does not contain
 *                a valid second element matching the predicate.
 * @param predicate A function that determines whether a given element in the list matches the criteria.
 * @since 2.1.0
 */
fun <E> List<E>.secondOr(default: Supplier<E>, predicate: Predicate<E>) = filter(predicate).secondOr(default)

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
 * Returns the third element in the list matching the given predicate after filtering.
 *
 * The method first filters the elements of the list using the provided predicate and
 * then attempts to retrieve the third element from the filtered result. If the filtered
 * list has less than three elements, this method will result in an exception.
 *
 * @param predicate A predicate to filter the elements of the list.
 * @since 2.1.0
 */
fun <E> List<E>.third(predicate: Predicate<E>) = filter(predicate).third()
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
 * Returns the third element that matches the given [predicate], or `null` if no such element exists.
 *
 * The search for the matching element is performed by filtering the list based on the given [predicate].
 *
 * @param predicate the condition used to filter the elements of the list.
 * @return the third element matching the [predicate], or `null` if there are less than three matching elements.
 * @since 2.1.0
 */
fun <E> List<E>.thirdOrNull(predicate: Predicate<E>) = filter(predicate).thirdOrNull()
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
 * Returns the third element in the list that matches the given predicate or throws an exception
 * provided by the lazyException supplier if there are less than three matching elements.
 *
 * @param lazyException a supplier that provides the exception to be thrown if the conditions are not met
 * @param predicate a condition to filter elements in the list
 * @since 2.1.0
 */
@IgnorableReturnValue
fun <E> List<E>.thirdOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>) = filter(predicate).thirdOrThrow(lazyException)
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
 * Returns the third element of the list that matches the given predicate if it exists; otherwise, returns the value supplied by the provided default supplier.
 * The matching elements are determined by filtering the list based on the given predicate.
 *
 * @param default A supplier function that provides a default value when the list does not contain at least three elements
 *                matching the given predicate.
 * @param predicate A predicate function used to filter the list.
 * @since 2.1.0
 */
fun <E> List<E>.thirdOr(default: Supplier<E>, predicate: Predicate<E>) = filter(predicate).thirdOr(default)

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
 * Finds the mode (the most frequently occurring element) in the given iterable.
 *
 * If there are multiple elements with the same frequency, the result will be the first one
 * encountered in the iteration. If the iterable is empty, null is returned.
 *
 * @return The most frequent element in the iterable or null if the iterable is empty.
 * @since 1.0.0
 */
fun <E> Iterable<E>.mode(): E? = groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

/**
 * Returns the single element in the iterable if it contains exactly one element.
 * Throws an exception otherwise:
 * - [NoSuchElementException] if the iterable is empty.
 * - [TooManyElementsException] if the iterable contains more than one element.
 *
 * @receiver An iterable collection of type [E].
 * @return The single element in the iterable.
 * @throws NoSuchElementException if the iterable is empty.
 * @throws TooManyElementsException if the iterable contains more than one element.
 * @since 1.0.0
 */
fun <E> Iterable<E>.onlyElement() = toList().run {
    if (isEmpty()) throw NoSuchElementException() 
    else if (size == 1) first() else throw TooManyElementsException(size)
}
/**
 * Returns the single element in the iterable if it contains exactly one element,
 * or `null` if the iterable is empty or contains more than one element.
 *
 * @receiver an iterable of elements
 * @return the single element in the iterable or `null` if the conditions are not met
 * @since 1.0.0
 */
fun <E> Iterable<E>.onlyElementOrNull() = toList().run { if (size == 1) first() else null }
/**
 * Returns the single element in the iterable if it contains only one element; otherwise,
 * it returns the value supplied by the provided [default] supplier.
 *
 * @param E the type of elements in the iterable
 * @param default a supplier that provides a default value if the iterable does not contain exactly one element
 * @return the single element in the iterable if there is exactly one, or the value supplied by [default]
 * @since 1.0.0
 */
infix fun <E> Iterable<E>.onlyElementOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return toList().run { if (size == 1) first() else default() }
}
/**
 * Returns the single element of the iterable if it contains exactly one element; otherwise, throws an exception provided by the given supplier.
 *
 * @param lazyException a supplier function that provides the exception to be thrown when the iterable does not contain exactly one element
 * @return the single element of the iterable if there is exactly one element
 * @throws Throwable the exception supplied by the `lazyException` if the iterable does not contain exactly one element
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
 * Filters the elements of an iterable based on a predicate and ensures that exactly one element
 * satisfies the predicate. If no elements or more than one element satisfy the predicate, an exception is thrown.
 *
 * @param predicate a predicate to filter the elements of the iterable
 * @return the single element that satisfies the predicate
 * @throws NoSuchElementException if no elements satisfy the predicate
 * @throws TooFewResultsException if the resulting size from filtering is less than the expected minimum (1)
 * @throws TooManyResultsException if more than one element satisfies the predicate
 * @since 1.0.0
 */
infix fun <E> Iterable<E>.onlyElement(predicate: Predicate<E>) = toList()
    .requireOrThrow({ NoSuchElementException() }, { it.isNotEmpty() })
    .filter(predicate).run {
        if (size == 1) first()
        else throw if (size > 1) TooManyResultsException(size) else TooFewResultsException(size)
    }
/**
 * Returns the single element matching the given [predicate], or `null` if no such element exists
 * or if there is more than one matching element in the iterable.
 *
 * @param predicate A lambda function used to filter elements in the iterable. The function should
 * return `true` for elements you want to include in the operation.
 * @since 1.0.0
 */
infix fun <E> Iterable<E>.onlyElementOrNull(predicate: Predicate<E>) = filter(predicate).run { if (size == 1) first() else null }
/**
 * Returns the single element that matches the given predicate if exactly one element matches,
 * otherwise returns the result from the default supplier.
 *
 * @param default A supplier function that provides a default value when no element
 * or more than one element matches the predicate.
 * @param predicate A predicate to filter the elements in the iterable.
 * @since 1.0.0
 */
fun <E> Iterable<E>.onlyElementOr(default: Supplier<E>, predicate: Predicate<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return filter(predicate).run { if (size == 1) first() else default() }
}
/**
 * Returns the single element matching the given [predicate] from the iterable or throws an exception
 * provided by [lazyException] if the condition is not met. The method ensures that exactly one element
 * matches the predicate.
 *
 * @param lazyException a supplier for the exception to be thrown if the number of matching elements
 * is not exactly one.
 * @param predicate a condition to be checked for each element in the iterable.
 * @return the single element that matches the [predicate].
 * @throws Throwable the exception supplied by [lazyException] if no element or more than one element matches.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <E> Iterable<E>.onlyElementOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return filter(predicate).run { if (size == 1) first() else throw lazyException() }
}

/**
 * Checks if the collection is not null and not empty.
 *
 * This method allows for safe checking of a nullable collection, ensuring it is both non-null
 * and contains elements.
 *
 * @receiver The nullable collection to be checked.
 * @return `true` if the collection is non-null and contains at least one element, otherwise `false`.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
@Suppress("kutils_null_check")
fun <E> Collection<E>?.isNotNullOrEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return isNotNull() && isNotEmpty()
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
@OptIn(ExperimentalContracts::class)
inline fun <C : Collection<E>, E> C?.ifNullOrEmpty(defaultValue: Supplier<C>): C {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
    }
    return if (isNullOrEmpty()) defaultValue() else this
}
/**
 * Executes the given action if the collection is not empty.
 *
 * @param action A transformer that will be executed if the collection is not empty.
 * @return The result of the action if the collection is not empty, or the collection itself otherwise.
 * @since 3.1.3
 */
@Suppress("UNCHECKED_CAST")
@IgnorableReturnValue
inline fun <C : Collection<E>, E, R> C.ifNotEmpty(action: ReceiverTransformer<C, R>): R {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    return (if (isNotEmpty()) action(this) else this) as R
}
/**
 * Performs the given [block] on the collection if it is not null and not empty.
 *
 * This method provides a safe way to execute an operation on a nullable collection only
 * when it contains elements. If the collection is null or empty, no action is performed
 * and the collection itself is returned.
 *
 * @param block The lambda function or transformation to be applied on the collection
 *               if it is not null and not empty.
 * @return The result of the [block] if the collection is not null and not empty,
 *         otherwise the collection itself as is.
 * @since 3.1.3
 */
@OptIn(ExperimentalExtendedContracts::class)
@Suppress("UNCHECKED_CAST")
@IgnorableReturnValue
inline fun <C : Collection<E>?, E, R> C?.ifNotNullOrEmpty(block: ReceiverTransformer<C, R>): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        (this@ifNotNullOrEmpty != null) holdsIn block
    }
    return (if (isNotNullOrEmpty()) block(this) else this) as R
}

/**
 * Filters elements in the iterable based on their index, keeping only those at positions
 * that are multiples of the specified step value.
 *
 * @param step The interval between indices to consider for filtering. Must be greater than 0.
 * @return A list of elements from the original iterable, selected at the specified step interval.
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
operator fun <E> Collection<E>?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty()
}

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
 * Filters the elements of the iterable based on the provided predicate.
 *
 * This operator function allows the use of the `invoke` function to apply a filter directly to an iterable collection. 
 *
 * @param filter the predicate used to test each element in the iterable. Only elements that satisfy the predicate will be included in the resulting collection.
 * @since 1.0.0
 */
inline operator fun <E> Iterable<E>.invoke(filter: Predicate<E>): List<E> {
    val destination = ArrayList<E>()
    for (element in this) if (filter(element)) destination.add(element)
    return destination
}

/**
 * Retrieves the first element in the iterable that matches the given predicate.
 *
 * @param E The type of elements in the iterable.
 * @param find A predicate used to determine the matching element.
 * @return The first element that satisfies the predicate.
 * @since 1.0.0
 */
inline operator fun <E> Iterable<E>.get(find: Predicate<E>): E? {
    for (element in this) if (find(element)) return element
    return null
}
/**
 * Retrieves the first element in the iterable that matches the given predicate.
 *
 * @param E The type of elements in the iterable.
 * @param find A predicate used to determine the matching element.
 * @param lazyException A supplier for the exception to be thrown if no element is found.
 * @return The first element that satisfies the predicate.
 * @since 1.0.0
 */
operator fun <E> Iterable<E>.get(find: Predicate<E>, lazyException: ThrowableSupplier) = find(find) ?: throw lazyException()

/**
 * Searches for the first element in the iterable that matches the specified predicate and returns it.
 * If no element is found, a specified exception is thrown.
 *
 * @param lazyException a supplier that provides the exception to be thrown if no element is found
 * @param find the predicate to apply to elements of the iterable
 * @throws Throwable if no element in the iterable matches the predicate
 * @since 1.0.0
 */
fun <E> Iterable<E>.findOrThrow(lazyException: ThrowableSupplier = { NoSuchElementException("No element found") }, find: Predicate<E>): E & Any {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return find(find) ?: throw lazyException()
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
operator fun <E> List<E>.get(index: Int, lazyException: ThrowableSupplier = { NoSuchElementException("Index $index not present") }): E =
    getOrNull(index) ?: throw lazyException()

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
infix fun <E> List<E>.subList(range: IntProgression): List<E> {
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
infix fun <E> MutableList<E>.subList(range: IntProgression): List<E> {
    val list = mutableListOf<E>()
    for (i in range) list.add(this[i])
    return list
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
 * Filters the elements of the given iterable where the value of the specified property is not null.
 *
 * @param element The property accessor used to evaluate each element's property for null-checking.
 * @return A list containing only the elements where the specified property is not null.
 * @since 1.0.0
 */
inline infix fun <E, R> Iterable<E>.filterNotNull(element: Transformer<E, R>) =
    filter { element(it).isNotNull() }

/**
 * Filters the elements of the given iterable where the value of the specified property is null.
 *
 * @param element The property accessor used to evaluate each element's property for null-checking.
 * @return A list containing only the elements where the specified property is null.
 * @since 1.0.0
 */
inline infix fun <E, R> Iterable<E>.filterNull(element: Transformer<E, R>) =
    filter { element(it).isNull() }

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
 * Returns a list containing elements of the original iterable excluding those that match the given predicate.
 *
 * @param filterNot The predicate used to test whether an element should be excluded.
 * @return A list of elements excluding those that match the predicate.
 * @since 3.5.1
 */
operator fun <E> Iterable<E>.minus(filterNot: Predicate<E>) = filterNot(filterNot)

/**
 * Iterates through each element in the iterable collection and applies the given block of code to it.
 * Returns the original iterable collection after the operation.
 *
 * @param E the type of elements in the iterable collection.
 * @param block the action to be performed on each element.
 * @return the original iterable collection after applying the block to each element.
 * @since 2.0.0
 */
inline fun <E> Iterable<E>.peek(block: Consumer<E>) = apply { for (element in this) block(element) }
/**
 * Stands for `controlledEach`. You can use [continueLoop] and [breakLoop].
 *
 * Iterates through each element in the iterable collection and applies the given block of code to it.
 * Returns the original iterable collection after the operation.
 *
 * @param E the type of elements in the iterable collection.
 * @param block the action to be performed on each element.
 * @return the original iterable collection after applying the block to each element.
 * @since 2.0.0
 */
inline fun <E> Iterable<E>.cForEach(block: ReceiverBiConsumer<LoopContext, E>) = apply {
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
 * Iterates over each element in the iterable along with its index, allowing processing within a loop context.
 *
 * This method provides a custom loop control mechanism using `LoopContext`. It supports breaking or continuing
 * the loop execution through the utilization of `Break` and `Continue` exceptions respectively.
 *
 * @param E the type of elements in the iterable.
 * @param block a lambda function that accepts the `LoopContext`, the index of the current element, and the current element itself.
 *              The `block` is executed for each element in the iterable.
 * @since 2.0.0
 */
inline fun <E> Iterable<E>.cForEachIndexed(block: ReceiverTriConsumer<LoopContext, Int, E>) = apply {
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
 * Performs the given [action] on each element of the iterable. If a `Break` exception is thrown during
 * the iteration, the result encapsulated within the `Break` is returned. If no `Break` is thrown,
 * the method returns `null`.
 *
 * @param action The action to be performed on each element in the iterable. The action can throw a
 * `Break` to provide a custom result and terminate the iteration early.
 * @return The result enclosed in the `Break` exception if thrown during the iteration, or `null`
 * if the entire iterable is processed without interruption.
 * @since 2.0.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <E, R> Iterable<E>.rForEach(action: ReceiverBiConsumer<LoopContext, E>): R? {
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
inline fun <E, R> Iterable<E>.rForEachIndexed(action: ReceiverTriConsumer<LoopContext, Int, E>): R? {
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
 * Invokes the integer operator on a given collection to either take or drop a specific number of elements
 * based on the integer value.
 *
 * @param collection the collection of elements from which elements are taken or dropped.
 * @return a list containing the resulting elements.
 * Returns an empty list if the integer is zero,
 * the first `this` elements if the integer is positive,
 * and all elements except the first `-this` elements if the integer is negative.
 * @since 1.0.0
 */
@Deprecated("Use take or drop instead", ReplaceWith("if (this > 0) sequence.take(this) else sequence.drop(-this)"))
operator fun <E> Int.invoke(collection: Collection<E>): List<E> {
    if (this == 0) return emptyList()
    if (isPositive) return collection.take(this)
    return collection.drop(-this)
}

/**
 * Converts the elements in the iterable into a string, separated by the specified separator,
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
 * @param transform An optional lambda function for transforming each element in the iterable to a CharSequence. If null, default string conversion is used.
 * @since 1.0.0
 */
fun <E> Iterable<E>.joinToString(separator: Char, prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: Transformer<E, CharSequence>? = null) =
    joinTo(StringBuilder(), separator.toString(), prefix, postfix, limit, truncated, transform).toString()

/**
 * Processes the elements of the iterable using the provided collector,
 * combining elements into a single result as defined by the collector implementation.
 *
 * @param E the type of elements in the iterable
 * @param A an intermediate accumulation type used by the collector
 * @param R the final result type produced by the collector
 * @param collector the collector that defines the logic for accumulation and final result construction
 * @return the result generated by the collector after processing all elements of the iterable
 * @since 1.0.0
 */
fun <E, A, R> Iterable<E>.legacyCollect(collector: Collector<E, A, R>): R = toList().stream().collect(collector)
/**
 * Processes the elements of the iterable using a mutable result container. Elements are supplied,
 * accumulated, and combined using the provided functions.
 *
 * @param supplier a function that provides a new result container. This is responsible for creating
 *                 the result object where elements will be accumulated.
 * @param accumulator a function that incorporates an element into a result container. This function
 *                    defines how each item of the iterable should modify the result container.
 * @param combiner a function that combines two result containers into one. This is used to merge results
 *                 when parallel processing or splitting of tasks occurs.
 * @return the final result obtained by accumulating all elements of the iterable, potentially combining
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

/**
 * Validates that the collection is not empty. If the collection is empty, a `ValidationFailedException` is thrown.
 * The exception may optionally have a cause provided by the `causeOf` or `cause` suppliers.
 *
 * @param causeOf a supplier that provides a throwable as the primary cause, or `null` if unused.
 * @param cause a supplier that provides a throwable as a secondary cause, or `null` if unused.
 * @return the original collection if it is not empty.
 * @throws ValidationFailedException if the collection is empty, with an optional cause or nested cause.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The collection is empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The collection is empty.", cause?.invoke()))
    return this
}
/**
 * Ensures that the collection is not empty. If the collection is empty, throws a
 * `ValidationFailedException` with the provided lazy message, and optionally sets
 * the provided cause or initializes the cause from another throwable.
 *
 * @param causeOf an optional supplier for the throwable to set as the cause of the
 *                validation failure. If null, a `ValidationFailedException` is thrown directly.
 * @param cause an optional supplier for the underlying cause of the validation failure,
 *              which can be set in the exception using `initCause`.
 * @param lazyMessage a supplier for the error message to use in the exception when validation fails.
 * @return the original collection if it is not empty.
 * @throws ValidationFailedException if the collection is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that the collection is not empty. If the collection is empty, a `ValidationFailedException` is thrown.
 *
 * @param property The property associated with the validation failure. Can be null if not applicable.
 * @param variableName An optional name for the variable involved in the validation. If provided, it is included in the exception message.
 * @param message An optional descriptive message for the validation failure. Defaults to "is empty" if not provided.
 * @param causeOf A supplier for the throwable that caused the validation failure, if applicable.
 * @param cause An optional supplier for an additional exception to be attached as the cause of `ValidationFailedException`.
 * @return The collection itself if it passes the validation.
 * @throws ValidationFailedException if the collection is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is not empty. If the collection is empty, a `ValidationFailedException` is thrown.
 *
 * @param property the main KProperty associated with the validation, or null if not specified.
 * @param variable an optional secondary KProperty that provides additional context, or null if not specified.
 * @param message an optional message to include in the exception if validation fails; defaults to "is empty".
 * @param causeOf an optional supplier for the cause of the exception if validation fails.
 * @param cause an optional supplier for a chained cause of the exception if validation fails.
 * @return the collection itself if it is not empty.
 * @throws ValidationFailedException if the collection is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that a collection is not empty. If the collection is empty, a `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter being validated. Used in the exception message. Can be null.
 * @param message Custom error message in case validation fails. Defaults to "is empty".
 * @param causeOf Supplier for a `Throwable` that will be thrown if the validation fails, overriding the default exception behavior. Can be null.
 * @param cause Supplier for the root cause `Throwable` to be attached to the exception if thrown. Can be null.
 * @return The collection itself if it is not empty.
 * @throws ValidationFailedException If the collection is empty and no `causeOf` is provided.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is not empty. If the collection is empty, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] associated with this validation, or null if not applicable
 * @param parameter the [KParameter] being validated, or null if not applicable
 * @param message an optional custom error message to include in the exception if the validation fails
 * @param causeOf an optional supplier for the root cause of the validation failure, or null if no such cause exists
 * @param cause an optional supplier for the secondary cause of the validation failure, or null if no such cause exists
 * @return the validated collection if it is not empty
 * @throws ValidationFailedException if the collection is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is not empty. If the collection is empty, a `ValidationFailedException`
 * is thrown with details about the failure.
 *
 * @param callableName the name of the callable (e.g., function or method) related to this validation
 * @param parameterName the name of the parameter being validated, or null if not specified
 * @param message a custom message describing the validation failure, or null to use the default message
 * @param causeOf a supplier for the cause of the exception, which will be used to initialize the root cause of the failure, or null
 * @param cause a supplier for an additional cause of the exception, or null
 * @return the original collection if it is not empty
 * @throws ValidationFailedException if the collection is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is not empty, throwing a `ValidationFailedException` if it is empty.
 *
 * @param callableName The name of the callable (e.g., function or property) where validation is performed. Can be null.
 * @param parameter The `KParameter` instance representing the parameter involved in the validation. Can be null.
 * @param message An optional error message providing more context if validation fails. Defaults to "is empty" if not specified.
 * @param causeOf A supplier for a throwable that can wrap the `ValidationFailedException`. Can be null.
 * @param cause A supplier for the cause of the `ValidationFailedException`. Can be null.
 * @return The same collection instance if it passes the validation.
 *
 * @throws ValidationFailedException if the collection is empty. The exception provides details such as the callable name,
 * parameter, message, or the underlying cause based on the inputs provided.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is neither `null` nor empty. If the validation fails, an exception is thrown.
 *
 * @param causeOf the supplier function for providing a custom exception to be thrown
 *                when the collection is `null` or empty. If this is `null`, a default exception is used.
 * @param cause additional supplier function for providing the cause of the exception, if necessary.
 *              This is optional and can also be `null`.
 * @return the original collection if it is valid (not `null` or empty).
 * @throws ValidationFailedException if the collection is `null` or empty, along with the specified or default cause.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The collection is null or empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The collection is null or empty.", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is not null or empty. If the validation fails,
 * a `ValidationFailedException` is thrown with the specified lazy message and optional cause.
 *
 * @param causeOf a supplier of the throwable to be used as the cause of the exception if provided.
 * @param cause a supplier of the throwable to be used as an additional cause of the exception if provided.
 * @param lazyMessage a supplier for the message to be included in the exception if the validation fails.
 * @return the original collection if it is not null or empty.
 * @throws ValidationFailedException if the collection is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that the given collection is not null or empty. If the collection is null or empty,
 * throws a [ValidationFailedException] with the specified parameters.
 *
 * @param property Optional property metadata associated with the collection being validated.
 * @param variableName Optional name of the variable involved in the validation. Included in the
 *                     exception message if provided.
 * @param message Optional custom message for the validation failure. Defaults to "is null or empty"
 *                if not provided.
 * @param causeOf Optional supplier for the primary cause of the validation failure, which is used
 *                to initialize the thrown exception.
 * @param cause Optional supplier for an additional cause to be included in the thrown exception.
 * @return The original collection if it is neither null nor empty.
 * @throws ValidationFailedException If the collection is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is not null or empty.
 * If the collection is null or empty, it throws a [ValidationFailedException].
 *
 * @param property The primary property associated with the validation, or null if not specified.
 * @param variable An optional secondary property providing additional context, or null if not specified.
 * @param message An optional message providing details about the validation failure, or null to use a default message.
 * @param causeOf A supplier for the root exception cause, or null if no root cause is specified.
 * @param cause A supplier for the exception cause, or null if no cause is specified.
 * @return The collection itself if the validation is successful.
 * @throws ValidationFailedException if the collection is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is neither null nor empty. If the validation fails, throws a `ValidationFailedException`.
 *
 * @param callable The Kotlin function (`KFunction`) that is associated with the validation.
 *                 This helps in identifying the context in which the validation is performed. Can be null.
 * @param parameterName The name of the parameter in the function `callable` that is being validated. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Default is "is null or empty".
 * @param causeOf A supplier of the cause for the exception. If provided, it takes precedence over the `cause` parameter. Can be null.
 * @param cause A supplier of the underlying cause for the exception. Used if `causeOf` is not supplied. Can be null.
 * @since 4.2.0
 **/
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is neither null nor empty. If it is null or empty, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] associated with the validation, or null if not applicable.
 * @param parameter the [KParameter] involved in the validation, or null if not applicable.
 * @param message an optional message providing additional context about the validation failure, defaulting to null.
 * @param causeOf an optional supplier for a custom exception cause, defaulting to null.
 * @param cause an optional supplier for the underlying cause of the validation failure, defaulting to null.
 * @return the original collection if the validation passes.
 * @throws ValidationFailedException if the collection is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is not null or empty. If the validation fails, it throws a [ValidationFailedException].
 *
 * @param callableName The name of the callable (e.g., function or method) associated with the validation.
 * @param parameterName The name of the parameter being validated, or `null` if not applicable.
 * @param message An optional custom message providing additional details about the failure. Defaults to "is null or empty".
 * @param causeOf A supplier for the primary exception to be thrown if validation fails, or `null` if not provided.
 * @param cause A supplier for the underlying cause of the validation failure, or `null` if not provided.
 * @return The collection itself if validation passes.
 * @throws ValidationFailedException If the collection is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is not `null` or empty. If the collection is `null` or empty,
 * a `ValidationFailedException` is thrown with additional details about the callable name,
 * parameter, and an optional error message or cause.
 *
 * @param callableName The name of the callable (e.g., function or property) for context in case of validation failure, or `null` if not specified.
 * @param parameter The parameter related to this validation check, represented as a `KParameter` instance, or `null` if not applicable.
 * @param message An optional error message providing context about the validation failure, or `null` for a default message.
 * @param causeOf A supplier (`ThrowableSupplier`) for the cause of the validation failure, or `null` if not specified.
 * @param cause An additional supplier (`ThrowableSupplier`) for a deeper cause of validation failure, or `null` if not specified.
 * @return The original collection if it is not `null` and not empty.
 * @throws ValidationFailedException If the collection is `null` or empty, providing detailed contextual information.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the current collection is empty. If the collection is not empty, an exception is thrown.
 *
 * @param causeOf an optional supplier that provides a custom exception to be thrown if the validation fails.
 * @param cause an optional supplier that provides the underlying cause for the exception.
 * @return the current collection if it is empty.
 * @throws ValidationFailedException if the collection is not empty and no custom exception is provided.
 * @throws Throwable if a custom exception supplied by `causeOf` or its cause is provided.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The collection is not empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The collection is not empty.", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is empty, throwing a [ValidationFailedException] if it is not.
 *
 * @param causeOf an optional supplier for a custom throwable that wraps the [ValidationFailedException].
 *                If null, the exception is not wrapped.
 * @param cause an optional supplier for the underlying cause of the [ValidationFailedException].
 * @param lazyMessage a supplier for the exception message to be used if validation fails.
 * @return the collection if it is empty.
 * @throws ValidationFailedException if the collection is not empty, optionally wrapped by the throwable provided by [causeOf].
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that the collection is empty. If the collection is not empty, a `ValidationFailedException` is thrown.
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Used in the exception message if provided.
 * @param message An optional custom error message to include in the exception. Defaults to "is not empty".
 * @param causeOf A supplier for the primary throwable to throw if validation fails. If null, a default exception is created.
 * @param cause A supplier for the underlying cause of the validation failure. This will be set as the `cause` of the exception.
 * @return The collection itself if it passes validation.
 * @throws ValidationFailedException if the collection is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is empty. If the collection is not empty, a [ValidationFailedException] is thrown.
 *
 * @param property the main [KProperty] associated with the validation failure, or null if not specified
 * @param variable an optional secondary [KProperty] providing additional context, or null if not specified
 * @param message an optional message providing additional details about the validation failure; defaults to "is not empty" if null
 * @param causeOf a supplier providing the root cause of the exception, or null if not specified
 * @param cause a supplier providing the additional cause of the exception, or null if not specified
 * @return the original collection if the validation succeeds
 * @throws ValidationFailedException if the collection is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is empty. If the collection is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) to which this validation is related. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom error message to be included in the exception. If not provided, a default message is used.
 * @param causeOf A supplier for a `Throwable` that represents the cause of the exception. Can be null.
 * @param cause A supplier for an additional `Throwable` to be used as the cause of the exception. Can be null.
 * @return The original collection if it is empty.
 * @throws ValidationFailedException If the collection is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the current collection is empty. Throws a [ValidationFailedException] if the collection is not empty.
 *
 * @param callable an optional [KFunction] associated with the validation context
 * @param parameter an optional [KParameter] representing the parameter being validated
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf an optional supplier for the cause of the exception, if needed
 * @param cause an optional supplier for the root cause of the exception
 * @return the collection itself if validation passes
 * @throws ValidationFailedException if the collection is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is empty. Throws a `ValidationFailedException` if the collection is not empty.
 *
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom message for the validation failure, or null to use the default message.
 * @param causeOf A supplier for the root cause of the exception, or null if no specific cause is provided.
 * @param cause A supplier for the underlying cause of the exception, or null if no cause is specified.
 * @return The collection instance when validation passes.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the given collection is empty.
 *
 * Throws a [ValidationFailedException] if the collection is not empty.
 *
 * @param callableName The name of the callable (e.g., function or property) being validated, or null if not specified.
 * @param parameter The [KParameter] associated with the validation, or null if not applicable.
 * @param message An optional error message providing additional context for the validation failure.
 * @param causeOf A supplier for the root cause of the exception, or null if not provided.
 * @param cause A supplier for the underlying cause of the exception, or null if not provided.
 * @return The original collection if it passes the validation check.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that a collection is either null or empty and throws an exception if it is not.
 *
 * The method checks the calling collection and ensures that it is either null or contains no elements.
 * If the collection is neither null nor empty, a `ValidationFailedException` is thrown with an optional custom cause.
 *
 * @param causeOf A supplier for a throwable that will be used as the main exception cause if the validation fails.
 * @param cause A supplier for an additional throwable that can be passed as the underlying cause
 *              when creating the main `ValidationFailedException`.
 * @return The same collection on which the method was called if it satisfies the null or empty condition.
 * @throws ValidationFailedException If the collection is not null or not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The collection is not null or empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The collection is not null or empty.", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is null or empty. If it's not null or empty, a `ValidationFailedException`
 * is thrown with the specified lazy message and optional causes.
 *
 * @param causeOf A supplier for a specific cause of the validation failure, which can be used to wrap the
 *                exception. If `null`, a default exception will be used.
 * @param cause An additional supplier for the underlying cause of the validation failure, which will be
 *              attached to the exception.
 * @param lazyMessage A supplier for the error message to be used in the exception. The evaluation of this
 *                    supplier is deferred until the exception is created.
 * @return The original collection if the validation passes (i.e., the collection is null or empty).
 * @throws ValidationFailedException If the collection is not null and not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that a collection is null or empty. If it is not, throws a [ValidationFailedException].
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName The name of the variable being validated. Can be null; used in the exception message if provided.
 * @param message A custom message describing the validation failure. Defaults to a generic message if not provided.
 * @param causeOf A supplier for the throwable that will be thrown if validation fails. If null, a [ValidationFailedException] is thrown.
 * @param cause An optional supplier for the underlying cause to be associated with the exception.
 * @return The validated collection if it is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that a collection is either null or empty. If the collection is neither, a `ValidationFailedException`
 * is thrown with the provided details.
 *
 * This validation is useful for ensuring that nullable collections meet specific requirements regarding their
 * nullability and emptiness.
 *
 * @param property The primary `KProperty` associated with the validation, providing metadata about the collection being validated.
 * @param variable An optional secondary `KProperty` that provides additional context for the validation, or null if not specified.
 * @param message An optional error message to include in the exception if validation fails, or null for a default message.
 * @param causeOf A supplier for an alternative throwable to use as the main cause of the exception, or null to omit.
 * @param cause A supplier for an additional throwable to attach as the underlying cause of the validation failure, or null to omit.
 * @return The original collection if it passes validation, allowing fluent-style chaining.
 * @throws ValidationFailedException If the collection is neither null nor empty, with details about the failure included.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is either null or empty. If the validation fails, a `ValidationFailedException`
 * is thrown based on the provided arguments.
 *
 * @param callable The Kotlin function (`KFunction`) to which this validation is linked. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message for the validation failure. Defaults to "is not null or empty".
 * @param causeOf An optional supplier for a custom `Throwable` to be thrown if validation fails. Can be null.
 * @param cause An optional supplier for the cause of the validation failure. Can be null.
 * @return The original collection if it is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is either null or empty.
 *
 * If the collection is not null and not empty, a `ValidationFailedException` is thrown with the provided parameters.
 *
 * @param callable The [KFunction] associated with the validation, or null if not applicable.
 * @param parameter The [KParameter] associated with the validation, or null if not applicable.
 * @param message An optional custom message providing additional context about the validation failure. Defaults to null.
 * @param causeOf A supplier for a throwable that is the cause of the validation failure. Defaults to null.
 * @param cause A supplier for a throwable used as the underlying cause of the exception. Defaults to null.
 * @return The validated collection if it is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is null or empty.
 *
 * This method checks the collection and throws a `ValidationFailedException` if the collection
 * is neither null nor empty. It supports customizable exception details, including the related
 * callable name, parameter name, custom message, and potential causes.
 *
 * @param callableName The name of the callable (e.g., function or method) where the validation is performed.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom message describing the validation failure.
 * @param causeOf A supplier providing a throwable to be used as the primary cause of the failure, or null.
 * @param cause A supplier providing a throwable to be linked as the underlying cause, or null.
 * @return The collection itself if the validation passes, enabling method chaining.
 * @throws ValidationFailedException If the collection is not null or not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the collection is null or empty, throwing a `ValidationFailedException` if it is not.
 *
 * This method is utilized to ensure that a given collection either has no elements or is null.
 * If the collection is neither null nor empty, it throws a `ValidationFailedException`.
 * Optionally, additional context such as a custom message or a cause may be provided to the exception.
 *
 * @param callableName The name of the callable (e.g., function or property) where the validation is performed. Can be null.
 * @param parameter The `KParameter` associated with the validation, if applicable. Can be null.
 * @param message An optional custom error message to include in the exception. If null, a default message will be used.
 * @param causeOf A `ThrowableSupplier` providing the main cause for the exception. If null, the default exception generation is used.
 * @param cause A `ThrowableSupplier` providing an additional cause for the exception. Can be null.
 * @return The validated collection if it is null or empty, allowing it to be further used in a fluent style.
 * @throws ValidationFailedException If the collection is neither null nor empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke()))
    return this
}