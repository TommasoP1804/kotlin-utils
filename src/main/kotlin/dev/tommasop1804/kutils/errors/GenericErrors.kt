/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.errors

import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType

/**
 * Marker interface for representing errors within the system.
 * This interface serves as a common type for all error-related data structures
 * and can be used as a base type for creating various domain-specific error hierarchies.
 *
 * Implementers of this interface are used for conveying specific error scenarios,
 * enabling structured error handling and categorization.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
interface Error

/**
 * Represents a generic error that can be used as a fallback or catch-all error type.
 * This object implements the `Error` interface and can be employed in cases where
 * no specific error type is applicable or available.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
object GenericError : Error

// ----------------------------------------------------------------------------------

/**
 * Represents an error encountered during parsing operations.
 *
 * This interface serves as a marker for all parsing-related errors, enabling
 * a unified way to handle and categorize parsing issues. Implementations of
 * this interface may provide additional details about specific errors such as
 * invalid data format, unrecognized patterns, or failed conversions.
 *
 * Examples of concrete implementations include:
 * - InvalidFormat: Signals that the input format is invalid.
 * - NoMatchingFormat: Indicates that no matching format could be found for the input.
 *
 * Use this interface to define error types for parsing domains and provide
 * descriptive and structured error handling in parsing workflows.
 *
 * @author Tommaso Pastorelli
 * @since 6.1.0
 */
interface ParsingError : Error

/**
 * Represents an error that occurs when a value cannot be parsed into the expected target class due to an invalid format.
 *
 * @constructor
 * Creates an instance of `InvalidFormat` with a given invalid value, the target class, and an optional reason.
 *
 * @property invalidValue The value that could not be parsed due to an invalid format.
 * @property targetClass The expected class type into which the parsing was attempted.
 * @property reason An optional description of why the parsing failed.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
data class InvalidFormat(val invalidValue: Any?, val targetType: KType, val reason: String? = null) : ParsingError {
    /**
     * Constructs an instance of `InvalidFormat` with the provided invalid value, target class,
     * and a throwable whose message will serve as the reason for the error.
     *
     * @param invalidValue The value that does not conform to the expected format.
     * @param targetType The target class that the value was expected to conform to.
     * @param throwable The throwable whose message represents the reason for the invalid format.
     * @since 6.1.0
     */
    constructor(invalidValue: Any?, targetType: KType, throwable: Throwable) : this(invalidValue, targetType, throwable.message)
}
/**
 * Represents an error that occurs when a given input does not conform to any expected format
 * for the target class during parsing operations.
 *
 * This error is raised specifically when a `CharSequence` fails to match any valid subtypes
 * associated with a target class during the parsing process. For example, in the context of parsing
 * an EAN (International Article Number), this error is generated when all format checks
 * for EAN variants fail, despite the input being properly structured.
 *
 * @property invalidValue The input value that could not be matched to any format.
 * @property targetType The class type against which the input was evaluated.
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
data class NoMatchingFormat(val invalidValue: Any?, val targetType: KType) : ParsingError

/**
 * Represents an error that occurs during a type conversion process when the conversion cannot be completed
 * due to an invalid or incompatible value.
 *
 * @property invalidValue The value that could not be converted. Can be null.
 * @property fromType The class of the value that was being converted from.
 * @property toType The target class to which the conversion was attempted.
 * @property reason An optional message providing additional details about why the conversion failed.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
data class InvalidConversion(val invalidValue: Any?, val fromType: KType, val toType: KType, val reason: String? = null) {
    /**
     * Secondary constructor for the InvalidConversion class that allows initialization of
     * an instance using a throwable to provide the reason for the invalid conversion.
     *
     * @param invalidValue The value that could not be converted.
     * @param fromType The source class type of the value.
     * @param toType The target class type to which conversion was attempted.
     * @param throwable The throwable whose message describes the reason for the failed conversion.
     * @since 6.1.0
     */
    constructor(invalidValue: Any?, fromType: KType, toType: KType, throwable: Throwable) : this(invalidValue, fromType, toType, throwable.message)
}

/**
 * Represents an error that arises when a computation cannot be performed.
 *
 * This error class is used to indicate cases where an operation or computation
 * is deemed impossible to execute. The optional `reason` property can be used
 * to provide a detailed explanation for why the computation is uncomputable.
 *
 * @param reason an optional explanation or message describing why the computation is uncomputable.
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
data class Uncomputable(val reason: String? = null) : Error

// ----------------------------------------------------------------------------------

/**
 * Represents an error that indicates the requirement of an element.
 *
 * This interface is commonly used as a marker for errors related to missing or required elements
 * in various contexts, such as properties, parameters, or collections.
 *
 * Implementations of this interface may provide additional details about the specific
 * element that is required or the nature of the requirement failure.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
interface RequiredElement : Error

/**
 * Represents a required property with its name and type information. This data class
 * is used to specify properties that must be present and describes their characteristics.
 *
 * @property propertyName The name of the required property, or null if not explicitly specified.
 * @property propertyType The Kotlin type of the required property, or null if not explicitly specified.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
data class RequiredProperty(val propertyName: String? = null, val propertyType: KType? = null) : RequiredElement {
    /**
     * Secondary constructor for the `RequiredProperty` class.
     *
     * Initializes a `RequiredProperty` instance using the provided `KProperty`.
     *
     * @param property The Kotlin property used to initialize the instance.
     * Its `name` is assigned to `propertyName`, and its `returnType` is assigned to `propertyType`.
     *
     * @see RequiredProperty
     * @since 6.1.0
     */
    constructor(property: KProperty<*>) : this(propertyName = property.name, propertyType = property.returnType)
}
/**
 * Represents a required parameter in a method or constructor, encapsulating its name and type information.
 *
 * @property parameterName The name of the parameter, or null if it's unavailable.
 * @property parameterType The type of the parameter, represented as a KType instance, or null if it's unavailable.
 *
 * @constructor Creates an instance by directly assigning the parameter name and type.
 * @constructor Creates an instance based on a given KParameter, extracting its name and type.
 *
 * Implements the [RequiredElement] interface to indicate that this represents a mandatory parameter.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
data class RequiredParameter(val parameterName: String? = null, val parameterType: KType? = null) : RequiredElement {
    /**
     * Secondary constructor for creating a [RequiredParameter] instance from a [KParameter].
     * Initializes the [parameterName] with the name of the given parameter,
     * and the [parameterType] with the type of the given parameter.
     *
     * @param parameter The [KParameter] used to initialize the [RequiredParameter] instance.
     * @since 6.1.0
     */
    constructor(parameter: KParameter) : this(parameterName = parameter.name, parameterType = parameter.type)
}

// ----------------------------------------------------------------------------------

/**
 * Represents validation-specific errors within the system.
 * This interface extends the base `Error` type to provide a framework for
 * categorizing and handling errors that arise due to validation failures.
 *
 * It serves as a contract for defining various types of validation error
 * scenarios, enabling structured error representation and processing.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
interface ValidationError : Error {
    /**
     * Represents a validation error that occurs when a generic validation failure is encountered.
     *
     * This error is used to indicate a validation issue that does not fall into specialized
     * error categories. The `message` property provides descriptive information about the
     * nature of the validation failure.
     *
     * @property message A human-readable message describing the validation error.
     * @since 6.1.0
     */
    data class ValidationFailed(val message: String) : ValidationError
    /**
     * Represents a validation error that occurs when an expected value does not match the actual value.
     *
     * This error is used to indicate discrepancies between what is expected and
     * what is observed during validation processes. The `expected` property holds
     * the anticipated value, while the `actual` property holds the observed value.
     *
     * @property obj The object that was being validated.
     * @property expected The value that was expected.
     * @property actual The value that was observed.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class ExpectationMismatch(val obj: Any?, val expected: Any?, val actual: Any?) : ValidationError
}

// ----------------------------------------------------------------------------------

/**
 * Represents an error that indicates an illegal or invalid operation was attempted.
 *
 * This error is specifically intended to capture and convey information
 * about operations that are disallowed within a given context or violate
 * the expected constraints of a system. The `message` property provides
 * additional details about the nature of the illegal operation.
 *
 * Implements the `Error` marker interface to enable structured categorization
 * and handling of errors within the system.
 *
 * @property message A textual description of the illegal operation.
 * @since 6.1.0
 */
data class IllegalOperation(val message: String) : Error
/**
 * Represents an unsupported operation within the system.
 *
 * This error is used to signify that a particular operation or feature is
 * not supported, usually due to limitations in implementation or domain-specific
 * constraints. The associated message provides additional details about
 * the unsupported operation.
 *
 * @param message A descriptive message explaining why the operation is unsupported.
 * @since 6.1.0
 */
data class UnsupportedOperation(val message: String) : Error