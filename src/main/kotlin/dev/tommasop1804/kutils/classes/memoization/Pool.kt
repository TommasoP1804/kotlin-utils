package dev.tommasop1804.kutils.classes.memoization

import dev.tommasop1804.kutils.Consumer
import dev.tommasop1804.kutils.Supplier
import dev.tommasop1804.kutils.Transformer
import dev.tommasop1804.kutils.emptyMSet
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Represents a generic object pool with a maximum size. Objects in the pool can be acquired, used, and released,
 * facilitating efficient reuse of resources while maintaining thread safety.
 *
 * @param T The type of objects managed by this pool.
 * @property maxSize The maximum number of objects that can be in the pool at any given time.
 * @param factory A supplier function to create new instances of the object type when the pool is not full.
 * @param reset An optional consumer function to reset an object state before it is returned to the pool.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "kutils_collection_declaration")
class Pool<T>(
    val maxSize: Int,
    private val factory: Supplier<T>,
    private val reset: Consumer<T> = {}
) {
    /**
     * A deque that maintains objects available for reuse in the pool.
     *
     * This collection holds instances that have been initialized and released back to the pool,
     * allowing them to be reused instead of creating new ones. Objects are added back to this deque
     * during a `release` operation and retrieved during an `acquire` operation.
     *
     * The capacity of this deque is implicitly limited by the `maxSize` of the pool, ensuring that no
     * more than `maxSize` objects are held across both `available` and `inUse` collections.
     *
     * This is an internal property and is guarded by a lock in all access points to ensure thread safety.
     *
     * @since 1.0.0
     */
    private val available = ArrayDeque<T>()
    
    /**
     * A mutable set representing the objects currently in use within the pool.
     *
     * This property tracks the elements that have been acquired and are not yet released back to the pool.
     * It is managed in a thread-safe manner by acquiring a lock before any operation involving it.
     *
     * @since 1.0.0
     */
    private val inUse = emptyMSet<T>()
    
    /**
     * A `ReentrantLock` instance used to synchronize access to shared resources within the `Pool` class.
     * It ensures thread safety by allowing only one thread at a time to execute critical sections
     * of code that involve access to or modification of the `available` and `inUse` collections.
     *
     * The lock is utilized in various methods, such as `acquire`, `release`, and `clear`, to guarantee
     * consistency and prevent race conditions when managing the pool's state.
     *
     * @since 1.0.0
     */
    private val lock = ReentrantLock()
    
    /**
     * Retrieves the count of objects currently available in the pool.
     *
     * This variable represents the size of the internal pool storage for reusable objects.
     * Modifications to the `available` objects are synchronized using a lock to ensure thread-safety.
     *
     * @return The number of objects currently available for reuse in the pool.
     * @since 1.0.0
     */
    val availableCount
        get() = lock.withLock { available.size }
    
    /**
     * Represents the current count of objects in use within the object pool.
     *
     * This value is dynamically computed by acquiring a lock and determining
     * the size of the internal set holding the objects that are currently in use.
     * It reflects the number of objects that have been acquired from the pool but
     * not yet released back to it.
     *
     * @since 1.0.0
     */
    val inUseCount
        get() = lock.withLock { inUse.size }
    
    /**
     * Acquires and returns an instance of type `T` from the pool. If an available object exists in the pool, it is removed
     * from the available objects and returned. Otherwise, a new object is created using the factory if the current number
     * of objects in use is less than the maximum pool size. If the pool is exhausted and no new objects can be created,
     * a [PoolExaustedException] is thrown.
     *
     * @return an instance of type `T` from the pool.
     * @throws PoolExaustedException if the pool is exhausted and no more objects can be created.
     * @since 1.0.0
     */
    fun acquire(): T = lock.withLock {
        val obj = if (available.isNotEmpty()) 
            available.removeFirst() 
        else if (inUse.size < maxSize) factory()
        else throw PoolExaustedException("The max size of this pool is ${maxSize}.")
        inUse += obj
        obj
    }
    
    /**
     * Releases an object back to the pool, making it available for reuse.
     * If the object is currently in use, it will be removed from the in-use set, reset, and added to the pool's available objects queue.
     *
     * @param obj The object to be released back into the pool.
     * @since 1.0.0
     */
    infix fun release(obj: T) = lock.withLock {
        if (inUse.remove(obj)) {
            reset(obj)
            available.addLast(obj)
        }
    }
    
    /**
     * Acquires an object from the pool, applies the provided function to it, and then releases the object
     * back to the pool. Ensures that the acquired object is always released, even if the function throws an exception.
     *
     * @param block The function to execute using the acquired object. It must take an object of type T and return a value of type R.
     * @return The result of executing the provided function.
     * @throws PoolExaustedException if no object is available in the pool and the maximum pool size is reached.
     * @since 1.0.0
     */
    fun <R> use(block: Transformer<T, R>): R {
        val obj = acquire()
        return try { block(obj) } finally { release(obj) }
    }
    
    /**
     * Clears all objects from the pool, removing them from both the "available" and "in-use" collections.
     *
     * This method ensures that the internal state of the pool is emptied and resets it to an initial state.
     * It is protected by a lock to guarantee thread-safety.
     *
     * @since 1.0.0
     */
    fun clear() = lock.withLock {
        available.clear()
        inUse.clear()
    }
    
    /**
     * An exception that is thrown when an attempt to acquire a resource from a pool exceeds its maximum size.
     *
     * This exception is typically used in the context of resource pooling mechanisms where a maximum pool size
     * is defined, and no additional resources can be allocated beyond this limit.
     *
     * @constructor Creates a new instance of PoolExaustedException.
     * @since 1.0.0
     * @author Tommaso Pastorelli
     */
    @Suppress("unused")
    open class PoolExaustedException : IllegalStateException {
        /**
         * Default constructor for the PoolExaustedException class.
         * Initializes a new instance with no specific message or cause.
         *
         * @since 1.0.0
         */
        constructor() : super()
        /**
         * Constructs a new instance of the `PoolExaustedException` class with a specified detail message.
         *
         * @param message the detail message that provides more information about the exception.
         * @since 1.0.0
         */
        constructor(message: String) : super(message)
        /**
         * Creates a new exception instance with the specified cause.
         *
         * @param cause The underlying cause of this exception.
         * @since 1.0.0
         */
        constructor(cause: Throwable) : super(cause)
        /**
         * Constructs a new exception with the specified detail message and cause.
         *
         * @param message The detail message, saved for later retrieval by the `message` property.
         * @param cause The cause of the exception, saved for later retrieval by the `cause` property.
         *              A null value is permitted and indicates that the cause is nonexistent or unknown.
         * @since 1.0.0
         */
        constructor(message: String, cause: Throwable) : super(message, cause)
    }
}