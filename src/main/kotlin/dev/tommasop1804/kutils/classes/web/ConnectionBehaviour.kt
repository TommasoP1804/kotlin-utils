/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.web

import dev.tommasop1804.kutils.*

/**
 * Represents the behavior of a connection, specifically whether it should remain open
 * or be closed after the current operation.
 * @since 6.1.1
 * @author Tommaso Pastorelli
 */
enum class ConnectionBehaviour(val value: String) {
    /**
     * Represents the `KeepAlive` behavior within a `ConnectionBehaviour` enumeration.
     * This behavior indicates that a connection should remain open and reusable
     * for multiple requests/responses rather than closing after a single transaction.
     * @since 6.1.1
     */
    KeepAlive("keep-alive"),
    /**
     * Represents the `Close` behavior within the `ConnectionBehaviour` enum.
     * This behavior indicates that the connection should be closed after the current request.
     * @since 6.1.1
     */
    Close("close");

    companion object {
        /**
         * Converts the given string value into a corresponding connection directive object.
         *
         * The method checks if the provided string matches known connection directive values
         * such as "keep-alive" or "close" (case-insensitive), and returns the appropriate object.
         *
         * @param value The string representation of the connection directive.
         * @return The corresponding connection directive object, or null if the value is unrecognized.
         * @since 6.1.1
         */
        infix fun of(value: String) = when {
            value equalsIgnoreCase "keep-alive" -> KeepAlive
            value equalsIgnoreCase "close" -> Close
            else -> null
        }
    }
}