/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.web

import dev.tommasop1804.kutils.*

/**
 * Represents the HTTP methods defined in the HTTP/1.1 standard.
 *
 * Each method includes associated metadata that describes its behavior concerning 
 * request and response bodies, security, idempotence, caching, and usage within 
 * HTML modules. 
 * 
 * @property canHasRequestBody Indicates whether the HTTP method can include a request body.
 * @property canHasResponseBody Indicates whether the HTTP method can expect a response body.
 * @property secure Indicates whether the HTTP method is considered secure for idempotent or safe operations.
 * @property idempotent Indicates whether the HTTP method guarantees the same result if called multiple times.
 * @property cacheable Specifies whether responses to the HTTP method are cacheable for subsequent requests.
 * @property canBeInHTMLModules Indicates whether the HTTP method can be used within HTML module contexts.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
enum class HttpMethod(
    val value: String,
    val canHasRequestBody: Boolean = false,
    val canHasResponseBody: Boolean = false,
    val secure: Boolean = false,
    val idempotent: Boolean = false,
    val cacheable: Boolean = false,
    val canBeInHTMLModules: Boolean = false
) {
    /**
     * Represents the HTTP CONNECT method.
     *
     * The CONNECT method is used to establish a tunnel to the server identified by the target resource. It is typically used for proxy communication.
     *
     * @since 1.0.0
     */
    Connect("CONNECT"),
    /**
     * Represents the HTTP DELETE method.
     *
     * DELETE is used to request that the origin server remove the resource identified by the Request-URI.
     * It can include a request body, although not all servers support this behavior.
     * A response body may also be returned, typically containing the status of the delete operation.
     * 
     * This method is idempotent, meaning multiple identical requests have the same effect as a single request.
     *
     * @since 1.0.0
     */
    Delete("DELETE", canHasRequestBody = true, canHasResponseBody = true, idempotent = true),
    /**
     * Represents the HTTP GET method.
     *
     * Characteristics of the GET method include:
     * - Supports a response body.
     * - Designed to be secure.
     * - Idempotent, meaning repeated requests produce the same result.
     * - Cacheable, allowing responses to be stored and reused.
     * - Can be utilized within HTML modules.
     *
     * @since 1.0.0
     */
    Get("GET", canHasResponseBody = true, secure = true, idempotent = true, cacheable = true, canBeInHTMLModules = true),
    /**
     * Represents the HTTP HEAD method.
     *
     * The HEAD method is used to request the headers that would be returned if the specified resource 
     * were requested with a GET request. This method is commonly used for testing hypertext links for 
     * validity, accessibility, and recent modification.
     *
     * Characteristics of the HEAD method:
     * - Secure: Requests can be made over secure channels, such as HTTPS.
     * - Idempotent: Multiple identical requests have the same effect as a single request.
     * - Cacheable: Responses to HEAD requests can be stored in caches.
     *
     * @since 1.0.0
     */
    Head("HEAD", secure = true, idempotent = true, cacheable = true),
    /**
     * Represents the HTTP OPTIONS method.
     *
     * The OPTIONS method is used to describe the communication options
     * for the target resource. It allows clients to determine the capabilities
     * and requirements of the server.
     *
     * @since 1.0.0
     */
    Options("OPTIONS", canHasRequestBody = true, canHasResponseBody = true, secure = true, idempotent = true),
    /**
     * Represents the HTTP PATCH method used to apply partial modifications to a resource.
     *
     * Properties:
     * - Allows a request body.
     * - Allows a response body.
     * - Cacheable.
     *
     * This method is typically used when updating only specific fields of a resource,
     * rather than replacing the entire resource, and allows for efficient updates by minimizing data transfer.
     *
     * @since 1.0.0
     */
    Patch("PATCH", canHasRequestBody = true, canHasResponseBody = true, cacheable = true),
    /**
     * Represents the HTTP POST method, which is primarily used to submit data to a server
     * for processing. Unlike the GET method, POST requests typically include a request body.
     *
     * Key characteristics:
     * - Supports having a request body, making it ideal for sending data (e.g., form submissions).
     * - Expects a response body in return, which often contains the server's processing result.
     * - Can be cached under certain conditions, but generally less commonly cached than GET requests.
     *
     * Commonly used for operations that create resources or submit data.
     *
     * @since 1.0.0
     */
    Post("POST", canHasRequestBody = true, canHasResponseBody = true, cacheable = true),
    /**
     * The HTTP PUT method.
     *
     * PUT is used to upload resources or update existing resources on the server.
     * It is capable of carrying a request body and receiving a response body.
     * The method is idempotent, meaning multiple identical requests will have the same effect as a single request.
     *
     * @since 1.0.0
     */
    Put("PUT", canHasRequestBody = true, canHasResponseBody = true, idempotent = true),
    /**
     * Represents the TRACE HTTP method.
     *
     * TRACE is primarily used for debugging purposes, allowing the client to see what is being received
     * at the other end of the request chain for testing and diagnostic purposes. It echoes back the message
     * received in the request to help clients verify what content was received.
     *
     * Characteristics:
     * - Supports a response body.
     * - Operates securely.
     * - Is idempotent, meaning multiple identical requests will yield the same result without additional side effects.
     *
     * @since 1.0.0
     */
    Trace("TRACE", canHasResponseBody = true, secure = true, idempotent = true);

    companion object {
        /**
         * Finds and returns the first entry in the list whose name matches the provided name,
         * ignoring case differences.
         *
         * @param name The name to search for in the entries.
         * @return The first matching entry, or `null` if no match is found.
         * @since 3.12.0
         */
        infix fun of(name: String) = entries.find { it.value equalsIgnoreCase name }
    }

    /**
     * Destructures the `HttpMethod` instance to retrieve the `canHasRequestBody` property.
     *
     * This operator function is part of the component destructuring mechanism,
     * allowing for concise decomposition of the `HttpMethod` object.
     *
     * @return A `Boolean` flag indicating the ability of the HTTP method to contain a request body.
     *
     * @since 3.1.0
     */
    operator fun component1() = canHasRequestBody
    /**
     * Destructures the instance, providing the value of the `canHasResponseBody` property.
     *
     * This operator function allows the second component of the `HttpMethod` object
     * to be accessed when destructured.
     *
     * @return The value of the `canHasResponseBody` property.
     * @since 3.1.0
     */
    operator fun component2() = canHasResponseBody
    /**
     * Provides access to the `secure` property of the `HttpMethod` class
     * as a component within a destructuring declaration.
     *
     * This operator function is the third component of the `HttpMethod`
     * class, often used in destructuring assignments to retrieve the
     * associated value for `secure`.
     *
     * @return The value of the `secure` property.
     * @since 3.1.0
     */
    operator fun component3() = secure
    /**
     * Returns the `idempotent` property of the HttpMethod instance.
     *
     * The `idempotent` property indicates whether the HTTP method is idempotent,
     * meaning that performing the same operation multiple times will have the
     * same effect as performing it once.
     *
     * @return The value of the `idempotent` property.
     * @since 3.1.0
     */
    operator fun component4() = idempotent
    /**
     * Destructures the `HttpMethod` instance to extract the value of the `cacheable` property.
     * This component is used as part of the Kotlin destructuring declaration mechanism.
     *
     * @return A boolean value indicating whether the HTTP method is cacheable.
     * @since 3.1.0
     */
    operator fun component5() = cacheable
    /**
     * Provides the sixth component of the HttpMethod instance when destructuring.
     * This component indicates whether the HTTP method can be used in HTML modules.
     *
     * @return A Boolean value representing if the method can be included in HTML modules.
     * @since 3.1.0
     */
    operator fun component6() = canBeInHTMLModules

    /**
     * Returns a string representation of the `HttpMethod` instance.
     *
     * The returned value corresponds to the underlying `value` property of the `HttpMethod` class,
     * which typically represents the name of the HTTP method (e.g., GET, POST, PUT, etc.).
     *
     * This method is overridden to provide a more meaningful string representation for logging
     * or debugging purposes.
     *
     * @return The `value` property of the `HttpMethod` instance as a string.
     * @since 4.0.0
     */
    override fun toString() = value
}