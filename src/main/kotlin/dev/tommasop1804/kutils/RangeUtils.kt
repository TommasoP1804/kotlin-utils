/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("RangeUtilsKt")
@file:Suppress("unused")
@file:Since("1.0.0")
@file:MustUseReturnValues
@file:OptIn(ExperimentalContracts::class)

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.numbers.*
import dev.tommasop1804.kutils.classes.range.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Calculates a value within the range based on the provided fraction.
 * The position in the range is determined by multiplying the range's span
 * by the given fraction and adding it to the start of the range.
 *
 * @param percentage A fraction between 0.0 and 1.0 (inclusive) representing the position within the range.
 * @return A value within the range corresponding to the specified fraction.
 * @throws IllegalArgumentException If the fraction is not between 0.0 and 1.0.
 * @since 4.6.1
 */
operator fun ClosedRange<Double>.get(percentage: Percentage): Double {
    if (percentage.isOverflowing) throw ValidationFailedException("Fraction should be between 0.0 and 100.0")
    val span = endInclusive - start
    return start + (span * percentage.toDouble(true))
}

/**
 * Extracts the starting value of the `ClosedRange` as the first component.
 * This function is commonly used in destructuring declaration syntax.
 *
 * @receiver The `ClosedRange` from which the starting value is extracted.
 * @return The start value of the `ClosedRange`.
 * @since 3.1.0
 */
operator fun <T : Comparable<T>> ClosedRange<T>.component1() = start
/**
 * Destructuring operator function that retrieves the upper bound of the range.
 *
 * This function is used as part of a destructuring declaration to access the `endInclusive` property
 * of a `ClosedRange` instance, which represents the inclusive upper limit of the range.
 *
 * @receiver A `ClosedRange` object from which the upper bound will be extracted.
 * @return The `endInclusive` value of the given range.
 * @since 3.1.0
 */
operator fun <T : Comparable<T>> ClosedRange<T>.component2() = endInclusive
/**
 * Retrieves the starting value of the `OpenEndRange`.
 * 
 * This operator function allows destructuring of an `OpenEndRange` instance, 
 * providing direct access to its starting element as the first component.
 * 
 * @receiver The range from which the starting value is extracted.
 * @return The starting value of the `OpenEndRange`.
 * @since 3.1.0
 */
operator fun <T : Comparable<T>> OpenEndRange<T>.component1() = start
/**
 * Operator function that provides destructuring support for the endExclusive property
 * of an OpenEndRange. When used in a destructuring declaration, this function allows
 * the endExclusive value to be retrieved as the second component.
 *
 * @receiver An OpenEndRange of a type that implements Comparable.
 * @return The endExclusive value of the OpenEndRange.
 * @since 3.1.0
 */
operator fun <T : Comparable<T>> OpenEndRange<T>.component2() = endExclusive
/**
 * Returns the first value of the IntRange.
 * 
 * This function allows destructuring declarations to access
 * the first element of an integer range.
 *
 * @receiver The integer range from which the first element is extracted.
 * @return The first integer in the range.
 * @since 3.1.0
 */
operator fun IntRange.component1() = first
/**
 * Returns the first value in the unsigned integer range.
 * This operator allows destructuring declarations to retrieve the first value of the range.
 *
 * @return The starting value of the range.
 * @since 5.0.0
 */
operator fun UIntRange.component1() = first
/**
 * Returns the upper bound of the IntRange.
 *
 * This operator function allows destructuring declaration
 * to extract the last element of the IntRange as the second component.
 *
 * @receiver An instance of IntRange.
 * @return The last value of the IntRange.
 * @since 3.1.0
 */
operator fun IntRange.component2() = last
/**
 * Returns the upper bound (inclusive) of the range.
 * This function enables destructuring declarations for `UIntRange`.
 * @since 5.0.0
 */
operator fun UIntRange.component2() = last
/**
 * Returns the first value of the `LongRange`.
 * This function allows destructuring declarations to be used with `LongRange`,
 * where the first component corresponds to the starting value of the range.
 *
 * @receiver The `LongRange` from which the first value is extracted.
 * @return The starting value of the range (`first`).
 * @since 3.1.0
 */
operator fun LongRange.component1() = first
/**
 * Returns the first element of the ULongRange when destructuring a range.
 *
 * This operator function enables the destructuring declaration syntax, where
 * the first component corresponds to the starting value of the range.
 *
 * @return The first value of the range.
 * @since 5.0.0
 */
operator fun ULongRange.component1() = first
/**
 * Returns the upper bound (inclusive) of the range.
 *
 * This function is a component operator function, allowing destructuring
 * declarations to retrieve the last value of the range.
 *
 * @receiver The range from which the last value is extracted.
 * @return The last value of the range.
 * @since 3.1.0
 */
operator fun LongRange.component2() = last
/**
 * Retrieves the upper bound of the unsigned long range.
 *
 * This function is a component function that allows destructuring declarations
 * to access the `last` value of the `ULongRange`.
 *
 * @return The `last` value representing the upper bound of the range.
 * @since 5.0.0
 */
operator fun ULongRange.component2() = last

/**
 * Executes the given action if the specified value is within the range.
 *
 * @param value The value to check for containment within the range.
 * @param action The action to execute if the value is within the range.
 * @return The original range.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <R : ClosedRange<T>, T : Comparable<T>> R.ifContains(value: T, action: Consumer<R>): R {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the specified action if the given value is within the range.
 *
 * @param value The value to check within the range.
 * @param action The action to execute if the value is in the range.
 * @return The original range.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <R : OpenEndRange<T>, T : Comparable<T>> R.ifContains(value: T, action: Consumer<R>): R {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the specified [action] if the given [value] is contained within the range.
 *
 * @param value The value to check against the range.
 * @param action A lambda function to execute if the value is within the range.
 * @return The original [IntRange] on which the method is invoked.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun IntRange.ifContains(value: Int, action: Consumer<IntRange>): IntRange {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is within this range, excluding the defined exclusions.
 *
 * @param value The integer value to check for containment within the range.
 * @param action The action to be executed if the value is within the range.
 * @return The original range (this) after potentially executing the action.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun IntRangeWithExclusions.ifContains(value: Int, action: Consumer<IntRangeWithExclusions>): IntRangeWithExclusions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the specified action if the given value is within the range.
 *
 * @param value The value to check for inclusion in the range.
 * @param action The action to perform if the value is contained in the range.
 * @return Returns the current instance of IntRangeWithConditions.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun IntRangeWithConditions.ifContains(value: Int, action: Consumer<IntRangeWithConditions>): IntRangeWithConditions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is contained within this UIntRange.
 *
 * @param value The value to check for containment within the range.
 * @param action The action to be executed if the value is within the range.
 * @return The original UIntRange.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun UIntRange.ifContains(value: UInt, action: Consumer<UIntRange>): UIntRange {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the specified action if the provided value is contained within the range, excluding any specifically
 * excluded values. Returns the original UIntRangeWithExclusions regardless of whether the action was executed.
 *
 * @param value The unsigned integer to check for containment within the range.
 * @param action The action to execute if the value is contained within the range.
 * @return The original UIntRangeWithExclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun UIntRangeWithExclusions.ifContains(value: UInt, action: Consumer<UIntRangeWithExclusions>): UIntRangeWithExclusions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is within the range.
 *
 * @param value The value to check for containment within the range.
 * @param action The action to execute if the value is found within the range.
 * @return The original instance of `UIntRangeWithConditions`.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun UIntRangeWithConditions.ifContains(value: UInt, action: Consumer<UIntRangeWithConditions>): UIntRangeWithConditions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is within the range, and returns the range.
 *
 * @param value The value to check for inclusion in the range.
 * @param action The action to be executed if the value is within the range.
 * @return The original range after the action is executed (if applicable).
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun LongRange.ifContains(value: Long, action: Consumer<LongRange>): LongRange {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the provided action if the specified value is within this range.
 *
 * @param value The value to check for containment within the range.
 * @param action The action to execute if the value is within this range.
 * @return The current instance of [LongRangeWithExclusions].
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun LongRangeWithExclusions.ifContains(value: Long, action: Consumer<LongRangeWithExclusions>): LongRangeWithExclusions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the provided action if the specified value is within the range of this LongRangeWithConditions.
 *
 * @param value The value to check for containment within the range.
 * @param action The action to perform if the value is contained within the range.
 * @return The current LongRangeWithConditions instance.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun LongRangeWithConditions.ifContains(value: Long, action: Consumer<LongRangeWithConditions>): LongRangeWithConditions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the specified action if the given value is within the range.
 *
 * @param value The ULong value to check for containment within the range.
 * @param action The action to execute if the value is within the range.
 * @return The current ULongRange instance.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun ULongRange.ifContains(value: ULong, action: Consumer<ULongRange>): ULongRange {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the specified action if the given value is contained within this range.
 *
 * @param value The value to check for membership in the range.
 * @param action The action to perform if the value is contained in the range. The current range will be passed to this action.
 * @return The original range instance.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun ULongRangeWithExclusions.ifContains(value: ULong, action: Consumer<ULongRangeWithExclusions>): ULongRangeWithExclusions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the specified action if the given value is within the range.
 *
 * @param value The unsigned long value to check for containment in the range.
 * @param action The action to be executed if the value is within the range.
 * @return The current instance of [ULongRangeWithConditions].
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun ULongRangeWithConditions.ifContains(value: ULong, action: Consumer<ULongRangeWithConditions>): ULongRangeWithConditions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value in this) action(this)
    return this
}
/**
 * Executes the specified action if the given value is not within the range.
 *
 * @param value The value to check for containment within the range.
 * @param action The action to be executed if the value is not within the range.
 * @return The same range on which the method was called.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <R : ClosedRange<T>, T : Comparable<T>> R.ifNotContains(value: T, action: Consumer<R>): R {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is not contained within the range.
 *
 * @param value The value to check for containment within the range.
 * @param action The action to execute if the value is not contained in the range.
 * @return The original range.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <R : OpenEndRange<T>, T : Comparable<T>> R.ifNotContains(value: T, action: Consumer<R>): R {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the provided action if the specified value is not within this range.
 *
 * @param value The integer value to check for containment within the range.
 * @param action The action to be invoked if the value is not contained in the range.
 * @return The original range on which the method was called.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun IntRange.ifNotContains(value: Int, action: Consumer<IntRange>): IntRange {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is not within the range
 * or its excluded elements, and always returns the current instance.
 *
 * @param value The value to check for presence in the range with exclusions.
 * @param action The action to perform if the value is not contained.
 * @return The current instance of IntRangeWithExclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun IntRangeWithExclusions.ifNotContains(value: Int, action: Consumer<IntRangeWithExclusions>): IntRangeWithExclusions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is not contained within the range.
 *
 * @param value The integer value to check for containment in the range.
 * @param action A consumer action to be executed if the value is not in the range.
 * @return The current instance of [IntRangeWithConditions].
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun IntRangeWithConditions.ifNotContains(value: Int, action: Consumer<IntRangeWithConditions>): IntRangeWithConditions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the specified action if the provided value is not within this range.
 *
 * @param value The unsigned integer value to check for membership in this range.
 * @param action A lambda that will be executed with this range as its parameter if the value is not contained in the range.
 * @return The original range, unchanged.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun UIntRange.ifNotContains(value: UInt, action: Consumer<UIntRange>): UIntRange {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is not contained within this range with exclusions.
 *
 * @param value The unsigned integer to check for containment within the range.
 * @param action The action to invoke if the value is not present in the range.
 * @return The instance of the current range with exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun UIntRangeWithExclusions.ifNotContains(value: UInt, action: Consumer<UIntRangeWithExclusions>): UIntRangeWithExclusions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the provided action if the specified value is not within the range.
 *
 * @param value The unsigned integer value to check for presence in the range.
 * @param action The action to perform if the value is not in the range. Receives the current range as a parameter.
 * @return The current range (`UIntRangeWithConditions`) regardless of whether the action was executed or not.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun UIntRangeWithConditions.ifNotContains(value: UInt, action: Consumer<UIntRangeWithConditions>): UIntRangeWithConditions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes a specified action if the given value is not within the LongRange.
 *
 * @param value The integer value to verify against the range.
 * @param action The action to perform if the range does not contain the value.
 * @return The original LongRange.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun LongRange.ifNotContains(value: Long, action: Consumer<LongRange>): LongRange {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is not contained within the range,
 * and returns the current range instance.
 *
 * @param value The value to check for presence in the range.
 * @param action The action to perform if the value is not contained within the range.
 * @return The current instance of LongRangeWithExclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun LongRangeWithExclusions.ifNotContains(value: Long, action: Consumer<LongRangeWithExclusions>): LongRangeWithExclusions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the specified action if the given value is not contained within the range.
 *
 * @param value The value to check for containment within the range.
 * @param action A consumer action to execute if the value is not within the range.
 * @return The original instance of [LongRangeWithConditions].
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun LongRangeWithConditions.ifNotContains(value: Long, action: Consumer<LongRangeWithConditions>): LongRangeWithConditions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is not within the range.
 *
 * @param value The value to check against the range.
 * @param action A function to be executed with the range as its argument if the value is not contained in the range.
 * @return The original range.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun ULongRange.ifNotContains(value: ULong, action: Consumer<ULongRange>): ULongRange {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is not within the range or its exclusions.
 *
 * @param value The value to check against the range and its exclusions.
 * @param action The action to be executed if the value is not contained.
 * @return The current instance of [ULongRangeWithExclusions].
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun ULongRangeWithExclusions.ifNotContains(value: ULong, action: Consumer<ULongRangeWithExclusions>): ULongRangeWithExclusions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}
/**
 * Executes the given action if the specified value is not contained within this range.
 *
 * @param value The value to check for containment within the range.
 * @param action The action to execute if the value is not present in the range.
 * @return The current instance of ULongRangeWithConditions.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun ULongRangeWithConditions.ifNotContains(value: ULong, action: Consumer<ULongRangeWithConditions>): ULongRangeWithConditions {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (value !in this) action(this)
    return this
}