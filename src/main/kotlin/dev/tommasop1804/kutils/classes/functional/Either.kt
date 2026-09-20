/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.functional

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.functional.Either.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Represents a value of one of two possible types, commonly referred to as a disjoint union.
 * Instances of `Either` are either an instance of `Left`, holding a value of type `L`, or
 * an instance of `Right`, holding a value of type `R`.
 *
 * This pattern is often used to model operations that can succeed or fail, with `Right` representing
 * success and containing the resulting value, and `Left` representing failure and containing details
 * about the failure.
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
sealed interface Either<out L, out R> {
    /**
     * Returns `true` if this instance is of type `Right`, otherwise `false`.
     * This property allows determining if the `Either` holds a right value.
     * @since 5.2.0
     */
    val isRight get() = this is Right
    /**
     * Indicates whether the current instance of `Either` is of type `Left`.
     * Returns `true` if the instance is `Left`, otherwise `false`.
     * @since 5.2.0
     */
    val isLeft get() = this is Left

    /**
     * Invokes the instance to retrieve the encapsulated value based on its type.
     *
     * This operator function determines whether the current instance is of type `Left` or `Right`
     * and returns the value associated with the respective type.
     *
     * @return The value contained in the `Left` or `Right` instance.
     * @since 6.1.0
     */
    operator fun invoke(): R = when (this) {
        is Left -> throw ErrorInvokedException(this)
        is Right -> this.value
    }

    /**
     * Overloads the not (`!`) operator to provide a custom implementation.
     *
     * This method is invoked when the `!` operator is used on an instance of the class.
     * It returns the value of the `isLeft` property, which is expected to be a boolean.
     *
     * @return The negation result based on the `isLeft` property.
     * @since 5.2.0
     */
    operator fun not() = isLeft

    /**
     * Represents the "Left" side of an `Either` type, typically used to hold an error or non-success value.
     *
     * `Left` is a generic data class that holds a single value of type `L`. It is commonly used in functional
     * programming contexts to represent computations or operations that can return one of two possible values:
     * success (on the "Right") or failure (on the "Left").
     *
     * This class extends the `Either` sealed class and is parameterized to contain only the left-hand type `L`,
     * with the right-hand type being `Nothing`.
     *
     * @param value The value of type `L` representing the "Left" side of the `Either`.
     * @since 5.2.0
     * @author Tommaso Pastorelli
     */
    data class Left<out L>(val value: L) : Either<L, Nothing>
    /**
     * Represents the successful or "right" value in the context of the `Either` type.
     *
     * The `Right` class is used to store a value of type `R` to signify a non-error or expected result.
     * It extends the `Either` sealed class, specifically as the successful outcome counterpart to `Left`.
     *
     * @param R The type of the value wrapped by this instance of `Right`.
     * @property value The successful value held by this instance.
     * @since 5.2.0
     * @author Tommaso Pastorelli
     */
    data class Right<out R>(val value: R) : Either<Nothing, R>
}

/**
 * Performs a transformation on the value contained within the `Right` instance of this `Either`
 * and flattens the result into a single `Either`. If this instance is a `Left`, it is returned as is.
 *
 * @param f A function that takes the value of type `R` from the `Right` instance and returns an `Either<L, T>`.
 * @return An `Either` resulting from applying the transformation function to the value of the `Right` instance,
 *         or the same `Left` instance if this is a `Left`.
 * @since 5.2.0
 */
inline fun <L, R, T> Either<L, R>.flatMap(f: Transformer<R, Either<@UnsafeVariance L, T>>): Either<L, T> = when (this) {
    is Left -> this
    is Right -> f(value)
}

/**
 * Transforms the left component of this `Either` using the provided function, if it is a `Left`.
 * If this is a `Right`, the function is not applied and this instance is returned as is.
 *
 * @param f The transformation function to apply to the left value if this is a `Left`.
 * @return A new `Either` instance with the transformed left value if this is a `Left`,
 *         or the same instance if this is a `Right`.
 * @since 5.2.0
 */
inline fun <L, R, T> Either<L, R>.mapLeft(f: Transformer<L, T>): Either<T, R> = when (this) {
    is Left -> Left(f(value))
    is Right -> this
}

/**
 * Transforms the value contained in a `Right` instance using the provided mapping function.
 * If the instance is `Left`, it remains unchanged.
 *
 * @param f A function that takes the value of type `R` from a `Right` instance and maps it to a value of type `T`.
 * @return An `Either` instance: if this is `Right`, it returns a new `Right` with the mapped value; if this is `Left`, it returns the same `Left`.
 * @since 5.2.0
 */
inline fun <L, R, T> Either<L, R>.map(f: Transformer<R, T>): Either<L, T> = when (this) {
    is Left -> this
    is Right -> Right(f(value))
}

/**
 * Applies one of the provided functions to the value contained in an instance of either `Left` or `Right`.
 *
 * @param onLeft the function to be applied if the instance is `Left`
 * @param onRight the function to be applied if the instance is `Right`
 * @return the result of applying the appropriate function to the value
 * @since 5.2.0
 */
inline fun <L, R, T> Either<L, R>.fold(onLeft: Transformer<L, T>, onRight: Transformer<R, T>): T = when (this) {
    is Left -> onLeft(value)
    is Right -> onRight(value)
}

/**
 * Applies one of the provided functions to the value contained in an instance of either `Left` or `Right`.
 *
 * @param onLeft the function to be applied if the instance is `Left`
 * @param onRight the function to be applied if the instance is `Right`
 * @return the result of applying the appropriate function to the value
 * @since 5.2.0
 */
inline operator fun <L, R, T> Either<L, R>.invoke(
    onLeft: Transformer<L, T>,
    onRight: Transformer<R, T>
) = fold(onLeft, onRight)
/**
 * Invokes the given default computation if this instance is of type `Left`,
 * otherwise returns the value contained in the `Right` side.
 *
 * This operator function provides a convenient way to handle the disjoint union
 * nature of the `Either` by supplying a fallback mechanism via the `default` parameter
 * when the value is not present on the `Right` side.
 *
 * @param default a supplier function that provides the default value to be used
 * if this instance is of type `Left`.
 * @since 6.1.0
 */
operator fun <L, R> Either<L, R>.invoke(default: Transformer<L, R>) = when (this) {
    is Left -> default(value)
    is Right -> value
}
/**
 * Invokes the `Either` instance and, based on its type, either throws an exception created
 * by the provided transformer or returns the contained value.
 *
 * If this instance is of type `Left`, it applies the `lazyException` function to transform
 * the left value into a `Throwable` and throws it. If this instance is of type `Right`,
 * it directly returns the contained value.
 *
 * @param lazyException A transformer function that converts a value of type `L`
 * to a `Throwable` in case the `Either` instance is of type `Left`.
 * @since 6.1.0
 */
@JvmName("invokeWithException")
operator fun <L, R> Either<L, R>.invoke(lazyException: Transformer<L, Throwable>) = when (this) {
    is Left -> throw lazyException(value)
    is Right -> value
}

/**
 * Executes a block of code in the context of a `Raise` scope, capturing any raised error
 * as a `Left` in the resulting `Either`, or returning the result of the block as a `Right`.
 *
 * This function allows for a functional style of handling errors by short-circuiting
 * computations using the `Raise` interface.
 *
 * @param E The type of the error that can be raised within the scope.
 * @param A The type of the result produced if no error is raised.
 * @param block A lambda function with a receiver of type `Raise<E>`, which can raise errors
 *              or return a value.
 * @return An `Either` value where:
 *         - `Left<E>` represents an error raised within the block.
 *         - `Right<A>` represents the successful result returned by the block.
 * @since 6.1.0
 */
@OptIn(ExperimentalContracts::class)
@Suppress("UNCHECKED_CAST")
inline fun <E, A> either(block: Raise<E>.() -> A): Either<E, A> {
    contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }
    val scope = DefaultRaise<E>()
    return try {
        Right(scope.block())
    } catch (s: RaiseSignal) {
        if (s.scope === scope) Left(s.error as E) else throw s
    }
}