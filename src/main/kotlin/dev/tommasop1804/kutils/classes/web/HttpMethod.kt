package dev.tommasop1804.kutils.classes.web

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
    CONNECT,
    /**
     * Represents the HTTP DELETE method.
     *
     * DELETE is used to request that the origin server remove the resource identified by the Request-URI.
     * It can include a request body, although not all servers support this behavior.
     * A response body may also be returned, typically containing the status of the delete operation.
     * 
     * This method is idempotent, meaning multiple identical requests have the same effect as a single request.
     *
     * @property canHasRequestBody Indicates whether the method allows a request body.
     * @property canHasResponseBody Indicates whether the method may return a response body.
     * @property idempotent Indicates whether the method is idempotent.
     * 
     * @since 1.0.0
     */
    DELETE(canHasRequestBody = true, canHasResponseBody = true, idempotent = true),
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
    GET(canHasResponseBody = true, secure = true, idempotent = true, cacheable = true, canBeInHTMLModules = true),
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
    HEAD(secure = true, idempotent = true, cacheable = true),
    /**
     * Represents the HTTP OPTIONS method.
     *
     * The OPTIONS method is used to describe the communication options
     * for the target resource. It allows clients to determine the capabilities
     * and requirements of the server.
     *
     * @property canHasRequestBody Indicates if the OPTIONS method can include a request body.
     * @property canHasResponseBody Indicates if the OPTIONS method can include a response body.
     * @property secure Indicates if the OPTIONS method is considered secure.
     * @property idempotent Indicates if the OPTIONS method is idempotent.
     *
     * @since 1.0.0
     */
    OPTIONS(canHasRequestBody = true, canHasResponseBody = true, secure = true, idempotent = true),
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
    PATCH(canHasRequestBody = true, canHasResponseBody = true, cacheable = true),
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
    POST(canHasRequestBody = true, canHasResponseBody = true, cacheable = true),
    /**
     * The HTTP PUT method.
     *
     * PUT is used to upload resources or update existing resources on the server.
     * It is capable of carrying a request body and receiving a response body.
     * The method is idempotent, meaning multiple identical requests will have the same effect as a single request.
     *
     * @since 1.0.0
     */
    PUT(canHasRequestBody = true, canHasResponseBody = true, idempotent = true),
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
    TRACE(canHasResponseBody = true, secure = true, idempotent = true);
}