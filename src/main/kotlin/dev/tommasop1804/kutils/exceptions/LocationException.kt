package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.classes.geography.GeoCoordinate

/**
 * Exception that represents errors related to invalid or unexpected location coordinates.
 *
 * This exception is a subclass of [RuntimeException] and provides multiple constructors
 * to create an instance with a specific error message, a cause, or invalid geolocation coordinates.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class LocationException : RuntimeException {
    /**
     * Initializes a new instance of the LocationException class with no detail message.
     *
     * This constructor creates an exception instance without providing specific context
     * or additional error details, representing a general issue related to location.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new [LocationException] with an error message containing invalid geographical coordinates.
     *
     * @param coordinates The invalid geographic coordinates consisting of latitude and longitude.
     * @since 1.0.0
     */
    constructor(coordinates: GeoCoordinate) : super(
        "Invalid coordinates, latitude: ${coordinates.latitude}, longitude: ${coordinates.longitude}"
    )
    /**
     * Constructs a `LocationException` with the specified detail message.
     *
     * This constructor can be used to provide additional context about
     * the location-related exception. The detail message can help describe
     * the issue encountered during processing.
     *
     * @param message The detail message associated with the exception, or null if no specific message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `LocationException` with the specified detail message and cause.
     *
     * This constructor allows providing a detailed error message and the underlying cause
     * of the location-related issue. It is useful for exception chaining and to give
     * additional context about the root cause of the error.
     *
     * @param message A detailed message describing the location-related issue, or null if no specific message is provided.
     * @param cause The underlying cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `LocationException` with the specified cause.
     *
     * This constructor is used to create an instance of `LocationException` that encapsulates
     * the underlying cause of the location-related error. It allows for exception chaining
     * and provides more context about the root cause of the issue.
     *
     * @param cause The cause of this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}