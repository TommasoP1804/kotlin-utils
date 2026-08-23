/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("NumberValidatorsKt")
@file:Since("5.0.0")
@file:Suppress("unused")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.range.IntRangeWithConditions
import dev.tommasop1804.kutils.classes.range.IntRangeWithExclusions
import dev.tommasop1804.kutils.classes.range.LongRangeWithConditions
import dev.tommasop1804.kutils.classes.range.LongRangeWithExclusions
import dev.tommasop1804.kutils.classes.range.UIntRangeWithConditions
import dev.tommasop1804.kutils.classes.range.UIntRangeWithExclusions
import dev.tommasop1804.kutils.classes.range.ULongRangeWithConditions
import dev.tommasop1804.kutils.classes.range.ULongRangeWithExclusions
import dev.tommasop1804.kutils.exceptions.ExpectationMismatchException
import dev.tommasop1804.kutils.exceptions.NumberOutOfRangeException
import dev.tommasop1804.kutils.exceptions.NumberSignException
import dev.tommasop1804.kutils.exceptions.ValidationFailedException
import kotlin.contracts.ExperimentalContracts
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

/**
 * Validates that the number is positive. If the number is not positive, throws a `NumberSignException`.
 *
 * @param causeOf The optional throwable to use as the primary exception; if provided, its cause is set to a new `NumberSignException`.
 * @param cause The optional throwable to be used as the cause of the `NumberSignException`.
 * @return The number itself if it is positive.
 * @throws NumberSignException If the number is not positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validatePositive(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotPositive) throw if (causeOf == null) NumberSignException("Value is not positive.", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException("Value is not positive.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is positive. If the number is not positive, a [NumberSignException] is thrown.
 *
 * @param causeOf An optional [Throwable] indicating the cause of the exception. If provided, it will be used as the base for exception chaining.
 * @param cause An optional [Throwable] representing the cause of the exception. Can be null.
 * @param lazyMessage A supplier function for the exception message, which will be lazily evaluated.
 * @return The validated number if it is positive.
 * @throws NumberSignException If the number is not positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validatePositive(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isNotPositive) throw if (causeOf == null) NumberSignException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is positive. If the number is not positive, a `NumberSignException` is thrown.
 *
 * @param property The Kotlin property associated with the validation, providing contextual information
 *                 such as its owner class, name, and return type. May be `null`.
 * @param variableName An optional name of the variable being validated. If provided, it will be included
 *                     in the exception for better error context. Defaults to `null`.
 * @param message An optional custom error message describing the validation failure. If not provided, a default
 *                message "is not positive" will be used. Defaults to `null`.
 * @param causeOf An optional `Throwable` to be used as the cause of the exception. If provided, it will wrap
 *                the `NumberSignException`. Defaults to `null`.
 * @param cause An optional `Throwable` to be included in the `NumberSignException` used for validation. Defaults to `null`.
 * @return The current number (this) if the validation is successful.
 * @throws NumberSignException if the number is not positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validatePositive(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotPositive) throw if (causeOf == null) NumberSignException(property, variableName, message ?: "is not positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(property, variableName, message ?: "is not positive", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current number is positive. If the number is not positive, a `NumberSignException` is thrown.
 *
 * @param property The primary property being validated. This is used for exception context and can be null.
 * @param variable An optional secondary property related to the validation. This is used for additional exception context if provided.
 * @param message  An optional custom error message to include in the exception if the validation fails.
 * @param causeOf  An optional root cause throwable to set as the cause of the exception. If null, a new `NumberSignException` is created.
 * @param cause    An optional secondary cause throwable to include when creating the exception.
 * @return The current instance if the validation passes.
 * @throws NumberSignException If the number is not positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validatePositive(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotPositive) throw if (causeOf == null) NumberSignException(property, variable, message ?: "is not positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(property, variable, message ?: "is not positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is positive and throws a `NumberSignException` if it is not.
 *
 * @param callable The Kotlin function that the parameter belongs to. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message to include in the exception. Defaults to "is not positive" if null.
 * @param causeOf An existing exception to propagate by initializing its cause with a new `NumberSignException`. Can be null.
 * @param cause An optional underlying cause for the exception. Can be null.
 * @return The current number instance if it is confirmed to be positive.
 * @throws NumberSignException if the number is not positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validatePositive(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotPositive) throw if (causeOf == null) NumberSignException(callable, parameterName, message ?: "is not positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callable, parameterName, message ?: "is not positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is positive.
 * If the number is not positive, a `NumberSignException` is thrown.
 *
 * @param callable The function to which the parameter belongs, or null if not applicable.
 * @param parameter The specific parameter within the function being validated, or null if not applicable.
 * @param message An optional descriptive message to include in the exception, or null to use the default message.
 * @param causeOf An exception that will serve as the cause of the `NumberSignException`, or null if not applicable.
 * @param cause The underlying cause to associate with this exception, or null if not applicable.
 * @return The current number if it is positive.
 * @throws NumberSignException If the number is not positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validatePositive(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotPositive) throw if (causeOf == null) NumberSignException(callable, parameter, message ?: "is not positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callable, parameter, message ?: "is not positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is positive. If the number is not positive, a `NumberSignException` is thrown.
 *
 * @param callableName The name of the callable where this validation occurs, or null.
 * @param parameterName The name of the parameter being validated, or null.
 * @param message A custom error message to include in the exception, or null to use the default message.
 * @param causeOf An optional throwable that will serve as the originating cause of the new exception, or null.
 * @param cause An optional throwable used to link exceptions for diagnostic purposes, or null.
 * @return The number if it is positive.
 * @throws NumberSignException If the number is not positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validatePositive(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotPositive) throw if (causeOf == null) NumberSignException(callableName, parameterName, message ?: "is not positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callableName, parameterName, message ?: "is not positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is positive. If the number is not positive, an exception is thrown.
 *
 * @param callableName The name of the callable (e.g., function or method) associated with the validation, or null if not applicable.
 * @param parameter The parameter being validated, or null if not applicable.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to a generic "is not positive" message.
 * @param causeOf An optional pre-existing throwable to be set as the cause of the thrown exception. If null, a new exception is constructed instead.
 * @param cause An optional underlying cause for the validation failure.
 * @return The current number if it passes the validation check.
 * @throws NumberSignException If the number is not positive and no `causeOf` is provided.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validatePositive(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotPositive) throw if (causeOf == null) NumberSignException(callableName, parameter, message ?: "is not positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callableName, parameter, message ?: "is not positive", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current number is not positive. If the number is positive,
 * it throws a `NumberSignException` with the specified cause or an initialized cause.
 *
 * @param causeOf The primary throwable cause to be used if the validation fails.
 *                If non-null, it will be augmented with a `NumberSignException`.
 * @param cause The secondary throwable cause. Used as the cause of the `NumberSignException`
 *              if the number is positive, and `causeOf` is null.
 * @return The current number instance if it is not positive.
 * @throws NumberSignException If the current number is positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotPositive(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isPositive) throw if (causeOf == null) NumberSignException("Value is positive.", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException("Value is positive.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not positive. If the number is positive, an exception is thrown.
 *
 * @param causeOf An optional throwable that will serve as the main cause. If provided, it will be initialized with a
 * secondary cause describing the positive value validation failure.
 * @param cause An optional secondary throwable that may provide additional context for the exception.
 * @param lazyMessage A supplier that generates the message used in the exception if the number is positive.
 * @return The original number if it is not positive.
 * @throws NumberSignException If the number is positive. The exception will use the lazyMessage's output as its
 * message and may include the provided cause and/or causeOf for additional context.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotPositive(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isPositive) throw if (causeOf == null) NumberSignException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is not positive. If the number is positive, a
 * `NumberSignException` is thrown.
 *
 * @param property An optional `KProperty` providing metadata about the property being validated.
 *                 This can include contextual information such as the class, property name, and
 *                 return type. May be `null`.
 * @param variableName An optional name of the variable being validated for additional context.
 *                     If provided, it will be included in the exception message. May be `null`.
 * @param message An optional custom error message that will be appended to the
 *                exception message if the validation fails. May be `null`.
 * @param causeOf An optional `Throwable` that caused this validation error, allowing exception
 *                chaining. If provided, it is set as the cause of the exception. May be `null`.
 * @param cause An optional `Throwable` providing additional context for the exception. May be
 *              `null`.
 * @return Returns the current number if it is not positive.
 * @throws NumberSignException If the current number is positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotPositive(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isPositive) throw if (causeOf == null) NumberSignException(property, variableName, message ?: "is positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(property, variableName, message ?: "is positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not positive.
 *
 * If the number is positive, a [NumberSignException] is thrown. The exception will include the provided property,
 * variable, an optional custom message, and optional cause details.
 *
 * @param property The primary property being validated. Can be `null` if not applicable.
 * @param variable An optional secondary property related to the validation. Can be `null` if not relevant.
 * @param message An optional custom message to include in the exception if thrown. Defaults to "is positive" if `null`.
 * @param causeOf An optional throwable that is considered the cause of this failure. If provided,
 *                it will be initialized as the cause of the created exception.
 * @param cause An optional root cause of this failure. If provided, it will be passed as part of the created exception.
 * @return Returns the same number instance if it is not positive.
 * @throws NumberSignException if the number is positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotPositive(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isPositive) throw if (causeOf == null) NumberSignException(property, variable, message ?: "is positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(property, variable, message ?: "is positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not positive. If the number is positive, a `NumberSignException` is thrown.
 *
 * @param callable The Kotlin function that the number parameter belongs to. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message A custom error message to provide additional context. Defaults to "is positive" if null.
 * @param causeOf A custom throwable that serves as the main exception. If provided, it is initialized with the
 *                `NumberSignException` as its cause.
 * @param cause A secondary throwable that serves as the cause of the exception. This is used if `causeOf` is null.
 * @return The number itself if the validation passes (i.e., the number is not positive).
 * @throws NumberSignException If the number is positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotPositive(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isPositive) throw if (causeOf == null) NumberSignException(callable, parameterName, message ?: "is positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callable, parameterName, message ?: "is positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is not positive. If the number is positive, a `NumberSignException` is thrown.
 *
 * @param callable The function to which the parameter belongs, or null if not available.
 * @param parameter The specific parameter within the function that caused the validation, or null if not applicable.
 * @param message An optional descriptive message providing additional information about the validation failure.
 * @param causeOf An optional exception that acts as the underlying cause of the validation failure.
 * @param cause An optional exception to be used as the direct cause of the thrown `NumberSignException`.
 * @return The current number, if it is not positive.
 * @throws NumberSignException if the number is positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotPositive(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isPositive) throw if (causeOf == null) NumberSignException(callable, parameter, message ?: "is positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callable, parameter, message ?: "is positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not positive. If the number is positive, a `NumberSignException` is thrown.
 *
 * @param callableName The name of the callable associated with this validation, or `null`.
 * @param parameterName The name of the parameter being validated, or `null`.
 * @param message Additional details about the validation failure. Defaults to `null` if not specified.
 * @param causeOf An optional existing `Throwable`, which will have the thrown exception set as its cause.
 * @param cause The throwable that caused this validation to fail, or `null` if there is none.
 * @return The original number if it passes validation (not positive).
 * @throws NumberSignException If the number is positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotPositive(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isPositive) throw if (causeOf == null) NumberSignException(callableName, parameterName, message ?: "is positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callableName, parameterName, message ?: "is positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not positive. If the number is positive, a `NumberSignException` is thrown.
 *
 * @param callableName The name of the callable (e.g., function or method) associated with this validation, or null if not applicable.
 * @param parameter The parameter related to this validation, or null if not applicable.
 * @param message An optional message providing additional context if validation fails; defaults to "is positive" if not specified.
 * @param causeOf The desired root cause of the exception, or null if none exists.
 * @param cause An optional exception to associate as the cause; it will be attached as the `cause` of the thrown exception.
 * @return The validated number if it is not positive.
 * @throws NumberSignException if the number is positive.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotPositive(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isPositive) throw if (causeOf == null) NumberSignException(callableName, parameter, message ?: "is positive", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callableName, parameter, message ?: "is positive", cause?.invoke(this)))
    return this
}

/**
 * Validates that the number is negative. If the number is not negative, an exception is thrown.
 *
 * @param causeOf An optional throwable that will be used as the cause of the exception if provided.
 * @param cause An optional throwable that will be set as the cause of the `NumberSignException` if `causeOf` is not provided.
 * @return The number instance if it is negative.
 * @throws NumberSignException if the number is not negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNegative(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotNegative) throw if (causeOf == null) NumberSignException("Value is not negative.", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException("Value is not negative.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the calling number instance is negative. If the number is not negative,
 * throws a [NumberSignException] with the specified message and optional cause.
 *
 * @param causeOf An optional `Throwable` that, if provided, will have its cause set to
 *                a newly created [NumberSignException].
 * @param cause An optional `Throwable` representing the cause of the exception to be
 *              assigned to the [NumberSignException]. If null, no cause is set in the
 *              exception.
 * @param lazyMessage A supplier that generates the exception message if the number
 *                    fails the validation.
 * @return The current `Number` instance if the validation passes.
 * @throws NumberSignException If the number is not negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNegative(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isNotNegative) throw if (causeOf == null) NumberSignException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is negative. If the number is not negative, a [NumberSignException]
 * is thrown.
 *
 * @param property The Kotlin property associated with the validation, providing contextual information
 *                 such as its owner class, name, and return type. May be `null`.
 * @param variableName An optional name of the variable related to the validation for additional context.
 *                     If provided, it will be included in the exception message. Defaults to `null`.
 * @param message An optional custom error message. If provided, it will be used in the exception.
 *                Defaults to `null`.
 * @param causeOf An optional `Throwable` that caused this validation failure, allowing exception chaining.
 *                If provided, it will be used to initialize the cause of the thrown exception. Defaults to `null`.
 * @param cause An optional `Throwable` to provide additional context in the exception. May be `null`.
 *              Defaults to `null`.
 * @return The current number if it passes the validation (is negative).
 * @throws NumberSignException If the number is not negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNegative(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotNegative) throw if (causeOf == null) NumberSignException(property, variableName, message ?: "is not negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(property, variableName, message ?: "is not negative", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `Number` instance is negative.
 * If the number is not negative, a `NumberSignException` is thrown.
 *
 * @param property The primary property associated with the validation. Can be null.
 * @param variable An optional secondary property used for additional context in the validation. Can be null.
 * @param message A custom error message to provide additional details about the exception. Optional and can be null.
 * @param causeOf An optional throwable that caused this exception. If provided, it is used as the cause of the exception.
 * @param cause An additional optional throwable that will be associated as the cause for the `NumberSignException`. Can be null.
 * @return The current `Number` instance if it is negative.
 * @throws NumberSignException If the number is not negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNegative(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotNegative) throw if (causeOf == null) NumberSignException(property, variable, message ?: "is not negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(property, variable, message ?: "is not negative", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current number is negative. Throws a [NumberSignException] if the number is not negative.
 *
 * @param callable The Kotlin function that the parameter belongs to. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message Custom message providing additional context for the exception. Can be null, defaults to "is not negative".
 * @param causeOf The higher-level cause leading to this validation failure. Can be null.
 * @param cause The underlying exception causing this validation failure. Can be null.
 * @return The current number if the validation passes (i.e., the number is negative).
 * @throws NumberSignException If the current number is not negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNegative(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotNegative) throw if (causeOf == null) NumberSignException(callable, parameterName, message ?: "is not negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callable, parameterName, message ?: "is not negative", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current number is negative. Throws a `NumberSignException` if the number is not negative.
 *
 * @param callable The function to which the parameter belongs, or null if not applicable.
 * @param parameter The specific parameter within the function to be validated, or null if not applicable.
 * @param message An optional message describing the validation failure, or null to use the default message.
 * @param causeOf An optional `Throwable` that serves as the root cause of the exception.
 * @param cause An optional `Throwable` providing additional context for the exception.
 * @return The current number if it is negative.
 * @throws NumberSignException If the number is not negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNegative(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotNegative) throw if (causeOf == null) NumberSignException(callable, parameter, message ?: "is not negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callable, parameter, message ?: "is not negative", cause?.invoke(this)))
    return this
}
/**
 * Validates that a number is negative. If the number is not negative, it throws a `NumberSignException`.
 *
 * @param callableName The name of the callable associated with this validation, or null.
 * @param parameterName The name of the parameter being validated, or null.
 * @param message Additional details about the validation failure; defaults to "is not negative" if null.
 * @param causeOf An optional throwable that caused this validation failure. If provided, it will be chained.
 * @param cause An optional cause for the `NumberSignException`. If provided, it will be associated with the exception.
 * @return The number itself if it is negative.
 * @throws NumberSignException If the number is not negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNegative(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotNegative) throw if (causeOf == null) NumberSignException(callableName, parameterName, message ?: "is not negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callableName, parameterName, message ?: "is not negative", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current number is negative. Throws a `NumberSignException` if the number is not negative.
 *
 * @param callableName The name of the function or callable associated with the validation, or null if not applicable.
 * @param parameter The parameter associated with the validation, or null if not applicable.
 * @param message An optional custom message for the exception, or null to use the default message.
 * @param causeOf The primary cause of the exception, or null if there is no prior throwable causing this validation failure.
 * @param cause The underlying cause of the `NumberSignException`, or null if no additional cause exists.
 * @return The current number (`this`) if it passes the validation check.
 * @throws NumberSignException if the number is not negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNegative(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotNegative) throw if (causeOf == null) NumberSignException(callableName, parameter, message ?: "is not negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callableName, parameter, message ?: "is not negative", cause?.invoke(this)))
    return this
}

/**
 * Validates that the calling number is not negative.
 * If the number is negative, it throws a `NumberSignException`.
 * Optionally, a custom cause or an overriding cause can be provided.
 *
 * @param causeOf An optional throwable used as the primary cause when initializing an exception chain.
 * @param cause An optional throwable used as the secondary cause for the exception.
 * @return The validated number if it is not negative.
 * @throws NumberSignException if the number is negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotNegative(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNegative) throw if (causeOf == null) NumberSignException("Value is negative.", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException("Value is negative.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not negative. If the number is negative, throws a [NumberSignException].
 *
 * @param causeOf An optional throwable to be used as the initial cause for the exception. If null,
 *                [NumberSignException] will be created as the root cause.
 * @param cause An optional throwable that can be specified as the secondary cause for the exception.
 * @param lazyMessage A lazily evaluated message supplier to generate the error message for the exception if thrown.
 * @return The current number if it is not negative.
 * @throws NumberSignException If the number is negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotNegative(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isNegative) throw if (causeOf == null) NumberSignException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is not negative. If the number is negative, a
 * `NumberSignException` is thrown.
 *
 * @param property The Kotlin property associated with the value being validated, providing
 *                 contextual information such as its owner class, name, and return type. May be `null`.
 * @param variableName An optional name for the variable being validated. If provided, this name will
 *                     be included in the exception message for easier identification. May be `null`.
 * @param message An optional custom error message that will be used if the validation fails. Defaults
 *                to "is negative" if not provided. May be `null`.
 * @param causeOf An optional `Throwable` that represents the cause of this validation failure. If not
 *                `null`, it will be initialized with a `NumberSignException` as its cause. May be `null`.
 * @param cause An optional `Throwable` that provides additional context for the default exception chaining. May be `null`.
 * @return The validated number if the validation passes and the number is not negative.
 * @throws NumberSignException If the number is negative, a `NumberSignException` is thrown with
 *                              appropriate contextual information and optional message or cause.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotNegative(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNegative) throw if (causeOf == null) NumberSignException(property, variableName, message ?: "is negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(property, variableName, message ?: "is negative", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is not negative.
 *
 * If the number is negative, a `NumberSignException` is thrown.
 * This method supports optional parameters for providing context about
 * the property or variable related to the exception, a custom message, and
 * an optional cause exception.
 *
 * @param property The primary property associated with this validation. Can be `null`.
 * @param variable An optional secondary property providing additional context. Can be `null`.
 * @param message A custom message to describe the exception if the number is negative. Defaults to `null`.
 * @param causeOf An optional throwable to serve as the cause of the exception. Defaults to `null`.
 * @param cause An optional secondary cause to provide further context. Defaults to `null`.
 * @return The current number if it is not negative.
 * @throws NumberSignException If the number is negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotNegative(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNegative) throw if (causeOf == null) NumberSignException(property, variable, message ?: "is negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(property, variable, message ?: "is negative", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not negative.
 *
 * @param callable The function in which the validation is performed. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional message to customize the exception message if validation fails. Defaults to "is negative" if null.
 * @param causeOf The prior throwable cause, used to initialize the chain of exceptions. Can be null.
 * @param cause The root cause of the exception, if applicable. Can be null.
 * @return The number itself if it is not negative.
 * @throws NumberSignException If the number is negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotNegative(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNegative) throw if (causeOf == null) NumberSignException(callable, parameterName, message ?: "is negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callable, parameterName, message ?: "is negative", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is not negative. If the number is negative, a [NumberSignException] is thrown.
 *
 * @param callable The function to which the parameter belongs, or null if not applicable.
 * @param parameter The parameter involved in the validation, or null if not applicable.
 * @param message An optional message to include in the exception, or null for a default message ("is negative").
 * @param causeOf An optional throwable indicating the root cause of this validation failure, or null if not applicable.
 * @param cause An optional throwable to be set as the cause of the exception, or null if not applicable.
 * @return The current number if it is not negative.
 *
 * @throws NumberSignException if the number is negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotNegative(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNegative) throw if (causeOf == null) NumberSignException(callable, parameter, message ?: "is negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callable, parameter, message ?: "is negative", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is not negative. If the number is negative, a
 * `NumberSignException` is thrown. The exception can optionally include information about the
 * callable, parameter, custom message, and cause.
 *
 * @param callableName The name of the callable in which the validation is performed, or null.
 * @param parameterName The name of the parameter being validated, or null.
 * @param message An optional custom message to include in the exception if the validation fails.
 * @param causeOf An optional `Throwable` that serves as the primary cause of the exception.
 * @param cause An optional `Throwable` providing additional context for the exception.
 * @return The original number if the validation passes (i.e., the number is not negative).
 * @throws NumberSignException If the number is negative and validation fails.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotNegative(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNegative) throw if (causeOf == null) NumberSignException(callableName, parameterName, message ?: "is negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callableName, parameterName, message ?: "is negative", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not negative. If the number is negative, throws a `NumberSignException`.
 *
 * @param callableName The name of the callable (e.g., function or method) associated with the value, or null if unassociated.
 * @param parameter The parameter involved in the validation, or null if not applicable.
 * @param message An optional detail message to include with the exception, or null for a default message.
 * @param causeOf An optional throwable that serves as the primary cause of this exception, or null if not applicable.
 * @param cause An optional secondary cause for the exception, or null if not applicable.
 * @return The validated number if it is not negative.
 * @throws NumberSignException If the number is negative.
 * @since 3.5.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotNegative(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNegative) throw if (causeOf == null) NumberSignException(callableName, parameter, message ?: "is negative", cause?.invoke(this)) else causeOf(this).initCause(NumberSignException(callableName, parameter, message ?: "is negative", cause?.invoke(this)))
    return this
}

/**
 * Validates whether the number is even. If the number is odd, a `ValidationFailedException` is thrown.
 * This method allows customization of the exception through optional transformers for the cause.
 *
 * @param causeOf an optional transformer that takes the current value and returns a `Throwable` to be used as the cause of the exception.
 * @param cause an optional transformer that takes the current value and returns a `Throwable` to be used as the secondary or underlying cause.
 * @return the validated number if it is even.
 * @throws ValidationFailedException if the number is odd. The exception includes a message and an optional cause, based on the provided transformers.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateEven(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isOdd) throw if (causeOf == null) ValidationFailedException("Value is odd.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is odd.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the number is even. If the number fails the validation, a `ValidationFailedException`
 * is thrown with an optional message and cause.
 *
 * @param causeOf A transformer that returns a throwable to be used as the primary cause
 *                when the validation fails. Can be `null`.
 * @param cause A transformer that returns a throwable to be used as the secondary
 *              underlying cause for the exception. Can be `null`.
 * @param lazyMessage A transformer that generates an error message if the validation fails.
 *                    This provides a custom message associated with the failure.
 * @return The original number if it passes the validation.
 * @throws ValidationFailedException If the validation fails due to the number not being even.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateEven(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isOdd) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the number is even. If the number is odd, an exception is thrown.
 *
 * @param property The property associated with the value being validated. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Can be null if not provided.
 * @param message An optional custom error message to include in the exception if the validation fails. Defaults to "is odd".
 * @param causeOf A transformer that generates a throwable from the current value to be used as the exception cause, or null if not applicable.
 * @param cause An optional transformer for generating a throwable to act as the cause of the validation failure, or null if not provided.
 * @return The original number if it is even.
 * @throws ValidationFailedException If the number is odd.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateEven(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isOdd) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is odd", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is odd", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is even. If the number is odd, a [ValidationFailedException] is thrown.
 *
 * @param property the primary [KProperty] associated with the validation, used to provide property-related information in the exception, or null if not applicable.
 * @param variable an optional secondary [KProperty] providing additional context about the validation, or null if not applicable.
 * @param message an optional message to include in the exception if validation fails, or null to use the default "is odd" message.
 * @param causeOf an optional transformer to generate a throwable cause when the validation fails, or null if no custom cause generator is needed.
 * @param cause an optional transformer for creating a throwable cause for the exception, or null if no cause is provided.
 * @return the current number if it successfully passes the validation.
 * @throws ValidationFailedException if the number is odd and fails the validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateEven(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isOdd) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is odd", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is odd", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is even. If the number is odd, it throws a `ValidationFailedException`.
 *
 * @param callable The function context (`KFunction`) associated with the validation. Can be null.
 * @param parameterName The name of the parameter in the provided function that is being validated. Can be null.
 * @param message An optional custom message to include in the exception if the validation fails. Defaults to "is odd".
 * @param causeOf An optional transformer function to generate the cause of the exception from the current number. Can be null.
 * @param cause An optional transformer function to generate an additional cause for the exception from the current number. Can be null.
 * @return The current number if it is even.
 * @throws ValidationFailedException If the number is odd and the validation fails.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateEven(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isOdd) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is odd", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is odd", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is even. If the number is odd, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] associated with the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional custom error message to include in the exception if validation fails
 * @param causeOf a transformer function to generate the cause of the validation failure based on the current value, or null if not applicable
 * @param cause a transformer function to generate an additional cause of the validation failure, or null if not applicable
 * @return the validated number if it is even
 * @throws ValidationFailedException if the number is odd
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateEven(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isOdd) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is odd", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is odd", cause?.invoke(this)))
    return this
}
/**
 * Validates if a numerical value is even. If the value is odd, it throws a `ValidationFailedException`.
 *
 * @param callableName The name of the callable (e.g., function or method) where validation is performed.
 * @param parameterName The name of the parameter being validated, or `null` if not applicable.
 * @param message An optional custom message describing the validation failure, or `null` for the default message.
 * @param causeOf A transformer that provides the root cause of the validation failure, or `null` if not used.
 * @param cause A transformer that provides an additional cause of the validation failure, or `null` if not used.
 * @return The original number if it passes the validation check (i.e., if it is even).
 * @throws ValidationFailedException If the value is odd.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateEven(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isOdd) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is odd", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is odd", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current numeric value is even. If the value is odd, a
 * `ValidationFailedException` is thrown.
 *
 * @param callableName The name of the callable (e.g., function or property) where validation is performed, or null if not provided.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message providing additional details about the validation failure. Defaults to "is odd" if not specified.
 * @param causeOf A transformer function that generates the cause of the exception based on the current numeric value, or null if no cause is derived this way.
 * @param cause A transformer function that generates an additional cause of the exception based on the current numeric value, or null if not specified.
 * @return The original numeric value if it passes validation.
 * @throws ValidationFailedException if the numeric value is odd.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateEven(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isOdd) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is odd", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is odd", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current number is odd. If the number is even, throws a [ValidationFailedException].
 *
 * @param causeOf an optional transformer that provides a custom throwable based on the current value
 *                when the validation fails.
 * @param cause an optional transformer that provides an additional underlying cause throwable
 *              when the validation fails.
 * @return the current number if it is odd.
 * @throws ValidationFailedException if the number is even.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateOdd(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEven) throw if (causeOf == null) ValidationFailedException("Value is even.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is even.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current number is odd. If the number is even, a `ValidationFailedException`
 * is thrown with the provided message and optional cause.
 *
 * @param causeOf A transformer that produces a specific exception to be thrown if the validation fails.
 *                It takes the current number as input and transforms it into an exception.
 * @param cause A transformer for providing the root cause of the validation failure.
 *              It takes the current number as input and transforms it into an exception.
 * @param lazyMessage A transformer that generates a message explaining the validation failure.
 *                    It takes the current number as input and transforms it into a message object.
 * @return The current number if it passes the validation (i.e., it is odd).
 * @throws ValidationFailedException if the number is even and the validation fails.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateOdd(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isEven) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is odd. If the number is even, a `ValidationFailedException` is thrown.
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName The variable name to include in the validation error message. Defaults to null.
 * @param message An optional custom message to include in the exception if the validation fails. Defaults to null.
 * @param causeOf A transformer function to generate an exception from the number, used as the cause. Defaults to null.
 * @param cause A transformer function to generate the underlying cause of the `ValidationFailedException`. Defaults to null.
 * @return The number itself if validation succeeds (i.e., the number is odd).
 * @throws ValidationFailedException if the number is even.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateOdd(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEven) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is even", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is even", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is odd. If the number is even, a `ValidationFailedException` is thrown.
 *
 * @param property the primary [KProperty] associated with the validation, or null if not specified.
 * @param variable an optional secondary [KProperty] providing additional context for the validation, or null if not specified.
 * @param message an optional message that provides details about the validation failure, or null if not specified.
 * @param causeOf an optional transformer function that generates a `Throwable` to be used as the root cause of the `ValidationFailedException`, or null if not specified.
 * @param cause an optional transformer function that generates a `Throwable` to be used as the direct cause of the `ValidationFailedException`, or null if not specified.
 * @return the current number if the validation passes (i.e., the number is odd).
 * @throws ValidationFailedException if the number is even.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateOdd(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEven) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is even", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is even", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the number is odd and throws a validation exception if it is even.
 *
 * @param callable The Kotlin function (`KFunction`) that the validation is associated with. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message for the validation failure. Default is null.
 * @param causeOf An optional transformer that generates a custom exception based on the number being validated. Can be null.
 * @param cause An optional transformer that generates the underlying cause (`Throwable`) for the validation failure. Can be null.
 * @return The validated number if it is odd.
 * @throws ValidationFailedException If the number is even.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateOdd(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEven) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is even", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is even", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is odd. If the number is even, a [ValidationFailedException] is thrown.
 *
 * @param callable The [KFunction] related to the validation, or null if not applicable.
 * @param parameter The [KParameter] representing the parameter being validated, or null if not applicable.
 * @param message An optional message providing additional context for the validation failure. Defaults to null.
 * @param causeOf An optional transformer that produces a [Throwable] for the failure cause. Defaults to null.
 * @param cause An optional transformer that produces a [Throwable] to be used as the underlying cause of the exception. Defaults to null.
 * @return The current number if validation passes.
 * @throws ValidationFailedException if the number is even.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateOdd(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEven) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is even", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is even", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is odd. Throws a `ValidationFailedException` if the number is even.
 *
 * @param callableName the name of the callable (e.g., function or method) related to the validation.
 * @param parameterName the name of the parameter being validated, or null if not applicable.
 * @param message an optional message to include in the exception detailing the validation failure.
 * @param causeOf a transformer that generates a throwable to be thrown as the cause upon validation failure.
 * @param cause an optional transformer to specify the underlying cause of the validation failure.
 * @return the same number if it passes the validation.
 * @throws ValidationFailedException if the number is even.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateOdd(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEven) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is even", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is even", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is odd. If the number is even, a `ValidationFailedException` is thrown.
 *
 * @param callableName The name of the callable (function or property) associated with this validation, or null if not specified.
 * @param parameter The `KParameter` related to the validation, or null if not applicable.
 * @param message An optional error message that will be included in the exception if the validation fails. Defaults to "is even".
 * @param causeOf A transformer function that takes the current number and returns a `Throwable` to use as the main exception.
 *                If null, a `ValidationFailedException` is created.
 * @param cause A transformer function that takes the current number and returns a `Throwable` to be used as the cause of the validation exception. If null, no root cause is specified
 * .
 * @return The current number if it passes the validation (i.e., if it is odd).
 * @throws ValidationFailedException If the number is even, an exception is thrown containing validation details.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateOdd(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isEven) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is even", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is even", cause?.invoke(this)))
    return this
}

/**
 * Validates if the current number is a decimal. If the number is not a decimal,
 * this method throws a `ValidationFailedException`.
 *
 * @param causeOf A transformer that generates a throwable based on this number, used if the validation fails. Can be null.
 * @param cause A transformer that generates a throwable based on this number, used as the cause of the `ValidationFailedException`. Can be null.
 * @return The current number if it passes the decimal validation.
 * @throws ValidationFailedException if the current number is not a decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateDecimal(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotDecimal) throw if (causeOf == null) ValidationFailedException("Value is not decimal.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is not decimal.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current number is a decimal. If the number is not a decimal,
 * it throws a `ValidationFailedException` with an appropriate message and an optional cause.
 *
 * @param causeOf An optional transformer function to produce a custom throwable as the root cause of the validation failure.
 * @param cause An optional transformer function to produce a throwable to be used as the direct cause of the validation failure.
 * @param lazyMessage A transformer function to produce a custom error message when validation fails.
 * @return The validated number if it is a decimal.
 * @throws ValidationFailedException if the number is not a decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateDecimal(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isNotDecimal) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current number is a valid decimal value.
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName An optional name for the variable being validated. Defaults to null.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to null.
 * @param causeOf A transformer that generates an exception to provide additional context if validation fails. Defaults to null.
 * @param cause A transformer that provides the underlying cause of the exception if validation fails. Defaults to null.
 * @return The validated number if it meets the decimal validation criteria.
 * @throws ValidationFailedException If the number is not a valid decimal, with an optional message and cause.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateDecimal(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotDecimal) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not decimal", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current number is a decimal value. If the number is not a decimal, a validation exception is thrown.
 *
 * @param property the main property associated with the validation process, or `null` if not specified
 * @param variable an optional secondary property providing additional context, or `null` if not specified
 * @param message an optional custom message that describes the validation failure; defaults to "is not decimal" if not provided
 * @param causeOf an optional transformer function that generates the primary cause of the exception from the current value; can be `null`
 * @param cause an optional transformer function that generates an additional cause of the exception from the current value; can be `null`
 * @return the current number if it passes the decimal validation
 * @throws ValidationFailedException if the current number is not a decimal
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateDecimal(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotDecimal) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not decimal", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given numeric value is a decimal. Throws a `ValidationFailedException` if the value is not a decimal.
 *
 * @param callable The Kotlin function (`KFunction`) where the validation is applied. Can be null.
 * @param parameterName The name of the parameter being validated. Defaults to null.
 * @param message An optional custom message describing the validation error. Defaults to null.
 * @param causeOf An optional transformer to generate a `Throwable` representing the cause of the validation failure from the input value. Defaults to null.
 * @param cause An optional transformer to create a `Throwable` as a secondary cause of the validation failure from the input value. Defaults to null.
 * @return The input value if it passes the decimal validation.
 * @throws ValidationFailedException If the input value is not a decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateDecimal(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotDecimal) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not decimal", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is a decimal. Throws a [ValidationFailedException] if the validation fails.
 *
 * @param callable the [KFunction] related to the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional message providing additional context about the validation failure
 * @param causeOf a transformer to generate the exception caused by this validation, or null if not applicable
 * @param cause a transformer to generate the root cause of the exception, or null if not applicable
 * @return the current number if validation passes
 * @throws ValidationFailedException if the number is not a decimal
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateDecimal(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotDecimal) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not decimal", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the number is a decimal.
 * Throws a [ValidationFailedException] if the number is not a decimal.
 *
 * @param callableName The name of the callable (e.g., function or method) being validated.
 * @param parameterName The name of the parameter associated with this validation, or null if not applicable.
 * @param message An optional custom message to include when the validation fails.
 * @param causeOf A transformer function to generate the throwable that acts as the root cause of the failure, or null.
 * @param cause A transformer function to generate additional context about the failure, or null.
 * @return The validated number if it passes the decimal check.
 * @throws ValidationFailedException If the number is not a decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateDecimal(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotDecimal) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not decimal", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current number is a decimal value. If the number is not a decimal,
 * it throws a `ValidationFailedException` with the provided details.
 *
 * @param callableName The name of the callable (function, property, etc.) associated with the validation, or null if unspecified.
 * @param parameter The KParameter instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message to include in the exception if validation fails. Defaults to "is not decimal".
 * @param causeOf A transformer that generates the cause of the exception when validation fails, or null if not provided.
 * @param cause A transformer that provides additional context/cause for the validation failure, or null if not provided.
 * @return The original number if it is a decimal.
 * @throws ValidationFailedException if the number is not a decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateDecimal(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotDecimal) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not decimal", cause?.invoke(this)))
    return this
}

/**
 * Validates that the number is not a decimal (non-integral value). If the number is a decimal,
 * an exception is thrown with the specified cause or a default error message.
 *
 * @param causeOf an optional transformer that generates a Throwable based on the invalid number.
 *                If not provided, a default exception will be used.
 * @param cause an optional transformer that generates a Throwable cause based on the invalid number.
 *              If not provided, no underlying cause will be set in the exception.
 * @return the original number if it is not a decimal.
 * @throws ValidationFailedException if the number is a decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotDecimal(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isDecimal) throw if (causeOf == null) ValidationFailedException("Value is decimal.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is decimal.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not a decimal value. If the number is decimal, an exception
 * is thrown with an optional custom cause and error message.
 *
 * @param causeOf A transformer function that generates a specific exception based on the current value.
 *                If null, a default `ValidationFailedException` is used.
 * @param cause A transformer function that generates a cause for the validation exception, based on the current value.
 *              If null, no cause is associated with the exception.
 * @param lazyMessage A transformer function that generates a custom error message based on the current value.
 * @return The current value if validation succeeds (i.e., the number is not a decimal).
 * @throws ValidationFailedException If the number is decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotDecimal(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isDecimal) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking number is not a decimal.
 *
 * If the number is a decimal (e.g., a floating-point number), the method throws a
 * [ValidationFailedException]. The exception message could include property, variable name,
 * and an additional message if provided. Optionally, you can define custom transformations for
 * the exception using the `causeOf` and `cause` parameters.
 *
 * @param property The KProperty instance associated with the validation. Can be null if not applicable.
 * @param variableName An optional name of the variable involved in the validation.
 *                     Included in the exception message if not null.
 * @param message An optional descriptive message to provide additional information about the validation failure.
 *                Defaults to "is decimal" if not provided.
 * @param causeOf An optional transformer function to generate the root cause of the exception.
 * @param cause An optional transformer function to generate a cause for the exception.
 * @return The original number if the validation passes, i.e., if it is not a decimal.
 * @throws ValidationFailedException if the number is a decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotDecimal(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isDecimal) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is decimal", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not a decimal value. If the number is a decimal, a
 * `ValidationFailedException` is thrown with the specified details.
 *
 * @param property the main `KProperty` associated with the validation, or null if not specified
 * @param variable an optional secondary `KProperty` providing additional context, or null if not specified
 * @param message an optional message providing details about the validation failure, or null if not specified
 * @param causeOf an optional transformer function that maps the current value to a `Throwable` cause for the exception, or null if not specified
 * @param cause an optional transformer function used to generate a secondary cause for the exception, or null if not specified
 * @return the validated number if it is not a decimal
 * @throws ValidationFailedException if the number is a decimal
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotDecimal(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isDecimal) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is decimal", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not a decimal value. Throws a `ValidationFailedException` if the check fails.
 *
 * @param callable The Kotlin function (`KFunction`) to which this validation is related. Can be nullable.
 * @param parameterName The name of the parameter being validated (if applicable). Can be nullable.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to "is decimal".
 * @param causeOf A transformer to generate a `Throwable` cause based on the current value when validation fails. Can be nullable.
 * @param cause An alternate transformer to generate a `Throwable` cause based on the current value when validation fails. Can be nullable.
 * @return The same number if it is not a decimal value.
 * @throws ValidationFailedException if the number is a decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotDecimal(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isDecimal) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is decimal", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is not a decimal. If the number is decimal, a validation exception is thrown.
 *
 * @param callable the [KFunction] related to the validation context, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter involved in the validation, or null if not applicable.
 * @param message an optional custom message to include in the validation exception, or null to use the default message.
 * @param causeOf an optional transformer to customize the generated exception when the validation fails, or null for the default behavior.
 * @param cause an optional transformer to customize the cause of the exception, or null for no specific cause.
 * @return the current number if it passes validation.
 * @throws ValidationFailedException if the number is a decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotDecimal(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isDecimal) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is decimal", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not a decimal value. If the number is a decimal, an exception is thrown.
 *
 * @param callableName The name of the callable where the validation is occurring, or null if not applicable.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom message for the validation failure; defaults to "is decimal" if not specified.
 * @param causeOf An optional transformer to provide the underlying cause of the exception, or null if not applicable.
 * @param cause An optional transformer to generate an additional cause of the exception, or null if not applicable.
 * @return The current number if it passes the validation (i.e., it is not a decimal).
 * @throws ValidationFailedException If the number is a decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotDecimal(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isDecimal) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is decimal", cause?.invoke(this)))
    return this
}
/**
 * Validates that the number is not a decimal (e.g., not a floating-point value).
 * If the number is a decimal, a `ValidationFailedException` is thrown.
 *
 * @param callableName The name of the callable where the validation is being performed, or null if not specified.
 * @param parameter The `KParameter` representing the parameter being validated, or null if not applicable.
 * @param message An optional error message to include in the exception, providing context for the validation failure.
 * @param causeOf A transformer for creating a custom exception from this number, or null if not applicable.
 * @param cause A transformer for determining the underlying cause of the exception, or null if not applicable.
 * @return The number itself if the validation passes.
 * @throws ValidationFailedException if the number is a decimal.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.validateNotDecimal(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isDecimal) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is decimal", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is decimal", cause?.invoke(this)))
    return this
}

/**
 * Validates that the integer value falls within the specified range.
 * If the value is outside the range, an exception is thrown.
 *
 * @param range The range within which the integer must lie.
 * @param causeOf A transformer function that, when provided, generates a throwable
 *                based on the current value if validation fails. Defaults to `null`.
 * @param cause A transformer function that generates additional cause information
 *              for the exception if validation fails. Defaults to `null`.
 * @return The validated integer value if it falls within the specified range.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRange, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is within the specified range. If the integer is not within the range,
 * a `NumberOutOfRangeException` is thrown.
 *
 * @param range The range within which the integer must fall.
 * @param causeOf An optional transformer to generate a specific `Throwable` as the cause of the exception.
 * @param cause An optional transformer to produce a specific cause when constructing the `NumberOutOfRangeException`.
 * @param lazyMessage A transformer to generate a lazy message describing the validation failure.
 * @return The integer itself if it passes validation.
 * @throws NumberOutOfRangeException if the integer is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRange, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null, lazyMessage: Transformer<Int, Any>): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the invoking integer is within the specified range.
 * Throws a `NumberOutOfRangeException` if the validation fails.
 *
 * @param range The range of valid integers to check against.
 * @param property An optional `KProperty` reference associated with the validation.
 *                 This can be used to include additional context in the exception message.
 * @param variableName The optional name of the variable being validated. This, if provided,
 *                     will appear in the exception message for better clarity.
 * @param message An optional custom error message to provide additional details about the failure.
 * @param causeOf An optional transformer to construct the exception as a result of the validation failure.
 * @param cause An optional transformer to specify the root cause of the validation failure.
 *
 * @return The same integer value if it passes validation within the specified range.
 *
 * @throws NumberOutOfRangeException If the integer is not within the given range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRange, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the integer value is within the specified range, throwing an exception if it is not.
 *
 * @param range The range of valid values to check against.
 * @param property Optional metadata about the primary property associated with the validation.
 * @param variable Optional metadata about a secondary variable involved in the validation.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf An optional transformer function to provide a custom exception based on the input value when validation fails.
 * @param cause An optional transformer function to define the underlying cause of the thrown exception.
 * @return The integer value, if it is within the specified range.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRange, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer value is within the specified range, throwing an exception if it is not.
 *
 * This method allows additional context to be provided, such as the callable function and parameter name,
 * to help identify the origin of the validation failure. Optional custom messages or exception transformation mechanisms
 * can also be specified to customize the behavior when the validation fails.
 *
 * @param range The acceptable range of values (inclusive). The integer value will be checked against this range.
 * @param callable The KFunction instance representing the callable function associated with this validation.
 *                 Can be null if no specific callable context is required.
 * @param parameterName The name of the parameter being validated, if applicable. Optional, defaults to null.
 * @param message A custom error message to include in the thrown exception if validation fails. Optional, defaults to null.
 * @param causeOf A transformation function to create a custom throwable cause, with the value being validated as input.
 *                Optional, defaults to null.
 * @param cause A transformation function to create a secondary throwable cause, with the value being validated as input.
 *              Optional, defaults to null.
 * @return The integer value if it is within the specified range.
 * @throws NumberOutOfRangeException If the integer value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRange, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current integer falls within the provided range. If not, a `NumberOutOfRangeException`
 * is thrown with optional details such as the callable function, the specific parameter, a custom message,
 * and/or underlying cause exceptions.
 *
 * @param range The range of valid integers. The value must lie within this range to pass validation.
 * @param callable The KFunction representing the function to which this validation is related. It may be null.
 * @param parameter The specific KParameter being validated. It may be null.
 * @param message An optional custom message to describe the validation failure. Defaults to a standard message
 * if not provided.
 * @param causeOf An optional transformer that generates a `Throwable` cause based on the integer that failed validation.
 * If provided, this takes precedence over the `cause` parameter.
 * @param cause An optional transformer to produce a `Throwable` cause based on the integer that failed validation.
 * This is used only if `causeOf` is null.
 * @return The validated integer, guaranteed to be within the specified range.
 * @throws NumberOutOfRangeException If the integer does not lie within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRange, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the integer value is within the specified range. If the value is not within the
 * range, a `NumberOutOfRangeException` is thrown.
 *
 * @param range The range of valid values.
 * @param callableName The name of the callable or function where the validation occurs, or null if unavailable.
 * @param parameterName The name of the parameter being validated, or null if unavailable. Default is null.
 * @param message An optional custom error message to include in the exception. Default is null.
 * @param causeOf A transformer that generates a custom exception using the invalid value,
 *                or null if unavailable. Default is null.
 * @param cause A transformer that generates the cause of the exception, or null if unavailable. Default is null.
 * @return The validated integer value if it falls within the specified range.
 * @throws NumberOutOfRangeException If the integer value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRange, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling integer is within the specified range. If the integer
 * is not within the range, throws a `NumberOutOfRangeException`.
 *
 * @param range The range in which the integer is expected to fall.
 * @param callableName The name of the callable function in which the validation is performed, or null if not applicable.
 * @param parameter The parameter of the callable function that is being validated, or null if not applicable.
 * @param message Optional custom error message to be included in the exception if validation fails.
 * @param causeOf A transformer to provide a custom exception cause when the validation fails. If null, default behavior is applied.
 * @param cause A transformer to provide an additional cause for the exception if validation fails. If null, default behavior is applied.
 * @return The validated integer if it falls within the specified range.
 * @throws NumberOutOfRangeException If the integer is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRange, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the integer value is within the specified range with exclusions. If the value is not
 * within the range, an exception is thrown.
 *
 * @param range The range with optional exclusions to validate the integer against.
 * @param causeOf An optional transformer to provide a specific throwable based on the integer value when it is out of range.
 * @param cause An optional transformer to provide additional context for the exception when the value is out of range.
 * @return The integer value itself if it is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithExclusions, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the integer falls within the specified range, considering exclusions, and throws an exception if it does not.
 *
 * @param range The range with exclusions to validate the integer against.
 * @param causeOf An optional transformer to create a specific exception from the given value if the validation fails.
 * @param cause An optional transformer to provide a cause for the exception if the validation fails.
 * @param lazyMessage A transformer providing a message to describe the failed validation, based on the integer value.
 * @return The integer itself if validation passes successfully.
 * @throws NumberOutOfRangeException If the integer does not lie within the specified range or falls in the excluded values.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithExclusions, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null, lazyMessage: Transformer<Int, Any>): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the integer is within the specified range, taking into account any exclusions
 * defined within the range. If the integer is not valid, an exception is thrown.
 *
 * @param range The range within which the integer must be validated. It supports exclusions that define
 * a set of values to be excluded from the range.
 * @param property The property associated with the validating integer, if available. Used for detailed exception information.
 * @param variableName The name of the variable being validated, if provided. Used for exception messages.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf A transformer responsible for generating a specific throwable instance to describe
 * the reason for validation failure.
 * @param cause A transformer for generating a more detailed throwable instance, which may serve as
 * the cause for a primary validation exception.
 * @return The integer being validated if it passes the validation checks.
 * @throws NumberOutOfRangeException If the integer does not fall within the specified range or its exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithExclusions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current integer value is within the specified range, considering any exclusions defined.
 * Throws an exception if the value is out of range.
 *
 * @param range The range, including any exclusions, against which the integer value should be validated.
 * @param property An optional property that can be referenced for additional context while validating.
 * @param variable An optional variable that can be referenced for additional context while validating.
 * @param message An optional custom message to include in the exception if the validation fails.
 * @param causeOf An optional transformer that generates a custom exception for the validation failure, which
 *                may wrap additional contextual information.
 * @param cause An optional transformer that generates the underlying cause for the validation failure.
 * @return The validated integer if it lies within the specified range.
 * @throws NumberOutOfRangeException if the integer value is outside the specified range or exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithExclusions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current integer is within a specified range with optional exclusions.
 * If the integer is not within the range, a custom exception is thrown.
 *
 * @param range The range (with optional exclusions) in which the integer is expected to be.
 * @param callable The optional callable representation used to provide context for the validation error.
 * @param parameterName An optional name of the parameter being validated for additional context in the error.
 * @param message An optional custom message for the exception if the validation fails.
 * @param causeOf An optional transformer to create a custom exception based on the input value if the validation fails.
 * @param cause An optional transformer to create a custom cause for the exception if the validation fails.
 * @return The validated integer if it is within the specified range.
 * @throws NumberOutOfRangeException If the integer is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithExclusions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the integer is within a specified range, considering possible exclusions.
 * If the integer is not in the range, an exception is thrown.
 *
 * @param range The range with optional exclusions to validate against.
 * @param callable The callable (e.g., function or method) associated with the validation (optional).
 * @param parameter The parameter being validated, if applicable (optional).
 * @param message An optional custom error message to include in the exception.
 * @param causeOf A transformer that generates the root cause of the exception to be thrown, based on the invalid value (optional).
 * @param cause A transformer that generates the general cause of the exception to be thrown, based on the invalid value (optional).
 * @return The validated integer if it is within the specified range.
 * @throws NumberOutOfRangeException If the integer is not within the range or is excluded.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithExclusions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the integer value is within the given range of allowed values.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range The range of valid integers, which may include exclusions.
 * @param callableName The name of the function or callable where the validation is performed.
 * @param parameterName The name of the parameter being validated (optional).
 * @param message A custom error message to include in the exception if validation fails (optional).
 * @param causeOf A transformer function that generates a specific throwable based on the integer value if validation fails (optional).
 * @param cause A transformer function that generates a throwable to be used as the cause in the exception if validation fails (optional).
 * @return The integer value itself if it is within the specified range.
 * @throws NumberOutOfRangeException if the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithExclusions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the integer falls within the specified range, considering exclusions,
 * and throws an exception if it does not. This method can use custom exception transformers
 * for advanced error handling.
 *
 * @param range The range of valid values, including the exclusions to be ignored.
 * @param callableName The name of the callable function being validated, or null if not applicable.
 * @param parameter The parameter metadata being validated, or null if not applicable.
 * @param message Optional custom error message to include in the exception, or null to use a default message.
 * @param causeOf An optional transformer that generates the root cause exception if validation fails.
 * @param cause An optional transformer to create additional causal information about the failure.
 * @return The validated integer if it falls within the specified range.
 * @throws NumberOutOfRangeException If the integer does not fall within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithExclusions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates if the integer is within the specified range. If the integer is outside the range, a custom exception
 * will be thrown based on the provided transformers for the cause.
 *
 * @param range The range against which the integer is validated.
 * @param causeOf A transformer that generates a Throwable based on the integer value, used as the primary cause of the exception.
 * @param cause A transformer that generates a Throwable based on the integer value, used as a secondary cause of the exception.
 * @return The validated integer if it lies within the specified range.
 * @throws NumberOutOfRangeException if the integer is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithConditions, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the integer value is within the specified range.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range the range to validate the integer against, which may include conditions.
 * @param causeOf an optional transformer to generate a throwable based on the integer, used as the cause of the exception.
 * @param cause an optional transformer to generate a throwable based on the integer, used within the exception context.
 * @param lazyMessage a transformer function to generate a custom error message when the validation fails.
 * @return the integer value itself if it is within the specified range.
 * @throws NumberOutOfRangeException if the integer is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithConditions, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null, lazyMessage: Transformer<Int, Any>): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the integer is within the provided range and throws an exception if it is not.
 *
 * @param range The range with conditions within which the integer is validated.
 * @param property The optional property reference used to provide more context about the value being validated.
 * @param variableName The optional variable name used in error messages to provide more context.
 * @param message The optional custom error message to include in the exception if the validation fails.
 * @param causeOf The optional transformer that generates a throwable to explain why the validation failed.
 * @param cause The optional transformer that generates a throwable as a secondary cause.
 * @return The integer itself if it passes the validation.
 * @throws NumberOutOfRangeException if the integer is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithConditions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the integer is within the specified range and throws an exception if it is not.
 *
 * @param range The range with conditions that defines the bounds within which the integer must lie.
 * @param property An optional property representing the integer being validated.
 * @param variable An optional variable representing additional contextual information.
 * @param message An optional custom message for the exception if the integer is out of range.
 * @param causeOf An optional transformer function to determine the cause of the exception.
 * @param cause An optional transformer function to generate the root cause of the exception when it is thrown.
 * @return The integer itself if it is within the specified range.
 * @throws NumberOutOfRangeException if the integer is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithConditions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the integer is within the specified range and throws an exception if it is not.
 *
 * @param range The range of acceptable values, including potential conditions.
 * @param callable An optional reference to the function where the validation is being performed.
 * @param parameterName The name of the parameter being validated, if applicable.
 * @param message An optional custom message for the exception if validation fails.
 * @param causeOf An optional transformer to generate a throwable cause based on the input value.
 * @param cause An optional transformer to produce additional context for the exception.
 * @return The integer being validated if it passes the range check.
 * @throws NumberOutOfRangeException If the integer does not fall within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithConditions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the integer falls within a specified range and, if not, throws an exception.
 *
 * @param range The range of valid integer values along with additional conditions.
 * @param callable The function context from which this validation is invoked, if applicable.
 * @param parameter The parameter associated with this validation, if any.
 * @param message An optional message to use for the exception if the validation fails.
 * @param causeOf An optional transformer that generates a custom exception from the integer value.
 * @param cause An optional transformer that generates a cause for the custom exception from the integer value.
 * @return The integer value if it is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithConditions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the integer value is within the specified range and throws an exception if it is not.
 *
 * @param range The range of valid integer values. Must be of type `IntRangeWithConditions`.
 * @param callableName The name of the callable or function where the validation occurs. Can be null.
 * @param parameterName The name of the parameter being validated. Defaults to null if not provided.
 * @param message An optional custom error message to be used within the exception. Defaults to null.
 * @param causeOf A transformer function that generates a custom exception based on the integer value. Defaults to null.
 * @param cause A transformer function that generates a causative exception based on the integer value. Defaults to null.
 * @return The integer value if it is within the range.
 * @throws NumberOutOfRangeException if the integer value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithConditions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the value of the integer invoking this method is within the specified range.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range The range object with conditions that the integer is validated against.
 * @param callableName (Optional) The name of the callable for context in error reporting.
 * @param parameter (Optional) The parameter reference for context in error reporting.
 * @param message (Optional) A custom message for the exception, if the validation fails.
 * @param causeOf (Optional) A transformer to generate a throwable cause when the validation fails.
 * @param cause (Optional) A transformer to provide an additional throwable cause when validation fails.
 * @return The integer value, if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateIn(range: IntRangeWithConditions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates if the current `UInt` value is within the specified range.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range The range of valid values for the `UInt`.
 * @param causeOf An optional transformer that converts the invalid value into a specific throwable.
 *                If null, the default behavior is applied.
 * @param cause An optional transformer that generates the root cause throwable based on the invalid value.
 *              If null, no additional cause is provided.
 * @return The original `UInt` value if it falls within the specified range.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRange, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current unsigned integer value is within the specified range.
 * If not, an exception is thrown based on the provided parameters.
 *
 * @param range The range within which the unsigned integer value is expected to fall.
 * @param causeOf An optional transformer to generate a specific exception if the value is out of range. Null by default.
 * @param cause An optional transformer to generate the root cause exception. Null by default.
 * @param lazyMessage A transformer to construct the error message when the value is out of the specified range.
 * @return The validated `UInt` value if it falls within the specified range.
 * @throws NumberOutOfRangeException If the value is outside the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRange, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null, lazyMessage: Transformer<UInt, Any>): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the current unsigned integer value is within the specified range.
 * If the value is outside the range, an exception is thrown.
 *
 * @param range The inclusive range of valid unsigned integer values.
 * @param property An optional property reference that provides additional context for exception handling.
 *                 Its type, name, and owner class will be included in the exception message if provided.
 * @param variableName The name of the variable being validated, or null if the variable is unnamed.
 *                     If provided, it will be included in the exception message.
 * @param message An optional error message to override the default one when the value is out of range.
 *                If null, a default error message is used.
 * @param causeOf A transformer function that generates a `Throwable` instance providing a custom root cause
 *                for the exception when the value is invalid. If null, no custom root cause is generated.
 * @param cause A transformer function that generates a `Throwable` instance to append additional context
 *              to the thrown exception. If null, no additional context is included.
 * @return The validated unsigned integer value if it is within the specified range.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRange, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current unsigned integer value falls within the specified range.
 * If the value is outside the range, an exception is thrown.
 *
 * @param range The `UIntRange` against which the value will be validated.
 * @param property The primary property whose metadata may be used in the exception message if validation fails.
 * @param variable An optional variable whose metadata may be included in the exception message if validation fails.
 * @param message An optional custom error message to include in the exception if validation fails. Defaults to null.
 * @param causeOf An optional transformer that generates a `Throwable` instance indicating the cause of the exception based on the current value. Defaults to null.
 * @param cause An optional transformer that generates a `Throwable` instance to provide additional context if validation fails. Defaults to null.
 * @return The current `UInt` value if it passes validation.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRange, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `UInt` is within the specified range. If the value is not within the range,
 * a `NumberOutOfRangeException` is thrown.
 *
 * @param range The range to validate the current `UInt` against.
 * @param callable The `KFunction` instance of the callable function where the validation is performed.
 *                 This is used to enhance the error message and tracking, and can be null.
 * @param parameterName The name of the parameter being validated, used in exception messages for clarity. Optional, can be null.
 * @param message An optional custom message that provides additional details in the exception. If null, a default message is used.
 * @param causeOf A transformer that generates the root cause exception based on the value being validated,
 *                if the value is outside the range. Optional, can be null.
 * @param cause A transformer that generates the cause exception based on the value being validated.
 *              This is used to wrap the resulting `NumberOutOfRangeException`. Optional, can be null.
 * @return The current `UInt` if it falls within the specified range.
 * @throws NumberOutOfRangeException if the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRange, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the receiver `UInt` lies within the specified range. If the value is out of the range,
 * an exception is thrown based on the provided parameters.
 *
 * @param range The `UIntRange` within which the value should reside. If the receiver is not in this range,
 *              an exception is thrown.
 * @param callable The `KFunction` instance representing the function to which the validation is associated,
 *                 used for constructing detailed exception messages. Can be nullable.
 * @param parameter The `KParameter` instance representing the specific parameter being validated,
 *                  used for constructing detailed exception messages. Can be nullable.
 * @param message An optional custom message to include with the exception, providing additional context.
 *                Defaults to a standard message indicating that the value is out of range.
 * @param causeOf An optional transformation function that maps the receiver to a `Throwable` to be used as
 *                the primary cause for the exception.
 * @param cause An optional transformation function that maps the receiver to a `Throwable` representing an
 *              underlying cause of the exception.
 * @return The receiver `UInt` if it lies within the specified range.
 * @throws NumberOutOfRangeException if the receiver `UInt` is not within the specified range, using the
 *                                   provided parameters to construct the exception details.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRange, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current unsigned integer is within the specified range. If the value is not
 * within the range, it throws a `NumberOutOfRangeException`. Optional parameters allow for
 * customization of the exception message and cause.
 *
 * @param range The range within which the value must fall.
 * @param callableName The name of the callable or function where validation occurs. Can be null.
 * @param parameterName The name of the parameter being validated. Optional, defaults to null.
 * @param message A custom error message to include in the exception if validation fails. Optional.
 * @param causeOf A transformer function to generate the primary cause exception. Optional.
 * @param cause A transformer function to generate a nested cause exception. Optional.
 * @return The unsigned integer if it passes validation.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRange, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `UInt` value falls within the specified range.
 * If the value is outside the range, a `NumberOutOfRangeException` is thrown.
 *
 * @param range The `UIntRange` within which the value should fall.
 * @param callableName The name of the callable function where validation occurs, or null if not applicable.
 * @param parameter The parameter of the callable function associated with this validation, or null if not applicable.
 * @param message An optional custom error message to include in the exception. Defaults to null.
 * @param causeOf An optional transformer to provide a cause for the exception if the value is out of range. Defaults to null.
 * @param cause An optional transformer to provide an additional cause for the exception. Defaults to null.
 * @return The validated `UInt` value if it is within the specified range.
 * @throws NumberOutOfRangeException if the `UInt` value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRange, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current `UInt` value lies within the specified range, taking exclusions into account.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range The range of acceptable values, including any explicit exclusions.
 * @param causeOf An optional transformer for generating a specific `Throwable` based on the value.
 * @param cause An optional transformer for generating a nested `Throwable` as the cause of an exception.
 * @return The current `UInt` value if it is within the range.
 * @throws NumberOutOfRangeException If the value is outside the specified range or in the exclusions.
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithExclusions, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current unsigned integer is within the specified range,
 * optionally excluding certain values, and throws an exception if the validation fails.
 *
 * @param range The range of allowed unsigned integer values, which may include exclusions.
 * @param causeOf An optional transformer to generate a specific throwable based on the value, or null to use a default.
 * @param cause An optional transformer to generate the root cause of the exception, or null if no root cause is provided.
 * @param lazyMessage A transformer to produce the exception message based on the value being validated.
 * @return Returns the current unsigned integer if the validation passes.
 * @throws NumberOutOfRangeException If the unsigned integer is outside the allowed range or excluded.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithExclusions, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null, lazyMessage: Transformer<UInt, Any>): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `UInt` value is within the specified range, throwing an exception if it is not.
 *
 * @param range The range of valid `UInt` values, which may include exclusions.
 * @param property An optional property reference for providing additional context in exception messages.
 * @param variableName An optional name of the variable for enhanced exception message clarity.
 * @param message An optional custom message that overrides the default exception message.
 * @param causeOf An optional transformer used to generate a throwable cause for the exception.
 * @param cause An optional transformer used to generate a cause for the exception when `causeOf` is not provided.
 * @return The original value if it passes validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithExclusions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current unsigned integer value is within the specified range.
 * If the value is outside the range, it throws a [NumberOutOfRangeException].
 *
 * @param range The inclusive range with optional exclusions against which the value will be validated.
 * @param property The property that holds the value being validated. Can be null.
 * @param variable The variable that holds the value being validated. Can be null.
 * @param message A custom error message to include in the exception if validation fails. Can be null.
 * @param causeOf A transformer to generate a specific exception using the current value if validation fails. Can be null.
 * @param cause A transformer to generate the cause for the exception using the current value. Can be null.
 * @return The current unsigned integer value if it satisfies the validation criteria.
 * @throws NumberOutOfRangeException If the current value is not within the specified range or violates the exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithExclusions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current unsigned integer value is within the specified range,
 * accounting for any exclusions specified in the range. If the value is not valid,
 * an exception is thrown with an optional cause and additional context.
 *
 * @param range The range of valid values with possible exclusions.
 * @param callable The Kotlin function that is the context of this validation, used for error reporting.
 * @param parameterName The name of the parameter being validated, used for error reporting.
 * @param message An optional custom error message to override the default message if validation fails.
 * @param causeOf A transformer function to generate a custom exception based on the value when outside the range.
 * @param cause A transformer function to provide the underlying cause of validation failure.
 * @return The validated value if it is within the specified range.
 * @throws NumberOutOfRangeException If the value is not within the range and exclusions are applied or a custom exception is thrown through the `causeOf` transformer.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithExclusions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current unsigned integer is within the specified range, potentially
 * throwing an exception if it falls outside the range.
 *
 * @param range The range to validate against, which may include exclusions.
 * @param callable The function reference for contextualizing the exception, if thrown.
 * @param parameter The parameter reference for contextualizing the exception, if thrown.
 * @param message An optional custom message to be included in the exception, if thrown.
 * @param causeOf An optional transformer to provide a specific cause for the exception, taking the unsigned integer as input.
 * @param cause An optional transformer to generate a secondary cause for the exception, taking the unsigned integer as input.
 * @return The original unsigned integer if it passes validation.
 * @throws NumberOutOfRangeException if the unsigned integer is not within the specified range or exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithExclusions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current unsigned integer (`UInt`) is within the specified range, excluding
 * any values as defined in the `range`. If the value is not within the range, an exception is thrown.
 *
 * @param range The range of valid values, which may include exclusions.
 * @param callableName An optional name of the callable (function or method) performing the validation, used in the exception message.
 * @param parameterName An optional name of the parameter being validated, used in the exception message.
 * @param message An optional custom error message to include in the exception when the validation fails.
 * @param causeOf An optional transformer function to create a custom exception on validation failure. This has priority over `cause` if not null.
 * @param cause An optional transformer function to create an exception cause when the validation fails. This is used if `causeOf` is null.
 * @return The current `UInt` value if it passes the validation successfully.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithExclusions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `UInt` instance is within the specified `UIntRangeWithExclusions`.
 * Throws an exception if the value is not within the range.
 *
 * @param range The range of valid values, including optional exclusions.
 * @param callableName An optional name of the callable associated with the validation.
 * @param parameter An optional parameter reference for context in the validation.
 * @param message An optional custom message to include in the exception if the value is not in the range.
 * @param causeOf An optional transformer to generate a custom throwable as the primary cause of the failure.
 * @param cause An optional transformer to generate a secondary throwable to include in the failure.
 * @return The validated `UInt` value if it successfully passes the range check.
 * @throws NumberOutOfRangeException If the value is not within the specified `range`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithExclusions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current unsigned integer value falls within the specified range.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range The range of valid unsigned integer values to check against.
 * @param causeOf An optional transformer used to generate a specific exception based on the value.
 * @param cause An optional transformer that can provide additional context for the exception.
 * @return The unsigned integer value if it is within the specified range.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithConditions, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current unsigned integer value is within the specified range.
 * Throws an exception if the value is out of range.
 *
 * @param range The range of allowed values. This range is defined with additional conditions.
 * @param causeOf An optional transformer to generate a throwable cause contextually based on the value.
 * @param cause An optional transformer to provide a custom throwable cause based on the value.
 * @param lazyMessage A transformer to generate a lazily evaluated error message when the value is out of range.
 * @return Returns the current unsigned integer value if it is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithConditions, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null, lazyMessage: Transformer<UInt, Any>): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current unsigned integer (`UInt`) is within the specified range.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range The range of values within which the `UInt` must lie.
 * @param property Optional property information used for detailed exception messages.
 * @param variableName An optional name of the variable being validated, used for better error messages.
 * @param message An optional custom message to include in the exception if the value is out of range.
 * @param causeOf An optional transformer to generate a custom exception to throw when validation fails.
 * @param cause An optional transformer to specify the cause of the exception when validation fails.
 * @return The current `UInt` if it passes the range validation.
 * @throws NumberOutOfRangeException If the `UInt` is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithConditions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current unsigned integer value falls within the specified range.
 * If the value is not within the range, a custom exception will be thrown based on the provided conditions.
 *
 * @param range The range of valid unsigned integer values to check against.
 * @param property The property representing this value, if applicable, used for exception context.
 * @param variable The secondary property, if applicable, used for additional exception context.
 * @param message The custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer to create a specific throwable cause when the validation fails.
 * @param cause An optional transformer to define a nested throwable cause for the thrown exception.
 * @return The current unsigned integer if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithConditions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current unsigned integer value falls within a specified range and throws an exception if it does not.
 *
 * @param range The range with conditions against which the current unsigned integer will be validated.
 * @param callable The Kotlin function that initiated the validation. This is used for error handling and reporting.
 * @param parameterName The name of the parameter being validated. This is optional and used for more descriptive error messages.
 * @param message The custom error message to be included in the exception if validation fails. Defaults to a standard message.
 * @param causeOf A transformer function that takes the invalid value and produces the throwable cause. If `null`, default behavior applies.
 * @param cause A transformer function that takes the invalid value and produces a secondary throwable cause. Optional and defaults to `null`.
 * @return The validated unsigned integer value, if it falls within the specified range.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithConditions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `UInt` value falls within the specified range. If the value is not within the range,
 * an exception is thrown based on the provided error-handling mechanisms.
 *
 * @param range The `UIntRangeWithConditions` object defining the range and conditions to validate against.
 * @param callable An optional `KFunction` representing the function requesting validation.
 * @param parameter An optional `KParameter` instance providing metadata about the parameter being validated.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf An optional transformer for creating a specific exception based on this `UInt` value.
 * @param cause An optional transformer for generating the cause of the range violation exception.
 * @return The validated `UInt` value if it passes the range validation.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithConditions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `UInt` value is within the specified range.
 * Throws an exception if the value is outside the range.
 *
 * @param range The range with conditions to validate against.
 * @param callableName An optional name associated with the function or callable invoking the validation.
 * @param parameterName The optional name of the parameter being validated.
 * @param message A custom optional message to include in the exception if validation fails.
 * @param causeOf An optional transformer for generating the primary cause of the exception based on the value.
 * @param cause An optional transformer for generating a secondary cause to attach to the exception.
 * @return Returns the validated `UInt` if it is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithConditions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `UInt` value is within the specified range, and throws an exception if it is not.
 *
 * @param range The range with additional conditions that this value should lie within.
 * @param callableName Optional name of the callable context for descriptive error messages.
 * @param parameter Optional parameter instance tied to this validation for error context.
 * @param message Optional custom error message to include if the value is outside the range.
 * @param causeOf Optional transformer function to generate a throwable cause based on the current value.
 * @param cause Optional transformer function to generate a cause throwable if validation fails.
 * @return The current `UInt` value if it passes the range validation.
 * @throws NumberOutOfRangeException If this value is not within the specified range and conditions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateIn(range: UIntRangeWithConditions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates if the current `Long` value is within the specified range.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range The range of valid `Long` values.
 * @param causeOf A transformer used to generate a specific exception for out-of-range values.
 *                If null, a default `NumberOutOfRangeException` is used instead.
 * @param cause A transformer used to generate the root cause for the exception, if needed.
 * @return The current `Long` value if it is within the specified range.
 * @throws NumberOutOfRangeException If the value is not within the given range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRange, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `Long` value lies within the specified range.
 * If not, it throws a `NumberOutOfRangeException` with a message generated using the provided `lazyMessage`.
 * Additionally, a custom throwable can be constructed using the optional `causeOf` and `cause` transformers.
 *
 * @param range the range to check the current `Long` value against.
 * @param causeOf an optional transformer that generates a custom throwable based on the value if it is out of range.
 * @param cause an optional transformer that generates a root cause throwable for the exception if the value is out of range.
 * @param lazyMessage a transformer generating an explanatory message based on the value if it is out of range.
 * @return the current `Long` value if it lies within the specified range.
 * @throws NumberOutOfRangeException if the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRange, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null, lazyMessage: Transformer<Long, Any>): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `Long` value is within the specified range.
 * If the value is not within the range, a `NumberOutOfRangeException` is thrown.
 *
 * @param range The inclusive range (`LongRange`) against which the value is validated.
 * @param property An optional `KProperty` reference for contextual information in the exception message.
 * @param variableName The optional name of the variable for clearer exception messages.
 * @param message An optional custom error message to include when the validation fails.
 * @param causeOf A transformer function to generate the root cause of the exception.
 * @param cause An optional transformer to generate the inner cause of the exception.
 * @return The validated `Long` value if it lies within the given range.
 * @throws NumberOutOfRangeException If the value is outside the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRange, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `Long` value is within the given range.
 * If the value is not in the specified range, an exception is thrown.
 *
 * @param range The range within which the value should lie.
 * @param property The primary property associated with the validation, which may provide metadata for error reporting.
 * @param variable An optional variable that may provide additional metadata for error reporting.
 * @param message An optional message to include if the validation fails.
 * @param causeOf An optional transformer to generate a specific `Throwable` if the validation fails.
 * @param cause An optional transformer to generate the underlying cause of the exception if validation fails.
 * @return The original `Long` value if it passes the validation.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRange, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `Long` value is within the specified range, throwing an exception if it is not.
 *
 * @param range The range within which the current `Long` value must fall.
 * @param callable The `KFunction` reference of the method or function in which the validation occurs. Used for exception context. Can be null.
 * @param parameterName The name of the parameter being validated. Used for exception context. Optional, can be null.
 * @param message An optional custom error message to be used if the validation fails. Defaults to a message indicating the value is out of range.
 * @param causeOf A transformer function to generate the underlying cause of the exception from the current value. Optional, can be null.
 * @param cause A transformer function to generate a cause from the current value. Optional, can be null.
 * @return The current `Long` value if it passes the validation.
 * @throws NumberOutOfRangeException If the current `Long` value is not in the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRange, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current [Long] value falls within the specified [range]. If the value is not in the range,
 * an exception is thrown.
 *
 * @param range The [LongRange] defining the valid range for the value.
 * @param callable The [KFunction] instance representing the function where the validation is being performed. Can be `null`.
 * @param parameter The [KParameter] instance representing the specific parameter being validated. Can be `null`.
 * @param message An optional custom message to include in the exception if the value is out of range. Defaults to `null`.
 * @param causeOf A transformer function that generates a [Throwable] when the value is invalid.
 *                If not provided, the default exception will be used. Defaults to `null`.
 * @param cause A transformer function that generates the underlying cause of the exception when the value is invalid.
 *              Defaults to `null`.
 * @return The current [Long] value if it is within the specified [range].
 * @throws NumberOutOfRangeException If the value is not within the specified [range].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRange, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `Long` value lies within the specified range. If the value is
 * outside the range, a `NumberOutOfRangeException` is thrown. This method is useful for validating
 * parameters within a given boundary and supports custom error messages and causes.
 *
 * @param range The `LongRange` within which the value must reside.
 * @param callableName The name of the callable or function where the validation is performed,
 *                     or null if unavailable.
 * @param parameterName Optional name of the parameter being validated, or null if unavailable.
 * @param message An optional custom message to use in the exception if validation fails.
 * @param causeOf A transformer used to generate a throwable as the root cause of the exception,
 *                or null if not used.
 * @param cause A transformer used to generate an additional cause of the exception based on
 *              the invalid value, or null if not used.
 * @return The validated `Long` value if it falls within the specified range.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRange, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `Long` value is within the specified range. If the value is not within
 * the range, a `NumberOutOfRangeException` is thrown. An optional custom message and cause can
 * be provided to enhance the exception details.
 *
 * @param range The range of valid values within which the `Long` value must fall.
 * @param callableName The name of the callable function in which this validation occurs. Can be null.
 * @param parameter The parameter of the callable function being validated. Can be null.
 * @param message An optional message to include in the exception if the value is out of range. Can be null.
 * @param causeOf A transformer that maps the current `Long` value into a `Throwable` object to
 *                be used as the cause of the primary exception. Can be null.
 * @param cause An optional transformer that maps the current `Long` value to a `Throwable`,
 *              which provides a secondary cause for the exception. Can be null.
 * @return The current `Long` value if it passes validation.
 * @throws NumberOutOfRangeException if the `Long` value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRange, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates whether the current `Long` value is within a specified range, considering possible exclusions,
 * and throws an exception if the value is out of range.
 *
 * @param range The range of valid values, including any specified exclusions.
 * @param causeOf An optional transformer to produce a custom exception based on the input value if it is out of range.
 * @param cause An optional transformer to produce the cause of the exception based on the input value.
 * @return The original `Long` value if it is within the given range.
 * @throws NumberOutOfRangeException If the value is outside the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithExclusions, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `Long` value falls within the specified range, taking into account any exclusions
 * defined in the `LongRangeWithExclusions`. If the value is outside the range, an exception is thrown.
 *
 * @param range the range within which the `Long` value should be validated, including specified exclusions.
 * @param causeOf an optional transformer to generate a throwable based on the current value, used as the
 *                primary exception cause.
 * @param cause an optional transformer to generate a throwable based on the current value, used as a
 *              secondary exception cause.
 * @param lazyMessage a transformer that generates a lazy message for the exception when the validation fails.
 * @return the current `Long` value if it satisfies the validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithExclusions, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null, lazyMessage: Transformer<Long, Any>): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current [Long] value is within the specified [range], considering any exclusions in the provided range.
 *
 * @param range The range of valid values, which may include exclusions to refine validation.
 * @param property The optional property reference associated with the value being validated, used for error descriptions.
 * @param variableName An optional variable name to include in error messages for better context.
 * @param message An optional custom error message to override the default validation error text.
 * @param causeOf An optional transformer that produces a throwable when the validation fails, allowing for customized exception handling.
 * @param cause An optional transformer that adds additional context or causes to the produced throwable upon validation failure.
 * @return The current [Long] value if it successfully validates within the provided range.
 * @throws NumberOutOfRangeException If the current [Long] is not within the specified range or its exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithExclusions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current [Long] value is within the specified [range], excluding any values
 * defined as excluded in the range. If the value is not within the range, an exception is thrown.
 *
 * @param range The [LongRangeWithExclusions] object defining the acceptable range and any excluded values.
 * @param property The primary [KProperty] representing the value being validated, used for error reporting. Can be null.
 * @param variable An optional secondary [KProperty] associated with the value being validated. Can be null.
 * @param message An optional custom error message to override the default error message. Can be null.
 * @param causeOf A transformer function that generates a [Throwable] to throw when validation fails instead of using the default behavior. Can be null.
 * @param cause An optional transformer function to produce the root cause [Throwable] used when the exception is thrown. Can be null.
 * @return The validated [Long] value if it is within the specified range and not in the excluded values.
 * @throws NumberOutOfRangeException If the value is not in the specified [range] and no [causeOf] transformer is provided.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithExclusions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `Long` is within the specified range, excluding any explicit exclusions defined in the range.
 * If the value is not within the range, a `NumberOutOfRangeException` is thrown, optionally with a custom message or cause.
 *
 * @param range the range to validate against, including exclusions.
 * @param callable an optional `KFunction` associated with this validation, used in error reporting.
 * @param parameterName an optional name of the parameter being validated, used in error reporting.
 * @param message an optional custom message for the exception if the validation fails.
 * @param causeOf an optional transformation function that converts the value into a `Throwable` reason for the exception.
 * @param cause an optional transformation function that provides a cause for the exception if validation fails.
 * @return the same `Long` value if it passes the validation.
 * @throws NumberOutOfRangeException if the value is not within the specified range and exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithExclusions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `Long` value is within the specified range, considering exclusions,
 * and throws an exception if the value is not valid.
 *
 * @param range The range of valid values, with exclusions.
 * @param callable The callable function to associate with the validation context, if applicable.
 * @param parameter The parameter being validated, if applicable.
 * @param message An optional custom message to include in the exception.
 * @param causeOf An optional transformer used to generate a custom throwable based on the current value when validation fails.
 * @param cause An optional transformer used to create a root cause of the exception based on the current value.
 * @return The current `Long` value if validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithExclusions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the `Long` value is within the specified range and throws an exception if it is not.
 *
 * @param range The range of valid values, including any defined exclusions.
 * @param callableName The name of the callable in which validation is performed, used in exception messages.
 * @param parameterName An optional name of the parameter being validated, used in exception messages.
 * @param message An optional custom message for the exception when validation fails.
 * @param causeOf An optional transformer to create a specific throwable cause for validation failures.
 * @param cause An optional transformer to generate the cause of the exception.
 * @return The `Long` value itself if it is valid (within the range and not excluded).
 * @throws NumberOutOfRangeException If the value is not within the specified range or is excluded.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithExclusions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `Long` value is within a specified range, optionally excluding certain values.
 * If the value does not fall within the range, an exception is thrown.
 *
 * @param range The range of `Long` values to validate against, which may also include exclusions.
 * @param callableName The name of the callable or function being validated, used in exception messaging.
 * @param parameter The parameter metadata of the callable being validated, used in exception messaging.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer that provides a custom throwable to be thrown when validation fails,
 *                initialized with the current `Long` value.
 * @param cause An optional transformer that creates the cause of the exception to be thrown when validation fails,
 *              initialized with the current `Long` value.
 * @return Returns the original `Long` value if validation passes.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithExclusions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates whether the current `Long` value falls within the specified range defined by `range`.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range The range within which the value should fall. Encapsulates conditions for validation.
 * @param causeOf An optional transformer used to construct a custom throwable based on the current value.
 * @param cause An optional transformer used to create a cause for the exception thrown when validation fails.
 * @return The current `Long` value, if it satisfies the specified range conditions.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithConditions, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `Long` value falls within the specified range.
 * If the value is outside the range, it throws an exception with an optional cause or lazy message.
 *
 * @param range The `LongRangeWithConditions` in which the value is expected to lie.
 * @param causeOf An optional transformer to generate a specific exception or modify the original exception
 *                when validation fails. Defaults to null.
 * @param cause An optional transformer to provide the underlying cause of the exception. Defaults to null.
 * @param lazyMessage A transformer function to generate a message when the value is out of range.
 * @return The validated `Long` value if it lies within the specified range.
 * @throws NumberOutOfRangeException If the value is outside the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithConditions, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null, lazyMessage: Transformer<Long, Any>): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the invoking [Long] value is within the specified [range].
 * If the value is outside the range, an appropriate exception will be thrown.
 *
 * @param range the [LongRangeWithConditions] defining the valid range for the value.
 * @param property the [KProperty] associated with the value being validated, or null if not applicable.
 * @param variableName an optional variable name used in the exception message, if provided.
 * @param message an optional custom message to include in the exception if validation fails.
 * @param causeOf an optional transformer to create a custom exception based on the invalid value,
 *                or null if the default exception is to be used.
 * @param cause an optional transformer to generate a cause for the exception using the invalid value,
 *              or null if no cause should be included.
 * @return the value itself if it passes the validation.
 * @throws NumberOutOfRangeException if the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithConditions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `Long` value is within the specified range.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range The range of valid values as a `LongRangeWithConditions`.
 * @param property The property being validated, if applicable.
 * @param variable An optional related property, if any, for additional context.
 * @param message An optional custom message for the exception.
 * @param causeOf An optional transformer to provide the specific exception to be thrown when validation fails.
 * @param cause An optional transformer to generate the cause for the thrown exception.
 * @return The current `Long` value if it is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithConditions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `Long` value is within the specified `range`.
 * If the value is not within the range, a custom exception can be thrown
 * optionally using the provided transformers or the default behavior.
 *
 * @param range A `LongRangeWithConditions` object specifying the valid range of the value.
 * @param callable Optional Kotlin function reference that helps identify the source of the validation.
 * @param parameterName Optional name of the parameter being validated.
 * @param message Optional custom error message to include in the exception.
 * @param causeOf Optional transformer to generate a custom exception as the cause when the value is out of range.
 * @param cause Optional transformer to generate a custom exception to attach as the direct cause.
 * @return The current `Long` value if it is within the specified `range`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithConditions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current Long value is within the specified range and throws an exception if it is not.
 *
 * @param range The range to validate the value against. It must be of type LongRangeWithConditions.
 * @param callable The KFunction instance associated with the context where the validation is occurring, or null.
 * @param parameter The KParameter instance associated with the specific parameter being validated, or null.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf An optional transformer to generate a Throwable cause for the exception if the value is invalid.
 * @param cause An optional transformer to generate a secondary Throwable cause for the exception if validation fails.
 * @return The current Long value if it is within the specified range.
 * @throws NumberOutOfRangeException If the current value is outside the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithConditions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the `Long` value is within the specified range. If the value
 * is outside the range, an exception is thrown.
 *
 * @param range The range within which the `Long` value should lie. It may include additional conditions.
 * @param callableName The name of the callable function, used for contextual information in error messages.
 * @param parameterName An optional name of the parameter being validated, used for contextual information in error messages.
 * @param message An optional custom error message that will be included in the exception if the value is not in the range.
 * @param causeOf An optional transformer that generates a throwable to represent the cause of the exception.
 * @param cause An optional transformer that generates a throwable as an underlying cause for the exception.
 * @return The validated `Long` value if it is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithConditions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the invoking `Long` value falls within the specified range and satisfies the included conditions.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range The range of acceptable values, including any additional conditions that must be satisfied.
 * @param callableName The name of the callable (function or property) being validated, if applicable.
 * @param parameter The KParameter instance representing the parameter being validated, if applicable.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer that generates a throwable to be thrown in case of validation failure.
 * @param cause An optional transformer that generates the cause of the validation exception.
 * @return The original `Long` value if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateIn(range: LongRangeWithConditions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates whether the calling [ULong] value is within the specified [range].
 * If the value is not within the [range], an exception is thrown.
 * The exception thrown can be customized through optional transformers [causeOf] and [cause].
 *
 * @param range The range of valid values. The calling [ULong] must be within this range.
 * @param causeOf An optional transformer that generates a custom [Throwable] to throw when the value is invalid.
 *                If not provided, a default exception message is used.
 * @param cause An optional transformer that generates a cause for the exception when the value is invalid.
 *              This is used as the inner cause of the thrown exception.
 * @return The validated [ULong] value if it is within the specified [range].
 * @throws NumberOutOfRangeException If the calling value is not within the [range] and no custom exception is provided by [causeOf].
 * @throws Throwable If a custom exception is provided by [causeOf] or a custom cause is generated by [cause].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRange, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `ULong` value falls within the specified range. If the value is
 * outside the range, an exception is thrown using the provided error details.
 *
 * @param range The inclusive range of acceptable `ULong` values.
 * @param causeOf An optional transformer that generates a custom exception from the current value.
 * @param cause An optional transformer that generates an underlying cause exception from the current value.
 * @param lazyMessage A transformer that generates a message to describe the validation failure.
 * @return The current `ULong` value if it is within the specified range.
 * @throws NumberOutOfRangeException if the current value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRange, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null, lazyMessage: Transformer<ULong, Any>): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the `ULong` is within the specified range. If it is not, a `NumberOutOfRangeException`
 * is thrown. Provides optional parameters for customizing the validation exception.
 *
 * @param range The range of valid values.
 * @param property The property associated with this validation, used for generating error details. Can be null.
 * @param variableName The name of the variable being validated, included in the error message if not null.
 * @param message An optional message that provides additional context for the validation failure.
 * @param causeOf A transformer function that generates a cause `Throwable` based on the invalid value, or null.
 * @param cause A transformer function that generates a `Throwable` based on the invalid value, or null.
 * @return The `ULong` if it passes the validation.
 * @throws NumberOutOfRangeException If the `ULong` is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRange, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current [ULong] value falls within the specified [range]. If the value is not within the range,
 * a [NumberOutOfRangeException] will be thrown with the appropriate error details.
 *
 * @param range the [ULongRange] within which the value must fall
 * @param property the main [KProperty] providing context for the validation, can be null
 * @param variable an optional secondary [KProperty] providing additional context, can be null
 * @param message an optional message providing custom details about the validation failure, default is null
 * @param causeOf a transformer to generate a cause exception if validation fails, default is null
 * @param cause a transformer to generate an additional cause exception, default is null
 * @return the validated [ULong] value, if it passes the validation checks
 * @throws NumberOutOfRangeException if the value does not fall within the specified [range]
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRange, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `ULong` value is within the specified range. If the value is not
 * within the range, an exception is thrown using the provided parameters for messaging and error handling.
 *
 * @param range The `ULongRange` defining the valid range for the value.
 * @param callable The Kotlin function (`KFunction`) from where the validation is performed. Can be null.
 * @param parameterName The name of the parameter being validated. Optional and can be null.
 * @param message An optional custom message to include in the exception if the validation fails. Default is null.
 * @param causeOf A transformer function that provides a custom cause of the exception based on the value being validated. Optional and can be null.
 * @param cause A transformer function that provides a throwable cause of the exception based on the value being validated. Optional and can be null.
 * @return The validated `ULong` value if it is within the specified range.
 * @throws NumberOutOfRangeException if the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRange, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current [ULong] value is within the specified [range]. If the value is not within the [range],
 * an exception will be thrown, optionally customized using the provided [callable], [parameter], [message],
 * and [cause] or [causeOf] transformers.
 *
 * @param range The inclusive range within which the [ULong] value must fit.
 * @param callable The [KFunction] associated with the validation context, or null if not applicable.
 * @param parameter The [KParameter] representing the parameter to which the value belongs, or null if not applicable.
 * @param message An optional custom message to include in the validation failure, defaulting to null.
 * @param causeOf An optional transformer to produce a [Throwable] cause for validation failure from the current [ULong] value, defaulting to null.
 * @param cause An optional transformer to generate a [Throwable] based on the current [ULong] value if the validation fails, defaulting to null.
 * @return The current [ULong] value if it is within the specified [range].
 * @throws NumberOutOfRangeException If the value is not within the specified [range].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRange, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `ULong` instance lies within the specified `range`.
 * Throws a `NumberOutOfRangeException` if the value is not within the range.
 *
 * @param range The range of acceptable `ULong` values.
 * @param callableName The name of the callable (e.g., function or method) performing the validation.
 * @param parameterName An optional name of the parameter being validated.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf A transformer for generating a specific cause of the validation exception based on the current value.
 * @param cause An optional transformer that produces the underlying cause of the exception based on the current value.
 *
 * @return The validated `ULong` value if it is within the specified range.
 * @throws NumberOutOfRangeException If the value is not within the `range`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRange, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `ULong` value falls within the specified range.
 * If the value is not within the range, a `NumberOutOfRangeException` is thrown.
 *
 * @param range The range of `ULong` values that the current value is expected to fall within.
 * @param callableName The name of the callable (e.g., function or property) associated with this validation, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf An optional transformer to generate the cause of the exception based on the current `ULong` value, or null if not specified.
 * @param cause An optional transformer to generate a supplementary cause of the exception based on the current `ULong` value, or null if not specified.
 * @return The current `ULong` value if it passes the validation check.
 * @throws NumberOutOfRangeException If the current `ULong` is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRange, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates if the current unsigned long value is within the specified range, considering possible exclusions.
 * If the value is not within the range, an exception is thrown.
 *
 * @param range The range of unsigned long values, including any exclusions, to validate against.
 * @param causeOf An optional transformer function that generates a specific cause of the validation failure based on the value.
 * @param cause An optional transformer function that provides a more detailed cause for the exception use case.
 * @return The current value if it is within the specified range.
 * @throws NumberOutOfRangeException if the value is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithExclusions, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `ULong` value lies within the specified range, excluding any values explicitly omitted.
 * If the value is outside the valid range, a validation exception is thrown.
 *
 * @param range The range of valid `ULong` values with potential exclusions.
 * @param causeOf An optional transformer that generates the cause of the exception based on the `ULong` value.
 * @param cause An optional transformer that generates an additional cause of the exception based on the `ULong` value.
 * @param lazyMessage A transformer that generates a custom validation failure message based on the `ULong` value.
 * @return The validated `ULong` value if it falls within the specified range.
 * @throws NumberOutOfRangeException if the `ULong` value is outside the specified range or one of the excluded values.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithExclusions, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null, lazyMessage: Transformer<ULong, Any>): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value lies within the specified range,
 * considering exclusions, and throws a `NumberOutOfRangeException` if the
 * value is outside the range or meets any defined exclusion criteria.
 *
 * @param range The range, including any exclusions, within which the value should be validated.
 * @param property An optional property reference associated with the value being validated.
 * @param variableName An optional name of the variable being validated.
 * @param message An optional custom message to be included in the exception if validation fails.
 * @param causeOf An optional transformer that generates a specific cause exception based on the current value.
 * @param cause An optional transformer to generate the general cause of the validation failure.
 * @return The original `ULong` value if validation succeeds.
 * @throws NumberOutOfRangeException If the value is outside the specified range or meets exclusion criteria.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithExclusions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `ULong` value is within the specified range, considering any exclusions.
 * If the value is not within the range, a `NumberOutOfRangeException` is thrown.
 *
 * @param range The `ULongRangeWithExclusions` object that defines the valid range and exclusions.
 * @param property The `KProperty` representing the property associated with the value being validated (nullable).
 * @param variable The `KProperty` representing the variable associated with the value being validated (nullable).
 * @param message An optional custom message describing the validation failure.
 * @param causeOf An optional transformer that supplies a `Throwable` cause based on the validation failure.
 * @param cause An optional transformer that provides an additional `Throwable` cause for the exception.
 * @return The `ULong` value if validation is successful.
 * @throws NumberOutOfRangeException If the value is not within the specified range or exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithExclusions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the unsigned long value is within the specified range, excluding any explicitly defined exclusions
 * in the `range`. If the value is out of range, an exception is thrown.
 *
 * @param range The range of acceptable values, including specific exclusions.
 * @param callable The function or method in which the validation is being performed. This is used for diagnostic
 *        purposes in the exception message. Can be null.
 * @param parameterName The name of the parameter being validated. Used for exception messages. Can be null.
 * @param message A custom message to include in the exception if validation fails. If null, a default message
 *        will be used.
 * @param causeOf A transformer that generates a throwable cause for the exception if validation fails. Can be null.
 * @param cause A secondary transformer that generates another throwable cause for the exception. This can
 *        provide additional details upon validation failure. Can be null.
 * @return The unsigned long value itself if it is within the acceptable range.
 * @throws NumberOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithExclusions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the `ULong` value is within the specified range, considering exclusions.
 * Throws a `NumberOutOfRangeException` if the value is not valid.
 *
 * @param range The range with exclusions within which the `ULong` value must fall.
 * @param callable The callable function where this validation occurs (nullable).
 * @param parameter The parameter associated with the validation (nullable).
 * @param message An optional custom error message to use in case of a validation failure.
 * @param causeOf A transformer used to generate the root cause of the exception (nullable).
 * @param cause A transformer used to generate the cause of the validation failure (nullable).
 * @return The validated `ULong` value if it is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithExclusions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `ULong` value is within the specified range, throwing an exception
 * if it is not. Allows customization of exception messages and causes.
 *
 * @param range The range within which the `ULong` value must reside. Can include exclusions.
 * @param callableName The name of the callable associated with the validation context. Optional.
 * @param parameterName The name of the parameter being validated. Optional.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf A transformer function to create a throwable based on the current value if validation fails. Optional.
 * @param cause A transformer function to create a throwable for the root cause of validation failure, leveraging the current value. Optional.
 * @return The validated `ULong` value if it resides within the specified range.
 * @throws NumberOutOfRangeException If the `ULong` value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithExclusions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `ULong` value falls within a specified range or is part of an exclusion in the range.
 * Throws an exception if the validation fails.
 *
 * @param range The range of valid values, potentially with exclusions, to validate against.
 * @param callableName The name of the function or method to associate with the validation context (optional).
 * @param parameter The parameter being validated, if applicable (optional).
 * @param message A custom error message to describe the validation failure (optional).
 * @param causeOf A transformer function that produces a `Throwable` as the root cause of validation failure (optional).
 * @param cause A transformer function that produces additional contextual causes of validation failure (optional).
 * @return The original `ULong` value if it passes validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithExclusions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates if the current unsigned long integer falls within the specified range. If the value
 * does not fall within the range, an exception is thrown based on the provided transformers.
 *
 * @param range The range of unsigned long integers to validate against.
 * @param causeOf An optional transformer that produces a throwable when the validation fails.
 * @param cause An optional transformer that provides additional context to the exception when the validation fails.
 * @return The current unsigned long integer if it passes the validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithConditions, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `ULong` is within the specified range.
 * If the value is outside the range, an exception is thrown with a detailed message and optional cause.
 *
 * @param range The range of acceptable `ULong` values, with possible additional conditions encapsulated in `ULongRangeWithConditions`.
 * @param causeOf An optional transformer that generates a throwable cause from the `ULong` value. If null, a generic exception is used.
 * @param cause An optional transformer that generates a throwable cause from the `ULong` value. This is used in combination with `causeOf`.
 * @param lazyMessage A transformer that creates a detailed error message from the `ULong` value when the validation fails.
 * @return The validated `ULong` if it falls within the specified range.
 * @throws NumberOutOfRangeException If the current `ULong` is not within the specified range. The exception includes the lazy-generated message and optional causes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithConditions, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null, lazyMessage: Transformer<ULong, Any>): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the unsigned long value is within the specified range. If the value is not within the range,
 * a validation exception is thrown.
 *
 * @param range the range with conditions to validate the unsigned long value against.
 * @param property the property being validated, optional.
 * @param variableName the name of the variable being validated, optional.
 * @param message the custom error message to use if validation fails, optional.
 * @param causeOf a transformer to create a specific throwable cause when validation fails, optional.
 * @param cause an additional transformer to create a throwable cause when validation fails, optional.
 * @return the validated unsigned long value if it is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithConditions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the calling `ULong` value is within the specified range. If the value is not
 * within the range, a validation exception is thrown.
 *
 * @param range The `ULongRangeWithConditions` that defines the inclusive range of valid values.
 * @param property The property associated with the validation, can be null.
 * @param variable The variable associated with the validation, can be null.
 * @param message An optional custom message for the validation failure exception, can be null.
 * @param causeOf A transformer that generates a custom `Throwable` for the validation failure, can be null.
 * @param cause A transformer that generates a default `Throwable` for the validation failure, can be null.
 * @return The validated `ULong` if it is within the specified range.
 * @throws NumberOutOfRangeException If the calling `ULong` is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithConditions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current [ULong] value is within the specified [range], and throws an exception if it is not.
 *
 * @param range The allowable range defined by [ULongRangeWithConditions].
 * @param callable An optional reference to the function in which this validation occurs.
 * @param parameterName An optional name of the parameter being validated.
 * @param message An optional error message to describe the validation failure.
 * @param causeOf An optional transformer used to generate the cause of the failure in the form of a [Throwable].
 * @param cause An optional transformer to provide additional context about the validation failure with a [Throwable].
 * @return The validated [ULong] value, if it falls within the specified [range].
 * @throws NumberOutOfRangeException If the [ULong] value is not within the [range].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithConditions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `ULong` is within the specified range, throwing a validation exception if not.
 *
 * @param range The range of valid `ULong` values with additional conditions.
 * @param callable The Kotlin function reference associated with the parameter being validated, if applicable.
 * @param parameter The Kotlin parameter reference being validated, if applicable.
 * @param message An optional custom validation failure message.
 * @param causeOf An optional transformation function to produce a specific throwable when validation fails.
 * @param cause An optional transformation function to generate the primary cause of the exception.
 * @return The original `ULong` value if validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithConditions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current `ULong` value is within the specified range. If the value
 * is outside the range, a `NumberOutOfRangeException` is thrown with the given context.
 *
 * @param range The range within which the value is expected to fall.
 * @param callableName The name of the callable where the validation is performed.
 * @param parameterName The name of the parameter being validated, defaulting to null.
 * @param message The custom exception message to use if validation fails, defaulting to null.
 * @param causeOf A function that generates a `Throwable` based on the value, which will
 *                serve as the cause of the exception, defaulting to null.
 * @param cause An additional function to generate a `Throwable` for the exception cause,
 *              defaulting to null.
 * @return The current `ULong` value if it successfully passes the validation.
 * @throws NumberOutOfRangeException if the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithConditions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `ULong` instance falls within the specified range,
 * and throws an exception if it does not.
 *
 * @param range The range of valid values, including any additional conditions encapsulated
 *              within the `ULongRangeWithConditions` object.
 * @param callableName The name of the callable object (e.g., method or function) associated
 *                     with the validation, or `null` if not applicable.
 * @param parameter The parameter being validated, represented as a `KParameter`, or `null` if not applicable.
 * @param message An optional validation failure message. If not provided, a default message
 *                will be constructed.
 * @param causeOf An optional transformer for generating a `Throwable` that serves as the
 *                primary cause of the validation failure.
 * @param cause An optional transformer for generating a `Throwable` that acts as an additional
 *              cause of the validation failure.
 * @return The current `ULong` value if it satisfies the specified range and conditions.
 * @throws NumberOutOfRangeException If the `ULong` is not within the specified range
 *                                   or fails the associated conditions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateIn(range: ULongRangeWithConditions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this !in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the integer is not within the specified range. If the integer is in the range,
 * throws a [NumberOutOfRangeException].
 *
 * @param range The range of integers to check against.
 * @param causeOf An optional transformer that provides a custom throwable if the validation fails.
 *                If null, the default [NumberOutOfRangeException] is used.
 * @param cause An optional transformer to provide the underlying cause for the exception.
 *              This can be used to attach additional context or reasons for the failure.
 * @return The integer itself if it is not within the given range.
 * @throws NumberOutOfRangeException If the integer is found within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRange, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range.
 * If the integer is within the range, an exception will be thrown.
 *
 * @param range The range of integers that the instance should not belong to.
 * @param causeOf A transformer that creates a Throwable based on the integer value, used as the root cause of the exception.
 * @param cause A transformer that provides an additional Throwable based on the integer value, used as the secondary cause of the exception.
 * @param lazyMessage A transformer that generates a message based on the integer value, used in the exception thrown.
 * @return The original integer if it is not within the specified range.
 * @throws NumberOutOfRangeException If the integer is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRange, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null, lazyMessage: Transformer<Int, Any>): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range. If the integer
 * is within the range, an exception is thrown.
 *
 * @param range The range of integers to check against.
 * @param property An optional reference to the property associated with the validation.
 * @param variableName The optional name of the variable being validated.
 * @param message An optional custom error message to include in the exception if thrown.
 * @param causeOf An optional transformer to generate the cause of the exception.
 * @param cause An optional transformer to generate a throwable cause based on the current integer.
 * @return The integer being validated if it is not within the specified range.
 * @throws NumberOutOfRangeException if the integer is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRange, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer value on which this method is called is not within the specified range.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The range of integers to check against. If the integer value is within this range, validation fails.
 * @param property An optional KProperty representing the primary property associated with the validation.
 * @param variable An optional KProperty representing an additional variable associated with the validation.
 * @param message An optional message providing context for the exception if validation fails.
 * @param causeOf An optional transformer that generates a throwable cause for the exception based on the integer value.
 * @param cause An optional transformer that generates a throwable cause for the exception based on the integer value.
 * @return The integer value itself if it passes validation (i.e., not within the specified range).
 * @throws NumberOutOfRangeException if the integer value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRange, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the receiver `Int` value does not fall within the specified range.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The range of `Int` values to check against.
 * @param callable The `KFunction` instance representing the callable function where this validation occurs.
 *                 Used to provide detailed information in the exception, if applicable. Can be null.
 * @param parameterName The name of the parameter being validated, used for exception messages. Optional, can be null.
 * @param message An optional custom message to include in the exception if the validation fails. Defaults to a descriptive message.
 * @param causeOf An optional `Transformer` that generates a specific exception based on the `Int` value when the validation fails.
 * @param cause An optional `Transformer` that generates the underlying cause of the exception when the validation fails.
 * @return The receiver `Int` value if it is not in the specified range.
 * @throws NumberOutOfRangeException If the receiver `Int` value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRange, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range. If the integer is within the range, an exception is thrown.
 *
 * @param range The range of integers to check against. If the integer is within this range, validation fails.
 * @param callable The KFunction instance representing the function to which the parameter belongs. This is used for exception context.
 * @param parameter The KParameter instance representing the specific parameter being validated. This is used for exception context.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf A transformer function that produces a Throwable based on the integer, used as the root cause of the exception.
 * @param cause A transformer function that produces a Throwable to be associated with the exception if validation fails.
 * @return The validated integer if it is not within the specified range.
 * @throws NumberOutOfRangeException if the integer is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRange, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range. If the integer is within the range,
 * it throws a `NumberOutOfRangeException` with additional details provided by the optional parameters.
 *
 * @param range The inclusive range of integers to check against.
 * @param callableName The name of the callable or function being validated, or null if unavailable.
 * @param parameterName The name of the parameter being validated, or null if unavailable.
 * @param message An optional custom message to include in the exception, or null for a default message.
 * @param causeOf A transformer to generate a custom throwable for the exception cause based on the input value, or null.
 * @param cause A transformer to generate a throwable representing the cause of the exception based on the input value, or null.
 * @return The integer itself if it is not within the specified range.
 * @throws NumberOutOfRangeException if the integer is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRange, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range. If the value is within the range, an exception is thrown.
 *
 * @param range The range of integers to check against.
 * @param callableName The name of the callable function in which this validation is applied, or null if not applicable.
 * @param parameter The parameter of the callable function associated with this validation, or null if not applicable.
 * @param message Optional custom message providing additional context for the exception. Defaults to null.
 * @param causeOf A transformer function that generates the root cause throwable for the exception, or null if not applicable.
 * @param cause A transformer function that generates the supplementary cause throwable for the exception, or null if not applicable.
 * @return The integer value if it is not within the specified range.
 * @throws NumberOutOfRangeException if the integer value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRange, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the integer is not within a specified range, including any excluded values, and throws an exception if the validation fails.
 *
 * @param range The range of integers with potential exclusions to validate against.
 * @param causeOf An optional transformer to generate a throwable based on the input integer.
 * @param cause An optional transformer to generate the cause of the throwable based on the input integer.
 * @return The integer itself if it is not within the range.
 * @throws NumberOutOfRangeException if the integer is within the range, including any excluded values.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithExclusions, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified inclusive range or its exclusions.
 * If the integer is within the range, an exception is thrown with the provided cause or lazy message.
 *
 * @param range The range with exclusions to check the integer against.
 * @param causeOf A transformer that provides a throwable cause based on the integer value,
 *                or null if no custom cause is required.
 * @param cause An additional transformer for throwable cause based on the integer value,
 *              used if `causeOf` is not provided.
 * @param lazyMessage A transformer that generates a message based on the integer value,
 *                    used in the exception if validation fails.
 * @return The validated integer if it is not within the specified range.
 * @throws NumberOutOfRangeException If the integer is within the specified range or its exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithExclusions, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null, lazyMessage: Transformer<Int, Any>): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer value is not within a specified range, which may include exclusions.
 * If the value is in the range, an exception is thrown.
 *
 * @param range The range of integers to check against, possibly containing exclusions.
 * @param property The optional property associated with this value, used for error reporting.
 * @param variableName An optional name of the variable to improve error clarity.
 * @param message An optional custom message to include in the exception if the validation fails.
 * @param causeOf An optional transformer to generate the primary cause of the exception.
 * @param cause An optional transformer to generate the secondary cause of the exception.
 * @return The original integer value if it is not in the specified range.
 * @throws NumberOutOfRangeException if the integer value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithExclusions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range, including any exclusions defined
 * in the range, and throws an exception if the validation fails.
 *
 * @param range The specified range with exclusions to validate against.
 * @param property Optional metadata property for the validated value.
 * @param variable Optional metadata variable for the validated value.
 * @param message Optional custom error message to use if validation fails.
 * @param causeOf Optional transformer to generate the cause throwable based on the value.
 * @param cause Optional transformer to generate the cause throwable based on the value.
 * @return The integer value if it passes the validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithExclusions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the integer is not within the specified range, including any defined exclusions.
 * If the integer is within the range, an exception is thrown.
 *
 * @param range The range with potential exclusions to validate the integer against.
 * @param callable The associated function for contextual purposes, if applicable.
 * @param parameterName An optional parameter name to include in the exception message for better traceability.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf A transformer to generate a throwable cause if validation fails.
 * @param cause A transformer to create an additional throwable cause if validation fails.
 * @return The integer being validated if it is not within the range or exclusions.
 * @throws NumberOutOfRangeException if the integer is within the range or exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithExclusions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range, including any exclusions defined within the range.
 * If the integer is within the range, this method throws an exception.
 *
 * @param range The range to validate against, which may include exclusions.
 * @param callable Optional reference to the function being executed, used for exception context.
 * @param parameter Optional reference to the parameter being validated, used for exception context.
 * @param message Optional custom message to be included in the exception if validation fails.
 * @param causeOf An optional transformer to create a specific exception based on the value that failed validation.
 * @param cause An optional transformer to provide the underlying cause for the generated exception.
 * @return The integer itself if it passes validation (i.e., it is not in the specified range).
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithExclusions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range, including handling of exclusions.
 * If the integer is found to be within the range, an exception is thrown with an optional customized message and cause.
 *
 * @param range The range to validate against, allowing for the possibility of certain exclusions.
 * @param callableName The name of the callable (e.g., function or method) performing the validation. Can be null if not applicable.
 * @param parameterName The name of the parameter being validated. Can be null if not applicable.
 * @param message An optional custom message to include in the thrown exception if validation fails. Defaults to null.
 * @param causeOf An optional transformer that provides a specific cause of the failure as a throwable, based on the value being validated.
 * @param cause An optional transformer that provides additional context for the exception thrown if validation fails.
 *
 * @return The integer that has been successfully validated as not within the specified range.
 * @throws NumberOutOfRangeException If the integer is found to be within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithExclusions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer value is not within the specified range (including exclusions).
 * If the value falls within the range, an exception is thrown.
 *
 * @param range The range of integers to check against, including any specific exclusions.
 * @param callableName An optional name of the callable associated with this validation.
 * @param parameter An optional reflection parameter to provide additional context about the validation.
 * @param message An optional custom error message to use if the value is invalid.
 * @param causeOf An optional transformer to create the cause of the exception based on the current value.
 * @param cause An optional transformer to generate a nested cause exception based on the current value.
 * @return The validated integer if it is not within the specified range.
 * @throws NumberOutOfRangeException If the integer is within the specified range or matches any excluded values.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithExclusions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the integer is not within the specified range. If the value is in the range,
 * an exception is thrown. The exception can be customized using the optional transformer parameters.
 *
 * @param range the range against which the integer is validated
 * @param causeOf a transformer for creating a base throwable cause, executed if the value is in the range
 * @param cause an optional transformer to provide an additional throwable cause for the exception
 * @return the integer itself if it is not in the range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithConditions, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range. If the integer is within the range,
 * an exception is thrown with an optional cause and a lazily generated message.
 *
 * @param range The range with specific conditions that this integer should not be within.
 * @param causeOf An optional transformer to generate a throwable that serves as the cause of the exception.
 * @param cause An optional transformer to generate an additional cause for the exception.
 * @param lazyMessage A transformer to generate a message for the exception lazily, using the integer value.
 * @return The integer being validated, if it passes the validation.
 * @throws NumberOutOfRangeException If the integer is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithConditions, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null, lazyMessage: Transformer<Int, Any>): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range. If the integer is within the range,
 * an exception is thrown based on the provided parameters.
 *
 * @param range The range of integers to be validated against.
 * @param property The property reference associated with the value being validated, or null if not applicable.
 * @param variableName The name of the variable being validated, or null if not applicable.
 * @param message An optional custom message to include in the exception if the validation fails.
 * @param causeOf A transformer function that generates a throwable based on the integer value, used if specified.
 * @param cause An optional transformer function that generates a throwable to use as the cause of the exception.
 * @return The validated integer if it is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithConditions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range. If the integer
 * is within the range, an exception is thrown.
 *
 * @param range The range of integers to check against, provided as an IntRangeWithConditions.
 * @param property An optional Kotlin property that represents metadata about the current object (can be null).
 * @param variable An optional Kotlin property that represents metadata about the target variable (can be null).
 * @param message An optional custom error message to include in the thrown exception (default is null).
 * @param causeOf An optional transformer to determine the specific cause exception (default is null).
 * @param cause An optional transformer to generate the base throwable cause related to the value (default is null).
 * @return Returns the integer value if validation passes (it's not within the specified range).
 * @throws NumberOutOfRangeException If the integer is found to be within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithConditions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer this method is invoked on is not within the specified range,
 * and throws an exception if the integer is within the range.
 *
 * @param range The range of integers to check against.
 * @param callable The Kotlin function that may be associated with the validation. Can be null.
 * @param parameterName An optional name of the parameter associated with the validation. Can be null.
 * @param message An optional message for the exception if the value is within the range. Can be null.
 * @param causeOf An optional transformer function to generate a cause exception if the value is within the range.
 * @param cause An optional transformer function to generate the primary cause exception.
 * @return The validated integer if it is not within the specified range.
 * @throws NumberOutOfRangeException if the integer is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithConditions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer is not within the specified range, throwing an exception if the validation fails.
 *
 * @param range The range to check against, which may contain specific conditions.
 * @param callable An optional reference to the callable function related to the validation. Can be null.
 * @param parameter An optional parameter reference associated with the validation. Can be null.
 * @param message An optional custom error message to include in the exception. Defaults to null.
 * @param causeOf An optional transformer to generate a specific throwable based on the integer value if validation fails. Defaults to null.
 * @param cause An optional transformer to generate a secondary throwable based on the integer value. Defaults to null.
 * @return The validated integer if it is not within the specified range.
 * @throws NumberOutOfRangeException If the integer value is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithConditions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer value is not within the specified range.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The range to check against, including additional conditions.
 * @param callableName The name of the function or context invoking the validation. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message for the exception. If not provided, a default message is used.
 * @param causeOf An optional transformer to generate a throwable as the root cause, which can incorporate additional processing on the value causing the exception.
 * @param cause An optional transformer to generate a throwable for the exception's direct cause, which can incorporate additional processing on the value causing the exception.
 * @return The integer value if it is not within the specified range.
 * @throws NumberOutOfRangeException If the value is within the specified range, or if the `causeOf` transformer generates a throwable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithConditions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the integer on which this method is called is not within the specified range.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The range of integers to check against. Validation fails if the integer is within this range.
 * @param callableName The name of the callable context in which the validation occurs (optional).
 * @param parameter The parameter being validated, if applicable (optional).
 * @param message The custom error message to use in the exception if validation fails (optional).
 * @param causeOf A transformer function to generate the specific throwable cause if validation fails (optional).
 * @param cause An alternative transformer function to generate the throwable cause, used if `causeOf` is not provided (optional).
 * @return The validated integer itself if it is not in the specified range.
 * @throws NumberOutOfRangeException if the integer is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Int.validateNotIn(range: IntRangeWithConditions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Int, Throwable>? = null, cause: Transformer<Int, Throwable>? = null): Int {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current `UInt` is not within the specified range. If the value is within
 * the range, an exception will be thrown.
 *
 * @param range The range of `UInt` values to check against.
 * @param causeOf An optional transformer to generate a throwable based on the current value
 *                if it is in the range. If provided and the value is in the range, this will
 *                create the exception that is thrown.
 * @param cause An optional transformer to generate a root cause throwable based on the current value.
 * @return The current `UInt` value if it passes the validation.
 * @throws NumberOutOfRangeException If the value is in the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRange, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling unsigned integer is not within the specified range.
 * If the value is within the range, an exception is thrown based on the provided transformers and lazy message.
 *
 * @param range The range of unsigned integers to check against.
 * @param causeOf A transformer function used to produce a custom throwable cause if the validation fails. Optional.
 * @param cause A transformer function used to generate a throwable cause if the validation fails. Optional.
 * @param lazyMessage A transformer function used to generate a lazy message for the exception thrown when validation fails.
 * @return The calling unsigned integer if it is not in the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRange, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null, lazyMessage: Transformer<UInt, Any>): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current unsigned integer value is not within the specified range.
 * If the value is within the range, a `NumberOutOfRangeException` will be thrown.
 *
 * @param range The range of unsigned integers to check against. If the value is within this range, an exception is thrown.
 * @param property An optional `KProperty` reference associated with the value for contextual information.
 * @param variableName The name of the variable being validated, or null if not provided.
 * @param message An optional custom message for the exception. Defaults to a message indicating the value is within the range.
 * @param causeOf An optional transformer to generate a throwable cause based on the value, providing more contextual information.
 * @param cause An optional transformer to generate a throwable cause directly tied to the validation failure.
 * @return The original unsigned integer if it is not within the specified range.
 * @throws NumberOutOfRangeException If the value is found within the specified range.
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRange, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the unsigned integer is not within the specified range. If the value is within the range,
 * a `NumberOutOfRangeException` is thrown.
 *
 * @param range The range to validate against. The method ensures the value is not inside this range.
 * @param property The property associated with the validation, used for error reporting.
 * @param variable An optional additional variable associated with the validation, used for error reporting.
 * @param message An optional custom message to include in the exception if the validation fails.
 * @param causeOf An optional transformation function to generate a `Throwable` using the input value.
 *                Used as the primary cause for the exception.
 * @param cause An optional transformation function to generate a `Throwable` using the input value.
 *              Used as the secondary cause of the exception.
 * @return The validated unsigned integer value if it is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRange, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current unsigned integer value is not within the specified range.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The range of unsigned integer values to validate against.
 * @param callable The callable function (KFunction) where this validation is being performed, used for exception details. Optional, can be null.
 * @param parameterName The name of the parameter associated with this validation, used for exception details. Optional, defaults to null.
 * @param message An additional message to include in the exception if validation fails. Optional, defaults to null.
 * @param causeOf A transformer to generate the primary exception (Throwable) from the current value if validation fails. Optional, defaults to null.
 * @param cause A transformer to generate a secondary cause exception (Throwable) from the current value if validation fails. Optional, defaults to null.
 * @return The current unsigned integer value if it passes validation (i.e., is not within the specified range).
 * @throws NumberOutOfRangeException If the current value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRange, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `UInt` value is not within the specified range. If the value is within the range,
 * an exception is thrown. The exception can be customized with an optional message or transformation logic for
 * the cause and exception itself.
 *
 * @param range The range of `UInt` values within which the current value should not exist.
 * @param callable The `KFunction` instance representing the function from which this validation is being invoked.
 *                 This parameter is used for providing detailed exception context.
 * @param parameter The `KParameter` instance representing the specific parameter being validated.
 *                  This parameter is used for providing detailed exception context.
 * @param message An optional message for the exception to provide additional context. Defaults to null.
 * @param causeOf An optional transformer to generate the throwable cause based on the current value. Defaults to null.
 * @param cause An optional transformer to generate a nested throwable cause based on the current value. Defaults to null.
 * @return The current `UInt` value if it is not within the range.
 * @throws NumberOutOfRangeException If the current `UInt` value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRange, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the unsigned integer (`UInt`) is not within the specified range (`UIntRange`).
 * If the value is within the range, a `NumberOutOfRangeException` is thrown.
 *
 * @param range The range of unsigned integer values to check against.
 * @param callableName The name of the callable or function where this validation occurs, or null if unavailable.
 * @param parameterName The name of the parameter to be validated, or null if unavailable.
 * @param message An optional custom message to include in the exception, or null to use the default message.
 * @param causeOf An optional transformer that produces a throwable representing the cause of the exception,
 *                executed prior to throwing the exception.
 * @param cause An optional transformer that produces a throwable representing the underlying cause of the exception.
 * @return The validated `UInt` if it is not within the specified range.
 * @throws NumberOutOfRangeException If the `UInt` falls within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRange, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling `UInt` value is not within the specified range.
 * If the value is found within the range, an exception is thrown.
 *
 * @param range The range of `UInt` values to check against.
 * @param callableName The name of the callable function where the validation is occurring, or null if not provided.
 * @param parameter The parameter associated with the validation process, or null if not applicable.
 * @param message An optional custom message for the exception if validation fails. Defaults to null.
 * @param causeOf An optional transformer to generate the primary exception based on the value, or null if not provided.
 * @param cause An optional transformer to generate a secondary cause exception based on the value, or null if not provided.
 * @return The `UInt` value if it is not within the specified range.
 * @throws NumberOutOfRangeException If the `UInt` value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRange, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current unsigned integer is not within the specified range. If the value is
 * within the range, an exception is thrown.
 *
 * @param range The range and its exclusions used for validation.
 * @param causeOf A transformer that produces a throwable cause from the value, used to customize
 * the exception's cause (optional).
 * @param cause A transformer that produces a throwable cause from the value, used as an alternate
 * way to customize the exception's cause (optional).
 * @return The validated unsigned integer if it is not in the range.
 * @throws NumberOutOfRangeException If the value is found within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithExclusions, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `UInt` value is not within the specified range, including any specified exclusions.
 * Throws an exception if the value is found within the range.
 *
 * @param range The range, possibly with exclusions, against which the value is validated.
 * @param causeOf An optional transformer to generate the primary exception based on the current value.
 * @param cause An optional transformer to generate the cause of the primary exception based on the current value.
 * @param lazyMessage A transformer to lazily generate the exception message using the current value.
 * @return The current `UInt` value if it is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithExclusions, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null, lazyMessage: Transformer<UInt, Any>): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the unsigned integer value is not within the specified range or its exclusions.
 * If the value resides within the range, an exception is thrown.
 *
 * @param range The `UIntRangeWithExclusions` defining the valid range with optional exclusions.
 * @param property The property being validated, if applicable. This can be null.
 * @param variableName The name of the variable associated with the value being validated. This can be null.
 * @param message An optional custom error message for the exception. This can be null.
 * @param causeOf A transformer function that generates a `Throwable` cause for the exception if the value fails validation. Can be null.
 * @param cause A transformer function that provides the underlying cause of the exception if validation fails. Can be null.
 * @return The validated unsigned integer value if it is not within the specified range or exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithExclusions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `UInt` is not within the specified range, including any exclusions in the range.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The range with optional exclusions to validate against.
 * @param property The primary property associated with the validation, if applicable.
 * @param variable An optional secondary reference property used in the validation context.
 * @param message An optional custom message to include with the exception, if the validation fails.
 * @param causeOf A transformer that generates a cause `Throwable`, optionally wrapping the exception.
 * @param cause An optional transformer to generate the cause `Throwable` if validation fails.
 * @return The validated `UInt` if it is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithExclusions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling `UInt` value is not within the specified range, including any exclusions in the range.
 * If the value is found within the range, an exception is thrown.
 *
 * @param range The range with potential exclusions to check against.
 * @param callable The callable function reference used in exception creation. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message A custom message to include in the exception, if thrown. Can be null.
 * @param causeOf A transformer used to produce the specific exception, leveraging the value for context. Can be null.
 * @param cause A transformer used to wrap the cause exception, leveraging the value for context. Can be null.
 * @return The original `UInt` value if validation passes successfully.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithExclusions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the unsigned integer is not within the specified range, including any excluded values.
 * Throws an exception if the validation fails.
 *
 * @param range The range of unsigned integers to check against, including exclusions.
 * @param callable Optional reference to the function being validated, used for error reporting.
 * @param parameter Optional reference to the parameter being validated, used for error reporting.
 * @param message Optional custom error message to include in the exception if validation fails.
 * @param causeOf Optional transformer that maps the unsigned integer to a throwable cause if validation fails.
 * @param cause Optional transformer that provides an additional throwable cause for chaining.
 * @return The unsigned integer if it passes the validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithExclusions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the unsigned integer is not within the specified range, including any exclusions.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The range, including exclusions, against which the value is validated.
 * @param callableName The name of the callable function or context, used in exception messages.
 * @param parameterName An optional parameter name to provide more context in exception messages.
 * @param message A custom message included in the exception if the value is invalid.
 * @param causeOf An optional transformer to generate a specific exception for the validation failure.
 * @param cause An optional transformer to create a causal exception for the validation failure.
 * @return The validated unsigned integer if it is not in the specified range.
 * @throws NumberOutOfRangeException If the unsigned integer is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithExclusions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the unsigned integer is not within the specified range, including any exclusions in the range.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The range of unsigned integers to validate against, including exclusions.
 * @param callableName The name of the callable (function or property) related to the validation context. Can be null.
 * @param parameter The parameter associated with the validation context. Can be null.
 * @param message An optional custom message to be used in the exception if the validation fails. Defaults to null.
 * @param causeOf An optional transformer that generates a specific throwable exception when the validation fails. Defaults to null.
 * @param cause An optional transformer that generates a base throwable cause to include in the thrown exception. Defaults to null.
 * @return The original unsigned integer if it is not within the specified range.
 * @throws NumberOutOfRangeException if the unsigned integer is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithExclusions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current unsigned integer is not within the specified range.
 * If the value is found within the range, an exception is thrown.
 *
 * @param range The range to check against, including conditions associated with the range.
 * @param causeOf An optional transformer that generates a specific throwable instance based on the value,
 *                which will be used as the root cause of the exception.
 * @param cause An optional transformer for generating an additional throwable instance associated with the value.
 * @return The value itself if the validation passes (i.e., the value is not in the specified range).
 * @throws NumberOutOfRangeException if the value is found to be within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithConditions, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `UInt` value is not within the specified range of values.
 * If the value is within the range, an exception is thrown with an optional custom cause or message.
 *
 * @param range The `UIntRangeWithConditions` specifying the range of values to check against.
 * @param causeOf An optional transformer that generates a throwable cause based on the `UInt` value. If null, a default exception is used.
 * @param cause An optional transformer that provides an additional cause for the exception based on the `UInt` value.
 * @param lazyMessage A transformer that generates a custom message object based on the `UInt` value for the thrown exception.
 * @return The current `UInt` value, if it is not within the specified range.
 * @throws Throwable if the value is within the specified range, with an optional custom message and cause.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithConditions, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null, lazyMessage: Transformer<UInt, Any>): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `UInt` value is not within the specified range.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The range of values to check against. The current value should not be in this range.
 * @param property Optional property reference for identifying the source of the value being validated.
 * @param variableName Optional variable name for context in error messages.
 * @param message Optional custom error message to use when validation fails.
 * @param causeOf Optional transformer function to generate a specific exception based on the value.
 * @param cause Optional transformer function to provide additional exception chaining.
 * @return The current `UInt` value, if it is not in the specified range.
 * @throws NumberOutOfRangeException if the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithConditions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current unsigned integer is not within the specified range.
 * If the value is within the range, it throws an exception either generated by the
 * provided [causeOf] transformer or a default `NumberOutOfRangeException`.
 *
 * @param range The range of unsigned integers with specific conditions to check against.
 * @param property The property of the object being validated. Can be null if not applicable.
 * @param variable The variable associated with the validation. Can be null if not applicable.
 * @param message An optional custom error message to be included in the exception, if thrown.
 * @param causeOf An optional transformer function used to generate the cause of the exception.
 *                If null, a default `NumberOutOfRangeException` is used.
 * @param cause An optional transformer function used to generate a cause for the exception.
 * @return The original unsigned integer if the validation passes (i.e., the number is not in the range).
 * @throws NumberOutOfRangeException If the current value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithConditions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the unsigned integer is not within the specified range. If the value falls within
 * the range, an exception is thrown.
 *
 * @param range The range of unsigned integers to check against.
 * @param callable The callable function related to this validation, if applicable. Can be null.
 * @param parameterName The name of the parameter being validated, if applicable. Can be null.
 * @param message An optional custom error message to include in the exception. Can be null.
 * @param causeOf A transformer to produce a throwable when the validation fails, if provided. Can be null.
 * @param cause An alternative transformer to produce a throwable when the validation fails. Can be null.
 * @return The unsigned integer if it is not within the specified range.
 * @throws NumberOutOfRangeException if the unsigned integer is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithConditions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `UInt` value is not within the specified range. If the value is within the range,
 * an exception will be thrown using the provided transformers and optional context details.
 *
 * @param range The range of type `UIntRangeWithConditions` against which the current value is validated.
 * @param callable The function or callable reference generally used for error context. Can be null.
 * @param parameter The parameter reference for providing additional context. Can be null.
 * @param message An optional custom message to include in the exception. If not specified, a default message is used.
 * @param causeOf An optional transformer that determines the specific throwable to raise if the validation fails.
 * @param cause An optional transformer that provides additional contextual information to include in the exception.
 * @return Returns the current `UInt` value if it successfully passes validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithConditions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the unsigned integer is not within the specified range. If the value is found in the range,
 * an exception is thrown.
 *
 * @param range The range of unsigned integers to validate against. If the value is within this range, an exception is thrown.
 * @param callableName The name of the callable function, used to provide better context in the exception message. Can be null.
 * @param parameterName The name of the parameter being validated, used to enhance the exception details. Can be null.
 * @param message Optional custom error message to include in the exception if validation fails. Defaults to indicating that the value is within the range.
 * @param causeOf Optional transformer to generate a custom `Throwable` as the root cause of the thrown exception based on the invalid value. Can be null.
 * @param cause Optional transformer to generate a `Throwable` to include as the cause of the thrown exception based on the invalid value. Can be null.
 * @return The original unsigned integer if it is not within the specified range.
 * @throws NumberOutOfRangeException If the unsigned integer is within the specified range, including any custom cause if provided.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithConditions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current unsigned integer value is not within the specified range with conditions.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The range with conditions to validate the unsigned integer against.
 * @param callableName The name of the callable related to the validation (optional).
 * @param parameter The parameter associated with the callable being validated (optional).
 * @param message A custom message to include in the exception if validation fails (optional).
 * @param causeOf Transformer to create the primary throwable cause when validation fails (optional).
 * @param cause Transformer to create additional throwable causes for the exception (optional).
 * @return The unsigned integer value, if it is successfully validated and not in the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UInt.validateNotIn(range: UIntRangeWithConditions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<UInt, Throwable>? = null, cause: Transformer<UInt, Throwable>? = null): UInt {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the given `Long` value is not within the specified range.
 * If the value is within the range, an exception is thrown.
 *
 * @param range the range from which the value must not belong.
 * @param causeOf an optional transformer that creates a specific exception for the provided value.
 * @param cause an optional transformer that provides the root cause exception for the validation failure.
 * @return the original value if it is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRange, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the receiver is not within the specified range. If the receiver is within the range,
 * an exception will be thrown based on the provided `causeOf`, `cause`, or `lazyMessage` parameters.
 *
 * @param range the range of numbers to validate against. If the receiver is within this range, validation fails.
 * @param causeOf an optional transformer used to generate the exception to be thrown. Defaults to `null`.
 * @param cause an optional transformer used to generate the cause of the exception. Defaults to `null`.
 * @param lazyMessage a transformer used to compute a message to include in the exception.
 * @return the receiver if it is not within the specified range, allowing for method chaining.
 * @throws NumberOutOfRangeException if the receiver is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRange, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null, lazyMessage: Transformer<Long, Any>): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `Long` value is not within the specified range.
 * If the value lies within the range, an exception is thrown.
 *
 * @param range The range of values that the current `Long` value must not belong to.
 * @param property An optional `KProperty` reference providing contextual information, such as the property name and type.
 * @param variableName An optional variable name associated with the value for contextual exception messages.
 * @param message An optional custom error message to be included if validation fails.
 * @param causeOf An optional transformer that generates a custom throwable to be used as the main cause of the exception.
 * @param cause An optional transformer that generates a throwable to act as a nested cause of the exception.
 * @return The current `Long` value if it is not within the specified range.
 * @throws NumberOutOfRangeException If the current value falls within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRange, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `Long` value is not within the specified range. If the value is within the range, it throws a
 * `NumberOutOfRangeException` with a detailed message and optional cause.
 *
 * @param range The range of `Long` values that the current value should not be in.
 * @param property The primary property involved in the validation, whose metadata may be used in the exception message.
 * @param variable An optional variable whose metadata may be additionally included in the exception message.
 * @param message An optional string providing additional information or context about the exception.
 * @param causeOf An optional transformer function that generates a `Throwable` as the primary cause of the exception.
 * @param cause An optional transformer function that generates a secondary `Throwable` to serve as the cause of the exception.
 * @return The current `Long` value if it is not in the specified range.
 * @throws NumberOutOfRangeException If the current `Long` value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRange, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified range.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The inclusive range of values to check against.
 * @param callable The KFunction instance representing the callable function where this validation is performed.
 *                 Used for providing context in the exception message. Can be null.
 * @param parameterName The name of the parameter being validated. Optional, can be null.
 * @param message A custom message to include in the exception if validation fails. Optional, can be null.
 * @param causeOf A transformer to generate the root cause of the exception. Optional, can be null.
 * @param cause A transformer to generate an additional cause for the exception. Optional, can be null.
 * @return The original value if it passes the validation.
 * @throws NumberOutOfRangeException If the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRange, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the receiver [Long] is not within the specified [range]. If the value is within the [range],
 * an exception is thrown.
 *
 * @param range The range to validate against. If the value is within this range, an exception is thrown.
 * @param callable An optional [KFunction] that provides context for where the validation is being performed.
 * @param parameter An optional [KParameter] that specifies the parameter being validated, used for error reporting.
 * @param message An optional message providing additional context in the exception. Defaults to a message
 *        indicating the value lies within the range.
 * @param causeOf An optional transformer to construct the root cause of the exception based on the offending value.
 * @param cause An optional transformer to provide a custom throwable based on the offending value.
 * @return The original receiver value if it is not within the specified range.
 * @throws NumberOutOfRangeException If the receiver value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRange, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking `Long` is not within the specified range. If the value falls within
 * the given range, this method throws a `NumberOutOfRangeException`.
 *
 * @param range The range of `Long` values, the invoking value is checked against.
 * @param callableName The name of the callable or function invoking this validation, or null if unavailable.
 * @param parameterName An optional name of the parameter being validated, or null if unavailable.
 * @param message An optional custom error message for the exception, or null to use the default.
 * @param causeOf An optional transformer to generate a specific exception as the root cause if the value is invalid.
 * @param cause An optional transformer to provide a cause for the `NumberOutOfRangeException`.
 * @return The validated `Long` value if it is not within the specified range.
 * @throws NumberOutOfRangeException If the invoking value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRange, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given `Long` value is not within the specified range. If the value is within the range,
 * an exception is thrown based on the provided parameters.
 *
 * @param range The range that the value should not belong to.
 * @param callableName The name of the callable function within which the validation is performed, or null if not applicable.
 * @param parameter The parameter of the callable function being validated, or null if not applicable.
 * @param message Optional custom message to include in the exception if the value is in the range. Default is null.
 * @param causeOf Optional transformer to generate a custom throwable cause based on the value. Default is null.
 * @param cause Optional transformer to generate a root cause throwable based on the value. Default is null.
 * @return The original `Long` value if it is not within the specified range.
 * @throws NumberOutOfRangeException If the value is within the specified range, including optional custom messages and causes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRange, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the provided value is not within the specified range, throwing an exception if it is.
 *
 * @param range The range with exclusions that the value is validated against.
 * @param causeOf An optional transformer to generate a cause for the thrown exception.
 * @param cause An optional transformer to generate an additional cause for the exception.
 * @return The value itself if it is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithExclusions, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `Long` value is not within the given range, including any defined exclusions.
 * If the value is within the range or its exclusions, an exception is thrown with a specified message or cause.
 *
 * @param range The range and exclusions to validate against.
 * @param causeOf An optional transformer to create a custom exception using the current value as input.
 * @param cause An optional transformer to create a nested cause exception using the current value as input.
 * @param lazyMessage A transformer to generate a custom message when the value is invalid.
 * @return The current `Long` value if it is not within the specified range or exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithExclusions, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null, lazyMessage: Transformer<Long, Any>): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [Long] value is not within the specified [range]. If the value is found
 * within the [range], an exception is thrown, which can be customized using the optional parameters.
 *
 * @param range The [LongRangeWithExclusions] against which the current value is validated.
 * @param property An optional [KProperty] representing the property associated with the value.
 * @param variableName An optional name of the variable associated with the value.
 * @param message An optional message to include in the exception if the value is found within the [range].
 * @param causeOf An optional transformer to create the base exception if the value is found within the [range].
 * @param cause An optional transformer to create the cause of the exception thrown, tied to the value.
 * @return The current [Long] value if it is not in the specified [range].
 * @throws NumberOutOfRangeException If the value is found within the specified [range].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithExclusions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling `Long` value is not within the specified range, including any exclusions.
 * If the value is within the range, an exception is thrown based on the provided parameters.
 *
 * @param range the range of `Long` values, including exclusions, that the value should not be within
 * @param property an optional reference to the property associated with the value being validated
 * @param variable an optional reference to the variable associated with the value being validated
 * @param message an optional custom message for the exception if validation fails
 * @param causeOf an optional transformer used to produce a specific throwable when validation fails
 * @param cause an optional transformer used to add a cause to the produced exception if validation fails
 * @return the calling `Long` value if validation succeeds
 * @throws NumberOutOfRangeException if the value is within the range (including exclusions)
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithExclusions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `Long` value is not within the specified range, including handling exclusions.
 * If the value is found within the range, an exception is thrown.
 *
 * @param range The range, including optional exclusions, against which the `Long` value is validated.
 * @param callable The callable function associated with this validation, used for error reporting purposes. Can be null.
 * @param parameterName The name of the parameter being validated, used in error messages. Can be null.
 * @param message An optional custom message for the exception if validation fails. Defaults to a standard message if not provided.
 * @param causeOf A transformer that provides a custom exception, taking the current `Long` value as input. Can be null.
 * @param cause A transformer that generates a cause `Throwable`, taking the current `Long` value as input. Can be null.
 * @return The original `Long` value if validation is successful.
 * @throws NumberOutOfRangeException If the value is in the specified range or within any exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithExclusions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `Long` value is not within the specified range, including handling exclusions.
 * If the value is found within the range, an exception is thrown.
 *
 * @param range The range to check against, including possible exclusions.
 * @param callable The callable function to associate with the validation, used for error reporting or debugging.
 * @param parameter The parameter being validated, used for error reporting or debugging.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf A transformer function to produce a custom exception based on the current value if validation fails.
 * @param cause An optional transformer function to produce a cause exception linked to the one raised if validation fails.
 * @return The validated `Long` value if it is not within the specified range.
 * @throws NumberOutOfRangeException If the current value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithExclusions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [Long] value is not within the specified [range].
 * If the value is found to be within the range, an exception is thrown.
 *
 * @param range The range with optional exclusions against which the value is validated.
 * @param callableName The name of the callable in which this validation occurs, used for informational purposes.
 * @param parameterName The name of the parameter being validated, used for context in the error message.
 * @param message The custom error message to include if validation fails; defaults to a message indicating the value is in the range.
 * @param causeOf An optional transformer that generates a throwable to represent the root cause of the error.
 * @param cause An optional transformer that generates a throwable to represent the underlying exception.
 * @return The validated [Long] value if it is not in the specified range.
 * @throws NumberOutOfRangeException If the value is found within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithExclusions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `Long` value is not within the specified range, including any defined exclusions.
 * If the value is in the range, an exception is thrown.
 *
 * @param range The range (with possible exclusions) to check against.
 * @param callableName The name of the callable being validated, used for exception messages. May be null.
 * @param parameter The parameter being validated, used for exception messages. May be null.
 * @param message An optional custom error message to include in the exception if validation fails. Defaults to null.
 * @param causeOf A transformer function to generate the specific cause of the exception when validation fails. May be null.
 * @param cause A transformer function to generate a more generic cause of the exception when validation fails. May be null.
 * @return The original `Long` value if it passes validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithExclusions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current `Long` value is not within the specified `range`.
 * If the value exists in the range, an exception is thrown.
 *
 * @param range The range of `Long` values to check against.
 * @param causeOf An optional transformer that provides a specific throwable cause based on the current value.
 * @param cause An optional transformer to provide a base throwable cause for the exception.
 * @return The current `Long` value if it is not in the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithConditions, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current Long value is not within the specified range.
 * If the value falls within the range, an exception is thrown based on the provided transformers or default behavior.
 *
 * @param range The range of values to check against. The current value should not fall within this range.
 * @param causeOf An optional transformer to create a custom exception based on the current value. If null, a default exception is used.
 * @param cause An optional transformer to specify the cause of the exception based on the current value.
 * @param lazyMessage A transformer used to lazily generate the exception message based on the current value.
 * @return The current Long value if it is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithConditions, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null, lazyMessage: Transformer<Long, Any>): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `Long` value is not within the specified range. If the value is found
 * within the range, an exception is thrown with the provided message or a default message.
 *
 * @param range The range of values to check against.
 * @param property The optional property metadata associated with the value being validated.
 * @param variableName An optional name for the variable being validated, used in exception messages.
 * @param message An optional custom error message to include in the exception.
 * @param causeOf An optional transformer to produce a custom exception that wraps the validation failure.
 * @param cause An optional transformer to produce a cause exception for the validation failure.
 * @return The current `Long` value if validation is successful.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithConditions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling value is not within the specified range. If the value falls within
 * the specified range, a custom exception is thrown.
 *
 * @param range The range of type `LongRangeWithConditions` to validate against.
 * @param property Optional reference to a property that represents the value being validated.
 * @param variable Optional reference to another property linked to validation.
 * @param message Optional custom error message to include in the exception if the value is in the range.
 * @param causeOf Optional transformer that produces an exception representing the cause, initialized with the current value.
 * @param cause Optional transformer that produces an exception to be chained as the cause.
 * @return The current value if it does not fall within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithConditions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current Long value is not within the specified range.
 * If the value is within the range, an exception is thrown.
 *
 * @param range The inclusive range of Long values to check against.
 * @param callable An optional reference to the callable associated with this validation.
 * @param parameterName An optional name of the parameter being validated.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer to produce a custom throwable exception as the primary cause.
 * @param cause An optional transformer to produce a throwable to attach as the cause of the exception.
 * @return The current Long value if it is not within the specified range.
 * @throws NumberOutOfRangeException if the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithConditions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified range. If the value is within the range,
 * an exception is thrown with optional messages and cause transformers.
 *
 * @param range The range of values to check against.
 * @param callable The callable function associated with the validation, used for error reporting.
 * @param parameter The parameter associated with the validation, used for error reporting.
 * @param message An optional custom error message describing the validation failure.
 * @param causeOf An optional transformer to generate a specific Throwable when the value fails validation.
 * @param cause An optional transformer to generate a cause Throwable to attach to the main exception.
 * @return The current value if it is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithConditions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `Long` value is not within the specified range. If the value is found
 * within the range, an exception is thrown using the provided parameters to construct the error.
 *
 * @param range The range of `Long` values to validate against. The current value should not be within this range.
 * @param callableName The name of the callable function or context invoking the validation. Can be `null`.
 * @param parameterName The name of the parameter being validated. Can be `null`.
 * @param message An optional custom message to include in the exception if validation fails. Can be `null`.
 * @param causeOf A transformer generating a throwable based on the value, used as the primary cause if validation fails. Can be `null`.
 * @param cause A transformer generating a throwable based on the value, used as a secondary cause if validation fails. Can be `null`.
 * @return The current `Long` value if it passes validation.
 * @throws NumberOutOfRangeException If the value is within the specified range and validation fails.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithConditions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given Long value is not within the specified range. If the value is within the range,
 * an exception is thrown based on the provided transformer or default logic.
 *
 * @param range The range to check the value against.
 * @param callableName The name of the callable associated with the validation, may be null.
 * @param parameter The parameter of the callable associated with the validation, may be null.
 * @param message An optional custom message to describe the validation error.
 * @param causeOf A transformer for creating the primary exception to be thrown, based on the value. May be null.
 * @param cause A transformer for creating the underlying cause exception, based on the value. May be null.
 * @return The Long value if it is not within the specified range.
 * @throws NumberOutOfRangeException If the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun Long.validateNotIn(range: LongRangeWithConditions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Long, Throwable>? = null, cause: Transformer<Long, Throwable>? = null): Long {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current `ULong` value is not within the specified range.
 * If the value is within the range, a `NumberOutOfRangeException` is thrown.
 * Optional transformers can be provided to customize the exception cause.
 *
 * @param range The `ULongRange` to check against. The value must not be within this range.
 * @param causeOf An optional transformer to generate a customized `Throwable`
 *                based on the current value when validation fails.
 * @param cause An optional transformer to generate a customized `Throwable`
 *              to include as the cause of the exception when validation fails.
 * @return The current `ULong` value if validation passes.
 * @throws NumberOutOfRangeException if the current value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRange, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value is not within the specified range.
 * If the value is within the range, a `NumberOutOfRangeException` is thrown.
 *
 * @param range The range to check the value against.
 * @param causeOf An optional transformer to produce a custom exception to throw when validation fails, based on the current value.
 * @param cause An optional transformer to produce the cause of the exception, based on the current value.
 * @param lazyMessage A transformer to generate the exception message lazily, based on the current value.
 * @return The current `ULong` value, if validation passes.
 * @throws NumberOutOfRangeException If the current value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRange, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null, lazyMessage: Transformer<ULong, Any>): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value is not within the specified range.
 * If the value is within the range, a `NumberOutOfRangeException` is thrown with an optional message
 * and cause transformers.
 *
 * @param range The range of `ULong` values to ensure the current value is not within.
 * @param property The property associated with this validation, or `null` if not applicable.
 * @param variableName An optional name of the variable being validated, or `null` if not applicable.
 * @param message An optional message describing the validation failure, or `null` for a default message.
 * @param causeOf A transformer that generates a throwable cause for the validation exception based on the current value, or `null` if not applicable.
 * @param cause A transformer that generates a throwable cause for the validation exception based on the current value, or `null` if not applicable.
 *
 * @return The current `ULong` value if it is not within the specified range.
 *
 * @throws NumberOutOfRangeException if the current `ULong` value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRange, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value does not fall within the specified `range`.
 * If the value is within the range, a `NumberOutOfRangeException` is thrown.
 *
 * @param range the range of `ULong` values that the current value must not belong to
 * @param property the primary `KProperty` associated with the validation, providing metadata context, or null if not applicable
 * @param variable an optional secondary `KProperty` providing additional context for the variable being validated, or null if not applicable
 * @param message an optional custom error message to include in the exception if validation fails
 * @param causeOf a transformer that provides a specific `Throwable` (based on the `ULong` value) to use as the cause for the exception, or null if not applicable
 * @param cause an alternative transformer that provides a `Throwable` (based on the `ULong` value) to include as the root cause, or null if not applicable
 * @return the same `ULong` value if validation passes
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRange, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value is not within the specified range. Throws a validation exception
 * if the value falls within the range.
 *
 * @param range The range of `ULong` values to check against.
 * @param callable The Kotlin function (`KFunction`) associated with the validation. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message to include in the exception upon validation failure. Defaults to null.
 * @param causeOf A transformer that generates a `Throwable` to be used as the cause in the exception. Can be null.
 * @param cause A transformer that generates a `Throwable` to be used as the root cause of the exception. Can be null.
 * @return The validated `ULong` value if it is not within the specified range.
 * @throws NumberOutOfRangeException If the `ULong` value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRange, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [ULong] is not within the specified [range].
 * If the value is within the range, a [NumberOutOfRangeException] is thrown.
 *
 * @param range the range of [ULong] values to validate against
 * @param callable the [KFunction] associated with the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional custom error message to be used in the exception if validation fails
 * @param causeOf an optional transformer function that generates the throwable cause based on the invalid value
 * @param cause an optional transformer function that generates a throwable based on the invalid value for additional context
 * @return the current [ULong] if it is not within the specified range
 * @throws NumberOutOfRangeException if the current [ULong] is within the specified [range]
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRange, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value is not within the specified range.
 * If the value is found within the range, a `NumberOutOfRangeException` is thrown.
 *
 * @param range the range of `ULong` values to check against
 * @param callableName the name of the callable (e.g., function) that invoked the validation
 * @param parameterName the name of the parameter being validated, or `null` if not applicable
 * @param message an optional custom message to include in the exception, or `null` for a default message
 * @param causeOf an optional transformer function to generate the exception cause if validation fails
 * @param cause an optional transformer function to specify an additional underlying cause for the exception
 * @return the validated `ULong` value if it does not fall within the given range
 * @throws NumberOutOfRangeException if the value is within the specified range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRange, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the `ULong` value is not within the specified `range`. If the value is within the range,
 * throws a `NumberOutOfRangeException` with the provided details.
 *
 * @param range The range of `ULong` values to check against.
 * @param callableName The name of the callable (e.g., function or property) where validation is performed, or null if not specified.
 * @param parameter The `KParameter` representing the parameter related to this validation, or null if not applicable.
 * @param message An optional error message providing additional details about the validation failure, or null.
 * @param causeOf A transformer function to generate a `Throwable` based on the `ULong` value, or null if not used.
 * @param cause A transformer function to generate the underlying cause of the exception based on the `ULong` value, or null if not used.
 *
 * @return The original `ULong` value if it is not within the specified range.
 *
 * @throws NumberOutOfRangeException If the `ULong` value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRange, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current unsigned long value is not within the specified range including exclusions.
 * If the value is within the range, a validation exception is thrown.
 *
 * @param range The range, including exclusions, that the value should not be part of.
 * @param causeOf A transformer that generates a custom throwable based on the current value when validation fails.
 * @param cause An optional transformer to provide an additional underlying cause for the exception.
 * @return The current unsigned long value if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithExclusions, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value is not within the specified range, including any excluded values.
 * If the value is within the specified range, an exception is thrown.
 *
 * @param range The range of `ULong` values to check against, potentially including exclusions.
 * @param causeOf An optional transformer that generates a specific exception for the validation failure,
 *                based on the current `ULong` value.
 * @param cause An optional transformer to produce a cause exception, based on the current `ULong` value.
 * @param lazyMessage A transformer that generates a lazy message associated with the validation failure,
 *                    based on the current `ULong` value.
 * @return The original `ULong` value if it is not in the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithExclusions, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null, lazyMessage: Transformer<ULong, Any>): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value is not within the specified range, including any explicitly excluded elements.
 * If the value is within the range, a `NumberOutOfRangeException` is thrown with the provided details.
 *
 * @param range The range with exclusions to check against.
 * @param property Optional `KProperty` representing the property associated with this validation.
 * @param variableName Optional name of the variable being validated. Defaults to `null`.
 * @param message Optional custom message to include in the exception if validation fails. Defaults to `null`.
 * @param causeOf Optional transformer function to generate a more specific `Throwable` cause when validation fails.
 * @param cause Optional transformer function to generate a supplemental cause for the thrown exception.
 * @return The current `ULong` value if it passed the validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithExclusions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value is not within the specified range, including considering exclusions.
 * If the value is within the range, throws a `NumberOutOfRangeException`.
 *
 * @param range The range and exclusions against which the validation is performed.
 * @param property An optional property associated with the validation context.
 * @param variable An optional variable associated with the validation context.
 * @param message An optional custom error message to use if the validation fails.
 * @param causeOf An optional transformer to create a throwable as the root cause of the exception.
 * @param cause An optional transformer to create a throwable for the validation failure.
 * @return The same `ULong` value, if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithExclusions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value is not within the specified `range`. If the value is found
 * within the range, an exception will be thrown. The exception can be customized using various optional
 * parameters.
 *
 * @param range The range with exclusions where the validation will check if the value belongs.
 * @param callable An optional reference to the function where the validation is being performed.
 * @param parameterName An optional parameter name to provide additional context in the validation exception.
 * @param message An optional custom message to include in the validation exception.
 * @param causeOf An optional transformer used to create the exception to be thrown when validation fails.
 * @param cause An optional transformer used to supply the causal exception for the validation failure.
 * @return The current `ULong` value if it passes the validation.
 * @throws NumberOutOfRangeException if the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithExclusions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value is not within the specified range, including any optional exclusions.
 * Throws a `NumberOutOfRangeException` if the current value satisfies the range condition.
 *
 * @param range The range, including any exclusions, against which the current value is validated.
 * @param callable The callable reference associated with the validation, or `null` if not applicable.
 * @param parameter The parameter reference associated with the validation, or `null` if not applicable.
 * @param message An optional message describing the validation failure. Defaults to a message specifying the range condition.
 * @param causeOf An optional transformation function to create a specific `Throwable` cause based on the current value. Defaults to `null`.
 * @param cause An optional transformation function to generate a cause of the validation failure based on the current value. Defaults to `null`.
 * @return The current `ULong` value if it does not satisfy the range condition.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithExclusions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [ULong] value is not within the specified [range], including any exclusions defined
 * within the range. If the value is within the range, a validation exception is thrown.
 *
 * @param range The range of [ULong] values, with optional exclusions, to check against.
 * @param callableName The name of the callable method or function associated with this validation. Can be null.
 * @param parameterName The name of the parameter being validated, if applicable. Can be null.
 * @param message A custom message to describe the validation failure. If null, a default message is used.
 * @param causeOf A transformer to produce a specific cause of the exception when a validation failure occurs. Can be null.
 * @param cause An additional transformer providing an exception cause when a validation failure occurs. Can be null.
 * @return The current [ULong] value if validation succeeds.
 * @throws NumberOutOfRangeException If the value is found within the specified range, including any exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithExclusions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current [ULong] is not within the specified range, throwing an exception if the validation fails.
 *
 * @param range The range with optional exclusions against which the [ULong] is validated.
 * @param callableName The name of the callable (function or property) for context in the validation error, or null.
 * @param parameter The parameter being validated for context in the validation error, or null.
 * @param message Additional details for the validation error message, or null. Defaults to a standard message.
 * @param causeOf A transformer that generates the cause exception if validation fails, or null.
 * @param cause An alternative transformer to generate the cause exception if validation fails, or null.
 * @return The current [ULong] if validation passes.
 * @throws NumberOutOfRangeException If the current [ULong] is found within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithExclusions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current [ULong] value is not within the specified [range].
 * If the value is within the range, a [NumberOutOfRangeException] is thrown.
 *
 * @param range The range of [ULong] values to compare against.
 * @param causeOf A transformer that generates a custom [Throwable] based on the value for the exception cause. Optional.
 * @param cause A transformer that generates a custom [Throwable] based on the value for the inner exception. Optional.
 * @return The original value if it is not within the specified range.
 * @throws NumberOutOfRangeException If the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithConditions, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current unsigned long value is not within the specified range with conditions.
 * If the value is within the range, a validation exception is thrown.
 *
 * @param range The range with conditions to validate against.
 * @param causeOf An optional transformer to generate a specific throwable cause for the validation failure based on the input value.
 * @param cause An optional transformer to generate an additional throwable cause for the validation failure based on the input value.
 * @param lazyMessage A transformer that generates the message for the validation exception lazily based on the input value.
 * @return The current unsigned long value if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithConditions, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null, lazyMessage: Transformer<ULong, Any>): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the current [ULong] value is not in the specified range defined by [range].
 * If the value is within the range, an exception is thrown.
 *
 * @param range The range against which the current value is validated.
 * @param property An optional property reference that can be used in exception messages.
 * @param variableName An optional variable name to include in the exception message for identification purposes.
 * @param message An optional custom message to use in the exception if validation fails.
 * @param causeOf An optional transformer function to generate a more specific cause of the exception.
 * @param cause An optional transformer function to provide a secondary cause of the exception.
 * @return The current value if it is not in the specified range.
 * @throws NumberOutOfRangeException If the current value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithConditions, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value is not within the specified range.
 * If the value is found within the range, an appropriate exception is thrown.
 *
 * @param range The range against which the current value is validated.
 * @param property The property associated with the value being validated, if applicable.
 * @param variable The variable associated with the value being validated, if applicable.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer to generate a custom exception based on the current value.
 * @param cause An optional transformer to generate the root cause of the validation exception.
 * @return The validated `ULong` value if it does not fall within the specified range.
 * @throws NumberOutOfRangeException If the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithConditions, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given unsigned long value is not within the specified range.
 * If the value falls within the range, an exception is thrown.
 *
 * @param range The range of unsigned long values to validate against.
 * @param callable The function in which the validation is being performed, may be null.
 * @param parameterName The name of the parameter being validated, may be null.
 * @param message An optional error message to be used in the exception, may be null.
 * @param causeOf A transformer function to generate a cause of type Throwable when the validation fails, may be null.
 * @param cause An alternative transformer function to generate a cause of type Throwable when the validation fails, may be null.
 * @return The validated unsigned long value if it is not within the specified range.
 * @throws NumberOutOfRangeException if the value is within the specified range, with details of the failure.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithConditions, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling ULong value is not within the specified range. If the value is within the range,
 * a validation exception is thrown.
 *
 * @param range The range of `ULong` values to check against, along with any additional conditions.
 * @param callable The callable function from which the validation is triggered, used for exception context.
 * @param parameter The specific parameter being validated, used for exception context.
 * @param message An optional custom error message to be included in the exception if the validation fails.
 * @param causeOf An optional transformer for generating a throwable based on the current `ULong` value,
 *                which serves as the primary cause when a validation exception is thrown.
 * @param cause An optional transformer for generating a supplemental throwable based on the current `ULong`
 *              value, added to the exception chain as a secondary cause.
 * @return The same `ULong` value if it passes validation (i.e., it is not within the specified range).
 * @throws NumberOutOfRangeException If the `ULong` value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithConditions, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` is not within the specified range. If the value exists in the range,
 * a `NumberOutOfRangeException` is thrown with an optional additional cause or customized error message.
 *
 * @param range The range of `ULong` values to check against. Validation will fail if the current value is within this range.
 * @param callableName The name of the function or callable that is performing the validation. This is used for error reporting.
 * @param parameterName An optional name of the parameter being validated. This is used for error reporting.
 * @param message An optional custom message to include when the validation fails.
 * @param causeOf An optional transformer that generates a `Throwable` as the cause of the validation failure.
 * @param cause An optional transformer that generates a `Throwable` to be included as a secondary cause of the failure.
 * @return The validated `ULong` if it is not within the specified range.
 * @throws NumberOutOfRangeException if the validation fails because the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithConditions, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `ULong` value is not within the specified range. If the value is found in the range, an exception
 * is thrown with the provided details.
 *
 * @param range The range of `ULong` values to validate against.
 * @param callableName The name of the calling function or method, if applicable (nullable).
 * @param parameter The `KParameter` associated with this validation, if applicable (nullable).
 * @param message The custom validation failure message (nullable). Defaults to a message indicating the value is within the specified range.
 * @param causeOf A transformer function to generate a specific cause exception when the validation fails (nullable).
 * @param cause A transformer function to generate an optional chained cause exception (nullable).
 * @return The current `ULong` value if it is not within the specified range.
 * @throws NumberOutOfRangeException If the current `ULong` value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULong.validateNotIn(range: ULongRangeWithConditions, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<ULong, Throwable>? = null, cause: Transformer<ULong, Throwable>? = null): ULong {
    if (this in range) throw if (causeOf == null) NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(NumberOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}

/**
 * Ensures that the current numeric value is zero. If the value is not zero, an exception is thrown.
 *
 * @param causeOf A transformer function that generates a specific exception based on the value,
 *                or `null` if a default exception should be used.
 * @param cause A transformer function that produces an underlying cause for the exception,
 *              or `null` if no specific cause is necessary.
 * @return The current instance if the value is zero.
 * @throws ExpectationMismatchException if the value is not zero and `causeOf` is `null`.
 *         If `causeOf` is not `null`, the exception thrown is the result of the `causeOf` function.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectZero(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotZero) throw if (causeOf == null) ExpectationMismatchException("Value is not zero.", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException("Value is not zero.", cause?.invoke(this)))
    return this
}
/**
 * Verifies that the number is zero. If the number is not zero, an exception is thrown.
 *
 * @param causeOf An optional transformer that generates a throwable to be thrown. If not provided, a default exception is thrown.
 * @param cause An optional transformer that generates a root cause throwable to be associated with the default or provided exception.
 * @param lazyMessage A transformer that provides a lazy-generated error message when the number is not zero.
 * @return The original number if it is zero.
 * @throws ExpectationMismatchException if the number is not zero and `causeOf` is not provided. Can include an additional cause if `cause` is supplied.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectZero(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isNotZero) throw if (causeOf == null) ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the numeric value is zero. If the value is not zero, throws an `ExpectationMismatchException`.
 *
 * @param property an optional `KProperty` instance representing the associated property, or null if not applicable
 * @param variableName an optional variable name to include in the error message, or null if not applicable
 * @param message an optional custom error message to include in the exception, or null to use the default message
 * @param causeOf an optional `Transformer` to produce the cause exception, or null if not used
 * @param cause an optional `Transformer` to create a secondary cause exception, or null if not used
 * @return the original numeric value if it is zero
 * @throws ExpectationMismatchException if the numeric value is not zero
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectZero(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotZero) throw if (causeOf == null) ExpectationMismatchException(property, variableName, message ?: "is not zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variableName, message ?: "is not zero", cause?.invoke(this)))
    return this
}
/**
 * Verifies that the numeric value is zero. If the value is not zero, an exception is thrown.
 *
 * @param property The primary property being evaluated. Can be null.
 * @param variable An optional variable that may be associated with the evaluation. Can be null.
 * @param message An optional message to be included in the exception if the verification fails. Defaults to null.
 * @param causeOf A transformer used to generate a specific throwable from the value when the evaluation fails. Can be null.
 * @param cause A transformer used to produce the root cause throwable from the value when the evaluation fails. Can be null.
 * @return The numeric value if the verification succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectZero(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotZero) throw if (causeOf == null) ExpectationMismatchException(property, variable, message ?: "is not zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variable, message ?: "is not zero", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is zero. If the number is not zero, an exception is thrown.
 *
 * @param callable The callable function whose context is used for validation, or null if unspecified.
 * @param parameterName The name of the parameter being validated, or null if unspecified.
 * @param message An optional custom message to describe the validation failure, or null to use a default message.
 * @param causeOf A transformer function to generate a custom exception, or null to use the default exception type.
 * @param cause A transformer function to provide the underlying cause of the validation failure, or null if unspecified.
 * @return The current number if validation passes.
 * @throws ExpectationMismatchException if the current number is not zero.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectZero(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotZero) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, message ?: "is not zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameterName, message ?: "is not zero", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the invoking numeric value is zero. If the value is not zero, an `ExpectationMismatchException` is thrown.
 *
 * @param callable The function in context, or null if not applicable.
 * @param parameter The parameter of the function being validated, or null if not applicable.
 * @param message An optional message to include in the exception if the validation fails, or null.
 * @param causeOf A transformer function to generate the primary `Throwable` to throw if the validation fails, or null.
 * @param cause An optional transformer function to generate a cause exception to attach to the `ExpectationMismatchException`, or null.
 * @return The original numeric value if it is zero.
 * @throws ExpectationMismatchException If the numeric value is not zero.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectZero(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotZero) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, message ?: "is not zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameter, message ?: "is not zero", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the calling number is equal to zero. If the number is not zero, an exception is thrown.
 *
 * @param callableName The name of the callable function that triggered this check, or null if unspecified.
 * @param parameterName The name of the parameter being validated, or null if unspecified.
 * @param message An optional custom error message to include in the exception if the check fails, or null to use the default message.
 * @param causeOf A transformer function that generates a throwable representing the specific cause of failure for this check,
 *                or null if not applicable.
 * @param cause A transformer function that generates a throwable as the underlying cause of the exception, or null if not applicable.
 * @return The original number if the validation succeeds.
 * @throws ExpectationMismatchException If the number is not zero.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectZero(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotZero) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, message ?: "is not zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameterName, message ?: "is not zero", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the current numeric value is zero. If it is not zero, an exception is thrown.
 *
 * @param callableName The name of the function or callable associated with this check, or null.
 * @param parameter The parameter being evaluated, or null.
 * @param message An optional custom message to include in the exception if the value is not zero, or null.
 * @param causeOf A transformer responsible for creating a throwable based on the current value, or null.
 * @param cause A transformer responsible for creating a throwable to include as a cause in the exception, or null.
 * @return The current numeric value if it is zero.
 * @throws ExpectationMismatchException If the numeric value is not zero.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectZero(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isNotZero) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, message ?: "is not zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameter, message ?: "is not zero", cause?.invoke(this)))
    return this
}

/**
 * Ensures that the value is not zero. If the value is zero, throws an `ExpectationMismatchException`.
 *
 * @param causeOf A transformer that provides a specific `Throwable` to throw based on the current value. Defaults to `null`.
 * @param cause A transformer that provides a cause `Throwable` to be linked to the generated exception. Defaults to `null`.
 * @return The original value if it is not zero.
 * @throws ExpectationMismatchException if the value is zero.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectNotZero(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isZero) throw if (causeOf == null) ExpectationMismatchException("Value is zero.", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException("Value is zero.", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the number is not zero. If the number is zero, an exception is thrown
 * based on the provided transformers for the cause and the exception message.
 *
 * @param causeOf A transformer that generates a throwable to be used as the primary cause of the exception.
 *                If `null`, a default exception is created.
 * @param cause A transformer that generates a throwable to be added as a secondary cause of the exception.
 *              If `null`, no secondary cause is added.
 * @param lazyMessage A transformer that produces the message for the exception when the number is zero.
 * @return The current number if it is not zero.
 * @throws ExpectationMismatchException if the number is zero, with the message and cause created using the provided transformers.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectNotZero(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (isZero) throw if (causeOf == null) ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is not zero, throwing an exception if the validation fails.
 *
 * @param property The [KProperty] associated with the number, used for more descriptive exception messages. Can be null if not applicable.
 * @param variableName An optional name of the variable to include in the exception message, providing additional context.
 * @param message A custom error message to include in the exception if the number is zero. Defaults to "is zero" if null.
 * @param causeOf A transformer function to provide a custom exception from the number if it is zero. If null, a default exception is used.
 * @param cause A transformer function to determine the cause of the exception from the number if it is zero. Can be null.
 * @return The same number if it is not zero.
 * @throws ExpectationMismatchException if the number is zero.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectNotZero(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isZero) throw if (causeOf == null) ExpectationMismatchException(property, variableName, message ?: "is zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variableName, message ?: "is zero", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the number is not zero. If the value is zero, an `ExpectationMismatchException`
 * is thrown describing the violation.
 *
 * @param property The primary property being evaluated. Can be null.
 * @param variable An optional variable associated with the expectation. Can be null.
 * @param message An optional custom exception message. If null, a default message is used.
 * @param causeOf A transformer function that takes the current value and returns the cause of
 *                the exception to be thrown. Can be null.
 * @param cause A transformer function that takes the current value and produces an underlying
 *              cause for the exception. Can be null.
 * @return The number itself if it is not zero.
 * @throws ExpectationMismatchException If the number is zero, providing details about the mismatch.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectNotZero(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isZero) throw if (causeOf == null) ExpectationMismatchException(property, variable, message ?: "is zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variable, message ?: "is zero", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current number is not zero. If the validation fails, an exception is thrown.
 *
 * @param callable The callable function associated with the validation context, or null if unspecified.
 * @param parameterName The name of the parameter being validated, or null if unspecified.
 * @param message An optional custom message to be included in the exception, or null for the default message.
 * @param causeOf An optional transformer to generate a custom exception based on the current number, or null if not required.
 * @param cause An optional transformer to generate the cause of the exception based on the current number, or null if not required.
 * @return The current number if it passes the validation.
 * @throws ExpectationMismatchException if the number is zero.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectNotZero(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isZero) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, message ?: "is zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameterName, message ?: "not zero", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the invoking number is not zero. If the value is zero, an exception is thrown.
 *
 * @param callable The function being evaluated, or `null` if not applicable.
 * @param parameter The parameter of the function being evaluated, or `null` if not applicable.
 * @param message An optional custom message to include in the exception if the value is zero.
 * @param causeOf An optional transformer for creating a custom exception based on the invoking value if it is zero.
 * @param cause An optional transformer for creating a cause exception to be included in the thrown exception if the value is zero.
 * @return The invoking number, ensuring it is not zero.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectNotZero(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isZero) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, message ?: "is zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameter, message ?: "is zero", cause?.invoke(this)))
    return this
}
/**
 * Verifies that the current number is not zero. If the number is zero, an exception is thrown.
 *
 * @param callableName The name of the callable (e.g., a function or method) associated with this check, or null if unspecified.
 * @param parameterName The name of the parameter involved in the check, or null if unspecified.
 * @param message The custom message to include in the exception if the check fails, or null to use the default message.
 * @param causeOf A transformer that generates the cause exception dynamically from the current number, or null if no specific cause is to be generated.
 * @param cause A transformer that generates a general cause exception dynamically from the current number, or null if no specific cause is to be generated.
 * @return The current number if it is not zero.
 * @throws ExpectationMismatchException If the current number is zero.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectNotZero(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isZero) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, message ?: "is zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameterName, message ?: "is zero", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the invoking numeric value is not equal to zero. If the value is zero, an
 * exception is thrown with detailed contextual information.
 *
 * @param callableName The name of the callable (function or context) being evaluated, or null.
 * @param parameter The parameter of the callable being evaluated, or null.
 * @param message Optional custom error message, or null. Defaults to a standard "is zero" message.
 * @param causeOf An optional transformer that generates the root cause exception from the current value, or null.
 * @param cause An optional transformer that generates a supplementary cause exception from the current value, or null.
 * @return The numeric value itself if it is not zero.
 * @throws ExpectationMismatchException if the numeric value is zero.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Number> T.expectNotZero(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (isZero) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, message ?: "is zero", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameter, message ?: "is zero", cause?.invoke(this)))
    return this
}