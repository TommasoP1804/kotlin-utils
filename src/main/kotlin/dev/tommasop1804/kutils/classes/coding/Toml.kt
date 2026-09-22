/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("java_integer_as_kotlin_int")
@file:OptIn(Beta::class)
@file:MustUseReturnValues

package dev.tommasop1804.kutils.classes.coding

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.Instant
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.Json.Companion.MAPPER
import dev.tommasop1804.kutils.classes.coding.Json.Companion.toJson
import dev.tommasop1804.kutils.classes.collections.NonEmptyList.Companion.toNonEmptyList
import dev.tommasop1804.kutils.classes.collections.NonEmptyMList.Companion.toNonEmptyMList
import dev.tommasop1804.kutils.classes.collections.NonEmptyMSet.Companion.toNonEmptyMSet
import dev.tommasop1804.kutils.classes.collections.NonEmptySet.Companion.toNonEmptySet
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.classes.maps.NonEmptyMMap.Companion.toNonEmptyMMap
import dev.tommasop1804.kutils.classes.maps.NonEmptyMap.Companion.toNonEmptyMap
import dev.tommasop1804.kutils.errors.*
import dev.tommasop1804.kutils.exceptions.*
import org.jetbrains.exposed.v1.core.Table
import org.tomlj.TomlArray
import org.tomlj.TomlParseResult
import org.tomlj.TomlTable
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.File
import java.nio.file.Path
import java.time.*
import kotlin.reflect.typeOf
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
class Toml(@param:IJLanguage("TOML") override var value: String) : CharSequence, Code(value, Language.Toml) {
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
        code.language.expect(Language.Toml)
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
        fun String.isValidToml() = runCatching { Toml(this) }.isSuccess

        /**
         * Converts the current File instance to a Toml representation.
         *
         * This method attempts to parse the content of the file into a Toml object.
         * If the conversion fails, it wraps the exception into an InvalidConversion error,
         * specifying the source type File, the target type Toml, and the underlying throwable.
         *
         * @return Either a successfully parsed Toml instance or an InvalidConversion error encapsulated in a catching block.
         * @since 6.1.0
         */
        fun File.toToml() = either {
            catching({ Toml(this@toToml) }) { t: Throwable ->
                InvalidConversionBetweenTypes(this@toToml, typeOf<File>(), typeOf<Toml>(), t)
            }
        }
        /**
         * Converts the current Path instance to a Toml representation.
         *
         * This method attempts to parse the content of the file into a Toml object.
         * If the conversion fails, it wraps the exception into an InvalidConversion error,
         * specifying the source type Path, the target type Toml, and the underlying throwable.
         *
         * @return Either a successfully parsed Toml instance or an InvalidConversion error encapsulated in a catching block.
         * @since 6.1.0
         */
        fun Path.toToml() = either {
            catching({ Toml(this@toToml) }) { t: Throwable ->
                InvalidConversionBetweenTypes(this@toToml, typeOf<Path>(), typeOf<Toml>(), t)
            }
        }
        /**
         * Parses the current string into a Toml object.
         *
         * The method attempts to parse the receiver string as TOML content.
         * If parsing is successful, it returns a validated result either containing
         * the parsed Toml object or a failure encapsulating an exception
         * caused by invalid formatting.
         *
         * @receiver The string to be parsed as TOML.
         * @return A validated result containing either the parsed Toml object if successful,
         *         or an InvalidFormat error if the parsing fails.
         *
         * @since 6.1.0
         */
        fun @receiver:IJLanguage("toml") String.toToml() = either {
            catching({ Toml(this@toToml) }) { t: Throwable ->
                InvalidFormatOfType(this@toToml, typeOf<Toml>(), t)
            }
        }
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
                is Collection<*> -> mapOf("value" to this)
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
         * Reads and deserializes the content of a given file into an object of type [T].
         *
         * Possible erorrs:
         * - [InvalidFormatOfType] - if the file does not contain a valid TOML representation
         * - [DeserializationError.MappingError] - if conversion failed
         *
         * @param file The file to read and deserialize.
         * @return An [Either] containing a [DeserializationError.MappingError] on failure
         *         or an object of type [T] on success.
         * @since 6.1.0
         */
        inline fun <reified T> readFromFile(file: File): Either<Error, T> = (either {
            catching({ Toml(file.readText()) }) { e: MalformedInputException ->
                InvalidFormatOfType(file, typeOf<Toml>(), e)
            }
        } thenEither { it.toObject<T>() }).flatten()
        /**
         * Reads an array of type [T] from the specified file.
         *
         * This method attempts to read a list from the given file and convert it to an array of the specified type [T].
         *
         * Possible errors:
         * - [InvalidFormatOfType] - if the file does not contain a valid TOML representation
         * - [DeserializationError.MappingError] - if conversion failed
         *
         * @param file The file from which the array will be read.
         * @return An [Either] containing an [Error] if the operation fails, or an array of type [T] if successful.
         * @since 6.1.0
         */
        inline fun <reified T> readArrayFromFile(file: File): Either<Error, Array<T>> = readListFromFile<T>(file).map { it.toTypedArray() }
        /**
         * Reads and parses a list of objects of type `T` from the specified file.
         * The file is expected to contain a TOML representation of the data.
         *
         * Possible errors:
         * - [InvalidFormatOfType] - if the file does not contain a valid TOML representation
         * - [DeserializationError.MappingError] - if conversion failed
         *
         * @param T The type of the objects to read from the file.
         * @param file The file from which the list of objects will be read.
         * @return Either an [Error] if the operation fails, or a [List] of objects of type `T` if the operation succeeds.
         * @since 6.1.0
         */
        inline fun <reified T> readListFromFile(file: File): Either<Error, List<T>> = (either {
            catching({ Toml(file.readText()) }) { e: MalformedInputException ->
                InvalidFormatOfType(file, typeOf<Toml>(), e)
            }
        } thenEither { it.toList<T>() }).flatten()
        /**
         * Reads a file and parses its contents into a set of elements of type [T].
         *
         * The file is expected to contain serialized data that can be deserialized into objects of type [T].
         * This method relies on the reified type [T] for deserialization and any parsing failures will be represented
         * as an error in the resulting `Either` type.
         *
         * Possible errors:
         * - [InvalidFormatOfType] - if the file does not contain a valid TOML representation
         * - [DeserializationError.MappingError] - if conversion failed
         *
         * @param file The file to be read and parsed.
         * @return An [Either] containing a parsed [Set] of elements of type [T] if successful, or an [Error] instance representing the problem encountered.
         * @since 6.1.0
         */
        inline fun <reified T> readSetFromFile(file: File): Either<Error, Set<T>> = readListFromFile<T>(file).map { it.toSet() }
        /**
         * Reads a TOML file and parses its content into a map.
         *
         * Possible errors:
         * - [InvalidFormatOfType] - if the file does not contain a valid TOML representation
         * - [DeserializationError.MappingError] - if conversion failed
         *
         * @param file The file to be read and parsed.
         * @return An [Either] containing an error if the parsing fails, or a map with keys as strings
         *         and values of type [T] if the parsing succeeds.
         * @since 6.1.0
         */
        inline fun <reified T> readMapFromFile(file: File): Either<Error, Map<String, T>> = (either {
            catching({ Toml(file.readText()) }) { e: MalformedInputException ->
                InvalidFormatOfType(file, typeOf<Toml>(), e)
            }
        } thenEither { it.toMap<T>() }).flatten()

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

        /**
         * Converts the specified column in the table to a Toml format handling.
         *
         * @param name The name of the column to be converted to Toml format.
         * @since 5.5.0
         */
        fun Table.toml(name: String) = jsonb<Toml>(name)

        @PublishedApi
        internal fun convertTomlValue(value: Any?): Any? = when (value) {
            null -> null
            is TomlTable -> value.keySet().associateWith { convertTomlValue(value.get(it)) }
            is TomlArray -> (0 until value.size()).map { convertTomlValue(value.get(it)) }
            else -> value
        }
    }

    /**
     * Converts the current object to an instance of the specified type [T].
     * This utility function utilizes a safe conversion approach, attempting to parse the object
     * first through JSON deserialization and then falling back to TOML deserialization if necessary.
     *
     * The function ensures that the returned result is wrapped in an `Either` type, where:
     * - An instance of `Either.Right` contains the successfully converted object of type [T].
     * - Any errors encountered during the deserialization will be handled by the fallback mechanism.
     *
     * @param T The target type to which the object will be converted. It must be specified explicitly or inferred.
     * @return An `Either` containing the object of type [T] on success, or an error representation otherwise.
     * @since 6.1.0
     */
    inline fun <reified T> toObject() = tryOr({ toJson().toObject<T>() }) {
        TOML_MAPPER.readValue<T>(value)!!.let { Either.Right(it) }
    }

    /**
     * Converts the elements of the receiver to an array of the specified type, wrapping the result in an `Either`.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return An `Either` containing a `DeserializationError.MappingError` if the conversion fails,
     *         or an array of type `T` on success.
     * @since 6.1.0
     */
    inline fun <reified T> toArray(): Either<DeserializationError.MappingError, Array<T>> = toList<T>().map { it.toTypedArray() }

    /**
     * Converts the underlying TOML data into a list of the specified type [T].
     *
     * This function attempts to extract a list of values from the provided TOML structure or
     * a single key-value map, converting each item to the specified type [T]. If no array
     * structure is present or the conversion fails, it returns an empty list.
     *
     * The function employs a reified type parameter [T] to ensure the expected type is preserved
     * during runtime and uses error handling to capture and wrap any deserialization errors
     * into a [DeserializationError.MappingError] for downstream processing.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return A list of elements of type [T], or an empty list if no data could be parsed.
     * @since 6.1.0
     */
    inline fun <reified T> toList() = either {
        catching({
            val parsed = TomlJ.parse(value)
            val firstArrayKey = parsed.keySet().firstOrNull { parsed.isArray(it) }
            val raw: Any? = if (firstArrayKey != null) parsed.getArray(firstArrayKey)
            else convertTomlValue(parsed) as? Map<*, *>
            when (raw) {
                is TomlArray -> (convertTomlValue(raw) as List<*>).map { it as T }
                is Map<*, *> -> raw.values.map { it as T }
                else -> emptyList()
            }
        }) { t: Throwable -> DeserializationError.MappingError(typeOf<T>(), t) }
    }
    /**
     * Converts the receiver to a non-empty list wrapped in an Either type.
     * If the conversion is successful, the result will be a `Right` containing the non-empty list.
     * In case the conversion fails or the list is empty, it will return a `Left` containing the error.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @param T The type of elements in the list.
     * @return An `Either` holding a non-empty list of type `T` on success, or an error if conversion fails.
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptyList() =
        toList<T>() as Either<Error, List<T>> thenEither { catching({ it.toNonEmptyList() }) { _: Throwable -> IterableError.Empty } }
    /**
     * Converts a list of type `T` into an `MList` of the same type, handling potential mapping errors.
     * The method uses the `toList` function to retrieve the initial list and maps it into an `MList`.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return Either a `DeserializationError.MappingError` in case of a failure during the mapping process,
     * or an `MList<T>` containing the successfully mapped items.
     * @since 6.1.0
     */
    inline fun <reified T> toMList() = toList<T>().map { it.toMList() }
    /**
     * Converts a collection to a non-empty mutable list wrapped in an `Either` type.
     *
     * This method first attempts to convert the collection to a mutable list of the specified type.
     * If successful, it wraps the mutable list in a right-side `Either`. If the conversion fails
     * or the collection is empty, an error of type `IterableError.Empty` is returned instead.
     *
     * The reified type parameter allows this method to be used generically without requiring explicit type information at the call site.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An `Either` containing either an `Error` or a non-empty mutable list (`MList<T>`).
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptyMList() =
        toMList<T>() as Either<Error, MList<T>> thenEither { catching({ it.toNonEmptyMList() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Transforms the result of deserialization into a set of type [T].
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return Either a [DeserializationError.MappingError] if deserialization fails,
     *         or a [Set] containing the deserialized elements of type [T].
     * @since 6.1.0
     */
    inline fun <reified T> toSet(): Either<DeserializationError.MappingError, Set<T>> = toList<T>().map { it.toSet() }
    /**
     * Converts a collection of elements into a non-empty set wrapped in an `Either`.
     *
     * This method attempts to create a `NonEmptySet` from the collection. If the collection is empty,
     * it will produce an `Either.Left` containing an `IterableError.Empty` error.
     * If the conversion is successful, it will return an `Either.Right` containing the resulting `NonEmptySet`.
     *
     * The method ensures type safety through reified type parameters and handles potential exceptions
     * that might occur during the conversion.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An `Either` representing either the successful conversion to a `NonEmptySet`
     *         (in `Right`) or an error (in `Left`).
     * @throws Throwable if unexpected errors occur during the conversion process.
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptySet() =
        toSet<T>() as Either<Error, Set<T>> thenEither { catching({ it.toNonEmptySet() }) { _: Throwable -> IterableError.Empty } }
    /**
     * Converts a deserialized list of type T into a mutable set (MSet).
     *
     * This method attempts to deserialize and transform the list into a mutable set.
     * If the deserialization fails, it returns a `DeserializationError.MappingError`.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return Either a `DeserializationError.MappingError` on failure or a `MSet<T>` on success.
     * @since 6.1.0
     */
    inline fun <reified T> toMSet(): Either<DeserializationError.MappingError, MSet<T>> = toList<T>().map { it.toMSet() }
    /**
     * Converts the current iterable to a non-empty mutable set wrapped in an `Either`.
     *
     * If the iterable is empty, the result will be a left value containing `IterableError.Empty`.
     * Otherwise, the result will be a right value containing the non-empty mutable set.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An `Either` containing `IterableError.Empty` on the left if the iterable is empty,
     *         or a non-empty `MSet` on the right if the iterable is non-empty.
     *
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptyMSet() =
        toMSet<T>() as Either<Error, MSet<T>> thenEither { catching({ it.toNonEmptyMSet() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Converts a TOML value into a map with keys as `String` and values of type `V`.
     *
     * This function uses the `convertTomlValue` method to transform the input TOML data into a map structure.
     * If the conversion process fails, it wraps the error in a `DeserializationError.MappingError` instance
     * containing the expected type and the caught exception.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return A result that either contains the successfully converted map of type `Map<String, V>` or an error.
     * @since 6.1.0
     */
    inline fun <reified V> toMap() = either {
        catching({ convertTomlValue(TomlJ.parse(value)) as Map<String, V> }) { t: Throwable ->
            DeserializationError.MappingError(typeOf<Map<String, V>>(), t)
        }
    }
    /**
     * Converts the underlying collection to a non-empty map, where the keys are of type `String` and the values are
     * of the specified type `V`. This method ensures the resulting map is non-empty, wrapping it in an `Either` to
     * handle potential errors.
     *
     * If the transformation is successful, the result is a `Right` containing the non-empty map. In case of errors
     * during the transformation process, an `IterableError.Empty` is returned wrapped in `Left`.
     *
     * The function leverages the `toMap` method for conversion and includes error handling to catch any exceptions
     * that may occur, ensuring robust execution.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An `Either` containing an `Error` on the left or a non-empty map of type `<String, V>` on the right.
     * @since 6.1.0
     */
    inline fun <reified V> toNonEmptyMap() =
        toMap<V>() as Either<Error, Map<String, V>> thenEither { catching({ it.toNonEmptyMap() }) { _: Throwable -> IterableError.Empty } }
    /**
     * Converts a map to an `MMap` while performing type-safe deserialization of its values.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return An `Either` instance containing a `MappingError` if the deserialization fails,
     *         or an `MMap` of type `<String, V>` upon successful conversion.
     * @since 6.1.0
     */
    inline fun <reified V> toMMap(): Either<DeserializationError.MappingError, MMap<String, V>> = toMap<V>().map { it.toMMap() }
    /**
     * Converts the current object into a non-empty `MMap` structure wrapped in an `Either`.
     * If the conversion fails or the resulting map is empty, an `IterableError.Empty` error is returned.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @param V The type of values associated with the keys in the resulting `MMap`.
     * @return An `Either` containing the successfully converted non-empty `MMap` or an `IterableError.Empty` in case of failure.
     * @since 6.1.0
     */
    inline fun <reified V> toNonEmptyMMap() =
        toMMap<V>() as Either<Error, MMap<String, V>> thenEither { catching({ it.toNonEmptyMMap() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Converts the current object into a `DataMap` structure, encapsulating the result
     * in an `Either` type for error handling.
     *
     * The method returns a successful `DataMap` upon successful conversion or
     * a `DeserializationError.MappingError` if the mapping process encounters an issue.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return An `Either` type that contains either a `DeserializationError.MappingError`
     *         on failure or a successfully constructed `DataMap`.
     * @since 6.1.0
     */
    fun toDataMap(): Either<DeserializationError.MappingError, DataMap> = toMap<Any?>()
    /**
     * Converts the current instance to a NonEmptyDataMap if possible.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An Either object containing an Error if the conversion fails,
     *         or a NonEmptyDataMap if the conversion succeeds.
     * @since 6.1.0
     */
    fun toNonEmptyDataMap(): Either<Error, NonEmptyDataMap> = toNonEmptyMap<Any?>()
    /**
     * Converts the current instance into a `DataMMap` representation.
     * The conversion is performed using a deserialization operation that may result
     * in either a successfully mapped `DataMMap` or a `MappingError` if the process fails.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return An `Either` containing a `MappingError` on failure or a `DataMMap` on success.
     * @since 6.1.0
     */
    fun toDataMMap(): Either<DeserializationError.MappingError, DataMMap> = toMMap<Any?>()
    /**
     * Converts the current data structure into a `NonEmptyDataMMap` if it contains non-empty data.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return Either an `Error` if the conversion is not possible or a `NonEmptyDataMMap` if the data is valid and non-empty.
     * @since 6.1.0
     */
    fun toNonEmptyDataMMap(): Either<Error, NonEmptyDataMMap> = toNonEmptyMMap<Any?>()

    /**
     * Converts the current data representation into a non-nullable DataMapNN format.
     * The method performs the transformation while ensuring that deserialization errors
     * are appropriately handled and encapsulated in the result type.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return Either a MappingError upon deserialization failure, or a successfully
     *         transformed non-nullable DataMapNN instance.
     * @since 6.1.0
     */
    fun toDataMapNN(): Either<DeserializationError.MappingError, DataMapNN> = toMap<Any>()
    /**
     * Converts the current instance into a non-empty data map encapsulated in an `Either`.
     * The resulting `Either` will contain a `NonEmptyDataMapNN` if the conversion is successful,
     * or an `Error` if the conversion fails.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return an `Either` instance containing either an `Error` or a `NonEmptyDataMapNN`.
     * @since 6.1.0
     */
    fun toNonEmptyDataMapNN(): Either<Error, NonEmptyDataMapNN> = toNonEmptyMap<Any>()
    /**
     * Transforms the current object into a DataMMapNN representation. This method attempts to map
     * the data and return the resulting structure, or an error if the mapping fails.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return An Either containing a DeserializationError.MappingError if the mapping fails,
     *         or a successfully mapped DataMMapNN object.
     * @since 6.1.0
     */
    fun toDataMMapNN(): Either<DeserializationError.MappingError, DataMMapNN> = toMMap<Any>()
    /**
     * Converts the current object to an instance of Either containing a NonEmptyDataMMapNN.
     * This transformation ensures that the resulting map is non-empty and meets the defined constraints.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return Either an Error if the conversion fails or a NonEmptyDataMMapNN if the operation succeeds.
     * @since 6.1.0
     */
    fun toNonEmptyDataMMapNN(): Either<Error, NonEmptyDataMMapNN> = toNonEmptyMMap<Any>()

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
     * Applies a JSON Merge Patch operation using the provided TOML patch.
     * Converts the current object to JSON, applies the patch,
     * and then converts the result back to TOML.
     *
     * @param patch the TOML object to be used as the source of the merge patch
     * @since 6.1.0
     */
    infix fun mergePatch(patch: Toml) = toJson().mergePatch(patch.toJson()).toToml()
    /**
     * Merges the given JSON patch with the current TOML document, applying changes
     * defined by the patch. The operation adheres to the JSON Merge Patch standard (RFC 7396).
     *
     * @param patch the JSON object containing the patch data to be merged into the current TOML
     * representation.
     * @since 6.1.0
     */
    infix fun mergePatch(patch: Json) = toJson().mergePatch(patch).toToml()
    /**
     * Applies a TOML patch to the current TOML object and returns the resulting patched TOML.
     *
     * This method converts the current TOML object to JSON, applies the given patch
     * (also converted to JSON), and then maps the result back to a patched TOML object.
     *
     * Possible erros:
     * - [InvalidFormatOfType] - if the patch is not a JSON array or a path is invalid.
     * - [RequiredProperty] - if a required property is missing in the patch.
     * - [JsonError.PathNotFound] - if the path specified in the patch does not exist in the target JSON.
     * - [IllegalOperation] - if you're trying to move a node into its own children.
     * - [ValidationError.ExpectationMismatch] - if the path specified in the patch does not match the expected value.
     * - [UnsupportedOperation] - if an unsupported operation is encountered in the patch.
     *
     * @param patch the TOML object representing the patch to apply.
     * @since 6.1.0
     */
    infix fun tomlPatch(patch: Toml) = toJson().jsonPatch(patch.toJson()).map { it.toToml() }
    /**
     * Applies a JSON patch to the current object converted to JSON and converts the result back to TOML.
     *
     * This function first converts the current object to a JSON representation,
     * applies the specified JSON patch, and then converts the resulting JSON
     * back to a TOML-compatible format.
     *
     * Possible erros:
     * - [InvalidFormatOfType] - if the patch is not a JSON array or a path is invalid.
     * - [RequiredProperty] - if a required property is missing in the patch.
     * - [JsonError.PathNotFound] - if the path specified in the patch does not exist in the target JSON.
     * - [IllegalOperation] - if you're trying to move a node into its own children.
     * - [ValidationError.ExpectationMismatch] - if the path specified in the patch does not match the expected value.
     * - [UnsupportedOperation] - if an unsupported operation is encountered in the patch.
     *
     * @param patch the JSON patch to apply to the current object
     * @since 6.1.0
     */
    infix fun tomlPatch(patch: Json) = toJson().jsonPatch(patch).map { it.toToml() }
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
    val isMissing: Boolean = rawValue == null
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
            current = if (index != null && current.isArray) current[index]
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

            if (index != null && current.isArray)
                current = current[index]
            else {
                if (current[key].isMissing) _set(key, mMapOf<String, Any?>())
                current = current._get(key)
            }
        }

        val lastIndex = lastKey.toIntOrNull()
        if (lastIndex != null && current.isArray) current[lastIndex] = value
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
     * Converts the current raw value of the TOML node into a list, if iterables.
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