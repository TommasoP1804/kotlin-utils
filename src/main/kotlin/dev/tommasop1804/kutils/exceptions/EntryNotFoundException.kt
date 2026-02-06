package dev.tommasop1804.kutils.exceptions

import kotlin.reflect.KClass

/**
 * Represents an exception that is thrown when a specific entry is not found within an enumeration.
 *
 * This exception is typically used to signal the absence of a matching entry in an enum class.
 * It includes constructors for providing a detailed message, the missing enum class, or the underlying cause
 * of the exception, enabling flexibility depending on the context in which the exception is raised.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
class EntryNotFoundException : IllegalArgumentException {
    /**
     * Initializes a new instance of the EntryNotFoundException class with no detail message.
     *
     * This constructor creates an exception instance without additional context
     * or a specific error message, indicating a general case of missing entry.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an `EntryNotFoundException` with a specific message indicating
     * that a value was not found for the provided enum class.
     *
     * This exception is generated when a value does not correspond to any entry
     * in the given enumeration class. It is useful for scenarios where strict
     * mapping between values and enum entries is required, and an invalid or
     * non-matching value is encountered.
     *
     * @param enum The `KClass` of the enum used for the lookup.
     * @param value The value that was not found in the specified `enum`.
     * @since 1.0.0
     */
    constructor(enum: KClass<out Enum<*>>, value: Any?) : super("Entry $value not found for enum ${enum.simpleName}")
    /**
     * Constructs an `EntryNotFoundException` with a detail message indicating that an entry
     * was not found for the specified enum type.
     *
     * This constructor allows specifying the enum class that caused the exception to provide
     * more context about the missing entry during runtime.
     *
     * @param enum The enum class for which the entry was not found.
     * @since 1.0.0
     */
    constructor(enum: KClass<out Enum<*>>) : super("Entry not found for enum ${enum.simpleName}")
    /**
     * Constructs an `EntryNotFoundException` with the specified detail message.
     *
     * This constructor allows for creating an exception instance with a detailed message
     * describing the context or reason for the exception. It provides additional
     * information about why the entry was not found, aiding in debugging or logging.
     *
     * @param message A message explaining the cause of the exception, or null if no specific message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs an `EntryNotFoundException` with the specified detail message and cause.
     *
     * This constructor is used to provide additional context about the exception
     * by supplying a descriptive message and an underlying cause. It allows for
     * exception chaining and enables better debugging of the error.
     *
     * @param message A detailed message describing the error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs an `EntryNotFoundException` with the specified cause.
     *
     * This constructor initializes an exception instance to represent a scenario where 
     * an expected entry is not found, while also encapsulating the underlying cause 
     * of the issue. It facilitates exception chaining and provides additional context 
     * about the error.
     *
     * @param cause The cause of this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}