package dev.tommasop1804.kutils.classes.memoization

import dev.tommasop1804.kutils.Transformer
import dev.tommasop1804.kutils.classes.time.Duration
import java.util.concurrent.ConcurrentHashMap

/**
 * A utility class that provides memoization for a given function, allowing the function to cache
 * its results for specific inputs based on the specified caching strategy. Helps improve
 * performance by avoiding redundant computations.
 *
 * @param T The type of the input to the function.
 * @param R The type of the result produced by the function.
 * @param transformer The function to be memoized.
 * @param cacheStrategy The caching strategy to be used. Determines how the input-output pairs
 * are stored and managed in the cache.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "unchecked_cast", "JavaCollectionWithNullableTypeArgument")
class Memoizer<T, R>(
    private val transformer: Transformer<T, R>,
    cacheStrategy: CacheStrategy = CacheStrategy.Unlimited
) {
    /**
     * A cache implementation that adapts its strategy based on the provided [CacheStrategy].
     *
     * - If the strategy is [CacheStrategy.Unlimited], the cache is implemented as a
     *   [ConcurrentHashMap], supporting unlimited entries.
     * - If the strategy is [CacheStrategy.LRU], an LRU (Least Recently Used) caching policy is applied,
     *   with a limit defined by [CacheStrategy.LRU.maxSize].
     * - If the strategy is [CacheStrategy.TTL], the cache applies a Time-To-Live policy, where entries
     *   expire after a duration specified by [CacheStrategy.TTL.defaultTTL].
     *
     * This variable is used for storing the cached results of computations, enabling efficient
     * memoization based on the selected strategy.
     *
     * @since 1.0.0
     */
    private val cache = when (cacheStrategy) {
        is CacheStrategy.Unlimited -> ConcurrentHashMap<T, R>()
        is CacheStrategy.LRU -> LRUCache<T, R>(cacheStrategy.maxSize)
        is CacheStrategy.TTL -> TTLCache<T, R>(cacheStrategy.defaultTTL)
    }
    
    /**
     * Invokes the memoized function with the provided input, utilizing the configured cache strategy
     * to store and retrieve results based on the input. The computation result for a given input
     * is cached and reused to improve performance on subsequent calls with the same input.
     *
     * @param input The input value for the memoized function.
     * @return The computed or cached result of the function for the given input.
     * @throws IllegalStateException If an invalid or unknown cache type is encountered.
     * @since 1.0.0
     */
    operator fun invoke(input: T): R = when (cache) {
        is ConcurrentHashMap<*, *> -> (cache as ConcurrentHashMap<T, R>).getOrPut(input) { transformer(input) }
        is LRUCache<*, *> -> (cache as LRUCache<T, R>).getOrPut(input) { transformer(input) }
        is TTLCache<*, *> -> (cache as TTLCache<T, R>).getOrPut(input) { transformer(input) }
        else -> throw IllegalStateException("Unknown cache type")
    }
    
    /**
     * Clears all entries from the underlying cache, regardless of the cache strategy being used.
     * This operation resets the cache to an empty state, removing all stored key-value mappings.
     *
     * Supported cache types include:
     * - Unlimited cache implemented using `ConcurrentHashMap`.
     * - Least Recently Used (LRU) cache.
     * - Time-to-Live (TTL) cache.
     *
     * @since 1.0.0
     */
    fun clear() {
        when (cache) {
            is ConcurrentHashMap<*, *> -> (cache as ConcurrentHashMap<T, R>).clear()
            is LRUCache<*, *> -> (cache as LRUCache<T, R>).clear()
            is TTLCache<*, *> -> (cache as TTLCache<T, R>).clear()
        }
    }
    
    /**
     * Removes an entry associated with the given key from the cache, if it exists.
     * The type of cache implementation determines the specific behavior.
     *
     * @param input the key whose associated entry is to be removed from the cache
     * @since 1.0.0
     */
    fun invalidate(input: T) {
        when (cache) {
            is ConcurrentHashMap<*, *> -> (cache as ConcurrentHashMap<T, R>).remove(input)
            is LRUCache<*, *> -> (cache as LRUCache<T, R>).remove(input)
            is TTLCache<*, *> -> (cache as TTLCache<T, R>).remove(input)
        }
    }

    /**
     * Represents caching strategies for controlling the behavior of cache usage and management.
     * This sealed class defines the different strategies that can be applied to manage cached data.
     *
     * @since 1.0.0
     * @author Tommaso Pastorelli
     */
    sealed class CacheStrategy {
        /**
         * Represents a cache strategy with no limitations or restrictions on size or expiration.
         * Use this strategy to allow unlimited storage of cached items.
         *
         * This cache strategy does not implement any form of cache eviction or
         * time-to-live management, and all items will be retained indefinitely.
         *
         * @since 1.0.0
         * @author Tommaso Pastorelli
         */
        object Unlimited : CacheStrategy()
        /**
         * Represents a cache strategy based on the Least Recently Used (LRU) eviction policy.
         * This strategy ensures that when the cache reaches its maximum capacity, the least recently used items are removed first.
         *
         * @property maxSize The maximum number of items the cache can hold. Once the limit is exceeded, the least recently used entries will be evicted.
         *
         * @since 1.0.0
         * @author Tommaso Pastorelli
         */
        data class LRU(val maxSize: Int) : CacheStrategy()
        /**
         * Represents a cache strategy where cached items have a defined time-to-live (TTL).
         *
         * @property defaultTTL The default duration after which items in the cache will expire.
         * @since 1.0.0
         * @author Tommaso Pastorelli
         */
        data class TTL(val defaultTTL: Duration) : CacheStrategy()
    }
}