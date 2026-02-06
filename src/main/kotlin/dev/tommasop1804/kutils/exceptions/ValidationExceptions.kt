package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.*
import kotlin.reflect.*

/**
 * Exception thrown to indicate that a validation process has failed.
 * This exception can be used to signal that the validation of input data
 * did not satisfy required conditions or rules.
 *
 * This exception is typically used in scenarios where input validation logic
 * determines that the provided data cannot be accepted for further processing.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class ValidationFailedException : RuntimeException {
    /**
     * Default constructor for ValidationFailedException.
     * This exception is thrown to indicate that a validation process has failed.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new ValidationFailedException with the specified detail message.
     *
     * @param message the detail message, which can be null.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a ValidationFailedException instance with a detailed error message composed of the provided property,
     * variable name, additional message, and an optional cause.
     *
     * The generated error message provides details about the variable name (if not null or blank), the property's
     * owning class, the property's name, and its return type. If an additional message is provided, it is appended
     * at the end of the formatted error message.
     *
     * @param property The property associated with the validation failure. Can be null if not applicable.
     * @param variableName Optional name of the variable involved in the validation.
     *                     Included in the message if it is not null or blank.
     * @param message Additional descriptive information about the validation failure. Defaults to an empty string.
     * @param cause The underlying throwable causing this exception, or null if not applicable.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variableName: String? = null, message: String? = "") : super($$"($${if (variableName.isNotNullOrBlank()) "`$variableName` of type " else ""}`$${property?.ownerClass?.simpleName}$$${property?.name}` of type `$${property?.returnType}`)$${if (message.isNotNullOrEmpty()) " $message" else ""}")
    /**
     * Constructs a `ValidationFailedException` instance with detailed information about the
     * parameter that failed validation in a callable function.
     *
     * @param callable The `KFunction` whose parameters are being validated. It can be nullable.
     * @param parameterName The name of the parameter that caused the validation failure. It can be nullable.
     * @param message An optional custom message providing additional context for the validation failure.
     *                Defaults to an empty string if not provided.
     * @throws IllegalArgumentException If the `callable` or `parameterName` is invalid or
     *         inconsistent with the parameter's details in the `callable`.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameterName: String?, message: String? = "") : super($$"(<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameterName }?.name}` of type `$${callable?.parameters?.find { it.name == parameterName }?.type}`)$${if (message.isNotNullOrEmpty()) " $message" else ""}")
    /**
     * Constructs a `ValidationFailedException` instance with detailed information about the
     * parameter that failed validation in a callable function.
     *
     * @param callableName The `KFunction` whose parameters are being validated. It can be nullable.
     * @param parameterName The name of the parameter that caused the validation failure. It can be nullable.
     * @param message An optional custom message providing additional context for the validation failure.
     *                Defaults to an empty string if not provided.
     * @throws IllegalArgumentException If the `callable` or `parameterName` is invalid or
     *         inconsistent with the parameter's details in the `callable`.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameterName: String?, message: String? = "") : super($$"(<parameters of `$$callableName`>$`$$parameterName`)$${if (message.isNotNullOrEmpty()) " $message" else ""}")

    /**
     * Constructs an instance of `ValidationFailedException` with a detailed message
     * derived from the provided `callable` function, its `parameter`, and an optional `message`.
     *
     * @param callable The function associated with the validation failure. Can be null.
     * @param parameter The specific parameter of the function that caused the validation failure. Can be null.
     * @param message An optional additional message providing more context for the validation failure.
     * Defaults to an empty string if not provided.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameter: KProperty<*>?, message: String? = "") : super($$"(<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameter?.name }?.name}` of type `$${callable?.parameters?.find { it.name == parameter?.name }?.type}`)$${if (message.isNotNullOrEmpty()) " $message" else ""}")
    /**
     * Constructs an instance of `ValidationFailedException` with a detailed message
     * derived from the provided `callable` function, its `parameter`, and an optional `message`.
     *
     * @param callableName The function associated with the validation failure. Can be null.
     * @param parameter The specific parameter of the function that caused the validation failure. Can be null.
     * @param message An optional additional message providing more context for the validation failure.
     * Defaults to an empty string if not provided.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameter: KProperty<*>?, message: String? = "") : super($$"(<parameters of `$$callableName`>$`$${parameter?.name}` of type `$${parameter?.returnType}`)$${if (message.isNotNullOrEmpty()) " $message" else ""}")

    /**
     * Constructs a new ValidationFailedException with the specified cause.
     * This constructor allows wrapping another throwable as the cause
     * of the ValidationFailedException.
     *
     * @param cause the cause of this exception, which can be null.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
    /**
     * Constructs a new ValidationFailedException with the specified detail message
     * and cause. This constructor allows both a custom error message and an underlying
     * cause to be provided for better error context.
     *
     * @param message the detail message, which can be null.
     * @param cause the cause that triggered this exception, which can be null.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a ValidationFailedException instance with a detailed error message composed of the provided property,
     * variable name, additional message, and an optional cause.
     *
     * The generated error message provides details about the variable name (if not null or blank), the property's
     * owning class, the property's name, and its return type. If an additional message is provided, it is appended
     * at the end of the formatted error message.
     *
     * @param property The property associated with the validation failure. Can be null if not applicable.
     * @param variableName Optional name of the variable involved in the validation.
     *                     Included in the message if it is not null or blank.
     * @param message Additional descriptive information about the validation failure. Defaults to an empty string.
     * @param cause The underlying throwable causing this exception, or null if not applicable.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variableName: String? = null, message: String? = "", cause: Throwable?) : super(
        $$"($${if (variableName.isNotNullOrBlank()) "`$variableName` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`)$${if (message.isNotNullOrEmpty()) " $message" else ""}", cause)
    /**
     * Constructs a new instance of the ValidationFailedException with a detailed error message and an optional cause.
     *
     * The error message is generated based on the provided property, variable, and message arguments.
     * - If the `variable` is not null or blank, its name and type will be included in the message.
     * - The `property` will be used to provide the class, name, and return type information.
     * - If the `message` is not null or empty, it will be appended to the generated message.
     *
     * @param property the main KProperty associated with the validation failure, or null if not specified
     * @param variable an optional secondary KProperty that provides additional context, or null if not specified
     * @param message an optional message providing additional details about the validation failure
     * @param cause the underlying cause of the exception, or null if no cause is specified
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variable: KProperty<*>? = null, message: String? = "", cause: Throwable?) : super(
        $$"($${if (variable?.name.isNotNullOrBlank()) "`${variable.name}` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`)$${if (message.isNotNullOrEmpty()) " $message" else ""}", cause)
    /**
     * Constructs a ValidationFailedException instance with a detailed message based on the provided callable's parameter
     * and optional custom message and cause for the exception.
     *
     * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can be null.
     * @param parameterName The name of the parameter in the given callable that caused the validation issue. Can be null.
     * @param message An optional custom message providing additional details about the validation failure. Default is an empty string.
     * @param cause The underlying cause of the validation failure, represented as a `Throwable`. Can be null.
     *
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameterName: String?, message: String? = "", cause: Throwable?) : super($$"(<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameterName }?.name}` of type `$${callable?.parameters?.find { it.name == parameterName }?.type}`)$${if (message.isNotNullOrEmpty()) " $message" else ""}", cause)
    /**
     * Constructs a new ValidationFailedException with details about the callable, parameter,
     * additional message, and underlying cause.
     *
     * @param callableName the name of the callable (e.g., function or method) related to the validation failure
     * @param parameterName the name of the parameter that caused the validation failure
     * @param message an optional custom message providing additional details about the validation failure
     * @param cause the underlying cause of the exception, or null if no cause is specified
     * @since 1.0.0
     */
    constructor(callableName: String?, parameterName: String?, message: String? = "", cause: Throwable?) : super($$"(<parameters of `$${callableName}`>$`$$parameterName`)$${if (message.isNotNullOrEmpty()) " $message" else ""})", cause)
    /**
     * Constructs a ValidationFailedException with detailed information about the callable, parameter, optional message, and cause.
     *
     * @param callable the [KFunction] related to the validation failure, or null if not applicable
     * @param parameter the [KParameter] representing the parameter involved in the validation failure, or null if not applicable
     * @param message an optional message providing additional context about the validation failure, defaulting to an empty string
     * @param cause the underlying cause of the validation failure, or null if no cause is specified
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameter: KParameter?, message: String? = "", cause: Throwable?) : super($$"(<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameter?.name }?.name}` of type `$${callable?.parameters?.find { it.name == parameter?.name }?.type}`)$${if (message.isNotNullOrEmpty()) " $message" else ""}", cause)
    /**
     * Constructs a ValidationFailedException with details about the validation failure.
     *
     * This constructor allows the specification of:
     * - the name of the callable (e.g., function or property)
     * - the parameter related to the validation failure
     * - an optional error message providing more context
     * - an optional cause (another Throwable that triggered this exception).
     *
     * The resulting exception message includes details about the parameter's name,
     * its return type, and the additional message if provided.
     *
     * @param callableName The name of the callable where validation failed, or null if not specified.
     * @param parameter The KParameter instance representing the parameter that failed validation, or null if not applicable.
     * @param message An optional error message providing additional details about the validation failure.
     * @param cause The underlying cause of the exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameter: KParameter?, message: String? = "", cause: Throwable?) : super($$"(<parameters of `$${callableName}`>$`$${parameter?.name}` of type `$${parameter?.type}`)$${if (message.isNotNullOrEmpty()) " $message" else ""}", cause)
}

/**
 * A specific type of `InputMismatchException` that indicates
 * an input is malformed or does not conform to the expected format or structure.
 *
 * This exception can be thrown to signal an input parsing or validation failure
 * where the provided input cannot be processed appropriately.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class MalformedInputException : IllegalArgumentException {
    /**
     * Constructs a MalformedInputException with no detail message.
     *
     * This exception indicates that malformed or inappropriate input was encountered.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new `MalformedInputException` with the specified detail message.
     *
     * @param message the detail message saved for later retrieval by the `Throwable.message` property.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `MalformedInputException` with a message indicating that the input
     * should be formatted like the specified type.
     *
     * @param type the expected type used to build the detail message, may be null.
     * @since 1.0.0
     */
    constructor(type: KType?) : super("Verify that the input is settable as $type")
    /**
     * Constructs a new `MalformedInputException` with a detail message indicating
     * that the input should be castable as an instance of the specified class.
     *
     * @param `class` the KClass object representing the type that the input is expected to be castable to.
     * @since 1.0.0
     */
    constructor(`class`: KClass<*>?) : super("Verify that the input is settable as ${`class`?.qualifiedName}")
    /**
     * Constructs a new `MalformedInputException` with the specified detail message and cause.
     *
     * This constructor allows for creating an exception instance by specifying
     * an error message and an underlying cause. It is useful for adding detailed context
     * to the exception and enables exception chaining.
     *
     * @param message A detailed message describing the malformed input error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `MalformedInputException` with the specified cause.
     *
     * This constructor allows creating an exception instance
     * by specifying the underlying reason for the malformed input.
     * It facilitates exception chaining by associating this exception
     * with another throwable cause.
     *
     * @param cause The cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that is thrown when an expectation mismatch occurs, typically during
 * the validation of properties, parameters, or values in a program.
 *
 * This exception is designed to provide detailed information about the offending property,
 * parameter, or value, as well as the expected and actual conditions. It is commonly used
 * in scenarios where specific states, types, or values are anticipated but are not met.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class ExpectationMismatchException : ValidationFailedException {
    /**
     * Creates an instance of the `ExpectationMismatchException` class with no additional details.
     *
     * This constructor initializes the exception without providing any specific message or cause.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new instance of the exception with the specified message.
     *
     * @param message The detail message describing the reason for the exception.
     *                May be null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an `ExpectationMismatchException` with a detailed error message based on the provided parameters.
     *
     * This exception is typically used when a particular expectation for a value does not match the actual value.
     *
     * @param property the `KProperty` associated with the expectation mismatch, or null if not applicable
     * @param variableName an optional variable name to include in the error message
     * @param expectation the expected value or condition
     * @param value the actual value that caused the mismatch
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variableName: String? = null, expectation: Any?, value: Any?) : super(
        $$"($${if (variableName.isNotNullOrBlank()) "`$variableName` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType})` was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}"
    )
    /**
     * Constructs an instance of `ExpectationMismatchException` with the given property, variable,
     * expectation, and value, providing detailed information about the mismatch.
     *
     * @param property The primary property being evaluated. Can be null.
     * @param variable An optional variable associated with the expectation. Defaults to null.
     * @param expectation The expected value for the property or variable. Can be null.
     * @param value The actual value of the property or variable. Can be null or empty.
     *
     * The exception message includes details about the type and name of the property
     * and optionally the variable, as well as describing the expected and actual values.
     *
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variable: KProperty<*>? = null, expectation: Any?, value: Any?) : super(
        $$"($${if (variable?.name.isNotNullOrBlank()) "`${variable.name}` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`) was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}"
    )
    /**
     * Constructs an `ExpectationMismatchException` that describes a mismatch between an expected
     * and actual value for a specific parameter of a callable function.
     *
     * @param callable The callable function whose parameter caused the expectation mismatch, or null if unspecified.
     * @param parameterName The name of the parameter associated with the expectation mismatch, or null if unspecified.
     * @param expectation The expected value for the parameter.
     * @param value The actual value that caused the mismatch.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameterName: String?, expectation: Any?, value: Any?) : super(
        $$"(<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameterName }?.name}` of type `$${callable?.parameters?.find { it.name == parameterName }?.type}`) was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}"
    )
    /**
     * Constructs an `ExpectationMismatchException` that describes a mismatch between an expected
     * and actual value for a specific parameter of a callable function.
     *
     * @param callableName The callable function whose parameter caused the expectation mismatch, or null if unspecified.
     * @param parameterName The name of the parameter associated with the expectation mismatch, or null if unspecified.
     * @param expectation The expected value for the parameter.
     * @param value The actual value that caused the mismatch.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameterName: String?, expectation: Any?, value: Any?) : super(
        $$"(<parameters of `$$callableName`>$`$$parameterName``) was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}"
    )
    /**
     * Constructs an `ExpectationMismatchException` with detailed context about an expectation mismatch.
     *
     * The exception message is dynamically built based on the provided values, including
     * the `callable` (function) name, the parameter property causing the mismatch, the expected
     * value, and the actual mismatched value.
     *
     * @param callable The function whose parameter is under evaluation, or null.
     * @param parameter The parameter of the function's parameter that is being compared, or null.
     * @param expectation The value that the parameter was expected to have, or null.
     * @param value The actual value of the parameter, which caused the mismatch.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameter: KParameter?, expectation: Any?, value: Any?) : super(
        $$"(<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameter?.name }?.name}` of type `$${callable?.parameters?.find { it.name == parameter?.name }?.type}`) was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}"
    )
    /**
     * Constructs an `ExpectationMismatchException` with detailed context about an expectation mismatch.
     *
     * The exception message is dynamically built based on the provided values, including
     * the `callable` (function) name, the parameter property causing the mismatch, the expected
     * value, and the actual mismatched value.
     *
     * @param callableName The function whose parameter is under evaluation, or null.
     * @param parameter The parameter of the function's parameter that is being compared, or null.
     * @param expectation The value that the parameter was expected to have, or null.
     * @param value The actual value of the parameter, which caused the mismatch.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameter: KParameter?, expectation: Any?, value: Any?) : super(
        $$"(<parameters of `$$callableName`>$`$${parameter?.name}` of type `$${parameter?.type}`) was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}"
    )
    /**
     * Constructs a new instance of `ExpectationMismatchException` with the specified cause.
     *
     * @param cause The cause of the exception, which can be accessed later through the {@link Throwable#getCause()} method. A null value is permitted,
     *              and indicates that the cause is nonexistent or unknown.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
    /**
     * Constructs an instance of ExpectationMismatchException with the provided detail message and cause.
     *
     * @param message The detail message, which provides additional information about the exception.
     * @param cause The cause of the exception, which can be used for exception chaining.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs an `ExpectationMismatchException` with detailed information about the
     * property or variable that caused the mismatch, including its name, owning class,
     * type, expected value, actual value, and an optional cause.
     *
     * The error message is dynamically generated based on the provided `property`, `variableName`,
     * `expectation`, and `value` parameters. If the `variableName` is not blank, it is included
     * in the message along with its type. Information about the property's owning class,
     * name, and return type is also injected when available.
     *
     * @param property The optional property representing the field or value involved in the mismatch.
     * @param variableName The optional name of the variable if explicitly provided.
     * @param expectation The expected value that the property or variable was supposed to have.
     * @param value The actual value of the property or variable that caused the mismatch.
     * @param cause An optional `Throwable` which represents the underlying cause of the exception.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variableName: String? = null, expectation: Any?, value: Any?, cause: Throwable?) : super(
        $$"($${if (variableName.isNotNullOrBlank()) "`$variableName` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`) was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}", cause)
    /**
     * Constructs an ExpectationMismatchException with detailed information about the mismatch
     * between the expected and actual values of a property or variable.
     *
     * @param property the KProperty associated with the exception, or null if no property is involved
     * @param variable an optional secondary KProperty related to the mismatch, or null if not applicable
     * @param expectation the value that was expected
     * @param value the actual value that was encountered
     * @param cause an optional cause of the exception, typically another Throwable, or null if not applicable
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variable: KProperty<*>? = null, expectation: Any?, value: Any?, cause: Throwable?) : super(
        $$"($${if (variable?.name.isNotNullOrBlank()) "`${variable.name}` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`) was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}", cause)
    /**
     * Constructs an instance of `ExpectationMismatchException`.
     *
     * This constructor provides detailed information about a mismatch between the expected
     * and actual values in the parameters of a given callable element. It generates a descriptive
     * message indicating the parameter name, type, expected value, and actual value.
     *
     * @param callable The Kotlin function which contains the parameter that caused the mismatch.
     *                 Can be `null` if the context of the callable is unavailable.
     * @param parameterName The name of the parameter within the callable for which the expectation was mismatched.
     *                      Can be `null` if the parameter name is unavailable.
     * @param expectation The expected value or condition for the given parameter. Can be `null`.
     * @param value The actual value of the parameter that failed to meet the expectation. Can be `null`.
     * @param cause The underlying cause of the exception. Can be `null`.
     *
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameterName: String?, expectation: Any?, value: Any?, cause: Throwable?) : super(
        $$"(<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameterName }?.name}` of type `$${callable?.parameters?.find { it.name == parameterName }?.type}`) was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}", cause)
    /**
     * Constructs an `ExpectationMismatchException` with detailed information about the mismatch between
     * the expected and actual value for a given parameter within a callable context.
     *
     * @param callableName the name of the callable where the expectation mismatch occurred, or `null` if unavailable
     * @param parameterName the name of the parameter involved in the expectation mismatch, or `null` if unavailable
     * @param expectation the value or condition that was expected
     * @param value the actual value that did not match the expected value
     * @param cause the underlying cause of the mismatch, or null if there is no additional cause
     * @since 1.0.0
     */
    constructor(callableName: String?, parameterName: String?, expectation: Any?, value: Any?, cause: Throwable?) : super(
        $$"(<parameters of `$$callableName`>$`$$parameterName`) was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}", cause)
    /**
     * Constructs an instance of `ExpectationMismatchException` with detailed information
     * about the expectation mismatch, including the callable function, the specific parameter,
     * the expected value, the actual value, and an optional cause for the exception.
     *
     * This constructor generates a descriptive error message based on the provided inputs,
     * indicating which parameter of the callable did not meet the expected value and what
     * the actual value was.
     *
     * @param callable the function in which the expectation mismatch occurred
     * @param parameter the parameter or parameter in the function that did not meet the expectation
     * @param expectation the expected value or condition
     * @param value the actual value that caused the mismatch
     * @param cause the underlying cause of the exception, or null if no cause is specified
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameter: KParameter?, expectation: Any?, value: Any?, cause: Throwable?) : super(
        $$"(<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameter?.name }?.name}` of type `$${callable?.parameters?.find { it.name == parameter?.name }?.type}`) was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}", cause)
    /**
     * Constructs an `ExpectationMismatchException` with a detailed message and cause.
     *
     * This exception is used to indicate that a specified expectation for a parameter
     * in a callable function or property is not met. The message provides details about
     * the mismatch, including the callable name, parameter, expected value, and actual value.
     *
     * @param callableName The name of the callable function or property being evaluated, or null.
     * @param parameter The parameter whose expected value is being compared, or null.
     * @param expectation The expected value of the property, or null.
     * @param value The actual value received for the property.
     * @param cause The underlying cause of the exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameter: KParameter?, expectation: Any?, value: Any?, cause: Throwable?) : super(
        $$"(<parameters of `$${callableName}>`$`$${parameter?.name}` of type `$${parameter?.type}`) was expected $${
            if (expectation.isNull()) "to be null" else "as ${
                if (expectation.toString().isBlank()) "\"\"" else expectation
            }"
        }, but is $${if (value == "") "\"\"" else value}", cause)
}

/**
 * This exception is thrown to indicate that an actual class does not match an expected class.
 * Typically used in scenarios where type expectations are enforced.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class ClassMismatchException : ExpectationMismatchException {
    /**
     * Initializes a new instance of the `ClassMismatchException` class with no detail message.
     *
     * This constructor creates a generic `ClassMismatchException` instance without
     * providing specific error details or context about the mismatch.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `ClassMismatchException` with the expected class provided.
     *
     * This exception is thrown when a mismatch between the expected and actual class
     * occurs. The expected class is passed as an argument, and a corresponding error
     * message is generated to provide context about the mismatch.
     *
     * @param expectedClass The class that was expected, causing this exception to be thrown.
     * @param cause The underlying cause of the exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(expectedClass: KClass<*>, cause: Throwable? = null) : super("Expected class was ${expectedClass.qualifiedName}", cause)
    /**
     * Constructs a `ClassMismatchException` with the specified expected and actual classes.
     *
     * This constructor creates an exception instance that provides a detailed error message
     * highlighting the mismatch between the expected and actual class types.
     *
     * If the `expectedClass` is null, the error message will indicate the actual class.
     * Otherwise, it specifies both the expected and actual class types in the error message.
     *
     * @param expectedClass The expected class type, or null if no specific class is expected.
     * @param actualClass The actual class type that caused the mismatch.
     * @param cause The underlying cause of the exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(expectedClass: KClass<*>?, actualClass: KClass<*>, cause: Throwable? = null) : super(
        if (expectedClass.isNull()) "Actual class: ${actualClass.qualifiedName}"
        else "Expected class was $expectedClass, but got ${actualClass.qualifiedName}"
        , cause)
    /**
     * Constructs a `ClassMismatchException` with the specified detail message.
     *
     * This constructor can be used to provide additional context regarding the specific
     * nature of a class mismatch error. The detail message can aid in debugging and
     * understanding the cause of the mismatch.
     *
     * @param message The detail message describing the class mismatch error, or null if no specific message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `ClassMismatchException` with the specified detail message and cause.
     *
     * This constructor allows for creating an exception instance with a custom error message
     * and an underlying cause. It is useful for providing detailed information about the
     * mismatched class error and for supporting exception chaining.
     *
     * @param message A detailed message describing the mismatch error, or null if no specific message is provided.
     * @param cause The root cause of the exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `ClassMismatchException` with the specified cause.
     *
     * This constructor creates an exception instance based on another exception that caused the class mismatch.
     * It facilitates exception chaining and provides additional context to the mismatch error.
     *
     * @param cause The underlying cause of the class mismatch, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
    /**
     * Constructs a `ClassMismatchException` with detailed information about the mismatch
     * between the expected and actual types of the given property or variable.
     *
     * This constructor generates an error message describing the mismatch, including
     * the variable name, its type, the owner's class name, and the expected type.
     *
     * @param property The property that caused the exception, or null if it is not applicable.
     * @param variableName An optional name of the variable associated with the property, or null if not specified.
     * @param expectedClass The expected class type for the property or variable.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variableName: String? = null, expectedClass: KClass<*>) : super(
        $$"($${if (variableName.isNotNullOrBlank()) "`$variableName` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType})` was expected to be of type `$${expectedClass.qualifiedName}`"
    )
    /**
     * Constructs a `ClassMismatchException` with the given properties and expected class type.
     *
     * This constructor initializes the exception with a detailed message that describes
     * the mismatch between the provided property or variable and the expected class type.
     * It constructs a formatted string using the property or variable information,
     * where applicable, to provide meaningful context about the class mismatch.
     *
     * @param property The property that caused the exception, or null if not available.
     * @param variable An optional related variable, or null if not provided.
     * @param expectedClass The class type that was expected.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variable: KProperty<*>? = null, expectedClass: KClass<*>?) : super(
        $$"($${if (variable.isNotNull()) "`${variable.name}` of type " else ""}`$${property?.ownerClass?.qualifiedName}`$`$${property?.name}` of type `$${property?.returnType})` was expected to be of type `$${expectedClass?.qualifiedName}`"
    )
    /**
     * Constructs an instance of `ClassMismatchException` with a detailed message about the parameter and expected class type.
     *
     * This constructor generates an exception message that indicates a mismatch between the type of a specific parameter
     * within the given function and the expected class type. It is used to detail the parameter name, its current type,
     * and the expected type. The exception message is constructed dynamically based on the provided arguments.
     *
     * @param callable The function object whose parameter is being validated. Can be null if no function context is provided.
     * @param parameterName The name of the parameter being checked for type mismatch. Can be null if unnamed.
     * @param expectedClass The class representing the expected type of the parameter. Can be null if no specific type is expected.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameterName: String?, expectedClass: KClass<*>?) : super(
        $$"(<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameterName }?.name}` of type `$${callable?.parameters?.find { it.name == parameterName }?.type}`) was expected to be of type `$${expectedClass?.qualifiedName}`"
    )
    /**
     * Constructs an instance of the `ClassMismatchException` with the specified callable name,
     * parameter name, expected class, and actual class.
     *
     * This exception is used to indicate a type mismatch where an actual class type does not
     * match the expected class type for a given callable and parameter. It provides additional
     * context by including the callable name, parameter name, and the conflicting types in its
     * error message.
     *
     * @param callableName The name of the callable where the type mismatch occurred, or null if unspecified.
     * @param parameterName The name of the parameter that caused the type mismatch, or null if unspecified.
     * @param expectedClass The expected class type for the parameter, or null if unspecified.
     * @param actualClass The actual class type of the parameter, or null if unspecified.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameterName: String?, expectedClass: KClass<*>?, actualClass: KClass<*>?) : super(
        $$"(<parameters of `$$callableName`>$`$$parameterName` of type `$$actualClass`) was expected to be of type `$${expectedClass?.qualifiedName}`"
    )
    /**
     * Constructs a `ClassMismatchException` with detailed information about the mismatched parameter.
     *
     * This constructor is used to create an instance of `ClassMismatchException` with information
     * about the callable function, the parameter that caused the mismatch, and the expected class type.
     * It generates a detailed message indicating which parameter was expected to be of a specific type.
     *
     * @param callable The callable function whose parameter type mismatch occurred, or null if unspecified.
     * @param parameter The specific parameter that caused the mismatch, or null if unspecified.
     * @param expectedClass The expected class type for the parameter, or null if unspecified.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameter: KParameter?, expectedClass: KClass<*>?) : super(
        $$"(<parameters of `$${callable?.name}`>$`$${parameter?.name}` of type `$${parameter?.type}`) was expected to be of type `$${expectedClass?.qualifiedName}`"
    )
    /**
     * Constructs a `ClassMismatchException` with details about the class mismatch issue.
     *
     * This exception is thrown when a value provided to a callable parameter is not of the
     * expected type, providing information about the callable name, parameter, and expected type.
     *
     * @param callableName The name of the callable where the mismatch occurred, or null if not specified.
     * @param parameter The parameter of the callable that caused the mismatch, or null if not specified.
     * @param expectedClass The expected class type of the parameter, or null if not specified.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameter: KParameter?, expectedClass: KClass<*>?) : super(
        $$"(<parameters of `$${callableName}`>$`$${parameter?.name}` of type `$${parameter?.type}`) was expected to be of type `$${expectedClass?.qualifiedName}`"
    )
    /**
     * Constructs a `ClassMismatchException` with detailed information about a property type mismatch.
     *
     * This constructor is used to create an exception instance when a property's runtime type
     * does not match the expected type. It provides detailed context in the exception message,
     * including the variable name, property details (if available), and the expected type.
     *
     * The generated message will include:
     * - The variable name if provided and not blank.
     * - The owner class and property name if the `KProperty` is supplied.
     * - The actual type of the property and the expected class type.
     *
     * @param property The property whose type does not match the expected type, or null if not provided.
     * @param variableName The name of the variable being checked, or null if not available.
     * @param expectedClass The runtime class that was expected for the property, or null if not specified.
     * @param cause An optional cause for this exception, allowing exception chaining, or null if not provided.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variableName: String? = null, expectedClass: KClass<*>?, cause: Throwable?) : super(
        $$"($${if (variableName.isNotNullOrBlank()) "`$variableName` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType})` was expected to be of type `$${expectedClass?.qualifiedName}`",
        cause
    )
    /**
     * Constructs a `ClassMismatchException` with a detailed error message and an optional cause.
     *
     * This exception is designed to handle cases where a mismatch between an expected class type
     * and an actual value type occurs during runtime. The error message provides information
     * about the property or variable involved in the mismatch, its expected type,
     * and the actual type encountered.
     *
     * @param property The `KProperty` instance representing the property associated with the mismatch, or null if not applicable.
     * @param variable The `KProperty` instance representing the variable involved in the mismatch, or null if not applicable.
     * @param expectedClass The expected `KClass` type the property or variable should conform to, or null if not specified.
     * @param cause The underlying `Throwable` cause of the exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variable: KProperty<*>? = null, expectedClass: KClass<*>?, cause: Throwable?) : super(
        $$"($${if (variable.isNotNull()) "`${variable.name}` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType})` was expected to be of type `$${expectedClass?.qualifiedName}`",
        cause
    )
    /**
     * Constructs a `ClassMismatchException` with detailed context about the parameter type mismatch.
     *
     * This constructor is called when there is an expectation for a parameter in a callable
     * function to be of a specific type, and this expectation is violated.
     *
     * @param callable The `KFunction` instance representing the callable in which the parameter type mismatch occurred, or null if not provided.
     * @param parameterName The name of the parameter that caused the mismatch, or null if not provided.
     * @param expectedClass The expected `KClass` that the parameter type should conform to, or null if not specified.
     * @param cause The underlying `Throwable` cause of this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameterName: String?, expectedClass: KClass<*>?, cause: Throwable?) : super(
        $$"(<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameterName }?.name}` of type `$${callable?.parameters?.find { it.name == parameterName }?.type}`) was expected to be of type `$${expectedClass?.qualifiedName}`",
        cause
    )
    /**
     * Constructs a `ClassMismatchException` with detailed information about the expected and actual class types.
     *
     * This exception is thrown to indicate a mismatch between the expected and actual types for a given parameter
     * during runtime. It is primarily used for error reporting when type expectations are violated within a callable method.
     *
     * @param callableName The name of the callable (e.g., method or function) where the type mismatch occurred, or null if not specified.
     * @param parameterName The name of the parameter that has the type mismatch, or null if not specified.
     * @param expectedClass The expected class type for the parameter, or null if not specified.
     * @param actualClass The actual class type found for the parameter, or null if not specified.
     * @param cause The underlying cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameterName: String?, expectedClass: KClass<*>?, actualClass: KClass<*>?, cause: Throwable?) : super(
        $$"(<parameters of `$$callableName`>$`$$parameterName` of type `$$actualClass`) was expected to be of type `$${expectedClass?.qualifiedName}`",
        cause
    )
    /**
     * Constructs a `ClassMismatchException` with detailed information about the type mismatch
     * that occurred during a reflective call.
     *
     * This constructor is used to create an exception instance that provides specific details
     * about a parameter or return value of a callable (such as a function or constructor) that
     * was expected to match a particular class type but did not. The exception includes the
     * callable name, parameter name, parameter type, and the expected class, along with an
     * optional cause for deeper context.
     *
     * @param callable The callable (e.g., function or constructor) where the mismatch occurred, or null if not applicable.
     * @param parameter The parameter in the callable that caused the mismatch, or null if not applicable.
     * @param expectedClass The class type expected for the parameter or return value, or null if not specified.
     * @param cause The underlying cause of the exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameter: KParameter?, expectedClass: KClass<*>?, cause: Throwable?) : super(
        $$"(<parameters of `$${callable?.name}`>$`$${parameter?.name}` of type `$${parameter?.type}`) was expected to be of type `$${expectedClass?.qualifiedName}`",
        cause
    )
    /**
     * Constructs an instance of `ClassMismatchException` with a detailed message describing the
     * mismatch between an expected class type and an actual parameter encountered in a callable.
     *
     * The exception message is dynamically generated to include the name of the callable,
     * the parameter, its type, and the expected class type. Additionally, an optional cause
     * can be provided to indicate the underlying reason for the error.
     *
     * @param callableName The name of the callable in which the class mismatch occurred, or null if not provided.
     * @param parameter The parameter that caused the exception, or null if not specified.
     * @param expectedClass The expected class type for the parameter, or null if not specified.
     * @param cause The root cause of the exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(callableName: String?, parameter: KParameter?, expectedClass: KClass<*>?, cause: Throwable?) : super(
        $$"(<parameters of `$${callableName}`>$`$${parameter?.name}` of type `$${parameter?.type}`) was expected to be of type `$${expectedClass?.qualifiedName}`",
        cause
    )
}

/**
 * Exception thrown to indicate that a field is duplicated during validation.
 * This exception is a specific case of the [ValidationFailedException], typically used
 * when a validation process fails due to multiple occurrences of a field that is expected to be unique.
 *
 * It provides constructors to support different use cases, such as creating the exception
 * with a custom message or including the cause of the validation failure.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused")
open class DuplicateFieldException : ValidationFailedException {
    /**
     * Constructs a new instance of `DuplicateFieldException` without a specific error message or cause.
     *
     * This exception is thrown to indicate that a validation error related to duplicate fields has occurred.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new `DuplicateFieldException` with the specified detail message.
     *
     * @param message the detail message, which allows for a more specific description
     *                of the exception condition.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a DuplicateFieldException with a custom message based on the provided property and variable name.
     *
     * If a variable name is provided and is not null or blank, it is included
     * in the message along with its type. Additionally, if the property is
     * provided, it is used to form a message that describes the property's
     * declaring class, name, and type.
     *
     * @param property the Kotlin reflection property used to describe the field
     * @param variableName an optional name of the variable to include in the exception message
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variableName: String? = null) : super($$"$${if (variableName.isNotNullOrBlank()) "`$variableName` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`")
    /**
     * Constructs a `DuplicateFieldException` with a custom message based on the given properties.
     *
     * @param property The primary Kotlin property involved, which is used to describe the duplicate field.
     *                 Can be nullable.
     * @param variable An additional mandatory Kotlin property.
     *
     * If `variable.isNotNull()` evaluates to `true`, its name is included in the exception message.
     * The exception message also incorporates the containing class name, property name, and its return type.
     *
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variable: KProperty<*>) : super($$"$${if (variable.isNotNull()) "`${variable.name}` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`")
    /**
     * Constructs a new DuplicateFieldException with a detailed message generated
     * from the provided callable function and parameter name.
     *
     * @param callable The callable function where the parameter is located, or null if not specified.
     * @param parameterName The name of the parameter for which the exception is being constructed, or null if not specified.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameterName: String?) : super($$"<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameterName }?.name}` of type `$${callable?.parameters?.find { it.name == parameterName }?.type}`")
    /**
     * Constructs an exception instance with a detailed message based on the provided callable function and parameter name.
     *
     * @param callable The Kotlin function from which the parameter is extracted.
     * @param parameter The name of the parameter in the specified function.
     *
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameter: KProperty<*>) : super($$"<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameter.name }?.name}` of type `$${callable?.parameters?.find { it.name == parameter.name }?.type}`")
    /**
     * Constructs a new `DuplicateFieldException` with the specified cause.
     *
     * This constructor allows for wrapping a throwable that caused the validation error
     * into a `DuplicateFieldException`.
     *
     * @param cause the underlying cause of this exception, or `null` if no cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
    /**
     * Constructs a new `DuplicateFieldException` with the specified detail message and cause.
     *
     * @param message the detail message, or `null` if no message is provided.
     * @param cause the cause of the exception, or `null` if the cause is not provided.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a DuplicateFieldException with a detailed message and an optional cause.
     *
     * The exception message includes the type of the variable and property, if available.
     * If `variableName` is provided and is not null or blank, it will be included in the message.
     *
     * @param property the property involved in the exception, nullable
     * @param variableName the name of the variable associated with the property, nullable
     * @param cause the cause of the exception, nullable
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variableName: String? = null, cause: Throwable?) : super($$"$${if (variableName.isNotNullOrBlank()) "`$variableName` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`", cause)
    /**
     * Constructor for the `DuplicateFieldException` class.
     *
     * Constructs a new exception that represents a duplicate field within a class.
     *
     * @param property The property of interest, allowing the exception to identify the duplicate field. Can be null.
     * @param variable A KProperty instance referring to the variable associated with the duplicate field.
     * @param cause The underlying cause of the exception, if available. Can be null.
     *
     * The exception message includes details regarding the property and variable. The `isNotNull` function
     * determines if the `variable` contains a non-null value, impacting the construction of the exception message.
     *
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, variable: KProperty<*>?, cause: Throwable?) : super($$"$${if (variable.isNotNull()) "`${variable.name}` of type " else ""}`$${property?.ownerClass?.simpleName}`$`$${property?.name}` of type `$${property?.returnType}`", cause)
    /**
     * Constructs a DuplicateFieldException with a detailed message based on the provided callable function, parameter name,
     * and cause of the exception.
     *
     * @param callable A Kotlin function whose parameter is causing the exception. It can be null.
     * @param parameterName The name of the parameter associated with the exception. It can be null.
     * @param cause The underlying cause of the exception. It can be null.
     *
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameterName: String?, cause: Throwable?) : super($$"<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameterName }?.name}` of type `$${callable?.parameters?.find { it.name == parameterName }?.type}`", cause)
    /**
     * Constructs a new DuplicateFieldException with the specified callable name, parameter name, and cause.
     *
     * This constructor is used when duplicate fields are encountered,
     * specifying the source callable name and parameter name for detailed error representation.
     * The error message follows the format: "<parameters of {callableName}>#{parameterName}".
     *
     * @param callableName the name of the callable associated with the duplicate field, or null if not provided
     * @param parameterName the name of the parameter that was found to be duplicate, or null if not provided
     * @param cause the underlying cause of the exception, or null if no cause is specified
     * @since 1.0.0
     */
    constructor(callableName: String?, parameterName: String?, cause: Throwable?) : super($$"<parameters of `$${callableName}`>$`$${parameterName}`", cause)
    /**
     * Constructs a new instance of the exception for duplicate field parameters in a call to a function.
     *
     * @param callable The function that contains the parameter with a duplicate field. May be null.
     * @param parameter The property that corresponds to the parameter causing the exception.
     * @param cause The root cause of this exception. May be null.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>?, parameter: KProperty<*>?, cause: Throwable?) : super($$"<parameters of `$${callable?.name}`>$`$${callable?.parameters?.find { it.name == parameter?.name }?.name}` of type `$${callable?.parameters?.find { it.name == parameter?.name }?.type}`", cause)
    /**
     * Constructs a new `DuplicateFieldException` with the specified details about a field duplication error.
     *
     * This constructor initializes an exception with a detailed message that provides context about
     * the duplicated field, including the callable name, the parameter name, and the parameter type.
     * An optional underlying cause for the exception can also be specified.
     *
     * @param callableName the name of the callable entity (e.g., method or property) where the duplication occurred; can be null
     * @param parameter the Kotlin property (`KProperty`) representing the parameter involved in the duplication; may be null
     * @param cause the throwable that triggered this exception, or null if no specific cause is given
     *
     * @since 1.0.0
     */
    constructor(callableName: String?, parameter: KProperty<*>?, cause: Throwable?) : super($$"<parameters of `$${callableName}`>$`$${parameter?.name}` of type `$${parameter?.returnType}`", cause)
}

/**
 * Represents an exception that is thrown when an operation encounters an unexpected
 * issue related to the sign of a number.
 *
 * This exception is designed for scenarios where a number's sign (positive, negative, or zero)
 * does not meet the expected criteria in a given context. It extends `IllegalStateException`
 * and provides multiple constructors to accommodate different use cases, such as specifying
 * a detailed message, a cause, or both.
 *
 * @since 1.0.0
 */
@Suppress("unused")
open class NumberSignException : ValidationFailedException {
    /**
     * Initializes a new instance of the `NumberSignException` class with no detail message.
     *
     * This constructor creates an instance of `NumberSignException` without providing
     * any specific message or additional context. It indicates a generic exception
     * related to number sign operations, without further detail about the cause
     * or nature of the issue.
     *
     * @since 1.0.0
     */
    constructor() : super()

    /**
     * Constructs a `NumberSignException` with the specified detail message.
     *
     * This constructor is used to create a `NumberSignException` with a custom detail message
     * that provides additional information about the context of the exception.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)

    /**
     * Constructs a `NumberSignException` with the specified detail message and cause.
     *
     * This constructor allows for creating an exception instance by specifying
     * a detailed message and the underlying cause. It is useful for providing
     * more context and for enabling exception chaining when handling errors
     * related to the invalid state caused by a number's sign.
     *
     * @param message A detailed message describing the error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)

    /**
     * Constructs a `NumberSignException` with the specified cause.
     *
     * This constructor creates an instance of `NumberSignException` while
     * encapsulating the underlying cause of the exception. It allows
     * for exception chaining and helps provide more context about the
     * origin of the error.
     *
     * @param cause The underlying cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}