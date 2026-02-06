@file:JvmName("NumberUtilsKt")
@file:Suppress("unused", "kutils_take_as_int_invoke", "kutils_drop_as_int_invoke", "java_integer_as_kotlin_int")
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.exceptions.NumberSignException
import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.*
import kotlin.math.pow

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
    if (this is Double) return !classBased && (toDouble() % 1 == 0.0)
    if (this is Float) return !classBased && (toFloat() % 1 == 0F)
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
 * Represents the priority levels used for alternative characters
 * in applications where content styling based on character variations is supported.
 *
 * This enum provides distinct priority levels for rendering or selecting
 * alternative characters in different scenarios. These priorities can
 * be applied depending on the context or the desired effect.
 *
 * @since 1.0.0
 */
enum class AltCharsPriority {
    /**
     * Represents the standard priority level in the `AltCharsPriority` enum.
     *
     * It serves as the default configuration for cases requiring no alternative character prioritization.
     * Use this when no special prioritization for ligatures or alternative characters is necessary.
     *
     * @since 1.0.0
     */
    STANDARD,
    /**
     * Specifies the default priority level for alternative ligature character handling.
     * This priority categorization determines how ligature substitutions are applied
     * relative to other alternative character handling mechanisms.
     *
     * @since 1.0.0
     */
    ALT_LIGA_PRIORITY_DEFAULT,
    /**
     * Represents the default priority setting for alternative characters.
     * This is used when no specific priority is otherwise defined.
     *
     * Belongs to the `AltCharsPriority` enumeration.
     *
     * @since 1.0.0
     */
    ALT_PRIORITY_DEFAULT,
    /**
     * Represents an alternative ligature priority in the AltCharsPriority enum class.
     * This value is used to handle cases where a specific priority is assigned to alternative ligatures.
     *
     * @since 1.0.0
     */
    ALT_LIGA,
    /**
     * Represents the `ALT` enum constant, which is a specific priority level defined in the `AltCharsPriority` enum.
     *
     * This constant can be utilized to indicate and manage alternate character handling logic when working with priority-based systems.
     * It is specifically designed for contexts where this alternative ligature or priority handling is necessary.
     *
     * @since 1.0.0
     */
    ALT
}

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