package dev.tommasop1804.kutils.exceptions

/**
 * Represents an exception that is thrown when an encryption operation fails.
 *
 * This exception is a runtime exception designed to indicate issues encountered
 * during data encryption processes. It can occur due to unsupported algorithms,
 * invalid parameters, or other internal encryption-related errors.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused")
open class EncryptionException : RuntimeException {
    /**
     * Initializes a new instance of the EncryptionException class with no detail message.
     *
     * This constructor can be used to create an exception without providing additional context
     * or a specific error message.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs an `EncryptionException` with the specified detail message.
     *
     * This constructor allows for providing additional context about the specific reason for the
     * exception through a custom message. The message can be used for debugging or logging purposes.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a new EncryptionException with the specified detail message and cause.
     *
     * This constructor allows for providing an error message and the underlying cause of the exception.
     * It is useful for including additional context regarding the encryption-related error and
     * facilitating exception chaining.
     *
     * @param message A message that provides additional information about this exception, or null if not specified.
     * @param cause The cause of this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a new EncryptionException with the specified cause.
     *
     * This constructor initializes an EncryptionException instance using a Throwable
     * that caused this exception. This allows for exception chaining and error analysis.
     *
     * @param cause The underlying cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Represents an exception that is thrown when a decryption operation fails.
 *
 * This exception is a runtime exception intended to signal issues encountered
 * during data decryption processes. It can occur due to unsupported algorithms,
 * corrupted data, invalid keys, or other internal decryption-related errors.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused")
open class DecryptionException : RuntimeException {
    /**
     * Initializes a new instance of the DecryptionException class with no detail message.
     *
     * This constructor is used to create an exception instance without providing
     * any additional context or a specific error message.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a `DecryptionException` with the specified detail message.
     *
     * This constructor allows providing additional context about a decryption-related error.
     * The detail message can assist in debugging or logging the specifics of the exception.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)
    /**
     * Constructs a `DecryptionException` with a specified detail message and cause.
     *
     * This constructor allows for providing a custom error message and the underlying
     * cause of the decryption failure. It enables exception chaining and helps in
     * understanding the root cause of the error.
     *
     * @param message The detailed error message, or null if no specific message is provided.
     * @param cause The cause of the exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    /**
     * Constructs a `DecryptionException` with the specified cause.
     *
     * This constructor allows for creating an exception instance that indicates
     * an error during the decryption process caused by another exception,
     * enabling exception chaining and providing more context to the underlying issue.
     *
     * @param cause The underlying cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)
}