/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("WebUtilsKt")
@file:Since("1.0.0")
@file:Suppress("unused")
@file:MustUseReturnValues
@file:OptIn(ExperimentalContracts::class)

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.time.*
import dev.tommasop1804.kutils.classes.web.*
import dev.tommasop1804.kutils.classes.web.HttpStatus.Companion.toHttpStatus
import dev.tommasop1804.kutils.exceptions.*
import java.io.File
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.file.Path
import java.util.concurrent.Flow
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.text.Charsets.UTF_8

/**
 * Retrieves the HTTP status of the response as an `HttpStatus` instance.
 *
 * This property transforms the raw integer HTTP status code of the response into a more descriptive
 * `HttpStatus` object using the `toHttpStatus` extension function.
 *
 * @receiver The HTTP response whose status code is to be accessed.
 * @return An `HttpStatus` representation of the response's status code.
 * @since 2.0.0
 */
val <T> HttpResponse<T>.status get() = statusCode().toHttpStatus()
/**
 * Gets the HTTP response status as an instance of `HttpStatus`.
 *
 * This read-only property retrieves the integer HTTP status code via `statusCode()`
 * and converts it into its corresponding `HttpStatus` representation using `toHttpStatus()`.
 *
 * @receiver The `HttpResponse.ResponseInfo` instance from which the status is retrieved.
 * @return A `HttpStatus` instance representing the HTTP response status.
 * @since 2.0.0
 */
val HttpResponse.ResponseInfo.status get() = statusCode().toHttpStatus()
/**
 * Provides access to the headers of the HTTP response.
 *
 * This property retrieves all headers associated with the HTTP response
 * as an instance of `HttpHeaders`, which offers convenient methods to
 * access and manipulate the header values.
 *
 * The headers are mapped to a structured form for easier usability
 * when working with the HTTP response in typed operations.
 * @since 4.7.0
 */
val <T> HttpResponse<T>.headers get() = HttpHeaders(headers().map())
/**
 * Provides access to the HTTP headers associated with this response.
 *
 * This property retrieves the headers of the HTTP response as an instance of [HttpHeaders],
 * which allows for accessing the key-value pairs representing the header names and their values.
 * @since 4.7.0
 */
val HttpResponse.ResponseInfo.headers get() = HttpHeaders(headers().map())
/**
 * Returns the URI associated with this HTTP response.
 *
 * This property provides the URI from which the response was obtained.
 * It is typically used to retrieve the endpoint or resource identifier
 * to which the HTTP request was made.
 *
 * @receiver HttpResponse<T> The HTTP response object for which the URI is being accessed.
 * @return The URI of the HTTP request that resulted in this response.
 * @since 4.7.0
 */
val <T> HttpResponse<T>.uri: Uri get() = uri()
/**
 * Represents the HTTP protocol version associated with the given HTTP response.
 * This property maps the version of the underlying HTTP client response to an
 * application-specific enumeration for protocol versions.
 *
 * Possible values:
 * - `HttpVersion.HTTP_1_1` for HTTP/1.1 responses.
 * - `HttpVersion.HTTP_2` for HTTP/2 responses.
 *
 * This is a read-only property.
 * @since 4.7.0
 */
val <T> HttpResponse<T>.version get() = when(version()) {
    HttpClient.Version.HTTP_1_1 -> HttpVersion.HTTP_1_1
    HttpClient.Version.HTTP_2 -> HttpVersion.HTTP_2
}
/**
 * Retrieves the HTTP version of the response.
 *
 * Maps the `HttpClient.Version` of the response to the corresponding `HttpVersion`.
 * This indicates whether the response adheres to HTTP/1.1 or HTTP/2 standards.
 * @since 4.7.0
 */
val HttpResponse.ResponseInfo.version get() = when(version()) {
    HttpClient.Version.HTTP_1_1 -> HttpVersion.HTTP_1_1
    HttpClient.Version.HTTP_2 -> HttpVersion.HTTP_2
}

/**
 * Creates a `BodyPublisher` that publishes the provided string as the request body using the specified charset.
 *
 * @param body the string content to be published as the request body
 * @param charset the character set to encode the string content, defaults to UTF-8
 * @return a `BodyPublisher` for the specified string and charset
 * @since 4.7.0
 */
fun BodyPublisher(body: String, charset: Charset = UTF_8) =
    HttpRequest.BodyPublishers.ofString(body, charset)!!
/**
 * Creates a new instance of BodyPublisher using the provided input stream supplier.
 *
 * @param stream A supplier of input streams that provides the data to publish as the request body.
 *               The supplier is expected to supply a new input stream each time it is invoked.
 *               The input stream will be consumed as part of the HTTP request body.
 * @return A BodyPublisher instance configured with the supplied input stream.
 * @since 4.7.0
 */
fun BodyPublisher(stream: Supplier<InputStream>) =
    HttpRequest.BodyPublishers.ofInputStream(stream)!!
/**
 * Creates a BodyPublisher instance that publishes the specified segment of a byte array.
 *
 * @param buf The byte array containing the data to be published.
 * @param offset The starting position in the array from which to begin publishing. Defaults to 0.
 * @param length The number of bytes to publish from the array, starting at the offset. Defaults to the size of the array.
 * @return A BodyPublisher that publishes the specified segment of the byte array.
 * @since 4.7.0
 */
fun BodyPublisher(buf: ByteArray, offset: Int = 0, length: Int = buf.size) =
    HttpRequest.BodyPublishers.ofByteArray(buf, offset, length)!!
/**
 * Creates a BodyPublisher instance that publishes the content of the provided file.
 *
 * @param file The file whose content is to be sent as the body of an HTTP request.
 *             Must not be null and must represent a valid file path.
 * @return A BodyPublisher that streams the content of the specified file.
 * @since 4.7.0
 */
fun BodyPublisher(file: File) =
    HttpRequest.BodyPublishers.ofFile(file.toPath())!!
/**
 * Creates a body publisher that reads the content of the provided file path.
 *
 * @param path The path to the file that will be used as the data source for the body publisher.
 * @return A body publisher that publishes the content of the specified file.
 * @since 4.7.0
 */
fun BodyPublisher(path: Path) =
    HttpRequest.BodyPublishers.ofFile(path)!!
/**
 * Creates a BodyPublisher instance that publishes the provided arrays of ByteArray.
 *
 * @param arrays A vararg of ByteArray elements to be used as the source data for the BodyPublisher.
 *               These byte arrays will be published in order.
 * @return A non-null BodyPublisher that streams the input byte arrays.
 * @since 4.7.0
 */
fun BodyPublisher(vararg arrays: ByteArray) =
    HttpRequest.BodyPublishers.ofByteArrays(arrays.toList())!!
/**
 * Creates a `BodyPublisher` that publishes the provided iterable of byte arrays.
 *
 * @param arrays an iterable collection of byte arrays to be published as the request body
 * @return a `BodyPublisher` instance created from the given byte arrays
 * @since 4.7.0
 */
fun BodyPublisher(arrays: Iterable<ByteArray>) =
    HttpRequest.BodyPublishers.ofByteArrays(arrays)!!
/**
 * Creates a BodyPublisher instance using the provided Flow.Publisher and an optional content length.
 * This is used to publish the request body for HTTP requests.
 *
 * @param publisher the Flow.Publisher that supplies the data to be sent as the request body.
 * @param contentLength the optional content length of the request body. If not provided or set to -1,
 *                      the content length is unknown.
 * @return a BodyPublisher instance constructed using the provided parameters.
 * @since 4.7.0
 */
fun BodyPublisher(publisher: Flow.Publisher<out ByteBuffer>, contentLength: Long = -1L) =
    HttpRequest.BodyPublishers.fromPublisher(publisher, contentLength)!!
/**
 * Creates a new instance of a BodyPublisher that publishes no body.
 *
 * This is a utility function that wraps the `HttpRequest.BodyPublishers.noBody()`
 * method, which is used in constructing HTTP requests when no request body is needed.
 *
 * @return A `BodyPublisher` instance that represents an empty request body.
 * @since 4.7.0
 */
fun BodyPublisher() =
    HttpRequest.BodyPublishers.noBody()!!

/**
 * Constructs an HTTP request with the specified parameters.
 *
 * @param uri The target URI of the HTTP request.
 * @param method The HTTP method to be used for the request (e.g., GET, POST). Defaults to HttpMethod.Get.
 * @param headers The headers to be included in the request. Defaults to an empty HttpHeaders instance.
 * @param bodyPublisher The body publisher for the request, used for sending request body data. Defaults to null.
 * @param timeout The timeout duration for the request. Defaults to null, indicating no timeout.
 * @param version The HTTP version to be used for the request. Defaults to HttpVersion.HTTP_1_1.
 * @param expectContinue A boolean indicating whether the "Expect: 100-continue" header should be used. Defaults to false.
 * @since 4.7.0
 */
@Suppress("RETURN_VALUE_NOT_USED_COERCION")
fun HttpRequest(
    uri: Uri,
    method: HttpMethod = HttpMethod.Get,
    headers: HttpHeaders = HttpHeaders(),
    bodyPublisher: HttpRequest.BodyPublisher? = null,
    timeout: Duration? = null,
    version: HttpVersion = HttpVersion.HTTP_1_1,
    expectContinue: Boolean = false
) = HttpRequest.newBuilder(uri).apply {
    method(method, bodyPublisher)
    headers.ifNotNull(::headers)
    timeout.ifNotNull(::timeout)
    version.ifNotNull(::version)
    expectContinue(expectContinue)
}.build()!!
/**
 * Constructs an HTTP request with the specified parameters.
 *
 * @param url The URL of the HTTP request.
 * @param method The HTTP method to be used (e.g., GET, POST). Defaults to HttpMethod.Get.
 * @param headers The headers to be included in the HTTP request. Defaults to an empty HttpHeaders instance.
 * @param bodyPublisher The publisher of the request body. Can be null if no body is required.
 * @param timeout The timeout duration for the request. Can be null to use the default timeout.
 * @param version The HTTP protocol version to be used. Defaults to HttpVersion.HTTP_1_1.
 * @param expectContinue Whether the "Expect: 100-continue" header should be included. Defaults to false.
 * @since 4.7.0
 */
fun HttpRequest(
    url: Url,
    method: HttpMethod = HttpMethod.Get,
    headers: HttpHeaders = HttpHeaders(),
    bodyPublisher: HttpRequest.BodyPublisher? = null,
    timeout: Duration? = null,
    version: HttpVersion = HttpVersion.HTTP_1_1,
    expectContinue: Boolean = false
) = HttpRequest(
    url.toUri(),
    method,
    headers,
    bodyPublisher,
    timeout,
    version,
    expectContinue
)
/**
 * Constructs an HTTP request with the specified parameters.
 *
 * @param uri The target URI of the request.
 * @param method The HTTP method to use for the request. Defaults to `HttpMethod.Get`.
 * @param headers The headers to include in the request. Defaults to an empty set of headers.
 * @param timeout The timeout duration for the request. Optional, defaults to `null` if not specified.
 * @param version The HTTP version to be used for the request. Defaults to `HttpVersion.HTTP_1_1`.
 * @param expectContinue Indicates if the `Expect: 100-continue` behavior is enabled. Defaults to `false`.
 * @param body A supplier function that provides the request body, serialized into a compatible format.
 * @since 4.7.0
 */
fun HttpRequest(
    uri: Uri,
    method: HttpMethod = HttpMethod.Get,
    headers: HttpHeaders = HttpHeaders(),
    timeout: Duration? = null,
    version: HttpVersion = HttpVersion.HTTP_1_1,
    expectContinue: Boolean = false,
    body: Supplier<Any>
) = HttpRequest(
    uri,
    method,
    headers,
    BodyPublisher(body().serialize()),
    timeout,
    version,
    expectContinue
)
/**
 * Constructs an HTTP request with the specified parameters.
 *
 * @param url The target URL for the HTTP request.
 * @param method The HTTP method to be used for the request. Defaults to `HttpMethod.Get`.
 * @param headers The HTTP headers to be included in the request. Defaults to an empty set of headers.
 * @param timeout The timeout duration for the request. If null, no timeout is applied.
 * @param version The HTTP version to be used for the request. Defaults to `HttpVersion.HTTP_1_1`.
 * @param expectContinue Indicates whether the request should use the "Expect: 100-continue" header. Defaults to `false`.
 * @param body A supplier function providing the body of the request.
 * @since 4.7.0
 */
fun HttpRequest(
    url: Url,
    method: HttpMethod = HttpMethod.Get,
    headers: HttpHeaders = HttpHeaders(),
    timeout: Duration? = null,
    version: HttpVersion = HttpVersion.HTTP_1_1,
    expectContinue: Boolean = false,
    body: Supplier<Any>
) = HttpRequest(
    url.toUri(),
    method,
    headers,
    BodyPublisher(body().serialize()),
    timeout,
    version,
    expectContinue
)
/**
 * Constructs a new HttpRequest by applying a filter to the existing request.
 *
 * @param request The original HttpRequest object to be modified.
 * @param filter A BiPredicate function used to filter headers or modify the request based on the key-value pair.
 * @return A new HttpRequest instance built using the provided filter and original request.
 * @since 4.7.0
 */
fun HttpRequest(
    request: HttpRequest,
    filter: BiPredicate<String, String>
) = HttpRequest.newBuilder(request, filter).build()!!

/**
 * A property that provides a pre-configured instance of `HttpRequest.Builder` for the current `HttpRequest` object.
 *
 * This builder is initialized with the current `HttpRequest` as a template, and a default filter function
 * that always returns `true`. It allows further customization and modification of the HTTP request.
 *
 * Note: The builder is created using the `HttpRequest.newBuilder` method. The receiver `HttpRequest` serves as
 * the source configuration for the new builder instance.
 */
val HttpRequest.builder get() = HttpRequest.newBuilder(this) { _, _ -> true }!!

/**
 * Adds or replaces headers in the HTTP request builder.
 *
 * @param headers The headers to be added to the request. Each key-value pair in the provided HttpHeaders
 * contains the header name and its corresponding values joined as a single string.
 * @since 4.7.0
 */
fun HttpRequest.Builder.headers(headers: HttpHeaders) = apply {
    headers.forEach { header(it.key, it.value.joinToString()) }
}
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
 * Sets the HTTP version for the HTTP request builder.
 *
 * This function configures the HTTP version for the provided `HttpRequest.Builder` instance. If the version
 * specified cannot be used, an exception is thrown. The method leverages the `tryOrThrow` utility for error handling
 * and maps the `HttpVersion` to its corresponding Java HTTP client version representation.
 *
 * @param version The HTTP version to be set for the request, represented by the `HttpVersion` enum.
 * @return The modified `HttpRequest.Builder` instance with the specified HTTP version configured.
 * @throws UnsupportedOperationException If the specified HTTP version is not supported by the application.
 * @since 4.7.0
 */
fun HttpRequest.Builder.version(version: HttpVersion): HttpRequest.Builder =
    tryOrThrow({ -> UnsupportedOperationException("Cannot use that version of HTTP") }, overwriteOnly = NoSuchEntryException::class) {
        version(version.toJavaHttpVersion())
    }

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
@Deprecated("Use method() instead", ReplaceWith("method(method)"))
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
@Deprecated("Use method() instead", ReplaceWith("method(method)"))
fun HttpRequest.Builder.request(method: HttpMethod, body: HttpRequest.BodyPublisher? = null): HttpRequest.Builder =
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
@Deprecated("Use method() instead", ReplaceWith("method(method)"))
fun HttpRequest.Builder.request(method: HttpMethod, body: Any): HttpRequest.Builder =
    method(method.name, HttpRequest.BodyPublishers.ofString(body.serialize()))

/**
 * Sets the HTTP method for the request being built.
 *
 * @param method The HTTP method to be used for the request (e.g., GET, POST, etc.).
 * @since 1.0.0
 */
fun HttpRequest.Builder.method(method: HttpMethod) =
    method(method.value, HttpRequest.BodyPublishers.noBody())!!
/**
 * Sets the HTTP method and request body for this HTTP request.
 *
 * @param method the HTTP method for the request, such as GET, POST, PUT, etc.
 * @param body the body publisher that provides the request payload.
 * @since 1.0.0
 */
fun HttpRequest.Builder.method(method: HttpMethod, body: HttpRequest.BodyPublisher? = null) =
    method(method.value, body)!!
/**
 * Sets the HTTP method and provides a request body for the HTTP request.
 *
 * @param method The HTTP method (e.g., GET, POST, PUT, DELETE) for the request.
 * @param body The body of the request, which will be serialized to a string.
 * @since 1.0.0
 */
@JvmName("methodAnyBody")
fun HttpRequest.Builder.method(method: HttpMethod, body: Any) = 
    method(method.value, HttpRequest.BodyPublishers.ofString(body.serialize()))!!

/**
 * Sends an HTTP request using the provided body handler to process the response.
 *
 * @param T The type of the response body.
 * @param bodyHandler The handler used to process the HTTP response body.
 * @return The processed HTTP response with the type specified by the body handler.
 *
 * This function requires an HttpClient context to execute the request.
 * @since 4.7.0
 */
context(client: HttpClient)
fun <T> HttpRequest.send(bodyHandler: HttpResponse.BodyHandler<T>) =
    client.send(this, bodyHandler)!!
/**
 * Sends an HTTP request asynchronously using the provided body handler to process the response.
 *
 * @param bodyHandler The handler used to process the HTTP response body.
 * @return The processed HTTP response object.
 * @since 4.7.0
 */
context(client: HttpClient)
fun <T> HttpRequest.sendAsync(bodyHandler: HttpResponse.BodyHandler<T>) =
    client.sendAsync(this, bodyHandler)!!
/**
 * Sends the HTTP request asynchronously using the provided body handler and push promise handler.
 *
 * @param T The type of the response body.
 * @param bodyHandler The body handler used to process the HTTP response body.
 * @param pushPromiseHandler The push promise handler used to handle server push promises.
 * @receiver The HTTP request to be sent.
 * @context client The HTTP client used to send the request.
 * @return The asynchronous HTTP response.
 * @since 4.7.0
 */
context(client: HttpClient)
fun <T> HttpRequest.sendAsync(bodyHandler: HttpResponse.BodyHandler<T>, pushPromiseHandler: HttpResponse.PushPromiseHandler<T>) =
    client.sendAsync(this, bodyHandler)!!

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
 * @since 3.0.0
 */
fun String.toUri() = runCatching { Uri(this) }
/**
 * Converts the string to a URL wrapped in a Result.
 * The method first attempts to convert the string to a URI and, if successful, converts the URI to a URL.
 *
 * @return A [Result] containing the [URL] if the conversion was successful, or an exception if it failed.
 * @since 3.0.0
 */
fun String.toUrl(): Result<Url> = runCatching { toUri()().toURL() }

/**
 * Converts this URL to a Uri.
 *
 * @return a [Uri] representation of this URL.
 * @since 3.0.3
 */
fun URL.toUri(): Uri = toURI()
/**
 * Converts the URI instance into a Url object.
 *
 * @return the Url representation of the URI.
 * @since 3.0.3
 */
fun URI.toUrl(): Url = toURL()

/**
 * Executes the provided action if the HTTP status is considered successful.
 *
 * @param action the action to perform if the HTTP status is successful
 * @return the current HTTP status
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifSuccessful(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isSuccessful) action(this)
    return this
}
/**
 * Executes the given action if the HTTP status is not successful.
 *
 * @param action a consumer that accepts the current HttpStatus if it is not successful
 * @return the current HttpStatus instance
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifNotSuccessful(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (!isSuccessful) action(this)
    return this
}
/**
 * Executes the specified action if the HTTP status represents an error.
 *
 * @param action a consumer function to be executed if the HTTP status is an error
 * @return the same HTTP status instance on which the method was called
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifError(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isError) action(this)
    return this
}
/**
 * Executes the given action if the current HTTP status is not an error status.
 *
 * @param action the action to be executed if the HTTP status is not an error.
 * @return the current HTTP status.
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifNotError(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (!isError) action(this)
    return this
}
/**
 * Executes the provided action if the HTTP status represents a client error (4xx status code).
 *
 * @param action A function to be invoked with the current HTTP status if it is a client error.
 * @return The current `HttpStatus` instance.
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifClientError(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isClientError) action(this)
    return this
}
/**
 * Executes the given action if the HTTP status is not a client error (4xx).
 *
 * @param action A consumer that takes the current HTTP status as a parameter and performs an operation.
 * @return The current HTTP status.
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifNotClientError(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (!isClientError) action(this)
    return this
}
/**
 * Executes the provided action if the HTTP status represents a server error.
 *
 * @param action a consumer lambda that accepts the current HTTP status, executed if the status is a server error
 * @return the current HTTP status
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifServerError(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isServerError) action(this)
    return this
}
/**
 * Executes the given action if the HTTP status does not represent a server error.
 *
 * @param action A consumer that takes the current HTTP status as input and performs an action.
 * @return The current HTTP status, regardless of whether the action was executed.
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifNotServerError(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (!isServerError) action(this)
    return this
}
/**
 * Executes the given action if the HTTP status code is informational (1xx).
 *
 * @param action The action to be executed if the HTTP status is informational.
 * @return The current HttpStatus instance.
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifInformational(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isInformational) action(this)
    return this
}
/**
 * Executes the specified action if the HTTP status is not informational.
 *
 * @param action The action to be performed on the HTTP status if it is not informational.
 * @return The original HTTP status.
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifNotInformational(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isInformational) action(this)
    return this
}
/**
 * Executes the given action if the HTTP status represents a redirection status code (3xx).
 *
 * @param action A consumer function that will be invoked with the current HTTP status
 *               if it is a redirection status.
 * @return The current HTTP status.
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifRedirection(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isRedirection) action(this)
    return this
}
/**
 * Invokes the given action if the HTTP status is not a redirection status.
 *
 * @param action A consumer that performs an operation on the `HttpStatus` if it is not a redirection.
 * @return The same `HttpStatus` instance on which the method was called.
 * @since 5.1.0
 */
@IgnorableReturnValue
inline fun HttpStatus.ifNotRedirection(action: Consumer<HttpStatus>): HttpStatus {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isRedirection) action(this)
    return this
}