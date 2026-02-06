package dev.tommasop1804.kutils.classes.memoization

import dev.tommasop1804.kutils.Instant
import dev.tommasop1804.kutils.classes.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Represents a generic cache interface providing basic caching operations.
 * Implementations of this interface must define the behaviors for storing,
 * retrieving, and removing data.
 *
 * @param K The type of key used to identify values in the cache.
 * @param V The type of values stored in the cache.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
sealed interface Cache<in K, V> {
    /**
     * Represents the current number of key-value pairs stored in the cache.
     *
     * This value reflects the size of the cache and is updated dynamically as elements are added or removed.
     *
     * @since 1.0.0
     */
    val size: Int
    /**
     * Checks if the given key exists in the cache.
     *
     * @param key The key to check for existence in the cache.
     * @return `true` if the key exists in the cache, `false` otherwise.
     * @since 1.0.0
     */
    operator fun contains(key: K): Boolean
    /**
     * Checks if a specified key-value pair exists in the cache.
     *
     * @param keyAndValue A pair representing the key and its corresponding value to be checked.
     * @return `true` if the key-value pair exists in the cache, `false` otherwise.
     * @since 1.0.0
     */
    operator fun contains(keyAndValue: Pair<K, V>): Boolean
    /**
     * Retrieves the value associated with the given key in the cache, or null if the key does not exist.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the specified key, or null if the key is not present in the cache.
     * @since 1.0.0
     */
    operator fun get(key: K): V?
    /**
     * Sets the specified key-value pair in the collection. If the key already exists,
     * updates its value with the provided one.
     *
     * @param key The key to be added or whose value is to be updated.
     * @param value The value to associate with the specified key.
     * @since 1.0.0
     */
    operator fun set(key: K, value: V)
    /**
     * Inserts or updates the value for the specified key in the cache.
     * If the key already exists, its value is replaced with the new value.
     *
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the previous value associated with the key, or null if there was no mapping for the key
     * @since 1.0.0
     */
    fun put(key: K, value: V): V?
    /**
     * Retrieves the value associated with the given key if it exists in the cache,
     * or computes the value using the provided defaultValue lambda, stores it
     * in the cache, and then returns the computed value.
     *
     * @param key The key whose associated value is to be returned or computed.
     * @param defaultValue A lambda function that generates the value to be associated
     *                     with the key if the key does not exist in the cache.
     * @return The existing value for the key, if it is present; otherwise,
     *         the computed value that has been added to the cache.
     * @since 1.0.0
     */
    fun getOrPut(key: K, defaultValue: () -> V): V
    /**
     * Removes the value associated with the specified key from the cache.
     *
     * @param key the key whose associated value is to be removed
     * @return the value that was associated with the key, or null if the key was not present in the cache
     * @since 1.0.0
     */
    fun remove(key: K): V?
    /**
     * Associates the specified key with the given value in the cache. If the key already exists,
     * its value is replaced with the specified value.
     *
     * @param keyAndValue A pair containing the key and value to be added or updated in the cache.
     * @since 1.0.0
     */
    operator fun plusAssign(keyAndValue: Pair<K, V>)
    /**
     * Removes the entry associated with the specified key from the cache.
     *
     * @param key The key whose mapping is to be removed from the cache.
     * @since 1.0.0
     */
    operator fun minusAssign(key: K)
    /**
     * Clears all entries from the cache, removing all key-value pairs.
     * After calling this method, the cache will be empty.
     *
     * @since 1.0.0
     */
    fun clear()
}

/**
 * A generic implementation of an LRU (Least Recently Used) cache. This class extends a basic `Cache` interface
 * and provides thread-safe operations using a `ReentrantLock`. It limits the number of elements stored to
 * a specified maximum size, evicting the least recently accessed element when the size exceeds the limit.
 *
 * @param K The type of keys the cache holds.
 * @param V The type of values the cache holds.
 * @property maxSize The maximum number of elements the cache is allowed to store.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
class LRUCache<K, V>(val maxSize: Int) : Cache<K, V> {
    /**
     * A private, customized `LinkedHashMap` implementation used to store a bounded cache of key-value pairs.
     * Maintains the order of access, enabling the least-recently-used (LRU) eviction policy.
     * When the size of the map exceeds the defined `maxSize`, the eldest entry is automatically removed.
     *
     * This implementation is part of the `LRUCache` class and ensures thread-safety with its containing class's mechanisms.
     *
     * @property maxSize The maximum number of entries this cache can hold.
     *
     * @since 1.0.0
     */
    private val cache = object : LinkedHashMap<K, V>(maxSize, 0.75f, true) {
        /**
         * Determines whether the eldest entry in the cache should be removed.
         *
         * Used in the implementation of an LRU (Least Recently Used) cache, this method
         * checks if the current size of the cache has exceeded the specified maximum size
         * and removes the eldest entry if true.
         *
         * @param eldest the eldest entry currently present in the cache, or null if the
         *               cache is empty.
         * @return a boolean indicating whether the eldest entry should be removed.
         * @since 1.0.0
         */
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?) = size > maxSize
    }

    /**
     * A ReentrantLock instance used to ensure thread-safe access and modification
     * of the LRUCache. It prevents concurrent threads from interacting with the
     * cache in an unsafe manner, ensuring consistency and integrity of cached data.
     *
     * @since 1.0.0
     */
    private val lock = ReentrantLock()

    /**
     * Returns the current number of elements in the cache.
     * This property is thread-safe and ensures that access to the backing cache's size
     * is synchronized using a locking mechanism.
     *
     * @return The number of entries currently stored in the cache.
     * @since 1.0.0
     */
    override val size
        get() = lock.withLock { cache.size }

    /**
     * Checks if the given key exists in the cache.
     *
     * @param key The key to check for existence in the cache.
     * @return `true` if the key exists in the cache, `false` otherwise.
     * @since 1.0.0
     */
    override operator fun contains(key: K): Boolean = lock.withLock { cache.containsKey(key) }

    /**
     * Checks if the specified key-value pair exists in the cache.
     *
     * @param keyAndValue the key-value pair to check for existence in the cache
     * @return true if the key exists in the cache and its associated value matches the specified value,
     *         false otherwise
     * @since 1.0.0
     */
    override operator fun contains(keyAndValue: Pair<K, V>): Boolean = lock.withLock {
        cache[keyAndValue.first] == keyAndValue.second
    }

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param key the key whose associated value is to be returned.
     * @return the value associated with the specified key, or `null` if the key is not present in the cache.
     * @since 1.0.0
     */
    override operator fun get(key: K): V? = lock.withLock { cache[key] }

    /**
     * Retrieves the value associated with the given [key] from the cache. If the [key] is not present,
     * the [defaultValue] function is invoked to provide a value, which is then added to the cache and returned.
     *
     * @param key The key to retrieve the associated value from the cache.
     * @param defaultValue A lambda function providing a default value that will be stored in the cache
     * if the [key] is not already present.
     * @return The value associated with the given [key], or the value returned by invoking [defaultValue] if the [key] is not present in the cache.
     * @since 1.0.0
     */
    override fun getOrPut(key: K, defaultValue: () -> V): V = lock.withLock {
        cache.getOrPut(key, defaultValue)
    }

    /**
     * Adds or updates the provided key-value pair in the cache. If the cache
     * has reached its maximum size, the least recently used entry will be evicted.
     *
     * @param key the key to add or update in the cache
     * @param value the value associated with the key
     * @since 1.0.0
     */
    override fun put(key: K, value: V) = lock.withLock {
        cache.put(key, value)
    }

    /**
     * Adds the given key-value pair to the cache. If the cache exceeds its maximum size after the addition,
     * the least recently used entry will be removed.
     *
     * @param keyAndValue A pair consisting of the key and value to be added to the cache.
     * @since 1.0.0
     */
    override operator fun plusAssign(keyAndValue: Pair<K, V>) = lock.withLock {
        cache.put(keyAndValue.first, keyAndValue.second)
        Unit
    }

    /**
     * Sets a new value in the cache for the specified key.
     * If the key already exists, its value will be updated.
     *
     * @param key The key of the entry to be inserted into the cache.
     * @param value The value to associate with the specified key.
     * @since 1.0.0
     */
    override operator fun set(key: K, value: V) = lock.withLock {
        cache[key] = value
    }

    /**
     * Removes the entry associated with the specified key from the cache.
     *
     * @param key The key of the entry to be removed.
     * @return The value associated with the removed key, or null if the key was not found in the cache.
     * @since 1.0.0
     */
    override fun remove(key: K): V? = lock.withLock { cache.remove(key) }

    /**
     * Removes the specified key and its associated value from the cache.
     * This operation is performed thread-safely using the internal lock mechanism.
     *
     * @param key The key of the entry to be removed from the cache.
     * @since 1.0.0
     */
    override operator fun minusAssign(key: K) = lock.withLock {
        cache.remove(key)
        Unit
    }

    /**
     * Removes all entries from the cache.
     *
     * This method clears the cache and resets its content to an empty state.
     * The operation is thread-safe and ensures that no concurrent modifications
     * will interfere with the clear process.
     *
     * @since 1.0.0
     */
    override fun clear() = lock.withLock { cache.clear() }
}

/**
 * A thread-safe cache implementation with a time-to-live (TTL) feature, allowing each entry to
 * expire and be removed automatically after a specified duration. This cache uses a default TTL
 * which can be overridden for individual entries.
 *
 * @param K Type for the keys used in the cache.
 * @param V Type for the values stored in the cache.
 * @param defaultTTL The default time-to-live duration for cache entries.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "JavaCollectionWithNullableTypeArgument")
class TTLCache<K, V>(private val defaultTTL: Duration = Duration(minutes = 5)) : Cache<K, V> {
    /**
     * Represents an entry stored in the cache, containing a value and its expiration timestamp.
     *
     * @param V The type of the value stored in the cache entry.
     * @property value The cached value.
     * @property expiresAt The absolute timestamp in milliseconds when this cache entry expires.
     *
     * This is a data structure used internally by the `TTLCache` to manage cached items alongside their time-to-live (TTL) metadata.
     *
     * @since 1.0.0
     */
    private data class CacheEntry<V>(
        val value: V,
        val expiresAt: Long
    )

    /**
     * A concurrent map used to store cache entries with their associated keys. This serves
     * as the core storage mechanism for the TTLCache, enabling thread-safe operations on the cached data.
     *
     * Keys are associated with `CacheEntry` objects, which encapsulate the cached value and its expiration timestamp.
     * The map allows efficient storage and retrieval of entries, ensuring proper synchronization across threads.
     *
     * @since 1.0.0
     */
    private val cache = ConcurrentHashMap<K, CacheEntry<V>>()

    /**
     * Represents the current number of entries in the cache.
     *
     * This property provides the size of the underlying storage within the cache.
     * It dynamically reflects the current number of key-value pairs stored
     * and is updated automatically as entries are added or removed.
     *
     * @since 1.0.0
     */
    override val size = cache.size

    /**
     * Retrieves the value associated with the specified key from the cache.
     * If the entry is not present or has expired, the method returns null.
     * If the entry is valid (not expired), the value is returned.
     * Expired entries are automatically removed from the cache.
     *
     * @param key The key whose associated value is to be retrieved.
     * @return The value associated with the specified key, or null if the key is not present
     *         or the entry has expired.
     * @since 1.0.0
     */
    override operator fun get(key: K): V? {
        val entry = cache[key] ?: return null
        return if (System.currentTimeMillis() < entry.expiresAt) {
            entry.value
        } else {
            cache.remove(key)
            null
        }
    }

    /**
     * Puts the specified key-value pair into the cache and sets its expiration time based on the default TTL.
     * If the key already exists, its value will be updated, and the expiration time will be reset.
     *
     * @param key The key associated with the value to insert or update in the cache.
     * @param value The value to associate with the specified key in the cache.
     * @return The value that was inserted or updated in the cache.
     * @since 1.0.0
     */
    override fun put(key: K, value: V): V? {
        val expiresAt = Instant().plus(defaultTTL)!!
        cache[key] = CacheEntry(value, expiresAt.toEpochMilli())
        return value
    }

    /**
     * Inserts a key-value pair into the cache with a specified time-to-live (TTL).
     * The entry will expire and be removed from the cache after the specified TTL duration elapses.
     *
     * @param key The key associated with the value being put into the cache.
     * @param value The value to store in the cache.
     * @param ttl The time-to-live duration after which the entry will expire.
     * @since 1.0.0
     */
    fun put(key: K, value: V, ttl: Duration) {
        val expiresAt = Instant().plus(ttl)!!
        cache[key] = CacheEntry(value, expiresAt.toEpochMilli())
        println(expiresAt)
    }

    /**
     * Retrieves the value associated with the specified [key] from the cache.
     * If the [key] is not present, computes the value using the provided [defaultValue] function,
     * inserts it into the cache with a predefined default time-to-live (TTL), and returns it.
     *
     * @param key The key whose associated value is to be retrieved or computed and added to the cache.
     * @param defaultValue A lambda function to compute the value to be associated with the [key] if it is not already present in the cache.
     * @return The value associated with the [key], either retrieved from the cache or computed and added to the cache.
     * @since 1.0.0
     */
    override fun getOrPut(key: K, defaultValue: () -> V): V {
        get(key)?.let { return it }
        val value = defaultValue()
        put(key, value, defaultTTL)
        return value
    }

    /**
     * Retrieves the value associated with the specified [key] from the cache if it exists. If the value
     * does not exist, it computes the value using [defaultValue], inserts it into the cache with the
     * given [ttl] (time-to-live), and then returns the computed value.
     *
     * @param key the key for which the value is to be retrieved or computed.
     * @param ttl the duration for which the computed value should remain in the cache.
     * @param defaultValue the lambda function used to compute the value if it is not present in the cache.
     * @return the existing or newly computed value associated with the key.
     * @since 1.0.0
     */
    fun getOrPut(key: K, ttl: Duration, defaultValue: () -> V): V {
        get(key)?.let { return it }
        val value = defaultValue()
        put(key, value, ttl)
        return value
    }

    /**
     * Checks if the specified key is present in the cache.
     *
     * @param key The key whose presence in this cache is to be tested.
     * @return `true` if the cache contains the specified key, `false` otherwise.
     * @since 1.0.0
     */
    override fun contains(key: K) = cache.containsKey(key)

    /**
     * Checks if the cache contains the specified key-value pair.
     *
     * @param keyAndValue a pair consisting of a key and its associated value to check for in the cache.
     * @since 1.0.0
     */
    override fun contains(keyAndValue: Pair<K, V>) = cache[keyAndValue.first] == keyAndValue.second

    /**
     * Updates or adds a value associated with the specified key in the collection.
     *
     * @param key the key with which the specified value is to be associated, must not be null.
     * @param value the value to associate with the specified key, must not be null.
     * @since 1.0.0
     */
    override operator fun set(key: K, value: V) {
        put(key, value)
    }

    /**
     * Adds the given key-value pair to the cache. Overwrites the existing value if the key already exists.
     *
     * @param keyAndValue A pair containing the key and the value to be added to the cache.
     * @since 1.0.0
     */
    override operator fun plusAssign(keyAndValue: Pair<K, V>) {
        put(keyAndValue.first, keyAndValue.second)
    }

    /**
     * Removes the specified key from the cache.
     *
     * This operator function allows you to use the `-=` syntax to remove an entry from the cache by its key.
     *
     * @param key the key to be removed from the cache
     * @since 1.0.0
     */
    override operator fun minusAssign(key: K) {
        remove(key)
    }

    /**
     * Removes the entry associated with the given key from the cache, if it exists.
     *
     * @param key the key whose associated entry is to be removed.
     * @return the value associated with the removed key, or null if the key was not present in the cache.
     * @since 1.0.0
     */
    override fun remove(key: K): V? = cache.remove(key)?.value

    /**
     * Clears all entries from the cache.
     *
     * This method removes all key-value pairs stored in the cache, effectively resetting it to an empty state.
     *
     * @since 1.0.0
     */
    override fun clear() = cache.clear()

    /**
     * Removes expired entries from the cache.
     *
     * This method iterates through the cache entries and removes any entry
     * whose expiration time has passed, based on the current system time.
     *
     * It is typically used to clean up outdated entries to free memory
     * and maintain efficient cache operations.
     *
     * @since 1.0.0
     */
    fun cleanup() {
        val now = System.currentTimeMillis()
        cache.entries.removeIf { it.value.expiresAt < now }
    }
}