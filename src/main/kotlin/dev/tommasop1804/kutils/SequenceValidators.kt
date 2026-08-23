/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
@file:JvmName("SequenceValidatorsKt")
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
 * Validates that the sequence is not empty.
 *
 * This function checks if the sequence contains no elements. If the sequence is empty,
 * it throws a `ValidationFailedException` with an optional cause specified by the provided
 * throwable suppliers. If the sequence is not empty, the original sequence is returned.
 *
 * @param causeOf a supplier for providing a specific throwable to be thrown; may be null.
 * @param cause a supplier for providing the underlying cause of the exception; may be null.
 * @return the original sequence if it is not empty.
 * @throws ValidationFailedException if the sequence is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty) throw if (causeOf == null) ValidationFailedException("The sequence is empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The sequence is empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is not empty. If the sequence is empty, it throws a `ValidationFailedException`
 * with an optional cause and message supplied by the provided suppliers.
 *
 * @param causeOf an optional supplier for a specific cause to be used as the root `Throwable`. If `null`, a default
 *                cause is created using `ValidationFailedException`.
 * @param cause an optional supplier for an additional nested cause to be attached to the exception.
 * @param lazyMessage a supplier for the lazy-evaluated message to be used in the exception if validation fails.
 * @return the original sequence if the validation is successful (i.e., the sequence is not empty).
 * @throws ValidationFailedException if the sequence is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isEmpty) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is not empty.
 *
 * If the sequence is empty, it throws a `ValidationFailedException`. The exception message can
 * include details about the property, variable name, a custom message, and the cause of the failure.
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName The name of the variable being validated. Included in the exception message if provided.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to "is empty".
 * @param causeOf A supplier for the primary cause of the exception, if applicable. Can be null.
 * @param cause A supplier for an additional cause to associate with the exception. Can be null.
 * @return The same sequence that was validated, if it is not empty.
 * @throws ValidationFailedException If the sequence is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is not empty. If the sequence is empty, a `ValidationFailedException` is thrown.
 *
 * @param property the primary property associated with the validation, used for generating contextual information in the exception
 * @param variable an optional secondary property providing additional context, used for detailed exception messages
 * @param message an optional custom error message describing the validation failure
 * @param causeOf a supplier providing a throwable to be used as the main cause of the exception; if null, a default exception is constructed
 * @param cause a supplier providing an additional cause to be attached to the generated `ValidationFailedException`
 * @return the sequence itself if validation passes, allowing for method chaining
 * @throws ValidationFailedException if the sequence is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is not empty. If the sequence is empty, this method throws a
 * ValidationFailedException with the provided details.
 *
 * @param callable The Kotlin function (`KFunction`) to which this validation is related. Can be null.
 * @param parameterName The name of the parameter in the specified callable being validated. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to "is empty".
 * @param causeOf A supplier for an exception to be thrown as the root cause if validation fails. Can be null.
 * @param cause A supplier for an additional cause to include in the ValidationFailedException. Can be null.
 * @return The original sequence if the validation passes.
 * @throws ValidationFailedException If the sequence is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is not empty. If the sequence is empty, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] related to the validation, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter involved in the validation, or null if not applicable.
 * @param message an optional message providing additional context for the validation failure. Defaults to null.
 * @param causeOf an optional supplier for the exception to be thrown if the validation fails. Defaults to null.
 * @param cause an optional supplier for the underlying cause of the validation failure. Defaults to null.
 * @return the validated sequence if it is not empty.
 * @throws ValidationFailedException if the sequence is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is not empty. If the sequence is empty, a `ValidationFailedException` is thrown.
 *
 * @param callableName the name of the callable (e.g., function or method) related to the validation failure. Can be `null`.
 * @param parameterName the name of the parameter that caused the validation failure. Can be `null`.
 * @param message an optional custom message providing additional details about the validation failure. Defaults to "is empty" if `null`.
 * @param causeOf a supplier for the primary exception that serves as the root cause. Can be `null`.
 * @param cause a supplier for a secondary exception that becomes an underlying cause. Can be `null`.
 * @return the same sequence if it is not empty.
 * @throws ValidationFailedException if the sequence is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is not empty. If the sequence is empty, throws a `ValidationFailedException`.
 *
 * @param callableName The name of the callable where the validation is performed, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional custom error message to use if validation fails. Defaults to "is empty".
 * @param causeOf A supplier for the specific `Throwable` to throw instead of the default exception, or null if not used.
 * @param cause A supplier for the underlying cause of the validation failure, or null if there is no underlying cause.
 * @return The original sequence if it is not empty.
 * @throws ValidationFailedException if the sequence is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is not null or empty. If the validation fails, a
 * `ValidationFailedException` is thrown with an optional cause.
 *
 * @param causeOf an optional supplier that provides a throwable to be used as the
 *                primary cause of the validation failure. If null, a default exception
 *                is created.
 * @param cause an optional supplier that provides a throwable which can be linked
 *              as an additional cause to provide more context for the exception.
 * @return the validated sequence if it is not null or empty.
 * @throws ValidationFailedException if the sequence is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNotNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty) throw if (causeOf == null) ValidationFailedException("The sequence is null or empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The sequence is null or empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given sequence is neither `null` nor empty.
 * If the sequence is `null` or empty, a `ValidationFailedException` is thrown.
 *
 * @param causeOf an optional supplier for a `Throwable` that will serve as the main cause if validation fails.
 * @param cause an optional supplier for a `Throwable` that will be attached as the underlying cause for the validation failure.
 * @param lazyMessage a supplier for the error message to be used in the `ValidationFailedException`, constructed lazily.
 * @return the validated sequence, if it is neither `null` nor empty.
 * @throws ValidationFailedException if the sequence is `null` or empty, with the provided error message and cause(s).
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNotNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is neither null nor empty. If the validation fails, a `ValidationFailedException`
 * is thrown with the provided details.
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName An optional name of the variable involved in the validation. Used in the error message if provided.
 * @param message An optional custom message to include in the exception. Defaults to "is null or empty".
 * @param causeOf A supplier for the throwable that should be used as the base exception. If null, a default exception is generated.
 * @param cause A supplier for the throwable that should be used as the direct cause of the validation exception. Can be null.
 * @return The sequence itself if validation passes successfully.
 * @throws ValidationFailedException if the sequence is null or empty, with the provided or default details.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNotNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is not null or empty. If the sequence is null or empty, throws a
 * `ValidationFailedException` with an optionally provided message and cause.
 *
 * @param property the primary property associated with the validation failure, or null if not specified
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional message explaining the validation failure; defaults to "is null or empty" if not specified
 * @param causeOf an optional supplier for a custom exception to be thrown if the validation fails; defaults to null
 * @param cause an optional supplier for the underlying cause of the exception; defaults to null
 * @return the original sequence if the validation succeeds
 * @throws ValidationFailedException if the sequence is null or empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNotNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is not `null` or empty. If the validation fails, throws a `ValidationFailedException`.
 *
 * @param callable An optional Kotlin function (`KFunction`) reference associated with the validation context.
 * @param parameterName The name of the parameter being validated. Can be `null` if not applicable.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional supplier for the exception's primary cause if validation fails.
 * @param cause An optional supplier for the additional cause to be set in the exception if validation fails.
 * @return The validated sequence, if it is not `null` or empty.
 * @throws ValidationFailedException If the sequence is `null` or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a sequence is neither `null` nor empty.
 *
 * Throws a [ValidationFailedException] if the sequence is `null` or empty.
 *
 * @param callable the [KFunction] related to the validation, providing context about the function in which the validation occurs; may be `null`.
 * @param parameter the [KParameter] representing the parameter being validated; may be `null`.
 * @param message an optional message describing the validation failure; default value is `null`.
 * @param causeOf an optional supplier for custom exception to throw as the primary cause; default value is `null`.
 * @param cause an optional supplier for the secondary cause to be attached to the exception; default value is `null`.
 * @return the original sequence if it is not `null` or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is not null or empty. If the sequence is null or empty,
 * throws a `ValidationFailedException` with the specified details.
 *
 * @param callableName The name of the callable (e.g., method, function) related to this validation check.
 * @param parameterName The name of the parameter being validated, which can be null if not applicable.
 * @param message An optional custom message providing more context about the validation failure.
 * @param causeOf A supplier that provides the primary cause of validation failure, or null if not applicable.
 * @param cause A supplier for the underlying throwable cause for the exception, or null if not applicable.
 * @return The sequence itself if the validation passes.
 * @throws ValidationFailedException If the sequence is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNotNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given sequence is neither null nor empty.
 * If the sequence is null or empty, a `ValidationFailedException` is thrown.
 *
 * @param callableName The name of the callable (e.g., function or property) associated with the validation,
 *                     or `null` if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or `null` if not applicable.
 * @param message An optional error message providing additional details about the validation failure,
 *                or `null` to use the default message.
 * @param causeOf A supplier for the root cause of the validation failure, which may wrap the generated `ValidationFailedException`,
 *                or `null` if not specified.
 * @param cause A supplier for the cause of the validation failure, or `null` if not specified.
 * @return The original sequence if it is neither null nor empty.
 * @throws ValidationFailedException If the sequence is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNotNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is empty. If the sequence is not empty, a validation exception is thrown.
 *
 * @param causeOf Optional supplier for the throwable that provides additional context for the exception.
 *                If provided, the throwable is used as the primary cause of failure.
 * @param cause Optional supplier for the throwable that serves as the underlying cause of the validation exception.
 * @return The original sequence, if it passes the validation (i.e., it is empty).
 * @throws ValidationFailedException if the sequence is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty) throw if (causeOf == null) ValidationFailedException("The sequence is not empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The sequence is not empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is empty and throws a validation exception if it is not.
 *
 * @param causeOf An optional supplier for a throwable to be used as the cause of the exception,
 *                or `null` to ignore this parameter.
 * @param cause An optional supplier for a throwable to be set as the cause of the validation exception,
 *              or `null` if no cause is needed.
 * @param lazyMessage A supplier that generates the error message to be used in the validation exception
 *                    if the sequence is not empty.
 * @return The original sequence if it is empty.
 * @throws ValidationFailedException if the sequence is not empty, constructed with the supplied message
 *                                   and optional cause(s).
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isNotEmpty) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the sequence is empty. If the sequence is not empty, throws a `ValidationFailedException`.
 *
 * @param property The property associated with the validation. Can be null if no specific property is involved.
 * @param variableName An optional name of the variable being validated. Included in the error details if provided.
 * @param message An optional custom error message. Defaults to "is not empty" if not specified.
 * @param causeOf A supplier providing a specific `Throwable` instance to be thrown. If null, a default exception is thrown.
 * @param cause A supplier providing the cause of the exception. Can be null if no cause is specified.
 * @return The original sequence, if validation passes.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is empty, throwing a ValidationFailedException if it's not.
 *
 * This method is useful for ensuring that a sequence contains no elements during validation checks.
 * If the sequence is not empty, the specified error details will be used to construct the exception.
 *
 * @receiver The sequence to validate.
 * @param property The main KProperty associated with the validation failure, or null if not specified.
 * @param variable An optional secondary KProperty that provides additional context, or null if not specified.
 * @param message An optional message providing additional details about the validation failure.
 * @param causeOf A supplier for the primary exception cause, or null if not specified.
 * @param cause A supplier for any additional exception cause, or null if not specified.
 * @return The original sequence if it is empty.
 * @throws ValidationFailedException if the sequence is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates if the sequence is empty. If the sequence is not empty, it throws a [ValidationFailedException].
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can be null.
 * @param parameterName The name of the parameter in the given callable that caused the validation issue. Defaults to null.
 * @param message An optional custom message providing additional details about the validation failure. Defaults to null.
 * @param causeOf A supplier that provides the root cause of the validation failure as a [Throwable]. Defaults to null.
 * @param cause A supplier that provides additional context for the validation failure as a [Throwable]. Defaults to null.
 * @return The original sequence if it is empty.
 * @throws ValidationFailedException if the sequence is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is empty. If the sequence is not empty, throws a [ValidationFailedException].
 *
 * @param callable The [KFunction] related to the validation, or null if not applicable.
 * @param parameter The [KParameter] representing the parameter involved in the validation, or null if not applicable.
 * @param message An optional message providing additional context about the validation failure. Defaults to "is not empty" if not specified.
 * @param causeOf A supplier for the throwable to be thrown as the root cause, or null if not specified.
 * @param cause A supplier for the underlying exception cause, or null if not specified.
 * @return The original sequence if it is empty.
 * @throws ValidationFailedException If the sequence is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is empty and throws a `ValidationFailedException` if it is not.
 *
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName The name of the parameter being validated, or null if unspecified.
 * @param message An optional custom message to provide additional context for the validation failure.
 * @param causeOf An optional supplier for the throwable cause of the validation failure, or null if unspecified.
 * @param cause An optional supplier for the underlying cause of the exception, or null if unspecified.
 * @return The same sequence if validation passes.
 * @throws ValidationFailedException If the sequence is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a sequence is empty. If the sequence is not empty, a `ValidationFailedException` is thrown.
 *
 * This method is useful for enforcing expectations about the emptiness of sequences in validation logic.
 *
 * @param callableName The name of the callable (e.g., function or property) related to the validation. Can be null.
 * @param parameter The parameter associated with the validation, represented as a `KParameter`. Can be null.
 * @param message An optional error message to provide additional context about the validation failure. Defaults to "is not empty" if not specified.
 * @param causeOf A supplier for an optional base cause `Throwable` to be used in creating the exception, or null.
 * @param cause A supplier for an optional `Throwable` detailing additional context for the exception, or null.
 * @return The original sequence if it is empty.
 * @throws ValidationFailedException If the sequence is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is either null or empty. If the sequence does not meet the condition,
 * it throws a [ValidationFailedException].
 *
 * @param causeOf An optional supplier for a [Throwable] that serves as the primary cause of the exception,
 *                or `null`. If provided, this will be used as the root cause.
 * @param cause An optional supplier for a secondary [Throwable] that will be wrapped as the cause of the
 *              [ValidationFailedException], or `null`.
 * @return The original sequence if it passes the validation (i.e., it is null or empty).
 * @throws ValidationFailedException if the sequence is not null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException("The sequence is not null or empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The sequence is not null or empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that a sequence is either null or empty.
 * If the sequence is neither null nor empty, a `ValidationFailedException` is thrown.
 *
 * @param causeOf A supplier for a throwable that will be raised if the validation fails.
 *                This throwable will be set as the cause of the thrown exception if provided.
 * @param cause An additional supplier of a throwable that can be used as a cause for context.
 *              If provided, this will be linked as a cause to the supplied `causeOf` throwable
 *              or directly to the `ValidationFailedException`.
 * @param lazyMessage A supplier for the error message used in the exception if validation fails.
 *                    This allows for lazy evaluation of the error message.
 * @return The same sequence on which the validation was performed.
 *         If the sequence passes the validation (is null or empty), it is returned as-is.
 * @throws ValidationFailedException If the sequence is neither null nor empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the sequence is either null or empty. If the sequence is not null and not empty,
 * a `ValidationFailedException` is thrown.
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName The name of the variable being validated. Can be null if not provided.
 * @param message An optional custom message for the validation error. Defaults to "is not null or empty".
 * @param causeOf An optional supplier for the cause of the exception to be thrown. If not null, it will be invoked
 *                to provide a `Throwable`.
 * @param cause An optional supplier for an additional throwable cause to be associated with the exception. If not null,
 *              it will be invoked to provide the cause.
 * @return The same sequence instance if the validation does not fail.
 * @throws ValidationFailedException If the sequence is not null and not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the sequence is null or empty and throws a [ValidationFailedException] if it is not.
 *
 * @param property the main [KProperty] associated with the validation, providing context for the error, or null if not applicable
 * @param variable an additional [KProperty] offering further context, or null if not applicable
 * @param message an optional error message to include in the exception if validation fails, or null for a default message
 * @param causeOf a [ThrowableSupplier] that supplies the root cause of the validation failure, or null if not specified
 * @param cause a [ThrowableSupplier] providing a supplementary cause for the [ValidationFailedException], or null if not specified
 * @return the original sequence if it passes validation
 * @throws ValidationFailedException if the sequence is not null or empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is either null or empty. If the sequence is neither null nor empty,
 * a `ValidationFailedException` is thrown with the provided details.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message for the exception. Default is "is not null or empty".
 * @param causeOf A supplier for the `Throwable` that represents the root cause of the validation failure. Can be null.
 * @param cause A supplier for the `Throwable` that provides additional context to the exception. Can be null.
 * @return The same sequence after validation.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given sequence is either `null` or empty. If the sequence is not `null` or empty,
 * a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] associated with the validation failure, or `null` if not applicable.
 * @param parameter the [KParameter] representing the parameter being validated, or `null` if not applicable.
 * @param message an optional message providing additional details about the validation failure, defaulting to a standard message.
 * @param causeOf a supplier for the root cause of the validation failure, or `null` if not applicable.
 * @param cause an optional supplier for additional context on the cause of the failure, or `null` if not specified.
 * @return the original sequence if it passes validation without exceptions.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence is either null or empty. If the sequence is not null and not empty,
 * a `ValidationFailedException` is thrown.
 *
 * @param callableName the name of the callable (e.g., function or method) where the validation is performed
 * @param parameterName the name of the parameter being validated; optional
 * @param message an optional custom message providing additional details about the validation failure
 * @param causeOf a supplier that provides a custom throwable to be thrown as the cause of the failure; optional
 * @param cause a supplier that provides an additional cause for the `ValidationFailedException`; optional
 * @return the original sequence if validation passes
 * @throws ValidationFailedException if the sequence is not null and not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a sequence is null or empty. Throws a [ValidationFailedException]
 * if the sequence is neither null nor empty.
 *
 * @param callableName The name of the callable where the validation is performed. This may be null if not applicable.
 * @param parameter The [KParameter] associated with the validation. This may be null if not applicable.
 * @param message An optional custom error message to be included in the exception if validation fails. Defaults to "is not null or empty".
 * @param causeOf A supplier for an optional root cause of the validation failure. If provided, it initializes the throwable chain with the supplied cause.
 * @param cause A supplier for an optional additional cause of the failure. If provided, this is set as the cause of the [ValidationFailedException].
 * @return The original sequence if it passes the validation (i.e., if it is null or empty).
 * @throws ValidationFailedException If the sequence is neither null nor empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>?, E> T.validateNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}

/**
 * Validates if the sequence contains the specified element. If the element is not found
 * in the sequence, a [ValidationFailedException] is thrown. Optionally, custom transformers
 * can be provided to generate specific exceptions or causes for better error context.
 *
 * @param element The element to check for within the sequence.
 * @param causeOf Optional transformer to generate a throwable that encapsulates the error
 *                if the validation fails.
 * @param cause Optional transformer to generate a nested throwable that provides additional
 *              context about the failure.
 * @return The original sequence if validation passes.
 * @throws ValidationFailedException If the specified element is not present in the sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateContains(element: E, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException("$element is not in the sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$element is not in the sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence contains the specified element. If the element is not found,
 * a `ValidationFailedException` is thrown. This exception can be customized with an optional
 * cause transformer, additional cause, and a lazily evaluated message.
 *
 * @param element The element to check for in the sequence.
 * @param causeOf A transformer function that produces a specific `Throwable` cause based on the sequence. Could be null.
 * @param cause An additional transformer function that produces a `Throwable` cause based on the sequence. Could be null.
 * @param lazyMessage A transformer function that generates the validation failure message based on the sequence.
 * @return The original sequence if the validation passes.
 * @throws ValidationFailedException If the element is not found in the sequence and a failure message is constructed.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateContains(element: E, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence contains the specified element. If the element is not found, throws a
 * [ValidationFailedException] with an optional custom message and cause.
 *
 * @param element The element that must be present in the sequence for validation.
 * @param property The property associated with the validation failure. Can be null if not applicable.
 * @param variableName Optional name of the variable involved in the validation. Included in the exception message if not null.
 * @param message Custom message to include in the exception if validation fails. Defaults to a pre-defined message.
 * @param causeOf An optional transformer function to construct the root cause of the exception if the validation fails.
 *                Invoked only if provided and applicable.
 * @param cause An optional transformer function to define an additional cause for the exception. Invoked only if provided.
 * @return The sequence itself if the validation passes.
 * @throws ValidationFailedException If the sequence does not contain the specified element.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateContains(element: E, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current sequence contains the specified element. If the validation fails,
 * it throws a `ValidationFailedException` with an optionally provided message or cause.
 *
 * @param element The element to check for in the sequence.
 * @param property The main property associated with the validation, or null if not specified.
 * @param variable An optional secondary property providing additional context, or null if not specified.
 * @param message An optional error message describing the validation failure.
 * @param causeOf A transformation function that constructs a throwable as the cause of the validation failure. If null, uses a default exception.
 * @param cause A transformation function providing an additional cause of the exception, or null if no cause is specified.
 * @return The same sequence instance if the validation succeeds.
 * @throws ValidationFailedException if the sequence does not contain the specified element.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateContains(element: E, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence contains the specified element.
 * If the element is not found, a `ValidationFailedException` is thrown.
 *
 * @param element The element that must be present within the sequence.
 * @param callable An optional reference to the KFunction for associating validation errors. Can be null.
 * @param parameterName The optional parameter name related to the callable. Can be null.
 * @param message An optional custom message to be used in case of validation failure. Defaults to a standard error description.
 * @param causeOf An optional transformer that generates a `Throwable` used to determine the root cause of the validation failure.
 * @param cause An optional transformer that generates a `Throwable` for additional exception details in case of failure.
 * @return The same sequence if the validation passes.
 * @throws ValidationFailedException If the specified element is not contained in the sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateContains(element: E, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling sequence contains the specified element. If the element is not present,
 * a [ValidationFailedException] is thrown, which can optionally include information about the callable
 * method, the parameter related to the validation, a custom error message, and a custom cause.
 *
 * @param element the element that the sequence is expected to contain
 * @param callable an optional [KFunction] representing the function related to this validation
 * @param parameter an optional [KParameter] representing the parameter involved in the validation
 * @param message an optional message providing additional context for the validation failure
 * @param causeOf an optional transformer to generate a [Throwable] as the primary cause of the exception
 * @param cause an optional transformer to generate a secondary [Throwable] to be chained as the cause
 * @return the original sequence if validation passes
 * @throws ValidationFailedException if the element is not present in the sequence
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateContains(element: E, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the sequence contains the specified element. If the element is not found,
 * a `ValidationFailedException` is thrown with details about the error and optional cause(s).
 *
 * @param element The element to check for in the sequence.
 * @param callableName The name of the callable (e.g., function or method) where the validation is performed.
 * @param parameterName The name of the parameter being validated; can be null.
 * @param message An optional custom message to include in the error; if null, a default message is used.
 * @param causeOf A transformer function that provides an alternative cause of the exception; can be null.
 * @param cause A transformer function returning the underlying cause of the exception; can be null.
 * @return The original sequence if the validation passes.
 * @throws ValidationFailedException If the sequence does not contain the specified element.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateContains(element: E, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the sequence contains the specified element. If the element is not found,
 * a `ValidationFailedException` is thrown with the optional details provided.
 *
 * @param element The element to check for in the sequence.
 * @param callableName The name of the callable where validation takes place, or null if not specified.
 * @param parameter An optional `KParameter` representing the parameter being validated, or null if not applicable.
 * @param message An optional custom error message to be included in the exception if validation fails.
 * @param causeOf A transformer function to produce the root cause exception, or null if not applicable.
 * @param cause A transformer function to produce the secondary cause exception, or null if not applicable.
 * @return The original sequence if validation succeeds.
 * @throws ValidationFailedException If the element is not found in the sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateContains(element: E, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given sequence does not contain the specified element. If the element is found,
 * a validation error is thrown with an optional cause.
 *
 * @param element The element to check for in the sequence.
 * @param causeOf An optional transformer to generate a specific exception if the element is found.
 *                Defaults to `null`.
 * @param cause An optional transformer to generate an underlying cause for the validation exception.
 *              Defaults to `null`.
 * @return The original sequence if the validation passes, allowing method chaining.
 * @throws ValidationFailedException if the specified element is found in the sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotContains(element: E, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException("$element is in the sequence.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$element is in the sequence.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence does not contain the specified element. If the element is found,
 * throws a `ValidationFailedException` with a custom error message and an optional cause.
 *
 * @param element the element to check for in the sequence.
 * @param causeOf a transformer function to generate a specific throwable cause if validation fails.
 *                If `null`, a default `ValidationFailedException` is used as the cause.
 * @param cause an additional transformer function to provide a higher-level cause if validation fails.
 * @param lazyMessage a transformer function to generate the error message when validation fails.
 * @return the original sequence upon successful validation.
 * @throws ValidationFailedException if the sequence contains the specified element.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotContains(element: E, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence does not contain the specified element.
 * If the element is present in the sequence, a ValidationFailedException is thrown.
 *
 * @param element The element to check for in the sequence.
 * @param property An optional property associated with this validation. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Included in the exception message if provided.
 * @param message An optional custom error message to include in the exception. Defaults to a message indicating the presence of the element.
 * @param causeOf An optional transformer to generate the cause of the exception if the validation fails.
 * @param cause An optional transformer to generate additional details about the exception cause.
 * @return The original sequence if validation passes.
 * @throws ValidationFailedException If the sequence contains the specified element.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotContains(element: E, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling sequence does not contain the specified element.
 * If the element is present, a `ValidationFailedException` is thrown.
 *
 * @param element The element to check for in the sequence.
 * @param property The primary `KProperty` associated with the validation, used for contextual information, or null if not applicable.
 * @param variable An optional secondary `KProperty` providing additional context, or null if not specified.
 * @param message An optional message that will be included in the thrown exception if validation fails, or null for a default message.
 * @param causeOf An optional transformer to generate a more specific cause for the exception based on the sequence, or null if not used.
 * @param cause An optional transformer to generate an underlying cause for the exception based on the sequence, or null if not used.
 * @return The original sequence if validation passes without throwing an exception.
 * @throws ValidationFailedException Thrown if the sequence contains the specified element. The exception includes contextual details and an optional cause.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotContains(element: E, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current sequence does not contain the specified element. If the sequence contains the element,
 * a `ValidationFailedException` is thrown with the provided or default message and optional cause.
 *
 * @param element The element that should not be present in the sequence.
 * @param callable The Kotlin function to which this validation is related. Can be null.
 * @param parameterName The name of the parameter in the `callable` associated with the validation. Can be null.
 * @param message An optional custom error message to provide additional context for the validation failure. Default is "contains {element}".
 * @param causeOf A transformer that generates the root cause to be wrapped in the exception, based on the current sequence. Can be null.
 * @param cause A transformer that generates the underlying cause of the validation failure, based on the current sequence. Can be null.
 * @return The same sequence on which the validation was performed, provided validation passes.
 * @throws ValidationFailedException if the sequence contains the specified element.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotContains(element: E, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the sequence does not contain the specified element. If the element is found, a [ValidationFailedException] is thrown.
 *
 * @param element The element that must not be contained in the sequence.
 * @param callable The [KFunction] related to the validation failure, or null if not applicable.
 * @param parameter The [KParameter] associated with the validation failure, or null if not applicable.
 * @param message An optional message providing details about the validation failure; defaults to "contains {element}" if not specified.
 * @param causeOf A [Transformer] function that generates a throwable cause for the validation failure; defaults to null.
 * @param cause A [Transformer] function that produces the nested cause for the validation exception; defaults to null.
 * @return The original sequence if the element is not found.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotContains(element: E, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given sequence does not contain the specified element. If the element is
 * found within the sequence, a `ValidationFailedException` is thrown.
 *
 * @param element The element that must not be present in the sequence.
 * @param callableName The name of the callable (e.g., function or method) from which the validation originates.
 * @param parameterName The name of the parameter being validated, or null if unspecified.
 * @param message An optional custom message to include in the exception if the validation fails.
 * @param causeOf A transformer function for computing the root cause of the exception, or null if not required.
 * @param cause A transformer function for computing the exception's direct cause, or null if not required.
 * @return The original sequence if validation is successful, i.e., the element is not present.
 * @throws ValidationFailedException If the specified element is found within the sequence.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotContains(element: E, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $element", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current sequence does not contain the specified element.
 * If the element is found in the sequence, a `ValidationFailedException` is thrown.
 *
 * @param element The element to check against the sequence.
 * @param callableName The name of the callable where validation occurs, or null if not specified.
 * @param parameter An optional `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message to include in the exception if validation fails.
 * @param causeOf An optional transformer to produce a `Throwable` to be set as the cause of the validation failure.
 * @param cause An optional transformer to produce a `Throwable` that describes the validation failure.
 * @return The original sequence if validation passes.
 * @throws ValidationFailedException If the sequence contains the specified element.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Sequence<E>, E> T.validateNotContains(element: E, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (element in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $element", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $element", cause?.invoke(this)))
    return this
}