@file:JvmName("MapUtilsKt")
@file:Suppress("unused", "UNCHECKED_CAST", "kutils_collection_declaration", "kutils_map_declaration", "kutils_drop_as_int_invoke",
    "kutils_null_check", "kutils_empty_check"
)
@file:Since("1.0.0")
@file:OptIn(ExperimentalContracts::class)

package dev.tommasop1804.kutils

import Break
import Continue
import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.tuple.toMapEntry
import dev.tommasop1804.kutils.exceptions.TooFewResultsException
import dev.tommasop1804.kutils.exceptions.TooManyElementsException
import dev.tommasop1804.kutils.exceptions.TooManyResultsException
import java.util.*
import java.util.stream.Collector
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.collections.forEach as kForEach
import kotlin.collections.groupBy as kGroupBy
import kotlin.collections.map as kMap
import kotlin.collections.mapKeys as kMapKeys
import kotlin.collections.mapNotNull as kMapNotNull
import kotlin.collections.mapValues as kMapValues

/**
 * Adds the specified entry to this map. If the key already exists in the map, 
 * the value associated with the key will be updated with the value from the provided entry.
 *
 * @param entry the entry to be added to the map, consisting of a key and a value
 * @since 1.0.0
 */
operator fun <K, V> MMap<K, V>.plusAssign(entry: Map.Entry<K, V>) { put(entry.key, entry.value) }
/**
 * Adds all the entries from the provided iterable to the current map. 
 * Each entry in the iterable is converted to a key-value pair and added to the map.
 *
 * @param entries An iterable collection of map entries to be added to the current map.
 * @since 1.0.0
 */
operator fun <K, V> MMap<K, V>.plusAssign(entries: Iterable<Map.Entry<K, V>>) { putAll(entries.mappedTo(Map.Entry<K, V>::toPair)) }
/**
 * Removes an entry from the map with the specified key and value.
 *
 * @param entry the key-value pair to be removed from the map.
 * @since 1.0.0
 */
operator fun <K, V> MMap<K, V>.minusAssign(entry: Map.Entry<K, V>) { remove(entry.key, entry.value) }
/**
 * Removes all specified entries from this map.
 *
 * @param entries The iterable collection of key-value pairs to be removed from the map.
 * @since 1.0.0
 */
fun <K, V> MMap<K, V>.removeAll(entries: Iterable<Map.Entry<K, V>>) { entries.forEach { remove(it.key, it.value) } }
/**
 * Removes all the specified entries from this map, if they are present.
 *
 * @param entries the entries to be removed from the map
 * @since 1.0.0
 */
operator fun <K, V> MMap<K, V>.minusAssign(entries: Iterable<Map.Entry<K, V>>) { removeAll(entries) }

/**
 * Adds one or more values to the collection associated with the specified key in the map.
 * If the key exists, the values will be added to the existing collection (a List or Set).
 * If the key does not exist, a new List with the values will be created and associated with the key.
 * The original map remains unchanged and a new map with the modifications is returned.
 *
 * @receiver The map to which the values should be added.
 * @param key The key for which the values should be added to the corresponding collection.
 * @param valuesToInsert The values to insert into the collection for the given key.
 * @return A new map that includes the modifications, or `null` if the operation could not be completed.
 * @since 1.0.0
 */
fun <K, V: Collection<IV>, IV> Map<K, V>.addToMapValue(key: K, vararg valuesToInsert: IV): Map<K, V> {
    val result = toMutableMap()
    if (key in result) {
        val isList = result[key] is List<*>
        val values = result[key]?.toMutableList() ?: emptyMList()
        values.addAll(valuesToInsert)
        result[key] = (if (isList) values else values.toSet()) as V
    } else result[key] = valuesToInsert.toMutableList() as V
    return result
}

/**
 * A property extension for a map that filters out entries with null keys.
 *
 * Returns a map containing only the entries whose keys are non-null.
 * The resulting map preserves the type of the original keys and values.
 *
 * @receiver A map with keys of nullable type.
 * @return A new map with all entries having null keys removed.
 * @since 1.0.0
 */
val <T, R> Map<T?, R>.noNullKeys
    get() = filterKeys { it.isNotNull() } as Map<T, R>

/**
 * Returns a new map containing only the entries from the original map
 * where the values are not null.
 *
 * This property extension filters out all key-value pairs from the map
 * where the value is null and returns a new map with non-null values.
 * The resulting map preserves the original key-value type pair.
 *
 * @receiver A map with nullable values.
 * @return A new map where all values are guaranteed to be non-null.
 * @since 1.0.0
 */
val <T, R> Map<T, R?>.noNullValues
    get() = filterValues { it.isNotNull() } as Map<T, R>

/**
 * Extension property for `Map` that filters out all entries with `null` keys or values.
 * Returns a new map containing only entries where both the key and value are non-null.
 *
 * This property ensures type safety by casting the resulting map to one with non-nullable keys and values.
 *
 * @receiver Map<T?, R?> A map with nullable keys and/or values.
 * @return Map<T, R> A new map containing only entries with non-null keys and values.
 * @since 1.0.0
 */
val <T, R> Map<T?, R?>.noNullEntries
    get() = filter { it.key.isNotNull() && it.value.isNotNull() } as Map<T, R>

/**
 * Merges the values of multiple maps into a single map.
 * If a key is present in multiple maps, their corresponding values, which must be `List`,
 * are concatenated into a single collection. Duplicate values are not removed.
 *
 * If all input maps are null or empty, the function returns null. If only one map is provided, it is returned as is.
 *
 * @receiver The map to which the values should be added.
 * @param K the type of the keys in the maps
 * @param V the type of the values in the maps, which must be a subclass of `List`
 * @param maps the vararg of maps to be merged. Any or all of the maps can be null
 * @return a new map consisting of merged keys and concatenated values, or null if all input maps are null or empty
 * @since 1.0.0
 */
fun <K, V> mergeMapsValuesList(vararg maps: Map<K, List<V>>): Map<K, List<V>> {
    if (maps.isEmpty()) return emptyMap()
    if (maps.size == 1) return maps[0]
    val result = maps[0].toMutableMap()
    for (map in maps.drop(1)) {
        if (map.isEmpty()) continue
        for ((key, value) in map) {
            if (key in result) {
                val mutableCollection = result[key]!!.toMutableList()
                mutableCollection.addAll(value)
                result[key] = mutableCollection
            } else result[key] = value
        }
    }
    return result
}

/**
 * Merges the values of multiple maps into a single map.
 * If a key is present in multiple maps, their corresponding values, which must be `Set`,
 * are concatenated into a single collection. Duplicate values are not removed.
 *
 * If all input maps are null or empty, the function returns null. If only one map is provided, it is returned as is.
 *
 * @receiver The map to which the values should be added.
 * @param K the type of the keys in the maps
 * @param V the type of the values in the maps, which must be a subclass of `Set`
 * @param maps the vararg of maps to be merged. Any or all of the maps can be null
 * @return a new map consisting of merged keys and concatenated values, or null if all input maps are null or empty
 * @since 1.0.0
 */
fun <K, V> mergeMapsValuesSet(vararg maps: Map<K, Set<V>>): Map<K, Set<V>> {
    if (maps.isEmpty()) return emptyMap()
    if (maps.size == 1) return maps[0]
    val result = maps[0].toMutableMap()
    for (map in maps.drop(1)) {
        if (map.isEmpty()) continue
        for ((key, value) in map) {
            if (key in result) {
                val mutableCollection = result[key]!!.toMutableSet()
                mutableCollection.addAll(value)
                result[key] = mutableCollection
            } else result[key] = value
        }
    }
    return result
}

/**
 * Merges multiple nested maps into a single map. If the same key exists in multiple maps:
 * - If the values are maps, they are merged recursively.
 * - If the values are collections, they are combined.
 * - Otherwise, the value from the last map is taken.
 *
 * @receiver The map to which the values should be added.
 * @param maps A variable number of nested maps to be merged. Null or empty maps are ignored.
 * @return A new map containing the merged key-value pairs, or `null` if no valid maps are provided.
 * @since 1.0.0
 */
fun mergeNestedMaps(vararg maps: Map<*, *>): Map<*, *> {
    val result = mutableMapOf<Any, Any>()
    if (maps.size == 1) return maps[0]

    for (map in maps) {
        if (map.isEmpty()) continue
        for ((key, value) in map) {
            if (key in result) {
                val existingValue = result[key]
                if (existingValue is Map<*, *> && value is Map<*, *>)
                    result[key as Any] = mergeNestedMaps(existingValue, value) as Any
                if (existingValue is Collection<*> && value is Collection<*>)
                    result[key as Any] = existingValue.plus(value) as Any
            } else result[key as Any] = value as Any
        }
    }
    return result
}

/**
 * Returns a map containing the common entries from the current map and one or more additional maps.
 * An entry is considered common if the same key-value pair exists in all of the provided maps.
 *
 * If no maps are provided as arguments, the method returns the original map.
 * If any of the provided maps are empty or null, the method returns an empty map.
 *
 * @receiver The map to which the values should be added.
 * @param maps One or more maps to be intersected with the current map.
 * @return A new map containing the common key-value pairs, or an empty map if no intersection exists.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.intersect(vararg maps: Map<K, V>): Map<K, V> {
    if (maps.isEmpty()) return this
    var result: Map<K, V> = this
    for (map in maps) {
        if (map.isEmpty()) return emptyMap()
        val tempMap = mutableMapOf<K, V>()
        for ((key, value) in result) {
            if (key in map && map[key] == value) tempMap[key] = value
        }
        result = tempMap
    }
    return result
}

/**
 * Computes the intersection of the current map and the provided map.
 * Returns a new map containing only the key-value pairs that exist in both maps
 * and have the same associated value.
 *
 * @param map the other map to be intersected with, can be null or empty
 * @return a new map containing the key-value pairs that are present in both maps
 * and have the same value; returns an empty map if the input map is null or empty
 * @since 1.0.0
 */
infix fun <K, V> Map<K, V>.intersect(map: Map<K, V>): Map<K, V> {
    var result: Map<K, V> = this
    if (map.isEmpty()) return emptyMap()
    val tempMap = mutableMapOf<K, V>()
    for ((key, value) in result) {
        if (key in map && map[key] == value) tempMap[key] = value
    }
    result = tempMap
    return result
}

/**
 * Checks whether two maps have at least one key-value pair in common.
 *
 * The function computes the intersection of the current map and the provided map
 * and determines if the resulting intersection map is not empty.
 *
 * @param other the other map to compare with the current map
 * @return `true` if the intersection of the two maps is not empty, otherwise `false`
 * @since 1.0.0
 */
infix fun <K, V> Map<K, V>.isIntersecting(other: Map<K, V>) = (intersect(other)).isNotEmpty()

/**
 * Subtracts key-value pairs from the current map based on the provided maps.
 *
 * This extension function removes entries from the current map that match the key-value pairs
 * present in any of the input maps. If multiple maps are provided, the subtraction is performed
 * iteratively for each map.
 *
 * @receiver The map to which the values should be added.
 * @param maps The maps containing key-value pairs to be removed from the current map.
 * @return A new map with the specified key-value pairs removed. Returns null if no input maps are specified.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.subtract(vararg maps: Map<K, V>): Map<K, V> {
    if (maps.isEmpty()) return this
    var result: Map<K, V> = this
    for (map in maps) {
        if (map.isEmpty()) continue
        val tempMap = result.toMutableMap()
        for ((key, value) in map) tempMap.remove(key, value)
        result = tempMap
    }
    return result
}

/**
 * Checks if a map contains a specific key-value pair.
 *
 * @receiver The map to which the values should be added.
 * @param pair the key-value pair to check for in the map.
 * @return `true` if the map contains the specified key and its corresponding value matches,
 *         otherwise `false`.
 * @since 1.0.0
 */
operator fun <K, V> Map<K, V>.contains(pair: Pair<K, V>): Boolean = containsKey(pair.first) && this[pair.first] == pair.second

/**
 * Checks if all the given key-value pairs exist in the map.
 *
 * @receiver The map to which the values should be added.
 * @param pairs The key-value pairs to check for containment in the map.
 * @return `true` if all the specified pairs are contained in the map, `false` otherwise.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.containsAll(vararg pairs: Pair<K, V>): Boolean {
    pairs.kForEach { if (!contains(it)) return false }
    return true
}

/**
 * Checks whether the map contains any of the specified key-value pairs.
 *
 * @receiver The map to which the values should be added.
 * @param pairs A vararg of key-value pairs to check for presence in the map.
 * @return `true` if at least one of the specified pairs is present in the map, `false` otherwise.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.containsAny(vararg pairs: Pair<K, V>): Boolean {
    pairs.kForEach { if (contains(it)) return true }
    return false
}

/**
 * Checks if none of the specified key-value pairs are present in the map.
 * The method returns `true` if none of the given pairs exist in the map; otherwise, it returns `false`.
 *
 * @receiver The map to which the values should be added.
 * @param pairs The key-value pairs to check for existence within the map.
 * @return `true` if none of the specified pairs are present in the map, `false` otherwise.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.containsNone(vararg pairs: Pair<K, V>): Boolean {
    pairs.kForEach { if (contains(it)) return false }
    return true
}

/**
 * Checks if the map contains all the specified keys.
 *
 * @receiver The map to which the values should be added.
 * @param keys The keys to check for presence in the map.
 * @return `true` if the map contains all the specified keys, `false` otherwise.
 * @since 1.0.0
 */
fun <K> Map<K, *>.containsAllKeys(vararg keys: K): Boolean {
    keys.kForEach { if (!containsKey(it)) return false }
    return true
}

/**
 * Checks if the map contains at least one of the specified keys.
 *
 * @receiver The map to which the values should be added.
 * @param keys The keys to check for existence in the map.
 * @return `true` if the map contains at least one of the specified keys, `false` otherwise.
 * @since 1.0.0
 */
fun <K> Map<K, *>.containsAnyKeys(vararg keys: K): Boolean {
    keys.kForEach { if (containsKey(it)) return true }
    return false
}

/**
 * Checks if the map does not contain any of the specified keys.
 *
 * This function iterates through the provided keys and verifies that none of them
 * are present as keys in the map. If at least one of the keys is found, it returns `false`.
 * If no keys are found, it returns `true`.
 *
 * @receiver The map to which the values should be added.
 * @param keys The keys to check for absence in the map.
 * @return `true` if none of the specified keys are present in the map, `false` otherwise.
 * @since 1.0.0
 */
fun <K> Map<K, *>.containsNoneKeys(vararg keys: K): Boolean {
    keys.kForEach { if (containsKey(it)) return false }
    return true
}

/**
 * Checks whether the map contains all the specified values.
 *
 * @receiver The map to which the values should be added.
 * @param values The values to check for presence in the map.
 * @return `true` if all the specified values are present in the map, `false` otherwise.
 * @since 1.0.0
 */
fun <V> Map<*, V>.containsAllValues(vararg values: V): Boolean {
    values.kForEach { if (!containsValue(it)) return false }
    return true
}

/**
 * Checks if the map contains any of the specified values.
 *
 * This function iterates through the given values and returns `true`
 * if any of the specified values exist in the map. Otherwise, it returns `false`.
 *
 * @receiver The map to which the values should be added.
 * @param V the type of values in the map
 * @param values the values to check for existence in the map
 * @return `true` if any of the specified values exist in the map, `false` otherwise
 * @since 1.0.0
 */
fun <V> Map<*, V>.containsAnyValues(vararg values: V): Boolean {
    values.kForEach { if (containsValue(it)) return true }
    return false
}

/**
 * Checks if the map does not contain any of the specified values.
 *
 * This function iterates through the provided values and verifies that none
 * of them exist in the map as values. If any of the specified values are found
 * in the map, the function returns `false`. Otherwise, it returns `true`.
 *
 * @receiver The map to which the values should be added.
 * @param values The values to check against the map's values.
 * @return `true` if none of the specified values are present in the map, `false` otherwise.
 * @since 1.0.0
 */
fun <V> Map<*, V>.containsNoneValues(vararg values: V): Boolean {
    values.kForEach { if (containsValue(it)) return false }
    return true
}

/**
 * Divides the current map into a list of sub-maps, each containing at most the specified number of entries.
 *
 * The original map is split into chunks based on the `limit` parameter. Each resulting sub-map retains
 * the insertion order of the original map. If the number of entries in the map is not divisible by `limit`,
 * the last sub-map will contain the remaining entries.
 *
 * @receiver The map to which the values should be added.
 * @param limit The maximum number of entries in each sub-map. Must be greater than 0.
 * @return A list of sub-maps, each containing at most `limit` entries, or `null` if the map is empty.
 * @since 1.0.0
 */
infix fun <K, V> Map<K, V>.chunked(limit: Int): List<Map<K, V>> {
    val result = mutableListOf<Map<K, V>>()
    val currentSubmap = mutableMapOf<K, V>()
    for ((key, value) in this) {
        currentSubmap[key] = value
        if (currentSubmap.size == limit) {
            result += currentSubmap.toMap()
            currentSubmap.clear()
        }
    }
    if (currentSubmap.isNotEmpty()) result += currentSubmap.toMutableMap()
    return result
}

/**
 * Divides the entries of the map into two separate maps based on the given predicate.
 * The first map contains entries that satisfy the predicate, while the second map
 * contains entries that do not satisfy the predicate.
 *
 * @param predicate a condition used to evaluate each entry in the map
 * @return a pair of maps, where the first map contains entries matching the predicate,
 *         and the second map contains the remaining entries
 * @since 1.0.0
 */
operator fun <K, V> Map<K, V>.div(predicate: Predicate<Map.Entry<K, V>>): MonoPair<Map<K, V>> {
    val first = emptyMMap<K, V>()
    val second = emptyMMap<K, V>()
    for (element in this) {
        if (predicate(element)) {
            first += element
        } else {
            second += element
        }
    }
    return Pair(first, second)
}

/**
 * Splits the map into a list of sub-maps, each containing at most the specified number of entries.
 *
 * The original map is divided into smaller sub-maps using the specified `limit`. Each resulting sub-map
 * preserves the insertion order of the original map. If the map cannot be evenly divided by `limit`,
 * the final sub-map will contain the remaining entries.
 *
 * @receiver The original map to be divided.
 * @param limit The maximum number of entries allowed in each resulting sub-map. Must be greater than 0.
 * @return A list of sub-maps, each containing at most `limit` entries.
 * @since 1.0.0
 */
operator fun <K, V> Map<K, V>.rem(limit: Int): List<Map<K, V>> = chunked(limit)
/**
 * Splits the current [MMap] into a list of [MMap] instances, each containing at most the specified number of entries.
 *
 * The original [MMap] is divided into chunks based on the `limit` parameter. Each resulting [MMap] retains
 * the insertion order of the original [MMap]. If the number of entries is not divisible by `limit`,
 * the last [MMap] will contain the remaining entries.
 *
 * @param limit The maximum number of entries in each resulting [MMap]. Must be greater than 0.
 * @return A mutable list of [MMap] instances, each containing at most `limit` entries.
 * @since 1.0.0
 */
@JvmName("mutableMapRem")
operator fun <K, V> MMap<K, V>.rem(limit: Int): MList<MMap<K, V>> = chunked(limit).kMap { it.toMMap() }.toMList()

/**
 * Iterates through the map and performs the given action on each entry.
 * The iteration can be interrupted by throwing specific exceptions with optional results.
 *
 * @param action a lambda expression to be invoked on each entry in the map. The lambda
 *               receives a single parameter, which is the current map entry.
 * @return the result of the operation of type [R], if provided as part of a `Continue` exception;
 *         otherwise, returns `null`. If the iteration is interrupted by a `Break` exception,
 *         the result provided with the `Break` is returned immediately.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <K, V, R> Map<K, V>.forEachWithReturn(action: ReceiverBiConsumer<LoopContext, Map.Entry<K, V>>): R? {
    with(LoopContext()) {
        for (element in this@forEachWithReturn) {
            try {
                action(element)
            } catch (b: Break) {
                return b.result as? R
            } catch (c: Continue) {
            }
        }
    }
    return null
}

/**
 * Returns the first key-value pair of the map as a [Map.Entry] instance.
 *
 * This function retrieves the first entry from the map based on its iteration order.
 * If the map is empty, a [NoSuchElementException] is thrown.
 *
 * @throws NoSuchElementException if the map is empty.
 * @return the first key-value pair in the map.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.first() = entries.first()
/**
 * Finds the first key-value pair in the map that matches the specified predicate.
 *
 * This function evaluates the entries of the map in the order of iteration, returning
 * the first entry that satisfies the provided predicate. If no entry matches the predicate,
 * a [NoSuchElementException] is thrown.
 *
 * @param predicate the condition used to evaluate each map entry
 * @return the first map entry that matches the predicate
 * @throws NoSuchElementException if no entry matches the predicate
 * @since 1.0.0
 */
infix fun <K, V> Map<K, V>.first(predicate: Predicate<Map.Entry<K, V>>) = entries.first(predicate)
/**
 * Returns the first entry in the map that matches the given [predicate],
 * or the result of the [default] supplier if no such entry is found.
 *
 * @receiver the map to search for the matching entry.
 * @param default a supplier function that provides a fallback key-value pair if no entry matches the [predicate].
 * @param predicate a predicate to test each entry for a match.
 * @return the first matching entry if found, otherwise the result of the [default] function.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.firstOr(default: Supplier<Pair<K, V>>, predicate: Predicate<Map.Entry<K, V>>) = entries.firstOr({ default().toMapEntry() }, predicate)
/**
 * Returns the first entry in the map that matches the given predicate, or throws an exception
 * created by the provided lambda function if no such entry exists.
 *
 * @receiver the map on which the operation is applied
 * @param lazyException a lambda function that provides the exception to be thrown
 * if no entry matching the predicate is found
 * @param predicate a function that defines the condition the entry must satisfy
 * @return the first entry in the map that matches the predicate
 * @throws Throwable the exception provided by the lambda function if no matching entry is found
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.firstOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Map.Entry<K, V>>) = entries.firstOrThrow(lazyException)

/**
 * Determines the most frequent value in the map's values. If there are multiple values
 * with the same highest frequency, one of them is returned arbitrarily. If the map is empty,
 * the result is null.
 *
 * @return the most frequent value in the map's values, or null if the map is empty.
 * @since 1.0.0
 */
fun <V> Map<*, V>.valuesMode(): V? = values.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

/**
 * Returns the single entry present in the map or throws an exception if the map does not
 * meet the condition of having exactly one entry.
 *
 * If the map is empty, a [NoSuchElementException] is thrown.
 * If the map contains more than one entry, a [TooManyElementsException] is thrown,
 * with the size of the map included in the exception.
 *
 * @throws NoSuchElementException if the map is empty.
 * @throws TooManyElementsException if the map contains more than one entry.
 * @return the single [Map.Entry] present in the map.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.onlyEntry() = entries.run {
    if (isEmpty()) throw NoSuchElementException()
    else if (size == 1) first() else throw TooManyElementsException(size)
}
/**
 * Returns the only entry in the map if the map contains exactly one entry, or `null` otherwise.
 *
 * @receiver the map to evaluate
 * @return the only entry in the map if its size is 1, or `null` if the map contains zero or more than one entry
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.onlyEntryOrNull() = entries.run { if (size == 1) first() else null }
/**
 * Returns the single entry of the map if the map contains exactly one entry. Otherwise, retrieves and returns
 * a default entry provided by the supplied parameter.
 *
 * @param default a supplier function that provides a default map entry to return when the map does not contain exactly one entry.
 * @since 1.0.0
 */
infix fun <K, V> Map<K, V>.onlyEntryOr(default: Supplier<Pair<K, V>>) = entries.run { if (size == 1) first() else default().toMapEntry() }
/**
 * Returns the only entry in the map or throws an exception if the map contains zero or more than one entry.
 *
 * @param lazyException A supplier that provides the exception to be thrown if the map does not contain exactly one entry.
 * @return The single entry in the map if the map contains exactly one entry.
 * @throws Throwable The exception provided by the lazyException supplier if the map does not have exactly one entry.
 * @since 1.0.0
 */
infix fun <K, V> Map<K, V>.onlyEntryOrThrow(lazyException: ThrowableSupplier) = entries.run { if (size == 1) first() else throw lazyException() }
/**
 * Filters the map based on a given predicate and ensures that exactly one entry matches.
 *
 * This extension function applies the given predicate to each entry in the map and verifies that
 * only a single entry satisfies the condition. If no entries satisfy the predicate, a
 * `NoSuchElementException` is thrown. If multiple entries satisfy the predicate, a
 * `TooManyResultsException` is thrown.
 *
 * @param predicate a `BiPredicate<K, V>` that takes a key and a value as parameters and returns `true`
 *                  if the entry satisfies the condition, `false` otherwise
 * @return the single entry that satisfies the predicate
 * @throws NoSuchElementException if no entries in the map satisfy the predicate
 * @throws TooManyResultsException if more than one entry satisfies the predicate
 * @since 1.0.0
 */
infix fun <K, V> Map<K, V>.onlyEntry(predicate: BiPredicate<K, V>) = entries
    .requireOrThrow({ NoSuchElementException() }, { isNotEmpty() })
    .filter { (k, v) -> predicate(k, v) }.run {
        if (size == 1) first()
        else throw if (size > 1) TooManyResultsException(size) else TooFewResultsException(size)
    }
/**
 * Filters the entries of the map that satisfy the given predicate and ensures that exactly one result exists.
 *
 * Throws a `NoSuchElementException` if the map is empty or if no entries satisfy the predicate.
 * Throws a `TooManyResultsException` if more than one entry satisfies the predicate.
 * Throws a `TooFewResultsException` if the result size is less than required.
 *
 * @param predicate the predicate used to filter the entries of the map
 * @return the single entry that satisfies the predicate
 * @throws NoSuchElementException if the map is empty or no entries satisfy the predicate
 * @throws TooManyResultsException if more than one entry satisfies the predicate
 * @throws TooFewResultsException if less than one entry satisfies the predicate
 * @since 1.0.0
 */
infix fun <K, V> Map<K, V>.onlyEntry(predicate: Predicate<Map.Entry<K, V>>) = entries
    .requireOrThrow({ NoSuchElementException() }, { isNotEmpty() })
    .filter(predicate).run {
        if (size == 1) first()
        else throw if (size > 1) TooManyResultsException(size) else TooFewResultsException(size)
    }
/**
 * Returns the only entry in the map that satisfies the given predicate, or null if the predicate matches
 * no entries or more than one entry.
 *
 * @param predicate A predicate function that takes a key-value pair and returns true if the pair satisfies
 * the condition.
 * @since 1.0.0
 */
infix fun <K, V> Map<K, V>.onlyEntryOrNull(predicate: BiPredicate<K, V>) = filter { (k, v) -> predicate(k, v) }.entries.run { if (size == 1) first() else null }
/**
 * Returns the single entry in the map that matches the given [predicate], or `null` if no entry
 * matches or more than one entry matches the [predicate].
 *
 * @param predicate a functional interface used to test each entry in the map for a match
 * @return the single matching entry or `null` if there is no match or multiple matches
 * @since 1.0.0
 */
infix fun <K, V> Map<K, V>.onlyEntryOrNull(predicate: Predicate<Map.Entry<K, V>>) = filter(predicate).entries.run { if (size == 1) first() else null }
/**
 * Returns the only entry from the map that matches the specified predicate.
 * If no entries or multiple entries match the predicate, returns a default entry provided by the supplier.
 *
 * @param default a supplier that provides the default entry if none or multiple entries match the predicate
 * @param predicate a predicate to evaluate each key-value pair in the map
 * @return the single matching map entry if exactly one matches the predicate, otherwise the supplied default entry
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.onlyEntryOr(default: Supplier<Pair<K, V>>, predicate: BiPredicate<K, V>) = filter { (k, v) -> predicate(k, v) }.entries.run { if (size == 1) first() else default().toMapEntry() }
/**
 * Filters entries in a map based on a given predicate and returns the single matching entry.
 * If there is no matching entry or more than one entry matches, the provided default value is returned.
 *
 * @param default a supplier providing a default map entry to return if the predicate does not match exactly one entry
 * @param predicate a condition to filter the entries of the map
 * @return the single map entry matching the predicate, or the default value if none or more than one entry matches
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.onlyEntryOr(default: Supplier<Pair<K, V>>, predicate: Predicate<Map.Entry<K, V>>) = filter(predicate).entries.run { if (size == 1) first() else default().toMapEntry() }
/**
 * Filters the map and ensures there is exactly one entry that matches the given predicate.
 * If the number of entries matching the predicate is not exactly one, the provided exception
 * is thrown.
 *
 * @param lazyException A supplier that provides the exception to be thrown if the number of matching entries is not exactly one.
 * @param predicate A condition that each entry in the map is tested against.
 * @return The unique entry (as a Map.Entry) that matches the predicate if exactly one entry satisfies the condition.
 * @throws Throwable The exception provided by the lazyException supplier if the number of matching entries is different from one.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.onlyEntryOrThrow(lazyException: ThrowableSupplier, predicate: BiPredicate<K, V>) = filter { (k, v) -> predicate(k, v) }.entries.run { if (size == 1) first() else throw lazyException() }
/**
 * Filters the map according to the provided predicate and ensures that it contains only one matching entry.
 * If there is exactly one entry that matches the predicate, it returns that entry.
 * Otherwise, it throws the exception supplied by the given lazy exception supplier.
 *
 * @param lazyException A supplier function that provides the exception to be thrown when the number of matching entries is not exactly one.
 * @param predicate A predicate to filter the entries in the map.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.onlyEntryOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Map.Entry<K, V>>) = filter(predicate).entries.run { if (size == 1) first() else throw lazyException() }

/**
 * Iterates over all the entries of the map and performs the given action on each entry.
 * This method enables performing a certain operation on the map without modifying it,
 * returning the original map after the operation.
 *
 * @param action a consumer function that will be applied to each entry in the map
 * @since 1.0.0
 */
infix fun <M : Map<K, V>, K, V> M.peek(action: Consumer<Map.Entry<K, V>>) = apply { entries.forEach(action) }

/**
 * Collects the entries of the map using the provided [Collector].
 *
 * This method allows for processing and transforming the entries of the map
 * into a desired result, leveraging the capabilities of the Java Stream API.
 *
 * @param collector the collector that defines the accumulation and transformation
 *                   strategy for the map entries
 * @return the result produced by the collector
 * @since 1.0.0
 */
fun <K, V, A, R> Map<K, V>.legacyCollect(collector: Collector<Map.Entry<K, V>, A, R>): R = entries.stream().collect(collector)
/**
 * Performs a reduction on the elements of the map, using the provided supplier, accumulator,
 * and combiner functions. This method allows the map entries to be collected into a mutable
 * result container.
 *
 * @param supplier a function that provides a new mutable result container
 * @param accumulator a function that accumulates a map entry into the mutable result container
 * @param combiner a function that combines two partial result containers
 * @return the result of the reduction
 * @since 1.0.0
 */
fun <K, V, R> Map<K, V>.legacyCollect(
    supplier: Supplier<R>,
    accumulator: BiConsumer<R, Map.Entry<K, V>>,
    combiner: BiConsumer<R, R>
): R = entries.stream().collect(supplier, accumulator, combiner)

/**
 * Retrieves the value associated with the provided key from the map, or throws an exception
 * if the key does not exist in the map or is null.
 *
 * @param key the key whose associated value is to be returned
 * @param lazyException a lambda providing the exception to be thrown if the key does not exist
 * @return the value associated with the specified key, or null if the value is null
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.getOrThrow(key: K, lazyException: ThrowableSupplier = { NoSuchElementException("Element with key $key is not present") }): V =
    this[key] ?: throw lazyException()

/**
 * Converts the current map with String keys to a Properties object.
 *
 * The method transfers all entries of the map into a new Properties instance.
 *
 * @return a Properties object containing all entries of the map
 * @since 1.0.0
 */
fun <V> Map<String, V>.toProperties(): Properties = Properties().apply { putAll(this@toProperties) }

/**
 * Filters the entries of the Map and returns a new Map containing only the entries whose keys start with the specified prefix.
 *
 * @param prefix The prefix used to filter keys in the Map. Only entries with keys starting with this prefix will be included in the resultant Map.
 * @return A new Map containing entries with keys that start with the given prefix.
 * @since 1.0.0
 */
infix fun <V> Map<String, V>.filterByKeyPrefix(prefix: String): Map<String, V> = filterKeys { it.startsWith(prefix) }

/**
 * Groups the keys of the map by their corresponding values.
 * Returns a map where each key is a value from the original map,
 * and the value is a list of keys from the original map that were associated with that value.
 *
 * @return A new map where the keys are the unique values of the original map,
 * and the values are lists of keys from the original map that share the same value.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.groupByValue(): MultiMap<V, K> = entries.kGroupBy({ it.value }, { it.key })

/**
 * Transforms the entries of a map into a new map with keys and values mapped
 * using the provided transformation function.
 *
 * @param transform A function that takes a map entry (key-value pair) from the
 * original map and returns a pair of type K2 and V2, representing the new key
 * and value respectively.
 * @since 1.0.0
 */
inline infix fun <K1, V1, K2, V2> Map<K1, V1>.mapToMap(transform: Transformer<Map.Entry<K1, V1>, Pair<K2, V2>>) = entries.associate { transform(it) }

/**
 * Creates a new map by transforming the keys and values of the original map using the provided mapping functions.
 *
 * @param transformKeys A function that takes a map entry and transforms its key into a new key for the resulting map.
 * @param trasnformValues A function that takes a map entry and transforms its value into a new value for the resulting map.
 * @since 1.0.0
 */
inline fun <K1, V1, K2, V2> Map<K1, V1>.mapToMap(transformKeys: Transformer<Map.Entry<K1, V1>, K2>, trasnformValues: Transformer<Map.Entry<K1, V1>, V2>) = entries.associate { transformKeys(it) to trasnformValues(it) }

/**
 * Finds the first entry in the map that matches the given predicate.
 *
 * Iterates through the map entries and applies the specified predicate
 * to each entry until a match is found. Returns the first matching entry
 * or `null` if no match is found.
 *
 * @param predicate A condition defined as a `Predicate` that is applied
 *                  to each map entry to determine if it matches.
 * @since 1.0.0
 */
inline fun <K, V> Map<K, V>.find(predicate: Predicate<Map.Entry<K, V>>) = entries.find { predicate(it) }

/**
 * Applies a transformation function to each entry in the map and returns the result.
 *
 * @param transformer A function that takes a map entry as input and returns a transformed value of type R.
 * @return A collection of transformed values resulting from applying the transformer to each entry in the map.
 * @since 1.0.0
 */
inline infix fun <K, V, R> Map<K, V>.mappedTo(transformer: Transformer<Map.Entry<K, V>, R>) = kMap(transformer)

/**
 * Returns a new map with keys transformed by the specified [transformer], keeping the
 * same values as the original map.
 *
 * The [transformer] is applied to each entry of the original map, and the result is used
 * as the key in the resulting map. The transformation does not alter the values of the map.
 *
 * @param transformer A function that takes a map entry as input and returns the transformed key.
 * @since 1.0.0
 */
inline infix fun <K, V, R> Map<K, V>.keysMappedTo(transformer: Transformer<Map.Entry<K, V>, R>) = kMapKeys(transformer)

/**
 * Transforms the values of a map using the provided transformer function, while keeping the keys unchanged.
 *
 * @param transformer a function that takes a map entry (key-value pair) and returns a transformed value.
 * @since 1.0.0
 */
inline infix fun <K, V, R> Map<K, V>.valuesMappedTo(transformer: Transformer<Map.Entry<K, V>, R>) = kMapValues(transformer)

/**
 * Applies the given transformer function to each entry in the Map and returns a new map containing
 * only the non-null results of the transformation.
 *
 * @param transformer A function that takes a map entry (key-value pair) as input and returns a transformed result,
 * which may be null. Non-null results are included in the resulting map.
 * @since 1.0.0
 */
inline infix fun <K, V, R> Map<K, V>.mappedToNotNull(transformer: Transformer<Map.Entry<K, V>, R?>) = kMapNotNull(transformer)

/**
 * Checks if a nullable map is neither null nor empty.
 *
 * This method returns true if the map is not null and contains at least one key-value pair.
 * Otherwise, it returns false.
 *
 * @receiver Nullable map that will be checked.
 * @return `true` if the map is not null and not empty, `false` otherwise.
 * @since 1.0.0
 */
@Suppress("kutils_null_check")
fun <K, V> Map<K, V>?.isNotNullOrEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return isNotNull() && isNotEmpty()
}

/**
 * Checks whether the Map is null or empty.
 *
 * This operator function is used to simplify the null or empty check
 * on a Map instance. If the Map is either null or contains no key-value
 * pairs, this function will return `true`. Otherwise, it will return `false`.
 *
 * @return `true` if the Map is null or empty, `false` otherwise.
 * @since 1.0.0
 */
operator fun <K, V> Map<K, V>?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrEmpty()
}

/**
 * Returns the result of the provided `block` if the map is null or empty, otherwise returns the map itself.
 *
 * @param block a supplier function that provides a new map when the current map is null or empty
 * @return the current map if it is not null or empty, otherwise the result of the `block` function
 * @since 1.0.0
 */
infix fun <M : Map<K, V>, K, V> M?.ifNullOrEmpty(block: Supplier<M>): M = if (isNullOrEmpty()) block() else this

/**
 * Retrieves the first entry in the map that satisfies the given predicate.
 *
 * This operator function serves as a shorthand for applying the `find` function
 * with a predicate to the map, returning the first map entry that matches the
 * specified condition or `null` if no such entry is found.
 *
 * @param find A predicate used to find a specific map entry. The predicate is applied
 *             to each entry in the map to evaluate if it matches the desired condition.
 * @since 1.0.0
 */
operator fun <K, V> Map<K, V>.get(find: Predicate<Map.Entry<K, V>>) = find(find)
/**
 * Retrieves the first entry in the map that satisfies the given predicate.
 *
 * This operator function serves as a shorthand for applying the `find` function
 * with a predicate to the map, returning the first map entry that matches the
 * specified condition or `null` if no such entry is found.
 *
 * @param find A predicate used to find a specific map entry. The predicate is applied
 *             to each entry in the map to evaluate if it matches the desired condition.
 * @param lazyException A supplier for the exception to be thrown if no element is found.
 * @since 1.0.0
 */
operator fun <K, V> Map<K, V>.get(find: Predicate<Map.Entry<K, V>>, lazyException: ThrowableSupplier) = find(find) ?: throw lazyException()

/**
 * Applies the given predicate to filter the entries of the map and returns the filtered result.
 *
 * @param filter A predicate to apply on the map entries. The predicate defines the condition
 *               to filter the entries of the map.
 * @since 1.0.0
 */
operator fun <K, V> Map<K, V>.invoke(filter: Predicate<Map.Entry<K, V>>) = filter(filter)

/**
 * Creates and returns a new empty mutable map.
 *
 * This function provides a type-safe way of creating an empty `MutableMap`
 * without needing to specify its type explicitly.
 * The returned map can have elements added or removed, as it is mutable.
 *
 * @return A new empty mutable map of the specified type.
 * @since 1.0.0
 */
fun <K, V> emptyMMap(): MMap<K, V> = mutableMapOf()

/**
 * Creates a new mutable map with the specified pairs.
 *
 * This function allows you to create a `MutableMap` with an initial set of key-value pairs.
 *
 * @param pairs a vararg of key-value pairs to populate the `MutableMap`
 * @return a new mutable map containing the provided key-value pairs
 * @since 1.0.0
 */
fun <K, V> mMapOf(vararg pairs: Pair<K, V>): MMap<K, V> = mutableMapOf(*pairs)

/**
 * Converts the current [Map] instance to a [MMap], which is a mutable map representation of it.
 *
 * @return A [MMap] containing all entries from the original map.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.toMMap(): MMap<K, V> = toMutableMap()