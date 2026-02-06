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
import dev.tommasop1804.kutils.*
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
 * Represents an International Standard Book Number (ISBN) value with extended functionality for validation,
 * barcode generation, and conversion. This class ensures that the ISBN provided is in a valid, properly formatted
 * ISBN-13 representation and provides utility methods for working with barcodes.
 *
 * The `ISBN` class is implemented as a value class to provide both efficiency and correctness.
 * It implements the `CharSequence`, `ProductCode`, `ProductCode.EAN`, and `PrintableBarcode` interfaces.
 *
 * @constructor Primary constructor for private instantiation. Takes a pre-validated ISBN string.
 * @constructor Secondary public constructor for accepting and processing raw input as an ISBN.
 * @since 1.0.0
 */
@JvmInline
@Suppress("unused")
@JsonSerialize(using = ISBN.Companion.Serializer::class)
@JsonDeserialize(using = ISBN.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = ISBN.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = ISBN.Companion.OldDeserializer::class)
value class ISBN private constructor(override val value: String) : CharSequence, ProductCode, ProductCode.EAN, PrintableBarcode {
    /**
     * Provides the length of the `value` property, which represents the number of characters
     * in the underlying `value` string associated with this ISBN instance.
     *
     * This property overrides the default behavior and returns the length of the `value` string.
     *
     * @return the length of the `value` string.
     * @since 1.0.0
     */
    override val length
        get() = value.length

    /**
     * Provides the EAN prefix for an ISBN-13 value if the value contains exactly four dashes ('-').
     * The EAN prefix is computed by applying a custom substring operation to the underlying value.
     * If the dash count does not match four, the result is `null`.
     *
     * This property is designed for use exclusively with ISBN-13 format codes that conform
     * to the specified dash requirement.
     *
     * @since 1.0.0
     */
    @OnlyForSpecificType("Only works for ISBN-13 codes with 4 dashes")
    val eanPrefix
        get() = if (count { it == Char.HYPEN } == 4) 3(value) else null

    /**
     * Represents the linguistic group extracted from an ISBN-13 code.
     *
     * This property retrieves the linguistic group segment of an ISBN-13 code,
     * provided the code contains exactly four dashes ('-') as delimiters.
     * If the dash count is different from four, the value of this property will be `null`.
     *
     * When valid, the property calculates the linguistic group segment by dividing
     * the string representation of the ISBN using the dash character as a delimiter
     * and selecting the second segment.
     *
     * Note: The property requires that the ISBN complies with the proper ISBN-13 format
     * and contains exactly four dashes for accurate extraction of the linguistic group.
     * Misuse may lead to unexpected results or null values.
     *
     * For reference to the meaning of the linguistic group, see the
     * [List of ISBN registration groups](https://en.wikipedia.org/wiki/List_of_ISBN_registration_groups) article on Wikipedia.
     *
     * @since 1.0.0
     */
    @OnlyForSpecificType("Only works for ISBN-13 codes with 4 dashes")
    val linguisticGroup
        get() = if (count { it == Char.HYPEN } == 4) (value / Char.HYPEN)[1] else null

    /**
     * Retrieves the publisher information from the ISBN value.
     *
     * This property is designed to extract the publisher component of an ISBN-13 formatted string,
     * which must include exactly four dashes ('-'). If the format does not meet this requirement,
     * the property will return `null`.
     *
     * The publisher field represents the third segment of an ISBN-13 code when split by dashes.
     *
     * It is recommended to use this property carefully, as it is strictly dependent on the presence
     * and correct positioning of dashes in the ISBN value.
     *
     * For reference to the meaning of the publisher component, see the
     * [List of ISBN registration groups 0](https://en.wikipedia.org/wiki/List_of_group-0_ISBN_publisher_codes)
     * and [List of ISBN registration groups 1](https://en.wikipedia.org/wiki/List_of_group-1_ISBN_publisher_codes)
     * articles on Wikipedia.
     *
     * @return The publisher segment of the ISBN-13 value or `null` if the format is invalid.
     * @since 1.0.0
     */
    @OnlyForSpecificType("Only works for ISBN-13 codes with 4 dashes")
    val publisher
        get() = if (count { it == Char.HYPEN } == 4) (value / Char.HYPEN)[2] else null

    /**
     * Represents the title component extracted from the ISBN-13 code.
     *
     * This property retrieves the title value from an ISBN-13 code formatted with exactly four dashes.
     * If the code does not meet the required format, the value will be `null`.
     *
     * The title component is determined by splitting the `value` string using dashes as delimiters and
     * accessing the fourth segment (zero-based index 3) of the resulting collection.
     *
     * Usage of this property is marked as requiring caution and is annotated with [OnlyForSpecificType]
     * because incorrect formatting of the ISBN-13 code (e.g., missing or additional dashes)
     * may lead to unexpected results or a `null` value.
     *
     * @return the title segment from the ISBN-13 code, or `null` if the format is invalid.
     * @since 1.0.0
     */
    @OnlyForSpecificType("Only works for ISBN-13 codes with 4 dashes")
    val title
        get() = if (count { it == Char.HYPEN } == 4) (value / Char.HYPEN)[3] else null

    /**
     * Represents the check digit of an ISBN value.
     *
     * The check digit is a single character, typically used to verify the validity of an ISBN.
     * It is derived from the last character of the `ISBN` value.
     *
     * @return the final character of the ISBN value.
     * @since 1.0.0
     */
    val checkDigit
        get() = last()

    /**
     * Constructs an instance of the ISBN class from the given character sequence.
     *
     * This constructor ensures that leading and trailing spaces are trimmed from
     * the input value and removes all occurrences of the space character (' ')
     * before initializing the `ISBN` object.
     *
     * @param value the character sequence to initialize the ISBN instance with.
     * @since 1.0.0
     */
    constructor(value: CharSequence) : this(value.toString().trim() - Char.SPACE)

    init {
        validateInputFormat(value.isValidISBN(), ISBN::class)
    }

    companion object {
        /**
         * Validates if the current `CharSequence` represents a valid ISBN-13 format.
         *
         * The method checks the following:
         * - The format conforms to the ISBN-13 standard, which includes a prefix ("978" or "979"), followed by
         *   variable-length blocks separated by optional hyphens, and ending with a single-digit check digit.
         * - The check digit at the end of the sequence is computed based on the ISBN-13 calculation rules and
         *   matches the actual check digit of the input value.
         *
         * @return `true` if the `CharSequence` is a valid ISBN-13, `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidISBN() = matches(Regex("97[89]-?[0-9]{1,5}-?[0-9]{2,7}-?[0-9]{1,6}[0-9]-?[0-9]")) && filter { it.isDigit() }.length == 13 && computeCheckDigit(toString()) == last()

        /**
         * Converts the current `CharSequence` to an `ISBN` instance by filtering out
         * all non-digit characters and attempting to construct an `ISBN` object from the result.
         * If the conversion fails, the function returns a `Result` capturing the exception.
         *
         * @receiver The original `CharSequence` to be processed and converted to `ISBN`.
         * @return A `Result` containing the successfully constructed `ISBN` object or capturing an exception if the conversion fails.
         * @since 1.0.0
         */
        fun CharSequence.toISBN() = filter { it.isDigit() || it == Char.HYPEN }.run { runCatching { ISBN(this) } }

        /**
         * Computes the check digit for an input code string using the EAN-13 checksum algorithm.
         *
         * The input code is validated to ensure it meets formatting requirements: it must
         * consist of only numeric characters and be exactly 12 characters in length.
         *
         * @param code the 12-digit numeric string for which the check digit is to be computed.
         * @return the computed check digit as a single character.
         * @since 1.0.0
         */
        fun computeCheckDigit(code: String) = EAN13.computeCheckDigit(code - 1 - Char.HYPEN)

        class Serializer : ValueSerializer<ISBN>() {
            override fun serialize(value: ISBN, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<ISBN>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = ISBN(p.string)
        }

        class OldSerializer : JsonSerializer<ISBN>() {
            override fun serialize(value: ISBN, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<ISBN>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): ISBN = ISBN(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<ISBN?, String?> {
            override fun convertToDatabaseColumn(attribute: ISBN?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): ISBN? = dbData?.let { ISBN(it) }
        }
    }

    /**
     * Retrieves the element at the specified index in the collection.
     *
     * @param index the position of the element to retrieve, starting from 0.
     * @return the element at the specified index.
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence, starting at the specified [startIndex] and ending at [endIndex - 1].
     *
     * @param startIndex the starting index of the subsequence, inclusive.
     * @param endIndex the ending index of the subsequence, exclusive.
     * @return a new character sequence representing the subsequence.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns a string representation of the object.
     *
     * This function overrides the default `toString` method
     * and provides a custom string representation based on the `value`.
     *
     * @return the string representation of the `value` field.
     * @since 1.0.0
     */
    override fun toString() = value

    /**
     * Generates a BufferedImage representation of the barcode.
     *
     * @return the barcode encoded as a BufferedImage
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
     * Writes the generated barcode matrix to the specified file path using the provided image format.
     *
     * @param format the image format (e.g., "PNG", "JPG") to use when writing the barcode.
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
     * Writes the generated barcode to the specified file path in the provided image format.
     *
     * @param format the desired image format (e.g., "PNG", "JPG") to use when saving the barcode.
     * @param file the path to the file where the barcode will be written.
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
     *
     * @param format the desired format for the output (e.g., "PNG", "JPEG").
     * @param stream the output stream to which the barcode will be written.
     * @since 1.0.0
     */
    override fun writeToStream(
        format: String,
        stream: OutputStream
    ) {
        MatrixToImageWriter.writeToStream(generateMatrix(), format, stream)
    }

    /**
     * Writes the generated barcode matrix to the provided output stream in the specified format.
     * This method uses the provided configuration to customize the rendering of the matrix to an image.
     *
     * @param format the format to use when writing the matrix image (e.g., "PNG", "JPEG").
     * @param stream the output stream where the matrix image will be written.
     * @param config the configuration options used to render the matrix into an image.
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
     * Generates a `BitMatrix` representation of the ISBN barcode.
     *
     * The matrix is encoded using the EAN-13 barcode format.
     * This method removes any occurrences of the dash character ('-') from the ISBN value
     * before generating the matrix. The resulting barcode has fixed dimensions.
     *
     * @return a `BitMatrix` representing the encoded ISBN barcode.
     * @since 1.0.0
     */
    override fun generateMatrix(): BitMatrix {
        val writer = MultiFormatWriter()
        return writer.encode(value - Char.HYPEN, BarcodeFormat.EAN_13, 285, 120)
    }
}