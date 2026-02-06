package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.EMPTY
import dev.tommasop1804.kutils.before
import dev.tommasop1804.kutils.classes.web.HttpMethod
import dev.tommasop1804.kutils.isNotNull
import java.net.URI

/**
 * Represents an exception that occurs during HTTP-related operations.
 *
 * This exception is typically thrown when an HTTP request fails or an unexpected
 * error occurs during communication with a server. It provides multiple constructors
 * to enable detailed and flexible error reporting, including the status code, URI,
 * HTTP method, and an optional error message for further context.
 *
 * @since 1.0.0
 */
@Suppress("unused")
open class HttpException : RuntimeException {
    /**
     * Constructs a default instance of the `HttpException` class with no specific details.
     *
     * This constructor creates a generic `HttpException` with no additional context or error message,
     * primarily used as a placeholder or for scenarios where no additional information about the error is required.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an `HttpException` with a dynamically generated message based on the provided parameters.
     *
     * This constructor creates an exception instance with a message that describes an error
     * occurring during an HTTP operation. The message is dynamically constructed using the following:
     * - The HTTP status code, if provided.
     * - The HTTP method, if provided.
     * - The URI involved in the operation, if provided.
     * - An additional error message, if provided.
     *
     * The intent of this constructor is to provide a detailed and meaningful description
     * of the HTTP error, which can aid in debugging and logging.
     *
     * @param statusCode The HTTP status code associated with the error, or null if not specified.
     * @param uri The `URI` object representing the target of the HTTP call, or null if not specified.
     * @param method The HTTP method (e.g., GET, POST) used in the operation, or null if not specified.
     * @param errorMessage An optional error message providing additional context, or null if not specified.
     * @since 1.0.0
     */
    constructor(statusCode: Int?, uri: URI? = null, method: HttpMethod?, errorMessage: String? = null) : super("Error ${if (statusCode.isNotNull()) "$statusCode " else " "}calling ${if (method.isNotNull()) "$method " else ""}${if (uri.isNotNull()) "$uri" else "an URI"}${if (errorMessage.isNotNull()) ": $errorMessage" else ""}")
    /**
     * Constructs an `HttpException` with the specified detail message.
     *
     * This constructor is used to create an exception instance with a specific
     * error message, which provides additional context about the HTTP-related issue.
     * It is helpful in debugging and logging issues where only a message describing
     * the exception is relevant.
     *
     * @param message The detail message associated with the exception, or null if no message is specified.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Initializes a new instance of the HttpException class with the specified detail message and cause.
     *
     * This constructor allows for creating an HttpException by providing a message describing the error and 
     * the underlying cause. It is useful for capturing additional context about HTTP-related failures and 
     * for enabling exception chaining.
     *
     * @param message A detailed message describing the HTTP error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no underlying cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs an `HttpException` with the specified cause.
     *
     * This constructor allows creating an `HttpException` instance by specifying
     * the underlying cause of the HTTP error. It helps in exception chaining and
     * provides context about the origin of the exception.
     *
     * @param cause The root cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that occurs during an HTTP request.
 *
 * This exception is a specialized form of [HttpException] and provides additional constructors
 * for creating instances with specific error details such as status code, URI, HTTP method,
 * and custom error messages. It is designed to be used when handling failures or unexpected
 * errors in HTTP-related operations.
 *
 * @since 1.0.0
 */
@Suppress("unused")
open class HttpRequestException : HttpException {
    /**
     * Initializes a new instance of the HttpRequestException class with no detail message.
     *
     * This constructor creates an exception without providing additional context or
     * specific error message, indicating a generic HTTP request-related failure.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an `HttpRequestException` with the specified status code, URI, HTTP method, and error message.
     *
     * This constructor allows creating an instance of the exception by specifying details about
     * the HTTP request that caused the exception, including the status code, the requested URI,
     * the HTTP method used, and a descriptive error message.
     *
     * @param statusCode The HTTP status code associated with the exception, or null if not specified.
     * @param uri The URI related to the HTTP request, or null if not applicable.
     * @param method The HTTP method used in the request (e.g., GET, POST), or null if not provided.
     * @param errorMessage A detailed error message describing the exception, or null if not specified.
     * @since 1.0.0
     */
    constructor(statusCode: Int?, uri: URI? = null, method: HttpMethod?, errorMessage: String? = null) : super(statusCode, uri, method, errorMessage)
    /**
     * Constructs an `HttpRequestException` with the specified detail message.
     *
     * This constructor allows for providing additional context about the HTTP request-related
     * error by attaching a descriptive message. The message can be used for debugging,
     * logging, or displaying clearer error information.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an instance of `HttpRequestException` with the specified detail message and cause.
     *
     * This constructor allows for providing a detailed error message along with the underlying
     * cause of the error. It facilitates exception chaining and provides additional context 
     * for debugging HTTP request-related issues.
     *
     * @param message A detailed message describing the HTTP request error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs an `HttpRequestException` with the specified cause.
     *
     * This constructor allows the creation of an `HttpRequestException` instance by specifying
     * the underlying cause of the exception. It facilitates exception chaining and provides
     * context for the root cause of HTTP request-related issues.
     *
     * @param cause The cause of this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that occurs during HTTP response handling.
 *
 * This exception is a subclass of [HttpException] and is specifically used to represent errors
 * related to HTTP responses. It provides multiple constructors to enable flexible and detailed
 * error reporting in various scenarios, including additional context such as the HTTP status code,
 * URI, HTTP method, and optional error messages.
 *
 * @since 1.0.0
 */
@Suppress("unused")
open class HttpResponseException : HttpException {
    /**
     * Initializes a new instance of the HttpResponseException class with no detail message.
     *
     * This constructor creates an exception instance without any additional context
     * or specific error message, representing a generic HTTP response-related error.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an `HttpResponseException` with the specified status code, URI, HTTP method, 
     * and error message. This constructor provides flexibility for creating an exception 
     * instance by optionally specifying relevant information about the failed HTTP response.
     *
     * @param statusCode The HTTP status code associated with the response, or null if not specified.
     * @param uri The URI of the request that triggered the exception, or null if not available.
     * @param method The HTTP method used in the request, or null if not specified.
     * @param errorMessage A detailed message describing the error, or null if no specific message is provided.
     * @since 1.0.0
     */
    constructor(statusCode: Int?, uri: URI? = null, method: HttpMethod?, errorMessage: String? = null) : super(statusCode, uri, method, errorMessage)
    /**
     * Constructs an `HttpResponseException` with the specified detail message.
     *
     * This constructor is used to create an instance of `HttpResponseException`
     * by providing a detailed message describing the HTTP response-related error. 
     * It enables the encapsulation of additional context regarding the error 
     * during exception handling or debugging.
     *
     * @param message The detail message associated with the exception, or null if no specific message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an `HttpResponseException` with the specified detail message and cause.
     *
     * This constructor allows for creating an instance of `HttpResponseException` by specifying
     * a detailed error message and an underlying cause. It facilitates exception chaining and provides
     * a way to include additional context about an HTTP response-related error.
     *
     * @param message A detailed message describing the HTTP error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs an `HttpResponseException` with the specified cause.
     *
     * This constructor allows creating an instance of `HttpResponseException` 
     * by specifying the underlying cause of the error. It is useful for propagating 
     * exceptions and preserving the exception chain for debugging purposes.
     *
     * @param cause The underlying cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that occurs during communication with external services over HTTP.
 *
 * This exception extends `HttpException` and provides specialized constructors for handling
 * errors that occur when interacting with external services. It enables detailed error reporting
 * by including the service name along with other HTTP-related information such as status codes,
 * URIs, HTTP methods, and custom error messages.
 *
 * The exception automatically constructs meaningful error messages based on the provided parameters,
 * making it easier to identify and debug issues related to external service calls.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class ExternalServiceHttpException : HttpException {
    /**
     * Extracts the internal error code from the associated message by retrieving the substring
     * that precedes the delimiter " @@@ ". If the extracted substring is blank, the result is null.
     *
     * This property provides additional context for errors, particularly when specific internal
     * codes are embedded within the message field.
     *
     * @since 1.0.0
     */
    val internalErrorCode: String?
        get() = message?.before(" @@@ ")?.ifBlank { null }

    /**
     * Default constructor for the `ExternalServiceHttpException` class.
     * Initializes a new instance of `ExternalServiceHttpException` without any specific message or cause.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an ExternalServiceHttpException with a service name.
     *
     * If the service name is not null, the exception message will be "Error calling service [serviceName]".
     * If the service name is null, an empty string is used as the message.
     *
     * @param serviceName The name of the external service that encountered an error, or null if not applicable.
     * @param internalErrorCode the internal error code associated with the exception.
     * @since 1.0.0
     */
    constructor(serviceName: String?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + (if (serviceName.isNotNull()) "Error calling service $serviceName" else String.EMPTY))
    /**
     * Creates an ExternalServiceHttpException with detailed HTTP request information.
     *
     * Constructs an exception message that includes the HTTP status code, service name, HTTP method,
     * URI, and optional error message. The message format is:
     * "Error [statusCode] calling [serviceName] [method] [uri][: errorMessage]"
     * where each component is only included if the corresponding parameter is non-null.
     *
     * @param serviceName the name of the external service that was called, or null if unknown
     * @param statusCode the HTTP status code returned by the service, or null if not available
     * @param uri the URI that was called, defaults to null; if null, "an URI" is used in the message
     * @param method the HTTP method used for the request (GET, POST, etc.), or null if not available
     * @param errorMessage an optional additional error message to append to the exception message, defaults to null
     * @param internalErrorCode the internal error code associated with the exception.
     * @since 1.0.0
     */
    constructor(serviceName: String?, statusCode: Int?, uri: URI? = null, method: HttpMethod?, errorMessage: String? = null, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + ("Error ${if (statusCode.isNotNull()) "$statusCode " else " "}calling ${if (serviceName.isNotNull()) "$serviceName " else ""}${if (method.isNotNull()) "$method " else ""}${if (uri.isNotNull()) "$uri" else "an URI"}${if (errorMessage.isNotNull()) ": $errorMessage" else ""}"))
    /**
     * Constructs a new ExternalServiceHttpException with the specified service name and error message.
     *
     * Creates an error message by concatenating "Error calling service {serviceName}" with the provided
     * message parameter. If either parameter is null, it is replaced with an empty string in the 
     * resulting message.
     *
     * @param serviceName The name of the external service that caused the exception. May be null.
     * @param message Additional error message details. May be null.
     * @param internalErrorCode the internal error code associated with the exception.
     * @since 1.0.0
     */
    constructor(serviceName: String?, message: String?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + (if (serviceName.isNotNull()) "Error calling service $serviceName" else String.EMPTY) + (if (message.isNotNull()) ": $message" else String.EMPTY))
    /**
     * Constructs a new ExternalServiceHttpException with the specified service name, detail message, and cause.
     *
     * This constructor creates an exception message by combining the service name (if provided) with
     * the custom message (if provided). The resulting message follows the pattern:
     * "Error calling service {serviceName}{message}". If either parameter is null, that portion
     * is omitted from the final message.
     *
     * @param serviceName The name of the external service that caused the exception. May be null.
     * @param message The detail message providing additional context about the error. May be null.
     * @param cause The underlying cause of this exception. May be null if the cause is nonexistent or unknown.
     * @param internalErrorCode the internal error code associated with the exception.
     * @since 1.0.0
     */
    constructor(serviceName: String?, message: String?, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + (if (serviceName.isNotNull()) "Error calling service $serviceName" else String.EMPTY) + (if (message.isNotNull()) message else String.EMPTY), cause)
    /**
     * Constructs a new ExternalServiceHttpException with the specified service name and cause.
     *
     * This constructor creates an exception with a detail message that includes the service name if provided,
     * and wraps the given cause. If the service name is not null, the message will be formatted as 
     * "Error calling service [serviceName]". If the service name is null, an empty string is used as the message.
     *
     * @param serviceName The name of the external service that caused the exception. May be null.
     * @param cause The underlying cause of the exception. May be null.
     * @param internalErrorCode the internal error code associated with the exception.
     * @since 1.0.0
     */
    constructor(serviceName: String?, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + ((if (serviceName.isNotNull()) "Error calling service $serviceName" else String.EMPTY)), cause)
}