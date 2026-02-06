package dev.tommasop1804.kutils.classes.collections

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.coding.JSON.Companion.asList
import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.node.ObjectNode

/**
 * Represents a persistent, immutable data structure similar to a linked list, where each element
 * is stored as a pair of the current value and a reference to the rest of the list.
 *
 * Instances of this class are composed of a `head` containing the current element, and a `tail`
 * pointing to the remainder of the list. It provides various operations for list manipulation
 * while preserving immutability.
 *
 * Primary fields:
 * - `pair`: Stores the head and tail of the list.
 * - `head`: The first element of the list.
 * - `tail`: The remainder of the list after the head.
 * - `first`: A reference to the first element in the list.
 * - `firstValue`: The value contained within the first element.
 * - `last`: A reference to the last element in the list.
 * - `lastValue`: The value contained within the last element.
 * - `size`: The total number of elements in the list.
 *
 * @param T The type of elements stored in the `ConsList`.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli, Roberto Pavia
 */
@JvmInline
@Suppress("unused")
@JsonSerialize(using = ConsList.Companion.Serializer::class)
@JsonDeserialize(using = ConsList.Companion.Deserializer::class)
value class ConsList<T>(private val pair: Pair<T, ConsList<T>?>?) : Collection<T> {
    /**
     * Retrieves the first element of the list if it is not empty; otherwise, throws an `IndexOutOfBoundsException`.
     *
     * This property provides access to the head of the list, represented by the first element of the `pair` field.
     * If the list is empty (`pair` is `null`), accessing this property will result in an exception.
     *
     * @throws IndexOutOfBoundsException if the list is empty.
     * @return The first element of the list if available.
     * @since 1.0.0
     */
    val head: T get() = pair?.first ?: throw IndexOutOfBoundsException("List is empty")
    /**
     * Retrieves the tail of the current list, which is the remaining part
     * of the list excluding the first element. If the list is empty, attempting
     * to access the tail will result in an IndexOutOfBoundsException.
     *
     * @throws IndexOutOfBoundsException if the list is empty.
     * @return the tail of the list as a `ConsList` or `null` if the tail is not present.
     * @since 1.0.0
     */
    val tail: ConsList<T>? get() = (pair ?: throw IndexOutOfBoundsException("List is empty")).second

    /**
     * Returns the current instance if the `pair` property is non-null, otherwise throws an
     * `IndexOutOfBoundsException` with the message "List is empty".
     *
     * This property is designed to provide safe access to the `pair` inside the `ConsList` class,
     * ensuring that nullability checks are enforced at runtime.
     * @since 1.0.0
     */
    val first get() = apply { pair.isNotNull() || throw IndexOutOfBoundsException("List is empty") }
    /**
     * Provides the first element of the `ConsList` if it exists, or throws an
     * `IndexOutOfBoundsException` if the list is empty.
     *
     * This property retrieves the first component of the `pair` field, which represents
     * the underlying data structure of the `ConsList`. It ensures that the list is not
     * empty before accessing the first element.
     * @since 1.0.0
     **/
    val firstValue get() = (pair ?: throw IndexOutOfBoundsException("List is empty")).first
    /**
     * Retrieves the last element in the list.
     *
     * This property traverses the list from the head to find the last element.
     * If the list is empty, an `IndexOutOfBoundsException` is thrown. Otherwise,
     * it iterates through the list until the last non-null element is reached and returns it.
     *
     * @throws IndexOutOfBoundsException if the list is empty.
     * @return The last element in the list.
     * @since 1.0.0
     */
    val last: ConsList<T> get() = compute {
        if (pair.isNull())
            throw IndexOutOfBoundsException("List is empty")
        var list = this
        while (list.tail.isNotNull())
            list = list.tail!!
        list
    }
    /**
     * Represents the last value in the `ConsList`.
     * This property retrieves the head element of the `last` reference,
     * which typically points to the final node in the structure.
     *
     * @since 1.0.0
     */
    val lastValue get() = last.head

    /**
     * Retrieves the total number of elements in the ConsList*/
    override val size get() = computeSize()

    /**
     * Secondary constructor that initializes a new instance of the ConsList class with a `null` parameter.
     * Delegates to the primary constructor to set up the instance.
     *
     * @since 1.0.0
     */
    constructor() : this(null)
    /**
     * Creates a new instance of `ConsList` using the provided value as the first element
     * and null as the second element in the pair, representing an empty tail.
     *
     * @param value The value to be stored in the head of the list.
     * @since 1.0.0
     */
    constructor(value: T) : this(Pair(value, null))
    /**
     * Constructs a new `ConsList` instance by populating it with the elements provided in the `values` vararg parameter.
     * The elements are added in reversed order, starting from the last element in the `values` array.
     *
     * This constructor uses the `compute` utility function to initialize the underlying pair representation of the list.
     *
     * @param values*/
    constructor(vararg values: T) : this(compute {
        var list = ConsList<T>()
        for (i in values.lastIndex downTo 0)
            list = ConsList(values[i] to list)
        list.pair
    })
    /**
     * Constructs a new `ConsList` instance from the given list of values.
     *
     * This constructor creates a cons list by iterating over the provided list of values
     * in reverse order and recursively constructing pairs of elements. It uses the `compute`
     * function to initialize the cons list by wrapping the resulting pairs.
     *
     * @param values The list of values to initialize the cons list with. The values are
     *               traversed from the last element to the first to construct the cons list
     *               in the correct order.
     * @since 1.0.0
     */
    constructor(values: List<T>) : this(compute {
        var list = ConsList<T>()
        for (i in values.lastIndex downTo 0)
            list = ConsList(values[i] to list)
        list.pair
    })

    companion object {
        /**
         * Prepends the current element to the beginning of the given cons list, creating a new cons list.
         *
         * @param fList The cons list to which the current element should be prepended.
         * @return A new cons list with the current element as the head and the provided list as the tail.
         * @since 1.0.0
         */
        infix fun <T> T.cons(fList: ConsList<T>): ConsList<T> = ConsList(this to fList)
        /**
         * Creates a new `ConsList` by adding the specified value to the current element,
         * effectively creating a pair of the current element and the value.
         *
         * @param value The value to pair with the current element in the resulting `ConsList`.
         * @return A new `ConsList` containing the current element followed*/
        infix fun <T> T.cons(value: T): ConsList<T> = ConsList(this to ConsList(value to null))

        private tailrec fun <T> containsRecursive(list: ConsList<T>?, element: T): Boolean {
            if (list.isNull() || list.isEmpty()) return false
            if (list.head == element) return true
            return containsRecursive(list.tail, element)
        }

        private tailrec fun <T> forEachRecursive(list: ConsList<T>?, action: Consumer<T>) {
            if (list.isNotNull() && !list.isEmpty()) {
                action(list.head)
                forEachRecursive(list.tail, action)
            }
        }

        private tailrec fun <T> reverseRecursive(current: ConsList<T>?, acc: ConsList<T>): ConsList<T> {
            return if (current.isNull() || current.isEmpty()) acc
            else reverseRecursive(current.tail, current.head cons acc)
        }

        private tailrec fun <T, R> mapRecursive(current: ConsList<T>?, acc: ConsList<R>, transform: Transformer<T, R>): ConsList<R> {
            println("<>")
            return if (current.isNull() || current.isEmpty()) acc
            else mapRecursive(current.tail, transform(current.head) cons acc, transform)
        }

        private tailrec fun <T> filterRecursive(current: ConsList<T>?, acc: ConsList<T>, predicate: Predicate<T>): ConsList<T> {
            if (current.isNull() || current.isEmpty()) return acc

            val newAcc = if (predicate(current.head)) (current.head cons acc) else acc
            return filterRecursive(current.tail, newAcc, predicate)
        }

        private tailrec fun <T, R> foldRecursive(list: ConsList<T>?, acc: R, operation: BiTransformer<R, T, R>): R {
            if (list.isNull() || list.isEmpty()) return acc
            val nextAcc = operation(acc, list.head)
            return foldRecursive(list.tail, nextAcc, operation)
        }

        class Serializer : ValueSerializer<ConsList<*>>() {
            override fun serialize(value: ConsList<*>, gen: JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartArray()
                value.forEach { gen.writeString(it.toString()) }
                gen.writeEndArray()
            }
        }

        class Deserializer : ValueDeserializer<ConsList<*>>() {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ConsList<*> {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                @Suppress("kutils_collection_declaration")
                return ConsList(node.asList<Any>())
            }
        }
    }

    /**
     * Checks whether the encapsulated pair is null or consists entirely of null elements.
     *
     * This method evaluates if the `pair` field is null, or if both its `first` and `second` components are null.
     * It is used to determine emptiness or null-related conditions for the pair structure.
     *
     * @return `true` if the `pair` is null or both `pair.first` and `pair.second` are null, `false` otherwise.
     * @since 1.0.0
     */
    fun isNullOrEmpty() = pair.isNull() || pair.first.isNull() && pair.second.isNull()

    /**
     * Checks if the list is neither null nor empty.
     *
     * This method evaluates whether the list contains at least one element.
     * It internally relies on the `isNullOrEmpty()` method to determine
     * the state of the list.
     *
     * @return `true` if the list is not null and has at least one element,
     *         `false` otherwise.
     * @since 1.0.0
     */
    fun isNotNullOrEmpty() = !isNullOrEmpty()

    /**
     * Converts the object into its string representation, formatted accordingly.
     *
     * @param first Indicates whether the representation should start with an opening bracket.
     * @return The string representation of the object. If the pair is null, returns "[]".
     *         Otherwise, constructs the string based on the `first` parameter and pair values.
     * @since 1.0.0
     */
    fun toString(first: Boolean, builder: StringBuilder = StringBuilder(String.EMPTY)): StringBuilder {
        with(builder) {
            if (pair.isNull())
                return if (first) append("[]") else {
                    builder.setLength(builder.length - 2)
                    append("]")
                }
            if (first) append("[") else append(String.EMPTY)
            append(pair.first.toString())
            if (pair.second.isNotNull()) {
                append(", ")
                pair.second!!.toString(false, builder)
            } else append("]")
        }
        return builder
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string that provides a textual representation of the object.
     * @since 1.0.0
     */
    override fun toString(): String = toString(true).toString()

    /**
     * Checks if the list is empty.
     *
     * This method determines whether the encapsulated `pair` is null.
     * An empty list is represented by a null value for the `pair`.
     *
     * @return `true` if the list is empty, `false` otherwise.
     * @since 1.0.0
     */
    override fun isEmpty() = pair.isNull()
    /**
     * Checks whether a collection, string, or other entity is not empty.
     * This method returns the negated result of the `isEmpty()` check.
     *
     * @return `true` if the entity is not empty, `false` otherwise.
     * @since 1.0.0
     */
    fun isNotEmpty() = !isEmpty()

    /**
     * Retrieves the element located at the specified index, starting from the given startIndex.
     *
     * @param index The target index of the element to retrieve. Must be greater than or equal to startIndex and non-negative.
     * @param startIndex The starting index for retrieval. Defaults to 0 if not specified.
     * @return The element of type T at the specified index.
     * @throws IndexOutOfBoundsException If the list is empty or the index is out of bounds.
     * @since 1.0.0
     */
    operator fun get(index: Int, startIndex: Int = 0): T =
        if (pair.isNull()) throw IndexOutOfBoundsException("List is empty")
        else if (index < startIndex || index < 0) throw IndexOutOfBoundsException("Index $index out ouf bounds due to startIndex limitation")
        else {
            if (index == startIndex) pair.first
            else if (pair.second.isNull()) throw IndexOutOfBoundsException("Index $index out ouf bounds")
            else pair.second!![index, startIndex + 1]
        }

    /**
     * Determines whether the specified element is contained within the list.
     *
     * Iterates through the structure to find if the given element exists in the list.
     *
     * @param element The element to search for within the list.
     * @return `true` if the element is found in the list, `false` otherwise.
     * @since 1.0.0
     */
    override operator fun contains(element: T): Boolean = containsRecursive(this, element)

    /**
     * Checks if the list contains all elements from the*/
    override fun containsAll(elements: Collection<T>): Boolean =
        elements.all { it in this }

    /**
     * Adds an element to the beginning of the current list, creating a new list.
     *
     * This function appends the given value to the head of the list, reversing
     * the current list and reconstructing it with the new value prepended.
     *
     * @param value The value to be added to the beginning of the list.
     * @return A new `ConsList` instance with the specified value prepended
     *         to the current list.
     * @since 1.0.0
     */
    operator fun plus(value: T): ConsList<T> {
        return if (isEmpty()) ConsList(value)
        else head cons (tail?.plus(value) ?: ConsList(value))
    }
    /**
     * Combines the current `ConsList` with another `ConsList` by appending the elements of the other list
     * to the end of this list, while preserving the original order of elements.
     *
     * The operation ensures that if either list is empty, the other list is returned as is.
     *
     * @param other The `ConsList` to be appended to this list.
     * @return A new `ConsList` that is the result of appending the elements of the other list to this list.
     * @since 1.0.0
     */
    operator fun plus(other: ConsList<T>): ConsList<T> {
        if (isEmpty()) return other
        if (other.isEmpty()) return this
        return head cons ((tail ?: ConsList()) + other)
    }

    /**
     * Converts the elements of the current collection-like structure into a new list.
     *
     * @return A newly created list containing all the elements of the current collection-like structure.
     * @since 1.0.0
     */
    fun toList(): List<T> {
        if (isEmpty()) return emptyList()
        val list = ArrayList<T>()
        forEach { list.add(it) }
        return list
    }

    /**
     * Converts the current collection to an `MList`.
     *
     * Iterates over the elements of the collection and adds them to a new `MList` instance.
     *
     * @return A newly created `MList` containing all elements of the current collection.
     * @since 1.0.0
     */
    fun toMList(): MList<T> {
        val list = mListOf<T>()
        forEach { list.add(it) }
        return list
    }

    /**
     * Converts the current collection to a [Set].
     *
     * @return A new [Set] containing all distinct elements from the collection.
     * @since 1.0.0
     */
    fun toSet(): Set<T> {
        val set = HashSet<T>()
        forEach { set.add(it) }
        return set
    }

    /**
     * Converts the current collection into a mutable set (MSet), preserving all unique elements.
     *
     * @return A mutable set (MSet) containing all unique elements present in the current collection.
     * @since 1.0.0
     */
    fun toMSet(): MSet<T> {
        val set = mSetOf<T>()
        forEach { set.add(it) }
        return set
    }

    /**
     * Provides an iterator to traverse through the elements of the collection.
     *
     * @return an [Iterator] that allows iteration over the elements of the collection.
     * @since 1.0.0
     */
    override operator fun iterator(): Iterator<T> = object : Iterator<T> {
        /**
         * Represents the current node in the iteration over a `ConsList`.
         * This variable holds the ongoing reference to the current position
         * in the linked list structure during traversal.
         * @since 1.0.0
         */
        var current: ConsList<T>? = this@ConsList
        
        /**
         * Checks if there are more elements in the collection to iterate over.
         *
         * This method determines if the current element is not null and if the collection
         * is not empty, thereby indicating whether further iteration is possible.
         *
         * @return `true` if there are more elements to iterate over, `false` otherwise.
         * @since 1.0.0
         */
        override fun hasNext(): Boolean = current.isNotNull() && !current!!.isEmpty()

        /**
         * Returns the next element in the iteration.
         *
         * This method retrieves the current element referred to by the iterator and advances the iterator
         * to the next element. If no elements remain in the iteration, a `NoSuchElementException` is thrown.
         *
         * @return The next element of type `T`.
         * @since 1.0.0
         **/
        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            val value = current!!.head
            current = current!!.tail
            return value
        }
    }

    /**
     * Iterates over each element in the list and applies the given action to it.
     *
     * @param action A lambda function to be executed for each element in the list.
     *               The current element is passed as an argument to the lambda.
     * @since 1.0.0
     */
    fun forEach(action: Consumer<T>) {
        forEachRecursive(this, action)
    }

    /**
     * Computes the size of the current `ConsList` by counting its elements.
     *
     * This method iterates through the list and increments a counter for each 
     * non-empty element until the end of the list is reached.
     *
     * @return The total number of elements in the `ConsList`.
     * @since 1.0.0
     */
    private fun computeSize(): Int {
        var count = 0
        var current: ConsList<T>? = this
        while (current.isNotNull() && !current.isEmpty()) {
            count++
            current = current.tail
        }
        return count
    }

    /**
     * Reverses the order of elements in a cons list.
     *
     * Iterates through the current cons list and constructs a new cons list with elements in reverse order.
     *
     * @return A new cons list containing the elements of the original cons list in reversed order.
     * @since 1.0.0
     */
    fun reversed() = reverseRecursive(this, ConsList())

    /**
     * Transforms each element of the current list into a new value using the provided transformer function 
     * and returns a new `ConsList` containing the transformed elements.
     *
     * This method reverses the current list, applies the transformation to each element in the reversed order, 
     * and constructs a new `ConsList` with the transformed elements.
     *
     * @param transform The transformer function to be applied to each element of the list. 
     *                  The function takes an element of type `T` and returns a value of type `R`.
     * @return A new `ConsList` containing the transformed elements.
     * @since 1.0.0
     */
    fun <R> map(transform: Transformer<T, R>) = mapRecursive(this, ConsList(), transform).reversed()

    /**
     * Filters the elements of the list based on the given predicate.
     *
     * This function iterates through the elements of the list in reverse order, 
     * applying the provided predicate to each element. If the predicate evaluates 
     * to `true`, the element is included in the resulting list. The filtered results 
     * are then returned as a new `ConsList` instance.
     *
     * @param predicate A lambda or function that takes an element of type T as input 
     *                  and returns `true` if the element should be included in the 
     *                  resulting list, or `false` otherwise.
     * @return A new `ConsList` containing only the elements that satisfy the 
     *         specified predicate.
     * @since 1.0.0
     */
    fun filter(predicate: Predicate<T>) = filterRecursive(this, ConsList(), predicate).reversed()

    /**
     * Combines all elements of the list into a single result using the specified initial value 
     * and a binary operation applied to each element.
     *
     * This method iteratively processes each element of the list, accumulating the result of the 
     * specified operation starting with the provided initial value. If the list is empty, the 
     * initial value is returned.
     *
     * @param R The type of the accumulated value and the return type of the folding operation.
     * @param initial The initial value to start the accumulation.
     * @param operation A binary operation that accepts the current accumulator value and 
     *        the current list element, and produces the next accumulator value.
     * @return The accumulated result after applying the given operation to all elements of the list, 
     *         starting from the initial value.
     * @since 1.0.0
     */
    fun <R> fold(initial: R, operation: BiTransformer<R, T, R>) = foldRecursive(this, initial, operation)

    /**
     * Returns a new ConsList containing up to the first `n` elements from this list.
     * If `n` is less than or equal to 0, an empty list is returned.
     * If the list has fewer elements than `n`, all elements of the list are included in the result.
     *
     * @param n The maximum number of elements to take from this list. Must be a non-negative integer.
     * @return A new ConsList containing at most `n` elements from the start of this list.
     * @since 1.0.0
     */
    fun take(n: Int): ConsList<T> {
        if (n <= 0 || isEmpty()) return ConsList()
        return head cons (tail?.take(n - 1) ?: ConsList())
    }

    /**
     * Returns a new list with the first `n` elements dropped from the original list.
     * If `n` is greater than or equal to the size of the list, an empty list is returned.
     *
     * @param n The number of elements to drop from the beginning of the list.
     * @return A new list with the first `n` elements removed.
     * @since 1.0.0
     */
    fun drop(n: Int): ConsList<T> {
        if (n <= 0) return this
        if (isEmpty()) return ConsList()
        return tail?.drop(n - 1) ?: ConsList()
    }

    /**
     * Returns a new list with the last `n` elements dropped.
     * If `n` is less than or equal to 0, the original list is returned.
     * If the list is empty or `n` is greater than or equal to the size of the list,
     * an empty list is returned.
     *
     * @param n The number of elements to drop from the end of the list.
     * @return A new list with the last `n` elements removed.
     * @since 1.0.0
     */
    fun dropLast(n: Int): ConsList<T> {
        if (n <= 0) return this
        val length = size
        if (n >= length) return ConsList()
        return take(length - n)
    }
}