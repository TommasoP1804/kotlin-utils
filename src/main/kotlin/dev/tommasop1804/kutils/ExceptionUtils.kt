/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("ExceptionUtilsKt")
@file:Suppress("unused")
@file:Since("1.0.0")
@file:OptIn(ExperimentalContracts::class)
@file:MustUseReturnValues

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Represents a code path that should never be reachable under normal circumstances.
 *
 * This value generates a runtime error when accessed, explicitly signaling that execution
 * has reached a location in the program that was assumed to be logically impossible.
 * Its primary use is for ensuring exhaustiveness in certain control structures, such as `when` expressions.
 *
 * @since 6.1.0
 */
val UNREACHABLE: Nothing = error("Unreachable")

/**
 * Extension property that retrieves the root cause of a [Throwable].
 *
 * This property traverses the chain of throwable causes and returns
 * the deepest nested cause. If there are no nested causes, it returns
 * the throwable itself.
 * @since 6.1.0
 */
val Throwable.rootCause: Throwable
    get() {
        var current = this
        while (current.cause != null) current = current.cause!!
        return current
    }

/**
 * Provides a list of all the causes for the given throwable, traversing
 * through the chain of `cause` properties until no further cause is available.
 *
 * Iterates over the throwable's cause hierarchy and collects each cause
 * into a list. The resulting list includes all nested causes of the throwable
 * starting from the direct cause.
 *
 * @receiver A throwable from which the list of causes is extracted.
 * @return A list of all the causes within the throwable's causal hierarchy.
 * @since 1.0.0
 */
val Throwable.causes: List<Throwable>
    get() {
        val list = emptyMList<Throwable>()
        var current: Throwable? = cause
        while (current != null) {
            list += current
            current = current.cause
        }
        return list
    }

/**
 * Creates a new instance of the throwable of the same type but without a cause.
 *
 * @return A new instance of the throwable of type `T` without a cause, carrying the same message as the original instance or the class's simple name if the message is null.
 * @since 1.0.0
 */
inline fun <reified T : Throwable> T.withoutCause(): T {
    return T::class.java
        .getConstructor(String::class.java)
        .newInstance(message ?: T::class.java.simpleName) as T
}

/**
 * Sets the specified throwable as the cause of the current throwable.
 *
 * This function uses `initCause` to link the provided `cause` to the current throwable.
 *
 * @param cause The throwable to be set as the cause of the current throwable.
 * @since 1.0.0
 */
inline infix fun <reified T : Throwable> T.causedBy(cause: Throwable? = null) = if (cause == null) withoutCause() else initCause(cause)!! as T
/**
 * Sets the cause of this throwable using a supplied Throwable. This method is designed
 * for use with the `infix` notation to enhance readability when specifying a cause.
 *
 * @param cause A supplier function that provides a Throwable to be set as the cause
 *              for this throwable.
 * @since 1.0.0
 */
inline infix fun <reified T : Throwable> T.causedBy(cause: ThrowableSupplier): T {
    contract {
        callsInPlace(cause, InvocationKind.EXACTLY_ONCE)
    }
    return initCause(cause())!! as T
}
/**
 * Sets the given throwable as the cause for the exception produced
 * by the `ThrowableSupplier`.
 *
 * @param cause The throwable to be set as the cause.
 * @return A `ThrowableSupplier` that produces an exception with the specified cause.
 * @since 1.0.0
 */
infix fun ThrowableSupplier.causedBy(cause: Throwable?): ThrowableSupplier = { if (cause == null) invoke().withoutCause() else invoke().initCause(cause)!! }
/**
 * Combines the current `ThrowableSupplier` with another `ThrowableSupplier` as its cause.
 * The resulting `Throwable` will have its cause set to the `Throwable` provided by the input supplier.
 *
 * @param cause the `ThrowableSupplier` that provides the cause of the original throwable.
 * @return a new `ThrowableSupplier` with its cause set to the `Throwable` provided by the input supplier.
 * @since 1.0.0
 */
infix fun ThrowableSupplier.causedBy(cause: ThrowableSupplier): ThrowableSupplier = { invoke().initCause(cause())!! }

/**
 * Sets the current throwable as the cause of the main throwable.
 *
 * This method links the current throwable as the cause for the provided
 * main throwable. It infix notation allows for improved readability when
 * chaining error-handling operations.
 *
 * @param main the throwable to which the current throwable will be set as the cause
 * @since 1.0.0
 */
inline infix fun <T2 : Throwable, reified T1 : Throwable> T2.causeOf(main: T1) = main.initCause(this)!! as T1
/**
 * Initializes the cause of a Throwable using another Throwable supplied by the provided
 * ThrowableSupplier. This allows for chaining of exceptions and provides additional context
 * for debugging or error handling.
 *
 * @param main A supplier function of type ThrowableSupplier that provides the main Throwable
 *             to which the current Throwable (this) will be set as the cause.
 * @since 1.0.0
 */
inline infix fun <T2 : Throwable, reified T1 : Throwable> T2.causeOf(main: Supplier<T1>): T1 {
    contract {
        callsInPlace(main, InvocationKind.EXACTLY_ONCE)
    }
    return main().initCause(this)!! as T1
}
/**
 * Sets the cause of the provided main throwable as the throwable supplied by the invoking ThrowableSupplier.
 *
 * @param main The primary throwable for which the cause should be set.
 * @return A ThrowableSupplier that supplies the main throwable after its cause has been set.
 * @since 1.0.0
 */
infix fun ThrowableSupplier.causeOf(main: Throwable): ThrowableSupplier = { main.initCause(invoke())!! }
/**
 * Associates a cause throwable with a main throwable by invoking both suppliers and setting the cause of the main throwable.
 *
 * @param main A supplier function that provides the main throwable.
 * @return A supplier function that results in the main throwable with the cause set to the throwable provided by this supplier.
 * @since 1.0.0
 */
infix fun ThrowableSupplier.causeOf(main: ThrowableSupplier): ThrowableSupplier = { main().initCause(invoke())!! }

/**
 * Associates the current throwable chain with a specified root cause.
 *
 * This function ensures the given throwable is set as the root cause of the throwable chain,
 * and takes necessary precautions to avoid circular references or redundant associations.
 *
 * @param cause The throwable to be set as the root cause.
 * @return The original throwable (receiver), after associating with the given root cause.
 * @since 6.1.0
 */
infix fun Throwable.withRootCause(cause: Throwable): Throwable = also { outer ->
    if (outer === cause) return@also
    val root = generateSequence(outer) { it.cause?.takeIf { next -> next !== it } }
        .last()
    if (root === cause) return@also
    runCatching { root.initCause(cause) }
        .onFailure { root.addSuppressed(cause) }
}
/**
 * Associates the given root cause with the current throwable. If the current throwable already has
 * the given cause in its causal chain, no action is taken. Otherwise, the root cause will be added
 * to the deepest node in the causal chain, either as its cause or by suppressing it if an exception
 * occurs during initialization.
 *
 * @param cause A supplier function that provides the root cause to be added.
 * @return The current throwable instance, potentially augmented with the root cause in its causal chain.
 * @since 6.1.0
 */
infix fun Throwable.withRootCause(cause: ThrowableSupplier): Throwable = also { outer ->
    if (outer === cause) return@also
    val root = generateSequence(outer) { it.cause?.takeIf { next -> next !== it } }
        .last()
    if (root === cause) return@also
    runCatching { root.initCause(cause()) }
        .onFailure { root.addSuppressed(cause()) }
}
/**
 * Associates the given cause with the current throwable chain, ensuring the specified cause
 * is linked to the deepest root cause of the throwable chain. If the given cause already
 * exists in the throwable chain, this method has no effect.
 *
 * @param cause The throwable to be associated as the root cause.
 * @return The receiver throwable supplier with the updated root cause.
 * @since 6.1.0
 */
infix fun ThrowableSupplier.withRootCause(cause: Throwable): ThrowableSupplier = also { outer ->
    if (outer === cause) return@also
    val root = generateSequence(outer) { it.cause?.takeIf { next -> next !== it } }
        .last()
    if (root === cause) return@also
    runCatching { root.initCause(cause) }
        .onFailure { root.addSuppressed(cause) }
}
/**
 * Associates a root cause with the current `ThrowableSupplier` instance. If a root cause is not already
 * present, it initializes the root cause. Otherwise, it adds the supplied cause as a suppressed exception
 * to the deepest cause in the chain.
 *
 * @param cause a supplier for the throwable instance to be associated or added as a suppressed exception
 * @return the original `ThrowableSupplier` instance with the updated root cause or suppressed exception
 * @since 6.1.0
 */
infix fun ThrowableSupplier.withRootCause(cause: ThrowableSupplier): ThrowableSupplier = also { outer ->
    if (outer === cause) return@also
    val root = generateSequence(outer) { it.cause?.takeIf { next -> next !== it } }
        .last()
    if (root === cause) return@also
    runCatching { root.initCause(cause()) }
        .onFailure { root.addSuppressed(cause()) }
}