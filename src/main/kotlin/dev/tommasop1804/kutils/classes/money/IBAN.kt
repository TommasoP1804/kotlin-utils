package dev.tommasop1804.kutils.classes.money

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.geography.Country
import dev.tommasop1804.kutils.classes.geography.Country.*
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import kotlin.reflect.KProperty

/**
 * Represents an International Bank Account Number (IBAN).
 *
 * Encapsulates the structure and validation of an IBAN value. The class ensures that the provided
 * IBAN adheres to the correct format, including country-specific length restrictions and content validation.
 * It also offers utility methods and features to work with its properties, such as country code,
 * check digits, and BBAN (Basic Bank Account Number).
 *
 * This is a value class that implements the `CharSequence` interface to allow character-based operations.
 * Instances of this class are immutable.
 *
 * @param value The IBAN string that satisfies the validation criteria, such as correct country and format.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JvmInline
@JsonSerialize(using = IBAN.Companion.Serializer::class)
@JsonDeserialize(using = IBAN.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = IBAN.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = IBAN.Companion.OldDeserializer::class)
@Suppress("unused")
value class IBAN private constructor(val value: String) : CharSequence {
    /**
     * Provides the length of the IBAN value.
     *
     * This property retrieves the number of characters in the internal IBAN `value`.
     * It is a read-only override and ensures access to the standard length information
     * of the IBAN format. This is primarily based on the associated `value` field
     * representing the IBAN's string data.
     *
     * @return The count of characters in the IBAN value.
     * @since 1.0.0
     */
    override val length
        get() = value.length

    /**
     * Represents the country information that is retrieved or associated with
     * its corresponding two-character ISO 3166-1 alpha-2 country code.
     *
     * This property provides a getter to obtain the country object based on the specified value.
     *
     * @since 1.0.0
     */
    val country
        get() = Country ofAlpha2 2(value)

    /**
     * Retrieves the check digits of the IBAN.
     *
     * This property provides access to the two-character string representing the check digits
     * of the International Bank Account Number (IBAN). The check digits are located at the
     * 3rd and 4th positions of the IBAN value and are used to verify the integrity of the IBAN.
     *
     * @return A string representing the two check digits of the IBAN.
     * @since 1.0.0
     */
    val checkDigits
        get() = value[2..3]

    /**
     * Retrieves the Basic Bank Account Number (BBAN) component of the IBAN.
     *
     * This property represents the BBAN portion of the IBAN, which typically contains
     * bank-specific and branch-specific information used to identify an account
     * within a specific banking context. The BBAN is derived by dropping the country
     * code and check digits from the IBAN value. The length and format of the BBAN
     * depend on the country-specific IBAN standards.
     *
     * @return The BBAN component of the IBAN as a string.
     * @throws IllegalStateException If the `value` of the IBAN is not initialized or invalid.
     * @since 1.0.0
     */
    val bban
        get() = (-4)(value)

    /**
     * Constructs an IBAN instance by processing the provided [value].
     *
     * This constructor processes the given `CharSequence` by removing all
     * occurrences of space characters, converting the result to uppercase,
     * and delegating the processed value to another constructor.
     *
     * @param value The input character sequence representing the IBAN value,
     *              which may contain spaces.
     * @since 1.0.0
     */
    constructor(value: CharSequence) : this(+(value.toString() - Char.SPACE))

    init {
        (country.isNull() || value.length == when (country) {
            ALBANIA -> 28
            ANDORRA -> 24
            AUSTRIA -> 20
            AZERBAIJAN -> 28
            BAHRAIN -> 22
            BELARUS -> 28
            BELGIUM -> 16
            BOSNIA_AND_HERZEGOVINA -> 20
            BRAZIL -> 29
            BULGARIA -> 22
            BURUNDI -> 21
            COSTA_RICA -> 22
            CROATIA -> 21
            CYPRUS -> 28
            CZECH_REPUBLIC -> 24
            DENMARK -> 18
            DJIBOUTI -> 27
            DOMINICAN_REPUBLIC -> 28
            TIMOR_LESTE -> 23
            EGYPT -> 29
            EL_SALVADOR -> 28
            ESTONIA -> 20
            FALKLAND_ISLANDS -> 18
            FAROE_ISLANDS -> 18
            FINLAND -> 18
            FRANCE,
            FRENCH_GUIANA,
            FRENCH_POLYNESIA,
            FRENCH_SOUTHERN_TERRITORIES,
            GUADELOUPE,
            MARTINIQUE,
            MAYOTTE,
            NEW_CALEDONIA,
            REUNION,
            SAINT_BARTHELEMY,
            SAINT_MARTIN,
            SAINT_PIERRE_AND_MIQUELON,
            WALLIS_AND_FUTUNA -> 27

            GEORGIA -> 22
            GERMANY -> 22
            GIBRALTAR -> 23
            GREECE -> 27
            GREENLAND -> 18
            GUATEMALA -> 28
            HONDURAS -> 28
            HUNGARY -> 28
            ICELAND -> 26
            IRAQ -> 23
            IRELAND -> 22
            ISRAEL -> 23
            ITALY -> 27
            JORDAN -> 30
            KAZAKHSTAN -> 20
            KOSOVO -> 20
            KUWAIT -> 30
            LATVIA -> 21
            LEBANON -> 28
            LIBYA -> 25
            LIECHTENSTEIN -> 21
            LITHUANIA -> 20
            LUXEMBOURG -> 20
            MALTA -> 31
            MAURITANIA -> 27
            MAURITIUS -> 30
            MONACO -> 27
            MOLDOVA -> 24
            MONGOLIA -> 20
            MONTENEGRO -> 22
            NETHERLANDS -> 18
            NICARAGUA -> 28
            NORTH_MACEDONIA -> 19
            NORWAY -> 15
            OMAN -> 23
            PAKISTAN -> 24
            PALESTINE -> 29
            POLAND -> 28
            PORTUGAL -> 25
            QATAR -> 29
            ROMANIA -> 24
            RUSSIAN_FEDERATION -> 33
            SAINT_LUCIA -> 32
            SAN_MARINO -> 27
            SAO_TOME_AND_PRINCIPE -> 25
            SAUDI_ARABIA -> 24
            SERBIA -> 22
            SEYCHELLES -> 31
            SLOVAKIA -> 24
            SLOVENIA -> 19
            SOMALIA -> 23
            SPAIN -> 24
            SUDAN -> 18
            SWEDEN -> 24
            SWITZERLAND -> 21
            TUNISIA -> 24
            TURKEY -> 26
            UKRAINE -> 29
            UNITED_ARAB_EMIRATES -> 23
            UNITED_KINGDOM, ISLE_OF_MAN, GUERNSEY, JERSEY -> 22
            VATICAN_CITY -> 22
            BRITISH_VIRGIN_ISLANDS -> 24
            YEMEN -> 30
            else -> value.length
        }) && Regex("^[A-Z]{2}[0-9]{2}[0-9A-Z]{1,30}$")(value)
                || throw MalformedInputException("Invalid IBAN format")

        var checkDigits = (
                98 - (bban + 2(value) + "00")
                    .map { EUROPEAN_CHECK_DIGITS_CONVERSION[it]!! }
                    .joinToString(String.EMPTY)
                    .toBigInt()()
                    .mod(97.toBigInt()).toInt()
                ).toString()
        if (checkDigits.length == 1) checkDigits = "0$checkDigits"
        checkDigits.expect(value[2..3]) { "Invalid check digits for IBAN" }

        if (country == ITALY) {
            Regex("^[A-Z]{2}[0-9]{2}[A-Z][0-9]{10}[0-9A-Z]{12}$")(value) || throw MalformedInputException("Invalid IBAN format for Italy")
            val odd = (-5)(value).filterIndexed { index, _ -> index.isEven }.map {
                when (it) {
                    '0' -> 1
                    '1' -> 0
                    '2' -> 5
                    '3' -> 7
                    '4' -> 9
                    '5' -> 13
                    '6' -> 15
                    '7' -> 17
                    '8' -> 19
                    '9' -> 21
                    'A' -> 1
                    'B' -> 0
                    'C' -> 5
                    'D' -> 7
                    'E' -> 9
                    'F' -> 13
                    'G' -> 15
                    'H' -> 17
                    'I' -> 19
                    'J' -> 21
                    'K' -> 2
                    'L' -> 4
                    'M' -> 18
                    'N' -> 20
                    'O' -> 11
                    'P' -> 3
                    'Q' -> 6
                    'R' -> 8
                    'S' -> 12
                    'T' -> 14
                    'U' -> 16
                    'V' -> 10
                    'W' -> 22
                    'X' -> 25
                    'Y' -> 24
                    'Z' -> 23
                    else -> throw Error()
                }
            }
            val even = (-5)(value).filterIndexed { index, _ -> index.isOdd }.map {
                when (it) {
                    '0' -> 0
                    '1' -> 1
                    '2' -> 2
                    '3' -> 3
                    '4' -> 4
                    '5' -> 5
                    '6' -> 6
                    '7' -> 7
                    '8' -> 8
                    '9' -> 9
                    'A' -> 0
                    'B' -> 1
                    'C' -> 2
                    'D' -> 3
                    'E' -> 4
                    'F' -> 5
                    'G' -> 6
                    'H' -> 7
                    'I' -> 8
                    'J' -> 9
                    'K' -> 10
                    'L' -> 11
                    'M' -> 12
                    'N' -> 13
                    'O' -> 14
                    'P' -> 15
                    'Q' -> 16
                    'R' -> 17
                    'S' -> 18
                    'T' -> 19
                    'U' -> 20
                    'V' -> 21
                    'W' -> 22
                    'X' -> 23
                    'Y' -> 24
                    'Z' -> 25
                    else -> throw Error()
                }
            }
            val result = ('A'..'Z').toList()[(odd.sum() + even.sum()).mod(26)]
            result.expect(value[4]) { "Invalid Italian IBAN check digit" }
        }
    }

    companion object {
        /**
         * A map defining the conversion of alphanumeric characters to their corresponding numeric
         * values according to the European check digits system.
         *
         * This map is commonly employed in the context of IBAN or similar international systems where
         * alphanumeric codes are used and need to be converted into numeric form for validation purposes.
         *
         * The map includes:
         * - Numerical characters ('0' to '9') mapped to their integer equivalents.
         * - Uppercase alphabetical characters ('A' to 'Z') mapped to integers starting from 10 up to 35.
         *
         * @since 1.0.0
         */
        private val EUROPEAN_CHECK_DIGITS_CONVERSION = mapOf(
            '0' to 0,
            '1' to 1,
            '2' to 2,
            '3' to 3,
            '4' to 4,
            '5' to 5,
            '6' to 6,
            '7' to 7,
            '8' to 8,
            '9' to 9,
            'A' to 10,
            'B' to 11,
            'C' to 12,
            'D' to 13,
            'E' to 14,
            'F' to 15,
            'G' to 16,
            'H' to 17,
            'I' to 18,
            'J' to 19,
            'K' to 20,
            'L' to 21,
            'M' to 22,
            'N' to 23,
            'O' to 24,
            'P' to 25,
            'Q' to 26,
            'R' to 27,
            'S' to 28,
            'T' to 29,
            'U' to 30,
            'V' to 31,
            'W' to 32,
            'X' to 33,
            'Y' to 34,
            'Z' to 35
        )

        /**
         * Validates whether the current string is a valid IBAN format.
         *
         * This method attempts to create an instance of the `IBAN` class using the current string.
         * If the string does not meet the requirements for a valid IBAN, the operation will fail
         * and return `false`.
         *
         * @receiver The string to be validated as an IBAN.
         * @return `true` if the string is successfully validated as an IBAN format; `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidIBAN() = runCatching { IBAN(this) }.isSuccess

        /**
         * Attempts to convert the current string into an IBAN instance.
         *
         * This method uses the `IBAN` class constructor to validate and transform the string
         * into an IBAN object. Any exceptions during the process are handled,
         * and the result is wrapped within a `Result` object.
         *
         * @receiver The string to be converted into an IBAN.
         * @return A `Result` containing the successfully created IBAN instance or an exception
         *         if the conversion fails.
         * @since 1.0.0
         */
        fun CharSequence.toIBAN() = runCatching { IBAN(this) }

        class Serializer : ValueSerializer<IBAN>() {
            override fun serialize(value: IBAN, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<IBAN>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = IBAN(p.string)
        }

        class OldSerializer : JsonSerializer<IBAN>() {
            override fun serialize(value: IBAN, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<IBAN>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): IBAN = IBAN(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<IBAN?, String?> {
            override fun convertToDatabaseColumn(attribute: IBAN?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): IBAN? = dbData?.let { IBAN(it) }
        }
    }

    /**
     * Converts the IBAN instance's key properties into a map representation.
     *
     * This method creates a `Map` containing the key attributes of the IBAN object
     * including `country`, `checkDigits`, and `bban`, mapping their names to their values.
     *
     * @return A map with the keys "country", "checkDigits", and "bban", corresponding to the values of the
     * respective properties in the IBAN.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "country" to country,
        "checkDigits" to checkDigits,
        "bban" to bban
    )

    /**
     * Retrieves the value associated with a property name from the internal map.
     *
     * This operator function allows dynamic access to properties of the class by their names, as defined
     * in the internal map returned by `_toMap`. The function attempts to cast the retrieved value to the
     * expected type parameter [R].
     *
     * - `country` - TYPE: [Country]
     * - `checkDigits` - TYPE: [String]
     * - `bban` - TYPE: [String]
     *
     * @param R The expected type of the value retrieved for the property.
     * @param thisRef The instance of the class from which this method is called. It can be `null` if not relevant.
     * @param property The property whose name is used to retrieve the value from the map.
     * @throws NoSuchElementException If the specified property name does not exist in the map.
     * @throws ClassCastException If the retrieved value cannot be cast to the expected type [R].
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R

    /**
     * Retrieves the character at the specified position in the IBAN value.
     *
     * This method provides access to a specific character in the internal `value` of the IBAN based on
     * the given index. The index is zero-based and must be within the bounds of the `value` length.
     *
     * @param index The zero-based position of the character to retrieve. Must be in the range
     * of `0` to `length - 1` where `length` is the number of characters in the IBAN's value.
     * @throws IndexOutOfBoundsException If the [index] is less than 0 or greater than or equal to the length of the IBAN value.
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of the original sequence.
     *
     * @param startIndex the start index (inclusive) of the subsequence, must be within the bounds of the sequence.
     * @param endIndex the end index (exclusive) of the subsequence, must be within the bounds of the sequence and
     *                 greater than or equal to `startIndex`.
     * @return a new subsequence from the range [startIndex, endIndex).
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Converts the underlying data of the IBAN instance into a string representation.
     *
     * This method divides the `value` of the IBAN into groups of four characters and
     * joins these groups with a space character as the separator. The resulting string
     * is a readable and formatted representation of the IBAN.
     *
     * @return A string representation of the IBAN, formatted into groups of four characters.
     * @since 1.0.0
     */
    override fun toString() = (value % 4).joinToString(separator = String.SPACE)
}