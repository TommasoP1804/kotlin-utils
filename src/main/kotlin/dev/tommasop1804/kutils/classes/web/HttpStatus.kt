/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.web

import dev.tommasop1804.kutils.*

/**
 * Represents the enumeration of HTTP status codes as defined by the HTTP standard.
 *
 * Each constant in this enum corresponds to a unique HTTP status code, categorized
 * into predefined families for better classification and handling. The enum also includes
 * additional metadata such as the status code's integer value and its descriptive text message.
 *
 * This enum is helpful for standardization and programmatic comparison of HTTP status codes
 * and their characteristics across various layers of an application.
 *
 * @property value The numerical representation of the HTTP status code.
 * @property family The category (family) that this status code belongs to.
 * @property reasonPhrase A brief textual description associated with the HTTP status code.
 * @since 2.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
enum class HttpStatus(val value: Int, val family: Family, val reasonPhrase: String) {
    Continue(100, Family.Informational, "Continue"),
    SwitchingProtocols(101, Family.Informational, "Switching Protocols"),
    @Deprecated("Removed from WebDAV specification") Processing(102, Family.Informational, "Processing"),
    EarlyHints(103, Family.Informational, "Early Hints"),

    Ok(200, Family.Successful, "OK"),
    Created(201, Family.Successful, "Created"),
    Accepted(202, Family.Successful, "Accepted"),
    NonAuthoritativeInformation(203, Family.Successful, "Non-Authoritative Information"),
    NoContent(204, Family.Successful, "No Content"),
    ResetContent(205, Family.Successful, "Reset Content"),
    PartialContent(206, Family.Successful, "Partial Content"),
    MultiStatus(207, Family.Successful, "Multi-Status"),
    AlreadyReported(208, Family.Successful, "Already Reported"),
    ImUsed(226, Family.Successful, "IM Used"),

    MultipleChoices(300, Family.Redirection, "Multiple Choices"),
    MovedPermanently(301, Family.Redirection, "Moved Permanently"),
    Found(302, Family.Redirection, "Found"),
    SeeOther(303, Family.Redirection, "See Other"),
    NotModified(304, Family.Redirection, "Not Modified"),
    @Deprecated("Deprecated status code") UseProxy(305, Family.Redirection, "Use Proxy"),
    @Deprecated("Deprecated status code", level = DeprecationLevel.ERROR) SwitchProxy(306, Family.Redirection, "Switch Proxy"),
    TemporaryRedirect(307, Family.Redirection, "Temporary Redirect"),
    PermanentRedirect(308, Family.Redirection, "Permanent Redirect"),

    BadRequest(400, Family.ClientError, "Bad Request"),
    Unauthorized(401, Family.ClientError, "Unauthorized"),
    PaymentRequired(402, Family.ClientError, "Payment Required"),
    Forbidden(403, Family.ClientError, "Forbidden"),
    NotFound(404, Family.ClientError, "Not Found"),
    MethodNotAllowed(405, Family.ClientError, "Method Not Allowed"),
    NotAcceptable(406, Family.ClientError, "Not Acceptable"),
    ProxyAuthenticationRequired(407, Family.ClientError, "Proxy Authentication Required"),
    RequestTimeout(408, Family.ClientError, "Request Timeout"),
    Conflict(409, Family.ClientError, "Conflict"),
    Gone(410, Family.ClientError, "Gone"),
    LengthRequired(411, Family.ClientError, "Length Required"),
    PreconditionFailed(412, Family.ClientError, "Precondition Failed"),
    ContentTooLarge(413, Family.ClientError, "Payload Too Large"),
    UriTooLong(414, Family.ClientError, "URI Too Long"),
    UnsupportedMediaType(415, Family.ClientError, "Unsupported Media Type"),
    RangeNotSatisfiable(416, Family.ClientError, "Range Not Satisfiable"),
    ExpectationFailed(417, Family.ClientError, "Expectation Failed"),
    @Deprecated("Deprecated status code", level = DeprecationLevel.ERROR) IAmATeapot(418, Family.ClientError, "I'm a teapot"),
    MisdirectedRequest(421, Family.ClientError, "Misdirected Request"),
    UnprocessableContent(422, Family.ClientError, "Unprocessable Content"),
    Locked(423, Family.ClientError, "Locked"),
    FailedDependency(424, Family.ClientError, "Failed Dependency"),
    TooEarly(425, Family.ClientError, "Too Early"),
    UpgradeRequired(426, Family.ClientError, "Upgrade Required"),
    PreconditionRequired(428, Family.ClientError, "Precondition Required"),
    TooManyRequests(429, Family.ClientError, "Too Many Requests"),
    RequestHeaderFieldsTooLarge(431, Family.ClientError, "Request Header Fields Too Large"),
    UnavailableForLegalReasons(451, Family.ClientError, "Unavailable For Legal Reasons"),

    InternalServerError(500, Family.ServerError, "Internal Server Error"),
    NotImplemented(501, Family.ServerError, "Not Implemented"),
    BadGateway(502, Family.ServerError, "Bad Gateway"),
    ServiceUnavailable(503, Family.ServerError, "Service Unavailable"),
    GatewayTimeout(504, Family.ServerError, "Gateway Timeout"),
    HttpVersionNotSupported(505, Family.ServerError, "HTTP Version Not Supported"),
    VariantAlsoNegotiates(506, Family.ServerError, "Variant Also Negotiates"),
    InsufficientStorage(507, Family.ServerError, "Insufficient Storage"),
    LoopDetected(508, Family.ServerError, "Loop Detected"),
    NotExtended(510, Family.ServerError, "Not Extended"),
    NetworkAuthenticationRequired(511, Family.ServerError, "Network Authentication Required");

    /**
     * Indicates whether the current HTTP status is marked as deprecated.
     *
     * This property determines if the HTTP status has the `@Deprecated` annotation applied to it.
     * It serves to highlight statuses that are no longer recommended for use and may be
     * phased out in future versions.
     *
     * The value is computed dynamically by checking for the presence of the `@Deprecated` annotation
     * in the class metadata.
     *
     * @return true if the HTTP status is marked as deprecated, false otherwise.
     * @since 2.0.0
     */
    val isDeprecated get() = hasAnnotation(Deprecated::class)

    /**
     * Indicates whether the HTTP status belongs to the "INFORMATIONAL" family.
     *
     * The "INFORMATIONAL" family includes HTTP status codes in the 1xx range, which signify provisional
     * responses. These responses typically inform the client about the interim status of the request
     * and may require further action to proceed.
     *
     * This property evaluates to `true` if the status code's family is `INFORMATIONAL`, and `false` otherwise.
     * @since 2.0.0
     */
    val isInformational get() = family == Family.Informational
    /**
     * Indicates whether the HTTP status code belongs to the "SUCCESSFUL" category.
     *
     * This property evaluates to true if the HTTP status code is categorized under the "SUCCESSFUL"
     * family, which represents status codes indicating that the client's request was successfully
     * received, understood, and accepted by the server. Examples of such status codes include 200 (OK),
     * 201 (Created), and 204 (No Content).
     * @since 2.0.0
     */
    val isSuccessful get() = family == Family.Successful
    /**
     * Indicates whether the HTTP status code is classified under the redirection family.
     *
     * A redirection status implies that further action is required by the client to complete
     * the request. This typically involves redirecting the client to another resource, often
     * indicated by a Location header in the response.
     *
     * The redirection family corresponds to HTTP status codes where the family is categorized
     * as `Family.REDIRECTION`.
     * @since 2.0.0
     */
    val isRedirection get() = family == Family.Redirection
    /**
     * Indicates whether the HTTP status code belongs to the client error category.
     *
     * This property evaluates to `true` if the status code is part of the `CLIENT_ERROR` family,
     * which covers status codes ranging from 400 to 499. These status codes typically denote
     * issues caused by the client, such as malformed requests, unauthorized access, or forbidden actions.
     * @since 2.0.0
     */
    val isClientError get() = family == Family.ClientError
    /**
     * Indicates whether the HTTP status belongs to the server error category.
     *
     * A server error is represented by HTTP response status codes in the range of 500 to 599.
     * These codes signify that the server encountered an issue or is unable to perform the request.
     * The property evaluates to `true` if the `family` of the HTTP status is `SERVER_ERROR`.
     * @since 2.0.0
     */
    val isServerError get() = family == Family.ServerError

    /**
     * Indicates whether the HTTP status represents an error condition.
     *
     * This property is computed as a logical OR between `isClientError` and `isServerError`.
     * It evaluates to `true` if the HTTP status code signifies either a client error
     * (4xx status codes) or a server error (5xx status codes).
     * @since 2.0.0
     */
    val isError get() = isClientError || isServerError

    companion object {
        /**
         * Searches for an entry in the `entries` collection that has a value matching the given code.
         *
         * @param code The integer value to search for in the `entries` collection.
         * @return The entry where the value matches the given code, or null if no match is found.
         * @since 2.0.0
         */
        infix fun of(code: Int) = entries.find { it.value == code }
        /**
         * Finds an entry in the `entries` collection where the `value` matches the provided code
         * converted to an integer.
         *
         * @param code The long value to be searched for in the `entries` collection. It is converted
         *             to an integer before comparison.
         * @return The first entry in the `entries` collection that matches the converted code, or null if no match is found.
         * @since 2.0.0
         */
        infix fun of(code: Long) = entries.find { it.value == code.toInt() }
        /**
         * Finds an HTTP status entry whose reason phrase matches the given phrase.
         *
         * @param phrase The reason phrase to search for within the HTTP status entries.
         * @return The HTTP status entry with the corresponding reason phrase, or null if no match is found.
         * @since 2.0.0
         */
        infix fun ofReasonPhrase(phrase: String) = entries.find { it.reasonPhrase == phrase }
        /**
         * Filters the current collection of HTTP status entries to include only those
         * that belong to the specified family of HTTP status codes.
         *
         * @param family The HTTP status code family to filter by. This value is used
         *               to match and include only the entries corresponding to the specified family.
         * @return A filtered list of entries where each entry's `family` matches the
         *         provided `family` parameter.
         * @since 2.0.0
         */
        infix fun byFamily(family: Family) = entries.filter { it.family == family }

        /**
         * Converts an integer HTTP status code into its corresponding `HttpStatus` instance.
         *
         * This method attempts to find and return the `HttpStatus` whose `value` matches the given integer code.
         * If no matching status is found, the result will be `null`.
         *
         * @receiver The integer HTTP status code to be converted.
         * @return The corresponding `HttpStatus` instance if a match is found, or `null` otherwise.
         * @since 2.0.0
         */
        fun Int.toHttpStatus() = of(this)
        /**
         * Converts the current Long value into an HttpStatus object, if a matching status exists.
         *
         * This function attempts to map the Long value, representing an HTTP status code,
         * to a corresponding `HttpStatus` instance, based on the `value` property
         * of all available `HttpStatus` entries.
         *
         * @receiver The Long value representing an HTTP status code.
         * @return The corresponding `HttpStatus` instance if the `value` matches; otherwise, null.
         * @since 2.0.0
         */
        fun Long.toHttpStatus() = of(this)
        /**
         * Converts a given HTTP reason phrase into its corresponding `HttpStatus` enumeration.
         *
         * This method matches the provided string against the reason phrase of known HTTP status codes
         * and returns the matching `HttpStatus` instance if found. If no match is found, the result is `null`.
         *
         * This utility provides a convenient way to look up the `HttpStatus` based on textual descriptions
         * (e.g., "Not Found" for status code 404) without directly referencing the numerical codes.
         *
         * @receiver A string representing the HTTP reason phrase to be matched.
         * @return The corresponding `HttpStatus` instance if a match is found, otherwise `null`.
         * @since 2.0.0
         */
        fun String.toHttpStatus() = ofReasonPhrase(this)
    }

    /**
     * Converts the current `HttpStatus` object to its string representation.
     *
     * The representation includes the status code (`value`) and the reason phrase (`reasonPhrase`)
     * associated with the HTTP status.
     *
     * @return A string in the format "value reasonPhrase" where `value` is the HTTP status code
     *         and `reasonPhrase` is the corresponding textual description.
     * @since 2.0.0
     */
    override fun toString() = "$value $reasonPhrase"

    /**
     * Extracts the `value` property of the `HttpStatus` instance as a component in a destructuring declaration.
     *
     * This method enables the use of destructuring declarations to retrieve the `value` field of the `HttpStatus` object.
     *
     * @return The HTTP status code represented by the `value` field.
     * @since 3.1.0
     */
    operator fun component1() = value
    /**
     * Retrieves the `family` property of the `HttpStatus` object when using this function
     * as part of destructuring declarations or other component-based operations.
     * 
     * @return The `family` of the current `HttpStatus`, providing information about the
     *         category or group to which this HTTP status belongs (e.g., informational, success, etc.).
     * @since 3.1.0
     */
    operator fun component2() = family
    /**
     * Retrieves the `reasonPhrase` component of the `HttpStatus`.
     *
     * This operator function allows destructuring declarations to access the `reasonPhrase`, 
     * which represents the textual description associated with the HTTP status code.
     *
     * @return The `reasonPhrase` of this `HttpStatus`.
     * @since 3.1.0
     */
    operator fun component3() = reasonPhrase

    /**
     * Represents a categorization of HTTP status codes into standard classes based on their function.
     *
     * Each class groups HTTP status codes that share similar semantics and purposes,
     * aiding in the interpretation and handling of responses during communication between
     * clients and servers.
     *
     * @property displayName The name of the HTTP status code family.
     * @property value The integer value of the HTTP status code family.
     *
     * @since 2.0.0
     * @author Tommaso Pastorelli
     */
    enum class Family(val displayName: String, val value: Int) {
        /**
         * The INFORMATIONAL status family represents HTTP responses with status codes in the range of 1xx.
         *
         * These responses are used to indicate provisional information about the status of the request,
         * often requiring the client to make further actions to complete the request.
         * @since 4.0.0
         */
        Informational("Informational", 1),
        /**
         * Represents the "SUCCESSFUL" category within the Family enum class.
         *
         * This category is typically used to denote HTTP status codes that indicate successful
         * operations. Responses in this category often signify that the client's request was
         * successfully received, understood, and accepted by the server.
         *
         * Examples of successful status codes include 200 (OK), 201 (Created), and 204 (No Content).
         * @since 4.0.0
         */
        Successful("Successful", 2),
        /**
         * Represents the REDIRECTION status in the HTTP response status code categorization.
         *
         * REDIRECTION indicates that further action needs to be taken by the client in order to complete the request.
         * This category is typically used for redirecting the client to a different resource, as navigated by a Location header.
         * @since 3.0.0
         */
        Redirection("Redirection", 3),
        /**
         * Represents the client error family of HTTP status codes.
         *
         * The CLIENT_ERROR category contains status codes ranging from 400 to 499.
         * These status codes indicate issues where the client seems to have made a
         * mistake in the request. Common scenarios include invalid syntax,
         * unauthorized access, forbidden access, or requests that cannot be fulfilled.
         * @since 4.0.0
         */
        ClientError("Client Error", 4),
        /**
         * Represents the server error family of HTTP response codes.
         *
         * This family indicates that the server encountered an error or is otherwise
         * incapable of performing the request. Responses in this family typically return
         * with status codes in the range of 500 to 599.
         * @since 4.0.0
         */
        ServerError("Server Error", 5);

        companion object {
            infix fun of(code: Int) = entries.find { it.value == code }
        }

        /**
         * Extracts the first component of the Family instance.
         *
         * This operator function provides destructuring support, allowing the `displayName` property
         * of the Family class to be retrieved as the first element in a destructuring declaration.
         *
         * @return The value of the `displayName` property in the Family class.
         * @since 3.1.0
         */
        operator fun component1() = displayName
        /**
         * Extracts the first component of the Family instance.
         *
         * This operator function provides destructuring support, allowing the `value` property
         * of the Family class to be retrieved as the first element in a destructuring declaration.
         *
         * @return The value of the `value` property in the Family class.
         * @since 3.1.0
         */
        operator fun component2() = value
    }
}