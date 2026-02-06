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
 * Represents an EAN-13 barcode with an additional 5-digit add-on (EAN-13 P5).
 * This value class is a compact and lightweight representation of the EAN-13 P5 code.
 * It also provides various methods for encoding, validating, and generating corresponding barcode graphics.
 *
 * This class implements [CharSequence], which allows it to be treated as a sequence of characters,
 * and [ProductCode], which provides additional utility methods for barcode generation and manipulation.
 *
 * @property value The string representation of the EAN-13 P5 code.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JvmInline
@Suppress("unused", "functionName", "ClassName")
@JsonSerialize(using = EAN13_P5.Companion.Serializer::class)
@JsonDeserialize(using = EAN13_P5.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = EAN13_P5.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = EAN13_P5.Companion.OldDeserializer::class)
value class EAN13_P5 private constructor(override val value: String) : CharSequence, ProductCode, ProductCode.EAN {
    /**
     * Represents the length of the underlying string value of the `EAN13P5` instance.
     * This value corresponds to the character count of the string encapsulated
     * within the `EAN13P5` value class.
     *
     * @return The length of the underlying string.
     * @since 1.0.0
     */
    override val length
        get() = value.length

    /**
     * Secondary constructor that allows the creation of an `EAN13P5` instance from a `CharSequence`.
     * The input value is converted to a trimmed string before being passed to the primary constructor.
     *
     * @param value The `CharSequence` input used to create the `EAN13P5` instance.
     * @since 1.0.0
     */
    constructor(value: CharSequence) : this(value.toString().trim() - Char.SPACE)

    init {
        println(value)
        validateInputFormat(value.isValidEAN13_P5())
    }

    companion object {
        /**
         * Validates whether the string conforms to the EAN-13 P5 format.
         *
         * This method checks if the string matches the pattern of an 18-digit numeric value
         * (inclusive of the 5-digit extended packaging code) and if the calculated control digit
         * corresponds to the control digit present at the 13th position of the string.
         *
         * The validation is done through the following steps:
         * 1. Ensuring the string matches the `[0-9]{18}` regular expression.
         * 2. Calculating the control digit using the `computeControlDigit(String)` method.
         * 3. Comparing the calculated control digit with the character at position 12 of the string.
         *
         * @return `true` if the string is a valid EAN-13 P5 code, `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidEAN13_P5() = matches(Regex("[0-9]{13} ?[0-9]{5}")) && filter { it.isDigit() }.run { EAN13.computeCheckDigit(toString() - 6) == this[12] }

        /**
         * Attempts to create an instance of `EAN13P5` from the invoking string.
         *
         * This method wraps the construction of the `EAN13P5` object within a `runCatching` block,
         * allowing potential exceptions resulting from invalid input or other errors to be safely
         * captured and handled.
         *
         * @receiver the string to be parsed into an `EAN13P5` object.
         * @return a `Result` containing the `EAN13P5` instance if successful, or an error if the
         *         creation fails.
         * @since 1.0.0
         */
        fun CharSequence.toEAN13_P5() = filter { it.isDigit() || it == Char.SPACE }.run { runCatching { EAN13_P5(this) } }

        class Serializer : ValueSerializer<EAN13_P5>() {
            override fun serialize(value: EAN13_P5, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<EAN13_P5>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = EAN13_P5(p.string)
        }

        class OldSerializer : JsonSerializer<EAN13_P5>() {
            override fun serialize(value: EAN13_P5, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<EAN13_P5>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): EAN13_P5 = EAN13_P5(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<EAN13_P5?, String?> {
            override fun convertToDatabaseColumn(attribute: EAN13_P5?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): EAN13_P5? = dbData?.let { EAN13_P5(it) }
        }
    }

    /**
     * Retrieves the character at the specified index from the wrapped string value.
     *
     * @param index the index of the character to retrieve. Must be within the bounds of the string.
     * @return the character at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a subsequence of the character sequence represented by this object,
     * starting at the specified [startIndex] and ending just before the specified [endIndex].
     *
     * @param startIndex the start index (inclusive) of the subsequence, must be within the bounds of the sequence.
     * @param endIndex the end index (exclusive) of the subsequence, must be within the bounds of the sequence.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns the string representation of the underlying value of the `EAN13P5` instance.
     *
     * This representation corresponds directly to the `value` property, which is expected to
     * hold a valid EAN-13 with a 5-digit Extended Packaging Code (P5).
     *
     * The method does not modify the state of the object and provides an intuitive way to
     * visualize the encapsulated value in text form.
     *
     * @return The string representation of the `value` property.
     * @since 1.0.0
     */
    override fun toString() = value.insert(13, Char.SPACE)
}