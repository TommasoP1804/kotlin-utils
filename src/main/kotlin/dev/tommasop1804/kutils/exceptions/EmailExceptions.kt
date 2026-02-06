@file:Suppress("unused")

package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.classes.web.EmailMessage

/**
 * An exception class that represents errors related to email processing or sending.
 *
 * This exception is typically thrown when there are issues constructing, processing, 
 * or sending an email. It provides multiple constructors to handle a variety of 
 * initialization scenarios.
 *
 * @constructor Creates an instance of EmailException with no message.
 * @since 1.0.0
 */
open class EmailException : RuntimeException {
    /**
     * Default constructor for the `EmailException` class.
     * Initializes a new instance of `EmailException` without any specific message or cause.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructor for creating an instance of EmailException with a specific error message.
     *
     * @param message The detail message explaining the reason for the exception.
     * @since 1.0.0
     */
    constructor(message: String) : super(message)
    /**
     * Constructs a new EmailException with the specified detail message and cause.
     *
     * @param message the detail message, saved for later retrieval by the [Throwable.message] method.
     * @param cause the cause of the exception, saved for later retrieval by the [Throwable.cause] method.
     *              A null value is permitted, and indicates that the cause is nonexistent or unknown.
     * @since 1.0.0
     */
    constructor(message: String, cause: Throwable) : super(message, cause)
    /**
     * Constructs a new [EmailException] with the specified cause.
     *
     * @param cause the cause of the exception
     * @since 1.0.0
     */
    constructor(cause: Throwable) : super(cause)
    /**
     * Constructs an EmailException with a message indicating an error occurred while sending the specified email message.
     *
     * @param emailMessage the email message that caused the exception
     * @param message an optional custom error message. If null, a default message using the subject of the emailMessage will be used
     * @since 1.0.0
     */
    constructor(emailMessage: EmailMessage, message: String? = null) : super(message ?: "Error sending email: ${emailMessage.subject}")
}

/**
 * An exception that is thrown to indicate failures occurring during the process of sending an email.
 * This exception extends the `EmailException` class, providing additional context or customization.
 *
 * @constructor Creates a new instance of `EmailSendingException` with optional parameters for detailed error information.
 * - If no arguments are provided, a generic exception is initialized.
 * - If a message is provided, it describes the reason for the exception.
 * - If both a message and a cause are provided, they define the reason and the root cause of the exception.
 * - If only a cause is provided, it defines the root cause of the exception.
 * - If an `EmailMessage` object is provided, it includes additional context about the failed email along with an optional message.
 *
 * @since 1.0.0
 */
open class EmailSendingException : EmailException {
    /**
     * Initializes a new instance of the EmailSendingException class without any additional information.
     *
     * This constructor is typically used when no specific message or cause is provided for the exception.
     *
     * @since 1.0.0
     */
    constructor() : super()
    /**
     * Constructs a new instance of the EmailSendingException with the specified detail message.
     *
     * @param message The detail message to be associated with this exception.
     * @since 1.0.0
     */
    constructor(message: String) : super(message)
    /**
     * Constructs a new EmailSendingException with the specified detail message and cause.
     *
     * @param message The detail message, saved for later retrieval by the [Throwable.message] property.
     * @param cause The cause of the exception, saved for later retrieval by the [Throwable.cause] property.
     * @since 1.0.0
     */
    constructor(message: String, cause: Throwable) : super(message, cause)
    /**
     * Constructs a new `EmailSendingException` with the specified cause.
     *
     * This constructor allows wrapping a `Throwable` as the cause of the exception.
     *
     * @param cause The cause of the exception. A `null` value is permitted and indicates no cause.
     * @since 1.0.0
     */
    constructor(cause: Throwable) : super(cause)
    /**
     * Constructs a new instance of the exception using the specified email message and optional message.
     *
     * @param emailMessage The email message associated with the exception.
     * @param message An optional detail message, providing additional context about the exception.
     *                Defaults to `null` if not specified.
     * @since 1.0.0
     */
    constructor(emailMessage: EmailMessage, message: String? = null) : super(emailMessage, message)
}