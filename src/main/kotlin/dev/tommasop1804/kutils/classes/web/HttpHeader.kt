/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:MustUseReturnValues

package dev.tommasop1804.kutils.classes.web

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.Json.Companion.toJson
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.classes.measure.*
import dev.tommasop1804.kutils.classes.measure.MeasureUnit.DataSizeUnit.Companion.BYTES
import dev.tommasop1804.kutils.classes.measure.RMeasurement.Companion.ofUnit
import dev.tommasop1804.kutils.classes.security.*
import dev.tommasop1804.kutils.classes.security.Jwt.Companion.toJwt
import dev.tommasop1804.kutils.classes.time.*
import dev.tommasop1804.kutils.classes.time.Duration.Companion.asSecondsOfDuration
import dev.tommasop1804.kutils.classes.time.TimeZone
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCEPT
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCEPT_CHARSET
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCEPT_LANGUAGE
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCEPT_PATCH
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCEPT_RANGES
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCESS_CONTROL_ALLOW_CREDENTIALS
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCESS_CONTROL_ALLOW_METHODS
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCESS_CONTROL_MAX_AGE
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCESS_CONTROL_REQUEST_METHOD
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ALLOW
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.AUTHORIZATION
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.CONNECTION
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.CONTENT_LANGUAGE
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.CONTENT_LENGTH
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.CONTENT_TYPE
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.DATE
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.EXPIRES
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.HOST
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.IF_MODIFIED_SINCE
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.IF_UNMODIFIED_SINCE
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.LAST_MODIFIED
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.LOCATION
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ORIGIN
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.PRIORITY
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.REFERER
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.headerDateToInstant
import dev.tommasop1804.kutils.errors.*
import dev.tommasop1804.kutils.exceptions.*
import jakarta.persistence.AttributeConverter
import org.jetbrains.exposed.v1.core.Table
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.net.InetSocketAddress
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.ZoneOffset.UTC
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.Locale.LanguageRange
import kotlin.reflect.typeOf
import kotlin.text.Charsets.ISO_8859_1

/**
 * Represents an HTTP header consisting of a name and associated values, implemented as a key-value pair
 * where the key is the header name, and the value is a [List<String>] of associated values.
 *
 * Common use cases include managing, processing, and serializing HTTP headers for requests and responses.
 *
 * @property name The name of the HTTP header.
 * @property values The associated list of values for the HTTP header.
 * @since 2.1.0
 */
@Suppress("unused", "JavaDefaultMethodsNotOverriddenByDelegation")
@JsonSerialize(using = HttpHeader.Companion.Serializer::class)
@JsonDeserialize(using = HttpHeader.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = HttpHeader.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = HttpHeader.Companion.OldDeserializer::class)
class HttpHeader(val name: String, values: Iterable<Any>) : List<String> by values.map(Any::toString) {
    /**
     * A transformed list containing string representations of the elements
     * from the original `values` collection. Each element in the original
     * collection is converted to its string equivalent using the `toString` method.
     *
     * @since 2.1.0
     */
    val values: List<String> = values.map(Any::toString)

    /**
     * Indicates whether the current HTTP header represents an authentication-related header.
     *
     * This property evaluates to `true` if the `name` of the header matches one of the 
     * predefined authentication-related header names, such as `Authorization`, 
     * `WWW-Authenticate`, `Proxy-Authenticate`, or `Proxy-Authorization`.
     *
     * It is useful in scenarios where specific handling is required for headers 
     * associated with authentication mechanisms in HTTP communication.
     *
     * @since 2.1.0
     */
    val isAutHeader
        get() = name in setOf(AUTHORIZATION, WWW_AUTHENTICATE, PROXY_AUTHENTICATE, PROXY_AUTHORIZATION)
    /**
     * Indicates whether the HTTP header's name starts with the prefix "Content-", 
     * ignoring case sensitivity.
     *
     * Useful for categorizing or filtering headers related to content, such as 
     * "Content-Type" or "Content-Length".
     *
     * @since 2.1.0
     */
    val isContentHeader
        get() = name startsWithIgnoreCase "Content-"
    /**
     * Determines if the current HTTP header belongs to the set of caching-related headers.
     *
     * This property evaluates whether the header name exists in a predefined set of cache-control 
     * and caching-related headers such as `Cache-Control`, `ETag`, `If-Match`, `If-None-Match`, 
     * `If-Modified-Since`, `If-Unmodified-Since`, `If-Range`, `Last-Modified`, `Expires`, `Pragma`, `Age`, or `Vary`.
     *
     * @since 2.1.0
     */
    val isCacheHeader
        get() = name in setOf(CACHE_CONTROL, ETAG, IF_MATCH, IF_NONE_MATCH, IF_MODIFIED_SINCE, IF_UNMODIFIED_SINCE, IF_RANGE, LAST_MODIFIED, EXPIRES, PRAGMA, AGE, VARY)
    /**
     * Indicates whether the current header is categorized as a request context header.
     *
     * A request context header is typically used to convey metadata about the
     * context of the HTTP request, such as client preferences or origin details. 
     * This property will return `true` if the header name matches one of the 
     * predefined headers: `ACCEPT`, `ACCEPT_CHARSET`, `ACCEPT_ENCODING`,
     * `ACCEPT_LANGUAGE`, `ACCEPT_RANGES`, `HOST`, `REFERER`, `ORIGIN`, `PRIORITY` or
     * `USER_AGENT`.
     *
     * @since 2.1.0
     */
    val isRequestContextHeader
        get() = name in setOf(ACCEPT, ACCEPT_CHARSET, ACCEPT_ENCODING, ACCEPT_LANGUAGE, ACCEPT_RANGES, HOST, REFERER, ORIGIN, USER_AGENT, PRIORITY)
    /**
     * Indicates whether the HTTP header name corresponds to a CORS (Cross-Origin Resource Sharing) header.
     *
     * A CORS header is identified by its name starting with the prefix "Access-Control-", irrespective of case sensitivity.
     * Such headers are typically used to manage cross-origin requests between different domains.
     *
     * @since 2.1.0
     */
    val isCorsHeader
        get() = name startsWithIgnoreCase "Access-Control-"
    /**
     * Indicates whether the current HTTP header is classified as a security-related header.
     *
     * Security headers are a subset of HTTP headers that provide guidance to the browser 
     * about security-related actions. These headers aim to mitigate certain attack vectors 
     * and protect the client from common web vulnerabilities, such as cross-site scripting (XSS),
     * man-in-the-middle attacks, and clickjacking.
     *
     * This property evaluates to `true` if the header `name` matches one of the predefined 
     * security header constants:
     * - `STRICT_TRANSPORT_SECURITY`
     * - `X_CONTENT_TYPE_OPTIONS`
     * - `X_FRAME_OPTIONS`
     * - `X_XSS_PROTECTION`
     * - `CONTENT_SECURITY_POLICY`
     * - `REFERRER_POLICY`
     *
     * @since 2.1.0
     */
    val isSecurityHeader
        get() = name in setOf(STRICT_TRANSPORT_SECURITY, X_CONTENT_TYPE_OPTIONS, X_FRAME_OPTIONS, X_XSS_PROTECTION, CONTENT_SECURITY_POLICY, REFERRER_POLICY)
    /**
     * Indicates whether this header corresponds to a "Set-Cookie" or "Cookie" header.
     *
     * This property checks if the `name` of the current HTTP header is equal to 
     * either "Set-Cookie" or "Cookie". It is commonly used to identify headers 
     * that are related to managing cookies in an HTTP context.
     *
     * @since 2.1.0
     */
    val isCookieHeader
        get() = name in setOf(COOKIE, SET_COOKIE)

    /**
     * Constructs an HttpHeader instance using a Pair containing the header name and its corresponding values.
     * The `first` element of the Pair is assigned to the header name, and the `second` element is assigned
     * to the header values.
     *
     * @param pair A Pair where the `first` represents the header name and the `second` represents the list of header values.
     * @since 2.1.0
     */
    constructor(pair: Pair<String, Any>) : this(from(pair))
    /**
     * Secondary constructor for the HttpHeader class that initializes
     * the object using an existing Map.Entry instance.
     *
     * @param entry The Map.Entry containing a header name as the key and
     *              a List<String> representing the header values as the value.
     * @since 2.1.0
     */
    constructor(entry: Map.Entry<String, Any>) : this(from(entry))

    /**
     * Initializes the HttpHeader instance with the specified name and value.
     * The provided value is converted into a singleton list before being
     * assigned to the header's value field.
     *
     * @param name The name of the HTTP header.
     * @param values The value of the HTTP header, which will be converted
     * into a singleton list.
     * @since 2.1.0
     */
    constructor(name: String, vararg values: Any) : this(name, values.map(Any::toString).toList())

    /**
     * Constructs an instance by parsing a notation string into components.
     * The input string is expected to contain two parts separated by a colon (`:`).
     * The first part is used as the first parameter, and the second part is split by commas (`,`),
     * forming the second parameter as a list.
     *
     * @param notation A string in the format "part1:part2,part3,part4,...".
     *
     * @since 2.1.0
     */
    constructor(notation: String) : this(
        notation.splitAndTrim(Char.COLON, limit = 2).first(),
        notation.splitAndTrim(Char.COLON, limit = 2).second() / Char.COMMA
    )

    private constructor(header: HttpHeader) : this(header.name, header.values)

    init {
        validate(name.isNotBlank()) { "Header name cannot be blank" }
        name.validateInputFormat(Regex("^[a-zA-Z0-9!#$%&'*+\\-.^_`|~]+$"), "Header name must be valid")

        validate(values.toList().isNotEmpty()) { "Header value cannot be empty" }
        values.map(Any::toString).forEach { it.validateInputFormat(Regex("^[\\x21-\\x7E\\x80-\\xFF\\t ]*$"), "Header value must be valid") }
    }

    companion object {
        // Authentication
        const val AUTHORIZATION = "Authorization"
        const val WWW_AUTHENTICATE = "WWW-Authenticate"
        const val PROXY_AUTHENTICATE = "Proxy-Authenticate"
        const val PROXY_AUTHORIZATION = "Proxy-Authorization"

        // Content
        const val CONTENT_TYPE = "Content-Type"
        const val CONTENT_LENGTH = "Content-Length"
        const val CONTENT_DISPOSITION = "Content-Disposition"
        const val CONTENT_ENCODING = "Content-Encoding"
        const val CONTENT_LANGUAGE = "Content-Language"
        const val CONTENT_LOCATION = "Content-Location"
        const val CONTENT_RANGE = "Content-Range"

        // Caching
        const val CACHE_CONTROL = "Cache-Control"
        const val ETAG = "ETag"
        const val IF_MATCH = "If-Match"
        const val IF_NONE_MATCH = "If-None-Match"
        const val IF_MODIFIED_SINCE = "If-Modified-Since"
        const val IF_UNMODIFIED_SINCE = "If-Unmodified-Since"
        const val IF_RANGE = "If-Range"
        const val LAST_MODIFIED = "Last-Modified"
        const val EXPIRES = "Expires"
        const val PRAGMA = "Pragma"
        const val AGE = "Age"
        const val VARY = "Vary"

        // Request context
        const val ACCEPT = "Accept"
        const val ACCEPT_CHARSET = "Accept-Charset"
        const val ACCEPT_ENCODING = "Accept-Encoding"
        const val ACCEPT_LANGUAGE = "Accept-Language"
        const val ACCEPT_PATCH = "Accept-Patch"
        const val ACCEPT_RANGES = "Accept-Ranges"
        /** [RFC 10008 - The Accept-Query Header Field](https://datatracker.ietf.org/doc/html/rfc10008#name-the-accept-query-header-fie) */
        const val ACCEPT_QUERY = "Accept-Query"
        const val HOST = "Host"
        const val REFERER = "Referer"
        const val ORIGIN = "Origin"
        const val PRIORITY = "Priority"
        const val USER_AGENT = "User-Agent"

        // CORS
        const val ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin"
        const val ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods"
        const val ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers"
        const val ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials"
        const val ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers"
        const val ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age"
        const val ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method"
        const val ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers"

        // Redirects & connection
        const val LOCATION = "Location"
        const val CONNECTION = "Connection"
        const val UPGRADE = "Upgrade"
        const val RETRY_AFTER = "Retry-After"
        const val TRANSFER_ENCODING = "Transfer-Encoding"

        // Security
        const val STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security"
        const val X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options"
        const val X_FRAME_OPTIONS = "X-Frame-Options"
        const val X_XSS_PROTECTION = "X-XSS-Protection"
        const val CONTENT_SECURITY_POLICY = "Content-Security-Policy"
        const val REFERRER_POLICY = "Referrer-Policy"

        // Cookies
        const val COOKIE = "Cookie"
        const val SET_COOKIE = "Set-Cookie"

        const val ALLOW = "Allow"
        const val DATE = "Date"
        const val EXPECT = "Expect"
        const val FROM = "From"
        const val LINK = "Link"
        const val MAX_FORWARDS = "Max-Forwards"
        const val RANGE = "Range"
        const val SERVER = "Server"
        const val TE = "TE"
        const val TRAILER = "Trailer"
        const val VIA = "Via"
        const val WARNING = "Warning"
        const val PREFER = "Prefer"

        /**
         * Computes a unique hash-based identifier (eTag) for the current object.
         *
         * The eTag is generated by converting the object to its JSON representation
         * and then applying an MD5 hash algorithm to produce a hexadecimal string.
         * This identifier can be used for caching, validation, and resource versioning purposes.
         * @since 2.1.0
         */
        val Any.eTag
            get() = toJson().value hashingToString HashingAlgorithm.MD5

        @Suppress("UNCHECKED_CAST")
        private fun from(pair: Pair<String, Any>) = when(pair.second) {
            is Iterable<*> -> HttpHeader(pair.first, (pair.second as Iterable<Any>))
            else -> HttpHeader(pair.first, pair.second)
        }
        @Suppress("UNCHECKED_CAST")
        private fun from(entry: Map.Entry<String, Any>) = when(entry.value) {
            is Iterable<*> -> HttpHeader(entry.key, (entry.value as Iterable<Any>))
            else -> HttpHeader(entry.key, entry.value)
        }

        /**
         * Converts a string representation of a date in HTTP header format to an [Instant].
         * The method attempts to parse the string using a standard ISO date-time validator,
         * or falls back to parsing the string as per common HTTP header date formats,
         * such as RFC 7231 and other conventional formats.
         *
         * @return Either an [InvalidTypeFormat] error indicating the string could not
         *         be parsed into an [Instant], or the resulting [Instant] representation of the date.
         * @since 6.1.0
         */
        fun String.headerDateToInstant(): Either<InvalidTypeFormat, Instant> = either {
            tryOr({
                tryOrRaise({ InvalidTypeFormat(this, typeOf<Instant>(), it) }) {
                    if (ISO_DATE_TIME_STANDARD_VALIDATOR(this)) return@tryOrRaise Instant(this)
                    val splitted = this / Char.SPACE
                    val day = splitted[1].toInt()
                    val month = when (splitted[2]) {
                        "Jan" -> 1
                        "Feb" -> 2
                        "Mar" -> 3
                        "Apr" -> 4
                        "May" -> 5
                        "Jun" -> 6
                        "Jul" -> 7
                        "Aug" -> 8
                        "Sep" -> 9
                        "Oct" -> 10
                        "Nov" -> 11
                        "Dec" -> 12
                        else -> throw IllegalArgumentException("Invalid month format")
                    }
                    val year = splitted[3].toInt()
                    val time = LocalTime(splitted[4])()
                    LocalDateTime(LocalDate(year, month, day), time).toInstant((TimeZone of splitted[5]).firstOrNull()?.offset ?: UTC)
                }
            }) { Instant.from(RFC_7231_DATE_TIME_FORMATTER.parse(this)) } }

        /**
         * Converts the invoking [TemporalAccessor] instance, such as a date-time object, into a string
         * formatted according to the RFC 7231 date time standard.
         *
         * This method is commonly used in scenarios requiring date-time values suitable for HTTP headers
         * and similar use cases.
         *
         * @return A string representation of the date-time in the RFC 1123 date time format.
         * @since 2.1.0
         */
        fun TemporalAccessor.toHeaderDate(): String = RFC_7231_DATE_TIME_FORMATTER.format(this)

        /**
         * Converts the current Pair of header name and corresponding list of header values into an `HttpHeader` instance.
         *
         * The `first` element of the Pair is mapped to the header name, and the `second` element is mapped to the header values.
         *
         * @receiver A Pair where the `first` represents the header name and the `second` represents the list of header values.
         * @return An `HttpHeader` instance constructed using the given*/
        fun Pair<String, List<String>>.toHttpHeader() = HttpHeader(this)
        /**
         * Converts the current String2 instance into an HttpHeader object.
         * The first element of the String2 represents the header name, and the second
         * element represents the header value, which is transformed into an appropriate
         * structure for the HttpHeader class.
         *
         * @receiver The String2 instance containing the header name and its value.
         * @return An HttpHeader instance constructed using the receiver String2 object.
         * @*/
        @JvmName("pairToHttpHeaderAny")
        fun Pair<String, Any>.toHttpHeader() = HttpHeader(first, second.toString().asSingleList())
        /**
         * Converts the current Map.Entry instance into an HttpHeader object.
         * The Map.Entry key represents the header name, and the List<String> value
         * represents the associated header values.
         *
         * This method leverages the secondary constructor of the HttpHeader class to
         * initialize an HttpHeader object with the key-value pair provided in the Map.Entry.
         *
         * @receiver The Map.Entry containing a key as the header name and a List<String>
         */
        fun Map.Entry<String, List<String>>.toHttpHeader() = HttpHeader(this)
        /**
         * Converts the current map entry into an instance of `HttpHeader`.
         * The map entry's key is treated as the header name, and its value is used as the header value.
         * This provides an easy way to transform key-value pairs into `HttpHeader` objects.
         *
         * @receiver The map entry representing a header name and its associated value.
         * @return An `HttpHeader` instance derived from the map entry.
         * @since 2.1.0
         */
        @JvmName("mapEntryToHttpHeaderAny")
        fun Map.Entry<String, Any>.toHttpHeader() = HttpHeader(key, value.toString().asSingleList())

        /**
         * Creates an HTTP header using the provided key-value pair.
         *
         * @param value The value associated with the header key. It can be `null`, an `Iterable`, or any other object.
         * @since 3.6.6
         */
        @Suppress("UNCHECKED_CAST")
        infix fun String.headerTo(value: Any?) = when (value) {
            null -> HttpHeader(this, "null")
            is Iterable<*> -> HttpHeader(this, value as Iterable<Any>)
            else -> HttpHeader(this, value)
        }

        class Serializer : ValueSerializer<HttpHeader>() {
            override fun serialize(value: HttpHeader, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<HttpHeader>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = HttpHeader(p.string)
        }

        class OldSerializer : JsonSerializer<HttpHeader>() {
            override fun serialize(value: HttpHeader, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.toString())
        }

        class OldDeserializer : JsonDeserializer<HttpHeader>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): HttpHeader = HttpHeader(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<HttpHeader?, String?> {
            override fun convertToDatabaseColumn(attribute: HttpHeader?): String? = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?): HttpHeader? = dbData?.let { HttpHeader(it) }
        }

        /**
         * Defines an HTTP header column in the table with a specified name and length.
         *
         * @param name The name of the HTTP header column.
         * @param length The maximum length of the HTTP header value. Defaults to 255.
         * @since 5.5.0
         */
        fun Table.httpHeader(name: String, length: Int = 255) = varchar(name, length)
            .transform(::HttpHeader, HttpHeader::toString)
    }

    /**
     * Converts the HttpHeader object into a Pair representation where the key is the header name
     * and the value is the associated list of header values.
     *
     * @return A Pair consisting of the header name and its corresponding values.
     * @since 2.1.0
     */
    fun toPair(): Pair<String, List<String>> = name to values
    /**
     * Converts the current `HttpHeader` instance into a typed representation that deserializes
     * all the values into the specified type [T], wrapped in an `Either` type.
     *
     * This function attempts to deserialize each value in the `values` collection to the type [T].
     * If all elements are successfully deserialized, it returns a `Pair` where the first element
     * is the header name and the second element is the list of deserialized values. In case of a
     * deserialization error, it returns a `DeserializationError`.
     *
     * @return An `Either` object containing either a `DeserializationError` if deserialization fails,
     *         or a `Pair` of the header name and a list of deserialized values as the success result.
     * @since 6.1.0
     */
    inline fun <reified T> toTypedPair(): Either<DeserializationError, Pair<String, List<T>>> =
        values.traverse { it.deserialize<T>() }.map { name to it }
    /**
     * Converts the current HttpHeader instance into a Map.Entry representation,
     * where the key is the header name and the value is the associated List<String>.
     *
     * @return A Map.Entry consisting of the header name as the key and List<String> as the value.
     * @since 2.1.0
     */
    fun toMapEntry(): Map.Entry<String, List<String>> = toPair().toMapEntry()
    /**
     * Converts a deserialization result into a typed map entry.
     *
     * This function attempts to deserialize data into a `Map.Entry` with a key of type `String`
     * and a value of type `List<T>`. If the deserialization succeeds, the result is wrapped in
     * an `Either.Right`. If the deserialization fails, the error is wrapped in an `Either.Left`.
     *
     * @return An `Either` containing either a `DeserializationError` in case of failure
     *         or a typed `Map.Entry<String, List<T>>` on success.
     * @since 6.1.0
     */
    inline fun <reified T> toTypedMapEntry(): Either<DeserializationError, Map.Entry<String, List<T>>> =
        toTypedPair<T>().map { it.toMapEntry() }

    /**
     * Converts the current `HttpHeader` instance into an `HttpHeaders` object.
     *
     * This method facilitates the transformation of a single `HttpHeader` instance
     * into a more standard `HttpHeaders` representation, which can be useful for
     * further processing or integration with APIs that require an `HttpHeaders` type.
     *
     * @return A new `HttpHeaders` instance created from the current `HttpHeader`.
     * @since 3.0.0
     */
    fun toHttpHeaders() = HttpHeaders(this)

    /**
     * Retrieves the deserialized list of values from the current `HttpHeader` instance
     * as the specified type [T], wrapped in an `Either` type.
     *
     * This function utilizes `toTypedPair` to perform the deserialization process
     * and extracts only the second element of the resulting `Pair`, which is the
     * list of successfully deserialized values.
     *
     * @return An `Either` object containing either a `DeserializationError` if
     *         deserialization fails, or a `List<T>` of deserialized values
     *         as the success result.
     * @since 6.1.0
     */
    inline fun <reified T> typedValues(): Either<DeserializationError, List<T>> = toTypedPair<T>().map { it.second }

    /**
     * Creates a new `HttpHeader` instance with the specified name while retaining the current values.
     *
     * @param name The name to assign to the new `HttpHeader` instance.
     * @return A new `HttpHeader` instance with the specified name and the current values.
     * @since 2.1.0
     */
    fun withName(name: String) = HttpHeader(name, values)
    /**
     * Creates a new instance of `HttpHeader` with the specified value while retaining the current header name.
     *
     * @param value The value to be associated with the HTTP header.
     * @since 2.1.0
     */
    fun withValue(value: String) = HttpHeader(name, value)

    /**
     * Returns a string representation of the `HttpHeader` instance.
     *
     * The string is constructed by combining the header's `name` field and its 
     * associated `values` collection, formatted as a comma-separated list.
     *
     * @return A string in the format "name: value1, value2, ...".
     * @since 2.1.0
     */
    override fun toString() = "$name: ${values.joinToString()}"

    /**
     * Compares this `HttpHeader` instance with another object for equality.
     *
     * The comparison checks if the `name` and `values` properties of the current instance
     * are equal to those of the specified object. If the specified object is not of type
     * `HttpHeader`, the method returns `false`.
     *
     * @param other The object to compare with the current `HttpHeader` instance.
     * @return `true` if the specified object is equal to the current instance; otherwise, `false`.
     * @since 2.1.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as HttpHeader

        if (name != other.name) return false
        if (values != other.values) return false

        return true
    }

    override fun hashCode() = name.hashCode() * 31 + values.hashCode()
    
    /**
     * Compares the name of the current HttpHeader instance with the name of another HttpHeader instance.
     *
     * This method checks if the `name` property of the current instance is equal to the `name`
     * property of the specified `HttpHeader`.
     *
     * @param other The HttpHeader instance to compare with the current instance.
     * @return `true` if the names are equal; otherwise, `false`.
     * @since 2.1.0
     */
    fun nameEquals(other: HttpHeader) = name == other.name
    /**
     * Compares the header's name with a given string to check for equality.
     *
     * @param other The string to compare with the header's name.
     * @return `true` if the header's name is equal to the given string, `false` otherwise.
     * @since 2.1.0
     */
    fun nameEquals(other: String) = name == other
    /**
     * Compares the `values` of the current `HttpHeader` instance with the `values` of another `HttpHeader`.
     *
     * @param other The `HttpHeader` instance to compare against.
     * @return `true` if the `values` of both instances are equal, `false` otherwise.
     * @since 2.1.0
     */
    fun valueEquals(other: HttpHeader) = values == other.values
    /**
     * Compares the current header values to the specified object.
     *
     * This method converts the provided `other` object to a string representation,
     * wraps it into a singleton list, and checks if it is equal to the `values` collection
     * of the current `HttpHeader` instance.
     *
     * @param other The object to compare against the current header values.
     * @since 2.1.0
     */
    fun valueEquals(other: Any) = values == other.toString().asSingleList()
    /**
     * Compares the `values` collection of the current `HttpHeader` instance to the content
     * of another `List<String>` instance to check for equality.
     *
     * The comparison is performed by converting the given `List<String>` to a single list
     * and then comparing it to the `values` collection of the `HttpHeader` instance.
     *
     * @param other The `List<String>` instance to compare against the `values` collection.
     * @since 2.1.0
     */
    fun valueEquals(other: List<String>) = values == other.asSingleList()

    /**
     * Operator function `component1` for destructuring declarations.
     * Returns the `name` property of the object.
     *
     * @return The `name` property value.
     * @since 3.1.0
     */
    operator fun component1() = name
    /**
     * Provides the first component of a data structure for destructuring declarations.
     *
     * This operator function is used to retrieve and return the `values` property 
     * from the current object when performing destructuring declarations. The precise 
     * nature of `values` depends on the implementation within the class or data type.
     *
     * @return The `values` property of the object.
     * @since 3.1.0
     */
    operator fun component2() = values
}

/**
 * Represents a collection of HTTP headers, allowing for various operations
 * such as retrieving, deserializing, and manipulating header values.
 *
 * @author Tommaso Pastorelli
 * @since 2.1.0
 */
@Suppress("unused")
@JsonSerialize(using = HttpHeaders.Companion.Serializer::class)
@JsonDeserialize(using = HttpHeaders.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = HttpHeaders.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = HttpHeaders.Companion.OldDeserializer::class)
class HttpHeaders private constructor(private val headers: MSet<HttpHeader>) : MultiStringMap {
    /**
     * Represents the size of the collection based on the number of elements 
     * within the `headers` object.
     *
     * This property is overridden to provide access to the current size 
     * of the `headers` collection.
     *
     * @since 2.1.0
     */
    override val size: Int 
        get() = headers.size
    
    /**
     * A set of string keys derived from the HTTP headers' names.
     * This property overrides the base class implementation to provide
     * a collection of unique header names as keys.
     *
     * @since 2.1.0
     */
    override val keys: Set<String>
        get() = headers.map(HttpHeader::name).toSet()
    /**
     * A collection of `List<String>` objects representing the values of all headers 
     * contained in the `HttpHeaders` instance.
     *
     * This property is derived by mapping each header's `values` through the `headers` collection.
     * It enables access to all header values without the need to work directly with individual headers.
     *
     * @since 2.1.0
     */
    override val values: Collection<List<String>>
        get() = headers.map(HttpHeader::values)
    /**
     * A set containing all the entries in the `HttpHeaders` collection, where each entry maps 
     * a header name to its associated `List<String>` values.
     *
     * The entries are derived by mapping the underlying `headers` collection into a set of 
     * key-value pairs using the `toMapEntry` extension function of `HttpHeader`.
     *
     * @since 2.1.0
     */
    override val entries: Set<Map.Entry<String, List<String>>>
        get() = headers.map(HttpHeader::toMapEntry).toSet()

    constructor() : this(emptyMSet())

    /**
     * Constructs an instance of the class by converting the provided collection of HTTP headers
     * into a mutable set and delegating to another constructor.
     *
     * @param headers A collection of HTTP headers to be converted into a mutable set.
     * @since 2.2.2
     */
    constructor(headers: Collection<HttpHeader>) : this(headers.toMSet())

    /**
     * Constructs an `HttpHeaders` instance containing the provided HTTP headers.
     *
     * This constructor allows initializing the `HttpHeaders` object with a variable number of
     * `HttpHeader` arguments. The headers passed to this constructor are converted into a list
     * and used to populate the internal collection of headers.
     *
     * @param headers A variable number of `HttpHeader` instances to be included in the collection.
     * @since 2.1.0
     */
    constructor(vararg headers: HttpHeader) : this(headers.toMSet())
    
    /**
     * Constructs an `HttpHeaders` instance using a single `HttpHeader` object.
     *
     * This constructor is useful for initializing an `HttpHeaders` object when you have a
     * single `HttpHeader` instance and want to encapsulate it into an `HttpHeaders` collection.
     *
     * The `HttpHeader` instance is internally converted into a list of headers to match
     * the structure expected by the `HttpHeaders` class.
     *
     * @param header The `HttpHeader` instance to initialize this `HttpHeaders` instance with.
     * @since 2.1.0
     */
    constructor(header: HttpHeader) : this(header.asSingleMSet())

    /**
     * Secondary constructor that initializes an instance using a variable number of
     * key-value pairs. Each pair represents a header with the key as the header
     * name and the value as the header's corresponding value. The pairs are converted
     * into a list of `HttpHeader` objects and passed to the primary constructor.
     *
     * @param pairs Vararg parameter of key-value pairs where the key is the header
     * name and the value is the associated header value.
     * @since 2.2.2
     */
    constructor(vararg pairs: Pair<String, Any>) : this(pairs.map(::HttpHeader))

    /**
     * Constructs an instance of `HttpHeaders` by wrapping the provided header name and value
     * into an `HttpHeader` instance.
     *
     * This constructor simplifies the creation of `HttpHeaders` objects by accepting a header
     * name and its associated values as parameters, instead of requiring the caller to
     * explicitly create an `HttpHeader` object.
     *
     * @param name The name of the HTTP header to include in the collection.
     * @param values The values associated with the header name, encapsulated in a `List<String>`.
     * @since 2.1.0
     */
    constructor(name: String, values: Iterable<Any>) : this(HttpHeader(name, values))
    /**
     * Constructs an `HttpHeaders` instance by creating an `HttpHeader` object
     * with the specified name and value.
     *
     * This constructor serves as a shorthand for initializing an `HttpHeaders` object
     * with a single `HttpHeader` with the given name and value. The `HttpHeader` is created
     * internally using the provided parameters.
     *
     * @param name The name of the HTTP header.
     * @param values The value of the HTTP header.
     * @since 2.1.0
     */
    constructor(name: String, vararg values: Any) : this(HttpHeader(name, values.map(Any::toString).toList()))

    /**
     * Constructs an instance using the provided HTTP header notation string.
     *
     * @param notation A string representing the HTTP header in a specific notation.
     * @since 2.1.0
     */
    constructor(notation: String) : this(HttpHeader(notation))

    /**
     * Secondary constructor for creating an instance from a map of header names to their values.
     * Converts the input map into a collection of `HttpHeader` objects and initializes the primary constructor.
     *
     * @param map A map where keys are header names and values are iterables of corresponding header values.
     * @since 3.0.0
     */
    constructor(map: Map<String, Iterable<Any>>) : this(map.map { HttpHeader(it.key, it.value) }.toMSet())

    companion object {
        /**
         * Converts the `MultiStringMap` instance into an `HttpHeaders` object.
         *
         * This method transforms each key-value pair in the `MultiStringMap` into
         * an `HttpHeader` instance and aggregates them into an `HttpHeaders` collection.
         *
         * @return An `HttpHeaders` object representing the current `MultiStringMap` instance.
         * @since 2.1.0
         */
        fun MultiStringMap.toHttpHeaders(): HttpHeaders = HttpHeaders(map { HttpHeader(it.key, it.value) }.toMSet())
        /**
         * Converts the current `DataMapNN` instance to an `HttpHeaders` object.
         *
         * This method maps each key-value pair from the `DataMapNN` instance
         * to an `HttpHeader` and constructs an `HttpHeaders` collection encapsulating
         * all derived `HttpHeader` objects.
         *
         * @return An `HttpHeaders` instance containing the headers constructed
         * from the entries of the current `DataMapNN`.
         * @since 2.1.0
         */
        @JvmName("dataMapNNToHttpHeaders")
        fun DataMapNN.toHttpHeaders(): HttpHeaders = HttpHeaders(map { HttpHeader(it.key, it.value) }.toMSet())

        /**
         * Converts a `List` of `HttpHeader` instances into an `HttpHeaders` instance.
         *
         * This function provides a convenient way to construct an `HttpHeaders` object
         * directly from a `List` of individual `HttpHeader` instances. The resulting
         * `HttpHeaders` object represents all the headers contained in the source list.
         *
         * @receiver A list of `HttpHeader` instances to be converted.
         * @return An `HttpHeaders` instance initialized with the headers from the source list.
         * @since 2.1.0
         */
        fun Iterable<HttpHeader>.toHttpHeaders() = HttpHeaders(toMSet())

        class Serializer : ValueSerializer<HttpHeaders>() {
            override fun serialize(value: HttpHeaders, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                for (header in value.headers) {
                    gen.writeArrayPropertyStart(header.name)
                    for (v in header.values)
                        gen.writeString(v)
                    gen.writeEndArray()
                }
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<HttpHeaders>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = p.readValueAs(MultiStringMap::class.java).toHttpHeaders()
        }

        class OldSerializer : JsonSerializer<HttpHeaders>() {
            override fun serialize(value: HttpHeaders, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                for (header in value.headers) {
                    gen.writeFieldName(header.name)
                    gen.writeStartArray()
                    for (v in header.values)
                        gen.writeString(v)
                    gen.writeEndArray()
                }
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<HttpHeaders>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext) = p.readValueAs(MultiStringMap::class.java).toHttpHeaders()
        }

        /**
         * Maps a column in the table to a JSONB type representing HTTP headers.
         *
         * @param name The name of the column to be mapped.
         * @since 5.5.0
         */
        fun Table.httpHeaders(name: String) = jsonb<HttpHeaders>(name)
    }

    /**
     * Checks if the collection of headers is empty.
     *
     * This method determines whether the headers collection contains no elements.
     *
     * @return `true` if the headers collection is empty, otherwise `false`.
     * @since 2.2.1
     */
    override fun isEmpty() = headers.isEmpty()

    /**
     * Checks whether the collection of headers is not empty.
     *
     * This method verifies if there is at least one element present in the `headers` collection.
     *
     * @return `true` if the `headers` collection contains one or more elements, `false` otherwise.
     * @since 2.2.1
     */
    fun isNotEmpty() = headers.isNotEmpty()

    /**
     * Checks whether a header with the specified name exists in the collection.
     *
     * @param key The name of the header to search for.
     * @return `true` if a header with the specified name exists, `false` otherwise.
     * @since 2.1.0
     */
    override fun containsKey(key: String) = headers.any { it.nameEquals(key) }

    /**
     * Checks whether any `HttpHeader` instance contained in the `HttpHeaders` object
     * has values equal to the specified `List<String>`.
     *
     * The comparison is performed using the `valueEquals` method of each `HttpHeader` instance,
     * which compares the `values` collection*/
    override fun containsValue(value: List<String>) = headers.any { it.valueEquals(value) }

    /**
     * Retrieves the values associated with the specified key from the headers.
     *
     * @param key The key to search for in the headers.
     * @return A list of values associated with the given key, or `null` if the key is not found.
     * @since 2.1.0
     */
    override fun get(key: String): List<String>? = headers.find { it.nameEquals(key) }?.values
    /**
     * Retrieves the value associated with the specified key or throws an exception if the key is not present.
     *
     * @param key The key whose associated value is to be retrieved.
     * @param lazyException A lambda function that supplies the exception to be thrown if the key is not found.
     * Defaults to throwing a `NoSuchHeaderException` initialized with the provided key.
     * @return The value associated with the specified key, if present.
     * @throws Throwable The exception provided by the `lazyException` supplier if the key is not found.
     * @since 3.0.0
     */
    fun getOrThrow(key: String, lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) =
        get(key) ?: throw lazyException()
    /**
     * Attempts to retrieve a value associated with the given key. If the key does not exist,
     * an error of type IterableError.NotFound is returned.
     *
     * @param key The key to retrieve the associated value for.
     * @return Either a List of Strings associated with the key or an IterableError.NotFound error.
     * @since 6.1.0
     */
    fun getOrError(key: String): Either<IterableError.NotFound, List<String>> =
        either { get(key).orRaise { IterableError.NotFound(key) } }

    /**
     * Retrieves the value associated with the specified key from the headers
     * or returns an empty string if the key is not found.
     *
     * @param key The key to search for in the headers.
     * @return The value associated with the given key, or an empty string if the key is not found.
     * @since 2.1.0
     */
    fun getOrEmpty(key: String) = get(key).orEmpty()

    /**
     * Retrieves the HTTP header that matches the specified key.
     *
     * @param key The name of the header to search for. Case-insensitive comparison is used.
     * @return The matching `HttpHeader` if found, or `null` if no matching header exists.
     * @since 2.1.0
     */
    fun getHeader(key: String): HttpHeader? = headers.find { it.nameEquals(key) }
    /**
     * Retrieves an HTTP header that matches the specified key. If no matching header is found,
     * the provided exception will be thrown.
     *
     * @param key the key of the header to retrieve
     * @param lazyException a supplier that provides the exception to be thrown if the header is not found
     * @return the HTTP header that matches the specified key
     * @since 6.1.0
     */
    fun getHeaderOrThrow(key: String, lazyException: ThrowableSupplier): HttpHeader =
        headers.findFirstOrThrow(lazyException) { it.nameEquals(key) }
    /**
     * Retrieves the HTTP header corresponding to the provided key. If the header is not found,
     * returns an error indicating the missing key.
     *
     * @param key The name of the header to look for.
     * @return Either an error of type [IterableError.NotFound] if the header is not found,
     *         or the matching [HttpHeader] if it exists.
     * @since 6.1.0
     */
    fun getHeaderOrError(key: String): Either<IterableError.NotFound, HttpHeader> =
        headers.findFirstOrError { it.nameEquals(key) }.mapLeft { IterableError.NotFound(key) }
    /**
     * Retrieves the HTTP header that matches the specified key. If no header is found,
     * the provided default supplier is used to supply a default header.
     *
     * @param key the name of the header to retrieve
     * @param default a supplier providing a default header to return if the specified key is not found
     * @return the HTTP header that matches the key or the default header supplied by the supplier
     * @since 6.1.0
     */
    fun getHeaderOr(key: String, default: Supplier<HttpHeader>): HttpHeader =
        headers.findFirstOr(default) { it.nameEquals(key) }

    /**
     * Retrieves a list of typed values associated with the given key. If no values are found or the values
     * cannot be cast to the specified type, an error is returned.
     *
     * Possible errors:
     * - [IterableError.NotFound] - Indicates that the specified key was not found in the header.
     * - [DeserializationError.ReadError] - Indicates an error occurred while reading the JSON string.
     * - [DeserializationError.MappingError] - Indicates an error occurred during the mapping process.
     * - [DeserializationError] - Indicates an unexpected error occurred during deserialization.
     *
     * @param key The key to look up the values for.
     * @return Either an error if the retrieval or type conversion fails, or a list of values of the specified type.
     * @since 6.1.0
     */
    inline fun <reified T> getTypedOrError(key: String): Either<Error, List<T>> =
        toList()
            .findFirstOrError { it.nameEquals(key) }
            .mapLeft { IterableError.NotFound(key) }
            .thenMergeWith { it.typedValues<T>() }

    /**
     * Sets a header with the given key and values. If a header with the same key already exists, it is removed before adding the new header.
     *
     * @param key The name of the header to set.
     * @param values The values to associate with the header.
     * @since 3.0.0
     */
    operator fun set(key: String, values: Iterable<Any>) {
        headers.removeIf { it.nameEquals(key) }
        headers += HttpHeader(key, values)
    }
    /**
     * Sets a header by replacing any existing header with the same key and adding a new one.
     *
     * @param key The name of the header to set.
     * @param value The value to associate with the given header key.
     * @since 3.0.0
     */
    @JvmName("setValueAny")
    operator fun set(key: String, value: Any) {
        headers.removeIf { it.nameEquals(key) }
        headers += HttpHeader(key, value)
    }

    /**
     * Converts the `headers` collection into a new `List` instance.
     *
     * This method creates an immutable list containing all elements of the `headers` collection
     * in the order they appear. The resulting list reflects the state of `headers` at the time
     * of invocation and will not be affected by subsequent modifications to `headers`.
     *
     * @return A list containing all elements of the `headers` collection.
     * @since 2.1.0
     */
    fun toList() = headers.toList()
    /**
     * Converts the `headers` object into a mutable list representation.
     *
     * This method delegates the conversion to the `toMList` function of the `headers` object.
     *
     * @return A mutable list derived from the `headers` object.
     *
     * @since 2.1.0
     */
    fun toMList() = headers.toMList()
    /**
     * Converts the collection of HTTP headers into a `Set` of unique entries.
     *
     * This function provides a set-based view of the headers contained in this `HttpHeaders` object,
     * ensuring that each header appears only once in the resulting collection.
     *
     * @return A `Set` containing all the unique headers in this `HttpHeaders` object.
     * @since 2.1.0
     */
    fun toSet() = headers.toSet()
    /**
     * Converts the `headers` collection into a multiset, which allows duplicate elements.
     * This method provides a convenient way to represent the `headers` structure
     * in a multiset format for scenarios where duplicate entries are significant.
     *
     * @return A multiset representation of the `headers` collection.
     * @since 2.1.0
     */
    fun toMSet() = headers
    /**
     * Converts the collection of HTTP headers into an array representation.
     *
     * This function allows for retrieving all headers in the form of an array,
     * preserving their insertion order as maintained within the underlying collection.
     *
     * @return An array containing all headers.
     * @since 2.1.0
     */
    fun toArray() = headers
    /**
     * Converts the collection of HTTP headers into a map, where the key is the header name
     * and the value is the corresponding `HttpHeader` object.
     *
     * @return A map of HTTP headers keyed by their names.
     * @since 2.1.0
     */
    fun toMap() = headers.associate(HttpHeader::name, HttpHeader::values)

    /**
     * Provides a string representation of the `HttpHeaders` instance.
     *
     * This method converts the collection of HTTP headers into a map representation
     * and then returns its string representation. The resulting string outlines
     * the mapping of header names to their corresponding values.
     *
     * @return A string representation of the headers as a map.
     * @since 2.1.0
     */
    override fun toString() = toMap().toString()

    /**
     * Creates a new instance of HttpHeaders by adding the specified HttpHeader elements
     * to the existing headers collection.
     *
     * @param header A vararg parameter representing the HttpHeader elements to be added
     * to the current headers collection.
     * @return A new instance of HttpHeaders containing the added headers.
     * @since 2.1.0
     */
    fun with(vararg header: HttpHeader) = HttpHeaders(headers.filterNot { h -> header.any { h.name == it.name } }.toList().plus(header).toMSet())
    
    /**
     * Adds or updates the specified HTTP header in the collection of headers. If a header with the same 
     * name already exists, the values are combined; otherwise, the new header is added.
     *
     * @param header The HTTP header to be added or updated in the collection.
     * @since 3.0.0
     */
    operator fun plusAssign(header: HttpHeader) {
        get(header.name).let { origin ->
            val other = if (origin != null) HttpHeader(header.name, origin + header.values) else header
            headers.removeIf { it.nameEquals(header.name) }
            headers += other
        }
    }
    /**
     * Adds all the headers from the provided [HttpHeaders] instance 
     * to the current collection of headers.
     *
     * @param headers The [HttpHeaders] instance whose headers should be added.
     * @since 3.0.0
     */
    operator fun plusAssign(headers: HttpHeaders) {
        headers.headers.forEach { plusAssign(it) }
    }
    /**
     * Adds the given collection of HTTP headers to the existing headers.
     * This operator allows combining multiple headers into the current set of headers.
     *
     * @param headers An iterables collection of HTTP headers to be added.
     * @since 3.0.0
     */
    operator fun plusAssign(headers: Iterable<HttpHeader>) {
        headers.forEach { plusAssign(it) }
    }
    /**
     * Adds the given headers to the existing collection of headers. 
     * If a header with the same key already exists, its values will be appended.
     *
     * @param headers a map where each key is the name of the header and the corresponding value is 
     *                an iterables collection of header values to add.
     * @since 3.0.0
     */
    operator fun plusAssign(headers: Map<String, Iterable<Any>>) {
        headers.forEach { plusAssign(HttpHeader(it.key, it.value)) }
    }
    /**
     * Adds the given header entry to the HTTP headers. This operator function is designed
     * to merge or append the specified header into the existing collection of headers.
     *
     * @param header a key-value pair where the key is a header name, and the value is an iterables
     * of any objects representing the header values.
     * @since 3.0.0
     */
    operator fun plusAssign(header: Map.Entry<String, Iterable<Any>>) {
        plusAssign(HttpHeader(header))
    }
    /**
     * Adds the given header represented as a pair of a key and a collection of values to the current collection of headers.
     * The operation uses the `HttpHeader` wrapper to encapsulate the provided key-value pair.
     *
     * @param header A pair consisting of a header name as a `String` and a collection of header values as an `Iterable<Any>`. 
     *               The header name should represent the key, and the collection should represent the values associated with the key.
     * @since 3.0.0
     */
    operator fun plusAssign(header: Pair<String, Iterable<Any>>) {
        plusAssign(HttpHeader(header))
    }

    /**
     * Removes the specified `HttpHeader` values from the current set of headers. If the header
     * is present and its values partially match, the matching values are removed. If no values
     * remain for the header, it is completely removed from the set of headers.
     *
     * @param header The `HttpHeader` instance whose values are to be subtracted from the current headers.
     * @since 3.0.0
     */
    operator fun minusAssign(header: HttpHeader) {
        get(header.name).let { origin ->
            val other = if (origin != null) HttpHeader(header.name, origin - header.values.toSet()) else null
            headers.removeIf { it.nameEquals(header.name) }
            if (other != null) headers += other
        }
    }
    /**
     * Removes the specified HTTP headers from the current collection.
     * The operation applies to each header in the provided `HttpHeaders` object.
     *
     * @param headers The `HttpHeaders` object containing the headers to be removed.
     * @since 3.0.0
     */
    operator fun minusAssign(headers: HttpHeaders) {
        headers.headers.forEach { minusAssign(it) }
    }
    /**
     * Subtracts the specified headers from the current collection of headers.
     * This operator function allows removing multiple headers by iterating through the provided collection.
     *
     * @param headers a collection of `HttpHeader` objects to be subtracted from the current collection.
     * @since 3.0.0
     */
    operator fun minusAssign(headers: Iterable<HttpHeader>) {
        headers.forEach { minusAssign(it) }
    }
    /**
     * Removes the specified headers from the current collection by subtracting them.
     *
     * @param headers a map of header names to their corresponding iterables values to be removed.
     * @since 3.0.0
     */
    operator fun minusAssign(headers: Map<String, Iterable<Any>>) {
        headers.forEach { minusAssign(HttpHeader(it.key, it.value)) }
    }
    /**
     * Removes the specified HTTP header from the current collection of headers.
     *
     * @param header a map entry representing the HTTP header to be removed, 
     * consisting of a key as the header name and a value as an iterables of the header's values.
     * @since 3.0.0
     */
    operator fun minusAssign(header: Map.Entry<String, Iterable<Any>>) {
        minusAssign(HttpHeader(header))
    }
    /**
     * Removes the specified header and its values from the collection.
     *
     * This operator function modifies the underlying collection by removing 
     * the header represented by the provided key-value pair.
     *
     * @param header a pair consisting of a header key as a String and its associated values as an Iterable of Any.
     * @since 3.0.0
     */
    operator fun minusAssign(header: Pair<String, Iterable<Any>>) {
        minusAssign(HttpHeader(header))
    }
    /**
     * Removes headers from the current collection if their names are contained within the provided iterables of keys.
     *
     * @param key An iterables collection of strings representing the keys of the headers to be removed.
     * @since 3.0.0
     */
    @JvmName("minusAssignIterableString")
    operator fun minusAssign(key: Iterable<String>) {
        headers.removeIf { it.name in keys }
    }
    /**
     * Removes all headers with the specified key from the `headers` collection.
     *
     * @param key The name of the header(s) to be removed.
     * @since 3.0.0
     */
    operator fun minusAssign(key: String) {
        headers.removeIf { it.name == key }
    }

    /**
     * Combines the current `HttpHeader` with another `HttpHeader`, producing a new `HttpHeader` instance.
     * If the specified `HttpHeader` already exists in the collection, the values of the two headers are merged.
     * Otherwise, a new header with the specified name and values is created.
     *
     * @param other The `HttpHeader` to add or merge with the current header.
     * @since 2.1.0
     */
    operator fun plus(other: HttpHeader) = get(other.name).let {
        val other = if (it != null) HttpHeader(other.name, it + other.values) else other
        val headers = if (it != null) headers.minus<HttpHeader>(getHeader(other.name)!!) else headers
        HttpHeaders(headers.plus<HttpHeader>(other).toMSet())
    }

    /**
     * Combines the current `HttpHeaders` instance with another `HttpHeaders` instance.
     *
     * This operator function merges the headers from both `HttpHeaders` instances
     * into a new `HttpHeaders` object. The resulting instance includes all headers
     * from the current and the provided `HttpHeaders`, preserving their order of insertion.
     *
     * @param other Another instance of `HttpHeaders` to be merged with the current one.
     * @return A new `HttpHeaders` instance containing the combined headers.
     * @since 2.1.0
     */
    operator fun plus(other: HttpHeaders): HttpHeaders {
        val otherHeaders = other.headers.toList()
        tailrec fun addRec(current: HttpHeaders, remaining: List<HttpHeader>): HttpHeaders {
            if (remaining.isEmpty()) return current
            return addRec(current + remaining.first(), remaining.drop(1))
        }

        return addRec(this, otherHeaders)
    }
    /**
     * Combines the current `HttpHeaders` instance with the entries from the specified `MultiStringMap`
     * and returns a new `HttpHeaders` instance containing the merged headers.
     *
     * Both the existing headers and the entries from `other` are included in the resulting collection.
     * Duplicate keys are not automatically resolved and will appear multiple times in the merged result.
     *
     * @param others The `MultiStringMap` instance whose entries should be added to the current headers.
     * @return A new `HttpHeaders` instance containing the combined headers.
     * @since 2.1.0
     */
    operator fun plus(others: MultiStringMap) = plus(others.toHttpHeaders())
    /**
     * Combines this `HttpHeaders` instance with another `DataMapNN` to produce a new `HttpHeaders` instance.
     *
     * This operator function creates a new `HttpHeaders` instance containing all headers from this `HttpHeaders`
     * object and additional headers derived from the entries of the provided `DataMapNN`. The entries in the
     * `DataMapNN` are converted into `HttpHeader` instances before being added to the resulting `HttpHeaders`.
     *
     * @param others The `DataMapNN` to be combined with this `HttpHeaders`.
     * @return A new `HttpHeaders` instance containing combined headers from both.
     * @since 2.1.0
     */
    @JvmName("plusDataMapNN")
    operator fun plus(others: DataMapNN) = plus(others.toHttpHeaders())
    /**
     * Removes the specified `HttpHeader` from the current collection of headers.
     *
     * This operator function filters out entries in the `headers` collection that match the given `HttpHeader`.
     * The equality comparison is performed using the `equals` method of `HttpHeader`.
     *
     * @param other The `HttpHeader` to be removed from the collection.
     * @return A new list of headers excluding the specified `HttpHeader`.
     * @since 2.1.0
     */
    operator fun minus(other: HttpHeader) = HttpHeaders(headers.filterNot { it == other }.toMSet())
    /**
     * Subtracts the headers in the specified [HttpHeaders] instance from the current collection of headers,
     * returning a new collection without the headers present in the given [HttpHeaders].
     *
     * This operation filters out headers from the original collection that have matching entries
     * in the provided [HttpHeaders] instance.
     *
     * @param other The [HttpHeaders] instance containing headers to be removed from the current collection.
     * @since 2.1.0
     */
    operator fun minus(other: HttpHeaders) = HttpHeaders(headers.filterNot { h -> other.headers.any { h == it } }.toMSet())
    /**
     * Removes all headers from the collection that match the specified key.
     *
     * The resulting collection will exclude any headers whose name equals the given key.
     *
     * @param key The name of the header to be removed.
     * @return A new collection of headers with the specified key excluded.
     * @since 2.1.0
     */
    operator fun minus(key: String) = HttpHeaders(headers.filterNot { it.name == key }.toMSet())
    /**
     * Subtracts the specified keys from the current HTTP headers and returns a new `HttpHeaders` instance
     * containing the remaining headers.
     *
     * @param keys A list of keys to be removed from the current headers.
     * @since 2.1.0
     */
    operator fun minus(keys: Iterable<String>) = HttpHeaders(headers.filterNot { it.name in keys }.toMSet())

    /**
     * Compares this instance with another object for equality.
     *
     * @param other The object to be compared with this instance.
     * @return `true` if the specified object is equal to this instance, `false` otherwise.
     * @since 2.1.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HttpHeaders

        return headers.sortedBy(HttpHeader::name) == other.headers.sortedBy(HttpHeader::name)
    }

    /**
     * Computes the hash code for this object based on the `headers` property.
     *
     * The hash code is derived from the `headers` property and is intended
     * to provide a suitable number for use in hash-based collections such as
     * `HashMap` or `HashSet`. The hash code of an object must remain consistent
     * as long as the object is not modified.
     *
     * @return The hash code value for this object.
     * @since 2.1.0
     */
    override fun hashCode() = headers.hashCode()

    /**
     * Retrieves and parses the "Accept" header from a request.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return Either an error indicating failure to retrieve or parse the "Accept" header,
     *         or a list of MediaType objects representing the parsed values of the header.
     * @since 6.1.0
     */
    fun getAccept(): Either<Error, List<MediaType>> =
        getOrError(ACCEPT) thenMergeWith { list -> list.traverse { MediaType.parse(it) } }
    /**
     * Sets the "Accept" header with the provided media types.
     *
     * Allows specifying multiple media types that the client is willing to accept.
     *
     * @param values Vararg parameter representing the media types to set in the "Accept" header.
     * @since 3.0.0
     */
    fun setAccept(vararg values: MediaType) = set(ACCEPT, values.toList())

    /**
     * Returns a value representing the parsed "Accept-Language" header.
     *
     * This method attempts to retrieve and parse the "Accept-Language" header
     * into a list of language ranges. It handles errors that may occur during
     * retrieval or parsing of the header and provides them as part of the result.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return Either an `Error` object in case of failure, or a list of `LanguageRange`
     *         objects parsed from the "Accept-Language" header.
     * @since 6.1.0
     */
    fun getAcceptLanguage(): Either<Error, List<LanguageRange>> = getOrError(ACCEPT_LANGUAGE)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(ACCEPT_LANGUAGE) }
        .thenMergeWith {
            tryOrError({ t -> InvalidTypeFormat(it, typeOf<LanguageRange>(), t) }) {
                LanguageRange.parse(it).toList()
            }
        }
    /**
     * Sets the `Accept-Language` header with the specified language ranges and their weights.
     * This header is used to specify the preferred languages for response content.
     *
     * @param values A variable number of `LanguageRange` objects representing the desired languages
     *               and their respective weights. The weight is a decimal value between 0.0 and 1.0,
     *               where 1.0 is the highest preference.
     * @since 3.0.0
     */
    fun setAcceptLangauge(vararg values: LanguageRange) {
        val decimal = DecimalFormat("0.0", DecimalFormatSymbols(Locale.ROOT))
        val values = values.toList()
            .map { range ->
                if (range.weight == LanguageRange.MAX_WEIGHT) range.range else range.range + ";q=" + decimal.format(
                    range.weight
                )
            }
            .toList()
        set(ACCEPT_LANGUAGE, values.joinToString())
    }

    /**
     * Retrieves the "Accept-Language" header values as a list of `Locale` objects.
     * Converts the language ranges into `Locale` instances, filtering out invalid entries.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return Either an `Error` or a list of `Locale` objects based on the parsed "Accept-Language" header.
     * @since 6.1.0
     */
    fun getAcceptLanguageAsLocales(): Either<Error, List<Locale>> = getAcceptLanguage().map { list ->
        val locales = emptyMList<Locale>()
        list.forEach { if (it.range notStartsWith Char.STAR) locales.add(Locale.forLanguageTag(it.range)) }
        locales.toList()
    }
    /**
     * Updates the accept-language settings using the provided locales.
     * Converts the input locales into language ranges and applies them.
     *
     * @param values A vararg of `Locale` objects representing the languages to be set.
     * @since 3.0.0
     */
    fun setAcceptLanguageAsLocales(vararg values: Locale) =
        setAcceptLangauge(*values.map { LanguageRange(it.toLanguageTag()) }.toTypedArray())

    /**
     * Retrieves and parses the "Accept-Patch" HTTP header value.
     *
     * This method returns a list of parsed media types indicating
     * the acceptable patch document formats for the resource.
     *
     * The parsing is performed on the header value using the MediaType.parse method.
     * If an error occurs during retrieval or parsing, it propagates as an error.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return A list of MediaType objects parsed from the "Accept-Patch" header value.
     * @since 6.1.0
     */
    fun getAcceptPatch() =
        getOrError(ACCEPT_PATCH) thenMergeWith { list -> list.traverse { MediaType.parse(it) } }
    /**
     * Sets the "Accept-Patch" header with the provided media types.
     *
     * The "Accept-Patch" header is used to specify the patch document media type(s)
     * that the server supports. This method takes a variable number of `MediaType`
     * arguments and sets them to the header.
     *
     * @param values A variable number of `MediaType` instances representing
     *               the media types to include in the "Accept-Patch" header.
     * @since 3.0.0
     */
    fun setAcceptPatch(vararg values: MediaType) = set(ACCEPT_PATCH, values.toList())

    /**
     * Retrieves and processes the `ACCESS_CONTROL_ALLOW_CREDENTIALS` value.
     *
     * This method fetches the value associated with `ACCESS_CONTROL_ALLOW_CREDENTIALS`, validates its existence,
     * and attempts to convert it to a boolean. If the required value is not present or cannot be parsed as a valid boolean,
     * it raises an appropriate error. The method handles intermediate errors and provides a streamlined processing flow.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return A result indicating the processed boolean value of `ACCESS_CONTROL_ALLOW_CREDENTIALS`, or an error representation.
     * @since 6.1.0
     */
    fun getAccessControlAllowCredentials() = getOrError(ACCESS_CONTROL_ALLOW_CREDENTIALS)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(ACCESS_CONTROL_ALLOW_CREDENTIALS) }
        .thenMergeWith { (-it).toBooleanStrictOrError() }
    /**
     * Sets the Access-Control-Allow-Credentials header to indicate whether the response to
     * the request can be exposed when the credentials flag is true.
     *
     * @param allowCredentials A boolean value indicating whether the resource supports
     * credentials. If true, credentials are allowed; otherwise, they are not.
     * @since 3.0.0
     */
    fun setAccessControlAllowCredentials(allowCredentials: Boolean) = set(ACCESS_CONTROL_ALLOW_CREDENTIALS, allowCredentials.toString())

    /**
     * Retrieves the list of allowed HTTP methods for Cross-Origin Resource Sharing (CORS).
     *
     * The method fetches the access control allow methods from a predefined source and returns a list of HTTP methods.
     * If the methods cannot be located, an error of type `IterableError.NotFound` is returned.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return Either an error of type `IterableError.NotFound` if the methods are not found,
     *         or a list of allowed HTTP methods as strings.
     * @since 6.1.0
     */
    fun getAccessControlAllowMethods(): Either<IterableError.NotFound, List<String>> = getOrError(ACCESS_CONTROL_ALLOW_METHODS)
        .thenMergeWith { it.orErrorIfEmpty() }
        .map { it.joinToString(Char.COMMA) }
        .mapLeft { IterableError.NotFound(ACCESS_CONTROL_ALLOW_METHODS) }
        .map { it / Char.COMMA }
    /**
     * Sets the "Access-Control-Allow-Methods" header with the specified HTTP methods.
     *
     * This method is used to configure the allowed HTTP methods for Cross-Origin Resource Sharing (CORS).
     *
     * @param values The HTTP methods to be allowed, provided as vararg parameters. Each method is represented by an instance of [HttpMethod].
     * @since 3.0.0
     */
    fun setAccessControlAllowMethods(vararg values: HttpMethod) = set(ACCESS_CONTROL_ALLOW_METHODS, values.joinToString(String.COMMA, transform = HttpMethod::value))
    /**
     * Sets the "Access-Control-Allow-Methods" header for the response with the specified HTTP methods.
     * This header indicates the methods allowed when accessing the resource in a cross-origin request.
     *
     * @param values The HTTP methods to be allowed, such as "GET", "POST", "PUT", etc.
     * @since 6.1.0
     */
    fun setAccessControlAllowMethods(vararg values: String) = set(ACCESS_CONTROL_ALLOW_METHODS, values.joinToString(String.COMMA))

    /**
     * Retrieves the maximum age for the access control setting from a configuration or source.
     *
     * This method attempts to fetch the value associated with the key ACCESS_CONTROL_MAX_AGE.
     * If the value is not found, an IterableError.NotFound is returned.
     * The retrieved value is then processed to ensure it is converted to a valid long value
     * and subsequently transformed into a duration in seconds.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return Either the maximum age as a duration in seconds or an error if the value is not found
     *         or cannot be processed as a valid duration.
     * @since 6.1.0
     */
    fun getAccessControlMaxAge() = getOrError(ACCESS_CONTROL_MAX_AGE)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(ACCESS_CONTROL_MAX_AGE) }
        .thenMergeWith { it.toLongOrError() }
        .map { it.asSecondsOfDuration() }

    /**
     * Sets the maximum age for the access control in seconds. This determines how long
     * the access control settings should be cached by the client.
     *
     * @param maxAge The maximum duration, as a [Duration], for which the access control settings
     *               are considered valid.
     * @since 3.0.0
     */
    @OptIn(RiskyApproximationOfTemporal::class)
    fun setAccessControlMaxAge(maxAge: Duration) = set(ACCESS_CONTROL_MAX_AGE, maxAge.toSeconds())

    /**
     * Retrieves the HTTP access control request method header value.
     *
     * This method attempts to fetch and process the value of the access control request
     * method header. If the value is found and successfully resolved, it returns the method
     * as a string. Otherwise, it returns an error indicating that the value was not found.
     *
     * @return Either an error representing a missing access control request method or the resolved request method as a string.
     * @since 6.1.0
     */
    fun getAccessControlRequestMethod(): Either<IterableError.NotFound, String> = getOrError(ACCESS_CONTROL_REQUEST_METHOD)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(ACCESS_CONTROL_REQUEST_METHOD) }
    /**
     * Sets the `Access-Control-Request-Method` header for a preflight request in a CORS (Cross-Origin Resource Sharing) scenario.
     * This header indicates which HTTP method will be used during the actual request.
     *
     * @param value The HTTP method to set for the `Access-Control-Request-Method` header.
     * @since 3.0.0
     */
    fun setAccessControlRequestMethod(value: HttpMethod) = set(ACCESS_CONTROL_REQUEST_METHOD, value.value)
    /**
     * Sets the HTTP method to be used for the Access-Control-Request-Method header.
     *
     * The Access-Control-Request-Method header is used in CORS requests to indicate
     * which HTTP method will be used when the actual request is made.
     *
     * @param value The HTTP method to be set for the Access-Control-Request-Method header.
     * @since 6.1.0
     */
    fun setAccessControlRequestMethod(value: String) = set(ACCESS_CONTROL_REQUEST_METHOD, value)

    /**
     * Retrieves the acceptable charsets from the provided input, parsing and validating
     * the values to return a list of `Charset` instances.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return Either an `Error` if the acceptable charsets could not be retrieved or parsed
     *         successfully, or a `List<Charset>` containing the parsed charsets.
     * @since 6.1.0
     */
    fun getAcceptCharset(): Either<Error, List<Charset>> = getOrError(ACCEPT_CHARSET)
        .thenMergeWith { it.orErrorIfEmpty() }
        .map { it.joinToString(Char.COMMA) }
        .mapLeft { IterableError.NotFound(ACCEPT_CHARSET) }
        .thenMergeWith {
            tryOrError({ t -> InvalidTypeFormat(it, typeOf<Charset>(), t) }) {
                val tokens = it / Char.COMMA
                val result = emptyMList<Charset>()
                for (token in tokens) {
                    val paramIdx = token.indexOf(';')
                    val charsetName = if (paramIdx == -1) token
                    else token.take(paramIdx)
                    if (charsetName != String.STAR) result.add(Charset.forName(charsetName))
                }
                result.toList()
            }
        }
    /**
     * Sets the Accept-Charset header with the specified character sets.
     *
     * @param values A vararg of Charset objects to be included in the Accept-Charset header.
     * @since 3.0.0
     */
    fun setAcceptCharset(vararg values: Charset) = set(ACCEPT_CHARSET, values.joinToString(String.COMMA) { it.name().lowercase(Locale.ROOT) })

    /**
     * Retrieves the allowed values as a comma-separated string or an error if not found.
     *
     * This method attempts to obtain the value associated with the 'ALLOW' key.
     * If the key does not exist or if the value is empty, a corresponding error
     * of type `IterableError.NotFound` is returned.
     *
     * @return Either an instance of `IterableError.NotFound` indicating the absence
     * of the 'ALLOW' key or a `String` representing the allowed values joined
     * by a comma.
     * @since 6.1.0
     */
    fun getAllow(): Either<IterableError.NotFound, String> = getOrError(ALLOW)
        .thenMergeWith { it.orErrorIfEmpty() }
        .map { it.joinToString(Char.COMMA) }
        .mapLeft { IterableError.NotFound(ALLOW) }
    /**
     * Configures the allowed HTTP methods for a specific resource or endpoint.
     *
     * @param values A variable number of HTTP methods to allow, specified as instances of [HttpMethod].
     * @since 3.0.0
     */
    fun setAllow(vararg values: HttpMethod) = set(ALLOW, values.joinToString(String.COMMA, transform = HttpMethod::value))
    /**
     * Configures the given values for the "ALLOW" functionality by joining them with a comma separator.
     *
     * @param values A variable number of string arguments to be set for "ALLOW".
     * @since 6.1.0
     */
    fun setAllow(vararg values: String) = set(ALLOW, values.joinToString(String.COMMA))

    /**
     * Retrieves a Bearer authentication token. The function attempts to extract the authorization token
     * and process it into a JWT (JSON Web Token) representation. It handles potential errors during the
     * extraction and transformation steps and returns either the successfully processed JWT or an error.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @param key The header key to retrieve the Bearer authentication token from. Defaults to "Authorization".
     * @return An `Either` instance where the left side represents an `Error` in case of failure, and
     *         the right side represents a `Jwt` object if the operation is successful.
     * @since 6.1.0
     */
    fun getBearerAuth(key: String = AUTHORIZATION): Either<Error, Jwt> = getOrError(key)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(AUTHORIZATION) }
        .thenMergeWith { it.toJwt() }
    /**
     * Sets the Bearer Authorization header with the provided JWT token.
     *
     * @param key The header key to retrieve the Bearer authentication token from. Defaults to "Authorization".
     * @param token The JWT token to use in the Authorization header.
     * @since 6.1.0
     */
    fun setBearerAuth(key: String = AUTHORIZATION, token: Jwt) = set(key, token.toString(true))
    /**
     * Sets the Authorization header to use Basic Authentication with the provided encoded credentials.
     *
     * The provided string should be a Base64-encoded combination of the username and password, formatted as "username:password".
     * This method adheres to the Basic Authentication standard and updates the Authorization header accordingly.
     *
     * @param encodedCredentials The Base64-encoded "username:password" string to be used for Basic Authentication.
     * @since 3.0.0
     */
    fun setBasicAuth(encodedCredentials: String) = set(AUTHORIZATION, "Basic $encodedCredentials")
    /**
     * Configures HTTP Basic Authentication by encoding the provided username and password
     * using the specified character set. This method adds the encoded credentials to the
     * Authorization header of the request.
     *
     * @param username The username to be included in the authorization credentials. 
     * It must not contain a colon (:).
     * @param password The password to be included in the authorization credentials.
     * @param charset The character set used to encode the credentials. Defaults to ISO_8859_1.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the charset cannot encode the username or password,
     * or if the username contains a colon.
     * @since 3.0.0
     */
    fun setBasicAuth(username: String, password: String, charset: Charset = ISO_8859_1) {
        username.validate("setBasicAuth", "username", "Username cannot contains a colon") { Char.COLON !in it }
        val encoder = charset.newEncoder()
        (encoder.canEncode(username) && encoder.canEncode(password)) || throw IllegalArgumentException("Charset $charset cannot encode username or password")
        val credentialsString = "$username:$password"
        val encodedBytes = Base64.getEncoder().encode(credentialsString.toByteArray(charset))

        set(AUTHORIZATION, String(encodedBytes, charset))
    }
    
    /**
     * Retrieves the content language associated with the current context.
     *
     * The method attempts to fetch the content language information and returns either
     * a `Locale` object representing the language tag or an `IterableError.NotFound` error
     * if the specified content language is not present.
     *
     * @return an `Either` that contains a `Locale` if the content language is found,
     *         or an `IterableError.NotFound` instance if the content language is not available.
     * @since 6.1.0
     */
    fun getContentLanguage(): Either<IterableError.NotFound, Locale> = getOrError(CONTENT_LANGUAGE)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(CONTENT_LANGUAGE) }
        .map { Locale.forLanguageTag(it) }
    /**
     * Sets the content language for the current operation or request. 
     * The language is specified using a Locale object and will be 
     * transformed to its corresponding language tag.
     *
     * @param value the Locale representing the language to set as the content language.
     * @since 3.0.0
     */
    fun setContentLanguage(value: Locale) = set(CONTENT_LANGUAGE, value.toLanguageTag())

    /**
     * Retrieves the content length as a `DataSize` object, wrapped in an `Either` type indicating success or failure.
     *
     * The method attempts to extract and normalize the content length information. If successful, it returns
     * a `DataSize` representing the content length in bytes. If the content length cannot be determined, an
     * `Error` is returned describing the issue.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return An `Either` object containing a normalized `DataSize` on success or an `Error` on failure.
     * @since 6.1.0
     */
    fun getContentLength(): Either<Error, DataSize> = getOrError(CONTENT_LENGTH)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(CONTENT_LENGTH) }
        .thenMergeWith { it.toLongOrError() }
        .map { (it ofUnit BYTES).normalize() }
    /**
     * Sets the content length of a data transfer operation.
     *
     * @param value The content length to set. The value is specified as a `DataSize` object 
     *              and will be internally converted to bytes.
     * @since 3.0.0
     */
    @OptIn(Beta::class)
    fun setContentLength(value: DataSize) = set(CONTENT_LENGTH, value.convertTo(BYTES)().value)
    /**
     * Sets the value of the Content-Length header for a request.
     *
     * @param bytes The length of the content in bytes to be set in the Content-Length header.
     * @since 3.0.0
     */
    fun setContentLength(bytes: Long) = set(CONTENT_LENGTH, bytes)

    /**
     * Retrieves the content type information as a MediaType object, or an error if the process fails.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return Either an Error instance if there was a failure, or a MediaType object representing the content type.
     * @since 6.1.0
     */
    fun getContentType(): Either<Error, MediaType> = getOrError(CONTENT_TYPE)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(CONTENT_TYPE) }
        .thenMergeWith { MediaType.parse(it) }
    /**
     * Sets the content type for the request or response by assigning a specified MediaType value.
     *
     * @param value The MediaType object representing the MIME type to be set as the content type.
     * @since 3.0.0
     */
    fun setContentType(value: MediaType) = set(CONTENT_TYPE, value.toString())

    /**
     * Retrieves the date information associated with the DATE key.
     *
     * The method processes the value corresponding to the DATE key, performs various operations
     * to ensure its validity, and converts it to an Instant if the data is found and valid.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return Either an Error object if the operation fails, or an Instant representing the date.
     * @since 6.1.0
     */
    fun getDate(): Either<Error, Instant> = getOrError(DATE)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(DATE) }
        .thenMergeWith { it.headerDateToInstant() }

    /**
     * Sets the date to the specified value using the provided TemporalAccessor and formats it 
     * using the RFC_7231_DATE_TIME_FORMATTER.
     *
     * @throws java.time.DateTimeException if the provided TemporalAccessor cannot be formatted as a header date.
     * @param value the TemporalAccessor representation of the date to be set
     * @since 3.0.0
     */
    fun setDate(value: TemporalAccessor = Instant()) = set(DATE, RFC_7231_DATE_TIME_FORMATTER(value))

    /**
     * Retrieves the expiration date as an Instant, wrapped in an Either type.
     * The method handles possible errors during processing and maps them into appropriate error types.
     *
     * @return Either containing an expiration date as an Instant on success,
     *         or an Error if retrieval or conversion fails.
     * @since 6.1.0
     */
    fun getExpires(): Either<Error, Instant> = getOrError(EXPIRES)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(EXPIRES) }
        .thenMergeWith { it.headerDateToInstant() }
    /**
     * Sets the expiration date for this object using the provided temporal accessor.
     * The expiration date is formatted according to the RFC 7231 date-time standard.
     *
     * @param value the temporal accessor representing the expiration date and time
     * @throws java.time.DateTimeException if the provided temporal accessor cannot be formatted as a header date.
     * @since 3.0.0
     */
    fun setExpires(value: TemporalAccessor) = set(EXPIRES, RFC_7231_DATE_TIME_FORMATTER(value))

    /**
     * Retrieves the host information as an `Either` type, representing either a successful result or an error.
     *
     * The method attempts to resolve the host and port information from a given data source. If successful, it returns
     * an `InetSocketAddress` containing the host and port. Otherwise, it returns an `IterableError.NotFound` instance
     * indicating that the host could not be found.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return An `Either` containing the resolved `InetSocketAddress` on success or `IterableError.NotFound` on failure.
     * @since 6.1.0
     */
    fun getHost(): Either<Error, InetSocketAddress> = getOrError(HOST)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(HOST) }
        .thenMergeWith { tryOrError({ t -> InvalidTypeFormat(it, typeOf<InetSocketAddress>(), t) }) {
            var host: String? = null
            var port = 0
            val separator = if (it startsWith '[') it.indexOf(Char.COLON, it.indexOf(']')) else it.lastIndexOf(Char.COLON)
            if (separator != -1) {
                host = it.take(separator)
                val portString = it.substring(separator + 1)
                tryOrNull { port = portString.toInt() }
            }
            if (host == null) host = it
            InetSocketAddress.createUnresolved(host, port)
        } }
    /**
     * Sets the host value in the configuration.
     *
     * @param host the socket address containing the host name or IP address 
     * and port that will be set as the host value.
     * @since 3.0.0
     */
    fun setHost(host: InetSocketAddress) {
        var value = host.hostString
        val port = host.port
        if (port != 0) value = "$value:$port"
        set(HOST, value)
    }

    /**
     * Retrieves the `If-Modified-Since` header value as an `Instant`.
     * Attempts to parse the value from the header, returning it wrapped in an `Either` instance.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return An `Either` containing the parsed `Instant` if successful,
     *         or an `Error` if the header is missing or cannot be parsed.
     * @since 6.1.0
     */
    fun getIfModifiedSince(): Either<Error, Instant> = getOrError(IF_MODIFIED_SINCE)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(IF_MODIFIED_SINCE) }
        .thenMergeWith { it.headerDateToInstant() }
    /**
     * Sets the "If-Modified-Since" header value with the provided temporal accessor.
     *
     * The header indicates the date and time at which the resource was last modified.
     * This can be used to make conditional requests to retrieve the resource only if it has been changed
     * since the provided date and time.
     *
     * @param value The temporal accessor representing the date and time to set for the "If-Modified-Since" header.
     *              It must be formatted using the RFC 7231 date-time format.
     * @throws java.time.DateTimeException if the provided temporal accessor cannot be formatted as a header date.
     * @since 3.0.0
     */
    fun setIfModifiedSince(value: TemporalAccessor) = set(IF_MODIFIED_SINCE, RFC_7231_DATE_TIME_FORMATTER(value))

    /**
     * Retrieves the value associated with the `IF_UNMODIFIED_SINCE` key, converting it into an `Instant` if present and valid.
     * Handles potential errors during the retrieval and conversion process, returning an `Either` that wraps the result.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return An `Either` containing an `Error` on failure or an `Instant` representing the parsed date if successful.
     * @since 6.1.0
     */
    fun getIfUnmodifiedSince(): Either<Error, Instant> = getOrError(IF_UNMODIFIED_SINCE)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(IF_UNMODIFIED_SINCE) }
        .thenMergeWith { it.headerDateToInstant() }
    /**
     * Sets the "If-Unmodified-Since" condition for a request. This determines the validity of the operation 
     * based on a specific timestamp. The server processes the request only if the resource has not been 
     * modified since the given timestamp.
     *
     * @param value the timestamp to compare against, specified as a [TemporalAccessor]. This value is formatted 
     *              according to the RFC 7231 standard before being applied to the request.
     * @throws java.time.DateTimeException if the provided temporal accessor cannot be formatted as a header date.
     * @since 3.0.0
     */
    fun setIfUnmodifiedSince(value: TemporalAccessor) = set(IF_UNMODIFIED_SINCE, RFC_7231_DATE_TIME_FORMATTER(value))

    /**
     * Retrieves the last modified timestamp from the specified data source.
     * This method processes the data to obtain an `Instant` representing the
     * last modification time. If an error occurs during retrieval or processing,
     * it returns an `Either` containing the corresponding error.
     *
     * @return An instance of `Either` containing either an `Error` or the `Instant`
     *         representing the last modification time.
     * @since 6.1.0
     */
    fun getLastModified(): Either<Error, Instant> = getOrError(LAST_MODIFIED)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(LAST_MODIFIED) }
        .thenMergeWith { it.headerDateToInstant() }
    /**
     * Sets the value of the `LAST_MODIFIED` field using the specified temporal accessor.
     *
     * This method converts the provided `TemporalAccessor` into a string formatted according to the
     * RFC 7231 date-time standard and assigns it to the `LAST_MODIFIED` field.
     *
     * @param value the temporal accessor representing the date and time to set as the last modified timestamp
     * @throws java.time.DateTimeException if the provided temporal accessor cannot be formatted as a header date.
     * @since 3.0.0
     */
    fun setLastModified(value: TemporalAccessor = Instant()) = set(LAST_MODIFIED, RFC_7231_DATE_TIME_FORMATTER(value))

    /**
     * Retrieves the location as a URI, wrapped in an Either type.
     * This method performs a series of operations to fetch and transform the location
     * data, handling errors and conversions along the way.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return Either an error indicating the failure reason or a URI representing the location.
     * @since 6.1.0
     */
    fun getLocation(): Either<Error, Uri> = getOrError(LOCATION)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(LOCATION) }
        .thenMergeWith { it.toUri() }
    /**
     * Sets the location URI for the given key.
     * 
     * @param value The URI to be set as the location.
     * @since 3.0.0
     */
    fun setLocation(value: Uri) = set(LOCATION, value.toString())

    /**
     * Retrieves a connection behavior wrapped in an Either structure, providing error handling for
     * various possible failure scenarios during the retrieval process.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [EnumError.NoSuchEntry] - The header value is malformed.
     *
     * @return Either an error indicating the failure reason or the successfully retrieved ConnectionBehaviour.
     * @since 6.1.1
     */
    fun getConnection(): Either<Error, ConnectionBehaviour> = getOrError(CONNECTION)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(CONNECTION) }
        .thenMergeWith { ConnectionBehaviour.of(it) orError { EnumError.NoSuchEntry(ConnectionBehaviour::class, it) } }
    /**
     * Sets the connection behavior for the current operation.
     *
     * @param value An instance of ConnectionBehaviour that defines
     * the behavior to be applied.
     * @since 6.1.1
     */
    fun setConnection(value: ConnectionBehaviour) = set(CONNECTION, value.value)

    /**
     * Determines if the "Accept-Ranges" header indicates support for byte-range requests.
     *
     * @return Either an error indicating a failure in retrieving or processing the "Accept-Ranges" header,
     *         or a Boolean where true indicates support for byte-range requests and false otherwise.
     * @since 6.1.1
     */
    fun getAcceptRanges(): Either<Error, Boolean> = getOrError(ACCEPT_RANGES)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(ACCEPT_RANGES) }
        .map { it == "bytes" }
    /**
     * Sets the "Accept-Ranges" header value to indicate whether the server supports range requests.
     *
     * @param value A Boolean indicating support for range requests.
     *              Pass `true` to set the header to "bytes", indicating support for range requests.
     *              Pass `false` to set the header to "none", indicating that range requests are not supported.
     * @since 6.1.1
     */
    fun setAcceptRanges(value: Boolean) = set(ACCEPT_RANGES, if (value) "bytes" else "none")

    /**
     * Retrieves the "Origin" header.
     *
     * Possible errors:
     * - [IterableError.NotFound] - The header was not found in the request.
     * - [InvalidTypeFormat] - The header value is malformed.
     *
     * @return An `Either` containing the origin as a `Uri` if retrieval and processing are successful,
     *         or an `Error` if any step in the process fails.
     * @since 6.1.1
     */
    fun getOrigin(): Either<Error, Uri> = getOrError(ORIGIN)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(ORIGIN) }
        .thenMergeWith { it.toUri() }
    /**
     * Sets the origin value as a string representation of the given Uri.
     *
     * @param value The Uri to be set as the origin.
     * @since 6.1.1
     */
    fun setOrigin(value: Uri) = set(ORIGIN, value.toString())

    /**
     * Retrieves the priority from a predefined configuration or input.
     *
     * This method parses the priority value and determines its associated attributes.
     * It employs error handling to account for missing or malformed data within the
     * priority configuration. The priority value is expected to follow a specific
     * pattern, supporting cases where the value begins with "u=" and may include an
     * optional additional flag (e.g., "i").
     *
     * @return An `Either` type containing an `Error` or a `Pair` where the first
     *         element is an integer priority value and the second element is a
     *         Boolean flag indicating additional characteristics.
     * @since 6.1.1
     */
    fun getPriority() = (getOrError(PRIORITY)
        .map { it.joinToString() }
        .mapLeft { IterableError.NotFound(PRIORITY) } as Either<Error, String>)
        .thenEither { if (it startsWith "u=") {
            if (Char.COMMA in it) it.split(Char.COMMA).run { first().after("u=").toIntOrError().bind() to (second() == "i") }
            else (it after "u=").toIntOrError().bind() to false
        } else 3 to (it startsWith "i") }
    /**
     * Sets the priority value based on the given parameters.
     *
     * @param pair A pair where the first element is an integer representing the priority level,
     *             coerced to a range of 0 to 7, and the second element is a boolean indicating
     *             whether an additional property ('i') should be appended.
     * @since 6.1.1
     */
    fun setPriority(pair: Pair<Int, Boolean>) =
        set(PRIORITY, "u=${pair.first.coerceIn(0, 7)}".asSingleList().letIf(pair.second) { it + "i" })
    /**
     * Sets the priority level for a specific operation. The priority value
     * determines the importance of the operation, influencing how it is handled
     * or executed. The priority is clamped to a range between 0 and 7.
     *
     * @param priority The priority level to be assigned, where 0 represents
     * the lowest priority and 7 represents the highest priority. Defaults to 3
     * if not specified.
     * @param incremental A flag indicating whether the priority should be
     * treated incrementally. If true, an additional incremental marker is applied.
     * @since 6.1.1
     */
    fun setPriority(priority: Int = 3, incremental: Boolean) =
        set(PRIORITY, "u=${priority.coerceIn(0, 7)}".asSingleList().letIf(incremental) { it + "i" })

    /**
     * Retrieves the referer value through a series of transformations and error handling.
     *
     * This method performs the following operations sequentially:
     * - Attempts to retrieve the referer using `getOrError` with the specified `REFERER` key.
     * - Merges the result with the first element of the resolved referer, converting it into a single value or error.
     * - Maps left-side errors to an instance of `IterableError.NotFound` with the `REFERER` key for context.
     * - Converts the resulting referer into a URI representation.
     *
     * @return The transformed referer, or an appropriate error if any step fails.
     * @since 6.1.1
     */
    fun getReferer() = getOrError(REFERER)
        .thenMergeWith { it.firstOrError() }
        .mapLeft { IterableError.NotFound(REFERER) }
        .thenMergeWith { it.toUri() }
    /**
     * Sets the referer header for a request.
     *
     * @param uri The URI to be set as the referer.
     * @since 6.1.1
     */
    fun setReferer(uri: Uri) = set(REFERER, uri.toString())
}