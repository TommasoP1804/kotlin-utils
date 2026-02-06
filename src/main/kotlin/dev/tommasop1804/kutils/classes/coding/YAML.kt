@file:Suppress("java_integer_as_kotlin_int")
@file:OptIn(Beta::class)

package dev.tommasop1804.kutils.classes.coding

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.DOT
import dev.tommasop1804.kutils.DataMMap
import dev.tommasop1804.kutils.DataMMapNN
import dev.tommasop1804.kutils.DataMap
import dev.tommasop1804.kutils.DataMapNN
import dev.tommasop1804.kutils.EMPTY
import dev.tommasop1804.kutils.HYPEN
import dev.tommasop1804.kutils.Instant
import dev.tommasop1804.kutils.LF
import dev.tommasop1804.kutils.LocalDate
import dev.tommasop1804.kutils.MList
import dev.tommasop1804.kutils.MMap
import dev.tommasop1804.kutils.MSet
import dev.tommasop1804.kutils.OffsetDateTime
import dev.tommasop1804.kutils.after
import dev.tommasop1804.kutils.annotations.Beta
import dev.tommasop1804.kutils.before
import dev.tommasop1804.kutils.classes.coding.JSON.Companion.MAPPER
import dev.tommasop1804.kutils.classes.coding.JSON.Companion.toJSON
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.expect
import dev.tommasop1804.kutils.invoke
import dev.tommasop1804.kutils.isDecimal
import dev.tommasop1804.kutils.isNotDecimal
import dev.tommasop1804.kutils.isNotNull
import dev.tommasop1804.kutils.isNull
import dev.tommasop1804.kutils.mMapOf
import dev.tommasop1804.kutils.minus
import dev.tommasop1804.kutils.sentenceCase
import dev.tommasop1804.kutils.serialize
import dev.tommasop1804.kutils.startsWith
import dev.tommasop1804.kutils.then
import dev.tommasop1804.kutils.toMList
import dev.tommasop1804.kutils.toMSet
import dev.tommasop1804.kutils.tryOr
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.error.MarkedYAMLException
import org.yaml.snakeyaml.error.YAMLException
import org.yaml.snakeyaml.inspector.TagInspector
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.representer.Represent
import org.yaml.snakeyaml.representer.Representer
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.File
import java.nio.file.Path
import java.time.*
import kotlin.apply
import kotlin.collections.map
import kotlin.collections.mapKeys
import kotlin.collections.mapValues
import org.intellij.lang.annotations.Language as IJLanguage

/**
 * The `YAML` class is a representation of YAML-encoded data. It provides functionality to parse, validate,
 * manipulate, and serialize YAML data. It supports the conversion of YAML content into objects, lists, maps,
 * and other data structures and also facilitates operations such as removing comments or accessing nested 
 * YAML nodes via dot paths.
 *
 * Features include:
 * - Validation of YAML content.
 * - Conversion between YAML and other data formats, including JSON.
 * - Writing and reading YAML from files.
 * - Accessing and modifying nested YAML structures.
 *
 * The class is compatible with JSON serialization and deserialization libraries and leverages SnakeYAML for 
 * underlying parsing and serialization logic.
 *
 * @param value The raw YAML content as a string.
 * @constructor Creates an instance of the YAML class with the given string content or a `YAMLNode`.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = YAML.Companion.Serializer::class)
@JsonDeserialize(using = YAML.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = YAML.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = YAML.Companion.OldDeserializer::class)
@Suppress("unused", "UNCHECKED_CAST", "kutils_collection_declaration")
@Beta(since = "6.11.0")
class YAML(@param:IJLanguage("YAML") override var value: String) : CharSequence, Code(value, Language.YAML) {

    /**
     * Indicates whether the current YAML instance can be represented as an object-like structure (e.g., a map or dictionary).
     * This variable evaluates to `true` if the deserialization of the YAML content via `toDataMap()` is successful.
     *
     * The underlying implementation relies on the result of a safe deserialization attempt using SnakeYAML.
     *
     * @since 1.0.0
     */
    val isObject = toDataMap().isSuccess
    /**
     * Indicates whether the value of the current instance represents an array-like structure.
     *
     * The determination is based on two criteria:
     * - The result of converting the `value` field to a list using the `toList` method is successful.
     * - The trimmed form of the `value` field starts with a hyphen character (`-`).
     *
     * This property is typically used to infer if the underlying YAML structure
     * represents an array (or list) based on its serialized format and conventions.
     *
     * @since 1.0.0
     */
    val isArray = toList<Any>().isSuccess && value.trim() startsWith Char.HYPEN
    /**
     * Indicates whether the current YAML node represents a scalar value.
     * This property evaluates to `true` if the node is neither an array nor an object.
     * 
     * @since 1.0.0
     */
    val isValue = !isArray && !isObject

    /**
     * Constructs a new instance of the YAML class using a given [YAMLNode].
     *
     * @param node The YAMLNode instance whose raw value will be used to initialize the YAML object.
     * The raw value is converted to a string and passed to the primary constructor of the YAML class.
     *
     * @since 1.0.0
     */
    constructor(node: YAMLNode) : this(node.rawValue.toString())

    /**
     * Secondary constructor that initializes an instance using a `Code` object.
     *
     * @param code The `Code` object containing the value to initialize.
     * @since 1.0.0
     */
    constructor(code: Code) : this(code.value) {
        code.language.expect(Language.YAML)
    }

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
        try {
            SNAKE_YAML.load<Any>(value)
        } catch (e: MarkedYAMLException) {
            throw MalformedInputException("${e.problem.sentenceCase()} at line ${e.problemMark.line + 1}, column ${e.problemMark.column + 1}${if (e.context.isNotNull()) " ${e.context}" else String.EMPTY}")
        } catch (e: YAMLException) {
            throw MalformedInputException("${e.message}")
        }
    }

    companion object {
        private val DUMPER_OPTIONS = DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            isPrettyFlow = true
            indent = 2
        }
        private val REPRESENTER = object : Representer(DUMPER_OPTIONS) {
            init {
                multiRepresenters[Set::class.java] = Represent {
                    representSequence(Tag.SEQ, it as Iterable<*>, DumperOptions.FlowStyle.BLOCK)
                }
                multiRepresenters[MSet::class.java] = Represent {
                    representSequence(Tag.SEQ, it as Iterable<*>, DumperOptions.FlowStyle.BLOCK)
                }
                multiRepresenters[LocalDate::class.java] = Represent { representScalar(Tag.TIMESTAMP, it.toString()) }
                multiRepresenters[LocalDateTime::class.java] = Represent { representScalar(Tag.TIMESTAMP, it.toString()) }
                multiRepresenters[ZonedDateTime::class.java] = Represent { representScalar(Tag.STR, it.toString()) }
                multiRepresenters[Instant::class.java] = Represent { representScalar(Tag.TIMESTAMP, it.toString()) }
                multiRepresenters[CharSequence::class.java] = Represent { representScalar(Tag.STR, it.toString()) }
                multiRepresenters[Any::class.java] = Represent {
                    when {
                        it is YAMLNode -> when {
                            it.isArray -> representSequence(Tag.SEQ, it.asList<Any>(), DumperOptions.FlowStyle.BLOCK)
                            it.isMap -> representMapping(Tag.MAP, it.asMap<Any>(), DumperOptions.FlowStyle.BLOCK)
                            it.isObject -> representMapping(Tag.MAP, it.toJSON().toMap<Any>()(), DumperOptions.FlowStyle.BLOCK)
                            else -> representScalar(Tag.STR, it.rawValue.toString())
                        }
                        it is CharSequence -> representScalar(Tag.STR, it.toString())
                        it is Iterable<*> -> representSequence(Tag.SEQ, it, DumperOptions.FlowStyle.BLOCK)
                        it.toJSON().isObject -> representMapping(Tag.MAP, it.toJSON().toMap<Any>()(), DumperOptions.FlowStyle.BLOCK)
                        else -> representScalar(Tag.STR, it.serialize())
                    }
                }
            }
        }
        private val LOADER_OPTIONS = LoaderOptions().apply {
            tagInspector = TagInspector { _ -> true }
        }
        private val CONSTRUCTOR = Constructor(LOADER_OPTIONS)
        val SNAKE_YAML = Yaml(CONSTRUCTOR, REPRESENTER, DUMPER_OPTIONS, LOADER_OPTIONS)

        /**
         * Checks if the String represents a valid YAML structure.
         * 
         * This method attempts to parse the String as YAML using the `YAML` class.
         * If the parsing succeeds, the String is considered valid YAML.
         * Otherwise, it is considered invalid.
         * 
         * @receiver The String to be validated as YAML.
         * @return `true` if the String is valid YAML; `false` otherwise.
         * @since 1.0.0
         */
        fun String.isValidYAML() = runCatching { YAML(this) }

        /**
         * Converts the current `String` into a YAML representation and wraps the operation in a `Result`.
         * 
         * This method attempts to parse the content of the `String` as YAML, creating an instance of the `YAML` class.
         * If the operation is successful, the `Result` will contain the parsed `YAML` object; otherwise, it will contain the exception.
         *
         * @receiver The `String` to be converted to YAML.
         * @return A `Result` that either contains the parsed YAML object or an exception if parsing fails.
         * @since 1.0.0
         */
        fun String.toYAML() = runCatching { YAML(this) }
        /**
         * Converts the current `JSON` instance into its equivalent `YAML` representation.
         *
         * This function transforms the data held in the `JSON` object into the corresponding
         * `YAML` format, preserving the structure and content of the original data.
         *
         * @return A `YAML` instance representing the data structure of the original `JSON` input.
         * @since 1.0.0
         */
        @JvmName("jsonToYaml")
        fun JSON.toYAML(): YAML {
            val obj = toObject<Any>()()
            return obj.toYAML()
        }
        /**
         * Converts the given object to its YAML representation.
         *
         * @param includeTag Specifies whether to include the YAML type tag in the generated output. 
         *                   If `true`, the type tag is included; if `false`, it is omitted. Default is `true`.
         * @return The YAML representation of the object as an instance of the `YAML` class.
         * @since 1.0.0
         */
        @JvmName("anyToYaml")
        fun Any.toYAML(includeTag: Boolean = true): YAML {
            val value1 = SNAKE_YAML.dump(this)!!
            return YAML(
                if (includeTag) value1
                else if (value1 startsWith "!!") value1 after Char.LF
                else value1
            )
        }

        /**
         * Reads the content of the specified file and parses it into a YAML object.
         *
         * @param file the file to be read and parsed as YAML
         * @return a Result containing the parsed YAML object if the operation is successful, 
         * or an exception if an error occurs
         * @since 1.0.0
         */
        fun <T> readFromFile(file: File): Result<T> = runCatching { SNAKE_YAML.load(file.readText()) }
        /**
         * Reads an array of the specified type from the given file.
         *
         * This method attempts to read the contents of the file and convert it into an array
         * of the specified type `T`. The operation is wrapped in a `Result` object to capture
         * success or failure without throwing exceptions directly.
         *
         * @param T The type of elements to be read and stored in the resulting array.
         * @param file The file from which the array is read.
         * @return A `Result` containing the array of type `T` if the operation succeeds, 
         * or the encapsulated exception if the operation fails.
         * @since 1.0.0
         */
        inline fun <reified T> readArrayFromFile(file: File): Result<Array<T>> = runCatching { readListFromFile<T>(file)().toTypedArray() }
        /**
         * Reads and parses a list of objects from the specified file.
         *
         * The file is expected to be in a YAML format, and the contents
         * will be deserialized into a list of objects of type [T].
         *
         * @param file The file to read from. It should contain YAML-formatted data.
         * @return A [Result] containing the parsed list of objects of type [T], 
         * or an exception if the operation fails.
         * @since 1.0.0
         */
        fun <T> readListFromFile(file: File): Result<List<T>> = runCatching { YAML(file.readText()).toList<T>()() }
        /**
         * Reads the content of a given file, parses it as YAML, and converts it to a set of type `T`.
         *
         * This method attempts to interpret the content of the specified file as YAML
         * and extract a set of elements of type `T`. The operation is performed within
         * a `Result` context to handle possible exceptions that might occur during file
         * reading or data parsing.
         *
         * @param file The file to be read, whose content is expected to be in YAML format.
         * @return A [Result] containing a [Set] of elements of type `T` if the operation is successful. 
         * In case of failure, the [Result] will encapsulate the exception.
         * @since 1.0.0
         */
        fun <T> readSetFromFile(file: File): Result<Set<T>> = runCatching { YAML(file.readText()).toSet<T>()() }
        /**
         * Reads the contents of a specified file and parses it into a map structure from YAML format.
         *
         * The method expects the file to contain valid YAML data. It then converts the YAML content
         * into a `Map` where the keys are `String` and the values are of the generic type `T`.
         * Any errors during reading or parsing the file are encapsulated in a `Result` object.
         *
         * @param file The file containing the YAML data to be parsed.
         * @return A [Result] containing the parsed map with keys as `String` and values of type `T`
         *         on success, or an exception on failure.
         * @since 1.0.0
         */
        fun <T> readMapFromFile(file: File): Result<Map<String, T>> = runCatching { YAML(file.readText()).toMap<T>()() }

        class Serializer : ValueSerializer<YAML>() {
            override fun serialize(value: YAML, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                val node = MAPPER.readTree(value.value)
                when {
                    node.isArray -> {
                        val arrayValue = value.toList<Any>()()
                        gen.writePOJO(arrayValue)
                    }
                    node.isObject -> {
                        val mapValue = value.toMap<Any>()()
                        gen.writePOJO(mapValue)
                    }
                    else -> gen.writeRaw(value.toJSON().value)
                }
            }
        }

        class Deserializer : ValueDeserializer<YAML>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = JSON(p.objectReadContext().readTree<JsonNode>(p).toString()).toYAML()
        }

        class OldSerializer : JsonSerializer<YAML>() {
            override fun serialize(value: YAML, gen: JsonGenerator, serializers: SerializerProvider) {
                val node = MAPPER.readTree(value.value)
                when {
                    node.isArray -> {
                        val arrayValue = value.toList<Any>()()
                        gen.writeObject(arrayValue)
                    }
                    node.isObject -> {
                        val mapValue = value.toMap<Any>()()
                        gen.writeObject(mapValue)
                    }
                    else -> gen.writeRaw(value.toJSON().value)
                }
            }
        }

        class OldDeserializer : JsonDeserializer<YAML>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): YAML = JSON(p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p).toString()).toYAML()
        }
    }

    /**
     * Converts the YAML content represented by this instance into an object of type [T].
     *
     * @param T The target type to which the YAML content will be converted.
     * @return A [Result] containing an instance of type [T] if the conversion succeeds,
     *         or an exception if the conversion fails.
     * @throws IllegalArgumentException if the value cannot be converted to the specified type.
     * @since 1.0.0
     */
    inline fun <reified T> toObject() = runCatching {
        tryOr({ toJSON().toObject<T>()() }) {
            SNAKE_YAML.loadAs(value, T::class.java)!!
        }
    }

    /**
     * Converts the YAML content into an array of type `T`.
     *
     * This function uses SnakeYAML's `loadAll` method to parse the YAML string stored in the `value` field
     * of the containing class, then maps the resulting objects to the specified type `T` and produces
     * an array of `T`. The operation is wrapped in a `Result` using `runCatching` to handle any parsing
     * or type casting errors.
     *
     * @param T The type to which the YAML objects should be cast.
     * @return A `Result` containing an array of type `T` if the conversion is successful, or the exception if it fails.
     * @throws ClassCastException If the YAML content cannot be cast to `T` at runtime.
     * @throws YAMLException If the YAML content is malformed.
     *
     * @since 1.0.0
     */
    inline fun <reified T> toArray() = runCatching { SNAKE_YAML.loadAll(value).map { it as T }.toTypedArray() }

    /**
     * Converts the YAML content stored in the `value` property into a list of objects of type `T`.
     * The method uses the SnakeYAML library to parse the YAML content and map it to a collection.
     * Any parsing errors will be captured and returned as a `Result` object.
     *
     * @param T The type to which each element in the resulting list will be cast.
     * @return A `Result` wrapping either the successfully parsed list of objects or any exception encountered during parsing.
     * @throws ClassCastException If an element in the YAML content cannot be cast to the specified type `T`.
     * @since 1.0.0
     */
    fun <T> toList() = runCatching { SNAKE_YAML.loadAll(value).map { it as T } }
    /**
     * Parses the YAML content stored in the current object and converts it into a mutable list of type [T].
     * 
     * Uses SnakeYAML to process the content and attempts to cast each deserialized element to the specified type.
     * 
     * @param T The type of elements in the resulting mutable list.
     * @return A [Result] containing the mutable list of type [T], or an exception if the operation fails.
     * @throws ClassCastException If any element in the YAML content cannot be cast to the specified type [T].
     * @since 1.0.0
     */
    fun <T> toMList() = runCatching { SNAKE_YAML.loadAll(value).map { it as T }.toMList() }

    /**
     * Converts the YAML content represented by `value` into a set of objects of type `T`.
     * This method uses SnakeYAML to parse the content and extract all objects, casting 
     * each to the specified type `T` and collecting them into an immutable set.
     * 
     * Any exception encountered during parsing or casting will be encapsulated 
     * within a `Result` using the `runCatching` construct.
     *
     * @param T The type of the elements in the resulting set.
     * @return A `Result` containing the set of objects of type `T` or an exception 
     *         if an error occurs.
     * @since 1.0.0
     */
    fun <T> toSet() = runCatching { SNAKE_YAML.loadAll(value).map { it as T }.toSet() }
    /**
     * Parses the YAML content stored in the `value` field and converts it into a mutable set of elements of type T.
     *
     * The method uses the SnakeYAML library to process the YAML content, loading all elements and casting them to the 
     * specified type T. These elements are then collected and transformed into a mutable set.
     *
     * @return A `Result` wrapping a mutable set of type T, containing the parsed and distinct elements from the YAML content.
     *         If parsing or type casting fails, a `Failure` with the corresponding exception is returned.
     * @since 1.0.0
     */
    fun <T> toMSet() = runCatching { SNAKE_YAML.loadAll(value).map { it as T }.toMSet() }

    /**
     * Converts the underlying YAML content into a map structure of key-value pairs.
     *
     * The method utilizes the SnakeYAML library to parse the YAML content stored in the `value` property
     * and transform it into a `Map<String, T>`. If the parsing process encounters an error, the result 
     * is wrapped in a `Result` instance, allowing safe handling of potential exceptions.
     *
     * @param V The type of values expected in the resulting map.
     * @return A `Result<Map<String, T>>` containing the parsed map if successful or the exception if an error occurred.
     * @throws IllegalStateException If the YAML content cannot be parsed due to formatting issues or data type mismatches.
     * @since 1.0.0
     */
    fun <V> toMap() = runCatching { SNAKE_YAML.load<Map<String, V>>(value)!! }
    /**
     * Converts a YAML string value into a mutable map (`MMap`) with string keys and values of type `T`.
     *
     * This method utilizes SnakeYAML to parse the YAML input and transform it into the desired data structure.
     * If the parsing process fails or the input is invalid, the result will encapsulate the error within a `Result` object.
     *
     * @param V The type of the values in the resulting mutable map.
     * @return A `Result` containing the parsed `MMap<String, T>` on success, or an exception on failure.
     * @throws NullPointerException If the YAML parsing result is null.
     * @since 1.0.0
     */
    fun <V> toMMap() = runCatching { SNAKE_YAML.load<MMap<String, V>>(value)!! }
    /**
     * Converts the stored YAML content in the `value` field into a `DataMap` object.
     * This method uses the SnakeYAML library to parse the YAML content.
     * If the conversion is successful, the resulting `DataMap` is returned wrapped in a `Result`.
     * If parsing fails, the exception is caught and returned within the `Result` object.
     *
     * @return A `Result` containing either the parsed `DataMap` object or an exception if parsing fails.
     * @since 1.0.0
     */
    fun toDataMap() = runCatching { SNAKE_YAML.load<DataMap>(value)!! }
    /**
     * Converts the content of the current YAML instance to a mutable map representation of `DataMMap`.
     * The method utilizes the SNAKE_YAML library to perform the YAML parsing and returns the result
     * wrapped in a `Result` object. Parsing errors are caught and encapsulated within the `Result`.
     *
     * @return A `Result` containing the parsed `DataMMap` or the exception in case of a failure.
     * @since 1.0.0
     */
    fun toDataMMap() = runCatching { SNAKE_YAML.load<DataMMap>(value)!! }
    /**
     * Parses the YAML content stored in the `value` field and converts it into a non-nullable `DataMapNN` object.
     * Utilizes the SnakeYAML library to perform the deserialization.
     *
     * @return A `Result` wrapping the successfully parsed `DataMapNN` object if the operation succeeds.
     *         If the operation fails (e.g., due to invalid YAML structure or type mismatch), 
     *         the result will contain the exception.
     *
     * @throws IllegalStateException If the YAML content is null or cannot be converted to `DataMapNN`.
     *
     * @since 1.0.0
     */
    fun toDataMapNN() = runCatching { SNAKE_YAML.load<DataMapNN>(value)!! }
    /**
     * Attempts to parse the current YAML value into a non-nullable [DataMMapNN] object.
     *
     * This method uses the SnakeYAML library to deserialize the value into
     * an instance of [DataMMapNN]. If the value cannot be parsed or is null,
     * an exception is captured and returned as a failed [Result].
     * 
     * @return [Result] containing either the successfully parsed [DataMMapNN] object or an exception.
     * @throws NullPointerException if the YAML value is parsed as `null`.
     * @since 1.0.0
     */
    fun toDataMMapNN() = runCatching { SNAKE_YAML.load<DataMMapNN>(value)!! }

    /**
     * Retrieves the element at the specified index from the value.
     *
     * @param index The position of the element to retrieve. Must be within the bounds of the value.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     * @since 1.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence, starting at the specified 
     * [startIndex] (inclusive) and ending at the specified [endIndex] (exclusive).
     *
     * @param startIndex the start index (inclusive) of the subsequence.
     * @param endIndex the end index (exclusive) of the subsequence.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns a string representation of the object.
     *
     * This method is typically overridden to provide a meaningful description 
     * of the instance content or state.
     *
     * @return A string representation of the object.
     * @since 1.0.0
     */
    override fun toString() = value

    /**
     * Writes the content of a specified text value to the provided file.
     *
     * @param file The file to which the text content will be written.
     * @since 1.0.0
     */
    fun writeToFile(file: File) = file.writeText(value)

    /**
     * Retrieves a YAML node based on the provided dot-separated path.
     *
     * @param dotPath The dot-separated string representing the path to the desired YAML node.
     * @return The YAMLNode corresponding to the specified path.
     * @since 1.0.0
     */
    operator fun get(dotPath: String) =
        if (Char.DOT !in dotPath) YAMLNode(toDataMap()()[dotPath])
        else YAMLNode(toDataMap()()[dotPath before Char.DOT])[dotPath after Char.DOT]

    /**
     * Sets the value at the specified dot-delimited path within the YAML structure.
     *
     * @param dotPath The dot-delimited path to identify the location in the YAML structure where the value should be set.
     * @param value The value to be set at the specified path. It can be null.
     * @since 1.0.0
     */
    operator fun set(dotPath: String, value: Any?) {
        val rootNode = YAMLNode(toDataMap()())
        rootNode[dotPath] = value
        this.value = rootNode.rawValue!!.toYAML().value
    }

    /**
     * Removes comments from a given string based on the pattern defined by the regex.
     * The method specifically targets content starting with a `#` symbol and removes it,
     * along with everything that follows on the same line.
     *
     * This function modifies the string by eliminating segments that match the regex pattern.
     *
     * @since 1.0.0
     */
    fun removeComments() {
        value -= Regex("#.*")
    }
}

/**
 * Represents a YAML Node, encapsulating a raw value that can be of various types and providing utilities for
 * interacting with data in a structured manner.
 *
 * @constructor Creates a new YAMLNode instance from a given raw value.
 * @param rawValue The raw value to be encapsulated by the node. May hold any type or be null.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "kutils_collection_declaration", "UNCHECKED_CAST")
@Beta(since = "8.0.0")
class YAMLNode(val rawValue: Any?) {
    /**
     * Indicates whether the current YAMLNode is missing a value.
     *
     * This property evaluates to `true` if the underlying `rawValue` is `null`, 
     * otherwise it evaluates to `false`. Can be utilized to quickly determine 
     * if the node lacks any assigned value.
     *
     * @since 1.0.0
     */
    val isMissing: Boolean = rawValue.isNull()
    /**
     * Indicates whether the current YAML node represents an array-like structure.
     * This is determined by checking if the underlying raw value is an instance of `Iterable`.
     *
     * @since 1.0.0
     */
    val isArray: Boolean = rawValue is Iterable<*>
    /**
     * Indicates whether the underlying value of this YAMLNode instance is a Map.
     * This is determined by checking if the `rawValue` field is of type `Map<*, *>`.
     *
     * @return `true` if the `rawValue` is a Map, otherwise `false`.
     *
     * @since 1.0.0
     */
    val isMap: Boolean = rawValue is Map<*, *>
    /**
     * Indicates whether the current `YAMLNode` represents a JSON object.
     *
     * This property is determined by converting the `rawValue` of the node to a JSON representation 
     * and verifying if the resulting structure is a JSON object.
     *
     * @receiver `YAMLNode` instance whose `rawValue` is evaluated.
     * @return `true` if the node is a JSON object; `false` otherwise.
     * @since 1.0.0
     */
    val isObject: Boolean = rawValue?.toJSON()?.isObject == true
    /**
     * Indicates whether the underlying value of this `YAMLNode` instance is a `String`.
     *
     * The value is determined based on the runtime type of the internal `rawValue`.
     *
     * @return `true` if the `rawValue` is a `String`, otherwise `false`.
     *
     * @since 1.0.0
     */
    val isString: Boolean = rawValue is String
    /**
     * Indicates whether the underlying value of the YAML node is of type `Number`.
     * This property evaluates to `true` if the `rawValue` is an instance of a numeric type, 
     * otherwise it evaluates to `false`.
     *
     * @since 1.0.0
     */
    val isNumber: Boolean = rawValue is Number
    /**
     * Indicates whether the raw value of this YAML node represents an integer number.
     * The check ensures that the raw value is of type `Number` and it does not have a decimal component.
     *
     * @since 1.0.0
     */
    val isIntegerNumber: Boolean = rawValue is Number && rawValue.isNotDecimal
    /**
     * Determines whether the underlying `rawValue` represents a decimal number.
     *
     * The property evaluates to `true` if `rawValue` is of type `Number` and is explicitly a decimal
     * type, distinguishing it from integers. Otherwise, it evaluates to `false`.
     *
     * @since 1.0.0
     */
    val isDecimalNumber: Boolean = rawValue is Number && rawValue.isDecimal
    /**
     * Indicates whether the underlying value of the node is a Boolean type.
     *
     * This property evaluates `true` if the `rawValue` of the YAML node is a `Boolean`.
     * It can be used to determine the type of the value stored in the node for type-safe operations.
     *
     * @see YAMLNode.asBoolean
     * @see YAMLNode.rawValue
     * @since 1.0.0
     */
    val isBoolean: Boolean = rawValue is Boolean
    /**
     * Indicates whether the current YAML node represents a value that can be interpreted as a `LocalDate`.
     * 
     * The value is determined based on the type of the `rawValue` field.
     *
     * @since 1.0.0
     */
    val isDate: Boolean = rawValue is LocalDate
    /**
     * Indicates whether the raw value of this node represents a valid date-time or instant object.
     * The raw value is considered a date-time if it is an instance of [OffsetDateTime] or [Instant] or [LocalDateTime].
     *
     * @since 1.0.0
     */
    val isDateTime: Boolean = rawValue is OffsetDateTime || rawValue is LocalDateTime || rawValue is Instant
    /**
     * Indicates whether the current node represents a singular value in the YAML structure.
     * A node is considered a value if it is not an array, map, or missing.
     *
     * @since 1.0.0
     */
    val isValue: Boolean = !isArray && !isMap && !isMissing

    companion object {
        private fun missingNode() = YAMLNode(null)
        private fun unwrap(value: Any?) = if (value is YAMLNode) value.rawValue else value

        /**
         * Converts the current YAML instance into a `YAMLNode`.
         *
         * Depending on the structure of the YAML data, it creates a `YAMLNode` 
         * representation. If the data is an array, it is converted into a list. 
         * Otherwise, a map representation is created.
         *
         * @param includeTag Specifies whether the tag information should be included during the conversion. 
         *                   Defaults to true.
         * @since 1.0.0
         */
        fun YAML.toYAMLNode(includeTag: Boolean = true) = YAMLNode(if (isArray) toList<Any>() else toDataMap()())
        /**
         * Converts the current object into a YAMLNode representation.
         *
         * @param includeTag Indicates whether to include the YAML tag in the resulting node. 
         *                   If true, the tag will be included; otherwise, it will be omitted.
         *                   Defaults to true.
         * @since 1.0.0
         */
        @JvmName("anyToYAMLNode")
        fun Any?.toYAMLNode(includeTag: Boolean = true) = YAMLNode(this)
    }

    /**
     * Retrieves a nested YAMLNode based on a dot-separated path.
     *
     * This function allows accessing nodes within a YAML structure 
     * using a string that represents the desired path. The path 
     * components are split by dots and traversed sequentially. 
     * If any part of the path is missing, a missing node is returned.
     *
     * @param dotPath the dot-separated path to the target node.
     * @return the YAMLNode at the specified path or a missing node 
     *         if the path is invalid or node is not found.
     * @since 1.0.0
     */
    operator fun get(dotPath: String): YAMLNode {
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
     * Retrieves the YAMLNode at the specified index within a list-like YAML structure.
     * If the index is out of bounds or the current node is not a list, a missing node is returned.
     *
     * @param index The position of the desired node in the list structure. 
     *              Must be a non-negative integer within the bounds of the list.
     * @return The YAMLNode at the specified index if the current node is a list 
     *         and the index is valid; otherwise, a missing node.
     * @since 1.0.0
     */
    operator fun get(index: Int): YAMLNode {
        if (rawValue is List<*>)
            if (index >= 0 && index < rawValue.size) {
                return YAMLNode(rawValue[index])
            }
        return missingNode()
    }
    @Suppress("FunctionName")
    private fun _get(key: String) = if (rawValue is Map<*, *>) YAMLNode(rawValue[key]) else missingNode()

    /**
     * Sets a value in a nested data structure based on a dotted path representation.
     *
     * @param dotPath A string representing the path to the target property, where nested properties are separated by dots.
     * @param value The value to set at the specified path. Can be of any type.
     * @since 1.0.0
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
     * @param index The position in the list where the value should be set.
     * @param value The new value to be assigned at the specified index.
     * @throws IndexOutOfBoundsException If the specified index is out of the list's bounds.
     * @throws UnsupportedOperationException If the underlying raw value is not a mutable list.
     * @since 1.0.0
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
            // Cast non controllato necessario perché SnakeYAML usa mappe generiche
            @Suppress("UNCHECKED_CAST")
            (rawValue as DataMMap)[key] = unwrap(value)
        } else {
            throw UnsupportedOperationException("Unable to set the key '$key': the current node is not a Map (is ${rawValue?.javaClass?.simpleName})")
        }
    }

    /**
     * Converts the current YAMLNode's raw value to its string representation.
     *
     * @return The string representation of the raw value if it exists
     * @since 1.0.0
     */
    fun asString() = rawValue?.toString()
    /**
     * Converts the value of the current YAMLNode to an integer.
     *
     * This method attempts to parse the node's value as a string, 
     * convert it to a number if possible.
     *
     * @return The integer representation of the node's value
     * @since 1.0.0
     */
    fun asInt() = asString()?.toDoubleOrNull()?.toInt()
    /**
     * Converts the current YAMLNode to a Long value.
     * 
     * If the node represents a numeric string (e.g., "10.0"), it will be parsed as a Double 
     * and converted to Long.
     * 
     * @return the Long representation of the YAMLNode's value
     * @since 1.0.0
     */
    fun asLong() = asString()?.toDoubleOrNull()?.toLong()
    /**
     * Converts the underlying value of the YAMLNode to a Double.
     * 
     * If the value is not directly convertible to a Double, it will return 0.0.
     * 
     * @return the value of the node as a Double, or 0.0 if conversion fails.
     * @since 1.0.0
     */
    fun asDouble() = asString()?.toDoubleOrNull()
    /**
     * Converts the current node's value to a Boolean representation.
     *
     * This method interprets the string value of the current YAML node as
     * a Boolean. It relies on Kotlin's `String.toBoolean()` implementation 
     * to perform the conversion, which recognizes the case-insensitive text
     * "true" as `true` and any other value as `false`.
     *
     * @return the Boolean representation of the node's value, or `false`
     *         if the string value does not match "true".
     * @since 1.0.0
     */
    fun asBoolean() = asString().toBoolean()
    /**
     * Converts the current raw value of the YAML node into a list of YAMLNode objects,
     * if the raw value is iterable. 
     * If the raw value is null or not iterable, returns an empty list.
     *
     * @return a list containing YAMLNode objects constructed from elements of the underlying iterable raw value,
     *         or an empty list if the raw value is not iterable.
     * @since 1.0.0
     */
    fun <T> asList(): List<T>? = (rawValue as? Iterable<*>)?.map { it as T }
    /**
     * Converts the raw value of this YAMLNode into a map representation.
     *
     * This function attempts to interpret the underlying `rawValue` as a map structure.
     * It converts the keys to strings and maps the values to `YAMLNode` instances.
     * If the `rawValue` is not a map or is null, an empty map is returned.
     *
     * @return a map where the keys are strings derived from the original map keys
     *         and the values are corresponding `YAMLNode` instances. Returns an empty map if
     *         the `rawValue` is not a map or is null.
     * @since 1.0.0
     */
    fun <T> asMap(): Map<String, T> = (rawValue as? Map<*, *>)?.mapKeys { it.key.toString() }?.mapValues { it.value as T } ?: emptyMap()
    /**
     * Converts the current YAMLNode to a LocalDate object.
     *
     * This method interprets the YAMLNode's value as a string and attempts to parse it into a LocalDate.
     * The parsing uses the ISO_LOCAL_DATE format. If the string cannot be parsed, an exception is thrown.
     *
     * @return the LocalDate representation of the node's value.
     * @since 1.0.0
     */
    fun asDate() = asString()?.then(::LocalDate)?.getOrThrow()
    /**
     * Converts the current YAMLNode to an OffsetDateTime representation.
     *
     * This function assumes the node's string value represents a valid date-time
     * with an offset in a format compatible with ISO_OFFSET_DATE_TIME.
     * An exception will be thrown if parsing fails.
     *
     * @return an OffsetDateTime object parsed from the node's value.
     * @since 1.0.0
     */
    fun asDateTime(): OffsetDateTime? = asString()?.then(::OffsetDateTime)?.getOrThrow()
    /**
     * Converts the current node to an [Instant] if possible.
     *
     * This method attempts to parse the string representation of the current YAML node
     * as an [Instant] using the ISO-8601 format. If parsing fails or the value cannot be
     * represented as an [Instant], an exception is thrown.
     *
     * @return The parsed [Instant] instance representing the node's value.
     * @since 1.0.0
     */
    fun asInstant(): Instant? = asString()?.then(::Instant)?.getOrThrow()

    /**
     * Returns a string representation of the YAMLNode object.
     *
     * This implementation delegates to the `toString` method
     * of the underlying raw value contained within the node.
     *
     * @return a string representation of the raw value encapsulated by this node.
     * @since 1.0.0
     */
    override fun toString(): String = rawValue.toString()

    /**
     * Checks whether the node at the specified dot-separated path is not marked as missing.
     *
     * This function provides a convenient way to determine the presence of a value at
     * a specific path within a YAML structure.
     *
     * @param dotPath the dot-separated path to the target node.
     * @return `true` if the node at the specified path exists and is not marked as missing; otherwise, `false`.
     * @since 1.0.0
     */
    operator fun invoke(dotPath: String) = !get(dotPath).isMissing
    /**
     * Determines whether the YAMLNode at the specified index is not missing.
     *
     * This operator function checks if the node retrieved at the given index
     * of a list-like YAML structure is not marked as missing. A missing node
     * implies that the given index is either out of bounds or that the current
     * node is not a list. The operation relies on the `isMissing` property of
     * the accessed node.
     *
     * @param index The index of the node to check. Must be a non-negative integer.
     * @since 1.0.0
     */
    operator fun invoke(index: Int) = !get(index).isMissing
}