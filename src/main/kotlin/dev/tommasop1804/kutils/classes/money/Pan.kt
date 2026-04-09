/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.money

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.money.Pan.Companion.normalize
import dev.tommasop1804.kutils.classes.money.PaymentMethod.Card.*
import dev.tommasop1804.kutils.exceptions.*
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize

/**
 * Represents a Payment Account Number (PAN) value class.
 * Provides utility methods for manipulation and validation of PAN values.
 * Instances of this class are immutable, ensuring the integrity of the PAN representation.
 * 
 * @property value The raw PAN value as a string.
 * @property normalized The normalized form of the PAN, containing only digits.
 * @property controlDigit The computed control digit for the PAN, based on its normalized value.
 * @property issuer The issuer information associated with the PAN, if determinable.
 * 
 * @since 3.1.0
 * @author Tommaso Pastorelli
 */
@JvmInline
@Suppress("unused")
@JsonSerialize(using = Pan.Companion.Serializer::class)
@JsonDeserialize(using = Pan.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Pan.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Pan.Companion.OldDeserializer::class)
value class Pan private constructor(val value: String) : CharSequence {
    /**
     * A normalized string representation of the underlying `value`, where all non-digit characters 
     * (such as spaces and dashes) are removed. The resulting string contains only numeric characters.
     * 
     * This property is derived using the private `normalize` function, which ensures that the 
     * transformation is consistent and adheres to the expected format for PAN normalization. 
     * 
     * @return A string consisting exclusively of digits extracted from the original `value`.
     * @since 3.1.0
     */
    val normalized: String get() = normalize(value)
    /**
     * The control digit derived from the normalized representation of the PAN (Primary Account Number).
     *
     * This value is computed using the Luhn algorithm, which is a checksum formula commonly used for
     * validating identification numbers. It ensures data integrity through a mathematically predictable
     * validation mechanism. The control digit is calculated based on all but the last digit of the
     * normalized PAN.
     *
     * @return The control digit, a single integer value ranging from 0 to 9.
     * @throws IllegalArgumentException if the normalized PAN is invalid or empty.
     * @since 3.1.0
     */
    val controlDigit: Int get() = computeControlDigit(normalized)
    /**
     * Represents the issuer entity associated with the current context.
     * This value is derived dynamically using the `Issuer.from(this)` method.
     *
     * @return An instance of `Issuer` if resolvable from the current context, or `null` otherwise.
     * @since 3.1.0
     */
    val issuer: Issuer? get() = Issuer.from(this)

    /**
     * Represents the length of the normalized value of the PAN. The value is obtained
     * by applying the `normalize` function to remove non-digit characters such as spaces
     * and dashes from the input, leaving only numeric digits.
     *
     * @return The number of numeric digits present in the normalized PAN value.
     * @since 3.1.0
     */
    override val length: Int get() = normalize(value).length

    /**
     * Secondary constructor for the `Pan` class that initializes the value
     * after normalization and whitespace replacement.
     *
     * The input `CharSequence` is first normalized to remove unwanted whitespace
     * and delimiter characters, ensuring that only valid digits remain.
     * Consecutive whitespace of size 4 or more is then replaced with a single space.
     * Finally, the processed value is passed to the primary constructor for validation.
     *
     * @param value The raw input `CharSequence` to be normalized and processed.
     * @throws MalformedInputException If the normalized value does not conform
     *         to the format requirements of a valid PAN.
     * @see normalize
     * @since 3.1.0
     */
    constructor(value: CharSequence) : this(compute {
        val normalized = normalize(value.toString())
        (normalized % 4).joinToString(Char.SPACE)
    })

    init {
        if (!value.isValidPan()) throw MalformedInputException(Pan::class)
    }

    companion object {
        /**
         * Attempts to convert the current `CharSequence` into a `Pan` instance.
         *
         * This method wraps the creation of a `Pan` object in a `runCatching` block,
         * allowing for safe handling of potential exceptions that may occur during
         * the normalization or validation of the input sequence. If the input is
         * successfully converted into a valid `Pan`, the resulting object is
         * encapsulated in a `Result`. Otherwise, an exception is captured in the
         * `Result`.
         *
         * @receiver The `CharSequence` to be normalized and converted into a `Pan`.
         * @return A `Result` containing either the successfully created `Pan` instance or an exception in case of failure.
         * @since 3.1.0
         */
        fun CharSequence.toPan() = runCatching { Pan(this) }

        /**
         * Validates if the given character sequence is a valid PAN (Primary Account Number) 
         * based on the Luhn algorithm and control digit computation.
         *
         * The method first normalizes the character sequence to remove any spaces or dashes. 
         * It then checks that the length of the input is sufficient and validates 
         * the control digit against the computed value. Finally, it ensures that the 
         * Luhn checksum modulo 10 equals 0.
         *
         * @return `true` if the character sequence represents a valid PAN, `false` otherwise.
         * @since 3.1.0
         */
        fun CharSequence.isValidPan(): Boolean {
            val digits = normalize(toString())
            if (digits.length < 2) return false
            return luhnSum(digits) % 10 == 0
        }

        /**
         * Normalizes the given input string by removing all whitespace and dash characters.
         * Ensures that the resulting string contains only numeric digits. Throws a 
         * `MalformedInputException` if the validation fails.
         *
         * @param input The input string to be normalized. It may contain numeric digits, spaces, and dashes.
         * @return A string containing only numeric digits derived from the given input.
         * @throws MalformedInputException If the input contains any non-numeric characters other than spaces or dashes.
         * @since 3.1.0
         */
        private fun normalize(input: String): String {
            val cleaned = input.replace("[\\s-]".toRegex(), "")
            return if (cleaned.all { it.isDigit() }) cleaned else throw MalformedInputException("Input must contain only digits, spaces, or dashes")
        }

        /**
         * Computes the control digit for a given numeric string using the Luhn algorithm.
         *
         * This function takes a numeric string without a check digit, normalizes it, and calculates the 
         * control digit required to make the entire number valid according to the Luhn algorithm. 
         * The control digit is appended at the end of the number to validate its integrity.
         *
         * @param numberWithoutCheck A numeric string without the check digit. It must contain only digits, 
         * spaces, or dashes, which will be normalized before processing.
         * @return The calculated control digit as an integer. This digit ensures the provided number 
         * conforms to the Luhn checksum formula.
         * @throws IllegalArgumentException If the input string is empty or invalid after normalization.
         * @since 3.1.0
         */
        fun computeControlDigit(numberWithoutCheck: String): Int {
            val digits = normalize(numberWithoutCheck)
            require(digits.isNotEmpty()) { "Input must not be empty" }
            val sum = luhnSum("${digits}0")
            return (10 - (sum % 10)) % 10
        }

        /**
         * Calculates the Luhn checksum for a given string of digits. The Luhn algorithm is used to validate
         * identification numbers such as credit card numbers.
         *
         * @param digits A string containing the numeric digits to be validated or processed using the Luhn algorithm.
         * @return The calculated Luhn checksum as an integer.
         * @since 3.1.0
         */
        private fun luhnSum(digits: String): Int =
            digits.reversed()
                .map { it.digitToInt() }
                .mapIndexed { index, digit ->
                    if (index % 2 == 1) {
                        val doubled = digit * 2
                        if (doubled > 9) doubled - 9 else doubled
                    } else digit
                }.sum()

        class Serializer : ValueSerializer<Pan>() {
            override fun serialize(value: Pan, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<Pan>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = Pan(p.string)
        }

        class OldSerializer : JsonSerializer<Pan>() {
            override fun serialize(value: Pan, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<Pan>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Pan = Pan(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Pan?, String?> {
            override fun convertToDatabaseColumn(attribute: Pan?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): Pan? = dbData?.let { Pan(it) }
        }
    }

    /**
     * Retrieves the character at the specified [index] from the underlying value.
     *
     * @param index the position of the character to be returned. Must be a valid index within the range of the value.
     * @return the character at the specified [index].
     * @since 3.1.0
     */
    override fun get(index: Int): Char = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this character sequence.
     * The subsequence starts at the specified [startIndex] and ends at the character
     * before [endIndex].
     *
     * @param startIndex the start index of the subsequence, inclusive.
     * @param endIndex the end index of the subsequence, exclusive.
     * @return the specified subsequence.
     * @since 3.1.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns a string representation of the current object.
     * This method is overridden to provide a custom string format
     * based on the internal [value] property.
     *
     * @return a string representation of this object.
     * @since 3.1.0
     */
    override fun toString() = value
}