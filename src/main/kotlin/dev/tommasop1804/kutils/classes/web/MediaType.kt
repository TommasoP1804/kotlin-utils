/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.web

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.errors.*
import jakarta.activation.MimeType
import jakarta.persistence.AttributeConverter
import org.jetbrains.exposed.v1.core.Table
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.net.URLConnection
import java.nio.charset.Charset
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.reflect.typeOf

/**
 * Represents a media type, which consists of a MIME type and optional parameters.
 *
 * @property mimeType The MIME type component of the media type.
 * @property parameters Additional parameters associated with the media type, such as charset.
 * @since 2.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = MediaType.Companion.Serializer::class)
@JsonDeserialize(using = MediaType.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = MediaType.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = MediaType.Companion.OldDeserializer::class)
@Suppress("unused")
@MustUseReturnValues
data class MediaType(
    val type: String,
    val subType: String,
    val parameters: StringMap = emptyMap(),
) : CharSequence {
    /**
     * Retrieves the value of the "charset" parameter from the media type's parameter map.
     *
     * This property is a read-only accessor that returns the character encoding specified
     * in the media type, if present. If the "charset" parameter is not defined in the
     * media type's parameters, this property will return null.
     * @since 2.0.0
     */
    val charset: String? get() = parameters["charset"]
    /**
     * A computed property that retrieves the suffix of the `subType` component of this `MediaType` instance.
     *
     * The suffix is defined as the portion of the `subType` that appears after the last occurrence
     * of the `+` character. If no `+` character is present, the suffix will be an empty string.
     *
     * @return The subtype suffix extracted from the `subType` property.
     * @since 6.1.0
     */
    val subTypeSuffix: String get() = subType.substringAfterLast(Char.PLUS)
    /**
     * Computes the length of the string representation of the MediaType instance.
     *
     * The length is derived from the `toString` method, which formats the media type
     * as a string including its MIME type and associated parameters.
     * @since 2.0.0
     */
    override val length get() = toString().length


    /**
     * Constructs a new MediaType instance by parsing a string representation of a media type.
     *
     * This constructor uses the `parse` function to interpret the provided string,
     * which should contain a MIME type followed by optional parameters in key-value format.
     * If the string cannot be parsed into a valid MediaType, an exception will be thrown.
     *
     * @param string The string representation of the media type to parse, including optional parameters.
     * @throws Throwable If parsing fails, the encapsulated exception from the `Result` will be thrown.
     * @since 2.0.0
     */
    constructor(string: String) : this(parse(string)())

    private constructor(mediaType: MediaType) : this(mediaType.type, mediaType.subType, mediaType.parameters)

    companion object {
        /**
         * Represents the media type for CBOR (Concise Binary Object Representation) data as defined by the
         * `application/cbor` MIME type.
         *
         * This constant can be used when working with HTTP requests or responses to specify or identify
         * payloads encoded in CBOR format, which is a binary data serialization format designed for compactness
         * and efficiency.
         *
         * The `MediaType` instance is created using the predefined `application/cbor` MIME type string
         * from the `MimeType` enumeration or constants.
         * @since 2.0.0
         */
        val APPLICATION_CBOR = MediaType("application", "cbor")
        /**
         * Represents a predefined `MediaType` instance for the `application/json` MIME type.
         *
         * This constant is commonly used to indicate that the associated data is structured
         * in JSON (JavaScript Object Notation) format. It facilitates consistent handling
         * of requests and responses where `application/json` is involved.
         *
         * The `MediaType` contains `MimeType.APPLICATION_JSON` as its underlying MIME type,
         * which encapsulates the type and subtype (both set to `application` and `json`, respectively).
         * @since 2.0.0
         */
        val APPLICATION_JSON = MediaType("application", "json")
        /**
         * Represents the media type `application/json` with the charset parameter explicitly set to `UTF-8`.
         * This ensures that JSON content is encoded and interpreted using the UTF-8 character encoding.
         *
         * It is a specific instance of the `MediaType` class created by modifying the
         * `APPLICATION_JSON` media type to include the `charset=UTF-8` parameter.
         * @since 2.0.0
         */
        val APPLICATION_JSON_UTF8 = APPLICATION_JSON.withCharset("utf-8")
        /**
         * Represents the media type `application/problem+json` as defined by RFC 7807.
         *
         * This media type is commonly used to indicate problem details in HTTP APIs,
         * providing a machine-readable format for API error responses along with
         * human-readable explanations.
         * @since 2.0.0
         */
        val APPLICATION_PROBLEM_JSON = MediaType("application", "problem+json")
        /**
         * Represents the MIME type `application/merge-patch+json`.
         *
         * This MIME type is primarily used to describe JSON Merge Patch documents,
         * which are used for partial updates to JSON resources. It follows
         * the specification outlined in RFC 7396, providing a lightweight mechanism
         * for patching JSON documents by defining only the differences between the
         * original and the desired state.
         * @since 3.2.0
         */
        val APPLICATION_MERGE_PATCH_JSON = MediaType("application", "merge-patch+json")
        /**
         * Represents the MIME type `application/json-patch+json`.
         *
         * This MIME type is commonly used to indicate a JSON Patch document,
         * which defines a set of operations for modifying a JSON document.
         * It conforms to the standard defined in RFC 6902.
         * @since 3.2.0
         */
        val APPLICATION_JSON_PATCH_JSON = MediaType("application", "json-patch+json")
        /**
         * Represents the `application/x-ndjson` media type, commonly used for streaming newline-delimited JSON (NDJSON) data.
         * NDJSON is a format where each line contains a single JSON object, enabling efficient transmission of structured data.
         * @since 2.0.0
         */
        val APPLICATION_NDJSON = MediaType("application", "x-ndjson")
        /**
         * Represents the `application/xml` media type.
         *
         * This constant is a predefined instance of the `MediaType` class with
         * the MIME type set to `application/xml`. It is typically used to indicate
         * that the content being handled is in XML (Extensible Markup Language) format,
         * which is commonly used for structured data representation and transmission.
         * @since 2.0.0
         */
        val APPLICATION_XML = MediaType("application", "xml")
        /**
         * Represents the media type for Atom XML documents.
         *
         * This constant is preconfigured with the MIME type `application/xml`
         * and signifies the Atom syndication format, an XML-based document standard.
         *
         * Usage of this constant ensures consistency when working with
         * HTTP headers or APIs that require media type specification for Atom feeds.
         * @since 2.0.0
         */
        val APPLICATION_ATOM_XML = MediaType("application", "atom+xml")
        /**
         * Represents a media type for problems formatted as XML according to the "application/problem+xml" MIME type.
         *
         * This constant is commonly used in APIs to specify the content type of error or problem responses
         * that adhere to the RFC 7807 "Problem Details for HTTP APIs" standard using XML representation.
         * @since 2.0.0
         */
        val APPLICATION_PROBLEM_XML = MediaType("application", "problem+xml")
        /**
         * Represents the media type for RSS XML content.
         *
         * This constant holds the predefined MediaType instance for the MIME type `application/rss+xml`.
         * It can be used in contexts where RSS feeds are processed or transmitted, ensuring consistency
         * and accuracy in specifying the associated MIME type.
         * @since 2.0.0
         */
        val APPLICATION_RSS_XML = MediaType("application", "rss+xml")
        /**
         * Constant representing the media type for XHTML documents.
         *
         * This media type is used to specify content that adheres to the XHTML standard,
         * which is a reformulation of HTML as an XML application. The MIME type for XHTML is
         * defined as `application/xhtml+xml`.
         *
         * Instances of this media type are commonly used in web development and XML-based
         * document processing to indicate content that combines the flexibility of XML
         * with the semantic structure of HTML.
         * @since 2.0.0
         */
        val APPLICATION_XHTML_XML = MediaType("application", "xhtml+xml")
        /**
         * A predefined `MediaType` instance representing the MIME type `application/pdf`.
         *
         * This type is commonly used to indicate that the data being described is a PDF document.
         * It can be leveraged in content negotiation, media type validation, or in specifying
         * the desired format for request or response payloads in web applications.
         * @since 2.0.0
         */
        val APPLICATION_PDF = MediaType("application", "pdf")
        /**
         * Represents the media type for arbitrary binary data.
         *
         * This constant is commonly used to indicate that the data is not associated with
         * a specific media type and is treated as a binary stream. The MIME type for this
         * media type is `application/octet-stream`.
         * @since 2.0.0
         */
        val APPLICATION_OCTET_STREAM = MediaType("application", "octet-stream")
        /**
         * Represents the media type `application/x-www-form-urlencoded`.
         *
         * This media type is commonly used in form submissions where key-value pairs
         * are encoded as a query string and sent in the body of HTTP requests.
         *
         * The `APPLICATION_FORM_URLENCODED` constant is derived from the `MimeType.APPLICATION_FORM_URLENCODED`
         * and encapsulated in an instance of the `MediaType` class.
         * @since 2.0.0
         */
        val APPLICATION_FORM_URLENCODED = MediaType("application", "x-www-form-urlencoded")
        /**
         * Represents the media type associated with `application/yaml`.
         *
         * This constant specifies a media type for handling YAML documents,
         * commonly used in configuration files, data exchange, or serialization of objects.
         *
         * YAML (YAML Ain't Markup Language) is a human-readable data format that uses
         * indentation to represent data hierarchies. It is often used as an alternative
         * to JSON or XML due to its simplicity and readability.
         *
         * This instance is useful for working with HTTP content types or for specifying
         * the expected format for parsing or serializing YAML data.
         * @since 2.0.0
         */
        val APPLICATION_YAML = MediaType("application", "yaml")
        /**
         * Represents the media type for a GraphQL response.
         *
         * This constant is used to define the MIME type associated with
         * GraphQL responses, enabling consistent identification and handling
         * of GraphQL-specific content in HTTP transactions.
         * @since 2.0.0
         */
        val APPLICATION_GRAPHQL_RESPONSE = MediaType("application", "graphql-response+json")
        /**
         * A constant representing the media type for Protocol Buffers, defined as `application/protobuf`.
         * This is commonly used for specifying the MIME type of data serialized using Protocol Buffers.
         * @since 2.0.0
         */
        val APPLICATION_PROTOBUF = MediaType("application", "protobuf")
        /**
         * Represents the MIME type for ZIP file content.
         *
         * This constant is initialized with a `MediaType` instance that has the MIME type
         * `application/zip`, commonly used for compressed archive files in the ZIP format.
         * @since 2.0.0
         */
        val APPLICATION_ZIP = MediaType("application", "zip")

        /**
         * Represents the plain text media type MIME type `text/plain`.
         *
         * This is a predefined instance of the `MediaType` class initialized
         * with the MIME type corresponding to plain text content. It is often
         * used to specify or indicate content that contains unformatted human-readable text.
         * @since 2.0.0
         */
        val TEXT_PLAIN = MediaType("text", "plain")
        /**
         * Represents a `MediaType` instance for the "text/plain" MIME type with the character set explicitly
         * set to UTF-8. This is commonly used to specify plain text data encoded in UTF-8.
         * @since 2.0.0
         */
        val TEXT_PLAIN_UTF8 = TEXT_PLAIN.withCharset("UTF-8")
        /**
         * Represents the "text/html" media type.
         *
         * This variable defines a `MediaType` object with the MIME type of "text/html", commonly used
         * for representing HTML content in HTTP responses or other data exchanges.
         * @since 2.0.0
         */
        val TEXT_HTML = MediaType("text", "html")
        /**
         * Represents a MediaType for HTML content with a UTF-8 character encoding.
         *
         * This value is created by appending the `charset=UTF-8` parameter to the base MIME type `text/html`.
         * It ensures that the textual content is interpreted using the UTF-8 character encoding.
         * @since 2.0.0
         */
        val TEXT_HTML_UTF8 = TEXT_HTML.withCharset("UTF-8")
        /**
         * Represents the MIME type `text/css`.
         *
         * This constant is a predefined instance of the `MediaType` class, specifically for the `text/css` media type.
         * It is commonly used to identify Cascading Style Sheets (CSS) resources in HTTP requests and responses.
         *
         * Use this constant to avoid directly creating a new `MediaType` instance for `text/css`.
         *
         * @since 6.1.0
         */
        val TEXT_CSS = MediaType("text", "css")
        /**
         * Represents the `text/csv` media type as a predefined instance of [MediaType].
         *
         * This instance corresponds to the MIME type `text/csv`, commonly used for CSV
         * (Comma-Separated Values) file formats.
         *
         * @since 2.0.0
         */
        val TEXT_CSV = MediaType("text", "csv")
        /**
         * Represents the MIME type for JavaScript resources, defined as `text/javascript`.
         *
         * This predefined constant simplifies the usage of the `MediaType` class for operations involving
         * JavaScript-related content types within applications such as HTTP request or response handling,
         * content negotiation, or MIME type matching.
         *
         * The `type` is set to `text` and the `subtype` is set to `javascript`, adhering to the standard
         * MIME type format.
         *
         * @since 6.1.0
         */
        val TEXT_JAVASCRIPT = MediaType("text", "javascript")
        /**
         * Represents the `text/event-stream` media type.
         *
         * This media type is commonly used for server-sent events (SSE), where a client receives a
         * continuous stream of text-based updates from the server. It is typically utilized in real-time
         * applications for sending event-driven data updates.
         * @since 2.0.0
         */
        val TEXT_EVENT_STREAM = MediaType("text", "event-stream")
        /**
         * Represents a MediaType for textual content formatted in Markdown.
         *
         * The `TEXT_MARKDOWN` variable is a predefined instance of the `MediaType` class,
         * initialized with the `MimeType.TEXT_MARKDOWN` MIME type. This media type is
         * typically used to indicate that the content is written in Markdown format.
         * @since 2.0.0
         */
        val TEXT_MARKDOWN = MediaType("text", "markdown")
        /**
         * Represents the media type for XML content with a MIME type of "text/xml".
         *
         * This constant can be used to specify or check for content types related to XML data
         * in situations where the "text/xml" MIME type is applicable.
         * @since 2.0.0
         */
        val TEXT_XML = MediaType("text", "xml")

        /**
         * Represents the media type for PNG images. This constant is a predefined `MediaType`
         * instance that corresponds to the MIME type `image/png`.
         *
         * Use this for handling or specifying media types for PNG image data within
         * the application or APIs requiring standardized MIME type definitions.
         * @since 2.0.0
         */
        val IMAGE_PNG = MediaType("image", "png")
        /**
         * Predefined `MediaType` instance representing the MIME type for JPEG image files.
         *
         * This constant is primarily used to denote media types with a MIME type of `image/jpeg`.
         * It is commonly associated with JPEG image content when working with HTTP headers or
         * media-type-based processing.
         *
         * The instance encapsulates the `image/jpeg` MIME type using the `MediaType` class.
         * @since 2.0.0
         */
        val IMAGE_JPEG = MediaType("image", "jpeg")
        /**
         * Represents the media type for the GIF image format.
         *
         * This constant encapsulates the MIME type `image/gif`, which is used to denote
         * files encoded in the Graphics Interchange Format (GIF). It is commonly used for
         * animated or static image content in web and multimedia applications.
         *
         * @since 6.1.0
         */
        val IMAGE_GIF = MediaType("image", "gif")
        /**
         * Represents the `image/svg+xml` media type, commonly used for Scalable Vector Graphics (SVG) files.
         *
         * This constant defines a specific instance of the `MediaType` class with the type `image`
         * and the subtype `svg`. It is utilized to clearly denote the SVG file format in contexts
         * where media type identification is required.
         *
         * @since 6.1.0
         */
        val IMAGE_SVG = MediaType("image", "svg")
        /**
         * Represents the "image/webp" media type.
         *
         * This constant is an instance of the `MediaType` class, with "image" as the primary type
         * and "webp" as the subtype. It is commonly used to specify WebP image formats in HTTP
         * headers or other data processing contexts.
         *
         * @since 6.1.0
         */
        val IMAGE_WEBP = MediaType("image", "webp")

        /**
         * Represents the MIME type for `multipart/form-data`, commonly used in HTTP
         * requests to handle file uploads and form data submissions.
         *
         * The `multipart/form-data` media type indicates that the body of the
         * request contains multiple parts, each with its own headers and content,
         * typically used for scenarios where files and form fields need to be
         * transmitted together.
         *
         * This constant is defined as an instance of the `MediaType` class
         * with the `MimeType.MULTIPART_FORM_DATA` value.
         * @since 2.0.0
         */
        val MULTIPART_FORM_DATA = MediaType("multipart", "form-data")
        /**
         * Represents the MIME type `multipart/related`.
         *
         * This media type is used to specify a compound document consisting of multiple related
         * parts, typically used for scenarios where separate parts, such as a primary document
         * and its accompanying resources, are transmitted together.
         *
         * Commonly applied in contexts like email attachments or SOAP with attachments.
         * @since 2.0.0
         */
        val MULTIPART_RELATED = MediaType("multipart", "related")
        /**
         * Represents the `multipart/mixed` media type, commonly used to encapsulate multiple body parts
         * within a single request or response, where each part can have its own content type.
         * This media type is often used in email messages and other multipart contexts.
         *
         * This instance is defined as a constant for convenience and denotes the MIME type `multipart/mixed`.
         * @since 2.0.0
         */
        val MULTIPART_MIXED = MediaType("multipart", "mixed")

        /**
         * Represents a media type with a MIME type that matches any type and subtype.
         *
         * This predefined instance can be used when a generic or wildcard MIME type is needed,
         * accommodating all possible media types. It serves as a convenient constant for situations
         * where no specific MIME type is required.
         * @since 2.0.0
         */
        val ANY = MediaType("*", "*")

        private val KNOWN_EXTENSIONS: Map<String, MediaType> by lazy {
            buildMap {
                put("json", APPLICATION_JSON)
                put("yaml", APPLICATION_YAML)
                put("yml", APPLICATION_YAML)
                put("xml", APPLICATION_XML)
                put("pdf", APPLICATION_PDF)
                put("zip", APPLICATION_ZIP)
                put("cbor", APPLICATION_CBOR)
                put("pb", APPLICATION_PROTOBUF)
                put("proto", APPLICATION_PROTOBUF)
                put("atom", APPLICATION_ATOM_XML)
                put("rss", APPLICATION_RSS_XML)
                put("txt", TEXT_PLAIN)
                put("html", TEXT_HTML)
                put("htm", TEXT_HTML)
                put("css", TEXT_CSS)
                put("csv", TEXT_CSV)
                put("js", TEXT_JAVASCRIPT)
                put("mjs", TEXT_JAVASCRIPT)
                put("md", TEXT_MARKDOWN)
                put("markdown", TEXT_MARKDOWN)
                put("png", IMAGE_PNG)
                put("jpg", IMAGE_JPEG)
                put("jpeg", IMAGE_JPEG)
                put("gif", IMAGE_GIF)
                put("svg", IMAGE_SVG)
                put("webp", IMAGE_WEBP)
            }
        }

        /**
         * Parses a given string value into a `MediaType` instance or returns an `InvalidFormatOfType`
         * if the input format is invalid.
         *
         * The parsing process splits the input string by semicolons (`;`) to extract the MIME type
         * and optional parameters. The first part of the string represents the MIME type, while
         * subsequent parts are treated as key-value parameter pairs, separated by an equals sign (`=`).
         *
         * @param value The input string to be parsed, expected to represent a media type and optional
         *              parameters in the format "type/subtype; key1=value1; key2=value2".
         * @return An `Either` containing:
         *         - A `MediaType` instance if the input string is correctly formatted.
         *         - An `InvalidFormatOfType` error if the input string is not in a valid media type format.
         * @since 6.1.0
         */
        infix fun parse(value: String): Either<InvalidFormatOfType, MediaType> = either { catching({
            val parts = value.split(';').map { it.trim() }
            val params = parts.drop(1).associate { param ->
                val [k, v] = param.split('=', limit = 2)
                k.trim() to v.trim()
            }
            parts.first().splitAndTrim("/").let { MediaType(
                it.first().validateNotEmpty(),
                it.second().validateNotEmpty(),
                params
            ) }
        }) { t: Throwable -> InvalidFormatOfType(value, typeOf<MediaType>(), t) } }

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
         * @since 6.1.0
         */
        infix fun fromExtension(extension: String): MediaType? {
            val ext = (-extension).trimStart('.')
            KNOWN_EXTENSIONS[ext]?.let { return it }

            URLConnection.getFileNameMap().getContentTypeFor("file.$ext")
                ?.let { runCatching { MediaType(it) }.getOrNull() }
                ?.let { return it }

            runCatching {
                Files.probeContentType(Path("file.$ext"))
                    ?.let { MediaType(it) }
            }.getOrNull()?.let { return it }

            return null
        }

        class Serializer : ValueSerializer<MediaType>() {
            override fun serialize(value: MediaType, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<MediaType>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): MediaType = MediaType(p.string)
        }

        class OldSerializer : JsonSerializer<MediaType>() {
            override fun serialize(value: MediaType, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.toString())
        }

        class OldDeserializer : JsonDeserializer<MediaType>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): MediaType = MediaType(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<MediaType?, String?> {
            override fun convertToDatabaseColumn(attribute: MediaType?): String? = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?): MediaType? = dbData?.let { MediaType(it) }
        }

        /**
         * Adds a column to the table for storing media type information as a string,
         * with optional maximum length specification.
         *
         * @param name The name of the column to be added to the table.
         * @param length The maximum length of the column's string value. Defaults to 255.
         * @since 5.5.0
         */
        fun Table.mediaType(name: String, length: Int = 255) = varchar(name, length)
            .transform(::MediaType, MediaType::toString)
    }

    /**
     * Creates a new MediaType instance by adding or updating the charset parameter.
     *
     * @param charset The charset value to be associated with the MediaType instance.
     * @return A new MediaType instance with the specified charset parameter added or updated.
     * @since 2.0.0
     */
    infix fun withCharset(charset: String): MediaType =
        copy(parameters = parameters + ("charset" to charset))
    /**
     * Adds or updates the charset parameter to the MediaType instance.
     *
     * This method creates a new MediaType instance with the specified
     * charset parameter included or updated.
     *
     * @param charset The Charset to be associated with the MediaType instance.
     * @return A new MediaType instance with the specified charset parameter.
     * @since 2.0.1
     */
    infix fun withCharset(charset: Charset): MediaType =
        copy(parameters = parameters + ("charset" to -charset.name()))

    /**
     * Adds a parameter to the current MediaType instance and returns a new MediaType instance with the updated parameters.
     *
     * @param key The key of the parameter to be added.
     * @param value The value of the parameter to be added.
     * @return A new MediaType instance with the specified parameter added.
     * @since 2.0.0
     */
    fun withParameter(key: String, value: String): MediaType =
        copy(parameters = parameters + (key to value))

    /**
     * Compares this MediaType instance with another to determine if they match.
     * Two MediaType instances match if their MIME types are considered compatible based on
     * their respective `type` and `subtype` components, or if either of them contains a wildcard (`*`).
     *
     * @param other The MediaType instance to compare against.
     * @return `true` if the MediaType instances match, `false` otherwise.
     * @since 2.0.0
     */
    infix fun matches(other: MediaType): Boolean =
        (type == "*" || other.type == "*" || type == other.type) &&
            (subType == "*" || other.subType == "*" || subType == other.subType)

    /**
     * Converts the media type to its string representation.
     *
     * The resulting string includes the MIME type and its associated parameters in
     * the format `mimeType; key1=value1; key2=value2`.
     *
     * @return A string representation of the media type, including its MIME type
     * and parameters.
     * @since 2.0.0
     */
    override fun toString(): String = buildString {
        append("$type/$subType")
        parameters.forEach { [k, v] -> append("; $k=$v") }
    }

    /**
     * Retrieves the character at the specified index from the MediaType's string representation.
     *
     * @param index The zero-based index of the character to retrieve.
     * @return The character at the specified index.
     * @since 2.0.0
     */
    override fun get(index: Int): Char = toString()[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence.
     *
     * @param startIndex The start index (inclusive) of the subsequence.
     * @param endIndex The end index (exclusive) of the subsequence.
     * @return A new character sequence that contains the characters from the specified range.
     * @since 2.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = toString().subSequence(startIndex, endIndex)

    /**
     * Compares this MediaType instance with another object for equality.
     *
     * Two MediaType instances are considered equal if:
     * - They reference the same object in memory, or
     * - They have the same MIME type and parameters.
     *
     * @param other The object to compare with this MediaType instance.
     * @return `true` if the objects are equal, `false` otherwise.
     * @since 2.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MediaType) return false
        if (type != other.type) return false
        if (subType != other.subType) return false
        if (!equalsParameters(other)) return false
        return true
    }

    /**
     * Computes a hash code for this MediaType instance.
     *
     * The hash code is calculated based on the `type`, `subType`, and `parameters` properties of the MediaType.
     * This ensures that two MediaType instances with the same `type`, `subType`, and `parameters` will produce
     * the same hash code.
     *
     * @return The hash code value for the MediaType instance.
     * @since 6.1.0
     */
    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + subType.hashCode()
        result = 31 * result + parameters.hashCode()
        return result
    }

    /**
     * Compares the parameters of this MediaType instance with those of another MediaType instance.
     *
     * The method checks for equality in the parameters of the two MediaType instances.
     * Parameters are compared in a case-insensitive manner for both keys and values.
     *
     * @param other The MediaType instance whose parameters are to be compared with this instance.
     * @return `true` if the parameters of both MediaType instances are equal, `false` otherwise.
     * @since 2.0.1
     */
    infix fun equalsParameters(other: MediaType): Boolean {
        if (this === other) return true
        if (parameters.mapToMap { -it.key to -it.value } != other.parameters.mapToMap { -it.key to -it.value }) return false
        return true
    }

    /**
     * Compares the `type` and `subType` of this `MediaType` instance with another `MediaType` instance
     * to determine if they are equal.
     *
     * @param other The `MediaType` instance to compare against.
     * @return `true` if both the `type` and `subType` of the two `MediaType` instances are equal, `false` otherwise.
     * @since 6.1.0
     */
    infix fun equalTypesAndSubTypes(other: MediaType): Boolean = type == other.type && subType == other.subType

    /**
     * Compares the `type` and `subTypeSuffix` of this `MediaType` instance with another `MediaType`
     * instance to determine if they are equal.
     *
     * @param other The `MediaType` instance to compare against.
     * @return `true` if both the `type` and `subTypeSuffix` of the two `MediaType` instances are equal, `false` otherwise.
     * @since 6.1.0
     */
    infix fun equalsTypeAndSubTypeSuffixes(other: MediaType): Boolean = type == other.type && subTypeSuffix == other.subTypeSuffix
}