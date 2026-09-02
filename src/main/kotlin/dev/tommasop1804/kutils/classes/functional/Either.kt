/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.functional

import dev.tommasop1804.kutils.*

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
@Suppress("unused")
sealed class Either<out L, out R> {
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

    operator fun invoke() = when (this) {
        is Left -> this.value
        is Right -> this.value
    }

    /**
     * Applies one of the provided functions to the value contained in an instance of either `Left` or `Right`.
     *
     * @param onLeft the function to be applied if the instance is `Left`
     * @param onRight the function to be applied if the instance is `Right`
     * @return the result of applying the appropriate function to the value
     * @since 5.2.0
     */
    inline operator fun <T> invoke(
        onLeft: Transformer<L, T>,
        onRight: Transformer<R, T>
    ) = fold(onLeft, onRight)

    /**
     * Applies one of the provided functions to the value contained in an instance of either `Left` or `Right`.
     *
     * @param onLeft the function to be applied if the instance is `Left`
     * @param onRight the function to be applied if the instance is `Right`
     * @return the result of applying the appropriate function to the value
     * @since 5.2.0
     */
    inline fun <T> fold(onLeft: Transformer<L, T>, onRight: Transformer<R, T>): T = when (this) {
        is Left -> onLeft(value)
        is Right -> onRight(value)
    }

    /**
     * Transforms the value contained in a `Right` instance using the provided mapping function.
     * If the instance is `Left`, it remains unchanged.
     *
     * @param f A function that takes the value of type `R` from a `Right` instance and maps it to a value of type `T`.
     * @return An `Either` instance: if this is `Right`, it returns a new `Right` with the mapped value; if this is `Left`, it returns the same `Left`.
     * @since 5.2.0
     */
    inline fun <T> map(f: Transformer<R, T>): Either<L, T> = when (this) {
        is Left -> this
        is Right -> Right(f(value))
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
    inline fun <T> mapLeft(f: Transformer<L, T>): Either<T, R> = when (this) {
        is Left -> Left(f(value))
        is Right -> this
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
    inline fun <T> flatMap(f: Transformer<R, Either<@UnsafeVariance L, T>>): Either<L, T> = when (this) {
        is Left -> this
        is Right -> f(value)
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
    data class Left<out L>(val value: L) : Either<L, Nothing>()
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
    data class Right<out R>(val value: R) : Either<Nothing, R>()
}