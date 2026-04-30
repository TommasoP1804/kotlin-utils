/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused", "UnusedReceiverParameter")
@file:Since("3.7.0")
@file:OptIn(Beta::class)

package dev.tommasop1804.kutils.dsl.yaml

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*

@DslMarker
annotation class YamlDslMarker

// --- VALUE TYPES ---

/**
 * Represents a value in a YAML structure.
 *
 * This interface is the base type for all YAML values, including strings, numbers,
 * booleans, mappings (objects), sequences (arrays), and null values. It provides a common
 * contract for serializing these values into their YAML representation using block style
 * (indentation-based, as commonly used in Kubernetes manifests and Spring Boot configuration).
 * @since 3.11.0
 * @author Tommaso Pastorelli
 */
sealed interface YamlValue {
    /**
     * Converts the current YAML value into its YAML string representation.
     *
     * This method serializes the YAML value into a string using block style formatting,
     * applying appropriate indentation based on the provided indent and depth levels.
     * Mappings and sequences are rendered with newlines and indentation for readability.
     *
     * @param indent The number of spaces to use for each level of indentation. Defaults to 2.
     * @param depth The current depth level in the YAML structure. This is primarily used
     *              for recursive calls to adjust the level of indentation. Defaults to 0.
     * @return The YAML string representation of the value.
     * @since 3.11.0
     */
    fun toYaml(indent: Int = 2, depth: Int = 0): Yaml
}

/**
 * Represents a YAML scalar string value.
 *
 * This class wraps a raw string value and provides functionality to properly
 * encode the value for YAML serialization. The encoder selects between plain,
 * single-quoted, double-quoted, or literal block style depending on content
 * (e.g. presence of special characters, newlines, or values that could be
 * misinterpreted as booleans, numbers, or null).
 *
 * @property value The raw string that this `YamlString` instance represents.
 * @since 3.11.0
 */
data class YamlString(val value: String) : YamlValue {
    /**
     * Converts the YAML string to its properly encoded YAML representation.
     *
     * For multiline strings, uses literal block scalar style (`|`).
     * For strings containing reserved characters or that could be misinterpreted
     * as other types (true, false, null, numbers), uses double-quoted style.
     * Otherwise, emits the value as a plain scalar.
     *
     * @param indent The number of spaces to use for indentation in the YAML output.
     * @param depth The current depth level in the YAML hierarchy, used for nested structures.
     * @return A YAML-formatted string representation of the value.
     * @since 3.11.0
     */
    override fun toYaml(indent: Int, depth: Int): Yaml {
        if (value.contains('\n')) {
            val pad = " ".repeat(indent * (depth + 1))
            val body = value.lines().joinToString("\n") { "$pad$it" }
            return Yaml("|\n$body")
        }
        return Yaml(encodeScalar(value))
    }

    private fun encodeScalar(s: String): String {
        if (s.isEmpty()) return "\"\""
        if (needsDoubleQuoting(s)) return "\"${escapeYaml(s)}\""
        return s
    }

    private fun needsDoubleQuoting(s: String): Boolean {
        if (s.lowercase() in RESERVED_LITERALS) return true
        if (NUMERIC_REGEX.matches(s)) return true
        if (s.first().isWhitespace() || s.last().isWhitespace()) return true
        if (s.first() in RESERVED_INDICATORS) return true
        if (s.contains(": ") || s.endsWith(":")) return true
        if (s.contains(" #")) return true
        return s.any { it.code < 0x20 || it == '\u007F' }
    }

    private fun escapeYaml(s: String): String = buildString {
        for (c in s) {
            when (c) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> if (c.code < 0x20) append("\\x%02x".format(c.code)) else append(c)
            }
        }
    }

    companion object {
        private val RESERVED_LITERALS = setOf(
            "true", "false", "null", "~", "yes", "no", "on", "off"
        )
        private val RESERVED_INDICATORS = setOf(
            '!', '&', '*', '{', '}', '[', ']', ',', '#', '|', '>', '\'', '"', '%', '@', '`'
        )
        private val NUMERIC_REGEX = Regex("^-?(\\d+\\.?\\d*|\\.\\d+)([eE][-+]?\\d+)?$")
    }
}

/**
 * Represents a YAML number within a YAML structure.
 *
 * This class is used to encapsulate numeric values in YAML format. It supports integer
 * and floating-point numbers. When serialized to YAML, floating-point numbers are
 * converted to integers if they have no fractional component, unless they are infinite.
 * Special floating-point values (NaN, Infinity) are emitted using YAML's reserved
 * representation (`.nan`, `.inf`, `-.inf`).
 *
 * @constructor Creates a YamlNumber with the specified numeric value.
 * @param value The numeric value to be represented as a YAML number.
 * @since 3.11.0
 */
data class YamlNumber(val value: Number) : YamlValue {
    /**
     * Converts the value of this YamlNumber instance to its YAML string representation.
     *
     * @param indent The number of spaces used for indentation in the generated YAML string.
     * @param depth The current depth level in the YAML structure, used for formatting purposes.
     * @return The YAML string representation of the value.
     * @since 3.11.0
     */
    override fun toYaml(indent: Int, depth: Int) = Yaml(when (value) {
        is Float -> when {
            value.isNaN() -> ".nan"
            value.isInfinite() -> if (value > 0) ".inf" else "-.inf"
            value % 1.0f == 0f -> value.toLong().toString()
            else -> value.toString()
        }
        is Double -> when {
            value.isNaN() -> ".nan"
            value.isInfinite() -> if (value > 0) ".inf" else "-.inf"
            value % 1.0 == 0.0 -> value.toLong().toString()
            else -> value.toString()
        }
        else -> value.toString()
    })
}

/**
 * Represents a YAML boolean value.
 *
 * This class encapsulates a boolean value as a YAML-compatible representation. It implements the `YamlValue` interface,
 * enabling it to be serialized into a YAML string. Instances of this class are immutable and directly map to a YAML
 * boolean (`true` or `false`).
 *
 * @property value The boolean value represented by this YAML boolean instance.
 * @since 3.11.0
 */
data class YamlBoolean(val value: Boolean) : YamlValue {
    /**
     * Converts the boolean value to its YAML string representation.
     *
     * @param indent The number of spaces to use for indentation in the YAML output.
     * @param depth The current depth in the YAML structure, used to calculate proper indentation.
     * @return The YAML string representation of the boolean value.
     * @since 3.11.0
     */
    override fun toYaml(indent: Int, depth: Int) = Yaml(value.toString())
}

/**
 * Represents a YAML `null` value.
 *
 * This object is a singleton implementation of the `YamlValue` interface
 * and is used to represent a `null` value in YAML structures.
 *
 * The YAML representation of this value is the literal `null`.
 * @since 3.11.0
 */
data object YamlNull : YamlValue {
    /**
     * Converts the object to its YAML string representation.
     *
     * @param indent The number of spaces to use for indentation in the resulting YAML string.
     * @param depth The current depth of the object in the YAML hierarchy, used for applying indentation.
     * @return A YAML string representation of the object.
     * @since 3.11.0
     */
    override fun toYaml(indent: Int, depth: Int) = Yaml("null")
}

/**
 * Represents a YAML mapping (object), which is a collection of key-value pairs where
 * each key is a string, and each value is a [YamlValue].
 *
 * Keys are unique and maintain insertion order. The mapping is rendered in block style,
 * with each entry on its own line and nested values indented relative to their parent.
 *
 * @property entries The internal map storing the key-value pairs of the YAML mapping.
 * @since 3.11.0
 */
data class YamlObject(val entries: LinkedHashMap<String, YamlValue>) : YamlValue {
    /**
     * Converts the YAML mapping into its block-style string representation.
     *
     * Empty mappings are rendered as flow style `{}`. Non-empty mappings are rendered
     * with each `key: value` pair on its own line. When a value is itself a non-empty
     * mapping or sequence, it is placed on the next line with increased indentation.
     *
     * @param indent The number of spaces to use for indentation per level.
     * @param depth The current depth of nested structures, used to calculate padding.
     * @return A string representing the YAML mapping in block style.
     * @since 3.11.0
     */
    override fun toYaml(indent: Int, depth: Int): Yaml {
        if (entries.isEmpty()) return Yaml("{}")
        val pad = " ".repeat(indent * depth)
        return Yaml(entries.entries.joinToString("\n") { [k, v] ->
            val key = encodeKey(k)
            when (v) {
                is YamlObject if v.entries.isNotEmpty() ->
                    "$pad$key:\n${v.toYaml(indent, depth + 1)}"
                is YamlArray if v.elements.isNotEmpty() ->
                    "$pad$key:\n${v.toYaml(indent, depth)}"
                is YamlString if v.value.contains('\n') ->
                    "$pad$key: ${v.toYaml(indent, depth)}"
                else -> "$pad$key: ${v.toYaml(indent, depth)}"
            }
        })
    }

    /**
     * Retrieves the value associated with the specified key from the YAML mapping.
     *
     * @param key the key whose associated value is to be returned
     * @return the value associated with the given key, or null if the key is not present
     * @since 3.11.0
     */
    operator fun get(key: String): YamlValue? = entries[key]

    private fun encodeKey(k: String): String {
        if (k.isEmpty()) return "\"\""
        if (k.any { it.isWhitespace() || it in ":#{}[],&*!|>'\"%@`" }) return "\"${k.replace("\"", "\\\"")}\""
        return k
    }
}

/**
 * Represents a YAML sequence (array), holding an ordered collection of YAML values.
 *
 * A YAML sequence can store elements such as strings, numbers, booleans, null, mappings,
 * or other sequences. It is rendered in block style with each element prefixed by `- `.
 * Nested mappings on a sequence element are inlined on the same line as the dash, with
 * subsequent keys indented to align with the first key.
 *
 * @property elements The list of YAML values contained in the sequence.
 * @since 3.11.0
 * @author Tommaso Pastorelli
 */
data class YamlArray(val elements: List<YamlValue>) : YamlValue {
    /**
     * Converts the current YamlArray to its YAML string representation in block style.
     *
     * Empty sequences are rendered as flow style `[]`. Each non-empty element is prefixed
     * with `- ` at the current indentation. Nested mappings have their first entry inlined
     * after the dash, with subsequent entries aligned to that first key. Nested sequences
     * are rendered on the next line with deeper indentation.
     *
     * @param indent The number of spaces to use for indentation.
     * @param depth The current depth of the YAML structure, used to calculate proper indentation.
     * @return A YAML string representation of the sequence.
     * @since 3.11.0
     */
    override fun toYaml(indent: Int, depth: Int): Yaml {
        if (elements.isEmpty()) return Yaml("[]")
        val pad = " ".repeat(indent * depth)
        return Yaml(elements.joinToString("\n") { el ->
            when (el) {
                is YamlObject if el.entries.isNotEmpty() -> {
                    val rendered = el.toYaml(indent, depth + 1).toString()
                    val firstLineIndent = " ".repeat(indent * (depth + 1))
                    val collapsed = rendered.replaceFirst(firstLineIndent, "$pad- ")
                    collapsed
                }
                is YamlArray if el.elements.isNotEmpty() ->
                    "$pad-\n${el.toYaml(indent, depth + 1)}"
                else -> "$pad- ${el.toYaml(indent, depth)}"
            }
        })
    }
}

// --- CONVERSION ---

/**
 * Converts an arbitrary object into a YAML-compatible [YamlValue].
 *
 * This function provides a generic mechanism for transforming any object
 * into a corresponding YAML representation. Specifically:
 * - `null` is mapped to [YamlNull].
 * - Instances of [YamlValue] are returned as is.
 * - Strings are wrapped in [YamlString].
 * - Numeric types (e.g., `Int`, `Long`, `Double`, `Float`, `Number`) are wrapped in [YamlNumber].
 * - Booleans are wrapped in [YamlBoolean].
 * - Maps are recursively converted into [YamlObject], with their keys converted to strings and
 *   their values converted into [YamlValue] instances.
 * - Lists and arrays are recursively converted into [YamlArray], with each element converted
 *   into a [YamlValue].
 * - Any other object is converted to a string via its `toString` method and wrapped in [YamlString].
 *
 * @return A [YamlValue] representing the YAML-serialized form of this object.
 * @since 3.11.0
 */
fun Any?.toYamlValue(): YamlValue = when (this) {
    null -> YamlNull
    is YamlValue -> this
    is String -> YamlString(this)
    is Int -> YamlNumber(this)
    is Long -> YamlNumber(this)
    is Double -> YamlNumber(this)
    is Float -> YamlNumber(this)
    is Boolean -> YamlBoolean(this)
    is Number -> YamlNumber(this)
    is Map<*, *> -> YamlObject(LinkedHashMap(entries.associate { [k, v] -> k.toString() to v.toYamlValue() }))
    is List<*> -> YamlArray(map { it.toYamlValue() })
    is Array<*> -> YamlArray(map { it.toYamlValue() })
    else -> YamlString(toString())
}

// --- OBJECT BUILDER ---

/**
 * A builder for constructing YAML mappings (objects) in a structured and readable manner.
 *
 * This builder provides DSL functions to associate keys with various types of
 * values, including strings, numbers, booleans, and nested YAML structures. Null values
 * are supported and represented as `YamlNull` within the mapping. The keys maintain their
 * insertion order.
 * @since 3.11.0
 */
@YamlDslMarker
class YamlObjectBuilder {
    /**
     * A map storing entries for building a YAML mapping.
     *
     * This map associates string keys with `YamlValue` instances, representing
     * the structure of a YAML mapping being dynamically built. It is used internally
     * by the `YamlObjectBuilder` class to collect and manage key-value pairs
     * before constructing a `YamlObject`.
     *
     * Keys in this map correspond to YAML mapping keys, while values
     * can be any valid `YamlValue`, including `YamlString`, `YamlNumber`,
     * `YamlBoolean`, or `YamlNull`. If a `null` or no value is provided for a key,
     * the corresponding `YamlNull` value will be stored in the map.
     *
     * The functions `String.to()` are used to populate this map with entries.
     *
     * This property is marked with `@PublishedApi` to allow inline functions
     * within the same module to access it.
     * @since 3.11.0
     */
    @PublishedApi
    internal val entries = LinkedHashMap<String, YamlValue>()

    /**
     * Associates the current string (key) with the provided value in the `entries` map.
     * If the value is null, it associates the key with a `YamlNull` instead.
     *
     * @param value The value to associate with the key. If null, the key is associated with `YamlNull`.
     * @since 3.11.0
     */
    infix fun String.to(value: String?) { entries[this] = value?.let { YamlString(it) } ?: YamlNull }
    /**
     * Associates a string key with a value, converting the value to a `YamlNumber` if it is non-null,
     * or to `YamlNull` if it is null. The association is stored in the `entries` map.
     *
     * @param value The number to associate with the string key. Can be null.
     * @since 3.11.0
     */
    infix fun String.to(value: Number?) { entries[this] = value?.let { YamlNumber(it) } ?: YamlNull }
    /**
     * Associates the invoking string key with a value of type YamlBoolean or YamlNull
     * in the entries map. If the provided boolean value is null, YamlNull is used
     * as the corresponding value.
     *
     * @param value The Boolean value to associate with the string key. If null,
     *              associates the string key with YamlNull.
     * @since 3.11.0
     */
    infix fun String.to(value: Boolean?) { entries[this] = value?.let { YamlBoolean(it) } ?: YamlNull }
    /**
     * Associates the current string with a specified [YamlValue] or assigns it to [YamlNull] if the value is null.
     *
     * @param value The YamlValue to associate with the string, or null to associate with YamlNull.
     * @since 3.11.0
     */
    infix fun String.to(value: YamlValue?) { entries[this] = value ?: YamlNull }
    /**
     * Associates the current string with a nullable value in the entries map.
     *
     * @param value A nullable value (of type `Nothing?`) to associate with the current string.
     * @since 3.11.0
     */
    @JvmName("toNullable")
    infix fun String.to(value: Nothing?) { entries[this] = YamlNull }

    /**
     * Associates the current string (key) with a YAML sequence constructed from the provided iterable of strings.
     * The resulting `YamlArray` object is added to the `entries` map with the string as its key.
     *
     * @param array The iterable collection of strings to be converted into a `YamlArray`
     *              and associated with the current string key.
     * @since 3.11.0
     */
    @JvmName("toIterableString")
    infix fun String.to(array: Iterable<String>) { entries[this] = YamlArray(array.map { YamlString(it) }) }
    /**
     * Associates a string key with an iterable collection of numeric values, converting the numbers
     * into instances of `YamlNumber`. The association is stored in the `entries` map as a `YamlArray`.
     *
     * @param array An iterable collection of numbers to associate with the string key. Each number
     *              in the collection is converted to a `YamlNumber` and included in the `YamlArray`.
     * @since 3.11.0
     */
    @JvmName("toIterableNumber")
    infix fun String.to(array: Iterable<Number>) { entries[this] = YamlArray(array.map { YamlNumber(it) }) }
    /**
     * Associates the current string (key) with the provided iterable of Boolean values in the `entries` map.
     * Each Boolean value in the iterable is converted to a `YamlBoolean` and grouped into a `YamlArray`.
     *
     * @param array An iterable collection of Boolean values to associate with the key. Each value is
     *              wrapped as a `YamlBoolean` and stored in a `YamlArray`.
     * @since 3.11.0
     */
    @JvmName("toIterableBoolean")
    infix fun String.to(array: Iterable<Boolean>) { entries[this] = YamlArray(array.map { YamlBoolean(it) }) }
    /**
     * Associates the current string (key) with an iterable collection of [YamlValue]s
     * in the `entries` map. The collection is converted to a [YamlArray] before being
     * associated with the key.
     *
     * @param array The iterable collection of [YamlValue]s to associate with the key.
     * @since 3.11.0
     */
    @JvmName("toIterableYamlValue")
    infix fun String.to(array: Iterable<YamlValue>) { entries[this] = YamlArray(array.toList()) }

    /**
     * Builds and returns a new instance of [YamlObject] using the key-value pairs
     * collected in the internal map of the [YamlObjectBuilder].
     *
     * @return A [YamlObject] containing all the key-value pairs defined in the builder.
     * @since 3.11.0
     */
    fun build(): YamlObject = YamlObject(entries)
}

// --- ARRAY BUILDER ---

/**
 * A builder for constructing YAML sequences (arrays) in a DSL-friendly manner.
 *
 * This class provides a set of utility functions to construct YAML sequences by appending
 * various types of YAML-compatible values, such as strings, numbers, booleans, and other
 * YAML structures. It supports both DSL-style syntax and programmatic additions.
 *
 * The resulting YAML sequence can be built using the [build] function.
 * @since 3.11.0
 */
@YamlDslMarker
class YamlArrayBuilder {
    /**
     * A mutable list holding YAML values that are components of a YAML sequence being constructed.
     *
     * This internal property is used within the `YamlArrayBuilder` to store YAML elements of type `YamlValue`
     * before building the final `YamlArray`. Elements can be added to this list through various operator functions
     * and utility methods provided by the builder.
     *
     * The list is initialized as empty using the `emptyMList` utility function and is populated dynamically
     * as elements are added by the user via operations such as `+` or `add`.
     *
     * Modifications to this list affect the resulting YAML structure generated by the builder.
     * The list is marked as `internal`, meaning it is only accessible within the same module.
     *
     * @since 3.11.0
     */
    @PublishedApi
    internal val elements = emptyMList<YamlValue>()

    /**
     * Adds the string as a `YamlString` element to the `elements` collection.
     *
     * This operator function is triggered when the unary plus operator is applied to a `String` object.
     * It converts the string into a `YamlString` instance and appends it to the `elements` collection.
     * @since 3.11.0
     */
    operator fun String.unaryPlus() { elements += YamlString(this) }
    /**
     * Adds the current number to the `elements` collection encapsulated as a `YamlNumber` instance.
     * This method is an operator overload for the unary plus operator (`+`).
     * @since 3.11.0
     */
    operator fun Number.unaryPlus() { elements += YamlNumber(this) }
    /**
     * Overloads the unary plus (`+`) operator for the `Boolean` type.
     *
     * This operator function is used to add a `YamlBoolean` representation
     * of the current `Boolean` instance to the `elements` collection.
     * @since 3.11.0
     */
    operator fun Boolean.unaryPlus() { elements += YamlBoolean(this) }
    /**
     * Adds the current `YamlValue` instance to a collection of elements.
     *
     * This operator function allows the `+` unary operator to be used on a `YamlValue` instance
     * to append it to a mutable list or collection of `YamlValue` objects.
     *
     * The operation modifies the `elements` collection by adding the instance it is invoked on.
     * @since 3.11.0
     */
    operator fun YamlValue.unaryPlus() { elements += this }

    /**
     * Adds a new value to the collection by converting it to a YAML-compatible format.
     *
     * @param value The value to be added to the collection. Can be of any type or null.
     * @since 3.11.0
     */
    fun add(value: Any?) { elements += value.toYamlValue() }

    /**
     * Builds and returns a `YamlArray` containing all the elements added to this builder.
     *
     * This method collects the elements accumulated in the builder,
     * wraps them into a `YamlArray`, and returns the resulting YAML sequence instance.
     *
     * @return A `YamlArray` containing the elements added to the builder.
     * @since 3.11.0
     */
    fun build(): YamlArray = YamlArray(elements.toList())
}

// --- TOP-LEVEL ENTRY POINTS ---

/**
 * Builds a YAML representation using the provided DSL block to define its structure.
 *
 * This function uses a [YamlObjectBuilder] to construct a YAML mapping based on the
 * key-value pairs specified within the DSL block. The result is then transformed
 * into a [Yaml] instance.
 *
 * @param block A DSL block using [YamlObjectBuilder] to define the structure
 *              and contents of the YAML mapping. Keys can be associated
 *              with strings, numbers, booleans, or other nested YAML values.
 * @return A [Yaml] object representing the YAML structure defined in the DSL block.
 * @since 3.11.0
 */
fun buildYaml(block: ReceiverConsumer<YamlObjectBuilder>): Yaml =
    YamlObjectBuilder().apply(block).build().toYaml()

/**
 * Initializes a YAML mapping using the specified DSL block.
 *
 * This function provides a builder-based approach to construct a YAML mapping.
 * The block parameter allows defining the content (key-value pairs) of the YAML
 * mapping using DSL syntax. The resulting YAML mapping is built using a [YamlObjectBuilder]
 * and returned as an immutable [YamlObject].
 *
 * @param block A DSL block used to configure a [YamlObjectBuilder]. Within this block,
 *              you can define key-value pairs for the YAML mapping being constructed.
 * @since 3.11.0
 */
fun initYaml(block: ReceiverConsumer<YamlObjectBuilder>) =
    YamlObjectBuilder().apply(block).build()

/**
 * Creates a YAML mapping using the provided block of operations defined on a [YamlObjectBuilder].
 *
 * This function provides a DSL for building YAML mappings in a structured manner. The [block]
 * parameter defines the operations to populate the [YamlObjectBuilder] with key-value pairs,
 * which can represent strings, numbers, booleans, nested objects, or null values.
 *
 * @param block A lambda with a receiver of type [YamlObjectBuilder], specifying the operations
 *              for constructing the YAML mapping.
 * @return A constructed [YamlObject] containing all the key-value pairs defined in the builder.
 * @since 3.11.0
 */
fun yamlObject(block: ReceiverConsumer<YamlObjectBuilder>) =
    YamlObjectBuilder().apply(block).build()

/**
 * Constructs a `YamlArray` using a DSL-style block for defining its elements.
 *
 * This function initializes a `YamlArrayBuilder`, applies the provided block to add
 * elements to the builder, and then builds the resulting `YamlArray`.
 *
 * @param block A lambda with receiver that defines the elements of the YAML sequence.
 *              The receiver is an instance of `YamlArrayBuilder`, allowing DSL-style
 *              construction of YAML elements.
 * @return The constructed `YamlArray` containing the elements added within the block.
 * @since 3.11.0
 */
fun yamlArray(block: ReceiverConsumer<YamlArrayBuilder>) =
    YamlArrayBuilder().apply(block).build()

/**
 * Constructs a YAML sequence using a DSL-friendly builder.
 *
 * This function leverages the [YamlArrayBuilder] to create a YAML sequence.
 * The provided lambda [block] is applied to the builder to add elements
 * in a DSL-like manner. Once all elements are defined, the YAML sequence
 * is built and converted to a [Yaml] representation.
 *
 * @param block A lambda with receiver defining the operations to populate
 * the YAML sequence using the [YamlArrayBuilder].
 * @return A [Yaml] object representing the constructed YAML sequence.
 * @since 3.11.0
 */
fun buildYamlArray(block: ReceiverConsumer<YamlArrayBuilder>): Yaml =
    YamlArrayBuilder().apply(block).build().toYaml()

/**
 * Initializes and constructs a YAML sequence using a DSL block.
 *
 * This function creates a new instance of `YamlArrayBuilder`, applies the provided
 * block to configure the YAML sequence, and returns the configured builder instance.
 *
 * @param block A DSL block to configure the `YamlArrayBuilder` instance. The block provides
 *              access to the builder, allowing the caller to add elements to the YAML sequence
 *              using DSL-style syntax.
 * @since 3.11.0
 */
fun initYamlArray(block: ReceiverConsumer<YamlArrayBuilder>) =
    YamlArrayBuilder().apply(block)

/**
 * Creates and returns a [YamlObject] by executing the specified [block] on a [YamlObjectBuilder].
 *
 * This function is a DSL construct that allows defining nested YAML mappings in a structured and readable
 * manner. The [block] parameter is a lambda with a receiver of type [YamlObjectBuilder], used to build
 * the key-value pairs of the resultant YAML mapping.
 *
 * @param block A lambda function with a [YamlObjectBuilder] receiver to define the structure
 *              of the YAML mapping.
 * @return A [YamlObject] containing the key-value pairs defined in the [block].
 * @since 3.11.0
 */
fun YamlObjectBuilder.obj(block: ReceiverConsumer<YamlObjectBuilder>): YamlObject = yamlObject(block)

/**
 * Adds a sequence to the YAML mapping being constructed, using a DSL-style block to define its contents.
 *
 * @param block A lambda with receiver of type [YamlArrayBuilder], used to define the contents
 *              of the YAML sequence.
 * @return The created [YamlArray], representing the sequence added to the YAML mapping.
 * @since 3.11.0
 */
fun YamlObjectBuilder.array(block: ReceiverConsumer<YamlArrayBuilder>): YamlArray = yamlArray(block)