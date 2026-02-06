@file:Suppress("unused")

package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.EMPTY
import dev.tommasop1804.kutils.isNotNull
import dev.tommasop1804.kutils.isNotNullOrEmpty
import dev.tommasop1804.kutils.ownerClass
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Represents a base class for exceptions related to issues with properties.
 * This class extends `IllegalStateException` and provides constructors to supply
 * additional context about the property that caused the exception, as well as
 * optional cause and message.
 *
 * @since 1.0.0
 */
abstract class PropertyException : IllegalStateException {
    /**
     * Default constructor for the PropertyException class.
     *
     * This constructor initializes a new instance of PropertyException 
     * without any additional message or cause. It delegates to the 
     * parent class's default constructor.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `PropertyException` with information about the specified property 
     * and an optional message.
     *
     * @param property The `KProperty` instance representing the property where the exception occurred.
     * @param message An optional message detailing the reason for the exception.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, message: String? = null) : super((if (property.isNotNull() || message.isNotNull()) "Property" else String.EMPTY) + if (property.isNotNull()) $$" `$${property.ownerClass?.simpleName}`$`$${property.name}` of type `$${property.returnType}`" else String.EMPTY + {if (message.isNotNullOrEmpty()) " $message" else ""})
    /**
     * Constructs a new instance of the exception with the specified detail message.
     *
     * @param message The detail message, which provides more information about the reason for this exception.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new PropertyException with the specified detail message and cause.
     *
     * @param message The detail message to be associated with this exception, or null if no message is provided.
     * @param cause The cause of this exception, or null if the cause is nonexistent or unknown.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new instance of the `PropertyException` class with the specified cause.
     *
     * @param cause The throwable that caused this exception to be thrown. It can be null.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Thrown to indicate that an attempt to access a property has failed due to insufficient accessibility rights.
 * This exception is typically used to signal that a particular property cannot be accessed, often due to 
 * private or protected visibility constraints or other access control restrictions.
 *
 * @since 1.0.0
 */
class PropertyNotAccessibleException : PropertyException {
    /**
     * Constructs a new instance of the PropertyNotAccessibleException with no additional details.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new instance of PropertyNotAccessibleException.
     *
     * @param property The property that is not accessible.
     * @param message Optional detailed message providing additional context.
     * 
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, message: String? = null) : super(property, message)
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message saved for later retrieval by the Throwable.message property.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new `PropertyNotAccessibleException` with the specified detail message
     * and cause. This allows for providing additional context about what caused the exception.
     *
     * @param message the detail message about the exception, which can be null.
     * @param cause the underlying cause of the exception, which can be null.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new instance of PropertyNotAccessibleException with the specified cause.
     *
     * @param cause The cause of the exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Exception indicating that a property is not writable.
 *
 * This exception is thrown when there is an attempt to write to a property
 * that has been explicitly marked as read-only or otherwise restricted
 * from being written to.
 *
 * @see PropertyException
 * @since 1.0.0
 */
class PropertyNotWritableException : PropertyException {
    /**
     * Initializes a new instance of the PropertyNotWritableException class with no additional details.
     * This constructor calls the base class's parameterless constructor.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new instance of PropertyNotWritableException with the specified property and an optional message.
     *
     * @param property The property that is not writable.
     * @param message An optional detail message providing more context about the exception.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, message: String? = null) : super(property, message)
    /**
     * Constructs a new instance of `PropertyNotWritableException` with the specified detail message.
     *
     * @param message The detail message to associate with the exception, or null if no specific message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new `PropertyNotWritableException` with the specified detail message and cause.
     *
     * @param message The detail message, which can be null.
     * @param cause The cause of the exception, which can be null.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new `PropertyNotWritableException` with the specified cause.
     *
     * @param cause The underlying cause of the exception, or `null` if no cause is specified.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Thrown to indicate that an attempt was made to read a property that is not readable.
 * This exception signifies that the property in question cannot be accessed for reading, 
 * potentially due to lack of proper accessors or other domain-specific restrictions.
 *
 * This exception is a specialized form of [PropertyException] and inherits its capabilities 
 * to provide contextual information about the property, such as its name and type.
 *
 * @since 1.0.0
 */
class PropertyNotReadableException : PropertyException {
    /**
     * Constructs an instance of PropertyNotReadableException with no additional information.
     *
     * This constructor invokes the default constructor of the parent PropertyException class.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Creates a new instance of PropertyNotReadableException with the specified property and an optional message.
     *
     * @param property The property that caused the exception.
     * @param message An optional detail message to provide additional context about the exception.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, message: String? = null) : super(property, message)
    /**
     * Constructs a new instance of PropertyNotReadableException with the specified detail message.
     *
     * @param message The detail message providing additional information about the exception.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message, which provides more information about the exception.
     * @param cause the cause of the exception, which may be used to retrieve further details.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new instance of the exception with the specified cause.
     *
     * @param cause The cause of the exception, or null if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Thrown to indicate that a property is not valid.
 *
 * This exception is typically used to signal that an expected validation or constraint
 * on a property has failed. It extends the functionality of [PropertyException] and provides
 * additional constructors for flexible initialization with property references, messages, or causes.
 *
 * @since 1.0.0
 */
class PropertyNotValidException : PropertyException {
    /**
     * Default constructor for PropertyNotValidException.
     *
     * Initializes the exception with no specific message or cause.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `PropertyNotValidException` with the specified property and an optional error message.
     *
     * @param property The property associated with the exception.
     * @param message An optional message providing additional context about the exception.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>?, message: String? = null) : super(property, message)
    /**
     * Constructs a new instance of PropertyNotValidException with the specified detail message.
     *
     * @param message The detail message, which provides additional information about the exception.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new instance of PropertyNotValidException with the specified 
     * detail message and cause. The message provides additional information about 
     * the exception, and the cause specifies the underlying reason for the exception.
     *
     * @param message The detail message, which can be null.
     * @param cause The cause of the exception, which can also be null.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new `PropertyNotValidException` with the specified cause.
     *
     * @param cause The cause of the exception, which can be retrieved later using [Throwable.initCause].
     * Passing `null` indicates that the cause is nonexistent or unknown.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that indicates a failure to find a specific property.
 * This exception is a subclass of [PropertyException] and can be used to provide
 * additional context about the property that could not be located.
 *
 * @since 1.0.0
 */
class PropertyNotFoundException : PropertyException {
    /**
     * Constructs a new `PropertyNotFoundException` with a message indicating that 
     * the specified property of the given class could not be found.
     *
     * @param propertyName The name of the property that was not found.
     * @param `class` The name of the class where the property was expected to be found.
     * @since 1.0.0
     */
    constructor(propertyName: String, `class`: String? = null) : super($$"Property `$${propertyName}`" + (if (`class`.isNotNull()) " of class `$${`class`}`" else String.EMPTY) + " not found")
    /**
     * Constructs a new `PropertyNotFoundException` with a message indicating that
     * the specified property of the given class could not be found.
     *
     * @param propertyName The name of the property that was not found.
     * @param `class` The class where the property was expected to be found.
     * @since 1.0.0
     */
    constructor(propertyName: String, `class`: KClass<*>) : super($$"Property `$${propertyName}` of class `$${`class`.qualifiedName}` not found")
    /**
     * Constructs a new [PropertyNotFoundException] with a custom error message.
     *
     * @param message A detailed message describing the cause of the exception.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new instance of PropertyNotFoundException with the specified detail message
     * and cause.
     *
     * @param message The detail message, which can be null.
     * @param cause The cause of the exception, which can be null.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new `PropertyNotFoundException` with the specified cause.
     *
     * @param cause The cause of the exception, which can be retrieved later using the `Throwable.cause` property.
     *              A null value is permitted, indicating that the cause is nonexistent or unknown.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
    /**
     * Default constructor for the PropertyNotFoundException class.
     * Initializes a new instance of the exception with no specific details.
     * 
     * @since 1.0.0
     */
    constructor() : super()
}