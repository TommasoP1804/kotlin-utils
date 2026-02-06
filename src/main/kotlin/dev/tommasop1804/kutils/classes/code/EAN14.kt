package dev.tommasop1804.kutils.classes.code

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.afterLast
import dev.tommasop1804.kutils.classes.code.ProductCode.EAN
import dev.tommasop1804.kutils.isEven
import dev.tommasop1804.kutils.minus
import dev.tommasop1804.kutils.validateInputFormat
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize

/**
 * Represents an EAN-14 (European Article Number) code as a value class.
 * Provides utility functions for working with EAN-14 codes and integrates with the Barcode generation libraries.
 * EAN-14 is used primarily for marking cartons or packages of items.
 *
 * This class is immutable and provides validation to ensure that the EAN-14 code is valid upon creation.
 * It implements the [CharSequence], [ProductCode], and [ProductCode.EAN] interfaces for compatibility and additional functionality.
 *
 * @property value The validated EAN-14 code as a string.
 * @constructor Creates an instance of [EAN14] by validating and normalizing the input.
 * @throws dev.tommasop1804.kutils.exceptions.MalformedInputException If the input is not a valid EAN-14 code.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JvmInline
@Suppress("unused")
@JsonSerialize(using = EAN14.Companion.Serializer::class)
@JsonDeserialize(using = EAN14.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = EAN14.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = EAN14.Companion.OldDeserializer::class)
value class EAN14 private constructor(override val value: String) : CharSequence, ProductCode, EAN {
    /**
     * Returns the length of the value represented by this instance.
     * The length is determined by the underlying string value.
     *
     * This property overrides the `length` property from its implementation
     * of the `CharSequence` interface.
     *
     * @since 1.0.0
     */
    override val length
        get() = value.length

    /**
     * Secondary constructor for the EAN14 value class. Accepts a [CharSequence],
     * converts it to a trimmed string, and delegates to the primary constructor.
     *
     * @param value The input value to represent as an EAN-14 compliant string.
     * @since 1.0.0
     */
    constructor(value: CharSequence) : this(value.toString().trim())

    init {
        validateInputFormat(value.isValidEAN14(), EAN14::class)
    }

    companion object {
        /**
         * Validates whether the current string conforms to the EAN-14 standard.
         *
         * This method checks if the string matches the required format for an EAN-14 barcode,
         * which is either a plain numeric string of 14 digits or a 14-digit numeric string
         * prefixed by the optional "(01)" identifier. Additionally, it verifies if the
         * control digit at the end of the code is valid by using the `computeControlDigit` function.
         *
         * @receiver The string to validate as an EAN-14 barcode.
         * @return `true` if the string is a valid EAN-14 code, otherwise `false`.
         * @since 1.0.0
         */
        fun CharSequence.isValidEAN14() = matches(Regex("(\\(01\\))?[0-9]{14}")) && computeCheckDigit(toString() - 1) == last()

        /**
         * Converts the current string to an instance of `EAN14` by attempting to create a valid object
         * from the string value.
         *
         * The conversion is done using `runCatching`, which encapsulates the operation and returns
         * the result as a `Result<EAN14>`. If the string is not valid for the creation of an `EAN14`
         * instance, the resulting object will contain the corresponding failure exception.
         *
         * @receiver The string representation of a potential EAN14 value.
         * @return A `Result` containing the successfully created `EAN14` object, or a failure with
         * the exception that occurred during the conversion.
         * @since 1.0.0
         */
        fun CharSequence.toEAN14() = run { runCatching { EAN14(this) } }

        /**
         * Computes the check digit for a given string code based on the EAN checksum algorithm.
         * The check digit is calculated by summing the weighted values of each digit in the code,
         * and adjusting it to ensure the overall sum is divisible by 10.
         *
         * @param code the input string representing the numeric code for which the check digit is to be computed.
         * @return the character representing the computed check digit.
         * @since 1.0.0
         */
        fun computeCheckDigit(code: String): Char {
            code.afterLast(')').validateInputFormat(EAN::class) {
                matches(Regex("[0-9]+")) && length == 13
            }
            val sum = code.mapIndexed { index, ch -> if (index.isEven) ch.digitToInt() else ch.digitToInt() * 3 }.sum()
            if (sum % 10 == 0) return '0'
            return ((((sum / 10) + 1) * 10) - sum).digitToChar()
        }

        class Serializer : ValueSerializer<EAN14>() {
            override fun serialize(value: EAN14, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<EAN14>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = EAN14(p.string)
        }

        class OldSerializer : JsonSerializer<EAN14>() {
            override fun serialize(value: EAN14, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<EAN14>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): EAN14 = EAN14(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<EAN14?, String?> {
            override fun convertToDatabaseColumn(attribute: EAN14?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): EAN14? = dbData?.let { EAN14(it) }
        }
    }

    /**
     * Returns the character at the specified index in the EAN14 value.
     *
     * @param index The position of the character to retrieve, starting from 0.
     * @return The character at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range (index < 0 || index >= length).
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence.
     *
     * @param startIndex the start index (inclusive) of the subsequence
     * @param endIndex the end index (exclusive) of the subsequence
     * @return a new character sequence that is a subsequence of this sequence
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns the string representation of the value contained within this EAN14 instance.
     *
     * This method overrides the default `toString` implementation to provide
     * a meaningful string representation of the internal value.
     *
     * @return The string representation of the EAN14 value.
     * @since 1.0.0
     */
    override fun toString() = value
}