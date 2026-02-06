package dev.tommasop1804.kutils.classes.code

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.SPACE
import dev.tommasop1804.kutils.insert
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
 * Represents a specialized value class for the EAN-13 barcode format with an optional 2-digit addendum.
 * This class ensures proper validation of the EAN-13P2 format and provides utility functions to work
 * with the encoded barcode data, including image generation and exporting.
 *
 * The EAN-13P2 format consists of a 13-digit EAN-13 code followed by a 2-digit addendum, making a total
 * length of 15 digits. The control digit of the main EAN-13 component is automatically validated.
 *
 * @property value The complete EAN-13P2 code, including the 13 digits of the EAN-13 and the 2-digit addendum.
 *
 * @constructor Initializes the `EAN13P2` value class by validating and storing the provided barcode value.
 * Ensures the provided value adheres to the EAN-13P2 specification.
 *
 * @param value The raw string input to be validated and assigned to the `EAN13P2` value class.
 *
 * @throws dev.tommasop1804.kutils.exceptions.MalformedInputException If the provided value does not conform to the EAN-13P2 format.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused", "functionName", "ClassName")
@JvmInline
@JsonSerialize(using = EAN13_P2.Companion.Serializer::class)
@JsonDeserialize(using = EAN13_P2.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = EAN13_P2.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = EAN13_P2.Companion.OldDeserializer::class)
value class EAN13_P2 private constructor(override val value: String) : CharSequence, ProductCode, ProductCode.EAN {
    /**
     * Returns the length of the string representation of the EAN13P2 value.
     * This corresponds to the number of characters in the encoded value.
     *
     * @return The length of the value string.
     * @since 1.0.0
     */
    override val length
        get() = value.length

    /**
     * Secondary constructor for initializing an instance of the `EAN13P2` value class.
     * The input `CharSequence` is converted to a trimmed `String` before being passed to the primary constructor.
     * This ensures that any leading or trailing whitespace in the input is removed.
     *
     * @param value The character sequence to initialize the `EAN13P2` instance.
     * @since 1.0.0
     */
    constructor(value: CharSequence) : this(value.toString().trim() - Char.SPACE)

    init {
        validateInputFormat(value.isValidEAN13_P2(), EAN13_P2::class)
    }

    companion object {
        /**
         * Checks if the string is a valid representation of an EAN-13 P2 code.
         *
         * An EAN-13 P2 code must:
         * - Comprise exactly 15 numeric characters.
         * - Contain a valid checksum digit as per the EAN-13 algorithm.
         *
         * The validity is determined by matching the string format with the EAN-13 P2 pattern
         * and verifying the checksum using the `computeControlDigit` function.
         *
         * @return `true` if the string is a valid EAN-13 P2 code; otherwise, `false`.
         * @since 1.0.0
         */
        fun CharSequence.isValidEAN13_P2() = matches(Regex("[0-9]{13} ?[0-9]{2}")) && filter { it.isDigit() }.run { EAN13.computeCheckDigit(toString() - 3) == this[12] }

        /**
         * Converts the current string into an instance of the `EAN13P2` class, encapsulating the EAN-13 P2 barcode logic.
         *
         * The method attempts to create an `EAN13P2` object by using the string as input and wraps
         * the result in a `Result` object, capturing any potential exceptions that might occur.
         *
         * @return A `Result` instance containing the `EAN13P2` object if the conversion succeeds,
         * or an exception if the operation fails.
         * @since 1.0.0
         */
        fun CharSequence.toEAN13_P2() = filter { it.isDigit() || it == Char.SPACE }.run { runCatching { EAN13_P2(this) } }

        class Serializer : ValueSerializer<EAN13_P2>() {
            override fun serialize(value: EAN13_P2, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<EAN13_P2>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = EAN13_P2(p.string)
        }

        class OldSerializer : JsonSerializer<EAN13_P2>() {
            override fun serialize(value: EAN13_P2, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<EAN13_P2>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): EAN13_P2 = EAN13_P2(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<EAN13_P2?, String?> {
            override fun convertToDatabaseColumn(attribute: EAN13_P2?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): EAN13_P2? = dbData?.let { EAN13_P2(it) }
        }
    }

    /**
     * Returns the character at the specified index.
     *
     * @param index The index of the character to return. Must be a valid index within the string.
     * @return The character at the specified position.
     * @throws IndexOutOfBoundsException If the index is out of range.
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence,
     * starting from the specified [startIndex] (inclusive) and ending at the
     * specified [endIndex] (exclusive).
     *
     * @param startIndex the start index (inclusive) of the subsequence
     * @param endIndex the end index (exclusive) of the subsequence
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns the string representation of the current instance.
     * The returned value corresponds to the encapsulated `value` property of the class.
     *
     * @return A string representation of the object.
     * @since 1.0.0
     */
    override fun toString() = value.insert(13, Char.SPACE)
}