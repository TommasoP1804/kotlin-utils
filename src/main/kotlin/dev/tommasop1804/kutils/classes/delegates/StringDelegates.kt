/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.delegates

import dev.tommasop1804.kutils.*
import kotlin.reflect.KProperty

/**
 * A delegated property class that ensures the value assigned is a non-blank string.
 *
 * This class enforces that the value assigned to it via the `setValue` operator is not null
 * and does not consist solely of whitespace characters. If the validation fails, it throws
 * a [dev.tommasop1804.kutils.exceptions.ValidationFailedException].
 *
 * When attempting to access the uninitialized property using the `getValue` operator, an
 * [IllegalStateException] is thrown with a message indicating that the property has not been
 * initialized.
 *
 * Operators:
 * - `setValue`: Validates and sets the value of the property.
 * - `getValue`: Retrieves the value of the property or throws an exception if uninitialized.
 *
 * @property value The value of the property. Must be used as constructor param for init.
 *
 * @since 3.7.0
 * @author Tommaso Pastorelli
 */
class NotBlankString(private var value: String? = null) {
    /**
     * Retrieves the value of a delegated property.
     *
     * This operator function is invoked when accessing a property that uses delegation with this class.
     * It returns the current value of the property if it has been initialized.
     * If the property has not been initialized, an [IllegalStateException] is thrown.
     *
     * @param thisRef The instance of the class containing the property delegate. Can be null for top-level or object properties.
     * @param property Metadata about the property being accessed, such as its name.
     * @throws IllegalStateException if the property has not been initialized.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value ?: throw IllegalStateException("Property ${property.name} not initialized")
    /**
     * Sets the value of the delegated property while ensuring it is a non-blank string.
     *
     * This function validates the new value assigned to the delegated property using the
     * [String.isNotNullOrBlank] predicate. If the validation fails, a
     * [dev.tommasop1804.kutils.exceptions.ValidationFailedException] is thrown.
     *
     * @param thisRef The reference to the object containing the property. Can be null.
     * @param property Metadata about the property being set.
     * @param newValue The new value to assign to the property. It must be a non-blank string.
     *
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if the newValue is null or blank.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: String) {
        newValue.validate(property, message = "must not be blank", predicate = String::isNotNullOrBlank)
        value = newValue
    }
}
/**
 * A delegated property class that ensures the value assigned is a non-blank string.
 *
 * This class enforces that the value assigned to it via the `setValue` operator is not null
 * and does not consist solely of whitespace characters. If the validation fails, it throws
 * a [dev.tommasop1804.kutils.exceptions.ValidationFailedException].
 *
 * When attempting to access the uninitialized property using the `getValue` operator, an
 * [IllegalStateException] is thrown with a message indicating that the property has not been
 * initialized.
 *
 * Operators:
 * - `setValue`: Validates and sets the value of the property.
 * - `getValue`: Retrieves the value of the property or throws an exception if uninitialized.
 *
 * @property value The value of the property. Must be used as constructor param for init.
 *
 * @since 3.7.0
 * @author Tommaso Pastorelli
 */
class NullableNotBlankString(private var value: String? = null) {
    /**
     * Retrieves the value of a delegated property.
     *
     * This operator function is invoked when accessing a property that uses delegation with this class.
     * It returns the current value of the property if it has been initialized.
     * If the property has not been initialized, an [IllegalStateException] is thrown.
     *
     * @param thisRef The instance of the class containing the property delegate. Can be null for top-level or object properties.
     * @param property Metadata about the property being accessed, such as its name.
     * @throws IllegalStateException if the property has not been initialized.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    /**
     * Sets the value of the delegated property while ensuring it is a non-blank string.
     *
     * This function validates the new value assigned to the delegated property using the
     * [String.isNotNullOrBlank] predicate. If the validation fails, a
     * [dev.tommasop1804.kutils.exceptions.ValidationFailedException] is thrown.
     *
     * @param thisRef The reference to the object containing the property. Can be null.
     * @param property Metadata about the property being set.
     * @param newValue The new value to assign to the property. It must be a non-blank string.
     *
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if the newValue is null or blank.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: String?) {
        newValue?.validate(property, message = "must not be blank", predicate = String::isNotNullOrBlank)
        value = newValue
    }
}