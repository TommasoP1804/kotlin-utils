/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:OptIn(ExperimentalContracts::class)
@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.functional

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.collections.*
import dev.tommasop1804.kutils.classes.functional.Either.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException

/**
 * Scope in which a typed error of type [Error] can be raised, short-circuiting the enclosing block.
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
interface Raise<in Error> {
    /**
     * Aborts the enclosing block, making it produce [error].
     * @since 6.1.0
     */
    fun raise(error: Error): Nothing

    fun <E, A> partitionEithers(
        eithers: List<Either<E, A>>
    ): Pair<List<E>, List<A>> =
        eithers.fold(emptyList<E>() to emptyList()) { [errs, oks], either ->
            when (either) {
                is Left -> (errs + either.value) to oks
                is Right -> errs to (oks + either.value)
            }
        }

    /**
     * Runs `block` on this receiver's [Either], raising its `Left`.
     * @since 6.1.0
     */
    operator fun <A> Either<Error, A>.component1(): A = bind()
}

/**
 * Signal used to unwind the stack on `raise`. It carries the owning [scope] so that
 * nested `either` blocks never intercept each other's errors. No stack trace is captured.
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
@PublishedApi
internal class RaiseSignal(
    @JvmField val scope: Any,
    @JvmField val error: Any?,
) : CancellationException("Raised") {
    override fun fillInStackTrace(): Throwable = this
}

/**
 * Default implementation of the [Raise] interface, allowing errors of type [E] to be raised
 * and short-circuit the enclosing block execution.
 *
 * This implementation raises the error by throwing a [RaiseSignal] containing the error
 * and the current scope.
 *
 * @param E The type of the error that can be raised within this scope.
 * @author Tommaso Pastorelli
 * @since 6.1.0
 */
@PublishedApi
internal class DefaultRaise<E> : Raise<E> {
    /**
     * Raises an error within the current context, encapsulated by `RaiseSignal`,
     * allowing the stack to unwind up to an appropriate handler.
     *
     * @param error The error of type `E` to be raised. This represents the failure
     *              or exceptional value to propagate.
     * @return This method does not return any value as it throws an exception of type `RaiseSignal`.
     * @since 6.1.0
     */
    override fun raise(error: E): Nothing = throw RaiseSignal(this, error)
}

/**
 * Ensures that a given condition is true; otherwise, raises an error using the provided error supplier.
 *
 * @param condition A boolean expression that is expected to be true. If it evaluates to false, an error is raised.
 * @param error A lambda function that produces an error of type [E] when the condition is false.
 * @since 6.1.0
 */
inline fun <E> Raise<E>.ensure(condition: Boolean, error: () -> E) {
    contract { returns() implies condition }
    if (!condition) raise(error())
}
/**
 * Ensures that a given condition is true; otherwise, raises an error using the provided error supplier.
 *
 * @param condition A boolean expression that is expected to be true. If it evaluates to false, an error is raised.
 * @param error A lambda function that produces an error of type [E] when the condition is false.
 * @since 6.1.0
 */
@JvmName("ensureWithContext")
context(r: Raise<E>)
inline fun <E> ensure(condition: Boolean, error: () -> E) {
    contract { returns() implies condition }
    if (!condition) r.raise(error())
}

/**
 * Ensures that the provided value is not null. If the value is null, raises the specified error.
 *
 * @param value The value to check for nullability.
 * @param error A lambda that provides the error to raise if the value is null.
 * @return The non-null value if the check passes.
 * @since 6.1.0
 */
inline fun <E, B : Any> Raise<E>.ensureNotNull(value: B?, error: () -> E): B {
    contract { returns() implies (value != null) }
    return value ?: raise(error())
}
/**
 * Ensures that the provided value is not null. If the value is null, raises the specified error.
 *
 * @param value The value to check for nullability.
 * @param error A lambda that provides the error to raise if the value is null.
 * @return The non-null value if the check passes.
 * @since 6.1.0
 */
@JvmName("ensureNotNullWithContext")
context(r: Raise<E>)
inline fun <E, B : Any> ensureNotNull(value: B?, error: () -> E): B {
    contract { returns() implies (value != null) }
    return value ?: r.raise(error())
}

/**
 * Executes the provided block of code, and catches exceptions of type [T].
 * If an exception of type [T] is caught, the [onError] transformer is invoked to convert
 * the exception into an error of type [E], which is then raised using the `raise` function.
 *
 * @param T The type of exception to catch.
 * @param E The type of the error to be raised.
 * @param A The return type of the block of code.
 * @param block The code block to be executed.
 * @param onError A transformer function that converts an exception of type [T] to an error of type [E].
 * @return The result of executing [block], if no exception of type [T] occurs.
 * @throws RaiseSignal If a signal for unwinding the stack is raised.
 * @throws CancellationException If a cancellation exception occurs during block execution.
 * @throws Throwable If an exception other than [T] occurs that is not handled by the function.
 * @since 6.1.0
 */
inline fun <reified T : Throwable, E, A> Raise<E>.catching(
    block: () -> A,
    onError: Transformer<T, E>,
): A = try {
    block()
} catch (e: RaiseSignal) {
    throw e
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    if (e is T) raise(onError(e)) else throw e
}
/**
* Executes the provided block of code, and catches exceptions of type [T].
* If an exception of type [T] is caught, the [onError] transformer is invoked to convert
* the exception into an error of type [E], which is then raised using the `raise` function.
*
* @param T The type of exception to catch.
* @param E The type of the error to be raised.
* @param A The return type of the block of code.
* @param block The code block to be executed.
* @param onError A transformer function that converts an exception of type [T] to an error of type [E].
* @return The result of executing [block], if no exception of type [T] occurs.
* @throws RaiseSignal If a signal for unwinding the stack is raised.
* @throws CancellationException If a cancellation exception occurs during block execution.
* @throws Throwable If an exception other than [T] occurs that is not handled by the function.
* @since 6.1.0
*/
@JvmName("catchingWithContext")
context(r: Raise<E>)
inline fun <reified T : Throwable, E, A> catching(
    block: () -> A,
    onError: Transformer<T, E>,
): A = try {
    block()
} catch (e: RaiseSignal) {
    throw e
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    if (e is T) r.raise(onError(e)) else throw e
}

/**
 * Executes a computation within a `Raise` scope and recovers by applying a
 * provided transformation in case of an error.
 *
 * @param E the type of the error that can be raised during the computation
 * @param A the type of the successful result of the computation
 * @param block the computation to execute within the `Raise` scope
 * @param onError a transformation function to handle errors and produce a result
 * @return the result of the computation if successful, or the result of the
 *         error transformation if an error occurs
 * @since 6.1.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <E, A> recover(
    block: Raise<E>.() -> A,
    onError: Transformer<E, A>,
): A = either(block).fold(onError, ::identity)

/**
 * Executes a block of code while transforming errors of type [E] into errors of type [EE].
 *
 * @param transform A function that transforms an error of type [E] into an error of type [EE].
 * @param block A block of code to execute within a `Raise<E>` scope.
 * @return The result of executing the block.
 * @since 6.1.0
 */
inline fun <E, EE, A> Raise<EE>.withError(
    crossinline transform: Transformer<E, EE>,
    block: Raise<E>.() -> A,
): A = object : Raise<E> {
    /**
     * Aborts the enclosing block, making it produce a transformed version of the specified [error].
     *
     * This method is used to signal that an error has occurred within a `Raise` context,
     * transforming the provided error of type `E` to the expected type `EE` using the `transform` function,
     * and then propagating it as a terminal operation.
     *
     * @param error The error of type `E` that needs to be raised.
     * @return This method does not return normally; it throws an exception to signal an error.
     * @since 6.1.0
     */
    override fun raise(error: E): Nothing = this@withError.raise(transform(error))
}.block()

/**
 * Provides a mechanism to short-circuit the execution flow by raising an error if the current value is null.
 *
 * @param E the type of the error that can be raised.
 * @param T the type of the value being checked.
 * @param left a supplier of the error to be raised if the current value is null.
 * @since 6.1.0
 */
context(r: Raise<E>)
infix fun <E, T> T.orRaise(left: Supplier<E>) = this ?: r.raise(left())

/**
 * Returns the `Right` value, or raises the `Left` value.
 * @since 6.1.0
 */
context(r: Raise<E>)
fun <E, A> Either<E, A>.bind(): A = fold({ r.raise(it) }, ::identity)
/**
 * Aborts the enclosing block, making it produce [error].
 * @since 6.1.0
 */
context(r: Raise<E>)
fun <E> raise(error: E): Nothing = r.raise(error)

/**
 * Combines two computations that may fail, either successfully producing a combined result
 * or accumulating errors from both computations.
 *
 * @param a A function producing the first computation which may return a successful result or an error.
 * @param b A function producing the second computation which may return a successful result or an error.
 * @param combine A function that combines the successful results of both computations into a single result.
 * @return An `Either` containing either a `NonEmptyList` of accumulated errors or a combined success result.
 * @since 6.1.0
 */
fun <E, A, B, C> zipOrAccumulate(
    a: Supplier<Either<E, A>>,
    b: Supplier<Either<E, B>>,
    combine: BiTransformer<A, B, C>
): Either<NonEmptyList<E>, C> {
    val resA = a()
    val resB = b()

    val errors = listOfNotNull(
        (resA as? Left)?.value,
        (resB as? Left)?.value
    )

    return if (errors.isNotEmpty()) {
        @Suppress("UNCHECKED_CAST")
        Left(NonEmptyList(errors.first(), errors.drop(1)) as NonEmptyList<E>)
    } else Right(combine(
        (resA as Right).value,
        (resB as Right).value
    ))
}

/**
 * Transforms a list of `Either` values into a single `Either` containing a list of all `Right` values
 * if all elements are `Right`, or a `Left` with a `NonEmptyList` of all `Left` values if one or more elements are `Left`.
 *
 * This method accumulates all errors (`Left` values) into a `NonEmptyList` when failures occur, rather than failing fast
 * on the first error. If all elements in the list are `Right`, their values are collected into a result list.
 *
 * @return An `Either` where the `Right` value is a list of all `Right` values from the input list, or the `Left`
 * value is a `NonEmptyList` of all `Left` values.
 * @since 6.1.0
 */
context(r: Raise<*>)
fun <E, A> List<Either<E, A>>.sequenceAccumulating(): Either<NonEmptyList<E>, List<A>> {
    val [lefts, rights] = r.partitionEithers(this)

    return if (lefts.isNotEmpty()) {
        @Suppress("UNCHECKED_CAST")
        Left(NonEmptyList(lefts.first(), lefts.drop(1)) as NonEmptyList<E>)
    } else Right(rights)
}