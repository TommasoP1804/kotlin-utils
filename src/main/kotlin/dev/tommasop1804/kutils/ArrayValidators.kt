/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
@file:JvmName("ArrayValidatorsKt")
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
 * Ensures that the array is not empty. If the array is empty, it throws a `ValidationFailedException`.
 *
 * @param causeOf An optional supplier for a custom throwable to be thrown. If provided, it takes precedence over a default exception.
 * @param cause An optional supplier for the underlying cause of the throwable.
 * @return The original array if it is not empty.
 * @throws ValidationFailedException if the array is empty and no custom throwable is provided.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException("The array is empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The array is empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that an array is not empty. If the array is empty, a `ValidationFailedException` is thrown
 * with an optional lazy message and optional throwable causes.
 *
 * @param causeOf an optional supplier for a throwable that will be initialized as the cause of a `ValidationFailedException`.
 * @param cause an optional supplier for a throwable that serves as the root cause of the validation failure.
 * @param lazyMessage a supplier for a lazy-evaluated message to include in the exception if validation fails.
 * @return the same array if it is not empty.
 * @throws ValidationFailedException if the array is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null, lazyMessage: Transformer<Array<E>, Any>): Array<E> {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the array is not empty. If the array is empty, a `ValidationFailedException` is thrown.
 *
 * @param property The property associated with this validation. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Used to provide more context in the error message.
 * @param message An optional custom message to be included in the exception if validation fails. Defaults to a generic message.
 * @param causeOf A supplier for the throwable cause of the exception. If provided, it will be invoked when constructing the exception.
 * @param cause An optional supplier for an underlying throwable that caused the validation failure. Defaults to null.
 * @return Returns this array instance if the validation succeeds.
 * @throws ValidationFailedException If the array is empty, with additional context provided by the parameters.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the array is not empty, throwing a validation exception if the array is empty.
 *
 * @param property the primary property associated with the validation context; can be null
 * @param variable an additional property providing further validation context; can be null
 * @param message an optional custom message for the validation error; defaults to "is empty" if not provided
 * @param causeOf a supplier for the cause of the exception, if applicable; can be null
 * @param cause an alternative supplier for the exception's cause, if applicable; can be null
 * @return the same array if validation passes (i.e., the array is not empty)
 * @throws ValidationFailedException if the array is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the array is not empty. If the array is empty, it throws a `ValidationFailedException`.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter in the callable being validated. Can be null.
 * @param message An optional custom message to describe the validation failure. Defaults to "is empty" if not provided.
 * @param causeOf A supplier for the primary `Throwable` cause of the validation failure, if applicable. Can be null.
 * @param cause A supplier for the secondary `Throwable` cause of the validation failure, if applicable. Can be null.
 * @return The same array after validation, if it is not empty.
 * @throws ValidationFailedException if the array is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the array is not empty. If the array is empty, a [ValidationFailedException] is thrown.
 *
 * @param callable the [KFunction] related to the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional message providing additional context about the validation failure, defaulting to null
 * @param causeOf a supplier for the primary cause of the validation failure, defaulting to null
 * @param cause a supplier for the underlying cause of the validation failure, defaulting to null
 * @return the validated array if it is not empty
 * @throws ValidationFailedException if the array is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the array is not empty. If the array is empty, a `ValidationFailedException` is thrown.
 *
 * @param callableName the name of the callable (e.g., function or method) to identify the context of the validation failure
 * @param parameterName the name of the parameter being validated, or null if not applicable
 * @param message an optional custom error message to provide additional details about the validation failure
 * @param causeOf a supplier of the root cause exception for the validation failure, or null if not applicable
 * @param cause a supplier of the exception to be set as the cause, or null if not applicable
 * @return the same array if it is not empty
 * @throws ValidationFailedException if the array is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the array is not empty. If the array is empty, throws a `ValidationFailedException`.
 *
 * @param callableName The name of the callable (e.g., function or property) where this validation is performed, or null if unspecified.
 * @param parameter The `KParameter` representing the parameter being validated, or null if not applicable.
 * @param message An optional custom error message to include in the exception if validation fails. Defaults to "is empty".
 * @param causeOf A supplier for a root cause to use as the primary exception or null if not provided.
 * @param cause A supplier for the underlying cause of the validation failure or null if not provided.
 * @return The validated array if it is not empty.
 * @throws ValidationFailedException if the array is empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateNotEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given array is neither null nor empty.
 *
 * This method checks if the array is null or contains no elements. If either condition is true,
 * a `ValidationFailedException` is thrown. The exception can optionally include a specific
 * cause or additional context via the provided `ThrowableSupplier` parameters.
 *
 * @param causeOf an optional supplier for a throwable, providing additional context for the exception.
 *                If null, a default context is used in the exception message.
 * @param cause an optional supplier for the exception's underlying cause. If null, no additional
 *              chained cause is included in the thrown exception.
 * @return the same array if it is neither null nor empty.
 * @throws ValidationFailedException if the array is null or empty, with an optional cause and context.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(causeOf: Transformer<Array<E>?, Throwable>? = null, cause: Transformer<Array<E>?, Throwable>? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException("The collection is null or empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The collection is null or empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the array is not null or empty.
 *
 * If the array is null or empty, this method throws a `ValidationFailedException` with a
 * message and optional causes provided by the supplied parameters.
 *
 * @param causeOf an optional supplier for the primary cause of the exception. If null,
 *                the default exception is used.
 * @param cause an optional supplier for the secondary or additional cause for context.
 * @param lazyMessage a supplier function that provides the error message to be included
 *                    in the exception. This is evaluated lazily.
 * @return the validated array if it is not null or empty.
 * @throws ValidationFailedException if the array is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(causeOf: Transformer<Array<E>?, Throwable>? = null, cause: Transformer<Array<E>?, Throwable>? = null, lazyMessage: Transformer<Array<E>?, Any>): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given array is not null or empty. If the array is null or empty, throws a
 * `ValidationFailedException` with an optional property reference, variable name, custom message, and causes
 * for additional context.
 *
 * @param property The property associated with the validation for diagnostic purposes. Can be null.
 * @param variableName An optional name of the variable being validated. Used in the exception message if provided.
 * @param message An optional custom message describing the validation failure. Defaults to "is null or empty" if not specified.
 * @param causeOf A supplier for the base exception to wrap the `ValidationFailedException`. If null, the validation exception will be created directly.
 * @param cause A supplier for an underlying throwable to be used as the cause of the `ValidationFailedException`. Can be null.
 * @return The original array if it is neither null nor empty.
 * @throws ValidationFailedException if the array is null or empty, with detailed information provided by the parameters.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Array<E>?, Throwable>? = null, cause: Transformer<Array<E>?, Throwable>? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the array is neither null nor empty.
 *
 * If the array is null or empty, a `ValidationFailedException` is thrown with an optional custom message
 * and cause. The exception can also include additional context via the specified `property` and `variable`.
 *
 * @param property the primary property associated with the validation, or null if not specified
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional message to include in the exception if validation fails, or null for a default message
 * @param causeOf an optional supplier to provide the throwable cause of the exception, or null if not used
 * @param cause an optional supplier to generate an additional cause for the exception, or null if not applicable
 * @return the validated array if it is neither null nor empty
 * @throws ValidationFailedException if the array is null or empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Array<E>?, Throwable>? = null, cause: Transformer<Array<E>?, Throwable>? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given array is not null or empty.
 *
 * This method ensures that the provided array is neither null nor empty.
 * If the array is null or empty, a `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) where this validation is being performed. Can be null.
 * @param parameterName The name of the parameter in the callable being validated. Can be null.
 * @param message An optional custom message to be included in the exception if validation fails. Defaults to "is null or empty".
 * @param causeOf A supplier for a `Throwable` to be thrown as the root cause of the validation failure. Can be null.
 * @param cause A supplier for an additional `Throwable` to be included as the cause of the `ValidationFailedException`. Can be null.
 * @return The validated array if it is not null or empty.
 * @throws ValidationFailedException if the array is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Array<E>?, Throwable>? = null, cause: Transformer<Array<E>?, Throwable>? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given array is neither null nor empty.
 *
 * This method checks if the array is null or contains no elements. If the validation fails,
 * it throws a `ValidationFailedException` with detailed information including the callable,
 * parameter, message, and cause.
 *
 * @param callable the [KFunction] related to the validation context, or `null` if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or `null` if not applicable
 * @param message an optional custom message to describe the validation failure; defaults to "is null or empty"
 * @param causeOf an optional supplier of a `Throwable` to use as the primary cause of failure, or `null`
 * @param cause an optional supplier of a secondary `Throwable` to associate with the validation failure, or `null`
 * @return the validated array if it is not `null` or empty
 * @throws ValidationFailedException if the array is `null` or empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Array<E>?, Throwable>? = null, cause: Transformer<Array<E>?, Throwable>? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given array is not `null` or empty.
 *
 * This method checks if the array is either `null` or contains no elements. If the condition is met,
 * it throws a `ValidationFailedException` with the specified details. Otherwise, the array is returned as-is.
 *
 * @param callableName the name of the callable (e.g., function or method) related to this validation.
 * This will be included in the exception details.
 * @param parameterName the name of the parameter being validated. This will be included in the exception details.
 * Defaults to `null` if not specified.
 * @param message an optional custom message providing additional details about the validation failure.
 * Defaults to `null` if not specified.
 * @param causeOf a supplier function for the root cause of the validation failure.
 * Defaults to `null` if not specified.
 * @param cause a supplier function for the underlying exception cause. Defaults to `null` if not specified.
 *
 * @return the original array if it is not `null` or empty.
 *
 * @throws ValidationFailedException if the array is either `null` or contains no elements.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Array<E>?, Throwable>? = null, cause: Transformer<Array<E>?, Throwable>? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given array is not null or empty.
 *
 * If the array is null or empty, a `ValidationFailedException` is thrown.
 *
 * @param callableName The name of the callable (e.g., function or property) where validation failed, or null if not specified.
 * @param parameter The KParameter instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message providing additional details about the validation failure. Default is `null`.
 * @param causeOf A supplier for an optional custom Throwable to be thrown instead of the default exception. Default is `null`.
 * @param cause A supplier for the root cause of the exception, if any. Default is `null`.
 * @return The validated array if it is not null or empty.
 * @throws ValidationFailedException If the array is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNotNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Array<E>?, Throwable>? = null, cause: Transformer<Array<E>?, Throwable>? = null): Array<E> {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates if the array is empty. Throws a `ValidationFailedException` if the array is not empty.
 *
 * @param causeOf An optional supplier for a custom throwable to be thrown instead of the default exception.
 *                If provided, this exception will wrap the default exception.
 * @param cause An optional supplier for an underlying cause to be associated with the exception.
 * @return The same array if it is empty.
 * @throws ValidationFailedException if the array is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException("The collection is not empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The collection is not empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the array is empty. If the array is not empty, a validation exception is thrown.
 *
 * @param causeOf Supplier for the exception to be thrown. If null, a `ValidationFailedException` with additional context is created.
 * @param cause Supplier for the underlying cause of the exception. This parameter is optional and may be null.
 * @param lazyMessage Supplier for the lazy-evaluated detail message included in the exception.
 * @return The original array if it is empty.
 * @throws ValidationFailedException if the array is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null, lazyMessage: Transformer<Array<E>, Any>): Array<E> {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the array is empty. If the array is not empty, a `ValidationFailedException` is thrown
 * with a detailed error message. The optional parameters allow customization of the validation failure message
 * and the associated cause.
 *
 * @param property The property associated with the validation failure. Can be null if not applicable.
 * @param variableName The optional name of the variable involved in the validation.
 * @param message An optional custom message describing the validation failure. Defaults to "is not empty".
 * @param causeOf A supplier for the throwable cause, used to wrap the `ValidationFailedException` if needed.
 *                Can be null if wrapping is not required.
 * @param cause A supplier for the throwable cause to be associated with the `ValidationFailedException`.
 *              Can be null if no cause is needed.
 * @return The original array if it is empty.
 * @throws ValidationFailedException If the array is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the array is empty. If the array is not empty, a `ValidationFailedException` is thrown.
 *
 * @param property the main KProperty associated with the validation, or null if not specified
 * @param variable an optional secondary KProperty providing additional context, or null if not specified
 * @param message an optional message providing additional details about the validation failure, or null to use a default message
 * @param causeOf a supplier for the cause of the exception, or null if no cause supplier is specified
 * @param cause a supplier for a specific throwable as the cause of the exception, or null if not specified
 * @return the original array if validation passes
 * @throws ValidationFailedException if the array is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the array is empty. If the array is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message for the validation failure. Default message: "is not empty".
 * @param causeOf An optional supplier providing a throwable to use as the cause of the validation failure. Can be null.
 * @param cause An optional supplier providing a secondary throwable cause if `causeOf` is not supplied. Can be null.
 * @return The original array if validation passes (i.e., the array is empty).
 * @throws ValidationFailedException If the array is not empty. Includes the callable, parameterName, custom message, and cause if provided.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the array is empty. If the array is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] related to the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter involved in the validation, or null if not applicable
 * @param message an optional message describing the reason for the validation failure, defaults to "is not empty"
 * @param causeOf a supplier for the throwable to be thrown as the primary cause of the validation failure, or null if not specified
 * @param cause an additional supplier for a throwable to be included as a secondary cause, or null if not specified
 * @return the same array instance if the validation passes
 * @throws ValidationFailedException if the array is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current array is empty. If the array is not empty, a `ValidationFailedException`
 * is thrown, optionally including additional context such as the callable name, parameter name, custom
 * error message, or a root cause.
 *
 * @param callableName The name of the callable (e.g., function or method) to include in the exception message, if applicable.
 * @param parameterName The name of the parameter being validated, if applicable.
 * @param message An optional custom error message to include in the exception, providing additional details about the validation failure.
 * @param causeOf A supplier for the root cause of the validation failure, if applicable.
 * @param cause A supplier for an additional cause to include in the exception, if applicable.
 * @return The current array if it passes validation (i.e., is empty).
 * @throws ValidationFailedException If the array is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the array is empty. If not, throws a [ValidationFailedException].
 *
 * @param callableName The name of the callable being validated, or null if not specified.
 * @param parameter The [KParameter] instance representing the parameter being validated, or null if not applicable.
 * @param message An optional custom error message for the validation failure. Defaults to "is not empty" if not specified.
 * @param causeOf An optional supplier providing the root cause [Throwable] for the validation failure. If this is not null, the
 * resulting exception will use it as the cause instead of creating a new one.
 * @param cause An optional supplier providing an additional [Throwable] to wrap as the cause in the exception.
 * @return The original array if the validation passes (i.e., it is empty).
 * @throws ValidationFailedException If the array is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>.validateEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E> {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given array is null or empty. If the array is not null or empty,
 * a `ValidationFailedException` is thrown.
 *
 * @param causeOf An optional supplier for the cause of the validation failure. Used to provide
 *                a custom exception to be thrown. If `null`, a default exception is constructed.
 * @param cause An optional supplier for the root cause of the validation failure, which will
 *              be included as the cause of the thrown exception.
 * @return The same array if it is null or empty, allowing chained calls.
 * @throws ValidationFailedException if the array is not null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException("The collection is not null or empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The collection is not null or empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given array is null or empty. If the array is not null or empty, a specified exception
 * is thrown. The exception and its message can be customized using the provided suppliers.
 *
 * @param E the type of elements in the array.
 * @param causeOf a supplier for a custom `Throwable` to throw if validation fails. If null, a default exception is used.
 * @param cause a supplier providing the underlying cause of the exception, if applicable. Can be null.
 * @param lazyMessage a supplier that generates the exception message lazily if validation fails.
 * @return the same array if it is `null` or empty.
 * @throws ValidationFailedException if the array is not null and not empty, and no custom throwable is provided.
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null, lazyMessage: Transformer<Array<E>, Any>): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given array is either null or empty. If the array is not null or not empty,
 * a `ValidationFailedException` is thrown with the provided details.
 *
 * @param property The property associated with the validation, used for error reporting. Can be null if not applicable.
 * @param variableName The name of the variable involved in the validation. Included in the exception message if provided.
 * @param message An optional message describing the reason for the validation failure. Defaults to a generic message.
 * @param causeOf An optional supplier for a custom throwable to be used as the primary cause of the validation failure.
 * @param cause An optional supplier for a throwable causing the validation failure. Used as an underlying cause.
 * @return The array itself if it is null or empty; otherwise, an exception is thrown.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given array is null or empty, throwing a `ValidationFailedException` if it is not.
 *
 * The exception message and cause can be customized using the provided parameters.
 *
 * @param property the primary KProperty associated with the validation, providing contextual information, or null if not specified
 * @param variable an optional secondary KProperty for additional context, or null if not specified
 * @param message an optional custom message to be appended to the exception if validation fails
 * @param causeOf a supplier for the exception to be thrown if validation fails, or null to use a default exception
 * @param cause a supplier for the underlying cause of the exception, or null if not needed
 * @return the original array if it is null or empty
 * @throws ValidationFailedException if the array is neither null nor empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates if the array is null or empty. If it is not null or empty, throws a `ValidationFailedException`.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can be null.
 * @param parameterName The name of the parameter in the given callable that caused the validation issue. Can be null.
 * @param message An optional custom message providing additional details about the validation failure. Default is null.
 * @param causeOf A supplier for the underlying cause of the validation error. Can be null.
 * @param cause A supplier for an additional cause to attach to the exception. Can be null.
 * @return Returns the original array if it is null or empty. If the array is neither null nor empty, an exception is thrown.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given array is null or empty and throws a `ValidationFailedException` if the validation fails.
 *
 * If the array is not null or not empty, the function throws a `ValidationFailedException` with the provided details.
 *
 * @param callable the [KFunction] related to the validation, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable.
 * @param message an optional custom message for the validation failure, defaulting to "is not null or empty".
 * @param causeOf an optional supplier for a throwable that directly represents the failure cause. If provided,
 *                it initializes the thrown exception. Defaults to null.
 * @param cause an optional supplier for a throwable that represents the underlying cause of the validation failure.
 *              It is chained as the cause of the exception thrown. Defaults to null.
 * @return the validated array if it passed the null or empty validation, or null if the input was null.
 * @throws ValidationFailedException if the array is not null or not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given array is null or empty. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param callableName the name of the callable (e.g., function or method) related to the validation process
 * @param parameterName the name of the parameter being validated, or null if not specified
 * @param message an optional custom message providing additional details about the validation failure
 * @param causeOf a supplier for the specific exception to throw as the root cause of validation failure, or null if not specified
 * @param cause a supplier for the underlying cause of the exception, or null if not specified
 * @return the original array if it passes the validation
 * @throws ValidationFailedException if the array is not null or not empty and validation fails
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that an array is either null or empty.
 *
 * This function checks if the array is null or empty and throws a `ValidationFailedException`
 * if it is not. The exception can include additional context such as the callable name,
 * parameter details, an optional message, and optionally configured causes. If the validation
 * passes (i.e., the array is null or empty), the original array is returned unchanged.
 *
 * @param callableName The name of the callable being validated, or null if not specified.
 * @param parameter The `KParameter` associated with the validation, or null if not applicable.
 * @param message An optional custom validation failure message to include in the exception.
 * @param causeOf A supplier for the specific cause of the validation failure, or null if not applicable.
 * @param cause An additional supplier for the root cause of the validation failure, or null if not applicable.
 * @return The original array if it is null or empty; otherwise, an exception is thrown.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <E> Array<E>?.validateNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Array<E>, Throwable>? = null, cause: Transformer<Array<E>, Throwable>? = null): Array<E>? {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}