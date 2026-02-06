package dev.tommasop1804.kutils.classes.numbers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.PERCENT
import dev.tommasop1804.kutils.classes.numbers.Percentage.Companion.percent
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.isDecimal
import dev.tommasop1804.kutils.minus
import dev.tommasop1804.kutils.validateInputFormat
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Represents a percentage value with utility functions to operate within percentage-related constraints.
 * This class ensures validation of the input to avoid improper percentage representations and provides
 * operations and utility functions for percentages.
 *
 * @param value The internal representation of the percentage value as a Double.
 * @since 1.0.0
 */
@JvmInline
@Suppress("unused")
@JsonSerialize(using = Percentage.Companion.Serializer::class)
@JsonDeserialize(using = Percentage.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Percentage.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Percentage.Companion.OldDeserializer::class)
value class Percentage private constructor(internal val value: Double): Comparable<Percentage> {
    /**
     * Indicates whether the current percentage value exceeds a threshold considered 
     * as positive overflowing (greater than 100.0).
     *
     * This property is useful for determining if a percentage value exceeds the 
     * full completion threshold and can be used in scenarios where such overflow 
     * conditions need to be validated or flagged.
     *
     * @return `true` if the value is greater than 100.0, otherwise `false`.
     * @since 1.0.0
     */
    val isPositiveOverflowing: Boolean get() = value > 100.0
    /**
     * Indicates whether a value has not exceeded the positive overflow boundary.
     *
     * This property evaluates to `true` if the value is not in a state of positive overflow.
     * The state of positive overflow is determined by the complementary property `isPositiveOverflowing`.
     *
     * @return `true` if the value has not experienced positive overflow, otherwise `false`.
     * @since 1.0.0
     */
    val isNotPositiveOverflowing: Boolean get() = !isPositiveOverflowing
    /**
     * Indicates whether the current percentage value represents a negative value
     * that is considered "overflowing" based on certain application-specific logic.
     *
     * This property evaluates to `true` when the internal `value` is less than zero,
     * signaling that the percentage has exceeded a negative threshold.
     *
     * @since 1.0.0
     */
    val isNegativeOverflowing: Boolean get() = value < 0
    /**
     * Indicates whether the current state does not represent a negative overflow condition.
     * This property evaluates to true when negative overflow is not occurring.
     *
     * Negative overflow typically refers to a condition where a value surpasses the minimum
     * representable value in its range, wrapping or exceeding its limit in an unintended manner.
     *
     * @return `true` if no negative overflow is detected, `false` otherwise.
     * @since 1.0.0
     */
    val isNotNegativeOverflowing: Boolean get() = !isNegativeOverflowing
    /**
     * Indicates whether the percentage value exceeds the valid range, 
     * either positively or negatively.
     * 
     * This property evaluates to `true` if the percentage is in a state of 
     * positive overflow or negative overflow, determined by the internal 
     * conditions `isPositiveOverflowing` or `isNegativeOverflowing`.
     * 
     * @return `true` if the percentage exceeds its valid bounds, otherwise `false`.
     * @since 1.0.0
     */
    val isOverflowing: Boolean get() = isPositiveOverflowing || isNegativeOverflowing
    /**
     * Indicates whether the current state is not overflowing.
     * This is the negation of the `isOverflowing` property.
     *
     * Useful for determining if the content or operation
     * fits within the allowable bounds.
     *
     * @return `true` if not overflowing, `false` otherwise.
     * @since 1.0.0
     */
    val isNotOverflowing: Boolean get() = !isOverflowing
    /**
     * Indicates whether the current value is exactly zero.
     *
     * This property evaluates to `true` if the `value` of the `Percentage` object
     * is equal to `0.0`, and `false` otherwise.
     *
     * @since 1.0.0
     */
    val isZero: Boolean get() = value == 0.0
    /**
     * Evaluates whether the percentage value is not zero.
     * 
     * This property returns `true` if the percentage value is not equal to zero, 
     * and `false` if it is zero. It is derived by negating the `isZero` property.
     *
     * @since 1.0.0
     */
    val isNotZero: Boolean get() = !isZero
    /**
     * Indicates whether the current percentage is at its maximum value of 100.0.
     *
     * This property evaluates to `true` when the underlying value represents a fully complete or 
     * maximum percentage state. Otherwise, it evaluates to `false`.
     *
     * @since 1.0.0
     */
    val isFull: Boolean get() = value == 100.0
    /**
     * Indicates whether the percentage value is not at its full capacity.
     * 
     * This property is the logical negation of [isFull]. It evaluates to `true` 
     * when the percentage is not at its maximum allowable value and `false` 
     * when it is.
     * 
     * @since 1.0.0
     */
    val isNotFull: Boolean get() = !isFull
    /**
     * Indicates whether the value of this percentage is exactly half (50.0).
     *
     * This property evaluates to `true` if the `value` field is equal to 50.0,
     * otherwise it evaluates to `false`.
     *
     * @return `true` if the percentage is half, `false` otherwise.
     * @since 1.0.0
     */
    val isHalf: Boolean get() = value == 50.0
    /**
     * Indicates whether the percentage value does not represent half of a full percentage (50%).
     * 
     * This property is the logical inverse of [isHalf]. When `isNotHalf` is `true`, 
     * the percentage value is not equivalent to half (50%).
     * 
     * @since 1.0.0
     */
    val isNotHalf: Boolean get() = !isHalf
    /**
     * Indicates whether the current percentage value corresponds to exactly one-quarter (25%).
     *
     * This property evaluates to `true` if the percentage value equals 25.0, and `false` otherwise.
     * 
     * @return `true` if the percentage value is one-quarter, `false` otherwise.
     * @since 1.0.0
     */
    val isQuarter: Boolean get() = value == 25.0
    /**
     * Represents the negation of the `isQuarter` property. 
     * Indicates whether the current state or value is not equivalent to a quarter percentage.
     *
     * @since 1.0.0
     */
    val isNotQuarter: Boolean get() = !isQuarter
    /**
     * Indicates whether the value is a decimal number.
     *
     * This property evaluates to `true` if the value contains a fractional component,
     * meaning it is not a whole number. Otherwise, it evaluates to `false`.
     *
     * @since 1.0.0
     */
    val isDecimal: Boolean get() = value % 1.0 != 0.0
    /**
     * Represents a boolean value indicating whether the number is not a decimal.
     *
     * The value is determined as the logical negation of `isDecimal`,
     * where `isDecimal` represents whether the number is a decimal.
     *
     * @since 1.0.0
     */
    val isNotDecimal: Boolean get() = !isDecimal

    /**
     * Constructs a new instance of the `Percentage` class based on the given parameters.
     *
     * This constructor creates a percentage value using the provided number. The number can be interpreted 
     * as a normalized value within the range [0, 1] or as a direct percentage based on the boolean flag `from0to1`.
     * Additionally, it validates the input to ensure it adheres to specified bounds unless explicitly allowed 
     * through the overflow parameters.
     *
     * @param number The input value to be converted to a percentage. Can be within [0, 1] or as-is based on `from0to1`.
     * @param from0to1 Indicates if the input `number` represents a normalized value between [0, 1]. Defaults to `false`.
     * @param allowPositiveOverflow Allows values exceeding 100% if set to `true`. Defaults to `false`.
     * @param allowNegativeOverflow Allows values less than 0% if set to `true`. Defaults to `false`.
     * @throws MalformedInputException If `from0to1` is `false` and `number` exceeds 100 or falls below 0, 
     * unless the respective overflow allowance parameters are enabled.
     * @since 1.0.0
     */
    constructor(
        number: Number,
        from0to1: Boolean = false,
        allowPositiveOverflow: Boolean = false,
        allowNegativeOverflow: Boolean = false
    ) : this(Unit.run {
        val value = if (from0to1) number.toDouble() * 100.0 else number.toDouble()

        if (!allowPositiveOverflow) validateInputFormat(value <= 100.0, lazyMessage = { "The percentage must be less than or equal to 100" })
        if (!allowNegativeOverflow) validateInputFormat(value >= 0.0, lazyMessage = { "The percentage must be greater than or equal to 0" })
        value
    })

    /**
     * Constructor for creating a `Percentage` instance using a string representation of a percentage value.
     *
     * The input string is trimmed, and any trailing '%' character is removed before parsing. If the string
     * is empty or contains only whitespace, a `MalformedInputException` is thrown. If the string cannot
     * be parsed as a valid double value, another `MalformedInputException` is thrown.
     * 
     * The parsed value is adjusted based on the `from0to1` flag:
     * - If `from0to1` is set to `true`, the input is interpreted as a percentage in the range `[0, 1]` 
     *   and multiplied by 100 to scale it to a valid percentage value.
     * - If `from0to1` is `false`, the input is interpreted as a straightforward percentage value.
     *
     * Additional flags can control overflow behavior:
     * - `allowPositiveOverflow`: Whether percentages exceeding 100% are considered valid.
     * - `allowNegativeOverflow`: Whether percentages below 0% are considered valid.
     *
     * @param string the string representation of the percentage value to initialize the `Percentage` instance.
     * @param from0to1 indicates if the input string represents a value in the range `[0, 1]` that should 
     *        be converted to a percentage.
     * @param allowPositiveOverflow determines whether values exceeding 100% are allowed.
     * @param allowNegativeOverflow determines whether values below 0% are allowed.
     * 
     * @throws MalformedInputException if the input string is empty, contains only whitespace, 
     * or cannot be parsed as a valid numerical value.
     * 
     * @since 1.0.0
     */
    @Suppress("LocalVariableName")
    constructor(
        string: String,
        from0to1: Boolean = false,
        allowPositiveOverflow: Boolean = false,
        allowNegativeOverflow: Boolean = false
    ) : this(
        Unit.run {
            val _string = string.trim() - Char.PERCENT
            if (_string.isEmpty()) throw MalformedInputException("The string is empty or contains only whitespace")
            val value = _string.toDoubleOrNull()
                ?: throw MalformedInputException("The string is not a valid percentage number")
            if (from0to1) value / 100.0 else value
        },
        allowPositiveOverflow,
        allowNegativeOverflow
    )

    companion object {
        /**
         * A constant representing an undefined percentage value. This is initialized with `Double.NaN`
         * to indicate that the percentage is not defined or cannot be determined.
         *
         * It can be used in scenarios where an operation involving percentages results in an invalid 
         * or undetermined result, or as a placeholder for uninitialized percentage values.
         *
         * @since 1.0.0
         */
        val UNDEFINED = Percentage(Double.NaN)
        /**
         * A constant representing positive infinity in the context of percentages.
         * This value is derived from `Double.POSITIVE_INFINITY` and is useful for 
         * representing unbounded or infinitely large percentages.
         *
         * This constant is typically utilized in calculations or comparisons 
         * where an infinite percentage is meaningful, such as boundary testing 
         * or overflow scenarios.
         *
         * @since 1.0.0
         */
        val POSITIVE_INFINITY = Percentage(Double.POSITIVE_INFINITY)
        /**
         * Represents the concept of negative infinity within the context of percentage values. 
         * This constant is defined as a `Percentage` instance with its internal numeric value set 
         * to `Double.NEGATIVE_INFINITY`.
         *
         * It is primarily used in calculations or comparisons where negative infinity needs to 
         * be explicitly represented, often to signify an unbounded lower limit in percentage-based
         * operations.
         *
         * @since 1.0.0
         */
        val NEGATIVE_INFINITY = Percentage(Double.NEGATIVE_INFINITY)
        /**
         * A predefined `Percentage` instance representing 0% (zero percent) value.
         *
         * This constant is primarily used when no percentage contribution is intended 
         * or as a baseline percentage for operations.
         *
         * @since 1.0.0
         */
        val ZERO_PERCENT = Percentage(0.0)
        /**
         * A constant representing ten percent as a [Percentage] object.
         * This can be used as a predefined instance for operations requiring a ten percent value.
         *
         * @since 1.0.0
         */
        val TEN_PERCENT = Percentage(10.0)
        /**
         * A predefined constant representing twenty percent (20.0%) as a [Percentage].
         * This value simplifies the handling and representation of twenty percent in operations
         * involving percentages.
         *
         * Use this constant when you need a reusable and clear reference to a twenty percent
         * value within percentage-related calculations or comparisons.
         *
         * @since 1.0.0
         */
        val TWENTY_PERCENT = Percentage(20.0)
        /**
         * Represents a constant value of twenty-five percent (25%).
         * Utilized as a predefined percentage object to avoid redundancy 
         * and improve consistency in contexts requiring a quarter value.
         *
         * Note: The `Percentage` class facilitates arithmetic operations, 
         * comparisons, and conversions, enabling versatile usage within 
         * percentage-based calculations.
         *
         * Example: Can be used in scenarios where a fixed value of 25% is required.
         *
         * @since 1.0.0
         */
        val TWENTYFIVE_PERCENT = Percentage(25.0)
        /**
         * A constant representing thirty percent (30%) as a [Percentage] value.
         * 
         * This value can be used in calculations or comparisons involving percentages
         * where 30% is required. Operations involving `THIRTY_PERCENT` will adhere to
         * the behavior defined in the [Percentage] class.
         * 
         * @since 1.0.0
         */
        val THIRTY_PERCENT = Percentage(30.0)
        /**
         * Represents a percentage value of 40.0%. This constant is commonly used in scenarios 
         * where operations with a fixed percentage of 40% are required. 
         * 
         * The percentage encapsulated here is immutable and provides utility operations 
         * through the `Percentage` class, such as arithmetic operations, comparisons, and 
         * conversions to other numeric types. 
         * 
         * @since 1.0.0
         */
        val FORTY_PERCENT = Percentage(40.0)
        /**
         * A constant representing fifty percent as a predefined [Percentage] instance.
         * This may be used in scenarios requiring a standard representation of half or 50%.
         *
         * @since 1.0.0
         */
        val FIFTY_PERCENT = Percentage(50.0)
        /**
         * A constant representing fifty percent as a predefined [Percentage] instance.
         * This may be used in scenarios requiring a standard representation of half or 50%.
         *
         * @since 1.0.0
         */
        val HALF = Percentage(50.0)
        /**
         * A constant representing sixty percent encapsulated as a [Percentage] instance.
         *
         * This value is commonly used in scenarios where a fixed percentage of 60% is required, 
         * offering a pre-defined and reusable representation.
         *
         * @since 1.0.0
         */
        val SIXTY_PERCENT = Percentage(60.0)
        /**
         * A constant representing 70% as a {@link Percentage} instance.
         *
         * This constant can be used in calculations or comparisons involving percentages.
         * It encapsulates a fixed percentage value of 70.0%.
         *
         * @since 1.0.0
         */
        val SEVENTY_PERCENT = Percentage(70.0)
        /**
         * Represents a constant percentage value of 75.0%.
         *
         * This variable is a predefined instance of the [Percentage] class, initialized with a value of 75.0.
         * It can be used to denote three-quarters (or 75%) in various contexts where percentages are applicable.
         *
         * @since 1.0.0
         */
        val SEVENTYFIVE_PERCENT = Percentage(75.0)
        /**
         * A predefined constant representing 80% as a [Percentage] value.
         * 
         * This value is often used in calculations or situations where 
         * an 80% proportion is required.
         *
         * @see Percentage
         * @since 1.0.0
         */
        val EIGHTY_PERCENT = Percentage(80.0)
        /**
         * A constant representing 90% as a [Percentage] value.
         *
         * This value is commonly used in scenarios requiring a predefined reference to ninety percent.
         * 
         * @since 1.0.0
         */
        val NINETY_PERCENT = Percentage(90.0)
        /**
         * Represents the constant value for 100% as a [Percentage] object.
         *
         * This value provides a standard representation of a fully completed or maximum percentage.
         *
         * @since 1.0.0
         */
        val HUNDRED_PERCENT = Percentage(100.0)
        /**
         * Represents the constant value for 100% as a [Percentage] object.
         *
         * This value provides a standard representation of a fully completed or maximum percentage.
         *
         * @since 1.0.0
         */
        val FULL = Percentage(100.0)

        /**
         * Converts a numeric value into a `Percentage` instance.
         *
         * This method interprets the numeric value and generates a `Percentage` object based on the specified flags.
         * The input can be treated as a normalized value within the range [0, 1] or as a direct percentage value.
         * It also performs input validation to enforce bounds unless positive or negative overflow is explicitly allowed. 
         * The conversion is wrapped in a `Result` to handle potential exceptions safely.
         *
         * @param from0to1 If `true`, the input value is treated as a normalized value in the range [0, 1]. Defaults to `false`.
         * @param allowPositiveOverflow If `true`, values exceeding 100% are allowed. Defaults to `false`.
         * @param allowNegativeOverflow If `true`, values below 0% are allowed. Defaults to `false`.
         * @return A `Result` containing the constructed `Percentage` instance or an exception if the input is invalid.
         * @since 1.0.0
         */
        fun Number.toPercentage(
            from0to1: Boolean = false,
            allowPositiveOverflow: Boolean = false,
            allowNegativeOverflow: Boolean = false
        ) = runCatching { Percentage(this, from0to1, allowPositiveOverflow, allowNegativeOverflow) }
        /**
         * Converts the string representation of a percentage into a `Percentage` instance.
         *
         * The method attempts to parse the string and initializes a `Percentage` object using the provided
         * flags to define the interpretation of the value and its overflow handling. The input is processed
         * based on the `from0to1` flag, and overflow permissions can be controlled through additional parameters.
         *
         * @param from0to1 indicates if the input string represents a value in the range `[0, 1]` that should
         *        be converted to a percentage value by scaling it by 100.
         * @param allowPositiveOverflow determines whether values exceeding 100% are considered valid percentages.
         * @param allowNegativeOverflow determines whether values below 0% are considered valid percentages.
         *
         * @return a `Result` encapsulating the creation of a `Percentage` instance or an error if parsing fails.
         *
         * @since 1.0.0
         */
        fun String.toPercentage(
            from0to1: Boolean = false,
            allowPositiveOverflow: Boolean = false,
            allowNegativeOverflow: Boolean = false
        ) = runCatching { Percentage(this, from0to1, allowPositiveOverflow, allowNegativeOverflow) }

        /**
         * Calculates the specified percentage of a number.
         *
         * This infix function takes a number and computes the percentage value
         * based on the provided percentage argument.
         *
         * @param percent The percentage to calculate, represented as a number.
         * @return The resulting value as a Double after calculating the percentage.
         * @since 1.0.0
         */
        infix fun Number.percent(percent: Number) = (toDouble() / 100.0) * percent.toDouble()
        /**
         * Calculates the specified percentage of a number.
         *
         * This infix function takes a number and computes the percentage value
         * based on the provided percentage argument.
         *
         * @param percent The percentage to calculate, represented as a number.
         * @return The resulting value as a Double after calculating the percentage.
         * @since 1.0.0
         */
        infix fun Number.percent(percent: Percentage) = (toDouble() / 100.0) * percent.toDouble()

        /**
         * Extension property to convert a [Number] to a [Percentage] instance.
         *
         * This property creates a [Percentage] object using the numeric value of the receiver.
         * By default, it allows both positive and negative overflow when converting the value.
         *
         * @receiver The numeric value to be converted to a [Percentage].
         * @return A [Percentage] instance representing the numeric value.
         * @since 1.0.0
         */
        val Number.percent : Percentage get() = Percentage(
            toDouble(),
            allowPositiveOverflow = true,
            allowNegativeOverflow = true
        )
        /**
         * An extension property that converts a numeric value into a `Percentage` object,
         * ensuring the value is clamped within the valid percentage range of 0.0 to 100.0.
         *
         * The original numeric value is coerced to stay within the bounds of this range.
         * If the value is below 0.0, it is set to 0.0; if it is above 100.0, it is set to 100.0.
         *
         * @receiver The numeric value to be converted and clamped.
         * @return A `Percentage` object representing the clamped value.
         * @since 1.0.0
         */
        val Number.clampedPercent : Percentage get() = Percentage(toDouble().coerceIn(0.0, 100.0))

        class Serializer : ValueSerializer<Percentage>() {
            override fun serialize(value: Percentage, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeNumber(value.value)
            }
        }

        class Deserializer : ValueDeserializer<Percentage>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = Percentage(p.numberValue)
        }

        class OldSerializer : JsonSerializer<Percentage>() {
            override fun serialize(value: Percentage, gen: JsonGenerator, serializers: SerializerProvider) = gen.writeNumber(value.value)
        }

        class OldDeserializer : JsonDeserializer<Percentage>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Percentage = Percentage(p.numberValue)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Percentage?, Double?> {
            override fun convertToDatabaseColumn(attribute: Percentage?): Double? = attribute?.value
            override fun convertToEntityAttribute(dbData: Double?): Percentage? = dbData?.let { Percentage(it) }
        }
    }

    /**
     * Compares this Percentage object with the specified Percentage object for order.
     * Returns a negative integer, zero, or a positive integer as this Percentage 
     * is less than, equal to, or greater than the specified Percentage.
     *
     * @param other the Percentage object to be compared with the current instance.
     * @return a negative integer, zero, or a positive integer as this instance 
     * is less than, equal to, or greater than the other instance.
     * @since 1.0.0
     */
    override fun compareTo(other: Percentage) = value.compareTo(other.value)

    /**
     * Adds the value of another Percentage instance to this instance.
     *
     * @param other The Percentage instance to be added to this instance.
     * @return A new Percentage instance with the sum of the two Percentage values.
     * @since 1.0.0
     */
    operator fun plus(other: Percentage) = Percentage(value + other.value)
    /**
     * Adds the given number to the current percentage value and returns 
     * a new Percentage instance with the result.
     *
     * @param other The number to be added to the current percentage.
     * @return A new Percentage instance representing the sum of the current percentage and the given number.
     * @since 1.0.0
     */
    operator fun plus(other: Number) = Percentage(value + other.toDouble())

    /**
     * Subtracts the value of the given `Percentage` instance from the current instance.
     *
     * @param other The `Percentage` instance to subtract from the current instance.
     * @return A new `Percentage` instance representing the result of the subtraction.
     * @since 1.0.0
     */
    operator fun minus(other: Percentage) = Percentage(value - other.value)
    /**
     * Subtracts the specified number from the current percentage value and returns the result 
     * as a new Percentage instance.
     *
     * @param other The number to be subtracted from the current percentage value.
     * @since 1.0.0
     */
    operator fun minus(other: Number) = Percentage(value - other.toDouble())

    /**
     * Multiplies the current Percentage instance with another Percentage instance.
     *
     * @param other The Percentage instance to multiply with the current instance.
     * @return A new Percentage instance representing the product of the two percentages.
     * @since 1.0.0
     */
    operator fun times(other: Percentage) = Percentage(value * other.value)
    /**
     * Multiplies the current `Percentage` by the specified `Number`, returning a new `Percentage` instance.
     *
     * @param other The number to multiply the current `Percentage` value by.
     * @return A new `Percentage` instance representing the result of the multiplication.
     * @since 1.0.0
     */
    operator fun times(other: Number) = Percentage(value * other.toDouble())

    /**
     * Divides the current percentage value by another percentage value.
     *
     * @param other The Percentage instance to divide the current instance by.
     * @return A new Percentage instance representing the result of the division.
     * @since 1.0.0
     */
    operator fun div(other: Percentage) = Percentage(value / other.value)
    /**
     * Divides the current percentage value by the provided number and returns a new Percentage instance.
     *
     * @param other The number by which the percentage value is divided.
     * @return A new Percentage instance representing the result of the division.
     * @since 1.0.0
     */
    operator fun div(other: Number) = Percentage(value / other.toDouble())

    /**
     * Computes the remainder when this percentage value is divided by the specified percentage value.
     *
     * @param other The percentage value to divide by.
     * @return A new `Percentage` instance representing the remainder.
     * @since 1.0.0
     */
    operator fun rem(other: Percentage) = Percentage(value % other.value)
    /**
     * Computes the remainder of the division between the current Percentage value and the given number.
     *
     * @param other The number to divide the current Percentage value by to compute the remainder.
     * @return A new Percentage instance representing the remainder of the division.
     * @since 1.0.0
     */
    operator fun rem(other: Number) = Percentage(value % other.toDouble())

    /**
     * Negates the current percentage value.
     *
     * This operator function allows the use of the unary minus operator (`-`)
     * to create a `Percentage` instance with the negated value of the original.
     *
     * @return A new `Percentage` instance with the negated value.
     * @since 1.0.0
     */
    operator fun unaryMinus() = Percentage(-value)
    /**
     * Returns a new instance of the `Percentage` class with the same value as the current instance.
     * This operator function allows the unary plus operator to be used for the `Percentage` type.
     *
     * @return A new `Percentage` object with the same value.
     * @since 1.0.0
     */
    operator fun unaryPlus() = Percentage(value)

    /**
     * Increments the value of the current Percentage object by 1.0.
     *
     * This operator function allows using the increment operator (++) 
     * to increase the percentage value encapsulated in the current instance.
     *
     * @return A new Percentage instance with its value incremented by 1.0.
     * @since 1.0.0
     */
    operator fun inc() = Percentage(value + 1.0)
    /**
     * Decrements the value of the current `Percentage` instance by 1.0.
     *
     * This operator function allows the `--` operator to be used with the `Percentage` class,
     * returning a new `Percentage` instance with the decremented value.
     *
     * @return A new `Percentage` instance with the value reduced by 1.0.
     * @since 1.0.0
     */
    operator fun dec() = Percentage(value - 1.0)

    /**
     * Returns a string representation of the object.
     *
     * This implementation formats the object as a percentage value 
     * by appending the '%' symbol to the object's value.
     *
     * @return the string representation of the object's value followed by a '%' symbol
     * @since 1.0.0
     */
    override fun toString() = if (value.isDecimal) "$value%" else "${value.toLong()}%"

    /**
     * Converts the current percentage value to a `Byte` type, after first converting it to an `Int`.
     * This operation may involve a narrowing conversion, potentially leading to data loss or truncation
     * if the value exceeds the representable range of a `Byte` (-128 to 127).
     *
     * @return The percentage value represented as a `Byte`.
     * @since 1.0.0
     */
    fun toByte() = value.toInt().toByte()
    /**
     * Converts the encapsulated numerical value of the `Percentage` instance to a `Short`.
     *
     * The conversion is performed by first converting the value to an `Int` and 
     * then casting it to a `Short`.
     *
     * @return The numerical value represented as a `Short`.
     * @since 1.0.0
     */
    fun toShort() = value.toInt().toShort()
    /**
     * Converts the value of this Percentage instance to an integer.
     *
     * This method uses the underlying value of the Percentage to calculate
     * its integer representation. No rounding or overflow detection is performed.
     *
     * @return The integer representation of the value.
     * @since 1.0.0
     */
    fun toInt() = value.toInt()
    /**
     * Converts the encapsulated value to its `Long` representation.
     * 
     * This method ensures that the value is transformed into the corresponding
     * `Long` type. The specific behavior depends on the underlying type of `value`.
     *
     * @return the `Long` representation of the value.
     * @since 1.0.0
     */
    fun toLong() = value.toLong()
    /**
     * Converts the percentage value to a floating-point representation.
     *
     * @param from0to1 A boolean flag indicating whether the result should be normalized within the range 0 to 1
     *                 or as a percentage value (default behavior). When true, the value is divided by 100.
     * @since 1.0.0
     */
    fun toFloat(from0to1: Boolean = false) = if (from0to1) value.toFloat() else value / 100.0f
    /**
     * Converts the stored value to a Double representation.
     * If the parameter `from0to1` is true, the value is divided by 100.0.
     * Otherwise, the original value is returned directly.
     *
     * @param from0to1 Determines if the conversion scales the value to a range between 0.0 and 1.0.
     *                 If true, the value is divided by 100.0. Default is false.
     * @since 1.0.0
     */
    fun toDouble(from0to1: Boolean = false) = if (from0to1) value / 100.0 else value

    /**
     * Rounds the current floating-point value to the nearest integer value.
     *
     * This method uses standard rounding rules where fractional values
     * of 0.5 or greater are rounded up, and less than 0.5 are rounded down.
     *
     * @return The nearest integer to the current floating-point value.
     *
     * @since 1.0.0
     */
    fun roundToInt() = value.roundToInt()
    /**
     * Rounds the value to the closest `Long` value.
     *
     * The rounding follows the standard mathematical rounding rules:
     * - Values with a fractional part less than 0.5 are rounded down to the nearest integer.
     * - Values with a fractional part of 0.5 or greater are rounded up to the nearest integer.
     *
     * This function makes use of Kotlin's `roundToLong` for the rounding operation.
     *
     * @return The rounded `Long` value.
     * @since 1.0.0
     */
    fun roundToLong() = value.roundToLong()

    /**
     * Rounds the value of the current percentage up to the nearest whole number.
     *
     * This function uses the `kotlin.math.ceil` function to compute the smallest 
     * integer value that is greater than or equal to the current percentage value.
     *
     * @return A new `Percentage` instance with the rounded-up value.
     * @since 1.0.0
     */
    fun ceil() = Percentage(kotlin.math.ceil(value))
    /**
     * Returns a new `Percentage` instance whose value is the largest whole number 
     * less than or equal to the current percentage value.
     *
     * This function uses the `kotlin.math.floor` function internally to achieve the 
     * flooring operation on the percentage value.
     *
     * @return A `Percentage` object with the floored value.
     * @since 1.0.0
     */
    fun floor() = Percentage(kotlin.math.floor(value))
    /**
     * Rounds the percentage value to the nearest whole number and returns a new `Percentage` object
     * representing the rounded value.
     *
     * This method internally uses `kotlin.math.round` to perform rounding on the numerical value.
     *
     * @return A new `Percentage` instance with the rounded value.
     * @since 1.0.0
     */
    fun round() = Percentage(kotlin.math.round(value))
}