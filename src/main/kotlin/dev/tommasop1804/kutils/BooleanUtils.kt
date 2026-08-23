/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("BooleanUtilsKt")
@file:Suppress("unused", "kutils_null_check")
@file:Since("1.0.0")
@file:OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
@file:MustUseReturnValues

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Checks if any of the given Boolean values is `true`.
 *
 * This function evaluates the provided Boolean values and returns `true` if at least one
 * of the values is non-null and equals `true`. If none of the values are `true` or all
 * values are null, it returns `false`.
 *
 * @param values a vararg parameter of nullable Boolean values to evaluate
 * @since 1.0.0
 */
fun anyTrue(vararg values: Boolean?) = values.any { it == true }
/**
 * Checks if any element in the given iterable of nullable Booleans is `true`.
 *
 * @param values an iterable of nullable Boolean values to check.
 * @since 1.0.0
 */
fun anyTrue(values: Iterable<Boolean?>) = values.any { it == true }

/**
 * Checks if all provided Boolean values are true.
 * Returns true if all the values are `true`, false if at least one is not `true`,
 * or the collection is empty.
 *
 * @param values A variable number of nullable Boolean values to evaluate.
 * @return True if all provided Boolean values are true and the array is non-empty, otherwise false.
 * @since 1.0.0
 */
fun allTrue(vararg values: Boolean?) = values.isNotEmpty() && values.all { it == true }
/**
 * Checks if all elements in the provided iterables of nullable booleans are `true`.
 * This function returns `true` if all iterable instances contain only `true` values
 * and the provided varargs are not empty.
 *
 * @param values Vararg parameter consisting of iterable collections of nullable Boolean values.
 *               Each iterable is checked to ensure all elements are `true`.
 * @return `true` if all elements in all provided iterables are `true` and the vararg is not empty, otherwise `false`.
 * @since 1.0.0
 */
fun allTrue(values: Iterable<Boolean?>) = values.toList().isNotEmpty() && values.all { it == true }

/**
 * Checks if all the given Boolean? values are false.
 *
 * This function evaluates the provided Boolean? values and returns true
 * if the collection is not empty and all the elements are explicitly false.
 *
 * @param values The vararg collection of Boolean? values to evaluate.
 * @return True if all values are false and the collection is non-empty, false otherwise.
 * @since 1.0.0
 */
fun allFalse(vararg values: Boolean?) = values.isNotEmpty() && values.all { it == false }
/**
 * Determines whether all elements in the given iterable are `false`.
 * This method returns `true` if all elements are explicitly `false` and the iterable is not empty.
 * If the iterable contains `null` values, they are ignored in the evaluation.
 *
 * @param values an iterable collection of nullable boolean values to evaluate
 * @since 1.0.0
 */
fun allFalse(values: Iterable<Boolean?>) = values.toList().isNotEmpty() && values.all { it == false }

/**
 * Checks if any value in the given array of nullable Booleans is explicitly false.
 *
 * @param values a vararg of nullable Boolean values to check.
 * @return true if at least one value is false, otherwise false.
 * @since 1.0.0
 */
fun anyFalse(vararg values: Boolean?) = values.any { it == false }
/**
 * Checks if any of the elements in the given iterable are explicitly `false`.
 *
 * @param values an iterable collection of nullable Boolean values to be checked
 * @return `true` if at least one element in the iterable is `false`, otherwise `false`
 * @since 1.0.0
 */
fun anyFalse(values: Iterable<Boolean?>) = values.any { it == false }

/**
 * Counts the number of non-null Boolean values that are true in the given input.
 *
 * @param values a variable number of nullable Boolean values to be evaluated
 * @return the count of values that are true
 * @since 1.0.0
 */
fun countTrue(vararg values: Boolean?) = values.count { it == true }
/**
 * Counts the number of `true` values in the given iterable of nullable booleans.
 *
 * @param values An iterable collection of nullable Boolean values to be evaluated.
 * @return The count of values that are `true`.
 * @since 1.0.0
 */
fun countTrue(values: Iterable<Boolean?>) = values.count { it == true }

/**
 * Counts the number of false values in the provided boolean array.
 *
 * @param values The variable number of nullable boolean values to evaluate.
 * @return The count of elements that are explicitly false.
 * @since 1.0.0
 */
fun countFalse(vararg values: Boolean?) = values.count { it == false }
/**
 * Counts the number of false values in the given iterable of nullable Booleans.
 *
 * @param values an iterable collection of nullable Boolean values
 * @return the count of false values within the provided iterable
 * @since 1.0.0
 */
fun countFalse(values: Iterable<Boolean?>) = values.count { it == false }

/**
 * Extension property for nullable Boolean type that evaluates whether the value is explicitly `true`.
 * Returns `true` if the Boolean is not null and has the value `true`; otherwise, returns `false`.
 *
 * @since 1.0.0
 */
val Boolean?.isTrue: Boolean get() {
    contract {
        returns(true) implies (this@isTrue != null)
    }
    return this == true
}
/**
 * Extension property that evaluates to `true` if the Boolean value is `true`.
 *
 * This property acts as a utility to explicitly confirm the truthiness of a Boolean value
 * and enforces a contract to infer conditions where this property will return `true`.
 * @since 3.10.1
 */
val Boolean.isTrue: Boolean get() {
    contract {
        returns(true) implies (this@isTrue)
    }
    return this
}
/**
 * Extension property to determine if a nullable Boolean is explicitly `false`.
 *
 * This property returns `true` if the Boolean is not null and equals `false`,
 * otherwise it returns `false`.
 *
 * @receiver A nullable Boolean to check if it is explicitly false.
 * @return `true` if the receiver is `false`, otherwise `false`.
 * @since 1.0.0
 */
val Boolean?.isFalse: Boolean get() {
    contract {
        returns(true) implies (this@isFalse != null)
    }
    return this == false
}
/**
 * Extension property for the `Boolean` type that evaluates whether the value is `false`.
 *
 * This property leverages Kotlin's contract system to indicate that when the property
 * returns `true`, the boolean value is guaranteed to be `false`.
 *
 * @return `true` if the boolean value is `false`, otherwise returns `false`.
 * @since 3.10.1
 */
val Boolean.isFalse: Boolean get() {
    contract {
        returns(true) implies (!this@isFalse)
    }
    return !this
}
/**
 * Extension property for nullable Boolean values that checks if the value is either `true` or `null`.
 * This can be used to simplify conditional checks involving nullable Booleans.
 *
 * @receiver A nullable Boolean value.
 * @return `true` if the Boolean is either `true` or `null`, otherwise `false`.
 * @since 1.0.0
 */
val Boolean?.isNullOrTrue: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrTrue != null)
    }
    return this == null || this
}
/**
 * Extension property to determine if a nullable Boolean is either `null` or `false`.
 *
 * @return `true` if the Boolean is `null` or `false`; `false` otherwise.
 * @since 1.0.0
 */
val Boolean?.isNullOrFalse: Boolean get() {
    contract {
        returns(false) implies (this@isNullOrFalse != null)
    }
    return this == null || !this
}

/**
 * Executes the given action if the boolean value is true.
 *
 * @param action The action to be executed if the boolean value is true.
 * @return The original boolean value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Boolean.ifTrue(action: Action): Boolean {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        (this@ifTrue) holdsIn action
    }
    if (isTrue) action()
    return this
}
/**
 * Executes the given action if the Boolean receiver is null or true.
 *
 * @param action the action to be performed if the Boolean is null or true
 * @return the original Boolean value
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Boolean?.ifNullOrTrue(action: Consumer<Boolean?>): Boolean? {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        (this@ifNullOrTrue != null) implies returnsNotNull()
    }
    if (isNullOrTrue) action(this)
    return this
}
/**
 * Executes the provided [action] if the Boolean value is `false`.
 * The original Boolean value is always returned.
 *
 * @param action The action to execute if the Boolean value is `false`.
 * @return The original Boolean value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Boolean.ifFalse(action: Action): Boolean {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        (!this@ifFalse) holdsIn action
    }
    if (isFalse) action()
    return this
}
/**
 * Executes the given action if the Boolean is either null or false.
 *
 * @param action a consumer function to be invoked when the Boolean is null or false
 * @return the original Boolean value
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Boolean?.ifNullOrFalse(action: Consumer<Boolean?>): Boolean? {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        (this@ifNullOrFalse != null) implies returnsNotNull()
    }
    if (isNullOrFalse) action(this)
    return this
}