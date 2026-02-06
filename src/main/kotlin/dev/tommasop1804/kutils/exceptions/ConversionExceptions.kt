@file:Suppress("unused")

package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.classes.measure.ScalarUnit
import java.util.*
import kotlin.reflect.KClass

/**
 * Represents an exception that is thrown when a conversion operation fails.
 *
 * This exception is a runtime exception intended to signal issues
 * encountered during type or data conversions.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
open class ConversionException : RuntimeException {
    /**
     * Initializes a new instance of the ConversionException class with no detail message.
     *
     * This constructor is used to create an exception instance without additional context or a specific error message.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `ConversionException` with a message indicating the failure of a conversion process
     * between two specific classes.
     *
     * This constructor generates an error message dynamically, specifying the source class and the target
     * class involved in the failed conversion. It is useful for providing context about conversion errors
     * encountered at runtime.
     *
     * @param fromClass The source class from which the conversion was attempted.
     * @param toClass The target class to which the conversion was attempted.
     * @since 1.0.0
     */
    constructor(fromClass: KClass<*>, toClass: KClass<*>) : super(
        "Conversion from ${fromClass.simpleName} to ${toClass.simpleName} failed."
    )
    /**
     * Constructs a `ConversionException` with the specified detail message.
     *
     * This constructor allows the creation of an instance of `ConversionException` with a message
     * providing additional context about what caused the exception. The message is intended to
     * offer more detailed information for debugging or logging purposes.
     *
     * @param message The detail message describing the error. This parameter should not be null.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `ConversionException` with a specified detail message and cause.
     *
     * This constructor allows for providing an error message and the underlying
     * cause of the exception to describe the circumstances of the error in more detail.
     *
     * @param message The detail message associated with this exception.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new ConversionException with the specified cause.
     *
     * This constructor allows creating a ConversionException instance with an underlying
     * cause of the exception, enabling exception chaining and root cause analysis.
     *
     * @param cause the underlying cause of this exception, or null if no cause is provided
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that is thrown when a unit conversion operation fails.
 *
 * This exception is a specialized version of [ConversionException], designed to handle errors
 * that occur during the conversion of values between different units. It provides constructors
 * for specifying the source and target units involved in the failed conversion, enabling
 * detailed context for debugging and logging.
 *
 * @since 1.0.0
 */
open class UnitConversionException : ConversionException {
    /**
     * Constructs a default instance of `UnitConversionException` with no additional context
     * or message. This typically serves as a general-purpose exception for unit conversion failures.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new UnitConversionException with a predefined message indicating the units
     * involved in the conversion failure.
     *
     * @param fromUnit The name of the unit that was the source of the conversion.
     * @param toUnit The name of the unit that was the target of the conversion.
     * @since 1.0.0
     */
    constructor(fromUnit: String, toUnit: String) : super(
        "Conversion from $fromUnit to $toUnit failed."
    )
    /**
     * Constructs a `UnitConversionException` with a detailed message indicating
     * the failure of a unit conversion between two scalar units.
     *
     * @param fromUnit The source scalar unit involved in the failed conversion.
     * @param toUnit The target scalar unit involved in the failed conversion.
     * @since 1.0.0
     */
    constructor(fromUnit: ScalarUnit, toUnit: ScalarUnit) : super(
        "Conversion from ${fromUnit.unitName} to ${toUnit.unitName} failed."
    )
    /**
     * Constructs a new instance of UnitConversionException with the specified detail message.
     *
     * @param message The detail message that provides additional information about the exception.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new `UnitConversionException` with the specified detail message and cause.
     *
     * @param message The detail message, which can be null.
     * @param cause The cause of the exception, which can be null.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `UnitConversionException` with the specified cause.
     * 
     * This constructor allows for creating an exception instance by wrapping another throwable
     * that caused the failure. This is useful for exception chaining, where one exception 
     * directly results from another.
     *
     * @param cause the cause of this exception. May be null, indicating no cause is specified.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that occurs during a currency conversion operation.
 *
 * This exception is thrown when a conversion between two currency types fails,
 * providing context about the source and target currencies or additional details
 * about the failure.
 *
 * @since 1.0.0
 */
open class CurrencyConversionException : ConversionException {
    /**
     * Default constructor for the CurrencyConversionException class.
     * Initializes the exception without any specific message or cause.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `CurrencyConversionException` with a message indicating 
     * the failed conversion between two currencies.
     *
     * @param fromCurrency The currency being converted from.
     * @param toCurrency The currency being converted to.
     * @since 1.0.0
     */
    constructor(fromCurrency: Currency, toCurrency: Currency) : super(
        "Conversion from ${fromCurrency.displayName} (${fromCurrency.currencyCode}) to ${toCurrency.displayName} (${toCurrency.currencyCode}) failed."
    )
    /**
     * Constructs a [CurrencyConversionException] with a detailed message describing the failure
     * to convert between two specified currencies.
     *
     * @param fromCurrency The source currency involved in the failed conversion.
     * @param toCurrency The target currency involved in the failed conversion.
     * @since 1.0.0
     */
    constructor(fromCurrency: dev.tommasop1804.kutils.classes.money.Currency, toCurrency: dev.tommasop1804.kutils.classes.money.Currency) : super(
        "Conversion from ${fromCurrency.currencyName} (${fromCurrency.code}) to ${toCurrency.currencyName} (${toCurrency.code}) failed."
    )
    /**
     * Constructs a new instance of `CurrencyConversionException` with the specified detail message.
     *
     * @param message the detail message, which provides additional information about the exception.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new CurrencyConversionException with the specified detail message and cause.
     * The message provides further context for the exception, while the cause indicates the underlying reason.
     *
     * @param message The detail message, which can be null.
     * @param cause The cause of the exception, which can be null.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new CurrencyConversionException with the specified cause.
     *
     * @param cause the underlying cause of this exception, or null if no cause is provided
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that occurs when a format conversion operation fails.
 *
 * This exception is a subclass of [ConversionException] and is used to indicate
 * that a specific format-to-format conversion has failed. It provides constructors
 * for specifying failure details, making it easier to diagnose and pinpoint the
 * issue during runtime.
 *
 * @since 1.0.0
 */
open class FormatConversionException : ConversionException {
    /**
     * Default constructor for the FormatConversionException class.
     * Initializes a new instance of this exception without any specific message or cause.
     * 
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `FormatConversionException` with a specific error message indicating
     * a failed conversion between two objects.
     *
     * @param from The source object involved in the conversion.
     * @param to The target object involved in the conversion.
     * @since 1.0.0
     */
    constructor(from: Any, to: Any) : super(
        "Conversion from $from to $to failed."
    )
    /**
     * Constructs a new `FormatConversionException` with the specified detail message.
     *
     * @param message The detail message for this exception, or `null` if no detail message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new [FormatConversionException] with the specified detail message and cause.
     *
     * @param message The detail message, which provides additional context about the exception. 
     *                It can be null if no detail message is provided.
     * @param cause The underlying cause of the exception, or null if no cause is specified.
     * 
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new [FormatConversionException] with the specified cause.
     *
     * @param cause The cause of the exception, which can be used to obtain further details 
     * about the reason for the failure. It may be null.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}