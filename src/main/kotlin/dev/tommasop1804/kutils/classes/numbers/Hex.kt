package dev.tommasop1804.kutils.classes.numbers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.classes.constants.TextCase
import dev.tommasop1804.kutils.classes.constants.TextCase.Companion.convertCase
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.exceptions.NumberSignException
import dev.tommasop1804.kutils.exceptions.ValidationFailedException
import dev.tommasop1804.kutils.isOdd
import dev.tommasop1804.kutils.unaryMinus
import dev.tommasop1804.kutils.unaryPlus
import dev.tommasop1804.kutils.validate
import jakarta.persistence.AttributeConverter
import org.bouncycastle.util.Strings
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize

/**
 * Represents a hexadecimal string value and enforces validation to ensure the string
 * contains valid hexadecimal characters (digits '0'-'9' and letters 'A'-'F' or 'a'-'f').
 * This class implements the [CharSequence] interface.
 *
 * A `HexString` is immutable and provides methods to access its properties and content,
 * as well as custom serialization and deserialization logic.
 *
 * @constructor Creates a [Hex] instance after validating the input string.
 * Throws [dev.tommasop1804.kutils.MalformedInputException] if the input string contains invalid hexadecimal characters.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Hex.Companion.Serializer::class)
@JsonDeserialize(using = Hex.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Hex.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Hex.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_drop_as_int_invoke")
class Hex(value: String) : Number(), CharSequence, Comparable<Number> {
    /**
     * Represents a constant string value.
     *
     * This value can be used as a predefined constant or a default string in various contexts.
     *
     * @since 1.0.0
     */
    private val value: String

    /**
     * Returns the length of the hexadecimal string value.
     *
     * This property provides the number of characters contained in the underlying hexadecimal string.
     *
     * @return The length of the hexadecimal string.
     * @since 1.0.0
     */
    override val length: Int
        get() = value.length

    /**
     * Secondary constructor that initializes the object using a numeric value.
     * Converts the provided number to its hexadecimal representation in uppercase format.
     * Ensures that the number is greater than zero before initializing.
     *
     * @param number A numeric value that will be converted to its hexadecimal representation.
     * @throws ValidationFailedException If the provided number is not greater than zero.
     * @since 1.0.0
     */
    constructor(number: Number) : this(+number.toLong().toString(16)) {
        validate(number.toLong() >= 0) { "The number must be greater than zero" }
    }

    /**
     * Constructs an instance using the provided byte array, starting at the beginning of the array,
     * and using the entire size of the array as the data range.
     *
     * @param data The input byte array used to initialize the instance.
     * @since 1.0.0
     */
    constructor(data: ByteArray) : this(data, 0, data.size)

    /**
     * Constructs an instance using a hex-encoded string representation of the specified
     * byte array, starting at a given offset and for a given length.
     *
     * @param data The byte array to be encoded into a hex string. Can be null.
     * @param off The starting offset within the byte array from which encoding begins.
     * @param length The number of bytes to encode from the byte array starting at the offset.
     *
     * @since 1.0.0
     */
    constructor(data: ByteArray?, off: Int, length: Int) : this(
        Strings.fromByteArray(org.bouncycastle.util.encoders.Hex.encode(data, off, length)!!)
    )

    init {
        value.matches(Regex("^#?(0x)?[0-9A-Fa-f]+$")) || throw MalformedInputException("The string is not a valid hexadecimal string")
        this.value = +(if (value.startsWith("#")) value.drop(1) else if (value.startsWith("0x")) value.drop(2) else value)
    }

    companion object {
        /**
         * Checks if the given CharSequence is a valid hexadecimal string.
         *
         * A valid hexadecimal string contains only characters 0-9 and A-F (case insensitive).
         * This method attempts to parse the CharSequence as a hexadecimal and returns true
         * if successful; otherwise, it returns false.
         *
         * @receiver The CharSequence to validate as a hexadecimal string.
         * @return True if the CharSequence is a valid hexadecimal string, false otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidHex() = runCatching { Hex(toString()) }.isSuccess

        /**
         * Converts this numeric value to its hexadecimal representation.
         *
         * This extension function creates a new instance of the [Hex] class, which encapsulates
         * the hexadecimal representation of the numeric value.
         *
         * @receiver The numeric value to be converted to a [Hex] representation.
         * @return An instance of [Hex] representing the hexadecimal equivalent of the numeric value.
         * @since 1.0.0
         */
        fun Number.toHex() = Hex(this)
        /**
         * Converts a [CharSequence] to its equivalent hexadecimal string representation by wrapping it
         * in a [Hex] instance.
         *
         * @receiver The [CharSequence] to be converted into a hexadecimal string.
         * @return A [Hex] instance representing the hexadecimal string equivalent of the input,
         * wrapped in a [Result].
         * @since 1.0.0
         */
        fun CharSequence.toHex() = runCatching { Hex(toString()) }
        /**
         * Converts the ByteArray into its hexadecimal string representation.
         *
         * This method transforms each byte of the array into a two-character
         * hexadecimal equivalent and concatenates them into a single string.
         *
         * @receiver The ByteArray to be converted.
         * @return A string containing the hexadecimal representation of the ByteArray.
         * @since 1.0.0
         */
        fun ByteArray.toHex() = Hex(this)

        /**
         * Adds the value of the given Hex object to this Byte and returns the resulting value.
         *
         * @param hex the Hex instance whose integer value is added to this Byte.
         * @since 1.0.0
         */
        operator fun Byte.plus(hex: Hex) = plus(hex.toInt())
        /**
         * Adds the given [Hex] value to this integer.
         *
         * This operator function enables the addition of a [Hex] instance to an integer.
         *
         * @param hex the hexadecimal value to be added, represented as a [Hex] instance
         * @return the result of adding the integer value of [hex] to this integer
         * @since 1.0.0
         */
        operator fun Int.plus(hex: Hex) = plus(hex.toInt())
        /**
         * Adds the given Hex value, converted to an Int, to this Short value.
         *
         * @param hex the Hex value to be added to this Short value
         * @since 1.0.0
         */
        operator fun Short.plus(hex: Hex) = plus(hex.toInt())
        /**
         * Adds the given [Hex] value to this [Long] value and returns the result.
         *
         * @param hex the [Hex] value to be added to this [Long] value
         * @return the sum of this [Long] and the [Hex] value as a [Long]
         * @since 1.0.0
         */
        operator fun Long.plus(hex: Hex) = plus(hex.toLong())
        /**
         * Adds a `Float` value to the numerical representation of a `Hex` object.
         *
         * @param hex The `Hex` object whose numerical value will be added to the `Float` operand.
         * @return The result of adding this `Float` value to the numerical value of the provided `Hex`.
         * @since 1.0.0
         */
        operator fun Float.plus(hex: Hex) = plus(hex.toFloat())
        /**
         * Adds a `Hex` value to the `Double` value on which this operator is invoked.
         *
         * @param hex the `Hex` value to be added to this `Double`.
         * @since 1.0.0
         */
        operator fun Double.plus(hex: Hex) = plus(hex.toDouble())

        /**
         * Subtracts the integer value of the provided [hex] from this Byte value.
         *
         * @param hex the Hex object whose integer value will be subtracted.
         * @since 1.0.0
         */
        operator fun Byte.minus(hex: Hex) = minus(hex.toInt())
        /**
         * Subtracts the value of the given Hex object from the current integer.
         *
         * @param hex the Hex object whose value will be subtracted.
         * @since 1.0.0
         */
        operator fun Int.minus(hex: Hex) = minus(hex.toInt())
        /**
         * Performs subtraction between this Short value and the integer representation of the given Hex.
         *
         * @param hex the Hex object whose integer value will be subtracted from this Short value.
         * @since 1.0.0
         */
        operator fun Short.minus(hex: Hex) = minus(hex.toInt())
        /**
         * Subtracts the value of the specified `Hex` object from this `Long` value.
         *
         * @param hex the `Hex` instance whose value will be subtracted.
         * @return the result of subtracting the value of `hex` from this `Long`.
         * @since 1.0.0
         */
        operator fun Long.minus(hex: Hex) = minus(hex.toLong())
        /**
         * Subtracts the value of the given `Hex` instance, converted to a `Float`, from the `Float` operand.
         *
         * @param hex the `Hex` instance whose float value will be subtracted.
         * @since 1.0.0
         */
        operator fun Float.minus(hex: Hex) = minus(hex.toFloat())
        /**
         * Subtracts the numeric value of the given `Hex` object from the `Double` on which this operator is called.
         *
         * @param hex The `Hex` object whose numeric value will be subtracted from the `Double`.
         * @since 1.0.0
         */
        operator fun Double.minus(hex: Hex) = minus(hex.toDouble())

        /**
         * Multiplies the current Byte value with the integer representation of the provided Hex value.
         *
         * @param hex The Hex instance to multiply with. It is converted to an integer before the operation.
         * @since 1.0.0
         */
        operator fun Byte.times(hex: Hex) = times(hex.toInt())
        /**
         * Multiplies an integer by the integer representation of the given Hex object.
         *
         * @param hex the Hex object to convert to an integer and multiply with
         * @since 1.0.0
         */
        operator fun Int.times(hex: Hex) = times(hex.toInt())
        /**
         * Multiplies this Short value with the integer representation of the given Hex instance.
         *
         * @param hex the Hex instance whose integer value will be multiplied with this Short value
         * @since 1.0.0
         */
        operator fun Short.times(hex: Hex) = times(hex.toInt())
        /**
         * Multiplies a `Long` value with a `Hex` value by converting the `Hex`
         * to its `Long` representation before performing the operation.
         *
         * @param hex the `Hex` value to be multiplied with the `Long` value
         * @since 1.0.0
         */
        operator fun Long.times(hex: Hex) = times(hex.toLong())
        /**
         * Multiplies a Float by a Hex object, converting the Hex to a Float before performing the operation.
         *
         * @param hex the Hex object to be multiplied by this Float
         * @since 1.0.0
         */
        operator fun Float.times(hex: Hex) = times(hex.toFloat())
        /**
         * Multiplies a [Double] value with a [Hex] object by converting the [Hex] to a [Double].
         *
         * @param hex the [Hex] object to be multiplied with the [Double].
         * @since 1.0.0
         */
        operator fun Double.times(hex: Hex) = times(hex.toDouble())

        /**
         * Divides the byte value by the specified hex value.
         *
         * @param hex the Hex instance representing the denominator to divide the byte value by.
         * @since 1.0.0
         */
        operator fun Byte.div(hex: Hex) = div(hex.toInt())
        /**
         * Divides an [Int] by the integer representation of a [Hex] instance.
         *
         * @param hex The [Hex] instance to be used for division. Its integer value is obtained via the `toInt` method.
         * @since 1.0.0
         */
        operator fun Int.div(hex: Hex) = div(hex.toInt())
        /**
         * Divides the Short value by the integer representation of a Hex object.
         *
         * @param hex an instance of the Hex class which will be converted to an integer
         *            and used as the divisor.
         * @since 1.0.0
         */
        operator fun Short.div(hex: Hex) = div(hex.toInt())
        /**
         * Divides the current Long value by the specified Hex value.
         *
         * The Hex value is first converted to a Long and the division is then performed.
         *
         * @param hex the Hex value to divide the current value by
         * @since 1.0.0
         */
        operator fun Long.div(hex: Hex) = div(hex.toLong())
        /**
         * Divides the current Float value by the value of a Hex object after converting it to Float.
         *
         * @param hex The Hex object whose float equivalent will be used as the divisor.
         * @since 1.0.0
         */
        operator fun Float.div(hex: Hex) = div(hex.toFloat())
        /**
         * Divides a Double value by the provided Hex object, converting the Hex value to a Double in the process.
         *
         * @param hex the Hex object to divide the Double by, converted into a Double value
         * @since 1.0.0
         */
        operator fun Double.div(hex: Hex) = div(hex.toDouble())

        /**
         * Computes the remainder of the division of this Byte by the specified Hex value.
         *
         * @param hex The Hex value to divide this Byte by. It is converted to an integer internally.
         * @since 1.0.0
         */
        operator fun Byte.rem(hex: Hex) = rem(hex.toInt())
        /**
         * Computes the remainder of this integer when divided by the integer value of the specified [hex].
         *
         * @param hex the Hex object whose integer value is used as the divisor.
         * @since 1.0.0
         */
        operator fun Int.rem(hex: Hex) = rem(hex.toInt())
        /**
         * Performs the remainder operation on a [Short] with the given [Hex] value.
         * The remainder is calculated by converting the [Hex] to an integer
         * and applying the modulus operation to the [Short].
         *
         * @param hex The value of type [Hex] to calculate the remainder with.
         * @since 1.0.0
         */
        operator fun Short.rem(hex: Hex) = rem(hex.toInt())
        /**
         * Computes the remainder of this Long value divided by the numeric value of the given Hex instance.
         *
         * @param hex the Hex instance providing the divisor value for the remainder operation
         * @since 1.0.0
         */
        operator fun Long.rem(hex: Hex) = rem(hex.toLong())
        /**
         * Computes the remainder of the division of this Float by the provided Hex value.
         *
         * @param hex The Hex value to divide this Float by.
         * @since 1.0.0
         */
        operator fun Float.rem(hex: Hex) = rem(hex.toFloat())
        /**
         * Computes the remainder of the division between this Double and the specified Hex value.
         *
         * @param hex The Hex value to divide this Double by.
         * @return The remainder of the division as a Double.
         * @since 1.0.0
         */
        operator fun Double.rem(hex: Hex) = rem(hex.toDouble())

        class Serializer : ValueSerializer<Hex>() {
            override fun serialize(value: Hex, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<Hex>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = Hex(p.string)
        }

        class OldSerializer : JsonSerializer<Hex>() {
            override fun serialize(value: Hex, gen: JsonGenerator, serializers: SerializerProvider) = gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<Hex>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Hex = Hex(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Hex?, String?> {
            override fun convertToDatabaseColumn(attribute: Hex?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): Hex? = dbData?.let { Hex(it) }
        }
    }

    /**
     * Returns the character at the specified index of this hexadecimal string.
     *
     * @param index The zero-based index of the character to return.
     * @return The character at the specified index.
     * @throws IndexOutOfBoundsException If the [index] is out of bounds for this string.
     * @since 1.0.0
     */
    override operator fun get(index: Int) = value[index]

    /**
     * Returns a subsequence of the hexadecimal string between the specified start and end indices.
     *
     * @param startIndex The start index, inclusive, for the subsequence.
     * @param endIndex The end index, exclusive, for the subsequence.
     * @return A CharSequence representing the selected subsequence.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = value.subSequence(startIndex, endIndex)

    /**
     * Returns the string representation of the hexadecimal value.
     *
     * @return The hexadecimal string value.
     * @since 1.0.0
     */
    override fun toString(): String = toString(HexSymbol.ZERO_X)

    /**
     * Converts the provided `symbol` and its associated value into a string representation based on the specified `textCase`.
     *
     * @param symbol the `HexSymbol` to be converted to its string representation.
     * @param textCase the case format to apply to the string representation; defaults to `TextCase.UPPER_CASE`.
     * @return the string representation of the given `symbol` and its value in the specified text case.
     * @since 1.0.0
     */
    fun toString(symbol: HexSymbol, textCase: TextCase = TextCase.UPPER_CASE) = buildString {
        append(symbol.symbol)
        append(value.convertCase(TextCase.UPPER_CASE, textCase))
    }

    /**
     * Converts the object to its string representation based on the specified text case.
     *
     * @param textCase the case formatting to apply to the string representation, such as upper or lower case.
     * @since 1.0.0
     */
    fun toString(textCase: TextCase) = toString(HexSymbol.ZERO_X, textCase)

    /**
     * Converts the hexadecimal string value to uppercase.
     *
     * @return A new HexString instance with all characters in uppercase.
     * @since 1.0.0
     */
    fun uppercase() = Hex(+value)

    /**
     * Converts the hexadecimal string to lowercase.
     *
     * This function transforms all uppercase characters in the hexadecimal string to their lowercase equivalents.
     *
     * @return A new instance of [Hex] containing the lowercase representation of the original string.
     * @since 1.0.0
     */
    fun lowercase() = Hex(-value)

    /**
     * Converts the hexadecimal value represented by this instance to a [Double].
     *
     * This method first converts the hexadecimal value to a [Long] using the [toLong] method
     * and then casts the resulting value to a [Double].
     *
     * @return the [Double] representation of the hexadecimal value.
     * @since 1.0.0
     */
    override fun toDouble() = toLong().toDouble()

    /**
     * Converts the hexadecimal string representation of the value to a floating-point number.
     *
     * This method first converts the hexadecimal string to its equivalent long representation
     * using base-16 parsing and then converts the resulting long value to a floating-point
     * number. The floating-point representation might lose precision for very large numbers.
     *
     * @return The floating-point representation of the hexadecimal value.
     * @throws NumberFormatException If the hexadecimal string cannot be parsed into a valid long value.
     * @since 1.0.0
     */
    override fun toFloat() = toLong().toFloat()

    /**
     * Converts the hexadecimal string value to its [Long] representation.
     *
     * The conversion is performed using a base-16 (hexadecimal) radix, interpreting
     * the string value as a hexadecimal number.
     *
     * @return the [Long] value corresponding to the hexadecimal representation of the string.
     * @throws NumberFormatException if the string value is not a valid hexadecimal number
     *                               or if it cannot be represented as a [Long].
     * @since 1.0.0
     */
    override fun toLong() = value.toLong(radix = 16)

    /**
     * Converts the hexadecimal string value to its decimal integer representation.
     *
     * This function interprets the hexadecimal value stored within the object
     * and converts it to a corresponding `Int` in base 10.
     *
     * @return the decimal integer representation of the hexadecimal value.
     * @throws NumberFormatException if the string cannot be parsed as a hexadecimal number.
     * @since 1.0.0
     */
    override fun toInt() = value.toInt(radix = 16)

    /**
     * Converts the hexadecimal string value to a `Short` representation.
     *
     * This function interprets the hexadecimal string, represented by the `value` property,
     * as a number in base 16 (radix = 16) and converts it to a `Short` type.
     *
     * @return the value of the hexadecimal string as a `Short`.
     * @throws NumberFormatException if the string cannot be parsed as a valid hexadecimal number.
     * @since 1.0.0
     */
    override fun toShort() = value.toShort(radix = 16)

    /**
     * Converts the hexadecimal string value to its byte representation.
     *
     * This method interprets the stored hexadecimal string value using a radix of 16
     * and converts it to a `Byte` value. It assumes the hexadecimal string is valid and
     * within the range representable by a `Byte`.
     *
     * @return The byte value of the hexadecimal string.
     * @throws NumberFormatException If the hexadecimal string cannot be parsed as a valid byte value.
     * @since 1.0.0
     */
    override fun toByte() = value.toByte(radix = 16)

    /**
     * Converts the hexadecimal string value of this [Hex] instance to an unsigned [ULong].
     *
     * This function treats the hexadecimal string as a base-16 number and converts it into
     * its corresponding unsigned [ULong] value.
     *
     * @return the unsigned [ULong] representation of the hexadecimal string.
     * @throws NumberFormatException if the string cannot be parsed as a valid hexadecimal number.
     * @since 1.0.0
     */
    fun toULong() = value.toULong(radix = 16)

    /**
     * Attempts to convert the hexadecimal string value to an [ULong].
     *
     * The conversion is performed with a radix of 16, interpreting the string
     * as a hexadecimal number. If the value cannot be parsed as a valid hexadecimal
     * representation of an unsigned long or if it contains any invalid characters,
     * the function returns `null` instead of throwing an exception.
     *
     * @return the parsed [ULong] value if the conversion is successful, or `null` if the string
     * cannot be interpreted as a valid hexadecimal representation.
     * @since 1.0.0
     */
    fun toULongOrNull() = value.toULongOrNull(radix = 16)

    /**
     * Converts the hexadecimal string value to an unsigned integer.
     *
     * This function interprets the [value] stored in the containing [Hex] class
     * as a hexadecimal number and returns its unsigned integer representation.
     * The conversion uses a radix of 16 to parse the hexadecimal string.
     *
     * @return The unsigned integer representation of the hexadecimal string.
     * @throws NumberFormatException If the [value] is not a valid hexadecimal string.
     * @since 1.0.0
     */
    fun toUInt() = value.toUInt(radix = 16)

    /**
     * Converts the hexadecimal string value of this [Hex] instance to an unsigned integer, or returns `null`
     * if the conversion fails.
     *
     * This function attempts to parse the string as an unsigned integer using a radix of 16. If the string is
     * not a valid hexadecimal value or cannot be represented as an unsigned integer, the function returns `null`.
     *
     * @return The converted [UInt] value, or `null` if the string cannot be parsed as a valid hexadecimal unsigned integer.
     * @since 1.0.0
     */
    fun toUIntOrNull() = value.toUIntOrNull(radix = 16)

    /**
     * Converts the hexadecimal string value into an unsigned 16-bit integer ([UShort]).
     *
     * This function interprets the string representation of a hexadecimal number
     * (base 16) and converts it into a [UShort] value. The string must represent
     * a valid hexadecimal number for the conversion to succeed.
     *
     * @return The [UShort] value represented by the hexadecimal string.
     * @throws NumberFormatException If the string value does not represent a valid hexadecimal number.
     * @since 1.0.0
     */
    fun toUShort() = value.toUShort(radix = 16)

    /**
     * Converts the hexadecimal string value to an unsigned 16-bit short integer, or returns `null` if the conversion fails.
     *
     * This method attempts to interpret the hexadecimal representation of the current value as an unsigned 16-bit short integer.
     * If the conversion is not possible, due to invalid hexadecimal format or overflow, the function safely returns `null`.
     *
     * @return The unsigned 16-bit short integer representation of the hexadecimal value, or `null` if the conversion fails.
     * @since 1.0.0
     */
    fun toUShortOrNull() = value.toUShortOrNull(radix = 16)

    /**
     * Converts the hexadecimal string value to an unsigned byte.
     *
     * This function interprets the hexadecimal string represented by `value`
     * as a number in base 16 and converts it to a `UByte` (unsigned 8-bit integer).
     *
     * @return The `UByte` representation of the hexadecimal string.
     * @throws NumberFormatException If the value cannot be converted to a valid `UByte`.
     * @since 1.0.0
     */
    fun toUByte() = value.toUByte(radix = 16)

    /**
     * Converts the hexadecimal string value to a [UByte] using a radix of 16, or returns null
     * if the conversion is not possible.
     *
     * This method attempts to parse the hexadecimal string and interprets it as an unsigned byte value.
     * If the string is invalid as a hexadecimal representation or the resulting value is not in the range
     * of a [UByte] (0..255), the method will return null.
     *
     * @return the parsed [UByte] value if conversion is successful, or null if the conversion fails.
     * @since 1.0.0
     */
    fun toUByteOrNull() = value.toUByteOrNull(radix = 16)

    /**
     * Converts the stored value to a ByteArray representation.
     *
     * This method is used to encode the current value into a platform-specific
     * array of bytes, which can be used for data serialization or transmission.
     *
     * @return A ByteArray representation of the value.
     * @since 1.0.0
     */
    fun toByteArray(): ByteArray {
        val paddedHex = if (value.length.isOdd) "0$value" else value
        return paddedHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }

    /**
     * Adds the given number to this Hex instance and returns the result as a new Hex instance.
     *
     * This operator function performs addition by converting both the current Hex value
     * and the provided number to their `Long` representations, executing the addition,
     * and then wrapping the sum back into a new Hex instance.
     *
     * @param other the number to be added to this Hex instance.
     * @since 1.0.0
     */
    operator fun plus(other: Number) = Hex(toLong() + other.toLong())

    /**
     * Subtracts the provided number from the current Hex value.
     * Ensures that the result is non-negative, otherwise throws an ArithmeticException.
     *
     * @param other The number to be subtracted from the current Hex value.
     * @return A new Hex instance representing the result of the subtraction.
     * @throws ArithmeticException If the result of the subtraction is negative.
     * @since 1.0.0
     */
    operator fun minus(other: Number): Hex {
        if ((toLong() - other.toLong()) < 0) throw ArithmeticException("The result of the subtraction will be postive or zero")
        return Hex(toLong() - other.toLong())
    }

    /**
     * Multiplies the current Hex object with the specified number and returns a new Hex object representing the result.
     *
     * @param other The number to multiply with the current Hex object.
     * @return A new Hex object that is the result of the multiplication.
     * @since 1.0.0
     */
    operator fun times(other: Number) = Hex(toLong() * other.toLong())

    /**
     * Divides the current Hex value by the given number and returns the resulting Hex value.
     *
     * @param other The number to divide the current Hex value by.
     * @return A new Hex object representing the result of the division.
     * @throws ArithmeticException If division by zero occurs.
     * @since 1.0.0
     */
    operator fun div(other: Number) = Hex(toLong() / other.toLong())

    /**
     * Computes the remainder of dividing this hexadecimal number by the given number.
     *
     * The result is a new hexadecimal number representing the remainder.
     *
     * @param other The divisor, represented as a [Number].
     * @return A new instance of [Hex] representing the remainder of the division.
     * @since 1.0.0
     */
    operator fun rem(other: Number) = Hex(toLong() % other.toLong())

    /**
     * Increments the current value of the Hex object by 1.
     * Provides support for the unary increment operator (++).
     *
     * @return A new Hex instance with the incremented value.
     * @since 1.0.0
     */
    operator fun inc() = Hex(toLong() + 1)

    /**
     * Decrements the current Hex value by one.
     * If the result of the decrement would be positive or zero, an ArithmeticException is thrown.
     *
     * @return A new Hex instance with the decremented value.
     * @throws NumberSignException if the result of the decrement is positive or zero.
     * @since 1.0.0
     */
    operator fun dec(): Hex {
        if ((toLong() - 1) == 0L) throw NumberSignException("The result of the decrement will be positive or zero")
        return Hex(toLong() - 1)
    }

    /**
     * Compares this number with the specified number for order.
     * Returns a negative integer, zero, or a positive integer as this number
     * is less than, equal to, or greater than the specified number.
     *
     * @param other the number to be compared with this instance
     * @return a negative integer, zero, or a positive integer as this number
     *         is less than, equal to, or greater than the specified number
     * @since 1.0.0
     */
    override operator fun compareTo(other: Number) = toLong().compareTo(other.toLong())

    /**
     * Represents a symbol used for hexadecimal notation.
     *
     * This enum provides a way to distinguish between different
     * formats of hex number representations, such as the "0x" prefix
     * or the "#" symbol.
     *
     * @property symbol The string representation of the hexadecimal symbol.
     * @since 1.0.0
     */
    enum class HexSymbol(val symbol: String) {
        /**
         * Represents an empty or absence-of-value symbol in the context of hexadecimal notation.
         *
         * This is a specific instance of the `HexSymbol` enumeration that indicates
         * no symbol or prefix is associated with the hexadecimal value.
         *
         * @since 1.0.0
         */
        NONE(""),
        /**
         * Represents the hex symbol "0x" used as a prefix for hexadecimal values.
         *
         * This is a component of the `HexSymbol` enumeration that defines various
         * symbols associated with hexadecimal formatting.
         *
         * @property symbol The string representation of the hex symbol.
         * @since 1.0.0
         */
        ZERO_X("0x"),
        /**
         * Represents the hash symbol "#" used within the HexSymbol enumeration.
         *
         * This symbol can be utilized in contexts requiring the '#' prefix format.
         *
         * @property symbol The string representation of the hash symbol.
         * @since 1.0.0
         */
        HASH("#")
    }
}