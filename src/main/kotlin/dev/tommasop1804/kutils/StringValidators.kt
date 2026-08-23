/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
@file:JvmName("StringValidatorsKt")
@file:Since("5.0.0")
@file:Suppress("unused")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.contract
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

/**
 * Validates that the given character sequence is not empty.
 * If the sequence is empty, a `ValidationFailedException` is thrown with an optional cause.
 *
 * @param causeOf an optional supplier that provides the primary cause of the exception.
 * @param cause an optional supplier that provides an additional underlying cause of the exception.
 * @return the original character sequence if it is not empty.
 * @throws ValidationFailedException if the character sequence is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException("The char sequence is empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The char sequence is empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` is not empty. If the validation fails,
 * a `ValidationFailedException` is thrown with a supplied message and optional cause.
 *
 * @param causeOf an optional supplier for a throwable that represents the primary cause
 *                of the validation failure. If provided, this will be thrown and initialized
 *                with a `ValidationFailedException` as its cause.
 * @param cause an optional supplier for a throwable representing additional context
 *              for the validation failure. This will be used as the cause for the
 *              `ValidationFailedException` if no primary cause is supplied.
 * @param lazyMessage a supplier for the error message to be included in the exception
 *                    if validation fails.
 * @return the current `CharSequence` instance if validation passes.
 * @throws ValidationFailedException if the validation fails and the `CharSequence` is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is not empty. If the sequence is empty, throws a ValidationFailedException.
 *
 * @param property The property associated with the validation failure. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Included in the exception message if provided.
 * @param message An optional custom error message. Defaults to "is empty" if not specified.
 * @param causeOf A supplier for the primary cause of the exception. If not null, initializes the exception with this cause.
 * @param cause A supplier for an optional additional cause of the exception.
 * @return The original CharSequence if it is not empty.
 * @throws ValidationFailedException if the CharSequence is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a `CharSequence` is not empty. If the `CharSequence` is empty, a `ValidationFailedException` is thrown.
 *
 * @param property the primary property associated with the validation, or null if not specified
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional custom error message to include in the exception, or null for a default message
 * @param causeOf an optional supplier for the underlying cause of the exception, or null if not specified
 * @param cause an optional supplier for an additional nested cause, or null if not specified
 * @return the validated `CharSequence` if it is not empty
 * @throws ValidationFailedException if the `CharSequence` is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [CharSequence] is not empty. If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) related to the validation. Can be null.
 * @param parameterName The name of the parameter related to the validation. Can be null.
 * @param message An optional custom message for the validation failure. Defaults to "is empty" if not provided.
 * @param causeOf A supplier for the throwable to be used as the primary cause if the validation fails. Can be null.
 * @param cause A supplier for an additional cause to be attached to the validation exception. Can be null.
 * @return The original [CharSequence] if the validation passes.
 * @throws ValidationFailedException if the [CharSequence] is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given CharSequence is not empty.
 *
 * @param callable The function in which the validation is being performed, or null if not applicable.
 * @param parameter The parameter related to the validation failure, or null if not applicable.
 * @param message An optional message to include if the validation fails, defaulting to null.
 * @param causeOf A supplier for the cause to be used if the validation fails and a specific root cause should be provided, defaulting to null.
 * @param cause A supplier for the exception cause to include in the thrown exception, defaulting to null.
 * @return The original CharSequence if it is not empty.
 * @throws ValidationFailedException If the CharSequence is empty, with detailed information about the failure.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is not empty. If the sequence is empty, a
 * ValidationFailedException is thrown with the specified details.
 *
 * @param callableName the name of the callable (e.g., function or method) associated with this validation.
 * @param parameterName the name of the parameter being validated, or null if unspecified.
 * @param message a custom error message to provide additional context for the validation failure, or null for a default message.
 * @param causeOf a supplier that provides a custom throwable as the root cause of the exception, or null if not defined.
 * @param cause a supplier for an additional throwable to be associated as the cause of the exception, or null if not provided.
 * @return the current CharSequence if validation passes.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given CharSequence is not empty.
 * Throws a ValidationFailedException if the validation fails.
 *
 * @param callableName The name of the callable (e.g., function or property) being validated, or null if not specified.
 * @param parameter The KParameter instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message to provide additional context when the exception is thrown.
 * @param causeOf A supplier for the throwable to be used as the cause of the validation failure, or null if not specified.
 * @param cause A supplier for an additional exception to be attached as the cause, or null if not specified.
 * @return The same CharSequence if it is not empty.
 * @throws ValidationFailedException If the CharSequence is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the receiver `CharSequence` is not null or empty.
 *
 * If the `CharSequence` is null or empty, a `ValidationFailedException` is thrown.
 * The exception can optionally include a custom cause or a cause of.
 *
 * @param causeOf a supplier for an optional `Throwable` to be used as the primary cause of the exception.
 * @param cause a supplier for an optional `Throwable` to be included as the underlying cause of the exception.
 * @return the validated `CharSequence`, ensuring it is not null or empty.
 * @throws ValidationFailedException if the `CharSequence` is null or empty.
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException("The char sequence is null or empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The char sequence is null or empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the `CharSequence` is not null or empty. If the validation fails,
 * a `ValidationFailedException` is thrown with the specified lazy message and optional causes.
 *
 * @param causeOf an optional supplier for a throwable that represents the primary cause of the exception.
 * @param cause an optional supplier for a throwable that represents a secondary cause of the exception.
 * @param lazyMessage a supplier for the exception message, executed lazily when the validation fails.
 * @return the validated `CharSequence` if it is not null or empty.
 * @throws ValidationFailedException if this `CharSequence` is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling [CharSequence] is not null or empty. If the validation fails, a
 * [ValidationFailedException] is thrown with an optional custom message and cause.
 *
 * @param property The associated property being validated. Can be null if there is no specific property.
 * @param variableName An optional name for the variable being validated. Used in the error message if provided.
 * @param message An optional custom message explaining the validation failure. Default message is "is null or empty".
 * @param causeOf A supplier for the root cause of the validation failure. If not null, it is used to create the exception.
 * @param cause A supplier for an additional cause, chained as the underlying throwable of the validation exception.
 * @return The original [CharSequence] if validation is successful.
 * @throws ValidationFailedException If the calling [CharSequence] is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a given CharSequence is not null or empty.
 *
 * Throws a [ValidationFailedException] if the CharSequence is null or empty.
 * The exception message can be customized using the `message` parameter.
 * Additional context can be provided through the `property` and `variable` parameters.
 * Optional throwable suppliers can be used to define the root cause or an additional cause for the exception.
 *
 * @param property the main property associated with the validation, or null if not provided
 * @param variable an optional secondary property providing additional context, or null if not provided
 * @param message a custom message to append to the exception, or null for a default message
 * @param causeOf a supplier for the root cause of the exception, or null if not provided
 * @param cause an additional cause supplier for the exception, or null if not provided
 * @return the original CharSequence instance if validation succeeds
 * @throws ValidationFailedException if the CharSequence is null or empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` is not null or empty. If the validation fails, a
 * `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation error relates. Can be null.
 * @param parameterName The name of the parameter in the given callable that failed validation. Can be null.
 * @param message An optional custom error message describing the validation failure. Defaults to "is null or empty".
 * @param causeOf A supplier providing the cause of the validation failure as a `Throwable`. If provided, it is used to
 *               initialize the `ValidationFailedException`.
 * @param cause A supplier providing additional context as a `Throwable` for the `ValidationFailedException`.
 * @return The validated `CharSequence` if it is neither null nor empty.
 * @throws ValidationFailedException If the current `CharSequence` is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence is not null or empty. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] associated with the validation, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional message describing the validation failure for additional context, defaulting to null
 * @param causeOf a supplier for the primary `Throwable` cause of the validation failure, or null if not applicable
 * @param cause a supplier for an additional `Throwable` to be used as the cause, or null if not specified
 * @return the original character sequence if the validation passes
 * @throws ValidationFailedException if the character sequence is null or empty, with optional details provided by the callable, parameter, message, or causes
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` is not null or empty.
 * If the validation fails, a `ValidationFailedException` is thrown with the provided details.
 *
 * @param callableName the name of the callable (e.g., function or method) performing the validation, or null if not specified.
 * @param parameterName the name of the parameter being validated, or null if not specified.
 * @param message an optional custom message to include in the exception if validation fails.
 * @param causeOf a supplier for the root cause of the validation failure, or null if not specified.
 * @param cause a supplier for an additional cause of the exception, or null if not specified.
 * @return the current `CharSequence` if validation succeeds.
 * @throws ValidationFailedException if the `CharSequence` is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` is not null or empty.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param callableName The name of the callable (e.g., function or property) associated with this validation, or null if not specified.
 * @param parameter The parameter being validated, represented as a `KParameter`, or null if not applicable.
 * @param message An optional error message to include in the exception if validation fails, or null to use the default message.
 * @param causeOf An optional supplier for the underlying `Throwable` that triggered the validation failure, or null if absent.
 * @param cause An additional optional supplier for the `Throwable` to attach as the cause of the failure, or null if absent.
 * @return The validated `CharSequence` if it is not null or empty.
 * @throws ValidationFailedException If the `CharSequence` is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is empty. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param causeOf an optional supplier for the exception to be thrown as the root cause if the validation fails.
 * @param cause an optional supplier for an additional cause to be associated with the thrown exception.
 * @return the current CharSequence if it passes the validation (i.e., it is empty).
 * @throws ValidationFailedException if the CharSequence is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException("The char sequence is not empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The char sequence is not empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that a [CharSequence] is empty. If the string is not empty, a validation
 * exception is thrown using the provided message and optional cause suppliers.
 *
 * @param causeOf A supplier for the primary cause of the validation failure (nullable). If this
 * supplier is provided, its result is used as the root exception cause.
 * @param cause A supplier for an additional cause to be chained to the validation failure (nullable).
 * This is used only when `causeOf` is not provided.
 * @param lazyMessage A supplier for the message to be used in the validation exception. The message
 * is lazily computed.
 * @return The original [CharSequence] instance if it is empty.
 * @throws ValidationFailedException if the [CharSequence] is not empty, with an appropriate
 * message and cause provided by the supplier arguments.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given [CharSequence] is empty. If the sequence is not empty, throws a [ValidationFailedException].
 *
 * @param property The property associated with the validation. May be null if not applicable.
 * @param variableName An optional name for the variable being validated. Useful for including in error messages.
 * @param message An optional custom error message to describe the validation failure. Defaults to "is not empty".
 * @param causeOf A supplier for the throwable that should be thrown instead of the default [ValidationFailedException].
 * @param cause A supplier for the throwable that should be set as the cause of the validation error, if any.
 * @return The original [CharSequence] if the validation passes (i.e., it is empty).
 * @throws ValidationFailedException If the [CharSequence] is not empty and no custom throwable is provided.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the `CharSequence` is empty. If it is not empty, a `ValidationFailedException` is thrown.
 *
 * @param property the primary property associated with the validation, may be null
 * @param variable an optional secondary property for additional context, may be null
 * @param message an optional message to describe the validation failure, defaults to a default message if null
 * @param causeOf an optional supplier for the cause of the exception, may be null
 * @param cause an optional supplier for an additional cause, may be null
 * @return the same `CharSequence` if validation passes
 * @throws ValidationFailedException if the `CharSequence` is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence is empty. If the sequence is not empty, a
 * `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can be null.
 * @param parameterName The name of the parameter in the given callable that caused the validation issue. Can be null.
 * @param message An optional custom message providing additional details about the validation failure. If null, a default message "is not empty" is used.
 * @param causeOf A supplier for the specific cause of the validation failure, represented as a `Throwable`. If null, the cause will not be supplied by this parameter.
 * @param cause A supplier for a general cause of the validation failure, used to chain exceptions. Can be null.
 * @return The validated character sequence if it is empty.
 * @throws ValidationFailedException If the character sequence is not empty, with optional details provided by `callable`, `parameterName`, `message`, `causeOf`, or `cause`.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [CharSequence] is empty.
 * Throws a [ValidationFailedException] if the [CharSequence] is not empty.
 *
 * @param callable the [KFunction] related to the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter associated with the validation context, or null if not applicable
 * @param message an optional message providing additional context about the validation failure, defaults to null
 * @param causeOf a supplier for the root cause of the exception if validation fails, defaults to null
 * @param cause a supplier for the underlying cause of the validation failure, defaults to null
 * @return the original [CharSequence] if it is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` is empty. If the `CharSequence` is not empty,
 * throws a `ValidationFailedException` with the provided details.
 *
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom message indicating the nature of the validation failure.
 * @param causeOf A supplier that provides the root cause of the validation failure, or null if not applicable.
 * @param cause A supplier that provides a secondary cause for the exception, or null if not applicable.
 * @return The original `CharSequence` if it is empty.
 * @throws ValidationFailedException if the `CharSequence` is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given CharSequence is empty.
 *
 * If the CharSequence is not empty, a [ValidationFailedException] is thrown. The exception includes
 * optional details like the callable name, parameter, a custom message, and causes for the exception.
 *
 * @param callableName The name of the callable where the validation is performed, or null if not specified.
 * @param parameter The KParameter instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message providing additional context for the validation failure.
 * @param causeOf An optional supplier for the primary cause of the exception.
 * @param cause An optional supplier for a secondary cause of the exception.
 * @return The original CharSequence if it is empty.
 * @throws ValidationFailedException If the CharSequence is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` is either null or empty.
 * If the `CharSequence` is not null and not empty, a `ValidationFailedException` is thrown.
 *
 * @param causeOf an optional supplier of a custom exception that will serve as the cause of the validation exception.
 *                If this parameter is null, a default `ValidationFailedException` will be used.
 * @param cause an optional supplier of a secondary cause for the exception, which provides additional context.
 *              This is used to chain exceptions and can be null.
 * @return the original `CharSequence` if it is successfully validated as null or empty.
 * @throws ValidationFailedException if the `CharSequence` is not null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException("The char sequence is not null or empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The char sequence is not null or empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling CharSequence instance is null or empty.
 * If the instance is not null or empty, an exception is thrown with the specified cause
 * and message.
 *
 * @param causeOf an optional supplier for a specific throwable to be returned,
 *        or null if no specific throwable is required.
 * @param cause an optional supplier for the cause of the exception.
 * @param lazyMessage a supplier for the error message to be used in the exception.
 * @return the calling CharSequence instance if the validation passes.
 * @throws ValidationFailedException if the CharSequence is not null and not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is null or empty. If the validation fails,
 * a [ValidationFailedException] is thrown with the provided details.
 *
 * @param property The Kotlin property associated with the validation. This is optional and can be null.
 * @param variableName An optional name of the variable being validated. Defaults to null.
 * @param message An optional custom message to describe the validation failure. Defaults to null.
 * @param causeOf A supplier for the primary cause of the exception. If present, the exception returned by this supplier is used
 *                as the cause of the validation exception.
 * @param cause An optional supplier for an additional nested exception to be included as the cause of the validation exception. Defaults to null.
 * @return The current CharSequence instance if the validation passes.
 * @throws ValidationFailedException If the validation fails, with details about the failure.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given CharSequence is null or empty. If it is not null or empty,
 * a `ValidationFailedException` is thrown.
 *
 * The exception message is constructed using the provided KProperties and optional message.
 * A custom throwable cause can also be included when the exception is thrown.
 *
 * @param property the primary KProperty associated with the validation, or null if not specified
 * @param variable an optional secondary KProperty that provides additional context, or null if not specified
 * @param message an optional message providing further details about the validation failure
 * @param causeOf a supplier for the exception cause, which can generate a throwable when invoked, or null if not specified
 * @param cause an additional supplier for a throwable cause, or null if not specified
 * @return the original CharSequence if it passes the validation (i.e., it is null or empty)
 * @throws ValidationFailedException if the CharSequence is not null or empty, including the provided details and causes
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `CharSequence` instance is null or empty.
 * If the validation fails, throws a `ValidationFailedException` with the specified details.
 *
 * @param callable The Kotlin function (`KFunction`) associated with the validation check. Can be null.
 * @param parameterName An optional name of the parameter being validated. Can be null.
 * @param message An optional custom message describing the validation failure. Defaults to "is not null or empty".
 * @param causeOf An optional custom supplier for the primary cause of the exception. If supplied, its result is used as the exception's cause.
 * @param cause An optional secondary supplier for additional exception cause details. Can be null.
 * @return Returns the current `CharSequence` instance for further usage if validation passes.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given CharSequence is null or empty. If the validation fails, throws a ValidationFailedException.
 *
 * @param callable the [KFunction] associated with the context of the validation, or null if not applicable
 * @param parameter the [KParameter] representing the parameter involved, or null if not applicable
 * @param message an optional custom message to include in the exception, or null to use a default message
 * @param causeOf an optional supplier for a specific exception to throw if the validation fails, or null to throw a default exception
 * @param cause an optional supplier for the root cause of the exception, or null if no root cause is provided
 * @return the original CharSequence if it passes the validation
 * @throws ValidationFailedException if the CharSequence is not null or empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given CharSequence is null or empty. Throws a `ValidationFailedException` if the validation fails.
 *
 * @param callableName The name of the callable (e.g., function or method) where the validation is being performed.
 * @param parameterName The name of the parameter being validated, or null if not specified.
 * @param message An optional custom message to be associated with the validation failure, or null for the default message.
 * @param causeOf A supplier for the exception that serves as the cause of the validation failure, if applicable.
 * @param cause A supplier for the exception that should be the root cause of the failure, or null if not specified.
 * @return The original CharSequence if validation passes.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given CharSequence is null or empty. If the CharSequence is not null or empty,
 * a `ValidationFailedException` is thrown with the provided details. This method returns the original
 * CharSequence if the validation succeeds.
 *
 * @param callableName The name of the function or property associated with the validation. Can be null.
 * @param parameter The parameter that this validation is related to. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Can be null.
 * @param causeOf An optional supplier for the root cause of the exception if validation fails. Can be null.
 * @param cause An optional supplier for additional context related to the failure, to be included as a cause. Can be null.
 * @return The original CharSequence if it is null or empty.
 * @throws ValidationFailedException If the CharSequence is not null or empty. The exception will include
 * details from the provided parameters to aid in debugging.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current `CharSequence` is not blank. If the sequence is blank, a
 * `ValidationFailedException` is thrown with the provided cause or message.
 *
 * @param causeOf A supplier that provides a throwable to be used as the primary exception cause,
 *                or `null` if no specific cause supplier is provided.
 * @param cause   A supplier that provides a throwable to be used as the underlying cause for the exception,
 *                or `null` if no specific cause supplier is provided.
 * @return The current `CharSequence` if it is not blank.
 * @throws ValidationFailedException if the current `CharSequence` is blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotBlank(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isBlank()) throw if (causeOf == null) ValidationFailedException("The char sequence is blank.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The char sequence is blank.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is not blank. Throws a ValidationFailedException
 * if the CharSequence is blank.
 *
 * @param causeOf an optional supplier that provides the cause of the exception. If null, defaults to a standard ValidationFailedException.
 * @param cause an optional supplier that provides an additional cause to be associated with the ValidationFailedException.
 * @param lazyMessage a supplier for the error message to be used in the exception if validation fails.
 * @return the original CharSequence if it passes the validation.
 * @throws ValidationFailedException if the CharSequence is blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotBlank(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isBlank()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is not blank (contains non-whitespace characters). If the validation
 * fails (i.e., the CharSequence is blank), a ValidationFailedException is thrown with an optional property,
 * variable name, custom message, and/or cause details.
 *
 * @param property The property associated with the validation failure. Can be null if not applicable.
 * @param variableName The name of the variable being validated. If null, it will not be included in the exception message.
 * @param message An optional custom message to include with the exception if validation fails. Defaults to "is blank".
 * @param causeOf A supplier for the throwable that serves as the main cause of the validation failure.
 *                If provided, the original exception will wrap the newly generated validation exception as its cause.
 * @param cause A supplier for an optional additional throwable cause to include with the exception.
 * @return The same CharSequence if validation passes (i.e., it is not blank).
 * @throws ValidationFailedException If the CharSequence is blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotBlank(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isBlank()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given [CharSequence] is not blank. If the validation fails,
 * a [ValidationFailedException] is thrown.
 *
 * @param property the main property being validated, used for contextual error information, or null if not applicable
 * @param variable an optional secondary property providing additional context, or null if not applicable
 * @param message an optional custom message describing the validation failure, or null if a default message should be used
 * @param causeOf an optional supplier for the cause exception to be thrown instead of creating a new [ValidationFailedException]
 * @param cause an optional supplier for providing the underlying cause of the [ValidationFailedException]
 * @return the original [CharSequence] if the validation passes
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotBlank(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isBlank()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [CharSequence] is not blank. If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param callable The function where the validation is being performed. This parameter is optional and can be null.
 * @param parameterName The name of the parameter being validated. This parameter is optional and can be null.
 * @param message A custom error message to include in the exception if validation fails. This parameter is optional and can be null.
 * @param causeOf A [ThrowableSupplier] that provides a cause for the exception, if any. This parameter is optional and can be null.
 * @param cause A [ThrowableSupplier] that provides an additional cause for the exception, if any. This parameter is optional and can be null.
 * @return The current [CharSequence] if it is not blank.
 * @throws ValidationFailedException if the current [CharSequence] is blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotBlank(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isBlank()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is not blank (i.e., not null, empty, or consisting only of whitespace characters).
 * If the validation fails, a ValidationFailedException is thrown.
 *
 * @param callable the [KFunction] associated with this validation, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional validation failure message, which defaults to "is blank" if not provided
 * @param causeOf an optional supplier of a [Throwable] to be used as the primary cause of the exception, or null
 * @param cause an optional supplier of a [Throwable] to be used as the secondary cause of the exception, or null
 * @return the validated CharSequence if it is not blank
 * @throws ValidationFailedException if the CharSequence is blank
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotBlank(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isBlank()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is not blank (contains non-whitespace characters).
 * Throws a [ValidationFailedException] if the validation fails.
 *
 * @param callableName the name of the callable (e.g., function or method) where the validation is performed.
 * @param parameterName the name of the parameter being validated, or null if not applicable.
 * @param message the custom message to include if the validation fails, or null to use a default message.
 * @param causeOf a supplier for the cause of the exception if validation fails, or null if not applicable.
 * @param cause an optional supplier for a secondary cause to associate with the validation failure, or null.
 * @return the validated CharSequence if it is not blank.
 * @throws ValidationFailedException if the CharSequence is blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotBlank(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isBlank()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [CharSequence] is not blank (not empty and not consisting solely of whitespace).
 * If the validation fails, throws a [ValidationFailedException].
 *
 * @param callableName The name of the callable (e.g., function or property) where the validation is performed, or null if not specified.
 * @param parameter The parameter being validated, represented as a [KParameter] instance, or null if not applicable.
 * @param message An optional error message to include in the exception if the validation fails. Defaults to "is blank".
 * @param causeOf An optional supplier for a custom exception to throw instead of a default [ValidationFailedException].
 * @param cause An optional supplier for the underlying cause of the exception, if any.
 * @return The original [CharSequence] if the validation passes.
 * @throws ValidationFailedException If the [CharSequence] is blank and no custom exception is supplied via [causeOf].
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotBlank(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isBlank()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [CharSequence] is neither `null` nor blank (consists solely of whitespace characters).
 * If it is, a [ValidationFailedException] is thrown. Optionally, custom suppliers for an exception or cause can be provided.
 *
 * @param causeOf A supplier for a custom exception to throw when validation fails. If not provided or `null`,
 *                a default [ValidationFailedException] is used.
 * @param cause A supplier for the underlying cause of the exception, if any. If `null`, no cause will be included.
 * @return The original [CharSequence], ensuring it is not `null` and not blank.
 * @throws ValidationFailedException If the [CharSequence] is `null` or blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrBlank(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrBlank != null)
    }
    if (isNullOrBlank()) throw if (causeOf == null) ValidationFailedException("The char sequence is null or blank.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The char sequence is null or blank.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the CharSequence is neither null nor blank (consisting of only whitespace characters).
 * Throws an exception if the validation fails.
 *
 * @param causeOf a supplier for a throwable that provides additional context or cause when the validation fails.
 * @param cause an optional supplier for a throwable to be used as the direct cause of the failure exception.
 * @param lazyMessage a supplier providing a message or object to describe the failure when the validation fails.
 * @return the validated CharSequence if it is not null and not blank.
 * @throws ValidationFailedException if the CharSequence is null or blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrBlank(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    contract {
        (this@validateNotNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrBlank != null)
    }
    if (isNullOrBlank()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoked [CharSequence] is not null or blank. If the value is null or blank, a
 * [ValidationFailedException] is thrown. This can be used to ensure that mandatory string properties or variables
 * are properly initialized and contain meaningful data.
 *
 * @param property The property associated with the validation, used for constructing the error message.
 *                 Can be null if not applicable.
 * @param variableName An optional name of the variable involved in the validation, used in the error message.
 *                     Defaults to null.
 * @param message An optional custom message describing the validation failure. Defaults to null. If not provided,
 *                a default "is null or blank" message is used.
 * @param causeOf A supplier for an alternative throwable cause for the exception. Defaults to null.
 * @param cause A supplier for the underlying cause of the exception. Defaults to null.
 * @return The original [CharSequence] itself if it is not null or blank.
 * @throws ValidationFailedException If the [CharSequence] is null or consists solely of whitespace characters.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrBlank(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrBlank != null)
    }
    if (isNullOrBlank()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is null or blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given [CharSequence] is not null or blank. If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param property The primary [KProperty] associated with the validation; can provide context about the invalid value, or `null`.
 * @param variable An optional secondary [KProperty] providing additional context about the validation, or `null`.
 * @param message An optional message describing the reason for the validation failure; default is "is null or blank".
 * @param causeOf A supplier providing a [Throwable] to be used as the root cause of the validation failure, or `null`.
 * @param cause A supplier providing a secondary [Throwable] to be used as the cause of the validation failure, or `null`.
 * @return The original [CharSequence], if it passes the validation.
 * @throws ValidationFailedException if the [CharSequence] is null or blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrBlank(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrBlank != null)
    }
    if (isNullOrBlank()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is null or blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given CharSequence is neither null nor blank. If the validation fails, an exception is thrown.
 *
 * @param callable The callable function that triggered the validation, used for contextual error reporting. Can be null.
 * @param parameterName The name of the parameter being validated, used for error message construction. Can be null.
 * @param message The custom error message to be included if validation fails. Defaults to "is null or blank" if not provided.
 * @param causeOf A supplier function that provides the root cause of the validation failure, if applicable. Can be null.
 * @param cause A supplier function that provides additional information about the cause of the failure, if applicable. Can be null.
 * @return The same non-null and non-blank CharSequence instance if validation succeeds.
 * @throws ValidationFailedException If the CharSequence is null or blank, with the appropriate context, message, and cause included.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrBlank(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrBlank != null)
    }
    if (isNullOrBlank()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is null or blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given CharSequence is neither null nor blank. If the validation fails,
 * an exception is thrown. This method is designed for parameter validation in function calls.
 *
 * @param callable The function in which this validation is occurring. Can be null.
 * @param parameter The parameter being validated. Can be null.
 * @param message An optional custom error message to include in the exception. Defaults to "is null or blank" if null.
 * @param causeOf An optional supplier for the root cause of the exception.
 * @param cause An optional supplier for the cause of the exception.
 * @return The validated CharSequence if it passes the validation.
 * @throws ValidationFailedException If the CharSequence is null or blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrBlank(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrBlank != null)
    }
    if (isNullOrBlank()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is null or blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given CharSequence is not null or blank. If the validation fails, an exception is thrown.
 *
 * @param callableName the name of the callable (e.g., method or function) where this validation occurs. Can be null.
 * @param parameterName the name of the parameter being validated. Can be null.
 * @param message the custom error message to include in the exception. Defaults to "is null or blank" if not provided.
 * @param causeOf a supplier for the root cause of the exception. This can be used to chain exceptions for better error tracking. Can be null.
 * @param cause a supplier for an additional exception cause. This is appended to provide further context about the error. Can be null.
 * @return the validated CharSequence, guaranteed to be non-null and non-blank, if no exception is thrown.
 * @throws ValidationFailedException if the CharSequence is null or blank, including any relevant messages and causes.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrBlank(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrBlank != null)
    }
    if (isNullOrBlank()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is null or blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that a given CharSequence value is neither null nor blank. If the value is null or blank, a
 * [ValidationFailedException] is thrown with the specified details.
 *
 * @param callableName An optional name of the callable or context in which the validation is being performed.
 *                     This is used in the exception message to aid debugging.
 * @param parameter    An optional [KParameter] that represents the parameter being validated.
 *                     This can provide additional context in the exception message.
 * @param message      An optional custom error message to include in the exception if validation fails.
 *                     If null, a default message "is null or blank" is used.
 * @param causeOf      An optional supplier for the primary cause of the exception. If provided, it will
 *                     initialize the cause of the [ValidationFailedException].
 * @param cause        An optional supplier for an additional cause to include in the exception.
 * @return The original [CharSequence] value, if it passes validation (is not null or blank).
 * @throws ValidationFailedException If the value is null or blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNotNullOrBlank(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrBlank != null)
    }
    if (isNullOrBlank()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is null or blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence is blank.
 * If the character sequence is not blank, a [ValidationFailedException] is thrown.
 *
 * @param causeOf an optional supplier for a custom [Throwable] to be thrown; if provided, this will be used
 *                to construct the exception. If null, [ValidationFailedException] is used by default.
 * @param cause an optional supplier for a cause [Throwable] to be attached to the thrown exception.
 *              Used as the reason for the validation failure.
 * @return the original character sequence if it is blank.
 * @throws ValidationFailedException if the character sequence is not blank and no custom [Throwable] is supplied.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateBlank(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotBlank()) throw if (causeOf == null) ValidationFailedException("The char sequence is not blank.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The char sequence is not blank.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling [CharSequence] is blank. If the string is not blank, it throws a
 * [ValidationFailedException] with a custom error message and optional cause.
 *
 * @param causeOf a supplier for a throwable that may be initialized as the cause of the validation failure.
 *                If not provided, a default [ValidationFailedException] is used as the cause.
 * @param cause an additional supplier for a throwable to be used as the inner cause of the exception.
 * @param lazyMessage a supplier for the error message to be included in the exception if validation fails.
 * @return the original [CharSequence] if it is blank.
 * @throws ValidationFailedException if the [CharSequence] is not blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateBlank(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isNotBlank()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence is blank. If the sequence is not blank, it throws
 * an exception with the provided details.
 *
 * @param property The property associated with the character sequence being validated (nullable).
 * @param variableName The name of the variable being validated (nullable).
 * @param message An optional custom message for the validation failure (nullable).
 * @param causeOf A supplier for a specific exception to be used as the root cause (nullable).
 * @param cause A supplier for an additional throwable to be used as the cause of the exception (nullable).
 * @return The original character sequence if it is blank.
 * @throws ValidationFailedException if the character sequence is not blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateBlank(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotBlank()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking CharSequence is blank. If the CharSequence is not blank,
 * it throws a `ValidationFailedException` with the specified parameters.
 *
 * @param property the primary property associated with the validation. This is used to provide context
 *                 in the error message when an exception is thrown, or null if not applicable.
 * @param variable an optional secondary property providing additional context for the validation,
 *                 or null if not applicable.
 * @param message an optional message describing the validation failure. Defaults to "is not blank" if not specified.
 * @param causeOf a supplier for an optional cause of the exception. If provided, it initializes the cause
 *                of the thrown exception.
 * @param cause a supplier for the direct underlying cause of the exception. If provided, it is set as the cause
 *              of the `ValidationFailedException` unless a non-null `causeOf` is specified.
 * @return the original CharSequence if it passes the validation (i.e., it is blank).
 * @throws ValidationFailedException if the CharSequence is not blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateBlank(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotBlank()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is blank. If the CharSequence is not blank,
 * a ValidationFailedException is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) associated with the validation. This can be null.
 * @param parameterName The name of the parameter being validated. This is used in the exception message if provided. Can be null.
 * @param message An optional custom message to detail the validation failure. Defaults to "is not blank" if not provided.
 * @param causeOf A supplier for the primary cause of the exception if any. Can be null.
 * @param cause A supplier for an optional underlying cause for the exception. Can be null.
 * @return The same CharSequence if it passes the validation (i.e., is blank).
 * @throws ValidationFailedException If the CharSequence is not blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateBlank(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotBlank()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given CharSequence is blank. If the CharSequence is not blank, this method throws a
 * [ValidationFailedException] with the provided details or invokes a supplied throwable as the cause.
 *
 * @param callable The callable (e.g., method or function) where the validation is being performed.
 * @param parameter The parameter related to the validation, used for error context.
 * @param message An optional custom validation failure message. Defaults to "is not blank" if not provided.
 * @param causeOf A supplier that provides the throwable to be used as the root cause of the exception if validation fails.
 * @param cause A supplier that provides an additional throwable cause to be associated with the exception if validation fails.
 * @return The same CharSequence instance, if the validation passes.
 * @throws ValidationFailedException If the CharSequence is not blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateBlank(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotBlank()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is blank. If the CharSequence is not blank, a
 * [ValidationFailedException] is thrown.
 *
 * @param callableName the name of the callable (function, method) where the validation occurs, used for error context.
 * @param parameterName the name of the parameter being validated, used for error context. Optional.
 * @param message the custom error message to include in the exception if validation fails. Optional.
 * @param causeOf a supplier for a custom Throwable cause to use for the exception if validation fails. Optional.
 * @param cause a supplier for an additional nested cause of the exception, used if [causeOf] is null. Optional.
 * @return the validated CharSequence itself if it is blank.
 * @throws ValidationFailedException if the CharSequence is not blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateBlank(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotBlank()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking [CharSequence] is blank. If it is not blank, a [ValidationFailedException] is thrown.
 *
 * @param callableName The name of the callable (e.g., function or property) where validation failed, or null if not specified.
 * @param parameter The [KParameter] instance representing the parameter that failed validation, or null if not applicable.
 * @param message An optional error message providing additional details about the validation failure. Defaults to "is not blank" if not provided.
 * @param causeOf A supplier for the cause of the failure (another [Throwable]), or null if not applicable.
 * @param cause A supplier for an additional underlying cause (another [Throwable]), or null if not applicable.
 * @return The invoking [CharSequence] if it is blank.
 * @throws ValidationFailedException if the [CharSequence] is not blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateBlank(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotBlank()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is either null or blank.
 *
 * This method ensures that a CharSequence meets a condition where it is either null
 * or contains only whitespace characters. If the condition is violated, an exception
 * is thrown, where additional details about the exception can be supplied through the
 * causeOf or cause parameters.
 *
 * @param causeOf an optional supplier for the overriding cause of the validation failure,
 *                which is used as the primary exception cause if provided.
 * @param cause an optional supplier for the underlying cause to be associated with
 *              the validation failure, used as a nested cause if provided.
 * @return the original CharSequence that passed the validation check (null or blank).
 *         If the CharSequence does not satisfy the validation condition, an exception is thrown.
 *         The return may be null or non-null based on the caller context.
 * @throws ValidationFailedException if the current CharSequence is not null and not blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrBlank(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrBlank != null)
    }
    if (isNotNullOrBlank) throw if (causeOf == null) ValidationFailedException("The char sequence is not null or blank.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The char sequence is not null or blank.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` is either `null` or blank.
 *
 * If the `CharSequence` is not `null` and not blank, an exception is thrown. The exception
 * message and cause can be customized using the provided suppliers.
 *
 * @param causeOf A supplier for the throwable that will be used as the primary exception. If `null`,
 *                a default `ValidationFailedException` is used.
 * @param cause A supplier for the cause of the exception, which will be set as the underlying cause
 *              of the primary exception. Can be `null`.
 * @param lazyMessage A supplier for the error message that will be included in the `ValidationFailedException`.
 * @return The current `CharSequence` if it is `null` or blank; otherwise, an exception is thrown.
 * @throws ValidationFailedException If the `CharSequence` is not `null` and not blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrBlank(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    contract {
        (this@validateNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrBlank != null)
    }
    if (isNotNullOrBlank) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the provided CharSequence is null or blank and throws a ValidationFailedException if it is not.
 *
 * If the CharSequence is not null and not blank, this method will raise a validation error. The validation error
 * message and optional cause details can be customized through the provided parameters.
 *
 * @param property The KProperty associated with the validation. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Used in the error message if not null.
 * @param message An optional custom validation error message. Defaults to a standard message if null.
 * @param causeOf A supplier for the root cause exception to be wrapped in the ValidationFailedException. Can be null.
 * @param cause A supplier for an additional throwable cause, if applicable. Can be null.
 * @return The original CharSequence input if it is null or blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrBlank(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrBlank != null)
    }
    if (isNotNullOrBlank) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not null or blank", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given [CharSequence] instance is null or blank. If the validation fails,
 * a [ValidationFailedException] is thrown. This method also allows specifying custom error messages
 * and exception suppliers for additional context or chaining of exceptions.
 *
 * @param property The primary property associated with the validation; can be null if not applicable.
 * @param variable The variable associated with the validation; can be null if not applicable.
 * @param message Optional custom error message to be used when the validation fails.
 * @param causeOf Optional supplier for an alternative exception to be thrown instead of default.
 * @param cause Optional supplier for the underlying cause of the exception.
 * @return Returns the original [CharSequence] instance if the validation passes.
 * @throws ValidationFailedException Thrown when the [CharSequence] is not null or blank, with
 * additional context based on the provided parameters.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrBlank(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrBlank != null)
    }
    if (isNotNullOrBlank) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not null or blank", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current CharSequence is null or blank and throws a ValidationFailedException if it is not.
 *
 * This function is useful for cases where null or blank values are acceptable,
 * but non-null or non-blank values must trigger a validation failure.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter in the given callable being validated. Can be null.
 * @param message An optional custom message to include in the ValidationFailedException when the validation fails. Can be null.
 * @param causeOf A supplier for the root cause of the ValidationFailedException. If provided, it generates the root cause exception. Can be null.
 * @param cause A supplier for an additional cause of the ValidationFailedException. If provided, it generates an additional layer of exception detail. Can be null.
 * @return The original CharSequence if it is null or blank; otherwise, this function throws a ValidationFailedException.
 *
 * @throws ValidationFailedException If the CharSequence is not null or blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrBlank(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrBlank != null)
    }
    if (isNotNullOrBlank) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not null or blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current nullable or blank CharSequence either meets the criteria of being null or blank,
 * and throws a [ValidationFailedException] if it is not null or blank.
 *
 * @param callable The [KFunction] related to the context of this validation, or `null` if not applicable.
 * @param parameter The [KParameter] that represents the parameter being validated, or `null` if not applicable.
 * @param message An optional message that provides additional details about the validation failure,
 *                defaulting to `null`.
 * @param causeOf The supplier function providing a root cause for the exception, or `null` if not applicable.
 * @param cause An optional supplier function supplying the throwable as the cause of the exception,
 *              or `null` if not applicable.
 * @return The original CharSequence if it meets the validation criteria (null or blank).
 * @throws ValidationFailedException If the CharSequence is not null and not blank.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrBlank(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrBlank != null)
    }
    if (isNotNullOrBlank) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not null or blank", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current CharSequence is either null or blank and throws a `ValidationFailedException`
 * if the validation fails. This can be used to ensure that optional strings or text-based parameters
 * meet the expected criteria, assisting in the validation and debugging process.
 *
 * @param callableName the name of the callable (e.g., function or method) for context in the exception.
 * @param parameterName the name of the parameter being validated, or null if not applicable.
 * @param message an optional custom message providing additional details for the exception.
 * @param causeOf a supplier for the primary cause of the exception, or null if not applicable.
 * @param cause a supplier for a secondary or underlying cause of the exception, or null if not applicable.
 * @return the validated CharSequence if it is null or blank, otherwise it throws an exception.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrBlank(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrBlank != null)
    }
    if (isNotNullOrBlank) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not null or blank", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence is null or blank.
 *
 * If the CharSequence is not null and not blank, a ValidationFailedException is thrown with the provided
 * details, including callable name, parameter, optional message, and optional causes.
 *
 * @param callableName The name of the callable where the validation is performed. May be null if not specified.
 * @param parameter The parameter of the callable being validated. May be null if not applicable.
 * @param message An optional error message that provides additional context for the validation failure. Defaults to null.
 * @param causeOf An optional supplier for the cause of the exception if the validation fails. Defaults to null.
 * @param cause An optional supplier that provides additional context for the underlying cause of the exception. Defaults to null.
 * @return The validated CharSequence if it passes validation (i.e., if it is null or blank).
 *         Throws a ValidationFailedException if validation fails.
 * @since 4.6.0
 */
@IgnorableReturnValue
fun <T : CharSequence?> T.validateNullOrBlank(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrBlank != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrBlank != null)
    }
    if (isNotNullOrBlank) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not null or blank", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not null or blank", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current char sequence contains the specified character. If the character
 * is not present, a [ValidationFailedException] is thrown.
 *
 * @param char The character to check for within the char sequence.
 * @param causeOf An optional transformer function to generate a customized throwable if validation fails.
 * @param cause An optional transformer function to generate a cause for the validation failure.
 * @return The original char sequence if the validation succeeds.
 * @throws ValidationFailedException If the character is not found in the char sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char !in this) throw if (causeOf == null) ValidationFailedException("$char is not in the char sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$char is not in the char sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence contains the specified character.
 * If the character is not found, a `ValidationFailedException` is thrown with an optional custom message and cause.
 *
 * @param char The character to check for in the character sequence.
 * @param causeOf An optional transformer for generating a throwable specified as the cause of the validation failure.
 * @param cause An optional transformer for generating a throwable linked as the root cause of the validation failure.
 * @param lazyMessage A transformer function to generate a custom error message when validation fails.
 * @return The original character sequence if validation succeeds.
 * @throws ValidationFailedException if the character is not found in the character sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (char !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling [CharSequence] contains the specified character. If the character is not found,
 * a [ValidationFailedException] is thrown with an optional custom message, variable name, and cause.
 *
 * @param char The character that this [CharSequence] must contain for validation to pass.
 * @param property The associated [KProperty] relevant to this validation, if any. Can be null.
 * @param variableName The name of the variable being validated. Can be null and is used for more descriptive exception messages.
 * @param message A custom error message to include in the exception. Defaults to a message indicating the missing character.
 * @param causeOf A transformer used to handle and produce a custom throwable instance related to the validation failure.
 * @param cause A transformer used to produce the underlying cause provided to the validation exception.
 * @return The calling [CharSequence] if it passes validation (contains the specified character).
 * @throws ValidationFailedException If the character is not found in the calling [CharSequence].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence contains the specified character.
 * If the character is not found, a ValidationFailedException is thrown.
 *
 * @param char the character to check for in the CharSequence
 * @param property the primary KProperty associated with the validation, or null if not specified
 * @param variable an optional secondary KProperty providing additional context, or null if not specified
 * @param message an optional custom error message to include in the exception, or null for a default message
 * @param causeOf an optional transformer that generates a specific Throwable as the root cause of the exception, or null if not used
 * @param cause an optional transformer invoked to provide a cause Throwable when the exception is constructed, or null if not used
 * @return the original CharSequence if validation succeeds
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character is contained within the current `CharSequence`. If the character is not
 * present, a `ValidationFailedException` is thrown.
 *
 * @param char The character that must be present in the `CharSequence`.
 * @param callable The function (`KFunction`) associated with the validation context. Can be null.
 * @param parameterName The name of the parameter associated with this validation. Can be null.
 * @param message An optional custom error message to include in the exception if validation fails. Can be null.
 * @param causeOf A transformer that produces the root cause of the validation failure as a `Throwable`. Can be null.
 * @param cause A transformer that provides a specific cause for the validation as a `Throwable`. Can be null.
 * @return The original `CharSequence` if validation passes successfully.
 * @throws ValidationFailedException If the character is not present in the `CharSequence`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given [CharSequence] contains the specified character. If the character is not found, a
 * [ValidationFailedException] is thrown with the provided details.
 *
 * @param char the character to validate for containment within the [CharSequence]
 * @param callable the [KFunction] relevant to the context of the validation, or null if not applicable
 * @param parameter the [KParameter] associated with the validation, or null if not applicable
 * @param message an optional error message to include in the exception, defaulting to a generic message
 * @param causeOf a transformer that generates the root cause of the exception, or null if none is provided
 * @param cause a transformer that provides an additional cause for the exception, or null if none is provided
 * @return the original [CharSequence] if the validation passes
 * @throws ValidationFailedException if the given character is not found in the [CharSequence]
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current character sequence contains the specified character.
 * If the character is not found, a `ValidationFailedException` is thrown.
 *
 * @param char The character to check for within the current sequence.
 * @param callableName The name of the callable (e.g., function or method) related to this validation, or null if not specified.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom validation message; if null, a default message is used.
 * @param causeOf An optional transformer for generating the root cause of the thrown exception based on the current sequence.
 * @param cause An optional transformer for creating a cause for the thrown exception based on the current sequence.
 * @return The original character sequence if validation succeeds.
 * @throws ValidationFailedException if the specified character is not found in the character sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the calling `CharSequence` contains the specified character.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param char The character to check for.
 * @param callableName The name of the callable where validation is being performed, or null if not specified.
 * @param parameter The `KParameter` associated with the validation, or null if not applicable.
 * @param message An optional validation failure message providing additional context, or null if none is specified.
 * @param causeOf An optional transformer that specifies the cause of the `ValidationFailedException`, or null if not provided.
 * @param cause An optional transformer to determine the root cause of the validation failure, or null if not provided.
 * @return The calling `CharSequence` if validation is successful.
 * @throws ValidationFailedException If the specified character is not found in the calling `CharSequence`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence contains the specified target character sequence.
 * Throws a [ValidationFailedException] if the condition is not met.
 *
 * @param cs The character sequence to check for within the current character sequence.
 * @param causeOf An optional transformer that generates a specific [Throwable] cause to be thrown
 *                if the validation fails, based on the current character sequence. Defaults to `null`.
 * @param cause An optional transformer that generates a generic [Throwable] cause to be thrown
 *              if the validation fails, based on the current character sequence. Defaults to `null`.
 * @return The original character sequence if the validation is successful.
 * @throws ValidationFailedException if the target character sequence is not found within the current character sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs !in this) throw if (causeOf == null) ValidationFailedException("$cs is not in the char sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$cs is not in the char sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence contains the specified substring. If the substring is not found,
 * a validation exception is thrown with an optional custom message and cause.
 *
 * @param cs The substring to check for within the current CharSequence.
 * @param causeOf An optional transformer to generate a specific Throwable object as the root cause
 *                of the validation failure.
 * @param cause An optional transformer to generate a Throwable object for additional context
 *              about the validation failure.
 * @param lazyMessage A transformer function used to generate the error message when the validation fails.
 * @return The original receiver CharSequence if validation is successful.
 * @throws ValidationFailedException if the specified substring is not found in the current CharSequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (cs !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current CharSequence contains the specified substring.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs The substring to check for within the current CharSequence.
 * @param property An optional Kotlin property metadata associated with the validation. Can be null if not applicable.
 * @param variableName The name of the variable involved in the validation. This is optional and can be null.
 * @param message A custom message to include in the exception if validation fails. Defaults to a message indicating
 *                that the current CharSequence does not contain the specified substring.
 * @param causeOf A transformer that can generate a specific `Throwable` to be thrown when validation fails. This is optional and can be null.
 * @param cause A transformer that can generate an additional underlying `Throwable` for the exception. This is optional and can be null.
 * @return The current CharSequence if the validation succeeds.
 * @throws ValidationFailedException If the current CharSequence does not contain the specified substring.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` contains the specified `cs` and throws a `ValidationFailedException` if the validation fails.
 *
 * @param cs The `CharSequence` that must be contained within the current instance.
 * @param property The primary `KProperty` associated with this validation. This provides metadata such as the property name or type for error reporting. May be null.
 * @param variable An optional secondary `KProperty` providing additional context for this validation. May be null.
 * @param message An optional custom error message to be used if validation fails. Defaults to a generated message if not provided.
 * @param causeOf An optional `Transformer` to dynamically generate the cause of the exception if validation fails, or null if not required.
 * @param cause An optional `Transformer` used to provide a custom cause for the exception. May be null.
 * @return The current `CharSequence` instance if validation passes.
 * @throws ValidationFailedException If the current instance does not contain the specified `cs`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence contains the specified substring. If the validation fails,
 * a `ValidationFailedException` is thrown.
 *
 * @param cs The substring that is expected to be present in the current CharSequence.
 * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can be `null`.
 * @param parameterName The name of the parameter in the given callable that caused the validation issue. Can be `null`.
 * @param message An optional custom error message to describe the validation failure. If `null`, a default message
 * will be used indicating the failure to contain the specified substring.
 * @param causeOf A transformer function that takes the current CharSequence and returns a `Throwable`
 * representing the cause of the validation failure. Can be `null`.
 * @param cause A transformer function that takes the current CharSequence and returns a `Throwable`
 * representing the underlying exception causing the validation failure. Can be `null`.
 * @return The same CharSequence instance if validation passes successfully.
 * @throws ValidationFailedException if the current CharSequence does not contain the specified substring.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current char sequence contains the specified fragment. If the validation fails,
 * a `ValidationFailedException` is thrown.
 *
 * @param cs The char sequence that must be contained within the current char sequence.
 * @param callable The [KFunction] associated with the validation failure, or null if not applicable.
 * @param parameter The [KParameter] representing the parameter involved in the validation failure, or null if not applicable.
 * @param message An optional custom message for the exception if validation fails. Defaults to null.
 * @param causeOf A transformer used to generate a throwable cause for the exception, or null if not applicable.
 * @param cause An additional transformer used to create a throwable cause for the exception, or null if not applicable.
 * @return The original instance of the char sequence if the validation passes.
 * @throws ValidationFailedException If the validation fails, with detailed information about the failure.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `CharSequence` contains the specified substring.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param cs the `CharSequence` that should be contained in the receiver
 * @param callableName the name of the function or callable performing the validation (optional)
 * @param parameterName the name of the parameter being validated (optional)
 * @param message an optional custom error message to include in the exception if validation fails
 * @param causeOf an optional transformation function to produce the root cause of the exception
 * @param cause an optional transformation function to produce an additional underlying cause for the exception
 * @return the current `CharSequence` instance if validation passes
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given character sequence contains the specified substring. If the substring
 * is not found, a `ValidationFailedException` is thrown.
 *
 * @param cs The substring to check for within the character sequence.
 * @param callableName The name of the callable (function or property) where validation is performed,
 *                     or null if not specified.
 * @param parameter The parameter being validated, represented as a `KParameter` instance, or null if not applicable.
 * @param message An optional custom message to provide additional details about the validation failure, or null for the default message.
 * @param causeOf An optional transformer that generates a `Throwable` based on the current instance, used as the primary cause of the failure.
 * @param cause An optional transformer that generates a supplementary `Throwable` based on the current instance.
 * @return The original character sequence if validation passes without throwing an exception.
 * @throws ValidationFailedException If the character sequence does not contain the specified substring.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` contains a match for the specified regular expression.
 * If no match is found, a `ValidationFailedException` is thrown.
 *
 * @param regex The regular expression to check for in the `CharSequence`.
 * @param causeOf A transformer function that generates a throwable cause based on the input `CharSequence`,
 *                or null if no custom cause generation is required.
 * @param cause A transformer function that provides an additional throwable cause based on the input `CharSequence`,
 *              or null if no additional cause is required.
 * @return The current `CharSequence` if the validation passes without exception.
 * @throws ValidationFailedException If the `regex` is not contained in the `CharSequence`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(regex: Regex, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex !in this) throw if (causeOf == null) ValidationFailedException("$regex is not in the char sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$regex is not in the char sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [CharSequence] contains a match for the specified [regex].
 * If no match is found, a [ValidationFailedException] is thrown. The exception message
 * and cause can be customized using the provided transformers.
 *
 * @param regex The regular expression to check for matches within the [CharSequence].
 * @param causeOf An optional transformer that creates a throwable to wrap the validation failure exception,
 *                providing additional context. If `null`, no wrapping is applied.
 * @param cause An optional transformer that generates a throwable to attach as the cause of the validation failure.
 *              If `null`, no cause is attached.
 * @param lazyMessage A transformer that generates a custom error message to include in the exception
 *                    when validation fails.
 * @return The original [CharSequence] if validation passes.
 * @throws ValidationFailedException If the given [regex] is not found within the [CharSequence].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(regex: Regex, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (regex !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` contains a match for the provided regular expression.
 * If the validation fails, an exception is thrown with a detailed message or optional cause.
 *
 * @param regex The regular expression to check for within this `CharSequence`.
 * @param property Optional property associated with the validation failure. Can be null if not applicable.
 * @param variableName The name of the variable being validated. Used in the error message if not null.
 * @param message Optional custom error message to describe the validation failure. Defaults to a standard message if null.
 * @param causeOf An optional transformer that generates a `Throwable` to represent the cause of the validation failure.
 *                If provided, it is used as the primary cause of the exception.
 * @param cause An optional transformer that generates a `Throwable` as the secondary cause of the validation failure.
 *              Used to set additional context for the exception.
 * @return The original `CharSequence` if it contains a match for the `regex`.
 * @throws ValidationFailedException if the `CharSequence` does not contain the provided `regex`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(regex: Regex, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence contains a match for the specified regular expression.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param regex The regular expression that the character sequence should contain.
 * @param property The primary `KProperty` associated with the validation, or null if not specified.
 * @param variable An optional secondary `KProperty` providing additional context, or null if not specified.
 * @param message An optional custom error message for the validation failure, or null to use the default message.
 * @param causeOf An optional transformer to generate a cause for the exception when the validation fails, or null if not specified.
 * @param cause An optional transformer to generate the root cause for the exception when the validation fails, or null if not specified.
 * @return The original character sequence if validation succeeds.
 * @throws ValidationFailedException if the character sequence does not contain a match for the specified regular expression.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(regex: Regex, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates that this CharSequence contains a match for the provided regular expression.
 * If no match is found, a `ValidationFailedException` is thrown.
 *
 * @param regex The regular expression to check for matches within this CharSequence.
 * @param callable The callable function (`KFunction`) related to the validation. Can be null.
 * @param parameterName The name of the parameter in the callable function to which the validation applies. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Can be null.
 * @param causeOf An optional transformer capable of generating a `Throwable` to use as the cause of the thrown exception if validation fails. Can be null.
 * @param cause An optional transformer capable of generating a `Throwable` to include as an additional cause for validation failure. Can be null.
 * @return The original CharSequence if the validation succeeds.
 * @throws ValidationFailedException If this CharSequence does not contain a match for the given regular expression.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(regex: Regex, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence contains a match for the specified regular expression.
 * If the validation fails, a [ValidationFailedException] is thrown with the provided context.
 *
 * @param regex the [Regex] to check for a match within the CharSequence
 * @param callable the [KFunction] associated with the validation, or null if not applicable
 * @param parameter the [KParameter] involved in the validation, or null if not applicable
 * @param message an optional message to include in the exception if validation fails
 * @param causeOf an optional transformer function to provide a specific exception as the cause of the validation failure
 * @param cause an optional transformer function to further specify the cause of the validation failure
 * @return the current CharSequence, if it passes the validation
 * @throws ValidationFailedException if the CharSequence does not contain a match for the specified regular expression
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(regex: Regex, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given [CharSequence] contains the specified [regex].
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param regex The regular expression to check for in the [CharSequence].
 * @param callableName The name of the function or method invoking the validation.
 * @param parameterName An optional parameter name related to the validation failure.
 * @param message An optional custom message providing additional details about the validation failure.
 * @param causeOf An optional transformer to specify the exception that will be thrown when validation fails, with an underlying cause initialized.
 * @param cause An optional transformer to create the underlying cause of the validation failure.
 * @return The original [CharSequence] if validation passes.
 * @throws ValidationFailedException if the [CharSequence] does not contain the [regex].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(regex: Regex, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence contains the specified regular expression.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param T The type of the character sequence to validate.
 * @param regex The regular expression that must be contained in the character sequence.
 * @param callableName The name of the callable (e.g., function or property) where the validation is performed,
 *        or `null` if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or `null` if not applicable.
 * @param message An optional message providing additional information about the validation failure, or `null` if not provided.
 * @param causeOf A transformer that generates a `Throwable` to be thrown when the validation fails, or `null` if not specified.
 * @param cause A transformer that generates an underlying `Throwable` cause for the validation failure, or `null` if not specified.
 * @return The validated character sequence if it contains the specified regular expression.
 * @throws ValidationFailedException If the character sequence does not contain the specified regular expression.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContains(regex: Regex, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given character is present in the character sequence, ignoring case.
 * If the character is not found, a [ValidationFailedException] is thrown.
 *
 * @param char the character to check for in the character sequence
 * @param causeOf optional transformer that generates a custom exception from the character sequence, used as the root cause of the validation failure
 * @param cause optional transformer that generates a custom exception from the character sequence, used as an additional cause for the validation failure
 * @return the original character sequence if the validation passes
 * @throws ValidationFailedException when the character is not present in the character sequence, ignoring case
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException("$char is not in the char sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$char is not in the char sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the character sequence contains the specified character, ignoring case.
 * If the validation fails, a `ValidationFailedException` is thrown with the provided message.
 *
 * @param char the character to validate for presence in the character sequence
 * @param causeOf an optional transformer function to generate a specific exception as the primary cause
 * @param cause an optional transformer function to produce a secondary underlying cause for the exception
 * @param lazyMessage a transformer function that generates the exception message lazily based on the character sequence
 * @return the original character sequence if validation succeeds
 * @throws ValidationFailedException if the character sequence does not contain the specified character, ignoring case
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (char notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the receiver character sequence contains the specified character, ignoring case.
 * Throws a `ValidationFailedException` if the character is not found.
 *
 * @param char The character to check for within the receiver string.
 * @param property An optional Kotlin property (`KProperty`) associated with the validation failure. Can be null.
 * @param variableName The optional name of the variable involved in validation. Used for error message context. Can be null.
 * @param message An optional custom validation failure message. Defaults to a generated message if null.
 * @param causeOf An optional transformer used to generate the underlying exception cause when validation fails. Can be null.
 * @param cause An optional transformer used to generate additional exception causes when validation fails. Can be null.
 * @return The unchanged receiver character sequence if the validation passes.
 * @throws ValidationFailedException If the character is not found in the character sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence contains a specified character, ignoring case.
 * If the character is not found, a validation exception is thrown.
 *
 * @param char the character to check for within the sequence
 * @param property the main property associated with the validation, or null if not applicable
 * @param variable an additional property providing context for the validation, or null if not applicable
 * @param message an optional validation failure message to include in the exception, or null for a default message
 * @param causeOf a custom transformer to produce the cause of the exception, or null if not specified
 * @param cause a secondary transformer to provide additional context for the exception, or null if not specified
 * @return the original character sequence if validation succeeds
 * @throws ValidationFailedException if the character is not found in the sequence, ignoring case
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified character sequence contains the given character, ignoring case.
 * If the character is not found, a validation exception is thrown.
 *
 * @param T The type of the CharSequence being validated.
 * @param char The character to search for in the character sequence.
 * @param callable The Kotlin function (`KFunction`) related to this validation. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message to provide additional details about the validation failure. Default is null.
 * @param causeOf A custom transformer to generate the root cause of the validation failure as a `Throwable`. Can be null.
 * @param cause A custom transformer to generate a wrapped `Throwable` for the exception. Can be null.
 * @return The original character sequence, if the validation succeeds.
 * @throws ValidationFailedException if the character is not found in the sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given character sequence contains the specified character, ignoring case.
 * If the character is not found, throws a [ValidationFailedException].
 *
 * @param char the character to validate for presence in the character sequence
 * @param callable the [KFunction] associated with the validation context, or null if not applicable
 * @param parameter the [KParameter] involved in the validation, or null if not applicable
 * @param message an optional custom message to include in the exception if validation fails; defaults to null
 * @param causeOf an optional transformer that generates the root cause of the failure, or null if not applicable
 * @param cause an optional transformer to provide additional cause details, or null if not applicable
 * @return the original character sequence if validation passes
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling character sequence contains the specified character, ignoring case.
 *
 * If the character is not present in the calling character sequence (case-insensitively), it throws a
 * [ValidationFailedException] with the provided details.
 *
 * @param char the character to verify is contained in the character sequence
 * @param callableName the name of the callable (e.g., function or method) associated with this validation
 * @param parameterName an optional name of the parameter being validated
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf an optional transformer function that can generate a throwable cause when validation fails
 * @param cause an optional direct transformer function to generate a throwable cause when validation fails
 * @return the original character sequence if validation succeeds
 * @throws ValidationFailedException if the character is not found in the character sequence, ignoring case
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given character sequence contains the specified character, ignoring case.
 * Throws a [ValidationFailedException] if the validation fails.
 *
 * @param char The character to check for within the character sequence.
 * @param callableName The name of the callable where the validation is being performed, or null if not specified.
 * @param parameter The parameter related to the validation, represented as a [KParameter], or null if not applicable.
 * @param message An optional error message to include in the exception if validation fails.
 * @param causeOf An optional transformer that generates the cause of the validation exception using the current object.
 * @param cause An optional transformer for providing additional cause details of the validation failure.
 * @return The validated character sequence if the validation succeeds.
 * @throws ValidationFailedException If the character sequence does not contain the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling character sequence contains the specified character sequence,
 * ignoring case considerations. If the validation fails, an exception is thrown.
 *
 * @param cs the character sequence to verify is contained within the calling character sequence.
 * @param causeOf an optional transformer that generates a custom throwable based on the calling character sequence
 * when the validation fails.
 * @param cause an optional transformer that generates an additional nested cause of the validation failure,
 * based on the calling character sequence.
 * @return the original character sequence if the validation passes.
 * @throws ValidationFailedException if the specified character sequence is not present in the calling character sequence,
 * ignoring case considerations.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException("$cs is not in the char sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$cs is not in the char sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence contains the specified character sequence,
 * ignoring case considerations. If the condition is not met, a validation exception is thrown.
 *
 * @param cs the character sequence to check if it is contained within the current sequence, ignoring case.
 * @param causeOf an optional transformer to generate a specific throwable if the validation fails.
 * @param cause an optional transformer to specify the cause of the throwable if the validation fails.
 * @param lazyMessage a transformer function to generate the error message to be associated with the validation failure.
 * @return the current character sequence if the validation is successful.
 * @throws ValidationFailedException if the current character sequence does not contain the specified sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (cs notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current character sequence contains the specified character sequence, ignoring case considerations.
 * If the validation fails, a `ValidationFailedException` is thrown with the provided parameters.
 *
 * @param cs The character sequence to check for within the current character sequence.
 * @param property An optional property associated with the validation.
 * @param variableName An optional name of the variable involved in the validation.
 *                     Used for constructing a more descriptive error message if provided.
 * @param message An optional custom validation error message. If not provided, a default message is used.
 * @param causeOf An optional transformer function for generating the root cause throwable.
 * @param cause An optional transformer function for generating the exception cause.
 * @return The current character sequence if it contains the specified character sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence contains the specified substring, ignoring case considerations.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs the substring to search for within the calling character sequence.
 * @param property the main property associated with the validation, or null if not provided.
 * @param variable an optional secondary property that provides additional context, or null if not provided.
 * @param message an optional custom message that describes the validation failure, or null to use a default message.
 * @param causeOf an optional transformer to specify a custom throwable when the validation fails, or null to use the default exception.
 * @param cause an optional transformer for the root cause of the exception, or null if no underlying cause is set.
 * @return the original character sequence if the validation passes.
 * @throws ValidationFailedException if the calling character sequence does not contain the specified substring, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current character sequence contains the specified character sequence, ignoring case considerations.
 * If the validation fails, it throws a `ValidationFailedException`.
 *
 * @param cs The character sequence to check for within the current character sequence.
 * @param callable The Kotlin function (`KFunction`) related to this validation. This can be used for debugging or indicating the source of validation.
 * @param parameterName The name of the parameter in the related callable that failed validation. Can be null.
 * @param message An optional custom message to include in the `ValidationFailedException` when validation fails.
 * @param causeOf An optional transformer that generates a `Throwable` from the current character sequence. Used as the cause for the thrown exception when provided.
 * @param cause An optional transformer that generates a `Throwable` from the current character sequence. Used as a cause in the absence of `causeOf`.
 * @return The current character sequence if validation is successful.
 * @throws ValidationFailedException if the specified character sequence is not found within the current character sequence, ignoring case considerations.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current character sequence contains the specified character sequence, ignoring case considerations.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs the character sequence that is expected to be contained within the current character sequence.
 * @param callable an optional [KFunction] associated with the validation, used for context in error reporting.
 * @param parameter an optional [KParameter] associated with the validation, used for context in error reporting.
 * @param message an optional custom message to include in the exception if validation fails. Defaults to `null`.
 * @param causeOf an optional transformer providing a custom throwable to use as the cause of the exception.
 * @param cause an optional transformer providing a custom throwable to include in the exception.
 * @return the current character sequence if validation passes.
 * @throws ValidationFailedException if the specified character sequence is not found within the current character sequence (case-insensitive).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence contains the specified character sequence, ignoring case.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param cs the character sequence that must be contained in the current character sequence.
 * @param callableName the name of the callable (e.g., function or method) related to the validation.
 * @param parameterName the name of the parameter being validated (optional).
 * @param message a custom validation failure message (optional).
 * @param causeOf a transformer providing the root cause of the validation failure (optional).
 * @param cause a transformer providing additional context for the validation failure (optional).
 * @return the current character sequence if the validation passes.
 * @throws ValidationFailedException if the current character sequence does not contain the specified sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence contains the specified character sequence (case-insensitively).
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs The character sequence that must exist within this character sequence (case-insensitively).
 * @param callableName The name of the callable where this validation logic is invoked, or null if not applicable.
 * @param parameter The KParameter instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message to include in the exception if validation fails.
 * @param causeOf An optional transformer that may produce a throwable to be used as the primary cause of the exception if validation fails.
 * @param cause An optional transformer that may produce a throwable to chain as the underlying cause in case of validation failure.
 * @return The current character sequence if the validation succeeds.
 * @throws ValidationFailedException If the current character sequence does not contain the specified character sequence (case-insensitively).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateContainsIgnoreCase(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs notInIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified character.
 * If the character is found within the sequence, a validation exception is thrown.
 *
 * @param char The character to check for exclusion from the sequence.
 * @param causeOf A transformer function that generates a throwable cause if the validation fails.
 * @param cause An optional transformer function to provide an additional cause for the validation failure.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException if the specified character is found in the character sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char in this) throw if (causeOf == null) ValidationFailedException("$char is in the char sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$char is in the char sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified character.
 * If the character is found, a `ValidationFailedException` is thrown.
 *
 * @param char The character that must not be contained in the character sequence.
 * @param causeOf A transformer to generate the cause of the exception. If provided and not null,
 *                it is invoked to produce the throwable to be thrown. Defaults to null.
 * @param cause A transformer to generate an additional underlying throwable cause
 *              to be included in the `ValidationFailedException`. Defaults to null.
 * @param lazyMessage A transformer that generates a message describing the validation failure,
 *                    which will be included in the exception if thrown.
 * @return The same character sequence if it does not contain the specified character.
 * @throws ValidationFailedException If the character is found within the character sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (char in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` does not contain the specified character.
 * Throws a `ValidationFailedException` if the character is found.
 *
 * @param char The character that must not be contained within the `CharSequence`.
 * @param property The optional property related to the validation. Used to provide context for the validation failure.
 * @param variableName The optional variable name to be included in the validation error message for additional context.
 * @param message An optional custom error message to include in the exception if validation fails.
 *                Defaults to a message indicating the character that was found.
 * @param causeOf An optional transformer that produces a throwable based on the current `CharSequence`
 *                when the character is contained, and is used as the primary root cause of failure.
 * @param cause A secondary optional transformer that produces a throwable based on the current `CharSequence`
 *              when the character is contained, and is used as an additional root cause of failure.
 * @return The original `CharSequence` if the character is not found.
 * @throws ValidationFailedException if the `CharSequence` contains the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not contain the specified character.
 * If the character is found in the sequence, a `ValidationFailedException` is thrown.
 *
 * @param char The character to validate against.
 * @param property The primary KProperty associated with the validation, providing contextual information, or null if not specified.
 * @param variable An optional secondary KProperty offering additional validation context, or null if not specified.
 * @param message An optional error message to be included in the exception if validation fails, or null for a default message.
 * @param causeOf An optional transformer to generate a specific throwable cause for the exception, or null if not used.
 * @param cause An optional transformer to create the base cause for the exception, or null if not required.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException if the character sequence contains the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking CharSequence does not contain the specified character.
 * If the character is found, a validation error is thrown.
 *
 * @param char The character to check for within the CharSequence.
 * @param callable The Kotlin function (`KFunction`) related to the validation. Can be null.
 * @param parameterName The name of the parameter in the function associated with the validation. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Can be null.
 * @param causeOf A transformer that generates a throwable cause when validation fails. Can be null.
 * @param cause An additional transformer for generating a throwable cause when validation fails. Can be null.
 * @return The original CharSequence if it passes validation.
 * @throws ValidationFailedException if the CharSequence contains the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking CharSequence does not contain the specified character.
 *
 * If the character is found within the CharSequence, a [ValidationFailedException] is thrown,
 * optionally with a message and/or caused by a provided transformer function.
 *
 * @param char The character to check for within the CharSequence.
 * @param callable The [KFunction] related to the validation, or null if not applicable.
 * @param parameter The [KParameter] representing the parameter involved in the validation, or null if not applicable.
 * @param message An optional error message to include in the exception if validation fails.
 * @param causeOf An optional transformer function to generate a [Throwable] if validation fails, instead of using a default exception.
 * @param cause An optional transformer function to generate the underlying cause of the exception, if provided.
 * @return The original CharSequence if validation succeeds, allowing for method chaining.
 * @throws ValidationFailedException If the character is found within the invoking CharSequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character is not contained within the current `CharSequence`.
 * If the character is found, a `ValidationFailedException` is thrown.
 *
 * @param char The character to check for within the `CharSequence`.
 * @param callableName The name of the callable (e.g., function or method) initiating the validation, or null if unspecified.
 * @param parameterName The name of the parameter being validated, or null if unspecified.
 * @param message An optional custom message providing additional context for the validation failure, or null if using a default message.
 * @param causeOf A transformer that generates a specific exception as the cause for the thrown `ValidationFailedException`, or null if no such transformer is provided.
 * @param cause A transformer that generates a general cause for the thrown `ValidationFailedException`, or null if no such transformer is provided.
 * @return The original `CharSequence` if validation passes without throwing an exception.
 * @throws ValidationFailedException If the character is found in the `CharSequence`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` does not contain the specified character.
 * Throws a `ValidationFailedException` if the character is found within the `CharSequence`.
 *
 * @param char The character to check for within the `CharSequence`.
 * @param callableName The name of the callable (e.g., function or property) associated with this validation, or null if not specified.
 * @param parameter The `KParameter` representing the parameter that is being validated, or null if not applicable.
 * @param message An optional custom error message for the validation failure. If not provided, a default message will be used.
 * @param causeOf An optional transformer to generate the underlying exception that triggered the validation.
 * @param cause An optional transformer to generate the root cause exception for the validation failure.
 * @return The current `CharSequence` if validation succeeds.
 * @throws ValidationFailedException If the `CharSequence` contains the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking CharSequence does not contain the given `cs` value.
 * If the `cs` value is found within the invoking CharSequence, a `ValidationFailedException`
 * is thrown.
 *
 * @param cs The CharSequence to validate against the invoking CharSequence. If this is found within the receiver, an exception is thrown.
 * @param causeOf Optional transformer that can generate a custom Throwable to be thrown if validation fails. If null, a default exception is used.
 * @param cause Optional transformer that provides a cause Throwable to be associated with the exception. If null, no cause Throwable is associated.
 * @return The original CharSequence, if validation passes.
 * @throws ValidationFailedException If the `cs` value is present within the invoking CharSequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs in this) throw if (causeOf == null) ValidationFailedException("$cs is in the char sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$cs is in the char sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified character sequence.
 * Throws a [ValidationFailedException] with the provided lazy message and optional cause transformers if the validation fails.
 *
 * @param cs The character sequence that must not be contained in this character sequence.
 * @param causeOf Optional transformer used to generate a throwable cause based on the current character sequence when the validation fails.
 * @param cause Optional transformer used to generate a specific throwable cause based on the current character sequence.
 * @param lazyMessage A transformer used to dynamically generate an error message if the validation fails.
 * @return The current character sequence if the validation passes.
 * @throws ValidationFailedException If this character sequence contains the specified character sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (cs in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the input sequence does not contain the specified substring. If the specified substring
 * is found, a `ValidationFailedException` is thrown.
 *
 * @param cs The substring to check for within the input sequence.
 * @param property The property associated with the validation, or null if not applicable.
 * @param variableName Optional name of the variable being validated. Defaults to null.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to a generic message.
 * @param causeOf An optional transformation function to generate the root cause exception if validation fails. Defaults to null.
 * @param cause An optional transformation function to generate a supplementary cause exception, chained to the root cause. Defaults to null.
 * @return The original sequence if validation succeeds.
 * @throws ValidationFailedException if the input sequence contains the specified substring.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence does not contain the specified sequence of characters.
 * If the condition is violated, a ValidationFailedException is thrown.
 *
 * @param cs the CharSequence to validate against; the method will throw an exception if this sequence is found
 * @param property the primary property associated with this validation, providing additional context; can be null
 * @param variable an optional secondary property used for additional context during validation; can be null
 * @param message an optional message to include in the exception if the validation fails
 * @param causeOf an optional transformer function that takes the current CharSequence and produces a Throwable cause; can be null
 * @param cause an optional transformer function that generates a Throwable cause based on the current CharSequence; can be null
 * @return the original CharSequence if validation succeeds
 * @throws ValidationFailedException if the specified CharSequence is found within the current CharSequence
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` does not contain the specified `cs` (substring).
 * If the current `CharSequence` contains the specified `cs`, a `ValidationFailedException` is thrown.
 *
 * @param T The type of the current `CharSequence`.
 * @param cs The substring to check for. If the current `CharSequence` contains this substring, validation fails.
 * @param callable The Kotlin function (`KFunction`) to which this validation is related. Can be null.
 * @param parameterName The name of the parameter in the callable related to the validation. Can be null.
 * @param message An optional custom message to include in the exception on validation failure. Default is null.
 * @param causeOf A transformer function to handle additional causes for the exception. Can be null.
 * @param cause A transformer function that provides the underlying cause for the exception. Can be null.
 * @return The current `CharSequence` if validation passes.
 * @throws ValidationFailedException if the current `CharSequence` contains the specified `cs`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [CharSequence] does not contain the specified substring [cs].
 * If the substring is found, a [ValidationFailedException] is thrown.
 *
 * @param cs the [CharSequence] to check for in the current sequence.
 * @param callable the callable ([KFunction]) associated with this validation, or null if not applicable.
 * @param parameter the parameter ([KParameter]) associated with this validation, or null if not applicable.
 * @param message an optional custom message to include in the exception if validation fails, defaults to null.
 * @param causeOf an optional transformer for generating a specific [Throwable] to throw if validation fails, defaults to null.
 * @param cause an optional transformer for deriving the cause of the [ValidationFailedException], defaults to null.
 * @return the current [CharSequence] if the substring [cs] is not found.
 * @throws ValidationFailedException if the current sequence contains the specified substring [cs].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain a specified substring.
 * Throws a [ValidationFailedException] if the validation fails.
 *
 * @param cs the substring that must not be contained in the current character sequence
 * @param callableName the name of the callable (e.g., function or method) where the validation is performed
 * @param parameterName the name of the parameter being validated, optional
 * @param message an optional custom message to include with the exception
 * @param causeOf a transformer to provide a custom throwable for the validation failure, optional
 * @param cause an additional transformer to provide a custom underlying throwable cause, optional
 * @return the current character sequence if validation passes
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` does not contain the specified `CharSequence`.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs The `CharSequence` that must not be contained within the current `CharSequence`.
 * @param callableName Optional name of the callable (e.g., function or property) that triggers the validation.
 * @param parameter Optional parameter instance representing the parameter being validated.
 * @param message Optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer for providing the cause of the validation failure as a `Throwable`.
 * @param cause An optional transformer for directly providing a `Throwable` as the cause of the exception.
 * @return The current `CharSequence` if the validation passes.
 * @throws ValidationFailedException if the current `CharSequence` contains the specified `CharSequence`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not contain any match for the provided regular expression.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param regex The regular expression to check against the character sequence.
 * @param causeOf An optional transformer to construct a throwable from the input if validation fails.
 * @param cause An optional transformer to provide the cause for the exception if validation fails.
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException if the character sequence contains a match for the given regular expression.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(regex: Regex, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex in this) throw if (causeOf == null) ValidationFailedException("$regex is in the char sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$regex is in the char sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence does not contain a match for the provided regular expression.
 * If a match is found, a ValidationFailedException is thrown.
 *
 * @param regex The regular expression to check for within the current CharSequence.
 * @param causeOf Optional transformer to create a Throwable exception as the primary cause of the validation failure.
 * @param cause Optional transformer to create a Throwable exception to be set as the cause when the ValidationFailedException is thrown.
 * @param lazyMessage A transformer function that generates a message when validation fails.
 * @return The current CharSequence if it does not contain a match for the provided regular expression.
 * @throws ValidationFailedException if the current CharSequence contains a match for the provided regular expression.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(regex: Regex, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (regex in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain a match for the given regular expression.
 * If a match is found, a `ValidationFailedException` is thrown.
 *
 * @param regex The regular expression that the character sequence should not match.
 * @param property An optional property associated with the validation. Can be null if not applicable.
 * @param variableName The name of the variable involved in the validation. Can be null if not applicable.
 * @param message An optional custom message to describe the validation failure. Defaults to a message indicating that the sequence contains the pattern.
 * @param causeOf An optional transformer to generate the root cause of the exception when the validation fails. Can be null if not applicable.
 * @param cause An optional transformer to generate a cause for the exception when the validation fails. Can be null if not applicable.
 * @return Returns the original character sequence if the validation passes.
 * @throws ValidationFailedException if the character sequence contains a match for the provided regular expression.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(regex: Regex, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` does not contain a pattern matching the provided `Regex`.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param regex the regular expression to check against the current `CharSequence`
 * @param property the main property associated with the validation, or null if not specified
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional error message to include in the exception if validation fails
 * @param causeOf an optional transformer that builds the cause exception linked to the validation failure
 * @param cause an optional transformer that provides a specific cause for the validation failure
 * @return the original `CharSequence` if the validation succeeds
 * @throws ValidationFailedException if the `CharSequence` contains a match of the given `Regex`
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(regex: Regex, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence does not contain the specified regex pattern.
 * If the validation fails, an exception is thrown.
 *
 * @param regex The regular expression that should not be contained in the current CharSequence.
 * @param callable The Kotlin function (`KFunction<*>`) where the validation is being performed. Can be null.
 * @param parameterName The name of the parameter being validated, if applicable. Can be null.
 * @param message An optional custom error message to include in the exception. Can be null.
 * @param causeOf A transformer function that produces a derived `Throwable` for the validation failure cause. Can be null.
 * @param cause A transformer function generating a `Throwable` to attach as the cause of the validation failure. Can be null.
 * @return The original CharSequence if it passes validation.
 * @throws ValidationFailedException if the current CharSequence contains the provided regex.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(regex: Regex, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [CharSequence] does not contain the specified [regex]. If the validation fails,
 * a [ValidationFailedException] is thrown.
 *
 * @param T The type of the [CharSequence].
 * @param regex The [Regex] pattern to check against the current [CharSequence].
 * @param callable An optional [KFunction] representing the function involved in the validation.
 * @param parameter An optional [KParameter] representing the parameter involved in the validation.
 * @param message An optional custom message to include in the exception if the validation fails. Defaults to null.
 * @param causeOf An optional [Transformer] to generate a throwable cause before the validation exception is thrown.
 * @param cause An optional [Transformer] to generate a throwable cause to provide additional context in the exception.
 * @return The original [CharSequence] if the validation passes.
 * @throws ValidationFailedException If the [CharSequence] contains the specified [regex].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(regex: Regex, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified regular expression.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param regex the regular expression to check for within the character sequence
 * @param callableName the name of the callable (e.g., function or method) triggering the validation
 * @param parameterName the name of the parameter being validated; defaults to `null` if not provided
 * @param message an optional custom error message to include in the exception; defaults to `null`
 * @param causeOf an optional transformer to generate a root cause exception if the validation fails; defaults to `null`
 * @param cause an optional transformer to generate an additional cause of the exception; defaults to `null`
 * @return the original character sequence if validation passes
 * @throws ValidationFailedException if the character sequence contains the specified regular expression
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(regex: Regex, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given `CharSequence` does not contain a match for the specified regular expression.
 * If a match is found, a `ValidationFailedException` is thrown.
 *
 * @param regex The regular expression to check against the `CharSequence`.
 * @param callableName The name of the callable (e.g., function or property) where this validation is being performed,
 *                     or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter to validate, or null if not applicable.
 * @param message An optional error message to include in the exception if validation fails, defaulting to a message
 *                indicating the presence of the regular expression.
 * @param causeOf An optional transformer to generate a `Throwable` to represent the root cause of the validation failure,
 *                or null if not needed.
 * @param cause An optional transformer to generate a `Throwable` to attach as the cause of the `ValidationFailedException`,
 *              or null if not needed.
 * @return The original `CharSequence` if validation succeeds without throwing an exception.
 * @throws ValidationFailedException If the `CharSequence` contains a match for the given regular expression.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContains(regex: Regex, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (regex in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $regex", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $regex", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not contain the specified character, ignoring case.
 * If the character is found in the sequence, a ValidationFailedException is thrown.
 *
 * @param char the character to check for exclusion from the character sequence
 * @param causeOf a transformer function that, when provided, generates the exception to be thrown based on the context
 * @param cause an optional transformer function that provides an underlying cause for the exception
 * @return the original character sequence if validation passes
 * @throws ValidationFailedException if the specified character is found in the character sequence
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char inIgnoreCase this) throw if (causeOf == null) ValidationFailedException("$char is in the char sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$char is in the char sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not contain the specified character, ignoring case.
 * If the character is found in the sequence, an exception is thrown using the provided transformers
 * and lazy message to construct the error details.
 *
 * @param char the character to check for within the character sequence.
 * @param causeOf an optional transformer that generates a custom exception based on the character sequence; may be null.
 * @param cause an optional transformer that provides the root cause for the exception based on the character sequence; may be null.
 * @param lazyMessage a transformer that generates the exception message based on the character sequence.
 * @return the original character sequence if validation passes.
 * @throws ValidationFailedException if the specified character is found in the sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (char inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not contain the specified character, ignoring case.
 * If the character is found, throws a `ValidationFailedException` with the provided parameters to describe the failure.
 *
 * @param char The character to check for, ignoring case.
 * @param property An optional KProperty reference associated with the validation. This is used to enhance error details.
 * @param variableName An optional variable name for more descriptive error messages. Defaults to null if not provided.
 * @param message An optional custom validation failure message. If not provided, a default message is generated.
 * @param causeOf An optional transformer to produce a custom underlying throwable cause based on the input value.
 * @param cause An optional transformer to produce a direct throwable cause for the failure based on the input value.
 * @return The same character sequence if validation is successful.
 * @throws ValidationFailedException If the specified character is found in the input character sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the provided character sequence does not contain the specified character, ignoring case.
 * If the character is found, a [ValidationFailedException] is thrown.
 *
 * @param char the character to check for absence in this character sequence
 * @param property the primary property associated with the validation, or `null` if not applicable
 * @param variable an optional secondary property providing additional context, or `null` if not applicable
 * @param message an optional message providing additional details about the validation failure
 * @param causeOf an optional transformer for creating the cause of the validation failure exception, or `null`
 * @param cause an optional transformer invoked to provide the root cause of the exception, or `null`
 * @return the original character sequence if validation passes
 * @throws ValidationFailedException if the specified character is found in the character sequence, ignoring case
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` does not contain the specified character, ignoring case.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character to check for within the `CharSequence`.
 * @param callable The function to which this validation is related. Can be null.
 * @param parameterName The name of the parameter associated with this validation, if applicable. Can be null.
 * @param message An optional custom error message to include in the exception if validation fails. Can be null.
 * @param causeOf A transformer function to create a `Throwable` cause for the exception, based on the current `CharSequence`. Can be null.
 * @param cause An optional transformer function to generate a fallback `Throwable` cause if `causeOf` is null. Can be null.
 * @return The original `CharSequence` if validation succeeds.
 * @throws ValidationFailedException If the validation fails because the `CharSequence` contains the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified character, ignoring case.
 * If the character is found, a [ValidationFailedException] is thrown.
 *
 * @param char the character to check for absence in the current character sequence
 * @param callable the [KFunction] associated with this validation, or null if not applicable
 * @param parameter the [KParameter] representing the parameter involved in the validation, or null if not applicable
 * @param message an optional custom validation failure message, or null for the default message
 * @param causeOf an optional transformer to produce a custom exception based on the current character sequence
 * @param cause an optional transformer to specify an underlying cause for the exception
 * @return the current character sequence if validation is successful
 * @throws ValidationFailedException if the specified character is found in the current character sequence
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that a character sequence does not contain the specified character, ignoring case.
 * If the character is found, a `ValidationFailedException` is thrown with the provided details.
 *
 * @param char the character to check for, ignoring case
 * @param callableName the name of the callable (e.g., function or method) triggering the validation
 * @param parameterName the name of the parameter being validated (optional)
 * @param message an optional custom exception message to provide additional context
 * @param causeOf an optional transformation to generate a `Throwable` as the root cause of the exception
 * @param cause an optional transformation to generate a secondary `Throwable` for exception chaining
 * @return this character sequence if validation passes without throwing an exception
 * @throws ValidationFailedException if the character sequence contains the specified character, ignoring case
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not contain the specified character, ignoring case.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character to check for the absence of in the character sequence.
 * @param callableName The name of the callable (e.g., function or property) where validation is being performed, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message to be included in the exception if validation fails, or null to use a default message.
 * @param causeOf An optional transformer function that generates the root cause exception for the failure, or null if no specific cause is provided.
 * @param cause An optional transformer function that generates an exception to be chained as the cause of the `ValidationFailedException`, or null if no specific cause is required
 * .
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException If the character sequence contains the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (char inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains char $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains char $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified character sequence,
 * ignoring case considerations. If validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs The character sequence that should not be present in the current character sequence.
 * @param causeOf An optional transformer to provide a throwable for the `ValidationFailedException`
 *                based on the current character sequence. Defaults to `null`.
 * @param cause An optional transformer to provide an underlying cause for the `ValidationFailedException`
 *              based on the current character sequence. Defaults to `null`.
 * @return The original character sequence, if the validation passes successfully.
 * @throws ValidationFailedException If the specified character sequence is found in the current
 *                                    character sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs inIgnoreCase this) throw if (causeOf == null) ValidationFailedException("$cs is in the char sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$cs is in the char sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified character sequence,
 * ignoring case considerations. If the specified character sequence exists, an exception is thrown.
 *
 * @param cs The character sequence to check for in the current sequence.
 * @param causeOf An optional transformer that provides a throwable as the cause of the validation failure.
 *                If specified and the validation fails, its result is used as the primary cause of the exception.
 * @param cause An optional transformer that provides a throwable to be included in the exception. If provided,
 *              its result is associated with the exception as an additional cause.
 * @param lazyMessage A transformer that generates a custom error message when the validation fails.
 * @return The current character sequence if the validation passes.
 * @throws ValidationFailedException If the specified character sequence is found, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (cs inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified [cs],
 * ignoring case considerations. If the validation fails, it throws a [ValidationFailedException].
 *
 * @param cs The character sequence to check for the absence of, ignoring case.
 * @param property The property associated with the validation. Can be null if not relevant.
 * @param variableName Optional name of the variable being validated. Used in the exception message if validation fails.
 * @param message Custom error message to include if the validation fails. Defaults to a generated message.
 * @param causeOf Optional transformer for creating the root cause of the exception, invoked only when validation fails.
 * @param cause Optional transformer for appending additional context to the exception, invoked only when validation fails.
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException If [cs] is found in the current character sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified character sequence, ignoring case.
 *
 * If the specified character sequence is found within the current sequence, a `ValidationFailedException` is thrown.
 * The exception can also include optional metadata such as property references and a custom message.
 *
 * @param cs the character sequence to check for absence, ignoring case.
 * @param property an optional primary `KProperty` providing context about the validation, or null if not specified.
 * @param variable an optional secondary `KProperty` providing additional context, or null if not specified.
 * @param message an optional custom message to include in the exception if validation fails, or null if not provided.
 * @param causeOf an optional transformer to generate the underlying cause of the exception based on the current value, or null if not provided.
 * @param cause an optional transformer to generate an additional cause to wrap within the exception, or null if not provided.
 * @return the original character sequence if validation succeeds.
 * @throws ValidationFailedException if the specified character sequence is present in the current sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified character sequence,
 * ignoring case considerations. If the validation fails, an exception is thrown.
 *
 * @param cs The character sequence that should not be contained within the current character sequence.
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName An optional name of the parameter that is being validated. Can be null.
 * @param message A custom error message for the exception if validation fails. Can be null.
 * @param causeOf A transformer to generate the root cause of the exception, if validation fails. Can be null.
 * @param cause A transformer to generate the cause of the exception, if validation fails. Can be null.
 * @return The validated character sequence if the validation passes.
 * @throws ValidationFailedException If the current character sequence contains the specified one, ignoring case, the exception is
 * thrown with an optional custom message or cause.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not contain the specified substring, ignoring case considerations.
 *
 * If the validation fails, a [ValidationFailedException] will be thrown with details about the callable, parameter,
 * an optional message, and cause information.
 *
 * @param cs the character sequence to check against to ensure it is not contained, ignoring case.
 * @param callable the [KFunction] related to the validation context, or null if not applicable.
 * @param parameter the [KParameter] associated with the validation context, or null if not applicable.
 * @param message an optional error message to include in the exception if the validation fails.
 * @param causeOf an optional transformer function for generating the root cause of the validation failure.
 * @param cause an optional transformer function for generating a cause for validation failure.
 * @return the original character sequence if validation is successful.
 * @throws ValidationFailedException if the character sequence contains the specified substring, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified character sequence,
 * ignoring case considerations. If the validation fails, an exception is thrown.
 *
 * @param cs the character sequence to check against the current character sequence.
 * @param callableName an optional name of the function or method performing the validation.
 * @param parameterName an optional name of the parameter that is being validated.
 * @param message an optional custom message to include in the exception if validation fails.
 * @param causeOf an optional transformer to generate a specific exception based on the validation failure.
 * @param cause an optional transformer to specify the cause of the exception.
 * @return the original character sequence if validation succeeds.
 * @throws ValidationFailedException if the current character sequence contains the specified character sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not contain the specified character sequence,
 * ignoring case considerations. If the condition is violated, a `ValidationFailedException` is thrown.
 *
 * @param cs The character sequence that must not be present in the current character sequence.
 * @param callableName The name of the callable (e.g., function or property) where the validation is occurring, or null if not specified.
 * @param parameter The parameter being validated, represented as a `KParameter` instance, or null if not applicable.
 * @param message An optional error message providing additional context about the validation. Defaults to null.
 * @param causeOf An optional `Transformer` defining how to produce a throwable cause for the validation failure. Defaults to null.
 * @param cause An optional `Transformer` defining the throwable cause for the validation failure, or null if no cause is provided.
 * @return The validated character sequence if the condition is satisfied.
 * @throws ValidationFailedException If the validation fails because the specified character sequence is found (case-insensitive).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotContainsIgnoreCase(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (cs inIgnoreCase this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $cs", cause?.invoke(this)))
    return this
}

/**
 * Validates that the character sequence starts with the specified character.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param char The character that the sequence should start with.
 * @param causeOf An optional transformer to provide a custom exception when validation fails.
 * @param cause An optional transformer to provide a nested cause for the exception.
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException If the character sequence does not start with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith char) throw if (causeOf == null) ValidationFailedException("Char sequence doesn't start with $char.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence doesn't start with $char.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence starts with the specified character.
 * If the validation fails, an exception is thrown based on the provided transformers.
 *
 * @param char The character to check against the start of the character sequence.
 * @param causeOf An optional transformer to generate a specific exception based on the character sequence. Default is `null`.
 * @param cause An optional transformer to generate the cause of the exception based on the character sequence. Default is `null`.
 * @param lazyMessage A transformer to create a lazy-loaded message for the exception in case of validation failure.
 * @return The original character sequence if it passes the validation.
 * @throws ValidationFailedException If the character sequence does not start with the specified character
 *         and `causeOf` is not provided, or if `causeOf` generates an exception.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this notStartsWith char) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence starts with the specified character.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param char The character to validate the start of the character sequence against.
 * @param property The property associated with the validation failure, if applicable. Can be null.
 * @param variableName Optional name of the variable being validated. Used in the exception message if provided.
 * @param message Optional custom message describing the validation failure. Defaults to a generated message.
 * @param causeOf A transformer that generates the primary exception to be thrown. Can be null.
 * @param cause A transformer that generates the underlying cause of the exception. Can be null.
 * @return The original character sequence if it passes validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith char) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence starts with the specified character.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param char The character that the character sequence is expected to start with.
 * @param property The primary [KProperty] associated with the validation failure, or `null` if not specified.
 * @param variable An optional secondary [KProperty] that provides additional context for the validation failure, or `null` if not specified.
 * @param message An optional detailed error message to describe the validation failure, or `null` if not specified.
 * @param causeOf A transformer that generates the root cause exception for the validation failure, or `null` if not provided.
 * @param cause A transformer that generates additional context for the validation failure, or `null` if not provided.
 * @return The same character sequence if the validation succeeds.
 * @throws ValidationFailedException If the character sequence does not start with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith char) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence starts with the specified character. If the validation fails,
 * a `ValidationFailedException` is thrown.
 *
 * @param char The character that the sequence must start with.
 * @param callable The Kotlin function (`KFunction`) related to the validation. Can be null.
 * @param parameterName The name of the parameter being validated in the given callable. Can be null.
 * @param message An optional custom message detailing the validation failure. Defaults to a message specifying the missing starting character.
 * @param causeOf A transformer responsible for creating a specific `Throwable` instance to be thrown as the root cause of the exception. Can be null.
 * @param cause A transformer responsible for creating the underlying `Throwable` used as the cause of the validation failure. Can be null.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException If the character sequence does not start with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith char) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given character sequence starts with the specified character. If the validation fails,
 * a custom exception is thrown, optionally including details about the provided callable, parameter, message,
 * and cause transformation logic.
 *
 * @param char The character that the character sequence is expected to start with.
 * @param callable The [KFunction] associated with the validation failure, or null if not applicable.
 * @param parameter The [KParameter] related to the validation failure, or null if not applicable.
 * @param message An optional custom error message to describe the validation failure, defaulting to null.
 * @param causeOf A transformer function to determine the cause of the exception dynamically based on the input, defaulting to null.
 * @param cause A transformer function to compute the root cause of the exception dynamically based on the input, defaulting to null.
 * @return The original character sequence if the validation succeeds.
 * @throws ValidationFailedException If the character sequence does not start with the specified character or
 * if any of the provided transformation logic results in an exception.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith char) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates if the character sequence starts with the specified character.
 * If the validation fails, a `ValidationFailedException` is thrown with the given details.
 *
 * @param char The character that the sequence should start with.
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName The name of the parameter being validated, or `null` if not applicable.
 * @param message An optional custom validation failure message, or `null` to use the default message.
 * @param causeOf A transformer generating a specific exception when validation fails, or `null` to skip this.
 * @param cause A transformer generating a cause for the validation failure, or `null` if no cause is specified.
 * @return The current character sequence if it passes validation.
 * @throws ValidationFailedException If the sequence does not start with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith char) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates if the character sequence starts with the specified character.
 * If the validation fails, it throws a `ValidationFailedException`.
 *
 * @param char The character the sequence must start with.
 * @param callableName The name of the callable (e.g., function or property) where the validation occurs, or null if not specified.
 * @param parameter The parameter related to the validation context, or null if not applicable.
 * @param message An optional error message to provide additional context in case of validation failure.
 * @param causeOf An optional transformer to provide the cause of the exception based on the input value; if null, a default exception cause is used.
 * @param cause Another optional transformer to provide the cause of the exception; if null, it is ignored.
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException If the character sequence does not start with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith char) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence starts with the specified prefix.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs the character sequence that this character sequence should start with
 * @param causeOf an optional transformer that generates a throwable from this character sequence to be used as the cause of the exception
 * @param cause an optional transformer that generates a throwable from this character sequence to provide additional context for the exception
 * @return the current character sequence if the validation is successful
 * @throws ValidationFailedException if the character sequence does not start with the specified prefix
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith cs) throw if (causeOf == null) ValidationFailedException("Char sequence doesn't start with $cs.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence doesn't start with $cs.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence starts with the specified prefix.
 * If the validation fails, an exception is thrown based on the provided transformation logic.
 *
 * @param cs the character sequence to check as the required prefix
 * @param causeOf an optional transformer to produce a specific throwable when validation fails
 * @param cause an optional transformer to produce the cause of the validation failure
 * @param lazyMessage a function to generate a lazy error message when validation fails
 * @return the original character sequence if the validation is successful
 * @throws ValidationFailedException if the validation fails, with an optional cause or message
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this notStartsWith cs) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence starts with the specified prefix.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param cs The prefix to check this character sequence against.
 * @param property The property associated with the validation failure, or null if not applicable.
 * @param variableName The variable name involved in the validation, or null if not specified.
 * @param message An optional custom message for the validation failure. Defaults to a message indicating the prefix mismatch.
 * @param causeOf An optional transformer to generate a specific throwable for the validation error.
 * @param cause An optional transformer to define the underlying cause throwable for the validation error.
 * @return The original character sequence if validation is successful.
 * @throws ValidationFailedException If this character sequence does not start with the specified prefix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith cs) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't start with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence starts with the specified prefix.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs the prefix to check against the current character sequence
 * @param property the main KProperty associated with the validation, or null if not specified
 * @param variable an optional secondary KProperty for additional context, or null if not specified
 * @param message an optional custom message providing details about the validation failure
 * @param causeOf a transformer that generates an exception to be thrown, or null if not specified
 * @param cause a transformer to create the cause of the validation failure, or null if not specified
 * @return the current character sequence if validation succeeds
 * @throws ValidationFailedException if the validation fails
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith cs) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't start $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence starts with the specified prefix.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs The character sequence that the current sequence must start with.
 * @param callable The function (`KFunction`) to which the validation relates. Can be null.
 * @param parameterName The name of the parameter in the given callable that caused the validation issue.
 *                      Can be null.
 * @param message An optional custom message to describe the validation failure. Default is "doesn't start with [cs]".
 * @param causeOf A lambda that produces a `Throwable` to be used as the cause of the thrown exception when the validation fails.
 *                Can be null.
 * @param cause A lambda that produces a secondary `Throwable` instance to further describe or chain exceptions. Can be null.
 * @return The original character sequence if the validation succeeds.
 * @throws ValidationFailedException if the character sequence does not start with the specified prefix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith cs) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't start with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking character sequence starts with the specified prefix and throws
 * a [ValidationFailedException] if the validation fails.
 *
 * @param cs the character sequence that should be the prefix of the invoking character sequence
 * @param callable the [KFunction] associated with the validation context, or null if not applicable
 * @param parameter the [KParameter] associated with the validation context, or null if not applicable
 * @param message an optional custom validation failure message to use; defaults to a message indicating the expected prefix
 * @param causeOf an optional transformer that converts the invoking character sequence into the `Throwable` cause of the exception, or null if not applicable
 * @param cause an optional transformer that generates an additional cause of the exception, or null if not applicable
 * @return the invoking character sequence if it passes the validation
 * @throws ValidationFailedException if the invoking character sequence does not start with the specified prefix
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith cs) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't start with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates whether a character sequence starts with a specified prefix. If the validation fails,
 * a `ValidationFailedException` is thrown with details about the failure.
 *
 * @param cs the prefix that the character sequence should start with
 * @param callableName the name of the callable (e.g., function or method) related to this validation
 * @param parameterName the name of the parameter being validated, or `null` if not applicable
 * @param message an optional custom message describing the validation failure, or `null` to use a default message
 * @param causeOf an optional transformer to provide a throwable as the underlying cause of the failure
 * @param cause an optional transformer to provide an alternate throwable as the underlying cause of the failure
 * @return the original character sequence if it passes the validation
 * @throws ValidationFailedException if the character sequence does not start with the specified prefix
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith cs) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't start with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence starts with the specified prefix.
 *
 * If the validation fails (i.e., the character sequence does not start with the given prefix),
 * this method throws a `ValidationFailedException` with the provided details.
 *
 * @param cs The prefix that the current character sequence should start with.
 * @param callableName The name of the callable (e.g., function or property) relevant to the validation context, or null if not specified.
 * @param parameter The parameter being validated, represented as a `KParameter` instance, or null if not applicable.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf A transformer that generates the root cause of the exception if validation fails, or null if not applicable.
 * @param cause An optional transformer that generates an additional cause for the exception if validation fails.
 * @return The current character sequence if it successfully passes validation.
 * @throws ValidationFailedException if the current character sequence does not start with the given prefix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWith(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWith cs) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't start with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence starts with the specified character, ignoring case considerations.
 * If the character sequence does not start with the specified character, an exception is thrown.
 *
 * @param char The character to check against the beginning of the character sequence.
 * @param causeOf An optional transformer to produce a throwable as the cause of validation failure.
 * @param cause An optional transformer to produce a throwable providing additional context for the validation failure.
 * @return The original character sequence if the validation is successful.
 * @throws ValidationFailedException If the character sequence does not start with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException("Char sequence doesn't start with $char.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence doesn't start with $char.", cause?.invoke(this)))
    return this
}
/**
 * Validates that a character sequence starts with the specified character, ignoring case considerations.
 * If the validation fails, an exception is thrown.
 *
 * @param char The character to check against the beginning of the character sequence.
 * @param causeOf A transformer function that provides a cause exception to be thrown if the validation fails.
 * @param cause A direct transformer function for the cause exception if the validation fails.
 * @param lazyMessage A transformer function used to generate the validation failure message.
 * @return The original character sequence if the validation succeeds.
 * @throws ValidationFailedException if the character sequence does not start with the specified character (ignoring case).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this notStartsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the character sequence starts with the specified character, ignoring case considerations.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param char The character to validate against the beginning of the character sequence.
 * @param property Optional property information associated with the validation. Can be null.
 * @param variableName Optional name of the variable involved in the validation context. Defaults to null.
 * @param message Optional custom error message for the validation failure. Defaults to null.
 * @param causeOf Optional transformer for creating a cause throwable if the validation fails.
 * @param cause Optional transformer for creating a nested cause throwable.
 * @return The original character sequence if the validation succeeds.
 * @throws ValidationFailedException if the character sequence does not start with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current character sequence starts with the specified character, ignoring case considerations.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character to validate as the starting character of the character sequence.
 * @param property The primary property associated with the validation, used for context in the exception message. Can be null.
 * @param variable An optional secondary property providing additional context for the validation failure. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to a standard message if not provided.
 * @param causeOf A transformer function used to generate a cause from the character sequence if validation fails. Can be null.
 * @param cause A transformer function used to add a cause to the exception if validation fails. Can be null.
 * @return The validated character sequence if it starts with the specified character, ignoring case.
 * @throws ValidationFailedException If the character sequence does not start with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates if the character sequence starts with the specified character, ignoring case considerations. If the validation
 * fails, it throws a `ValidationFailedException`.
 *
 * @param char The character that the character sequence is expected to start with, ignoring case.
 * @param callable The `KFunction` representing the function that triggered the validation. Can be null.
 * @param parameterName The name of the parameter associated with the validation. Can be null.
 * @param message An optional custom message to include in the exception if the validation fails. Can be null.
 * @param causeOf An optional transformer that produces a `Throwable` cause to wrap the exception thrown when validation fails. Can be null.
 * @param cause An optional transformer that produces a `Throwable` to serve as the explicit cause for the exception. Can be null.
 * @return The original character sequence if validation succeeds.
 * @throws ValidationFailedException If the character sequence does not start with the specified character (ignoring case).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current character sequence starts with the specified character, ignoring case considerations.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character to verify the start of the character sequence against, ignoring case.
 * @param callable An optional KFunction associated with this validation, representing the context in which the validation is performed.
 * @param parameter An optional KParameter representing the parameter involved in this validation, if applicable.
 * @param message An optional custom message to include in the exception if validation fails; defaults to null.
 * @param causeOf An optional transformer used to produce a specific cause of the `ValidationFailedException` if validation fails.
 * @param cause An optional transformer used to generate the underlying cause of the exception.
 * @return The original character sequence if validation succeeds.
 * @throws ValidationFailedException If the character sequence does not start with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling character sequence starts with the specified character, ignoring case considerations.
 * If the validation fails, it throws a `ValidationFailedException`.
 *
 * @param char The character to validate against the start of the character sequence.
 * @param callableName The name of the callable (e.g., function or method) related to this validation.
 * @param parameterName Optional parameter name causing the validation, if applicable.
 * @param message Optional custom message to include in the validation exception if the validation fails.
 * @param causeOf Optional transformer to create a different exception type if validation fails.
 * @param cause Optional transformer for underlying cause of validation failure.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException If the character sequence does not start with the specified character (ignoring case).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current character sequence starts with the specified character,
 * ignoring case considerations. If the validation fails, a `ValidationFailedException`
 * is thrown.
 *
 * @param char The character to check against the beginning of the character sequence.
 * @param callableName The name of the callable (e.g., function or property) where validation is performed, or null if not specified.
 * @param parameter The KParameter instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message providing additional context if validation fails.
 * @param causeOf An optional transformer that generates a custom exception related to the validation failure, or null if not specified.
 * @param cause An optional transformer that generates the underlying cause of the validation failure, or null if not specified.
 * @return The current character sequence if validation passes.
 * @throws ValidationFailedException If the validation fails, with details about the cause and context of the failure.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't start with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't start with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence starts with the specified prefix, ignoring case considerations.
 * If the validation fails, an exception is thrown.
 *
 * @param cs the character sequence to check as the prefix.
 * @param causeOf a transformer function to create a custom throwable, based on the current sequence, if the validation fails.
 * @param cause an optional transformer function to generate an underlying cause for the exception.
 * @return the current character sequence if the validation passes.
 * @throws ValidationFailedException if the current character sequence does not start with the specified prefix (case-insensitive).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException("Char sequence doesn't start with $cs.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence doesn't start with $cs.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence starts with the specified prefix, ignoring case considerations.
 * If the validation fails, an exception is thrown.
 *
 * @param cs the prefix to validate against the start of the character sequence.
 * @param causeOf an optional transformer that creates a throwable cause based on the current character sequence, or null if not used.
 * @param cause an optional transformer that generates a throwable cause based on the current character sequence, or null if not used.
 * @param lazyMessage a transformer to produce a custom message if the validation fails.
 * @return the validated character sequence if it starts with the specified prefix (case-insensitive).
 * @throws ValidationFailedException if the character sequence does not start with the specified prefix, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this notStartsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current character sequence starts with the specified prefix, ignoring case considerations.
 * If the validation fails, a `ValidationFailedException` is thrown with an optional message and cause.
 *
 * @param cs The character sequence to check for at the start of the current character sequence.
 * @param property The property associated with the validation failure, used for detailed error reporting. Can be null.
 * @param variableName The optional name of the variable involved in the validation. Can be null.
 * @param message An optional custom error message to include in the exception if validation fails. Can be null.
 * @param causeOf A transformer function that generates a throwable cause for the exception in case validation fails. Can be null.
 * @param cause An alternative transformer function for generating a throwable cause. Can be null.
 * @return The current character sequence if validation succeeds.
 * @throws ValidationFailedException If the character sequence does not start with the specified prefix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't start with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current character sequence starts with the specified prefix, ignoring case considerations.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs the prefix to validate against the start of this character sequence.
 * @param property the primary property associated with this validation, or null if not applicable.
 * @param variable an optional secondary property providing additional context, or null if not applicable.
 * @param message an optional custom message for the validation failure; if null, a default message is used.
 * @param causeOf a transformer function to generate the cause of the exception if validation fails, or null if not applicable.
 * @param cause a transformer function to generate the underlying throwable for the exception, or null if not applicable.
 * @return the original character sequence if validation is successful.
 * @throws ValidationFailedException if this character sequence does not start with the specified prefix (case-insensitive).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't start $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current character sequence starts with the specified prefix, ignoring case considerations.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param cs The prefix to validate at the start of the current character sequence.
 * @param callable The Kotlin function (`KFunction`) associated with the validation. Can be null.
 * @param parameterName The parameter name associated with the current validation. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Default is null.
 * @param causeOf An optional transformer responsible for providing the cause of the exception as a `Throwable`. Can be null.
 * @param cause An optional transformer to generate the cause of the exception as a `Throwable`. Can be null.
 * @return The original character sequence if it passes the validation.
 * @throws ValidationFailedException if the character sequence does not start with the specified prefix (case-insensitive).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't start with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current character sequence starts with the specified prefix, ignoring case considerations.
 * Throws a [ValidationFailedException] if the validation fails.
 *
 * @param cs the prefix to validate against, case-insensitively.
 * @param callable the [KFunction] related to the validation failure, if applicable; can be null.
 * @param parameter the [KParameter] involved in the validation failure, if applicable; can be null.
 * @param message an optional message included in the thrown exception if the validation fails; defaults to null.
 * @param causeOf an optional transformer function to generate a specific cause for the thrown exception; defaults to null.
 * @param cause an optional transformer function to provide the root cause for the thrown exception; defaults to null.
 * @return the current character sequence if it passes the validation.
 * @throws ValidationFailedException if the character sequence does not start with the specified prefix, case-insensitively.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't start with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence starts with the specified prefix, ignoring case considerations.
 * If the validation fails, throws a [ValidationFailedException] with the provided details.
 *
 * @param T the type of the character sequence on which the validation is performed.
 * @param cs the prefix to validate against the start of this character sequence.
 * @param callableName an optional name of the callable (e.g., function or method) where the validation is performed.
 * @param parameterName an optional name of the parameter being validated.
 * @param message an optional custom message to include in the exception if validation fails.
 * @param causeOf an optional transformer to create a throwable representing the cause of the validation failure, before including additional validation details.
 * @param cause an optional transformer to further define the underlying cause throwable associated with the validation failure.
 * @return the current character sequence if the validation passes.
 * @throws ValidationFailedException if the character sequence does not start with the specified prefix, ignoring case considerations.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't start with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence starts with the specified prefix, ignoring case considerations.
 * If the validation fails, an exception is thrown, as configured.
 *
 * @param cs The character sequence to validate against as the prefix.
 * @param callableName The name of the callable (e.g., function or property) where validation is being performed, or null if not specified.
 * @param parameter The KParameter instance representing the parameter related to the validation, or null if not applicable.
 * @param message An optional error message to provide additional context for the validation failure, or null to use the default message.
 * @param causeOf A transformer function that takes the current instance and returns a Throwable as the cause of the validation exception, or null if not provided.
 * @param cause An alternative transformer function that takes the current instance and returns a Throwable as the cause of the validation exception, or null if not provided.
 * @return The original character sequence if validation is successful.
 * @throws ValidationFailedException If the validation fails and the sequence does not start with the specified prefix (case-insensitive).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateStartsWithIgnoreCase(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notStartsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't start with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't start with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified character.
 * If the character sequence starts with the given character, a validation exception is thrown.
 *
 * @param char the character to check against the start of the character sequence.
 * @param causeOf an optional transformer for providing a custom exception to throw when validation fails.
 *                If null, the default exception logic is used.
 * @param cause an optional transformer for providing a cause of the exception when validation fails.
 *              If null, no additional cause is applied.
 * @return the original character sequence if validation passes.
 * @throws ValidationFailedException if the character sequence starts with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith char) throw if (causeOf == null) ValidationFailedException("Char sequence starts with $char.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence starts with $char.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not start with the specified character.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param char the character that the character sequence must not start with.
 * @param causeOf a transformer function to provide a custom cause for the exception, or `null`.
 * @param cause an optional transformer function to generate an additional cause for the exception.
 * @param lazyMessage a transformer function to provide the message for the exception. The message is lazily computed based on the character sequence.
 * @return the original character sequence if the validation succeeds.
 * @throws ValidationFailedException if the character sequence starts with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this startsWith char) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not start with the specified character.
 * If the character sequence starts with the given character, this method throws a `ValidationFailedException`.
 *
 * @param char The character that the sequence should not start with.
 * @param property Optional property metadata associated with the validation context.
 * @param variableName Optional name of the variable being validated. Used for clearer error descriptions.
 * @param message Optional custom message to be included in the exception if validation fails.
 * @param causeOf Optional transformer to produce a cause exception before the `ValidationFailedException` is thrown.
 * @param cause Optional transformer to produce the underlying cause of the `ValidationFailedException`.
 * @return The original character sequence if it does not start with the specified character.
 * @throws ValidationFailedException If the character sequence starts with the given character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith char) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified character.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param char the character that the sequence should not start with.
 * @param property the main [KProperty] associated with this validation, providing context about the property being validated.
 * @param variable an optional secondary [KProperty] providing additional context about the validation.
 * @param message an optional custom message for the validation failure.
 * @param causeOf an optional transformer to provide an alternative exception as the cause of the failure.
 * @param cause an optional transformer to define the specific cause of the validation failure.
 * @return the original character sequence if validation passes.
 * @throws ValidationFailedException if the validation fails because the character sequence starts with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith char) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not start with the specified character.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char the character that the character sequence must not start with.
 * @param callable the Kotlin function (`KFunction`) associated with the validation failure. Can be null.
 * @param parameterName the name of the parameter in the associated callable that is being validated. Can be null.
 * @param message an optional custom error message. If null, a default message will be used.
 * @param causeOf an optional transformer responsible for generating a cause exception when validation fails. Can be null.
 * @param cause an optional transformer responsible for creating the underlying exception cause. Can be null.
 * @return the original character sequence if the validation is successful.
 * @throws ValidationFailedException if the character sequence starts with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith char) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not start with the specified character.
 * If the sequence starts with the character, this method throws a [ValidationFailedException].
 *
 * @param char the character that the sequence must not start with.
 * @param callable the [KFunction] associated with the validation context, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter involved in validation, or null if not applicable.
 * @param message an optional message providing additional details for the validation failure. Defaults to `null` if not specified.
 * @param causeOf an optional transformer function that generates the primary cause of the exception based on the input. Defaults to `null`.
 * @param cause an optional transformer function for creating an additional nested cause for the exception. Defaults to `null`.
 * @return the original character sequence if validation passes.
 * @throws ValidationFailedException if the character sequence starts with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith char) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified character.
 * If the sequence starts with the given character, a [ValidationFailedException] is thrown.
 *
 * @param char the character to validate against the start of the character sequence.
 * @param callableName the name of the callable (e.g., function or method) related to this validation.
 * @param parameterName the name of the parameter being validated (optional).
 * @param message an optional custom message to include in the exception if validation fails.
 * @param causeOf a transformer function to generate the underlying cause of the exception (optional).
 * @param cause a transformer function to provide an additional cause for the exception (optional).
 * @return the character sequence itself if validation passes.
 * @throws ValidationFailedException if the character sequence starts with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith char) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that a character sequence does not start with the specified character.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character to check against the start of the sequence.
 * @param callableName The name of the callable where validation is being performed. Can be null.
 * @param parameter The `KParameter` instance representing the parameter being validated. Can be null.
 * @param message An optional custom error message for the validation failure. Defaults to a generated message if not provided.
 * @param causeOf An optional transformer function that generates a cause (Throwable) based on the input, determining the root cause for the failure.
 * @param cause An optional transformer function that generates a cause (Throwable) for the thrown exception.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException if the character sequence starts with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith char) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not start with the specified prefix.
 * If the character sequence starts with the given prefix, a `ValidationFailedException` is thrown.
 *
 * @param cs The character sequence to check as a prefix.
 * @param causeOf An optional transformer function to generate a specific throwable if validation fails.
 * @param cause An optional transformer function to generate the cause for the exception if validation fails.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException If the character sequence starts with the specified prefix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith cs) throw if (causeOf == null) ValidationFailedException("Char sequence starts with $cs.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence starts with $cs.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not start with the specified prefix.
 * If this character sequence starts with the given prefix, a `ValidationFailedException`
 * is thrown with an optional lazy evaluation message and cause.
 *
 * @param cs The prefix that the character sequence should not start with.
 * @param causeOf A transformer that generates a `Throwable` to throw as the primary cause,
 *        or `null` if no transformer is provided.
 * @param cause A transformer that generates an additional cause `Throwable`,
 *        or `null` if no transformer is provided.
 * @param lazyMessage A transformer used to generate the error message lazily based on the input character sequence.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException If the character sequence starts with the specified prefix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this startsWith cs) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given CharSequence does not start with the specified prefix.
 * Throws a ValidationFailedException if the validation fails.
 *
 * @param cs The prefix to check against the beginning of this character sequence.
 * @param property The Kotlin property associated with the validation, or null if not applicable.
 * @param variableName Optional name of the variable being validated. Included in the exception message if provided.
 * @param message Optional custom error message for the exception. Defaults to a generic message indicating the validation failure.
 * @param causeOf An optional transformer to provide a Throwable in case of validation failure. If null, a default exception is used.
 * @param cause An optional transformer to provide a secondary Throwable as the cause of the primary exception.
 * @return The original CharSequence if the validation passes.
 * @throws ValidationFailedException If the character sequence starts with the specified prefix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith cs) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified prefix.
 * If the validation fails, it throws a `ValidationFailedException`.
 *
 * @param cs The prefix to check against the start of the character sequence.
 * @param property The main KProperty associated with this validation, providing context for error reporting.
 * @param variable An optional secondary KProperty that adds more context to the validation or error reporting.
 * @param message An optional error message to include if the validation fails.
 * @param causeOf An optional transformer that generates the cause of the exception based on the input character sequence.
 * @param cause An optional transformer that generates an underlying cause of the exception.
 * @return The character sequence itself if validation succeeds.
 * @throws ValidationFailedException if the character sequence starts with the specified prefix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith cs) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not start with the specified prefix. If the validation fails,
 * a `ValidationFailedException` is thrown.
 *
 * @param cs The prefix to check against the beginning of the character sequence.
 * @param callable The Kotlin function (`KFunction`) associated with this validation. Can be null.
 * @param parameterName The name of the parameter in the callable that underwent validation. Can be null.
 * @param message An optional custom message describing the validation failure. Can be null; a default message will be used otherwise.
 * @param causeOf A transformer function that generates a throwable cause for the validation failure. Can be null.
 * @param cause An alternative transformer function that generates a throwable cause for the validation failure. Can be null.
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException If the character sequence starts with the specified prefix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith cs) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified prefix. If the validation fails, a
 * [ValidationFailedException] is thrown.
 *
 * @param cs The character sequence to check as a prefix.
 * @param callable The [KFunction] that is being validated, or null if not applicable.
 * @param parameter The [KParameter] involved in the validation, or null if not applicable.
 * @param message An optional custom error message to include in the [ValidationFailedException], or null for the default message.
 * @param causeOf A transformer to generate the cause of the [ValidationFailedException], or null if none is specified.
 * @param cause A transformer to generate the root cause of the error as a [Throwable], or null if not required.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException If the character sequence starts with the specified prefix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith cs) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not start with the specified prefix.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs The prefix that the sequence should not start with.
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName The name of the parameter that caused the validation failure, or null if none.
 * @param message An optional custom message to be included in the validation exception.
 * @param causeOf A transformer that produces a throwable to be thrown based on the current character sequence.
 * @param cause A transformer that produces the cause of the validation exception based on the current character sequence.
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException If the character sequence starts with the given prefix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith cs) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified prefix.
 * If validation fails, a ValidationFailedException is thrown.
 *
 * @param cs The character sequence that should not be the prefix of this character sequence.
 * @param callableName The name of the callable where the validation is being performed, or null if not specified.
 * @param parameter The KParameter instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message to describe the validation failure. Defaults to a message indicating the input starts with the provided prefix.
 * @param causeOf An optional transformer to generate the root cause of the exception, or null if not specified.
 * @param cause An optional transformer to generate the cause of the exception, or null if not specified.
 * @return The validated character sequence. If validation fails, an exception is thrown, and the original sequence is not returned.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWith(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWith cs) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified character, ignoring case differences.
 * If the validation fails, it throws a [ValidationFailedException] optionally constructed using the provided transformers.
 *
 * @param char The character to check against the start of the character sequence.
 * @param causeOf An optional transformer to generate a Throwable that provides context for the validation failure.
 * @param cause An optional transformer to generate a Throwable representing the root cause of the validation failure.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException if the character sequence starts with the specified character, ignoring case differences.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException("Char sequence starts with $char.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence starts with $char.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified character, ignoring case differences.
 * If the character sequence starts with the specified character, a validation exception will be thrown.
 *
 * @param char The character to check against the start of the character sequence.
 * @param causeOf An optional transformer function to generate a throwable for additional context in case of validation failure.
 * @param cause An optional transformer function to generate the root cause throwable when validation fails.
 * @param lazyMessage A transformer function to generate a detailed message for the exception when validation fails.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException if the character sequence starts with the specified character, ignoring case differences.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this startsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified character, ignoring character case.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character to check against the start of this character sequence, ignoring case.
 * @param property An optional `KProperty` associated with the validation. Used for error message construction.
 * @param variableName An optional name of the variable being validated. Included in the error message if provided.
 * @param message An optional custom error message to override the default validation failure message.
 * @param causeOf An optional transformer to produce a throwable representing the root cause of the validation failure.
 * @param cause An optional transformer to produce a secondary throwable to attach to the exception as a cause.
 * @return The validated character sequence if the validation passes.
 * @throws ValidationFailedException If the character sequence starts with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified character, ignoring case differences.
 * If the validation fails, a `ValidationFailedException` will be thrown.
 *
 * @param char The character to check against the start of the character sequence.
 * @param property An optional main property providing context about the validation target, or null if not specified.
 * @param variable An optional secondary property providing additional context, or null if not specified.
 * @param message An optional message to be included in the exception if validation fails, or null for the default message.
 * @param causeOf An optional transformer to generate the cause of the exception, or null if no specific cause is provided.
 * @param cause An optional transformer to generate an additional cause for the exception, or null if no additional cause is provided.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException If the character sequence starts with the specified character, ignoring case differences.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not start with the specified character, ignoring case.
 * If the validation fails, an exception is thrown.
 *
 * @param char the character to check against the start of the sequence, case-insensitively.
 * @param callable optional callable reference related to the validation operation.
 * @param parameterName optional name of the parameter being validated.
 * @param message optional custom message for the validation failure.
 * @param causeOf optional transformer to create a throwable cause when validation fails.
 * @param cause optional transformer to include additional throwable context for the validation failure.
 * @return the original character sequence if validation passes.
 * @throws ValidationFailedException if the character sequence starts with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified character sequence does not start with the given character
 * (case-insensitive). If the validation fails, an exception is thrown with optional additional
 * information.
 *
 * @param char The character that the sequence must not start with (case-insensitive).
 * @param callable Optional callable context to associate with the validation failure.
 * @param parameter Optional parameter context to associate with the validation failure.
 * @param message Optional custom error message to include in the validation failure.
 * @param causeOf Optional transformer to provide a custom throwable if the validation fails.
 * @param cause Optional transformer to generate the cause of the exception if validation fails.
 * @return The original character sequence if validation succeeds.
 * @throws ValidationFailedException If the character sequence starts with the specified character
 * and a cause or additional error context is provided.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified character, ignoring case differences.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character to check at the start of the character sequence.
 * @param callableName The name of the callable (e.g., function or method) associated with this validation.
 * @param parameterName An optional parameter name associated with this validation, or `null` if not applicable.
 * @param message An optional custom message to include in the exception if validation fails, or `null` for a default message.
 * @param causeOf An optional transformer that produces an exception based on this character sequence to use as the root cause, or `null`.
 * @param cause An optional transformer that produces an exception based on this character sequence to chain as the cause, or `null`.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException If the character sequence starts with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not start with the specified character, ignoring case differences.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param char The character to compare with the start of this character sequence.
 * @param callableName The name of the callable where validation is applied, or null if not specified.
 * @param parameter An optional [KParameter] representing the parameter being validated.
 * @param message An optional custom error message to include in the exception. Defaults to a generic message.
 * @param causeOf An optional transformer to generate the root cause throwable if validation fails.
 * @param cause An optional transformer to generate a specific cause throwable tied to this validation.
 * @return The original character sequence if validation succeeds.
 * @throws ValidationFailedException if the character sequence starts with the specified character (case-insensitive).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "starts with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "starts with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not start with the specified prefix, ignoring case.
 * If the validation fails, it throws a [ValidationFailedException] with an optional cause.
 *
 * @param cs the character sequence to check as the prefix.
 * @param causeOf a transformer to provide a specific throwable when the validation fails, or `null` to use the default exception.
 * @param cause a transformer that generates a throwable as the root cause of the exception, or `null` for no root cause.
 * @return the original character sequence if the validation passes.
 * @throws ValidationFailedException if the current character sequence starts with the specified prefix, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException("Char sequence starts with $cs.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence starts with $cs.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not start with the specified prefix,
 * ignoring case differences. If the validation fails, a custom exception is thrown.
 *
 * @param cs the character sequence to check as the prefix.
 * @param causeOf an optional transformer that provides the exception to be thrown when the validation fails.
 * @param cause an optional transformer to provide the underlying cause of the exception.
 * @param lazyMessage a transformer to generate the error message when the validation fails.
 * @return the current character sequence if the validation passes.
 * @throws ValidationFailedException if the character sequence starts with the specified prefix, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this startsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not start with the specified prefix, ignoring case considerations.
 * If the validation fails, an exception is thrown.
 *
 * @param cs the prefix character sequence to check against
 * @param property the property metadata (nullable) associated with the value being validated
 * @param variableName the name of the variable being validated (nullable)
 * @param message a custom error message to include in the exception (nullable)
 * @param causeOf a transformer that generates a throwable cause for the validation failure (nullable)
 * @param cause a transformer that generates an additional throwable cause for the validation failure (nullable)
 * @return the original character sequence if validation passes
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not start with the specified prefix, ignoring case.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param cs The character sequence that should not match the beginning of the current character sequence.
 * @param property The main property associated with the validation, used for detailed error reporting.
 * @param variable An optional secondary property providing additional context for the validation, or null if not specified.
 * @param message An optional custom error message describing the validation failure, or null to use the default message.
 * @param causeOf A transformer function that generates the specific cause of the exception, or null if not required.
 * @param cause A transformer function that generates the root cause exception, or null if not specified.
 * @return The current character sequence if validation passes.
 * @throws ValidationFailedException if the validation fails because the current character sequence starts with the specified prefix, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current CharSequence does not start with the specified prefix, ignoring case.
 * If the validation fails, an exception is thrown.
 *
 * @param cs the prefix to be checked against this CharSequence.
 * @param callable the optional KFunction representing the callable associated with the validation.
 * @param parameterName the optional name of the parameter being validated.
 * @param message an optional custom validation failure message.
 * @param causeOf an optional transformer that generates the root cause of the exception when the validation fails.
 * @param cause an optional transformer that generates the exception to be thrown when the validation fails.
 * @return the original CharSequence if validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling character sequence does not start with the specified character sequence, ignoring case sensitivity.
 * If the validation fails (i.e., the character sequence starts with the specified sequence), a `ValidationFailedException` is thrown.
 *
 * @param cs the character sequence to check as the invalid prefix.
 * @param callable the [KFunction] associated with the context of the validation, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter for which the validation is being performed, or null if not applicable.
 * @param message an optional custom message providing additional context about the validation failure.
 * @param causeOf an optional transformer function that generates a throwable as the primary cause of the validation failure.
 * @param cause an optional transformer function that generates a throwable as the nested cause of the validation failure.
 * @return the original character sequence if the validation passes.
 * @throws ValidationFailedException if the character sequence starts with the specified sequence, ignoring case sensitivity.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not start with the specified prefix, ignoring case.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs the character sequence to check as the prefix.
 * @param callableName the name of the callable (e.g., function or method) related to this validation.
 * @param parameterName an optional name of the parameter associated with this validation, or null if not applicable.
 * @param message an optional custom message to provide more details about the validation failure, or null for default messaging.
 * @param causeOf an optional transformer that generates the root cause of the failure as a throwable, or null if not applicable.
 * @param cause an optional transformer that generates additional throwable information, or null if not applicable.
 * @return the original character sequence if the validation passes.
 * @throws ValidationFailedException if the character sequence starts with the specified prefix, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `CharSequence` does not start with the specified string, ignoring case sensitivity.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs The `CharSequence` to check against the start of the current `CharSequence`, ignoring case.
 * @param callableName An optional name of the callable associated with this validation, primarily for debugging purposes.
 * @param parameter An optional parameter reference for more detailed information about the failing entity.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf An optional transformer for generating the root cause exception.
 * @param cause An optional transformer for generating additional context for the exception.
 * @return The current `CharSequence` if it does not start with the specified `CharSequence`, ignoring case.
 * @throws ValidationFailedException If the current `CharSequence` starts with the specified string, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotStartsWithIgnoreCase(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this startsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "starts with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "starts with $cs", cause?.invoke(this)))
    return this
}

/**
 * Validates that the character sequence ends with the specified character.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character that the sequence must end with.
 * @param causeOf A transformer to produce a specific throwable based on the input sequence, if the validation fails (optional).
 * @param cause A transformer to produce a cause throwable based on the input sequence, if the validation fails (optional).
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException if the character sequence does not end with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith char) throw if (causeOf == null) ValidationFailedException("Char sequence doesn't end with $char.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence doesn't end with $char.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence ends with the specified character. If the validation fails,
 * a `ValidationFailedException` is thrown with a lazily computed message and optional causes.
 *
 * @param char the character that the sequence should end with.
 * @param causeOf an optional transformer to generate a throwable cause for validation failure.
 * @param cause an additional optional transformer to provide a secondary cause for the exception.
 * @param lazyMessage a transformer to compute the error message lazily based on the sequence.
 * @return the original character sequence if validation succeeds.
 * @throws ValidationFailedException if the character sequence does not end with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this notEndsWith char) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the character sequence ends with the specified character. If the validation fails,
 * a `ValidationFailedException` is thrown with the provided details.
 *
 * @param char The character that the sequence should end with for validation to pass.
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName Name of the variable being validated. Optional, can be null.
 * @param message Additional validation failure message. Defaults to null if not provided.
 * @param causeOf A transformer function that creates an exception based on the input sequence,
 *                to be used as the root cause. Optional, can be null.
 * @param cause A transformer function that creates an exception based on the input sequence.
 *              Used as the underlying cause for the exception thrown. Optional, can be null.
 * @return The original character sequence if the validation succeeds.
 * @throws ValidationFailedException If the character sequence does not end with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith char) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current character sequence ends with the specified character.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param char the character to check if the character sequence ends with.
 * @param property the main property associated with the validation, or null if not specified.
 * @param variable an optional secondary property providing additional context, or null if not specified.
 * @param message an optional message providing details about the validation failure, or null if a default message is sufficient.
 * @param causeOf an optional transformer that generates the cause of the exception, or null to skip additional cause generation.
 * @param cause an optional transformer that provides additional context to the exception, or null if not needed.
 * @return the original character sequence if the validation succeeds.
 * @throws ValidationFailedException if the character sequence does not end with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith char) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence ends with the specified character.
 * If the validation fails, an exception is thrown with optional parameters for additional context or a custom cause.
 *
 * @param char The character that the sequence is expected to end with.
 * @param callable The Kotlin function (`KFunction`) associated with the validation, providing contextual information. Can be null.
 * @param parameterName An optional name of the parameter in the callable that is being validated. Can be null.
 * @param message An optional custom validation failure message. Default is generated based on the validation rule.
 * @param causeOf An optional transformer that creates a custom `Throwable` for the validation failure. Can be null.
 * @param cause An optional transformer to generate a cause for the validation failure exception. Can be null.
 * @return The original character sequence, if validation passes.
 * @throws ValidationFailedException If the character sequence does not end with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith char) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates if the character sequence ends with a specific character. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char the character that the sequence should end with.
 * @param callable the [KFunction] representing the callable to associate with the validation failure, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter involved in the validation failure, or null if not applicable.
 * @param message an optional custom message to include in the exception if validation fails. Defaults to null.
 * @param causeOf a transformer function to produce a specific cause of type [Throwable] from the character sequence, or null if not applicable.
 * @param cause a transformer function to produce a nested cause of type [Throwable] from the character sequence, or null if not applicable.
 * @return the original character sequence if the validation succeeds.
 * @throws ValidationFailedException if the character sequence does not end with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith char) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence ends with the specified character.
 * If the validation fails, it throws a `ValidationFailedException` with optional details.
 *
 * @param char the character that the character sequence is expected to end with.
 * @param callableName the name of the callable (e.g., function or method) where the validation is performed.
 * @param parameterName an optional name of the parameter being validated.
 * @param message an optional custom message for the validation failure.
 * @param causeOf an optional transformer to generate a throwable that describes the root cause of the validation failure.
 * @param cause an optional transformer to generate a throwable that represents an immediate cause of the validation failure.
 * @return the original character sequence if the validation passes.
 * @throws ValidationFailedException if the character sequence does not end with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith char) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the character sequence ends with the specified character.
 * If it does not, throws a [ValidationFailedException].
 *
 * @param char The character that the sequence is expected to end with.
 * @param callableName The name of the callable (e.g., function or property) where validation is taking place, or null if not specified.
 * @param parameter The [KParameter] instance representing the parameter being validated, or null if not applicable.
 * @param message An optional custom error message describing the validation failure. Defaults to a message indicating that the sequence does not end with the specified character
 * .
 * @param causeOf An optional transformer function that generates a throwable to be used as the primary cause of the [ValidationFailedException]. Defaults to null.
 * @param cause An optional transformer function that generates a throwable to be used as the secondary (chained) cause of the [ValidationFailedException]. Defaults to null.
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException If the character sequence does not end with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith char) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the invoking character sequence ends with the specified suffix.
 * If the validation fails, an exception is thrown. Optionally, custom transformers for the error cause
 * or combined error cause can be provided.
 *
 * @param cs The character sequence that the invoking character sequence must end with.
 * @param causeOf An optional transformer to generate a specific throwable if the validation fails.
 * @param cause An optional transformer to generate an underlying cause for the failure.
 * @return The original character sequence if the validation succeeds.
 * @throws ValidationFailedException If the character sequence does not end with the specified suffix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith cs) throw if (causeOf == null) ValidationFailedException("Char sequence doesn't end with $cs.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence doesn't end with $cs.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence ends with the specified suffix.
 * If the validation fails, a `ValidationFailedException` is thrown with the provided custom message and cause logic.
 *
 * @param cs The suffix to validate against the current character sequence.
 * @param causeOf An optional transformer that generates a custom exception, triggered if the validation fails. Can be null.
 * @param cause An optional transformer to provide a cause for the `ValidationFailedException`. Can be null.
 * @param lazyMessage A transformer to generate a detailed error message used in the exception when validation fails.
 * @return The current character sequence if validation is successful.
 * @throws ValidationFailedException if the current character sequence does not end with the specified suffix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this notEndsWith cs) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the current character sequence ends with the specified suffix.
 *
 * If the character sequence does not end with the specified suffix, a `ValidationFailedException` is thrown.
 * The exception can be customized using optional parameters such as the associated property, variable name,
 * message, and additional throwable transformers for specific handling.
 *
 * @param cs The character sequence that this character sequence is validated against as the required suffix.
 * @param property The `KProperty` associated with this validation, or null if the validation is not tied to a specific property.
 * @param variableName The name of the variable being validated, or null if not specified.
 * @param message The custom message to include in the exception when validation fails, or null to use the default message.
 * @param causeOf Transformer for creating the throwable cause of the exception; may be null if no specific transformer is provided.
 * @param cause Additional transformer for processing the throwable cause; may be null if no specific processing is needed.
 * @return The original character sequence if the validation passes successfully.
 * @throws ValidationFailedException Thrown when the character sequence does not end with the specified suffix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith cs) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't end with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence ends with the specified suffix. If the validation fails, an exception is thrown.
 *
 * @param cs The character sequence that the input should end with.
 * @param property The primary property associated with the validation, providing context about the validation target.
 * @param variable An optional secondary property providing additional context about the validation target.
 * @param message An optional message to include in the validation exception if the validation fails.
 * @param causeOf An optional transformer to create a throwable cause for the exception if the validation fails.
 * @param cause An optional transformer to provide an additional cause of the exception based on the input value.
 * @return The original character sequence if it successfully passes the validation.
 * @throws ValidationFailedException If the character sequence does not end with the specified suffix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith cs) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't end $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence ends with the specified suffix.
 * If the validation fails, throws a `ValidationFailedException` with an optional message and cause.
 *
 * @param cs The character sequence that is expected to be the suffix.
 * @param callable The Kotlin function (`KFunction`) associated with this validation, used for error context. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message A custom error message for the validation failure. Defaults to "doesn't end with [cs]".
 * @param causeOf A transformer generating the underlying exception when validation fails. Can be null.
 * @param cause A transformer generating the cause for the `ValidationFailedException` when validation fails. Can be null.
 * @return The original character sequence if it passes the validation.
 * @throws ValidationFailedException if this character sequence does not end with the specified suffix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith cs) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't end with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence ends with the specified character sequence.
 *
 * Throws a ValidationFailedException if the validation fails, optionally using provided
 * additional information and custom exception transformers.
 *
 * @param cs The character sequence that the current character sequence must end with.
 * @param callable The [KFunction] associated with the validation context, if applicable.
 * @param parameter The [KParameter] representing the parameter involved in the validation, if applicable.
 * @param message An optional custom message to include in the exception if the validation fails.
 * @param causeOf An optional transformer to create a custom exception as the root cause of the failure.
 * @param cause An optional transformer to create an additional cause for the validation failure.
 * @return The validated character sequence if it ends with the specified suffix.
 * @throws ValidationFailedException if the character sequence does not end with the specified suffix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith cs) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't end with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence ends with the specified suffix. If it does not,
 * a `ValidationFailedException` is thrown.
 *
 * @param cs The suffix to check if the current character sequence ends with.
 * @param callableName The name of the callable (function or method) associated with this validation.
 * @param parameterName The name of the parameter being validated, if applicable. Defaults to `null`.
 * @param message An optional custom error message to include in the exception. Defaults to `null`.
 * @param causeOf A transformer function that takes the invalid input and produces a
 *                specific throwable cause for the exception. Defaults to `null`.
 * @param cause A transformer function providing the underlying cause of the exception when it is thrown. Defaults to `null`.
 * @return The original character sequence if it passes validation.
 * @throws ValidationFailedException if the character sequence does not end with the specified suffix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith cs) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't end with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence ends with the specified suffix.
 *
 * If the character sequence does not end with the given suffix, this method throws
 * a `ValidationFailedException` with details about the validation failure. The exception
 * can include an optional message and a cause, as well as details about the callable
 * name and parameter involved in the validation (if provided).
 *
 * @param cs The expected suffix to check for in the character sequence.
 * @param callableName The name of the callable being validated, or null if not applicable.
 * @param parameter The parameter related to the validation, or null if not applicable.
 * @param message An optional custom error message to include in the exception, or null for a default message.
 * @param causeOf An optional transformer that generates the cause of the exception based on the character sequence, or null.
 * @param cause An optional transformer that generates the cause of the exception based on the character sequence, or null.
 * @return The original character sequence if validation succeeds.
 * @throws ValidationFailedException if the character sequence does not end with the specified suffix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWith(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWith cs) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't end with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence ends with the specified character, ignoring case sensitivity.
 * If the validation fails, an exception is thrown.
 *
 * @param char The character to validate against the end of the character sequence.
 * @param causeOf An optional transformer to provide a custom exception based on the character sequence if validation fails.
 * @param cause An optional transformer to provide a custom root cause exception if validation fails.
 * @return The original character sequence if the validation succeeds.
 * @throws ValidationFailedException if the character sequence does not end with the specified character, ignoring case sensitivity.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException("Char sequence doesn't end with $char.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence doesn't end with $char.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence ends with the specified character, ignoring case sensitivity.
 * If the validation fails, an exception is thrown with the provided custom message and optional causes.
 *
 * @param char The character that the sequence should end with, case insensitive.
 * @param causeOf An optional transformer for creating a customized throwable if validation fails.
 * @param cause An optional transformer for creating the cause of the validation failure.
 * @param lazyMessage A transformer that provides a lazy message to include in the exception when validation fails.
 * @return The original character sequence if the validation is successful.
 * @throws ValidationFailedException if the character sequence does not end with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this notEndsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence ends with the specified character, ignoring case sensitivity.
 * If the validation fails, a ValidationFailedException is thrown with an optional custom message or cause.
 *
 * @param char The character to check against the end of the character sequence (case insensitive).
 * @param property An optional KProperty representing the property associated with the validation failure.
 *                 Used to provide additional context in the exception, if thrown.
 * @param variableName An optional name of the variable involved in the validation. Included in the exception
 *                     message if provided.
 * @param message Optional custom error message to be used if validation fails. Defaults to a generated message.
 * @param causeOf An optional transformer that generates the root cause throwable when validation fails.
 *                If provided, it is used to generate the exception's cause.
 * @param cause An optional transformer that generates an additional cause for the validation failure.
 *              Only used if `causeOf` is not provided.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException If the character sequence does not end with the specified character,
 *                                   ignoring case sensitivity.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates if the character sequence ends with the specified character, ignoring case sensitivity.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character to check against the end of the character sequence.
 * @param property The main `KProperty` associated with the validation, or `null` if not specified.
 * @param variable An optional secondary `KProperty` that provides additional context, or `null` if not specified.
 * @param message An optional custom message to provide additional details for the validation failure, or `null` if not specified.
 * @param causeOf An optional transformer that produces a `Throwable` to be used as the cause when validation fails, or `null` if not specified.
 * @param cause An optional transformer that produces a `Throwable` for the exception caused when validation fails, or `null` if not specified.
 * @return The original character sequence if the validation succeeds.
 * @throws ValidationFailedException if the character sequence does not end with the specified character (case insensitive).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the character sequence ends with the specified character, ignoring case sensitivity.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character that the character sequence is expected to end with, case insensitively.
 * @param callable The Kotlin function (`KFunction`) related to the validation context. Can be `null`.
 * @param parameterName The name of the parameter being validated within the callable. Can be `null`.
 * @param message An optional, custom error message for the validation failure. Default is `null`.
 * @param causeOf An optional transformer that produces a `Throwable` cause for the validation error. Default is `null`.
 * @param cause An optional transformer that creates a secondary `Throwable` for additional validation context. Default is `null`.
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException If the character sequence does not end with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the character sequence ends with the specified character, ignoring case sensitivity.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character to check against the end of the character sequence.
 * @param callable The [KFunction] associated with the validation context, or null if not applicable.
 * @param parameter The [KParameter] related to the validation context, or null if not applicable.
 * @param message An optional message to include in the exception if validation fails, defaulting to "doesn't end with <char>".
 * @param causeOf A [Transformer] for deriving the cause of the exception, or null if not applicable.
 * @param cause A [Transformer] for generating the cause throwable, or null if not applicable.
 * @return The original character sequence if the validation is successful.
 * @throws ValidationFailedException If the character sequence does not end with the specified character (case insensitive).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence ends with the specified character, ignoring case sensitivity.
 * If the validation fails, it throws a `ValidationFailedException` with an optional message and cause.
 *
 * @param char The character to verify as the ending character of the character sequence.
 * @param callableName The name of the callable (e.g., function or method) associated with this validation.
 * @param parameterName The name of the parameter being validated; defaults to `null` if unspecified.
 * @param message An optional custom message to provide additional context about the validation failure; defaults to `null`.
 * @param causeOf A transformation function for deriving a throwable cause for this validation failure; defaults to `null`.
 * @param cause A transformation function for wrapping the validation failure; defaults to `null`.
 * @return The original character sequence if the validation is successful.
 * @throws ValidationFailedException if the character sequence does not end with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the character sequence ends with the specified character, ignoring case sensitivity.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param char The character to check against the end of the character sequence.
 * @param callableName The name of the callable where the validation is being performed, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message to provide additional details when validation fails. Defaults to null.
 * @param causeOf An optional transformer to produce a throwable instance in case validation fails.
 *                Defaults to null, indicating no specific throwable generation.
 * @param cause An optional transformer to produce the underlying cause of the exception. Defaults to null.
 * @return The original character sequence if validation passes.
 * @throws ValidationFailedException If the character sequence does not end with the specified character (case insensitive).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't end with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't end with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current character sequence ends with the specified character sequence, ignoring case.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs the character sequence that this character sequence should end with. Case is ignored during the comparison.
 * @param causeOf an optional transformer that provides a throwable to be thrown if validation fails.
 * @param cause an optional transformer that generates a cause for the thrown exception, used for error context.
 * @return the current character sequence if the validation passes.
 * @throws ValidationFailedException if this character sequence does not end with the specified character sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException("Char sequence doesn't end with $cs.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence doesn't end with $cs.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence ends with the specified character sequence,
 * ignoring case. If the validation fails, an exception is thrown.
 *
 * @param cs the character sequence that the current sequence should end with, ignoring case.
 * @param causeOf an optional transformer that provides a specific exception to throw if validation fails.
 * @param cause an optional transformer that generates the cause of the validation failure.
 * @param lazyMessage a transformer to create the validation error message lazily.
 * @return the same character sequence if validation succeeds.
 * @throws ValidationFailedException if the current character sequence does not end with the specified sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this notEndsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling `CharSequence` ends with the specified `CharSequence` (`cs`),
 * ignoring character case. If the condition is not met, a `ValidationFailedException` is thrown.
 *
 * @param cs The character sequence that the calling sequence must end with, ignoring case.
 * @param property The `KProperty` associated with this validation check, if applicable.
 * @param variableName An optional name of the variable being validated. Used in the error message if provided.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf A transformer to produce the exception to be thrown. Used if the validation fails and this is not null.
 * @param cause An optional transformer used to provide the cause of the exception.
 * @return The calling `CharSequence` if it successfully passes the validation.
 * @throws ValidationFailedException If the calling `CharSequence` does not end with the specified `cs`, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't end with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence ends with the specified character sequence, ignoring case.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param cs the character sequence to check against the end of the current character sequence.
 * @param property the main [KProperty] associated with the validation failure, or null if not specified.
 * @param variable an optional secondary [KProperty] providing additional context, or null if not specified.
 * @param message an optional message providing additional details about the validation failure.
 * @param causeOf a transformer to compute the specific throwable to represent the root cause of the failure, or null if not specified.
 * @param cause a transformer to calculate a fallback cause for the exception, or null if not specified.
 * @return the current character sequence if validation passes.
 * @throws ValidationFailedException if the validation fails, i.e., the current character sequence does not end with [cs] ignoring character case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't end $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling character sequence ends with the specified character sequence `cs`,
 * ignoring case sensitivity. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs The character sequence that the calling character sequence should end with. A case-insensitive
 *           comparison is performed.
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be `null`.
 * @param parameterName The name of the parameter in the callable that this validation applies to. Can be `null`.
 * @param message An optional custom error message to be used if the validation fails. If `null`, a default
 *                message will be used.
 * @param causeOf An optional transformer used to create a custom exception as the cause of failure. This takes
 *                the calling character sequence as input and returns a `Throwable`.
 * @param cause An optional transformer used to generate a custom cause for the validation failure exception
 *              if the validation fails. This takes the calling character sequence as input and returns a `Throwable`.
 * @return Returns the calling character sequence if it ends with `cs`, ignoring case sensitivity.
 * @throws ValidationFailedException If the calling character sequence does not end with `cs`, ignoring case sensitivity.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't end with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence ends with the specified character sequence, ignoring case sensitivity.
 * If the validation fails, a `ValidationFailedException` is thrown with the provided details.
 *
 * @param cs the character sequence to compare against the end of the current character sequence.
 * @param callable the [KFunction] related to the validation, providing additional context for the exception, or `null` if not applicable.
 * @param parameter the [KParameter] being validated, used for error context, or `null` if not applicable.
 * @param message an optional custom error message to include in the exception, or `null` to use a default message.
 * @param causeOf an optional transformer function for generating a cause exception based on the current character sequence, or `null` if not applicable.
 * @param cause an optional transformer function to specify a cause exception for the `ValidationFailedException`, or `null` if not applicable.
 * @return the original character sequence if validation succeeds.
 * @throws ValidationFailedException if the character sequence does not end with the specified sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't end with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling character sequence ends with the specified suffix, ignoring character case.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs the character sequence that the calling object should end with, ignoring case.
 * @param callableName the name of the callable (e.g., function or method) performing the validation. Can be null.
 * @param parameterName the name of the parameter being validated. Can be null.
 * @param message an optional custom message to include in the exception if validation fails. Can be null.
 * @param causeOf an optional transformer function for creating a specific cause exception if validation fails. Can be null.
 * @param cause an optional transformer function for specifying a root cause exception if validation fails. Can be null.
 * @return the calling character sequence if it ends with the specified suffix, ignoring case.
 * @throws ValidationFailedException if the calling character sequence does not end with the specified suffix, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't end with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence ends with the specified character sequence, ignoring case sensitivity.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs The character sequence to compare against the end of the current sequence.
 * @param callableName Optional name of the callable (e.g., function or property) associated with the validation.
 * @param parameter Optional `KParameter` instance representing the parameter being validated.
 * @param message An optional validation failure message. Defaults to a message indicating that the current sequence doesn’t end with the specified sequence.
 * @param causeOf An optional transformer that generates a specific `Throwable` based on the current sequence for custom exception details.
 * @param cause An optional transformer that generates a specific `Throwable` to serve as the cause of the validation failure.
 * @return The current character sequence (`this`) if it passes the validation.
 * @throws ValidationFailedException If the current sequence does not end with the specified sequence, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateEndsWithIgnoreCase(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this notEndsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't end with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't end with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not end with the specified character.
 * Throws a `ValidationFailedException` if the character sequence ends with the specified character.
 *
 * @param char the character to check as the unwanted suffix.
 * @param causeOf an optional transformer that produces a throwable cause based on the input character sequence.
 * @param cause an optional transformer to generate a throwable cause with additional context when validation fails.
 * @return the original character sequence if the validation passes.
 * @throws ValidationFailedException if the character sequence ends with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith char) throw if (causeOf == null) ValidationFailedException("Char sequence ends with $char.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence ends with $char.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not end with the specified character.
 * If the validation fails, an exception is thrown based on the provided parameters.
 *
 * @param char The character that the sequence should not end with.
 * @param causeOf A transformer function to generate an exception, based on the input character sequence, when the validation fails.
 * @param cause A transformer function to generate the cause of an exception when the validation fails.
 * @param lazyMessage A transformer function to generate a custom error message when the validation fails.
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException If the character sequence ends with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this endsWith char) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not end with the specified character.
 * If the sequence ends with the given character, a `ValidationFailedException` is thrown.
 *
 * @param char The character that the sequence should not end with.
 * @param property The property associated with the validation failure. Can be `null` if not applicable.
 * @param variableName Optional name of the variable being validated. Used in the exception message if provided.
 * @param message Optional custom message for the exception if validation fails.
 * @param causeOf An optional transformer that generates a `Throwable` to be thrown as the cause
 *                of the validation failure exception if not `null`.
 * @param cause An optional transformer that generates a `Throwable` to be included as the exception's inner cause if not `null`.
 * @return The original character sequence if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith char) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not end with the specified character.
 * Throws a [ValidationFailedException] if the validation fails.
 *
 * @param char the character that the character sequence should not end with
 * @param property the main property associated with the validation, or null if not specified
 * @param variable an optional secondary property for additional context, or null if not specified
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf a transformer for generating the root cause of the exception if specified
 * @param cause a transformer for creating the throwable cause of the exception, or null if not specified
 * @return the original character sequence if validation passes
 * @throws ValidationFailedException if the character sequence ends with the specified character
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith char) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not end with the specified character.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char The character to check against as a suffix of the given sequence.
 * @param callable The Kotlin function (`KFunction`) associated with the validation. May be null.
 * @param parameterName The name of the parameter related to this validation. May be null.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to a message indicating the sequence ends with the specified character.
 * @param causeOf A transformer used to generate the root cause (`Throwable`) for the exception. May be null.
 * @param cause An additional transformer used to generate a cause (`Throwable`) for the exception. May be null.
 * @return The validated character sequence if it does not end with the specified character.
 * @throws ValidationFailedException if the character sequence ends with the specified character.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith char) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified character.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char the character that the character sequence should not end with
 * @param callable the [KFunction] associated with the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional custom message describing the validation failure; defaults to a generated message if not provided
 * @param causeOf an optional transformer to generate the exception to throw when validation fails, or null
 * @param cause an optional transformer to generate the cause of the failure to be passed into the exception, or null
 * @return the original character sequence if validation passes
 * @throws ValidationFailedException if the character sequence ends with the specified character
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith char) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not end with the specified character.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char the character to check against the end of the character sequence
 * @param callableName the name of the callable (e.g., function or method) related to the validation; can be null
 * @param parameterName the name of the parameter being validated; can be null
 * @param message a custom message to include in the exception if validation fails; can be null
 * @param causeOf a transformer to provide an alternate throwable cause of the validation failure; can be null
 * @param cause a transformer to provide the underlying cause of the validation failure; can be null
 * @return the original character sequence if the validation succeeds
 * @throws ValidationFailedException if the character sequence ends with the specified character
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith char) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not end with the specified character. If the validation fails,
 * a [ValidationFailedException] is thrown with optional details about the failure.
 *
 * @param char the character that the character sequence must not end with
 * @param callableName the name of the callable that triggered this validation, or null if not applicable
 * @param parameter the parameter associated with this validation, or null if not applicable
 * @param message an optional custom message to include in the exception, or null to use the default message
 * @param causeOf an optional transformer to generate the cause of the exception, or null if not applicable
 * @param cause an optional transformer to generate the underlying cause of the exception, or null if not applicable
 * @return the original character sequence if validation passes
 * @throws ValidationFailedException if the character sequence ends with the specified character
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith char) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified character sequence.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param cs The character sequence to check against.
 * @param causeOf A transformer that generates the throwable cause when the validation fails, or null if no custom cause should be provided.
 * @param cause An optional transformer for customizing the underlying cause of the validation failure.
 * @return The original character sequence if the validation passes.
 * @throws ValidationFailedException If the character sequence ends with the specified [cs].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith cs) throw if (causeOf == null) ValidationFailedException("Char sequence ends with $cs.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence ends with $cs.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not end with the specified suffix.
 * If the validation fails, an exception is thrown, optionally with a custom error message
 * and/or a cause.
 *
 * @param cs The character sequence that should not be a suffix of the current character sequence.
 * @param causeOf An optional transformer to generate the exception that will serve as the cause of the thrown exception.
 * @param cause An optional transformer to generate a direct cause for the thrown exception.
 * @param lazyMessage A transformer used to generate the error message if the validation fails.
 * @return The original character sequence if the validation succeeds.
 * @throws ValidationFailedException If the character sequence ends with the specified suffix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this endsWith cs) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not end with the specified value. If the validation fails,
 * a [ValidationFailedException] is thrown with a detailed error message, optionally enhanced with a custom
 * cause or message.
 *
 * @param cs the character sequence that should not match the suffix of the current character sequence.
 * @param property the property associated with the validation logic. Can be null if not applicable.
 * @param variableName the optional name of the variable being validated, included in the error message if provided.
 * @param message an optional additional descriptive message to include in the exception,
 *                defaulting to "ends with [cs]" if not specified.
 * @param causeOf an optional transformer that determines the root cause of the failed validation,
 *                providing a custom [Throwable] if the validation fails.
 * @param cause an optional transformer to generate a cause for the exception, invoked with the current value
 *              if the validation fails.
 * @return the same character sequence if the validation passes.
 * @throws ValidationFailedException if the character sequence ends with the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith cs) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified suffix [cs].
 * If the character sequence ends with [cs], a `ValidationFailedException` is thrown.
 *
 * @param cs the suffix to check for in the character sequence.
 * @param property the primary property associated with the validation context, or null if not specified.
 * @param variable an optional secondary property providing additional context, or null if not specified.
 * @param message an optional custom message to use for the validation failure exception, or null to use the default message.
 * @param causeOf an optional transformer to generate the underlying cause of the exception when validation fails.
 * @param cause an optional transformer to produce a cause for the exception when validation fails.
 * @return the original character sequence, if validation passes without exceptions.
 * @throws ValidationFailedException if the character sequence ends with [cs].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith cs) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not end with the specified suffix.
 * If the validation fails, a `ValidationFailedException` will be thrown.
 *
 * @param cs The character sequence that should not match the end of the receiver.
 * @param callable The Kotlin function (`KFunction`) associated with the validation. Can be null.
 * @param parameterName The name of the parameter in the `callable` to which the validation applies. Can be null.
 * @param message An optional custom message to use in the exception if validation fails. If null, a default message is generated.
 * @param causeOf An optional transformer function that generates a `Throwable` as the cause of the validation failure. Can be null.
 * @param cause An optional transformer function that generates a secondary cause as a `Throwable`. Can be null.
 * @return The receiver character sequence if it passes the validation.
 * @throws ValidationFailedException If the receiver ends with the specified character sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith cs) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified [cs] value.
 * If the validation fails, a [ValidationFailedException] is thrown with the optional additional context provided.
 *
 * @param cs the character sequence that should not match the end of this character sequence.
 * @param callable the [KFunction] related to the validation, used for contextual exception information, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter involved in the validation, or null if not applicable.
 * @param message an optional message providing additional context for the validation failure, or null to use a default message.
 * @param causeOf an optional transformer to compute a cause for the thrown exception from this character sequence, or null if no transformation is required.
 * @param cause an optional transformer to compute an additional cause for the thrown exception from this character sequence, or null if no transformation is required.
 * @return the current character sequence if the validation passes.
 * @throws ValidationFailedException if this character sequence ends with the specified [cs] value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith cs) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not end with the specified suffix.
 * If the validation fails, it throws a [ValidationFailedException] with optional details.
 *
 * @param cs the character sequence acting as the suffix to compare against
 * @param callableName the name of the callable (e.g., function or method) associated with the validation
 * @param parameterName the name of the parameter being validated, or null if not applicable
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf an optional transformer that generates a specific cause of type [Throwable] for the exception
 * @param cause an optional transformer that generates a more contextual cause of type [Throwable] for the exception
 * @return the validated character sequence if it does not end with the specified suffix
 * @throws ValidationFailedException if the character sequence ends with the specified suffix
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith cs) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified suffix.
 * If the validation fails, it throws a [ValidationFailedException].
 *
 * @param cs The character sequence that should not be the suffix of the current instance.
 * @param callableName The name of the callable where the validation occurs, or null if unspecified.
 * @param parameter The parameter being validated, or null if not applicable.
 * @param message An optional custom message to include with the validation failure, or null to use the default message.
 * @param causeOf An optional transformer that generates a throwable cause for the validation failure, or null if not provided.
 * @param cause An optional transformer that generates a throwable for the validation failure, or null if not provided.
 * @return The current character sequence if validation passes.
 * @throws ValidationFailedException If the character sequence ends with the specified [cs].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWith(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWith cs) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not end with the specified character, ignoring case considerations.
 * Throws a `ValidationFailedException` if the character sequence ends with the specified character.
 *
 * @param char the character to check against the end of the character sequence.
 * @param causeOf an optional transformer for creating a specific throwable cause when validation fails.
 * @param cause an optional transformer to provide an additional cause when validation fails.
 * @return the original character sequence if validation passes.
 * @throws ValidationFailedException if the character sequence ends with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException("Char sequence ends with $char.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence ends with $char.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not end with the specified character, ignoring case considerations.
 * If the validation fails, a `ValidationFailedException` is thrown with an optional message and cause.
 *
 * @param char the character to validate against the end of the character sequence
 * @param causeOf an optional transformer to generate the exception to be thrown; if not provided, a default exception is used
 * @param cause an optional transformer to generate the underlying cause of the exception; if not provided, no cause is set
 * @param lazyMessage a transformer to generate the error message when validation fails
 * @return the character sequence itself if the validation succeeds
 * @throws ValidationFailedException if the character sequence ends with the specified character, ignoring case
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(char: Char, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this endsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not end with the specified character, ignoring case considerations.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param char The character that this character sequence should not end with, ignoring case.
 * @param property An optional property associated with the validation context. Can be used for enhanced exception messages.
 * @param variableName The optional name of the variable being validated. Useful for exception messages.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer used to generate a custom cause for the exception.
 * @param cause An optional transformer for the underlying exception cause to be attached to the validation exception.
 * @return The character sequence itself if validation succeeds.
 * @throws ValidationFailedException If the character sequence ends with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(char: Char, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified character, ignoring case considerations.
 * Throws a `ValidationFailedException` if the character sequence ends with the specified character.
 *
 * @param char the character to check at the end of the character sequence
 * @param property the primary `KProperty` related to the validation, or null if not specified
 * @param variable an optional secondary `KProperty` providing additional context for the validation, or null if not specified
 * @param message an optional message providing additional details about the validation failure, or null for a default message
 * @param causeOf an optional transformer to create a throwable cause based on the current character sequence, or null if not specified
 * @param cause another optional transformer to create a throwable cause, or null if not specified
 * @return the original character sequence if the validation passes
 * @throws ValidationFailedException if the character sequence ends with the specified character, ignoring case
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(char: Char, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not end with the specified character, ignoring case considerations.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char the character that should not appear at the end of the character sequence, ignoring case
 * @param callable the Kotlin function (`KFunction`) to which the validation is related, used to generate more detailed exception messages. Can be null
 * @param parameterName the name of the parameter in the callable for which validation is performed. Can be null
 * @param message an optional custom message providing additional details if validation fails. Can be null
 * @param causeOf a transformer function to generate an exception to be thrown as the root cause of the `ValidationFailedException`. Can be null
 * @param cause a transformer function to generate a secondary cause for the `ValidationFailedException`. Can be null
 * @return the character sequence itself if the validation passes
 * @throws ValidationFailedException if the character sequence ends with the specified character, ignoring case considerations
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(char: Char, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not end with the specified character, ignoring case considerations.
 * If the validation fails, it throws a [ValidationFailedException].
 *
 * @param char the character that the end of the character sequence should not match, ignoring case
 * @param callable the [KFunction] associated with the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter involved in the validation, or null if not applicable
 * @param message an optional message to provide additional context in case of a validation failure, defaulting to null
 * @param causeOf an optional transformer function to produce a custom exception, or null to use the default exception
 * @param cause an optional transformer function providing the cause of the validation failure, or null if no cause is specified
 * @return the original character sequence if validation passes
 * @throws ValidationFailedException if the character sequence ends with the specified character, ignoring case
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(char: Char, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence does not end with the specified character, ignoring case considerations.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param char the character that should not appear at the end of the character sequence
 * @param callableName the name of the callable (e.g., function or method) performing the validation
 * @param parameterName an optional name of the parameter being validated, or `null` if not applicable
 * @param message an optional custom message to include in the exception, or `null` to use a default message
 * @param causeOf an optional transformer that generates a throwable to wrap the failure, or `null` if none
 * @param cause an optional transformer that generates the underlying cause of the failure, or `null` if none
 * @return the original character sequence if the validation passes
 * @throws ValidationFailedException if the character sequence ends with the specified character, ignoring case
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(char: Char, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified character, ignoring case considerations.
 *
 * If the character sequence ends with the specified character (case-insensitive), a [ValidationFailedException]
 * is thrown with details about the validation failure. The exception can optionally include additional context
 * via a custom message or cause transformer.
 *
 * @param char The character to check against the end of the character sequence.
 * @param callableName The name of the callable where the validation is being performed, or null if not specified.
 * @param parameter The parameter related to the validation, or null if not applicable.
 * @param message An optional custom error message to include in the exception if the validation fails.
 * @param causeOf A transformer that produces the exception to be thrown when validation fails, or null to use the default exception.
 * @param cause A transformer that provides the underlying cause of the exception, or null if no cause is specified.
 * @return The original character sequence if validation succeeds.
 * @throws ValidationFailedException If the character sequence ends with the specified character, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(char: Char, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase char) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "ends with $char", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "ends with $char", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified character sequence,
 * ignoring case. If the validation fails, a `ValidationFailedException` is thrown with the provided
 * cause or a generated message.
 *
 * @param cs The character sequence to check as the suffix to validate against.
 * @param causeOf A transformer that generates a throwable based on the current sequence when validation fails,
 *                or `null` if no transformation is required.
 * @param cause A transformer that generates the root cause throwable based on the current sequence when validation fails,
 *              or `null` if no underlying root cause throwable is provided.
 * @return The current character sequence if the validation passes.
 * @throws ValidationFailedException If the character sequence ends with the specified suffix, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException("Char sequence ends with $cs.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Char sequence ends with $cs.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified character sequence,
 * ignoring case during the check. If the validation fails, an exception is thrown.
 *
 * @param cs the character sequence that the current character sequence should not end with.
 * @param causeOf an optional transformer that generates a throwable cause when the validation fails.
 * @param cause an optional transformer that generates a throwable cause directly when the validation fails.
 * @param lazyMessage a transformer function to generate the error message lazily based on the current character sequence.
 * @return the current character sequence if validation passes.
 * @throws ValidationFailedException if the current character sequence ends with the specified character sequence, regardless of case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(cs: CharSequence, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this endsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not end with the specified suffix, ignoring case.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs The suffix that the character sequence should not end with.
 * @param property The associated KProperty for additional validation context. Can be null if not applicable.
 * @param variableName The name of the variable being validated. Used for descriptive error messages. Can be null.
 * @param message A custom validation failure message. If null, a default message will be used.
 * @param causeOf An optional transformer to generate a throwable cause for the exception when validation fails.
 * @param cause An optional transformer used to generate the root cause of the exception when validation fails.
 * @return The original character sequence if validation succeeds.
 * @throws ValidationFailedException If the character sequence ends with the specified suffix, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(cs: CharSequence, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified suffix, ignoring case.
 * If the character sequence ends with the specified suffix, a `ValidationFailedException` is thrown.
 *
 * @param cs the character sequence to check as the suffix
 * @param property the main `KProperty` associated with the validation failure, or `null` if not specified
 * @param variable an optional secondary `KProperty` that provides additional context, or `null` if not specified
 * @param message an optional custom validation failure message, or `null` to use a default message
 * @param causeOf an optional `Transformer` function to generate the cause exception, or `null` if not specified
 * @param cause an optional `Transformer` function to specify the cause of the validation failure, or `null` if not specified
 * @return the current character sequence if the validation passes
 * @throws ValidationFailedException if the character sequence ends with the specified suffix, ignoring case
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(cs: CharSequence, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given character sequence does not end with the specified suffix, ignoring case.
 * If the validation fails, it throws a `ValidationFailedException`.
 *
 * @param cs The character sequence to check as a suffix.
 * @param callable The Kotlin function (`KFunction`) related to the validation; used for exception metadata. Can be null.
 * @param parameterName The name of the parameter being validated; used for exception metadata. Can be null.
 * @param message An optional custom message for the exception. If not provided, a default message is used.
 * @param causeOf A transformer function to create a `Throwable` representing the cause of the failure.
 * @param cause A transformer function to generate the root cause `Throwable` for the exception.
 * @return The validated character sequence if it does not end with the specified suffix (ignoring case).
 * @throws ValidationFailedException if the character sequence ends with the specified suffix.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified character sequence, ignoring case.
 * Throws a [ValidationFailedException] if the validation fails.
 *
 * @param cs the character sequence to check as a suffix
 * @param callable the [KFunction] representing the callable context of the validation, or null if not applicable
 * @param parameter the [KParameter] representing the parameter involved in the validation, or null if not applicable
 * @param message an optional custom message to include if validation fails, defaults to null
 * @param causeOf a transformer function that maps the current value to a [Throwable] used as the root cause of the failure,
 *        or null if no custom root cause is specified
 * @param cause a transformer function that maps the current value to a [Throwable] as an additional cause, or null if no
 *        additional cause is specified
 * @return the current character sequence if validation passes without throwing an exception
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(cs: CharSequence, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified character sequence,
 * ignoring case sensitivity. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param cs the character sequence that should not be present as a suffix
 * @param callableName the name of the callable (e.g., function or method) invoking this validation
 * @param parameterName the name of the parameter being validated, or null if not applicable
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf an optional transformer to generate the underlying cause of the exception
 * @param cause an optional transformer to provide additional context for the exception
 * @return the same character sequence if the validation succeeds
 * @throws ValidationFailedException if the current character sequence ends with the specified character sequence, ignoring case sensitivity
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(cs: CharSequence, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current character sequence does not end with the specified character sequence,
 * ignoring case sensitivity during comparison. If the validation fails, a `ValidationFailedException`
 * is thrown with the specified details.
 *
 * @param cs The character sequence that should not match the end of the current character sequence.
 * @param callableName The name of the callable (e.g., function or property) related to this validation, or null if not specified.
 * @param parameter The parameter associated with the callable being validated, or null if not applicable.
 * @param message An optional custom error message for the validation failure; defaults to a generic message if not provided.
 * @param causeOf An optional transformer that provides a customized throwable when validation fails.
 * @param cause An optional transformer for generating a nested cause throwable for the validation failure.
 * @return The original character sequence if it does not end with the specified character sequence, ignoring case.
 * @throws ValidationFailedException If the current character sequence ends with the specified one, ignoring case.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateNotEndsWithIgnoreCase(cs: CharSequence, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this endsWithIgnoreCase cs) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "ends with $cs", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "ends with $cs", cause?.invoke(this)))
    return this
}

/**
 * Validates if the string is entirely uppercase. If the string is not
 * uppercase, a validation exception is thrown. Optionally, transformers
 * for customizing the exception and its cause can be provided.
 *
 * @param causeOf a transformer to generate a custom `Throwable` as the primary
 *                cause of the exception. Null if default exception behavior
 *                is preferred.
 * @param cause a transformer to generate a custom `Throwable` as the underlying
 *              cause of the exception. Null if no underlying cause is needed.
 * @return the original string if the string is uppercase.
 * @throws ValidationFailedException if the string is not uppercase.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateUppercase(causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isUpperCase) throw if (causeOf == null) ValidationFailedException("The string is not uppercase.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The string is nuot uppercase.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the invoking string contains only uppercase characters.
 *
 * @param causeOf An optional transformer function for creating a custom `Throwable` cause when validation fails.
 * @param cause An optional transformer function for creating an underlying cause of the validation failure.
 * @param lazyMessage A transformer function to generate a lazy error message when validation fails.
 * @return The original string if the validation is successful.
 * @throws ValidationFailedException If the string is not entirely uppercase.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateUppercase(causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null, lazyMessage: Transformer<String, Any>): String {
    if (!isUpperCase) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the string is in uppercase. If the validation fails, throws a `ValidationFailedException`.
 *
 * @param property The property associated with the validation, used for constructing the exception message. Can be null if not applicable.
 * @param variableName Optional name of the variable being validated. Can be null if not applicable.
 * @param message Additional descriptive message explaining the validation failure. Defaults to "is not uppercase" if null.
 * @param causeOf A transformer function to create a custom throwable as the cause of the exception. Can be null if not applicable.
 * @param cause A transformer function to specify the underlying cause of the exception. Can be null if not applicable.
 * @return The original string if it passes the validation.
 * @throws ValidationFailedException if the string is not in uppercase.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateUppercase(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isUpperCase) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not uppercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not uppercase", cause?.invoke(this)))
    return this
}
/**
 * Validates if the string is entirely in uppercase. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param property the primary `KProperty` associated with the validation, providing context about the value being validated. Can be null.
 * @param variable an optional secondary `KProperty` providing additional context about the validation. Can be null.
 * @param message an optional custom error message to include in the validation exception if validation fails. Defaults to "is not uppercase".
 * @param causeOf a transformer function that takes the current string and returns a `Throwable` as the cause of the exception. Can be null.
 * @param cause an alternative transformer function that takes the current string and produces a `Throwable` to be used as the cause. Can be null.
 * @return the original string if validation is successful.
 * @throws ValidationFailedException if the string is not entirely uppercase, with optional details provided by the parameters.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateUppercase(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isUpperCase) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not uppercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not uppercase", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the string is in uppercase and throws a `ValidationFailedException` if the condition is not met.
 *
 * @param callable The Kotlin function (`KFunction`) related to the validation. Can be null.
 * @param parameterName The name of the parameter being validated, usually related to `callable`. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to "is not uppercase".
 * @param causeOf A transformer function to produce a custom `Throwable` if validation fails. Can be null.
 * @param cause A transformer function to produce the root cause of the validation failure. Can be null.
 * @return The original string if the validation passes without throwing an exception.
 * @throws ValidationFailedException If the string is not in uppercase.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateUppercase(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isUpperCase) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not uppercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not uppercase", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current string is in uppercase. If the string is not in uppercase, a
 * [ValidationFailedException] is thrown. Returns the string itself if validation passes.
 *
 * @param callable the [KFunction] associated with the validation, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional error message to include in the exception if validation fails
 * @param causeOf an optional transformer to generate a custom exception from the string causing the validation failure
 * @param cause an optional transformer to generate a cause for the exception
 * @return the validated string if it successfully meets the uppercase requirement
 * @throws ValidationFailedException if the string is not in uppercase
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateUppercase(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isUpperCase) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not uppercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not uppercase", cause?.invoke(this)))
    return this
}
/**
 * Validates if the string is entirely uppercase. If the string is not uppercase, a `ValidationFailedException`
 * is thrown using the provided parameters for detailed failure information.
 *
 * @param callableName the name of the callable (e.g., function or method) related to the validation
 * @param parameterName an optional name of the parameter that caused the validation failure
 * @param message an optional custom message providing additional details for the validation failure
 * @param causeOf an optional transformer function to generate an exception representing the cause of the validation failure
 * @param cause an optional transformer function to generate an underlying cause of the validation failure
 * @return the validated string if it is entirely uppercase
 * @throws ValidationFailedException if the string is not uppercase
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateUppercase(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isUpperCase) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not uppercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not uppercase", cause?.invoke(this)))
    return this
}
/**
 * Validates that the string is in uppercase. Throws a `ValidationFailedException` if the string is not uppercase.
 *
 * @param callableName The name of the callable (e.g., function or property) where the validation is applied. Can be null.
 * @param parameter The `KParameter` instance representing the parameter being validated. Can be null.
 * @param message An optional error message to include in the exception if validation fails. Defaults to "is not uppercase".
 * @param causeOf A transformer function that converts the string into a `Throwable`, used as the exception cause. Can be null.
 * @param cause A transformer function that converts the string into a `Throwable` to represent the underlying cause of validation failure. Can be null.
 * @return The original string if it is uppercase.
 * @throws ValidationFailedException if the string is not in uppercase.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateUppercase(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isUpperCase) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not uppercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not uppercase", cause?.invoke(this)))
    return this
}

/**
 * Validates that the string is entirely in lowercase. Throws a `ValidationFailedException`
 * if the string is not lowercase. Custom transformers can be provided to generate
 * exceptions with additional details or context.
 *
 * @param causeOf a transformer that generates a throwable based on the input string,
 * used to customize the thrown exception. Defaults to null.
 * @param cause a transformer that generates a throwable providing a cause for
 * the validation failure. Defaults to null.
 * @return the current string if validation passes.
 * @throws ValidationFailedException if the string is not entirely in lowercase.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateLowercase(causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isLowerCase) throw if (causeOf == null) ValidationFailedException("The string is not lowercase.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The string is nuot lowercase.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the string is entirely in lowercase. If the string is not
 * in lowercase, a `ValidationFailedException` will be thrown with the provided
 * custom error message and optional cause.
 *
 * @param causeOf a transformer that generates a throwable to be thrown as
 *                the primary cause of the error. Defaults to null if not provided.
 * @param cause a transformer that generates a throwable to be used as the
 *              underlying cause for the exception. Defaults to null if not provided.
 * @param lazyMessage a transformer that generates the error message when the validation
 *                    fails. Accepts the string being validated as input.
 * @return the validated string if it passes the lowercase check.
 * @throws ValidationFailedException if the string contains any characters that are not
 *                                   lowercase.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateLowercase(causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null, lazyMessage: Transformer<String, Any>): String {
    if (!isLowerCase) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the string is entirely in lowercase. If the validation fails, a `ValidationFailedException`
 * is thrown with the provided details.
 *
 * @param property The property associated with the validation, used for detailed error reporting. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Included in the error message if provided.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf A transformer function to generate a custom `Throwable` as the primary exception for the failure.
 * @param cause A transformer function to generate a custom `Throwable` as the cause of the failure.
 * @return The original string, if validation succeeds, ensuring it is in lowercase.
 * @throws ValidationFailedException if the string is not entirely in lowercase.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateLowercase(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isLowerCase) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not lowercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not lowercase", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current string is in lowercase. If the string is not in lowercase, this method throws a `ValidationFailedException`.
 *
 * @param property the primary property associated with the validation, or null if none is specified
 * @param variable an optional secondary property providing additional context, or null if none is specified
 * @param message an optional custom error message to include in the exception, or null to use the default message
 * @param causeOf an optional transformer to map the string being validated into a throwable to be used as the primary cause; null if not specified
 * @param cause an optional transformer to provide the underlying cause of the exception; null if not specified
 * @return the original string if validation is successful
 * @throws ValidationFailedException if the string is not in lowercase
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateLowercase(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isLowerCase) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not lowercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not lowercase", cause?.invoke(this)))
    return this
}
/**
 * Validates if the string is entirely in lowercase. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter in the given callable being validated. Can be null.
 * @param message An optional custom validation failure message. If null, a default message is used.
 * @param causeOf A transformer function that returns a `Throwable` representing the root cause of the validation failure. Can be null.
 * @param cause A transformer function that returns a `Throwable` as the cause of a validation failure exception. Can be null.
 * @return The original string if the validation succeeds.
 * @throws ValidationFailedException if the string is not entirely in lowercase.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateLowercase(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isLowerCase) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not lowercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not lowercase", cause?.invoke(this)))
    return this
}
/**
 * Validates if the string is entirely in lowercase. If the validation fails, it throws a [ValidationFailedException].
 *
 * @param callable the [KFunction] related to the validation context, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable.
 * @param message an optional custom message to include in the exception when the validation fails. Defaults to "is not lowercase".
 * @param causeOf an optional transformer function to generate the cause of the exception based on the input string, or null if not applicable.
 * @param cause an optional transformer function to generate an additional cause for the exception based on the input string, or null if not applicable.
 * @return the validated string if it is entirely in lowercase.
 * @throws ValidationFailedException if the string is not entirely in lowercase.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateLowercase(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isLowerCase) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not lowercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not lowercase", cause?.invoke(this)))
    return this
}
/**
 * Validates that the string is entirely in lowercase. If the validation fails, a `ValidationFailedException`
 * is thrown with the provided details.
 *
 * @param callableName The name of the callable (e.g., function or method) invoking the validation. This is used for context in the exception.
 * @param parameterName The name of the parameter being validated, if applicable. This is used for context in the exception.
 * @param message An optional custom validation failure message that describes the error.
 * @param causeOf A transformer that converts the string being validated into a cause exception, used as the root cause of the thrown exception.
 * @param cause A transformer that converts the string being validated into a cause exception, passed to the resulting exception.
 * @return The string itself if validation is successful.
 * @throws ValidationFailedException If the string is not fully in lowercase, this exception is triggered with the provided details.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateLowercase(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isLowerCase) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not lowercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not lowercase", cause?.invoke(this)))
    return this
}
/**
 * Validates that the string is completely in lowercase.
 *
 * If the string is not lowercase, a `ValidationFailedException` is thrown with a message or cause if provided.
 *
 * @param callableName The name of the callable where the validation is being performed, or null if not specified.
 * @param parameter The `KParameter` associated with the validation, or null if not applicable.
 * @param message An optional custom validation error message.
 * @param causeOf A transformer that generates a `Throwable` exception to be thrown if validation fails. If null, a `ValidationFailedException` will be thrown instead.
 * @param cause A transformer that generates the underlying cause for the exception. If null, no additional cause will be attached.
 * @return The original string if it passes the lowercase validation.
 * @throws ValidationFailedException if the validation fails and no `causeOf` transformer is provided.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun String.validateLowercase(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<String, Throwable>? = null, cause: Transformer<String, Throwable>? = null): String {
    if (!isLowerCase) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not lowercase", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not lowercase", cause?.invoke(this)))
    return this
}

/**
 * Checks that the length of the CharSequence is equal to the specified length.
 * If the length does not match, an exception is thrown.
 *
 * @param length The expected length of the CharSequence.
 * @param causeOf Optional transformer to generate a custom exception based on the CharSequence.
 * @param cause Optional transformer to generate a cause for the exception.
 * @return The original CharSequence if the length matches the expected value.
 * @throws ExpectationMismatchException if the length of the CharSequence does not match the expected length.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectLength(length: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length != length) throw if (causeOf == null) ExpectationMismatchException("The string is not of length $length.", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException("The string is not of length $length.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the character sequence has the expected length. If the length does not match,
 * an exception is thrown based on the provided parameters.
 *
 * @param length The expected length of the character sequence.
 * @param causeOf An optional transformer used to produce a specific cause of the exception, or null if not needed.
 * @param cause An optional transformer used to produce the root cause of the exception, or null if not needed.
 * @param lazyMessage A transformer used to generate a message to describe the expectation mismatch.
 * @return The original character sequence if the length matches the expectation.
 * @throws ExpectationMismatchException If the character sequence length does not match the expected length.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectLength(length: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this.length != length) throw if (causeOf == null) ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the length of the current `CharSequence` matches the specified `length`.
 * If the length does not match, an `ExpectationMismatchException` is thrown.
 *
 * @param length The expected length of the `CharSequence`.
 * @param property An optional `KProperty` associated with this validation, used for error description.
 * @param variableName An optional variable name to include in the error message for clarity.
 * @param message An optional custom error message to override the default one.
 * @param causeOf An optional transformer to provide a custom throwable that describes the cause of failure.
 * @param cause An optional transformer to generate the root cause throwable for the exception.
 * @return The original `CharSequence` instance if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectLength(length: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length != length) throw if (causeOf == null) ExpectationMismatchException(property, variableName, message ?: "is not of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variableName, message ?: "is not of length $length", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the length of the given `CharSequence` matches the specified expected length.
 * Throws an `ExpectationMismatchException` if the length does not match.
 *
 * @param T The type of `CharSequence` being evaluated.
 * @param length The expected length of the `CharSequence`.
 * @param property The primary property being validated. Can be null.
 * @param variable An additional variable associated with the validation. Can be null.
 * @param message An optional message to use in the exception if the expectation fails. Defaults to `null`.
 * @param causeOf An optional transformer that generates the root cause for the exception based on the input. Defaults to `null`.
 * @param cause An optional transformer for generating additional exception context based on the input. Defaults to `null`.
 * @return The original `CharSequence` if its length matches the expected length.
 * @throws ExpectationMismatchException if the length of the `CharSequence` doesn't match the expected length.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectLength(length: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length != length) throw if (causeOf == null) ExpectationMismatchException(property, variable, message ?: "is not of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variable, message ?: "is not of length $length", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the length of the current character sequence matches the expected length.
 * If the length does not match the expectation, an `ExpectationMismatchException` is thrown.
 *
 * @param length The expected length of the character sequence.
 * @param callable An optional reference to the callable function that triggered this check. If provided, it will be included in the exception for context.
 * @param parameterName The optional name of the parameter associated with the expectation check. Included in the exception description if specified.
 * @param message An optional custom message to include in the exception if the expectation fails. Defaults to a standard message if not provided.
 * @param causeOf An optional transformer to generate an exception that wraps the `ExpectationMismatchException` if the expectation fails.
 * @param cause An optional transformer to generate the root cause exception if the expectation fails.
 * @return The original character sequence (`this`) if the length matches the expected value.
 * @throws ExpectationMismatchException If the length of the character sequence does not match the expected length.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectLength(length: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length != length) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, message ?: "is not of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameterName, message ?: "is not of length $length", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the `CharSequence` has the expected length. If the length does not match,
 * an `ExpectationMismatchException` is thrown with detailed context.
 *
 * @param length The expected length of the `CharSequence`.
 * @param callable The function under evaluation, typically used for contextual debug information, or null.
 * @param parameter The parameter of the function being evaluated, or null.
 * @param message A custom error message to include in the exception, or null.
 * @param causeOf A transformer function to generate a cause of the exception, if needed, or null.
 * @param cause A transformer function to create an optional cause for the exception, or null.
 * @return The original `CharSequence` if the length matches the expected length.
 * @throws ExpectationMismatchException If the length of the `CharSequence` differs from the expected value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectLength(length: Int, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length != length) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, message ?: "is not of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameter, message ?: "is not of length $length", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the length of the current `CharSequence` matches the specified value.
 * If the length does not match, throws an `ExpectationMismatchException` with the appropriate details.
 *
 * @param length The expected length of the `CharSequence`.
 * @param callableName The name of the callable function where the expectation is being checked, or null if unspecified.
 * @param parameterName The name of the parameter associated with the expectation, or null if unspecified.
 * @param message A custom message to include in the exception when the expectation fails, or null to use the default message.
 * @param causeOf A transformer that generates the root cause for the exception, or null if no specific root cause is needed.
 * @param cause A transformer that generates additional exception details, or null if no additional details are needed.
 * @return The original `CharSequence` if the length matches the specified value.
 * @throws ExpectationMismatchException If the length of the `CharSequence` does not match the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectLength(length: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length != length) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, message ?: "is not of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameterName, message ?: "is not of length $length", cause?.invoke(this)))
    return this
}
/**
 * Validates that the length of the current [CharSequence] matches the expected [length].
 * If the length does not match, an [ExpectationMismatchException] is thrown.
 *
 * @param length The expected length of the [CharSequence].
 * @param callableName The name of the callable function being evaluated, or null if not applicable.
 * @param parameter The parameter of the callable function being validated, or null.
 * @param message An optional custom message for the exception if the expectation is not met.
 * @param causeOf A transformer function that generates the root cause of the exception, or null.
 * @param cause An optional transformer function that provides a secondary cause for the exception, or null.
 * @return The original [CharSequence] instance if the length matches the expected value.
 * @throws ExpectationMismatchException if the length of the [CharSequence] does not match the expected [length].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectLength(length: Int, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length != length) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, message ?: "is not of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameter, message ?: "is not of length $length", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the length of the given CharSequence is not equal to the specified value.
 * If the length matches the specified value, an exception will be thrown.
 *
 * @param length The length that the CharSequence should not have.
 * @param causeOf An optional transformer that produces a Throwable cause based on the given CharSequence.
 * @param cause An optional transformer that provides an additional cause for the thrown exception.
 * @return The original CharSequence if the length does not match the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectNotLength(length: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length == length) throw if (causeOf == null) ExpectationMismatchException("The string is of length $length.", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException("The string is of length $length.", cause?.invoke(this)))
    return this
}
/**
 * Asserts that the length of the current CharSequence is not equal to the specified value.
 * Throws an [ExpectationMismatchException] if the length matches the given value.
 *
 * @param length The expected length that the current CharSequence should not match.
 * @param causeOf An optional transformer that generates a cause exception based on the current CharSequence.
 * @param cause An optional transformer that provides a cause exception based on the current CharSequence.
 * @param lazyMessage A transformer for composing the exception message dynamically, based on the current CharSequence.
 * @return The original CharSequence if its length does not match the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectNotLength(length: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this.length == length) throw if (causeOf == null) ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Verifies that the receiver [CharSequence] does not have the specified [length]. If the length matches,
 * throws an [ExpectationMismatchException].
 *
 * @param length the length to check against the receiver's length.
 * @param property an optional [KProperty] associated with the receiver, providing context for the exception if thrown.
 * @param variableName an optional name of the variable being validated to include in the exception message for better context.
 * @param message an optional custom message to include in the exception if the expectation fails.
 * @param causeOf an optional transformer function to generate a specific throwable cause when the expectation fails.
 * @param cause an optional transformer function to customize the cause when the expectation fails.
 * @return the receiver [CharSequence] if its length is not equal to the specified [length].
 * @throws ExpectationMismatchException if the receiver's length is equal to the specified [length].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectNotLength(length: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length == length) throw if (causeOf == null) ExpectationMismatchException(property, variableName, message ?: "is of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variableName, message ?: "is of length $length", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the length of the given CharSequence is not equal to the specified value. If the length
 * matches the specified value, an `ExpectationMismatchException` is thrown.
 *
 * @param length The length to check against the CharSequence.
 * @param property The primary property being evaluated. May be null.
 * @param variable An optional variable related to the property. May be null.
 * @param message An optional custom message to use in the exception. Defaults to null.
 * @param causeOf An optional transformer that generates a throwable to chain as the cause. Defaults to null.
 * @param cause An optional transformer to produce an alternative throwable to chain as the cause. Defaults to null.
 * @return The CharSequence on which the check was performed, if no exception was thrown.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectNotLength(length: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length == length) throw if (causeOf == null) ExpectationMismatchException(property, variable, message ?: "is of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variable, message ?: "is of length $length", cause?.invoke(this)))
    return this
}
/**
 * Validates that the length of the current `CharSequence` is not equal to the specified length.
 * If the length matches the specified value, an `ExpectationMismatchException` is thrown.
 *
 * @param length The expected length that the current `CharSequence` should not match.
 * @param callable The callable function associated with the validation, or null if unspecified.
 * @param parameterName The name of the parameter being validated, or null if unspecified.
 * @param message The custom message for the exception, or null to use the default message.
 * @param causeOf A transformer function to generate the root cause of the exception, or null if no transformation is needed.
 * @param cause A transformer function to generate a secondary cause for the exception, or null if no transformation is needed.
 * @return The current instance of `CharSequence` if its length does not match the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectNotLength(length: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length == length) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, message ?: "is of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameterName, message ?: "is of length $length", cause?.invoke(this)))
    return this
}
/**
 * Validates that the length of a given `CharSequence` is not equal to the specified `length`.
 * If the length matches the specified value, an `ExpectationMismatchException` is thrown.
 *
 * @param length The length to check against the `CharSequence`.
 * @param callable The callable function (if any) associated with this validation, or null.
 * @param parameter The specific parameter of the callable function to which the validation applies, or null.
 * @param message Optional custom message to include in the exception when the validation fails.
 * @param causeOf Optional transformer to generate the root cause of the exception dynamically, or null.
 * @param cause Optional transformer to generate the cause of the exception dynamically, or null.
 * @return The original `CharSequence` if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectNotLength(length: Int, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length == length) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, message ?: "is of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameter, message ?: "is of length $length", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the receiving [CharSequence] does not have the specified length.
 * If the length of the receiver matches the specified [length], an [ExpectationMismatchException] is thrown.
 *
 * @param length The length to compare against the receiver's length.
 * @param callableName The name of the callable function associated with this check, or null if unspecified.
 * @param parameterName The name of the parameter associated with this check, or null if unspecified.
 * @param message A custom error message to include in the exception, or null to use the default message.
 * @param causeOf A transformer function to generate the root cause of the exception, or null if not applicable.
 * @param cause A transformer function to generate an additional cause for the exception, or null if not applicable.
 * @return The receiver [CharSequence], ensuring it can be used fluently in further operations.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectNotLength(length: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length == length) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, message ?: "is of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameterName, message ?: "is of length $length", cause?.invoke(this)))
    return this
}
/**
 * Ensures the current `CharSequence` does not have the specified length.
 * If the length matches, an `ExpectationMismatchException` is thrown.
 *
 * @param length The length to validate against the current `CharSequence`.
 * @param callableName The name of the function or callable being validated, or null.
 * @param parameter The parameter of the callable being validated, or null.
 * @param message Optional custom error message to include in the exception.
 * @param causeOf A transformer function that derives a throwable based on this `CharSequence`.
 * @param cause A transformer function that provides an underlying cause for the exception.
 * @return Returns the original `CharSequence` if it does not match the specified length.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.expectNotLength(length: Int, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.length == length) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, message ?: "is of length $length", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameter, message ?: "is of length $length", cause?.invoke(this)))
    return this
}