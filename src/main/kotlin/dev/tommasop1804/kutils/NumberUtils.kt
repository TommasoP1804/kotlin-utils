/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("NumberUtilsKt")
@file:Suppress("unused", "kutils_take_as_int_invoke", "kutils_drop_as_int_invoke", "java_integer_as_kotlin_int")
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.numbers.*
import dev.tommasop1804.kutils.exceptions.*
import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.*
import kotlin.math.pow
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

/**
 * Indicates whether the current number is not a decimal (i.e., it represents a whole number),
 * with the evaluation based solely on the type of the number.
 *
 * Delegates the determination to the `isNotDecimal` function with the `classBased` parameter set to `true`.
 *
 * @receiver The number instance to check.
 * @since 1.0.0
 */
val Number.isNotDecimalClassBased
    get() = isNotDecimal(true)

/**
 * Indicates whether the current number is not a decimal (represents a whole number).
 *
 * Delegates to an internal helper function to perform the underlying check.
 *
 * @receiver The number instance to evaluate.
 * @return `true` if the number is not a decimal, `false` otherwise.
 * @since 1.0.0
 */
val Number.isNotDecimal
    get() = isNotDecimal()

/**
 * Indicates whether the current number is a decimal type, utilizing a class-based determination method.
 *
 * This property evaluates if the number has a fractional component by relying on its specific class type.
 *
 * @receiver The number to evaluate for being decimal.
 * @return True if the number is a decimal type based on its class, otherwise false.
 * @since 1.0.0
 */
val Number.isDecimalClassBased
    get() = isDecimal(true)

/**
 * Indicates whether the current number is a decimal value.
 *
 * A number is considered decimal if it has a fractional part. This property
 * provides a shorthand to determine the decimal nature of the number without
 * requiring parameters.
 *
 * @receiver The number to check for being decimal.
 * @since 1.0.0
 */
val Number.isDecimal
    get() = isDecimal()

/**
 * Checks if the number is even.
 *
 * An even number is any integer that is divisible by 2 without a remainder.
 *
 * @receiver The number to be checked.
 * @return `true` if the number is even, `false` otherwise.
 * @since 1.0.0
 */
val Number.isEven
    get() = toLong() % 2 == 0.toLong()

/**
 * Determines if the number is odd.
 *
 * This function evaluates whether the current [Number] instance is an odd number.
 * It relies on the complementary `isEven` method for its calculation.
 *
 * @receiver the [Number] instance being evaluated.
 * @return `true` if the number is odd, `false` otherwise.
 * @since 1.0.0
 */
val Number.isOdd
    get() = !isEven

/**
 * Determines if the number is positive.
 *
 * @receiver The number to check.
 * @return `true` if the number is greater than zero, `false` otherwise.
 * @since 1.0.0
 */
val Number.isPositive
    get() = toDouble() > 0.toDouble()

/**
 * Checks if the number is negative.
 *
 * The method converts the receiver [Number] to a [Double] and compares it to zero to determine
 * if the value is negative.
 *
 * @receiver The number to be checked.
 * @return `true` if the number is negative, `false` otherwise.
 * @since 1.0.0
 */
val Number.isNegative
    get() = toDouble() < 0.toDouble()

/**
 * Checks whether a [Number] is not positive.
 *
 * A number is considered not positive if it is less than or equal to zero.
 * This function complements the `isPositive` function.
 *
 * @receiver The number to check.
 * @return `true` if the number is not positive, `false` otherwise.
 * @since 1.0.0
 */
val Number.isNotPositive
    get() = !isPositive

/**
 * Checks if the number is not negative.
 *
 * This function determines whether the given number is greater than
 * or equal to zero by internally leveraging the `isNegative` function.
 * It returns `true` if the number is either zero or positive,
 * and `false` if the number is negative.
 *
 * @receiver The number to be checked.
 * @return `true` if the number is not negative, `false` otherwise.
 * @since 1.0.0
 */
val Number.isNotNegative
    get() = !isNegative

/**
 * Extension property that provides the positive integer representation of the Byte value.
 * If the Byte value is positive, it returns its integer equivalent.
 * If the Byte value is negative, it returns the positive equivalent of its integer representation.
 *
 * This property is a simplified and direct way to access the positive representation of a Byte value,
 * ensuring that the resulting integer is always non-negative.
 *
 * @receiver The Byte value for which the positive integer representation is computed.
 * @since 1.0.0
 */
val Byte.positive
    get() = positiveIfNot()
/**
 * An extension property for the Short type that returns its positive Int equivalent.
 * If the value is negative, it is converted to its positive counterpart.
 * If already positive, it is returned as-is, converted to Int.
 *
 * @receiver The Short value whose positive equivalent is to be returned.
 * @return An Int representing the positive equivalent of the Short value.
 * @since 1.0.0
 */
val Short.positive
    get() = positiveIfNot()
/**
 * A computed property that returns the positive value of the integer.
 * If the integer is already positive, it is returned unchanged.
 * If the integer is non-positive (negative or zero), its positive counterpart is returned.
 *
 * @receiver The integer for which the positive value is computed.
 * @return A positive integer.
 * @since 1.0.0
 */
val Int.positive
    get() = positiveIfNot()
/**
 * Returns the positive equivalent of the given Long value.
 *
 * If the value is already positive, the same value is returned. If the value is negative or zero,
 * its positive counterpart is returned.
 *
 * @receiver The Long value to evaluate.
 * @return The positive equivalent of the value.
 * @since 1.0.0
 */
val Long.positive
    get() = positiveIfNot()
/**
 * Provides a positive equivalent of the Float value.
 *
 * If the Float value is already positive, the value remains unchanged.
 * If the Float value is negative, it returns its positive counterpart.
 *
 * @receiver the Float value to evaluate.
 * @return a positive Float value based on the receiver.
 * @since 1.0.0
 */
val Float.positive
    get() = positiveIfNot()
/**
 * Extension property to retrieve the positive equivalent of a `Double` value.
 * If the value is already positive, it retrieves the value itself. Otherwise, it converts
 * the value to its positive counterpart.
 *
 * @receiver The `Double` value for which the positive equivalent is required.
 * @since 1.0.0
 */
val Double.positive
    get() = positiveIfNot()

/**
 * Retrieves the negative value of the current Byte as an Int, ensuring the result is negative.
 *
 * This property utilizes the `negativeIfNot` method to convert the Byte to its negative value
 * if it is not already negative. If the Byte is already negative, the original value is returned
 * as an Int.
 *
 * @receiver The Byte value to be evaluated and possibly negated.
 * @return The negative value of the Byte as an Int.
 * @since 1.0.0
 */
val Byte.negative
    get() = negativeIfNot()
/**
 * Extension property to evaluate the negative integer representation of a `Short` value.
 * If the `Short` value is negative, the property returns its integer equivalent without changes;
 * otherwise, it returns the negated integer value.
 *
 * @receiver The `Short` value on which the operation is applied.
 * @return The integer representation of the `Short` value, negated if it is not already negative.
 * @since 1.0.0
 */
val Short.negative
    get() = negativeIfNot()
/**
 * Provides the negative equivalent of the integer value.
 * If the integer is already negative, it remains unchanged.
 *
 * This property ensures that the integer is always in its negative form.
 *
 * @since 1.0.0
 */
val Int.negative
    get() = negativeIfNot()
/**
 * An extension property for the Long data type that ensures the value is negative.
 * If the Long is already negative, it remains unchanged. Otherwise, it is converted
 * to its negative equivalent.
 *
 * @receiver The Long value to be evaluated and potentially converted to negative.
 * @return The negative form of the receiver if it was not already negative,
 * or the same value if it was already negative.
 * @since 1.0.0
 */
val Long.negative
    get() = negativeIfNot()
/**
 * A property that ensures the floating-point number is negative.
 *
 * This property uses the `negativeIfNot` extension function to return the negative value of the
 * `Float` if it is not already negative. If the value is already negative, it is returned as is.
 *
 * Useful for normalizing floating-point values to ensure they are always non-positive.
 *
 * @receiver The `Float` value to be evaluated.
 * @return A negative `Float` value, or the original value if it is already negative.
 * @since 1.0.0
 */
val Float.negative
    get() = negativeIfNot()
/**
 * Extension property that returns the negative value of the current Double.
 * If the value is already negative, it is returned unchanged.
 *
 * @receiver the Double to evaluate.
 * @return the negative value of the Double if it is not already negative, or the original value otherwise.
 * @since 1.0.0
 */
val Double.negative
    get() = negativeIfNot()

/**
 * Determines whether the number is a prime number.
 *
 * A prime number is a natural number greater than 1 that is not a product of two
 * smaller natural numbers. This method evaluates the primality of the number by
 * checking divisibility rules.
 *
 * @receiver Number The number to be checked for primality.
 * @return `true` if this number is prime, otherwise `false`.
 * @since 1.0.0
 */
val Number.isPrime
    get() = when (this) {
        0, 1 -> false
        2 -> true
        else -> (3..sqrt(toDouble()).toLong()).none { toDouble().toLong() % it == 0L }
    }

/**
 * Determines if the receiver [Number] is a composite number.
 *
 * A composite number is a positive integer that has at least one positive divisor other than 1 and itself.
 * Any number that is not prime and greater than 1 is considered composite.
 *
 * @receiver The [Number] to check for being composite.
 * @return `true` if the number is composite, `false` otherwise.
 * @since 1.0.0
 */
val Number.isComposite
    get() = !isPrime

/**
 * Calculates and returns the square of the number.
 *
 * The method converts the receiver number to a double and computes its square
 * by raising it to the power of 2.
 *
 * @receiver the number to be squared
 * @return the squared value of the number as a double
 * @since 1.0.0
 */
val Number.square
    get() = toDouble().pow(2)

/**
 * Calculates the cubic (third power) of the receiver number.
 *
 * This method converts the receiver [Number] to a [Double] and raises it
 * to the power of 3, effectively returning the cube of the number.
 *
 * @receiver The number to be cubed.
 * @return The cubic of the receiver as a [Double].
 * @since 1.0.0
 */
val Number.cubic
    get() = toDouble().pow(3)

/**
 * Computes the square root of the number.
 *
 * This function works for any instance of [Number]. It converts the number to a [Double]
 * and calculates the square root using the [kotlin.math.sqrt] method.
 *
 * @receiver The number for which the square root is to be calculated.
 * @return The square root of the number as a [Double].
 * @throws IllegalArgumentException If the number is negative.
 * @since 1.0.0
 */
val Number.sqrt
    get() = sqrt(toDouble())

/**
 * Calculates the cube root of the number.
 *
 * This function computes the cube root of the number by converting it to a Double precision
 * floating-point value and using the `kotlin.math.cbrt` function. It works on all numeric
 * types as it extends the `Number` class.
 *
 * @receiver The number for which the cube root is to be computed.
 * @return The cube root of the receiver as a Double.
 * @since 1.0.0
 */
val Number.cbrt
    get() = cbrt(toDouble())

/**
 * Calculates the base-2 logarithm of the number.
 *
 * @receiver The number for which the base-2 logarithm is to be calculated.
 * @return The base-2 logarithm of the number as a Double.
 * @since 1.0.0
 */
val Number.log2
    get() = log(2.0)

/**
 * Calculates the base-10 logarithm of the number.
 *
 * This function computes the logarithm of the number with base 10.
 * The result is a `Double` representing the logarithm value.
 *
 * @receiver The number for which the logarithm (base 10) is computed.
 * @return The base-10 logarithm of the receiver as a `Double`.
 * @since 1.0.0
 */
val Number.log10
    get() = log(10.0)

/**
 * Computes the natural logarithm (base e) of this number.
 *
 * This function is a shorthand for invoking the `log` function with the base set to Euler's number `e`.
 * It returns the logarithm of the given number in the natural logarithmic scale.
 *
 * @receiver the number for which to calculate the natural logarithm
 * @return the natural logarithm (base e) of the number
 * @since 1.0.0
 */
val Number.logE
    get() = log(Math.E)

/**
 * Checks if the number is a perfect number.
 * A perfect number is a positive integer that is equal to the sum of its proper divisors, excluding itself.
 *
 * @receiver The number to check. Must be convertible to a long.
 * @return `true` if the number is a perfect number, otherwise `false`.
 * @since 1.0.0
 */
val Number.isPerfectNumber: Boolean
    get() {
        var sum = 1L
        for (i in 2..sqrt(toDouble()).toLong()) {
            if (toLong() % i == 0L) {
                sum += if (i == (toLong() / i)) i
                else i + (toLong() / i)
            }
        }
        return sum == toLong() && toLong() != 1L
    }

/**
 * Extension function to calculate the factorial of a number.
 *
 * Factorial of a non-negative number is the product of all positive integers less than or equal to the number.
 * For example, the factorial of 5 (denoted as 5!) is 5 * 4 * 3 * 2 * 1 = 120.
 *
 * @receiver the number for which the factorial is to be calculated. Must be non-negative.
 * @return the factorial of the number as a [BigInteger].
 * @throws NumberSignException if the number is negative.
 * @since 1.0.0
 */
val Number.factorial: BigInt
    get() {
        if (toLong() < 0) throw NumberSignException("Number must be greater than 0")
        if (toLong() == 0L) return BigInt.ONE
        var result = BigInt.ONE
        for (i in 2..toLong()) result *= i.toBigInt()
        return result
    }

/**
 * Extension property for the [Number] class that rounds up the value
 * to the nearest integer greater than or equal to this value.
 *
 * This property leverages the `ceil` function from the Kotlin
 * standard library and converts the [Number] to a [Double]
 * before applying the operation.
 *
 * @receiver A [Number] value to apply the ceiling operation on.
 * @return The smallest integer value greater than or equal to this [Number].
 * @since 1.0.0
 */
val Number.ceil
    get() = ceil(toDouble())

/**
 * Returns the largest integer value less than or equal to the current numeric value.
 * This is calculated by using the mathematical floor operation on the value.
 *
 * The operation considers the number's double-precision floating-point
 * representation for computation.
 *
 * @receiver The numeric value on which the floor operation is applied.
 * @return The largest integer value less than or equal to the current number.
 * @since 1.0.0
 */
val Number.floor
    get() = floor(toDouble())

/**
 * Extension property for rounding a numeric value to the nearest integer.
 * This property converts the current number to a double and applies a rounding operation.
 *
 * @receiver The numeric value to be rounded.
 * @return The rounded value as a double.
 * @since 1.0.0
 */
val Number.round
    get() = round(toDouble())

/**
 * Extension property that returns the signum (sign) of this number.
 * The signum is determined as follows:
 * - Returns 1 if the number is positive.
 * - Returns -1 if the number is negative.
 * - Returns 0 if the number is zero.
 *
 * This property internally converts the number to a double before determining the sign.
 *
 * @receiver Number The numeric value for which the signum is determined.
 * @return Int The sign of the given number.
 * @since 1.0.0
 */
val Number.signum
    get() = sign(toDouble())

/**
 * Converts the current Long value into its corresponding word representation.
 *
 * This property provides a human-readable string representation of the Long value
 * in words. It is achieved by utilizing the `toWords` function, which handles
 * the logic for converting numbers to their equivalent textual format, including
 * support for large numbers and negative values.
 *
 * For example, `123L.words` would return "one hundred twenty-three" as a representation.
 * @since 4.0.0
 */
val Long.words get() = NumberWords.toWords(this)
/**
 * Extension property that converts an integer to its word representation.
 * For example, the integer value `123` would be converted to the string "one hundred twenty-three".
 *
 * This property leverages the `toWords` function from the `NumberWords` utility,
 * which processes the number and outputs its textual equivalent.
 * @since 4.0.0
 */
val Int.words get() = NumberWords.toWords(toLong())
/**
 * Extension property that converts a [Short] value to its equivalent word representation.
 *
 * The conversion uses the `toWords` function to transform the numeric value of the [Short]
 * into a human-readable word format. The word representation will include the number
 * broken down by scales (e.g., thousands, millions) and will handle negative values
 * appropriately.
 *
 * This property is useful for generating textual representations of numbers that may
 * be displayed in user interfaces, reports, or other scenarios where human-readable
 * content is required.
 * @since 4.0.0
 */
val Short.words get() = NumberWords.toWords(toLong())
/**
 * Extension property that converts the value of a Byte into its equivalent English words representation.
 * This is particularly useful for representing numeric values in human-readable text format.
 *
 * The property utilizes the `NumberWords.toWords` function, which handles the logic of
 * converting numeric values to words, supporting numbers of various sizes and handling edge
 * cases like zero or negative values.
 * @since 4.0.0
 */
val Byte.words get() = NumberWords.toWords(toLong())

/**
 * Parses the content of the CharSequence as a numeric value represented in words and returns the result as a [Result].
 *
 * The method attempts to convert textual numeric representations (e.g., "one hundred twenty-three") into a numerical value.
 * It utilizes the `NumberWords.parse` function for the underlying parsing logic.
 * If the parsing succeeds, a [Result] containing the parsed [Long] value is returned.
 * If parsing fails (e.g., due to invalid input), the resulting object contains the exception encountered.
 *
 * @receiver The input [CharSequence] containing the number words to parse.
 * @return A [Result] holding either the parsed numeric value as a [Long], or an exception if parsing fails.
 * @since 4.0.0
 */
fun CharSequence.parseNumberWords() = runCatching { NumberWords.parse(toString()) }

/**
 * Checks if the current [CharSequence] represents a valid numeric value.
 *
 * A numeric value can include:
 * - Optional leading '+' or '-' sign.
 * - Digits with optional decimal point.
 * - Exponential notation (e.g., 'e' or 'E' followed by optional '+' or '-' and digits).
 *
 * @receiver The [CharSequence] to check for numeric validity.
 * @return `true` if the [CharSequence] represents a valid numeric value, otherwise `false`.
 * @since 1.0.0
 */
fun CharSequence.isNumber() = toString().matches("^[+-]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][+-]?\\d+)?$".toRegex())

/**
 * Checks if the current number is not a decimal (i.e., it represents a whole number).
 *
 * Private since `1.2.2`
 * @receiver The number instance to check.
 * @param classBased If `true`, the method will rely solely on the type of the number to determine whether
 * it is not a decimal. If `false`, additional checks will be performed for certain types like `BigDecimal`,
 * `Double`, and `Float` to verify if the value has a fractional part.
 * @return `true` if the number is not a decimal, otherwise `false`.
 * @since 1.0.0
 */
private fun Number.isNotDecimal(classBased: Boolean = false): Boolean {
    if (this is Int || this is Long || this is Short || this is Byte || this is BigInt) return true
    if (this is BigDecimal) return !classBased && (toDouble() % 1 == 0.0)
    if (this is Double) return !classBased && (this % 1 == 0.0)
    if (this is Float) return !classBased && (this % 1 == 0F)
    return false
}

/**
 * Determines if the current number is a decimal value.
 *
 * A number is considered decimal if it has a fractional part. Optionally,
 * the determination can be based on the specific class type of the number
 * when the `classBased` parameter is set to true.
 *
 * Private since `1.2.2`
 *
 * @receiver The number to check for being decimal.
 * @param classBased When true, the check will rely on the actual number class type
 * rather than calculating based on the value of the number.
 * @return True if the number is decimal, otherwise false.
 * @since 1.0.0
 */
private fun Number.isDecimal(classBased: Boolean = false): Boolean = !isNotDecimal(classBased)

/**
 * Converts the current Byte to its negative value if it is not already negative.
 *
 * This method checks if the Byte is negative (`isNegative`). If it is already negative,
 * the original value is returned as an Int. Otherwise, it returns the negated value of
 * the Byte as an Int.
 *
 * @receiver The Byte value to be evaluated and possibly negated.
 * @return The negative value of the Byte as an Int.
 * @since 1.0.0
 */
private fun Byte.negativeIfNot() = if (isNegative) toInt() else -this
/**
 * Returns the integer representation of the current `Short` value.
 * If the value is negative (`isNegative` evaluates to true), it returns the value as is.
 * If the value is not negative, it returns the negated value as an `Int`.
 *
 * @receiver The `Short` value on which the operation is applied.
 * @return The original value as an `Int` if negative, or the negated value as an `Int` if not negative.
 * @since 1.0.0
 */
private fun Short.negativeIfNot() = if (isNegative) toInt() else -this
/**
 * Converts the integer value to its negative equivalent
 * if it is not already negative. If the value is already negative,
 * it remains unchanged.
 *
 * This function checks whether the integer is negative, and if not,
 * it multiplies the value by -1 to ensure it becomes negative.
 *
 * @return The negative equivalent of the integer or the same value if already negative.
 * @since 1.0.0
 */
private fun Int.negativeIfNot() = if (isNegative) this else -this
/**
 * Converts the calling Long to a negative value if it is not already negative.
 * If the Long is already negative, it remains unchanged.
 *
 * @receiver The Long value to be converted to negative if not negative.
 * @return The negative equivalent of the receiver if it was not negative,
 * or the same value if it was already negative.
 * @since 1.0.0
 */
private fun Long.negativeIfNot() = if (isNegative) this else -this
/**
 * Returns the negative value of the floating-point number if it is not already negative.
 * If the floating-point number is negative, it is returned as is.
 *
 * This function is useful to ensure that a `Float` value is always non-positive.
 *
 * @receiver The `Float` value to be evaluated.
 * @return A negative `Float` value, or the original value if it is already negative.
 * @since 1.0.0
 */
private fun Float.negativeIfNot() = if (isNegative) this else -this
/**
 * Returns the negative value of the current Double if it is not already negative.
 * If the value is already negative, it is returned as is.
 *
 * @receiver the Double to evaluate.
 * @return the negative value of the Double if it is not negative already, otherwise the same value.
 * @since 1.0.0
 */
private fun Double.negativeIfNot() = if (isNegative) this else -this

/**
 * Returns the positive integer representation of the current Byte value.
 * If the Byte value is positive, it will return the integer equivalent of the Byte.
 * If the Byte value is not positive, it will return the positive equivalent of its integer representation.
 *
 * @receiver Byte value to be converted to its positive integer representation.
 * @return Positive integer representation of the Byte value.
 * @since 1.0.0
 */
private fun Byte.positiveIfNot() = if (isPositive) toInt() else -this
/**
 * Converts the current Short value to its positive Int equivalent.
 * If the number is already positive, it returns the current value
 * converted to Int. If the number is not positive, it returns
 * the negation of the current value converted to Int.
 *
 * @receiver The Short value to be checked for positivity.
 * @return An Int representing the positive equivalent of the Short value.
 * @since 1.0.0
 */
private fun Short.positiveIfNot() = if (isPositive) toInt() else -this
/**
 * Ensures the integer value is positive. If the integer is already positive, it is returned as is.
 * If the integer is non-positive (negative or zero), its positive counterpart is returned.
 *
 * @receiver The integer to be evaluated.
 * @return A positive integer value.
 * @since 1.0.0
 */
private fun Int.positiveIfNot() = if (isPositive) this else -this
/**
 * Returns the same Long value if it is positive,
 * otherwise returns its positive counterpart.
 *
 * @receiver The Long value to evaluate.
 * @return The same value if it is positive, or the positive equivalent if it is negative or zero.
 * @since 1.0.0
 */
private fun Long.positiveIfNot() = if (isPositive) this else -this
/**
 * Returns the value of the current Float if it is positive.
 * If the Float value is not positive, it returns the positive equivalent of this value.
 *
 * This function ensures the resulting value is always positive.
 *
 * @receiver the Float value to check and possibly convert.
 * @return a positive Float value based on the evaluation of the receiver.
 * @since 1.0.0
 */
private fun Float.positiveIfNot() = if (isPositive) this else -this
/**
 * Ensures the given double value is positive. If the value is already positive,
 * it returns the value as is. Otherwise, it negates the value to make it positive.
 *
 * @receiver The double value to be checked and adjusted if necessary.
 * @return The positive equivalent of the original double value.
 * @since 1.0.0
 */
private fun Double.positiveIfNot() = if (isPositive) this else -this

/**
 * Raises the current Double value to the power of the specified number.
 *
 * WARNING: 0 ^ 0 is considered as 1.
 *
 * @param exponent The exponent to which the base (current Double) is raised.
 * @return The result of raising the current Double to the power of the specified number.
 * @since 1.0.0
 */
infix fun Number.pow(exponent: Number) = toDouble().pow(exponent.toDouble())

/**
 * Calculates the specified root of a number where the root is defined by the given rootIndex.
 *
 * The operation raises the number to the power of the reciprocal of rootIndex.
 *
 * @param rootIndex The degree of the root to calculate. This determines which root (e.g., square root, cube root) will be extracted from the number.
 * @return The result of the root operation as a Double.
 * @since 1.0.0
 */
infix fun Number.root(rootIndex: Number) = pow(1.0 / rootIndex.toDouble())

/**
 * Calculates the logarithm of this number with the specified base.
 *
 * @receiver the number for which the logarithm will be calculated
 * @param base the base of the logarithm
 * @return the logarithm of the number with the given base
 * @since 1.0.0
 */
infix fun Number.log(base: Double) = log(toDouble(), base)

/**
 * Calculates the logarithm of the number with the specified base.
 *
 * @receiver The number for which the logarithm will be calculated.
 * @param base The base of the logarithm. Must be a positive number different from 1.
 * @return The logarithm of the number within the specified base as a Double.
 * @since 1.0.0
 */
infix fun Number.log(base: Int) = log(base.toDouble())

/**
 * Calculates the greatest common divisor (GCD) of two numbers using the Euclidean algorithm.
 *
 * @param a The first number.
 * @param b The second number.
 * @return The greatest common divisor of the two given numbers as a Long.
 * @since 1.0.0
 */
fun gcd(a: Number, b: Number): Long {
    if (b.toLong() == 0L) return a.toLong()
    return gcd(b.toLong(), a.toLong() % b.toLong())
}

/**
 * Calculates the least common multiple (LCM) of the given numbers.
 *
 * This function computes the LCM of all the provided numbers. The LCM is the smallest number
 * that is evenly divisible by all the numbers in the input. If no numbers are provided,
 * an exception will be thrown.
 *
 * @param numbers the numbers for which to calculate the least common multiple
 * @return the LCM of the given numbers as a Long
 * @throws IllegalArgumentException if no numbers are provided
 * @since 1.0.0
 */
fun lcm(vararg numbers: Number): Long {
    if (numbers.isEmpty()) throw IllegalArgumentException("Numbers must not be empty")
    return numbers.map { it.toLong() }.reduce { acc, l -> (acc * l) / gcd(acc, l) }
}

/**
 * Counts the number of digits in the number.
 *
 * @receiver the number whose digits are to be counted
 * @param includeDecimal if true, includes digits after the decimal point;
 *                       if false, considers only the integer part
 * @return the number of digits in the number
 * @since 1.0.0
 */
fun Number.countDigits(includeDecimal: Boolean = true): Int {
    if (includeDecimal) {
        var str = toString()
        if ("." in str) {
            str = str.replace("0*$".toRegex(), "")
            str = str.replace("\\.".toRegex(), "")
        }
        return str.length
    }
    return toLong().toString().length
}

/**
 * Calculates the sum of the digits of the number.
 * If the number is a floating-point number and [includeDecimal] is true,
 * the digits in both the integral and fractional parts are considered.
 * If [includeDecimal] is false, only the digits in the integral part are considered.
 *
 * @receiver The number whose digits' sum needs to be calculated.
 * @param includeDecimal Indicates whether to include the digits of the fractional part for floating-point numbers.
 * Defaults to true.
 * @return The sum of the digits of the number as a [Long].
 * @since 1.0.0
 */
fun Number.sumOfDigits(includeDecimal: Boolean = true): Long {
    if (includeDecimal) {
        var str = toString()
        if ("." in str) {
            str = str.replace("0*$".toRegex(), "")
            str = str.replace("\\.".toRegex(), "")
        }
        return str.toCharArray().sumOf { it.toString().toLong() }
    }
    return toLong().toString().toCharArray().sumOf { it.toString().toLong() }
}

/**
 * Calculates the weighted average of a set of values and their corresponding weights.
 *
 * @param numbers A variable number of pairs where each pair consists of a number and its associated weight
 * in the form of a Percentage object. The weight determines the influence of the value on the average.
 * @return The weighted average as a Double.
 * @throws ArithmeticException If the sum of weights in the collection is zero, as division by zero is not allowed.
 * @since 3.3.5
 */
fun weightedAverage(vararg numbers: Pair<Number, Percentage>) =
    numbers.sumOf { [number, weight] -> number.toDouble() * weight.value } / numbers.sumOf { it.second.value }
/**
 * Calculates the weighted average of the given pairs of numbers and weights.
 *
 * @param numbers A variable number of MonoPair<Number>, where each pair contains a number and its corresponding weight.
 *                The first value of the pair represents the number, and the second value represents the weight.
 * @return The weighted average computed as the sum of the product of each number and its weight,
 *         divided by the total sum of the weights.
 * @throws ArithmeticException If the sum of weights in the collection is zero, as division by zero is not allowed.
 * @since 3.3.5
 */
@JvmName("weightedAverageVarargNumber2")
fun weightedAverage(vararg numbers: Number2) =
    numbers.sumOf { [number, weight] -> number.toDouble() * weight.toDouble() } / numbers.sumOf { it.second.toDouble() }

/**
 * Calculates the weighted average of a collection of number-percentage pairs.
 *
 * Each pair in the collection consists of a numerical value and its corresponding weight,
 * represented as a percentage. The method computes the sum of all weighted values
 * (number multiplied by weight) and divides it by the sum of all weights to determine
 * the weighted average.
 *
 * @receiver An iterable collection of pairs containing a number and a percentage weight.
 * @return The weighted average as a Double.
 * @throws ArithmeticException If the sum of weights is zero, which would result in a division by zero.
 * @since 3.4.0
 */
@JvmName("weightAverageIterablePairNumberPercentage")
fun Iterable<Pair<Number, Percentage>>.weightAverage() =
    sumOf { [number, weight] -> number.toDouble() * weight.value } / sumOf { it.second.value }
/**
 * Calculates the weighted average of a collection of `Number2` instances.
 * Each instance in the collection is expected to represent a pair of a number and its associated weight.
 *
 * The weighted average is derived by summing the products of each number and its weight,
 * and dividing it by the sum of the weights.
 *
 * @receiver An iterable collection of `Number2`, where each item consists of a number and its respective weight.
 * @return The weighted average as a `Double` value.
 * @throws ArithmeticException If the sum of weights in the collection is zero, as division by zero is not allowed.
 * @since 3.4.0
 */
@JvmName("weightAverageIterablePairNumber2")
fun Iterable<Number2>.weightAverage() =
    sumOf { [number, weight] -> number.toDouble() * weight.toDouble() } / sumOf { it.second.toDouble() }

/**
 * Converts the number to its scientific notation representation with the specified number of decimal places.
 *
 * @param decimals The number of decimal places to include in the scientific notation. Defaults to 3.
 * @return A string representing the number in scientific notation with the specified number of decimals.
 * @since 1.0.0
 */
infix fun Number.scientificNotation(decimals: Int = 3): String =
    DecimalFormat("0.${"#" * decimals}E0", DecimalFormatSymbols(Locale.US)).format(toDouble())
/**
 * Converts the BigInteger to its scientific notation representation with the specified number of decimal places.
 *
 * @param decimals The number of decimal places to include in the scientific notation. Defaults to 3.
 * @return A string representing the BigInteger in scientific notation with the specified number of decimals.
 * @since 1.0.0
 */
infix fun BigInt.scientificNotation(decimals: Int = 3): String =
    DecimalFormat("0.${"#" * decimals}E0", DecimalFormatSymbols(Locale.US)).format(this)
/**
 * Converts the BigDecimal to its scientific notation representation with the specified number of decimal places.
 *
 * @param decimals The number of decimal places to include in the scientific notation. Defaults to 3.
 * @return A string representing the BigDecimal in scientific notation with the specified number of decimals.
 * @since 1.0.0
 */
infix fun BigDecimal.scientificNotation(decimals: Int = 3): String =
    DecimalFormat("0.${"#" * decimals}E0", DecimalFormatSymbols(Locale.US)).format(this)

/**
 * Converts the current String to a BigInt object.
 * Assumes the string represents a valid numeric value.
 *
 * @return the BigInt equivalent of the String.
 * @since 1.0.0
 */
@Suppress("kutils_tobiginteger_as_tobigint")
fun String.toBigInt(): Result<BigInt> = runCatching { toBigInteger() }
/**
 * Converts the string representation of a number in the specified radix to a BigInt.
 *
 * @param radix The base to use for the conversion. Must be between 2 and 36 inclusive.
 * @return A BigInt representing the numerical value of the string in the specified radix.
 * @throws NumberFormatException If the string does not represent a valid number for the given radix.
 * @since 1.0.0
 */
fun String.toBigInt(radix: Int): Result<BigInt> = runCatching { toBigInteger(radix) }
/**
 * Converts the string to a [BigInt] if it is a valid representation of a number,
 * or returns `null` if the string is not a valid representation.
 *
 * The function checks the string's content to determine if it can be converted to a [BigInt].
 * If the string represents a valid number within the range supported by [BigInt],
 * the conversion succeeds. Otherwise, it returns `null`.
 *
 * @return The [BigInt] representation of this string, or `null` if the string
 * is not a valid numeric representation.
 * @since 1.0.0
 */
fun String.toBigIntOrNull(): BigInt? = toBigIntegerOrNull()
/**
 * Converts the string argument to a [BigInt], interpreting the value as a number
 * in the specified radix. Returns `null` if the string is not a valid representation
 * of a number in the specified radix or if the value is out of range for a [BigInt].
 *
 * @param radix The radix to use for parsing the string, must be between [Character.MIN_RADIX]
 * and [Character.MAX_RADIX] inclusively.
 * @return The [BigInt] representation of the string in the specified radix, or `null`
 * if the string is not a valid number in that radix or is out of range.
 * @since 1.0.0
 */
fun String.toBigIntOrNull(radix: Int): BigInt? = toBigIntegerOrNull(radix)
/**
 * Converts an integer value to its equivalent BigInt representation.
 *
 * @return The BigInt representation of this integer.
 * @since 1.0.0
 */
@Suppress("java_integer_as_kotlin_int", "kutils_tobiginteger_as_tobigint")
fun Int.toBigInt(): BigInt = toBigInteger()
/**
 * Converts a long value to its equivalent BigInt representation.
 *
 * @return The BigInt representation of this long.
 * @since 1.0.0
 */
@Suppress("java_integer_as_kotlin_int", "kutils_tobiginteger_as_tobigint")
fun Long.toBigInt(): BigInt = toBigInteger()

/**
 * Converts the String representation of a number to a BigDecimal instance.
 *
 * The String must represent a valid number that can be parsed into a BigDecimal.
 * If the String is not a valid numeric representation, an exception may be thrown.
 *
 * @return a BigDecimal representation of the numeric value in the String.
 * @since 1.0.0
 */
fun String.toBigDecimal(): Result<BigDecimal> = runCatching { BigDecimal.valueOf(toDouble()) }
/**
 * Converts the current [Number] instance to a [BigDecimal].
 *
 * This method ensures precise conversion of numeric values to the [BigDecimal] type,
 * preserving the exact numeric representation where possible.
 *
 * @return a [BigDecimal] representation of the current [Number].
 * @since 1.0.0
 */
fun Number.toBigDecimal(): BigDecimal = BigDecimal.valueOf(toDouble())

/**
 * Validates that the number is positive. If the number is not positive, throws a `NumberSignException`.
 *
 * @param causeOf The optional throwable to use as the primary exception; if provided, its cause is set to a new `NumberSignException`.
 * @param cause The optional throwable to be used as the cause of the `NumberSignException`.
 * @return The number itself if it is positive.
 * @throws NumberSignException If the number is not positive.
 * @since 3.5.0
 */
fun <T : Number> T.validatePositive(causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotPositive) throw if (causeOf.isNull()) NumberSignException("Value is not positive.", cause) else causeOf.initCause(NumberSignException("Value is not positive.", cause))
    return this
}
/**
 * Validates that the number is positive. If the number is not positive, a [NumberSignException] is thrown.
 *
 * @param causeOf An optional [Throwable] indicating the cause of the exception. If provided, it will be used as the base for exception chaining.
 * @param cause An optional [Throwable] representing the cause of the exception. Can be null.
 * @param lazyMessage A supplier function for the exception message, which will be lazily evaluated.
 * @return The validated number if it is positive.
 * @throws NumberSignException If the number is not positive.
 * @since 3.5.0
 */
fun <T : Number> T.validatePositive(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    if (isNotPositive) throw if (causeOf.isNull()) NumberSignException(lazyMessage().toString(), cause) else causeOf.initCause(NumberSignException(lazyMessage().toString(), cause))
    return this
}
/**
 * Validates that the current number is positive. If the number is not positive, a `NumberSignException` is thrown.
 *
 * @param property The Kotlin property associated with the validation, providing contextual information
 *                 such as its owner class, name, and return type. May be `null`.
 * @param variableName An optional name of the variable being validated. If provided, it will be included
 *                     in the exception for better error context. Defaults to `null`.
 * @param message An optional custom error message describing the validation failure. If not provided, a default
 *                message "is not positive" will be used. Defaults to `null`.
 * @param causeOf An optional `Throwable` to be used as the cause of the exception. If provided, it will wrap
 *                the `NumberSignException`. Defaults to `null`.
 * @param cause An optional `Throwable` to be included in the `NumberSignException` used for validation. Defaults to `null`.
 * @return The current number (this) if the validation is successful.
 * @throws NumberSignException if the number is not positive.
 * @since 3.5.0
 */
fun <T : Number> T.validatePositive(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotPositive) throw if (causeOf.isNull()) NumberSignException(property, variableName, message ?: "is not positive", cause) else causeOf.initCause(NumberSignException(property, variableName, message ?: "is not positive", cause))
    return this
}
/**
 * Validates if the current number is positive. If the number is not positive, a `NumberSignException` is thrown.
 *
 * @param property The primary property being validated. This is used for exception context and can be null.
 * @param variable An optional secondary property related to the validation. This is used for additional exception context if provided.
 * @param message  An optional custom error message to include in the exception if the validation fails.
 * @param causeOf  An optional root cause throwable to set as the cause of the exception. If null, a new `NumberSignException` is created.
 * @param cause    An optional secondary cause throwable to include when creating the exception.
 * @return The current instance if the validation passes.
 * @throws NumberSignException If the number is not positive.
 * @since 3.5.0
 */
fun <T : Number> T.validatePositive(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotPositive) throw if (causeOf.isNull()) NumberSignException(property, variable, message ?: "is not positive", cause) else causeOf.initCause(NumberSignException(property, variable, message ?: "is not positive", cause))
    return this
}
/**
 * Validates that the number is positive and throws a `NumberSignException` if it is not.
 *
 * @param callable The Kotlin function that the parameter belongs to. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message to include in the exception. Defaults to "is not positive" if null.
 * @param causeOf An existing exception to propagate by initializing its cause with a new `NumberSignException`. Can be null.
 * @param cause An optional underlying cause for the exception. Can be null.
 * @return The current number instance if it is confirmed to be positive.
 * @throws NumberSignException if the number is not positive.
 * @since 3.5.0
 */
fun <T : Number> T.validatePositive(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotPositive) throw if (causeOf.isNull()) NumberSignException(callable, parameterName, message ?: "is not positive", cause) else causeOf.initCause(NumberSignException(callable, parameterName, message ?: "is not positive", cause))
    return this
}
/**
 * Validates that the current number is positive.
 * If the number is not positive, a `NumberSignException` is thrown.
 *
 * @param callable The function to which the parameter belongs, or null if not applicable.
 * @param parameter The specific parameter within the function being validated, or null if not applicable.
 * @param message An optional descriptive message to include in the exception, or null to use the default message.
 * @param causeOf An exception that will serve as the cause of the `NumberSignException`, or null if not applicable.
 * @param cause The underlying cause to associate with this exception, or null if not applicable.
 * @return The current number if it is positive.
 * @throws NumberSignException If the number is not positive.
 * @since 3.5.0
 */
fun <T : Number> T.validatePositive(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotPositive) throw if (causeOf.isNull()) NumberSignException(callable, parameter, message ?: "is not positive", cause) else causeOf.initCause(NumberSignException(callable, parameter, message ?: "is not positive", cause))
    return this
}
/**
 * Validates that the number is positive. If the number is not positive, a `NumberSignException` is thrown.
 *
 * @param callableName The name of the callable where this validation occurs, or null.
 * @param parameterName The name of the parameter being validated, or null.
 * @param message A custom error message to include in the exception, or null to use the default message.
 * @param causeOf An optional throwable that will serve as the originating cause of the new exception, or null.
 * @param cause An optional throwable used to link exceptions for diagnostic purposes, or null.
 * @return The number if it is positive.
 * @throws NumberSignException If the number is not positive.
 * @since 3.5.0
 */
fun <T : Number> T.validatePositive(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotPositive) throw if (causeOf.isNull()) NumberSignException(callableName, parameterName, message ?: "is not positive", cause) else causeOf.initCause(NumberSignException(callableName, parameterName, message ?: "is not positive", cause))
    return this
}
/**
 * Validates that the current number is positive. If the number is not positive, an exception is thrown.
 *
 * @param callableName The name of the callable (e.g., function or method) associated with the validation, or null if not applicable.
 * @param parameter The parameter being validated, or null if not applicable.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to a generic "is not positive" message.
 * @param causeOf An optional pre-existing throwable to be set as the cause of the thrown exception. If null, a new exception is constructed instead.
 * @param cause An optional underlying cause for the validation failure.
 * @return The current number if it passes the validation check.
 * @throws NumberSignException If the number is not positive and no `causeOf` is provided.
 * @since 3.5.0
 */
fun <T : Number> T.validatePositive(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotPositive) throw if (causeOf.isNull()) NumberSignException(callableName, parameter, message ?: "is not positive", cause) else causeOf.initCause(ValidationFailedException(callableName, parameter, message ?: "is not positive", cause))
    return this
}

/**
 * Validates that the current number is not positive. If the number is positive,
 * it throws a `NumberSignException` with the specified cause or an initialized cause.
 *
 * @param causeOf The primary throwable cause to be used if the validation fails.
 *                If non-null, it will be augmented with a `NumberSignException`.
 * @param cause The secondary throwable cause. Used as the cause of the `NumberSignException`
 *              if the number is positive, and `causeOf` is null.
 * @return The current number instance if it is not positive.
 * @throws NumberSignException If the current number is positive.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotPositive(causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isPositive) throw if (causeOf.isNull()) NumberSignException("Value is positive.", cause) else causeOf.initCause(NumberSignException("Value is positive.", cause))
    return this
}
/**
 * Validates that the number is not positive. If the number is positive, an exception is thrown.
 *
 * @param causeOf An optional throwable that will serve as the main cause. If provided, it will be initialized with a
 * secondary cause describing the positive value validation failure.
 * @param cause An optional secondary throwable that may provide additional context for the exception.
 * @param lazyMessage A supplier that generates the message used in the exception if the number is positive.
 * @return The original number if it is not positive.
 * @throws NumberSignException If the number is positive. The exception will use the lazyMessage's output as its
 * message and may include the provided cause and/or causeOf for additional context.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotPositive(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    if (isPositive) throw if (causeOf.isNull()) NumberSignException(lazyMessage().toString(), cause) else causeOf.initCause(NumberSignException(lazyMessage().toString(), cause))
    return this
}
/**
 * Validates that the current number is not positive. If the number is positive, a
 * `NumberSignException` is thrown.
 *
 * @param property An optional `KProperty` providing metadata about the property being validated.
 *                 This can include contextual information such as the class, property name, and
 *                 return type. May be `null`.
 * @param variableName An optional name of the variable being validated for additional context.
 *                     If provided, it will be included in the exception message. May be `null`.
 * @param message An optional custom error message that will be appended to the
 *                exception message if the validation fails. May be `null`.
 * @param causeOf An optional `Throwable` that caused this validation error, allowing exception
 *                chaining. If provided, it is set as the cause of the exception. May be `null`.
 * @param cause An optional `Throwable` providing additional context for the exception. May be
 *              `null`.
 * @return Returns the current number if it is not positive.
 * @throws NumberSignException If the current number is positive.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotPositive(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isPositive) throw if (causeOf.isNull()) NumberSignException(property, variableName, message ?: "is positive", cause) else causeOf.initCause(NumberSignException(property, variableName, message ?: "is positive", cause))
    return this
}
/**
 * Validates that the number is not positive.
 *
 * If the number is positive, a [NumberSignException] is thrown. The exception will include the provided property,
 * variable, an optional custom message, and optional cause details.
 *
 * @param property The primary property being validated. Can be `null` if not applicable.
 * @param variable An optional secondary property related to the validation. Can be `null` if not relevant.
 * @param message An optional custom message to include in the exception if thrown. Defaults to "is positive" if `null`.
 * @param causeOf An optional throwable that is considered the cause of this failure. If provided,
 *                it will be initialized as the cause of the created exception.
 * @param cause An optional root cause of this failure. If provided, it will be passed as part of the created exception.
 * @return Returns the same number instance if it is not positive.
 * @throws NumberSignException if the number is positive.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotPositive(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isPositive) throw if (causeOf.isNull()) NumberSignException(property, variable, message ?: "is positive", cause) else causeOf.initCause(NumberSignException(property, variable, message ?: "is positive", cause))
    return this
}
/**
 * Validates that the number is not positive. If the number is positive, a `NumberSignException` is thrown.
 *
 * @param callable The Kotlin function that the number parameter belongs to. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message A custom error message to provide additional context. Defaults to "is positive" if null.
 * @param causeOf A custom throwable that serves as the main exception. If provided, it is initialized with the
 *                `NumberSignException` as its cause.
 * @param cause A secondary throwable that serves as the cause of the exception. This is used if `causeOf` is null.
 * @return The number itself if the validation passes (i.e., the number is not positive).
 * @throws NumberSignException If the number is positive.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotPositive(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isPositive) throw if (causeOf.isNull()) NumberSignException(callable, parameterName, message ?: "is positive", cause) else causeOf.initCause(NumberSignException(callable, parameterName, message ?: "is positive", cause))
    return this
}
/**
 * Validates that the current number is not positive. If the number is positive, a `NumberSignException` is thrown.
 *
 * @param callable The function to which the parameter belongs, or null if not available.
 * @param parameter The specific parameter within the function that caused the validation, or null if not applicable.
 * @param message An optional descriptive message providing additional information about the validation failure.
 * @param causeOf An optional exception that acts as the underlying cause of the validation failure.
 * @param cause An optional exception to be used as the direct cause of the thrown `NumberSignException`.
 * @return The current number, if it is not positive.
 * @throws NumberSignException if the number is positive.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotPositive(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isPositive) throw if (causeOf.isNull()) NumberSignException(callable, parameter, message ?: "is positive", cause) else causeOf.initCause(NumberSignException(callable, parameter, message ?: "is positive", cause))
    return this
}
/**
 * Validates that the number is not positive. If the number is positive, a `NumberSignException` is thrown.
 *
 * @param callableName The name of the callable associated with this validation, or `null`.
 * @param parameterName The name of the parameter being validated, or `null`.
 * @param message Additional details about the validation failure. Defaults to `null` if not specified.
 * @param causeOf An optional existing `Throwable`, which will have the thrown exception set as its cause.
 * @param cause The throwable that caused this validation to fail, or `null` if there is none.
 * @return The original number if it passes validation (not positive).
 * @throws NumberSignException If the number is positive.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotPositive(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isPositive) throw if (causeOf.isNull()) NumberSignException(callableName, parameterName, message ?: "is positive", cause) else causeOf.initCause(NumberSignException(callableName, parameterName, message ?: "is positive", cause))
    return this
}
/**
 * Validates that the number is not positive. If the number is positive, a `NumberSignException` is thrown.
 *
 * @param callableName The name of the callable (e.g., function or method) associated with this validation, or null if not applicable.
 * @param parameter The parameter related to this validation, or null if not applicable.
 * @param message An optional message providing additional context if validation fails; defaults to "is positive" if not specified.
 * @param causeOf The desired root cause of the exception, or null if none exists.
 * @param cause An optional exception to associate as the cause; it will be attached as the `cause` of the thrown exception.
 * @return The validated number if it is not positive.
 * @throws NumberSignException if the number is positive.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotPositive(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isPositive) throw if (causeOf.isNull()) NumberSignException(callableName, parameter, message ?: "is positive", cause) else causeOf.initCause(NumberSignException(callableName, parameter, message ?: "is positive", cause))
    return this
}

/**
 * Validates that the number is negative. If the number is not negative, an exception is thrown.
 *
 * @param causeOf An optional throwable that will be used as the cause of the exception if provided.
 * @param cause An optional throwable that will be set as the cause of the `NumberSignException` if `causeOf` is not provided.
 * @return The number instance if it is negative.
 * @throws NumberSignException if the number is not negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNegative(causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotNegative) throw if (causeOf.isNull()) NumberSignException("Value is not negative.", cause) else causeOf.initCause(NumberSignException("Value is not negative.", cause))
    return this
}
/**
 * Validates if the calling number instance is negative. If the number is not negative,
 * throws a [NumberSignException] with the specified message and optional cause.
 *
 * @param causeOf An optional `Throwable` that, if provided, will have its cause set to
 *                a newly created [NumberSignException].
 * @param cause An optional `Throwable` representing the cause of the exception to be
 *              assigned to the [NumberSignException]. If null, no cause is set in the
 *              exception.
 * @param lazyMessage A supplier that generates the exception message if the number
 *                    fails the validation.
 * @return The current `Number` instance if the validation passes.
 * @throws NumberSignException If the number is not negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNegative(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    if (isNotNegative) throw if (causeOf.isNull()) NumberSignException(lazyMessage().toString(), cause) else causeOf.initCause(NumberSignException(lazyMessage().toString(), cause))
    return this
}
/**
 * Validates that the current number is negative. If the number is not negative, a [NumberSignException]
 * is thrown.
 *
 * @param property The Kotlin property associated with the validation, providing contextual information
 *                 such as its owner class, name, and return type. May be `null`.
 * @param variableName An optional name of the variable related to the validation for additional context.
 *                     If provided, it will be included in the exception message. Defaults to `null`.
 * @param message An optional custom error message. If provided, it will be used in the exception.
 *                Defaults to `null`.
 * @param causeOf An optional `Throwable` that caused this validation failure, allowing exception chaining.
 *                If provided, it will be used to initialize the cause of the thrown exception. Defaults to `null`.
 * @param cause An optional `Throwable` to provide additional context in the exception. May be `null`.
 *              Defaults to `null`.
 * @return The current number if it passes the validation (is negative).
 * @throws NumberSignException If the number is not negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNegative(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotNegative) throw if (causeOf.isNull()) NumberSignException(property, variableName, message ?: "is not negative", cause) else causeOf.initCause(NumberSignException(property, variableName, message ?: "is not negative", cause))
    return this
}
/**
 * Validates that the current `Number` instance is negative.
 * If the number is not negative, a `NumberSignException` is thrown.
 *
 * @param property The primary property associated with the validation. Can be null.
 * @param variable An optional secondary property used for additional context in the validation. Can be null.
 * @param message A custom error message to provide additional details about the exception. Optional and can be null.
 * @param causeOf An optional throwable that caused this exception. If provided, it is used as the cause of the exception.
 * @param cause An additional optional throwable that will be associated as the cause for the `NumberSignException`. Can be null.
 * @return The current `Number` instance if it is negative.
 * @throws NumberSignException If the number is not negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNegative(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotNegative) throw if (causeOf.isNull()) NumberSignException(property, variable, message ?: "is not negative", cause) else causeOf.initCause(NumberSignException(property, variable, message ?: "is not negative", cause))
    return this
}
/**
 * Validates if the current number is negative. Throws a [NumberSignException] if the number is not negative.
 *
 * @param callable The Kotlin function that the parameter belongs to. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message Custom message providing additional context for the exception. Can be null, defaults to "is not negative".
 * @param causeOf The higher-level cause leading to this validation failure. Can be null.
 * @param cause The underlying exception causing this validation failure. Can be null.
 * @return The current number if the validation passes (i.e., the number is negative).
 * @throws NumberSignException If the current number is not negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNegative(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotNegative) throw if (causeOf.isNull()) NumberSignException(callable, parameterName, message ?: "is not negative", cause) else causeOf.initCause(NumberSignException(callable, parameterName, message ?: "is not negative", cause))
    return this
}
/**
 * Validates if the current number is negative. Throws a `NumberSignException` if the number is not negative.
 *
 * @param callable The function to which the parameter belongs, or null if not applicable.
 * @param parameter The specific parameter within the function to be validated, or null if not applicable.
 * @param message An optional message describing the validation failure, or null to use the default message.
 * @param causeOf An optional `Throwable` that serves as the root cause of the exception.
 * @param cause An optional `Throwable` providing additional context for the exception.
 * @return The current number if it is negative.
 * @throws NumberSignException If the number is not negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNegative(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotNegative) throw if (causeOf.isNull()) NumberSignException(callable, parameter, message ?: "is not negative", cause) else causeOf.initCause(NumberSignException(callable, parameter, message ?: "is not negative", cause))
    return this
}
/**
 * Validates that a number is negative. If the number is not negative, it throws a `NumberSignException`.
 *
 * @param callableName The name of the callable associated with this validation, or null.
 * @param parameterName The name of the parameter being validated, or null.
 * @param message Additional details about the validation failure; defaults to "is not negative" if null.
 * @param causeOf An optional throwable that caused this validation failure. If provided, it will be chained.
 * @param cause An optional cause for the `NumberSignException`. If provided, it will be associated with the exception.
 * @return The number itself if it is negative.
 * @throws NumberSignException If the number is not negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNegative(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotNegative) throw if (causeOf.isNull()) NumberSignException(callableName, parameterName, message ?: "is not negative", cause) else causeOf.initCause(NumberSignException(callableName, parameterName, message ?: "is not negative", cause))
    return this
}
/**
 * Validates if the current number is negative. Throws a `NumberSignException` if the number is not negative.
 *
 * @param callableName The name of the function or callable associated with the validation, or null if not applicable.
 * @param parameter The parameter associated with the validation, or null if not applicable.
 * @param message An optional custom message for the exception, or null to use the default message.
 * @param causeOf The primary cause of the exception, or null if there is no prior throwable causing this validation failure.
 * @param cause The underlying cause of the `NumberSignException`, or null if no additional cause exists.
 * @return The current number (`this`) if it passes the validation check.
 * @throws NumberSignException if the number is not negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNegative(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNotNegative) throw if (causeOf.isNull()) NumberSignException(callableName, parameter, message ?: "is not negative", cause) else causeOf.initCause(NumberSignException(callableName, parameter, message ?: "is not negative", cause))
    return this
}

/**
 * Validates that the calling number is not negative.
 * If the number is negative, it throws a `NumberSignException`.
 * Optionally, a custom cause or an overriding cause can be provided.
 *
 * @param causeOf An optional throwable used as the primary cause when initializing an exception chain.
 * @param cause An optional throwable used as the secondary cause for the exception.
 * @return The validated number if it is not negative.
 * @throws NumberSignException if the number is negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotNegative(causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNegative) throw if (causeOf.isNull()) NumberSignException("Value is negative.", cause) else causeOf.initCause(NumberSignException("Value is negative.", cause))
    return this
}
/**
 * Validates that the number is not negative. If the number is negative, throws a [NumberSignException].
 *
 * @param causeOf An optional throwable to be used as the initial cause for the exception. If null,
 *                [NumberSignException] will be created as the root cause.
 * @param cause An optional throwable that can be specified as the secondary cause for the exception.
 * @param lazyMessage A lazily evaluated message supplier to generate the error message for the exception if thrown.
 * @return The current number if it is not negative.
 * @throws NumberSignException If the number is negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotNegative(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    if (isNegative) throw if (causeOf.isNull()) NumberSignException(lazyMessage().toString(), cause) else causeOf.initCause(NumberSignException(lazyMessage().toString(), cause))
    return this
}
/**
 * Validates that the current number is not negative. If the number is negative, a
 * `NumberSignException` is thrown.
 *
 * @param property The Kotlin property associated with the value being validated, providing
 *                 contextual information such as its owner class, name, and return type. May be `null`.
 * @param variableName An optional name for the variable being validated. If provided, this name will
 *                     be included in the exception message for easier identification. May be `null`.
 * @param message An optional custom error message that will be used if the validation fails. Defaults
 *                to "is negative" if not provided. May be `null`.
 * @param causeOf An optional `Throwable` that represents the cause of this validation failure. If not
 *                `null`, it will be initialized with a `NumberSignException` as its cause. May be `null`.
 * @param cause An optional `Throwable` that provides additional context for the default exception chaining. May be `null`.
 * @return The validated number if the validation passes and the number is not negative.
 * @throws NumberSignException If the number is negative, a `NumberSignException` is thrown with
 *                              appropriate contextual information and optional message or cause.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotNegative(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNegative) throw if (causeOf.isNull()) NumberSignException(property, variableName, message ?: "is negative", cause) else causeOf.initCause(NumberSignException(property, variableName, message ?: "is negative", cause))
    return this
}
/**
 * Validates that the current number is not negative.
 *
 * If the number is negative, a `NumberSignException` is thrown.
 * This method supports optional parameters for providing context about
 * the property or variable related to the exception, a custom message, and
 * an optional cause exception.
 *
 * @param property The primary property associated with this validation. Can be `null`.
 * @param variable An optional secondary property providing additional context. Can be `null`.
 * @param message A custom message to describe the exception if the number is negative. Defaults to `null`.
 * @param causeOf An optional throwable to serve as the cause of the exception. Defaults to `null`.
 * @param cause An optional secondary cause to provide further context. Defaults to `null`.
 * @return The current number if it is not negative.
 * @throws NumberSignException If the number is negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotNegative(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNegative) throw if (causeOf.isNull()) NumberSignException(property, variable, message ?: "is negative", cause) else causeOf.initCause(NumberSignException(property, variable, message ?: "is negative", cause))
    return this
}
/**
 * Validates that the number is not negative.
 *
 * @param callable The function in which the validation is performed. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional message to customize the exception message if validation fails. Defaults to "is negative" if null.
 * @param causeOf The prior throwable cause, used to initialize the chain of exceptions. Can be null.
 * @param cause The root cause of the exception, if applicable. Can be null.
 * @return The number itself if it is not negative.
 * @throws NumberSignException If the number is negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotNegative(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNegative) throw if (causeOf.isNull()) NumberSignException(callable, parameterName, message ?: "is negative", cause) else causeOf.initCause(NumberSignException(callable, parameterName, message ?: "is negative", cause))
    return this
}
/**
 * Validates that the current number is not negative. If the number is negative, a [NumberSignException] is thrown.
 *
 * @param callable The function to which the parameter belongs, or null if not applicable.
 * @param parameter The parameter involved in the validation, or null if not applicable.
 * @param message An optional message to include in the exception, or null for a default message ("is negative").
 * @param causeOf An optional throwable indicating the root cause of this validation failure, or null if not applicable.
 * @param cause An optional throwable to be set as the cause of the exception, or null if not applicable.
 * @return The current number if it is not negative.
 *
 * @throws NumberSignException if the number is negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotNegative(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNegative) throw if (causeOf.isNull()) NumberSignException(callable, parameter, message ?: "is negative", cause) else causeOf.initCause(NumberSignException(callable, parameter, message ?: "is negative", cause))
    return this
}
/**
 * Validates that the current number is not negative. If the number is negative, a
 * `NumberSignException` is thrown. The exception can optionally include information about the
 * callable, parameter, custom message, and cause.
 *
 * @param callableName The name of the callable in which the validation is performed, or null.
 * @param parameterName The name of the parameter being validated, or null.
 * @param message An optional custom message to include in the exception if the validation fails.
 * @param causeOf An optional `Throwable` that serves as the primary cause of the exception.
 * @param cause An optional `Throwable` providing additional context for the exception.
 * @return The original number if the validation passes (i.e., the number is not negative).
 * @throws NumberSignException If the number is negative and validation fails.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotNegative(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNegative) throw if (causeOf.isNull()) NumberSignException(callableName, parameterName, message ?: "is negative", cause) else causeOf.initCause(NumberSignException(callableName, parameterName, message ?: "is negative", cause))
    return this
}
/**
 * Validates that the number is not negative. If the number is negative, throws a `NumberSignException`.
 *
 * @param callableName The name of the callable (e.g., function or method) associated with the value, or null if unassociated.
 * @param parameter The parameter involved in the validation, or null if not applicable.
 * @param message An optional detail message to include with the exception, or null for a default message.
 * @param causeOf An optional throwable that serves as the primary cause of this exception, or null if not applicable.
 * @param cause An optional secondary cause for the exception, or null if not applicable.
 * @return The validated number if it is not negative.
 * @throws NumberSignException If the number is negative.
 * @since 3.5.0
 */
fun <T : Number> T.validateNotNegative(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (isNegative) throw if (causeOf.isNull()) NumberSignException(callableName, parameter, message ?: "is negative", cause) else causeOf.initCause(NumberSignException(callableName, parameter, message ?: "is negative", cause))
    return this
}

private object NumberWords {

    private val UNITS = listOf(
        "zero", "one", "two", "three", "four", "five", "six", "seven",
        "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen",
        "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
    )

    private val TENS = listOf(
        "", "", "twenty", "thirty", "forty", "fifty",
        "sixty", "seventy", "eighty", "ninety"
    )

    // index = power of 1000
    private val SCALES = listOf(
        "", "thousand", "million", "billion", "trillion",
        "quadrillion", "quintillion"
    )

    // ---------- NUMBER -> WORDS ----------

    fun toWords(value: Long): String {
        if (value == 0L) return UNITS[0]
        val negative = value < 0
        var n = abs(value)

        val groups = ArrayDeque<Int>()
        while (n > 0) {
            groups.addFirst((n % 1000).toInt())
            n /= 1000
        }
        groups.size.validate(lazyMessage = { "Number too large" }) { groups.size <= SCALES.size }

        val parts = mutableListOf<String>()
        val scaleBase = groups.size - 1
        groups.forEachIndexed { i, group ->
            if (group == 0) return@forEachIndexed
            val scale = SCALES[scaleBase - i]
            val chunk = threeDigitsToWords(group)
            parts += if (scale.isEmpty()) chunk else "$chunk $scale"
        }

        val result = parts.joinToString(" ")
        return if (negative) "negative $result" else result
    }

    private fun threeDigitsToWords(n: Int): String {
        require(n in 0..999)
        val parts = mutableListOf<String>()
        val hundreds = n / 100
        val rest = n % 100
        if (hundreds > 0) parts += "${UNITS[hundreds]} hundred"
        if (rest > 0) parts += twoDigitsToWords(rest)
        return parts.joinToString(" ")
    }

    private fun twoDigitsToWords(n: Int): String = when {
        n < 20 -> UNITS[n]
        n % 10 == 0 -> TENS[n / 10]
        else -> "${TENS[n / 10]}-${UNITS[n % 10]}"
    }

    // ---------- WORDS -> NUMBER ----------

    private val WORD_VALUES: Map<String, Long> = buildMap {
        UNITS.forEachIndexed { i, w -> put(w, i.toLong()) }
        TENS.forEachIndexed { i, w -> if (w.isNotEmpty()) put(w, (i * 10).toLong()) }
    }

    private val SCALE_VALUES: Map<String, Long> = buildMap {
        put("hundred", 100L)
        SCALES.forEachIndexed { i, w -> if (w.isNotEmpty()) put(w, pow1000(i)) }
    }

    private fun pow1000(exp: Int): Long {
        var r = 1L
        kotlin.repeat(exp) { r *= 1000L }
        return r
    }

    fun parse(text: String): Long {
        val tokens = text.lowercase()
            .replace("-", " ")
            .replace(",", " ")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() && it != "and" }

        if (tokens.isEmpty()) throw ValidationFailedException("Empty input")

        var negative = false
        var idx = 0
        if (tokens[0] == "negative" || tokens[0] == "minus") {
            negative = true; idx = 1
        }

        var total = 0L      // accumulated full result
        var current = 0L    // current group being built (< 1000 before scale applied)

        for (i in idx until tokens.size) {
            val token = tokens[i]
            when {
                WORD_VALUES.containsKey(token) -> current += WORD_VALUES[token]!!
                token == "hundred" -> current *= 100
                SCALE_VALUES.containsKey(token) -> {
                    val scale = SCALE_VALUES[token]!!
                    total += current * scale
                    current = 0
                }
                else -> throw IllegalArgumentException("Unknown token: '$token'")
            }
        }
        val result = total + current
        return if (negative) -result else result
    }
}