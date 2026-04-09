/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

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
 * Represents a specific variation of an EAN-8 barcode with an additional 5-digit extension (EAN-8+5).
 * This value class ensures that any instance created conforms to the rules and format of a valid EAN-8P5 code.
 * It implements `CharSequence` for string-like operations and extends functionality common to product codes.
 *
 * @constructor
 * Creates an instance of `EAN8P5` using the provided `value` as the raw barcode string.
 * The input is validated to ensure it complies with the EAN-8P5 barcode format.
 *
 * @property value
 * The string representation of the EAN-8P5 barcode.
 *
 * @since 3.0.0
 * @author Tommaso Pastorelli
 */
@JvmInline
@Suppress("unused", "functionName", "ClassName")
@JsonSerialize(using = Ean8P5.Companion.Serializer::class)
@JsonDeserialize(using = Ean8P5.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Ean8P5.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Ean8P5.Companion.OldDeserializer::class)
value class Ean8P5 private constructor(override val value: String) : CharSequence, ProductCode, ProductCode.Ean {
    /**
     * Represents the length of the `value` string in the `EAN8P5` value class.
     * This property provides the character count of the encapsulated string.
     *
     * @return The length of the `value` string.
     * @since 3.0.0
     */
    override val length
        get() = value.length

    /**
     * Secondary constructor that initializes the value of the EAN8P5 instance from the given [value].
     * The provided character sequence is converted to a string and trimmed of excess whitespace
     * before being passed to the primary constructor.
     *
     * @param value The character sequence to initialize the EAN8P5 instance.
     * @since 3.0.0
     */
    constructor(value: CharSequence) : this(value.toString().trim() - Char.SPACE)

    init {
        validateInputFormat(value.isValidEan8P5())
    }

    companion object {
        /**
         * Validates whether the string represents a correct EAN-8 barcode with a 5-digit add-on.
         *
         * This function checks if the string format conforms to a 13-digit numeric pattern and
         * verifies whether the control digit (checksum) of the EAN-8 portion matches the expected
         * control digit computed using the `computeControlDigit` logic.
         *
         * The EAN-8 with the 5-digit add-on consists of a primary 8-digit code followed by an
         * additional 5-digit numeric extension. The control digit is located at the 8th position,
         * providing error detection.
         *
         * @receiver The string to validate as an EAN-8 with a 5-digit add-on barcode.
         * @return `true` if the input string is a valid EAN-8 with a 5-digit add-on, `false` otherwise.
         * @since 3.0.0
         */
        fun CharSequence.isValidEan8P5() = matches(Regex("[0-9]{8} ?[0-9]{5}")) && filter { it.isDigit() }.run { Ean8.computeCheckDigit(toString() - 6) == this[7] }

        /**
         * Converts the string to an instance of the EAN8P5 class.
         *
         * This method attempts to parse the current string as an EAN8P5 object,
         * encapsulating an EAN-8 barcode with a 5-digit add-on.
         * The method returns a [Result] containing the successfully created
         * EAN8P5 instance or an exception if the conversion fails.
         *
         * @receiver The string to be converted.
         * @return A [Result] containing the EAN8P5 instance if successful,
         * or an exception otherwise.
         * @since 3.0.0
         */
        fun CharSequence.toEan8P5() = filter { it.isDigit() || it == Char.SPACE }.run { runCatching { Ean8P5(this) } }

        class Serializer : ValueSerializer<Ean8P5>() {
            override fun serialize(value: Ean8P5, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<Ean8P5>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = Ean8P5(p.string)
        }

        class OldSerializer : JsonSerializer<Ean8P5>() {
            override fun serialize(value: Ean8P5, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<Ean8P5>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Ean8P5 = Ean8P5(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Ean8P5?, String?> {
            override fun convertToDatabaseColumn(attribute: Ean8P5?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): Ean8P5? = dbData?.let { Ean8P5(it) }
        }
    }

    /**
     * Returns the character at the specified [index] in the underlying string representation of the EAN8P5 code.
     *
     * @param index The position of the character to return. Must be in the range from `0` to `length - 1`.
     * @return The character at the specified [index].
     * @throws IndexOutOfBoundsException If the [index] is out of range.
     * @since 3.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence.
     *
     * @param startIndex the start index (inclusive) of the subsequence.
     * @param endIndex the end index (exclusive) of the subsequence.
     * @return the specified subsequence from the string value.
     * @since 3.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns the string representation of the value of this EAN8P5 instance.
     *
     * This method provides the raw string value stored in the EAN8P5 object,
     * serving as a plain representation of the encoded product code.
     *
     * @return the raw string value of the EAN8P5.
     * @since 3.0.0
     */
    override fun toString() = value.insert(8, Char.SPACE)
}