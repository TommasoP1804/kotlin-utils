package dev.tommasop1804.kutils.classes.code

import com.fasterxml.jackson.core.JsonGenerator
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
import java.awt.image.BufferedImage
import java.io.File
import java.io.OutputStream
import java.nio.file.Path

/**
 * Represents an EAN-13 code as a value class that enforces validation and provides
 * utility functions for barcode generation and manipulation.
 *
 * The EAN-13 code standard is typically used for identifying products with a 13-digit
 * numeric code, and this class ensures that the code meets the required format.
 *
 * This value class implements the `CharSequence` interface for utility methods,
 * as well as the `ProductCode` and `ProductCode.EAN` interfaces for specialized
 * operations with product codes and barcode generation.
 *
 * @property value The 13-digit string representing the EAN-13 code.
 * @throws dev.tommasop1804.kutils.exceptions.MalformedInputException If the provided input does not meet the EAN-13 format.
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@JvmInline
@Suppress("unused")
@JsonSerialize(using = EAN13.Companion.Serializer::class)
@JsonDeserialize(using = EAN13.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = EAN13.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = EAN13.Companion.OldDeserializer::class)
value class EAN13 private constructor(override val value: String) : CharSequence, ProductCode, ProductCode.EAN, PrintableBarcode {
    /**
     * Returns the length of the underlying value representing the EAN-13 code.
     * This property delegates to the `length` of the string value.
     *
     * @since 1.0.0
     */
    override val length
        get() = value.length

    /**
     * Secondary constructor for the EAN13 value class,
     * which accepts a [CharSequence], converts it to a trimmed string,
     * and delegates to the primary constructor.
     *
     * @param value The input value to be processed and stored as a trimmed string.
     * @since 1.0.0
     */
    constructor(value: CharSequence) : this(value.toString().trim())

    init {
        validateInputFormat(value.isValidEAN13(), EAN13::class)
    }

    companion object {
        /**
         * Validates if the string conforms to the EAN-13 standard.
         *
         * This method checks if the string:
         * 1. Consists of exactly 13 numeric characters.
         * 2. Contains a valid control digit as the last character, calculated using the EAN-13 checksum algorithm.
         *
         * @receiver The string to be validated as an EAN-13 code.
         * @return `true` if the string is a valid EAN-13 code, `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidEAN13() = matches(Regex("[0-9]{13}")) && computeCheckDigit(toString() - 1) == this[12]

        /**
         * Converts a string to an instance of the EAN13 class, encapsulating a valid EAN-13 barcode.
         *
         * This method uses the provided string to construct an `EAN13` object. If the string does not
         * conform to the expected requirements of an EAN-13 barcode, an exception will be thrown and
         * caught, returning a `Result` encapsulating the failure. This ensures safe handling of
         * potential parsing issues.
         *
         * @receiver the string to be converted.
         * @return a `Result` containing the `EAN13` object if the conversion succeeds, or the exception
         * if it fails.
         * @since 1.0.0
         */
        fun CharSequence.toEAN13() = filter { it.isDigit() }.run { runCatching { EAN13(this) } }

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
            code.afterLast(')').validateInputFormat(EAN13::class) {
                matches(Regex("[0-9]+")) && length == 12
            }
            val sum = code.mapIndexed { index, ch -> if (index.isEven) ch.digitToInt() else ch.digitToInt() * 3 }.sum()
            if (sum % 10 == 0) return '0'
            return ((((sum / 10) + 1) * 10) - sum).digitToChar()
        }

        class Serializer : ValueSerializer<EAN13>() {
            override fun serialize(value: EAN13, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<EAN13>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = EAN13(p.string)
        }

        class OldSerializer : JsonSerializer<EAN13>() {
            override fun serialize(value: EAN13, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<EAN13>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): EAN13 = EAN13(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<EAN13?, String?> {
            override fun convertToDatabaseColumn(attribute: EAN13?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): EAN13? = dbData?.let { EAN13(it) }
        }
    }

    /**
     * Returns the character at the specified index of the string representation of the EAN13 value.
     *
     * @param index the index of the character to be retrieved.
     * @return the character at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range of the EAN13 value.
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this character sequence.
     *
     * @param startIndex the start index (inclusive) of the subsequence.
     * @param endIndex the end index (exclusive) of the subsequence.
     * @return the specified subsequence.
     * @throws IndexOutOfBoundsException if the start or end indices are out of range.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns the string representation of the EAN13 value.
     * This method provides the raw string backing the EAN13 instance, which represents the product code.
     *
     * @return The string value of the EAN13 code.
     * @since 1.0.0
     */
    override fun toString() = value

    /**
     * Converts the generated matrix data into a BufferedImage representation.
     * This method utilizes the `MatrixToImageWriter` to create a visual representation
     * of the EAN-13 barcode.
     *
     * @return a BufferedImage representing the rendered EAN-13 barcode as a matrix image.
     * @since 1.0.0
     */
    override fun toBufferedImage(): BufferedImage = MatrixToImageWriter.toBufferedImage(generateMatrix())

    /**
     * Converts the generated matrix to a `BufferedImage` using the provided configuration.
     *
     * @param config specifies the configuration options for rendering the matrix into an image.
     * @return a `BufferedImage` representing the matrix with applied configuration settings.
     * @since 1.0.0
     */
    override fun toBufferedImage(
        config: MatrixToImageConfig
    ): BufferedImage = MatrixToImageWriter.toBufferedImage(generateMatrix(), config)

    /**
     * Writes the generated barcode matrix to the specified file path in the given format.
     *
     * @param format the desired format in which the barcode will be written, for example, "PNG" or "JPG".
     * @param file the file to which the barcode will be written.
     * @since 1.0.0
     */
    override fun writeToPath(
        format: String,
        file: File
    ) {
        MatrixToImageWriter.writeToPath(generateMatrix(), format, file.toPath())
    }

    /**
     * Writes the generated matrix representation to a file at the specified path in the given format.
     *
     * @param format the file format in which the matrix should be written (e.g., "PNG", "JPG").
     * @param file the path where the file should be written.
     * @since 1.0.0
     */
    override fun writeToPath(
        format: String,
        file: Path
    ) {
        MatrixToImageWriter.writeToPath(generateMatrix(), format, file)
    }

    /**
     * Writes the generated matrix representation of the EAN-13 code to the specified output stream
     * in the given image format.
     *
     * @param format the desired format of the output image (e.g., "PNG", "JPG").
     * @param stream the output stream to which the image representation will be written.
     * @since 1.0.0
     */
    override fun writeToStream(
        format: String,
        stream: OutputStream
    ) {
        MatrixToImageWriter.writeToStream(generateMatrix(), format, stream)
    }

    /**
     * Writes the encoded EAN-13 barcode as an image to the specified output stream.
     *
     * @param format the format of the image (e.g., "PNG", "JPG"). Must be a supported format.
     * @param stream the output stream to write the generated image to.
     * @param config the configuration for generating the image, including colors and other parameters.
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
     * Generates a BitMatrix representation of the EAN-13 barcode based on the value provided.
     *
     * This method uses the `MultiFormatWriter` from the ZXing library to encode the value.
     * The generated matrix has fixed dimensions with a width of 200 and a height of 100.
     *
     * @return A BitMatrix object representing the encoded EAN-13 barcode.
     * @since 1.0.0
     */
    override fun generateMatrix(): BitMatrix {
        val writer = MultiFormatWriter()
        return writer.encode(value, BarcodeFormat.EAN_13, 285, 120)
    }
}