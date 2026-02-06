@file:JvmName("WebUtilsKt")
@file:Since("1.0.0")
@file:Suppress("unused")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.time.Duration
import dev.tommasop1804.kutils.classes.web.HttpMethod
import java.net.URI
import java.net.URL
import java.net.http.HttpRequest

/**
 * Adds multiple header key-value pairs to the `HttpRequest.Builder`.
 *
 * Each provided pair in the `headers` parameter will be added as a header, 
 * where the first value of the pair represents the header name, and the second value 
 * represents the header's value (converted to a string).
 *
 * @param headers one or more pairs representing the header name and its corresponding value.
 * @since 1.0.0
 */
fun HttpRequest.Builder.headers(vararg headers: Pair<String, Any>) = apply { 
    headers.forEach { header(it.first, it.second.toString()) } 
}
/**
 * Sets multiple headers to the HttpRequest.Builder instance.
 * 
 * This function iterates over the provided DataMapNN and applies each key-value pair as a header
 * to the HttpRequest.Builder.
 *
 * @param headers a map containing the header names as keys and header values as non-null data to be added.
 * @since 1.0.0
 */
fun HttpRequest.Builder.headers(headers: DataMapNN) = apply {
    headers.forEach { header(it.key, it.value.toString()) }
}
/**
 * Sets the timeout duration for the HTTP request being built.
 *
 * @param timeout The duration to set as the timeout. This value determines 
 * the maximum amount of time the request is allowed to take.
 * @return The updated `HttpRequest.Builder` with the configured timeout.
 * @since 1.0.0
 */
fun HttpRequest.Builder.timeout(timeout: Duration): HttpRequest.Builder = 
    timeout(timeout.toJavaDuration())

/**
 * Configures the HTTP request builder with the specified HTTP method.
 *
 * This method utilizes the provided `HttpMethod` to define the request type 
 * (e.g., GET, POST, DELETE) and assigns an empty body to the request.
 *
 * @param method The HTTP method to set for this request, represented by an 
 * `HttpMethod` enum value (e.g., GET, POST, PUT).
 * @return The updated `HttpRequest.Builder` instance configured with the 
 * specified HTTP method.
 * @since 1.0.0
 */
fun HttpRequest.Builder.request(method: HttpMethod): HttpRequest.Builder = 
    method(method.name, HttpRequest.BodyPublishers.noBody())
/**
 * Sets the HTTP method and body for an HTTP request using the provided `HttpMethod`
 * and `HttpRequest.BodyPublisher`. This method simplifies configuring the request
 * method and associated body content in a fluent manner.
 *
 * @param method The HTTP method to be set for the request. This parameter uses the `HttpMethod` enum
 *               to ensure strict type safety for supported HTTP methods.
 * @param body The body content of the HTTP request, represented as an instance of `HttpRequest.BodyPublisher`.
 *             This defines the data to be sent in the request body.
 * @return An updated instance of `HttpRequest.Builder` with the specified HTTP method and body applied.
 * @since 1.0.0
 */
fun HttpRequest.Builder.request(method: HttpMethod, body: HttpRequest.BodyPublisher): HttpRequest.Builder = 
    method(method.name, body)
/**
 * Configures the HTTP request with the specified HTTP method and body.
 *
 * This method extends `HttpRequest.Builder` to set the HTTP method and 
 * encode the provided `body` as a JSON string using the `serialize` function.
 * It is designed for flexibility and compatibility with any object type.
 *
 * @receiver The HTTP request builder instance being configured.
 * @param method The HTTP method to be used for the request, represented by an instance of `HttpMethod`.
 * @param body The request payload represented as an `Any` object, which will be serialized to a JSON string.
 * @return The updated `HttpRequest.Builder` instance configured with the specified method and body.
 * @since 1.0.0
 */
@JvmName("requestAnyBody")
fun HttpRequest.Builder.request(method: HttpMethod, body: Any): HttpRequest.Builder = 
    method(method.name, HttpRequest.BodyPublishers.ofString(body.serialize()))

/**
 * Sets the HTTP method for the request being built.
 *
 * @param method The HTTP method to be used for the request (e.g., GET, POST, etc.).
 * @since 1.0.0
 */
fun HttpRequest.Builder.method(method: HttpMethod) = request(method)
/**
 * Sets the HTTP method and request body for this HTTP request.
 *
 * @param method the HTTP method for the request, such as GET, POST, PUT, etc.
 * @param body the body publisher that provides the request payload.
 * @since 1.0.0
 */
fun HttpRequest.Builder.method(method: HttpMethod, body: HttpRequest.BodyPublisher) = request(method, body)
/**
 * Sets the HTTP method and provides a request body for the HTTP request.
 *
 * @param method The HTTP method (e.g., GET, POST, PUT, DELETE) for the request.
 * @param body The body of the request, which will be serialized to a string.
 * @since 1.0.0
 */
@JvmName("methodAnyBody")
fun HttpRequest.Builder.method(method: HttpMethod, body: Any) = 
    request(method, HttpRequest.BodyPublishers.ofString(body.serialize()))

/**
 * Converts the current string to a [URI] instance. If the string is not a valid URI,
 * the operation will return a [Result] encapsulating the failure.
 *
 * This method attempts to parse the string as a URI and wraps the result in a [Result] object.
 * If the string is malformed or violates URI syntax rules, the exception will be captured
 * within the [Result] failure.
 *
 * @receiver The string to be converted to a [URI].
 * @return A [Result] object containing either a successfully parsed [URI] or an exception if parsing fails.
 * @since 1.0.0
 */
fun String.toURI() = runCatching { URI(this) }
/**
 * Converts the string to a URL wrapped in a Result.
 * The method first attempts to convert the string to a URI and, if successful, converts the URI to a URL.
 *
 * @return A [Result] containing the [URL] if the conversion was successful, or an exception if it failed.
 * @since 1.0.0
 */
fun String.toURL(): Result<URL> = runCatching { toURI()().toURL() }