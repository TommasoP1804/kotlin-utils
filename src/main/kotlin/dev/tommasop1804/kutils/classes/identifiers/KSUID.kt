package dev.tommasop1804.kutils.classes.identifiers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.Instant
import dev.tommasop1804.kutils.Supplier
import dev.tommasop1804.kutils.classes.base.Base62
import dev.tommasop1804.kutils.classes.numbers.Hex
import dev.tommasop1804.kutils.classes.numbers.Hex.Companion.toHex
import dev.tommasop1804.kutils.validate
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
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.Instant
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

/**
 * Represents a KSUID (K-Sortable Unique Identifier), which is a type of unique identifier
 * that is chronologically sortable.
 *
 * This class provides utilities for handling KSUIDs, including methods for converting
 * them to various representations like byte arrays, hexadecimal strings, and
 * Base62-encoded strings. It also includes methods for comparing and checking equality
 * with other KSUID instances.
 *
 * @property timestamp The timestamp component of the KSUID, typically used for
 * chronological ordering.
 * @property payload The payload component of the KSUID, representing additional randomly
 * generated data or a specific unique identifier.
 * @property ksuidBytes The raw byte array representation of the KSUID.
 * @property instant The timestamp represented as an Instant, allowing interoperability
 * with standard temporal and date-time APIs.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = KSUID.Companion.Serializer::class)
@JsonDeserialize(using = KSUID.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = KSUID.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = KSUID.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_null_check")
class KSUID(timestamp: Int? = null, payload: ByteArray? = null, ksuidBytes: ByteArray? = null) : Comparable<KSUID>, Serializable {

    /**
     * Represents the timestamp component of a KSUID (K-Sortable Unique Identifier).
     * The value is stored as a 32-bit unsigned integer representing the number of seconds
     * since the custom epoch of January 1, 2014, 00:00:00 UTC.
     *
     * This property is backed by a delegate and must be set before use. It is initialized
     * at object creation and can only be modified within the containing class.
     *
     * @since 1.0.0
     */
    val timestamp: Int
    /**
     * The payload portion of the KSUID, represented as a 16-byte array.
     * This value is immutable and is set internally on object creation.
     *
     * @since 1.0.0
     */
    val payload: ByteArray
    /**
     * Represents the internal byte array of the KSUID (K-Sortable Unique Identifier),
     * combining the timestamp and payload data to form the full identifier in its binary format.
     * This property is used to access or manipulate the raw byte representation of the KSUID.
     *
     * The value of this property is immutable from outside the class, ensuring the integrity
     * of the KSUID structure after it has been created.
     *
     * @since 1.0.0
     */
    val ksuidBytes: ByteArray

    /**
     * Represents the `Instant` corresponding to the timestamp
     * derived from this KSUID. The instant is calculated by
     * adding the KSUID-specific epoch offset to the timestamp.
     *
     * This allows the KSUID to be interpreted in terms of
     * standard temporal representations.
     *
     * @since 1.0.0
     */
    val instant: Instant
        get() = Instant.ofEpochSecond(timestamp.toLong() + EPOCH)

    /**
     * Constructs a KSUID instance by delegating to another constructor
     * with a byte array representation of the provided `ksuid` argument.
     *
     * @param ksuid The KSUID instance used to initialize this constructor.
     * @since 1.0.0
     */
    private constructor(ksuid: KSUID) : this(ksuidBytes = ksuid.ksuidBytes)
    /**
     * Constructs a new KSUID instance with the specified parameters.
     *
     * This constructor allows partial initialization of the KSUID object by
     * accepting a nullable integer timestamp, a mandatory hexadecimal payload,
     * and an optional byte array representation of the KSUID. If provided, the
     * payload is converted to its byte array representation.
     *
     * @param timestamp The optional timestamp associated with the KSUID. If null, a default value is used.
     * @param payload The mandatory hexadecimal payload for the KSUID.
     * @param ksuidBytes An optional byte array representation of the KSUID.
     * @since 1.0.0
     */
    constructor(timestamp: Int? = null, payload: Hex, ksuidBytes: ByteArray? = null) : this(timestamp, payload.toByteArray(), ksuidBytes)
    /**
     * Constructs a KSUID instance from a Base62-encoded string.
     *
     * This constructor decodes the provided Base62-encoded string into its corresponding byte array representation
     * using the decode function and initializes the KSUID instance with the resulting byte array.
     *
     * @param string The Base62-encoded string to initialize the KSUID instance.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the provided string contains invalid Base62 characters.
     * @since 1.0.0
     */
    constructor(string: String) : this(ksuidBytes = Base62.decode(string)) {
        validate(string.length == PAD_TO_LENGTH) { "Invalid Base62 string" }
    }
    /**
     * Constructs a KSUID instance using the provided timestamp.
     * The KSUID is generated based on the provided `instant`.
     *
     * @param instant the timestamp to use for generating the KSUID
     * @since 1.0.0
     */
    constructor(instant: Instant) : this(Generator.INSTANCE.newKSUID(instant))
    /**
     * Constructs a KSUID instance using the provided timestamp.
     * The KSUID is generated based on the provided `instant`.
     *
     * @param instant the timestamp to use for generating the KSUID
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(instant: kotlin.time.Instant) : this(Generator.INSTANCE.newKSUID(instant.toJavaInstant()))
    /**
     * Constructs a new instance of the KSUID class using a newly generated KSUID string.
     *
     * This constructor internally calls a factory method to generate a KSUID value,
     * which is then converted to its string representation.
     *
     * @since 1.0.0
     */
    constructor() : this(Generator.createKSUID())

    init {
        if (ksuidBytes != null) {
            val newKsuidBytes = if (ksuidBytes.size != TOTAL_BYTES)
                if (ksuidBytes.size < TOTAL_BYTES)
                    ByteArray(TOTAL_BYTES - ksuidBytes.size) + ksuidBytes
                else ksuidBytes.takeLast(TOTAL_BYTES).toByteArray()
            else ksuidBytes

            this.ksuidBytes = newKsuidBytes

            val byteBuffer = ByteBuffer.wrap(newKsuidBytes)!!
            this.timestamp = byteBuffer.int
            this.payload = ByteArray(PAYLOAD_BYTES)
            byteBuffer.get(this.payload)
        } else {
            validate(payload!!.size == PAYLOAD_BYTES) { "payload must be 16 bytes" }
            this.timestamp = timestamp!!
            this.payload = payload
            this.ksuidBytes = ByteBuffer.allocate(TOTAL_BYTES).putInt(this.timestamp).put(this.payload).array()
        }
    }

    companion object {
        /**
         * Represents the fixed epoch time used in the generation of KSUIDs, measured in seconds
         * since a specified Unix epoch (2014-05-13T16:53:20Z). This epoch is used as a reference point for
         * calculating the timestamp component of a KSUID.
         *
         * @since 1.0.0
         */
        private const val EPOCH = 1400000000
        /**
         * Defines the number of bytes reserved for the payload in a KSUID (K-Sortable Unique Identifier).
         * KSUIDs consist of a timestamp and a payload, with the payload providing additional uniqueness.
         * This constant specifies the fixed size of the payload portion in bytes.
         *
         * @since 1.0.0
         */
        private const val PAYLOAD_BYTES = 16
        /**
         * Represents the fixed byte size of the timestamp portion in a KSUID.
         * This value is used to define the number of bytes allocated to storing the timestamp.
         *
         * @since 1.0.0
         */
        private const val TIMESTAMP_BYTES = 4
        /**
         * Represents the total number of bytes required for the KSUID,
         * which is the sum of the payload bytes and timestamp bytes.
         *
         * This value is used to define the total size of the KSUID structure
         * when working with its byte representation.
         *
         * @since 1.0.0
         */
        private const val TOTAL_BYTES = PAYLOAD_BYTES + TIMESTAMP_BYTES
        /**
         * Represents the fixed length to which KSUID strings are padded
         * when displaying or processing them in textual format.
         *
         * This constant ensures consistent formatting and alignment
         * of KSUID strings across different outputs or systems.
         *
         * @since 1.0.0
         */
        private const val PAD_TO_LENGTH = 27
        /**
         * Comparator instance used for comparing KSUID objects.
         *
         * This comparator first compares KSUID instances based on their `timestamp` in ascending order.
         * If the `timestamp` values are equal, it performs a secondary comparison using the
         * string representation of their `payload` arrays.
         *
         * The comparator ensures a consistent order for KSUID objects, aiding in sorting and other
         * operations that rely on defined ordering.
         *
         * @since 1.0.0
         */
        private val COMPARATOR = Comparator.comparingInt(KSUID::timestamp).thenComparing { it.payload.contentToString() }

        /**
         * Checks whether the current `CharSequence` is a valid KSUID.
         *
         * A KSUID (K-Sortable Unique Identifier) is a 20-byte value consisting of a timestamp and a random payload.
         * This method attempts to construct a `KSUID` instance from the given `CharSequence`. If the construction
         * is successful, the `CharSequence` is considered a valid KSUID; otherwise, it is not.
         *
         * @receiver The `CharSequence` to validate as a KSUID.
         * @return `true` if the `CharSequence` represents a valid KSUID; `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidKSUID() = runCatching { KSUID(toString()) }.isSuccess

        /**
         * Converts the given [CharSequence] into a [KSUID] instance.
         *
         * This method attempts to create a [KSUID] object using the string representation of the
         * [CharSequence]. The result is wrapped in a [Result] to handle potential exceptions
         * that may occur during the creation process, such as invalid format or other constraints.
         *
         * @return A [Result] containing the [KSUID] instance if successful, or an exception if an error occurs.
         * @since 1.0.0
         */
        fun CharSequence.toKSUID() = runCatching { KSUID(toString()) }

        class Serializer : ValueSerializer<KSUID>() {
            override fun serialize(value: KSUID, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<KSUID>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = KSUID(p.string)
        }

        class OldSerializer : JsonSerializer<KSUID>() {
            override fun serialize(value: KSUID, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.toString())
        }

        class OldDeserializer : JsonDeserializer<KSUID>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): KSUID = KSUID(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<KSUID?, String?> {
            override fun convertToDatabaseColumn(attribute: KSUID?): String? = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?): KSUID? = dbData?.let { KSUID(it) }
        }

        class TypeChar : EnhancedUserType<KSUID> {
            override fun getSqlType(): Int = SqlTypes.CHAR

            override fun returnedClass(): Class<KSUID> = KSUID::class.java

            override fun equals(
                x: KSUID?,
                y: KSUID?
            ): Boolean = x == y

            override fun hashCode(x: KSUID?): Int = x?.hashCode() ?: 0

            override fun nullSafeGet(
                rs: ResultSet?,
                position: Int,
                session: SharedSessionContractImplementor?,
                owner: Any?
            ): KSUID? {
                val value = rs?.getString(position) ?: return null
                return KSUID(value)
            }

            override fun nullSafeSet(
                st: PreparedStatement?,
                value: KSUID?,
                index: Int,
                session: SharedSessionContractImplementor?
            ) {
                st?.setString(index, value?.toString()) ?: throw IllegalArgumentException("Statement cannot be null")
            }

            override fun deepCopy(value: KSUID?): KSUID? = value?.let { KSUID(it) }

            override fun isMutable(): Boolean = false

            override fun disassemble(value: KSUID?): Serializable? = deepCopy(value)

            override fun assemble(
                cached: Serializable?,
                owner: Any?
            ): KSUID? = cached as? KSUID

            override fun toSqlLiteral(value: KSUID?): String? = value?.let { "'${it}'" }

            override fun toString(value: KSUID?): String? = value?.toString()

            override fun fromStringValue(sequence: CharSequence?): KSUID =
                sequence?.let { KSUID(it.toString()) } ?: throw IllegalArgumentException("Cannot convert null to KSUID")
        }

        class TypeBytea : EnhancedUserType<KSUID> {
            override fun getSqlType(): Int = SqlTypes.VARBINARY

            override fun returnedClass(): Class<KSUID> = KSUID::class.java

            override fun equals(
                x: KSUID?,
                y: KSUID?
            ): Boolean = x == y

            override fun hashCode(x: KSUID?): Int = x?.hashCode() ?: 0

            override fun nullSafeGet(
                rs: ResultSet?,
                position: Int,
                session: SharedSessionContractImplementor?,
                owner: Any?
            ): KSUID? {
                val bytes = rs?.getBytes(position) ?: return null
                return KSUID(ksuidBytes = bytes)
            }

            override fun nullSafeSet(
                st: PreparedStatement?,
                value: KSUID?,
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

            override fun deepCopy(value: KSUID?): KSUID? = value?.let { KSUID(it) }

            override fun isMutable(): Boolean = false

            override fun disassemble(value: KSUID?): Serializable? = value?.toByteArray()

            override fun assemble(
                cached: Serializable?,
                owner: Any?
            ): KSUID? = (cached as? ByteArray)?.let { KSUID(ksuidBytes = it) }

            override fun toSqlLiteral(value: KSUID?): String? = value?.let { "E'\\\\x${it.toHex().toString(symbol = Hex.HexSymbol.NONE)}'" }

            override fun toString(value: KSUID?): String? = value?.toString()

            override fun fromStringValue(sequence: CharSequence?): KSUID =
                sequence?.let { KSUID(it.toString()) } ?: throw IllegalArgumentException("Cannot convert null to KSUID")
        }
    }

    /**
     * Converts the KSUID instance into a byte array representation.
     *
     * @return A copy of the internal byte array representing this KSUID.
     * @since 1.0.0
     */
    fun toByteArray() = ksuidBytes.copyOf()

    /**
     * Converts the KSUID into its hexadecimal string representation.
     *
     * This method utilizes the `toByteArray` function to retrieve the byte representation
     * of the KSUID and then transforms it into a hexadecimal string using the `toHex` function.
     *
     * @return A string containing the hexadecimal representation of the KSUID.
     * @since 1.0.0
     */
    fun toHex() = toByteArray().toHex()

    /**
     * Creates a string representation suitable for logging, providing details about the instance
     * of the containing class. The generated string includes the class name, the result of the
     * `toString` method, the timestamp, the payload, and the `ksuidBytes` array.
     *
     * @return A detailed log string containing the values of key properties of the instance.
     * @since 1.0.0
     */
    fun toLogString() = StringJoiner(", ", this::class.simpleName + "[", "]")
        .add("string = " + toString())
        .add("timestamp = $timestamp")
        .add("payload = " + payload.contentToString())
        .add("ksuidBytes = " + ksuidBytes.contentToString())
        .toString()

    /**
     * Returns the Base62-encoded string representation of the KSUID.
     *
     * This method encodes the backing byte array of the KSUID into a Base62 format,
     * ensuring a compact and human-readable string. The encoded result is
     * padded to a predefined length if necessary.
     *
     * @return A Base62-encoded string representing the KSUID.
     * @since 1.0.0
     */
    override fun toString() = Base62.encode(ksuidBytes, PAD_TO_LENGTH)

    /**
     * Determines whether another object is equal to this KSUID instance.
     *
     * Two KSUID objects are considered equal if they have the same payload, ksuidBytes,
     * instant, and timestamp values.
     *
     * @param other The object to compare for equality with this instance.
     * @return `true` if the specified object is equal to this instance, `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KSUID

        if (!payload.contentEquals(other.payload)) return false
        if (!ksuidBytes.contentEquals(other.ksuidBytes)) return false
        if (instant.toEpochMilli() != other.instant.toEpochMilli()) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    /**
     * Computes the hash code for the KSUID instance. The hash code is calculated based on the
     * hash codes of its components, including `payload`, `ksuidBytes`, `instant`, and `timestamp`.
     *
     * @return the computed hash code value for the KSUID instance.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = payload.contentHashCode()
        result = 31 * result + ksuidBytes.contentHashCode()
        result = 31 * result + instant.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }

    /**
     * Compares this KSUID instance with another KSUID instance.
     *
     * @param other the KSUID instance to compare this instance with
     * @return a negative integer, zero, or a positive integer as this instance
     *         is less than, equal to, or greater than the specified instance
     * @since 1.0.0
     */
    override fun compareTo(other: KSUID): Int = COMPARATOR.compare(this, other)

    /**
     * A private class that serves as a generator for creating KSUID instances. This class is responsible
     * for providing payload data and encapsulating the logic for initializing and generating KSUIDs.
     *
     * @property payloadSupplier A supplier function that provides the payload for the KSUID as a byte array.
     * The supplier must produce byte arrays of a fixed size defined by `PAYLOAD_BYTES`.
     *
     * @constructor Initializes the generator by requiring the payload supplier to produce valid byte arrays
     * of length `PAYLOAD_BYTES`. If the validation fails, an `IllegalArgumentException` is thrown.
     *
     * @since 1.0.0
     * @author Tommaso Pastorelli
     */
    private class Generator(val payloadSupplier: Supplier<ByteArray>) {
        /**
         * Creates a new instance of the Generator class with a payload supplier that generates
         * random byte arrays using the provided Random instance.
         *
         * @param random the Random instance used to generate random byte arrays
         * @since 1.0.0
         */
        constructor(random: Random) : this({
            val payload = ByteArray(PAYLOAD_BYTES)
            random.nextBytes(payload)
            payload
        })

        init {
            payloadSupplier().size == PAYLOAD_BYTES || throw IllegalArgumentException("payloadBytesSupplier must supply byte arrays of length $PAYLOAD_BYTES")
        }

        companion object {
            /**
             * A singleton instance of the `Generator` class initialized with a secure random number generator.
             * This instance is used to create KSUIDs with a unique payload each time.
             *
             * @since 1.0.0
             */
            val INSTANCE = Generator(SecureRandom())

            /**
             * Generates a KSUID (K-Sortable Unique Identifier) as a string representation.
             *
             * This method utilizes the `createKSUID` function internally to create a KSUID
             * and converts it into its Base62 string form using the `toString` method.
             *
             * @return A string representation of the generated KSUID.
             * @throws IllegalArgumentException If the payload byte array size does not match
             * the expected size required by the underlying `Generator` implementation.
             *
             * @since 1.0.0
             */
            fun generate() = createKSUID().toString()
            /**
             * Generates a new KSUID (K-Sortable Unique Identifier) instance using the default generator.
             *
             * KSUIDs are designed to be unique and lexicographically sortable based on time,
             * making them particularly suitable for distributed systems or databases requiring
             * unique, time-ordered identifiers.
             *
             * This function uses a secure random payload supplier and the current timestamp
             * to ensure uniqueness and temporal sortability of the generated KSUID.
             *
             * @return A new KSUID instance.
             * @since 1.0.0
             */
            fun createKSUID() = INSTANCE.newKSUID()
        }

        /**
         * Generates a new KSUID instance based on the provided time or the current time if no time is provided.
         *
         * @param instant The point in time to be used for the KSUID. Defaults to the current time (`Instant.now()`).
         * @since 1.0.0
         */
        fun newKSUID(instant: Instant = Instant()) = KSUID((instant.toEpochMilli() / 1000 - EPOCH).toInt(), payloadSupplier())
    }
}