/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 * @since 5.1.0
 */

@file:JvmName("WebValidatorsKt")
@file:Since("5.1.0")
@file:Suppress("unused")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.web.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

/**
 * Validates whether the current HTTP status is successful. If the status is not successful, it throws an exception.
 *
 * @param causeOf An optional transformer that converts the current HTTP status to a throwable cause.
 * @param cause An optional transformer that generates a throwable cause from the current HTTP status.
 * @return The current HTTP status if it is successful.
 * @throws ValidationFailedException If the HTTP status is not successful and no `causeOf` transformer is provided.
 * @throws Throwable If the HTTP status is not successful and a `causeOf` or `cause` transformer generates a throwable.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateSuccessfull(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isSuccessful) throw if (causeOf == null) ValidationFailedException("$this is not successful.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$this is not successful.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current `HttpStatus` is successful (within the successful HTTP status code range).
 * If not successful, throws an exception with the provided transformations to customize the cause or message.
 * 
 * @param causeOf A transformer that maps the `HttpStatus` to a custom `Throwable` cause. Defaults to `null`.
 * @param cause A transformer that maps the `HttpStatus` to a secondary `Throwable` cause. Defaults to `null`.
 * @param lazyMessage A transformer that generates a message object based on the `HttpStatus`.
 * @return The current `HttpStatus` instance if it is successful.
 * @throws ValidationFailedException If the `HttpStatus` is not successful, with the exception message and cause constructed using the provided transformers.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateSuccessfull(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null, lazyMessage: Transformer<HttpStatus, Any>): HttpStatus {
    if (!isSuccessful) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status is successful. If the status is not successful, throws a validation exception.
 *
 * @param property The associated property for which this validation is performed, optional.
 * @param variableName The name of the variable related to the validation context, optional.
 * @param message A custom validation message to use when the validation fails, optional.
 * @param causeOf A transformer function that provides a throwable based on the current HTTP status when a validation failure occurs, optional.
 * @param cause A transformer function that provides a throwable to be used as the cause for the validation failure, optional.
 * @return The current [HttpStatus] if validation is successful.
 * @throws ValidationFailedException If the HTTP status is not successful.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateSuccessfull(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isSuccessful) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not successful", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not successful", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status is successful. If the status is not successful, a custom exception 
 * is thrown based on the provided parameters.
 *
 * @param property The property associated with the validation, can be null.
 * @param variable The variable associated with the validation, can be null.
 * @param message An optional custom validation failure message to include in the exception.
 * @param causeOf A transformer that generates a throwable cause from the current HTTP status when validation fails, can be null.
 * @param cause A transformer that generates an inner throwable based on the current HTTP status, used as the cause of the validation failure exception, can be null.
 * @return The current `HttpStatus` if it is successful.
 * @throws ValidationFailedException if the HTTP status is not successful and no `causeOf` transformer is provided.
 * @throws Throwable when a custom throwable is generated by the `causeOf` transformer based on the HTTP status.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateSuccessfull(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isSuccessful) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not successful", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not successful", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status is successful. If the status is not successful, 
 * it throws a `ValidationFailedException` or an exception provided by the given transformer.
 *
 * @param callable The reference to the function being validated, if applicable. Can be null.
 * @param parameterName The name of the parameter being validated, if applicable. Can be null.
 * @param message An optional message to describe the validation failure. Defaults to "is not successful" if not provided.
 * @param causeOf A transformer function that takes the current HTTP status and returns a custom throwable to be thrown. Can be null.
 * @param cause A transformer function that takes the current HTTP status and provides the cause for the exception. Can be null.
 * @return The current `HttpStatus` if validation is successful.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateSuccessfull(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isSuccessful) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not successful", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not successful", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HttpStatus instance represents a successful status.
 * If the status is not successful, this method throws a ValidationFailedException or an exception 
 * provided by the transformers.
 *
 * @param callable The function reference that is associated with the validation context, if provided.
 * @param parameter The parameter reference that is associated with the validation context, if provided.
 * @param message An optional custom message to include in the thrown exception if the status is not successful.
 * @param causeOf An optional transformer function that generates a Throwable based on the current HttpStatus 
 * if the validation fails.
 * @param cause An optional transformer function that generates a base cause for the exception based on 
 * the current HttpStatus if the validation fails.
 * @return The current HttpStatus instance if validation is successful.
 * @throws ValidationFailedException If the HttpStatus is not successful and no `causeOf` transformer is provided.
 * @throws Throwable If the HttpStatus is not successful and a `causeOf` transformer is provided, utilizing 
 * the transformer to generate the exception to throw.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateSuccessfull(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isSuccessful) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not successful", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not successful", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the HTTP status is successful and throws an exception if it is not.
 *
 * @param callableName The name of the callable context where the validation is being performed. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message to describe the validation failure. If null, a default message is used.
 * @param causeOf A function that transforms the current HTTP status into a throwable cause for the exception if validation fails. Can be null.
 * @param cause Additional transformer for the HTTP status into a throwable cause for the exception. Can be null.
 * @return This HTTP status, if it is successful.
 * @throws ValidationFailedException if the HTTP status is not successful.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateSuccessfull(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isSuccessful) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not successful", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not successful", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status is considered successful. If not successful, throws a validation exception.
 *
 * @param callableName The name of the callable that is being validated. Can be null.
 * @param parameter The parameter being validated. Can be null.
 * @param message An optional custom error message to include in the exception. Defaults to "is not successful" if null.
 * @param causeOf A transformer function that generates the primary cause of the exception based on the HTTP status. Can be null.
 * @param cause A transformer function that generates an additional cause of the exception based on the HTTP status. Can be null.
 * @return The instance of the HTTP status if it is determined to be successful.
 * @throws ValidationFailedException If the HTTP status is not successful.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateSuccessfull(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isSuccessful) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not successful", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not successful", cause?.invoke(this)))
    return this
}

/**
 * Validates if the current HTTP status represents an error status.
 * If the status is not an error, an exception is thrown.
 *
 * @param causeOf A transformer that generates a Throwable based on the current HttpStatus. 
 *                If provided, it overrides the default exception generated when the status is not an error.
 * @param cause A transformer that generates a Throwable based on the current HttpStatus to be used as the cause of the exception.
 * @return The current instance of HttpStatus if it represents an error status.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateError(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isError) throw if (causeOf == null) ValidationFailedException("$this is not an error.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$this is not an error.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `HttpStatus` indicates an error condition. If the status is not an error,
 * it throws a custom exception created using the provided transformers and lazy message.
 *
 * @param causeOf A transformer that generates a throwable derived from the current `HttpStatus`. It is used as the primary cause of the exception.
 * @param cause A transformer that generates a throwable derived from the current `HttpStatus`. It is used as the secondary cause of the exception.
 * @param lazyMessage A transformer that generates a lazy message dynamically based on the current `HttpStatus`.
 * @return The `HttpStatus` instance, if it indicates an error condition.
 * @throws ValidationFailedException If the `HttpStatus` does not indicate an error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateError(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null, lazyMessage: Transformer<HttpStatus, Any>): HttpStatus {
    if (!isError) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current `HttpStatus` instance represents an error status.
 * If the `HttpStatus` is not an error, an exception will be thrown.
 *
 * @param property The property related to the validation, if applicable. Can be null.
 * @param variableName The name of the variable being validated, if applicable. Can be null.
 * @param message An optional custom validation message. Can be null.
 * @param causeOf A transformer that generates a throwable based on the current `HttpStatus`.
 *                Used to generate the "cause" of the exception. Can be null.
 * @param cause A transformer that generates a throwable based on the current `HttpStatus`.
 *              Used as the primary cause of the exception. Can be null.
 * @return The current `HttpStatus` instance for fluent validation.
 * @throws ValidationFailedException If the current `HttpStatus` is not an error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateError(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isError) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not an error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not an error", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status represents an error state. If the status is not an error,
 * an exception is thrown. This method can include information about the property or variable being validated,
 * as well as a custom error message and throwable cause transformations.
 *
 * @param property The property being validated. This can be null if no specific property is associated with the validation.
 * @param variable The variable being validated. This can be null if no specific variable is associated with the validation.
 * @param message An optional custom message describing the validation failure. Defaults to null.
 * @param causeOf A transformer function that generates a throwable cause based on the current HTTP status. Defaults to null.
 * @param cause A transformer function that generates an additional nested throwable cause based on the current HTTP status. Defaults to null.
 * @return The current HTTP status if it represents an error. Otherwise, an exception is thrown.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateError(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isError) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not an error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not an error", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status represents an error. If it does not, this method throws
 * a validation exception. The thrown exception can optionally include additional information 
 * like the related callable, parameter name, custom message, or a throwable cause.
 *
 * @param callable The optional callable (e.g., function or method) associated with the validation failure.
 * @param parameterName The optional name of the parameter linked to the validation failure.
 * @param message An optional custom error message to provide additional context for the validation failure.
 * @param causeOf A transformer function that maps the current HTTP status to a throwable cause.
 *                This evaluated cause, if present, is used as the cause of the exception.
 * @param cause A transformer function that maps the current HTTP status to a throwable cause.
 *              This cause is included with the validation exception if no `causeOf` is provided.
 * @return The current HTTP status if it represents an error.
 * @throws ValidationFailedException If the HTTP status does not represent an error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateError(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isError) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not an error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not an error", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current HttpStatus is an error status. If it is not an error, 
 * throws a ValidationFailedException or a Throwable created by the provided transformers.
 *
 * @param callable The callable function associated with this validation, if applicable. Can be null.
 * @param parameter The parameter involved in this validation, if applicable. Can be null.
 * @param message An optional custom message to include in the exception if the validation fails.
 * @param causeOf A transformer that generates a Throwable based on the current HttpStatus. This is used as 
 *        the primary cause when the HttpStatus is not an error.
 * @param cause A transformer that generates an additional Throwable based on the current HttpStatus, used as 
 *        a supplementary cause in the exception.
 * @return The current HttpStatus instance if it is an error status.
 * @throws ValidationFailedException If this HttpStatus is not an error and no causeOf transformer is provided.
 * @throws Throwable If a causeOf transformer is provided and this HttpStatus is not an error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateError(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isError) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not an error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not an error", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status represents an error state. If the status is not an error,
 * this method throws a [ValidationFailedException] or a custom throwable as defined by the optional transformers.
 *
 * @param callableName The name of the callable associated with this validation, or null if not applicable.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message The validation error message to use if the status is not an error.
 * @param causeOf A transformer function to generate a custom throwable if the status is not an error. The HTTP status is passed to this transformer to create the throwable.
 * @param cause A transformer function to generate the underlying cause of the exception, if applicable. The HTTP status is passed to this transformer to create the cause.
 * @return The current HTTP status, if it represents an error state.
 * @throws ValidationFailedException If the status is not an error and no custom throwable is provided by the `causeOf` transformer.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateError(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isError) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not an error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not an error", cause?.invoke(this)))
    return this
}
/**
 * Validates if the `HttpStatus` instance represents an error status. Throws a `ValidationFailedException`
 * if the status is not an error.
 *
 * @param callableName The name of the callable associated with the validation (nullable).
 * @param parameter The parameter being validated, if applicable (nullable).
 * @param message An optional custom error message to include in the exception (nullable).
 * @param causeOf A transformer to produce a throwable cause based on the current `HttpStatus` (nullable).
 * @param cause Another transformer to produce a throwable cause based on the current `HttpStatus` (nullable).
 * @return The `HttpStatus` instance if it represents an error status.
 * @throws ValidationFailedException if the `HttpStatus` is not an error, constructed with the provided details.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateError(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isError) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not an error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not an error", cause?.invoke(this)))
    return this
}

/**
 * Validates whether the HTTP status code represents a client error (status code 4xx).
 * Throws an exception if the status is not a client error.
 *
 * @param causeOf A transformer function that converts the HTTP status to a throwable if the validation fails.
 * @param cause A transformer function that converts the HTTP status to a throwable to be used as the cause of the exception.
 * @return The HTTP status itself if it represents a client error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateClientError(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isClientError) throw if (causeOf == null) ValidationFailedException("$this is not a client error.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$this is not a client error.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the HttpStatus represents a client error (4xx status codes). 
 * If the status is not a client error, throws an exception based on the provided transformers and message.
 *
 * @param causeOf A nullable transformer to derive a Throwable from the HttpStatus, used as the primary exception.
 * @param cause A nullable transformer to derive a Throwable from the HttpStatus, used as the cause of the exception.
 * @param lazyMessage A transformer to derive a lazy evaluation message or object from the HttpStatus for the exception.
 * @return The current HttpStatus instance if it passes the validation.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateClientError(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null, lazyMessage: Transformer<HttpStatus, Any>): HttpStatus {
    if (!isClientError) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status represents a client error (4xx).
 * Throws a validation exception or a custom exception via transformers if the status is not a client error.
 *
 * @param property The optional property reference associated with the validation failure, or null.
 * @param variableName The optional name of the variable being validated, or null.
 * @param message The optional custom message to include if validation fails, or null.
 * @param causeOf An optional transformer to produce a cause exception if validation fails, or null.
 * @param cause An optional transformer to produce a direct cause exception for the validation failure, or null.
 * @return The current `HttpStatus` instance if it represents a client error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateClientError(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isClientError) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not a client error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not a client error", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the HTTP status is a client error (4xx).
 * If the status is not a client error, an exception is thrown.
 * 
 * @param property The primary property associated with the validation context. Can be null.
 * @param variable An optional secondary property pertinent to the validation context. Can be null.
 * @param message An optional custom error message to include in the exception. Defaults to null.
 * @param causeOf A transformer function that, when invoked with the current HTTP status, produces a `Throwable`
 *                to be used as the root cause of the exception. Can be null.
 * @param cause A transformer function that, when invoked with the current HTTP status, produces a `Throwable`
 *              to be attached as additional context for the exception. Can be null.
 * @return The current HTTP status if it represents a client error, allowing method chaining.
 * @throws ValidationFailedException If the HTTP status does not represent a client error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateClientError(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isClientError) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not a client error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not a client error", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status represents a client error (4xx) and throws an exception if not.
 *
 * @param callable The callable function associated with the validation (optional).
 * @param parameterName The name of the parameter being validated (optional).
 * @param message The custom error message to include in the exception if validation fails (optional).
 * @param causeOf A transformer function to generate a cause exception if validation fails (optional).
 * @param cause A transformer function to generate a detailed cause exception (optional).
 * @return The current `HttpStatus` instance if validation passes.
 * @throws ValidationFailedException If the HTTP status is not within the client error range (4xx).
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateClientError(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isClientError) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not a client error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not a client error", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status represents a client error (status codes 4xx). 
 * If the status is not a client error, an exception is thrown based on the provided transformers 
 * or a default exception with the specified message.
 *
 * @param callable The callable function related to the validation context, or `null` if not applicable.
 * @param parameter The parameter associated with the validation context, or `null` if not applicable.
 * @param message An optional message to include in the exception if validation fails. Defaults to `null`.
 * @param causeOf An optional transformer that converts the current HTTP status to a `Throwable` 
 * to be used as the primary cause of the raised exception, or `null` if not required.
 * @param cause An optional transformer that converts the current HTTP status to a `Throwable` 
 * to be used as the secondary cause of the raised exception, or `null` if not required.
 * @return The current HTTP status if it represents a client error (status codes 4xx).
 * @throws ValidationFailedException If the current HTTP status is not a client error and 
 * no `causeOf` transformer is provided for customized exception construction.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateClientError(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isClientError) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not a client error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not a client error", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status represents a client error (4xx).
 * If the HTTP status is not a client error, an exception is thrown.
 *
 * @param callableName The name of the callable function or context in which this validation is performed. Can be null.
 * @param parameterName The name of the specific parameter being validated. Can be null.
 * @param message A custom error message to include in the exception if the validation fails. Can be null.
 * @param causeOf A transformer that generates a throwable based on the HTTP status if the validation fails. Can be null.
 * @param cause A transformer that generates a throwable which may be used as the cause of the exception if the validation fails. Can be null.
 * @return The current HTTP status if it is a client error.
 * @throws ValidationFailedException If the HTTP status is not a client error and no alternative throwable is generated via `causeOf`.
 * @throws Throwable If the `causeOf` transformer is provided and generates an exception.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateClientError(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isClientError) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not a client error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not a client error", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status represents a client error (HTTP status codes 4xx).
 * If the status is not a client error, throws a `ValidationFailedException` or a transformed exception.
 *
 * @param callableName The name of the callable being validated, used for context in the exception.
 * @param parameter The parameter being validated, used for context in the exception.
 * @param message Optional custom validation failure message.
 * @param causeOf A transformer function that generates a throwable for the validation failure based on the HTTP status.
 * @param cause A transformer function that generates a throwable cause for the validation failure based on the HTTP status.
 * @return The current HTTP status if it represents a client error.
 * @throws ValidationFailedException If the HTTP status is not a client error and no custom transformer is provided in `causeOf`.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateClientError(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isClientError) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not a client error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not a client error", cause?.invoke(this)))
    return this
}

/**
 * Validates if the HTTP status represents a server error (status code 5xx). 
 * Throws an exception if the status is not a server error.
 *
 * @param causeOf An optional transformer that generates a custom exception 
 *                based on the provided HTTP status. It will be used as the result of the exception 
 *                if provided and the status is not a server error.
 * @param cause   An optional transformer that generates a throwable cause based on the 
 *                provided HTTP status. This will be used as the cause of the exception 
 *                thrown if the status is not a server error.
 * @return The original HTTP status if it represents a server error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateServerError(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isServerError) throw if (causeOf == null) ValidationFailedException("$this is not a server error.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$this is not a server error.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the HTTP status code represents a server error (status code 5xx).
 * If the status code is not a server error, throws a `ValidationFailedException` 
 * with a custom message and an optional cause.
 *
 * @param causeOf A transformer function that, given the current HTTP status, produces a `Throwable` 
 *                to be thrown as the cause of the exception. Defaults to `null`.
 * @param cause A transformer function that, given the current HTTP status, produces an optional 
 *              cause `Throwable` for the `ValidationFailedException`. Defaults to `null`.
 * @param lazyMessage A transformer function that generates a custom validation failure message 
 *                    based on the current HTTP status.
 * @return The current `HttpStatus` object if it is a server error.
 * @throws ValidationFailedException If the HTTP status code is not a server error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateServerError(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null, lazyMessage: Transformer<HttpStatus, Any>): HttpStatus {
    if (!isServerError) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the HTTP status represents a server error (5xx status codes). If the status is not
 * a server error, a [ValidationFailedException] is thrown.
 *
 * @param property An optional [KProperty] to provide additional context about the validation, which may 
 *                 be used in the exception.
 * @param variableName An optional variable name to include in the validation failure message.
 * @param message An optional custom error message to use when validation fails.
 * @param causeOf An optional transformer function to generate the root cause of the exception based on the HTTP status.
 * @param cause An optional transformer function to generate an additional cause for the exception based on the HTTP status.
 * @return The current [HttpStatus] instance if it represents a server error.
 * @throws ValidationFailedException if the HTTP status does not represent a server error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateServerError(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isServerError) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not a server error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not a server error", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status represents a server error (status code 5xx). 
 * If the status is not a server error, it throws a `ValidationFailedException` or a custom throwable 
 * generated by the provided transformer(s).
 *
 * @param property The optional property that caused the validation failure, used for context in the exception.
 * @param variable The optional variable that caused the validation failure, used for context in the exception.
 * @param message An optional custom error message to be included in the exception if the validation fails.
 * @param causeOf A transformer function that takes the current HTTP status and returns a custom throwable 
 *                for cases where the validation fails. If provided, the resulting throwable will be used as the primary exception.
 * @param cause A transformer function that takes the current HTTP status and returns a custom throwable 
 *              to be set as the cause for the validation failure exception.
 * @return The current HTTP status if it is a server error.
 * @throws ValidationFailedException If the status is not a server error and no `causeOf` transformer is provided.
 * @throws Throwable If the status is not a server error and a `causeOf` transformer provides a custom throwable.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateServerError(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isServerError) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not a server error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not a server error", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status represents a server error (5xx series).
 * If the status is not a server error, an exception is thrown.
 *
 * @param callable The function associated with the validation, if applicable.
 * @param parameterName The name of the parameter being validated, if applicable.
 * @param message An optional custom error message for the exception.
 * @param causeOf A transformer that generates a custom cause exception based on the HTTP status.
 * @param cause A transformer that generates a custom exception cause, invoked if `causeOf` is null.
 * @return The current HTTP status if it represents a server error.
 * @throws ValidationFailedException If the HTTP status does not represent a server error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateServerError(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isServerError) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not a server error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not a server error", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status represents a server error (5xx range). If not, it throws a validation exception.
 *
 * @param callable The callable function related to the context of validation, which may be null.
 * @param parameter The specific parameter associated with the validation context, which may be null.
 * @param message An optional custom message to describe the validation failure. Defaults to null.
 * @param causeOf A transformer to create a custom throwable cause specific to the validation failure, which may be null.
 * @param cause An additional transformer to create a custom throwable cause, which may be null.
 * @return The current HTTP status if it represents a server error.
 * @throws ValidationFailedException If the HTTP status is not a server error and validation fails.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateServerError(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isServerError) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not a server error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not a server error", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the HTTP status represents a server error (5xx range) and throws an exception if it does not.
 *
 * @param callableName Optional name of the callable entity related to the validation. Used for exception context.
 * @param parameterName Optional name of the parameter that caused the validation failure. Used for exception context.
 * @param message Optional custom error message to describe the validation failure.
 * @param causeOf An optional transformer function to generate a throwable cause based on the HTTP status.
 * @param cause An optional transformer function to generate an additional cause throwable based on the HTTP status.
 * @return The same HttpStatus instance, if the status is a server error.
 * @throws ValidationFailedException If the status is not a server error.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateServerError(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isServerError) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not a server error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not a server error", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status represents a server error (5xx status code).
 * If the status is not a server error, this method throws a validation exception.
 *
 * @param callableName The name of the callable (e.g., function or method) associated with the validation context.
 * @param parameter The parameter associated with the validation context, if applicable.
 * @param message An optional custom message to include in the validation exception.
 * @param causeOf A transformer that converts the current HTTP status into a specific Throwable as the root cause of the validation failure.
 * @param cause A transformer that converts the current HTTP status into a specific Throwable to include in the validation exception.
 * @return The current HTTP status if the validation passes (i.e., it is a server error).
 * @throws ValidationFailedException If the status is not a server error, the exception is thrown with details provided by the input arguments.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateServerError(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isServerError) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not a server error", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not a server error", cause?.invoke(this)))
    return this
}

/**
 * Validates if the HTTP status code is informational (1xx), throwing an exception if not.
 *
 * @param causeOf An optional transformer that maps the HTTP status to a throwable to be thrown in case validation fails.
 * @param cause An optional transformer that maps the HTTP status to a throwable, which will be used as the cause of the exception.
 * @return The HTTP status if it is informational.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateInformational(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isInformational) throw if (causeOf == null) ValidationFailedException("$this is not an informational code.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$this is not an informational code.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the HTTP status is informational (1xx). If not informational, throws a validation exception.
 *
 * @param causeOf A transformer that generates a Throwable cause based on the HTTP status. Optional and defaults to null.
 * @param cause A secondary transformer that generates a Throwable cause based on the HTTP status. Optional and defaults to null.
 * @param lazyMessage A transformer that generates an informational message or description based on the HTTP status.
 * @return The same HTTP status if it is informational.
 * @throws ValidationFailedException if the HTTP status is not informational, optionally with a generated cause and message.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateInformational(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null, lazyMessage: Transformer<HttpStatus, Any>): HttpStatus {
    if (!isInformational) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the HTTP status code is informational (1xx). If the status is not informational, throws a 
 * validation exception with the provided details.
 *
 * @param property The property being validated, if applicable, or null if not associated with a specific property.
 * @param variableName Optional name of the variable associated with this validation for error reporting purposes.
 * @param message Optional custom error message to use if the validation fails.
 * @param causeOf A transformer that generates an exception from the provided HTTP status code, used for encapsulating 
 *                the validation failure cause. If not provided, the default exception is thrown.
 * @param cause A transformer that generates an exception to be used as the root cause of the validation failure, 
 *              based on the provided HTTP status code.
 * @return The current HTTP status code if it passes the validation.
 * @throws ValidationFailedException if the HTTP status code is not informational (1xx).
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateInformational(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isInformational) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not an informational code", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not an informational code", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status code is informational (1xx).
 * Throws a validation exception if the status code is not informational.
 *
 * @param property The property associated with the validation context, can be null.
 * @param variable The variable associated with the validation context, can be null.
 * @param message A custom validation failure message, optional.
 * @param causeOf A transformer function to generate the main exception that includes additional context, optional.
 * @param cause A transformer function to generate a nested cause exception, optional.
 * @return The current HTTP status code if it passes the informational validation.
 * @throws ValidationFailedException If the status code is not informational, with details derived from the provided parameters.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateInformational(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isInformational) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not an informational code", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not an informational code", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the HTTP status is informational (1xx status code).
 * If the status code is not informational, this method throws a ValidationFailedException or another specified exception.
 *
 * @param callable The callable function where the validation is invoked, used for context in the exception.
 * @param parameterName The name of the parameter being validated, used for context in the exception.
 * @param message An optional custom message for the exception if validation fails.
 * @param causeOf A transformer function to create a Throwable instance representing the cause of the validation failure.
 *                If provided, it will be used to create the exception thrown on failure.
 * @param cause A secondary transformer function to create an additional cause Throwable for the exception.
 *              This is primarily used to append more context to the exception chain.
 * @return The HTTP status being validated if it passes the informational validation check.
 * @throws ValidationFailedException If the HTTP status is not informational and no custom cause function is provided.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateInformational(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isInformational) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not an informational code", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not an informational code", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status is an informational status code (1xx).
 * If the status code is not informational, an exception is thrown, optionally with a custom message or cause.
 *
 * @param callable The callable context related to the validation, can be null.
 * @param parameter The parameter context related to the validation, can be null.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf A transformer to generate a custom exception, invoked if validation fails.
 * @param cause An optional transformer to generate a cause of the validation failure.
 * @return The original HttpStatus if the validation passes.
 * @throws ValidationFailedException If the HTTP status is not an informational status code.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateInformational(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isInformational) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not an informational code", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not an informational code", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status is informational (1xx status code). 
 * If the status is not informational, an exception is thrown.
 *
 * @param callableName Optional name of the callable context in which the validation is performed.
 * @param parameterName Optional name of the parameter being validated.
 * @param message Optional custom validation error message.
 * @param causeOf Optional transformer to determine the exception to be thrown based on the HTTP status.
 * @param cause Optional transformer to determine the cause of the exception based on the HTTP status.
 * @return The current HttpStatus instance if it passes the informational validation.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateInformational(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isInformational) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not an informational code", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not an informational code", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status code is informational (1xx). If the code is not informational, 
 * a `ValidationFailedException` or a transformed `Throwable` is thrown based on the provided parameters.
 *
 * @param callableName The name of the callable being validated. Can be null.
 * @param parameter The parameter related to the validation. Can be null.
 * @param message An optional custom message for the exception. Defaults to a generic message if null.
 * @param causeOf A transformer to generate a specific cause `Throwable` based on the current HTTP status. Can be null.
 * @param cause A transformer to generate a nested cause `Throwable` based on the current HTTP status. Can be null.
 * @return The current HTTP status if it is informational.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateInformational(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isInformational) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not an informational code", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not an informational code", cause?.invoke(this)))
    return this
}

/**
 * Validates whether the HTTP status code represents a redirection status. 
 * If the status is not a redirection, an exception is thrown.
 *
 * @param causeOf A transformer function that takes the HTTP status as input and returns a throwable 
 *                to be used as the cause of the exception if validation fails. Can be null.
 * @param cause   A transformer function that takes the HTTP status as input and returns a throwable 
 *                to be used as an additional cause for the exception. Can be null.
 * @return The same HTTP status if it represents a redirection.
 * @throws ValidationFailedException if the HTTP status is not a redirection.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateRedirection(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isRedirection) throw if (causeOf == null) ValidationFailedException("$this is not a redirection.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("$this is not a redirection.", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status represents a redirection. If not, throws an exception with
 * a provided lazy message and optional cause transformers.
 *
 * @param causeOf A transformer that converts the HTTP status into a throwable to be thrown. If null, a 
 *                default exception is created using the lazy message and cause transformers.
 * @param cause A transformer that converts the HTTP status into a throwable cause for the exception.
 * @param lazyMessage A transformer that generates a message based on the HTTP status when the 
 *                    validation fails.
 * @return The HTTP status itself if the validation passes.
 * @throws Throwable If the HTTP status is not a redirection, the exception defined by the 
 *                   causeOf transformer or a default exception is thrown.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateRedirection(causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null, lazyMessage: Transformer<HttpStatus, Any>): HttpStatus {
    if (!isRedirection) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status code represents a redirection (3xx status codes).
 * If the status code is not a redirection, an exception is thrown.
 *
 * @param property An optional property associated with the validation, typically used to provide 
 *     context in error messages.
 * @param variableName An optional variable name associated with the context of the validation,
 *     often used in constructing error messages.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf An optional transformer that provides a custom exception to throw when the
 *     validation fails, based on the current HTTP status.
 * @param cause An optional transformer providing the root cause of the exception, based on the
 *     current HTTP status.
 * @return The same HTTP status if it represents a redirection. Throws an exception otherwise.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateRedirection(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isRedirection) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not a redirection", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not a redirection", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status represents a redirection status. Throws an exception if the validation fails.
 *
 * @param property The property related to the validation, can be null.
 * @param variable An additional variable related to the validation, can be null.
 * @param message An optional custom message to be used in the exception if validation fails.
 * @param causeOf A transformer to generate a throwable based on the HTTP status, used as the cause of the throwable.
 * @param cause An optional transformer to generate a throwable based on the HTTP status if validation fails.
 * @return The current instance of HttpStatus if the validation passes.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateRedirection(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isRedirection) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not a redirection", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not a redirection", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status code represents a redirection. If the status code
 * is not a redirection, it throws a validation exception.
 *
 * @param callable The callable function associated with the validation, if any.
 * @param parameterName The name of the parameter being validated, if applicable.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf A transformation function that generates a custom exception to be thrown 
 *                based on this HTTP status code when validation fails.
 * @param cause A transformation function that generates the cause of the exception to be thrown 
 *              based on this HTTP status code when validation fails.
 * @return The current `HttpStatus` if the validation passes.
 * @throws ValidationFailedException if the HTTP status is not a redirection and validation fails.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateRedirection(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isRedirection) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not a redirection", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not a redirection", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status code represents a redirection status. If the status 
 * is not a redirection, a validation exception is thrown with the specified parameters.
 *
 * @param callable The callable function that is associated with the validation context, or null 
 * if no specific function is applicable.
 * @param parameter The parameter of the callable function associated with the validation context, 
 * or null if no specific parameter is applicable.
 * @param message An optional custom validation failure message. Defaults to "is not a redirection" 
 * if not provided.
 * @param causeOf A transformer function to generate a custom exception based on the HTTP status 
 * code when validation fails.
 * @param cause A transformer function to create a chained cause exception based on the HTTP 
 * status code.
 * @return The current HTTP status if it represents a redirection status.
 * @throws ValidationFailedException if the HTTP status code is not a redirection status.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateRedirection(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isRedirection) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not a redirection", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not a redirection", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current HTTP status code represents a redirection (3xx series).
 * If the status code is not a redirection, throws a [ValidationFailedException] or an exception
 * transformed by the provided `causeOf` or `cause` transformers.
 *
 * @param callableName The name of the callable being validated, used for contextual information in the exception.
 * @param parameterName The name of the parameter being validated, used for contextual information in the exception.
 * @param message Optional custom message to include in the exception if validation fails.
 * @param causeOf A transformer function to produce a specific exception based on the current status and its context.
 * @param cause Another transformer function to produce a specific exception to chain as the cause of the validation failure.
 * @return The current HTTP status, if it is a redirection status.
 * @throws ValidationFailedException If the HTTP status is not a redirection and no custom transformer is provided.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateRedirection(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isRedirection) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not a redirection", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not a redirection", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current HTTP status code represents a redirection (3xx).
 * Throws a `ValidationFailedException` or a custom exception if the condition fails.
 *
 * @param callableName An optional name of the callable being validated.
 * @param parameter An optional reference to the parameter being validated.
 * @param message An optional custom error message to be used when validation fails.
 * @param causeOf An optional transformer function to produce a custom exception using the current HTTP status.
 * @param cause An optional transformer function to create a root cause exception using the current HTTP status.
 * @return The current HTTP status if validation is successful.
 * @since 5.1.0
 */
@IgnorableReturnValue
fun HttpStatus.validateRedirection(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<HttpStatus, Throwable>? = null, cause: Transformer<HttpStatus, Throwable>? = null): HttpStatus {
    if (!isRedirection) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not a redirection", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not a redirection", cause?.invoke(this)))
    return this
}