package dev.tommasop1804.kutils.classes.identifiers

/**
 * Enum representing different versions of UUIDs as specified by the UUID standard.
 *
 * Each enum constant corresponds to a specific version of UUID, with a descriptive string
 * representation of the version type.
 *
 * @since 3.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
enum class UuidVersion(val description: String) {
    /**
     * Represents a UUID of version 1, commonly referred to as "time-based UUID."
     *
     * UUIDv1 generates a unique identifier using the current timestamp and other data
     * such as the node identifier, providing temporal and spatial uniqueness.
     *
     * @since 3.0.0
     */
    V1("This version generates a UUID using a 60-bit timestamp (with 100-nanosecond precision) and the 48-bit MAC address of the generating computer. A clock sequence is used to handle cases where the system clock is adjusted."),
    /**
     * Represents the DCE Security version of a UUID, corresponding to version 2 of the UUID standard.
     *
     * This version is used for DCE (Distributed Computing Environment) security-specific purposes, incorporating
     * local domain information and unique identifiers.
     *
     * @since 3.0.0
     */
    V2("Version 2 is a variant of v1, specified by the Distributed Computing Environment (DCE). It is similar to v1 but replaces some of the timestamp with a user or domain identifier."),
    /**
     * Represents UUID version 3, which is a name-based UUID generated using MD5 hashing.
     *
     * UUIDv3 is derived from a namespace identifier and a name, which are hashed together
     * using the MD5 algorithm to produce a deterministic and reproducible UUID.
     *
     * @since 3.0.0
     */
    V3("This version generate a UUID from a \"namespace\" (which is another UUID) and a \"name\" (a string). The resulting UUID is a hash of these two inputs. Hashing algoritm: MD5"),
    /**
     * Represents version 4 UUIDs, which are based on random data as per the UUID standard.
     *
     * Random-based UUIDs use random or pseudo-random number generation to create a unique identifier.
     *
     * @since 3.0.0
     */
    V4("This is the most common and widely used version. A UUID v4 is generated using purely random or pseudo-random numbers."),
    /**
     * Represents the name-based UUID version that uses SHA-1 hashing to generate the UUID.
     *
     * This corresponds to Version 5 of the UUID standard, where the UUID is deterministically
     * derived from a namespace identifier and a name using the SHA-1 hashing algorithm.
     *
     * @since 3.0.0
     */
    V5("This version generate a UUID from a \"namespace\" (which is another UUID) and a \"name\" (a string). The resulting UUID is a hash of these two inputs. Hashing algoritm: SHA-1"),
    /**
     * Represents a time-based version of UUID, known as UUIDv6. It is designed to generate UUIDs using
     * a 60-bit timestamp with 100-nanosecond precision and the 48-bit MAC address of the generating device.
     * A clock sequence is incorporated to resolve potential conflicts caused by adjustments to the system clock.
     *
     * @since 3.0.0
     */
    V6("This version generates a UUID using a 60-bit timestamp (with 100-nanosecond precision) and the 48-bit MAC address of the generating computer. A clock sequence is used to handle cases where the system clock is adjusted."),
    /**
     * Represents the UUID version 7 that generates a UUID using a random and time-based approach.
     *
     * UUIDv7 combines a 64-bit timestamp with 100-nanosecond precision and a 48-bit node identifier.
     * It incorporates a random number to mitigate cases where the node identifier is unavailable.
     *
     * @since 3.0.0
     */
    V7("This version generates a UUID using a 64-bit timestamp (with 100-nanosecond precision) and 48-bit node identifier. A random number is used to handle cases where the node identifier is not available."),
    /**
     * Represents a custom implementation of UUID (Universally Unique Identifier) version 8.
     * This class contains a description associated with this specific version of UUID.
     *
     * @since 3.0.0
     */
    V8("This is a custom version of UUID");

    companion object {
        /**
         * Finds a UuidVersion instance associated with the given version number.
         *
         * @param version The numeric representation of the UUID version to search for.
         * @return The corresponding UuidVersion if a match is found, or null otherwise.
         * @since 3.0.0
         */
        fun fromVersion(version: Number) = entries.find { it.name.last().toString() == version.toLong().toString() }
    }
}