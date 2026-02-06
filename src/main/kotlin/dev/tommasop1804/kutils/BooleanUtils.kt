@file:JvmName("BooleanUtilsKt")
@file:Suppress("unused", "kutils_null_check")
@file:Since("1.0.0")
@file:OptIn(ExperimentalContracts::class, ExperimentalExtendedContracts::class)

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.exceptions.ExpectationMismatchException
import dev.tommasop1804.kutils.exceptions.ValidationFailedException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty

/**
 * Checks if any of the given Boolean values is `true`.
 *
 * This function evaluates the provided Boolean values and returns `true` if at least one
 * of the values is non-null and equals `true`. If none of the values are `true` or all
 * values are null, it returns `false`.
 *
 * @param values a vararg parameter of nullable Boolean values to evaluate
 * @since 1.0.0
 */
fun anyTrue(vararg values: Boolean?) = values.any { it == true }
/**
 * Checks if any element in the given iterable of nullable Booleans is `true`.
 *
 * @param values an iterable of nullable Boolean values to check.
 * @since 1.0.0
 */
fun anyTrue(values: Iterable<Boolean?>) = values.any { it == true }

/**
 * Checks if all provided Boolean values are true.
 * Returns true if all the values are `true`, false if at least one is not `true`,
 * or the collection is empty.
 *
 * @param values A variable number of nullable Boolean values to evaluate.
 * @return True if all provided Boolean values are true and the array is non-empty, otherwise false.
 * @since 1.0.0
 */
fun allTrue(vararg values: Boolean?) = values.isNotEmpty() && values.all { it == true }
/**
 * Checks if all elements in the provided iterables of nullable booleans are `true`.
 * This function returns `true` if all iterable instances contain only `true` values
 * and the provided varargs are not empty.
 *
 * @param values Vararg parameter consisting of iterable collections of nullable Boolean values.
 *               Each iterable is checked to ensure all elements are `true`.
 * @return `true` if all elements in all provided iterables are `true` and the vararg is not empty, otherwise `false`.
 * @since 1.0.0
 */
fun allTrue(values: Iterable<Boolean?>) = values.toList().isNotEmpty() && values.all { it == true }

/**
 * Checks if all the given Boolean? values are false.
 *
 * This function evaluates the provided Boolean? values and returns true
 * if the collection is not empty and all the elements are explicitly false.
 *
 * @param values The vararg collection of Boolean? values to evaluate.
 * @return True if all values are false and the collection is non-empty, false otherwise.
 * @since 1.0.0
 */
fun allFalse(vararg values: Boolean?) = values.isNotEmpty() && values.all { it == false }
/**
 * Determines whether all elements in the given iterable are `false`.
 * This method returns `true` if all elements are explicitly `false` and the iterable is not empty.
 * If the iterable contains `null` values, they are ignored in the evaluation.
 *
 * @param values an iterable collection of nullable boolean values to evaluate
 * @since 1.0.0
 */
fun allFalse(values: Iterable<Boolean?>) = values.toList().isNotEmpty() && values.all { it == false }

/**
 * Checks if any value in the given array of nullable Booleans is explicitly false.
 *
 * @param values a vararg of nullable Boolean values to check.
 * @return true if at least one value is false, otherwise false.
 * @since 1.0.0
 */
fun anyFalse(vararg values: Boolean?) = values.any { it == false }
/**
 * Checks if any of the elements in the given iterable are explicitly `false`.
 *
 * @param values an iterable collection of nullable Boolean values to be checked
 * @return `true` if at least one element in the iterable is `false`, otherwise `false`
 * @since 1.0.0
 */
fun anyFalse(values: Iterable<Boolean?>) = values.any { it == false }

/**
 * Counts the number of non-null Boolean values that are true in the given input.
 *
 * @param values a variable number of nullable Boolean values to be evaluated
 * @return the count of values that are true
 * @since 1.0.0
 */
fun countTrue(vararg values: Boolean?) = values.count { it == true }
/**
 * Counts the number of `true` values in the given iterable of nullable booleans.
 *
 * @param values An iterable collection of nullable Boolean values to be evaluated.
 * @return The count of values that are `true`.
 * @since 1.0.0
 */
fun countTrue(values: Iterable<Boolean?>) = values.count { it == true }

/**
 * Counts the number of false values in the provided boolean array.
 *
 * @param values The variable number of nullable boolean values to evaluate.
 * @return The count of elements that are explicitly false.
 * @since 1.0.0
 */
fun countFalse(vararg values: Boolean?) = values.count { it == false }
/**
 * Counts the number of false values in the given iterable of nullable Booleans.
 *
 * @param values an iterable collection of nullable Boolean values
 * @return the count of false values within the provided iterable
 * @since 1.0.0
 */
fun countFalse(values: Iterable<Boolean?>) = values.count { it == false }

/**
 * Extension property for nullable Boolean type that evaluates whether the value is explicitly `true`.
 * Returns `true` if the Boolean is not null and has the value `true`; otherwise, returns `false`.
 *
 * @since 1.0.0
 */
val Boolean?.isTrue get() = this == true
/**
 * Extension property to determine if a nullable Boolean is explicitly `false`.
 *
 * This property returns `true` if the Boolean is not null and equals `false`,
 * otherwise it returns `false`.
 *
 * @receiver A nullable Boolean to check if it is explicitly false.
 * @return `true` if the receiver is `false`, otherwise `false`.
 * @since 1.0.0
 */
val Boolean?.isFalse get() = this == false
/**
 * Extension property for nullable Boolean values that checks if the value is either `true` or `null`.
 * This can be used to simplify conditional checks involving nullable Booleans.
 *
 * @receiver A nullable Boolean value.
 * @return `true` if the Boolean is either `true` or `null`, otherwise `false`.
 * @since 1.0.0
 */
val Boolean?.isNullOrTrue get() = this == null || this
/**
 * Extension property to determine if a nullable Boolean is either `null` or `false`.
 *
 * @return `true` if the Boolean is `null` or `false`; `false` otherwise.
 * @since 1.0.0
 */
val Boolean?.isNullOrFalse get() = this == null || !this

/**
 * Invokes one of the provided lambda expressions based on the boolean value.
 * If the boolean is `true`, it executes the `onTrue` lambda; otherwise, it executes the `onFalse` lambda.
 *
 * @param T the generic return type of the lambda expressions
 * @param onTrue the lambda expression to execute if the boolean is `true`
 * @param onFalse the lambda expression to execute if the boolean is `false`
 * @return the result of evaluating either `onTrue` or `onFalse`
 * @since 1.0.0
 */
inline operator fun <T> Boolean.invoke(onTrue: Supplier<T?> = { null }, onFalse: Supplier<T?> = { null }): T? {
    contract {
        callsInPlace(onTrue, InvocationKind.AT_MOST_ONCE)
        this@invoke holdsIn onTrue
    }
    return if (this) onTrue() else onFalse()
}
/**
 * Invokes the Boolean as a functional operator to return one of the two provided values
 * based on the Boolean's value.
 *
 * @param T the type of the values being returned.
 * @param onTrue the value returned if the Boolean is true.
 * @param onFalse the value returned if the Boolean is false.
 * @return the value of `onTrue` if the Boolean is true, otherwise the value of `onFalse`.
 * @since 1.0.0
 */
operator fun <T> Boolean.invoke(onTrue: T? = null, onFalse: T? = null) = if (this) onTrue else onFalse

/**
 * Ensures that the current Boolean value is `true`.
 * If the value is `false`, it throws an `IllegalArgumentException` or a provided throwable.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception.
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @return the value `true` if the current Boolean value is `true`.
 * @throws IllegalArgumentException if the current Boolean value is `false` and no custom throwable is provided.
 * @since 1.0.0
 */
fun Boolean.requireTrue(causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@requireTrue
    }
    if (!this) throw if (causeOf.isNull()) IllegalArgumentException("Invalid argument: should be true", cause) else causeOf.initCause(IllegalArgumentException("Invalid argument: should be true", cause))
    return true
}
/**
 * Ensures that the boolean value is `true`, throwing an exception if the condition is not met.
 * If `false`, it throws an `IllegalArgumentException` or the provided throwable with a lazily evaluated
 * error message.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @param lazyMessage a supplier function that provides the error message to include in the thrown exception.
 * @return the boolean value `true` if the condition is met.
 * @since 1.0.0
 */
fun Boolean.requireTrue(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): Boolean {
    contract {
        returns() implies this@requireTrue
    }
    if (!this) throw if (causeOf.isNull()) IllegalArgumentException(lazyMessage().toString(), cause) else causeOf.initCause(IllegalArgumentException(lazyMessage().toString(), cause))
    return true
}
/**
 * Ensures the boolean value is true, otherwise throws an exception provided by the given exception supplier.
 *
 * @param lazyException a supplier that generates an exception to be thrown if the boolean value is false
 * @return the boolean value `true`, if the check passes
 * @since 1.0.0
 */
fun Boolean.requireTrueOrThrow(lazyException: ThrowableSupplier): Boolean {
    contract {
        returns() implies this@requireTrueOrThrow
    }
    if (!this) throw lazyException()
    return true
}

/**
 * Ensures that the calling Boolean is false.
 * If the Boolean is true, an exception is thrown with the provided cause or a default cause.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @return Returns false if the Boolean is false.
 * @since 1.0.0
 */
fun Boolean.requireFalse(causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@requireFalse
    }
    if (this) throw if (causeOf.isNull()) IllegalArgumentException("Invalid argument: should be false", cause) else causeOf.initCause(IllegalArgumentException("Invalid argument: should be false", cause))
    return false
}
/**
 * Ensures that the boolean value is `false`. If the value is `true`, it throws an exception.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @param lazyMessage a supplier providing a message used for creating the exception
 * @return the boolean value `false` if the condition is satisfied
 * @since 1.0.0
 */
fun Boolean.requireFalse(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): Boolean {
    contract {
        returns() implies !this@requireFalse
    }
    if (this) throw if (causeOf.isNull()) IllegalArgumentException(lazyMessage().toString(), cause) else causeOf.initCause(IllegalArgumentException(lazyMessage().toString(), cause))
    return false
}
/**
 * Ensures that the Boolean value is false, throwing the specified exception if it is true.
 *
 * If the Boolean value is true, the exception supplied by the given `lazyException` supplier
 * will be thrown. If the Boolean value is false, it will simply return false.
 *
 * @param lazyException a supplier function that provides the exception to throw if the Boolean value is true
 * @return false if the Boolean value is false
 * @throws Exception the exception provided by the `lazyException` supplier when the Boolean value is true
 * @since 1.0.0
 */
fun Boolean.requireFalseOrThrow(lazyException: ThrowableSupplier): Boolean {
    contract {
        returns() implies !this@requireFalseOrThrow
    }
    if (this) throw lazyException()
    return false
}

/**
 * Verifies whether the invoking boolean value is `true`.
 * If the value is `false`, it throws an exception with an optional cause.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @return `true` if the invoking boolean value is `true`.
 * @throws IllegalStateException if the invoking boolean value is `false` and no cause is provided.
 * @throws Throwable if the invoking boolean value is `false` and a cause is provided, with its cause being initialized to an IllegalStateException.
 * @since 1.0.0
 */
fun Boolean.checkTrue(causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@checkTrue
    }
    if (!this) throw if (causeOf.isNull()) IllegalStateException("Invalid state: should be true", cause) else causeOf.initCause(IllegalStateException("Invalid state: should be true", cause))
    return true
}
/**
 * Checks if the boolean value is `true`. If not, throws an exception with the provided lazy message and optional cause.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @param lazyMessage a supplier function providing the message to be used in the exception if the boolean is `false`.
 * @return `true` if the boolean value is `true`; otherwise, an exception is thrown.
 * @since 1.0.0
 */
fun Boolean.checkTrue(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): Boolean {
    contract {
        returns() implies this@checkTrue
    }
    if (!this) throw if (causeOf.isNull()) IllegalStateException(lazyMessage().toString(), cause) else causeOf.initCause(IllegalStateException(lazyMessage().toString(), cause))
    return true
}

/**
 * Ensures the boolean value is false. If the value is true, it throws an exception.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @return `false` if the boolean value is false.
 * @throws IllegalStateException if the boolean value is true. If `causeOf` is provided, it will be used as the cause for the exception, with an additional "Invalid state: should
 *  be false" message.
 * @since 1.0.0
 */
fun Boolean.checkFalse(causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@checkFalse
    }
    if (this) throw if (causeOf.isNull()) IllegalStateException("Invalid state: should be false", cause) else causeOf.initCause(IllegalStateException("Invalid state: should be false", cause))
    return false
}
/**
 * Checks if the value of the Boolean is `false`. If the value is `true`, an exception is thrown.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @param lazyMessage A supplier providing the error message for the exception if thrown.
 * @return Returns `false` if the check passes without throwing an exception.
 * @since 1.0.0
 */
fun Boolean.checkFalse(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): Boolean {
    contract {
        returns() implies !this@checkFalse
    }
    if (this) throw if (causeOf.isNull()) IllegalStateException(lazyMessage().toString(), cause) else causeOf.initCause(IllegalStateException(lazyMessage().toString(), cause))
    return false
}

/**
 * Validates whether the current Boolean value is `true`. If the value is `false`, a
 * [ValidationFailedException] is thrown, optionally with a provided cause.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @return The Boolean value `true`, if validation passes.
 * @throws ValidationFailedException If the Boolean value is `false`.
 * @since 1.0.0
 */
fun Boolean.validateTrue(causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@validateTrue
    }
    if (!this) throw if (causeOf.isNull()) ValidationFailedException("Value is not true.", cause) else causeOf.initCause(ValidationFailedException("Value is not true.", cause))
    return true
}
/**
 * Validates whether the boolean value is true and throws an exception if the validation fails.
 * This method is typically used to ensure a condition has been met, such as input validation
 * or enforcing preconditions.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @param lazyMessage A supplier for a custom error message. The supplier is evaluated only
 *                    if the validation fails, allowing for delayed and potentially expensive
 *                    computation of error messages.
 * @return Returns `true` if the boolean value is `true`.
 * @throws Throwable If the boolean value is `false`, throws the provided `causeOf` or a new [ValidationFailedException].
 * @since 1.0.0
 */
fun Boolean.validateTrue(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): Boolean {
    contract {
        returns() implies this@validateTrue
    }
    if (!this) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause) else causeOf.initCause(ValidationFailedException(lazyMessage().toString(), cause))
    return true
}
/**
 * Validates that the boolean value is `true`. If it is not, a `ValidationFailedException` is thrown with
 * optional property, variable name, message, and cause details.
 *
 * @param property An optional `KProperty` associated with the validation process. Defaults to `null`.
 * @param variableName An optional name of the variable being validated. Defaults to `null`.
 * @param message An optional custom message to include in case of failure. Defaults to `null`.
 * @param causeOf The throwable that should cause this validation failure, if applicable. Defaults to `null`.
 * @param cause An additional throwable cause that can be associated with the validation failure. Defaults to `null`.
 * @return Returns `true` if the boolean value is true.
 * @throws ValidationFailedException if the boolean value is not true.
 * @since 1.0.0
 */
fun Boolean.validateTrue(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@validateTrue
    }
    if (!this) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is not true", cause) else causeOf.initCause(ValidationFailedException(property, variableName, message ?: "is not true", cause))
    return true
}
/**
 * Validates that a boolean value is `true`. If the value is `false`, a `ValidationFailedException` is thrown.
 *
 * @param property the primary property being validated, or null if not applicable
 * @param variable an optional secondary property providing additional context for the validation, or null if not applicable
 * @param message an optional custom error message to be included in the exception, or null to use a default message
 * @param causeOf an optional pre-existing exception to be used as the cause of the validation failure, or null to create a new exception
 * @param cause an optional underlying cause to be attached to the generated exception, or null if no underlying cause is specified
 * @return `true` if the validation passes (i.e., the boolean value is `true`)
 * @since 1.0.0
 */
fun Boolean.validateTrue(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@validateTrue
    }
    if (!this) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is not true", cause) else causeOf.initCause(ValidationFailedException(property, variable, message ?: "is not true", cause))
    return true
}
/**
 * Validates if the Boolean value is `true`. If not, it throws a `ValidationFailedException`.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation relates. Can be null.
 * @param parameterName The name of the parameter in the callable that failed validation. Can be null.
 * @param message Optional custom message to describe the validation failure. Defaults to "is not true" if not provided.
 * @param causeOf An optional existing `Throwable` to be used as the main exception, with the validation failure as its cause. Can be null.
 * @param cause An optional underlying cause (`Throwable`) for the validation error. Can be null.
 * @return Returns `true` if the validation succeeds (i.e., the Boolean value is `true`).
 * @throws ValidationFailedException If the Boolean value is `false`.
 * @since 1.0.0
 */
fun Boolean.validateTrue(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@validateTrue
    }
    if (!this) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is not true", cause) else causeOf.initCause(ValidationFailedException(callable, parameterName, message ?: "is not true", cause))
    return true
}
/**
 * Validates if the Boolean value is true. If not, throws a [ValidationFailedException] with optional detailed information.
 *
 * @param callable the [KFunction] associated with the validation context. Nullable if no callable is provided.
 * @param parameter the [KParameter] representing the parameter involved in the validation context. Nullable if no parameter is provided.
 * @param message an optional custom message to include in the exception if validation fails. Defaults to a standard message if null.
 * @param causeOf an optional explicitly specified exception to be used as the cause of validation failure. If not null, it will be initialized with the internally created exception
 * .
 * @param cause an optional throwable to attach to the exception for additional context about the failure.
 * @return true if the Boolean value is valid (true), otherwise throws a [ValidationFailedException].
 * @since 1.0.0
 */
fun Boolean.validateTrue(callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@validateTrue
    }
    if (!this) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is not true", cause) else causeOf.initCause(ValidationFailedException(callable, parameter, message ?: "is not true", cause))
    return true
}
/**
 * Validates that the Boolean value is true. If the value is false, a `ValidationFailedException`
 * is thrown with optional contextual information including the callable name, parameter name,
 * custom message, or root cause.
 *
 * @param callableName The name of the callable (e.g., function or method) related to the validation.
 * @param parameterName The name of the parameter being validated, or null if not applicable.
 * @param message An optional custom message describing the validation failure.
 * @param causeOf An optional existing `Throwable` to be enhanced with `ValidationFailedException`.
 * @param cause An optional underlying cause for the exception, if the validation fails.
 * @return Returns true if the validation passes.
 * @throws ValidationFailedException if the Boolean value is false and validation fails.
 * @since 1.0.0
 */
fun Boolean.validateTrue(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@validateTrue
    }
    if (!this) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is not true", cause) else causeOf.initCause(ValidationFailedException(callableName, parameterName, message ?: "is not true", cause))
    return true
}
/**
 * Validates whether the Boolean instance is `true`. If not, a [ValidationFailedException] is thrown.
 *
 * @param callableName The name of the callable (e.g., function or property) associated with the validation, or null if not applicable.
 * @param parameter The parameter (represented as a [KParameter]) related to the validation, or null if not applicable.
 * @param message An optional error message providing additional details. If not provided, a default message is used.
 * @param causeOf An optional already-existing [Throwable] used as the main exception being thrown, which may also chain a new [ValidationFailedException] as its cause.
 * @param cause An optional cause to provide additional context for chaining in the exception. This is passed to the [ValidationFailedException].
 * @return `true` if the validation succeeds (i.e., the Boolean is `true`).
 * @throws ValidationFailedException If the Boolean is `false`, with additional context provided by parameters.
 * @since 1.0.0
 */
fun Boolean.validateTrue(callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@validateTrue
    }
    if (!this) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is not true", cause) else causeOf.initCause(ValidationFailedException(callableName, parameter, message ?: "is not true", cause))
    return true
}

/**
 * Validates that the boolean value is false. If the value is true, throws a [ValidationFailedException].
 * Optionally, a specific cause can be provided, which will be linked to the thrown exception.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @return Always returns false if no exception is thrown.
 * @throws ValidationFailedException if the boolean value is true.
 * @since 1.0.0
 */
fun Boolean.validateFalse(causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@validateFalse
    }
    if (this) throw if (causeOf.isNull()) ValidationFailedException("Value is not false.", cause) else causeOf.initCause(ValidationFailedException("Value is not false.", cause))
    return false
}
/**
 * Validates that the receiver boolean is `false`. If the boolean is `true`, a validation exception is thrown.
 *
 * The exception thrown can be a custom one provided via the `causeOf` parameter or a default
 * `ValidationFailedException` with a message generated from the `lazyMessage` supplier.
 * This method is typically used for invariant checks where `false` indicates a valid state.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @param lazyMessage A lambda supplying the error message for the validation exception.
 *                    The message is only computed if an exception is about to be thrown.
 * @return Always returns `false` if the receiver boolean is `false`.
 * @since 1.0.0
 */
fun Boolean.validateFalse(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): Boolean {
    contract {
        returns() implies !this@validateFalse
    }
    if (this) throw if (causeOf.isNull()) ValidationFailedException(lazyMessage().toString(), cause) else causeOf.initCause(ValidationFailedException(lazyMessage().toString(), cause))
    return false
}
/**
 * Validates that the Boolean value is `false`.
 *
 * If the value is `true`, this method throws a [ValidationFailedException].
 *
 * @param property The property associated with the validation, which can be used to provide additional context
 *                 in the validation error message. Can be `null` if not applicable.
 * @param variableName Optional name of the variable being validated. If specified, it will be included in the
 *                     validation error message. Defaults to `null`.
 * @param message Custom error message describing the validation failure. If not provided, a default message
 *                "is not false" is used.
 * @param causeOf An optional throwable to use as the root cause of the exception. If provided, it will be
 *                initialized with a [ValidationFailedException] that encapsulates validation details.
 * @param cause An optional throwable representing an additional cause of the exception, or `null` if not applicable.
 * @return `true` if the value is validated as `false`. If the value is `true`, an exception is thrown instead.
 * @since 1.0.0
 */
fun Boolean.validateFalse(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@validateFalse
    }
    if (this) throw if (causeOf.isNull()) ValidationFailedException(property, variableName, message ?: "is not false", cause) else causeOf.initCause(ValidationFailedException(property, variableName, message ?: "is not false", cause))
    return false
}
/**
 * Validates whether the boolean receiver is `false`.
 * If the receiver is `true`, a `ValidationFailedException` is thrown.
 *
 * @param property The main KProperty associated with the validation failure, or null if not specified.
 * @param variable An optional secondary KProperty that provides additional context, or null if not specified.
 * @param message An optional message providing additional details about the validation failure. Defaults to "is not false" if null.
 * @param causeOf A pre-existing throwable to chain as a cause. If specified, it will be initialized with a `ValidationFailedException`.
 * @param cause An optional underlying cause of the exception.
 * @return Returns `true` if the receiver is `false`.
 * @since 1.0.0
 */
fun Boolean.validateFalse(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@validateFalse
    }
    if (this) throw if (causeOf.isNull()) ValidationFailedException(property, variable, message ?: "is not false", cause) else causeOf.initCause(ValidationFailedException(property, variable, message ?: "is not false", cause))
    return false
}
/**
 * Validates that the boolean value is `false`. If the boolean value is `true`, a `ValidationFailedException`
 * is thrown with the given parameters.
 *
 * @param callable The Kotlin function (`KFunction`) to which the validation error is related. Can be null.
 * @param parameterName The optional parameter name from the callable that caused the validation issue. Can be null.
 * @param message An optional custom message providing details about the validation failure. Default message is "is not false".
 * @param causeOf The optional existing exception that caused the validation failure. The exception will wrap the validation exception.
 * @param cause The optional underlying cause for the validation failure represented as a `Throwable`.
 * @return Returns `true` if the validation passes, i.e., the boolean value is `false`.
 *
 * @since 1.0.0
 */
fun Boolean.validateFalse(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@validateFalse
    }
    if (this) throw if (causeOf.isNull()) ValidationFailedException(callable, parameterName, message ?: "is not false", cause) else causeOf.initCause(ValidationFailedException(callable, parameterName, message ?: "is not false", cause))
    return false
}
/**
 * Validates that the Boolean value is `false`. If the value is `true`, it throws a `ValidationFailedException`.
 *
 * @param callable an optional [KFunction] that provides context about the function where the validation occurs
 * @param parameter an optional [KParameter] representing the parameter involved in the validation
 * @param message an optional custom message describing the validation failure
 * @param causeOf an optional [Throwable] that will serve as the cause of the exception if provided
 * @param cause an optional [Throwable] that provides additional context for the validation failure
 * @return `true` if the validation succeeds (i.e., the Boolean value is `false`)
 * @since 1.0.0
 */
fun Boolean.validateFalse(callable: KFunction<*>?, parameter: KParameter? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@validateFalse
    }
    if (this) throw if (causeOf.isNull()) ValidationFailedException(callable, parameter, message ?: "is not false", cause) else causeOf.initCause(ValidationFailedException(callable, parameter, message ?: "is not false", cause))
    return false
}
/**
 * Validates that the current Boolean value is `false`. If the value is `true`, a `ValidationFailedException`
 * is thrown with the specified details.
 *
 * @param callableName the name of the callable (e.g., function or method) related to the validation failure
 * @param parameterName the name of the parameter being validated, or null if not specified
 * @param message an optional custom message to include in the exception if validation fails
 * @param causeOf an optional `Throwable` to be used as the root cause of the exception if validation fails
 * @param cause an optional additional `Throwable` providing extra context for the exception
 * @return `true` if validation passes (i.e., the Boolean value is `false`)
 * @since 1.0.0
 */
fun Boolean.validateFalse(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@validateFalse
    }
    if (this) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameterName, message ?: "is not false", cause) else causeOf.initCause(ValidationFailedException(callableName, parameterName, message ?: "is not false", cause))
    return false
}
/**
 * Validates that the Boolean value is `false`.
 *
 * Throws a [ValidationFailedException] if the Boolean value is `true`.
 *
 * @param callableName The name of the callable (e.g., function or property) where the validation occurs, or null if not specified.
 * @param parameter The [KParameter] representing the parameter related to the validation, or null if not applicable.
 * @param message An optional error message providing additional context for the validation failure.
 * @param causeOf The initial cause of the validation failure, which may be another [Throwable], or null if not specified.
 * @param cause An optional additional cause for the exception, or null if not applicable.
 * @return `true` if the validation succeeds (i.e., the Boolean is `false`); otherwise, throws an exception.
 * @since 1.0.0
 */
fun Boolean.validateFalse(callableName: String?, parameter: KParameter? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@validateFalse
    }
    if (this) throw if (causeOf.isNull()) ValidationFailedException(callableName, parameter, message ?: "is not false", cause) else causeOf.initCause(ValidationFailedException(callableName, parameter, message ?: "is not false", cause))
    return false
}

/**
 * Verifies that the current `Boolean` value is `true`. If the value is `false`, an exception is thrown.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @return The `Boolean` value, which will always be `true` if no exception is thrown.
 * @throws ExpectationMismatchException if the value is `false` and no cause is provided.
 * @throws Throwable if the value is `false` and a non-null `causeOf` is provided.
 * @since 1.0.0
 */
fun Boolean.expectTrue(causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@expectTrue
    }
    if (!this) throw if (causeOf.isNull()) ExpectationMismatchException("Value was expected to be true, but was ${false}.", cause) else causeOf.initCause(ExpectationMismatchException("Value was expected to be true, but was ${false}.", cause))
    return true
}
/**
 * Validates that the Boolean value is `true`. If the value is `false`, throws an `ExpectationMismatchException`
 * with an optional cause or a custom message provided by the `lazyMessage` supplier.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @param lazyMessage A supplier providing the error message to be used in the exception if the validation fails.
 * @return Returns `true` if the boolean value is `true`.
 * @throws ExpectationMismatchException If the boolean value is `false` and no `causeOf` is provided.
 * @since 1.0.0
 */
fun Boolean.expectTrue(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): Boolean {
    contract {
        returns() implies this@expectTrue
    }
    if (!this) throw if (causeOf.isNull()) ExpectationMismatchException(lazyMessage().toString(), cause) else causeOf.initCause(ExpectationMismatchException(lazyMessage().toString(), cause))
    return true
}
/**
 * Validates that the `Boolean` value is `true`, throwing an exception if the value is `false`.
 *
 * This function checks whether the `Boolean` instance it is invoked on is `true`. If the value
 * is `false`, it throws an `ExpectationMismatchException` with optional details about the
 * property or variable associated with the validation, expected value, and actual value:
 * - If `causeOf` is provided, it will be used as the exception thrown, initialized with the
 *   `ExpectationMismatchException` as its cause.
 * - Otherwise, an `ExpectationMismatchException` is directly thrown.
 *
 * @param property An optional reflection-based property reference used to provide contextual
 *                 information about the property involved in the expectation mismatch.
 * @param variableName An optional name of the variable being validated, used for more human-readable error messages.
 * @param causeOf An optional `Throwable` provided as the primary exception cause if validation fails.
 * @param cause An optional additional `Throwable` representing a secondary cause of the failure.
 * @return Always returns `true` if the validation passes, as the input `Boolean` is expected to be `true`.
 * @since 1.0.0
 */
fun Boolean.expectTrue(property: KProperty<*>?, variableName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@expectTrue
    }
    if (!this) throw if (causeOf.isNull()) ExpectationMismatchException(property, variableName,
        expectation = true,
        value = false,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(property, variableName,
        expectation = true,
        value = false,
        cause = cause
    ))
    return true
}
/**
 * Asserts that the boolean receiver is `true`. If not, throws an `ExpectationMismatchException`
 * or optionally another specified `Throwable`.
 *
 * @param property the primary `KProperty` involved in the expectation, or null if not applicable
 * @param variable an optional secondary `KProperty` related to the expectation, or null if not applicable
 * @param causeOf an optional `Throwable` to be thrown instead of `ExpectationMismatchException`, or null if not applicable
 * @param cause an optional cause passed to the `ExpectationMismatchException`, or null if not applicable
 * @return true if the receiver is `true`; otherwise, an exception is thrown
 * @since 1.0.0
 */
fun Boolean.expectTrue(property: KProperty<*>?, variable: KProperty<*>?, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@expectTrue
    }
    if (!this) throw if (causeOf.isNull()) ExpectationMismatchException(property, variable,
        expectation = true,
        value = false,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(property, variable,
        expectation = true,
        value = false,
        cause = cause
    ))
    return true
}
/**
 * Ensures that the Boolean receiver is true, throwing an `ExpectationMismatchException`
 * if it is false. This function is useful for validating expectations in contexts where
 * a specific condition must be met.
 *
 * @param callable The Kotlin function related to the expectation being validated.
 *                 Can be `null` if the context of the callable is not available.
 * @param parameterName The name of the parameter being checked, if applicable. Can be `null`.
 * @param causeOf A specific throwable to be used as the cause of the exception
 *                if the expectation fails. Can be `null`.
 * @param cause The underlying cause of the exception. Can be `null`.
 * @return `true` if the Boolean receiver is true. If the receiver is false, an exception is thrown.
 * @since 1.0.0
 */
fun Boolean.expectTrue(callable: KFunction<*>?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@expectTrue
    }
    if (!this) throw if (causeOf.isNull()) ExpectationMismatchException(callable, parameterName,
        expectation = true,
        value = false,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(callable, parameterName,
        expectation = true,
        value = false,
        cause = cause
    ))
    return true
}
/**
 * Ensures that the Boolean value is true; otherwise, throws an `ExpectationMismatchException`
 * with the provided details about the callable, parameter, and optional causes.
 *
 * This function is intended to be used for assertions or validation and leverages Kotlin contracts
 * to imply the Boolean value is true in its scope, allowing for smarter compiler optimizations.
 *
 * @param callable the callable function where this expectation is being checked; can be null
 * @param parameter the parameter or property being asserted; can be null
 * @param causeOf an optional throwable that is the direct cause of this exception, or null
 * @param cause an optional throwable that provides additional context for the exception, or null
 * @return true if the Boolean value is true; otherwise, the method throws an exception and does not return
 * @since 1.0.0
 */
fun Boolean.expectTrue(callable: KFunction<*>?, parameter: KParameter?, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@expectTrue
    }
    if (!this) throw if (causeOf.isNull()) ExpectationMismatchException(callable, parameter,
        expectation = true,
        value = false,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(callable, parameter,
        expectation = true,
        value = false,
        cause = cause
    ))
    return true
}
/**
 * Verifies that the Boolean value is `true`. If the value is `false`, an `ExpectationMismatchException` is thrown
 * with detailed information about the failed expectation.
 *
 * @param callableName the name of the callable in which the expectation check is performed, or `null` if unavailable
 * @param parameterName the name of the parameter involved in the expectation check, or `null` if unavailable
 * @param causeOf an optional `Throwable` to indicate the initial cause of the exception, which will be used as the root cause
 * @param cause the underlying cause for the exception, or `null` if there is no additional cause
 * @return `true` if the Boolean value is `true`
 * @since 1.0.0
 */
fun Boolean.expectTrue(callableName: String?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@expectTrue
    }
    if (!this) throw if (causeOf.isNull()) ExpectationMismatchException(callableName, parameterName,
        expectation = true,
        value = false,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(callableName, parameterName,
        expectation = true,
        value = false,
        cause = cause
    ))
    return true
}
/**
 * Asserts that the Boolean value is `true`. If the value is `false`, an exception is thrown.
 * This method is used to enforce expectations programmatically.
 *
 * @param callableName The name of the callable function or property being evaluated, or null.
 * @param parameter The property whose expected value is being compared, or null.
 * @param causeOf The primary cause of the exception, if available, or null.
 * @param cause An optional secondary cause to attach to the exception, or null.
 * @return The value `true` if the assertion passes.
 * @throws ExpectationMismatchException if the Boolean value is `false`.
 * @since 1.0.0
 */
fun Boolean.expectTrue(callableName: String?, parameter: KParameter?, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies this@expectTrue
    }
    if (!this) throw if (causeOf.isNull()) ExpectationMismatchException(callableName, parameter,
        expectation = true,
        value = false,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(callableName, parameter,
        expectation = true,
        value = false,
        cause = cause
    ))
    return true
}

/**
 * Verifies that the Boolean value is `false`.
 * If the value is `true`, an exception is thrown.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @return Returns `false` if the value is already `false`.
 * @throws ExpectationMismatchException if the Boolean value is `true`.
 * @since 1.0.0
 */
fun Boolean.expectFalse(causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@expectFalse
    }
    if (this) throw if (causeOf.isNull()) ExpectationMismatchException("Value was expected to be false, but was ${true}.", cause) else causeOf.initCause(ExpectationMismatchException("Value was expected to be false, but was ${true}.", cause))
    return false
}
/**
 * Validates that the current Boolean value is `false`. If the value is `true`,
 * an `ExpectationMismatchException` is thrown with a custom message provided
 * by the supplied lazy message.
 *
 * @param causeOf an optional throwable to be used as the main exception, or `null`
 *                to use the standard situation as exception
 * @param cause An optional underlying exception to include in the thrown exception. Defaults to null.
 * @param lazyMessage A `Supplier` that provides a message describing the expectation mismatch,
 * which is invoked only when the exception is thrown.
 * @return Returns `false`, indicating the current value meets the expectation.
 * @since 1.0.0
 */
fun Boolean.expectFalse(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): Boolean {
    contract {
        returns() implies !this@expectFalse
    }
    if (this) throw if (causeOf.isNull()) ExpectationMismatchException(lazyMessage().toString(), cause) else causeOf.initCause(ExpectationMismatchException(lazyMessage().toString(), cause))
    return false
}
/**
 * Validates that the boolean calling this method is `false`. If the value is `true`, an
 * `ExpectationMismatchException` is thrown. This method utilizes Kotlin contracts to specify
 * that if the method returns successfully, the boolean value is guaranteed to be `false`.
 *
 * @param property An optional `KProperty` representing the property being validated,
 * providing additional metadata about the evaluation.
 * @param variableName An optional `String` representing the name of the variable being validated.
 *                     This is useful for detailed error reporting in case of mismatches.
 * @param causeOf An optional `Throwable` which can override or wrap the default exception.
 *                This allows additional context to be attached to the error.
 * @param cause An optional `Throwable` representing the root cause of the error, which is
 *              passed to the `ExpectationMismatchException` for detailed error chains.
 * @return Always returns `false` when the current boolean value is `false`.
 * @throws ExpectationMismatchException If the current boolean value is `true`.
 * @since 1.0.0
 */
fun Boolean.expectFalse(property: KProperty<*>?, variableName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@expectFalse
    }
    if (this) throw if (causeOf.isNull()) ExpectationMismatchException(property, variableName,
        expectation = false,
        value = true,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(property, variableName,
        expectation = false,
        value = true,
        cause = cause
    ))
    return false
}
/**
 * Verifies that the boolean value is `false`. If the value is `true`, an `ExpectationMismatchException`
 * is thrown with optional details about the mismatch.
 *
 * @param property The primary `KProperty` associated with the expectation, or `null` if no property is involved.
 * @param variable An optional secondary `KProperty` associated with the expectation, or `null` if not applicable.
 * @param causeOf An optional `Throwable` representing the root cause of the exception, or `null` if not applicable.
 * @param cause An optional `Throwable` providing additional context for the exception, or `null` if not applicable.
 * @return Always returns `false` if the boolean value is validated successfully.
 * @throws ExpectationMismatchException if the boolean value is `true`. The exception will include details from
 * the provided `property`, `variable`, `expectation`, and `cause` parameters.
 * @since 1.0.0
 */
fun Boolean.expectFalse(property: KProperty<*>?, variable: KProperty<*>?, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@expectFalse
    }
    if (this) throw if (causeOf.isNull()) ExpectationMismatchException(property, variable,
        expectation = false,
        value = true,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(property, variable,
        expectation = false,
        value = true,
        cause = cause
    ))
    return false
}
/**
 * Ensures that the receiver Boolean value is `false`. If it evaluates to `true`,
 * an `ExpectationMismatchException` is thrown with the provided context details.
 *
 * @param callable The Kotlin function which contains the parameter being checked. Can be `null` if not applicable.
 * @param parameterName The name of the parameter being validated. Can be `null` if the name is not available.
 * @param causeOf An optional pre-existing throwable to be used as the root cause, instead of creating a new exception.
 * @param cause The underlying cause of the failure. Can be `null`.
 * @return Always returns `false` if the receiver is `false`.
 * @since 1.0.0
 */
fun Boolean.expectFalse(callable: KFunction<*>?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@expectFalse
    }
    if (this) throw if (causeOf.isNull()) ExpectationMismatchException(callable, parameterName,
        expectation = false,
        value = true,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(callable, parameterName,
        expectation = false,
        value = true,
        cause = cause
    ))
    return false
}
/**
 * Asserts that the receiver Boolean value is `false`. If the value is `true`, it throws an
 * `ExpectationMismatchException` with detailed information about the callable function, its parameter,
 * the expected value, the actual value, and an optional cause.
 *
 * This method is useful for enforcing expectations and signaling mismatches in program logic.
 *
 * @param callable the function in which the expectation mismatch is being checked, or null if not applicable
 * @param parameter the property or parameter related to the expectation, or null if not applicable
 * @param causeOf an optional throwable that caused this mismatch, or null if not applicable
 * @param cause an optional additional throwable cause, or null if not applicable
 * @return always returns `false` if the receiver is `false`
 * @throws ExpectationMismatchException if the receiver Boolean value is `true`
 * @since 1.0.0
 */
fun Boolean.expectFalse(callable: KFunction<*>?, parameter: KParameter?, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@expectFalse
    }
    if (this) throw if (causeOf.isNull()) ExpectationMismatchException(callable, parameter,
        expectation = false,
        value = true,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(callable, parameter,
        expectation = false,
        value = true,
        cause = cause
    ))
    return false
}
/**
 * Verifies that the boolean value is `false`. If the boolean value is `true`,
 * an `ExpectationMismatchException` is thrown with the provided context details.
 *
 * @param callableName the name of the callable where the check is performed, or `null` if unavailable
 * @param parameterName the name of the parameter being checked, or `null` if unavailable
 * @param causeOf a specific cause of the failure, or `null` if no specific cause is provided
 * @param cause an additional throwable cause to detail the failure, or `null` if no such cause exists
 * @return `false` if the boolean value is already false
 * @since 1.0.0
 */
fun Boolean.expectFalse(callableName: String?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@expectFalse
    }
    if (this) throw if (causeOf.isNull()) ExpectationMismatchException(callableName, parameterName,
        expectation = false,
        value = true,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(callableName, parameterName,
        expectation = false,
        value = true,
        cause = cause
    ))
    return false
}
/**
 * Ensures that the boolean value is `false`, throwing an exception if the value is `true`.
 *
 * This function is used to validate that a given boolean condition is `false`. If the condition is not met,
 * an `ExpectationMismatchException` is thrown, providing detailed information about the callable name,
 * relevant parameter, and the mismatch.
 *
 * @param callableName The name of the callable function or property being evaluated, or null if not specified.
 * @param parameter The property whose expected value is being compared, or null if not applicable.
 * @param causeOf An optional throwable that serves as the primary cause or context for the failure, or null.
 * @param cause The underlying cause of the exception, or null if no specific cause is specified.
 * @return Always returns `false` if no exception is thrown.
 * @throws ExpectationMismatchException If the boolean value is `true`.
 * @since 1.0.0
 */
fun Boolean.expectFalse(callableName: String?, parameter: KParameter?, causeOf: Throwable? = null, cause: Throwable? = null): Boolean {
    contract {
        returns() implies !this@expectFalse
    }
    if (this) throw if (causeOf.isNull()) ExpectationMismatchException(callableName, parameter,
        expectation = false,
        value = true,
        cause = cause
    ) else causeOf.initCause(ExpectationMismatchException(callableName, parameter,
        expectation = false,
        value = true,
        cause = cause
    ))
    return false
}