@file:JvmName("AliasesKt")
@file:Suppress("unused", "kutils_collection_declaration", "kutils_map_declaration", "kutils_tuple_declaration", "java_integer_as_kotlin_int")
@file:Since("1.0.0")


package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Beta
import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.base.Base36
import dev.tommasop1804.kutils.classes.coding.JSON
import dev.tommasop1804.kutils.classes.coding.YAML
import dev.tommasop1804.kutils.classes.collections.MTable
import dev.tommasop1804.kutils.classes.collections.Table
import dev.tommasop1804.kutils.classes.colors.Color
import dev.tommasop1804.kutils.classes.constants.QuantityLevel
import dev.tommasop1804.kutils.classes.identifiers.*
import dev.tommasop1804.kutils.classes.measure.MeasureUnit
import dev.tommasop1804.kutils.classes.measure.RMeasurement
import dev.tommasop1804.kutils.classes.pagination.FilterOperator
import dev.tommasop1804.kutils.classes.tuple.Quadruple
import dev.tommasop1804.kutils.classes.tuple.Quintuple
import dev.tommasop1804.kutils.exceptions.UnsupportedJSONTypeException
import dev.tommasop1804.kutils.exceptions.UnsupportedYAMLTypeException
import java.math.BigInteger
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * A type alias representing a list containing elements of any type.
 *
 * This alias simplifies the declaration of lists that can hold elements
 * of any type, providing flexibility for generic data storage and manipulation.
 *
 * @since 1.0.0
 * @see List
 * @see Any
 */
typealias AnyList = List<Any>
/**
 * A type alias for `MList<Any?>`, representing a mutable list that can hold elements of any type, including null.
 *
 * This can be used when working with heterogeneous collections or when the element type is uncertain.
 *
 * @since 1.0.0
 * @see MutableList
 * @see Any
 */
typealias AnyMList = MList<Any>

/**
 * A type alias for `List<Int>`, representing a list containing integer values.
 *
 * This alias can enhance code readability by providing a more descriptive name
 * for lists specifically intended to store integers.
 *
 * @since 1.0.0
 * @see List
 * @see Int
 */
typealias IntList = List<Int>
/**
 * A type alias representing a mutable list of integers.
 * This alias provides a more concise and readable way to work with mutable lists of type Int.
 *
 * @since 1.0.0
 * @see MutableList
 * @see Int
 */
typealias IntMList = MutableList<Int>

/**
 * A type alias representing a list of Long values.
 * This can be used to simplify type signatures and improve code readability.
 *
 * @since 1.0.0
 * @see List
 * @see Long
 */
typealias LongList = List<Long>
/**
 * A type alias for a mutable list of long integers (`MutableList<Long>`).
 *
 * This alias can be used to provide better readability and context
 * when working with mutable lists that specifically store `Long` values.
 *
 * @since 1.0.0
 * @see MutableList
 * @see Long
 */
typealias LongMList = MutableList<Long>

/**
 * A type alias for a list of double precision floating-point numbers.
 *
 * This alias can be used to represent or handle lists specifically containing `Double` elements
 * in a more concise or semantically meaningful way.
 *
 * @since 1.0.0
 * @see List
 * @see Double
 */
typealias DoubleList = List<Double>
/**
 * A type alias representing a mutable list of doubles.
 *
 * This alias simplifies the usage of `MutableList<Double>` by
 * providing a more concise and descriptive name for it.
 *
 * It can be used to represent collections of double-precision
 * floating-point numbers that can be modified (e.g., adding, removing, or updating elements).
 *
 * @since 1.0.0
 * @see MutableList
 * @see Double
 */
typealias DoubleMList = MutableList<Double>

/**
 * A type alias for a list of strings. This alias simplifies the readability and usability
 * of code where a list of strings is expected, without the need to repeatedly use
 * the full `List<String>` type declaration.
 *
 * @since 1.0.0
 * @see List
 * @see String
 */
typealias StringList = List<String>
/**
 * A type alias for a mutable list of strings.
 *
 * This provides a more readable and concise way to refer to a `MutableList<String>`,
 * which can be used wherever a mutable list of strings is needed.
 *
 * @since 1.0.0
 * @see MutableList
 * @see String
 */
typealias StringMList = MutableList<String>

/**
 * Type alias representing a list of characters.
 *
 * This alias simplifies the usage of lists containing characters,
 * allowing for more expressive and concise code.
 *
 * @since 1.0.0
 * @see List
 * @see Char
 */
typealias CharList = List<Char>
/**
 * A typealias representing a mutable list of characters.
 *
 * This alias simplifies the use of `MutableList<Char>` by allowing the shorthand `CharMList`.
 *
 * @since 1.0.0
 * @see MutableList
 * @see Char
 */
typealias CharMList = MutableList<Char>

/**
 * Defines a type alias `UUIDList` which represents a `List` of `UUID` objects.
 *
 * This alias can be used to simplify the representation of collections that strictly
 * hold `UUID` elements, improving code readability and reducing verbosity.
 *
 * @since 1.0.0
 * @see List
 * @see UUID
 */
typealias UUIDList = List<UUID>
/**
 * A typealias representing a mutable list of UUIDs.
 *
 * This provides a shorthand for `MutableList<UUID>`
 * to improve code readability and maintainability
 * in contexts where mutable lists of UUIDs are frequently used.
 *
 * @since 1.0.0
 * @see MutableList
 * @see UUID
 */
typealias UUIDMList = MutableList<UUID>

/**
 * Typealias for a generic MutableList in Kotlin.
 *
 * This alias simplifies usage when referring to the MutableList interface.
 * It is commonly used to represent a mutable collection of elements.
 *
 * @param E the type of elements contained in the list.
 * @since 1.0.0
 * @see MutableList
 */
typealias MList<E> = MutableList<E>

/**
 * A type alias that represents a list of lists, where each inner list contains elements of the generic type [E].
 *
 * This can be used to simplify the representation of nested lists and make code more readable.
 *
 * @param E The type of elements contained in the nested lists.
 * @since 1.0.0
 */
typealias MultiList<E> = List<List<E>>
/**
 * A typealias for a mutable list of mutable lists.
 * Represents a nested list structure where each element is a mutable list.
 *
 * @param E The type of the elements in the inner mutable lists.
 * @since 1.0.0
 */
typealias MultiMList<E> = MList<MList<E>>

/**
 * A typealias for a set of elements of any type.
 *
 * This provides a shorthand for defining sets that can hold any type of objects.
 *
 * @since 1.0.0
 * @see Set
 * @see Any
 */
typealias AnySet = Set<Any>
/**
 * A type alias for the MSet collection class which allows the storage of any type of nullable objects.
 *
 * This alias provides an easier and more concise way to represent a multi-set that can store
 * elements of any type including `null`.
 *
 * @since 1.0.0
 * @see MutableSet
 * @see Any
 */
typealias AnyMSet = MSet<Any>

/**
 * A typealias for a Set of integers.
 *
 * This allows you to use the name `IntSet` as a shorthand for `Set<Int>`,
 * providing improved readability and understandability in your code when working
 * specifically with sets of integers.
 *
 * @since 1.0.0
 * @see Set
 * @see Int
 */
typealias IntSet = Set<Int>
/**
 * A typealias representing a mutable set of integers.
 *
 * This typealias is a shorthand for `MutableSet<Int>` and is used to improve code readability
 * and clarity when working specifically with sets of integers that need to be mutable.
 *
 * @since 1.0.0
 * @see MutableSet
 * @see Int
 */
typealias IntMSet = MutableSet<Int>

/**
 * A type alias for a [Set] containing [Long] elements.
 *
 * This alias simplifies the usage of sets specifically typed for long values,
 * enhancing code readability and maintainability when working with collections of Long.
 *
 * @since 1.0.0
 * @see Set
 * @see Long
 */
typealias LongSet = Set<Long>
/**
 * A type alias representing a mutable set of Long values.
 *
 * This type alias is used to simplify the representation of a `MutableSet<Long>`
 * and improve code readability.
 *
 * @since 1.0.0
 * @see MutableSet
 * @see Long
 */
typealias LongMSet = MutableSet<Long>

/**
 * A type alias for a [Set] of [Double] values.
 * This alias can be used to represent a collection of distinct [Double]s.
 *
 * @since 1.0.0
 * @see Set
 * @see Double
 */
typealias DoubleSet = Set<Double>
/**
 * A typealias for a `MutableSet` that specifically holds `Double` values.
 * It provides a shorthand notation for creating and working with mutable sets of doubles.
 *
 * @since 1.0.0
 * @see MutableSet
 * @see Double
 */
typealias DoubleMSet = MutableSet<Double>

/**
 * Type alias for a set of strings.
 * This provides a more descriptive name when handling sets containing string values.
 *
 * @since 1.0.0
 * @see Set
 * @see String
 */
typealias StringSet = Set<String>
/**
 * A type alias representing a mutable set of strings.
 * This can be used as an alternative name for `MutableSet<String>`.
 *
 * @since 1.0.0
 * @see MutableSet
 * @see String
 */
typealias StringMSet = MutableSet<String>

/**
 * A typealias for a set of characters (`Set<Char>`).
 * It provides a more readable and concise way to represent a collection of unique characters.
 *
 * @since 1.0.0
 * @see Set
 * @see Char
 */
typealias CharSet = Set<Char>
/**
 * A type alias for a mutable set of characters.
 * This can be used to provide a more descriptive or context-specific name
 * for a `MutableSet<Char>` in the codebase.
 *
 * @since 1.0.0
 * @see MutableSet
 * @see Char
 */
typealias CharMSet = MutableSet<Char>

/**
 * A type alias representing a set of universally unique identifiers (UUIDs).
 * This can be used wherever a set of UUIDs is required to enhance code readability.
 *
 * @since 1.0.0
 * @see Set
 * @see UUID
 */
typealias UUIDSet = Set<UUID>
/**
 * A typealias for a mutable set that holds UUID objects.
 *
 * This alias provides a more descriptive and concise way to refer
 * to a `MutableSet<UUID>`, often used for collections of unique
 * identifiers that can be modified.
 *
 * @since 1.0.0
 * @see MutableSet
 * @see UUID
 */
typealias UUIDMSet = MutableSet<UUID>

/**
 * A typealias for MutableSet<E>, providing a more concise name for usage in the codebase.
 *
 * @param E the type of elements contained in the set.
 * @since 1.0.0
 * @see MutableSet
 */
typealias MSet<E> = MutableSet<E>

/**
 * A type alias representing a multiset, where a multiset is a set of sets.
 *
 * This type alias can be used to define a collection of sets, where each inner set can represent
 * a distinct group of items, and the outer set maintains uniqueness for these groups of items.
 *
 * @param E The type of elements contained in the sets of the multiset.
 * @since 1.0.0
 */
typealias MultiSet<E> = Set<Set<E>>
/**
 * A type alias representing a multiset of multisets. This allows creating collections
 * where each element is itself a multiset of elements.
 *
 * @param E the type of elements contained in the innermost multiset
 *
 * @since 1.0.0
 */
typealias MultiMSet<E> = MSet<MSet<E>>

/**
 * A type alias for `Table<String, String, V>` where the row and column keys are both of type `String`.
 *
 * This alias simplifies the usage of Guava's Table interface when working with string-based row and column keys.
 *
 * @param V The type of the values stored in the table.
 * @since 1.0.0
 * @see Table
 * @see String
 */
typealias StringKeysTable<V> = Table<String, String, V>
/**
 * A typealias for a MutableTable where the keys are both of type String.
 * This provides a convenient shorthand for tables that use String as both row and column keys.
 *
 * @param V The type of the values stored in the table.
 * @since 1.0.0
 * @see MTable
 * @see String
 */
typealias StringKeysMTable<V> = MTable<String, String, V>

/**
 * A type alias for a Table with both row and column keys as Integers.
 * This alias simplifies the usage of a Table when working specifically
 * with integer keys for rows and columns.
 *
 * @param V The type of the values stored in the table.
 * @since 1.0.0
 * @see Table
 * @see Int
 */
typealias IntKeysTable<V> = Table<Int, Int, V>
/**
 * Type alias for a mutable table with integer keys for both rows and columns, and a generic value type.
 *
 * Represents a specialized form of `MutableTable` where the keys for rows and columns are restricted to `Int`
 * values, providing more type-specific operations in relevant contexts.
 *
 * @param V the type of values stored in the table
 * @since 1.0.0
 * @see MTable
 * @see Int
 */
typealias IntKeysMTable<V> = MTable<Int, Int, V>

/**
 * A typealias that represents a non-nullable map with `String` keys and values of type `Any`.
 *
 * This alias can be used to simplify the declaration and usage of maps that conform to this specification.
 *
 * @since 1.0.0
 * @see Map
 * @see String
 * @see Any
 */
typealias DataMapNN = Map<String, Any>
/**
 * A type alias representing a map with String keys and values of type Any.
 * This alias is typically used to indicate a non-nullable data mapping structure.
 *
 * @since 1.0.0
 * @see MutableMap
 * @see String
 * @see Any
 */
typealias DataMMapNN = MutableMap<String, Any>

/**
 * A type alias representing a map with string keys and nullable values of any type.
 * This can be used to simplify the usage of a common data structure pattern
 * where keys are of type `String` and the values can be of any type, including null.
 *
 * @since 1.0.0
 * @see Map
 * @see String
 * @see Any
 */
typealias DataMap = Map<String, Any?>
/**
 * Type alias for a mutable map with `String` keys and nullable `Any?` values.
 * This alias can be used to simplify and improve readability for maps frequently
 * utilized to hold various types of data.
 *
 * @since 1.0.0
 * @see MutableMap
 * @see String
 * @see Any
 */
typealias DataMMap = MutableMap<String, Any?>

/**
 * A type alias for a map that associates a `QuantityLevel` with an `Integer` value.
 *
 * This alias is often used to represent a mapping of quantity levels to their
 * corresponding integer values, which can be useful in cases such as settings,
 * thresholds, or validations.
 *
 * @since 1.0.0
 * @see Map
 * @see QuantityLevel
 * @see Int
 */
typealias QuantityMap = Map<QuantityLevel, Int>
/**
 * A typealias for a mutable map representing associations between `QuantityLevel` values
 * and corresponding integer values.
 *
 * This alias simplifies the declaration of maps used to store quantity-related data
 * where keys are instances of `QuantityLevel` and values are integers. It is particularly
 * useful in scenarios that involve tracking, managing, or evaluating quantity levels with
 * specific integer constraints.
 *
 * @since 1.0.0
 * @see MutableMap
 * @see QuantityLevel
 * @see Int
 */
typealias QuantityMMap = MutableMap<QuantityLevel, Int>

/**
 * A type alias representing a map with string keys and integer values. This can be used to store
 * counts or frequencies of occurrences associated with string keys.
 *
 * @param K The type of keys in the map, restricted to String for this alias.
 * @since 1.0.0
 * @see Map
 * @see Int
 */
typealias CountMap<K> = Map<K, Int>
/**
 * A typealias representing a mutable map where keys are strings and values are integers.
 * It is useful for scenarios where you need to track counts or frequencies of string keys.
 *
 * @param K The type of keys in the map, which must be a String.
 *
 * @since 1.0.0
 * @see MutableMap
 * @see Int
 */
typealias CountMMap<K> = MutableMap<K, Int>

/**
 * A type alias representing a map where the keys are integers and values are of a generic type.
 *
 * This can be utilized as a shorthand for maps with integer keys, commonly used for indexing elements
 * or associating data with specific integer identifiers.
 *
 * @param V The type of values associated with the integer keys in the map.
 * @since 1.0.0
 */
typealias IndexMap<V> = Map<Int, V>
/**
 * A type alias for a mutable map where the keys are integers and the values are of a generic type.
 *
 * This alias simplifies the representation and usage of a mutable map with integer keys in code.
 *
 * @param V The type of values stored in the map associated with integer keys.
 *
 * @since 1.0.0
 */
typealias IndexMMap<V> = MutableMap<Int, V>

/**
 * Type alias representing a map with String keys and String values.
 *
 * This alias simplifies the use of maps where both the keys and values are of type String.
 *
 * @since 1.0.0
 */
typealias StringMap = Map<String, String>
/**
 * A typealias for a mutable map with keys and values of type String.
 *
 * This typealias simplifies the usage of mutable maps where both the keys
 * and values are Strings, providing enhanced readability and reducing code
 * verbosity.
 *
 * @since 1.0.0
 */
typealias StringMMap = MutableMap<String, String>
/**
 * A typealias representing a map where both the keys and the values are nullable strings.
 *
 * This can be useful when working with maps where either the keys, the values, or both
 * might be `null`, allowing for flexibility in handling optional or missing entries.
 *
 * @since 1.0.0
 */
typealias NullableStringMap = Map<String, String?>
/**
 * A type alias representing a mutable map where both keys and values can be nullable strings.
 *
 * This alias is useful for simplifying code readability and maintenance when working
 * with maps containing potentially nullable string keys and values.
 *
 * @since 1.0.0
 */
typealias NullableStringMMap = MutableMap<String, String?>

/**
 * Type alias for a map where each key is associated with a list of values.
 *
 * This can be used to represent a collection of mappings where a single key is
 * associated with multiple values.
 *
 * @param K the type of keys maintained by this map
 * @param V the type of elements in the lists associated with the keys
 * @since 1.0.0
 * @see Map
 * @see List
 */
typealias MultiMap<K, V> = Map<K, List<V>>
/**
 * A type alias for a mutable multimap, which is represented as a `MutableMap` where
 * each key is associated with a `MutableList` of values. This allows storing multiple
 * values for a single key while maintaining mutability for both the map itself and
 * the lists of values it contains.
 *
 * This can be useful for grouping values under a common key, while still allowing
 * modifications to the groups or the map.
 *
 * @param K The type of keys in the map.
 * @param V The type of values in the lists.
 * @since 1.0.0
 * @see MutableMap
 * @see MutableList
 */
typealias MultiMMap<K, V> = MutableMap<K, MutableList<V>>

/**
 * A type alias for the generic `Map<K, V>` interface, allowing for more concise and readable code.
 *
 * @param K the type of keys in the map.
 * @param V the type of values in the map.
 * @since 1.0.0
 * @see MutableMap
 */
typealias MMap<K, V> = MutableMap<K, V>

/**
 * A typealias for a sequence containing elements of any type.
 *
 * This alias can be used to simplify the type declaration for sequences
 * where the element type is not restricted and can hold any type of object.
 *
 * @since 1.0.0
 */
typealias AnySequence = Sequence<Any>
/**
 * A typealias for a sequence of integers. Represents a lazily evaluated collection of integer values.
 *
 * This alias can be used to simplify code and improve readability when working with `Sequence<Int>` types.
 *
 * @since 1.0.0
 */
typealias IntSequence = Sequence<Int>
/**
 * A typealias for a sequence of Long values.
 * This is used to simplify the representation of sequences containing Long elements.
 *
 * @since 1.0.0
 */
typealias LongSequence = Sequence<Long>
/**
 * A typealias representing a sequence of Double values.
 *
 * This is useful for providing a more descriptive or domain-specific
 * naming convention for sequences that solely contain Double elements.
 *
 * @since 1.0.0
 */
typealias DoubleSequence = Sequence<Double>
/**
 * A type alias that represents a sequence of strings.
 *
 * This can be used to simplify type definitions and improve code readability
 * when working with sequences that specifically consist of string elements.
 *
 * @since 1.0.0
 */
typealias StringSequence = Sequence<String>

/**
 * A typealias representing a predicate function that takes a single parameter of type `T`
 * and returns a `Boolean` indicating whether the parameter satisfies a certain condition.
 *
 * Typically used to represent a condition or filter logic in functional programming.
 *
 * @param T the type of input the predicate operates on
 * @since 1.0.0
 */
typealias Predicate<T> = (T) -> Boolean
/**
 * A type alias representing an extension function type that acts as a predicate on the receiver object.
 * It takes a receiver of type [T] and returns a Boolean, indicating whether the receiver meets the specific condition.
 *
 * @param T the type of the receiver object
 * @since 1.0.0
 */
typealias ReceiverPredicate<T> = T.() -> Boolean
/**
 * A type alias for a function type that takes two parameters of types T1 and T2
 * and returns a Boolean result. Represents a predicate (boolean-valued function)
 * of two arguments.
 *
 * @param T1 the type of the first argument to the predicate
 * @param T2 the type of the second argument to the predicate
 *
 * @since 1.0.0
 */
typealias BiPredicate<T1, T2> = (T1, T2) -> Boolean
/**
 * A typealias representing a predicate function that operates on a receiver of type `T1`
 * and takes an additional parameter of type `T2`. It returns a Boolean value based on the logic defined.
 *
 * This can be used to define extension functions or lambda expressions where the receiver is of type `T1`.
 *
 * @param T1 The receiver type on which the predicate function is invoked.
 * @param T2 The type of the additional parameter required by the predicate function.
 * @since 1.0.0
 */
typealias ReceiverBiPredicate<T1, T2> = T1.(T2) -> Boolean

/**
 * A type alias for a single-argument function that consumes a value of type [T]
 * and performs an operation without returning any result. Typically used to represent
 * callback functions or consumers where a value is processed but not returned.
 *
 * @param T the type of the input value consumed by the function.
 * @since 1.0.0
 */
typealias Consumer<T> = (T) -> Unit
/**
 * Type alias representing a function literal with receiver. This is a functional type where
 * the receiver object of type `T` is used as the function's receiver. Allows for invoking
 * the function with the context of the receiver object.
 *
 * @param T the type of the receiver object.
 * @since 1.0.0
 */
typealias ReceiverConsumer<T> = T.() -> Unit
/**
 * A type alias for a lambda function that accepts two parameters of types T1 and T2, and returns no result.
 * This is a functional interface commonly used to represent a consumer operation that consumes
 * two input arguments without producing a result.
 *
 * @param T1 the type of the first input to the operation
 * @param T2 the type of the second input to the operation
 * @since 1.0.0
 */
typealias BiConsumer<T1, T2> = (T1, T2) -> Unit
/**
 * A typealias representing a function type with a receiver. It allows invoking a function on a receiver object of type `T1`,
 * while also passing an additional parameter of type `T2`.
 *
 * This is typically used to define operations or extensions where a receiver object is required
 * along with an additional argument.
 *
 * @param T1 The type of the receiver on which the function is invoked.
 * @param T2 The type of the additional parameter passed to the function.
 * @since 1.0.0
 */
typealias ReceiverBiConsumer<T1, T2> = T1.(T2) -> Unit
/**
 * A typealias representing a function that takes three parameters of types T1, T2, and T3, and returns no value.
 *
 * Useful for passing a lambda or function reference that performs an operation using three input arguments
 * without producing a result.
 *
 * @param T1 The type of the first input parameter.
 * @param T2 The type of the second input parameter.
 * @param T3 The type of the third input parameter.
 * @since 1.0.0
 */
typealias TriConsumer<T1, T2, T3> = (T1, T2, T3) -> Unit
/**
 * A typealias defining a function type where the first parameter `T1` acts as the receiver,
 * and it takes two additional parameters of types `T2` and `T3`. The function does not return a value.
 *
 * This is useful for creating concise and readable higher-order functions or receivers with three parameters.
 *
 * @param T1 The receiver type on which the function operates.
 * @param T2 The type of the second parameter.
 * @param T3 The type of the third parameter.
 *
 * @since 1.0.0
 */
typealias ReceiverTriConsumer<T1, T2, T3> = T1.(T2, T3) -> Unit

/**
 * Type alias representing a function that takes an input of type T and returns a result of type R.
 *
 * This provides a concise way to refer to a function type throughout the codebase,
 * improving readability and reducing verbosity.
 *
 * @param T The input type of the function.
 * @param R The return type of the function.
 * @since 1.0.0
 */
typealias Transformer<T, R> = (T) -> R
/**
 * A typealias that represents a function type where the receiver is of type [T]
 * and the function returns a value of type [R].
 *
 * This is commonly used for receiver-based lambda expressions.
 *
 * @param T the receiver type of the function
 * @param R the return type of the function
 * @since 1.0.0
 */
typealias ReceiverTransformer<T, R> = T.() -> R
/**
 * Represents a function that accepts two arguments and produces a result.
 * This type alias is a shorthand for a function type with two input parameters of types T1 and T2,
 * and a return value of type R.
 *
 * @param T1 the type of the first argument to the function
 * @param T2 the type of the second argument to the function
 * @param R the type of the result of the function
 *
 * @since 1.0.0
 */
typealias BiTransformer<T1, T2, R> = (T1, T2) -> R
/**
 * A typealias representing a function type where the first parameter (receiver) is of type `T1`,
 * the second parameter is of type `T2`, and the return type is `R`.
 *
 * This allows defining extension-like functions that take a single additional argument.
 *
 * @param T1 The type of the receiver object.
 * @param T2 The type of the second argument passed to the function.
 * @param R The return type of the function.
 * @since 1.0.0
 */
typealias ReceiverBiTransformer<T1, T2, R> = T1.(T2) -> R
/**
 * A typealias representing a function that takes three parameters of types T1, T2, and T3
 * and returns a result of type R.
 *
 * @param T1 the type of the first parameter
 * @param T2 the type of the second parameter
 * @param T3 the type of the third parameter
 * @param R the type of the result returned by the function
 * @since 1.0.0
 */
typealias TriTransformer<T1, T2, T3, R> = (T1, T2, T3) -> R
/**
 * A typealias representing a function type with a receiver of type [T1] that accepts
 * two additional parameters of types [T2] and [T3], and returns a value of type [R].
 *
 * This allows defining functions where the first parameter is treated as the receiver type,
 * enabling method-like invocation syntax.
 *
 * @param T1 the receiver type of the function
 * @param T2 the type of the first parameter
 * @param T3 the type of the second parameter
 * @param R the return type of the function
 * @since 1.0.0
 */
typealias ReceiverTriTransformer<T1, T2, T3, R> = T1.(T2, T3) -> R

/**
 * A type alias representing a supplier function that takes no arguments
 * and returns a value of type R.
 *
 * Commonly used to represent deferred or lazy value providers.
 *
 * @param R The type of the value supplied by the function.
 * @since 1.0.0
 */
typealias Supplier<R> = () -> R
/**
 * A typealias representing a supplier that provides a pair of values of types R1 and R2.
 * The supplier is implemented as a no-argument function that returns a Pair.
 *
 * @param R1 The type of the first component of the pair.
 * @param R2 The type of the second component of the pair.
 * @since 1.0.0
 */
typealias BiSupplier<R1, R2> = () -> Pair<R1, R2>
/**
 * Represents a functional type alias for a supplier that provides a triple of values
 * of types [R1], [R2], and [R3].
 *
 * This typealias simplifies the usage of a lambda function that returns a [Triple].
 *
 * @param R1 The type of the first value in the triple.
 * @param R2 The type of the second value in the triple.
 * @param R3 The type of the third value in the triple.
 * @since 1.0.0
 */
typealias TriSupplier<R1, R2, R3> = () -> Triple<R1, R2, R3>

/**
 * A type alias representing a supplier function that provides a Throwable instance.
 *
 * This can be used in scenarios where deferred creation of a Throwable is required.
 *
 * @since 1.0.0
 */
typealias ThrowableSupplier = () -> Throwable
/**
 * A typealias representing a function that takes an object as input
 * and returns a transformed `Throwable` as output. Commonly used for
 * modifying or wrapping exceptions before rethrowing or handling them.
 *
 * @since 1.0.0
 */
typealias ThrowableTransformer = (Throwable) -> Throwable

/**
 * A type alias for a function that returns a nullable [Throwable].
 *
 * This type alias is typically used when a function needs to supply
 * an exception or indicate the absence of one by returning null.
 *
 * @since 1.0.0
 */
typealias NullableThrowableSupplier = () -> Throwable?

/**
 * A typealias representing an action that is a function with no parameters and no return value.
 * It is used to encapsulate a block of executable code, typically as a callback or event handler.
 *
 * @since 1.0.0
 */
typealias Action = () -> Unit

/**
 * Represents a type alias for a [Pair] consisting of two generic [Any] types.
 *
 * This type alias can be utilized to simplify type declarations when working
 * with pairs of heterogeneous objects.
 *
 * @since 1.0.0
 * @see Pair
 * @see Any
 */
typealias Any2 = Pair<Any, Any>
/**
 * A typealias representing a pair of nullable and non-nullable values where both elements can hold `Any` type.
 *
 * The first element can be nullable (`Any?`), while the second element is non-nullable and holds `Any`.
 *
 * @since 1.0.0
 * @see Pair
 * @see Any
 */
typealias NullableAny2 = Pair<Any?, Any?>
/**
 * A type alias representing a pair of integers.
 *
 * @see Pair
 * @since 1.0.0
 * @see Pair
 * @see Int
 */
typealias Int2 = Pair<Int, Int>
/**
 * A typealias representing a pair of nullable integers.
 * It encapsulates two optional integer values in a single structure.
 *
 * @since 1.0.0
 * @see Pair
 * @see Int
 */
typealias NullableInt2 = Pair<Int?, Int?>

/**
 * A typealias for a Pair consisting of two Long values.
 * This can be used to represent a tuple or pair of Longs,
 * providing better readability and type safety in certain contexts.
 *
 * @since 1.0.0
 * @see Pair
 * @see Long
 */
typealias Long2 = Pair<Long, Long>
/**
 * A typealias definition for a pair consisting of nullable `Long` values.
 *
 * This typealias can be used to improve code readability when working with pairs of
 * nullable `Long` values.
 *
 * @since 1.0.0
 * @see Pair
 * @see Long
 */
typealias NullableLong2 = Pair<Long?, Long?>

/**
 * A type alias for a [Pair] of two [Double] values, representing a coordinate or value pair.
 *
 * @since 1.0.0
 * @see Pair
 * @see Double
 */
typealias Double2 = Pair<Double, Double>
/**
 * A typealias for a pair of nullable Double values.
 *
 * This typealias represents a convenient shorthand for a `Pair` where both elements
 * are optional `Double` values.
 *
 * @since 1.0.0
 * @see Pair
 * @see Double
 */
typealias NullableDouble2 = Pair<Double?, Double?>

/**
 * A typealias for a Pair of Booleans.
 *
 * This alias is used to represent a pair of Boolean values
 * for situations where working with a tuple structure of
 * Boolean data is necessary.
 *
 * @since 1.0.0
 * @see Pair
 * @see Boolean
 */
typealias Boolean2 = Pair<Boolean, Boolean>
/**
 * A type alias that represents a pair of nullable Boolean values.
 * It encapsulates two Boolean? (nullable Boolean) values.
 *
 * This can be used to represent states where Booleans can either be true, false, or null for cases
 * involving undefined or unknown values for each of the pair's elements.
 *
 * @since 1.0.0
 * @see Pair
 * @see Boolean
 */
typealias NullableBoolean2 = Pair<Boolean?, Boolean?>

/**
 * A type alias for a `Pair` where both elements are of type `String`.
 * This can be used to represent a pair of strings with a meaningful name, improving code readability.
 *
 * @since 1.0.0
 * @see Pair
 * @see String
 */
typealias String2 = Pair<String, String>
/**
 * A typealias for a pair consisting of two nullable strings.
 *
 * This can be used to represent a pair of optional string values
 * where each string in the pair can independently be null or non-null.
 *
 * @since 1.0.0
 * @see Pair
 * @see String
 */
typealias NullableString2 = Pair<String?, String?>

/**
 * A type alias for a pair of two elements of the same type.
 *
 * @param T the type of elements contained in the pair.
 * @since 1.0.0
 * @see Pair
 */
typealias MonoPair<T> = Pair<T, T>

/**
 * Represents a type alias for a [Triple] where each element can hold a value of any type.
 *
 * This is a shorthand to use instead of explicitly declaring
 * `Triple<Any, Any, Any>` in your code.
 *
 * Use this type alias to work with a tuple of three values of any type.
 *
 * @since 1.0.0
 * @see Triple
 * @see Any
 */
typealias Any3 = Triple<Any, Any, Any>
/**
 * A type alias representing a `Triple` where each component can be a nullable `Any` type.
 * This alias provides a more concise way to refer to a `Triple` of nullable `Any` elements.
 *
 * @since 1.0.0
 * @see Triple
 * @see Any
 */
typealias NullableAny3 = Triple<Any?, Any?, Any?>

/**
 * A typealias for `Triple<Int, Int, Int>`, which represents a group of three integers.
 *
 * This can be used to simplify the representation and readability of triples of integers.
 *
 * @since 1.0.0
 * @see Triple
 * @see Int
 */
typealias Int3 = Triple<Int, Int, Int>
/**
 * A typealias for a Triple consisting of three nullable integers.
 *
 * This typealias can be used to provide a more descriptive name for a Triple
 * where each of the three elements can be either an integer or null.
 *
 * @since 1.0.0
 * @see Triple
 * @see Int
 */
typealias NullableInt3 = Triple<Int?, Int?, Int?>

/**
 * A typealias for a `Triple` of `Long` values.
 * It represents a tuple containing three `Long` data types.
 *
 * @since 1.0.0
 * @see Triple
 * @see Long
 */
typealias Long3 = Triple<Long, Long, Long>
/**
 * A typealias for a Triple containing three nullable Long values.
 *
 * This can be useful for representing a set of three optional Long values
 * without the need for explicitly repeating the nullable Long type in the Triple.
 *
 * @since 1.0.0
 * @see Triple
 * @see Long
 */
typealias NullableLong3 = Triple<Long?, Long?, Long?>

/**
 * Type alias representing a triplet of three `Double` values.
 *
 * This alias simplifies the representation and usage of a `Triple` where
 * all three elements are of type `Double`.
 *
 * @since 1.0.0
 * @see Triple
 * @see Double
 */
typealias Double3 = Triple<Double, Double, Double>
/**
 * A type alias representing a triple of nullable Double values.
 *
 * @since 1.0.0
 * @see Triple
 * @see Double
 */
typealias NullableDouble3 = Triple<Double?, Double?, Double?>

/**
 * Represents a type alias for a Triple consisting of three Boolean values.
 * This can be used to express a combination of three Boolean states or flags.
 *
 * @since 1.0.0
 * @see Triple
 * @see Boolean
 */
typealias Boolean3 = Triple<Boolean, Boolean, Boolean>
/**
 * A typealias for a Triple containing three nullable Boolean values.
 * Represents a group of three Booleans where each can be null.
 *
 * @since 1.0.0
 * @see Triple
 * @see Boolean
 */
typealias NullableBoolean3 = Triple<Boolean?, Boolean?, Boolean?>

/**
 * A typealias representing a Triple of three Strings.
 *
 * This typealias simplifies the usage of `Triple<String, String, String>`
 * by allowing it to be referred to as `String3`.
 *
 * @since 1.0.0
 * @see Triple
 * @see String
 */
typealias String3 = Triple<String, String, String>
/**
 * A type alias for a Triple of nullable Strings.
 * This can be used to represent a group of three Strings where each one can either hold a value or be null.
 *
 * @since 1.0.0
 * @see Triple
 * @see String
 */
typealias NullableString3 = Triple<String?, String?, String?>

/**
 * Defines a type alias `FilterTriple` that represents a tuple of a string (e.g. name of the field), a filter operator,
 * and a value used for filtering or querying operations.
 *
 * This alias is utilized to associate a property, the desired filtering logic (using a
 * `FilterOperator`), and an optional filtering value into a single reusable data structure.
 * It aids in the construction of filter conditions for datasets or querying mechanisms.
 *
 * @since 1.0.0
 * @author
 * @see Triple
 * @see FilterOperator
 * @see String
 */
typealias Filter3 = Triple<String, FilterOperator, String?>

/**
 * Represents a type alias for a `Triple` where all three components are of the same type.
 *
 * This type alias can be used to simplify the definition of a `Triple` when all the elements share the same type.
 *
 * @param T The type of the three elements in the `Triple`.
 * @since 1.0.0
 * @see Triple
 */
typealias MonoTriple<T> = Triple<T, T, T>

/**
 * Type alias for a `Quadruple` where all four elements are of type `Any`.
 *
 * This alias can be used to represent a generic tuple of four values without
 * specific type constraints.
 *
 * @since 1.0.0
 * @see Quadruple
 * @see Any
 */
typealias Any4 = Quadruple<Any, Any, Any, Any>
/**
 * A typealias representing a quadruple of nullable elements of type `Any`.
 *
 * This typealias simplifies the usage of `Quadruple` specifically for scenarios where
 * all four elements can be of any type and can also be nullable.
 *
 * @since 1.0.0
 * @see Quadruple
 * @see Any
 */
typealias NullableAny4 = Quadruple<Any?, Any?, Any?, Any?>

/**
 * A type alias representing a quadruple of integers.
 *
 * This type alias simplifies the usage of `Quadruple<Int, Int, Int, Int>`,
 * making it more concise and readable when working with groups of four integer values.
 *
 * @since 1.0.0
 * @see Quadruple
 * @see Int
 */
typealias Int4 = Quadruple<Int, Int, Int, Int>
/**
 * A type alias for a nullable tuple containing four elements of type [Int].
 *
 * This alias is used for representing a [Quadruple] where each element can either
 * be an [Int] or null.
 *
 * @since 1.0.0
 * @see Quadruple
 * @see Int
 */
typealias NullableInt4 = Quadruple<Int?, Int?, Int?, Int?>

/**
 * A type alias for a quadruple of four `Long` values.
 *
 * This provides a more concise and readable way to refer to a
 * `Quadruple<Long, Long, Long, Long>` in code, simplifying its usage.
 *
 * @since 1.0.0
 * @see Quadruple
 * @see Long
 */
typealias Long4 = Quadruple<Long, Long, Long, Long>
/**
 * A type alias representing a `Quadruple` with all elements being nullable `Long` types.
 *
 * This can be used for cases where you need to represent a tuple of four values,
 * where each value can either hold a `Long` or be `null`.
 *
 * @since 1.0.0
 * @see Quadruple
 * @see Long
 */
typealias NullableLong4 = Quadruple<Long?, Long?, Long?, Long?>

/**
 * A typealias representing a quadruple of four doubles.
 *
 * This provides a shorthand for using `Quadruple<Double, Double, Double, Double>`.
 * Useful for scenarios where a group of four double values needs to be represented as a single entity.
 *
 * @since 1.0.0
 * @see Quadruple
 * @see Double
 */
typealias Double4 = Quadruple<Double, Double, Double, Double>
/**
 * A type alias representing a nullable quadruple of [Double] values.
 *
 * This alias is useful for scenarios where a grouping of up to four nullable
 * [Double] values is needed, allowing for better code readability and type clarity.
 *
 * Maps to `Quadruple<Double?, Double?, Double?, Double?>`.
 *
 * @since 1.0.0
 * @see Quadruple
 * @see Double
 */
typealias NullableDouble4 = Quadruple<Double?, Double?, Double?, Double?>

/**
 * A type alias for a [Quadruple] of four [Boolean] values.
 *
 * This alias simplifies working with quadruples of Boolean values by providing
 * a more semantic and concise name.
 *
 * @since 1.0.0
 * @see Quadruple
 * @see Boolean
 */
typealias Boolean4 = Quadruple<Boolean, Boolean, Boolean, Boolean>
/**
 * Represents a quadruple of nullable Boolean values.
 *
 * This typealias provides a more specific and descriptive name for a
 * `Quadruple<Boolean?, Boolean?, Boolean?, Boolean?>` structure.
 * It is useful when working with scenarios that involve groups of four
 * nullable Boolean values.
 *
 * @since 1.0.0
 * @see Quadruple
 * @see Boolean
 */
typealias NullableBoolean4 = Quadruple<Boolean?, Boolean?, Boolean?, Boolean?>

/**
 * A type alias for a `Quadruple` where all four elements are of type `String`.
 *
 * This can be used to represent a set of four related strings and simplifies the usage
 * of the `Quadruple` class for such scenarios.
 *
 * @since 1.0.0
 * @see Quadruple
 * @see String
 */
typealias String4 = Quadruple<String, String, String, String>
/**
 * A typealias representing a quadruple of nullable strings using the [Quadruple] class.
 *
 * This alias is designed to group four nullable string values into a single, easy-to-use structure.
 * Each element in the quadruple is of type `String?`, allowing for the possibility of null values.
 *
 * @see Quadruple for more information about the underlying implementation.
 * @since 1.0.0
 * @see Quadruple
 * @see String
 */
typealias NullableString4 = Quadruple<String?, String?, String?, String?>

/**
 * A typealias for a quadruple where all four elements are of the same type.
 *
 * This simplifies the usage of `Quadruple` when the type of all elements is identical.
 *
 * @param T the type of all elements in the quadruple
 * @since 1.0.0
 * @see Quadruple
 */
typealias MonoQuadruple<T> = Quadruple<T, T, T, T>

/**
 * A typealias representing a quintuple where all elements are of type `Any`.
 *
 * This is shorthand for `Quintuple<Any, Any, Any, Any, Any>` and is used to hold
 * five values of potentially varying types, all treated generically as `Any`.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see Any
 */
typealias Any5 = Quintuple<Any, Any, Any, Any, Any>
/**
 * A type alias for a quintuple containing nullable values of any type.
 *
 * This type represents a tuple of five elements where each element can either hold a value
 * of any type or be null. It uses the `Quintuple` class to group the elements together.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see Any
 */
typealias NullableAny5 = Quintuple<Any?, Any?, Any?, Any?, Any?>

/**
 * A type alias for a quintuple of five integers.
 *
 * This alias simplifies the usage of a `Quintuple` specifically with
 * integer values for all five components.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see Int
 */
typealias Int5 = Quintuple<Int, Int, Int, Int, Int>
/**
 * A typealias representing a quintuple of nullable integers.
 *
 * This typealias simplifies the representation of `Quintuple` where all five elements are of type `Int?`.
 * Each element of the quintuple can either hold an integer value or be null.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see Int
 */
typealias NullableInt5 = Quintuple<Int?, Int?, Int?, Int?, Int?>

/**
 * A typealias for a Quintuple where all five elements are of type Long.
 *
 * This is a shorthand for representing a tuple containing exactly five Long values.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see Long
 */
typealias Long5 = Quintuple<Long, Long, Long, Long, Long>
/**
 * A type alias for a quintuple where all five elements are nullable `Long` values.
 *
 * This type alias can be used to represent a group of five nullable `Long` values,
 * leveraging the `Quintuple` class for tuple-like use cases.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see Long
 */
typealias NullableLong5 = Quintuple<Long?, Long?, Long?, Long?, Long?>

/**
 * A typealias for `Quintuple` where all five elements are of type `Double`.
 *
 * This is useful when working with a tuple that specifically requires
 * five `Double` elements, providing a shorthand notation for improved readability.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see Double
 */
typealias Double5 = Quintuple<Double, Double, Double, Double, Double>
/**
 * A type alias representing a quintuple with nullable double values.
 *
 * This is a shorthand for `Quintuple<Double?, Double?, Double?, Double?, Double?>`,
 * which can be used to represent a group of five nullable double values as a single object.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see Double
 */
typealias NullableDouble5 = Quintuple<Double?, Double?, Double?, Double?, Double?>

/**
 * A type alias representing a quintuple of five Boolean values.
 *
 * This type alias simplifies the usage of `Quintuple` when all elements
 * are of type `Boolean`, enhancing code readability and reducing verbosity.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see Boolean
 */
typealias Boolean5 = Quintuple<Boolean, Boolean, Boolean, Boolean, Boolean>
/**
 * A typealias representing a tuple of five nullable Boolean values.
 *
 * This typealias provides a more readable and concise way to define and work
 * with a grouping of five nullable Boolean values, using the `Quintuple` class.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see Boolean
 */
typealias NullableBoolean5 = Quintuple<Boolean?, Boolean?, Boolean?, Boolean?, Boolean?>

/**
 * A type alias representing a quintuple with all elements of type `String`.
 *
 * This type alias simplifies the usage of `Quintuple` when working specifically with
 * five string values, providing a more concise way to define such structures.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see String
 */
typealias String5 = Quintuple<String, String, String, String, String>
/**
 * A type alias for a quintuple that consists of five nullable strings.
 *
 * This type alias can be used to represent a group of five `String?` values,
 * simplifying readability when dealing with these specific kinds of tuples.
 *
 * @since 1.0.0
 * @see Quintuple
 * @see String
 */
typealias NullableString5 = Quintuple<String?, String?, String?, String?, String?>

/**
 * A typealias representing a Quintuple with all elements of the same type.
 *
 * This alias simplifies the declaration of quintuples where all five elements
 * share the same data type. It is a specific use case of the generic `Quintuple`
 * class and improves code readability and consistency.
 *
 * @param T the type of all elements in the quintuple
 * @since 1.0.0
 * @see Quintuple
 */
typealias MonoQuintuple<T> = Quintuple<T, T, T, T, T>

/**
 * Type alias for the `CUID` class or type.
 * This provides an alternative name for `CUID`, allowing it to be referred to as `Cuid`
 * within the codebase for improved readability or convention adherence.
 *
 * @since 1.0.0
 * @see CUID
 */
typealias Cuid = CUID

/**
 * A type alias for the KSUID class. This alias provides a more concise or context-specific
 * naming convention for KSUID, making the code more readable and easier to understand
 * within specific contexts in the application.
 *
 * @since 1.0.0
 * @see KSUID
 */
typealias Ksuid = KSUID

/**
 * Type alias for the NanoID class, which can be used to substitute the NanoID type with NanoId for readability
 * or convenience.
 *
 * This alias provides better customizability and flexibility when using NanoID-based functionality.
 *
 * @since 1.0.0
 * @see NanoID
 */
typealias NanoId = NanoID

/**
 * Alias for the `ShortUUID` type.
 *
 * This typealias is used to simplify references and improve code readability.
 *
 * @since 1.0.0
 * @see ShortUUID
 */
typealias ShortUuid = ShortUUID

/**
 * Represents a type alias for the ULID (Universally Unique Lexicographically Sortable Identifier) type.
 * Ulid is used to improve code clarity by providing a semantic name for the underlying ULID type.
 *
 * @since 1.0.0
 * @see ULID
 */
typealias Ulid = ULID

/**
 * Defines a type alias for `JSON`, making it available as `Json`.
 *
 * This can be used to increase readability or provide a simplified name for the `JSON` type.
 *
 * @since 1.0.0
 */
typealias Json = JSON

/**
 * A typealias for the `YAML` class.
 * This can be used interchangeably with the original `YAML` type,
 * providing a more concise or contextually descriptive naming alternative.
 *
 * @since 1.0.0
 */
@Beta
typealias Yaml = YAML

/**
 * Typealias for `Instant` from the kotlinx-datetime library, enabling the use of `Instant` with a shorter name (`KInstant`).
 * This typealias is experimental and subject to change as the `kotlinx-datetime` library evolves.
 *
 * @since 1.0.0
 * @see Instant
 */
@ExperimentalTime
typealias KInstant = Instant

/**
 * A type alias for the `Duration` class, providing an alternative name `KDuration`.
 *
 * This alias can be used interchangeably with `Duration` to represent time-based values.
 *
 * @since 1.0.0
 * @see Duration
 */
typealias KDuration = Duration

/**
 * Type alias for `BigInteger` to represent large integer values with arbitrary precision.
 * This type alias helps integrate `BigInteger` into Kotlin code with a more concise name.
 *
 * @since 1.0.0
 * @see BigInteger
 */
typealias BigInt = BigInteger

/**
 * Defines a type alias for `RMeasurement` specifically for measurements of data size.
 *
 * This alias simplifies the usage of the `RMeasurement` class when associated with the
 * `MeasureUnit.DataSizeUnit` unit, representing various units of data size such as bytes,
 * kilobytes, megabytes, etc.
 *
 * @see RMeasurement
 * @see MeasureUnit.DataSizeUnit
 * @since 1.0.0
 */
typealias DataSize = RMeasurement<MeasureUnit.DataSizeUnit>
/**
 * A type alias for `RMeasurement` specialized with length units.
 *
 * This type alias simplifies the usage of `RMeasurement` when working specifically
 * with physical measurements expressed in length units. The `MeasureUnit.LengthUnit`
 * type parameter ensures that only length-related units are used in conjunction with
 * this type alias.
 *
 * @see RMeasurement
 * @see MeasureUnit.LengthUnit
 * @since 1.0.0
 */
typealias Length = RMeasurement<MeasureUnit.LengthUnit>
/**
 * A type alias representing a measurement specifically for temperature-related units.
 *
 * This alias simplifies working with temperature measurements by explicitly associating
 * them with the `MeasureUnit.TemperatureUnit` scalar type. It is backed by the `RMeasurement`
 * class, enabling the use of the encapsulated functionalities like serialization support
 * and type safety for temperature measurements.
 *
 * @see RMeasurement
 * @see MeasureUnit.TemperatureUnit
 * @since 1.0.0
 */
typealias Temperature = RMeasurement<MeasureUnit.TemperatureUnit>
/**
 * A type alias for `RMeasurement` with `MeasureUnit.AreaUnit` as the scalar unit type.
 *
 * This alias represents a measurement of area, wherein the unit of measurement
 * is constrained to `MeasureUnit.AreaUnit`. It simplifies access to area-related
 * measurements by reducing verbosity and enhancing readability.
 *
 * @see RMeasurement
 * @see MeasureUnit.AreaUnit
 * @since 1.0.0
 */
typealias Area = RMeasurement<MeasureUnit.AreaUnit>
/**
 * A type alias representing a measurement of volume.
 *
 * The `Volume` type is a specialized usage of the `RMeasurement` class,
 * bound specifically to scalar units categorized as `MeasureUnit.VolumeUnit`.
 * It represents a numerical value paired with a unit of volume measurement.
 *
 * This type alias enables more meaningful and concise code, improving readability
 * when working with measurements of volume.
 *
 * @since 1.0.0
 */
typealias Volume = RMeasurement<MeasureUnit.VolumeUnit>
/**
 * A type alias for `RMeasurement` with `MeasureUnit.SpeedUnit` as the scalar unit type.
 *
 * This alias represents a speed measurement, where the numerical value is paired
 * with a unit of speed. It simplifies working with speed-related measurements by
 * providing a more descriptive type name.
 *
 * @since 1.0.0
 */
typealias Speed = RMeasurement<MeasureUnit.SpeedUnit>
/**
 * A type alias representing a measurement of acceleration with an associated unit of measurement.
 *
 * This alias simplifies the usage of `RMeasurement` with `MeasureUnit.AccelerationUnit`,
 * which represents acceleration units within the measurement framework.
 *
 * @see RMeasurement
 * @see MeasureUnit.AccelerationUnit
 * @since 1.0.0
 */
typealias Accelertation = RMeasurement<MeasureUnit.AccelerationUnit>
/**
 * A typealias representing a density measurement with units restricted to those of type `MeasureUnit.DensityUnit`.
 *
 * This typealias simplifies the use of `RMeasurement` for operations specifically related to density values,
 * where the unit of measurement is expected to be one of the predefined `DensityUnit`s in the `MeasureUnit` hierarchy.
 *
 * @see RMeasurement
 * @see MeasureUnit.DensityUnit
 * @since 1.0.0
 */
typealias Density = RMeasurement<MeasureUnit.DensityUnit>
/**
 * Typealias representing a plane angle measurement.
 *
 * `PlaneAngle` is a specific type of `RMeasurement` where the units are restricted to
 * those defined under `MeasureUnit.PlaneAngleUnit`. This allows for precise and
 * specialized handling of measurements related to plane angles.
 *
 * @see RMeasurement
 * @see MeasureUnit.PlaneAngleUnit
 * @since 1.0.0
 */
typealias PlaneAngle = RMeasurement<MeasureUnit.PlaneAngleUnit>
/**
 * A type alias representing a restricted measurement of mass.
 *
 * This alias is defined for `RMeasurement` using `MeasureUnit.MassUnit` as the scalar unit type,
 * specialized for measurements that pertain to mass. It simplifies working with mass units while
 * ensuring type safety and adherence to the `RMeasurement` structure.
 *
 * @see RMeasurement
 * @see MeasureUnit.MassUnit
 * @since 1.0.0
 */
typealias Mass = RMeasurement<MeasureUnit.MassUnit>

/**
 * Type alias for the `Color` class, allowing the use of the alternative name `Colour`.
 *
 * This can be used interchangeably with `Color` throughout the codebase.
 *
 * @since 1.0.0
 */
typealias Colour = Color

/**
 * Type alias for representing a Base36 type.
 * Base36 encodes numerical values using digits (0-9) and letters (A-Z), allowing for compact representation.
 *
 * @since 1.0.0
 */
typealias Hexatrigesimal = Base36

/**
 * Represents an alias for the `UnsupportedJSONTypeException` class.
 *
 * This typealias is provided for convenience and maintains consistency
 * in naming when referring to exceptions specific to unsupported JSON types.
 *
 * @see UnsupportedJSONTypeException
 * @since 1.0.0
 */
typealias UnsupportedJsonTypeException = UnsupportedJSONTypeException

/**
 * Alias for `UnsupportedYAMLTypeException`.
 *
 * This typealias provides a more convenient or readable name for
 * `UnsupportedYAMLTypeException`, which represents an exception
 * thrown when an unsupported JYAML type is encountered during
 * processing operations.
 *
 * @see UnsupportedYAMLTypeException
 * @since 1.0.0
 */
typealias UnsupportedYamlTypeException = UnsupportedYAMLTypeException