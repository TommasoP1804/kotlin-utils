/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("NumberUtilsKt")
@file:Suppress("unused", "kutils_take_as_int_invoke", "kutils_drop_as_int_invoke", "java_integer_as_kotlin_int")
@file:Since("1.0.0")
@file:MustUseReturnValues
@file:OptIn(ExperimentalContracts::class)

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.constants.NumberSign
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.classes.numbers.*
import dev.tommasop1804.kutils.classes.range.*
import dev.tommasop1804.kutils.errors.*
import dev.tommasop1804.kutils.exceptions.*
import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.math.*
import kotlin.math.pow
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * A lambda function that when invoked returns the integer value `0`.
 * Represents a constant and immutable zero value provider.
 * @since 5.4.0
 */
val ZERO: Supplier<Int> = { 0 }
/**
 * Represents a lambda expression that returns the constant value `0L` of type `Long`.
 * This can be utilized whenever a consistent zero value of type `Long` is needed.
 * @since 5.4.0
 */
val ZERO_L: Supplier<Long> = { 0L }
/**
 * A lambda function that returns the floating-point value `0.0f`.
 * This can be used as a convenient method to obtain a zero float value.
 * @since 5.4.0
 */
val ZERO_F: Supplier<Float> = { 0f }
/**
 * A lambda expression that returns the double value `0.0`.
 *
 * This can be used as a constant supplier to provide the default
 * zero value for calculations or initializations where a double-precision
 * floating-point number is required.
 * @since 5.4.0
 */
val ZERO_D: Supplier<Double> = { 0.0 }
/**
 * A constant supplier of the short value `0`.
 *
 * This supplier provides a consistent and stateless way
 * to retrieve the short value `0` whenever invoked.
 * It can be used in contexts where a default short value
 * or placeholder is required.
 * @since 5.4.0
 */
val ZERO_S: Supplier<Short> = { 0 }
/**
 * A constant supplier that provides the byte value `0`.
 * This can be used wherever a consistent byte value of `0` is needed.
 * @since 5.4.0
 */
val ZERO_B: Supplier<Byte> = { 0 }

/**
 * A constant supplier that always provides the integer value `1`.
 * This can be used wherever a predefined, unchanging value of `1` is required.
 * @since 5.4.0
 */
val ONE: Supplier<Int> = { 1 }
/**
 * A supplier that provides the constant value `1L` of type `Long`.
 * @since 5.4.0
 */
val ONE_L: Supplier<Long> = { 1L }
/**
 * A constant supplier that provides the float value `1.0` whenever invoked.
 * This can be used in scenarios where a consistent float value of `1.0` is required.
 * @since 5.4.0
 */
val ONE_F: Supplier<Float> = { 1f }
/**
 * A constant supplier that always provides the value 1.0 of type Double.
 * This can be used wherever a predefined constant value of 1.0 is required.
 * @since 5.4.0
 */
val ONE_D: Supplier<Double> = { 1.0 }
/**
 * A constant supplier that provides the value `1` as a `Short`.
 * This can be used wherever a default or constant short value of `1` is needed.
 * @since 5.4.0
 */
val ONE_S: Supplier<Short> = { 1 }
/**
 * A constant supplier that always provides the Byte value 1.
 *
 * This supplier can be used wherever a consistent Byte value of 1 is required.
 * @since 5.4.0
 */
val ONE_B: Supplier<Byte> = { 1 }

/**
 * A constant Supplier that provides the integer value `-1` whenever invoked.
 * This can be used as a predefined value supplier in contexts where a negative one is required.
 * @since 5.4.0
 */
val MINUS_ONE: Supplier<Int> = { -1 }
/**
 * A constant supplier that always provides the value `-1L`.
 *
 * This can be used in scenarios where a consistent supplier of the value `-1L` is required.
 * @since 5.4.0
 */
val MINUS_ONE_L: Supplier<Long> = { -1L }
/**
 * A constant supplier that provides the float value -1.0.
 * This can be used to consistently retrieve the same negative float value
 * in situations where such a value is required.
 * @since 5.4.0
 */
val MINUS_ONE_F: Supplier<Float> = { -1f }
/**
 * A supplier that provides a constant value of -1.0 as a Double.
 * This can be used wherever a consistent supplier of the Double value -1.0 is required.
 * @since 5.4.0
 */
val MINUS_ONE_D: Supplier<Double> = { -1.0 }
/**
 * A constant supplier that provides the value `-1` as a [Short].
 * This supplier can be used whenever a consistent `-1` short value is required.
 * @since 5.4.0
 */
val MINUS_ONE_S: Supplier<Short> = { -1 }
/**
 * A constant supplier that provides the byte value `-1`.
 *
 * This supplier can be used wherever a consistent `-1` byte value is needed.
 * @since 5.4.0
 */
val MINUS_ONE_B: Supplier<Byte> = { -1 }

/**
 * Constant value representing that an index was not found.
 * Typically used to signify the absence of a valid index in search operations.
 * @since 5.4.0
 */
const val INDEX_NOT_FOUND = -1

/**
 * Retrieves the `NumberSign` representation of the current number.
 *
 * This property provides a convenient way to determine whether the number
 * is positive, negative, or zero by mapping it to its corresponding `NumberSign` value.
 *
 * @return `NumberSign.Positive` if the number is positive,
 *         `NumberSign.Negative` if it is negative,
 *         or `NumberSign.Zero` if it is neither.
 * @since 6.1.0
 */
val Number.sign get() = NumberSign from this

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
 * A read-only extension property for the [Number] class that checks if the number is zero.
 *
 * @return `true` if the number is equal to zero, `false` otherwise.
 * @since 5.0.0
 */
val Number.isZero
    get() = this == 0
/**
 * Extension property for the [Number] class that checks if the numeric value is not zero.
 * Returns `true` if the value is not equal to zero, otherwise returns `false`.
 * @since 5.0.0
 */
val Number.isNotZero
    get() = this != 0
/**
 * Extension property that checks if the number is equal to 1.
 *
 * @return `true` if the number equals 1, otherwise `false`.
 * @since 5.4.0
 */
val Number.isOne
    get() = this == 1
/**
 * A property that checks if the numeric value is not equal to 1.
 *
 * @return `true` if the value is not 1, otherwise `false`.
 * @since 5.4.0
 */
val Number.isNotOne
    get() = this != 1
/**
 * Extension property that checks if the number is equal to -1.
 *
 * @return `true` if the number equals -1, otherwise `false`.
 * @since 5.4.0
 */
val Number.isMinusOne
    get() = this == -1
/**
 * A property that checks if the numeric value is not equal to -1.
 *
 * @return `true` if the value is not -1, otherwise `false`.
 * @since 5.4.0
 */
val Number.isNotMinusOne
    get() = this != -1
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
 * @since 5.5.0
 */
val Number.rounded
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
 * An extension property that calculates the sine of a numeric value.
 * The value is converted to a Double before computing the sine using the standard sine function.
 *
 * The result represents the sine of the numeric value, where the value is expected
 * to be in radians. For degree-based calculations, the value must first be converted to radians.
 *
 * @receiver The numeric value for which the sine is to be calculated.
 * @return The sine of the numeric value.
 * @throws ArithmeticException If the computation overflows or results in any error.
 * @since 5.5.0
 */
val Number.sin
    get() = sin(toDouble())
/**
 * Extension property that calculates the cosine of this number.
 * The number is first converted to a double, and then the cosine is computed.
 * Returns the cosine of the angle, where the angle is in radians.
 * Uses the standard library `cos` function under the hood.
 *
 * @receiver Number The number for which the cosine is to be calculated.
 * @return Double The cosine of the number as a double value.
 *
 * @since 5.5.0
 */
val Number.cos
    get() = cos(toDouble())
/**
 * Extension property for calculating the tangent of a number.
 *
 * This property computes the trigonometric tangent of the number,
 * interpreting the value as an angle in radians.
 *
 * Note: The result may be undefined for angles where the tangent
 * function approaches infinity (e.g., π/2, 3π/2, etc.).
 *
 * @receiver A numeric value representing an angle in radians.
 * @return The tangent of the given angle.
 * @throws ArithmeticException If the computation produces an undefined result.
 * @since 5.5.0
 */
val Number.tan
    get() = tan(toDouble())
/**
 * Computes the arcsine (inverse sine) of the number in radians.
 * The value returned is in the range of -π/2 to π/2.
 *
 * This property accesses the value as a [kotlin.math.asin] computation,
 * where the input is converted to a [Double] before evaluation.
 *
 * @receiver The number for which to calculate the arcsine.
 * @return The arcsine of the input number in radians.
 * @throws IllegalArgumentException If the input value is not in the range [-1.0, 1.0].
 *
 * @since 5.5.0
 */
val Number.asin
    get() = asin(toDouble())
/**
 * Extension property that calculates the arc cosine (inverse cosine) of the number.
 * The value is returned in radians, ranging from 0.0 to Pi.
 *
 * This property converts the number to a double before performing the calculation.
 * If the number is outside the range [-1, 1], the result is NaN.
 *
 * @receiver Number on which the arc cosine operation is performed.
 * @return The arc cosine of the number as a double.
 * @throws ArithmeticException If the value is out of the domain for acos calculation.
 *
 * @since 5.5.0
 */
val Number.acos
    get() = acos(toDouble())
/**
 * Extension property for Number to calculate the arc tangent (in radians) of
 * the value represented by the Number instance.
 * Converts the current Number to a Double prior to performing the calculation.
 * The arc tangent is the inverse operation of the tangent, returning a value
 * in the range of -π/2 to π/2.
 *
 * @receiver Number The numeric value for which the arc tangent is to be calculated.
 * @return Double The arc tangent of the numeric value, in radians.
 * @since 5.5.0
 */
val Number.atan
    get() = atan(toDouble())
/**
 * Calculates the arctangent of the pair's components, treating the first component as the y-coordinate
 * and the second component as the x-coordinate, using the `atan2` function. This represents the angle
 * in radians from the x-axis to the point defined by the pair.
 *
 * The result is constrained to the range [-π, π].
 *
 * @receiver A `MonoPair` containing two `Number` values representing coordinates.
 * @return The arctangent of the pair's components in radians.
 * @since 5.5.0
 */
val MonoPair<Number>.atan
    get() = atan2(first.toDouble(), second.toDouble())
/**
 * Returns the hyperbolic sine of this number.
 *
 * The hyperbolic sine function is defined as:
 * sinh(x) = (e^x - e^(-x)) / 2
 *
 * This extension property computes the hyperbolic sine by converting the receiver
 * to a Double and calculating the result using the standard mathematical formula.
 *
 * Note that this property applies to all numeric types through extension and
 * returns the result as a Double.
 *
 * @receiver The number for which to calculate the hyperbolic sine.
 * @return The hyperbolic sine of the receiver as a Double.
 * @throws ArithmeticException If an exceptional condition arises while computing sinh, such as overflow.
 * @since 5.5.0
 */
val Number.sinh
    get() = sinh(toDouble())
/**
 * Extension property that computes the hyperbolic cosine (cosh) of a number.
 *
 * The hyperbolic cosine is defined as (e^x + e^(-x)) / 2, where x is the input number.
 * This property converts the number to a Double before performing the calculation.
 *
 * @receiver The number for which the hyperbolic cosine is to be calculated.
 * @return The hyperbolic cosine of the number as a Double.
 * @since 5.5.0
 */
val Number.cosh
    get() = cosh(toDouble())
/**
 * Calculates the hyperbolic tangent (tanh) of this number.
 *
 * The function returns the hyperbolic tangent of the value,
 * which is defined as (e^x - e^(-x)) / (e^x + e^(-x)), where x is the input.
 *
 * The calculation is based on the double representation of the number.
 *
 * @receiver The number on which the hyperbolic tangent operation will be performed.
 * @return The hyperbolic tangent of this number as a Double.
 * @since 5.5.0
 */
val Number.tanh
    get() = tanh(toDouble())
/**
 * Extension property that calculates the inverse hyperbolic sine (area hyperbolic sine)
 * of a numeric value. The result is computed in radians.
 *
 * This property utilizes the `toDouble()` method to convert the number
 * to a double-precision floating-point value before performing the calculation.
 *
 * @receiver The numeric value for which the inverse hyperbolic sine is computed.
 * @return The inverse hyperbolic sine of the numeric value, in radians.
 * @since 5.5.0
 */
val Number.asinh
    get() = asinh(toDouble())
/**
 * Extension property that computes the inverse hyperbolic cosine (area hyperbolic cosine) of a numeric value.
 * The value must be greater than or equal to 1. Results are computed in the natural logarithmic base (e).
 *
 * @receiver Number whose inverse hyperbolic cosine is to be calculated.
 * @return The inverse hyperbolic cosine of the numeric value as a Double.
 * @throws IllegalArgumentException if the value is less than 1.
 *
 * @since 5.5.0
 */
val Number.acosh
    get() = acosh(toDouble())
/**
 * A property that calculates the inverse hyperbolic tangent (atanh) of a number.
 * The value is computed by converting the current `Number` instance to a `Double`
 * and applying the mathematical atanh function.
 *
 * @receiver The number for which to calculate the inverse hyperbolic tangent.
 * @return The inverse hyperbolic tangent of the number as a `Double`.
 * @since 5.5.0
 */
val Number.atanh
    get() = atanh(toDouble())
/**
 * Computes the length of the hypotenuse of a right-angled triangle
 * using the pair of numeric values as the lengths of the two perpendicular sides.
 *
 * The calculation is performed using the formula: √(a² + b²), where `a` and `b`
 * are the numeric values from the pair.
 *
 * @receiver A pair of numeric values representing the lengths of the two sides.
 * @return The length of the hypotenuse as a `Double`.
 * @since 5.5.0
 */
val MonoPair<Number>.hypot
    get() = hypot(first.toDouble(), second.toDouble())
/**
 * A property that calculates the truncated value of the number.
 * For floating-point numbers, truncation removes the fractional part,
 * leaving only the integer component.
 *
 * @receiver The number to be truncated.
 * @return The truncated value as a number.
 * @since 5.5.0
 */
val Number.truncated
    get() = truncate(toDouble())
/**
 * An extension property that returns the absolute value of the number.
 * Converts the number to a double internally before calculating the absolute value.
 *
 * This property is useful for retrieving non-negative values regardless of the
 * sign of the original number.
 *
 * @receiver Number The original number whose absolute value will be calculated.
 * @return Double The absolute value of the number.
 * @since 5.5.0
 */
val Number.abs
    get() = abs(toDouble())
/**
 * Represents the minimum value between the first and second numeric components
 * of a `MonoPair<Number>` instance. Calculates the minimum by converting both
 * numbers to `Double`.
 *
 * This property simplifies the process of comparing the two numeric values
 * stored in the pair and obtaining the smaller of the two.
 *
 * @receiver A `MonoPair<Number>` instance containing two numeric values to compare.
 * @return The smaller value between the first and second components as a `Double`.
 * @since 5.5.0
 */
val MonoPair<Number>.min
    get() = min(first.toDouble(), second.toDouble())
/**
 * Represents the maximum value between the first and second elements of a `MonoPair<Number>`
 * after being converted to `Double`.
 *
 * This property computes the maximum by invoking the `max` function on the
 * `toDouble` conversion of the pair's elements.
 *
 * Only applicable to instances of `MonoPair` containing numeric values.
 *
 * @since 5.5.0
 */
val MonoPair<Number>.max
    get() = max(first.toDouble(), second.toDouble())
/**
 * Returns the floating-point value adjacent to this number in the direction of positive infinity.
 *
 * If this number is a finite value, this property returns the next larger representable floating-point value.
 * If this number is positive infinity, the result is positive infinity.
 * If this number is NaN (Not a Number), the result is NaN.
 *
 * This property ensures compatibility with IEEE 754 floating-point arithmetic standards.
 *
 * @since 5.5.0
 */
val Number.nextUp
    get() = toDouble().nextUp()
/**
 * Retrieves the floating-point value that is immediately less than this number.
 * If this number is a finite value, it returns the next representable double value
 * closer to negative infinity. If the number is positive or negative infinity,
 * it will return itself. For NaN (Not-a-Number) values, the behavior is undefined.
 *
 * @receiver The number for which the next lower representable value is to be retrieved.
 * @return The next representable double value smaller than this number.
 * @since 5.5.0
 */
val Number.nextDown
    get() = toDouble().nextDown()

/**
 * Provides a string representation of the current [Number] in words, including
 * both the integer and fractional parts (if any). For fractional parts, each digit
 * after the decimal point is translated into its corresponding word and joined with "point."
 * Example: 3.14 -> "three point one four".
 * @since 4.8.0
 */
val Number.words get() = NumberWords.toWords(toDouble().toBigDecimal())
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
 * Returns the value of the Byte if it is non-null; otherwise, returns 0.
 *
 * This function is a convenient way to handle nullable Byte values
 * by providing a default value of 0 when the value is null.
 *
 * @receiver The nullable Byte value.
 * @return The non-null Byte value, or 0 if the receiver is null.
 * @since 5.4.0
 */
fun Byte?.orZero() = this ?: 0
/**
 * Returns the value of the nullable [Short] if it is not null,
 * or `0` if the value is null.
 *
 * This extension function provides a convenient way to handle nullable
 * [Short] types by ensuring a non-null result.
 *
 * @receiver The nullable [Short] value being checked.
 * @return The receiver value if it is not null, or `0` if it is null.
 * @since 5.4.0
 */
fun Short?.orZero() = this ?: 0
/**
 * Returns the value of the integer if it is not null; otherwise, returns zero.
 *
 * This function is an extension on nullable integers, providing a convenient way
 * to handle null values by substituting them with a zero.
 *
 * @receiver The nullable integer to be checked.
 * @return The integer value if not null, or zero if the integer is null.
 * @since 5.4.0
 */
fun Int?.orZero() = this ?: 0
/**
 * Returns the value of the nullable `Long` if it is not null,
 * or returns 0L if the value is null.
 *
 * This function provides a default value of 0L for nullable `Long` objects.
 *
 * @receiver The nullable `Long` instance to evaluate.
 * @return The current value if not null, or 0L if null.
 * @since 5.4.0
 */
fun Long?.orZero() = this ?: 0L
/**
 * Returns the value of the Float if it is non-null, or `0f` if it is null.
 *
 * This function provides a safe way to handle nullable Float values by
 * replacing `null` with a default value of `0f`.
 *
 * @receiver The nullable Float to be evaluated.
 * @return The non-null Float value, or `0f` if the receiver is null.
 * @since 5.4.0
 */
fun Float?.orZero() = this ?: 0f
/**
 * Returns the value of the nullable [Double] if it is non-null; otherwise, returns 0.0.
 *
 * This function provides a convenient way to handle nullable [Double] values by
 * substituting a default value of 0.0 when the input is null.
 *
 * @receiver The nullable [Double] value to evaluate.
 * @return The original value if it is non-null; otherwise, 0.0.
 * @since 5.4.0
 */
fun Double?.orZero() = this ?: 0.0

/**
 * Returns the value of the Byte if it is not null; otherwise, returns 1.
 *
 * This function provides a safe way to handle nullable Byte values
 * by substituting a default value of 1 when the value is null.
 *
 * @receiver The nullable Byte value on which the function is called.
 * @return The Byte value if not null, or 1 if the Byte is null.
 * @since 5.4.0
 */
fun Byte?.orOne() = this ?: 1
/**
 * Returns the value of the current Short if it is not null, or `1` if the value is null.
 *
 * This function provides a default value of `1` for nullable Short instances,
 * ensuring that a non-null Short value is always returned.
 *
 * @receiver The nullable Short value to evaluate.
 * @return The current Short value if it is not null, otherwise `1`.
 * @since 5.4.0
 */
fun Short?.orOne() = this ?: 1
/**
 * Returns the integer value of the receiver if it is not null;
 * otherwise, returns 1 as the default value.
 *
 * This extension function is useful in scenarios where a nullable integer
 * needs to be replaced with a default value of 1 when null.
 *
 * @receiver The nullable integer to check.
 * @return The value of the receiver if not null, or 1 if the receiver is null.
 * @since 5.4.0
 */
fun Int?.orOne() = this ?: 1
/**
 * Returns the value of the Long if it is not null; otherwise, returns 1L.
 *
 * This function provides a default value of 1L for nullable Long values.
 *
 * @receiver The nullable Long value to check.
 * @return The original Long value if it is not null, or 1L if the receiver is null.
 * @since 5.4.0
 */
fun Long?.orOne() = this ?: 1L
/**
 * Returns the Float value if it is not null, or `1f` if the value is null.
 *
 * This extension function provides a default value of `1f` for nullable Float values.
 *
 * @receiver A nullable Float value.
 * @return The original Float value if it is not null, otherwise `1f`.
 * @since 5.4.0
 */
fun Float?.orOne() = this ?: 1f
/**
 * Returns the value of the Double if it is not null; otherwise, returns 1.0.
 *
 * This extension function provides a fallback value of 1.0 for nullable Double
 * instances, commonly used to avoid null checks or provide default behavior.
 *
 * @receiver Nullable Double value.
 * @return The original Double value if it is not null, otherwise 1.0.
 * @since 5.4.0
 */
fun Double?.orOne() = this ?: 1.0

/**
 * Returns the value of the Byte if it is not null; otherwise, returns -1.
 *
 * This method provides a convenient way to handle nullable Byte values
 * by replacing a null value with -1.
 *
 * @return The Byte value if it is not null, or -1 if it is null.
 * @since 5.4.0
 */
fun Byte?.orMinusOne() = this ?: -1
/**
 * Returns the value of the nullable Short if it is not null;
 * otherwise, returns -1.
 *
 * This extension function provides a default value of -1
 * for nullable Short instances, simplifying operations
 * where a default value is required if the original value is null.
 *
 * @receiver The nullable Short value to evaluate.
 * @return The original Short value if not null, or -1 if the receiver is null.
 * @since 5.4.0
 */
fun Short?.orMinusOne() = this ?: -1
/**
 * Returns the value of this nullable [Int], or `-1` if the value is `null`.
 *
 * This function provides a convenient way to handle nullable [Int] values
 * by substituting `-1` when the value is absent (null), ensuring a non-null
 * return value.
 *
 * @receiver A nullable [Int] that may be null.
 * @return The value of the [Int] if it is not null; otherwise, `-1`.
 * @since 5.4.0
 */
fun Int?.orMinusOne() = this ?: -1
/**
 * Returns the value of the nullable [Long] if it is not null, or `-1L` if it is null.
 *
 * This function provides a safe way to handle nullable [Long] values,
 * ensuring a default value of `-1L` in cases where the receiver is null.
 *
 * @receiver The nullable [Long] value to evaluate.
 * @return The receiver value if it is not null, or `-1L` if it is null.
 * @since 5.4.0
 */
fun Long?.orMinusOne() = this ?: -1L
/**
 * Returns the value of the nullable Float if it is not null, or -1.0f if it is null.
 *
 * This function provides a way to handle nullable Float values gracefully
 * by substituting a default value of -1.0f when the Float is null.
 *
 * @receiver The nullable Float value to evaluate.
 * @return The original Float value if not null, or -1.0f if null.
 * @since 5.4.0
 */
fun Float?.orMinusOne() = this ?: -1f
/**
 * Returns the value of the current nullable Double if it is not null,
 * or returns -1.0 if the value is null.
 *
 * This extension function provides a default value of -1.0 for nullable
 * Double instances, allowing for safe handling of null values without
 * the need for additional checks.
 *
 * @receiver The nullable Double instance.
 * @return The original value if not null, or -1.0 if the receiver is null.
 * @since 5.4.0
 */
fun Double?.orMinusOne() = this ?: -1.0

/** Long esatto, o `null` se la conversione perderebbe informazione (decimali, NaN, infiniti, overflow). */
@PublishedApi
internal fun Number.exactLongOrNull(): Long? = when (this) {
    is Byte, is Short, is Int, is Long -> toLong()
    is Float -> toDouble().exactLongOrNull()
    is Double -> takeIf { it.isFinite() && it == truncate(it) && it >= -TWO_POW_63 && it < TWO_POW_63 }?.toLong()
    is BigInteger -> takeIf { bitLength() < Long.SIZE_BITS }?.toLong()
    is BigDecimal -> try { longValueExact() } catch (e: ArithmeticException) { null }
    is Base36 -> toLong()
    else -> null
}

private const val TWO_POW_63 = Long.MAX_VALUE.toDouble()

/** Narrowing generico: converte in Long esatto, verifica il range, applica [build]. */
@PublishedApi
internal inline fun <reified N : Number, T : Any> N.narrow(
    range: LongRange,
    target: KType,
    build: (Long) -> T,
): Either<InvalidTypeConversion, T> =
    exactLongOrNull()
        ?.takeIf { it in range }
        ?.let(build)
        .rightIfNotNull { InvalidTypeConversion(this, typeOf<N>(), target, reason = "Number out of valid range for type ${target.simpleName}") }

@PublishedApi
internal fun <T : Any> String.parseOrError(target: KType, parse: (String) -> T): Either<InvalidTypeFormat, T> =
    either { catching({ parse(this@parseOrError) }) { _: NumberFormatException -> raise(InvalidTypeFormat(this@parseOrError, target)) } }

/**
 * Converts the current number to a `Byte` if it falls within the valid range for a `Byte`
 * (from `Byte.MIN_VALUE` to `Byte.MAX_VALUE`), or returns an error if the value is out of range.
 *
 * This method utilizes the `narrow` function to perform range validation and conversion, ensuring
 * that the number can safely be cast to a `Byte`. If the conversion is not possible due to the
 * number being out of bounds, an appropriate error is returned.
 *
 * @receiver The number to be converted.
 * @return An `Either` containing the successfully converted `Byte` value if the conversion is valid,
 *         or a `NumberConversionError` if the conversion cannot be performed.
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toByteOrError() =
    narrow(Byte.MIN_VALUE.toLong()..Byte.MAX_VALUE.toLong(), typeOf<Byte>(), Long::toByte)

/**
 * Converts a number of type [N] to a [Short], verifying if the number falls within
 * the valid range for [Short] type values. If the conversion is not possible due to
 * an out-of-range value, it throws an error.
 *
 * This function uses the [narrow] helper method to:
 * - Convert the number to a [Long] for validation.
 * - Check if the value falls within the range of [Short.MIN_VALUE] to [Short.MAX_VALUE].
 * - Apply the conversion to a [Short] if the validation succeeds.
 *
 * @receiver The numeric value of type [N] to be converted.
 * @return The [Short] representation of the number if the conversion is valid.
 *
 * @see narrow
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toShortOrError() =
    narrow(Short.MIN_VALUE.toLong()..Short.MAX_VALUE.toLong(), typeOf<Short>(), Long::toShort)

/**
 * Attempts to convert the current number to an `Int`.
 *
 * This function ensures that the value adheres to the range of valid `Int` values,
 * defined by `Int.MIN_VALUE` and `Int.MAX_VALUE`. If the value cannot be safely converted
 * to an `Int`, the operation will fail and return an error wrapped in a `NumberConversionError`.
 *
 * This method leverages the `narrow` utility, which performs the following:
 * - Converts the number to a `Long` for exact range validation.
 * - Verifies whether the number is within the range of valid `Int` values.
 * - Applies a transformation to convert the valid value into an `Int`.
 * - Provides error handling for cases where the conversion fails.
 *
 * @receiver The number instance to be converted to an `Int`.
 * @return Either a successful conversion result of type `Int`, or an error of type `NumberConversionError`.
 *
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toIntOrError() =
    narrow(Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong(), typeOf<Int>(), Long::toInt)

/**
 * Attempts to convert the current number instance of type `N` into a `Long`.
 * If the conversion is successful, it returns an `Either.Right` containing the `Long` value.
 * If the conversion fails due to loss of precision, overflow, or the number being incompatible
 * with `Long`, it returns an `Either.Left` containing a `NumberConversionError` with details
 * about the failure.
 *
 * @return An `Either` containing `Right` with the resulting `Long` on success, or `Left` with
 *         a `NumberConversionError` on failure.
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toLongOrError(): Either<InvalidTypeConversion, Long> =
    exactLongOrNull().rightIfNotNull { InvalidTypeConversion(this, typeOf<N>(), typeOf<Long>()) }

/**
 * Converts the current [Number] to a [UByte] if it falls within the valid range for [UByte],
 * otherwise returns an error of type [InvalidTypeConversion].
 *
 * This method utilizes the `narrow` utility function to perform the conversion. The conversion
 * process includes verifying that the numeric value can be safely represented as a [UByte],
 * ensuring it falls within the range of `0` to `UByte.MAX_VALUE`.
 *
 * @receiver The [Number] to be converted.
 * @return An instance of [Either], which will hold a [UByte] if the conversion is successful,
 * or a [InvalidTypeConversion] if the value is out of range or cannot be converted.
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toUByteOrError() =
    narrow(0L..UByte.MAX_VALUE.toLong(), typeOf<UByte>(), Long::toUByte)

/**
 * Converts this [Number] instance to an [UShort] if the value falls within the valid [UShort] range.
 * Throws a [InvalidTypeConversion] if the conversion is not possible due to the number being out of range
 * or any other incompatibility.
 *
 * This function leverages the `narrow` method to ensure the value is first cast to a `Long`, validated
 * against the range of valid [UShort] values, and then converted to an [UShort] if valid.
 *
 * @return An [UShort] representation of this number if the conversion is successful.
 * @receiver The number to be converted.
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toUShortOrError() =
    narrow(0L..UShort.MAX_VALUE.toLong(), typeOf<UShort>(), Long::toUShort)

/**
 * Converts the current number of type [N] to an unsigned integer ([UInt]) if it falls within
 * the valid range for [UInt]. If the number is out of range or not convertible, an error is returned.
 *
 * This method relies on the internal `narrow` function to handle the conversion process.
 * The conversion validates whether the number can be represented as a [UInt] and, if valid,
 * performs the transformation. Otherwise, an instance of [InvalidTypeConversion] is returned
 * to indicate the failure.
 *
 * @receiver The number to be converted.
 * @return An [Either] containing the successfully converted [UInt] if the operation succeeds,
 *         or a [InvalidTypeConversion] if the number is out of the valid [UInt] range
 *         or cannot be converted.
 *
 * @throws IllegalArgumentException if the input number falls outside the acceptable range
 *         for narrowing to [UInt].
 *
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toUIntOrError() =
    narrow(0L..UInt.MAX_VALUE.toLong(), typeOf<UInt>(), Long::toUInt)

/**
 * Converts a number of type [N] to a [ULong], or returns a [InvalidTypeConversion] if the conversion is invalid.
 * The conversion ensures the number is non-negative and within the range of an unsigned long integer.
 *
 * Supported input types include:
 * - [BigInteger]: Conversion succeeds if the value is non-negative and its bit length does not exceed [ULong.SIZE_BITS].
 * - [BigDecimal]: Conversion succeeds if it can be represented as an exact integer, is non-negative, and fits within [ULong.SIZE_BITS].
 * - Other [Number] types: Conversion succeeds if it can be exactly converted to a non-negative long integer.
 *
 * @return An [Either] containing either the successfully converted [ULong] value or a [InvalidTypeConversion] if the conversion fails.
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toULongOrError(): Either<InvalidTypeConversion, ULong> =
    when (this) {
        is BigInteger -> takeIf { signum() >= 0 && bitLength() <= ULong.SIZE_BITS }?.toLong()?.toULong() // wrap intenzionale
        is BigDecimal -> try { toBigIntegerExact() } catch (e: ArithmeticException) { null }
            ?.takeIf { it.signum() >= 0 && it.bitLength() <= ULong.SIZE_BITS }?.toLong()?.toULong()
        else -> exactLongOrNull()?.takeIf { it >= 0 }?.toULong()
    }.rightIfNotNull { InvalidTypeConversion(this, typeOf<N>(), typeOf<ULong>()) }

/**
 * Attempts to convert a number of type `N` to a `Float`. If the conversion is successful
 * and the resulting `Float` value is finite, the result is wrapped in a `Right`. If the
 * conversion fails or the value is not finite, a `NumberConversionError` is wrapped in a `Left`.
 *
 * @return An `Either` containing `Right` with the finite `Float` value if the conversion is
 * successful, or `Left` with a `NumberConversionError` if the conversion fails or results in
 * a non-finite value.
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toFloatOrError(): Either<InvalidTypeConversion, Float> =
    toFloat()
        .takeIf { it.isFinite() }
        .rightIfNotNull { InvalidTypeConversion(this, typeOf<N>(), typeOf<Float>()) }

/**
 * Converts the current number instance to a `Double` or returns an error if the conversion fails.
 *
 * This function attempts to convert the current number (`N`) to a `Double` using
 * the `toDouble` function. If the resulting `Double` is finite, it wraps the value in a
 * `Right`. Otherwise, the conversion error is represented in a `Left` containing a
 * `NumberConversionError` with details about the source type, the target type, and the value.
 *
 * @return An `Either` containing `Right` with the converted `Double` value if successful,
 * or `Left` with a `NumberConversionError` if the conversion fails.
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toDoubleOrError(): Either<InvalidTypeConversion, Double> =
    toDouble()
        .takeIf { it.isFinite() }
        .rightIfNotNull { InvalidTypeConversion(this, typeOf<N>(), typeOf<Double>()) }

/**
 * Attempts to convert a number of type [N] to a [BigInteger].
 *
 * If the conversion is successful, the resulting [BigInteger] is returned wrapped in an [Either.Right].
 * Otherwise, a [InvalidTypeConversion] is returned wrapped in an [Either.Left].
 *
 * The method performs the following type checks and transformations:
 * - If the number is already a [BigInteger], it is returned directly.
 * - If the number is a [BigDecimal], it is converted to an exact [BigInteger], if possible.
 * - If the number is a [Double] or [Float], it verifies that the value is finite and represents an integer,
 *   converting it to a [BigInteger] if it satisfies these conditions.
 * - For other numeric types, it tries to convert the number to a `Long` exactly and then to a [BigInteger].
 *
 * If none of the above conversions are possible, a [InvalidTypeConversion] is created to indicate
 * the failure, specifying the original number, its class type, and the target type ([BigInteger]).
 *
 * @return An [Either] containing the resulting [BigInteger] wrapped in [Either.Right]
 * if the conversion succeeds, or a [InvalidTypeConversion] wrapped in [Either.Left] if it fails.
 *
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toBigIntOrError(): Either<InvalidTypeConversion, BigInteger> =
    when (this) {
        is BigInteger -> this
        is BigDecimal -> try { toBigIntegerExact() } catch (e: ArithmeticException) { null }
        is Double, is Float -> toDouble().takeIf { it.isFinite() && it == truncate(it) }?.let { BigDecimal(it).toBigInteger() }
        else -> exactLongOrNull()?.let(BigInteger::valueOf)
    }.rightIfNotNull { InvalidTypeConversion(this, typeOf<N>(), typeOf<BigInteger>()) }

/**
 * Converts the current number instance into a `BigDecimal`, returning the result as an `Either`.
 * If the conversion is successful, a `Right` containing the `BigDecimal` value is returned.
 * If the conversion fails, a `Left` containing a `NumberConversionError` is returned.
 *
 * The method supports the following number types:
 * - `BigDecimal`: Returned as is.
 * - `BigInteger`: Converted to `BigDecimal`.
 * - Primitive integer types (`Byte`, `Short`, `Int`, `Long`): Converted to `BigDecimal` using `toLong()`.
 * - Floating-point types (`Double`, `Float`): Converted to `BigDecimal` using `toDouble()`, only if finite.
 *
 * Any unsupported number type will result in a `Left` with a `NumberConversionError`.
 *
 * @return An `Either` where:
 * - `Right` contains the successfully converted `BigDecimal` value.
 * - `Left` contains a `NumberConversionError` if the conversion fails.
 * @since 6.1.0
 */
inline fun <reified N : Number> N.toBigDecimalOrError(): Either<InvalidTypeConversion, BigDecimal> =
    when (this) {
        is BigDecimal -> this
        is BigInteger -> BigDecimal(this)
        is Byte, is Short, is Int, is Long -> BigDecimal.valueOf(toLong())
        is Double, is Float -> toDouble().takeIf { it.isFinite() }?.let(BigDecimal::valueOf)
        else -> null
    }.rightIfNotNull { InvalidTypeConversion(this, typeOf<N>(), typeOf<BigDecimal>()) }

/**
 * Attempts to parse the string as a [Byte] and returns the result wrapped in an [Either].
 *
 * If the string cannot be parsed into a [Byte] due to a [NumberFormatException],
 * an instance of [InvalidTypeFormat] containing the original string and target type
 * will be returned in the [Either] as an error.
 *
 * @return [Either] containing the successfully parsed [Byte] or a [InvalidTypeFormat].
 * @since 6.1.0
 */
fun String.toByteOrError() = parseOrError(typeOf<Byte>(), String::toByte)
/**
 * Parses the string as a [Short] or returns an error if the parsing fails.
 *
 * This function attempts to convert the string to a [Short].
 * If the string cannot be successfully parsed, a [InvalidTypeFormat] is returned
 * encapsulating details of the failure.
 *
 * @return An [Either] containing a successfully parsed [Short] or a [InvalidTypeFormat].
 * @since 6.1.0
 */
fun String.toShortOrError() = parseOrError(typeOf<Short>(), String::toShort)
/**
 * Attempts to parse the string into an integer. Returns an `Either` result where the success case
 * contains the parsed integer value, and the failure case represents an instance of `NumberParsingError`.
 *
 * This method leverages the `parseOrError` utility, which ensures consistent error handling for cases
 * where the string cannot be converted to an integer due to invalid formatting or other issues
 * that would normally throw a `NumberFormatException`.
 *
 * @receiver The string to be parsed into an integer.
 * @return An `Either` containing either the successfully parsed integer or a `NumberParsingError`
 * in case of failure.
 * @since 6.1.0
 */
fun String.toIntOrError() = parseOrError(typeOf<Int>(), String::toInt)
/**
 * Parses the current string into a Long or returns a `NumberParsingError` if the conversion fails.
 *
 * This method attempts to convert the string representation of a number into a Long.
 * If the conversion is not possible due to an invalid format or any other parsing exception,
 * the operation results in a `NumberParsingError` to encapsulate the failure details.
 *
 * @receiver The string to be parsed into a Long.
 * @return An `Either` containing the successfully parsed Long or a `NumberParsingError` if parsing fails.
 * @since 6.1.0
 */
fun String.toLongOrError() = parseOrError(typeOf<Long>(), String::toLong)

/**
 * Converts the current string to a [UByte], or throws an exception if the conversion fails.
 *
 * This method attempts to parse the string as an unsigned byte value. If the string does not
 * represent a valid unsigned byte or is malformed, an exception is thrown.
 *
 * @return the [UByte] value represented by this string.
 * @since 6.1.0
 */
fun String.toUByteOrError() = parseOrError(typeOf<UByte>(), String::toUByte)
/**
 * Converts the string to an unsigned 16-bit integer (`UShort`) or throws an error if the conversion fails.
 *
 * The function attempts to parse the string as a `UShort` using the `String.toUShort` method. If the string
 * cannot be parsed into a valid `UShort`, an exception will be thrown indicating the failure.
 *
 * @receiver The string to be converted to a `UShort`.
 * @return The `UShort` value represented by the string.
 * @since 6.1.0
 */
fun String.toUShortOrError() = parseOrError(typeOf<UShort>(), String::toUShort)
/**
 * Converts the string to an unsigned integer (`UInt`) or throws an error if the conversion fails.
 *
 * This method attempts to parse the string as an unsigned integer.
 * If the string is not a valid representation of an unsigned integer,
 * it will throw an exception indicating the failure.
 *
 * @receiver The string to be converted to an unsigned integer.
 * @return The parsed unsigned integer representation of the string.
 * @since 6.1.0
 */
fun String.toUIntOrError() = parseOrError(typeOf<UInt>(), String::toUInt)
/**
 * Parses the string as an unsigned [ULong] number or returns a [InvalidTypeFormat] if the string is not a valid representation of an unsigned long.
 *
 * This method attempts to convert the string into an unsigned [ULong] using the [String.toULong] function. If the parsing fails
 * due to an invalid format or overflow, a [InvalidTypeFormat] encapsulating the erroneous input and target type is returned.
 *
 * @receiver The string to be parsed as an unsigned long.
 * @return An [Either] instance containing a successful [ULong] parsing result or a [InvalidTypeFormat] in case of failure.
 *
 * @since 6.1.0
 */
fun String.toULongOrError() = parseOrError(typeOf<ULong>(), String::toULong)

/**
 * Converts the string to a [Float] or returns an error encapsulated in an [Either] if the string cannot be parsed.
 *
 * This method attempts to parse the current string as a floating-point number. If the parsing fails due to an
 * invalid format, a [InvalidTypeFormat] is returned, which includes details about the failure and the target type.
 *
 * @return An [Either] containing the parsed [Float] if successful, or a [InvalidTypeFormat] if parsing fails.
 *
 * @since 6.1.0
 */
fun String.toFloatOrError() = parseOrError(typeOf<Float>(), String::toFloat)
/**
 * Attempts to parse the string as a [Double]. If parsing is successful, the result is returned
 * as a successful value. Otherwise, a [InvalidTypeFormat] is returned indicating the failure.
 *
 * This method makes use of `parseOrError` to handle the conversion and error wrapping.
 *
 * @receiver The string to be parsed into a [Double].
 * @return An [Either] containing either a successful parsed [Double] or an instance of [InvalidTypeFormat].
 * @since 6.1.0
 */
fun String.toDoubleOrError() = parseOrError(typeOf<Double>(), String::toDouble)

/**
 * Converts the current string to a [BigInt] or throws an error if the conversion fails.
 *
 * This method attempts to parse the string as a [BigInt] using the specified parser function.
 * If the string cannot be properly parsed into a [BigInt], an exception is thrown to indicate
 * the failure. Ensure that the string is in a valid numerical format for successful conversion.
 *
 * @receiver The string to be converted to a [BigInt].
 * @return The parsed [BigInt] value.
 * @since 6.1.0
 */
fun String.toBigIntOrError() = parseOrError(typeOf<BigInt>(), String::toBigInteger)
/**
 * Parses the string as a [BigDecimal] or returns an error if parsing fails.
 *
 * This method leverages a utility function to attempt parsing the string. If the string
 * cannot be parsed into a valid [BigDecimal], an instance of [InvalidTypeFormat] is returned.
 *
 * @receiver The string to be parsed.
 * @return An [Either] containing the successfully parsed [BigDecimal] or a [InvalidTypeFormat].
 * @since 6.1.0
 */
fun String.toBigDecimalOrError() = parseOrError(typeOf<BigDecimal>(), ::BigDecimal)

/**
 * Converts the string to a [BigInteger] using the specified [radix]. If the conversion fails or the radix is invalid, an error is returned.
 *
 * @param radix The base to use for the conversion. Must be between [Character.MIN_RADIX] and [Character.MAX_RADIX], inclusive.
 * @return An [Either] instance containing the parsed [BigInteger] on success, or an error object on failure.
 * @since 6.1.0
 */
fun String.toBigIntOrError(radix: Int): Either<Error, BigInteger> = either {
    ensure(radix in Character.MIN_RADIX..Character.MAX_RADIX) { NumberError.InvalidRadix(radix, Character.MIN_RADIX..Character.MAX_RADIX) }
    catching({ toBigInteger(radix) }) { _: NumberFormatException -> raise(InvalidTypeFormat(this@toBigIntOrError, typeOf<BigInt>())) }
}
/**
 * Converts the string representation of a number to a `BigInt` instance.
 * The string is expected to represent a valid numeric value.
 *
 * @return the `BigInt` representation of the numeric value contained in the string.
 * @since 6.1.0
 */
fun String.toBigInt(): BigInt = toBigInteger()
/**
 * Converts the current string to a [BigInt] using the specified numeric [radix].
 *
 * @param radix The base to interpret the string as. Must be between `2` and `36`.
 * @return The [BigInt] representation of the string in the given [radix].
 * @since 6.1.0
 */
fun String.toBigInt(radix: Int): BigInt = toBigInteger(radix)
/**
 * Attempts to parse the string as a [BigInt] number and returns the result,
 * or returns null if the string is not a valid representation of a [BigInt].
 *
 * @return A [BigInt] representation of the string, or null if parsing fails.
 * @since 6.1.0
 */
fun String.toBigIntOrNull(): BigInt? = toBigIntegerOrNull()
/**
 * Converts the string to a [BigInt] using the specified [radix], or returns `null` if the string is not a valid representation of a number in the given radix.
 *
 * @param radix The base to use for the conversion, which must be between [Character.MIN_RADIX] and [Character.MAX_RADIX].
 * @return The converted [BigInt] value, or `null` if the string is not a valid number in the specified radix.
 * @since 6.1.0
 */
fun String.toBigIntOrNull(radix: Int): BigInt? = toBigIntegerOrNull(radix)
/**
 * Converts the current integer value to a `BigInt` representation.
 *
 * @return A `BigInt` instance representing the value of the current integer.
 * @since 6.1.0
 */
fun Int.toBigInt(): BigInt = toBigInteger()
/**
 * Converts the current Long value to a BigInt instance.
 *
 * @return a BigInt representation of the current Long value.
 * @since 6.1.0
 */
fun Long.toBigInt(): BigInt = toBigInteger()
/**
 * Converts the current BigInt instance to a BigDecimal.
 *
 * @return a BigDecimal representation of the current BigInt.
 * @since 6.1.0
 */
fun BigInt.toBigDecimal(): BigDecimal = BigDecimal(this)
/**
 * Converts the given integer to a BigDecimal.
 *
 * @return a BigDecimal representation of the integer.
 * @since 6.1.0
 */
fun Int.toBigDecimal(): BigDecimal = BigDecimal(this)
/**
 * Converts the current Long value to a BigDecimal.
 *
 * @return a BigDecimal representation of the Long value.
 * @since 6.1.0
 */
fun Long.toBigDecimal(): BigDecimal = BigDecimal(this)
/**
 * Converts the current Double value to a BigDecimal.
 *
 * This method provides a precise conversion from a Double to a BigDecimal to
 * avoid potential loss of precision that can occur when working with floating-point numbers.
 *
 * @return a BigDecimal representation of the current Double value
 * @since 6.1.0
 */
fun Double.toBigDecimal(): BigDecimal = BigDecimal(this)

private fun <T : Any> Long.narrowU(
    source: Any,
    from: KType,
    target: KType,
    max: Long,
    build: (Long) -> T,
): Either<InvalidTypeConversion, T> =
    takeIf { it in 0..max }
        ?.let(build)
        .rightIfNotNull { InvalidTypeConversion(source, from, target) }

/** ULong → Long esatto, `null` se > Long.MAX_VALUE (il bit di segno indica il wrap). */
private fun ULong.exactLongOrNull(): Long? = toLong().takeIf { it >= 0 }

/**
 * Converts this [UByte] to a [Byte], ensuring the value is within the valid range for the target type.
 *
 * @receiver The [UByte] value to be converted.
 * @return An [Either] containing a [Byte] if the conversion is successful, or a [InvalidTypeConversion]
 *         if the [UByte] value cannot be represented as a [Byte].
 * @since 6.1.0
 */
fun UByte.toByteOrError() = toLong().narrowU(this, typeOf<UByte>(), typeOf<Byte>(), Byte.MAX_VALUE.toLong(), Long::toByte)

/**
 * Converts the current [UShort] value to a [Byte], or returns an error if the value cannot be represented
 * within the range of [Byte].
 *
 * The method ensures a safe and precise conversion by checking if the [UShort] value lies within the
 * valid range for a [Byte] ([0] to [127]). If the value is out of range, the conversion fails and
 * provides a [InvalidTypeConversion].
 *
 * @receiver The [UShort] value to be converted to a [Byte].
 * @return An [Either] containing the successfully converted [Byte] value or a [InvalidTypeConversion]
 * if the value is out of the valid range.
 * @since 6.1.0
 */
fun UShort.toByteOrError() = toLong().narrowU(this, typeOf<UShort>(), typeOf<Byte>(), Byte.MAX_VALUE.toLong(), Long::toByte)
/**
 * Converts an [UShort] to a [Short], returning an error if the value cannot be represented
 * within the bounds of a [Short].
 *
 * This function performs a narrowing conversion from [UShort] to [Short]. If the value
 * of the [UShort] exceeds the maximum value representable by a [Short], an error of type
 * [InvalidTypeConversion] will be generated.
 *
 * @receiver The [UShort] value to be converted.
 * @return An [Either] instance containing the successfully converted [Short] or a
 *         [InvalidTypeConversion] in case of failure.
 * @since 6.1.0
 */
fun UShort.toShortOrError() = toLong().narrowU(this, typeOf<UShort>(), typeOf<Short>(), Short.MAX_VALUE.toLong(), Long::toShort)
/**
 * Converts the receiver [UShort] value to a [UByte], or returns an error if the value exceeds [UByte.MAX_VALUE].
 *
 * This method ensures safe narrowing of a [UShort] to a [UByte] by validating
 * that the value lies within the range of [UByte]. If the value is outside the permissible
 * range, an error of type [InvalidTypeConversion] is returned.
 *
 * @return An instance of `Either<NumberConversionError, UByte>` representing the result of the conversion process.
 *         The success case contains the converted [UByte] value, while the failure case contains the corresponding error.
 * @since 6.1.0
 */
fun UShort.toUByteOrError() = toLong().narrowU(this, typeOf<UShort>(), typeOf<UByte>(), UByte.MAX_VALUE.toLong(), Long::toUByte)

/**
 * Converts the current [UInt] value to a [Byte], or returns an error if the value exceeds the range of [Byte].
 *
 * @receiver The source [UInt] value to be converted.
 * @return [Byte] representation of this [UInt] if it is within the valid range of [Byte],
 * or a [InvalidTypeConversion] wrapped in an `Either` if the conversion cannot be performed.
 * @since 6.1.0
 */
fun UInt.toByteOrError() = toLong().narrowU(this, typeOf<UInt>(), typeOf<Byte>(), Byte.MAX_VALUE.toLong(), Long::toByte)
/**
 * Converts this [UInt] to a [Short] if possible, or returns an error if the value cannot be represented
 * as a [Short] without loss of information.
 *
 * This method performs a narrowing conversion. If the [UInt] value exceeds the range of [Short],
 * an error is returned encapsulating the original value and type information.
 *
 * @receiver The [UInt] value to be converted.
 * @return The result of the conversion as a [Short] or an error wrapped in an `Either` type.
 * @since 6.1.0
 */
fun UInt.toShortOrError() = toLong().narrowU(this, typeOf<UInt>(), typeOf<Short>(), Short.MAX_VALUE.toLong(), Long::toShort)
/**
 * Converts the current `UInt` value to an `Int`.
 *
 * If the value exceeds the maximum allowable range for `Int` or
 * if the conversion is otherwise invalid, the method will return an error.
 *
 * The conversion process utilizes a narrowing function to assess
 * whether the current value can safely fit within the target type (`Int`).
 * If the value is valid, it narrows and returns the converted result.
 *
 * @receiver The `UInt` value to be converted.
 * @return The result of the conversion wrapped in an `Either`,
 *         containing the `Int` value on success or a `NumberConversionError` on failure.
 *
 * @since 6.1.0
 */
fun UInt.toIntOrError() = toLong().narrowU(this, typeOf<UInt>(), typeOf<Int>(), Int.MAX_VALUE.toLong(), Long::toInt)
/**
 * Converts this [UInt] to a [UByte] if the conversion can be performed without
 * data loss or overflow. If the [UInt] value exceeds the maximum value that can
 * be represented by a [UByte] or is invalid for conversion, the method returns
 * an error describing the issue.
 *
 * @receiver The [UInt] value to be converted.
 * @return An [Either] type that contains the resulting [UByte] if the conversion
 *         is successful, or a [InvalidTypeConversion] if the conversion fails.
 * @since 6.1.0
 */
fun UInt.toUByteOrError() = toLong().narrowU(this, typeOf<UInt>(), typeOf<UByte>(), UByte.MAX_VALUE.toLong(), Long::toUByte)
/**
 * Attempts to convert the current [UInt] value to a [UShort].
 *
 * If the current value can be represented as a [UShort] (i.e., it falls within the valid
 * [UShort] range), the conversion is performed successfully. Otherwise, an error
 * of type [InvalidTypeConversion] is returned encapsulating the source value and type.
 *
 * This method internally uses the `narrowU` utility function to perform the range validation
 * and conversion safely.
 *
 * @receiver The [UInt] value being converted.
 * @return Either a [UShort] representation of the receiver if the value is within range,
 * or a [InvalidTypeConversion] if the value exceeds the valid range.
 * @since 6.1.0
 */
fun UInt.toUShortOrError() = toLong().narrowU(this, typeOf<UInt>(), typeOf<UShort>(), UShort.MAX_VALUE.toLong(), Long::toUShort)

/**
 * Attempts to narrow the current `ULong` value to a specific type `T` based on the provided constraints.
 * If the conversion is successful within the given limits, the result is wrapped in `Either.Right`.
 * Otherwise, a `NumberConversionError` is returned in `Either.Left`.
 *
 * @param from The source class type, representing the original type of the value.
 * @param target The target class type, representing the desired narrowed type.
 * @param max The maximum permissible value for the narrowing process.
 * @param build A lambda that builds the desired type `T` from the current value if it is within limits.
 * @return An `Either` containing a successful value of type `T` in `Right` or a `NumberConversionError` in `Left`.
 * @since 6.1.0
 */
private fun <T : Any> ULong.narrowU(
    from: KType,
    target: KType,
    max: Long,
    build: (Long) -> T,
): Either<InvalidTypeConversion, T> =
    exactLongOrNull()
        ?.takeIf { it <= max }
        ?.let(build)
        .rightIfNotNull { InvalidTypeConversion(this, from, target) }

/**
 * Attempts to convert this `ULong` to a `Byte`. Returns a `Right` containing the resulting value
 * if the conversion is successful, or a `Left` containing a `NumberConversionError` if the value
 * exceeds the maximum permissible `Byte` value.
 *
 * @return An `Either` representing the result of the conversion.
 * A `Right` contains the converted `Byte` value, while a `Left` contains a `NumberConversionError`.
 * @since 6.1.0
 */
fun ULong.toByteOrError() = narrowU(typeOf<ULong>(), typeOf<Byte>(), Byte.MAX_VALUE.toLong(), Long::toByte)
/**
 * Converts the current [ULong] value to a [Short].
 *
 * If the value cannot be represented as a [Short] without overflow, an error is returned.
 * The conversion uses a boundary check to ensure the [ULong] value is within
 * the range of a [Short], which is from `0` to `Short.MAX_VALUE` inclusive.
 *
 * @return Either a [Short] representation of the current [ULong] value or a [InvalidTypeConversion]
 * indicating the value is out of range for the target type.
 * @since 6.1.0
 */
fun ULong.toShortOrError() = narrowU(typeOf<ULong>(), typeOf<Short>(), Short.MAX_VALUE.toLong(), Long::toShort)
/**
 * Attempts to convert the current `ULong` value to an `Int`. If the conversion
 * cannot be performed due to the value exceeding the maximum size allowed for an `Int`,
 * a `NumberConversionError` is returned encapsulated within an `Either` type.
 *
 * @return An `Either` containing the successfully converted `Int` value or a
 *         `NumberConversionError` if the conversion fails.
 * @since 6.1.0
 */
fun ULong.toIntOrError() = narrowU(typeOf<ULong>(), typeOf<Int>(), Int.MAX_VALUE.toLong(), Long::toInt)
/**
 * Converts the current [ULong] value to a [Long], or returns an error if the conversion is not possible.
 *
 * This method leverages `narrowU` to check the validity of the conversion
 * and ensures the [Long.MAX_VALUE] boundary is respected. If the value
 * exceeds the maximum allowable value for [Long], an appropriate
 * [InvalidTypeConversion] is returned encapsulated in an `Either`.
 *
 * @receiver The [ULong] value to be converted to [Long].
 * @return An `Either` containing the converted [Long] value if successful,
 *         or a [InvalidTypeConversion] if the conversion fails.
 * @since 6.1.0
 */
fun ULong.toLongOrError() = narrowU(typeOf<ULong>(), typeOf<Long>(), Long.MAX_VALUE) { it }
/**
 * Attempts to convert the current [ULong] value to a [UByte].
 *
 * If the conversion succeeds, the result is returned as a [UByte].
 * If the current value exceeds the maximum value of [UByte], a [InvalidTypeConversion] is returned.
 *
 * @return Either a converted [UByte] or a [InvalidTypeConversion] if the conversion fails.
 * @since 6.1.0
 */
fun ULong.toUByteOrError() = narrowU(typeOf<ULong>(), typeOf<UByte>(), UByte.MAX_VALUE.toLong(), Long::toUByte)
/**
 * Attempts to convert the current [ULong] value to a [UShort] value.
 *
 * If the current value exceeds the maximum value representable by [UShort],
 * an error is returned encapsulated in an [Either] type, where the error indicates
 * the reason for the failed conversion.
 *
 * This method ensures that the conversion is safe and avoids silent overflows.
 *
 * @return An [Either] containing the successfully converted [UShort] value or a
 * [InvalidTypeConversion] if the conversion fails.
 * @since 6.1.0
 */
fun ULong.toUShortOrError() = narrowU(typeOf<ULong>(), typeOf<UShort>(), UShort.MAX_VALUE.toLong(), Long::toUShort)
/**
 * Converts the current [ULong] value to a [UInt].
 *
 * If the current [ULong] value exceeds the maximum representable value of [UInt],
 * an exception is thrown. This ensures that the conversion operates only when
 * the source value can safely fit within the bounds of a [UInt].
 *
 * @return The [UInt] representation of the current [ULong] value.
 * @throws IllegalArgumentException if the value exceeds [UInt.MAX_VALUE].
 * @since 6.1.0
 */
fun ULong.toUIntOrError() = narrowU(typeOf<ULong>(), typeOf<UInt>(), UInt.MAX_VALUE.toLong(), Long::toUInt)

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
fun CharSequence.parseNumberWords() = either {
    catching({ NumberWords.parse(this@parseNumberWords.toString()) }) { _: Exception ->
        InvalidTypeFormat(this@parseNumberWords, typeOf<BigDecimal>())
    }
}

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
 * Restricts a number to be within a specified closed range.
 *
 * @param range The closed range used for bounding the number.
 * @return The number after being coerced within the specified closed range.
 *         If the number is less than the start of the range, the start value is returned.
 *         If the number is greater than the end of the range, the end value is returned.
 *         Otherwise, the number itself is returned.
 * @since 5.5.0
 */
infix fun <N> N.coercedIn(range: ClosedRange<N>) : N where N : Number, N : Comparable<N> {
    if (this < range.start) return range.start
    if (this > range.endInclusive) return range.endInclusive
    return this
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
 * @receiver An iterables collection of pairs containing a number and a percentage weight.
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
 * @receiver An iterables collection of `Number2`, where each item consists of a number and its respective weight.
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
 * Executes the given action if the number is positive.
 *
 * @param action The action to be executed if the number is positive.
 * @return The original number.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifPositive(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isPositive) action(this)
    return this
}
/**
 * Executes the given action if the number is negative.
 *
 * @param action The action to be performed if the number is negative.
 * @return The original number.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifNegative(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNegative) action(this)
    return this
}
/**
 * Executes the given action if the number is not positive.
 *
 * @param action The action to perform if the number is not positive.
 * @return The original number.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifNotPositive(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNotPositive) action(this)
    return this
}
/**
 * Executes the given action if the number is not negative.
 *
 * @param action The action to be executed if the number is not negative.
 * @return The original number.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifNotNegative(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNotNegative) action(this)
    return this
}
/**
 * Executes the given action if the number is zero.
 *
 * @param action the action to be executed if the number is zero
 * @return the original number
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifZero(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isZero) action(this)
    return this
}
/**
 * Executes the given action if the number is not zero.
 *
 * @param action The action to perform if the number is not zero.
 * @return The original number.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifNotZero(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNotZero) action(this)
    return this
}
/**
 * Performs the specified action if the current number is equal to 1.
 *
 * @param action A function to be executed if the number is 1.
 * @return The original number.
 * @since 5.4.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifOne(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isOne) action(this)
    return this
}
/**
 * Executes the given action if the number is not equal to 1.
 *
 * @param action The action to be executed if the number is not 1.
 * @return The original number on which the method was called.
 * @since 5.4.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifNotOne(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNotOne) action(this)
    return this
}
/**
 * Performs the specified action if the current number is equal to -1.
 *
 * @param action A function to be executed if the number is -1.
 * @return The original number.
 * @since 5.4.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifMinusOne(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isMinusOne) action(this)
    return this
}
/**
 * Executes the given action if the number is not equal to -1.
 *
 * @param action The action to be executed if the number is not -1.
 * @return The original number on which the method was called.
 * @since 5.4.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifNotMinusOne(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNotMinusOne) action(this)
    return this
}
/**
 * Executes the provided action if the number is even.
 *
 * @param action A consumer function to be executed if the number is even.
 * @return The original number.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifEven(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isEven) action(this)
    return this
}
/**
 * Executes the provided action if the number is odd.
 *
 * @param action A lambda that will be invoked with the number as its argument if the number is odd.
 * @return The original number.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifOdd(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isOdd) action(this)
    return this
}
/**
 * Executes the provided action if the number is a decimal (i.e., has a fractional part).
 *
 * @param action A lambda function that will be invoked with this number as a parameter
 *               if it is a decimal.
 * @return The original number.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifDecimal(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isDecimal) action(this)
    return this
}
/**
 * Executes the specified [action] if the number is not a decimal (i.e., it does not have a fractional component).
 *
 * @param action The lambda function to be executed if the condition is met.
 * @return The original number.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifNotDecimal(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNotDecimal) action(this)
    return this
}
/**
 * Executes the given action if the number is based on a decimal class.
 *
 * The function takes a generic type parameter constrained to `Number`.
 * If the invoking number meets the decimal class-based condition,
 * the specified action is performed on it.
 *
 * @param action A single action to be executed if the number is based on a decimal class.
 * @return The original number instance.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifDecimalClassBased(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isDecimalClassBased) action(this)
    return this
}
/**
 * Executes the given action if the number is not of a decimal class type.
 *
 * @param action A function to be invoked if the number is not decimal class-based.
 * @return The original number on which the operation was invoked.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun <T : Number> T.ifNotDecimalClassBased(action: Consumer<T>): T {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNotDecimalClassBased) action(this)
    return this
}

/**
 * Executes the given action if the integer is within the specified range.
 *
 * @param range The range to check the integer against.
 * @param action The action to execute if the condition is met.
 * @return The same integer on which the function was called.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Int.ifIn(range: IntProgression, action: Consumer<Int>): Int {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if this integer is within the specified range.
 *
 * @param range the range to check if this integer is within.
 * @param action the action to execute if the condition is met.
 * @return this integer, regardless of whether the action was executed or not.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Int.ifIn(range: IntRange, action: Consumer<Int>): Int {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if the integer is within the specified range, considering any exclusions.
 *
 * @param range Defines the range with possible exclusions to check against.
 * @param action A function to be executed if the integer is within the range.
 * @return The original integer.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Int.ifIn(range: IntRangeWithExclusions, action: Consumer<Int>): Int {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes a given action if the integer is within the specified range.
 *
 * @param range the range of integers with conditions to evaluate against
 * @param action the action to perform on the integer if it is within the range
 * @return the original integer
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Int.ifIn(range: IntRangeWithConditions, action: Consumer<Int>): Int {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if the unsigned integer is within the specified range.
 *
 * @param range The progression of unsigned integers to check against.
 * @param action The action to be executed if the value is within the range.
 * @return The original unsigned integer.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun UInt.ifIn(range: UIntProgression, action: Consumer<UInt>): UInt {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if the current unsigned integer lies within the specified range.
 *
 * @param range The range of unsigned integers to check against.
 * @param action The action to be executed if the current unsigned integer is within the range.
 * @return The current unsigned integer.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun UInt.ifIn(range: UIntRange, action: Consumer<UInt>): UInt {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if the unsigned integer is within the specified range,
 * and then returns the original value.
 *
 * @param range The range, including any exclusions, to check if the unsigned integer belongs to.
 * @param action The action to be performed if this unsigned integer is within the specified range.
 * @return The original unsigned integer value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun UInt.ifIn(range: UIntRangeWithExclusions, action: Consumer<UInt>): UInt {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if the current `UInt` is within the specified range.
 *
 * @param range the range of `UInt` values, potentially with additional conditions, to check against.
 * @param action the action to perform if the current `UInt` is within the specified range.
 * @return the original `UInt` value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun UInt.ifIn(range: UIntRangeWithConditions, action: Consumer<UInt>): UInt {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Evaluates the given action if the current Long value is within the specified range.
 *
 * @param range The range of Long values to check against.
 * @param action The action to perform if the Long value is within the specified range.
 * @return The original Long value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Long.ifIn(range: LongProgression, action: Consumer<Long>): Long {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if the current value is within the specified range.
 *
 * @param range the range to check if the current value is within.
 * @param action the action to perform if the current value is within the range.
 * @return the current value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Long.ifIn(range: LongRange, action: Consumer<Long>): Long {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if the current Long value is within the specified range,
 * while respecting exclusions within the range.
 *
 * @param range The range, potentially with exclusions, to check the Long value against.
 * @param action The action to be executed if the Long value is within the specified range.
 * @return Returns the original Long value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Long.ifIn(range: LongRangeWithExclusions, action: Consumer<Long>): Long {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if the current Long value is within the specified range.
 *
 * @param range The range with conditions to check against the current Long value.
 * @param action The consumer action to be performed if the current value is within the range.
 * @return The original Long value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun Long.ifIn(range: LongRangeWithConditions, action: Consumer<Long>): Long {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if the current `ULong` value is within the specified range.
 *
 * @param range the range in which the check is performed.
 * @param action a function to be executed if the value is within the range.
 * @return the original `ULong` value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun ULong.ifIn(range: ULongProgression, action: Consumer<ULong>): ULong {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if the current unsigned long value is within the specified range.
 *
 * @param range The range to check if the current value is within.
 * @param action The action to execute if the current value is within the range.
 * @return The current value of the unsigned long, regardless of whether the action was executed or not.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun ULong.ifIn(range: ULongRange, action: Consumer<ULong>): ULong {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the provided action if the current `ULong` value is within the specified range
 * while considering exclusions.
 *
 * @param range the range with exclusions to check against
 * @param action the action to invoke if the value is in the range
 * @return the current `ULong` value
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun ULong.ifIn(range: ULongRangeWithExclusions, action: Consumer<ULong>): ULong {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
    return this
}
/**
 * Executes the given action if the ULong value is within the specified range.
 *
 * @param range The range with conditions to check the value against.
 * @param action The action to execute if the value is within the range.
 * @return The original ULong value.
 * @since 5.0.0
 */
@IgnorableReturnValue
inline fun ULong.ifIn(range: ULongRangeWithConditions, action: Consumer<ULong>): ULong {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this in range) action(this)
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

    /**
     * Converte un numero decimale in parole, leggendo le cifre dopo il punto
     * una per una (es. 3.14 -> "three point one four").
     */
    fun toWords(value: BigDecimal): String {
        val negative = value.signum() < 0
        val abs = value.abs()

        // separa parte intera e parte decimale come stringa di cifre "grezze"
        val plain = abs.stripTrailingZeros().toPlainString()
        val dotIdx = plain.indexOf('.')

        val integerPart: Long
        val fractionalDigits: String
        if (dotIdx == -1) {
            integerPart = plain.toLong()
            fractionalDigits = ""
        } else {
            integerPart = plain.substring(0, dotIdx).toLong()
            fractionalDigits = plain.substring(dotIdx + 1)
        }

        val integerWords = toWords(integerPart)

        val result = if (fractionalDigits.isEmpty()) {
            integerWords
        } else {
            val digitWords = fractionalDigits.map { UNITS[it - '0'] }.joinToString(" ")
            "$integerWords point $digitWords"
        }

        return if (negative) "negative $result" else result
    }

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

    fun parse(text: String): BigDecimal {
        val tokens = text.lowercase()
            .replace("-", " ")
            .replace(",", " ")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() && it != "and" }

        if (tokens.isEmpty()) throw MalformedInputException("Empty input")

        var negative = false
        var idx = 0
        if (tokens[0] == "negative" || tokens[0] == "minus") {
            negative = true; idx = 1
        }

        val pointIdx = tokens.indexOf("point").let { if (it >= idx) it else -1 }

        val integerTokens = if (pointIdx == -1) tokens.subList(idx, tokens.size) else tokens.subList(idx, pointIdx)
        val fractionalTokens = if (pointIdx == -1) emptyList() else tokens.subList(pointIdx + 1, tokens.size)

        val integerValue = parseIntegerTokens(integerTokens)

        val fractionalDigits = buildString {
            for (token in fractionalTokens) {
                val digit = WORD_VALUES[token]
                validateInputFormat(digit != null && digit in 0..9) { "Unknown digit after 'point': '$token'" }
                append(digit)
            }
        }

        var result = BigDecimal(integerValue)
        if (fractionalDigits.isNotEmpty()) {
            result = result.add(BigDecimal("0.$fractionalDigits"))
        }

        return if (negative) result.negate() else result
    }

    private fun parseIntegerTokens(tokens: List<String>): Long {
        if (tokens.isEmpty()) return 0L

        var total = 0L      // accumulated full result
        var current = 0L    // current group being built (< 1000 before scale applied)

        for (token in tokens) {
            when {
                WORD_VALUES.containsKey(token) -> current += WORD_VALUES[token]!!
                token == "hundred" -> current *= 100
                SCALE_VALUES.containsKey(token) -> {
                    val scale = SCALE_VALUES[token]!!
                    total += current * scale
                    current = 0
                }
                else -> throw MalformedInputException("Unknown token: '$token'")
            }
        }
        return total + current
    }
}