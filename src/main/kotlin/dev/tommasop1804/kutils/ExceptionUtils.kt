@file:JvmName("ExceptionUtilsKt")
@file:Suppress("unused")
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since

val Throwable.rootCause: Throwable
    get() {
        var current = this
        while (current.cause.isNotNull()) current = current.cause!!
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
        var current: Throwable? = this
        while (current.isNotNull()) {
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
inline infix fun <reified T : Throwable> T.causedBy(cause: Throwable? = null) = if (cause.isNull()) withoutCause() else initCause(cause)!! as T
/**
 * Sets the cause of this throwable using a supplied Throwable. This method is designed
 * for use with the `infix` notation to enhance readability when specifying a cause.
 *
 * @param cause A supplier function that provides a Throwable to be set as the cause
 *              for this throwable.
 * @since 1.0.0
 */
inline infix fun <reified T : Throwable> T.causedBy(cause: ThrowableSupplier) = initCause(cause())!! as T
/**
 * Sets the given throwable as the cause for the exception produced
 * by the `ThrowableSupplier`.
 *
 * @param cause The throwable to be set as the cause.
 * @return A `ThrowableSupplier` that produces an exception with the specified cause.
 * @since 1.0.0
 */
infix fun ThrowableSupplier.causedBy(cause: Throwable?): ThrowableSupplier = { if (cause.isNull()) invoke().withoutCause() else invoke().initCause(cause)!! }
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
inline infix fun <T2 : Throwable, reified T1 : Throwable> T2.causeOf(main: Supplier<T1>) = main().initCause(this)!! as T1
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