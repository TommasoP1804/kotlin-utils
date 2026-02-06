package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.isNotNull
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

/**
 * Represents an exception that is thrown when there is no matching format available.
 *
 * This exception is intended to signal that a specific operation could not find a
 * format that matches the required criteria or parameters. It may occur in scenarios
 * involving data parsing, processing, or format validation. The class provides
 * multiple constructors to allow flexibility in specifying error details, such as
 * a specific message, a cause, or both, to provide better context for the failure.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class NoMatchingFormatException : MalformedInputException {
    /**
     * Constructs a `NoMatchingFormatException` with no detail message.
     *
     * This constructor initializes a new instance of the `NoMatchingFormatException` class
     * without providing any specific error details. It represents a generic state
     * where no matching format is found or applicable.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `NoMatchingFormatException` with the specified detail message.
     *
     * This constructor is used to provide a message describing the specific details of the
     * exception. It is primarily intended to specify additional context about why the error
     * occurred during a state transition or validation process.
     *
     * @param message The detail message associated with the exception, or null if no specific message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `NoMatchingFormatException` with the specified detail message and cause.
     *
     * This constructor allows for specifying both a detailed error message and the underlying cause
     * of the exception. It is useful for providing more context about the error and facilitating
     * exception chaining.
     *
     * @param message A detailed message describing the format mismatch error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `NoMatchingFormatException` with the specified cause.
     *
     * This constructor creates an instance of the `NoMatchingFormatException` class
     * that encapsulates the root cause of the exception. It enables exception
     * chaining by linking the current exception to an underlying cause.
     *
     * @param cause The underlying cause of the exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that is thrown when a search or query operation yields no results.
 *
 * This exception is designed to handle scenarios where a specific search, lookup, or query
 * does not return any results. It provides multiple constructors to accommodate various use
 * cases, including specifying a custom message, a cause, or both. This exception can be used
 * to signal the absence of expected data in a consistent manner.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class NoResultsException : NoSuchElementException {
    /**
     * Initializes a new instance of the NoResultsException class with no detail message.
     *
     * This constructor creates an exception instance indicating a specific absence
     * of results or data. It does not contain additional context or specific error
     * details, representing a generic scenario where no results are available.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `NoResultsException` with the specified detail message.
     *
     * This constructor allows specifying a detailed message to provide additional
     * context about the error that led to this exception. It can be used to convey
     * specific information about the absence of results in an operation.
     *
     * @param message A string representing the detail message, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `NoResultsException` with the specified cause.
     *
     * This constructor is used to create an exception instance that encapsulates
     * the underlying cause of the absence of results. It allows for exception chaining
     * and provides more context about the root cause of the issue when no results
     * are obtained.
     *
     * @param cause The underlying cause of this exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
    /**
     * Constructs a `NoResultsException` with a specified detail message and cause.
     *
     * This constructor provides a mechanism to include both a descriptive error message
     * and the underlying cause of the exception. It is particularly useful for debugging
     * and exception chaining, helping to identify the root cause of an issue.
     *
     * @param message The detail message describing the exception, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}

/**
 * Represents an exception thrown when the number of results retrieved during an evaluation of a condition
 * is less than the required or acceptable amount.
 *
 * This exception provides multiple constructors to handle various scenarios, including default messages,
 * custom messages, underlying causes, and constraints on the number of acceptable results.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class TooFewResultsException : RuntimeException {
    /**
     * Constructs a new [TooFewResultsException] with a default message.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a [TooFewResultsException] with a specified message.
     *
     * This constructor allows a custom error message to be passed when the exception is thrown,
     * describing the issue in detail.
     *
     * @param message The detail message describing the exception. Cannot be null.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `TooFewResultsException` with a specified cause.
     *
     * This constructor allows initializing the exception with an underlying throwable cause
     * that may provide more context about the error.
     *
     * @param cause the underlying throwable cause of this exception
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
    /**
     * Constructs a [TooFewResultsException] with the specified message and cause.
     *
     * This constructor allows specifying an explanatory message for the exception
     * and an underlying cause that triggered this exception.
     *
     * @param message The detail message providing more information about the exception.
     * @param cause The underlying cause of the exception.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a TooFewResultsException with a message indicating that the number of results
     * retrieved during a condition evaluation is less than the required or acceptable amount.
     *
     * If the `size` parameter is not null, the message specifies the retrieved results and the minimum
     * required number. Otherwise, it only mentions the minimum acceptable number of results.
     *
     * @param size the number of results retrieved, or null if not specified
     * @param minNumber the minimum acceptable number of results for a condition
     * @since 1.0.0
     */
    constructor(size: Int? = null, minNumber: Int = 1) : super("${if (size.isNotNull()) "The results of the search are $size, but the accepted minimum number of results is $minNumber" else "The accepted minimum number of results of this predicate is $minNumber"}.")
    /**
     * Constructs a [TooFewResultsException] with a message indicating that the number of results
     * from the evaluation of a condition does not meet the required constraints defined by the given range.
     *
     * @param size the actual number of results obtained from the evaluation. Defaults to null if not provided.
     * @param range the acceptable range of results. The exception message will indicate that the results must fall within this range.
     *
     * @since 1.0.0
     */
    constructor(size: Int? = null, range: IntRange) :  super("${if (size.isNotNull()) "The results of the search are $size, but the accepted number of results must be inside $range" else "The accepted number of results of this predicate must be inside $range"}.")
}

/**
 * Exception thrown to indicate that too many results were returned when
 * evaluating a condition.
 *
 * This exception is typically used to enforce constraints that limit the
 * acceptable number of results, commonly for queries or predicates that
 * require a specific range or exact count of results.
 *
 * The exception provides multiple constructors to allow for additional
 * context, such as the number of results returned, the maximum allowed
 * result count, or a custom error message.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class TooManyResultsException : RuntimeException {
    /**
     * Initializes a new instance of the TooManyResultsException class with no additional information.
     *
     * This constructor creates an exception instance with no message or cause, useful as a general placeholder.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a TooManyResultsException with the specified detail message.
     *
     * @param message The detail message describing the exception.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `TooManyResultsException` with a specified cause.
     *
     * This constructor initializes the exception with the throwable that caused this exception.
     *
     * @param cause the underlying cause of this exception
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
    /**
     * Constructs a new instance of TooManyResultsException with a specified detail message and cause.
     *
     * This constructor allows providing additional context about the exception, combining a descriptive message
     * and a throwable cause to specify the reason for the exception's occurrence.
     *
     * @param message The detail message describing the exception. Must not be null.
     * @param cause The underlying cause of the exception. Must not be null.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `TooManyResultsException` with an optional size and a required maximum number of results.
     *
     * If the provided size is not null, the exception message will include both the predicate results size
     * and the maximum allowable number of results. Otherwise, the message will state the maximum limit on the results.
     *
     * @param size The actual number of results from a predicate, or null if unspecified.
     * @param maxNumber The maximum allowable number of results from a predicate. Default value is 1.
     * @since 1.0.0
     */
    constructor(size: Int? = null, maxNumber: Int = 1) : super("${if (size.isNotNull()) "The results of the search are $size, but the accepted max number of results is $maxNumber" else "The accepted maximum number of results of this predicate is $maxNumber"}.")
    /**
     * Constructs an exception that indicates the number of results for a predicate
     * has exceeded the defined acceptable range.
     *
     * @param size the actual number of results; if null, no specific number is provided.
     * @param range the acceptable range for the number of results.
     * @since 1.0.0
     */
    constructor(size: Int? = null, range: IntRange) :  super("${if (size.isNotNull()) "The results of the search are $size, but the accepted number of results must be inside $range" else "The accepted number of results of this predicate must be inside $range"}.")
}

/**
 * Represents an exception indicating that an iterable, collection, or property has fewer elements
 * than required or acceptable, based on defined size or range constraints.
 *
 * This exception extends the `RuntimeException` and is often used to signal violations of minimum
 * size or range requirements for collections, lists, or other iterable structures.
 *
 * Various constructors allow specifying custom messages, causes, and context such as the current
 * size, required size, or range constraints, and the associated property causing the exception.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class TooFewElementsException : RuntimeException {
    /**
     * Initializes a new instance of the TooFewElementsException class.
     *
     * This default constructor creates an exception with no additional context
     * or detailed error message. It invokes the `RuntimeException` parent class
     * with no arguments.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Initializes a new instance of the TooFewElementsException class with a specified detail message.
     *
     * @param message The detail message describing the exception.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new TooFewElementsException with a specified cause of the exception.
     *
     * This constructor allows specifying a [Throwable] that caused this exception, enabling exception chaining.
     *
     * @param cause The [Throwable] that triggered this exception. Can be null.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
    /**
     * Constructs a TooFewElementsException with a detail message and a cause.
     *
     * This constructor allows specifying both a descriptive message and an underlying
     * cause for the exception, providing more context about the error.
     *
     * @param message The detail message explaining the error. Must not be null.
     * @param cause The Throwable that caused this exception. Must not be null.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)

    /**
     * Constructs a TooFewElementsException with a detailed message explaining
     * the mismatch between the expected and minimum number of elements in an iterable.
     *
     * @param size the size of the iterable, or null if not specified
     * @param minNumber the minimum number of elements required
     * @param variableName an optional variable name associated with the iterable, or null if not specified
     * @since 1.0.0
     */
    constructor(size: Int? = null, minNumber: Int = 1, variableName: String? = null) : super("The iterable ${if (variableName.isNotNull()) "$variableName " else ""}${if (size.isNotNull()) "has $size elements, but the minimum number of elements is $minNumber" else "accept a minumum of $minNumber elements"}")
    /**
     * Initializes a new instance of the TooFewElementsException class.
     *
     * This exception is thrown when an iterable object does not meet the constraints
     * regarding the number of its elements. The error message is constructed dynamically
     * based on the provided parameters.
     *
     * @param size the number of elements in the iterable, or null if unspecified
     * @param range the acceptable range for the number of elements
     * @param variableName the name of the iterable variable, or null if unspecified
     * @since 1.0.0
     */
    constructor(size: Int? = null, range: IntRange, variableName: String? = null) : super("The iterable ${if (variableName.isNotNull()) "$variableName " else ""}${if (size.isNotNull()) "has $size elements, but the elements number must be inside $range" else "accept a number of elements inside $range"}")
    /**
     * Constructs a `TooFewElementsException` with a message derived from the specified property, size,
     * minimum number of elements required, and an optional underlying cause.
     *
     * @param property the property that caused the exception
     * @param size the current number of elements, or null if unknown
     * @param minNumber the minimum required number of elements
     * @param cause the underlying cause of the exception, or null if no cause is specified
     * @since 1.0.0
     */
    constructor(property: KProperty<*>, size: Int? = null, minNumber: Int = 1, cause: Throwable? = null) : super("The `${property.returnType}` `${property.name} ${if (size.isNotNull()) "has $size elements, but the minimum number of elements is $minNumber" else "accept a minimum of $minNumber elements"}")
    /**
     * Constructs a new `TooFewElementsException` for a property with an optional size,
     * a required range, and an optional cause.
     *
     * This constructor generates an exception message describing the mismatch
     * between the actual and expected number of elements for a given property,
     * based on the specified size and range requirements.
     *
     * @param property the [KProperty] that caused the exception.
     * @param size the actual number of elements, or null if undefined.
     * @param range the acceptable range for the property elements.
     * @param cause the optional underlying cause of this exception.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>, size: Int? = null, range: IntRange, cause: Throwable? = null) : super("The `${property.returnType}` `${property.name} ${if (size.isNotNull()) "has $size elements, but the elements number must be inside $range" else "accept a number of element inside $range"}")
    /**
     * Constructs a `TooFewElementsException` with a message derived from the specified property, size,
     * minimum number of elements required, and an optional underlying cause.
     *
     * @param parameter the parameter that caused the exception
     * @param size the current number of elements, or null if unknown
     * @param minNumber the minimum required number of elements
     * @param cause the underlying cause of the exception, or null if no cause is specified
     * @since 1.0.0
     */
    constructor(parameter: KParameter, size: Int? = null, minNumber: Int = 1, cause: Throwable? = null) : super("The `${parameter.type}` `${parameter.name} ${if (size.isNotNull()) "has $size elements, but the minimum number of elements is $minNumber" else "accept a minimum of $minNumber elements"}")
    /**
     * Constructs a new `TooFewElementsException` for a property with an optional size,
     * a required range, and an optional cause.
     *
     * This constructor generates an exception message describing the mismatch
     * between the actual and expected number of elements for a given property,
     * based on the specified size and range requirements.
     *
     * @param parameter the [KParameter] that caused the exception.
     * @param size the actual number of elements, or null if undefined.
     * @param range the acceptable range for the property elements.
     * @param cause the optional underlying cause of this exception.
     * @since 1.0.0
     */
    constructor(parameter: KParameter, size: Int? = null, range: IntRange, cause: Throwable? = null) : super("The `${parameter.type}` `${parameter.name} ${if (size.isNotNull()) "has $size elements, but the elements number must be inside $range" else "accept a number of element inside $range"}")
}

/**
 * Represents an exception indicating that an iterable contains too many elements,
 * violating the specified constraint on the maximum allowable number of elements or range.
 *
 * This exception provides various constructors to handle different contexts, such as
 * specifying the actual size of the iterable, the maximum allowed number of elements,
 * acceptable ranges, or related properties.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class TooManyElementsException : RuntimeException {
    /**
     * Initializes a new instance of the TooManyElementsException class with no arguments,
     * providing a default message describing the exception.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `TooManyElementsException` with the specified detail message.
     *
     * @param message A detailed message outlining the nature of the exception.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an instance of [TooManyElementsException] with a specified cause.
     *
     * @param cause the underlying cause of this exception
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
    /**
     * Constructs a TooManyElementsException with a specific error message and a cause.
     *
     * @param message The detail message describing the error.
     * @param cause The underlying cause of the exception.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `TooManyElementsException` with a detailed message based on the size and maximum number
     * of allowed elements for a specific variable.
     *
     * @param size the number of elements currently in the iterable, or null if not specified
     * @param maxNumber the maximum allowable number of elements in the iterable
     * @param variableName the name of the variable associated with the iterable, or null if not applicable
     * @since 1.0.0
     */
    constructor(size: Int? = null, maxNumber: Int = 1, variableName: String? = null) : super("The iterable ${if (variableName.isNotNull()) "$variableName " else ""}${if (size.isNotNull()) "has $size elements, but the max number of elements is $maxNumber" else "accept a maximum of $maxNumber elements"}")
    /**
     * Constructs a `TooManyElementsException` with a detailed error message based on the specified parameters.
     *
     * The error message indicates whether a variable name is provided, as well as the allowed range
     * and the optional size of elements for the iterable. If the size is specified, it mentions the
     * exact number of elements and the range constraint. Otherwise, it states the acceptable range of elements.
     *
     * @param size The number of elements in the iterable, or null if unspecified.
     * @param range The range of acceptable number of elements.
     * @param variableName The name of the variable related to the exception, or null if unspecified.
     * @since 1.0.0
     */
    constructor(size: Int? = null, range: IntRange, variableName: String? = null) : super("The iterable ${if (variableName.isNotNull()) "$variableName " else ""}${if (size.isNotNull()) "has $size elements, but the elements number must be inside $range" else "accept a number of elements inside $range"}")
    /**
     * Constructs a `TooManyElementsException` with a message derived from the provided property details, collection size,
     * maximum allowed number, and an optional cause.
     *
     * The exception message describes the issue based on the runtime type and name of the property provided,
     * along with the specified size constraints. If the `size` parameter is not null, the message specifies the
     * actual number of elements compared to the maximum allowed. Otherwise, the message indicates the maximum
     * accepted number of elements.
     *
     * @param property the property whose value caused the exception
     * @param size the current size of the collection, or null if not specified
     * @param maxNumber the maximum allowed number of elements
     * @param cause an optional throwable representing the cause of this exception
     * @since 1.0.0
     */
    constructor(property: KProperty<*>, size: Int? = null, maxNumber: Int = 1, cause: Throwable? = null) : super("The `${property.returnType}` `${property.name} ${if (size.isNotNull()) "has $size elements, but the max number of elements is $maxNumber" else "accept a maximum of $maxNumber elements"}")
    /**
     * Constructs a `TooManyElementsException` with details about a property, the size of its elements,
     * a specified valid range, and an optional underlying cause.
     *
     * This constructor provides a detailed error message that includes the information about:
     * - The property's type and name.
     * - The current size of the elements (if provided).
     * - The valid range for the number of elements.
     *
     * @param property the property associated with this exception
     * @param size the number of elements currently present, which can be null
     * @param range the range specifying the valid number of elements
     * @param cause the underlying cause of the exception, which can be null
     * @since 1.0.0
     */
    constructor(property: KProperty<*>, size: Int? = null, range: IntRange, cause: Throwable? = null) : super("The `${property.returnType}` `${property.name} ${if (size.isNotNull()) "has $size elements, but the elements number must be inside $range" else "accept a number of element inside $range"}")
    /**
     * Constructs a `TooManyElementsException` with a message derived from the provided property details, collection size,
     * maximum allowed number, and an optional cause.
     *
     * The exception message describes the issue based on the runtime type and name of the property provided,
     * along with the specified size constraints. If the `size` parameter is not null, the message specifies the
     * actual number of elements compared to the maximum allowed. Otherwise, the message indicates the maximum
     * accepted number of elements.
     *
     * @param parameter the parameter whose value caused the exception
     * @param size the current size of the collection, or null if not specified
     * @param maxNumber the maximum allowed number of elements
     * @param cause an optional throwable representing the cause of this exception
     * @since 1.0.0
     */
    constructor(parameter: KParameter, size: Int? = null, maxNumber: Int = 1, cause: Throwable? = null) : super("The `${parameter.type}` `${parameter.name} ${if (size.isNotNull()) "has $size elements, but the max number of elements is $maxNumber" else "accept a maximum of $maxNumber elements"}")
    /**
     * Constructs a `TooManyElementsException` with details about a property, the size of its elements,
     * a specified valid range, and an optional underlying cause.
     *
     * This constructor provides a detailed error message that includes the information about:
     * - The property's type and name.
     * - The current size of the elements (if provided).
     * - The valid range for the number of elements.
     *
     * @param parameter the parameter associated with this exception
     * @param size the number of elements currently present, which can be null
     * @param range the range specifying the valid number of elements
     * @param cause the underlying cause of the exception, which can be null
     * @since 1.0.0
     */
    constructor(parameter: KParameter, size: Int? = null, range: IntRange, cause: Throwable? = null) : super("The `${parameter.type}` `${parameter.name} ${if (size.isNotNull()) "has $size elements, but the elements number must be inside $range" else "accept a number of element inside $range"}")
}