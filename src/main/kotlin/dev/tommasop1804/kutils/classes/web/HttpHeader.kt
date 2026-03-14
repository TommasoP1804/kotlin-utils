package dev.tommasop1804.kutils.classes.web

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.coding.JSON.Companion.toJSON
import dev.tommasop1804.kutils.classes.measure.MeasureUnit
import dev.tommasop1804.kutils.classes.measure.RMeasurement.Companion.ofUnit
import dev.tommasop1804.kutils.classes.security.JWT
import dev.tommasop1804.kutils.classes.security.JWT.Companion.toJWT
import dev.tommasop1804.kutils.classes.time.Duration
import dev.tommasop1804.kutils.classes.time.Duration.Companion.asSecondsOfDuration
import dev.tommasop1804.kutils.classes.time.TimeZone
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCEPT
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCEPT_CHARSET
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCEPT_LANGUAGE
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCEPT_PATCH
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCESS_CONTROL_ALLOW_CREDENTIALS
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCESS_CONTROL_ALLOW_METHODS
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCESS_CONTROL_MAX_AGE
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ACCESS_CONTROL_REQUEST_METHOD
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.ALLOW
import dev.tommasop1804.kutils.classes.web.HttpHeader.Companion.AUTHORIZATION
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
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.exceptions.NoSuchHeaderException
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.net.InetSocketAddress
import java.net.URI
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.ZoneOffset.UTC
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.Locale.LanguageRange
import kotlin.text.Charsets.ISO_8859_1

/**
 * Represents an HTTP header consisting of a name and associated values, implemented as a key-value pair
 * where the key is the header name, and the value is a [StringList] of associated values.
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
class HttpHeader(val name: String, values: Iterable<Any>) : StringList by values.map(Any::toString) {
    /**
     * A transformed list containing string representations of the elements
     * from the original `values` collection. Each element in the original
     * collection is converted to its string equivalent using the `toString` method.
     *
     * @since 2.1.0
     */
    val values: StringList = values.map(Any::toString)

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
     * `ACCEPT_LANGUAGE`, `ACCEPT_RANGES`, `HOST`, `REFERER`, `ORIGIN`, or 
     * `USER_AGENT`.
     *
     * @since 2.1.0
     */
    val isRequestContextHeader
        get() = name in setOf(ACCEPT, ACCEPT_CHARSET, ACCEPT_ENCODING, ACCEPT_LANGUAGE, ACCEPT_RANGES, HOST, REFERER, ORIGIN, USER_AGENT)
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
    constructor(pair: Pair<String, Iterable<Any>>) : this(pair.first, pair.second)
    /**
     * Secondary constructor for the HttpHeader class that initializes
     * the object using an existing Map.Entry instance.
     *
     * @param entry The Map.Entry containing a header name as the key and
     *              a StringList representing the header values as the value.
     * @since 2.1.0
     */
    constructor(entry: Map.Entry<String, Iterable<Any>>) : this(entry.key, entry.value)

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

    constructor(notation: String) : this(
        notation.splitAndTrim(Char.COLON, limit = 2).first(),
        notation.splitAndTrim(Char.COLON, limit = 2).second() / Char.COMMA
    )

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
        const val HOST = "Host"
        const val REFERER = "Referer"
        const val ORIGIN = "Origin"
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

        /**
         * Computes a unique hash-based identifier (eTag) for the current object.
         *
         * The eTag is generated by converting the object to its JSON representation
         * and then applying an MD5 hash algorithm to produce a hexadecimal string.
         * This identifier can be used for caching, validation, and resource versioning purposes.
         * @since 2.1.0
         */
        val Any.eTag
            get() = toJSON().value hashingToString HashingAlgorithm.MD5

        /**
         * Converts a string representation of a date, commonly found in HTTP headers, into an [Instant].
         *
         * The input string is expected to follow the format "WeekDay, Day Month Year Time TZ" (e.g., "Tue, 15 Nov 1994 08:12:31 GMT").
         *
         * @receiver The date string to be converted.
         * @return The corresponding [Instant] representation of the date.
         * @throws MalformedInputException If the month in the input string is invalid or if the format does not match expectations.
         * @since 2.1.0
         */
        fun String.headerDateToInstant(): Instant =
            tryOr({
                tryOrThrow({ -> MalformedInputException(Instant::class) }) {
                    if (ISO_DATE_TIME_STANDARD_VALIDATOR(this)) return@tryOrThrow Instant(this)()
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
            }) { Instant.from(RFC_7231_DATE_TIME_FORMATTER.parse(this)) }

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
        fun Pair<String, StringList>.toHttpHeader() = HttpHeader(this)
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
         * The Map.Entry key represents the header name, and the StringList value
         * represents the associated header values.
         *
         * This method leverages the secondary constructor of the HttpHeader class to
         * initialize an HttpHeader object with the key-value pair provided in the Map.Entry.
         *
         * @receiver The Map.Entry containing a key as the header name and a StringList
         */
        fun Map.Entry<String, StringList>.toHttpHeader() = HttpHeader(this)
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
    }

    /**
     * Converts the HttpHeader object into a Pair representation where the key is the header name
     * and the value is the associated list of header values.
     *
     * @return A Pair consisting of the header name and its corresponding values.
     * @since 2.1.0
     */
    fun toPair(): Pair<String, StringList> = name to values
    /**
     * Converts the current `HttpHeader` instance into a `Pair` representation, where the key
     * is the header name and the value is deserialized to the specified type [T].
     *
     * This function uses the first value from the header's value list, attempts to
     * deserialize it into the specified type [T], and then combines it with the header name
     * to form the resulting `Pair`.
     *
     * @return A `Pair` consisting of the header name as the key and the deserialized value
     * of type [T] as the value.
     * @throws Throwable If deserialization of the header value fails.
     * @since 2.1.0
     */
    inline fun <reified T> toTypedPair(): Pair<String, T> = name to values.first().deserialize<T>()()
    /**
     * Converts the current HttpHeader instance into a Map.Entry representation,
     * where the key is the header name and the value is the associated StringList.
     *
     * @return A Map.Entry consisting of the header name as the key and StringList as the value.
     * @since 2.1.0
     */
    fun toMapEntry(): Map.Entry<String, StringList> = toPair().toMapEntry()
    /**
     * Converts the current `HttpHeader` instance into a `Map.Entry` representation,
     * where the key is the header name and the value is deserialized to the specified type [T].
     *
     * This function reuses the `toPair` method to obtain a `Pair` representation and then converts
     * it into a `Map.Entry`.
     *
     * @return A `Map.Entry` consisting of the header name as the key and its deserialized value of type [T].
     * @since 2.1.0
     */
    inline fun <reified T> toTypedMapEntry(): Map.Entry<String, T> = toTypedPair<T>().toMapEntry()

    /**
     * Converts the current `HttpHeader` instance into an `HttpHeaders` object.
     *
     * This method facilitates the transformation of a single `HttpHeader` instance
     * into a more standard `HttpHeaders` representation, which can be useful for
     * further processing or integration with APIs that require an `HttpHeaders` type.
     *
     * @return A new `HttpHeaders` instance created from the current `HttpHeader`.
     * @since 2.2.0
     */
    fun toHttpHeaders() = HttpHeaders(this)

    /**
     * Deserializes the first value in the `values` list of the containing `HttpHeader` class
     * to the specified type [T].
     *
     * This function leverages the `deserialize` extension function and attempts to convert
     * the first string in the `values` list into an object of type [T]. If deserialization fails,
     * an exception is thrown.
     *
     * @return The deserialized value of type [T].
     * @since 2.1.0
     */
    inline fun <reified T> typedValue() = values.first().deserialize<T>()
    /**
     * Deserializes all values in the `values` collection to a specified type [T].
     *
     * This function uses the `deserialize` extension function to transform each
     * element in the `values` collection into an instance of the specified type [T].
     * The deserialization process may throw exceptions if any element cannot be
     * converted into the desired type.
     *
     * @return A list of objects of type [T], obtained by deserializing the elements
     * in the `values` collection.
     * @since 2.1.0
     */
    inline fun <reified T> typedValues() = values.map { it.serialize().deserialize<T>() }
    /**
     * Deserializes all values in the `values` collection to a specified type [T].
     *
     * This function uses the `deserialize` extension function to transform each
     * element in the `values` collection into an instance of the specified type [T].
     * The deserialization process may throw exceptions if any element cannot be
     * converted into the desired type.
     *
     * @return A list of objects of type [T], obtained by deserializing the elements
     * in the `values` collection.
     * @throws Throwable If the deserialization process for any element in the
     * `values` collection fails, the exception will be propagated.
     * @since 2.1.0
     */
    inline fun <reified T> unsafeTypedValues() = values.map { it.serialize().deserialize<T>()() }

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
        if (other.isNull() || this::class != other::class) return false

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
     * of another `StringList` instance to check for equality.
     *
     * The comparison is performed by converting the given `StringList` to a single list
     * and then comparing it to the `values` collection of the `HttpHeader` instance.
     *
     * @param other The `StringList` instance to compare against the `values` collection.
     * @since 2.1.0
     */
    fun valueEquals(other: StringList) = values == other.asSingleList()
}

/**
 * Represents a collection of HTTP headers, allowing for various operations
 * such as retrieving, deserializing, and manipulating header values.
 *
 * @since 2.1.0
 */
@Suppress("unused", "JavaDefaultMethodsNotOverriddenByDelegation")
@JsonSerialize(using = HttpHeaders.Companion.Serializer::class)
@JsonDeserialize(using = HttpHeaders.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = HttpHeaders.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = HttpHeaders.Companion.OldDeserializer::class)
class HttpHeaders(val headers: MSet<HttpHeader>) : MultiStringMap, Collection<HttpHeader> by headers {
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
    override val keys: StringSet
        get() = headers.map(HttpHeader::name).toSet()
    /**
     * A collection of `StringList` objects representing the values of all headers 
     * contained in the `HttpHeaders` instance.
     *
     * This property is derived by mapping each header's `values` through the `headers` collection.
     * It enables access to all header values without the need to work directly with individual headers.
     *
     * @since 2.1.0
     */
    override val values: Collection<StringList>
        get() = headers.map(HttpHeader::values)
    /**
     * A set containing all the entries in the `HttpHeaders` collection, where each entry maps 
     * a header name to its associated `StringList` values.
     *
     * The entries are derived by mapping the underlying `headers` collection into a set of 
     * key-value pairs using the `toMapEntry` extension function of `HttpHeader`.
     *
     * @since 2.1.0
     */
    override val entries: Set<Map.Entry<String, StringList>>
        get() = headers.map(HttpHeader::toMapEntry).toSet()

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
    constructor(vararg headers: HttpHeader) : this(headers.toSet().toMSet())
    
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
     * Constructs an instance of `HttpHeaders` by wrapping the provided header name and value
     * into an `HttpHeader` instance.
     *
     * This constructor simplifies the creation of `HttpHeaders` objects by accepting a header
     * name and its associated values as parameters, instead of requiring the caller to
     * explicitly create an `HttpHeader` object.
     *
     * @param name The name of the HTTP header to include in the collection.
     * @param values The values associated with the header name, encapsulated in a `StringList`.
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
     * @since 2.2.0
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
    }

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
     * has values equal to the specified `StringList`.
     *
     * The comparison is performed using the `valueEquals` method of each `HttpHeader` instance,
     * which compares the `values` collection*/
    override fun containsValue(value: StringList) = headers.any { it.valueEquals(value) }

    /**
     * Retrieves the values associated with the specified key from the headers.
     *
     * @param key The key to search for in the headers.
     * @return A list of values associated with the given key, or `null` if the key is not found.
     * @since 2.1.0
     */
    override fun get(key: String): StringList? = headers.find { it.nameEquals(key) }?.values

    /**
     * Retrieves the value associated with the specified key or throws an exception if the key is not present.
     *
     * @param key The key whose associated value is to be retrieved.
     * @param lazyException A lambda function that supplies the exception to be thrown if the key is not found.
     * Defaults to throwing a `NoSuchHeaderException` initialized with the provided key.
     * @return The value associated with the specified key, if present.
     * @throws Throwable The exception provided by the `lazyException` supplier if the key is not found.
     * @since 2.2.0
     */
    fun getOrThrow(key: String, lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) =
        get(key) ?: throw lazyException()

    /**
     * Retrieves the first value associated with the specified key from the headers.
     * Searches through the headers, finds the header that matches the given key,
     * and returns the first value from the matched header.
     *
     * @param key The key used to locate the matching header.
     * @return The first value corresponding to the specified key.
     * @throws NoSuchElementException If no header matches the given key.
     * @since 2.1.0
     */
    fun getFirst(key: String) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.first()
    /**
     * Retrieves the first value of a specific type associated with the given key from the headers.
     *
     * This function searches for a header with the specified key, extracts its values,
     * serializes the first value, and deserializes it into the desired type.
     *
     * @param T The type to which the first value should be deserialized.
     * @param key The key used to locate the header.
     * @return The first value deserialized into the specified type.
     * @throws Exception If the header cannot be found or if deserialization fails.
     * @since 2.1.0
     */
    inline fun <reified T> getFirstTyped(key: String) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.first().serialize().deserialize<T>()
    /**
     * Retrieves and deserializes the first value associated with the specified header key,
     * casting it to the desired type. This method is considered unsafe as it does not perform
     * explicit validation of the type during runtime.
     *
     * @param T The reified type to which the value will be cast.
     * @param key The header key to search for in the headers.
     * @return The first value associated with the header key, deserialized and cast to type T.
     * @throws NoSuchElementException If no header with the specified key exists.
     * @since 2.1.0
     */
    inline fun <reified T> getFirstTypedUnsafe(key: String) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.first().serialize().deserialize<T>()()

    /**
     * Retrieves the first value associated with the given key from the headers or returns null if no match is found.
     *
     * @param key The key to search for in the headers.
     * @return The first value associated with the key, or null if no such key exists.
     * @since 2.1.0
     */
    fun getFirstOrNull(key: String): String? = headers.find { it.nameEquals(key) }?.values?.firstOrNull()
    /**
     * Retrieves the first value associated with the given key, deserializing it into the specified type if possible.
     *
     * This function searches for a header entry matching the provided key, attempts to serialize the first value,
     * and then deserializes it into the specified type defined by the reified generic type parameter.
     *
     * @param T The type to which the value should be deserialized.
     * @param key The key identifying the header entry to search for.
     * @return The first value deserialized into the specified type, or null if the key is not found or deserialization fails.
     * @since 2.1.0
     */
    inline fun <reified T> getFirstTypedOrNull(key: String): T? = headers.find { it.nameEquals(key) }?.values?.firstOrNull()?.serialize()?.deserialize<T>()?.getOrThrow()
    /**
     * Retrieves the first value associated with the given key from the headers, deserializes it into the specified type,
     * and returns it if available. This method uses an unsafe approach to type casting and may throw runtime exceptions
     * if the deserialization fails. Use with caution.
     *
     * @param T The expected type of the deserialized value.
     * @param key The key used to find the corresponding header value.
     * @return The deserialized value of type T if found, or null if no value is available.
     * @since 2.1.0
     */
    inline fun <reified T> getFirstTypedUnsafeOrNull(key: String): T? = headers.find { it.nameEquals(key) }?.values?.firstOrNull()?.serialize()?.deserialize<T>()?.getOrThrow()

    /**
     * Retrieves the first value associated with the given key from the headers or throws an exception
     * provided by the lazyException supplier if no matching value is found.
     *
     * @param key The key to search for in the headers.
     * @param lazyException A supplier that provides the exception to throw if no matching value is found.
     * @return The first value associated with the given key.
     * @throws Throwable An exception provided by the lazyException supplier if no matching key or value is found.
     * @since 2.1.0
     */
    fun getFirstOrThrow(key: String, lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.firstOrThrow(lazyException)
    /**
     * Retrieves and deserializes the first value associated with the specified key from the headers
     * into the expected type [T]. Throws a lazily-supplied exception if the key or value is not found.
     *
     * @param key The key to search for in the headers.
     * @param lazyException A supplier function that provides the exception to be thrown 
     *                      if the key or value is not found.
     * @since 2.1.0
     */
    inline fun <reified T> getFirstTypedOrThrow(key: String, noinline lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.firstOrThrow(lazyException).serialize().deserialize<T>()
    /**
     * Retrieves the first value associated with the specified key from the headers and deserializes it into the specified type.
     * Throws a lazily-supplied exception if the value is not found or cannot be deserialized.
     *
     * @param key The key to search for in the headers.
     * @param lazyException A supplier function that provides the exception to be thrown if the value is not found
     *        or cannot be deserialized.
     * @return The deserialized first value associated with the provided key, cast to the specified type [T].
     * @throws Throwable The lazily-supplied exception if the key is not found or if deserialization fails.
     * @since 2.1.0
     */
    inline fun <reified T> getFirstTypedUnsafeOrThrow(key: String, noinline lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.firstOrThrow(lazyException).serialize().deserialize<T>()()

    /**
     * Retrieves the first value associated with the specified key from the headers. 
     * If no matching key-value pair is found, the supplied default value is returned.
     *
     * @param key The key to search for in the headers.
     * @param default A supplier function that provides a default value when the key is not found.
     * @return The first value associated with the specified key, or the value from the default supplier if the key is not present.
     * @since 2.1.0
     */
    fun getFirstOr(key: String, default: Supplier<String>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.firstOr(default)
    /**
     * Retrieves the first value associated with the specified key or a typed default value if the key is not found.
     * The value is serialized and deserialized into the specified type `T`.
     *
     * @param key The key to search for in the headers.
     * @param default A supplier providing a default value of type `T` if the key is not found.
     * @since 2.1.0
     */
    inline fun <reified T> getFirstTypedOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.firstOr(default).serialize().deserialize<T>()
    /**
     * Retrieves the first value associated with the specified key from the headers, attempting to cast it
     * to the provided type `T`. If no value is found, it uses the provided default supplier.
     * This function performs an unsafe cast, so use it with caution.
     *
     * @param T The type to which the value should be cast.
     * @param key The key to search for in the headers.
     * @param default A supplier function that provides a default value of type `T` if no valid value is found.
     * @return The first value associated with the key, cast to the specified type `T`, or the default value if not found.
     * @since 2.1.0
     */
    inline fun <reified T> getFirstTypedUnsafeOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.firstOr(default).serialize().deserialize<T>()()

    /**
     * Retrieves the second value associated with the specified key from the headers.
     *
     * This method searches for a header with a name matching the given key, and then it retrieves
     * the second value from the values of that header. If no matching header is found or if there
     * is no second value, the operation will throw an exception.
     *
     * @param key The name of the header to search for.
     * @return The second value associated with the specified header key.
     * @throws NoSuchElementException If no header with the specified key is found.
     * @throws IndexOutOfBoundsException If the header does not contain at least two values.
     * @since 2.1.0
     */
    fun getSecond(key: String) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.second()
    /**
     * Retrieves the second value associated with the given key from headers, serializes 
     * it, and deserializes it into the specified type.
     *
     * @param key The key whose second associated value is to be retrieved.
     * @return The second value associated with the key, deserialized into the specified type.
     * @since 2.1.0
     */
    inline fun <reified T> getSecondTyped(key: String) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.second().serialize().deserialize<T>()
    /**
     * Retrieves the second value associated with the given key from the `headers`,
     * performs serialization and deserialization, and returns the result as the specified type [T].
     * This operation assumes the existence of the key and at least two values, and performs
     * unchecked type casting, which may lead to runtime exceptions if the type does not match.
     *
     * @param T The type to which the second value in the header will be deserialized.
     * @param key The key used to locate the corresponding header in the `headers`.
     * @return The second value associated with the specified key, deserialized into the specified type [T].
     * @throws NoSuchElementException If the key is not found or there is no second value.
     * @since 2.1.0
     */
    inline fun <reified T> getSecondTypedUnsafe(key: String) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.second().serialize().deserialize<T>()()

    /**
     * Retrieves the second value associated with the specified key from the headers.
     *
     * Searches through the headers for an entry that matches the given key and
     * returns the second value associated with that key, if it exists. If no
     * matching entry or second value is found, `null` is returned.
     *
     * @param key the key used to identify the entry in the headers.
     * @return the second value associated with the specified key, or `null` if not found.
     * @since 2.1.0
     */
    fun getSecondOrNull(key: String): String? = headers.find { it.nameEquals(key) }?.values?.secondOrNull()
    /**
     * Retrieves the second value associated with the specified key from the headers,
     * attempts to deserialize it into the specified type [T], and returns the result.
     * If the value is not found or the deserialization fails, returns null.
     *
     * @param T The type to which the value should be deserialized.
     * @param key The key whose associated second value is to be retrieved.
     * @return The second value associated with the key, deserialized to type [T], or null if not found or deserialization fails.
     * @since 2.1.0
     */
    inline fun <reified T> getSecondTypedOrNull(key: String): T? = headers.find { it.nameEquals(key) }?.values?.secondOrNull()?.serialize()?.deserialize<T>()?.getOrThrow()
    /**
     * Retrieves the second value associated with the given key from the headers,
     * attempts to deserialize it into the specified type, and returns it.
     * If no value exists or deserialization fails, `null` is returned.
     *
     * This function is considered unsafe as it relies on unchecked type casts and reified generics,
     * which may result in runtime type errors if the expected type does not match the actual data type.
     *
     * @param T The type to which the value will be deserialized.
     * @param key The key associated with the header whose value is to be retrieved.
     * @return The second value associated with the specified key, deserialized to the type `T`,
     *         or `null` if no such value exists or deserialization fails.
     **/
    inline fun <reified T> getSecondTypedUnsafeOrNull(key: String): T? = headers.find { it.nameEquals(key) }?.values?.secondOrNull()?.serialize()?.deserialize<T>()?.getOrThrow()

    /**
     * Retrieves the second value associated with the specified key or throws an exception if not found.
     *
     * This method searches through the headers to find a match based on the provided key.
     * If found, it retrieves the associated values and ensures the second value exists.
     * If the second value is not present, the provided lazy exception is thrown.
     *
     * @param key The key used to search for the header entry.
     * @param lazyException A supplier*/
    fun getSecondOrThrow(key: String, lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.secondOrThrow(lazyException)
    /**
     * Retrieves and deserializes the second value associated with the given key from the headers,
     * ensuring that the value is of the specified type. If the key or second value is not found,
     * the provided exception supplier is invoked to throw an exception.
     *
     * @param T The type to which the second value should be deserialized.
     * @param key The key to search for in the headers.
     * @param lazyException A supplier that provides the exception to be thrown if the key or second value is not found.
     * @return The deserialized second value of the specified type.
     * @throws Throwable The exception provided by the supplier if the key or second value is not found.
     * @since 2.1.0
     */
    inline fun <reified T> getSecondTypedOrThrow(key: String, noinline lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.secondOrThrow(lazyException).serialize().deserialize<T>()
    /**
     * Retrieves the second value associated with a specific header key, performs serialization and 
     * deserialization, and returns it as a typed object. If the second value is not found, an exception is thrown.
     *
     * This method is considered "unsafe" as it performs unchecked casting after deserialization,
     * and assumptions about the type must be correct to avoid runtime errors.
     *
     * @param T The expected type of the deserialized object.
     * @param key The header key whose second value is to be retrieved.
     * @param lazyException A supplier for the exception to be thrown if the required value is not found.
     * @return The deserialized object of the expected type T.
     * @throws Throwable If the second value is not found or if deserialization fails.
     * @since 2.1.0
     */
    inline fun <reified T> getSecondTypedUnsafeOrThrow(key: String, noinline lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.secondOrThrow(lazyException).serialize().deserialize<T>()()

    /**
     * Retrieves the second value associated with the specified header key, or falls back to the default value if no such value exists.
     *
     * @param key The header key used to search for the desired entry.
     * @param default A supplier that provides a fallback value if the second value is not found.
     * @return The second value corresponding to the header key, or the provided default value.
     * @throws NoSuchElementException If the key is not found in the headers.
     * @since 2.1.0
     */
    fun getSecondOr(key: String, default: Supplier<String>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.secondOr(default)
    /**
     * Retrieves the second value associated with the given key from the headers, converting it to the specified type.
     * If the second value is not present, the provided default value is used instead.
     *
     * @param key The key whose associated value is to be fetched.
     * @param default A supplier that provides a default value to be used if the second value does not exist.
     * @return The second value associated with the key, converted to the type `T`, or the provided default value.
     * @since 2.1.0
     */
    inline fun <reified T> getSecondTypedOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.secondOr(default).serialize().deserialize<T>()
    /**
     * Retrieves the second value associated with a specified key from a headers collection, or a default value
     * of the specified type if the second value does not exist. The type is determined at runtime and deserialized
     * from the stored representation.
     *
     * @param key The key used to locate the header entry in the collection.
     * @param default A supplier that provides a default value of type `T` to be used if a second value is not found.
     * @return The second value associated with the key, deserialized to the specified type `T`, or the default value if unavailable.
     * @throws NoSuchElementException if the key is not found in the headers collection.
     * @since 2.1.0
     */
    inline fun <reified T> getSecondTypedUnsafeOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.secondOr(default).serialize().deserialize<T>()()

    /**
     * Retrieves the third value corresponding to a specified key from the headers.
     * If the key does not exist or there is no third value, an exception is thrown.
     *
     * @param key The key used to locate the header in the collection.
     * @return The third value associated with the specified key.
     * @throws NoSuchElementException If the key is not found in the headers or if
     * there is no third value for the key.
     * @since 2.1.0
     */
    fun getThird(key: String) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.third()
    /**
     * Retrieves the third item from the list of values associated with the provided key,
     * deserializes it into the specified type [T], and returns the result.
     *
     * @param T The type to which the third item will be deserialized.
     * @param key The key used to locate the header with the list of values.
     * @return The third value from the list, deserialized into the specified type [T].
     * @throws NoSuchElementException If the header with the provided key is not found.
     * @since 2.2.0
     */
    inline fun <reified T> getThirdTyped(key: String) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.third().serialize().deserialize<T>()
    /**
     * Retrieves the third value associated with the specified key, attempts to serialize and 
     * deserialize it into the provided type [T], and returns it. This method is considered unsafe 
     * as it assumes the existence of the key, a third value, and successful deserialization to [T].
     *
     * @param key The key to locate in the headers.
     * @return The deserialized third value of type [T] associated with the given key.
     * @throws IllegalStateException If the key is not found in the headers or if a third value does not exist.
     *
     * @since 2.1.0
     */
    inline fun <reified T> getThirdTypedUnsafe(key: String) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.third().serialize().deserialize<T>()()

    /**
     * Retrieves the third value associated with the specified key from the headers, or null if either the key is not found
     * or there are fewer than three values for the key.
     *
     * @param key The key whose associated third value is to be returned.
     * @return The third value associated with the key, or null if the key is not found or does not have at least three values.
     * @since 2.1.0
     */
    fun getThirdOrNull(key: String): String? = headers.find { it.nameEquals(key) }?.values?.thirdOrNull()
    /**
     * Retrieves the third element corresponding to the given key from a collection, deserializes it, and returns it as the specified type, or null if not found.
     *
     * @param T The reified type to which the third element should be deserialized.
     * @param key The key used to locate the collection of data.
     * @return The third element deserialized to the specified type, or null if the element is not found or deserialization fails.
     * @since 2.1.0
     */
    inline fun <reified T> getThirdTypedOrNull(key: String): T? = headers.find { it.nameEquals(key) }?.values?.thirdOrNull()?.serialize()?.deserialize<T>()?.getOrThrow()
    /**
     * Retrieves the third item from the values associated with the specified key, deserialized into the requested type `T`.
     * If the third item does not exist or deserialization fails, it returns null.
     *
     * @param T The type to which the third value is deserialized. This type must be reified.
     * @param key The key used to search for the associated values from which the third item is retrieved.
     * @return The third value deserialized into type `T`, or null if the third value is not present or deserialization fails.
     * @since 2.1.0
     */
    inline fun <reified T> getThirdTypedUnsafeOrNull(key: String): T? = headers.find { it.nameEquals(key) }?.values?.thirdOrNull()?.serialize()?.deserialize<T>()?.getOrThrow()

    /**
     * Retrieves the third element associated with the specified key from the headers.
     * If the key is not found or the third element does not exist, the provided exception is thrown.
     *
     * @param key the key to look up in the headers.
     * @param lazyException a supplier for the exception to be thrown if the key is not found
     * or the third value does not exist.
     * @return the third value associated with the specified key.
     * @throws Throwable the exception produced by the provided supplier if the key
     * or third value does not exist.
     * @since 2.1.0
     */
    fun getThirdOrThrow(key: String, lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.thirdOrThrow(lazyException)
    /**
     * Retrieves the third value from the headers corresponding to the specified key, deserializes it
     * into the specified type [T], or throws an exception if the value cannot be found or deserialized.
     *
     * @param T The type to which the value is deserialized.
     * @param key The key used to locate the required header in the headers collection.
     * @param lazyException A supplier that provides the exception to be thrown if the required value
     *                       cannot be found or deserialized.
     * @return The third value corresponding to the specified key, deserialized into type [T].
     * @throws Throwable The exception provided by the [lazyException] supplier if the value
     *                   cannot be found or deserialized.
     * @since 2.1.0
     */
    inline fun <reified T> getThirdTypedOrThrow(key: String, noinline lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.thirdOrThrow(lazyException).serialize().deserialize<T>()
    /**
     * Retrieves the third value associated with the given key from the headers, performs type deserialization, 
     * and throws a lazily supplied exception if the key or value cannot be resolved.
     *
     * This method is considered unsafe because type deserialization is performed without additional checks, 
     * and runtime exceptions may occur if the type does not match or if the third value is absent.
     *
     * @param T The expected type of the deserialized third value.
     * @param key The key used to find the corresponding header in the collection.
     * @param lazyException A supplier that lazily provides the exception to be thrown if the key or value is 
     * not resolvable or valid.
     * @return The deserialized third value of the specified type associated with the provided key.
     * @throws Throwable The lazily supplied exception if the key or third value does not exist or deserialization fails.
     * @since 2.1.0
     */
    inline fun <reified T> getThirdTypedUnsafeOrThrow(key: String, noinline lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.thirdOrThrow(lazyException).serialize().deserialize<T>()()

    /**
     * Retrieves the third value associated with the given key from the headers.
     * If the third value is not available, the provided default supplier is used to supply the fallback value.
     *
     * @param key The key used to locate the header values.
     * @param default A supplier function that provides a default value if the third value is unavailable.
     * @since 2.1.0
     */
    fun getThirdOr(key: String, default: Supplier<String>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.thirdOr(default)
    /**
     * Retrieves the third value associated with the specified key from the headers, or provides a default value.
     * If a third value exists in the header's values list, it is returned after being serialized and deserialized
     * to the appropriate type. If the third value does not exist, the default value provided by the supplier
     * is used instead.
     *
     * @param T The expected type of the retrieved value.
     * @param key The key used to find the target header in the collection.
     * @param default A supplier function that provides the default value if the third value is not found.
     * @return The third value associated with the key, deserialized to type T.
     * @since 2.1.0
     */
    inline fun <reified T> getThirdTypedOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.thirdOr(default).serialize().deserialize<T>()
    /**
     * Retrieves the third value associated with the specified key from the headers, or a default value if the key does not exist,
     * and attempts to deserialize it into the specified type. The operation is performed unsafely, and type mismatches may result in runtime exceptions.
     *
     * @param T The target type to deserialize the value into.
     * @param key The key to search for in the headers.
     * @param default A supplier that provides the default value if the key does not exist or if the list does not contain a third value.
     * @return The deserialized value of type T, which may be the third value from the specified key's associated list or the supplied default.
     * @throws RuntimeException If the deserialization process fails due to a type mismatch or other errors.
     * @since 2.1.0
     */
    inline fun <reified T> getThirdTypedUnsafeOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.thirdOr(default).serialize().deserialize<T>()()

    /**
     * Retrieves the only element associated with the specified key from the headers.
     * The method performs a search for the header with the given key, ensuring that 
     * exactly one element exists in its associated values. An exception is thrown 
     * if no matching key is found or if there are multiple elements in the values.
     *
     * @param key The key of the header whose single associated value is to be retrieved.
     * @return The single element associated with the specified key.
     * @throws NoSuchElementException If no matching key is found.
     * @throws IllegalStateException If multiple values are associated with the key.
     * @since 2.1.0
     */
    fun getOnlyElement(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.onlyElement()
    /**
     * Retrieves and processes a single element of a specified type from a collection identified by the given key.
     *
     * This method filters the headers by the specified key, extracts a single element from the matching entries,
     * and deserializes it to the requested type.
     *
     * @param T The reified type to which the extracted element should be deserialized.
     * @param key The key used to locate the desired element in the headers.
     * @return An instance of type T obtained by processing the single element associated with the given key.
     * @throws IllegalStateException If the key does not match any elements or if more than one element is found.
     * 
     * @since 2.1.0
     */
    inline fun <reified T> getOnlyElementTyped(key: String) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.onlyElement().serialize().deserialize<T>()
    /**
     * Retrieves the only element of type [T] from the specified header key. This method assumes
     * that the provided key exists, the header contains exactly one value, and it can be safely
     * deserialized to the specified type [T]. Unsafe behavior can occur if these assumptions are 
     * violated, such as deserialization errors or unexpected multiple values.
     *
     * @param key The key used to locate the header from which the single element will be retrieved.
     * @return The single deserialized value of type [T] associated with the specified header key.
     * @throws IllegalArgumentException If the header matching the key is not found or contains multiple 
     * elements.
     * @since 2.1.0
     */
    inline fun <reified T> getOnlyElementTypedUnsafe(key: String) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.onlyElement().serialize().deserialize<T>()()

    /**
     * Retrieves the only element associated with the specified key if it exists, or returns null
     * if there are no elements or more than one element.
     *
     * @param key The key to search for in the headers.
     * @return The only element associated with the key, or null if none exists or if there are multiple elements.
     * @since 2.1.0
     */
    fun getOnlyElementOrNull(key: String): String? = headers.find { it.nameEquals(key) }?.values?.onlyElementOrNull()
    /**
     * Retrieves a single element of the specified type [T] by the given key from a collection, 
     * or returns null if no such element exists or multiple elements are found.
     *
     * @param T The type of the element to retrieve.
     * @param key The key used to locate the element in the collection.
     * @return The single element of type [T] if found, or null if not found or multiple elements are present.
     * @since 2.1.0
     */
    inline fun <reified T> getOnlyElementTypedOrNull(key: String): T? = headers.find { it.nameEquals(key) }?.values?.onlyElementOrNull()?.serialize()?.deserialize<T>()?.getOrThrow()
    /**
     * Retrieves the only element matching the specified key from headers as a deserialized object of the specified type,
     * or returns null if no matching element is found, there are multiple elements, or deserialization fails.
     *
     * This function uses inline reification and performs unsafe casting. It is intended for scenarios where type consistency
     * is guaranteed externally.
     *
     * @param key The key used to search for the matching element in headers.
     * @return The deserialized object of type [T] if a single matching element is found and deserialization succeeds, 
     * or null otherwise.
     * @since 2.1.0
     */
    inline fun <reified T> getOnlyElementTypedUnsafeOrNull(key: String): T? = headers.find { it.nameEquals(key) }?.values?.onlyElementOrNull()?.serialize()?.deserialize<T>()?.getOrThrow()

    /**
     * Retrieves the only element matching the provided key from a collection. 
     * Throws an exception supplied by the `lazyException` if no matching element is found
     * or if there are multiple matching elements.
     *
     * @param key The key used to identify the element to be retrieved.
     * @param lazyException A supplier that provides the exception to be thrown if the conditions are not met.
     * @since 2.1.0
     */
    fun getOnlyElementOrThrow(key: String, lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.onlyElementOrThrow(lazyException)
    /**
     * Retrieves and deserializes the only element from the headers' values associated with the specified key.
     * If there are no elements or more than one element, the provided exception supplier is invoked to throw an exception.
     *
     * @param T The type to which the single value will be deserialized.
     * @param key The key to identify the header whose values are to be processed.
     * @param lazyException A supplier that provides the exception to be thrown if finding the single element fails.
     * @return The deserialized element with the specified type.
     * @throws Exception Thrown with the exception supplied by lazyException if the single element retrieval fails.
     * @since 2.1.0
     */
    inline fun <reified T> getOnlyElementTypedOrThrow(key: String, noinline lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.onlyElementOrThrow(lazyException).serialize().deserialize<T>()
    /**
     * Retrieves the only element of a given type from the headers associated with the specified key, or throws 
     * an exception if the conditions are not met.
     *
     * This method searches for a header matching the provided key, ensures it contains exactly one value, 
     * and then deserializes the value to the specified type. If the search fails, or if multiple elements are 
     * found, it throws an exception provided by the supplied [lazyException].
     *
     * The method is considered "typed unsafe" since it uses inline reified type arguments for deserialization.
     *
     * @param T The reified type to which the retrieved element will be deserialized.
     * @param key The key used to search for the corresponding header.
     * @param lazyException A supplier of a throwable exception, used in case of errors during searching or deserialization.
     * @since 2.1.0
     */
    inline fun <reified T> getOnlyElementTypedUnsafeOrThrow(key: String, noinline lazyException: ThrowableSupplier = { NoSuchHeaderException(key) }) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.onlyElementOrThrow(lazyException).serialize().deserialize<T>()()

    /**
     * Retrieves the only occurrence of a value associated with the specified key. If no such element
     * exists, the default value provided by the supplied function is returned.
     *
     * @param key The key whose associated single element value is to be retrieved.
     * @param default A supplier function that provides a default value if no element is found.
     * @return The value associated with the key if found, otherwise the supplied default value.
     * @since 2.1.0
     */
    fun getOnlyElementOr(key: String, default: Supplier<String>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.firstOr(default)
    /**
     * Retrieves the only element associated with the specified key from the headers and deserializes it into the specified type.
     * If no element is found, the provided default value is used.
     *
     * @param T The expected type of the deserialized object.
     * @param key The key used to search for the element in the headers.
     * @param default A supplier function providing the default value if no element is found.
     * @return The deserialized object of type T.
     * @throws IllegalStateException If there are multiple elements associated with the key.
     * @since 2.1.0
     */
    inline fun <reified T> getOnlyElementTypedOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.onlyElementOr(default).serialize().deserialize<T>()
    /**
     * Retrieves the only element associated with the provided key as a deserialized type-safe object.
     * 
     * If the key exists in the `headers` collection and has a single value, that value is serialized,
     * deserialized to ensure type safety, and returned. If no value is associated with the key, the
     * supplied default value is serialized, deserialized, and returned instead.
     * 
     * This method can potentially throw runtime exceptions if the deserialization process or 
     * type conversion fails at runtime.
     * 
     * @param T The reified type of the object to deserialize.
     * @param key The key to search within the `headers` collection.
     * @param default A supplier function to provide a default value if the key is not present or no value exists.
     * @since 2.1.0
     */
    inline fun <reified T> getOnlyElementTypedUnsafeOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow({ NoSuchHeaderException(key) }) { it.nameEquals(key) }.values.onlyElementOr(default).serialize().deserialize<T>()()

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
     * Retrieves and deserializes the values of an HTTP header with the specified key into a list of objects of type [T].
     *
     * This function searches for a header in the collection whose name matches the provided `key`.
     * If a matching header is found, its values are deserialized into instances of type [T].
     * Returns null if no matching header is found or if deserialization fails.
     *
     * @param key The name of the header whose values should be deserialized.
     * @return A list of deserialized objects of type [T], or null if no matching header is found.
     * @since 2.1.0
     */
    inline fun <reified T> getTyped(key: String) = headers.find { it.nameEquals(key) }?.typedValues<T>()
    /**
     * Retrieves the values of an HTTP header with the specified name and deserializes
     * them to the specified type [T]. This method performs an unsafe operation where
     * deserialization errors will propagate as exceptions.
     *
     * @param T The target type to which the header values should be deserialized.
     * @param key The name of the HTTP header to search for.
     * @return A list of deserialized objects of type [T], or `null` if the header
     *         with the specified name does not exist.
     * @throws Throwable If the deserialization of any header value fails.
     * @since 2.1.0
     */
    inline fun <reified T> getTypedUnsafe(key: String) = headers.find { it.nameEquals(key) }?.unsafeTypedValues<T>()

    /**
     * Sets a header with the given key and values. If a header with the same key already exists, it is removed before adding the new header.
     *
     * @param key The name of the header to set.
     * @param values The values to associate with the header.
     * @since 2.2.0
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
     * @since 2.2.0
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
    fun toMSet() = headers.toMSet()
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
     * @since 2.2.0
     */
    operator fun plusAssign(header: HttpHeader) {
        get(header.name).let { origin ->
            val other = if (origin.isNotNull()) HttpHeader(header.name, origin + header.values) else header
            headers.removeIf { it.nameEquals(header.name) }
            headers += other
        }
    }
    /**
     * Adds all the headers from the provided [HttpHeaders] instance 
     * to the current collection of headers.
     *
     * @param headers The [HttpHeaders] instance whose headers should be added.
     * @since 2.2.0
     */
    operator fun plusAssign(headers: HttpHeaders) {
        headers.headers.forEach { plusAssign(it) }
    }
    /**
     * Adds the given collection of HTTP headers to the existing headers.
     * This operator allows combining multiple headers into the current set of headers.
     *
     * @param headers An iterable collection of HTTP headers to be added.
     * @since 2.2.0
     */
    operator fun plusAssign(headers: Iterable<HttpHeader>) {
        headers.forEach { plusAssign(it) }
    }
    /**
     * Adds the given headers to the existing collection of headers. 
     * If a header with the same key already exists, its values will be appended.
     *
     * @param headers a map where each key is the name of the header and the corresponding value is 
     *                an iterable collection of header values to add.
     * @since 2.2.0
     */
    operator fun plusAssign(headers: Map<String, Iterable<Any>>) {
        headers.forEach { plusAssign(HttpHeader(it.key, it.value)) }
    }
    /**
     * Adds the given header entry to the HTTP headers. This operator function is designed
     * to merge or append the specified header into the existing collection of headers.
     *
     * @param header a key-value pair where the key is a header name, and the value is an iterable
     * of any objects representing the header values.
     * @since 2.2.0
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
     * @since 2.2.0
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
     * @since 2.2.0
     */
    operator fun minusAssign(header: HttpHeader) {
        get(header.name).let { origin ->
            val other = if (origin.isNotNull()) HttpHeader(header.name, origin - header.values.toSet()) else null
            headers.removeIf { it.nameEquals(header.name) }
            if (other.isNotNull()) headers += other
        }
    }
    /**
     * Removes the specified HTTP headers from the current collection.
     * The operation applies to each header in the provided `HttpHeaders` object.
     *
     * @param headers The `HttpHeaders` object containing the headers to be removed.
     * @since 2.2.0
     */
    operator fun minusAssign(headers: HttpHeaders) {
        headers.headers.forEach { minusAssign(it) }
    }
    /**
     * Subtracts the specified headers from the current collection of headers.
     * This operator function allows removing multiple headers by iterating through the provided collection.
     *
     * @param headers a collection of `HttpHeader` objects to be subtracted from the current collection.
     * @since 2.2.0
     */
    operator fun minusAssign(headers: Iterable<HttpHeader>) {
        headers.forEach { minusAssign(it) }
    }
    /**
     * Removes the specified headers from the current collection by subtracting them.
     *
     * @param headers a map of header names to their corresponding iterable values to be removed.
     * @since 2.2.0
     */
    operator fun minusAssign(headers: Map<String, Iterable<Any>>) {
        headers.forEach { minusAssign(HttpHeader(it.key, it.value)) }
    }
    /**
     * Removes the specified HTTP header from the current collection of headers.
     *
     * @param header a map entry representing the HTTP header to be removed, 
     * consisting of a key as the header name and a value as an iterable of the header's values.
     * @since 2.2.0
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
     * @since 2.2.0
     */
    operator fun minusAssign(header: Pair<String, Iterable<Any>>) {
        minusAssign(HttpHeader(header))
    }
    /**
     * Removes headers from the current collection if their names are contained within the provided iterable of keys.
     *
     * @param key An iterable collection of strings representing the keys of the headers to be removed.
     * @since 2.2.0
     */
    @JvmName("minusAssignIterableString")
    operator fun minusAssign(key: Iterable<String>) {
        headers.removeIf { it.name in keys }
    }
    /**
     * Removes all headers with the specified key from the `headers` collection.
     *
     * @param key The name of the header(s) to be removed.
     * @since 2.2.0
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
        val other = if (it.isNotNull()) HttpHeader(other.name, it + other.values) else other
        val headers = if (it.isNotNull()) headers.minus<HttpHeader>(getHeader(other.name)!!) else headers
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
            return addRec(current + remaining.first(), (-1)(remaining))
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

    // 2.2.02.2.02.2.02.2.02.2.02.2.02.2.02.2.0-

    /**
     * Retrieves and parses the "ACCEPT" header values into a list of `MediaType` objects.
     *
     * This method first retrieves the ACCEPT header using `getOrThrow` and subsequently maps
     * each value to its corresponding `MediaType` representation.
     *
     * @return A list of `MediaType` objects parsed from the "ACCEPT" header values.
     * @throws NoSuchHeaderException if the header is missing.
     * @throws MalformedInputException if the header value is malformed.
     * @since 2.2.0
     */
    fun getAccept() = getOrThrow(ACCEPT).map { MediaType.parse(it)() }
    /**
     * Sets the "Accept" header with the provided media types.
     *
     * Allows specifying multiple media types that the client is willing to accept.
     *
     * @param values Vararg parameter representing the media types to set in the "Accept" header.
     * @since 2.2.0
     */
    fun setAccept(vararg values: MediaType) = set(ACCEPT, values.toList())

    /**
     * Parses and retrieves the `Accept-Language` header value as a list of language ranges.
     *
     * The method attempts to fetch the `Accept-Language` value and parse it into a list of
     * `LanguageRange` objects. If the header value is null, blank, or parsing fails, an
     * empty list is returned.
     *
     * @return A list of `LanguageRange` objects based on the `Accept-Language` header value,
     * or an empty list if the value is invalid or not present.
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    fun getAcceptLanguage(): List<LanguageRange> {
        val value = getFirstOrThrow(ACCEPT_LANGUAGE)
        return if (value.isNotNullOrBlank())
            tryOr({ emptyList() }) { LanguageRange.parse(value) }
        else emptyList()
    }
    /**
     * Sets the `Accept-Language` header with the specified language ranges and their weights.
     * This header is used to specify the preferred languages for response content.
     *
     * @param values A variable number of `LanguageRange` objects representing the desired languages
     *               and their respective weights. The weight is a decimal value between 0.0 and 1.0,
     *               where 1.0 is the highest preference.
     * @since 2.2.0
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
     * Converts the Accept-Language header information into a list of Locale objects.
     *
     * This method processes language ranges obtained from the Accept-Language
     * header, filters out entries with a wildcard ('*'), and converts the remaining
     * entries into Locale objects.
     *
     * @return a list of Locale objects derived from the Accept-Language header.
     *         Returns an empty list if the header contains no valid entries.
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    fun getAcceptLanguageAsLocales(): List<Locale> {
        val ranges = getAcceptLanguage()
        if (ranges.isEmpty()) return emptyList()
        val locales = emptyMList<Locale>()
        ranges.forEach { if (it.range notStartsWith Char.STAR) locales.add(Locale.forLanguageTag(it.range)) }
        return locales.toList()
    }
    /**
     * Updates the accept-language settings using the provided locales.
     * Converts the input locales into language ranges and applies them.
     *
     * @param values A vararg of `Locale` objects representing the languages to be set.
     * @since 2.2.0
     */
    fun setAcceptLanguageAsLocales(vararg values: Locale) =
        setAcceptLangauge(*values.map { LanguageRange(it.toLanguageTag()) }.toTypedArray())

    /**
     * Retrieves the "Accept-Patch" HTTP header value from the response and parses it into a list of MediaType objects.
     *
     * This method fetches the value associated with the "Accept-Patch" header by using `getOrThrow`
     * and then maps each value string into a `MediaType` object by applying the `MediaType.parse` parser.
     *
     * @return A list of parsed `MediaType` objects representing the values of the "Accept-Patch" header.
     * @throws NoSuchHeaderException if the header is missing.
     * @throws MalformedInputException if the header value is malformed.
     * @since 2.2.0
     */
    fun getAcceptPatch() = getOrThrow(ACCEPT_PATCH).map { MediaType.parse(it)() }
    /**
     * Sets the "Accept-Patch" header with the provided media types.
     *
     * The "Accept-Patch" header is used to specify the patch document media type(s)
     * that the server supports. This method takes a variable number of `MediaType`
     * arguments and sets them to the header.
     *
     * @param values A variable number of `MediaType` instances representing
     *               the media types to include in the "Accept-Patch" header.
     * @since 2.2.0
     */
    fun setAcceptPatch(vararg values: MediaType) = set(ACCEPT_PATCH, values.toList())

    /**
     * Retrieves the value of the "Access-Control-Allow-Credentials" header and converts it to a boolean.
     * This function ensures the presence of the header before attempting to convert its value.
     * Throws an exception if the header is missing.
     *
     * @return `true` if the "Access-Control-Allow-Credentials" header is present and its value is truthy, `false` otherwise.
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    fun getAccessControlAllowCredentials() = getFirstOrThrow(ACCESS_CONTROL_ALLOW_CREDENTIALS)
        .toBoolean()
    /**
     * Sets the Access-Control-Allow-Credentials header to indicate whether the response to
     * the request can be exposed when the credentials flag is true.
     *
     * @param allowCredentials A boolean value indicating whether the resource supports
     * credentials. If true, credentials are allowed; otherwise, they are not.
     * @since 2.2.0
     */
    fun setAccessControlAllowCredentials(allowCredentials: Boolean) = set(ACCESS_CONTROL_ALLOW_CREDENTIALS, allowCredentials.toString())

    /**
     * Retrieves the allowed HTTP methods from the "Access-Control-Allow-Methods" header.
     *
     * This method extracts the value of the `ACCESS_CONTROL_ALLOW_METHODS` header,
     * splits the value by commas, and maps each element to the corresponding
     * `HttpMethod` enum constant.
     *
     * @return A list of `HttpMethod` enum constants representing the allowed HTTP methods.
     * @throws dev.tommasop1804.kutils.exceptions.NoSuchEntryException if the header value cannot be parsed correctly.
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    fun getAccessControlAllowMethods() = getFirstOrThrow(ACCESS_CONTROL_ALLOW_METHODS)
        .split(Char.COMMA).map { it.toEnumConst<HttpMethod>() }
    /**
     * Sets the "Access-Control-Allow-Methods" header with the specified HTTP methods.
     *
     * This method is used to configure the allowed HTTP methods for Cross-Origin Resource Sharing (CORS).
     *
     * @param values The HTTP methods to be allowed, provided as vararg parameters. Each method is represented by an instance of [HttpMethod].
     * @since 2.2.0
     */
    fun setAccessControlAllowMethods(vararg values: HttpMethod) = set(ACCESS_CONTROL_ALLOW_METHODS, values.joinToString(String.COMMA, transform = HttpMethod::name))

    /**
     * Retrieves the maximum age for access control from a predefined configuration key.
     *
     * This method works by fetching the value associated with the `ACCESS_CONTROL_MAX_AGE` key,
     * converting it to a Long, and then interpreting it as a duration in seconds.
     *
     * @return The maximum age for access control as a duration in seconds.
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    fun getAccessControlMaxAge() = getFirstOrThrow(ACCESS_CONTROL_MAX_AGE)
        .toLong()
        .asSecondsOfDuration()
    /**
     * Sets the maximum age for the access control in seconds. This determines how long
     * the access control settings should be cached by the client.
     *
     * @param maxAge The maximum duration, as a [Duration], for which the access control settings
     *               are considered valid.
     * @since 2.2.0
     */
    @OptIn(RiskyApproximationOfTemporal::class)
    fun setAccessControlMaxAge(maxAge: Duration) = set(ACCESS_CONTROL_MAX_AGE, maxAge.toSeconds())

    /**
     * Retrieves the HTTP access control request method from the request headers.
     *
     * This method queries the header defined by `ACCESS_CONTROL_REQUEST_METHOD` and attempts to
     * convert its value to an enum constant of type [HttpMethod].
     *
     * @param value An instance of [HttpMethod] representing the required HTTP method type for mapping.
     * @return The corresponding [HttpMethod] enum constant determined from the header value.
     * @throws dev.tommasop1804.kutils.exceptions.NoSuchEntryException If the header value cannot be matched to a valid enum constant.
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    fun getAccessControlRequestMethod(value: HttpMethod) = getFirstOrThrow(ACCESS_CONTROL_REQUEST_METHOD)
        .toEnumConst<HttpMethod>()
    /**
     * Sets the `Access-Control-Request-Method` header for a preflight request in a CORS (Cross-Origin Resource Sharing) scenario.
     * This header indicates which HTTP method will be used during the actual request.
     *
     * @param value The HTTP method to set for the `Access-Control-Request-Method` header.
     * @since 2.2.0
     */
    fun setAccessControlRequestMethod(value: HttpMethod) = set(ACCESS_CONTROL_REQUEST_METHOD, value.name)

    /**
     * Retrieves a list of character sets from the "Accept-Charset" header value.
     *
     * This method parses the "Accept-Charset" header and returns a list of character sets
     * specified in the header. If the header contains the wildcard '*', it is ignored in the result.
     *
     * @return a list of character sets specified in the "Accept-Charset" header.
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    fun getAcceptCharset(): List<Charset> {
        val value = getFirstOrThrow(ACCEPT_CHARSET)
        val tokens = value / Char.COMMA
        val result = emptyMList<Charset>()
        for (token in tokens) {
            val paramIdx = ';'(token)
            val charsetName = if (paramIdx == -1) token
            else paramIdx(token)
            if (charsetName != String.STAR) result.add(Charset.forName(charsetName))
        }
        return result.toList()
    }
    /**
     * Sets the Accept-Charset header with the specified character sets.
     *
     * @param values A vararg of Charset objects to be included in the Accept-Charset header.
     * @since 2.2.0
     */
    fun setAcceptCharset(vararg values: Charset) = set(ACCEPT_CHARSET, values.joinToString(String.COMMA) { it.name().lowercase(Locale.ROOT) })

    /**
     * Retrieves and processes the "Allow" HTTP header field to produce a list of HTTP methods.
     *
     * The method obtains the value associated with the "Allow" header, splits it into a collection of 
     * comma-separated string tokens, trims any leading or trailing whitespace from each token, and
     * maps them to corresponding `HttpMethod` enum constants.
     *
     * @return A list of `HttpMethod` enum constants derived from the "Allow" header field.
     * @throws NoSuchHeaderException If the "Allow" header is not present in the data source.
     * @throws dev.tommasop1804.kutils.exceptions.NoSuchEntryException If any of the tokens cannot be mapped to a valid `HttpMethod` enum constant.
     * @since 2.2.0
     */
    fun getAllow() = getFirstOrThrow(ALLOW).splitAndTrim(Char.COMMA).map { it.toEnumConst<HttpMethod>() }
    /**
     * Configures the allowed HTTP methods for a specific resource or endpoint.
     *
     * @param values A variable number of HTTP methods to allow, specified as instances of [HttpMethod].
     * @since 2.2.0
     */
    fun setAllow(vararg values: HttpMethod) = set(ALLOW, values.joinToString(String.COMMA, transform = HttpMethod::name))

    /**
     * Retrieves the Bearer authentication token from the authorization header.
     * This method extracts the first value found in the AUTHORIZATION header and converts it
     * to a JWT (JSON Web Token) representation.
     *
     * @return The JWT token extracted from the AUTHORIZATION header.
     * @throws NoSuchHeaderException if the AUTHORIZATION header is missing or invalid.
     * @since 2.2.0
     */
    fun getBearerAuth() = getFirstOrThrow(AUTHORIZATION).toJWT()()
    /**
     * Sets the Bearer Authorization header with the provided JWT token.
     *
     * @param token The JWT token to use in the Authorization header.
     * @since 2.2.0
     */
    fun setBearerAuth(token: JWT) = set(AUTHORIZATION, token.toString(true))

    /**
     * Sets the Authorization header to use Basic Authentication with the provided encoded credentials.
     *
     * The provided string should be a Base64-encoded combination of the username and password, formatted as "username:password".
     * This method adheres to the Basic Authentication standard and updates the Authorization header accordingly.
     *
     * @param encodedCredentials The Base64-encoded "username:password" string to be used for Basic Authentication.
     * @since 2.2.0
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
     * @since 2.2.0
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
     * Retrieves the content language as a Locale object based on the CONTENT_LANGUAGE value.
     *
     * @return the Locale corresponding to the content language tag.
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    fun getContentLanguage(): Locale = getFirstOrThrow(CONTENT_LANGUAGE).let(Locale::forLanguageTag)
    /**
     * Sets the content language for the current operation or request. 
     * The language is specified using a Locale object and will be 
     * transformed to its corresponding language tag.
     *
     * @param value the Locale representing the language to set as the content language.
     * @since 2.2.0
     */
    fun setContentLanguage(value: Locale) = set(CONTENT_LANGUAGE, value.toLanguageTag())

    /**
     * Retrieves the content length from a predefined source and converts it 
     * to a numerical value represented in bytes.
     *
     * This function fetches the value associated with the `CONTENT_LENGTH` key 
     * using the `getFirstOrThrow` method. The returned value is then converted 
     * to a `Long` representing the size in bytes. The unit of the result is 
     * `MeasureUnit.DataSizeUnit.BYTE`.
     *
     * @return The content length in bytes as a `Long`.
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    fun getContentLength() = getFirstOrThrow(CONTENT_LENGTH).toLong() ofUnit MeasureUnit.DataSizeUnit.BYTE
    /**
     * Sets the content length of a data transfer operation.
     *
     * @param value The content length to set. The value is specified as a `DataSize` object 
     *              and will be internally converted to bytes.
     * @since 2.2.0
     */
    fun setContentLength(value: DataSize) = set(CONTENT_LENGTH, value.convertTo(MeasureUnit.DataSizeUnit.BYTE)().value)
    /**
     * Sets the value of the Content-Length header for a request.
     *
     * @param bytes The length of the content in bytes to be set in the Content-Length header.
     * @since 2.2.0
     */
    fun setContentLength(bytes: Long) = set(CONTENT_LENGTH, bytes)

    /**
     * Retrieves the content type from a predefined constant or configuration. This method extracts 
     * the first occurrence of the content type value and parses it into a `MediaType` object.
     *
     * @return A `MediaType` object representing the parsed content type.
     * @throws MalformedInputException if the content type cannot be parsed.
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    fun getContentType() = getFirstOrThrow(CONTENT_TYPE).let { MediaType.parse(it)() }
    /**
     * Sets the content type for the request or response by assigning a specified MediaType value.
     *
     * @param value The MediaType object representing the MIME type to be set as the content type.
     * @since 2.2.0
     */
    fun setContentType(value: MediaType) = set(CONTENT_TYPE, value.toString())

    /**
     * Retrieves a date in the form of an Instant by parsing the first occurrence
     * of a date string that matches the RFC 7231 date-time format.
     *
     * @return an Instant representing the parsed date and time.
     * @throws NoSuchHeaderException if the header is missing.
     * @throws java.time.format.DateTimeParseException if the date string cannot be parsed.
     * @since 2.2.0
     */
    fun getDate(): Instant = getFirstOrThrow(DATE).let { RFC_7231_DATE_TIME_FORMATTER.parse(it, Instant::from) }
    /**
     * Sets the date to the specified value using the provided TemporalAccessor and formats it 
     * using the RFC_7231_DATE_TIME_FORMATTER.
     *
     * @throws java.time.DateTimeException if the provided TemporalAccessor cannot be formatted as a header date.
     * @param value the TemporalAccessor representation of the date to be set
     * @since 2.2.0
     */
    fun setDate(value: TemporalAccessor = Instant()) = set(DATE, RFC_7231_DATE_TIME_FORMATTER(value))

    /**
     * Retrieves the expiry date and time as an Instant.
     * Parses the content of the "EXPIRES" field according to the RFC 7231 date-time format.
     *
     * @return the expiration date and time as an Instant
     * @throws NoSuchHeaderException if the header is missing.
     * @throws java.time.format.DateTimeParseException if the date string cannot be parsed.
     * @since 2.2.0
     */
    fun getExpires(): Instant = getFirstOrThrow(EXPIRES).let { RFC_7231_DATE_TIME_FORMATTER.parse(it, Instant::from) }
    /**
     * Sets the expiration date for this object using the provided temporal accessor.
     * The expiration date is formatted according to the RFC 7231 date-time standard.
     *
     * @param value the temporal accessor representing the expiration date and time
     * @throws java.time.DateTimeException if the provided temporal accessor cannot be formatted as a header date.
     * @since 2.2.0
     */
    fun setExpires(value: TemporalAccessor) = set(EXPIRES, RFC_7231_DATE_TIME_FORMATTER(value))

    /**
     * Retrieves the host information in the form of an unresolved `InetSocketAddress`.
     *
     * The method parses a host string and identifies both the host name and port, when available.
     * If the host string is enclosed in square brackets (e.g., for IPv6 addresses), 
     * it handles the enclosed formatting appropriately to extract the host and port.
     * In case the host string does not specify a port, the port defaults to `0`.
     *
     * @return an `InetSocketAddress` instance with the extracted host and port, or a host with port `0` if no port is specified.
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    @Suppress("kutils_substring_as_int_invoke")
    fun getHost(): InetSocketAddress = getFirstOrThrow(HOST).let {
        var host: String? = null
        var port = 0
        val separator = if (it startsWith '[') it.indexOf(Char.COLON, ']'(it)) else it.lastIndexOf(Char.COLON)
        if (separator != -1) {
            host = separator(it)
            val portString = it.substring(separator + 1)
            tryOrNull { port = portString.toInt() }
        }
        if (host.isNull()) host = it
        InetSocketAddress.createUnresolved(host, port)
    }
    /**
     * Sets the host value in the configuration.
     *
     * @param host the socket address containing the host name or IP address 
     * and port that will be set as the host value.
     * @since 2.2.0
     */
    fun setHost(host: InetSocketAddress) {
        var value = host.hostString
        val port = host.port
        if (port != 0) value = "$value:$port"
        set(HOST, value)
    }

    /**
     * Retrieves the value of the `If-Modified-Since` HTTP header as an `Instant`.
     * If the header value is present, it is parsed using the RFC 7231 date-time format.
     * Throws an exception if the header is missing or cannot be parsed.
     *
     * @return The `Instant` representation of the `If-Modified-Since` header value.
     * @throws NoSuchHeaderException if the header is missing.
     * @throws java.time.format.DateTimeParseException if the date string cannot be parsed.
     * @since 2.2.0
     */
    fun getIfModifiedSince(): Instant = getFirstOrThrow(IF_MODIFIED_SINCE).let { RFC_7231_DATE_TIME_FORMATTER.parse(it, Instant::from) }
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
     * @since 2.2.0
     */
    fun setIfModifiedSince(value: TemporalAccessor) = set(IF_MODIFIED_SINCE, RFC_7231_DATE_TIME_FORMATTER(value))

    /**
     * Parses the value associated with the "If-Unmodified-Since" header and returns it as an Instant.
     *
     * This method retrieves the "If-Unmodified-Since" header value, processes it using the 
     * RFC 7231 Date-Time formatter, and converts it to an Instant object.
     *
     * @return an Instant representing the parsed "If-Unmodified-Since" header value
     * @throws NoSuchHeaderException if the header is missing.
     * @since 2.2.0
     */
    fun getIfUnmodifiedSince(): Instant = getFirstOrThrow(IF_UNMODIFIED_SINCE).let { RFC_7231_DATE_TIME_FORMATTER.parse(it, Instant::from) }
    /**
     * Sets the "If-Unmodified-Since" condition for a request. This determines the validity of the operation 
     * based on a specific timestamp. The server processes the request only if the resource has not been 
     * modified since the given timestamp.
     *
     * @param value the timestamp to compare against, specified as a [TemporalAccessor]. This value is formatted 
     *              according to the RFC 7231 standard before being applied to the request.
     * @throws java.time.DateTimeException if the provided temporal accessor cannot be formatted as a header date.
     * @since 2.2.0
     */
    fun setIfUnmodifiedSince(value: TemporalAccessor) = set(IF_UNMODIFIED_SINCE, RFC_7231_DATE_TIME_FORMATTER(value))

    /**
     * Retrieves the timestamp of the last modification.
     *
     * This method parses the value associated with the LAST_MODIFIED field and converts it 
     * into an Instant object following the RFC 7231 date-time format.
     *
     * @return an Instant representing the last modified timestamp.
     * @throws NoSuchHeaderException if the header is missing.
     * @throws java.time.format.DateTimeParseException if the date string cannot be parsed.
     * @since 2.2.0
     */
    fun getLastModified(): Instant = getFirstOrThrow(LAST_MODIFIED).let { RFC_7231_DATE_TIME_FORMATTER.parse(it, Instant::from) }
    /**
     * Sets the value of the `LAST_MODIFIED` field using the specified temporal accessor.
     *
     * This method converts the provided `TemporalAccessor` into a string formatted according to the
     * RFC 7231 date-time standard and assigns it to the `LAST_MODIFIED` field.
     *
     * @param value the temporal accessor representing the date and time to set as the last modified timestamp
     * @throws java.time.DateTimeException if the provided temporal accessor cannot be formatted as a header date.
     * @since 2.2.0
     */
    fun setLastModified(value: TemporalAccessor = Instant()) = set(LAST_MODIFIED, RFC_7231_DATE_TIME_FORMATTER(value))

    /**
     * Retrieves the location as a URI object.
     * 
     * This method fetches the first occurrence of the `LOCATION` key and attempts 
     * to convert it into a URI. If the key is missing or the value cannot 
     * be processed, an exception may be thrown.
     * 
     * @return the location as a URI
     * @throws NoSuchElementException if the `LOCATION` key is not found
     * @throws IllegalArgumentException if the value cannot be converted to a valid URI
     * @since 2.2.0
     */
    fun getLocation() = getFirstOrThrow(LOCATION).toURI()()
    /**
     * Sets the location URI for the given key.
     * 
     * @param value The URI to be set as the location.
     * @since 2.2.0
     */
    fun setLocation(value: URI) = set(LOCATION, value.toString())
}