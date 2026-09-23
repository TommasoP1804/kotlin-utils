/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.errors

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.web.*

/**
 * Represents a base contract for HTTP errors that may occur during request or response execution.
 *
 * This interface holds key properties and metadata describing HTTP interactions and errors,
 * including details about the request, response, and any associated error messages.
 *
 * The interface provides two main implementations:
 * - [RequestError]: Represents an error that occurs during the formation or sending of a request.
 * - [ResponseError]: Represents an error that occurs during the reception or processing of a response.
 *
 * Key properties included:
 * - [serviceName]: Name of the service involved in the HTTP operation. Can be null.
 * - [statusCode]: The HTTP status code returned by the service. Can be null.
 * - [uri]: The target URI for the HTTP operation. Can be null.
 * - [method]: The HTTP method used for the operation (e.g., GET, POST).
 * - [requestBody]: The body of the original HTTP request. Can be null.
 * - [responseBody]: The body of the HTTP response, if available. Can be null.
 * - [requestHeaders]: The headers present in the original HTTP request.
 * - [responseHeaders]: The headers in the HTTP response, if available.
 * - [errorMessage]: A descriptive message providing additional context about the error. Can be null.
 *
 * Implementations of this interface help provide clear, structured information about
 * HTTP errors for debugging and handling purposes in client-server communication scenarios.
 *
 * @author Tommaso Pastorelli
 * @since 6.1.0
 */
interface HttpError : Error {
    val serviceName: String?
    val statusCode: Int?
    val uri: Uri?
    val method: String?
    val requestBody: Any?
    val responseBody: Any?
    val requestHeaders: HttpHeaders?
    val responseHeaders: HttpHeaders?
    val errorMessage: String?

    /**
     * Represents an error that occurs during an HTTP request, providing detailed information about the request
     * and response at the time of the failure.
     *
     * This class extends the `HttpError` interface, enabling it to be used wherever an `HttpError` type is expected.
     *
     * @constructor Creates an instance of `RequestError` with specific details about the HTTP error.
     * @param serviceName The optional name of the service associated with the error.
     * @param statusCode The optional HTTP status code returned by the failed request.
     * @param uri The optional URI of the request that led to the error.
     * @param method The HTTP method used in the request.
     * @param requestBody The body of the HTTP request, if any.
     * @param responseBody The body of the HTTP response, if any.
     * @param requestHeaders The headers included in the HTTP request, if any.
     * @param responseHeaders The headers included in the HTTP response, if any.
     * @param errorMessage A textual description of the error, if available.
     *
     * @since 6.1.0
     */
    data class RequestError(
        override val serviceName: String? = null,
        override val statusCode: Int? = null,
        override val uri: Uri? = null,
        override val method: String?,
        override val requestBody: Any?,
        override val responseBody: Any?,
        override val requestHeaders: HttpHeaders?,
        override val responseHeaders: HttpHeaders?,
        override val errorMessage: String? = null
    ) : HttpError {
        constructor(
            serviceName: String? = null,
            statusCode: HttpStatus,
            uri: Uri? = null,
            method: String? = null,
            requestBody: Any? = null,
            responseBody: Any? = null,
            requestHeaders: HttpHeaders? = null,
            responseHeaders: HttpHeaders? = null,
            errorMessage: String? = null
        ) : this(
            serviceName,
            statusCode.value,
            uri,
            method,
            requestBody,
            responseBody,
            requestHeaders,
            responseHeaders,
            errorMessage
        )
        constructor(
            serviceName: String? = null,
            statusCode: Int? = null,
            uri: Uri? = null,
            method: HttpMethod,
            requestBody: Any? = null,
            responseBody: Any? = null,
            requestHeaders: HttpHeaders? = null,
            responseHeaders: HttpHeaders? = null,
            errorMessage: String? = null
        ) : this(
            serviceName,
            statusCode,
            uri,
            method.value,
            requestBody,
            responseBody,
            requestHeaders,
            responseHeaders,
            errorMessage
        )
        constructor(
            serviceName: String? = null,
            statusCode: HttpStatus,
            uri: Uri? = null,
            method: HttpMethod,
            requestBody: Any? = null,
            responseBody: Any? = null,
            requestHeaders: HttpHeaders? = null,
            responseHeaders: HttpHeaders? = null,
            errorMessage: String? = null
        ) : this(
            serviceName,
            statusCode.value,
            uri,
            method.value,
            requestBody,
            responseBody,
            requestHeaders,
            responseHeaders,
            errorMessage
        )
    }
    /**
     * Represents an error response received during an HTTP request. This class provides detailed
     * information about the error encountered, such as HTTP method, URI, request and response details,
     * and any error messages.
     *
     * This class implements the `HttpError` interface.
     *
     * @property serviceName The name of the service associated with the request, if available.
     * @property statusCode The HTTP status code associated with the error, if available.
     * @property uri The URI of the request that caused the error, if available.
     * @property method The HTTP method used in the request that caused the error.
     * @property requestBody The body of the request sent when the error was encountered, if available.
     * @property responseBody The body of the response received when the error was encountered, if available.
     * @property requestHeaders The headers of the request sent, if available.
     * @property responseHeaders The headers of the response received, if available.
     * @property errorMessage An optional detailed error message describing the problem, if available.
     *
     * @since 6.1.0
     */
    data class ResponseError(
        override val serviceName: String? = null,
        override val statusCode: Int? = null,
        override val uri: Uri? = null,
        override val method: String? = null,
        override val requestBody: Any? = null,
        override val responseBody: Any? = null,
        override val requestHeaders: HttpHeaders? = null,
        override val responseHeaders: HttpHeaders? = null,
        override val errorMessage: String? = null
    ) : HttpError {
        constructor(
            serviceName: String? = null,
            statusCode: HttpStatus,
            uri: Uri? = null,
            method: String? = null,
            requestBody: Any? = null,
            responseBody: Any? = null,
            requestHeaders: HttpHeaders? = null,
            responseHeaders: HttpHeaders? = null,
            errorMessage: String? = null
        ) : this(
            serviceName,
            statusCode.value,
            uri,
            method,
            requestBody,
            responseBody,
            requestHeaders,
            responseHeaders,
            errorMessage
        )
        constructor(
            serviceName: String? = null,
            statusCode: Int? = null,
            uri: Uri? = null,
            method: HttpMethod,
            requestBody: Any? = null,
            responseBody: Any? = null,
            requestHeaders: HttpHeaders? = null,
            responseHeaders: HttpHeaders? = null,
            errorMessage: String? = null
        ) : this(
            serviceName,
            statusCode,
            uri,
            method.value,
            requestBody,
            responseBody,
            requestHeaders,
            responseHeaders,
            errorMessage
        )
        constructor(
            serviceName: String? = null,
            statusCode: HttpStatus,
            uri: Uri? = null,
            method: HttpMethod,
            requestBody: Any? = null,
            responseBody: Any? = null,
            requestHeaders: HttpHeaders? = null,
            responseHeaders: HttpHeaders? = null,
            errorMessage: String? = null
        ) : this(
            serviceName,
            statusCode.value,
            uri,
            method.value,
            requestBody,
            responseBody,
            requestHeaders,
            responseHeaders,
            errorMessage
        )
    }
    /**
     * Represents an error response from an external service.
     *
     * This class encapsulates details about a failed HTTP interaction
     * with an external service, including request and response information,
     * as well as any associated error message.
     *
     * @property serviceName The name of the external service where the error occurred.
     * @property statusCode The HTTP status code returned by the external service.
     * @property uri The URI associated with the HTTP request to the external service.
     * @property method The HTTP method used in the request to the external service.
     * @property requestBody The body of the HTTP request sent to the external service.
     * @property responseBody The body of the HTTP response returned by the external service.
     * @property requestHeaders The headers included in the HTTP request to the external service.
     * @property responseHeaders The headers included in the HTTP response from the external service.
     * @property errorMessage A detailed error message describing the nature of the failure.
     *
     * @since 6.1.0
     */
    data class ExternalServiceError(
        override val serviceName: String? = null,
        override val statusCode: Int? = null,
        override val uri: Uri? = null,
        override val method: String? = null,
        override val requestBody: Any? = null,
        override val responseBody: Any? = null,
        override val requestHeaders: HttpHeaders? = null,
        override val responseHeaders: HttpHeaders? = null,
        override val errorMessage: String? = null
    ) : HttpError {
        constructor(
            serviceName: String? = null,
            statusCode: HttpStatus,
            uri: Uri? = null,
            method: String? = null,
            requestBody: Any? = null,
            responseBody: Any? = null,
            requestHeaders: HttpHeaders? = null,
            responseHeaders: HttpHeaders? = null,
            errorMessage: String? = null
        ) : this(
            serviceName,
            statusCode.value,
            uri,
            method,
            requestBody,
            responseBody,
            requestHeaders,
            responseHeaders,
            errorMessage
        )
        constructor(
            serviceName: String? = null,
            statusCode: Int? = null,
            uri: Uri? = null,
            method: HttpMethod,
            requestBody: Any? = null,
            responseBody: Any? = null,
            requestHeaders: HttpHeaders? = null,
            responseHeaders: HttpHeaders? = null,
            errorMessage: String? = null
        ) : this(
            serviceName,
            statusCode,
            uri,
            method.value,
            requestBody,
            responseBody,
            requestHeaders,
            responseHeaders,
            errorMessage
        )
        constructor(
            serviceName: String? = null,
            statusCode: HttpStatus,
            uri: Uri? = null,
            method: HttpMethod,
            requestBody: Any? = null,
            responseBody: Any? = null,
            requestHeaders: HttpHeaders? = null,
            responseHeaders: HttpHeaders? = null,
            errorMessage: String? = null
        ) : this(
            serviceName,
            statusCode.value,
            uri,
            method.value,
            requestBody,
            responseBody,
            requestHeaders,
            responseHeaders,
            errorMessage
        )
    }
}