package dev.tommasop1804.kutils.classes.web

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.SLASH
import dev.tommasop1804.kutils.STAR
import dev.tommasop1804.kutils.classes.builder.buildRegex
import dev.tommasop1804.kutils.unaryMinus
import dev.tommasop1804.kutils.validateInputFormat
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.net.URLConnection
import java.nio.file.Files
import java.nio.file.Path

/**
 * Represents a media type (MIME type) in the context of content negotiation, file type determination,
 * and data-processing applications.
 *
 * A MIME type encompasses a primary type and a subtype, separated by a slash (`/`), and is used to
 * indicate the format of data being transmitted or processed, such as in HTTP headers (`Content-Type`).
 *
 * The [MimeType] class encapsulates a MIME type in a type-safe manner, preventing misuse and
 * enforcing validation rules for valid MIME type formatting.
 *
 * This class implements the [CharSequence] interface, allowing the MIME type value to be
 * accessed and manipulated using standard string-like operations.
 *
 * Key features:
 * - Validation of MIME type format upon instantiation.
 * - Convenient access to primary type and subtype via the [type] and [subtype] properties.
 * - Integration with Kotlin's string handling APIs through the [CharSequence] implementation.
 *
 * Primary use cases include:
 * - Validation and comparison of MIME types in HTTP requests and responses.
 * - Simplification of MIME type management in file upload/download contexts.
 * - Usage in APIs requiring strongly-typed representations of media types.
 * @since 2.0.0
 */
@JvmInline
@Suppress("unused")
@JsonSerialize(using = MimeType.Companion.Serializer::class)
@JsonDeserialize(using = MimeType.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = MimeType.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = MimeType.Companion.OldDeserializer::class)
value class MimeType private constructor(val value: String) : CharSequence {

    /**
     * A computed property that extracts and returns the substring
     * of the `value` property occurring before the first '/' character.
     *
     * This property is typically used to determine a prefix-like
     * portion of the `value` string.
     *
     * @return The portion of the `value` string preceding the first '/' character.
     * @since 2.0.0
     */
    val type: String get() = value.substringBefore('/')
    /**
     * Retrieves the subtype portion of the MIME type represented by this instance.
     *
     * The MIME type is expected to follow the standard "type/subtype" structure.
     * This property extracts and returns the portion of the MIME type that appears after the forward slash (`/`).
     *
     * If the MIME type does not contain a forward slash, the entire value is returned.
     *
     * @return The subtype component of the MIME type.
     * @since 2.0.0
     */
    val subtype: String get() = value.substringAfter('/')
    /**
     * The length of the value represented as an integer.
     * This property returns the number of characters present
     * in the `value` string. It is computed dynamically.
     * @since 2.0.0
     */
    override val length: Int get() = value.length

    /**
     * Secondary constructor for the MimeType class that initializes the instance
     * using a given character sequence. The provided character sequence is transformed
     * into a lowercase string using a custom unary minus operator and then passed to the
     * primary constructor.
     *
     * @param value A character sequence representing the initial value for the MimeType.
     * @since 2.0.1
     */
    constructor(value: CharSequence) : this(-value.toString())
    /**
     * Constructs a MimeType instance using the specified `type` and `subtype`.
     *
     * This constructor internally combines the provided `type` and `subtype` into a single
     * MIME type string representation using the `of` method. The resulting string is
     * used to initialize the corresponding properties of the MimeType instance.
     *
     * @param type The primary type of the MIME type (e.g., "text", "application").
     * @param subtype The subtype of the MIME type (e.g., "plain", "json").
     * @throws IllegalArgumentException If either `type` or `subtype` is empty.
     * @since 2.0.0
     */
    constructor(type: String, subtype: String) : this(of(type, subtype))

    init {
        value.validateInputFormat(buildRegex {
            startOfGroup()
            literal("application").or()
            literal("audio").or()
            literal("example").or()
            literal("font").or()
            literal("haptics").or()
            literal("image").or()
            literal("message").or()
            literal("model").or()
            literal("multipart").or()
            literal("text").or()
            literal("video").or()
            char(Char.STAR)
            endOfGroup()
            char(Char.SLASH)
            anyChar().oneOrMore()
        }, MimeType::class)
    }

    companion object {
        /**
         * A predefined [MimeType] instance representing the "application/cbor" media type.
         *
         * CBOR (Concise Binary Object Representation) is a binary data serialization format designed
         * for small code size and small message size, making it suitable for constrained or
         * resource-limited environments.
         *
         * This constant provides a convenient way to reference the standard "application/cbor"
         * MIME type in applications that handle CBOR-encoded data.
         *
         * @since 2.0.0
         */
        val APPLICATION_CBOR = MimeType("application/cbor")
        /**
         * Represents the `application/json` MIME type.
         *
         * This constant encapsulates the MIME type for JSON content, commonly used in web APIs
         * for data serialization and communication. It specifies that the data being exchanged is
         * formatted in JSON (JavaScript Object Notation), which is a lightweight and human-readable
         * data-interchange format.
         * @since 2.0.0
         */
        val APPLICATION_JSON = MimeType("application/json")
        /**
         * Represents the MIME type `application/problem+json`, which is commonly used to convey
         * detailed error information in a standardized format in API responses.
         *
         * This MIME type adheres to the Problem Details for HTTP APIs specification, allowing
         * clients and servers to exchange structured problem details for error handling.
         * @since 2.0.0
         */
        val APPLICATION_PROBLEM_JSON = MimeType("application/problem+json")
        /**
         * Represents the MIME type for newline-delimited JSON (NDJSON) data streams.
         * NDJSON is a format where each line is a separate JSON object, allowing for
         * efficient processing of large or streaming data sets.
         *
         * The MIME type uses the `application/x-ndjson` specification.
         * @since 2.0.0
         */
        val APPLICATION_NDJSON = MimeType("application/x-ndjson")
        /**
         * Represents the MIME type for XML-formatted content.
         *
         * This constant is defined as "application/xml" and can be used to specify or compare
         * the content type of a resource, such as HTTP requests or responses, when dealing with
         * XML-based data.
         * @since 2.0.0
         */
        val APPLICATION_XML = MimeType("application/xml")
        /**
         * Represents the MIME type "application/atom+xml".
         *
         * This MIME type is used for Atom feeds, which are XML-based web syndication formats
         * designed for publishing and updating content such as blogs or news articles.
         * @since 2.0.0
         */
        val APPLICATION_ATOM_XML = MimeType("application/atom+xml")
        /**
         * Represents the MIME type `application/problem+xml`.
         * This MIME type is typically used for problem details and machine-readable error formats
         * as defined in the RFC 7807 specification.
         * @since 2.0.0
         */
        val APPLICATION_PROBLEM_XML = MimeType("application/problem+xml")
        /**
         * Represents the MIME type for RSS feeds in XML format.
         *
         * This MIME type is typically used for syndicating content such as news articles
         * or blog posts. It is recognized by applications capable of processing RSS feeds.
         * @since 2.0.0
         */
        val APPLICATION_RSS_XML = MimeType("application/rss+xml")
        /**
         * Represents the MIME type for XHTML documents, commonly identified as `application/xhtml+xml`.
         *
         * This MIME type is used to specify that the content being handled is an XHTML document, which
         * is an XML-based variant of HTML. It ensures the compatibility of web content and applications
         * that require strict XML parsing rules.
         * @since 2.0.0
         */
        val APPLICATION_XHTML_XML = MimeType("application/xhtml+xml")
        /**
         * Represents the MIME type for PDF documents.
         *
         * This constant is commonly used in contexts where the explicit representation
         * of the `application/pdf` MIME type is required, such as in HTTP headers,
         * file type validation, or content negotiation processes.
         *
         * MIME Type structure:
         * - Type: `application`
         * - Subtype: `pdf`
         * @since 2.0.0
         */
        val APPLICATION_PDF = MimeType("application/pdf")
        /**
         * Represents the MIME type for arbitrary binary data.
         *
         * The `application/octet-stream` MIME type is a generic binary stream type that is commonly
         * used for files or data of unknown or unspecified format. It indicates that the content
         * does not have a more specific MIME type and may require further processing or determination
         * based on application logic.
         *
         * This MIME type is often used:
         * - As a default when the format of the content is not recognized.
         * - To facilitate downloading of binary files without interpretation by the browser.
         * @since 2.0.0
         */
        val APPLICATION_OCTET_STREAM = MimeType("application/octet-stream")
        /**
         * Represents the MIME type for form submissions encoded as `application/x-www-form-urlencoded`.
         *
         * This MIME type is commonly used for submitting simple key-value pairs in HTTP requests,
         * such as during `application/x-www-form-urlencoded` form submissions in web forms.
         *
         * It ensures compatibility with standard web form processing and is widely supported
         * by web servers and browsers for encoding form data in HTTP requests.
         * @since 2.0.0
         */
        val APPLICATION_FORM_URLENCODED = MimeType("application/x-www-form-urlencoded")
        /**
         * Represents the MIME type for YAML data, defined as "application/yaml".
         *
         * This constant can be used to denote resources encoded in the YAML format,
         * which is widely used for configuration files and data serialization purposes.
         *
         * Usage of this MIME type helps in content negotiation, API communication, and
         * specifying the format of input or output data in web and application contexts.
         * @since 2.0.0
         */
        val APPLICATION_YAML = MimeType("application/yaml")
        /**
         * Represents the MIME type for ZIP file archives.
         *
         * This constant defines the "application/zip" MIME type, which is commonly used for files that
         * contain compressed archives in the ZIP file format. It can be utilized when handling, validating,
         * or serving ZIP files in a web or application context, ensuring consistent and accurate content type
         * identification.
         * @since 2.0.0
         */
        val APPLICATION_ZIP = MimeType("application/zip")
        /**
         * Represents the MIME type for a GraphQL response encoded in JSON format.
         * This MIME type is used to indicate that the content being handled
         * adheres to the "application/graphql-response+json" specification,
         * which is commonly used in GraphQL APIs to format server responses.
         * @since 2.0.0
         */
        val APPLICATION_GRAPHQL_RESPONSE = MimeType("application/graphql-response+json")
        /**
         * Represents the MIME type for Protocol Buffers data serialization format,
         * typically used for efficient storage and communication.
         *
         * The `APPLICATION_PROTOBUF` MIME type is registered as "application/x-protobuf".
         * It is commonly employed in systems leveraging Protocol Buffers for compact
         * and structured data exchange between services.
         * @since 2.0.0
         */
        val APPLICATION_PROTOBUF = MimeType("application/x-protobuf")

        /**
         * Represents the "text/plain" MIME type as a constant instance of the [MimeType] class.
         *
         * This MIME type is commonly used to indicate textual data that is encoded
         * in plain text format without any specific markup or type-specific encoding.
         *
         * Typical use cases include:
         * - Plain text responses in HTTP communication.
         * - Representation of files or data streams containing unformatted text.
         * - Default MIME type for unknown or generic text-based data.
         * @since 2.0.0
         */
        val TEXT_PLAIN = MimeType("text/plain")
        /**
         * Represents the MIME type for HTML content.
         *
         * This constant specifies the `text/html` MIME type, which is commonly used to indicate
         * that the content is an HTML document. It is typically associated with web pages rendered
         * in browsers.
         * @since 2.0.0
         */
        val TEXT_HTML = MimeType("text/html")
        /**
         * Represents the MIME type for CSS (Cascading Style Sheets).
         *
         * This constant facilitates the identification and handling of CSS content
         * in HTTP headers, request processing, or other scenarios requiring
         * content-type validation or manipulation.
         *
         * MIME type details:
         * - Type: `text`
         * - Subtype: `css`
         * @since 2.0.0
         */
        val TEXT_CSS = MimeType("text/css")
        /**
         * Represents the MIME type for CSV (Comma-Separated Values) text files.
         *
         * This MIME type is commonly used for data exchange and storage, especially for tabular data,
         * where fields are separated by commas and rows are delineated by newlines.
         *
         * The `text/csv` MIME type facilitates content negotiation and proper handling of CSV files
         * in HTTP headers, file uploads, and media type checks.
         * @since 2.0.0
         */
        val TEXT_CSV = MimeType("text/csv")
        /**
         * Represents the MIME type for XML content with a primary type of "text" and a subtype of "xml".
         *
         * This is commonly used to define the content type of an HTTP response or request body
         * when XML data is being transmitted.
         *
         * Examples of usage include specifying the `Content-Type` header in HTTP
         * requests and responses or determining the appropriate parser for XML-based payloads.
         * @since 2.0.0
         */
        val TEXT_XML = MimeType("text/xml")
        /**
         * A constant representing the MIME type for JavaScript files.
         *
         * This variable is used to denote the "text/javascript" MIME type,
         * which is the standard media type for JavaScript source code files.
         *
         * MIME types help identify the type of data a file contains and enable
         * appropriate handling by applications or services, such as web browsers
         * that load JavaScript code.
         * @since 2.0.0
         */
        val TEXT_JAVASCRIPT = MimeType("text/javascript")
        /**
         * Represents the MIME type for server-sent events, defined as `text/event-stream`.
         * This MIME type is used for streaming event data to clients in real-time, where
         * each event is represented as a line of text.
         * @since 2.0.0
         */
        val TEXT_EVENT_STREAM = MimeType("text/event-stream")
        /**
         * Represents the MIME type for Markdown text files.
         * This value is commonly used to indicate content in Markdown format,
         * which is a lightweight markup language for creating formatted text.
         * @since 2.0.0
         */
        val TEXT_MARKDOWN = MimeType("text/markdown")

        /**
         * Represents the MIME type for PNG images.
         *
         * This constant holds the value "image/png", which is the standard identifier
         * for files in the Portable Network Graphics (PNG) format. It is typically
         * used in contexts where a specific MIME type needs to be specified, such as
         * HTTP headers (e.g., `Content-Type` or `Accept`) or file type validation
         * in various applications.
         * @since 2.0.0
         */
        val IMAGE_PNG = MimeType("image/png")
        /**
         * Represents the MIME type for JPEG images.
         *
         * This constant defines the media type `image/jpeg` and can be used in contexts
         * where a MIME type is required, such as HTTP headers, file uploads, or content negotiation.
         * @since 2.0.0
         */
        val IMAGE_JPEG = MimeType("image/jpeg")
        /**
         * Represents the MIME type for GIF images.
         *
         * This constant specifies the media type `image/gif` used to identify files in the GIF format.
         * GIF is commonly used for images on the web that include animation or transparency.
         * @since 2.0.0
         */
        val IMAGE_GIF = MimeType("image/gif")
        /**
         * Represents the MIME type for SVG images, defined as "image/svg+xml".
         *
         * This constant is used to specify content type or to validate and match media types
         * in scenarios involving SVG (Scalable Vector Graphics) files.
         *
         * Conforming to the MIME type standard, this designation ensures compatibility and
         * proper handling of SVG files within web and other content management applications.
         * @since 2.0.0
         */
        val IMAGE_SVG = MimeType("image/svg+xml")
        /**
         * Represents the WebP image MIME type definition.
         *
         * Specifies the media type for WebP images, enabling accurate identification and handling
         * of image content in WebP format. WebP is a modern image format developed by Google
         * that provides superior lossless and lossy compression for web images.
         *
         * Commonly used in web applications and image processing to signal WebP content type.
         * @since 2.0.0
         */
        val IMAGE_WEBP = MimeType("image/webp")

        /**
         * Represents the MIME type "multipart/form-data", commonly used for encoding file uploads and
         * form submissions in HTTP requests.
         *
         * This MIME type allows the transmission of data consisting of multiple parts, where each
         * part can have different content types and headers. It is typically used when submitting
         * forms that include file uploads, enabling binary data and textual data to coexist in a
         * single request.
         *
         * Refer to the relevant HTTP standards for more information on how "multipart/form-data" is handled.
         * @since 2.0.0
         */
        val MULTIPART_FORM_DATA = MimeType("multipart/form-data")
        /**
         * Represents the MIME type "multipart/related".
         *
         * It is commonly used to encapsulate interrelated resources, such as an HTML
         * document with inline images or a set of related files, sent as part of a single
         * multipart message. This MIME type facilitates the inclusion of multiple
         * related parts in a single message while maintaining their association.
         * @since 2.0.0
         */
        val MULTIPART_RELATED = MimeType("multipart/related")
        /**
         * Represents the MIME type "multipart/mixed", typically used to indicate
         * a message that contains a combination of different content types in its payload.
         *
         * This is commonly employed in scenarios where multiple body parts of varying types
         * (e.g., plain text, binary data) need to be included within a single message
         * or document, such as email messages with attachments.
         * @since 2.0.0
         */
        val MULTIPART_MIXED = MimeType("multipart/mixed")

        /**
         * A constant representing a wildcard MIME type that matches any type and any subtype.
         * This is useful for scenarios where a generic or unspecified MIME type is required,
         * typically in HTTP content negotiation or when handling various media types without
         * knowing their exact specifications.
         * @since 2.0.0
         */
        val ANY = MimeType("*/*")

        /**
         * Attempts to resolve the MIME type from a given file extension.
         *
         * This method takes a file extension as input and performs several checks:
         * - It first looks up the extension in a pre-defined map of known extensions.
         * - If not found, it falls back to using the `URLConnection` API to determine the MIME type.
         * - Finally, it leverages `Files.probeContentType` to probe the file type based on the extension.
         *
         * @param extension The file extension (e.g., "txt", "jpg", "pdf"). The extension may include or exclude a leading dot.
         * @return An instance of [MimeType] representing the resolved MIME type if successful, or `null` if the type could not be determined.
         * @since 3.0.0
         */
        infix fun fromExtension(extension: String): MimeType? {
            val ext = (-extension).trimStart('.')
            knownExtensions[ext]?.let { return it }

            URLConnection.getFileNameMap().getContentTypeFor("file.$ext")
                ?.let { runCatching { MimeType(it) }.getOrNull() }
                ?.let { return it }

            runCatching {
                Files.probeContentType(Path.of("file.$ext"))
                    ?.let { MimeType(it) }
            }.getOrNull()?.let { return it }

            return null
        }

        private val knownExtensions: Map<String, MimeType> by lazy {
            buildMap {
                put("json",     APPLICATION_JSON)
                put("yaml",     APPLICATION_YAML)
                put("yml",      APPLICATION_YAML)
                put("xml",      APPLICATION_XML)
                put("pdf",      APPLICATION_PDF)
                put("zip",      APPLICATION_ZIP)
                put("cbor",     APPLICATION_CBOR)
                put("pb",       APPLICATION_PROTOBUF)
                put("proto",    APPLICATION_PROTOBUF)
                put("atom",     APPLICATION_ATOM_XML)
                put("rss",      APPLICATION_RSS_XML)
                put("txt",      TEXT_PLAIN)
                put("html",     TEXT_HTML)
                put("htm",      TEXT_HTML)
                put("css",      TEXT_CSS)
                put("csv",      TEXT_CSV)
                put("js",       TEXT_JAVASCRIPT)
                put("mjs",      TEXT_JAVASCRIPT)
                put("md",       TEXT_MARKDOWN)
                put("markdown", TEXT_MARKDOWN)
                put("png",      IMAGE_PNG)
                put("jpg",      IMAGE_JPEG)
                put("jpeg",     IMAGE_JPEG)
                put("gif",      IMAGE_GIF)
                put("svg",      IMAGE_SVG)
                put("webp",     IMAGE_WEBP)
            }
        }

        private fun of(type: String, subtype: String): String{
            validateInputFormat(type.isNotEmpty() && subtype.isNotEmpty()) { "Invalid MIME type: $type/$subtype" }
            return "$type/$subtype"
        }

        class Serializer : ValueSerializer<MimeType>() {
            override fun serialize(value: MimeType, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<MimeType>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): MimeType = MimeType(p.string)
        }

        class OldSerializer : JsonSerializer<MimeType>() {
            override fun serialize(value: MimeType, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.toString())
        }

        class OldDeserializer : JsonDeserializer<MimeType>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): MimeType = MimeType(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<MimeType?, String?> {
            override fun convertToDatabaseColumn(attribute: MimeType?): String? = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?): MimeType? = dbData?.let { MimeType(it) }
        }
    }

    /**
     * Compares this MimeType instance with another to determine if they match.
     * Two MimeType instances match if their `type` and `subtype` are either equal
     * or if one of them is a wildcard (`*`).
     *
     * @param other The MimeType instance to compare against.
     * @return `true` if the MimeType instances match, `false` otherwise.
     * @since 2.0.0
     */
    infix fun matches(other: MimeType): Boolean =
        (type == "*" || other.type == "*" || type == other.type) &&
                (subtype == "*" || other.subtype == "*" || subtype == other.subtype)

    /**
     * Returns a string representation of the object.
     *
     * @return the string representation of the object.
     * @since 2.0.0
     */
    override fun toString(): String = value

    /**
     * Retrieves the character at the specified index from the underlying value.
     *
     * @param index The position of the character to retrieve.
     * @return The character located at the given index.
     * @since 2.0.0
     */
    override fun get(index: Int): Char = value[index]

    /**
     * Returns a subsequence of this character sequence starting at the specified [startIndex] and ending
     * just before the specified [endIndex].
     *
     * @param startIndex the start index (inclusive) of the subsequence, must be non-negative.
     * @param endIndex the end index (exclusive) of the subsequence, must be greater than [startIndex].
     * @return a new [CharSequence] that is a subsequence of this character sequence.
     * @since 2.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = value.subSequence(startIndex, endIndex)

    /**
     * Converts the current MimeType instance into a MediaType instance.
     *
     * This method uses the properties of the MimeType instance, such as the
     * `type` and `subtype`, to create a corresponding MediaType representation.
     *
     * @return A new MediaType instance based on the current MimeType.
     * @since 2.0.0
     */
    fun toMediaType() = MediaType(this)
}