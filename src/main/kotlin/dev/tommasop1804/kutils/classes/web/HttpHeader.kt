package dev.tommasop1804.kutils.classes.web

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.coding.JSON.Companion.toJSON
import dev.tommasop1804.kutils.classes.time.TimeZone
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.time.Instant
import java.time.ZoneOffset.UTC
import java.time.temporal.TemporalAccessor

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
class HttpHeader(val name: String, val values: StringList) : StringList by values {
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
    constructor(pair: Pair<String, StringList>) : this(pair.first, pair.second)
    /**
     * Secondary constructor for the HttpHeader class that initializes
     * the object using an existing Map.Entry instance.
     *
     * @param entry The Map.Entry containing a header name as the key and
     *              a StringList representing the header values as the value.
     * @since 2.1.0
     */
    constructor(entry: Map.Entry<String, StringList>) : this(entry.key, entry.value)

    /**
     * Initializes the HttpHeader instance with the specified name and value.
     * The provided value is converted into a singleton list before being
     * assigned to the header's value field.
     *
     * @param name The name of the HTTP header.
     * @param value The value of the HTTP header, which will be converted
     * into a singleton list.
     * @since 2.1.0
     */
    constructor(name: String, vararg value: Any) : this(name, value.map(Any::toString).toList())

    constructor(notation: String) : this(
        notation.splitAndTrim(Char.COLON, limit = 2).first(),
        notation.splitAndTrim(Char.COLON, limit = 2).second()
    )

    init {
        validate(name.isNotBlank()) { "Header name cannot be blank" }
        name.validateInputFormat(Regex("^[a-zA-Z0-9!#$%&'*+\\-.^_`|~]+$"), "Header name must be valid")

        validate(values.isNotEmpty()) { "Header value cannot be empty" }
        values.forEach { it.validateInputFormat(Regex("^[\\x21-\\x7E\\x80-\\xFF\\t ]*$"), "Header value must be valid") }
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
 * Represents a collection of HTTP headers that conforms to the `MultiStringMap` interface
 * and delegates collection operations to a list of `HttpHeader` instances.
 *
 * The class supports common operations for accessing and managing headers, including
 * key-based lookups, type conversion, and conversion to various collection types.
 *
 * @constructor Constructs an `HttpHeaders` instance with the provided headers.
 * @param headers Vararg parameter representing the headers to include in the collection.
 * @since 2.1.0
 */
@Suppress("unused", "JavaDefaultMethodsNotOverriddenByDelegation")
@JsonSerialize(using = HttpHeaders.Companion.Serializer::class)
@JsonDeserialize(using = HttpHeaders.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = HttpHeaders.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = HttpHeaders.Companion.OldDeserializer::class)
class HttpHeaders(val headers: Set<HttpHeader>) : MultiStringMap, Collection<HttpHeader> by headers {
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
    constructor(vararg headers: HttpHeader) : this(headers.toSet())
    
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
    constructor(header: HttpHeader) : this(header.asSingleSet())

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
    constructor(name: String, values: StringList) : this(HttpHeader(name, values))
    /**
     * Constructs an `HttpHeaders` instance by creating an `HttpHeader` object
     * with the specified name and value.
     *
     * This constructor serves as a shorthand for initializing an `HttpHeaders` object
     * with a single `HttpHeader` with the given name and value. The `HttpHeader` is created
     * internally using the provided parameters.
     *
     * @param name The name of the HTTP header.
     * @param value The value of the HTTP header.
     * @since 2.1.0
     */
    constructor(name: String, vararg value: Any) : this(HttpHeader(name, value.map(Any::toString).toList()))

    /**
     * Constructs an instance using the provided HTTP header notation string.
     *
     * @param notation A string representing the HTTP header in a specific notation.
     * @since 2.1.0
     */
    constructor(notation: String) : this(HttpHeader(notation))

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
        fun MultiStringMap.toHttpHeaders(): HttpHeaders = HttpHeaders(map { HttpHeader(it.key, it.value) }.toSet())
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
        fun DataMapNN.toHttpHeaders(): HttpHeaders = HttpHeaders(map { HttpHeader(it.key, it.value) }.toSet())

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
        fun Iterable<HttpHeader>.toHttpHeaders() = HttpHeaders(toSet())

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
     * Retrieves the first value associated with the specified key from the headers.
     * Searches through the headers, finds the header that matches the given key,
     * and returns the first value from the matched header.
     *
     * @param key The key used to locate the matching header.
     * @return The first value corresponding to the specified key.
     * @throws NoSuchElementException If no header matches the given key.
     * @since 2.1.0
     */
    fun getFirst(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.first()
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
    inline fun <reified T> getFirstTyped(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.first().serialize().deserialize<T>()
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
    inline fun <reified T> getFirstTypedUnsafe(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.first().serialize().deserialize<T>()()

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
    fun getFirstOrThrow(key: String, lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.firstOrThrow(lazyException)
    /**
     * Retrieves and deserializes the first value associated with the specified key from the headers
     * into the expected type [T]. Throws a lazily-supplied exception if the key or value is not found.
     *
     * @param key The key to search for in the headers.
     * @param lazyException A supplier function that provides the exception to be thrown 
     *                      if the key or value is not found.
     * @since 2.1.0
     */
    inline fun <reified T> getFirstTypedOrThrow(key: String, noinline lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.firstOrThrow(lazyException).serialize().deserialize<T>()
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
    inline fun <reified T> getFirstTypedUnsafeOrThrow(key: String, noinline lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.firstOrThrow(lazyException).serialize().deserialize<T>()()

    /**
     * Retrieves the first value associated with the specified key from the headers. 
     * If no matching key-value pair is found, the supplied default value is returned.
     *
     * @param key The key to search for in the headers.
     * @param default A supplier function that provides a default value when the key is not found.
     * @return The first value associated with the specified key, or the value from the default supplier if the key is not present.
     * @since 2.1.0
     */
    fun getFirstOr(key: String, default: Supplier<String>) = headers.findOrThrow { it.nameEquals(key) }.values.firstOr(default)
    /**
     * Retrieves the first value associated with the specified key or a typed default value if the key is not found.
     * The value is serialized and deserialized into the specified type `T`.
     *
     * @param key The key to search for in the headers.
     * @param default A supplier providing a default value of type `T` if the key is not found.
     * @since 2.1.0
     */
    inline fun <reified T> getFirstTypedOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow { it.nameEquals(key) }.values.firstOr(default).serialize().deserialize<T>()
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
    inline fun <reified T> getFirstTypedUnsafeOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow { it.nameEquals(key) }.values.firstOr(default).serialize().deserialize<T>()()

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
    fun getSecond(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.second()
    /**
     * Retrieves the second value associated with the given key from headers, serializes 
     * it, and deserializes it into the specified type.
     *
     * @param key The key whose second associated value is to be retrieved.
     * @return The second value associated with the key, deserialized into the specified type.
     * @since 2.1.0
     */
    inline fun <reified T> getSecondTyped(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.second().serialize().deserialize<T>()
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
    inline fun <reified T> getSecondTypedUnsafe(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.second().serialize().deserialize<T>()()

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
    fun getSecondOrThrow(key: String, lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.secondOrThrow(lazyException)
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
    inline fun <reified T> getSecondTypedOrThrow(key: String, noinline lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.secondOrThrow(lazyException).serialize().deserialize<T>()
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
    inline fun <reified T> getSecondTypedUnsafeOrThrow(key: String, noinline lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.secondOrThrow(lazyException).serialize().deserialize<T>()()

    /**
     * Retrieves the second value associated with the specified header key, or falls back to the default value if no such value exists.
     *
     * @param key The header key used to search for the desired entry.
     * @param default A supplier that provides a fallback value if the second value is not found.
     * @return The second value corresponding to the header key, or the provided default value.
     * @throws NoSuchElementException If the key is not found in the headers.
     * @since 2.1.0
     */
    fun getSecondOr(key: String, default: Supplier<String>) = headers.findOrThrow { it.nameEquals(key) }.values.secondOr(default)
    /**
     * Retrieves the second value associated with the given key from the headers, converting it to the specified type.
     * If the second value is not present, the provided default value is used instead.
     *
     * @param key The key whose associated value is to be fetched.
     * @param default A supplier that provides a default value to be used if the second value does not exist.
     * @return The second value associated with the key, converted to the type `T`, or the provided default value.
     * @since 2.1.0
     */
    inline fun <reified T> getSecondTypedOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow { it.nameEquals(key) }.values.secondOr(default).serialize().deserialize<T>()
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
    inline fun <reified T> getSecondTypedUnsafeOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow { it.nameEquals(key) }.values.secondOr(default).serialize().deserialize<T>()()

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
    fun getThird(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.third()
    /**
     * Retrieves and deserializes the third value corresponding to the specified key
     * from the headers. The value is*/
    inline fun <reified T> getThirdTyped(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.third().serialize().deserialize<T>()
    /**
     * Retrieves the third value associated with the specified key, attempts to serialize and 
     * deserialize it into the provided type [T], and returns it. This method is considered unsafe 
     * as it assumes the existence of the key, a third value, and successful deserialization to [T].
     *
     * @param key The key to locate in the headers.
     * @return The deserialized third value of type [T] associated with the given key.
     * @throws IllegalStateException If the key is not found in the headers or if a third value does not exist.
     * @throws SerializationException If the serialization or deserialization fails.
     *
     * @since 2.1.0
     */
    inline fun <reified T> getThirdTypedUnsafe(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.third().serialize().deserialize<T>()()

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
    fun getThirdOrThrow(key: String, lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.thirdOrThrow(lazyException)
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
    inline fun <reified T> getThirdTypedOrThrow(key: String, noinline lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.thirdOrThrow(lazyException).serialize().deserialize<T>()
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
    inline fun <reified T> getThirdTypedUnsafeOrThrow(key: String, noinline lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.thirdOrThrow(lazyException).serialize().deserialize<T>()()

    /**
     * Retrieves the third value associated with the given key from the headers.
     * If the third value is not available, the provided default supplier is used to supply the fallback value.
     *
     * @param key The key used to locate the header values.
     * @param default A supplier function that provides a default value if the third value is unavailable.
     * @since 2.1.0
     */
    fun getThirdOr(key: String, default: Supplier<String>) = headers.findOrThrow { it.nameEquals(key) }.values.thirdOr(default)
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
    inline fun <reified T> getThirdTypedOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow { it.nameEquals(key) }.values.thirdOr(default).serialize().deserialize<T>()
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
    inline fun <reified T> getThirdTypedUnsafeOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow { it.nameEquals(key) }.values.thirdOr(default).serialize().deserialize<T>()()

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
    inline fun <reified T> getOnlyElementTyped(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.onlyElement().serialize().deserialize<T>()
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
    inline fun <reified T> getOnlyElementTypedUnsafe(key: String) = headers.findOrThrow { it.nameEquals(key) }.values.onlyElement().serialize().deserialize<T>()()

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
    fun getOnlyElementOrThrow(key: String, lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.onlyElementOrThrow(lazyException)
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
    inline fun <reified T> getOnlyElementTypedOrThrow(key: String, noinline lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.onlyElementOrThrow(lazyException).serialize().deserialize<T>()
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
    inline fun <reified T> getOnlyElementTypedUnsafeOrThrow(key: String, noinline lazyException: ThrowableSupplier) = headers.findOrThrow(lazyException) { it.nameEquals(key) }.values.onlyElementOrThrow(lazyException).serialize().deserialize<T>()()

    /**
     * Retrieves the only occurrence of a value associated with the specified key. If no such element
     * exists, the default value provided by the supplied function is returned.
     *
     * @param key The key whose associated single element value is to be retrieved.
     * @param default A supplier function that provides a default value if no element is found.
     * @return The value associated with the key if found, otherwise the supplied default value.
     * @since 2.1.0
     */
    fun getOnlyElementOr(key: String, default: Supplier<String>) = headers.findOrThrow { it.nameEquals(key) }.values.firstOr(default)
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
    inline fun <reified T> getOnlyElementTypedOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow { it.nameEquals(key) }.values.onlyElementOr(default).serialize().deserialize<T>()
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
    inline fun <reified T> getOnlyElementTypedUnsafeOr(key: String, noinline default: Supplier<T>) = headers.findOrThrow { it.nameEquals(key) }.values.onlyElementOr(default).serialize().deserialize<T>()()

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

    fun with(vararg header: HttpHeader) = HttpHeaders(headers.toList().plus(header).toSet())

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
        HttpHeaders(headers.plus<HttpHeader>(other))
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
    operator fun minus(other: HttpHeader) = HttpHeaders(headers.filterNot { it == other }.toSet())
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
    operator fun minus(other: HttpHeaders) = HttpHeaders(headers.filterNot { h -> other.headers.any { h == it } }.toSet())
    /**
     * Removes all headers from the collection that match the specified key.
     *
     * The resulting collection will exclude any headers whose name equals the given key.
     *
     * @param key The name of the header to be removed.
     * @return A new collection of headers with the specified key excluded.
     * @since 2.1.0
     */
    operator fun minus(key: String) = HttpHeaders(headers.filterNot { it.name == key }.toSet())
    /**
     * Subtracts the specified keys from the current HTTP headers and returns a new `HttpHeaders` instance
     * containing the remaining headers.
     *
     * @param keys A list of keys to be removed from the current headers.
     * @since 2.1.0
     */
    operator fun minus(keys: Iterable<String>) = HttpHeaders(headers.filterNot { it.name in keys }.toSet())

    /**
     * Creates a new [HttpHeader] instance by overriding the properties of the given [HttpHeader].
     *
     * @param other The [HttpHeader] instance whose properties are used to create a new instance.
     * @since 2.1.0
     */
    fun override(other: HttpHeader) = HttpHeader(other.name, other.values)

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
}