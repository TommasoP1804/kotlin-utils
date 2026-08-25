/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("TemporalValidatorsKt")
@file:Since("5.0.0")
@file:Suppress("unused")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.time.*
import dev.tommasop1804.kutils.exceptions.*
import java.time.*
import java.time.temporal.Temporal
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

@PublishedApi
internal fun <T> T.checkEquality(other: T): Boolean where T : Comparable<T>, T : Temporal = when(this) {
    is LocalDate -> isEqual(other as LocalDate)
    is OffsetTime -> isEqual(other as OffsetTime)
    is LocalDateTime -> isEqual(other as LocalDateTime)
    is OffsetDateTime -> isEqual(other as OffsetDateTime)
    is ZonedDateTime -> isEqual(other as ZonedDateTime)
    is LocalMonthDayTime -> isEqual(other as LocalMonthDayTime, firstLeap = false, secondLeap = false)
    is OffsetMonthDayTime -> isEqual(other as OffsetMonthDayTime, firstLeap = false, secondLeap = false)
    is ZonedMonthDayTime -> isEqual(other as ZonedMonthDayTime, firstLeap = false, secondLeap = false)
    else -> equals(other)
}
@PublishedApi
internal fun <T> T.checkIsBefore(other: T): Boolean where T : Comparable<T>, T : Temporal = when(this) {
    is LocalDate -> isBefore(other as LocalDate)
    is LocalTime -> isBefore(other as LocalTime)
    is OffsetTime -> isBefore(other as OffsetTime)
    is LocalDateTime -> isBefore(other as LocalDateTime)
    is OffsetDateTime -> isBefore(other as OffsetDateTime)
    is ZonedDateTime -> isBefore(other as ZonedDateTime)
    is LocalMonthDayTime -> isBefore(other as LocalMonthDayTime, firstLeap = false, secondLeap = false)
    is OffsetMonthDayTime -> isBefore(other as OffsetMonthDayTime, firstLeap = false, secondLeap = false)
    is ZonedMonthDayTime -> isBefore(other as ZonedMonthDayTime, firstLeap = false, secondLeap = false)
    else -> this < other
}
@PublishedApi
internal fun <T> T.checkIsAfter(other: T): Boolean where T : Comparable<T>, T : Temporal = when(this) {
    is LocalDate -> isAfter(other as LocalDate)
    is LocalTime -> isAfter(other as LocalTime)
    is OffsetTime -> isAfter(other as OffsetTime)
    is LocalDateTime -> isAfter(other as LocalDateTime)
    is OffsetDateTime -> isAfter(other as OffsetDateTime)
    is ZonedDateTime -> isAfter(other as ZonedDateTime)
    is LocalMonthDayTime -> isAfter(other as LocalMonthDayTime, firstLeap = false, secondLeap = false)
    is OffsetMonthDayTime -> isAfter(other as OffsetMonthDayTime, firstLeap = false, secondLeap = false)
    is ZonedMonthDayTime -> isAfter(other as ZonedMonthDayTime, firstLeap = false, secondLeap = false)
    else -> this > other
}

/**
 * Validates that the current temporal object is strictly before the specified [other] temporal object.
 * If the validation fails, it throws a [ValidationFailedException] with an optional custom cause.
 *
 * @param other The temporal object to compare against. The current object must be strictly before this value.
 * @param causeOf An optional transformer function to generate a custom cause for the thrown exception
 *                based on the current object. If null, it will not be used in exception generation.
 * @param cause An optional transformer function to generate a secondary cause for the thrown exception
 *              based on the current object.
 * @return The current object if validation passes, allowing for method chaining.
 * @throws ValidationFailedException If the current object is not strictly before [other].
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateBefore(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(
        "Temporal $this is not before $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException("Temporal $this is not before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is less than a specified `other` object.
 * If the current object is greater than or equal to `other`, a validation exception is thrown.
 *
 * @param other The other object to compare against.
 * @param causeOf An optional transformer that, when provided, determines the cause of the exception to be thrown.
 * @param cause An optional transformer responsible for generating an additional cause message for the exception.
 * @param lazyMessage A transformer that generates a message for the exception when validation fails.
 * @return The current object if validation passes.
 * @throws ValidationFailedException If the current object is greater than or equal to `other`.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateBefore(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T where T : Comparable<T>, T : Temporal {
    if (!checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is strictly before the specified `other` object.
 * If this object is greater than or equal to `other`, a `ValidationFailedException` is thrown.
 *
 * @param other The reference object to compare with. Validation succeeds only if the current object is strictly before this.
 * @param property An optional property reference used for generating detailed error messages in case of validation failure.
 * @param variableName An optional variable name to be included in the validation failure message for better context.
 * @param message An optional custom message to include in the validation failure exception.
 * @param causeOf An optional transformer to generate a throwable cause for the `ValidationFailedException`.
 *                Applied when the object does not pass the validation.
 * @param cause An optional transformer to provide a secondary throwable cause for the `ValidationFailedException`.
 * @return The current object if validation succeeds.
 * @throws ValidationFailedException if this object is not strictly before the specified `other` object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateBefore(other: T, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not before $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current instance is strictly before the specified `other` instance.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param other The instance to compare against.
 * @param property The primary property associated with the validation, or null if not specified.
 * @param variable An optional secondary property providing additional context, or null if not specified.
 * @param message A custom message to include in the exception if validation fails, or null to use the default message.
 * @param causeOf A transformer function to generate the root cause exception, or null if not specified.
 * @param cause A transformer function to generate a cause for the `ValidationFailedException`, or null if not specified.
 * @return The current instance if the validation passes.
 * @throws ValidationFailedException If the current instance is not before the `other` instance.
 */
@IgnorableReturnValue
fun <T> T.validateBefore(other: T, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not before $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current instance of type `T` is strictly before the specified `other` value.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * This method supports validation for types that are both `Comparable` and `Temporal`.
 *
 * @param other The value to compare against. The current instance should be strictly less than this value.
 * @param callable An optional reference to the function (`KFunction`) that is performing the validation.
 * @param parameterName The optional name of the parameter being validated. Used for more detailed error reporting.
 * @param message An optional custom message that will be included in the exception if validation fails.
 * @param causeOf An optional transformer that derives a specific `Throwable` cause from the current value if validation fails.
 * @param cause An optional transformer that derives a specific `Throwable` cause from the current value if validation fails.
 * @return The current instance of type `T` if validation succeeds.
 * @throws ValidationFailedException if the current instance is not strictly before the `other` value.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateBefore(other: T, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not before $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current [Comparable] and [Temporal] object is strictly earlier than the specified [other] object.
 * If the validation fails, it throws a [ValidationFailedException].
 *
 * @param other The value to compare against.
 * @param callable An optional [KFunction] representing the function context of the validation.
 * @param parameter An optional [KParameter] that corresponds to the parameter being validated.
 * @param message An optional message providing additional context about the validation failure.
 * @param causeOf An optional transformer that generates the underlying cause of type [Throwable] based on the current value.
 * @param cause An optional transformer that generates the underlying cause of type [Throwable] based on the current value.
 * @return The validated object if it satisfies the validation criteria.
 * @throws ValidationFailedException If the current object is not earlier than [other].
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateBefore(other: T, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(
        callable,
        parameter,
        message ?: "is not before $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is strictly before another given object, based on their comparison rules.
 * If the validation fails, a `ValidationFailedException` is thrown with an optional custom message and cause.
 *
 * @param other The object to compare against. Validation ensures that the current object is strictly before this object.
 * @param callableName The name of the callable (e.g., function or method) related to the validation. Can be null.
 * @param parameterName The name of the parameter that caused the validation failure. Can be null.
 * @param message An optional custom message providing additional context or explanation for the validation failure. Defaults to "is not before {other}".
 * @param causeOf An optional transformer function to generate a custom exception as the root cause of the validation failure, using the current object.
 * @param cause An optional transformer function to generate a supplementary cause exception for the validation failure, using the current object.
 * @return The original object if the validation passes.
 * @throws ValidationFailedException If the current object is not strictly before the specified `other` object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateBefore(other: T, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not before $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is strictly before the specified `other` value according to both
 * its chronological order and its comparable relationship. If the validation fails, an exception is thrown.
 *
 * @param other The value to compare against. The current value must be strictly before this value.
 * @param callableName The name of the callable (e.g., function or property) where validation is performed, or null if not specified.
 * @param parameter The KParameter instance representing the parameter involved in the validation, or null if not applicable.
 * @param message An optional error message providing additional details about the validation failure.
 * @param causeOf An optional transformer that generates a custom exception from the current value when validation fails.
 * @param cause An optional transformer that generates the underlying cause (Throwable) from the current value when validation fails.
 * @return The current value if it successfully passes validation.
 * @throws ValidationFailedException If the current value is not strictly before the given `other` value.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateBefore(other: T, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not before $other", cause?.invoke(this)) else causeOf(this).initCause(
        ValidationFailedException(callableName, parameter, message ?: "is not before $other", cause?.invoke(this))
    )
    return this
}
/**
 * Validates that the current temporal value is strictly after the provided temporal value. If the validation fails,
 * a [ValidationFailedException] is thrown with an optional cause or cause transformer.
 *
 * @param other The temporal value that the current value is validated against. The current value must be after this.
 * @param causeOf A transformer function that generates a [Throwable] cause based on the current value if the validation fails. Defaults to `null`.
 * @param cause An alternative transformer function that generates a [Throwable] based on the current value if the validation fails. Defaults to `null`.
 * @return The current temporal value if the validation passes.
 * @throws ValidationFailedException If the current temporal value is not strictly after the provided `other` value.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateAfter(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(
        "Temporal $this is not after $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException("Temporal $this is not after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking `Comparable` and `Temporal` object is strictly greater than the specified `other` object.
 * If the validation fails, a `ValidationFailedException` is thrown with the provided lazy-generated message and optional cause.
 *
 * @param other The object to compare against. The validation succeeds only if the invoking object is greater than `other`.
 * @param causeOf An optional transformer to dynamically create an exception when the validation fails.
 *                If `null`, the default exception creation behavior is used.
 * @param cause An optional transformer to generate a cause `Throwable` for the validation failure exception.
 * @param lazyMessage A transformer to create a custom lazy-evaluated validation failure message from the invoking object.
 * @return The invoking object (`this`) if the validation succeeds.
 * @throws ValidationFailedException If the invoking object is not strictly greater than `other`.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateAfter(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T where T : Comparable<T>, T : Temporal {
    if (!checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current instance is strictly after the specified temporal value. If validation fails,
 * a `ValidationFailedException` is thrown with an optional message and/or cause.
 *
 * @param other The temporal value to compare against. The current instance must be strictly greater than this value.
 * @param property An optional property representing the field being validated. Can be null if not applicable.
 * @param variableName An optional variable name to include in the exception message for better context. Defaults to null.
 * @param message An optional custom message to describe the validation failure. Defaults to null.
 * @param causeOf An optional transformer that generates a specific throwable to serve as the underlying cause
 *        of the `ValidationFailedException`. Defaults to null.
 * @param cause An optional transformer that generates a throwable to serve as the root cause of
 *        the `ValidationFailedException`. Defaults to null.
 * @return The current instance if validation passes.
 * @throws ValidationFailedException If the validation fails because the current instance is not strictly after `other`.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateAfter(other: T, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not after $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current temporal and comparable value is after a specified value.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param other the value to compare against; the current value must be greater than this
 * @param property the primary property associated with the validation, or null if not applicable
 * @param variable an optional secondary property for additional context, or null if not applicable
 * @param message an optional custom error message to describe the validation failure
 * @param causeOf an optional transformer to generate the cause of the exception based on the current value
 * @param cause an optional transformer to generate the underlying exception cause based on the current value
 * @return the current value if validation passes
 * @throws ValidationFailedException if the current value is not after the specified value
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateAfter(other: T, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not after $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is after the specified `other` object in both comparable and temporal contexts.
 * If the validation fails, a [ValidationFailedException] is thrown with an optional custom message and cause.
 *
 * @param other The object to compare against. The current object must be greater than this value.
 * @param callable The Kotlin function (`KFunction`) associated with the validation. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message describing the validation failure. Defaults to "is not after `other`."
 * @param causeOf A transformer that generates the cause exception using the current object, if provided. Can be null.
 * @param cause A transformer that generates an additional cause exception using the current object, if provided. Can be null.
 * @return The current object if the validation is successful.
 * @throws ValidationFailedException If the current object is not after the `other` object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateAfter(other: T, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not after $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is greater than the specified object, throwing a validation exception if the condition is not met.
 *
 * @param other The object to compare against. The current object must be greater than this value.
 * @param callable The [KFunction] associated with the validation, or null if not applicable.
 * @param parameter The [KParameter] associated with the validation, or null if not applicable.
 * @param message An optional error message to include with the exception if the validation fails. Defaults to a pre-defined message.
 * @param causeOf An optional [Transformer] to generate the cause exception from the current object, or null if not applicable.
 * @param cause An optional [Transformer] to generate an additional cause exception from the current object, or null if not applicable.
 * @return The current object, if the validation is successful.
 * @throws ValidationFailedException if the current object is not greater than the specified object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateAfter(other: T, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(
        callable,
        parameter,
        message ?: "is not after $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current instance is strictly after the specified `other` value.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param other the value to compare the current instance against
 * @param callableName the name of the callable (e.g., function or method) for context in the exception,
 *                     or `null` if not applicable
 * @param parameterName the name of the parameter being validated, or `null` if not applicable
 * @param message an optional custom message to include in the exception, or `null` for a default message
 * @param causeOf an optional transformer to produce a throwable cause when a validation failure occurs,
 *                or `null` if not applicable
 * @param cause an optional transformer to produce an additional throwable cause specific to the current value,
 *              or `null` if not applicable
 * @return the current instance if the validation passes
 * @throws ValidationFailedException if the current instance is not strictly after the `other` value
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateAfter(other: T, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not after $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is chronologically after the specified `other` object.
 *
 * If the validation fails, a `ValidationFailedException` is thrown with details about
 * the failure, including an optional message or a cause generated by the provided transformers.
 *
 * @param other The reference object to compare against.
 * @param callableName The name of the callable (e.g., function or property) where the validation is performed, or null if not specified.
 * @param parameter The parameter associated with the validation, or null if not applicable.
 * @param message An optional message describing the validation failure (default is null).
 * @param causeOf An optional transformer for generating the primary cause of the failure (default is null).
 * @param cause An additional optional transformer for generating a nested cause of the failure (default is null).
 * @return The current object if validation succeeds.
 * @throws ValidationFailedException If the current object is not after the specified `other` object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateAfter(other: T, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not after $other", cause?.invoke(this)) else causeOf(this).initCause(
        ValidationFailedException(callableName, parameter, message ?: "is not after $other", cause?.invoke(this))
    )
    return this
}
/**
 * Validates the current temporal instance to ensure it is not before the specified `other` temporal instance.
 * Throws a `ValidationFailedException` if the current instance is before the `other` instance.
 *
 * @param other the temporal instance to compare with the current instance.
 * @param causeOf optional transformer for generating the cause of the exception based on the current instance.
 * @param cause optional transformer for generating the cause of the exception directly.
 * @return the current temporal instance if validation passes.
 * @throws ValidationFailedException if the current instance is before the `other` instance.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotBefore(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(
        "Temporal $this is after to $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException("Temporal $this is before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current temporal object is not before the specified `other` temporal object.
 * If the current object is found to be before `other`, a `ValidationFailedException` is thrown.
 *
 * @param other The temporal object to compare against.
 * @param causeOf An optional transformer to generate a throwable cause based on the current object. Defaults to null.
 * @param cause An optional transformer to generate an additional throwable cause based on the current object. Defaults to null.
 * @param lazyMessage A transformer function to generate a lazy evaluation for the error message based on the current object.
 * @return The current object (`this`) if the validation succeeds.
 * @throws ValidationFailedException if the current object is before `other`.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotBefore(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T where T : Comparable<T>, T : Temporal {
    if (checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current temporal object is not before the specified temporal object.
 * If the validation fails, an exception is thrown with an optional customized message and cause.
 *
 * @param other The temporal object to compare with.
 * @param property The property associated with the validation, or `null` if not applicable.
 * @param variableName Optional name of the variable being validated. Included in the exception message if provided.
 * @param message Optional custom validation failure message. Defaults to a standard message if not provided.
 * @param causeOf Optional transformer to generate the primary exception based on the current object.
 * @param cause Optional transformer to generate the root cause throwable based on the current object.
 * @return The current temporal object if the validation passes.
 * @throws ValidationFailedException If the current object is before the specified object according to the comparator logic.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotBefore(other: T, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is before $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current temporal value is not before the specified temporal value.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param other the reference temporal value to compare against
 * @param property an optional KProperty that provides additional context for the validation failure
 * @param variable an optional secondary KProperty that provides deeper contextual information for the validation failure
 * @param message an optional custom message to describe the validation failure
 * @param causeOf an optional transformer that generates a throwable as the root cause of the validation failure
 * @param cause an optional transformer that creates a throwable to include as the underlying cause of the failure
 * @return the current instance if validation succeeds
 * @throws ValidationFailedException if the current value is before the reference value
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotBefore(other: T, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is before $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current temporal object is not chronologically before the specified temporal object.
 * If the validation fails, throws a `ValidationFailedException` with the provided callable, parameter name, and message.
 *
 * @param other The temporal object to compare against. The current object must not be before this object.
 * @param callable The Kotlin function (`KFunction`) associated with this validation. Can be null.
 * @param parameterName The name of the parameter being validated, for error reporting purposes. Can be null.
 * @param message A custom error message describing the validation failure. Default is null.
 * @param causeOf A transformer that generates a `Throwable` instance to represent the root cause of the validation failure.
 *                Can be null.
 * @param cause A transformer that generates a `Throwable` instance to represent the additional cause of the validation failure.
 *              Can be null.
 * @return The original temporal object (`this`) if validation succeeds.
 * @throws ValidationFailedException If the current object is chronologically before the specified object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotBefore(other: T, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is before $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current temporal value is not before the specified `other` value.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param other the comparative temporal value to validate against
 * @param callable an optional function reference providing context about the callable being validated
 * @param parameter an optional parameter reference related to the validation
 * @param message a custom error message to describe the validation failure, or null for a default message
 * @param causeOf a transformer to generate the primary cause of the failure, or null if no specific transformation is required
 * @param cause a transformer to generate a supplementary cause of the failure, or null if no supplementary cause is needed
 * @return the current value if the validation passes successfully
 * @throws ValidationFailedException if the current value is before the specified `other` value
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotBefore(other: T, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(
        callable,
        parameter,
        message ?: "is before $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current instance is not before the specified temporal value. Throws a `ValidationFailedException`
 * if the validation fails.
 *
 * @param other the temporal value to compare against
 * @param callableName the name of the callable (e.g., function or method) related to the validation
 * @param parameterName the name of the parameter that caused the validation failure, optional
 * @param message an optional custom message providing additional details about the validation failure
 * @param causeOf an optional transformer to generate the throwable cause of the validation failure
 * @param cause an optional transformer to generate an additional throwable to be set as the cause
 * @return the current instance if the validation succeeds
 * @throws ValidationFailedException if the current instance is before the specified `other` value
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotBefore(other: T, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is before $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is before $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not before the specified reference object. If the validation fails,
 * a `ValidationFailedException` is thrown.
 *
 * @param other The reference object to compare against.
 * @param callableName The name of the callable where validation is being performed, or null if not applicable.
 * @param parameter The parameter instance (of type `KParameter`) related to the validation, or null if not applicable.
 * @param message An optional error message to provide details about the validation failure.
 * @param causeOf A transformer function that generates a `Throwable` when validation fails, or null if not applicable.
 * @param cause A transformer function that generates a `Throwable` as the root cause of the exception, or null if not applicable.
 * @return The current object if the validation passes.
 * @throws ValidationFailedException if the current object is before the reference object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotBefore(other: T, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsBefore(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is before $other", cause?.invoke(this)) else causeOf(this).initCause(
        ValidationFailedException(callableName, parameter, message ?: "is before $other", cause?.invoke(this))
    )
    return this
}
/**
 * Validates that the current temporal value is not after the specified temporal value.
 * If the current value is determined to be after the given value, a `ValidationFailedException`
 * is thrown. Optionally, custom exception transformers can be provided to generate causes
 * for the validation failure.
 *
 * @param other The temporal value to compare against.
 * @param causeOf An optional transformer for generating a custom throwable from the current value if validation fails.
 * @param cause An optional transformer for generating an underlying cause throwable from the current value.
 * @return The current temporal value if validation passes.
 * @throws ValidationFailedException if the current temporal value is determined to be after the specified `other` value.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotAfter(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(
        "Temporal $this is after to $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException("Temporal $this is after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not after the specified object in a temporal comparison.
 * If the validation fails, a `ValidationFailedException` is thrown, optionally with a custom cause
 * or message provided by transformers.
 *
 * @param other The object to compare against. The current object must not be after this for the validation to pass.
 * @param causeOf An optional transformer that provides a throwable to be thrown as the primary cause of the exception.
 * @param cause An optional transformer that provides a throwable to be the secondary cause of the exception.
 * @param lazyMessage A transformer to generate the exception message in case validation fails.
 * @return The current object if the validation is successful.
 * @throws ValidationFailedException If the current object is after the specified object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotAfter(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T where T : Comparable<T>, T : Temporal {
    if (checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not after the specified object in terms of temporal comparison.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param other The object to compare with the current object.
 * @param property The property associated with the validation, or null if not applicable.
 * @param variableName The name of the variable involved in the validation, or null if not provided.
 * @param message An optional descriptive message to include in the exception if validation fails.
 * @param causeOf A transformer to generate a detailed throwable cause based on the current object if validation fails, or null if not applicable.
 * @param cause An alternate transformer to generate a throwable cause based on the current object if validation fails, or null if not applicable.
 * @return The current object if validation passes.
 * @throws ValidationFailedException if the current object is after the specified object in temporal comparison.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotAfter(other: T, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is after $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not chronologically after the specified `other` object.
 * If the validation fails, an exception is thrown.
 *
 * @param other The object to compare against.
 * @param property The main property associated with the validation, used for error context, or null if not specified.
 * @param variable An optional secondary property providing additional context, or null if not specified.
 * @param message An optional error message to include if validation fails, or null for a default message.
 * @param causeOf A transformer function that generates a throwable cause if validation fails, or null if not used.
 * @param cause An optional transformer function that generates a throwable cause, or null if not specified.
 * @return The current object if validation succeeds.
 * @throws ValidationFailedException If the validation fails and the current object is after the specified `other` object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotAfter(other: T, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is after $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current temporal value is not after the specified temporal value.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param other The temporal value to compare against. The current value must not be after this value.
 * @param callable An optional reference to the Kotlin function (`KFunction`) where this validation is being applied.
 *                 This provides additional context for debugging when an exception is thrown. Can be null.
 * @param parameterName An optional name of the parameter being validated. Used to enhance the exception message.
 *                      Can be null.
 * @param message An optional custom error message for the exception in case validation fails. Defaults to a generated message.
 * @param causeOf An optional transformer that produces a specific `Throwable` to be thrown when validation fails.
 *                Can be null.
 * @param cause An optional transformer to define the underlying cause (`Throwable`) of the `ValidationFailedException`.
 *              Can be null.
 * @return The current temporal value if it passes validation.
 * @throws ValidationFailedException If the current temporal value is after the specified temporal value.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotAfter(other: T, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is after $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current instance is not after the specified value in a temporal and comparable context.
 * Throws a ValidationFailedException if the validation fails.
 *
 * @param other the value against which the current instance is compared
 * @param callable the [KFunction] associated with the validation process, or null if not applicable
 * @param parameter the [KParameter] involved in the validation process, or null if not applicable
 * @param message an optional error message providing additional details about the validation failure
 * @param causeOf an optional transformer function to produce the root cause of the validation failure, or null if not used
 * @param cause an optional transformer function to produce an additional cause of the validation failure, or null if not used
 * @return the instance being validated if it does not violate the condition
 * @throws ValidationFailedException if the instance is after the specified value
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotAfter(other: T, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(
        callable,
        parameter,
        message ?: "is after $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not after the specified `other` value.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param other The value to compare against.
 * @param callableName The name of the callable that uses this validation, or null if not applicable.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf A transformer function to derive a specific cause of the failure from the current value, or null.
 * @param cause A transformer function to derive a general cause of the failure from the current value, or null.
 * @return The current value if the validation passes.
 * @throws ValidationFailedException If the current value is after the specified `other` value.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotAfter(other: T, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is after $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is after $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current instance is not after the specified `other` value.
 * Throws a `ValidationFailedException` if the current instance is determined to be after the `other` value.
 *
 * @param other The value to compare against. Validation ensures this instance is not after `other`.
 * @param callableName The name of the callable (e.g., function or property) where validation is applied, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message to describe the validation failure. Defaults to a message indicating the instance is after `other`.
 * @param causeOf An optional transformer that generates a `Throwable` cause for the exception based on the current instance, or null if not provided.
 * @param cause An optional transformer that generates a `Throwable` cause for the exception based on the current instance, or null if not provided.
 * @return The current instance if validation passes.
 * @throws ValidationFailedException if the current instance is determined to be after the `other` value.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotAfter(other: T, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkIsAfter(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is after $other", cause?.invoke(this)) else causeOf(this).initCause(
        ValidationFailedException(callableName, parameter, message ?: "is after $other", cause?.invoke(this))
    )
    return this
}
/**
 * Validates if the current temporal object is equal to the specified `other` temporal object.
 * If the validation fails, it throws a `ValidationFailedException` with an optional cause provided
 * by the supplied transformer functions.
 *
 * @param T The type of temporal object, which must be both `Comparable` and `Temporal`.
 * @param other The temporal object to compare against the current object.
 * @param causeOf An optional transformer function that generates a custom cause exception based on the current object.
 *                Defaults to `null`.
 * @param cause An optional transformer function that generates a custom cause exception based on the current object.
 *              Defaults to `null`.
 * @return The current temporal object if validation passes.
 * @throws ValidationFailedException If the current temporal object is not equal to the given `other` temporal object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateEquals(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkEquality(other)) throw if (causeOf == null) ValidationFailedException(
        "Temporal $this is not equal to $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException("Temporal $this is not equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current instance is equal to the given `other` instance. If the validation fails,
 * a `ValidationFailedException` is thrown with a custom message and optional cause.
 *
 * The equality check is inverted using a custom method which determines the equivalence
 * based on specific temporal types or direct equality.
 *
 * @param other The object to compare against the current instance.
 * @param causeOf An optional transformer for generating a custom exception when validation fails.
 *                If provided, it creates a specific cause exception before throwing `ValidationFailedException`.
 * @param cause An optional transformer for generating the underlying cause for `ValidationFailedException`.
 * @param lazyMessage A transformer to generate a custom error message based on the current instance.
 * @return The current instance if validation succeeds.
 * @throws ValidationFailedException If the validation fails, with the custom message and optional causes.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateEquals(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T where T : Comparable<T>, T : Temporal {
    if (!checkEquality(other)) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is equal to the specified [other] object.
 * If the validation fails, throws a [ValidationFailedException] with a detailed message
 * and an optional cause.
 *
 * @param T The type of the objects being compared. Must implement [Comparable] and [Temporal].
 * @param other The object to compare the current object against.
 * @param property The property associated with the validation failure, if applicable. Can be null.
 * @param variableName An optional name of the variable involved in the validation. Included in the error
 *                     message if provided.
 * @param message An optional additional message for the validation failure. Defaults to a generic message.
 * @param causeOf An optional transformer function to provide a custom cause for the validation failure.
 * @param cause An optional transformer function to specify the cause of the exception.
 * @return The current object if the validation succeeds.
 * @throws ValidationFailedException If the current object is not equal to the [other] object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateEquals(other: T, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkEquality(other)) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current object is equal to the specified object. If the validation fails,
 * a `ValidationFailedException` is thrown with the provided message and cause(s).
 *
 * @param other the object to compare with the current object
 * @param property the main property associated with this validation, or null if not specified
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional custom validation failure message, or null if not specified
 * @param causeOf an optional function to generate the root cause of the validation exception, or null if not specified
 * @param cause an optional function to generate an additional cause for the validation exception, or null if not specified
 * @return the current object if the validation passes
 * @throws ValidationFailedException if the objects being compared are not equal
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateEquals(other: T, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkEquality(other)) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current object equals the specified other object. If the validation fails,
 * a `ValidationFailedException` is thrown with optional details such as callable function, parameter name,
 * custom message, and cause transformers.
 *
 * @param other The object to compare with this instance for equality.
 * @param callable The Kotlin function (`KFunction`) related to the validation, which provides context for the validation failure. Can be null.
 * @param parameterName The name of the parameter involved in the validation failure. Can be null.
 * @param message An optional custom message to include in the exception if the validation fails. Defaults to a constructed message indicating inequality.
 * @param causeOf An optional transformer that produces a throwable cause if the validation fails. Can be null.
 * @param cause An optional transformer that produces a throwable cause if the validation fails. Can be null.
 * @return The current object (`this`) if the validation succeeds.
 * @throws ValidationFailedException if the objects are not equal.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateEquals(other: T, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkEquality(other)) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current object is equal to the specified object. If the validation fails,
 * a `ValidationFailedException` is thrown. The exception can include additional context
 * such as the callable, parameter, message, and cause.
 *
 * This method operates on objects that are both `Comparable` and implement the `Temporal` interface.
 *
 * @param other The object to compare against the current object.
 * @param callable The `KFunction` associated with the validation, or null if not applicable.
 * @param parameter The `KParameter` involved in the validation, or null if not applicable.
 * @param message An optional validation error message. Defaults to null.
 * @param causeOf An optional transformation function to produce the cause of the validation failure
 *                if the condition is not met. Defaults to null.
 * @param cause An optional transformation function to produce a general cause of the validation failure. Defaults to null.
 * @return The current object if validation passes.
 * @throws ValidationFailedException if validation fails.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateEquals(other: T, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkEquality(other)) throw if (causeOf == null) ValidationFailedException(
        callable,
        parameter,
        message ?: "is not equal to $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current object is equal to the specified `other` object. If validation fails,
 * a `ValidationFailedException` is thrown with an optional custom message and cause.
 *
 * @param other The object to compare equality against.
 * @param callableName The name of the callable (e.g., function or method) related to this validation.
 * @param parameterName The name of the parameter being validated, if applicable. Defaults to `null`.
 * @param message An optional custom message to include in the exception. Defaults to `null`.
 * @param causeOf An optional transformer to generate a specific throwable as the cause of the validation exception. Defaults to `null`.
 * @param cause An optional transformer to generate a throwable cause for the validation exception. Defaults to `null`.
 * @return The current object if validation succeeds.
 * @throws ValidationFailedException if the current object is not equal to the specified `other` object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateEquals(other: T, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkEquality(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current object is equal to the specified object based on specific conditions for temporal and comparable types.
 * If the validation fails, a `ValidationFailedException` is thrown with relevant details, including an optional custom message and cause.
 *
 * @param other The object to compare the current object with.
 * @param callableName The name of the callable (e.g., function or property) where validation is performed, or null if not specified.
 * @param parameter The parameter of the callable tied to the validation, or null if not applicable.
 * @param message An optional message providing additional details about the validation failure.
 * @param causeOf A transformer that produces a `Throwable` cause from the current object, or null if not used.
 * @param cause A transformer that produces a `Throwable` cause from the current object, or null if not used.
 * @return The current object if the validation succeeds.
 * @throws ValidationFailedException if the current object is not equal to the specified object based on the conditions.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateEquals(other: T, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (!checkEquality(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not equal to $other", cause?.invoke(this)) else causeOf(this).initCause(
        ValidationFailedException(callableName, parameter, message ?: "is not equal to $other", cause?.invoke(this))
    )
    return this
}
/**
 * Validates that the current instance is not equal to the specified `other` instance according to temporal equality rules.
 * Throws a `ValidationFailedException` if the two instances are considered equal.
 *
 * @param other The instance to compare against the current instance.
 * @param causeOf An optional transformer for creating a custom `Throwable` when the validation fails,
 *        based on the current instance.
 * @param cause An optional transformer for creating a custom `Throwable` to be used as the cause of the exception,
 *        based on the current instance.
 * @return The current instance if validation succeeds.
 * @throws ValidationFailedException If the current instance is equal to the `other` instance.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotEquals(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkEquality(other)) throw if (causeOf == null) ValidationFailedException(
        "Temporal $this is equal to $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException("Temporal $this is equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not equal to the specified object and throws an exception if the validation fails.
 *
 * The validation logic is based on the `invertEquality` method, which performs the comparison. If the objects are equal,
 * a `ValidationFailedException` is thrown with the specified lazy message and optional cause. Additional providers
 * for the exception (`causeOf` and `cause`) can be supplied to customize the thrown exception.
 *
 * @param other The object to compare against the current object for equality.
 * @param causeOf An optional transformer that produces the primary exception to be thrown if validation fails.
 * @param cause An optional transformer that produces a supplementary throwable cause for the validation exception.
 * @param lazyMessage A transformer that supplies a lazy message to describe the validation failure.
 * @return The current object if the validation succeeds.
 * @throws ValidationFailedException If the current object is equal to the specified object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotEquals(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T where T : Comparable<T>, T : Temporal {
    if (checkEquality(other)) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not equal to the specified object. If the objects are equal,
 * a `ValidationFailedException` is thrown with the provided details.
 *
 * @param other The object to compare against.
 * @param property The property associated with the validation failure, or null if not applicable.
 * @param variableName The name of the variable being validated, or null if not applicable.
 * @param message An optional custom message for the validation failure. Defaults to null.
 * @param causeOf A transformer that produces the cause of the validation failure, or null if not applicable.
 * @param cause A transformer that provides additional context for the validation failure, or null if not applicable.
 * @return The current object if the validation passes.
 * @throws ValidationFailedException If the current object is equal to the specified object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotEquals(other: T, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkEquality(other)) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not equal to the provided object, throwing a ValidationFailedException if the validation fails.
 *
 * The method compares the current object and the specified `other` object using a custom equality logic defined in the utility method.
 * If the objects are considered equal, a `ValidationFailedException` is thrown with optional additional details provided via the
 * `property`, `variable`, and `message` parameters.
 *
 * @param other the object to compare against
 * @param property an optional KProperty representing the property associated with the validation, or null if not applicable
 * @param variable an optional secondary KProperty providing additional context, or null if not applicable
 * @param message an optional custom error message to include in the exception, or null to use the default message
 * @param causeOf an optional transformer to create the underlying cause of the validation failure as a Throwable, or null if not required
 * @param cause an optional transformer to provide an additional cause of the validation failure as a Throwable, or null if not required
 * @return the original object if the validation succeeds
 * @throws ValidationFailedException if the current object is equal to the `other` object
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotEquals(other: T, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkEquality(other)) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not equal to the specified `other` object. If the validation fails,
 * a `ValidationFailedException` is thrown with an optional custom message and cause.
 *
 * @param other The object to compare against for inequality. Must be of the same type as the current object.
 * @param callable The Kotlin function (`KFunction`) associated with the validation. Can be null.
 * @param parameterName The name of the parameter causing the validation failure in the given callable. Can be null.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional `Transformer` that produces a `Throwable` cause based on the current object if validation fails. Can be null.
 * @param cause An optional `Transformer` that produces a `Throwable` cause based on the current object if validation fails. Can be null.
 * @return The current object if validation is successful.
 * @throws ValidationFailedException If the current object is equal to the `other` object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotEquals(other: T, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkEquality(other)) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not equal to the specified `other` object. If the validation fails,
 * this method throws a `ValidationFailedException` with details about the failure.
 *
 * @param other the object to compare the current object against
 * @param callable the [KFunction] associated with the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter involved in the validation, or null if not applicable
 * @param message an optional custom message describing the validation failure, defaulting to "is equal to $other" if not specified
 * @param causeOf an optional transformer function to generate a custom exception from the current object, applied before wrapping it in `ValidationFailedException`, or null if not
 *  used
 * @param cause an optional transformer function to generate the underlying cause of the exception from the current object, or null if not used
 * @return the current object if validation succeeds
 * @throws ValidationFailedException if validation fails
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotEquals(other: T, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkEquality(other)) throw if (causeOf == null) ValidationFailedException(
        callable,
        parameter,
        message ?: "is equal to $other",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not equal to the specified `other` value.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param other the value to compare the current object against
 * @param callableName the name of the callable function or method related to this validation
 * @param parameterName the name of the parameter involved in the validation, if applicable
 * @param message an optional custom message describing the validation failure
 * @param causeOf an optional transformation function to generate a throwable cause based on the current object
 * @param cause an optional transformation function to provide additional context for the validation failure
 * @return the current object if the validation succeeds
 * @throws ValidationFailedException if the validation fails and the current object is equal to `other`
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotEquals(other: T, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkEquality(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not equal to the specified object.
 *
 * If the validation fails, a `ValidationFailedException` is thrown with optional details such as
 * the callable name, parameter, and custom error message. Additionally, custom transformers can be
 * provided to compute the underlying cause of the validation failure.
 *
 * @param other The object to compare against the current object.
 * @param callableName The name of the callable where validation is being performed, or `null` if not specified.
 * @param parameter The `KParameter` instance representing the parameter involved in validation, or `null` if not applicable.
 * @param message An optional custom error message to include in the exception if validation fails, or `null` for a default message.
 * @param causeOf An optional transformer to compute the root cause of the exception from the current object, or `null` for no such transformation.
 * @param cause An optional transformer to compute a cause from the current object if validation fails, or `null` for no cause.
 * @return The current object if validation is successful.
 * @throws ValidationFailedException if the current object is equal to the specified `other` object.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun <T> T.validateNotEquals(other: T, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (checkEquality(other)) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is equal to $other", cause?.invoke(this)) else causeOf(this).initCause(
        ValidationFailedException(callableName, parameter, message ?: "is equal to $other", cause?.invoke(this))
    )
    return this
}

/**
 * Validates that the temporal object is within the specified temporal interval.
 * If the temporal object is not within the interval, an exception is thrown.
 *
 * @param interval The temporal interval within which the temporal object must be validated.
 * @param causeOf A transformer that generates a `Throwable` based on the temporal object,
 *                if the validation fails. This parameter is optional.
 * @param cause An alternative transformer that generates a `Throwable` based on the temporal object,
 *              if the validation fails. This parameter is optional.
 * @return The temporal object itself if the validation succeeds.
 * @throws ValidationFailedException If the temporal object is not within the specified interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateIn(interval: TemporalInterval, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this !in interval) throw if (causeOf == null) ValidationFailedException(
        "Temporal $this is not in interval $interval",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException("Temporal $this is not in interval $this", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is within the specified temporal interval.
 * If the object does not fall within the interval, an exception is thrown.
 *
 * @param interval The temporal interval against which the validation is performed.
 * @param causeOf A transformer function that generates the throwable cause of the validation failure.
 *                If null, a default validation failure exception is used.
 * @param cause A transformer function that generates an additional throwable to include as the cause
 *              of the validation failure exception.
 * @param lazyMessage A transformer function that generates a lazy message containing details
 *                    about the validation failure.
 * @return The validated object if it is within the specified temporal interval.
 * @throws ValidationFailedException if the object does not fall within the interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateIn(interval: TemporalInterval, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T where T : Comparable<T>, T : Temporal {
    if (this !in interval) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current object falls within the specified temporal interval.
 * If the object does not lie within the interval, an exception is thrown.
 *
 * @param interval The temporal interval against which the validation is performed.
 * @param property The property associated with the value being validated. Can be null.
 * @param variableName The name of the variable for error reporting purposes. Can be null.
 * @param message The error message to use if validation fails. Can be null;
 *                defaults to "is not in interval $interval".
 * @param causeOf A transformer that produces a custom exception to throw if validation fails. Can be null.
 * @param cause A transformer that produces the root cause exception to associate with the thrown exception. Can be null.
 * @return The current object if validation passes.
 * @throws ValidationFailedException if the object does not fall within the specified interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateIn(interval: TemporalInterval, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this !in interval) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not in interval $interval", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not in interval $interval", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current value is within the specified temporal interval. If the value is
 * outside the interval, a validation exception is thrown.
 *
 * @param interval The temporal interval within which the value must lie.
 * @param property The property associated with the value being validated, or null if not applicable.
 * @param variable The variable associated with the value being validated, or null if not applicable.
 * @param message An optional custom error message to include in the exception, or null for the default message.
 * @param causeOf An optional transformer that provides a custom throwable cause when the validation fails.
 * @param cause An optional transformer that provides an additional throwable cause when the validation fails.
 * @return The validated value, if it lies within the specified interval.
 * @throws ValidationFailedException if the value does not lie within the interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateIn(interval: TemporalInterval, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this !in interval) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not in interval $interval", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not in interval $interval", cause?.invoke(this)))
    return this
}
/**
 * Validates if the invoking temporal value lies within the specified interval.
 * Throws a `ValidationFailedException` if the value does not fall within the interval.
 *
 * @param interval The temporal interval within which the invoking value must lie.
 * @param callable The callable function associated with the validation, if provided.
 * @param parameterName The name of the parameter being validated, if applicable.
 * @param message An optional custom error message to include in the exception.
 * @param causeOf A transformer function that generates the root cause of the exception from the validated value, if applicable.
 * @param cause A transformer function that provides a secondary cause of the exception from the validated value, if applicable.
 * @return The invoking value if it satisfies the interval validation.
 * @throws ValidationFailedException If the invoking value does not fall within the specified interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateIn(interval: TemporalInterval, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this !in interval) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not in interval $interval", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not in interval $interval", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current object is within the specified temporal interval.
 * Throws a ValidationFailedException if the object is not within the interval.
 *
 * @param interval The temporal interval against which the object will be validated.
 * @param callable The function being validated, or null if not applicable.
 * @param parameter The parameter being validated, or null if not applicable.
 * @param message An optional custom error message to include in the exception.
 * @param causeOf An optional transformer that generates a throwable cause based on the object,
 * used to wrap the validation failure further.
 * @param cause An optional transformer that generates a throwable cause based on the object.
 * @return The object itself, if it passes the validation.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateIn(interval: TemporalInterval, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this !in interval) throw if (causeOf == null) ValidationFailedException(
        callable,
        parameter,
        message ?: "is not in interval $interval",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not in interval $interval", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current temporal value falls within a specified interval.
 * If the value is not within the interval, a validation exception is thrown.
 *
 * @param interval The temporal interval to validate against.
 * @param callableName The name of the callable where the validation is applied (optional).
 * @param parameterName The name of the parameter being validated (optional).
 * @param message A custom message to include in the validation exception (optional).
 * @param causeOf A transformer to generate the root cause of the validation failure exception (optional).
 * @param cause A transformer to generate the cause of the exception for additional context (optional).
 * @return The same value if it lies within the specified interval.
 * @throws ValidationFailedException If the value is not within the specified interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateIn(interval: TemporalInterval, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this !in interval) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not in interval $interval", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not in interval $interval", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current object is within the specified temporal interval.
 * If the object is not within the interval, an exception is thrown.
 *
 * @param interval The temporal interval to validate against.
 * @param callableName The name of the callable associated with the validation, or null if not applicable.
 * @param parameter The parameter being validated, or null if not applicable.
 * @param message An optional custom error message to include when the validation fails. Defaults to null.
 * @param causeOf A transformer that generates the throwable to be thrown when validation fails, based on the provided value. Defaults to null.
 * @param cause A transformer that generates the root cause of the validation failure, based on the provided value. Defaults to null.
 * @return The original object if validation succeeds.
 * @throws ValidationFailedException If the object is not within the specified interval, including the generated or customized cause, if provided.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateIn(interval: TemporalInterval, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this !in interval) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not in interval $interval", cause?.invoke(this)) else causeOf(this).initCause(
        ValidationFailedException(callableName, parameter, message ?: "is not in interval $interval", cause?.invoke(this))
    )
    return this
}
/**
 * Validates that the current temporal value is not within the specified temporal interval.
 * If the value is within the interval, an exception is thrown.
 *
 * @param interval The temporal interval to check against.
 * @param causeOf A transformer function that generates a throwable cause when validation fails, or null.
 * @param cause A transformer function that generates a throwable cause tied to the failure, or null.
 * @return The current temporal value if the validation passes successfully.
 * @throws ValidationFailedException if the temporal value is within the interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T : Temporal> T.validateNotIn(interval: TemporalInterval, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in interval) throw if (causeOf == null) ValidationFailedException(
        "Temporal $this is in interval $interval",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException("Temporal $this is in interval $this", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given comparable temporal value is not within a specified temporal interval.
 * If the value is within the interval, an exception is thrown.
 *
 * @param interval The temporal interval against which the value should be validated.
 * @param causeOf An optional transformer to produce a throwable cause based on the value.
 * @param cause An optional transformer to generate a throwable directly based on the value.
 * @param lazyMessage A transformer to produce a detailed contextual message if the validation fails.
 * @return The value itself, if the validation passes (not within the specified interval).
 * @throws ValidationFailedException If the value is within the specified temporal interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateNotIn(interval: TemporalInterval, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T where T : Comparable<T>, T : Temporal {
    if (this in interval) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified temporal interval.
 * If the value is within the interval, a ValidationFailedException is thrown.
 *
 * @param interval The temporal interval to check against.
 * @param property An optional property associated with the value being validated.
 * @param variableName An optional name of the variable being validated.
 * @param message An optional custom error message to use if validation fails.
 * @param causeOf An optional transformer to produce a throwable cause based on the current value, which wraps the validation exception.
 * @param cause An optional transformer to produce a throwable cause based on the current value for additional context when validation fails.
 * @return The current value if it is not within the specified interval.
 * @throws ValidationFailedException if the current value is within the specified interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateNotIn(interval: TemporalInterval, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this in interval) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is in interval $interval", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is in interval $interval", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified temporal interval.
 * If the value is found to be within the interval, a validation exception is thrown.
 *
 * @param interval The temporal interval to check the value against.
 * @param property The property associated with the value for additional context. Nullable.
 * @param variable The variable associated with the value for additional context. Nullable.
 * @param message An optional custom message to include in the validation exception.
 * @param causeOf An optional transformer that provides a throwable as the cause for the exception.
 * @param cause An optional transformer that derives a throwable cause specific to the value.
 * @return The current value if it is not within the interval.
 * @throws ValidationFailedException If the value is within the specified interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateNotIn(interval: TemporalInterval, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this in interval) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is in interval $interval", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is in interval $interval", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given value is not within the specified temporal interval. If the value is within the interval,
 * an exception is thrown.
 *
 * @param interval The temporal interval to check against.
 * @param callable The function that is being validated, if available.
 * @param parameterName The name of the parameter being validated, if specified.
 * @param message An optional custom validation failure message.
 * @param causeOf An optional transformer to create a more specific exception, derived from the validation context.
 * @param cause An optional transformer to create the cause of the exception, based on the value.
 * @return The validated value, if the validation passes.
 * @throws ValidationFailedException If the value is within the specified interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateNotIn(interval: TemporalInterval, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this in interval) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is in interval $interval", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is in interval $interval", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking object is not within the specified temporal interval.
 * If the object is within the interval, an exception is thrown.
 *
 * @param interval The temporal interval to check against.
 * @param callable The function where the validation is invoked, or null if not provided.
 * @param parameter The parameter being validated, or null if not applicable.
 * @param message An optional custom message for the validation failure.
 * @param causeOf A transformer for generating a throwable to be thrown in case of failure, or null.
 * @param cause A transformer for generating a throwable cause for the failure, or null.
 * @return The invoking object if the validation is successful.
 * @throws ValidationFailedException If the invoking object is within the specified interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateNotIn(interval: TemporalInterval, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this in interval) throw if (causeOf == null) ValidationFailedException(
        callable,
        parameter,
        message ?: "is not in interval $interval",
        cause?.invoke(this)
    ) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is in interval $interval", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current temporal value is not within the specified temporal interval.
 * If the value is found within the interval, a `ValidationFailedException` will be thrown.
 *
 * @param interval The temporal interval to check against.
 * @param callableName The name of the callable where the validation is performed. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message for the exception. If null, a default message will be used.
 * @param causeOf A transformer function to generate an alternative throwable which will wrap the validation exception as a cause. Can be null.
 * @param cause A transformer function to generate a throwable to associate as the cause of the validation exception. Can be null.
 * @return The validated temporal value if it is not within the specified interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateNotIn(interval: TemporalInterval, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this in interval) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is in interval $interval", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is in interval $interval", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking value is not within the specified temporal interval.
 * If the value is within the interval, an exception is thrown.
 *
 * @param interval The temporal interval to check against.
 * @param callableName The name of the callable in which the validation is performed, or null if not applicable.
 * @param parameter The parameter being validated, or null if not applicable.
 * @param message Optional custom error message, defaulting to a standard message if null.
 * @param causeOf An optional transformer to generate a throwable cause for the exception from the value, or null if not used.
 * @param cause An optional transformer to generate an additional throwable cause for the exception from the value, or null if not used.
 * @return The original value if the validation passes.
 * @throws ValidationFailedException If the invoking value is found within the specified interval.
 * @since 5.0.4
 */
@IgnorableReturnValue
fun <T> T.validateNotIn(interval: TemporalInterval, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T where T : Comparable<T>, T : Temporal {
    if (this in interval) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is in interval $interval", cause?.invoke(this)) else causeOf(this).initCause(
        ValidationFailedException(callableName, parameter, message ?: "is in interval $interval", cause?.invoke(this))
    )
    return this
}