/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.identifiers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.time.*
import dev.tommasop1804.kutils.classes.time.Duration.Companion.asMillisOfDuration
import dev.tommasop1804.kutils.exceptions.*
import jakarta.persistence.AttributeConverter
import org.hibernate.type.SqlTypes
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.usertype.EnhancedUserType
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.Instant
import java.util.concurrent.ThreadLocalRandom

/**
 * Represents a Snowflake ID, a unique identifier typically used for distributed systems.
 * The identifier encodes a timestamp, a node ID, and a sequence number into a single 64-bit signed long value.
 *
 * This class is implemented as a value class, providing lightweight encapsulation over a `Long` value.
 * It includes utility methods for extracting specific components of the Snowflake ID and validation checks.
 *
 * @since 3.0.0
 * @author Tommaso Pastorelli
 */
@JvmInline
@Suppress("unused")
@MustUseReturnValues
value class SnowflakeId(val value: Long) : Comparable<SnowflakeId>, Serializable {
    /**
     * Represents the elapsed time portion of the Snowflake ID.
     * This value is derived by shifting the internal value by the defined
     * timestamp shift and interpreting the result as milliseconds of duration.
     * @since 3.0.0
     */
    val elapsedTime: Duration get() = (value ushr TIMESTAMP_SHIFT).asMillisOfDuration()
    /**
     * The timestamp extracted from the Snowflake ID.
     *
     * This represents the number of milliseconds since the custom epoch.
     * It's derived by shifting the internal value by a predefined constant
     * and adding the custom epoch time to decode the timestamp.
     * @since 3.0.0
     */
    val timestamp: Long get() = (value ushr TIMESTAMP_SHIFT) + EPOCH
    /**
     * Provides an `Instant` representation of the current snowflake ID's timestamp.
     * The timestamp is measured as the number of milliseconds since the Unix epoch (1970-01-01T00:00:00Z).
     *
     * This value is derived from the internal `timestamp` field of the snowflake ID.
     * @since 3.0.0
     */
    val instant: Instant get() = Instant(timestamp)
    /**
     * Represents the ID of the node that generated the snowflake. This value is derived by shifting
     * and masking the raw snowflake value. It uniquely identifies the node within the system.
     */
    val nodeId: Int get() = ((value ushr NODE_SHIFT) and MAX_NODE_ID).toInt()
    /**
     * Provides the sequence portion of the Snowflake ID value.
     * This is calculated by applying a bitwise AND operation between the `value` field
     * and the `MAX_SEQUENCE` constant and converting the result to an integer.
     * @since 3.0.0
     */
    val sequence: Int get() = (value and MAX_SEQUENCE).toInt()

    /**
     * Constructs a new SnowflakeID instance by parsing the given character sequence as a long value.
     *
     * @param value The character sequence to be parsed into a long value and used to initialize the SnowflakeID.
     * @since 3.0.0
     */
    constructor(value: CharSequence) : this(value.toString().toLong())
    /**
     * Secondary constructor for the `SnowflakeID` class.
     *
     * This constructor initializes an instance using a Snowflake ID value generated
     * by the `defaultGenerator.nextId()` method. The generated value is assigned to the
     * `value` property of the `SnowflakeID`.
     *
     * The `defaultGenerator.nextId()` method ensures the uniqueness of the ID by
     * combining the elapsed time, node ID, and sequence, following the Snowflake ID format.
     * @since 3.0.0
     */
    constructor() : this(defaultGenerator.nextId().value)

    init {
        if (value <= 0) throw MalformedInputException("The snowflake id must be positive")
        if (timestamp < EPOCH) throw MalformedInputException("The snowflake id must be after or equal to the epoch")
        if (nodeId !in 0..MAX_NODE_ID) throw MalformedInputException("Node ID $nodeId is out of range [0, $MAX_NODE_ID]")
    }

    companion object {
        /**
         * Represents the epoch timestamp in milliseconds used as the reference point
         * for generating unique identifiers in the SnowflakeID system.
         *
         * This value corresponds to the custom starting point from which all
         * timestamps in the SnowflakeID system are measured, and serves as the basis
         * for determining elapsed time when constructing unique IDs.
         * @since 3.0.0
         */
        const val EPOCH = 1_704_067_200_000L
        /**
         * Represents the number of bits allocated for storing the node identifier
         * in the Snowflake ID generation process.
         *
         * This value determines the maximum number of unique nodes that can exist
         * simultaneously in a distributed system.
         *
         * With a value of 10, the system can support up to 2^10 (1024) unique nodes.
         * @since 3.0.0
         */
        const val NODE_BITS = 10
        /**
         * The number of bits allocated for the sequence number within the Snowflake ID.
         *
         * These bits are used to distinguish multiple Snowflake IDs generated in the same
         * millisecond, ensuring uniqueness when IDs are created rapidly in high-concurrency
         * environments.
         * @since 3.0.0
         */
        const val SEQUENCE_BITS = 12
        /**
         * The number of bit positions to shift the timestamp component when constructing
         * a Snowflake ID. This value is derived by summing the bit allocations for the
         * node identifier and the sequence number, ensuring the timestamp occupies the
         * most significant bits in the Snowflake ID.
         * @since 3.0.0
         */
        const val TIMESTAMP_SHIFT = NODE_BITS + SEQUENCE_BITS
        /**
         * Represents the bit-wise shift value used for encoding the node identifier
         * within the unique Snowflake ID generation process. The value is derived
         * from the number of bits allocated for the sequence component.
         * @since 3.0.0
         */
        const val NODE_SHIFT = SEQUENCE_BITS
        /**
         * Represents the maximum possible value for a node identifier within the SnowflakeID system.
         * It is derived based on the bit length defined by the `NODE_BITS` constant.
         * This value is used to ensure that node identifiers do not exceed the allowed bit allocation.
         * @since 3.0.0
         */
        const val MAX_NODE_ID = (1L shl NODE_BITS) - 1L
        /**
         * Represents the maximum allowable value for the sequence portion in a Snowflake ID.
         * This value is determined by shifting 1 left by the number of bits allocated to the sequence (defined by `SEQUENCE_BITS`)
         * and subtracting 1. It ensures that the sequence remains within the allowed bit-width constraints.
         * @since 3.0.0
         */
        const val MAX_SEQUENCE = (1L shl SEQUENCE_BITS) - 1L

        /**
         * Determines if a given 64-bit integer (this Long) is a valid Snowflake ID.
         *
         * A Snowflake ID is a unique identifier following a specific structure, and the validity
         * is determined by successfully constructing a `SnowflakeID` object with the value.
         *
         * @return `true` if the value can be successfully used to create a `SnowflakeID`, `false` otherwise.
         * @since 3.0.0
         */
        fun Long.isValidSnowflakeId() = runCatching { SnowflakeId(this) }.isSuccess
        /**
         * Validates whether the current character sequence can be parsed as a valid SnowflakeID.
         *
         * This method attempts to construct a `SnowflakeID` instance using the character sequence and
         * returns `true` if the construction succeeds without throwing an exception. Otherwise, it returns `false`.
         *
         * @receiver The character sequence to validate as a SnowflakeID.
         * @return `true` if the character sequence represents a valid SnowflakeID, otherwise `false`.
         * @since 3.0.0
         */
        fun CharSequence.isValidSnowflakeId() = runCatching { SnowflakeId(this) }.isSuccess

        /**
         * Converts the current [Long] value to an instance of [SnowflakeId].
         *
         * The conversion is wrapped in a [Result] object to safely handle potential exceptions
         * during the creation of the [SnowflakeId] instance.
         *
         * @return A [Result] containing the created [SnowflakeId] instance if successful, or an exception if the creation fails.
         * @since 3.0.0
         */
        fun Long.toSnowflakeId() = runCatching { SnowflakeId(this) }
        /**
         * Converts the current character sequence into a `SnowflakeID` instance.
         *
         * This extension function attempts to create a `SnowflakeID` object by parsing
         * the character sequence as a long value. The operation is wrapped in a `Result`
         * object to gracefully handle potential parsing exceptions.
         *
         * @receiver The character sequence to be converted into a `SnowflakeID`.
         * @return A `Result` containing the successfully created `SnowflakeID` instance,
         *         or a failure if the character sequence could not be parsed into a valid long value.
         * @since 3.0.0
         */
        fun CharSequence.toSnowflakeId() = runCatching { SnowflakeId(this) }

        private fun resolveDefaultNodeId() = try {
            val hostname = java.net.InetAddress.getLocalHost().hostName
            (hostname.hashCode() and 0x7FFF_FFFF) % (MAX_NODE_ID + 1).toInt()
        } catch (_: Exception) {
            ThreadLocalRandom.current().nextInt((MAX_NODE_ID + 1).toInt())
        }
        private val defaultGenerator by lazy { Generator(resolveDefaultNodeId()) }

        class Serializer : ValueSerializer<SnowflakeId>() {
            override fun serialize(value: SnowflakeId, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<SnowflakeId>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = SnowflakeId(p.string)
        }

        class OldSerializer : JsonSerializer<SnowflakeId>() {
            override fun serialize(value: SnowflakeId, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.toString())
        }

        class OldDeserializer : JsonDeserializer<SnowflakeId>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): SnowflakeId = SnowflakeId(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<SnowflakeId?, Long?> {
            override fun convertToDatabaseColumn(attribute: SnowflakeId?): Long? = attribute?.value
            override fun convertToEntityAttribute(dbData: Long?): SnowflakeId? = dbData?.let { SnowflakeId(it) }
        }

        class Type : EnhancedUserType<SnowflakeId> {
            override fun getSqlType(): Int = java.sql.Types.BIGINT

            override fun returnedClass(): Class<SnowflakeId> = SnowflakeId::class.java

            override fun equals(
                x: SnowflakeId?,
                y: SnowflakeId?
            ): Boolean = x == y

            override fun hashCode(x: SnowflakeId?): Int = x.hashCode()

            override fun deepCopy(value: SnowflakeId?): SnowflakeId? = value?.let { SnowflakeId(it.value) }

            override fun isMutable(): Boolean = false

            override fun disassemble(value: SnowflakeId?): Serializable? = deepCopy(value)

            override fun assemble(cached: Serializable?, owner: Any?): SnowflakeId? = when (cached) {
                is SnowflakeId -> cached
                is Long -> SnowflakeId(cached)
                else -> null
            }

            override fun toSqlLiteral(value: SnowflakeId?): String? = value?.let { "'${it.value}'" }

            override fun toString(value: SnowflakeId?): String? = value?.toString()

            override fun fromStringValue(sequence: CharSequence?): SnowflakeId =
                sequence?.let { SnowflakeId(it.toString()) } ?: throw IllegalArgumentException("Cannot convert null to TSID")

            override fun nullSafeGet(
                rs: ResultSet,
                position: Int,
                options: WrapperOptions
            ): SnowflakeId? {
                val value = rs.getLong(position)
                return if (rs.wasNull()) null else SnowflakeId(value)
            }

            override fun nullSafeSet(
                st: PreparedStatement,
                value: SnowflakeId?,
                index: Int,
                options: WrapperOptions
            ) {
                if (value == null) st.setNull(index, SqlTypes.BIGINT)
                else st.setLong(index, value.value)
            }
        }
    }

    /**
     * Compares this SnowflakeID with the specified SnowflakeID for order.
     *
     * @param other The SnowflakeID to be compared.
     * @return A negative integer, zero, or a positive integer as this SnowflakeID
     *         is less than, equal to, or greater than the specified SnowflakeID.
     * @since 3.0.0
     */
    override fun compareTo(other: SnowflakeId) = value.compareTo(other.value)
    /**
     * Returns a string representation of the SnowflakeID.
     *
     * This method transforms the internal value of the SnowflakeID into its string form.
     * The resulting string reflects the unique identifier encapsulated by this instance.
     *
     * @return The string representation of the internal value.
     * @since 3.0.0
     */
    override fun toString() = value.toString()

    /**
     * Converts the internal value of the SnowflakeID to its Double representation.
     *
     * @return The Double value corresponding to the internal value of this SnowflakeID.
     * @since 3.0.0
     */
    fun toDouble() = value.toDouble()
    /**
     * Converts the internal value of this SnowflakeID to its Float representation.
     *
     * This method provides a Float value derived from the underlying value
     * of the SnowflakeID. The result is useful in scenarios where a
     * floating-point number interpretation of the SnowflakeID is required.
     *
     * @return A Float representation of the internal value.
     * @since 3.0.0
     */
    fun toFloat() = value.toFloat()
    /**
     * Converts the SnowflakeID to its equivalent long representation.
     *
     * This method provides access to the internal value of the SnowflakeID,
     * allowing it to be used as a primitive long type.
     *
     * @return The 64-bit long value representing this SnowflakeID.
     * @since 3.0.0
     */
    fun toLong() = value
    /**
     * Converts the internal value of this SnowflakeID to an unsigned long representation.
     *
     * This method transforms the internal `value` field, which represents the identifier, into an
     * unsigned 64-bit integer (`ULong`). The resulting value reflects the same numeric data as the
     * original but encoded in unsigned form.
     *
     * @return The unsigned long (`ULong`) representation of the internal value.
     * @since 3.0.0
     */
    fun toULong() = value.toULong()
    /**
     * Converts the internal value of this SnowflakeID to an integer.
     *
     * @return The integer representation of the internal value.
     * @since 3.0.0
     */
    fun toInt() = value.toInt()
    /**
     * Converts the internal value of the SnowflakeID to an unsigned 32-bit integer representation.
     *
     * This method enables retrieving the underlying value of the SnowflakeID as an UInt,
     * which can be useful for scenarios where an unsigned integer representation is required.
     *
     * @return The unsigned 32-bit integer equivalent of the internal value.
     * @since 3.0.0
     */
    fun toUInt() = value.toUInt()
    /**
     * Converts the internal value of this SnowflakeID to a `Short`.
     *
     * This method utilizes the underlying integer representation of the SnowflakeID
     * and returns its equivalent `Short` value. Useful when a smaller-sized numeric
     * representation of the SnowflakeID is required.
     *
     * @return The `Short` representation of the internal value.
     * @since 3.0.0
     */
    fun toShort() = value.toShort()
    /**
     * Converts the internal value of this SnowflakeID into an [UShort].
     *
     * This method provides a way to transform the unique value stored in the SnowflakeID
     * into its unsigned 16-bit integer representation. The returned value corresponds to
     * the lower 16 bits of the internal value.
     *
     * @return The [UShort] representation of the internal value.
     * @since 3.0.0
     */
    fun toUShort() = value.toUShort()
    /**
     * Converts the internal value of the SnowflakeID to a Byte.
     *
     * This method provides a byte representation of the identifier encapsulated
     * within this SnowflakeID instance. It is useful for scenarios where a smaller
     * numeric representation of the ID is required, such as serialization or communication
     * with systems that operate on byte-sized data.
     *
     * @return The byte representation of the internal value.
     * @since 3.0.0
     */
    fun toByte() = value.toByte()
    /**
     * Converts the internal value of the SnowflakeID to an unsigned byte representation.
     *
     * This function maps the underlying value to an 8-bit unsigned integer, ensuring
     * compatibility with systems or operations requiring unsigned byte data.
     *
     * @return The unsigned byte representation of the internal value.
     * @since 3.0.0
     */
    fun toUByte() = value.toUByte()

    /**
     * A generator class designed to produce unique Snowflake ID values based on the Twitter Snowflake algorithm.
     *
     * This implementation ensures that each generated ID is unique within a distributed system, using the
     * combination of a timestamp, a machine-specific Node ID, and a sequence number. The class is thread-safe
     * and handles ID generation in a high-concurrency environment.
     *
     * @constructor Creates a new Generator instance.
     * @param nodeId The unique identifier for the machine or node generating the IDs. Must be within the range of 0 to MAX_NODE_ID.
     * @param epoch The custom epoch (in milliseconds) to be used as the starting point for the timestamp. Defaults to EPOCH.
     * @param clock A supplier function providing the current time in milliseconds. Defaults to the system clock.
     * @since 3.0.0
     * @author Tommaso Pastorelli
     */
    class Generator(
        val nodeId: Int,
        val epoch: Long = EPOCH,
        val clock: Supplier<Long> = System::currentTimeMillis
    ) {
        init {
            nodeId.validate(::nodeId, message = "NodeID must be between 0 and $MAX_NODE_ID") { it in 0..MAX_NODE_ID }
        }

        private val state = java.util.concurrent.atomic.AtomicLong(0L)

        fun nextId(): SnowflakeId {
            while (true) {
                val currentTime = clock()
                val elapsed = currentTime - epoch

                validate(elapsed >= 0) { "Clock is before the epoch ($epoch)" }
                validate(elapsed < (1L shl 41)) { "Timestamp overflow — epoch needs updating" }

                val prev = state.get()
                val prevTimestamp = prev ushr 22
                val prevSequence = prev and MAX_SEQUENCE

                val [newTimestamp, newSequence] = if (elapsed == prevTimestamp) {
                    val seq = prevSequence + 1
                    if (seq > MAX_SEQUENCE) {
                        continue
                    }
                    elapsed to seq
                } else if (elapsed > prevTimestamp) {
                    elapsed to 0L
                } else {
                    val seq = prevSequence + 1
                    if (seq > MAX_SEQUENCE) continue
                    prevTimestamp to seq
                }

                val newState = (newTimestamp shl 22) or newSequence
                if (state.compareAndSet(prev, newState)) {
                    val id = (newTimestamp shl TIMESTAMP_SHIFT) or
                            (nodeId.toLong() shl NODE_SHIFT) or
                            newSequence
                    return SnowflakeId(id)
                }
            }

        }
    }
}