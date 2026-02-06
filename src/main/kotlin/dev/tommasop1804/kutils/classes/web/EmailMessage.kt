package dev.tommasop1804.kutils.classes.web

import dev.tommasop1804.kutils.classes.registry.Contact
import dev.tommasop1804.kutils.isNotNull
import java.io.File

/**
 * Represents an email message with essential information such as sender, recipients, subject, body content, 
 * and optional attachments. This class provides utility properties to easily check the presence of optional 
 * email components like CC, BCC, attachments, and body content.
 *
 * @property from The sender's email address.
 * @property to The list of recipient email addresses.
 * @property cc The list of carbon copy (CC) email addresses. Default is an empty list.
 * @property bcc The list of blind carbon copy (BCC) email addresses. Default is an empty list.
 * @property subject The subject of the email. Default is null.
 * @property textBody The plain text content of the email. Default is null.
 * @property htmlBody The HTML content of the email. Default is null.
 * @property attachments The list of attached files to the email. Default is an empty list.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
data class EmailMessage(
    val from: Contact.Email,
    val to: List<Contact.Email>,
    val cc: List<Contact.Email> = emptyList(),
    val bcc: List<Contact.Email> = emptyList(),
    val subject: String? = null,
    val textBody: String? = null,
    val htmlBody: String? = null,
    val attachments: List<File> = emptyList()
) {
    /**
     * Indicates whether the email message has recipients in the CC (Carbon Copy) field.
     *
     * This property evaluates to `true` if the `cc` list contains at least one recipient, 
     * and `false` otherwise.
     *
     * @since 1.0.0
     */
    val hasCC: Boolean get() = cc.isNotEmpty()
    /**
     * Indicates whether the email message contains any BCC (blind carbon copy) recipients.
     *
     * This property evaluates to `true` if the `bcc` list is not empty, meaning there are
     * one or more recipients specified in the BCC field of the email message.
     *
     * @return `true` if the `bcc` list contains at least one entry, `false` otherwise.
     * @since 1.0.0
     */
    val hasBCC: Boolean get() = bcc.isNotEmpty()
    /**
     * Indicates whether the email message has a non-null subject.
     *
     * This property evaluates to `true` if the `subject` field of the containing
     * `EmailMessage` instance is not null, and `false` otherwise.
     *
     * @since 1.0.0
     */
    val hasSubject: Boolean get() = subject.isNotNull()
    /**
     * Indicates whether the email message contains a text body.
     *
     * This property returns `true` if the `textBody` of the email message is not null; otherwise, it returns `false`.
     * It can be used to check the existence of a plain text representation in the message before performing 
     * operations that depend on the existence of a text body.
     *
     * @since 1.0.0
     */
    val hasTextBody: Boolean get() = textBody.isNotNull()
    /**
     * Indicates whether the email message contains an HTML body.
     *
     * This property evaluates to `true` if the `htmlBody` field is not null, and `false` otherwise.
     *
     * @since 1.0.0
     */
    val hasHtmlBody: Boolean get() = htmlBody.isNotNull()
    /**
     * Indicates whether the message contains a body, either in plain text or HTML format.
     * Returns true if the message has a text body or an HTML body; false otherwise.
     * 
     * This property provides a convenient way to check if the message includes
     * any form of content body, regardless of its format.
     * 
     * @since 1.0.0
     */
    val hasBody: Boolean get() = hasTextBody || hasHtmlBody
    /**
     * Indicates whether the email message contains one or more attachments.
     *
     * This property evaluates the `attachments` list to determine if it has any elements.
     * Useful for identifying email messages that include additional files.
     *
     * @since 1.0.0
     */
    val hasAttachments: Boolean get() = attachments.isNotEmpty()
}