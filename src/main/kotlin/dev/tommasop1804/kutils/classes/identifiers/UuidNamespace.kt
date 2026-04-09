/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.identifiers

import dev.tommasop1804.kutils.Uuid
import java.util.*

/**
 * Enum class representing predefined UUID namespaces as defined in RFC 4122.
 * These namespaces can be used for creating name-based UUIDs (version 3 and version 5).
 *
 * Each namespace is associated with a specific UUID value.
 *
 * @property predefinedNamespace The UUID value corresponding to the namespace.
 * @since 3.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "kutils_uuid_fromstring_as_uuid")
enum class UuidNamespace(val predefinedNamespace: Uuid) {
    /**
     * Represents the Domain Name System (DNS) namespace predefined in the UUID specification.
     *
     * This namespace is used for generating UUIDs based on domain names.
     *
     * @since 3.0.0
     */
    DNS(UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8")),
    /**
     * Represents the UUID namespace for URLs, as defined in RFC 4122.
     *
     * This namespace is used to create name-based UUIDs derived from URL strings.
     *
     * @since 3.0.0
     */
    URL(UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8")),
    /**
     * Represents the UUID namespace associated with Object Identifiers (OID).
     * This predefined namespace UUID is used for generating names based on the OID format.
     *
     * @see UuidNamespace for other predefined namespaces.
     * @since 3.0.0
     */
    OID(UUID.fromString("6ba7b812-9dad-11d1-80b4-00c04fd430c8")),
    /**
     * Represents the X.500 namespace as defined for UUID-based generation.
     * This namespace is identified by the predefined UUID "6ba7b814-9dad-11d1-80b4-00c04fd430c8".
     * It can be used to create UUIDs that are scoped or related to the X.500 directory standard.
     *
     * @since 3.0.0
     */
    X500(UUID.fromString("6ba7b814-9dad-11d1-80b4-00c04fd430c8"))
}