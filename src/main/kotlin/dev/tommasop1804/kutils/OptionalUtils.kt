/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils

import java.util.*

/**
 * Invokes the `Optional` instance, returning the contained value if present,
 * or `null` if the instance is empty.
 *
 * This operator provides a shorthand for accessing the wrapped value
 * within an `Optional` without explicitly calling the `orElse` method.
 *
 * @return the value contained within this `Optional` if present, or `null` if the `Optional` is empty.
 * @since 1.0.0
 */
@IgnorableReturnValue
operator fun <T> Optional<T>.invoke(): T? = orElse(null)
/**
 * Provides an operator function for the `Optional` type, allowing an alternative value
 * to be returned if the optional instance is empty.
 *
 * @param other the value to return if the optional instance is empty
 * @return the value contained within the optional if present, or `other` if empty
 * @since 1.0.0
 */
@IgnorableReturnValue
operator fun <T> Optional<T>.invoke(other: T): T = orElse(other)!!
/**
 * Invokes the `Optional` instance, returning the contained value if present,
 * or `null` if the instance is empty.
 *
 * This operator provides a shorthand for accessing the wrapped value
 * within an `Optional` without explicitly calling the `orElse` method.
 *
 * @return the value contained within this `Optional` if present, or `null` if the `Optional` is empty.
 * @since 1.0.0
 */
@JvmName("optionalInvokeThrowableSupplier")
@IgnorableReturnValue
operator fun <T> Optional<T>.invoke(lazyException: ThrowableSupplier): T = orElseThrow(lazyException)!!