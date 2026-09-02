/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */


@file:JvmName("NonEmptyMapsKt")
@file:Since("5.2.0")
@file:MustUseReturnValues
@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.maps

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.collections.*
import dev.tommasop1804.kutils.exceptions.*
import tools.jackson.databind.ValueDeserializer

/**
 * Represents a map that guarantees to have at least one key-value pair.
 * Delegates its implementation to the standard [Map] interface, while ensuring
 * non-emptiness through validation during initialization.
 *
 * @param K the type of keys maintained by this map
 * @param V the type of mapped values
 * @param elements the underlying map representation to be validated for non-emptiness
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
open class NonEmptyMap<K, out V>(@PublishedApi internal val elements: Map<K, V>) : Map<K, V> by elements {

    /**
     * Constructs a new instance of the `NonEmptyMap` class from a variable number of key-value pairs.
     * Ensures that the resulting map is not empty.
     *
     * @param pairs Key-value pairs to initialize the map with. Each pair represents an association between a key of type `K`
     *              and a value of type `V`.
     * @throws TooFewElementsException If no pairs are provided, resulting in an empty map.
     * @since 5.2.0
     */
    constructor(vararg pairs: Pair<K, V>) : this(mapOf(*pairs))

    init { elements.isEmpty() && throw TooFewElementsException("Elements are empty.") }

    /**
     * Provides access to the keys of the current `NonEmptyMap` as a `NonEmptySet`.
     *
     * This property guarantees that the returned set of keys will always have at least one element,
     * given the non-empty nature of the containing `NonEmptyMap`. It delegates the underlying elements to
     * the keys of the `elements` map, ensuring non-emptiness is preserved.
     *
     * @return A `NonEmptySet` containing all the keys from the `NonEmptyMap`.
     * @since 5.2.0
     */
    open val nonEmptyKeys get() = NonEmptySet(elements.keys)
    /**
     * Retrieves all values from the underlying map as a `NonEmptyList`.
     *
     * This property represents the collection of values from the `NonEmptyMap`,
     * ensuring that the resulting list is not empty. The values are obtained
     * in the order they appear in the map.
     *
     * @return A `NonEmptyList` containing all the values from the map.
     * @since 5.2.0
     */
    open val nonEmptyValues get() = NonEmptyList(elements.values.toList())
    /**
     * Provides a `NonEmptySet` containing all key-value pairs (entries) present in the underlying map.
     *
     * This property ensures that the set of entries is non-empty, leveraging the invariant
     * that the parent `NonEmptyMap` always contains at least one key-value pair.
     * @since 5.2.0
     */
    open val nonEmptyEntries get() = NonEmptySet(elements.entries)

    companion object {
        /**
         * Constructs a NonEmptyMap from the provided pairs.
         *
         * This operator function takes a variable number of key-value pairs and creates a
         * NonEmptyMap containing those entries. Useful for creating a NonEmptyMap with an
         * immutable set of key-value pairs.
         *
         * @param pairs A variable number of pairs representing the key-value entries for the map.
         *
         * @return A NonEmptyMap containing the specified key-value pairs.
         * @since 5.2.0
         */
        operator fun <K, V> of(vararg pairs: Pair<K, V>) = NonEmptyMap(mapOf(*pairs))

        /**
         * Converts the current [Map] into a [NonEmptyMap].
         *
         * This function ensures that the resulting [NonEmptyMap] is not empty.
         * If the original [Map] is empty, a runtime exception will be thrown.
         *
         * @receiver The map to be converted into a [NonEmptyMap].
         * @return A [NonEmptyMap] containing all elements of the original map.
         * @throws TooFewElementsException if the original map is empty.
         * @since 5.2.0
         */
        fun <K, V> Map<K, V>.toNonEmptyMap() = NonEmptyMap(this)
        /**
         * Converts the current map to a [NonEmptyMap] if it is not empty, or returns `null` if the map is empty
         * or an exception occurs during the conversion process.
         *
         * @receiver The map to be converted.
         * @return A [NonEmptyMap] containing the entries of the current map if it is non-empty, or `null` otherwise.
         * @since 5.2.0
         */
        fun <K, V> Map<K, V>.toNonEmptyMapOrNull() = tryOrNull { NonEmptyMap(this) }
        /**
         * Converts the current map into a `NonEmptyMap`. If the map is empty or an exception occurs during the
         * conversion, the provided default value supplier is invoked to return a fallback `NonEmptyMap`.
         *
         * @receiver The map to be converted.
         * @param default A supplier function that provides a default `NonEmptyMap` to be used if the map is empty
         * or the conversion fails.
         * @return A `NonEmptyMap` containing the elements of the original map, or the default `NonEmptyMap` if
         * the original map is empty or an exception occurs.
         * @since 5.2.0
         */
        fun <K, V> Map<K, V>.toNonEmptyMapOrDefault(default: Supplier<NonEmptyMap<K, V>>) =
            tryOr({ default() }) { NonEmptyMap(this) }
    }

    /**
     * Compares this object with the specified object for equality.
     *
     * @param other The object to be compared with this one for equality.
     * @return `true` if the specified object is equal to this object, `false` otherwise.
     * @since 5.2.0
     */
    override fun equals(other: Any?) = elements == other
    /**
     * Computes the hash code for this `NonEmptyMap` instance.
     *
     * This method returns the hash code of the internal `elements` property,
     * which represents the underlying map of the `NonEmptyMap`.
     *
     * @return The hash code of the `NonEmptyMap`, derived from its elements.
     * @since 5.2.0
     */
    override fun hashCode() = elements.hashCode()
    /**
     * Returns a string representation of the object.
     *
     * This method overrides the default implementation of `toString`
     * and provides a string representation derived from the `elements` property.
     *
     * @return A string that represents the current object.
     * @since 5.2.0
     */
    override fun toString() = elements.toString()

    /**
     * Always returns `false`, indicating that the map is not empty.
     *
     * This method is overridden to reflect the contract of `NonEmptyMap`,
     * which guarantees that it always contains at least one element.
     *
     * @return `false` to signify that the map is non-empty.
     * @since 5.2.0
     */
    override fun isEmpty() = false

    /**
     * Transforms the values of the entries in the map using the provided [transform] function
     * and returns a new non-empty map with the transformed values.
     *
     * @param transform A function that takes a map entry as input and returns a transformed value.
     * @return A new instance of [NonEmptyMap] where the values are transformed using the provided function.
     * @since 5.2.0
     */
    inline fun <R> mapValues(transform: Transformer<Map.Entry<K, V>, R>) =
        NonEmptyMap(elements.mapValues(transform))

    /**
     * Returns a new [NonEmptyMap] with its keys transformed by the given [transform] function.
     *
     * @param transform A function that applies a transformation to each key-value pair,
     * producing a new key for the resulting map.
     * @return A [NonEmptyMap] containing the transformed keys and their associated values.
     * @since 5.2.0
     */
    inline fun <R> mapKeys(transform: Transformer<Map.Entry<K, V>, R>) =
        NonEmptyMap(elements.mapKeys(transform))

    /**
     * Returns a new `NonEmptyMap` instance by adding the given key-value pair to the current map.
     *
     * @param pair The key-value pair to be added to the map.
     * @return A new `NonEmptyMap` containing the original entries plus the specified key-value pair.
     * @since 5.2.0
     */
    operator fun plus(pair: Pair<@UnsafeVariance K, @UnsafeVariance V>) =
        NonEmptyMap(elements + pair)
    /**
     * Returns a new instance of [NonEmptyMap] by adding all entries from the given [other] map
     * to the current map. Existing keys in the original map will be overwritten by the corresponding
     * keys from [other], if any.
     *
     * @param other A map containing key-value pairs to be added to the current map.
     * @return A new [NonEmptyMap] containing all entries from the original map and the additional entries
     * in [other].
     * @since 5.2.0
     */
    operator fun plus(other: Map<@UnsafeVariance K, @UnsafeVariance V>) =
        NonEmptyMap(elements + other)
}

/**
 * Creates a new instance of a [NonEmptyMap] containing at least one key-value pair.
 *
 * @param head The first key-value pair to include in the map. This parameter is required to ensure
 * that the resulting map is not empty.
 * @param rest Additional key-value pairs to be added to the map. This parameter is optional and can
 * include zero or more pairs.
 * @return A [NonEmptyMap] instance containing the provided key-value pairs.
 * @since 5.2.0
 */
fun <K, V> nonEmptyMapOf(head: Pair<K, V>, vararg rest: Pair<K, V>): NonEmptyMap<K, V> =
    NonEmptyMap(head.asSingleMap() + rest)

/**
 * A mutable, non-empty map implementation that guarantees at least one key-value pair at all times.
 *
 * This class wraps a backing `MMap` and enforces non-emptiness via validation during initialization
 * and operations that could potentially result in an empty map.
 *
 * @param K the type of keys maintained by this map
 * @param V the type of mapped values
 * @property mElements the underlying mutable map used for storage, validated to ensure non-emptiness
 * @constructor Initializes a `NonEmptyMMap` using the provided elements validated as non-empty
 * @since 5.2.0
 * @author Tommaso Pastorelli
 */
class NonEmptyMMap<K, V>(private val mElements: MMap<K, V>) : MMap<K, V> by mElements, NonEmptyMap<K, V>(mElements) {

    /**
     * A removal guard to enforce constraints that prevent the `NonEmptyMMap` from becoming empty.
     *
     * This instance of `RemovalGuard` ensures that any operation reducing the number of elements
     * in the `NonEmptyMMap` does not result in an empty map. If such an operation is attempted,
     * an exception is thrown with the provided error message, indicating the violation.
     *
     * @since 5.2.0
     */
    private val guard = RemovalGuard(
        { mElements.size },
        "Operation would leave NonEmptyMMap empty",
    )

    /**
     * Represents the set of all key-value pairs (entries) in this non-empty map.
     * The entries are part of the internal state maintained by the `elements` field.
     * This property ensures that only non-empty maps can provide access to their entries.
     * @since 5.2.0
     */
    override val entries: MSet<MMapEntry<K, V>> get() = GuardedMSet(mElements.entries, guard)
    /**
     * Provides access to the set of keys held within the collection.
     * This property returns the keys as a mutable set (`MSet`).
     * The keys correspond to the elements stored in the underlying data structure.
     * @since 5.2.0
     */
    override val keys: MSet<K> get() = GuardedMSet(mElements.keys, guard)
    /**
     * Provides an override accessor to retrieve a collection of values contained within the current instance.
     *
     * This property returns the values stored in the internal `elements` map as a collection.
     * It represents the collection of all values that are mapped within the `elements` structure.
     * @since 5.2.0
     */
    override val values: MCollection<V> get() = GuardedMCollection(mElements.values, guard)
    /**
     * Represents the number of elements contained in the collection.
     * Provides the size of the collection based on the underlying elements.
     * @since 5.2.0
     */
    override val size: Int get() = mElements.size

    /**
     * Provides a non-empty set of all the keys present in the map.
     * This property guarantees that the returned set is not empty, aligning with the non-empty invariant
     * of the containing class.
     * @since 5.2.0
     */
    override val nonEmptyKeys get() = NonEmptySet(mElements.keys)
    /**
     * A read-only property that retrieves all the non-empty values present in the `NonEmptyMMap` instance.
     *
     * This property ensures that the resulting collection of values is always a `NonEmptyList`,
     * guaranteeing that it contains at least one value. It is derived from the `values` property
     * of the `elements` map, which holds the internal key-value pairs of the `NonEmptyMMap`.
     *
     * @return A `NonEmptyList` that contains all the non-empty values from the map.
     * @since 5.2.0
     */
    override val nonEmptyValues get() = NonEmptyList(mElements.values.toList())
    /**
     * Represents a collection of non-empty map entries (`MMapEntry<K, V>`) within the `NonEmptyMMap`.
     * This property guarantees that the set of entries is not empty.
     *
     * The `nonEmptyEntries` property provides access to the entries of the map in a non-empty format
     * encapsulated within a `NonEmptySet`. It ensures that operations performed on the entries
     * are safe with the assumption that at least one entry exists.
     * @since 5.2.0
     */
    override val nonEmptyEntries: NonEmptySet<MMapEntry<K, V>> get() = NonEmptySet(mElements.entries)
    /**
     * Constructs a new instance of `NonEmptyMMap` with the provided key-value pairs.
     *
     * Ensures that the resulting map is non-empty and initializes it using the given pairs.
     *
     * @param pairs A variable number of key-value pairs to populate the `NonEmptyMMap`.
     * @throws TooFewElementsException If the provided pairs result in an empty map.
     * @since 5.2.0
     */
    constructor(vararg pairs: Pair<K, V>) : this(mMapOf(*pairs))

    init { mElements.isEmpty() && throw TooFewElementsException("Elements are empty.") }

    companion object {
        /**
         * Constructs a `NonEmptyMMap` from the provided vararg pairs.
         *
         * @param K The type of keys in the map.
         * @param V The type of values in the map.
         * @param pairs A vararg array of key-value pairs to initialize the map.
         * @return A `NonEmptyMMap` containing the provided key-value pairs.
         * @since 5.2.0
         */
        operator fun <K, V> of(vararg pairs: Pair<K, V>) = NonEmptyMMap(mMapOf(*pairs))

        /**
         * Converts the current [Map] instance to a [NonEmptyMMap], which is a specialized map
         * implementation that ensures it always contains at least one key-value pair.
         *
         * This method assumes the input map is non-empty. If the input map is empty, the behavior
         * of this function is undefined.
         *
         * @receiver The original [Map] instance to be converted.
         * @return A [NonEmptyMMap] containing all entries from the original map.
         * @throws TooFewElementsException if the input map is empty.
         * @since 5.2.0
         */
        fun <K, V> Map<K, V>.toNonEmptyMMap() = NonEmptyMMap(toMMap())
        /**
         * Converts the current [Map] instance to a [NonEmptyMMap] or returns `null` if the map is empty
         * or an exception occurs during conversion.
         *
         * This function attempts to create a [NonEmptyMMap] instance from the current map by first converting it
         * to a mutable map using [toMMap], and then wrapping it in a [NonEmptyMMap]. If the map is empty or an
         * exception is thrown during this process, the function safely returns `null`.
         *
         * @receiver The original map to be converted.
         * @return A [NonEmptyMMap] if the conversion succeeds and the map is non-empty, or `null` otherwise.
         * @throws UnsupportedOperationException if the conversion to [NonEmptyMMap] fails under specialized conditions.
         * @throws TooFewElementsException if the map does not satisfy the requirements for non-empty constraints.
         * @since 5.2.0
         */
        fun <K, V> Map<K, V>.toNonEmptyMMapOrNull() = tryOrNull { NonEmptyMMap(toMMap()) }
        /**
         * Converts the current map to a `NonEmptyMMap`. If the map is empty, the specified default supplier is used
         * to provide a fallback `NonEmptyMMap`.
         *
         * @param default A supplier function that provides a default `NonEmptyMMap` instance if the current map is empty.
         * @return A `NonEmptyMMap` containing the entries of the current map if it is non-empty, or the result of the
         * default supplier if the current map is empty.
         * @throws ParametersInConflictException If conflicts are detected in the fallback mechanism during the
         * exception handling process.
         * @since 5.2.0
         */
        fun <K, V> Map<K, V>.toNonEmptyMMapOrDefault(default: Supplier<NonEmptyMMap<K, V>>) =
            tryOr({ default() }) { NonEmptyMMap(toMMap()) }
    }

    /**
     * Compares this object to the specified object for equality.
     *
     * @param other The object to compare with this instance.
     * @return `true` if the specified object is equal to this instance, otherwise `false`.
     * @since 5.2.0
     */
    override fun equals(other: Any?) = mElements == other
    /**
     * Generates a hash code for this `NonEmptyMMap` instance.
     *
     * The hash code is computed based on the `elements` field of the collection, ensuring consistency
     * with the `equals` implementation. This allows for proper functionality in hash-based collections.
     *
     * @return The hash code value of the `elements` field.
     * @since 5.2.0
     */
    override fun hashCode() = mElements.hashCode()
    /**
     * Returns a string representation of the object.
     *
     * This method overrides the default `toString` implementation to provide
     * a string representation of the `elements` property.
     *
     * @return A string representation of the `elements` property.
     * @since 5.2.0
     */
    override fun toString() = mElements.toString()

    /**
     * Determines whether the collection is empty.
     *
     * This implementation always returns `false` to indicate that the collection
     * is guaranteed to contain elements, and cannot be empty by design.
     *
     * @return `false` always, as this collection type does not allow an empty state.
     * @since 5.2.0
     */
    override fun isEmpty() = false

    /**
     * Removes the element associated with the specified key from the collection.
     *
     * @param key The key of the element to be removed.
     * @return The value associated with the specified key if it exists, or null if the key was not found.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun remove(key: K): V? {
        if (!mElements.containsKey(key)) return null
        guard.check(1)
        return mElements.remove(key)
    }
    /**
     * Removes the specified key-value pair from the collection if it exists.
     *
     * @param key The key associated with the element to be removed.
     * @param value The value associated with the specified key to be removed.
     * @return `true` if the key-value pair was successfully removed, `false` otherwise.
     * @since 5.2.0
     */
    @IgnorableReturnValue
    override fun remove(key: K, value: V): Boolean {
        if (mElements[key] != value || !mElements.containsKey(key)) return false
        guard.check(1)
        return mElements.remove(key, value)
    }

    /**
     * Throws an `UnsupportedOperationException` when invoked, as clearing a `NonEmptyMMap`
     * is not supported. This collection type is designed to always maintain a non-empty state,
     * and thus cannot be cleared to an empty state.
     *
     * @throws UnsupportedOperationException always, indicating that the clear operation is not allowed.
     * @since 5.2.0
     */
    override fun clear() = throw UnsupportedOperationException("Cannot clear a NonEmptyMMap")

    /**
     * Checks if the specified key is present in the map.
     *
     * @param key The key to be checked for presence in the map.
     * @return `true` if the map contains the specified key, `false` otherwise.
     * @since 5.2.0
     */
    override fun containsKey(key: K) = mElements.containsKey(key)
    /**
     * Checks if the specified value is present in the collection.
     *
     * @param value the value to be checked for presence within the collection.
     * @return `true` if the value is found in the collection, `false` otherwise.
     * @since 5.2.0
     */
    override fun containsValue(value: V) = mElements.containsValue(value)

    /**
     * Retrieves the value associated with the specified key from the collection.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the specified key, or `null` if the key is not present in the collection.
     * @since 5.2.0
     */
    override fun get(key: K) = mElements[key]
}

/**
 * Creates a NonEmptyMMap with at least one key-value pair.
 *
 * @param head the first key-value pair that guarantees the map is non-empty
 * @param rest additional key-value pairs to be included in the map
 * @return a NonEmptyMMap containing the provided key-value pairs
 * @since 5.2.0
 */
fun <K, V> nonEmptyMMapOf(head: Pair<K, V>, vararg rest: Pair<K, V>): NonEmptyMMap<K, V> =
    NonEmptyMMap((head.asSingleMap() + rest).toMMap())

internal class NonEmptyMapDeserializer<T : Any>(
    private val targetType: Class<T>,
    private val wrap: (Map<Any?, Any?>) -> T,
    private val keyType: tools.jackson.databind.JavaType? = null,
    private val valueType: tools.jackson.databind.JavaType? = null,
) : ValueDeserializer<T>() {

    override fun createContextual(ctxt: tools.jackson.databind.DeserializationContext, property: tools.jackson.databind.BeanProperty?): ValueDeserializer<*> {
        val wrapper = property?.type ?: ctxt.contextualType ?: return this
        return NonEmptyMapDeserializer(
            targetType, wrap,
            wrapper.containedTypeOrUnknown(0),
            wrapper.containedTypeOrUnknown(1),
        )
    }

    override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: tools.jackson.databind.DeserializationContext): T {
        val k = keyType ?: ctxt.constructType(Any::class.java)
        val v = valueType ?: ctxt.constructType(Any::class.java)
        val buffer: Map<Any?, Any?> =
            ctxt.readValue(p, ctxt.typeFactory.constructMapType(LinkedHashMap::class.java, k, v))

        return try {
            wrap(buffer)
        } catch (e: TooFewElementsException) {
            ctxt.reportInputMismatch<T>(
                targetType,
                "Cannot deserialize %s from an empty object: at least one entry is required",
                targetType.simpleName,
            )
        }
    }
}

internal class NonEmptyMapOldDeserializer<T : Any>(
    private val targetType: Class<T>,
    private val wrap: (Map<Any?, Any?>) -> T,
    private val keyType: JavaType? = null,
    private val valueType: JavaType? = null,
) : JsonDeserializer<T>(), ContextualDeserializer {

    override fun createContextual(ctxt: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
        val wrapper = property?.type ?: ctxt.contextualType
        return NonEmptyMapOldDeserializer(
            targetType, wrap,
            wrapper?.containedTypeOrUnknown(0) ?: ctxt.constructType(Any::class.java),
            wrapper?.containedTypeOrUnknown(1) ?: ctxt.constructType(Any::class.java),
        )
    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        val k = keyType ?: ctxt.constructType(Any::class.java)
        val v = valueType ?: ctxt.constructType(Any::class.java)
        val buffer: Map<Any?, Any?> =
            ctxt.readValue(p, ctxt.typeFactory.constructMapType(LinkedHashMap::class.java, k, v))

        return try {
            wrap(buffer)
        } catch (e: TooFewElementsException) {
            throw MismatchedInputException.from(
                p, targetType,
                "Cannot deserialize ${targetType.simpleName} from an empty object: at least one entry is required",
            ).also { it.initCause(e) }
        }
    }
}