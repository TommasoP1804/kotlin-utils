/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.delegates

import dev.tommasop1804.kutils.*
import kotlin.reflect.KProperty

/**
 * A delegator class that provides a default value if the current value is null. It uses a list of suppliers
 * to determine the default value in case no explicit value is set.
 *
 * @param T the type of the value being handled.
 * @param value the initial value of the property, which can be null.
 * @param default an iterable collection of suppliers providing potential default values.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class WithDefault<T : Any>(private var value: T?, private val default: Iterable<Supplier<T?>>) {
    /**
     * Secondary constructor for the `WithDefault` class.
     *
     * Initializes an instance of `WithDefault` with a nullable value and a vararg array of default suppliers.
     * The vararg list of default suppliers is converted into a `List` internally.
     *
     * @param value The initial value of type `T`, which can be null.
     * @param default A variable number of lambdas or `Supplier` instances used as fallback suppliers for default values.
     * @since 3.7.0
     */
    constructor(value: T?, vararg default: Supplier<T?>) : this(
        value,
        default.toList()
    )
    /**
     * Secondary constructor for creating an instance of the `WithDefault` class.
     *
     * @param value The initial value to assign, can be null.
     * @param default A single supplier function that provides a default value when needed.
     * @since 3.7.0
     */
    constructor(value: T?, default: Supplier<T?>) : this(
        value,
        listOf(default)
    )

    /**
     * Gets the value associated with the property. If the current value is null, attempts to resolve a value from
     * the provided suppliers in the default iterable. Throws an exception if no valid value can be resolved.
     *
     * @param thisRef The reference to the object for which the value is being retrieved.
     * @param property The metadata for the property being accessed.
     * @return The resolved value of the property.
     * @throws IllegalStateException if no value is set and the default suppliers do not provide a valid value.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value ?: default.firstNotNullOfOrNull { it() } ?: throw IllegalStateException("No suppliers provided for default value of ${property.name}")
    /**
     * Sets a new value for the property.
     *
     * @param thisRef The reference to the object for which the value is being set. Can be null.
     * @param property Metadata for the property being accessed.
     * @param newValue The new value to assign to the property.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        value = newValue
    }
}

/**
 * A delegate class that enforces that a property value stays within a specified range.
 *
 * @param T The type of the property value. Must implement [Comparable].
 * @property value The current value of the property, or null if not initialized.
 * @property range The range within which the property value must lie.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class InRange<T : Comparable<T>>(private var value: T? = null, private var range: ClosedRange<T>) {
    /**
     * Retrieves the value of a delegated property. If the value has not been initialized,
     * an IllegalStateException is thrown indicating that the property has not been initialized.
     *
     * @param thisRef The reference to the object for which the property is being requested.
     * @param property Metadata for the property being accessed.
     * @throws IllegalStateException If the property is accessed before being initialized.
     * @return The current value of the property if it has been initialized.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value ?: throw IllegalStateException("Property ${property.name} not initialized")
    /**
     * Sets the value of a property, ensuring that the new value satisfies the defined range constraint.
     *
     * @param thisRef The reference to the object on which the property is being set. Can be null.
     * @param property Metadata for the property being set.
     * @param newValue The new value to assign to the property. The value is validated against the range constraint before being set.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the new value does not satisfy the range constraint.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue.validate(
            property,
            message = "($newValue) is not within the range $range",
            predicate = { it in range },
        )
        value = newValue
    }
}
/**
 * A delegate class that manages a nullable value constrained by a specified range.
 * This class ensures that any value assigned to it falls within the specified range.
 *
 * @param T The type of the value being managed. It must implement [Comparable].
 * @property value The current value of the delegate, initially set to null by default.
 * @property range The inclusive range within which the value must lie.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class NullableInRange<T : Comparable<T>?>(private var value: T? = null, private var range: ClosedRange<T & Any>) {
    /**
     * Provides the value associated with the delegated property.
     *
     * @param thisRef The reference to the object on which the property was accessed.
     * @param property The metadata for the property being delegated.
     * @return The value associated with the delegated property.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    /**
     * Sets the value of the property, validating the new value to ensure it falls within the specified range.
     *
     * @param thisRef The reference to the object in which the property is defined.
     * @param property Metadata for the property being assigned a value.
     * @param newValue The new value to be assigned to the property.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if the new value is not within the defined range.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue?.validate(
            property,
            message = "($newValue) is not within the range $range",
            predicate = { it in range },
        )
        value = newValue
    }
}

/**
 * A generic class that manages a convertible property value. The property value is stored in one type (S)
 * while it gets transformed to another type (T) during read operations and back to the original type (S)
 * during write operations.
 *
 * @param S The type of the stored value.
 * @param T The type of the value after transformation.
 * @property value The internal storage for the original value of type S.
 * @property read A transformer function to convert the value from type S to type T during reads.
 * @property write A transformer function to convert the value from type T to type S during writes.
 * @since 3.7.0
 * @author Tommaso Pastorelli
 */
class Converted<S, T>(private var value: S? = null, private val read: Transformer<S?, T>, private val write: Transformer<T?, S>) {
    /**
     * Retrieves the transformed value of the property, applying the specified read transformation.
     * Throws an exception if the property has not been initialized.
     *
     * @param thisRef The instance of the containing class or object where the property is defined.
     * @param property Metadata about the property being accessed.
     * @return The transformed value of the property.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = read(value)
    /**
     * Sets a new value for the property by transforming the input value using the specified write transformer.
     *
     * @param thisRef The reference to the object containing the property. Can be null.
     * @param property Metadata for the property being set.
     * @param newValue The new value to set, which will be transformed before assignment.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        value = write(newValue)
    }
}
/**
 * A generic class that wraps a nullable source value and provides getter and setter operators
 * for transforming the value between two types using provided transformation logic for reading and writing.
 *
 * @param S The type of the source value.
 * @param T The type of the transformed value.
 * @property value The nullable source value to be transformed.
 * @property read A transformer function to convert the source value of type S to type T.
 * @property write A transformer function to convert the value of type T back to type S.
 * @since 3.7.0
 * @author Tommaso Pastorelli
 */
class NullableConverted<S, T>(private var value: S? = null, private val read: Transformer<S, T>, private val write: Transformer<T, S>) {
    /**
     * Retrieves the transformed value using the provided transformer.
     *
     * @param thisRef The reference to the object for which the property is requested.
     * @param property The metadata for the property being accessed.
     * @return The transformed value, or null if the original value is null.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value?.let(read)
    /**
     * Sets a new value after applying a transformation function. The transformed
     * value is stored internally.
     *
     * @param thisRef The reference to the object for which the property is being set.
     * @param property The metadata for the property being set.
     * @param newValue The new value to be set, which will be transformed and stored.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        value = newValue?.let(write)
    }
}