@file:JvmName("UUIDUtilsKt")
@file:Suppress("unused", "functionName", "kutils_uuid_fromstring_as_uuid", "kutils_randomuuid_as_uuid", "kutils_collection_declaration", "kutils_temporal_now_as_temporal")
@file:Since("1.0.0")
@file:OptIn(ExperimentalUuidApi::class)

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.identifiers.ShortUUID
import dev.tommasop1804.kutils.classes.identifiers.ULID
import dev.tommasop1804.kutils.classes.tuple.map
import dev.tommasop1804.kutils.exceptions.RequiredParameterException
import java.net.NetworkInterface
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid


/**
 * Enum representing different versions of UUIDs as specified by the UUID standard.
 *
 * Each enum constant corresponds to a specific version of UUID, with a descriptive string
 * representation of the version type.
 *
 * @since 1.0.0
 */
enum class UUIDVersion(val description: String) {
    /**
     * Represents a UUID of version 1, commonly referred to as "time-based UUID."
     *
     * UUIDv1 generates a unique identifier using the current timestamp and other data
     * such as the node identifier, providing temporal and spatial uniqueness.
     *
     * @since 1.0.0
     */
    UUIDv1("This version generates a UUID using a 60-bit timestamp (with 100-nanosecond precision) and the 48-bit MAC address of the generating computer. A clock sequence is used to handle cases where the system clock is adjusted."),
    /**
     * Represents the DCE Security version of a UUID, corresponding to version 2 of the UUID standard.
     *
     * This version is used for DCE (Distributed Computing Environment) security-specific purposes, incorporating
     * local domain information and unique identifiers.
     *
     * @since 1.0.0
     */
    UUIDv2("Version 2 is a variant of v1, specified by the Distributed Computing Environment (DCE). It is similar to v1 but replaces some of the timestamp with a user or domain identifier."),
    /**
     * Represents UUID version 3, which is a name-based UUID generated using MD5 hashing.
     *
     * UUIDv3 is derived from a namespace identifier and a name, which are hashed together
     * using the MD5 algorithm to produce a deterministic and reproducible UUID.
     *
     * @since 1.0.0
     */
    UUIDv3("This version generate a UUID from a \"namespace\" (which is another UUID) and a \"name\" (a string). The resulting UUID is a hash of these two inputs. Hashing algoritm: MD5"),
    /**
     * Represents version 4 UUIDs, which are based on random data as per the UUID standard.
     *
     * Random-based UUIDs use random or pseudo-random number generation to create a unique identifier.
     *
     * @since 1.0.0
     */
    UUIDv4("This is the most common and widely used version. A UUID v4 is generated using purely random or pseudo-random numbers."),
    /**
     * Represents the name-based UUID version that uses SHA-1 hashing to generate the UUID.
     *
     * This corresponds to Version 5 of the UUID standard, where the UUID is deterministically
     * derived from a namespace identifier and a name using the SHA-1 hashing algorithm.
     *
     * @since 1.0.0
     */
    UUIDv5("This version generate a UUID from a \"namespace\" (which is another UUID) and a \"name\" (a string). The resulting UUID is a hash of these two inputs. Hashing algoritm: SHA-1"),
    /**
     * Represents a time-based version of UUID, known as UUIDv6. It is designed to generate UUIDs using
     * a 60-bit timestamp with 100-nanosecond precision and the 48-bit MAC address of the generating device.
     * A clock sequence is incorporated to resolve potential conflicts caused by adjustments to the system clock.
     *
     * @property description A detailed description of how this UUID version operates.
     * @since 1.0.0
     */
    UUIDv6("This version generates a UUID using a 60-bit timestamp (with 100-nanosecond precision) and the 48-bit MAC address of the generating computer. A clock sequence is used to handle cases where the system clock is adjusted."),
    /**
     * Represents the UUID version 7 that generates a UUID using a random and time-based approach.
     *
     * UUIDv7 combines a 64-bit timestamp with 100-nanosecond precision and a 48-bit node identifier.
     * It incorporates a random number to mitigate cases where the node identifier is unavailable.
     *
     * @property description A detailed description of the UUIDv7 generation algorithm.
     * @since 1.0.0
     */
    UUIDv7("This version generates a UUID using a 64-bit timestamp (with 100-nanosecond precision) and 48-bit node identifier. A random number is used to handle cases where the node identifier is not available."),
    /**
     * Represents a custom implementation of UUID (Universally Unique Identifier) version 8.
     * This class contains a description associated with this specific version of UUID.
     *
     * @property description A textual representation or explanation for this UUID version.
     *
     * @since 1.0.0
     */
    UUIDv8("This is a custom version of UUID");

    companion object {
        /**
         * Finds a UUIDVersion instance associated with the given version number.
         *
         * @param version The numeric representation of the UUID version to search for.
         * @return The corresponding UUIDVersion if a match is found, or null otherwise.
         * @since 1.0.0
         */
        fun fromVersion(version: Number) = entries.find { it.name.last().toString() == version.toLong().toString() }
    }
}

/**
 * Represents the different types of UUIDv8 generation types.
 * Each type defines a distinct way to construct and format UUIDs based on string, timestamp,
 * and random components.
 *
 * @since 1.0.0
 */
enum class UUIDv8Type(subvariant: Int) {
    /**
     * Represents a UUIDv8 type that encodes data using a single string format.
     * Use this type when the value should be stored or represented as a single string.
     *
     * @since 1.0.0
     */
    STRING(0),
    /**
     * Represents a UUIDv8 type that combines a timestamp with a string value.
     *
     * This type is used to generate UUIDs that embed a timestamp alongside
     * a unique string to ensure temporal and unique identification within the system.
     *
     * @since 1.0.0
     */
    TS_STRING(1),
    /**
     * Represents a UUIDv8 type that combines a string and random elements.
     *
     * This type is designed to generate unique identifiers consisting of a string
     * component concatenated with a random sequence of characters, ensuring a high degree
     * of randomness and uniqueness.
     *
     * Suitable for use cases where a hybrid of predefined string data and randomness
     * is needed for identifier generation.
     *
     * @since 1.0.0
     */
    STRING_RANDOM(2),
    /**
     * Represents a UUID type indicating a pair of string components as its format.
     * This type is used to define a UUIDv8 configuration where two string values
     * are combined to generate a UUID.
     *
     * @since 1.0.0
     */
    STRING_STRING(0),
    /**
     * Represents a UUID version 8 type where the structure consists of a timestamp and two string components.
     * This type is typically used when a hybrid identifier combining both time-based data
     * and user-defined or context-specific string data is required.
     *
     * @since 1.0.0
     */
    TS_STRING_STRING(1),
    /**
     * Represents a UUIDv8 type that combines two string components with a random element.
     * This type is used to generate a UUID that consists of two string fields separated
     * by a random identifier segment.
     *
     * This enum value is part of the `UUIDv8Type` classification.
     *
     * @since 1.0.0
     */
    STRING_STRING_RANDOM(2)
}

/**
 * Enum class representing predefined UUID namespaces as defined in RFC 4122.
 * These namespaces can be used for creating name-based UUIDs (version 3 and version 5).
 *
 * Each namespace is associated with a specific UUID value.
 *
 * @property predefinedNamespace The UUID value corresponding to the namespace.
 * @since 1.0.0
 */
enum class UUIDNamespace(val predefinedNamespace: UUID) {
    /**
     * Represents the Domain Name System (DNS) namespace predefined in the UUID specification.
     *
     * This namespace is used for generating UUIDs based on domain names.
     *
     * @param predefinedNamespace The predefined UUID associated with the DNS namespace:
     * UUID("6ba7b810-9dad-11d1-80b4-00c04fd430c8").
     *
     * @since 1.0.0
     */
    DNS(UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8")),
    /**
     * Represents the UUID namespace for URLs, as defined in RFC 4122.
     *
     * This namespace is used to create name-based UUIDs derived from URL strings.
     *
     * @property predefinedNamespace The UUID value associated with the URL namespace.
     * @since 1.0.0
     */
    URL(UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8")),
    /**
     * Represents the UUID namespace associated with Object Identifiers (OID).
     * This predefined namespace UUID is used for generating names based on the OID format.
     *
     * @property predefinedNamespace The UUID value corresponding to the OID namespace.
     * @see UUIDNamespace for other predefined namespaces.
     * @since 1.0.0
     */
    OID(UUID.fromString("6ba7b812-9dad-11d1-80b4-00c04fd430c8")),
    /**
     * Represents the X.500 namespace as defined for UUID-based generation.
     * This namespace is identified by the predefined UUID "6ba7b814-9dad-11d1-80b4-00c04fd430c8".
     * It can be used to create UUIDs that are scoped or related to the X.500 directory standard.
     *
     * @since 1.0.0
     */
    X500(UUID.fromString("6ba7b814-9dad-11d1-80b4-00c04fd430c8"))
}

/**
 * Represents a UUID value where all bits are set to zero.
 * Often used to signify a "null" or "uninitialized" UUID.
 *
 * @since 1.0.0
 */
val NIL_UUID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
/**
 * A constant representing the maximum possible UUID value.
 *
 * The value is equivalent to "ffffffff-ffff-ffff-ffff-ffffffffffff", denoting a UUID
 * with all bits set to their maximum value.
 *
 * This can be used in cases where the maximum UUID value is required for comparison,
 * testing, or as an upper bound in algorithms working with UUIDs.
 *
 * @since 1.0.0
 */
val MAX_UUID: UUID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")

/**
 * Extension property for `UUID` that converts the timestamp of the UUID to an `Instant` object.
 *
 * The timestamp of the UUID is interpreted in milliseconds since the epoch
 * (1970-01-01T00:00:00Z) and converted into an `Instant`.
 *
 *
 * @receiver The `UUID` object whose timestamp will be converted.
 * @return An `Instant` corresponding to the UUID timestamp.
 * @since 1.0.0
 */
val UUID.instant: Instant
    get() = if (version() == 1) Instant(timestamp()) else ULID(this).instant
/**
 * Extension property for [Uuid] that converts the timestamp of the UUID to an `Instant` object.
 *
 * The timestamp of the UUID is interpreted in milliseconds since the epoch
 * (1970-01-01T00:00:00Z) and converted into an `Instant`.
 *
 *
 * @receiver The [Uuid] object whose timestamp will be converted.
 * @return An `Instant` corresponding to the UUID timestamp.
 * @since 1.0.0
 */
val Uuid.instant: Instant
    get() = if (toJavaUuid().version() == 1) Instant(toJavaUuid().timestamp()) else ULID(toJavaUuid()).instant

/**
 * An extension property for the `UUID` class that retrieves the corresponding version of the UUID
 * as an instance of the `UUIDVersion` enum.
 *
 * This property determines the version of a `UUID` by invoking its internal `version()` function
 * and maps it to the appropriate `UUIDVersion` constant using the `UUIDVersion.fromVersion` method.
 *
 * @receiver The `UUID` instance for which the version is being determined.
 * @return The specific version of the `UUID` as a `UUIDVersion` enum value.
 * @since 1.0.0
 */
val UUID.version: UUIDVersion
    get() = UUIDVersion.fromVersion(version())!!
/**
 * An extension property for the [Uuid] class that retrieves the corresponding version of the UUID
 * as an instance of the `UUIDVersion` enum.
 *
 * This property determines the version of a `UUID` by invoking its internal `version()` function
 * and maps it to the appropriate `UUIDVersion` constant using the `UUIDVersion.fromVersion` method.
 *
 * @receiver The `UUID` instance for which the version is being determined.
 * @return The specific version of the `UUID` as a `UUIDVersion` enum value.
 * @since 1.0.0
 */
val Uuid.version: UUIDVersion
    get() = UUIDVersion.fromVersion(toJavaUuid().version())!!

/**
 * Retrieves the variant of the UUID.
 * The variant denotes the layout of the UUID.
 *
 * This property provides a convenient way to access the variant information
 * directly from the UUID instance.
 *
 * @return The variant value as an integer.
 * @since 1.0.0
 */
val UUID.variant: Int
    get() = variant()
/**
 * Retrieves the variant of the UUID. The variant determines the layout of the UUID and
 * indicates the internal format, as specified in the UUID standard.
 *
 * @receiver The UUID instance from which the variant is to be extracted.
 * @return The variant value of the UUID, defined as an `Int`.
 * @since 1.0.0
 */
val Uuid.variant: Int
    get() = toJavaUuid().variant()

/**
 * Retrieves the subvariant value of the UUID based on its 8th byte.
 *
 * The subvariant is calculated by decoding specific bits (0x30 mask applied,
 * then shifted right by 4) of the UUID's byte representation.
 *
 * @receiver The UUID instance from which the subvariant is derived.
 * @return The subvariant of the UUID as an integer.
 * @since 1.0.0
 */
val UUID.subvariant: Int
    get() {
        val bb = ByteBuffer.allocate(16)
        bb.putLong(mostSignificantBits)
        bb.putLong(leastSignificantBits)
        val bytes = bb.array()

        return (bytes[8].toInt() and 0x30) shr 4
    }
/**
 * Retrieves the subvariant of the UUID.
 *
 * The subvariant is determined based on the underlying UUID structure.
 * This property provides access to the corresponding subvariant value
 * as defined in the UUID specification.
 *
 * @receiver The UUID instance whose subvariant is to be retrieved.
 * @return The subvariant of the UUID as an integer.
 * @since 1.0.0
 */
val Uuid.subvariant: Int
    get() = toJavaUuid().subvariant

/**
 * Extension property for `UUID` that returns the string representation of the UUID with all hyphens removed.
 *
 * This can be useful when a compact representation of the UUID is required.
 *
 * @since 1.0.0
 */
val UUID.withoutHyphens: String
    get() = toString() - '-'
/**
 * Extension property for [Uuid] that returns the string representation of the UUID with all hyphens removed.
 *
 * This can be useful when a compact representation of the UUID is required.
 *
 * @since 1.0.0
 */
val Uuid.withoutHyphens: String
    get() = toString() - '-'

/**
 * Generates a UUID based on the specified version and optional namespace and name.
 *
 * This function supports multiple UUID versions, each leveraging a specific methodology
 * for UUID generation as dictated by the UUID standard. The version determines the algorithm
 * used to generate the UUID, whether it's time-based, random-based, or hash-based.
 *
 * - WARNING: UUIDv1 can expose MAC address.
 * - WARNING: UUIDv6 can expose node identifier.
 *
 * @param version The version of the UUID to generate. Supported versions include UUIDv1, UUIDv3, UUIDv4, UUIDv5, UUIDv6, UUIDv7 and custom UUIDv8.
 *                UUIDv2 is not supported and will throw an UnsupportedOperationException.
 * @param namespaceAndName An optional pair containing a namespace UUID and a name string. This parameter is mandatory for
 *                         name-based UUIDs (UUIDv3 and UUIDv5), and a RequiredParameterException will be thrown if it is null.
 *                         It is ignored for other versions.
 * @param timestamp The timestamp of the UUID. Supported version include UUIDv1, UUIDv6, UUIDv7, pontentially UUIDv8.
 * @param v8Type An optional parameter for UUIDv8. This parameter is ignored for other versions.
 * @param v8Strings An optional parameter for UUIDv8. This parameter is ignored for other versions.
 * @return A UUID instance corresponding to the specified version and provided inputs.
 * @since 1.0.0
 */
fun UUID(
    version: UUIDVersion = UUIDVersion.UUIDv4,
    namespaceAndName: Pair<UUID, String>? = null,
    timestamp: Instant? = null,
    v8Type: UUIDv8Type? = null,
    v8Strings: Any2? = null
): UUID = when(version) {
    UUIDVersion.UUIDv1 -> UUIDGenerator.v1(timestamp?.toEpochMilli())
    UUIDVersion.UUIDv2 -> throw UnsupportedOperationException("DCE_SECURITY generation is not supported .")
    UUIDVersion.UUIDv3 -> UUIDGenerator.v3(
        (namespaceAndName ?: throw RequiredParameterException("UUID", "namespaceAndName", NullPointerException("namespaceAndName"))).first,
        namespaceAndName.second
    )
    UUIDVersion.UUIDv4 -> UUID.randomUUID()
    UUIDVersion.UUIDv5 -> UUIDGenerator.v5(
        (namespaceAndName ?: throw RequiredParameterException("UUID", "namespaceAndName", NullPointerException("namespaceAndName"))).first,
        namespaceAndName.second
    )
    UUIDVersion.UUIDv6 -> UUIDGenerator.v6(timestamp?.toEpochMilli())
    UUIDVersion.UUIDv7 -> UUIDGenerator.v7(timestamp?.toEpochMilli())
    UUIDVersion.UUIDv8 -> UUIDGenerator.v8(v8Type ?: throw RequiredParameterException("UUID", "v8Type"), timestamp?.toEpochMilli(), v8Strings ?: throw RequiredParameterException("UUID", "v8Strings"))
}
/**
 * Generates a UUID based on the specified version and optional namespace and name.
 *
 * This function supports multiple UUID versions, each leveraging a specific methodology
 * for UUID generation as dictated by the UUID standard. The version determines the algorithm
 * used to generate the UUID, whether it's time-based, random-based, or hash-based.
 *
 * - WARNING: UUIDv1 can expose MAC address.
 * - WARNING: UUIDv6 can expose node identifier.
 *
 * @param version The version of the UUID to generate. Supported versions include UUIDv1, UUIDv3, UUIDv4, UUIDv5, UUIDv6, UUIDv7 and custom UUIDv8.
 *                UUIDv2 is not supported and will throw an UnsupportedOperationException.
 * @param namespaceAndName An optional pair containing a namespace UUID and a name string. This parameter is mandatory for
 *                         name-based UUIDs (UUIDv3 and UUIDv5), and a RequiredParameterException will be thrown if it is null.
 *                         It is ignored for other versions.
 * @param timestamp The timestamp of the UUID. Supported version include UUIDv1, UUIDv6, UUIDv7, pontentially UUIDv8.
 * @param v8Type An optional parameter for UUIDv8. This parameter is ignored for other versions.
 * @param v8Strings An optional parameter for UUIDv8. This parameter is ignored for other versions.
 * @return A UUID instance corresponding to the specified version and provided inputs.
 * @since 1.0.0
 */
fun Uuid(
    version: UUIDVersion = UUIDVersion.UUIDv4,
    namespaceAndName: Pair<Uuid, String>? = null,
    timestamp: Instant? = null,
    v8Type: UUIDv8Type? = null,
    v8Strings: Any2? = null
): Uuid = when(version) {
    UUIDVersion.UUIDv1 -> UUIDGenerator.v1(timestamp?.toEpochMilli())
    UUIDVersion.UUIDv2 -> throw UnsupportedOperationException("DCE_SECURITY generation is not supported .")
    UUIDVersion.UUIDv3 -> UUIDGenerator.v3(
        (namespaceAndName ?: throw RequiredParameterException("UUID", "namespaceAndName")).first,
        namespaceAndName.second
    )
    UUIDVersion.UUIDv4 -> UUID.randomUUID()
    UUIDVersion.UUIDv5 -> UUIDGenerator.v5(
        (namespaceAndName ?: throw RequiredParameterException("UUID", "namespaceAndName")).first,
        namespaceAndName.second
    )
    UUIDVersion.UUIDv6 -> UUIDGenerator.v6(timestamp?.toEpochMilli())
    UUIDVersion.UUIDv7 -> UUIDGenerator.v7(timestamp?.toEpochMilli())
    UUIDVersion.UUIDv8 -> UUIDGenerator.v8(v8Type ?: throw RequiredParameterException("UUID", "v8Type"), timestamp?.toEpochMilli(), v8Strings ?: throw RequiredParameterException("UUID", "v8Strings"))
}.toKotlinUuid()
/**
 * Generates a UUID based on the specified version and optional namespace and name.
 *
 * This function supports multiple UUID versions, each leveraging a specific methodology
 * for UUID generation as dictated by the UUID standard. The version determines the algorithm
 * used to generate the UUID, whether it's time-based, random-based, or hash-based.
 *
 * - WARNING: UUIDv1 can expose MAC address.
 * - WARNING: UUIDv6 can expose node identifier.
 *
 * @param version The version of the UUID to generate. Supported versions include UUIDv1, UUIDv3, UUIDv4, UUIDv5, UUIDv6, UUIDv7 and custom UUIDv8.
 *                UUIDv2 is not supported and will throw an UnsupportedOperationException.
 * @param timestamp The timestamp of the UUID. Supported version include UUIDv1, UUIDv6, UUIDv7, pontentially UUIDv8.
 * @param v8Type An optional parameter for UUIDv8. This parameter is ignored for other versions.
 * @param v8String An optional parameter for UUIDv8. This parameter is ignored for other versions.
 * @return A UUID instance corresponding to the specified version and provided inputs.
 * @since 1.0.0
 */
fun UUID(
    version: UUIDVersion,
    timestamp: Instant? = null,
    v8Type: UUIDv8Type? = null,
    v8String: String
) = UUID(version, timestamp = timestamp, v8Type = v8Type, v8Strings = v8String to String.EMPTY)
/**
 * Generates a UUID based on the specified version and optional namespace and name.
 *
 * This function supports multiple UUID versions, each leveraging a specific methodology
 * for UUID generation as dictated by the UUID standard. The version determines the algorithm
 * used to generate the UUID, whether it's time-based, random-based, or hash-based.
 *
 * - WARNING: UUIDv1 can expose MAC address.
 * - WARNING: UUIDv6 can expose node identifier.
 *
 * @param version The version of the UUID to generate. Supported versions include UUIDv1, UUIDv3, UUIDv4, UUIDv5, UUIDv6, UUIDv7 and custom UUIDv8.
 *                UUIDv2 is not supported and will throw an UnsupportedOperationException.
 * @param timestamp The timestamp of the UUID. Supported version include UUIDv1, UUIDv6, UUIDv7, pontentially UUIDv8.
 * @param v8Type An optional parameter for UUIDv8. This parameter is ignored for other versions.
 * @param v8String An optional parameter for UUIDv8. This parameter is ignored for other versions.
 * @return A UUID instance corresponding to the specified version and provided inputs.
 * @since 1.0.0
 */

fun Uuid(
    version: UUIDVersion,
    timestamp: Instant? = null,
    v8Type: UUIDv8Type? = null,
    v8String: String
) = Uuid(version, timestamp = timestamp, v8Type = v8Type, v8Strings = v8String to String.EMPTY)
/**
 * Generates a UUID based on the specified version and a namespace-name pair.
 * This method supports the creation of UUIDs compliant with the UUID standard,
 * leveraging different generation methods determined by the provided version.
 *
 * - WARNING: UUIDv1 can expose MAC address.
 * - WARNING: UUIDv6 can expose node identifier.
 *
 * @param version The version of the UUID to be generated. This determines the algorithm and specifications
 *                for UUID creation, as defined in the `UUIDVersion` enum.
 * @param namespaceAndName A pair consisting of a `UUIDNamespace` and a `String`. The namespace is a predefined
 *                         UUID representing the context (e.g., DNS, URL, OID, X500), and the name is a
 *                         specific identifier within that namespace. These are used in name-based UUID generation.
 * @since 1.0.0
 */
@JvmName("UUIDUUIDVersionUUIDNamespaceString")
fun UUID(version: UUIDVersion, namespaceAndName: Pair<UUIDNamespace, String>) = UUID(version, namespaceAndName.map(f1 = UUIDNamespace::predefinedNamespace))
/**
 * Generates a UUID based on the specified version and a namespace-name pair.
 * This method supports the creation of UUIDs compliant with the UUID standard,
 * leveraging different generation methods determined by the provided version.
 *
 * - WARNING: UUIDv1 can expose MAC address.
 * - WARNING: UUIDv6 can expose node identifier.
 *
 * @param version The version of the UUID to be generated. This determines the algorithm and specifications
 *                for UUID creation, as defined in the `UUIDVersion` enum.
 * @param namespaceAndName A pair consisting of a `UUIDNamespace` and a `String`. The namespace is a predefined
 *                         UUID representing the context (e.g., DNS, URL, OID, X500), and the name is a
 *                         specific identifier within that namespace. These are used in name-based UUID generation.
 * @since 1.0.0
 */
@JvmName("UuidUUIDVersionUUIDNamespaceString")
fun Uuid(version: UUIDVersion, namespaceAndName: Pair<UUIDNamespace, String>) = UUID(version, namespaceAndName.map(f1 = UUIDNamespace::predefinedNamespace)).toKotlinUuid()
/**
 * Generates a list of distinct UUIDs based on the specified parameters.
 *
 * This function creates multiple UUIDs using the specified version, and optionally
 * a namespace and name for name-based UUIDs (UUIDv3 and UUIDv5). It ensures that
 * the resulting list contains distinct UUIDs and appends additional UUIDs if duplicates
 * are found during the generation process.
 *
 * @param number The number of UUIDs to generate.
 * @param version The version of the UUIDs to generate. Supported versions include UUIDv1, UUIDv3, UUIDv4, UUIDv5, UUIDv6, and UUIDv7.
 *                UUIDv2 is not supported and will throw an UnsupportedOperationException.
 * @param namespaceAndName An optional pair containing a namespace UUID and a name string.
 *                         This parameter is mandatory for name-based UUIDs (UUIDv3 and UUIDv5),
 *                         and a RequiredParameterException will be thrown if it is null.
 *                         It is ignored for other versions.
 * @return A list of UUIDs of the specified version, ensuring all UUIDs in the list are distinct.
 * @since 1.0.0
 */
fun UUID(number: Number, version: UUIDVersion, namespaceAndName: Pair<UUID, String>? = null, timestamp: Instant? = null): UUIDList {
    val result = mutableListOf<UUID>()
    for (i in 1..number.toLong())
        result += UUID(version, namespaceAndName, timestamp)
    if (result.size != result.distinct().size)
        return result.distinct() + UUID(result.size - result.distinct().size, version, namespaceAndName, timestamp)
    return result.toList()
}
/**
 * Generates a list of distinct UUIDs based on the specified parameters.
 *
 * This function creates multiple UUIDs using the specified version, and optionally
 * a namespace and name for name-based UUIDs (UUIDv3 and UUIDv5). It ensures that
 * the resulting list contains distinct UUIDs and appends additional UUIDs if duplicates
 * are found during the generation process.
 *
 * @param number The number of UUIDs to generate.
 * @param version The version of the UUIDs to generate. Supported versions include UUIDv1, UUIDv3, UUIDv4, UUIDv5, UUIDv6, and UUIDv7.
 *                UUIDv2 is not supported and will throw an UnsupportedOperationException.
 * @param namespaceAndName An optional pair containing a namespace UUID and a name string.
 *                         This parameter is mandatory for name-based UUIDs (UUIDv3 and UUIDv5),
 *                         and a RequiredParameterException will be thrown if it is null.
 *                         It is ignored for other versions.
 * @return A list of UUIDs of the specified version, ensuring all UUIDs in the list are distinct.
 * @since 1.0.0
 */
fun Uuid(number: Number, version: UUIDVersion, namespaceAndName: Pair<Uuid, String>? = null, timestamp: Instant? = null): List<Uuid> {
    val result = mutableListOf<Uuid>()
    for (i in 1..number.toLong())
        result += Uuid(version, namespaceAndName, timestamp)
    if (result.size != result.distinct().size)
        return result.distinct() + Uuid(result.size - result.distinct().size, version, namespaceAndName, timestamp)
    return result.toList()
}
/**
 * Generates a list of unique UUIDs based on the specified version, namespace, and name, repeating the generation
 * process as many times as specified by the number parameter. In the event that duplicate UUIDs are produced,
 * adjustments will be made to ensure uniqueness within the resulting list.
 *
 * @param number The number of UUIDs to generate. Defines how many UUIDs will be created and included in the list.
 * @param version The version of UUID generation to use. Determines the method for creating UUIDs,
 *                as specified in the `UUIDVersion` enum.
 * @param namespaceAndName A pair consisting of a `UUIDNamespace` and a `String`. The namespace defines the context
 *                         (e.g., DNS, URL, etc.) and the name is the specific identifier within that namespace.
 *                         Used in name-based UUID generation.
 * @return A list of `UUID` objects, each generated according to the specified parameters.
 *         If duplicates are detected, the method ensures that all UUIDs in the list are distinct.
 * @since 1.0.0
 */
@JvmName("UUIDUUIDNumberVersionUUIDNamespaceString")
fun UUID(number: Number, version: UUIDVersion, namespaceAndName: Pair<UUIDNamespace, String>): UUIDList {
    val result = mutableListOf<UUID>()
    for (i in 1..number.toLong())
        result += UUID(version, namespaceAndName)
    if (result.size != result.distinct().size)
        return result.distinct() + UUID(result.size - result.distinct().size, version, namespaceAndName)
    return result.toList()
}
/**
 * Generates a list of unique UUIDs based on the specified version, namespace, and name, repeating the generation
 * process as many times as specified by the number parameter. In the event that duplicate UUIDs are produced,
 * adjustments will be made to ensure uniqueness within the resulting list.
 *
 * @param number The number of UUIDs to generate. Defines how many UUIDs will be created and included in the list.
 * @param version The version of UUID generation to use. Determines the method for creating UUIDs,
 *                as specified in the `UUIDVersion` enum.
 * @param namespaceAndName A pair consisting of a `UUIDNamespace` and a `String`. The namespace defines the context
 *                         (e.g., DNS, URL, etc.) and the name is the specific identifier within that namespace.
 *                         Used in name-based UUID generation.
 * @return A list of `UUID` objects, each generated according to the specified parameters.
 *         If duplicates are detected, the method ensures that all UUIDs in the list are distinct.
 * @since 1.0.0
 */
@JvmName("UuidUUIDNumberVersionUUIDNamespaceString")
fun Uuid(number: Number, version: UUIDVersion, namespaceAndName: Pair<UUIDNamespace, String>): List<Uuid> {
    val result = mutableListOf<Uuid>()
    for (i in 1..number.toLong())
        result += Uuid(version, namespaceAndName)
    if (result.size != result.distinct().size)
        return result.distinct() + Uuid(result.size - result.distinct().size, version, namespaceAndName)
    return result.toList()
}
/**
 * Creates a UUID from the given string representation.
 *
 * @param string The string representation of the UUID.
 * @return A UUID instance created from the input string.
 * @throws IllegalArgumentException If the input string is not a valid UUID format.
 * @since 1.0.0
 */
fun UUID(string: String) = UUID.fromString(string)!!
/**
 * Creates a UUID from the given string representation.
 *
 * @param string The string representation of the UUID.
 * @return A UUID instance created from the input string.
 * @throws IllegalArgumentException If the input string is not a valid UUID format.
 * @since 1.0.0
 */
fun Uuid(string: String) = Uuid.parse(string)
/**
 * Converts a nullable string to a UUID. If the input string is null, this function will return null.
 * Otherwise, it attempts to parse the string into a UUID.
 *
 * @param string The nullable string that represents a UUID.
 * @return A [UUID] parsed from the input string or null if the input string is null.
 * @throws IllegalArgumentException If the input string is not null but is not a valid UUID format.
 * @since 1.0.0
 */
@JvmName("UUIDNullableString")
fun UUID(string: String?) = string?.run(UUID::fromString)
/**
 * Converts a nullable string to a UUID. If the input string is null, this function will return null.
 * Otherwise, it attempts to parse the string into a UUID.
 *
 * @param string The nullable string that represents a UUID.
 * @return A [UUID] parsed from the input string or null if the input string is null.
 * @throws IllegalArgumentException If the input string is not null but is not a valid UUID format.
 * @since 1.0.0
 */
@JvmName("UuidNullableString")
fun Uuid(string: String?) = string?.run(Uuid::parse)
/**
 * Converts a given ULID to its corresponding UUID representation.
 *
 * @param ulid The ULID instance to be converted into a UUID.
 * @return A UUID generated from the provided ULID.
 * @since 1.0.0
 */
fun UUID(ulid: ULID) = ulid.toUUID()
/**
 * Converts a given ULID to its corresponding UUID representation.
 *
 * @param ulid The ULID instance to be converted into a UUID.
 * @return A UUID generated from the provided ULID.
 * @since 1.0.0
 */
fun Uuid(ulid: ULID) = ulid.toUUID().toKotlinUuid()
/**
 * Converts a given ULID to a UUID if the ULID is not null.
 *
 * @param ulid Instance of ULID to be converted to UUID. Nullable.
 * @return A UUID representation of the provided ULID, or null if the ULID is null.
 * @since 1.0.0
 */
@JvmName("UUIDNullableULID")
fun UUID(ulid: ULID?) = ulid?.toUUID()
/**
 * Converts a given ULID to a UUID if the ULID is not null.
 *
 * @param ulid Instance of ULID to be converted to UUID. Nullable.
 * @return A UUID representation of the provided ULID, or null if the ULID is null.
 * @since 1.0.0
 */
@JvmName("UuidNullableULID")
fun Uuid(ulid: ULID?) = ulid?.toUUID()?.toKotlinUuid()
/**
 * Converts a [ShortUUID] instance into its full [UUID] representation.
 *
 * This function decodes the shortened, compact [ShortUUID] string back into
 * a standard [UUID] object, restoring the original full representation of the UUID.
 * Useful for scenarios where a compact identifier needs to be reverted to its full counterpart.
 *
 * @param shortUUID The [ShortUUID] object to be converted to a standard [UUID].
 * @return The full [UUID] representation derived from the given [ShortUUID].
 * @since 1.0.0
 */
fun UUID(shortUUID: ShortUUID) = shortUUID.toUUID()
/**
 * Converts a [ShortUUID] instance into its full [UUID] representation.
 *
 * This function decodes the shortened, compact [ShortUUID] string back into
 * a standard [UUID] object, restoring the original full representation of the UUID.
 * Useful for scenarios where a compact identifier needs to be reverted to its full counterpart.
 *
 * @param shortUUID The [ShortUUID] object to be converted to a standard [UUID].
 * @return The full [Uuid] representation derived from the given [ShortUUID].
 * @since 1.0.0
 */
fun Uuid(shortUUID: ShortUUID) = shortUUID.toUUID().toKotlinUuid()
/**
 * Converts a nullable [ShortUUID] to a full [UUID].
 *
 * If the provided [shortUUID] is not `null`, this method decodes the contained
 * shortened string into a full UUID representation using the [ShortUUID.toUUID] method.
 *
 * @param shortUUID The nullable [ShortUUID] to be converted. If `null`, the result will also be `null`.
 * @since 1.0.0
 */
@JvmName("UUIDNullableShortUUID")
fun UUID(shortUUID: ShortUUID?) = shortUUID?.toUUID()
/**
 * Converts a nullable [ShortUUID] to a full [UUID].
 *
 * If the provided [shortUUID] is not `null`, this method decodes the contained
 * shortened string into a full UUID representation using the [ShortUUID.toUUID] method.
 *
 * @param shortUUID The nullable [ShortUUID] to be converted. If `null`, the result will also be `null`.
 * @since 1.0.0
 */
@JvmName("UUIDNullableShortUUID")
fun Uuid(shortUUID: ShortUUID?) = shortUUID?.toUUID()?.toKotlinUuid()

/**
 * Converts the current [CharSequence] to a [UUID] instance. The method attempts to parse
 * the [CharSequence] as a valid UUID string.
 *
 * The conversion process is encapsulated within a [Result] using `runCatching`,
 * allowing the caller to handle parsing errors without throwing an exception.
 *
 * @return A [Result] containing the parsed [UUID] if successful, or the exception
 *         thrown during parsing if the input is not a valid UUID string.
 * @since 1.0.0
 */
fun CharSequence.toUUID() = runCatching { UUID.fromString(toString())!! }
/**
 * Converts the current [CharSequence] to a [Uuid] instance. The method attempts to parse
 * the [CharSequence] as a valid UUID string.
 *
 * The conversion process is encapsulated within a [Result] using `runCatching`,
 * allowing the caller to handle parsing errors without throwing an exception.
 *
 * @return A [Result] containing the parsed [Uuid] if successful, or the exception
 *         thrown during parsing if the input is not a valid UUID string.
 * @since 1.0.0
 */
fun CharSequence.toKotlinUuid() = runCatching { Uuid.parse(toString()) }
/**
 * Converts the current [CharSequence] to a [UUID] object.
 * If the current [CharSequence] is null or blank, the result will be null.
 * If the [CharSequence] is not null or blank, it attempts to parse it into a [UUID].
 * This method uses a safe approach with `runCatching` to handle potential exceptions.
 *
 * @receiver The [CharSequence] to convert to a [UUID].
 * @return A [UUID] object if the [CharSequence] is successfully parsed, or null if the receiver is null, blank,
 *         or if an exception occurs during parsing.
 * @since 1.0.0
 */
@JvmName("toUUIDNullable")
fun CharSequence?.toUUID() = runCatching { if (isNullOrBlank()) null else UUID.fromString(toString())!! }
/**
 * Converts the current [CharSequence] to a [Uuid] object.
 * If the current [CharSequence] is null or blank, the result will be null.
 * If the [CharSequence] is not null or blank, it attempts to parse it into a [Uuid].
 * This method uses a safe approach with `runCatching` to handle potential exceptions.
 *
 * @receiver The [CharSequence] to convert to a [Uuid].
 * @return A [Uuid] object if the [CharSequence] is successfully parsed, or null if the receiver is null, blank,
 *         or if an exception occurs during parsing.
 * @since 1.0.0
 */
@JvmName("toUuidNullable")
fun CharSequence?.toKotlinUuid() = runCatching { if (isNullOrBlank()) null else Uuid.parse(toString()) }

/**
 * Validates whether the provided string is a valid UUID.
 *
 * @param string The string to validate as a UUID.
 * @return True if the string is a valid UUID, otherwise false.
 * @since 1.0.0
 */
fun isValidUUID(string: String) = runCatching { UUID.fromString(string) }.isSuccess

/**
 * Returns the most significant bits of this UUID.
 * This operation allows the UUID to be destructured as its components.
 *
 * @receiver UUID instance from which the most significant bits will be retrieved.
 * @return The most significant 64 bits of the UUID as a `Long`.
 * @since 1.0.0
 */
operator fun UUID.component1() = mostSignificantBits
/**
 * Operator function that extracts the least significant bits component
 * from a UUID instance. This allows destructuring declarations to
 * access the least significant bits as the second component when a UUID
 * object is destructured.
 *
 * @receiver the UUID instance from which the least significant bits are extracted.
 * @return the least significant bits of the UUID as a Long value.
 * @since 1.0.0
 */
operator fun UUID.component2() = leastSignificantBits

/**
 * A utility object for generating UUIDs conforming to various versions (v1, v3, v5, v6, v7).
 * This object uses a combination of system time, random values, and hashing mechanisms to create unique identifiers.
 *
 * @since 1.0.0
 */
private object UUIDGenerator {
    /**
     * The offset in milliseconds between the Gregorian epoch (1582-10-15) and the Unix epoch (1970-01-01).
     * This constant is used to convert or calculate time values between the two epoch systems.
     *
     * @since 1.0.0
     */
    private const val GREGORIAN_EPOCH_OFFSET = 12219292800000L // Offset in millisecondi tra epoca Gregoriana e Unix
    
    /**
     * An instance of `SecureRandom` used to generate cryptographically strong random values.
     * This variable provides a secure random number generator that uses the underlying operating
     * system's default algorithm for generating randomness.
     *
     * The `SecureRandom` class is specifically designed for cryptographical purposes, ensuring
     * highly unpredictable values that are suitable for secure applications, such as generating
     * keys, nonces, or tokens.
     *
     * @since 1.0.0
     */
    private val random = SecureRandom()
    
    /**
     * Represents a unique identifier for a node, derived from the network interface's MAC address if available,
     * or generated randomly when a valid MAC address is unavailable or an error occurs.
     *
     * The identifier is constructed by extracting the last 6 bytes of the MAC address, appending a specific bit pattern
     * to ensure consistency. In cases where no eligible network interface is found, or an exception is encountered,
     * the identifier is generated as a random 48-bit number.
     *
     * This value ensures that each node can have a stable and reasonably unique identifier in distributed systems or
     * other environments where node identification is required.
     *
     * @since 1.0.0
     */
    private val node: Long = try {
        val networkInterface = NetworkInterface.getNetworkInterfaces().toList()
            .firstOrNull { it.isUp && !it.isLoopback && it.hardwareAddress.isNotNull() }
        val mac = networkInterface?.hardwareAddress
        mac?.let {
            val buffer = ByteBuffer.wrap(it)
            var result = 0L
            if (buffer.remaining() >= 6) {
                buffer.position(buffer.limit() - 6)
                result = buffer.long
            }
            result or 0x0000010000000000L
        } ?: (random.nextLong() and 0x0000FFFFFFFFFFFFL)
    } catch (e: Exception) { random.nextLong() and 0x0000FFFFFFFFFFFFL }

    /**
     * Represents the clock sequence used for UUID generation to ensure unique timestamps.
     * The clock sequence helps prevent conflicts when UUIDs are generated on systems
     * with unsynchronized clocks or duplicate node identifiers.
     *
     * Initialized with a random value in the range [0, 0x3FFF].
     *
     * @since 1.0.0
     */
    private var clockSequence: Int = random.nextInt(0x3FFF)

    /**
     * Generates a Version 1 UUID based on the current timestamp and random values for the node and clock sequence.
     *
     * Version 1 UUIDs are time-based and use the timestamp, a clock sequence, and a simulated node identifier (e.g., MAC address).
     *
     * @return A UUID of type 1 (time-based UUID).
     * @since 1.0.0
     */
    fun v1(ts: Long?): UUID {
        val timestamp = ts ?: System.currentTimeMillis()

        // UUID v1 usa timestamp da 15 ottobre 1582. Aggiustiamo grossolanamente.
        val time = (timestamp + GREGORIAN_EPOCH_OFFSET) * 10000 // in 100ns

        val timeLow = (time and 0xFFFFFFFFL).toInt()
        val timeMid = ((time shr 32) and 0xFFFF).toShort()
        val timeHiAndVersion = (((time shr 48) and 0x0FFF) or (1L shl 12)).toShort() // version 1

        // Node (MAC simulato) e clockSeq random
        val clockSeq = (random.nextInt() and 0x3FFF).toShort()
        val node = ByteArray(6)
        random.nextBytes(node)

        val bb = ByteBuffer.allocate(16)
        bb.putInt(timeLow)
        bb.putShort(timeMid)
        bb.putShort(timeHiAndVersion)
        bb.putShort(clockSeq)
        bb.put(node)

        bb.flip()

        val mostSigBits = bb.long
        val leastSigBits = bb.long

        return UUID(mostSigBits, leastSigBits)
    }

    /**
     * Generates a Version 3 UUID (name-based and using MD5 hashing) based on the provided namespace and name.
     *
     * @param namespace The namespace UUID to use as a base for the generation.
     * @param name The name to hash in combination with the namespace to generate the UUID.
     * @return The generated Version 3 UUID.
     * @since 1.0.0
     */
    fun v3(namespace: UUID, name: String): UUID {
        val md5 = MessageDigest.getInstance("MD5")

        val namespaceBytes = namespace.toBytes()
        val nameBytes = name.toByteArray(Charsets.UTF_8)

        md5.update(namespaceBytes)
        md5.update(nameBytes)

        val hash = md5.digest()

        // Imposta i bit di versione (3) e variante
        hash[6] = (hash[6].toInt() and 0x0F or 0x30).toByte()
        hash[8] = (hash[8].toInt() and 0x3F or 0x80).toByte()

        return hash.toUUID()
    }
    /**
     * Generates a Version 3 UUID (name-based and using MD5 hashing) based on the provided namespace and name.
     *
     * @param namespace The namespace UUID to use as a base for the generation.
     * @param name The name to hash in combination with the namespace to generate the UUID.
     * @return The generated Version 3 UUID.
     * @since 1.0.0
     */
    fun v3(namespace: Uuid, name: String) = v3(namespace.toJavaUuid(), name)

    /**
     * Generates a version 5 (SHA-1 hashed) UUID based on a given namespace and name.
     * This UUID generation complies with RFC 4122 standards for version 5 UUIDs.
     *
     * @param namespace The namespace UUID to use as the basis for the variant 5 UUID. This provides context for the UUID.
     * @param name The name to be hashed under the given namespace. This is combined with the namespace to create a unique UUID.
     * @return A new UUID of version 5 that is derived from the provided namespace and name.
     * @since 1.0.0
     */
    fun v5(namespace: UUID, name: String): UUID {
        val sha1 = MessageDigest.getInstance("SHA-1")

        val namespaceBytes = namespace.toBytes()
        val nameBytes = name.toByteArray(Charsets.UTF_8)

        sha1.update(namespaceBytes)
        sha1.update(nameBytes)

        val hash = sha1.digest()

        hash[6] = (hash[6].toInt() and 0x0F or 0x50).toByte()
        hash[8] = (hash[8].toInt() and 0x3F or 0x80).toByte()

        return hash.toUUID()
    }
    /**
     * Generates a version 5 (SHA-1 hashed) UUID based on a given namespace and name.
     * This UUID generation complies with RFC 4122 standards for version 5 UUIDs.
     *
     * @param namespace The namespace UUID to use as the basis for the variant 5 UUID. This provides context for the UUID.
     * @param name The name to be hashed under the given namespace. This is combined with the namespace to create a unique UUID.
     * @return A new UUID of version 5 that is derived from the provided namespace and name.
     * @since 1.0.0
     */
    fun v5(namespace: Uuid, name: String) = v5(namespace.toJavaUuid(), name)

    /**
     * Generates a Version 6 UUID based on the timestamp, clock sequence, and node identifier.
     * Version 6 UUIDs reorder the timestamp for lexicographical sorting based on creation time.
     *
     * @return A UUID object representing the generated Version 6 UUID.
     * @since 1.0.0
     */
    fun v6(ts: Long?): UUID {
        val time = ts ?: ((System.currentTimeMillis() * 10000) + (GREGORIAN_EPOCH_OFFSET * 10))

        val timeHigh = (time shr 28) and 0xFFFFFFFFL
        val timeMid = (time shr 12) and 0xFFFFL
        val timeLowAndVersion = (time and 0x0FFFL) or (6L shl 12)

        val mostSigBits = (timeHigh shl 32) or (timeMid shl 16) or timeLowAndVersion

        val clockSeqAndVariant = ((clockSequence and 0x3FFF) or 0x8000).toLong()
        val leastSigBits = (clockSeqAndVariant shl 48) or node

        return UUID(mostSigBits, leastSigBits)
    }

    /**
     * Generates a UUID based on the version 7 specification, which incorporates
     * the current timestamp and random bytes to ensure uniqueness and proper ordering.
     *
     * @return a UUID instance that conforms to the version 7 UUID standard.
     * @since 1.0.0
     */
    fun v7(ts: Long?): UUID {
        val timestamp = ts ?: System.currentTimeMillis()
        val randBytes = ByteArray(10)
        random.nextBytes(randBytes)

        val mostSigBits = (timestamp shl 16) or
                ((randBytes[0].toLong() and 0x0F) or (7L shl 4) shl 8) or
                (randBytes[1].toLong() and 0xFF)

        val leastSigBits = ((randBytes[2].toLong() and 0x3F) or 0x80 shl 56) or
                ((randBytes[3].toLong() and 0xFF) shl 48) or
                ((randBytes[4].toLong() and 0xFF) shl 40) or
                ((randBytes[5].toLong() and 0xFF) shl 32) or
                ((randBytes[6].toLong() and 0xFF) shl 24) or
                ((randBytes[7].toLong() and 0xFF) shl 16) or
                ((randBytes[8].toLong() and 0xFF) shl 8) or
                (randBytes[9].toLong() and 0xFF)

        return UUID(mostSigBits, leastSigBits)
    }

    /**
     * Generates a UUIDv8 based on the specified type, timestamp, and string parameters.
     *
     * @param type The type of UUIDv8 generation to be used. Determines the structure and components
     *             of the resulting UUID. For instance, `STRING` for single string encoding,
     *             `TS_STRING` for combining a timestamp with a string, or others as defined in `UUIDv8Type`.
     * @param timestamp The timestamp value in milliseconds used for UUID generation, when applicable.
     *                  Required when the type involves a timestamp (e.g., `TS_STRING` or `TS_STRING_STRING`).
     *                  If the type does not utilize a timestamp, this parameter can be null.
     * @param strings A data structure containing string components utilized to construct the UUID.
     *                Depending on the `type`, it may involve one or two string elements.
     *                Throws a `RequiredParameterException` when mandatory fields are missing in certain types.
     * @since 1.0.0
     */
    fun v8(type: UUIDv8Type, timestamp: Long?, strings: Any2) = when (type) {
        UUIDv8Type.STRING -> {
            val sha1 = MessageDigest.getInstance("SHA-1")
            sha1.update(strings.first.toString().toByteArray(Charsets.UTF_8))
            val hash = sha1.digest()

            hash[6] = (hash[6].toInt() and 0x0F or 0x80).toByte()
            hash[8] = (hash[8].toInt() and 0x0F or 0x80).toByte()

            hash.toUUID()
        }
        UUIDv8Type.TS_STRING -> {
            val sha1 = MessageDigest.getInstance("SHA-1")
            val hash = sha1.digest(strings.first.toString().toByteArray(Charsets.UTF_8))

            val bb = ByteBuffer.allocate(16)
            bb.putLong(0, (timestamp ?: Instant.now().toEpochMilli()) shl 16)
            for (i in 0..<10)
                bb.put(6 + i, hash[i])

            val uuidBytes = bb.array()
            uuidBytes[6] = (uuidBytes[6].toInt() and 0x0F or 0x80).toByte()
            uuidBytes[8] = (uuidBytes[8].toInt() and 0x0F or 0x90).toByte()

            val bb2 = ByteBuffer.wrap(uuidBytes)
            UUID(bb2.long, bb2.long)
        }
        UUIDv8Type.STRING_RANDOM -> {
            val sha1 = MessageDigest.getInstance("SHA-1")
            val hash = sha1.digest(strings.first.toString().toByteArray(Charsets.UTF_8))

            val random = SecureRandom()
            val randomBytes = ByteArray(16)
            random.nextBytes(randomBytes)

            val bytes = ByteArray(16)

            System.arraycopy(hash, 0, bytes, 0, 8)
            System.arraycopy(randomBytes, 0, bytes, 8, 8)

            bytes[6] = (bytes[6].toInt() and 0x0F or 0x80).toByte()
            bytes[8] = (bytes[8].toInt() and 0x0F or 0xA0).toByte()

            val bb = ByteBuffer.wrap(bytes)
            UUID(bb.long, bb.long)
        }
        UUIDv8Type.STRING_STRING -> {
            val sha1 = MessageDigest.getInstance("SHA-1")
            sha1.update(strings.first.toString().toByteArray(Charsets.UTF_8))
            sha1.update(0.toByte()) // delimitatore per evitare ambiguità
            sha1.update(strings.second.toString().toByteArray(Charsets.UTF_8))
            val hash = sha1.digest()

            val bytes = hash.copyOfRange(0, 16)

            bytes[6] = (bytes[6].toInt() and 0x0F or 0x80).toByte()
            bytes[8] = (bytes[8].toInt() and 0x0F or 0x80).toByte()

            val bb = ByteBuffer.wrap(bytes)
            UUID(bb.long, bb.long)
        }
        UUIDv8Type.TS_STRING_STRING -> {
            val sha1 = MessageDigest.getInstance("SHA-1")
            sha1.update(strings.first.toString().toByteArray(Charsets.UTF_8))
            sha1.update(0.toByte()) // delimitatore
            sha1.update(strings.second.toString().toByteArray(Charsets.UTF_8))
            val hash = sha1.digest()

            val bytes = ByteArray(16)
            val bb = ByteBuffer.wrap(bytes)

            bb.putLong(0, (timestamp ?: Instant.now().toEpochMilli()) shl 16)

            System.arraycopy(hash, 0, bytes, 6, 9)

            bytes[15] = hash[9]

            bytes[6] = (bytes[6].toInt() and 0x0F or 0x80).toByte()
            bytes[8] = (bytes[8].toInt() and 0x0F or 0x90).toByte()

            val bb2 = ByteBuffer.wrap(bytes)
            UUID(bb2.long, bb2.long)
        }
        UUIDv8Type.STRING_STRING_RANDOM -> {
            val sha1 = MessageDigest.getInstance("SHA-1")
            sha1.update(strings.first.toString().toByteArray(Charsets.UTF_8))
            sha1.update(0.toByte())
            sha1.update(strings.second.toString().toByteArray(Charsets.UTF_8))
            val hash = sha1.digest()

            val random = SecureRandom()
            val randomBytes = ByteArray(16)
            random.nextBytes(randomBytes)

            val bytes = ByteArray(16)

            System.arraycopy(hash, 0, bytes, 0, 8)
            System.arraycopy(randomBytes, 0, bytes, 8, 8)

            bytes[6] = (bytes[6].toInt() and 0x0F or 0x80).toByte()
            bytes[8] = (bytes[8].toInt() and 0x0F or 0xA0).toByte()

            val bb = ByteBuffer.wrap(bytes)
            UUID(bb.long, bb.long)
        }
    }

    /**
     * Converts the UUID instance into a ByteArray representation.
     *
     * The generated byte array consists of 16 bytes in total,
     * where the first 8 bytes represent the most significant
     * bits, and the latter 8 bytes represent the least significant bits.
     *
     * @return a ByteArray containing the 16-byte representation of the UUID.
     * @since 1.0.0
     */
    private fun UUID.toBytes(): ByteArray {
        val buffer = ByteBuffer.wrap(ByteArray(16))
        buffer.putLong(this.mostSignificantBits)
        buffer.putLong(this.leastSignificantBits)
        return buffer.array()
    }

    /**
     * Converts the ByteArray into a UUID object. The first 8 bytes of the array
     * are used to construct the most significant bits, while the next 8 bytes
     * are used for the least significant bits of the UUID.
     *
     * @return A UUID object created from the ByteArray.
     * @since 1.0.0
     */
    private fun ByteArray.toUUID(): UUID {
        val buffer = ByteBuffer.wrap(this)
        val mostSigBits = buffer.long
        val leastSigBits = buffer.long
        return UUID(mostSigBits, leastSigBits)
    }
}