/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.web

import dev.tommasop1804.kutils.*
import org.jetbrains.exposed.v1.core.Table

/**
 * Represents the various types of authentication mechanisms supported.
 * @since 3.12.0
 * @author Tommaso Pastorelli
 */
@MustUseReturnValues
@Suppress("unused")
enum class AuthType {
    /**
     * Represents the HTTP Basic authentication type.
     * This authentication mechanism involves sending the user's
     * credentials (username and password) encoded as a base64 string
     * within the HTTP request's `Authorization` header.
     * @since 4.0.0
     */
    Basic,
    /**
     * Represents the FORM authentication type within the AuthType enum.
     * Typically used when authentication is performed via a form submission.
     * @since 4.0.0
     */
    Form,
    /**
     * Represents the client certificate-based authentication type.
     * This is used when the authentication mechanism relies on client certificates
     * for verifying the identity of the client.
     * @since 4.0.0
     */
    ClientCert,
    /**
     * Represents the DIGEST authentication type used in the `AuthType` enum.
     * This authentication mechanism is typically used for HTTP Digest Access Authentication
     * to enhance security compared to basic authentication.
     * @since 4.0.0
     */
    Digest;

    companion object {
        /**
         * Searches for an entry in the `entries` collection by comparing the provided `type`
         * string (with "_AUTH" removed) to the name of the entries, ignoring case sensitivity.
         *
         * @param type The string to compare against the names of entries after removing "_AUTH" and ignoring case.
         * @return The matching entry from the `entries` collection, or null if no match is found.
         * @since 3.12.0
         */
        infix fun of(type: String) = entries.find { it.name equalsIgnoreCase (type - "_AUTH" - "Auth") }

        /**
         * Fetches an enumeration value of type `AuthType` from the current table based on the provided name.
         * The method supports two lookup mechanisms: by name or by ordinal.
         *
         * @param name The name of the enumeration value to look up. It should match the name of an `AuthType` constant.
         * @param byName Whether to perform the lookup by name. If true, the method attempts to retrieve the enumeration
         *               value using a case-sensitive name match. If false, the enumeration is retrieved using an ordinal index.
         *               Defaults to true.
         * @since 5.5.0
         */
        fun Table.authType(name: String, byName: Boolean = true) =
            if (byName) enumerationByName<AuthType>(name, 10) else enumeration(name)
    }
}