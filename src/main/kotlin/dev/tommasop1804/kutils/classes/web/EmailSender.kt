package dev.tommasop1804.kutils.classes.web

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.exceptions.EmailSendingException
import dev.tommasop1804.kutils.exceptions.RequiredParameterException
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeBodyPart
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeMultipart
import java.util.*

/**
 * Represents a service responsible for sending email messages.
 * This interface provides methods to send `EmailMessage` objects using different approaches.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
interface EmailSender {
    /**
     * Sends the email message to the specified recipients.
     *
     * This method uses the properties of the `EmailMessage` instance to send the email.
     * It is expected that the `from`, `to`, and optionally `cc` and `bcc` fields are populated.
     * If a subject, text body, HTML body, or attachments are provided, they will be included in the sent message.
     *
     * The sending process may depend on the implementation of the containing `EmailSender` interface
     * or any other underlying infrastructure or protocol mechanisms.
     *
     * @receiver The instance of `EmailMessage` that needs to be sent.
     * @throws EmailSendingException If there is an issue during the sending process
     * (e.g., connectivity issues, server errors, or invalid email formats).
     * @since 1.0.0
     */
    fun send(emailMessage: EmailMessage)
}

/**
 * A concrete implementation of the `EmailSender` interface that sends email messages using SMTP.
 * This class requires SMTP server details such as host, port, username, and either a password or
 * a token provider for authentication. Optionally, it supports enabling or disabling TLS.
 *
 * @param host The SMTP server hostname.
 * @param port The port number of the SMTP server.
 * @param username The username for SMTP authentication.
 * @param tokenProvider A supplier function that provides a token for authentication (optional).
 * @param password The password for SMTP authentication (optional).
 * @param useTLS Specifies whether to use TLS for the connection. Default is `true`.
 * @throws RequiredParameterException If neither `tokenProvider` nor `password` is provided.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
class SMTPEmailSender(
    private val host: String,
    private val port: Int,
    private val username: String,
    private val tokenProvider: Supplier<String>? = null,
    private val password: String? = null,
    private val useTLS: Boolean = true
) : EmailSender {
    /**
     * Represents an SMTP session initialized to send emails using the provided SMTP server configuration.
     *
     * This field is a critical component of the email sending functionality, encapsulating
     * the connection properties and authentication details for the SMTP server.
     *
     * The session object is used to construct and send emails while ensuring compliance
     * with the server's security and connection requirements. Authentication is typically
     * handled using the `username` and either the `password` or `tokenProvider`.
     *
     * It supports features like the use of TLS (`useTLS`) for secure communication and ensures
     * interoperability with the configured `host` and `port`.
     *
     * @since 1.0.0
     */
    private val session: Session

    init {
        if (tokenProvider.isNull() && password.isNull()) throw RequiredParameterException("You must provide a tokenProvider and a password")

        val props = Properties() apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", useTLS.toString())
            put("mail.smtp.host", host)
            put("mail.smtp.port", port.toString())
            if (tokenProvider.isNotNull()) put("mail.smtp.auth.mechanism", "XOAUTH2")
        }
        session = Session.getInstance(props, object : Authenticator() {
            /**
             * Provides authentication details for accessing an SMTP service.
             *
             * This method returns a `PasswordAuthentication` object containing the username
             * and credentials required to authenticate a connection. If a token provider
             * is available, it will supply the authentication token; otherwise, the password
             * field is used as the credential.
             *
             * @return A `PasswordAuthentication` instance containing the username and authentication credentials.
             * @since 1.0.0
             */
            override fun getPasswordAuthentication(): PasswordAuthentication = PasswordAuthentication(
                username,
                if (tokenProvider.isNotNull()) tokenProvider() else password
            )
        })
    }

    /**
     * Sends an email message using the SMTP protocol.
     *
     * This method constructs an email message based on the details provided in the `EmailMessage` instance. It includes
     * handling of recipients (To, CC, BCC), subject, body (plain text or HTML), and attachments. The constructed
     * email is sent via the underlying JavaMail `Transport` API.
     *
     * The method utilizes `tryOrThrow` to capture and handle any exceptions that occur during the email sending process.
     * If an error happens, it throws an `EmailSendingException` with the relevant details about the failed email message.
     *
     * Email body content is determined in the following order of priority:
     * 1. HTML body (`htmlBody`) if available.
     * 2. Plain text body (`textBody`) if HTML body is not provided.
     * 3. An empty value if neither is set.
     *
     * Attachments are added to the email as MIME parts, and multiple recipients (To, CC, BCC) are supported.
     *
     * @param emailMessage The `EmailMessage` instance containing the email's configuration and content.
     * @throws EmailSendingException If sending the email fails due to issues like connectivity, authentication, or invalid configuration.
     * @since 1.0.0
     */
    override fun send(emailMessage: EmailMessage) = with(emailMessage) {
        tryOrThrow({ -> EmailSendingException(this@with) }, overwriteOnly = MessagingException::class) {
            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress(from.value))

            to.forEach { mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(it.toString())) }
            cc.forEach { mimeMessage.addRecipient(Message.RecipientType.CC, InternetAddress(it.toString())) }
            bcc.forEach { mimeMessage.addRecipient(Message.RecipientType.BCC, InternetAddress(it.toString())) }

            mimeMessage.subject = subject

            val multipart = MimeMultipart()

            val bodyPart = MimeBodyPart()
            when {
                htmlBody.isNotNull() -> bodyPart.setContent(htmlBody, "text/html; charset=utf-8")
                textBody.isNotNull() -> bodyPart.setText(textBody)
                else -> bodyPart.setText(String.EMPTY) // Fallback vuoto
            }
            multipart.addBodyPart(bodyPart)

            // 2. Parte Allegati
            attachments.forEach { file ->
                val attachmentPart = MimeBodyPart()
                attachmentPart.attachFile(file)
                multipart.addBodyPart(attachmentPart)
            }

            mimeMessage.setContent(multipart)

            Transport.send(mimeMessage)
        }
    }
}