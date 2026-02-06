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
 * Represents an EAN-8 barcode with a 2-digit add-on (EAN-8 P2 format). This barcode is a 10-digit string
 * that consists of the 8-digit EAN-8 code followed by a 2-digit add-on.
 *
 * This value class is immutable and ensures validation upon initialization. The validation checks that
 * the input conforms to the EAN-8 P2 format, including having the correct control digit.
 *
 * @property value The validated EAN-8 P2 barcode string.
 * @constructor Creates a new EAN8P2 instance from the provided [value], validating that it adheres to
 * the EAN-8 P2 format and constrains it as a proper 10-digit representation.
 * @throws dev.tommasop1804.kutils.exceptions.MalformedInputException If the input string does not conform to the EAN-8 P2 format.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JvmInline
@Suppress("unused", "className", "functionName")
@JsonSerialize(using = EAN8_P2.Companion.Serializer::class)
@JsonDeserialize(using = EAN8_P2.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = EAN8_P2.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = EAN8_P2.Companion.OldDeserializer::class)
value class EAN8_P2 private constructor(override val value: String) : CharSequence, ProductCode, ProductCode.EAN {
    /**
     * Represents the length of the associated value. This property provides
     * the length of the `value` as an integer, typically representing the
     * number of characters in a string or elements in a collection.
     *
     * @return the length of the given `value`.
     * @since 1.0.0
     */
    override val length
        get() = value.length

    /**
     * Secondary constructor that initializes the `EAN8P2` value class
     * with a trimmed version of the input `CharSequence`.
     *
     * @param value the `CharSequence` to initialize the `EAN8P2` instance.
     * @throws dev.tommasop1804.kutils.exceptions.MalformedInputException if the input does not meet the expected EAN-8 P2 format.
     * @since 1.0.0
     */
    constructor(value: CharSequence) : this(value.toString().trim() - Char.SPACE)

    init {
        validateInputFormat(value.isValidEAN8_P2(), EAN8_P2::class)
    }

    companion object {
        /**
         * Validates whether this string is a valid EAN-8 P2 barcode.
         *
         * The method checks if the string is structured correctly according to the EAN-8 standard
         * by matching the format of 10 numeric characters. It also verifies that the computed
         * control digit matches the expected control digit located at the 8th position of the string.
         *
         * @receiver The string to evaluate.
         * @return `true` if the string is a valid EAN-8 P2 barcode, `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidEAN8_P2() = matches(Regex("[0-9]{8} ?[0-9]{2}")) && filter { it.isDigit() }.run { EAN8.computeCheckDigit(toString() - 3) == this[7] }

        /**
         * Converts the current string into an instance of `EAN8P2` while handling potential exceptions.
         *
         * Uses a safe execution block (`runCatching`) to create an `EAN8P2` object from the string.
         * If an exception occurs during the creation process, it will be caught and handled as part of the result.
         *
         * @receiver The string to be converted into an `EAN8P2` object.
         * @return A `Result<EAN8P2>` containing the converted `EAN8P2` instance if successful, or the exception if an error occurred.
         * @since 1.0.0
         */
        fun CharSequence.toEAN8_P2() = filter { it.isDigit() || it == Char.SPACE }.run { runCatching { EAN8_P2(this) } }

        class Serializer : ValueSerializer<EAN8_P2>() {
            override fun serialize(value: EAN8_P2, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<EAN8_P2>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = EAN8_P2(p.string)
        }

        class OldSerializer : JsonSerializer<EAN8_P2>() {
            override fun serialize(value: EAN8_P2, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<EAN8_P2>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): EAN8_P2 = EAN8_P2(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<EAN8_P2?, String?> {
            override fun convertToDatabaseColumn(attribute: EAN8_P2?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): EAN8_P2? = dbData?.let { EAN8_P2(it) }
        }
    }

    /**
     * Retrieves the element at the specified position in this collection.
     *
     * @param index the position of the element to retrieve
     * @return the element at the specified position in this collection
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this character sequence,
     * starting from the specified start index (inclusive) and ending at the specified
     * end index (exclusive).
     *
     * @param startIndex the start index (inclusive) of the subsequence.
     * @param endIndex the end index (exclusive) of the subsequence.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns a string representation of the object. The output format is determined by the `value` property of the instance.
     *
     * Overrides the default `toString` implementation to provide a customized string representation.
     *
     * @return A string representation of the instance.
     * @since 1.0.0
     */
    override fun toString() = value.insert(8, Char.SPACE)
}