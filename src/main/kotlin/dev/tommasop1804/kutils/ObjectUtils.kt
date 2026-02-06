@file:JvmName("ObjectUtilsKt")
@file:Suppress("unused", "kutils_null_check", "kutils_collection_declaration", "kutils_map_declaration", "deprecation", "kutils_tuple_declaration",
    "UseExpressionBody",
    "UseExpressionBody",
    "kutils_empty_check"
)
@file:Since("1.0.0")
@file:OptIn(ExperimentalExtendedContracts::class, ExperimentalContracts::class)

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.coding.JSON.Companion.MAPPER
import dev.tommasop1804.kutils.classes.constants.TextCase
import dev.tommasop1804.kutils.classes.constants.TextCase.Companion.convertCase
import dev.tommasop1804.kutils.exceptions.*
import org.slf4j.Logger
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.*


/**
 * Invokes the `Optional` instance, returning the contained value if present,
 * or `null` if the instance is empty.
 *
 * This operator provides a shorthand for accessing the wrapped value
 * within an `Optional` without explicitly calling the `orElse` method.
 *
 * @return the value contained within this `Optional` if present, or `null` if the `Optional` is empty.
 * @since 1.0.0
 */
operator fun <T> Optional<T>.invoke(): T? = orElse(null)
/**
 * Provides an operator function for the `Optional` type, allowing an alternative value
 * to be returned if the optional instance is empty.
 *
 * @param other the value to return if the optional instance is empty
 * @return the value contained within the optional if present, or `other` if empty
 * @since 1.0.0
 */
operator fun <T> Optional<T>.invoke(other: T): T = orElse(other)!!
/**
 * Invokes the `Optional` instance, returning the contained value if present,
 * or `null` if the instance is empty.
 *
 * This operator provides a shorthand for accessing the wrapped value
 * within an `Optional` without explicitly calling the `orElse` method.
 *
 * @return the value contained within this `Optional` if present, or `null` if the `Optional` is empty.
 * @since 1.0.0
 */
@JvmName("optionalInvokeThrowableSupplier")
operator fun <T> Optional<T>.invoke(lazyException: ThrowableSupplier): T = orElseThrow(lazyException)!!

/**
 * Checks if the current object is not null.
 *
 * This function serves as a utility to ensure that the calling object is not null, providing
 * an expressive way to handle nullability checks.
 *
 * @return `true` if the object is not null, `false` otherwise.
 * @since 1.0.0
 */
fun Any?.isNotNull(): Boolean {
    contract {
        returns(true) implies (this@isNotNull != null)
        returns(false) implies (this@isNotNull == null)
    }
    return this != null
}

/**
 * Checks if the given object is `null`.
 *
 * @return `true` if the object is `null`, `false` otherwise.
 * @since 1.0.0
 */
fun Any?.isNull(): Boolean {
    contract {
        returns(true) implies (this@isNull == null)
        returns(false) implies (this@isNull != null)
    }
    return this == null
}

/**
 * Executes the specified action if the current nullable receiver is not null.
 *
 * @param action The action to perform on the non-null receiver of type [T].
 * @since 1.0.0
 */
infix fun <T> T?.ifNotNull(action: ReceiverConsumer<T>) {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        (this@ifNotNull != null) holdsIn action
    }
    if (this != null) this.action()
}

/**
 * Checks if the object instance is castable to the specified class type.
 *
 * @param T the class type to check the instance against.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any?.isCastableTo() = runCatching { this as T }.isSuccess

/**
 * Attempts to cast the current instance to the specified type [T].
 * Returns the result as a [Result], wrapping the casted value if successful
 * or an exception if the cast failed.
 *
 * This method is useful in scenarios where type safety is uncertain,
 * as it avoids runtime exceptions by capturing errors in the [Result].
 *
 * @receiver The instance to be cast.
 * @return A [Result] containing the casted value if successful, or an exception if the cast fails.
 * @since 1.0.0
 */
inline fun <reified T> Any?.safeCast() = runCatching { this as T }
/**
 * Safely casts the current object to the specified type [T]. If the cast fails,
 * returns the provided default value.
 *
 * @param default A value of type [T] that will be returned if the cast operation fails.
 * @return The object cast to type [T], or the given default value in case of a failure.
 * @since 1.0.0
 */
inline fun <reified T> Any?.safeCast(default: T) = runCatching { this as T }.getOrDefault(default)
/**
 * Attempts to safely cast the caller object to the specified type [T].
 * If the cast is successful, the method returns the object as [T],
 * otherwise it returns `null`.
 *
 * The cast operation is performed within a `runCatching` block, which
 * ensures that the method handles any potential exceptions gracefully.
 *
 * @return The object cast to [T] if successful, or `null` if the cast fails.
 * @throws ClassCastException if the type cast fails, but in this implementation,
 *         it is caught and handled by returning `null`.
 *
 * @since 1.0.0
 */
inline fun <reified T> Any?.safeCastOrNull(): T? = runCatching { this as T }.getOrNull()
/**
 * Safely attempts to cast the receiver of type [T1] to the target type [T2].
 * If the cast fails, applies the given transformation to provide a fallback value.
 *
 * @param transform A transformation function that will be applied if the cast fails.
 * @since 1.0.0
 */
inline fun <T1, reified T2> T1?.safeCastOr(transform: ReceiverTransformer<T1?, T2>) = runCatching { this as T2 }.getOrNull() ?: this.transform()
/**
 * Attempts to cast the calling object to the specified type [T]. If the cast fails, the provided exception
 * supplied by [lazyException] is thrown.
 *
 * This method uses `runCatching` to perform the cast operation and leverages the provided exception supplier
 * to handle failures, ensuring a safe and customizable casting mechanism.
 *
 * @param T The type to which the object is being cast.
 * @param lazyException A supplier function that generates the exception to be thrown if the cast operation fails.
 * Should return an instance of [Throwable].
 * @throws Throwable The exception provided by [lazyException] is thrown if the cast operation fails.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> Any?.safeCastOrThrow(lazyException: ThrowableSupplier) = runCatching { this as T }.getOrThrow(lazyException)

/**
 * Returns the receiver object if the specified condition is true, otherwise returns null.
 *
 * @param condition The condition to evaluate. If true, the receiver object is returned; if false, null is returned.
 * @since 1.0.0
 */
infix fun <T> Supplier<T>.whenTrue(condition: Boolean) = if (condition) this() else null
/**
 * Provides a conditional extension function that returns the receiver object only
 * if it satisfies a given predicate. Otherwise, it returns null.
 *
 * WARNING: Invoking this method may cause an exception if the throw is determined by the condition.
 *
 * @param predicate A lambda function representing the condition to be evaluated for the receiver object.
 * @since 1.0.0
 */
@JvmName("whenTrueGeneric")
@ConditionNotPreventingExceptions
inline infix fun <T> T.whenTrue(predicate: ReceiverPredicate<T>) = if (this.predicate()) this else null
/**
 * Returns the receiver object if the specified condition is true, otherwise returns null.
 *
 * WARNING: Invoking this method may cause an exception if the throw is determined by the condition.
 *
 * @param condition The condition to evaluate. If true, the receiver object is returned; if false, null is returned.
 * @since 1.0.0
 */
@JvmName("whenTrueGeneric")
@ConditionNotPreventingExceptions
infix fun <T> T.whenTrue(condition: Boolean) = if (condition) this else null

/**
 * Returns the receiver object if the given condition is false; otherwise, returns null.
 *
 * @param condition A Boolean value that determines whether the receiver object is returned.
 * If the condition evaluates to false, the receiver object is returned; otherwise, null is returned.
 * @since 1.0.0
 */
infix fun <T> Supplier<T>.whenFalse(condition: Boolean) = if (!condition) this() else null
/**
 * Returns the current instance if the given predicate evaluates to false. If the predicate evaluates to true, null is returned.
 *
 * WARNING: Invoking this method may cause an exception if the throw is determined by the condition.
 *
 * @param predicate A lambda expression or function reference that takes the current instance as a receiver and returns a boolean value.
 * @since 1.0.0
 */
@JvmName("whenFalseGeneric")
@ConditionNotPreventingExceptions
inline infix fun <T> T.whenFalse(predicate: ReceiverPredicate<T>) = if (!this.predicate()) this else null
/**
 * Returns the receiver object if the given condition is false; otherwise, returns null.
 *
 * WARNING: Invoking this method may cause an exception if the throw is determined by the condition.
 *
 * @param condition A Boolean value that determines whether the receiver object is returned.
 * If the condition evaluates to false, the receiver object is returned; otherwise, null is returned.
 * @since 1.0.0
 */
@JvmName("whenFalseGeneric")
@ConditionNotPreventingExceptions
infix fun <T> T.whenFalse(condition: Boolean) = if (!condition) this else null

/**
 * Applies the provided block of code to the receiver object and returns the receiver object.
 *
 * Allows scoped execution of the block where the receiver object is accessible as `this`.
 *
 * @param T the type of the receiver object.
 * @param block the block of code to be executed with the receiver object as its context.
 * @return the receiver object after applying the block.
 * @since 1.0.0
 */

infix fun <T> T.apply(block: ReceiverConsumer<T>): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
    return this
}

/**
 * Runs the given block of code on the receiver object and returns its result.
 *
 * @param block A function that takes the receiver as a parameter and returns a result of type R.
 * @return The result of executing the block on the receiver.
 * @since 1.0.0
 */
infix fun <T, R> T.then(block: ReceiverTransformer<T, R>): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return this.block()
}
/**
 * Conditionally applies a transformation to this value based on a predicate.
 *
 * If the predicate evaluates to true when applied to this value, the transformation block is executed
 * with this value as the receiver and its result is returned. Otherwise, this value is returned unchanged.
 *
 * @param T the type of the receiver value
 * @param predicate a function that takes this value as receiver and returns a Boolean indicating whether to apply the transformation
 * @param block a transformation function that takes this value as receiver and returns a transformed value of the same type
 * @return the transformed value if the predicate is true, otherwise this value unchanged
 * @since 1.0.0
 */
fun <T> T.thenWhen(predicate: ReceiverPredicate<T>, block: ReceiverTransformer<T, T>): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (predicate(this)) {
        return block(this)
    }
    return this
}
/**
 * Executes the given block transformation on the receiver if it satisfies the specified predicate.
 *
 * @param predicate A predicate function to test the receiver.
 * @param block A transformation function applied to the receiver when the predicate evaluates to true.
 * @return The transformed value of type [R] if the predicate returns true, otherwise null.
 * @since 1.0.0
 */
fun <T, R> T.thenWhenOrNull(predicate: ReceiverPredicate<T>, block: ReceiverTransformer<T, R>): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (this.predicate()) this.block() else null
}
/**
 * Evaluates a predicate on the receiver object and, if it returns true, applies the transformation block with 
 * the receiver object. Otherwise, returns the result of the provided default supplier.
 *
 * @param T the type of the receiver object
 * @param R the type of the result
 * @param predicate a function that takes the receiver object and returns a boolean indicating whether the condition is met
 * @param default a supplier function that provides the default result to be returned if the predicate evaluates to false
 * @param block a transformation function that takes the receiver object and returns the result of type R when the predicate evaluates to true
 * @return the result of applying the transformation block when the predicate evaluates to true, or the result of the default supplier when the predicate evaluates to false
 * @since 1.0.0
 */
fun <T, R> T.thenWhenOr(predicate: ReceiverPredicate<T>, default: Supplier<R>, block: ReceiverTransformer<T, R>): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (this.predicate()) this.block() else default()
}
/**
 * Conditionally applies a transformation to the receiver if the specified condition is true.
 *
 * This function evaluates the condition and, if true, applies the transformation block to the receiver.
 * If the condition is false, the original receiver is returned unchanged. The transformation block
 * is guaranteed to be called at most once and only when the condition is true.
 *
 * @param T the type of the receiver and return value
 * @param condition the boolean condition that determines whether the transformation should be applied
 * @param block the transformation function to apply to the receiver when the condition is true
 * @return the transformed receiver if condition is true, otherwise the original receiver unchanged
 * @since 1.0.0
 */
inline fun <T> T.thenWhen(condition: Boolean, block: ReceiverTransformer<T, T>): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        condition holdsIn block
    }
    return if (condition) this.block() else this
}
/**
 * Executes the given block of code with the receiver if the specified condition is true.
 * The method evaluates the condition and only invokes the block if the condition holds true.
 *
 * @param condition A Boolean value determining whether the block should be invoked.
 * @param block A transformation block that is executed with the receiver as its input.
 * @return The result of the block if the condition is true, otherwise null.
 * @since 1.0.0
 */
inline fun <T, R> T.thenWhenOrNull(condition: Boolean, block: ReceiverTransformer<T, R>): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        condition holdsIn block
        returnsNotNull() implies (condition)
    }
    return if (condition) this.block() else null
}
/**
 * Executes a transformation block on the receiver when the provided condition is true, otherwise returns 
 * the result from the default supplier. Ensures the transformation block or the default supplier is invoked at most once 
 * with the contracts provided.
 *
 * @param condition A boolean determining which operation to execute. If true, the block is invoked.
 * @param default A supplier function to provide the result when the condition is false.
 * @param block A transformation function applied to the receiver when the condition is true.
 * @return The result of the transformation block if the condition is true, the result of the default supplier if the condition is false, or null in certain cases as per the contracts
 * .
 * @since 1.0.0
 */

inline fun <T, R> T.thenWhenOr(condition: Boolean, default: Supplier<R>, block: ReceiverTransformer<T, R>): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        condition holdsIn block
        !condition holdsIn default
    }
    return if (condition) this.block() else default()
}
/**
 * Conditionally transforms this value using the provided block if the predicate returns false.
 *
 * If the predicate evaluates to false, applies the transformation block to this value and returns the result.
 * Otherwise, returns this value unchanged.
 *
 * This is the inverse of a conditional "then" operation - the transformation is applied unless the condition is met.
 *
 * @param T the type of the receiver value
 * @param predicate a function that takes this value as a receiver and returns a boolean condition
 * @param block a transformation function that takes this value as a receiver and returns a transformed value, invoked only when the predicate returns false
 * @return the transformed value if predicate returns false, or this value unchanged if predicate returns true
 * @since 1.0.0
 */
fun <T> T.thenUnless(predicate: ReceiverPredicate<T>, block: ReceiverTransformer<T, T>): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (!this.predicate()) this.block() else this
}
/**
 * Executes the given [block] if the result of the [predicate] is false. Returns the result of the [block]
 * if the predicate evaluates to false, or null otherwise.
 *
 * @param predicate A lambda function that takes the receiver object [T] and returns a Boolean value.
 *                  If this evaluates to false, the [block] is executed.
 * @param block A lambda function that takes the receiver object [T] and returns a value of type [R],
 *              executed only if the predicate evaluates to false.
 * @return The result of the [block] if the [predicate] evaluates to false, or null otherwise.
 * @since 1.0.0
 */
fun <T, R> T.thenUnlessOrNull(predicate: ReceiverPredicate<T>, block: ReceiverTransformer<T, R>): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (!this.predicate()) this.block() else null
}
/**
 * Executes the provided [block] if the given [predicate] evaluates to false for the receiver object.
 * Otherwise, it returns the result of the [default] supplier.
 *
 * @param T the type of the receiver object.
 * @param R the type of the result produced by the [block] or [default].
 * @param predicate a function that takes the receiver object as input and returns a Boolean value.
 *                  If the predicate evaluates to true, the [default] is executed; otherwise, the [block] is executed.
 * @param default a supplier function that produces a fallback result when the [predicate] evaluates to true.
 * @param block a transformation function applied to the receiver object when the [predicate] evaluates to false.
 * @return the result of applying the [block] to the receiver object if the [predicate] evaluates to false, 
 *         or the result of the [default] supplier if the [predicate] evaluates to true. Returns `null` if neither function is executed.
 * @since 1.0.0
 */
fun <T, R> T.thenUnlessOr(predicate: ReceiverPredicate<T>, default: Supplier<R>, block: ReceiverTransformer<T, R>): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (!this.predicate()) this.block() else default()
}
/**
 * Conditionally applies a transformation to the receiver unless the specified condition is true.
 *
 * This function applies the given transformation block to the receiver object when the condition
 * evaluates to false. If the condition is true, the receiver is returned unchanged.
 *
 * @param T the type of the receiver object
 * @param R the return type (not used in this function's signature but required for inline compatibility)
 * @param condition the boolean condition to evaluate; when false, the block is executed
 * @param block the transformation function to apply to the receiver when the condition is false
 * @return the transformed receiver if the condition is false, otherwise the original receiver unchanged
 * @since 1.0.0
 */
inline fun <T, R> T.thenUnless(condition: Boolean, block: ReceiverTransformer<T, T>): T {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        !condition holdsIn block
    }
    return if (!condition) this.block() else this
}
/**
 * Executes the given [block] on the receiver if the [condition] is `false`. 
 * If the [condition] is `true`, the method returns `null`.
 *
 * @param T The type of the receiver on which the block is executed.
 * @param R The type of the value returned by the block.
 * @param condition A boolean condition that determines whether the block will be executed.
 *                    If `true`, the block is not executed, and `null` is returned.
 *                    If `false`, the block is executed, and its result is returned.
 * @param block A [ReceiverTransformer] lambda that operates on the receiver and produces a result of type [R].
 * @return Returns the result of the [block] if [condition] is `false`. Returns `null` otherwise.
 * @since 1.0.0
 */
inline fun <T, R> T.thenUnlessOrNull(condition: Boolean, block: ReceiverTransformer<T, R>): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        !condition holdsIn block
        returnsNotNull() implies (!condition)
    }
    return if (!condition) this.block() else null
}
/**
 * Executes the given [block] if the [condition] is false. If the [condition] is true, it executes the [default] 
 * supplier instead. This function ensures that the [block] or [default] is invoked based on the specified condition. 
 * Contracts are defined to ensure proper control flow and invocation constraints.
 *
 * @param T the type of the receiver parameter.
 * @param R the type of the resulting value.
 * @param condition a Boolean value that determines whether the [block] or [default] is executed.
 * @param default a supplier function providing the default value when the [condition] is true.
 * @param block a transformer that is applied to the receiver to calculate a result when the [condition] is false.
 * @return the result of the [block] when the [condition] is false or the result of [default] when the [condition] is true.
 * @since 1.0.0
 */
inline fun <T, R> T.thenUnlessOr(condition: Boolean, default: Supplier<R>, block: ReceiverTransformer<T, R>): R {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        !condition holdsIn block
        condition holdsIn default
    }
    return if (!condition) this.block() else default()
}

/**
 * Executes the provided block and returns its result, or null if an exception is thrown during execution.
 *
 * @param T the type of the result returned by the block
 * @param block the lambda function to be executed
 * @return the result of the block if successful, or null if an exception occurs
 * @since 1.0.0
 */
fun <T> tryOrNull(
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
): T? {
    return try {
        block()
    } catch (e: Throwable) {
        if (overwriteOnly.isEmpty() && notOverwrite.isEmpty())
            return null
        else if (e::class !in overwriteOnly || e::class in notOverwrite) throw e
        null
    }
}
/**
 * Executes the provided block and returns its result, or null if an exception is thrown during execution.
 *
 * @param T the type of the result returned by the block
 * @param overwriteOnly a specific type of Throwable to overwrite. If null, no specific overwrite is applied.
 * @param notOverwrite a set of Throwable types that should not be overwritten, even if overwriteOnly is specified.
 * @param block the lambda function to be executed
 * @return the result of the block if successful, or null if an exception occurs
 * @since 1.0.0
 */
fun <T> tryOrNull(
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
) = tryOrNull(overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite, block)
/**
 * Executes the provided block and returns its result, or null if an exception is thrown during execution.
 *
 * This method allows optional filtering of exceptions to handle or ignore based on the provided parameters.
 *
 * @param T the type of the result returned by the block
 * @param overwriteOnly specifies a class of exceptions that should exclusively be handled as null (if provided).
 *        If null, no specific filtering is applied at this level.
 * @param notOverwrite specifies a class of exceptions that should not be handled as null (if provided).
 *        If null, no specific filtering is applied at this level.
 * @param block the lambda function to be executed
 * @return the result of the block if successful, or null if an exception occurs, considering the filter criteria
 * @since 1.0.0
 */
fun <T> tryOrNull(
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
) = tryOrNull(overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite?.let { setOf(it) } ?: emptySet(), block)
/**
 * Executes the given block and returns its result, or null if an exception is thrown during execution.
 * Allows customization of exception handling based on the provided sets of exception classes.
 *
 * @param T the type of the result returned by the block
 * @param overwriteOnly a set of exception classes which can be overwritten to return null, unless present in notOverwrite
 * @param notOverwrite an optional exception class that overrides the overwriteOnly rule, causing the exception to be rethrown
 * @param block the lambda function to be executed
 * @since 1.0.0
 */
fun <T> tryOrNull(
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
) = tryOrNull(overwriteOnly, notOverwrite?.let { setOf(it) } ?: emptySet(), block)

/**
 * Executes the provided block of code within a try-catch block and handles exceptions by logging them
 * according to the specified configuration. This method allows flexible control over logging behavior
 * for general messages, specific exception cases, and whether to rethrow exceptions or not.
 *
 * @param T The return type of the block of code to execute.
 * @param logger The logger instance used to log messages and exceptions.
 * @param message A transformer that return a pair consisting of a log message (nullable) and a default log level to use if no specific
 * exception handling rules match.
 * @param specificCases A map defining custom logging rules for specific exception types. Each entry maps
 * a class of exception (`KClass<out Throwable>`) to a pair of a custom log message and the log level to use.
 * Specific cases take precedence over the general logging behavior.
 * @param includeException A flag indicating whether the caught exception should be included in the log output.
 * Defaults to true.
 * @param overwriteOnly A set of exception types for which the general logging behavior defined by `message`
 * should be replaced with custom handling. If empty, no overwriting is applied.
 * @param notOverwrite A set of exception types for which the custom handling defined in `specificCases`
 * should not apply. Exceptions in this set will always be treated with the default logging rule.
 * @param block The block of code to execute.
 * @return The result of the block of code, or `null` if an exception is handled and not rethrown.
 * @throws ParametersInConflictException If there are conflicting configurations between `overwriteOnly`,
 * `notOverwrite`, or `specificCases`.
 * @since 1.0.0
 */
fun <T> tryOrLog(
    logger: Logger,
    message: Transformer<Throwable, Pair<String?, LogLevel?>>,
    specificCases: Map<KClass<out Throwable>, Pair<String?, LogLevel?>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeException: Boolean = true,
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
): T? {
    if (overwriteOnly isIntersecting notOverwrite) throw ParametersInConflictException(
        callableName = "tryOrLog",
        parametersName = listOf("overwriteOnly", "notOverwrite"),
        valuesInConflict = overwriteOnly intersect notOverwrite
    )
    if (specificCases.keys isIntersecting notOverwrite) throw ParametersInConflictException(
        callableName = "tryOrLog",
        parametersName = listOf("specificCases", "notOverwrite"),
        valuesInConflict = specificCases.keys intersect notOverwrite
    )
    return try {
        block()
    } catch (e: Throwable) {
        val message = message(e)
        if (e::class in specificCases)
            logWithOrWithoutException(logger, specificCases[e::class]!!.second ?: message.second ?: LogLevel.ERROR, specificCases[e::class]!!.first ?: e.message, includeException, e)
        else if (overwriteOnly.isEmpty() && notOverwrite.isEmpty())
            logWithOrWithoutException(logger, message.second ?: LogLevel.ERROR, message.first ?: e.message, includeException, e)
        else {
            if (e::class !in overwriteOnly || e::class in notOverwrite) throw e
            logWithOrWithoutException(logger, message.second ?: LogLevel.ERROR, message.first ?: e.message, includeException, e)
        }
        null
    }
}
/**
 * Executes the provided block of code within a try-catch block and handles exceptions by logging them
 * according to the specified configuration. This method provides flexibility in specifying default logging behavior,
 * custom handling for specific exceptions, and conditions for overwriting or excluding logging rules.
 *
 * @param T The return type of the block of code to execute.
 * @param logger The logger instance used to log messages and exceptions.
 * @param message A transformer that return a pair consisting of a log message (nullable) and a default log level to use if no specific
 * exception handling rules match.
 * @param specificCases A map defining custom logging rules for specific exception types. Each entry maps
 * a class of exception (`KClass<out Throwable>`) to a pair of a custom log message and the log level to use.
 * Specific cases take precedence over the general logging behavior.
 * @param includeException A flag indicating whether the caught exception should be included in the log output.
 * Defaults to true.
 * @param overwriteOnly A single exception type for which the general logging behavior defined by `message`
 * should be replaced with custom handling. If null, no overwriting is applied.
 * @param notOverwrite A set of exception types for which the custom handling defined in `specificCases`
 * should not apply. Exceptions in this set will always be treated with the default logging rule.
 * @param block The block of code to execute.
 * @return The result of the block of code, or `null` if an exception is handled and not rethrown.
 * @since 1.0.0
 */
fun <T> tryOrLog(
    logger: Logger,
    message: Transformer<Throwable, Pair<String?, LogLevel?>>,
    specificCases: Map<KClass<out Throwable>, Pair<String?, LogLevel?>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeException: Boolean = true,
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
): T? = tryOrLog(logger, message, specificCases, includeException, overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite, block)
/**
 * Executes the provided block of code within a try-catch block and handles exceptions by logging them
 * based on the specified configuration. This method provides flexible control over logging behavior for
 * general cases, specific exceptions, and conditions for overwriting default behavior.
 *
 * @param T The return type of the block to execute.
 * @param logger The logger instance used to log messages and exceptions.
 * @param message A transformer that return a pair representing the default log message (nullable) and the default log level to use
 * if no specific exception handling rules apply.
 * @param specificCases A map specifying custom log messages and log levels for particular exception types.
 * Takes precedence over general logging behavior.
 * @param includeException Indicates whether the caught exception should be included in the log output.
 * Defaults to true.
 * @param overwriteOnly A specific exception type that, if encountered, will overwrite the general logging
 * rule with the default behavior provided by the `message` parameter.
 * @param notOverwrite A specific exception type that will not be overwritten and will strictly adhere to
 * custom handling rules from `specificCases`.
 * @param block A block of code to execute.
 * @return The result of the execution block or `null` if an exception is caught and not rethrown.
 * @since 1.0.0
 */
fun <T> tryOrLog(
    logger: Logger,
    message: Transformer<Throwable, Pair<String?, LogLevel?>>,
    specificCases: Map<KClass<out Throwable>, Pair<String?, LogLevel?>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeException: Boolean = true,
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
): T? = tryOrLog(logger, message, specificCases, includeException, overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite?.let { setOf(it) } ?: emptySet(), block)
/**
 * Executes the provided block of code within a try-catch block and handles exceptions by logging them
 * according to the specified configuration. This method allows flexible control over logging behavior
 * for general messages, specific exception cases, and whether to rethrow exceptions or not.
 *
 * @param T The return type of the block of code to execute.
 * @param logger The logger instance used to log messages and exceptions.
 * @param message A transformer that return a pair consisting of a log message (nullable) and a default log level to use if no specific
 * exception handling rules match.
 * @param specificCases A map defining custom logging rules for specific exception types. Each entry maps
 * a class of exception (`KClass<out Throwable>`) to a pair of a custom log message and the log level to use.
 * Specific cases take precedence over the general logging behavior.
 * @param includeException A flag indicating whether the caught exception should be included in the log output.
 * Defaults to true.
 * @param overwriteOnly A set of exception types for which the general logging behavior defined by `message`
 * should be replaced with custom handling. If empty, no overwriting is applied.
 * @param notOverwrite An exception type for which the custom handling defined in `specificCases`
 * should not apply. Exceptions of this type will always be treated with the default logging rule.
 * @param block The block of code to execute.
 * @return The result of the block of code, or `null` if an exception is handled and not rethrown.
 * @throws ParametersInConflictException If there are conflicting configurations between `overwriteOnly`,
 * `notOverwrite`, or `specificCases`.
 * @since 1.0.0
 */
fun <T> tryOrLog(
    logger: Logger,
    message: Transformer<Throwable, Pair<String?, LogLevel?>>,
    specificCases: Map<KClass<out Throwable>, Pair<String?, LogLevel?>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeException: Boolean = true,
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
): T? = tryOrLog(logger, message, specificCases, includeException, overwriteOnly, notOverwrite?.let { setOf(it) } ?: emptySet(), block)


/**
 * Executes the given block of code, returning the result or applying fallback strategies in case of exceptions.
 * Allows customization of specific exception handling behavior and optional constraints on default handling logic.
 *
 * @param T The type of the result returned by the block or the fallback functions.
 * @param default A fallback function to be invoked when no specific case or overwrite rule applies.
 * @param specificCases A map associating exception types with specific fallback functions for handling them.
 *                      These functions have the highest priority and take precedence over `overwriteOnly` and `notOverwrite`.
 * @param overwriteOnly A set of exception types for which the default fallback function should be applied, ignoring others.
 *                      Exceptions outside this set will be rethrown unless they have an entry in `specificCases`.
 * @param notOverwrite A set of exception types for which the default fallback function should not be applied, causing these exceptions to be rethrown unless they have an entry in
 *  `specificCases`.
 * @param block A supplier function representing the primary operation to execute.
 * @return Either the result of the executed block, or the result of the applied fallback strategy in case of an exception.
 *         If rethrow conditions are met and no fallback strategy applies, an uncaught exception is propagated.
 * @throws ParametersInConflictException If the `overwriteOnly` and `notOverwrite` sets have intersection,
 *                                       or if `specificCases` keys intersect with `notOverwrite`.
 * @since 1.0.0
 */
fun <T> tryOr(
    default: Transformer<Throwable, T>,
    specificCases: Map<KClass<out Throwable>, Transformer<Throwable, T>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
): T {
    if (overwriteOnly isIntersecting notOverwrite) throw ParametersInConflictException(
        callableName = "tryOrThrow",
        parametersName = listOf("overwriteOnly", "notOverwrite"),
        valuesInConflict = overwriteOnly intersect notOverwrite
    )
    if (specificCases.keys isIntersecting notOverwrite) throw ParametersInConflictException(
        callableName = "tryOrThrow",
        parametersName = listOf("specificCases", "notOverwrite"),
        valuesInConflict = specificCases.keys intersect notOverwrite
    )
    return try {
        block()
    } catch (e: Throwable) {
        return if (e::class in specificCases) specificCases[e::class]!!(e)
        else if (overwriteOnly.isEmpty() && notOverwrite.isEmpty()) default(e)
        else {
            if (e::class !in overwriteOnly || e::class in notOverwrite) throw e
            else default(e)
        }
    }
}
/**
 * Executes a given block of code and provides specialized handling for potential exceptions.
 * The method attempts to execute the provided `block`, and if an exception is thrown, it determines
 * how to handle the exception based on the provided parameters. Specific exception handling takes precedence
 * over general controls such as `overwriteOnly` and `notOverwrite`.
 *
 * @param T The type of the result produced by the block or exception handlers.
 * @param default The default exception handler to be used if no more specific handler matches.
 * @param specificCases A map of specific exception types to their corresponding handlers.
 *                      These handlers take precedence over default handling and the `overwriteOnly` parameter.
 * @param overwriteOnly A single exception type which will be handled by the `default` handler even
 *                      if it exists in `notOverwrite`. Null will default to no `overwriteOnly` handling.
 * @param notOverwrite A set of exception types which should never be handled by the `default` handler.
 *                     These will always be rethrown unless matched by `specificCases`.
 * @param block The block of code to execute, which may throw exceptions.
 * @return The result produced by the block or the result from the corresponding exception handler.
 * @throws ParametersInConflictException If there are conflicts between the `overwriteOnly` and `notOverwrite`
 *                                        lists or between `specificCases` and `notOverwrite`.
 * @since 1.0.0
 */
fun <T> tryOr(
    default: Transformer<Throwable, T>,
    specificCases: Map<KClass<out Throwable>, Transformer<Throwable, T>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
) = tryOr(default, specificCases, overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite, block)
/**
 * Executes the given block and handles exceptions using the specified handling strategy.
 * Defines a hierarchy of exception handling based on specific cases, overwrite rules, and a default fallback.
 *
 * @param T The return type of the block and the handling functions.
 * @param default A function to handle exceptions not covered by any specific cases or the overwrite rules.
 * @param specificCases A map where keys are specific exception classes and values are functions to handle those exceptions.
 *                       Exceptions in this map are handled with the highest priority.
 * @param overwriteOnly A set of exception classes that should only be handled by the default function, unless matched in specific cases.
 * @param notOverwrite A set of exception classes that should never be handled even by overwriteOnly or specificCases, and should propagate.
 * @param block The block of code to execute, which may throw exceptions.
 * @return The result of the block execution, or the fallback result from the provided handling strategy if an exception is encountered.
 * @throws ParametersInConflictException If `overwriteOnly` and `notOverwrite` contain intersecting exception classes, or if `specificCases` and `notOverwrite` have conflicts.
 * @since 1.0.0
 */
fun <T> tryOr(
    default: Transformer<Throwable, T>,
    specificCases: Map<KClass<out Throwable>, Transformer<Throwable, T>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
) = tryOr(default, specificCases, overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite?.let { setOf(it) } ?: emptySet(), block)
/**
 * Executes a block of code and handles exceptions using specified handlers.
 * Provides a default handler and optional specific exception handling logic.
 *
 * @param T The return type of the code block and handlers.
 * @param default The default handler that processes exceptions into a value of type [T].
 * @param specificCases A map of specific exception types to their corresponding handlers, giving priority
 * over the default handler. Handled exceptions matching these keys will use the corresponding function.
 * @param overwriteOnly A set of exception types where the default handler should always be applied unless
 * the exception is explicitly excluded in [notOverwrite].
 * @param notOverwrite A single exception type that should bypass the default handler, even if it is included
 * in [overwriteOnly]. If null, no exceptions are excluded in this way.
 * @param block The block of code to be executed that might throw exceptions.
 * @since 1.0.0
 */
fun <T> tryOr(
    default: Transformer<Throwable, T>,
    specificCases: Map<KClass<out Throwable>, Transformer<Throwable, T>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
) = tryOr(default, specificCases, overwriteOnly, notOverwrite?.let { setOf(it) } ?: emptySet(), block)

/**
 * Attempts to execute a given block of code and determines the resulting boolean outcome
 * based on the provided specific exception handling rules.
 * 
 * This method evaluates a block of code and handles exceptions with customizable behavior.
 * It provides mechanisms for defining specific cases, overwriting behavior, or excluding
 * specific exceptions from being handled.
 * 
 * @param T The return type of the block to be executed.
 * @param specificCases A map defining specific exceptions and their associated transformations 
 * to return boolean values. If an exception type from this map is encountered, the corresponding 
 * transformer is applied. Default is an empty map.
 * @param overwriteOnly A set of exception types for which the handling behavior is restricted to overwriting. 
 * Any exceptions not in this set will not be caught. Default is an empty set.
 * @param notOverwrite A set of exception types that are explicitly excluded from being handled.
 * If an exception in this set is encountered, it will be thrown. Default is an empty set.
 * @param block A supplier block of code to execute which potentially throws an exception.
 * @return True if the block executes successfully without exceptions or if an exception is handled 
 * as true based on the provided rules. False if an exception occurs and is handled as such. 
 * Throws the exception if it does not meet any handling conditions.
 * @since 1.0.0
 */
fun <T> tryTrueOrFalse(
    specificCases: Map<KClass<out Throwable>, Transformer<Throwable, Boolean>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
): Boolean {
    if (overwriteOnly isIntersecting notOverwrite) throw ParametersInConflictException(
        callableName = "tryTrueOrFalse",
        parametersName = listOf("overwriteOnly", "notOverwrite"),
        valuesInConflict = overwriteOnly intersect notOverwrite
    )
    if (specificCases.keys isIntersecting notOverwrite) throw ParametersInConflictException(
        callableName = "tryTrueOrFalse",
        parametersName = listOf("specificCases", "notOverwrite"),
        valuesInConflict = specificCases.keys intersect notOverwrite
    )
    return try {
        block()
        true
    } catch (e: Throwable) {
        return if (e::class in specificCases) specificCases[e::class]!!(e)
        else if (overwriteOnly.isEmpty() && notOverwrite.isEmpty()) false
        else {
            if (e::class !in overwriteOnly || e::class in notOverwrite) throw e
            else false
        }
    }
}
/**
 * Executes a provided block of code and returns a Boolean value based on the success or failure of the execution.
 * The behavior on exceptions can be customized through parameters such as specific exception handling, 
 * overwriting, and exclusion rules.
 *
 * @param T The type of the result produced by the provided block of code.
 * @param specificCases A map defining specific exception classes and their corresponding transformer functions 
 * converting the exception to a Boolean value. These have the highest priority over other parameters.
 * @param overwriteOnly A specific exception class that, if thrown during execution of the block, indicates 
 * whether the exception should be consumed or propagate.
 * @param notOverwrite A set of exception classes that should not be overwritten. Exceptions from this set are 
 * rethrown even if other parameters are specified.
 * @param block A supplier representing the block of code to execute.
 * @return Boolean True if the block executes successfully or matches the rules to return true, false otherwise.
 * @throws ParametersInConflictException If `overwriteOnly` and `notOverwrite` share conflicting exception 
 * classes, or if `specificCases` and `notOverwrite` overlap.
 * @since 1.0.0
 */
fun <T> tryTrueOrFalse(
    specificCases: Map<KClass<out Throwable>, Transformer<Throwable, Boolean>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
) = tryTrueOrFalse(specificCases, overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite, block)
/**
 * Attempts to execute a given block of code and returns true if it succeeds, or handles exceptions
 * based on the provided rules and returns a boolean indicating the result.
 *
 * @param T The type of the result produced by the block.
 * @param specificCases A map specifying custom exception handling rules. It maps throwable classes
 * to transformers that determine how to handle a specific exception. Has the highest priority over
 * `overwriteOnly` and `notOverwrite`.
 * @param overwriteOnly A specific set of throwable classes where exceptions should always be caught
 * and handled as returning `false`, unless they clash with specific cases or other conditions.
 * @param notOverwrite A specific set of throwable classes where exceptions should not be caught, and
 * instead rethrown. If an exception class exists in both `overwriteOnly` and `notOverwrite`, it will
 * cause a conflict.
 * @param block The block of code to execute that produces the result of type `T`.
 * @return A boolean indicating whether the block succeeded (returns `true`) or was handled according
 * to the given exception handling logic (returns `false`) without rethrowing.
 * @throws ParametersInConflictException If there are conflicting parameters between `overwriteOnly`,
 * `notOverwrite`, or `specificCases`.
 * @throws Throwable Rethrows any exception not caught or specified by the handling rules.
 * @since 1.0.0
 */
fun <T> tryTrueOrFalse(
    specificCases: Map<KClass<out Throwable>, Transformer<Throwable, Boolean>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
) = tryTrueOrFalse(specificCases, overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite?.let { setOf(it) } ?: emptySet(), block)
/**
 * Executes the provided block and captures any thrown exceptions. Returns a Boolean value based on 
 * specific handling rules configured via the parameters.
 *
 * @param T the return type of the block to be executed.
 * @param specificCases a map associating specific exception types with transformers to handle them. 
 *                      The transformer converts the exception into a Boolean. This map has the highest priority 
 *                      over `overwriteOnly` and `notOverwrite`.
 * @param overwriteOnly a set of exception types that should be explicitly caught and processed 
 *                      as false unless `notOverwrite` indicates otherwise.
 * @param notOverwrite a single exception type that should not be processed, even if it is in 
 *                     `specificCases` or `overwriteOnly`.
 * @param block a supplier representing the code block to be executed, which may throw an exception. 
 *              If no exception occurs, the method returns true.
 * @return true if the block executed successfully, or a Boolean result based on the specific rules 
 *         defined by the parameters if exceptions occur.
 * @throws ParametersInConflictException if there are contradictions in the configuration, such as overlapping 
 *                                       rules between `overwriteOnly`, `notOverwrite`, and `specificCases`.
 * @since 1.0.0
 */
fun <T> tryTrueOrFalse(
    specificCases: Map<KClass<out Throwable>, Transformer<Throwable, Boolean>> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
) = tryTrueOrFalse(specificCases, overwriteOnly, notOverwrite?.let { setOf(it) } ?: emptySet(), block)

/**
 * Executes the provided block of code and handles thrown exceptions based on the specified rules.
 *
 * This function attempts to execute the supplied block. If an exception is thrown, it applies custom handling based on
 * the provided parameters, such as specific cases for exceptions, inclusion of causes, or overwriting rules. If none of
 * the conditions are met, it propagates the exception or throws a new one as defined.
 *
 * @param lazyException A supplier for creating a throwable to be thrown if no specific case or rules apply.
 * @param specificCases A map of specific exception types to their respective throwable suppliers. If a caught exception matches a key
 * in this map, the corresponding supplier is invoked to provide the exception to be thrown.
 * @param includeCause Determines whether the original exception should be set as the cause of the newly thrown exception.
 * Default is true.
 * @param overwriteOnly A set of exception types. If a caught exception’s type is included in this set, and not in the
 * `notOverwrite` set, a new exception from `lazyException` is thrown, optionally including the original exception as its cause.
 * @param notOverwrite A set of exception types that should not be overwritten, even if they are present in the `overwriteOnly` set.
 * @param block The block of code to be executed. If it completes without throwing an exception, its result is returned.
 *
 * @return The result of the executed block if no exception is thrown or if exceptions are handled and not propagated.
 *
 * @throws ParametersInConflictException If there is a conflict between the values of `overwriteOnly` and `notOverwrite`,
 * or between the keys of `specificCases` and the `notOverwrite` set.
 * @throws Throwable If a specific case, overwrite rules, or new exception rules are not applicable,
 * the original or newly created exception is propagated.
 *
 * @since 1.0.0
 */
fun <T> tryOrThrow(
    lazyException: ThrowableSupplier,
    specificCases: Map<KClass<out Throwable>, ThrowableSupplier> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeCause: Boolean = true,
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
): T {
    if (overwriteOnly isIntersecting notOverwrite) throw ParametersInConflictException(
        callableName = "tryOrThrow",
        parametersName = listOf("overwriteOnly", "notOverwrite"),
        valuesInConflict = overwriteOnly intersect notOverwrite
    )
    if (specificCases.keys isIntersecting notOverwrite) throw ParametersInConflictException(
        callableName = "tryOrThrow",
        parametersName = listOf("specificCases", "notOverwrite"),
        valuesInConflict = specificCases.keys intersect notOverwrite
    )
    return try {
        block()
    } catch (e: Throwable) {
        if (e::class in specificCases) throwWithOrWithoutCause(specificCases[e::class]!!, includeCause, e)
        else if (overwriteOnly.isEmpty() && notOverwrite.isEmpty())
            throwWithOrWithoutCause(lazyException, includeCause, e)
        else {
            if (e::class !in overwriteOnly || e::class in notOverwrite) throw e
            else throwWithOrWithoutCause(lazyException, includeCause, e)
        }
    }
}
/**
 * Executes the provided block of code and handles thrown exceptions based on the specified rules.
 *
 * This function executes the given block of code and applies custom exception handling logic. It supports specifying
 * specific cases for exceptions, determining whether the original exception should be included as a cause, and defining
 * overwrite rules to influence the behavior. If no rules match, it either propagates the original exception or throws
 * a new one provided by `lazyException`.
 *
 * @param lazyException A supplier for creating a throwable to be thrown if no specific case or overwrite rules match.
 * @param specificCases A map of exception types to their associated throwable suppliers. If an exception matches a key
 * in this map, the corresponding supplier provides the exception to be thrown.
 * @param includeCause If true, includes the original exception as the cause of the newly thrown exception. Default is true.
 * @param overwriteOnly A class of the exception. If a caught exception matches this type, it is overwritten with
 * the exception provided by `lazyException`, unless excluded by `notOverwrite`.
 * @param notOverwrite A set of exception types that should not be overwritten, even if they match `overwriteOnly`.
 * @param block The block of code to execute. If completed successfully, its result is returned.
 *
 * @return The result of the executed block if no exceptions are thrown or if exceptions are not propagated further.
 *
 * @throws ParametersInConflictException If there is a conflict between `overwriteOnly` and `notOverwrite`,
 * or between `specificCases` and `notOverwrite`.
 * @throws Throwable If no specific handling rules apply, the original exception or a throwable from `lazyException` is propagated.
 *
 * @since 1.0.0
 */
fun <T> tryOrThrow(
    lazyException: ThrowableSupplier,
    specificCases: Map<KClass<out Throwable>, ThrowableSupplier> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeCause: Boolean = true,
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
) = tryOrThrow(lazyException, specificCases, includeCause, overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite, block)
/**
 * Executes the given block of code and applies custom exception handling based on the provided configuration.
 * If the block throws an exception, this function decides whether to suppress, propagate, or replace it with a new exception.
 *
 * @param lazyException A supplier that provides the throwable to be thrown if no specific case or rule overrides
 * apply to the encountered exception.
 * @param specificCases A map specifying exception types to be handled explicitly. For each key-value pair in the map,
 * if a caught exception matches the key, the associated supplier's throwable is thrown.
 * @param includeCause If true, includes the original exception as the cause of the newly thrown exception, if applicable.
 * Defaults to true.
 * @param overwriteOnly If specified, exceptions matching any type in this set will be replaced with the throwable
 * supplied by `lazyException`, unless excluded by `notOverwrite`.
 * @param notOverwrite Exceptions matching any type in this set will not be replaced, even if they are in the `overwriteOnly` set.
 * @param block The block of code to execute. If the block completes without throwing an exception, its result is returned.
 *
 * @return The result of the executed block if no exception occurs, or if thrown exceptions are handled without propagation.
 *
 * @throws ParametersInConflictException Thrown if there are conflicts between `overwriteOnly` and `notOverwrite`,
 * or between `specificCases` keys and `notOverwrite`.
 * @throws Throwable Propagates the original or newly created exception if no matching rule is applied.
 *
 * @since 1.0.0
 */
fun <T> tryOrThrow(
    lazyException: ThrowableSupplier,
    specificCases: Map<KClass<out Throwable>, ThrowableSupplier> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeCause: Boolean = true,
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
) = tryOrThrow(lazyException, specificCases, includeCause, overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite?.let { setOf(it) } ?: emptySet(), block)
/**
 * Executes the given block and manages exceptions based on the specified parameters.
 * Allows customization of exception handling, including specific cases, conditional overwriting,
 * and inclusion of original causes.
 *
 * @param lazyException A supplier for creating a throwable to be thrown if there are no matching cases
 * or applicable rules for exceptions.
 * @param specificCases A map where keys represent specific exception types, and values are suppliers
 * for creating custom throwables. If an exception of a matching type is caught, the corresponding
 * supplier is used to provide the throwable to throw.
 * @param includeCause A flag indicating whether the caught exception should be included as the cause
 * of the new throwable. Default is true.
 * @param overwriteOnly A set of exception types for which the caught exception should always be overwritten
 * with a new throwable provided by `lazyException`, unless specified otherwise by `notOverwrite`.
 * Defaults to an empty set.
 * @param notOverwrite A single exception type that should not be overwritten even if it is included in
 * `overwriteOnly`. This type of exception is rethrown as is.
 * @param block The block of code to invoke, potentially throwing exceptions that will be handled
 * in accordance with the provided parameters.
 *
 * @since 1.0.0
 */
fun <T> tryOrThrow(
    lazyException: ThrowableSupplier,
    specificCases: Map<KClass<out Throwable>, ThrowableSupplier> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeCause: Boolean = true,
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
) = tryOrThrow(lazyException, specificCases, includeCause, overwriteOnly, notOverwrite?.let { setOf(it) } ?: emptySet(), block)


/**
 * Executes the provided block of code and handles thrown exceptions based on the specified rules.
 *
 * This function attempts to execute the supplied block. If an exception is thrown, it applies custom handling based on
 * the provided parameters, such as specific cases for exceptions, inclusion of causes, or overwriting rules. If none of
 * the conditions are met, it propagates the exception or throws a new one as defined.
 *
 * @param lazyException A transformer for creating a throwable to be thrown if no specific case or rules apply.
 * @param specificCases A map of specific exception types to their respective throwable suppliers. If a caught exception matches a key
 * in this map, the corresponding supplier is invoked to provide the exception to be thrown.
 * @param includeCause Determines whether the original exception should be set as the cause of the newly thrown exception.
 * Default is true.
 * @param overwriteOnly A set of exception types. If a caught exception’s type is included in this set, and not in the
 * `notOverwrite` set, a new exception from `lazyException` is thrown, optionally including the original exception as its cause.
 * @param notOverwrite A set of exception types that should not be overwritten, even if they are present in the `overwriteOnly` set.
 * @param block The block of code to be executed. If it completes without throwing an exception, its result is returned.
 *
 * @return The result of the executed block if no exception is thrown or if exceptions are handled and not propagated.
 *
 * @throws ParametersInConflictException If there is a conflict between the values of `overwriteOnly` and `notOverwrite`,
 * or between the keys of `specificCases` and the `notOverwrite` set.
 * @throws Throwable If a specific case, overwrite rules, or new exception rules are not applicable,
 * the original or newly created exception is propagated.
 *
 * @since 1.0.0
 */
fun <T> tryOrThrow(
    lazyException: ThrowableTransformer,
    specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeCause: Boolean = true,
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
): T {
    if (overwriteOnly isIntersecting notOverwrite) throw ParametersInConflictException(
        callableName = "tryOrThrow",
        parametersName = listOf("overwriteOnly", "notOverwrite"),
        valuesInConflict = overwriteOnly intersect notOverwrite
    )
    if (specificCases.keys isIntersecting notOverwrite) throw ParametersInConflictException(
        callableName = "tryOrThrow",
        parametersName = listOf("specificCases", "notOverwrite"),
        valuesInConflict = specificCases.keys intersect notOverwrite
    )
    return try {
        block()
    } catch (e: Throwable) {
        if (e::class in specificCases) throwWithOrWithoutCause(specificCases[e::class]!!, includeCause, e)
        else if (overwriteOnly.isEmpty() && notOverwrite.isEmpty())
            throwWithOrWithoutCause(lazyException, includeCause, e)
        else {
            if (e::class !in overwriteOnly || e::class in notOverwrite) throw e
            else throwWithOrWithoutCause(lazyException, includeCause, e)
        }
    }
}
/**
 * Executes the provided block of code and handles thrown exceptions based on the specified rules.
 *
 * This function executes the given block of code and applies custom exception handling logic. It supports specifying
 * specific cases for exceptions, determining whether the original exception should be included as a cause, and defining
 * overwrite rules to influence the behavior. If no rules match, it either propagates the original exception or throws
 * a new one provided by `lazyException`.
 *
 * @param lazyException A transformer for creating a throwable to be thrown if no specific case or overwrite rules match.
 * @param specificCases A map of exception types to their associated throwable suppliers. If an exception matches a key
 * in this map, the corresponding supplier provides the exception to be thrown.
 * @param includeCause If true, includes the original exception as the cause of the newly thrown exception. Default is true.
 * @param overwriteOnly A class of the exception. If a caught exception matches this type, it is overwritten with
 * the exception provided by `lazyException`, unless excluded by `notOverwrite`.
 * @param notOverwrite A set of exception types that should not be overwritten, even if they match `overwriteOnly`.
 * @param block The block of code to execute. If completed successfully, its result is returned.
 *
 * @return The result of the executed block if no exceptions are thrown or if exceptions are not propagated further.
 *
 * @throws ParametersInConflictException If there is a conflict between `overwriteOnly` and `notOverwrite`,
 * or between `specificCases` and `notOverwrite`.
 * @throws Throwable If no specific handling rules apply, the original exception or a throwable from `lazyException` is propagated.
 *
 * @since 1.0.0
 */
fun <T> tryOrThrow(
    lazyException: ThrowableTransformer,
    specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeCause: Boolean = true,
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: Set<KClass<out Throwable>> = emptySet(),
    block: Supplier<T>
) = tryOrThrow(lazyException, specificCases, includeCause, overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite, block)
/**
 * Executes the given block of code and applies custom exception handling based on the provided configuration.
 * If the block throws an exception, this function decides whether to suppress, propagate, or replace it with a new exception.
 *
 * @param lazyException A transformer that provides the throwable to be thrown if no specific case or rule overrides
 * apply to the encountered exception.
 * @param specificCases A map specifying exception types to be handled explicitly. For each key-value pair in the map,
 * if a caught exception matches the key, the associated supplier's throwable is thrown.
 * @param includeCause If true, includes the original exception as the cause of the newly thrown exception, if applicable.
 * Defaults to true.
 * @param overwriteOnly If specified, exceptions matching any type in this set will be replaced with the throwable
 * supplied by `lazyException`, unless excluded by `notOverwrite`.
 * @param notOverwrite Exceptions matching any type in this set will not be replaced, even if they are in the `overwriteOnly` set.
 * @param block The block of code to execute. If the block completes without throwing an exception, its result is returned.
 *
 * @return The result of the executed block if no exception occurs, or if thrown exceptions are handled without propagation.
 *
 * @throws ParametersInConflictException Thrown if there are conflicts between `overwriteOnly` and `notOverwrite`,
 * or between `specificCases` keys and `notOverwrite`.
 * @throws Throwable Propagates the original or newly created exception if no matching rule is applied.
 *
 * @since 1.0.0
 */
fun <T> tryOrThrow(
    lazyException: ThrowableTransformer,
    specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeCause: Boolean = true,
    overwriteOnly: KClass<out Throwable>?,
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
) = tryOrThrow(lazyException, specificCases, includeCause, overwriteOnly?.let { setOf(it) } ?: emptySet(), notOverwrite?.let { setOf(it) } ?: emptySet(), block)
/**
 * Executes the given block and manages exceptions based on the specified parameters.
 * Allows customization of exception handling, including specific cases, conditional overwriting,
 * and inclusion of original causes.
 *
 * @param lazyException A transformer for creating a throwable to be thrown if there are no matching cases
 * or applicable rules for exceptions.
 * @param specificCases A map where keys represent specific exception types, and values are suppliers
 * for creating custom throwables. If an exception of a matching type is caught, the corresponding
 * supplier is used to provide the throwable to throw.
 * @param includeCause A flag indicating whether the caught exception should be included as the cause
 * of the new throwable. Default is true.
 * @param overwriteOnly A set of exception types for which the caught exception should always be overwritten
 * with a new throwable provided by `lazyException`, unless specified otherwise by `notOverwrite`.
 * Defaults to an empty set.
 * @param notOverwrite A single exception type that should not be overwritten even if it is included in
 * `overwriteOnly`. This type of exception is rethrown as is.
 * @param block The block of code to invoke, potentially throwing exceptions that will be handled
 * in accordance with the provided parameters.
 *
 * @since 1.0.0
 */
fun <T> tryOrThrow(
    lazyException: ThrowableTransformer,
    specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(), // has priority to overwriteOnly and notOverwrite
    includeCause: Boolean = true,
    overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
    notOverwrite: KClass<out Throwable>?,
    block: Supplier<T>
) = tryOrThrow(lazyException, specificCases, includeCause, overwriteOnly, notOverwrite?.let { setOf(it) } ?: emptySet(), block)

/**
 * Throws an exception supplied by the provided `laxyException` function.
 * Optionally includes a cause for the thrown exception if `includeCause` is true.
 *
 * @param laxyException A supplier function that provides the exception to be thrown.
 * @param includeCause Indicates whether to include the specified cause for the thrown exception.
 * @param e The throwable to potentially set as the cause of the exception.
 * @return Nothing, since this function always throws an exception.
 * @since 1.0.0
 */
private fun throwWithOrWithoutCause(laxyException: ThrowableSupplier, includeCause: Boolean, e: Throwable): Nothing =
    throw if (includeCause) laxyException() causedBy e else laxyException()


/**
 * Throws an exception supplied by the provided `laxyException` function.
 * Optionally includes a cause for the thrown exception if `includeCause` is true.
 *
 * @param laxyException A supplier function that provides the exception to be thrown.
 * @param includeCause Indicates whether to include the specified cause for the thrown exception.
 * @param e The throwable to potentially set as the cause of the exception.
 * @return Nothing, since this function always throws an exception.
 * @since 1.0.0
 */
private fun throwWithOrWithoutCause(laxyException: ThrowableTransformer, includeCause: Boolean, e: Throwable): Nothing =
    throw if (includeCause) laxyException(e) causedBy e else laxyException(e)

/**
 * Logs a message at the specified logging level, with or without an associated exception.
 *
 * @param logger the logger instance used to log the message
 * @param level the logging level at which the message should be logged
 * @param message the log message, can be null
 * @param includeException flag indicating whether the exception should be included in the log
 * @param e the exception to include in the log if `includeException` is true
 * @since 1.0.0
 */
private fun logWithOrWithoutException(logger: Logger, level: LogLevel, message: String?, includeException: Boolean, e: Throwable) {
    if (includeException) log(logger, level, message ?: e.message ?: String.EMPTY, e)
    else log(logger, level, message ?: e.message ?: String.EMPTY)
}

/**
 * Converts the receiver object to a string representation in a safe manner.
 * If the object is null, the string "null" is returned.
 * If the object is an array, a deep string representation of the array is returned.
 * Otherwise, the object's `toString()` method is used.
 *
 * @receiver The object to be converted to a string.
 * @return A string representation of the receiver, handling null and array types safely.
 * @since 1.0.0
 */
fun Any?.toSafeString(): String = when (this) {
    null -> "null"
    is Array<*> -> contentDeepToString()
    is ByteArray -> contentToString()
    is ShortArray -> contentToString()
    is LongArray -> contentToString()
    is FloatArray -> contentToString()
    is DoubleArray -> contentToString()
    is BooleanArray -> contentToString()
    is CharArray -> contentToString()
    else -> toString()
}

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
fun <T> T.require(causeOf: Throwable? = null, cause: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) IllegalArgumentException("Invalid argument: $this not ensure the predicate", cause) else causeOf.initCause(IllegalArgumentException("Invalid argument: $this not ensure the predicate", cause))
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
fun <T> T.require(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) IllegalArgumentException(lazyMessage().toString(), cause) else causeOf.initCause(IllegalArgumentException(lazyMessage().toString(), cause))
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
fun <T> T.requireOrThrow(lazyException: ReceiverTransformer<T, Throwable>, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw this.lazyException()
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
fun <T> T?.requireNull(causeOf: Throwable? = null, cause: Throwable? = null): T? {
    contract {
        returns() implies (this@requireNull == null)
    }
    if (this != null) throw if (causeOf == null) IllegalArgumentException("Invalid argument: $this is null", cause) else causeOf.initCause(IllegalArgumentException("Invalid argument: $this is null", cause))
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
fun <T> T?.requireNull(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T? {
    contract {
        returns() implies (this@requireNull == null)
    }
    if (this != null) throw if (causeOf == null) IllegalArgumentException(lazyMessage().toString(), cause) else causeOf.initCause(IllegalArgumentException(lazyMessage().toString(), cause))
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
fun <T> T?.requireNullOrThrow(lazyException: ThrowableSupplier): T? {
    contract {
        returns() implies (this@requireNullOrThrow == null)
    }
    if (this != null) throw lazyException()
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
fun <T> T?.requireNotNull(causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@requireNotNull != null)
    }
    if (this == null) throw if (causeOf == null) IllegalArgumentException("Invalid argument: $this is null", cause) else causeOf.initCause(IllegalArgumentException("Invalid argument: $this is null", cause))
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
fun <T> T?.requireNotNull(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    contract {
        returns() implies (this@requireNotNull != null)
    }
    if (this == null) throw if (causeOf == null) IllegalArgumentException(lazyMessage().toString(), cause) else causeOf.initCause(IllegalArgumentException(lazyMessage().toString(), cause))
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
fun <T> T.check(causeOf: Throwable? = null, cause: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) IllegalStateException("Invalid argument: $this not ensure the predicate", cause) else causeOf.initCause(IllegalStateException("Invalid argument: $this not ensure the predicate", cause))
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
fun <T> T.check(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) IllegalStateException(lazyMessage().toString(), cause) else causeOf.initCause(IllegalStateException(lazyMessage().toString(), cause))
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

fun <T> T?.checkNull(causeOf: Throwable? = null, cause: Throwable? = null): T? {
    contract {
        returns() implies (this@checkNull == null)
    }
    if (this != null) throw if (causeOf == null) IllegalStateException("Invalid state: $this is not-null", cause) else causeOf.initCause(IllegalStateException("Invalid state: $this is not-null", cause))
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

fun <T> T?.checkNull(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T? {
    contract {
        returns() implies (this@checkNull == null)
    }
    if (this != null) throw if (causeOf == null) IllegalStateException(lazyMessage().toString(), cause) else causeOf.initCause(IllegalStateException(lazyMessage().toString(), cause))
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

fun <T> T?.checkNotNull(causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@checkNotNull != null)
    }
    if (this == null) throw if (causeOf == null) IllegalStateException("Invalid state: $this is null", cause) else causeOf.initCause(IllegalStateException("Invalid state: $this is null", cause))
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

fun <T> T?.checkNotNull(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    contract {
        returns() implies (this@checkNotNull != null)
    }
    if (this == null) throw if (causeOf == null) IllegalStateException(lazyMessage().toString(), cause) else causeOf.initCause(IllegalStateException(lazyMessage().toString(), cause))
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
fun <T> T.validate(causeOf: Throwable? = null, cause: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) ValidationFailedException("Validation failed.", cause) else causeOf.initCause(ValidationFailedException("Validation failed.", cause))
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
fun <T> T.validate(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) ValidationFailedException(lazyMessage().toString(), cause) else causeOf.initCause(ValidationFailedException(lazyMessage().toString(), cause))
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
fun <T> T.validate(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) ValidationFailedException(property, variableName, message, cause) else causeOf.initCause(ValidationFailedException(property, variableName, message, cause))
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
fun <T> T.validate(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) ValidationFailedException(property, variable, message, cause) else causeOf.initCause(ValidationFailedException(property, variable, message, cause))
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
fun <T> T.validate(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message, cause) else causeOf.initCause(ValidationFailedException(callable, parameterName, message, cause))
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
fun <T> T.validate(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) ValidationFailedException(callable, parameter, message, cause) else causeOf.initCause(ValidationFailedException(callable, parameter, message, cause))
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
fun <T> T.validate(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message, cause) else causeOf.initCause(ValidationFailedException(callableName, parameterName, message, cause))
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
fun <T> T.validate(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message, cause) else causeOf.initCause(ValidationFailedException(callableName, parameter, message, cause))
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
@JvmName("receiverValidateNull")

fun <T> T?.validateNull(causeOf: Throwable? = null, cause: Throwable? = null): T? {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException("Value is not null.", cause) else causeOf.initCause(ValidationFailedException("Value is not null.", cause))
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

fun <T> T?.validateNull(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T? {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(lazyMessage().toString(), cause) else causeOf.initCause(ValidationFailedException(lazyMessage().toString(), cause))
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

fun <T> T.validateNull(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is not null", cause) else causeOf.initCause(ValidationFailedException(property, variableName, message ?: "is not null", cause))
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

fun <T> T.validateNull(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is not null", cause) else causeOf.initCause(ValidationFailedException(property, variable, message ?: "is not null", cause))
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

fun <T> T.validateNull(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is not null", cause) else causeOf.initCause(ValidationFailedException(callable, parameterName, message ?: "is not null", cause))
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

fun <T> T.validateNull(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is not null", cause) else causeOf.initCause(ValidationFailedException(callable, parameter, message ?: "is not null", cause))
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

fun <T> T.validateNull(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is not null", cause) else causeOf.initCause(ValidationFailedException(callableName, parameterName, message ?: "is not null", cause))
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

fun <T> T.validateNull(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNull == null)
    }
    if (this != null) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is not null", cause) else causeOf.initCause(ValidationFailedException(callableName, parameter, message ?: "is not null", cause))
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
@JvmName("receiverValidateNotNull")

fun <T> T?.validateNotNull(causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException("Value is null.", cause) else causeOf.initCause(ValidationFailedException("Value is null.", cause))
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
@JvmName("receiverValidateNotNull")

fun <T> T?.validateNotNull(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(lazyMessage().toString(), cause) else causeOf.initCause(ValidationFailedException(lazyMessage().toString(), cause))
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

fun <T> T.validateNotNull(property: KProperty<*>?, variableName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(property, variableName, message ?: "is null", cause) else causeOf.initCause(ValidationFailedException(property, variableName, message ?: "is null", cause))
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

fun <T> T.validateNotNull(property: KProperty<*>?, variable: KProperty<*>?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(property, variable, message ?: "is null", cause) else causeOf.initCause(ValidationFailedException(property, variable, message ?: "is null", cause))
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

fun <T> T.validateNotNull(callable: KFunction<*>?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(callable, parameterName, message ?: "is null", cause) else causeOf.initCause(ValidationFailedException(callable, parameterName, message ?: "is null", cause))
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

fun <T> T.validateNotNull(callable: KFunction<*>?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(callable, parameter, message ?: "is null", cause) else causeOf.initCause(ValidationFailedException(callable, parameter, message ?: "is null", cause))
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

fun <T> T.validateNotNull(callableName: String?, parameterName: String? = null, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(callableName, parameterName, message ?: "is null", cause) else causeOf.initCause(ValidationFailedException(callableName, parameterName, message ?: "is null", cause))
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

fun <T> T.validateNotNull(callableName: String?, parameter: KParameter?, message: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@validateNotNull != null)
    }
    if (this == null) throw if (causeOf == null) ValidationFailedException(callableName, parameter, message ?: "is null", cause) else causeOf.initCause(ValidationFailedException(callableName, parameter, message ?: "is null", cause))
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
fun <T> T.validateInputFormat(message: String? = null, causeOf: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) MalformedInputException(message) else causeOf.initCause(MalformedInputException(message))
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
fun <T> T.validateInputFormat(`class`: KClass<*>? = null, causeOf: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) MalformedInputException(`class`) else causeOf.initCause(MalformedInputException(`class`))
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
fun <T> T.validateInputFormat(type: KType? = null, causeOf: Throwable? = null, predicate: ReceiverPredicate<T>): T {
    if (!this.predicate()) throw if (causeOf == null) MalformedInputException(type) else causeOf.initCause(MalformedInputException(type))
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
fun <T : CharSequence> T.validateInputFormat(regex: Regex, message: String? = null, causeOf: Throwable? = null): T {
    if (!regex(this)) throw if (causeOf == null) MalformedInputException(message) else causeOf.initCause(MalformedInputException(message))
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
fun <T : CharSequence> T.validateInputFormat(regex: Regex, `class`: KClass<*>? = null, causeOf: Throwable? = null): T {
    if (!regex(this)) throw if (causeOf == null) MalformedInputException(`class`) else causeOf.initCause(MalformedInputException(`class`))
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
fun <T : CharSequence> T.validateInputFormat(regex: Regex, type: KType? = null, causeOf: Throwable? = null): T {
    if (!regex(this)) throw if (causeOf == null) MalformedInputException(type) else causeOf.initCause(MalformedInputException(type))
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
fun <T> T.expect(expectation: T, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (this != expectation) throw
        if (causeOf == null) ExpectationMismatchException("Value was expected as ${if (expectation.toString().isBlank()) "\"\"" else expectation}, but was $this", cause)
        else causeOf.initCause(ExpectationMismatchException("Value was expected as ${if (expectation.toString().isBlank()) "\"\"" else expectation}, but was $this", cause))
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
fun <T> T.expect(expectation: T, causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(lazyMessage().toString(), cause) else causeOf.initCause(ExpectationMismatchException(lazyMessage().toString(), cause))
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
fun <T> T.expect(expectation: T, property: KProperty<*>?, variableName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(property, variableName, expectation, this, cause) else causeOf.initCause(ExpectationMismatchException(property, variableName, expectation, this, cause))
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
fun <T> T.expect(expectation: T, property: KProperty<*>?, variable: KProperty<*>?, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(property, variable, expectation, this, cause) else causeOf.initCause(ExpectationMismatchException(property, variable, expectation, this, cause))
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
fun <T> T.expect(expectation: T, callable: KFunction<*>?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, expectation, this, cause) else causeOf.initCause(ExpectationMismatchException(callable, parameterName, expectation, this, cause))
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
fun <T> T.expect(expectation: T, callable: KFunction<*>?, parameter: KParameter?, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, expectation, this, cause) else causeOf.initCause(ExpectationMismatchException(callable, parameter, expectation, this, cause))
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
fun <T> T.expect(expectation: T, callableName: String?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, expectation, this, cause) else causeOf.initCause(ExpectationMismatchException(callableName, parameterName, expectation, this, cause))
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
fun <T> T.expect(expectation: T, callableName: String?, parameter: KParameter?, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (this != expectation) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, expectation, this, cause) else causeOf.initCause(ExpectationMismatchException(callableName, parameter, expectation, this, cause))
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

fun <T> T.expectNull(causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException("Variable was expected to be null, but was $this", cause) else causeOf.initCause(ExpectationMismatchException("Variable was expected to be null, but was $this", cause))
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

fun <T> T.expectNull(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(lazyMessage().toString(), cause) else causeOf.initCause(ExpectationMismatchException(lazyMessage().toString(), cause))
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

fun <T> T.expectNull(property: KProperty<*>?, variableName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(property, variableName, null, this, cause) else causeOf.initCause(ExpectationMismatchException(property, variableName, null, this, cause))
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

fun <T> T.expectNull(property: KProperty<*>?, variable: KProperty<*>?, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(property, variable, null, this, cause) else causeOf.initCause(ExpectationMismatchException(property, variable, null, this, cause))
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

fun <T> T.expectNull(callable: KFunction<*>?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(callable, parameterName, null, this, cause) else causeOf.initCause(ExpectationMismatchException(callable, parameterName, null, this, cause))
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

fun <T> T.expectNull(callable: KFunction<*>?, parameter: KParameter?, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(callable, parameter, null, this, cause) else causeOf.initCause(ExpectationMismatchException(callable, parameter, null, this, cause))
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

fun <T> T.expectNull(callableName: String?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(callableName, parameterName, null, this, cause) else causeOf.initCause(ExpectationMismatchException(callableName, parameterName, null, this, cause))
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

fun <T> T.expectNull(callableName: String?, parameter: KParameter?, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@expectNull == null)
    }
    if (this != null) throw if (causeOf == null) ExpectationMismatchException(callableName, parameter, null, this, cause) else causeOf.initCause(ExpectationMismatchException(callableName, parameter, null, this, cause))
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
fun <T : Any> T.expectClass(expectationClass: KClass<*>, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(expectationClass, this::class, cause)
    else causeOf.initCause(ClassMismatchException(expectationClass, this::class, cause))
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
fun <T : Any> T.expectClass(expectationClass: KClass<*>, causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(lazyMessage().toString(), cause) else causeOf.initCause(ClassMismatchException(lazyMessage().toString(), cause))
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
fun <T : Any> T.expectClass(expectationClass: KClass<*>, property: KProperty<*>?, variableName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(property, variableName, expectationClass, cause) else causeOf.initCause(ClassMismatchException(property, variableName, expectationClass, cause))
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
fun <T : Any> T.expectClass(expectationClass: KClass<*>, property: KProperty<*>?, variable: KProperty<*>?, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(property, variable, expectationClass, cause) else causeOf.initCause(ClassMismatchException(property, variable, expectationClass, cause))
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
fun <T : Any> T.expectClass(expectationClass: KClass<*>, callable: KFunction<*>?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(callable, parameterName, expectationClass, cause) else causeOf.initCause(ClassMismatchException(callable, parameterName, expectationClass, cause))
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
fun <T : Any> T.expectClass(expectationClass: KClass<*>, callable: KFunction<*>?, parameter: KParameter?, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(callable, parameter, expectationClass, cause) else causeOf.initCause(ClassMismatchException(callable, parameter, expectationClass, cause))
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
fun <T : Any> T.expectClass(expectationClass: KClass<*>, callableName: String?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(callableName, parameterName, expectationClass, this::class, cause) else causeOf.initCause(ClassMismatchException(callableName, parameterName, expectationClass, this::class, cause))
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
fun <T : Any> T.expectClass(expectationClass: KClass<*>, callableName: String?, parameter: KParameter?, causeOf: Throwable? = null, cause: Throwable? = null): T {
    if (!expectationClass.isInstance(this)) throw if (causeOf == null) ClassMismatchException(callableName, parameter, expectationClass, cause) else causeOf.initCause(ClassMismatchException(callableName, parameter, expectationClass, cause))
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
 * @throws RequiredFieldException if the object is null.
 * @since 1.0.0
 */

fun <T> T?.requiredField(causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@requiredField != null)
    }
    if (this == null) throw if (causeOf == null) RequiredFieldException("Field is required.", cause) else causeOf.initCause(RequiredFieldException("Field is required.", cause))
    return this
}

/**
 * Ensures that the receiver is non-null. If the receiver is null, a `RequiredFieldException` is thrown.
 *
 * @param causeOf an optional pre-existing throwable to use as the root cause for the exception, if applicable.
 * @param cause an optional secondary throwable to specify additional context for the exception.
 * @param lazyMessage a lambda function supplying a custom message for the exception when it is thrown.
 * @return the receiver instance if it is non-null.
 * @since 1.0.0
 */

fun <T> T?.requiredField(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    contract {
        returns() implies (this@requiredField != null)
    }
    if (this == null) throw if (causeOf == null) RequiredFieldException(lazyMessage().toString(), cause) else causeOf.initCause(RequiredFieldException(lazyMessage().toString(), cause))
    return this
}
/**
 * Validates that the current object (`this`) is not null and throws a [RequiredFieldException]
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
 * @param cause An optional underlying cause of the [RequiredFieldException], used for debugging or chaining.
 * @return The current object (`this`), guaranteed to be non-null.
 * @throws RequiredFieldException If the current object (`this`) is null, providing detailed error context.
 * @since 1.0.0
 */

fun <T> T?.requiredField(property: KProperty<*>?, variableName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@requiredField != null)
    }
    if (this == null) throw if (causeOf == null) RequiredFieldException(property, variableName, cause) else causeOf.initCause(RequiredFieldException(property, variableName, cause))
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
 * @since 1.0.0
 */
fun <T> KProperty0<T>.requiredField(variableName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null) = get().run {
    if (this == null) throw
    if (causeOf == null) RequiredFieldException(this@requiredField, variableName, cause)
    else causeOf.initCause(RequiredFieldException(this@requiredField, variableName, cause))
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
 * @throws RequiredFieldException if the object is null, with additional context
 *         provided by `property`, `variable`, `causeOf`, or `cause`.
 * @since 1.0.0
 */

fun <T> T?.requiredField(property: KProperty<*>?, variable: KProperty<*>, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@requiredField != null)
    }
    if (this == null) throw if (causeOf == null) RequiredFieldException(property, variable, cause) else causeOf.initCause(RequiredFieldException(property, variable, cause))
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
 * @since 1.0.0
 */
fun <T> KProperty0<T>.requiredField(variable: KProperty<*>, causeOf: Throwable? = null, cause: Throwable? = null) = get().run {
    if (this == null) throw
    if (causeOf == null) RequiredFieldException(this@requiredField, variable, cause)
    else causeOf.initCause(RequiredFieldException(this@requiredField, variable, cause))
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

fun <T> T?.requiredParameter(causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException("Parameter is required.", cause) else causeOf.initCause(RequiredParameterException("Parameter is required.", cause))
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

fun <T> T?.requiredParameter(causeOf: Throwable? = null, cause: Throwable? = null, lazyMessage: Supplier<Any>): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException(lazyMessage().toString(), cause) else causeOf.initCause(RequiredParameterException(lazyMessage().toString(), cause))
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

fun <T> T?.requiredParameter(callable: KFunction<*>?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException(callable, parameterName, cause) else causeOf.initCause(RequiredParameterException(callable, parameterName, cause))
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

fun <T> T?.requiredParameter(callable: KFunction<*>?, parameter: KParameter, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException(callable, parameter, cause) else causeOf.initCause(RequiredParameterException(callable, parameter, cause))
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

fun <T> T?.requiredParameter(callableName: String?, parameterName: String? = null, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException(callableName, parameterName, cause) else causeOf.initCause(RequiredParameterException(callableName, parameterName, cause))
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

fun <T> T?.requiredParameter(callableName: String?, parameter: KParameter, causeOf: Throwable? = null, cause: Throwable? = null): T {
    contract {
        returns() implies (this@requiredParameter != null)
    }
    if (this == null) throw if (causeOf == null) RequiredParameterException(callableName, parameter, cause) else causeOf.initCause(RequiredParameterException(callableName, parameter, cause))
    return this
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

@Deprecated("Use `value.isNull() || throw` instead", ReplaceWith("value.isNull() || throw lazyException()", "dev.tommasop1804.kutils.isNull"))
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

@Deprecated("Use `value.isNotNull() || throw` instead", ReplaceWith("value.isNotNull() || throw lazyException()", "dev.tommasop1804.kutils.isNotNull"))
inline fun <T> requireNotNullOrThrow(value: T?, lazyException: ThrowableSupplier): T {
    contract {
        returns() implies (value != null)
    }
    if (value == null) throw lazyException.invoke()
    return value
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

/**
 * Repeatedly applies a given function to an object a specified number of times.
 *
 * @receiver The initial object to which the function is applied.
 * @param times the number of times the function should be applied. Must be non-negative;
 * passing a non-positive value will result in the method returning the initial object.
 * @param transformer the function to apply to the object.
 * @return the result of applying the function to the object the specified number of times,
 * or the initial object if times is non-positive.
 * @since 1.0.0
 */
fun <T> T.repeat(times: Int, transformer: Transformer<T, T>): T {
    if (times <= 0) return this

    var result = this
    for (i in 0..<times) {
        result = transformer(result)
    }
    return result
}

/**
 * Serializes the current object into its JSON string representation.
 *
 * This extension function utilizes `JSONUtils.toJson` to convert the object
 * into a JSON-encoded string, ensuring consistent serialization across the application.
 *
 * @receiver Any object to be serialized.
 * @return The JSON string representation of the object.
 * @since 1.0.0
 */
fun Any?.serialize() = MAPPER.writeValueAsString(this)!!
// deserialize method is in StringUtils

/**
 * Attempts to convert a string to an enum value using valueOf.
 *
 * @receiver The string to be converted
 * @param enumClass The enum class to convert to
 * @return The enum value if successful, null otherwise
 * @since 1.0.0
 */
inline infix fun <reified T : Enum<T>> String.like(enumClass: Class<T>): T? = try {
    java.lang.Enum.valueOf(enumClass, convertCase(to = TextCase.UPPER_SNAKE_CASE))
} catch (e: Exception) { null }

/**
 * Attempts to convert a string to an enum value using valueOf.
 *
 * @receiver The string to be converted
 * @param enumClass The enum class to convert to
 * @return The enum value if successful, null otherwise
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
inline infix fun <reified T : Enum<T>> String.like(enumClass: KClass<T>): T? = try {
    java.lang.Enum.valueOf(enumClass.java, convertCase(to = TextCase.UPPER_SNAKE_CASE))
} catch (e: Exception) { null }

/**
 * Attempts to convert a string to an enum value using valueOf.
 *
 * @receiver The string to be converted
 * @return The enum value if successful
 * @throws EntryNotFoundException if the string does not match any enum value.
 * @since 1.0.0
 */
inline fun <reified T : Enum<T>> String.toEnumConst(): T = try {
    java.lang.Enum.valueOf(T::class.java, convertCase(to = TextCase.UPPER_SNAKE_CASE))
} catch (e: IllegalArgumentException) { throw EntryNotFoundException(T::class, this) }

/**
 * Wraps the given value into a singleton list.
 *
 * @return a list containing only this instance.
 * @since 1.0.0
 */
fun <T> T.asSingleList(): List<T> = listOf(this)
/**
 * Converts the current object into an MList containing only this object as its single element.
 *
 * @return an MList containing the current object as its single element.
 * @since 1.0.0
 */
fun <T> T.asSingleMList(): MList<T> = mListOf(this)
/**
 * Wraps the current element into a singleton set containing only this element.
 *
 * @return a set containing the current element as its sole member.
 * @since 1.0.0
 */
fun <T> T.asSingleSet(): Set<T> = setOf(this)
/**
 * Converts the current object into a mutable set containing only this object as its single element.
 *
 * @return A mutable set containing the current object as its only element.
 * @since 1.0.0
 */
fun <T> T.asSingleMSet(): MSet<T> = mSetOf(this)
/**
 * Converts the current key to a single-entry map with the specified value.
 *
 * @param value The value to associate with the key in the resulting map.
 * @return A map containing the current key associated with the specified value.
 * @since 1.0.0
 */
fun <K, V> K.asSingleMap(value: V): Map<K, V> = mapOf(this to value)
/**
 * Converts the current value to a single-entry map with the specified key.
 *
 * @param key The key to associate with the value in the resulting map.
 * @return A map containing the current value associated with the specified value.
 * @since 1.0.0
 */
fun <K, V> V.asSingleMapValue(key: K): Map<K, V> = mapOf(key to this)
/**
 * Creates a single-pair mutable map with the current object as the key and the specified value.
 *
 * This function allows for constructing a mutable map (`MMap`) containing a single key-value pair,
 * where the key is the current object (`this`) and the value is the provided argument.
 *
 * @param value the value associated with the key represented by the current object
 * @return a mutable map containing the single key-value pair
 * @since 1.0.0
 */
fun <K, V> K.asSingleMMap(value: V): MMap<K, V> = mMapOf(this to value)
/**
 * Converts the current value to a single-entry mutable map with the specified key.
 *
 * @param key The key to associate with the value in the resulting map.
 * @return A map containing the current value associated with the specified value.
 * @since 1.0.0
 */
fun <K, V> V.asSingleMMapValue(key: K): Map<K, V> = mapOf(key to this)
/**
 * Converts this pair into a map containing a single key-value pair.
 *
 * @return a map containing the key from the first component of the pair and the value from the second component.
 * @since 1.0.0
 */
fun <K, V> Pair<K, V>.asSingleMap(): Map<K, V> = mapOf(this)
/**
 * Converts the current pair into a single-entry mutable map.
 *
 * This function creates a new `MMap` containing the current pair as its only entry.
 *
 * @return a new mutable map containing a single key-value pair from this `Pair`
 * @since 1.0.0
 */
fun <K, V> Pair<K, V>.asSingleMMap(): MMap<K, V> = mMapOf(this)
/**
 * Creates a map where the key and value are both the instance itself.
 *
 * @return A map containing a single entry where the instance is both the key and the value.
 * @since 1.0.0
 */
fun <T> T.asSelfMap(): Map<T, T> = mapOf(this to this)
/**
 * Creates a mutable map with the current object as both the key
 * and the value, effectively mapping the object to itself.
 *
 * This function is particularly useful for initializing maps where objects
 * are intended to be self-referencing keys and values.
 *
 * @return a new mutable map where the current object is both the key and the value
 * @since 1.0.0
 */
fun <T> T.asSelfMMap(): MMap<T, T> = mMapOf(this to this)

/**
 * Prints the integer value to the standard output.
 *
 * This extension function allows you to directly print an integer
 * using the `print` function of Kotlin.
 *
 * @receiver The integer value to be printed.
 * @since 1.0.0
 */
fun Int.print() = apply { print(this) }
/**
 * Prints the value of the Byte to the standard output stream.
 *
 * This function uses the standard `print` method to output
 * the Byte value without any additional formatting or line breaks.
 *
 * @receiver The Byte value to be printed.
 * @since 1.0.0
 */
fun Byte.print() = apply { print(this) }
/**
 * Prints the character to the standard output.
 *
 * This method invokes the `print` function to display the given character.
 *
 * @receiver The character to be printed.
 * @since 1.0.0
 */
fun Char.print() = apply { print(this) }
/**
 * Prints the value of the Long instance to the standard output.
 *
 * This function utilizes the `print` function from the Kotlin standard library
 * to output the value of the current Long receiver.
 *
 * @receiver Long value to be printed.
 * @since 1.0.0
 */
fun Long.print() = apply { print(this) }
/**
 * Prints the current `Float` value to the standard output.
 *
 * This function delegates the printing task to the standard `print` function
 * and displays the float value as a string.
 *
 * @receiver The `Float` value to be printed.
 * @since 1.0.0
 */
fun Float.print() = apply { print(this) }
/**
 * Prints the value of the current `Short` instance to the standard output.
 *
 * This function uses Kotlin's standard `print` function to display the value
 * of the `Short` on the same line without appending a newline character.
 *
 * @receiver The `Short` value to be printed.
 *
 * @since 1.0.0
 */
fun Short.print() = apply { print(this) }
/**
 * Prints the value of the Double instance to the standard output.
 *
 * This method wraps the [kotlin.io.print] function for the Double type, allowing
 * instances of Double to be directly printed using member function syntax.
 *
 * @receiver The Double value to be printed.
 * @since 1.0.0
 */
fun Double.print() = apply { print(this) }
/**
 * Prints the Boolean value to the standard output.
 *
 * This method outputs the current Boolean value (`true` or `false`)
 * to the console using the `print` function.
 *
 * @since 1.0.0
 */
fun Boolean.print() = apply { print(this) }
/**
 * Prints the contents of the CharArray to the standard output.
 *
 * This function writes all characters in the CharArray to the console or the standard output stream.
 * Each character in the array is outputted sequentially without any additional formatting or separators.
 *
 * Note that the behavior of this function is synonymous with passing the CharArray directly to
 * the standard print function.
 *
 * @receiver The CharArray whose content will be printed to the standard output.
 * @since 1.0.0
 */
fun CharArray.print() = apply { print(toSafeString()) }
/**
 * Prints the string representation of the nullable receiver object to the standard output.
 * If the receiver is `null`, it prints the string `"null"`.
 *
 * This function provides a shorthand for printing any object, including `null`, without explicit null checks.
 *
 * @receiver The nullable object to be printed.
 * @since 1.0.0
 */
@JvmName("printGeneric")
fun <T> T.print() = apply { print(toSafeString()) }

/**
 * Prints the integer receiver to the standard output followed by a newline.
 *
 * The method delegates the printing operation to the standard `println` function.
 *
 * @receiver The integer to be printed.
 * @since 1.0.0
 */
fun Int.println() = apply { println(this) }
/**
 * Prints the Byte value to the standard output followed by a newline.
 *
 * This extension function enhances the Byte type by providing a direct way to
 * output its value to the console.
 *
 * The function utilizes the standard `println` to perform the output operation.
 *
 * @receiver The Byte value to be printed.
 * @since 1.0.0
 */
fun Byte.println() = apply { println(this) }
/**
 * Prints the character value of the receiver to the standard output followed by a line break.
 *
 * This function delegates to the standard `println` function in Kotlin, which appends a line
 * separator after printing the value.
 *
 * @receiver The character to be printed to the standard output.
 * @since 1.0.0
 */
fun Char.println() = apply { println(this) }
/**
 * Prints the `Long` value to the standard output.
 *
 * This method is an extension function for the `Long` type, enabling a concise way
 * to output its value directly.
 *
 * @receiver The `Long` value to be printed.
 * @since 1.0.0
 */
fun Long.println() = apply { println(this) }
/**
 * Prints the float value to the standard output followed by a newline.
 *
 * This method is an extension function for the `Float` type, allowing
 * direct invocation on a `Float` instance to print its value.
 *
 * @receiver The `Float` value to be printed.
 * @since 1.0.0
 */
fun Float.println() = apply { println(this) }
/**
 * Prints the value of the Short receiver to the standard output, followed by a newline.
 *
 * This method leverages the standard println function for output.
 *
 * @receiver The Short value to be printed.
 * @since 1.0.0
 */
fun Short.println() = apply { println(this) }
/**
 * Prints the value of the Double to the standard output followed by a newline character.
 *
 * This function is an extension for the Double type and simplifies the process
 * of printing a Double value directly.
 *
 * @receiver The Double value to be printed.
 * @since 1.0.0
 */
fun Double.println() = apply { println(this) }
/**
 * Prints the boolean value to the standard output followed by a newline.
 *
 * This method invokes the standard `println` function to display
 * the boolean value (`true` or `false`) represented by the receiver.
 *
 * @receiver the boolean value to be printed.
 * @since 1.0.0
 */
fun Boolean.println() = apply { println(this) }
/**
 * Prints the contents of the CharArray to the standard output, followed by a newline character.
 *
 * The function delegates the operation to the `println` function, which converts
 * the CharArray to a string representation before printing.
 *
 * @receiver The CharArray to be printed.
 * @since 1.0.0
 */
fun CharArray.println() = apply { println(toSafeString()) }
/**
 * Prints the string representation of the object to the standard output, followed by a newline.
 *
 * If the object is `null`, it prints "null".
 * This function utilizes the platform default implementation of `println` for printing.
 *
 * @receiver Any? The object to be printed. Can be nullable.
 * @since 1.0.0
 */
@JvmName("printlnGeneric")
fun <T> T.println() = apply { println(toSafeString()) }

/**
 * Prints the integer value to the standard error stream.
 *
 * This extension function allows an integer to be directly printed
 * to the standard error stream (System.err).
 *
 * @receiver The integer to be printed.
 * @since 1.0.0
 */
fun Int.printErr() = apply { System.err.print(this) }
/**
 * Prints the byte value to the standard error stream without a newline.
 * This method writes the byte value directly as is, using the standard error output.
 *
 * @receiver Byte value to be printed to the standard error stream.
 *
 * @since 1.0.0
 */
fun Byte.printErr() = apply { System.err.print(this) }
/**
 * Prints the character to the standard error output stream.
 *
 * This function sends the provided character to the `System.err` stream.
 * It is useful for logging error messages or debugging purposes when
 * standard error output is required.
 *
 * @receiver The character to be printed to the error output stream.
 * @since 1.0.0
 */
fun Char.printErr() = apply { System.err.print(this) }
/**
 * Prints the value of the Long to the standard error stream.
 *
 * This extension function is a convenient way to write the value of the Long
 * instance directly to the error output without requiring additional formatting.
 *
 * @since 1.0.0
 */
fun Long.printErr() = apply { System.err.print(this) }
/**
 * Prints the value of the Float to the standard error stream.
 *
 * This method writes the Float value directly to the `System.err` output stream.
 *
 * @receiver The Float value to be printed.
 * @since 1.0.0
 */
fun Float.printErr() = apply { System.err.print(this) }
/**
 * Prints the value of the Short to the standard error output stream.
 *
 * This function sends the Short value directly to the `System.err` stream without any additional formatting.
 *
 * @receiver The Short value to be printed to the error stream.
 * @since 1.0.0
 */
fun Short.printErr() = apply { System.err.print(this) }
/**
 * Prints the double value to the standard error stream (`System.err`).
 *
 * This method provides a convenient way to send the numeric value
 * represented by the Double to the error output stream.
 *
 * @receiver The Double value to be printed.
 * @since 1.0.0
 */
fun Double.printErr() = apply { System.err.print(this) }
/**
 * Prints the Boolean value to the standard error stream.
 *
 * This function outputs the Boolean value (`true` or `false`) to `System.err`.
 * It can be used for debugging or logging purposes where error output is required.
 *
 * @receiver The Boolean value to be printed to the error stream.
 * @since 1.0.0
 */
fun Boolean.printErr() = apply { System.err.print(this) }
/**
 * Prints the contents of the `CharArray` to the standard error output stream.
 * Each character in the array is written in sequence.
 *
 * @receiver The `CharArray` whose contents are to be printed.
 * @since 1.0.0
 */
fun CharArray.printErr() = apply { System.err.print(toSafeString()) }
/**
 * Prints the string representation of the object to the standard error output stream.
 * If the object is `null`, "null" will be printed.
 *
 * This method is an extension function for all types, allowing any object or nullable object
 * to use it for logging or debugging purposes to the error output.
 *
 * @receiver The object whose string representation is to be printed to the standard error output stream.
 * @since 1.0.0
 */
@JvmName("printErrGeneric")
fun <T> T.printErr() = apply { System.err.println(toSafeString()) }
/**
 * Extension function for the [Int] type that outputs the integer
 * to the standard error stream.
 *
 * This method uses `System.err.println` to print the integer value.
 *
 * @receiver the integer value to be printed to the error stream.
 * @since 1.0.0
 */
fun Int.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the value of the Byte to the standard error output stream.
 *
 * This function writes the byte value to the `System.err` stream,
 * using the `println` function to append a newline after the value.
 *
 * @receiver The Byte value to be printed to the error output stream.
 * @since 1.0.0
 */
fun Byte.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the character to the standard error output stream followed by a line terminator.
 *
 * This function is useful for logging or error reporting where output is directed to the standard error stream.
 *
 * @receiver The character to be printed to the standard error output stream.
 * @since 1.0.0
 */
fun Char.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the value of the `Long` to the standard error output stream.
 *
 * This method is an extension function for the `Long` type, enabling
 * direct invocation on `Long` instances to output their value to `System.err`.
 *
 * @receiver The `Long` value to be printed to the error stream.
 * @since 1.0.0
 */
fun Long.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the floating-point number to the standard error stream.
 *
 * This method writes the value of the floating-point number
 * to the error output (System.err) followed by a new line.
 *
 * @receiver The floating-point number to be printed to the error stream.
 * @since 1.0.0
 */
fun Float.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the value of the Short instance to the standard error stream.
 *
 * This function sends the string representation of the Short value to
 * the `System.err` output, providing a mechanism to log or display
 * error-related numeric information.
 *
 * @receiver The Short value to be printed to the error output stream.
 * @since 1.0.0
 */
fun Short.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the double value of the current instance to the standard error stream.
 *
 * This function is an extension on the `Double` type and outputs the value
 * using `System.err.println`.
 *
 * @receiver The double value to be printed to the error stream.
 * @since 1.0.0
 */
fun Double.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the value of the Boolean to the standard error stream.
 *
 * This function outputs the Boolean value (`true` or `false`)
 * as a string to the error output stream.
 *
 * @receiver The Boolean value to be printed to the error output stream.
 * @since 1.0.0
 */
fun Boolean.printlnErr() = apply { System.err.println(this) }
/**
 * Prints the content of the `CharArray` to the standard error output stream.
 *
 * This method converts the `CharArray` to its string representation and sends it to `System.err`
 * for error or debug output purposes.
 *
 * @receiver The `CharArray` whose contents will be printed to the error stream.
 *
 * @since 1.0.0
 */
fun CharArray.printlnErr() = apply { System.err.println(toSafeString()) }
/**
 * Prints the string representation of the receiver object to the standard error stream.
 *
 * If the receiver is `null`, the string "null" will be printed.
 *
 * This method is a convenience function for printing error messages or diagnostic information
 * to the error output stream.
 *
 * @receiver The object whose string representation will be printed to the error stream.
 *
 * @since 1.0.0
 */
@JvmName("printlnErrGeneric")
fun <T> T.printlnErr() = apply { System.err.println(toSafeString()) }