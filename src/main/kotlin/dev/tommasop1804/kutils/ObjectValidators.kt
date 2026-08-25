/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:OptIn(ExperimentalContracts::class)
@file:JvmName("ObjectValidatorsKt")
@file:Since("5.0.0")
@file:Suppress("unused")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.exceptions.ClassMismatchException
import dev.tommasop1804.kutils.exceptions.ExpectationMismatchException
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.exceptions.RequiredParameterException
import dev.tommasop1804.kutils.exceptions.RequiredPropertyException
import dev.tommasop1804.kutils.exceptions.ValidationFailedException
import dev.tommasop1804.kutils.exceptions.ValueOutOfRangeException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.invoke
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KType
import kotlin.toString

/**
 * Ensures that the current object satisfies the specified predicate. If the predicate
 * returns false, an IllegalArgumentException is thrown. Otherwise, the object itself
 * is returned.
 *
 * @param predicate a predicate function to test the current object
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @return the current object if it satisfies the predicate
 * @throws IllegalArgumentException if the current object does not satisfy the predicate
 * @since 1.0.0
 */
@JvmName("receiverRequire")
@IgnorableReturnValue
fun <T> T.require(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) IllegalArgumentException("Invalid argument: $this not ensure the predicate", cause?.invoke(this)) else causeOf(this).initCause(IllegalArgumentException("Invalid argument: $this not ensure the predicate", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the current object satisfies the specified predicate. If the predicate evaluates
 * to `false`, an `IllegalArgumentException` is thrown with a message supplied by `lazyMessage`.
 *
 * @param T the type of the object being checked
 * @param predicate a condition that the current object must satisfy
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param lazyMessage a supplier that provides the exception message if the condition is not met
 * @return the current object if the predicate evaluates to `true`
 * @since 1.0.0
 */
@JvmName("receiverRequire")
@IgnorableReturnValue
fun <T> T.require(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Supplier<Any>, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) IllegalArgumentException(lazyMessage().toString(), cause?.invoke(this)) else causeOf(this).initCause(IllegalArgumentException(lazyMessage().toString(), cause?.invoke(this)))
    return this
}

/**
 * Evaluates the current object against the provided predicate and throws an exception if the predicate returns false.
 *
 * The method ensures that the provided object meets the specified conditions, defined by the predicate. If the object
 * does not satisfy the predicate, an exception will be thrown, which is supplied by the `lazyException` supplier.
 *
 * @param predicate a predicate function that takes the current object as input and returns a boolean indicating
 * whether the condition is met
 * @param lazyException a supplier function that provides the exception to be thrown if the predicate evaluates to false
 * @return the current object, if the predicate evaluates to true
 * @since 1.0.0
 */
@JvmName("receiverRequireOrThrow")
@IgnorableReturnValue
fun <T> T.requireOrThrow(lazyException: Transformer<T, Throwable>, predicate: Predicate<T>): T {
    if (!predicate(this)) throw lazyException(this)
    return this
}

/**
 * Ensures that the current object does not satisfy the given predicate. If the predicate evaluates to true,
 * this method throws an exception, optionally accepting a specific cause or a custom throwable.
 *
 * @param causeOf an optional custom throwable to be used if the predicate evaluates to true.
 * @param cause an optional underlying cause for the thrown exception.
 * @param predicate a conditional function that tests the current object.
 * @return the current object if it does not satisfy the predicate.
 * @throws Throwable the provided cause or a default IllegalArgumentException if the predicate evaluates to true.
 * @since 4.2.0
 */
@JvmName("receiverRequireNot")
@IgnorableReturnValue
fun <T> T.requireNot(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) IllegalArgumentException("Invalid argument: $this not ensure the predicate", cause?.invoke(this)) else causeOf(this).initCause(IllegalArgumentException("Invalid argument: $this not ensure the predicate", cause?.invoke(this)))
    return this
}
/**
 * Evaluates the given predicate on the receiver object, and if the predicate returns `true`,
 * throws an exception with the specified message and optional causes.
 *
 * @param causeOf The primary throwable to be thrown if the predicate evaluates to `true`.
 *                If null, a default `IllegalArgumentException` is created.
 * @param cause The secondary throwable to be used as the cause of the exception.
 *              Can be null.
 * @param lazyMessage A supplier for the exception message which is lazily evaluated if the
 *                    predicate evaluates to `true`.
 * @param predicate A predicate that is evaluated on the receiver object.
 *                  If the predicate returns `true`, an exception is thrown.
 * @return The receiver object if the predicate evaluates to `false`.
 * @since 4.2.0
 */
@JvmName("receiverRequireNot")
@IgnorableReturnValue
fun <T> T.requireNot(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) IllegalArgumentException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(IllegalArgumentException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}

/**
 * Verifies that the receiver does not satisfy the given predicate. If the predicate is satisfied,
 * an exception is thrown using the provided lazy exception transformer. Otherwise, returns the receiver.
 *
 * @param lazyException a function that transforms the receiver into an exception to be thrown
 *                       if the predicate evaluates to true
 * @param predicate a condition to evaluate against the receiver
 * @return the receiver itself if the predicate evaluates to false
 * @since 4.2.0
 */
@JvmName("receiverRequireNotOrThrow")
@IgnorableReturnValue
fun <T> T.requireNotOrThrow(lazyException: Transformer<T, Throwable>, predicate: Predicate<T>): T {
    if (predicate(this)) throw lazyException(this)
    return this
}

/**
 * Ensures that the receiver is null. If the receiver is not null, an IllegalArgumentException is thrown.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @return The receiver itself if it is null.
 * @since 1.0.0
 */
@JvmName("receiverRequireNull")
@IgnorableReturnValue
fun <T> T?.requireNull(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@requireNull == null)
    }
    if (this != null) throw if (causeOf == null) IllegalArgumentException("Invalid argument: $this is null", cause?.invoke(this)) else causeOf(this).initCause(IllegalArgumentException("Invalid argument: $this is null", cause?.invoke(this)))
    return this
}
/**
 * Throws an [IllegalArgumentException] if the value is not null. If the value is null, it is returned.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param lazyMessage A supplier function that provides the exception message if the value is not null.
 * @return Returns the nullable value if it is null.
 * @since 1.0.0
 */
@JvmName("receiverRequireNull")
@IgnorableReturnValue
fun <T> T?.requireNull(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T? {
    contract {
        returns() implies (this@requireNull == null)
    }
    if (this != null) throw if (causeOf == null) IllegalArgumentException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(IllegalArgumentException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Ensures that the given nullable object is null. If the object is not null, an exception provided
 * by the supplied [lazyException] is thrown.
 *
 * @param lazyException a supplier function that provides the exception to be thrown if the object is not null
 * @return the nullable object itself if it is null; otherwise, an exception is thrown
 * @since 1.0.0
 */
@JvmName("receiverRequireNullOrThrow")
@IgnorableReturnValue
fun <T> T?.requireNullOrThrow(lazyException: Transformer<T, Throwable>): T? {
    contract {
        returns() implies (this@requireNullOrThrow == null)
    }
    if (this != null) throw lazyException(this)
    return this
}

/**
 * Ensures that the receiver is not null. If the receiver is null, an IllegalArgumentException is thrown.
 * This function is typically used when nullability should be enforced at runtime.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @return the non-nullable value of the receiver.
 * @since 1.0.0
 */
@JvmName("receiverRequireNotNull")
@IgnorableReturnValue
fun <T> T?.requireNotNull(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@requireNotNull != null)
    }
    if (this == null) throw if (causeOf == null) IllegalArgumentException("Invalid argument: $this is null", cause?.invoke()) else causeOf().initCause(IllegalArgumentException("Invalid argument: $this is null", cause?.invoke()))
    return this
}
/**
 * Ensures that the nullable receiver is not null. If the receiver is null, an
 * IllegalArgumentException is thrown with the message provided by the lazyMessage supplier.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param lazyMessage a supplier function that generates the exception message if the receiver is null
 * @return the non-nullable receiver
 * @since 1.0.0
 */
@JvmName("receiverRequireNotNull")
@IgnorableReturnValue
fun <T> T?.requireNotNull(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    contract {
        returns() implies (this@requireNotNull != null)
    }
    if (this == null) throw if (causeOf == null) IllegalArgumentException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(IllegalArgumentException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Ensures that the value is not null and returns it. If the value is null, throws an exception
 * provided by the given exception supplier.
 *
 * @param lazyException A supplier function that provides the exception to be thrown if the value is null.
 * @return The non-null value.
 * @since 1.0.0
 */
@JvmName("receiverRequireNotNullOrThrow")
@IgnorableReturnValue
fun <T> T?.requireNotNullOrThrow(lazyException: ThrowableSupplier): T {
    contract {
        returns() implies (this@requireNotNullOrThrow != null)
    }
    if (this == null) throw lazyException()
    return this
}

/**
 * Checks if the given object satisfies the provided predicate.
 * Throws an IllegalStateException if the predicate evaluation returns false.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param predicate a function that evaluates to true for valid objects and false otherwise
 * @return the original object if it satisfies the predicate
 * @since 1.0.0
 */
@JvmName("receiverCheck")
@IgnorableReturnValue
fun <T> T.check(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) IllegalStateException("Invalid argument: $this not ensure the predicate", cause?.invoke(this)) else causeOf(this).initCause(IllegalStateException("Invalid argument: $this not ensure the predicate", cause?.invoke(this)))
    return this
}
/**
 * Checks if the current object satisfies the given predicate.
 * If the predicate evaluates to `false`, an `IllegalStateException` is thrown
 * with the result of the `lazyMessage` supplier as the exception message.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param predicate a predicate function to test the current object
 * @param lazyMessage a supplier for the exception message if the check fails
 * @return the current object if the predicate evaluates to `true`
 * @since 1.0.0
 */
@JvmName("receiverCheck")
@IgnorableReturnValue
fun <T> T.check(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Supplier<Any>, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) IllegalStateException(lazyMessage().toString(), cause?.invoke(this)) else causeOf(this).initCause(IllegalStateException(lazyMessage().toString(), cause?.invoke(this)))
    return this
}

/**
 * Checks if the current object does not satisfy the given predicate. If the predicate evaluates to `true`,
 * an exception is thrown. Otherwise, the current object is returned.
 *
 * @param causeOf The primary throwable cause to be thrown if the predicate evaluates to `true`.
 *                Defaults to `null`.
 * @param cause The secondary throwable cause that is set as the cause of the exception if thrown.
 *              Defaults to `null`.
 * @param predicate The predicate to evaluate the current object.
 * @return The current object if the predicate evaluates to `false`.
 * @throws IllegalStateException If the predicate evaluates to `true` and no `causeOf` is provided.
 *         If `causeOf` is provided, it is thrown instead, with its cause set to an `IllegalStateException`.
 * @since 4.2.0
 */
@JvmName("receiverCheckNot")
@IgnorableReturnValue
fun <T> T.checkNot(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) IllegalStateException("Invalid argument: $this not ensure the predicate", cause?.invoke(this)) else causeOf(this).initCause(IllegalStateException("Invalid argument: $this not ensure the predicate", cause?.invoke(this)))
    return this
}
/**
 * Evaluates the given predicate on the receiver object, and if the predicate returns true,
 * throws an exception constructed using the provided optional cause or causeOf, and a lazily evaluated message.
 *
 * @param causeOf the primary throwable that caused this exception to be thrown (optional)
 * @param cause the secondary throwable or cause (optional)
 * @param lazyMessage a supplier that provides the error message used in the exception
 * @param predicate the condition to evaluate on the receiver object
 * @return the receiver object if the predicate evaluates to false
 * @throws IllegalStateException if the predicate evaluates to true and no causeOf is provided
 * @since 4.2.0
 */
@JvmName("receiverCheckNot")
@IgnorableReturnValue
fun <T> T.checkNot(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) IllegalStateException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(IllegalStateException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}

/**
 * Checks if the receiver of this function is null. Throws an IllegalStateException
 * if it is not null.
 *
 * Use this function to enforce null checks in code paths where null is an expected state
 * and non-null cases should be flagged as invalid.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @return Returns the receiver object if it is null.
 * @since 1.0.0
 */
@JvmName("receiverCheckNull")
@IgnorableReturnValue
fun <T> T?.checkNull(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@checkNull == null)
    }
    if (this != null) throw if (causeOf == null) IllegalStateException("Invalid state: $this is not-null", cause?.invoke(this)) else causeOf(this).initCause(IllegalStateException("Invalid state: $this is not-null", cause?.invoke(this)))
    return this
}
/**
 * Checks if the current value is null. If the value is not null, throws an IllegalStateException with
 * the message provided by the `lazyMessage`.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param lazyMessage a supplier that provides the exception message if the value is non-null.
 * @return the current value if it is null; otherwise, an exception is thrown.
 * @since 1.0.0
 */
@JvmName("receiverCheckNull")
@IgnorableReturnValue
fun <T> T?.checkNull(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T? {
    contract {
        returns() implies (this@checkNull == null)
    }
    if (this != null) throw if (causeOf == null) IllegalStateException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(IllegalStateException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}

/**
 * Ensures that the current nullable value is not null and returns the value.
 * Throws an IllegalStateException if the value is null.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @return The non-nullable value.
 * @throws IllegalStateException if the value is null.
 * @since 1.0.0
 */
@JvmName("receiverCheckNotNull")
@IgnorableReturnValue
fun <T> T?.checkNotNull(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@checkNotNull != null)
    }
    if (this == null) throw if (causeOf == null) IllegalStateException("Invalid state: $this is null", cause?.invoke()) else causeOf().initCause(IllegalStateException("Invalid state: $this is null", cause?.invoke()))
    return this
}
/**
 * Ensures that the receiver is not null. If the receiver is null, an IllegalStateException
 * is thrown with the message provided by the lazyMessage supplier.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param lazyMessage a supplier that provides the message to include in the exception if null is encountered
 * @return the non-null receiver of the function
 * @since 1.0.0
 */
@JvmName("receiverCheckNotNull")
@IgnorableReturnValue
fun <T> T?.checkNotNull(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    contract {
        returns() implies (this@checkNotNull != null)
    }
    if (this == null) throw if (causeOf == null) IllegalStateException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(IllegalStateException(lazyMessage().toString(), cause?.invoke()))
    return this
}

/**
 * Validates the current object instance against a provided predicate. If the predicate
 * does not hold true for the current instance, a `ValidationFailedException` is thrown.
 *
 * This utility function simplifies the process of ensuring that an object meets a specific
 * condition or set of conditions before further processing.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param predicate A condition represented as a `Predicate` that the current object is validated against.
 * @return The current instance, if it satisfies the given predicate.
 * @throws ValidationFailedException If the object instance does not satisfy the predicate.
 * @since 1.0.0
 */
@JvmName("receiverValidate")
@IgnorableReturnValue
fun <T> T.validate(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) ValidationFailedException("Validation failed.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Validation failed.", cause?.invoke(this)))
    return this
}
/**
 * Validates an object of type [T] against a specified predicate function. If the validation fails,
 * a custom exception is thrown with the message provided by the [lazyMessage].
 *
 * @param T The type of the object being validated.
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param predicate A [Predicate] representing the validation logic to be applied on the object.
 *                   The predicate should return `true` if the validation passes and `false` otherwise.
 * @param lazyMessage A [Supplier] that provides the exception message to be used if the validation fails.
 * @return The object itself if it satisfies the predicate.
 * @throws ValidationFailedException if the validation fails, with the message provided by [lazyMessage].
 * @since 1.0.0
 */
@JvmName("receiverValidate")
@IgnorableReturnValue
fun <T> T.validate(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates the object using the provided predicate. If the predicate returns false,
 * a [ValidationFailedException] is thrown.
 *
 * @param predicate The predicate function that determines whether the object is valid.
 * @param property The Kotlin property ([KProperty]) associated with the object being validated. Can be null.
 * @param variableName An optional name for the variable being validated. Defaults to null.
 * @param message An optional message providing additional details about the validation failure. Defaults to null.
 * @param causeOf An optional [Throwable] representing the root cause of the validation failure. Defaults to null.
 * @param cause The cause of exception (another exception)
 * @return The validated object if the predicate returns true.
 * @throws ValidationFailedException If the predicate returns false, with the provided details.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.validate(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) ValidationFailedException(property, variableName, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message, cause?.invoke(this)))
    return this
}
/**
 * Validates the current receiver instance using the provided predicate.
 * If the predicate evaluates to false, a `ValidationFailedException` is thrown.
 *
 * @param predicate the predicate to evaluate against the instance; should return true if the instance is valid
 * @param property the primary property associated with the validation, or null if not specified
 * @param variable an additional variable providing context for the validation, or null if not specified
 * @param message an optional message to include with the exception if validation fails
 * @param causeOf an optional pre-existing throwable that will be thrown if validation fails, with an initialized cause
 * @param cause an optional cause for the exception if validation fails
 * @return the original receiver instance if validation succeeds
 * @throws ValidationFailedException if the predicate evaluates to false
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.validate(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) ValidationFailedException(property, variable, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message, cause?.invoke(this)))
    return this
}
/**
 * Validates the current object using a specified predicate. If the predicate returns `false`,
 * a `ValidationFailedException` is thrown with optional details about the failure.
 *
 * @param predicate The predicate function that determines whether the object is valid.
 * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can be nullable.
 * @param parameterName The name of the parameter that caused the validation issue. Can be nullable.
 * @param message An optional custom message providing additional details about the validation failure. Can be nullable.
 * @param causeOf An optional `Throwable` cause for the validation failure, which will wrap the `ValidationFailedException` if provided.
 * @param cause The cause of exception (another exception)
 * @return The object being validated, if it satisfies the given predicate.
 * @throws ValidationFailedException if the predicate returns `false` and no `causeOf` is provided.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.validate(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message, cause?.invoke(this)))
    return this
}
/**
 * Validates the current receiver object against a given predicate and throws a `ValidationFailedException`
 * if the validation fails.
 *
 * @param T the type of the receiver object
 * @param predicate the predicate function used to validate the receiver object
 * @param callable an optional reference to the related [KFunction], used to provide context in case of validation failure
 * @param parameter an optional reference to the [KParameter] representing the parameter involved in the validation
 * @param message an optional message providing additional context about the validation failure
 * @param causeOf an optional throwable that acts as the cause of the validation failure exception
 * @param cause an optional throwable that serves as the underlying cause of the validation failure exception
 * @return the receiver object if validation passes
 * @throws ValidationFailedException if the predicate evaluates to `false`
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.validate(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) ValidationFailedException(callable, parameter, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message, cause?.invoke(this)))
    return this
}
/**
 * Validates the receiver object based on a specified predicate. If the validation fails,
 * a `ValidationFailedException` is thrown with the provided details.
 *
 * @param predicate the validation logic that determines whether the receiver is valid
 * @param callableName the name of the callable (e.g., function or method) to associate with the validation
 * @param parameterName the name of the parameter being validated, or null if not applicable
 * @param message an optional custom message providing additional context about the validation failure
 * @param causeOf an existing throwable that will be used as the cause of a `ValidationFailedException`, if provided
 * @param cause the underlying cause of the exception, or null if no cause exists
 * @return the receiver object if validation passes
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.validate(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message, cause?.invoke(this)))
    return this
}
/**
 * Validates the current object against a specified predicate.
 *
 * If the validation fails, a `ValidationFailedException` is thrown with the provided details.
 *
 * @param T The type of the object being validated.
 * @param predicate The predicate function used to validate the current object.
 * @param callableName The name of the callable (e.g., function or property) where validation is being performed, or null if not specified.
 * @param parameter The parameter associated with the validation, represented as a `KParameter`, or null if not applicable.
 * @param message An optional message providing additional context about the validation failure.
 * @param causeOf The exception to be thrown as the cause if validation fails. If not specified, a `ValidationFailedException` is created.
 * @param cause An underlying root cause of the failure that will be attached to the `ValidationFailedException`.
 * @return The validated object if the predicate function returns true.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.validate(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message, cause?.invoke(this)))
    return this
}

/**
 * Validates that the receiver does not satisfy the given predicate. If the predicate
 * evaluates to true, a [ValidationFailedException] or the provided exception is thrown.
 *
 * @param causeOf An optional throwable that will be thrown if validation fails.
 *                If null, a new [ValidationFailedException] is created.
 * @param cause An optional cause that can be passed to the generated exception
 *              for additional context. This is ignored if `causeOf` is provided.
 * @param predicate A predicate to test the receiver. If the predicate evaluates to true,
 *                  the validation is considered failed.
 * @return The receiver if the validation passes.
 * @throws ValidationFailedException If the receiver satisfies the predicate and
 *                                   no exception is provided in `causeOf`.
 * @since 4.2.0
 */
@JvmName("receiverValidateNot")
@IgnorableReturnValue
fun <T> T.validateNot(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) ValidationFailedException("Validation failed.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Validation failed.", cause?.invoke(this)))
    return this
}
/**
 * Validates the receiver object against a given predicate and throws a validation exception
 * if the predicate evaluates to true.
 *
 * @param causeOf An optional throwable to use as the cause of the validation failure.
 * @param cause An optional throwable to set as the underlying cause of the validation failure.
 * @param lazyMessage A supplier that provides a message for the validation failure when needed.
 * @param predicate A predicate to test the receiver object against.
 * @return The receiver object if the predicate evaluates to false.
 * @since 4.2.0
 */
@JvmName("receiverValidateNot")
@IgnorableReturnValue
fun <T> T.validateNot(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates the current value of type `T` against a given predicate. If the predicate evaluates to `true`,
 * a `ValidationFailedException` is thrown.
 *
 * @param property The property associated with this validation, or null if not applicable.
 * @param variableName The name of the variable being validated, or null if not specified.
 * @param message An optional message providing additional context in case of validation failure.
 * @param causeOf An optional throwable that is the overarching cause of this validation, used to chain exceptions.
 * @param cause An optional throwable to include as the cause of the `ValidationFailedException`.
 * @param predicate A predicate that determines the validation logic. If the predicate returns `true`, the validation fails.
 * @return The current instance of type `T` if validation succeeds.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.validateNot(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) ValidationFailedException(property, variableName, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message, cause?.invoke(this)))
    return this
}
/**
 * Validates the current object against a specified predicate and throws a `ValidationFailedException`
 * if the predicate returns true.
 *
 * The exception includes optional metadata such as the associated properties, an optional message,
 * and an optional cause chain.
 *
 * @param property the primary property associated with the validation, providing contextual information; may be null
 * @param variable an optional secondary property that may offer additional context; may be null
 * @param message an optional descriptive message to include in the exception, providing further details; may be null
 * @param causeOf an optional pre-existing throwable used as the root cause of the exception; may be null
 * @param cause an additional optional throwable to include as the underlying cause in the exception; may be null
 * @param predicate a predicate function to evaluate the condition; the validation fails if this function returns true
 * @return the current object if the validation passes without throwing an exception
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.validateNot(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) ValidationFailedException(property, variable, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message, cause?.invoke(this)))
    return this
}
/**
 * Validates the receiver object against the specified predicate and throws a `ValidationFailedException`
 * if the predicate evaluates to `true`.
 *
 * @param callable A reference to the Kotlin function (`KFunction`) that the validation is related to. Can be null.
 * @param parameterName The name of the parameter in the `callable` to which this validation applies. Can be null.
 * @param message An optional custom message that provides additional context about the validation failure. Default is null.
 * @param causeOf An optional pre-existing throwable to be set as the primary cause of the exception. Can be null.
 * @param cause An optional underlying cause of the validation failure, represented as a `Throwable`. Can be null.
 * @param predicate The predicate function that determines if the validation fails for the receiver object.
 * @return The receiver object if the predicate does not evaluate to `true`.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.validateNot(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message, cause?.invoke(this)))
    return this
}
/**
 * Validates the current value against a specified predicate. If the predicate evaluates to true,
 * a `ValidationFailedException` is thrown with the provided details.
 *
 * @param callable the callable (function) related to the validation context, or null if not applicable
 * @param parameter the parameter being validated, or null if not applicable
 * @param message an optional message providing additional context about the validation failure
 * @param causeOf an optional throwable to wrap the validation failure exception as its cause
 * @param cause the underlying cause of the validation failure, or null if no cause is specified
 * @param predicate the predicate used to determine if validation fails
 * @return the current value if validation passes
 * @throws ValidationFailedException if the predicate evaluates to true for the current value
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.validateNot(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) ValidationFailedException(callable, parameter, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message, cause?.invoke(this)))
    return this
}
/**
 * Validates the current value against the specified predicate. If the predicate evaluates to `true`,
 * a validation exception is thrown with optional contextual details.
 *
 * @param callableName The name of the callable (e.g., function or method) where the validation occurs.
 * @param parameterName The name of the parameter being validated, or `null` if not applicable.
 * @param message An optional custom message providing additional details about the validation failure.
 * @param causeOf An optional pre-existing exception to wrap the validation failure exception, or `null` if not applicable.
 * @param cause An optional underlying cause of the validation failure, or `null` if not applicable.
 * @param predicate A predicate that determines if the validation should fail. If this evaluates to `true`, validation fails.
 * @return The original value if validation passes.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.validateNot(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message, cause?.invoke(this)))
    return this
}
/**
 * Validates the current instance against a specified predicate and throws a ValidationFailedException
 * if the predicate evaluation returns true.
 *
 * @param callableName The name of the callable (e.g., function or property) where the validation occurs, or null if not specified.
 * @param parameter The parameter associated with the validation, or null if not applicable.
 * @param message An optional custom error message for the validation failure.
 * @param causeOf An optional primary throwable that caused the validation failure and is used as the root cause.
 * @param cause An optional supplementary throwable providing additional context to the validation failure.
 * @param predicate A condition to evaluate the current instance. If the condition returns true, the validation fails.
 * @return The current instance if the validation passes.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.validateNot(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (predicate(this)) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message, cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message, cause?.invoke(this)))
    return this
}

/**
 * Validates that the calling object is null. If it is not null, a [ValidationFailedException] is thrown
 * with an appropriate error message.
 *
 * This function uses Kotlin contracts to enable smart casting in validating expressions.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @return The original value, which is expected to be null.
 * @throws ValidationFailedException If the value is not null.
 * @since 1.0.0
 */
@IgnorableReturnValue
@JvmName("receiverValidateNull")
fun <T> T?.validateNull(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException("Value is not null.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is not null.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current object or value is null. If the object is not null, a
 * [ValidationFailedException] is thrown with a message provided by the `lazyMessage` supplier.
 *
 * This function is useful to ensure that a specific value is null in contexts where
 * validation is necessary.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param lazyMessage a supplier providing the exception message when validation fails.
 *                    The supplier will be evaluated lazily if the value is not null.
 * @return the current value if it is null; otherwise, a [ValidationFailedException] is thrown.
 * @since 1.0.0
 */
@JvmName("receiverValidateNull")
@IgnorableReturnValue
fun <T> T?.validateNull(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T? {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the given object is null. If the object is not null, this method throws a [ValidationFailedException].
 *
 * @param property The [KProperty] linked to the validation context, or null if not applicable.
 * @param variableName The name of the variable being validated, or null if not specified.
 * @param message An optional custom message to include in the exception, or null to use the default message.
 * @param causeOf An optional [Throwable] to wrap the validation exception, or null if not applicable.
 * @param cause The underlying cause of the exception, or null if not applicable.
 * @return The current object if the validation passes (i.e., it is null).
 * @throws ValidationFailedException if the object is not null. The exception includes detailed information such as the property, variable name, custom message, and causes.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNull(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not null", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not null", cause?.invoke(this)))
    return this
}
/**
 * Validates if the receiver object is null. If the receiver is not null, a `ValidationFailedException` is thrown.
 *
 * The exception thrown can include detailed messages and causes if specified.
 *
 * @param property the primary property being validated; optional and can be null
 * @param variable an optional secondary property providing additional context, or null if not specified
 * @param message an optional message to describe the validation failure, or null if not specified
 * @param causeOf optional pre-existing throwable that acts as the source of this exception; `initCause` is invoked if provided
 * @param cause an optional root cause of the exception, or null if no underlying cause exists
 * @return the receiver object if it is null; otherwise, an exception is thrown
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNull(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not null", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not null", cause?.invoke(this)))
    return this
}
/**
 * Validates that the value of the calling receiver is null. If the receiver is not null,
 * this method throws a `ValidationFailedException` with detailed contextual information.
 *
 * @param callable An optional reference to the function (`KFunction`) to which this validation applies.
 * @param parameterName An optional name of the parameter in the provided `callable`
 *                      that is being validated.
 * @param message An optional custom error message describing the validation failure.
 *                Defaults to "is not null" if not provided.
 * @param causeOf An optional `Throwable` to provide additional context about the validation failure's cause.
 * @param cause An optional `Throwable` representing the root cause of the validation failure.
 * @return The receiver itself if it is null.
 * @throws ValidationFailedException If the receiver is not null, providing details about
 *                                    the failed validation, including the optional parameters.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNull(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not null", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not null", cause?.invoke(this)))
    return this
}
/**
 * Validates that the receiver is null and throws a `ValidationFailedException` if the receiver is not null.
 *
 * @param callable the [KFunction] associated with the validation, may be null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, may be null if not applicable
 * @param message an optional error message to provide context in case validation fails, default is null
 * @param causeOf the optional main cause of the exception, if applicable, default is null
 * @param cause an optional underlying secondary cause for additional exception context, default is null
 * @return the receiver [T] if it is null
 * @throws ValidationFailedException if the receiver is not null, providing the callable, parameter, message, and causes
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNull(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not null", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not null", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is null based on a specified predicate, and throws a
 * `ValidationFailedException` if the object is not null.
 *
 * @param callableName the name of the callable (e.g., function or method) where the validation occurs
 * @param parameterName the name of the parameter being validated (optional)
 * @param message a custom message describing the validation failure (optional)
 * @param causeOf the root cause of the validation failure to be thrown (optional)
 * @param cause the underlying exception causing the validation failure (optional)
 * @return the current object if it is null
 * @throws ValidationFailedException if the object is not null
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNull(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not null", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not null", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given object is null. If the object is not null, this method throws a
 * `ValidationFailedException` with the provided details.
 *
 * @param callableName The name of the callable (e.g., function or property) where the validation occurs.
 *                     Can be null.
 * @param parameter The KParameter instance representing the parameter related to the validation. Can be null.
 * @param message An optional message to include in the exception if validation fails. Defaults to "is not null".
 * @param causeOf The primary cause for the validation failure, which can be a throwable. Can be null.
 * @param cause An optional throwable that caused this validation to fail. Can be null.
 * @return The validated object if it is null.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNull(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not null", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not null", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current nullable receiver is not null.
 * If the receiver is null, a `ValidationFailedException` is thrown.
 * Otherwise, the receiver is returned as a non-nullable type.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @return The non-nullable version of the receiver.
 * @throws ValidationFailedException if the receiver is null.
 * @since 1.0.0
 */
@IgnorableReturnValue
@JvmName("receiverValidateNotNull")
fun <T> T?.validateNotNull(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException("Value is null.", cause?.invoke()) else causeOf().initCause(ValidationFailedException("Value is null.", cause?.invoke()))
    return this
}
/**
 * Validates that the current nullable receiver object is not null. If it is null, a [ValidationFailedException]
 * is thrown using the provided lazy message supplier.
 *
 * This function ensures that the receiver object is not null during runtime and returns the non-null
 * value if the validation passes. The lazy message supplier is evaluated only if the validation fails.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param lazyMessage A function that supplies a message to be included in the exception if validation fails.
 * @return The non-null receiver object of type T.
 * @throws ValidationFailedException if the receiver object is null.
 * @since 1.0.0
 */
@IgnorableReturnValue
@JvmName("receiverValidateNotNull")
fun <T> T?.validateNotNull(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(ValidationFailedException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that the given receiver is not null and satisfies the specified validation predicate.
 * If the receiver is null, a `ValidationFailedException` is thrown with optional details about
 * the property, variable name, message, and causes.
 *
 * @param property The Kotlin property (`KProperty`) where validation is being applied. Optional.
 * @param variableName The name of the variable being validated. Used for contextual error messages. Optional.
 * @param message Additional message providing details about the validation failure. Defaults to null.
 * @param causeOf Primary throwable cause to be used when the receiver fails validation. Optional.
 * @param cause Secondary throwable cause for the validation failure. Optional.
 * @return The non-null receiver if validation succeeds.
 * @throws ValidationFailedException If the receiver is null or the predicate fails validation.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNotNull(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is null", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variableName, message ?: "is null", cause?.invoke()))
    return this
}
/**
 * Validates that the receiver is not null, throwing a `ValidationFailedException` if the validation fails.
 *
 * This method is particularly useful for assertions and ensures a given value is not null. If the receiver is null,
 * a `ValidationFailedException` is thrown with detailed information, including the provided property, variable,
 * optional message, and cause.
 *
 * @param property The primary property associated with this validation, providing context when throwing an exception.
 * @param variable An optional secondary property that provides additional context about the failure.
 * @param message Optional additional details or custom messages about the validation failure.
 * @param causeOf Optional cause to replace the `ValidationFailedException` if specified.
 * @param cause Optional root cause of the validation failure for exception chaining.
 * @return The validated object if it is not null.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNotNull(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is null", cause?.invoke()) else causeOf().initCause(ValidationFailedException(property, variable, message ?: "is null", cause?.invoke()))
    return this
}
/**
 * Validates that the receiver is not null. If the receiver is null, a `ValidationFailedException` is thrown.
 *
 * This function can be used to enforce non-null constraints on objects and provide detailed error messages
 * contextualized by the provided callable, parameter name, custom message, and cause.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation context is related. Can be null.
 * @param parameterName The name of the parameter in the provided callable that is being validated. Optional and can be null.
 * @param message An optional custom message describing the validation failure. Defaults to "is null" if not provided.
 * @param causeOf A throwable that serves as a higher-level exception wrapping the validation failure. If null, a new `ValidationFailedException` is created.
 * @param cause The underlying cause of the validation failure. Can be null.
 * @return The non-null receiver if validation succeeds.
 * @throws ValidationFailedException If the receiver is null, an exception with detailed context is thrown.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNotNull(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is null", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameterName, message ?: "is null", cause?.invoke()))
    return this
}
/**
 * Validates that the receiver is not null. If the receiver is null, a `ValidationFailedException`
 * is thrown with optional details such as a custom message, the relevant callable (function),
 * parameter, or causes.
 *
 * @param callable the optional [KFunction] associated with this validation
 * @param parameter the optional [KParameter] representing the parameter being validated
 * @param message an optional custom error message to include if validation fails
 * @param causeOf an optional pre-existing exception, which will be used as the cause for the thrown exception
 * @param cause an optional secondary throwable to chain as the cause of the exception
 * @return the receiver object if validation passes
 * @throws ValidationFailedException if the receiver is null
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNotNull(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is null", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callable, parameter, message ?: "is null", cause?.invoke()))
    return this
}
/**
 * Validates that the receiver is not null. If the receiver is null, it throws a `ValidationFailedException`.
 *
 * @param callableName The name of the callable (e.g., function or method) where the validation is performed.
 * @param parameterName The name of the parameter being validated, if applicable.
 * @param message An optional custom message providing additional context for the validation failure.
 * @param causeOf An optional existing throwable that caused this validation to fail.
 * @param cause An optional underlying cause of the exception.
 * @return The receiver, if it is not null.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNotNull(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is null", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameterName, message ?: "is null", cause?.invoke()))
    return this
}
/**
 * Validates that the current object is not null.
 *
 * This method checks if the current receiver (`this`) is null and throws a `ValidationFailedException`
 * if it is. The exception can include details such as the callable name, parameter information, an optional
 * error message, and a cause.
 *
 * @param callableName The name of the callable performing the validation, or null if not specified.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional message to include in the exception if validation fails, or null for a default message.
 * @param causeOf An optional `Throwable` to use as the primary cause for the exception, or null if not specified.
 * @param cause An optional additional cause for the exception, or null if not specified.
 * @return The current receiver (`this`) if the validation passes.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.validateNotNull(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is null", cause?.invoke()) else causeOf().initCause(ValidationFailedException(callableName, parameter, message ?: "is null", cause?.invoke()))
    return this
}

/**
 * Validates that the current value falls within the specified range. If the value is not
 * within the range, a `ValueOutOfRangeException` is thrown.
 *
 * @param range the range within which the value must fall.
 * @param causeOf an optional supplier for a throwable to be thrown, augmenting the default exception.
 * @param cause an optional supplier for a cause to be added to the thrown exception.
 * @return the current value if it is within the specified range.
 * @throws ValueOutOfRangeException if the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: ClosedRange<T>, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is within the specified range and throws a validation exception if it is not.
 *
 * @param range the range within which the current value must fall.
 * @param causeOf an optional supplier for the exception that may wrap the validation failure.
 * @param cause an optional supplier for the underlying cause of the validation failure.
 * @param lazyMessage a supplier that provides the error message for the validation failure.
 * @return the current value if it is within the specified range.
 * @throws ValueOutOfRangeException if the current value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: ClosedRange<T>, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is within the specified range. If the validation fails, a
 * [ValueOutOfRangeException] is thrown with the provided property, variable name, custom message, or cause.
 *
 * @param range The closed range within which the value should lie.
 * @param property The property associated with the validation failure. Can be null if not applicable.
 * @param variableName An optional name of the variable involved in the validation. Used to provide detailed error messages.
 * @param message An optional custom message describing the validation failure. Defaults to a generic message if not provided.
 * @param causeOf An optional supplier for the throwable exception to be used as the cause of the validation failure.
 * @param cause An optional supplier for an additional throwable cause to be included with the validation failure.
 * @return The validated value, if it lies within the specified range.
 * @throws ValueOutOfRangeException if the value is outside the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: ClosedRange<T>, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the receiver value is within the specified range.
 *
 * If the value is not within the provided range, a `ValueOutOfRangeException` is thrown,
 * optionally including additional context such as the property, variable, message, and cause.
 *
 * @param range the range within which the value must reside
 * @param property the primary property associated with the validation, or null if not applicable
 * @param variable an optional secondary property providing additional context, or null if not applicable
 * @param message an optional custom error message describing the validation failure
 * @param causeOf a supplier for the throwable to be used as the primary cause of the exception, or null if not specified
 * @param cause a supplier for the throwable to be used as a secondary cause of the exception, or null if not specified
 * @return the receiver value if it is within the specified range
 * @throws ValueOutOfRangeException if the receiver value is not within the specified range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: ClosedRange<T>, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is within the specified range. If the value is outside the range,
 * a `ValueOutOfRangeException` is thrown with the provided details.
 *
 * @param range The closed range within which the value must fall.
 * @param callable The Kotlin function associated with the validation, used to provide context in the exception. Can be null.
 * @param parameterName The name of the parameter being validated, used for better error reporting. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to a message indicating the value is outside the range.
 * @param causeOf A supplier for a throwable that can act as the primary cause of the validation failure. If provided, this will wrap the `ValueOutOfRangeException` as its cause
 * . Can be null.
 * @param cause A supplier for a throwable to be used as the cause of the `ValueOutOfRangeException`. Can be null.
 * @return The validated value if it is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: ClosedRange<T>, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is within the specified range. If the value is not within the range,
 * a [ValueOutOfRangeException] is thrown with detailed information about the validation context,
 * including the callable, parameter, optional custom message, and cause.
 *
 * @param range the [ClosedRange] within which the value must fall
 * @param callable an optional reference to the [KFunction] related to the validation
 * @param parameter an optional [KParameter] representing the parameter involved in the validation
 * @param message an optional custom message providing additional context about the validation failure
 * @param causeOf an optional supplier for the primary cause of the validation failure
 * @param cause an optional supplier for an additional, underlying cause of the validation failure
 * @return the validated value if it falls within the specified range
 * @throws ValueOutOfRangeException if the value is not within the range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: ClosedRange<T>, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the value is within the specified range. If the value is not in the range,
 * a `ValueOutOfRangeException` is thrown. The exception can include information about the
 * callable name, parameter name, and an optional custom message or cause(s).
 *
 * @param range The range of valid values against which the current value is validated.
 * @param callableName The name of the callable (e.g., function or method) associated with the validation.
 * @param parameterName An optional name of the parameter being validated.
 * @param message An optional custom message describing the validation failure.
 * @param causeOf An optional supplier for a throwable cause to be thrown as the primary exception.
 *                If provided, its result will be used as the primary exception and augmented
 *                with the validation failure details.
 * @param cause An optional supplier for a throwable that can be used as the underlying cause
 *              of the primary exception.
 * @return The validated value, if it is within the specified range.
 * @throws ValueOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: ClosedRange<T>, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the value is within the specified range. If the value is not in the range,
 * a ValueOutOfRangeException is thrown.
 *
 * @param range The inclusive range of valid values for this validation.
 * @param callableName The name of the callable (function or property) where the validation is taking place,
 *                     or null if not provided.
 * @param parameter The parameter representing the value being validated, or null if not applicable.
 * @param message An optional error message providing additional details about the validation failure.
 * @param causeOf An optional supplier for a throwable cause that overrides the default exception.
 * @param cause An optional supplier for the root cause of the exception, used if causeOf is null.
 * @return The validated value, if it is within the specified range.
 * @throws ValueOutOfRangeException If the value is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: ClosedRange<T>, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the current value lies within the specified open-ended range.
 * If the value is not within the range, a `ValueOutOfRangeException` is thrown,
 * optionally initialized with a provided cause or exception supplier for detailed context.
 *
 * @param range The open-ended range to validate the value against.
 * @param causeOf A supplier that generates a throwable for additional context as the root cause, if the validation fails.
 * @param cause A supplier that generates a throwable to specify the secondary cause, if the validation fails.
 * @return The current value (`this`) if it is within the specified range.
 * @throws ValueOutOfRangeException if the value is not within the range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: OpenEndRange<T>, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException("Value is not in range $range.", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException("Value is not in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current value is within the specified open-ended range.
 * Throws a ValueOutOfRangeException if the validation fails.
 *
 * @param range the open-ended range to check the value against.
 * @param causeOf an optional supplier of a throwable to be used as the primary cause
 *                for validation failure. Defaults to null.
 * @param cause an optional supplier of a throwable to be associated as the secondary
 *              cause for validation failure. Defaults to null.
 * @param lazyMessage a supplier for the error message to be included in the exception
 *                    if validation fails.
 * @return the current value if it passes the validation.
 * @throws ValueOutOfRangeException if the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: OpenEndRange<T>, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking value is within the specified open-ended range.
 * If the value is not within the range, throws a [ValueOutOfRangeException].
 *
 * @param range The open-ended range to validate against.
 * @param property The property associated with the validation context. Can be null if not applicable.
 * @param variableName The name of the variable being validated. Can be null if not applicable.
 * @param message An optional custom message to be used in the exception if validation fails.
 * @param causeOf A supplier for the root cause of the validation failure. Can be null.
 * @param cause A supplier for an optional throwable cause to attach to the exception. Can be null.
 * @return The original value if validation succeeds.
 * @throws ValueOutOfRangeException If the value is outside the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: OpenEndRange<T>, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(property, variableName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is within the specified open-ended range. If the value is not within
 * the range, a `ValueOutOfRangeException` is thrown.
 *
 * @param range The open-ended range of acceptable values.
 * @param property The primary property associated with the validation, or null if not specified.
 * @param variable An optional secondary property for additional context in case of validation failure, or null.
 * @param message An optional custom error message for the validation failure, or null.
 * @param causeOf A supplier that provides the throwable to be used as the main cause of the exception, or null.
 * @param cause A supplier that provides an additional throwable cause to be associated with the exception, or null.
 * @return The value being validated, if it is within the specified range.
 * @throws ValueOutOfRangeException If the value does not fall within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: OpenEndRange<T>, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(property, variable, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current value falls within the specified open-ended range.
 * If the value is not within the range, a `ValueOutOfRangeException` is thrown.
 *
 * @param T The type of the value being validated, which must implement `Comparable`.
 * @param range The open-ended range within which the value is expected to fall.
 * @param callable An optional reference to the Kotlin function (`KFunction`) related to this validation.
 * @param parameterName The name of the parameter associated with the validation, if applicable.
 * @param message An optional custom message to be used in the exception if validation fails.
 * @param causeOf A supplier that provides the underlying cause for the exception if validation fails.
 * @param cause A supplier that provides an additional cause for the exception, if needed.
 * @return The current value if it falls within the specified range.
 * @throws ValueOutOfRangeException If the value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: OpenEndRange<T>, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callable, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking value lies within the specified range. If the value is not within the range, a
 * `ValueOutOfRangeException` is thrown.
 *
 * @param T the type of the value being validated, which must implement `Comparable<T>`
 * @param range the range of acceptable values; the invoking value must lie within this range
 * @param callable an optional `KFunction` representing the context of the validation
 * @param parameter an optional `KParameter` associated with the value being validated
 * @param message an optional message providing additional context about the validation failure
 * @param causeOf an optional supplier for the primary exception to be thrown, allowing for custom exception creation
 * @param cause an optional supplier for the underlying cause of the validation failure
 * @return the validated value if it lies within the specified range
 * @throws ValueOutOfRangeException if the invoking value does not lie within the specified range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: OpenEndRange<T>, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callable, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the given object is within the specified open-ended range.
 * If the object is not within the range, a `ValueOutOfRangeException` is thrown.
 *
 * @param T The type of the object being validated, which must implement `Comparable<T>`.
 * @param range The open-ended range within which the object should fall.
 * @param callableName The name of the callable (e.g., function or method) performing the validation, or null if not applicable.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom message providing additional details in case of validation failure.
 * @param causeOf A supplier for a custom throwable to wrap the validation exception, if provided.
 * @param cause A supplier for the underlying cause throwable, if applicable.
 * @return The object being validated, if it is within the specified range.
 * @throws ValueOutOfRangeException If the object is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: OpenEndRange<T>, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates whether the current value lies within the specified range. If the value is not within the range,
 * this method throws a `ValueOutOfRangeException` with relevant details including the callable name,
 * parameter, optional message, and optional causes.
 *
 * @param T The type of the value being validated, constrained to be `Comparable`.
 * @param range The `OpenEndRange` within which the value is expected to fall.
 * @param callableName The name of the callable (e.g., function or property) related to the validation, or null if not applicable.
 * @param parameter The `KParameter` instance representing the parameter being validated, or null if not applicable.
 * @param message An optional error message providing additional information about the validation failure.
 * @param causeOf A supplier for a base `Throwable` cause that triggered the issue, or null if not applicable.
 * @param cause An additional `Throwable` supplier representing an underlying issue, or null if not applicable.
 * @return The validated value, if it lies within the specified range.
 * @throws ValueOutOfRangeException If the current value is not within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateIn(range: OpenEndRange<T>, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this !in range) throw if (causeOf == null) ValueOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callableName, parameter, message ?: "is not in range $range", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the value is present within the specified iterable. If the value is not found,
 * an exception is thrown with an optional cause or error-producing logic.
 *
 * @param iterable The iterable collection to validate the value against.
 * @param causeOf An optional supplier for a throwable to be used as the primary exception.
 * @param cause An optional supplier for a throwable to be used as the root cause of the exception.
 * @return The validated value if it exists in the iterable.
 * @throws ValidationFailedException if the value is not found in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateIn(iterable: Iterable<E>, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this !in iterable) throw if (causeOf == null) ValidationFailedException("Value is not in $iterable.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is not in $iterable.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current element is present in the given iterable collection.
 * If the element is not found, this method throws a `ValidationFailedException` with the
 * provided lazy message and optional cause(s).
 *
 * @param iterable The collection of elements against which the current element is validated.
 * @param causeOf An optional supplier for the primary throwable to be used as the cause of the validation exception.
 * @param cause An optional supplier for a secondary throwable to provide additional context for the validation exception.
 * @param lazyMessage A supplier for the validation failure message, evaluated only if the validation fails.
 * @return The current element, if validation is successful.
 * @throws ValidationFailedException if the current element is not present in the given iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateIn(iterable: Iterable<E>, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null, lazyMessage: Transformer<E, Any>): E {
    if (this !in iterable) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is present in the provided iterable collection.
 *
 * If the object is not found in the iterable, a `ValidationFailedException` is thrown. The exception
 * can optionally include details such as the associated property, variable name, a custom message, and
 * a cause for the validation failure.
 *
 * @param iterable The iterable collection to check against.
 * @param property The property associated with the validation context, if applicable.
 * @param variableName The name of the variable being validated, used for more descriptive messages. Optional.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to a generic message.
 * @param causeOf Supplier of a `Throwable` to be used as the primary cause for the exception, if applicable. Optional.
 * @param cause Supplier of a secondary `Throwable` to be attached, if applicable. Optional.
 * @return Returns the current object if it is present in the iterable.
 * @throws ValidationFailedException If the object is not found in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateIn(iterable: Iterable<E>, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this !in iterable) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is not in $iterable", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object exists within the given `iterable`.
 * If the object is not found, a `ValidationFailedException` is thrown with the specified details.
 *
 * @param iterable the collection of elements to check the current object against
 * @param property the main `KProperty` associated with the validation context, or null if not applicable
 * @param variable an optional secondary `KProperty` providing additional validation context, or null if not applicable
 * @param message an optional message to include in the exception if validation fails; defaults to null
 * @param causeOf an optional supplier for a custom throwable to serve as the main exception, or null if not specified
 * @param cause an optional supplier for a throwable to serve as the cause of the exception, or null if not specified
 * @return the current object if validation succeeds
 * @throws ValidationFailedException if the object is not found in the provided `iterable`
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateIn(iterable: Iterable<E>, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this !in iterable) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is not in $iterable", cause?.invoke(this)))
    return this
}
/**
 * Validates that the caller object is present within the given iterable. If the object is not
 * found in the iterable, a `ValidationFailedException` is thrown.
 *
 * @param iterable The iterable collection to check against.
 * @param callable The Kotlin function (`KFunction`) to which the validation is related. This can be null.
 * @param parameterName The name of the parameter being validated. This can be null.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to null.
 * @param causeOf A supplier for a throwable that should act as the primary cause of the failure. Defaults to null.
 * @param cause A supplier for the underlying cause of the exception. Defaults to null.
 * @return The validated object if the validation succeeds.
 * @throws ValidationFailedException If the object is not present in the given iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateIn(iterable: Iterable<E>, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this !in iterable) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is not in $iterable", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current element is contained within the specified iterable. If the element is not found
 * in the iterable, a `ValidationFailedException` is thrown.
 *
 * @param iterable the iterable collection against which the current element is validated
 * @param callable the Kotlin function (`KFunction`) related to this validation, or null if not applicable
 * @param parameter the function parameter (`KParameter`) involved in the validation, or null if not applicable
 * @param message an optional custom validation failure message, or null if no message is provided
 * @param causeOf a supplier for a throwable to be used as the primary cause of the validation failure, or null
 * @param cause a supplier for a secondary throwable cause of the failure, or null
 * @return the validated element if it is present in the iterable
 * @throws ValidationFailedException if the element is not found within the iterable
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateIn(iterable: Iterable<E>, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this !in iterable) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is not in $iterable", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current element exists in the provided iterable. If not, throws a ValidationFailedException.
 *
 * @param iterable the collection of elements to validate against
 * @param callableName the name of the callable, such as a function or method, associated with the validation
 * @param parameterName the name of the parameter being validated (optional)
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf a supplier that provides the throwable representing the primary cause of validation failure (optional)
 * @param cause a supplier that provides an additional throwable cause chained to the exception (optional)
 * @return the validated element if it exists in the iterable
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateIn(iterable: Iterable<E>, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this !in iterable) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is not in $iterable", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is contained within the given iterable.
 * If the object is not found in the iterable, a `ValidationFailedException` is thrown.
 *
 * @param iterable The collection of elements to validate against.
 * @param callableName The name of the callable (e.g., function or property) where this validation occurs, or null if not specified.
 * @param parameter The parameter associated with this validation, represented as a `KParameter`, or null if not applicable.
 * @param message An optional error message providing additional context on why the validation failed.
 * @param causeOf An optional supplier for a custom throwable, which will be used to wrap the `ValidationFailedException`.
 * @param cause An optional supplier for the cause of the `ValidationFailedException`.
 * @return The current object if validation passes, allowing for method chaining.
 * @throws ValidationFailedException If the object is not found in the specified iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateIn(iterable: Iterable<E>, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this !in iterable) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is not in $iterable", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current value is not within the specified range. If the value
 * is within the range, a `ValueOutOfRangeException` is thrown.
 *
 * @param range the closed range to validate against
 * @param causeOf a supplier that provides a throwable to be used as the primary cause of the exception
 * @param cause an optional supplier that provides an additional throwable cause
 * @return the current value if the validation succeeds
 * @throws ValueOutOfRangeException if the value is within the specified range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: ClosedRange<T>, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within a specified range. If the value
 * is inside the range, an exception is thrown with optional contextual information.
 *
 * @param range The range to validate against.
 * @param causeOf A supplier to provide a throwable that will be thrown as the primary cause of the exception.
 *                If null, a default exception will be used.
 * @param cause A supplier to provide a throwable cause for the exception. Can be null if no cause is provided.
 * @param lazyMessage A supplier for the error message that provides additional context upon failure.
 * @return The current value if validation is successful (i.e., the value is not within the range).
 * @throws ValueOutOfRangeException If the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: ClosedRange<T>, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the value of the current object is not within the specified range.
 * If the value is within the range, throws a [ValueOutOfRangeException].
 *
 * @param range The closed range against which the validation is performed.
 * @param property An optional [KProperty] associated with the value being validated.
 *                 This is used to provide additional context in the error message.
 * @param variableName An optional name of the variable being validated.
 *                     Included in the error message if supplied.
 * @param message An optional custom error message to include in the exception if validation fails.
 * @param causeOf An optional supplier for the throwable cause in case of validation failure.
 *                If provided, it is used to throw a customized exception.
 * @param cause An optional supplier for a throwable that serves as the cause of the exception.
 *              Used when `causeOf` is not provided.
 * @return The current value if the validation passes.
 * @throws ValueOutOfRangeException if the current value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: ClosedRange<T>, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified range. If the value is within the range,
 * a `ValueOutOfRangeException` is thrown.
 *
 * @param range the range to validate the value against
 * @param property the primary `KProperty` associated with the validation, or null if not applicable
 * @param variable an optional secondary `KProperty` providing additional context, or null if not specified
 * @param message an optional custom message to be used in the exception if validation fails
 * @param causeOf a supplier for a custom cause exception, which will be used if validation fails and a specific exception is required
 * @param cause a supplier for the underlying cause of the exception, or null if there is no specific cause
 * @return the current value if it is not within the range
 * @throws ValueOutOfRangeException if the current value is within the specified range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: ClosedRange<T>, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified range. If the value is within the range,
 * it throws a `ValueOutOfRangeException` with the provided details or defaults.
 *
 * @param range The range to check the current value against.
 * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can be null.
 * @param parameterName The name of the parameter associated with the validation. Can be null.
 * @param message An optional custom message describing the validation failure. Default is a generated message.
 * @param causeOf A supplier for a specific `Throwable` that should be thrown if validation fails. Can be null.
 * @param cause A supplier for the underlying cause of the exception if validation fails. Can be null.
 * @return The current value, if the validation passes.
 * @throws ValueOutOfRangeException If the current value is found within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: ClosedRange<T>, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified range. If the value is within the range,
 * a [ValueOutOfRangeException] is thrown with the provided details.
 *
 * @param range the [ClosedRange] against which the current value is validated
 * @param callable the [KFunction] reference associated with the validation, or null if not applicable
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable
 * @param message an optional custom error message to provide additional context
 * @param causeOf a supplier for the root cause of the exception, or null if not applicable
 * @param cause a supplier for the cause of the exception, or null if not applicable
 * @return the original value if it is not within the range
 * @throws ValueOutOfRangeException if the current value is within the specified range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: ClosedRange<T>, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking object is not within the specified range. If the value is within the range,
 * a `ValueOutOfRangeException` is thrown.
 *
 * @param range The range of values to check against.
 * @param callableName The name of the callable (e.g., function or method) being validated.
 * @param parameterName The name of the parameter whose value is being validated (optional).
 * @param message A custom message to include in the exception if validation fails (optional).
 * @param causeOf A supplier for an alternative exception to throw in case of failure (optional).
 * @param cause A supplier for the underlying cause of the exception if validation fails (optional).
 * @return The original value if it is not within the specified range.
 * @throws ValueOutOfRangeException If the value is within the specified range and no alternative exception is provided.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: ClosedRange<T>, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified range and throws a `ValueOutOfRangeException` if it is.
 *
 * @param range The closed range to validate against.
 * @param callableName The name of the callable (e.g., function or property) where validation is being performed, or null if not specified.
 * @param parameter The parameter (as a `KParameter` instance) that is being validated, or null if not applicable.
 * @param message An optional custom error message providing details about the validation failure. Defaults to null.
 * @param causeOf An optional supplier for a throwable that should be used as the primary cause of the exception, or null if not specified.
 * @param cause An optional supplier for a secondary throwable cause, or null if not specified.
 * @return The current value if it is not within the specified range.
 * @throws ValueOutOfRangeException If the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: ClosedRange<T>, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified open-end range.
 * If the value is within the range, a `ValueOutOfRangeException` is thrown.
 *
 * @param range The open-end range to check the value against.
 * @param causeOf A supplier for an optional throwable to initialize as the cause of the exception. Can be null.
 * @param cause A supplier for an additional throwable cause to attach to the exception. Can be null.
 * @return The current value if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: OpenEndRange<T>, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException("Value is in range $range.", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException("Value is in range $range.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified open-ended range.
 * Throws a `ValueOutOfRangeException` if the value is found within the range.
 *
 * @param range The open-ended range to check the value against.
 * @param causeOf A supplier that provides the primary cause of the exception, if applicable. Defaults to null.
 * @param cause A supplier for any underlying cause of the exception. Defaults to null.
 * @param lazyMessage A supplier for the error message used when the validation fails.
 * @return The original value if it is not within the specified range.
 * @throws ValueOutOfRangeException If the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: OpenEndRange<T>, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified open-ended range.
 * If the value is within the given range, a `ValueOutOfRangeException` is thrown.
 *
 * @param range The open-ended range to check against.
 * @param property The property associated with this value, or null if not applicable.
 * @param variableName The name of the variable, or null if not provided.
 * @param message An optional custom validation failure message. Defaults to a predefined message.
 * @param causeOf A supplier for the primary throwable that caused the validation failure, or null if not applicable.
 * @param cause A supplier for the secondary throwable cause to be attached, or null if not applicable.
 * @return The original value if it is not within the specified range.
 * @throws ValueOutOfRangeException If the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: OpenEndRange<T>, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(property, variableName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified open-ended range.
 * If the value lies within the range, a `ValueOutOfRangeException` is thrown.
 *
 * @param range the `OpenEndRange` specifying the range to compare against
 * @param property the primary `KProperty` to associate with the validation, or null if not specified
 * @param variable an optional secondary `KProperty` to provide additional context, or null if not specified
 * @param message an optional message supplying additional details about the validation failure, defaulting to null
 * @param causeOf an optional supplier for the primary throwable cause, defaulting to null
 * @param cause an optional supplier for the supplementary throwable cause, defaulting to null
 * @return the current value if it is not within the specified range
 * @throws ValueOutOfRangeException if the value is within the specified range
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: OpenEndRange<T>, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(property, variable, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified open-end range. If the validation fails,
 * a `ValueOutOfRangeException` is thrown.
 *
 * @param T The type of the value being validated, which must implement `Comparable<T>`.
 * @param range The open-end range to validate against. The value should not be contained within this range.
 * @param callable The `KFunction` representing the function or callable the validation is related to. Can be null.
 * @param parameterName The name of the parameter being validated. Useful for constructing error messages. Can be null.
 * @param message An optional custom message for the exception. If null, a default message is used.
 * @param causeOf An optional supplier for a custom exception to be thrown instead of the default `ValueOutOfRangeException`.
 * @param cause An optional supplier for the underlying cause of the thrown exception.
 *
 * @return Returns the current value if validation passes.
 * @throws ValueOutOfRangeException if the value is within the specified range and no custom exception is supplied via `causeOf`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: OpenEndRange<T>, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callable, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified open-ended range.
 * If the value is found within the range, a `ValueOutOfRangeException` will be thrown.
 *
 * @param T the type of the value being validated, which must implement [Comparable].
 * @param range the [OpenEndRange] that defines the range of values to validate against.
 * @param callable the [KFunction] representing the context of the validation check, or null if not applicable.
 * @param parameter the [KParameter] representing the parameter being validated, or null if not applicable.
 * @param message an optional custom error message to include in the exception if validation fails, or null.
 * @param causeOf an optional supplier for the initial cause of the validation exception, or null if not applicable.
 * @param cause an optional supplier for the subsequent cause of the validation exception, or null if not applicable.
 * @return the current value if validation passes without exception.
 *
 * @throws ValueOutOfRangeException if the current value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: OpenEndRange<T>, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callable, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the value is not within the specified range. Throws a [ValueOutOfRangeException]
 * if the value is within the range. The exception can include additional metadata such as the callable
 * name, parameter name, a custom message, and optional causes.
 *
 * @param range The range against which the value is validated. The range is open-ended.
 * @param callableName The name of the callable (e.g., function or method) associated with the validation failure, or null if not specified.
 * @param parameterName The name of the parameter being validated, or null if not specified.
 * @param message An optional custom message providing additional context about the validation failure, or null if not specified.
 * @param causeOf A supplier for the primary cause of the failure, or null if not specified. This is used to construct the exception.
 * @param cause A supplier for additional cause information, or null if not specified. This is used as a linked cause in the exception.
 * @return The original value if validation passes successfully.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: OpenEndRange<T>, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(callableName, parameterName, message ?: "is not in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callableName, parameterName, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is not within the specified range.
 * If the value is within the range, a `ValueOutOfRangeException` is thrown.
 *
 * @param T The type of the value being validated. It must implement `Comparable`.
 * @param range The `OpenEndRange` against which the value is validated. The current value must not fall within this range.
 * @param callableName The name of the callable (e.g., function or property) where validation is performed. This is used in the exception to provide context.
 * @param parameter The `KParameter` instance representing the parameter associated with the validation failure. This is used in exception construction for additional context.
 * @param message An optional custom message that provides more details about the validation context or reason for failure.
 * @param causeOf An optional supplier for the primary cause of the exception. When provided, the exception returned by this supplier will be used instead of creating a new exception
 * .
 * @param cause An optional supplier for the underlying exception cause. This is used as the cause of the thrown `ValueOutOfRangeException`, if applicable.
 * @return The current value if it successfully passes the validation (i.e., is not within the specified range).
 * @throws ValueOutOfRangeException If the value is within the specified range.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateNotIn(range: OpenEndRange<T>, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this in range) throw if (causeOf == null) ValueOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)) else causeOf(this).initCause(ValueOutOfRangeException(callableName, parameter, message ?: "is in range $range", cause?.invoke(this)))
    return this
}
/**
 * Validates that the calling element is not present in the provided iterable.
 * If the element exists within the iterable, an exception is thrown.
 *
 * @param iterable The collection of elements to check against.
 * @param causeOf An optional supplier for the root cause of the exception to be thrown.
 * @param cause An optional supplier for additional context cause to include in the exception.
 * @return The calling element if validation succeeds without throwing an exception.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateNotIn(iterable: Iterable<E>, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this in iterable) throw if (causeOf == null) ValidationFailedException("Value is in $iterable.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is in $iterable.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not present in the provided iterable.
 * If the object is found in the iterable, an exception is thrown with the
 * supplied error message or cause.
 *
 * @param iterable The iterable collection against which the object is being validated.
 * @param causeOf A supplier for a custom throwable to be thrown if the validation fails.
 *                This can be null.
 * @param cause A supplier for an additional throwable cause to be attached. This can be null.
 * @param lazyMessage A supplier for a custom message to include in the exception
 *                    if the validation fails.
 * @return The current object if it is not found in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateNotIn(iterable: Iterable<E>, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null, lazyMessage: Transformer<E, Any>): E {
    if (this in iterable) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current instance is not contained within the specified iterable.
 * If the instance is found within the iterable, a [ValidationFailedException] is thrown.
 *
 * @param iterable The collection to check against for the presence of the instance.
 * @param property Optional metadata about the property being validated.
 * @param variableName Optional name of the variable involved in validation for improved debugging.
 * @param message Custom validation failure message. Defaults to a message indicating the instance is in the iterable.
 * @param causeOf A supplier for a custom throwable to be thrown as the root cause, if validation fails.
 * @param cause A supplier for a cause throwable to be attached to the generated exception (if applicable).
 * @return The original instance if validation passes.
 * @throws ValidationFailedException if the instance is found within the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateNotIn(iterable: Iterable<E>, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this in iterable) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is in $iterable", cause?.invoke(this)))
    return this
}
/**
 * Validates that the receiver object is not contained within the specified iterable.
 * If the receiver is found in the iterable, a `ValidationFailedException` is thrown.
 *
 * @param iterable An iterable collection to check for the presence of the receiver.
 * @param property An optional KProperty associated with the main validation context, or null if not applicable.
 * @param variable An optional secondary KProperty that provides additional validation context, or null if not applicable.
 * @param message An optional custom message for the validation failure. If null, a default message is used.
 * @param causeOf An optional supplier of the primary cause exception to be thrown instead of the default exception.
 * @param cause An optional supplier for the underlying cause to be attached to the exception if thrown.
 * @return Returns the receiver instance if validation passes (i.e., it is not in the iterable).
 * @throws ValidationFailedException if the receiver object is found in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateNotIn(iterable: Iterable<E>, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this in iterable) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is in $iterable", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not contained in the provided iterable collection.
 * If the object is found in the iterable, a `ValidationFailedException` is thrown.
 *
 * @param iterable The iterable collection to check against.
 * @param callable The Kotlin function (`KFunction`) associated with the validation context. Can be null.
 * @param parameterName The name of the parameter being validated. Can be null.
 * @param message An optional custom message to use when the validation fails. Default is null.
 * @param causeOf A supplier for a custom exception to throw when validation fails. Default is null.
 * @param cause A supplier for the cause of the exception. Default is null.
 * @return Returns the object being validated if it is not found in the iterable.
 * @throws ValidationFailedException if the object is found in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateNotIn(iterable: Iterable<E>, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this in iterable) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is in $iterable", cause?.invoke(this)))
    return this
}
/**
 * Validates that the object is not present in the provided iterable. If the object is found in the iterable,
 * throws a `ValidationFailedException` with detailed information.
 *
 * @param iterable the collection of elements to validate against
 * @param callable the [KFunction] related to the validation, or null if not applicable
 * @param parameter the [KParameter] representing the parameter involved in the validation, or null if not applicable
 * @param message an optional message providing additional context about the validation failure, defaults to null
 * @param causeOf a supplier for the primary exception to be thrown, defaults to null
 * @param cause a supplier for the underlying cause of the exception, defaults to null
 * @return the validated object if it is not in the iterable
 * @throws ValidationFailedException if the object is found in the iterable
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateNotIn(iterable: Iterable<E>, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this in iterable) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is in $iterable", cause?.invoke(this)))
    return this
}
/**
 * Validates that the receiver object is not contained within the specified `iterable`. If the receiver is found in
 * the `iterable`, a `ValidationFailedException` is thrown with the provided details.
 *
 * @param iterable the collection of elements to check against
 * @param callableName the name of the callable (e.g., function or method) associated with this validation
 * @param parameterName the name of the parameter being validated, or null if not applicable
 * @param message an optional custom message describing the validation failure, or null for a default message
 * @param causeOf a supplier for the exception to be thrown, which may include additional details or logic
 * @param cause a supplier for the underlying cause of the exception, or null if no specific cause is provided
 * @return the receiver object itself if validation passes without throwing an exception
 * @throws ValidationFailedException if the receiver object is found within the specified `iterable`
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateNotIn(iterable: Iterable<E>, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this in iterable) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is in $iterable", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is not present in the specified iterable.
 * If the object is found in the iterable, a `ValidationFailedException` is thrown.
 *
 * @param iterable The iterable collection to check against.
 * @param callableName The name of the callable (e.g., function or property) where the validation is performed, or null.
 * @param parameter The parameter associated with the validation, or null.
 * @param message An optional error message providing details about the validation failure.
 * @param causeOf An optional supplier providing a custom `Throwable` to be used as the exception, instead of the default.
 * @param cause An optional supplier providing the underlying cause of the exception.
 * @return The current object (`this`) if it is not found in the iterable.
 * @throws ValidationFailedException If the object is found in the iterable.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <E> E.validateNotIn(iterable: Iterable<E>, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<E, Throwable>? = null, cause: Transformer<E, Throwable>? = null): E {
    if (this in iterable) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is in $iterable", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is in $iterable", cause?.invoke(this)))
    return this
}

/**
 * Validates that the current value is greater than or equal to the specified value.
 * If the validation fails, an exception is thrown.
 *
 * @param other The value to compare against.
 * @param causeOf A supplier for the exception to be thrown in case of validation failure,
 *                wrapping additional context or custom logic for the exception. Optional.
 * @param cause A supplier for the underlying cause of the validation failure. Optional.
 * @return The current value if the validation succeeds.
 * @throws ValidationFailedException If the current value is less than the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterOrEqualThan(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this < other) throw if (causeOf == null) ValidationFailedException("Value is in lower than $other.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is in lower than $other.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than or equal to the specified value.
 * If this condition is not met, a `ValidationFailedException` is thrown.
 *
 * @param other The value to compare against.
 * @param causeOf An optional supplier for a custom throwable to be used as the primary cause.
 * @param cause An optional supplier for a throwable to be used as the secondary cause.
 * @param lazyMessage A supplier for the detail message to be used in the exception.
 * @return The current value if the validation passes.
 * @throws ValidationFailedException If the current value is less than the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterOrEqualThan(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this < other) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than or equal to the specified [other] value.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param other The value to compare against. The current value must be greater than or equal to this value.
 * @param property Optional property metadata associated with the value being validated.
 * @param variableName Optional name of the variable involved in the validation. Included in the exception message if provided.
 * @param message Optional additional descriptive message to include in the exception if validation fails. Defaults to a generated message.
 * @param causeOf Optional supplier to generate a custom exception as the cause of the validation failure.
 * @param cause Optional supplier for a throwable that serves as the underlying cause of the validation failure.
 * @return The current value if the validation passes.
 * @throws ValidationFailedException If the validation fails because the current value is less than [other].
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterOrEqualThan(other: T, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this < other) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is lower than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is lower than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than or equal to a specified value.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param other the value to compare the current value against
 * @param property the primary property associated with the validation, providing context for the validation failure
 * @param variable an optional secondary property to provide additional context about the validation failure
 * @param message an optional custom message to include in the exception if the validation fails
 * @param causeOf an optional supplier for the root cause of the exception
 * @param cause an optional supplier for the specific cause of the validation failure
 * @return the current value if the validation passes
 * @throws ValidationFailedException if the current value is less than the specified value
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterOrEqualThan(other: T, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this < other) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is lower than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is lower than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than or equal to the specified value. If the validation fails, an exception is thrown.
 *
 * @param other The value to compare against. The current value must be greater than or equal to this value.
 * @param callable The Kotlin function (`KFunction`) associated with the validation. Can be null if not applicable.
 * @param parameterName The name of the parameter associated with the validation. Can be null if not applicable.
 * @param message An optional custom error message to use if the validation fails. Defaults to a generated message indicating the comparison failure.
 * @param causeOf An optional supplier that provides a `Throwable` representing the specific cause for the exception. Can be null.
 * @param cause An optional supplier that provides a `Throwable` as the root cause for the validation exception. Can be null.
 * @return The current value if it passes the validation.
 * @throws ValidationFailedException if the current value is less than the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterOrEqualThan(other: T, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this < other) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is lower than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is lower than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates if the current value is greater than or equal to the specified value.
 * If the current value is less than the specified value, throws a [ValidationFailedException].
 *
 * @param other The value to compare against.
 * @param callable The [KFunction] related to the validation, or null if not applicable.
 * @param parameter The [KParameter] representing the parameter involved in the validation, or null if not applicable.
 * @param message An optional message providing additional context about the validation failure.
 * @param causeOf A supplier for the exception to be thrown if validation fails, or null to use the default exception.
 * @param cause A supplier for the underlying cause of the exception, or null if no cause is specified.
 * @return The current value if the validation succeeds.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterOrEqualThan(other: T, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this < other) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is lower than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is lower than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than or equal to the specified value. If the validation fails,
 * a `ValidationFailedException` is thrown with details about the failure.
 *
 * @param other the value to compare against
 * @param callableName the name of the callable (e.g., method or function) related to the validation
 * @param parameterName the name of the parameter that caused the validation failure, default is null
 * @param message an optional custom message providing details about the validation failure, default is null
 * @param causeOf a supplier for the primary cause exception, default is null
 * @param cause a supplier for any secondary cause exception, default is null
 * @return the current value if the validation passes
 * @throws ValidationFailedException if the current value is less than the specified value
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterOrEqualThan(other: T, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this < other) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is lower than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is lower than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than or equal to the specified value.
 * Throws a [ValidationFailedException] if the current value is less than the specified value.
 *
 * @param other The value to compare against.
 * @param callableName The name of the callable (e.g., function or property) where this validation takes place, or null if not specified.
 * @param parameter The parameter related to this validation, or null if not applicable.
 * @param message An optional custom error message to include in the exception.
 * @param causeOf An optional supplier for creating a custom cause of the exception, or null if not applicable.
 * @param cause An optional supplier for another throwable to act as the direct cause of the exception, or null if not applicable.
 * @return The current value if validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterOrEqualThan(other: T, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this < other) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is lower than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is lower than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than the specified value.
 * If the validation fails, a [ValidationFailedException] is thrown with the specified cause or message.
 *
 * @param other the value to compare against, which the current value must be greater than.
 * @param causeOf an optional supplier for a custom exception, initialized with the validation failure.
 * @param cause an optional supplier for the root cause of the validation failure.
 * @return the current value if it passes validation.
 * @throws ValidationFailedException if the current value is less than or equal to the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterThan(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this <= other) throw if (causeOf == null) ValidationFailedException("Value is in lower than or equal to $other.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is in lower than or equal to $other.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current instance is greater than the specified comparable value.
 * Throws a ValidationFailedException if the validation fails.
 *
 * @param other the value to compare against the current instance.
 * @param causeOf an optional supplier for a custom throwable used as the primary exception,
 *                which can be initialized with an underlying cause.
 * @param cause an optional supplier for the throwable used as the underlying cause
 *              of the exception, if one is thrown.
 * @param lazyMessage a supplier for the error message, which will be lazily evaluated
 *                    and provided to the exception if validation fails.
 * @return the current instance, if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterThan(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this <= other) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates if the current value is greater than the specified value.
 * Throws a [ValidationFailedException] if the current value is less than or equal to the provided value.
 *
 * @param other The value to compare against.
 * @param property The property associated with this validation. This can be null.
 * @param variableName The name of the variable being validated. Can be null.
 * @param message Optional message to include in the exception if validation fails.
 * @param causeOf A supplier for the throwable to be used as the cause of the exception. Can be null.
 * @param cause An optional supplier for the underlying cause of the exception. Can be null.
 * @return The current value if it passes the validation.
 * @throws ValidationFailedException If the current value is less than or equal to the provided value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterThan(other: T, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this <= other) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is lower than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is lower than or equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than the specified value.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param other the value to compare the current value against
 * @param property the primary property associated with the value being validated, or null if not applicable
 * @param variable an optional secondary property providing additional context, or null if not applicable
 * @param message an optional custom message for the validation failure, or null to use the default message
 * @param causeOf an optional supplier for the underlying cause of the validation failure, or null if not specified
 * @param cause an optional supplier for additional details about the cause, or null if not specified
 * @return the original value if validation succeeds
 * @throws ValidationFailedException if the current value is less than or equal to the specified value
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterThan(other: T, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this <= other) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is lower than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is lower than or equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than the specified `other` value.
 *
 * If the current value is less than or equal to `other`, a `ValidationFailedException` is thrown.
 *
 * @param other The value to compare the current value against.
 * @param callable The Kotlin function (`KFunction`) to which this validation is related. Can be null.
 * @param parameterName The name of the parameter being validated, typically from the callable. Can be null.
 * @param message An optional custom message to include in the exception if validation fails. Defaults to a standard message.
 * @param causeOf A supplier for the root cause to be used in the exception chain. Can be null.
 * @param cause A supplier for the direct cause to be set in the exception. Can be null.
 * @return The validated value if it passes the validation check.
 * @throws ValidationFailedException If the current value is less than or equal to the `other` value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterThan(other: T, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this <= other) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is lower than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is lower than or equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than the specified value.
 * If the validation fails, a ValidationFailedException is thrown.
 *
 * @param other the value to compare against
 * @param callable the related [KFunction], or null if not applicable
 * @param parameter the [KParameter] involved in the validation, or null if not applicable
 * @param message an optional custom message to include in the exception; defaults to null
 * @param causeOf a supplier for the exception cause, invoked if validation fails; defaults to null
 * @param cause an alternative supplier for the exception cause, invoked if validation fails and `causeOf` is null; defaults to null
 * @return the current value if the validation succeeds
 * @throws ValidationFailedException if the current value is not greater than the specified value
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterThan(other: T, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this <= other) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is lower than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is lower than or equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than the specified value. If the validation fails,
 * a [ValidationFailedException] is thrown with the provided details.
 *
 * @param other The value to compare against.
 * @param callableName The name of the callable (e.g., function or method) associated with the validation.
 * @param parameterName The name of the parameter being validated (optional).
 * @param message An optional custom validation failure message.
 * @param causeOf A supplier that provides the primary cause of the validation failure (optional).
 * @param cause A supplier that provides an additional underlying cause (optional).
 * @return The current value if validation succeeds.
 * @throws ValidationFailedException If the current value is not greater than the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterThan(other: T, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this <= other) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is lower than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is lower than or equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is greater than the specified `other` value.
 * If the validation fails (i.e., the current value is less than or equal to `other`),
 * throws a `ValidationFailedException` with the provided context details.
 *
 * @param other The value to compare against. The current value must be greater than this.
 * @param callableName The name of the callable (e.g., function or property) where this validation occurs. Can be null.
 * @param parameter The parameter related to this validation, represented as a `KParameter`. Can be null.
 * @param message An optional error message providing additional context for the validation failure. Defaults to null.
 * @param causeOf An optional supplier for a throwable that represents the primary cause of the failure. Defaults to null.
 * @param cause An optional supplier for a throwable that provides additional details about the validation failure. Defaults to null.
 * @return The current value if the validation succeeds (i.e., the value is greater than `other`).
 * @throws ValidationFailedException if the current value is less than or equal to `other`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateGreaterThan(other: T, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this <= other) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is lower than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is lower than or equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current instance is less than or equal to the provided value.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param other The value to compare against.
 * @param causeOf An optional supplier for a custom Throwable that will be thrown if validation fails.
 * @param cause An optional supplier for a Throwable to be used as the cause of the exception if validation fails.
 * @return The current instance if the validation is successful.
 * @throws ValidationFailedException if the current instance is greater than the provided value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerOrEqualThan(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this > other) throw if (causeOf == null) ValidationFailedException("Value is in greater than $other.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is in greater than $other.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is lower than or equal to the specified value.
 * If the validation fails, it throws a `ValidationFailedException`.
 *
 * @param other The value to compare against.
 * @param causeOf A supplier providing a throwable to be set as the primary error cause if validation fails.
 * @param cause A supplier providing an optional underlying cause for the failure.
 * @param lazyMessage A supplier providing the detail message for the validation failure exception.
 * @return The current value if it satisfies the validation condition.
 * @throws ValidationFailedException if the current value is greater than the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerOrEqualThan(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this > other) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is less than or equal to the specified `other` value.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param other The value to compare the current value against.
 * @param property The property associated with the validation; can be null if not applicable.
 * @param variableName Optional name of the variable involved in the validation; included in the exception message if provided.
 * @param message Additional message to include in the exception; defaults to a standard error message if not provided.
 * @param causeOf An optional supplier for a throwable that represents the cause of the exception.
 * @param cause An optional supplier for an additional underlying throwable.
 * @return The validated value if the validation succeeds.
 * @throws ValidationFailedException if the current value is greater than the `other` value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerOrEqualThan(other: T, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this > other) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is greater than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is greater than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is less than or equal to the specified value.
 * If the condition is not met, a `ValidationFailedException` is thrown.
 *
 * @param other the value to compare the current object against
 * @param property the main property associated with the validation, or null if not applicable
 * @param variable an optional secondary property providing additional context for the validation, or null if not applicable
 * @param message an optional message describing the validation failure, or null to use a default message
 * @param causeOf a supplier function for creating the root cause exception, or null if not specified
 * @param cause a supplier function for providing additional context for the exception, or null if not specified
 * @return the current value if the validation passes
 * @throws ValidationFailedException if the current value is greater than the specified `other` value
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerOrEqualThan(other: T, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this > other) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is greater than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is greater than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is less than or equal to the specified value.
 * Throws a `ValidationFailedException` if the validation fails.
 *
 * @param other The value to compare against. Validation passes if the current value is less than or equal to this value.
 * @param callable An optional reference to the Kotlin function (`KFunction`) associated with the validation error. Can be null.
 * @param parameterName An optional name of the parameter in the associated callable that caused the validation error. Can be null.
 * @param message An optional custom error message to describe the validation failure. Defaults to a message indicating the current value is greater than `other`.
 * @param causeOf An optional supplier for providing the root cause of the validation failure. Can be null.
 * @param cause An optional supplier for specifying additional `Throwable` information about the validation failure. Can be null.
 * @return The current value if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerOrEqualThan(other: T, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this > other) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is greater than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is greater than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that a value is lower than or equal to the specified threshold.
 * Throws a [ValidationFailedException] if the validation fails.
 *
 * @param other the value to compare against
 * @param callable the [KFunction] associated with the validation, or null if not applicable
 * @param parameter the [KParameter] representing the parameter involved in the validation, or null if not applicable
 * @param message an optional message providing additional context about the validation failure, defaults to null
 * @param causeOf an optional supplier for the underlying cause of the validation failure, defaults to null
 * @param cause an optional supplier for the additional throwables to include as a cause, defaults to null
 * @return the original value if validation passes
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerOrEqualThan(other: T, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this > other) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is greater than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is greater than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking value is lower than or equal to the specified value.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param other the value to compare against
 * @param callableName the name of the callable (e.g., function or method) performing the validation, or null
 * @param parameterName the name of the parameter being validated, or null
 * @param message an optional custom message to use for the validation failure, or null
 * @param causeOf a supplier for the underlying cause of the exception, or null
 * @param cause a supplier for an additional cause to be attached to the exception, or null
 * @return the invoking value if it passes the validation
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerOrEqualThan(other: T, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this > other) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is greater than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is greater than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is less than or equal to the specified value.
 *
 * Throws a [ValidationFailedException] if the validation fails.
 *
 * @param other The value to compare against.
 * @param callableName The name of the callable where validation is being performed, or null if not specified.
 * @param parameter The parameter related to the validation, or null if not applicable.
 * @param message An optional custom message for the exception if validation fails.
 * @param causeOf A supplier for the root cause of the exception if validation fails, or null if not provided.
 * @param cause A supplier for the exception to serve as the direct cause, or null if not provided.
 * @return The current value, if it satisfies the validation condition.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerOrEqualThan(other: T, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this > other) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is greater than $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is greater than $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is strictly less than the specified `other` value.
 * If the validation fails, a `ValidationFailedException` is thrown with additional details
 * optionally provided by `causeOf` or `cause`.
 *
 * @param other The value to compare against. The current value must be less than this.
 * @param causeOf An optional supplier for the throwable to be used as the primary cause if the validation fails.
 * @param cause An optional supplier for the throwable to be used as a secondary cause to provide additional context.
 * @return The current value if the validation passes.
 * @throws ValidationFailedException If the current value is greater than or equal to `other`.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerThan(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this >= other) throw if (causeOf == null) ValidationFailedException("Value is in greater than or equal to $other.", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException("Value is in greater than or equal to $other.", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is strictly less than the specified value.
 * If the validation fails, a `ValidationFailedException` is thrown.
 *
 * @param other the value to compare against.
 * @param causeOf a supplier for a throwable that provides additional contextual cause information.
 *                Can be null.
 * @param cause a supplier for a throwable to be set as the cause of the validation failure
 *              exception. Can be null.
 * @param lazyMessage a supplier for the lazy evaluation of the exception message when the
 *                    validation fails.
 * @return the current value if it satisfies the validation condition.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerThan(other: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this >= other) throw if (causeOf == null) ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is strictly less than the specified `other` value.
 * If the validation fails, throws a `ValidationFailedException`.
 *
 * @param other The value to compare against.
 * @param property The property associated with the validation. Can be null if not applicable.
 * @param variableName The optional name of the variable involved in the validation.
 * @param message An optional custom message to include in the exception if validation fails.
 * @param causeOf A supplier for a throwable instance to be used as the primary cause of the exception.
 * @param cause A supplier for an additional throwable instance to include as a secondary cause.
 * @return The current value if validation is successful.
 * @throws ValidationFailedException If the current value is greater than or equal to the `other` value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerThan(other: T, property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this >= other) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is greater than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variableName, message ?: "is greater than or equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is strictly lower than the specified value. If the validation fails,
 * a `ValidationFailedException` is thrown.
 *
 * @param other The value to compare the current value to.
 * @param property The primary property associated with this validation, or null if not specified.
 * @param variable An optional secondary property providing additional context, or null if not specified.
 * @param message An optional custom message for the exception if validation fails, or null to use the default message.
 * @param causeOf An optional supplier for the throwable to be used as the primary cause of the exception, or null if not specified.
 * @param cause An optional supplier for the underlying cause of the exception, or null if not specified.
 * @return The current value if the validation passes.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerThan(other: T, property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this >= other) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is greater than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(property, variable, message ?: "is greater than or equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is strictly less than the specified value. If the validation fails,
 * a `ValidationFailedException` is thrown.
 *
 * @param other The value to compare the current value against.
 * @param callable The Kotlin function (`KFunction`) related to the validation. Can be null.
 * @param parameterName The name of the parameter associated with the validation in the given callable. Can be null.
 * @param message An optional custom message for the validation failure. Default is null.
 * @param causeOf A supplier for the root cause of the validation failure, if available. Default is null.
 * @param cause A supplier for the exception that caused the validation failure. Default is null.
 * @return The current value if it successfully passes the validation.
 * @throws ValidationFailedException If the current value is greater than or equal to the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerThan(other: T, callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this >= other) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is greater than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameterName, message ?: "is greater than or equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is lower than the specified value. If the validation fails,
 * a `ValidationFailedException` is thrown.
 *
 * @param other The value to compare with. The current object must be less than this value.
 * @param callable The `KFunction` related to the validation, or null if not applicable.
 * @param parameter The `KParameter` representing the parameter involved in the validation, or null if not applicable.
 * @param message An optional custom error message to use if validation fails. Defaults to null.
 * @param causeOf A supplier function for the primary cause of the exception, or null if not applicable.
 * @param cause A supplier function for an additional underlying cause of the exception, or null if not applicable.
 * @return The current object if it satisfies the validation condition.
 * @throws ValidationFailedException If the current object is not lower than the specified value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerThan(other: T, callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this >= other) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is greater than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callable, parameter, message ?: "is greater than or equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is less than the specified [other] value.
 * If the validation fails, a [ValidationFailedException] is thrown.
 *
 * @param other The value to compare the current object against.
 * @param callableName The name of the callable (e.g., function or method) where the validation occurs.
 * @param parameterName An optional parameter name that caused the validation failure.
 * @param message An optional custom message providing additional details about the validation failure.
 * @param causeOf An optional supplier for the primary throwable cause of the failure.
 * @param cause An optional supplier for a secondary throwable cause of the failure, if any.
 * @return The current object if the validation succeeds.
 * @throws ValidationFailedException If the current value is not less than the [other] value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerThan(other: T, callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this >= other) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is greater than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameterName, message ?: "is greater than or equal to $other", cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value is strictly less than the specified value.
 *
 * If the current value is greater than or equal to the `other` value, a
 * `ValidationFailedException` is thrown. The exception message and cause can
 * be customized by providing optional parameters.
 *
 * @param other The value to compare against.
 * @param callableName The name of the callable (e.g., function or property) where validation is performed, or null if not specified.
 * @param parameter The parameter being validated, as a KParameter instance, or null if not applicable.
 * @param message An optional message that will be included in the exception if validation fails. Defaults to null.
 * @param causeOf An optional supplier for the main cause of the validation exception. If provided, the exception created by `cause` will be its cause.
 * @param cause An optional supplier for a secondary exception cause, or null if not provided.
 * @return The original value, if validation passes.
 * @throws ValidationFailedException If the current value is greater than or equal to the `other` value.
 * @since 5.0.0
 */
@IgnorableReturnValue
fun <T : Comparable<T>> T.validateLowerThan(other: T, callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this >= other) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is greater than or equal to $other", cause?.invoke(this)) else causeOf(this).initCause(ValidationFailedException(callableName, parameter, message ?: "is greater than or equal to $other", cause?.invoke(this)))
    return this
}

/**
 * Validates the input object against the specified predicate. If the validation fails,
 * a `MalformedInputException` or the provided `causeOf` exception is thrown.
 *
 * @param predicate the predicate used to validate the input object.
 * @param message a custom error message to be used in the exception if validation fails, may be null.
 * @param causeOf an optional throwable to be used as the cause of the exception if provided, may be null.
 * @return the original input object if validation passes.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.validateInputFormat(message: String? = null, causeOf: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) MalformedInputException(message) else causeOf(this).initCause(MalformedInputException(message))
    return this
}
/**
 * Validates the input format of the receiver object based on a given predicate.
 * If the validation fails and the value is `null`, a `MalformedInputException` is thrown.
 *
 * @param predicate a lambda or function reference that serves as the predicate to validate the receiver object.
 * @param `class` an optional KClass instance representing the expected type of the receiver for constructing a meaningful exception message.
 * @param causeOf an optional throwable that, if provided, will be used as the cause for any exception thrown.
 * @return the receiver object if validation passes.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.validateInputFormat(`class`: KClass<*>? = null, causeOf: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) MalformedInputException(`class`) else causeOf(this).initCause(MalformedInputException(`class`))
    return this
}
/**
 * Validates the input based on the given predicate, required type, and optional cause.
 * Throws a `MalformedInputException` if the input is null or does not conform to the expected format.
 *
 * @param predicate the predicate function used to validate the input.
 * @param type the expected type of the input, used for error reporting if the input is invalid. Optional, defaults to null.
 * @param causeOf an optional underlying throwable that caused this validation failure. If provided, it will be augmented with a `MalformedInputException`. Defaults to null.
 * @return the original input if it passes validation.
 * @throws MalformedInputException if the input is null or its format does not meet the expected criteria.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.validateInputFormat(type: KType? = null, causeOf: Transformer<T, Throwable>? = null, predicate: Predicate<T>): T {
    if (!predicate(this)) throw if (causeOf == null) MalformedInputException(type) else causeOf(this).initCause(MalformedInputException(type))
    return this
}
/**
 * Validates the input string against a specified regular expression.
 * If the input does not match the given regular expression, a `MalformedInputException` is thrown.
 *
 * @param regex The regular expression to validate the input against.
 * @param message An optional custom exception message to provide additional context, may be null.
 * @param causeOf An optional `Throwable` to wrap the validation exception, may be null.
 * @return The same `CharSequence` if validation passes successfully.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateInputFormat(regex: Regex, message: String? = null, causeOf: Transformer<T, Throwable>? = null): T {
    if (!regex(this)) throw if (causeOf == null) MalformedInputException(message) else causeOf(this).initCause(MalformedInputException(message))
    return this
}
/**
 * Validates the format of the given character sequence against the specified regular expression.
 * If the validation fails, it throws a `MalformedInputException` or associates it with an optional cause.
 *
 * @param regex The regular expression used to validate the format of the character sequence.
 * @param `class` An optional `KClass` representing the expected class type for the input.
 * Used for providing additional context in the exception message if validation fails.
 * @param causeOf An optional throwable that serves as the cause of the validation exception if provided.
 * @return The calling character sequence if the input format is valid.
 * @throws MalformedInputException If the character sequence does not conform to the regular expression.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateInputFormat(regex: Regex, `class`: KClass<*>? = null, causeOf: Transformer<T, Throwable>? = null): T {
    if (!regex(this)) throw if (causeOf == null) MalformedInputException(`class`) else causeOf(this).initCause(MalformedInputException(`class`))
    return this
}
/**
 * Validates the input string against a provided regular expression (regex) and optionally associates it
 * with a specific type context. If the input does not match the regex, a `MalformedInputException` is thrown.
 * Optionally, you can pass an existing cause to wrap the exception when validation fails.
 *
 * @param regex The regular expression to validate the input against.
 * @param type The expected KType of the input for additional context in the exception message (nullable).
 * @param causeOf An optional existing throwable to be wrapped as the cause of the exception (nullable).
 * @return The original string if it is valid and matches the provided regex.
 * @throws MalformedInputException if the validation fails and the input does not conform to the regex.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T : CharSequence> T.validateInputFormat(regex: Regex, type: KType? = null, causeOf: Transformer<T, Throwable>? = null): T {
    if (!regex(this)) throw if (causeOf == null) MalformedInputException(type) else causeOf(this).initCause(MalformedInputException(type))
    return this
}

/**
 * Evaluates whether the current object matches the given expected value. If the actual value does not
 * match the expectation, an `ExpectationMismatchException` is thrown with a detailed error message.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param expectation The value to be compared with the current object.
 * @return The original object if it does not match the expectation.
 * @throws ExpectationMismatchException if the current object equals the expected value.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.expect(expectation: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this != expectation) throw
    if (causeOf == null) ExpectationMismatchException("Value was expected as ${if (expectation.toString().isBlank()) "\"\"" else expectation}, but was $this", cause?.invoke(this))
    else causeOf(this).initCause(ExpectationMismatchException("Value was expected as ${if (expectation.toString().isBlank()) "\"\"" else expectation}, but was $this", cause?.invoke(this)))
    return this
}
/**
 * Checks if the current object is equal to the given expected value. If they are equal,
 * an `ExpectationMismatchException` is thrown with the message generated from `lazyMessage`.
 * Otherwise, it returns the current object.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param expectation The value that the current object is compared against.
 * @param lazyMessage A supplier that provides the message to be used in the exception if the values match.
 * @return The current object if it does not match the expected value.
 * @throws ExpectationMismatchException If the current object equals the expected value.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.expect(expectation: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current value does not match the provided expectation. If the values match,
 * an `ExpectationMismatchException` is thrown.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param expectation The value that the current instance is compared against.
 * @param property The `KProperty` reflecting the property name and type for which the validation occurred.
 * @param variableName The name of the variable being validated.
 * @return The current value if it does not match the expectation.
 * @throws ExpectationMismatchException if the current value matches the expectation.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.expect(expectation: T, property: KProperty<*>?, variableName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(property, variableName, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variableName, expectation, this, cause?.invoke(this)))
    return this
}
/**
 * Validates that the invoking object is equal to the provided expected value. If the values
 * do not match, it throws an `ExpectationMismatchException`.
 *
 * @param expectation the expected value against which the invocation object is compared
 * @param property the property involved in the comparison, or null if no specific property is applicable
 * @param variable an optional additional property associated with the validation, or null if not applicable
 * @param causeOf an optional cause of the exception, if it should replace the default exception
 * @param cause an optional underlying cause of the mismatch, or null if not applicable
 * @return the invoking object if it matches the expected value
 * @throws ExpectationMismatchException if the invoking object does not match the expected value
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.expect(expectation: T, property: KProperty<*>?, variable: KProperty<*>?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(property, variable, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variable, expectation, this, cause?.invoke(this)))
    return this
}
/**
 * Compares the current object with an expected value and throws an `ExpectationMismatchException`
 * if they are equal, providing details about the mismatch.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param expectation The value to compare against the current object.
 * @param callable The Kotlin function containing the parameter associated with the expectation.
 * @param parameterName The name of the parameter for which the expectation is being checked.
 * @return The current object (`this`) if no mismatch is found.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.expect(expectation: T, callable: KFunction<*>?, parameterName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameterName, expectation, this, cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object matches the provided expectation. If the value does
 * not match the expectation, an exception is thrown. Additional context such as the
 * callable function, parameter, and a cause for the exception can also be provided.
 *
 * @param expectation The expected value to compare against the current object.
 * @param callable The function where the expectation is being checked, or null if not applicable.
 * @param parameter The specific parameter or property associated with the expectation,
 *     or null if not applicable.
 * @param causeOf An optional exception to be thrown directly, instead of constructing a new one.
 * @param cause An optional underlying cause for the exception if one is created.
 * @return The current object if it matches the expectation.
 * @throws ExpectationMismatchException Thrown when the current object does not match
 *     the provided expectation, with detailed context about the mismatch.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.expect(expectation: T, callable: KFunction<*>?, parameter: KParameter?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameter, expectation, this, cause?.invoke(this)))
    return this
}
/**
 * Verifies if the current value matches the expected value and throws an `ExpectationMismatchException`
 * if the condition is not met. This is useful for validating specific conditions within callable functions
 * or parameters.
 *
 * @param expectation the value that is expected
 * @param callableName the name of the callable where the expectation mismatch occurred, or `null` if unprovided
 * @param parameterName the name of the parameter being validated, or `null` if unprovided
 * @param causeOf an existing `Throwable` to wrap the exception, or `null` if not used
 * @param cause an optional additional cause for the mismatch, or `null` if not provided
 * @return the current value if it matches the expected value
 * @throws ExpectationMismatchException if the current value does not match the expected value
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.expect(expectation: T, callableName: String?, parameterName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameterName, expectation, this, cause?.invoke(this)))
    return this
}
/**
 * Verifies that the current value matches the specified expectation. If the values do not match,
 * an `ExpectationMismatchException` is thrown. The exception can optionally include a cause or be
 * wrapped as the cause of another throwable.
 *
 * @param expectation The expected value to match against the current value.
 * @param callableName The name of the callable function or property being evaluated, or null if not applicable.
 * @param parameter The property being validated, or null if not applicable.
 * @param causeOf An optional throwable that this mismatch should be the cause of, or null.
 * @param cause An optional underlying cause of this mismatch, or null.
 * @return The current value if it matches the expected value.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T.expect(expectation: T, callableName: String?, parameter: KParameter?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameter, expectation, this, cause?.invoke(this)))
    return this
}

/**
 * Verifies that the current object is not equal to the expected value.
 * If the object matches the expected value, an [ExpectationMismatchException] is thrown.
 * Optionally allows specifying a cause of type [Throwable].
 *
 * @param expectation The value that the current object is compared against. If the two values are equal, an exception is thrown.
 * @param causeOf An optional throwable that will have the generated [ExpectationMismatchException] set as its cause.
 * @param cause An optional throwable to act as the direct cause of the generated [ExpectationMismatchException].
 * @return The current object if it does not match the expected value.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.expectNot(expectation: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this == expectation) throw
    if (causeOf == null) ExpectationMismatchException("Value was expected as ${if (expectation.toString().isBlank()) "\"\"" else expectation}, but was $this", cause?.invoke(this))
    else causeOf(this).initCause(ExpectationMismatchException("Value was expected as ${if (expectation.toString().isBlank()) "\"\"" else expectation}, but was $this", cause?.invoke(this)))
    return this
}
/**
 * Ensures that the current object is not equal to the specified expectation.
 * Throws an `ExpectationMismatchException` if the expectation is met.
 *
 * @param expectation The value that this object is compared against.
 * @param causeOf An optional throwable that serves as the primary cause of the failure. If null, a new exception will be created.
 * @param cause An optional underlying throwable cause for the new exception.
 * @param lazyMessage A supplier that provides a message for the exception if the expectation is met.
 * @return The original object if it does not match the expectation.
 * @throws ExpectationMismatchException If the current object equals the expectation.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.expectNot(expectation: T, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (this == expectation) throw if (causeOf == null) ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Checks whether the current value is not equal to the provided `expectation`.
 * If the values are equal, an `ExpectationMismatchException` is thrown.
 *
 * @param expectation The value that the current instance is not expected to be equal to.
 * @param property An optional property representing the field or value involved in the check.
 * @param variableName An optional name of the variable being evaluated for debugging or logging purposes.
 * @param causeOf An optional `Throwable` to use as the primary cause for the exception, if thrown.
 * @param cause An optional `Throwable` to attach as an additional cause for the exception, if thrown.
 * @return The current value if it does not match the provided expectation.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.expectNot(expectation: T, property: KProperty<*>?, variableName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this == expectation) throw if (causeOf == null) ExpectationMismatchException(property, variableName, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variableName, expectation, this, cause?.invoke(this)))
    return this
}
/**
 * Asserts that the current value does not match the specified expectation.
 * If the expectation matches the current value, an `ExpectationMismatchException` is thrown.
 *
 * @param expectation The value that the current value should not match.
 * @param property The primary property associated with the value being checked, or null if not applicable.
 * @param variable An optional secondary property associated with the value being checked, or null if not applicable.
 * @param causeOf An optional throwable to serve as the main exception cause, or null if not applicable.
 * @param cause An additional throwable cause, or null if not applicable.
 * @return The current value, if it does not match the expectation.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.expectNot(expectation: T, property: KProperty<*>?, variable: KProperty<*>?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this == expectation) throw if (causeOf == null) ExpectationMismatchException(property, variable, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variable, expectation, this, cause?.invoke(this)))
    return this
}
/**
 * Ensures that the receiver value does not match the specified expectation. If the receiver value
 * equals the expectation, an `ExpectationMismatchException` is thrown.
 *
 * @param expectation The value that the receiver should not match. Can be `null`.
 * @param callable The Kotlin function that contains the parameter being validated. Can be `null` if the context
 * of the callable is unavailable.
 * @param parameterName The name of the parameter being validated in the callable. Can be `null` if the parameter name
 * is unavailable.
 * @param causeOf The primary cause to propagate if the mismatch exception is triggered. Can be `null`.
 * @param cause The root cause of the failure, which can be chained to the thrown exception. Can be `null`.
 * @return The receiver value if it does not match the expectation.
 * @throws ExpectationMismatchException if the receiver value matches the expectation.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.expectNot(expectation: T, callable: KFunction<*>?, parameterName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this == expectation) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameterName, expectation, this, cause?.invoke(this)))
    return this
}
/**
 * Compares the current value with a given expectation and throws an `ExpectationMismatchException`
 * if they are equal. This method is typically used to ensure that certain expectations are not met.
 *
 * @param expectation the value that the current object is expected NOT to match
 * @param callable the callable function associated with the expectation check, or null if not applicable
 * @param parameter the parameter being checked within the callable function, or null if not applicable
 * @param causeOf an optional pre-existing exception that caused this expectation to fail
 * @param cause an optional cause to be used for the `ExpectationMismatchException`, if the expectation fails
 * @return the current value if it does not match the expectation
 * @throws ExpectationMismatchException if the current value equals the given expectation
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.expectNot(expectation: T, callable: KFunction<*>?, parameter: KParameter?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this == expectation) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameter, expectation, this, cause?.invoke(this)))
    return this
}
/**
 * Ensures that the current value does not match the specified expectation. If the value matches
 * the expectation, an [ExpectationMismatchException] is thrown.
 *
 * @param expectation the value that the current value is expected not to match
 * @param callableName the name of the callable where this check occurs, or `null` if unavailable
 * @param parameterName the name of the parameter being checked, or `null` if unavailable
 * @param causeOf an optional existing throwable that this exception wraps or extends, or `null` if unavailable
 * @param cause an optional throwable to be set as the cause of the exception if a mismatch occurs, or `null` if unavailable
 * @return the original value if it does not match the expectation
 * @throws ExpectationMismatchException if the current value matches the specified expectation
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.expectNot(expectation: T, callableName: String?, parameterName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this == expectation) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameterName, expectation, this, cause?.invoke(this)))
    return this
}
/**
 * Verifies that the current value is not equal to the provided expectation.
 *
 * If the current value matches the expectation, an `ExpectationMismatchException`
 * is thrown. Optionally, a custom throwable can be provided as the cause.
 *
 * @param expectation The value that the current value should not match.
 * @param callableName The name of the function or property being evaluated, or null if unknown.
 * @param parameter The parameter being evaluated, or null if not applicable.
 * @param causeOf An optional throwable representing the root cause to be used if the expectation is not met.
 * @param cause An optional additional cause that provides context for the failure.
 * @return The current value (receiver) if it does not match the expectation.
 * @throws ExpectationMismatchException If the current value matches the expectation.
 * @since 4.2.0
 */
@IgnorableReturnValue
fun <T> T.expectNot(expectation: T, callableName: String?, parameter: KParameter?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (this == expectation) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, expectation, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameter, expectation, this, cause?.invoke(this)))
    return this
}

/**
 * Asserts that the invoking variable is null. If the variable is not null,
 * an `ExpectationMismatchException` is thrown with the variable’s runtime value included in the message.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @return The variable itself, which is expected to be null.
 * @throws ExpectationMismatchException If the variable is not null.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.expectNull(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException("Variable was expected to be null, but was $this", cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException("Variable was expected to be null, but was $this", cause?.invoke(this)))
    return this
}
/**
 * Checks if the current object is null, and throws an `ExpectationMismatchException` if it is not null.
 *
 * This method evaluates the receiver instance (`this`) against the null expectation. If the receiver is
 * not null, an `ExpectationMismatchException` is thrown with a message generated by the supplied `lazyMessage`.
 * If the receiver is null, it is returned as is.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param lazyMessage A supplier function to generate the exception message if the expectation fails.
 * @return The receiver instance if it is null.
 * @throws ExpectationMismatchException if the receiver is not null.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.expectNull(causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T? {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Ensures that the current value is `null`. If the value is not `null`,
 * it throws an `ExpectationMismatchException` with detailed information
 * about the property and variable name provided.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param property The property associated with the value being checked.
 * @param variableName The name of the variable being checked.
 * @return The current value (`this`). Useful for method chaining, though it will always be `null`.
 * @throws ExpectationMismatchException if the value is not `null`.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.expectNull(property: KProperty<*>?, variableName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(property, variableName, null, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variableName, null, this, cause?.invoke(this)))
    return this
}
/**
 * Ensures that the value of this object is null. If the value is not null, an exception is thrown.
 *
 * @param property The primary property associated with the null expectation, or null if not applicable.
 * @param variable An optional secondary property related to the null expectation, or null if not applicable.
 * @param causeOf An optional throwable that serves as the root cause of the exception, or null if not applicable.
 * @param cause An optional secondary cause of the exception, or null if not applicable.
 * @return The object itself if it passes the null expectation.
 * @throws ExpectationMismatchException If the object is not null.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.expectNull(property: KProperty<*>?, variable: KProperty<*>?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(property, variable, null, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(property, variable, null, this, cause?.invoke(this)))
    return this
}
/**
 * Ensures that the receiver is `null`. If the receiver is not `null`, an `ExpectationMismatchException`
 * is thrown, indicating the mismatch for the specified parameter of the given callable function.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause The cause of exception (another exception)
 * @param callable The callable function whose parameter is being checked for `null`.
 *                 This provides context for where the mismatch occurred.
 * @param parameterName The name of the parameter being validated as `null`.
 *                      This helps identify the specific parameter causing the mismatch.
 * @return The receiver itself if it is `null`.
 * @throws ExpectationMismatchException If the receiver is not `null`.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.expectNull(callable: KFunction<*>?, parameterName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, null, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameterName, null, this, cause?.invoke(this)))
    return this
}
/**
 * Ensures that the invoking object is `null`. If the object is not `null`, this method will throw an
 * `ExpectationMismatchException` with the provided information about the callable, parameter, and potential causes.
 *
 * @param callable The function in which the expectation is being validated, or null if not applicable.
 * @param parameter The property or parameter that is validated, or null if not applicable.
 * @param causeOf The root exception that triggered this validation failure, if available.
 * @param cause The optional cause of the expectation mismatch, or null if no specific cause is provided.
 * @return The nullable object itself if it passes the validation (i.e., it is `null`).
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.expectNull(callable: KFunction<*>?, parameter: KParameter?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, null, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callable, parameter, null, this, cause?.invoke(this)))
    return this
}
/**
 * Ensures that the receiver object is `null`. If the receiver is not `null`, an `ExpectationMismatchException` is thrown.
 *
 * @param callableName the name of the callable where this check is performed, or `null` if not specified
 * @param parameterName the name of the parameter being checked, or `null` if not specified
 * @param causeOf an optional throwable that serves as the primary cause of the exception, if applicable
 * @param cause an optional throwable that serves as an additional cause of the exception, if applicable
 * @return the receiver object if it is `null`
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.expectNull(callableName: String?, parameterName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, null, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameterName, null, this, cause?.invoke(this)))
    return this
}
/**
 * Ensures that the calling object is null. If the object is not null, an exception is thrown.
 *
 * This function verifies that the caller object conforms to the expectation of being null.
 * If the expectation is not met, an `ExpectationMismatchException` is thrown with
 * details about the mismatch, including the callable name, parameter, optional causes,
 * or additional exception chains.
 *
 * @param callableName The name of the callable function or property being evaluated, or null if unavailable.
 * @param parameter The property whose expected value is being asserted as null, or null if unavailable.
 * @param causeOf A potential throwable cause linked to this assertion failure, or null.
 * @param cause An optional underlying cause for the exception, or null if none exists.
 * @return The caller object itself if it is null.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.expectNull(callableName: String?, parameter: KParameter?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T? {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, null, this, cause?.invoke(this)) else causeOf(this).initCause(ExpectationMismatchException(callableName, parameter, null, this, cause?.invoke(this)))
    return this
}

/**
 * Verifies that the current instance is of the specified expected class type, throwing an exception if it is not.
 *
 * This method checks if the instance is an instance of the provided `expectationClass`.
 * If the instance does not match the expected type, a `ClassMismatchException` is thrown.
 * Optionally, it can propagate a provided cause or set it as the underlying cause of the exception.
 *
 * @param T The type of the current instance being checked.
 * @param expectationClass The Kotlin class (`KClass`) representing the expected type.
 * @param causeOf An optional throwable that will be set as the cause of the generated exception.
 * @param cause An optional throwable providing additional context about the mismatch error.
 * @return The current instance (unchanged) if it matches the expected type.
 * @throws ClassMismatchException If the instance does not match the specified `expectationClass`.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T : Any> T.expectClass(expectationClass: KClass<*>, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(expectationClass, this::class, cause?.invoke(this))
    else causeOf(this).initCause(ClassMismatchException(expectationClass, this::class, cause?.invoke(this)))
    return this
}
/**
 * Ensures that the calling object is an instance of the specified expectation class.
 * If the object is not an instance, a `ClassMismatchException` is thrown.
 *
 * @param expectationClass The `KClass` that the object is expected to be an instance of.
 * @param causeOf An optional `Throwable` to be the cause of the `ClassMismatchException`. If provided,
 * this exception's cause will be initialized with the `ClassMismatchException` being thrown.
 * @param cause An optional additional cause for the `ClassMismatchException`.
 * @param lazyMessage A lambda or supplier providing a detailed message for the exception in case of mismatch.
 * @return The calling object if it is an instance of the specified expectation class.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T : Any> T.expectClass(expectationClass: KClass<*>, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null, lazyMessage: Transformer<T, Any>): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(lazyMessage(this).toString(), cause?.invoke(this)) else causeOf(this).initCause(ClassMismatchException(lazyMessage(this).toString(), cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is an instance of a specified class and returns the object if valid.
 * Throws a `ClassMismatchException` if the object's type does not match the expected type.
 *
 * @param expectationClass The `KClass` instance representing the expected type.
 * @param property The property being checked for type conformity, or null if not applicable.
 * @param variableName The name of the variable being validated, or null if not specified.
 * @param causeOf An optional throwable indicating the primary cause of the validation, or null if not provided.
 * @param cause An optional throwable to be used as the root cause of the exception, or null if not provided.
 * @return The current object (`this`) if it is an instance of the specified class.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T : Any> T.expectClass(expectationClass: KClass<*>, property: KProperty<*>?, variableName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(property, variableName, expectationClass, cause?.invoke(this)) else causeOf(this).initCause(ClassMismatchException(property, variableName, expectationClass, cause?.invoke(this)))
    return this
}
/**
 * Validates that the current object is an instance of the specified class and throws a `ClassMismatchException` if it is not.
 *
 * @param expectationClass The `KClass` instance representing the expected class type.
 * @param property The `KProperty` associated with the validation, or null if not applicable.
 * @param variable The variable `KProperty` being validated, or null if not applicable.
 * @param causeOf An optional `Throwable` that acts as the root cause of the exception, or null if not specified.
 * @param cause An optional `Throwable` cause to be attached to the exception, or null if not specified.
 * @return The original object if the validation passes.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T : Any> T.expectClass(expectationClass: KClass<*>, property: KProperty<*>?, variable: KProperty<*>?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(property, variable, expectationClass, cause?.invoke(this)) else causeOf(this).initCause(ClassMismatchException(property, variable, expectationClass, cause?.invoke(this)))
    return this
}
/**
 * Verifies whether the receiver object is an instance of the specified expectation class.
 * If the receiver object does not match the expected type, the method throws a `ClassMismatchException`.
 *
 * @param T The type of the receiver object.
 * @param expectationClass The expected class (`KClass`) instance that the receiver object should conform to.
 * @param callable The `KFunction` instance representing the callable in which the validation failure
 *                 might occur, or null if not provided.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param causeOf A higher-level throwable cause to associate with the mismatch exception,
 *                or null if not applicable.
 * @param cause The underlying `Throwable` cause for the mismatch exception, or null if not applicable.
 * @return The receiver object if it conforms to the expected class.
 * @throws ClassMismatchException If the receiver object does not meet the type expectations.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T : Any> T.expectClass(expectationClass: KClass<*>, callable: KFunction<*>?, parameterName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(callable, parameterName, expectationClass, cause?.invoke(this)) else causeOf(this).initCause(ClassMismatchException(callable, parameterName, expectationClass, cause?.invoke(this)))
    return this
}
/**
 * Verifies if the current instance is of the specified class type and throws a `ClassMismatchException` if validation fails.
 *
 * This method is typically used to enforce type expectations during runtime, such as when invoking reflective calls.
 * If the instance is not of the specified class type, a `ClassMismatchException` is thrown, providing details about
 * the mismatch and the context in which it occurred.
 *
 * @param expectationClass The class type that the current instance is expected to conform to.
 * @param callable The callable (e.g., function or constructor) involved in the context of this validation, or null if not applicable.
 * @param parameter The parameter within the callable that triggered this validation, or null if not applicable.
 * @param causeOf An optional throwable that caused this validation to fail, if available; otherwise `ClassMismatchException` is created.
 * @param cause The underlying cause or exception providing additional context, or null if not applicable.
 * @return The current instance if it conforms to the expected class type.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T : Any> T.expectClass(expectationClass: KClass<*>, callable: KFunction<*>?, parameter: KParameter?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(callable, parameter, expectationClass, cause?.invoke(this)) else causeOf(this).initCause(ClassMismatchException(callable, parameter, expectationClass, cause?.invoke(this)))
    return this
}
/**
 * Verifies that the instance is of the specified expected class type. If the instance does not match
 * the expected type, a `ClassMismatchException` is thrown.
 *
 * @param expectationClass The `KClass` representing the expected type of the instance.
 * @param callableName The name of the callable (e.g., method or function) where this validation is performed, or null if not specified.
 * @param parameterName The name of the parameter being validated, or null if not specified.
 * @param causeOf An optional throwable to specify any external cause for this validation error. If non-null, this will be the direct cause of the exception thrown.
 * @param cause An optional throwable cause for additional context or nested reasons for the exception.
 * @return Returns the original instance if it matches the expected type.
 * @throws ClassMismatchException If the instance is not of the expected type.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T : Any> T.expectClass(expectationClass: KClass<*>, callableName: String?, parameterName: String? = null, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(callableName, parameterName, expectationClass, this::class, cause?.invoke(this)) else causeOf(this).initCause(ClassMismatchException(callableName, parameterName, expectationClass, this::class, cause?.invoke(this)))
    return this
}
/**
 * Verifies if the current instance is of the specified expected class type. If the instance is not
 * compatible with the provided `expectationClass`, a `ClassMismatchException` is thrown.
 * The exception includes details about the callable, parameter, and an optional cause of the error.
 *
 * @param expectationClass The expected class type to verify against the current instance.
 * @param callableName The name of the callable being executed, or null if not applicable.
 * @param parameter The parameter being validated, or null if not specified.
 * @param causeOf An optional pre-existing throwable that should be used as the root cause
 *        for `ClassMismatchException`, or null if not required.
 * @param cause Additional information about an error or exception that may help
 *        in diagnosing the issue, or null if not applicable.
 * @return The current instance if it matches the specified `expectationClass`.
 * @throws ClassMismatchException If the current instance is not of the expected class type.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T : Any> T.expectClass(expectationClass: KClass<*>, callableName: String?, parameter: KParameter?, causeOf: Transformer<T, Throwable>? = null, cause: Transformer<T, Throwable>? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(callableName, parameter, expectationClass, cause?.invoke(this)) else causeOf(this).initCause(ClassMismatchException(callableName, parameter, expectationClass, cause?.invoke(this)))
    return this
}

/**
 * Ensures that the caller-provided object is not null and throws a `RequiredFieldException`
 * if the object is null.
 *
 * This method is typically used to enforce the presence of required fields during validation.
 * It throws an exception if the provided object is null, optionally chaining a custom cause
 * or using a pre-existing exception to provide additional context.
 *
 * @param causeOf an optional `Throwable` to be used as the cause of the `RequiredFieldException`.
 * If provided, it will wrap the exception created inside this method. Can be null.
 * @param cause an optional `Throwable` cause added to the `RequiredFieldException` to provide
 * additional debugging information. Can be null.
 * @return the original object if it is not null.
 * @throws RequiredPropertyException if the object is null.
 * @since 3.2.0
 */
@IgnorableReturnValue
fun <T> T?.requiredProperty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@requiredProperty != null)
    }
    if (this == null) throw if (causeOf == null) RequiredPropertyException("Property is required.", cause?.invoke()) else causeOf().initCause(RequiredPropertyException("Field is required.", cause?.invoke()))
    return this
}

/**
 * Ensures that the receiver is non-null. If the receiver is null, a `RequiredPropertyException` is thrown.
 *
 * @param causeOf an optional pre-existing throwable to use as the root cause for the exception, if applicable.
 * @param cause an optional secondary throwable to specify additional context for the exception.
 * @param lazyMessage a lambda function supplying a custom message for the exception when it is thrown.
 * @return the receiver instance if it is non-null.
 * @since 3.2.0
 */
@IgnorableReturnValue
fun <T> T?.requiredProperty(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    contract {
        returns() implies (this@requiredProperty != null)
    }
    if (this == null) throw if (causeOf == null) RequiredPropertyException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(RequiredPropertyException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Validates that the current object (`this`) is not null and throws a [RequiredPropertyException]
 * if it is null, providing detailed context from the property and variable name. Optionally,
 * associates an additional cause for the exception.
 *
 * @param property The Kotlin property associated with the required field validation.
 *                 This is used for generating meaningful exception messages.
 * @param variableName The name of the variable being validated. If provided, this will be included
 *                     in the exception message for additional context.
 * @param causeOf An optional exception that is the root cause of this validation failure.
 *                If present, this exception will be the thrown exception, with its cause updated to include
 *                additional context from the validation process.
 * @param cause An optional underlying cause of the [RequiredPropertyException], used for debugging or chaining.
 * @return The current object (`this`), guaranteed to be non-null.
 * @throws RequiredPropertyException If the current object (`this`) is null, providing detailed error context.
 * @since 3.2.0
 */
@IgnorableReturnValue
fun <T> T?.requiredProperty(property: KProperty<*>?, variableName: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@requiredProperty != null)
    }
    if (this == null) throw if (causeOf == null) RequiredPropertyException(property, variableName, cause?.invoke()) else causeOf().initCause(RequiredPropertyException(property, variableName, cause?.invoke()))
    return this
}
/**
 * Ensures that the referenced property is not null. If the property is null, throws a `RequiredFieldException`.
 * Optionally, a `variableName` and `causeOf`/`cause` can be provided to include additional context in the exception.
 *
 * @param T the type of the referenced property.
 * @param variableName an optional name of the variable to include in the exception message for context. Nullable.
 * @param causeOf an optional throwable that triggers this exception, which will wrap a `RequiredFieldException`. Nullable.
 * @param cause an optional cause of the exception to be attached for debugging purposes. Nullable.
 * @since 3.2.0
 */
@IgnorableReturnValue
fun <T> KProperty0<T>.requiredProperty(variableName: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null) = get().run {
    if (this == null) throw
    if (causeOf == null) RequiredPropertyException(this@requiredProperty, variableName, cause?.invoke())
    else causeOf().initCause(RequiredPropertyException(this@requiredProperty, variableName, cause?.invoke()))
    this as T
}
/**
 * Ensures that a field is not null and throws a `RequiredFieldException` if the field is null.
 * This utility function is primarily used to validate the presence of required fields
 * in a domain or application logic.
 *
 * @param property The Kotlin property reference representing the field that is required.
 * @param variable The Kotlin property reference used to provide contextual information
 *                 about the variable triggering the null check.
 * @param causeOf An optional pre-existing exception that caused this validation failure.
 * @param cause An optional additional cause of the exception.
 * @return The original object if it is not null.
 * @throws RequiredPropertyException if the object is null, with additional context
 *         provided by `property`, `variable`, `causeOf`, or `cause`.
 * @since 3.2.0
 */
@IgnorableReturnValue
fun <T> T?.requiredProperty(property: KProperty<*>?, variable: KProperty<*>, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@requiredProperty != null)
    }
    if (this == null) throw if (causeOf == null) RequiredPropertyException(property, variable, cause?.invoke()) else causeOf().initCause(RequiredPropertyException(property, variable, cause?.invoke()))
    return this
}
/**
 * Ensures that the referenced property is not null. If the property is null, throws a `RequiredFieldException`.
 * Optionally, a `variableName` and `causeOf`/`cause` can be provided to include additional context in the exception.
 *
 * @param T the type of the referenced property.
 * @param variable an optional variable to include in the exception message for context. Nullable.
 * @param causeOf an optional throwable that triggers this exception, which will wrap a `RequiredFieldException`. Nullable.
 * @param cause an optional cause of the exception to be attached for debugging purposes. Nullable.
 * @since 3.2.0
 */
@IgnorableReturnValue
fun <T> KProperty0<T>.requiredProperty(variable: KProperty<*>, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null) = get().run {
    if (this == null) throw
    if (causeOf == null) RequiredPropertyException(this@requiredProperty, variable, cause?.invoke())
    else causeOf().initCause(RequiredPropertyException(this@requiredProperty, variable, cause?.invoke()))
    this as T
}
/**
 * Ensures that the parameter on which the function is called is not null. If the parameter is null,
 * a [RequiredParameterException] is thrown with an optional cause or a combination of a cause and a user-defined exception.
 *
 * @param causeOf An optional [Throwable] to use as a user-defined exception, initialized with the
 *                [RequiredParameterException] if the parameter is null. Defaults to null.
 * @param cause An optional [Throwable] representing the cause of the exception. Defaults to null.
 * @return The non-null value of the parameter.
 * @throws RequiredParameterException If the parameter is null, this exception is thrown with an appropriate message and cause.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.requiredParameter(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException("Parameter is required.", cause?.invoke()) else causeOf().initCause(RequiredParameterException("Parameter is required.", cause?.invoke()))
    return this
}
/**
 * Ensures that the current object (`this`) is not `null`, throwing an exception if it is.
 * This method is used to validate that a required parameter is provided and non-null.
 *
 * If the object is `null`, a [RequiredParameterException] is thrown with an optional message
 * and cause. Optionally, a pre-existing `Throwable` can be provided as the `causeOf`, which is
 * used to initialize the exception instead of creating a new one.
 *
 * @param causeOf An optional `Throwable` that, if provided, will be used to initialize the
 *                [RequiredParameterException]. Default is `null`.
 * @param cause An optional `Throwable` that specifies the cause of the exception, if any.
 *              Default is `null`.
 * @param lazyMessage A `Supplier` that provides the message to be included in the exception.
 *                    This message is lazily evaluated.
 * @return The caller object (`this`) if it is not `null`.
 * @throws RequiredParameterException if the caller object (`this`) is `null`.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.requiredParameter(causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null, lazyMessage: Supplier<Any>): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException(lazyMessage().toString(), cause?.invoke()) else causeOf().initCause(RequiredParameterException(lazyMessage().toString(), cause?.invoke()))
    return this
}
/**
 * Ensures that the given object is not null. If the object is null, a `RequiredParameterException` is thrown,
 * providing detailed context about the missing or invalid parameter within a callable function.
 *
 * @param callable The callable function where the parameter is required.
 * @param parameterName The name of the parameter expected to be non-null within the callable.
 * @param causeOf The underlying cause of the parameter exception, if applicable. Can be null.
 * @param cause An additional exception cause to be attached. Can be null.
 * @return The non-null object `T` if the validation is successful.
 * @throws RequiredParameterException If the object is null, a descriptive exception is thrown.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.requiredParameter(callable: KFunction<*>?, parameterName: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException(callable, parameterName, cause?.invoke()) else causeOf().initCause(RequiredParameterException(callable, parameterName, cause?.invoke()))
    return this
}
/**
 * Ensures that a parameter is non-null and throws a `RequiredParameterException` if it is null.
 *
 * This function is typically used to validate that a required parameter of a callable function
 * is provided and is non-null. If the parameter is `null`, an exception is thrown with detailed
 * information about the callable and parameter that caused the issue.
 *
 * @param callable The callable function associated with the parameter.
 * @param parameter The property reference representing the parameter to validate.
 * @param causeOf An optional throwable that is the cause of the exception. Defaults to null.
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @return The original parameter if it is non-null.
 * @since 1.0.0
 * @throws RequiredParameterException if the parameter is null.
 */
@IgnorableReturnValue
fun <T> T?.requiredParameter(callable: KFunction<*>?, parameter: KParameter, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException(callable, parameter, cause?.invoke()) else causeOf().initCause(RequiredParameterException(callable, parameter, cause?.invoke()))
    return this
}
/**
 * Ensures that the current value is not null, throwing a [RequiredParameterException] if it is.
 * This is intended to validate required parameters for a callable function or method.
 *
 * @param callableName The name of the callable (e.g., function or method) requiring the parameter. Can be null.
 * @param parameterName The name of the required parameter. Can be null.
 * @param causeOf A throwable that caused the exception to be thrown. If provided, will be used as the cause for the exception. Can be null.
 * @param cause The underlying cause of the exception, if any. Can be null.
 * @return The non-null value of the receiver.
 * @throws RequiredParameterException If the receiver is null.
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.requiredParameter(callableName: String?, parameterName: String? = null, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException(callableName, parameterName, cause?.invoke()) else causeOf().initCause(RequiredParameterException(callableName, parameterName, cause?.invoke()))
    return this
}
/**
 * Ensures that a parameter is not null and throws a [RequiredParameterException] if the parameter is null.
 *
 * This function is typically used to validate required parameters in a callable context,
 * where null values are not allowed. If the parameter is null, an exception is thrown,
 * providing detailed information such as the callable name and parameter details.
 *
 * @param callableName the name of the callable associated with the required parameter, or null if not applicable
 * @param parameter the Kotlin parameter representing the required parameter
 * @param causeOf the initial cause of the exception, or null if there is no initial cause
 * @param cause the underlying cause of the exception, or null if there is no underlying cause
 * @return the non-nullable instance of this object
 * @throws RequiredParameterException if this object is null
 * @since 1.0.0
 */
@IgnorableReturnValue
fun <T> T?.requiredParameter(callableName: String?, parameter: KParameter, causeOf: ThrowableSupplier? = null, cause: ThrowableSupplier? = null): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException(callableName, parameter, cause?.invoke()) else causeOf().initCause(RequiredParameterException(callableName, parameter, cause?.invoke()))
    return this
}

/**
 * Throws an [IllegalArgumentException] if the [value] is `true`.
 *
 * @param value The condition to be checked. Throws an exception if this evaluates to `true`.
 * @param lazyMessage A supplier for the error message to be used if the exception is thrown.
 * @since 4.2.0
 */
inline fun requireNot(value: Boolean, lazyMessage: Supplier<Any>) {
    contract {
        returns() implies value
    }
    if (value) {
        val message = lazyMessage()
        throw IllegalArgumentException(message.toString())
    }
}
/**
 * Validates that the given condition is not true.
 * If the condition is true, an IllegalArgumentException is thrown.
 *
 * @param value The condition to validate.
 *              If true, an exception will be thrown.
 * @since 4.2.0
 */
fun requireNot(value: Boolean) {
    contract {
        returns() implies value
    }
    if (value) throw IllegalArgumentException("Validation failed.")
}

/**
 * Ensures that the given value is null, throwing an [IllegalArgumentException] if the condition is not met.
 *
 * The function utilizes Kotlin contracts to help the compiler understand the nullability state of the provided value.
 *
 * @param value The value to check for nullability. Must be null; otherwise, an exception is thrown.
 * @since 1.0.0
 */
fun requireNull(value: Any?) {
    contract {
        returns() implies (value == null)
    }
    require(value == null)
}
/**
 * Ensures that the provided value is null. If the value is not null, an [IllegalArgumentException]
 * is thrown with the result of the [lazyMessage] function as the error message.
 *
 * This function uses Kotlin contracts to indicate that if the function returns normally,
 * the specified value is confirmed to be null.
 *
 * @param value The value to validate as null.
 * @param lazyMessage A lambda that provides the error message to use if the validation fails.
 * @since 1.0.0
 */
inline fun requireNull(value: Any?, lazyMessage: Supplier<Any>) {
    contract {
        returns() implies (value == null)
    }
    require(value == null) { lazyMessage().toString() }
}
/**
 * Ensures that the given value is null, otherwise throws an exception created by the provided lambda.
 *
 * This function is used to validate that a specific value is null before proceeding.
 * If the value is not null, the exception provided by the `lazyException` lambda
 * is thrown. The exception instance is created lazily to avoid unnecessary
 * object creation if the condition is satisfied.
 *
 * @param value The value to check for nullity. If this value is not null, the function throws the exception
 * provided by the `lazyException` lambda.
 * @param lazyException A lambda that supplies the exception to throw when the `value` is not null.
 * The exception is instantiated only if the condition fails.
 * @since 1.0.0
 */
@Deprecated("Use `value == null || throw` instead", ReplaceWith("value == null || throw lazyException()", "dev.tommasop1804.kutils.isNull"))
inline fun requireNullOrThrow(value: Any?, lazyException: ThrowableSupplier) {
    contract {
        returns() implies (value == null)
    }
    value == null || throw lazyException()
}
/**
 * Evaluates a condition and throws an exception if the condition is false.
 *
 * This function is useful for enforcing preconditions, ensuring that a specific requirement is met
 * before proceeding further in the code. The exception is created lazily via the provided lambda
 * to avoid unnecessary object creation if the condition is true.
 *
 * @param value The condition to evaluate. If this value is `false`, the function throws the exception
 * provided by the `lazyException` lambda.
 * @param lazyException A lambda that supplies the exception to throw when the condition is false.
 * The exception is instantiated only if the condition fails.
 * @since 1.0.0
 */
@Deprecated("Use `|| throw` instead", ReplaceWith("value || throw lazyException()"))
inline fun requireOrThrow(value: Boolean, lazyException: ThrowableSupplier) {
    contract {
        returns() implies value
    }
    if (!value) throw lazyException.invoke()
}
/**
 * Ensures that the specified value is not null by throwing an exception generated by the provided function.
 * If the value is null, the exception created by the lazyException function is thrown.
 *
 * @param value The value to be checked for nullability.
 * @param lazyException A lambda function that creates the exception to be thrown if the value is null.
 * @since 1.0.0
 */
@Deprecated("Use `value != null || throw` instead", ReplaceWith("value != null || throw lazyException()", "dev.tommasop1804.kutils.isNotNull"))
inline fun <T> requireNotNullOrThrow(value: T?, lazyException: ThrowableSupplier): T {
    contract {
        returns() implies (value != null)
    }
    if (value == null) throw lazyException.invoke()
    return value
}

/**
 * Throws an [IllegalStateException] if the provided condition [value] is false.
 *
 * The exception message is generated using the [lazyMessage] supplier.
 *
 * @param value The condition to evaluate. If false, an exception is thrown.
 * @param lazyMessage A supplier for generating the exception message when the condition is false.
 * @since 4.2.0
 */
inline fun checkNot(value: Boolean, lazyMessage: Supplier<Any>) {
    contract {
        returns() implies value
    }
    if (value) {
        val message = lazyMessage()
        throw IllegalStateException(message.toString())
    }
}
/**
 * Ensures that the provided boolean value is true. If the value is false, an exception is thrown.
 *
 * @param value the boolean value to be checked; must be true to avoid triggering an exception
 * @since 4.2.0
 */
fun checkNot(value: Boolean) {
    contract {
        returns() implies value
    }
    if (value) throw IllegalStateException("Validation failed.")
}

/**
 * Checks if the given value is null and throws an exception if it's not.
 *
 * This function uses a contract to inform the compiler about the nullability of the given value.
 *
 * @param value The value to be checked for null.
 * @since 1.0.0
 */

fun checkNull(value: Any?) {
    contract {
        returns() implies (value == null)
    }
    check(value == null)
}
/**
 * Checks if the given value is null and throws an exception with a lazily calculated message if it is not.
 *
 * @param value The value to check for nullity.
 * @param lazyMessage A lambda function to generate the exception message if the check fails.
 * @since 1.0.0
 */
fun checkNull(value: Any?, lazyMessage: Supplier<Any>) {
    contract {
        returns() implies (value == null)
    }
    check(value == null, lazyMessage)
}

/**
 * Validates the given condition, throwing a [ValidationFailedException] if the condition is not met.
 * The exception message is provided by the `lazyMessage` lambda, which is evaluated only if the validation fails.
 *
 * This function uses Kotlin contracts to indicate that upon successful execution, the provided condition is true.
 *
 * @param value The condition to validate. If `false`, a [ValidationFailedException] is thrown.
 * @param lazyMessage A lambda that provides the exception message if validation fails.
 * @throws ValidationFailedException If the validation condition is not met.
 * @since 1.0.0
 */
inline fun validate(value: Boolean, lazyMessage: Supplier<Any>) {
    contract {
        returns() implies value
    }
    if (!value) {
        val message = lazyMessage()
        throw ValidationFailedException(message.toString())
    }
}
/**
 * Validates a boolean value and throws a [ValidationFailedException] if the value is `false`.
 * This method uses Kotlin's contract system to provide implications about the provided value.
 *
 * @param value the boolean value to validate. If `false`, a [ValidationFailedException] is thrown.
 * @throws ValidationFailedException if the validation fails (i.e., the value is `false`).
 * @since 1.0.0
 */
fun validate(value: Boolean) {
    contract {
        returns() implies value
    }
    if (!value) throw ValidationFailedException("Validation failed.")
}

/**
 * Validates that the provided boolean expression is false. If the expression evaluates to true,
 * it throws a ValidationFailedException with a lazily-evaluated error message.
 *
 * @param value The boolean value to validate. If true, a ValidationFailedException will be thrown.
 * @param lazyMessage A supplier to provide the error message only when the validation fails.
 * @since 4.2.0
 */
inline fun validateNot(value: Boolean, lazyMessage: Supplier<Any>) {
    contract {
        returns() implies value
    }
    if (value) {
        val message = lazyMessage()
        throw ValidationFailedException(message.toString())
    }
}
/**
 * Validates that the given condition is false. If the condition is true, a ValidationFailedException is thrown.
 *
 * @param value The condition to validate. Throws an exception if this value is true.
 * @since 4.2.0
 */
fun validateNot(value: Boolean) {
    contract {
        returns() implies value
    }
    if (value) throw ValidationFailedException("Validation failed.")
}

/**
 * Validates whether the provided value is null.
 * If the value is not null, a [ValidationFailedException] is thrown.
 *
 * @param value The value to be validated for nullity.
 * @throws ValidationFailedException if the provided value is not null.
 * @since 1.0.0
 */
fun validateNull(value: Any?) {
    contract {
        returns() implies (value == null)
    }
    if (value != null) throw ValidationFailedException("Value is not null.")
}
/**
 * Validates that the given [value] is null. If the validation fails, a [ValidationFailedException]
 * is thrown with the provided lazy message.
 *
 * This method uses Kotlin contracts to indicate that, if the function returns without throwing
 * an exception, the [value] is guaranteed to be null.
 *
 * @param value The value to be checked for nullity.
 * @param lazyMessage A lambda function that provides a detail message for the exception when
 * the validation fails. The message is evaluated lazily only when needed.
 *
 * @since 1.0.0
 */
fun validateNull(value: Any?, lazyMessage: Supplier<Any>) {
    contract {
        returns() implies (value == null)
    }
    if (value != null) throw ValidationFailedException(lazyMessage().toString())
}
/**
 * Validates that the provided value is not null. If the value is null, a [ValidationFailedException]
 * is thrown with a custom message generated by the provided lambda.
 *
 * @param value The value to be validated. If null, the validation will fail.
 * @param lazyMessage A lambda function to provide a custom exception message if validation fails.
 *                    The message is only computed when the value is null.
 * @throws ValidationFailedException if the provided value is null.
 * @since 1.0.0
 */
inline fun <T> validateNotNull(value: T?, lazyMessage: Supplier<Any>) {
    contract {
        returns() implies (value != null)
    }
    if (value == null) {
        val message = lazyMessage()
        throw ValidationFailedException(message.toString())
    }
}
/**
 * Validates that the given value is not null. Throws a [ValidationFailedException]
 * if the value is null.
 *
 * @param value the value to validate. Must not be null.
 * @throws ValidationFailedException if the value is null.
 * @since 1.0.0
 */
fun <T> validateNotNull(value: T?) {
    contract {
        returns() implies (value != null)
    }
    if (value == null) throw ValidationFailedException("Value is null.")
}

/**
 * Validates the input format by checking the specified condition.
 * Throws a `MalformedInputException` if the condition is not met.
 *
 * This method ensures that the provided input conforms to the expected rules or structure
 * defined by the boolean `value` parameter.
 *
 * @param value a boolean indicating whether the input format is valid. If `false`, an exception is thrown.
 * @throws MalformedInputException if the input does not meet the expected format.
 * @since 1.0.0
 */
fun validateInputFormat(value: Boolean) {
    contract {
        returns() implies value
    }
    if (!value) throw MalformedInputException()
}
/**
 * Validates the input format and throws a `MalformedInputException` if the provided condition is false.
 *
 * This method enforces input validation by verifying the given condition. If the condition is not met,
 * an exception with a lazily generated error message is thrown.
 *
 * @param value the condition to validate; if false, a `MalformedInputException` will be thrown.
 * @param lazyMessage a supplier function that generates the exception message when the validation fails.
 * @since 1.0.0
 */
fun validateInputFormat(value: Boolean, lazyMessage: Supplier<Any>) {
    contract {
        returns() implies value
    }
    if (!value) throw MalformedInputException(lazyMessage().toString())
}
/**
 * Validates if the input format is correct based on the specified condition.
 * Throws a [MalformedInputException] if the validation fails.
 *
 * @param value a boolean indicating the result of the input validation.
 *              If `false`, the method throws a [MalformedInputException].
 * @param `class` the [KClass] representing the expected type for the input.
 *              Used to construct the exception message if validation fails.
 * @since 1.0.0
 */
fun validateInputFormat(value: Boolean, `class`: KClass<*>) {
    contract {
        returns() implies value
    }
    if (!value) throw MalformedInputException(`class`)
}
/**
 * Validates the format of an input value based on a given type. If the validation fails,
 * a [MalformedInputException] is thrown with details about the expected type.
 *
 * @param value A boolean indicating whether the input format is valid.
 * @param type The expected type of the input used for validation.
 *             This is used to generate the exception message in case validation fails.
 * @throws MalformedInputException If the input format is invalid (i.e., `value` is false).
 * @since 1.0.0
 */
fun validateInputFormat(value: Boolean, type: KType) {
    contract {
        returns() implies value
    }
    if (!value) throw MalformedInputException(type)
}
/**
 * Validates whether the given input string matches the specified regular expression pattern.
 * Throws a `MalformedInputException` if the input does not conform to the expected format.
 *
 * @param value The input string to be validated.
 * @param regex The regular expression pattern that the input string is expected to match.
 * @throws MalformedInputException if the input string does not match the provided regex pattern.
 * @since 1.0.0
 */
fun validateInputFormat(value: CharSequence, regex: Regex) {
    if (!regex(value)) throw MalformedInputException()
}
/**
 * Validates whether a given input string matches a specified regular expression.
 * If the input does not match, a `MalformedInputException` is thrown with a custom message.
 *
 * @param value The input string to validate.
 * @param regex The regular expression against which the input string is matched.
 * @param lazyMessage A supplier function providing the detail message for the exception if the validation fails.
 * @throws MalformedInputException if the input does not match the specified regular expression.
 * @since 1.0.0
 */
fun validateInputFormat(value: CharSequence, regex: Regex, lazyMessage: Supplier<Any>) {
    if (!regex(value)) throw MalformedInputException(lazyMessage().toString())
}
/**
 * Validates that the provided input string matches the specified regular expression pattern.
 * If the input does not match the regex, a `MalformedInputException` is thrown, indicating
 * the expected type of the input.
 *
 * @param value The input string to be validated against the regular expression.
 * @param regex The regular expression pattern that the input should conform to.
 * @param `class` The `KClass` representing the expected type of the input. Used for error messaging in case of validation failure.
 * @throws MalformedInputException if the input string does not match the provided regular expression.
 * @since 1.0.0
 */
fun validateInputFormat(value: CharSequence, regex: Regex, `class`: KClass<*>) {
    if (!regex(value)) throw MalformedInputException(`class`)
}
/**
 * Validates whether the provided input matches the specified format defined by a regular expression.
 * If the input does not match the expected format, a `MalformedInputException` is thrown.
 *
 * @param value The input string to validate.
 * @param regex The regular expression defining the expected format.
 * @param type The expected type of the input, used in the exception message if validation fails.
 * @throws MalformedInputException If the input does not match the specified regular expression.
 * @since 1.0.0
 */
fun validateInputFormat(value: CharSequence, regex: Regex, type: KType) {
    if (!regex(value)) throw MalformedInputException(type)
}

/**
 * Validates that the provided `value` matches the expected `expectation`.
 * If the values do not match, an `ExpectationMismatchException` is thrown with a custom message.
 *
 * @param T The type of the value being validated.
 * @param value The actual value to be checked against the expectation.
 * @param expectation The value that the `value` parameter is expected to match.
 * @param lazyMessage A supplier that provides a custom error message in the event of a mismatch.
 * @return The original `value` if it matches the `expectation`.
 * @throws ExpectationMismatchException If the `value` does not match the `expectation`.
 * @since 1.0.0
 */
fun <T> expect(value: T, expectation: T, lazyMessage: Supplier<Any>) {
    if (value != expectation) throw ExpectationMismatchException(lazyMessage().toString())
}
/**
 * Validates that the provided value matches the expected value. If the values do not match,
 * an `ExpectationMismatchException` is thrown.
 *
 * @param T The type of the values being compared.
 * @param value The actual value to be validated.
 * @param expectation The expected value that the actual value should match.
 * @return The actual value if it matches the expected value.
 * @throws ExpectationMismatchException If the actual value does not match the expected value.
 * @since 1.0.0
 */
fun <T> expect(value: T, expectation: T) {
    if (value != expectation) throw ExpectationMismatchException("Value was expected as ${if (expectation.toString().isBlank()) "\"\"" else expectation}, but is $value.")
}
/**
 * Validates that the provided value matches the expected value. If the values do not match,
 * an `ExpectationMismatchException` is thrown with a detailed error message.
 *
 * @param T The type of the value being compared.
 * @param value The actual value to be validated.
 * @param expectation The expected value that the actual value is compared against.
 * @param property The `KProperty` associated with the value, used to provide additional context in the exception.
 * @param variableName The name of the variable being validated, used in the exception message for additional context.
 * @return The actual value if it matches the expected value.
 * @throws ExpectationMismatchException if the actual value does not match the expected value.
 * @since 1.0.0
 */
fun <T> expect(value: T, expectation: T, property: KProperty<*>, variableName: String) {
    if (value != expectation) throw ExpectationMismatchException(property, variableName, expectation, value)
}
/**
 * Validates that a given value matches the expected value. If the values do not match,
 * an `ExpectationMismatchException` is thrown with a descriptive message.
 *
 * @param T The type of the value and expectation.
 * @param value The actual value to be validated against the expectation.
 * @param expectation The expected value to compare with the actual value.
 * @param callable The callable function for which the mismatch occurred. This is included
 *                 in the exception message for better context.
 * @param parameterName The name of the parameter being validated. This is used to
 *                      provide detailed information in case of a mismatch.
 * @return The actual value `value` if it matches the `expectation`.
 * @throws ExpectationMismatchException if `value` does not equal `expectation`.
 * @since 1.0.0
 */
fun <T> expect(value: T, expectation: T, callable: KFunction<*>, parameterName: String) {
    if (value != expectation) throw ExpectationMismatchException(callable, parameterName, expectation, value)
}

/**
 * Throws an exception if the given `value` is equal to the `expectation`.
 *
 * @param T The type of the arguments `value` and `expectation`.
 * @param value The actual value to be tested.
 * @param expectation The value that `value` should not be equal to.
 * @param lazyMessage A supplier function to provide a custom error message if the expectation is not met.
 *                    This message will be used in the thrown exception.
 * @throws ExpectationMismatchException if `value` is equal to `expectation`.
 * @since 4.2.0
 */
fun <T> expectNot(value: T, expectation: T, lazyMessage: Supplier<Any>) {
    if (value == expectation) throw ExpectationMismatchException(lazyMessage().toString())
}
/**
 * Verifies that the given value does not match the expected value.
 * Throws an `ExpectationMismatchException` if the value matches the expectation.
 *
 * @param T The type of the values being compared.
 * @param value The actual value to be evaluated.
 * @param expectation The value that the actual value is expected not to match.
 * @throws ExpectationMismatchException If the provided value matches the expectation.
 * @since 4.2.0
 */
fun <T> expectNot(value: T, expectation: T) {
    if (value == expectation) throw ExpectationMismatchException("Value was expected as ${if (expectation.toString().isBlank()) "\"\"" else expectation}, but is $value.")
}
/**
 * Validates that the given `value` is not equal to the specified `expectation`.
 * If the validation fails, an `ExpectationMismatchException` is thrown with details about the mismatch.
 *
 * @param T the type of the values being compared
 * @param value the actual value to validate
 * @param expectation the value that the actual value should not match
 * @param property the `KProperty` associated with the value, used for detailed exception reporting
 * @param variableName the name of the variable being evaluated, used for detailed exception reporting
 * @since 4.2.0
 */
fun <T> expectNot(value: T, expectation: T, property: KProperty<*>, variableName: String) {
    if (value == expectation) throw ExpectationMismatchException(property, variableName, expectation, value)
}
/**
 * Validates that the given value does not match the expected value for a specific parameter
 * of a callable function. If the values match, an `ExpectationMismatchException` is thrown.
 *
 * @param T The type of the value being compared.
 * @param value The actual value that is being checked.
 * @param expectation The value that the actual value must not be equal to.
 * @param callable The callable function associated with the parameter being validated.
 * @param parameterName The name of the parameter being checked.
 * @throws ExpectationMismatchException if the actual value matches the expected value.
 * @since 4.2.0
 */
fun <T> expectNot(value: T, expectation: T, callable: KFunction<*>, parameterName: String) {
    if (value == expectation) throw ExpectationMismatchException(callable, parameterName, expectation, value)
}

/**
 * Validates that the given value is an instance of the specified expected class.
 * If the value is not an instance of the expected class, a `ClassMismatchException`
 * is thrown with the provided error message.
 *
 * @param T The type of the value being validated.
 * @param value The value to check for type conformity.
 * @param expectedClass The expected class to which the value should belong.
 * @param lazyMessage A supplier function that provides the error message to be used
 *                    in the exception if a mismatch occurs.
 * @throws ClassMismatchException If the value is not an instance of the expected class.
 * @since 1.0.0
 */
fun <T : Any> expectClass(value: T, expectedClass: KClass<*>, lazyMessage: Supplier<Any>) {
    if (!expectedClass.isInstance(value)) throw ClassMismatchException(lazyMessage().toString())
}

/**
 * Verifies whether the given value is an instance of the specified class type.
 * If the value is not an instance of the expected class, a [ClassMismatchException] is thrown.
 *
 * @param T The type of the value being checked.
 * @param value The value to verify against the expected class.
 * @param expectedClass The class type to compare the given value against.
 * @throws ClassMismatchException if the value is not an instance of the expected class.
 * @since 1.0.0
 */
fun <T : Any> expectClass(value: T, expectedClass: KClass<*>) {
    if (!expectedClass.isInstance(value)) throw ClassMismatchException(expectedClass::class, value::class)
}

/**
 * Verifies that the provided value is null. If the value is not null, an exception is thrown
 * with a message generated by the supplied function.
 *
 * @param T The type of the value being checked.
 * @param value The value to check for nullity.
 * @param lazyMessage A supplier function to generate the error message if the value is not null.
 * @throws ExpectationMismatchException If the supplied value is not null.
 * @since 1.0.0
 */
fun <T> expectNull(value: T, lazyMessage: Supplier<Any>) {
    if (value != null) throw ExpectationMismatchException(lazyMessage().toString())
}

/**
 * Validates that the given value is null. If the provided value is not null, an
 * `ExpectationMismatchException` is thrown, indicating that an unexpected value was encountered.
 *
 * This function is useful for assertions or validation checks where explicitly null values are expected.
 *
 * @param value The value to be validated against a null expectation. If the value is not null,
 *              an `ExpectationMismatchException` is thrown.
 * @since 1.0.0
 */
fun <T> expectNull(value: T) {
    if (value != null) throw ExpectationMismatchException("Variable was expected to be null, but was $value.")
}

/**
 * Validates that the provided value is null; if not, it throws an `ExpectationMismatchException`.
 *
 * @param value The value to check for nullability. If this value is not null, an exception is thrown.
 * @param property The Kotlin property associated with the value being checked.
 * @param variableName The name of the variable being checked, used for descriptive error messages.
 * @throws ExpectationMismatchException if the provided value is not null.
 * @since 1.0.0
 */
fun <T> expectNull(value: T?, property: KProperty<*>, variableName: String) {
    if (value != null) throw ExpectationMismatchException(property, variableName, null, value)
}

/**
 * Checks if the provided value is null, and throws an `ExpectationMismatchException` if it is not null.
 * This function is commonly used to enforce nullability expectations on function parameters.
 *
 * @param T The type of the value.
 * @param value The value to be checked. If this is not null, an `ExpectationMismatchException` is thrown.
 * @param callable The callable function for which the nullability expectation is being enforced.
 * @param parameterName The name of the parameter being validated.
 * @since 1.0.0
 */
fun <T> expectNull(value: T?, callable: KFunction<*>, parameterName: String) {
    if (value != null) throw ExpectationMismatchException(callable, parameterName, null, value)
}