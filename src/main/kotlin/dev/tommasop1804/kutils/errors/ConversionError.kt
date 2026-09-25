/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.errors

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.measure.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.reflect.KType

/**
 * Represents a type of error that occurs during a conversion process.
 *
 * This interface extends the base `Error` type to provide a more specific
 * categorization of issues that arise when data or objects are being converted
 * from one form or type to another.
 *
 * Implementers of this interface may define concrete error types that describe
 * the specific nature of the conversion failure, enabling more granular error
 * handling and debugging.
 *
 * This interface is designed to be used in a wide array of conversion-related
 * scenarios, including but not limited to data format transformations, type
 * casting, and serialization/deserialization operations.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
interface ConversionError : Error {
    override val linkedException: ConversionException
}

/**
 * Represents an error occurring during a type conversion process where the conversion is deemed invalid.
 *
 * This class provides context about the failed conversion by specifying the invalid value,
 * the source type, and the target type, along with an optional reason describing the failure.
 *
 * @property invalidValue The value that could not be converted.
 * @property from The source type of the conversion.
 * @property to The target type of the conversion.
 * @property reason An optional description of the reason for the invalid conversion.
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
open class InvalidConversion(open val invalidValue: Any?, val from: String, val to: String, open val reason: String? = null) : ConversionError {
    override val linkedException get() = ConversionException("Conversion of `${invalidValue}` from $from to $to failed${if (reason != null) " because: $reason." else String.EMPTY}")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InvalidConversion

        if (invalidValue != other.invalidValue) return false
        if (from != other.from) return false
        if (to != other.to) return false
        if (reason != other.reason) return false

        return true
    }

    override fun hashCode(): Int {
        var result = invalidValue.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + to.hashCode()
        result = 31 * result + reason.hashCode()
        return result
    }

    override fun toString(): String = "InvalidConversion(invalidValue=$invalidValue, from=$from, to=$to, reason=$reason)"
}

/**
 * Represents an error that occurs during a type conversion process when the conversion cannot be completed
 * due to an invalid or incompatible value.
 *
 * @property invalidValue The value that could not be converted. Can be null.
 * @property fromType The class of the value that was being converted from.
 * @property toType The target class to which the conversion was attempted.
 * @property reason An optional message providing additional details about why the conversion failed.
 *
 * @since 6.3.0
 * @author Tommaso Pastorelli
 */
data class InvalidTypeConversion(override val invalidValue: Any?, val fromType: KType, val toType: KType, override val reason: String? = null) : InvalidConversion(
    invalidValue, fromType.toString(), toType.toString(), reason
) {
    /**
     * Secondary constructor for the InvalidConversion class that allows initialization of
     * an instance using a throwable to provide the reason for the invalid conversion.
     *
     * @param invalidValue The value that could not be converted.
     * @param fromType The source class type of the value.
     * @param toType The target class type to which conversion was attempted.
     * @param throwable The throwable whose message describes the reason for the failed conversion.
     * @since 6.3.0
     */
    constructor(invalidValue: Any?, fromType: KType, toType: KType, throwable: Throwable) : this(invalidValue, fromType, toType, throwable.message)
}

/**
 * Represents an error that occurs when an illegal conversion
 * attempt is made between two types or formats.
 *
 * This class is a specific implementation of the [ConversionError] interface
 * and is used to encapsulate details about a conversion failure, including:
 * - The value that caused the error.
 * - The source type or format.
 * - The target type or format.
 * - An optional reason providing additional context on why the conversion failed.
 *
 * @property invalidValue The value that caused the conversion to fail.
 *                        This represents the input that could not be successfully
 *                        converted from the source type to the target type.
 * @property from The source type or format of the conversion.
 *                Specifies the origin type or format in which the invalid value
 *                was initially present.
 * @property to The target type or format of the conversion.
 *              Indicates the destination type or format that the conversion failed to achieve.
 * @property reason An optional descriptive message providing details about why the
 *                  conversion is deemed invalid or why it could not be performed.
 *                  This may include technical or user-level information regarding the issue.
 *
 * The class overrides standard methods such as `equals`, `hashCode`, and `toString` to
 * provide equality checking, hash code computation, and string representation tailored
 * for instances of `IllegalConversion`.
 *
 * @author Tommaso Pastorelli
 * @since 6.1.0
 */
open class IllegalConversion(open val invalidValue: Any?, val from: String, val to: String, open val reason: String? = null) : ConversionError {
    override val linkedException get() = ConversionException("Conversion of `${invalidValue}` from $from to $to is illegal${if (reason != null) " because: $reason." else String.EMPTY}")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IllegalConversion

        if (invalidValue != other.invalidValue) return false
        if (from != other.from) return false
        if (to != other.to) return false
        if (reason != other.reason) return false

        return true
    }

    override fun hashCode(): Int {
        var result = invalidValue.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + to.hashCode()
        result = 31 * result + reason.hashCode()
        return result
    }

    override fun toString(): String = "IllegalConversion(invalidValue=$invalidValue, from=$from, to=$to, reason=$reason)"
}

/**
 * Represents an error occurring during an illegal conversion between two types.
 *
 * This data class provides information about the conversion attempt, including
 * the value being converted, the source type, and the target type. It extends
 * the [IllegalConversion] class, adding additional context specific to type-related
 * conversion errors.
 *
 * @property invalidValue The value that failed to be converted.
 * @property fromType The source type from which the conversion was attempted.
 * @property toType The target type to which the conversion was attempted.
 * @property reason An optional message providing additional details on the cause of the error.
 *                  If not provided, it may default to the message of an associated exception.
 * @constructor Creates an instance of this class using the provided details about the failed conversion.
 * @constructor Overloaded constructor that also accepts a [Throwable], utilizing its message as the reason.
 * @author Tommaso Pastorelli
 * @since 6.3.0
 */
data class IllegalTypeConversion(override val invalidValue: Any?, val fromType: KType, val toType: KType, override val reason: String? = null) : IllegalConversion(
    invalidValue, fromType.toString(), toType.toString(), reason
) {
    /**
     * Secondary constructor for creating an instance of `IllegalConversionBetweenTypes`
     * with an additional `Throwable` parameter to extract the message as the `reason`.
     *
     * This constructor is useful for cases where the invalid conversion error originates
     * from a specific exception, and the exception's message serves as the rationale for
     * the illegal conversion between types.
     *
     * @param invalidValue The value that caused the illegal conversion.
     * @param fromType The source type involved in the conversion.
     * @param toType The target type involved in the conversion.
     * @param throwable The throwable instance that provides context for the error, specifically its message.
     * @since 6.3.0
     */
    constructor(invalidValue: Any?, fromType: KType, toType: KType, throwable: Throwable) : this(invalidValue, fromType, toType, throwable.message)
}

/**
 * Represents an exception that is thrown when an attempt is made to convert between two incompatible scalar units.
 *
 * This exception encapsulates details about the invalid conversion, including the value that caused the issue
 * and the source and target units involved in the conversion. It extends the IllegalConversion class
 * to provide additional context specific to scalar unit conversions.
 *
 * @property invalidValue The value that could not be converted between the specified units.
 * @property fromUnit The source unit of the attempted conversion.
 * @property toUnit The target unit of the attempted conversion.
 * @constructor Creates an instance of the exception with details about the invalid conversion.
 * @author Tommaso Pastorelli
 * @since 6.3.0
 */
data class IllegalUnitConversion(override val invalidValue: Any?, val fromUnit: ScalarUnit, val toUnit: ScalarUnit) : IllegalConversion(
    invalidValue, fromUnit.unitName, toUnit.unitName, "Incompatible measure (${fromUnit.measure} -> ${toUnit.measure})"
)