/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("java_integer_as_kotlin_int")
@file:OptIn(Beta::class)

package dev.tommasop1804.kutils.classes.coding

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.Instant
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.Json.Companion.MAPPER
import dev.tommasop1804.kutils.classes.coding.Json.Companion.toJson
import dev.tommasop1804.kutils.exceptions.*
import org.tomlj.TomlArray
import org.tomlj.TomlParseResult
import org.tomlj.TomlTable
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.File
import java.nio.file.Path
import java.time.*
import org.intellij.lang.annotations.Language as IJLanguage
import org.tomlj.Toml as TomlJ

/**
 * The `TOML` class is a representation of TOML-encoded data. It provides functionality to parse, validate,
 * manipulate, and serialize TOML data. It supports the conversion of TOML content into objects, lists, maps,
 * and other data structures and also facilitates operations such as removing comments or accessing nested
 * TOML nodes via dot paths.
 *
 * Features include:
 * - Validation of TOML content.
 * - Conversion between TOML and other data formats, including JSON and YAML.
 * - Writing and reading TOML from files.
 * - Accessing and modifying nested TOML structures.
 *
 * The class is compatible with JSON serialization and deserialization libraries and leverages
 * [tomlj](https://github.com/tomlj/tomlj) for underlying parsing and Jackson's
 * `jackson-dataformat-toml` for serialization.
 *
 * @param value The raw TOML content as a string.
 * @constructor Creates an instance of the TOML class with the given string content or a `TomlNode`.
 * @since 3.11.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Toml.Companion.Serializer::class)
@JsonDeserialize(using = Toml.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Toml.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Toml.Companion.OldDeserializer::class)
@Suppress("unused", "UNCHECKED_CAST", "kutils_collection_declaration")
@Beta(since = "3.11.0")
class Toml(@param:IJLanguage("TOML") override var value: String) : CharSequence, Code(value, Language.TOML) {
    /**
     * Constructs a new instance of the TOML class using a given [TomlNode].
     *
     * @param node The TomlNode instance whose raw value will be used to initialize the TOML object.
     * The raw value is converted to a TOML string and passed to the primary constructor.
     *
     * @since 3.11.0
     */
    constructor(node: TomlNode) : this(
        node.rawValue.toToml().value
    )

    /**
     * Secondary constructor that initializes an instance using a `Code` object.
     *
     * @param code The `Code` object containing the value to initialize.
     * @since 3.11.0
     */
    constructor(code: Code) : this(code.value) {
        code.language.expect(Language.TOML)
    }

    /**
     * Creates an instance by reading the content of the specified file and passing it as a
     * parameter to the primary constructor.
     *
     * @param file The file whose content will be read and used to initialize the instance.
     * @since 3.11.0
     */
    constructor(file: File) : this(file.readText()) {
        file.exists().expect(true)
        file.isFile.expect(true)
        file.canRead().expect(true)
        file.extension.validate(file::extension, "file") { it equalsIgnoreCase "toml" }
    }
    /**
     * Creates an instance by reading the content of the specified file and passing it as a
     * parameter to the primary constructor.
     *
     * @param path The path of the file whose content will be read and used to initialize the instance.
     * @since 3.11.0
     */
    constructor(path: Path) : this(path.toFile())

    init {
        val parsed: TomlParseResult = TomlJ.parse(value)
        if (parsed.hasErrors()) {
            val first = parsed.errors().first()
            val pos = first.position()
            throw MalformedInputException(
                "${first.message.orEmpty().sentenceCase()} at line ${pos?.line() ?: 1}, column ${pos?.column() ?: 1}"
            )
        }
    }

    companion object {
        /**
         * The Jackson [TomlMapper] used to write/read TOML content. Reads delegate to `tomlj` for
         * better diagnostics; writes go through Jackson because `tomlj` does not provide a writer.
         *
         * @since 3.11.0
         */
        val TOML_MAPPER: TomlMapper = TomlMapper.builder().build()

        /**
         * Checks if the String represents a valid TOML structure.
         *
         * This method attempts to parse the String as TOML using the `Toml` class.
         * If the parsing succeeds, the String is considered valid TOML.
         * Otherwise, it is considered invalid.
         *
         * @receiver The String to be validated as TOML.
         * @return `true` if the String is valid TOML; `false` otherwise.
         * @since 3.11.0
         */
        fun String.isValidToml() = runCatching { Toml(this) }

        /**
         * Converts the current file to a `Toml` instance encapsulated within a `Result`.
         *
         * This method parses the content of the file on which it is called and attempts to create an instance
         * of the `Toml` class with the file as input. The operation is performed within a `Result` context,
         * meaning it captures any exception that occurs during the parsing process.
         *
         * @return A `Result` wrapping the `Toml` instance if the parsing succeeds, or an exception if it fails.
         * @since 3.13.0
         */
        fun File.toToml() = runCatching { Toml(this) }
        /**
         * Converts the current `String` into a TOML representation and wraps the operation in a `Result`.
         *
         * @receiver The `String` to be converted to TOML.
         * @return A `Result` that either contains the parsed TOML object or an exception if parsing fails.
         * @since 3.11.0
         */
        fun @receiver:IJLanguage("toml") String.toToml() = runCatching { Toml(this) }
        /**
         * Converts the current `JSON` instance into its equivalent `TOML` representation.
         *
         * Since TOML at the root is always a table, a JSON array or scalar will be wrapped under a
         * `value` key (i.e. `{"value": [...]}` → `value = [...]`).
         *
         * @return A `TOML` instance representing the data structure of the original `JSON` input.
         * @since 3.11.0
         */
        @JvmName("jsonToToml")
        fun Json.toToml(): Toml {
            val obj = toObject<Any>()()
            return obj.toToml()
        }
        /**
         * Converts the current `YAML` instance into its equivalent `TOML` representation.
         *
         * @return A `TOML` instance representing the data structure of the original `YAML` input.
         * @since 3.11.0
         */
        @JvmName("yamlToToml")
        fun Yaml.toToml(): Toml {
            val obj = toObject<Any>()()
            return obj.toToml()
        }
        /**
         * Converts the XML instance into its equivalent TOML representation.
         *
         * This function first transforms the XML content into JSON format and then
         * converts the resulting JSON representation into TOML format. It assumes that
         * the XML instance being operated on contains a valid structure that can be mapped
         * to JSON and subsequently to TOML.
         *
         * @receiver The [Xml] instance that will be converted to TOML format.
         * @return The TOML representation of the XML content.
         * @since 3.11.0
         */
        @JvmName("xmlToToml")
        fun Xml.toToml() = toJson().toToml()
        /**
         * Converts the current CSV instance into its equivalent TOML representation.
         *
         * @return A TOML instance representing the tabular data of the original CSV input.
         * @since 3.13.0
         */
        @JvmName("csvToToml")
        fun Csv.toToml(): Toml = toJson().toToml()
        /**
         * Converts the given object to its TOML representation.
         *
         * If [Any] is not already a map-like structure, it is wrapped as `{ "value": <self> }` so that
         * the produced TOML is a valid root table (TOML 1.0 forbids non-table roots).
         *
         * @return The TOML representation of the object as an instance of the `TOML` class.
         * @since 3.11.0
         */
        @JvmName("anyToToml")
        fun Any?.toToml(): Toml {
            val rootMap: Map<String, Any?> = when (this) {
                null -> mapOf("value" to null)
                is Map<*, *> -> this.entries.associate { it.key.toString() to it.value }
                is TomlNode -> when {
                    this.rawValue is Map<*, *> -> (this.rawValue).entries.associate { it.key.toString() to it.value }
                    else -> mapOf("value" to this.rawValue)
                }
                is Toml -> return this
                is Json -> return this.toToml()
                is Yaml -> return this.toToml()
                is Xml -> return this.toToml()
                else -> return Toml(TOML_MAPPER.writeValueAsString(this))
            }
            return Toml(TOML_MAPPER.writeValueAsString(rootMap))
        }

        /**
         * Reads the content of the specified file and parses it into a TOML object.
         *
         * @param file the file to be read and parsed as TOML
         * @return a Result containing the parsed object if the operation is successful,
         * or an exception if an error occurs
         * @since 3.11.0
         */
        inline fun <reified T> readFromFile(file: File): Result<T> = runCatching { Toml(file.readText()).toObject<T>()() }
        /**
         * Reads an array of the specified type from the given file.
         *
         * @param T The type of elements to be read and stored in the resulting array.
         * @param file The file from which the array is read.
         * @return A `Result` containing the array of type `T` if the operation succeeds,
         * or the encapsulated exception if the operation fails.
         * @since 3.11.0
         */
        inline fun <reified T> readArrayFromFile(file: File): Result<Array<T>> = runCatching { readListFromFile<T>(file)().toTypedArray() }
        /**
         * Reads and parses a list of objects from the specified file.
         *
         * Since TOML cannot have a root array, the file must define a single key that holds the array
         * (the first array-typed top-level key is returned).
         *
         * @param file The file to read from. It should contain TOML-formatted data.
         * @return A [Result] containing the parsed list of objects of type [T],
         * or an exception if the operation fails.
         * @since 3.11.0
         */
        fun <T> readListFromFile(file: File): Result<List<T>> = runCatching { Toml(file.readText()).toList<T>()() }
        /**
         * Reads the content of a given file, parses it as TOML, and converts it to a set of type `T`.
         *
         * @param file The file to be read, whose content is expected to be in TOML format.
         * @return A [Result] containing a [Set] of elements of type `T` if the operation is successful.
         * In case of failure, the [Result] will encapsulate the exception.
         * @since 3.11.0
         */
        fun <T> readSetFromFile(file: File): Result<Set<T>> = runCatching { Toml(file.readText()).toSet<T>()() }
        /**
         * Reads the contents of a specified file and parses it into a map structure from TOML format.
         *
         * @param file The file containing the TOML data to be parsed.
         * @return A [Result] containing the parsed map with keys as `String` and values of type `T`
         *         on success, or an exception on failure.
         * @since 3.11.0
         */
        fun <T> readMapFromFile(file: File): Result<Map<String, T>> = runCatching { Toml(file.readText()).toMap<T>()() }

        class Serializer : ValueSerializer<Toml>() {
            override fun serialize(value: Toml, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                // TOML root is always an object/table, but we mirror Yaml's behaviour for safety.
                val node = MAPPER.readTree(value.toJson().value)
                when {
                    node.isArray -> gen.writePOJO(value.toList<Any>()())
                    node.isObject -> gen.writePOJO(value.toMap<Any>()())
                    else -> gen.writeRaw(value.toJson().value)
                }
            }
        }

        class Deserializer : ValueDeserializer<Toml>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) =
                Json(p.objectReadContext().readTree<JsonNode>(p).toString()).toToml()
        }

        class OldSerializer : JsonSerializer<Toml>() {
            override fun serialize(value: Toml, gen: JsonGenerator, serializers: SerializerProvider) {
                val node = MAPPER.readTree(value.toJson().value)
                when {
                    node.isArray -> gen.writeObject(value.toList<Any>()())
                    node.isObject -> gen.writeObject(value.toMap<Any>()())
                    else -> gen.writeRaw(value.toJson().value)
                }
            }
        }

        class OldDeserializer : JsonDeserializer<Toml>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Toml =
                Json(p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p).toString()).toToml()
        }

        internal fun convertTomlValue(value: Any?): Any? = when (value) {
            null -> null
            is TomlTable -> value.keySet().associateWith { convertTomlValue(value.get(it)) }
            is TomlArray -> (0 until value.size()).map { convertTomlValue(value.get(it)) }
            else -> value
        }
    }

    /**
     * Converts the TOML content represented by this instance into an object of type [T].
     *
     * @param T The target type to which the TOML content will be converted.
     * @return A [Result] containing an instance of type [T] if the conversion succeeds,
     *         or an exception if the conversion fails.
     * @throws IllegalArgumentException if the value cannot be converted to the specified type.
     * @since 3.11.0
     */
    inline fun <reified T> toObject() = runCatching {
        tryOr({ toJson().toObject<T>()() }) {
            TOML_MAPPER.readValue(value, T::class.java)!!
        }
    }

    /**
     * Converts the TOML content into an array of type `T`.
     *
     * Since the TOML root is always a table, this returns the first top-level array as an array.
     *
     * @param T The type to which the TOML elements should be cast.
     * @return A `Result` containing an array of type `T` if the conversion is successful, or the exception if it fails.
     * @since 3.11.0
     */
    inline fun <reified T> toArray() = runCatching { toList<T>()().toTypedArray() }

    /**
     * Converts the TOML content stored in the `value` property into a list of objects of type `T`.
     *
     * Since TOML's root is a table, the first top-level array value is returned. If no array is present,
     * the values of the root table are returned as a list.
     *
     * @param T The type to which each element in the resulting list will be cast.
     * @return A `Result` wrapping either the successfully parsed list of objects or any exception encountered during parsing.
     * @since 3.11.0
     */
    fun <T> toList() = runCatching {
        val parsed = TomlJ.parse(value)
        val firstArrayKey = parsed.keySet().firstOrNull { parsed.isArray(it) }
        val raw: Any? = if (firstArrayKey != null) parsed.getArray(firstArrayKey)
        else convertTomlValue(parsed) as? Map<*, *>
        when (raw) {
            is TomlArray -> (convertTomlValue(raw) as List<*>).map { it as T }
            is Map<*, *> -> raw.values.map { it as T }
            else -> emptyList()
        }
    }
    /**
     * Parses the TOML content stored in the current object and converts it into a mutable list of type [T].
     *
     * @param T The type of elements in the resulting mutable list.
     * @return A [Result] containing the mutable list of type [T], or an exception if the operation fails.
     * @since 3.11.0
     */
    fun <T> toMList() = runCatching { toList<T>()().toMList() }

    /**
     * Converts the TOML content represented by `value` into a set of objects of type `T`.
     *
     * @param T The type of the elements in the resulting set.
     * @return A `Result` containing the set of objects of type `T` or an exception if an error occurs.
     * @since 3.11.0
     */
    fun <T> toSet() = runCatching { toList<T>()().toSet() }
    /**
     * Parses the TOML content stored in the `value` field and converts it into a mutable set of elements of type T.
     *
     * @return A `Result` wrapping a mutable set of type T, containing the parsed and distinct elements from the TOML content.
     * @since 3.11.0
     */
    fun <T> toMSet() = runCatching { toList<T>()().toMSet() }

    /**
     * Converts the underlying TOML content into a map structure of key-value pairs.
     *
     * @param V The type of values expected in the resulting map.
     * @return A `Result<Map<String, T>>` containing the parsed map if successful or the exception if an error occurred.
     * @since 3.11.0
     */
    fun <V> toMap() = runCatching {
        @Suppress("UNCHECKED_CAST")
        (convertTomlValue(TomlJ.parse(value)) as Map<String, V>)
    }
    /**
     * Converts a TOML string value into a mutable map (`MMap`) with string keys and values of type `V`.
     *
     * @param V The type of the values in the resulting mutable map.
     * @return A `Result` containing the parsed `MMap<String, V>` on success, or an exception on failure.
     * @since 3.11.0
     */
    fun <V> toMMap() = runCatching {
        @Suppress("UNCHECKED_CAST")
        (toMap<V>()().toMutableMap())
    }
    /**
     * Converts the stored TOML content in the `value` field into a `DataMap` object.
     *
     * @return A `Result` containing either the parsed `DataMap` object or an exception if parsing fails.
     * @since 3.11.0
     */
    fun toDataMap() = runCatching {
        @Suppress("UNCHECKED_CAST")
        (convertTomlValue(TomlJ.parse(value)) as DataMap)
    }
    /**
     * Converts the content of the current TOML instance to a mutable map representation of `DataMMap`.
     *
     * @return A `Result` containing the parsed `DataMMap` or the exception in case of a failure.
     * @since 3.11.0
     */
    fun toDataMMap() = runCatching {
        @Suppress("UNCHECKED_CAST")
        (toDataMap()().toMutableMap())
    }
    /**
     * Parses the TOML content stored in the `value` field and converts it into a non-nullable `DataMapNN` object.
     *
     * @return A `Result` wrapping the successfully parsed `DataMapNN` object if the operation succeeds.
     * @since 3.11.0
     */
    fun toDataMapNN() = runCatching {
        @Suppress("UNCHECKED_CAST")
        (toDataMap()() as DataMapNN)
    }
    /**
     * Attempts to parse the current TOML value into a non-nullable [DataMMapNN] object.
     *
     * @return [Result] containing either the successfully parsed [DataMMapNN] object or an exception.
     * @since 3.11.0
     */
    fun toDataMMapNN() = runCatching {
        @Suppress("UNCHECKED_CAST")
        (toDataMMap()() as DataMMapNN)
    }

    /**
     * Retrieves the element at the specified index from the value.
     *
     * @param index The position of the element to retrieve.
     * @return The character at the specified index.
     * @since 3.11.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence.
     *
     * @since 3.11.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns the raw TOML string representation.
     *
     * @since 3.11.0
     */
    override fun toString() = value

    /**
     * Writes the content of the TOML to the provided file.
     *
     * @param file The file to which the content will be written.
     * @since 3.11.0
     */
    fun writeToFile(file: File) = file.writeText(value)

    /**
     * Retrieves a TOML node based on the provided dot-separated path.
     *
     * @param dotPath The dot-separated string representing the path to the desired TOML node.
     * @return The Toml wrapping the node found at the specified path.
     * @since 3.11.0
     */
    operator fun get(dotPath: String) = Toml(getAsNode(dotPath))

    /**
     * Retrieves a TOML node based on the provided dot-separated path.
     *
     * @param dotPath The dot-separated string representing the path to the desired TOML node.
     * @return The TomlNode corresponding to the specified path.
     * @since 3.11.0
     */
    infix fun getAsNode(dotPath: String): TomlNode =
        if (Char.DOT !in dotPath) TomlNode(toDataMap()()[dotPath])
        else TomlNode(toDataMap()()[dotPath before Char.DOT])[dotPath after Char.DOT]

    /**
     * Sets the value at the specified dot-delimited path within the TOML structure.
     *
     * @param dotPath The dot-delimited path to identify the location in the TOML structure where the value should be set.
     * @param value The value to be set at the specified path. It can be null.
     * @since 3.11.0
     */
    operator fun set(dotPath: String, value: Any?) {
        val rootNode = TomlNode(toDataMMap()())
        rootNode[dotPath] = value
        this.value = rootNode.rawValue!!.toToml().value
    }

    /**
     * Removes comments from the TOML content. TOML comments start with `#` and run to end of line.
     *
     * Note: this is a textual strip; it does NOT understand `#` characters embedded in basic strings.
     * Use with care if your document contains literal `#` inside string values.
     *
     * @since 3.11.0
     */
    fun removeComments() {
        value -= Regex("#.*")
    }

    /**
     * Applies a merge patch to the current TOML object, transforming it based on the provided patch TOML.
     *
     * The method converts the TOML content to JSON, applies the JSON merge patch algorithm,
     * and converts the resulting JSON back to TOML.
     *
     * @param patch the TOML object containing the patch to apply
     * @return a Result containing the patched TOML object, or an exception if an error occurs
     * @since 3.11.0
     */
    infix fun mergePatch(patch: Toml) = runCatching {
        tryOrThrow({ e -> NoSuchTomlPathException((-Char.COLON(e.message.orEmpty()) - 2)(e.message.orEmpty())) }, overwriteOnly = NoSuchJsonPathException::class) {
            toJson().mergePatch(patch.toJson())().toToml()
        }
    }
    /**
     * Applies a merge patch to the current TOML object, transforming it based on the provided patch JSON.
     *
     * @param patch the JSON object containing the patch to apply
     * @return a Result containing the patched TOML object, or an exception if an error occurs
     * @since 3.11.0
     */
    infix fun mergePatch(patch: Json) = runCatching {
        tryOrThrow({ e -> NoSuchTomlPathException((-Char.COLON(e.message.orEmpty()) - 2)(e.message.orEmpty())) }, overwriteOnly = NoSuchJsonPathException::class) {
            toJson().mergePatch(patch)().toToml()
        }
    }
    /**
     * Applies a TOML patch (RFC 6902 style, executed via JSON Patch) to modify the current TOML content.
     *
     * @param patch the TOML patch to be applied.
     * @return a [Result] encapsulating the modified TOML or an exception if the operation fails.
     * @since 3.11.0
     */
    infix fun tomlPatch(patch: Toml) = runCatching {
        tryOrThrow({ e -> NoSuchTomlPathException((-Char.COLON(e.message.orEmpty()) - 2)(e.message.orEmpty()), e.cause) }, includeCause = false, overwriteOnly = NoSuchJsonPathException::class) {
            toJson().jsonPatch(patch.toJson())().toToml()
        }
    }
    /**
     * Applies a TOML patch (executed via JSON Patch) to modify the current TOML content.
     *
     * @param patch the JSON patch to be applied.
     * @return a [Result] encapsulating the modified TOML or an exception if the operation fails.
     * @since 3.11.0
     */
    infix fun tomlPatch(patch: Json) = runCatching {
        tryOrThrow({ e -> NoSuchTomlPathException((-Char.COLON(e.message.orEmpty()) - 2)(e.message.orEmpty())) }, overwriteOnly = NoSuchJsonPathException::class) {
            toJson().jsonPatch(patch)().toToml()
        }
    }
}

/**
 * Represents a TOML Node, encapsulating a raw value that can be of various types and providing utilities for
 * interacting with data in a structured manner.
 *
 * @constructor Creates a new TomlNode instance from a given raw value.
 * @param rawValue The raw value to be encapsulated by the node. May hold any type or be null.
 * @since 3.11.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "kutils_collection_declaration", "UNCHECKED_CAST")
@Beta(since = "6.11.0")
class TomlNode(val rawValue: Any?) {
    /**
     * Indicates whether the current TomlNode is missing a value.
     *
     * @since 3.11.0
     */
    val isMissing: Boolean = rawValue.isNull()
    /**
     * Indicates whether the current TOML node represents an array-like structure.
     *
     * @since 3.11.0
     */
    val isArray: Boolean = rawValue is Iterable<*>
    /**
     * Indicates whether the underlying value of this TomlNode instance is a Map (TOML table).
     *
     * @since 3.11.0
     */
    val isMap: Boolean = rawValue is Map<*, *>
    /**
     * Indicates whether the current `TomlNode` represents a JSON object equivalent (i.e. a TOML table).
     *
     * @since 3.11.0
     */
    val isObject: Boolean = rawValue?.toJson()?.isObject == true
    /**
     * Indicates whether the underlying value is a `String`.
     *
     * @since 3.11.0
     */
    val isString: Boolean = rawValue is String
    /**
     * Indicates whether the underlying value is a `Number`.
     *
     * @since 3.11.0
     */
    val isNumber: Boolean = rawValue is Number
    /**
     * Indicates whether the underlying value is an integer-typed number.
     *
     * @since 3.11.0
     */
    val isIntegerNumber: Boolean = rawValue is Number && rawValue.isNotDecimal
    /**
     * Indicates whether the underlying value is a decimal-typed number.
     *
     * @since 3.11.0
     */
    val isDecimalNumber: Boolean = rawValue is Number && rawValue.isDecimal
    /**
     * Indicates whether the underlying value is a `Boolean`.
     *
     * @since 3.11.0
     */
    val isBoolean: Boolean = rawValue is Boolean
    /**
     * Indicates whether the current node represents a `LocalDate` value
     * (TOML "local date" type).
     *
     * @since 3.11.0
     */
    val isDate: Boolean = rawValue is LocalDate
    /**
     * Indicates whether the raw value of this node represents a date-time
     * (TOML offset/local date-time types).
     *
     * @since 3.11.0
     */
    val isDateTime: Boolean = rawValue is OffsetDateTime || rawValue is LocalDateTime || rawValue is Instant
    /**
     * Indicates whether the current node represents a singular value
     * (i.e. is not an array, table, or missing).
     *
     * @since 3.11.0
     */
    val isValue: Boolean = !isArray && !isMap && !isMissing

    companion object {
        private fun missingNode() = TomlNode(null)
        private fun unwrap(value: Any?) = if (value is TomlNode) value.rawValue else value

        /**
         * Converts the current TOML instance into a [TomlNode].
         *
         * @since 3.11.0
         */
        fun Toml.toTomlNode() = TomlNode(toDataMap()())
        /**
         * Wraps the current object into a [TomlNode] representation.
         *
         * @since 3.11.0
         */
        @JvmName("anyToTomlNode")
        fun Any?.toTomlNode() = TomlNode(this)
    }

    /**
     * Retrieves a nested TomlNode based on a dot-separated path.
     *
     * @param dotPath the dot-separated path to the target node.
     * @return the TomlNode at the specified path or a missing node if the path is invalid or node is not found.
     * @since 3.11.0
     */
    operator fun get(dotPath: String): TomlNode {
        if (dotPath.isBlank()) return this

        val keys = dotPath.split(".")
        var current = this

        for (key in keys) {
            if (current.isMissing) return current

            val index = key.toIntOrNull()
            current = if (index.isNotNull() && current.isArray) current[index]
            else current._get(key)
        }
        return current
    }
    /**
     * Retrieves the TomlNode at the specified index within a list-like TOML structure.
     *
     * @since 3.11.0
     */
    operator fun get(index: Int): TomlNode {
        if (rawValue is List<*>)
            if (index >= 0 && index < rawValue.size) {
                return TomlNode(rawValue[index])
            }
        return missingNode()
    }
    @Suppress("FunctionName")
    private fun _get(key: String) = if (rawValue is Map<*, *>) TomlNode(rawValue[key]) else missingNode()

    /**
     * Sets a value in a nested data structure based on a dotted path representation.
     *
     * @since 3.11.0
     */
    operator fun set(dotPath: String, value: Any?) {
        val keys = dotPath.split(".")
        val lastKey = keys.last()
        val parentPath = keys.dropLast(1)

        var current = this
        for (key in parentPath) {
            val index = key.toIntOrNull()

            if (index.isNotNull() && current.isArray)
                current = current[index]
            else {
                if (current[key].isMissing) _set(key, mMapOf<String, Any?>())
                current = current._get(key)
            }
        }

        val lastIndex = lastKey.toIntOrNull()
        if (lastIndex.isNotNull() && current.isArray) current[lastIndex] = value
        else current._set(lastKey, value)
    }
    /**
     * Sets the element at the specified index with the provided value in the underlying mutable list.
     *
     * @since 3.11.0
     */
    operator fun set(index: Int, value: Any?) {
        if (rawValue is MutableList<*>) {
            @Suppress("UNCHECKED_CAST")
            val list = (rawValue as MList<Any?>)
            if (index >= 0 && index < list.size) {
                list[index] = unwrap(value)
            } else {
                throw IndexOutOfBoundsException("Index $index out of bounds (size: ${list.size})")
            }
        } else {
            throw UnsupportedOperationException("Unable to set the index $index: the current node is not a List (is ${rawValue?.javaClass?.simpleName})")
        }
    }
    @Suppress("FunctionName")
    fun _set(key: String, value: Any?) {
        if (rawValue is MutableMap<*, *>) {
            @Suppress("UNCHECKED_CAST")
            (rawValue as DataMMap)[key] = unwrap(value)
        } else {
            throw UnsupportedOperationException("Unable to set the key '$key': the current node is not a Map (is ${rawValue?.javaClass?.simpleName})")
        }
    }

    /**
     * Converts the current TomlNode's raw value to its string representation.
     *
     * @since 3.11.0
     */
    fun asString() = rawValue?.toString()
    /**
     * Converts the value of the current TomlNode to an integer.
     *
     * @since 3.11.0
     */
    fun asInt() = asString()?.toDoubleOrNull()?.toInt()
    /**
     * Converts the current TomlNode to a Long value.
     *
     * @since 3.11.0
     */
    fun asLong() = asString()?.toDoubleOrNull()?.toLong()
    /**
     * Converts the underlying value of the TomlNode to a Double.
     *
     * @since 3.11.0
     */
    fun asDouble() = asString()?.toDoubleOrNull()
    /**
     * Converts the current node's value to a Boolean representation.
     *
     * @since 3.11.0
     */
    fun asBoolean() = asString().toBoolean()
    /**
     * Converts the current raw value of the TOML node into a list, if iterable.
     *
     * @since 3.11.0
     */
    fun <T> asList(): List<T>? = (rawValue as? Iterable<*>)?.map { it as T }
    /**
     * Converts the raw value of this TomlNode into a map representation.
     *
     * @since 3.11.0
     */
    fun <T> asMap(): Map<String, T> = (rawValue as? Map<*, *>)?.mapKeys { it.key.toString() }?.mapValues { it.value as T } ?: emptyMap()
    /**
     * Converts the current TomlNode to a LocalDate object.
     *
     * @since 3.11.0
     */
    fun asDate(): LocalDate? = when (rawValue) {
        is LocalDate -> rawValue
        else -> asString()?.let(::LocalDate)?.getOrThrow()
    }
    /**
     * Converts the current TomlNode to an OffsetDateTime representation.
     *
     * @since 3.11.0
     */
    fun asDateTime(): OffsetDateTime? = when (rawValue) {
        is OffsetDateTime -> rawValue
        else -> asString()?.let(::OffsetDateTime)?.getOrThrow()
    }
    /**
     * Converts the current node to an [Instant] if possible.
     *
     * @since 3.11.0
     */
    fun asInstant(): Instant? = when (rawValue) {
        is Instant -> rawValue
        else -> asString()?.let(::Instant)?.getOrThrow()
    }

    /**
     * Returns a string representation of the TomlNode object.
     *
     * @since 3.11.0
     */
    override fun toString(): String = rawValue.toString()

    /**
     * Checks whether the node at the specified dot-separated path is not marked as missing.
     *
     * @since 3.11.0
     */
    operator fun invoke(dotPath: String) = !get(dotPath).isMissing
    /**
     * Determines whether the TomlNode at the specified index is not missing.
     *
     * @since 3.11.0
     */
    operator fun invoke(index: Int) = !get(index).isMissing
}