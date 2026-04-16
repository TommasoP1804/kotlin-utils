/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.delegates

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.identifiers.*
import kotlin.reflect.KProperty

/**
 * A class responsible for managing a UUID (Universally Unique Identifier) restricted to a specific version.
 *
 * This class enforces the UUID version constraint defined at the time of instantiation.
 * The UUID value must conform to the specified version, or a validation exception will be thrown.
 *
 * @param value The initial UUID value, or null if not provided.
 * @param version The UUID version that must be met by any value assigned to this property.
 * @since 3.7.0
 * @author Tommaso Pastorelli
 */
class UuidRestrictedVersion(private var value: Uuid? = null, private val version: UuidVersion) {
    /**
     * Retrieves the value of the property or throws an exception if the value has not been initialized.
     *
     * @param thisRef The reference to the object this property is accessed on. Can be null for top-level properties or delegates.
     * @param property The metadata of the property being accessed.
     * @throws IllegalStateException if the property value is not initialized.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value ?: throw IllegalStateException("Property ${property.name} not initialized")
    /**
     * Sets a new value of type [Uuid] to the property, ensuring the value conforms to the required [UuidVersion].
     *
     * @param thisRef The object for which the property is set. Can be null.
     * @param property Metadata for the property being set.
     * @param newValue The new [Uuid] value to assign to the property. This value's version will be validated
     *                 against the required version before being assigned.
     * @throws IllegalArgumentException If the version of the provided [newValue] does not match the required version.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: Uuid) {
        newValue.version.validate(property, message = "version must be $version", predicate = { it == version })
        value = newValue
    }
}
/**
 * A class that restricts a nullable UUID to a specific UUID version.
 *
 * This class manages a nullable UUID while enforcing validation to ensure any assigned value
 * conforms to a specified UUID version. The allowed version is determined at the time of instantiation.
 *
 * @property value The current value of the UUID. Can be null.
 * @property version The UUID version that the value must conform to. This is immutable.
 * @since 3.7.0
 * @author Tommaso Pastorelli
 */
class NullableUuidRestrictedVersion(private var value: Uuid? = null, private val version: UuidVersion) {
    /**
     * Retrieves the current value of the property.
     *
     * @param thisRef The reference to the object where the property is accessed. Can be null.
     * @param property The metadata of the property being accessed.
     * @return The current value of the property.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    /**
     * Sets a new value for the property, ensuring the UUID version matches the expected version.
     *
     * @param thisRef The reference to the object for which the property is being set. Can be null.
     * @param property The metadata for the property being accessed via [KProperty].
     * @param newValue The new value to set. This value is validated to ensure its version matches the expected version.
     *                  If the validation fails, a validation exception is thrown.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: Uuid?) {
        newValue?.version?.validate(property, message = "version must be $version", predicate = { it == version })
        value = newValue
    }
}