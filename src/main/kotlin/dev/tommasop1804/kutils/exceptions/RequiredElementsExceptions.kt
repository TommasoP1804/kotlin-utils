package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.*
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

/**
 * Exception that signals a required field is missing or not populated as expected.
 * This exception is a specialized form of [ValidationFailedException], indicating
 * that a specific required field did not pass validation checks.
 *
 * Typically used in input validation processes where a field marked as required
 * is found to be null, empty, or otherwise invalid.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class RequiredFieldException : ValidationFailedException {
    /**
     * Extracts the internal error code from the associated message by retrieving the substring
     * that precedes the delimiter " @@@ ". If the extracted substring is blank, the result is null.
     *
     * This property provides additional context for errors, particularly when specific internal
     * codes are embedded within the message field.
     *
     * @since 1.0.0
     */
    val internalErrorCode: String?
        get() = message?.before(" @@@ ")?.ifBlank { null }

    /**
     * Constructs a new instance of RequiredFieldException with no detailed message.
     * This exception indicates that a required field in the validation process was missing or incorrect.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new RequiredFieldException with the specified detail message.
     *
     * @param message the detail message, which provides additional information about the exception.
     * @since 1.0.0
     */
    constructor(message: String?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + message)
    /**
     * Constructs an instance of RequiredFieldException with a detailed message
     * describing the context of the provided property and variable name.
     *
     * The message created uses the provided `variableName` and `property` to construct
     * a string that identifies the property's type, its owning class, and its name.
     *
     * For example, if `variableName` is not null or blank, it is included in the message.
     * The `property` parameter provides additional context such as its owning class
     * and return type.
     *
     * @param property the property whose details are used in constructing the exception message
     * @param variableName an optional descriptive name for the variable, included in the message if provided and not blank
     * @throws IllegalArgumentException if the provided parameters cannot construct a valid message
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variableName: String? = null, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"$${if (variableName.isNotNullOrBlank()) "`$variableName` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`")
    /**
     * Constructs a new instance of `RequiredFieldException` with a formatted message based on
     * the provided property and variable name.
     *
     * @param property the property reference, used to construct the error message. Can be nullable.
     * @param variable the variable name property reference used to prepend additional context to the error message.
     * If the `variableName` is not null, its name will be included in the message.
     *
     * The generated error message includes the following details:
     * - If `variableName` is not null, its name will be prefixed with "of type".
     * - The qualified name of the owner's class of `property` is included.
     * - The name and return type of `property` are included.
     *
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variable: KProperty<*>, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"$${if (variable.isNotNull()) "`${variable.name}` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`")
    /**
     * Constructs a new RequiredFieldException with the specified cause.
     * This constructor is typically used when another exception raised during validation serves as the root cause
     * for this exception, allowing it to be chained for better debugging and error analysis.
     *
     * @param cause the underlying cause of the exception, providing detailed information about the validation failure.
     * @since 1.0.0
     */
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY), cause)
    /**
     * Constructs a new RequiredFieldException with the specified detail message and cause.
     *
     * @param message the detail message, which provides additional information about the exception.
     * @param cause the cause of the exception, which can be used to trace the root issue.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + message, cause)
    /**
     * Constructs an instance of `RequiredFieldException` with a message generated from the provided property,
     * variable name, and optional cause. The message describes the property, its owner class, and return type.
     *
     * @param property the Kotlin property associated with the exception, where relevant information such as
     * its owner class, name, and return type will be used in the exception message. Nullable.
     * @param variableName the name of the variable associated with the exception, if any. If present and not blank,
     * it will be included in the exception message. Nullable.
     * @param cause the optional cause of the exception, if any. Nullable.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variableName: String? = null, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"$${if (variableName.isNotNullOrBlank()) "`$variableName` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`", cause)
    /**
     * Constructor for the RequiredFieldException class.
     *
     * This constructor initializes the exception with a message formatted based on the
     * property and variable provided, and optionally includes a cause for the exception.
     *
     * The message includes information such as the variable's name and, if applicable,
     * the property's owning class and return type.
     *
     * @param property The property associated with the exception, or `null` if not applicable.
     * @param variable The variable that caused the exception.
     * @param cause The underlying cause of the exception, or `null` if not applicable.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variable: KProperty<*>, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"$${if (variable.isNotNull()) "`${variable.name}` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`", cause)
}

/**
 * Exception indicating that a required parameter was not provided or failed validation.
 * This exception is typically thrown when one or more required parameters for a callable
 * function or method are either missing or invalid.
 *
 * Inherits from [ValidationFailedException] to provide additional context for parameter-related
 * validation failures, allowing detailed debugging and error tracking in case of invalid input handling.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class RequiredParameterException : ValidationFailedException {
    /**
     * Extracts the internal error code from the associated message by retrieving the substring
     * that precedes the delimiter " @@@ ". If the extracted substring is blank, the result is null.
     *
     * This property provides additional context for errors, particularly when specific internal
     * codes are embedded within the message field.
     *
     * @since 1.0.0
     */
    val internalErrorCode: String?
        get() = message?.before(" @@@ ")?.ifBlank { null }

    /**
     * Constructs a new instance of RequiredParameterException with no specific message or cause.
     *
     * This constructor initializes the RequiredParameterException without any additional context or explanation.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new `RequiredParameterException` with the specified detail message.
     *
     * @param message the detail message, or null if none.
     * @since 1.0.0
     */
    constructor(message: String?, internalErrorCode: String? = null) : super(message)
    /**
     * Constructs an instance of `RequiredParameterException` with a formatted message
     * describing the parameter of the given callable function that caused the exception.
     *
     * The message includes the name of the callable function and the matching parameter's
     * name and type. If the callable or parameter does not exist, the corresponding parts
     * of the message will be null.
     *
     * @param callable The callable function whose parameter caused the exception.
     * @param parameterName The name of the parameter that caused the exception.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameterName: String?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameterName }?.name}` of type `$${callable?.parameters?.find { it.name == parameterName }?.type}`")
    /**
     * Constructs an instance of `RequiredParameterException` with a custom message.
     *
     * @param callable The function reference which contains the parameter.
     * @param parameter The property reference for the parameter.
     *
     * @throws IllegalStateException if the parameter information cannot be constructed correctly.
     *
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameter: KParameter, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameter.name }?.name}` of type `$${callable?.parameters?.find { it.name == parameter.name }?.type}`")
    /**
     * Constructs an exception that describes a missing required parameter of a specific callable.
     *
     * @param callableName The name of the callable (function, method, etc.) missing the required parameter.
     * @param parameterName The name of the required parameter that is missing.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameterName: String?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"<parameters of `$${callableName}`>$`$$parameterName`")
    /**
     * Constructs a new instance of the RequiredParameterException with a custom message
     * based on the provided callable name and parameter.
     *
     * The exception message is dynamically generated using the format:
     * "<parameters of {callableName}>#{parameter?.returnType}".
     *
     * @param callableName the name of the callable entity, which could be null
     * @param parameter the parameter involved in the exception, represented by a [KProperty], which could be null
     * @since 1.0.0
     */
    constructor(callableName: String?, parameter: KParameter?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"<parameters of `$${callableName}`>$`$${parameter?.type}`")
    /**
     * Constructs a new `RequiredParameterException` with the specified cause.
     * This allows wrapping another exception that caused this exception.
     *
     * @param cause the underlying cause of this exception, or null if there is no cause.
     * @since 1.0.0
     */
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY), cause)
    /**
     * Constructs a new [RequiredParameterException] with the specified detail message and cause.
     *
     * @param message the detail message, which can provide more information about the exception (optional).
     * @param cause the underlying cause of the exception (optional).
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + message, cause)
    /**
     * Constructs a new RequiredParameterException with a detailed message based on the provided callable function,
     * parameter name, and optional root cause.
     *
     * The message includes the name of the callable function and the parameter details such as its name
     * and type, if available. This helps identify the missing or invalid parameter that triggered the exception.
     *
     * @param callable The callable function where the exception occurred. It may be null if the function context is unavailable.
     * @param parameterName The name of the parameter that caused the exception. It may be null if the parameter name is not specified.
     * @param cause The root cause of the exception. It may be null if no underlying exception exists.
     *
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameterName: String?, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameterName }?.name}` of type `$${callable?.parameters?.find { it.name == parameterName }?.type}`", cause)
    /**
     * Constructs a new instance of the RequiredParameterException.
     *
     * @param callable The callable function associated with the parameter. Null if no function is specified.
     * @param parameter The KParameter instance representing the required parameter.
     * @param cause The underlying cause of the error, if any. Null if no cause is provided.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameter: KParameter, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameter.name }?.name}` of type `$${callable?.parameters?.find { it.name == parameter.name }?.type}`", cause)
    /**
     * Constructs a `RequiredParameterException` instance with the provided callable name,
     * parameter name, and cause of the exception. This exception is intended to indicate
     * a required parameter that has not been properly specified.
     *
     * @param callableName The name of the callable that requires the parameter. Can be null.
     * @param parameterName The name of the required parameter. Can be null.
     * @param cause The underlying cause of this exception. Can be null.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameterName: String?, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"<parameters of `$${callableName}`>$`$$parameterName`", cause)
    /**
     * Constructs an instance of `RequiredParameterException` with the specified callable name, parameter, and cause.
     *
     * This constructor initializes the instance with a detailed message derived from the provided callable name and parameter name,
     * and optionally wraps an existing exception as its cause.
     *
     * @param callableName the name of the callable associated with the required parameter, or null if not applicable
     * @param parameter the Kotlin property representing the required parameter
     * @param cause the underlying cause of the exception, or null if there is no cause
     *
     * @since 1.0.0
     */
    constructor(callableName: String?, parameter: KParameter?, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + $$"<parameters of `$${callableName}`>$`$${parameter?.name}` of type `$${parameter?.type}`", cause)
}