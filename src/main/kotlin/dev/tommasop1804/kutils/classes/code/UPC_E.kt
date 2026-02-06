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
 * Represents a UPC-E formatted barcode.
 * This is a compact version of UPC-A, used for small packages where space is limited.
 *
 * The UPC-E format contains 7 digits which include a 6-digit payload and a check digit.
 * It is validated upon initialization to ensure conformity with the required structure.
 *
 * @property value The string representation of the UPC-E code.
 * @constructor Initializes the UPC-E from a given sequence of characters.
 * @throws dev.tommasop1804.kutils.exceptions.MalformedInputException If the provided UPC-E code is invalid.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JvmInline
@Suppress("unused", "functionName", "className")
@JsonSerialize(using = UPC_E.Companion.Serializer::class)
@JsonDeserialize(using = UPC_E.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = UPC_E.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = UPC_E.Companion.OldDeserializer::class)
value class UPC_E private constructor(override val value: String) : CharSequence, ProductCode, ProductCode.UPC, PrintableBarcode {
    /**
     * Represents the length of the value held by this property.
     * The length is dynamically calculated based on the size of the value.
     *
     * @since 1.0.0
     */
    override val length
        get() = value.length

    /**
     * Secondary constructor that initializes an instance of the UPC_E class
     * by taking a CharSequence input, converting it to a trimmed string,
     * and passing it to the primary constructor.
     *
     * @param value The CharSequence input that represents the UPC-E code
     * @since 1.0.0
     */
    constructor(value: CharSequence) : this(value.toString().trim())

    init {
        validateInputFormat(value.isValidUPC_E(), UPC_E::class)
    }

    companion object {
        /**
         * Checks if a string is a valid UPC-E code.
         *
         * A valid UPC-E code must satisfy the following criteria:
         * - It consists of exactly 7 numeric characters.
         * - The last character (check digit) is computed correctly based on the first six digits.
         *
         * The function uses a regular expression to validate the 7-digit numeric format, and then computes
         * the check digit using the `computeCheckDigit` function to validate it against the provided check digit.
         *
         * @receiver The string to validate as a UPC-E code.
         * @return `true` if the string is a valid UPC-E code, `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidUPC_E() = matches(Regex("[0-9]{8}")) && computeCheckDigit(toString() - 1) == this[7]

        /**
         * Converts the current string into a UPC_E-compatible object by filtering out non-digit characters and
         * attempting to parse the result as a UPC_A instance.
         *
         * The function processes the string by retaining only numeric characters, then creates a `UPC_A` instance
         * if the filtered content is valid for a UPC_A barcode.
         *
         * @receiver The string to be converted to a UPC_E-compatible format.
         * @return A `Result` wrapping a `UPC_A` instance if the conversion is successful, or an exception otherwise.
         * @since 1.0.0
         */
        fun CharSequence.toUPC_E() = filter { it.isDigit() }.run { runCatching { UPC_A(this) } }

        class Serializer : ValueSerializer<UPC_E>() {
            override fun serialize(value: UPC_E, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<UPC_E>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = UPC_E(p.string)
        }

        class OldSerializer : JsonSerializer<UPC_E>() {
            override fun serialize(value: UPC_E, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<UPC_E>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): UPC_E = UPC_E(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<UPC_E?, String?> {
            override fun convertToDatabaseColumn(attribute: UPC_E?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): UPC_E? = dbData?.let { UPC_E(it) }
        }
    }

    /**
     * Retrieves the element at the specified index from the value.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence.
     * The subsequence starts at the specified `startIndex` and ends right before the specified `endIndex`.
     *
     * @param startIndex the start index of the subsequence, inclusive
     * @param endIndex the end index of the subsequence, exclusive
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns a string representation of the object. This method overrides the
     * default implementation to provide a custom string representation of the value.
     *
     * @return A string representation of the value.
     * @since 1.0.0
     */
    override fun toString() = value

    /**
     * Converts the internally generated matrix to a BufferedImage.
     *
     * @return a BufferedImage representation of the generated matrix.
     * @since 1.0.0
     */
    override fun toBufferedImage(): BufferedImage = MatrixToImageWriter.toBufferedImage(generateMatrix())

    /**
     * Converts the generated bit matrix into a buffered image using the specified configuration.
     *
     * @param config the configuration to customize the output image, such as colors and other settings
     * @return a BufferedImage representation of the current bit matrix
     * @since 1.0.0
     */
    override fun toBufferedImage(
        config: MatrixToImageConfig
    ): BufferedImage = MatrixToImageWriter.toBufferedImage(generateMatrix(), config)

    /**
     * Writes the generated barcode matrix to the specified file in the given format.
     *
     * @param format the image format to use (e.g., "PNG", "JPG").
     * @param file the file to which the barcode image will be written.
     * @since 1.0.0
     */
    override fun writeToPath(
        format: String,
        file: File
    ) {
        MatrixToImageWriter.writeToPath(generateMatrix(), format, file.toPath())
    }

    /**
     * Writes the generated matrix as an image to the specified file path in the provided format.
     *
     * @param format The image format (e.g., "PNG", "JPG") in which the matrix will be written.
     * @param file The target file path where the image will be saved.
     * @since 1.0.0
     */
    override fun writeToPath(
        format: String,
        file: Path
    ) {
        MatrixToImageWriter.writeToPath(generateMatrix(), format, file)
    }

    /**
     * Writes the generated matrix to the specified output stream in the given image format.
     *
     * @param format the image format in which the matrix will be written (e.g., "PNG", "JPEG").
     * @param stream the output stream to which the matrix will be written.
     * @since 1.0.0
     */
    override fun writeToStream(
        format: String,
        stream: OutputStream
    ) {
        MatrixToImageWriter.writeToStream(generateMatrix(), format, stream)
    }

    /**
     * Writes the matrix representation of the UPC_E barcode to the given output stream in the specified format.
     *
     * @param format the image format to use for the output (e.g., "PNG", "JPEG").
     * @param stream the output stream where the image representation of the barcode will be written.
     * @param config the configuration used for rendering the image, including colors and other visual properties.
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
     * Generates a BitMatrix representation of the barcode data following the EAN-8 format.
     *
     * @return a BitMatrix that encodes the barcode with the specified dimensions and format.
     * @since 1.0.0
     */
    override fun generateMatrix(): BitMatrix {
        val writer = MultiFormatWriter()
        return writer.encode(value, BarcodeFormat.UPC_E, 150, 80)
    }
}