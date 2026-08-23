/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 * @since 5.0.0
 */

@file:JvmName("RangeValidatorsKt")
@file:Suppress("unused")
@file:Since("5.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.range.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

/**
 * Validates whether the specified number is contained within the current range.
 * If the number is not within the range, an exception is thrown.
 *
 * @param number the number to validate against the range.
 * @param causeOf an optional transformer that generates a throwable cause based on the range if the validation fails.
 * @param cause an optional transformer that generates a throwable cause based on the range if the validation fails.
 * @return the current range if the validation is successful.
 * @throws ValidationFailedException if the number is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateContains(number: Int, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within this [IntRange].
 * If the number is not within the range, a [ValidationFailedException] is thrown.
 *
 * @param number The number to validate against this [IntRange].
 * @param causeOf An optional transformer that provides a throwable cause if the validation fails.
 * @param cause An optional transformer that provides an alternative throwable cause if the validation fails.
 * @param lazyMessage A transformer that generates the message for the [ValidationFailedException] upon validation failure.
 * @return The original [IntRange] if validation is successful.
 * @throws ValidationFailedException If the given number is not within this [IntRange].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateContains(number: Int, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null, lazyMessage: Transformer<IntRange, Any>): IntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is contained within the `IntRange`. If the number is not within the range,
 * a `ValidationFailedException` is thrown with a customizable error message and optional cause transformers.
 *
 * @param number The number to check if it is within the `IntRange`.
 * @param property The Kotlin property associated with the validation, used to enrich the exception message. Nullable.
 * @param variableName An optional name of the variable being validated, included in the exception message if provided.
 * @param message An optional custom error message. Defaults to "doesn't contain <number>" if not provided.
 * @param causeOf An optional transformer function that generates a cause exception when the validation fails.
 * @param cause An optional transformer function that generates a cause exception to be attached to the `ValidationFailedException`.
 * @return The original `IntRange` if the validation succeeds.
 * @throws ValidationFailedException If the number is not contained within the `IntRange`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateContains(number: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within the current [IntRange].
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param number the integer to check for containment within the range
 * @param property the primary property associated with the validation, or null if not specified
 * @param variable an optional secondary property to provide additional context, or null if not specified
 * @param message an optional custom error message to describe the validation failure
 * @param causeOf an optional transformer to generate the cause of the validation failure, or null if not specified
 * @param cause an optional transformer to generate additional context for the validation failure, or null if not specified
 * @return the same [IntRange] if the validation succeeds
 * @throws ValidationFailedException if the number is not contained within the range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateContains(number: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is within the bounds of the IntRange. If the number is not contained
 * within the range, an exception is thrown.
 *
 * @param number The number to validate against this range.
 * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can be null.
 * @param parameterName The name of the parameter in the callable associated with the validation. Can be null.
 * @param message An optional custom message for the exception if validation fails. Can be null.
 * @param causeOf An optional transformer to generate the root cause of the validation failure. Can be null.
 * @param cause An optional transformer to provide additional context or cause for the validation failure. Can be null.
 * @return This `IntRange` instance if validation succeeds.
 * @throws ValidationFailedException if the number is not contained within the range, with an optional custom
 * message and cause determined by the provided transformers.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateContains(number: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the specified number is within the current [IntRange]. If not, throws a [ValidationFailedException].
 *
 * @param number The number to be validated against the range.
 * @param callable An optional [KFunction] that provides context for the validation failure.
 * @param parameter An optional [KParameter] representing the parameter associated with the validation failure.
 * @param message An optional custom message for the exception if validation fails.
 * @param causeOf An optional transformer to generate a [Throwable] from the current range if validation fails.
 * @param cause An optional transformer to generate a secondary cause [Throwable] from the current range if validation fails.
 * @return The current [IntRange] if validation succeeds.
 * @throws ValidationFailedException if the number is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateContains(number: Int, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether a given number is contained within the invoked [IntRange].
 * If the number is not within the range, throws a [ValidationFailedException].
 *
 * @param number The number to check for containment within the range.
 * @param callableName The name of the callable associated with the validation context,
 *                     used for debugging or error reporting purposes.
 * @param parameterName The name of the parameter being validated; can be null if not applicable.
 * @param message An optional custom message to provide more context in case of validation failure.
 * @param causeOf A transformer that generates the cause for the exception, based on the range, if applicable.
 * @param cause A transformer that can generate a root cause exception based on the range, if applicable.
 * @return The [IntRange] instance itself if the validation passes successfully (i.e., the number is in the range).
 * @throws ValidationFailedException If the number is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateContains(number: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified integer is within the current range.
 * If the integer is not in the range, throws a [ValidationFailedException].
 *
 * @param number The integer to validate against the range.
 * @param callableName The name of the callable (e.g., function or property) where validation is performed. Can be null.
 * @param parameter The [KParameter] instance representing the parameter related to the validation failure. Can be null.
 * @param message An optional custom error message for the validation exception. Defaults to "doesn't contain {number}" if null.
 * @param causeOf An optional transformer to generate the root cause of the exception. Can be null.
 * @param cause An optional transformer to generate a secondary cause of the exception. Can be null.
 * @return The current [IntRange] if validation succeeds.
 * @throws ValidationFailedException If the specified integer is not part of the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateContains(number: Int, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given number is within the range, considering any exclusions. If the number does not
 * meet the criteria, an exception is thrown.
 *
 * @param number The number to validate against the range.
 * @param causeOf An optional transformer used to produce a throwable cause if the validation fails.
 * @param cause An optional transformer used to provide additional context or a throwable cause.
 * @return The current instance of [IntRangeWithExclusions] if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateContains(number: Int, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number falls within the `IntRangeWithExclusions`.
 * If the number is not within the range, a validation exception is thrown.
 *
 * @param number The integer value to validate against the range.
 * @param causeOf A transformer that generates a throwable instance representing the cause of the validation failure.
 * @param cause A transformer that provides additional context for the throwable cause.
 * @param lazyMessage A transformer function that generates the error message for the validation exception.
 * @return The same `IntRangeWithExclusions` instance if the validation passes.
 * @throws ValidationFailedException If the provided number is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateContains(number: Int, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null, lazyMessage: Transformer<IntRangeWithExclusions, Any>): IntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within the range of this `IntRangeWithExclusions`.
 * If the number is not contained, a `ValidationFailedException` is thrown.
 *
 * @param number The number to validate against this range with exclusions.
 * @param property The associated property being validated, if applicable. Can be null.
 * @param variableName The variable name used in the validation context, if applicable. Can be null.
 * @param message An optional custom error message to associate with the validation exception. Can be null.
 * @param causeOf An optional transformer to generate a custom cause when validation fails. Can be null.
 * @param cause An optional transformer to generate a nested cause in the failed validation exception. Can be null.
 * @return The current instance of `IntRangeWithExclusions` if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateContains(number: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within the `IntRangeWithExclusions` instance.
 * If the number is not within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to be checked for inclusion in the range.
 * @param property The primary property being validated. Can be `null`.
 * @param variable An additional variable related to the validation. Can be `null`.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer for generating a specific cause for the exception.
 * @param cause An additional optional transformer for generating a specific cause for the exception.
 * @return The original `IntRangeWithExclusions` instance if validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateContains(number: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether a given number is contained within this `IntRangeWithExclusions`.
 * If the number is not contained within the range, an exception is thrown.
 *
 * @param number The number to check for containment within the range.
 * @param callable The callable function associated with this validation, if applicable.
 * @param parameterName The name of the parameter being validated, if applicable.
 * @param message The custom message to include in the exception, if the validation fails.
 * @param causeOf A transformer that provides the cause of the validation failure.
 * @param cause A transformer that provides the underlying cause exception.
 * @return The original `IntRangeWithExclusions` instance if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateContains(number: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is within the range, respecting exclusions,
 * and throws a `ValidationFailedException` if the validation fails.
 *
 * @param number The number to validate against the range with exclusions.
 * @param callable The callable function being validated, used for exception context.
 * @param parameter The parameter being validated, used for exception context. Defaults to `null`.
 * @param message Optional custom message to include in the exception. Defaults to `null`.
 * @param causeOf A transformer function used to determine the primary cause if validation fails. Defaults to `null`.
 * @param cause A transformer function to generate a causal exception if validation fails. Defaults to `null`.
 * @return The original `IntRangeWithExclusions` object if validation succeeds.
 * @throws ValidationFailedException If the number is not within the range or within its exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateContains(number: Int, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within this `IntRangeWithExclusions` instance.
 * If the number is not contained, a `ValidationFailedException` is thrown.
 *
 * @param number The number to check for containment.
 * @param callableName The name of the callable invoking this validation, used for error context, or null if not provided.
 * @param parameterName The name of the parameter being validated, or null if not provided.
 * @param message An optional message for the validation failure, defaulting to "doesn't contain {number}" if null.
 * @param causeOf A transformer for creating the cause of the validation failure if provided, or null if not applicable.
 * @param cause A transformer for generating the root cause of the validation failure, or null if not provided.
 * @return The current `IntRangeWithExclusions` instance if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateContains(number: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within this `IntRangeWithExclusions`.
 * Throws a `ValidationFailedException` if the number is not contained within the range.
 *
 * @param number The number to check for containment within the range.
 * @param callableName Optional name of the callable associated with this validation.
 * @param parameter Optional parameter involved in the validation.
 * @param message Optional custom message to include in the exception if validation fails.
 * @param causeOf Optional transformer to generate the root cause of the exception if validation fails.
 * @param cause Optional transformer to generate the direct cause of the exception if validation fails.
 * @return This `IntRangeWithExclusions` instance if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateContains(number: Int, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is within the range specified by the `IntRangeWithConditions`.
 * If the number is not within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to validate against the range.
 * @param causeOf Optional transformer to generate a custom throwable if the validation fails.
 * @param cause Optional transformer to provide a related cause for the validation failure.
 * @return The same `IntRangeWithConditions` instance if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateContains(number: Int, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the current range.
 * If the number is not within the range, a validation exception is thrown.
 *
 * @param number The number to check for containment within the range.
 * @param causeOf An optional transformer that provides a throwable cause for the exception, if validation fails.
 * @param cause An optional transformer that provides an additional throwable cause for the exception, if validation fails.
 * @param lazyMessage A transformer function used to generate the error message when validation fails.
 * @return The current range, if validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateContains(number: Int, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null, lazyMessage: Transformer<IntRangeWithConditions, Any>): IntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the current range. If the number is not within the range,
 * an exception is thrown with an optional custom message and cause.
 *
 * @param number The number to check for containment within this range.
 * @param property The optional property to associate with the validation.
 * @param variableName The optional name of the variable being checked.
 * @param message The optional custom error message to use if validation fails. Defaults to a generic message.
 * @param causeOf The optional transformer to generate a custom exception cause if validation fails.
 * @param cause The optional transformer to provide an additional cause if validation fails.
 * @return The current range to allow method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateContains(number: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within the current IntRangeWithConditions.
 * If the number is not within the range, a ValidationFailedException is thrown.
 *
 * @param number The number to validate if it is contained within the range.
 * @param property The KProperty representing the property being validated (optional).
 * @param variable An optional KProperty representing the associated variable.
 * @param message An optional custom validation failure message.
 * @param causeOf An optional transformer to handle the cause of the exception if validation fails.
 * @param cause An optional transformer to provide an additional cause for the validation exception.
 * @return The current IntRangeWithConditions if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateContains(number: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given number is within the range defined by the `IntRangeWithConditions` instance.
 * If the number is not within the range, it throws a `ValidationFailedException`.
 *
 * @param number The number to check for inclusion in the range.
 * @param callable The optional Kotlin function reference to associate with the validation context.
 * @param parameterName The optional name of the parameter being validated.
 * @param message The optional custom message to use in case the validation fails.
 * @param causeOf An optional transformer to generate a throwable cause if the validation fails.
 * @param cause An optional transformer to generate an additional throwable cause if the validation fails.
 * @return The same `IntRangeWithConditions` instance for fluent chaining after validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateContains(number: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the range defined by this IntRangeWithConditions instance.
 * If the number is not within the range, a ValidationFailedException is thrown.
 *
 * @param number The number to check for containment within the range.
 * @param callable The callable function where the validation is being performed (optional).
 * @param parameter The specific parameter being validated within the callable (optional).
 * @param message A custom validation error message to use if the validation fails (optional).
 * @param causeOf A transformer to generate the root cause of the error (optional).
 * @param cause A transformer to define the additional cause of the validation failure (optional).
 * @return The current IntRangeWithConditions instance if the validation is successful.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateContains(number: Int, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether a given number is contained within the current `IntRangeWithConditions`.
 * If the number is not within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to validate presence within the range.
 * @param callableName The name of the callable associated with the validation, if any.
 * @param parameterName The name of the parameter being validated, if any. Defaults to `null`.
 * @param message An optional custom error message for the validation failure. Defaults to `null`.
 * @param causeOf A transformer function that may provide a throwable cause of the validation failure. Defaults to `null`.
 * @param cause A transformer function to generate a throwable cause of the validation failure, if any. Defaults to `null`.
 * @return The `IntRangeWithConditions` instance on which the validation was performed.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateContains(number: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given integer is contained within the range represented by this instance.
 * If the number is not in the range, a validation exception is thrown.
 *
 * @param number The integer to validate for containment within this range.
 * @param callableName An optional name of the callable for error context.
 * @param parameter An optional KParameter for error context.
 * @param message An optional custom error message, or defaults to indicating that the number is not contained.
 * @param causeOf An optional transformer to define the cause of the validation failure as a throwable.
 * @param cause An optional transformer to apply an additional cause to the exception created.
 * @return The current instance of IntRangeWithConditions for method chaining.
 * @throws ValidationFailedException If the given number is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateContains(number: Int, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the range and throws an exception if it is not.
 *
 * @param number The unsigned integer to check for inclusion in the range.
 * @param causeOf A transformer that generates a throwable based on the current range when the validation fails, or `null` if no specific cause is provided.
 * @param cause A transformer that generates a throwable based on the current range to include as a cause for the validation failure, or `null` if no cause is required.
 * @return The original range if the validation passes.
 * @throws ValidationFailedException If the specified number is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateContains(number: UInt, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is within the range and throws an exception if the number is not contained.
 *
 * @param number The number to check for containment within this range.
 * @param causeOf Optional transformer to provide a custom exception when the validation fails.
 * @param cause Optional transformer for specifying the cause of the error when validation fails.
 * @param lazyMessage A transformer to generate a lazy evaluation message for the validation failure.
 * @return The original range if the validation passes.
 * @throws ValidationFailedException If the number is not contained within this range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateContains(number: UInt, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null, lazyMessage: Transformer<UIntRange, Any>): UIntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified unsigned integer [number] is within the bounds of the [UIntRange] on which this
 * method is invoked. If the [number] is not within the range, a [ValidationFailedException] is thrown.
 *
 * @param number The unsigned integer to validate, ensuring it falls within the [UIntRange].
 * @param property An optional [KProperty] associated with the validation. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Used to provide context in the error message. Defaults to null.
 * @param message A custom error message to use if the validation fails. If null, a default message is generated. Defaults to null.
 * @param causeOf A transformer function for creating an optional cause [Throwable] when the validation fails. Can be null.
 * @param cause A transformer function for generating an additional [Throwable] to associate as the root cause of the validation failure. Can be null.
 * @return The [UIntRange] on which the method was invoked, for fluent method chaining.
 * @throws ValidationFailedException If the [number] is not contained within the [UIntRange].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateContains(number: UInt, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within this UIntRange.
 * If the number is not within the range, a ValidationFailedException is thrown.
 *
 * @param number the number to validate against the range
 * @param property the main property associated with the validation, or null if not specified
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional custom message describing the validation failure, or null for a default message
 * @param causeOf an optional transformer that constructs the exception to throw based on the current range, or null if not provided
 * @param cause an optional transformer for appending an underlying cause to the exception, or null if not specified
 * @return the original UIntRange if the validation passes
 * @throws ValidationFailedException if the specified number is not within the range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateContains(number: UInt, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the specified `number` is within the bounds of the current `UIntRange`.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param number The unsigned integer value to be checked for containment within the `UIntRange`.
 * @param callable The Kotlin function (`KFunction`) related to this validation. Can be null.
 * @param parameterName The name of the parameter in the provided callable that caused the validation issue. Can be null.
 * @param message An optional custom error message to be displayed in case of validation failure. Can be null.
 * @param causeOf An optional transformer function to generate a `Throwable` cause based on the `UIntRange`. Can be null.
 * @param cause An optional transformer function to generate a secondary `Throwable` cause based on the `UIntRange`. Can be null.
 * @return The original `UIntRange` if validation is successful.
 * @throws ValidationFailedException if the `number` is not within the bounds of the `UIntRange`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateContains(number: UInt, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the UIntRange. If the number is not within the range,
 * a ValidationFailedException is thrown with an optional message and underlying cause.
 *
 * @param number The `UInt` value to be checked against the range.
 * @param callable The `KFunction` related to the validation context, or `null` if not applicable.
 * @param parameter The `KParameter` representing the parameter involved in the validation, or `null` if not applicable.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to "doesn't contain {number}".
 * @param causeOf A transformer function for generating the root cause of the exception based on the range, or `null` if not applicable.
 * @param cause A transformer function for generating a cause based on the range, or `null` if not applicable.
 * @return The original `UIntRange` if the validation succeeds.
 * @throws ValidationFailedException If the specified number is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateContains(number: UInt, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified `number` is contained within this `UIntRange`.
 * If the `number` is not within the range, a `ValidationFailedException` is thrown with the provided metadata.
 *
 * @param number The `UInt` value to check for containment in this range.
 * @param callableName An optional name of the callable associated with this validation, typically for debugging or logging purposes.
 * @param parameterName An optional name of the parameter related to the validation, typically for debugging or logging purposes.
 * @param message An optional custom message that provides additional context for the validation failure.
 * @param causeOf An optional transformer to generate the root cause for the `ValidationFailedException` based on this range.
 * @param cause An optional transformer to generate the direct cause for the `ValidationFailedException` based on this range.
 * @return This `UIntRange` if the validation passes.
 * @throws ValidationFailedException If the `number` is not contained within this range, with relevant details and causes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateContains(number: UInt, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is within the range of this `UIntRange`, throwing an exception if not.
 *
 * If the `number` is not contained within this range, a `ValidationFailedException` is thrown, which can be
 * optionally customized using additional parameters such as the callable name, parameter details, custom message,
 * or cause transformers.
 *
 * @param number The unsigned integer to be validated against this range.
 * @param callableName The name of the callable (e.g., function or property) triggering the validation, or null if not specified.
 * @param parameter The `KParameter` instance associated with the validation, or null if not applicable.
 * @param message An optional error message to provide additional context when the validation fails, or null for a default message.
 * @param causeOf An optional transformer function to generate a throwable cause when validation fails, or null if not used.
 * @param cause An optional transformer function to generate an additional throwable cause, or null if not used.
 * @return The same `UIntRange` instance if validation is successful.
 * @throws ValidationFailedException If `number` is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateContains(number: UInt, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the range,
 * excluding any defined exclusions. If the number is not within the valid range,
 * a validation exception is thrown.
 *
 * @param number The number to check for containment within the range.
 * @param causeOf An optional transformer that generates a Throwable cause
 *                from the current range if the validation fails.
 * @param cause An optional transformer that generates a Throwable cause
 *              for the validation failure.
 * @return Returns the current instance of `UIntRangeWithExclusions` if validation succeeds.
 * @throws ValidationFailedException If the number is not contained within the valid range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateContains(number: UInt, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the range or its exclusions.
 * If the number is not contained, an exception with a lazy message is thrown.
 *
 * @param number The number to be validated against the range.
 * @param causeOf An optional transformer to generate a throwable cause based on the range
 *                if the validation fails.
 * @param cause An optional transformer to generate a throwable cause based on the range
 *              when used in conjunction with causeOf.
 * @param lazyMessage A transformer used to produce a message when the validation fails.
 * @return The same instance of UIntRangeWithExclusions after validation to allow for chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateContains(number: UInt, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null, lazyMessage: Transformer<UIntRangeWithExclusions, Any>): UIntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is within the range, taking exclusions into account.
 * If the number is not within the range, an exception is thrown.
 *
 * @param number The number to validate against the range.
 * @param property The property associated with the validation, if available.
 * @param variableName The name of the variable being validated, if applicable.
 * @param message Optional custom validation failure message.
 * @param causeOf An optional transformer that determines the cause of the exception if validation fails.
 * @param cause An optional transformer that provides additional context for the exception when validation fails.
 * @return The same instance of the UIntRangeWithExclusions on successful validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateContains(number: UInt, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given number is contained within the range, accounting for exclusions.
 * If the validation fails, it throws a `ValidationFailedException`.
 *
 * @param number The number to validate against the range with exclusions.
 * @param property The property related to the validation (can be null).
 * @param variable The variable related to the validation (can be null).
 * @param message The custom error message to use if validation fails; defaults to a standard message.
 * @param causeOf A transformer for producing the root cause throwable if validation fails.
 * @param cause A transformer for producing a nested cause throwable if validation fails.
 * @return The original instance of `UIntRangeWithExclusions`, allowing for chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateContains(number: UInt, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is contained within the range. If the number is not within the range,
 * a `ValidationFailedException` will be thrown with the given optional arguments providing additional context.
 *
 * @param number The unsigned integer to validate against this range.
 * @param callable The optional Kotlin function reference related to the validation context.
 * @param parameterName The optional parameter name used in the validation context.
 * @param message The optional custom error message to be used in the exception if validation fails.
 * @param causeOf An optional transformer to generate a throwable cause based on this range, providing context for the exception.
 * @param cause An optional transformer to directly specify the throwable cause for when validation fails.
 * @return The current `UIntRangeWithExclusions` instance if the validation passes.
 * @throws ValidationFailedException If the number is not contained in this range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateContains(number: UInt, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given number is within the range defined by `UIntRangeWithExclusions`.
 * If the number is not within the range, an exception is thrown.
 *
 * @param number The unsigned integer to check for its presence in the range.
 * @param callable The function context, which may be used in exception handling or logging.
 * @param parameter An optional parameter reference, primarily used for validation error context.
 * @param message An optional message for the validation exception if the number is not contained in the range.
 * @param causeOf An optional transformer to generate a throwable cause, which could be applied while handling the exception.
 * @param cause An optional transformer for creating a cause throwable that adds more context to the exception.
 * @return Returns the same instance of `UIntRangeWithExclusions` if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateContains(number: UInt, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the `UIntRangeWithExclusions` instance.
 * If the number is not contained, a `ValidationFailedException` is thrown.
 *
 * @param number The unsigned integer to validate against the range.
 * @param callableName The name of the callable function to include in the exception if validation fails.
 * @param parameterName The name of the parameter to include in the exception if validation fails. Optional.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf A transformer to generate a throwable using the current range when validation fails. Optional.
 * @param cause A transformer to provide a root cause throwable for the exception when validation fails. Optional.
 * @return The same instance of `UIntRangeWithExclusions` if validation is successful.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateContains(number: UInt, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is within the current range, throwing an exception if the condition is not met.
 *
 * @param number The number to validate against the range.
 * @param callableName Optional name of the callable function where the validation is performed.
 * @param parameter Optional reflection parameter for additional metadata in validation.
 * @param message Optional message describing the validation failure.
 * @param causeOf Optional transformer to generate the primary cause of the validation failure.
 * @param cause Optional transformer to define an additional nested throwable cause.
 * @return The current instance of `UIntRangeWithExclusions` if the validation is successful.
 * @throws ValidationFailedException if the number is not in the range with provided/excluded values.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateContains(number: UInt, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given number is contained within the range. If the number is not within the range,
 * it throws a validation exception optionally using the provided cause transformers.
 *
 * @param number The number to validate against the current range.
 * @param causeOf An optional transformer to generate a throwable instance when the validation fails.
 * @param cause An optional transformer to generate a throwable instance to be set as the cause of the validation failure.
 * @return The current range instance if the validation passes.
 * @throws ValidationFailedException If the number is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateContains(number: UInt, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the current range.
 * If the number is not in the range, an exception is thrown with the provided lazy message and optional cause.
 *
 * @param number The unsigned integer to validate if it lies within the range.
 * @param causeOf An optional transformer that provides a Throwable to be thrown in case of validation failure.
 * @param cause An optional transformer that provides a cause Throwable to be associated with the primary exception.
 * @param lazyMessage A transformer to lazily generate the error message in case of validation failure.
 * @return The current range instance if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateContains(number: UInt, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null, lazyMessage: Transformer<UIntRangeWithConditions, Any>): UIntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the range represented by this instance.
 * If the number is not contained in the range, it throws a validation exception.
 *
 * @param number The number to check for containment within the range.
 * @param property An optional property reference associated with the validation context.
 * @param variableName An optional name for the variable being validated, used in error messages.
 * @param message An optional custom message to include in the validation exception.
 * @param causeOf An optional transformer to generate a custom exception if validation fails.
 * @param cause An optional transformer to generate a nested cause for the exception.
 * @return The current instance of `UIntRangeWithConditions` if validation passes.
 * @throws ValidationFailedException If the number is not contained in this range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateContains(number: UInt, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is within the range defined by the current instance of `UIntRangeWithConditions`.
 * If the number is not within the range, throws a `ValidationFailedException` with the provided details.
 *
 * @param number The unsigned integer value to validate.
 * @param property An optional property reference associated with the validation failure.
 * @param variable An optional variable reference associated with the validation failure.
 * @param message An optional message describing the validation failure.
 * @param causeOf An optional transformer used to generate the root cause of the failure.
 * @param cause An optional transformer used to generate the secondary cause of the failure.
 * @return The current instance of `UIntRangeWithConditions`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateContains(number: UInt, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the current range.
 * If the number is not within the range, an exception is thrown.
 *
 * @param number The number to check for containment within the range.
 * @param callable The callable function (optional) associated with this validation.
 * @param parameterName The name of the parameter being validated (optional).
 * @param message A custom error message to include in the exception if validation fails (optional).
 * @param causeOf A transformer that produces the root cause of the validation failure (optional).
 * @param cause A transformer that produces additional context or cause information for the validation failure (optional).
 * @return The current range object (UIntRangeWithConditions) if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateContains(number: UInt, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is contained within the range represented by this `UIntRangeWithConditions`.
 * If the number is not in the range, an exception is thrown.
 *
 * @param number The unsigned integer to validate.
 * @param callable The optional function reference associated with this validation.
 * @param parameter The optional parameter information associated with this validation.
 * @param message An optional custom error message for the exception if the number is not within the range.
 * @param causeOf An optional transformer to generate a custom throwable that incorporates the validation failure.
 * @param cause An optional transformer to generate the direct cause of the throwable if validation fails.
 * @return The current instance of `UIntRangeWithConditions` if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateContains(number: UInt, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number exists within the range defined by this `UIntRangeWithConditions`.
 * If the number is not within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The unsigned integer to check for existence within the range.
 * @param callableName The name of the callable where this validation occurs (optional).
 * @param parameterName The name of the parameter being validated (optional).
 * @param message A custom message for the validation failure (optional).
 * @param causeOf A transformation function to generate a throwable for the root cause of the exception (optional).
 * @param cause A transformation function to generate a throwable that represents the secondary cause (optional).
 * @return The current `UIntRangeWithConditions` instance if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateContains(number: UInt, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within the range defined by this `UIntRangeWithConditions` instance.
 * If the number is not within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The `UInt` value to validate against the range.
 * @param callableName The name of the callable being validated, or `null` if not applicable.
 * @param parameter The `KParameter` representing the parameter being validated, or `null` if not applicable.
 * @param message The optional error message to describe the validation failure, or `null` to use a default message.
 * @param causeOf A transformer function that can generate a `Throwable` cause based on the current range, or `null`.
 * @param cause A transformer function that can generate a `Throwable` cause for the exception, or `null`.
 * @return The current `UIntRangeWithConditions` instance, allowing method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateContains(number: UInt, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number exists within the bounds of this `LongRange`.
 * If the number is not within the range, an exception is thrown.
 *
 * @param number The number to validate against the range.
 * @param causeOf An optional transformer to provide a specific throwable to be used
 *                if validation fails. Default is `null`.
 * @param cause An optional transformer to provide an additional cause/context for
 *              the exception if validation fails. Default is `null`.
 * @return The current `LongRange` if the validation is successful.
 * @throws ValidationFailedException If the number is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateContains(number: Long, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the specified number is within the range defined by this `LongRange`.
 * If the number is not within the range, it throws a `ValidationFailedException`.
 *
 * @param number The number to check if it is within this `LongRange`.
 * @param causeOf An optional transformer that generates a throwable to be thrown instead of `ValidationFailedException`.
 * @param cause An optional transformer to generate the underlying cause for the exception.
 * @param lazyMessage A transformer to generate the exception message lazily using the current range.
 * @return The original `LongRange` if validation passes.
 * @throws ValidationFailedException If the number is not within the range and no custom throwable is provided via `causeOf`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateContains(number: Long, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null, lazyMessage: Transformer<LongRange, Any>): LongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is contained within the current [LongRange] instance.
 * If the number is not within the range, throws a [ValidationFailedException] with an optional custom message
 * and cause.
 *
 * @param number The number to check for membership within the [LongRange].
 * @param property The property associated with this validation. Can be null if not applicable.
 * @param variableName The optional name of the variable being validated. Included in validation failure messages if provided.
 * @param message Optional custom error message to include if validation fails. Defaults to a generic message.
 * @param causeOf A transformer function to produce a throwable in case of validation failure. Can be null if not applicable.
 * @param cause An optional transformer function to generate the cause of the [ValidationFailedException]. Can be null.
 * @return The same [LongRange] instance if validation succeeds, allowing for method chaining.
 * @throws ValidationFailedException if the [number] is not contained within this [LongRange].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateContains(number: Long, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given `number` is within the bounds of this `LongRange`.
 * If the `number` is not contained in the range, an exception is thrown.
 *
 * @param number The number to validate that should be within the range.
 * @param property The primary property tied to the validation, or null if not specified.
 * @param variable An optional secondary property providing additional context, or null if not specified.
 * @param message An optional message describing the validation failure. Defaults to a general message if null.
 * @param causeOf A transformer to generate the base cause of the exception, or null if not specified.
 * @param cause A transformer to specify additional cause information for the exception, or null if not specified.
 * @return The current `LongRange` if validation is successful.
 * @throws ValidationFailedException If the `number` is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateContains(number: Long, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is within the bounds of the LongRange instance.
 * Throws a ValidationFailedException if the validation fails.
 *
 * @param number The number to validate for inclusion within the range.
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter in the callable that caused the validation issue. Can be null. Defaults to null.
 * @param message An optional custom message providing details about the validation failure. Defaults to null.
 * @param causeOf A transformer that generates the cause exception based on the LongRange instance. Can be null. Defaults to null.
 * @param cause A transformer that generates the cause exception based on the LongRange instance. Can be null. Defaults to null.
 * @return The LongRange instance being validated, if the validation succeeds.
 * @throws ValidationFailedException if the provided number is not within the bounds of the LongRange instance.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateContains(number: Long, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given number is within the specified [LongRange]. If the number is not contained within the range,
 * an exception is thrown with optional additional information.
 *
 * @param number The number to validate against the [LongRange].
 * @param callable An optional [KFunction] representing the context of the validation, used for generating detailed error information.
 * @param parameter An optional [KParameter] indicating the parameter involved in the validation, used for error context.
 * @param message An optional custom message to include in the validation failure exception.
 * @param causeOf An optional [Transformer] to generate a custom exception based on the [LongRange] when validation fails.
 * @param cause An optional [Transformer] to provide the cause of the validation failure exception based on the [LongRange].
 * @return The original [LongRange] if the validation succeeds.
 * @throws ValidationFailedException If the number is not contained within the range, providing detailed error context if parameters are supplied.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateContains(number: Long, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is within the range defined by the current [LongRange] instance.
 * If the number is not within this range, a validation exception is thrown.
 *
 * @param number The value to check if it lies within the range.
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName The name of the parameter being validated (optional).
 * @param message Custom message to include in the exception if validation fails (optional).
 * @param causeOf Function that generates a throwable to be thrown, with the range as the input, overriding the default exception (optional).
 * @param cause Function that generates a cause for the validation exception, with the range as the input (optional).
 * @return The current [LongRange] instance if validation is successful.
 * @throws ValidationFailedException If the number is not contained within this range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateContains(number: Long, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified `number` is within this `LongRange`.
 * If the `number` is not within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to check for inclusion within the range.
 * @param callableName The name of the callable entity associated with this validation, or null if not applicable.
 * @param parameter The `KParameter` instance related to this validation, or null if not applicable.
 * @param message An optional custom error message providing additional details about the validation failure.
 * @param causeOf A transformer function that, when provided, generates the `Throwable` cause for the exception.
 * @param cause An alternative transformer function that generates the `Throwable` cause for the exception if `causeOf` is not specified.
 * @return The current `LongRange` if the validation passes.
 * @throws ValidationFailedException If the specified `number` is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateContains(number: Long, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the range,
 * taking into account any exclusions defined in the `LongRangeWithExclusions`.
 * If the number is not contained, an exception is thrown.
 *
 * @param number The number to validate against the range.
 * @param causeOf An optional transformer to generate an exception from the current range if validation fails.
 * @param cause An optional transformer to generate the cause of the exception if validation fails.
 * @return The current `LongRangeWithExclusions` instance if the validation is successful.
 * @throws ValidationFailedException If the number is not contained within the range and its exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateContains(number: Long, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the specified number is within the range of this `LongRangeWithExclusions`.
 * If the number is not within the range, an exception is thrown with the provided message and cause.
 *
 * @param number The `Long` value to validate against the range.
 * @param causeOf An optional transformer that generates a throwable cause based on the range. Default value is `null`.
 * @param cause An optional transformer that generates a throwable cause based on the range. Default value is `null`.
 * @param lazyMessage A transformer that generates a custom message object based on the range when validation fails.
 * @return The current `LongRangeWithExclusions` instance if validation is successful.
 * @throws ValidationFailedException If the number is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateContains(number: Long, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null, lazyMessage: Transformer<LongRangeWithExclusions, Any>): LongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within this [LongRangeWithExclusions].
 * If the number is not within the range or excluded values, a [ValidationFailedException] is thrown.
 *
 * @param number The number to validate against the range.
 * @param property Optional property associated with the validation.
 * @param variableName Optional name of the variable being validated.
 * @param message Optional custom error message for validation failure.
 * @param causeOf Optional transformer to construct a specific throwable cause when validation fails.
 * @param cause Optional transformer to create an additional cause for validation failure.
 * @return The current [LongRangeWithExclusions] instance if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateContains(number: Long, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the range, and throws an exception if it is not.
 *
 * @param number The number to check for containment within the range.
 * @param property An optional property associated with the validation context.
 * @param variable An optional variable associated with the validation context.
 * @param message An optional message to include if the validation fails.
 * @param causeOf An optional transformer to provide the specific exception to throw when validation fails.
 * @param cause An optional transformer to derive a cause for the validation failure.
 * @return The current `LongRangeWithExclusions` instance if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateContains(number: Long, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within this `LongRangeWithExclusions` instance.
 * If the number is not contained, a `ValidationFailedException` is thrown.
 *
 * @param number The number to check for containment within the range.
 * @param callable The function where the validation is being performed, used for detailed exception construction. Can be null.
 * @param parameterName The name of the parameter being validated, used for detailed exception construction. Can be null.
 * @param message Optional message to include in the exception if validation fails. Defaults to a message indicating the number is not contained.
 * @param causeOf A transformer function that generates a Throwable cause for the exception using the current instance. Can be null.
 * @param cause An alternative transformer function that generates a Throwable cause for the exception using the current instance. Can be null.
 * @return The current `LongRangeWithExclusions` instance if validation is successful.
 * @throws ValidationFailedException if the number is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateContains(number: Long, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the specified number is contained within the `LongRangeWithExclusions`.
 * Throws a `ValidationFailedException` if the number is not within the range or fails the custom validation logic provided via the parameters.
 *
 * @param number The number to validate whether it is contained within the range.
 * @param callable An optional reference to the function where validation is being performed. Used for exception reporting.
 * @param parameter An optional parameter reference associated with the validation. Used for exception reporting.
 * @param message An optional custom error message to include when the validation fails.
 * @param causeOf An optional transformer to generate the exception to be thrown when validation fails based on the `LongRangeWithExclusions`.
 * @param cause An optional transformer to create the root cause of the exception when validation fails, based on the `LongRangeWithExclusions`.
 * @return The instance of `LongRangeWithExclusions` if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateContains(number: Long, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within the current `LongRangeWithExclusions` instance.
 * If the number is not contained, an exception is thrown based on the provided optional cause transformers.
 *
 * @param number The number to validate against the range.
 * @param callableName The name of the callable or function where this validation is used (optional).
 * @param parameterName The name of the parameter being validated (optional).
 * @param message The custom error message to use if validation fails (optional).
 * @param causeOf An optional transformer to generate a cause exception if the validation fails.
 * @param cause An optional transformer to generate a secondary cause exception if the validation fails.
 * @return The instance of `LongRangeWithExclusions` on successful validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateContains(number: Long, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the range, considering any exclusions.
 * If the number is not included, a `ValidationFailedException` is thrown.
 *
 * @param number The number to be checked against the range.
 * @param callableName The name of the callable in context, used for enhanced exception information. Can be null.
 * @param parameter The parameter being validated, used for enhanced exception information. Can be null.
 * @param message An optional custom error message to be used when validation fails. Can be null.
 * @param causeOf A transformer for generating a root cause exception based on the current range. Can be null.
 * @param cause A transformer for generating a linked cause exception based on the current range. Can be null.
 * @return The current `LongRangeWithExclusions` instance, to allow method chaining and fluent usage.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateContains(number: Long, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the LongRangeWithConditions instance.
 * If the number is not within the range, an exception is thrown.
 *
 * @param number The number to validate against the range.
 * @param causeOf An optional transformer that generates a Throwable based on the LongRangeWithConditions instance
 *                to be used as the main cause of the exception if validation fails.
 * @param cause An optional transformer that generates a Throwable based on the LongRangeWithConditions instance
 *              to be used as the secondary cause (nested cause) of the exception if validation fails.
 * @return The same LongRangeWithConditions instance, if the validation passes successfully.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateContains(number: Long, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether a given number is contained within the range represented by the current `LongRangeWithConditions` instance.
 * If the number is not within the range, an exception is thrown with the specified cause and message.
 *
 * @param number The number to be checked for inclusion within the range.
 * @param causeOf A transformer that generates the primary throwable cause if the validation fails. Default is null.
 * @param cause A transformer that generates an additional throwable cause if the validation fails. Default is null.
 * @param lazyMessage A transformer that generates a message to describe the failure context.
 * @return The current `LongRangeWithConditions` instance for chaining purposes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateContains(number: Long, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null, lazyMessage: Transformer<LongRangeWithConditions, Any>): LongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the current range.
 * If the number is not within the range, an exception is thrown with configurable properties.
 *
 * @param number The number to be checked against the range.
 * @param property The property related to the validation, which can be used for error reporting.
 * @param variableName The name of the variable being validated for inclusion in error messages, if applicable.
 * @param message An optional custom error message to use if the validation fails.
 * @param causeOf An optional transformer to generate a cause for the exception based on the current object.
 * @param cause An optional transformer to generate the root cause for the exception.
 * @return The current instance of LongRangeWithConditions if the validation passes successfully.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateContains(number: Long, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the specified number is within the range represented by this `LongRangeWithConditions` instance.
 * If the number is not within the range, an exception is thrown.
 *
 * @param number The number to check for containment within the range.
 * @param property The property associated with the validation, may be null.
 * @param variable An optional secondary property associated with the validation, may be null.
 * @param message An optional error message to be included in the exception if validation fails, may be null.
 * @param causeOf An optional transformer for generating a throwable cause of type `Throwable` from the current `LongRangeWithConditions` instance, may be null.
 * @param cause An optional transformer for generating a throwable cause of type `Throwable` from the current `LongRangeWithConditions` instance, may be null.
 * @return The current instance of `LongRangeWithConditions` if the validation passes.
 * @throws ValidationFailedException If the specified number is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateContains(number: Long, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is within the current range.
 * If the number is not within the range, it throws a `ValidationFailedException`.
 *
 * @param number The number to check for containment within the range.
 * @param callable The callable function associated with the validation, or null if not applicable.
 * @param parameterName The name of the parameter being validated, or null if not provided.
 * @param message A custom error message to be used in the exception, or null for a default message.
 * @param causeOf A transformer to generate the cause exception, or null if this step is skipped.
 * @param cause A transformer for generating the root cause of the exception, or null if not applicable.
 * @return The validated LongRangeWithConditions instance if the number is within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateContains(number: Long, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the current range.
 * Throws a `ValidationFailedException` if the number is not within the range.
 *
 * @param number The number to check for containment within the range.
 * @param callable An optional Kotlin function reference associated with the validation context.
 * @param parameter An optional parameter reference associated with the validation context.
 * @param message An optional custom error message to include in exceptions.
 * @param causeOf An optional transformer to generate the cause of the exception based on the current instance.
 * @param cause An optional transformer to generate additional details for the cause of the exception.
 * @return The current `LongRangeWithConditions` instance if validation passes successfully.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateContains(number: Long, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is contained within this range. If the number is not
 * within the range, a validation exception is thrown.
 *
 * @param number The number to check for inclusion in the range.
 * @param callableName An optional name of the callable being validated. May be null.
 * @param parameterName An optional name of the parameter being validated. Defaults to null if not provided.
 * @param message An optional custom validation failure message. Defaults to null if not provided.
 * @param causeOf An optional transformer that generates an exception to throw when validation fails. Defaults to null if not provided.
 * @param cause An optional transformer that provides the cause of the validation exception. Defaults to null if not provided.
 * @return The current instance of `LongRangeWithConditions` if the validation succeeds.
 * @throws ValidationFailedException If the number is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateContains(number: Long, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is contained within the current `LongRangeWithConditions`.
 * If the number is not contained, an exception is thrown.
 *
 * @param number The number to validate against the range.
 * @param callableName The optional name of the callable triggering this validation.
 * @param parameter An optional parameter associated with the validation context.
 * @param message An optional custom message if validation fails.
 * @param causeOf An optional function to provide the cause of the exception if validation fails.
 * @param cause An optional function to specify additional context for the thrown exception when validation fails.
 * @return The current `LongRangeWithConditions` if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateContains(number: Long, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the provided number is contained within the current `ULongRange`. If the number is
 * not within the range, a `ValidationFailedException` is thrown. The exception can include a custom
 * cause generated by the optional transformers provided as parameters.
 *
 * @param number The `ULong` value to check for containment within the current range.
 * @param causeOf An optional transformer that generates a `Throwable` cause when the validation fails,
 *                using the current range as input. If `null`, the default error behavior is used.
 * @param cause An optional transformer that generates a supplementary `Throwable` for additional
 *              error context, using the current range as input. If `null`, no additional context is added.
 * @return The current `ULongRange` if the validation passes.
 * @throws ValidationFailedException if the number is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateContains(number: ULong, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether a given number is within the specified range (`ULongRange`).
 * If the number is not within the range, an exception is thrown, which can optionally
 * be customized using transformers for detailed error context.
 *
 * @param number The `ULong` number to check for containment within the range.
 * @param causeOf An optional transformer that generates a `Throwable` based on the current range,
 *                used to specify the cause of the `ValidationFailedException`.
 * @param cause An optional transformer that generates a `Throwable` based on the current range,
 *              which can be used as the direct cause of the exception.
 * @param lazyMessage A transformer that generates a message from the current range, providing
 *                    additional context for the exception.
 * @return The original `ULongRange` instance, enabling method chaining when the validation succeeds.
 * @throws ValidationFailedException If the provided number is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateContains(number: ULong, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null, lazyMessage: Transformer<ULongRange, Any>): ULongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within the range. If the number is not within the range,
 * it throws a `ValidationFailedException` with an optional message and cause.
 *
 * @param number The `ULong` number to check for containment within the range.
 * @param property Optional `KProperty` associated with the validation, used for error reporting. Can be null.
 * @param variableName Optional name of the variable involved in the validation. Included in the error message if provided.
 * @param message Optional custom error message to include in the exception if the validation fails. Defaults to a standard message.
 * @param causeOf Optional transformer that, when invoked, provides a throwable to be used as the cause of the exception.
 * @param cause Optional transformer that provides a general throwable cause of the exception. Defaults to null.
 * @return The same `ULongRange` instance if the validation passes.
 * @throws ValidationFailedException If the number is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateContains(number: ULong, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified `number` exists within the current `ULongRange`. If the `number` is not contained
 * within the range, a `ValidationFailedException` is thrown with an optional custom error message and cause.
 *
 * @param number The `ULong` value to check for containment within the `ULongRange`.
 * @param property An optional `KProperty` representing the main property context of the validation.
 * @param variable An optional `KProperty` providing additional context for the validation failure.
 * @param message An optional custom error message that describes the validation failure.
 * @param causeOf An optional transformer to generate a cause `Throwable` from the `ULongRange`.
 * @param cause An optional transformer to generate the root cause `Throwable` from the `ULongRange`.
 * @return The current `ULongRange` instance if validation is successful.
 * @throws ValidationFailedException If the `number` is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateContains(number: ULong, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given `ULong` number is contained within this `ULongRange`.
 * If the number is not within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The `ULong` number to validate against this range.
 * @param callable The Kotlin function (`KFunction`) associated with this validation. Can be null.
 * @param parameterName The name of the parameter in the associated callable. Can be null.
 * @param message An optional custom message to include in the validation failure exception. Default is null.
 * @param causeOf A transformer function that generates a `Throwable` based on this range. Used to create custom causes for validation failure. Can be null.
 * @param cause A transformer function that generates a `Throwable` based on this range. Used as a secondary cause for validation failure. Can be null.
 * @return The current `ULongRange` if the validation succeeds.
 * @throws ValidationFailedException If the given `ULong` number is not contained within this range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateContains(number: ULong, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified `number` is contained within this `ULongRange`.
 * If the `number` is not contained, an exception is thrown.
 *
 * @param number The unsigned long number to check for inclusion within the range.
 * @param callable The callable function reference associated with this validation, if any.
 * @param parameter The specific function parameter related to this validation, if applicable.
 * @param message An optional custom message for the exception if validation fails.
 * @param causeOf An optional transformer for constructing a `Throwable` cause based on the range, if validation fails.
 * @param cause An optional transformer for constructing a `Throwable` cause based on the range, if validation fails.
 * @return The same `ULongRange` instance, allowing method chaining.
 * @throws ValidationFailedException If the `number` is not within the range, with the relevant exception details.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateContains(number: ULong, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if a given number is within the range represented by the current `ULongRange` instance.
 * If the number is not contained in the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to check for containment within the range.
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName An optional name of the parameter that caused the validation failure, or `null` if not applicable.
 * @param message An optional custom message providing additional details about the validation failure, or `null` for a default message.
 * @param causeOf An optional transformer to generate the root cause of the exception, or `null` if not applicable.
 * @param cause An optional transformer to generate the underlying cause of the exception, or `null` if no underlying cause exists.
 * @return The original `ULongRange` instance if the validation passes.
 * @throws ValidationFailedException If the specified number is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateContains(number: ULong, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified [number] is within this [ULongRange].
 * If the [number] is not in the range, an exception is thrown with an optional message and cause.
 *
 * @param number The unsigned long number to check for containment within the range.
 * @param callableName The name of the callable (e.g., function or property) where the validation is performed, or null if not specified.
 * @param parameter The [KParameter] related to the validation failure, or null if not applicable.
 * @param message An optional error message to include in the exception if the validation fails.
 * @param causeOf A transformer function to optionally supply a custom [Throwable] for the failure based on the current range.
 * @param cause A transformer function to optionally supply a nested [Throwable] cause for the validation failure.
 * @return The original [ULongRange] if the [number] is valid (contained within the range).
 * @throws ValidationFailedException if the [number] is not contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateContains(number: ULong, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether a given number is within the specified range, optionally throwing a custom exception
 * if the validation fails.
 *
 * @param number The number to check for presence within the range.
 * @param causeOf Optional transformer used to generate a custom exception when the validation fails.
 * @param cause Optional transformer used to specify the root cause of the failure.
 * @return The current instance of [ULongRangeWithExclusions] if the validation passes.
 * @throws ValidationFailedException If the number is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateContains(number: ULong, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the current range, considering exclusions.
 * If the number is not in the range, throws a `ValidationFailedException` with the provided message and cause.
 *
 * @param number The number to validate against the range.
 * @param causeOf A transformer function that, when provided, generates a throwable cause of the validation failure.
 * @param cause An optional transformer function to generate an additional throwable cause.
 * @param lazyMessage A transformer function to generate a message for the validation failure lazily.
 * @return The current instance of `ULongRangeWithExclusions`. Useful for chaining operations.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateContains(number: ULong, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null, lazyMessage: Transformer<ULongRangeWithExclusions, Any>): ULongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within this ULongRangeWithExclusions instance.
 * If the number is not contained, a ValidationFailedException is thrown.
 *
 * @param number The number to validate against the range.
 * @param property An optional KProperty representing the property being validated.
 * @param variableName An optional string representing the name of the variable being validated.
 * @param message An optional custom message to use in the exception if validation fails.
 * @param causeOf An optional transformer that produces a Throwable to be thrown as the cause of the exception.
 * @param cause An optional transformer that produces a Throwable to be used as the direct cause of the exception.
 * @return The current ULongRangeWithExclusions instance if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateContains(number: ULong, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the specified number is within the range, excluding any defined exclusions.
 * Throws a validation exception if the number is not valid.
 *
 * @param number The ULong value to check for containment within the range.
 * @param property Optional property related to the value being validated.
 * @param variable Optional variable related to the value being validated.
 * @param message Optional custom validation error message.
 * @param causeOf Optional transformer to generate the cause of the validation failure.
 * @param cause Optional transformer to generate an exception cause for the validation failure.
 * @return The current instance of ULongRangeWithExclusions if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateContains(number: ULong, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within this range,
 * throwing a ValidationFailedException if the number is not included.
 *
 * @param number The number to validate against the range.
 * @param callable The callable function associated with the validation.
 * @param parameterName The name of the parameter being validated, if applicable.
 * @param message A custom error message to be used in the exception, if validation fails.
 * @param causeOf A transformer to generate a throwable cause based on the current range, if validation fails.
 * @param cause An alternative transformer to generate a throwable cause based on the current range, if validation fails.
 * @return The current ULongRangeWithExclusions instance if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateContains(number: ULong, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within the range, including any exclusions,
 * and throws a validation exception if the condition is not met.
 *
 * @param number The number to validate against the range.
 * @param callable The function in which the validation is being performed (optional).
 * @param parameter The parameter of the callable that this validation is associated with (optional).
 * @param message A custom error message to be used if validation fails (optional).
 * @param causeOf A transformer that generates a throwable cause from the current range when validation fails (optional).
 * @param cause Another transformer that generates a throwable cause from the current range when validation fails (optional).
 * @return The current instance of the range after validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateContains(number: ULong, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is contained within the ULongRangeWithExclusions instance.
 * If the number is not contained, throws a validation exception.
 *
 * @param number The number to validate against the range.
 * @param callableName The name of the callable function being validated, or null if not applicable.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message Custom validation failure message, or null to use the default message.
 * @param causeOf A transformer to produce the cause of the exception, or null if not applicable.
 * @param cause A transformer to produce an additional cause for the exception, or null if not applicable.
 * @return The ULongRangeWithExclusions instance, to allow method chaining.
 * @throws ValidationFailedException If the range does not contain the specified number.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateContains(number: ULong, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is within the range defined by this `ULongRangeWithExclusions`.
 * If the number is not contained in the range, an exception is thrown.
 *
 * @param number The unsigned long value to validate.
 * @param callableName The name of the callable element for error reporting, if applicable.
 * @param parameter The parameter associated with the value, if applicable.
 * @param message An optional custom error message to use if validation fails.
 * @param causeOf A transformer used to generate a throwable cause if validation fails.
 * @param cause An optional transformer to generate an additional cause for the validation exception.
 * @return The current instance of `ULongRangeWithExclusions` if validation succeeds.
 * @throws ValidationFailedException If the number is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateContains(number: ULong, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given number is within the ULongRangeWithConditions.
 * If the number is not within the range, it throws a ValidationFailedException with an optional cause provided by the transformers.
 *
 * @param number The unsigned long number to validate against the range.
 * @param causeOf An optional transformer to generate a throwable cause based on the current range if the validation fails.
 * @param cause An optional transformer to generate a throwable cause based on the current range if the validation fails.
 * @return The current ULongRangeWithConditions instance if the validation succeeds.
 * @throws ValidationFailedException if the number is not within the range, with an optional cause provided by the transformers.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateContains(number: ULong, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException("$number is not in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is not in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given number is within the range defined by this `ULongRangeWithConditions`.
 * If the number is not contained, an exception is thrown with an optional lazy message and cause.
 *
 * @param number The unsigned long number to validate against the range.
 * @param causeOf An optional transformer used to generate specific throwable causes.
 * @param cause An optional transformer used to generate an alternative throwable cause.
 * @param lazyMessage A transformer to generate a lazy evaluation message if validation fails.
 * @return The current `ULongRangeWithConditions` instance for chaining or further operations.
 * @throws ValidationFailedException If the number is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateContains(number: ULong, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null, lazyMessage: Transformer<ULongRangeWithConditions, Any>): ULongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within this range. If the number is not contained,
 * a specific exception is thrown with the provided or default message and cause.
 *
 * @param number The number to validate against the range.
 * @param property An optional property associated with the validation, used for contextual exception messages.
 * @param variableName An optional variable name for inclusion in the exception message.
 * @param message An optional custom validation failure message. Defaults to a standard format if not provided.
 * @param causeOf An optional transformer to generate a specific exception based on this range,
 *                which can provide additional context or customization.
 * @param cause An optional transformer to generate the root cause of the exception.
 * @return The same instance of `ULongRangeWithConditions` for fluent chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateContains(number: ULong, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates if the given `number` is contained within the current `ULongRangeWithConditions` instance.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param number The unsigned long number to validate if it is within the range.
 * @param property The property being validated, or `null` if not applicable.
 * @param variable The variable being validated, or `null` if not applicable.
 * @param message Optional custom error message to use when the validation fails.
 * @param causeOf An optional transformer that generates the cause of the `ValidationFailedException`.
 * @param cause An optional transformer to supply additional details for the `ValidationFailedException` cause.
 * @return The `ULongRangeWithConditions` instance to allow method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateContains(number: ULong, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is within the current range. If the number is not contained in the range,
 * a validation exception is thrown using the provided parameters to construct the error message and cause.
 *
 * @param number The number to check for containment within the range.
 * @param callable The function or callable element to provide context for the validation (optional).
 * @param parameterName The name of the parameter being validated, used in the validation failure message (optional).
 * @param message Custom error message to include in the validation exception (optional).
 * @param causeOf A transformer function to generate a throwable cause for the exception based on the current range (optional).
 * @param cause A transformer function to generate an additional throwable cause for the exception based on the current range (optional).
 * @return The original range (this) if the validation passes.
 * @throws ValidationFailedException If validation fails, including contextual information provided by the parameters.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateContains(number: ULong, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the provided number is contained within the range represented by this `ULongRangeWithConditions`.
 * If the number is not contained within the range, a `ValidationFailedException` will be thrown.
 *
 * @param number The number to validate against the range.
 * @param callable An optional callable reference providing context for the validation.
 * @param parameter An optional parameter providing additional context for the validation.
 * @param message An optional custom error message to include if validation fails.
 * @param causeOf An optional transformer to determine the cause of the validation failure exception.
 * @param cause An optional transformer to define the root cause of the validation failure exception.
 * @return The original `ULongRangeWithConditions` instance if the validation is successful.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateContains(number: ULong, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the provided number is contained within the ULongRangeWithConditions.
 * If the number is not in the range, it throws a ValidationFailedException.
 *
 * @param number The unsigned long number to validate against the range.
 * @param callableName The name of the function or callable relevant to the validation context.
 * @param parameterName The name of the parameter being validated, if available. Defaults to null.
 * @param message An optional custom validation failure message. Defaults to null.
 * @param causeOf A transformer used to generate a throwable as the primary cause of the validation failure. Defaults to null.
 * @param cause A transformer used to generate a throwable as the secondary cause of the validation failure. Defaults to null.
 * @return The current instance of ULongRangeWithConditions for chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateContains(number: ULong, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the given number is contained within the range defined by this `ULongRangeWithConditions`.
 * If validation fails, an exception is thrown.
 *
 * @param number The `ULong` value to validate against the range.
 * @param callableName An optional name of the callable associated with the validation.
 * @param parameter An optional `KParameter` representing the parameter associated with the validation.
 * @param message An optional custom message for the exception in case validation fails.
 * @param causeOf An optional transformer to generate the exception cause from the current range.
 * @param cause An optional transformer to set a cause for the exception.
 * @return The same `ULongRangeWithConditions` instance, if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateContains(number: ULong, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number !in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "doesn't contain $number", cause?.invoke(this)))
    return this
}

/**
 * Validates that the specified number is not contained within the current integer range.
 * If the number is found in the range, an exception is thrown.
 *
 * @param number The integer to check for containment within the range.
 * @param causeOf An optional transformer that generates a throwable based on the current range,
 *                which will be thrown as the cause of the exception. Can be null.
 * @param cause An optional transformer that generates an additional cause throwable
 *              based on the current range. Can be null.
 * @return The original range if the validation passes (i.e., the number is not in the range).
 * @throws ValidationFailedException If the specified number exists within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateNotContains(number: Int, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within this range.
 * If the number is within the range, a ValidationFailedException is thrown.
 *
 * @param number The number to validate against the range.
 * @param causeOf A transformer to generate the exception to be thrown, which allows for customization of exception creation. Can be null.
 * @param cause A transformer that generates the underlying cause of the exception. Can be null.
 * @param lazyMessage A transformer that produces a custom message for the exception.
 * @return The original range if validation passes successfully.
 * @throws ValidationFailedException if the number is contained within this range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateNotContains(number: Int, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null, lazyMessage: Transformer<IntRange, Any>): IntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within this range. If the number is found in the range,
 * a `ValidationFailedException` is thrown with the specified details.
 *
 * @param number The number to check for presence in the range.
 * @param property The property associated with the validation failure, or null if not applicable.
 * @param variableName Optional name of the variable involved in the validation. Included in the exception message
 *                     if it is not null.
 * @param message Additional descriptive message for the validation failure. Defaults to a message indicating
 *                that the range contains the given number.
 * @param causeOf A function that generates a throwable to wrap the `ValidationFailedException`, or null if not applicable.
 * @param cause A function that generates the underlying cause for the `ValidationFailedException`, or null if not applicable.
 * @return The original range if the number is not contained within it.
 * @throws ValidationFailedException If the number is contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateNotContains(number: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given `IntRange` does not contain the specified number.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param number The integer value to check for absence in the range.
 * @param property The `KProperty` associated with the validation, providing additional context.
 *                 It can be null if not explicitly specified.
 * @param variable An optional secondary `KProperty` that provides further context about the variable.
 *                 It can also be null if not used.
 * @param message An optional custom error message to override the default. It can be null.
 * @param causeOf An optional transformer for generating the root cause `Throwable`, or null to use the default behavior.
 * @param cause An optional transformer for providing the causal `Throwable` tied to the validation.
 * @return The original `IntRange` if the validation passes.
 * @throws ValidationFailedException if the specified `number` is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateNotContains(number: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not within the bounds of this `IntRange`.
 *
 * If the number is found within the range, this method throws a `ValidationFailedException`.
 *
 * @param number The integer value to check for exclusion from this range.
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter associated with validation. Can be null.
 * @param message An optional custom message for the validation failure. If not provided, defaults to "contains <number>".
 * @param causeOf A transformer that generates the cause if validation fails. Can be null.
 * @param cause A transformer that creates a throwable to be used as the original cause in the exception. Can be null.
 * @return The input `IntRange` if the validation succeeds.
 * @throws ValidationFailedException If the number is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateNotContains(number: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that an integer is not contained within the given range. If the number is found
 * within the range, a validation exception is thrown.
 *
 * @param number the integer value to check for exclusion from the range.
 * @param callable the optional callable (function or method) associated with the validation context.
 * @param parameter the optional parameter within the callable related to the validation context.
 * @param message an optional string to customize the exception message, defaulting to a generic message if not provided.
 * @param causeOf an optional transformer function to generate the root cause of the exception based on the provided range.
 * @param cause an optional transformer function to generate an additional cause of the exception based on the provided range.
 * @return the original range if validation succeeds, ensuring that the number is not contained within it.
 * @throws ValidationFailedException if the number is found within the range, optionally containing detailed context and a specified cause.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateNotContains(number: Int, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not included in this range. If the number is found within
 * the range, a `ValidationFailedException` is thrown with the provided details.
 *
 * @param number The number to check for exclusion from the range.
 * @param callableName The name of the callable (function or method) that performs this validation.
 *                     Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message to include in the exception if the validation fails.
 *                Defaults to a message indicating the range contains the specified number
 *                when not provided.
 * @param causeOf A transformer function that produces the exception to be thrown based on the
 *                current range. Can be null.
 * @param cause A transformer function providing a cause for the thrown exception, if any.
 *              Can be null.
 * @return The original range if the validation succeeds, allowing for method chaining.
 * @throws ValidationFailedException If the specified number is found in this range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateNotContains(number: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within this range. If the number is found in the range,
 * a validation exception is thrown with optional customization for the exception message, callable information,
 * and cause configuration.
 *
 * @param number The number to check against the range.
 * @param callableName The name of the function or callable where this validation occurs, or null if not applicable.
 * @param parameter The parameter associated with this validation, or null if not applicable.
 * @param message An optional message to include in the exception if validation fails.
 * @param causeOf An optional transformer to define the cause of the exception triggered when validation fails.
 * @param cause An optional transformer to set an inner cause of the exception.
 * @return The original range if the validation passes.
 * @throws ValidationFailedException If the number is contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRange.validateNotContains(number: Int, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRange, Throwable>? = null, cause: Transformer<IntRange, Throwable>? = null): IntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range of this IntRangeWithExclusions object.
 * If the number is within the range and exclusions, a ValidationFailedException is thrown.
 *
 * @param number The integer to validate against the range and exclusions.
 * @param causeOf An optional transformer to generate a throwable cause based on the current IntRangeWithExclusions, in case of validation failure.
 * @param cause An optional transformer to generate a secondary throwable cause based on the current IntRangeWithExclusions, in case of validation failure.
 * @return The current IntRangeWithExclusions object, if validation is successful.
 * @throws ValidationFailedException If the number is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateNotContains(number: Int, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range, including its exclusions.
 * Throws a `ValidationFailedException` if the number is contained in the range.
 *
 * @param number The number to check within the range.
 * @param causeOf An optional transformer for generating a throwable cause if validation fails.
 * @param cause An optional transformer for generating a secondary throwable cause if validation fails.
 * @param lazyMessage A transformer used to generate a custom message for the exception when validation fails.
 * @return The current `IntRangeWithExclusions` instance if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateNotContains(number: Int, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null, lazyMessage: Transformer<IntRangeWithExclusions, Any>): IntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current `IntRangeWithExclusions`.
 * If the validation fails, an appropriate exception is thrown.
 *
 * @param number The number to validate as not being contained within the range.
 * @param property An optional `KProperty` associated with the validation.
 * @param variableName An optional name of the variable being validated.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer that provides the root cause of the exception.
 * @param cause An optional transformer that provides an additional cause for the exception.
 * @return The current `IntRangeWithExclusions` if the number is not contained within it.
 * @throws ValidationFailedException if the number is contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateNotContains(number: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within the range with exclusions.
 * If the number is found within the range or its exclusions, a validation exception is thrown.
 *
 * @param number The number to be checked against the range and its exclusions.
 * @param property The property associated with the value being validated, or null if not applicable.
 * @param variable An additional variable associated with the validation context, or null if not applicable.
 * @param message An optional error message to include in the exception if validation fails.
 * @param causeOf An optional transformer to generate a specific cause exception if validation fails.
 * @param cause Another optional transformer to define the root cause of the exception.
 * @return The same instance of the IntRangeWithExclusions if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateNotContains(number: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number does not fall within the range defined by this object. If the number is found within
 * the range, it throws a [ValidationFailedException].
 *
 * @param number The number to validate against this range.
 * @param callable An optional reference to a Kotlin function, providing context for debugging or error reporting.
 * @param parameterName An optional name of the parameter being validated.
 * @param message An optional custom message to be included in the exception if validation fails.
 * @param causeOf An optional transformer that defines how the exception is generated if the validation fails.
 * @param cause An optional transformer to generate the cause of the exception if validation fails.
 * @return The current instance of [IntRangeWithExclusions] for method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateNotContains(number: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified [number] is not within the range defined by this [IntRangeWithExclusions].
 * If the [number] is found within the range, an exception is thrown.
 *
 * @param number The number to validate against the range.
 * @param callable The callable function that initiated the validation, used for error reporting.
 * @param parameter The parameter being validated, used for error reporting. Optional.
 * @param message The custom error message to use if validation fails. Optional.
 * @param causeOf A transformer function to generate a throwable based on the current range when validation fails. Optional.
 * @param cause A transformer function to provide the underlying cause of the failure based on the current range. Optional.
 * @return The same [IntRangeWithExclusions] instance if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateNotContains(number: Int, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not within the current range or its exclusions.
 * If the number is found, a validation exception is thrown with the specified message and optional cause.
 *
 * @param number The number to be checked against the range and exclusions.
 * @param callableName The name of the calling function used for error reporting, or `null` if not applicable.
 * @param parameterName The name of the parameter being validated, or `null` if not applicable.
 * @param message A custom error message, or `null` to use the default message.
 * @param causeOf A transformer to produce a custom exception for the failure cause, or `null` if not applicable.
 * @param cause A transformer to produce a custom cause of the exception, or `null` if not applicable.
 * @return The current instance of `IntRangeWithExclusions` for method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateNotContains(number: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not within the current IntRangeWithExclusions instance.
 * If the number is found within the range or exclusions, an exception will be thrown.
 *
 * @param number The number to validate against the range and exclusions.
 * @param callableName The name of the callable being validated, used for exception context.
 * @param parameter The parameter being validated, used to offer additional context for exception handling.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf Transformer instance to generate a throwable cause of the validation failure.
 * @param cause Transformer instance for additional exception context when validation fails.
 * @return The current IntRangeWithExclusions instance, if the validation passes without errors.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithExclusions.validateNotContains(number: Int, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRangeWithExclusions, Throwable>? = null, cause: Transformer<IntRangeWithExclusions, Throwable>? = null): IntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not within the current range. If the number is within the range,
 * a `ValidationFailedException` is thrown.
 *
 * @param number The number to be checked against the range.
 * @param causeOf Optional transformer to generate the root cause exception when validation fails.
 * @param cause Optional transformer to generate an additional, nested cause for the exception.
 * @return The current instance of `IntRangeWithConditions` if the validation passes.
 * @throws ValidationFailedException if the specified number is within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateNotContains(number: Int, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range.
 * If the number is present in the range, this method throws a validation exception.
 *
 * @param number The number to check against the current range.
 * @param causeOf A transformer responsible for generating an exception when a validation failure occurs.
 * @param cause A secondary transformer for generating an additional underlying cause for the validation exception.
 * @param lazyMessage A transformer to provide a detailed message for the validation exception.
 * @return The current range (`IntRangeWithConditions`) if the validation passes.
 * @throws ValidationFailedException If the specified number is present in the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateNotContains(number: Int, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null, lazyMessage: Transformer<IntRangeWithConditions, Any>): IntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within the range defined by this `IntRangeWithConditions`.
 * If the number is found within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to be validated to ensure it is not contained within the range.
 * @param property The associated property, if any, related to the validation. Can be null.
 * @param variableName The name of the variable for error reporting purposes. Can be null.
 * @param message An optional custom error message to include in the exception. Can be null.
 * @param causeOf A transformer providing the cause of the validation failure. Can be null.
 * @param cause A transformer providing additional cause details for the validation failure. Can be null.
 * @return The current `IntRangeWithConditions` instance if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateNotContains(number: Int, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the range defined by this instance of IntRangeWithConditions.
 * If the number is found in the range, an exception is thrown with the provided details.
 *
 * @param number The number that must not be present in the range.
 * @param property The property reference associated with this validation, can be null.
 * @param variable The variable reference associated with this validation, can be null.
 * @param message A custom error message to include in the exception if the number is found in the range, can be null.
 * @param causeOf A transformer to generate a throwable cause if the number is found in the range, can be null.
 * @param cause A transformer to generate a throwable cause to wrap in a generated exception, can be null.
 * @return The unchanged IntRangeWithConditions object if the validation passes.
 * @throws ValidationFailedException if the number is found in the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateNotContains(number: Int, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that this `IntRangeWithConditions` does not contain the specified number.
 * If the number is found within the range, it throws a validation exception.
 *
 * @param number The number to check against the range.
 * @param callable An optional callable indicating the function context for the validation failure.
 * @param parameterName An optional name of the parameter associated with the validation.
 * @param message An optional custom message for the validation failure.
 * @param causeOf An optional transformer that produces a throwable to indicate the root cause of the failure.
 * @param cause An optional transformer used to generate an underlying cause throwable for the failure.
 * @return The current `IntRangeWithConditions` instance after validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateNotContains(number: Int, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within the range represented by this object.
 * If the number is found within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to check against the range.
 * @param callable The callable reference (optional) used for constructing the exception.
 * @param parameter The parameter reference (optional) used for constructing the exception.
 * @param message The custom message (optional) for the exception if validation fails.
 * @param causeOf A transformer function (optional) generating the exception's cause.
 * @param cause Another transformer function (optional) generating the underlying cause of the exception.
 * @return The current instance of `IntRangeWithConditions`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateNotContains(number: Int, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within the range represented by the IntRangeWithConditions object.
 * If the number is contained within the range, an exception is thrown.
 *
 * @param number The number to validate.
 * @param callableName The name of the callable function or context where the validation occurs, used for exception message construction.
 * @param parameterName The name of the parameter being validated, used for validation failure description. Optional.
 * @param message A custom message to include in the exception if validation fails. Optional.
 * @param causeOf A transformer function to derive the root cause of the exception. Optional.
 * @param cause A transformer function to provide additional context or chaining of exceptions. Optional.
 * @return The same IntRangeWithConditions instance that the method was called on, for chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateNotContains(number: Int, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range. If the number exists within the
 * range, an exception is thrown.
 *
 * @param number The number to check if it is excluded from the range.
 * @param callableName The name of the callable context in which the validation occurs, or null if not applicable.
 * @param parameter The parameter associated with the context of validation, or null if not applicable.
 * @param message Custom error message to be included in the exception, or null for a default message.
 * @param causeOf A transformer function that provides a throwable cause of validation failure based on this range,
 *                or null if not applicable.
 * @param cause A transformer function that specifies the underlying cause of the exception based on this range when the
 *              validation fails, or null if not applicable.
 * @return The current range (`IntRangeWithConditions`) if the validation passes without exception.
 * @throws ValidationFailedException if the specified number is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun IntRangeWithConditions.validateNotContains(number: Int, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<IntRangeWithConditions, Throwable>? = null, cause: Transformer<IntRangeWithConditions, Throwable>? = null): IntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not within the range. If the number is found within the range,
 * a `ValidationFailedException` is thrown.
 *
 * @param number The number to check that must not be contained within the range.
 * @param causeOf A transformer that generates a throwable cause based on the range if provided.
 * @param cause An optional transformer that generates a secondary throwable cause based on the range.
 * @return The original range if the validation passes.
 * @throws ValidationFailedException If the number is within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateNotContains(number: UInt, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that a specified number is not contained within this `UIntRange`. If the number
 * is present, a `ValidationFailedException` is thrown with an optional custom cause and message.
 *
 * @param number The unsigned integer to check for presence in the range.
 * @param causeOf A transformer that produces a throwable to be used as the primary cause of the validation failure, or `null` if not specified.
 * @param cause A transformer that produces an additional throwable to be associated with the validation failure, or `null` if not specified.
 * @param lazyMessage A transformer that provides a custom message for the validation failure. The message is generated lazily when the exception is thrown.
 * @return The `UIntRange` instance this method was called on if validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateNotContains(number: UInt, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null, lazyMessage: Transformer<UIntRange, Any>): UIntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the `UIntRange`.
 * If the range contains the number, a `ValidationFailedException` is thrown.
 *
 * @param number The number to check for exclusion from the range.
 * @param property The property associated with the validation, or null if not applicable.
 * @param variableName An optional variable name involved in the validation. Appears in the exception message if not null.
 * @param message An additional message to include in the exception if validation fails. Defaults to `null`.
 * @param causeOf A transformer that provides a specific `Throwable` cause based on the range. Defaults to `null`.
 * @param cause A transformer that provides an additional `Throwable` cause based on the range. Defaults to `null`.
 * @return The original `UIntRange` if validation passes.
 * @throws ValidationFailedException If the range contains the specified number.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateNotContains(number: UInt, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified `UIntRange` does not contain a given number.
 * If the number is found within the range, this method throws a `ValidationFailedException`.
 *
 * @param number The `UInt` value to check for within the range.
 * @param property The primary `KProperty` associated with the validation, providing contextual information, or null if not specified.
 * @param variable An optional secondary `KProperty` providing additional context, or null if not specified.
 * @param message An optional custom message describing the validation failure. Defaults to "contains {number}" if not provided.
 * @param causeOf An optional transformer used to generate the underlying throwable cause. If null, it is ignored.
 * @param cause An optional transformer for generating an exception cause if validation fails.
 * @return The original `UIntRange` if the validation succeeds.
 * @throws ValidationFailedException if the specified number is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateNotContains(number: UInt, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given `UInt` number is not contained within the current `UIntRange`.
 * If the range contains the number, a `ValidationFailedException` is thrown.
 *
 * @param number The `UInt` number to check for exclusion from the range.
 * @param callable The `KFunction` representing the callable for context in the exception. Can be null.
 * @param parameterName The name of the parameter associated with the validation. Can be null.
 * @param message An optional custom message to include in the exception. Can be null.
 * @param causeOf A `Transformer` that takes the range as input and produces the underlying cause of the exception. Can be null.
 * @param cause An alternate `Transformer` that provides an additional cause for the exception. Can be null.
 * @return The original `UIntRange` if validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateNotContains(number: UInt, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified range does not contain the given number. If the number is found within the range,
 * a `ValidationFailedException` is thrown.
 *
 * @param number the unsigned integer to validate against the range
 * @param callable an optional reference to the [KFunction] related to this validation, or null if not applicable
 * @param parameter an optional reference to the [KParameter] involved in the validation, or null if not applicable
 * @param message an optional message providing additional context for the validation failure
 * @param causeOf an optional transformer to derive the validation failure's cause from the range, or null if not applicable
 * @param cause an optional transformer to produce an exception cause based on the range, or null if not applicable
 * @return the original range if validation passes (i.e., if the number is not contained within the range)
 * @throws ValidationFailedException if the given number is contained within the range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateNotContains(number: UInt, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given range does not contain the specified number. If the number
 * is found within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to check for presence in the range.
 * @param callableName The name of the callable (e.g., function or method) that invokes the validation.
 * @param parameterName The optional name of the parameter being validated.
 * @param message The optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer that can produce a `Throwable` cause based on the range.
 * @param cause An optional transformer that can produce a `Throwable` cause based on the range.
 * @return The same range if the number is not found.
 * @throws ValidationFailedException if the number is found in the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateNotContains(number: UInt, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified `number` is not contained within this `UIntRange`.
 * If the range contains the `number`, a `ValidationFailedException` is thrown.
 * Additional details such as the callable name, parameter, and a custom message
 * can be provided. Optional transformers can be used to customize the exception
 * cause.
 *
 * @param number The unsigned integer to check for absence in the range.
 * @param callableName The name of the callable (e.g., function or property) that triggered the validation, or null if not applicable.
 * @param parameter The `KParameter` instance representing the parameter to which the validation applies, or null if not applicable.
 * @param message An optional error message providing additional details about the validation failure.
 * @param causeOf An optional transformer to produce the primary cause of the exception based on the range, or null if not applicable.
 * @param cause An optional transformer to produce an additional cause of the exception based on the range, or null if not applicable.
 * @return The same `UIntRange` instance on which this function was called, if validation passes.
 * @throws ValidationFailedException If the range contains the specified `number`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRange.validateNotContains(number: UInt, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRange, Throwable>? = null, cause: Transformer<UIntRange, Throwable>? = null): UIntRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the range. If the number is found in the range,
 * a ValidationFailedException is thrown with an optional cause provided by transformers.
 *
 * @param number The number to check against the range.
 * @param causeOf An optional transformer that generates a throwable cause for the validation failure.
 * @param cause Another optional transformer for creating additional context for the throwable cause.
 * @return The original range if the validation succeeds without throwing an exception.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateNotContains(number: UInt, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range or its exclusions.
 * If the number is found, an exception is thrown with the provided message and optional causes.
 *
 * @param number The unsigned integer value to check against the range and exclusions.
 * @param causeOf Optional transformer to generate a throwable cause based on the current range
 *                and exclusions. If this is not null, it takes precedence in generating the exception.
 * @param cause Optional transformer to generate a throwable cause based on the current range
 *              and exclusions. Used as a nested cause if `causeOf` is null.
 * @param lazyMessage A transformer to generate a custom message based on the current range
 *                    and exclusions, which is included in the exception if the number is found.
 * @return The current instance of `UIntRangeWithExclusions` for chaining purposes.
 * @throws ValidationFailedException if the specified number is contained in the range or its exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateNotContains(number: UInt, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null, lazyMessage: Transformer<UIntRangeWithExclusions, Any>): UIntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the range, throwing an exception if it is.
 *
 * @param number The number to validate against the range.
 * @param property An optional property to associate with the validation.
 * @param variableName An optional variable name for descriptive error messages.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer to provide additional context or exceptions if validation fails.
 * @param cause An optional transformer for chaining a specific cause in the exception if validation fails.
 * @return The current `UIntRangeWithExclusions` object if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateNotContains(number: UInt, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified `number` does not fall within the current `UIntRangeWithExclusions` instance.
 * If the `number` is contained, an exception is thrown.
 *
 * @param number The unsigned integer to validate.
 * @param property Optional property reference associated with the validation context.
 * @param variable Optional variable reference associated with the validation context.
 * @param message Optional custom error message to use if validation fails.
 * @param causeOf Optional transformer to create a more specific cause of the exception if validation fails.
 * @param cause Optional transformer to define the underlying cause of the exception.
 * @return The same `UIntRangeWithExclusions` instance if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateNotContains(number: UInt, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified `number` is not contained within this range.
 * If the number is found within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to check against the range.
 * @param callable The function context in which the validation is being performed, can be null.
 * @param parameterName An optional name for the parameter being validated.
 * @param message A custom message to be included in the exception if validation fails.
 * @param causeOf An optional transformer to provide the underlying cause of the exception when validation fails.
 * @param cause Another optional transformer to customize the cause of the exception if validation fails.
 * @return The same `UIntRangeWithExclusions` instance upon successful validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateNotContains(number: UInt, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range.
 *
 * @param number The number to check against the range and exclusions.
 * @param callable The callable function, if applicable, associated with this validation. Can be null.
 * @param parameter The parameter, if applicable, related to this validation. Can be null.
 * @param message An optional custom error message to use when validation fails. If null, a default message is provided.
 * @param causeOf An optional transformer that provides a throwable cause for the validation failure. Can be null.
 * @param cause An optional transformer that provides additional throwable context for the validation failure. Can be null.
 * @return The current instance of `UIntRangeWithExclusions`.
 * @throws ValidationFailedException if the number is contained within the range or its exclusions.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateNotContains(number: UInt, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within the range with exclusions.
 * If the number is contained, an exception is thrown.
 *
 * @param number The number to check for absence within the range.
 * @param callableName The name of the callable that invoked the validation, used for error reporting.
 * @param parameterName Optional parameter name associated with the validation, used for error reporting.
 * @param message Optional custom message for the exception if validation fails.
 * @param causeOf Optional transformer to create a throwable cause for the exception based on the current range.
 * @param cause Optional transformer to provide a secondary cause for the exception.
 * @return The current `UIntRangeWithExclusions` instance if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateNotContains(number: UInt, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current `UIntRangeWithExclusions` instance.
 * If the number is contained, a `ValidationFailedException` is thrown.
 *
 * @param number The unsigned integer to check against the range.
 * @param callableName The name of the callable function being validated, can be null.
 * @param parameter The parameter of the callable associated with the validation, can be null.
 * @param message A custom error message to include in the exception, can be null.
 * @param causeOf A transformer used to generate the cause of the exception, can be null.
 * @param cause A transformer used to set the cause of the exception, can be null.
 * @return The current `UIntRangeWithExclusions` instance if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithExclusions.validateNotContains(number: UInt, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRangeWithExclusions, Throwable>? = null, cause: Transformer<UIntRangeWithExclusions, Throwable>? = null): UIntRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the range. If the number is within
 * the range, a validation exception is thrown.
 *
 * @param number The number to check against the range.
 * @param causeOf An optional transformer that generates a throwable cause when the validation fails.
 * @param cause An optional transformer that generates a throwable for additional context when the validation fails.
 * @return The same instance of [UIntRangeWithConditions] if validation passes.
 * @throws ValidationFailedException If the specified number is within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateNotContains(number: UInt, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained in the current range. If the number is found within the range,
 * an exception is thrown with details derived from the provided transformers and lazy message.
 *
 * @param number The number to check for exclusion from the range.
 * @param causeOf A transformer for generating a throwable cause when the validation fails. Can be null.
 * @param cause A transformer for generating an additional throwable cause when the validation fails. Can be null.
 * @param lazyMessage A transformer for generating a message to include in the exception when the validation fails.
 * @return The current range object, allowing for method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateNotContains(number: UInt, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null, lazyMessage: Transformer<UIntRangeWithConditions, Any>): UIntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified `UInt` number is not contained within the current range.
 * If the number is found within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The unsigned integer to be checked against the range.
 * @param property An optional property associated with the validation; can be null.
 * @param variableName An optional name of the variable being validated; can be null.
 * @param message An optional validation failure message; defaults to "contains $number" if null.
 * @param causeOf An optional transformer for producing the cause of the exception; can be null.
 * @param cause An optional transformer for wrapping the exception cause; can be null.
 * @return The current `UIntRangeWithConditions` instance if validation passes successfully.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateNotContains(number: UInt, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified range does not contain the given number. If the range contains the number,
 * an exception is thrown with the provided details.
 *
 * @param number The number to check against the range.
 * @param property The property related to the validation (may be null).
 * @param variable The variable associated with the validation (may be null).
 * @param message The custom error message to include if validation fails (optional).
 * @param causeOf A transformer to generate a throwable as the cause of the validation failure (optional).
 * @param cause A transformer to generate the direct cause of the validation failure (optional).
 * @return The current instance of `UIntRangeWithConditions` if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateNotContains(number: UInt, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range.
 * Throws an exception if the condition is not satisfied.
 *
 * @param number The unsigned integer to check for within the range.
 * @param callable The function reference that initiated the validation, or null if not applicable.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional message to include with the exception if validation fails.
 * @param causeOf A transformer to generate a throwable cause if validation fails, or null if not applicable.
 * @param cause An alternative transformer to generate a throwable cause if validation fails, or null if not applicable.
 * @return The current instance of `UIntRangeWithConditions`, allowing method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateNotContains(number: UInt, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given unsigned integer is not contained within the range.
 * If the specified number is found within the range, a validation failure exception is thrown.
 *
 * @param number The unsigned integer value that must not be contained in the range.
 * @param callable An optional reference to the calling function for error context.
 * @param parameter An optional parameter reference associated with the validation context.
 * @param message An optional custom message to include in the validation exception.
 * @param causeOf An optional transformer for generating a custom cause of the exception.
 * @param cause An optional transformer for providing an additional cause for the exception.
 * @return The current instance of UIntRangeWithConditions, provided validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateNotContains(number: UInt, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range. If the range contains
 * the number, a validation exception is thrown.
 *
 * @param number The unsigned integer to check against the range.
 * @param callableName The name of the callable to include as context in the exception message, if applicable.
 * @param parameterName The name of the parameter associated with the validation, or null if not applicable.
 * @param message The custom error message to use if validation fails, or null to use the default message.
 * @param causeOf A transformer that generates a throwable as the primary cause in case of validation failure.
 * @param cause A transformer that generates a throwable as the secondary cause in case of validation failure.
 * @return The original range (`this`) if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateNotContains(number: UInt, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within this range. If the number exists within the range,
 * an exception is thrown.
 *
 * @param number The number to validate.
 * @param callableName The name of the callable to be included in the exception, if triggered.
 * @param parameter The parameter associated with the callable, if applicable.
 * @param message An optional custom validation message.
 * @param causeOf A transformer that generates the root cause of the exception, if triggered.
 * @param cause A transformer to generate the underlying cause of the exception.
 * @return The current range instance, allowing for method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun UIntRangeWithConditions.validateNotContains(number: UInt, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<UIntRangeWithConditions, Throwable>? = null, cause: Transformer<UIntRangeWithConditions, Throwable>? = null): UIntRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the receiver `LongRange`.
 * If the number is found within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to check against the range.
 * @param causeOf An optional transformer to generate a specific cause for the exception using the receiver range.
 * @param cause An optional transformer to generate a specific cause for the exception using the receiver range.
 * @return The receiver `LongRange` if validation passes (i.e., the number is not in the range).
 * @throws ValidationFailedException If the number is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateNotContains(number: Long, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified `number` is not contained within this `LongRange`.
 * If the `number` is found within the range, an exception is thrown.
 *
 * @param number The number to validate against the `LongRange`.
 * @param causeOf An optional transformer function that produces a custom exception to be thrown
 * when the validation fails. Defaults to null.
 * @param cause An optional transformer function to produce the underlying cause of the exception.
 * Defaults to null.
 * @param lazyMessage A transformer function used to generate the exception message lazily
 * when the validation fails.
 * @return The original `LongRange` if validation passes.
 * @throws ValidationFailedException If the `number` is within the `LongRange`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateNotContains(number: Long, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null, lazyMessage: Transformer<LongRange, Any>): LongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not within the range of this `LongRange`.
 * If the number is contained within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The numeric value to validate against the range.
 * @param property The property associated with the validation failure, if applicable. Can be null.
 * @param variableName The optional name of the variable involved in the validation. Defaults to null.
 * @param message An optional descriptive message to include in the exception when validation fails. Defaults to null.
 * @param causeOf A transformer responsible for generating the cause exception
 *                when validation fails. Defaults to null.
 * @param cause A transformer responsible for providing additional exception context
 *              when validation fails. Defaults to null.
 * @return The current range (`this`) if the validation passes.
 * @throws ValidationFailedException If the specified number is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateNotContains(number: Long, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within this [LongRange]. If the number is found,
 * a [ValidationFailedException] is thrown.
 *
 * @param number the number to check for containment within the range
 * @param property the primary property associated with the validation, or null if not specified
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional custom message to include with the validation failure, or null for a default message
 * @param causeOf an optional transformer to generate a cause [Throwable] if validation fails, or null for no transformer
 * @param cause an optional transformer to generate a secondary cause [Throwable], or null for no transformer
 * @return the original [LongRange] if validation succeeds
 * @throws ValidationFailedException if the specified number is within this range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateNotContains(number: Long, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not within the range of the current `LongRange` instance.
 * If the number is within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to check against the current `LongRange`.
 * @param callable An optional Kotlin function (`KFunction`) related to the validation context.
 * @param parameterName An optional name of the parameter involved in the validation.
 * @param message An optional custom message to describe the validation failure.
 * @param causeOf An optional transformer function used to determine the cause of the exception, if applicable.
 * @param cause An optional transformer function to provide additional details about the failure cause.
 * @return The current `LongRange` instance if validation passes.
 * @throws ValidationFailedException If the specified number is contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateNotContains(number: Long, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not within the range represented by this [LongRange].
 * If the number is found within the range, a [ValidationFailedException] is thrown.
 *
 * @param number the number to check for exclusion from the range
 * @param callable an optional [KFunction] related to the validation, providing context for the failure
 * @param parameter an optional [KParameter] representing the parameter involved in the validation, providing additional context
 * @param message an optional custom error message used when validation fails
 * @param causeOf an optional transformer for generating a custom exception based on the current range when a validation failure occurs
 * @param cause an optional transformer for wrapping an underlying cause as part of the exception
 * @return the original [LongRange] if the validation passes
 * @throws ValidationFailedException if the specified [number] is found within the range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateNotContains(number: Long, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not within the current LongRange. If the number is found within
 * the range, a ValidationFailedException is thrown.
 *
 * @param number The number to check against the LongRange.
 * @param callableName The name of the callable (e.g., function or method) associated with the validation failure.
 *                     Can be null if the callable name is not applicable.
 * @param parameterName The name of the parameter that caused the validation failure. Optional, can be null.
 * @param message An optional custom message providing additional details about the validation failure. Defaults to
 *                "contains <number>" if not specified.
 * @param causeOf A transformer for the LongRange that generates a custom Throwable to use as the cause if
 *                validation fails. Can be null.
 * @param cause A transformer for the LongRange generating the underlying Throwable cause for the exception.
 *              Can be null.
 * @return The current LongRange instance if the validation passes (i.e., the number is not in the range).
 * @throws ValidationFailedException if the number is found within the range, with details about the validation failure.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateNotContains(number: Long, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within this [LongRange].
 * If the number is found within the range, a [ValidationFailedException] is thrown.
 *
 * @param number The number to check for inclusion in the range.
 * @param callableName The name of the callable (e.g., function or property) where the validation is performed, or null if not specified.
 * @param parameter The [KParameter] instance associated with the validation context, or null if not applicable.
 * @param message An optional error message to include in the exception if validation fails, or null for a default message.
 * @param causeOf A transformer for creating a custom exception cause that wraps the validation failure, or null if not provided.
 * @param cause A transformer for providing an optional cause for the validation failure, or null if no cause is specified.
 * @return The original [LongRange] if the validation passes.
 * @throws ValidationFailedException If the number is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRange.validateNotContains(number: Long, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRange, Throwable>? = null, cause: Transformer<LongRange, Throwable>? = null): LongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the range, throwing an exception if the validation fails.
 *
 * @param number The number to be validated against the range.
 * @param causeOf A transformer function that generates a custom exception based on the range, or null for default behavior.
 * @param cause A transformer function that generates a custom cause for the exception, or null for no additional cause.
 * @return The original instance of the range if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateNotContains(number: Long, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current LongRangeWithExclusions instance.
 * If the number is found within the range, it throws a ValidationFailedException with the provided message and optional cause.
 *
 * @param number The number to check against the range.
 * @param causeOf An optional transformer to generate a throwable cause if the validation fails.
 * @param cause An optional transformer to generate another throwable cause if the validation fails.
 * @param lazyMessage A transformer to generate the error message lazily based on the current LongRangeWithExclusions.
 * @return The current LongRangeWithExclusions instance if the validation passes.
 * @throws ValidationFailedException if the specified number is contained in the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateNotContains(number: Long, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null, lazyMessage: Transformer<LongRangeWithExclusions, Any>): LongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range.
 * If the number is found within the range, a validation exception is thrown.
 *
 * @param number The number to check against the current range.
 * @param property The optional property associated with the validation context.
 * @param variableName The optional name of the variable being validated.
 * @param message An optional custom error message for the validation exception.
 * @param causeOf An optional transformer to create the root cause of the validation exception.
 * @param cause An optional transformer to create the additional cause of the validation exception.
 * @return The current LongRangeWithExclusions instance if validation passes successfully.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateNotContains(number: Long, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range.
 *
 * @param number The number to check for containment within the range.
 * @param property The property being validated, associated with the value, or null if irrelevant.
 * @param variable The variable being validated, related to the value, or null if not applicable.
 * @param message An optional custom message to include within the exception if validation fails.
 * @param causeOf A transformer that generates a throwable to explain the cause of validation failure, or null.
 * @param cause A transformer that generates a throwable as a secondary cause of validation failure, or null.
 * @return The original instance of LongRangeWithExclusions if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateNotContains(number: Long, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within the current range, including specified exclusions.
 * If the validation fails, an exception is thrown.
 *
 * @param number The number to check against the range and exclusions.
 * @param callable An optional reference to the callable function for additional context in exception handling.
 * @param parameterName An optional name of the parameter being validated for enhanced error reporting.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf An optional transformer to customize the exception behavior if the validation fails.
 * @param cause An optional secondary transformer that decorates the exception cause if validation fails.
 * @return The current `LongRangeWithExclusions` instance if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateNotContains(number: Long, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within this LongRangeWithExclusions instance.
 * If the number is found within the range, a ValidationFailedException is thrown.
 *
 * @param number The number to validate, ensuring it is not within the range or exclusions.
 * @param callable The callable context (function/method) for which this validation applies. Can be null.
 * @param parameter The parameter involved in the validation. Can be null.
 * @param message An optional custom message for the validation exception. Defaults to null.
 * @param causeOf An optional transformer to generate a throwable that indicates the cause of the validation failure. Defaults to null.
 * @param cause An optional transformer to define the underlying cause of the validation exception. Defaults to null.
 * @return The current LongRangeWithExclusions instance if validation passes without throwing an exception.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateNotContains(number: Long, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within this range. If the number is found in the range,
 * a ValidationFailedException is thrown with the provided configuration.
 *
 * @param number The number to check for exclusion in this range.
 * @param callableName The name of the callable (function or method) performing the validation, or null if not applicable.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional message to include in the exception if validation fails. Defaults to a message indicating the number is contained in the range.
 * @param causeOf A transformer function to generate the cause of the validation exception, or null if not applicable.
 * @param cause A transformer function to generate an exception to set as the cause of the ValidationFailedException.
 * @return The same `LongRangeWithExclusions` instance if validation passes.
 * @throws ValidationFailedException If the number is found within this range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateNotContains(number: Long, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained in the current range with exclusions.
 * If the number is contained, an exception will be thrown with the provided details.
 *
 * @param number The number to check against the range with exclusions.
 * @param callableName The name of the callable associated with this validation, or null if not applicable.
 * @param parameter The parameter related to the validation, or null if not applicable.
 * @param message An optional custom error message to describe the validation failure.
 * @param causeOf A transformer function to produce the root cause throwable for the validation failure, or null if not applicable.
 * @param cause A transformer function to produce the secondary cause throwable for the validation failure, or null if not applicable.
 * @return The original instance of LongRangeWithExclusions if the number is not in the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithExclusions.validateNotContains(number: Long, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRangeWithExclusions, Throwable>? = null, cause: Transformer<LongRangeWithExclusions, Throwable>? = null): LongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified `number` is not within the current range. If the `number` is found within the range,
 * an exception is thrown. The exception message and cause can be customized using the provided transformers.
 *
 * @param number The number to validate against the current range.
 * @param causeOf Optional transformer to generate the exception if the `number` is within the range.
 * @param cause Optional transformer to generate the cause of the exception.
 * @return The current range if the validation passes successfully.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateNotContains(number: Long, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within the range represented by this instance.
 * If the number is within the range, an exception is thrown based on the provided transformers for cause or a lazy message.
 *
 * @param number The number to validate against the range.
 * @param causeOf An optional transformer to generate the throwable cause if the number is within the range.
 * @param cause An optional transformer to produce the throwable to be used as the root cause.
 * @param lazyMessage A transformer to generate the error message if validation fails.
 * @return The current instance of [LongRangeWithConditions] if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateNotContains(number: Long, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null, lazyMessage: Transformer<LongRangeWithConditions, Any>): LongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not within the range defined by this LongRangeWithConditions instance.
 * If the number is found within the range, a ValidationFailedException is thrown.
 *
 * @param number The number to validate against the range.
 * @param property An optional KProperty reference associated with this validation.
 * @param variableName An optional string representing the variable name being validated.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf An optional transformer to handle the cause of the validation failure exception.
 * @param cause An optional transformer to create additional context for the validation failure exception.
 * @return The current LongRangeWithConditions instance to allow for method chaining.
 * @throws ValidationFailedException If the specified number is within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateNotContains(number: Long, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the specified number is not contained within the current range.
 * If the number is found within the range, a validation exception is thrown.
 *
 * @param number The number to check for exclusion from the range.
 * @param property The optional property metadata describing the range being validated.
 * @param variable The optional property metadata describing the input variable being validated.
 * @param message An optional custom message to include in the validation exception.
 * @param causeOf An optional transformer to provide a custom throwable cause if validation fails.
 * @param cause An optional transformer to include an additional cause for the validation failure.
 * @return The current instance of LongRangeWithConditions for method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateNotContains(number: Long, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the `LongRangeWithConditions`.
 * If the number is found within the range, a validation exception is thrown.
 *
 * @param number The number to check against the range.
 * @param callable The callable function or method where the validation is performed. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message The custom validation message. Defaults to "contains [number]" if not provided.
 * @param causeOf A transformer that generates the root cause exception based on the current range. Can be null.
 * @param cause A transformer that generates the exception cause based on the current range. Can be null.
 * @return The original `LongRangeWithConditions` instance if the number is not contained in the range.
 * @throws ValidationFailedException If the number is contained in the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateNotContains(number: Long, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range.
 * If the number is found in the range, an exception is thrown.
 *
 * @param number The number to validate against the range.
 * @param callable The function or method associated with the validation.
 * @param parameter The parameter being validated, if applicable.
 * @param message An optional custom message for the exception.
 * @param causeOf An optional transformer for generating a specific exception from the current range.
 * @param cause An optional transformer for generating the underlying cause for the exception.
 * @return The current instance of `LongRangeWithConditions` if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateNotContains(number: Long, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified [number] is not contained within the range. Throws a [ValidationFailedException]
 * if the number is contained in the range.
 *
 * @param number The number to check against the current range.
 * @param callableName The name of the calling function, used to provide context in the exception message. Optional.
 * @param parameterName The name of the parameter being validated, used to provide context in the exception message. Optional.
 * @param message A custom message to include in the exception if validation fails. Defaults to "contains [number]". Optional.
 * @param causeOf A transformer function to provide a custom throwable representing the root cause of the exception. Optional.
 * @param cause A transformer function to provide a custom throwable as the specific cause of the exception. Optional.
 * @return The current [LongRangeWithConditions] instance, allowing for method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateNotContains(number: Long, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given `number` is not contained within the range represented by this `LongRangeWithConditions`.
 * If the number is found within the range, an exception is thrown.
 *
 * @param number The `Long` number to check for exclusion from this range.
 * @param callableName The name of the callable function or property being validated, or `null` if not applicable.
 * @param parameter An optional `KParameter` related to the validation, or `null` if not applicable.
 * @param message An optional custom error message to include in the exception, or `null` to use the default message.
 * @param causeOf An optional transformer function that produces a throwable for the root cause, or `null`.
 * @param cause An optional transformer function that produces a throwable for an additional context cause, or `null`.
 * @return The current instance of `LongRangeWithConditions` for method chaining.
 * @throws ValidationFailedException If the `number` is found within this range, with an optional message and cause.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun LongRangeWithConditions.validateNotContains(number: Long, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<LongRangeWithConditions, Throwable>? = null, cause: Transformer<LongRangeWithConditions, Throwable>? = null): LongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified `number` is not contained within the current `ULongRange`.
 * If the `number` is contained within the range, a `ValidationFailedException` is thrown.
 * An optional `causeOf` transformer can be provided to generate a throwable
 * based on the current range for customized error handling. Additionally,
 * a `cause` transformer can be supplied to generate a cause for the exception.
 *
 * @param number The unsigned long value to check against the range.
 * @param causeOf An optional transformer to create a throwable based on the current range,
 *                used as the primary exception when validation fails.
 * @param cause An optional transformer to create a throwable based on the current range,
 *              used as the cause of the `ValidationFailedException`.
 * @return The current `ULongRange` instance if validation passes.
 * @throws ValidationFailedException If the specified `number` is within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateNotContains(number: ULong, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the provided `number` is not contained within the `ULongRange`.
 * If the `number` is within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The unsigned long number to validate against the range.
 * @param causeOf A transformer to provide a custom exception derived from the range, or null.
 * @param cause A transformer to provide the cause of the exception, or null.
 * @param lazyMessage A transformer to generate an exception message lazily using the range.
 * @return The original `ULongRange` if the validation passes.
 * @throws ValidationFailedException If the `number` is contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateNotContains(number: ULong, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null, lazyMessage: Transformer<ULongRange, Any>): ULongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified `ULong` number is not contained within the current `ULongRange`.
 * If the number is found within the range, throws a `ValidationFailedException` with an optional
 * custom message or cause.
 *
 * @param number The `ULong` number to check for absence in the range.
 * @param property The property associated with the validation, used for contextual exception messages. Can be null.
 * @param variableName An optional name of the variable to use in the exception message. Defaults to null.
 * @param message An optional custom message to include in the validation exception. Defaults to null.
 * @param causeOf A transformer to generate a throwable cause when the validation fails. Defaults to null.
 * @param cause An additional transformer to provide a throwable cause. Defaults to null.
 * @return The same `ULongRange` instance if the validation passes.
 * @throws ValidationFailedException if the `number` is within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateNotContains(number: ULong, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given `number` does not exist within this `ULongRange`.
 * If the `number` is found within the range, a `ValidationFailedException` is thrown.
 *
 * @param number the unsigned long number to check for absence in the range
 * @param property the main property associated with this validation, or null if not specified
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional custom error message to include in the exception, or null for the default message
 * @param causeOf an optional transformer for generating a specific throwable cause if validation fails, or null
 * @param cause an optional transformer for generating additional context in the throwable cause, or null
 * @return the original `ULongRange` if validation succeeds
 * @throws ValidationFailedException if the specified `number` is found within the range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateNotContains(number: ULong, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given `ULong` number is not contained within the current `ULongRange`.
 * If the validation fails, an exception is thrown based on the provided configuration.
 *
 * @param number The `ULong` number to check for presence in the range.
 * @param callable An optional `KFunction` instance associated with the validation, used for error context.
 * @param parameterName An optional name of the parameter, for inclusion in the exception message.
 * @param message An optional custom error message to describe the validation failure.
 * @param causeOf An optional transformer to generate the root cause exception based on the range. Defaults to `null`.
 * @param cause An optional transformer to generate the secondary cause exception based on the range. Defaults to `null`.
 * @return The current `ULongRange` instance, if the validation passes.
 * @throws ValidationFailedException If the range contains the specified `number`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateNotContains(number: ULong, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that a given [ULong] is not contained within the [ULongRange]. If the [number] is found in the range, an exception is thrown.
 *
 * @param number the unsigned long number to check for presence in the range
 * @param callable the [KFunction] associated with the validation, or `null` if not applicable
 * @param parameter the [KParameter] representing the parameter involved in the validation, or `null` if not applicable
 * @param message an optional custom error message if validation fails, or `null` for a default message
 * @param causeOf a transformation function for generating a more specific [Throwable] as the root cause if the validation fails
 * @param cause a transformation function for generating a secondary [Throwable] as the cause if the validation fails
 * @return the original [ULongRange] if validation passes
 * @throws ValidationFailedException if the [number] is contained within the [ULongRange]
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateNotContains(number: ULong, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified `ULong` number is not contained within the `ULongRange`.
 * If the number is found within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The `ULong` number to validate against the range.
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName The name of the parameter being validated, or `null` if unspecified.
 * @param message An optional custom error message to describe the validation failure, or `null` to use a default message.
 * @param causeOf An optional transformer that generates the exception to throw based on the failed `ULongRange`, or `null`.
 * @param cause An optional transformer to provide the root cause of the exception, or `null` if no cause is specified.
 * @return The original `ULongRange` if the validation passes (i.e., the number is not contained in the range).
 * @throws ValidationFailedException If the number is contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateNotContains(number: ULong, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the provided [ULong] number is not within this [ULongRange].
 * If the number is found within the range, a [ValidationFailedException] is thrown.
 *
 * @param number The [ULong] number to check against the range.
 * @param callableName The name of the callable where the validation is being performed, or null if not specified.
 * @param parameter The [KParameter] representing the parameter related to the validation, or null if not applicable.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf An optional transformer function for generating a specific [Throwable] as the primary cause of the exception.
 * @param cause An optional transformer function for generating an additional cause for the exception.
 * @return The original [ULongRange] if the validation passes successfully (i.e., the range does not contain the number).
 * @throws ValidationFailedException if the provided number is within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRange.validateNotContains(number: ULong, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRange, Throwable>? = null, cause: Transformer<ULongRange, Throwable>? = null): ULongRange {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range.
 * If the number is found within the range, a validation exception is thrown.
 *
 * @param number The unsigned long number that should not be present in the range.
 * @param causeOf Optional transformer responsible for generating a throwable cause when the validation fails.
 * @param cause Optional transformer for generating an additional throwable cause when the validation fails.
 * @return The current instance of `ULongRangeWithExclusions` if the validation passes.
 * @throws ValidationFailedException If the specified number is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateNotContains(number: ULong, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within the current range. If the number is found in the range,
 * a validation exception is thrown with the specified message and optional cause transformers.
 *
 * @param number The number to check for non-inclusion in the range.
 * @param causeOf A transformer that generates a throwable as the primary cause of the exception, or null if not used.
 * @param cause A transformer that generates a throwable as an additional cause of the exception, or null if not used.
 * @param lazyMessage A transformer that generates a lazily evaluated message for the validation exception.
 * @return The current range, allowing for method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateNotContains(number: ULong, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null, lazyMessage: Transformer<ULongRangeWithExclusions, Any>): ULongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current `ULongRangeWithExclusions` instance.
 * If the number is found within the range, a `ValidationFailedException` is thrown.
 *
 * @param number The unsigned long number to check for exclusion.
 * @param property An optional property reference associated with the validation context.
 * @param variableName An optional name of the variable being validated.
 * @param message An optional custom error message to describe the validation failure.
 * @param causeOf An optional transformer to produce the root cause of the exception.
 * @param cause An optional transformer to create a throwable representing the specific cause.
 * @return The current `ULongRangeWithExclusions` instance if validation passes.
 * @throws ValidationFailedException If the number is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateNotContains(number: ULong, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current ULongRangeWithExclusions instance.
 * If the number is contained, a ValidationFailedException is thrown.
 *
 * @param number The unsigned long number to check for absence within the ULongRangeWithExclusions instance.
 * @param property The property associated with the validation, can be null.
 * @param variable The variable associated with the validation, can be null.
 * @param message An optional custom message for the exception if validation fails, can be null.
 * @param causeOf An optional transformer providing a throwable cause for the exception if validation fails, can be null.
 * @param cause An optional additional transformer for the throwable cause of the exception, can be null.
 * @return The current ULongRangeWithExclusions instance if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateNotContains(number: ULong, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained in the range with exclusions.
 *
 * @param number The number to validate.
 * @param callable The callable being validated, if applicable.
 * @param parameterName The name of the parameter being validated, if provided.
 * @param message An optional custom message to include in the validation exception.
 * @param causeOf A transformer that generates the cause of the exception from the current range, if applicable.
 * @param cause A transformer that generates the underlying exception reason from the current range, if applicable.
 * @return The current ULongRangeWithExclusions instance if the validation passes.
 * @throws ValidationFailedException if the number is contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateNotContains(number: ULong, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the current range with exclusions.
 * Throws a ValidationFailedException if the number is found within the range.
 *
 * @param number The unsigned long number to validate against the range.
 * @param callable The callable function being validated, if applicable.
 * @param parameter The specific parameter being validated, or null if not applicable.
 * @param message An optional custom message for the exception if validation fails.
 * @param causeOf An optional transformer to generate the exception cause from the current range, if validation fails.
 * @param cause An optional transformer to generate the root cause exception from the current range, if validation fails.
 * @return The current instance of ULongRangeWithExclusions if validation is successful.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateNotContains(number: ULong, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the range with exclusions.
 * If the number is found within the range, a validation exception is thrown.
 *
 * @param number The number to check for presence within the range.
 * @param callableName The name of the callable function for error context, or null if not applicable.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom error message to use in the exception.
 * @param causeOf An optional transformer that produces a throwable cause using the current state of the range.
 * @param cause An optional transformer that produces a throwable cause for creating the exception.
 * @return The ULongRangeWithExclusions instance after validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateNotContains(number: ULong, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within the current range with exclusions.
 * If the number is found within the range, an exception is thrown.
 *
 * @param number The number to validate against the range.
 * @param callableName The name of the callable to be included in the exception, if any.
 * @param parameter The parameter related to the number being validated, if any.
 * @param message An optional custom error message to be used in case of validation failure.
 * @param causeOf A transformer function that provides a throwable instance to be used as the cause of the exception.
 * @param cause A transformer function that provides a throwable instance to be combined with the exception.
 * @return The current `ULongRangeWithExclusions` instance if validation is successful.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithExclusions.validateNotContains(number: ULong, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRangeWithExclusions, Throwable>? = null, cause: Transformer<ULongRangeWithExclusions, Throwable>? = null): ULongRangeWithExclusions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the range. If the number is found
 * within the range, a validation error is thrown with an optional cause.
 *
 * @param number The number to validate against the range.
 * @param causeOf A transformer that generates a throwable cause when the validation fails. This parameter is optional.
 * @param cause A transformer that generates a throwable for an additional cause when the validation fails. This parameter is optional.
 * @return The same instance of [ULongRangeWithConditions] if the validation succeeds.
 * @throws ValidationFailedException If the specified number is found within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateNotContains(number: ULong, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException("$number is in the range.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$number is in the range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within the range. If the number exists in the range,
 * an exception is thrown using the provided transformers for customization of the error cause and message.
 *
 * @param number The unsigned long value to check for inclusion in the range.
 * @param causeOf A transformer function that, when provided, generates a throwable cause related to the validation failure.
 * @param cause An optional transformer function that generates a throwable cause related to the validation failure.
 * @param lazyMessage A transformer function used to generate a custom message for the validation failure.
 * @return The current ULongRangeWithConditions instance if the validation passes without any exception.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateNotContains(number: ULong, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null, lazyMessage: Transformer<ULongRangeWithConditions, Any>): ULongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within this range. If the number is found
 * within the range, a validation exception will be thrown.
 *
 * @param number The number to validate against the range.
 * @param property The property associated with the range for validation purposes. Can be null.
 * @param variableName The name of the variable being validated. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Can be null.
 * @param causeOf An optional transformer that provides a throwable cause if validation fails. Can be null.
 * @param cause An optional transformer that provides a throwable to wrap if validation fails. Can be null.
 * @return The current instance of [ULongRangeWithConditions] if the validation passes.
 * @throws ValidationFailedException If the specified number is contained within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateNotContains(number: ULong, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within the range.
 * If the number is within the range, an exception is thrown.
 *
 * @param number The number to validate against the range.
 * @param property Optional property reference associated with the validation.
 * @param variable Optional variable reference associated with the validation.
 * @param message Optional error message to include if validation fails.
 * @param causeOf Optional transformer to create a cause exception for a failed validation.
 * @param cause Optional transformer to create a deeper cause exception for the validation failure.
 * @return The current instance of `ULongRangeWithConditions` for method chaining.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateNotContains(number: ULong, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given number is not contained within the range. If the validation fails,
 * an exception is thrown.
 *
 * @param number The unsigned long number to check for presence in the range.
 * @param callable An optional Kotlin function reference associated with the validation.
 * @param parameterName An optional name of the parameter being validated.
 * @param message An optional custom message for the validation failure.
 * @param causeOf An optional transformer used to generate a throwable cause when the validation fails.
 * @param cause An optional transformer used to provide an additional cause for the validation failure.
 * @return The current range instance on successful validation.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateNotContains(number: ULong, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the specified number is not contained within this range. If the number is found
 * in the range, a `ValidationFailedException` is thrown.
 *
 * @param number The number to check against this range.
 * @param callable An optional reference to the function where this validation is applied.
 * @param parameter An optional parameter reference associated with the callable.
 * @param message An optional custom message to include in the thrown exception if validation fails.
 * @param causeOf An optional transformer used to generate a specific cause for the exception.
 * @param cause An optional transformer used as the primary cause of the validation failure.
 * @return The current instance of `ULongRangeWithConditions` if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateNotContains(number: ULong, callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given range does not contain the specified number. If the number is found within the range,
 * a validation exception is thrown with an optional message, parameter name, and callable information.
 *
 * @param number The number to check for presence in the range.
 * @param callableName The name of the callable invoking the validation (useful for error reporting).
 * @param parameterName The name of the parameter involved in the validation (optional).
 * @param message The custom message to include in the validation error if the condition fails (optional).
 * @param causeOf A transformer to produce a throwable cause if the condition fails (optional).
 * @param cause Another transformer to provide an additional throwable cause if the condition fails (optional).
 * @return The current range object (`ULongRangeWithConditions`) if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateNotContains(number: ULong, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "contains $number", cause?.invoke(this)))
    return this
}
/**
 * Validates that the range does not contain the specified number. If the number is found in the range,
 * a `ValidationFailedException` is thrown with the provided message or default error message.
 *
 * @param number The number to check for absence in the range.
 * @param callableName Optional name of the callable that triggered the validation.
 * @param parameter Optional parameter related to the validation check.
 * @param message Optional custom error message to use if the validation fails.
 * @param causeOf Optional transformer to generate the root cause of the exception.
 * @param cause Optional transformer to generate the cause for the exception.
 * @return The same instance of `ULongRangeWithConditions` if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun ULongRangeWithConditions.validateNotContains(number: ULong, callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Transformer<ULongRangeWithConditions, Throwable>? = null, cause: Transformer<ULongRangeWithConditions, Throwable>? = null): ULongRangeWithConditions {
    if (number in this) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "contains $number", cause?.invoke(this)))
    return this
}