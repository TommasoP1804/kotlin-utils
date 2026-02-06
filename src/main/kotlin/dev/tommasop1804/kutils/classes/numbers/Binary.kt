package dev.tommasop1804.kutils.classes.numbers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.EMPTY
import dev.tommasop1804.kutils.div
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
import kotlin.math.pow

/**
 * Represents a binary data structure that supports various numerical and arithmetic operations.
 *
 * @property value The binary representation of the number as a string.
 * @property length The length of the binary representation.
 * @since 1.0.0
 */
@Suppress("unused")
@JsonSerialize(using = Binary.Companion.Serializer::class)
@JsonDeserialize(using = Binary.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Binary.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Binary.Companion.OldDeserializer::class)
class Binary(value: String) : CharSequence, Number(), Comparable<Number> {
    /**
     * A string value that can be assigned or used within the application scope.
     *
     * This variable may represent text-based data that is crucial for specific
     * operations or configurations.
     *
     * @since 1.0.0
     */
    private val value: String

    /**
     * The length of the string represented by the `value` property.
     * This override provides the length of the current string.
     *
     * @return The number of characters in the string.
     * @since 1.0.0
     */
    override val length: Int
        get() = value.length

    /**
     * Secondary constructor for creating an instance using a Number and a specified precision.
     * Converts the given Number to the appropriate format using the `fromNumber` function.
     *
     * @param number the numeric value to be used for initialization
     * @param precision the precision level for conversion, default value is 10
     * @since 1.0.0
     */
    constructor(number: Number, precision: Int = 10) : this(fromNumber(number, precision))

    init {
        value.all { it in "bB01 .2#" } || throw MalformedInputException("The string is not a binary number")
        this.value =
            (if (value.startsWith("0b") || value.startsWith("0B") || value.startsWith("2#")) (-2)(value) else value)
                .replace("b", "")
                .replace("B", "")
                .replace("2", "")
                .replace("#", "")
    }

    companion object {
        /**
         * Checks if the given CharSequence represents a valid binary value.
         *
         * This method attempts to parse the CharSequence as a binary value and returns
         * `true` if the parsing succeeds, otherwise `false`.
         *
         * @receiver The CharSequence to check for binary validity.
         * @return `true` if the CharSequence represents a valid binary value, otherwise `false`.
         * @since 1.0.0
         */
        fun CharSequence.isValidBinary() = runCatching { Binary(toString()) }.isSuccess

        /**
         * Converts the given string to its binary representation encapsulated
         * within a `Binary` object. The conversion is performed safely, and
         * any errors during the process will result in a failed `Result`.
         *
         * @receiver The string to be converted to a binary representation.
         * @return A `Result` containing the `Binary` object if the conversion
         *         succeeds, or failure if an exception occurs.
         * @since 1.0.0
         */
        fun String.toBinary() = runCatching { Binary(this) }
        /**
         * Converts the current numeric value into its binary representation.
         *
         * This extension function constructs a `Binary` representation for the invoking `Number` instance.
         * It is typically used when a binary format of the number is required for further operations or display purposes.
         *
         * @receiver Number instance to be converted to binary representation.
         * @return A `Binary` instance representing the binary equivalent of the invoking number.
         * @since 1.0.0
         */
        fun Number.toBinary() = Binary(this)

        /**
         * Adds the value of the specified binary object to this Byte and returns the result.
         *
         * @param binary the Binary object whose value will be added to this Byte.
         * @since 1.0.0
         */
        operator fun Byte.plus(binary: Binary) = plus(binary.toInt())
        /**
         * Adds an integer to a binary number, where the binary number is first
         * converted to its integer representation.
         *
         * @param binary the binary number to be added to the integer
         * @since 1.0.0
         */
        operator fun Int.plus(binary: Binary) = plus(binary.toInt())
        /**
         * Performs addition between a `Short` value and a `Binary` object.
         * The `Binary` object is converted to its integer representation
         * before performing the addition.
         *
         * @param binary The `Binary` object to be added to the `Short` value.
         * @since 1.0.0
         */
        operator fun Short.plus(binary: Binary) = plus(binary.toInt())
        /**
         * Adds the specified Binary value to the Long value and returns the result.
         *
         * @param binary The Binary value to be added to the Long value.
         * @since 1.0.0
         */
        operator fun Long.plus(binary: Binary) = plus(binary.toLong())
        /**
         * Adds a `Binary` value to a `Float` and returns the result as a `Float`.
         *
         * @param binary The `Binary` value to be added to this `Float`.
         * @since 1.0.0
         */
        operator fun Float.plus(binary: Binary) = plus(binary.toFloat())
        /**
         * Adds the value of a `Binary` object to this `Double` and returns the result as a `Double`.
         *
         * @param binary The `Binary` object whose value will be added to this `Double`.
         * @since 1.0.0
         */
        operator fun Double.plus(binary: Binary) = plus(binary.toDouble())

        /**
         * Subtracts the value of the specified Binary object from this Byte.
         *
         * @param binary the Binary object whose value is to be subtracted from this Byte
         * @since 1.0.0
         */
        operator fun Byte.minus(binary: Binary) = minus(binary.toInt())
        /**
         * Subtracts the value of a Binary object represented as an integer from this integer.
         *
         * @param binary the Binary object whose value will be subtracted from this integer
         * @since 1.0.0
         */
        operator fun Int.minus(binary: Binary) = minus(binary.toInt())
        /**
         * Subtracts the given [Binary] value from the current [Short] value.
         *
         * @param binary the [Binary] value to subtract from the current [Short].
         * @since 1.0.0
         */
        operator fun Short.minus(binary: Binary) = minus(binary.toInt())
        /**
         * Subtracts the numeric value of the specified `Binary` object from this `Long`.
         *
         * @param binary The `Binary` object whose numeric value will be subtracted from this `Long`.
         * @since 1.0.0
         */
        operator fun Long.minus(binary: Binary) = minus(binary.toLong())
        /**
         * Subtracts the value represented by the given `Binary` instance from this `Float` value.
         *
         * @param binary The `Binary` instance to subtract, converted to a `Float`.
         * @since 1.0.0
         */
        operator fun Float.minus(binary: Binary) = minus(binary.toFloat())
        /**
         * Performs a subtraction operation between a Double and a Binary, converting the Binary to a Double.
         *
         * @param binary the Binary value to be subtracted from the Double.
         * @since 1.0.0
         */
        operator fun Double.minus(binary: Binary) = minus(binary.toDouble())

        /**
         * Multiplies a Byte with a Binary object by converting the Binary to an integer
         * representation and performing the multiplication.
         *
         * @param binary the Binary object to be multiplied with the Byte
         * @since 1.0.0
         */
        operator fun Byte.times(binary: Binary) = times(binary.toInt())
        /**
         * Multiplies an integer with the integer value of a Binary instance.
         *
         * @param binary an instance of Binary whose integer value will be multiplied with the Int instance.
         * @return the result of multiplying the integer with the binary's integer value.
         * @since 1.0.0
         */
        operator fun Int.times(binary: Binary) = times(binary.toInt())
        /**
         * Multiplies this `Short` value by the numerical representation of the given `Binary` object.
         *
         * @param binary the `Binary` object whose integer representation will be multiplied with this `Short` value.
         * @since 1.0.0
         */
        operator fun Short.times(binary: Binary) = times(binary.toInt())
        /**
         * Multiplies a [Long] value by a [Binary] value.
         *
         * @param binary The binary value to perform the multiplication with.
         * @since 1.0.0
         */
        operator fun Long.times(binary: Binary) = times(binary.toLong())
        /**
         * Multiplies a floating-point number by the numeric value of a binary input.
         *
         * @param binary The binary number to be multiplied, which will be converted to a Float.
         * @since 1.0.0
         */
        operator fun Float.times(binary: Binary) = times(binary.toFloat())
        /**
         * Multiplies a Double value with a Binary value by converting the Binary to a Double.
         *
         * @param binary The Binary value to multiply this Double with.
         * @since 1.0.0
         */
        operator fun Double.times(binary: Binary) = times(binary.toDouble())

        /**
         * Divides the current Byte value by a given Binary value.
         *
         * @param binary The Binary number that will be used as the divisor.
         * @since 1.0.0
         */
        operator fun Byte.div(binary: Binary) = div(binary.toInt())
        /**
         * Divides the integer by the value represented by the given Binary object.
         *
         * @param binary The Binary object whose integer value will be used as the divisor.
         * @return The result of the division as an integer.
         * @since 1.0.0
         */
        operator fun Int.div(binary: Binary) = div(binary.toInt())
        /**
         * Performs division between a `Short` value and a `Binary` value.
         *
         * @param binary the `Binary` value to divide the `Short` value by.
         * @return the result of the division as an `Int`.
         * @since 1.0.0
         */
        operator fun Short.div(binary: Binary) = div(binary.toInt())
        /**
         * Divides the current Long value by the specified Binary value.
         *
         * @param binary The Binary value to divide the current Long value by.
         * @since 1.0.0
         */
        operator fun Long.div(binary: Binary) = div(binary.toLong())
        /**
         * Divides a Float by the value of the given Binary instance, converting the Binary to a Float
         * before performing the division operation.
         *
         * @param binary the Binary instance to divide the Float by
         * @since 1.0.0
         */
        operator fun Float.div(binary: Binary) = div(binary.toFloat())
        /**
         * Divides a Double by the value of a Binary object converted to Double.
         *
         * @param binary The Binary object whose value will be used for division.
         * @return The result of dividing the Double value by the Binary value as a Double.
         * @since 1.0.0
         */
        operator fun Double.div(binary: Binary) = div(binary.toDouble())

        /**
         * Performs the remainder operation between this Byte and the given Binary object,
         * which is converted to an integer.
         *
         * @param binary the Binary object to perform the remainder operation with.
         * @since 1.0.0
         */
        operator fun Byte.rem(binary: Binary) = rem(binary.toInt())
        /**
         * Computes the remainder of the division between this integer and a given binary value.
         *
         * @param binary the binary value to divide this integer by, converted to an integer.
         * @since 1.0.0
         */
        operator fun Int.rem(binary: Binary) = rem(binary.toInt())
        /**
         * Computes the remainder of the division of this [Short] value by the value represented by the given [Binary] instance.
         *
         * @param binary The [Binary] instance whose integer representation is used as the divisor.
         * @since 1.0.0
         */
        operator fun Short.rem(binary: Binary) = rem(binary.toInt())
        /**
         * Computes the remainder of the division of this [Long] value by the given [Binary] value.
         *
         * This operator function allows the use of the remainder (%) operator directly between
         * a [Long] object and a [Binary] object by converting the [Binary] value to its [Long] representation.
         *
         * @param binary the [Binary] object to divide this [Long] value by.
         * @since 1.0.0
         */
        operator fun Long.rem(binary: Binary) = rem(binary.toLong())
        /**
         * Computes the remainder of the division of this Float value by the given Binary value.
         *
         * @param binary the Binary operand to compute the remainder with.
         * @since 1.0.0
         */
        operator fun Float.rem(binary: Binary) = rem(binary.toFloat())
        /**
         * Calculates the remainder of the division of this Double by the value of the provided Binary.
         *
         * @param binary the Binary value used as the divisor in the operation
         * @since 1.0.0
         */
        operator fun Double.rem(binary: Binary) = rem(binary.toDouble())

        /**
         * Converts a given number to its binary representation as a string. The binary representation
         * includes both the integer and fractional parts, separated by a dot (e.g., "101.01").
         *
         * @param n The number to convert to a binary string.
         * @param precision The maximum number of digits to include in the fractional part of the binary representation.
         *                   Defaults to 10 if not specified.
         * @return A string representing the binary equivalent of the input number, including its fractional part if applicable.
         * @since 1.0.0
         */
        private fun fromNumber(n: Number, precision: Int = 10): String {
            val integerPart = n.toLong()
            val fractionalPart = n.toDouble() - integerPart

            val integerBinary = integerPart.toString(2)

            if (fractionalPart == 0.0)
                return integerBinary

            val fractionalBinary = StringBuilder()
            var fraction = fractionalPart
            var count = 0

            while (fraction > 0.0 && count < precision) {
                fraction *= 2
                if (fraction >= 1.0) {
                    fractionalBinary.append('1')
                    fraction -= 1L
                } else {
                    fractionalBinary.append('0')
                }
                count++
            }

            return "$integerBinary.${fractionalBinary}"
        }

        class Serializer : ValueSerializer<Binary>() {
            override fun serialize(value: Binary, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<Binary>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = Binary(p.string)
        }

        class OldSerializer : JsonSerializer<Binary>() {
            override fun serialize(value: Binary, gen: JsonGenerator, serializers: SerializerProvider) = gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<Binary>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Binary = Binary(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Binary?, String?> {
            override fun convertToDatabaseColumn(attribute: Binary?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): Binary? = dbData?.let { Binary(it) }
        }
    }
    
    /**
     * Retrieves the element at the specified index from the value property.
     *
     * @param index the position of the element to retrieve from the value property.
     * @return the element at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]
    
    /**
     * Returns a new character sequence that is a subsequence of the original character sequence.
     *
     * @param startIndex the start index (inclusive) of the subsequence
     * @param endIndex the end index (exclusive) of the subsequence
     * @return a character sequence corresponding to the specified subsequence
     * @throws IndexOutOfBoundsException if the startIndex or endIndex is out of bounds,
     * or if startIndex > endIndex
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex) 

    /**
     * Converts a binary numeric representation into its equivalent double-precision floating-point value.
     *
     * This method interprets the input value as a binary number with both integer and fractional parts.
     *
     * @return The double value corresponding to the binary representation.
     * @since 1.0.0
     */
    override fun toDouble(): Double {
        val parts = value / '.'
        val intPart = parts[0]
        val fractionalPart = if (parts.size > 1) parts[1] else String.EMPTY

        var result = 0.0
        if (!intPart.isEmpty()) {
            result = intPart.toInt(2).toDouble()
        }

        for (i in fractionalPart.indices) {
            if (fractionalPart[i] == '1') {
                result += 2.0.pow(-(i + 1).toDouble())
            }
        }
        return result

    }

    /**
     * Converts the value to a [Float] representation.
     *
     * This method overrides the base implementation to first convert the
     * value to a [Double], and then casts it to a [Float]. It ensures
     * compatibility or narrowing down a wider numeric type to a floating-point
     * type with single precision.
     *
     * @return the value as a [Float].
     * @since 1.0.0
     */
    override fun toFloat() = toDouble().toFloat()

    /**
     * Converts the binary value represented in the current instance to its [Long] representation.
     * The conversion is performed considering the value as a binary number.
     *
     * @return the [Long] representation of the binary value.
     * @since 1.0.0
     */
    override fun toLong() = value.toLong(radix = 2)

    /**
     * Converts the value contained in the instance to an `Int`.
     * The conversion is performed by first converting the value to a `Long`
     * using the `toLong` method and then narrowing it to an `Int`.
     *
     * @return An `Int` representing the numeric value of the instance.
     * @throws ArithmeticException if the resulting integer representation
     * cannot be represented as an `Int`.
     * @since 1.0.0
     */
    override fun toInt() = toLong().toInt()

    /**
     * Converts the binary value to a [Short] representation.
     *
     * This method first converts the binary value to a [Long] using the `toLong` method,
     * then casts the result to a [Short].
     *
     * @return The [Short] representation of the binary value.
     * @since 1.0.0
     */
    override fun toShort() = toLong().toShort()

    /**
     * Converts the binary value to its Byte representation.
     *
     * This method overrides the `toByte` function and utilizes the `toLong` method
     * of the class to first convert the binary value to a Long. It then maps the
     * Long value to a Byte representation. The conversion may cause truncation if
     * the value exceeds the Byte range.
     *
     * @return The Byte representation of the binary value.
     * @throws NumberFormatException If the binary value cannot be parsed to a Long.
     * @since 1.0.0
     */
    override fun toByte() = toLong().toByte()

    /**
     * Converts the current binary value to an unsigned long (ULong).
     *
     * This method interprets the binary value of the current instance
     * and transforms it into an unsigned long representation.
     *
     * @return The unsigned long representation of the binary value.
     * @throws NumberFormatException if the binary value cannot be represented as ULong.
     * @since 1.0.0
     */
    fun toULong() = value.toULong()

    /**
     * Converts the value of this Binary instance to a [ULong] or returns `null` if the conversion is not possible.
     *
     * @return the [ULong] representation of the value, or `null` if the value cannot be converted.
     * @since 1.0.0
     */
    fun toULongOrNull() = value.toULongOrNull()

    /**
     * Converts the value of the Binary instance to an unsigned 32-bit integer.
     *
     * This method interprets the `value` property of the Binary instance as a string
     * representation of a hexadecimal number and converts it to a `UInt`.
     *
     * @return The unsigned 32-bit integer representation of the Binary's value.
     * @throws NumberFormatException if the value cannot be parsed as a valid hexadecimal number.
     * @since 1.0.0
     */
    fun toUInt() = value.toUInt(radix = 16)

    /**
     * Converts the underlying `value` of the object to an unsigned integer (`UInt`)
     * using base 16 (hexadecimal) and returns the result.
     * If the conversion fails, for example, if the value cannot be parsed as a hexadecimal number,
     * the method returns `null`.
     *
     * @return the unsigned integer value of `value` if the conversion succeeds, or `null` if it fails.
     * @since 1.0.0
     */
    fun toUIntOrNull() = value.toUIntOrNull(radix = 16)

    /**
     * Converts the `value` of the current `Binary` instance to an unsigned short (UShort).
     * The conversion assumes the `value` is represented in a radix of 16 (hexadecimal).
     *
     * @return the unsigned short (UShort) representation of the `value`.
     *
     * @since 1.0.0
     */
    fun toUShort() = value.toUShort(radix = 16)

    /**
     * Converts the `value` of this Binary instance to a `UShort` using base 16 (hexadecimal) representation.
     * Returns `null` if the conversion is not possible.
     *
     * This method interprets the string representation of the `value` as a number in base 16
     * and attempts to convert it to an unsigned short integer.
     *
     * @return The converted `UShort` value, or `null` if the conversion fails.
     * @since 1.0.0
     */
    fun toUShortOrNull() = value.toUShortOrNull(radix = 16)

    /**
     * Converts the binary value to an unsigned byte (UByte) using a hexadecimal radix.
     *
     * This function interprets the binary value as a representation in base 16 (hexadecimal)
     * and converts it into an unsigned byte data type. It is useful for working with binary
     * data that should be represented in unsigned form within the range of UByte (0 to 255).
     *
     * @return The unsigned byte (UByte) equivalent of the binary value.
     * @throws NumberFormatException If the binary value is not a valid representation in the specified radix.
     * @since 1.0.0
     */
    fun toUByte() = value.toUByte(radix = 16)

    /**
     * Attempts to convert the `value` property of the `Binary` class to an unsigned byte (`UByte`)
     * using a hexadecimal numeral system (radix 16).
     * If the conversion is successful, it returns the corresponding `UByte` value.
     * If the conversion fails, such as if the `value` string is not a valid hexadecimal representation
     * or exceeds the maximum value representable by an unsigned byte, it returns `null`.
     *
     * @return The `UByte` representation of `value` if the conversion is successful, or `null` otherwise.
     * @since 1.0.0
     */
    fun toUByteOrNull() = value.toUByteOrNull(radix = 16)

    /**
     * Adds the value of another `Binary` instance to this instance and returns the result as a new `Binary` instance.
     *
     * @param other the `Binary` instance to be added to this instance.
     * @return a new `Binary` instance representing the sum of this instance and the given `Binary` instance.
     * @since 1.0.0
     */
    operator fun plus(other: Number) = Binary(toLong() + other.toLong())

    /**
     * Subtracts the given `Binary` object from this `Binary` object.
     * Throws an `ArithmeticException` if the result of the subtraction is negative.
     *
     * @param other the `Binary` object to subtract from this `Binary` object.
     * @return a new `Binary` object representing the result of the subtraction.
     * @throws ArithmeticException if the result of the subtraction is negative.
     * @since 1.0.0
     */
    operator fun minus(other: Number): Binary {
        if ((toLong() - other.toLong()) < 0) throw ArithmeticException("The result of the subtraction will be postive or zero")
        return Binary(toLong() - other.toLong())
    }

    /**
     * Multiplies the current Binary object by another Binary object, resulting in a new Binary object.
     *
     * @param other The Binary object to multiply with the current Binary object.
     * @return A new Binary object representing the product of the two Binary values.
     * @since 1.0.0
     */
    operator fun times(other: Number) = Binary(toLong() * other.toLong())

    /**
     * Performs division of the current `Binary` object by another `Binary` object.
     *
     * The division operation is performed using the long representation of the binary values.
     *
     * @param other The `Binary` object by which the current object should be divided.
     * @since 1.0.0
     */
    operator fun div(other: Number) = Binary(toLong() / other.toLong())

    /**
     * Computes the remainder of the division between this `Binary` instance and another `Binary` instance.
     *
     * This function uses the modulus operator to calculate the remainder of the division
     * of the numeric values represented by the two `Binary` objects.
     *
     * @param other The `Binary` instance to divide this instance by.
     * @return A new `Binary` instance representing the remainder of the division.
     * @since 1.0.0
     */
    operator fun rem(other: Number) = Binary(toLong() % other.toLong())

    /**
     * Increments the current binary value by one.
     *
     * This operator function is used to perform a unary increment operation on a `Binary` object.
     * It returns a new `Binary` instance with its value increased by one.
     *
     * @return A new `Binary` instance representing the result of the increment operation.
     * @since 1.0.0
     */
    operator fun inc() = Binary(toLong() + 1)

    /**
     * Decrements the current binary value by one.
     * If the result of the decrement is zero or positive, an `ArithmeticException` will be thrown.
     *
     * @return A new instance of `Binary` representing the decremented value.
     * @throws ArithmeticException If the result of the decrement is zero or positive.
     * @since 1.0.0
     */
    operator fun dec(): Binary {
        if ((toLong() - 1) == 0L) throw ArithmeticException("The result of the decrement will be positive or zero")
        return Binary(toLong() - 1)
    }

    /**
     * Compares this Binary object with the specified other Binary object for order.
     * Returns a negative integer, zero, or a positive integer as this object is less than,
     * equal to, or greater than the specified object.
     *
     * This comparison is based on the numeric representation of the Binary object when
     * converted to a Long value.
     *
     * @param other the Binary object to be compared.
     * @since 1.0.0
     */
    override fun compareTo(other: Number) = toLong().compareTo(other.toLong())

    /**
     * Converts the current value into its string representation.
     *
     * @param prefix Determines if the result string should be prefixed with "0b" to indicate binary format.
     * @return A string representation of the current value, optionally prefixed with "0b" if `prefix` is true.
     * @since 1.0.0
     */
    fun toString(prefix: Boolean): String = "${if (prefix) "0b" else String.EMPTY}$value"

    /**
     * Returns a string representation of the object.
     *
     * @return the string value representing the object
     * @since 1.0.0
     */
    override fun toString(): String = toString(true)
}