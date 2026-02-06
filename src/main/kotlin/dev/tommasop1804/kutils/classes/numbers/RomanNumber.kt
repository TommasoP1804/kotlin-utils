package dev.tommasop1804.kutils.classes.numbers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.exceptions.ValidationFailedException
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * Represents a Roman numeral with its corresponding numeric components.
 *
 * This class provides functionality to update the individual numeric values
 * for Roman numeral symbols (_one, _five, etc.), as well as perform arithmetic
 * operations, comparisons, and conversions to numeric types.
 *
 * @property one The derived value of "one" used in the Roman numeral representation.
 * @property five The derived value of "five" used in the Roman numeral representation.
 * @property ten The derived value of "ten" used in the Roman numeral representation.
 * @property fifty The derived value of "fifty" used in the Roman numeral representation.
 * @property hundred The derived value of "hundred" used in the Roman numeral representation.
 * @property fiveHundred The derived value of "five hundred" used in the Roman numeral representation.
 * @property thousand The derived value of "thousand" used in the Roman numeral representation.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = RomanNumber.Companion.Serializer::class)
@JsonDeserialize(using = RomanNumber.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = RomanNumber.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = RomanNumber.Companion.OldDeserializer::class)
@Suppress("unused")
class RomanNumber(one: Long, five: Long, ten: Long, fifty: Long, hundred: Long, fiveHundred: Long, thousand: Long) : CharSequence, Number(), Comparable<Number>, Serializable {
    private var _one: Long = 0
    private var _five: Long = 0
    private var _ten: Long = 0
    private var _fifty: Long = 0
    private var _hundred: Long = 0
    private var _fiveHundred: Long = 0
    private var _thousand: Long = 0

    /**
     * Represents the length of the string representation of the object.
     * This property calculates the character count of the object's string form.
     *
     * @since 1.0.0
     */
    override val length: Int
        get() = toString().length

    /**
     * Represents the value of one in the Roman numeral system.
     * Setting this variable updates the internal Roman numeral components accordingly.
     *
     * The provided value must be greater than or equal to zero.
     *
     * @throws ValidationFailedException if the provided value is less than zero.
     * @since 1.0.0
     */
    var one: Long
        get() = _one
        set(value) {
            validate(value >= 0) { "Value must be greater than or equal to zero" }
            val newRN = RomanNumber(value, five, ten, fifty, hundred, fiveHundred, thousand)
            _one = newRN.one
            _five = newRN.five
            _ten = newRN.ten
            _fifty = newRN.fifty
            _hundred = newRN.hundred
            _fiveHundred = newRN.fiveHundred
            _thousand = newRN.thousand
        }
    /**
     * Represents the value of five in the Roman numeral system for this instance.
     * This property enforces that the value must be greater than or equal to zero.
     * Updates the corresponding Roman numeral components when modified.
     *
     * @throws ValidationFailedException if the provided value is less than zero
     * @since 1.0.0
     */
    var five: Long
        get() = _five
        set(value) {
            validate(value >= 0) { "Value must be greater than or equal to zero" }
            val newRN = RomanNumber(one, value, ten, fifty, hundred, fiveHundred, thousand)
            _one = newRN.one
            _five = newRN.five
            _ten = newRN.ten
            _fifty = newRN.fifty
            _hundred = newRN.hundred
            _fiveHundred = newRN.fiveHundred
            _thousand = newRN.thousand
        }
    /**
     * Represents the "ten" value in a Roman numeral system.
     * The value must always be non-negative. If a negative value is assigned, an
     * exception will be thrown. Changing this value will update related Roman numeral
     * components in the containing `RomanNumber` instance.
     *
     * @throws ValidationFailedException if the provided value is negative.
     * @since 1.0.0
     */
    var ten: Long
        get() = _ten
        set(value) {
            validate(value >= 0) { "Value must be greater than or equal to zero" }
            val newRN = RomanNumber(one, five, value, fifty, hundred, fiveHundred, thousand)
            _one = newRN.one
            _five = newRN.five
            _ten = newRN.ten
            _fifty = newRN.fifty
            _hundred = newRN.hundred
            _fiveHundred = newRN.fiveHundred
            _thousand = newRN.thousand
        }
    /**
     * Represents the value of fifty in a Roman numeral system. The value must be a non-negative number.
     * When set, the Roman numeral components (`one`, `five`, `ten`, `hundred`, `fiveHundred`, and `thousand`)
     * are recalculated and updated accordingly to maintain consistency.
     *
     * @throws ValidationFailedException if the value is negative.
     * @since 1.0.0
     */
    var fifty: Long
        get() = _fifty
        set(value) {
            validate(value >= 0) { "Value must be greater than or equal to zero" }
            val newRN = RomanNumber(one, five, ten, value, hundred, fiveHundred, thousand)
            _one = newRN.one
            _five = newRN.five
            _ten = newRN.ten
            _fifty = newRN.fifty
            _hundred = newRN.hundred
            _fiveHundred = newRN.fiveHundred
            _thousand = newRN.thousand
        }
    /**
     * Represents the value of `hundred` in the Roman numeral system.
     * This variable is used in the context of Roman numeral manipulation and ensures that values remain valid
     * according to the system's requirements.
     *
     * @throws ValidationFailedException if the assigned value is negative.
     * @since 1.0.0
     */
    var hundred: Long
        get() = _hundred
        set(value) {
            validate(value >= 0) { "Value must be greater than or equal to zero" }
            val newRN = RomanNumber(one, five, ten, fifty, value, fiveHundred, thousand)
            _one = newRN.one
            _five = newRN.five
            _ten = newRN.ten
            _fifty = newRN.fifty
            _hundred = newRN.hundred
            _fiveHundred = newRN.fiveHundred
            _thousand = newRN.thousand
        }
    /**
     * Represents the Roman numeral value for five hundred.
     *
     * This variable is mutable and must always hold a non-negative value. Setting a new value updates
     * the related numeral components for proper representation of Roman numeral logic.
     *
     * @throws ValidationFailedException if the provided value is negative.
     * @since 1.0.0
     */
    var fiveHundred: Long
        get() = _fiveHundred
        set(value) {
            validate(value >= 0) { "Value must be greater than or equal to zero" }
            val newRN = RomanNumber(one, five, ten, fifty, hundred, value, thousand)
            _one = newRN.one
            _five = newRN.five
            _ten = newRN.ten
            _fifty = newRN.fifty
            _hundred = newRN.hundred
            _fiveHundred = newRN.fiveHundred
            _thousand = newRN.thousand
        }
    /**
     * Represents the numerical value of a thousand in Roman numeral representation.
     *
     * This property is validated to ensure the value assigned is greater than or equal to zero.
     * When a new value is set, it updates dependent Roman numeral components appropriately.
     *
     * @throws ValidationFailedException if the provided value is negative.
     * @since 1.0.0
     */
    var thousand: Long
        get() = _thousand
        set(value) {
            validate(value >= 0) { "Value must be greater than or equal to zero" }
            val newRN = RomanNumber(one, five, ten, fifty, hundred, fiveHundred, value)
            _one = newRN.one
            _five = newRN.five
            _ten = newRN.ten
            _fifty = newRN.fifty
            _hundred = newRN.hundred
            _fiveHundred = newRN.fiveHundred
            _thousand = newRN.thousand
        }

    /**
     * Secondary constructor that initializes the class with a number.
     * Converts the given number to a `Long` type and delegates to the primary constructor.
     *
     * @param number The number to initialize the class with. Must be greater than zero.
     *
     * @throws ValidationFailedException if the provided number is not greater than zero.
     *
     * @since 1.0.0
     */
    constructor(number: Number) : this(one = number.toLong(), 0, 0, 0, 0, 0, 0) {
        validate(number.toInt() > 0) { "Roman number cannot be negative or zero: $number" }
    }
    
    /**
     * Constructs an instance using the given character sequence.
     * The input character sequence is parsed into a required format and used to initialize the object.
     *
     * @param cs The character sequence to parse and initialize the object.
     * @since 1.0.0
     */
    constructor(cs: CharSequence) : this(parse(cs.toString())())
    
    /**
     * Private secondary constructor that initializes an instance with a RomanNumber.
     *
     * This constructor converts the given RomanNumber to its equivalent Long representation
     * and delegates initialization to the primary constructor.
     *
     * @param rn The RomanNumber to be converted and used for initialization.
     * @since 1.0.0
     */
    private constructor(rn: RomanNumber) : this(rn.toLong())

    init {
        validate(one >= 0 && five >= 0 && ten >= 0 && fifty >= 0 && hundred >= 0 && fiveHundred >= 0 && thousand >= 0) {
            "Negative values are not allowed"
        }
        validate(one > 0 || five > 0 || ten > 0 || fifty > 0 || hundred > 0 || fiveHundred > 0 || thousand > 0) {
            "At least one value must be greater than 0"
        }

        var currentOne = one
        var currentFive = five
        var currentTen = ten
        var currentFifty = fifty
        var currentHundred = hundred
        var currentFiveHundred = fiveHundred
        var currentThousand = thousand

        while (currentOne >= 5) {
            currentOne -= 5
            currentFive++
        }
        while (currentFive >= 2) {
            currentFive -= 2
            currentTen++
        }
        while (currentTen >= 5) {
            currentTen -= 5
            currentFifty++
        }
        while (currentFifty >= 2) {
            currentFifty -= 2
            currentHundred++
        }
        while (currentHundred >= 5) {
            currentHundred -= 5
            currentFiveHundred++
        }
        while (currentFiveHundred >= 2) {
            currentFiveHundred -= 2
            currentThousand++
        }

        _one = currentOne
        _five = currentFive
        _ten = currentTen
        _fifty = currentFifty
        _hundred = currentHundred
        _fiveHundred = currentFiveHundred
        _thousand = currentThousand
    }

    companion object {
        /**
         * A unique identifier for serializable classes used to verify compatibility
         * during the deserialization process. It ensures that a loaded class
         * is compatible with the serialized object.
         *
         * Any modification to the class that is incompatible with the serialized
         * form should result in a change to the serialVersionUID value to prevent
         * deserialization errors.
         *
         * @since 1.0.0
         */
        @Serial
        private const val serialVersionUID = 1L
        /**
         * Represents the Roman numeral 'I' with its value and positional structure in the Roman numeral system.
         * This constant is defined as an instance of the [RomanNumber] class with specific values for each denomination.
         *
         * @since 1.0.0
         */
        val I = RomanNumber(1, 0, 0, 0, 0, 0, 0)
        /**
         * Represents the Roman numeral II, corresponding to the decimal number 2.
         * This value is immutable and uses the RomanNumber class to encapsulate
         * the representation of the Roman numeral.
         *
         * @since 1.0.0
         */
        val II = RomanNumber(2, 0, 0, 0, 0, 0, 0)
        /**
         * Represents the Roman numeral III with its corresponding value components.
         * This predefined instance corresponds to the Roman numeral for the value 3 in standard Roman numeral notation.
         *
         * @since 1.0.0
         */
        val III = RomanNumber(3, 0, 0, 0, 0, 0, 0)
        /**
         * Represents the Roman numeral "IV", which has a value of 4.
         *
         * This static constant is part of the RomanNumber class and is defined with predefined component values.
         * Used primarily for operations and representations involving Roman numerals.
         *
         * @since 1.0.0
         */
        val IV = RomanNumber(4, 0, 0, 0, 0, 0, 0)
        /**
         * Represents a default RomanNumber instance with initial values set to zero, except for `five`,
         * which is assigned a value of 1. This variable can be used as a base constant for Roman numeral operations.
         *
         * @since 1.0.0
         */
        val V = RomanNumber(0, 1, 0, 0, 0, 0, 0)
        /**
         * Represents the Roman numeral VI.
         * It is initialized with the respective amounts of Roman numeral components for VI (6).
         *
         * @since 1.0.0
         */
        val VI = RomanNumber(1, 1, 0, 0, 0, 0, 0)
        /**
         * Represents the Roman numeral VII.
         *
         * The numeral is constructed using the specific count of symbols:
         * - One (I): 2
         * - Five (V): 1
         * - Ten (X): 0
         * - Fifty (L): 0
         * - Hundred (C): 0
         * - Five Hundred (D): 0
         * - Thousand (M): 0
         *
         * This value corresponds to the Roman numeral VII, equivalent to the decimal value 7.
         *
         * @since 1.0.0
         */
        val VII = RomanNumber(2, 1, 0, 0, 0, 0, 0)
        /**
         * Represents the Roman numeral VIII in its predefined composition of Roman numeral components.
         *
         * @since 1.0.0
         */
        val VIII = RomanNumber(3, 1, 0, 0, 0, 0, 0)
        /**
         * Represents the Roman numeral IX, which corresponds to the number 9 in decimal.
         * IX is a combination of 'I' (1) and 'X' (10), where 'I' precedes 'X' to indicate subtraction.
         *
         * This value is immutable and part of the Roman numeral representation system.
         *
         * @since 1.0.0
         */
        val IX = RomanNumber(4, 1, 0, 0, 0, 0, 0)
        /**
         * Represents a Roman number with specified initial values.
         * This property initializes a RomanNumber instance with predefined values for its components.
         * The initialized values are configured as 0 for most components except the 'ten' component, which is set to 1.
         *
         * @since 1.0.0
         */
        val X = RomanNumber(0, 0, 1, 0, 0, 0, 0)
        /**
         * Represents the Roman numeral "L" with its specific numeral structure.
         * It is statically defined and associated with the number 50 in the Roman numeral system.
         *
         * @since 1.0.0
         */
        val L = RomanNumber(0, 0, 0, 1, 0, 0, 0)
        /**
         * Represents a specific Roman numeral configuration as a predefined constant.
         * This constant specifies a Roman numeral with the following values:
         * - 1 unit of `hundred`.
         *
         * This variable is defined using the custom `RomanNumber` class that encapsulates values
         * for Roman numeral symbols.
         *
         * @since 1.0.0
         */
        val C = RomanNumber(0, 0, 0, 0, 1, 0, 0)
        /**
         * Represents the Roman numeral "D", which corresponds to the value 500.
         * This value is predefined as an instance of the RomanNumber class with the
         * respective fields initialized to represent Roman numeral "D".
         *
         * @since 1.0.0
         */
        val D = RomanNumber(0, 0, 0, 0, 0, 1, 0)
        /**
         * Represents the Roman numeral "M", corresponding to the value 1000 in the Roman numeral system.
         * This value is instantiated using the `RomanNumber` class, with specific counts indicating
         * the presence of "M" and no other Roman numeral symbols.
         *
         * @since 1.0.0
         */
        val M = RomanNumber(0, 0, 0, 0, 0, 0, 1)
        
        /**
         * Adds a RomanNumber to a Byte value and returns the resulting integer value.
         *
         * @param romanNumber the RomanNumber instance to be added to the Byte value.
         * @since 1.0.0
         */
        operator fun Byte.plus(romanNumber: RomanNumber) = plus(romanNumber.toInt())
        /**
         * Adds the integer value of a RomanNumber instance to an Int.
         *
         * @param romanNumber the RomanNumber instance whose integer value is to be added
         * @since 1.0.0
         */
        operator fun Int.plus(romanNumber: RomanNumber) = plus(romanNumber.toInt())
        /**
         * Adds a RomanNumber to this Short value by converting the RomanNumber to its integer representation.
         *
         * @param romanNumber the RomanNumber to be added to this Short.
         * @since 1.0.0
         */
        operator fun Short.plus(romanNumber: RomanNumber) = plus(romanNumber.toInt())
        /**
         * Adds the numerical value of the given RomanNumber to this Long value.
         *
         * @param romanNumber the RomanNumber to be added to this Long value.
         * @return the result of adding the numerical equivalent of the RomanNumber to this Long value.
         * @since 1.0.0
         */
        operator fun Long.plus(romanNumber: RomanNumber) = plus(romanNumber.toLong())
        /**
         * Adds a RomanNumber to a Float and returns the result as a Float.
         *
         * @param romanNumber the RomanNumber instance to be added to the Float.
         * @since 1.0.0
         */
        operator fun Float.plus(romanNumber: RomanNumber) = plus(romanNumber.toFloat())
        /**
         * Adds a `RomanNumber` to a `Double` and returns the result as a `Double`.
         *
         * @param romanNumber the `RomanNumber` to be added to the `Double`.
         * @since 1.0.0
         */
        operator fun Double.plus(romanNumber: RomanNumber) = plus(romanNumber.toDouble())

        /**
         * Subtracts the integer value of the given RomanNumber from this Byte value.
         *
         * @param romanNumber the RomanNumber whose integer value will be subtracted from this Byte.
         * @since 1.0.0
         */
        operator fun Byte.minus(romanNumber: RomanNumber) = minus(romanNumber.toInt())
        /**
         * Subtracts a RomanNumber from an integer and returns the result.
         *
         * @param romanNumber The RomanNumber to be subtracted from the integer.
         * @since 1.0.0
         */
        operator fun Int.minus(romanNumber: RomanNumber) = minus(romanNumber.toInt())
        /**
         * Subtracts the integer representation of the given Roman number from this `Short` value.
         *
         * @param romanNumber The Roman number to be subtracted, converted to its integer equivalent.
         * @since 1.0.0
         */
        operator fun Short.minus(romanNumber: RomanNumber) = minus(romanNumber.toInt())
        /**
         * Subtracts the numeric value of a RomanNumber from the current Long value.
         *
         * @param romanNumber the RomanNumber to be subtracted from the current Long value
         * @since 1.0.0
         */
        operator fun Long.minus(romanNumber: RomanNumber) = minus(romanNumber.toLong())
        /**
         * Subtracts the value of the given RomanNumber from the current Float instance.
         *
         * @param romanNumber The RomanNumber to be subtracted. It will be converted to a Float for the operation.
         * @since 1.0.0
         */
        operator fun Float.minus(romanNumber: RomanNumber) = minus(romanNumber.toFloat())
        /**
         * Subtracts the numeric value of a RomanNumber instance from this Double.
         *
         * @receiver The Double value from which the RomanNumber's value will be subtracted.
         * @param romanNumber The RomanNumber instance whose value is to be subtracted.
         * @return A Double representing the result of the subtraction.
         * @since 1.0.0
         */
        operator fun Double.minus(romanNumber: RomanNumber) = minus(romanNumber.toDouble())

        /**
         * Multiplies the numeric value of a Byte with the integer representation of a RomanNumber.
         *
         * @param romanNumber the RomanNumber to be multiplied after being converted to an integer
         * @return the resulting product as an Int
         * @since 1.0.0
         */
        operator fun Byte.times(romanNumber: RomanNumber) = times(romanNumber.toInt())
        /**
         * Multiplies an integer with a RomanNumber, converting the RomanNumber to its integer value first.
         *
         * @param romanNumber The RomanNumber instance to be multiplied with the integer.
         * @since 1.0.0
         */
        operator fun Int.times(romanNumber: RomanNumber) = times(romanNumber.toInt())
        /**
         * Multiplies a Short value by the integer representation of a RomanNumber.
         *
         * @param romanNumber the RomanNumber instance whose integer equivalent is used for multiplication.
         * @since 1.0.0
         */
        operator fun Short.times(romanNumber: RomanNumber) = times(romanNumber.toInt())
        /**
         * Multiplies the given RomanNumber by this Long value and returns the resulting Long value.
         *
         * @param romanNumber the RomanNumber operand to be multiplied with this Long value.
         * @since 1.0.0
         */
        operator fun Long.times(romanNumber: RomanNumber) = times(romanNumber.toLong())
        /**
         * Multiplies a [Float] by the given [RomanNumber]. The [RomanNumber] is converted to its
         * float representation before multiplication.
         *
         * @param romanNumber the Roman numeral to be multiplied as a [RomanNumber].
         * @since 1.0.0
         */
        operator fun Float.times(romanNumber: RomanNumber) = times(romanNumber.toFloat())
        /**
         * Multiplies a Double by a RomanNumber. Converts the RomanNumber to its Double equivalent
         * before performing the multiplication.
         *
         * @param romanNumber The RomanNumber operand to multiply with the Double.
         * @since 1.0.0
         */
        operator fun Double.times(romanNumber: RomanNumber) = times(romanNumber.toDouble())

        /**
         * Divides the Byte value by the integer value of the given RomanNumber.
         *
         * @param romanNumber The RomanNumber to divide the Byte value by. It is converted to its integer equivalent.
         * @since 1.0.0
         */
        operator fun Byte.div(romanNumber: RomanNumber) = div(romanNumber.toInt())
        /**
         * Divides the integer by the integer value of the provided RomanNumber.
         *
         * @param romanNumber The RomanNumber to be converted to an integer and used as the divisor.
         * @return The result of the division as an integer.
         * @throws ArithmeticException if the division by zero occurs.
         * @since 1.0.0
         */
        operator fun Int.div(romanNumber: RomanNumber) = div(romanNumber.toInt())
        /**
         * Divides the Short value by the integer representation of a RomanNumber.
         *
         * @param romanNumber the RomanNumber to divide this Short value by. The RomanNumber is converted to its integer equivalent before the operation.
         * @since 1.0.0
         */
        operator fun Short.div(romanNumber: RomanNumber) = div(romanNumber.toInt())
        /**
         * Divides the current `Long` value by a `RomanNumber` and returns the result as a `Long`.
         *
         * @param romanNumber the `RomanNumber` to divide the current `Long` value by.
         * @since 1.0.0
         */
        operator fun Long.div(romanNumber: RomanNumber) = div(romanNumber.toLong())
        /**
         * Divides a Float value by the numeric equivalent of a RomanNumber.
         *
         * @param romanNumber the RomanNumber to be converted to a Float and used as the divisor
         * @since 1.0.0
         */
        operator fun Float.div(romanNumber: RomanNumber) = div(romanNumber.toFloat())
        /**
         * Performs division between a Double and a RomanNumber by converting the RomanNumber to its Double equivalent.
         *
         * @param romanNumber The RomanNumber to be divided.
         *
         * @since 1.0.0
         */
        operator fun Double.div(romanNumber: RomanNumber) = div(romanNumber.toDouble())

        /**
         * Performs the modulo operation between this Byte and the integer value
         * of the specified RomanNumber.
         *
         * @param romanNumber the RomanNumber to be used as the divisor after conversion to an integer
         * @since 1.0.0
         */
        operator fun Byte.rem(romanNumber: RomanNumber) = rem(romanNumber.toInt())
        /**
         * Computes the remainder of this integer when divided by the integer value of the given RomanNumber.
         *
         * @param romanNumber the RomanNumber instance whose integer value is used as the divisor
         * @since 1.0.0
         */
        operator fun Int.rem(romanNumber: RomanNumber) = rem(romanNumber.toInt())
        /**
         * Performs the remainder operation between this Short value and the integer representation
         * of the given RomanNumber.
         *
         * @param romanNumber the RomanNumber whose integer value will be used in the operation.
         * @since 1.0.0
         */
        operator fun Short.rem(romanNumber: RomanNumber) = rem(romanNumber.toInt())
        /**
         * Performs a remainder operation between this [Long] value and the numeric value of the given [RomanNumber].
         *
         * @param romanNumber The [RomanNumber] whose numeric value will be used for the remainder operation.
         * @since 1.0.0
         */
        operator fun Long.rem(romanNumber: RomanNumber) = rem(romanNumber.toLong())
        /**
         * Calculates the remainder of the division of this Float value by the value of the given RomanNumber.
         *
         * This operator function enables the use of the '%' operator with a Float and a RomanNumber.
         *
         * @param romanNumber the RomanNumber to divide this Float value by.
         * @since 1.0.0
         */
        operator fun Float.rem(romanNumber: RomanNumber) = rem(romanNumber.toFloat())
        /**
         * Calculates the remainder of this Double value when divided by the numeric value of the given RomanNumber.
         *
         * @param romanNumber the RomanNumber instance whose numeric equivalent is used as the divisor.
         * @since 1.0.0
         */
        operator fun Double.rem(romanNumber: RomanNumber) = rem(romanNumber.toDouble())

        /**
         * Checks if the given string is a valid Roman numeral.
         *
         * @param value the string to be validated as a Roman numeral
         * @return true if the string represents a valid Roman numeral, false otherwise
         * @since 1.0.0
         */
        fun CharSequence.isValidRomanNumber(value: String) = runCatching(::RomanNumber).isSuccess

        /**
         * Converts a number to its Roman numeral representation.
         * The number is converted to [Long], so in case of decimal digits, the fractional part is truncated.
         *
         * @since 1.0.0
         */
        fun Number.toRomanNumber() = RomanNumber(toLong())

        /**
         * Converts the given [CharSequence] to its equivalent Roman numeral representation.
         *
         * The method processes the [CharSequence] to parse and return its Roman numeral format.
         * It assumes the input [CharSequence] represents a valid numeric value suitable
         * for conversion.
         *
         * @receiver CharSequence A sequence of characters representing the input value to be converted.
         * @return String The Roman numeral representation of the input.
         *
         * @throws IllegalArgumentException If the input is not a valid numeric value or cannot
         *         be converted to a Roman numeral.
         * @since 1.0.0
         */
        fun CharSequence.toRomanNumber() = parse(toString())

        /**
         * Converts a Roman numeral string to its integer equivalent.
         * Handles validation and supports both uppercase and trimmed input.
         * If the parsing operation fails, an exception will be wrapped in a [Result].
         *
         * @param value The Roman numeral string to be parsed. It is expected to be in a valid Roman numeral format.
         * @return [Result] which contains an integer equivalent of the Roman numeral if successful, or an exception if the parsing fails.
         * @since 1.0.0
         */
        private infix fun parse(value: String) = runCatching {
            val number = tryOrNull { RomanNumber(value.toLong()) }
            if (number.isNotNull()) return@runCatching number

            val roman: String = +value.trim()
            var result = 0
            var prevValue = 0

            for (i in roman.length - 1 downTo 0) {
                val value = romanCharToValue(roman[i])
                if (value < prevValue) result -= value
                else result += value
                prevValue = value
            }
            RomanNumber(result)
        }

        /**
         * Converts a Roman numeral character into its corresponding integer value.
         *
         * @param char The Roman numeral character to be converted.
         * It can be either modern Roman numerals (e.g., 'I', 'V', 'X') or Unicode Roman numeral representations.
         * @return The integer value corresponding to the provided Roman numeral character.
         * @throws IllegalArgumentException If the provided character is not a valid Roman numeral.
         * @since 1.0.0
         */
        infix fun romanCharToValue(char: Char): Int {
            return when (char) {
                'I', 'Ⅰ', 'ⅰ', 'i' -> 1
                'V', 'Ⅴ', 'ⅴ', 'v' -> 5
                'X', 'Ⅹ', 'ⅹ', 'x' -> 10
                'L', 'Ⅼ', 'ⅼ', 'l', 'ↆ' -> 50
                'C', 'Ⅽ', 'ⅽ', 'c' -> 100
                'D', 'Ⅾ', 'ⅾ', 'd' -> 500
                'M', 'Ⅿ', 'ⅿ', 'm', 'ↀ' -> 1000
                'ↁ' -> 5000
                'ↂ' -> 10000
                'ↇ' -> 50000
                'ↈ' -> 100000
                else -> throw IllegalArgumentException("Invalid Roman character: $char")
            }
        }

        class Serializer : ValueSerializer<RomanNumber>() {
            override fun serialize(
                value: RomanNumber,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<RomanNumber>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = parse(p.string)()
        }

        class OldSerializer : JsonSerializer<RomanNumber>() {
            override fun serialize(value: RomanNumber, gen: JsonGenerator, serializers: SerializerProvider) = gen.writeString(value.toString())
        }

        class OldDeserializer : JsonDeserializer<RomanNumber>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): RomanNumber = parse(p.text)()
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<RomanNumber?, String?> {
            override fun convertToDatabaseColumn(attribute: RomanNumber?): String? = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?): RomanNumber? = dbData?.let { parse(it)() }
        }
    }

    /**
     * Creates an instance of RomanNumber with the specified numeric values for symbolic Roman numeral representations.
     *
     * @param one The value representing the Roman numeral 'I' (1).
     * @param five The value representing the Roman numeral 'V' (5).
     * @param ten The value representing the Roman numeral 'X' (10).
     * @param fifty The value representing the Roman numeral 'L' (50).
     * @param hundred The value representing the Roman numeral 'C' (100).
     * @param fiveHundred The value representing the Roman numeral 'D' (500).
     * @param thousand The value representing the Roman numeral 'M' (1000).
     * @since 1.0.0
     */
    fun copy(one: Long, five: Long, ten: Long, fifty: Long, hundred: Long, fiveHundred: Long, thousand: Long) =
        RomanNumber(one, five, ten, fifty, hundred, fiveHundred, thousand)
    
    /**
     * Updates the RomanNumber instance by setting the value of the "one" symbol.
     *
     * @param one The numeric value to be assigned to the "one" symbol of the RomanNumber.
     * @return A RomanNumber instance with the updated "one" symbol value.
     * @since 1.0.0
     */
    infix fun withOne(one: Long) = RomanNumber(one, five, ten, fifty, hundred, fiveHundred, thousand)

    /**
     * Updates the RomanNumber instance by providing a specific numeric value for the "five" field.
     *
     * 
     * @param five The numeric value to set for the "five" field of the RomanNumber instance.
     * @since 1.0.0
     */
    infix fun withFive(five: Long) = RomanNumber(one, five, ten, fifty, hundred, fiveHundred, thousand)

    /**
     * Updates the `ten` field of the `RomanNumber` instance with the provided value.
     *
     * 
     * @param ten The new value to set for the `ten` field.
     * @since 1.0.0
     */
    infix fun withTen(ten: Long) = RomanNumber(one, five, ten, fifty, hundred, fiveHundred, thousand)

    /**
     * Updates the RomanNumber instance with the given value for fifty.
     *
     * 
     * @param fifty The value to set as the fifty component of the Roman number.
     * @since 1.0.0
     */
    infix fun withFifty(fifty: Long) = RomanNumber(one, five, ten, fifty, hundred, fiveHundred, thousand)

    /**
     * Updates the `hundred` field of the `RomanNumber` instance and returns a new `RomanNumber` instance.
     *
     * 
     * @param hundred The numeric value to set for the `hundred` field.
     * @since 1.0.0
     */
    infix fun withHundred(hundred: Long) = RomanNumber(one, five, ten, fifty, hundred, fiveHundred, thousand)

    /**
     * Updates the RomanNumber instance with a provided value for the "five hundred" component.
     *
     * 
     * @param fiveHundred The value to be assigned to the "five hundred" component.
     * @return A new RomanNumber instance with the updated "five hundred" component.
     * @since 1.0.0
     */
    infix fun withFiveHundred(fiveHundred: Long) = RomanNumber(one, five, ten, fifty, hundred, fiveHundred, thousand)

    /**
     * Updates the current RomanNumber instance with the specified value for the `thousand` field.
     *
     * 
     * @param thousand The numeric value assigned to the `thousand` field of the RomanNumber instance.
     * @since 1.0.0
     */
    infix fun withThousand(thousand: Long) = RomanNumber(one, five, ten, fifty, hundred, fiveHundred, thousand)
    
    /**
     * Adds the provided RomanNumber to the current RomanNumber and updates the current instance.
     *
     * 
     * @param other The RomanNumber instance to be added to the current instance.
     * @return The updated RomanNumber instance after addition.
     * @since 1.0.0
     */
    operator fun plus(other: Number): RomanNumber {
        val value = toLong() + other.toLong()
        val newRN = RomanNumber(value)
        _one = newRN.one
        _five = newRN.five
        _ten = newRN.ten
        _fifty = newRN.fifty
        _hundred = newRN.hundred
        _fiveHundred = newRN.fiveHundred
        _thousand = newRN.thousand
        return this
    }

    /**
     * Subtracts the numeric value of another RomanNumber instance from this instance.
     *
     * 
     * @param other The RomanNumber instance to be subtracted.
     * @return The updated RomanNumber instance after subtraction.
     * @since 1.0.0
     */
    operator fun minus(other: Number): RomanNumber {
        val value = toLong() - other.toLong()
        val newRN = RomanNumber(value)
        _one = newRN.one
        _five = newRN.five
        _ten = newRN.ten
        _fifty = newRN.fifty
        _hundred = newRN.hundred
        _fiveHundred = newRN.fiveHundred
        _thousand = newRN.thousand
        return this
    }

    /**
     * Multiplies the current RomanNumber instance by another RomanNumber instance.
     *
     * 
     * @param other The RomanNumber instance to multiply with the receiver.
     * @return The result of the multiplication as a RomanNumber.
     * @since 1.0.0
     */
    operator fun times(other: Number): RomanNumber {
        val value = toLong() * other.toLong()
        val newRN = RomanNumber(value)
        _one = newRN.one
        _five = newRN.five
        _ten = newRN.ten
        _fifty = newRN.fifty
        _hundred = newRN.hundred
        _fiveHundred = newRN.fiveHundred
        _thousand = newRN.thousand
        return this
    }

    /**
     * Performs division between the current RomanNumber and another RomanNumber.
     * Updates the current RomanNumber with the quotient of the division result.
     *
     * 
     * @param other The RomanNumber used as the divisor in the division operation.
     * @return The updated RomanNumber instance representing the result of the division.
     * @since 1.0.0
     */
    operator fun div(other: Number): RomanNumber {
        val value = toLong() / other.toLong()
        val newRN = RomanNumber(value)
        _one = newRN.one
        _five = newRN.five
        _ten = newRN.ten
        _fifty = newRN.fifty
        _hundred = newRN.hundred
        _fiveHundred = newRN.fiveHundred
        _thousand = newRN.thousand
        return this
    }

    /**
     * Performs the modulus operation between the current `RomanNumber` instance and the given `RomanNumber` instance.
     * The result is the remainder of the division of the numeric representations of the two Roman numbers.
     * This operation updates the current `RomanNumber` instance to hold the result.
     *
     * 
     * @param other The `RomanNumber` instance representing the divisor.
     * @return The updated `RomanNumber` instance representing the result of the modulus operation.
     * @since 1.0.0
     */
    operator fun rem(other: Number): RomanNumber {
        val value = toLong() % other.toLong()
        val newRN = RomanNumber(value)
        _one = newRN.one
        _five = newRN.five
        _ten = newRN.ten
        _fifty = newRN.fifty
        _hundred = newRN.hundred
        _fiveHundred = newRN.fiveHundred
        _thousand = newRN.thousand
        return this
    }

    /**
     * Increments the value of the current RomanNumber instance by one.
     *
     * 
     * @return The updated RomanNumber instance after the increment operation.
     * @since 1.0.0
     */
    operator fun inc(): RomanNumber {
        val value = toLong() + 1
        val newRN = RomanNumber(value)
        _one = newRN.one
        _five = newRN.five
        _ten = newRN.ten
        _fifty = newRN.fifty
        _hundred = newRN.hundred
        _fiveHundred = newRN.fiveHundred
        _thousand = newRN.thousand
        return this
    }

    /**
     * Decrements the value of the current RomanNumber by one and updates its internal representation.
     *
     * 
     * @return The updated RomanNumber instance after decrementing by one.
     * @since 1.0.0
     */
    operator fun dec(): RomanNumber {
        val value = toLong() + 1
        val newRN = RomanNumber(value)
        _one = newRN.one
        _five = newRN.five
        _ten = newRN.ten
        _fifty = newRN.fifty
        _hundred = newRN.hundred
        _fiveHundred = newRN.fiveHundred
        _thousand = newRN.thousand
        return this
    }

    /**
     * Unary plus operator function. This function is typically used to reflect the operand
     * without altering its value, effectively returning the object itself.
     *
     * @return The current instance of the object.
     * @since 1.0.0
     */
    operator fun unaryPlus() = this

    /**
     * Performs unary negation on the current instance, returning a new instance
     * with the negated value of the original object's numeric representation.
     *
     * @return A new instance with the negated value.
     * @since 1.0.0
     */
    operator fun unaryMinus() = RomanNumber(-toLong())

    /**
     * Returns a string representation of the current RomanNumber instance.
     *
    */
    /**
     * Compares the current RomanNumber instance with another RomanNumber instance.
     *
     * 
     * @param other the RomanNumber instance to be compared with.
     * @return a negative integer, zero, or a positive integer as this RomanNumber
     *         is less than, equal to, or greater than the specified RomanNumber.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Number) = toLong().compareTo(other.toLong())

    /**
     * Compares this RomanNumber instance to another object for equality.
     *
     * 
     * @param other the object to compare this instance with
     * @return `true` if the other object is a RomanNumber and represents the same value; `false` otherwise
     * @since 1.0.0
     */
    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Number) return false
        return toLong() == other.toLong()
    }

    /**
     * Converts the current RomanNumber instance to its equivalent Double representation.
     *
     * 
     * @return The Double representation of the RomanNumber.
     * @since 1.0.0
     */
    override fun toDouble(): Double = toLong().toDouble()
    /**
     * Converts the current RomanNumber instance to a Float representation.
     * The conversion is performed by first obtaining the Long representation of the instance
     * and then casting it to Float.
     *
     * 
     * @return A Float representation of the RomanNumber instance.
     * @since 1.0.0
     */
    override fun toFloat(): Float = toLong().toFloat()
    /**
     * Converts the cu1rrent RomanNumber instance into its numeric representation as a `Long`.
     *
     * 
     * @return The numeric value of the RomanNumber as a `Long`.
     * @since 1.0.0
     */
    override fun toLong(): Long = one + 5 * five + 10 * ten + 50 * fifty + 100 * hundred + 500 * fiveHundred + 1000 * thousand
    /**
     * Converts the current RomanNumber instance to its integer representation.
     *
     * 
     * @return The integer representation of this RomanNumber instance.
     * @since 1.0.0
     */
    override fun toInt(): Int = toLong().toInt()
    /**
     * Converts the current RomanNumber instance to its numeric value as a Short.
     *
     * 
     * @return The numeric value of the RomanNumber as a Short.
     * @since 1.0.0
     */
    override fun toShort(): Short = toLong().toShort()
    /**
     * Converts the numeric value of the RomanNumber instance to a Byte representation.
     *
     * 
     * @return The Byte value representation of the RomanNumber.
     * @since 1.0.0
     */
    override fun toByte(): Byte = toLong().toByte()
    
    /**
     * Computes the hash code for the object using the hash codes of its properties.
     *
     * 
     * @return The computed hash code as an integer.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = one.hashCode()
        result = 31 * result + five.hashCode()
        result = 31 * result + ten.hashCode()
        result = 31 * result + fifty.hashCode()
        result = 31 * result + hundred.hashCode()
        result = 31 * result + fiveHundred.hashCode()
        result = 31 * result + thousand.hashCode()
        return result
    }

    /**
     * Returns the number as a `String` in Roman numerals.
     * @param latinChars if `true` the result will be in Latin characters. If `false` the result will be in Roman numeral characters.
     * @param upperCase if `true` the result will be in upper case. If `false` the result will be in lower case.
     * @param multiplesSuffixes if `true` the multiples suffixes will be used. If `false` the multiples suffixes will not be used.
     * @param altChars the priority of alternative characters. See [dev.tommasop1804.kutils.AltCharsPriority]
     * @return the number as a `String` in Roman numerals.
     * @since 1.0.0
     */
    fun toString(latinChars: Boolean = true, upperCase: Boolean = true, multiplesSuffixes: Boolean = false, altChars: AltCharsPriority = AltCharsPriority.STANDARD): String {
        val sb = StringBuilder()

        var number = toLong()

        if (altChars != AltCharsPriority.STANDARD) {
            if (number == 50L) {
                return if (altChars == AltCharsPriority.ALT || altChars == AltCharsPriority.ALT_LIGA) "ↆ"
                else if (latinChars) if (upperCase) "L" else "l"
                else if (upperCase) "Ⅼ" else "ⅼ"
            }
            if (number == 1000L) {
                return if (altChars == AltCharsPriority.ALT) "ⅭⅠↃ"
                else if (altChars == AltCharsPriority.ALT_LIGA) "ↀ"
                else if (latinChars) if (upperCase) "M" else "m"
                else if (upperCase) "Ⅿ" else "ⅿ"
            }
            if (number == 2000L) {
                return if (altChars == AltCharsPriority.ALT) "ⅭⅠↃⅭⅠↃ"
                else if (altChars == AltCharsPriority.ALT_LIGA) "ↀↀ"
                else if (latinChars) if (upperCase) "MM" else "mm"
                else if (upperCase) "ⅯⅯ" else "ⅿ"
            }
            if (number == 5000L) {
                if (altChars == AltCharsPriority.ALT || altChars == AltCharsPriority.ALT_PRIORITY_DEFAULT) return "ⅠↃↃ"
                else if (altChars == AltCharsPriority.ALT_LIGA || altChars == AltCharsPriority.ALT_LIGA_PRIORITY_DEFAULT) return "ↁ"
            }
            if (number == 10000L) {
                if (altChars == AltCharsPriority.ALT || altChars == AltCharsPriority.ALT_PRIORITY_DEFAULT) return "ⅭⅭⅠↃↃ"
                else if (altChars == AltCharsPriority.ALT_LIGA || altChars == AltCharsPriority.ALT_LIGA_PRIORITY_DEFAULT) return "ↂ"
            }
            if (number == 50000L) {
                if (altChars == AltCharsPriority.ALT || altChars == AltCharsPriority.ALT_PRIORITY_DEFAULT) return "ⅠↃↃↃ"
                else if (altChars == AltCharsPriority.ALT_LIGA || altChars == AltCharsPriority.ALT_LIGA_PRIORITY_DEFAULT) return "ↇ"
            }
            if (number == 100000L) {
                if (altChars == AltCharsPriority.ALT || altChars == AltCharsPriority.ALT_PRIORITY_DEFAULT) return "ⅭⅭⅭⅠↃↃↃ"
                else if (altChars == AltCharsPriority.ALT_LIGA || altChars == AltCharsPriority.ALT_LIGA_PRIORITY_DEFAULT) return "ↈ"
            }
        }

        val romanNumeralsUpper = if (multiplesSuffixes)
            arrayOf(
                arrayOf("1000000", if (latinChars) "M̅" else "Ⅿ̅"),
                arrayOf("900000", if (latinChars) "C̅M̅" else "Ⅽ̅Ⅿ̅"),
                arrayOf("500000", if (latinChars) "D̅" else "Ⅾ̅"),
                arrayOf("400000", if (latinChars) "C̅D̅" else "Ⅽ̅Ⅾ̅"),
                arrayOf("100000", if (latinChars) "C̅" else "Ⅽ̅"),
                arrayOf("90000", if (latinChars) "X̅C̅" else "Ⅹ̅Ⅽ̅"),
                arrayOf("50000", if (latinChars) "L̅" else "Ⅼ̅"),
                arrayOf("40000", if (latinChars) "X̅L̅" else "Ⅹ̅Ⅼ̅"),
                arrayOf("10000", if (latinChars) "X̅" else "Ⅹ̅"),
                arrayOf("9000", if (latinChars) "I̅X̅" else "Ⅰ̅Ⅹ̅"),
                arrayOf("5000", if (latinChars) "V̅" else "Ⅴ̅"),
                arrayOf("4000", if (latinChars) "I̅V̅" else "Ⅰ̅Ⅴ̅"),
                arrayOf("1000", if (latinChars) "M" else "Ⅿ"),
                arrayOf("900", if (latinChars) "CM" else "ⅭⅯ"),
                arrayOf("500", if (latinChars) "D" else "Ⅾ"),
                arrayOf("400", if (latinChars) "CD" else "ⅭⅮ"),
                arrayOf("100", if (latinChars) "C" else "Ⅽ"),
                arrayOf("90", if (latinChars) "XC" else "ⅩⅭ"),
                arrayOf("50", if (latinChars) "L" else "Ⅼ"),
                arrayOf("40", if (latinChars) "XL" else "ⅩⅬ"),
                arrayOf("10", if (latinChars) "X" else "Ⅹ"),
                arrayOf("9", if (latinChars) "IX" else "ⅠⅩ"),
                arrayOf("5", if (latinChars) "V" else "Ⅴ"),
                arrayOf("4", if (latinChars) "IV" else "ⅠⅤ"),
                arrayOf("1", if (latinChars) "I" else "Ⅰ")
            )
        else
            arrayOf(
                arrayOf("1000", if (latinChars) "M" else "Ⅿ"),
                arrayOf("900", if (latinChars) "CM" else "ⅭⅯ"),
                arrayOf("500", if (latinChars) "D" else "Ⅾ"),
                arrayOf("400", if (latinChars) "CD" else "ⅭⅮ"),
                arrayOf("100", if (latinChars) "C" else "Ⅽ"),
                arrayOf("90", if (latinChars) "XC" else "ⅩⅭ"),
                arrayOf("50", if (latinChars) "L" else "Ⅼ"),
                arrayOf("40", if (latinChars) "XL" else "ⅩⅬ"),
                arrayOf("10", if (latinChars) "X" else "Ⅹ"),
                arrayOf("9", if (latinChars) "IX" else "ⅠⅩ"),
                arrayOf("5", if (latinChars) "V" else "Ⅴ"),
                arrayOf("4", if (latinChars) "IV" else "ⅠⅤ"),
                arrayOf("1", if (latinChars) "I" else "Ⅰ")
            )

        val romanNumeralsLower = if (multiplesSuffixes)
            arrayOf(
                arrayOf("1000000", if (latinChars) "m̅" else "ⅿ̅"),
                arrayOf("900000", if (latinChars) "c̅m̅" else "ⅽ̅ⅿ̅"),
                arrayOf("500000", if (latinChars) "d̅" else "ⅾ̅"),
                arrayOf("400000", if (latinChars) "c̅d̅" else "ⅽ̅ⅾ̅"),
                arrayOf("100000", if (latinChars) "c̅" else "ⅽ̅"),
                arrayOf("90000", if (latinChars) "x̅c̅" else "ⅹ̅ⅽ̅"),
                arrayOf("50000", if (latinChars) "l̅" else "ⅼ̅"),
                arrayOf("40000", if (latinChars) "x̅l̅" else "ⅹ̅ⅼ̅"),
                arrayOf("10000", if (latinChars) "x̅" else "ⅹ̅"),
                arrayOf("9000", if (latinChars) "i̅x̅" else "ⅰ̅ⅹ̅"),
                arrayOf("5000", if (latinChars) "v̅" else "ⅴ̅"),
                arrayOf("4000", if (latinChars) "i̅v̅" else "ⅰ̅ⅴ̅"),
                arrayOf("1000", if (latinChars) "m" else "ⅿ"),
                arrayOf("900", if (latinChars) "cm" else "ⅽⅿ"),
                arrayOf("500", if (latinChars) "d" else "ⅾ"),
                arrayOf("400", if (latinChars) "cd" else "ⅽⅾ"),
                arrayOf("100", if (latinChars) "c" else "ⅽ"),
                arrayOf("90", if (latinChars) "xc" else "Ⅹⅽ"),
                arrayOf("50", if (latinChars) "l" else "ⅼ"),
                arrayOf("40", if (latinChars) "xl" else "ⅹⅼ"),
                arrayOf("10", if (latinChars) "x" else "ⅹ"),
                arrayOf("9", if (latinChars) "ix" else "ⅰⅹ"),
                arrayOf("5", if (latinChars) "v" else "ⅴ"),
                arrayOf("4", if (latinChars) "iv" else "ⅰⅴ"),
                arrayOf("1", if (latinChars) "i" else "ⅰ")
            )
        else
            arrayOf(
                arrayOf("1000", if (latinChars) "m" else "ⅿ"),
                arrayOf("900", if (latinChars) "cm" else "ⅽⅿ"),
                arrayOf("500", if (latinChars) "d" else "ⅾ"),
                arrayOf("400", if (latinChars) "cd" else "ⅽⅾ"),
                arrayOf("100", if (latinChars) "c" else "ⅽ"),
                arrayOf("90", if (latinChars) "xc" else "Ⅹⅽ"),
                arrayOf("50", if (latinChars) "l" else "ⅼ"),
                arrayOf("40", if (latinChars) "xl" else "ⅹⅼ"),
                arrayOf("10", if (latinChars) "x" else "ⅹ"),
                arrayOf("9", if (latinChars) "ix" else "ⅰⅹ"),
                arrayOf("5", if (latinChars) "v" else "ⅴ"),
                arrayOf("4", if (latinChars) "iv" else "ⅰⅴ"),
                arrayOf("1", if (latinChars) "i" else "ⅰ")
            )

        for (pair in if (upperCase) romanNumeralsUpper else romanNumeralsLower) {
            val value = pair[0].toInt()
            val symbol = pair[1]
            while (number >= value) {
                sb.append(symbol)
                number -= value.toLong()
            }
        }
        return if (upperCase) sb.toString() else sb.toString().lowercase(Locale.getDefault())
    }

    /**
     * Returns the number as a `String` in Roman numerals.
     * @return the number as a `String` in Roman numerals.
     * @since 1.0.0
     */
    override fun toString()= toString(altChars = AltCharsPriority.STANDARD)

    /**
     * Retrieves the character at the specified index from the string representation of the object.
     *
     * @param index The position of the character to retrieve. Must be non-negative and less than the length of the string.
     * @return The character at the given index in the string representation.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     * @since 1.0.0
     */
    override fun get(index: Int) = toString()[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence.
     * The subsequence starts at the specified `startIndex` and ends right before the specified `endIndex`.
     *
     * @param startIndex the start index of the subsequence, inclusive
     * @param endIndex the end index of the subsequence, exclusive
     * @return the specified subsequence
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = toString().subSequence(startIndex, endIndex)
}