package dev.tommasop1804.kutils.classes.money

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.coding.JSON
import dev.tommasop1804.kutils.classes.time.TimeZone
import dev.tommasop1804.kutils.classes.time.TimeZoneDesignator
import dev.tommasop1804.kutils.exceptions.ConversionException
import dev.tommasop1804.kutils.exceptions.CurrencyConversionException
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.OffsetDateTime
import java.util.*
import kotlin.reflect.KProperty

/**
 * Represents a monetary value associated with a specific currency.
 *
 * @property currency The currency of the monetary value.
 * @property amount The monetary amount in the specified currency.
 * @property constCurrency Flag indicating if the currency is constant and cannot be changed.
 * @property isPositive Flag indicating if the monetary amount is positive.
 * @property isNegative Flag indicating if the monetary amount is negative.
 * @property isZero Flag indicating if the monetary amount is zero.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Money.Companion.Serializer::class)
@JsonDeserialize(using = Money.Companion.Deserializer::class)
@Suppress("unused")
class Money (amount: BigDecimal = BigDecimal.ZERO, var currency: java.util.Currency) : Number(), Serializable, Comparable<Money> {
    /**
     * Represents the monetary amount for the Money class, automatically scaled
     * to match the currency's default fraction digits using `RoundingMode.HALF_EVEN`.
     *
     * Writing to this property ensures the value is adjusted to the correct number
     * of decimal places as specified by the currency.
     *
     * Note that the `amount` value is immutable after applying `setScale` with
     * the specified precision and rounding mode.
     *
     * 
     * @since 1.0.0
     */
    var amount: BigDecimal = amount.setScale(currency.defaultFractionDigits, RoundingMode.HALF_EVEN)
        set(value) {
            field = value
            field.setScale(currency.defaultFractionDigits, RoundingMode.HALF_EVEN)
        }

    init {
        amount.setScale(currency.defaultFractionDigits, RoundingMode.HALF_EVEN)
    }

    /**
     * Secondary constructor that initializes a `Money` instance using a numeric value and a currency.
     *
     * @param amount The amount of money represented as a `Number`.
     * @param currency The currency of the monetary amount, represented as a `Currency` object.
     * @since 1.0.0
     */
    constructor(amount: Number, currency: java.util.Currency) : this(BigDecimal.valueOf(amount.toDouble()), currency)
    /**
     * Constructs a new `Money` instance using the specified numeric value and currency.
     *
     * @param amount The numeric amount to represent. It will be converted to a `BigDecimal`.
     * @param currency The `Currency` representation of the monetary unit for this `Money` instance.
     * @throws IllegalArgumentException If the provided currency code is invalid or cannot be resolved.
     * @since 1.0.0
     */
    constructor(amount: Number, currency: Currency) :
            this(BigDecimal.valueOf(amount.toDouble()), currency.toJavaCurrency() ?: throw IllegalArgumentException("Invalid currency code"))

    /**
     * Provides a constant reference to the `Currency` instance associated with this `Money` object.
     *
     * This property retrieves the `Currency` by its currency code from the `Currency` class. If the
     * specified currency code cannot be found, an `IllegalStateException` will be thrown.
     *
     * 
     * @throws IllegalStateException if the currency code cannot be resolved into a `Currency` instance.
     * @since 1.0.0
     */
    val constCurrency: Currency
        get() = Currency.of(currency.currencyCode) ?: throw ConversionException("Unable to find currency code: ${currency.currencyCode}")

    /**
     * Indicates whether the monetary amount is positive.
     *
     * Returns `true` if the value of the amount is greater than zero, otherwise `false`.
     * 
     * @since 1.0.0
     */
    val isPositive: Boolean
        get() = amount.signum() == 1

    /**
     * Indicates whether the monetary value represented by this instance is negative.
     *
     * This property evaluates to `true` if the underlying amount has a negative sign.
     * Otherwise, it evaluates to `false`.
     * 
     * @since 1.0.0
     */
    val isNegative: Boolean
        get() = amount.signum() == -1

    /**
     * Indicates whether the current monetary value is zero.
     *
     * This property evaluates the numeric value of the `amount` field and checks if it equals zero.
     * It returns `true` if the value is zero, otherwise `false`.
     * 
     * @since 1.0.0
     */
    val isZero: Boolean
        get() = amount.signum() == 0

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
        @Serial private const val serialVersionUID = 1L

        /**
         * Creates a new instance of `Money` with the specified amount in minor units and the given currency.
         *
         * The amount will be automatically scaled based on the currency's default fraction digits.
         *
         * @receiver The monetary amount in minor units (e.g., cents).
         * @param currency The currency of the monetary value.
         * @since 1.0.0
         */
        infix fun Long.ofMinorCurrencyOf(currency: java.util.Currency) = Money(BigDecimal.valueOf(this, currency.defaultFractionDigits), currency)
        /**
         * Creates a new instance of Money using the specified amount in minor units and the given currency.
         *
         * @receiver The amount in minor units (e.g., cents for USD).
         * @param currency The currency of the money as a [Currency] instance.
         * @return A new Money instance with the specified amount and currency.
         * @throws IllegalArgumentException If the provided currency is invalid.
         * @since 1.0.0
         */
        infix fun Long.ofMinorCurrencyOf(currency: Currency) = ofMinorCurrencyOf(currency.toJavaCurrency() ?: throw IllegalArgumentException("Invalid currency code"))
        /**
         * Creates a new instance of `Money` with the specified amount in minor units and the given currency.
         *
         * The amount will be automatically scaled based on the currency's default fraction digits.
         *
         * @receiver The monetary amount in minor units (e.g., cents).
         * @param currency The currency of the monetary value.
         * @since 1.0.0
         */
        infix fun Short.ofMinorCurrencyOf(currency: java.util.Currency) = Money(BigDecimal.valueOf(toLong(), currency.defaultFractionDigits), currency)
        /**
         * Creates a new instance of Money using the specified amount in minor units and the given currency.
         *
         * @receiver The amount in minor units (e.g., cents for USD).
         * @param currency The currency of the money as a [Currency] instance.
         * @return A new Money instance with the specified amount and currency.
         * @throws IllegalArgumentException If the provided currency is invalid.
         * @since 1.0.0
         */
        infix fun Short.ofMinorCurrencyOf(currency: Currency) = ofMinorCurrencyOf(currency.toJavaCurrency() ?: throw IllegalArgumentException("Invalid currency code"))
        /**
         * Creates a new instance of `Money` with the specified amount in minor units and the given currency.
         *
         * The amount will be automatically scaled based on the currency's default fraction digits.
         *
         * @receiver The monetary amount in minor units (e.g., cents).
         * @param currency The currency of the monetary value.
         * @since 1.0.0
         */
        infix fun Int.ofMinorCurrencyOf(currency: java.util.Currency) = toLong().ofMinorCurrencyOf(currency)
        /**
         * Creates a new instance of Money using the specified amount in minor units and the given currency.
         *
         * @receiver The amount in minor units (e.g., cents for USD).
         * @param currency The currency of the money as a [Currency] instance.
         * @return A new Money instance with the specified amount and currency.
         * @throws IllegalArgumentException If the provided currency is invalid.
         * @since 1.0.0
         */
        infix fun Int.ofMinorCurrencyOf(currency: Currency) = ofMinorCurrencyOf(currency.toJavaCurrency() ?: throw IllegalArgumentException("Invalid currency code"))

        /**
         * Converts a numeric value to a Money object with the specified currency.
         *
         * @receiver The numeric value to be converted to a Money object.
         * @param currency The currency to associate with the Money object.
         * @return A Money object representing the numeric value in the specified currency.
         * @since 1.0.0
         */
        infix fun Number.ofCurrency(currency: java.util.Currency) = Money(BigDecimal.valueOf(toDouble()), currency)
        /**
         * Converts a `Number` to a `Money` object with the specified currency.
         *
         * @receiver The `Number` to be converted to a `Money` object.
         * @param currency The currency to be associated with the resulting `Money` object.
         * @return A `Money` object representing the value of the `Number` in the specified currency.
         * @since 1.0.0
         */
        infix fun Number.ofCurrency(currency: Currency) = Money(BigDecimal.valueOf(toDouble()), currency)
        /**
         * Converts a numeric value to a Money object with the specified currency.
         *
         * @receiver The numeric value to be converted to a Money object.
         * @param currency The currency to associate with the Money object.
         * @return A Money object representing the numeric value in the specified currency.
         * @since 1.0.0
         */
        infix fun BigDecimal.ofCurrency(currency: java.util.Currency) = Money(toDouble(), currency)
        /**
         * Converts a `Number` to a `Money` object with the specified currency.
         *
         * @receiver The `Number` to be converted to a `Money` object.
         * @param currency The currency to be associated with the resulting `Money` object.
         * @return A `Money` object representing the value of the `Number` in the specified currency.
         * @since 1.0.0
         */
        infix fun BigDecimal.ofCurrency(currency: Currency) = Money(toDouble(), currency)

        /**
         * Parses a string representation of a monetary value and returns a [Money] object.
         * The input string should contain a currency code and an amount separated by a space.
         * The order of currency and amount can be specified through the `currencyBefore` parameter.
         *
         * @param s the input string to be parsed, expected to contain a currency code and an amount separated by a space
         * @param currencyBefore a boolean flag indicating whether the currency code comes before the amount
         * (default is true, where the format should be "currency amount"; if false, the format should be "amount currency")
         * @return a [Result] wrapping the parsing operation, which contains the [Money] object if successful
         * @since 1.0.0
         */
        fun parse(s: String, currencyBefore: Boolean = true) = runCatching {
            if (currencyBefore) {
                val (currency, amount) = s.split(" ", limit = 2)
                Money(BigDecimal(amount), java.util.Currency.getInstance(currency))
            } else {
                val (amount, currency) = s.split(" ", limit = 2)
                Money(BigDecimal(amount), java.util.Currency.getInstance(currency))
            }
        }

        class Serializer : ValueSerializer<Money>() {
            override fun serialize(value: Money, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<Money>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Money = parse(p.string)()
        }

        class OldSerializer : JsonSerializer<Money>() {
            override fun serialize(value: Money, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.toString())
            }
        }

        class OldDeserializer : JsonDeserializer<Money>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Money = parse(p.text)()
        }
    }

    /**
     * Creates a new instance of the Money class with modified properties.
     *
     * @param amount the new amount for the Money instance. Defaults to the current amount.
     * @param currency the new currency for the Money instance. Defaults to the current currency.
     * @return a new Money instance with the specified amount and currency.
     * @since 1.0.0
     */
    fun copy(amount: BigDecimal = this.amount, currency: java.util.Currency = this.currency) = Money(amount, currency)
    /**
     * Creates a new instance of Money with the provided amount and currency. If no arguments are supplied, it defaults
     * to using the current instance's amount and a constant currency.
     *
     * @param amount the amount of money for the new instance. Defaults to `this.amount`.
     * @param currency the currency of the new instance. Defaults to `constCurrency`.
     * @since 1.0.0
     */
    fun copy(amount: BigDecimal = this.amount, currency: Currency = constCurrency) = Money(amount, currency)

    /**
     * Converts the current monetary value to a specified currency using exchange rates.
     *
     * The currency conversion values update daily at 16:00 CET.
     *
     * @param currency The target currency to which the monetary value needs to be converted.
     * @return A [Pair] consisting of the converted monetary value as [Money] and
     *         the last update of currency conversion values as [OffsetDateTime].
     * @throws CurrencyConversionException if the conversion fails due to an error in the exchange rate API.
     * @since 1.0.0
     */
    infix fun convert(currency: java.util.Currency): Pair<Money, OffsetDateTime> {
        val response = HttpClient
            .newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .uri("https://api.frankfurter.dev/v1/latest?base=${this.currency.currencyCode}&symbols=${currency.currencyCode}&amount=${amount}".toURI()())
                    .build(),
                HttpResponse.BodyHandlers.ofString()
            ).requireOrThrow({ CurrencyConversionException("Unable to convert money to currency ${currency.currencyCode}") }) { statusCode() in 200..299 }
            .body()
            .then(::JSON)

        return Pair(
            Money(response["rates"]!![currency.currencyCode].asDecimal().toBigDecimal(), currency),
            TimeZoneDesignator.Z.convert(
                if (OffsetTime().isAfter(OffsetTime(16, 0, zoneOffset = TimeZone.CET)))
                    OffsetDateTime(TimeZone.CET).withHour(16).withMinute(0).withSecond(0).withNano(0)
                else OffsetDateTime(TimeZone.CET).minusDays(1).withHour(16).withMinute(0).withSecond(0).withNano(0)
            )
        )
    }

    /**
     * Converts the current monetary value to a specified currency using exchange rates.
     *
     * The currency conversion values update daily at 16:00 CET.
     *
     * @param currency The target currency to which the monetary value needs to be converted.
     * @return A [Pair] consisting of the converted monetary value as [Money] and
     *         the last update of currency conversion values as [OffsetDateTime].
     * @since 1.0.0
     */
    infix fun convert(currency: Currency) = convert(currency.toJavaCurrency() ?: throw IllegalArgumentException("Invalid currency code"))

    /**
     * Adds the given [Money] instance to the current [Money] instance.
     * The addition operation requires both instances to have the same currency.
     *
     *
     * @param other The [Money] instance to be added.
     * @return A new [Money] instance representing the sum of the two [Money] amounts.
     * @throws UnsupportedOperationException if the currencies of the two [Money] instances do not match.
     * @since 1.0.0
     */
    operator fun plus(other: Money): Money {
        if (currency != other.currency) {
            throw UnsupportedOperationException("The currencies must be the same")
        }
        return Money(amount.add(other.amount), currency)
    }

    /**
     * Adds the given [BigDecimal] value to the current monetary amount while preserving the currency.
     *
     * 
     * @param other The [BigDecimal] value to be added to the monetary amount.
     * @since 1.0.0
     */
    operator fun plus(other: BigDecimal) = Money(amount.add(other), currency)

    /**
     * Adds a numerical value to the current Money instance and returns a new Money instance
     * with the updated amount while preserving the currency.
     *
     * 
     * @param other The number to be added to the current Money amount.
     * @return A new Money instance with the updated amount.
     * @since 1.0.0
     */
    operator fun plus(other: Number) = Money(amount.add(BigDecimal.valueOf(other.toDouble())), currency)

    /**
     * Subtracts the specified [Money] amount from this [Money] instance.
     *
     * 
     * @param other the [Money] instance to subtract.
     * @return a new [Money] instance representing the result of the subtraction.
     * @throws UnsupportedOperationException if the currencies of the two [Money] instances are not the same.
     * @since 1.0.0
     */
    operator fun minus(other: Money): Money {
        if (currency != other.currency) {
            throw UnsupportedOperationException("The currencies must be the same")
        }
        return Money(amount.subtract(other.amount), currency)
    }

    /**
     * Subtracts the given [BigDecimal] value from the amount of this [Money] instance,
     * preserving the currency.
     *
     * 
     * @param other The [BigDecimal] value to subtract.
     * @return A new [Money] instance with the updated amount.
     * @since 1.0.0
     */
    operator fun minus(other: BigDecimal) = Money(amount.subtract(other), currency)

    /**
     * Subtracts the given numeric value from the monetary amount, resulting in a new `Money` instance
     * with the adjusted amount. The calculation respects the existing currency of the `Money` instance.
     *
     * 
     * @param other The numeric value to subtract from the `Money` instance. It can be of any type that inherits from `Number`.
     * @since 1.0.0
     */
    operator fun minus(other: Number) = Money(amount.subtract(BigDecimal.valueOf(other.toDouble())), currency)

    /**
     * Multiplies the monetary amount by the given BigDecimal.
     * The operation uses the currency of the current Money instance.
     *
     * 
     * @param other the BigDecimal value to multiply with the monetary amount
     * @since 1.0.0
     */
    operator fun times(other: BigDecimal) = Money(amount.multiply(other), currency)

    /**
     * Multiplies the current monetary value by a given number.
     *
     * 
     * @param other The number by which the monetary value will be multiplied.
     * @since 1.0.0
     */
    operator fun times(other: Number) = Money(amount.multiply(BigDecimal.valueOf(other.toDouble())), currency)

    /**
     * Divides the monetary amount by the given [other] value, rounding the result
     * according to the currency's default fraction digits and using the HALF_EVEN rounding mode.
     *
     * 
     * @param other The [BigDecimal] value by which the monetary amount is divided.
     * @since 1.0.0
     */
    operator fun div(other: BigDecimal) = Money(amount.divide(other, currency.defaultFractionDigits, RoundingMode.HALF_EVEN), currency)

    /**
     * Negates the monetary amount while preserving the currency.
     *
     * This operator provides the unary minus functionality for the `Money` class,
     * returning a new instance of `Money` with a negated amount and the same currency.
     *
     * 
     * @return A new `Money` object with the amount negated and the same currency as the original.
     * @since 1.0.0
     */
    operator fun unaryMinus() = Money(amount.negate(), currency)

    /**
     * Returns the absolute value of the operand.
     *
     * When the unary plus operator is applied, this function provides the absolute value.
     * It effectively ensures that the result is non-negative.
     *
     * 
     * @return The absolute value of the operand.
     * @since 1.0.0
     */
    operator fun unaryPlus() = abs()

    /**
     * Increments the amount represented by this Money instance by one unit.
     *
     * This operator function allows the use of the `++` increment operator with Money instances.
     * The increment operation adds `BigDecimal.ONE` to the current amount, preserving the currency.
     *
     * 
     * @return A new Money instance with its amount increased by one.
     * @since 1.0.0
     */
    operator fun inc() = this + BigDecimal.ONE

    /**
     * Decrements the current `Money` instance by one unit.
     *
     * Subtracts `BigDecimal.ONE` from the monetary amount encapsulated within the `Money` instance.
     * This operator function enables concise and intuitive use of the decrement operator (`--`) for objects of type `Money`.
     *
     * 
     * @since 1.0.0
     */
    operator fun dec() = this - BigDecimal.ONE

    /**
     * Rounds the monetary amount upward to the nearest integer value, preserving the currency.
     * If the value is already an integer, no rounding is performed. This operation ensures
     * that the result is always greater than or equal to the original amount unless the original value
     * is an integer.
     *
     * 
     * @return A new [Money] instance with the rounded monetary amount and the same currency.
     * @since 1.0.0
     */
    fun ceil() = Money(amount.setScale(0, RoundingMode.CEILING), currency)

    /**
     * Adjusts the value of this Money instance by rounding down to the nearest whole number
     * in the context of its currency and precision.
     *
     * This method uses the `RoundingMode.FLOOR` strategy for rounding, meaning it will always
     * round towards the smaller value. Negative values will be rounded down to the next lower
     * absolute integer.
     *
     * 
     * @return A new Money instance with the adjusted value.
     * @since 1.0.0
     */
    fun floor() = Money(amount.setScale(0, RoundingMode.FLOOR), currency)

    /**
     * Rounds the monetary value to the nearest whole unit using the `HALF_EVEN` rounding mode,
     * typically used for financial calculations to minimize cumulative rounding errors.
     *
     * 
     * @return A new `Money` instance with the rounded monetary value and the same currency.
     * @since 1.0.0
     */
    fun round() = Money(amount.setScale(0, RoundingMode.HALF_EVEN), currency)

    /**
     * Computes the absolute value of the monetary amount while preserving the currency.
     *
     * 
     * @return A new Money instance with the absolute value of the amount, retaining the same currency.
     * @since 1.0.0
     */
    fun abs() = Money(amount.abs(), currency)

    /**
     * Compares this Money object with the specified Money object for order.
     * Throws an exception if the currencies of the two Money objects are not the same.
     *
     * 
     * @param other the Money object to be compared
     * @return a negative integer, zero, or a positive integer as this Money object
     *         is less than, equal to, or greater than the specified Money object
     * @throws UnsupportedOperationException if the currencies of the two Money objects are not the same
     * @since 1.0.0
     */
    override operator fun compareTo(other: Money): Int {
        if (currency != other.currency) { throw UnsupportedOperationException("The currencies must be the same") }
        return amount.compareTo(other.amount)
    }

    /**
     * Checks whether this instance is equal to another object.
     *
     * Compares the type and properties of this instance with the given object to determine equality.
     *
     * 
     * @param other The object to compare with this instance for equality.
     * @return `true` if the other object is a `Money` instance with the same `currency` and `amount` as this instance, otherwise `false`.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Money

        if (currency != other.currency) return false
        if (amount.compareTo(other.amount) != 0) return false

        return true
    }

    /**
     * Computes the hash code for the Money object. The hash code is calculated using the hash codes
     * of its primary properties, `currency` and `amount`.
     *
     * 
     * @return An integer representing the hash code of the Money object.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = currency.hashCode()
        result = 31 * result + amount.hashCode()
        return result
    }

    /**
     * Applies a discount to the current monetary value based on the supplied percentage.
     * Returns a new monetary value instance with the discounted amount.
     *
     * @param percentage The discount percentage to apply. This value should be between 0.0 and 100.0 inclusive.
     * @since 1.0.0
     */
    infix fun withDiscountOf(percentage: Number) = copy(
        amount = amount - ((amount / BigDecimal(100)) * BigDecimal.valueOf(percentage.toDouble())),
        currency = currency
    )

    /**
     * Converts the object properties into a map representation.
     *
     * Maps `amount`, `currency`, `constCurrency`, and `currencyCode` to their corresponding values.
     * The `currencyCode` is retrieved from the `currency` property and is expected to be non-null.
     *
     * @return A map containing the properties and their corresponding values.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "amount" to amount,
        "currency" to currency,
        "constCurrency" to constCurrency,
        "currencyCode" to currency.currencyCode.toString(),
    )

    /**
     * Provides the value associated with the given property name from an internal map.
     *
     * - `amount`: The monetary amount represented by the `Money` instance - TYPE: [BigDecimal].
     * - `currency`: The currency of the `Money` instance - TYPE: [java.util.Currency].
     * - `constCurrency`: A constant representing the currency - TYPE: [Currency].
     * - `currencyCode`: The ISO 4217 currency code - TYPE: [String].
     *
     * @param R The type of the value expected to be returned.
     * @param thisRef The reference to the object on which the property is being accessed.
     * @param property The metadata for the property whose value is being retrieved.
     * @return The value associated with the given property name cast to the expected type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R

    /**
     * Converts the monetary amount to a Byte representation.
     * This method uses `byteValueExact()` to ensure that the conversion is precise
     * and will throw an exception if the value cannot be accurately converted.
     *
     * 
     * @return The Byte value of the monetary amount.
     * @throws ArithmeticException If the amount cannot be represented as a Byte.
     * @since 1.0.0
     */
    override fun toByte() = amount.byteValueExact()

    /**
     * Converts the amount of money to a `Short` value.
     *
     * 
     * @return The amount represented as a `Short`.
     * @throws ArithmeticException If the amount cannot be represented as a `Short` without precision loss.
     * @since 1.0.0
     */
    override fun toShort() = amount.shortValueExact()

    /**
     * Converts the amount represented by this Money instance to an integer.
     *
     * If the amount is a decimal, it will be truncated to fit into an integer. This method is typically
     * used when a whole number representation of the monetary amount is required.
     *
     * 
     * @return The integer representation of the amount.
     * @since 1.0.0
     */
    override fun toInt() = amount.toInt()

    /**
     * Converts the monetary amount to its `Long` representation.
     *
     * 
     * @return The monetary amount as a `Long`.
     * @since 1.0.0
     */
    override fun toLong() = amount.toLong()

    /**
     * Converts the monetary amount to a [Float] value.
     *
     * 
     * @return The monetary amount as a [Float].
     * @since 1.0.0
     */
    override fun toFloat() = amount.toFloat()

    /**
     * Converts the monetary amount to its `Double` representation.
     *
     * 
     * @return The monetary amount as a `Double`.
     * @since 1.0.0
     */
    override fun toDouble() = amount.toDouble()

    /**
     * Converts the object to its string representation.
     *
     * 
     * @return A string in the format of the currency code followed by the amount in its plain string representation.
     * @since 1.0.0
     */
    override fun toString() = "${currency.currencyCode} ${amount.toPlainString()}"

    /**
     * Converts the object to its string representation based on the provided symbol style, position of
     * the symbol, and locale.
     *
     * 
     * @param symbolStyle Specifies the style of the symbol to be used in the string representation, such as code, name, or symbol.
     * @param symbolBefore Determines whether the symbol appears before or after the amount. Default value is true if the `symbolStyle` is `SymbolStyle.CODE`.
     * @param locale Specifies the locale to be used for retrieving localized currency information, such as display name or symbol. Defaults to the system's default locale.
     * @return A string representation of the object formatted with the specified symbol style, position, and locale.
     * @since 1.0.0
     */
    fun toString(symbolStyle: SymbolStyle = SymbolStyle.CODE, symbolBefore: Boolean = (symbolStyle == SymbolStyle.CODE), locale: Locale = Locale.getDefault()) = buildString {
        append(if (symbolBefore) "" else amount.toPlainString())
        when (symbolStyle) {
            SymbolStyle.CODE -> append(if (symbolBefore) "" else " ").append(currency.currencyCode)
            SymbolStyle.NAME -> append(if (symbolBefore) "" else " ").append(currency.displayName)
            SymbolStyle.NAME_WITH_LOCALE -> append(if (symbolBefore) "" else " ").append(currency.getDisplayName(locale))
            SymbolStyle.NUMERIC_CODE -> append(if (symbolBefore) "" else " ").append(currency.numericCode)
            SymbolStyle.SYMBOL -> append(if (symbolBefore) "" else " ").append(currency.symbol)
            SymbolStyle.SIMPLIFIED_SIMBOL -> append(if (symbolBefore) "" else " ").append(
                if (amount.compareTo(BigDecimal.ONE) == 0) Currency.of(currency.currencyCode)?.symbol?.first
                else Currency.of(currency.currencyCode)?.symbol?.second
            )
            SymbolStyle.SYMBOL_WITH_LOCALE -> append(if (symbolBefore) "" else " ").append(currency.getSymbol(locale))
        }
        append(if (symbolBefore) (" " + amount.toPlainString()) else "")
    }

    /**
     * Represents the possible styles of symbol representation for currencies.
     * This enum is typically used to define how a currency symbol or name should be displayed.
     * @since 1.0.0
     * @author Tommaso Pastorelli
     */
    enum class SymbolStyle {
        /**
         * Represents an enumeration constant that is used to define the code representation of a symbol style.
         * This format typically refers to an alphanumeric identifier for a specific symbol or entity.
         * @since 1.0.0
         */
        CODE,
        /**
         * Represents the symbolic style for a name.
         * It is a label format typically used to identify or describe an entity by its name within a specific context.
         * @since 1.0.0
         */
        NAME,
        /**
         * Represents the name of a symbol along with its associated locale.
         * This is used to provide localized names for various symbols
         * in the context of the SymbolStyle enum.
         * @since 1.0.0
         */
        NAME_WITH_LOCALE,
        /**
         * Represents the numeric code format style within the SymbolStyle enum.
         * This style is intended for usage where numeric codes are required to identify specific symbols.
         * @since 1.0.0
         */
        NUMERIC_CODE,
        /**
         * Represents the symbol style for a specific context or usage.
         * This can be utilized to format and display the symbol of an object, entity, or system component.
         * @since 1.0.0
         */
        SYMBOL,
        /**
         * Represents a simplified version of a symbol, typically used
         * for more readable or concise display purposes.
         * @since 1.0.0
         */
        SIMPLIFIED_SIMBOL,
        /**
         * Represents a symbol associated with a specific locale for formatting or displaying purposes.
         * Typically used in scenarios requiring localization of symbols within the application.
         * @since 1.0.0
         */
        SYMBOL_WITH_LOCALE
    }
}