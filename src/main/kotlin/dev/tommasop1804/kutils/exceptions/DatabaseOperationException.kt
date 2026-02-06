package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.classes.coding.SqlQuery

/**
 * Represents a custom exception that is thrown during database operation failures.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class DatabaseOperationException : RuntimeException {
    /**
     * Initializes a new instance of the DatabaseOperationException class with no detail message.
     *
     * This constructor creates a generic instance of the `DatabaseOperationException` class 
     * with no additional context or specific error details. It can be used to indicate an 
     * unspecified database operation failure.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new DatabaseOperationException with a detailed error message
     * indicating the query that caused the exception.
     *
     * @param query the SQL query that triggered the exception
     * @since 1.0.0
     */
    constructor(query: SqlQuery) : super("Error executing query: $query")
    /**
     * Constructs a `DatabaseOperationException` with the specified detail message.
     *
     * This constructor enables the creation of an exception instance to provide a detailed
     * message about the database operation failure. The message can offer context
     * or additional error details to help identify the reason for the exception.
     *
     * @param message The detail message associated with the exception, or null if no message is specified.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `DatabaseOperationException` with the specified detail message and cause.
     *
     * This constructor allows for creating an exception instance by providing a detailed
     * error message describing the database operation failure and the underlying cause.
     * It facilitates exception chaining and adds more context to the error during
     * database operations.
     *
     * @param message A detailed message describing the database operation error, or null if no specific message is provided.
     * @param cause The underlying cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `DatabaseOperationException` with the specified cause.
     *
     * This constructor is used to create an exception instance that wraps the provided
     * cause, enabling exception chaining and helping to identify the underlying issue
     * related to database operations.
     *
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}