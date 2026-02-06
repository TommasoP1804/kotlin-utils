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
import dev.tommasop1804.kutils.classes.code.ProductCode.UPC.Companion.computeCheckDigit
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
 * Represents a UPC-A barcode, which is a 12-digit numeric product code standardized for product identification.
 * This value class enforces format validation and provides operations to generate various representations
 * such as barcodes, images, and streams.
 *
 * Implements the [CharSequence], [ProductCode.UPC], and [PrintableBarcode] interfaces for data manipulation
 * and compatibility with barcode processing utilities.
 *
 * @property value The underlying string representation of the UPC-A code.
 * @constructor Accepts a validated [CharSequence] and trims it to ensure proper format.
 * @since 1.0.0
 */
@JvmInline
@Suppress("unused", "functionName", "className")
@JsonSerialize(using = UPC_A.Companion.Serializer::class)
@JsonDeserialize(using = UPC_A.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = UPC_A.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = UPC_A.Companion.OldDeserializer::class)
value class UPC_A private constructor(override val value: String) : CharSequence, ProductCode, ProductCode.UPC, PrintableBarcode {
    /**
     * Represents the length of the `value` field, which denotes the number of
     * characters in the underlying value associated with the class.
     *
     * The property is read-only and computes the length dynamically.
     *
     * @since 1.0.0
     */
    override val length
        get() = value.length

    /**
     * Constructs an instance of the UPC_A class by accepting a CharSequence, trimming its whitespace,
     * and delegating to the primary constructor with the processed string.
     *
     * @param value the input CharSequence to be processed and used to construct the instance
     * @since 1.0.0
     */
    constructor(value: CharSequence) : this(value.toString().trim())

    init {
        validateInputFormat(value.isValidUPC_A(), UPC_A::class)
    }

    companion object {
        /**
         * Validates if the string represents a valid UPC-A format.
         *
         * A valid UPC-A consists of exactly 12 numeric digits. The last digit (the check digit)
         * is calculated based on the first 11 digits using the UPC-A check digit algorithm and must match.
         *
         * @receiver The string to be validated as a UPC-A code.
         * @return `true` if the string is a valid UPC-A code, `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidUPC_A() = matches(Regex("[0-9]{12}")) && computeCheckDigit(toString() - 1) == this[11]

        /**
         * Converts the string to a UPC-A representation by filtering out all non-digit characters.
         * The resulting string is then used to create a UPC_A object.
         *
         * @receiver The original string to be converted.
         * @return A [Result] containing the [UPC_A] object if successful, or an exception if the conversion fails.
         * @since 1.0.0
         */
        fun CharSequence.toUPC_A() = filter { it.isDigit() }.run { runCatching { UPC_A(this) } }

        class Serializer : ValueSerializer<UPC_A>() {
            override fun serialize(value: UPC_A, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<UPC_A>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = UPC_A(p.string)
        }

        class OldSerializer : JsonSerializer<UPC_A>() {
            override fun serialize(value: UPC_A, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<UPC_A>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): UPC_A = UPC_A(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<UPC_A?, String?> {
            override fun convertToDatabaseColumn(attribute: UPC_A?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): UPC_A? = dbData?.let { UPC_A(it) }
        }
    }

    /**
     * Retrieves the character at the specified index in the value of the UPC_A object.
     *
     * @param index The index of the character to retrieve. Must be within the bounds of the value.
     * @return The character at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this character sequence.
     * The subsequence starts at the specified startIndex and ends right before the specified endIndex.
     *
     * @param startIndex the start index, inclusive.
     * @param endIndex the end index, exclusive.
     * @return a subsequence of the original character sequence.
     * @throws IndexOutOfBoundsException if startIndex or endIndex is invalid.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns the string representation of the object.
     *
     * The method provides the string equivalent of the `value` property of the instance.
     *
     * @return The string representation stored in the `value` property.
     * @since 1.0.0
     */
    override fun toString() = value

    /**
     * Converts the internally generated matrix representation of the barcode
     * into a BufferedImage format suitable for rendering or further processing.
     *
     * @return a BufferedImage representing the barcode.
     * @since 1.0.0
     */
    override fun toBufferedImage(): BufferedImage = MatrixToImageWriter.toBufferedImage(generateMatrix())

    /**
     * Converts the generated matrix representation of the barcode into a BufferedImage
     * with the specified rendering configuration.
     *
     * @param config The MatrixToImageConfig specifying the color and rendering options
     * for the BufferedImage.
     * @return A BufferedImage representation of the barcode based on the generated matrix
     * and the given configuration.
     * @since 1.0.0
     */
    override fun toBufferedImage(
        config: MatrixToImageConfig
    ): BufferedImage = MatrixToImageWriter.toBufferedImage(generateMatrix(), config)

    /**
     * Writes the generated barcode matrix to the specified file in the given format.
     *
     * @param format The format to use for writing the barcode image (e.g., "PNG", "JPG").
     * @param file The file where the barcode image will be saved.
     * @since 1.0.0
     */
    override fun writeToPath(
        format: String,
        file: File
    ) {
        MatrixToImageWriter.writeToPath(generateMatrix(), format, file.toPath())
    }

    /**
     * Writes a generated barcode matrix to the specified file path in the given format.
     *
     * @param format The format in which the barcode should be written (e.g., "PNG", "JPG").
     * @param file The target file path where the barcode will be written.
     * @since 1.0.0
     */
    override fun writeToPath(
        format: String,
        file: Path
    ) {
        MatrixToImageWriter.writeToPath(generateMatrix(), format, file)
    }

    /**
     * Writes the generated barcode matrix to the provided output stream in the specified format.
     * The image is encoded and output using the provided parameters.
     *
     * @param format the format in which the barcode image is to be written (e.g., "PNG", "JPEG").
     * @param stream the `OutputStream` to which the barcode image will be written.
     * @since 1.0.0
     */
    override fun writeToStream(
        format: String,
        stream: OutputStream
    ) {
        MatrixToImageWriter.writeToStream(generateMatrix(), format, stream)
    }

    /**
     * Writes the generated matrix to the given output stream in the specified format.
     *
     * @param format the format for the output (e.g., "PNG", "JPEG").
     * @param stream the output stream where the matrix will be written.
     * @param config the configuration for generating the image from the matrix.
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
     * Generates a `BitMatrix` representation of the EAN-8 barcode based on the provided value.
     *
     * This method utilizes the `MultiFormatWriter` to encode the value into the EAN-8 barcode format
     * with predefined dimensions of 285x120 pixels.
     *
     * @return a `BitMatrix` object representing the encoded EAN-8 barcode.
     * @since 1.0.0
     */
    override fun generateMatrix(): BitMatrix {
        val writer = MultiFormatWriter()
        return writer.encode(value, BarcodeFormat.UPC_A, 285, 120)
    }
}