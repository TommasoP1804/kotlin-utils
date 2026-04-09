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
    CONTINUE(100, Family.INFORMATIONAL, "Continue"),
    SWITCHING_PROTOCOLS(101, Family.INFORMATIONAL, "Switching Protocols"),
    @Deprecated("Removed from WebDAV specification") PROCESSING(102, Family.INFORMATIONAL, "Processing"),
    EARLY_HINTS(103, Family.INFORMATIONAL, "Early Hints"),

    OK(200, Family.SUCCESSFUL, "OK"),
    CREATED(201, Family.SUCCESSFUL, "Created"),
    ACCEPTED(202, Family.SUCCESSFUL, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, Family.SUCCESSFUL, "Non-Authoritative Information"),
    NO_CONTENT(204, Family.SUCCESSFUL, "No Content"),
    RESET_CONTENT(205, Family.SUCCESSFUL, "Reset Content"),
    PARTIAL_CONTENT(206, Family.SUCCESSFUL, "Partial Content"),
    MULTI_STATUS(207, Family.SUCCESSFUL, "Multi-Status"),
    ALREADY_REPORTED(208, Family.SUCCESSFUL, "Already Reported"),
    IM_USED(226, Family.SUCCESSFUL, "IM Used"),

    MULTIPLE_CHOICES(300, Family.REDIRECTION, "Multiple Choices"),
    MOVED_PERMANENTLY(301, Family.REDIRECTION, "Moved Permanently"),
    FOUND(302, Family.REDIRECTION, "Found"),
    SEE_OTHER(303, Family.REDIRECTION, "See Other"),
    NOT_MODIFIED(304, Family.REDIRECTION, "Not Modified"),
    @Deprecated("Deprecated status code") USE_PROXY(305, Family.REDIRECTION, "Use Proxy"),
    @Deprecated("Deprecated status code", level = DeprecationLevel.ERROR) SWITCH_PROXY(306, Family.REDIRECTION, "Switch Proxy"),
    TEMPORARY_REDIRECT(307, Family.REDIRECTION, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, Family.REDIRECTION, "Permanent Redirect"),

    BAD_REQUEST(400, Family.CLIENT_ERROR, "Bad Request"),
    UNAUTHORIZED(401, Family.CLIENT_ERROR, "Unauthorized"),
    PAYMENT_REQUIRED(402, Family.CLIENT_ERROR, "Payment Required"),
    FORBIDDEN(403, Family.CLIENT_ERROR, "Forbidden"),
    NOT_FOUND(404, Family.CLIENT_ERROR, "Not Found"),
    METHOD_NOT_ALLOWED(405, Family.CLIENT_ERROR, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, Family.CLIENT_ERROR, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, Family.CLIENT_ERROR, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, Family.CLIENT_ERROR, "Request Timeout"),
    CONFLICT(409, Family.CLIENT_ERROR, "Conflict"),
    GONE(410, Family.CLIENT_ERROR, "Gone"),
    LENGTH_REQUIRED(411, Family.CLIENT_ERROR, "Length Required"),
    PRECONDITION_FAILED(412, Family.CLIENT_ERROR, "Precondition Failed"),
    CONTENT_TOO_LARGE(413, Family.CLIENT_ERROR, "Payload Too Large"),
    URI_TOO_LONG(414, Family.CLIENT_ERROR, "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, Family.CLIENT_ERROR, "Unsupported Media Type"),
    RANGE_NOT_SATISFIABLE(416, Family.CLIENT_ERROR, "Range Not Satisfiable"),
    EXPECTATION_FAILED(417, Family.CLIENT_ERROR, "Expectation Failed"),
    @Deprecated("Deprecated status code", level = DeprecationLevel.ERROR) I_AM_A_TEAPOT(418, Family.CLIENT_ERROR, "I'm a teapot"),
    MISDIRECTED_REQUEST(421, Family.CLIENT_ERROR, "Misdirected Request"),
    UNPROCESSABLE_ENTITY(422, Family.CLIENT_ERROR, "Unprocessable Entity"),
    LOCKED(423, Family.CLIENT_ERROR, "Locked"),
    FAILED_DEPENDENCY(424, Family.CLIENT_ERROR, "Failed Dependency"),
    TOO_EARLY(425, Family.CLIENT_ERROR, "Too Early"),
    UPGRADE_REQUIRED(426, Family.CLIENT_ERROR, "Upgrade Required"),
    PRECONDITION_REQUIRED(428, Family.CLIENT_ERROR, "Precondition Required"),
    TOO_MANY_REQUESTS(429, Family.CLIENT_ERROR, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, Family.CLIENT_ERROR, "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, Family.CLIENT_ERROR, "Unavailable For Legal Reasons"),

    INTERNAL_SERVER_ERROR(500, Family.SERVER_ERROR, "Internal Server Error"),
    NOT_IMPLEMENTED(501, Family.SERVER_ERROR, "Not Implemented"),
    BAD_GATEWAY(502, Family.SERVER_ERROR, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, Family.SERVER_ERROR, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, Family.SERVER_ERROR, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, Family.SERVER_ERROR, "HTTP Version Not Supported"),
    VARIANT_ALSO_NEGOTIATES(506, Family.SERVER_ERROR, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507, Family.SERVER_ERROR, "Insufficient Storage"),
    LOOP_DETECTED(508, Family.SERVER_ERROR, "Loop Detected"),
    NOT_EXTENDED(510, Family.SERVER_ERROR, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511, Family.SERVER_ERROR, "Network Authentication Required");

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
    val isInformational get() = family == Family.INFORMATIONAL
    /**
     * Indicates whether the HTTP status code belongs to the "SUCCESSFUL" category.
     *
     * This property evaluates to true if the HTTP status code is categorized under the "SUCCESSFUL"
     * family, which represents status codes indicating that the client's request was successfully
     * received, understood, and accepted by the server. Examples of such status codes include 200 (OK),
     * 201 (Created), and 204 (No Content).
     * @since 2.0.0
     */
    val isSuccessful get() = family == Family.SUCCESSFUL
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
    val isRedirection get() = family == Family.REDIRECTION
    /**
     * Indicates whether the HTTP status code belongs to the client error category.
     *
     * This property evaluates to `true` if the status code is part of the `CLIENT_ERROR` family,
     * which covers status codes ranging from 400 to 499. These status codes typically denote
     * issues caused by the client, such as malformed requests, unauthorized access, or forbidden actions.
     * @since 2.0.0
     */
    val isClientError get() = family == Family.CLIENT_ERROR
    /**
     * Indicates whether the HTTP status belongs to the server error category.
     *
     * A server error is represented by HTTP response status codes in the range of 500 to 599.
     * These codes signify that the server encountered an issue or is unable to perform the request.
     * The property evaluates to `true` if the `family` of the HTTP status is `SERVER_ERROR`.
     * @since 2.0.0
     */
    val isServerError get() = family == Family.SERVER_ERROR

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
         * @since 2.0.0
         */
        INFORMATIONAL("Informational", 1),
        /**
         * Represents the "SUCCESSFUL" category within the Family enum class.
         *
         * This category is typically used to denote HTTP status codes that indicate successful
         * operations. Responses in this category often signify that the client's request was
         * successfully received, understood, and accepted by the server.
         *
         * Examples of successful status codes include 200 (OK), 201 (Created), and 204 (No Content).
         * @since 2.0.0
         */
        SUCCESSFUL("Successful", 2),
        /**
         * Represents the REDIRECTION status in the HTTP response status code categorization.
         *
         * REDIRECTION indicates that further action needs to be taken by the client in order to complete the request.
         * This category is typically used for redirecting the client to a different resource, as navigated by a Location header.
         * @since 2.0.0
         */
        REDIRECTION("Redirection", 3),
        /**
         * Represents the client error family of HTTP status codes.
         *
         * The CLIENT_ERROR category contains status codes ranging from 400 to 499.
         * These status codes indicate issues where the client seems to have made a
         * mistake in the request. Common scenarios include invalid syntax,
         * unauthorized access, forbidden access, or requests that cannot be fulfilled.
         * @since 2.0.0
         */
        CLIENT_ERROR("Client Error", 4),
        /**
         * Represents the server error family of HTTP response codes.
         *
         * This family indicates that the server encountered an error or is otherwise
         * incapable of performing the request. Responses in this family typically return
         * with status codes in the range of 500 to 599.
         * @since 2.0.0
         */
        SERVER_ERROR("Server Error", 5);

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