/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("UuidValidatorsKt")
@file:Since("5.0.0")
@file:Suppress("unused")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.identifiers.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

/**
 * Validates if the UUID matches the specified version. If the version does not match,
 * an exception is thrown, optionally using the provided transformers for custom exception handling.
 *
 * @param version The expected version of the UUID to validate against.
 * @param causeOf A transformer that maps the UUID to a specific throwable to be used as the cause.
 *                Can be null if no custom cause transformation is needed.
 * @param cause Another transformer that maps the UUID to a throwable that will be appended to the
 *              exception as a cause. Can be null if no custom cause appending is needed.
 * @return The validated UUID if the version matches the provided expected version.
 * @throws ValidationFailedException Thrown when the UUID does not match the specified version, optionally containing the
 *                             transformed cause based on the provided transformers.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun Uuid.validateVersion(version: UuidVersion, causeOf: Transformer<Uuid, Throwable>? = null, cause: Transformer<Uuid, Throwable>? = null): Uuid {
    if (this.version != version) throw if (causeOf == null) ValidationFailedException("Uuid is not an uuid of version ${version.number}.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is not positive.", cause?.invoke(this)))
    return this
}
/**
 * Validates the version of this UUID against the expected version.
 *
 * If the version of the UUID does not match the expected version, an exception is thrown.
 * The exception can be customized using the provided `causeOf`, `cause`, and `lazyMessage` transformers.
 *
 * @param version The expected `UuidVersion` to validate this UUID against.
 * @param causeOf An optional transformer that generates a custom exception from the UUID when the validation fails.
 *                If not provided, the default behavior is applied.
 * @param cause An optional transformer to generate the cause of the exception from the UUID. If not provided, there will be no underlying cause.
 * @param lazyMessage A transformer that generates a message for the exception lazily, based on the UUID.
 * @return The original UUID if the version is valid.
 * @throws ValidationFailedException If the UUID version does not match the expected version and no custom exception is provided.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun Uuid.validateVersion(version: UuidVersion, causeOf: Transformer<Uuid, Throwable>? = null, cause: Transformer<Uuid, Throwable>? = null, lazyMessage: Transformer<Uuid, Any>): Uuid {
    if (this.version != version) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the UUID's version matches the specified version.
 *
 * @param version The expected UUID version to validate against.
 * @param property An optional Kotlin property associated with the validation. May provide additional
 *                 context such as the property owner, name, and return type. Can be `null`.
 * @param variableName An optional name of the variable being validated, used for descriptive error messages.
 *                     Can be `null`.
 * @param message An optional custom error message to include in the exception if the validation fails.
 *                Defaults to a generated message if not provided. Can be `null`.
 * @param causeOf An optional transformer that generates the cause of the exception if the validation fails.
 *                Can be `null`.
 * @param cause Another optional transformer that generates the cause of the exception for chaining purposes.
 *              Can be `null`.
 * @return The original UUID if the validation succeeds.
 * @throws ValidationFailedException If the UUID's version does not match the specified version, including
 *                             additional context about the failure.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun Uuid.validateVersion(version: UuidVersion, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<Uuid, Throwable>? = null, cause: Transformer<Uuid, Throwable>? = null): Uuid {
    if (this.version != version) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not an uuid of version ${version.number}", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current UUID matches the specified version. If the UUID does not match the
 * given version, an exception is thrown.
 *
 * @param version The expected version of the UUID (e.g., V1, V4).
 * @param property Optional property associated with this validation for contextual information.
 * @param variable Optional secondary property related to the validation context.
 * @param message An optional custom error message to use if validation fails. If not specified, a
 *                default message is generated.
 * @param causeOf An optional transformer to generate the cause of the exception in case of validation failure.
 * @param cause An optional transformer to provide additional context or cause for the exception.
 * @return The instance of the UUID itself if validation passes.
 * @throws ValidationFailedException If the UUID does not match the specified version.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun Uuid.validateVersion(version: UuidVersion, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<Uuid, Throwable>? = null, cause: Transformer<Uuid, Throwable>? = null): Uuid {
    if (this.version != version) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not an uuid of version ${version.number}", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not positive", cause?.invoke(this)))
    return this
}
/**
 * Validates the version of the current UUID against the provided expected version.
 * If the UUID version does not match the expected version, an exception is thrown.
 *
 * @param version The expected `UuidVersion` to validate against.
 * @param callable The callable function associated with the validation context. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message for the exception in case of failure. Defaults to null.
 * @param causeOf An optional transformer that generates a cause of type `Throwable` based on the UUID. Defaults to null.
 * @param cause An optional transformer that generates a secondary cause of type `Throwable` based on the UUID. Defaults to null.
 * @return The same UUID instance if validation is successful.
 * @throws ValidationFailedException if the UUID version does not match the expected version.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun Uuid.validateVersion(version: UuidVersion, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Uuid, Throwable>? = null, cause: Transformer<Uuid, Throwable>? = null): Uuid {
    if (this.version != version) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not an uuid of version ${version.number}", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the version of this UUID matches the specified version.
 *
 * @param version The `UuidVersion` that this UUID is expected to match.
 * @param callable An optional `KFunction` instance representing the function where this validation occurs.
 * @param parameter An optional `KParameter` instance representing the parameter involved in the validation.
 * @param message An optional custom error message to provide additional context in case of a validation failure.
 * @param causeOf An optional transformer function that generates a `Throwable` based on this UUID in case of validation failure.
 * @param cause An optional transformer function that generates a `Throwable` to be used as the root cause of the exception.
 * @return The validated UUID if the version matches the specified version.
 * @throws ValidationFailedException if the version of the UUID does not match the specified version.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun Uuid.validateVersion(version: UuidVersion, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Uuid, Throwable>? = null, cause: Transformer<Uuid, Throwable>? = null): Uuid {
    if (this.version != version) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not an uuid of version ${version.number}", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not positive", cause?.invoke(this)))
    return this
}
/**
 * Validates that the UUID has the specified version. If the version does not match, an exception
 * of type `ValidationFailedException` is thrown.
 *
 * @param version The expected version of the UUID to validate against.
 * @param callableName The name of the callable where the validation occurs, or null.
 * @param parameterName The name of the parameter being validated, or null.
 * @param message The custom message to be included in the exception if validation fails, or null.
 * @param causeOf A transformer function that generates a `Throwable` cause based on the UUID, or null.
 * @param cause A fallback transformer function that generates a `Throwable` cause based on the UUID, or null.
 * @return The original UUID if validation succeeds.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun Uuid.validateVersion(version: UuidVersion, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<Uuid, Throwable>? = null, cause: Transformer<Uuid, Throwable>? = null): Uuid {
    if (this.version != version) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not an uuid of version ${version.number}", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not positive", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the UUID matches the specified version. If the validation fails, an exception is thrown.
 *
 * @param version The expected version of the UUID.
 * @param callableName The name of the callable (e.g., function or method) associated with the validation, or null if not applicable.
 * @param parameter The parameter involved in the validation, or null if not applicable.
 * @param message An optional custom error message to include in the exception if the validation fails. Defaults to null.
 * @param causeOf An optional transformer for creating a specific throwable if the validation fails. Defaults to null.
 * @param cause An optional transformer for chaining additional exceptions if the validation fails. Defaults to null.
 * @return The UUID instance if it matches the specified version.
 * @throws ValidationFailedException If the UUID version does not match the specified version and no custom exception transformer is provided.
 * @since 4.7.0
 */
@IgnorableReturnValue
fun Uuid.validateVersion(version: UuidVersion, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<Uuid, Throwable>? = null, cause: Transformer<Uuid, Throwable>? = null): Uuid {
    if (this.version != version) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not an uuid of version ${version.number}", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not positive", cause?.invoke(this)))
    return this
}