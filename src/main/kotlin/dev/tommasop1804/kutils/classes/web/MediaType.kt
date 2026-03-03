package dev.tommasop1804.kutils.classes.web

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.StringMap
import dev.tommasop1804.kutils.invoke
import dev.tommasop1804.kutils.mapToMap
import dev.tommasop1804.kutils.unaryMinus
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.nio.charset.Charset

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
data class MediaType(
    val mimeType: MimeType,
    val parameters: StringMap = emptyMap(),
) : CharSequence {
    /**
     * Provides the type component of the MIME media type.
     * The type represents the primary classification of the media type
     * (e.g., "text", "image", "application").
     *
     * This property is derived from the `mimeType` field of the containing class.
     * @since 2.0.0
     */
    val type: String get() = mimeType.type
    /**
     * The subtype component of the MIME type associated with this media type.
     * Represents the secondary identifier in a MIME type, which follows the primary type and is separated by a forward slash.
     * For example, in the MIME type "text/plain", "plain" is the subtype.
     * @since 2.0.0
     */
    val subtype: String get() = mimeType.subtype
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
     * Computes the length of the string representation of the MediaType instance.
     *
     * The length is derived from the `toString` method, which formats the media type
     * as a string including its MIME type and associated parameters.
     * @since 2.0.0
     */
    override val length get() = toString().length

    /**
     * Constructs a new MediaType instance using the provided type, subtype, and optional parameters.
     *
     * @param type The primary type of the media type (e.g., "application", "text").
     * @param subtype The specific subtype of the media type (e.g., "json", "plain").
     * @param parameters An optional map of parameter key-value pairs associated with the media type.
     * @since 2.0.0
     */
    constructor(type: String, subtype: String, parameters: StringMap = emptyMap()) : this(MimeType(type, subtype), parameters)
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
    private constructor(mediaType: MediaType) : this(mediaType.mimeType, mediaType.parameters)

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
        val APPLICATION_CBOR = MediaType(MimeType.APPLICATION_CBOR)
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
        val APPLICATION_JSON = MediaType(MimeType.APPLICATION_JSON)
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
        val APPLICATION_PROBLEM_JSON = MediaType(MimeType.APPLICATION_PROBLEM_JSON)
        /**
         * Represents the `application/x-ndjson` media type, commonly used for streaming newline-delimited JSON (NDJSON) data.
         * NDJSON is a format where each line contains a single JSON object, enabling efficient transmission of structured data.
         * @since 2.0.0
         */
        val APPLICATION_NDJSON = MediaType(MimeType.APPLICATION_NDJSON)
        /**
         * Represents the `application/xml` media type.
         *
         * This constant is a predefined instance of the `MediaType` class with
         * the MIME type set to `application/xml`. It is typically used to indicate
         * that the content being handled is in XML (Extensible Markup Language) format,
         * which is commonly used for structured data representation and transmission.
         * @since 2.0.0
         */
        val APPLICATION_XML = MediaType(MimeType.APPLICATION_XML)
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
        val APPLICATION_ATOM_XML = MediaType(MimeType.APPLICATION_XML)
        /**
         * Represents a media type for problems formatted as XML according to the "application/problem+xml" MIME type.
         *
         * This constant is commonly used in APIs to specify the content type of error or problem responses
         * that adhere to the RFC 7807 "Problem Details for HTTP APIs" standard using XML representation.
         * @since 2.0.0
         */
        val APPLICATION_PROBLEM_XML = MediaType(MimeType.APPLICATION_PROBLEM_XML)
        /**
         * Represents the media type for RSS XML content.
         *
         * This constant holds the predefined MediaType instance for the MIME type `application/rss+xml`.
         * It can be used in contexts where RSS feeds are processed or transmitted, ensuring consistency
         * and accuracy in specifying the associated MIME type.
         * @since 2.0.0
         */
        val APPLICATION_RSS_XML = MediaType(MimeType.APPLICATION_RSS_XML)
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
        val APPLICATION_XHTML_XML = MediaType(MimeType.APPLICATION_XHTML_XML)
        /**
         * A predefined `MediaType` instance representing the MIME type `application/pdf`.
         *
         * This type is commonly used to indicate that the data being described is a PDF document.
         * It can be leveraged in content negotiation, media type validation, or in specifying
         * the desired format for request or response payloads in web applications.
         * @since 2.0.0
         */
        val APPLICATION_PDF = MediaType(MimeType.APPLICATION_PDF)
        /**
         * Represents the media type for arbitrary binary data.
         *
         * This constant is commonly used to indicate that the data is not associated with
         * a specific media type and is treated as a binary stream. The MIME type for this
         * media type is `application/octet-stream`.
         * @since 2.0.0
         */
        val APPLICATION_OCTET_STREAM = MediaType(MimeType.APPLICATION_OCTET_STREAM)
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
        val APPLICATION_FORM_URLENCODED = MediaType(MimeType.APPLICATION_FORM_URLENCODED)
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
        val APPLICATION_YAML = MediaType(MimeType.APPLICATION_YAML)
        /**
         * Represents the media type for a GraphQL response.
         *
         * This constant is used to define the MIME type associated with
         * GraphQL responses, enabling consistent identification and handling
         * of GraphQL-specific content in HTTP transactions.
         * @since 2.0.0
         */
        val APPLICATION_GRAPHQL_RESPONSE = MediaType(MimeType.APPLICATION_GRAPHQL_RESPONSE)
        /**
         * A constant representing the media type for Protocol Buffers, defined as `application/protobuf`.
         * This is commonly used for specifying the MIME type of data serialized using Protocol Buffers.
         * @since 2.0.0
         */
        val APPLICATION_PROTOBUF = MediaType(MimeType.APPLICATION_PROTOBUF)
        /**
         * Represents the MIME type for ZIP file content.
         *
         * This constant is initialized with a `MediaType` instance that has the MIME type
         * `application/zip`, commonly used for compressed archive files in the ZIP format.
         * @since 2.0.0
         */
        val APPLICATION_ZIP = MediaType(MimeType.APPLICATION_ZIP)

        /**
         * Represents the plain text media type MIME type `text/plain`.
         *
         * This is a predefined instance of the `MediaType` class initialized
         * with the MIME type corresponding to plain text content. It is often
         * used to specify or indicate content that contains unformatted human-readable text.
         * @since 2.0.0
         */
        val TEXT_PLAIN = MediaType(MimeType.TEXT_PLAIN)
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
        val TEXT_HTML = MediaType(MimeType.TEXT_HTML)
        /**
         * Represents a MediaType for HTML content with a UTF-8 character encoding.
         *
         * This value is created by appending the `charset=UTF-8` parameter to the base MIME type `text/html`.
         * It ensures that the textual content is interpreted using the UTF-8 character encoding.
         * @since 2.0.0
         */
        val TEXT_HTML_UTF8 = TEXT_HTML.withCharset("UTF-8")
        /**
         * Represents the `text/csv` media type as a predefined instance of [MediaType].
         *
         * This instance corresponds to the MIME type `text/csv`, commonly used for CSV
         * (Comma-Separated Values) file formats.
         *
         * @since 2.0.0
         */
        val TEXT_CSV = MediaType(MimeType.TEXT_CSV)
        /**
         * Represents the `text/event-stream` media type.
         *
         * This media type is commonly used for server-sent events (SSE), where a client receives a
         * continuous stream of text-based updates from the server. It is typically utilized in real-time
         * applications for sending event-driven data updates.
         * @since 2.0.0
         */
        val TEXT_EVENT_STREAM = MediaType(MimeType.TEXT_EVENT_STREAM)
        /**
         * Represents a MediaType for textual content formatted in Markdown.
         *
         * The `TEXT_MARKDOWN` variable is a predefined instance of the `MediaType` class,
         * initialized with the `MimeType.TEXT_MARKDOWN` MIME type. This media type is
         * typically used to indicate that the content is written in Markdown format.
         * @since 2.0.0
         */
        val TEXT_MARKDOWN = MediaType(MimeType.TEXT_MARKDOWN)
        /**
         * Represents the media type for XML content with a MIME type of "text/xml".
         *
         * This constant can be used to specify or check for content types related to XML data
         * in situations where the "text/xml" MIME type is applicable.
         * @since 2.0.0
         */
        val TEXT_XML = MediaType(MimeType.TEXT_XML)

        /**
         * Represents the media type for PNG images. This constant is a predefined `MediaType`
         * instance that corresponds to the MIME type `image/png`.
         *
         * Use this for handling or specifying media types for PNG image data within
         * the application or APIs requiring standardized MIME type definitions.
         * @since 2.0.0
         */
        val IMAGE_PNG = MediaType(MimeType.IMAGE_PNG)
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
        val IMAGE_JPEG = MediaType(MimeType.IMAGE_JPEG)

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
        val MULTIPART_FORM_DATA = MediaType(MimeType.MULTIPART_FORM_DATA)
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
        val MULTIPART_RELATED = MediaType(MimeType.MULTIPART_RELATED)
        /**
         * Represents the `multipart/mixed` media type, commonly used to encapsulate multiple body parts
         * within a single request or response, where each part can have its own content type.
         * This media type is often used in email messages and other multipart contexts.
         *
         * This instance is defined as a constant for convenience and denotes the MIME type `multipart/mixed`.
         * @since 2.0.0
         */
        val MULTIPART_MIXED = MediaType(MimeType.MULTIPART_MIXED)

        /**
         * Represents a media type with a MIME type that matches any type and subtype.
         *
         * This predefined instance can be used when a generic or wildcard MIME type is needed,
         * accommodating all possible media types. It serves as a convenient constant for situations
         * where no specific MIME type is required.
         * @since 2.0.0
         */
        val ANY = MediaType(MimeType.ANY)

        /**
         * Parses a string representation of a media type into a MediaType instance.
         *
         * The input string should consist of a MIME type followed by optional parameters
         * separated by semicolons. Parameters should be in key-value format, with each key
         * and value separated by an equals sign (`=`).
         *
         * @param value The string to be parsed, representing the media type and its parameters.
         * @return A Result containing the MediaType instance if parsing succeeds, or an exception if parsing fails.
         * @since 2.0.0
         */
        fun parse(value: String) = runCatching {
            val parts = value.split(';').map { it.trim() }
            val mimeType = MimeType(parts.first())
            val params = (-1)(parts).associate { param ->
                val (k, v) = param.split('=', limit = 2)
                k.trim() to v.trim()
            }
            MediaType(mimeType, params)
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
    infix fun matches(other: MediaType): Boolean = mimeType.matches(other.mimeType)

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
        append(mimeType)
        parameters.forEach { (k, v) -> append("; $k=$v") }
    }

    /**
     * Retrieves the character at the specified index from the MediaType's string representation.
     *
     * @param index The zero-based index of the character to retrieve.
     * @return The character at the specified index.
     * @since 2.0.0
     */
    override fun get(index: Int): Char = this[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence.
     *
     * @param startIndex The start index (inclusive) of the subsequence.
     * @param endIndex The end index (exclusive) of the subsequence.
     * @return A new character sequence that contains the characters from the specified range.
     * @since 2.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = this.substring(startIndex, endIndex)

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
        if (mimeType != other.mimeType) return false
        if (!equalsParameters(other)) return false
        return true
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
    fun equalsParameters(other: MediaType): Boolean {
        if (this === other) return true
        if (parameters.mapToMap { -it.key to -it.value } != other.parameters.mapToMap { -it.key to -it.value }) return false
        return true
    }

    /**
     * Computes the hash code for this MediaType instance.
     *
     * The hash code is calculated based on the `mimeType`, `parameters`, `type`, `subtype`, `charset`, and `length` properties.
     * It ensures that equal instances of MediaType generate the same hash code, conforming to the contract of `hashCode` in Kotlin.
     *
     * @return The computed hash code as an integer.
     */
    override fun hashCode(): Int {
        var result = mimeType.hashCode()
        result = 31 * result + parameters.hashCode()
        result = 31 * result + length
        result = 31 * result + type.hashCode()
        result = 31 * result + subtype.hashCode()
        result = 31 * result + (charset?.hashCode() ?: 0)
        return result
    }
}