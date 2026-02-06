package dev.tommasop1804.kutils.classes.code

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageConfig
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import java.awt.image.BufferedImage
import java.io.File
import java.io.OutputStream
import java.nio.file.Path

/**
 * Defines an interface for working with barcodes that can be rendered as images or written to various output formats.
 * This interface provides methods to generate the barcode's data matrix, convert it into image formats, and output it to files or streams.
 * All methods should correctly handle the rendering of the barcode.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
interface PrintableBarcode {
    companion object {
        /**
         * Generates a barcode as a BitMatrix based on the provided value, format, and dimensions.
         *
         * @param value The data to encode in the barcode.
         * @param format The format of the barcode (e.g., QR_CODE, CODE_128).
         * @param width The width of the generated barcode in pixels.
         * @param height The height of the generated barcode in pixels.
         * @return A BitMatrix representation of the encoded barcode.
         * @since 1.0.0
         */
        fun generateBarcode(value: String, format: BarcodeFormat, width: Int, height: Int): BitMatrix {
            val writer = MultiFormatWriter()
            return writer.encode(value, format, width, height)
        }

        /**
         * Converts a given BitMatrix into a BufferedImage.
         *
         * @param matrix the BitMatrix representation to be converted into a BufferedImage
         * @since 1.0.0
         */
        fun toBufferImage(matrix: BitMatrix) = MatrixToImageWriter.toBufferedImage(matrix)!!
        /**
         * Converts a BitMatrix into a BufferedImage using the specified configuration.
         *
         * @param matrix the BitMatrix representation of the barcode or data.
         * @param config the configuration settings for generating the BufferedImage, such as colors and rendering options.
         * @since 1.0.0
         */
        fun toBufferImage(matrix: BitMatrix, config: MatrixToImageConfig) = MatrixToImageWriter.toBufferedImage(matrix, config)!!

        /**
         * Writes the barcode or matrix representation to the specified file path in the given format.
         *
         * @param matrix The BitMatrix representing the barcode to be written.
         * @param format The image format (e.g., "PNG", "JPG") in which the barcode will be written.
         * @param file The target file where the barcode will be written.
         * @since 1.0.0
         */
        fun writeToPath(matrix: BitMatrix, format: String, file: File) = MatrixToImageWriter.writeToPath(matrix, format, file.toPath())
        /**
         * Writes the given BitMatrix representation of a barcode to the specified file path in the given format.
         *
         * @param matrix the BitMatrix representation of the barcode to be written
         * @param format the image format (e.g., "PNG", "JPG") in which the barcode image will be saved
         * @param file the destination path where the barcode image will be written
         * @since 1.0.0
         */
        fun writeToPath(matrix: BitMatrix, format: String, file: Path) = MatrixToImageWriter.writeToPath(matrix, format, file)

        /**
         * Writes the provided BitMatrix as an image to the specified output stream in the given format.
         *
         * @param matrix The BitMatrix representation of the barcode to be written.
         * @param format The format in which the image should be written (e.g., "PNG", "JPG").
         * @param stream The output stream to which the image will be written.
         * @since 1.0.0
         */
        fun writeToStream(matrix: BitMatrix, format: String, stream: OutputStream) = MatrixToImageWriter.writeToStream(matrix, format, stream)
        /**
         * Writes the BitMatrix representation of a barcode to the specified output stream in the given format,
         * using the provided configuration for rendering.
         *
         * @param matrix The BitMatrix representation of the barcode to be written.
         * @param format The image format to use (e.g., "PNG", "JPEG").
         * @param stream The output stream to which the image will be written.
         * @param config The configuration to apply when generating the image.
         * @since 1.0.0
         */
        fun writeToStream(matrix: BitMatrix, format: String, stream: OutputStream, config: MatrixToImageConfig) = MatrixToImageWriter.writeToStream(matrix, format, stream, config)
    }

    /**
     * Generates a BufferedImage representation of the barcode.
     *
     * @return the barcode encoded as a BufferedImage
     * @since 1.0.0
     */
    fun toBufferedImage(): BufferedImage
    /**
     * Converts the barcode or matrix representation into a BufferedImage using the specified configuration.
     *
     * @param config the configuration settings for creating the BufferedImage, including colors and other options
     * @return a BufferedImage representation of the barcode or matrix
     * @since 1.0.0
     */
    fun toBufferedImage(config: MatrixToImageConfig): BufferedImage
    /**
     * Writes the barcode image to the specified file path in the given format.
     *
     * @param format the image format (e.g., "PNG", "JPG") to write the barcode image.
     * @param file the destination file where the barcode image will be written.
     * @since 1.0.0
     */
    fun writeToPath(format: String, file: File)
    /**
     * Writes the barcode image to the specified file path in the given format.
     *
     * @param format The format in which the barcode image should be written (e.g., "PNG", "JPG").
     * @param file The path to the file where the barcode image will be written.
     * @since 1.0.0
     */
    fun writeToPath(format: String, file: Path)
    /**
     * Writes the barcode representation to the provided output stream in the specified format.
     *
     * @param format The format in which the barcode should be written (e.g., "png", "jpg").
     * @param stream The output stream where the barcode will be written.
     * @since 1.0.0
     */
    fun writeToStream(format: String, stream: OutputStream)
    /**
     * Writes the barcode image to the specified output stream in the given format, using the provided configuration.
     *
     * @param format The image format to use (e.g., "PNG", "JPEG").
     * @param stream The output stream to which the image will be written.
     * @param config The configuration to apply when generating the image.
     * @since 1.0.0
     */
    fun writeToStream(format: String, stream: OutputStream, config: MatrixToImageConfig)
    /**
     * Generates and returns a BitMatrix representation of the barcode.
     *
     * @return a BitMatrix object that represents the encoded barcode.
     * @since 1.0.0
     */
    fun generateMatrix(): BitMatrix
}