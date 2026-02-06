package dev.tommasop1804.kutils.classes.base

import dev.tommasop1804.kutils.BigInt
import dev.tommasop1804.kutils.classes.base.Base62.BASE_62_CHARACTERS
import java.util.stream.IntStream
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.ln

/**
 * A utility object for Base62 encoding and decoding. Base62 is a text-based encoding mechanism
 * that encodes binary data into an alphanumeric string consisting of 62 characters.
 *
 * @since 1.0.0
 */
@Suppress("unused")
internal object Base62 {
    /**
     * A constant array of characters representing the Base62 numeral system.
     * Base62 includes digits (0-9), uppercase alphabet (A-Z), and lowercase alphabet (a-z),
     * providing 62 unique symbols for encoding purposes.
     *
     * This constant is typically used in encoding or decoding operations that require
     * Base62 numeral representation, such as encoding binary data into a Base62 string.
     *
     * @since 1.0.0
     */
    val BASE_62_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray()
    /**
     * Represents the base value used in Base62 encoding and decoding.
     *
     * This constant is calculated as the size of the character set used
     * for Base62 operations, and it determines the numeric base for
     * conversions between byte arrays and Base62 formatted strings.
     *
     * @since 1.0.0
     */
    val BASE: BigInt = BigInt.valueOf(BASE_62_CHARACTERS.size.toLong())
    /**
     * Represents the number of bits in a single byte.
     *
     * This constant is commonly used in operations where the bit-length of a byte
     * is required, such as encoding, decoding, or bitwise computations.
     *
     * @since 1.0.0
     */
    const val BYTE_BITS = 8
    /**
     * Represents the number of bits required to represent a single Base62 digit.
     * This is calculated as the logarithm (base 2) of the number of Base62 characters.
     *
     * This constant is used in encoding operations, such as converting data
     * to a Base62 encoded string representation, ensuring the calculations
     * align with the Base62 character set.
     *
     * @see BASE_62_CHARACTERS
     * @since 1.0.0
     */
    val DIGIT_BITS = ln(BASE_62_CHARACTERS.size.toDouble()) / ln(2.0)

    /**
     * Encodes the given byte array into a Base62-encoded string.
     *
     * @param bytes The byte array to encode.
     * @param length Optional parameter specifying the minimum length of the resulting string.
     * Padding with '0' characters will be added if the encoded result is shorter than this length.
     * Defaults to 0 for no padding.
     * @return A Base62-encoded string representation of the input byte array.
     * @since 1.0.0
     */
    fun encode(bytes: ByteArray, length: Int = 0): String {
        val size = ceil((bytes.size * BYTE_BITS) / DIGIT_BITS).toInt()
        val sb = StringBuilder(size)
        val tempBuffer = ByteArray(bytes.size + 1)
        System.arraycopy(bytes, 0, tempBuffer, 1, bytes.size)

        var value = BigInt(tempBuffer)
        while (value > BigInt.ZERO) {
            val quotientAndRemainder = value.divideAndRemainder(BASE)
            sb.append(BASE_62_CHARACTERS[abs(quotientAndRemainder[1].toInt())])
            value = quotientAndRemainder[0]
        }

        while (length > 0 && sb.length < length)
            sb.append('0')

        return sb.reverse().toString()
    }

    /**
     * Decodes a Base62-encoded string into a byte array.
     *
     * The decoding process involves converting each character of the input string into its corresponding index
     * in the Base62 encoding system, and then combining the results into a byte array representation.
     *
     * @param base62 The Base62-encoded string to be decoded.
     * @return A byte array obtained from decoding the provided Base62-encoded string.
     * @throws IllegalArgumentException If the input string contains characters that are not valid in Base62 encoding.
     * @since 1.0.0
     */
    fun decode(base62: String): ByteArray = IntStream.range(0, base62.length)
        .mapToObj(base62::get)
        .map(Base62::indexOf)
        .map(BigInt::valueOf)
        .reduce(BigInt.ZERO) { result, index -> result.multiply(BASE).add(index) }
        .toByteArray()

    /**
     * Determines the index of a given character in a Base62 encoding scheme.
     *
     * @param c the character to find the index of. Must be a valid Base62 character: '0'-'9', 'A'-'Z', or 'a'-'z'.
     * @return the zero-based index of the given character in the Base62 character set.
     * @throws IllegalArgumentException if the provided character is not a valid Base62 character.
     * @since 1.0.0
     */
    fun indexOf(c: Char): Long {
        if (c in '0'..'9') return (c.code - 48).toLong()
        if (c in 'A'..'Z') return (c.code - 55).toLong()
        if (c in 'a'..'z') return (c.code - 61).toLong()
        throw IllegalArgumentException("'$c' is not a valid Base62 character")
    }
}