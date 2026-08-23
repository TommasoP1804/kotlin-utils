/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.delegates

import dev.tommasop1804.kutils.*
import kotlin.reflect.KProperty

/**
 * A delegate class that enforces values assigned to it to be positive numbers.
 * This class is a generic type constrained to subclasses of `Number`.
 *
 * @param T The type of the number, constrained to `Number`.
 * @property value The value of the property. Must be used as constructor param for init.
 * @since 3.7.0
 * @author Tommaso Pastorelli
 */
class PositiveNumber<T : Number>(private var value: T? = null) {
    init { value?.validatePositive() }

    /**
     * Retrieves the value of a delegated property or throws an exception if the value is not initialized.
     *
     * @param thisRef The reference to the object containing the delegated property.
     * @param property Metadata for the property being accessed.
     * @throws IllegalStateException if the property's value has not been initialized.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value ?: throw IllegalStateException("Property ${property.name} not initialized")
    /**
     * Sets the value of the given property to the new value, after validating it.
     *
     * @param thisRef The reference to the object containing the property this operator is applied to.
     * @param property The property being delegated.
     * @param newValue The new value to set, which must pass validation.
     * @throws dev.tommasop1804.kutils.exceptions.NumberSignException If the provided value is not positive.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue.validatePositive(property)
        value = newValue
    }
}
/**
 * A delegate class that enforces values assigned to it to be positive numbers.
 * This class is a generic type constrained to subclasses of `Number`.
 *
 * @param T The type of the number, constrained to `Number`.
 * @property value The value of the property. Must be used as constructor param for init.
 * @since 3.7.0
 * @author Tommaso Pastorelli
 */
class NullablePositiveNumber<T : Number?>(private var value: T? = null) {
    init { value?.validatePositive() }

    /**
     * Retrieves the value of a delegated property or throws an exception if the value is not initialized.
     *
     * @param thisRef The reference to the object containing the delegated property.
     * @param property Metadata for the property being accessed.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    /**
     * Sets the value of the given property to the new value, after validating it.
     *
     * @param thisRef The reference to the object containing the property this operator is applied to.
     * @param property The property being delegated.
     * @param newValue The new value to set, which must pass validation.
     * @throws dev.tommasop1804.kutils.exceptions.NumberSignException If the provided value is not positive.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue?.validatePositive(property)
        value = newValue
    }
}

/**
 * A delegate class that enforces the value of a property to always remain negative. This class
 * is generic and can work with any type that extends the `Number` class. It ensures at runtime
 * that the value assigned to the property is negative, throwing an exception otherwise.
 *
 * @param T The type of number this delegate works with, constrained to types extending `Number`.
 * @property value The value of the property. Must be used as constructor param for init.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class NegativeNumber<T : Number>(private var value: T? = null) {
    init { value?.validateNegative() }

    /**
     * Retrieves the value of the delegated property or throws an exception if the value has not been initialized.
     *
     * @param thisRef The reference to the object for which the property is being accessed. May be `null`.
     * @param property The metadata of the property being accessed, such as name and type.
     * @throws IllegalStateException If the property has not been initialized.
     * @return The value of the property if it is not null.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value ?: throw IllegalStateException("Property ${property.name} not initialized")
    /**
     * Sets the value of the delegated property, ensuring that the new value meets the validation criteria.
     * Specifically, the new value must be a negative number; otherwise, an exception is thrown.
     *
     * @param thisRef The reference to the object that contains the delegated property. May be `null`.
     * @param property Metadata for the property being delegated, such as its name and type.
     * @param newValue The new value to set for the property. Must pass the validation that it is a negative number.
     * @throws dev.tommasop1804.kutils.exceptions.NumberSignException If the provided `newValue` is not negative.
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue.validateNegative(property)
        value = newValue
    }
}
/**
 * A delegate class that enforces the value of a property to always remain negative. This class
 * is generic and can work with any type that extends the `Number` class. It ensures at runtime
 * that the value assigned to the property is negative, throwing an exception otherwise.
 *
 * @param T The type of number this delegate works with, constrained to types extending `Number`.
 * @property value The value of the property. Must be used as constructor param for init.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class NullableNegativeNumber<T : Number?>(private var value: T? = null) {
    init { value?.validateNegative() }


    /**
     * Retrieves the value of the delegated property or throws an exception if the value has not been initialized.
     *
     * @param thisRef The reference to the object for which the property is being accessed. May be `null`.
     * @param property The metadata of the property being accessed, such as name and type.
     * @return The value of the property if it is not null.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    /**
     * Sets the value of the delegated property, ensuring that the new value meets the validation criteria.
     * Specifically, the new value must be a negative number; otherwise, an exception is thrown.
     *
     * @param thisRef The reference to the object that contains the delegated property. May be `null`.
     * @param property Metadata for the property being delegated, such as its name and type.
     * @param newValue The new value to set for the property. Must pass the validation that it is a negative number.
     * @throws dev.tommasop1804.kutils.exceptions.NumberSignException If the provided `newValue` is not negative.
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue?.validateNegative(property)
        value = newValue
    }
}

/**
 * Delegate class to enforce that a property can only store non-positive numeric values.
 * It throws an exception if an attempt is made to assign a positive number.
 *
 * @param T The type of the number. This must extend the `Number` class.
 * @property value The value of the property. Must be used as constructor param for init.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class NotPositiveNumber<T : Number>(private var value: T? = null) {
    init { value?.validateNotPositive() }

    /**
     * Retrieves the value of a delegated property.
     *
     * If the underlying value is not initialized, an [IllegalStateException] is thrown.
     *
     * @param thisRef The object containing the property. Can be null.
     * @param property Metadata about the property whose value is being retrieved.
     * @throws IllegalStateException If the value has not been initialized.
     * @return The current value of the property.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value ?: throw IllegalStateException("Property ${property.name} not initialized")
    /**
     * Sets the value of the delegated property after validating that the new value is not positive.
     *
     * @param thisRef The reference to the object for which the property is being set. May be `null`.
     * @param property The metadata for the property being delegated, such as its name and type.
     * @param newValue The new value to be assigned to the delegated property. Must not be positive.
     *
     * @throws dev.tommasop1804.kutils.exceptions.NumberSignException If the new value is positive.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue.validateNotPositive(property)
        value = newValue
    }
}
/**
 * Delegate class to enforce that a property can only store non-positive numeric values.
 * It throws an exception if an attempt is made to assign a positive number.
 *
 * @param T The type of the number. This must extend the `Number` class.
 * @property value The value of the property. Must be used as constructor param for init.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class NullableNotPositiveNumber<T : Number?>(private var value: T? = null) {
    init { value?.validateNotPositive() }


    /**
     * Retrieves the value of a delegated property.
     *
     * If the underlying value is not initialized, an [IllegalStateException] is thrown.
     *
     * @param thisRef The object containing the property. Can be null.
     * @param property Metadata about the property whose value is being retrieved.
     * @return The current value of the property.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    /**
     * Sets the value of the delegated property after validating that the new value is not positive.
     *
     * @param thisRef The reference to the object for which the property is being set. May be `null`.
     * @param property The metadata for the property being delegated, such as its name and type.
     * @param newValue The new value to be assigned to the delegated property. Must not be positive.
     *
     * @throws dev.tommasop1804.kutils.exceptions.NumberSignException If the new value is positive.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue?.validateNotPositive(property)
        value = newValue
    }
}

/**
 * A delegate class that ensures the associated property is initialized with a non-negative number.
 * This class can only be used with numbers, as it enforces validation to ensure the value is not negative.
 *
 * @param T The type of the number, constrained to subclasses of the `Number` class.
 * @property value The value of the property. Must be used as constructor param for init.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class NotNegativeNumber<T : Number>(private var value: T? = null) {
    init { value?.validateNotNegative() }

    /**
     * Retrieves the value of the delegated property.
     *
     * @param thisRef The reference to the object for which the property is being accessed. Can be null.
     * @param property Metadata about the property being accessed, such as its name and type.
     * @return The value of the property if it has been initialized.
     * @throws IllegalStateException if the property has not been initialized.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value ?: throw IllegalStateException("Property ${property.name} not initialized")
    /**
     * Sets the value of the property and validates that the new value is not negative.
     *
     * @param thisRef The reference to the object for which the property is being set. May be `null`.
     * @param property The metadata of the property being set, including its name and owner.
     * @param newValue The new value to be assigned to the property. Must not be negative;
     *                 otherwise, a validation error will occur.
     * @throws dev.tommasop1804.kutils.exceptions.NumberSignException If the provided value is negative. The exception will include
     *                             relevant contextual information about the property.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue.validateNotNegative(property)
        value = newValue
    }
}
/**
 * A delegate class that ensures the associated property is initialized with a non-negative number.
 * This class can only be used with numbers, as it enforces validation to ensure the value is not negative.
 *
 * @param T The type of the number, constrained to subclasses of the `Number` class.
 * @property value The value of the property. Must be used as constructor param for init.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class NullableNotNegativeNumber<T : Number?>(private var value: T? = null) {
    init { value?.validateNotNegative() }

    /**
     * Retrieves the value of the delegated property.
     *
     * @param thisRef The reference to the object for which the property is being accessed. Can be null.
     * @param property Metadata about the property being accessed, such as its name and type.
     * @return The value of the property if it has been initialized.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    /**
     * Sets the value of the property and validates that the new value is not negative.
     *
     * @param thisRef The reference to the object for which the property is being set. May be `null`.
     * @param property The metadata of the property being set, including its name and owner.
     * @param newValue The new value to be assigned to the property. Must not be negative;
     *                 otherwise, a validation error will occur.
     * @throws dev.tommasop1804.kutils.exceptions.NumberSignException If the provided value is negative. The exception will include
     *                             relevant contextual information about the property.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue?.validateNotNegative(property)
        value = newValue
    }
}

/**
 * A delegate class that enforces the constraint of holding only even numbers.
 * This class allows properties to be delegated with it and ensures that the assigned value
 * satisfies the "even number" condition.
 *
 * @param T The type of the number (must be a subtype of [Number]).
 * @property value The value of the property. Must be used as constructor param for init.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class EvenNumber<T : Number>(private var value: T? = null) {
    init { value?.validateEven() }

    /**
     * Retrieves the value of a delegated property.
     *
     * The method is invoked when attempting to retrieve the value of a property
     * that uses this delegate. If the value has not been initialized, an
     * [IllegalStateException] is thrown.
     *
     * @param thisRef The reference to the object for which the property is being accessed.
     *                Can be null if the delegated property is not bound to any specific instance.
     * @param property The metadata for the property being accessed, such as its name and type.
     * @throws IllegalStateException If the property is accessed before being initialized.
     * @return The value of the delegated property if initialized.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the validation predicate fails.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value ?: throw IllegalStateException("Property ${property.name} not initialized")
    /**
     * Sets a new value for the property while validating it using a specified predicate.
     * The validation ensures that the new value satisfies the defined condition before assignment.
     *
     * @param thisRef The reference to the owning object of the property. Can be null.
     * @param property Metadata about the property being assigned. Used in validation.
     * @param newValue The new value to be assigned to the property. Must pass validation.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the new value does not satisfy the validation predicate.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue.validateEven(property)
        value = newValue
    }
}
/**
 * A delegate class that enforces the constraint of holding only even numbers.
 * This class allows properties to be delegated with it and ensures that the assigned value
 * satisfies the "even number" condition.
 *
 * @param T The type of the number (must be a subtype of [Number]).
 * @property value The value of the property. Must be used as constructor param for init.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class NullableEvenNumber<T : Number?>(private var value: T? = null) {
    init { value?.validateEven() }

    /**
     * Retrieves the value of a delegated property.
     *
     * The method is invoked when attempting to retrieve the value of a property
     * that uses this delegate. If the value has not been initialized, an
     * [IllegalStateException] is thrown.
     *
     * @param thisRef The reference to the object for which the property is being accessed.
     *                Can be null if the delegated property is not bound to any specific instance.
     * @param property The metadata for the property being accessed, such as its name and type.
     * @return The value of the delegated property if initialized.
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    /**
     * Sets a new value for the property while validating it using a specified predicate.
     * The validation ensures that the new value satisfies the defined condition before assignment.
     *
     * @param thisRef The reference to the owning object of the property. Can be null.
     * @param property Metadata about the property being assigned. Used in validation.
     * @param newValue The new value to be assigned to the property. Must pass validation.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the new value does not satisfy the validation predicate.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue?.validateEven(property)
        value = newValue
    }
}

/**
 * A class representing a delegate for properties that must hold an odd number of a specific numeric type.
 * This delegate enforces validation to ensure the assigned value is an odd number at the time of assignment.
 *
 * @param T The numeric type of the value. Must be a subtype of [Number].
 * @property value The value of the property. Must be used as constructor param for init.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class OddNumber<T : Number>(private var value: T? = null) {
    init { value?.validateOdd() }

    /**
     * Retrieves the value of the delegated property.
     *
     * If the value is not initialized, an [IllegalStateException] is thrown.
     *
     * @param thisRef The reference to the object on which the property is accessed.
     * @param property Metadata for the property being accessed.
     * @throws IllegalStateException If the property value is not initialized.
     * @return The value of the property.
     *
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value ?: throw IllegalStateException("Property ${property.name} not initialized")
    /**
     * Sets the value of the delegated property after performing validation.
     *
     * @param thisRef The object containing the property. Can be null if the property is not bound to a receiver.
     * @param property The metadata for the property being delegated.
     * @param newValue The new value to be assigned to the property. It must satisfy the validation condition defined by the predicate.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the new value does not pass validation.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue.validateOdd(property)
        value = newValue
    }
}
/**
 * A class representing a delegate for properties that must hold an odd number of a specific numeric type.
 * This delegate enforces validation to ensure the assigned value is an odd number at the time of assignment.
 *
 * @param T The numeric type of the value. Must be a subtype of [Number].
 * @property value The value of the property. Must be used as constructor param for init.
 * @author Tommaso Pastorelli
 * @since 3.7.0
 */
class NullableOddNumber<T : Number?>(private var value: T? = null) {
    init { value?.validateOdd() }

    /**
     * Retrieves the value of the delegated property.
     *
     * If the value is not initialized, an [IllegalStateException] is thrown.
     *
     * @param thisRef The reference to the object on which the property is accessed.
     * @param property Metadata for the property being accessed.
     * @return The value of the property.
     *
     * @since 3.7.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    /**
     * Sets the value of the delegated property after performing validation.
     *
     * @param thisRef The object containing the property. Can be null if the property is not bound to a receiver.
     * @param property The metadata for the property being delegated.
     * @param newValue The new value to be assigned to the property. It must satisfy the validation condition defined by the predicate.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the new value does not pass validation.
     * @since 3.7.0
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        newValue?.validate(property, message = "must be odd", predicate = Number::isOdd)
        value = newValue
    }
}