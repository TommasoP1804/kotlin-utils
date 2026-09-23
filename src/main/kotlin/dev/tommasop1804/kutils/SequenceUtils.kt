/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("SequenceUtilsKt")
@file:Suppress("unused", "kutils_collection_declaration", "kutils_take_as_int_invoke", "kutils_drop_as_int_invoke", "deprecation",
    "RETURN_VALUE_NOT_USED_COERCION"
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
import dev.tommasop1804.kutils.errors.IterableError.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Checks if the sequence is empty.
 *
 * @receiver The sequence to be checked.
 * @return `true` if the sequence contains no elements, `false` otherwise.
 */
val <E> Sequence<E>.isEmpty get() = none()
/**
 * Extension property for `Sequence` to check if it is not empty.
 *
 * This property evaluates to `true` if the sequence contains at least one element,
 * otherwise it evaluates to `false`.
 *
 * It is a shorthand for calling the `any()` function on the sequence.
 */
val <E> Sequence<E>.isNotEmpty get() = any()
/**
 * Extension property to check if a nullable [Sequence] is either null or empty.
 *
 * This property returns `true` if the sequence is null or contains no elements.
 * It returns `false` if the sequence is non-null and contains at least one element.
 *
 * Contracts are used to ensure that when this property returns `false`,
 * the sequence is guaranteed to be non-null.
 */
val <E> Sequence<E>?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || none()
}
/**
 * Checks if the sequence is not null and contains at least one element.
 *
 * @receiver The sequence to check, which can be nullable.
 * @return `true` if the sequence is not null and has at least one element, otherwise `false`.
 */
val <E> Sequence<E>?.isNotNullOrEmpty: Boolean get() {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return this != null && any()
}

/**
 * Extension property that calculates a map representing the cardinality of each element
 * in the sequence. The cardinality is the count of occurrences of each unique element.
 *
 * For a null receiver or an empty sequence, returns an empty map.
 *
 * @receiver A nullable sequence of elements whose cardinality is to be computed.
 * @return A [CountMap] where keys are elements of the sequence and values are their respective counts.
 * @since 6.1.0
 */
val <E> Sequence<E>?.cardinalityMap: CountMap<E>
    get() {
        val count = mutableMapOf<E, Int>()
        for (element in this ?: emptySequence()) {
            count[element] = count.getOrDefault(element, 0) + 1
        }
        return count
    }

/**
 * Extension property for [Sequence] that checks if all elements in the sequence are equal.
 *
 * If the sequence is empty, it is considered to have all elements equal and returns `true`.
 * Otherwise, iterates through the sequence and compares all elements to the first element.
 * If any element differs, the result is `false`.
 *
 * @receiver Sequence of elements to evaluate.
 * @return `true` if all elements in the sequence are equal or if the sequence is empty, otherwise `false`.
 * @since 6.1.0
 */
val <E> Sequence<E>.allEqual: Boolean get() {
    val iterator = iterator()
    iterator.hasNext().ifFalse { return true }
    iterator.next().let {
        while (iterator.hasNext())
            if (it != iterator.next()) return false
    }
    return true
}

/**
 * Checks if the sequence contains any duplicate elements.
 *
 * The property evaluates to `true` if there are elements in the sequence
 * that appear more than once. Otherwise, it evaluates to `false`.
 *
 * This property creates a map to count the occurrences of each element
 * in the sequence and determines if any of the counts exceed 1.
 * @since 6.1.0
 */
val <E> Sequence<E>.containsDuplicates
    get() = cardinalityMap.any { it.value > 1 }

/**
 * Determines whether the sequence contains exactly one element.
 *
 * @return `true` if the sequence has exactly one element, otherwise `false`.
 * @since 1.0.0
 */
val  <E> Sequence<E>.isSingleElement: Boolean get() = singleOrNull() != null
/**
 * Checks if the sequence does not contain exactly one element.
 *
 * This function determines whether the sequence either has no elements
 * or contains more than one element.
 *
 * @return `true` if the sequence does not contain exactly one element,
 * `false` if it contains exactly one element.
 * @since 1.0.0
 */
val <E> Sequence<E>.isNotSingleElement: Boolean get() = !isSingleElement

/**
 * Returns `null` if the sequence is empty; otherwise, returns the sequence itself.
 *
 * This function allows safely returning `null` for empty sequences instead of
 * continuing to operate on an empty sequence.
 *
 * @receiver The sequence to check for emptiness.
 * @return `null` if the sequence is empty; the sequence itself otherwise.
 * @since 5.4.0
 */
fun <S : Sequence<E>, E> S.orNullIfEmpty() = if (isEmpty) null else this

/**
 * Returns an `Either` instance based on whether is empty or not.
 *
 * If is empty, the method returns a `Left` containing an `IterableError.Empty` value.
 * Otherwise, it returns a `Right` containing the original object.
 *
 * @return An `Either` instance where:
 *         - `Left` indicates that the array is empty.
 *         - `Right` contains the array when it is not empty.
 * @since 6.1.0
 */
fun <E> Sequence<E>.orErrorIfEmpty(): Either<Empty, Sequence<E>> =
    either { ifEmpty { raise(Empty) } }

/**
 * Merges the current sequence with additional sequences and returns the resulting sequence.
 * 
 * If the current sequence is null or empty and no additional sequences are provided, 
 * it returns an empty sequence. If additional sequences are provided, merges them iteratively.
 *
 * @param sequences Additional sequences to be merged with the current sequence.
 * @return A merged sequence containing elements from the current sequence and the additional sequences.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <T: Sequence<E>, E> T?.merge(vararg sequences: Sequence<E>): T {
    return when {
        sequences.isEmpty() && (this == null || none()) -> emptySequence<E>() as T
        sequences.isEmpty() -> this as T
        this == null || none() -> sequences.first().merge(*sequences.drop(1).toTypedArray()) as T
        else -> sequences.fold(this) { acc, sequence -> acc.plus(sequence) as T }
    }
}

/**
 * Checks if the sequence contains any of the specified elements.
 *
 * This function evaluates lazily and stops as soon as any element in the sequence
 * matches one of the provided elements. The function uses the `in` operator internally
 * to check membership.
 *
 * @param elements the elements to be checked against the sequence.
 * @since 1.0.0
 */
fun <E> Sequence<E>.containsAny(vararg elements: E) = any { it in elements }

/**
 * Checks if none of the elements in the sequence are present in the specified elements.
 *
 * @param elements The elements to check against the sequence.
 * @return `true` if none of the elements in the sequence are present in the specified elements, `false` otherwise.
 * @since 1.0.0
 */
fun <E> Sequence<E>.containsNone(vararg elements: E) = none { it in elements }

/**
 * Checks if any element in the sequence satisfies the given predicate.
 *
 * @param predicate A lambda function that takes an element of the sequence and returns true if the condition is met, false otherwise.
 * @since 1.1.0
 */
operator fun <E> Sequence<E>.contains(predicate: Predicate<E>) = any { predicate(it) }

/**
 * Returns a sequence containing all elements of the original sequence except those that match the given predicate.
 *
 * @param filterNot The predicate used to test elements for exclusion from the resulting sequence.
 * @return A sequence with elements that do not match the specified predicate.
 * @since 3.5.2
 */
operator fun <E> Sequence<E>.minus(filterNot: Predicate<E>) = filterNot(filterNot)

/**
 * Splits a sequence into chunks where each chunk ends when the given predicate evaluates to true for an element.
 *
 * @param predicate A function that takes an element of the sequence and returns true when a chunk should end.
 * @return A sequence of lists, where each list represents a chunk of the original elements partitioned by the predicate.
 * @since 1.0.0
 */
fun <E> Sequence<E>.chunkedWhile(predicate: Predicate<E>): Sequence<List<E>> = run {
    val result = mutableListOf<List<E>>()
    var current = mutableListOf<E>()
    for (e in this) {
        current += e
        if (predicate(e)) {
            result.add(current)
            current = mutableListOf()
        }
    }
    if (current.isNotEmpty()) result.add(current)
    return result.asSequence()
}

/**
 * Repeats each element of the sequence a specified number of times and returns the resulting sequence.
 *
 * @param n The number of times each element of the sequence should be repeated. Must be a non-negative integer.
 * @since 1.0.0
 */
infix fun <E> Sequence<E>.repeatEach(n: Int) = flatMap { e -> e.asSingleList() * n }

/**
 * Returns the first element of the sequence or the result of the provided default supplier
 * if the sequence is empty.
 *
 * @param default A supplier function that provides a default value if the sequence is empty.
 * @return The first element of the sequence, or the result of the `default` supplier if the sequence is empty.
 * @since 6.1.0
 */
fun <E> Sequence<E>.firstOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { first() } catch (e: NoSuchElementException) { default() }
}
/**
 * Returns the first element of the sequence or throws the exception supplied by the given [lazyException].
 *
 * @param lazyException A lambda function that supplies the exception to be thrown if the sequence is empty.
 * @return The first element of the sequence.
 * @throws Throwable The exception provided by the [lazyException] supplier if the sequence is empty.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Sequence<E>.firstOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { first() } catch (e: NoSuchElementException) { throw lazyException() }
}
/**
 * Retrieves the first element of the sequence, or raises an error if the sequence is empty.
 *
 * This extension function is a utility to safely access the first element in a sequence
 * while providing an error-handling mechanism in case the sequence does not contain any elements.
 * The function will attempt to return the first element and will raise an `Empty` error
 * when a `NoSuchElementException` is encountered.
 *
 * @receiver The sequence from which the first element is to be retrieved.
 * @return An `Either` instance where:
 *         - `Right<E>` represents the first element if the sequence is not empty.
 *         - `Left<Empty>` represents an error indicating that the sequence is empty.
 * @throws Throwable If any other unexpected exception occurs during execution.
 *
 * @since 6.1.0
 */
fun <E> Sequence<E>.firstOrError() = either {
    catching({ first() }) { _: NoSuchElementException -> Empty }
}
/**
 * Finds the first element in the sequence that matches the given predicate.
 *
 * This method evaluates the sequence to identify the first element that satisfies
 * the provided predicate. If the sequence is empty, a `NoSuchElementException` is thrown.
 * If no elements satisfy the predicate, a `NoResultsException` is thrown.
 *
 * @param predicate The condition used to filter the elements of the sequence.
 * @return The first element in the sequence that matches the predicate.
 * @throws NoSuchElementException If the sequence is empty.
 * @throws NoResultsException If no elements satisfy the given predicate.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Sequence<E>.findFirst(predicate: Predicate<E>): E {
    val list = toList()
    if (list.isEmpty()) throw NoSuchElementException()
    val filtered = list.filter(predicate)
    if (filtered.isEmpty()) throw NoResultsException()
    return filtered.first()
}
/**
 * Returns the first element in the sequence that matches the given predicate.
 * If no such element is found, the value provided by the default supplier is returned instead.
 *
 * @param default A supplier function that provides the default value to return if no element matches the predicate.
 * @param predicate A condition used to test elements in the sequence.
 * @return The first element matching the predicate, or the value provided by the default supplier if no such element is found.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findFirstOr(default: Supplier<E>, predicate: Predicate<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { first(predicate) } catch (e: NoSuchElementException) { default() }
}
/**
 * Returns the first element in the sequence that matches the given [predicate].
 * If no elements match, a lazily created exception from [lazyException] is thrown.
 *
 * @param lazyException a supplier that provides an exception to be thrown if no element matches the predicate
 * @param predicate a condition that the elements in the sequence are tested against
 * @return the first element that matches the given [predicate]
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Sequence<E>.findFirstOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { first(predicate) } catch (e: NoSuchElementException) { throw lazyException() }
}
/**
 * Attempts to find the first element in the sequence that satisfies a given predicate and returns it as a successful result wrapped in a `Right`.
 * If no element matches the predicate or the sequence is empty, an error represented by `Left` is returned.
 *
 * @param E The type of the elements in the sequence.
 * @param predicate A predicate used to test each element in the sequence.
 * @return An `Either` instance where:
 *         - `Right<E>` represents the first element that satisfies the predicate.
 *         - `Left<NotFirstResultErrors>` represents an error if the sequence is empty or no elements match the predicate.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findFirstOrError(predicate: Predicate<E>): Either<NotFirstResultError, E> = either {
    catching({ findFirst(predicate) }) { e: Exception -> when (e) {
        is NoResultsException -> NoResults
        is NoSuchElementException -> Empty
        else -> throw IllegalStateException()
    } }
}

/**
 * Returns the last element of the sequence, or the value provided by the given supplier if the sequence is empty.
 *
 * @param default A supplier function that provides a default value if the sequence is empty.
 * @return The last element if the sequence has values, or the value provided by the supplier if the sequence is empty.
 * @since 6.1.0
 */
fun <E> Sequence<E>.lastOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { last() } catch (_: NoSuchElementException) { default() }
}
/**
 * Returns the last element of the sequence or throws the exception provided by the given [lazyException]
 * if the sequence is empty.
 *
 * @param lazyException A supplier function that provides the exception to be thrown when the sequence is empty.
 * The supplier is invoked at most once.
 * @return The last element of the sequence.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Sequence<E>.lastOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { last() } catch (_: NoSuchElementException) { throw lazyException() }
}
/**
 * Returns the last element of a sequence as a successful result within an `Either` context
 * or raises an error if the sequence is empty.
 *
 * This extension function utilizes the `either` scope to provide a functional approach
 * for handling empty sequences. If the sequence contains elements, the last element is
 * returned wrapped in a successful `Right`. If the sequence is empty, an error of type `Empty`
 * is raised and wrapped in a `Left`.
 *
 * @receiver The sequence for which the last element is to be obtained.
 * @return An `Either` value:
 *         - `Right<E>` containing the last element if the sequence is not empty.
 *         - `Left<Empty>` containing an error if the sequence is empty.
 * @throws Throwable If an exception other than `NoSuchElementException` occurs during execution.
 * @since 6.1.0
 */
fun <E> Sequence<E>.lastOrError() = either {
    catching({ last() }) { _: NoSuchElementException -> Empty }
}
/**
 * Finds the last element in a sequence that matches the given predicate.
 *
 * This method evaluates the sequence to find the last element that satisfies the specified
 * predicate. If no elements in the sequence match the predicate, a `NoResultsException`
 * is thrown. If the sequence is empty, a `NoSuchElementException` is thrown.
 *
 * @param E the type of elements in the sequence.
 * @param predicate a condition that each element is tested against.
 * @return the last element in the sequence that matches the predicate.
 * @throws NoResultsException if no elements match the predicate.
 * @throws NoSuchElementException if the sequence is empty.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Sequence<E>.findLast(predicate: Predicate<E>): E {
    val list = toList()
    if (list.isEmpty()) throw NoSuchElementException()
    val filtered = list.filter(predicate)
    if (filtered.isEmpty()) throw NoResultsException()
    return filtered.last()
}
/**
 * Finds the last element in the sequence that matches the given predicate.
 * If no element matches, the default value supplied by the provided supplier is returned.
 *
 * @param default A supplier that provides the default value to return if no matching element is found.
 * @param predicate A predicate function to test each element for a condition.
 * @return The last element matching the predicate, or the value supplied by the default supplier if no such element exists.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findLastOr(default: Supplier<E>, predicate: Predicate<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return try { last(predicate) } catch (_: NoSuchElementException) { default() }

}
/**
 * Finds the last element in the sequence that matches the given predicate or throws an exception
 * supplied by the provided lazy exception supplier if no such element is found.
 *
 * @param lazyException a supplier that provides the exception to be thrown if no matching element is found
 * @param predicate a predicate to test elements for a condition
 * @return the last element in the sequence that matches the predicate
 * @throws Throwable the exception provided by the lazy exception supplier if no matching element is found
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Sequence<E>.findLastOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return try { last(predicate) } catch (_: NoSuchElementException) { throw lazyException() }
}
/**
 * Finds the last element in the sequence that matches the given predicate or returns an error
 * wrapped in an `Either` if no matching element is found or an exception occurs.
 *
 * This method uses a functional approach to handle errors through the `Either` type, where:
 * - `Left` contains a specific error detailing what went wrong.
 * - `Right` contains the found element if a matching one exists.
 *
 * @param E The type of elements in the sequence.
 * @param predicate The predicate to apply to elements of the sequence to determine the match.
 * @return An `Either` instance where:
 *         - `Left` contains a `NotLastResultsErrors` indicating the specific error (e.g., `NoResults` or `Empty`).
 *         - `Right` contains the last element that matches the predicate.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findLastOrError(predicate: Predicate<E>): Either<NotLastResultsError, E> = either {
    catching({ findLast(predicate) }) { e: Exception -> when (e) {
        is NoResultsException -> NoResults
        is NoSuchElementException -> Empty
        else -> throw IllegalStateException()
    } }
}

/**
 * Returns the second element of the sequence.
 *
 * @return the second element of this sequence
 * @throws NoSuchElementException if the sequence contains fewer than two elements
 * @since 6.1.0
 */
fun <E> Sequence<E>.second(): E {
    val iterator = iterator()
    kotlin.repeat(1) {
        if (!iterator.hasNext()) throw NoSuchElementException("Sequence doesn't allow to get second element.")
        iterator.next()
    }
    if (!iterator.hasNext()) throw NoSuchElementException("Sequence doesn't allow to get second element.")
    return iterator.next()
}
/**
 * Returns the second element of the sequence or `null` if the sequence contains fewer than two elements.
 *
 * This function iterates through the elements of the sequence to determine the second element if it exists.
 *
 * @return The second element of the sequence, or `null` if the sequence contains fewer than two elements.
 * @since 6.1.0
 */
fun <E> Sequence<E>.secondOrNull(): E? {
    val iterator = iterator()
    kotlin.repeat(1) {
        if (!iterator.hasNext()) return null
        iterator.next()
    }
    return if (iterator.hasNext()) iterator.next() else null
}
/**
 * Returns the second element of the sequence if it exists, or the result of the provided default supplier if the sequence has fewer than two elements.
 *
 * @param default A supplier function that provides a fallback value if the sequence has fewer than two elements.
 * @return The second element of the sequence, or the result of the default supplier if the sequence contains fewer than two elements.
 * @since 6.1.0
 */
fun <E> Sequence<E>.secondOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    val iterator = iterator()
    kotlin.repeat(1) {
        if (!iterator.hasNext()) return default()
        iterator.next()
    }
    return if (iterator.hasNext()) iterator.next() else default()
}
/**
 * Returns the second element of the sequence or throws the exception provided by [lazyException]
 * if the sequence contains fewer than two elements.
 *
 * This method allows for deferred exception creation by using a supplier, which is invoked only
 * when the exception needs to be thrown.
 *
 * @param E the type of elements in the sequence
 * @param lazyException a supplier function to create the exception to throw if the sequence
 * contains fewer than two elements
 * @return the second element of the sequence
 * @throws Throwable the exception created and thrown by [lazyException] when the sequence contains fewer than two elements
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Sequence<E>.secondOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    val iterator = iterator()
    kotlin.repeat(1) {
        if (!iterator.hasNext()) throw lazyException()
        iterator.next()
    }
    if (!iterator.hasNext()) throw lazyException()
    return iterator.next()
}
/**
 * Returns the second element in the sequence as an `Either` type.
 *
 * If the sequence contains at least two elements, the second element is returned as a `Right<E>`.
 * If the sequence is empty, or it does not have a second element, an error of type `NotInnerElementErrors`
 * is returned as a `Left`.
 *
 * @return An `Either` instance where:
 *         - `Right<E>` contains the second element of the sequence if it is present.
 *         - `Left<NotInnerElementErrors>` contains an appropriate error:
 *              - `Empty` if the sequence is empty.
 *              - `NoSuchElement(1)` if second element is not found.
 * @since 6.1.0
 */
fun <E> Sequence<E>.secondOrError(): Either<NotInnerElementError, E> = either {
    catching({ second() }) { _: NoSuchElementException -> if (isEmpty) Empty else NoSuchElement(1) }
}
/**
 * Finds the second element in the sequence that satisfies the specified predicate.
 *
 * This method filters the sequence based on the provided predicate and returns
 * the second element from the filtered results. If the sequence is empty, does
 * not contain elements matching the predicate, or has fewer than two matching
 * elements, an exception is thrown.
 *
 * @param E The type of elements in the sequence.
 * @param predicate The condition to apply to filter elements in the sequence.
 * @return The second element in the sequence that satisfies the given predicate.
 * @throws NoSuchElementException If the sequence is empty.
 * @throws NoResultsException If no elements match the provided predicate.
 * @throws TooFewResultsException If fewer than two elements match the predicate.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Sequence<E>.findSecond(predicate: Predicate<E>): E {
    if (isEmpty) throw NoSuchElementException()
    val filtered = filter(predicate)
    if (filtered.isEmpty) throw NoResultsException()
    return try { filtered.second() } catch (e: NoSuchElementException) { throw TooFewResultsException() }
}
/**
 * Finds the second element in the sequence that matches the given predicate or returns `null`
 * if no such element is found or if an exception occurs during execution.
 *
 * This function invokes [findSecond], wraps its execution inside [tryOrNull], and catches any relevant
 * exception as per the configuration of [tryOrNull]. If an exception is caught, `null` is returned.
 *
 * @param E The type of elements in the sequence.
 * @param predicate A predicate function used to determine the element to match.
 * @return The second element matching the predicate or `null` if no such element is found or an exception occurs.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findSecondOrNull(predicate: Predicate<E>) = tryOrNull { findSecond(predicate) }
/**
 * Searches for the second element in the sequence that matches the given predicate.
 * If no such element exists, the provided default supplier is invoked to produce a fallback result.
 * This function gracefully handles exceptions that might occur during the operation using a fallback logic.
 *
 * @param E The type of the elements in the sequence.
 * @param default A supplier function that provides a default value if the required second matching element is not found.
 * @param predicate A predicate function that determines whether an element in the sequence matches the criteria.
 * @return The second element that satisfies the specified predicate, or the result of the default supplier if no such element exists.
 * @throws TooFewResultsException If fewer than two elements in the sequence match the predicate.
 * @throws NoSuchElementException If the sequence is empty or contains no elements that match the predicate.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findSecondOr(default: Supplier<E>, predicate: Predicate<E>) = tryOr({ default() }) { findSecond(predicate) }
/**
 * Finds the second element in the sequence that satisfies the given predicate or throws an exception
 * provided by the `lazyException` supplier if no such element is found or if any exception occurs during the search.
 *
 * @param E the type of elements in the sequence.
 * @param lazyException a supplier function that provides the exception to throw if no second element is found.
 * @param predicate a function that determines whether an element satisfies the condition.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Sequence<E>.findSecondOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>) =
    try { findSecond(predicate) }
    catch (e: Exception) { throw lazyException() }
/**
 * Attempts to find the second element in the sequence that matches the provided predicate.
 * If the element is found successfully, it returns the element wrapped in a `Right` value.
 * If an error occurs during the process (e.g., no elements match, or there are too few elements),
 * it returns an error wrapped in a `Left` value.
 *
 * @param E The type of elements in the sequence.
 * @param predicate A predicate function to test each element of the sequence.
 * @return An `Either` where:
 *         - `Right<E>` contains the second matched element, on success.
 *         - `Left<NotInnerResultErrors>` contains an error, if no results, too few results, or an exception occurs.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findSecondOrError(predicate: Predicate<E>): Either<NotInnerResultError, E> = either {
    catching({ findSecond(predicate) }) { e: Exception -> when (e) {
        is NoResultsException -> NoResults
        is NoSuchElementException -> Empty
        is TooFewResultsException -> TooFewResults
        else -> throw IllegalStateException()
    } }
}

/**
 * Returns the third element of the sequence.
 *
 * @return The third element in the sequence.
 * @throws NoSuchElementException If the sequence contains fewer than three elements.
 * @since 6.1.0
 */
fun <E> Sequence<E>.third(): E {
    val iterator = iterator()
    kotlin.repeat(2) {
        if (!iterator.hasNext()) throw NoSuchElementException("Sequence doesn't allow to get third element.")
        iterator.next()
    }
    if (!iterator.hasNext()) throw NoSuchElementException("Sequence doesn't allow to get third element.")
    return iterator.next()
}
/**
 * Returns the third element in the sequence or `null` if the sequence contains fewer than three elements.
 *
 * @return The third element of the sequence, or `null` if there are fewer than three elements.
 * @since 6.1.0
 */
fun <E> Sequence<E>.thirdOrNull(): E? {
    val iterator = iterator()
    kotlin.repeat(2) {
        if (!iterator.hasNext()) return null
        iterator.next()
    }
    return if (iterator.hasNext()) iterator.next() else null
}
/**
 * Returns the third element of the sequence or the value provided by the given default supplier
 * if the sequence contains fewer than three elements.
 *
 * @param default A supplier function providing a default value if the sequence has fewer than three elements.
 * @return The third element of the sequence, or the value from the default supplier if the sequence has less than three elements.
 * @since 6.1.0
 */
fun <E> Sequence<E>.thirdOr(default: Supplier<E>): E {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    val iterator = iterator()
    kotlin.repeat(2) {
        if (!iterator.hasNext()) return default()
        iterator.next()
    }
    return if (iterator.hasNext()) iterator.next() else default()
}
/**
 * Returns the third element of the sequence if it exists; otherwise, throws an exception
 * provided by the given `lazyException` supplier.
 *
 * @param lazyException a lambda or supplier that provides the exception to be thrown
 *                       if the sequence contains fewer than three elements.
 * @return the third element of the sequence.
 * @throws Throwable if the sequence has fewer than three elements.
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Sequence<E>.thirdOrThrow(lazyException: ThrowableSupplier): E {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    val iterator = iterator()
    kotlin.repeat(2) {
        if (!iterator.hasNext()) throw lazyException()
        iterator.next()
    }
    if (!iterator.hasNext()) throw lazyException()
    return iterator.next()
}
/**
 * Retrieves the third element from a sequence if it exists or returns an error wrapped in an `Either`.
 *
 * This method attempts to fetch the third element of the sequence.
 * If the sequence is empty, it raises an `Empty` error as a `Left`.
 * If the third element does not exist, it raises a `NoSuchElement` error with the corresponding index.
 *
 * @return An `Either` value where:
 *         - `Right<E>` contains the third element if it exists.
 *         - `Left<NotInnerElementErrors>` contains an error indicating why the third element could not be retrieved.
 * @since 6.1.0
 */
fun <E> Sequence<E>.thirdOrError(): Either<NotInnerElementError, E> = either {
    catching({ third() }) { _: NoSuchElementException -> if (isEmpty) Empty else NoSuchElement(2) }
}
/**
 * Returns the third element in the sequence that matches the given predicate.
 *
 * This method filters the sequence based on the provided predicate. If there are fewer
 * than three elements matching the predicate, or if the sequence is empty, exceptions
 * are thrown to indicate the issue.
 *
 * @param E the type of elements in the sequence.
 * @param predicate a condition used to filter the elements of the sequence.
 * @return the third element that satisfies the given predicate.
 * @throws NoSuchElementException if the sequence is empty.
 * @throws NoResultsException if no elements satisfy the predicate.
 * @throws TooFewResultsException if fewer than three elements satisfy the predicate.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findThird(predicate: Predicate<E>): E {
    if (isEmpty) throw NoSuchElementException()
    val filtered = filter(predicate)
    if (filtered.isEmpty) throw NoResultsException()
    return try { filtered.third() } catch (e: Exception) { throw TooFewResultsException() }
}
/**
 * Finds the third element in the sequence that matches the specified predicate, or returns `null`
 * if no such element exists or an exception occurs during the process.
 *
 * The search is conducted using the provided predicate and handles exceptions internally. Exceptions
 * like `NoSuchElementException`, `NoResultsException`, or `TooFewResultsException` will result in
 * the method returning `null` rather than propagating the exception.
 *
 * @param predicate A condition used to filter the sequence elements. Only the elements satisfying
 * the predicate will be considered.
 * @return The third matching element of the sequence if it exists, or `null` if no match is found
 * or if an exception occurs.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findThirdOrNull(predicate: Predicate<E>) = tryOrNull { findThird(predicate) }
/**
 * Attempts to find the third element in a sequence that satisfies the given predicate.
 * If no such element exists, the provided default value is returned instead.
 *
 * @param E the type of elements in the sequence.
 * @param default a fallback supplier function that provides a value to return if no third matching element exists in the sequence.
 * @param predicate the condition that elements in the sequence must satisfy in order to be considered.
 * @return the third element in the sequence that satisfies the predicate, or the value provided by the fallback supplier if no such element exists.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findThirdOr(default: Supplier<E>, predicate: Predicate<E>) = tryOr({ default() }) { findThird(predicate) }
/**
 * Finds the third element in the sequence that matches the given predicate or throws a provided exception if the
 * condition is not met. This method applies the [predicate] to the elements of the sequence in order and
 * returns the third matching element. If the sequence does not contain at least three elements matching the
 * predicate, it throws the result of invoking [lazyException].
 *
 * @param lazyException a supplier of the exception to be thrown when the sequence does not contain at least three matching elements.
 * @param predicate a condition to filter the elements of the sequence.
 * @throws Throwable if the sequence does not contain at least three matching elements as supplied by [lazyException].
 * @since 6.1.0
 */
@IgnorableReturnValue
fun <E> Sequence<E>.findThirdOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>) =
    try { findThird(predicate) }
    catch (e: Exception) { throw lazyException() }
/**
 * Finds the third element in the sequence that matches the given predicate.
 * If no such element exists, it raises an error wrapped in an `Either` type.
 *
 * The function uses the `either` construct to handle potential errors, such as:
 * - `NoResultsException`, when the sequence contains no results matching the predicate.
 * - `NoSuchElementException`, when the sequence is empty.
 * - `TooFewResultsException`, when fewer than three elements match the predicate.
 *
 * @param E The type of elements in the sequence.
 * @param predicate A predicate function used to identify elements in the sequence.
 * @return An `Either` instance where:
 *         - `Left<NotInnerResultErrors>` represents an error encountered during execution.
 *         - `Right<E>` contains the third element satisfying the predicate.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findThirdOrError(predicate: Predicate<E>): Either<NotInnerResultError, E> = either {
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
fun <E> Sequence<E>.onlyElement(): E {
    if (isEmpty) throw NoSuchElementException()
    else return try { single() } catch (e: IllegalArgumentException) { throw TooManyElementsException() }
}
/**
 * Returns the single element in the iterables if it contains exactly one element,
 * or `null` if the iterables is empty or contains more than one element.
 *
 * @receiver an iterables of elements
 * @return the single element in the iterables or `null` if the conditions are not met
 * @since 1.0.0
 */
fun <E> Sequence<E>.onlyElementOrNull() = singleOrNull()
/**
 * Returns the single element in the iterables if it contains only one element; otherwise,
 * it returns the value supplied by the provided [default] supplier.
 *
 * @param E the type of elements in the iterables
 * @param default a supplier that provides a default value if the iterables does not contain exactly one element
 * @return the single element in the iterables if there is exactly one, or the value supplied by [default]
 * @since 1.0.0
 */
infix fun <E> Sequence<E>.onlyElementOr(default: Supplier<E>) = tryOr({ default() }) { onlyElement() }
/**
 * Returns the single element of the iterables if it contains exactly one element; otherwise, throws an exception provided by the given supplier.
 *
 * @param lazyException a supplier function that provides the exception to be thrown when the iterables does not contain exactly one element
 * @return the single element of the iterables if there is exactly one element
 * @throws Throwable the exception supplied by the `lazyException` if the iterables does not contain exactly one element
 * @since 1.0.0
 */
@IgnorableReturnValue
infix fun <E> Sequence<E>.onlyElementOrThrow(lazyException: ThrowableSupplier) =
    try { onlyElement() } catch (e: Exception) { throw lazyException() }
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
fun <E> Sequence<E>.onlyElementOrError(): Either<NotOnlyElementError, E> = either {
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
infix fun <E> Sequence<E>.findOnlyElement(predicate: Predicate<E>): E {
    if (isEmpty) throw NoSuchElementException()
    filter(predicate).run {
        if (isEmpty) throw NoResultsException()
        else return try { single() } catch (e: IllegalArgumentException) { throw TooManyResultsException() }
    }
}
/**
 * Returns the single element matching the given [predicate], or `null` if no such element exists
 * or if there is more than one matching element in the iterables.
 *
 * @param predicate A lambda function used to filter elements in the iterables. The function should
 * return `true` for elements you want to include in the operation.
 * @since 6.1.0
 */
infix fun <E> Sequence<E>.findOnlyElementOrNull(predicate: Predicate<E>) = tryOrNull { findOnlyElement(predicate) }
/**
 * Returns the single element that matches the given predicate if exactly one element matches,
 * otherwise returns the result from the default supplier.
 *
 * @param default A supplier function that provides a default value when no element
 * or more than one element matches the predicate.
 * @param predicate A predicate to filter the elements in the iterables.
 * @since 6.1.0
 */
fun <E> Sequence<E>.findOnlyElementOr(default: Supplier<E>, predicate: Predicate<E>) = tryOr({ default() }) { findOnlyElement(predicate) }
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
fun <E> Sequence<E>.findOnlyElementOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<E>) =
    try { findOnlyElement(predicate) }
    catch (e: Exception) { throw lazyException() }
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
fun <E> Sequence<E>.findOnlyElementOrError(predicate: Predicate<E>): Either<NotOnlyResultError, E> = either {
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
 * Returns the original sequence if it is neither null nor empty; otherwise, it invokes the
 * specified [defaultValue] supplier and returns its result.
 *
 * This function uses a contract to ensure that the [defaultValue] supplier is invoked at most once.
 *
 * @param defaultValue a supplier function that provides a replacement sequence in case the
 *                     original sequence is null or empty
 * @return the original sequence if it's not null or empty; otherwise, the result of the [defaultValue] supplier
 * @since 1.0.0
 */
inline infix fun <S : Sequence<E>, E> S?.ifNullOrEmpty(defaultValue: Supplier<S>): S {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
    }
    return if (isNullOrEmpty) defaultValue() else this
}
/**
 * Executes the given [action] on the sequence if it is not empty.
 *
 * This function allows performing an operation on a sequence only when it contains
 * at least one element, without affecting the sequence itself.
 *
 * @param action A consumer function to be invoked with the sequence if it is not empty.
 * @return The original sequence, regardless of whether the action was invoked.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> Sequence<E>.ifNotEmpty(action: Consumer<Sequence<E>>): Sequence<E> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNotEmpty) action(this)
    return this
}
/**
 * Invokes the given action with the current sequence if it is not null and contains at least one element.
 *
 * @param action A function to be executed with the sequence if it is not null or empty.
 * @return The original sequence, or `null` if the sequence is null.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E, R> Sequence<E>?.ifNotNullOrEmpty(action: Consumer<Sequence<E>>): Sequence<E>? {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        (this@ifNotNullOrEmpty != null) holdsIn action
        (this@ifNotNullOrEmpty != null) implies returnsNotNull()
    }
    if (isNotNullOrEmpty) action(this)
    return this
}

/**
 * Executes the specified action if the sequence contains the given element.
 *
 * @param element the element to be checked for in the sequence.
 * @param action the action to be executed if the element is found in the sequence.
 * @return the original sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> Sequence<E>.ifContains(element: E, action: Consumer<Sequence<E>>): Sequence<E> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (contains(element)) action(this)
    return this
}
/**
 * Executes the specified action if the sequence does not contain the given element.
 *
 * @param element The element to check for in the sequence.
 * @param action The action to perform if the element is not found in the sequence.
 * @return The original sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> Sequence<E>.ifNotContains(element: E, action: Consumer<Sequence<E>>): Sequence<E> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (!contains(element)) action(this)
    return this
}

/**
 * Invokes the given [action] if the sequence contains exactly one element.
 *
 * @param action the action to perform on the single element of the sequence
 * @return the sequence itself
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> Sequence<E>.ifSingleElement(action: Consumer<Sequence<E>>): Sequence<E> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isSingleElement) action(this)
    return this
}

/**
 * Filters the elements of the sequence, returning every `step`th element starting from the first.
 *
 * @param step The step interval to filter the sequence by. Must be greater than 0.
 * @since 6.1.0
 */
infix fun <E> Sequence<E>.step(step: Int) = filterIndexed { index, c -> index % step == 0 }

/**
 * Checks if the sequence is null or empty. The operator function `not` is used as a negation
 * to verify whether the sequence contains any elements or exists at all.
 *
 * @return `true` if the sequence is either null or empty, `false` otherwise.
 * @since 1.0.0
 */
@Suppress("kutils_null_check")
@OptIn(ExperimentalContracts::class)
@JvmName("nullableNot")
operator fun <E> Sequence<E>?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty
}
/**
 * Extension operator function that returns whether a sequence is empty.
 *
 * The `not` operator is used as an alternative way to check if the sequence contains no elements.
 * This function evaluates to `true` if the sequence is empty, and `false` otherwise.
 *
 * @receiver Sequence to be checked for emptiness.
 * @return `true` if the sequence is empty, `false` otherwise.
 * @since 5.0.0
 */
operator fun <E> Sequence<E>.not() = isEmpty

/**
 * Repeats the elements of the sequence the specified number of times.
 *
 * The operator takes an integer parameter and creates a new sequence by
 * repeating the elements of the original sequence the specified number of times.
 *
 * @param n The number of times the sequence elements should be repeated. Must be non-negative.
 * @return A sequence containing the elements of the original sequence repeated `n` times.
 * @since 6.1.0
 */
operator fun <E> Sequence<E>.times(n: Int) = (1..n).flatMap { this }.asSequence()

/**
 * Divides the elements of a sequence into two groups based on a given predicate.
 * The result is a pair of lists, where the first list contains elements
 * that satisfy the predicate, and the second list contains elements that do not.
 *
 * @param predicate The condition to partition the elements of the sequence.
 * @return A pair of lists where the first list contains elements satisfying the predicate
 *         and the second list contains the remaining elements.
 * @since 1.0.0
 */
operator fun <E> Sequence<E>.div(predicate: Predicate<E>) = partition(predicate)
/**
 * Splits the elements of the sequence into lists each not exceeding the specified chunk size.
 *
 * The resulting sequence contains lists which group the original sequence's elements.
 * Each list will have a size of at most `chunkSize`. If the number of elements in
 * the sequence is not evenly divisible by `chunkSize`, then the last list in the resulting
 * sequence will contain the remaining elements.
 *
 * @param chunkSize the maximum number of elements in each chunked list.
 * @return a sequence of lists, where each list contains at most `chunkSize` elements.
 * @since 1.0.0
 */
operator fun <E> Sequence<E>.rem(chunkSize: Int): Sequence<List<E>> = chunked(chunkSize)
/**
 * Applies a partitioning operation on the sequence, grouping its elements into continuous chunks
 * that satisfy or do not satisfy the given predicate. The resulting sequence contains lists of elements,
 * where each list corresponds to a chunk determined by the changes in the predicate's condition.
 *
 * @param predicate a function that determines the condition applied to each element in the sequence.
 * @return a sequence of lists, where each list is a chunk of elements based on the predicate.
 * @since 1.0.0
 */
operator fun <E> Sequence<E>.rem(predicate: Predicate<E>): Sequence<List<E>> = chunkedWhile(predicate)

/**
 * Applies the given predicate function to filter the elements of the sequence.
 *
 * This operator function allows applying a custom filtering logic encapsulated in the `Predicate`.
 *
 * @param filter A predicate function that determines whether an element should be included in the resulting sequence.
 * @return A sequence containing only the elements that match the provided predicate.
 * @since 1.1.1
 */
inline operator fun <E> Sequence<E>.invoke(crossinline filter: Predicate<E>): Sequence<E> = filter { filter(it) }

/**
 * Extension operator function for `Sequence` that retrieves the first element satisfying the provided predicate.
 *
 * @param find A lambda function or predicate that is applied to each element of the sequence to determine the desired element.
 * @return The first element that matches the predicate, or `null` if no such element is found.
 * @since 1.1.1
 */
inline operator fun <E> Sequence<E>.get(find: Predicate<E>): E? = find { find(it) }
/**
 * Finds the first element in a sequence that satisfies the given predicate, or throws a provided exception if no such element is found.
 *
 * @param find a predicate that evaluates each element of the sequence to determine if it matches the condition.
 * @param lazyException a supplier for the exception to be thrown if no element satisfies the given predicate.
 * @return the first element in the sequence that satisfies the predicate.
 * @since 1.1.1
 */
inline operator fun <E> Sequence<E>.get(find: Predicate<E>, lazyException: ThrowableSupplier): E = find { find(it) } ?: throw lazyException()

/**
 * Retrieves a subsequence of elements from the original sequence within the specified range.
 *
 * The method drops elements before the start of the range (inclusive) and
 * takes elements up to the end of the range (exclusive) from the sequence.
 *
 * @param T the type of elements in the sequence
 * @param range the range specifying the indices to extract; the start is inclusive, and the end is exclusive
 * @return a sequence containing the elements within the specified range
 * @since 1.0.0
 */
operator fun <T> Sequence<T>.get(range: IntProgression) = filterIndexed { index, _ -> index in range }

/**
 * Retrieves an element at the specified index from the sequence.
 *
 * This function skips the specified number of elements in the sequence and then
 * returns the first element encountered. It is useful for random access in a sequence.
 *
 * Note that using this function on a sequence is not efficient for large indices, as it
 * involves iterating through elements sequentially until the specified position is reached.
 *
 * @param int The zero-based index of the element to retrieve from the sequence.
 * @return The element at the specified position in the sequence.
 * @throws IndexOutOfBoundsException If the specified index is beyond the bounds of the sequence.
 * @since 1.0.0
 */
operator fun <T> Sequence<T>.get(int: Int) = try { drop(int).first() } catch (e: NoSuchElementException) { throw IndexOutOfBoundsException("For index $int") }

/**
 * Retrieves an element at the specified index from the sequence or returns null
 * if an exception is encountered during the operation.
 *
 * This function combines the behavior of the `get` function and `tryOrNull` to safely
 * access elements by index in a sequence. If the index is out of bounds or another
 * exception occurs, it will return null instead of throwing an exception.
 *
 * Note that accessing elements by index in a sequence is not efficient for large
 * indices, as it involves sequential iteration through the elements.
 *
 * @param T the type of elements in the sequence
 * @param index the zero-based index of the element to retrieve
 * @return the element at the specified index, or null if an exception occurs
 * @since 1.0.0
 */
infix fun <T> Sequence<T>.getOrNull(index: Int) = tryOrNull { get(index) }

/**
 * Retrieves the element at the specified index from a sequence or throws a custom exception if the index
 * is out of bounds.
 *
 * This function attempts to return the element at the given index by traversing the sequence up to the specified
 * position. If the index is invalid (e.g., negative or exceeds the size of the sequence), a custom exception
 * provided by the `lazyException` supplier is thrown.
 *
 * @param index The zero-based index of the element to retrieve from the sequence.
 * @param lazyException A supplier that provides the exception to be thrown if the element at the specified index
 * cannot be retrieved. By default, a `IndexOutOfBoundsException` is thrown with a message indicating the invalid index.
 * @return The element of the sequence at the specified index.
 * @throws Throwable The exception provided by `lazyException` if the index is invalid or if an error occurs during
 * the sequence traversal.
 *
 * @since 1.0.0
 */
fun <E> Sequence<E>.getOrThrow(index: Int, lazyException: ExceptionSupplier = { IndexOutOfBoundsException("Index $index not present") }): E =
    runCatching { take(index).first() }.getOrThrow(lazyException = lazyException)

/**
 * Creates a subsequence from the current sequence starting at the first element
 * and containing elements up to and including the specified end index.
 *
 * @param endIndex The zero-based index of the last element to include in the subsequence.
 *                 Must be greater than or equal to 0.
 * @since 6.1.0
 */
operator fun <E> Sequence<E>.rangeTo(endIndex: Int) = take(endIndex + 1)

/**
 * Sorts the elements of the sequence based on the specified sorting direction.
 *
 * @param direction The sorting direction to apply. Use `SortingDirection.ASCENDING` to sort
 * in ascending order or `SortingDirection.DESCENDING` to sort in descending order.
 * @since 1.0.0
 */
fun <E: Comparable<E>> Sequence<E>.sorted(direction: SortDirection) = when (direction) {
    SortDirection.Ascending -> sorted()
    SortDirection.Descending -> sortedDescending()
}

/**
 * Sorts elements in the sequence based on the specified sorting direction and a selector function.
 * Returns a sequence sorted in ascending or descending order as per the provided direction and selector.
 *
 * @param direction The direction to sort the sequence, either [SortDirection.Ascending] or [SortDirection.Descending].
 * @param selector A lambda function to transform sequence elements into values used for sorting.
 *                 These values must be comparable or nullable comparable.
 * @since 1.0.0
 */
inline fun <E, R : Comparable<R>> Sequence<E>.sortedBy(direction: SortDirection, crossinline selector: Transformer<E, R?>) = when (direction) {
    SortDirection.Ascending -> sortedBy(selector)
    SortDirection.Descending -> sortedByDescending(selector)
}

/**
 * Returns a subsequence of elements that appear before the given element in the sequence.
 * If the given element does not exist in the sequence, an empty sequence is returned.
 *
 * @param element the element in the sequence up to which the subsequence is returned, excluding the element itself.
 * @since 1.0.0
 */
infix fun <E> Sequence<E>.before(element: E) = if (contains(element)) get(0..<indexOf(element)) else emptySequence()
/**
 * Returns a new sequence of elements occurring before and including the first appearance of the specified element.
 * If the sequence does not contain the specified element, an empty sequence is returned.
 *
 * @param element The element to search for in the sequence. The resulting sequence includes elements
 *                from the start of the original sequence up to and including the first occurrence of this element.
 * @since 1.0.0
 */
infix fun <E> Sequence<E>.beforeIncluding(element: E) = if (contains(element)) get(0..indexOf(element)) else emptySequence()
/**
 * Returns a sequence containing elements of the original sequence that appear before the last occurrence
 * of the specified element. If the specified element is not found, an empty sequence is returned.
 *
 * @param element the element whose last occurrence is used as the boundary for creating the resulting sequence
 * @since 1.0.0
 */
infix fun <E> Sequence<E>.beforeLast(element: E) = if (contains(element)) get(0..<lastIndexOf(element)) else emptySequence()
/**
 * Returns a subsequence of elements from the beginning of the original sequence
 * up to and including the last occurrence of the specified [element].
 * If the [element] does not exist in the sequence, returns an empty sequence.
 *
 * @param element The element to locate within the sequence. The returned sequence
 * includes elements up to and including the last occurrence of this element.
 * @since 1.0.0
 */
infix fun <E> Sequence<E>.beforeLastIncluding(element: E) = if (contains(element)) get(0..lastIndexOf(element)) else emptySequence()
/**
 * Returns a sequence containing elements after the specified [element] in the original sequence.
 * If the [element] is not found in the sequence, returns an empty sequence.
 *
 * @param element the element after which the resulting sequence begins
 * @return a sequence of elements after the specified [element]
 * @since 1.0.0
 */
infix fun <E> Sequence<E>.after(element: E) = if (contains(element)) drop(indexOf(element) + 1) else emptySequence()
/**
 * Returns a sequence containing all elements of the original sequence starting from the first occurrence
 * of the specified element, including the element itself. If the element is not found, an empty sequence is returned.
 *
 * @param element the element from which to start the resulting sequence, inclusive.
 * @return a sequence starting from the specified element or an empty sequence if the element is not found.
 * @since 1.0.0
 */
infix fun <E> Sequence<E>.afterIncluding(element: E) = if (contains(element)) drop(indexOf(element)) else emptySequence()
/**
 * Creates a new sequence containing all elements after the last occurrence of the specified [element].
 * If the [element] is not present in the sequence, an empty sequence is returned.
 *
 * @param element The element after whose last occurrence the new sequence should start.
 * @since 1.0.0
 */
infix fun <E> Sequence<E>.afterLast(element: E) = if (contains(element)) drop(lastIndexOf(element) + 1) else emptySequence()
/**
 * Returns a sequence containing the elements of the original sequence after the last occurrence
 * of the specified [element], including the [element] itself. If the [element] is not present
 * in the sequence, an empty sequence is returned.
 *
 * @param element The element to search for in the sequence. The returned sequence starts
 * from the last occurrence of this element, including it.
 * @since 1.0.0
 */
infix fun <E> Sequence<E>.afterLastIncluding(element: E) = if (contains(element)) drop(lastIndexOf(element)) else emptySequence()

/**
 * Filters elements from the sequence by applying the provided transformer function and
 * checking if the result of the transformation is not null.
 *
 * @param element A transformer function that maps an element of the sequence to another value,
 * which is then checked for nullability.
 * @since 1.0.0
 */
infix fun <E, R> Sequence<E>.filterNotNull(element: Transformer<E, R>) =
    filter { element(it) != null }

/**
 * Filters elements from the sequence by applying the provided transformer function and
 * checking if the result of the transformation is null.
 *
 * @param element A transformer function that maps an element of the sequence to another value,
 * which is then checked for nullability.
 * @since 1.0.0
 */
infix fun <E, R> Sequence<E>.filterNull(element: Transformer<E, R>) =
    filter { element(it) == null }

/**
 * Transforms a [Sequence] into a [Map] by applying the provided key and value mapping functions.
 *
 * @param key a function that defines how to transform each element of the sequence into a key.
 * @param value a function that defines how to transform each element of the sequence into a value.
 * @return a [Map] containing key-value pairs generated by applying the key and value functions
 *         to elements of the original sequence.
 * @since 1.0.0
 */
fun <E, K, V> Sequence<E>.associate(key: Transformer<E, K>, value: Transformer<E, V>) =
    associate { key(it) to value(it) }

/**
 * Populates the given [destination] mutable map with key-value pairs where keys and values are provided
 * by the [key] and [value] functions applied to elements of the sequence.
 *
 * The sequence is iterated to generate pairs, which are then added to the [destination] map.
 *
 * @param destination The mutable map to which the generated key-value pairs will be added.
 * @param key A function that computes a key from an element of the sequence.
 * @param value A function that computes a value from an element of the sequence.
 * @param E The type of the elements in the sequence.
 * @param K The type of the keys in the map.
 * @param V The type of the values in the map.
 * @param M The type of the destination mutable map (or a supertype of it).
 * @since 1.0.0
 */
fun <E, K, V, M : MutableMap<in K, in V>> Sequence<E>.associateTo(
    destination: M,
    key: Transformer<E, K>,
    value: Transformer<E, V>
) = associateTo(destination) { key(it) to value(it) }

/**
 * Iterates through each element of the sequence, allowing the provided block to process each element with a `LoopContext`.
 * Control flow interruptions through the `Break` and `Continue` exceptions are supported for advanced loop control.
 *
 * @param block A lambda expression with a `LoopContext` and the current element of the sequence as parameters.
 * The lambda can throw `Break` or `Continue` to influence loop execution.
 *
 * @since 6.1.0
 */
inline fun <T : Sequence<E>, E> T.cForEach(block: ReceiverBiConsumer<LoopContext, E>) = apply {
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
 * Iterates through each element in the sequence along with its index, providing a custom loop context.
 * If a `Break` exception is thrown within the block, the loop terminates immediately.
 * If a `Continue` exception is thrown within the block, the current iteration is skipped.
 *
 * @param block A function accepting a `LoopContext`, the index of the current element, and the element itself.
 * @return The original sequence after completing the iteration.
 * @since 6.1.0
 */
inline fun <T : Sequence<E>, E> T.cForEachIndexed(block: ReceiverTriConsumer<LoopContext, Int, E>) = apply {
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
 * Iterates over the elements of the sequence, invoking the specified action for each element.
 * This method supports custom loop control mechanisms such as break and continue using `Break` and `Continue` exceptions.
 *
 * @param action A consumer that takes a `LoopContext` and an element of the sequence as parameters,
 * allowing for custom behavior during iteration.
 * @return A result of type `R` if the loop was terminated with a `Break` containing a result; otherwise, null.
 * @since 6.1.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <T : Sequence<E>, E, R> T.rForEach(action: ReceiverBiConsumer<LoopContext, E>): R? {
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
 * Iterates over the elements of the sequence in reverse order, along with their indices, and
 * allows execution of a specified action. The iteration can be controlled using `Break`
 * and `Continue` exceptions to optionally stop or skip further processing.
 *
 * @param action A tri-consumer function that takes a `LoopContext`, the current index,
 *               and the current element as parameters and performs a specific action.
 * @return If the loop is terminated with a `Break`, the result provided to `Break` is returned.
 *         Returns `null` if the loop completes without being interrupted by `Break`.
 * @since 6.1.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <T : Sequence<E>, E, R> T.rForEachIndexed(action: ReceiverTriConsumer<LoopContext, Int, E>): R? {
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