@file:Suppress("unused", "UnusedReceiverParameter")
@file:Since("3.3.0")

package dev.tommasop1804.kutils.dsl.json

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.dsl.jsonschema.*

@DslMarker
annotation class JsonDsl

// --- VALUE TYPES ---

/**
 * Represents a value in a JSON structure.
 *
 * This interface is the base type for all JSON values, including strings, numbers,
 * booleans, objects, arrays, and null values. It provides a common contract
 * for serializing these values into their JSON representation.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
sealed interface JsonValue {
    /**
     * Converts the current JSON value into its JSON string representation.
     *
     * This method serializes the JSON value into a string, applying appropriate formatting
     * based on the provided indentation and depth levels. Indentation is applied to nested
     * structures such as objects and arrays for human-readable formatting.
     *
     * @param indent The number of spaces to use for each level of indentation. Defaults to 2.
     *               If set to 0, no indentation is used, and the output will be compact.
     * @param depth The current depth level in the JSON structure. This is primarily used
     *              for recursive calls to adjust the level of indentation. Defaults to 0.
     * @return The JSON string representation of the value.
     * @since 3.3.0
     */
    fun toJson(indent: Int = 2, depth: Int = 0): Json
}

/**
 * Represents a JSON string value.
 *
 * This class wraps a raw string value and provides functionality to properly
 * escape the value for JSON serialization. It is a concrete implementation
 * of the `JsonValue` interface.
 *
 * @property value The raw string that this `JsonString` instance represents.
 * @since 3.3.0
 */
data class JsonString(val value: String) : JsonValue {
    /**
     * Converts the JSON string to its escaped and properly formatted JSON representation.
     *
     * @param indent The number of spaces to use for indentation in the JSON output.
     * @param depth The current depth level in the JSON hierarchy, used for nested structures.
     * @return A JSON-formatted string representation of the value.
     * @since 3.3.0
     */
    override fun toJson(indent: Int, depth: Int) = Json("\"${escapeJson(value)}\"")
}

/**
 * Represents a JSON number within a JSON structure.
 *
 * This class is used to encapsulate numeric values in JSON format. It supports integer
 * and floating-point numbers. When serialized to JSON, floating-point numbers are
 * converted to integers if they have no fractional component, unless they are infinite.
 *
 * @constructor Creates a JsonNumber with the specified numeric value.
 * @param value The numeric value to be represented as a JSON number.
 * @since 3.3.0
 */
data class JsonNumber(val value: Number) : JsonValue {
    /**
     * Converts the value of this JsonNumber instance to its JSON string representation.
     *
     * @param indent The number of spaces used for indentation in the generated JSON string.
     * @param depth The current depth level in the JSON structure, used for formatting purposes.
     * @return The JSON string representation of the value.
     * @since 3.3.0
     */
    override fun toJson(indent: Int, depth: Int) = Json(when (value) {
        is Float -> if (value % 1.0f == 0f && !value.isInfinite()) value.toLong().toString() else value.toString()
        is Double -> if (value % 1.0 == 0.0 && !value.isInfinite()) value.toLong().toString() else value.toString()
        else -> value.toString()
    })
}

/**
 * Represents a JSON boolean value.
 *
 * This class encapsulates a boolean value as a JSON-compatible representation. It implements the `JsonValue` interface,
 * enabling it to be serialized into a JSON string. Instances of this class are immutable and directly map to a JSON
 * boolean (`true` or `false`).
 *
 * @property value The boolean value represented by this JSON boolean instance.
 * @since 3.3.0
 */
data class JsonBoolean(val value: Boolean) : JsonValue {
    /**
     * Converts the boolean value to its JSON string representation.
     *
     * @param indent The number of spaces to use for indentation in the JSON output.
     * @param depth The current depth in the JSON structure, used to calculate proper indentation.
     * @return The JSON string representation of the boolean value.
     * @since 3.3.0
     */
    override fun toJson(indent: Int, depth: Int) = Json(value.toString())
}

/**
 * Represents a JSON `null` value.
 *
 * This object is a singleton implementation of the `JsonValue` interface
 * and is used to represent a `null` value in JSON structures.
 *
 * The JSON representation of this value is the literal `null`.
 * @since 3.3.0
 */
data object JsonNull : JsonValue {
    /**
     * Converts the object to its JSON string representation with the specified formatting options.
     *
     * @param indent The number of spaces to use for indentation in the resulting JSON string.
     * @param depth The current depth of the object in the JSON hierarchy, used for applying indentation.
     * @return A JSON string representation of the object.
     * @since 3.3.0
     */
    override fun toJson(indent: Int, depth: Int) = Json("null")
}

/**
 * Represents a JSON object, which is a collection of key-value pairs where
 * each key is a string, and each value is a [JsonValue].
 *
 * Keys are unique and maintain insertion order.
 *
 * @property entries The internal map storing the key-value pairs of the JSON object.
 * @since 3.3.0
 */
data class JsonObject(val entries: LinkedHashMap<String, JsonValue>) : JsonValue {
    /**
     * Converts the JSON object into its string representation with optional indentation.
     *
     * @param indent The number of spaces to use for indentation. If 0, no indentation or newlines will be added.
     * @param depth The current depth of nested structures, used to calculate padding for pretty printing.
     * @return A string representing the JSON object.
     * @since 3.3.0
     */
    override fun toJson(indent: Int, depth: Int): Json {
        if (entries.isEmpty()) return Json.EMPTY_JSON
        val pad = if (indent > 0) " ".repeat(indent * (depth + 1)) else String.EMPTY
        val padClose = if (indent > 0) " ".repeat(indent * depth) else String.EMPTY
        val nl = if (indent > 0) "\n" else String.EMPTY
        return Json(entries.entries.joinToString(",$nl", "{$nl", "$nl$padClose}") { [k, v] ->
            "$pad\"${escapeJson(k)}\": ${v.toJson(indent, depth + 1)}"
        })
    }

    /**
     * Retrieves the value associated with the specified key from the JSON structure.
     *
     * @param key the key whose associated value is to be returned
     * @return the value associated with the given key, or null if the key is not present
     * @since 3.3.0
     */
    operator fun get(key: String): JsonValue? = entries[key]
}

/**
 * Represents a JSON array, holding an ordered collection of JSON values.
 *
 * A JSON array can store elements such as strings, numbers, booleans, null, JSON objects, or
 * other JSON arrays. It provides functionality to serialize its contents into JSON format with
 * optional indentation for pretty printing.
 *
 * @property elements The list of JSON values contained in the array.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class JsonArray(val elements: List<JsonValue>) : JsonValue {
    /**
     * Converts the current JsonArray object to its JSON string representation.
     *
     * The JSON string is formatted based on the provided indentation level and depth.
     * If the array is empty, it returns `"[]"`.
     * For arrays containing only simple types (e.g., JsonString, JsonNumber, JsonBoolean, JsonNull)
     * and with a maximum of 10 elements, the representation is compact without additional formatting.
     * Otherwise, the JSON string will be pretty-printed with the given indentation.
     *
     * @param indent The number of spaces to use for indentation. If 0, no indentation is applied.
     * @param depth The current depth of the JSON structure, used to calculate proper indentation.
     * @return A JSON string representation of the array.
     * @since 3.3.0
     */
    override fun toJson(indent: Int, depth: Int): Json {
        if (elements.isEmpty()) return Json.EMPTY_JSON_ARRAY
        // Compact for primitives
        val allSimple = elements.all { it is JsonString || it is JsonNumber || it is JsonBoolean || it is JsonNull }
        if (allSimple && elements.size <= 10) {
            return Json(elements.joinToString(", ", "[", "]") { it.toJson(0) })
        }
        val pad = if (indent > 0) " ".repeat(indent * (depth + 1)) else String.EMPTY
        val padClose = if (indent > 0) " ".repeat(indent * depth) else String.EMPTY
        val nl = if (indent > 0) "\n" else String.EMPTY
        return Json(elements.joinToString(",$nl", "[$nl", "$nl$padClose]") { "$pad${it.toJson(indent, depth + 1)}" })
    }
}

// --- CONVERSION ---

/**
 * Converts an arbitrary object into a JSON-compatible [JsonValue].
 *
 * This function provides a generic mechanism for transforming any object
 * into a corresponding JSON representation. Specifically:
 * - `null` is mapped to [JsonNull].
 * - Instances of [JsonValue] are returned as is.
 * - Strings are wrapped in [JsonString].
 * - Numeric types (e.g., `Int`, `Long`, `Double`, `Float`, `Number`) are wrapped in [JsonNumber].
 * - Booleans are wrapped in [JsonBoolean].
 * - Maps are recursively converted into [JsonObject], with their keys converted to strings and
 *   their values converted into [JsonValue] instances.
 * - Lists and arrays are recursively converted into [JsonArray], with each element converted
 *   into a [JsonValue].
 * - Any other object is converted to a string via its `toString` method and wrapped in [JsonString].
 *
 * @return A [JsonValue] representing the JSON-serialized form of this object.
 * @since 3.3.0
 */
fun Any?.toJsonValue(): JsonValue = when (this) {
    null -> JsonNull
    is JsonValue -> this
    is String -> JsonString(this)
    is Int -> JsonNumber(this)
    is Long -> JsonNumber(this)
    is Double -> JsonNumber(this)
    is Float -> JsonNumber(this)
    is Boolean -> JsonBoolean(this)
    is Number -> JsonNumber(this)
    is Map<*, *> -> JsonObject(LinkedHashMap(entries.associate { [k, v] -> k.toString() to v.toJsonValue() }))
    is List<*> -> JsonArray(map { it.toJsonValue() })
    is Array<*> -> JsonArray(map { it.toJsonValue() })
    else -> JsonString(toString())
}

// --- OBJECT BUILDER ---

/**
 * A builder for constructing JSON objects in a structured and readable manner.
 *
 * This builder provides DSL functions to associate keys with various types of
 * values, including strings, numbers, booleans, and nested JSON structures. Null values
 * are supported and represented as `JsonNull` within the object. The keys maintain their
 * insertion order.
 * @since 3.3.0
 */
@JsonDsl
class JsonObjectBuilder {
    /**
     * A map storing entries for building a JSON object.
     *
     * This map associates string keys with `JsonValue` instances, representing
     * the structure of a JSON object being dynamically built. It is used internally
     * by the `JsonObjectBuilder` class to collect and manage key-value pairs
     * before constructing a `JsonObject`.
     *
     * Keys in this map correspond to JSON object property names, while values
     * can be any valid `JsonValue`, including `JsonString`, `JsonNumber`,
     * `JsonBoolean`, or `JsonNull`. If a `null` or no value is provided for a key,
     * the corresponding `JsonNull` value will be stored in the map.
     *
     * The functions `String.to()` are used to populate this map with entries.
     *
     * This property is marked with `@PublishedApi` to allow inline functions
     * within the same module to access it.
     * @since 3.3.0
     */
    @PublishedApi
    internal val entries = LinkedHashMap<String, JsonValue>()

    /**
     * Associates the current string (key) with the provided value in the `entries` map.
     * If the value is null, it associates the key with a `JsonNull` instead.
     *
     * @param value The value to associate with the key. If null, the key is associated with `JsonNull`.
     * @since 3.3.0
     */
    infix fun String.to(value: String?) { entries[this] = value?.let { JsonString(it) } ?: JsonNull }
    /**
     * Associates a string key with a value, converting the value to a `JsonNumber` if it is non-null,
     * or to `JsonNull` if it is null. The association is stored in the `entries` map.
     *
     * @param value The number to associate with the string key. Can be null.
     * @since 3.3.0
     */
    infix fun String.to(value: Number?) { entries[this] = value?.let { JsonNumber(it) } ?: JsonNull }
    /**
     * Associates the invoking string key with a value of type JsonBoolean or JsonNull
     * in the entries map. If the provided boolean value is null, JsonNull is used
     * as the corresponding value.
     *
     * @param value The Boolean value to associate with the string key. If null,
     *              associates the string key with JsonNull.
     * @since 3.3.0
     */
    infix fun String.to(value: Boolean?) { entries[this] = value?.let { JsonBoolean(it) } ?: JsonNull }
    /**
     * Associates the current string with a specified [JsonValue] or assigns it to [JsonNull] if the value is null.
     *
     * @param value The JsonValue to associate with the string, or null to associate with JsonNull.
     * @since 3.3.0
     */
    infix fun String.to(value: JsonValue?) { entries[this] = value ?: JsonNull }
    /**
     * Associates the current string with a nullable value in the entries map.
     *
     * @param value A nullable value (of type `Nothing?`) to associate with the current string.
     * @since 3.3.0
     */
    @JvmName("toNullable")
    infix fun String.to(value: Nothing?) { entries[this] = JsonNull }

    /**
     * Associates the current string (key) with a JSON array constructed from the provided iterable of strings.
     * The resulting `JsonArray` object is added to the `entries` map with the string as its key.
     *
     * @param array The iterable collection of strings to be converted into a `JsonArray`
     *              and associated with the current string key.
     * @since 3.3.0
     */
    @JvmName("toIterableString")
    infix fun String.to(array: Iterable<String>) { entries[this] = JsonArray(array.map { JsonString(it) }) }
    /**
     * Associates a string key with an iterable collection of numeric values, converting the numbers
     * into instances of `JsonNumber`. The association is stored in the `entries` map as a `JsonArray`.
     *
     * @param array An iterable collection of numbers to associate with the string key. Each number
     *              in the collection is converted to a `JsonNumber` and included in the `JsonArray`.
     * @since 3.3.0
     */
    @JvmName("toIterableNumber")
    infix fun String.to(array: Iterable<Number>) { entries[this] = JsonArray(array.map { JsonNumber (it) }) }
    /**
     * Associates the current string (key) with the provided iterable of Boolean values in the `entries` map.
     * Each Boolean value in the iterable is converted to a `JsonBoolean` and grouped into a `JsonArray`.
     *
     * @param array An iterable collection of Boolean values to associate with the key. Each value is
     *              wrapped as a `JsonBoolean` and stored in a `JsonArray`.
     * @since 3.3.0
     */
    @JvmName("toIterableBoolean")
    infix fun String.to(array: Iterable<Boolean>) { entries[this] = JsonArray(array.map { JsonBoolean(it) }) }
    /**
     * Associates the current string (key) with an iterable collection of [JsonValue]s
     * in the `entries` map. The collection is converted to a [JsonArray] before being
     * associated with the key.
     *
     * @param array The iterable collection of [JsonValue]s to associate with the key.
     * @since 3.3.0
     */
    @JvmName("toIterableJsonValue")
    infix fun String.to(array: Iterable<JsonValue>) { entries[this] = JsonArray(array.toList()) }

    /**
     * Builds and returns a new instance of [JsonObject] using the key-value pairs
     * collected in the internal map of the [JsonObjectBuilder].
     *
     * @return A [JsonObject] containing all the key-value pairs defined in the builder.
     * @since 3.3.0
     */
    fun build(): JsonObject = JsonObject(entries)
}

// --- ARRAY BUILDER ---

/**
 * A builder for constructing JSON arrays in a DSL-friendly manner.
 *
 * This class provides a set of utility functions to construct JSON arrays by appending
 * various types of JSON-compatible values, such as strings, numbers, booleans, and other
 * JSON structures. It supports both DSL-style syntax and programmatic additions.
 *
 * The resulting JSON array can be built using the [build] function.
 * @since 3.3.0
 */
@JsonDsl
class JsonArrayBuilder {
    /**
     * A mutable list holding JSON values that are components of a JSON array being constructed.
     *
     * This internal property is used within the `JsonArrayBuilder` to store JSON elements of type `JsonValue`
     * before building the final `JsonArray`. Elements can be added to this list through various operator functions
     * and utility methods provided by the builder.
     *
     * The list is initialized as empty using the `emptyMList` utility function and is populated dynamically
     * as elements are added by the user via operations such as `+` or `add`.
     *
     * Modifications to this list affect the resulting JSON structure generated by the builder.
     * The list is marked as `internal`, meaning it is only accessible within the same module.
     *
     * @since 3.3.0
     */
    @PublishedApi
    internal val elements = emptyMList<JsonValue>()

    /**
     * Adds the string as a `JsonString` element to the `elements` collection.
     *
     * This operator function is triggered when the unary plus operator is applied to a `String` object.
     * It converts the string into a `JsonString` instance and appends it to the `elements` collection.
     * @since 3.3.0
     */
    operator fun String.unaryPlus() { elements += JsonString(this) }
    /**
     * Adds the current number to the `elements` collection encapsulated as a `JsonNumber` instance.
     * This method is an operator overload for the unary plus operator (`+`).
     * @since 3.3.0
     */
    operator fun Number.unaryPlus() { elements += JsonNumber(this) }
    /**
     * Overloads the unary plus (`+`) operator for the `Boolean` type.
     *
     * This operator function is used to add a `JsonBoolean` representation
     * of the current `Boolean` instance to the `elements` collection.
     * @since 3.3.0
     */
    operator fun Boolean.unaryPlus() { elements += JsonBoolean(this) }
    /**
     * Adds the current `JsonValue` instance to a collection of elements.
     *
     * This operator function allows the `+` unary operator to be used on a `JsonValue` instance
     * to append it to a mutable list or collection of `JsonValue` objects.
     *
     * The operation modifies the `elements` collection by adding the instance it is invoked on.
     * @since 3.3.0
     */
    operator fun JsonValue.unaryPlus() { elements += this }

    /**
     * Adds a new value to the collection by converting it to a JSON-compatible format.
     *
     * @param value The value to be added to the collection. Can be of any type or null.
     * @since 3.3.0
     */
    fun add(value: Any?) { elements += value.toJsonValue() }

    /**
     * Builds and returns a `JsonArray` containing all the elements added to this builder.
     *
     * This method collects the elements accumulated in the builder,
     * wraps them into a `JsonArray`, and returns the resulting JSON array instance.
     *
     * @return A `JsonArray` containing the elements added to the builder.
     * @since 3.3.0
     */
    fun build(): JsonArray = JsonArray(elements.toList())
}

// --- TOP-LEVEL ENTRY POINTS ---

/**
 * Builds a JSON representation using the provided DSL block to define its structure.
 *
 * This function uses a [JsonObjectBuilder] to construct a JSON object based on the
 * key-value pairs specified within the DSL block. The result is then transformed
 * into a [Json] instance.
 *
 * @param block A DSL block using [JsonObjectBuilder] to define the structure
 *              and contents of the JSON object. Keys can be associated
 *              with strings, numbers, booleans, or other nested JSON values.
 * @return A [Json] object representing the JSON structure defined in the DSL block.
 * @since 3.3.0
 */
fun buildJson(block: ReceiverConsumer<JsonObjectBuilder>): Json =
    JsonObjectBuilder().apply(block).build().toJson()

/**
 * Creates a JSON object using the provided block of operations defined on a [JsonObjectBuilder].
 *
 * This function provides a DSL for building JSON objects in a structured manner. The [block]
 * parameter defines the operations to populate the [JsonObjectBuilder] with key-value pairs,
 * which can represent strings, numbers, booleans, nested objects, or null values.
 *
 * @param block A lambda with a receiver of type [JsonObjectBuilder], specifying the operations
 *              for constructing the JSON object.
 * @return A constructed [JsonObject] containing all the key-value pairs defined in the builder.
 * @since 3.3.0
 */
fun jsonObject(block: ReceiverConsumer<JsonObjectBuilder>) =
    JsonObjectBuilder().apply(block).build()

/**
 * Constructs a `JsonArray` using a DSL-style block for defining its elements.
 *
 * This function initializes a `JsonArrayBuilder`, applies the provided block to add
 * elements to the builder, and then builds the resulting `JsonArray`.
 *
 * @param block A lambda with receiver that defines the elements of the JSON array.
 *              The receiver is an instance of `JsonArrayBuilder`, allowing DSL-style
 *              construction of JSON elements.
 * @return The constructed `JsonArray` containing the elements added within the block.
 * @since 3.3.0
 */
fun jsonArray(block: ReceiverConsumer<JsonArrayBuilder>) =
    JsonArrayBuilder().apply(block).build()

/**
 * Constructs a JSON array using a DSL-friendly builder.
 *
 * This function leverages the [JsonArrayBuilder] to create a JSON array.
 * The provided lambda [block] is applied to the builder to add elements
 * in a DSL-like manner. Once all elements are defined, the JSON array
 * is built and converted to a [Json] representation.
 *
 * @param block A lambda with receiver defining the operations to populate
 * the JSON array using the [JsonArrayBuilder].
 * @return A [Json] object representing the constructed JSON array.
 * @since 3.3.0
 */
fun buildJsonArray(block: ReceiverConsumer<JsonArrayBuilder>): Json =
    JsonArrayBuilder().apply(block).build().toJson()

/**
 * Creates and returns a [JsonObject] by executing the specified [block] on a [JsonObjectBuilder].
 *
 * This function is a DSL construct that allows defining nested JSON objects in a structured and readable
 * manner. The [block] parameter is a lambda with a receiver of type [JsonObjectBuilder], used to build
 * the key-value pairs of the resultant JSON object.
 *
 * @param block A lambda function with a [JsonObjectBuilder] receiver to define the structure
 *              of the JSON object.
 * @return A [JsonObject] containing the key-value pairs defined in the [block].
 * @since 3.3.0
 */
fun JsonObjectBuilder.obj(block: ReceiverConsumer<JsonObjectBuilder>): JsonObject = jsonObject(block)

/**
 * Adds an array to the JSON object being constructed, using a DSL-style block to define the array's contents.
 *
 * @param block A lambda with receiver of type [JsonArrayBuilder], used to define the contents
 *              of the JSON array.
 * @return The created [JsonArray], representing the array added to the JSON object.
 * @since 3.3.0
 */
fun JsonObjectBuilder.array(block: ReceiverConsumer<JsonArrayBuilder>): JsonArray = jsonArray(block)