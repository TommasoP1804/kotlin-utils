/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("ObjectUtilsKt")
@file:Suppress("unused", "kutils_null_check", "kutils_collection_declaration", "kutils_map_declaration", "deprecation", "kutils_tuple_declaration",
    "UseExpressionBody",
    "UseExpressionBody",
    "kutils_empty_check"
)
@file:Since("1.0.0")
@file:OptIn(ExperimentalExtendedContracts::class, ExperimentalContracts::class)
@file:MustUseReturnValues

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.Json.Companion.MAPPER
import dev.tommasop1804.kutils.classes.constants.*
import dev.tommasop1804.kutils.classes.constants.TextCase.Companion.convertCase
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.errors.*
import dev.tommasop1804.kutils.exceptions.*
import org.slf4j.Logger
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException
import kotlin.reflect.KClass


/**
 * Checks if the current object is not null.
 *
 * This function serves as a utility to ensure that the calling object is not null, providing
 * an expressive way to handle nullability checks.
 *
 * @return `true` if the object is not null, `false` otherwise.
 * @since 5.0.0
 */
val Any?.isNotNull: Boolean get() {
    contract {
        returns(true) implies (this@isNotNull != null)
        returns(false) implies (this@isNotNull == null)
    }
    return this != null
}

/**
 * Checks if the given object is `null`.
 *
 * @return `true` if the object is `null`, `false` otherwise.
 * @since 5.0.0
 */
val Any?.isNull: Boolean get() {
    contract {
        returns(true) implies (this@isNull == null)
        returns(false) implies (this@isNull != null)
    }
    return this == null
}

/**
 * Executes the given action if the current object is not null.
 *
 * @param action The action to be executed if the object is non-null.
 * @return The original object, whether or not the action was executed.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T> T?.ifNotNull(action: Consumer<T>): T? {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        (this@ifNotNull != null) holdsIn action
    }
    if (this != null) action(this)
    return this
}
/**
 * Executes the given action if the instance is `null` and returns the instance.
 *
 * @param action A consumer that will be invoked if the instance is `null`.
 * @return The original instance.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T> T?.ifNull(action: Action): T? {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this == null) action()
    return this
}

/**
 * Executes the given action if the current object is equal to the specified `other` object.
 *
 * @param other The object to compare the current object with.
 * @param action The action to perform if the objects are equal. The action receives a `MonoPair`
 *               containing the current object and the `other` object as its receiver.
 * @return The result of the action if the objects are equal, or `null` otherwise.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T> T.ifEquals(other: T, action: Consumer<MonoPair<T>>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this == other) action(this to other)
    return this
}
/**
 * Executes the provided action if the current object is not equal to the specified other object.
 *
 * @param other The object to compare with the current object for equality.
 * @param action The action to execute if the objects are not equal. It is a function that takes a pair of the current
 *               object and the other object as input and produces a result.
 * @return The result of the action if the objects are not equal, or null if they are equal.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T> T.ifNotEquals(other: T, action: Consumer<MonoPair<T>>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this != other) action(this to other)
    return this
}

/**
 * Executes the given action if the current value is within the specified range,
 * and always returns the current value.
 *
 * @param range the range to check if the current value is within.
 * @param action the action to be executed if the current value is within the specified range.
 * @return the current value, regardless of whether the action was executed.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Comparable<T>> T.ifIn(range: ClosedRange<T>, action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the specified action if the current value is within the given range.
 *
 * @param range The range to check if the current value belongs to.
 * @param action The action to perform if the current value is within the range.
 * @return The current value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Comparable<T>> T.ifIn(range: OpenEndRange<T>, action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the specified action if the current element is found within the given iterables.
 *
 * @param iterable The collection to check for the presence of the current element.
 * @param action The action to execute if the element is found in the iterables.
 * @return The current element.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> E.ifIn(iterable: Iterable<E>, action: Consumer<E>): E {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in iterable) action(this)
    return this
}
/**
 * Executes the provided action if the current key-value pair exists in the specified map.
 *
 * @param map The map against which the key-value pair will be checked for existence.
 * @param action The action to execute if the key-value pair is present in the map.
 *               It receives the current key-value pair as an input.
 * @return The current key-value pair, regardless of whether the action was executed.
 * @since 6.1.0
 */
@IgnorableReturnValue
inline fun <K, V> Pair<K, V>.ifIn(map: Map<K, V>, action: Consumer<Pair<K, V>>): Pair<K, V> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in map) action(this)
    return this
}
/**
 * Executes the provided action if the current key exists in the specified map.
 *
 * @param map the map to check if the key exists in.
 * @param action the action to be performed on the key if it is present in the map.
 * @return the original key.
 * @since 6.1.0
 */
@IgnorableReturnValue
inline fun <K> K.ifIn(map: Map<K, *>, action: Consumer<K>): K {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in map) action(this)
    return this
}
/**
 * Executes the given action if the current element is present in the specified array.
 *
 * @param array The array to check for the presence of the current element.
 * @param action The action to be executed if the current element is found in the array.
 * @return The current element.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> E.ifIn(array: Array<E>, action: Consumer<E>): E {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in array) action(this)
    return this
}
/**
 * Executes the given action if the current element exists in the specified sequence.
 *
 * @param sequence The sequence to check for the current element.
 * @param action The action to be executed if the current element is found in the sequence.
 * @return The current element.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> E.ifIn(sequence: Sequence<E>, action: Consumer<E>): E {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in sequence) action(this)
    return this
}
/**
 * Executes the given action if the current value is not within the specified range.
 *
 * @param range the closed range to check the value against
 * @param action the action to execute if the value is not within the range
 * @return the original value
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Comparable<T>> T.ifNotIn(range: ClosedRange<T>, action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the specified action if the current value is not within the specified open-ended range.
 *
 * @param range The open-ended range to compare the current value against.
 * @param action The action to be executed if the current value is not in the range.
 * @return The current value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Comparable<T>> T.ifNotIn(range: OpenEndRange<T>, action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given [action] if the current object is not contained in the provided [iterable].
 *
 * @param iterable The collection of elements to check for the presence of the current object.
 * @param action The operation to perform if the current object is not in the [iterable].
 * @return The current object.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> E.ifNotIn(iterable: Iterable<E>, action: Consumer<E>): E {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this !in iterable) action(this)
    return this
}
/**
 * Executes the given action if the key-value pair is not present in the specified map.
 *
 * @param map the map in which the presence of the key-value pair will be checked.
 * @param action the action to perform if the key-value pair is not present in the map.
 * @return the receiver key-value pair.
 * @since 6.1.0
 */
@IgnorableReturnValue
inline fun <K, V> Pair<K, V>.ifNotIn(map: Map<K, V>, action: Consumer<Pair<K, V>>): Pair<K, V> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this !in map) action(this)
    return this
}
/**
 * Executes the given action if the key is not present in the specified map.
 *
 * @param map the map in which the key presence is checked
 * @param action the action to be performed if the key is not in the map
 * @return the key itself
 * @since 6.1.0
 */
@IgnorableReturnValue
inline fun <K> K.ifNotIn(map: Map<K, *>, action: Consumer<K>): K {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this !in map) action(this)
    return this
}
/**
 * Executes the given action if the current element is not present in the specified array.
 *
 * @param array The array to check for the presence of the current element.
 * @param action The action to execute if the current element is not in the array.
 * @return The current element.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> E.ifNotIn(array: Array<E>, action: Consumer<E>): E {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this !in array) action(this)
    return this
}
/**
 * Invokes the given action if the current element is not present in the specified sequence.
 *
 * @param sequence The sequence to check for the presence of the current element.
 * @param action A consumer action to execute if the current element is not in the sequence.
 * @return The current element.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <E> E.ifNotIn(sequence: Sequence<E>, action: Consumer<E>): E {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this !in sequence) action(this)
    return this
}

/**
 * Executes the given action if the current instance is greater than or equal to the specified value.
 *
 * @param other The value to compare against.
 * @param action The action to execute if the condition is met.
 * @return The current instance.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Comparable<T>> T.ifGreaterOrEqualThan(other: T, action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this >= other) action(this)
    return this
}
/**
 * Executes the specified action if the current value is greater than the given value.
 *
 * @param other The value to compare with the current value.
 * @param action The action to be executed if the current value is greater than the specified value.
 * @return The current value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Comparable<T>> T.ifGreaterThan(other: T, action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this > other) action(this)
    return this
}
/**
 * Executes the given action if the current object is less than or equal to the specified other object.
 *
 * @param other The object to compare with the current object.
 * @param action The action to execute if the condition is met.
 * @return The original object (`this`).
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Comparable<T>> T.ifLowerOrEqualThan(other: T, action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this <= other) action(this)
    return this
}
/**
 * Executes the given action if the current value is less than the specified value.
 *
 * @param other The value to compare against.
 * @param action The action to execute if the current value is less than the specified value.
 * @return The original value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Comparable<T>> T.ifLowerThan(other: T, action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this < other) action(this)
    return this
}

/**
 * Checks if the object instance is castable to the specified class type.
 *
 * @param T the class type to check the instance against.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any?.isCastableTo() = runCatching { this as T }.isSuccess

/**
 * Attempts to cast the current instance to the specified type [T].
 * Returns the result as a [Result], wrapping the casted value if successful
 * or an exception if the cast failed.
 *
 * This method is useful in scenarios where type safety is uncertain,
 * as it avoids runtime exceptions by capturing errors in the [Result].
 *
 * @receiver The instance to be cast.
 * @return A [Result] containing the casted value if successful, or an exception if the cast fails.
 * @since 1.0.0
 */
inline fun <reified T> Any?.safeCast() = runCatching { this as T }
/**
 * Safely casts the current object to the specified type [T]. If the cast fails,
 * returns the provided default value.
 *
 * @param default A value of type [T] that will be returned if the cast operation fails.
 * @return The object cast to type [T], or the given default value in case of a failure.
 * @since 1.0.0
 */
inline fun <reified T> Any?.safeCast(default: T) = runCatching { this as T }.getOrDefault(default)
/**
 * Attempts to safely cast the caller object to the specified type [T].
 * If the cast is successful, the method returns the object as [T],
 * otherwise it returns `null`.
 *
 * The cast operation is performed within a `runCatching` block, which
 * ensures that the method handles any potential exceptions gracefully.
 *
 * @return The object cast to [T] if successful, or `null` if the cast fails.
 * @throws ClassCastException if the type cast fails, but in this implementation,
 *         it is caught and handled by returning `null`.
 *
 * @since 1.0.0
 */
inline fun <reified T> Any?.safeCastOrNull(): T? = runCatching { this as T }.getOrNull()
/**
 * Safely attempts to cast the receiver of type [T1] to the target type [T2].
 * If the cast fails, applies the given transformation to provide a fallback value.
 *
 * @param transform A transformation function that will be applied if the cast fails.
 * @since 1.0.0
 */
inline fun <T1, reified T2> T1?.safeCastOr(transform: Transformer<T1?, T2>): T2 {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return runCatching { this as T2 }.getOrNull() ?: transform(this)
}
/**
 * Attempts to cast the calling object to the specified type [T]. If the cast fails, the provided exception
 * supplied by [lazyException] is thrown.
 *
 * This method uses `runCatching` to perform the cast operation and leverages the provided exception supplier
 * to handle failures, ensuring a safe and customizable casting mechanism.
 *
 * @param T The type to which the object is being cast.
 * @param lazyException A supplier function that generates the exception to be thrown if the cast operation fails.
 * Should return an instance of [Throwable].
 * @throws Throwable The exception provided by [lazyException] is thrown if the cast operation fails.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
@IgnorableReturnValue
@Deprecated("Use expectClass<T> instead", ReplaceWith("this.expectClass<T>(causeOf = lazyException)"))
inline fun <reified T> Any?.safeCastOrThrow(lazyException: ThrowableSupplier) = runCatching { this as T }.getOrThrow(lazyException = lazyException)

/**
 * Returns the receiver object if the specified condition is true, otherwise returns null.
 *
 * @param condition The condition to evaluate. If true, the receiver object is returned; if false, null is returned.
 * @since 1.0.0
 */
@IgnorableReturnValue
infix fun <T> Supplier<T>.whenTrue(condition: Boolean): T? {
    contract {
        callsInPlace(this@whenTrue, InvocationKind.AT_MOST_ONCE)
        condition holdsIn this@whenTrue
    }
    return if (condition) this() else null
}
/**
 * Provides a conditional extension function that returns the receiver object only
 * if it satisfies a given predicate. Otherwise, it returns null.
 *
 * WARNING: Invoking this method may cause an exception if the throw is determined by the condition.
 *
 * @param predicate A lambda function representing the condition to be evaluated for the receiver object.
 * @since 1.0.0
 */
@JvmName("whenTrueGeneric")
@IgnorableReturnValue
@Deprecated("Use takeIf instead", replaceWith = ReplaceWith("takeIf(predicate)"))
inline infix fun <T> T.whenTrue(predicate: Predicate<T>) = if (predicate(this)) this else null
/**
 * Returns the receiver object if the specified condition is true, otherwise returns null.
 *
 * WARNING: Invoking this method may cause an exception if the throw is determined by the condition.
 *
 * @param condition The condition to evaluate. If true, the receiver object is returned; if false, null is returned.
 * @since 1.0.0
 */
@JvmName("whenTrueGeneric")
@IgnorableReturnValue
@Deprecated("Use takeIf instead", replaceWith = ReplaceWith("takeIf { condition }"))
infix fun <T> T.whenTrue(condition: Boolean) = if (condition) this else null

/**
 * Returns the receiver object if the given condition is false; otherwise, returns null.
 *
 * @param condition A Boolean value that determines whether the receiver object is returned.
 * If the condition evaluates to false, the receiver object is returned; otherwise, null is returned.
 * @since 1.0.0
 */
@IgnorableReturnValue
infix fun <T> Supplier<T>.whenFalse(condition: Boolean): T? {
    contract {
        callsInPlace(this@whenFalse, InvocationKind.AT_MOST_ONCE)
        !condition holdsIn this@whenFalse
    }
    return if (!condition) this() else null
}
/**
 * Returns the current instance if the given predicate evaluates to false. If the predicate evaluates to true, null is returned.
 *
 * WARNING: Invoking this method may cause an exception if the throw is determined by the condition.
 *
 * @param predicate A lambda expression or function reference that takes the current instance as a receiver and returns a boolean value.
 * @since 1.0.0
 */
@JvmName("whenFalseGeneric")
@IgnorableReturnValue
@Deprecated("Use takeUnless instead", replaceWith = ReplaceWith("takeUnless(predicate)"))
inline infix fun <T> T.whenFalse(predicate: Predicate<T>) = if (!predicate(this)) this else null
/**
 * Returns the receiver object if the given condition is false; otherwise, returns null.
 *
 * WARNING: Invoking this method may cause an exception if the throw is determined by the condition.
 *
 * @param condition A Boolean value that determines whether the receiver object is returned.
 * If the condition evaluates to false, the receiver object is returned; otherwise, null is returned.
 * @since 1.0.0
 */
@JvmName("whenFalseGeneric")
@IgnorableReturnValue
@Deprecated("Use takeUnless instead", replaceWith = ReplaceWith("takeUnless { condition }"))
infix fun <T> T.whenFalse(condition: Boolean) = if (!condition) this else null

/**
 * Conditionally applies a transformation to this value based on a predicate.
 *
 * If the predicate evaluates to true when applied to this value, the transformation block is executed
 * with this value as the receiver and its result is returned. Otherwise, this value is returned unchanged.
 *
 * @param T the type of the receiver value
 * @param predicate a function that takes this value as receiver and returns a Boolean indicating whether to apply the transformation
 * @param block a transformation function that takes this value as receiver and returns a transformed value of the same type
 * @return the transformed value if the predicate is true, otherwise this value unchanged
 * @since 4.6.1
 */
@IgnorableReturnValue
inline fun <T> T.letIf(predicate: Predicate<T>, block: MonoTransformer<T>): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (predicate(this)) {
        return block(this)
    }
    return this
}
/**
 * Executes the given block transformation on the receiver if it satisfies the specified predicate.
 *
 * @param predicate A predicate function to test the receiver.
 * @param block A transformation function applied to the receiver when the predicate evaluates to true.
 * @return The transformed value of type [R] if the predicate returns true, otherwise null.
 * @since 4.6.1
 */
@IgnorableReturnValue
inline fun <T, R> T.letIfOrNull(predicate: Predicate<T>, block: Transformer<T, R>): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (predicate(this)) block(this) else null
}
/**
 * Evaluates a predicate on the receiver object and, if it returns true, applies the transformation block with
 * the receiver object. Otherwise, returns the result of the provided default supplier.
 *
 * @param T the type of the receiver object
 * @param R the type of the result
 * @param predicate a function that takes the receiver object and returns a boolean indicating whether the condition is met
 * @param default a supplier function that provides the default result to be returned if the predicate evaluates to false
 * @param block a transformation function that takes the receiver object and returns the result of type R when the predicate evaluates to true
 * @return the result of applying the transformation block when the predicate evaluates to true, or the result of the default supplier when the predicate evaluates to false
 * @since 4.6.1
 */
@IgnorableReturnValue
inline fun <T, R> T.letIfOr(predicate: Predicate<T>, default: Supplier<R>, block: Transformer<T, R>): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (predicate(this)) block(this) else default()
}
/**
 * Conditionally applies a transformation to the receiver if the specified condition is true.
 *
 * This function evaluates the condition and, if true, applies the transformation block to the receiver.
 * If the condition is false, the original receiver is returned unchanged. The transformation block
 * is guaranteed to be called at most once and only when the condition is true.
 *
 * @param T the type of the receiver and return value
 * @param condition the boolean condition that determines whether the transformation should be applied
 * @param block the transformation function to apply to the receiver when the condition is true
 * @return the transformed receiver if condition is true, otherwise the original receiver unchanged
 * @since 2.0.0
 */
@IgnorableReturnValue
inline fun <T> T.letIf(condition: Boolean, block: MonoTransformer<T>): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        condition holdsIn block
    }
    return if (condition) block(this) else this
}
/**
 * Executes the given block of code with the receiver if the specified condition is true.
 * The method evaluates the condition and only invokes the block if the condition holds true.
 *
 * @param condition A Boolean value determining whether the block should be invoked.
 * @param block A transformation block that is executed with the receiver as its input.
 * @return The result of the block if the condition is true, otherwise null.
 * @since 2.0.0
 */
@IgnorableReturnValue
inline fun <T, R> T.letIfOrNull(condition: Boolean, block: Transformer<T, R>): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        condition holdsIn block
        returnsNotNull() implies (condition)
    }
    return if (condition) block(this) else null
}
/**
 * Executes a transformation block on the receiver when the provided condition is true, otherwise returns
 * the result from the default supplier. Ensures the transformation block or the default supplier is invoked at most once
 * with the contracts provided.
 *
 * @param condition A boolean determining which operation to execute. If true, the block is invoked.
 * @param default A supplier function to provide the result when the condition is false.
 * @param block A transformation function applied to the receiver when the condition is true.
 * @return The result of the transformation block if the condition is true, the result of the default supplier if the condition is false, or null in certain cases as per the contracts
 * .
 * @since 2.0.0
 */

@IgnorableReturnValue
inline fun <T, R> T.letIfOr(condition: Boolean, default: Supplier<R>, block: Transformer<T, R>): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        condition holdsIn block
        !condition holdsIn default
    }
    return if (condition) block(this) else default()
}
/**
 * Conditionally transforms this value using the provided block if the predicate returns false.
 *
 * If the predicate evaluates to false, applies the transformation block to this value and returns the result.
 * Otherwise, returns this value unchanged.
 *
 * This is the inverse of a conditional "letWhen" operation - the transformation is applied unless the condition is met.
 *
 * @param T the type of the receiver value
 * @param predicate a function that takes this value as a receiver and returns a boolean condition
 * @param block a transformation function that takes this value as a receiver and returns a transformed value, invoked only when the predicate returns false
 * @return the transformed value if predicate returns false, or this value unchanged if predicate returns true
 * @since 2.0.0
 */
@IgnorableReturnValue
inline fun <T> T.letUnless(predicate: Predicate<T>, block: MonoTransformer<T>): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (!predicate(this)) block(this) else this
}
/**
 * Executes the given [block] if the result of the [predicate] is false. Returns the result of the [block]
 * if the predicate evaluates to false, or null otherwise.
 *
 * @param predicate A lambda function that takes the receiver object [T] and returns a Boolean value.
 *                  If this evaluates to false, the [block] is executed.
 * @param block A lambda function that takes the receiver object [T] and returns a value of type [R],
 *              executed only if the predicate evaluates to false.
 * @return The result of the [block] if the [predicate] evaluates to false, or null otherwise.
 * @since 2.0.0
 */
@IgnorableReturnValue
inline fun <T, R> T.letUnlessOrNull(predicate: Predicate<T>, block: Transformer<T, R>): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (!predicate(this)) block(this) else null
}
/**
 * Executes the provided [block] if the given [predicate] evaluates to false for the receiver object.
 * Otherwise, it returns the result of the [default] supplier.
 *
 * @param T the type of the receiver object.
 * @param R the type of the result produced by the [block] or [default].
 * @param predicate a function that takes the receiver object as input and returns a Boolean value.
 *                  If the predicate evaluates to true, the [default] is executed; otherwise, the [block] is executed.
 * @param default a supplier function that produces a fallback result when the [predicate] evaluates to true.
 * @param block a transformation function applied to the receiver object when the [predicate] evaluates to false.
 * @return the result of applying the [block] to the receiver object if the [predicate] evaluates to false,
 *         or the result of the [default] supplier if the [predicate] evaluates to true. Returns `null` if neither function is executed.
 * @since 2.0.0
 */
@IgnorableReturnValue
inline fun <T, R> T.letUnlessOr(predicate: Predicate<T>, default: Supplier<R>, block: Transformer<T, R>): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (!predicate(this)) block(this) else default()
}
/**
 * Conditionally applies a transformation to the receiver unless the specified condition is true.
 *
 * This function applies the given transformation block to the receiver object when the condition
 * evaluates to false. If the condition is true, the receiver is returned unchanged.
 *
 * @param T the type of the receiver object
 * @param R the return type (not used in this function's signature but required for inline compatibility)
 * @param condition the boolean condition to evaluate; when false, the block is executed
 * @param block the transformation function to apply to the receiver when the condition is false
 * @return the transformed receiver if the condition is false, otherwise the original receiver unchanged
 * @since 2.0.0
 */
@IgnorableReturnValue
inline  fun <T, R> T.letUnless(condition: Boolean, block: MonoTransformer<T>): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        !condition holdsIn block
    }
    return if (!condition) block(this) else this
}
/**
 * Executes the given [block] on the receiver if the [condition] is `false`.
 * If the [condition] is `true`, the method returns `null`.
 *
 * @param T The type of the receiver on which the block is executed.
 * @param R The type of the value returned by the block.
 * @param condition A boolean condition that determines whether the block will be executed.
 *                    If `true`, the block is not executed, and `null` is returned.
 *                    If `false`, the block is executed, and its result is returned.
 * @param block A [ReceiverTransformer] lambda that operates on the receiver and produces a result of type [R].
 * @return Returns the result of the [block] if [condition] is `false`. Returns `null` otherwise.
 * @since 2.0.0
 */
@IgnorableReturnValue
inline fun <T, R> T.letUnlessOrNull(condition: Boolean, block: Transformer<T, R>): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        !condition holdsIn block
        returnsNotNull() implies (!condition)
    }
    return if (!condition) block(this) else null
}
/**
 * Executes the given [block] if the [condition] is false. If the [condition] is true, it executes the [default]
 * supplier instead. This function ensures that the [block] or [default] is invoked based on the specified condition.
 * Contracts are defined to ensure proper control flow and invocation constraints.
 *
 * @param T the type of the receiver parameter.
 * @param R the type of the resulting value.
 * @param condition a Boolean value that determines whether the [block] or [default] is executed.
 * @param default a supplier function providing the default value when the [condition] is true.
 * @param block a transformer that is applied to the receiver to calculate a result when the [condition] is false.
 * @return the result of the [block] when the [condition] is false or the result of [default] when the [condition] is true.
 * @since 2.0.0
 */
@IgnorableReturnValue
inline fun <T, R> T.letUnlessOr(condition: Boolean, default: Supplier<R>, block: Transformer<T, R>): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        !condition holdsIn block
        condition holdsIn default
    }
    return if (!condition) block(this) else default()
}

/**
 * Executes the given block of code and catches exceptions based on the specified criteria.
 * Returns `null` if an exception matching the criteria is caught, otherwise rethrows the exception.
 *
 * @param only A set of exception types to include. If empty, all exception types are included by default.
 * @param except A set of exception types to exclude. Exceptions in this set will not be caught even if included in [only].
 * @param block The block of code to execute. This block is expected to return a value of type [T].
 * @return The result of the block execution if it completes successfully, or `null` if an exception matching the criteria is caught.
 * @throws ParametersInConflictException If both [only] and [except] contain the same exception type.
 * @since 1.0.0
 */
@IgnorableReturnValue
inline fun <T> tryOrNull(
    only: Set<KClass<out Exception>> = emptySet(),
    except: Set<KClass<out Exception>> = emptySet(),
    block: Supplier<T>
): T? {
    (only intersect except).takeIf { it.isNotEmpty() }?.let {
        throw ParametersInConflictException(
            callableName = "tryOrLog",
            parametersName = listOf("only", "except"),
            valuesInConflict = it
        )
    }

    return try {
        block()
    } catch (e: Exception) {
        if (e is InterruptedException || e is CancellationException)
            throw e
        val included = only.isEmpty() || only.any { it.isInstance(e) }
        val excluded = except.any { it.isInstance(e) }
        if (included && !excluded) null else throw e
    }
}

/**
 * Executes the given block of code and logs any exceptions that are thrown, according to the provided logging rules.
 * Returns `null` if an exception is handled and logged.
 *
 * @param T The return type of the block of code to execute.
 * @param message A function to transform a throwable into a pair representing the log level and optional log message.
 * @param specificCases A map of specific exception classes to logging preferences (log level and message).
 *                      These take precedence over other logging configurations.
 * @param includeException Whether to include exception details in the logged output.
 * @param only A set of exception classes to explicitly handle and log. If empty, all exceptions are considered unless excluded.
 * @param except A set of exception classes to explicitly exclude from handling and logging.
 * @param block The block of code to execute.
 * @return The result of the block if no exception occurs, or `null` if an exception is handled and logged.
 *         Exceptions not handled by the specified rules are re-thrown.
 * @throws ParametersInConflictException If the `only` and `except` sets intersect, or if the `specificCases` map keys conflict with the `except` set.
 * @throws InterruptedException If the caught exception is an `InterruptedException`.
 * @throws CancellationException If the caught exception is a `CancellationException`.
 * @since 1.0.0
 */
@IgnorableReturnValue
context(logger: Logger)
inline fun <T> tryOrLog(
    message: Transformer<Exception, Pair<LogLevel?, String?>>,
    specificCases: Map<KClass<out Exception>, Pair<LogLevel?, String?>> = emptyMap(), // priority over only/except
    includeException: Boolean = true,
    only: Set<KClass<out Exception>> = emptySet(),
    except: Set<KClass<out Exception>> = emptySet(),
    block: Supplier<T>
): T? {
    (only intersect except).takeIf { it.isNotEmpty() }?.let {
        throw ParametersInConflictException(
            callableName = "tryOrLog",
            parametersName = listOf("only", "except"),
            valuesInConflict = it
        )
    }
    (specificCases.keys intersect except).takeIf { it.isNotEmpty() }?.let {
        throw ParametersInConflictException(
            callableName = "tryOrLog",
            parametersName = listOf("specificCases", "except"),
            valuesInConflict = it
        )
    }
    return try {
        block()
    } catch (e: Exception) {
        if (e is InterruptedException || e is CancellationException) throw e

        val specific = specificCases.entries
            .filter { [type, _] -> type.isInstance(e) }
            .minWithOrNull { a, b ->
                when {
                    a.key == b.key -> 0
                    a.key.java.isAssignableFrom(b.key.java) -> 1
                    else -> -1
                }
            }
            ?.value

        val handled = specific != null ||
                ((only.isEmpty() || only.any { it.isInstance(e) }) && except.none { it.isInstance(e) })
        if (!handled) throw e

        val default = message(e)
        logWithOrWithoutException(
            logger,
            specific?.first ?: default.first ?: LogLevel.Error,
            specific?.second ?: default.second ?: e.message,
            includeException,
            e
        )
        null
    }
}

/**
 * Executes the given block of code, returning the result or applying fallback strategies in case of exceptions.
 * Allows customization of specific exception handling behavior and optional constraints on default handling logic.
 *
 * @param T The type of the result returned by the block or the fallback functions.
 * @param default A fallback function to be invoked when no specific case or overwrite rule applies.
 * @param specificCases A map associating exception types with specific fallback functions for handling them.
 *                      These functions have the highest priority and take precedence over `overwriteOnly` and `notOverwrite`.
 * @param only A set of exception types for which the default fallback function should be applied, ignoring others.
 *                      Exceptions outside this set will be rethrown unless they have an entry in `specificCases`.
 * @param except A set of exception types for which the default fallback function should not be applied, causing these exceptions to be rethrown unless they have an entry in
 *  `specificCases`.
 * @param block A supplier function representing the primary operation to execute.
 * @return Either the result of the executed block, or the result of the applied fallback strategy in case of an exception.
 *         If rethrow conditions are met and no fallback strategy applies, an uncaught exception is propagated.
 * @throws ParametersInConflictException If the `overwriteOnly` and `notOverwrite` sets have intersection,
 *                                       or if `specificCases` keys intersect with `notOverwrite`.
 * @since 1.0.0
 */
@IgnorableReturnValue
inline fun <T> tryOr(
    default: Transformer<Exception, T>,
    specificCases: Map<KClass<out Exception>, Transformer<Exception, T>> = emptyMap(), // priority over only/except
    only: Set<KClass<out Exception>> = emptySet(),
    except: Set<KClass<out Exception>> = emptySet(),
    block: Supplier<T>
): T {
    if (only intersects except) throw ParametersInConflictException(
        callableName = "tryOr",
        parametersName = listOf("only", "except"),
        valuesInConflict = only intersect except
    )
    if (specificCases.keys intersects except) throw ParametersInConflictException(
        callableName = "tryOr",
        parametersName = listOf("specificCases", "except"),
        valuesInConflict = specificCases.keys intersect except
    )
    return try {
        block()
    } catch (e: Exception) {
        if (e is InterruptedException || e is CancellationException) throw e

        val passes = (only.isEmpty() || only.any { it.isInstance(e) }) && except.none { it.isInstance(e) }
        val specific = generateSequence<Class<*>>(e.javaClass) { it.superclass }
            .firstNotNullOfOrNull { specificCases[it.kotlin] }

        when {
            specific != null -> specific(e)
            passes -> default(e)
            else -> throw e
        }
    }
}

/**
 * Attempts to execute a given block of code and determines the resulting boolean outcome
 * based on the provided specific exception handling rules.
 *
 * This method evaluates a block of code and handles exceptions with customizable behavior.
 * It provides mechanisms for defining specific cases, overwriting behavior, or excluding
 * specific exceptions from being handled.
 *
 * @param specificCases A map defining specific exceptions and their associated transformations
 * to return boolean values. If an exception type from this map is encountered, the corresponding
 * transformer is applied. Default is an empty map.
 * @param only A set of exception types for which the handling behavior is restricted to overwriting.
 * Any exceptions not in this set will not be caught. Default is an empty set.
 * @param except A set of exception types that are explicitly excluded from being handled.
 * If an exception in this set is encountered, it will be thrown. Default is an empty set.
 * @param block An action block of code to execute which potentially throws an exception.
 * @return True if the block executes successfully without exceptions or if an exception is handled
 * as true based on the provided rules. False if an exception occurs and is handled as such.
 * Throws the exception if it does not meet any handling conditions.
 * @since 1.0.0
 */
@IgnorableReturnValue
inline fun tryTrueOrFalse(
    specificCases: Map<KClass<out Exception>, Transformer<Exception, Boolean>> = emptyMap(), // priority over only/except
    only: Set<KClass<out Exception>> = emptySet(),
    except: Set<KClass<out Exception>> = emptySet(),
    block: Action
): Boolean = tryOr(
    default = { false },
    specificCases = specificCases,
    only = only,
    except = except
) {
    block()
    true
}

/**
 * Executes the provided block of code and handles thrown exceptions based on the specified rules.
 *
 * This function attempts to execute the supplied block. If an exception is thrown, it applies custom handling based on
 * the provided parameters, such as specific cases for exceptions, inclusion of causes, or overwriting rules. If none of
 * the conditions are met, it propagates the exception or throws a new one as defined.
 *
 * @param lazyException A transformer for creating a throwable to be thrown if no specific case or rules apply.
 * @param specificCases A map of specific exception types to their respective throwable suppliers. If a caught exception matches a key
 * in this map, the corresponding supplier is invoked to provide the exception to be thrown.
 * @param includeCause Determines whether the original exception should be set as the cause of the newly thrown exception.
 * Default is true.
 * @param only A set of exception types. If a caught exception’s type is included in this set, and not in the
 * `notOverwrite` set, a new exception from `lazyException` is thrown, optionally including the original exception as its cause.
 * @param except A set of exception types that should not be overwritten, even if they are present in the `overwriteOnly` set.
 * @param block The block of code to be executed. If it completes without throwing an exception, its result is returned.
 *
 * @return The result of the executed block if no exception is thrown or if exceptions are handled and not propagated.
 *
 * @throws ParametersInConflictException If there is a conflict between the values of `overwriteOnly` and `notOverwrite`,
 * or between the keys of `specificCases` and the `notOverwrite` set.
 * @throws Throwable If a specific case, overwrite rules, or new exception rules are not applicable,
 * the original or newly created exception is propagated.
 *
 * @since 1.0.0
 */
@IgnorableReturnValue
inline fun <T> tryOrThrow(
    lazyException: ExceptionTransformer,
    specificCases: Map<KClass<out Exception>, ExceptionTransformer> = emptyMap(), // priority over only/except
    includeCause: Boolean = true,
    only: Set<KClass<out Exception>> = emptySet(),
    except: Set<KClass<out Exception>> = emptySet(),
    block: Supplier<T>
): T {
    if (only intersects except) throw ParametersInConflictException(
        callableName = "tryOrThrow",
        parametersName = listOf("only", "except"),
        valuesInConflict = only intersect except
    )
    if (specificCases.keys intersects except) throw ParametersInConflictException(
        callableName = "tryOrThrow",
        parametersName = listOf("only", "except"),
        valuesInConflict = specificCases.keys intersect except
    )

    return try {
        block()
    } catch (e: Exception) {
        if (e is InterruptedException || e is CancellationException) throw e

        val passes = (only.isEmpty() || only.any { it.isInstance(e) }) && except.none { it.isInstance(e) }
        val specific = generateSequence<Class<*>>(e.javaClass) { it.superclass }
            .firstNotNullOfOrNull { specificCases[it.kotlin] }

        when {
            specific != null -> throwWithOrWithoutCause(specific, includeCause, e)
            passes -> throwWithOrWithoutCause(lazyException, includeCause, e)
            else -> throw e
        }
    }
}
/**
 * Throws an exception supplied by the provided `laxyException` function.
 * Optionally includes a cause for the thrown exception if `includeCause` is true.
 *
 * @param laxyException A supplier function that provides the exception to be thrown.
 * @param includeCause Indicates whether to include the specified cause for the thrown exception.
 * @param e The throwable to potentially set as the cause of the exception.
 * @return Nothing, since this function always throws an exception.
 * @since 1.0.0
 */
@PublishedApi
internal inline fun throwWithOrWithoutCause(laxyException: ExceptionSupplier, includeCause: Boolean, e: Exception): Nothing =
    throw if (includeCause) laxyException() withRootCause e else laxyException()
/**
 * Throws an exception supplied by the provided `laxyException` function.
 * Optionally includes a cause for the thrown exception if `includeCause` is true.
 *
 * @param laxyException A supplier function that provides the exception to be thrown.
 * @param includeCause Indicates whether to include the specified cause for the thrown exception.
 * @param e The throwable to potentially set as the cause of the exception.
 * @return Nothing, since this function always throws an exception.
 * @since 1.0.0
 */
@PublishedApi
internal inline fun throwWithOrWithoutCause(laxyException: ExceptionTransformer, includeCause: Boolean, e: Exception): Nothing =
    throw if (includeCause) laxyException(e) withRootCause e else laxyException(e)
/**
 * Logs a message at the specified logging level, with or without an associated exception.
 *
 * @param logger the logger instance used to log the message
 * @param level the logging level at which the message should be logged
 * @param message the log message, can be null
 * @param includeException flag indicating whether the exception should be included in the log
 * @param e the exception to include in the log if `includeException` is true
 * @since 1.0.0
 */
@PublishedApi
internal fun logWithOrWithoutException(logger: Logger, level: LogLevel, message: String?, includeException: Boolean, e: Exception) {
    if (includeException) log(logger, level, message ?: e.message.orEmpty(), e)
    else log(logger, level, message ?: e.message.orEmpty())
}

/**
 * Executes the given block of code, returning the result if successful or an error of the specified type
 * in case of an exception. Supports configurable behavior for handling specific exception cases.
 *
 * @param T The type of the result produced by the successful execution of the block.
 * @param E The type of the error returned in case of an exception.
 * @param error A transformer to map generic exceptions to a domain-specific error of type [E].
 * @param specificCases A map of specific exception types to transformers that map them to
 * corresponding errors of type [E]. These transformers take precedence over other configurations.
 * @param only A set of exception types that are explicitly allowed to be mapped to errors.
 * If this set is not empty, exceptions outside this set will be re-thrown instead of being transformed.
 * @param except A set of exception types to be explicitly excluded from error transformation.
 * Exceptions in this set will always be re-thrown and not transformed, even if present in [only] or [specificCases].
 * @param block The code block to attempt executing.
 * @return An [Either] instance where the left value represents an error of type [E] in case of an exception,
 * or the right value represents the result of the successful execution of the block.
 * @since 6.1.0
 * */
@IgnorableReturnValue
inline fun <T, E : Error> tryOrError(
    error: Transformer<Exception, E>,
    specificCases: Map<KClass<out Exception>, Transformer<Exception, E>> = emptyMap(), // priority over only/except
    only: Set<KClass<out Exception>> = emptySet(),
    except: Set<KClass<out Exception>> = emptySet(),
    block: Supplier<T>
): Either<E, T> = either {
    if (only intersects except) throw ParametersInConflictException(
        callableName = "tryOrError",
        parametersName = listOf("only", "except"),
        valuesInConflict = only intersect except
    )
    if (specificCases.keys intersects except) throw ParametersInConflictException(
        callableName = "tryOrError",
        parametersName = listOf("only", "except"),
        valuesInConflict = specificCases.keys intersect except
    )

    try {
        block()
    } catch (e: Exception) {
        if (e is InterruptedException || e is CancellationException) throw e

        val passes = (only.isEmpty() || only.any { it.isInstance(e) }) && except.none { it.isInstance(e) }
        val specific = generateSequence<Class<*>>(e.javaClass) { it.superclass }
            .firstNotNullOfOrNull { specificCases[it.kotlin] }

        when {
            specific != null -> raise(specific(e))
            passes -> raise(error(e))
            else -> throw e
        }
    }
}
/**
 * Executes the provided [block] and handles exceptions by either raising an error or re-throwing.
 * This method allows fine-grained control over exception handling by specifying specific cases,
 * inclusion filters ([only]), and exclusion filters ([except]).
 *
 * The [block] is executed in the current context, and exceptions are handled based on the following rules:
 * - If the exception is of type [InterruptedException] or [CancellationException], it is re-thrown immediately.
 * - If specific cases are provided in [specificCases], they take precedence over other filters.
 * - If an exception matches the [only] set, it is processed unless it is excluded by the [except] set.
 * - If no specific handling or inclusion logic matches, the exception is passed through unhandled.
 *
 * @param error A transformer function mapping generic exceptions to a specific error type [E].
 * @param specificCases A map of specific exception classes to transformer functions. These take precedence over other filters.
 * @param only A set of exception classes to be processed. If empty, all exceptions will be considered.
 * @param except A set of exception classes to be excluded from processing. Takes precedence over [only].
 * @param block The code block to execute, potentially throwing exceptions to handle.
 * @return The result of the execution of [block], if no exception causes it to raise or fail.
 * @since 6.1.0
 */
@IgnorableReturnValue
context(raise: Raise<E>)
inline fun <T, E> tryOrRaise(
    error: Transformer<Exception, E>,
    specificCases: Map<KClass<out Exception>, Transformer<Exception, E>> = emptyMap(), // priority over only/except
    only: Set<KClass<out Exception>> = emptySet(),
    except: Set<KClass<out Exception>> = emptySet(),
    block: Supplier<T>
): T {
    if (only intersects except) throw ParametersInConflictException(
        callableName = "tryOrError",
        parametersName = listOf("only", "except"),
        valuesInConflict = only intersect except
    )
    if (specificCases.keys intersects except) throw ParametersInConflictException(
        callableName = "tryOrError",
        parametersName = listOf("only", "except"),
        valuesInConflict = specificCases.keys intersect except
    )

    return try {
        block()
    } catch (e: Exception) {
        if (e is InterruptedException || e is CancellationException) throw e

        val passes = (only.isEmpty() || only.any { it.isInstance(e) }) && except.none { it.isInstance(e) }
        val specific = generateSequence<Class<*>>(e.javaClass) { it.superclass }
            .firstNotNullOfOrNull { specificCases[it.kotlin] }

        when {
            specific != null -> raise.raise(specific(e))
            passes -> raise.raise(error(e))
            else -> throw e
        }
    }
}

/**
 * Converts the receiver object to a string representation in a safe manner.
 * If the object is null, the string "null" is returned.
 * If the object is an array, a deep string representation of the array is returned.
 * Otherwise, the object's `toString()` method is used.
 *
 * @receiver The object to be converted to a string.
 * @return A string representation of the receiver, handling null and array types safely.
 * @since 1.0.0
 */
fun Any?.toSafeString(): String = when (this) {
    null -> "null"
    is Array<*> -> contentDeepToString()
    is ByteArray -> contentToString()
    is ShortArray -> contentToString()
    is LongArray -> contentToString()
    is FloatArray -> contentToString()
    is DoubleArray -> contentToString()
    is BooleanArray -> contentToString()
    is CharArray -> contentToString()
    else -> toString()
}

/**
 * Repeatedly applies a given function to an object a specified number of times.
 *
 * @receiver The initial object to which the function is applied.
 * @param times the number of times the function should be applied. Must be non-negative;
 * passing a non-positive value will result in the method returning the initial object.
 * @param transformer the function to apply to the object.
 * @return the result of applying the function to the object the specified number of times,
 * or the initial object if times is non-positive.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.repeat(times: Int, transformer: MonoTransformer<T>): T {
    if (times <= 0) return this

    var result = this
    for (i in 0..<times) {
        result = transformer(result)
    }
    return result
}

/**
 * Serializes the current object into its JSON string representation.
 *
 * This extension function utilizes `JSONUtils.toJson` to convert the object
 * into a JSON-encoded string, ensuring consistent serialization across the application.
 *
 * @receiver Any object to be serialized.
 * @return The JSON string representation of the object.
 * @since 1.0.0
 */
fun Any?.serialize() = MAPPER.writeValueAsString(this)!!
// deserialize method is in StringUtils

/**
 * Attempts to convert a string to an enum value.
 *
 * Case insensitive.
 *
 * @receiver The string to be converted
 * @param enumClass The enum class to convert to
 * @return The enum value if successful, null otherwise
 * @since 1.0.0
 */
inline infix fun <reified T : Enum<T>> String.like(enumClass: Class<T>): T? {
    val candidates = matchedCases.ifEmpty { listOf(TextCase.Standard) }
    return enumValues<T>().firstOrNull { entry ->
        candidates.any { case -> convertCase(from = case, to = TextCase.PascalCase) == entry.name }
    }
}
/**
 * Attempts to convert a string to an enum value.
 *
 * Case insensitive.
 *
 * @receiver The string to be converted
 * @param enumClass The enum class to convert to
 * @return The enum value if successful, null otherwise
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
inline infix fun <reified T : Enum<T>> String.like(enumClass: KClass<T>): T? {
    val candidates = matchedCases.ifEmpty { listOf(TextCase.Standard) }
    return enumValues<T>().firstOrNull { entry ->
        candidates.any { case -> convertCase(from = case, to = TextCase.PascalCase) == entry.name }
    }
}

/**
 * Attempts to convert a string to an enum value using valueOf.
 *
 * @receiver The string to be converted
 * @return The enum value if successful
 * @throws NoSuchEntryException if the string does not match any enum value.
 * @since 1.0.0
 */
inline fun <reified T : Enum<T>> String.toEnumConst(): T {
    val candidates = matchedCases.ifEmpty { listOf(TextCase.Standard) }
    return enumValues<T>().findFirstOrThrow({ NoSuchEntryException(T::class, this) }) { entry ->
        candidates.any { case -> convertCase(from = case, to = TextCase.PascalCase) equalsIgnoreCase entry.name }
    }
}

/**
 * Wraps the given value into a singleton list.
 *
 * @return a list containing only this instance.
 * @since 1.0.0
 */
fun <T> T.asSingleList(): List<T> = listOf(this)
/**
 * Converts the current object into an MList containing only this object as its single element.
 *
 * @return an MList containing the current object as its single element.
 * @since 1.0.0
 */
fun <T> T.asSingleMList(): MList<T> = mListOf(this)
/**
 * Wraps the current element into a singleton set containing only this element.
 *
 * @return a set containing the current element as its sole member.
 * @since 1.0.0
 */
fun <T> T.asSingleSet(): Set<T> = setOf(this)
/**
 * Converts the current object into a mutable set containing only this object as its single element.
 *
 * @return A mutable set containing the current object as its only element.
 * @since 1.0.0
 */
fun <T> T.asSingleMSet(): MSet<T> = mSetOf(this)
/**
 * Converts the current key to a single-entry map with the specified value.
 *
 * @param value The value to associate with the key in the resulting map.
 * @return A map containing the current key associated with the specified value.
 * @since 1.0.0
 */
fun <K, V> K.asSingleMap(value: V): Map<K, V> = mapOf(this to value)
/**
 * Converts the current value to a single-entry map with the specified key.
 *
 * @param key The key to associate with the value in the resulting map.
 * @return A map containing the current value associated with the specified value.
 * @since 1.0.0
 */
fun <K, V> V.asSingleMapValue(key: K): Map<K, V> = mapOf(key to this)
/**
 * Creates a single-pair mutable map with the current object as the key and the specified value.
 *
 * This function allows for constructing a mutable map (`MMap`) containing a single key-value pair,
 * where the key is the current object (`this`) and the value is the provided argument.
 *
 * @param value the value associated with the key represented by the current object
 * @return a mutable map containing the single key-value pair
 * @since 1.0.0
 */
fun <K, V> K.asSingleMMap(value: V): MMap<K, V> = mMapOf(this to value)
/**
 * Converts the current value to a single-entry mutable map with the specified key.
 *
 * @param key The key to associate with the value in the resulting map.
 * @return A map containing the current value associated with the specified value.
 * @since 1.0.0
 */
fun <K, V> V.asSingleMMapValue(key: K): Map<K, V> = mapOf(key to this)
/**
 * Converts this pair into a map containing a single key-value pair.
 *
 * @return a map containing the key from the first component of the pair and the value from the second component.
 * @since 1.0.0
 */
fun <K, V> Pair<K, V>.asSingleMap(): Map<K, V> = mapOf(this)
/**
 * Converts the current pair into a single-entry mutable map.
 *
 * This function creates a new `MMap` containing the current pair as its only entry.
 *
 * @return a new mutable map containing a single key-value pair from this `Pair`
 * @since 1.0.0
 */
fun <K, V> Pair<K, V>.asSingleMMap(): MMap<K, V> = mMapOf(this)
/**
 * Creates a map where the key and value are both the instance itself.
 *
 * @return A map containing a single entry where the instance is both the key and the value.
 * @since 1.0.0
 */
fun <T> T.asSelfMap(): Map<T, T> = mapOf(this to this)
/**
 * Creates a mutable map with the current object as both the key
 * and the value, effectively mapping the object to itself.
 *
 * This function is particularly useful for initializing maps where objects
 * are intended to be self-referencing keys and values.
 *
 * @return a new mutable map where the current object is both the key and the value
 * @since 1.0.0
 */
fun <T> T.asSelfMMap(): MMap<T, T> = mMapOf(this to this)

/**
 * Creates a StringMap by associating the current string as a key with the provided value string.
 *
 * @param value The string value to map with the current string key.
 * @return A StringMap containing the current string as the key and the provided value as the associated value.
 * @since 3.12.2
 */
infix fun String.mapWith(value: String): StringMap = mapOf(this to value)
/**
 * Creates a map containing the receiver as the key and the provided value as the associated value.
 *
 * @param value The value to associate with the receiver key in the resulting map.
 * @return A map containing a single entry with the receiver as the key and the provided value as its value.
 * @since 3.12.2
 */
@JvmName("mapWithGeneric")
infix fun <K, V> K.mapWith(value: V): Map<K, V> = mapOf(this to value)
/**
 * Creates a `StringMMap` containing a single key-value pair with the provided string as the key
 * and the specified value.
 *
 * @param value the value to be associated with the key (this string)
 * @return a `StringMMap` containing the key-value pair
 * @since 3.12.2
 */
infix fun String.mMapWith(value: String): StringMMap = mMapOf(this to value)
/**
 * Creates a new mutable map entry by associating the given key with the specified value.
 *
 * This infix function provides a convenient way to create a `MMap` instance
 * containing a single key-value pair.
 *
 * @param value the value to be associated with the key
 * @return a new `MMap` containing the provided key-value pair
 * @since 3.12.2
 */
@JvmName("mMapWithGeneric")
infix fun <K, V> K.mMapWith(value: V): MMap<K, V> = mMapOf(this to value)
/**
 * Creates a map with the provided key and the string as the value.
 *
 * @param key The key to associate with the string in the resulting map.
 * @return A map containing the given key and this string as the key-value pair.
 * @since 3.12.2
 */
infix fun String.mapWithKey(key: String): StringMap = mapOf(key to this)
/**
 * Creates a map containing a single key-value pair, where the key is provided as a parameter
 * and the value is the instance on which this method is called.
 *
 * @param key The key to associate with the value in the map.
 * @return A map containing the specified key and the instance as the value.
 * @since 3.12.2
 */
@JvmName("mapWithKeyGeneric")
infix fun <K, V> V.mapWithKey(key: K): Map<K, V> = mapOf(key to this)
/**
 * Creates a new `StringMMap` by associating the provided key with the current string value.
 *
 * @param key the key to associate with the current string
 * @return a `StringMMap` containing a single key-value pair where the key is the provided key and
 *         the value is the current string
 * @since 3.12.2
 */
infix fun String.mMapWithKey(key: String): StringMMap = mMapOf(key to this)
/**
 * Associates the provided key with the calling value and returns a new mutable map containing this key-value pair.
 *
 * @param key the key to associate with the calling value
 * @return a new mutable map containing the provided key-value pair
 * @since 3.12.2
 */
@JvmName("mMapWithKeyGeneric")
infix fun <K, V> V.mMapWithKey(key: K): MMap<K, V> = mMapOf(key to this)

/**
 * Prints the integer value to the standard output.
 *
 * This extension function allows you to directly print an integer
 * using the `print` function of Kotlin.
 *
 * @receiver The integer value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Int.print() = apply { print(this) }
/**
 * Prints the value of the Byte to the standard output stream.
 *
 * This function uses the standard `print` method to output
 * the Byte value without any additional formatting or line breaks.
 *
 * @receiver The Byte value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Byte.print() = apply { print(this) }
/**
 * Prints the character to the standard output.
 *
 * This method invokes the `print` function to display the given character.
 *
 * @receiver The character to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Char.print() = apply { print(this) }
/**
 * Prints the value of the Long instance to the standard output.
 *
 * This function utilizes the `print` function from the Kotlin standard library
 * to output the value of the current Long receiver.
 *
 * @receiver Long value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Long.print() = apply { print(this) }
/**
 * Prints the current `Float` value to the standard output.
 *
 * This function delegates the printing task to the standard `print` function
 * and displays the float value as a string.
 *
 * @receiver The `Float` value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Float.print() = apply { print(this) }
/**
 * Prints the value of the current `Short` instance to the standard output.
 *
 * This function uses Kotlin's standard `print` function to display the value
 * of the `Short` on the same line without appending a newline character.
 *
 * @receiver The `Short` value to be printed.
 *
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Short.print() = apply { print(this) }
/**
 * Prints the value of the Double instance to the standard output.
 *
 * This method wraps the [kotlin.io.print] function for the Double type, allowing
 * instances of Double to be directly printed using member function syntax.
 *
 * @receiver The Double value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Double.print() = apply { print(this) }
/**
 * Prints the Boolean value to the standard output.
 *
 * This method outputs the current Boolean value (`true` or `false`)
 * to the console using the `print` function.
 *
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Boolean.print() = apply { print(this) }
/**
 * Prints the contents of the CharArray to the standard output.
 *
 * This function writes all characters in the CharArray to the console or the standard output stream.
 * Each character in the array is outputted sequentially without any additional formatting or separators.
 *
 * Note that the behavior of this function is synonymous with passing the CharArray directly to
 * the standard print function.
 *
 * @receiver The CharArray whose content will be printed to the standard output.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun CharArray.print() = apply { print(toSafeString()) }
/**
 * Prints the result of applying the specified transformer to the current object.
 *
 * @param transfor A transformer function that converts the current object into a string representation.
 * @since 3.10.0
 */
@JvmName("printGeneric")
@IgnorableReturnValue
fun <T> T.print(transfor: Transformer<T, Any?> = { it.toSafeString() }): T {
    contract {
        callsInPlace(transfor, InvocationKind.EXACTLY_ONCE)
    }
    return apply { print(transfor(this)) }
}

/**
 * Prints the integer receiver to the standard output followed by a newline.
 *
 * The method delegates the printing operation to the standard `println` function.
 *
 * @receiver The integer to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Int.println() = apply { println(this) }
/**
 * Prints the Byte value to the standard output followed by a newline.
 *
 * This extension function enhances the Byte type by providing a direct way to
 * output its value to the console.
 *
 * The function utilizes the standard `println` to perform the output operation.
 *
 * @receiver The Byte value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Byte.println() = apply { println(this) }
/**
 * Prints the character value of the receiver to the standard output followed by a line break.
 *
 * This function delegates to the standard `println` function in Kotlin, which appends a line
 * separator after printing the value.
 *
 * @receiver The character to be printed to the standard output.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Char.println() = apply { println(this) }
/**
 * Prints the `Long` value to the standard output.
 *
 * This method is an extension function for the `Long` type, enabling a concise way
 * to output its value directly.
 *
 * @receiver The `Long` value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Long.println() = apply { println(this) }
/**
 * Prints the float value to the standard output followed by a newline.
 *
 * This method is an extension function for the `Float` type, allowing
 * direct invocation on a `Float` instance to print its value.
 *
 * @receiver The `Float` value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Float.println() = apply { println(this) }
/**
 * Prints the value of the Short receiver to the standard output, followed by a newline.
 *
 * This method leverages the standard println function for output.
 *
 * @receiver The Short value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Short.println() = apply { println(this) }
/**
 * Prints the value of the Double to the standard output followed by a newline character.
 *
 * This function is an extension for the Double type and simplifies the process
 * of printing a Double value directly.
 *
 * @receiver The Double value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Double.println() = apply { println(this) }
/**
 * Prints the boolean value to the standard output followed by a newline.
 *
 * This method invokes the standard `println` function to display
 * the boolean value (`true` or `false`) represented by the receiver.
 *
 * @receiver the boolean value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Boolean.println() = apply { println(this) }
/**
 * Prints the contents of the CharArray to the standard output, followed by a newline character.
 *
 * The function delegates the operation to the `println` function, which converts
 * the CharArray to a string representation before printing.
 *
 * @receiver The CharArray to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun CharArray.println() = apply { println(toSafeString()) }
/**
 * Prints the string representation of the receiver object to the standard output.
 * A custom transformation function can be provided to define how the object is
 * converted into a string before printing. By default, a safe string representation
 * is used.
 *
 * @param transfor A transformation function that converts the receiver object into
 *                 a string before printing. The default behavior converts the object
 *                 to a safe string representation.
 * @since 3.10.0
 */
@IgnorableReturnValue
@JvmName("printlnGeneric")
fun <T> T.println(transfor: Transformer<T, Any?> = { it.toSafeString() }): T {
    contract {
        callsInPlace(transfor, InvocationKind.EXACTLY_ONCE)
    }
    return apply { println(transfor(this)) }
}

/**
 * Prints the integer value to the standard error stream.
 *
 * This extension function allows an integer to be directly printed
 * to the standard error stream (System.err).
 *
 * @receiver The integer to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Int.printErr() = apply { System.err.print(this) }
/**
 * Prints the byte value to the standard error stream without a newline.
 * This method writes the byte value directly as is, using the standard error output.
 *
 * @receiver Byte value to be printed to the standard error stream.
 *
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Byte.printErr() = apply { System.err.print(this) }
/**
 * Prints the character to the standard error output stream.
 *
 * This function sends the provided character to the `System.err` stream.
 * It is useful for logging error messages or debugging purposes when
 * standard error output is required.
 *
 * @receiver The character to be printed to the error output stream.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Char.printErr() = apply { System.err.print(this) }
/**
 * Prints the value of the Long to the standard error stream.
 *
 * This extension function is a convenient way to write the value of the Long
 * instance directly to the error output without requiring additional formatting.
 *
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Long.printErr() = apply { System.err.print(this) }
/**
 * Prints the value of the Float to the standard error stream.
 *
 * This method writes the Float value directly to the `System.err` output stream.
 *
 * @receiver The Float value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Float.printErr() = apply { System.err.print(this) }
/**
 * Prints the string to the standard error output stream (System.err) without adding a newline.
 *
 * This function allows chaining operations on the resulting string.
 *
 * @receiver The string to be printed to the standard error output stream.
 * @return The original string, allowing further chaining operations.
 * @since 4.8.0
 */
@IgnorableReturnValue
fun String.printErr() = apply { System.err.print(this) }
/**
 * Prints the double value to the standard error stream (`System.err`).
 *
 * This method provides a convenient way to send the numeric value
 * represented by the Double to the error output stream.
 *
 * @receiver The Double value to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Double.printErr() = apply { System.err.print(this) }
/**
 * Prints the Boolean value to the standard error stream.
 *
 * This function outputs the Boolean value (`true` or `false`) to `System.err`.
 * It can be used for debugging or logging purposes where error output is required.
 *
 * @receiver The Boolean value to be printed to the error stream.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Boolean.printErr() = apply { System.err.print(this) }
/**
 * Prints the contents of the `CharArray` to the standard error output stream.
 * Each character in the array is written in sequence.
 *
 * @receiver The `CharArray` whose contents are to be printed.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun CharArray.printErr() = apply { System.err.print(toSafeString()) }
/**
 * Prints the string representation of the receiver object to the standard error stream.
 * The transformation of the receiver object to a string is handled by the provided transformer function.
 *
 * @param transform A lambda function that converts the receiver object to a string.
 *                  The default transformer uses `toSafeString()` to generate a safe string representation.
 * @since 3.10.0
 */
@JvmName("printErrGeneric")
@IgnorableReturnValue
fun <T> T.printErr(transform: Transformer<T, Any?> = { it.toSafeString() }): T {
    contract {
        callsInPlace(transform, InvocationKind.EXACTLY_ONCE)
    }
    return apply { System.err.println(transform(this)) }
}

/**
 * Extension function for the [Int] type that outputs the integer
 * to the standard error stream.
 *
 * This method uses `System.err.println` to print the integer value.
 *
 * @receiver the integer value to be printed to the error stream.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Int.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the value of the Byte to the standard error output stream.
 *
 * This function writes the byte value to the `System.err` stream,
 * using the `println` function to append a newline after the value.
 *
 * @receiver The Byte value to be printed to the error output stream.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Byte.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the character to the standard error output stream followed by a line terminator.
 *
 * This function is useful for logging or error reporting where output is directed to the standard error stream.
 *
 * @receiver The character to be printed to the standard error output stream.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Char.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the value of the `Long` to the standard error output stream.
 *
 * This method is an extension function for the `Long` type, enabling
 * direct invocation on `Long` instances to output their value to `System.err`.
 *
 * @receiver The `Long` value to be printed to the error stream.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Long.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the floating-point number to the standard error stream.
 *
 * This method writes the value of the floating-point number
 * to the error output (System.err) followed by a new line.
 *
 * @receiver The floating-point number to be printed to the error stream.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Float.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the string to the standard error output stream.
 *
 * This function sends the string content to `System.err.println`,
 * allowing messages or errors to be logged to the error stream.
 *
 * @receiver The string to be printed to the error stream.
 * @return The original string (`this`) to allow method chaining.
 * @since 4.8.0
 */
@IgnorableReturnValue
fun String.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the double value of the current instance to the standard error stream.
 *
 * This function is an extension on the `Double` type and outputs the value
 * using `System.err.println`.
 *
 * @receiver The double value to be printed to the error stream.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Double.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the value of the Boolean to the standard error stream.
 *
 * This function outputs the Boolean value (`true` or `false`)
 * as a string to the error output stream.
 *
 * @receiver The Boolean value to be printed to the error output stream.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun Boolean.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the content of the `CharArray` to the standard error output stream.
 *
 * This method converts the `CharArray` to its string representation and sends it to `System.err`
 * for error or debug output purposes.
 *
 * @receiver The `CharArray` whose contents will be printed to the error stream.
 *
 * @since 1.0.0
 */
@IgnorableReturnValue
fun CharArray.printlnErr() = apply { System.err.println(toSafeString()) }
/**
 * Prints the current object to the standard error stream after applying the specified transformation.
 *
 * @param transform A lambda or function that transforms the current object of type [T]
 *                  into a string representation. Defaults to a safe string conversion.
 * @return The current object of type [T], allowing method chaining.
 * @since 3.10.0
 */
@JvmName("printlnErrGeneric")
@IgnorableReturnValue
fun <T> T.printlnErr(transform: Transformer<T, Any?> = { it.toSafeString() }): T {
    contract {
        callsInPlace(transform, InvocationKind.EXACTLY_ONCE)
    }
    return apply { System.err.println(transform(this)) }
}