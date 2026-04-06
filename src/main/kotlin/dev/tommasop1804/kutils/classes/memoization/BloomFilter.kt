package dev.tommasop1804.kutils.classes.memoization

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.numbers.*
import dev.tommasop1804.kutils.classes.numbers.Percentage.Companion.percent
import java.io.Serializable
import java.util.concurrent.atomic.AtomicLongArray
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.roundToLong

/**
 * A BloomFilter is a probabilistic data structure designed to test set membership efficiently.
 * It provides fast insert and membership query operations, with a configurable false positive rate.
 *
 * This implementation is parameterized to work with a fixed number of expected insertions and a
 * certain false positive rate. It uses multiple hash functions to achieve probabilistic guarantees
 * about the presence or absence of an element.
 *
 * @param T The type of elements this BloomFilter can hold.
 *
 * Properties:
 * @property expectedInsertions The maximum number of elements the Bloom filter is designed to hold.
 *                              This value impacts the number of hash functions and bit array size.
 * @property falsePositiveRate The probability of a false positive match. This value determines the level
 *                             of accuracy and space efficiency of the Bloom filter.
 * @property numBits The total number of bits in the underlying bit array used by the Bloom filter.
 *                   Computed based on `expectedInsertions` and `falsePositiveRate`.
 * @property numHashFunctions The number of hash functions used to map an element to bits in the bit array.
 *                            This value is derived from the false positive rate and bit array size.
 * @property bits The underlying bit array used by the Bloom filter to store element hashes.
 * @property count The number of elements that have been added to the Bloom filter.
 * @property size The total capacity of the bit array (equivalent to `numBits`).
 * @property fillRatio The proportion of bits in the filter that are currently set.
 *                     This can give an indication of filter saturation.
 * @since 3.5.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
class BloomFilter<T> private constructor(
    val expectedInsertions: Long,
    val falsePositiveRate: Percentage,
    val numBits: Long,
    val numHashFunctions: Int,
    private val bits: AtomicLongArray
) : Serializable {

    /**
     * Tracks the current number of elements that have been added to the Bloom filter.
     * This value is incremented as new elements are added.
     * It is marked as `@Volatile` to ensure thread-safety for concurrent access.
     * @since 3.5.0
     */
    @Volatile
    private var count: Long = 0L

    /**
     * Represents the size of the collection, typically equivalent to the number of elements contained within it.
     * The value is derived from the `count` property and provides a read-only view of the size.
     * @since 3.5.0
     */
    val size get() = count

    /**
     * Represents the ratio of set bits to the total number of bits in the Bloom filter.
     * This value is expressed as a percentage between 0 and 100, inclusive of both bounds.
     * It provides an indication of the current utilization of the bit array in the Bloom filter.
     *
     * A higher fill ratio may lead to an increased likelihood of false positives during element checks.
     * @since 3.5.0
     */
    val fillRatio: Percentage get() {
        var set = 0L
        for (i in 0 until bits.length())
            set += java.lang.Long.bitCount(bits.get(i))
        return Percentage(set.toDouble() / numBits, from0to1 = true)
    }

    /**
     * Secondary constructor for the BloomFilter class.
     *
     * This constructor initializes the BloomFilter with the expected number of insertions
     * and a false positive rate. It delegates to the primary constructor after determining
     * the required internal parameters based on the input values.
     *
     * @param expectedInsertions The estimated number of elements to be inserted into the filter.
     * @param falsePositiveRate The desired probability of false positives, represented as a percentage.
     * @since 3.5.0
     */
    constructor(expectedInsertions: Long, falsePositiveRate: Percentage = 1.percent) : this(
        create(expectedInsertions, falsePositiveRate)
    )
    /**
     * Constructs a BloomFilter instance with the specified expected number of insertions and a defined false positive rate.
     *
     * @param expectedInsertions The anticipated number of unique elements that will be inserted into the Bloom filter.
     * @param falsePositiveRate The acceptable false positive rate for the filter, with a default of 1%.
     * @since 3.5.0
     */
    constructor(expectedInsertions: Int, falsePositiveRate: Percentage = 1.percent) : this(
        create(expectedInsertions, falsePositiveRate)
    )

    /**
     * Private secondary constructor for creating a new BloomFilter instance
     * based on the properties of the provided BloomFilter object.
     *
     * @param filter The BloomFilter instance whose properties will be used
     *               to initialize the new BloomFilter.
     * @since 3.5.0
     */
    private constructor(filter: BloomFilter<T>) : this(
        filter.expectedInsertions,
        filter.falsePositiveRate,
        filter.numBits,
        filter.numHashFunctions,
        filter.bits
    )

    companion object {
        /**
         * Creates a BloomFilter with the specified expected number of insertions and false positive rate.
         *
         * @param T The type of elements to be stored in the BloomFilter.
         * @param expectedInsertions The number of expected elements to be inserted into the BloomFilter.
         * This value must be positive.
         * @param falsePositiveRate The desired false positive rate for the BloomFilter, expressed as a Percentage.
         * This value must be positive and less than 100.
         * @return A BloomFilter instance configured for the given parameters.
         * @since 3.5.0
         */
        private fun <T> create(expectedInsertions: Long, falsePositiveRate: Percentage): BloomFilter<T> {
            expectedInsertions.validatePositive(lazyMessage = { "Expected insertions must be positive" })
            falsePositiveRate.value.validatePositive(lazyMessage = { "False positive rate must be positive" })
            falsePositiveRate.value.validate(lazyMessage = { "False positive rate must be under 100" }) { it < 100 }

            val m = optimalNumBits(expectedInsertions, falsePositiveRate)
            val k = optimalNumHashFunctions(expectedInsertions, m)
            val words = ((m + 63) / 64).toInt()
            val actualBits = words.toLong() * 64
            return BloomFilter(
                expectedInsertions,
                falsePositiveRate,
                actualBits,
                k,
                AtomicLongArray(words)
            )
        }
        /**
         * Creates a new instance of a Bloom filter with the specified expected number of insertions
         * and desired false positive rate.
         *
         * @param T The type of elements to be stored in the Bloom filter.
         * @param expectedInsertions The expected number of elements to be inserted into the Bloom filter.
         * @param falsePositiveRate The acceptable false positive rate for the Bloom filter.
         * @since 3.5.0
         */
        private fun <T> create(expectedInsertions: Int, falsePositiveRate: Percentage) =
            create<T>(expectedInsertions.toLong(), falsePositiveRate)

        /**
         * Calculates the optimal number of bits required for the Bloom filter based on the expected
         * number of insertions and the desired false positive probability.
         *
         * @param n The expected number of elements to be inserted into the Bloom filter.
         * @param p The desired false positive probability, represented as a percentage.
         * @return The calculated number of bits, ensuring a minimum value of 64.
         * @since 3.5.0
         */
        private fun optimalNumBits(n: Long, p: Percentage) =
            (-n.toDouble() * ln(p.toDouble(true)) / (ln(2.0) * ln(2.0))).roundToLong().coerceAtLeast(64)

        /**
         * Calculates the optimal number of hash functions for a Bloom filter based on the number
         * of expected insertions and the size of the bit array.
         *
         * @param n The expected number of elements to be inserted into the Bloom filter.
         * @param m The size of the bit array used by the Bloom filter.
         * @return The optimal number of hash functions constrained to be between 1 and 30.
         * @since 3.5.0
         */
        private fun optimalNumHashFunctions(n: Long, m: Long) =
            ceil(m.toDouble() / n * ln(2.0)).toInt().coerceIn(1, 30)

        /**
         * Computes the 128-bit MurmurHash3 hash for the given input data using the x86 variant.
         *
         * The MurmurHash3 algorithm is a non-cryptographic hash function suitable for general
         * hash-based lookup and is optimized for speed and quality of the hash distribution.
         *
         * @param data The input byte array for which the hash is computed.
         * @param seed The initial hash seed value, which can be used to vary the hash output.
         * @return A LongArray of size 2, containing the computed 128-bit hash where the first
         *         element corresponds to the lower 64 bits, and the second element corresponds
         *         to the upper 64 bits.
         * @since 3.5.0
         */
        @Suppress("FunctionName", "java_integer_as_kotlin_int", "SameParameterValue")
        private fun murmurHash3_x86_128(data: ByteArray, seed: Int): LongArray {
            val len = data.size
            val nblocks = len / 16

            var h1 = seed
            var h2 = seed
            var h3 = seed
            var h4 = seed

            val c1 = 0x239b961b
            val c2 = 0xab0e9789.toInt()
            val c3 = 0x38b34ae5
            val c4 = 0xa1e38b93.toInt()

            // body
            for (i in 0 until nblocks) {
                val off = i * 16
                var k1 = getBlock32(data, off)
                var k2 = getBlock32(data, off + 4)
                var k3 = getBlock32(data, off + 8)
                var k4 = getBlock32(data, off + 12)

                k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1
                h1 = Integer.rotateLeft(h1, 19); h1 += h2; h1 = h1 * 5 + 0x561ccd1b

                k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2
                h2 = Integer.rotateLeft(h2, 17); h2 += h3; h2 = h2 * 5 + 0x0bcaa747

                k3 *= c3; k3 = Integer.rotateLeft(k3, 17); k3 *= c4; h3 = h3 xor k3
                h3 = Integer.rotateLeft(h3, 15); h3 += h4; h3 = h3 * 5 + 0x96cd1c35.toInt()

                k4 *= c4; k4 = Integer.rotateLeft(k4, 18); k4 *= c1; h4 = h4 xor k4
                h4 = Integer.rotateLeft(h4, 13); h4 += h1; h4 = h4 * 5 + 0x32ac3b17
            }

            // tail
            val tail = nblocks * 16
            var k1 = 0; var k2 = 0; var k3 = 0; var k4 = 0

            @Suppress("KotlinConstantConditions")
            when (len and 15) {
                15 -> { k4 = k4 xor ((data[tail + 14].toInt() and 0xFF) shl 16); k4 = k4 xor ((data[tail + 13].toInt() and 0xFF) shl 8); k4 = k4 xor (data[tail + 12].toInt() and 0xFF); k4 *= c4; k4 = Integer.rotateLeft(k4, 18); k4 *= c1; h4 = h4 xor k4; k3 = k3 xor ((data[tail + 11].toInt() and 0xFF) shl 24); k3 = k3 xor ((data[tail + 10].toInt() and 0xFF) shl 16); k3 = k3 xor ((data[tail + 9].toInt() and 0xFF) shl 8); k3 = k3 xor (data[tail + 8].toInt() and 0xFF); k3 *= c3; k3 = Integer.rotateLeft(k3, 17); k3 *= c4; h3 = h3 xor k3; k2 = k2 xor ((data[tail + 7].toInt() and 0xFF) shl 24); k2 = k2 xor ((data[tail + 6].toInt() and 0xFF) shl 16); k2 = k2 xor ((data[tail + 5].toInt() and 0xFF) shl 8); k2 = k2 xor (data[tail + 4].toInt() and 0xFF); k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2; k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                14 -> { k4 = k4 xor ((data[tail + 13].toInt() and 0xFF) shl 8); k4 = k4 xor (data[tail + 12].toInt() and 0xFF); k4 *= c4; k4 = Integer.rotateLeft(k4, 18); k4 *= c1; h4 = h4 xor k4; k3 = k3 xor ((data[tail + 11].toInt() and 0xFF) shl 24); k3 = k3 xor ((data[tail + 10].toInt() and 0xFF) shl 16); k3 = k3 xor ((data[tail + 9].toInt() and 0xFF) shl 8); k3 = k3 xor (data[tail + 8].toInt() and 0xFF); k3 *= c3; k3 = Integer.rotateLeft(k3, 17); k3 *= c4; h3 = h3 xor k3; k2 = k2 xor ((data[tail + 7].toInt() and 0xFF) shl 24); k2 = k2 xor ((data[tail + 6].toInt() and 0xFF) shl 16); k2 = k2 xor ((data[tail + 5].toInt() and 0xFF) shl 8); k2 = k2 xor (data[tail + 4].toInt() and 0xFF); k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2; k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                13 -> { k4 = k4 xor (data[tail + 12].toInt() and 0xFF); k4 *= c4; k4 = Integer.rotateLeft(k4, 18); k4 *= c1; h4 = h4 xor k4; k3 = k3 xor ((data[tail + 11].toInt() and 0xFF) shl 24); k3 = k3 xor ((data[tail + 10].toInt() and 0xFF) shl 16); k3 = k3 xor ((data[tail + 9].toInt() and 0xFF) shl 8); k3 = k3 xor (data[tail + 8].toInt() and 0xFF); k3 *= c3; k3 = Integer.rotateLeft(k3, 17); k3 *= c4; h3 = h3 xor k3; k2 = k2 xor ((data[tail + 7].toInt() and 0xFF) shl 24); k2 = k2 xor ((data[tail + 6].toInt() and 0xFF) shl 16); k2 = k2 xor ((data[tail + 5].toInt() and 0xFF) shl 8); k2 = k2 xor (data[tail + 4].toInt() and 0xFF); k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2; k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                12 -> { k3 = k3 xor ((data[tail + 11].toInt() and 0xFF) shl 24); k3 = k3 xor ((data[tail + 10].toInt() and 0xFF) shl 16); k3 = k3 xor ((data[tail + 9].toInt() and 0xFF) shl 8); k3 = k3 xor (data[tail + 8].toInt() and 0xFF); k3 *= c3; k3 = Integer.rotateLeft(k3, 17); k3 *= c4; h3 = h3 xor k3; k2 = k2 xor ((data[tail + 7].toInt() and 0xFF) shl 24); k2 = k2 xor ((data[tail + 6].toInt() and 0xFF) shl 16); k2 = k2 xor ((data[tail + 5].toInt() and 0xFF) shl 8); k2 = k2 xor (data[tail + 4].toInt() and 0xFF); k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2; k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                11 -> { k3 = k3 xor ((data[tail + 10].toInt() and 0xFF) shl 16); k3 = k3 xor ((data[tail + 9].toInt() and 0xFF) shl 8); k3 = k3 xor (data[tail + 8].toInt() and 0xFF); k3 *= c3; k3 = Integer.rotateLeft(k3, 17); k3 *= c4; h3 = h3 xor k3; k2 = k2 xor ((data[tail + 7].toInt() and 0xFF) shl 24); k2 = k2 xor ((data[tail + 6].toInt() and 0xFF) shl 16); k2 = k2 xor ((data[tail + 5].toInt() and 0xFF) shl 8); k2 = k2 xor (data[tail + 4].toInt() and 0xFF); k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2; k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                10 -> { k3 = k3 xor ((data[tail + 9].toInt() and 0xFF) shl 8); k3 = k3 xor (data[tail + 8].toInt() and 0xFF); k3 *= c3; k3 = Integer.rotateLeft(k3, 17); k3 *= c4; h3 = h3 xor k3; k2 = k2 xor ((data[tail + 7].toInt() and 0xFF) shl 24); k2 = k2 xor ((data[tail + 6].toInt() and 0xFF) shl 16); k2 = k2 xor ((data[tail + 5].toInt() and 0xFF) shl 8); k2 = k2 xor (data[tail + 4].toInt() and 0xFF); k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2; k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                9  -> { k3 = k3 xor (data[tail + 8].toInt() and 0xFF); k3 *= c3; k3 = Integer.rotateLeft(k3, 17); k3 *= c4; h3 = h3 xor k3; k2 = k2 xor ((data[tail + 7].toInt() and 0xFF) shl 24); k2 = k2 xor ((data[tail + 6].toInt() and 0xFF) shl 16); k2 = k2 xor ((data[tail + 5].toInt() and 0xFF) shl 8); k2 = k2 xor (data[tail + 4].toInt() and 0xFF); k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2; k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                8  -> { k2 = k2 xor ((data[tail + 7].toInt() and 0xFF) shl 24); k2 = k2 xor ((data[tail + 6].toInt() and 0xFF) shl 16); k2 = k2 xor ((data[tail + 5].toInt() and 0xFF) shl 8); k2 = k2 xor (data[tail + 4].toInt() and 0xFF); k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2; k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                7  -> { k2 = k2 xor ((data[tail + 6].toInt() and 0xFF) shl 16); k2 = k2 xor ((data[tail + 5].toInt() and 0xFF) shl 8); k2 = k2 xor (data[tail + 4].toInt() and 0xFF); k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2; k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                6  -> { k2 = k2 xor ((data[tail + 5].toInt() and 0xFF) shl 8); k2 = k2 xor (data[tail + 4].toInt() and 0xFF); k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2; k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                5  -> { k2 = k2 xor (data[tail + 4].toInt() and 0xFF); k2 *= c2; k2 = Integer.rotateLeft(k2, 16); k2 *= c3; h2 = h2 xor k2; k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                4  -> { k1 = k1 xor ((data[tail + 3].toInt() and 0xFF) shl 24); k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                3  -> { k1 = k1 xor ((data[tail + 2].toInt() and 0xFF) shl 16); k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                2  -> { k1 = k1 xor ((data[tail + 1].toInt() and 0xFF) shl 8); k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
                1  -> { k1 = k1 xor (data[tail].toInt() and 0xFF); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 = h1 xor k1 }
            }

            // finalization
            h1 = h1 xor len; h2 = h2 xor len; h3 = h3 xor len; h4 = h4 xor len

            h1 += h2; h1 += h3; h1 += h4
            h2 += h1; h3 += h1; h4 += h1

            h1 = fmix32(h1); h2 = fmix32(h2); h3 = fmix32(h3); h4 = fmix32(h4)

            h1 += h2; h1 += h3; h1 += h4
            h2 += h1; h3 += h1; h4 += h1

            // pack into two 64-bit values
            return longArrayOf(
                (h1.toLong() and 0xFFFFFFFFL) or (h2.toLong() shl 32),
                (h3.toLong() and 0xFFFFFFFFL) or (h4.toLong() shl 32),
            )
        }

        /**
         * Extracts a 32-bit integer from the given byte array starting at the specified offset.
         *
         * @param data The byte array from which the integer is extracted.
         * @param offset The starting position in the byte array. Must point to at least 4 bytes within the array.
         * @return The 32-bit integer assembled from 4 bytes starting at the offset.
         * @since 3.5.0
         */
        private fun getBlock32(data: ByteArray, offset: Int): Int =
            (data[offset].toInt() and 0xFF) or
                    ((data[offset + 1].toInt() and 0xFF) shl 8) or
                    ((data[offset + 2].toInt() and 0xFF) shl 16) or
                    ((data[offset + 3].toInt() and 0xFF) shl 24)

        /**
         * Finalizes the hash computation and mixes the bits to ensure high-quality outputs.
         *
         * @param h the intermediate hash value to be mixed.
         * @return the final mixed hash value.
         * @since 3.5.0
         */
        private fun fmix32(h: Int): Int {
            var x = h
            x = x xor (x ushr 16); x *= 0x85ebca6b.toInt()
            x = x xor (x ushr 13); x *= 0xc2b2ae35.toInt()
            x = x xor (x ushr 16)
            return x
        }

        /**
         * Converts the invoking object to a ByteArray representation based on its type.
         *
         * @return A ByteArray representing the object. If the object type is:
         * - `ByteArray`: Returns the object itself.
         * - `String`: Returns its UTF-8 encoded byte array.
         * - `Int`: Returns a 4-byte array in big-endian format.
         * - `Long`: Returns an 8-byte array in big-endian format.
         * - `Short`: Returns a 2-byte array in big-endian format.
         * - `Float`: Encodes as raw bits and converts to bytes.
         * - `Double`: Encodes as raw bits and converts to bytes.
         * - Any other type: Returns the UTF-8 encoded byte array of the object's string representation.
         * @since 3.5.0
         */
        private fun <T> T.toBytes(): ByteArray = when (this) {
            is ByteArray -> this
            is String -> encodeToByteArray()
            is Int -> ByteArray(4).also {
                val v = this
                it[0] = (v shr 24).toByte()
                it[1] = (v shr 16).toByte()
                it[2] = (v shr 8).toByte()
                it[3] = v.toByte()
            }
            is Long -> ByteArray(8).also {
                val v = this
                for (i in 7 downTo 0)
                    it[7 - i] = (v shr (1 * 8)).toByte()
            }
            is Short -> ByteArray(2).also {
                val v = toInt()
                it[0] = (v shr 8).toByte()
                it[1] = v.toByte()
            }
            is Float -> (toRawBits()).toBytes()
            is Double -> (toRawBits()).toBytes()
            else -> toString().encodeToByteArray()
        }
    }

    /**
     * Adds an element to the Bloom filter and updates its state.
     *
     * The Bloom filter uses multiple hash functions to determine the positions
     * of bits to set in the underlying bit array. If any bit is modified during
     * this process, the method returns `true`.
     *
     * @param element The element to be added to the Bloom filter.
     * @return `true` if the Bloom filter’s state was changed as a result
     *         of adding the element, `false` otherwise.
     * @since 3.5.0
     */
    fun put(element: T): Boolean {
        val bytes = element.toBytes()
        val [h1, h2] = murmurHash3_x86_128(bytes, 0)

        var bitsChanged = false
        for (i in 0 until numHashFunctions) {
            val combined = h1 + i.toLong() * h2
            val bitIndex = ((combined % numBits) + numBits) % numBits
            if (setBit(bitIndex)) bitsChanged = true
        }
        if (bitsChanged) count++
        return bitsChanged
    }

    /**
     * Adds the specified element to the Bloom filter.
     *
     * This operator function invokes the `put` method internally to hash and store
     * the element into the filter, updating the underlying bit array to reflect its presence.
     *
     * @param element The element to add to the Bloom filter.
     * @since 3.5.0
     */
    operator fun plusAssign(element: T) {
        put(element)
    }

    /**
     * Checks if the given element is likely contained within the Bloom filter.
     *
     * WARNING: This is a "migh contain".
     *
     * @param element The element to check for containment within the Bloom filter.
     * @return `true` if the element is likely contained in the Bloom filter, `false` otherwise.
     * @since 3.5.0
     */
    operator fun contains(element: T): Boolean {
        val bytes = element.toBytes()
        val [h1, h2] = murmurHash3_x86_128(bytes, 0)

        for (i in 0 until numHashFunctions) {
            val combined = h1 + i.toLong() * h2
            val bitIndex = ((combined % numBits) + numBits) % numBits
            if (!getBit(bitIndex)) return false
        }
        return true
    }

    /**
     * Clears the Bloom filter by resetting all bits to zero and setting the count of elements to zero.
     *
     * This operation effectively removes all elements from the filter, making it empty.
     * It does not change the size of the underlying bit array or alter any parameters related
     * to its configuration, such as the false positive rate or the number of hash functions.
     * @since 3.5.0
     */
    fun clear() {
        for (i in 0 until bits.length()) bits.set(i, 0L)
        count = 0
    }

    /**
     * Merges the current Bloom filter with another Bloom filter.
     * This operation combines the bit arrays of both Bloom filters, resulting in a union of the elements represented
     * in the two filters. The current Bloom filter is updated in place.
     *
     * @param other The Bloom filter to merge with. This filter must have the same number of hash functions and
     *              the same size (number of bits) as the current filter, otherwise an exception is thrown.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the number of hash functions or size of the Bloom filters differ.
     * @since 3.5.0
     */
    infix fun mergeWith(other: BloomFilter<T>) {
        validate(other.numHashFunctions == numHashFunctions) { "Number of hash functions must match" }
        validate(other.numBits == numBits) { "Number of bits must match" }

        for (i in 0 until bits.length()) {
            val otherWord = other.bits.get(i)
            if (otherWord != 0L) {
                while (true) {
                    val current = bits.get(i)
                    val merged = current or otherWord
                    if (merged == current || bits.compareAndSet(i, current, merged)) break
                }
            }
        }

        val setBits = (0 until bits.length()).sumOf { java.lang.Long.bitCount(bits.get(it)).toLong() }
        val ratio = setBits.toDouble() / numBits
        count = if (ratio >= 1.0) Long.MAX_VALUE
        else (-(numBits.toDouble() / numHashFunctions) * ln(1.0 - ratio)).toLong()
    }

    /**
     * Converts the BloomFilter instance to its string representation.
     *
     * @return A string containing details about the BloomFilter instance,
     * including expected insertions, false positive probability,
     * number of bits, number of hash functions, approximate insert count,
     * and the fill ratio.
     * @since 3.5.0
     */
    override fun toString(): String =
        "BloomFilter(expected=$expectedInsertions, fpp=$falsePositiveRate, " +
                "bits=$numBits, k=$numHashFunctions, inserted≈$count, fill=${"%.4f".format(fillRatio.toDouble(true))})"

    /**
     * Sets the bit at the specified index in the bit array if it is not already set.
     *
     * @param index The index of the bit to set. Must be a non-negative value.
     * @return `true` if the bit was successfully set, `false` if the bit was already set.
     * @since 3.5.0
     */
    private fun setBit(index: Long): Boolean {
        val wordIndex = (index ushr 6).toInt()
        val mask = 1L shl index.toInt()
        while (true) {
            val current = bits.get(wordIndex)
            if (current and mask != 0L) return false
            if (bits.compareAndSet(wordIndex, current, current or mask))
                return true
        }
    }

    /**
     * Retrieves the bit at the specified index in the underlying data structure.
     *
     * @param index The bit index to retrieve, where 0 represents the least significant bit.
     * @return True if the bit at the given index is set, false otherwise.
     * @since 3.5.0
     */
    private fun getBit(index: Long): Boolean {
        val wordIndex = (index ushr 6).toInt()
        val mask = 1L shl index.toInt()
        return bits.get(wordIndex) and mask != 0L
    }
}