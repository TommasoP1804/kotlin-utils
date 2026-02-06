package dev.tommasop1804.kutils.classes.identifiers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.Supplier
import dev.tommasop1804.kutils.Transformer
import dev.tommasop1804.kutils.classes.identifiers.ULID.Companion.generateHashULID
import dev.tommasop1804.kutils.classes.identifiers.ULID.Factory.ByteRandom.Companion.newRandomFunction
import dev.tommasop1804.kutils.classes.numbers.Hex.Companion.toHex
import dev.tommasop1804.kutils.expect
import dev.tommasop1804.kutils.isNull
import dev.tommasop1804.kutils.validate
import dev.tommasop1804.kutils.validateInputFormat
import jakarta.persistence.AttributeConverter
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.type.SqlTypes
import org.hibernate.usertype.EnhancedUserType
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serializable
import java.lang.Byte
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.Clock
import java.time.Instant
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.Any
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.CharArray
import kotlin.CharSequence
import kotlin.Comparable
import kotlin.IllegalArgumentException
import kotlin.IllegalStateException
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.Result
import kotlin.String
import kotlin.Suppress
import kotlin.code
import kotlin.let
import kotlin.runCatching
import kotlin.time.ExperimentalTime

/**
 * Represents a Universally Unique Lexicographically Sortable Identifier (ULID).
 *
 * ULIDs are 128-bit identifiers designed to be lexicographically sortable and universally unique.
 * They contain two main components: a timestamp for ordering and a random component for uniqueness.
 *
 * @property mostSignificantBits The most significant 64 bits of the ULID.
 * @property leastSignificantBits The least significant 64 bits of the ULID.
 * @property instant Optional timestamp component of the ULID, typically representing creation time.
 * @property timestamp Optional time field derived from the timestamp for lexicographical sorting.
 * @property randomComponent Optional random component that guarantees uniqueness within the same timestamp.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = ULID.Companion.Serializer::class)
@JsonDeserialize(using = ULID.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = ULID.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = ULID.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_temporal_of_as_temporal")
class ULID (val mostSignificantBits: Long, val leastSignificantBits: Long) : Comparable<ULID>, Serializable {

    /**
     * Represents the point in time derived from the ULID's timestamp.
     * This value is computed using the epoch millisecond value of the ULID's timestamp.
     *
     * @since 1.0.0
     */
    val instant: Instant = Instant.ofEpochMilli(timestamp)

    /**
     * Represents the time component of the ULID, derived by shifting the
     * most significant bits by 16 positions to the right.
     *
     * This value provides the temporal portion of the ULID, which is encoded
     * in the most significant bits of the identifier.
     *
     * @since 1.0.0
     */
    val timestamp
        get() = mostSignificantBits ushr 16

    /**
     * Represents the random component of a ULID as a ByteArray.
     *
     * This property extracts a sequence of random bytes derived from the most significant
     * and least significant bits of the ULID. The first two bytes are derived from the
     * most significant bits, and the remaining bytes are derived from various segments
     * of the least significant bits. This data structure aims to conform to the random
     * component section of the ULID specification.
     *
     * @return A ByteArray containing the random component of the ULID.
     * @since 1.0.0
     */
    val randomComponent: ByteArray
        get() {
            val bytes = ByteArray(RANDOM_BYTES)

            bytes[0x0] = (mostSignificantBits ushr 8).toByte()
            bytes[0x1] = (mostSignificantBits).toByte()

            bytes[0x2] = (leastSignificantBits ushr 56).toByte()
            bytes[0x3] = (leastSignificantBits ushr 48).toByte()
            bytes[0x4] = (leastSignificantBits ushr 40).toByte()
            bytes[0x5] = (leastSignificantBits ushr 32).toByte()
            bytes[0x6] = (leastSignificantBits ushr 24).toByte()
            bytes[0x7] = (leastSignificantBits ushr 16).toByte()
            bytes[0x8] = (leastSignificantBits ushr 8).toByte()
            bytes[0x9] = (leastSignificantBits).toByte()

            return bytes
        }

    /**
     * Secondary constructor for creating a ULID instance using another ULID.
     * Copies the most significant and least significant bits from the provided ULID.
     *
     * @param ulid The ULID instance from which to copy the bit values.
     * @since 1.0.0
     */
    private constructor(ulid: ULID) : this(ulid.mostSignificantBits, ulid.leastSignificantBits)
    /**
     * Constructs a ULID (Universally Unique Lexicographically Sortable Identifier) instance
     * by combining the provided time and random components.
     *
     * The resulting ULID is comprised of 128 bits, where:
     * - The `time` component contributes 48 bits.
     * - The `random` component contributes 80 bits.
     *
     * @param time The time component of this ULID. Must fit within 48 bits.
     * @param random The random component of this ULID. Must be exactly 10 bytes in length.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the `time` does not fit within 48 bits.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the size of the `random` array is different from 10 bytes.
     * @since 1.0.0
     */
    constructor(time: Long, random: ByteArray) : this(
        calculateMostSignificantBits(time, random),
        calculateLeastSignificantBits(random)
    ) {
        // The time component has 48 bits.
        validate((time and -0x1000000000000L) == 0L) { "Invalid time value" }

        // The random component has 80 bits (10 bytes).
        validate(random.size == RANDOM_BYTES) { "Invalid random bytes" }
    }
    /**
     * Constructs a new ULID instance from a string representation.
     *
     * This secondary constructor takes a string input, parses it using the `from` function,
     * and initializes a new ULID instance. The resulting ULID instance corresponds to the
     * string representation provided.
     *
     * @param string The string representation of the ULID to be converted into an instance.
     * @since 1.0.0
     */
    constructor(string: String) : this(from(string))
    /**
     * Constructs a new ULID instance with the given timestamp and the option to ensure monotonicity.
     *
     * This constructor creates a ULID by generating its internal bit representation based on the provided
     * timestamp in milliseconds. If monotonicity is enabled, the ULID ensures that subsequent ULIDs with the
     * same or slightly decreasing timestamps maintain monotonic ordering.
     *
     * @param instant The point in time used to generate the ULID, represented as an `Instant`.
     * @param monotonic A flag indicating whether to generate a monotonic ULID. If `true`, the ULID will ensure
     * monotonicity for closely related timestamps. Default is `false`.
     * @since 1.0.0
     */
    constructor(instant: Instant, monotonic: Boolean = false) : this(if (monotonic) generateMonotonic(instant.toEpochMilli()) else generate(instant.toEpochMilli()))
    /**
     * Constructs a new ULID instance with the given timestamp and the option to ensure monotonicity.
     *
     * This constructor creates a ULID by generating its internal bit representation based on the provided
     * timestamp in milliseconds. If monotonicity is enabled, the ULID ensures that subsequent ULIDs with the
     * same or slightly decreasing timestamps maintain monotonic ordering.
     *
     * @param instant The point in time used to generate the ULID, represented as an `Instant`.
     * @param monotonic A flag indicating whether to generate a monotonic ULID. If `true`, the ULID will ensure
     * monotonicity for closely related timestamps. Default is `false`.
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(instant: kotlin.time.Instant, monotonic: Boolean = false) : this(if (monotonic) generateMonotonic(instant.toEpochMilliseconds()) else generate(instant.toEpochMilliseconds()))
    /**
     * Constructs a ULID instance based on the provided timestamp and monotonicity flag.
     *
     * If the `monotonic` flag is set to `true`, the ULID is generated using the `generateMonotonic` method,
     * ensuring that the ULIDs maintain monotonic behavior even when the clock moves backward or remains unchanged.
     * Otherwise, the ULID is generated using the standard `generate` method with the given timestamp.
     *
     * @param time The timestamp in milliseconds used for ULID generation.
     * @param monotonic A flag indicating whether the ULID should be generated with monotonicity guarantees.
     * Defaults to `false`, implying standard (non-monotonic) generation.
     *
     * @since 1.0.0
     */
    constructor(time: Long, monotonic: Boolean = false) : this(if (monotonic) generateMonotonic(time) else generate(time))
    /**
     * Constructs a ULID instance from the given UUID.
     *
     * This constructor utilizes the `from` function to extract the most significant
     * and least significant bits of the provided `UUID` and initializes a new ULID
     * instance with the derived values.
     *
     * @param uuid The UUID from which the ULID will be constructed.
     * @since 1.0.0
     */
    constructor(uuid: UUID) : this(from(uuid))
    /**
     * Constructs a ULID instance from the given byte array.
     *
     * This constructor invokes the `from` function to create a ULID instance
     * using the provided `bytes` array, which must represent a 128-bit value.
     *
     * @param bytes The byte array representing a 128-bit ULID value.
     * It must be exactly 16 bytes in length, with the first 8 bytes
     * representing the most significant bits and the last 8 bytes
     * representing the least significant bits.
     * @since 1.0.0
     */
    constructor(bytes: ByteArray) : this(from(bytes))
    /**
     * Constructs a ULID instance from the given timestamp and string input by generating
     * a hashed ULID using the provided parameters.
     *
     * This constructor utilizes the `generateHashULID` function to produce a ULID value
     * based on the combination of the timestamp (`time`) and the string (`string`).
     * The resulting ULID is then passed to the primary constructor.
     *
     * @param time The timestamp used to generate the ULID.
     * @param string The string input to be hashed along with the timestamp to create the ULID.
     * @since 1.0.0
     */
    constructor(time: Long, string: String) : this(generateHashULID(time, string))
    /**
     * Constructs a ULID instance by generating a hashed ULID based on the provided timestamp and string input.
     *
     * This constructor utilizes the supplied `time` and `string` parameters to generate
     * the most significant and least significant bits of the ULID through the `generateHashULID` function.
     *
     * @param time The timestamp used to calculate the ULID, represented as an `Instant`.
     * @param string A string input used to influence the hash generation of the ULID.
     * @since 1.0.0
     */
    constructor(time: Instant, string: String) : this(generateHashULID(time.toEpochMilli(), string))
    /**
     * Constructs a ULID instance by generating a hashed ULID based on the provided timestamp and string input.
     *
     * This constructor utilizes the supplied `time` and `string` parameters to generate
     * the most significant and least significant bits of the ULID through the `generateHashULID` function.
     *
     * @param time The timestamp used to calculate the ULID, represented as an `Instant`.
     * @param string A string input used to influence the hash generation of the ULID.
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(time: kotlin.time.Instant, string: String) : this(generateHashULID(time.toEpochMilliseconds(), string))
    /**
     * Constructs a new ULID instance using a specific timestamp and a hashed representation of a byte array.
     *
     * This constructor creates the ULID by generating a random component derived from the SHA-256 hash of the provided
     * byte array. The timestamp and the random component are used to ensure both temporal ordering and uniqueness.
     *
     * @param time An `Instant` representing the timestamp portion of the ULID. Must fit within 48 bits.
     * @param bytes A byte array to be hashed for generating the random component of the ULID.
     * @throws IllegalArgumentException If the timestamp exceeds 48 bits or if the hash-derived random component is invalid.
     * @see generateHashULID
     * @since 1.0.0
     */
    constructor(time: Instant, bytes: ByteArray) : this(generateHashULID(time.toEpochMilli(), bytes))
    /**
     * Constructs a new ULID instance by deriving its components from the given ULID and time values.
     *
     * This constructor takes an existing ULID and extracts its random component, which is then combined
     * with the provided time value to create a new ULID instance. The resulting ULID retains the specified
     * time while using the random component of the provided ULID.
     *
     * @param ulid The existing ULID instance from which the random component will be extracted.
     * @param time The time value to set in the new ULID instance.
     * @since 1.0.0
     */
    constructor(ulid: ULID, time: Long) : this(time, ulid.randomComponent)
    /**
     * Secondary constructor for the ULID class.
     *
     * This constructor is used to create a ULID instance by combining the timestamp and
     * the random component from an existing ULID instance and a provided `Instant`.
     *
     * The `instant` parameter is converted to its epoch millisecond value, and the
     * `randomComponent` is extracted from the provided ULID to construct the new instance.
     *
     * @param ulid The ULID instance from which the random component is derived.
     * @param time The `Instant` used to set the timestamp.
     * @since 1.0.0
     */
    constructor(ulid: ULID, time: Instant) : this(time.toEpochMilli(), ulid.randomComponent)
    /**
     * Secondary constructor for the ULID class.
     *
     * This constructor is used to create a ULID instance by combining the timestamp and
     * the random component from an existing ULID instance and a provided `Instant`.
     *
     * The `instant` parameter is converted to its epoch millisecond value, and the
     * `randomComponent` is extracted from the provided ULID to construct the new instance.
     *
     * @param ulid The ULID instance from which the random component is derived.
     * @param time The `Instant` used to set the timestamp.
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(ulid: ULID, time: kotlin.time.Instant) : this(time.toEpochMilliseconds(), ulid.randomComponent)
    /**
     * Constructs a new ULID using a ULID instance and optionally enforces monotonicity for sequential ULID generation.
     * The ULID has the same timestamp of the given one, but a different random part.
     *
     * @param ulid The ULID instance used as a reference for generating the new ULID.
     * @param monotonic Specifies whether the monotonic generation should be applied. If `true`,
     * ensures that the generated ULIDs are ordered sequentially in strictly increasing order. Defaults to `false`.
     * @since 1.0.0
     */
    constructor(ulid: ULID, monotonic: Boolean = false) : this(ulid.timestamp, monotonic)
    /**
     * Creates a ULID with the same timestamp as the provided `ulid` but derived
     * from the specified `string`.
     *
     * This constructor facilitates the creation of a ULID that maintains temporal association
     * with the given ULID while incorporating uniqueness defined by the input string.
     *
     * @param ulid The ULID whose timestamp will be preserved in the new instance.
     * @param string The input string used to derive a new ULID with the same timestamp.
     * @since 1.0.0
     */
    constructor(ulid: ULID, string: String) : this(ulid.timestamp, string)
    /**
     * Constructs a new ULID instance using a specified random component while keeping
     * the timestamp from the provided `ULID` instance.
     *
     * Creates a new ULID by combining its timestamp with the given random component.
     *
     * @param ulid The source ULID instance whose timestamp is used.
     * @param bytes The random component to be used for creating the new ULID.
     * Must be exactly 10 bytes in length.
     * @throws IllegalArgumentException If the size of the `bytes` array is different from 10 bytes.
     * @since 1.0.0
     */
    constructor(ulid: ULID, bytes: ByteArray) : this(ulid.timestamp, bytes)
    /**
     * Constructs a ULID instance based on the provided monotonicity preference.
     *
     * If `monotonic` is set to `true`, the constructor will generate the ULID using a monotonic algorithm,
     * ensuring sequential ordering for ULIDs generated within the same timestamp. Otherwise, it generates
     * a standard ULID instance using a non-monotonic approach.
     *
     * @param monotonic Indicates whether the ULID should be generated using the monotonic factory (`true`)
     * or the standard factory (`false`). Defaults to `false`.
     * @since 1.0.0
     */
    constructor(monotonic: Boolean = false) : this(if (monotonic) generateMonotonic() else generate())

    companion object {
        /**
         * A constant representing the number of unique characters used in the Base32 encoding
         * for ULID (Universally Unique Lexicographically Sortable Identifier) generation.
         *
         * The ULID_CHARS constant defines the size of the character set utilized in the encoding
         * and ensures compatibility with the ULID specification.
         *
         * @since 1.0.0
         */
        const val ULID_CHARS: Int = 26
        /**
         * Defines the number of characters used to represent the time component
         * of a ULID (Universally Unique Lexicographically Sortable Identifier).
         *
         * This constant specifies the fixed length of the time component
         * when encoding or decoding a ULID.
         *
         * @since 1.0.0
         */
        const val TIME_CHARS: Int = 10
        /**
         * Defines the default length of the random character component used in the ULID generation process.
         * The value represents the standard size for ensuring unique identifiers.
         *
         * @since 1.0.0
         */
        const val RANDOM_CHARS: Int = 16

        /**
         * Represents the number of bytes required to store a ULID (Universally Unique Lexicographically Sortable Identifier).
         * ULIDs are 128-bit identifiers, which equate to 16 bytes.
         *
         * @since 1.0.0
         */
        const val ULID_BYTES: Int = 16
        /**
         * Represents the fixed size in bytes allocated for encoding the time component
         * in the ULID (Universally Unique Lexicographically Sortable Identifier) format.
         * The time component is a significant part of the ULID, providing sorting capabilities
         * based on the time of creation.
         *
         * @since 1.0.0
         */
        const val TIME_BYTES: Int = 6
        /**
         * Specifies the number of random bytes to be used in the generation process.
         *
         * This constant is utilized for defining the length of random byte data required
         * for generating unique identifiers, ensuring sufficient entropy.
         *
         * @since 1.0.0
         */
        const val RANDOM_BYTES: Int = 10

        /**
         * The smallest possible ULID value where both the most significant
         * and least significant bits are set to zero.
         *
         * This represents the minimum boundary for ULID values and can be
         * used as a reference or sentinel value when working with ULID
         * instances.
         *
         * @since 1.0.0
         */
        val MIN: ULID = ULID(0x0000000000000000L, 0x0000000000000000L)
        /**
         * A constant ULID value representing the maximum possible ULID.
         * It is constructed using the largest possible values for both the
         * most significant and least significant bits.
         *
         * This value can be used as a reference point for comparisons or
         * as a limit in operations involving ULIDs.
         *
         * @since 1.0.0
         */
        val MAX: ULID = ULID(-0x1L, -0x1L)

        /**
         * A `ByteArray` containing precomputed values for encoding and decoding operations
         * associated with alphabetic characters, used in the context of ULID generation
         * or processing.
         *
         * The array is initialized with 256 elements to represent all possible byte values.
         * Specific indices hold mappings for easy retrieval of encoded or decoded values.
         *
         * This variable is intended for internal use to optimize performance during
         * encoding or decoding operations.
         *
         * @since 1.0.0
         */
        val ALPHABET_VALUES: ByteArray = ByteArray(256)
        /**
         * Represents an array of uppercase alphanumeric characters excluding ambiguous letters
         * such as 'I' and 'O'. This character pool is typically used in contexts requiring
         * clearer differentiation of symbols, such as unique identifier generation.
         *
         * The character set includes numbers (0-9) and uppercase letters from the English alphabet
         * (A-Z), excluding 'I' and 'O'.
         *
         * @since 1.0.0
         */
        val ALPHABET_UPPERCASE: CharArray = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray()
        /**
         * A character array containing the lowercase alphabet along with numeric characters,
         * specifically excluding ambiguous characters like 'i', 'l', 'o', and 'u'.
         * This array is often used for generating unique identifiers or encoding data into
         * a concise, compact, and human-readable format.
         *
         * @since 1.0.0
         */
        val ALPHABET_LOWERCASE: CharArray = "0123456789abcdefghjkmnpqrstvwxyz".toCharArray()

        /**
         * Represents the result of an overflow in a binary increment operation.
         * This constant holds the value that results when the maximum unsigned
         * 64-bit integer (0xffffffffffffffffL) is incremented by 1, causing it to wrap
         * around to 0x0000000000000000L.
         *
         * @since 1.0.0
         */
        // 0xffffffffffffffffL + 1 = 0x0000000000000000L
        private const val INCREMENT_OVERFLOW = 0x0000000000000000L

        init {
            // Initialize the alphabet map with -1
            Arrays.fill(ALPHABET_VALUES, (-1).toByte())

            // Map the alphabets chars to values
            for (i in ALPHABET_UPPERCASE.indices)
                ALPHABET_VALUES[ALPHABET_UPPERCASE[i].code] = i.toByte()
            for (i in ALPHABET_LOWERCASE.indices)
                ALPHABET_VALUES[ALPHABET_LOWERCASE[i].code] = i.toByte()

            // Upper case OIL
            ALPHABET_VALUES['O'.code] = 0x00
            ALPHABET_VALUES['I'.code] = 0x01
            ALPHABET_VALUES['L'.code] = 0x01


            // Lower case OIL
            ALPHABET_VALUES['o'.code] = 0x00
            ALPHABET_VALUES['i'.code] = 0x01
            ALPHABET_VALUES['l'.code] = 0x01
        }

        /**
         * Calculates the most significant bits of the ULID based on the given time and random byte array.
         *
         * @param time The timestamp to be used for generating the most significant bits.
         * @param random A byte array providing additional randomness for the calculation.
         * @return A Long value representing the most significant bits of the ULID.
         * @since 1.0.0
         */
        private fun calculateMostSignificantBits(time: Long, random: ByteArray): Long {
            var long0: Long = 0
            long0 = long0 or (time shl 16)
            long0 = long0 or ((random[0x0].toInt() and 0xff).toLong() shl 8)
            long0 = long0 or (random[0x1].toInt() and 0xff).toLong()
            return long0
        }

        /**
         * Calculates the least significant bits of a ULID from the given byte array.
         *
         * @param random A byte array containing random data from which the least significant bits are generated.
         *               The array is assumed to have a minimum length of 10 bytes, where the required bytes are taken
         *               starting from index 2 to index 9.
         * @return A long value representing the least significant bits derived from the given byte array.
         * @since 1.0.0
         */
        private fun calculateLeastSignificantBits(random: ByteArray): Long {
            var long1: Long = 0
            long1 = long1 or ((random[0x2].toInt() and 0xff).toLong() shl 56)
            long1 = long1 or ((random[0x3].toInt() and 0xff).toLong() shl 48)
            long1 = long1 or ((random[0x4].toInt() and 0xff).toLong() shl 40)
            long1 = long1 or ((random[0x5].toInt() and 0xff).toLong() shl 32)
            long1 = long1 or ((random[0x6].toInt() and 0xff).toLong() shl 24)
            long1 = long1 or ((random[0x7].toInt() and 0xff).toLong() shl 16)
            long1 = long1 or ((random[0x8].toInt() and 0xff).toLong() shl 8)
            long1 = long1 or (random[0x9].toInt() and 0xff).toLong()
            return long1
        }

        /**
         * Generates a new ULID instance using the `Factory` singleton.
         *
         * This method utilizes the `Factory.Holder.INSTANCE` to create a ULID with its
         * default configuration. The generated ULID is based on the current system time
         * and a random component to ensure uniqueness.
         *
         * @return a newly created ULID instance
         * @since 1.0.0
         */
        private fun generate() = Factory.Holder.INSTANCE.create()
        /**
         * Generates a ULID (Universally Unique Lexicographically Sortable Identifier) based on the provided time.
         *
         * @param time The timestamp in milliseconds to be used for ULID generation.
         * @since 1.0.0
         */
        private fun generate(time: Long) = Factory.Holder.INSTANCE.create(time)

        /**
         * Generates a monotonic ULID (Universally Unique Lexicographically Sortable Identifier) using the default monotonic factory instance.
         * This method guarantees monotonicity by ensuring that ULIDs generated for the same timestamp are ordered
         * and incremented sequentially if generated within the same clock cycle.
         *
         * Internally, this utilizes a singleton monotonic factory instance (`Factory.MonotonicHolder.INSTANCE`)
         * to create ULIDs with enhanced uniqueness and proper handling of clock drift tolerance.
         *
         * @return A newly generated monotonic ULID.
         * @since 1.0.0
         */
        private fun generateMonotonic() = Factory.MonotonicHolder.INSTANCE.create()
        /**
         * Generates a monotonic ULID based on the provided timestamp.
         * This method ensures that ULIDs generated with the same or slightly decreasing timestamps
         * (due to clock adjustments or leap seconds) maintain monotonicity.
         *
         * @param time The timestamp in milliseconds to base the ULID generation on.
         * It is expected to be the current system time, but can also handle scenarios
         * where the clock moves backward or remains unchanged.
         *
         * @since 1.0.0
         */
        private fun generateMonotonic(time: Long) = Factory.MonotonicHolder.INSTANCE.create(time)

        /**
         * Generates a hashed ULID using the provided timestamp and string input.
         *
         * @param time The timestamp used to compute the ULID.
         * @param string The string input which will be hashed to generate the ULID.
         * @since 1.0.0
         */
        fun generateHashULID(time: Long, string: String) = generateHashULID(time, string.toByteArray(StandardCharsets.UTF_8))
        /**
         * Generates a ULID (Universally Unique Lexicographically Sortable Identifier) instance
         * based on a given time and a hashed representation of the provided byte array.
         * The hash function used is SHA-256, and the first 10 bytes of the hashed output
         * are used as the random component for the ULID.
         *
         * @param time The time component of the ULID. Must fit within 48 bits.
         * @param bytes The input byte array to be hashed. Used to generate the random component of the ULID.
         * @return A ULID instance constructed from the given time and the hashed random component.
         * @throws IllegalArgumentException If the time does not fit in 48 bits or the resulting random component is invalid.
         * @since 1.0.0
         */
        fun generateHashULID(time: Long, bytes: ByteArray) =
            ULID(time, MessageDigest.getInstance("SHA-256").digest(bytes).copyOf(10))

        /**
         * Generates a new ULID based on the current system time and a randomly generated component.
         * The time component ensures that the ULID is lexicographically sortable, while the random component
         * adds uniqueness to ULIDs generated at the same timestamp.
         *
         * @return A newly generated ULID instance combining the current system time and a random component.
         * @since 1.0.0
         */
        fun fast(): ULID {
            val time = System.currentTimeMillis()
            val random = ThreadLocalRandom.current()!!
            return ULID((time shl 16) or (random.nextLong() and 0xffffL), random.nextLong())
        }

        /**
         * Computes a ULID value based on the provided time parameter.
         *
         * @param time The time in milliseconds used to calculate the ULID. The lower 16 bits will be set to zero.
         * @return A ULID generated from the given time parameter.
         * @since 1.0.0
         */
        infix fun min(time: Long) = ULID((time shl 16) or 0x0000L, 0x0000000000000000L)

        /**
         * Generates a ULID (Universally Unique Lexicographically Sortable Identifier) based on the provided time value.
         *
         * The generated ULID uses the given time shifted by 16 bits combined with a constant value.
         *
         * @param time The time value used to generate the ULID. This time is expected to be in milliseconds.
         * @since 1.0.0
         */
        infix fun max(time: Long) = ULID((time shl 16) or 0xffffL, -0x1L)

        /**
         * Converts a [UUID] to a [ULID] representation.
         *
         * The conversion utilizes the `mostSignificantBits` and `leastSignificantBits` of the [UUID]
         * to construct a new [ULID].
         *
         * @receiver A [UUID] instance to be converted.
         * @return A [ULID] instance representing the same underlying 128-bit value as the original [UUID].
         * @since 1.0.0
         */
        fun UUID.toULID() = ULID(this.mostSignificantBits, this.leastSignificantBits)

        /**
         * Creates a ULID instance from a given UUID.
         *
         * @param uuid The UUID whose most significant and least significant bits will be used to construct the ULID.
         * @return A new ULID instance derived from the given UUID.
         * @since 1.0.0
         */
        private fun from(uuid: UUID) = ULID(uuid.mostSignificantBits, uuid.leastSignificantBits)
        /**
         * Constructs a ULID from the given byte array.
         *
         * The input byte array must have a size of 16, representing the 128-bit ULID value,
         * where the first 8 bytes represent the most significant bits and the last 8 bytes
         * represent the least significant bits.
         *
         * @param bytes the input byte array containing the 128-bit ULID value
         * @return a ULID instance created from the given byte array
         * @since 1.0.0
         */
        private fun from(bytes: ByteArray): ULID {
            expect(bytes.size, ULID_BYTES) { "Invalid ULID bytes" }

            var msb: Long = 0
            var lsb: Long = 0

            msb = msb or ((bytes[0x0].toLong() and 0xffL) shl 56)
            msb = msb or ((bytes[0x1].toLong() and 0xffL) shl 48)
            msb = msb or ((bytes[0x2].toLong() and 0xffL) shl 40)
            msb = msb or ((bytes[0x3].toLong() and 0xffL) shl 32)
            msb = msb or ((bytes[0x4].toLong() and 0xffL) shl 24)
            msb = msb or ((bytes[0x5].toLong() and 0xffL) shl 16)
            msb = msb or ((bytes[0x6].toLong() and 0xffL) shl 8)
            msb = msb or (bytes[0x7].toLong() and 0xffL)

            lsb = lsb or ((bytes[0x8].toLong() and 0xffL) shl 56)
            lsb = lsb or ((bytes[0x9].toLong() and 0xffL) shl 48)
            lsb = lsb or ((bytes[0xa].toLong() and 0xffL) shl 40)
            lsb = lsb or ((bytes[0xb].toLong() and 0xffL) shl 32)
            lsb = lsb or ((bytes[0xc].toLong() and 0xffL) shl 24)
            lsb = lsb or ((bytes[0xd].toLong() and 0xffL) shl 16)
            lsb = lsb or ((bytes[0xe].toLong() and 0xffL) shl 8)
            lsb = lsb or (bytes[0xf].toLong() and 0xffL)

            return ULID(msb, lsb)
        }

        /**
         * Converts a given ULID string representation into a ULID object.
         *
         * Parses the provided ULID string and reconstructs its internal representation,
         * including the most significant bits (time) and least significant bits (random components).
         *
         * @param string the string representation of the ULID to be converted. It must be a valid ULID.
         * @return the ULID object constructed from the given string representation.
         * @since 1.0.0
         */
        private fun from(string: String): ULID {
            val chars = toCharArray(string)

            var time: Long = 0
            var random0: Long = 0
            var random1: Long = 0

            time = time or (ALPHABET_VALUES[chars[0x00].code].toLong() shl 45)
            time = time or (ALPHABET_VALUES[chars[0x01].code].toLong() shl 40)
            time = time or (ALPHABET_VALUES[chars[0x02].code].toLong() shl 35)
            time = time or (ALPHABET_VALUES[chars[0x03].code].toLong() shl 30)
            time = time or (ALPHABET_VALUES[chars[0x04].code].toLong() shl 25)
            time = time or (ALPHABET_VALUES[chars[0x05].code].toLong() shl 20)
            time = time or (ALPHABET_VALUES[chars[0x06].code].toLong() shl 15)
            time = time or (ALPHABET_VALUES[chars[0x07].code].toLong() shl 10)
            time = time or (ALPHABET_VALUES[chars[0x08].code].toLong() shl 5)
            time = time or ALPHABET_VALUES[chars[0x09].code].toLong()

            random0 = random0 or (ALPHABET_VALUES[chars[0x0a].code].toLong() shl 35)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0b].code].toLong() shl 30)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0c].code].toLong() shl 25)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0d].code].toLong() shl 20)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0e].code].toLong() shl 15)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0f].code].toLong() shl 10)
            random0 = random0 or (ALPHABET_VALUES[chars[0x10].code].toLong() shl 5)
            random0 = random0 or ALPHABET_VALUES[chars[0x11].code].toLong()

            random1 = random1 or (ALPHABET_VALUES[chars[0x12].code].toLong() shl 35)
            random1 = random1 or (ALPHABET_VALUES[chars[0x13].code].toLong() shl 30)
            random1 = random1 or (ALPHABET_VALUES[chars[0x14].code].toLong() shl 25)
            random1 = random1 or (ALPHABET_VALUES[chars[0x15].code].toLong() shl 20)
            random1 = random1 or (ALPHABET_VALUES[chars[0x16].code].toLong() shl 15)
            random1 = random1 or (ALPHABET_VALUES[chars[0x17].code].toLong() shl 10)
            random1 = random1 or (ALPHABET_VALUES[chars[0x18].code].toLong() shl 5)
            random1 = random1 or ALPHABET_VALUES[chars[0x19].code].toLong()

            val msb = (time shl 16) or (random0 ushr 24)
            val lsb = (random0 shl 40) or (random1 and 0xffffffffffL)

            return ULID(msb, lsb)
        }

        /**
         * Converts the provided string representation into an Instant based on its encoded timestamp.
         *
         * @param string the string representation used to extract the timestamp and convert to an Instant
         * @return the Instant corresponding to the timestamp encoded in the given string
         * @since 1.0.0
         */
        fun getInstant(string: String): Instant = Instant.ofEpochMilli(getTime(string))

        /**
         * Converts a given string representation of a ULID to its corresponding timestamp.
         *
         * @param string The ULID string to convert. Must be a valid ULID representation.
         * @return The extracted timestamp as a `Long` value.
         * @since 1.0.0
         */
        fun getTime(string: String): Long {
            val chars = toCharArray(string)

            var time: Long = 0

            time = time or (ALPHABET_VALUES[chars[0x00].code].toLong() shl 45)
            time = time or (ALPHABET_VALUES[chars[0x01].code].toLong() shl 40)
            time = time or (ALPHABET_VALUES[chars[0x02].code].toLong() shl 35)
            time = time or (ALPHABET_VALUES[chars[0x03].code].toLong() shl 30)
            time = time or (ALPHABET_VALUES[chars[0x04].code].toLong() shl 25)
            time = time or (ALPHABET_VALUES[chars[0x05].code].toLong() shl 20)
            time = time or (ALPHABET_VALUES[chars[0x06].code].toLong() shl 15)
            time = time or (ALPHABET_VALUES[chars[0x07].code].toLong() shl 10)
            time = time or (ALPHABET_VALUES[chars[0x08].code].toLong() shl 5)
            time = time or ALPHABET_VALUES[chars[0x09].code].toLong()

            return time
        }

        /**
         * Generates a random component from the provided string and returns it as a byte array.
         *
         * This method processes a string to extract specific characters, performs bitwise
         * operations using predefined alphabet values, and constructs a byte array
         * representation of the processed random component.
         *
         * @param string The input string used to generate the random component. It must be
         *               valid and fulfill the required format for ULID processing.
         * @return A byte array representing the random component generated from the input string.
         * @since 1.0.0
         */
        fun getRandomComponent(string: String): ByteArray {
            val chars = toCharArray(string)

            var random0: Long = 0
            var random1: Long = 0

            random0 = random0 or (ALPHABET_VALUES[chars[0x0a].code].toLong() shl 35)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0b].code].toLong() shl 30)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0c].code].toLong() shl 25)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0d].code].toLong() shl 20)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0e].code].toLong() shl 15)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0f].code].toLong() shl 10)
            random0 = random0 or (ALPHABET_VALUES[chars[0x10].code].toLong() shl 5)
            random0 = random0 or ALPHABET_VALUES[chars[0x11].code].toLong()

            random1 = random1 or (ALPHABET_VALUES[chars[0x12].code].toLong() shl 35)
            random1 = random1 or (ALPHABET_VALUES[chars[0x13].code].toLong() shl 30)
            random1 = random1 or (ALPHABET_VALUES[chars[0x14].code].toLong() shl 25)
            random1 = random1 or (ALPHABET_VALUES[chars[0x15].code].toLong() shl 20)
            random1 = random1 or (ALPHABET_VALUES[chars[0x16].code].toLong() shl 15)
            random1 = random1 or (ALPHABET_VALUES[chars[0x17].code].toLong() shl 10)
            random1 = random1 or (ALPHABET_VALUES[chars[0x18].code].toLong() shl 5)
            random1 = random1 or ALPHABET_VALUES[chars[0x19].code].toLong()

            val bytes = ByteArray(RANDOM_BYTES)

            bytes[0x0] = (random0 ushr 32).toByte()
            bytes[0x1] = (random0 ushr 24).toByte()
            bytes[0x2] = (random0 ushr 16).toByte()
            bytes[0x3] = (random0 ushr 8).toByte()
            bytes[0x4] = (random0).toByte()

            bytes[0x5] = (random1 ushr 32).toByte()
            bytes[0x6] = (random1 ushr 24).toByte()
            bytes[0x7] = (random1 ushr 16).toByte()
            bytes[0x8] = (random1 ushr 8).toByte()
            bytes[0x9] = (random1).toByte()

            return bytes
        }

        /**
         * Converts the provided string into a character array and validates it.
         *
         * @param string The input string to be converted to a character array.
         * @return A character array representation of the provided string.
         * @throws IllegalArgumentException if the generated character array is invalid according to ULID specification.
         * @since 1.0.0
         */
        fun toCharArray(string: String): CharArray {
            val chars: CharArray = (string.toCharArray())
            validateInputFormat(isValidCharArray(chars)) { String.format("Invalid ULID: \"%s\"", string) }
            return chars
        }

        /**
         * Validates whether a given character array represents a valid ULID encoded string.
         *
         * @param chars The character array to validate as a ULID.
         * @return `true` if the character array is a valid ULID; `false` otherwise.
         * @since 1.0.0
         */
        fun isValidCharArray(chars: CharArray): Boolean {
            if (chars.size != ULID_CHARS) return false

            // The time component has 48 bits.
            // The base32 encoded time component has 50 bits.
            // The time component cannot be greater than than 2^48-1.
            // So the 2 first bits of the base32 decoded time component must be ZERO.
            // As a consequence, the 1st char of the input string must be between 0 and 7.
            if ((ALPHABET_VALUES[chars[0].code].toInt() and 24) != 0) {
                // ULID specification:
                // "Any attempt to decode or encode a ULID larger than this (time > 2^48-1)
                // should be rejected by all implementations, to prevent overflow bugs."
                return false // time overflow!
            }

            for (i in chars.indices) {
                if (ALPHABET_VALUES[chars[i].code].toInt() == -1) {
                    return false // invalid character!
                }
            }

            return true // It seems OK.
        }

        /**
         * Validates the provided string to ensure it conforms to the ULID format.
         * This method internally converts the string to a character array and performs
         * the validation using `isValidCharArray`.
         *
         * @return `true` if the input string is a valid ULID, `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidULID() = isValidCharArray(toString().toCharArray())

        /**
         * Attempts to convert the current [CharSequence] into a [ULID] instance by parsing its string representation.
         *
         * This function wraps the parsing operation in a `Result` object, allowing the caller to handle
         * success and failure scenarios gracefully. The operation will succeed if the [CharSequence]
         * contains a valid ULID string; otherwise, it will fail with an exception.
         *
         * @receiver The [CharSequence] representing the potential ULID.
         * @return A [Result] containing the parsed [ULID] if successful, or a failure if the parsing fails.
         * @since 1.0.0
         */
        fun CharSequence.toULID() = runCatching { from(toString()) }

        class Serializer : ValueSerializer<ULID>() {
            override fun serialize(value: ULID, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<ULID>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = from(p.string)
        }

        class OldSerializer : JsonSerializer<ULID>() {
            override fun serialize(value: ULID, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.toString())
        }

        class OldDeserializer : JsonDeserializer<ULID>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): ULID = from(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<ULID?, String?> {
            override fun convertToDatabaseColumn(attribute: ULID?): String? = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?): ULID? = dbData?.let { from(it) }
        }

        class TypeChar : EnhancedUserType<ULID> {
            override fun getSqlType(): Int = SqlTypes.CHAR

            override fun returnedClass(): Class<ULID> = ULID::class.java

            override fun equals(
                x: ULID?,
                y: ULID?
            ): Boolean = x == y

            override fun hashCode(x: ULID?): Int = x?.hashCode() ?: 0

            override fun nullSafeGet(
                rs: ResultSet?,
                position: Int,
                session: SharedSessionContractImplementor?,
                owner: Any?
            ): ULID? {
                val value = rs?.getString(position) ?: return null
                return ULID(value)
            }

            override fun nullSafeSet(
                st: PreparedStatement?,
                value: ULID?,
                index: Int,
                session: SharedSessionContractImplementor?
            ) {
                st?.setString(index, value?.toString()) ?: throw IllegalArgumentException("Statement cannot be null")
            }

            override fun deepCopy(value: ULID?): ULID? = value?.let { ULID(it) }

            override fun isMutable(): Boolean = false

            override fun disassemble(value: ULID?): Serializable? = deepCopy(value)

            override fun assemble(
                cached: Serializable?,
                owner: Any?
            ): ULID? = cached as? ULID

            override fun toSqlLiteral(value: ULID?): String? = value?.let { "'${it}'" }

            override fun toString(value: ULID?): String? = value?.toString()

            override fun fromStringValue(sequence: CharSequence?): ULID =
                sequence?.let { ULID(it.toString()) } ?: throw IllegalArgumentException("Cannot convert null to ULID")
        }

        class TypeBytea : EnhancedUserType<ULID> {
            override fun getSqlType(): Int = SqlTypes.VARBINARY

            override fun returnedClass(): Class<ULID> = ULID::class.java

            override fun equals(
                x: ULID?,
                y: ULID?
            ): Boolean = x == y

            override fun hashCode(x: ULID?): Int = x?.hashCode() ?: 0

            override fun nullSafeGet(
                rs: ResultSet?,
                position: Int,
                session: SharedSessionContractImplementor?,
                owner: Any?
            ): ULID? {
                val bytes = rs?.getBytes(position) ?: return null
                return ULID(bytes = bytes)
            }

            override fun nullSafeSet(
                st: PreparedStatement?,
                value: ULID?,
                index: Int,
                session: SharedSessionContractImplementor?
            ) {
                if (st == null) throw IllegalArgumentException("Statement cannot be null")
                if (value != null) {
                    st.setBytes(index, value.toByteArray())
                } else {
                    st.setNull(index, SqlTypes.VARBINARY)
                }
            }

            override fun deepCopy(value: ULID?): ULID? = value?.let { ULID(it) }

            override fun isMutable(): Boolean = false

            override fun disassemble(value: ULID?): Serializable? = value?.toByteArray()

            override fun assemble(
                cached: Serializable?,
                owner: Any?
            ): ULID? = (cached as? ByteArray)?.let { ULID(bytes = it) }

            override fun toSqlLiteral(value: ULID?): String? = value?.let { "E'\\\\x${it.toHex()}'" }

            override fun toString(value: ULID?): String? = value?.toString()

            override fun fromStringValue(sequence: CharSequence?): ULID =
                sequence?.let { ULID(it.toString()) } ?: throw IllegalArgumentException("Cannot convert null to ULID")
        }
    }

    /**
     * Converts the current ULID instance to a UUID representation.
     *
     * This method combines the most significant bits and least significant bits
     * of the ULID to form a standard `java.util.UUID` object. The resulting UUID
     * maintains compatibility with systems or APIs that accept UUIDs.
     *
     * @return A `UUID` instance representing the same value as this ULID.
     * @since 1.0.0
     */
    fun toUUID() = UUID(mostSignificantBits, leastSignificantBits)

    /**
     * Converts the ULID to a byte array representation, with the most significant bits and least significant bits
     * stored in a sequential order.
     *
     * @return A 16-byte array where the first 8 bytes represent the most significant bits and
     * the last 8 bytes represent the least significant bits of the ULID.
     * @since 1.0.0
     */
    fun toByteArray(): ByteArray {
        val bytes = ByteArray(ULID_BYTES)

        bytes[0x0] = (mostSignificantBits ushr 56).toByte()
        bytes[0x1] = (mostSignificantBits ushr 48).toByte()
        bytes[0x2] = (mostSignificantBits ushr 40).toByte()
        bytes[0x3] = (mostSignificantBits ushr 32).toByte()
        bytes[0x4] = (mostSignificantBits ushr 24).toByte()
        bytes[0x5] = (mostSignificantBits ushr 16).toByte()
        bytes[0x6] = (mostSignificantBits ushr 8).toByte()
        bytes[0x7] = (mostSignificantBits).toByte()

        bytes[0x8] = (leastSignificantBits ushr 56).toByte()
        bytes[0x9] = (leastSignificantBits ushr 48).toByte()
        bytes[0xa] = (leastSignificantBits ushr 40).toByte()
        bytes[0xb] = (leastSignificantBits ushr 32).toByte()
        bytes[0xc] = (leastSignificantBits ushr 24).toByte()
        bytes[0xd] = (leastSignificantBits ushr 16).toByte()
        bytes[0xe] = (leastSignificantBits ushr 8).toByte()
        bytes[0xf] = (leastSignificantBits).toByte()

        return bytes
    }

    /**
     * Returns the string representation of this ULID using the default uppercase alphabet.
     *
     * This method overrides the base `toString` function to provide a ULID-specific implementation.
     * The resulting string is generated using the predefined uppercase character alphabet.
     *
     * @return A string representation of the ULID.
     * @since 1.0.0
     */
    override fun toString() = toString(ALPHABET_UPPERCASE)

    /**
     * Converts the ULID instance into its string representation using the provided alphabet.
     *
     * The string representation is generated by mapping each segment of the ULID's
     * internal bit representation into characters from the specified alphabet.
     *
     * @param alphabet A character array used as the mapping for the string representation.
     * The alphabet must contain exactly 32 unique characters.
     * @return A string representation of the ULID using the provided character alphabet.
     * @since 1.0.0
     */
    fun toString(alphabet: CharArray): String {
        val chars = CharArray(ULID_CHARS)

        val time: Long = mostSignificantBits ushr 16
        val random0: Long = ((mostSignificantBits and 0xffffL) shl 24) or (leastSignificantBits ushr 40)
        val random1: Long = leastSignificantBits and 0xffffffffffL

        chars[0x00] = alphabet[(time ushr 45 and 31L).toInt()]
        chars[0x01] = alphabet[(time ushr 40 and 31L).toInt()]
        chars[0x02] = alphabet[(time ushr 35 and 31L).toInt()]
        chars[0x03] = alphabet[(time ushr 30 and 31L).toInt()]
        chars[0x04] = alphabet[(time ushr 25 and 31L).toInt()]
        chars[0x05] = alphabet[(time ushr 20 and 31L).toInt()]
        chars[0x06] = alphabet[(time ushr 15 and 31L).toInt()]
        chars[0x07] = alphabet[(time ushr 10 and 31L).toInt()]
        chars[0x08] = alphabet[(time ushr 5 and 31L).toInt()]
        chars[0x09] = alphabet[(time and 31L).toInt()]

        chars[0x0a] = alphabet[(random0 ushr 35 and 31L).toInt()]
        chars[0x0b] = alphabet[(random0 ushr 30 and 31L).toInt()]
        chars[0x0c] = alphabet[(random0 ushr 25 and 31L).toInt()]
        chars[0x0d] = alphabet[(random0 ushr 20 and 31L).toInt()]
        chars[0x0e] = alphabet[(random0 ushr 15 and 31L).toInt()]
        chars[0x0f] = alphabet[(random0 ushr 10 and 31L).toInt()]
        chars[0x10] = alphabet[(random0 ushr 5 and 31L).toInt()]
        chars[0x11] = alphabet[(random0 and 31L).toInt()]

        chars[0x12] = alphabet[(random1 ushr 35 and 31L).toInt()]
        chars[0x13] = alphabet[(random1 ushr 30 and 31L).toInt()]
        chars[0x14] = alphabet[(random1 ushr 25 and 31L).toInt()]
        chars[0x15] = alphabet[(random1 ushr 20 and 31L).toInt()]
        chars[0x16] = alphabet[(random1 ushr 15 and 31L).toInt()]
        chars[0x17] = alphabet[(random1 ushr 10 and 31L).toInt()]
        chars[0x18] = alphabet[(random1 ushr 5 and 31L).toInt()]
        chars[0x19] = alphabet[(random1 and 31L).toInt()]

        return chars.joinToString("")
    }

    /**
     * Converts the ULID to its string representation using a lowercase alphabet.
     *
     * This method generates a string representation of the ULID using an alphabet of lowercase characters.
     * It is useful when a lowercase format of the ULID is required for specific use cases such as
     * case-sensitive environments or when following specific format conventions.
     *
     * @return A string containing the lowercase representation of the ULID.
     * @since 1.0.0
     */
    fun lowercase() = toString(ALPHABET_LOWERCASE)

    /**
     * Converts the current ULID to a format compliant with RFC 4122 version 4.
     *
     * This method transforms the internal bit representation of the ULID to conform
     * with the version 4 (random) specification of the Universally Unique Identifier (UUID),
     * as defined in RFC 4122. Specifically, it adjusts the most and least significant bits
     * to encode the version and variant according to the standard.
     *
     * @return A new ULID instance that adheres to the RFC 4122 version 4 specification.
     * @since 1.0.0
     */
    fun toRFC4122(): ULID {
        // set the 4 most significant bits of the 7th byte to 0, 1, 0 and 0
        val msb4: Long = (mostSignificantBits and -0xf001L) or 0x0000000000004000L // RFC-4122 version 4
        // set the 2 most significant bits of the 9th byte to 1 and 0
        val lsb4: Long = (leastSignificantBits and 0x3fffffffffffffffL) or Long.MIN_VALUE // RFC-4122 variant 2

        return ULID(msb4, lsb4)
    }

    /**
     * Increments the ULID value by 1.
     * The least significant bits are incremented first. If they overflow,
     * the most significant bits are incremented to account for the overflow.
     *
     * @return A new ULID instance with an incremented value.
     * @since 1.0.0
     */
    operator fun inc(): ULID {
        var newMsb: Long = mostSignificantBits
        val newLsb: Long = leastSignificantBits + 1 // increment the LEAST significant bits

        if (newLsb == INCREMENT_OVERFLOW) {
            newMsb += 1 // increment the MOST significant bits
        }

        return ULID(newMsb, newLsb)
    }

    /**
     * Compares this instance with the specified object to determine equality.
     *
     * @param other The object to compare with this instance.
     * @return `true` if the specified object is equal to this instance, otherwise `false`.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (other.isNull()) return false
        if (other.javaClass != ULID::class.java) return false

        val that = other as ULID
        if (leastSignificantBits != that.leastSignificantBits) return false
        else if (mostSignificantBits != that.mostSignificantBits) return false
        return true
    }

    /**
     * Computes the hash code for this ULID instance.
     *
     * @return An integer representing the hash code, derived from the XOR of
     * the most significant and least significant bits, further processed for
     * even distribution.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        val bits: Long = mostSignificantBits xor leastSignificantBits
        return (bits xor (bits ushr 32)).toInt()
    }

    /**
     * Compares this ULID instance with another ULID instance for order.
     *
     * @param other the ULID instance to be compared with.
     * @return a negative integer, zero, or a positive integer as this ULID is less than,
     * equal to, or greater than the specified ULID.
     * @since 1.0.0
     * @author Tommaso Pastorelli
     */
    override operator fun compareTo(other: ULID): Int {
        // used to compare as UNSIGNED longs
        val min: Long = Long.MIN_VALUE

        val a: Long = mostSignificantBits + min
        val b: Long = other.mostSignificantBits + min

        if (a > b) return 1
        else if (a < b) return -1

        val c: Long = leastSignificantBits + min
        val d: Long = other.leastSignificantBits + min

        if (c > d) return 1
        else if (c < d) return -1

        return 0
    }

    /**
     * Converts the ULID into its hexadecimal string representation.
     *
     * This method utilizes the `toByteArray` function to retrieve the byte representation
     * of the ULID and then transforms it into a hexadecimal string using the `toHex` function.
     *
     * @return A Hex instance containing the hexadecimal representation of the ULID.
     * @since 1.0.0
     */
    fun toHex() = toByteArray().toHex()

    /**
     * Represents a Factory class for generating ULIDs.
     * Provides methods to create ULID instances with default or customized behavior
     * using different randomness strategies and clock sources.
     *
     * This class allows for monotonic and non-monotonic ULID generation.
     * It is thread-safe for ULID creation.
     *
     * @constructor
     * Primary constructor allowing custom ULID function and clock.
     * Allows for fine-grained customization of ULID generation.
     *
     * @since 1.0.0
     * @author Tommaso Pastorelli
     */
    class Factory(
        private val ulidTransformer: Transformer<Long, ULID>,
        private val clock: Clock = Clock.systemUTC()
    ) {
        /**
         * Constructs a Factory instance with the specified ULID generation function
         * and a default clock set to the UTC system clock.
         *
         * @param ulidTransformer A function that generates ULID values based on the provided time in milliseconds.
         * @since 1.0.0
         */
        constructor(ulidTransformer: Transformer<Long, ULID>) : this(ulidTransformer, Clock.systemUTC())
        /**
         * Secondary constructor for the `Factory` class.
         * Initializes the `ulidFunction` field with an instance of `ULIDFunction` using a random generator.
         * Initializes the `clock` field with the system's UTC clock.
         *
         * @since 1.0.0
         */
        constructor() : this(
            ULIDFunction(IRandom.newInstance()),
            Clock.systemUTC()
        )

        companion object {
            /**
             * Creates a new instance of the `Factory` class using a default ULID generation strategy.
             *
             * This method initializes the `Factory` with a `ULIDFunction` constructed using a new instance of `IRandom`.
             * The `ULIDFunction` encapsulates the logic for generating ULIDs based on a given timestamp and a random component.
             *
             * @return a new instance of the `Factory` class.
             * @since 1.0.0
             */
            fun newInstance() = Factory(ULIDFunction(IRandom.newInstance()))

            /**
             * Creates a new instance of `Factory` with a ULID function based on the provided random generator.
             *
             * @param random An optional `Random` instance used for generating ULID values. If `null`, a default implementation will be used.
             * @since 1.0.0
             */
            fun newInstance(random: Random?) = Factory(ULIDFunction(IRandom.newInstance(random)))

            /**
             * Creates a new instance of `Factory` using the provided random function.
             *
             * @param randomFunction A function that generates random `Long` values. This is used to initialize
             * the `ULIDFunction` internally, which generates ULIDs based on the provided randomness.
             * @since 1.0.0
             */
            fun newInstance(randomFunction: Supplier<Long>) = Factory(ULIDFunction(IRandom.newInstance(randomFunction)))

            /**
             * Creates a new instance of `Factory` using the provided random byte array generating function.
             *
             * @param randomTransformer A function that takes an integer as input, representing the requested
             * number of bytes, and returns a ByteArray of that length to be used for random data generation.
             * @return A new instance of the `Factory` class configured with a `ULIDFunction` that utilizes
             * the specified random byte array function.
             * @since 1.0.0
             */
            fun newInstance(randomTransformer: Transformer<Int, ByteArray>) = Factory(ULIDFunction(IRandom.newInstance(randomTransformer)))

            /**
             * Creates a new instance of `Factory` with a monotonic ULID generation function.
             * The monotonic function ensures ULIDs generated are strictly ordered even
             * in cases where the system clock moves backwards or remains constant.
             *
             * The generated factory uses a monotonic function based on the current system clock
             * and introduces randomness for unique ULIDs.
             *
             * @return A new instance of `Factory` configured with a monotonic ULID function.
             * @since 1.0.0
             */
            fun newMonotonicInstance() = Factory(MonotonicFunction(IRandom.newInstance()))

            /**
             * Creates a new instance of `Factory` with a `ULIDFunction` that uses a specific random number generator.
             *
             * @param random An optional `Random` instance that will be used to seed the `ULIDFunction`.
             *               If `null`, a default random number generator will be used.
             * @return A new instance of `Factory` configured with a monotonic `ULIDFunction`.
             * @since 1.0.0
             */
            fun newMonotonicInstance(random: Random?) = Factory(ULIDFunction(IRandom.newInstance(random)))

            /**
             * Creates a new monotonic instance of the `Factory` class using the specified random function.
             * The provided random function is used to generate random values required for creating ULIDs.
             *
             * @param randomFunction A function that generates random `Long` values for ULID creation.
             * @return A new instance of `Factory` configured with a monotonic ULID function.
             * @since 1.0.0
             */
            fun newMonotonicInstance(randomFunction: Supplier<Long>) = Factory(ULIDFunction(IRandom.newInstance(randomFunction)))

            /**
             * Creates a new instance of `Factory` using a monotonic ULID generator.
             *
             * @param randomTransformer A function that generates a `ByteArray` of the specified size.
             *                        This is used as the source of random values for ULID generation.
             * @return A new instance of `Factory` configured with a monotonic ULID generator.
             * @since 1.0.0
             */
            fun newMonotonicInstance(randomTransformer: Transformer<Int, ByteArray>) = Factory(ULIDFunction(IRandom.newInstance(randomTransformer)))

            /**
             * Creates a new instance of the `Factory` class configured with a monotonic function.
             *
             * @param randomFunction A lambda producing random `Long` values, utilized to create a new instance
             * of `IRandom` for the internal monotonic function.
             * @param clock The `Clock` instance to provide timestamps for the monotonic function.
             * @return A `Factory` instance configured with a monotonic ULID generation function and clock.
             * @since 1.0.0
             * */
            fun newMonotonicInstance(randomFunction: Supplier<Long>, clock: Clock): Factory {
                return Factory(
                    MonotonicFunction(
                        IRandom.newInstance(randomFunction),
                        clock
                    ), clock
                )
            }

            /**
             * Creates a new instance of the `Factory` class with a monotonic function
             * based on the provided random function and clock.
             *
             * @param randomTransformer A function that generates a `ByteArray` of the specified size.
             * @param clock The clock instance used for time-based functionality.
             * @return A new `Factory` instance configured with a `MonotonicFunction` and the specified clock.
             * @since 1.0.0
             */
            fun newMonotonicInstance(randomTransformer: Transformer<Int, ByteArray>, clock: Clock): Factory {
                return Factory(
                    MonotonicFunction(
                        IRandom.newInstance(randomTransformer),
                        clock
                    ), clock
                )
            }
        }

        /**
         * Generates a ULID (Universally Unique Lexicographically Sortable Identifier) using the current timestamp obtained
         * from the specified clock instance.
         *
         * This method is synchronized to ensure thread-safe execution when invoked in a multithreaded environment.
         *
         * @return A ULID generated based on the current timestamp.
         * @throws IllegalStateException If the clock instance is not initialized.
         * @since 1.0.0
         */
        @Synchronized
        fun create() = ulidTransformer(clock.millis())

        /**
         * Creates a ULID (Universally Unique Lexicographically Sortable Identifier) for the provided time.
         * This is a thread-safe method to generate the identifier based on the given timestamp.
         *
         * @param time The timestamp in milliseconds to generate the ULID.
         * @return The generated ULID as a string.
         * @since 1.0.0
         */
        @Synchronized
        fun create(time: Long) = ulidTransformer(time)

        /**
         * ULIDFunction is a function-like class that generates unique lexicographically sortable identifiers (ULIDs).
         * It uses a given random number generator (`IRandom`) for producing random components.
         * This class implements the `(Long) -> ULID` functional type, allowing it to be invoked with a timestamp to generate a ULID.
         *
         * @property random The random generator used to produce random components of the ULID.
         * @constructor Creates an instance of ULIDFunction using the specified random generator.
         *
         * @since 1.0.0
         * @author Tommaso Pastorelli
         */
        internal class ULIDFunction(private val random: IRandom) : Transformer<Long, ULID> {
            /**
             * Generates a ULID (Universally Unique Lexicographically Sortable Identifier) based on the provided time and random data.
             *
             * @param time The timestamp in milliseconds used to generate the ULID.
             * @return A new ULID instance generated using the specified time and random data.
             * @since 1.0.0
             */
            override fun invoke(time: Long): ULID {
                if (random is ByteRandom) {
                    return ULID(time, random.nextBytes(RANDOM_BYTES))
                }
                val msb: Long = (time shl 16) or (random.nextLong() and 0xffffL)
                val lsb: Long = random.nextLong()
                return ULID(msb, lsb)
            }
        }

        /**
         * Represents a monotonic ULID generator function which produces ULIDs with increasing monotonicity
         * even in the presence of slight clock drift or identical timestamp inputs. This is particularly
         * useful in systems where generation order or uniqueness is crucial.
         *
         * The monotonic function ensures that, for a given timestamp, any newly generated ULID is greater
         * than the last one and maintains the ULID standard constraints.
         *
         * Instances of this class should typically be created using provided constructors.
         *
         * @constructor Creates an instance of the monotonic function with a given random generator
         * and clock. This ensures determinism and supports specific use cases around time-based ULID generation.
         *
         * @since 1.0.0
         * @author Tommaso Pastorelli
         */
        internal open class MonotonicFunction private constructor(private val lastULID: ULID, private val random: IRandom) :
            Transformer<Long, ULID> {
            /**
             * Constructs a new instance of the MonotonicFunction class using the given random generator and clock.
             *
             * This constructor initializes the MonotonicFunction with a ULID generated based on the current
             * time provided by the clock and a random byte array provided by the random generator.
             * The ULID serves as the base identifier upon which monotonicity constraints will be applied.
             *
             * @param random An instance of the IRandom interface used to generate random bytes for the ULID.
             * @param clock  A Clock instance used to provide the current time for the ULID generation.
             * @since 1.0.0
             */
            constructor(random: IRandom, clock: Clock) : this(ULID(clock.millis(), random.nextBytes(RANDOM_BYTES)), random)
            /**
             * Constructs a `MonotonicFunction` instance using the provided random number generator.
             * The clock used for timestamp generation defaults to the system UTC clock.
             *
             * @param random The random number generator implementation used for `ULID` generation.
             * @since 1.0.0
             */
            constructor(random: IRandom) : this(random, Clock.systemUTC())

            /**
             * Generates a new ULID (Universally Unique Lexicographically Sortable Identifier) based on
             * the provided time parameter. It ensures monotonicity by incrementing the ULID if the
             * given time is within the clock drift tolerance or has moved backward.
             *
             * @param time The timestamp used for generating a new ULID. It should be provided in milliseconds.
             * @return A newly generated ULID instance that preserves monotonicity.
             * @since 1.0.0
             */
            @Suppress("localVariableName")
            override fun invoke(time: Long): ULID {
                val lastTime: Long = lastULID.timestamp
                var _lastULID = lastULID

                // Check if the current time is the same as the previous time or has moved
                // backwards after a small system clock adjustment or after a leap second.
                // Drift tolerance = (previous_time - 10s) < current_time <= previous_time
                if ((time > lastTime - CLOCK_DRIFT_TOLERANCE) && (time <= lastTime)) {
                    _lastULID = _lastULID.inc()
                } else {
                    if (random is ByteRandom) {
                        _lastULID = ULID(time, random.nextBytes(RANDOM_BYTES))
                    } else {
                        val msb = (time shl 16) or (random.nextLong() and 0xffffL)
                        val lsb: Long = random.nextLong()
                       _lastULID = ULID(msb, lsb)
                    }
                }

                return ULID(_lastULID)
            }

            companion object {
                /**
                 * Defines the maximum allowable clock drift tolerance in milliseconds for system clock adjustments
                 * or leap seconds when generating ULIDs in the `MonotonicFunction` class.
                 *
                 * This constant represents how much the system clock is permitted to drift backward (i.e.,
                 * discrepancies between consecutive timestamps) without causing issues in the sequential nature
                 * of ULIDs. If a timestamp falls within the drift tolerance range, a monotonic increment is applied
                 * to ensure ULID uniqueness.
                 *
                 * A typical use case for this value is handling scenarios such as minor clock adjustments or leap
                 * seconds during operations that require unique, time-ordered identifiers.
                 *
                 * @since 1.0.0
                 */
                protected const val CLOCK_DRIFT_TOLERANCE = 10000
            }
        }

        /**
         * Interface for generating random values.
         * Provides methods to generate random `Long` values and random byte arrays.
         *
         * @since 1.0.0
         * @author Tommaso Pastorelli
         */
        internal interface IRandom {
            /**
             * Generates the next random long value based on the implementation's random number generation strategy.
             *
             * @return The generated random long value.
             * @since 1.0.0
             */
            fun nextLong(): Long

            /**
             * Generates a new ByteArray with the specified length, filled with random byte values.
             *
             * @param length the number of random bytes to generate.
             * @return a ByteArray containing random byte values.
             * @since 1.0.0
             */
            fun nextBytes(length: Int): ByteArray

            companion object {
                /**
                 * Creates a new instance of the `IRandom` implementation.
                 *
                 * @return A concrete implementation of `IRandom`. By default, this will be an instance of `ByteRandom`.
                 * @since 1.0.0
                 */
                fun newInstance(): IRandom = ByteRandom()

                /**
                 * Creates a new instance of the IRandom implementation based on the provided Random instance.
                 *
                 * If the provided Random instance is null, a default `ByteRandom` instance is created.
                 * If the provided Random instance is an instance of `SecureRandom`, a `ByteRandom` instance wrapping it is created.
                 * Otherwise, a `LongRandom` instance wrapping the provided Random instance is returned.
                 *
                 * @param random An optional Random instance. If null, a default `ByteRandom` is created.
                 * @return A new instance of an implementation of the IRandom interface.
                 * @since 1.0.0
                 */
                fun newInstance(random: Random?): IRandom {
                    return if (random.isNull()) ByteRandom() else {
                        if (random is SecureRandom) ByteRandom(random)
                        else LongRandom(random)
                    }
                }

                /**
                 * Creates a new instance of `LongRandom` using the specified random function.
                 *
                 * @param randomFunction A lambda function that generates a `Long` value.
                 * This function is used as the source of randomness for the created `LongRandom` instance.
                 * @since 1.0.0
                 */
                fun newInstance(randomFunction: Supplier<Long>) = LongRandom(randomFunction)

                /**
                 * Creates a new instance of `ByteRandom` using the specified byte array generation function.
                 *
                 * @param randomTransformer A function that generates a `ByteArray` of the specified length.
                 * It serves as the source of randomness for the generated object.
                 * @return A new `ByteRandom` instance utilizing the provided `randomFunction`.
                 * @since 1.0.0
                 */
                fun newInstance(randomTransformer: Transformer<Int, ByteArray>) = ByteRandom(randomTransformer)
            }
        }

        /**
         * A random number generator implementation based on a long-producing function.
         * This class generates random long values or random bytes of specified length
         * using the provided or default random function.
         *
         * @property randomFunction A lambda function that produces random long values.
         * By default, it is initialized using `newRandomFunction`.
         *
         * @constructor Creates an instance of [LongRandom] using the provided random function.
         * If no random is provided, the default implementation uses `SecureRandom`.
         *
         * @constructor Overloaded constructor that allows initializing the [LongRandom]
         * with a [Random] instance.
         *
         * @since 1.0.0
         * @author Tommaso Pastorelli
         */
        internal class LongRandom (private val randomFunction: Supplier<Long> = newRandomFunction(null)) : IRandom {
            /**
             * Secondary constructor for the `LongRandom` class.
             * Allows initialization of the `LongRandom` instance with a custom `Random` instance.
             * Delegates to the primary constructor by utilizing the `newRandomFunction` utility
             * to generate a lambda function for producing random `Long` values.
             *
             * @param random An optional `Random` instance to serve as the source of entropy.
             *               If null, a default `SecureRandom` instance is used.
             *
             * @since 1.0.0
             */
            constructor(random: Random?) : this(newRandomFunction(random))

            /**
             * Generates the next pseudorandom long value.
             *
             * This method produces a random 64-bit signed integer value,
             * based on the underlying random function provided to the instance.
             *
             * @return A random `Long` value.
             * @since 1.0.0
             */
            override fun nextLong() = randomFunction()

            /**
             * Generates a random byte array of the specified length.
             *
             * @param length the number of random bytes to generate.
             * @return a ByteArray containing the randomly generated bytes.
             * @since 1.0.0
             */
            override fun nextBytes(length: Int): ByteArray {
                var shift = 0
                var random: Long = 0
                val bytes = ByteArray(length)

                for (i in 0..<length) {
                    if (shift < Byte.SIZE) {
                        shift = java.lang.Long.SIZE
                        random = randomFunction()
                    }
                    shift -= Byte.SIZE // 56, 48, 40...
                    bytes[i] = (random ushr shift).toByte()
                }

                return bytes
            }

            companion object {
                /**
                 * Creates a function that generates random `Long` values.
                 *
                 * The function uses the provided `Random` instance to generate the values. If the provided
                 * `Random` instance is `null`, a `SecureRandom` instance is used instead.
                 *
                 * @param random the `Random` instance used to generate random values, or `null` to use a default `SecureRandom` instance.
                 * @return a function that generates random `Long` values.
                 * @since 1.0.0
                 */
                fun newRandomFunction(random: Random?): Supplier<Long> {
                    val entropy = random ?: SecureRandom()
                    return entropy::nextLong
                }
            }
        }

        /**
         * Represents a random byte generator implementation of the [IRandom] interface.
         * Utilizes a customizable byte array generating function to provide
         * random data based on given input parameters.
         *
         * @property randomTransformer A function that generates a random [ByteArray] given a specified length.
         * By default, this uses a secure random function created by [newRandomFunction].
         *
         * @since 1.0.0
         * @author Tommaso Pastorelli
         */
        internal class ByteRandom(private val randomTransformer: Transformer<Int, ByteArray> = newRandomFunction(null)) : IRandom {
            /**
             * Constructs a new instance of ByteRandom by initializing the internal random function with
             * a specific implementation based on the provided Random instance.
             *
             * @param random an optional Random instance to initialize the random function; if null, a SecureRandom instance is used.
             * @since 1.0.0
             */
            constructor(random: Random?) : this(newRandomFunction(random))

            /**
             * Generates the next random `Long` value using a byte array derived from the provided random function.
             *
             * This method converts a sequence of randomly generated bytes into a `Long` value. Each byte
             * contributes to the construction of the final value by shifting and combining bits.
             *
             * @return A random `Long` value generated from the internal random byte function.
             * @since 1.0.0
             */
            override fun nextLong(): Long {
                var number: Long = 0
                val bytes = randomTransformer(java.lang.Long.BYTES)
                for (i in 0..<java.lang.Long.BYTES) {
                    number = (number shl 8) or (bytes[i].toInt() and 0xff).toLong()
                }
                return number
            }

            /**
             * Generates a random byte array of the specified length.
             *
             * @param length The number of random bytes to generate. Must be a non-negative integer.
             * @since 1.0.0
             */
            override fun nextBytes(length: Int) = randomTransformer(length)

            companion object {
                /**
                 * Creates a function that generates a byte array of the specified length filled with random data.
                 * If no `Random` instance is provided, a `SecureRandom` instance will be used.
                 *
                 * @param random an optional `Random` instance to use for generating random bytes, or `null`
                 *               to use a `SecureRandom` instance.
                 * @return a function that takes an `Int` input representing the desired length of the byte array
                 *         and returns a `ByteArray` of that length filled with random data.
                 * @since 1.0.0
                 */
                fun newRandomFunction(random: Random?): Transformer<Int, ByteArray> {
                    val entropy = random ?: SecureRandom()
                    return { length: Int ->
                        val bytes = ByteArray(length)
                        entropy.nextBytes(bytes)
                        bytes
                    }
                }
            }
        }

        /**
         * A holder class providing a singleton instance of the `Factory` object.
         *
         * This class internally manages the lifecycle of the `Factory` object by calling `newInstance`
         * to create the singleton.
         *
         * It ensures thread-safe access to the instance of `Factory`.
         *
         * @since 1.0.0
         * @author Tommaso Pastorelli
         */
        internal class Holder {
            companion object {
                /**
                 * Singleton instance of the `Factory`, generated using the `newInstance` method.
                 * Provides a thread-safe mechanism to access the shared `Factory` instance
                 * initialized with an internal ULID generation function and a random number generator.
                 *
                 * @since 1.0.0
                 */
                val INSTANCE = newInstance()
            }
        }

        /**
         * A helper class that provides a singleton instance of a monotonic factory.
         *
         * This class is intended to manage and hold a singleton instance that ensures monotonicity
         * during its operations, particularly for use in generating ULIDs or managing related clock
         * operations.
         *
         * @since 1.0.0
         * @author Tommaso Pastorelli
         */
        internal class MonotonicHolder {
            companion object {
                /**
                 * A singleton instance of a monotonic object, used to maintain a consistent monotonic function behavior.
                 * This instance is internally instantiated using the `newMonotonicInstance` function.
                 *
                 * @since 1.0.0
                 */
                val INSTANCE = newMonotonicInstance()
            }
        }
    }
}