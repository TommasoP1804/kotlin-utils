/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.maps

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.reflect.KProperty1
import kotlin.collections.filter as kFilter

/**
 * A specialized wrapper for a map where keys correspond to Kotlin property references (`KProperty1`)
 * of a specific type `T` and values represent the associated property values.
 *
 * This class provides additional utility methods for working with the underlying map,
 * such as accessing values by property names, converting keys to their string representation,
 * and removing entries by key names.
 *
 * @param T The type of the object whose properties are used as keys in the map.
 * Must be a non-abstract class.
 * @constructor Creates an instance of `PropertiesMap` with the specified property-to-value map.
 * @property map The underlying map of property references to values.
 * @since 3.10.0
 * @author Tommaso Pastorelli
 */
@Suppress("UNCHECKED_CAST", "unused")
class PropertiesMap<T : Any>(private val map: Map<KProperty1<out T, *>, Any?>) : Map<KProperty1<out T, *>, Any?> by map {
    /**
     * A read-only property that returns a new `PropertiesMap` containing only entries from the original map
     * whose values are not null.
     *
     * Filters the underlying map by excluding all key-value pairs where the value is `null`.
     * The resulting map retains all non-null values and their corresponding keys.
     * @since 3.10.0
     */
    val withoutNulls get() = PropertiesMap(map.filterValues { it != null })

    /**
     * Retrieves a value of type R associated with the given name from the map.
     *
     * @param key The name used to locate the corresponding value in the map.
     * @return The value associated with the given name if found; otherwise, null.
     * @since 3.10.0
     */
    @JvmName("getGeneric")
    operator fun <R> get(key: String): R? = map.find { it.key.name == key } as R?
    /**
     * Retrieves the value associated with the given key from the map.
     *
     * @param key The name of the key to search for in the map.
     * @return The value corresponding to the specified key, or null if the key is not found.
     * @since 3.10.0
     */
    operator fun get(key: String) = map.find { it.key.name == key }?.value

    /**
     * Checks whether the specified property key exists within the map.
     *
     * @param key The property key to check for presence in the map.
     * @return `true` if the map contains the specified key, otherwise `false`.
     * @since 3.10.0
     */
    operator fun contains(key: String) = map.keys.find { it.name == key }.isNotNull()
    /**
     * Checks if the underlying map contains no key-value mappings.
     *
     * @return `true` if the map is empty, `false` otherwise.
     * @since 3.10.0
     */
    override fun isEmpty() = map.isEmpty()

    /**
     * Checks if the underlying map contains a key with the specified name.
     *
     * @param key The name of the key to look for in the map.
     * @return `true` if the map contains a key with the specified name; otherwise, `false`.
     * @since 3.10.0
     */
    fun containsKey(key: String) = map.keys.find { it.name == key }.isNotNull()

    /**
     * Checks if the specified value is present in the underlying map.
     *
     * @param value The value to check for existence in the map.
     * @return `true` if the value exists in the map, `false` otherwise.
     * @since 3.10.0
     */
    override fun containsValue(value: Any?) = map.containsValue(value)

    /**
     * Converts the properties of the given map to a `DataMap` by transforming the keys using the specified `keysTransform` function.
     *
     * @param keysTransform A transformer function that maps each `Map.Entry` of a property and its value to a `String` key.
     *                      By default, it uses the property name of the key from the entry.
     * @return A `DataMap` where the keys are transformed based on the provided `keysTransform` function.
     * @since 3.10.0
     */
    fun toDataMap(keysTransform: Transformer<Map.Entry<KProperty1<out T, *>, Any?>, String> = { it.key.name }): DataMap = map.mapKeys(keysTransform)

    /**
     * Removes a key-value pair from the map based on the specified key's name and returns a new `PropertiesMap` instance.
     * If the key with the given name is not found, the existing map is returned unchanged.
     *
     * @param key The name of the key to be removed from the map.
     * @return A new `PropertiesMap` instance with the specified key removed, or the existing map if the key is not found.
     * @since 3.10.0
     */
    operator fun minus(key: String): PropertiesMap<T> = PropertiesMap(map.keys.find { it.name == key }?.let { map - it } ?: map)
    /**
     * Subtracts the specified property key from the current properties map
     * and returns a new `PropertiesMap` instance with the updated map.
     *
     * @param key The property key to be subtracted from the map.
     * @return A new `PropertiesMap` instance with the specified key removed.
     * @since 3.10.0
     */
    operator fun minus(key: KProperty1<out T, *>): PropertiesMap<T> = PropertiesMap(map - key)

    /**
     * Creates a new `PropertiesMap` instance that includes only the specified properties.
     *
     * Filters the underlying map to include only the entries associated with the provided property references.
     *
     * @param properties The property references to retain in the resulting `PropertiesMap`.
     *                   Each property reference is of type `KProperty1<out T, *>`.
     *                   Only keys matching these properties will be included in the output map.
     * @since 3.10.0
     */
    fun withOnly(vararg properties: KProperty1<out T, *>) = PropertiesMap(map.filterKeys { it in properties })
    /**
     * Filters the current map to include only the specified properties.
     *
     * This function creates a new `PropertiesMap` instance containing only the key-value
     * pairs where the keys are included in the provided iterable of properties.
     *
     * @param properties An iterable collection of property references (`KProperty1`)
     *                   that determine which key-value pairs to include in the result.
     * @return A `PropertiesMap` instance containing only the specified properties.
     * @since 3.10.0
     */
    infix fun withOnly(properties: Iterable<KProperty1<out T, *>>) = PropertiesMap(map.filterKeys { it in properties })
    /**
     * Creates a new `PropertiesMap` instance containing only the specified properties.
     *
     * Filters the underlying map to include only those entries whose keys match the given property names.
     *
     * @param properties The names of the properties to include in the resulting `PropertiesMap`.
     *                   Only entries with keys matching these names will be retained.
     * @return A new `PropertiesMap` containing the filtered entries.
     * @since 3.10.0
     */
    fun withOnly(vararg properties: String) = PropertiesMap(map.filterKeys { it.name in properties })
    /**
     * Filters the current `PropertiesMap` to retain only the entries whose keys match the specified property names.
     *
     * @param properties An iterable of property names to retain within the resulting `PropertiesMap`.
     *                   Only entries with keys matching these names will be included in the final map.
     * @return A new `PropertiesMap` instance containing only the filtered entries.
     * @since 3.10.0
     */
    @JvmName("withOnlyItrableOfString")
    infix fun withOnly(properties: Iterable<String>) = PropertiesMap(map.filterKeys { it.name in properties })

    /**
     * Creates a new `PropertiesMap` instance with an additional key-value pair.
     *
     * This function adds the specified property and its corresponding value to the map,
     * returning a new `PropertiesMap` instance with the updated mapping.
     * The original map remains unmodified.
     *
     * @param property The property reference (`KProperty1`) to be used as the key in the map.
     * @param value The value to be associated with the specified property key.
     * @return A new `PropertiesMap` instance with the additional key-value pair.
     * @since 3.10.0
     */
    fun with(property: KProperty1<out T, *>, value: Any?) = PropertiesMap(map + (property to value))
    /**
     * Updates the properties map by setting a value for the specified property.
     *
     * This method searches for a property in the existing map by matching its name.
     * If the property is found, its value is replaced with the provided value.
     * If the property is not found, a `PropertyNotFoundException` is thrown.
     *
     * @param property The name of the property to update in the map.
     * @param value The new value to associate with the specified property.
     * @return A new `PropertiesMap` instance with the updated key-value pair.
     * @throws PropertyNotFoundException if the property with the specified name does not exist in the map.
     * @since 3.10.0
     */
    fun with(property: String, value: Any?) = PropertiesMap(map + ((map.find { it.key.name == property }?.key ?: throw PropertyNotFoundException(property)) to value))
    /**
     * Creates a new `PropertiesMap` instance by adding or replacing an entry in the current map.
     *
     * This function takes a property-value pair as input, where the key is a property reference
     * (`KProperty1`) and the value is the associated value. The property reference is used as
     * the key in the map, and the new value overrides any existing value associated with the key.
     *
     * @param property A pair consisting of a property reference (`KProperty1<out T, *>`) as the key
     *                 and the associated value of type `Any?`. The property reference determines
     *                 the key under which the value will be stored in the map.
     * @since 3.10.0
     */
    infix fun with(property: Pair<KProperty1<out T, *>, Any?>) = PropertiesMap(map + (property.first to property.second))
    /**
     * Creates a new `PropertiesMap` instance with an updated key-value pair.
     *
     * This function searches the current map for a key matching the provided property name.
     * If a match is found, the corresponding key is updated with the new value.
     * If no matching key can be found, a `PropertyNotFoundException` is thrown.
     *
     * @param property A pair consisting of a property name as a `String` (key)
     *                 and the associated value to be updated or added to the map.
     * @throws PropertyNotFoundException If the key specified in the property pair is not found in the map.
     * @return A new `PropertiesMap` instance containing the updated key-value pair.
     * @since 3.10.0
     */
    @JvmName("withPairOfStringNullableAny")
    infix fun with(property: Pair<String, Any?>) = PropertiesMap(map + ((map.find { it.key.name == property.first }?.key ?: throw PropertyNotFoundException(property.first)) to property.second))

    /**
     * Determines whether the specified object is equal to this instance.
     *
     * @param other The object to compare with the current instance.
     * @return `true` if the specified object is equal to this instance; otherwise, `false`.
     * @since 3.10.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PropertiesMap<*>

        return map == other.map
    }

    /**
     * Returns the hash code value for this `PropertiesMap` instance.
     *
     * The hash code is computed based on the underlying map, ensuring that
     * the hash code reflects the contents of the map.
     *
     * @return The hash code value of the underlying map.
     * @since 3.10.0
     */
    override fun hashCode(): Int {
        return map.hashCode()
    }

    /**
     * Converts the underlying data structure into a string representation using the provided transformer
     * for transforming keys during the conversion process.
     *
     * @param keysTransform A transformer function that takes an entry containing a property and its value
     * and converts it into a string representation for use in the final output.
     * @since 3.10.0
     */
    fun toString(keysTransform: Transformer<Map.Entry<KProperty1<out T, *>, Any?>, String>) = toDataMap(keysTransform).toString()
    /**
     * Converts the object into its string representation.
     *
     * This implementation transforms the object's data into a map where the keys
     * are derived from the `name` property of each key. The resulting map is then
     * converted to its string representation.
     *
     * @return A string representation of the object's data.
     * @since 3.10.0
     */
    override fun toString() = toDataMap { it.key.name }.toString()

    /**
     * Filters the entries of a properties map based on the given predicate.
     *
     * @param predicate A predicate used to test each entry of the properties map.
     *                  Only entries that satisfy the predicate will be included
     *                  in the resulting map.
     * @return A new PropertiesMap containing only the entries that match
     *         the given predicate.
     * @since 3.10.0
     */
    fun filter(predicate: Predicate<Map.Entry<KProperty1<out T, *>, Any?>>) = PropertiesMap(kFilter(predicate))
    /**
     * Filters the entries of the current collection based on the provided predicate.
     *
     * @param predicate A condition applied to each entry of type `Map.Entry<KProperty1<out T, *>, Any?>`.
     *                  Only entries that satisfy this condition will be included in the filtered result.
     * @since 3.10.0
     */
    operator fun invoke(predicate: Predicate<Map.Entry<KProperty1<out T, *>, Any?>>) = filter(predicate)
}