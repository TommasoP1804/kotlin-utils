/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("ArrayUtilsKt")
@file:Since("1.0.0")
@file:Suppress("unused", "kutils_null_check", "kutils_map_declaration", "kutils_collection_declaration",
    "kutils_sublist_as_int_invoke", "RedundantSuppression", "deprecation", "kutils_take_as_int_invoke",
    "kutils_drop_as_int_invoke"
)
@file:OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
@file:MustUseReturnValues

package dev.tommasop1804.kutils

import Break
import Continue
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.constants.*
import dev.tommasop1804.kutils.classes.numbers.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

/**
 * Negates the state of the array by returning `true` if the array is either `null` or empty.
 *
 * This operator checks if the array reference is `null` or if it contains no elements.
 *
 * @return `true` if the array is `null` or empty, otherwise `false`.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
operator fun Array<*>?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNull() || isEmpty()
}

/**
 * Extension property to check if the array contains duplicate elements.
 *
 * This property evaluates whether the array has repeated elements by comparing
 * the size of the array with the size of its distinct elements. If the sizes
 * differ, it indicates that the array contains duplicates.
 *
 * @receiver The array to evaluate for duplicate elements.
 * @return `true` if the array contains duplicate elements, `false` otherwise.
 * @since 2.1.0
 */
val <E> Array<E>.containsDuplicates
    get() = distinct().size != size

/**
 * Extension property for arrays that checks if the array contains exactly one element.
 *
 * This property evaluates to `true` if the array's size is equal to 1, and `false` otherwise.
 *
 * @receiver The array to check.
 * @return `true` if the array has exactly one element, `false` otherwise.
 * @since 2.1.0
 */
val Array<*>.isSingleElement get() = size == 1
/**
 * Extension property for arrays that checks whether the array does not contain
 * exactly one element.
 *
 * @return `true` if the array contains zero elements or more than one element, 
 *         `false` if the array contains exactly one element.
 * @since 2.1.0
 */
val Array<*>.isNotSingleElement get() = size != 1

/**
 * Merges the current array with one or more additional collections into a new array of the same type.
 * If the current array is null or empty, the resulting array will contain elements from the provided collections.
 *
 * @receiver the array of elements to merge; nullable.
 * @param collections additional collections of elements to merge with the current array.
 * @return a new array of the same type containing merged elements.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified E> Array<E>?.merge(vararg collections: Collection<E>) = orEmpty().toList().merge(*collections).toTypedArray()

/**
 * Determines if two arrays share at least one common element.
 *
 * This method checks whether the invoking array and the specified array have any overlapping elements.
 * It uses set intersection to perform the comparison.
 *
 * @param other The array to compare with the calling array.
 * @return `true` if at least one element is common between the arrays, `false` otherwise.
 * @since 2.1.0
 */
infix fun <E> Array<E>.intersects(other: Array<E>) = (this intersect other.toSet()).isNotEmpty()

/**
 * Inserts the specified separator element between each element of the array, 
 * and returns a new array containing the original elements along with the separators.
 *
 * @param separator The element to be inserted between each pair of elements in the array.
 * @return A new array with the separator inserted between each element of the original array.
 * @since 2.1.0
 */
inline infix fun <reified E> Array<E>.intersperseWith(separator: E): Array<E> =
    flatMapIndexed { index, item ->
        if (index == lastIndex) listOf(item)
        else listOf(item, separator)
    }.toTypedArray()

/**
 * Checks if the array contains any of the specified elements.
 *
 * @param elements Vararg parameter representing the elements to check for in the array.
 * @return `true` if at least one of the specified elements is found in the array, `false` otherwise.
 * @since 2.1.0
 */
fun <E> Array<E>.containsAny(vararg elements: E) = any { it in elements }
/**
 * Checks if none of the elements in the array are contained in the given set of elements.
 *
 * @param elements The elements to check against the array.
 * @return `true` if none of the elements in the array are present in the given set of elements, `false` otherwise.
 * @since 2.1.0
 */
fun <E> Array<E>.containsNone(vararg elements: E) = none { it in elements }
/**
 * Checks if there is at least one element in the array that satisfies the given predicate.
 *
 * @param predicate A condition to test each element of the array.
 * @return `true` if any element matches the predicate, otherwise `false`.
 * @since 2.1.0
 */
operator fun <E> Array<E>.contains(predicate: Predicate<E>) = any { predicate(it) }

/**
 * Returns the first element of the array if it exists, or throws an exception provided by the given supplier.
 *
 * @param lazyException A supplier that provides the exception to be thrown if the array is empty.
 * @return The first element of the array.
 * @throws Throwable The exception provided by the supplier if the array is empty.
 * @since 2.1.0
 */
@IgnorableReturnValue
infix fun <E> Array<E>.firstOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return firstOrNull() ?: throw lazyException()
}
/**
 * Returns the first element in the array that matches the given [predicate]. 
 * If no such element is found, throws an exception provided by the [lazyException] supplier.
 *
 * @param lazyException A supplier that provides the exception to be thrown if no element matches the predicate.
 * @param predicate A condition that determines whether an element matches.
 * @return The first element that matches the [predicate].
 * @since 2.1.0
 */
@IgnorableReturnValue
fun <E> Array<E>.firstOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return firstOrNull(predicate) ?: throw lazyException()
}
/**
 * Returns the first element of the array if it exists, or the provided default value
 * if the array is empty.
 *
 * This is an infix function that allows clean and readable syntax when used.
 *
 * @param default A supplier function that provides the default value to return
 *                if the array is empty.
 * @return The first element of the array or the value provided by the `default` supplier.
 * @since 2.1.0
 */
infix fun <E> Array<E>.firstOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return firstOrNull() ?: default()
}
/**
 * Returns the first element of the array that matches the given [predicate].
 * If no such element is found, returns the result of the [default] supplier.
 *
 * @param default A supplier that provides a default value when no element satisfies the [predicate].
 * @param predicate A predicate used to test elements of the array.
 * @since 2.1.0
 */
fun <E> Array<E>.firstOr(default: Supplier<E>, predicate: Predicate<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return firstOrNull(predicate) ?: default()
}

/**
 * Returns the second element of the array.
 *
 * This function retrieves the second element from the array it is called on.
 * If the array has fewer than two elements, a `NoSuchElementException` is thrown.
 *
 * @throws NoSuchElementException if the size of the array is less than 2.
 * @return the second element of the array.
 * @since 2.1.0
 */
fun <E> Array<E>.second() = if (size < 2) throw NoSuchElementException("List size $size doesn't allow to get second element.") else this[1]
/**
 * Returns the second element of the array that matches the given [predicate].
 * If no such element exists or there are fewer than two matching elements, this function typically throws an exception.
 *
 * @param predicate A condition applied to each element to filter the array.
 * @return The second element that satisfies the [predicate].
 * @throws NoSuchElementException if fewer than two elements match the [predicate].
 * @since 2.1.0
 */
fun <E> Array<E>.second(predicate: Predicate<E>) = filter(predicate).second()
/**
 * Returns the second element of the array, or `null` if the array size is less than 2.
 *
 * @receiver The array from which to retrieve the second element.
 * @return The second element of the array, or `null` if the array has fewer than two elements.
 * @since 2.1.0
 */
fun <E> Array<E>.secondOrNull() = if (size < 2) null else this[1]
/**
 * Returns the second element of the array that matches the given predicate, or `null`
 * if no such element exists or the filtered result contains fewer than two elements.
 *
 * This function filters the array based on the specified predicate and attempts to 
 * retrieve the second element from the filtered results. It safely returns `null` 
 * if the filtered result does not have enough elements, avoiding potential exceptions.
 *
 * @param predicate A condition used to filter the elements of the array.
 * @return The second element that matches the predicate, or `null` if fewer than 
 * two elements match the predicate.
 * @since 2.1.0
 */
fun <E> Array<E>.secondOrNull(predicate: Predicate<E>) = filter(predicate).secondOrNull()
/**
 * Returns the second element of the array if it exists, otherwise throws an exception provided by the given supplier.
 *
 * @param lazyException A supplier that provides the exception to be thrown if the array has fewer than two elements.
 * @throws Throwable If the array contains fewer than two elements.
 * @return The second element of the array.
 * @since 2.1.0
 */
@IgnorableReturnValue
fun <E> Array<E>.secondOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return if (size < 2) throw lazyException() else this[1]
}
/**
 * Returns the second element of the array that matches the given predicate, or throws the exception
 * provided by the given `lazyException` supplier if fewer than two elements match.
 *
 * @param lazyException A supplier function that produces the exception to be thrown if fewer than two elements match the predicate.
 * @param predicate A predicate used to filter elements in the array before retrieving the second matching element.
 * @since 2.1.0
 */
@IgnorableReturnValue
fun <E> Array<E>.secondOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>) = filter(predicate).secondOrThrow(lazyException)
/**
 * Returns the second element of the array if it exists; otherwise, returns the value provided
 * by the given default supplier.
 *
 * @param E the type of elements in the array
 * @param default a supplier function that provides a default value if the array has less than two elements
 * @return the second element of the array or the default value provided by the supplier
 * @since 2.1.0
 */
fun <E> Array<E>.secondOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return if (size < 2) default() else this[1]
}
/**
 * Returns the second element of the array that matches the given predicate, if such an element exists.
 * If no such element exists, the value provided by the given default supplier is returned.
 *
 * @param default A supplier function that provides a default value when the array does not contain at least two elements
 *                matching the predicate.
 * @param predicate A predicate function used to filter the elements of the array.
 * @return The second element of the filtered array that matches the predicate, or the result of invoking the default 
 *         supplier if the filtered array has less than two matching elements.
 * @since 2.1.0
 */
fun <E> Array<E>.secondOr(default: Supplier<E>, predicate: Predicate<E>) = filter(predicate).secondOr(default)

/**
 * Returns the third element of the array.
 *
 * Throws a [NoSuchElementException] if the array size is less than three.
 *
 * @receiver The array instance from which the third element is to be retrieved.
 * @return The third element in the array.
 * @throws NoSuchElementException If the size of the array is less than three.
 * @since 2.1.0
 */
fun <E> Array<E>.third() = if (size < 3) throw NoSuchElementException("List size $size doesn't allow to get third element.") else this[2]
/**
 * Returns the third element from the array that matches the given predicate.
 *
 * This method filters the array using the provided predicate and then retrieves the third element
 * from the resulting filtered collection. If the array does not contain at least three elements
 * matching the predicate, an exception may be thrown.
 *
 * @param E the type of elements in the array.
 * @param predicate the predicate used to filter elements in the array.
 * @return the third element that matches the predicate after filtering.
 * @throws NoSuchElementException if there are fewer than three matching elements.
 * @since 2.1.0
 */
fun <E> Array<E>.third(predicate: Predicate<E>) = filter(predicate).third()
/**
 * Returns the third element of the array if the array contains at least three elements,
 * or `null` if the array has fewer than three elements.
 *
 * This function provides a safe way to access the third element of an array
 * without causing an `IndexOutOfBoundsException`.
 *
 * @receiver The array to retrieve the third element from.
 * @return The third element of the array, or `null` if the array size is less than three.
 * @since 2.1.0
 */
fun <E> Array<E>.thirdOrNull() = if (size < 3) null else this[2]
/**
 * Returns the third element in the array that matches the specified [predicate], or `null` if no such element exists.
 *
 * This function filters the elements of the array based on the provided [predicate], 
 * then retrieves the third element from the filtered results using a safe access method.
 * If the filtered list has fewer than three elements, it will return `null`.
 *
 * @param predicate A predicate to filter elements of the array.
 * @return The third element that matches the [predicate] or `null` if there are fewer than three matches.
 * @since 2.1.0
 */
fun <E> Array<E>.thirdOrNull(predicate: Predicate<E>) = filter(predicate).thirdOrNull()
/**
 * Returns the third element of the array if it exists; otherwise, throws an exception
 * provided by the supplied `lazyException`.
 *
 * @param lazyException A supplier that generates the exception to be thrown if the array 
 * does not contain at least three elements.
 * @return The third element of the array.
 * @throws Throwable The exception provided by `lazyException` if the array size is less 
 * than three.
 * @since 2.1.0
 */
@IgnorableReturnValue
fun <E> Array<E>.thirdOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return if (size < 3) throw lazyException() else this[2]
}
/**
 * Returns the third element of the array that matches the given predicate, or throws the exception
 * provided by the given `lazyException` supplier if there are fewer than three matching elements.
 *
 * @param lazyException A supplier function that produces the exception to be thrown if the array has fewer than three matching elements.
 * @param predicate A predicate used to filter the elements of the array.
 * @since 2.1.0
 */
@IgnorableReturnValue
fun <E> Array<E>.thirdOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>) = filter(predicate).thirdOrThrow(lazyException)

/**
 * Returns the third element of the array if it exists, otherwise returns the value
 * supplied by the given default supplier.
 *
 * @param default A supplier function that provides a default value to return
 * if the array has fewer than three elements.
 * @return The third element of the array or the value returned by the default supplier.
 * @since 2.1.0
 */
fun <E> Array<E>.thirdOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return if (size < 3) default() else this[2]
}
/**
 * Returns the third element of the array that satisfies the given predicate if it exists; 
 * otherwise, returns the value supplied by the given default supplier.
 *
 * @param default A supplier function that provides a default value when the filtered elements do not 
 *        contain at least three elements.
 * @param predicate A predicate used to filter the elements of the array.
 * @return The third element of the filtered array, or the result of invoking the default supplier 
 *         if the filtered array has less than three elements.
 * @since 2.1.0
 */
fun <E> Array<E>.thirdOr(default: Supplier<E>, predicate: Predicate<E>) = filter(predicate).thirdOr(default)

/**
 * Splits the array into chunks based on a predicate condition. Each chunk will end
 * immediately before an element that satisfies the given predicate, and a new chunk
 * will start after that element.
 *
 * @param E the type of elements contained in the array.
 * @param predicate a function that evaluates each element to determine chunk boundaries.
 * @return a list of lists, where each sublist represents a chunk of the original array 
 *         split according to the predicate.
 * @since 2.1.0
 */
infix fun <E> Array<E>.chunkedWhile(predicate: Predicate<E>): List<List<E>> = toList().run {
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
 * Creates a new list where each element in the original array is repeated a specified number of times.
 *
 * @param n The number of times each element in the array should be repeated. Must be a non-negative integer.
 * @return A list containing the elements of the original array repeated the specified number of times.
 * @since 2.1.0
 */
infix fun <E> Array<E>.repeatEach(n: Int): List<E> {
    val resultList = mutableListOf<E>()
    forEach {
        { resultList += it } * n
    }

    return resultList
}

/**
 * Finds the mode (most frequently occurring element) in the array. 
 * If there are multiple elements with the same frequency, 
 * the first one encountered is returned.
 *
 * @return The mode of the array, or `null` if the array is empty.
 * @since 2.1.0
 */
fun <E> Array<E>.mode(): E? = groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

/**
 * Returns the single element in the array if the array contains exactly one element.
 * 
 * If the array is empty, a [NoSuchElementException] is thrown. If the array contains
 * more than one element, a [TooManyElementsException] is thrown, including the 
 * current size of the array as part of the exception details.
 * 
 * @receiver The array to process.
 * @return The only element in the array.
 * @throws NoSuchElementException if the array is empty.
 * @throws TooManyElementsException if the array contains more than one element.
 * @since 2.1.0
 */
fun <E> Array<E>.onlyElement() = toList().run {
    if (isEmpty()) throw NoSuchElementException()
    else if (size == 1) first() else throw TooManyElementsException(size)
}
/**
 * Returns the single element of the array if it contains exactly one element, or `null` if the array
 * is empty or contains more than one element.
 *
 * @receiver The array to evaluate.
 * @return The single element of the array, or `null` if the array is empty or has more than one element.
 * 
 * @since 2.1.0
 */
fun <E> Array<E>.onlyElementOrNull() = toList().run { if (size == 1) first() else null }
/**
 * Returns the single element of the array if it contains exactly one element, or the value
 * provided by the supplied default function otherwise.
 *
 * @param default A supplier function to provide a default value if the array does not contain exactly one element.
 * @since 2.1.0
 */
infix fun <E> Array<E>.onlyElementOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return toList().run { if (size == 1) first() else default() }
}
/**
 * Returns the single element in the array if the array contains exactly one element.
 * Otherwise, throws the exception provided by the given supplier.
 *
 * @param lazyException A supplier function that provides the exception to be thrown 
 * if the array does not contain exactly one element.
 * @throws Throwable If the array does not contain exactly one element.
 * @since 2.1.0
 */
@IgnorableReturnValue
infix fun <E> Array<E>.onlyElementOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return toList().run { if (size == 1) first() else throw lazyException() }
}
/**
 * Returns the single element in the array that matches the given predicate.
 * Throws an exception if no elements match the predicate or if more than one element matches.
 *
 * @param predicate A condition that each element in the array will be tested against.
 * @return The single element that matches the predicate.
 * @throws NoSuchElementException If no elements match the predicate.
 * @throws TooManyResultsException If more than one element matches the predicate.
 * @throws TooFewResultsException If the number of matching elements is fewer than expected.
 * @since 2.1.0
 */
infix fun <E> Array<E>.onlyElement(predicate: Predicate<E>) = toList()
    .requireOrThrow({ NoSuchElementException() }, { it.isNotEmpty() })
    .filter(predicate).run {
        if (size == 1) first()
        else throw if (size > 1) TooManyResultsException(size) else TooFewResultsException(size)
    }
/**
 * Returns the only element in the array that matches the given [predicate], or `null` if no such element
 * exists or if more than one element matches the [predicate].
 *
 * @param predicate the condition used to filter elements in the array.
 * @since 2.1.0
 */
infix fun <E> Array<E>.onlyElementOrNull(predicate: Predicate<E>) = filter(predicate).run { if (size == 1) first() else null }
/**
 * Returns the single element of the array that matches the given predicate, or the value provided
 * by the default supplier if no such element exists or if more than one element matches the predicate.
 *
 * @param default The supplier that provides a default value if there isn't exactly one matching element.
 * @param predicate The condition to evaluate each element of the array.
 * @return The single matching element or the value provided by the default supplier.
 * @since 2.1.0
 */
fun <E> Array<E>.onlyElementOr(default: Supplier<E>, predicate: Predicate<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return filter(predicate).run { if (size == 1) first() else default() }
}
/**
 * Returns the only element of the array that matches the given predicate or throws the exception provided
 * by the given `lazyException` supplier if the matching element count is not exactly one.
 *
 * @param lazyException A supplier function that provides the exception to be thrown if the condition is not met.
 * @param predicate A predicate used to filter elements in the array.
 * @throws Throwable When the array does not contain exactly one element that matches the predicate.
 * @since 2.1.0
 */
@IgnorableReturnValue
fun <E> Array<E>.onlyElementOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return filter(predicate).run { if (size == 1) first() else throw lazyException() }
}

/**
 * Checks if the array is either null or empty.
 *
 * This function determines whether the array contains no elements or is null.
 *
 * @return `true` if the array is null or has no elements, `false` otherwise.
 * @since 2.1.0
 */
@OptIn(ExperimentalContracts::class)
@Suppress("kutils_null_check")
fun <E> Array<E>?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return isNotNull() && isNotEmpty()
}

/**
 * Checks if the array is not null and not empty.
 *
 * This function ensures the array is both non-null and contains at least one element.
 *
 * @return true if the array is not null and contains at least one element, false otherwise.
 * @since 2.1.0
 */
@OptIn(ExperimentalContracts::class)
@Suppress("kutils_null_check")
fun <E> Array<E>?.isNotNullOrEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return isNotNull() && isNotEmpty()
}

/**
 * Returns the original array if it is not null or empty. Otherwise, invokes the provided
 * `defaultValue` supplier and returns its result.
 *
 * @param defaultValue A supplier function that provides a default array when the original array is null or empty.
 * @return The original array if it is not null or empty, otherwise the result of invoking `defaultValue`.
 * @since 2.1.0
 */
@OptIn(ExperimentalContracts::class)
inline fun <E> Array<E>?.ifNullOrEmpty(defaultValue: Supplier<Array<E>>): Array<E> {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
    }
    return if (isNullOrEmpty()) defaultValue() else this
}

/**
 * Executes the given action if the array is not empty. Returns the result of the action
 * if the array is non-empty; otherwise, returns the array itself.
 *
 * @param action A function to be invoked with the array as the receiver if it is not empty.
 * @return The result of the action if the array is not empty, or the array itself if it is empty.
 * @since 3.1.3
 */
@OptIn(ExperimentalContracts::class)
@Suppress("UNCHECKED_CAST")
@IgnorableReturnValue
inline fun <E, R> Array<E>.ifNotEmpty(action: ReceiverTransformer<Array<E>, R>): R {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    return (if (isNotEmpty()) action(this) else this) as R
}

/**
 * Executes the specified [action] if the array is not null and not empty.
 *
 * This function checks whether the array fulfills the condition of being non-null and having at least one element.
 * If the condition is met, the given [action] is invoked with the array as its receiver.
 * Otherwise, the function returns the array as is.
 *
 * @param action The lambda or function to execute when the array is not null and not empty.
 * @return The result of the [action] if executed, or the original array cast to the expected type if not.
 * @since 3.1.3
 */
@OptIn(ExperimentalExtendedContracts::class, ExperimentalContracts::class)
@Suppress("UNCHECKED_CAST")
@IgnorableReturnValue
inline fun <E, R> Array<E>?.ifNotNullOrEmpty(action: ReceiverTransformer<Array<E>, R>): R? {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        (this@ifNotNullOrEmpty != null) implies returnsNotNull()
    }
    return (if (isNotNullOrEmpty()) action(this) else this) as R
}

/**
 * Filters the elements of the array by selecting only those at indices that are multiples of the given step.
 *
 * @param step The interval at which elements are selected from the array. Must be a positive integer.
 * @return A list containing elements at indices divisible by the specified step.
 * @since 2.1.0
 */
infix fun <E> Array<E>.step(step: Int) =  filterIndexed { index, c -> index % step == 0 }

/**
 * Repeats the elements of the array a specified number of times and returns a new array.
 *
 * @receiver The array whose elements will be repeated.
 * @param n The number of times the elements of the array should be repeated.
 * @return An array containing the elements of the array repeated n times.
 * @since 2.1.0
 */
inline operator fun <reified E> Array<E>.times(n: Int) = (1..n).flatMap { toList() }.toTypedArray()

/**
 * Splits the array into two collections based on a given predicate.
 *
 * This operator function divides the elements in the array into two groups:
 * one containing elements that satisfy the given predicate and the other
 * containing elements that do not.
 *
 * @param predicate A condition used to evaluate each element in the array.
 * @since 2.1.0
 */
operator fun <E> Array<E>.div(predicate: Predicate<E>) = partition(predicate)

/**
 * Splits the array into a list of smaller lists (chunks), each of the specified size.
 *
 * @param chunkSize the size of each chunk into which the array will be divided
 * @return a MultiList containing the chunked lists
 * @since 2.1.0
 */
operator fun <E> Array<E>.rem(chunkSize: Int): MultiList<E> = toList().chunked(chunkSize)
/**
 * Divides the elements of the array into a list of sublists based on the provided predicate.
 * Each sublist contains consecutive elements until the predicate returns false.
 *
 * @param predicate A condition used to split the array into multiple sublists.
 * @return A MultiList containing all sublists, where each sublist satisfies the chunked condition.
 * @since 2.1.0
 */
operator fun <E> Array<E>.rem(predicate: Predicate<E>): MultiList<E> = chunkedWhile(predicate)

/**
 * Decrements the size of the array by removing the last element and returns a new array containing the remaining elements.
 *
 * @return A new array of the same type containing all elements of the original array except the last one.
 * @since 2.1.0
 */
inline operator fun <reified E> Array<E>.dec(): Array<E> = toList().subList(0, lastIndex).toTypedArray()
/**
 * Creates a new array containing a range of elements from the start of the array up to and including the specified end index.
 *
 * @param endIndex The inclusive end index of the range to be included in the new array. Must be within the valid bounds of the array.
 * @return A new array containing the specified range of elements from the original array.
 * @since 2.1.0
 */
inline operator fun <reified E> Array<E>.rangeTo(endIndex: Int) = toList().subList(0, endIndex + 1).toTypedArray()

/**
 * Returns a new array containing elements from the original array based on circular indexing and a specified step value.
 * The range of indices is determined by [circularStartIndex] and [circularEndIndex], and the iteration wraps around
 * circularly beyond the array bounds if necessary.
 *
 * @param circularStartIndex The starting index for the circular iteration. Must be non-negative.
 * @param circularEndIndex The ending index for the circular iteration. Can exceed the array length indicating a wrapped boundary.
 * @param step The interval between indices during iteration. Defaults to 1 and must be greater than zero.
 * @return A new array containing the elements selected from the circular iteration.
 * @throws IllegalArgumentException If [step] is not greater than zero.
 * @throws NoSuchElementException If the array is empty.
 * @since 2.1.0
 */
inline operator fun <reified E> Array<E>.invoke(circularStartIndex: Int, circularEndIndex: Int, step: Int = 1): Array<E> {
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
    return result.toTypedArray()
}
/**
 * Enables invoking an array with a circular range to access elements in the specified step order.
 * This operator function allows the array to be accessed using circular indexing within the defined range.
 * 
 * @param circularRange The range of indices to iterate over. Indices can wrap around circularly within the array bounds.
 * @param step The step value for iteration. Defaults to 1 if not specified.
 * @since 2.1.0
 */
inline operator fun <reified E> Array<E>.invoke(circularRange: IntRange, step: Int = 1) = 
    invoke(circularRange.first, circularRange.last + 1, step)
/**
 * Invokes each index in the given circular progression on the array, producing a new array
 * with the elements at those indices. The progression can wrap around if indices exceed the array bounds.
 *
 * @param circularProgression The progression of indices to invoke on the array. Can wrap around if indices exceed bounds.
 * @return A new array containing elements at the indices defined by the circular progression.
 * @throws IndexOutOfBoundsException If the array is empty.
 * @since 2.1.0
 */
inline operator fun <reified E> Array<E>.invoke(circularProgression: IntProgression): Array<E> {
    if (isEmpty()) throw IndexOutOfBoundsException("List cannot be null.")

    val result = mutableListOf<E>()
    for (i in circularProgression) result.add(invoke(i))
    return result.toTypedArray()
}
/**
 * Provides a way to access elements of an array using a circular indexing mechanism.
 * If the provided index exceeds the array bounds, the index wraps around based on the array's size.
 *
 * @param circularIndex The index to access, which may exceed the array bounds and will wrap around circularly.
 * @return The element at the resolved circular index in the array.
 * @throws IndexOutOfBoundsException if the array is empty.
 * @since 2.1.0
 */
inline operator fun <reified E> Array<E>.invoke(circularIndex: Int): E {
    if (isEmpty()) throw IndexOutOfBoundsException("List cannot be null.")
    val actualIndex = circularIndex.mod(size)
    return this[actualIndex]
}

/**
 * Filters the elements of the array based on a provided predicate.
 * This function returns a new array containing only the elements that satisfy the given predicate.
 *
 * @param filter The predicate to apply to each element of the array.
 * @return A new array containing the elements that satisfy the predicate.
 * @since 2.1.0
 */
inline operator fun <reified E> Array<E>.invoke(filter: Predicate<E>): Array<E> {
    val destination = ArrayList<E>()
    for (element in this) if (filter(element)) destination.add(element)
    return destination.toTypedArray()
}

/**
 * Returns the first element of the array that satisfies the given predicate.
 *
 * @param find A predicate function that evaluates each element to determine
 *             if it matches the desired condition.
 * @return The first element that satisfies the predicate, or `null` if no such element is found.
 * @since 2.1.0
 */
inline operator fun <E> Array<E>.get(find: Predicate<E>): E? {
    for (element in this) if (find(element)) return element
    return null
}
/**
 * Finds an element in the array that matches the given predicate. 
 * If no matching element is found, the provided exception is thrown lazily.
 *
 * @param find The predicate used to test each element for a match.
 * @param lazyException A supplier that provides the exception to be thrown if no element matches.
 * @return The element that matches the predicate.
 * @throws Throwable The exception provided by `lazyException` if no matching element is found.
 * @since 2.1.0
 */
operator fun <E> Array<E>.get(find: Predicate<E>, lazyException: ThrowableSupplier) =
    find(find) ?: throw lazyException()

/**
 * Searches for an element in the array that matches the given predicate. 
 * If no such element is found, throws an exception provided by the lazyException supplier.
 *
 * @param lazyException A supplier that provides the exception to be thrown if no matching element is found.
 *                       Defaults to throwing a NoSuchElementException with a default message.
 * @param find A predicate used to evaluate each element in the array.
 * @since 2.1.0
 */
fun <E> Array<E>.findOrThrow(lazyException: ThrowableSupplier = { NoSuchElementException("No element found") }, find: Predicate<E>): E & Any {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return find(find) ?: throw lazyException()
}

/**
 * Returns a new array containing elements of the original array that do not match
 * the given predicate.
 *
 * @param filterNot A predicate used to filter out elements from the array.
 *                  Elements that satisfy this predicate will be excluded from the result.
 * @return A new array without the elements that match the given predicate.
 * @since 3.5.2
 */
operator fun <E> Array<E>.minus(filterNot: Predicate<E>) = filterNot(filterNot)

/**
 * Retrieves a subarray from the current array based on the specified range.
 *
 * @param range The range of indices to extract from the array. The range should be defined as an `IntProgression`.
 * @return A new array containing the elements within the specified range.
 * @since 2.1.0
 */
@Suppress("deprecation")
inline operator fun <reified E> Array<E>.get(range: IntProgression) = toList().subList(range).toTypedArray()

/**
 * Retrieves the element at the specified [index] from the array. If the element at the given index
 * does not exist, the [lazyException] supplier is invoked to provide a throwable that is then thrown.
 *
 * @param index The position of the element to retrieve from the array.
 * @param lazyException A lambda that supplies the throwable to be thrown if the index is not valid.
 *                       Defaults to a supplier of NoSuchElementException.
 * @return The element at the specified [index].
 * @throws Throwable The exception supplied by [lazyException] if the index is out of bounds.
 * @since 2.1.0
 */
operator fun <E> Array<E>.get(index: Int, lazyException: ThrowableSupplier = { NoSuchElementException("Index $index not present") }): E =
    getOrNull(index) ?: throw lazyException()
/**
 * Returns the element at the position corresponding to the given percentage
 * within the array. The percentage is calculated relative to the size of the array.
 *
 * @param percentage The percentage of the array where the element should be retrieved.
 *          Must be between 0 and 100 inclusive.
 * @return The element at the specified percentage-based position in the array.
 * @throws IndexOutOfBoundsException If the array is empty.
 * @throws IllegalArgumentException If the percentage is not in the valid range [0, 100].
 * @since 4.6.1
 */
operator fun <E> Array<E>.get(percentage: Percentage) = percent(percentage)

/**
 * Sorts the array based on the specified sorting direction.
 *
 * This infix function is used to sort the elements of the array in either
 * ascending or descending order as determined by the provided `direction` parameter.
 * The sorting is performed using the natural ordering of the elements.
 *
 * @param direction The `SortDirection` specifying whether the sorting should be
 * performed in ascending or descending order.
 * 
 * @since 2.1.0
 */
infix fun <E: Comparable<E>> Array<E>.sorted(direction: SortDirection) = when (direction) {
    SortDirection.Ascending -> sorted()
    SortDirection.Descending -> sortedDescending()
}

/**
 * Sorts the elements of an array based on a specified direction and a selector function.
 *
 * The direction determines whether the sorting is in ascending or descending order, while the selector
 * function is used to extract the comparable value from each element for sorting.
 *
 * @param direction The direction to sort the array in, either [SortDirection.Ascending] or [SortDirection.Descending].
 * @param selector A lambda function that transforms each element of the array into a value of type [R?],
 * which is used to determine the sort order.
 * @since 2.1.0
 */
inline fun <E, R : Comparable<R>> Array<E>.sortedBy(direction: SortDirection, crossinline selector: Transformer<E, R?>) = when (direction) {
    SortDirection.Ascending -> sortedBy(selector)
    SortDirection.Descending -> sortedByDescending(selector)
}

/**
 * Returns a subarray consisting of elements before the specified element in the array.
 * If the specified element is not found in the array, an empty array is returned.
 *
 * @param element The element to find in the array. The returned subarray will contain
 * all elements that are positioned before this element in the array.
 * @return A subarray of elements before the specified element or an empty array if
 * the element is not found.
 * @since 2.1.0
 */
inline infix fun <reified E> Array<E>.before(element: E) = if (contains(element)) get(0..<indexOf(element)) else emptyArray<E>()
/**
 * Returns a subarray from the beginning of the array up to and including the specified element.
 * If the element is not found in the array, an empty array is returned.
 *
 * @param element The element up to which the subarray should be extracted, inclusive.
 * @return A new array containing all elements from the beginning of the array up to and including the specified element,
 * or an empty array if the element is not found.
 * @since 2.1.0
 */
inline infix fun <reified E> Array<E>.beforeIncluding(element: E) = if (contains(element)) get(0..indexOf(element)) else emptyArray<E>()
/**
 * Returns a new array containing all elements before the last occurrence of the specified element in the array.
 * If the specified element is not found, an empty array is returned.
 *
 * @param element The element whose last occurrence determines the cutoff point in the array.
 * @return A new array containing elements before the last occurrence of the specified element, or an empty array if the element is not found.
 * @since 2.1.0
 */
inline infix fun <reified E> Array<E>.beforeLast(element: E) = if (contains(element)) get(0..<lastIndexOf(element)) else emptyArray<E>()
/**
 * Retrieves a subarray from the current array, starting from the first element and up to and including
 * the last occurrence of the specified element. If the element is not present in the array, returns an empty array.
 *
 * @param element The element up to which the subarray should be extracted, including the element itself.
 * @return An array containing elements from the start up to and including the last occurrence of the provided element,
 *         or an empty array if the element is not found.
 * @since 2.1.0
 */
inline infix fun <reified E> Array<E>.beforeLastIncluding(element: E) = if (contains(element)) get(0..lastIndexOf(element)) else emptyArray<E>()
/**
 * Returns a new array containing the elements that appear after the specified element in the original array.
 * If the given element is not found in the array, an empty array is returned.
 *
 * @param E the type of elements in the array.
 * @param element the element after which the sub-array should be returned.
 * @return a new array containing elements following the given element, or an empty array if the element is not found.
 * @since 2.1.0
 */
inline infix fun <reified E> Array<E>.after(element: E) = if (contains(element)) get(indexOf(element) + 1..<size) else emptyArray<E>()
/**
 * Returns a new array containing the elements starting from the specified [element], inclusive, 
 * and up to the end of the array. If the [element] is not found in the array, an empty array is returned.
 *
 * @param element The element from which the resulting array should start, including this element.
 * @since 2.1.0
 */
inline infix fun <reified E> Array<E>.afterIncluding(element: E) = if (contains(element)) get(indexOf(element)..size) else emptyArray<E>()
/**
 * Returns a sub-array containing elements that appear after the last occurrence 
 * of the specified element in the current array. If the element does not exist 
 * in the array, an empty array is returned.
 *
 * @param element The element whose last occurrence in the array determines the starting point of the sub-array.
 * @since 2.1.0
 */
inline infix fun <reified E> Array<E>.afterLast(element: E) = if (contains(element)) get(lastIndexOf(element) + 1..<size) else emptyArray<E>()
/**
 * Returns a new array containing all elements from the specified element's last occurrence (inclusive) 
 * to the end of the original array. If the specified element is not found, returns an empty array.
 *
 * @param element The element in the array from which to start the subarray, including the element itself.
 * @since 2.1.0
 */
inline infix fun <reified E> Array<E>.afterLastIncluding(element: E) = if (contains(element)) get(lastIndexOf(element)..size) else emptyArray<E>()

/**
 * Returns the element at the position corresponding to the given percentage 
 * within the array. The percentage is calculated relative to the size of the array.
 *
 * @param p The percentage of the array where the element should be retrieved. 
 *          Must be between 0 and 100 inclusive.
 * @return The element at the specified percentage-based position in the array.
 * @throws IndexOutOfBoundsException If the array is empty.
 * @throws IllegalArgumentException If the percentage is not in the valid range [0, 100].
 * @since 2.1.0
 */
@Deprecated("Use get operator instead", replaceWith = ReplaceWith("this[p]"))
infix fun <E> Array<E>.percent(p: Percentage): E {
    isNotEmpty() || throw IndexOutOfBoundsException("List is empty.")
    validate(p.isNotOverflowing) { "Percentage must be between 0 and 100." }
    val index = if (p.isFull) size - 1 else (p.toDouble() / 100 * size).toInt()
    return this[index]
}

/**
 * Creates a Map by associating each element of the array with a key-value pair produced by the given [key] and [value] transformers.
 * Each element of the array is transformed into a key-value pair where the key is the result of the [key] function
 * and the value is the result of the [value] function.
 *
 * @param key A function that returns the key for a map entry from the given element.
 * @param value A function that returns the value for a map entry from the given element.
 * @since 2.1.0
 */
inline fun <E, K, V> Array<E>.associate(key: Transformer<E, K>, value: Transformer<E, V>) =
    associate { key(it) to value(it) }

/**
 * Populates the given mutable map with key-value pairs generated by applying the provided
 * key and value transformers to each element of the array.
 *
 * @param destination The mutable map to be populated with the key-value pairs.
 * @param key A function that transforms an element of the array into a key.
 * @param value A function that transforms an element of the array into a value.
 * @since 2.1.0
 */
inline fun <E, K, V, M : MutableMap<in K, in V>> Array<E>.associateTo(
    destination: M,
    key: Transformer<E, K>,
    value: Transformer<E, V>
) = associateTo(destination) { key(it) to value(it) }

/**
 * Filters the elements of the array by applying the given transformer and retaining only the non-null results.
 * 
 * @param element A transformer function that takes an element of type [E] and returns a result of type [R].
 * @since 2.1.0
 */
inline infix fun <E, R> Array<E>.filterNotNull(element: Transformer<E, R>) =
    filter { element(it).isNotNull() }

/**
 * Filters the elements of the array based on a transformation function that checks for null values.
 *
 * @param element A transformation function that converts each element of the array into a result which is then checked for nullability.
 * @since 2.1.0
 */
inline infix fun <E, R> Array<E>.filterNull(element: Transformer<E, R>) =
    filter { element(it).isNull() }

/**
 * Applies the given block of code to each element in the array without modifying the array itself.
 * This function is typically used for inspecting or performing side effects on the elements.
 *
 * @param E The type of elements contained in the array.
 * @param block A lambda function or consumer to be applied to each element of the array.
 * @since 2.1.0
 */
inline fun <E> Array<E>.peek(block: Consumer<E>) = apply { for (element in this) block(element) }

/**
 * Iterates over the elements of the array and executes the provided block for each element.
 * The block has access to the receiver [LoopContext] and the current element, enabling
 * controlled breaking and continuation operations using the [Break] and [Continue] exceptions.
 *
 * @param E the type of elements in the array
 * @param block a lambda with [LoopContext] as receiver and the current element as parameter
 * used for processing each element in the array
 * @since 2.1.0
 */
@IgnorableReturnValue
inline fun <E> Array<E>.cForEach(block: ReceiverBiConsumer<LoopContext, E>) = apply {
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
 * Iterates over the elements of the array, providing the index and element to the specified block.
 * The iteration can be controlled using `Break` and `Continue` exceptions within the block.
 *
 * @param block A lambda function that takes a [LoopContext], the current index, and the value at that index.
 *              The lambda is invoked for each element in the array.
 *
 * @since 2.1.0
 */
@IgnorableReturnValue
inline fun <E> Array<E>.cForEachIndexed(block: ReceiverTriConsumer<LoopContext, Int, E>) = apply {
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
 * Iterates through the elements of the array and executes the specified action for each element.
 * 
 * This method allows for additional control flows such as `Break` and `Continue` by leveraging
 * a custom implementation. It carries a loop context that can be accessed within the action.
 *
 * @param E the type of elements within the array.
 * @param R the generic result type returned if a `Break` exception is thrown during iteration.
 * @param action a function that accepts a loop context and an element of type `E`. 
 * It contains the operation to be performed for each element.
 * @return an optional result of type `R` if the loop is interrupted by a `Break` exception, or `null` if the iteration completes normally.
 * @since 2.1.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <E, R> Array<E>.rForEach(action: ReceiverBiConsumer<LoopContext, E>): R? {
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
 * Iterates through the array with indexed values and provides a loop context while supporting controlled breaking
 * and continuation of the iteration using `Break` and `Continue` exceptions.
 *
 * @param action A functional receiver accepting a `LoopContext`, the current index of iteration, and the array element.
 * It defines the action to be executed for each element.
 * @return The result of the iteration if the `Break` exception is thrown with a result. Returns `null` if the iteration completes normally or only `Continue` exceptions are thrown
 * .
 * @since 2.1.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <E, R> Array<E>.rForEachIndexed(action: ReceiverTriConsumer<LoopContext, Int, E>): R? {
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
 * Invokes the function-like behavior on an integer value, allowing for manipulation
 * of the provided array based on the integer's value. A positive integer will select
 * the first `n` elements from the array, a negative integer will drop the first `n` 
 * elements, and zero will result in an empty array.
 *
 * @param E the type of elements in the array.
 * @param array the array to be manipulated based on the integer's value.
 * @return an array of type `E` resulting from the operation defined by
 *         the integer's value.
 * @since 2.1.0
 */
@Deprecated("Use take or drop instead")
inline operator fun <reified E> Int.invoke(array: Array<E>): Array<E> {
    if (this == 0) return emptyArray()
    if (isPositive) return array.take(this).toTypedArray<E>()
    return array.drop(-this).toTypedArray<E>()
}

/**
 * Ensures that the array is not empty. If the array is empty, it throws a `ValidationFailedException`.
 *
 * @param causeOf An optional supplier for a custom throwable to be thrown. If provided, it takes precedence over a default exception.
 * @param cause An optional supplier for the underlying cause of the throwable.
 * @return The original array if it is not empty.
 * @throws ValidationFailedException if the array is empty and no custom throwable is provided.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The array is empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The array is empty.", cause?.invoke()))
    return this
}
/**
 * Validates that an array is not empty. If the array is empty, a `ValidationFailedException` is thrown
 * with an optional lazy message and optional throwable causes.
 *
 * @param causeOf an optional supplier for a throwable that will be initialized as the cause of a `ValidationFailedException`.
 * @param cause an optional supplier for a throwable that serves as the root cause of the validation failure.
 * @param lazyMessage a supplier for a lazy-evaluated message to include in the exception if validation fails.
 * @return the same array if it is not empty.
 * @throws ValidationFailedException if the array is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): Array<E> {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that the array is not empty. If the array is empty, a `ValidationFailedException` is thrown.
 *
 * @param property The property associated with this validation. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Used to provide more context in the error message.
 * @param message An optional custom message to be included in the exception if validation fails. Defaults to a generic message.
 * @param causeOf A supplier for the throwable cause of the exception. If provided, it will be invoked when constructing the exception.
 * @param cause An optional supplier for an underlying throwable that caused the validation failure. Defaults to null.
 * @return Returns this array instance if the validation succeeds.
 * @throws ValidationFailedException If the array is empty, with additional context provided by the parameters.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Ensures that the array is not empty, throwing a validation exception if the array is empty.
 *
 * @param property the primary property associated with the validation context; can be null
 * @param variable an additional property providing further validation context; can be null
 * @param message an optional custom message for the validation error; defaults to "is empty" if not provided
 * @param causeOf a supplier for the cause of the exception, if applicable; can be null
 * @param cause an alternative supplier for the exception's cause, if applicable; can be null
 * @return the same array if validation passes (i.e., the array is not empty)
 * @throws ValidationFailedException if the array is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the array is not empty. If the array is empty, it throws a `ValidationFailedException`.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter in the callable being validated. Can be null.
 * @param message An optional custom message to describe the validation failure. Defaults to "is empty" if not provided.
 * @param causeOf A supplier for the primary `Throwable` cause of the validation failure, if applicable. Can be null.
 * @param cause A supplier for the secondary `Throwable` cause of the validation failure, if applicable. Can be null.
 * @return The same array after validation, if it is not empty.
 * @throws ValidationFailedException if the array is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Ensures that the array is not empty. If the array is empty, a [ValidationFailedException] is thrown.
 *
 * @param callable the [KFunction] related to the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional message providing additional context about the validation failure, defaulting to null
 * @param causeOf a supplier for the primary cause of the validation failure, defaulting to null
 * @param cause a supplier for the underlying cause of the validation failure, defaulting to null
 * @return the validated array if it is not empty
 * @throws ValidationFailedException if the array is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the array is not empty. If the array is empty, a `ValidationFailedException` is thrown.
 *
 * @param callableName the name of the callable (e.g., function or method) to identify the context of the validation failure
 * @param parameterName the name of the parameter being validated, or null if not applicable
 * @param message an optional custom error message to provide additional details about the validation failure
 * @param causeOf a supplier of the root cause exception for the validation failure, or null if not applicable
 * @param cause a supplier of the exception to be set as the cause, or null if not applicable
 * @return the same array if it is not empty
 * @throws ValidationFailedException if the array is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the array is not empty. If the array is empty, throws a `ValidationFailedException`.
 *
 * @param callableName The name of the callable (e.g., function or property) where this validation is performed, or null if unspecified.
 * @param parameter The `KParameter` representing the parameter being validated, or null if not applicable.
 * @param message An optional custom error message to include in the exception if validation fails. Defaults to "is empty".
 * @param causeOf A supplier for a root cause to use as the primary exception or null if not provided.
 * @param cause A supplier for the underlying cause of the validation failure or null if not provided.
 * @return The validated array if it is not empty.
 * @throws ValidationFailedException if the array is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the given array is neither null nor empty.
 *
 * This method checks if the array is null or contains no elements. If either condition is true,
 * a `ValidationFailedException` is thrown. The exception can optionally include a specific
 * cause or additional context via the provided `ThrowableSupplier` parameters.
 *
 * @param causeOf an optional supplier for a throwable, providing additional context for the exception.
 *                If null, a default context is used in the exception message.
 * @param cause an optional supplier for the exception's underlying cause. If null, no additional
 *              chained cause is included in the thrown exception.
 * @return the same array if it is neither null nor empty.
 * @throws ValidationFailedException if the array is null or empty, with an optional cause and context.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The collection is null or empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The collection is null or empty.", cause?.invoke()))
    return this
}
/**
 * Validates that the array is not null or empty.
 *
 * If the array is null or empty, this method throws a `ValidationFailedException` with a
 * message and optional causes provided by the supplied parameters.
 *
 * @param causeOf an optional supplier for the primary cause of the exception. If null,
 *                the default exception is used.
 * @param cause an optional supplier for the secondary or additional cause for context.
 * @param lazyMessage a supplier function that provides the error message to be included
 *                    in the exception. This is evaluated lazily.
 * @return the validated array if it is not null or empty.
 * @throws ValidationFailedException if the array is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that the given array is not null or empty. If the array is null or empty, throws a
 * `ValidationFailedException` with an optional property reference, variable name, custom message, and causes
 * for additional context.
 *
 * @param property The property associated with the validation for diagnostic purposes. Can be null.
 * @param variableName An optional name of the variable being validated. Used in the exception message if provided.
 * @param message An optional custom message describing the validation failure. Defaults to "is null or empty" if not specified.
 * @param causeOf A supplier for the base exception to wrap the `ValidationFailedException`. If null, the validation exception will be created directly.
 * @param cause A supplier for an underlying throwable to be used as the cause of the `ValidationFailedException`. Can be null.
 * @return The original array if it is neither null nor empty.
 * @throws ValidationFailedException if the array is null or empty, with detailed information provided by the parameters.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the array is neither null nor empty.
 *
 * If the array is null or empty, a `ValidationFailedException` is thrown with an optional custom message
 * and cause. The exception can also include additional context via the specified `property` and `variable`.
 *
 * @param property the primary property associated with the validation, or null if not specified
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional message to include in the exception if validation fails, or null for a default message
 * @param causeOf an optional supplier to provide the throwable cause of the exception, or null if not used
 * @param cause an optional supplier to generate an additional cause for the exception, or null if not applicable
 * @return the validated array if it is neither null nor empty
 * @throws ValidationFailedException if the array is null or empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the given array is not null or empty.
 *
 * This method ensures that the provided array is neither null nor empty.
 * If the array is null or empty, a `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) where this validation is being performed. Can be null.
 * @param parameterName The name of the parameter in the callable being validated. Can be null.
 * @param message An optional custom message to be included in the exception if validation fails. Defaults to "is null or empty".
 * @param causeOf A supplier for a `Throwable` to be thrown as the root cause of the validation failure. Can be null.
 * @param cause A supplier for an additional `Throwable` to be included as the cause of the `ValidationFailedException`. Can be null.
 * @return The validated array if it is not null or empty.
 * @throws ValidationFailedException if the array is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the given array is neither null nor empty.
 *
 * This method checks if the array is null or contains no elements. If the validation fails,
 * it throws a `ValidationFailedException` with detailed information including the callable,
 * parameter, message, and cause.
 *
 * @param callable the [KFunction] related to the validation context, or `null` if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or `null` if not applicable
 * @param message an optional custom message to describe the validation failure; defaults to "is null or empty"
 * @param causeOf an optional supplier of a `Throwable` to use as the primary cause of failure, or `null`
 * @param cause an optional supplier of a secondary `Throwable` to associate with the validation failure, or `null`
 * @return the validated array if it is not `null` or empty
 * @throws ValidationFailedException if the array is `null` or empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the given array is not `null` or empty.
 *
 * This method checks if the array is either `null` or contains no elements. If the condition is met,
 * it throws a `ValidationFailedException` with the specified details. Otherwise, the array is returned as-is.
 *
 * @param callableName the name of the callable (e.g., function or method) related to this validation.
 * This will be included in the exception details.
 * @param parameterName the name of the parameter being validated. This will be included in the exception details.
 * Defaults to `null` if not specified.
 * @param message an optional custom message providing additional details about the validation failure.
 * Defaults to `null` if not specified.
 * @param causeOf a supplier function for the root cause of the validation failure.
 * Defaults to `null` if not specified.
 * @param cause a supplier function for the underlying exception cause. Defaults to `null` if not specified.
 *
 * @return the original array if it is not `null` or empty.
 *
 * @throws ValidationFailedException if the array is either `null` or contains no elements.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the given array is not null or empty.
 *
 * If the array is null or empty, a `ValidationFailedException` is thrown.
 *
 * @param callableName The name of the callable (e.g., function or property) where validation failed, or null if not specified.
 * @param parameter The KParameter instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message providing additional details about the validation failure. Default is `null`.
 * @param causeOf A supplier for an optional custom Throwable to be thrown instead of the default exception. Default is `null`.
 * @param cause A supplier for the root cause of the exception, if any. Default is `null`.
 * @return The validated array if it is not null or empty.
 * @throws ValidationFailedException If the array is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates if the array is empty. Throws a `ValidationFailedException` if the array is not empty.
 *
 * @param causeOf An optional supplier for a custom throwable to be thrown instead of the default exception.
 *                If provided, this exception will wrap the default exception.
 * @param cause An optional supplier for an underlying cause to be associated with the exception.
 * @return The same array if it is empty.
 * @throws ValidationFailedException if the array is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The collection is not empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The collection is not empty.", cause?.invoke()))
    return this
}
/**
 * Validates that the array is empty. If the array is not empty, a validation exception is thrown.
 *
 * @param causeOf Supplier for the exception to be thrown. If null, a `ValidationFailedException` with additional context is created.
 * @param cause Supplier for the underlying cause of the exception. This parameter is optional and may be null.
 * @param lazyMessage Supplier for the lazy-evaluated detail message included in the exception.
 * @return The original array if it is empty.
 * @throws ValidationFailedException if the array is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): Array<E> {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates whether the array is empty. If the array is not empty, a `ValidationFailedException` is thrown
 * with a detailed error message. The optional parameters allow customization of the validation failure message
 * and the associated cause.
 *
 * @param property The property associated with the validation failure. Can be null if not applicable.
 * @param variableName The optional name of the variable involved in the validation.
 * @param message An optional custom message describing the validation failure. Defaults to "is not empty".
 * @param causeOf A supplier for the throwable cause, used to wrap the `ValidationFailedException` if needed.
 *                Can be null if wrapping is not required.
 * @param cause A supplier for the throwable cause to be associated with the `ValidationFailedException`.
 *              Can be null if no cause is needed.
 * @return The original array if it is empty.
 * @throws ValidationFailedException If the array is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the array is empty. If the array is not empty, a `ValidationFailedException` is thrown.
 *
 * @param property the main KProperty associated with the validation, or null if not specified
 * @param variable an optional secondary KProperty providing additional context, or null if not specified
 * @param message an optional message providing additional details about the validation failure, or null to use a default message
 * @param causeOf a supplier for the cause of the exception, or null if no cause supplier is specified
 * @param cause a supplier for a specific throwable as the cause of the exception, or null if not specified
 * @return the original array if validation passes
 * @throws ValidationFailedException if the array is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the array is empty. If the array is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message for the validation failure. Default message: "is not empty".
 * @param causeOf An optional supplier providing a throwable to use as the cause of the validation failure. Can be null.
 * @param cause An optional supplier providing a secondary throwable cause if `causeOf` is not supplied. Can be null.
 * @return The original array if validation passes (i.e., the array is empty).
 * @throws ValidationFailedException If the array is not empty. Includes the callable, parameterName, custom message, and cause if provided.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the array is empty. If the array is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] related to the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter involved in the validation, or null if not applicable
 * @param message an optional message describing the reason for the validation failure, defaults to "is not empty"
 * @param causeOf a supplier for the throwable to be thrown as the primary cause of the validation failure, or null if not specified
 * @param cause an additional supplier for a throwable to be included as a secondary cause, or null if not specified
 * @return the same array instance if the validation passes
 * @throws ValidationFailedException if the array is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the current array is empty. If the array is not empty, a `ValidationFailedException`
 * is thrown, optionally including additional context such as the callable name, parameter name, custom
 * error message, or a root cause.
 *
 * @param callableName The name of the callable (e.g., function or method) to include in the exception message, if applicable.
 * @param parameterName The name of the parameter being validated, if applicable.
 * @param message An optional custom error message to include in the exception, providing additional details about the validation failure.
 * @param causeOf A supplier for the root cause of the validation failure, if applicable.
 * @param cause A supplier for an additional cause to include in the exception, if applicable.
 * @return The current array if it passes validation (i.e., is empty).
 * @throws ValidationFailedException If the array is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the array is empty. If not, throws a [ValidationFailedException].
 *
 * @param callableName The name of the callable being validated, or null if not specified.
 * @param parameter The [KParameter] instance representing the parameter being validated, or null if not applicable.
 * @param message An optional custom error message for the validation failure. Defaults to "is not empty" if not specified.
 * @param causeOf An optional supplier providing the root cause [Throwable] for the validation failure. If this is not null, the
 * resulting exception will use it as the cause instead of creating a new one.
 * @param cause An optional supplier providing an additional [Throwable] to wrap as the cause in the exception.
 * @return The original array if the validation passes (i.e., it is empty).
 * @throws ValidationFailedException If the array is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates whether the given array is null or empty. If the array is not null or empty,
 * a `ValidationFailedException` is thrown.
 *
 * @param causeOf An optional supplier for the cause of the validation failure. Used to provide
 *                a custom exception to be thrown. If `null`, a default exception is constructed.
 * @param cause An optional supplier for the root cause of the validation failure, which will
 *              be included as the cause of the thrown exception.
 * @return The same array if it is null or empty, allowing chained calls.
 * @throws ValidationFailedException if the array is not null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The collection is not null or empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The collection is not null or empty.", cause?.invoke()))
    return this
}
/**
 * Validates if the given array is null or empty. If the array is not null or empty, a specified exception
 * is thrown. The exception and its message can be customized using the provided suppliers.
 *
 * @param E the type of elements in the array.
 * @param causeOf a supplier for a custom `Throwable` to throw if validation fails. If null, a default exception is used.
 * @param cause a supplier providing the underlying cause of the exception, if applicable. Can be null.
 * @param lazyMessage a supplier that generates the exception message lazily if validation fails.
 * @return the same array if it is `null` or empty.
 * @throws ValidationFailedException if the array is not null and not empty, and no custom throwable is provided.
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates whether the given array is either null or empty. If the array is not null or not empty,
 * a `ValidationFailedException` is thrown with the provided details.
 *
 * @param property The property associated with the validation, used for error reporting. Can be null if not applicable.
 * @param variableName The name of the variable involved in the validation. Included in the exception message if provided.
 * @param message An optional message describing the reason for the validation failure. Defaults to a generic message.
 * @param causeOf An optional supplier for a custom throwable to be used as the primary cause of the validation failure.
 * @param cause An optional supplier for a throwable causing the validation failure. Used as an underlying cause.
 * @return The array itself if it is null or empty; otherwise, an exception is thrown.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates whether the given array is null or empty, throwing a `ValidationFailedException` if it is not.
 *
 * The exception message and cause can be customized using the provided parameters.
 *
 * @param property the primary KProperty associated with the validation, providing contextual information, or null if not specified
 * @param variable an optional secondary KProperty for additional context, or null if not specified
 * @param message an optional custom message to be appended to the exception if validation fails
 * @param causeOf a supplier for the exception to be thrown if validation fails, or null to use a default exception
 * @param cause a supplier for the underlying cause of the exception, or null if not needed
 * @return the original array if it is null or empty
 * @throws ValidationFailedException if the array is neither null nor empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates if the array is null or empty. If it is not null or empty, throws a `ValidationFailedException`.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can be null.
 * @param parameterName The name of the parameter in the given callable that caused the validation issue. Can be null.
 * @param message An optional custom message providing additional details about the validation failure. Default is null.
 * @param causeOf A supplier for the underlying cause of the validation error. Can be null.
 * @param cause A supplier for an additional cause to attach to the exception. Can be null.
 * @return Returns the original array if it is null or empty. If the array is neither null nor empty, an exception is thrown.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates whether the given array is null or empty and throws a `ValidationFailedException` if the validation fails.
 *
 * If the array is not null or not empty, the function throws a `ValidationFailedException` with the provided details.
 *
 * @param callable the [KFunction] related to the validation, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable.
 * @param message an optional custom message for the validation failure, defaulting to "is not null or empty".
 * @param causeOf an optional supplier for a throwable that directly represents the failure cause. If provided,
 *                it initializes the thrown exception. Defaults to null.
 * @param cause an optional supplier for a throwable that represents the underlying cause of the validation failure.
 *              It is chained as the cause of the exception thrown. Defaults to null.
 * @return the validated array if it passed the null or empty validation, or null if the input was null.
 * @throws ValidationFailedException if the array is not null or not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates whether the given array is null or empty. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param callableName the name of the callable (e.g., function or method) related to the validation process
 * @param parameterName the name of the parameter being validated, or null if not specified
 * @param message an optional custom message providing additional details about the validation failure
 * @param causeOf a supplier for the specific exception to throw as the root cause of validation failure, or null if not specified
 * @param cause a supplier for the underlying cause of the exception, or null if not specified
 * @return the original array if it passes the validation
 * @throws ValidationFailedException if the array is not null or not empty and validation fails
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that an array is either null or empty.
 *
 * This function checks if the array is null or empty and throws a `ValidationFailedException`
 * if it is not. The exception can include additional context such as the callable name,
 * parameter details, an optional message, and optionally configured causes. If the validation
 * passes (i.e., the array is null or empty), the original array is returned unchanged.
 *
 * @param callableName The name of the callable being validated, or null if not specified.
 * @param parameter The `KParameter` associated with the validation, or null if not applicable.
 * @param message An optional custom validation failure message to include in the exception.
 * @param causeOf A supplier for the specific cause of the validation failure, or null if not applicable.
 * @param cause An additional supplier for the root cause of the validation failure, or null if not applicable.
 * @return The original array if it is null or empty; otherwise, an exception is thrown.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke()))
    return this
}