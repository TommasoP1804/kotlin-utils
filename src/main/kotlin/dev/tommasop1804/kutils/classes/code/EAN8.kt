package dev.tommasop1804.kutils.classes.code

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageConfig
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import dev.tommasop1804.kutils.afterLast
import dev.tommasop1804.kutils.isOdd
import dev.tommasop1804.kutils.minus
import dev.tommasop1804.kutils.validateInputFormat
import jakarta.persistence.AttributeConverter
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.awt.image.BufferedImage
import java.io.File
import java.io.OutputStream
import java.nio.file.Path

/**
 * Represents a value class for EAN-8 product codes, a standardized 8-digit barcode format primarily used in retail.
 * This class provides utilities to validate, generate, and encode EAN-8 barcodes.
 *
 * EAN-8 is specifically used for small packaging and is a sub-format of the broader EAN barcode system.
 * It includes built-in validation that ensures the provided input matches the EAN-8 standard.
 * Instances of this class are immutable.
 *
 * @constructor Initializes an `EAN8` object after validating that the provided value conforms to the EAN-8 format.
 * The input value is trimmed before validation and storage.
 *
 * @property value Contains the raw string value representing the EAN-8 code.
 *
 * Implements `CharSequence`, allowing access to barcode digits as character indices.
 * Also implements `ProductCode` and `ProductCode.EAN`.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JvmInline
@JsonSerialize(using = EAN8.Companion.Serializer::class)
@JsonDeserialize(using = EAN8.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = EAN8.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = EAN8.Companion.OldDeserializer::class)
@Suppress("unused")
value class EAN8 private constructor(override val value: String) : CharSequence, ProductCode, ProductCode.EAN, PrintableBarcode {
    /**
     * The length of the underlying string representation of the EAN-8 code.
     *
     * This property returns the total number of characters in the `value` of this instance.
     * It implements the `length` property of the `CharSequence` interface.
     *
     * @since 1.0.0
     */
    override val length
        get() = value.length

    /**
     * Secondary constructor for the `EAN8` value class that accepts a `CharSequence` as input.
     * The provided input is converted to a trimmed `String` before being passed to the primary constructor.
     *
     * @param value the input value of type `CharSequence` to represent the EAN8 code.
     * @since 1.0.0
     */
    constructor(value: CharSequence) : this(value.toString().trim())

    init {
        validateInputFormat(value.isValidEAN8(), EAN8::class)
    }

    companion object {
        /**
         * Validates whether the current string represents a valid EAN-8 code.
         *
         * The EAN-8 format is a compact barcode standard consisting of exactly 8 numeric characters.
         * The last digit is a control digit (checksum), computed based on the preceding 7 digits.
         * This method ensures that the string matches the EAN-8 format pattern and validates the control digit.
         *
         * @receiver The string to validate as an EAN-8 code.
         * @return `true` if the string is a valid EAN-8 code, `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidEAN8() = matches(Regex("[0-9]{8}")) && computeCheckDigit(toString() - 1) == this[7]

        /**
         * Converts the current string to an EAN-8 object, encapsulating the representation of an EAN-8 barcode.
         *
         * This function performs the conversion by trying to initialize an `EAN8` instance using the string.
         * It uses a `runCatching` block to catch potential exceptions that may arise during the conversion.
         *
         * @receiver The string being converted to the EAN-8 format.
         * @return A `Result` wrapping the constructed `EAN8` instance. If an error occurs during conversion,
         * the `Result` will contain the exception.
         * @since 1.0.0
         */
        fun CharSequence.toEAN8() = filter { it.isDigit() }.run { runCatching { EAN8(this) } }

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
            code.afterLast(')').validateInputFormat(EAN8::class) {
                matches(Regex("[0-9]+")) && length == 7
            }
            val sum = code.mapIndexed { index, ch -> if (index.isOdd) ch.digitToInt() else ch.digitToInt() * 3 }.sum()
            if (sum % 10 == 0) return '0'
            return ((((sum / 10) + 1) * 10) - sum).digitToChar()
        }

        class Serializer : ValueSerializer<EAN8>() {
            override fun serialize(value: EAN8, gen: JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<EAN8>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): EAN8 = EAN8(p.string)
        }

        class OldSerializer : JsonSerializer<EAN8>() {
            override fun serialize(value: EAN8, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<EAN8>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): EAN8 = EAN8(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<EAN8?, String?> {
            override fun convertToDatabaseColumn(attribute: EAN8?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): EAN8? = dbData?.let { EAN8(it) }
        }
    }

    /**
     * Retrieves the element at the specified index from the value collection.
     *
     * @param index the index of the element to retrieve, must be within the bounds of the collection.
     * @return the element at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size).
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence, starting from the
     * specified [startIndex] (inclusive) and ending at the specified [endIndex] (exclusive).
     *
     * @param startIndex the beginning index, inclusive.
     * @param endIndex the ending index, exclusive.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns the string representation of this `EAN8` value.
     *
     * This method overrides the default `toString` to return the underlying string value
     * associated with the `EAN8` instance.
     *
     * @return The string value representing the `EAN8` code.
     * @since 1.0.0
     */
    override fun toString() = value

    /**
     * Converts the current EAN-8 code into a BufferedImage representation.
     *
     * Internally, this method generates a `BitMatrix` representation of the EAN-8 barcode
     * and converts it to a `BufferedImage` using the ZXing library's `MatrixToImageWriter`.
     *
     * @return a BufferedImage representing the EAN-8 barcode.
     * @since 1.0.0
     */
    override fun toBufferedImage(): BufferedImage = MatrixToImageWriter.toBufferedImage(generateMatrix())

    /**
     * Converts the generated barcode matrix into a BufferedImage using the specified configuration.
     *
     * @param config the configuration used for customizing the colors and appearance of the image
     * @return a BufferedImage representation of the barcode matrix
     * @since 1.0.0
     */
    override fun toBufferedImage(
        config: MatrixToImageConfig
    ): BufferedImage = MatrixToImageWriter.toBufferedImage(generateMatrix(), config)

    /**
     * Writes the generated barcode matrix to the specified file path in the given format.
     *
     * @param format The format in which the barcode will be written (e.g., "PNG", "JPG").
     * @param file The file to which the barcode will be written.
     * @since 1.0.0
     */
    override fun writeToPath(
        format: String,
        file: File
    ) {
        MatrixToImageWriter.writeToPath(generateMatrix(), format, file.toPath())
    }

    /**
     * Writes the generated barcode matrix to the specified file path in the given format.
     *
     * @param format the format in which the matrix should be written (e.g., "PNG", "JPEG").
     * @param file the file path where the barcode will be saved.
     * @since 1.0.0
     */
    override fun writeToPath(
        format: String,
        file: Path
    ) {
        MatrixToImageWriter.writeToPath(generateMatrix(), format, file)
    }

    /**
     * Encodes the current value into a barcode format and writes it to the provided output stream.
     *
     * @param format the image format for the output (e.g., "PNG", "JPEG").
     * @param stream the output stream where the encoded barcode image will be written.
     * @since 1.0.0
     */
    override fun writeToStream(
        format: String,
        stream: OutputStream
    ) {
        MatrixToImageWriter.writeToStream(generateMatrix(), format, stream)
    }

    /**
     * Writes the generated barcode matrix to the specified output stream in the given image format.
     *
     * @param format The format of the image to be written (e.g., "PNG", "JPG").
     * @param stream The output stream to which the image will be written.
     * @param config The configuration for the barcode image (e.g., colors for foreground and background).
     * @since 1.0.0
     */
    override fun writeToStream(
        format: String,
        stream: OutputStream,
        config: MatrixToImageConfig
    ) {
        MatrixToImageWriter.writeToStream(generateMatrix(), format, stream, config)
    }

    /**
     * Generates a `BitMatrix` representation of the current EAN-8 value.
     * This matrix can be used to create visual representations of the barcode.
     *
     * @return a `BitMatrix` containing the encoded EAN-8 value.
     * @since 1.0.0
     */
    override fun generateMatrix(): BitMatrix {
        val writer = MultiFormatWriter()
        return writer.encode(value, BarcodeFormat.EAN_8, 200, 100)
    }
}