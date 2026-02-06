package dev.tommasop1804.kutils.classes.numbers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.EMPTY
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.invoke
import dev.tommasop1804.kutils.startsWith
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize


/**
 * Represents an immutable wrapper for an octal numeric value, offering various utility functions
 * for numeric operations and conversions. The value is stored as an octal string.
 *
 * Implements `CharSequence` to allow accessing individual characters and subsequences, `Number`
 * for numeric compatibility, and `Comparable` for comparison with other numbers.
 *
 * This class ensures the internal string representation is a valid octal number.
 *
 * @constructor Initializes the Octal instance with a string representation of the octal number,
 * ensuring that the input is valid. For converting decimal to octal use constructor with number.
 *
 * @throws IllegalArgumentException if the input string is not a valid octal number.
 *
 * @since 1.0.0
 */
@Suppress("unused")
@JsonSerialize(using = Octal.Companion.Serializer::class)
@JsonDeserialize(using = Octal.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Octal.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Octal.Companion.OldDeserializer::class)
class Octal(value: String) : CharSequence, Number(), Comparable<Number> {

    /**
     * Represents a generic string value that can be used across the application
     * for various purposes such as display, configuration, or user input.
     *
     * @since 1.0.0
     */
    private val value: String

    /**
     * Represents the length of the octal value as an integer.
     * The value is determined based on the length of the internal string representation.
     *
     * @since 1.0.0
     */
    override val length: Int
        get() = value.length

    /**
     * Constructs an instance of the Octal class from a given DECIMAL number. The number is converted
     * to its octal string representation internally during the initialization of the object.
     *
     * @param number The numeric value to be represented as an octal string.
     * @since 1.0.0
     */
    constructor(number: Number) : this(fromNumber(number))

    init {
        value.all { it in "01234567oO#8" } || throw MalformedInputException("The string is not an octal number")
        this.value =
            (if (value.startsWith("0o") || value.startsWith("0O") || value.startsWith("8#")) (-2)(value) else if (value.startsWith(
                    "0"
                )
            ) (-1)(value) else value)
                .replace("o", "")
                .replace("O", "")
                .replace("8", "")
                .replace("#", "")
    }

    /**
     * Provides companion object functionality for the Octal class. This includes
     * extension functions for creating Octal instances, serializers, deserializers,
     * and attribute converters.
     *
     * @since 1.0.0
     */
    companion object {
        /**
         * Checks if the given character sequence represents a valid octal number.
         *
         * The method determines validity by attempting to parse the input as an octal value.
         * If parsing is successful, the method returns true; otherwise, false.
         *
         * @receiver the character sequence to be checked
         * @return true if the character sequence is a valid octal number, false otherwise
         * @since 1.0.0
         */
        fun CharSequence.isValidOctal() = runCatching { Octal(toString()) }.isSuccess

        /**
         * Converts the current string to an instance of the `Octal` class if possible.
         * Returns the result as a `Result<Octal>` object, allowing safe handling of
         * any potential exceptions during the conversion process.
         *
         * @receiver The string to be converted into an `Octal` instance.
         * @return A `Result` wrapping an `Octal` instance if the string represents
         *         a valid octal value, or an exception otherwise.
         * @since 1.0.0
         */
        fun String.toOctal() = runCatching { Octal(this) }
        /**
         * Converts the receiver DECIMAL [Number] into an octal representation encapsulated in an [Octal] instance.
         *
         * The [Octal] class provides a structured way to represent and operate on octal numbers,
         * supporting conversions, arithmetic operations, and comparisons.
         *
         * @receiver The [Number] to be converted into octal form.
         * @return An instance of [Octal] representing the octal representation of the receiver.
         * @since 1.0.0
         */
        fun Number.toOctal() = Octal(this)

        /**
         * Adds an Octal value to the Byte value and returns the result as an Int.
         *
         * @param octal The Octal value to be added to the Byte.
         * @return The sum of the Byte and Octal values as an Int.
         * @since 1.0.0
         */
        operator fun Byte.plus(octal: Octal) = plus(octal.toInt())
        /**
         * Adds the value of an Octal number to this integer and returns the result.
         *
         * @param octal The Octal number to be added to this integer.
         * @since 1.0.0
         */
        operator fun Int.plus(octal: Octal) = plus(octal.toInt())
        /**
         * Adds the value of the Octal object to this Short value.
         *
         * @param octal the Octal object whose value is to be added to this Short value.
         * @since 1.0.0
         */
        operator fun Short.plus(octal: Octal) = plus(octal.toInt())
        /**
         * Adds the value of the provided [octal] to this [Long] value.
         *
         * @param octal the Octal instance whose value is to be added to this [Long].
         * @since 1.0.0
         */
        operator fun Long.plus(octal: Octal) = plus(octal.toLong())
        /**
         * Adds a given `Octal` value, after converting it to a `Float`, to this `Float` value.
         *
         * @param octal the `Octal` value to be added to this `Float`.
         * @since 1.0.0
         */
        operator fun Float.plus(octal: Octal) = plus(octal.toFloat())
        /**
         * Adds the given octal value to this Double value.
         *
         * @param octal the Octal value to be added to this Double.
         * @since 1.0.0
         */
        operator fun Double.plus(octal: Octal) = plus(octal.toDouble())

        /**
         * Subtracts the value of an `Octal` object from a `Byte` and returns the result as an `Int`.
         *
         * @param octal an `Octal` object whose integer value will be subtracted from the `Byte`.
         * @since 1.0.0
         */
        operator fun Byte.minus(octal: Octal) = minus(octal.toInt())
        /**
         * Subtracts an Octal number from an Int.
         *
         * @param octal the Octal value to be subtracted from the Int.
         * @since 1.0.0
         */
        operator fun Int.minus(octal: Octal) = minus(octal.toInt())
        /**
         * Subtracts the given octal value from this `Short` value.
         *
         * @param octal The `Octal` value to be subtracted.
         * @return The result of subtracting the octal value from this `Short` value.
         * @since 1.0.0
         */
        operator fun Short.minus(octal: Octal) = minus(octal.toInt())
        /**
         * Subtracts the value of an Octal object from a Long value.
         *
         * @param octal the Octal object to subtract, converted to a Long.
         * @since 1.0.0
         */
        operator fun Long.minus(octal: Octal) = minus(octal.toLong())
        /**
         * Subtracts the value of an Octal object, converted to a Float, from the Float on which this operator is invoked.
         *
         * @param octal The Octal object whose value is to be subtracted.
         * @return A Float result of the subtraction.
         * @since 1.0.0
         */
        operator fun Float.minus(octal: Octal) = minus(octal.toFloat())
        /**
         * Subtracts the given Octal value from the Double value.
         *
         * @param octal the Octal value to be subtracted.
         * @since 1.0.0
         */
        operator fun Double.minus(octal: Octal) = minus(octal.toDouble())

        /**
         * Multiplies a Byte value by an Octal value. The Octal is converted to an integer before multiplication.
         *
         * @param octal The Octal value to multiply with the Byte.
         * @return The result of the multiplication as an integer.
         * @since 1.0.0
         */
        operator fun Byte.times(octal: Octal) = times(octal.toInt())
        /**
         * Computes the product of this integer and the specified octal value.
         *
         * @param octal the octal value to be multiplied with this integer
         * @since 1.0.0
         */
        operator fun Int.times(octal: Octal) = times(octal.toInt())
        /**
         * Multiplies this Short value with the integer representation of the provided Octal instance.
         *
         * @param octal the Octal instance whose integer value will be multiplied with this Short.
         * @since 1.0.0
         */
        operator fun Short.times(octal: Octal) = times(octal.toInt())
        /**
         * Multiplies a [Long] value by the numeric representation of an [Octal] instance.
         *
         * @param octal the [Octal] instance whose numeric value will be multiplied with the [Long] value.
         * @since 1.0.0
         */
        operator fun Long.times(octal: Octal) = times(octal.toLong())
        /**
         * Multiplies this Float value by the value of the provided Octal object.
         *
         * @param octal the Octal object whose value is multiplied with this Float.
         * @since 1.0.0
         */
        operator fun Float.times(octal: Octal) = times(octal.toFloat())
        /**
         * Multiplies a Double value with an Octal value. The Octal value is first converted to Double
         * before performing the multiplication.
         *
         * @param octal the Octal value to be multiplied with the Double value
         * @since 1.0.0
         */
        operator fun Double.times(octal: Octal) = times(octal.toDouble())

        /**
         * Divides the Byte by the provided Octal value.
         *
         * @param octal The Octal value to divide the Byte by.
         * Converts the Octal to an Int before performing the division.
         * @since 1.0.0
         */
        operator fun Byte.div(octal: Octal) = div(octal.toInt())
        /**
         * Divides the integer by the numerical value of the given Octal instance.
         *
         * @param octal the Octal instance to divide by, converted to an integer.
         * @return the result of the division as an integer.
         * @since 1.0.0
         */
        operator fun Int.div(octal: Octal) = div(octal.toInt())
        /**
         * Divides this Short value by the given Octal value.
         *
         * @param octal the Octal value to divide this Short by
         * @return the result of dividing this Short by the Octal value as a Short
         * @since 1.0.0
         */
        operator fun Short.div(octal: Octal) = div(octal.toInt())
        /**
         * Divides a Long value by the value of the given Octal instance.
         *
         * @param octal The Octal instance whose value is used as the divisor.
         * @return The result of the division.
         * @since 1.0.0
         */
        operator fun Long.div(octal: Octal) = div(octal.toLong())
        /**
         * Divides the Float value by the given Octal value.
         *
         * @param octal the Octal value to divide the Float by
         * @since 1.0.0
         */
        operator fun Float.div(octal: Octal) = div(octal.toFloat())
        /**
         * Performs division operation between a Double and an Octal.
         *
         * @param octal the Octal operand to divide the Double by
         * @return the result of the division as a Double
         * @since 1.0.0
         */
        operator fun Double.div(octal: Octal) = div(octal.toDouble())

        /**
         * Computes the remainder of the division of this Byte by the given Octal.
         *
         * @param octal the Octal value by which this Byte is divided to compute the remainder.
         * @since 1.0.0
         */
        operator fun Byte.rem(octal: Octal) = rem(octal.toInt())
        /**
         * Performs the remainder operation between this integer and the specified Octal value.
         * The octal value is internally converted to an integer for the operation.
         *
         * @param octal the Octal value that is used as the divisor for the remainder operation.
         * @since 1.0.0
         */
        operator fun Int.rem(octal: Octal) = rem(octal.toInt())
        /**
         * Performs the remainder operation between this Short value and the provided Octal value.
         *
         * @param octal The Octal value to compute the remainder with.
         * @return The remainder of the division.
         * @since 1.0.0
         */
        operator fun Short.rem(octal: Octal) = rem(octal.toInt())
        /**
         * Performs a remainder operation between this [Long] and the specified [Octal].
         *
         * The operation calculates the remainder when dividing this [Long] by the [Octal] value.
         *
         * @param octal the [Octal] value to divide by
         * @since 1.0.0
         */
        operator fun Long.rem(octal: Octal) = rem(octal.toLong())
        /**
         * Calculates the remainder of this Float when divided by the specified Octal value.
         *
         * @param octal the Octal value to divide by.
         * @since 1.0.0
         */
        operator fun Float.rem(octal: Octal) = rem(octal.toFloat())
        /**
         * Computes the remainder of the division of this [Double] value by the given [Octal] value.
         *
         * The operation uses the `toDouble` representation of the [Octal] value to perform the calculation.
         *
         * @param octal the [Octal] value to divide this [Double] by.
         * @since 1.0.0
         */
        operator fun Double.rem(octal: Octal) = rem(octal.toDouble())

        /**
         * Converts the given number into its octal string representation.
         *
         * @param n The number to be converted to an octal string. The number can
         *          be of any type that implements the `Number` interface.
         * @return The octal string representation of the given number.
         * @since 1.0.0
         */
        private fun fromNumber(n: Number): String =
            n.toLong().toString(8)

        class Serializer : ValueSerializer<Octal>() {
            override fun serialize(value: Octal, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<Octal>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = Octal(p.string)
        }

        class OldSerializer : JsonSerializer<Octal>() {
            override fun serialize(value: Octal, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<Octal>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Octal =
                Octal(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Octal?, String?> {
            override fun convertToDatabaseColumn(attribute: Octal?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): Octal? = dbData?.let { Octal(it) }
        }
    }

    /**
     * Retrieves the element at the specified index in the collection.
     *
     * @param index The position of the element to retrieve, starting from 0.
     * @return The element located at the specified index.
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]
    /**
     * Returns a new character sequence that is a subsequence of this sequence.
     *
     * @param startIndex the start index (inclusive) of the subsequence.
     * @param endIndex the end index (exclusive) of the subsequence.
     * @return the specified subsequence as a new character sequence.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Converts the octal value to its `Double` representation.
     *
     * This method internally calls the `toLong` method to first convert the octal value
     * to its `Long` representation, and then converts the result to `Double`.
     *
     * @return The `Double` representation of the octal value.
     * @since 1.0.0
     */
    override fun toDouble() = toLong().toDouble()
    /**
     * Converts the Octal value to a Float representation.
     *
     * This method overrides the base implementation and provides a conversion of the current
     * octal value by first transforming it to its Long equivalent and then to Float.
     *
     * @return The float representation of the octal value.
     * @since 1.0.0
     */
    override fun toFloat() = toLong().toFloat()
    /**
     * Converts the octal representation of the current instance to a `Long`.
     * The conversion is based on interpreting the underlying value in base-8.
     *
     * @return The `Long` representation of this octal value.
     * @since 1.0.0
     */
    override fun toLong() = value.toLong(8)
    /**
     * Converts the octal value to an integer representation.
     * This method invokes the `toLong` function and then converts the result to an integer.
     *
     * @return The integer representation of the octal value.
     * @throws ArithmeticException if the resulting value is out of the range of `Int`.
     * @since 1.0.0
     */
    override fun toInt() = toLong().toInt()
    /**
     * Converts the current octal value to a `Short`.
     *
     * This method overrides the `toShort` implementation, leveraging the `toLong` method
     * to interpret the octal value and then converting it to a `Short`.
     *
     * @return the `Short` representation of the current octal value
     * @since 1.0.0
     */
    override fun toShort() = toLong().toShort()
    /**
     * Converts the octal representation of the current object to a Byte.
     *
     * This method internally converts the octal value to a Long and
     * subsequently to a Byte. It may result in data loss if the octal value
     * cannot be accurately represented as a Byte.
     *
     * @return The Byte representation of the current octal value.
     * @throws ArithmeticException if the resulting Byte value overflows.
     * @since 1.0.0
     */
    override fun toByte() = toLong().toByte()

    /**
     * Converts the octal value represented by this instance to an unsigned long integer.
     * The conversion is performed using base 8.
     *
     * @return The unsigned long representation of the octal value.
     * @throws NumberFormatException if the value cannot be converted to an unsigned long.
     * @since 1.0.0
     */
    fun toULong() = value.toULong(8)
    /**
     * Converts the `value` of the `Octal` instance to an [ULong] using a base 8 interpretation.
     * If the conversion is not possible (e.g., due to invalid format or overflow), returns `null`.
     *
     * @return The [ULong] representation of the value in base 8, or `null` if the conversion fails.
     * @since 1.0.0
     */
    fun toULongOrNull() = value.toULongOrNull(8)
    /**
     * Converts the stored octal value into an unsigned 32-bit integer representation.
     *
     * This method interprets the current octal value as base-8 and converts
     * it into a corresponding `UInt`. The operation assumes that the octal value
     * is encoded in the internal storage mechanism of the `Octal` class.
     *
     * @return The resulting unsigned 32-bit integer value of the octal conversion.
     * @throws NumberFormatException if the value cannot be correctly interpreted
     * as an octal number.
     * @since 1.0.0
     */
    fun toUInt() = value.toUInt(8)
    /**
     * Attempts to convert the current value, represented as a string in octal (base-8) format,
     * to an unsigned integer. Returns the resultant [UInt] if the conversion is successful,
     * or `null` if the value cannot be parsed as a valid unsigned integer in the specified base.
     *
     * @return The parsed [UInt] if the conversion is valid, or `null` if the conversion fails.
     * @since 1.0.0
     */
    fun toUIntOrNull() = value.toUIntOrNull(8)
    /**
     * Converts the octal value represented by the current object to an unsigned Short (UShort).
     *
     * The method interprets the octal [value] and converts it to its corresponding unsigned Short representation.
     * This method assumes the octal number is valid and does not perform any validation.
     *
     * @return The unsigned Short representation of the octal value.
     * @since 1.0.0
     */
    fun toUShort() = value.toUShort(8)
    /**
     * Converts the stored octal value to an unsigned 16-bit integer (UShort), or returns null if the conversion fails.
     * The conversion is performed with base 8.
     *
     * @return The converted UShort value, or null if the value cannot be converted.
     * @since 1.0.0
     */
    fun toUShortOrNull() = value.toUShortOrNull(8)
    /**
     * Converts the value of the Octal object to an unsigned 8-bit byte (UByte).
     *
     * The conversion interprets the value in an 8-bit unsigned context, providing
     * a representation suitable for cases where only unsigned byte values are required.
     *
     * @return The unsigned 8-bit byte equivalent of the value.
     * @since 1.0.0
     */
    fun toUByte() = value.toUByte(8)
    /**
     * Converts the octal string value of this instance to an unsigned byte (UByte), or returns null if
     * the conversion is not possible (e.g., the value is not in the valid range for a UByte or the format is invalid).
     *
     * The conversion assumes the value is represented in base 8 (octal).
     *
     * @return The UByte representation of the value, or null if the conversion fails.
     * @since 1.0.0
     */
    fun toUByteOrNull() = value.toUByteOrNull(8)

    /**
     * Adds the specified number to the current Octal instance and returns a new Octal object representing the result.
     *
     * @param other the number to be added to the current Octal value.
     * @return a new Octal instance that represents the sum of the current Octal value and the specified number.
     * @since 1.0.0
     */
    operator fun plus(other: Number) = Octal(toLong() + other.toLong())
    /**
     * Subtracts the specified number from this Octal value.
     *
     * Ensures the result of the subtraction is non-negative.
     *
     * @param other the number to be subtracted from this Octal value
     * @return a new Octal instance representing the result of the subtraction
     * @throws ArithmeticException if the result of the subtraction is negative
     * @since 1.0.0
     */
    operator fun minus(other: Number): Octal {
        if ((toLong() - other.toLong()) < 0) throw ArithmeticException("The result of the subtraction must be >= 0")
        return Octal(toLong() - other.toLong())
    }
    /**
     * Multiplies the current octal value by the specified number.
     *
     * @param other The number to multiply with the current octal value.
     * @return A new Octal instance resulting from the multiplication.
     * @since 1.0.0
     */
    operator fun times(other: Number) = Octal(toLong() * other.toLong())
    /**
     * Divides the current Octal instance by the specified number and returns a new Octal instance.
     *
     * @param other The number to divide the current Octal value by.
     * @return A new Octal instance resulting from the division.
     * @since 1.0.0
     */
    operator fun div(other: Number) = Octal(toLong() / other.toLong())
    /**
     * Performs a remainder operation between this Octal number and the given numeric value.
     * The result is a new Octal object representing the remainder when this Octal is divided by the given number.
     *
     * @param other The number to divide this Octal by.
     * @return A new Octal instance representing the remainder of the division.
     * @since 1.0.0
     */
    operator fun rem(other: Number) = Octal(toLong() % other.toLong())
    /**
     * Increments the value of the current `Octal` instance by 1.
     *
     * This method creates a new `Octal` instance with its value increased,
     * leaving the original instance unmodified, as `Octal` is immutable.
     *
     * @return A new `Octal` instance representing the incremented value.
     * @since 1.0.0
     */
    operator fun inc() = Octal(toLong() + 1)
    /**
     * Decrements the current instance of Octal by 1, ensuring that the result is not less than 0.
     * If the result would be negative, an ArithmeticException is thrown.
     *
     * @return A new Octal instance after decrementing the current value.
     * @throws ArithmeticException if the result of the decrement is less than 0.
     * @since 1.0.0
     */
    operator fun dec(): Octal {
        if ((toLong() - 1) < 0) throw ArithmeticException("The result of the decrement must be >= 0")
        return Octal(toLong() - 1)
    }

    /**
     * Compares this Number object with the specified Number object for order.
     * Returns a negative integer, zero, or a positive integer if this object
     * is less than, equal to, or greater than the specified object.
     *
     * @param other the Number to be compared to this instance
     * @return an integer representing the result of the comparison
     * @since 1.0.0
     */
    override fun compareTo(other: Number) = toLong().compareTo(other.toLong())

    /**
     * Converts the current object to its string representation in octal format.
     *
     * @param prefix Determines whether the returned string should include the "0o" prefix.
     *               If true, the string will have the "0o" prefix; otherwise, it will not.
     * @return The string representation of the object in octal format, optionally prefixed with "0o".
     * @since 1.0.0
     */
    fun toString(prefix: Boolean): String = "${if (prefix) "0o" else String.EMPTY}$value"
    /**
     * Returns a string representation of the object.
     *
     * @return The string representation of the object.
     * @since 1.0.0
     */
    override fun toString(): String = toString(true)
}
