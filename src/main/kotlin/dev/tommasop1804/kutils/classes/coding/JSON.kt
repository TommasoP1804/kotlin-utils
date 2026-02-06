package dev.tommasop1804.kutils.classes.coding

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.Beta
import dev.tommasop1804.kutils.classes.constants.SortDirection
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.exceptions.UnsupportedJSONTypeException
import org.intellij.lang.annotations.Language
import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.node.ObjectNode
import tools.jackson.module.kotlin.KotlinModule
import java.io.File
import java.nio.file.Path


/**
 * Represents a JSON object with various utilities for parsing, serializing, and converting JSON strings.
 *
 * @property value The string representation of the JSON.
 * @property length The length of the JSON string.
 * @property pretty Indicates whether the JSON string is formatted in a human-readable way.
 * @property isArray Indicates whether the JSON represents a JSON array.
 * @property isObject Indicates whether the JSON represents a JSON object.
 * @property fieldsNames A list of field names, if the JSON represents an object.
 * @property fields The key-value pairs of the JSON object, if applicable.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = JSON.Companion.Serializer::class)
@JsonDeserialize(using = JSON.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = JSON.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = JSON.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_collection_declaration", "kutils_getorthrow_as_invoke", "RedundantSuppression")
class JSON private constructor(@param:Language("json") override val value: String) : CharSequence, Code(value, dev.tommasop1804.kutils.classes.coding.Language.JSON) {

    /**
     * Represents the length of the underlying string value.
     *
     * Provides the total number of characters in the string encapsulated by this class.
     *
     * @return the total number of characters in the string.
     * @since 1.0.0
     */
    override val length: Int
        get() = value.length

    /**
     * Provides a prettified JSON representation of the underlying value.
     * This lazily computed property formats the JSON in a human-readable way
     * with appropriate indentation and spacing.
     *
     * @property pretty The prettified JSON output based on the current value.
     * @receiver The object containing the raw JSON value to be prettified.
     * @since 1.0.0
     */
    val pretty: JSON
        get() = JSON(prettify(value))

    /**
     * Indicates whether the provided JSON value represents an array.
     * This property validates the JSON structure and determines whether it is an array type.
     *
     * @return `true` if the JSON value is an array, `false` otherwise.
     * @since 1.0.0
     */
    val isArray: Boolean
        get() = MAPPER.readTree(value).isArray

    /**
     * A computed property that determines whether the parsed JSON value is an object.
     *
     * This property uses a JSON parser to evaluate the provided `value` and checks
     * if it represents a JSON object. It returns `true` if the parsed value is
     * an object, otherwise `false`.
     *
     * @return `true` if the JSON value is an object, `false` otherwise.
     * @since 1.0.0
     */
    val isObject: Boolean
        get() = MAPPER.readTree(value).isObject

    /**
     * A set of field names extracted from a JSON structure represented by the `value` property.
     * The field names are determined using a JSON mapper to parse the `value`.
     *
     * This property uses lazy evaluation to read and extract the field names as a set.
     *
     * @return A set of strings representing the field names in the parsed JSON.
     * @since 1.0.0
     */
    val fieldsNames: StringSet
        get() = MAPPER.readTree(value).propertyNames().toSet()
    
    /**
     * Extracts all fields from a JSON node and maps them into a list of key-value pairs.
     * The keys are represented as strings, and the corresponding values are represented
     * as JSON nodes.
     *
     * This property leverages the Jackson library to parse JSON content, providing
     * a convenient way to access and iterate over all key-value pairs in a JSON structure.
     *
     * @return A list of pairs where each pair consists of a field name (key) and its associated
     * JSON value as a Jackson `JsonNode`.
     * @since 1.0.0
     */
    val fields
        get() = MAPPER.readTree(value).properties().toList().mappedTo { it.key.toString() to it.value!! }

    /**
     * A property that generates a JSON string representation of the map after
     * filtering out all entries where the value is null.
     *
     * The property performs the following operations in sequence:
     * - Converts the object to a mutable map.
     * - Checks and retrieves the map using a `getOrThrow` function to ensure validity.
     * - Filters out all entries with null values.
     * - Converts the filtered map into a JSON string.
     *
     * @return A JSON string representation of the non-null entries in the map.
     * @since 1.0.0
     */
    val withoutNulls
        get() = toMap<Any?>().getOrThrow().removeNullsRecursively().toJSON()

    /**
     * Secondary constructor that initializes an instance using a `Code` object.
     * It internally delegates to the primary constructor with the code's value.
     *
     * @param code The `Code` object containing the value and the language information.
     *
     * @throws dev.tommasop1804.kutils.exceptions.ExpectationMismatchException if the `Code` object does not have a language equal to `Language.JSON`.
     *
     * @since 1.0.0
     */
    constructor(code: Code) : this(code.value) {
        code.length.expect(dev.tommasop1804.kutils.classes.coding.Language.JSON)
    }

    /**
     * Constructs an instance by parsing the given JSON input. The input is validated and converted
     * to a JSON string representation using the specified mapper. If the input is malformed,
     * a `MalformedInputException` is thrown.
     *
     * @param json The input JSON as a character sequence to be parsed and validated.
     *
     * @throws MalformedInputException If the provided JSON input is invalid or malformed.
     *
     * @since 1.0.0
     */
    constructor(@Language("json") json: CharSequence) : this(
        tryOrThrow({ -> MalformedInputException(JSON::class) }) {
            MAPPER.writeValueAsString(MAPPER.readTree(json.toString()))
    })

    /**
     * Creates an instance by reading the content of the specified file and passing it as a
     * parameter to the primary constructor.
     *
     * @param file The file whose content will be read and used to initialize the instance.
     * @since 1.0.0
     */
    constructor(file: File) : this(file.readText())
    /**
     * Creates an instance by reading the content of the specified file and passing it as a
     * parameter to the primary constructor.
     *
     * @param path The path of the file whose content will be read and used to initialize the instance.
     * @since 1.0.0
     */
    constructor(path: Path) : this(path.toFile().readText())

    init {
        tryOrThrow({ -> MalformedInputException(JSON::class) }) {
            MAPPER.readTree(value)
        }
    }

    companion object {
        val MAPPER = JsonMapper().rebuild()
            .addModule(KotlinModule.Builder().build())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false)
            .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build()!!

        val OLD_MAPPER = ObjectMapper()
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_SELF_REFERENCES, false)
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(JavaTimeModule())!!

        /**
         * Represents an empty JSON object as a string literal.
         *
         * Can be used as a placeholder or default value in contexts where an empty JSON
         * representation is required.
         *
         * @since 1.0.0
         */
        val EMPTY_JSON = JSON("{}")
        /**
         * An immutable representation of an empty JSON array.
         *
         * This constant provides a string representation of an empty JSON array ("[]").
         * It can be used wherever an empty JSON array placeholder is needed.
         *
         * @since 1.0.0
         */
        val EMPTY_JSON_ARRAY = JSON("[]")
        /**
         * A regular expression used as the default separator for splitting strings.
         *
         * This regex matches a single period (`.`), often used to split strings
         * based on a dot delimiter.
         *
         * @since 1.0.0
         */
        val DEFAULT_SEPARATOR = Regex("\\.")

        /**
         * Checks if the string is a valid JSON object or array.
         *
         * This function attempts to parse the string as JSON and verifies
         * whether the parsed result is a JSON object or a JSON array.
         *
         * @receiver The string to be checked for validity as JSON.
         * @return `true` if the string is a valid JSON object or array, `false` otherwise.
         * @since 1.0.0
         */
        fun String.isValidJSON() =  try {
            val node = MAPPER.readTree(this)
            node.isObject || node.isArray
        } catch (_: Exception) {
            false
        }

        /**
         * Formats a given JSON string into a human-readable, pretty-printed format.
         *
         * @param json The JSON string to be pretty-printed. It must be a valid JSON string.
         * @return A formatted JSON string with indentation and line breaks to improve readability.
         * @since 1.0.0
         */
        fun prettify(@Language("json") json: String) = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(MAPPER.readTree(json))!!

        /**
         * Converts the string into a JSON object representation.
         * The method attempts to parse the string as JSON and returns the result
         * encapsulated within a `Result` object. If the parsing fails, the exception
         * is captured in the `Result` object.
         *
         * @receiver The string to be converted into a JSON object.
         * @return A `Result` containing the parsed JSON object or an exception if parsing fails.
         * @since 1.0.0
         */
        fun String.toJSON() = runCatching { JSON(this) }
        /**
         * Converts a YAML object to its JSON representation.
         *
         * The method attempts to transform the current YAML object into a JSON object.
         * Any exceptions encountered during the conversion process are encapsulated
         * within a [Result] instance.
         *
         * @receiver The YAML object to be converted to JSON.
         * @return A [Result] containing the JSON representation if the conversion succeeds,
         *         or encapsulating the exception if it fails.
         * @since 1.0.0
         */
        @JvmName("yamlToJson")
        @OptIn(Beta::class)
        fun YAML.toJSON(): JSON {
            if (isBlank()) return EMPTY_JSON
            if (isObject) return toDataMap()().toJSON()
            if (isArray) return toList<Any>()().toJSON()
            val obj = toObject<Any>()()
            return obj.toJSON()
        }
        /**
         * Converts the given object to a JSON representation using a predefined object mapper.
         *
         * This method serializes the object into a JSON string and wraps it in a `JSON` instance.
         *
         * @receiver The object to be converted into JSON
         * @return A `JSON` instance containing the serialized JSON string.
         * @since 1.0.0
         */
        @JvmName("anyToJson")
        fun Any.toJSON() = JSON(MAPPER.writeValueAsString(this))

        /**
         * Converts a JSON string into a formatted, pretty-printed JSON string.
         * This method parses the input string as JSON and ensures that the output
         * is easy to read with appropriate indentation.
         *
         * If the input string is not a valid JSON, the method will return a failed
         * [Result], encapsulating the error encountered during the parsing process.
         *
         * @receiver A string representation of a JSON object or array.
         * @return A [Result] containing the pretty-printed JSON string, or an error
         * if the input string is not a valid JSON.
         * @since 1.0.0
         */
        fun String.toPrettyJSON() = runCatching { JSON(this).pretty }
        /**
         * Converts a YAML object to its JSON representation.
         *
         * The method attempts to transform the current YAML object into a JSON object.
         *
         * @receiver The YAML object to be converted to JSON.
         * @return JSON representation
         * @since 1.0.0
         */
        @JvmName("yamlToPrettyJson")
        @OptIn(Beta::class)
        fun YAML.toPrettyJSON(): JSON {
            if (isBlank()) return JSON.EMPTY_JSON
            val obj = toObject<Any>()
            return obj.toJSON().pretty
        }
        /**
         * Converts the given object to a JSON string formatted with indentation for better readability.
         * This method utilizes a predefined JSON mapper with a pretty printing feature.
         *
         * @receiver The object to be serialized into a pretty-printed JSON string. If the receiver is null,
         *           the resulting JSON representation will handle it appropriately.
         * @return A formatted JSON string representation of the object.
         * @since 1.0.0
         */
        @JvmName("anyToPrettyJson")
        fun Any.toPrettyJSON() = JSON(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(this))

        /**
         * Reads a JSON file from the specified file path and maps its content to an object of the specified type.
         *
         * The function deserializes the JSON content using the provided class type and returns the corresponding object instance.
         *
         * @param T The type of the object to map the JSON content to.
         * @param file The file containing the JSON data.
         * @return An object of type [T] representing the deserialized JSON data, wrapped in a [Result].
         * @since 1.0.0
         */
        inline fun <reified T> readFromFile(file: File): Result<T> =
            runCatching { MAPPER.readValue(file, T::class.java) }

        /**
         * Reads a JSON file from the specified file path and deserializes its contents
         * into a list of objects of the specified type.
         *
         * @param file The JSON file to be read.
         * @return A list of objects of the specified type deserialized from the JSON file, wrapped in a [Result].
         * @since 1.0.0
         */
        inline fun <reified T> readArrayFromFile(file: File): Result<Array<T>> = runCatching {
            readListFromFile<T>(file).getOrThrow().toTypedArray()
        }

        /**
         * Reads a JSON file from the specified file path and deserializes its contents
         * into a list of objects of the specified type.
         *
         * @param file The JSON file to be read.
         * @return A list of objects of the specified type deserialized from the JSON file, wrapped in a [Result].
         * @since 1.0.0
         */
        inline fun <reified T> readListFromFile(file: File): Result<List<T>> = runCatching {
            MAPPER.readValue(file, MAPPER.typeFactory.constructCollectionType(List::class.java, T::class.java))
        }

        /**
         * Reads a JSON file from the specified file path and deserializes its contents
         * into a set of objects of the specified type.
         *
         * @param file The JSON file to be read.
         * @return A set of objects of the specified type deserialized from the JSON file, wrapped in a [Result].
         * @since 1.0.0
         */
        inline fun <reified T> readSetFromFile(file: File): Result<Set<T>> = runCatching {
            MAPPER.readValue(file, MAPPER.typeFactory.constructCollectionType(Set::class.java, T::class.java))
        }

        /**
         * Reads a JSON file and deserializes its content into a `Map` with `String` keys and values of a generic type.
         *
         * @param file The JSON file to be read
         * @return a map containing keys as `String` and values as the generic type `T` parsed from the JSON file, wrapped in a [Result]
         * @since 1.0.0
         */
        fun <T> readMapFromFile(file: File): Result<Map<String, T>> = runCatching {
            MAPPER.readValue(file, object : TypeReference<Map<String, T>>() {})
        }

        /**
         * Converts a [JsonNode] to a list of elements of type [T].
         * The conversion is performed based on the specified type [T].
         *
         * @param T the type of elements to be extracted from the [JsonNode].
         * @param force a flag indicating whether to force conversion
         *              of unsupported types using the object mapper.
         *              Defaults to `false`.
         * @return a [Result] containing the list of elements of type [T] if conversion is successful,
         *         or a failure if an error occurs.
         * @since 1.0.0
         */
        inline fun <reified T> JsonNode.asList(force: Boolean = false): Result<List<T>> = runCatching {
            val list = emptyMList<T>()
            if (isArray) {
                for (node in this) {
                    when(T::class) {
                        Int::class -> list.add(node.asInt() as T)
                        Long::class -> list.add(node.asLong() as T)
                        Double::class -> list.add(node.asDouble() as T)
                        Boolean::class -> list.add(node.asBoolean() as T)
                        String::class -> list.add(node.asString() as T)
                        JsonNode::class -> list.add(node as T)
                        else -> if (force) list.add(MAPPER.treeToValue(node, T::class.java))
                            else throw UnsupportedJSONTypeException(T::class.simpleName)
                    }
                }
            }
            list.toList()
        }
        /**
         * Converts a JsonNode to a Result containing a Set of the specified type.
         *
         * If the JsonNode represents an array, each element is converted to the specified type using
         * appropriate deserialization. The conversion may fail for incompatible or invalid elements.
         *
         * @param force a flag indicating whether to force conversion
         *              of unsupported types using the object mapper.
         *              Defaults to `false`.
         * @return a Result containing a Set of elements of type T if the conversion is successful.
         *         If an error occurs during the conversion process, the Result contains the exception.
         * @since 1.0.0
         */
        inline fun <reified T> JsonNode.asSet(force: Boolean = false): Result<Set<T>> = runCatching {
            val set = emptyMSet<T>()
            if (isArray) {
                for (node in this) {
                    when(T::class) {
                        Int::class -> set.add(node.asInt() as T)
                        Long::class -> set.add(node.asLong() as T)
                        Double::class -> set.add(node.asDouble() as T)
                        Boolean::class -> set.add(node.asBoolean() as T)
                        String::class -> set.add(node.asString() as T)
                        else -> if (force) set.add(MAPPER.treeToValue(node, T::class.java))
                            else throw UnsupportedJSONTypeException(T::class.simpleName)
                    }
                }
            }
            set.toSet()
        }

        /**
         * Converts the current JsonNode into a JSON object by serializing it
         * into its string representation.
         *
         * @return a Result wrapping the JSON object, or an exception if the conversion fails.
         * @since 1.0.0
         */
        fun JsonNode.asJSON(): Result<JSON> = runCatching {
            JSON(MAPPER.writeValueAsString(this))
        }
        /**
         * Converts the current JsonNode into a JSON object by serializing it
         * into its string representation.
         *
         * @return a Result wrapping the JSON object, or an exception if the conversion fails.
         * @since 1.0.0
         */
        fun JsonNode.asPrettyJSON(): Result<JSON> = runCatching {
            JSON(MAPPER.writeValueAsString(this)).pretty
        }

        /**
         * Copies a field from the current JsonNode to the target ObjectNode, creating intermediate
         * structure if necessary, based on an array of field names and the specified index.
         *
         * @param target The ObjectNode where the field will be copied to.
         * @param fieldNames An array of strings representing the field path to navigate and copy.
         * @param index The current index in the fieldNames array being processed.
         * @since 1.0.0
         */
        @Suppress("kutils_null_check")
        internal fun JsonNode.copyField(target: ObjectNode, fieldNames: Array<String>, index: Int) {
            if (index >= fieldNames.size) return

            val currentField = fieldNames[index]
            val childNode = get(currentField) ?: return

            if (index == fieldNames.size - 1) target.set(currentField, childNode)
            else {
                var nextNode = target.get(currentField) as ObjectNode?
                if (nextNode == null) {
                    nextNode = MAPPER.createObjectNode()
                    target.set(currentField, nextNode)
                }
                childNode.copyField(nextNode, fieldNames, index + 1)
            }
        }

        /**
         * Checks if a JSON node contains the specified field path.
         *
         * The method traverses the JSON node hierarchy based on the provided field path,
         * separated by the specified regex separator. If all fields in the path exist
         * in the current hierarchy, it returns true; otherwise, it returns false.
         *
         * @param fieldPath The string representing the path of fields to look for in the JSON node, delimited by the separator.
         * @param regexSeparator The optional regular expression to split the field path. Defaults to `DEFAULT_SEPARATOR`.
         * @return True if the JSON node contains all the fields in the specified path, false otherwise.
         * @since 1.0.0
         */
        operator fun JsonNode.invoke(fieldPath: String, regexSeparator: Regex = DEFAULT_SEPARATOR): Boolean {
            val fields = fieldPath.split(regexSeparator)
            var current = this

            for (field in fields) {
                if (!current.has(field))
                    return false
                current = current.get(field)
            }
            return true
        }

        class Serializer : ValueSerializer<JSON>() {
            override fun serialize(value: JSON, gen: JsonGenerator, ctxt: SerializationContext) {
                val node = MAPPER.readTree(value.value)
                when {
                    node.isArray -> {
                        val arrayValue = value.toList<Any>().getOrThrow()
                        gen.writePOJO(arrayValue)
                    }
                    node.isObject -> {
                        val mapValue = value.toMap<Any>().getOrThrow()
                        gen.writePOJO(mapValue)
                    }
                    else -> gen.writeRaw(value.value)
                }
            }
        }

        class Deserializer : ValueDeserializer<JSON>() {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = JSON(p.objectReadContext().readTree<JsonNode>(p).toString())
        }

        class OldSerializer : JsonSerializer<JSON>() {
            override fun serialize(value: JSON, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: SerializerProvider) {
                val node = MAPPER.readTree(value.value)
                when {
                    node.isArray -> {
                        val arrayValue = value.toList<Any>().getOrThrow()
                        gen.writeObject(arrayValue)
                    }
                    node.isObject -> {
                        val mapValue = value.toMap<Any>().getOrThrow()
                        gen.writeObject(mapValue)
                    }
                    else -> gen.writeRaw(value.value)
                }
            }
        }

        class OldDeserializer : JsonDeserializer<JSON>() {
            override fun deserialize(p: com.fasterxml.jackson.core.JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): JSON = JSON(p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p).toString())
        }
    }

    /**
     * Retrieves the element at the specified index from the collection.
     *
     * @param index The position of the element to retrieve. Must be a valid index within the collection.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size).
     * @since 1.0.0
     */
    override operator fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this character sequence.
     *
     * @param startIndex the start index of the subsequence (inclusive).
     * @param endIndex the end index of the subsequence (exclusive).
     * @return a new character sequence that represents the specified subsequence.
     * @throws IndexOutOfBoundsException if the start or end index is out of bounds of the character sequence.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns a string representation of the object.
     * This implementation provides a representation of the internal `value`.
     *
     * @return the string representation of the object.
     * @since 1.0.0
     */
    override fun toString() = value

    /**
     * Converts the stored JSON string into an object of the specified type [T].
     *
     * This method uses the Jackson ObjectMapper to deserialize the JSON string
     * into a strongly-typed object of the given type [T]. It wraps the operation
     * in a [Result] to safely handle any exceptions that may occur during the
     * deserialization process.
     *
     * @return a [Result] containing the deserialized object of type [T] if
     * successful, or an exception if an error occurs.
     * @since 1.0.0
     */
    inline fun <reified T> toObject() = runCatching { MAPPER.readValue(value, T::class.java) as T }

    /**
     * Converts the content represented by the current instance into a typed array of the specified type [T].
     *
     * This method uses the `toTypedList` method to first construct a `List` of type [T],
     * and then converts it to a typed array. If any exception occurs during this process,
     * it is wrapped and returned as a `Result` object.
     *
     * @return a `Result` containing the typed array of type [T], or the exception if an error occurs.
     * @since 1.0.0
     */
    inline fun <reified T> toArray() = runCatching { toList<T>().getOrThrow().toTypedArray() }

    /**
     * Converts the underlying JSON string value into a strongly-typed list of objects of type [T].
     *
     * This function utilizes the Jackson ObjectMapper to deserialize the JSON representation into a Kotlin list.
     * It uses a reified generic parameter to resolve the type information at runtime.
     *
     * The result of the function is wrapped in a [Result], allowing for safe execution and error handling.
     * If the deserialization is successful, the resulting [List] is returned inside a successful [Result].
     * If any exception occurs, it is captured and returned as a failure [Result].
     *
     * @return a [Result] containing either the deserialized list of type [T] or an exception if deserialization fails.
     * @param T the type of objects contained in the resulting list.
     * @since 1.0.0
     */
    inline fun <reified T> toList() = runCatching {
        MAPPER.readValue(value, MAPPER.typeFactory.constructCollectionType(List::class.java, T::class.java)) as List<T>
    }

    /**
     * Converts the underlying JSON string value into a strongly-typed list of objects of type [T].
     *
     * This function utilizes the Jackson ObjectMapper to deserialize the JSON representation into a Kotlin list.
     * It uses a reified generic parameter to resolve the type information at runtime.
     *
     * The result of the function is wrapped in a [Result], allowing for safe execution and error handling.
     * If the deserialization is successful, the resulting [List] is returned inside a successful [Result].
     * If any exception occurs, it is captured and returned as a failure [Result].
     *
     * @return a [Result] containing either the deserialized list of type [T] or an exception if deserialization fails.
     * @param T the type of objects contained in the resulting list.
     * @since 1.0.0
     */
    inline fun <reified T> toMList() = runCatching { toList<T>().getOrThrow().toMList() }

    /**
     * Converts a JSON-formatted string into a typed [Set] of the specified type [T].
     *
     * This function attempts to deserialize the JSON representation stored in the `value` property
     * of the containing object into a [Set] of elements of type [T]. The operation is executed
     * using Jackson's `ObjectMapper` and its type construction utilities to ensure type safety.
     *
     * The result is wrapped in a [Result] object, providing a safe way to handle potential exceptions
     * that might occur during the deserialization process, such as malformed JSON or type mismatches.
     *
     * @param T The type of elements expected in the resulting [Set].
     * @return A [Result] containing the deserialized [Set] of [T] if successful, or an error if deserialization fails.
     * @since 1.0.0
     */
    inline fun <reified T> toSet() = runCatching {
        MAPPER.readValue(value, MAPPER.typeFactory.constructCollectionType(Set::class.java, T::class.java)) as Set<T>
    }

    /**
     * Converts a JSON-formatted string into a typed [Set] of the specified type [T].
     *
     * This function attempts to deserialize the JSON representation stored in the `value` property
     * of the containing object into a [Set] of elements of type [T]. The operation is executed
     * using Jackson's `ObjectMapper` and its type construction utilities to ensure type safety.
     *
     * The result is wrapped in a [Result] object, providing a safe way to handle potential exceptions
     * that might occur during the deserialization process, such as malformed JSON or type mismatches.
     *
     * @param T The type of elements expected in the resulting [Set].
     * @return A [Result] containing the deserialized [Set] of [T] if successful, or an error if deserialization fails.
     * @since 1.0.0
     */
    inline fun <reified T> toMSet() = runCatching { toSet<T>().getOrThrow().toMSet() }

    /**
     * Converts the JSON representation of the current value into a strongly-typed [Map] with [String] keys
     * and values of type [V].
     *
     * The function leverages the Jackson library to deserialize the JSON content into a Kotlin [Map], where
     * the value's type is determined at runtime using reified type parameters.
     *
     * The operation is encapsulated in a [Result], allowing callers to handle potential exceptions,
     * such as deserialization errors or type mismatches.
     *
     * @param V The type of the values in the resulting [Map].
     * @return A [Result] containing the deserialized [Map] on success, or an exception on failure.
     * @since 1.0.0
     */
    inline fun <reified V> toMap(): Result<Map<String, V>> = runCatching {
        MAPPER.readValue(value, object : TypeReference<Map<String, V>>() {}) as Map<String, V>
    }

    /**
     * Converts the current value to a DataMap instance using a JSON mapper.
     *
     * @return a [Result] containing the [DataMap] if the conversion is successful, or an error if it fails.
     * @since 1.0.0
     */
    fun toDataMap(): Result<DataMap> = runCatching {
        MAPPER.readValue(value, object : TypeReference<DataMap>() {}) as DataMap
    }
    /**
     * Converts the current value to a DataMapNN instance using a JSON mapper.
     *
     * @return a [Result] containing the [DataMapNN] if the conversion is successful, or an error if it fails.
     * @since 1.0.0
     */
    fun toDataMapNN(): Result<DataMapNN> = runCatching {
        MAPPER.readValue(value, object : TypeReference<DataMapNN>() {}) as DataMapNN
    }

    /**
     * Converts the JSON representation of the current value into a strongly-typed [Map] with [String] keys
     * and values of type [V].
     *
     * The function leverages the Jackson library to deserialize the JSON content into a Kotlin [Map], where
     * the value's type is determined at runtime using reified type parameters.
     *
     * The operation is encapsulated in a [Result], allowing callers to handle potential exceptions,
     * such as deserialization errors or type mismatches.
     *
     * @param V The type of the values in the resulting [Map].
     * @return A [Result] containing the deserialized [Map] on success, or an exception on failure.
     * @since 1.0.0
     */
    inline fun <reified V> toMMap(): Result<MMap<String, V>> = runCatching { toMap<V>().getOrThrow().toMMap() }

    /**
     * Converts the current value to a DataMMap instance using a JSON mapper.
     *
     * @return a [Result] containing the [DataMMap] if the conversion is successful, or an error if it fails.
     * @since 1.0.0
     */
    fun toDataMMap(): Result<DataMMap> = runCatching {
        MAPPER.readValue(value, object : TypeReference<DataMMap>() {}) as DataMMap
    }
    /**
     * Converts the current value to a DataMMapNN instance using a JSON mapper.
     *
     * @return a [Result] containing the [DataMMapNN] if the conversion is successful, or an error if it fails.
     * @since 1.0.0
     */
    fun toDataMMapNN(): Result<DataMMapNN> = runCatching {
        MAPPER.readValue(value, object : TypeReference<DataMMapNN>() {}) as DataMMapNN
    }

    /**
     * Writes the current object to the specified file in JSON format using the configured mapper.
     *
     * @param file The file to which the object should be written. The file must be writable and accessible.
     * @since 1.0.0
     */
    fun writeToFile(file: File) = MAPPER.writeValue(file, MAPPER.readTree(value))

    /**
     * Invokes the operator function to retrieve a nested value from a JSON tree using a key path.
     *
     * @param keyPath The string representing the nested path to the desired value, separated by the specified regex separator.
     * @param regexSeparator The regex used to split the key path into individual keys. If null mapped to `DEFAULT_SEPARATOR`.
     * @return The nested JSON node at the specified key path, or `null` if the path does not exist.
     * @since 1.0.0
     */
    operator fun get(keyPath: String, regexSeparator: Regex = DEFAULT_SEPARATOR): JsonNode? {
        var node = MAPPER.readTree(value)
        for (key in keyPath.split(regexSeparator))
            if (node.has(key)) node = node.get(key) else return null
        return node
    }

    /**
     * Finds and returns a list of JSON nodes from the current value where the specified property matches the given value.
     *
     * This method searches through the JSON object or array represented by the current instance,
     * attempting to identify elements that contain a property with the given key matching the specified value.
     * The comparison considers various data types including number, string, boolean, null, and array.
     *
     * @param key the property name to look for in the JSON nodes.
     * @param value the value to match against the property's value. The method handles equality checks for
     *              numbers, strings, booleans, nulls, and arrays.
     * @return a [Result] containing a list of matched JSON nodes if successful, or an exception if an error occurs.
     * @since 1.0.0
     */
    fun findByPropertyValue(key: String, value: Any?) = runCatching {
        val node = MAPPER.readTree(this.value)
        val result = mutableListOf<JsonNode>()
        node.forEach {
            if (it.has(key)) when {
                it.get(key).isNumber && value is Number -> if (it.get(key).asDouble() == value.toDouble()) result.add(it)
                it.get(key).isString && value is String -> if (it.get(key).asString() == value) result.add(it)
                it.get(key).isBoolean && value is Boolean -> if (it.get(key).asBoolean() == value) result.add(it)
                it.get(key).isNull && value.isNull() -> result.add(it)
                it.get(key).isArray && (value is List<*> || value is Array<*>) -> if (it.get(key).toList() == value) result.add(it)
            }
        }
        result.toList()
    }

    /**
     * Searches for and retrieves all JSON elements that match the given property path and value
     * in a nested JSON structure. The function traverses the JSON hierarchy based on the provided
     * path and performs value comparisons to locate matching elements.
     *
     * @param keyPath The path to the desired property in the JSON structure. The path segments
     *                are separated using the defined delimiter in `regexSeparator`.
     * @param regexSeparator A regular expression used to split the `keyPath` into segments
     *                       representing the traversal hierarchy. Defaults to `DEFAULT_SEPARATOR`.
     * @param value The value to match against in the JSON property. The type of the provided value
     *              determines the comparison logic applied.
     * @since 1.0.0
     */
    fun findByPropertyValueFromNestedJSON(keyPath: String, regexSeparator: Regex = DEFAULT_SEPARATOR, value: Any?) = runCatching {
        val result = mutableListOf<JsonNode>()
        var node = MAPPER.readTree(this.value)
        var last = ""

        for (key in keyPath.split(regexSeparator)) {
            if (node.has(key)) node = node.get(key) else return@runCatching emptyList()
            last = key
        }
        node.forEach {
            if (it.has(last)) when {
                it.get(last).isNumber && value is Number -> if (it.get(last).asDouble() == value.toDouble()) result.add(it)
                it.get(last).isString && value is String -> if (it.get(last).asString() == value) result.add(it)
                it.get(last).isBoolean && value is Boolean -> if (it.get(last).asBoolean() == value) result.add(it)
                it.get(last).isNull && value.isNull() -> result.add(it)
                it.get(last).isArray && (value is List<*> || value is Array<*>) -> if (it.get(last).toList() == value) result.add(it)
            }
        }
        result.toList()
    }
    
    /**
     * Merges the current JSON object with another JSON object and returns a new JSON object
     * containing the combined data. The merging is performed by adding all the fields
     * from the provided JSON object to the current JSON object.
     *
     * @param other the JSON object to be merged with the current JSON
     * @return a new JSON object containing the merged data
     * @since 1.0.0
     */
    operator fun plus(other: JSON): JSON {
        val node1 = MAPPER.readTree(value)
        val node2 = MAPPER.readTree(other.value)
        (node1 as ObjectNode).setAll(node2 as ObjectNode?)
        return JSON(MAPPER.writeValueAsString(node1))
    }
    /**
     * Combines the current JSON object with the given Iterable of JSON objects,
     * merging their contents.
     *
     * @param others An iterable collection of JSON objects to merge with this JSON.
     * @return A new JSON object representing the merged contents of the initial JSON and the provided JSON objects.
     * @since 1.0.0
     */
    operator fun plus(others: Iterable<JSON>): JSON {
        var mergedJson = value
        others.forEach { mergedJson += it }
        return JSON(mergedJson)
    }
    /**
     * Adds a new field to a JSON object.
     *
     * @param field A pair consisting of the field name as a String and the field value as a JsonNode.
     * @since 1.0.0
     */
    operator fun plus(field: Pair<String, Any?>): JSON {
        val node = MAPPER.readTree(value)
        (node as ObjectNode).set(field.first, MAPPER.valueToTree(field.second))
        return JSON(MAPPER.writeValueAsString(node))
    }
    /**
     * Adds a nested field to the current JSON structure by navigating through the hierarchy
     * defined in the provided `nestedField` parameter. The function applies the changes
     * and returns a new JSON instance with the modifications.
     *
     * @param nestedField A triple containing the field path as a string, an optional regex for splitting the path,
     *                    and the JSON node to be added or updated in the structure.
     *                    - The first element is the field path string.
     *                    - The second element is a regex pattern (optional) used to split the path string.
     *                      If null, a default separator is applied.
     *                    - The third element is the object to be added at the specified nested field path.
     * @return Result encapsulating the new JSON instance with the updated structure.
     * @since 1.0.0
     */
    operator fun plus(nestedField: Triple<String, Regex?, Any?>): JSON {
        val rootNode = MAPPER.readTree(value)
        var current = rootNode as ObjectNode
        val fieldNames = nestedField.first.split(nestedField.second ?: DEFAULT_SEPARATOR)

        for (i in 0 until fieldNames.size - 1) {
            val currentField = fieldNames[i]
            if (!current.has(currentField))
                current.set(currentField, MAPPER.createObjectNode())
            current = current.get(currentField) as ObjectNode
        }
        current.set(fieldNames.last(), MAPPER.valueToTree(nestedField.third))
        return JSON(MAPPER.writeValueAsString(current))
    }

    /**
     * Operator function to remove a specific field from a JSON object string.
     *
     * @param fieldName the name of the field to be removed from the JSON object.
     * @return a new JSON object string with the specified field removed, if successful.
     * @since 1.0.0
     */
    operator fun minus(fieldName: String): JSON {
        val node = MAPPER.readTree(value)
        (node as ObjectNode).remove(fieldName)
        return JSON(MAPPER.writeValueAsString(node))
    }

    /**
     * Removes a field specified by the given pair of field name and optional regex from the JSON structure.
     *
     * The field name string is split using the provided regex or a default separator if no regex is provided,
     * and the corresponding nested field is removed from the JSON object.
     *
     * @param field A pair where the first value is the field name string to remove (with optional nested structure)
     *              and the second value is an optional regex used for splitting the field name.
     * @return A new JSON object with the specified field removed.
     * @since 1.0.0
     */
    operator fun minus(field: Pair<String, Regex?>): JSON {
        val rootNode = MAPPER.readTree(value)
        var current = rootNode as ObjectNode
        val fieldNames = field.first.split(field.second ?: DEFAULT_SEPARATOR)

        for (i in 0 until fieldNames.size - 1)
            current = current.get(fieldNames[i]) as ObjectNode? ?: return JSON(MAPPER.writeValueAsString(rootNode))
        current.remove(fieldNames.last())
        return JSON(MAPPER.writeValueAsString(current))
    }
    
    /**
     * Filters the JSON object to only include the specified fields.
     *
     * @param fieldsToKeep The fields to retain in the filtered JSON object.
     * @since 1.0.0
     */
    fun filterFields(vararg fieldsToKeep: String) = runCatching {
        val node = MAPPER.readTree(value)
        val result = MAPPER.createObjectNode()
        fieldsToKeep.forEach { if (node.has(it)) result.set(it, node.get(it)) }
        JSON(MAPPER.writeValueAsString(result))
    }

    /**
     * Filters nested fields from a JSON object based on the specified field paths and returns the filtered JSON as a string.
     *
     * @param regexSeparator the delimiter used to split field paths into nested levels, defaulting to DEFAULT_SEPARATOR.
     * @param fieldsToKeep the list of field paths to retain in the filtered JSON object. Field paths should be specified as strings.
     * @since 1.0.0
     */
    fun filterNestedFields(regexSeparator: Regex = DEFAULT_SEPARATOR, vararg fieldsToKeep: String) = runCatching {
        val rootNode = MAPPER.readTree(value)
        val filteredNode = MAPPER.createObjectNode()

        for (fieldPath in fieldsToKeep)
            rootNode.copyField(filteredNode, fieldPath.split(regexSeparator).toTypedArray(), 0)

        MAPPER.writeValueAsString(filteredNode)
    }


    /**
     * Checks if the JSON field path exists in the given tree structure by traversing it,
     * using the specified regex separator or a default separator.
     *
     * @param fieldPath the string representing the path of the field to be checked
     * @param regexSeparator a custom regex separator to split the field path, or null to use the default separator
     * @return true if the field exists in the JSON structure, otherwise false
     * @since 1.0.0
     */
    operator fun invoke(fieldPath: String, regexSeparator: Regex = DEFAULT_SEPARATOR): Boolean {
        var node = MAPPER.readTree(value)
        val fieldNames = fieldPath.split(regexSeparator)

        for (i in 0 until fieldNames.size - 1) {
            if (!node.has(fieldNames[i])) return false
            node = node.get(fieldNames[i])
        }
        return node.has(fieldNames.last())
    }

    /**
     * Validates whether the JSON structure contains the expected fields and optionally checks if it is overly restrictive.
     *
     * @param expectedFields The list of field names that are expected to be present in the JSON structure.
     * @param restrictive When set to true, ensures the JSON contains only the expected fields; otherwise, allows additional fields.
     * @return `true` if the structure is valid according to the conditions, otherwise `false`.
     * @since 1.0.0
     */
    fun isValidStructure(vararg expectedFields: String, restrictive: Boolean = true): Boolean {
        val node = MAPPER.readTree(value)
        for (field in expectedFields)
            if (!node.has(field)) return false
        return if (restrictive)
            expectedFields.size == MAPPER.readTree(value).propertyNames().toSet().size
        else true
    }

    /**
     * Validates if the given JSON structure matches the expected nested fields based on a separator.
     *
     * @param regexSeparator The optional regular expression used to separate nested fields. Defaults to DEFAULT_SEPARATOR.
     * @param expectedFields The fields to be checked for existence within the JSON structure.
     * @return `true` if all expected fields are present in the JSON structure, `false` otherwise.
     * @since 1.0.0
     */
    fun isValidNestedStructure(regexSeparator: Regex = DEFAULT_SEPARATOR, vararg expectedFields: String): Boolean {
        val node = MAPPER.readTree(value)
        for (field in expectedFields)
            if (!node(field, regexSeparator)) return false
        return true
    }

    /**
     * Sorts the keys of the JSON object in the specified order.
     *
     * This method sorts the keys of the current JSON object either in ascending or descending order,
     * based on the provided `direction` parameter. If the JSON content is not an object, the original JSON is returned unchanged.
     *
     * @param direction The sorting direction, either ascending or descending. Default is `SortDirection.ASCENDING`.
     * @return A new instance of JSON with the keys of the object sorted as specified, or the original JSON if it is not an object.
     * @since 1.0.0
     */
    fun sortedKeys(direction: SortDirection = SortDirection.ASCENDING): JSON {
        val node = MAPPER.readTree(value)
        val sortedNode = sortKeysRecursively(node, direction)
        return JSON(MAPPER.writeValueAsString(sortedNode))
    }

    private fun sortKeysRecursively(node: JsonNode, direction: SortDirection): JsonNode {
        return when {
            node.isObject -> {
                val sortedNode = MAPPER.createObjectNode()
                val fields = node.properties().toMutableList()

                if (direction == SortDirection.ASCENDING) fields.sortBy { it.key }
                else fields.sortByDescending { it.key }

                for (entry in fields) {
                    val sortedValue = sortKeysRecursively(entry.value, direction)
                    sortedNode.set(entry.key, sortedValue)
                }
                sortedNode
            }
            node.isArray -> {
                val sortedArray = MAPPER.createArrayNode()
                node.forEach { element -> sortedArray.add(sortKeysRecursively(element, direction)) }
                sortedArray
            }
            else -> node
        }
    }
    
    /**
     * Checks if a JSON entity is empty. A JSON entity is considered empty if it is
     * either an empty object or an empty array.
     *
     * @return True if the JSON entity is empty, otherwise false.
     * @since 1.0.0
     */
    fun isEmptyJSON() = isEmptyObject() || isEmptyArray()

    /**
     * Negates the current state by checking if the JSON is empty.
     * This method is an operator function, meaning it can be used with the `!` operator.
     *
     * @return `true` if the JSON is empty; otherwise, `false`.
     * @since 1.0.0
     */
    operator fun not() = isEmptyJSON()

    /**
     * Checks if the object is an empty JSON object.
     *
     * This function compares the current value with a predefined constant `EMPTY_JSON`
     * to determine if the object is empty.
     *
     * @return `true` if the object represents an empty JSON, `false` otherwise.
     *
     * @since 1.0.0
     */
    fun isEmptyObject() =
        value.replace(" ", "").replace("\n", "").replace("\r", "").replace("\t", "").replace(" ", "") == EMPTY_JSON.value

    /**
     * Checks if the given value is an empty JSON array.
     *
     * This method evaluates whether the `value` matches the constant `EMPTY_JSON_ARRAY`.
     * Returns `true` if the value represents an empty JSON array, otherwise returns `false`.
     *
     * @return `true` if the value is an empty JSON array, otherwise `false`
     * @since 1.0.0
     */
    fun isEmptyArray() =
        value.replace(" ", "").replace("\n", "").replace("\r", "").replace("\t", "") == EMPTY_JSON_ARRAY.value

    /**
     * Checks if the current collection, string, or other applicable type is not empty.
     *
     * This function evaluates whether the invoking instance contains at least one element
     * or character, returning true if it does, otherwise false.
     *
     * @return true if the instance is not empty, false otherwise.
     * @since 1.0.0
     */
    fun isNotEmpty() = !isEmptyObject() && !isEmptyArray()
    
    /**
     * Determines whether the object is not empty.
     *
     * This method serves as a negation of the `isEmptyObject` method,
     * returning `true` if the object is not empty and `false` otherwise.
     *
     * @return `true` if the object is not empty, `false` otherwise.
     * @since 1.0.0
     */
    fun isNotEmptyObject() = !isEmptyObject()
    
    /**
     * Checks if the array is not empty. This function acts as the negation of `isEmptyArray`,
     * returning true if the array contains at least one element and false otherwise.
     *
     * @return `true` if the array is not empty, `false` otherwise.
     * @since 1.0.0
     */
    fun isNotEmptyArray() = !isEmptyArray()

    /**
     * Recursively removes all null values from a map structure, including nested objects and arrays.
     *
     * @return A new map with all null values removed recursively.
     * @since 1.0.0
     */
    private fun DataMap.removeNullsRecursively(): DataMap {
        return mappedToNotNull { (key, value) ->
            when (value) {
                null -> null
                is Map<*, *> -> {
                    @Suppress("UNCHECKED_CAST")
                    val cleanedMap = (value as DataMap).removeNullsRecursively()
                    if (cleanedMap.isNotEmpty()) key to cleanedMap else null
                }
                is List<*> -> {
                    val cleanedList = value.removeNullsFromList()
                    if (cleanedList.isNotEmpty()) key to cleanedList else null
                }
                is Array<*> -> {
                    val cleanedArray = value.removeNullsFromArray()
                    if (cleanedArray.isNotEmpty()) key to cleanedArray else null
                }
                else -> key to value
            }
        }.toMap()
    }

    /**
     * Recursively removes all null values from a list, including nested objects and arrays.
     *
     * @return A new list with all null values removed recursively.
     * @since 1.0.0
     */
    private fun List<*>.removeNullsFromList(): List<Any?> {
        return this.mapNotNull { item ->
            when (item) {
                null -> null
                is Map<*, *> -> {
                    @Suppress("UNCHECKED_CAST")
                    val cleanedMap = (item as DataMap).removeNullsRecursively()
                    cleanedMap.ifEmpty { null }
                }
                is List<*> -> {
                    val cleanedList = item.removeNullsFromList()
                    cleanedList.ifEmpty { null }
                }
                is Array<*> -> {
                    val cleanedArray = item.removeNullsFromArray()
                    if (cleanedArray.isNotEmpty()) cleanedArray else null
                }
                else -> item
            }
        }
    }

    /**
     * Recursively removes all null values from an array, including nested objects and arrays.
     *
     * @return A new array with all null values removed recursively.
     * @since 1.0.0
     */
    private fun Array<*>.removeNullsFromArray(): Array<Any?> {
        return this.mapNotNull { item ->
            when (item) {
                null -> null
                is Map<*, *> -> {
                    @Suppress("UNCHECKED_CAST")
                    val cleanedMap = (item as DataMap).removeNullsRecursively()
                    cleanedMap.ifEmpty { null }
                }
                is List<*> -> {
                    val cleanedList = item.removeNullsFromList()
                    cleanedList.ifEmpty { null }
                }
                is Array<*> -> {
                    val cleanedArray = item.removeNullsFromArray()
                    if (cleanedArray.isNotEmpty()) cleanedArray else null
                }
                else -> item
            }
        }.toTypedArray()
    }
}