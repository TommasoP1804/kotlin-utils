package dev.tommasop1804.kutils.classes.base

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.classes.constants.TextCase
import dev.tommasop1804.kutils.classes.constants.TextCase.Companion.convertCase
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.exceptions.NumberSignException
import dev.tommasop1804.kutils.isAlphanumeric
import dev.tommasop1804.kutils.isNegative
import dev.tommasop1804.kutils.isOdd
import dev.tommasop1804.kutils.unaryMinus
import dev.tommasop1804.kutils.unaryPlus
import jakarta.persistence.AttributeConverter
import org.bouncycastle.util.Strings
import org.bouncycastle.util.encoders.Hex
import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import kotlin.collections.map

/**
 * A class that represents a Base36-encoded value. The Base36 encoding consists of alphanumeric
 * characters (0-9, A-Z) and can be used to encode numerical values in a compact and readable format.
 * This class supports conversion to and from numbers, manipulation of Base36 values, and utility
 * operations for working with Base36 strings.
 *
 * @constructor Creates a Base36 object from a Base36-encoded string.
 * @param value The Base36-encoded string.
 * @author Tommaso Pastorelli
 *
 * @since 1.0.0
 */
@JsonSerialize(using = Base36.Companion.Serializer::class)
@JsonDeserialize(using = Base36.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Base36.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Base36.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_drop_as_int_invoke")
class Base36(private val value: String) : Number(), CharSequence, Comparable<Number> {
    /**
     * Represents the length of the `value` field in the `Base36` class.
     * 
     * The length is calculated based on the current state of the underlying `value`.
     * This property provides the size of the `value` as an integer.
     *
     * @since 1.0.0
     */
    override val length = value.length

    /**
     * Constructs a new [Base36] instance from the given [Number].
     *
     * Converts the provided numeric value to its Base36 string representation
     * and initializes the object with the resulting string. The input number 
     * must be non-negative; otherwise, a `NumberSignException` is thrown.
     *
     * @param number The numeric value to convert to Base36.
     * 
     * @throws NumberSignException If the provided number is negative.
     *
     * @since 1.0.0
     */
    constructor(number: Number) : this(+number.toLong().toString(36)) {
        number.toLong() >= 0 || throw NumberSignException("The number must be greater than zero")
    }

    /**
     * Constructs a Base36 instance from the given byte array.
     * The entire array is used to initialize the value.
     *
     * @param data The byte array to be interpreted as a Base36 value.
     * @since 1.0.0
     */
    constructor(data: ByteArray) : this(data, 0, data.size)

    /**
     * Constructs a new instance of the `Base36` class using the specified byte array, offset, 
     * and length. The byte array is encoded as a hexadecimal string starting from the given 
     * offset and spanning the specified length.
     *
     * @param data The byte array to be encoded. Can be null.
     * @param off The starting offset within the byte array.
     * @param length The number of bytes to encode starting from the offset.
     * @throws IllegalArgumentException If the provided arguments result in an invalid encoding.
     * @since 1.0.0
     */
    constructor(data: ByteArray?, off: Int, length: Int) : this(
        Strings.fromByteArray(Hex.encode(data, off, length)!!)
    )

    init {
        value.isAlphanumeric || throw MalformedInputException("The string is not a valid Base36 string")
    }

    companion object {
        /**
         * Checks if the current character sequence is a valid Base36 encoded value.
         *
         * This function attempts to create a `Base36` object using the current character sequence. 
         * If the object can be constructed successfully, it indicates that the character sequence 
         * is a valid Base36 representation. Otherwise, it is considered invalid.
         *
         * @receiver The character sequence to validate.
         * @return `true` if the character sequence is a valid Base36 representation, `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidBase36() = runCatching { Base36(toString()) }.isSuccess

        /**
         * Converts the current `Number` instance to its Base36 representation.
         *
         * The method creates a `Base36` instance based on the numeric value of the receiver. 
         * Base36 encoding is commonly used to represent numbers compactly using digits (0-9) 
         * and letters (A-Z or a-z) as symbols.
         *
         * @receiver The number to be converted to Base36. It must be a non-negative value.
         * @return A `Base36` instance representing the Base36-encoded form of the number.
         * @since 1.0.0
         */
        fun Number.toBase36() = Base36(this)
        /**
         * Converts the current [CharSequence] to an instance of `Base36`.
         *
         * This method attempts to represent the given character sequence as a `Base36` object,
         * which encapsulates a value encoded in base-36 numeral system.
         *
         * The operation is performed within a `runCatching` block, allowing for safe execution.
         * Any exception encountered during the conversion will be encapsulated in a [Result] object.
         *
         * @receiver The [CharSequence] to be converted to `Base36`.
         * @return A [Result] containing the converted `Base36` instance if the operation is successful,
         * or an error if a failure occurs.
         * @since 1.0.0
         */
        fun CharSequence.toBase36() = runCatching { Base36(toString()) }

        /**
         * Adds a Base36 number to a Byte and returns the result as an Int.
         *
         * @param base36 the Base36 number to be added to this Byte. The Base36 number is converted to an Int during the operation.
         * @since 1.0.0
         */
        operator fun Byte.plus(base36: Base36) = plus(base36.toInt())
        /**
         * Adds an integer to a Base36 number, treating the Base36 number as its integer equivalent.
         *
         * @param base36 The Base36 number to be added to the integer.
         * @since 1.0.0
         */
        operator fun Int.plus(base36: Base36) = plus(base36.toInt())
        /**
         * Adds the given Base36 number, converted to an integer, to the Short receiver.
         *
         * @param base36 the Base36 number to be added to the Short receiver.
         * @since 1.0.0
         */
        operator fun Short.plus(base36: Base36) = plus(base36.toInt())
        /**
         * Adds the numeric value of a Base36 object to a Long value.
         *
         * The operation converts the Base36 object to its Long representation and 
         * performs addition with the given Long operand.
         *
         * @param base36 the Base36 object whose value will be added to the Long.
         * @since 1.0.0
         */
        operator fun Long.plus(base36: Base36) = plus(base36.toLong())
        /**
         * Adds a Base36 number, converted to a Float, to this Float value.
         *
         * @param base36 the Base36 number to be added, which will be converted to its Float representation.
         * @since 1.0.0
         */
        operator fun Float.plus(base36: Base36) = plus(base36.toFloat())
        /**
         * Adds a `Base36` value to a `Double` value by converting the `Base36` value to its `Double` representation.
         *
         * @param base36 a `Base36` instance that will be converted to `Double` and added to the current `Double` value.
         * @since 1.0.0
         */
        operator fun Double.plus(base36: Base36) = plus(base36.toDouble())

        /**
         * Subtracts the integer representation of the given Base36 value from this Byte value.
         *
         * @param base36 A Base36 value to be converted to an integer and subtracted from this Byte.
         * @since 1.0.0
         */
        operator fun Byte.minus(base36: Base36) = minus(base36.toInt())
        /**
         * Subtracts the integer representation of the given Base36 object from the current integer.
         *
         * @param base36 The Base36 object whose value will be converted to an integer and subtracted.
         * @since 1.0.0
         */
        operator fun Int.minus(base36: Base36) = minus(base36.toInt())
        /**
         * Subtracts the integer value of the specified [Base36] from this [Short].
         *
         * @param base36 the [Base36] instance whose integer value will be subtracted from this [Short]
         * @since 1.0.0
         */
        operator fun Short.minus(base36: Base36) = minus(base36.toInt())
        /**
         * Subtracts the numeric value of the provided Base36 object from the Long value.
         *
         * @param base36 the Base36 object whose numeric value will be subtracted.
         * @return the result of the subtraction as a Long.
         * @since 1.0.0
         */
        operator fun Long.minus(base36: Base36) = minus(base36.toLong())
        /**
         * Subtracts the value of the given `Base36` object, converted to a `Float`, from the `Float` operand.
         *
         * @param base36 The `Base36` operand whose value will be converted to `Float` and subtracted.
         * @since 1.0.0
         */
        operator fun Float.minus(base36: Base36) = minus(base36.toFloat())
        /**
         * Subtracts a Base36 number from a Double, returning the result as a Double.
         *
         * @param base36 the Base36 number to subtract from this Double
         * @since 1.0.0
         */
        operator fun Double.minus(base36: Base36) = minus(base36.toDouble())

        /**
         * Multiplies the given Byte with the numeric value of the specified Base36 object.
         *
         * @param base36 The Base36 object whose value is used in the multiplication.
         * @since 1.0.0
         */
        operator fun Byte.times(base36: Base36) = times(base36.toInt())
        /**
         * Multiplies an integer by a Base36 instance's numeric value.
         *
         * @param base36 A Base36 object whose numeric value (converted to an integer) will be multiplied
         *            by the integer on which this operator is invoked.
         *
         * @since 1.0.0
         */
        operator fun Int.times(base36: Base36) = times(base36.toInt())
        /**
         * Multiplies a [Short] value by a [Base36] value, where the [Base36] value
         * is converted to its integer representation in base-36.
         *
         * @param base36 the [Base36] instance to multiply with the [Short] value.
         * @since 1.0.0
         */
        operator fun Short.times(base36: Base36) = times(base36.toInt())
        /**
         * Multiplies a Long value by a Base36 number.
         *
         * The Base36 instance is converted to a Long representation and then multiplied by the Long value.
         *
         * @param base36 The Base36 instance to be multiplied with the Long value.
         * @since 1.0.0
         */
        operator fun Long.times(base36: Base36) = times(base36.toLong())
        /**
         * Multiplies a Float with a Base36 value by converting the Base36 value to a Float.
         *
         * @param base36 The Base36 value to be converted to a float before multiplication.
         * @since 1.0.0
         */
        operator fun Float.times(base36: Base36) = times(base36.toFloat())
        /**
         * Multiplies a Double value by the numeric representation of a Base36 value.
         *
         * @param base36 the Base36 instance whose numeric value will be multiplied with the Double value.
         * @since 1.0.0
         */
        operator fun Double.times(base36: Base36) = times(base36.toDouble())

        /**
         * Performs division between a `Byte` and a `Base36` instance, where the `Base36` instance is 
         * converted to an integer before the operation.
         *
         * @param base36 The `Base36` instance used as the divisor. It is converted to an integer in base 36.
         * @since 1.0.0
         */
        operator fun Byte.div(base36: Base36) = div(base36.toInt())
        /**
         * Divides an integer by a Base36 value, returning the result of the division.
         *
         * @param base36 the Base36 value to divide the integer by.
         * @since 1.0.0
         */
        operator fun Int.div(base36: Base36) = div(base36.toInt())
        /**
         * Divides this [Short] value by the [Base36] value.
         *
         * @param base36 the [Base36] value used as the divisor. The value is converted to an integer
         *            using its `toInt()` method in base-36 before performing the division.
         * @since 1.0.0
         */
        operator fun Short.div(base36: Base36) = div(base36.toInt())
        /**
         * Performs division between a Long and a Base36 instance.
         *
         * @param base36 The Base36 instance to divide the Long by. The Base36 value is converted to its Long representation
         *            before performing the division.
         * @since 1.0.0
         */
        operator fun Long.div(base36: Base36) = div(base36.toLong())
        /**
         * Divides a Float by a Base36 value, converting the Base36 instance to a Float
         * using its `toFloat` method before performing the division.
         *
         * @param base36 the Base36 value to divide by
         * @since 1.0.0
         */
        operator fun Float.div(base36: Base36) = div(base36.toFloat())
        /**
         * Divides a Double by a Base36 instance, utilizing the Base36 representation's
         * conversion to a Double for the calculation.
         *
         * @param base36 The Base36 object to divide the Double by. The provided Base36
         *            instance is converted to a Double via its internal `toDouble` 
         *            implementation before performing the division.
         * @since 1.0.0
         */
        operator fun Double.div(base36: Base36) = div(base36.toDouble())

        /**
         * Performs the remainder operation between a Byte value and a Base36 instance.
         * 
         * @param base36 The Base36 value to use as the divisor. The {@code toInt()} method of the Base36 object
         *            is used to convert its value to an integer in base 36 for the calculation.
         * @since 1.0.0
         */
        operator fun Byte.rem(base36: Base36) = rem(base36.toInt())
        /**
         * Computes the remainder of this integer divided by the numeric value of the provided Base36 object.
         *
         * @param base36 The Base36 object whose numeric value is used as the divisor.
         * @since 1.0.0
         */
        operator fun Int.rem(base36: Base36) = rem(base36.toInt())
        /**
         * Computes the remainder of dividing this [Short] by the integer value of the given [Base36] object.
         *
         * @param base36 the [Base36] object whose integer value will be used as the divisor.
         * @since 1.0.0
         */
        operator fun Short.rem(base36: Base36) = rem(base36.toInt())
        /**
         * Computes the remainder of the division of this `Long` value by a `Base36` value.
         *
         * The operation is equivalent to `this % hex.toLong()`, where `hex.toLong()` converts
         * the `Base36` value to its numeric representation in base-10.
         *
         * @param base36 the `Base36` value used as the divisor for the remainder operation.
         * @since 1.0.0
         */
        operator fun Long.rem(base36: Base36) = rem(base36.toLong())
        /**
         * Performs the modulo operation between the Float receiver and a Base36 object.
         * The calculation is performed by converting the Base36 object to its Float representation.
         *
         * @param base36 The Base36 object to be used as the divisor in the modulo operation.
         * @since 1.0.0
         */
        operator fun Float.rem(base36: Base36) = rem(base36.toFloat())
        /**
         * Computes the remainder of a division operation between a [Double] and a [Base36] number.
         *
         * @param base36 the divisor represented as a [Base36] number. It is internally converted to a [Double].
         * @since 1.0.0
         */
        operator fun Double.rem(base36: Base36) = rem(base36.toDouble())

        class Serializer : ValueSerializer<Base36>() {
            override fun serialize(value: Base36, gen: JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<Base36>() {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = Base36(p.string)
        }

        class OldSerializer : JsonSerializer<Base36>() {
            override fun serialize(value: Base36, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: SerializerProvider) = gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<Base36>() {
            override fun deserialize(p: com.fasterxml.jackson.core.JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Base36 = Base36(p.text)
        }

        /**
         * A JPA attribute converter that manages conversion between the `Base36` and `String` types.
         *
         * The converter is annotated with `@jakarta.persistence.Converter` and is automatically applied
         * to entities where the `Base36` type is used. The primary purpose of the class is to handle
         * the persistence of `Base36` objects as strings in the database and vice versa.
         *
         * @since 1.0.0
         */
        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Base36?, String?> {
            /**
             * Converts a Base36 attribute to its String representation for database storage.
             *
             * @param attribute the Base36 object to be converted, or null if no value is present.
             * @return the String representation of the Base36 object, or null if the input attribute is null.
             * @since 1.0.0
             */
            override fun convertToDatabaseColumn(attribute: Base36?): String? = attribute?.value
            /**
             * Converts a database column value represented as a String into a Base36 entity attribute.
             *
             * @param dbData the String value retrieved from the database column, which may be null
             * @return a Base36 instance representing the provided dbData, or null if dbData is null
             * @since 1.0.0
             */
            override fun convertToEntityAttribute(dbData: String?): Base36? = dbData?.let { Base36(it) }
        }
    }

    /**
     * Retrieves the element at the specified index from the value.
     *
     * @param index The position of the element to retrieve.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     * @since 1.0.0
     */
    override operator fun get(index: Int) = value[index]

    /**
     * Returns a new CharSequence that is a subsequence of the current sequence.
     *
     * @param startIndex The beginning index of the subsequence, inclusive.
     * @param endIndex The ending index of the subsequence, exclusive.
     * @return The specified subsequence as a CharSequence.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = value.subSequence(startIndex, endIndex)

    /**
     * Returns the string representation of the object.
     *
     * This method overrides the default `toString` implementation
     * to provide a string that corresponds to the internal `value`
     * of the class.
     *
     * @return A string representation of the object.
     * @since 1.0.0
     */
    override fun toString() = value

    /**
     * Converts the internal `value` representation of the current instance to a `String`
     * formatted in the specified text case.
     *
     * @param textCase the desired text case format to apply to the resulting string.
     *                 Defaults to [dev.tommasop1804.kutils.classes.constants.TextCase.UPPER_CASE] if not specified.
     * @return the string representation of the current instance formatted in the specified text case.
     * @since 1.0.0
     */
    fun toString(textCase: TextCase) = value.convertCase(TextCase.UPPER_CASE, textCase)

    /**
     * Converts the value of this Base36 instance to uppercase.
     *
     * This method returns a new Base36 representation of the value,
     * transformed entirely to uppercase format. It is useful for
     * normalizing and ensuring consistency in cases where uppercase
     * formatting is required.
     *
     * @return A new Base36 instance with its value represented in uppercase.
     *
     * @since 1.0.0
     */
    fun uppercase() = Base36(+value)

    /**
     * Converts the value of the current Base36 instance to its lowercase representation.
     *
     * @return A new Base36 instance with the lowercase representation of the current value.
     * @since 1.0.0
     */
    fun lowercase() = Base36(-value)

    /**
     * Converts the Base36 value to its `Double` representation.
     *
     * This method first converts the Base36 value to a `Long` using the `toLong` method
     * and then converts the resulting `Long` value to a `Double`.
     *
     * @return The `Double` representation of the Base36 value.
     * @since 1.0.0
     */
    override fun toDouble() = toLong().toDouble()

    /**
     * Converts the current Base36 value to its equivalent floating-point representation.
     *
     * The conversion is done by first converting the value to a `Long` based on the Base36 radix
     * and then converting the resulting `Long` to a `Float`.
     *
     * @return A `Float` representation of the current Base36 value.
     * @since 1.0.0
     */
    override fun toFloat() = toLong().toFloat()

    /**
     * Converts the current object to its `Long` representation using a base-36 numeral system.
     *
     * This method interprets the value as a string in base-36 and converts it into a `Long`.
     *
     * @return The `Long` equivalent of the value in base-36.
     * @throws NumberFormatException If the value cannot be converted to a `Long` in base-36.
     * @since 1.0.0
     */
    override fun toLong() = value.toLong(radix = 36)

    /**
     * Converts the current value to its integer representation using a radix of 36.
     *
     * The method interprets the underlying value as a base-36 number and returns
     * its corresponding integer value. This is useful when working with 
     * alphanumeric strings or custom encoding schemes that rely on base-36 conversions.
     *
     * @return The integer representation of the current value.
     * @throws NumberFormatException if the value cannot be properly converted 
     *         to an integer using base-36.
     *
     * @since 1.0.0
     */
    override fun toInt() = value.toInt(radix = 36)

    /**
     * Converts the current value into a `Short` representation using a radix of 36.
     * This method internally invokes the `toShort` function on the underlying value.
     *
     * @return The `Short` representation of the value using base-36 conversion.
     * @throws NumberFormatException If the conversion to `Short` fails.
     * @since 1.0.0
     */
    override fun toShort() = value.toShort(radix = 36)

    /**
     * Converts the value of the current object to its `Byte` representation
     * using a radix of 36.
     *
     * The method interprets the numerical value of the object and converts it 
     * to a byte value based on the specified numeral system (radix 36).
     * 
     * This allows for encoding and decoding of values in a compact byte form,
     * suitable for specific use cases like serialization or compact numeric processing.
     *
     * @return A `Byte` representation of the current object's value converted using radix 36.
     * @since 1.0.0
     */
    override fun toByte() = value.toByte(radix = 36)

    /**
     * Converts the current Base36 value to an unsigned long (ULong) representation.
     *
     * The conversion is performed using a radix of 36, which interprets the value as a 
     * base-36 encoded string and computes its corresponding numerical value as an unsigned long.
     *
     * @return The converted unsigned long value.
     * @since 1.0.0
     */
    fun toULong() = value.toULong(radix = 36)

    /**
     * Converts the value represented in base-36 to an unsigned long (`ULong`), or returns `null` if the conversion fails.
     *
     * This method interprets the current `value` as a base-36 encoded string and attempts to parse it into an unsigned long.
     * The conversion will fail and return `null` if the `value` contains invalid characters 
     * for base-36 representation or if the resulting number exceeds the range of `ULong`.
     *
     * @return The converted `ULong` value if the conversion is successful, or `null` otherwise.
     *
     * @since 1.0.0
     */
    fun toULongOrNull() = value.toULongOrNull(radix = 36)

    /**
     * Converts the current value to an unsigned integer using a radix of 36.
     *
     * This method interprets the current value as a number in base-36 and converts 
     * it to its corresponding unsigned integer representation.
     *
     * Note: The behavior may throw an exception if the value cannot be properly 
     * converted (e.g., invalid characters for base-36 or value out of range).
     * 
     * @return The unsigned integer representation of the value after conversion.
     * @since 1.0.0
     */
    fun toUInt() = value.toUInt(radix = 36)

    /**
     * Converts the given string `value` to an unsigned integer (`UInt`) using a radix of 36.
     * 
     * If the string is not a valid representation of a number in the base-36 numeral system
     * or if it exceeds the range of an unsigned integer, this method returns `null`.
     *
     * @return The resulting `UInt` value if the conversion is successful, or `null` if the conversion fails.
     * @throws NumberFormatException If the string contains invalid characters that cannot be parsed in base-36.
     * 
     * @since 1.0.0
     */
    fun toUIntOrNull() = value.toUIntOrNull(radix = 36)

    /**
     * Converts a numeric value represented as a string in base-36 format 
     * to an unsigned 16-bit integer (UShort).
     *
     * The string representation must be a valid base-36 number. 
     * Any invalid input, such as characters outside the base-36 range, 
     * will result in a runtime exception.
     *
     * @return the converted UShort value.
     * @throws NumberFormatException if the input is not a valid base-36 number
     * or if the value exceeds the range of UShort.
     * @since 1.0.0
     */
    fun toUShort() = value.toUShort(radix = 36)

    /**
     * Attempts to convert a string representation of a number in the specified radix (base 36) 
     * to an unsigned 16-bit integer (`UShort`). If the conversion is not possible, it returns `null`.
     *
     * This method leverages the `toUShortOrNull` function and interprets the string using base 36, 
     * which includes digits 0-9 and letters a-z (case insensitive).
     *
     * @return The unsigned 16-bit integer (`UShort`) representation of the string if the conversion
     *         is successful, or `null` if the string cannot be parsed as a valid number in base 36.
     * @throws IllegalArgumentException if the string contains invalid characters outside of 
     *                                   the base 36 range or is malformed in any way.
     * @since 1.0.0
     */
    fun toUShortOrNull() = value.toUShortOrNull(radix = 36)

    /**
     * Converts the given value to an unsigned byte using the specified radix.
     *
     * This method internally uses a radix of 36 for conversion.
     * It interprets the value as a string representation of a number in base 36 
     * and converts it to an unsigned 8-bit integer.
     *
     * @return The unsigned byte representation of the value.
     * @since 1.0.0
     */
    fun toUByte() = value.toUByte(radix = 36)

    /**
     * Converts the given value to a [UByte] using the specified base-36 radix.
     * 
     * If the value cannot be represented as a [UByte], the method returns `null`.
     * 
     * @receiver The string representation of the value to be converted.
     * @return The corresponding [UByte] if conversion is successful, or `null` if the value is not a valid base-36 representation 
     *         or does not fit in the range of [UByte].
     * @throws NumberFormatException If the string contains invalid characters for the given radix.
     * @since 1.0.0
     */
    fun toUByteOrNull() = value.toUByteOrNull(radix = 36)

    /**
     * Converts the string representation of a Base36 value into an array of bytes.
     *
     * @return a ByteArray representing the Base36 value.
     * @since 1.0.0
     */
    fun toByteArray(): ByteArray {
        val paddedHex = if (value.length.isOdd) "0$value" else value
        return paddedHex.chunked(2).map { it.toInt(36).toByte() }.toByteArray()
    }

    /**
     * Adds the given number to the current Base36 instance and returns a new Base36 instance
     * representing the sum.
     *
     * @param other The number to be added to the current Base36 instance.
     * @return A new Base36 instance whose value is the sum of the current instance and the given number.
     * @since 1.0.0
     */
    operator fun plus(other: Number) = Base36(toLong() + other.toLong())

    /**
     * Subtracts the given number from the current Base36 instance.
     *
     * @param other The number to be subtracted, provided as a `Number`.
     * @return A new `Base36` instance representing the result of the subtraction.
     * @throws ArithmeticException If the result of the subtraction is negative.
     * @since 1.0.0
     */
    operator fun minus(other: Number): Base36 {
        if ((toLong() - other.toLong()) < 0) throw ArithmeticException("The result of the subtraction will be postive or zero")
        return Base36(toLong() - other.toLong())
    }

    /**
     * Multiplies the current Base36 value by the specified number.
     *
     * @param other The number to multiply with the current Base36 value.
     * @return A new Base36 instance representing the product of the current value and the specified number.
     * @since 1.0.0
     */
    operator fun times(other: Number) = Base36(toLong() * other.toLong())

    /**
     * Divides the current Base36 value by the given number and returns the result as a new Base36 instance.
     *
     * @param other The number to divide the current Base36 value by. It must be of type Number.
     * @return A new Base36 instance representing the result of the division.
     * @since 1.0.0
     */
    operator fun div(other: Number) = Base36(toLong() / other.toLong())

    /**
     * Performs the modulo operation on the current number and the specified [other] number, returning the result as a new Base36 instance.
     *
     * @param other The number to divide the current number by for the modulo operation.
     * @return A new Base36 instance representing the result of the modulo operation.
     * @since 1.0.0
     */
    operator fun rem(other: Number) = Base36(toLong() % other.toLong())

    /**
     * Increments the current value by 1 and returns a new instance of `Base36` representing the result.
     *
     * This operator function allows for the use of the `++` operator with `Base36` objects.
     * The current object's value remains unchanged, and a new `Base36` instance is created.
     *
     * @return A new `Base36` instance with the incremented value.
     * @since 1.0.0
     */
    operator fun inc() = Base36(toLong() + 1)

    /**
     * Decrements the current Base36 instance by 1.
     * If the result of the decrement is negative, a NumberSignException will be thrown.
     *
     * @return a new Base36 instance with a decremented value.
     * @throws dev.tommasop1804.kutils.exceptions.NumberSignException if the result of the decrement operation is negative.
     * @since 1.0.0
     */
    operator fun dec(): Base36 {
        if ((toLong() - 1).isNegative) throw NumberSignException("The result of the decrement will be positive or zero")
        return Base36(toLong() - 1)
    }

    /**
     * Compares this number with the specified number for order.
     * Returns a negative integer, zero, or a positive integer as this number
     * is less than, equal to, or greater than the specified number.
     *
     * @param other The number to be compared with this number.
     * @return A negative integer, zero, or a positive integer as this number
     * is less than, equal to, or greater than the specified number.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Number) = toLong().compareTo(other.toLong())
}