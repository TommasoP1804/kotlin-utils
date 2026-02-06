package dev.tommasop1804.kutils.classes.identifiers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.classes.identifiers.NanoID.Companion.DEFAULT_SIZE
import dev.tommasop1804.kutils.classes.identifiers.NanoID.Companion.VALID_ALPHABET
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
import java.security.SecureRandom
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.ln

/**
 * Represents a NanoId, a unique, URL-safe, and non-predictable identifier string.
 *
 * This class provides an inline value class for encapsulating `NanoId` functionality,
 * implementing the [CharSequence] interface to allow basic string operations.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@JsonSerialize(using = NanoID.Companion.Serializer::class)
@JsonDeserialize(using = NanoID.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = NanoID.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = NanoID.Companion.OldDeserializer::class)
@Suppress("unused")
@JvmInline
value class NanoID(val value: String) : CharSequence, Serializable {

    /**
     * Represents the length of the NanoId string.
     *
     * This property provides the total number of characters present in the `value` field of the NanoId instance.
     *
     * @return the number of characters in the NanoId string.
     * @since 1.0.0
     */
    override val length: Int
        get() = value.length

    /**
     * Constructs a new instance of the NanoID class using a randomly generated NanoId string.
     *
     * This constructor allows customization of the random number generator, the set of allowed
     * characters (alphabet), and the desired length of the NanoId. It uses the `randomNanoId`
     * function to generate a NanoId string based on the provided or default parameters.
     *
     * @param random the source of randomness for generating the NanoId. Defaults to an instance of [java.security.SecureRandom].
     * @param alphabet the set of characters that can appear in the generated NanoId. Defaults to [VALID_ALPHABET].
     * @param size the length of the NanoId to generate. Defaults to [DEFAULT_SIZE]. Must be a positive value.
     * @since 1.0.0
     */
    constructor(
        random: Random = SecureRandom(),
        alphabet: CharArray = VALID_ALPHABET,
        size: Int = DEFAULT_SIZE
    ) : this(randomNanoId(random, alphabet, size))

    /**
     * Constructs a new instance of the NanoID class with a randomly generated identifier.
     *
     * The identifier is generated using the `randomNanoId` function with its default parameters, which involve
     * a secure random generator, a predefined valid alphabet, and a default size.
     *
     * @since 1.0.0
     */
    constructor() : this(randomNanoId())

    init {
        validate(value.isValidNanoID()) { "The string is not a valid NanoID" }
    }

    companion object {
        /**
         * Defines a valid set of characters that can be used to generate NanoIDs.
         * The set includes lowercase letters (a-z), uppercase letters (A-Z), numeric digits (0-9),
         * as well as the underscore ('_') and hyphen ('-') characters.
         *
         * This character set is used to ensure that NanoIDs are URL-friendly,
         * unambiguous, and versatile for a wide range of applications.
         *
         * @since 1.0.0
         */
        val VALID_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

        /**
         * Represents the default size for a generated NanoID.
         *
         * The size determines the number of characters that will be included in
         * the generated NanoID string. This default value is used unless another
         * size is explicitly specified during NanoID creation.
         *
         * @since 1.0.0
         */
        const val DEFAULT_SIZE = 21

        /**
         * Validates whether the given string is not blank and consists only of characters
         * within the valid alphabet defined by the NanoID.
         *
         * @return `true` if the string is valid according to the NanoID alphabet and is not blank, otherwise `false`.
         * @since 1.0.0
         */
        fun CharSequence.isValidNanoID() = isNotBlank() && all { it in VALID_ALPHABET }

        /**
         * Converts the current [CharSequence] to a [NanoID] instance, encapsulating this string as its value.
         *
         * This method attempts to create a [NanoID] object from the string representation of the current [CharSequence].
         * The operation is wrapped in a [Result] to handle any exceptions that may occur during instantiation.
         *
         * @receiver the [CharSequence] to be converted into a [NanoID].
         * @return a [Result] containing the [NanoID] instance if the conversion is successful, or the exception if a failure occurs.
         * @since 1.0.0
         */
        fun CharSequence.toNanoID() = runCatching { NanoID(toString()) }

        /**
         * Generates a random NanoId string using the specified random generator, character alphabet, and size.
         *
         * @param random the source of randomness for generating the NanoId. Defaults to a new instance of [Random].
         * @param alphabet the set of characters that can appear in the generated NanoId. Must not be empty.
         * @param size the length of the NanoId to generate. Defaults to 21. Must be a positive value.
         * @return a randomly generated NanoId string with the specified parameters.
         * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if the alphabet is empty or contains more than 255 characters.
         * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if the size is not positive.
         * @since 1.0.0
         */
        private fun randomNanoId(
            random: Random = SecureRandom(),
            alphabet: CharArray = VALID_ALPHABET,
            size: Int = DEFAULT_SIZE
        ): String {
            validate(!(alphabet.isEmpty() || alphabet.size >= 256)) { "alphabet must contain between 1 and 255 symbols." }
            validate(size > 0) { "size must be greater than zero." }

            val mask = (2 shl floor(ln((alphabet.size - 1).toDouble()) / ln(2.0)).toInt()) - 1
            val step = ceil(1.6 * mask * size / alphabet.size).toInt()
            val idBuilder = StringBuilder()

            while (true) {
                val bytes = ByteArray(step)
                random.nextBytes(bytes)

                for (i in 0..<step) {
                    val alphabetIndex = bytes[i].toInt() and mask
                    if (alphabetIndex < alphabet.size) {
                        idBuilder.append(alphabet[alphabetIndex])
                        if (idBuilder.length == size) return idBuilder.toString()
                    }
                }
            }
        }

        class Serializer : ValueSerializer<NanoID>() {
            override fun serialize(value: NanoID, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<NanoID>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = NanoID(p.string)
        }

        class OldSerializer : JsonSerializer<NanoID>() {
            override fun serialize(value: NanoID, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<NanoID>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): NanoID = NanoID(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<NanoID?, String?> {
            override fun convertToDatabaseColumn(attribute: NanoID?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): NanoID? = dbData?.let { NanoID(it) }
        }

        class Type : EnhancedUserType<NanoID> {
            override fun getSqlType(): Int = SqlTypes.VARCHAR

            override fun returnedClass(): Class<NanoID> = NanoID::class.java

            override fun equals(
                x: NanoID?,
                y: NanoID?
            ): Boolean = x == y

            override fun hashCode(x: NanoID?): Int = x?.hashCode() ?: 0

            override fun nullSafeGet(
                rs: ResultSet?,
                position: Int,
                session: SharedSessionContractImplementor?,
                owner: Any?
            ): NanoID? {
                val value = rs?.getString(position) ?: return null
                return NanoID(value)
            }

            override fun nullSafeSet(
                st: PreparedStatement?,
                value: NanoID?,
                index: Int,
                session: SharedSessionContractImplementor?
            ) {
                st?.setString(index, value?.value) ?: throw IllegalArgumentException("Statement cannot be null")
            }

            override fun deepCopy(value: NanoID?): NanoID? = value?.let { NanoID(it.value) }

            override fun isMutable(): Boolean = false

            override fun disassemble(value: NanoID?): Serializable? = deepCopy(value)

            override fun assemble(
                cached: Serializable?,
                owner: Any?
            ): NanoID? = cached as? NanoID

            override fun toSqlLiteral(value: NanoID?): String? = value?.let { "'${it.value}'" }

            override fun toString(value: NanoID?): String? = value?.value

            override fun fromStringValue(sequence: CharSequence?): NanoID =
                sequence?.let { NanoID(it.toString()) } ?: throw IllegalArgumentException("Cannot convert null to NanoID")
        }
    }

    /**
     * Returns the character at the specified [index] of the NanoId string.
     *
     * @param index the position of the character to be returned, where the first character is at index 0.
     * @return the character at the specified [index].
     * @throws IndexOutOfBoundsException if [index] is less than 0 or greater than or equal to the length of the string.
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this character sequence, starting at the specified
     * [startIndex] (inclusive) and ending at the specified [endIndex] (exclusive).
     *
     * The returned sequence shares the same underlying data structure and memory as the original sequence,
     * but represents only the specified range of characters.
     *
     * @param startIndex the beginning index, inclusive. Must be non-negative and less than or equal to [endIndex].
     * @param endIndex the ending index, exclusive. Must be greater than or equal to [startIndex] and no greater than
     * the length of this sequence.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns a string representation of the NanoId object.
     *
     * The representation is equivalent to the value of the `value` property
     * within the NanoId instance.
     *
     * @return a string representation of the NanoId.
     * @since 1.0.0
     */
    override fun toString() = value
}