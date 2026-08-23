/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("UuidUtilsKt")
@file:Suppress("unused", "functionName", "kutils_uuid_fromstring_as_uuid", "kutils_randomuuid_as_uuid", "kutils_collection_declaration", "kutils_temporal_now_as_temporal")
@file:Since("3.0.0")
@file:MustUseReturnValues

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.identifiers.*
import dev.tommasop1804.kutils.exceptions.*
import java.net.NetworkInterface
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.*

/**
 * Represents a UUID value where all bits are set to zero.
 * Often used to signify a "null" or "uninitialized" UUID.
 *
 * @since 1.0.0
 */
val NIL_UUID: Uuid = UUID.fromString("00000000-0000-0000-0000-000000000000")
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
val MAX_UUID: Uuid = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff")

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
val Uuid.instant: Instant
    get() = if (version() == 1) Instant(timestamp()) else Ulid(this).instant

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
val Uuid.version: UuidVersion
    get() = UuidVersion.fromVersion(version())!!

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
val Uuid.variant: Int
    get() = variant()

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
val Uuid.subvariant: Int
    get() {
        val bb = ByteBuffer.allocate(16)
        bb.putLong(mostSignificantBits)
        bb.putLong(leastSignificantBits)
        val bytes = bb.array()

        return (bytes[8].toInt() and 0x30) shr 4
    }

/**
 * Extension property for `UUID` that returns the string representation of the UUID with all hyphens removed.
 *
 * This can be useful when a compact representation of the UUID is required.
 *
 * @since 3.0.0
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
 * @since 3.0.0
 */
fun Uuid(
    version: UuidVersion = UuidVersion.V4,
    namespaceAndName: Pair<Uuid, String>? = null,
    timestamp: Instant? = null,
    v8Type: UuidV8Type? = null,
    v8Strings: Any2? = null
): Uuid = when(version) {
    UuidVersion.V1 -> UuidGenerator.v1(timestamp?.toEpochMilli())
    UuidVersion.V2 -> throw UnsupportedOperationException("DCE_SECURITY generation is not supported .")
    UuidVersion.V3 -> UuidGenerator.v3(
        (namespaceAndName ?: throw RequiredParameterException("UUID", "namespaceAndName", NullPointerException("namespaceAndName"))).first,
        namespaceAndName.second
    )
    UuidVersion.V4 -> UUID.randomUUID()
    UuidVersion.V5 -> UuidGenerator.v5(
        (namespaceAndName ?: throw RequiredParameterException("UUID", "namespaceAndName", NullPointerException("namespaceAndName"))).first,
        namespaceAndName.second
    )
    UuidVersion.V6 -> UuidGenerator.v6(timestamp?.toEpochMilli())
    UuidVersion.V7 -> UuidGenerator.v7(timestamp?.toEpochMilli())
    UuidVersion.V8 -> UuidGenerator.v8(v8Type ?: throw RequiredParameterException("UUID", "v8Type"), timestamp?.toEpochMilli(), v8Strings ?: throw RequiredParameterException("UUID", "v8Strings"))
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
 * @param timestamp The timestamp of the UUID. Supported version include UUIDv1, UUIDv6, UUIDv7, pontentially UUIDv8.
 * @param v8Type An optional parameter for UUIDv8. This parameter is ignored for other versions.
 * @param v8String An optional parameter for UUIDv8. This parameter is ignored for other versions.
 * @return A UUID instance corresponding to the specified version and provided inputs.
 * @since 3.0.0
 */
fun Uuid(
    version: UuidVersion,
    timestamp: Instant? = null,
    v8Type: UuidV8Type? = null,
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
 * @since 3.0.0
 */
@JvmName("UuidUuidVersionUuidNamespaceString")
fun Uuid(version: UuidVersion, namespaceAndName: Pair<UuidNamespace, String>) = Uuid(version, namespaceAndName.map(f1 = UuidNamespace::predefinedNamespace))
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
 * @since 3.0.0
 */
fun Uuid(number: Number, version: UuidVersion, namespaceAndName: Pair<Uuid, String>? = null, timestamp: Instant? = null): List<Uuid> {
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
 * @since 3.0.0
 */
@JvmName("UuidNumberUuidVersionUuidNamespaceString")
fun Uuid(number: Number, version: UuidVersion, namespaceAndName: Pair<UuidNamespace, String>): List<Uuid> {
    val result = mutableListOf<UUID>()
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
 * @since 3.0.0
 */
fun Uuid(string: String): Uuid = UUID.fromString(string)!!
/**
 * Converts a given ULID to its corresponding UUID representation.
 *
 * @param ulid The ULID instance to be converted into a UUID.
 * @return A UUID generated from the provided ULID.
 * @since 3.0.0
 */
fun Uuid(ulid: Ulid) = ulid.toUuid()
/**
 * Converts a [ShortUuid] instance into its full [UUID] representation.
 *
 * This function decodes the shortened, compact [ShortUuid] string back into
 * a standard [UUID] object, restoring the original full representation of the UUID.
 * Useful for scenarios where a compact identifier needs to be reverted to its full counterpart.
 *
 * @param shortUuid The [ShortUuid] object to be converted to a standard [UUID].
 * @return The full [UUID] representation derived from the given [ShortUuid].
 * @since 3.0.0
 */
fun Uuid(shortUuid: ShortUuid) = shortUuid.toUuid()
/**
 * Converts the current [CharSequence] to a [UUID] instance. The method attempts to parse
 * the [CharSequence] as a valid UUID string.
 *
 * The conversion process is encapsulated within a [Result] using `runCatching`,
 * allowing the caller to handle parsing errors without throwing an exception.
 *
 * @return A [Result] containing the parsed [UUID] if successful, or the exception
 *         thrown during parsing if the input is not a valid UUID string.
 * @since 3.0.0
 */
fun CharSequence.toUuid(): Result<Uuid> = runCatching { UUID.fromString(toString())!! }
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
fun CharSequence.toKotlinUuid() = runCatching { kotlin.uuid.Uuid.parse(toString()) }

/**
 * Validates whether the provided string is a valid UUID.
 *
 * @param string The string to validate as a UUID.
 * @return True if the string is a valid UUID, otherwise false.
 * @since 3.0.0
 */
fun isValidUuid(string: String) = runCatching { UUID.fromString(string) }.isSuccess

/**
 * Returns the most significant bits of this UUID.
 * This operation allows the UUID to be destructured as its components.
 *
 * @receiver UUID instance from which the most significant bits will be retrieved.
 * @return The most significant 64 bits of the UUID as a `Long`.
 * @since 1.0.0
 */
operator fun Uuid.component1() = mostSignificantBits
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
operator fun Uuid.component2() = leastSignificantBits

/**
 * A utility object for generating UUIDs conforming to various versions (v1, v3, v5, v6, v7).
 * This object uses a combination of system time, random values, and hashing mechanisms to create unique identifiers.
 *
 * @since 3.0.0
 */
private object UuidGenerator {
    /**
     * The offset in milliseconds between the Gregorian epoch (1582-10-15) and the Unix epoch (1970-01-01).
     * This constant is used to convert or calculate time values between the two epoch systems.
     *
     * @since 3.0.0
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
     * @since 3.0.0
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
     * @since 3.0.0
     */
    private val node: Long = try {
        val networkInterface = NetworkInterface.getNetworkInterfaces().toList()
            .firstOrNull { it.isUp && !it.isLoopback && it.hardwareAddress != null }
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
     * @since 3.0.0
     */
    private var clockSequence: Int = random.nextInt(0x3FFF)

    /**
     * Generates a Version 1 UUID based on the current timestamp and random values for the node and clock sequence.
     *
     * Version 1 UUIDs are time-based and use the timestamp, a clock sequence, and a simulated node identifier (e.g., MAC address).
     *
     * @return A UUID of type 1 (time-based UUID).
     * @since 3.0.0
     */
    fun v1(ts: Long?): Uuid {
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
     * @since 3.0.0
     */
    fun v3(namespace: Uuid, name: String): Uuid {
        val md5 = MessageDigest.getInstance("MD5")

        val namespaceBytes = namespace.toBytes()
        val nameBytes = name.toByteArray(Charsets.UTF_8)

        md5.update(namespaceBytes)
        md5.update(nameBytes)

        val hash = md5.digest()

        // Imposta i bit di versione (3) e variante
        hash[6] = (hash[6].toInt() and 0x0F or 0x30).toByte()
        hash[8] = (hash[8].toInt() and 0x3F or 0x80).toByte()

        return hash.toUuid()
    }

    /**
     * Generates a version 5 (SHA-1 hashed) UUID based on a given namespace and name.
     * This UUID generation complies with RFC 4122 standards for version 5 UUIDs.
     *
     * @param namespace The namespace UUID to use as the basis for the variant 5 UUID. This provides context for the UUID.
     * @param name The name to be hashed under the given namespace. This is combined with the namespace to create a unique UUID.
     * @return A new UUID of version 5 that is derived from the provided namespace and name.
     * @since 3.0.0
     */
    fun v5(namespace: Uuid, name: String): Uuid {
        val sha1 = MessageDigest.getInstance("SHA-1")

        val namespaceBytes = namespace.toBytes()
        val nameBytes = name.toByteArray(Charsets.UTF_8)

        sha1.update(namespaceBytes)
        sha1.update(nameBytes)

        val hash = sha1.digest()

        hash[6] = (hash[6].toInt() and 0x0F or 0x50).toByte()
        hash[8] = (hash[8].toInt() and 0x3F or 0x80).toByte()

        return hash.toUuid()
    }

    /**
     * Generates a Version 6 UUID based on the timestamp, clock sequence, and node identifier.
     * Version 6 UUIDs reorder the timestamp for lexicographical sorting based on creation time.
     *
     * @return A UUID object representing the generated Version 6 UUID.
     * @since 3.0.0
     */
    fun v6(ts: Long?): Uuid {
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
     * @since 3.0.0
     */
    fun v7(ts: Long?): Uuid {
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
     * @since 3.0.0
     */
    fun v8(type: UuidV8Type, timestamp: Long?, strings: Any2): Uuid = when (type) {
        UuidV8Type.String -> {
            val sha1 = MessageDigest.getInstance("SHA-1")
            sha1.update(strings.first.toString().toByteArray(Charsets.UTF_8))
            val hash = sha1.digest()

            hash[6] = (hash[6].toInt() and 0x0F or 0x80).toByte()
            hash[8] = (hash[8].toInt() and 0x0F or 0x80).toByte()

            hash.toUuid()
        }
        UuidV8Type.TimestampString -> {
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
            Uuid(bb2.long, bb2.long)
        }
        UuidV8Type.StringRandom -> {
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
            Uuid(bb.long, bb.long)
        }
        UuidV8Type.StringString -> {
            val sha1 = MessageDigest.getInstance("SHA-1")
            sha1.update(strings.first.toString().toByteArray(Charsets.UTF_8))
            sha1.update(0.toByte()) // delimitatore per evitare ambiguità
            sha1.update(strings.second.toString().toByteArray(Charsets.UTF_8))
            val hash = sha1.digest()

            val bytes = hash.copyOfRange(0, 16)

            bytes[6] = (bytes[6].toInt() and 0x0F or 0x80).toByte()
            bytes[8] = (bytes[8].toInt() and 0x0F or 0x80).toByte()

            val bb = ByteBuffer.wrap(bytes)
            Uuid(bb.long, bb.long)
        }
        UuidV8Type.TimestampStringString -> {
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
            Uuid(bb2.long, bb2.long)
        }
        UuidV8Type.StringStringRandom -> {
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
            Uuid(bb.long, bb.long)
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
     * @since 3.0.0
     */
    private fun Uuid.toBytes(): ByteArray {
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
     * @since 3.0.0
     */
    private fun ByteArray.toUuid(): Uuid {
        val buffer = ByteBuffer.wrap(this)
        val mostSigBits = buffer.long
        val leastSigBits = buffer.long
        return Uuid(mostSigBits, leastSigBits)
    }
}