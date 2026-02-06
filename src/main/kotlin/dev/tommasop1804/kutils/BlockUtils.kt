@file:JvmName("BlockUtilsKt")
@file:Suppress("unused", "kutils_collection_declaration", "JavaCollectionWithNullableTypeArgument",
    "kutils_tuple_declaration"
)
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import Break
import Continue
import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.memoization.LRUCache
import dev.tommasop1804.kutils.classes.memoization.TTLCache
import dev.tommasop1804.kutils.classes.time.Duration
import dev.tommasop1804.kutils.classes.tuple.Quadruple
import dev.tommasop1804.kutils.classes.tuple.Quintuple
import dev.tommasop1804.kutils.exceptions.RetryLimitExceededException
import java.util.concurrent.*
import kotlin.reflect.KClass
import kotlin.system.measureNanoTime

/**
 * Repeats the execution of the Action a specified number of times.
 *
 * @param n The number of times the Action should be executed. Must be a non-negative integer.
 * @since 1.0.0
 */
operator fun Action.times(n: Int) = (1..n).forEach { _ -> this() }

/**
 * Executes the given supplier and returns the result.
 *
 * @param supplier A functional interface representing a supplier of results.
 *                   It provides the value to be computed and returned.
 * @return The result provided by the supplier.
 * @since 1.0.0
 */
fun <T> compute(supplier: Supplier<T>) = supplier()

/**
 * Repeatedly executes the given action until a `breakLoop()` is thrown.
 * The loop handles `breakLoop()` to exit execution and `continueLoop()` to skip
 * the current iteration without terminating the loop.
 *
 * @param action The action to be invoked in each iteration of the loop.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun loop(action: ReceiverConsumer<LoopContext>) {
    with(LoopContext()) {
        while (true) {
            try {
                action()
            } catch (b: Break) {
                return
            } catch (c: Continue) {
                continue
            }
        }
    }
}
/**
 * Executes the given action within a loop that can be controlled using
 * `breakLoop()` or `continueLoop()` methods. The loop continues indefinitely,
 * unless interrupted by a `breakLoop()` method, which ends the loop.
 * If a continueLoop() method is called, the loop iterates to the next cycle.
 *
 * @param action the action to execute within the loop
 * @return the result provided by a `breakLoop` method or the last result
 *         set within a `continueLoop` method, or `null` if no result is specified
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <R> loopWithReturn(action: ReceiverConsumer<LoopContext>): R? {
    with(LoopContext()) {
        while (true) {
            try {
                action()
            } catch (b: Break) {
                return (b.result as? R)
            } catch (c: Continue) {
                continue
            }
        }
    }
}

/**
 * Exits a loop.
 *
 * @since 1.0.0
 */
context(_: LoopContext)
fun breakLoop() { throw Break() }
/**
 * Exits a loop, optionally carrying a result value.
 *
 * @param R The type of the result value to be returned from the loop.
 * @param returnValue The optional value to return when exiting the loop. Defaults to null.
 * @since 1.0.0
 */
context(_: LoopContext)
fun <R> breakLoop(returnValue: R?) {
    throw Break(returnValue)
}

/**
 * Triggers the continuation of a loop by throwing a `Continue` exception.
 * This method can be used to programmatically control loop flow by skipping
 * the current iteration and continuing with the next one.
 *
 * Note that this method relies on handling the `Continue` exception to
 * function as intended and may require specific setup in the surrounding
 * control flow environment.
 *
 * @since 1.0.0
 */
context(_: LoopContext)
fun continueLoop() { throw Continue() }

/**
 * Extension function that transforms a function into a memoized version of itself.
 * The returned function caches the results of previous calls with the same inputs,
 * improving performance for functions with expensive or repetitive computations.
 *
 * @return A memoized version of the original function.
 * @since 1.0.0
 */
fun <T, R> ((T) -> R).memoize(): (T) -> R {
    val cache = ConcurrentHashMap<T, R>()
    return { input ->
        cache.getOrPut(input) { this(input) }
    }
}
/**
 * Extension function that memoizes the results of a given binary function, storing them in a concurrent cache.
 * This allows the function to recall previously computed results for the same pair of input arguments,
 * avoiding repetitive computations and improving performance in scenarios with repeated invocations.
 *
 * @receiver A binary function taking two input parameters of types T1 and T2, and returning a result of type R.
 * @return A memoized version of the original function which retains computed results
 *         for already encountered input argument pairs, using a thread-safe cache.
 * @since 1.0.0
 */
fun <T1, T2, R> ((T1, T2) -> R).memoize(): (T1, T2) -> R {
    val cache = ConcurrentHashMap<Pair<T1, T2>, R>()
    return { t1, t2 ->
        cache.getOrPut(Pair(t1, t2)) { this(t1, t2) }
    }
}
/**
 * Extension function to memoize a function with three parameters.
 * The memoization stores the results of function calls and
 * returns the cached results when the same inputs occur again.
 *
 * This helps optimize repeated calls with the same arguments by avoiding
 * redundant computations and enhancing performance.
 *
 * @return A memoized version of the original function that caches the
 * results for previously computed input triples.
 * @since 1.0.0
 */
fun <T1, T2, T3, R> ((T1, T2, T3) -> R).memoize(): (T1, T2, T3) -> R {
    val cache = ConcurrentHashMap<Triple<T1, T2, T3>, R>()
    return { t1, t2, t3 ->
        cache.getOrPut(Triple(t1, t2, t3)) { this(t1, t2, t3) }
    }
}
/**
 * Creates a memoized version of a function that takes four parameters.
 *
 * Each unique combination of input parameters is stored in a cache,
 * and subsequent calls with the same parameters retrieve the result
 * from the cache rather than recomputing it. This is particularly
 * useful for expensive computations where input parameters are likely
 * to repeat.
 *
 * @return a memoized function that will cache results for each unique
 * combination of inputs.
 * @since 1.0.0
 */
fun <T1, T2, T3, T4, R> ((T1, T2, T3, T4) -> R).memoize(): (T1, T2, T3, T4) -> R {
    val cache = ConcurrentHashMap<Quadruple<T1, T2, T3, T4>, R>()
    return { t1, t2, t3, t4 ->
        cache.getOrPut(Quadruple(t1, t2, t3, t4)) { this(t1, t2, t3, t4) }
    }
}
/**
 * Creates a memoized version of a function that takes five parameters.
 *
 * The memoized function caches its results based on the inputs, ensuring that
 * the computation is only performed once for each unique set of arguments.
 * Subsequent calls with the same arguments retrieve the result from the cache,
 * improving performance for expensive or repetitive computations.
 *
 * This method uses a `ConcurrentHashMap` for thread-safe caching,
 * making it safe to use in concurrent environments.
 *
 * @receiver the function to be memoized
 * @return a new function that caches and returns results of the original function
 * @since 1.0.0
 */
fun <T1, T2, T3, T4, T5, R> ((T1, T2, T3, T4, T5) -> R).memoize(): (T1, T2, T3, T4, T5) -> R {
    val cache = ConcurrentHashMap<Quintuple<T1, T2, T3, T4, T5>, R>()
    return { t1, t2, t3, t4, t5 ->
        cache.getOrPut(Quintuple(t1, t2, t3, t4, t5)) { this(t1, t2, t3, t4, t5) }
    }
}

/**
 * Enhances a function to cache its results using a Least Recently Used (LRU) cache strategy,
 * so repeated calls with the same argument return cached results instead of recalculating them.
 *
 * @param T The input type of the function being memoized.
 * @param R The return type of the function being memoized.
 * @param maxSize The maximum number of entries the LRU cache can hold. Defaults to 100.
 * @return A new function that wraps the original function and caches results based on the input argument,
 *         evicting the least recently used cached result when the cache exceeds the specified size.
 * @since 1.0.0
 */
fun <T, R> ((T) -> R).memoizeLRU(maxSize: Int = 100): (T) -> R {
    val cache = LRUCache<T, R>(maxSize)
    return { input ->
        cache.getOrPut(input) { this(input) }
    }
}

/**
 * Extension function to memoize a function with a time-to-live (TTL) cache.
 * This ensures that the results of the function are cached for a specified
 * duration, avoiding repetitive and expensive calls for the same inputs.
 *
 * @param ttl The time-to-live duration for each cached entry. Defaults to 5 minutes.
 * @return A memoized version of the original function using the specified TTL.
 * @since 1.0.0
 */
fun <T, R> ((T) -> R).memoizeTTL(ttl: Duration = Duration(minutes = 5)): (T) -> R {
    val cache = TTLCache<T, R>(ttl)
    return { input ->
        cache.getOrPut(input, ttl) { this(input) }
    }
}

/**
 * Measures the execution time of a given action and returns the duration as a [Duration].
 *
 * This function invokes the action and records the time taken to execute it using nanosecond precision.
 *
 * @receiver The action function whose execution time is to be measured.
 * @return The duration of time taken for the execution of the action function.
 * @since 1.0.0
 */
@JvmName("measureTimeWithReceiver")
fun <T> Supplier<T>.measureTime() = Duration(nanos = measureNanoTime { this() })
/**
 * Measures the time taken to execute the given action function and returns the duration.
 *
 * @param action The block of code whose execution time is to be measured.
 * @since 1.0.0
 */
fun <T> measureTime(action: Supplier<T>) = Duration(nanos = measureNanoTime { action() })

/**
 * Executes the supplier function and measures the time taken for its execution.
 *
 * @return A pair containing the result of the supplier execution and the duration it took to execute.
 * @since 1.0.0
 */
@JvmName("fetchAndMeasureTimeWithReceiver")
fun <T> Supplier<T>.fetchAndMeasureTime(): Pair<T, Duration> {
    val timestamp = System.nanoTime()
    val result = this()
    return result to Duration(nanos = System.nanoTime() - timestamp)
}
/**
 * Executes a given action and measures the time taken to complete its execution.
 *
 * @param action A supplier function that represents the action to be executed.
 *               The function should return a result of type T.
 * @return A Pair where the first value is the result of the executed action,
 *         and the second value is the duration taken to execute the action.
 * @since 1.0.0
 */
fun <T> fetchAndMeasureTime(action: Supplier<T>): Pair<T, Duration> {
    val timestamp = System.nanoTime()
    val result = action()
    return result to Duration(nanos = System.nanoTime() - timestamp)
}

/**
 * Executes a given lambda function after a specified delay.
 *
 * The function will pause the current thread for the specified duration and
 * then execute the lambda function.
 *
 * @param delay The delay in milliseconds before executing the lambda.
 * @since 1.0.0
 */
@OptIn(RiskyApproximationOfTemporal::class)
infix fun <T> Supplier<T>.withDelayOf(delay: Duration): T {
    Thread.sleep(delay.toNanos() / 1_000_000L)
    return this()
}
/**
 * Executes a given lambda function after a specified delay.
 *
 * The function will pause the current thread for the specified duration and
 * then execute the lambda function.
 *
 * @param delay The delay in milliseconds before executing the lambda.
 * @param block The lambda function to be executed after the delay.
 * @since 1.0.0
 */
@OptIn(RiskyApproximationOfTemporal::class)
fun <T> withDelayOf(delay: Duration, block: Supplier<T>): T {
    Thread.sleep(delay.toNanos() / 1_000_000L)
    return block()
}

/**
 * Executes the calling lambda function and monitors its execution time.
 * If the execution time surpasses the specified time limit, a TimeoutException is thrown.
 *
 * @param timeLimit the maximum duration allowed for the execution of the lambda function
 * @param lazyException exception to throw if duration exceed limit. Default is [RetryLimitExceededException]
 * @since 1.0.0
 */
@OptIn(RiskyApproximationOfTemporal::class)
fun <T> Supplier<T>.executeWithTimeLimit(timeLimit: Duration, lazyException: ThrowableSupplier = { RetryLimitExceededException("Execution time exceeded the limit ($timeLimit)") }): T {
    val executor = Executors.newSingleThreadExecutor()
    val future: Future<T> = executor.submit(this)

    return try {
        future.get(timeLimit.toNanos(), TimeUnit.NANOSECONDS)
    } catch (e: TimeoutException) {
        future.cancel(true)
        throw lazyException()
    } finally {
        executor.shutdownNow()
    }
}
/**
 * Executes the calling lambda function and monitors its execution time.
 * If the execution time surpasses the specified time limit, a TimeoutException is thrown.
 *
 * @param timeLimit the maximum duration allowed for the execution of the lambda function
 * @param lazyException exception to throw if duration exceed limit. Default is [RetryLimitExceededException]
 * @param block lambda function to execute
 * @since 1.0.0
 */
fun <T> executeWithTimeLimit(timeLimit: Duration, lazyException: ThrowableSupplier = { RetryLimitExceededException("Execution time exceeded the limit ($timeLimit)") }, block: Supplier<T>) =
    block.executeWithTimeLimit(timeLimit, lazyException)

/**
 * Retries the execution of an action a specified number of times, handling specific exceptions.
 *
 * This method allows retrying the execution of the given action up to the provided number of times.
 * It supports specifying certain exception types to be caught or ignored during retries and allows
 * for a custom exception to be thrown when the retry limit is exceeded.
 *
 * @param times The maximum number of retry attempts. The action is attempted once initially and
 *              retried up to `times - 1` additional times on failure.
 * @param lazyException A supplier for the exception to be thrown when the retry limit is exceeded.
 *                      The default exception is `RetryLimitExceededException` with a message
 *                      indicating the retry limit.
 * @param catchOnly A set of specific exception types to be caught during retries. If provided, only exceptions
 *                  of these types will be caught; all others will be rethrown immediately.
 *                  Defaults to an empty set, meaning all exceptions are caught by default.
 * @param dontCatch A set of specific exception types to be excluded from catching during retries. If provided,
 *                 exceptions of these types will bypass the retry mechanism and be rethrown immediately.
 *                 Defaults to an empty set.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <T> Supplier<T>.retry(
    times: Int,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($times) reached") },
    catchOnly: Set<KClass<out Throwable>> = emptySet(),
    dontCatch: Set<KClass<out Throwable>> = emptySet()
): T {
    for (i in 1..times) {
        try {
            return this()
        } catch (e: Exception) {
            if (catchOnly.isNotEmpty() && catchOnly.none { it.isInstance(e) }) throw e
            if (dontCatch.isNotEmpty() && dontCatch.any { it.isInstance(e) }) throw e
        }
    }
    throw lazyException()
}
/**
 * Retries the execution of an action a specified number of times with customizable exception handling.
 *
 * This method enables retrying an action multiple times in case of failures. It provides options
 * to specify the number of retry attempts, exceptions to catch or avoid, and a custom exception
 * supplier to be thrown when the retry limit is reached.
 *
 * @param times The maximum number of retry attempts allowed. Must be greater than or equal to 1.
 * @param lazyException A supplier that provides the exception to be thrown when the retry limit
 *                      is exceeded. Defaults to `RetryLimitExceededException` with a message
 *                      including the limit.
 * @param catchOnly A class type or set of throwable classes specifying the types of exception
 *                  that should trigger a retry. If empty, retries on all exceptions by default.
 * @param dontCatch A set of throwable class types that should explicitly not trigger a retry, even
 *                 if they are included in `catchOnly`.
 * @since 1.0.0
 */
fun <T> Supplier<T>.retry(
    times: Int,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($times) reached") },
    catchOnly: KClass<out Throwable>,
    dontCatch: Set<KClass<out Throwable>> = emptySet()
) = retry(times, lazyException, catchOnly.asSingleSet(), dontCatch)
/**
 * Executes the action with a retry mechanism, allowing for a specified number of attempts and configurable
 * exception handling.
 *
 * The method retries the action up to the specified number of times (`times`). If the action does not
 * succeed within the allowed attempts, the specified exception (`lazyException`) will be thrown.
 * The retry behavior can be customized to catch only specific exceptions or exclude certain exceptions from
 * being caught during retries.
 *
 * @param times The number of retry attempts to be made before throwing the `lazyException`.
 * @param lazyException A supplier for the exception to be thrown when the retry limit is exceeded.
 *                       Defaults to a `RetryLimitExceededException` with a message mentioning the retry limit.
 * @param catchOnly A class representing an exception type that should be caught and retried during execution.
 *                  Exceptions that are instances of the specified class will trigger a retry. If empty, retries
 *                  will not be filtered by this condition.
 * @param dontCatch A class representing an exception type that should not be caught during execution. If an
 *                  exception of this type is thrown, it will not trigger a retry and will be propagated
 *                  immediately. If empty, retries will not consider this condition.
 * @since 1.0.0
 */
fun <T> Supplier<T>.retry(
    times: Int,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($times) reached") },
    catchOnly: KClass<out Throwable>,
    dontCatch: KClass<out Throwable>
) = retry(times, lazyException, catchOnly.asSingleSet(), dontCatch.asSingleSet())
/**
 * Repeatedly attempts to invoke an action with specified retry conditions.
 *
 * This function executes the given action up to the specified number of retries (`times`)
 * and stops when the action completes successfully or when the retry limit is reached.
 * It provides options to handle specific exceptions during retries based on the
 * provided `catchOnly` and `dontCatch` parameters. If the action fails after the maximum
 * retries, a custom or default exception is thrown as specified by the `lazyException`.
 *
 * @param times The number of retry attempts allowed before the operation is considered failed.
 * @param lazyException A supplier for the exception to be thrown if the retry limit is exceeded.
 * By default, it throws a `RetryLimitExceededException` with a message indicating the retry limit.
 * @param catchOnly A set of exception types to exclusively catch during retries.
 * Retries will only occur if the thrown exception matches one of these types.
 * By default, all exceptions are caught unless restricted by `dontCatch`.
 * @param dontCatch A specific exception type that will not be caught during retries.
 * If this exception type is thrown, retries are aborted immediately, and the exception propagates.
 * @since 1.0.0
 */
fun <T> Supplier<T>.retry(
    times: Int,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($times) reached") },
    catchOnly: Set<KClass<out Throwable>> = emptySet(),
    dontCatch: KClass<out Throwable>
) = retry(times, lazyException, catchOnly, dontCatch.asSingleSet())
/**
 * Retries the execution of a supplier function until it succeeds,
 * or the specified duration is exceeded. Provides configuration to
 * handle specific exceptions to catch or not catch during retries.
 *
 * @param duration The maximum duration for retrying the supplier function.
 * @param lazyException A supplier for the exception to be thrown when retries exceed the duration.
 * Defaults to throwing an `RetryLimitExceededException` with a descriptive message.
 * @param catchOnly A set of exception classes to catch exclusively during retries.
 * If not provided, all exceptions are considered catchable unless specified by `dontCatch`.
 * @param dontCatch A set of exception classes to exclude from catching during retries.
 * If not provided, exceptions are handled according to `catchOnly` or default behavior.
 * @since 1.0.0
 */
@OptIn(RiskyApproximationOfTemporal::class)
@Suppress("UNCHECKED_CAST")
fun <T> Supplier<T>.retry(
    duration: Duration,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($duration) reached") },
    catchOnly: Set<KClass<out Throwable>> = emptySet(),
    dontCatch: Set<KClass<out Throwable>> = emptySet()
): T {
    val startTime = System.currentTimeMillis()
    val endTime = startTime + duration.toMillis()

    while (System.currentTimeMillis() < endTime) {
        try { return this() } catch (e: Exception) {
            if (catchOnly.isNotEmpty() && catchOnly.none { it.isInstance(e) }) throw e
            if (dontCatch.isNotEmpty() && dontCatch.any { it.isInstance(e) }) throw e
        }
        Thread.sleep(10)
    }
    throw lazyException()
}
/**
 * Retries the execution of the action until the specified duration is exhausted.
 *
 * @param duration The total duration to allow retries before timing out.
 * @param lazyException A supplier for the exception to throw when the retry limit is reached.
 *                       Defaults to an `RetryLimitExceededException` with a message indicating the retry duration.
 * @param catchOnly A specific type of throwable to catch and retry on.
 * @param dontCatch A set of throwable types that should not be caught and retried. Defaults to an empty set.
 * @since 1.0.0
 */
fun <T> Supplier<T>.retry(
    duration: Duration,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($duration) reached") },
    catchOnly: KClass<out Throwable>,
    dontCatch: Set<KClass<out Throwable>> = emptySet()
) = retry(duration, lazyException, catchOnly.asSingleSet(), dontCatch)
/**
 * Retries the execution of an action within a specified duration, with the ability to define exception handling rules.
 *
 * @param duration The total time duration within which the action should be retried.
 * @param lazyException A supplier for the exception to be thrown if the retry duration is exceeded. Defaults to an `RetryLimitExceededException`.
 * @param catchOnly Specifies the type of throwable to catch and retry. Other types of exceptions will not trigger retries.
 * @param dontCatch Specifies the type of throwable to avoid catching during retries. These exceptions will immediately terminate retries.
 * @since 1.0.0
 */
fun <T> Supplier<T>.retry(
    duration: Duration,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($duration) reached") },
    catchOnly: KClass<out Throwable>,
    dontCatch: KClass<out Throwable>
) = retry(duration, lazyException, catchOnly.asSingleSet(), dontCatch.asSingleSet())
/**
 * Retries the execution of an action until the specified duration elapses or until success,
 * unless a specified exception is thrown. The conditions for retrying are configurable
 * through parameters.
 *
 * @param duration The maximum duration to keep retrying the action.
 * @param lazyException A supplier for the exception to be thrown when the retries exceed the allowed duration.
 * @param catchOnly A set of exception classes that should be caught and retried. If empty, all exceptions are caught.
 * @param dontCatch A specific exception class that should not be caught and will immediately stop the retry process if thrown.
 * @since 1.0.0
 */
fun <T> Supplier<T>.retry(
    duration: Duration,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($duration) reached") },
    catchOnly: Set<KClass<out Throwable>> = emptySet(),
    dontCatch: KClass<out Throwable>
) = retry(duration, lazyException, catchOnly, dontCatch.asSingleSet())

/**
 * Retries the execution of an action a specified number of times, handling specific exceptions.
 *
 * This method allows retrying the execution of the given action up to the provided number of times.
 * It supports specifying certain exception types to be caught or ignored during retries and allows
 * for a custom exception to be thrown when the retry limit is exceeded.
 *
 * @param times The maximum number of retry attempts. The action is attempted once initially and
 *              retried up to `times - 1` additional times on failure.
 * @param lazyException A supplier for the exception to be thrown when the retry limit is exceeded.
 *                      The default exception is `RetryLimitExceededException` with a message
 *                      indicating the retry limit.
 * @param catchOnly A set of specific exception types to be caught during retries. If provided, only exceptions
 *                  of these types will be caught; all others will be rethrown immediately.
 *                  Defaults to an empty set, meaning all exceptions are caught by default.
 * @param dontCatch A set of specific exception types to be excluded from catching during retries. If provided,
 *                 exceptions of these types will bypass the retry mechanism and be rethrown immediately.
 *                 Defaults to an empty set.
 * @param supplier The action to be retried.
 * @since 1.0.0
 */
@Suppress("UNCHECKED_CAST")
fun <T> retry(
    times: Int,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($times) reached") },
    catchOnly: Set<KClass<out Throwable>> = emptySet(),
    dontCatch: Set<KClass<out Throwable>> = emptySet(),
    supplier: Supplier<T>
) = supplier.retry(times, lazyException, catchOnly, dontCatch)
/**
 * Retries the execution of an action a specified number of times with customizable exception handling.
 *
 * This method enables retrying an action multiple times in case of failures. It provides options
 * to specify the number of retry attempts, exceptions to catch or avoid, and a custom exception
 * supplier to be thrown when the retry limit is reached.
 *
 * @param times The maximum number of retry attempts allowed. Must be greater than or equal to 1.
 * @param lazyException A supplier that provides the exception to be thrown when the retry limit
 *                      is exceeded. Defaults to `RetryLimitExceededException` with a message
 *                      including the limit.
 * @param catchOnly A class type or set of throwable classes specifying the types of exception
 *                  that should trigger a retry. If empty, retries on all exceptions by default.
 * @param dontCatch A set of throwable class types that should explicitly not trigger a retry, even
 *                 if they are included in `catchOnly`.
 * @param supplier The action to be retried.
 * @since 1.0.0
 */
fun <T> retry(
    times: Int,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($times) reached") },
    catchOnly: KClass<out Throwable>,
    dontCatch: Set<KClass<out Throwable>> = emptySet(),
    supplier: Supplier<T>
) = supplier.retry(times, lazyException, catchOnly.asSingleSet(), dontCatch)
/**
 * Executes the action with a retry mechanism, allowing for a specified number of attempts and configurable
 * exception handling.
 *
 * The method retries the action up to the specified number of times (`times`). If the action does not
 * succeed within the allowed attempts, the specified exception (`lazyException`) will be thrown.
 * The retry behavior can be customized to catch only specific exceptions or exclude certain exceptions from
 * being caught during retries.
 *
 * @param times The number of retry attempts to be made before throwing the `lazyException`.
 * @param lazyException A supplier for the exception to be thrown when the retry limit is exceeded.
 *                       Defaults to a `RetryLimitExceededException` with a message mentioning the retry limit.
 * @param catchOnly A class representing an exception type that should be caught and retried during execution.
 *                  Exceptions that are instances of the specified class will trigger a retry. If empty, retries
 *                  will not be filtered by this condition.
 * @param dontCatch A class representing an exception type that should not be caught during execution. If an
 *                  exception of this type is thrown, it will not trigger a retry and will be propagated
 *                  immediately. If empty, retries will not consider this condition.
 * @param supplier The action to be retried.
 * @since 1.0.0
 */
fun <T> retry(
    times: Int,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($times) reached") },
    catchOnly: KClass<out Throwable>,
    dontCatch: KClass<out Throwable>,
    supplier: Supplier<T>
) = supplier.retry(times, lazyException, catchOnly.asSingleSet(), dontCatch.asSingleSet())
/**
 * Repeatedly attempts to invoke an action with specified retry conditions.
 *
 * This function executes the given action up to the specified number of retries (`times`)
 * and stops when the action completes successfully or when the retry limit is reached.
 * It provides options to handle specific exceptions during retries based on the
 * provided `catchOnly` and `dontCatch` parameters. If the action fails after the maximum
 * retries, a custom or default exception is thrown as specified by the `lazyException`.
 *
 * @param times The number of retry attempts allowed before the operation is considered failed.
 * @param lazyException A supplier for the exception to be thrown if the retry limit is exceeded.
 * By default, it throws a `RetryLimitExceededException` with a message indicating the retry limit.
 * @param catchOnly A set of exception types to exclusively catch during retries.
 * Retries will only occur if the thrown exception matches one of these types.
 * By default, all exceptions are caught unless restricted by `dontCatch`.
 * @param dontCatch A specific exception type that will not be caught during retries.
 * If this exception type is thrown, retries are aborted immediately, and the exception propagates.
 * @param supplier The action to be retried.
 * @since 1.0.0
 */
fun <T> retry(
    times: Int,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($times) reached") },
    catchOnly: Set<KClass<out Throwable>> = emptySet(),
    dontCatch: KClass<out Throwable>,
    supplier: Supplier<T>
) = supplier.retry(times, lazyException, catchOnly, dontCatch.asSingleSet())
/**
 * Retries the execution of a supplier function until it succeeds,
 * or the specified duration is exceeded. Provides configuration to
 * handle specific exceptions to catch or not catch during retries.
 *
 * @param duration The maximum duration for retrying the supplier function.
 * @param lazyException A supplier for the exception to be thrown when retries exceed the duration.
 * Defaults to throwing an `RetryLimitExceededException` with a descriptive message.
 * @param catchOnly A set of exception classes to catch exclusively during retries.
 * If not provided, all exceptions are considered catchable unless specified by `dontCatch`.
 * @param dontCatch A set of exception classes to exclude from catching during retries.
 * If not provided, exceptions are handled according to `catchOnly` or default behavior.
 * @param supplier The action to be retried.
 * @since 1.0.0
 */
@OptIn(RiskyApproximationOfTemporal::class)
@Suppress("UNCHECKED_CAST")
fun <T> retry(
    duration: Duration,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($duration) reached") },
    catchOnly: Set<KClass<out Throwable>> = emptySet(),
    dontCatch: Set<KClass<out Throwable>> = emptySet(),
    supplier: Supplier<T>
) = supplier.retry(duration, lazyException, catchOnly, dontCatch)
/**
 * Retries the execution of the action until the specified duration is exhausted.
 *
 * @param duration The total duration to allow retries before timing out.
 * @param lazyException A supplier for the exception to throw when the retry limit is reached.
 *                       Defaults to an `RetryLimitExceededException` with a message indicating the retry duration.
 * @param catchOnly A specific type of throwable to catch and retry on.
 * @param dontCatch A set of throwable types that should not be caught and retried. Defaults to an empty set.
 * @param supplier The action to be retried.
 * @since 1.0.0
 */
fun <T> retry(
    duration: Duration,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($duration) reached") },
    catchOnly: KClass<out Throwable>,
    dontCatch: Set<KClass<out Throwable>> = emptySet(),
    supplier: Supplier<T>
) = supplier.retry(duration, lazyException, catchOnly.asSingleSet(), dontCatch)
/**
 * Retries the execution of an action within a specified duration, with the ability to define exception handling rules.
 *
 * @param duration The total time duration within which the action should be retried.
 * @param lazyException A supplier for the exception to be thrown if the retry duration is exceeded. Defaults to an `RetryLimitExceededException`.
 * @param catchOnly Specifies the type of throwable to catch and retry. Other types of exceptions will not trigger retries.
 * @param dontCatch Specifies the type of throwable to avoid catching during retries. These exceptions will immediately terminate retries.
 * @param supplier The action to be retried.
 * @since 1.0.0
 */
fun <T> retry(
    duration: Duration,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($duration) reached") },
    catchOnly: KClass<out Throwable>,
    dontCatch: KClass<out Throwable>,
    supplier: Supplier<T>
) = supplier.retry(duration, lazyException, catchOnly.asSingleSet(), dontCatch.asSingleSet())
/**
 * Retries the execution of an action until the specified duration elapses or until success,
 * unless a specified exception is thrown. The conditions for retrying are configurable
 * through parameters.
 *
 * @param duration The maximum duration to keep retrying the action.
 * @param lazyException A supplier for the exception to be thrown when the retries exceed the allowed duration.
 * @param catchOnly A set of exception classes that should be caught and retried. If empty, all exceptions are caught.
 * @param dontCatch A specific exception class that should not be caught and will immediately stop the retry process if thrown.
 * @param supplier The action to be retried.
 * @since 1.0.0
 */
fun <T> retry(
    duration: Duration,
    lazyException: ThrowableSupplier = { RetryLimitExceededException("Limit of retries ($duration) reached") },
    catchOnly: Set<KClass<out Throwable>> = emptySet(),
    dontCatch: KClass<out Throwable>,
    supplier: Supplier<T>
) = supplier.retry(duration, lazyException, catchOnly, dontCatch.asSingleSet())