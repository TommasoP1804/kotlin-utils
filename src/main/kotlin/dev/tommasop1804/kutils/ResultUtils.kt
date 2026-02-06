@file:JvmName("ResultUtilsKt")
@file:Suppress("unused")
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since

/**
 * Invokes the given lambda functions based on the success or failure state of the `Result`.
 *
 * Executes the `onSuccess` function if the `Result` is successful, passing the encapsulated value.
 * Executes the `onFailure` function if the `Result` is a failure, passing the encapsulated exception.
 *
 * @param onSuccess a lambda function to execute if the `Result` is successful
 * @param onFailure a lambda function to execute if the `Result` is a failure
 * @return the result of the invoked lambda, either from the `onSuccess` or `onFailure` function
 * @since 1.0.0
 */
inline operator fun <T, R> Result<T>.invoke(
    onSuccess: Transformer<T, R>,
    onFailure: Transformer<Throwable, R>
): R = fold(onSuccess, onFailure)
/**
 * Invokes the `Result` to extract and return the encapsulated value if it is successful,
 * or throws the encapsulated exception if the result is a failure.
 *
 * This operator function provides a convenient way to directly retrieve the value
 * from a `Result` instance without explicitly calling methods like `getOrThrow`.
 *
 * @receiver The `Result` object from which the encapsulated value or exception is accessed.
 * @return The encapsulated value of type `T` if the result is successful.
 * @throws Throwable If the result represents a failure, the encapsulated exception will be thrown.
 * @since 1.0.0
 */
operator fun <T> Result<T>.invoke() = getOrThrow()

/**
 * Invokes the `getOrThrow` method on the `Result` object. If the operation encapsulated in the
 * `Result` fails, it uses the provided `ExceptionSupplier` to determine the thrown exception.
 *
 * @param T the type of the encapsulated value in the `Result`.
 * @param exception the functional supplier that provides the exception to be thrown
 * if the `Result` is a failure.
 * @since 1.0.0
 */
operator fun <T> Result<T>.invoke(exception: ThrowableSupplier) = getOrThrow(exception)

/**
 * Negates the result of the current operation, providing a boolean value that
 * indicates whether the operation was unsuccessful.
 *
 * This operator function evaluates `isFailure` on the `Result` instance.
 *
 * @receiver The `Result` instance whose failure status is being evaluated.
 * @return `true` if the operation represented by the `Result` was unsuccessful.
 *         Otherwise, returns `false`.
 * @since 1.0.0
 */
operator fun Result<*>.not() = isFailure

/**
 * Attempts to retrieve the value encapsulated within the [Result] instance.
 * If there is no value (i.e., the [Result] contains an exception), this method throws an exception.
 * The exception can be customized by providing a [lazyException] supplier.
 *
 * The optional [includeCause] parameter determines whether the original cause of the failure in the [Result]
 * should be included as the cause for the new exception. If set to `true`, the cause is included (if present),
 * otherwise, it is omitted.
 *
 * @param lazyException A lambda that produces a [Throwable] used as the new exception to be thrown
 * if the [Result] instance contains a failure.
 * @param includeCause A boolean that indicates whether the cause of the original exception should be included
 * in the new exception. Default is `true`.
 * @throws Throwable If the [Result] contains a failure, the provided [lazyException] or its modified version is thrown.
 * @since 1.0.0
 */
inline fun <T> Result<T>.getOrThrow(lazyException: ThrowableSupplier, includeCause: Boolean = true) = getOrElse {
    val exception = lazyException()
    throw if (exception.cause.isNotNull() || !includeCause) exception else (exception causedBy exceptionOrNull())
}