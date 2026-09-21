/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("ArrayByteUtilsKt")
@file:Since("6.1.0")
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
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.classes.numbers.*
import dev.tommasop1804.kutils.errors.IterableError.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Extension property to check if the byte array is empty.
 *
 * @receiver The byte array being checked.
 * @return `true` if the byte array has no elements, otherwise `false`.
 * @since 6.1.0
 */
val ByteArray.isEmpty get() = isEmpty()
/**
 * Extension property for `ByteArray` that checks if the array is not empty.
 *
 * Returns `true` if the `ByteArray` contains one or more elements.
 * Returns `false` if the `ByteArray` is empty.
 * @since 6.1.0
 */
val ByteArray.isNotEmpty get() = isNotEmpty()
/**
 * Extension property to check if a nullable [ByteArray] is either null or empty.
 *
 * @return `true` if the [ByteArray] is null or has no elements, `false` otherwise.
 * @since 6.1.0
 */
val ByteArray?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || isEmpty()
}
/**
 * Extension property for nullable ByteArray that checks if the array is not null and not empty.
 *
 * @receiver Nullable ByteArray to be checked.
 * @return `true` if the array is not null and contains elements, `false` otherwise.
 * @since 6.1.0
 */
val ByteArray?.isNotNullOrEmpty: Boolean get() {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return this != null && isNotEmpty()
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
 * @since 6.1.0
 */
val ByteArray.containsDuplicates
    get() = distinct().size != size

/**
 * Extension property for arrays that checks if the array contains exactly one element.
 *
 * This property evaluates to `true` if the array's size is equal to 1, and `false` otherwise.
 *
 * @receiver The array to check.
 * @return `true` if the array has exactly one element, `false` otherwise.
 * @since 6.1.0
 */
val ByteArray.isSingleElement get() = size == 1
/**
 * Extension property for arrays that checks whether the array does not contain
 * exactly one element.
 *
 * @return `true` if the array contains zero elements or more than one element,
 *         `false` if the array contains exactly one element.
 * @since 6.1.0
 */
val ByteArray.isNotSingleElement get() = size != 1

/**
 * Extension property that evaluates whether the ByteArray is sorted in ascending order.
 * It checks each element sequentially to determine if the current element is less than
 * or equal to the next one.
 *
 * This property is read-only and computes the result dynamically whenever accessed.
 * @since 6.1.0
 */
val ByteArray.isSorted get() = isSorted()
/**
 * Extension property for ByteArray that indicates whether the array is not sorted in ascending order.
 *
 * This property returns `true` if the byte array is not sorted in ascending order, and `false` otherwise.
 * It internally utilizes the `isSorted()` function to determine the sorting state of the array.
 * @since 6.1.0
 */
val ByteArray.isNotSorted get() = !isSorted()
/**
 * An extension property for the `ByteArray` class that checks if the array's elements are sorted
 * in descending order. The comparison is based on the natural order of the elements.
 *
 * @return `true` if the array is sorted in descending order, or `false` otherwise.
 * @since 6.1.0
 */
val ByteArray.isSortedDescending get() = isSortedDescending()
/**
 * Extension property for `ByteArray` that determines whether
 * the array is not sorted in descending order.
 *
 * @return `true` if the byte array is not sorted in strictly descending order, `false` otherwise.
 * @since 6.1.0
 */
val ByteArray.isNotDescendingSorted get() = !isSortedDescending()

/**
 * Returns the array itself if it is not empty, or `null` if the array is empty.
 *
 * @receiver The array to be checked.
 * @return The array itself if it contains elements, or `null` if the array is empty.
 * @since 6.1.0
 */
fun ByteArray.orNullIfEmpty() = if (isEmpty()) null else this

/**
 * Returns the current ByteArray if it is not null, or an empty ByteArray if it is null.
 *
 * This function provides a safe way to handle nullable ByteArray instances by ensuring 
 * that a non-null ByteArray is always returned.
 *
 * @return The original ByteArray if it is non-null, otherwise an empty ByteArray.
 * @since 6.1.0
 */
fun ByteArray?.orEmpty() = this ?: byteArrayOf()

/**
 * Performs a logical NOT operation on the nullable [ByteArray].
 * This operator checks whether the [ByteArray] is null or empty.
 *
 * @return `true` if the [ByteArray] is null or empty, otherwise `false`.
 * @since 6.1.0
 */
@JvmName("nullableNot")
operator fun ByteArray?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty
}

/**
 * Operator function that checks whether the ByteArray is empty.
 *
 * @return true if the ByteArray is empty, false otherwise.
 * @since 6.1.0
 */
operator fun ByteArray.not(): Boolean = isEmpty

/**
 * Merges the current array with one or more additional collections into a new array of the same type.
 * If the current array is null or empty, the resulting array will contain elements from the provided collections.
 *
 * @receiver the array of elements to merge; nullable.
 * @param collections additional collections of elements to merge with the current array.
 * @return a new array of the same type containing merged elements.
 * @since 6.1.0
 */
@Suppress("UNCHECKED_CAST")
fun ByteArray?.merge(vararg collections: Collection<Byte>) = orEmpty().toList().merge(*collections).toByteArray()

/**
 * Determines if two arrays share at least one common element.
 *
 * This method checks whether the invoking array and the specified array have any overlapping elements.
 * It uses set intersection to perform the comparison.
 *
 * @param other The array to compare with the calling array.
 * @return `true` if at least one element is common between the arrays, `false` otherwise.
 * @since 6.1.0
 */
infix fun ByteArray.intersects(other: ByteArray) = (this intersect other.toSet()).isNotEmpty()

/**
 * Inserts the specified separator element between each element of the array, 
 * and returns a new array containing the original elements along with the separators.
 *
 * @param separator The element to be inserted between each pair of elements in the array.
 * @return A new array with the separator inserted between each element of the original array.
 * @since 6.1.0
 */
infix fun ByteArray.intersperseWith(separator: Byte): ByteArray =
    flatMapIndexed { index, item ->
        if (index == lastIndex) listOf(item)
        else listOf(item, separator)
    }.toByteArray()

/**
 * Checks if the array contains any of the specified elements.
 *
 * @param elements Vararg parameter representing the elements to check for in the array.
 * @return `true` if at least one of the specified elements is found in the array, `false` otherwise.
 * @since 6.1.0
 */
fun ByteArray.containsAny(vararg elements: Byte) = any { it in elements }
/**
 * Checks if none of the elements in the array are contained in the given set of elements.
 *
 * @param elements The elements to check against the array.
 * @return `true` if none of the elements in the array are present in the given set of elements, `false` otherwise.
 * @since 6.1.0
 */
fun ByteArray.containsNone(vararg elements: Byte) = none { it in elements }
/**
 * Checks if there is at least one element in the array that satisfies the given predicate.
 *
 * @param predicate A condition to test each element of the array.
 * @return `true` if any element matches the predicate, otherwise `false`.
 * @since 6.1.0
 */
operator fun ByteArray.contains(predicate: Predicate<Byte>) = any { predicate(it) }

/**
 * Returns the first element of the array if it exists, or the result of the provided default supplier if the array is empty.
 *
 * @param default A supplier function that provides a default value to return if the array is empty.
 * @return The first element of the array, or the result of the default supplier if the array is empty.
 * @since 6.1.0
 */
fun ByteArray.firstOr(default: Supplier<Byte>): Byte {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { first() } catch (e: NoSuchElementException) { default() }
}
/**
 * Returns the first element of the array if it exists; otherwise, throws an exception provided
 * by the given lazy exception supplier.
 *
 * @param lazyException A supplier function that provides the exception to be thrown if the array is empty.
 *                       This supplier is invoked only when the array does not contain any elements.
 * @return The first element of the array if the array is not empty.
 * @throws Throwable The exception provided by the `lazyException` supplier function if the array is empty.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.firstOrThrow(lazyException: ThrowableSupplier): Byte {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { first() } catch (e: NoSuchElementException) { throw lazyException() }
}
/**
 * Returns the first element of the array wrapped in an `Either` context if it exists,
 * or raises an `Empty` error if the array is empty.
 *
 * This function enables safe access to the first element of an array by using
 * the functional error-handling mechanisms provided by the `either` and `catching`
 * constructs. If the array is empty, a `NoSuchElementException` is caught and
 * transformed into an `Empty` error.
 *
 * @receiver The array whose first element is to be retrieved.
 * @return An `Either` value where:
 *         - `Right<Byte>` contains the first element of the array.
 *         - `Left<Bytempty>` represents an error indicating that the array is empty.
 * @throws Throwable If any exception other than `NoSuchElementException` occurs during execution.
 * @since 6.1.0
 */
fun ByteArray.firstOrError() = either {
    catching({ first() }) { _: NoSuchElementException -> Empty }
}
/**
 * Finds the first element in the array that satisfies the given predicate.
 *
 * If the array is empty, a `NoSuchElementException` is thrown. If no elements
 * in the array match the predicate, a `NoResultsException` is thrown.
 *
 * @param predicate A predicate function used to test each element for a condition.
 * @return The first element that satisfies the predicate.
 * @throws NoSuchElementException If the array is empty.
 * @throws NoResultsException If no elements satisfy the predicate.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.findFirst(predicate: Predicate<Byte>): Byte {
    val list = toList()
    if (list.isEmpty()) throw NoSuchElementException()
    val filtered = list.filter(predicate)
    if (filtered.isEmpty()) throw NoResultsException()
    return filtered.first()
}
/**
 * Returns the first element in the array that matches the given [predicate].
 * If no such element is found, the value provided by the [default] supplier is returned.
 *
 * @param default A supplier function that provides a fallback value if no element satisfies the [predicate].
 * @param predicate A condition that each element is tested against.
 * @return The first element matching the [predicate], or the value produced by [default] if no match is found.
 * @since 6.1.0
 */
fun ByteArray.findFirstOr(default: Supplier<Byte>, predicate: Predicate<Byte>): Byte {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { first(predicate) } catch (e: NoSuchElementException) { default() }
}
/**
 * Finds the first element in the array that matches the given predicate. If no such element is found, the provided exception is thrown.
 *
 * @param lazyException A supplier that provides the exception to be thrown if no element matches the predicate.
 * @param predicate A function that defines the condition to filter elements in the array.
 * @return The first element that matches the predicate.
 * @throws Throwable The exception returned by the lazyException supplier if no matching element is found.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.findFirstOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Byte>): Byte {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { first(predicate) } catch (e: NoSuchElementException) { throw lazyException() }
}
/**
 * Searches for the first element in the array that matches the provided predicate and returns it as a success result.
 * If no element matches, returns an appropriate error wrapped in an `Either`.
 *
 * @param predicate A function that evaluates each element and returns `true` if the element satisfies the condition.
 * @return `Either.Left` containing a `NotFirstResultErrors` object if no matching element is found,
 * or `Either.Right` containing the first element that matches the predicate.
 * @since 6.1.0
 */
fun ByteArray.findFirstOrError(predicate: Predicate<Byte>): Either<NotFirstResultError, Byte> = either {
    catching({ findFirst(predicate) }) { e: Exception -> when (e) {
        is NoResultsException -> NoResults
        is NoSuchElementException -> Empty
        else -> throw IllegalStateException()
    } }
}

/**
 * Returns the last element of the array if it exists; otherwise, returns the result provided by the default supplier.
 *
 * @param default A supplier that provides a default value when the array is empty.
 * @return The last element of the array, or the default value supplied by the given supplier if the array is empty.
 * @since 6.1.0
 */
fun ByteArray.lastOr(default: Supplier<Byte>): Byte {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { last() } catch (_: NoSuchElementException) { default() }
}
/**
 * Returns the last element in the array, or throws an exception provided by the specified supplier if the array is empty.
 *
 * The `lazyException` supplier is invoked at most once if the array is empty.
 *
 * @param lazyException A supplier function that provides the exception to be thrown if the array is empty.
 * @return The last element in the array.
 * @throws Throwable The exception provided by the `lazyException` supplier if the array is empty.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.lastOrThrow(lazyException: ThrowableSupplier): Byte {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { last() } catch (_: NoSuchElementException) { throw lazyException() }
}
/**
 * Returns the last element of the array wrapped in an `Either`, or raises an error if the array is empty.
 *
 * This method attempts to retrieve the last element of an array. If the array is empty, a `NoSuchElementException`
 * is caught and transformed into a predefined `Empty` error type within the `Raise` context.
 *
 * @receiver ByteArray The array from which the last element is to be retrieved.
 * @return An `Either` where:
 *         - `Right<Byte>` contains the last element of the array if it exists.
 *         - `Left<Bytempty>` represents an error raised when the array is empty.
 * @throws RaiseSignal If the underlying `either` or `catching` functions raise a signal during execution.
 * @see either
 * @see catching
 * @since 6.1.0
 */
fun ByteArray.lastOrError() = either {
    catching({ last() }) { _: NoSuchElementException -> Empty }
}
/**
 * Finds the last element in the array that matches the given predicate.
 *
 * This method searches through the array and returns the last element that satisfies
 * the condition defined by the provided predicate. If the array is empty, a
 * `NoSuchElementException` is thrown. If no elements match the predicate, a
 * `NoResultsException` is thrown.
 *
 * @param predicate A predicate function used to filter elements in the array.
 *                  The function takes an element of type `E` as an input and
 *                  returns a boolean indicating whether the element matches
 *                  the condition.
 * @return The last element in the array that satisfies the predicate.
 * @throws NoSuchElementException If the array is empty.
 * @throws NoResultsException If no elements match the predicate.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.findLast(predicate: Predicate<Byte>): Byte {
    val list = toList()
    if (list.isEmpty()) throw NoSuchElementException()
    val filtered = list.filter(predicate)
    if (filtered.isEmpty()) throw NoResultsException()
    return filtered.last()
}
/**
 * Returns the last element of the array that matches the given [predicate].
 * If no such element is found, returns the result of calling the [default] supplier.
 *
 * @param default A supplier function that provides a default value when no matching element is found.
 *                It is invoked at most once if needed.
 * @param predicate A predicate function that tests each element for a matching condition.
 * @return The last element matching the [predicate], or the result of [default] if no match is found.
 * @since 6.1.0
 */
fun ByteArray.findLastOr(default: Supplier<Byte>, predicate: Predicate<Byte>): Byte {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { last(predicate) } catch (_: NoSuchElementException) { default() }

}
/**
 * Finds the last element in the array that matches the given predicate or throws an exception if no element matches.
 *
 * @param lazyException A supplier that provides the exception to be thrown if no element matches the predicate.
 * @param predicate A predicate function to evaluate the elements of the array.
 * @return The last element in the array that matches the predicate.
 * @throws Throwable The exception supplied by the lazyException if no element matches the predicate.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.findLastOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Byte>): Byte {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { last(predicate) } catch (_: NoSuchElementException) { throw lazyException() }
}
/**
 * Attempts to find the last element in the array that satisfies the given predicate.
 * If no such element exists, it returns an error wrapped in an `Either` type.
 *
 * @param predicate A function that determines whether an element satisfies the condition.
 * @return An `Either` containing the last matching element, or an error of type `NotLastResultsErrors` if no match is found.
 * @since 6.1.0
 */
fun ByteArray.findLastOrError(predicate: Predicate<Byte>): Either<NotLastResultsError, Byte> = either {
    catching({ findLast(predicate) }) { e: Exception -> when (e) {
        is NoResultsException -> NoResults
        is NoSuchElementException -> Empty
        else -> throw IllegalStateException()
    } }
}

/**
 * Returns the second element of the array.
 *
 * Throws [NoSuchElementException] if the size of the array is less than 2.
 *
 * @receiver The array from which the second element will be retrieved.
 * @return The second element of the array.
 * @throws NoSuchElementException If the array contains less than two elements.
 * @since 6.1.0
 */
fun ByteArray.second() = if (size < 2) throw NoSuchElementException("List size $size doesn't allow to get second element.") else this[1]
/**
 * Returns the second element of the array if the array contains at least two elements,
 * or `null` if the array contains fewer than two elements.
 *
 * This function does not modify the original array and returns a nullable result.
 *
 * @receiver The array from which the second element is to be retrieved.
 * @return The second element of the array, or `null` if the array has fewer than two elements.
 *
 * @since 6.1.0
 */
fun ByteArray.secondOrNull() = if (size < 2) null else this[1]
/**
 * Returns the second element of the array if it exists, or the result
 * of the [default] supplier if the array has fewer than two elements.
 *
 * @param default A supplier function that provides the default value if the array has fewer than two elements.
 * @return The second element of the array, or the result of the [default] supplier.
 * @since 6.1.0
 */
fun ByteArray.secondOr(default: Supplier<Byte>): Byte {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return if (size < 2) default() else this[1]
}
/**
 * Returns the second element of the array or throws an exception provided by the given supplier
 * if the array contains fewer than two elements.
 *
 * @param lazyException a function that supplies the exception to throw if the array has fewer than two elements.
 * @return the second element of the array.
 * @throws Throwable if the array contains fewer than two elements.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.secondOrThrow(lazyException: ThrowableSupplier): Byte {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    if (size < 2) throw lazyException()
    return this[1]
}
/**
 * Returns the second element of the array wrapped in an `Either` if it exists, or an error if the array does not contain a second element.
 *
 * The method checks if the array has a second element and wraps it in a successful `Either`. If the array is empty, an `Empty` error is returned.
 * If there is no second element but the array is not empty, a `NoSuchElement` error is returned with the index `1`.
 *
 * @return An `Either` containing the second element of the array on success or a `NotInnerElementErrors` error on failure.
 * @since 6.1.0
 */
fun ByteArray.secondOrError(): Either<NotInnerElementError, Byte> = either {
    catching({ second() }) { _: NoSuchElementException -> if (isEmpty()) Empty else NoSuchElement(1) }
}
/**
 * Finds and returns the second element in the array that matches the specified predicate.
 *
 * @param predicate A condition to determine whether an element in the array matches.
 * @return The second element in the array that satisfies the given predicate.
 * @throws NoSuchElementException If the array is empty.
 * @throws NoResultsException If no elements match the specified predicate.
 * @throws TooFewResultsException If less than two elements match the specified predicate.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.findSecond(predicate: Predicate<Byte>): Byte {
    val list = toList()
    if (list.isEmpty()) throw NoSuchElementException()
    val filtered = list.filter(predicate)
    if (filtered.isEmpty()) throw NoResultsException()
    if (filtered.size < 2) throw TooFewResultsException(filtered.size)
    return filtered.second()
}
/**
 * Finds the second element in the array that matches the given predicate or returns `null` if no such element exists.
 *
 * The function filters the array by applying the provided predicate and then retrieves the second element
 * from the resulting collection, if available. If the filtered collection contains fewer than two elements,
 * the function returns `null`.
 *
 * @param predicate A function that defines the condition to match elements in the array. Only elements that pass
 * this condition are considered for finding the second match.
 * @return The second element matching the predicate, or `null` if fewer than two elements match.
 * @since 6.1.0
 */
fun ByteArray.findSecondOrNull(predicate: Predicate<Byte>) = filter(predicate).secondOrNull()
/**
 * Filters the array using the provided predicate and returns the second element that matches the predicate.
 * If there are fewer than two matching elements, the default value provided by the supplier is returned.
 *
 * @param default A supplier function that provides a default value when the filtered array does not contain at least two matching elements.
 * @param predicate A predicate to filter the elements of the array.
 * @return The second element that matches the predicate, or the result of invoking the default supplier if fewer than two elements match.
 * @since 6.1.0
 */
fun ByteArray.findSecondOr(default: Supplier<Byte>, predicate: Predicate<Byte>) = filter(predicate).secondOr(default)
/**
 * Filters the elements of the array based on the given predicate and returns the second matching
 * element if it exists, or throws an exception provided by the given `lazyException` supplier if
 * there are fewer than two matching elements.
 *
 * @param lazyException A supplier function that produces the exception to be thrown if fewer
 *                      than two elements match the given predicate.
 * @param predicate A condition to filter elements of the array.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.findSecondOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Byte>) = filter(predicate).secondOrThrow(lazyException)
/**
 * Searches for the second element in the array that matches the given predicate.
 * If a second matching element is found, it is returned wrapped in `Either.Right`.
 * If an error occurs or the condition is not met, an appropriate error is returned
 * wrapped in `Either.Left`.
 *
 * @param predicate A condition used to search for the second element in the array.
 * @return Either a matching element wrapped in `Either.Right` if found, or an error
 * wrapped in `Either.Left` if no matching element or insufficient results are found.
 * @since 6.1.0
 */
fun ByteArray.findSecondOrError(predicate: Predicate<Byte>): Either<NotInnerResultError, Byte> = either {
    catching({ findSecond(predicate) }) { e: Exception -> when (e) {
        is NoResultsException -> NoResults
        is NoSuchElementException -> Empty
        is TooFewResultsException -> TooFewResults
        else -> throw IllegalStateException()
    } }
}

/**
 * Returns the third element of the array.
 *
 * This function retrieves the element at index 2 in the array.
 * If the array size is less than 3, a [NoSuchElementException] is thrown.
 *
 * @receiver The array from which the third element is to be retrieved.
 * @return The third element of the array at index 2.
 * @throws NoSuchElementException if the array size is less than 3.
 * @since 6.1.0
 */
fun ByteArray.third() = if (size < 3) throw NoSuchElementException("List size $size doesn't allow to get third element.") else this[2]
/**
 * Returns the third element of the array if the array contains at least three elements,
 * or `null` if the array has fewer than three elements.
 *
 * @receiver Array of elements of type E.
 * @return The third element of the array, or `null` if the array size is less than 3.
 * @since 6.1.0
 */
fun ByteArray.thirdOrNull() = if (size < 3) null else this[2]
/**
 * Returns the third element of the array if it exists; otherwise, returns the value
 * provided by the given default supplier.
 *
 * @param default A supplier function that provides a default value if the array size is less than three.
 * @return The third element of the array or the value provided by the default supplier.
 * @since 6.1.0
 */
fun ByteArray.thirdOr(default: Supplier<Byte>): Byte {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return if (size < 3) default() else this[2]
}
/**
 * Returns the third element of the array if it exists; otherwise, throws an exception
 * provided by the given `lazyException` supplier.
 *
 * @param lazyException A supplier that provides the exception to be thrown if the array
 * does not contain at least three elements.
 * @return The third element of the array.
 * @throws Throwable The exception provided by the `lazyException` supplier if the array
 * has fewer than three elements.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.thirdOrThrow(lazyException: ThrowableSupplier): Byte {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    if (size < 3) throw lazyException()
    return this[2]
}
/**
 * Returns the third element of the array wrapped in an `Either` type, or an error if the element
 * does not exist. If the array is empty, it will return an `Empty` error. If the array does not
 * have a third element but is not empty, it will return a `NoSuchElement` error with the requested index of 2.
 *
 * @return An `Either` containing the third element of the array or a `NotInnerElementErrors`.
 * @since 6.1.0
 */
fun ByteArray.thirdOrError(): Either<NotInnerElementError, Byte> = either {
    catching({ third() }) { _: NoSuchElementException -> if (isEmpty()) Empty else NoSuchElement(2) }
}
/**
 * Finds the third element in the array that matches the given predicate.
 *
 * @param predicate a condition used to filter the elements of the array.
 * @return the third element that satisfies the given predicate.
 * @throws NoSuchElementException if the array is empty.
 * @throws NoResultsException if no elements satisfy the predicate.
 * @throws TooFewResultsException if fewer than three elements satisfy the predicate.
 * @since 6.1.0
 */
fun ByteArray.findThird(predicate: Predicate<Byte>): Byte {
    val list = toList()
    if (list.isEmpty()) throw NoSuchElementException()
    val filtered = list.filter(predicate)
    if (filtered.isEmpty()) throw NoResultsException()
    if (filtered.size < 3) throw TooFewResultsException(filtered.size)
    return filtered.third()
}
/**
 * Finds the third element in the array that matches the given predicate or returns `null` if no such element exists.
 *
 * The filtering is done using the provided predicate, and the resulting elements are checked for their third presence.
 *
 * @param predicate The condition used to filter elements in the array.
 * @return The third element that matches the predicate, or `null` if no such element exists.
 * @since 6.1.0
 */
fun ByteArray.findThirdOrNull(predicate: Predicate<Byte>) = filter(predicate).thirdOrNull()
/**
 * Filters the array based on the provided predicate and returns the third element if it exists;
 * otherwise, evaluates and returns the result of the provided default supplier.
 *
 * @param default A supplier function that provides a default value to return if the filtered
 * array contains fewer than three elements.
 * @param predicate A predicate function used to filter the elements of the array.
 * @return The third element of the filtered array if present, or the default value provided
 * by the supplier.
 * @since 6.1.0
 */
fun ByteArray.findThirdOr(default: Supplier<Byte>, predicate: Predicate<Byte>) = filter(predicate).thirdOr(default)
/**
 * Finds the third element in the array that matches the specified predicate and returns it.
 * If no such element is found, the provided exception supplier is used to throw an exception.
 *
 * @param lazyException a supplier that provides the exception to be thrown if no third element is found
 * @param predicate a condition that elements in the array must satisfy
 * @throws Throwable if no third element matching the predicate is found
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.findThirdOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Byte>) = filter(predicate).thirdOrThrow(lazyException)
/**
 * Searches for the third element in the array that matches the given predicate.
 * Returns the element wrapped in an `Either` if found, or an error if no such element or too few elements exist.
 *
 * @param predicate A condition used to filter elements in the array.
 * @return An `Either` containing the third matching element if it exists, or an error of type `NotInnerResultErrors`.
 * @since 6.1.0
 */
fun ByteArray.findThirdOrError(predicate: Predicate<Byte>): Either<NotInnerResultError, Byte> = either {
    catching({ findThird(predicate) }) { e: Exception -> when (e) {
        is NoResultsException -> NoResults
        is NoSuchElementException -> Empty
        is TooFewResultsException -> TooFewResults
        else -> throw IllegalStateException()
    } }
}

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
 * @since 6.1.0
 */
fun ByteArray.onlyElement() = toList().run {
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
 * @since 6.1.0
 */
fun ByteArray.onlyElementOrNull() = toList().run { if (size == 1) first() else null }
/**
 * Returns the single element of the array if it contains exactly one element, or the value
 * provided by the supplied default function otherwise.
 *
 * @param default A supplier function to provide a default value if the array does not contain exactly one element.
 * @since 6.1.0
 */
infix fun ByteArray.onlyElementOr(default: Supplier<Byte>): Byte {
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
 * @since 6.1.0
 */
@IgnorableReturnValue
infix fun ByteArray.onlyElementOrThrow(lazyException: ThrowableSupplier): Byte {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return toList().run { if (size == 1) first() else throw lazyException() }
}
/**
 * Returns the single element of the array wrapped inside an `Either` if the array contains exactly one element,
 * or returns an appropriate error wrapped in an `Either` if the array is empty or has more than one element.
 *
 * @return `Either<NotOnlyElementErrors, Byte>` where the right value is the single element of the array if present,
 * or the left value contains a `NotOnlyElementErrors` indicating the error condition:
 * - `Empty` if the array is empty.
 * - `TooManyElement` if the array contains more than one element.
 * @since 6.1.0
 */
fun ByteArray.onlyElementOrError(): Either<NotOnlyElementError, Byte> = either {
    catching({ onlyElement() }) { e: Exception -> when (e) {
        is NoSuchElementException -> Empty
        is TooManyElementsException -> TooManyElements
        else -> throw IllegalStateException()
    } }
}

/**
 * Finds the only element in the array that matches the given predicate.
 * Throws an exception if no elements match, or if more than one element matches.
 *
 * @param predicate A condition to evaluate each element in the array.
 * @return The single element that matches the predicate.
 * @throws NoSuchElementException if the array is empty.
 * @throws NoResultsException if no elements match the predicate.
 * @throws TooManyResultsException if more than one element matches the predicate.
 * @since 6.1.0
 */
infix fun ByteArray.findOnlyElement(predicate: Predicate<Byte>) = toList()
    .requireOrThrow({ NoSuchElementException() }, { it.isNotEmpty() })
    .filter(predicate).run {
        if (isEmpty()) throw NoResultsException()
        if (size == 1) first()
        else throw TooManyResultsException(size)
    }
/**
 * Finds the only element in the array that matches the given predicate or returns null if no such
 * element exists or if there is more than one matching element.
 *
 * @param predicate The condition used to filter the elements of the array.
 * @return The single element matching the predicate or null if none or more than one match is found.
 * @since 6.1.0
 */
infix fun ByteArray.findOnlyElementOrNull(predicate: Predicate<Byte>) = filter(predicate).run { if (size == 1) first() else null }
/**
 * Finds the only element in the array that matches the given predicate. If no such element exists or
 * if more than one element matches, the provided default value is returned.
 *
 * @param default A supplier providing the default value to return when the condition is not met.
 * @param predicate A predicate used to evaluate which elements match the condition.
 * @return The single element that matches the predicate or the default value if no such element
 *         exists or multiple elements match.
 * @since 6.1.0
 */
fun ByteArray.findOnlyElementOr(default: Supplier<Byte>, predicate: Predicate<Byte>): Byte {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return filter(predicate).run { if (size == 1) first() else default() }
}
/**
 * Finds the only element in the array that matches the given predicate, or throws an exception
 * provided by the `lazyException` supplier if there is not exactly one matching element.
 *
 * @param lazyException A supplier for the exception to be thrown if the conditions are not met.
 * This supplier will only be invoked at most once.
 * @param predicate A condition used to filter elements in the array. The method will evaluate this
 * predicate for each element in the array.
 * @return The single element that matches the predicate if precisely one element satisfies the condition.
 * @throws Throwable The exception provided by the `lazyException` supplier if no matching element
 * is found or if more than one element matches the predicate.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun ByteArray.findOnlyElementOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Byte>): Byte {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return filter(predicate).run { if (size == 1) first() else throw lazyException() }
}
/**
 * Finds the only element in the array that matches the given predicate or returns an error if the conditions are not met.
 *
 * @param predicate A function that tests whether an element satisfies the desired condition.
 * @return Either an error encapsulated in a [NotOnlyResultError] object if the array has no matching element,
 *         too many matching elements, or another exception occurs, or the single matching element if exactly one exists.
 * @since 6.1.0
 */
fun ByteArray.findOnlyElementOrError(predicate: Predicate<Byte>): Either<NotOnlyResultError, Byte> = either {
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
 * Splits the array into chunks based on a predicate condition. Each chunk will end
 * immediately before an element that satisfies the given predicate, and a new chunk
 * will start after that element.
 *
 * @param predicate a function that evaluates each element to determine chunk boundaries.
 * @return a list of lists, where each sublist represents a chunk of the original array
 *         split according to the predicate.
 * @since 6.1.0
 */
infix fun ByteArray.chunkedWhile(predicate: Predicate<Byte>): List<List<Byte>> = toList().run {
    if (isEmpty()) return@run emptyList()
    val result = mutableListOf<MutableList<Byte>>()
    var current = mutableListOf<Byte>()
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
 * @since 6.1.0
 */
infix fun ByteArray.repeatEach(n: Int): List<Byte> {
    val resultList = mutableListOf<Byte>()
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
 * @since 6.1.0
 */
fun ByteArray.mode(): Byte? = toTypedArray().groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

/**
 * Searches for the specified element in the array and returns its index. If the element is not found,
 * the specified exception is lazily created and thrown.
 *
 * @param element The element to locate in the array.
 * @param lazyException A supplier function that provides the exception to be thrown when the element is not found.
 * @throws Throwable The exception returned by the [lazyException] supplier if the element is not found.
 * @return The index of the element in the array if it exists.
 * @since 6.1.0
 */
fun ByteArray.indexOfOrThrow(element: Byte, lazyException: ThrowableSupplier) =
    indexOf(element).expectNot(INDEX_NOT_FOUND, causeOf = { lazyException() })
/**
 * Searches for the last occurrence of the specified element within the array and
 * throws a custom exception if the element is not found.
 *
 * @param element The element to search for within the array.
 * @param lazyException A supplier that provides the exception to be thrown if the element is not found.
 * @throws Throwable The exception supplied by [lazyException] if the element is not found.
 * @return The index of the last occurrence of the specified element if it exists in the array.
 * @since 6.1.0
 */
fun ByteArray.lastIndexOfOrThrow(element: Byte, lazyException: ThrowableSupplier) =
    lastIndexOf(element).expectNot(INDEX_NOT_FOUND, causeOf = { lazyException() })
/**
 * Finds the index of the first element in the array that matches the given predicate.
 * Throws an exception provided by the `lazyException` supplier if no element satisfies the predicate.
 *
 * @param lazyException A supplier that provides the throwable to be thrown if no elements match the predicate.
 * @param predicate A predicate that determines whether an element satisfies a condition.
 * @return The index of the first element matching the predicate.
 * @throws Throwable The exception provided by `lazyException` if no element matches the predicate.
 * @since 6.1.0
 */
fun ByteArray.indexOfFirstOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Byte>) =
    indexOfFirst(predicate).expectNot(INDEX_NOT_FOUND, causeOf = { lazyException() })
/**
 * Returns the index of the last element in the array that matches the given [predicate].
 * Throws an exception provided by [lazyException] if no matching element is found.
 *
 * @param lazyException A supplier for the exception to be thrown if no matching element is found.
 * @param predicate A condition to evaluate each element of the array.
 * @throws Throwable The exception provided by [lazyException] if no element satisfies the [predicate].
 * @return The index of the last element that matches the [predicate].
 * @since 6.1.0
 */
fun ByteArray.indexOfLastOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Byte>) =
    indexOfLast(predicate).expectNot(INDEX_NOT_FOUND, causeOf = { lazyException() })
/**
 * Searches the array for the specified element and returns its index, or raises a `NotFound` error
 * if the element is not present.
 *
 * The method uses the `either` scope to handle potential errors functionally. If the element is found,
 * its index is returned; otherwise, a `NotFound` error is raised.
 *
 * @param element The element to search for within the array.
 * @return The index of the given element in the array if it is found.
 * @since 6.1.0
 */
fun ByteArray.indexOfOrError(element: Byte) = either {
    val index = indexOf(element)
    ensure(index != INDEX_NOT_FOUND) { NotFound(element) }
    index
}
/**
 * Returns the last index of the specified element in the array, or raises a [NotFound] error
 * if the element is not present.
 *
 * This method ensures safe operations by leveraging the `either` scope to handle errors functionally.
 *
 * @param element The element to search for in the array.
 * @return The last index of the specified element in the array if it exists.
 * @since 6.1.0
 */
fun ByteArray.lastIndexOfOrError(element: Byte) = either {
    val index = lastIndexOf(element)
    ensure(index != INDEX_NOT_FOUND) { NotFound(element) }
    index
}
/**
 * Returns the index of the first element in the array that matches the given [predicate].
 * If no such element is found, raises an error of type `NoResults`.
 *
 * This function utilizes the `either` scope for functional-style error handling,
 * ensuring short-circuiting if no match is found.
 *
 * @param predicate A predicate function used to evaluate each element in the array.
 *                  The function should return `true` for the desired element.
 * @return The index of the first element that matches the [predicate].
 * @since 6.1.0
 */
fun ByteArray.indexOfFirstOrError(predicate: Predicate<Byte>) = either {
    val index = indexOfFirst(predicate)
    ensure(index != INDEX_NOT_FOUND) { NoResults }
    index
}
/**
 * Returns the index of the last element matching the given [predicate] in the array.
 * If no such element is found, raises an error.
 *
 * This function uses the `either` scope to handle the case where no matching element is found
 * by raising an error of type `NoResults`.
 *
 * @param predicate A lambda function that takes an element of type [Byte] and returns `true`
 *                  if the element matches the condition, or `false` otherwise.
 * @return The index of the last element in the array that matches the given [predicate].
 * @since 6.1.0
 */
fun ByteArray.indexOfLastOrError(predicate: Predicate<Byte>) = either {
    val index = indexOfLast(predicate)
    ensure(index != INDEX_NOT_FOUND) { NoResults }
    index
}

/**
 * Returns the original array if it is not null or empty. Otherwise, invokes the provided
 * `defaultValue` supplier and returns its result.
 *
 * @param defaultValue A supplier function that provides a default array when the original array is null or empty.
 * @return The original array if it is not null or empty, otherwise the result of invoking `defaultValue`.
 * @since 6.1.0
 */
inline fun ByteArray?.ifNullOrEmpty(defaultValue: Supplier<ByteArray>): ByteArray {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
    }
    return if (isNullOrEmpty) defaultValue() else this
}

/**
 * Executes the given action if the array is not empty and returns the array.
 *
 * @param action A consumer that performs an operation on the array if it is not empty.
 * @return The original array.
 */
@IgnorableReturnValue
inline fun ByteArray.ifNotEmpty(action: Consumer<ByteArray>): ByteArray {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNotEmpty()) action(this)
    return this
}
/**
 * Executes the specified [action] if the array is not null and not empty.
 *
 * This function checks if the array is both non-null and contains at least one element.
 * If the condition is met, the provided [action] is invoked with the array as its parameter.
 *
 * @param action A function to be executed if the array is not null and not empty.
 * @return The original array, or null if the array is null or empty.
 */
@IgnorableReturnValue
inline fun ByteArray?.ifNotNullOrEmpty(action: Consumer<ByteArray>): ByteArray? {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        (this@ifNotNullOrEmpty != null) implies returnsNotNull()
    }
    if (isNotNullOrEmpty) action(this)
    return this
}

/**
 * Executes the specified action if the array contains the given element and returns the array.
 *
 * @param element The element to check for in the array.
 * @param action The action to execute if the element is found.
 * @return The original array, regardless of whether the action was executed.
 * @since 6.1.0
 */
@IgnorableReturnValue
inline fun ByteArray.ifContains(element: Byte, action: Consumer<ByteArray>): ByteArray {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (contains(element)) action(this)
    return this
}
/**
 * Executes the given action if the array does not contain the specified element.
 *
 * @param element The element to check for in the array.
 * @param action The action to be performed if the array does not contain the specified element.
 * @return The original array after performing the action, if applicable.
 * @since 6.1.0
 */
@IgnorableReturnValue
inline fun ByteArray.ifNotContains(element: Byte, action: Consumer<ByteArray>): ByteArray {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (!contains(element)) action(this)
    return this
}

/**
 * Filters the elements of the array by selecting only those at indices that are multiples of the given step.
 *
 * @param step The interval at which elements are selected from the array. Must be a positive integer.
 * @return A list containing elements at indices divisible by the specified step.
 * @since 6.1.0
 */
infix fun ByteArray.step(step: Int) =  filterIndexed { index, c -> index % step == 0 }

/**
 * Repeats the elements of the array a specified number of times and returns a new array.
 *
 * @receiver The array whose elements will be repeated.
 * @param n The number of times the elements of the array should be repeated.
 * @return An array containing the elements of the array repeated n times.
 * @since 6.1.0
 */
operator fun ByteArray.times(n: Int) = (1..n).flatMap { toList() }.toByteArray()

/**
 * Splits the array into two collections based on a given predicate.
 *
 * This operator function divides the elements in the array into two groups:
 * one containing elements that satisfy the given predicate and the other
 * containing elements that do not.
 *
 * @param predicate A condition used to evaluate each element in the array.
 * @since 6.1.0
 */
operator fun ByteArray.div(predicate: Predicate<Byte>) = partition(predicate)

/**
 * Splits the array into a list of smaller lists (chunks), each of the specified size.
 *
 * @param chunkSize the size of each chunk into which the array will be divided
 * @return a MultiList containing the chunked lists
 * @since 6.1.0
 */
operator fun ByteArray.rem(chunkSize: Int): MultiList<Byte> = toList().chunked(chunkSize)
/**
 * Divides the elements of the array into a list of sublists based on the provided predicate.
 * Each sublist contains consecutive elements until the predicate returns false.
 *
 * @param predicate A condition used to split the array into multiple sublists.
 * @return A MultiList containing all sublists, where each sublist satisfies the chunked condition.
 * @since 6.1.0
 */
operator fun ByteArray.rem(predicate: Predicate<Byte>): MultiList<Byte> = chunkedWhile(predicate)

/**
 * Decrements the size of the array by removing the last element and returns a new array containing the remaining elements.
 *
 * @return A new array of the same type containing all elements of the original array except the last one.
 * @since 6.1.0
 */
operator fun ByteArray.dec(): ByteArray = toList().subList(0, lastIndex).toByteArray()
/**
 * Creates a new array containing a range of elements from the start of the array up to and including the specified end index.
 *
 * @param endIndex The inclusive end index of the range to be included in the new array. Must be within the valid bounds of the array.
 * @return A new array containing the specified range of elements from the original array.
 * @since 6.1.0
 */
operator fun ByteArray.rangeTo(endIndex: Int) = toList().subList(0, endIndex + 1).toByteArray()

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
 * @since 6.1.0
 */
operator fun ByteArray.invoke(circularStartIndex: Int, circularEndIndex: Int, step: Int = 1): ByteArray {
    validate(step > 0) { "The step value must be greater than zero." }
    if (isEmpty()) throw NoSuchElementException("List cannot be empty.")

    val result = mutableListOf<Byte>()
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
    return result.toByteArray()
}
/**
 * Enables invoking an array with a circular range to access elements in the specified step order.
 * This operator function allows the array to be accessed using circular indexing within the defined range.
 * 
 * @param circularRange The range of indices to iterate over. Indices can wrap around circularly within the array bounds.
 * @param step The step value for iteration. Defaults to 1 if not specified.
 * @since 6.1.0
 */
operator fun ByteArray.invoke(circularRange: IntRange, step: Int = 1) =
    invoke(circularRange.first, circularRange.last + 1, step)
/**
 * Invokes each index in the given circular progression on the array, producing a new array
 * with the elements at those indices. The progression can wrap around if indices exceed the array bounds.
 *
 * @param circularProgression The progression of indices to invoke on the array. Can wrap around if indices exceed bounds.
 * @return A new array containing elements at the indices defined by the circular progression.
 * @throws IndexOutOfBoundsException If the array is empty.
 * @since 6.1.0
 */
operator fun ByteArray.invoke(circularProgression: IntProgression): ByteArray {
    if (isEmpty()) throw IndexOutOfBoundsException("List cannot be null.")

    val result = mutableListOf<Byte>()
    for (i in circularProgression) result.add(invoke(i))
    return result.toByteArray()
}
/**
 * Provides a way to access elements of an array using a circular indexing mechanism.
 * If the provided index exceeds the array bounds, the index wraps around based on the array's size.
 *
 * @param circularIndex The index to access, which may exceed the array bounds and will wrap around circularly.
 * @return The element at the resolved circular index in the array.
 * @throws IndexOutOfBoundsException if the array is empty.
 * @since 6.1.0
 */
operator fun ByteArray.invoke(circularIndex: Int): Byte {
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
 * @since 6.1.0
 */
inline operator fun ByteArray.invoke(filter: Predicate<Byte>): ByteArray {
    val destination = ArrayList<Byte>()
    for (element in this) if (filter(element)) destination.add(element)
    return destination.toByteArray()
}

/**
 * Returns the first element of the array that satisfies the given predicate.
 *
 * @param find A predicate function that evaluates each element to determine
 *             if it matches the desired condition.
 * @return The first element that satisfies the predicate, or `null` if no such element is found.
 * @since 6.1.0
 */
inline operator fun ByteArray.get(find: Predicate<Byte>): Byte? {
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
 * @since 6.1.0
 */
operator fun ByteArray.get(find: Predicate<Byte>, lazyException: ThrowableSupplier): Byte {
    for (element in this) if (find(element)) return element
    throw lazyException()
}

/**
 * Returns a new array containing elements of the original array that do not match
 * the given predicate.
 *
 * @param filterNot A predicate used to filter out elements from the array.
 *                  Elements that satisfy this predicate will be excluded from the result.
 * @return A new array without the elements that match the given predicate.
 * @since 6.1.0
 */
operator fun ByteArray.minus(filterNot: Predicate<Byte>) = filterNot(filterNot)

/**
 * Retrieves a subarray from the current array based on the specified range.
 *
 * @param range The range of indices to extract from the array. The range should be defined as an `IntProgression`.
 * @return A new array containing the elements within the specified range.
 * @since 6.1.0
 */
@Suppress("deprecation")
operator fun ByteArray.get(range: IntProgression) = toList().subList(range).toByteArray()

/**
 * Retrieves the element at the specified [index] from the array. If the element at the given index
 * does not exist, the [lazyException] supplier is invoked to provide a throwable that is then thrown.
 *
 * @param index The position of the element to retrieve from the array.
 * @param lazyException A lambda that supplies the throwable to be thrown if the index is not valid.
 *                       Defaults to a supplier of NoSuchElementException.
 * @return The element at the specified [index].
 * @throws Throwable The exception supplied by [lazyException] if the index is out of bounds.
 * @since 6.1.0
 */
operator fun ByteArray.get(index: Int, lazyException: ThrowableSupplier = { IndexOutOfBoundsException("Index $index not present") }): Byte =
    try { this[index] } catch (_: Exception) { throw lazyException() }
/**
 * Returns the element at the position corresponding to the given percentage
 * within the array. The percentage is calculated relative to the size of the array.
 *
 * @param percentage The percentage of the array where the element should be retrieved.
 *          Must be between 0 and 100 inclusive.
 * @return The element at the specified percentage-based position in the array.
 * @throws IndexOutOfBoundsException If the array is empty.
 * @throws IllegalArgumentException If the percentage is not in the valid range [0, 100].
 * @since 6.1.0
 */
operator fun ByteArray.get(percentage: Percentage) = percent(percentage)

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
 * @since 6.1.0
 */
infix fun ByteArray.sorted(direction: SortDirection) = when (direction) {
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
 * @since 6.1.0
 */
inline fun <R : Comparable<R>> ByteArray.sortedBy(direction: SortDirection, crossinline selector: Transformer<Byte, R?>) = when (direction) {
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
 * @since 6.1.0
 */
infix fun ByteArray.before(element: Byte) = if (contains(element)) get(0..<indexOf(element)) else byteArrayOf()
/**
 * Returns a subarray from the beginning of the array up to and including the specified element.
 * If the element is not found in the array, an empty array is returned.
 *
 * @param element The element up to which the subarray should be extracted, inclusive.
 * @return A new array containing all elements from the beginning of the array up to and including the specified element,
 * or an empty array if the element is not found.
 * @since 6.1.0
 */
infix fun ByteArray.beforeIncluding(element: Byte) = if (contains(element)) get(0..indexOf(element)) else byteArrayOf()
/**
 * Returns a new array containing all elements before the last occurrence of the specified element in the array.
 * If the specified element is not found, an empty array is returned.
 *
 * @param element The element whose last occurrence determines the cutoff point in the array.
 * @return A new array containing elements before the last occurrence of the specified element, or an empty array if the element is not found.
 * @since 6.1.0
 */
infix fun ByteArray.beforeLast(element: Byte) = if (contains(element)) get(0..<lastIndexOf(element)) else byteArrayOf()
/**
 * Retrieves a subarray from the current array, starting from the first element and up to and including
 * the last occurrence of the specified element. If the element is not present in the array, returns an empty array.
 *
 * @param element The element up to which the subarray should be extracted, including the element itself.
 * @return An array containing elements from the start up to and including the last occurrence of the provided element,
 *         or an empty array if the element is not found.
 * @since 6.1.0
 */
infix fun ByteArray.beforeLastIncluding(element: Byte) = if (contains(element)) get(0..lastIndexOf(element)) else byteArrayOf()
/**
 * Returns a new array containing the elements that appear after the specified element in the original array.
 * If the given element is not found in the array, an empty array is returned.
 *
 * @param element the element after which the sub-array should be returned.
 * @return a new array containing elements following the given element, or an empty array if the element is not found.
 * @since 6.1.0
 */
infix fun ByteArray.after(element: Byte) = if (contains(element)) get(indexOf(element) + 1..<size) else byteArrayOf()
/**
 * Returns a new array containing the elements starting from the specified [element], inclusive, 
 * and up to the end of the array. If the [element] is not found in the array, an empty array is returned.
 *
 * @param element The element from which the resulting array should start, including this element.
 * @since 6.1.0
 */
infix fun ByteArray.afterIncluding(element: Byte) = if (contains(element)) get(indexOf(element)..size) else byteArrayOf()
/**
 * Returns a sub-array containing elements that appear after the last occurrence 
 * of the specified element in the current array. If the element does not exist 
 * in the array, an empty array is returned.
 *
 * @param element The element whose last occurrence in the array determines the starting point of the sub-array.
 * @since 6.1.0
 */
infix fun ByteArray.afterLast(element: Byte) = if (contains(element)) get(lastIndexOf(element) + 1..<size) else byteArrayOf()
/**
 * Returns a new array containing all elements from the specified element's last occurrence (inclusive) 
 * to the end of the original array. If the specified element is not found, returns an empty array.
 *
 * @param element The element in the array from which to start the subarray, including the element itself.
 * @since 6.1.0
 */
infix fun ByteArray.afterLastIncluding(element: Byte) = if (contains(element)) get(lastIndexOf(element)..size) else byteArrayOf()

/**
 * Returns the element at the position corresponding to the given percentage 
 * within the array. The percentage is calculated relative to the size of the array.
 *
 * @param p The percentage of the array where the element should be retrieved. 
 *          Must be between 0 and 100 inclusive.
 * @return The element at the specified percentage-based position in the array.
 * @throws IndexOutOfBoundsException If the array is empty.
 * @throws IllegalArgumentException If the percentage is not in the valid range [0, 100].
 * @since 6.1.0
 */
@Deprecated("Use get operator instead", replaceWith = ReplaceWith("this[p]"))
infix fun ByteArray.percent(p: Percentage): Byte {
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
infix fun ByteArray.percentOrError(p: Percentage): Either<IndexOutOfBoundsError, Byte> = either {
    ensure(isNotEmpty()) { Empty }
    validate(p.isNotOverflowing) { "Percentage must be between 0 and 100." }
    val index = if (p.isFull) size - 1 else (p.toDouble() / 100 * size).toInt()
    catching({ this@percentOrError[index] }) { e: IndexOutOfBoundsException ->
        IndexOutOfBounds(tryOrNull { e.message.orEmpty().let { (it / Char.SPACE).second().toInt() } })
    }
}

/**
 * Creates a Map by associating each element of the array with a key-value pair produced by the given [key] and [value] transformers.
 * Each element of the array is transformed into a key-value pair where the key is the result of the [key] function
 * and the value is the result of the [value] function.
 *
 * @param key A function that returns the key for a map entry from the given element.
 * @param value A function that returns the value for a map entry from the given element.
 * @since 6.1.0
 */
inline fun <K, V> ByteArray.associate(key: Transformer<Byte, K>, value: Transformer<Byte, V>) =
    associate { key(it) to value(it) }

/**
 * Populates the given mutable map with key-value pairs generated by applying the provided
 * key and value transformers to each element of the array.
 *
 * @param destination The mutable map to be populated with the key-value pairs.
 * @param key A function that transforms an element of the array into a key.
 * @param value A function that transforms an element of the array into a value.
 * @since 6.1.0
 */
inline fun <K, V, M : MutableMap<in K, in V>> ByteArray.associateTo(
    destination: M,
    key: Transformer<Byte, K>,
    value: Transformer<Byte, V>
) = associateTo(destination) { key(it) to value(it) }

/**
 * Filters the elements of the array by applying the given transformer and retaining only the non-null results.
 * 
 * @param element A transformer function that takes an element of type [Byte] and returns a result of type [R].
 * @since 6.1.0
 */
inline infix fun <R> ByteArray.filterNotNull(element: Transformer<Byte, R>) =
    filter { element(it) != null }

/**
 * Filters the elements of the array based on a transformation function that checks for null values.
 *
 * @param element A transformation function that converts each element of the array into a result which is then checked for nullability.
 * @since 6.1.0
 */
inline infix fun <R> ByteArray.filterNull(element: Transformer<Byte, R>) =
    filter { element(it) == null }

/**
 * Iterates over the elements of the array and executes the provided block for each element.
 * The block has access to the receiver [LoopContext] and the current element, enabling
 * controlled breaking and continuation operations using the [Break] and [Continue] exceptions.
 *
 * @param block a lambda with [LoopContext] as receiver and the current element as parameter
 * used for processing each element in the array
 * @since 6.1.0
 */
@IgnorableReturnValue
inline fun ByteArray.cForEach(block: ReceiverBiConsumer<LoopContext, Byte>) = apply {
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
 * @since 6.1.0
 */
@IgnorableReturnValue
inline fun ByteArray.cForEachIndexed(block: ReceiverTriConsumer<LoopContext, Int, Byte>) = apply {
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
 * @param R the generic result type returned if a `Break` exception is thrown during iteration.
 * @param action a function that accepts a loop context and an element of type `Byte`. 
 * It contains the operation to be performed for each element.
 * @return an optional result of type `R` if the loop is interrupted by a `Break` exception, or `null` if the iteration completes normally.
 * @since 6.1.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <R> ByteArray.rForEach(action: ReceiverBiConsumer<LoopContext, Byte>): R? {
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
 * @since 6.1.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <R> ByteArray.rForEachIndexed(action: ReceiverTriConsumer<LoopContext, Int, Byte>): R? {
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