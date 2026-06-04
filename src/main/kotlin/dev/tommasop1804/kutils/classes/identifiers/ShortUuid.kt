/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.identifiers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.BigInt
import dev.tommasop1804.kutils.Uuid
import dev.tommasop1804.kutils.invoke
import dev.tommasop1804.kutils.isNull
import dev.tommasop1804.kutils.toUuid
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
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.repeat
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

/**
 * Represents a compact, URL-safe and human-readable identifier derived from a UUID.
 * This value class holds a shortened string representation of a UUID
 * while providing methods for encoding and decoding UUID data.
 *
 * It includes integrations for JSON serialization/deserialization
 * and persistence mapping.
 *
 * @param value The string representation of the short UUID.
 * @constructor Constructs a ShortUUID from a given string representation.
 * @author Tommaso Pastorelli
 * @since 3.0.0
 */
@JsonSerialize(using = ShortUuid.Companion.Serializer::class)
@JsonDeserialize(using = ShortUuid.Companion.Deserializer::class)
@JvmInline
@Suppress("unused", "kutils_substring_as_get_intprogression", "kutils_take_as_int_invoke")
@MustUseReturnValues
value class ShortUuid(private val value: String) : Serializable, CharSequence {

    /**
     * Provides the length of the value string associated with the ShortUUID instance.
     *
     * This property is computed dynamically and represents the number of characters
     * in the `value` string.
     *
     * @return The length of the value string.
     * @since 3.0.0
     */
    override val length: Int get() = value.length

    /**
     * Creates an instance of the [ShortUuid] class using a [UUID].
     *
     * This constructor encodes the given [UUID] into a shortened string format
     * using a custom alphabet. The encoding process is performed by converting
     * the UUID into a [BigInt], removing non-numeric characters, and encoding
     * it using a specific length calculated based on the size of the custom alphabet.
     *
     * @param uuid the UUID to encode into a shortened string.
     * @since 3.0.0
     */
    constructor(uuid: Uuid) : this(encode(
        BigInt(uuid.toString().replace("-", ""), 16),
        ceil((ln(25.0) / ln(ALPHABET.size.toDouble())) * 16).toInt()
    ))

    /**
     * Default constructor for the ShortUUID class.
     *
     * Constructs a new ShortUUID instance by generating a random UUID.
     * This constructor facilitates the creation of a ShortUUID with no prior inputs, leveraging
     * the underlying default UUID generation mechanism.
     *
     * @since 3.0.0
     */
    constructor() : this(Uuid())

    /**
     * Companion object for the ShortUUID class. Provides utility methods and nested classes
     * for serialization, deserialization, string encoding/decoding, and database conversions.
     *
     * @since 3.0.0
     */
    companion object {
        /**
         * Represents the set of characters used in the creation of short UUIDs.
         * The selected characters exclude visually similar ones (e.g., '0' and 'O', '1' and 'l')
         * to prevent confusion and improve readability.
         *
         * This array can be utilized to generate unique identifiers or for any use case
         * requiring a restricted and distinctive character set.
         *
         * @since 3.0.0
         */
        private val ALPHABET = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray()

        /**
         * Encodes a given BigInt into a custom representation using a predefined alphabet, and optionally
         * pads the result to a specified length.
         *
         * @param bigInt The BigInt value to be encoded.
         * @param padToLen The length to which the encoded result should be padded. If set to 0, no padding will occur.
         * @since 3.0.0
         */
        private fun encode(bigInt: BigInt, padToLen: Int) = buildString {
            var value = BigInt(bigInt.toString())
            val alphaSize = BigInt.valueOf(ALPHABET.size.toLong())

            while (value > BigInt.ZERO) {
                val fracAndRemainder = value.divideAndRemainder(alphaSize)
                append(ALPHABET[fracAndRemainder[1].toInt()])
                value = fracAndRemainder[0]
            }

            if (padToLen > 0) {
                val padding = (padToLen - length).coerceAtLeast(0)
                repeat(padding) { append(ALPHABET[0]) }
            }
        }

        /**
         * Decodes the given encoded character array into a readable UUID string format.
         *
         * The decoding process translates the input array of characters, based on a predefined ALPHABET,
         * into a UUID string. The resultant string is formatted into the structure of five segments separated by dashes:
         * 8 characters-4 characters-4 characters-4 characters-12 characters, following UUID conventions.
         *
         * @param encoded A character array containing the encoded representation of the UUID.
         * @since 3.0.0
         */
        private fun decode(encoded: CharArray) = buildString{
            var sum = BigInt.ZERO
            val alphaSize = BigInt.valueOf(ALPHABET.size.toLong())

            for ([i, element] in encoded.withIndex()) {
                sum = sum.add(alphaSize.pow(i).multiply(BigInt.valueOf(
                    Arrays.binarySearch(ALPHABET, element).toLong()
                )))
            }
            var str = sum.toString(16)
            if (str.length < 32) str = String.format("%32s", str).replace(' ', '0')

            append(str.take(8))
            append("-")
            append(str.substring(8, 12))
            append("-")
            append(str.substring(12, 16))
            append("-")
            append(str.substring(16, 20))
            append("-")
            append(str.substring(20, 32))
        }

        /**
         * Converts this [UUID] into its shortened [ShortUuid] representation.
         *
         * This extension function utilizes the [ShortUuid] class constructor to generate
         * a compact, encoded string representation of the UUID. The resulting [ShortUuid]
         * provides a more concise alternative format for the UUID.
         *
         * @return A [ShortUuid] instance representing the shortened form of the original UUID.
         * @since 3.0.0
         */
        fun UUID.toShortUuid() = ShortUuid(this)
        /**
         * Converts a [CharSequence] into a `ShortUUID` representation.
         *
         * This extension function constructs a `ShortUUID` object from the current [CharSequence].
         * A `ShortUUID` provides a shortened representation of a UUID string.
         *
         * @receiver The [CharSequence] to be converted into a `ShortUUID`.
         * @return A `ShortUUID` instance representing the shortened UUID.
         * @since 3.0.0
         */
        fun CharSequence.toShortUuid() = ShortUuid(toString())

        class Serializer : ValueSerializer<ShortUuid>() {
            override fun serialize(
                value: ShortUuid,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<ShortUuid>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = ShortUuid(p.string)
        }

        class OldSerializer : JsonSerializer<ShortUuid>() {
            override fun serialize(value: ShortUuid, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<ShortUuid>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): ShortUuid = ShortUuid(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<ShortUuid?, UUID?> {
            override fun convertToDatabaseColumn(attribute: ShortUuid?): UUID? = attribute?.value?.toUuid()()
            override fun convertToEntityAttribute(dbData: UUID?): ShortUuid? = dbData?.toShortUuid()
        }

        class TypeVarchar : EnhancedUserType<ShortUuid> {
            override fun getSqlType(): Int = SqlTypes.VARCHAR

            override fun returnedClass(): Class<ShortUuid> = ShortUuid::class.java

            override fun equals(
                x: ShortUuid?,
                y: ShortUuid?
            ): Boolean = x == y

            override fun hashCode(x: ShortUuid?): Int = x?.hashCode() ?: 0

            override fun nullSafeGet(
                rs: ResultSet?,
                position: Int,
                session: SharedSessionContractImplementor?,
                owner: Any?
            ): ShortUuid? {
                val value = rs?.getString(position) ?: return null
                return ShortUuid(value)
            }

            override fun nullSafeSet(
                st: PreparedStatement?,
                value: ShortUuid?,
                index: Int,
                session: SharedSessionContractImplementor?
            ) {
                st?.setString(index, value?.value)
            }

            override fun deepCopy(value: ShortUuid?): ShortUuid? = value?.let { ShortUuid(it.value) }

            override fun isMutable(): Boolean = false

            override fun disassemble(value: ShortUuid?): Serializable? = deepCopy(value)

            override fun assemble(
                cached: Serializable?,
                owner: Any?
            ): ShortUuid? = when (cached) {
                is ShortUuid -> cached
                is String -> ShortUuid(cached)
                else -> null
            }

            override fun toSqlLiteral(value: ShortUuid?): String? = value?.let { "'${it.value}'" }

            override fun toString(value: ShortUuid?): String? = value?.value

            override fun fromStringValue(sequence: CharSequence?): ShortUuid =
                sequence?.let { ShortUuid(it.toString()) } ?: throw IllegalArgumentException("Cannot convert null to ShortUUID")
        }

        class TypeUuid : EnhancedUserType<ShortUuid> {
            override fun getSqlType(): Int = SqlTypes.UUID

            override fun returnedClass(): Class<ShortUuid> = ShortUuid::class.java

            override fun equals(
                x: ShortUuid?,
                y: ShortUuid?
            ): Boolean = x == y

            override fun hashCode(x: ShortUuid?): Int = x?.hashCode() ?: 0

            override fun nullSafeGet(
                rs: ResultSet?,
                position: Int,
                session: SharedSessionContractImplementor?,
                owner: Any?
            ): ShortUuid? {
                val value = rs?.getObject(position, UUID::class.java) ?: return null
                return ShortUuid(value)
            }

            override fun nullSafeSet(
                st: PreparedStatement?,
                value: ShortUuid?,
                index: Int,
                session: SharedSessionContractImplementor?
            ) {
                if (value.isNull()) {
                    st?.setNull(index, SqlTypes.UUID)
                } else {
                    st?.setObject(index, value.toUuid())
                }
            }

            override fun deepCopy(value: ShortUuid?): ShortUuid? = value?.let { ShortUuid(it.toUuid()) }

            override fun isMutable(): Boolean = false

            override fun disassemble(value: ShortUuid?): Serializable? = deepCopy(value)

            override fun assemble(
                cached: Serializable?,
                owner: Any?
            ): ShortUuid? = cached as? ShortUuid

            override fun toSqlLiteral(value: ShortUuid?): String? = value?.let { "'${it.toUuid()}'" }

            override fun toString(value: ShortUuid?): String? = value?.toString()

            override fun fromStringValue(sequence: CharSequence?): ShortUuid =
                sequence?.let { ShortUuid(it.toUuid()().toString()) } ?: throw IllegalArgumentException("Cannot convert null to ShortUUID")
        }
    }

    /**
     * Returns a string representation of the object. This method is typically
     * overridden to provide a meaningful string representation of the object
     * based on its internal state.
     *
     * @return A string representation of the object.
     * @since 3.0.0
     */
    override fun toString() = value

    /**
     * Retrieves the character at the specified index from the internal value.
     *
     * @param index The position of the character to retrieve. Must be a valid index within the range of the internal value.
     * @return The character at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     * @since 3.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of the current sequence.
     * The subsequence starts at the specified [startIndex] and ends right before the specified [endIndex].
     *
     * @param startIndex The beginning index, inclusive. Must be non-negative and less than or equal to [endIndex].
     * @param endIndex The ending index, exclusive. Must be greater than or equal to [startIndex] and less than or equal to the length of the sequence.
     * @return A new character sequence that is a subsequence of this sequence.
     * @since 3.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Decodes a given shortened UUID string into its full UUID representation.
     *
     * @receiver The shortened UUID string to decode.
     * @since 3.0.0
     */
    fun toUuid(): Uuid = Uuid(decode(value.toCharArray()))

    /**
     * Decodes a given shortened UUID string into its full UUID representation.
     *
     * @receiver The shortened UUID string to decode.
     * @since 3.0.0
     */
    @OptIn(ExperimentalUuidApi::class)
    fun toKotlinUuid() = Uuid(decode(value.toCharArray())).toKotlinUuid()
}