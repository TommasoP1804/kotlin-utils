package dev.tommasop1804.kutils.classes.identifiers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.BigInt
import dev.tommasop1804.kutils.UUID
import dev.tommasop1804.kutils.Uuid
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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

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
 * @since 1.0.0
 */
@JsonSerialize(using = ShortUUID.Companion.Serializer::class)
@JsonDeserialize(using = ShortUUID.Companion.Deserializer::class)
@JvmInline
@Suppress("unused", "kutils_substring_as_get_intprogression", "kutils_take_as_int_invoke")
value class ShortUUID(val value: String) : Serializable {
    /**
     * Creates an instance of the [ShortUUID] class using a [UUID].
     *
     * This constructor encodes the given [UUID] into a shortened string format
     * using a custom alphabet. The encoding process is performed by converting
     * the UUID into a [BigInt], removing non-numeric characters, and encoding
     * it using a specific length calculated based on the size of the custom alphabet.
     *
     * @param uuid the UUID to encode into a shortened string.
     * @since 1.0.0
     */
    constructor(uuid: UUID) : this(encode(
        BigInt(uuid.toString().replace("-", ""), 16),
        ceil((ln(25.0) / ln(ALPHABET.size.toDouble())) * 16).toInt()
    ))

    /**
     * Creates an instance of the [ShortUUID] class using a [Uuid].
     *
     * This constructor encodes the given [Uuid] into a shortened string format
     * using a custom alphabet. The encoding process is performed by converting
     * the UUID into a [BigInt], removing non-numeric characters, and encoding
     * it using a specific length calculated based on the size of the custom alphabet.
     *
     * @param uuid the UUID to encode into a shortened string.
     * @since 1.0.0
     */
    @OptIn(ExperimentalUuidApi::class)
    constructor(uuid: Uuid) : this(uuid.toJavaUuid())

    /**
     * Default constructor for the ShortUUID class.
     *
     * Constructs a new ShortUUID instance by generating a random UUID.
     * This constructor facilitates the creation of a ShortUUID with no prior inputs, leveraging
     * the underlying default UUID generation mechanism.
     *
     * @since 1.0.0
     */
    constructor() : this(UUID())

    /**
     * Companion object for the ShortUUID class. Provides utility methods and nested classes
     * for serialization, deserialization, string encoding/decoding, and database conversions.
     *
     * @since 1.0.0
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
         * @since 1.0.0
         */
        private val ALPHABET = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray()

        /**
         * Encodes a given BigInt into a custom representation using a predefined alphabet, and optionally
         * pads the result to a specified length.
         *
         * @param bigInt The BigInt value to be encoded.
         * @param padToLen The length to which the encoded result should be padded. If set to 0, no padding will occur.
         * @since 1.0.0
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
         * @since 1.0.0
         */
        private fun decode(encoded: CharArray) = buildString{
            var sum = BigInt.ZERO
            val alphaSize = BigInt.valueOf(ALPHABET.size.toLong())

            for (i in 0 until encoded.size) {
                sum = sum.add(alphaSize.pow(i).multiply(BigInt.valueOf(
                    Arrays.binarySearch(ALPHABET, encoded[i]).toLong()
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
         * Converts this [UUID] into its shortened [ShortUUID] representation.
         *
         * This extension function utilizes the [ShortUUID] class constructor to generate
         * a compact, encoded string representation of the UUID. The resulting [ShortUUID]
         * provides a more concise alternative format for the UUID.
         *
         * @return A [ShortUUID] instance representing the shortened form of the original UUID.
         * @since 1.0.0
         */
        fun UUID.toShortUUID() = ShortUUID(this)
        /**
         * Converts this [UUID] into its shortened [ShortUUID] representation.
         *
         * This extension function utilizes the [ShortUUID] class constructor to generate
         * a compact, encoded string representation of the UUID. The resulting [ShortUUID]
         * provides a more concise alternative format for the UUID.
         *
         * @return A [ShortUUID] instance representing the shortened form of the original UUID.
         * @since 1.0.0
         */
        @OptIn(ExperimentalUuidApi::class)
        fun Uuid.toShortUuid() = ShortUUID(this)
        /**
         * Converts a [CharSequence] into a `ShortUUID` representation.
         *
         * This extension function constructs a `ShortUUID` object from the current [CharSequence].
         * A `ShortUUID` provides a shortened representation of a UUID string.
         *
         * @receiver The [CharSequence] to be converted into a `ShortUUID`.
         * @return A `ShortUUID` instance representing the shortened UUID.
         * @since 1.0.0
         */
        fun CharSequence.toShortUUID() = ShortUUID(toString())

        class Serializer : ValueSerializer<ShortUUID>() {
            override fun serialize(
                value: ShortUUID,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<ShortUUID>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = ShortUUID(p.string)
        }

        class OldSerializer : JsonSerializer<ShortUUID>() {
            override fun serialize(value: ShortUUID, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<ShortUUID>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): ShortUUID = ShortUUID(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<ShortUUID?, String?> {
            override fun convertToDatabaseColumn(attribute: ShortUUID?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): ShortUUID? = dbData?.let { ShortUUID(it) }
        }

        class Type : EnhancedUserType<ShortUUID> {
            override fun getSqlType(): Int = SqlTypes.VARCHAR

            override fun returnedClass(): Class<ShortUUID> = ShortUUID::class.java

            override fun equals(
                x: ShortUUID?,
                y: ShortUUID?
            ): Boolean = x == y

            override fun hashCode(x: ShortUUID?): Int = x?.hashCode() ?: 0

            override fun nullSafeGet(
                rs: ResultSet?,
                position: Int,
                session: SharedSessionContractImplementor?,
                owner: Any?
            ): ShortUUID? {
                val value = rs?.getString(position) ?: return null
                return ShortUUID(value)
            }

            override fun nullSafeSet(
                st: PreparedStatement?,
                value: ShortUUID?,
                index: Int,
                session: SharedSessionContractImplementor?
            ) {
                st?.setString(index, value?.value)
            }

            override fun deepCopy(value: ShortUUID?): ShortUUID? = value?.let { ShortUUID(it.value) }

            override fun isMutable(): Boolean = false

            override fun disassemble(value: ShortUUID?): Serializable? = deepCopy(value)

            override fun assemble(
                cached: Serializable?,
                owner: Any?
            ): ShortUUID? = cached as? ShortUUID

            override fun toSqlLiteral(value: ShortUUID?): String? = value?.let { "'${it.value}'" }

            override fun toString(value: ShortUUID?): String? = value?.value

            override fun fromStringValue(sequence: CharSequence?): ShortUUID =
                sequence?.let { ShortUUID(it.toString()) } ?: throw IllegalArgumentException("Cannot convert null to ShortUUID")
        }
    }

    /**
     * Returns a string representation of the object. This method is typically
     * overridden to provide a meaningful string representation of the object
     * based on its internal state.
     *
     * @return A string representation of the object.
     * @since 1.0.0
     */
    override fun toString() = value

    /**
     * Decodes a given shortened UUID string into its full UUID representation.
     *
     * @receiver The shortened UUID string to decode.
     * @since 1.0.0
     */
    fun toUUID() = UUID(decode(value.toCharArray()))

    /**
     * Decodes a given shortened UUID string into its full UUID representation.
     *
     * @receiver The shortened UUID string to decode.
     * @since 1.0.0
     */
    @OptIn(ExperimentalUuidApi::class)
    fun toKotlinUuid() = Uuid(decode(value.toCharArray()))
}