/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
@file:JvmName("IterableValidatorsKt")
@file:Since("5.0.0")
@file:Suppress("unused")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.contract
import kotlin.invoke
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.toString

/**
 * Validates that the collection is not empty. If the collection is empty, a `ValidationFailedException` is thrown.
 * The exception may optionally have a cause provided by the `causeOf` or `cause` suppliers.
 *
 * @param causeOf a supplier that provides a throwable as the primary cause, or `null` if unused.
 * @param cause a supplier that provides a throwable as a secondary cause, or `null` if unused.
 * @return the original collection if it is not empty.
 * @throws ValidationFailedException if the collection is empty, with an optional cause or nested cause.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException("The collection is empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The collection is empty.", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the collection is not empty. If the collection is empty, throws a
 * `ValidationFailedException` with the provided lazy message, and optionally sets
 * the provided cause or initializes the cause from another throwable.
 *
 * @param causeOf an optional supplier for the throwable to set as the cause of the
 *                validation failure. If null, a `ValidationFailedException` is thrown directly.
 * @param cause an optional supplier for the underlying cause of the validation failure,
 *              which can be set in the exception using `initCause`.
 * @param lazyMessage a supplier for the error message to use in the exception when validation fails.
 * @return the original collection if it is not empty.
 * @throws ValidationFailedException if the collection is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is not empty. If the collection is empty, a `ValidationFailedException` is thrown.
 *
 * @param property The property associated with the validation failure. Can be null if not applicable.
 * @param variableName An optional name for the variable involved in the validation. If provided, it is included in the exception message.
 * @param message An optional descriptive message for the validation failure. Defaults to "is empty" if not provided.
 * @param causeOf A supplier for the throwable that caused the validation failure, if applicable.
 * @param cause An optional supplier for an additional exception to be attached as the cause of `ValidationFailedException`.
 * @return The collection itself if it passes the validation.
 * @throws ValidationFailedException if the collection is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is not empty. If the collection is empty, a `ValidationFailedException` is thrown.
 *
 * @param property the main KProperty associated with the validation, or null if not specified.
 * @param variable an optional secondary KProperty that provides additional context, or null if not specified.
 * @param message an optional message to include in the exception if validation fails; defaults to "is empty".
 * @param causeOf an optional supplier for the cause of the exception if validation fails.
 * @param cause an optional supplier for a chained cause of the exception if validation fails.
 * @return the collection itself if it is not empty.
 * @throws ValidationFailedException if the collection is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a collection is not empty. If the collection is empty, a `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter being validated. Used in the exception message. Can be null.
 * @param message Custom error message in case validation fails. Defaults to "is empty".
 * @param causeOf Supplier for a `Throwable` that will be thrown if the validation fails, overriding the default exception behavior. Can be null.
 * @param cause Supplier for the root cause `Throwable` to be attached to the exception if thrown. Can be null.
 * @return The collection itself if it is not empty.
 * @throws ValidationFailedException If the collection is empty and no `causeOf` is provided.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is not empty. If the collection is empty, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] associated with this validation, or null if not applicable
 * @param parameter the [KParameter] being validated, or null if not applicable
 * @param message an optional custom error message to include in the exception if the validation fails
 * @param causeOf an optional supplier for the root cause of the validation failure, or null if no such cause exists
 * @param cause an optional supplier for the secondary cause of the validation failure, or null if no such cause exists
 * @return the validated collection if it is not empty
 * @throws ValidationFailedException if the collection is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is not empty. If the collection is empty, a `ValidationFailedException`
 * is thrown with details about the failure.
 *
 * @param callableName the name of the callable (e.g., function or method) related to this validation
 * @param parameterName the name of the parameter being validated, or null if not specified
 * @param message a custom message describing the validation failure, or null to use the default message
 * @param causeOf a supplier for the cause of the exception, which will be used to initialize the root cause of the failure, or null
 * @param cause a supplier for an additional cause of the exception, or null
 * @return the original collection if it is not empty
 * @throws ValidationFailedException if the collection is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is not empty, throwing a `ValidationFailedException` if it is empty.
 *
 * @param callableName The name of the callable (e.g., function or property) where validation is performed. Can be null.
 * @param parameter The `KParameter` instance representing the parameter involved in the validation. Can be null.
 * @param message An optional error message providing more context if validation fails. Defaults to "is empty" if not specified.
 * @param causeOf A supplier for a throwable that can wrap the `ValidationFailedException`. Can be null.
 * @param cause A supplier for the cause of the `ValidationFailedException`. Can be null.
 * @return The same collection instance if it passes the validation.
 *
 * @throws ValidationFailedException if the collection is empty. The exception provides details such as the callable name,
 * parameter, message, or the underlying cause based on the inputs provided.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateNotEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is neither `null` nor empty. If the validation fails, an exception is thrown.
 *
 * @param causeOf the supplier function for providing a custom exception to be thrown
 *                when the collection is `null` or empty. If this is `null`, a default exception is used.
 * @param cause additional supplier function for providing the cause of the exception, if necessary.
 *              This is optional and can also be `null`.
 * @return the original collection if it is valid (not `null` or empty).
 * @throws ValidationFailedException if the collection is `null` or empty, along with the specified or default cause.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException("The collection is null or empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The collection is null or empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is not null or empty. If the validation fails,
 * a `ValidationFailedException` is thrown with the specified lazy message and optional cause.
 *
 * @param causeOf a supplier of the throwable to be used as the cause of the exception if provided.
 * @param cause a supplier of the throwable to be used as an additional cause of the exception if provided.
 * @param lazyMessage a supplier for the message to be included in the exception if the validation fails.
 * @return the original collection if it is not null or empty.
 * @throws ValidationFailedException if the collection is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given collection is not null or empty. If the collection is null or empty,
 * throws a [ValidationFailedException] with the specified parameters.
 *
 * @param property Optional property metadata associated with the collection being validated.
 * @param variableName Optional name of the variable involved in the validation. Included in the
 *                     exception message if provided.
 * @param message Optional custom message for the validation failure. Defaults to "is null or empty"
 *                if not provided.
 * @param causeOf Optional supplier for the primary cause of the validation failure, which is used
 *                to initialize the thrown exception.
 * @param cause Optional supplier for an additional cause to be included in the thrown exception.
 * @return The original collection if it is neither null nor empty.
 * @throws ValidationFailedException If the collection is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is not null or empty.
 * If the collection is null or empty, it throws a [ValidationFailedException].
 *
 * @param property The primary property associated with the validation, or null if not specified.
 * @param variable An optional secondary property providing additional context, or null if not specified.
 * @param message An optional message providing details about the validation failure, or null to use a default message.
 * @param causeOf A supplier for the root exception cause, or null if no root cause is specified.
 * @param cause A supplier for the exception cause, or null if no cause is specified.
 * @return The collection itself if the validation is successful.
 * @throws ValidationFailedException if the collection is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is neither null nor empty. If the validation fails, throws a `ValidationFailedException`.
 *
 * @param callable The Kotlin function (`KFunction`) that is associated with the validation.
 *                 This helps in identifying the context in which the validation is performed. Can be null.
 * @param parameterName The name of the parameter in the function `callable` that is being validated. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Default is "is null or empty".
 * @param causeOf A supplier of the cause for the exception. If provided, it takes precedence over the `cause` parameter. Can be null.
 * @param cause A supplier of the underlying cause for the exception. Used if `causeOf` is not supplied. Can be null.
 * @since 4.2.0
 **/
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is neither null nor empty. If it is null or empty, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] associated with the validation, or null if not applicable.
 * @param parameter the [KParameter] involved in the validation, or null if not applicable.
 * @param message an optional message providing additional context about the validation failure, defaulting to null.
 * @param causeOf an optional supplier for a custom exception cause, defaulting to null.
 * @param cause an optional supplier for the underlying cause of the validation failure, defaulting to null.
 * @return the original collection if the validation passes.
 * @throws ValidationFailedException if the collection is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is not null or empty. If the validation fails, it throws a [ValidationFailedException].
 *
 * @param callableName The name of the callable (e.g., function or method) associated with the validation.
 * @param parameterName The name of the parameter being validated, or `null` if not applicable.
 * @param message An optional custom message providing additional details about the failure. Defaults to "is null or empty".
 * @param causeOf A supplier for the primary exception to be thrown if validation fails, or `null` if not provided.
 * @param cause A supplier for the underlying cause of the validation failure, or `null` if not provided.
 * @return The collection itself if validation passes.
 * @throws ValidationFailedException If the collection is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is not `null` or empty. If the collection is `null` or empty,
 * a `ValidationFailedException` is thrown with additional details about the callable name,
 * parameter, and an optional error message or cause.
 *
 * @param callableName The name of the callable (e.g., function or property) for context in case of validation failure, or `null` if not specified.
 * @param parameter The parameter related to this validation check, represented as a `KParameter` instance, or `null` if not applicable.
 * @param message An optional error message providing context about the validation failure, or `null` for a default message.
 * @param causeOf A supplier (`ThrowableSupplier`) for the cause of the validation failure, or `null` if not specified.
 * @param cause An additional supplier (`ThrowableSupplier`) for a deeper cause of validation failure, or `null` if not specified.
 * @return The original collection if it is not `null` and not empty.
 * @throws ValidationFailedException If the collection is `null` or empty, providing detailed contextual information.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNotNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current collection is empty. If the collection is not empty, an exception is thrown.
 *
 * @param causeOf an optional supplier that provides a custom exception to be thrown if the validation fails.
 * @param cause an optional supplier that provides the underlying cause for the exception.
 * @return the current collection if it is empty.
 * @throws ValidationFailedException if the collection is not empty and no custom exception is provided.
 * @throws Throwable if a custom exception supplied by `causeOf` or its cause is provided.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException("The collection is not empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The collection is not empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is empty, throwing a [ValidationFailedException] if it is not.
 *
 * @param causeOf an optional supplier for a custom throwable that wraps the [ValidationFailedException].
 *                If null, the exception is not wrapped.
 * @param cause an optional supplier for the underlying cause of the [ValidationFailedException].
 * @param lazyMessage a supplier for the exception message to be used if validation fails.
 * @return the collection if it is empty.
 * @throws ValidationFailedException if the collection is not empty, optionally wrapped by the throwable provided by [causeOf].
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is empty. If the collection is not empty, a `ValidationFailedException` is thrown.
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Used in the exception message if provided.
 * @param message An optional custom error message to include in the exception. Defaults to "is not empty".
 * @param causeOf A supplier for the primary throwable to throw if validation fails. If null, a default exception is created.
 * @param cause A supplier for the underlying cause of the validation failure. This will be set as the `cause` of the exception.
 * @return The collection itself if it passes validation.
 * @throws ValidationFailedException if the collection is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is empty. If the collection is not empty, a [ValidationFailedException] is thrown.
 *
 * @param property the main [KProperty] associated with the validation failure, or null if not specified
 * @param variable an optional secondary [KProperty] providing additional context, or null if not specified
 * @param message an optional message providing additional details about the validation failure; defaults to "is not empty" if null
 * @param causeOf a supplier providing the root cause of the exception, or null if not specified
 * @param cause a supplier providing the additional cause of the exception, or null if not specified
 * @return the original collection if the validation succeeds
 * @throws ValidationFailedException if the collection is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is empty. If the collection is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) to which this validation is related. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom error message to be included in the exception. If not provided, a default message is used.
 * @param causeOf A supplier for a `Throwable` that represents the cause of the exception. Can be null.
 * @param cause A supplier for an additional `Throwable` to be used as the cause of the exception. Can be null.
 * @return The original collection if it is empty.
 * @throws ValidationFailedException If the collection is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current collection is empty. Throws a [ValidationFailedException] if the collection is not empty.
 *
 * @param callable an optional [KFunction] associated with the validation context
 * @param parameter an optional [KParameter] representing the parameter being validated
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf an optional supplier for the cause of the exception, if needed
 * @param cause an optional supplier for the root cause of the exception
 * @return the collection itself if validation passes
 * @throws ValidationFailedException if the collection is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is empty. Throws a `ValidationFailedException` if the collection is not empty.
 *
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom message for the validation failure, or null to use the default message.
 * @param causeOf A supplier for the root cause of the exception, or null if no specific cause is provided.
 * @param cause A supplier for the underlying cause of the exception, or null if no cause is specified.
 * @return The collection instance when validation passes.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given collection is empty.
 *
 * Throws a [ValidationFailedException] if the collection is not empty.
 *
 * @param callableName The name of the callable (e.g., function or property) being validated, or null if not specified.
 * @param parameter The [KParameter] associated with the validation, or null if not applicable.
 * @param message An optional error message providing additional context for the validation failure.
 * @param causeOf A supplier for the root cause of the exception, or null if not provided.
 * @param cause A supplier for the underlying cause of the exception, or null if not provided.
 * @return The original collection if it passes the validation check.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.validateEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a collection is either null or empty and throws an exception if it is not.
 *
 * The method checks the calling collection and ensures that it is either null or contains no elements.
 * If the collection is neither null nor empty, a `ValidationFailedException` is thrown with an optional custom cause.
 *
 * @param causeOf A supplier for a throwable that will be used as the main exception cause if the validation fails.
 * @param cause A supplier for an additional throwable that can be passed as the underlying cause
 *              when creating the main `ValidationFailedException`.
 * @return The same collection on which the method was called if it satisfies the null or empty condition.
 * @throws ValidationFailedException If the collection is not null or not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException("The collection is not null or empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The collection is not null or empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is null or empty. If it's not null or empty, a `ValidationFailedException`
 * is thrown with the specified lazy message and optional causes.
 *
 * @param causeOf A supplier for a specific cause of the validation failure, which can be used to wrap the
 *                exception. If `null`, a default exception will be used.
 * @param cause An additional supplier for the underlying cause of the validation failure, which will be
 *              attached to the exception.
 * @param lazyMessage A supplier for the error message to be used in the exception. The evaluation of this
 *                    supplier is deferred until the exception is created.
 * @return The original collection if the validation passes (i.e., the collection is null or empty).
 * @throws ValidationFailedException If the collection is not null and not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that a collection is null or empty. If it is not, throws a [ValidationFailedException].
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName The name of the variable being validated. Can be null; used in the exception message if provided.
 * @param message A custom message describing the validation failure. Defaults to a generic message if not provided.
 * @param causeOf A supplier for the throwable that will be thrown if validation fails. If null, a [ValidationFailedException] is thrown.
 * @param cause An optional supplier for the underlying cause to be associated with the exception.
 * @return The validated collection if it is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a collection is either null or empty. If the collection is neither, a `ValidationFailedException`
 * is thrown with the provided details.
 *
 * This validation is useful for ensuring that nullable collections meet specific requirements regarding their
 * nullability and emptiness.
 *
 * @param property The primary `KProperty` associated with the validation, providing metadata about the collection being validated.
 * @param variable An optional secondary `KProperty` that provides additional context for the validation, or null if not specified.
 * @param message An optional error message to include in the exception if validation fails, or null for a default message.
 * @param causeOf A supplier for an alternative throwable to use as the main cause of the exception, or null to omit.
 * @param cause A supplier for an additional throwable to attach as the underlying cause of the validation failure, or null to omit.
 * @return The original collection if it passes validation, allowing fluent-style chaining.
 * @throws ValidationFailedException If the collection is neither null nor empty, with details about the failure included.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is either null or empty. If the validation fails, a `ValidationFailedException`
 * is thrown based on the provided arguments.
 *
 * @param callable The Kotlin function (`KFunction`) to which this validation is linked. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message for the validation failure. Defaults to "is not null or empty".
 * @param causeOf An optional supplier for a custom `Throwable` to be thrown if validation fails. Can be null.
 * @param cause An optional supplier for the cause of the validation failure. Can be null.
 * @return The original collection if it is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is either null or empty.
 *
 * If the collection is not null and not empty, a `ValidationFailedException` is thrown with the provided parameters.
 *
 * @param callable The [KFunction] associated with the validation, or null if not applicable.
 * @param parameter The [KParameter] associated with the validation, or null if not applicable.
 * @param message An optional custom message providing additional context about the validation failure. Defaults to null.
 * @param causeOf A supplier for a throwable that is the cause of the validation failure. Defaults to null.
 * @param cause A supplier for a throwable used as the underlying cause of the exception. Defaults to null.
 * @return The validated collection if it is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is null or empty.
 *
 * This method checks the collection and throws a `ValidationFailedException` if the collection
 * is neither null nor empty. It supports customizable exception details, including the related
 * callable name, parameter name, custom message, and potential causes.
 *
 * @param callableName The name of the callable (e.g., function or method) where the validation is performed.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom message describing the validation failure.
 * @param causeOf A supplier providing a throwable to be used as the primary cause of the failure, or null.
 * @param cause A supplier providing a throwable to be linked as the underlying cause, or null.
 * @return The collection itself if the validation passes, enabling method chaining.
 * @throws ValidationFailedException If the collection is not null or not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection is null or empty, throwing a `ValidationFailedException` if it is not.
 *
 * This method is utilized to ensure that a given collection either has no elements or is null.
 * If the collection is neither null nor empty, it throws a `ValidationFailedException`.
 * Optionally, additional context such as a custom message or a cause may be provided to the exception.
 *
 * @param callableName The name of the callable (e.g., function or property) where the validation is performed. Can be null.
 * @param parameter The `KParameter` associated with the validation, if applicable. Can be null.
 * @param message An optional custom error message to include in the exception. If null, a default message will be used.
 * @param causeOf A `ThrowableSupplier` providing the main cause for the exception. If null, the default exception generation is used.
 * @param cause A `ThrowableSupplier` providing an additional cause for the exception. Can be null.
 * @return The validated collection if it is null or empty, allowing it to be further used in a fluent style.
 * @throws ValidationFailedException If the collection is neither null nor empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Collection<E>?, E> T.validateNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}

/**
 * Validates that the given iterable contains the specified element. If the element is not
 * present, a ValidationFailedException is thrown. Optionally, a custom transformer can be
 * provided to generate the cause for the exception.
 *
 * @param element The element expected to be present in the iterable.
 * @param causeOf Optional transformer to generate the primary cause of the exception.
 * @param cause Optional transformer to generate the secondary cause of the exception.
 * @return The original iterable if the validation passes.
 * @throws ValidationFailedException If the element is not found in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateContains(element: E, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException("$element is not in the iterable.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$element is not in the iterable.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given iterable contains the specified element. If the element is not present,
 * a validation exception is thrown with an optional custom cause or message.
 *
 * @param element The element to check for presence within the iterable.
 * @param causeOf An optional transformer to generate a custom throwable as the cause if validation fails.
 * @param cause An optional transformer to derive an additional cause when the validation fails.
 * @param lazyMessage A transformer used to provide a deferred message for the validation failure.
 * @return The original iterable if the validation passes.
 * @throws ValidationFailedException if the specified element is not present in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateContains(element: E, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the iterable contains the specified element. If the element is not present,
 * a `ValidationFailedException` is thrown with an optional property and variable name context,
 * along with a customizable message and cause provided by optional transformers.
 *
 * @param element The element to validate whether it exists in the iterable.
 * @param property The property associated with this validation, providing context about the property in focus. Can be null.
 * @param variableName Optional name of the variable being validated, included in the exception message if not null.
 * @param message Optional custom message describing the validation failure. Defaults to a generic message.
 * @param causeOf Transformer that optionally defines the cause for the exception. Can be null.
 * @param cause Transformer to set an optional root cause for the exception. Can be null.
 * @return The original iterable if the validation passes.
 * @throws ValidationFailedException If the specified element is not present in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateContains(element: E, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current iterable contains the specified element. If the element is not found,
 * throws a [ValidationFailedException] with the provided properties and message.
 *
 * @param element The element that must be present in the iterable.
 * @param property The main KProperty associated with the validation, or null if not applicable.
 * @param variable An optional secondary KProperty providing additional context, or null if not applicable.
 * @param message An optional message to include in the exception if validation fails.
 * @param causeOf An optional transformer function to produce the specific cause of the exception
 *                if validation fails, or null if not applicable.
 * @param cause An optional transformer function to produce a nested cause of the exception, or null
 *              if not applicable.
 * @return The same iterable instance if the validation passes.
 * @throws ValidationFailedException If the specified element is not found within the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateContains(element: E, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the iterable contains the specified element. If the element is not found,
 * a `ValidationFailedException` will be thrown.
 *
 * @param element The element that should be present in the iterable.
 * @param callable The Kotlin function (`KFunction`) associated with the validation. Used for contextual exception reporting. Can be null.
 * @param parameterName The name of the parameter in the associated callable that caused the validation failure. Can be null.
 * @param message An optional custom message to provide details about the validation failure. Defaults to a generated message indicating the absence of the element.
 * @param causeOf An optional transformer function that generates a `Throwable` as the main cause of the validation failure. Can be null.
 * @param cause An optional transformer function that generates a `Throwable` for additional context regarding the validation failure. Can be null.
 * @return The original iterable if the specified element is present.
 * @throws ValidationFailedException If the iterable does not contain the specified element.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateContains(element: E, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given iterable contains the specified element. If the validation fails,
 * a `ValidationFailedException` is thrown.
 *
 * @param element the element to check for existence within the iterable
 * @param callable the function associated with the validation, or null if not applicable
 * @param parameter the parameter involved in the validation, or null if not applicable
 * @param message an optional message describing the validation failure
 * @param causeOf an optional transformer that generates a throwable cause based on the current context, or null if not applicable
 * @param cause an optional transformer that generates the underlying cause of the failure based on the current context, or null if not applicable
 * @return the original iterable instance if the validation succeeds
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateContains(element: E, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the iterable contains the specified element. If the element is not present,
 * a `ValidationFailedException` is thrown with detailed information about the validation failure.
 *
 * @param element The element that must be present in the iterable.
 * @param callableName The name of the function or method performing the validation, used for error context.
 * @param parameterName The name of the parameter being validated, used for error context (optional).
 * @param message A custom error message to include in the exception if validation fails (optional).
 * @param causeOf A transformer that generates an alternate cause of the exception based on the current state (optional).
 * @param cause A transformer that generates the root cause of the exception based on the current state (optional).
 * @return The original iterable if validation passes.
 * @throws ValidationFailedException if the specified element is not present in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateContains(element: E, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given iterable contains a specific element. If the element is not found, a
 * `ValidationFailedException` is thrown.
 *
 * @param element The element that should be present in the iterable.
 * @param callableName An optional name of the callable (e.g., function or property) to include in the exception message.
 * @param parameter An optional `KParameter` associated with the validation failure.
 * @param message An optional custom message to include in the `ValidationFailedException`.
 * @param causeOf An optional transformer to generate a custom `Throwable` as the main cause of the exception.
 * @param cause An optional transformer to generate a nested `Throwable` as the cause of the exception.
 * @return The original iterable if the validation passes.
 * @throws ValidationFailedException If the element is not found in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateContains(element: E, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current iterable does not contain the specified element.
 * If the element is found in the iterable, a `ValidationFailedException` is thrown.
 *
 * @param element the element to check for absence in the iterable.
 * @param causeOf a transformer that generates a throwable based on the iterable, used as the cause for the exception if provided.
 * @param cause an optional transformer that generates the underlying cause throwable for the exception.
 * @return the same iterable instance if the validation passes without throwing an exception.
 * @throws ValidationFailedException if the specified element is found in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateNotContains(element: E, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException("$element is in the iterable.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$element is in the iterable.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the iterable object does not contain the specified element.
 * If the element is found, a `ValidationFailedException` is thrown with the provided lazy message.
 *
 * @param element The element to check for in the iterable object.
 * @param causeOf An optional transformer function for creating a specific cause of type `Throwable` when validation fails.
 * @param cause An optional transformer function for generating the underlying cause of type `Throwable` when validation fails.
 * @param lazyMessage A transformer function that generates the error message to be included in the exception if validation fails.
 * @return The original iterable object if validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateNotContains(element: E, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking collection does not contain the specified element. If the element is present,
 * a `ValidationFailedException` is thrown with the provided or default details.
 *
 * @param element The element that the collection must not contain.
 * @param property The property associated with the validation, used for error reporting. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Used in the error message if provided.
 * @param message An optional custom message describing the validation failure. If not specified, a default message is used.
 * @param causeOf An optional transformer to generate the root cause of the exception, based on the collection context.
 * @param cause An optional transformer to generate a throwable detailing the cause of the failure, based on the collection context.
 * @return The original collection if validation passes without exception.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateNotContains(element: E, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the iterable instance does not contain the specified element. If the element is found,
 * a `ValidationFailedException` is thrown. The exception message and cause can be customized.
 *
 * @param element the element to check for in the iterable instance
 * @param property the main property associated with the validation, providing context if validation fails
 * @param variable an optional secondary property providing additional context if validation fails
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf a transformer for generating a specific throwable to be used as the cause if validation fails
 * @param cause an alternative transformer for generating a specific throwable to serve as the cause if validation fails
 * @return the original iterable instance if validation passes
 * @throws ValidationFailedException if the element is found in the iterable
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateNotContains(element: E, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the iterable does not contain a specific element and throws a `ValidationFailedException` if it does.
 *
 * @param element The element to check for in the iterable.
 * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can be null.
 * @param parameterName The name of the parameter in the given callable that caused the validation issue. Can be null.
 * @param message An optional custom message providing additional details about the validation failure. Default is null.
 * @param causeOf A transformer function that produces a throwable cause for the validation failure based on the iterable. Can be null.
 * @param cause An alternative transformer function that generates a throwable cause for the validation failure. Used if `causeOf` is not provided. Can be null.
 * @return The original iterable (`this`) if validation succeeds.
 * @throws ValidationFailedException If the element exists in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateNotContains(element: E, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the iterable does not contain the specified element. If the element is found,
 * a [ValidationFailedException] is thrown with optional details about the callable, parameter,
 * message, and cause.
 *
 * @param element the element to check for in the iterable
 * @param callable the [KFunction] related to the validation, or null if not applicable
 * @param parameter the [KParameter] that triggered the validation, or null if not applicable
 * @param message an optional message providing context for the validation failure
 * @param causeOf an optional transformer for generating the cause of the exception based on the iterable
 * @param cause an optional transformer for setting the underlying cause of the exception
 * @return the original iterable if the element is not found
 * @throws ValidationFailedException if the element is found in the iterable
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateNotContains(element: E, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the iterable does not contain the specified element. Throws a [ValidationFailedException]
 * if the element is found within the iterable.
 *
 * @param element the element to check for existence in the iterable; if found, validation fails
 * @param callableName the name of the function or method that triggered the validation
 * @param parameterName an optional name of the parameter being validated
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf an optional transformer that generates the root cause of the exception based on the iterable
 * @param cause an optional transformer that generates the cause of the exception based on the iterable
 * @return the original iterable instance if validation succeeds (i.e., the element is not found)
 * @throws ValidationFailedException if the iterable contains the specified element
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateNotContains(element: E, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling collection does not contain the specified element.
 * If the element is found, a `ValidationFailedException` is thrown.
 *
 * @param element The element that should not be present in the iterable.
 * @param callableName The name of the callable (e.g., function or property) where this validation occurs, or null if not specified.
 * @param parameter The `KParameter` associated with the validation, or null if not applicable.
 * @param message An optional custom error message for the validation failure.
 * @param causeOf An optional `Transformer` that generates the cause for the exception upon validation failure.
 * @param cause An optional `Transformer` that generates an additional cause for the exception.
 * @return The original iterable, if the validation passes without throwing an exception.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Iterable<E>, E> T.validateNotContains(element: E, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $element", cause?.invoke(this)))
    return this
}

/**
 * Ensures that the collection has the specified size. If the size does not match,
 * an exception is thrown, optionally using provided transformers to generate the cause.
 *
 * @param size The expected size of the collection.
 * @param causeOf A transformer to generate the root cause of the exception if the size does not match.
 * @param cause A transformer to generate an additional cause for the exception if the size does not match.
 * @return The collection itself if the size matches the expected value.
 * @throws ExpectationMismatchException If the collection size does not match the expected value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectSize(size: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException("The collection is not of size $size.", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException("The collection is not of size $size.", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the size of the collection matches the expected size. Throws a
 * `ExpectationMismatchException` if the size does not match.
 *
 * @param size The expected size of the collection.
 * @param causeOf A transformer that converts the collection into a throwable cause, used for exception chaining (optional).
 * @param cause A transformer that generates a throwable cause for the exception (optional).
 * @param lazyMessage A transformer that generates the error message to be included in the exception.
 * @return The collection itself if the size matches the expected size.
 * @throws ExpectationMismatchException If the collection size does not match the expected size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectSize(size: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Ensures that the collection has the specified size. If the size does not match, a validation exception is thrown.
 *
 * @param size The expected size of the collection.
 * @param property The property associated with the validation, if applicable. Can be null if not relevant.
 * @param variableName The optional name of the variable being validated. Used in the exception message if provided.
 * @param message An optional custom message for the validation failure. Defaults to a message indicating the expected size.
 * @param causeOf An optional transformer that generates the root cause of the validation exception.
 * @param cause An optional transformer that generates a detailed cause for the validation failure.
 * @return The original collection if the size matches the expected value.
 * @throws ExpectationMismatchException If the size of the collection does not match the expected value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectSize(size: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(property, variableName, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variableName, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Validates that the size of the collection is equal to the expected size.
 *
 * If the size of the collection does not match the given `size`, a `ExpectationMismatchException` is thrown.
 *
 * @param size The expected size of the collection.
 * @param property The main property associated with the validation failure. Can be null if not specified.
 * @param variable An optional secondary property providing additional context. Can be null if not specified.
 * @param message An optional custom message for the validation failure. Defaults to a generated message if null.
 * @param causeOf A transformer function that generates a custom throwable as the root cause of the exception. Can be null if not specified.
 * @param cause A transformer function that generates a throwable to be used as the exception's cause. Can be null if not specified.
 * @return The original collection if the size matches the expected value.
 * @throws ExpectationMismatchException If the size of the collection does not match the expected size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectSize(size: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(property, variable, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variable, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Validates that the size of the collection matches the specified size. If the size does not match,
 * a validation exception is thrown.
 *
 * @param size The expected size of the collection.
 * @param callable The Kotlin function (`KFunction`) related to the validation. Can be null.
 * @param parameterName The name of the parameter in `callable` that this validation applies to. Can be null.
 * @param message A custom error message to include with the exception if validation fails. Can be null.
 * @param causeOf A transformer that generates a `Throwable` to represent the root cause of the exception. Can be null.
 * @param cause A transformer that generates a `Throwable` for additional information about the validation failure. Can be null.
 * @return The original collection if the size is as expected.
 * @throws ExpectationMismatchException If the collection size does not match the expected size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectSize(size: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameterName, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Validates that a collection has the expected size. If the validation fails, a `ExpectationMismatchException`
 * is thrown with detailed information about the failure.
 *
 * @param size the expected size of the collection.
 * @param callable an optional reference to the `KFunction` related to this validation, used for exception context.
 * @param parameter an optional reference to the `KParameter` involved in the validation, used for exception context.
 * @param message an optional custom message to include in the exception if validation fails.
 * @param causeOf an optional transformer that generates a specific throwable to be used as the primary exception.
 * @param cause an optional transformer that generates a specific throwable to be included as the cause of the exception.
 * @return the original collection if the validation succeeds.
 * @throws ExpectationMismatchException if the collection's size does not match the expected size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectSize(size: Int, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameter, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the size of the collection matches the specified value. If the size does not match,
 * throws a `ExpectationMismatchException` with details about the callable, parameter, provided message, and optional cause.
 *
 * @param size the expected size of the collection
 * @param callableName the name of the callable (e.g., function or method) for context in exception messages
 * @param parameterName the name of the parameter being validated, optional
 * @param message an optional custom message for the validation failure
 * @param causeOf an optional transformer used to generate the primary cause of the exception from the collection
 * @param cause an optional transformer used to generate an additional cause of the exception from the collection
 * @return the collection itself if the validation succeeds
 * @throws ExpectationMismatchException if the collection size does not match the expected size
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectSize(size: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameterName, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the collection has the specified size. Throws a `ExpectationMismatchException` if the size does not match.
 *
 * @param size The expected size of the collection.
 * @param callableName The name of the callable (e.g., function or property) where this validation is applied, or null if not specified.
 * @param parameter The parameter being validated, represented as a `KParameter`, or null if not applicable.
 * @param message An optional custom error message to describe the validation failure. Default is a message indicating the expected size.
 * @param causeOf An optional transformer to generate a throwable that serves as the primary cause of the exception.
 * @param cause An optional transformer to generate a throwable that provides additional context for the exception.
 * @return The same collection if its size matches the specified value.
 * @throws ExpectationMismatchException If the collection's size does not match the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectSize(size: Int, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameter, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the collection does not have the specified size. If the collection's size
 * matches the provided size, a [ExpectationMismatchException] is thrown.
 *
 * @param size The size that the collection should not equal.
 * @param causeOf A transformer function that generates a custom throwable for the failure using the collection context, or null.
 * @param cause A transformer function that generates a custom underlying cause for the validation failure, or null.
 * @return The original collection if the validation passes.
 * @throws ExpectationMismatchException if the collection's size matches the specified size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectNotSize(size: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException("The collection is of size $size.", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException("The collection is of size $size.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection does not have the specified size. If the collection size matches
 * the given size, a `ExpectationMismatchException` is thrown with the specified message and cause.
 *
 * The function is designed to be used within a fluent API style, where the collection itself
 * is returned when the validation passes.
 *
 * @param size the size that the collection should not have.
 * @param causeOf an optional transformer to create a specific throwable based on the collection.
 * @param cause an optional transformer to define the underlying cause of the exception.
 * @param lazyMessage a transformer to generate the exception message using the collection.
 * @return the same collection instance if the validation passes.
 * @throws ExpectationMismatchException if the collection has the specified size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectNotSize(size: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Ensures that the size of the collection is not equal to the specified size. If the size of the
 * collection matches the provided size, a `ExpectationMismatchException` is thrown.
 *
 * @param size The size that the collection should not match.
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName The name of the variable being validated. Optional parameter.
 * @param message An optional custom message to include in the exception if validation fails.
 *                If null, a default message will be used.
 * @param causeOf A transformer function that generates the root cause exception, if applicable.
 * @param cause A transformer function to specify the cause of the `ExpectationMismatchException`,
 *              applicable when `causeOf` is not provided.
 * @return The original collection to enable method chaining.
 * @throws ExpectationMismatchException If the size of the collection matches the specified size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectNotSize(size: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(property, variableName, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variableName, message ?: "is of size $size", cause?.invoke(this)))
    return this
}
/**
 * Validates that the collection does not have the specified size.
 * If the collection's size matches the specified size, a `ExpectationMismatchException` is thrown.
 *
 * @param size the size to validate against; the method will throw an exception if the collection's size matches this value
 * @param property the main `KProperty` associated with the validation failure, or null if not specified
 * @param variable an optional secondary `KProperty` that provides additional context, or null if not specified
 * @param message an optional message to include in the exception if the validation fails; defaults to a description of the issue
 * @param causeOf an optional transformer function to create a custom throwable as the primary cause, or null if not specified
 * @param cause an optional transformer function to provide additional contextual information for the exception, or null if not specified
 * @return the original collection if the size validation is successful
 * @throws ExpectationMismatchException if the collection's size matches the specified size
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectNotSize(size: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(property, variable, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variable, message ?: "is of size $size", cause?.invoke(this)))
    return this
}
/**
 * Verifies that the collection's size is not equal to the specified size. If the size matches,
 * a `ExpectationMismatchException` is thrown. This is often used for validation purposes in contexts
 * such as data transformation pipelines.
 *
 * @param size The size that the collection must not equal.
 * @param callable The function (`KFunction`) associated with this validation check. Can be null.
 * @param parameterName The name of the parameter in the specified callable that this validation applies to. Can be null.
 * @param message An optional custom error message to provide additional context for the failure. Defaults to null.
 * @param causeOf An optional transformer responsible for deriving the root cause if the validation fails. Defaults to null.
 * @param cause An optional transformer responsible for deriving additional details about the failure's cause. Defaults to null.
 * @return The original collection (`this`) if the size validation passes.
 * @throws ExpectationMismatchException If the collection's size matches the specified size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectNotSize(size: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameterName, message ?: "is of size $size", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the size of the collection does not match the specified size. If the size matches, a
 * [ExpectationMismatchException] is thrown.
 *
 * @param size the size that the collection is asserted to not have.
 * @param callable an optional [KFunction] representing the callable involved in the validation, used for contextual information.
 * @param parameter an optional [KParameter] representing the parameter involved in the validation, used for contextual information.
 * @param message an optional message providing additional context about the validation failure.
 * @param causeOf an optional transformer that generates the cause of the exception from the collection, providing detailed contextual information.
 * @param cause an optional transformer that generates the exception's direct cause from the collection, offering additional context.
 * @return the collection if its size does not match the specified size.
 * @throws ExpectationMismatchException if the size of the collection matches the specified size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectNotSize(size: Int, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameter, message ?: "is of size $size", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the collection does not have the specified size. If the collection's size matches the specified size,
 * a `ExpectationMismatchException` is thrown.
 *
 * @param size the size that the collection is expected not to match.
 * @param callableName the name of the callable (e.g., function or method) for context in the validation failure.
 * @param parameterName the name of the parameter causing the validation failure, or null if not applicable.
 * @param message an optional custom message to include in the exception, or null for a default message.
 * @param causeOf an optional transformer to generate the underlying cause exception based on the collection.
 * @param cause an optional transformer to generate a cause for the validation failure.
 * @return the collection itself if its size does not match the specified size, for chaining operations.
 * @throws ExpectationMismatchException if the collection's size matches the provided size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectNotSize(size: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameterName, message ?: "is of size $size", cause?.invoke(this)))
    return this
}
/**
 * Validates that the size of the collection is not equal to the specified size. If the collection's size
 * matches the given size, a `ExpectationMismatchException` is thrown.
 *
 * @param size The size that the collection's size should not match.
 * @param callableName The name of the callable (e.g., function or property) where this validation is applied, or null if not applicable.
 * @param parameter The parameter related to the validation, or null if not applicable.
 * @param message An optional error message providing additional context about the validation failure.
 * @param causeOf An optional transformer function that creates a throwable to describe the root cause of the validation failure, based on the collection.
 * @param cause An optional transformer function that provides the underlying cause of the validation failure, based on the collection.
 * @return The collection itself, if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Collection<E>, E> T.expectNotSize(size: Int, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameter, message ?: "is of size $size", cause?.invoke(this)))
    return this
}