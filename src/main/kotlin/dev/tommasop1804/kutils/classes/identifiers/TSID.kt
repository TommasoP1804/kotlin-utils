@file:Suppress("LocalVariableName", "java_integer_as_kotlin_int", "unused")

package dev.tommasop1804.kutils.classes.identifiers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.BigInt
import dev.tommasop1804.kutils.Instant
import dev.tommasop1804.kutils.expect
import dev.tommasop1804.kutils.invoke
import dev.tommasop1804.kutils.isNull
import dev.tommasop1804.kutils.unaryPlus
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
import java.security.SecureRandom
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.Clock
import java.time.Instant
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.apply
import kotlin.math.ceil
import kotlin.math.ln

/**
 * Represents a Time-Sorted Identifiers (TSID) implementation.
 * TSIDs are compact, time-sortable 64-bit identifiers that encode temporal data
 * and randomness for unique and efficient identification.
 *
 * @property number A 64-bit long value representing the TSID.
 * @constructor Creates a TSID from a long value.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = TSID.Companion.Serializer::class)
@JsonDeserialize(using = TSID.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = TSID.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = TSID.Companion.OldDeserializer::class)
@JvmInline
value class TSID(val number: Long) : Comparable<TSID>, Serializable {
    /**
     * Represents the current instant in time, providing a snapshot of the current point on the timeline.
     * Utilizes the `Instant` class to retrieve the current system time.
     *
     * This value is read-only and is dynamically evaluated each time it is accessed.
     *
     * @return The current instant as an `Instant` object.
     * @since 1.0.0
     */
    val instant
        get() = Instant(timestamp)
    /**
     * Represents the timestamp component of a TSID (Time-Sorted Identifier).
     * The timestamp is derived from the number of milliseconds since the Unix epoch 
     * and is used to ensure the temporal ordering of identifiers.
     *
     * This property retrieves the timestamp value associated with the current TSID.
     *
     * @return The timestamp value in milliseconds since the Unix epoch.
     * @since 1.0.0
     */
    val timestamp
        get() = timeComponent + TSID_EPOCH

    /**
     * Represents the time component extracted from the internal `number` field by performing an 
     * unsigned right shift operation. This value is derived during the TSID generation process 
     * and typically corresponds to a timestamp or portion of time-related data.
     *
     * The `timeComponent` is computed by shifting the bits of `number` to the right by a constant 
     * number of `RANDOM_BITS`, isolating the time-based section of the value.
     *
     * This field is immutable and serves as a core component for time-based sorting and comparisons.
     *
     * @since 1.0.0
     */
    val timeComponent
        get() = number ushr RANDOM_BITS
    /**
     * Represents the randomly generated component of the TSID value.
     * This component is derived by applying a bitwise AND operation 
     * between the `number` field and the `RANDOM_MASK`.
     *
     * The randomComponent helps ensure uniqueness within the TSID 
     * by combining it with other components like time.
     *
     * @since 1.0.0
     */
    val randomComponent
        get() = number and RANDOM_MASK

    /**
     * Provides the Base32 string representation of the TSID.
     *
     * The derived string is constructed by encoding the internal numeric value of the TSID
     * into a fixed-length character sequence using a base-32 encoding scheme. Each character 
     * represents chunks of bits from the numeric value, ensuring a compact and collision-resistant 
     * identifier format.
     *
     * @return A string containing the Base32-encoded TSID.
     * @since 1.0.0
     */
    val base32String
        get(): String {
            val chars = CharArray(TSID_CHARS)

            chars[0x00] = ALPHABET[((number ushr 60) and 31L).toInt()]
            chars[0x01] = ALPHABET[((number ushr 55) and 31L).toInt()]
            chars[0x02] = ALPHABET[((number ushr 50) and 31L).toInt()]
            chars[0x03] = ALPHABET[((number ushr 45) and 31L).toInt()]
            chars[0x04] = ALPHABET[((number ushr 40) and 31L).toInt()]
            chars[0x05] = ALPHABET[((number ushr 35) and 31L).toInt()]
            chars[0x06] = ALPHABET[((number ushr 30) and 31L).toInt()]
            chars[0x07] = ALPHABET[((number ushr 25) and 31L).toInt()]
            chars[0x08] = ALPHABET[((number ushr 20) and 31L).toInt()]
            chars[0x09] = ALPHABET[((number ushr 15) and 31L).toInt()]
            chars[0x0a] = ALPHABET[((number ushr 10) and 31L).toInt()]
            chars[0x0b] = ALPHABET[((number ushr 5) and 31L).toInt()]
            chars[0x0c] = ALPHABET[(number and 31L).toInt()]

            return String(chars)
        }

    /**
     * Constructs a TSID instance from a given string representation.
     *
     * @param string The string input representing the TSID. If `base32` is true, the string is
     * considered to be in a custom Base32 encoding, otherwise it is interpreted as a numeric value.
     * @param base32 A boolean flag indicating whether the string should be parsed as a Base32 TSID.
     * Defaults to `true`.
     * 
     * @throws IllegalArgumentException If the string is not a valid Base32 encoded TSID and `base32` is `true`.
     * The validation checks ensure the string adheres to the expected TSID format, including length and character set.
     * 
     * @since 1.0.0
     */
    constructor(string: String, base32: Boolean = true) : this(with(string) {
        if (!base32) return@with string.toLong()

        val chars = toCharArray()
        validateInputFormat(chars.isValidCharArray()) { "Invalid base32 TSID: $string" }
        var _number = 0L
        _number = _number or (ALPHABET_VALUES[chars[0x00].code] shl 60)
        _number = _number or (ALPHABET_VALUES[chars[0x01].code] shl 55)
        _number = _number or (ALPHABET_VALUES[chars[0x02].code] shl 50)
        _number = _number or (ALPHABET_VALUES[chars[0x03].code] shl 45)
        _number = _number or (ALPHABET_VALUES[chars[0x04].code] shl 40)
        _number = _number or (ALPHABET_VALUES[chars[0x05].code] shl 35)
        _number = _number or (ALPHABET_VALUES[chars[0x06].code] shl 30)
        _number = _number or (ALPHABET_VALUES[chars[0x07].code] shl 25)
        _number = _number or (ALPHABET_VALUES[chars[0x08].code] shl 20)
        _number = _number or (ALPHABET_VALUES[chars[0x09].code] shl 15)
        _number = _number or (ALPHABET_VALUES[chars[0x0a].code] shl 10)
        _number = _number or (ALPHABET_VALUES[chars[0x0b].code] shl 5)
        _number = _number or ALPHABET_VALUES[chars[0x0c].code]
        _number
    })
    /**
     * Constructs an instance using the specified byte array. The byte array must be 8 bytes long.
     *
     * Converts the provided byte array into a numeric value by processing each byte and shifting 
     * its bits to construct a 64-bit long integer.
     *
     * @param bytes The byte array to initialize the instance. Must contain exactly 8 bytes.
     * @throws dev.tommasop1804.kutils.exceptions.ExpectationMismatchException If the size of the byte array is not equal to 8 bytes.
     * @since 1.0.0
     */
    constructor(bytes: ByteArray) : this(with(bytes) {
        expect(size == TSID_BYTES) { "TSID must be 8 bytes long" }
        var _number = 0L
        _number = _number or ((bytes[0x0].toLong() and 0xffL) shl 56)
        _number = _number or ((bytes[0x1].toLong() and 0xffL) shl 48)
        _number = _number or ((bytes[0x2].toLong() and 0xffL) shl 40)
        _number = _number or ((bytes[0x3].toLong() and 0xffL) shl 32)
        _number = _number or ((bytes[0x4].toLong() and 0xffL) shl 24)
        _number = _number or ((bytes[0x5].toLong() and 0xffL) shl 16)
        _number = _number or ((bytes[0x6].toLong() and 0xffL) shl 8)
        _number = _number or (bytes[0x7].toLong() and 0xffL)
        _number
    })

    /**
     * Secondary constructor for initializing an instance using a specific factory type.
     *
     * The constructor takes a `factoryType`, which determines the specific factory instance
     * that will be used to generate the required object. Depending on the `factoryType` provided,
     * one of the predefined factory instances (`INSTANCE`, `INSTANCE_256`, `INSTANCE_1024`, or `INSTANCE_4096`) 
     * is selected and used to produce the required data for the primary constructor.
     *
     * @param factoryType The type of factory to use for generating the instance.
     * @since 1.0.0
     */
    constructor(factoryType: FactoryType) : this(
        when (factoryType) {
            FactoryType.DEFAULT -> Factory.INSTANCE
            FactoryType.NODES_256 -> Factory.INSTANCE_256
            FactoryType.NODES_1024 -> Factory.INSTANCE_1024
            FactoryType.NODES_4096 -> Factory.INSTANCE_4096
        }.generate().number
    )

    /**
     * Secondary constructor that initializes an object using a given Factory instance.
     * The factory is utilized to generate the required input for delegating the initialization.
     *
     * @param factory The Factory instance responsible for generating the necessary data.
     * @since 1.0.0
     */
    constructor(factory: Factory) : this(factory.generate().number)

    /**
     * Returns a fast new TSID.

     * This static method is a quick alternative to [Factory.getTSID].

     * It employs [AtomicInteger] to generate up to 2^22 (4,194,304) TSIDs per
     * millisecond. It can be useful, for example, for logging.

     * Security-sensitive applications that require a cryptographically secure
     * pseudo-random generator **should** use [Factory.getTSID]}.

     * System property "tsidcreator.node" and environment variable
     * "TSID_NODE" are ignored by this method. Therefore, there will be
     * collisions if more than one process is generating TSIDs using this method. In
     * that case, [Factory.getTSID] **should** be used in conjunction
     * with that property or variable.
     *
     * @return a TSID
     * @see AtomicInteger
     * @since 1.0.0
     */
    constructor() : this(with(Unit) {
        val time = (System.currentTimeMillis() - TSID_EPOCH) shl RANDOM_BITS
        val tail = LazyHolder.counter.incrementAndGet().toLong() and RANDOM_MASK
        TSID(time or tail).number
    })

    companion object {
        /**
         * Represents the number of bytes used to encode the TSID.
         *
         * This constant defines the fixed size, in bytes, for the binary representation of a TSID.
         *
         * @since 1.0.0
         */
        const val TSID_BYTES = 8
        /**
         * Represents the fixed length of a TSID (Time-Sorted Identifier) when encoded using Base32.
         * The value corresponds to the number of characters required to encode the TSID.
         *
         * @since 1.0.0
         */
        const val TSID_CHARS = 13
        /**
         * The custom epoch used for generating time-based TSIDs (Time-Sortable Identifier).
         *
         * This epoch corresponds to 2020-01-01T00:00:00Z in milliseconds since the Unix epoch (1970-01-01T00:00:00Z).
         * It serves as the reference point for calculating timestamps relative to this specific time, 
         * enabling smaller numeric values for the timestamp component in TSIDs.
         *
         * @since 1.0.0
         */
        const val TSID_EPOCH = 1577836800000 // 2020-01-01T00:00:00Z epoch millis
        /**
         * Represents the number of random bits used in the generation of a TSID.
         *
         * TSID (Time-Sorted Identifier) uses a combination of time and randomness to ensure unique and 
         * sortable identifiers. The `RANDOM_BITS` constant specifies the number of bits dedicated 
         * to the random component in TSID generation.
         *
         * Modifying this value would impact the randomness entropy of TSIDs and could lead 
         * to unintended consequences if not handled carefully.
         *
         * @since 1.0.0
         */
        const val RANDOM_BITS = 22
        /**
         * A constant value representing the random component mask used to extract
         * or constrain the randomly generated portion of a TSID.
         *
         * This mask limits the random component to 22 bits, ensuring it fits within
         * the required bit range for proper encoding and comparison.
         *
         * @since 1.0.0
         */
        const val RANDOM_MASK = 0x003fffffL
        /**
         * A constant character array representing a custom base-32 alphabet.
         * This alphabet is used for operations involving encoding and decoding in base-32.
         * It excludes ambiguous characters such as 'I' and 'O' to improve readability.
         *
         * The character set consists of digits 0-9 and uppercase letters A-Z, with the following exclusions:
         * - 'I' (to avoid confusion with '1')
         * - 'O' (to avoid confusion with '0')
         *
         * This alphabet is commonly utilized in scenarios where base-32 encoding with unambiguous characters is required.
         *
         * @since 1.0.0
         */
        val ALPHABET = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z')
        /**
         * A predefined array of size 128 initialized to -1, intended for mapping or storing 
         * specific values associated with ASCII characters or other inputs.
         *
         * This array is typically used for efficient lookups based on character codes
         * (e.g., for encoding or decoding operations).
         *
         * The default value of -1 indicates an unmapped or uninitialized state for any 
         * given index.
         *
         * @since 1.0.0
         */
        val ALPHABET_VALUES = LongArray(128) { -1 }
        init {
            ALPHABET_VALUES['0'.code] = 0x00
            ALPHABET_VALUES['1'.code] = 0x01
            ALPHABET_VALUES['2'.code] = 0x02
            ALPHABET_VALUES['3'.code] = 0x03
            ALPHABET_VALUES['4'.code] = 0x04
            ALPHABET_VALUES['5'.code] = 0x05
            ALPHABET_VALUES['6'.code] = 0x06
            ALPHABET_VALUES['7'.code] = 0x07
            ALPHABET_VALUES['8'.code] = 0x08
            ALPHABET_VALUES['9'.code] = 0x09
            // Lower case
            ALPHABET_VALUES['a'.code] = 0x0a
            ALPHABET_VALUES['b'.code] = 0x0b
            ALPHABET_VALUES['c'.code] = 0x0c
            ALPHABET_VALUES['d'.code] = 0x0d
            ALPHABET_VALUES['e'.code] = 0x0e
            ALPHABET_VALUES['f'.code] = 0x0f
            ALPHABET_VALUES['g'.code] = 0x10
            ALPHABET_VALUES['h'.code] = 0x11
            ALPHABET_VALUES['j'.code] = 0x12
            ALPHABET_VALUES['k'.code] = 0x13
            ALPHABET_VALUES['m'.code] = 0x14
            ALPHABET_VALUES['n'.code] = 0x15
            ALPHABET_VALUES['p'.code] = 0x16
            ALPHABET_VALUES['q'.code] = 0x17
            ALPHABET_VALUES['r'.code] = 0x18
            ALPHABET_VALUES['s'.code] = 0x19
            ALPHABET_VALUES['t'.code] = 0x1a
            ALPHABET_VALUES['v'.code] = 0x1b
            ALPHABET_VALUES['w'.code] = 0x1c
            ALPHABET_VALUES['x'.code] = 0x1d
            ALPHABET_VALUES['y'.code] = 0x1e
            ALPHABET_VALUES['z'.code] = 0x1f
            // Lower case OIL
            ALPHABET_VALUES['o'.code] = 0x00
            ALPHABET_VALUES['i'.code] = 0x01
            ALPHABET_VALUES['l'.code] = 0x01
            // Upper case
            ALPHABET_VALUES['A'.code] = 0x0a
            ALPHABET_VALUES['B'.code] = 0x0b
            ALPHABET_VALUES['C'.code] = 0x0c
            ALPHABET_VALUES['D'.code] = 0x0d
            ALPHABET_VALUES['E'.code] = 0x0e
            ALPHABET_VALUES['F'.code] = 0x0f
            ALPHABET_VALUES['G'.code] = 0x10
            ALPHABET_VALUES['H'.code] = 0x11
            ALPHABET_VALUES['J'.code] = 0x12
            ALPHABET_VALUES['K'.code] = 0x13
            ALPHABET_VALUES['M'.code] = 0x14
            ALPHABET_VALUES['N'.code] = 0x15
            ALPHABET_VALUES['P'.code] = 0x16
            ALPHABET_VALUES['Q'.code] = 0x17
            ALPHABET_VALUES['R'.code] = 0x18
            ALPHABET_VALUES['S'.code] = 0x19
            ALPHABET_VALUES['T'.code] = 0x1a
            ALPHABET_VALUES['V'.code] = 0x1b
            ALPHABET_VALUES['W'.code] = 0x1c
            ALPHABET_VALUES['X'.code] = 0x1d
            ALPHABET_VALUES['Y'.code] = 0x1e
            ALPHABET_VALUES['Z'.code] = 0x1f
            // Upper case OIL
            ALPHABET_VALUES['O'.code] = 0x00
            ALPHABET_VALUES['I'.code] = 0x01
            ALPHABET_VALUES['L'.code] = 0x01
        }
        
        /**
         * Verifies if the current string qualifies as a valid Base32-encoded TSID (Time-Sorted Unique Identifier).
         *
         * A valid Base32 TSID string adheres to the following criteria:
         * - It has a fixed length defined by the TSID specification.
         * - The first character does not belong to a set of disallowed values.
         * - All characters are recognized as part of the Base32 alphabet as defined by the TSID encoding rules.
         *
         * This method utilizes `isValidCharArray` on the string's character array 
         * to perform the necessary validation.
         *
         * @receiver The string to validate as a Base32 TSID.
         * @return `true` if the string is a valid Base32 TSID, otherwise `false`.
         * @since 1.0.0
         */
        fun String.isValidBase32TSID() = toCharArray().isValidCharArray()
        /**
         * Validates whether the `CharArray` represents a valid character sequence
         * based on predefined constraints. The validation checks include ensuring
         * the size matches the expected length and verifying that each character
         * belongs to a valid alphabet set.
         *
         * @return `true` if the `CharArray` is valid according to the predefined
         *         criteria; `false` otherwise.
         * @since 1.0.0
         */
        private fun CharArray.isValidCharArray(): Boolean {
            if (size != TSID_CHARS || ALPHABET_VALUES[get(0).code] and 0b10000 != 0L) return false
            for (i in 0 until size)
                if (ALPHABET_VALUES[get(i).code] == -1L) return false
            return true
        }
        
        /**
         * Converts the given string into a TSID instance.
         *
         * @param base32 Indicates whether the string should be interpreted as a Base32-encoded 
         * value. If true, the string is expected to follow Base32 encoding. Defaults to true.
         * 
         * @return A Result encapsulating the successful conversion to a TSID instance, or any 
         * exception that occurred during the process.
         *
         * @since 1.0.0
         */
        fun String.toTSID(base32: Boolean = true) = runCatching { TSID(this, base32) }
        /**
         * Converts a `Long` value to a `TSID` instance.
         *
         * This method takes the `Long` number it is called on and creates a new `TSID` 
         * object using that value. The `Long` value is typically a representation of 
         * a unique identifier that the `TSID` type models.
         *
         * @return A `TSID` instance initialized with the `Long` value.
         * @since 1.0.0
         */
        fun Long.toTSID() = TSID(this)
        
        /**
         * Decodes the current string representation of a TSID (Time-Sorted Unique Identifier) 
         * from the specified base into a TSID object.
         *
         * This method validates that the given base is within the acceptable range and uses
         * the provided base to decode the string representation into a TSID instance.
         *
         * @param base The numeric base to use for decoding. Must be between 2 and 62 (inclusive).
         * @return The decoded TSID instance.
         * @throws IllegalArgumentException If the base is outside the valid range or if the string 
         * does not conform to the expected format for the specified base.
         * @since 1.0.0
         */
        fun String.decodeToTSID(base: Int): TSID {
            validate(base in 2..62) { "Invalid base: $base" }
            return BaseN.decode(this, base)
        }

        class Serializer : ValueSerializer<TSID>() {
            override fun serialize(value: TSID, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<TSID>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = TSID(p.string)
        }

        class OldSerializer : JsonSerializer<TSID>() {
            override fun serialize(value: TSID, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.toString())
        }

        class OldDeserializer : JsonDeserializer<TSID>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): TSID = TSID(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<TSID?, Long?> {
            override fun convertToDatabaseColumn(attribute: TSID?): Long? = attribute?.number
            override fun convertToEntityAttribute(dbData: Long?): TSID? = dbData?.let { TSID(it) }
        }

        class Type : EnhancedUserType<TSID> {
            override fun getSqlType(): Int = SqlTypes.BIGINT

            override fun returnedClass(): Class<TSID> = TSID::class.java

            override fun equals(
                x: TSID?,
                y: TSID?
            ): Boolean = x == y

            override fun hashCode(x: TSID?): Int = x?.hashCode() ?: 0

            override fun nullSafeGet(
                rs: ResultSet?,
                position: Int,
                session: SharedSessionContractImplementor?,
                owner: Any?
            ): TSID? {
                val value = rs?.getLong(position)?.run { if (this == -1L) null else this } ?: return null
                return TSID(value)
            }

            override fun nullSafeSet(
                st: PreparedStatement?,
                value: TSID?,
                index: Int,
                session: SharedSessionContractImplementor?
            ) {
                st?.setLong(index, value?.number ?: -1) ?: throw IllegalArgumentException("Statement cannot be null")
            }

            override fun deepCopy(value: TSID?): TSID? = value?.let { TSID(it.number) }

            override fun isMutable(): Boolean = false

            override fun disassemble(value: TSID?): Serializable? = deepCopy(value)

            override fun assemble(
                cached: Serializable?,
                owner: Any?
            ): TSID? = cached as? TSID

            override fun toSqlLiteral(value: TSID?): String? = value?.let { "'${it.number}'" }

            override fun toString(value: TSID?): String? = value?.toString()

            override fun fromStringValue(sequence: CharSequence?): TSID =
                sequence?.let { TSID(it.toString()) } ?: throw IllegalArgumentException("Cannot convert null to TSID")
        }
    }

    /**
     * Extracts the first component of the object.
     * 
     * This function acts as a destructuring operator to retrieve the `timeComponent` 
     * from the object it is invoked upon. Designed for use in destructuring declarations.
     * 
     * @return The `timeComponent` of the object as defined by its implementation.
     * 
     * @since 1.0.0
     */
    operator fun component1() = timeComponent
    /**
     * Provides the second component of a data structure when using destructuring declarations.
     * This operator function extracts the second constituent (component) of an object.
     *
     * @return The second component of the object.
     * @since 1.0.0
     */
    operator fun component2() = randomComponent

    /**
     * Returns an `Instant` object based on a given custom epoch.
     *
     * @param customEpoch An `Instant` object representing the custom epoch from which an adjusted timestamp is calculated.
     * @since 1.0.0
     */
    fun getInstant(customEpoch: Instant) = Instant(getTimestamp(customEpoch.toEpochMilli()))
    /**
     * Computes a timestamp by adding a custom epoch offset to the current time component.
     *
     * @param customEpoch the custom epoch offset, in milliseconds, to be added to the time component.
     * @return the computed timestamp, which represents the time adjusted by the custom epoch.
     * @since 1.0.0
     */
    fun getTimestamp(customEpoch: Long) = timeComponent + customEpoch

    /**
     * Converts the current TSID to its byte array representation.
     *
     * @return A `ByteArray` of size 8 that contains the byte representation of the TSID number.
     * @since 1.0.0
     */
    fun toByteArray(): ByteArray {
        val bytes = ByteArray(TSID_BYTES)

        bytes[0x0] = (number ushr 56).toByte()
        bytes[0x1] = (number ushr 48).toByte()
        bytes[0x2] = (number ushr 40).toByte()
        bytes[0x3] = (number ushr 32).toByte()
        bytes[0x4] = (number ushr 24).toByte()
        bytes[0x5] = (number ushr 16).toByte()
        bytes[0x6] = (number ushr 8).toByte()
        bytes[0x7] = (number).toByte()

        return bytes
    }

    /**
     * Converts the current object to its string representation.
     *
     * @param base32 A boolean flag indicating whether the string representation 
     * should be in base32 format. If true, the result will be the base32-encoded 
     * string; otherwise, it will be the standard numeric string representation.
     * @return The string representation of the object based on the specified format.
     * @since 1.0.0
     */
    fun toString(base32: Boolean) = if (base32) base32String else number.toString()
    /**
     * Returns a string representation of the TSID object.
     * Delegates to the `toString(Boolean)` method, using `true` as the default argument.
     *
     * @return A string representation of the TSID, encoded in Base32 format.
     * @since 1.0.0
     */
    override fun toString() = toString(true)

    /**
     * Compares this TSID instance with the specified TSID object for order.
     * 
     * @param other the TSID instance to be compared with this instance.
     * @return an integer value: 
     *         - `1` if this instance is greater than the specified instance, 
     *         - `-1` if this instance is smaller than the specified instance, 
     *         - `0` if both instances are equal.
     * @since 1.0.0
     */
    override fun compareTo(other: TSID): Int {
        val a = number.toULong()
        val b = other.number.toULong()

        if (a > b) return 1
        else if (a < b) return -1

        return 0
    }
    
    /**
     * Encodes the current object into a string representation using the specified base.
     *
     * @param base The base to use for encoding. Must be between 2 and 62, inclusive.
     * @return A string representation of the object encoded in the specified base.
     * @since 1.0.0
     */
    fun encode(base: Int): String {
        validate(base in 2..62) { "Invalid base: $base" }
        return BaseN.encode(this, base)
    }
    
    private object BaseN {
        val MAX: BigInt = BigInt.valueOf(2).pow(64)
        const val ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

        fun encode(tsid: TSID, base: Int): String {
            var x = BigInt(1, tsid.toByteArray())
            val radix = BigInt.valueOf(base.toLong())
            val length = ceil(64 / (ln(base.toDouble()) / ln(2.0))).toInt()
            var b = length // buffer index
            val buffer = CharArray(length)
            while (x > BigInt.ZERO) {
                val result = x.divideAndRemainder(radix)
                buffer[--b] = ALPHABET[result[1].toInt()]
                x = result[0]
            }
            while (b > 0) {
                buffer[--b] = '0'
            }
            return String(buffer)
        }

        fun decode(str: String, base: Int): TSID {
            var x = BigInt.ZERO
            val radix = BigInt.valueOf(base.toLong())
            val length = ceil(64 / (ln(base.toDouble()) / ln(2.0))).toInt()
            expect(str.length == length) { String.format("Invalid base-%d length: %s", base, str.length) }
            for (i in 0..<str.length) {
                val plus = str[i](ALPHABET).toLong()
                validate(plus in 0..<base) { String.format("Invalid base-%d character: %s", base, str[i]) }
                x = x.multiply(radix).add(BigInt.valueOf(plus))
            }
            validate(x <= MAX) {
                String.format("Invalid base-%d value (overflow): %s", base, x)
            }
            return TSID(x.toLong())
        }
    }
    
    /**
     * LazyHolder is a private object used for lazy initialization of shared resources.
     * It provides a thread-safe mechanism to generate and store a random integer.
     *
     * This object utilizes Java's `AtomicInteger` to ensure thread-safety and consistency
     * when accessing the `counter` property, which is initialized with a random integer.
     *
     * @since 1.0.0
     */
    private object LazyHolder {
        /**
         * An atomic integer counter initialized with a random value.
         *
         * This variable is used to generate unique identifiers or track counts
         * in a thread-safe manner. The counter starts at a random value generated
         * using `SplittableRandom` to help reduce collisions in concurrent environments.
         *
         * @receiver TSID
         * @since 1.0.0
         */
        val counter = AtomicInteger(SplittableRandom().nextInt())
    }

    /**
     * Represents the types of factories associated with generating TSID instances. 
     * Each enum value corresponds to a specific configuration of nodes, 
     * which determine the scalability and uniqueness of ID generation.
     *
     * @since 1.0.0
     */
    enum class FactoryType {
        /**
         * Represents the default factory type used when no specific type is specified.
         *
         * @since 1.0.0
         */
        DEFAULT,
        /**
         * Represents the NODES_256 type within the set of available factory types.
         * This type is typically used to configure or specify behavior involving a resource
         * or system with 256 nodes or equivalent entities.
         *
         * @since 1.0.0
         */
        NODES_256,
        /**
         * Represents a specific type of factory node configuration
         * characterized by the use of 1024 nodes.
         *
         * This is a member of the FactoryType enumeration, providing
         * predefined configurations for various node sizes.
         *
         * @since 1.0.0
         */
        NODES_1024,
        /**
         * Represents a configuration type for a node-based system supporting up to 4096 nodes.
         * This enum constant is part of the `FactoryType` enumeration, which provides predefined configurations 
         * for various node capacities. It is typically used to specify larger-scale node configurations.
         *
         * @since 1.0.0
         */
        NODES_4096;
    }

    /**
     * A factory that actually generates Time-Sorted Unique Identifiers (TSID).
     *
     * You can use this class to generate a Tsid or to make some customizations,
     * for example changing the default [SecureRandom] random generator to a faster pseudo-random generator.
     *
     * If a system property "tsidcreator.node" or environment variable
     * "TSID_NODE" is defined, its value is utilized as node identifier. One
     * of them **should** be defined to embed a machine ID in the generated TSID
     * in order to avoid TSID collisions. Using that property or variable is
     * **highly recommended**. If no property or variable is defined, a random
     * node ID is generated at initialization.
     *
     * If a system property "tsidcreator.node.count" or environment variable
     * "TSID_NODE_COUNT" is defined, its value is utilized by the
     * constructors of this class to adjust the amount of bits needed to embed the
     * node ID. For example, if the number 50 is given, the node bit amount is
     * adjusted to 6, which is the minimum number of bits to accommodate 50 nodes.
     * If no property or variable is defined, the number of bits reserved for node
     * ID is set to 10, which can accommodate 1024 nodes.
     *
     * This class **should** be used as a singleton. Make sure that you create
     * and reuse a single instance of [Factory] per node in your
     * distributed system.
     * @since 1.0.0
     */
    class Factory(builder: Builder) {
        private var counter: Int
        private var lastTime: Long

        private val node: Int
        private val nodeBits: Int
        private val counterBits: Int
        private val nodeMask: Int
        private val counterMask: Int
        private val clock: Clock
        private val customEpoch: Long
        private val random: IRandom
        private val randomBytes: Int

        init {
            // setup node bits, custom epoch and random function
            customEpoch = builder.getCustomEpoch()
            nodeBits = builder.getNodeBits()
            random = builder.getRandom()
            clock = builder.getClock()

            // setup constants that depend on node bits
            counterBits = RANDOM_BITS - nodeBits
            counterMask = (RANDOM_MASK ushr nodeBits).toInt()
            nodeMask = (RANDOM_MASK ushr counterBits).toInt()

            // setup how many bytes to get from the random function
            randomBytes = ((counterBits - 1) / 8) + 1

            // setup the node identifier
            node = builder.getNode() and nodeMask

            // finally, initialize internal state
            lastTime = clock.millis()
            synchronized(LOCK) {
                counter = getRandomValue()
            }
        }

        companion object {
            private val LOCK = Any()

            val INSTANCE = Factory(builder())
            val INSTANCE_256 = newInstance256()
            val INSTANCE_1024 = newInstance1024()
            val INSTANCE_4096 = newInstance4096()

            val THREAD_LOCAL_RANDOM_FUNCTION: () -> Int = {
                java.util.concurrent.ThreadLocalRandom.current().nextInt()
            }

            const val NODE_BITS_256 = 8
            const val NODE_BITS_1024 = 10
            const val NODE_BITS_4096 = 12

            /**
             * Returns a new factory for up to 256 nodes and 16384 ID/ms.
             *
             * @return [Factory]
             * @since 1.0.0
             */
            fun newInstance256() = builder().withNodeBits(NODE_BITS_256).build()

            /**
             * Returns a new factory for up to 256 nodes and 16384 ID/ms.
             *
             * @param node the node identifier
             * @return [Factory]
             * @since 1.0.0
             */
            fun newInstance256(node: Int) = builder().withNodeBits(NODE_BITS_256).withNode(node).build()

            /**
             * Returns a new factory for up to 1024 nodes and 4096 ID/ms.
             *
             * It is equivalent to `Factory()`.
             *
             * @return [Factory]
             * @since 1.0.0
             */
            fun newInstance1024() = builder().withNodeBits(NODE_BITS_1024).build()

            /**
             * Returns a new factory for up to 1024 nodes and 4096 ID/ms.
             *
             * It is equivalent to `Factory(node)`.
             *
             * @param node the node identifier
             * @return [Factory]
             * @since 1.0.0
             */
            fun newInstance1024(node: Int) = builder().withNodeBits(NODE_BITS_1024).withNode(node).build()

            /**
             * Returns a new factory for up to 4096 nodes and 1024 ID/ms.
             *
             * @return [Factory]
             * @since 1.0.0
             */
            fun newInstance4096() = builder().withNodeBits(NODE_BITS_4096).build()

            /**
             * Returns a new factory for up to 4096 nodes and 1024 ID/ms.
             *
             * @param node the node identifier
             * @return [Factory]
             * @since 1.0.0
             */
            fun newInstance4096(node: Int) = builder().withNodeBits(NODE_BITS_4096).withNode(node).build()

            /**
             * Returns a builder object.
             *
             * It is used to build a custom [Factory].
             * @since 1.0.0
             */
            fun builder(): Builder = Builder()

            /**
             * Returns a new TSID.
             *
             * The node ID is set by defining the system property "tsid.node" or
             * the environment variable "TSID_NODE". One of them **should** be
             * used to embed a machine ID in the generated TSID in order to avoid TSID
             * collisions. If that property or variable is not defined, the node ID is
             * chosen randomly.
             *
             * The amount of nodes can be set by defining the system property
             * "tsid.node.count" or the environment variable
             * "TSID_NODE_COUNT". That property or variable is used to adjust the
             * minimum amount of bits to accommodate the node ID. If that property or
             * variable is not defined, the default amount of nodes is 1024, which takes 10
             * bits.
             *
             * The amount of bits needed to accommodate the node ID is calculated by this
             * pseudo-code formula: `node_bits = ceil(log(node_count)/log(2))`.
             *
             * Random component settings:
             * - Node bits: node_bits
             * - Counter bits: 22-node_bits
             * - Maximum node: 2^node_bits
             * - Maximum counter: 2^(22-node_bits)
             *
             * The time component can be 1 ms or more ahead of the system time when
             * necessary to maintain monotonicity and generation speed.
             *
             * @return a TSID
             * @since 1.0.0
             */
            fun getTSID(): TSID = INSTANCE.generate()

            /**
             * Returns a new TSID.
             *
             * It supports up to 256 nodes.
             *
             * It can generate up to 16,384 TSIDs per millisecond per node.
             *
             * The node ID is set by defining the system property "tsid.node" or
             * the environment variable "TSID_NODE". One of them **should** be
             * used to embed a machine ID in the generated TSID in order to avoid TSID
             * collisions. If that property or variable is not defined, the node ID is
             * chosen randomly.
             *
             * Random component settings:
             * - Node bits: 8
             * - Counter bits: 14
             * - Maximum node: 256 (2^8)
             * - Maximum counter: 16,384 (2^14)
             *
             * The time component can be 1 ms or more ahead of the system time when
             * necessary to maintain monotonicity and generation speed.
             *
             * @return a TSID
             * @since 1.0.0
             */
            fun getTSID256(): TSID = INSTANCE_256.generate()

            /**
             * Returns a new TSID.
             *
             * It supports up to 1,024 nodes.
             *
             * It can generate up to 4,096 TSIDs per millisecond per node.
             *
             * The node ID is set by defining the system property "tsid.node" or
             * the environment variable "TSID_NODE". One of them **should** be
             * used to embed a machine ID in the generated TSID in order to avoid TSID
             * collisions. If that property or variable is not defined, the node ID is
             * chosen randomly.
             *
             * Random component settings:
             * - Node bits: 10
             * - Counter bits: 12
             * - Maximum node: 1,024 (2^10)
             * - Maximum counter: 4,096 (2^12)
             *
             * The time component can be 1 ms or more ahead of the system time when
             * necessary to maintain monotonicity and generation speed.
             *
             * @return a TSID
             * @since 1.0.0
             */
            fun getTSID1024(): TSID = INSTANCE_1024.generate()

            /**
             * Returns a new TSID.
             *
             * It supports up to 4,096 nodes.
             *
             * It can generate up to 1,024 TSIDs per millisecond per node.
             *
             * The node ID is set by defining the system property "tsid.node" or
             * the environment variable "TSID_NODE". One of them **should** be
             * used to embed a machine ID in the generated TSID in order to avoid TSID
             * collisions. If that property or variable is not defined, the node ID is
             * chosen randomly.
             *
             * Random component settings:
             * - Node bits: 12
             * - Counter bits: 10
             * - Maximum node: 4,096 (2^12)
             * - Maximum counter: 1,024 (2^10)
             *
             * The time component can be 1 ms or more ahead of the system time when
             * necessary to maintain monotonicity and generation speed.
             *
             * @return a TSID number
             * @since 1.0.0
             */
            fun getTSID4096(): TSID = INSTANCE_4096.generate()
        }

        /**
         * Returns a TSID.
         *
         * @return a TSID.
         * @since 1.0.0
         */
        fun generate(): TSID {
            val time: Long
            val counterValue: Long
            synchronized(LOCK) {
                time = getTime() shl RANDOM_BITS
                counterValue = counter.toLong() and counterMask.toLong()
            }
            val nodeValue = node.toLong() shl counterBits
            return TSID(time or nodeValue or counterValue)
        }

        /**
         * Returns the current time.
         *
         * If the current time is equal to the previous time, the counter is incremented
         * by one. Otherwise, the counter is reset to a random value.
         *
         * The maximum number of increment operations depend on the counter bits. For
         * example, if the counter bits is 12, the maximum number of increment
         * operations is 2^12 = 4096.
         *
         * @return the current time
         * @since 1.0.0
         */
        private fun getTime(): Long {
            var time = clock.millis()

            if (time <= lastTime) {
                counter++
                // Carry is 1 if an overflow occurs after ++.
                val carry = counter ushr counterBits
                counter = counter and counterMask
                time = lastTime + carry // increment time
            } else {
                // If the system clock has advanced as expected,
                // simply reset the counter to a new random value.
                counter = getRandomValue()
            }

            // save current time
            lastTime = time

            // adjust to the custom epoch
            return time - customEpoch
        }

        /**
         * Returns a random counter value from 0 to 0x3fffff (2^22-1 = 4,194,303).
         *
         * The counter maximum value depends on the node identifier bits. For example,
         * if the node identifier has 10 bits, the counter has 12 bits.
         *
         * @return a number
         * @since 1.0.0
         */
        private fun getRandomCounter(): Int {
            return if (random is ByteRandom) {
                val bytes = random.nextBytes(randomBytes)

                when (bytes.size) {
                    1 -> (bytes[0].toInt() and 0xff) and counterMask
                    2 -> (((bytes[0].toInt() and 0xff) shl 8) or (bytes[1].toInt() and 0xff)) and counterMask
                    else -> (((bytes[0].toInt() and 0xff) shl 16) or
                            ((bytes[1].toInt() and 0xff) shl 8) or
                            (bytes[2].toInt() and 0xff)) and counterMask
                }
            } else {
                random.nextInt() and counterMask
            }
        }

        /**
         * Returns a random value based on the counter and the current Thread id.
         *
         * @return a number
         * @since 1.0.0
         */
        private fun getRandomValue(): Int {
            val randomCounter = getRandomCounter()
            val threadId = ((Thread.currentThread().threadId().toInt() % 256) shl (counterBits - 8))

            return threadId or (randomCounter shr (counterBits - 8))
        }

        /**
         * A nested class that builds custom TSID factories.
         *
         * It is used to setup a custom [Factory].
         * @since 1.0.0
         */
        class Builder {
            private var node: Int? = null
            private var nodeBits: Int? = null
            private var customEpoch: Long? = null
            private var random: IRandom? = null
            private var clock: Clock? = null

            /**
             * Set the node identifier.
             *
             * @param node a number that must be between 0 and 2^nodeBits-1.
             * @return [Builder]
             * @throws IllegalArgumentException if the node identifier is out of the range
             *                                  [0, 2^nodeBits-1] when `build()` is invoked
             * @since 1.0.0
             */
            fun withNode(node: Int): Builder {
                this.node = node
                return this
            }

            /**
             * Set the node identifier bits length.
             *
             * @param nodeBits a number that must be between 0 and 20.
             * @return [Builder]
             * @throws IllegalArgumentException if the node bits are out of the range [0, 20]
             *                                  when `build()` is invoked
             * @since 1.0.0
             */
            fun withNodeBits(nodeBits: Int): Builder {
                this.nodeBits = nodeBits
                return this
            }

            /**
             * Set the custom epoch.
             *
             * @param customEpoch an instant that represents the custom epoch.
             * @return [Builder]
             * @since 1.0.0
             */
            fun withCustomEpoch(customEpoch: Instant): Builder {
                this.customEpoch = customEpoch.toEpochMilli()
                return this
            }

            /**
             * Set the random generator.
             *
             * The random generator is used to create a random function that is used to
             * reset the counter when the millisecond changes.
             *
             * @param random a [Random] generator
             * @return [Builder]
             * @since 1.0.0
             */
            fun withRandom(random: Random?): Builder {
                random?.let {
                    this.random = if (it is SecureRandom) {
                        ByteRandom(it)
                    } else {
                        IntRandom(it)
                    }
                }
                return this
            }

            /**
             * Set the random function.
             *
             * The random function is used to reset the counter when the millisecond changes.
             *
             * @param randomFunction a random function that returns an integer value
             * @return [Builder]
             * @since 1.0.0
             */
            fun withRandomFunction(randomFunction: () -> Int): Builder {
                random = IntRandom(randomFunction)
                return this
            }

            /**
             * Set the random function.
             *
             * The random function must return a byte array of a given length.
             *
             * The random function is used to reset the counter when the millisecond changes.
             *
             * Despite its name, the random function MAY return a fixed value, for example,
             * if your app requires the counter to be reset to ZERO whenever the millisecond
             * changes, like Twitter Snowflakes, this function should return an array filled
             * with ZEROS.
             *
             * @param randomFunction a random function that returns a byte array
             * @return [Builder]
             * @since 1.0.0
             */
            fun withRandomFunction(randomFunction: (Int) -> ByteArray): Builder {
                random = ByteRandom(randomFunction)
                return this
            }

            /**
             * Set the clock to be used in tests.
             *
             * @param clock a clock
             * @return [Builder]
             * @since 1.0.0
             */
            fun withClock(clock: Clock): Builder {
                this.clock = clock
                return this
            }

            /**
             * Get the node identifier.
             *
             * @return a number
             * @throws IllegalArgumentException if the node is out of range
             * @since 1.0.0
             */
            internal fun getNode(): Int {
                val bits = nodeBits ?: NODE_BITS_1024
                val max = (1 shl bits) - 1

                if (node.isNull()) {
                    node = Settings.getNode() ?: (random!!.nextInt() and max)
                }

                val nodeValue = node!!
                if (nodeValue !in 0..max) {
                    node = Math.floorMod(nodeValue, max)
                }

                return node!!
            }

            /**
             * Get the node identifier bits length within the range 0 to 20.
             *
             * @return a number
             * @throws IllegalArgumentException if the node bits are out of range
             * @since 1.0.0
             */
            internal fun getNodeBits(): Int {
                if (nodeBits.isNull()) {
                    nodeBits = Settings.getNodeCount()?.let {
                        ceil(ln(it.toDouble()) / ln(2.0)).toInt()
                    } ?: NODE_BITS_1024
                }

                val bits = nodeBits!!
                validate(bits in 0..20) { "Node bits out of range [0, 20]: $bits" }

                return bits
            }

            /**
             * Gets the custom epoch.
             *
             * @return a number
             * @since 1.0.0
*/
            internal fun getCustomEpoch() = customEpoch ?: TSID_EPOCH // 2020-01-01

            /**
             * Gets the random generator.
             *
             * @return a random generator
             * @since 1.0.0
*/
            internal fun getRandom(): IRandom {
                if (random.isNull()) {
                    withRandom(SecureRandom())
                }
                return random!!
            }

            /**
             * Gets the clock to be used in tests.
             *
             * @return a clock
             * @since 1.0.0
             */
            internal fun getClock(): Clock {
                if (clock.isNull()) {
                    withClock(Clock.systemUTC())
                }
                return clock!!
            }

            /**
             * Returns a built TSID factory.
             *
             * @return [Factory]
             * @throws IllegalArgumentException if the node is out of range
             * @throws IllegalArgumentException if the node bits are out of range
             * @since 1.0.0
             */
            fun build() = Factory(this)
        }

        interface IRandom {
            fun nextInt(): Int
            fun nextBytes(length: Int): ByteArray
        }

        private class IntRandom(private val randomFunction: () -> Int) : IRandom {

            constructor() : this(newRandomFunction(null))

            constructor(random: Random) : this(newRandomFunction(random))

            override fun nextInt(): Int = randomFunction()

            override fun nextBytes(length: Int): ByteArray {
                var shift = 0
                var randomValue = 0L
                val bytes = ByteArray(length)

                for (i in 0 until length) {
                    if (shift < Byte.SIZE_BITS) {
                        shift = Int.SIZE_BITS
                        randomValue = randomFunction().toLong()
                    }
                    shift -= Byte.SIZE_BITS // 56, 48, 40...
                    bytes[i] = (randomValue ushr shift).toByte()
                }

                return bytes
            }

            companion object {
                fun newRandomFunction(random: Random?): () -> Int {
                    val entropy = random ?: SecureRandom()
                    return { entropy.nextInt() }
                }
            }
        }

        private class ByteRandom(private val randomFunction: (Int) -> ByteArray) : IRandom {

            constructor() : this(newRandomFunction(null))

            constructor(random: Random) : this(newRandomFunction(random))

            override fun nextInt(): Int {
                var number = 0
                val bytes = randomFunction(Int.SIZE_BYTES)
                for (i in 0 until Int.SIZE_BYTES) {
                    number = (number shl 8) or (bytes[i].toInt() and 0xff)
                }
                return number
            }

            override fun nextBytes(length: Int): ByteArray = randomFunction(length)

            companion object {
                fun newRandomFunction(random: Random?): (Int) -> ByteArray {
                    val entropy = random ?: SecureRandom()
                    return { length ->
                        ByteArray(length).apply { entropy.nextBytes(this) }
                    }
                }
            }
        }

        private object Settings {
            const val NODE = "tsid.node"
            const val NODE_COUNT = "tsid.node.count"

            fun getNode(): Int? = getPropertyAsInteger(NODE)

            fun getNodeCount(): Int? = getPropertyAsInteger(NODE_COUNT)

            private fun getPropertyAsInteger(property: String): Int? {
                return try {
                    getProperty(property)?.let { Integer.decode(it) }
                } catch (e: NumberFormatException) {
                    null
                }
            }

            private fun getProperty(name: String): String? {
                val property = System.getProperty(name)
                if (!property.isNullOrEmpty()) {
                    return property
                }

                val variable = System.getenv((+name).replace(".", "_"))
                if (!variable.isNullOrEmpty()) {
                    return variable
                }

                return null
            }
        }
    }
}