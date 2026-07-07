/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("MapUtilsKt")
@file:Suppress("unused", "UNCHECKED_CAST", "kutils_collection_declaration", "kutils_map_declaration", "kutils_drop_as_int_invoke",
    "kutils_null_check", "kutils_empty_check", "deprecation"
)
@file:Since("1.0.0")
@file:OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)
@file:MustUseReturnValues

package dev.tommasop1804.kutils

import Break
import Continue
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.exceptions.*
import java.util.*
import java.util.stream.Collector
import kotlin.collections.map
import kotlin.collections.putAll
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.collections.forEach as kForEach
import kotlin.collections.groupBy as kGroupBy
import kotlin.collections.map as kMap

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
 * Determines if the map contains exactly one entry.
 *
 * This property checks whether the map has a single key-value pair by leveraging the `onlyEntryOrNull` function
 * to determine if the map has exactly one entry, and then validates its non-null status using `isNotNull`.
 *
 * @receiver the map being evaluated
 * @return `true` if the map contains exactly one key-value pair, `false` otherwise
 * @since 1.1.0
 */
val Map<*, *>.isSingleElement: Boolean get() = onlyEntryOrNull().isNotNull()
/**
 * Extension property for a Map that checks if the map does not contain exactly one key-value pair.
 *
 * @return `true` if the map contains zero or more than one entries, `false` if it contains exactly one entry.
 * @since 1.1.0
 */
val Map<*, *>.isNotSingleElement: Boolean get() = !isSingleElement

/**
 * Determines the most frequent value in the map's values. If there are multiple values
 * with the same highest frequency, one of them is returned arbitrarily. If the map is empty,
 * the result is null.
 *
 * @return the most frequent value in the map's values, or null if the map is empty.
 * @since 2.0.0
 */
val <V> Map<*, V>.valuesMode get() = values.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key

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
operator fun <K, V> MMap<K, V>.plusAssign(entries: Iterable<Map.Entry<K, V>>) { putAll(entries.map(Map.Entry<K, V>::toPair)) }
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
fun <K, V> mergeMapsValuesList(vararg maps: MultiMap<K, V>): MultiMap<K, V> {
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
fun <K, V> mergeMapsValuesSet(vararg maps: SetMap<K, V>): SetMap<K, V> {
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
 * @since 1.1.0
 */
infix fun <K, V> Map<K, V>.intersects(other: Map<K, V>) = (intersect(other)).isNotEmpty()

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
 * Checks if any entry in the map satisfies the given predicate.
 *
 * @param predicate A predicate function that takes a map entry as a parameter and returns a Boolean.
 * @return `true` if any entry matches the predicate; otherwise, `false`.
 * @since 1.1.0
 */
operator fun <K, V> Map<K, V>.contains(predicate: Predicate<Map.Entry<K, V>>) = any { predicate(it) }

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
 * Iterates over each entry in the map and applies the specified block to it.
 *
 * The map itself remains unmodified, and the original map is returned to allow method chaining.
 *
 * @param K the type of keys in the map.
 * @param V the type of values in the map.
 * @param block a lambda function that is invoked for each entry in the map.
 *              Receives a `Map.Entry<K, V>` as its parameter.
 * @return the original map after applying the block to its entries.
 * @since 2.0.0
 */
inline fun <K, V> Map<K, V>.peek(block: ReceiverConsumer<Map.Entry<K, V>>) = apply {
    for (element in this@peek) block(element)
}

/**
 * Stands for `controlledEach`. You can use [continueLoop] and [breakLoop].
 *
 * Executes the given block for each entry in the map, passing both the loop context and the map entry
 * to the block. Supports custom loop control using `Break` and `Continue` exceptions for flow handling.
 *
 * @param block a lambda receiving a `LoopContext` and a map entry, which defines the processing logic
 *              for each entry in the map. The lambda can utilize `Break` to terminate the loop or
 *              `Continue` to skip to the next iteration.
 * @return the original map after applying the block to each entry.
 * @since 2.0.0
 */
inline fun <K, V> Map<K, V>.cForEach(block: ReceiverBiConsumer<LoopContext, Map.Entry<K, V>>) = apply {
    with(LoopContext()) {
        for (element in this@cForEach) {
            try {
                block(element)
            } catch (b: Break) {
                break
            } catch (c: Continue) {
                continue
            }
        }
    }
}

/**
 * Stands for `eachWithReturn`. You can use [continueLoop] and [breakLoop] to return a value.
 *
 * Iterates through the map and performs the given action on each entry.
 * The iteration can be interrupted by throwing specific exceptions with optional results.
 *
 * @param action a lambda expression to be invoked on each entry in the map. The lambda
 *               receives a single parameter, which is the current map entry.
 * @return the result of the operation of type [R], if provided as part of a `Continue` exception;
 *         otherwise, returns `null`. If the iteration is interrupted by a `Break` exception,
 *         the result provided with the `Break` is returned immediately.
 * @since 2.0.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <K, V, R> Map<K, V>.rForEach(action: ReceiverBiConsumer<LoopContext, Map.Entry<K, V>>): R? {
    with(LoopContext()) {
        for (element in this@rForEach) {
            try {
                action(element)
            } catch (b: Break) {
                return b.result as? R
            } catch (c: Continue) {
                continue
            }
        }
    }
    return null
}

/**
 * Transforms the entries of a map into a new map with keys and values mapped
 * using the provided transformation function.
 *
 * @param transform A function that takes a map entry (key-value pair) from the
 * original map and returns a pair of type K2 and V2, representing the new key
 * and value respectively.
 * @since 2.0.0
 */
inline fun <K1, V1, K2, V2> Map<K1, V1>.mapToMap(transform: Transformer<Map.Entry<K1, V1>, Pair<K2, V2>>) = entries.associate { transform(it) }
/**
 * Creates a new map by transforming the keys and values of the original map using the provided mapping functions.
 *
 * @param transformKeys A function that takes a map entry and transforms its key into a new key for the resulting map.
 * @param trasnformValues A function that takes a map entry and transforms its value into a new value for the resulting map.
 * @since 2.0.0
 */
inline fun <K1, V1, K2, V2> Map<K1, V1>.mapToMap(transformKeys: Transformer<Map.Entry<K1, V1>, K2>, trasnformValues: Transformer<Map.Entry<K1, V1>, V2>) = entries.associate { transformKeys(it) to trasnformValues(it) }
/**
 * Transforms the entries of the original map using the provided transform function and
 * creates a new map containing only the non-null results.
 *
 * @param transform A function that maps each entry of the original map to a nullable pair of a new key and value.
 *                  If the function returns a non-null pair, it is included in the resulting map.
 * @return A new map containing transformed entries that are not null.
 * @since 3.10.3
 */
inline fun <K1, V1, K2, V2> Map<K1, V1>.mapToMapNotNull(transform: Transformer<Map.Entry<K1, V1>, Pair<K2, V2>?>) = entries.mapNotNull { transform(it) }.toMap()

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
fun <K, V> Map<K, V>.firstOr(default: Supplier<Pair<K, V>>, predicate: Predicate<Map.Entry<K, V>>) : Map.Entry<K, V> {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return entries.firstOr({ default().toMapEntry() }, predicate)
}
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
fun <K, V> Map<K, V>.firstOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Map.Entry<K, V>>): Map.Entry<K, V> {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return entries.firstOrThrow(lazyException)
}

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
infix fun <K, V> Map<K, V>.onlyEntryOr(default: Supplier<Pair<K, V>>): Map.Entry<K, V> {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return entries.run { if (size == 1) first() else default().toMapEntry() }
}
/**
 * Returns the only entry in the map or throws an exception if the map contains zero or more than one entry.
 *
 * @param lazyException A supplier that provides the exception to be thrown if the map does not contain exactly one entry.
 * @return The single entry in the map if the map contains exactly one entry.
 * @throws Throwable The exception provided by the lazyException supplier if the map does not have exactly one entry.
 * @since 1.0.0
 */
infix fun <K, V> Map<K, V>.onlyEntryOrThrow(lazyException: ThrowableSupplier): Map.Entry<K, V> {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return entries.run { if (size == 1) first() else throw lazyException() }
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
    .requireOrThrow({ NoSuchElementException() }, { it.isNotEmpty() })
    .filter(predicate).run {
        if (size == 1) first()
        else throw if (size > 1) TooManyResultsException(size) else TooFewResultsException(size)
    }
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
 * Filters entries in a map based on a given predicate and returns the single matching entry.
 * If there is no matching entry or more than one entry matches, the provided default value is returned.
 *
 * @param default a supplier providing a default map entry to return if the predicate does not match exactly one entry
 * @param predicate a condition to filter the entries of the map
 * @return the single map entry matching the predicate, or the default value if none or more than one entry matches
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.onlyEntryOr(default: Supplier<Pair<K, V>>, predicate: Predicate<Map.Entry<K, V>>): Map.Entry<K, V> {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return filter(predicate).entries.run { if (size == 1) first() else default().toMapEntry() }
}
/**
 * Filters the map according to the provided predicate and ensures that it contains only one matching entry.
 * If there is exactly one entry that matches the predicate, it returns that entry.
 * Otherwise, it throws the exception supplied by the given lazy exception supplier.
 *
 * @param lazyException A supplier function that provides the exception to be thrown when the number of matching entries is not exactly one.
 * @param predicate A predicate to filter the entries in the map.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.onlyEntryOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Map.Entry<K, V>>): Map.Entry<K, V> {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return filter(predicate).entries.run { if (size == 1) first() else throw lazyException() }
}

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
    accumulator: (resultContainer: R, element: Map.Entry<K, V>) -> Unit,
    combiner: (resultContainer: R, partialContainer: R) -> Unit
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
fun <K, V> Map<K, V>.getOrThrow(key: K, lazyException: ThrowableSupplier = { NoSuchElementException("Element with key $key is not present") }): V {
    contract {
        callsInPlace(lazyException, InvocationKind.AT_MOST_ONCE)
    }
    return this[key] ?: throw lazyException()
}

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
 * Executes the given [action] if the map is not empty. If the map is empty, it returns the map itself.
 *
 * @param action A lambda function to be executed if the map is not empty. It takes the map as a receiver.
 * @return The result of the [action] if the map is not empty, or the map itself if it is empty.
 * @since 3.1.3
 */
@Suppress("UNCHECKED_CAST")
inline fun <M : Map<K, V>, K, V, R> M.ifNotEmpty(action: ReceiverTransformer<M, R>): R {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    return (if (isNotEmpty()) action(this) else this) as R
}

/**
 * Executes the given action if the nullable map is not null and not empty.
 *
 * This method checks if the receiver map is neither null nor empty and, if true,
 * invokes the specified action with the map as the input. If the map is null or empty,
 * it simply returns the map without invoking the action.
 *
 * @param action The action to be executed if the map is not null and not empty.
 *               It is a function that takes the map as input and returns a result.
 * @return The result of the action if the map is not null and not empty,
 *         or the map itself if it is null or empty.
 * @since 3.1.3
 */
@OptIn(ExperimentalExtendedContracts::class)
@Suppress("UNCHECKED_CAST")
inline fun <M : Map<K, V>?, K, V, R> M?.ifNotNullOrEmpty(action: ReceiverTransformer<M, R>): R? {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        (this@ifNotNullOrEmpty != null) implies returnsNotNull()
    }
    return (if (isNotNullOrEmpty()) action(this) else this) as R
}

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
inline operator fun <K, V> Map<K, V>.get(find: Predicate<Map.Entry<K, V>>): Map.Entry<K, V>? = entries[find]
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
inline operator fun <K, V> Map<K, V>.get(find: Predicate<Map.Entry<K, V>>, lazyException: ThrowableSupplier) = get(find) ?: throw lazyException()

/**
 * Applies the given predicate to filter the entries of the map and returns the filtered result.
 *
 * @param filter A predicate to apply on the map entries. The predicate defines the condition
 *               to filter the entries of the map.
 * @since 1.0.0
 */
inline operator fun <K, V> Map<K, V>.invoke(filter: Predicate<Map.Entry<K, V>>): Map<K, V> {
    val destination = LinkedHashMap<K, V>()
    for (element in this) {
        if (filter(element)) {
            destination[element.key] = element.value
        }
    }
    return destination
}

/**
 * Returns a new map containing only the entries of the original map that do not match
 * the specified predicate.
 *
 * @param filterNot A predicate to test each entry of the map.
 * Entries for which the predicate returns `true` will be excluded from the resulting map.
 * @since 3.5.1
 */
operator fun <K, V> Map<K, V>.minus(filterNot: Predicate<Map.Entry<K, V>>) = filterNot(filterNot)

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

/**
 * Validates that the map is not empty. If the map is empty, a validation exception is thrown.
 *
 * @param causeOf An optional supplier for a custom throwable to be thrown when the validation fails. If provided, this throwable will be the primary exception, and its cause will
 *  be set to the validation exception.
 * @param cause An optional supplier for a throwable that will act as the cause for the validation exception if no custom throwable is provided.
 * @return The map itself if it is not empty.
 * @throws ValidationFailedException if the map is empty and no custom throwable is supplied via `causeOf`.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The map is empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The map is empty.", cause?.invoke()))
    return this
}
/**
 * Validates that the map is not empty. If the map is empty, an exception is thrown
 * with the specified lazy message, optional cause, and optional cause of the exception.
 *
 * @param causeOf an optional supplier for a throwable cause that will be
 *                set as the cause of the {@code ValidationFailedException}.
 *                If this is not null, its initialized cause will be set to the
 *                {@code ValidationFailedException}.
 * @param cause an optional supplier for a throwable that will be used to
 *              provide additional context to the exception.
 * @param lazyMessage a supplier for the lazy evaluation of the exception message
 *                    in case the validation fails.
 * @return the current map instance if it passes validation.
 * @throws ValidationFailedException if the map is empty. The exception message
 *                                   and cause are populated using the provided
 *                                   suppliers.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that the map is not empty. If the map is empty, throws a ValidationFailedException.
 *
 * @param property The property being validated. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Included in the exception message if provided.
 * @param message An optional custom message to include in the exception. Defaults to "is empty".
 * @param causeOf A supplier for an alternative exception cause. If provided, it will be used as the root cause of
 *                the ValidationFailedException.
 * @param cause A supplier for an additional cause included in the exception chain. Optional and can be null.
 * @return The same map passed as the receiver if validation succeeds.
 * @throws ValidationFailedException If the map is empty, encapsulating the provided details.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the current map is not empty. If the map is empty, a `ValidationFailedException` is thrown.
 *
 * @param property the main `KProperty` associated with this validation, providing class, name, and type context; can be null
 * @param variable an optional secondary `KProperty` providing additional variable-specific context; can be null
 * @param message an optional validation failure message to include in the exception; can be null, with a default of "is empty"
 * @param causeOf an optional supplier for a custom cause of the thrown exception; can be null
 * @param cause an optional supplier for an additional cause to be included in the exception; can be null
 * @return the same non-empty map instance that was validated
 * @throws ValidationFailedException if the map is empty, providing detailed validation context and an optional cause
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the given map is not empty. Throws a `ValidationFailedException`
 * if the map is empty. The exception can include optional details such as
 * the callable, parameter name, custom message, and cause.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. Can be null.
 * @param parameterName The name of the parameter in the callable to validate. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Default is "is empty".
 * @param causeOf A supplier for the root cause throwable associated with this validation failure. Can be null.
 * @param cause A supplier for a throwable indicating why the validation failed. Can be null.
 * @return The validated map, if it is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the map is not empty. If the map is empty, throws a [ValidationFailedException].
 *
 * @param callable the [KFunction] associated with the validation, or null if not applicable
 * @param parameter the [KParameter] representing the parameter under validation, or null if not applicable
 * @param message an optional custom error message to include in the exception, defaults to "is empty" if null
 * @param causeOf an optional supplier for the primary cause of the exception, or null if not applicable
 * @param cause an optional supplier for an additional cause of the exception, or null if not applicable
 * @return the validated map if it is not empty
 * @throws ValidationFailedException if the map is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the given map is not empty. Throws a ValidationFailedException if the map is empty.
 *
 * @param callableName the name of the callable (e.g., a function or method) where the validation is performed
 * @param parameterName the name of the parameter being validated; can be null
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf a supplier for the primary cause of the validation failure; if null, a ValidationFailedException will be created instead
 * @param cause a supplier for the underlying cause of the exception; can be null
 * @return the validated map if it is not empty
 * @throws ValidationFailedException if the map is empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the map is not empty. If the map is empty, throws a `ValidationFailedException`.
 *
 * @param callableName The name of the callable (e.g., function or property) where validation failed, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter that failed validation, or null if not applicable.
 * @param message An optional error message providing additional context about the validation failure. Defaults to "is empty" if not specified.
 * @param causeOf An optional supplier for a cause `Throwable` that directly triggered this validation failure. Defaults to null.
 * @param cause An optional supplier for a secondary cause `Throwable`. Defaults to null.
 * @return The map itself if it is not empty.
 * @throws ValidationFailedException If the map is empty, with optional details about the failed callable, parameter, message, and cause(s).
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateNotEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is empty", cause?.invoke()))
    return this
}
/**
 * Validates that the map is not null or empty.
 * If the map is null or empty, a specified throwable or a default `ValidationFailedException` is thrown.
 *
 * @param causeOf An optional supplier for the throwable to be used as the cause of the exception.
 *                If provided, this supplier will determine the throwable to be thrown.
 * @param cause   An optional supplier for the inner cause of the exception, used to provide additional
 *                context about the validation failure.
 * @return The original map if it is not null and not empty.
 * @throws ValidationFailedException if the map is null or empty and no custom throwable is supplied.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The map is null or empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The map is null or empty.", cause?.invoke()))
    return this
}
/**
 * Validates that a given map is neither null nor empty. Throws a `ValidationFailedException` if the validation fails.
 *
 * @param causeOf optional supplier for the primary throwable cause, which can be used to associate a specific cause with the failure.
 * @param cause optional supplier for an additional throwable cause, providing more context about the failure.
 * @param lazyMessage a supplier for creating the message of the exception, used to describe the reason for validation failure.
 * @return the original map if the validation passes, ensuring fluent usage within processing chains.
 * @throws ValidationFailedException if the map is null or empty. The exception includes a detailed message and optionally a cause.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that the map is not null or empty. If the map is null or empty, a
 * [ValidationFailedException] is thrown with the provided details.
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName The name of the variable being validated. Used in the exception message if provided.
 * @param message An optional custom error message. Defaults to "is null or empty" if not specified.
 * @param causeOf A supplier for the cause of the validation failure. If provided, its result is used as the cause
 *                of the exception. Defaults to null.
 * @param cause An optional supplier for a throwable cause. It is used as the inner cause of the exception if `causeOf`
 *              is not provided. Defaults to null.
 * @return The validated map if it is not null or empty.
 * @throws ValidationFailedException If the map is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that a Map is not null or empty. If the Map is null or empty, a `ValidationFailedException` is thrown.
 *
 * @param property the main property being validated, providing class and type context, or null if not specified
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional detailed message to include in the exception if validation fails, or null if not provided
 * @param causeOf a supplier function to provide a specific throwable as the cause of the exception, or null if not specified
 * @param cause a supplier function to provide an additional throwable to initialize the exception's cause, or null if not specified
 * @return the original Map if validation passes
 * @throws ValidationFailedException if the Map is null or empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the map is not null or empty. If the map is null or empty, this function throws a
 * `ValidationFailedException` with an optional custom message and cause.
 *
 * @param callable The Kotlin function (`KFunction`) associated with this validation. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message to describe the validation failure. Defaults to "is null or empty" if not provided.
 * @param causeOf A supplier for a throwable that serves as the primary cause of failure. Can be null.
 * @param cause A supplier for a secondary throwable that may have contributed to the failure. Can be null.
 * @return The original map (`this`) if it is not null or empty.
 * @throws ValidationFailedException If the map is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the given map is not null or empty. If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] related to the validation, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable.
 * @param message an optional error message to be included in the exception if validation fails; defaults to "is null or empty".
 * @param causeOf an optional supplier for the throwable causing the validation failure, or null if not applicable.
 * @param cause an optional supplier for the underlying cause of the exception, or null if not applicable.
 * @return the map being validated if the validation passes.
 * @throws ValidationFailedException if the map is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the map is not null or empty. Throws a `ValidationFailedException` if the map is null or empty.
 *
 * @param callableName The name of the callable (e.g., function or method) related to the validation failure.
 * @param parameterName The name of the parameter that caused the validation failure, or null if not specified.
 * @param message An optional custom message providing additional details about the validation failure, or null if not specified.
 * @param causeOf A supplier for the root cause of the validation failure, or null if not specified.
 * @param cause A supplier for an additional cause to chain with the exception, or null if not specified.
 * @return The original map if validation passes.
 * @throws ValidationFailedException If the map is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the map is not null or empty and throws a validation exception if the condition fails.
 *
 * @param callableName The name of the function or property where validation is performed,
 *                     or null if not specified.
 * @param parameter The KParameter instance representing the parameter being validated,
 *                  or null if not applicable.
 * @param message An optional error message to include in the exception if validation fails,
 *                or null if not specified. Defaults to "is null or empty".
 * @param causeOf A supplier for the root cause of the validation failure exception, or null if not specified.
 * @param cause A supplier for the secondary cause of the validation failure exception, or null if not specified.
 * @return The original map if validation passes.
 * @throws ValidationFailedException If the map is null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNotNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNotNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNotNullOrEmpty != null)
    }
    if (isNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that the map is empty. If the map is not empty, throws a [ValidationFailedException].
 * The exception message indicates that the map is not empty. Optionally, allows for custom
 * throwable suppliers to provide specific causes for the exception.
 *
 * @param causeOf an optional supplier for a throwable to throw when validation fails.
 *                If provided, this throwable will be used instead of the default exception.
 *                The supplier can return `null`, in which case the default exception is used.
 * @param cause an optional supplier for a throwable to be used as the cause of the exception.
 *              This throwable will be passed as the cause of the validation exception.
 *              The supplier can return `null`, which results in no cause being assigned.
 * @return the map on which this method is invoked, if it is empty.
 * @throws ValidationFailedException if the map is not empty and no custom throwable supplier
 *                                   is provided via `causeOf`.
 * @throws Throwable if a throwable supplier is provided via `causeOf` and returns a throwable.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The map is not empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The map is not empty.", cause?.invoke()))
    return this
}
/**
 * Validates that a map is empty. If the map is not empty, a `ValidationFailedException` is thrown.
 *
 * @param causeOf a supplier for a custom exception to be thrown if validation fails. If `null`, a default
 *        `ValidationFailedException` is used.
 * @param cause a supplier for the underlying cause of the exception to be thrown. Can be `null`.
 * @param lazyMessage a supplier for the error message to be included in the exception if validation fails.
 * @return the original map if it is empty.
 * @throws ValidationFailedException if the map is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that the map is empty. Throws a [ValidationFailedException] if the map is not empty.
 *
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName An optional name of the variable being validated. Included in the exception message if provided.
 * @param message A custom message to include in the exception if the validation fails. Defaults to "is not empty".
 * @param causeOf A supplier for the primary exception cause, if applicable.
 * @param cause A supplier for the additional exception cause to set as the `cause` of the thrown exception. Can be null.
 * @return The original map if the validation passes.
 * @throws ValidationFailedException If the map is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the map is empty. If the map is not empty, throws a [ValidationFailedException].
 *
 * @param property the primary [KProperty] associated with the validation, used for error details, or null if not applicable
 * @param variable an optional secondary [KProperty] for additional context, or null if not applicable
 * @param message an optional custom message to include in the exception, or null for a default message
 * @param causeOf an optional supplier for a cause exception, invoked if the validation fails, or null if not applicable
 * @param cause an optional supplier for an exception to be used as the root cause, or null if not applicable
 * @return the original map if the validation passes
 * @throws ValidationFailedException if the map is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the map is empty. If the map is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callable The Kotlin function (`KFunction`) related to this validation. Can be null.
 * @param parameterName Optional name of the parameter related to the validation. Can be null.
 * @param message An optional custom validation message. If not provided, a default message "is not empty" is used.
 * @param causeOf A supplier providing a throwable that will be the primary cause of the exception. Can be null.
 * @param cause A supplier providing an additional cause for the exception. Can be null.
 * @return Returns the map itself if it is empty, allowing for method chaining.
 * @throws ValidationFailedException if the map is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the current map is empty. If the map is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callable the [KFunction] related to the validation context, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional message to include in the validation exception, or null for a default message
 * @param causeOf a supplier providing a specific exception to throw, or null to use the default behavior
 * @param cause a supplier providing the underlying cause of the exception, or null if there is no specific cause
 * @return the same map instance upon successful validation
 * @throws ValidationFailedException if the map is not empty
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the map is empty. If the map is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName The name of the parameter being validated, or `null` if not applicable.
 * @param message An optional custom message to provide additional details about the validation failure. Defaults to "is not empty" if not specified.
 * @param causeOf A supplier that provides the root cause of the exception, or `null` if no specific cause is provided.
 * @param cause A supplier for the underlying cause associated with the validation failure, or `null` if not specified.
 * @return The same map if it passes validation (i.e., is empty).
 * @throws ValidationFailedException If the map is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that a given map is empty. If the map is not empty, a `ValidationFailedException` is thrown.
 *
 * @param callableName The name of the callable (e.g., function or property) related to the validation, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message providing additional context about the validation failure. Defaults to "is not empty" if not provided.
 * @param causeOf A supplier for the primary `Throwable` cause of the validation failure, or null if no primary cause is provided.
 * @param cause A supplier for the secondary `Throwable` cause of the validation failure, or null if no secondary cause is provided.
 * @return The original map instance if it is empty.
 * @throws ValidationFailedException If the map is not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>> T.validateEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    if (isNotEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is not empty", cause?.invoke()))
    return this
}
/**
 * Validates that the map is either `null` or empty. If the map is not `null`
 * or not empty, an exception is thrown.
 *
 * @param causeOf A supplier for the cause of the exception if validation fails.
 *                If provided, this will generate a throwable to further explain the failure.
 *                Default is `null`.
 * @param cause   A supplier for an additional cause to be attached to the exception,
 *                if validation fails. Default is `null`.
 * @return The same map instance if the validation passes (i.e., the map is `null` or empty).
 * @throws ValidationFailedException If the map is not `null` or not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException("The map is not null or empty.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("The map is not null or empty.", cause?.invoke()))
    return this
}
/**
 * Validates if the map is null or empty, and throws a validation exception if the condition is not met.
 *
 * @param causeOf an optional supplier that provides a throwable to be used as the cause of the exception.
 * @param cause an optional supplier that provides an additional chained throwable to be used as the cause.
 * @param lazyMessage a supplier that generates the message for the validation exception.
 * @return the map instance if the validation passes.
 * @throws ValidationFailedException if the map is not null and not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that a map is either null or empty. If the map is not null and not empty, it throws a
 * ValidationFailedException with the provided details.
 *
 * @param property The property associated with the validation failure. Can be null if not applicable.
 * @param variableName Optional name of the variable being validated. Included in the exception message
 *                     if not null.
 * @param message Additional message to describe the validation failure. Defaults to "is not null or empty"
 *                if not specified.
 * @param causeOf Supplier for the cause of the exception. If provided, it is used to define the cause
 *                of the exception.
 * @param cause Supplier to initialize the cause of the ValidationFailedException. Can be null if not
 *              applicable.
 * @return The original map if it satisfies the validation (i.e., it is null or empty).
 * @throws ValidationFailedException if the map is not null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates if the given map is null or empty. Throws a [ValidationFailedException] if the map is not null or empty.
 *
 * @param property The property associated with the map being validated. Can be null.
 * @param variable The variable associated with the map being validated. Can be null.
 * @param message An optional custom message for the validation failure.
 * @param causeOf A supplier for the root cause exception, if any. Can be null.
 * @param cause An additional supplier for the cause of the validation failure, if any. Can be null.
 * @return The map itself if the validation passes.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates if the given map is null or empty. If the map is not null and not empty, a
 * `ValidationFailedException` is thrown. The exception can include optional details
 * such as the callable method, parameter name, error message, or a cause.
 *
 * @param callable An optional Kotlin function (`KFunction`) that the validation is associated with.
 *                 This is used in the construction of the exception. Can be null.
 * @param parameterName An optional name of the parameter that caused the validation failure.
 *                      This is used in the exception message. Can be null.
 * @param message An optional message providing details about the validation failure.
 *                Defaults to "is not null or empty" if not provided. Can be null.
 * @param causeOf An optional supplier for a pre-constructed throwable to be used as the primary
 *                cause of the exception. If provided, this is used as the exception thrown. Can be null.
 * @param cause An optional supplier for a throwable to include as the cause of the validation
 *              failure. This is appended as the cause to the `ValidationFailedException`
 *              if `causeOf` is not supplied. Can be null.
 * @return The original map (`T`) if no exception is thrown, allowing for chaining.
 * @throws ValidationFailedException If the map is not null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that a map is null or empty. If the map is not null and not empty, this method throws a
 * [ValidationFailedException] with the provided details.
 *
 * @param callable The [KFunction] related to the validation failure, or null if not applicable.
 * @param parameter The [KParameter] representing the parameter involved in the validation failure, or null if not applicable.
 * @param message An optional message for the exception, describing the validation failure. Defaults to "is not null or empty" if null.
 * @param causeOf An optional supplier for the exception to be thrown. If supplied, this will wrap the generated exception.
 * @param cause An optional supplier for the cause of the exception, providing additional context about the failure.
 * @return The validated map if it is null or empty.
 * @throws ValidationFailedException If the map is not null and not empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates if the map is null or empty and throws a `ValidationFailedException` if it is not.
 *
 * This method is used to enforce that a map must either be null or empty in specific contexts.
 * If the validation fails, an exception is thrown with detailed information about the callable, parameter,
 * optional custom message, and underlying cause, if provided.
 *
 * @param callableName The name of the callable (e.g., function or method) being validated.
 * @param parameterName An optional name of the parameter associated with this validation.
 * @param message An optional custom message providing additional details for the validation failure.
 * @param causeOf An optional supplier that provides the cause of the exception to be thrown. If null, a default exception is thrown.
 * @param cause An optional supplier that provides the underlying cause of the exception, adding more context.
 * @return The validated map `T` itself if it passes the validation.
 * @throws ValidationFailedException If the map is neither null nor empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is not null or empty", cause?.invoke()))
    return this
}
/**
 * Validates that a map is null or empty.
 *
 * If the map is neither null nor empty, this method throws a `ValidationFailedException`.
 *
 * @param callableName The name of the callable (e.g., function or property) the validation is associated with, or null if unspecified.
 * @param parameter The parameter being validated, represented as a `KParameter`, or null if not applicable.
 * @param message An optional error message to provide context about the validation failure.
 * @param causeOf A supplier that provides the throwable to be thrown as the cause of the validation failure, or null if no such cause exists.
 * @param cause A supplier that provides an additional throwable cause if applicable, or null if absent.
 * @return The same map (`this`) that was validated, if no exception is thrown.
 * @throws ValidationFailedException If the map is not null or empty.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T : Map<*, *>?> T.validateNullOrEmpty(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        (this@validateNullOrEmpty != null) implies returnsNotNull()
        returnsNotNull() implies (this@validateNullOrEmpty != null)
    }
    if (isNotNullOrEmpty()) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is not null or empty", cause?.invoke()))
    return this
}