/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.coding

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.networknt.schema.JsonSchemaFactory
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.collections.*
import dev.tommasop1804.kutils.classes.collections.NonEmptyList.Companion.toNonEmptyList
import dev.tommasop1804.kutils.classes.collections.NonEmptyMList.Companion.toNonEmptyMList
import dev.tommasop1804.kutils.classes.collections.NonEmptyMSet.Companion.toNonEmptyMSet
import dev.tommasop1804.kutils.classes.collections.NonEmptySet.Companion.toNonEmptySet
import dev.tommasop1804.kutils.classes.constants.*
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.classes.maps.*
import dev.tommasop1804.kutils.classes.maps.NonEmptyMMap.Companion.toNonEmptyMMap
import dev.tommasop1804.kutils.classes.maps.NonEmptyMap.Companion.toNonEmptyMap
import dev.tommasop1804.kutils.errors.*
import dev.tommasop1804.kutils.exceptions.*
import org.intellij.lang.annotations.Language
import org.jetbrains.exposed.v1.core.Table
import tools.jackson.core.JacksonException
import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.core.exc.StreamReadException
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.node.ArrayNode
import tools.jackson.databind.node.ObjectNode
import tools.jackson.module.kotlin.KotlinModule
import tools.jackson.module.kotlin.readValue
import java.io.File
import java.nio.file.Path
import kotlin.reflect.typeOf


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
 * @since 3.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Json.Companion.Serializer::class)
@JsonDeserialize(using = Json.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Json.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Json.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_collection_declaration", "kutils_getorthrow_as_invoke", "RedundantSuppression")
@MustUseReturnValues
open class Json private constructor(@param:Language("json") override val value: String) : CharSequence, Code(value, dev.tommasop1804.kutils.classes.coding.Language.Json) {

    /**
     * Represents the length of the underlying string value.
     *
     * Provides the total number of characters in the string encapsulated by this class.
     *
     * @return the total number of characters in the string.
     * @since 3.0.0
     */
    override val length: Int
        get() = value.length

    /**
     * Provides a prettified JSON representation of the underlying value.
     * This lazily computed property formats the JSON in a human-readable way
     * with appropriate indentation and spacing.
     *
     * @receiver The object containing the raw JSON value to be prettified.
     * @since 3.0.0
     */
    val pretty: Json
        get() = Json(prettify(value))

    /**
     * Indicates whether the provided JSON value represents an array.
     * This property validates the JSON structure and determines whether it is an array type.
     *
     * @return `true` if the JSON value is an array, `false` otherwise.
     * @since 3.0.0
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
     * @since 3.0.0
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
     * @since 3.0.0
     */
    val fieldsNames: Set<String>
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
     * @since 3.0.0
     */
    val fields
        get() = MAPPER.readTree(value).properties().toList().map { it.key.toString() to it.value!! }

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
     * @since 3.0.0
     */
    val withoutNulls
        get() = toMap<Any?>().getOrThrow().removeNullsRecursively().toJson()

    /**
     * Checks whether a JSON structure is considered empty.
     *
     * This property evaluates to true if the JSON structure is empty, either
     * as an object (`isEmptyObject`) or as an array (`isEmptyArray`).
     *
     * @return true if the JSON structure is empty, false otherwise.
     * @since 6.1.0
     */
    val isEmptyJson get() = isEmptyObject || isEmptyArray
    /**
     * Indicates whether the JSON object or structure is not empty.
     *
     * This property derives its value by negating the `isEmptyJson` condition.
     * It is useful for performing quick checks to ensure that
     * the JSON is not empty before processing.
     *
     * @see isEmptyJson
     * @since 6.1.0
     */
    val isNotEmptyJson get() = !isEmptyJson
    /**
     * Checks if the given JSON object is empty after removing all whitespace,
     * including spaces, newlines, carriage returns, and tabs. This property
     * applies transformations to the JSON object value and compares it to a
     * pre-defined empty JSON representation.
     *
     * @return true if the JSON object is considered empty, otherwise false.
     * @since 6.1.0
     */
    val isEmptyObject get() = value.replace(" ", "").replace("\n", "").replace("\r", "").replace("\t", "").replace(" ", "") == EMPTY_JSON.value
    /**
     * A computed property that evaluates to `true` if the current object is not empty,
     * and `false` otherwise. This is determined by checking the negation of `isEmptyObject`.
     *
     * @return `true` if the object is not empty, `false` otherwise.
     * @since 6.1.0
     */
    val isNotEmptyObject get() = !isEmptyObject
    /**
     * A computed property that checks if the `value` string corresponds to an empty JSON array after
     * removing all whitespace characters, including spaces, newlines, carriage returns, and tabs.
     * Returns `true` if the processed string matches the representation of an empty JSON array.
     *
     * @since 6.1.0
     */
    val isEmptyArray get() = value.replace(" ", "").replace("\n", "").replace("\r", "").replace("\t", "") == EMPTY_JSON_ARRAY.value
    /**
     * A read-only property indicating whether an array is not empty.
     * Returns `true` if the associated `isEmptyArray` property evaluates to `false`,
     * meaning the array contains one or more elements.
     *
     * @return Boolean value indicating the non-empty state of the array.
     * @since 6.1.0
     */
    val isNotEmptyArray get() = !isEmptyArray

    /**
     * Secondary constructor that initializes an instance using a `Code` object.
     * It internally delegates to the primary constructor with the code's value.
     *
     * @param code The `Code` object containing the value and the language information.
     *
     * @throws dev.tommasop1804.kutils.exceptions.ExpectationMismatchException if the `Code` object does not have a language equal to `Language.JSON`.
     *
     * @since 3.0.0
     */
    constructor(code: Code) : this(code.value) {
        code.language.expect(dev.tommasop1804.kutils.classes.coding.Language.Json)
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
     * @since 3.0.0
     */
    constructor(@Language("json") json: CharSequence) : this(
        tryOrThrow({ MalformedInputException("Input is not a valid JSON") }) {
            MAPPER.writeValueAsString(MAPPER.readTree(json.toString()))
    })

    /**
     * Creates an instance by reading the content of the specified file and passing it as a
     * parameter to the primary constructor.
     *
     * @param file The file whose content will be read and used to initialize the instance.
     * @since 3.0.0
     */
    constructor(file: File) : this(file.readText()) {
        file.exists().expect(true)
        file.isFile.expect(true)
        file.canRead().expect(true)
        file.extension.validate(file::extension, "file") { it equalsIgnoreCase "json" }
    }
    /**
     * Creates an instance by reading the content of the specified file and passing it as a
     * parameter to the primary constructor.
     *
     * @param path The path of the file whose content will be read and used to initialize the instance.
     * @since 3.0.0
     */
    constructor(path: Path) : this(path.toFile())

    init {
        tryOrThrow({ MalformedInputException(Json::class) }) {
            MAPPER.readTree(value)
        }
    }

    companion object {
        val MAPPER = JsonMapper().rebuild()
            .addModule(KotlinModule.Builder().build())
            .addModule(NonEmptyCollectionsModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false)
            .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build()!!

        val OLD_MAPPER = ObjectMapper()
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_SELF_REFERENCES, false)
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(JavaTimeModule())
            .registerModule(NonEmptyCollectionsOldModule())!!

        /**
         * Represents an empty JSON object as a string literal.
         *
         * Can be used as a placeholder or default value in contexts where an empty JSON
         * representation is required.
         *
         * @since 3.0.0
         */
        val EMPTY_JSON = Json("{}")
        /**
         * An immutable representation of an empty JSON array.
         *
         * This constant provides a string representation of an empty JSON array ("[]").
         * It can be used wherever an empty JSON array placeholder is needed.
         *
         * @since 3.0.0
         */
        val EMPTY_JSON_ARRAY = Json("[]")
        /**
         * A regular expression used as the default separator for splitting strings.
         *
         * This regex matches a single period (`.`), often used to split strings
         * based on a dot delimiter.
         *
         * @since 3.0.0
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
         * @since 3.8.1
         */
        fun String.isValidJson() =  try {
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
         * @since 3.0.0
         */
        fun prettify(@Language("json") json: String) = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(MAPPER.readTree(json))!!

        /**
         * Attempts to convert a File instance into a Json representation.
         *
         * This method encapsulates the conversion process within a functional "either" context,
         * allowing for safe error handling during the transformation. If the conversion fails,
         * an InvalidConversion instance is returned, containing details about the source and
         * target types, as well as the underlying exception.
         *
         * @return Either a successfully converted Json instance or an InvalidConversion error.
         * @since 6.1.0
         */
        fun File.toJson() = either {
            catching({ Json(this@toJson) }) { t: Throwable ->
                InvalidTypeConversion(this@toJson, typeOf<File>(), typeOf<Json>(), t)
            }
        }
        /**
         * Converts the invoking `Path` instance to a `Json` representation.
         * This method attempts to create a `Json` object using the `Path` as input.
         * If the conversion fails, an `InvalidConversion` error is returned, capturing
         * the source path, the target type (`Json::class`), and the exception that caused the failure.
         *
         * @return An `Either` containing the resulting `Json` object or an `InvalidConversion` error in case of failure.
         * @receiver The `Path` object to be converted to `Json`.
         * @since 6.1.0
         */
        fun Path.toJson() = either {
            catching({ Json(this@toJson) }) { t: Throwable ->
                InvalidTypeConversion(this@toJson, typeOf<Path>(), typeOf<Json>(), t)
            }
        }
        /**
         * Converts the receiver String into a JSON representation.
         *
         * Attempts to parse the receiver string as JSON and returns the result
         * encapsulated in an Either type. If the parsing is successful, it provides
         * the JSON object; otherwise, it supplies an error representation indicating
         * the invalid format.
         *
         * @receiver The String to be parsed into JSON.
         * @return An Either containing the parsed JSON object on success, or an
         *         InvalidFormat error if parsing fails.
         * @since 6.1.0
         */
        fun @receiver:Language("json") String.toJson() = either {
            catching({ Json(this@toJson) }) { t: Throwable ->
                InvalidTypeFormat(this@toJson, typeOf<Json>(), t)
            }
        }
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
         * @since 3.0.0
         */
        @JvmName("yamlToJson")
        fun Yaml.toJson(): Json {
            if (isBlank()) return EMPTY_JSON
            if (isObject) return toDataMap()().toJson()
            if (isArray) return toList<Any>()().toJson()
            val obj = toObject<Any>()()
            return obj.toJson()
        }
        /**
         * Converts the current TOML instance into its JSON representation.
         *
         * This method processes the TOML structure and transforms it into a JSON format,
         * providing an equivalent representation of the data.
         *
         * @return the JSON representation of the TOML instance. If the TOML is blank,
         * it returns an empty JSON object.
         * @since 3.11.0
         */
        @JvmName("tomlToJson")
        @OptIn(Beta::class)
        fun Toml.toJson(): Json {
            if (isBlank()) return EMPTY_JSON
            return toDataMap()().toJson()
        }
        /**
         * Converts the current CSV instance into its JSON representation
         * (a JSON array of objects, where each row becomes an object keyed by headers).
         *
         * @return The JSON representation of the CSV instance. If the CSV is blank,
         * it returns an empty JSON array.
         * @since 3.13.0
         */
        @JvmName("csvToJson")
        @OptIn(Beta::class)
        fun Csv.toJson(): Json {
            if (isBlank()) return EMPTY_JSON_ARRAY
            val rows = toListOfMaps()
            if (rows.isEmpty()) return EMPTY_JSON_ARRAY
            return (if (!hasHeaders && rows.all { it.keys.size == 1 }) rows.map { it.values.first() } else rows).toJson()
        }
        /**
         * Converts the given object to a JSON representation using a predefined object mapper.
         *
         * This method serializes the object into a JSON string and wraps it in a `JSON` instance.
         *
         * @receiver The object to be converted into JSON
         * @return A `JSON` instance containing the serialized JSON string.
         * @since 3.0.0
         */
        @JvmName("anyToJson")
        fun Any.toJson() = Json(MAPPER.writeValueAsString(this))

        /**
         * Converts the contents of the file to a prettified JSON string representation.
         *
         * @receiver The File instance whose contents are to be converted.
         * @return A Result containing the prettified JSON string if the operation is successful,
         * or the exception if an error occurs during processing.
         * @since 3.13.0
         */
        fun File.toPrettyJson() = toJson().map(Json::pretty)
        /**
         * Converts the content of the specified file path to a formatted JSON string.
         *
         * This function reads the file content using the provided path, parses it as JSON,
         * and returns a human-readable, indented JSON string. It uses `runCatching` to
         * safely handle exceptions that may occur during file reading or parsing.
         *
         * @receiver The file path pointing to the JSON file to be formatted.
         * @return The formatted JSON string if the operation is successful.
         *         Otherwise, the function captures and handles any exceptions internally.
         * @since 3.13.0
         */
        fun Path.toPrettyJson() = toJson().map(Json::pretty)
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
         * @since 3.0.0
         */
        fun @receiver:Language("json") String.toPrettyJson() = toJson().map(Json::pretty)
        /**
         * Converts a YAML object to its JSON representation.
         *
         * The method attempts to transform the current YAML object into a JSON object.
         *
         * @receiver The YAML object to be converted to JSON.
         * @return JSON representation
         * @since 3.0.0
         */
        @JvmName("yamlToPrettyJson")
        fun Yaml.toPrettyJson(): Json {
            if (isBlank()) return EMPTY_JSON
            val obj = toObject<Any>()
            return obj.toJson().pretty
        }
        /**
         * Converts the current TOML object to a pretty-printed JSON string.
         *
         * This method transforms the TOML data structure into its equivalent JSON representation
         * and formats the output with indentation for readability.
         *
         * @receiver Toml The TOML object to be converted.
         * @return A pretty-printed JSON string representation of the TOML object.
         * @since 3.13.0
         */
        @JvmName("tomlToPrettyJson")
        @OptIn(Beta::class)
        fun Toml.toPrettyJson() = toJson().pretty
        /**
         * Converts the `Csv` object to a well-formatted, pretty-printed JSON string.
         *
         * This method uses the `toJson` function to transform the `Csv` object
         * into its JSON representation and formats it with indentation
         * for better readability.
         *
         * The method applies certain experimental or beta-level functionality,
         * as it is annotated with the `@OptIn(Beta::class)` annotation.
         *
         * @receiver The `Csv` object to be converted and formatted.
         * @return A string containing the pretty-printed JSON representation of the `Csv` object.
         * @since 3.13.0
         */
        @JvmName("csvToPrettyJson")
        @OptIn(Beta::class)
        fun Csv.toPrettyJson() = toJson().pretty
        /**
         * Converts the given object to a JSON string formatted with indentation for better readability.
         * This method utilizes a predefined JSON mapper with a pretty printing feature.
         *
         * @receiver The object to be serialized into a pretty-printed JSON string. If the receiver is null,
         *           the resulting JSON representation will handle it appropriately.
         * @return A formatted JSON string representation of the object.
         * @since 3.0.0
         */
        @JvmName("anyToPrettyJson")
        fun Any.toPrettyJson() = Json(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(this))

        /**
         * Reads a JSON file from the specified file path and maps its content to an object of the specified type.
         *
         * The function deserializes the JSON content using the provided class type and returns the corresponding object instance.
         *
         * @param T The type of the object to map the JSON content to.
         * @param file The file containing the JSON data.
         * @return An object of type [T] representing the deserialized JSON data, wrapped in a [Result].
         * @since 3.0.0
         */
        inline fun <reified T> readFromFile(file: File): Either<DeserializationError, T> = either {
            catching({ MAPPER.readValue(file, T::class.java) }) { e: Exception -> when (e) {
                is StreamReadException -> DeserializationError.ReadError(typeOf<T>(), e)
                is DatabindException -> DeserializationError.MappingError(typeOf<T>(), e)
                is JacksonException -> DeserializationError(typeOf<T>(), e)
                else -> throw e
            } }
        }

        /**
         * Reads a file and transforms its content into an array of the specified type.
         *
         * @param T The type of elements in the resulting array.
         * @param file The file to read from.
         * @return A list of arrays, where each array corresponds to the transformed data from the file.
         * @since 6.1.0
         */
        inline fun <reified T> readArrayFromFile(file: File) = readListFromFile<T>(file).map { it.toTypedArray() }

        /**
         * Reads a list of objects of type [T] from the specified file and attempts to deserialize it using a preconfigured mapper.
         *
         * @param file The file from which the list of objects will be read.
         * @return An [Either] instance, containing either a [DeserializationError] in case of failure or a successfully deserialized list of type [T].
         * @since 6.1.0
         */
        inline fun <reified T> readListFromFile(file: File): Either<DeserializationError, List<T>> = either {
            catching({ MAPPER.readValue(file, MAPPER.typeFactory.constructCollectionType(List::class.java, T::class.java)) }) { e: Exception -> when (e) {
                is StreamReadException -> DeserializationError.ReadError(typeOf<List<T>>(), e)
                is DatabindException -> DeserializationError.MappingError(typeOf<List<T>>(), e)
                is JacksonException -> DeserializationError(typeOf<List<T>>(), e)
                else -> throw e
            } }
        }

        /**
         * Reads and deserializes a set of objects from the specified file.
         *
         * @param file The file from which the set will be read and deserialized.
         * @return An [Either] containing the deserialized set of type [T] if successful, or a [DeserializationError] in case of failure.
         * @since 6.1.0
         */
        inline fun <reified T> readSetFromFile(file: File): Either<DeserializationError, Set<T>> = either {
            catching({ MAPPER.readValue(file, MAPPER.typeFactory.constructCollectionType(Set::class.java, T::class.java)) }) { e: Exception -> when (e) {
                is StreamReadException -> DeserializationError.ReadError(typeOf<Set<T>>(), e)
                is DatabindException -> DeserializationError.MappingError(typeOf<Set<T>>(), e)
                is JacksonException -> DeserializationError(typeOf<Set<T>>(), e)
                else -> throw e
            } }
        }

        /**
         * Reads a map from the provided file and attempts to deserialize it.
         *
         * @param file The file to be read and deserialized into a map.
         * @return An [Either] containing either a [DeserializationError] in case of a failure
         *         or a [Map] with keys as strings and values of type [T] on success.
         * @since 6.1.0
         */
        fun <T> readMapFromFile(file: File): Either<DeserializationError, Map<String, T>> = either {
            catching({ MAPPER.readValue(file, object : TypeReference<Map<String, T>>() {}) }) { e: Exception -> when (e) {
                is StreamReadException -> DeserializationError.ReadError(typeOf<Map<String, T>>(), e)
                is DatabindException -> DeserializationError.MappingError(typeOf<Map<String, T>>(), e)
                is JacksonException -> DeserializationError(typeOf<Map<String, T>>(), e)
                else -> throw e
            } }
        }

        /**
         * Converts a JsonNode to a List of the specified type [T]. The method
         * supports primitive types (Int, Long, Double, Boolean, String), JsonNode,
         * and other data types that can be mapped using a registered ObjectMapper.
         * This operation works only if the JsonNode is an array; otherwise, it throws
         * an UnsupportedJsonTypeException.
         *
         * @return a List of elements of type [T] converted from the current JsonNode.
         * @throws UnsupportedJsonTypeException if the current JsonNode is not an array.
         * @since 6.1.0
         */
        inline fun <reified T> JsonNode.asList(): List<T> {
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
                        else -> list.add(MAPPER.treeToValue(node, T::class.java))
                    }
                }
            } else throw UnsupportedJsonTypeException(T::class.simpleName)
            return list.toList()
        }
        /**
         * Converts a [JsonNode] into a list of the specified type [T].
         * The method attempts to map each element in a JSON array to the specified type.
         * Supported types include Int, Long, Double, Boolean, String, and JsonNode.
         * For unsupported types, it uses a custom mapper to perform the conversion.
         * Throws an exception if the node is not an array.
         *
         * @return A list of elements of type [T] extracted from the JSON array.
         * @throws UnsupportedJsonTypeException if the JSON node type is not an array or is incompatible with [T].
         * @since 6.1.0
         */
        inline fun <reified T> com.fasterxml.jackson.databind.JsonNode.asList(): List<T> {
            val list = emptyMList<T>()
            if (isArray) {
                for (node in this) {
                    when(T::class) {
                        Int::class -> list.add(node.asInt() as T)
                        Long::class -> list.add(node.asLong() as T)
                        Double::class -> list.add(node.asDouble() as T)
                        Boolean::class -> list.add(node.asBoolean() as T)
                        String::class -> list.add(node.asText() as T)
                        JsonNode::class -> list.add(node as T)
                        else -> list.add(OLD_MAPPER.treeToValue(node, T::class.java))
                    }
                }
            } else throw UnsupportedJsonTypeException(T::class.simpleName)
            return list.toList()
        }
        /**
         * Converts a JsonNode representation of an array into a Set of the specified type.
         *
         * This function traverses the elements of the JsonNode and maps each element to the
         * specified type [T] using reflection. If the JsonNode is not an array, an
         * UnsupportedJsonTypeException will be thrown.
         *
         * @return A Set containing the elements from the JsonNode converted to the specified type [T].
         * @throws UnsupportedJsonTypeException If the JsonNode is not an array or cannot be converted to the specified type.
         * @since 6.1.0
         */
        inline fun <reified T> JsonNode.asSet(): Set<T> {
            val set = emptyMSet<T>()
            if (isArray) {
                for (node in this) {
                    when(T::class) {
                        Int::class -> set.add(node.asInt() as T)
                        Long::class -> set.add(node.asLong() as T)
                        Double::class -> set.add(node.asDouble() as T)
                        Boolean::class -> set.add(node.asBoolean() as T)
                        String::class -> set.add(node.asString() as T)
                        else -> set.add(MAPPER.treeToValue(node, T::class.java))
                    }
                }
            } else throw UnsupportedJsonTypeException(T::class.simpleName)
            return set.toSet()
        }
        /**
         * Converts a [JsonNode] to a [Set] of the specified type [T]. This method identifies the elements
         * within a JSON array and maps them to the corresponding Kotlin type based on the provided type parameter.
         * If the [JsonNode] is not an array, an exception is thrown.
         *
         * The following mappings are supported for primitive types:
         * - [Int]: Elements are retrieved using `node.asInt()`.
         * - [Long]: Elements are retrieved using `node.asLong()`.
         * - [Double]: Elements are retrieved using `node.asDouble()`.
         * - [Boolean]: Elements are retrieved using `node.asBoolean()`.
         * - [String]: Elements are retrieved using `node.asText()`.
         *
         * For other types, the elements are deserialized using Jackson's `treeToValue` method.
         *
         * @return A [Set] of elements cast to the specified type [T].
         * @throws UnsupportedJsonTypeException if the [JsonNode] is not a JSON array or if the type cannot be resolved.
         * @since 6.1.0
         */
        inline fun <reified T> com.fasterxml.jackson.databind.JsonNode.asSet(): Set<T> {
            val set = emptyMSet<T>()
            if (isArray) {
                for (node in this) {
                    when(T::class) {
                        Int::class -> set.add(node.asInt() as T)
                        Long::class -> set.add(node.asLong() as T)
                        Double::class -> set.add(node.asDouble() as T)
                        Boolean::class -> set.add(node.asBoolean() as T)
                        String::class -> set.add(node.asText() as T)
                        else -> set.add(OLD_MAPPER.treeToValue(node, T::class.java))
                    }
                }
            } else throw UnsupportedJsonTypeException(T::class.simpleName)
            return set.toSet()
        }

        /**
         * Converts a JsonNode instance into a Json object by serializing it to a JSON string.
         *
         * @receiver The JsonNode to be serialized.
         * @return A Json object containing the serialized representation of the JsonNode.
         * @throws JacksonException If an error occurs during the serialization process.
         * @since 6.1.0
         */
        fun JsonNode.asJson() = Json(MAPPER.writeValueAsString(this))
        /**
         * Converts the current JsonNode instance into a Json representation.
         *
         * This method serializes the JsonNode's content into a JSON-formatted string
         * and wraps it in a Json object for further processing or use.
         *
         * @receiver The JsonNode instance to be converted into a Json representation.
         * @return A Json object containing the serialized JSON string of the JsonNode.
         * @throws com.fasterxml.jackson.core.JsonProcessingException If an error occurs during the serialization process.
         * @since 6.1.0
         */
        fun com.fasterxml.jackson.databind.JsonNode.asJson() = Json(OLD_MAPPER.writeValueAsString(this))
        /**
         * Converts the current `JsonNode` to a formatted, human-readable JSON string.
         * This method serializes the `JsonNode` into a JSON string and applies
         * pretty-printing to enhance readability.
         *
         * @receiver The `JsonNode` to be converted into a pretty JSON string.
         * @return A formatted JSON string representation of the `JsonNode`.
         * @throws JacksonException If the JSON serialization process fails.
         * @since 6.1.0
         */
        fun JsonNode.asPrettyJson() = Json(MAPPER.writeValueAsString(this)).pretty
        /**
         * Converts the current JsonNode into a prettified JSON string representation.
         * This method utilizes a predefined object mapper to serialize the JsonNode
         * into a JSON string and formats it for improved readability.
         *
         * @return A formatted, human-readable JSON string representation of the JsonNode.
         * @throws com.fasterxml.jackson.core.JsonProcessingException If an error occurs during the serialization process.
         * @since 6.1.0
         */
        fun com.fasterxml.jackson.databind.JsonNode.asPrettyJson() = Json(OLD_MAPPER.writeValueAsString(this)).pretty

        /**
         * Copies a field from the current JsonNode to the target ObjectNode, creating intermediate
         * structure if necessary, based on an array of field names and the specified index.
         *
         * @param target The ObjectNode where the field will be copied to.
         * @param fieldNames An array of strings representing the field path to navigate and copy.
         * @param index The current index in the fieldNames array being processed.
         * @since 3.0.0
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
         * @since 3.0.0
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

        /**
         * Reads and deserializes the given JSON content into an object of the specified type.
         *
         * @param T The type of the object to be deserialized.
         * @param json The JSON content to be deserialized.
         * @since 6.1.0
         */
        inline fun <reified  T> JsonMapper.readValue(json: Json) = readValue<T>(json.value)
        /**
         * Reads the given JSON content and deserializes it into an object of the specified type.
         *
         * @param json The JSON content to deserialize, provided as a `Json` wrapper.
         * @return The deserialized object of type `T`.
         * @since 6.1.0
         */
        inline fun <reified  T> ObjectMapper.readValue(json: Json): T = readValue(json.value, object : com.fasterxml.jackson.core.type.TypeReference<T>() {})

        class Serializer : ValueSerializer<Json>() {
            override fun serialize(value: Json, gen: JsonGenerator, ctxt: SerializationContext) {
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

        class Deserializer : ValueDeserializer<Json>() {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
                Json(p.objectReadContext().readTree<JsonNode>(p).toString())
        }

        class OldSerializer : JsonSerializer<Json>() {
            override fun serialize(value: Json, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: SerializerProvider) {
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

        class OldDeserializer : JsonDeserializer<Json>() {
            override fun deserialize(p: com.fasterxml.jackson.core.JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Json =
                Json(p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p).toString())
        }

        /**
         * Creates a JSONB column representation for the specified column name.
         *
         * @param name The name of the column to be represented as JSONB.
         * @since 5.5.0
         */
        fun Table.rawJson(name: String) = jsonb<Json>(name)
    }

    /**
     * Retrieves the element at the specified index from the collection.
     *
     * @param index The position of the element to retrieve. Must be a valid index within the collection.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size).
     * @since 3.0.0
     */
    override operator fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this character sequence.
     *
     * @param startIndex the start index of the subsequence (inclusive).
     * @param endIndex the end index of the subsequence (exclusive).
     * @return a new character sequence that represents the specified subsequence.
     * @throws IndexOutOfBoundsException if the start or end index is out of bounds of the character sequence.
     * @since 3.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns a string representation of the object.
     * This implementation provides a representation of the internal `value`.
     *
     * @return the string representation of the object.
     * @since 3.0.0
     */
    override fun toString() = value

    /**
     * Parses a value into an object of the specified type using a mapper.
     *
     * This function attempts to deserialize the given input into the generic type T.
     * It uses an `either` block to safely handle any exceptions that may occur during deserialization,
     * such as a `DatabindException`. The function returns an appropriate error (`DeserializationError.MappingError`)
     * if deserialization fails.
     *
     * @param T The target type to which the value will be deserialized.
     * @return Either the deserialized object of type T or a `DeserializationError.MappingError`
     *         containing the error details in case of failure.
     * @since 6.1.0
     */
    inline fun <reified T> toObject() = either {
        catching({ MAPPER.readValue<T>(value) as T }) { e: DatabindException ->
            DeserializationError.MappingError(typeOf<T>(), e.message)
        }
    }

    /**
     * Converts the deserialized data into an array of the specified type.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return An `Either` containing a `DeserializationError.MappingError` if deserialization fails,
     *         or an array of type `T` if successful.
     * @since 6.1.0
     */
    inline fun <reified T> toArray() = toList<T>().map { it.toTypedArray() }

    /**
     * Converts the current value to a list of the specified type [T].
     * Utilizes a JSON mapper to perform the deserialization into a list.
     * If an error occurs during deserialization, wraps it in a `MappingError`.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @param T The type of elements in the resulting list.
     * @return Either a successfully deserialized list of type [T] or a `MappingError` detailing the issue.
     *
     * @since 6.1.0
     */
    inline fun <reified T> toList() = either {
        catching({ MAPPER.readValue<List<T>>(value) }) { e: DatabindException ->
            DeserializationError.MappingError(typeOf<T>(), e.message)
        }
    }
    /**
     * Converts the current context to a non-empty list of the specified type [T].
     * This method ensures that the resulting list is not empty by wrapping
     * the operation in a safe transformation, preserving the type information.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @param T The type of elements within the resulting non-empty list.
     * @return A non-empty list containing elements of type [T].
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptyList(): Either<Error, NonEmptyList<T>> =
        toList<T>() as Either<Error, List<T>> thenEither { catching({ it.toNonEmptyList() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Converts a generic list into an MList, wrapping the result in an Either type to handle potential mapping errors.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return An [Either] containing either a [DeserializationError.MappingError] if the mapping fails,
     *         or an [MList] of type [T] if the conversion is successful.
     * @since 6.1.0
     */
    inline fun <reified T> toMList() = toList<T>().map(List<T>::toMList)
    /**
     * Converts the current receiver into a mutable non-empty list of the specified type [T].
     *
     * This operation leverages `toMList` for initial conversion, followed by verifying
     * and ensuring the result is a non-empty mutable list. If the transformation does not
     * produce a valid non-empty mutable list, the function will signal an error encapsulated
     * in an `Either` construct.
     *
     * This method is particularly useful when enforcing non-empty constraints on lists
     * while still retaining mutability.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the resulting list is empty
     *
     * @param T The type of elements in the resulting list.
     * @return An `Either` containing the successfully transformed non-empty mutable list
     *         or an error indicating the conversion was unsuccessful.
     *
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptyMList() =
        toMList<T>() as Either<Error, MList<T>> thenEither { catching({ it.toNonEmptyMList() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Converts the receiver to a Set of type T if possible, returning either a MappingError
     * in case of a deserialization issue or the resulting Set.
     *
     * @return An [Either] containing a [DeserializationError.MappingError] on failure
     * or the resulting [Set] of type [T] on success.
     * @since 6.1.0
     */
    inline fun <reified T> toSet(): Either<DeserializationError.MappingError, Set<T>> =
        toList<T>().map { it.toSet() }
    /**
     * Converts the receiving collection into a non-empty set.
     * Requires the collection to contain at least one element; otherwise, the operation will fail.
     * Utilizes the `toSet` function to transform the collection into a standard set
     * and subsequently ensures the resulting set is non-empty.
     *
     * This function is inlined and uses reified generics to preserve the type information of the elements
     * during the transformation process.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if set is empty
     *
     * @param T The type of elements contained in the collection.
     * @return A non-empty set containing the elements from the original collection.
     *
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptySet() =
        toSet<T>() as Either<Error, Set<T>> thenEither { catching({ it.toNonEmptySet() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Converts a deserialized set of type T into an MSet (mutable set) if deserialization is successful.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return Either a MappingError if deserialization fails, or an MSet<T> containing elements of type T.
     * @since 6.1.0
     */
    inline fun <reified T> toMSet(): Either<DeserializationError.MappingError, MSet<T>> =
        toSet<T>().map(Set<T>::toMSet)
    /**
     * Converts the invoking collection or sequence into a `NonEmptyMSet`.
     *
     * This function first attempts to convert the collection into an `MSet`.
     * If the resulting set is empty, an `IterableError.Empty` is returned as a failure.
     * Otherwise, the result is wrapped in a successful `Either` containing a `NonEmptyMSet`.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if set is empty
     *
     * @return An `Either` where the left represents an `Error` and the right represents a `NonEmptyMSet`.
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptyMSet() =
        toMSet<T>() as Either<Error, MSet<T>> thenEither { catching({ it.toNonEmptyMSet() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Converts the current value to a `Map<String, V>` type using Jackson ObjectMapper.
     *
     * This function attempts to deserialize the provided value into a map where the keys are strings
     * and the values are of type `V`. It uses Jackson's `ObjectMapper` for the deserialization process
     * and captures any exceptions that may occur during this operation.
     *
     * @param V The type of the values in the resulting map.
     * @return A result type indicating either a successful `Map<String, V>` conversion
     *         or a deserialization error. The possible errors include:
     *         - [DeserializationError.ReadError] for issues during stream reading.
     *         - [DeserializationError.MappingError] for issues during data mapping.
     *         - [DeserializationError] for general Jackson-related exceptions.
     * @since 6.1.0
     */
    inline fun <reified V> toMap() = either {
        catching({ MAPPER.readValue(value, object : TypeReference<Map<String, V>>() {}) as Map<String, V> }) { e: Exception -> when (e) {
            is StreamReadException -> DeserializationError.ReadError(typeOf<Map<String, V>>(), e)
            is DatabindException -> DeserializationError.MappingError(typeOf<Map<String, V>>(), e)
            is JacksonException -> DeserializationError(typeOf<Map<String, V>>(), e)
            else -> throw e
        } }
    }
    /**
     * Converts the current structure into a non-empty map of type [Map<String, V>].
     * If the conversion is successful, it returns a `Right` containing the map.
     * If any exception is thrown during conversion or the resulting map is empty, it returns a `Left` with an appropriate error.
     *
     * Uses `toMap<V>()` for the conversion and ensures the resulting map is non-empty by wrapping it in an `Either` structure
     * with error handling for possible exceptions or empty results.
     *
     * @return An `Either` where the right value is a non-empty map and the left value represents an error.
     * @since 6.1.0
     */
    inline fun <reified V> toNonEmptyMap() =
        toMap<V>() as Either<Error, Map<String, V>> thenEither { catching({ it.toNonEmptyMap() }) { _: Exception -> IterableError.Empty } }

    /**
     * Converts the current value into a DataMap representation. This method utilizes the Jackson ObjectMapper
     * for deserialization and wraps the result in an Either type to handle potential errors.
     *
     * @return Either a successfully deserialized [DataMap] or a [DeserializationError] in case of a failure.
     * @since 6.1.0
     */
    fun toDataMap(): Either<DeserializationError, DataMap> = either {
        catching({ MAPPER.readValue(value, object : TypeReference<DataMap>() {}) as DataMap }) { e: Exception -> when (e) {
            is StreamReadException -> DeserializationError.ReadError(typeOf<DataMap>(), e)
            is DatabindException -> DeserializationError.MappingError(typeOf<DataMap>(), e)
            is JacksonException -> DeserializationError(typeOf<DataMap>(), e)
            else -> throw e
        } }
    }
    /**
     * Converts the current instance to a [NonEmptyDataMap] wrapped in an [Either].
     *
     * This method attempts to transform the existing DataMap into a [NonEmptyDataMap].
     * If the conversion fails or the resulting map is empty, an [Error] is returned.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if set is empty
     *
     * @return [Either] an [Error] if the conversion fails or the result is empty,
     *         or a successfully created [NonEmptyDataMap] instance.
     * @since 6.1.0
     */
    fun toNonEmptyDataMap(): Either<Error, NonEmptyDataMap> =
        toDataMap() as Either<Error, DataMap> thenEither { catching({ it.toNonEmptyMap() }) { _: Exception -> IterableError.Empty } }
    /**
     * Converts the current value into a `DataMapNN` instance, handling potential deserialization errors.
     *
     * This method attempts to deserialize the value into a `DataMapNN` object using a pre-configured
     * Jackson object mapper. If the deserialization succeeds, it returns the resulting `DataMapNN`
     * instance wrapped in an `Either.Right`. If deserialization fails, an appropriate `DeserializationError`
     * is returned wrapped in an `Either.Left`.
     *
     * @return `Either.Left` containing a `DeserializationError` if deserialization fails, or
     *         `Either.Right` containing a `DataMapNN` instance if deserialization is successful.
     * @since 6.1.0
     */
    fun toDataMapNN(): Either<DeserializationError, DataMapNN> = either {
        catching({ MAPPER.readValue(value, object : TypeReference<DataMapNN>() {}) as DataMapNN }) { e: Exception -> when (e) {
            is StreamReadException -> DeserializationError.ReadError(typeOf<DataMapNN>(), e)
            is DatabindException -> DeserializationError.MappingError(typeOf<DataMapNN>(), e)
            is JacksonException -> DeserializationError(typeOf<DataMapNN>(), e)
            else -> throw e
        } }
    }
    /**
     * Converts the current object into a `NonEmptyDataMapNN` wrapped in an `Either` type.
     * The method ensures that the resulting map is non-empty, and returns an appropriate error if this condition is not met.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if set is empty
     *
     * @return An instance of `Either` holding either an `Error` or a non-empty `NonEmptyDataMapNN`.
     * @since 6.1.0
     */
    fun toNonEmptyDataMapNN(): Either<Error, NonEmptyDataMapNN> =
        toDataMapNN() as Either<Error, DataMapNN> thenEither { catching({ it.toNonEmptyMap() }) { _: Exception -> IterableError.Empty } }

    /**
     * Converts the current map structure into a mutable map of type `Map<String, V>`.
     * The function is inline and reified, preserving the type `V` at runtime.
     * This transformation allows for further operations on the map in a mutable context.
     *
     * @param V The type of the values in the resulting map.
     * @return A mutable map transformed into the desired structure.
     * @since 6.1.0
     */
    inline fun <reified V> toMMap() = toMap<V>().map(Map<String, V>::toMMap)
    /**
     * Converts the current structure into a `NonEmptyMMap<String, V>`, wrapped in an `Either` type for error handling.
     *
     * This method attempts to transform the current structure into a non-empty multimap.
     * If the structure is empty or an error occurs during transformation, it returns an `Error` encapsulated in the `Either` type.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if set is empty
     *
     * @return An `Either` containing either an `Error` in case of failure or a successfully created `NonEmptyMMap<String, V>`.
     * @since 6.1.0
     */
    inline fun <reified V> toNonEmptyMMap(): Either<Error, NonEmptyMMap<String, V>> =
        toMMap<V>() as Either<Error, MMap<String, V>> thenEither { catching({ it.toNonEmptyMMap() }) { _: Exception -> IterableError.Empty } }

    /**
     * Converts a given value to a `DataMMap` instance by attempting to deserialize it
     * using the predefined object mapper. In case of a failure during deserialization,
     * an appropriate `DeserializationError` is returned.
     *
     * @return An `Either` instance containing `DataMMap` on successful deserialization
     * or `DeserializationError` on failure.
     * @since 6.1.0
     */
    fun toDataMMap(): Either<DeserializationError, DataMMap> = either {
        catching({ MAPPER.readValue(value, object : TypeReference<DataMMap>() {}) as DataMMap }) { e: Exception -> when (e) {
            is StreamReadException -> DeserializationError.ReadError(typeOf<DataMMap>(), e)
            is DatabindException -> DeserializationError.MappingError(typeOf<DataMMap>(), e)
            is JacksonException -> DeserializationError(typeOf<DataMMap>(), e)
            else -> throw e
        } }
    }
    /**
     * Converts the current instance into a `NonEmptyDataMMap`.
     * This method attempts to transform the data structure, ensuring it is non-empty.
     * If the transformation is unsuccessful, an error is returned.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if set is empty
     *
     * @return An `Either` containing an `Error` if the conversion fails, or a `NonEmptyDataMMap` if it succeeds.
     * @since 6.1.0
     */
    fun toNonEmptyDataMMap(): Either<Error, NonEmptyDataMMap> =
        toDataMMap() as Either<Error, DataMMap> thenEither { catching({ it.toNonEmptyMMap() }) { _: Exception -> IterableError.Empty } }
    /**
     * Converts the current object into a `DataMMapNN` instance.
     *
     * This method attempts to deserialize the current object into a `DataMMapNN` type
     * using the Jackson library. If the deserialization process encounters an error,
     * the method will return a `DeserializationError` indicating the specific issue.
     *
     * @return An `Either` containing either a successfully deserialized `DataMMapNN` instance
     * or a `DeserializationError` describing the failure encountered during the deserialization process.
     * @since 6.1.0
     */
    fun toDataMMapNN(): Either<DeserializationError, DataMMapNN> = either {
        catching({ MAPPER.readValue(value, object : TypeReference<DataMMapNN>() {}) as DataMMapNN }) { e: Exception -> when (e) {
            is StreamReadException -> DeserializationError.ReadError(typeOf<DataMMapNN>(), e)
            is DatabindException -> DeserializationError.MappingError(typeOf<DataMMapNN>(), e)
            is JacksonException -> DeserializationError(typeOf<DataMMapNN>(), e)
            else -> throw e
        } }
    }
    /**
     * Converts the current object into a `NonEmptyDataMMapNN` wrapped in an `Either` type.
     * This method attempts to transform the object into a `DataMMapNN`, then ensures
     * that the resulting map is non-empty.
     *
     *
     * @return An `Either` containing an `Error` in case of failure, or a `NonEmptyDataMMapNN` upon successful transformation.
     * @since 6.1.0
     */
    fun toNonEmptyDataMMapNN(): Either<Error, NonEmptyDataMMapNN> =
        toDataMMapNN() as Either<Error, DataMMapNN> thenEither { catching({ it.toNonEmptyMMap() }) { _: Exception -> IterableError.Empty } }

    /**
     * Converts the provided value to a JsonNode representation using the predefined object mapper.
     *
     * @return a JsonNode representation of the given value.
     * @since 3.8.0
     */
    fun toJsonNode(): JsonNode = MAPPER.readTree(value)
    /**
     * Converts the provided value to a JsonNode representation using the predefined object mapper.
     *
     * @return a JsonNode representation of the given value.
     * @since 3.8.1
     */
    fun toFasterXmlJsonNode(): com.fasterxml.jackson.databind.JsonNode = OLD_MAPPER.readTree(value)

    /**
     * Writes the current object to the specified file in JSON format using the configured mapper.
     *
     * @param file The file to which the object should be written. The file must be writable and accessible.
     * @since 3.0.0
     */
    fun writeToFile(file: File) = MAPPER.writeValue(file, MAPPER.readTree(value))

    /**
     * Invokes the operator function to retrieve a nested value from a JSON tree using a key path.
     *
     * @param keyPath The string representing the nested path to the desired value, separated by the specified regex separator.
     * @param regexSeparator The regex used to split the key path into individual keys. If null mapped to `DEFAULT_SEPARATOR`.
     * @return The nested JSON node at the specified key path, or `null` if the path does not exist.
     * @since 3.8.1
     */
    operator fun get(keyPath: String, regexSeparator: Regex = DEFAULT_SEPARATOR) = getAsNode(keyPath, regexSeparator)?.toJson()

    /**
     * Invokes the operator function to retrieve a nested value from a JSON tree using a key path.
     *
     * @param keyPath The string representing the nested path to the desired value, separated by the specified regex separator.
     * @param regexSeparator The regex used to split the key path into individual keys. If null mapped to `DEFAULT_SEPARATOR`.
     * @return The nested JSON node at the specified key path, or `null` if the path does not exist.
     * @since 3.8.1
     */
    fun getAsNode(keyPath: String, regexSeparator: Regex): JsonNode? {
        var node = MAPPER.readTree(value)
        for (key in keyPath.split(regexSeparator))
            if (node.has(key)) node = node.get(key) else return null
        return node
    }
    /**
     * Retrieves a nested JsonNode from the given key path using the specified separator.
     *
     * @param keyPath The string representing the path to the desired JsonNode, with keys separated by the default separator.
     * @return The JsonNode at the specified key path, or null if no corresponding node is found.
     * @since 3.8.1
     */
    infix fun getAsNode(keyPath: String): JsonNode? = getAsNode(keyPath, DEFAULT_SEPARATOR)

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
     * @since 3.0.0
     */
    fun findByPropertyValue(key: String, value: Any?): List<JsonNode> {
        val node = toJsonNode()
        val result = mutableListOf<JsonNode>()
        node.forEach {
            if (it.has(key)) when {
                it.get(key).isNumber && value is Number -> if (it.get(key).asDouble() == value.toDouble()) result.add(it)
                it.get(key).isString && value is String -> if (it.get(key).asString() == value) result.add(it)
                it.get(key).isBoolean && value is Boolean -> if (it.get(key).asBoolean() == value) result.add(it)
                it.get(key).isNull && value == null -> result.add(it)
                it.get(key).isArray && (value is List<*> || value is Array<*>) -> if (it.get(key).toList() == value) result.add(it)
            }
        }
        return result.toList()
    }

    /**
     * Finds and retrieves all JSON nodes containing a specific property value within a nested JSON structure.
     *
     * @param keyPath The path of keys separated by a specified regex that leads to the target JSON property.
     * @param regexSeparator A regex pattern used to split the key path into individual keys. Defaults to `DEFAULT_SEPARATOR`.
     * @param value The value to match against the target property value in the nested JSON. Supports types such as String, Number, Boolean, null, or arrays.
     * @return A list of JSON nodes where the property value matches the specified `value`. Returns an empty list if no matches are found.
     * @since 6.1.0
     */
    fun findByPropertyValueFromNestedJson(keyPath: String, regexSeparator: Regex = DEFAULT_SEPARATOR, value: Any?): List<JsonNode> {
        val result = mutableListOf<JsonNode>()
        var node = toJsonNode()
        var last = ""

        for (key in keyPath.split(regexSeparator)) {
            if (node.has(key)) node = node.get(key) else return emptyList()
            last = key
        }
        node.forEach {
            if (it.has(last)) when {
                it.get(last).isNumber && value is Number -> if (it.get(last).asDouble() == value.toDouble()) result.add(it)
                it.get(last).isString && value is String -> if (it.get(last).asString() == value) result.add(it)
                it.get(last).isBoolean && value is Boolean -> if (it.get(last).asBoolean() == value) result.add(it)
                it.get(last).isNull && value == null -> result.add(it)
                it.get(last).isArray && (value is List<*> || value is Array<*>) -> if (it.get(last).toList() == value) result.add(it)
            }
        }
        return result.toList()
    }
    
    /**
     * Merges the current JSON object with another JSON object and returns a new JSON object
     * containing the combined data. The merging is performed by adding all the fields
     * from the provided JSON object to the current JSON object.
     *
     * @param other the JSON object to be merged with the current JSON
     * @return a new JSON object containing the merged data
     * @since 3.0.0
     */
    operator fun plus(other: Json): Json {
        val node1 = MAPPER.readTree(value)
        val node2 = MAPPER.readTree(other.value)
        (node1 as ObjectNode).setAll(node2 as ObjectNode?)
        return Json(MAPPER.writeValueAsString(node1))
    }
    /**
     * Combines the current JSON object with the given Iterable of JSON objects,
     * merging their contents.
     *
     * @param others An iterables collection of JSON objects to merge with this JSON.
     * @return A new JSON object representing the merged contents of the initial JSON and the provided JSON objects.
     * @since 3.0.0
     */
    operator fun plus(others: Iterable<Json>): Json {
        var mergedJson = value
        others.forEach { mergedJson += it }
        return Json(mergedJson)
    }
    /**
     * Adds a new field to a JSON object.
     *
     * @param field A pair consisting of the field name as a String and the field value as a JsonNode.
     * @since 3.0.0
     */
    operator fun plus(field: Pair<String, Any?>): Json {
        val node = MAPPER.readTree(value)
        (node as ObjectNode).set(field.first, MAPPER.valueToTree(field.second))
        return Json(MAPPER.writeValueAsString(node))
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
     * @since 3.0.0
     */
    operator fun plus(nestedField: Triple<String, Regex?, Any?>): Json {
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
        return Json(MAPPER.writeValueAsString(current))
    }

    /**
     * Operator function to remove a specific field from a JSON object string.
     *
     * @param fieldName the name of the field to be removed from the JSON object.
     * @return a new JSON object string with the specified field removed, if successful.
     * @since 3.0.0
     */
    operator fun minus(fieldName: String): Json {
        val node = MAPPER.readTree(value)
        (node as ObjectNode).remove(fieldName)
        return Json(MAPPER.writeValueAsString(node))
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
     * @since 3.0.0
     */
    operator fun minus(field: Pair<String, Regex?>): Json {
        val rootNode = MAPPER.readTree(value)
        var current = rootNode as ObjectNode
        val fieldNames = field.first.split(field.second ?: DEFAULT_SEPARATOR)

        for (i in 0 until fieldNames.size - 1)
            current = current.get(fieldNames[i]) as ObjectNode? ?: return Json(MAPPER.writeValueAsString(rootNode))
        current.remove(fieldNames.last())
        return Json(MAPPER.writeValueAsString(current))
    }
    
    /**
     * Filters the JSON object to only include the specified fields.
     *
     * @param fieldsToKeep The fields to retain in the filtered JSON object.
     * @since 6.1.0
     */
    fun filterFields(vararg fieldsToKeep: String): Json {
        val node = toJsonNode()
        val result = MAPPER.createObjectNode()
        fieldsToKeep.forEach { if (node.has(it)) result.set(it, node.get(it)) }
        return Json(MAPPER.writeValueAsString(result))
    }

    /**
     * Filters nested fields from a JSON object based on the specified field paths and returns the filtered JSON as a string.
     *
     * @param regexSeparator the delimiter used to split field paths into nested levels, defaulting to DEFAULT_SEPARATOR.
     * @param fieldsToKeep the list of field paths to retain in the filtered JSON object. Field paths should be specified as strings.
     * @since 3.1.0
     */
    fun filterNestedFields(regexSeparator: Regex = DEFAULT_SEPARATOR, vararg fieldsToKeep: String): Json {
        val rootNode = toJsonNode()
        val filteredNode = MAPPER.createObjectNode()

        for (fieldPath in fieldsToKeep)
            rootNode.copyField(filteredNode, fieldPath.split(regexSeparator).toTypedArray(), 0)

        return Json(MAPPER.writeValueAsString(filteredNode))
    }


    /**
     * Checks if the JSON field path exists in the given tree structure by traversing it,
     * using the specified regex separator or a default separator.
     *
     * @param fieldPath the string representing the path of the field to be checked
     * @param regexSeparator a custom regex separator to split the field path, or null to use the default separator
     * @return true if the field exists in the JSON structure, otherwise false
     * @since 3.0.0
     */
    operator fun invoke(fieldPath: String, regexSeparator: Regex = DEFAULT_SEPARATOR): Boolean {
        var node = toJsonNode()
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
     * @since 3.0.0
     */
    fun isValidStructure(vararg expectedFields: String, restrictive: Boolean = true): Boolean {
        val node = toJsonNode()
        for (field in expectedFields)
            if (!node.has(field)) return false
        return !restrictive || expectedFields.size == MAPPER.readTree(value).propertyNames().toSet().size
    }

    /**
     * Validates if the given JSON structure matches the expected nested fields based on a separator.
     *
     * @param regexSeparator The optional regular expression used to separate nested fields. Defaults to DEFAULT_SEPARATOR.
     * @param expectedFields The fields to be checked for existence within the JSON structure.
     * @return `true` if all expected fields are present in the JSON structure, `false` otherwise.
     * @since 3.0.0
     */
    fun isValidNestedStructure(regexSeparator: Regex = DEFAULT_SEPARATOR, vararg expectedFields: String): Boolean {
        val node = toJsonNode()
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
     * @since 3.0.0
     */
    fun sortedKeys(direction: SortDirection = SortDirection.Ascending): Json {
        val node = MAPPER.readTree(value)
        val sortedNode = sortKeysRecursively(node, direction)
        return Json(MAPPER.writeValueAsString(sortedNode))
    }

    private fun sortKeysRecursively(node: JsonNode, direction: SortDirection): JsonNode {
        return when {
            node.isObject -> {
                val sortedNode = MAPPER.createObjectNode()
                val fields = node.properties().toMutableList()

                if (direction == SortDirection.Ascending) fields.sortBy { it.key }
                else fields.sortByDescending { it.key }

                for ([key, value1] in fields) {
                    val sortedValue = sortKeysRecursively(value1, direction)
                    sortedNode.set(key, sortedValue)
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
     * Negates the current state by checking if the JSON is empty.
     * This method is an operator function, meaning it can be used with the `!` operator.
     *
     * @return `true` if the JSON is empty; otherwise, `false`.
     * @since 3.0.0
     */
    operator fun not() = isEmptyJson

    /**
     * Recursively removes all null values from a map structure, including nested objects and arrays.
     *
     * @return A new map with all null values removed recursively.
     * @since 3.0.0
     */
    private fun DataMap.removeNullsRecursively(): DataMap {
        return mapNotNull { (key, value) ->
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
     * @since 3.0.0
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
     * @since 3.0.0
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

    // JSON MERGE PATCH (RFC 7396)

    /**
     * Applies a JSON Merge Patch to the current JSON object and returns the resulting JSON.
     *
     * The merge patch informs how to modify the current JSON structure to produce a new JSON structure.
     * It is a partial update where unwanted fields are removed, and specified fields are updated or added.
     *
     * @param patch The JSON Merge Patch object that defines the modifications to apply to the current JSON object.
     * @since 6.1.0
     */
    infix fun mergePatch(patch: Json) = Json(MAPPER.writeValueAsString(applyMergePatch(toJsonNode(), patch.toJsonNode())))
    /**
     * Applies a JSON Merge Patch to the current object using the provided YAML patch.
     *
     * @param patch The YAML object representing the patch to be applied.
     * @since 3.2.0
     */
    infix fun mergePatch(patch: Yaml) = mergePatch(patch.toJson())

    private fun applyMergePatch(target: JsonNode?, patch: JsonNode): JsonNode {
        if (patch.isObject) {
            val targetObj = if (target != null && target.isObject)
                target.deepCopy() as ObjectNode
            else MAPPER.createObjectNode()

            patch.properties().forEach { entry ->
                val key = entry.key
                val value = entry.value

                if (value.isNull) targetObj.remove(key)
                else {
                    val existing = targetObj.get(key)
                    targetObj.set(key, applyMergePatch(existing, value))
                }
            }
            return targetObj
        }
        return patch.deepCopy()
    }

    // JSON PATCH (RFC 6902)

    /**
     * Applies a JSON Patch to the current JSON data and returns the result. The method follows the specification
     * of JSON Patch (RFC 6902) to modify the JSON structure according to the provided patch definition.
     * This implementation supports operations such as "add", "replace", and "remove".
     *
     * Possible erros:
     * - [InvalidTypeFormat] - if the patch is not a JSON array or a path is invalid.
     * - [RequiredProperty] - if a required property is missing in the patch.
     * - [JsonError.PathNotFound] - if the path specified in the patch does not exist in the target JSON.
     * - [IllegalOperation] - if you're trying to move a node into its own children.
     * - [ValidationError.ExpectationMismatch] - if the path specified in the patch does not match the expected value.
     * - [UnsupportedOperation] - if an unsupported operation is encountered in the patch.
     *
     * @param patch The JSON Patch to apply. The patch must be a JSON array containing the operations to perform.
     * @return The modified JSON object wrapped in a `Result` object, which will contain the result of the `runCatching` block.
     * @since 6.1.0
     */
    infix fun jsonPatch(patch: Json) = either {
        val targetNode = toJsonNode().deepCopy()
        val patchNode = patch.toJsonNode()

        ensure(patchNode.isArray) { InvalidTypeFormat(patchNode, typeOf<List<*>>(), reason = "Patch must be an array of operations") }

        for (operation in patchNode) {
            val op = operation.get("op")?.asString()
                ?: raise(RequiredProperty("op", typeOf<String>()))
            val pathStr = operation.get("path")?.asString()
                ?: raise(RequiredProperty("path", typeOf<String>()))

            when (op) {
                "add" -> {
                    val value = operation.get("value") ?: raise(
                        RequiredProperty("value", typeOf<Any>())
                    )
                    applyAdd(targetNode, pathStr, value)
                }
                "replace" -> {
                    val value = operation.get("value") ?: raise(
                        RequiredProperty("value", typeOf<Any>())
                    )
                    val [parent, leaf] = resolvePointer(targetNode, pathStr)
                    if (parent.isObject) {
                        (parent as ObjectNode).set(leaf, value)
                    } else if (parent.isArray) {
                        (parent as ArrayNode).set(leaf.toInt(), value)
                    }
                }
                "remove" -> {
                    applyRemove(targetNode, pathStr)
                }
                "copy" -> {
                    val fromStr = operation.get("from")?.asString()
                        ?: raise(RequiredProperty("from", typeOf<String>()))
                    val valueToCopy = getPointerValue(targetNode, fromStr)?.deepCopy()
                        ?: raise(JsonError.PathNotFound(fromStr))

                    applyAdd(targetNode, pathStr, valueToCopy)
                }
                "move" -> {
                    val fromStr = operation.get("from")?.asString()
                        ?: raise(RequiredProperty("from", typeOf<String>()))
                    ensure(!pathStr.startsWith("$fromStr/")) {
                        IllegalOperation("Cannot move a node into its own children: from $fromStr to $pathStr")
                    }

                    val valueToMove = getPointerValue(targetNode, fromStr)?.deepCopy()
                        ?: raise(JsonError.PathNotFound(fromStr))

                    applyRemove(targetNode, fromStr)
                    applyAdd(targetNode, pathStr, valueToMove)
                }
                "test" -> {
                    val expectedValue = operation.get("value")
                        ?: raise(RequiredProperty("value", typeOf<Any>()))
                    val actualValue = getPointerValue(targetNode, pathStr)

                    if (actualValue == null || actualValue != expectedValue) {
                        raise(ValidationError.ExpectationMismatch(pathStr, expectedValue, actualValue))
                    }
                }
                else -> raise(UnsupportedOperation("Operation '$op' is not supported."))
            }
        }

        Json(MAPPER.writeValueAsString(targetNode))
    }
    /**
     * Applies a JSON Patch to the current JSON data and returns the result. The method follows the specification
     * of JSON Patch (RFC 6902) to modify the JSON structure according to the provided patch definition.
     * This implementation supports operations such as "add", "replace", and "remove".
     *
     * Possible erros:
     * - [InvalidTypeFormat] - if the patch is not a JSON array or a path is invalid.
     * - [RequiredProperty] - if a required property is missing in the patch.
     * - [JsonError.PathNotFound] - if the path specified in the patch does not exist in the target JSON.
     * - [IllegalOperation] - if you're trying to move a node into its own children.
     * - [ValidationError.ExpectationMismatch] - if the path specified in the patch does not match the expected value.
     * - [UnsupportedOperation] - if an unsupported operation is encountered in the patch.
     *
     * @param patch The JSON Patch to apply. The patch must be a JSON array containing the operations to perform.
     * @return The modified JSON object wrapped in a `Result` object, which will contain the result of the `runCatching` block.
     * @since 6.1.0
     */
    infix fun jsonPatch(patch: Yaml) = jsonPatch(patch.toJson())

    context(_: Raise<Error>)
    private fun applyAdd(root: JsonNode, pathStr: String, value: JsonNode) {
        val [parent, leaf] = resolvePointer(root, pathStr)
        if (parent.isObject) {
            (parent as ObjectNode).set(leaf, value)
        } else if (parent.isArray) {
            val arr = parent as ArrayNode
            val index = if (leaf == String.HYPEN) arr.size() else leaf.toInt()
            arr.insert(index, value)
        }
    }

    context(_: Raise<Error>)
    private fun applyRemove(root: JsonNode, pathStr: String) {
        val [parent, leaf] = resolvePointer(root, pathStr)
        if (parent.isObject) {
            (parent as ObjectNode).remove(leaf)
        } else if (parent.isArray) {
            (parent as ArrayNode).remove(leaf.toInt())
        }
    }

    context(_: Raise<Error>)
    private fun getPointerValue(root: JsonNode, pathStr: String): JsonNode? {
        val [parent, leaf] = resolvePointer(root, pathStr)
        return if (parent.isObject) {
            parent.get(leaf)
        } else if (parent.isArray) {
            parent.get(leaf.toInt())
        } else null
    }

    context(_: Raise<Error>)
    private fun resolvePointer(root: JsonNode, pathStr: String): Pair<JsonNode, String> {
        if (pathStr.isEmpty() || !pathStr.startsWith("/")) {
            raise(InvalidTypeFormat(pathStr, typeOf<String>()))
        }

        val tokens = pathStr.split(Char.SLASH).drop(1).map {
            it.replace("~1", Char.SLASH).replace("~0", Char.TILDE)
        }

        var current = root
        for (i in 0 until tokens.size - 1) {
            val token = tokens[i]
            current = (if (current.isArray) current.get(token.toInt())
            else current.get(token)) ?: raise(JsonError.PathNotFound(pathStr))
        }

        return current to tokens.last()
    }

    /**
     * Validates the current JSON value against the given JSON schema and version.
     *
     * Possible errors:
     * - [InvalidTypeFormat] - if the input JSON schema is malformed.
     * - [JsonError.SchemaValidationFailed] - if the validation fails.
     *
     * @param jsonSchema The JSON schema instance used for validation.
     * @return Either the validated value or a validation error containing schema validation failures.
     * @since 6.1.0
     */
    infix fun validateWithSchema(jsonSchema: JsonSchema) = validateWithSchema(jsonSchema, JsonSchema.Version.V2020_12)
    /**
     * Validates the current JSON value against the given JSON schema and version.
     *
     * Possible errors:
     * - [InvalidTypeFormat] - if the input JSON schema is malformed.
     * - [JsonError.SchemaValidationFailed] - if the validation fails.
     *
     * @param jsonSchema The JSON schema instance used for validation.
     * @param version The version of the JSON schema to be applied during validation.
     * @return Either the validated value or a validation error containing schema validation failures.
     * @since 6.1.0
     */
    fun validateWithSchema(jsonSchema: JsonSchema, version: JsonSchema.Version) = either {
        try {
            val schema = JsonSchemaFactory
                .getInstance(version.toVersionFlag())
                .getSchema(OLD_MAPPER.readTree(jsonSchema.value))

            val validationMessages = schema.validate(OLD_MAPPER.readTree(value))
            if (validationMessages.isEmpty()) return@either this

            val errors = validationMessages.map {
                JsonSchema.SchemaError(
                    it.evaluationPath?.toString() ?: "$",
                    it.message,
                    it.details
                )
            }
            raise(JsonError.SchemaValidationFailed(errors))
        } catch (e: Exception) {
            InvalidTypeFormat(jsonSchema, typeOf<JsonSchema>())
        }
    }
}