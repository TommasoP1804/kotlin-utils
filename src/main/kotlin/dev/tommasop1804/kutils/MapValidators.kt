/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
@file:JvmName("MapValidatorsKt")
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
 * Validates that the map is not empty. If the map is empty, a validation exception is thrown.
 *
 * @param causeOf An optional supplier for a custom throwable to be thrown when the validation fails. If provided, this throwable will be the primary exception, and its cause will
 *  be set to the validation exception.
 * @param cause An optional supplier for a throwable that will act as the cause for the validation exception if no custom throwable is provided.
 * @return The map itself if it is not empty.
 * @throws ValidationFailedException if the map is empty and no custom throwable is supplied via `causeOf`.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException("The map is empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The map is empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is not empty. If the map is empty, an exception is thrown
 * with the specified lazy message, optional cause, and optional cause of the exception.
 *
 * @param causeOf an optional supplier for a throwable cause that will be
 *                set as the cause of the {@code ValidationFailedException}.
 *                If this is not null, its initialized cause will be set to the
 *                {@code ValidationFailedException}.
 * @param cause an optional supplier for a throwable that will be used to
 *              provide additional context to the exception.
 * @param lazyMessage a supplier for the lazy evaluation of the exception message
 *                    in case the validation fails.
 * @return the current map instance if it passes validation.
 * @throws ValidationFailedException if the map is empty. The exception message
 *                                   and cause are populated using the provided
 *                                   suppliers.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is not empty. If the map is empty, throws a ValidationFailedException.
 *
 * @param property The property being validated. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Included in the exception message if provided.
 * @param message An optional custom message to include in the exception. Defaults to "is empty".
 * @param causeOf A supplier for an alternative exception cause. If provided, it will be used as the root cause of
 *                the ValidationFailedException.
 * @param cause A supplier for an additional cause included in the exception chain. Optional and can be null.
 * @return The same map passed as the receiver if validation succeeds.
 * @throws ValidationFailedException If the map is empty, encapsulating the provided details.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current map is not empty. If the map is empty, a `ValidationFailedException` is thrown.
 *
 * @param property the main `KProperty` associated with this validation, providing class, name, and type context; can be null
 * @param variable an optional secondary `KProperty` providing additional variable-specific context; can be null
 * @param message an optional validation failure message to include in the exception; can be null, with a default of "is empty"
 * @param causeOf an optional supplier for a custom cause of the thrown exception; can be null
 * @param cause an optional supplier for an additional cause to be included in the exception; can be null
 * @return the same non-empty map instance that was validated
 * @throws ValidationFailedException if the map is empty, providing detailed validation context and an optional cause
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given map is not empty. Throws a `ValidationFailedException`
 * if the map is empty. The exception can include optional details such as
 * the callable, parameter name, custom message, and cause.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter in the callable to validate. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Default is "is empty".
 * @param causeOf A supplier for the root cause throwable associated with this validation failure. Can be null.
 * @param cause A supplier for a throwable indicating why the validation failed. Can be null.
 * @return The validated map, if it is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is not empty. If the map is empty, throws a [ValidationFailedException].
 *
 * @param callable the [KFunction] associated with the validation, or null if not applicable
 * @param parameter the [KParameter] representing the parameter under validation, or null if not applicable
 * @param message an optional custom error message to include in the exception, defaults to "is empty" if null
 * @param causeOf an optional supplier for the primary cause of the exception, or null if not applicable
 * @param cause an optional supplier for an additional cause of the exception, or null if not applicable
 * @return the validated map if it is not empty
 * @throws ValidationFailedException if the map is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given map is not empty. Throws a ValidationFailedException if the map is empty.
 *
 * @param callableName the name of the callable (e.g., a function or method) where the validation is performed
 * @param parameterName the name of the parameter being validated; can be null
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf a supplier for the primary cause of the validation failure; if null, a ValidationFailedException will be created instead
 * @param cause a supplier for the underlying cause of the exception; can be null
 * @return the validated map if it is not empty
 * @throws ValidationFailedException if the map is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is not empty. If the map is empty, throws a `ValidationFailedException`.
 *
 * @param callableName The name of the callable (e.g., function or property) where validation failed, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter that failed validation, or null if not applicable.
 * @param message An optional error message providing additional context about the validation failure. Defaults to "is empty" if not specified.
 * @param causeOf An optional supplier for a cause `Throwable` that directly triggered this validation failure. Defaults to null.
 * @param cause An optional supplier for a secondary cause `Throwable`. Defaults to null.
 * @return The map itself if it is not empty.
 * @throws ValidationFailedException If the map is empty, with optional details about the failed callable, parameter, message, and cause(s).
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is not null or empty.
 * If the map is null or empty, a specified throwable or a default `ValidationFailedException` is thrown.
 *
 * @param causeOf An optional supplier for the throwable to be used as the cause of the exception.
 *                If provided, this supplier will determine the throwable to be thrown.
 * @param cause   An optional supplier for the inner cause of the exception, used to provide additional
 *                context about the validation failure.
 * @return The original map if it is not null and not empty.
 * @throws ValidationFailedException if the map is null or empty and no custom throwable is supplied.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException("The map is null or empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The map is null or empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that a given map is neither null nor empty. Throws a `ValidationFailedException` if the validation fails.
 *
 * @param causeOf optional supplier for the primary throwable cause, which can be used to associate a specific cause with the failure.
 * @param cause optional supplier for an additional throwable cause, providing more context about the failure.
 * @param lazyMessage a supplier for creating the message of the exception, used to describe the reason for validation failure.
 * @return the original map if the validation passes, ensuring fluent usage within processing chains.
 * @throws ValidationFailedException if the map is null or empty. The exception includes a detailed message and optionally a cause.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is not null or empty. If the map is null or empty, a
 * [ValidationFailedException] is thrown with the provided details.
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName The name of the variable being validated. Used in the exception message if provided.
 * @param message An optional custom error message. Defaults to "is null or empty" if not specified.
 * @param causeOf A supplier for the cause of the validation failure. If provided, its result is used as the cause
 *                of the exception. Defaults to null.
 * @param cause An optional supplier for a throwable cause. It is used as the inner cause of the exception if `causeOf`
 *              is not provided. Defaults to null.
 * @return The validated map if it is not null or empty.
 * @throws ValidationFailedException If the map is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a Map is not null or empty. If the Map is null or empty, a `ValidationFailedException` is thrown.
 *
 * @param property the main property being validated, providing class and type context, or null if not specified
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional detailed message to include in the exception if validation fails, or null if not provided
 * @param causeOf a supplier function to provide a specific throwable as the cause of the exception, or null if not specified
 * @param cause a supplier function to provide an additional throwable to initialize the exception's cause, or null if not specified
 * @return the original Map if validation passes
 * @throws ValidationFailedException if the Map is null or empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is not null or empty. If the map is null or empty, this function throws a
 * `ValidationFailedException` with an optional custom message and cause.
 *
 * @param callable The Kotlin function (`KFunction`) associated with this validation. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message to describe the validation failure. Defaults to "is null or empty" if not provided.
 * @param causeOf A supplier for a throwable that serves as the primary cause of failure. Can be null.
 * @param cause A supplier for a secondary throwable that may have contributed to the failure. Can be null.
 * @return The original map (`this`) if it is not null or empty.
 * @throws ValidationFailedException If the map is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given map is not null or empty. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] related to the validation, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable.
 * @param message an optional error message to be included in the exception if validation fails; defaults to "is null or empty".
 * @param causeOf an optional supplier for the throwable causing the validation failure, or null if not applicable.
 * @param cause an optional supplier for the underlying cause of the exception, or null if not applicable.
 * @return the map being validated if the validation passes.
 * @throws ValidationFailedException if the map is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is not null or empty. Throws a `ValidationFailedException` if the map is null or empty.
 *
 * @param callableName The name of the callable (e.g., function or method) related to the validation failure.
 * @param parameterName The name of the parameter that caused the validation failure, or null if not specified.
 * @param message An optional custom message providing additional details about the validation failure, or null if not specified.
 * @param causeOf A supplier for the root cause of the validation failure, or null if not specified.
 * @param cause A supplier for an additional cause to chain with the exception, or null if not specified.
 * @return The original map if validation passes.
 * @throws ValidationFailedException If the map is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is not null or empty and throws a validation exception if the condition fails.
 *
 * @param callableName The name of the function or property where validation is performed,
 *                     or null if not specified.
 * @param parameter The KParameter instance representing the parameter being validated,
 *                  or null if not applicable.
 * @param message An optional error message to include in the exception if validation fails,
 *                or null if not specified. Defaults to "is null or empty".
 * @param causeOf A supplier for the root cause of the validation failure exception, or null if not specified.
 * @param cause A supplier for the secondary cause of the validation failure exception, or null if not specified.
 * @return The original map if validation passes.
 * @throws ValidationFailedException If the map is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is empty. If the map is not empty, throws a [ValidationFailedException].
 * The exception message indicates that the map is not empty. Optionally, allows for custom
 * throwable suppliers to provide specific causes for the exception.
 *
 * @param causeOf an optional supplier for a throwable to throw when validation fails.
 *                If provided, this throwable will be used instead of the default exception.
 *                The supplier can return `null`, in which case the default exception is used.
 * @param cause an optional supplier for a throwable to be used as the cause of the exception.
 *              This throwable will be passed as the cause of the validation exception.
 *              The supplier can return `null`, which results in no cause being assigned.
 * @return the map on which this method is invoked, if it is empty.
 * @throws ValidationFailedException if the map is not empty and no custom throwable supplier
 *                                   is provided via `causeOf`.
 * @throws Throwable if a throwable supplier is provided via `causeOf` and returns a throwable.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException("The map is not empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The map is not empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates that a map is empty. If the map is not empty, a `ValidationFailedException` is thrown.
 *
 * @param causeOf a supplier for a custom exception to be thrown if validation fails. If `null`, a default
 *        `ValidationFailedException` is used.
 * @param cause a supplier for the underlying cause of the exception to be thrown. Can be `null`.
 * @param lazyMessage a supplier for the error message to be included in the exception if validation fails.
 * @return the original map if it is empty.
 * @throws ValidationFailedException if the map is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is empty. Throws a [ValidationFailedException] if the map is not empty.
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Included in the exception message if provided.
 * @param message A custom message to include in the exception if the validation fails. Defaults to "is not empty".
 * @param causeOf A supplier for the primary exception cause, if applicable.
 * @param cause A supplier for the additional exception cause to set as the `cause` of the thrown exception. Can be null.
 * @return The original map if the validation passes.
 * @throws ValidationFailedException If the map is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is empty. If the map is not empty, throws a [ValidationFailedException].
 *
 * @param property the primary [KProperty] associated with the validation, used for error details, or null if not applicable
 * @param variable an optional secondary [KProperty] for additional context, or null if not applicable
 * @param message an optional custom message to include in the exception, or null for a default message
 * @param causeOf an optional supplier for a cause exception, invoked if the validation fails, or null if not applicable
 * @param cause an optional supplier for an exception to be used as the root cause, or null if not applicable
 * @return the original map if the validation passes
 * @throws ValidationFailedException if the map is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is empty. If the map is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) related to this validation. Can be null.
 * @param parameterName Optional name of the parameter related to the validation. Can be null.
 * @param message An optional custom validation message. If not provided, a default message "is not empty" is used.
 * @param causeOf A supplier providing a throwable that will be the primary cause of the exception. Can be null.
 * @param cause A supplier providing an additional cause for the exception. Can be null.
 * @return Returns the map itself if it is empty, allowing for method chaining.
 * @throws ValidationFailedException if the map is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current map is empty. If the map is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] related to the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional message to include in the validation exception, or null for a default message
 * @param causeOf a supplier providing a specific exception to throw, or null to use the default behavior
 * @param cause a supplier providing the underlying cause of the exception, or null if there is no specific cause
 * @return the same map instance upon successful validation
 * @throws ValidationFailedException if the map is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is empty. If the map is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName The name of the parameter being validated, or `null` if not applicable.
 * @param message An optional custom message to provide additional details about the validation failure. Defaults to "is not empty" if not specified.
 * @param causeOf A supplier that provides the root cause of the exception, or `null` if no specific cause is provided.
 * @param cause A supplier for the underlying cause associated with the validation failure, or `null` if not specified.
 * @return The same map if it passes validation (i.e., is empty).
 * @throws ValidationFailedException If the map is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a given map is empty. If the map is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callableName The name of the callable (e.g., function or property) related to the validation, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message providing additional context about the validation failure. Defaults to "is not empty" if not provided.
 * @param causeOf A supplier for the primary `Throwable` cause of the validation failure, or null if no primary cause is provided.
 * @param cause A supplier for the secondary `Throwable` cause of the validation failure, or null if no secondary cause is provided.
 * @return The original map instance if it is empty.
 * @throws ValidationFailedException If the map is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotEmpty()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is either `null` or empty. If the map is not `null`
 * or not empty, an exception is thrown.
 *
 * @param causeOf A supplier for the cause of the exception if validation fails.
 *                If provided, this will generate a throwable to further explain the failure.
 *                Default is `null`.
 * @param cause   A supplier for an additional cause to be attached to the exception,
 *                if validation fails. Default is `null`.
 * @return The same map instance if the validation passes (i.e., the map is `null` or empty).
 * @throws ValidationFailedException If the map is not `null` or not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException("The map is not null or empty.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("The map is not null or empty.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the map is null or empty, and throws a validation exception if the condition is not met.
 *
 * @param causeOf an optional supplier that provides a throwable to be used as the cause of the exception.
 * @param cause an optional supplier that provides an additional chained throwable to be used as the cause.
 * @param lazyMessage a supplier that generates the message for the validation exception.
 * @return the map instance if the validation passes.
 * @throws ValidationFailedException if the map is not null and not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that a map is either null or empty. If the map is not null and not empty, it throws a
 * ValidationFailedException with the provided details.
 *
 * @param property The property associated with the validation failure. Can be null if not applicable.
 * @param variableName Optional name of the variable being validated. Included in the exception message
 *                     if not null.
 * @param message Additional message to describe the validation failure. Defaults to "is not null or empty"
 *                if not specified.
 * @param causeOf Supplier for the cause of the exception. If provided, it is used to define the cause
 *                of the exception.
 * @param cause Supplier to initialize the cause of the ValidationFailedException. Can be null if not
 *              applicable.
 * @return The original map if it satisfies the validation (i.e., it is null or empty).
 * @throws ValidationFailedException if the map is not null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given map is null or empty. Throws a [ValidationFailedException] if the map is not null or empty.
 *
 * @param property The property associated with the map being validated. Can be null.
 * @param variable The variable associated with the map being validated. Can be null.
 * @param message An optional custom message for the validation failure.
 * @param causeOf A supplier for the root cause exception, if any. Can be null.
 * @param cause An additional supplier for the cause of the validation failure, if any. Can be null.
 * @return The map itself if the validation passes.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given map is null or empty. If the map is not null and not empty, a
 * `ValidationFailedException` is thrown. The exception can include optional details
 * such as the callable method, parameter name, error message, or a cause.
 *
 * @param callable An optional Kotlin function (`KFunction`) that the validation is associated with.
 *                 This is used in the construction of the exception. Can be null.
 * @param parameterName An optional name of the parameter that caused the validation failure.
 *                      This is used in the exception message. Can be null.
 * @param message An optional message providing details about the validation failure.
 *                Defaults to "is not null or empty" if not provided. Can be null.
 * @param causeOf An optional supplier for a pre-constructed throwable to be used as the primary
 *                cause of the exception. If provided, this is used as the exception thrown. Can be null.
 * @param cause An optional supplier for a throwable to include as the cause of the validation
 *              failure. This is appended as the cause to the `ValidationFailedException`
 *              if `causeOf` is not supplied. Can be null.
 * @return The original map (`T`) if no exception is thrown, allowing for chaining.
 * @throws ValidationFailedException If the map is not null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a map is null or empty. If the map is not null and not empty, this method throws a
 * [ValidationFailedException] with the provided details.
 *
 * @param callable The [KFunction] related to the validation failure, or null if not applicable.
 * @param parameter The [KParameter] representing the parameter involved in the validation failure, or null if not applicable.
 * @param message An optional message for the exception, describing the validation failure. Defaults to "is not null or empty" if null.
 * @param causeOf An optional supplier for the exception to be thrown. If supplied, this will wrap the generated exception.
 * @param cause An optional supplier for the cause of the exception, providing additional context about the failure.
 * @return The validated map if it is null or empty.
 * @throws ValidationFailedException If the map is not null and not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates if the map is null or empty and throws a `ValidationFailedException` if it is not.
 *
 * This method is used to enforce that a map must either be null or empty in specific contexts.
 * If the validation fails, an exception is thrown with detailed information about the callable, parameter,
 * optional custom message, and underlying cause, if provided.
 *
 * @param callableName The name of the callable (e.g., function or method) being validated.
 * @param parameterName An optional name of the parameter associated with this validation.
 * @param message An optional custom message providing additional details for the validation failure.
 * @param causeOf An optional supplier that provides the cause of the exception to be thrown. If null, a default exception is thrown.
 * @param cause An optional supplier that provides the underlying cause of the exception, adding more context.
 * @return The validated map `T` itself if it passes the validation.
 * @throws ValidationFailedException If the map is neither null nor empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}
/**
 * Validates that a map is null or empty.
 *
 * If the map is neither null nor empty, this method throws a `ValidationFailedException`.
 *
 * @param callableName The name of the callable (e.g., function or property) the validation is associated with, or null if unspecified.
 * @param parameter The parameter being validated, represented as a `KParameter`, or null if not applicable.
 * @param message An optional error message to provide context about the validation failure.
 * @param causeOf A supplier that provides the throwable to be thrown as the cause of the validation failure, or null if no such cause exists.
 * @param cause A supplier that provides an additional throwable cause if applicable, or null if absent.
 * @return The same map (`this`) that was validated, if no exception is thrown.
 * @throws ValidationFailedException If the map is not null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke(this)))
    return this
}

/**
 * Validates whether the map contains the specified key-value pair. If the pair is not found in the map,
 * a `ValidationFailedException` is thrown. Optional transformers can be used to customize the exception's
 * cause or the exception itself.
 *
 * @receiver The map to validate.
 * @param pair The key-value pair to check for in the map.
 * @param causeOf An optional transformer that produces a custom exception based on the map when the validation fails.
 * @param cause An optional transformer that produces the cause of the exception based on the map.
 * @return The original map if the validation succeeds.
 * @throws ValidationFailedException if the specified key-value pair is not present in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContains(pair: Pair<K, V>, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair !in this) throw if (causeOf == null) ValidationFailedException("$pair is not in the map.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$pair is not in the map.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map contains the specified key-value pair. If the key-value pair
 * is not found, a validation exception is thrown.
 *
 * @param pair the key-value pair to check for in the map.
 * @param causeOf an optional transformer that generates a cause throwable based on the map,
 *        or `null` if no additional cause is required.
 * @param cause an optional transformer that generates a throwable to be used as a nested
 *        cause, or `null` if no nested cause is required.
 * @param lazyMessage a transformer to generate a custom validation failure message using the map.
 * @return the original map if the validation succeeds.
 * @throws ValidationFailedException if the map does not contain the specified key-value pair.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContains(pair: Pair<K, V>, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null, lazyMessage: Transformer<M, Any>): M {
    if (pair !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the map contains the specified key-value pair. If the pair is not present,
 * an exception will be thrown with an optional custom message and/or cause.
 *
 * @param pair The key-value pair to validate.
 * @param property An optional property associated with the validation, used for error reporting.
 * @param variableName An optional name of the variable being validated, included in the error message if provided.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer used to generate a cause of the exception if validation fails.
 * @param cause An optional transformer used to generate the root cause of the exception if validation fails.
 * @return The original map if the validation passes.
 * @throws ValidationFailedException If the key-value pair is not present in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContains(pair: Pair<K, V>, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn'M contain $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn'M contain $pair", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the map contains the specified key-value pair. If the pair is not found,
 * a ValidationFailedException is thrown with an optional message and cause.
 *
 * @param pair the key-value pair to check for in the map.
 * @param property the primary property associated with the validation, or null if not specified.
 * @param variable an optional secondary property providing additional context, or null if not specified.
 * @param message an optional error message to include when the validation fails, or null for a default message.
 * @param causeOf an optional transformer for customizing the exception when the validation fails, or null to use the default exception behavior.
 * @param cause an optional transformer for customizing the cause of the exception, or null to exclude a cause.
 * @return this map instance if the validation passes.
 * @throws ValidationFailedException if the map does not contain the specified key-value pair.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContains(pair: Pair<K, V>, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn'M contain $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn'M contain $pair", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map contains the specified key-value pair. If the pair is not found, an exception is thrown.
 *
 * @param pair The key-value pair to check for in the map.
 * @param callable An optional Kotlin function (`KFunction`) related to the validation context. Can be null.
 * @param parameterName The name of the parameter in the callable related to the validation context. Can be null.
 * @param message An optional custom error message to include in the exception if validation fails. Default is null.
 * @param causeOf A transformer function that generates a `Throwable` based on the map when validation fails. Can be null.
 * @param cause An alternative transformer function that generates a `Throwable` based on the map. Can be null.
 * @return The original map (`M`) if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContains(pair: Pair<K, V>, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn'M contain $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn'M contain $pair", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map contains the specified key-value pair, and throws a `ValidationFailedException` if it does not.
 *
 * @param pair the key-value pair to validate the presence of in the map.
 * @param callable an optional `KFunction` that represents the context of the validation, or null if not applicable.
 * @param parameter an optional `KParameter` that represents a specific parameter involved in the validation, or null if not applicable.
 * @param message an optional message providing additional context about the validation failure; if null, a default message is used.
 * @param causeOf an optional transformer used to generate the cause of the validation failure from the current map; if null, the default `ValidationFailedException` is used.
 * @param cause an optional transformer used to attach an additional cause to the `ValidationFailedException` from the current map; if null, no additional cause is attached.
 * @return the current map instance if validation passes.
 * @throws ValidationFailedException if the key-value pair is not contained in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContains(pair: Pair<K, V>, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn'M contain $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn'M contain $pair", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map contains the specified key-value pair. If the pair is not found,
 * throws a [ValidationFailedException] with additional context and a custom message if provided.
 *
 * @param pair the key-value pair to validate against the map.
 * @param callableName the name of the callable (e.g., function or method) where the validation is triggered.
 * @param parameterName the name of the parameter being validated, optional.
 * @param message an optional custom error message to describe the validation failure.
 * @param causeOf an optional transformer to generate the root cause exception to be thrown.
 * @param cause an optional transformer to generate a nested cause exception to include in the validation failure.
 * @return the original map if the validation succeeds.
 * @throws ValidationFailedException if the map does not contain the specified key-value pair.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContains(pair: Pair<K, V>, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn'M contain $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn'M contain $pair", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current map contains the specified key-value pair.
 * If the pair is not found, a `ValidationFailedException` is thrown.
 *
 * @param pair The key-value pair to validate.
 * @param callableName The name of the callable where validation failed, or null if not specified.
 * @param parameter The KParameter instance representing the parameter that failed validation, or null if not applicable.
 * @param message An optional error message providing additional details about the validation failure.
 * @param causeOf A transformer function that generates a base Throwable to wrap the `ValidationFailedException`,
 *                or null if no base Throwable should be used.
 * @param cause A transformer function that generates the underlying cause of the `ValidationFailedException`,
 *              or null if no cause should be included.
 * @return The map itself if validation succeeds.
 * @throws ValidationFailedException if the validation fails.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContains(pair: Pair<K, V>, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn'M contain $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn'M contain $pair", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the map does not contain the specified key-value pair. Throws a validation
 * exception if the pair is found in the map.
 *
 * @param pair The key-value pair to check for absence in the map.
 * @param causeOf An optional transformer to generate a custom exception based on the map
 *                if the validation fails.
 * @param cause An optional transformer to generate a custom cause for the exception if
 *              the validation fails.
 * @return The map instance if validation succeeds.
 * @throws ValidationFailedException if the specified key-value pair exists in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContains(pair: Pair<K, V>, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair in this) throw if (causeOf == null) ValidationFailedException("$pair is in the map.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$pair is in the map.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking map does not contain the specified key-value pair.
 * If the pair exists in the map, a validation exception is thrown.
 *
 * @param pair the key-value pair to check for exclusion in the map.
 * @param causeOf an optional transformer that generates a specific exception when called with the map (nullable).
 * @param cause an optional transformer that generates a specific cause for the exception when called with the map (nullable).
 * @param lazyMessage a transformer function to lazily produce an error message if validation fails.
 * @return the map itself if the validation succeeds without exceptions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContains(pair: Pair<K, V>, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null, lazyMessage: Transformer<M, Any>): M {
    if (pair in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given map does not contain a specific key-value pair. If the map contains the pair,
 * a `ValidationFailedException` is thrown with an optional custom message and cause.
 *
 * @param pair The key-value pair to validate against the map.
 * @param property An optional Kotlin property associated with this validation, used to construct the exception message.
 * @param variableName An optional variable name to include in the exception message, providing additional context.
 * @param message An optional custom validation failure message. Defaults to "contains $pair" if not provided.
 * @param causeOf A lambda to generate a throwable representing the cause of the validation failure.
 * @param cause A lambda to generate an additional throwable to chain as the root cause of the exception.
 * @return The map (receiver) itself if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContains(pair: Pair<K, V>, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $pair", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified key-value pair. If the pair exists in the map,
 * a `ValidationFailedException` is thrown with the provided details.
 *
 * @param pair the key-value pair to ensure is not present in the map.
 * @param property an optional property reference associated with the validation failure, if applicable.
 * @param variable an optional secondary property reference providing additional context for the validation.
 * @param message an optional message to include in the exception if validation fails.
 * @param causeOf an optional transformer to generate the exception to be thrown based on the current map.
 * @param cause an optional transformer to provide a cause for the exception if thrown.
 * @return the original map if the validation passes without throwing an exception.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContains(pair: Pair<K, V>, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $pair", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified key-value pair. If the pair is present, an exception is thrown.
 *
 * @param pair The key-value pair to check for in the map.
 * @param callable The Kotlin function (`KFunction`) to which this validation is related. Can be null.
 * @param parameterName The name of the parameter in the given callable that caused the validation issue. Can be null.
 * @param message An optional custom message to include in the exception if the validation fails. Default is null.
 * @param causeOf A transformer to provide a custom cause when the validation fails. Default is null.
 * @param cause An additional transformer to provide a custom cause for the exception. Default is null.
 * @return The map (`this`) if validation was successful (i.e., the specified pair is not present).
 * @throws ValidationFailedException if the map contains the specified key-value pair.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContains(pair: Pair<K, V>, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $pair", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain a specific key-value pair.
 * If the specified pair exists in the map, a `ValidationFailedException` is thrown.
 *
 * @param pair the key-value pair to check for in the map.
 * @param callable the `KFunction` related to the validation context, or null if not applicable.
 * @param parameter the `KParameter` involved in the validation, or null if not applicable.
 * @param message an optional message providing additional details about the validation failure.
 * @param causeOf a function transforming the map into a `Throwable` to be used as the main cause of the exception, or null if not applicable.
 * @param cause a function transforming the map into a `Throwable` to be used as an additional cause of the exception, or null if not applicable.
 * @return the original map if the validation passes without exceptions.
 * @throws ValidationFailedException if the map contains the specified key-value pair.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContains(pair: Pair<K, V>, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $pair", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified key-value pair. If the pair exists in the map,
 * a `ValidationFailedException` is thrown. The exception may include additional information such as the callable name,
 * parameter name, a custom message, and an optional cause.
 *
 * @param pair The key-value pair to check for in the map.
 * @param callableName The name of the callable (function or method) related to this validation.
 * @param parameterName The name of the parameter being validated, if applicable.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf A transformer that generates the cause of the exception when validation fails. This is invoked on the current map.
 * @param cause A secondary transformer that generates an additional cause for the exception. This is invoked on the current map.
 * @return The original map, allowing chaining of validation operations.
 * @throws ValidationFailedException If the specified key-value pair exists in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContains(pair: Pair<K, V>, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $pair", cause?.invoke(this)))
    return this
}
/**
 * Validates that a map does not contain the specified key-value pair.
 * If the pair exists in the map, a `ValidationFailedException` is thrown.
 *
 * @param pair The key-value pair to check for existence in the map.
 * @param callableName The name of the callable (e.g., function or property) where validation is performed, or null if unspecified.
 * @param parameter The parameter associated with the validation, or null if not applicable.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer that produces a custom exception to throw based on the map when validation fails.
 * @param cause An optional transformer that produces a cause for the exception when validation fails.
 * @return The original map if validation succeeds.
 * @throws ValidationFailedException if the map contains the specified key-value pair.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContains(pair: Pair<K, V>, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (pair in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $pair", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $pair", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified map contains the given key. If the key is not found in the map,
 * a `ValidationFailedException` is thrown with a custom message and optional cause.
 *
 * @param key The key to check for in the map.
 * @param causeOf A transformer to generate a `Throwable` to be thrown as the cause of the exception
 *                if the key is not found in the map. Optional and can be `null`.
 * @param cause A transformer to generate a `Throwable` that will be linked as the underlying cause
 *              for the `ValidationFailedException`. Optional and can be `null`.
 * @return The original map if the key is found.
 * @throws ValidationFailedException If the key is not in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsKey(key: K, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key !in this) throw if (causeOf == null) ValidationFailedException("$key is not in the map.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$key is not in the map.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given map contains the specified key. If the key is not present, a
 * `ValidationFailedException` is thrown with the specified lazy message and optional causes.
 *
 * @param key the key to check for presence in the map.
 * @param causeOf an optional transformer that produces a throwable to be thrown as the cause
 *                of the exception if the validation fails. Defaults to `null`.
 * @param cause an optional transformer that produces a throwable to be set as the `cause`
 *              of the `ValidationFailedException`. Defaults to `null`.
 * @param lazyMessage a transformer that generates a message to be used in the exception
 *                    if the validation fails.
 * @return the original map instance if the key is present, allowing for fluent chaining.
 * @throws ValidationFailedException if the key is not present in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsKey(key: K, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null, lazyMessage: Transformer<M, Any>): M {
    if (key !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified map contains the provided key. If the key is missing, an exception is thrown.
 *
 * @param key The key whose presence in the map is to be validated.
 * @param property The associated property object, used for generating a contextual error message. Can be null.
 * @param variableName An optional name of a variable for use in the error message. Defaults to null.
 * @param message An optional custom error message. If not provided, a default message is used.
 * @param causeOf An optional transformer to generate a root cause for the exception when the validation fails. Defaults to null.
 * @param cause An optional transformer to generate a supplemental cause for the exception. Defaults to null.
 * @return The same map instance on which this function is called, if the validation passes.
 * @throws ValidationFailedException If the key is not contained in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsKey(key: K, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn'M contain $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn'M contain $key", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map contains the specified key. If the key is not present, a [ValidationFailedException]
 * is thrown with an optional message or cause.
 *
 * @param key The key to check for existence in the map.
 * @param property The primary property associated with the validation, used for generating detailed error messages. Can be null.
 * @param variable An optional secondary property providing additional context for the validation failure. Can be null.
 * @param message An optional string containing additional details about the validation failure. Defaults to null.
 * @param causeOf An optional transformer that produces a cause for the exception based on the map. Defaults to null.
 * @param cause An optional transformer that produces a generic cause for the exception based on the map. Defaults to null.
 * @return The map instance itself, allowing for method chaining.
 * @throws ValidationFailedException If the key is not present in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsKey(key: K, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn'M contain $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn'M contain $key", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map contains the specified key. If the key is not present, an exception is thrown.
 *
 * @param key The key to check for presence in the map.
 * @param callable The Kotlin function (`KFunction`) related to the validation. Can be null.
 * @param parameterName The name of the parameter associated with the validation. Can be null.
 * @param message An optional custom error message if validation fails. Can be null.
 * @param causeOf Function to generate a `Throwable` if validation fails and an alternative cause is required. Can be null.
 * @param cause Function to generate a `Throwable` to use as the validation exception's cause. Can be null.
 * @return The original map if the validation passes.
 * @throws ValidationFailedException if the key is not present in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsKey(key: K, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn'M contain $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn'M contain $key", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map contains the specified key. If the key is not present, a `ValidationFailedException` is thrown.
 *
 * @param key The key to check for in the map.
 * @param callable The `KFunction` related to the validation context, or null if not applicable.
 * @param parameter The `KParameter` associated with the validation context, or null if not applicable.
 * @param message An optional message providing additional context for the validation failure.
 * @param causeOf A transformer function to generate a custom cause for the thrown exception based on the map.
 * @param cause A transformer function to generate a secondary cause for the thrown exception based on the map.
 * @return The same map instance on which the method was called, allowing for method chaining.
 * @throws ValidationFailedException If the specified key is not found in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsKey(key: K, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn'M contain $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn'M contain $key", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking map contains the specified key. If the key is not present in the map,
 * a `ValidationFailedException` is thrown with an optional message and cause.
 *
 * @param key the key to check for in the map
 * @param callableName the name of the callable (e.g., function or method) associated with this validation
 * @param parameterName the name of the parameter related to this validation (optional)
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf a transformer function that generates a throwable cause from the map when validation fails (optional)
 * @param cause an additional transformer function that generates a throwable cause from the map when validation fails (optional)
 * @return the original map if the validation succeeds
 * @throws ValidationFailedException if the key is not present in the map
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsKey(key: K, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn'M contain $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn'M contain $key", cause?.invoke(this)))
    return this
}
/**
 * Validates if the map contains a specific key. If the key is not present, throws a `ValidationFailedException`.
 *
 * @param key The key to check for existence in the map.
 * @param callableName The name of the callable from which this validation is invoked, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter associated with the validation, or null if not applicable.
 * @param message An optional custom error message to provide context for the validation failure.
 * @param causeOf A transformation function for generating the root cause of the exception from the map, or null if not applicable.
 * @param cause A secondary transformation function for generating the cause of the exception from the map, or null if not applicable.
 * @return The original map if the key is present.
 * @throws ValidationFailedException If the key is not present in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsKey(key: K, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn'M contain $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn'M contain $key", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified key. If the key exists in the map,
 * an exception is thrown with an optional cause or a dynamically generated cause.
 *
 * @param key The key to check for existence in the map.
 * @param causeOf A transformer function to generate a custom exception using the current map, or null.
 * @param cause A transformer function to generate the cause of the exception using the current map, or null.
 * @return The original map if the validation passes.
 * @throws ValidationFailedException If the key is found in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsKey(key: K, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key in this) throw if (causeOf == null) ValidationFailedException("$key is in the map.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$key is in the map.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified key. If the key is found, an exception is thrown
 * based on the provided transformers for the cause and a lazy message.
 *
 * @param key The key to check for absence in the map.
 * @param causeOf An optional transformer for producing a custom exception as the cause when the validation fails.
 * @param cause An optional transformer for producing an underlying cause for the validation failure.
 * @param lazyMessage A transformer that generates a message to include in the exception when validation fails.
 * @return The original map, if validation is successful.
 * @throws ValidationFailedException If the key is found in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsKey(key: K, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null, lazyMessage: Transformer<M, Any>): M {
    if (key in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified key. If the key exists in the map, a `ValidationFailedException`
 * is thrown with the provided `property`, `variableName`, `message`, and/or `cause` to describe the failure.
 *
 * @param key The key to check for existence in the map.
 * @param property The property associated with the validation failure, if applicable. Can be null.
 * @param variableName The optional name of the variable being validated. Included in error messages if provided.
 * @param message Custom error message to describe the validation failure. Defaults to a message indicating the key is present.
 * @param causeOf A transformer to create a specific throwable as the cause of the exception. Can be null.
 * @param cause A transformer to generate an additional cause for the exception. Can be null.
 * @return The original map if no exception is thrown, allowing for method chaining.
 * @throws ValidationFailedException if the map contains the specified key.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsKey(key: K, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $key", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified key. If the key exists in the map,
 * a `ValidationFailedException` is thrown with the specified message and cause.
 *
 * @param key The key that must not be present in the map.
 * @param property The primary `KProperty` associated with the validation failure, or null if not specified.
 * @param variable An optional secondary `KProperty` providing additional context, or null if not specified.
 * @param message An optional custom error message to include in the exception, or null for a default message.
 * @param causeOf An optional transformer providing a cause derived from the map if validation fails, or null if unused.
 * @param cause An optional transformer providing a cause for the exception, or null if not specified.
 * @return The original map if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsKey(key: K, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $key", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified key. If the key is present in the map, a
 * `ValidationFailedException` is thrown.
 *
 * @param key The key to check for existence in the map.
 * @param callable An optional Kotlin function (`KFunction`) to associate with the validation error.
 *                 This is typically the function performing the validation.
 * @param parameterName An optional name of the parameter related to the validation error.
 * @param message An optional custom message for the validation error. Defaults to "contains [key]"
 *                if not provided.
 * @param causeOf An optional transformer to produce a `Throwable` representing the cause of the
 *                validation failure, based on the map.
 * @param cause An optional transformer to produce a `Throwable` cause of the validation failure,
 *              based on the map.
 * @return The original map (`M`) if the validation passes, allowing for fluent operations.
 * @throws ValidationFailedException If the key is found in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsKey(key: K, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $key", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given map does not contain the specified key. If the key is found in the map, a
 * ValidationFailedException is thrown with optional additional context or a custom cause.
 *
 * @param key the key to check for in the map
 * @param callable an optional [KFunction] reference to provide context for the validation
 * @param parameter an optional [KParameter] to associate with the validation context
 * @param message an optional custom error message to include in the exception if validation fails
 * @param causeOf an optional transformer to generate a custom exception to be thrown when validation fails
 * @param cause an optional transformer to generate a custom cause for the exception
 * @return the original map if validation passes
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsKey(key: K, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $key", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified key. If the key is present, an exception is thrown.
 *
 * @param key The key to check for existence in the map.
 * @param callableName The name of the callable (e.g., function or method) associated with this validation.
 * @param parameterName The name of the parameter being validated. Optional and may be null.
 * @param message An optional custom message to be used in the exception if validation fails. Default is null.
 * @param causeOf An optional transformer that generates the underlying exception to be thrown if validation fails. Default is null.
 * @param cause An optional transformer to specify the cause of the exception. Default is null.
 * @return The original map if the key is not present.
 * @throws ValidationFailedException if the map contains the specified key.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsKey(key: K, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $key", cause?.invoke(this)))
    return this
}
/**
 * Validates that a specified key does not exist in the map. If the key is found, a `ValidationFailedException` is thrown.
 *
 * @param key The key to check for existence in the map.
 * @param callableName The name of the callable (e.g., function or property) performing the validation, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional custom error message providing additional context for the validation failure.
 * @param causeOf A transformer that generates the cause of the validation failure from the map, or null if not provided.
 * @param cause A transformer that generates the root cause of the exception from the map, or null if not provided.
 * @return The original map if the validation succeeds.
 * @throws ValidationFailedException If the key exists in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsKey(key: K, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (key in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $key", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $key", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map contains the specified value. If the value is not found in the map,
 * a `ValidationFailedException` is thrown. Optional transformers can be provided to customize
 * the exception or its cause.
 *
 * @param value The value to validate for existence in the map.
 * @param causeOf An optional transformer function to produce a `Throwable` based on
 *                the map when the validation fails. Defaults to `null`.
 * @param cause An optional transformer function to produce a `Throwable` as the underlying
 *              cause when the validation fails. Defaults to `null`.
 * @return The map instance if the value validation passes.
 * @throws ValidationFailedException If the specified value is not present in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsValue(value: V, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (!containsValue(value)) throw if (causeOf == null) ValidationFailedException("$value is not in the map.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$value is not in the map.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map contains the specified value. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param value The value to check for in the map.
 * @param causeOf A transformer function to generate a throwable as the root cause of the exception (optional).
 * @param cause A transformer function to generate a secondary throwable to associate with the exception (optional).
 * @param lazyMessage A transformer function to provide a lazy-evaluated message when the validation fails.
 * @return The original map if the validation succeeds.
 * @throws ValidationFailedException If the specified value is not found in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsValue(value: V, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null, lazyMessage: Transformer<M, Any>): M {
    if (!containsValue(value)) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Ensures that the map contains the specified value. If the value is not present, a validation exception is thrown.
 *
 * @param value The value to check for within the map.
 * @param property Optional property associated with the validation, used for descriptive error messaging.
 * @param variableName Optional name of the variable being validated for inclusion in the exception message.
 * @param message Optional custom message for the validation failure.
 * @param causeOf Optional transformer providing a cause for the exception, invoked if the validation fails.
 * @param cause Optional transformer providing an additional underlying cause for the exception, invoked if the validation fails.
 * @return The map instance if validation succeeds.
 * @throws ValidationFailedException if the map does not contain the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsValue(value: V, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (!containsValue(value)) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn'M contain $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn'M contain $value", cause?.invoke(this)))
    return this
}
/**
 * Validates if the map contains the specified value. If the value is not found, throws a ValidationFailedException.
 *
 * @param value the value to verify if it exists in the map
 * @param property an optional property reference used to provide validation context
 * @param variable an optional variable reference for additional context
 * @param message an optional error message to include in case of validation failure
 * @param causeOf an optional transformer to produce a throwable cause for the exception
 * @param cause an optional transformer to produce a throwable cause for more detailed exception chaining
 * @return the same map instance if the validation succeeds
 * @throws ValidationFailedException if the map does not contain the specified value
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsValue(value: V, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (!containsValue(value)) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn'M contain $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn'M contain $value", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the map contains the specified value. If the value is not found, a validation exception
 * is thrown.
 *
 * @param value The value to check for in the map.
 * @param callable The Kotlin function (`KFunction`) related to the validation. Can be null.
 * @param parameterName The name of the parameter in the function that triggered the validation. Can be null.
 * @param message An optional custom error message to use when validation fails. Default is null.
 * @param causeOf A transformer function to produce a `Throwable` cause for the exception, based on the current map state. Can be null.
 * @param cause Another transformer function to produce a `Throwable` to use as the cause of the exception. Can be null.
 * @return The original map if the validation passes.
 * @throws ValidationFailedException if the validation fails.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsValue(value: V, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (!containsValue(value)) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn'M contain $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn'M contain $value", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the map contains the specified value. If the value is not present, throws a `ValidationFailedException`.
 *
 * @param value The value to check for presence in the map.
 * @param callable An optional reference to the [KFunction] related to the validation.
 * @param parameter An optional [KParameter] representing the specific parameter tied to the validation.
 * @param message An optional custom message for the exception if validation fails.
 * @param causeOf An optional transformer that generates the cause of the exception based on the map.
 * @param cause An optional transformer to compute the underlying cause of the exception.
 * @return The map instance after validation.
 * @throws ValidationFailedException if the value is not contained in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsValue(value: V, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (!containsValue(value)) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn'M contain $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn'M contain $value", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map contains the specified value. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param value The value to check for existence within the map.
 * @param callableName The name of the callable (e.g., function or method) associated with the validation.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom message providing additional details in case of validation failure.
 * @param causeOf A transformer that generates a specific cause of the validation failure.
 * @param cause A transformer that generates a secondary cause of the validation failure.
 * @return The original map if the validation succeeds.
 * @throws ValidationFailedException if the map does not contain the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsValue(value: V, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (!containsValue(value)) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn'M contain $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn'M contain $value", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map contains the specified value.
 *
 * If the value is not found, a `ValidationFailedException` is thrown. The exception can optionally
 * include details such as the name of the callable, the parameter related to the validation,
 * a custom message, and a transformation for the cause of the exception.
 *
 * @param value The value to check for in the map.
 * @param callableName The name of the callable where validation is performed, or null if not specified.
 * @param parameter An optional parameter (`KParameter`) involved in the validation.
 * @param message An optional custom message included in the exception if validation fails.
 * @param causeOf An optional transformer to provide the cause of the exception from the map itself.
 * @param cause An optional transformer to supply an additional throwable cause from the map.
 * @return The map itself if validation is successful.
 * @throws ValidationFailedException If the map does not contain the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateContainsValue(value: V, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (!containsValue(value)) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn'M contain $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn'M contain $value", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified value. If the value is found in the map,
 * a [ValidationFailedException] is thrown with the provided cause or a custom exception from `causeOf`.
 *
 * @param value The value to check for non-existence in the map.
 * @param causeOf An optional transformer to provide a custom [Throwable] if validation fails.
 * @param cause An optional transformer to generate a cause [Throwable] to attach to the exception.
 * @return The original map if the validation passes.
 * @throws ValidationFailedException If the specified value is found in the map.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsValue(value: V, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (containsValue(value)) throw if (causeOf == null) ValidationFailedException("$value is in the map.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$value is in the map.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified value. If the value is found in the map,
 * a `ValidationFailedException` is thrown with a message and optional cause transformed from the map.
 *
 * @param value The value that should not exist in the map.
 * @param causeOf A transformer function that generates a throwable based on the map if the validation fails. Defaults to null.
 * @param cause A transformer function that provides a cause for the validation failure, derived from the map. Defaults to null.
 * @param lazyMessage A transformer function generating a message based on the map for the validation failure.
 * @return The original map if the validation succeeds.
 * @throws ValidationFailedException If the map contains the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsValue(value: V, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null, lazyMessage: Transformer<M, Any>): M {
    if (containsValue(value)) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified value. If the validation fails,
 * a `ValidationFailedException` is thrown with the provided details.
 *
 * @param value The value that must not be present in the map.
 * @param property The property associated with the validation, can be null if not applicable.
 * @param variableName The name of the variable involved in the validation, optional.
 * @param message The custom error message to include if validation fails, optional.
 * @param causeOf A transformer function to create a custom `Throwable` to be thrown as the main cause of failure, optional.
 * @param cause A transformer function to create a nested `Throwable` cause for the validation failure, optional.
 * @return The original map if the validation passes.
 * @throws ValidationFailedException if the map contains the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsValue(value: V, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (containsValue(value)) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $value", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified value. If the value is found in the map,
 * a `ValidationFailedException` is thrown with an optional message and cause.
 *
 * @param value the value to check against the map
 * @param property the main property associated with the validation, or null if not applicable
 * @param variable an optional secondary property providing additional context, or null if not applicable
 * @param message an optional message to include in the validation failure exception, or null for a default message
 * @param causeOf an optional transformer to generate the primary cause of the exception when the validation fails
 * @param cause an optional transformer to specify a secondary cause of the exception
 * @return the original map if validation succeeds
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsValue(value: V, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (containsValue(value)) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $value", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified value. If the value is found within the map,
 * a `ValidationFailedException` will be thrown with the provided or default error details.
 *
 * @param value The value to check for existence in the map. If the value exists in the map, the validation will fail.
 * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can provide additional context for the exception. Can be null.
 * @param parameterName The name of the parameter being validated. Used for constructing detailed error messages. Can be null.
 * @param message An optional custom message for the exception, providing additional context about the validation failure. If not provided, a default message is used.
 * @param causeOf An optional transformer that generates a cause (`Throwable`) based on the map when the validation fails. Can be null.
 * @param cause An optional transformer that generates the underlying cause (`Throwable`) for the failure when validation fails. Can be null.
 * @return The map itself if the validation passes successfully, allowing method chaining.
 * @throws ValidationFailedException if the map contains the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsValue(value: V, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (containsValue(value)) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $value", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified value. If the value is found, a `ValidationFailedException` is thrown.
 *
 * @param value the value to check for in the map
 * @param callable an optional [KFunction] providing context about the callable associated with the validation failure
 * @param parameter an optional [KParameter] providing context about the parameter involved in the validation failure
 * @param message an optional custom message for the validation failure
 * @param causeOf an optional transformer to create a `Throwable` cause for the exception
 * @param cause an optional transformer to create a `Throwable` cause for the exception
 * @return the original map if the validation passes
 * @throws ValidationFailedException if the map contains the specified value
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsValue(value: V, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (containsValue(value)) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $value", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified value.
 * If the value is found in the map, an exception is thrown.
 *
 * @param value the value to check for existence in the map
 * @param callableName the name of the callable (e.g., function or method) where the validation is performed
 * @param parameterName the name of the parameter that is being validated, or null if not applicable
 * @param message an optional custom message to include in the exception if the validation fails
 * @param causeOf a transformer that produces the cause of the exception when the validation fails, or null if not applicable
 * @param cause a transformer that produces an additional cause for the exception, or null if not applicable
 * @return the original map, if the validation succeeds
 * @throws ValidationFailedException if the specified value is found in the map
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsValue(value: V, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (containsValue(value)) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $value", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not contain the specified value. If the value is found,
 * an exception is thrown with details about the validation failure.
 *
 * @param value The value that the map should not contain.
 * @param callableName The name of the callable where the validation is being performed, or null if not applicable.
 * @param parameter An optional `KParameter` representing the parameter being validated, or null if not applicable.
 * @param message An optional custom error message to use if validation fails.
 * @param causeOf An optional transformer responsible for generating a custom exception to throw if validation fails.
 * @param cause An optional transformer for specifying the underlying cause of the validation failure.
 * @return The map itself if validation succeeds.
 * @throws ValidationFailedException If the map contains the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <M : Map<K, V>, K, V> M.validateNotContainsValue(value: V, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<M, Throwable>? = null, cause: Transformer<M, Throwable>? = null): M {
    if (containsValue(value)) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $value", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $value", cause?.invoke(this)))
    return this
}

/**
 * Validates that the map has the exact specified size. If the size does not match,
 * a `ExpectationMismatchException` is thrown.
 *
 * @param size The expected size of the map.
 * @param causeOf An optional transformer that provides a custom throwable as the cause of the exception.
 * @param cause An optional transformer to generate a throwable cause if the validation fails.
 * @return The original map if the size matches the expected value.
 * @throws ExpectationMismatchException If the map size does not match the expected value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectSize(size: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException("The map is not of size $size.", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException("The map is not of size $size.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the size of the map matches the expected size. If the validation fails,
 * a `ExpectationMismatchException` is thrown with the specified message and optional cause.
 *
 * @param size the expected size of the map to validate against.
 * @param causeOf a transformer that generates a throwable cause when the validation fails, or null if not required.
 * @param cause a transformer that provides an additional underlying cause, or null if not required.
 * @param lazyMessage a transformer that generates the exception message lazily based on the map instance.
 * @return the original map instance if the size validation passes.
 * @throws ExpectationMismatchException if the size validation fails.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectSize(size: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Ensures that the size of the map matches the expected size, throwing a validation exception if it does not.
 *
 * @param size The expected size of the map.
 * @param property The property associated with the map being validated. Can be null if not applicable.
 * @param variableName The name of the variable being validated, used in the error message if provided. Defaults to null.
 * @param message An optional custom validation failure message. If null, a default message is used.
 * @param causeOf A function that generates a throwable to use as the root cause of the validation failure, if provided.
 * @param cause A function that generates a throwable to include as the cause of the validation failure, if applicable.
 * @return The original map if the size matches the expected value.
 * @throws ExpectationMismatchException If the map's size is not equal to the provided size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectSize(size: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(property, variableName, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variableName, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map has the expected size. If the size does not match, a `ExpectationMismatchException` is thrown.
 *
 * @param size The expected size of the map.
 * @param property A KProperty providing context about the property being validated, or null if not specified.
 * @param variable An optional secondary KProperty providing additional context for validation, or null if not specified.
 * @param message An optional custom message to append to the exception, or null to use the default message.
 * @param causeOf A transformer function to create the root cause exception, or null if not specified.
 * @param cause A transformer function to create a detailed exception cause, or null if not specified.
 * @return The map itself if the validation passes.
 * @throws ExpectationMismatchException If the map does not have the expected size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectSize(size: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(property, variable, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variable, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the size of the map is equal to the expected size.
 * If the map does not match the expected size, a `ExpectationMismatchException` is thrown.
 *
 * @param size The expected size of the map.
 * @param callable The Kotlin function (`KFunction`) related to this validation, which can be used for contextual information. Can be null.
 * @param parameterName The name of the parameter in the callable that caused the validation issue. Can be null.
 * @param message An optional custom message to include in the validation exception. Defaults to "is not of size [size]".
 * @param causeOf A transformer function to generate the cause of the exception based on the map. Can be null.
 * @param cause An alternative transformer function to generate the cause of the exception based on the map, if `causeOf` is not provided. Can be null.
 * @return The map itself when the size validation passes, allowing method chaining or further processing.
 * @throws ExpectationMismatchException if the map's size does not match the expected size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectSize(size: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameterName, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map has the exact expected size as specified.
 *
 * If the actual size of the map does not match the expected size, a `ExpectationMismatchException` is thrown.
 *
 * @param size the expected size of the map
 * @param callable the [KFunction] related to the validation failure, or null if not applicable
 * @param parameter the [KParameter] representing the parameter involved in the validation failure, or null if not applicable
 * @param message an optional message providing additional context about the validation failure, defaulting to null
 * @param causeOf an optional transformer for generating the root cause of the exception from the map, defaulting to null
 * @param cause an optional transformer generating a more specific cause of the exception from the map, defaulting to null
 * @return the map itself if the size matches, allowing method chaining
 * @throws ExpectationMismatchException if the size of the map does not match the expected size
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectSize(size: Int, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameter, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Validates that the size of the map matches the expected size. If the size does not match,
 * a `ExpectationMismatchException` is thrown.
 *
 * @param size the expected size of the map
 * @param callableName the name of the callable (e.g., function or method) related to the validation
 * @param parameterName the name of the parameter being validated, or null if not applicable
 * @param message an optional custom message providing additional context for the validation failure
 * @param causeOf an optional transformer that generates the cause of the validation failure exception
 *                from the map when a validation failure occurs
 * @param cause an optional transformer that generates the secondary cause of the exception from the map
 *              when a validation failure occurs
 * @return the original map if the size validation is successful
 * @throws ExpectationMismatchException if the size of the map does not match the expected size
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectSize(size: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameterName, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the size of the map matches the expected size. If the size does not match,
 * a `ExpectationMismatchException` is thrown with the provided details.
 *
 * @param size The expected size of the map.
 * @param callableName The name of the callable (e.g., function or property) for which validation is applied, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message providing additional context for the validation failure.
 * @param causeOf An optional transformer that produces a throwable to further contextualize the cause of the validation failure.
 * @param cause An optional transformer that produces a throwable to be used as the root cause of the validation failure.
 * @return The map instance if the validation succeeded.
 * @throws ExpectationMismatchException if the map size does not match the expected size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectSize(size: Int, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size != size) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, message ?: "is not of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameter, message ?: "is not of size $size", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the map does not have a specific size. If the size of the map matches the given size,
 * a `ExpectationMismatchException` will be thrown. Optionally, a custom cause can be provided for
 * the exception using transformers.
 *
 * @param size the size that the map must not have.
 * @param causeOf an optional transformer that generates a throwable based on the map,
 *                which acts as the root cause of the `ExpectationMismatchException`. Default is null.
 * @param cause an optional transformer that generates a throwable based on the map,
 *              which is used as the secondary cause of the `ExpectationMismatchException`. Default is null.
 * @return the original map if it does not have the specified size.
 * @throws ExpectationMismatchException if the map has the specified size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectNotSize(size: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException("The map is of size $size.", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException("The map is of size $size.", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the size of the map is not equal to the specified value. If the map's size
 * matches the given size, a `ExpectationMismatchException` is thrown.
 *
 * @param size the size that the map is not expected to have.
 * @param causeOf a transformer that can produce a customized exception using the map, or null.
 * @param cause a transformer that can produce the underlying cause of the exception using the map, or null.
 * @param lazyMessage a transformer that generates the message for the exception using the map.
 * @return the original map if its size does not match the specified value.
 * @throws ExpectationMismatchException if the map's size matches the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectNotSize(size: Int, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the map is not of the specified size. If the validation fails (i.e.,
 * the map has the specified size), a `ExpectationMismatchException` will be thrown.
 *
 * @param size The size that the map should not have.
 * @param property The property associated with this validation. Can be null if not applicable.
 * @param variableName Optional name of the variable being validated. Included in the exception message if provided.
 * @param message Optional custom message to describe the validation failure. Defaults to "is of size $size" if not specified.
 * @param causeOf A transformer function to produce the root cause exception if the validation fails. Can be null.
 * @param cause A transformer function to produce an additional cause exception if the validation fails. Can be null.
 * @return The same map instance if the validation passes.
 * @throws ExpectationMismatchException if the map has the specified size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectNotSize(size: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(property, variableName, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variableName, message ?: "is of size $size", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the size of the map does not match the specified value. If the map's size is equal to the given size,
 * a `ExpectationMismatchException` is thrown.
 *
 * @param size the size that the map's size must not match
 * @param property the main KProperty associated with the validation, providing context for the validation failure, or null if not specified
 * @param variable an optional secondary KProperty that provides additional context for the validation, or null if not specified
 * @param message an optional custom message describing the validation failure; if null, a default message will be generated
 * @param causeOf an optional transformer that produces a throwable representing the validation failure cause, or null if no such transformer is provided
 * @param cause an optional transformer that produces a nested throwable cause, or null if not provided
 * @return the original map if the validation passes
 * @throws ExpectationMismatchException if the size of the map matches the specified value
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectNotSize(size: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(property, variable, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variable, message ?: "is of size $size", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the map is not of the specified size. If the map's size matches the given size,
 * a `ExpectationMismatchException` is thrown.
 *
 * @param size The size to validate against. If the map's size equals this value, the validation will fail.
 * @param callable The Kotlin function (`KFunction`) related to the validation. Can be null.
 * @param parameterName The name of the parameter within the given function that caused the validation issue. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Can be null.
 * @param causeOf A transformer that returns a `Throwable` to be used as the cause of the exception. Can be null.
 * @param cause An additional transformer that returns a `Throwable` to be set as the exception's cause. Can be null.
 * @return The map itself if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectNotSize(size: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameterName, message ?: "is of size $size", cause?.invoke(this)))
    return this
}
/**
 * Validates that the size of the map is not equal to the specified size.
 * If the size matches the given value, a `ExpectationMismatchException` is thrown.
 *
 * @param size The size to check against the map's size.
 * @param callable The callable reference related to the validation, used for error context, or null if not applicable.
 * @param parameter The parameter involved in the validation, used for error context, or null if not applicable.
 * @param message An optional additional message providing more context for the validation failure, defaulting to null.
 * @param causeOf A transformer to create the cause of the failure, optionally used to wrap the `ExpectationMismatchException`.
 * @param cause A transformer to generate an underlying throwable cause for the validation failure, or null by default.
 * @return The original map if the size is not equal to the specified size.
 * @throws ExpectationMismatchException if the map has the specified size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectNotSize(size: Int, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameter, message ?: "is of size $size", cause?.invoke(this)))
    return this
}
/**
 * Validates that the map does not have the specified size. If the map's size matches the
 * provided size, a validation failure exception is thrown.
 *
 * @param size The size that the map should not match.
 * @param callableName The name of the callable or function related to the validation.
 * @param parameterName An optional name of the parameter being validated (default is null).
 * @param message An optional custom message for the validation failure (default is null).
 * @param causeOf An optional transformer to produce the exception to throw from the map (default is null).
 * @param cause An optional transformer to produce the underlying cause of the validation failure exception (default is null).
 * @return The original map, if the validation passes (i.e., its size does not match the specified size).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectNotSize(size: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameterName, message ?: "is of size $size", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the size of the map is not equal to the specified size.
 * If the map's size is equal to the specified size, a ExpectationMismatchException is thrown.
 *
 * @param size The size to compare against the map's size.
 * @param callableName The name of the callable (e.g., function or property) where this check is performed.
 * @param parameter The KParameter instance representing the parameter being validated, if applicable.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf An optional transformer function to construct the primary cause of the exception.
 * @param cause An optional transformer function to construct an additional cause of the exception.
 * @return The original map if its size is not equal to the specified size.
 * @throws ExpectationMismatchException If the size of the map matches the specified size.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Map<K, V>, K, V> T.expectNotSize(size: Int, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this.size == size) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, message ?: "is of size $size", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameter, message ?: "is of size $size", cause?.invoke(this)))
    return this
}