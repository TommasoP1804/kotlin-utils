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

/**
 * Extension property that checks if the array is empty.
 *
 * @receiver The array instance on which this property is invoked.
 * @return `true` if the array contains no elements, `false` otherwise.
 * @since 5.0.0
 */
val <E> Array<E>.isEmpty get() = isEmpty()
/**
 * Extension property to check if the byte array is empty.
 *
 * @receiver The byte array being checked.
 * @return `true` if the byte array has no elements, otherwise `false`.
 * @since 5.0.0
 */
val ByteArray.isEmpty get() = isEmpty()
/**
 * Extension property that checks if the ShortArray is empty.
 *
 * @return `true` if the array has no elements, otherwise `false`.
 * @since 5.0.0
 */
val ShortArray.isEmpty get() = isEmpty()
/**
 * Extension property for IntArray that indicates whether the array is empty.
 * Returns `true` if the array contains no elements, otherwise `false`.
 * @since 5.0.0
 */
val IntArray.isEmpty get() = isEmpty()
/**
 * Checks if the `LongArray` is empty.
 *
 * @return `true` if the array contains no elements, `false` otherwise.
 * @since 5.0.0
 */
val LongArray.isEmpty get() = isEmpty()
/**
 * Extension property to determine if a FloatArray is empty.
 *
 * @return `true` if the array has no elements, otherwise `false`.
 * @since 5.0.0
 */
val FloatArray.isEmpty get() = isEmpty()
/**
 * Extension property that checks if a [DoubleArray] is empty.
 *
 * @return `true` if the array contains no elements, `false` otherwise.
 * @since 5.0.0
 */
val DoubleArray.isEmpty get() = isEmpty()
/**
 * Indicates whether the BooleanArray is empty.
 *
 * @return `true` if the array contains no elements, `false` otherwise.
 * @since 5.0.0
 */
val BooleanArray.isEmpty get() = isEmpty()
/**
 * Extension property for [CharArray] that checks if the array is empty.
 *
 * @return `true` if the array contains no elements, `false` otherwise.
 * @since 5.0.0
 */
val CharArray.isEmpty get() = isEmpty()
/**
 * Checks if the array is not empty.
 *
 * This property returns `true` if the array contains one or more elements,
 * otherwise returns `false`.
 * @since 5.0.0
 */
val <E> Array<E>.isNotEmpty get() = isNotEmpty()
/**
 * Extension property for `ByteArray` that checks if the array is not empty.
 *
 * Returns `true` if the `ByteArray` contains one or more elements.
 * Returns `false` if the `ByteArray` is empty.
 * @since 5.0.0
 */
val ByteArray.isNotEmpty get() = isNotEmpty()
/**
 * Extension property that returns `true` if the `ShortArray` is not empty, otherwise `false`.
 * @since 5.0.0
 */
val ShortArray.isNotEmpty get() = isNotEmpty()
/**
 * Extension property to check if an IntArray is not empty.
 *
 * @receiver IntArray on which the property is accessed.
 * @return `true` if the array contains one or more elements, otherwise `false`.
 * @since 5.0.0
 */
val IntArray.isNotEmpty get() = isNotEmpty()
/**
 * A property that checks if the LongArray is not empty.
 *
 * Returns `true` if the array contains one or more elements, otherwise `false`.
 * @since 5.0.0
 */
val LongArray.isNotEmpty get() = isNotEmpty()
/**
 * Extension property to check if a FloatArray is not empty.
 *
 * Returns `true` if the array contains one or more elements, otherwise `false`.
 * @since 5.0.0
 */
val FloatArray.isNotEmpty get() = isNotEmpty()
/**
 * Extension property for `DoubleArray` that checks if the array is not empty.
 *
 * @return `true` if the array contains at least one element, otherwise `false`.
 * @since 5.0.0
 */
val DoubleArray.isNotEmpty get() = isNotEmpty()
/**
 * Extension property that checks if the BooleanArray is not empty.
 *
 * @return `true` if the BooleanArray contains one or more elements, `false` otherwise.
 * @since 5.0.0
 */
val BooleanArray.isNotEmpty get() = isNotEmpty()
/**
 * Extension property for the CharArray class that checks if the array is not empty.
 *
 * @return True if the character array contains one or more elements, false otherwise.
 * @since 5.0.0
 */
val CharArray.isNotEmpty get() = isNotEmpty()
/**
 * Extension property for nullable arrays that checks whether the array is either null or empty.
 *
 * @return `true` if the array is null or contains no elements, `false` otherwise.
 * @since 5.0.0
 */
val <E> Array<E>?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return isNullOrEmpty()
}
/**
 * Extension property to check if a nullable [ByteArray] is either null or empty.
 *
 * @return `true` if the [ByteArray] is null or has no elements, `false` otherwise.
 * @since 5.0.0
 */
val ByteArray?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || isEmpty()
}
/**
 * Checks if the nullable ShortArray is either null or empty.
 *
 * @return `true` if the ShortArray is null or contains no elements, `false` otherwise.
 * @since 5.0.0
 */
val ShortArray?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || isEmpty()
}
/**
 * Checks if the array is null or empty.
 *
 * This property provides a convenient way to validate whether an array is either null or contains no elements.
 *
 * @return `true` if the array is null or has no elements, `false` otherwise.
 * @since 5.0.0
 */
val IntArray?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || isEmpty()
}
/**
 * Extension property for nullable `LongArray` to check if the array is either null or empty.
 *
 * @return `true` if the array is null or has no elements, otherwise `false`.
 * @since 5.0.0
 */
val LongArray?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || isEmpty()
}
/**
 * Extension property to check whether a nullable FloatArray is either null or empty.
 *
 * This property evaluates to `true` if the FloatArray is null or contains no elements.
 * Otherwise, it evaluates to `false`. The contract ensures that if the result is `false`,
 * the receiver is not null.
 * @since 5.0.0
 */
val FloatArray?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || isEmpty()
}
/**
 * Extension property that determines if the nullable DoubleArray is either null or empty.
 *
 * @return `true` if the array is null or contains no elements, `false` otherwise.
 * @since 5.0.0
 */
val DoubleArray?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || isEmpty()
}
/**
 * Checks if the BooleanArray is either null or empty.
 *
 * @return `true` if the BooleanArray is null or contains no elements, otherwise `false`.
 * @since 5.0.0
 */
val BooleanArray?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || isEmpty()
}
/**
 * Extension property for nullable `CharArray` to check if it is `null` or empty.
 *
 * Returns `true` if the `CharArray` is `null` or contains no elements.
 * Returns `false` if the `CharArray` is not `null` and contains one or more elements.
 * @since 5.0.0
 */
val CharArray?.isNullOrEmpty: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return this == null || isEmpty()
}
/**
 * Extension property for nullable arrays that checks whether the array is neither null nor empty.
 *
 * @return `true` if the array is not null and contains at least one element, `false` otherwise.
 * @since 5.0.0
 */
val <E> Array<E>?.isNotNullOrEmpty: Boolean get() {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return !isNullOrEmpty()
}
/**
 * Extension property for nullable ByteArray that checks if the array is not null and not empty.
 *
 * @receiver Nullable ByteArray to be checked.
 * @return `true` if the array is not null and contains elements, `false` otherwise.
 * @since 5.0.0
 */
val ByteArray?.isNotNullOrEmpty: Boolean get() {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return this != null && isNotEmpty()
}
/**
 * Checks if the nullable [ShortArray] is not null and not empty.
 *
 * @return `true` if the array is non-null and contains at least one element, otherwise `false`.
 * @since 5.0.0
 */
val ShortArray?.isNotNullOrEmpty: Boolean get() {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return this != null && isNotEmpty()
}
/**
 * Checks if the nullable `IntArray` is not null and not empty.
 *
 * @receiver The nullable `IntArray` to be evaluated.
 * @return `true` if the array is not null and contains at least one element, otherwise `false`.
 * @since 5.0.0
 */
val IntArray?.isNotNullOrEmpty: Boolean get() {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return this != null && isNotEmpty()
}
/**
 * Extension property for a nullable `LongArray` to check if it is neither null nor empty.
 *
 * This property returns `true` if the array is not null and contains at least one element.
 * Otherwise, it returns `false`.
 *
 * A contract is specified to indicate that if this property returns `true`,
 * the receiver is guaranteed to be non-null.
 * @since 5.0.0
 */
val LongArray?.isNotNullOrEmpty: Boolean get() {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return this != null && isNotEmpty()
}
/**
 * Checks if the nullable [FloatArray] is not null and not empty.
 *
 * @return `true` if the [FloatArray] is neither null nor empty, `false` otherwise.
 * @since 5.0.0
 */
val FloatArray?.isNotNullOrEmpty: Boolean get() {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return this != null && isNotEmpty()
}
/**
 * Extension property for nullable `DoubleArray` that checks if the array is not null
 * and contains at least one element.
 *
 * @return `true` if the array is not null and not empty, `false` otherwise.
 * @since 5.0.0
 */
val DoubleArray?.isNotNullOrEmpty: Boolean get() {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return this != null && isNotEmpty()
}
/**
 * Extension property to determine whether a nullable [BooleanArray] is not null and not empty.
 *
 * Evaluates to `true` if the array is both non-null and contains at least one element.
 * Utilizes the Kotlin contract to provide additional information about nullability.
 * @since 5.0.0
 */
val BooleanArray?.isNotNullOrEmpty: Boolean get() {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return this != null && isNotEmpty()
}
/**
 * Extension property for nullable `CharArray` that checks whether the array is not null and not empty.
 *
 * @return `true` if the `CharArray` is not null and contains at least one character, otherwise `false`.
 * @since 5.0.0
 */
val CharArray?.isNotNullOrEmpty: Boolean get() {
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
 * Extension property for an array of comparable elements that determines
 * whether the array is sorted in ascending order.
 *
 * This property evaluates to `true` if the array's elements are sorted
 * such that each element is less than or equal to the next element.
 * For an empty array or a single-element array, this property
 * will always return `true`.
 *
 * @receiver Array of comparable elements.
 * @return `true` if the array is sorted in non-decreasing order, `false` otherwise.
 * @since 5.0.0
 */
val <E : Comparable<E>> Array<E>.isSorted get() = isSorted()
/**
 * Extension property that evaluates whether the ByteArray is sorted in ascending order.
 * It checks each element sequentially to determine if the current element is less than
 * or equal to the next one.
 *
 * This property is read-only and computes the result dynamically whenever accessed.
 * @since 5.0.0
 */
val ByteArray.isSorted get() = isSorted()
/**
 * Extension property for a ShortArray that checks if the array is sorted in ascending order.
 *
 * This property utilizes the `isSorted` function to determine whether the current array elements
 * are sorted. It evaluates the array without modifying its contents.
 *
 * @receiver ShortArray The array to check for sorted order.
 * @return Boolean `true` if the array is sorted in ascending order, otherwise `false`.
 * @since 5.0.0
 */
val ShortArray.isSorted get() = isSorted()
/**
 * Extension property to determine if an integer array is sorted in ascending order.
 *
 * This property evaluates whether all elements in the array are in non-decreasing order.
 * An empty array or an array with a single element is considered sorted.
 *
 * @receiver IntArray The integer array to check.
 * @return Boolean `true` if the array is sorted in ascending order, `false` otherwise.
 * @since 5.0.0
 */
val IntArray.isSorted get() = isSorted()
/**
 * Indicates whether the elements in the LongArray are sorted in ascending order.
 *
 * This property evaluates to `true` if all elements in the array are in non-decreasing order,
 * meaning each element is less than or equal to the next element. If the array is empty or
 * contains only one element, it is considered sorted.
 * @since 5.0.0
 */
val LongArray.isSorted get() = isSorted()
/**
 * An extension property for `FloatArray` that checks if the array is sorted in ascending order.
 *
 * This property evaluates whether the elements of the array are in non-decreasing order.
 * It returns `true` if the array is empty, contains a single element,
 * or if each element is less than or equal to the next element.
 *
 * @return `true` if the array is sorted in ascending order, otherwise `false`.
 * @since 5.0.0
 */
val FloatArray.isSorted get() = isSorted()
/**
 * Extension property that checks whether the elements of a [DoubleArray] are sorted
 * in ascending order.
 *
 * @return `true` if the array elements are in ascending order, otherwise `false`.
 * @since 5.0.0
 */
val DoubleArray.isSorted get() = isSorted()
/**
 * Extension property for `BooleanArray` that evaluates whether the array is sorted in non-decreasing order.
 *
 * The property returns `true` if all elements in the `BooleanArray` are arranged such that each element
 * is less than or equal to the one following it. In the context of a `BooleanArray`, the values are
 * treated as `false < true`.
 *
 * The computation is performed lazily when accessed.
 * @since 5.0.0
 */
val BooleanArray.isSorted get() = isSorted()
/**
 * Extension property for CharArray that checks if the array is sorted in ascending order.
 *
 * @return `true` if the characters in the array are sorted in ascending order, `false` otherwise.
 * @since 5.0.0
 */
val CharArray.isSorted get() = isSorted()
/**
 * Extension property for an array of comparable elements that checks
 * if the array is not sorted in ascending order.
 *
 * @return `true` if the array is not sorted, otherwise `false`.
 * @since 5.0.0
 */
val <E : Comparable<E>> Array<E>.isNotSorted get() = !isSorted()
/**
 * Extension property for ByteArray that indicates whether the array is not sorted in ascending order.
 *
 * This property returns `true` if the byte array is not sorted in ascending order, and `false` otherwise.
 * It internally utilizes the `isSorted()` function to determine the sorting state of the array.
 * @since 5.0.0
 */
val ByteArray.isNotSorted get() = !isSorted()
/**
 * Extension property for `ShortArray` that determines if the array is not sorted in ascending order.
 *
 * This property evaluates to `true` if the array is not sorted, and `false` if the array is sorted
 * in ascending order. Sorting is determined based on the natural order of the elements.
 *
 * Note: This property calls the `isSorted` function to perform the sorting check.
 * @since 5.0.0
 */
val ShortArray.isNotSorted get() = !isSorted()
/**
 * Extension property for `IntArray` that indicates whether the array is not sorted.
 *
 * Returns `true` if the array is not sorted in ascending order, otherwise `false`.
 * Utilizes the `isSorted()` function to determine the sorting status.
 * @since 5.0.0
 */
val IntArray.isNotSorted get() = !isSorted()
/**
 * Extension property for `LongArray` that indicates whether the array is not sorted.
 * Returns `true` if the array is not sorted in ascending order, otherwise `false`.
 * @since 5.0.0
 */
val LongArray.isNotSorted get() = !isSorted()
/**
 * Extension property for `FloatArray` that indicates whether the array
 * is not sorted in ascending order.
 *
 * @return `true` if the array is not sorted, otherwise `false`.
 * @since 5.0.0
 */
val FloatArray.isNotSorted get() = !isSorted()
/**
 * A read-only property that returns `true` if the elements in the `DoubleArray` are not sorted in ascending order,
 * and `false` otherwise.
 *
 * This property internally uses the `isSorted()` function to determine if the array is sorted and negates its result.
 * @since 5.0.0
 */
val DoubleArray.isNotSorted get() = !isSorted()
/**
 * Extension property that returns `true` if the `BooleanArray` is not sorted in ascending order,
 * and `false` otherwise. Sorting is determined based on the natural order of boolean values,
 * where `false` precedes `true`.
 * @since 5.0.0
 */
val BooleanArray.isNotSorted get() = !isSorted()
/**
 * Extension property for a CharArray that determines whether the array is not sorted in ascending order.
 * The property evaluates to `true` if the array is not sorted and `false` if it is sorted.
 * It relies on the `isSorted()` function to perform the check.
 * @since 5.0.0
 */
val CharArray.isNotSorted get() = !isSorted()
/**
 * Extension property for determining if an array is sorted in descending order.
 *
 * This property operates on arrays where elements implement the `Comparable` interface.
 * It evaluates whether each element in the array is greater than or equal to the next element,
 * ensuring the entire array is sorted from the largest to the smallest value.
 *
 * @receiver The array of comparable elements to check.
 * @return `true` if the array is sorted in descending order, or `false` otherwise.
 * @since 5.0.0
 */
val <E : Comparable<E>> Array<E>.isSortedDescending get() = isSortedDescending()
/**
 * An extension property for the `ByteArray` class that checks if the array's elements are sorted
 * in descending order. The comparison is based on the natural order of the elements.
 *
 * @return `true` if the array is sorted in descending order, or `false` otherwise.
 * @since 5.0.0
 */
val ByteArray.isSortedDescending get() = isSortedDescending()
/**
 * Extension property indicating whether the elements in the [ShortArray] are sorted in descending order.
 * Returns `true` if the array is sorted in strictly decreasing order, or `false` otherwise.
 * @since 5.0.0
 */
val ShortArray.isSortedDescending get() = isSortedDescending()
/**
 * Extension property that checks if the elements in the array are sorted in descending order.
 * Returns `true` if the array is sorted in non-increasing order, otherwise `false`.
 * @since 5.0.0
 */
val IntArray.isSortedDescending get() = isSortedDescending()
/**
 * Extension property for LongArray to determine if the array is sorted
 * in descending order.
 *
 * @return `true` if the elements in the array are in non-increasing order,
 *         otherwise `false`. An empty array or an array with a single element
 *         is considered sorted in descending order.
 * @since 5.0.0
 */
val LongArray.isSortedDescending get() = isSortedDescending()
/**
 * Checks if the elements in the FloatArray are sorted in descending order.
 *
 * @return `true` if the FloatArray is sorted in descending order or is empty,
 *         `false` otherwise.
 * @since 5.0.0
 */
val FloatArray.isSortedDescending get() = isSortedDescending()
/**
 * Extension property that checks if a DoubleArray is sorted in descending order.
 *
 * Returns `true` if the elements in the array are arranged from largest to smallest,
 * and `false` otherwise.
 * @since 5.0.0
 */
val DoubleArray.isSortedDescending get() = isSortedDescending()
/**
 * Extension property for `BooleanArray` that determines whether the elements
 * in the array are sorted in descending order.
 *
 * The property evaluates to `true` if each element in the array is greater than
 * or equal to the element that follows it, and `false` otherwise. Returns `true`
 * for an empty or single-element array, as they are considered trivially sorted.
 * @since 5.0.0
 */
val BooleanArray.isSortedDescending get() = isSortedDescending()
/**
 * Extension property for `CharArray` that checks if the array is sorted in descending order.
 *
 * This property evaluates whether the characters in the array are arranged in
 * non-increasing order, meaning each character is greater than or equal to
 * the character following it.
 *
 * @return `true` if the array is sorted in descending order, otherwise `false`.
 * @since 5.0.0
 */
val CharArray.isSortedDescending get() = isSortedDescending()
/**
 * Extension property that evaluates whether the elements in the array are not sorted
 * in descending order. This is determined by checking if the array is not sorted
 * in the reverse of natural order for the elements.
 *
 * @receiver Array of elements that are comparable.
 * @return `true` if the array is not sorted in descending order, `false` otherwise.
 * @since 5.0.0
 */
val <E : Comparable<E>> Array<E>.isNotSortedDescending get() = !isSortedDescending()
/**
 * Extension property for `ByteArray` that determines whether
 * the array is not sorted in descending order.
 *
 * @return `true` if the byte array is not sorted in strictly descending order, `false` otherwise.
 * @since 5.0.0
 */
val ByteArray.isNotDescendingSorted get() = !isSortedDescending()
/**
 * Checks if the elements in the [ShortArray] are not sorted in a strictly descending order.
 *
 * This property evaluates to `true` if the array is not in descending order,
 * including cases where the array is sorted in ascending order, unsorted,
 * or has repeated elements. If the array is strictly sorted in descending
 * order, the property evaluates to `false`.
 * @since 5.0.0
 */
val ShortArray.isNotDescendingSorted get() = !isSortedDescending()
/**
 * Extension property for IntArray that checks whether the array
 * is not sorted in descending order.
 *
 * Returns `true` if the array is not sorted in descending order,
 * otherwise returns `false`.
 *
 * This property internally utilizes the `isSortedDescending` function
 * to perform the descending order check and negates its result.
 * @since 5.0.0
 */
val IntArray.isNotDescendingSorted get() = !isSortedDescending()
/**
 * Extension property to check if a [LongArray] is not sorted in descending order.
 *
 * This property evaluates to `true` if the array is not sorted in descending order,
 * and `false` otherwise. It internally utilizes the `isSortedDescending` method
 * to determine the sorting order of the array.
 * @since 5.0.0
 */
val LongArray.isNotDescendingSorted get() = !isSortedDescending()
/**
 * Extension property for `FloatArray` that checks if the array is not sorted in descending order.
 *
 * This property returns `true` if the array is not sorted in strictly descending order.
 * Otherwise, it returns `false`.
 * @since 5.0.0
 */
val FloatArray.isNotDescendingSorted get() = !isSortedDescending()
/**
 * A read-only property extension for `DoubleArray` that determines if the array is not
 * sorted in strictly descending order.
 *
 * This property evaluates to `true` if the array elements are not in descending order
 * and `false` otherwise. The check is determined by negating the result of the
 * `isSortedDescending()` function applied to the array.
 * @since 5.0.0
 */
val DoubleArray.isNotDescendingSorted get() = !isSortedDescending()
/**
 * A read-only extension property for `BooleanArray` that checks whether the array is not sorted
 * in descending order.
 *
 * This property evaluates to `true` if the array is not sorted in strictly descending order,
 * and `false` otherwise.
 *
 * It uses the `isSortedDescending()` function to determine the descending order of the array,
 * and negates the result.
 * @since 5.0.0
 */
val BooleanArray.isNotDescendingSorted get() = !isSortedDescending()
/**
 * Extension property for `CharArray` which checks whether the array is
 * not sorted in descending order.
 *
 * This property returns `true` if the character array is not sorted
 * in strictly descending order, and `false` otherwise.
 *
 * The check is performed by negating the result of the `isSortedDescending`
 * function. If the array is empty or contains a single element, it is
 * considered as not descending sorted.
 * @since 5.0.0
 */
val CharArray.isNotDescendingSorted get() = !isSortedDescending()

/**
 * Negates the state of the array by returning `true` if the array is either `null` or empty.
 *
 * This operator checks if the array reference is `null` or if it contains no elements.
 *
 * @return `true` if the array is `null` or empty, otherwise `false`.
 * @since 1.0.0
 */
@JvmName("nullableNot")
operator fun Array<*>?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty()
}
/**
 * Performs a logical NOT operation on the nullable [ByteArray].
 * This operator checks whether the [ByteArray] is null or empty.
 *
 * @return `true` if the [ByteArray] is null or empty, otherwise `false`.
 * @since 5.0.0
 */
@JvmName("nullableNot")
operator fun ByteArray?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty
}
/**
 * Checks if the nullable ShortArray is either null or empty.
 *
 * @return `false` if the array is not null and contains elements, `true` otherwise.
 * @since 5.0.0
 */
@JvmName("nullableNot")
operator fun ShortArray?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty
}
/**
 * Checks whether the nullable [IntArray] is null or empty.
 *
 * @return `false` if the [IntArray] is not null and contains elements, otherwise `true`.
 * @since 5.0.0
 */
@JvmName("nullableNot")
operator fun IntArray?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty
}
/**
 * Checks whether the long array is either `null` or empty.
 *
 * @return `true` if the array is `null` or contains no elements, `false` otherwise.
 * @since 5.0.0
 */
@JvmName("nullableNot")
operator fun LongArray?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty
}
/**
 * Checks whether the nullable FloatArray is null or empty.
 *
 * This operator function allows using the logical NOT operator (`!`) on a nullable FloatArray to determine
 * if it is either null or contains no elements.
 *
 * @return `false` if the FloatArray is not null and contains one or more elements, otherwise `true`.
 * @since 5.0.0
 */
@JvmName("nullableNot")
operator fun FloatArray?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty
}
/**
 * Checks if the DoubleArray is null or empty.
 *
 * This operator function returns `false` if the DoubleArray is not null and contains elements.
 * Otherwise, it returns `true` if the DoubleArray is either null or has no elements.
 *
 * @return `true` if the DoubleArray is null or empty, `false` otherwise.
 * @since 5.0.0
 */
@JvmName("nullableNot")
operator fun DoubleArray?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty
}
/**
 * Performs a logical NOT operation on the provided BooleanArray.
 *
 * @receiver The nullable BooleanArray on which the operation is performed.
 * @return `false` if the BooleanArray is not null and not empty; `true` otherwise.
 * @since 5.0.0
 */
@JvmName("nullableNot")
operator fun BooleanArray?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty
}
/**
 * Checks if the `CharArray` is null or empty.
 *
 * @return `true` if the `CharArray` is null or empty, `false` otherwise.
 * @since 5.0.0
 */
@JvmName("nullableNot")
operator fun CharArray?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty
}

/**
 * Returns `true` if the array is empty, otherwise `false`.
 *
 * This operator function provides a not (~) operation for arrays,
 * allowing the empty state of the array to be evaluated conveniently.
 *
 * @return `true` if the array contains no elements, otherwise `false`.
 * @since 5.0.0
 */
operator fun Array<*>.not(): Boolean = isEmpty()
/**
 * Operator function that checks whether the ByteArray is empty.
 *
 * @return true if the ByteArray is empty, false otherwise.
 * @since 5.0.0
 */
operator fun ByteArray.not(): Boolean = isEmpty
/**
 * Checks if the ShortArray is empty.
 *
 * This operator function is used to determine whether the array contains no elements.
 *
 * @return `true` if the array is empty, `false` otherwise.
 * @since 5.0.0
 */
operator fun ShortArray.not(): Boolean = isEmpty
/**
 * Checks if the IntArray is empty.
 *
 * This operator function negates the array by returning true if the array is empty
 * and false otherwise.
 *
 * @return true if the array is empty, false otherwise.
 * @since 5.0.0
 */
operator fun IntArray.not(): Boolean = isEmpty
/**
 * Checks if the LongArray is empty, providing a negation-like operation.
 *
 * @return true if the array is empty, false otherwise.
 */
operator fun LongArray.not(): Boolean = isEmpty
/**
 * Checks whether the FloatArray is empty.
 *
 * This operator function allows the use of the `not` operator to determine
 * if the FloatArray contains no elements.
 *
 * @return `true` if the FloatArray is empty, `false` otherwise.
 * @since 5.0.0
 */
operator fun FloatArray.not(): Boolean = isEmpty
/**
 * Returns `true` if the DoubleArray is empty, otherwise returns `false`.
 *
 * @return `true` if the array has no elements, `false` otherwise.
 * @since 5.0.0
 */
operator fun DoubleArray.not(): Boolean = isEmpty
/**
 * Returns `true` if the Boolean array is empty, otherwise returns `false`.
 *
 * This operator function is a Kotlin extension that negates the Boolean state
 * of the array based on its emptiness.
 *
 * @return `true` if the array is empty, `false` otherwise.
 * @since 5.0.0
 */
operator fun BooleanArray.not(): Boolean = isEmpty
/**
 * Checks if the character array is empty.
 *
 * This operator function returns true if the character array has no elements,
 * and false otherwise.
 *
 * @return true if the character array is empty, false otherwise.
 * @since 5.0.0
 */
operator fun CharArray.not(): Boolean = isEmpty

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
 * Returns the original array if it is not null or empty. Otherwise, invokes the provided
 * `defaultValue` supplier and returns its result.
 *
 * @param defaultValue A supplier function that provides a default array when the original array is null or empty.
 * @return The original array if it is not null or empty, otherwise the result of invoking `defaultValue`.
 * @since 2.1.0
 */
inline fun <E> Array<E>?.ifNullOrEmpty(defaultValue: Supplier<Array<E>>): Array<E> {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
    }
    return if (isNullOrEmpty()) defaultValue() else this
}

/**
 * Executes the given action if the array is not empty and returns the array.
 *
 * @param action A consumer that performs an operation on the array if it is not empty.
 * @return The original array.
 */
@IgnorableReturnValue
inline fun <E> Array<E>.ifNotEmpty(action: Consumer<Array<E>>): Array<E> {
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
inline fun <E> Array<E>?.ifNotNullOrEmpty(action: Consumer<Array<E>>): Array<E>? {
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
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> Array<E>.ifContains(element: E, action: Consumer<Array<E>>): Array<E> {
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
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> Array<E>.ifNotContains(element: E, action: Consumer<Array<E>>): Array<E> {
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
    filter { element(it) != null }

/**
 * Filters the elements of the array based on a transformation function that checks for null values.
 *
 * @param element A transformation function that converts each element of the array into a result which is then checked for nullability.
 * @since 2.1.0
 */
inline infix fun <E, R> Array<E>.filterNull(element: Transformer<E, R>) =
    filter { element(it) == null }

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