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
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.Instant
import dev.tommasop1804.kutils.OffsetDateTime
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.Json.Companion.MAPPER
import dev.tommasop1804.kutils.classes.coding.Json.Companion.toJson
import dev.tommasop1804.kutils.classes.collections.*
import dev.tommasop1804.kutils.classes.collections.NonEmptyList.Companion.toNonEmptyList
import dev.tommasop1804.kutils.classes.collections.NonEmptyMList.Companion.toNonEmptyMList
import dev.tommasop1804.kutils.classes.collections.NonEmptyMSet.Companion.toNonEmptyMSet
import dev.tommasop1804.kutils.classes.collections.NonEmptySet.Companion.toNonEmptySet
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.classes.maps.*
import dev.tommasop1804.kutils.classes.maps.NonEmptyMMap.Companion.toNonEmptyMMap
import dev.tommasop1804.kutils.classes.maps.NonEmptyMap.Companion.toNonEmptyMap
import dev.tommasop1804.kutils.errors.*
import dev.tommasop1804.kutils.exceptions.*
import org.jetbrains.exposed.v1.core.Table
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
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
import kotlin.reflect.typeOf
import org.intellij.lang.annotations.Language as IJLanguage
import org.yaml.snakeyaml.Yaml as SnakeYaml

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
 * @since 3.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Yaml.Companion.Serializer::class)
@JsonDeserialize(using = Yaml.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Yaml.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Yaml.Companion.OldDeserializer::class)
@Suppress("unused", "UNCHECKED_CAST", "kutils_collection_declaration")
class Yaml(@param:IJLanguage("YAML") override var value: String) : CharSequence, Code(value, Language.Yaml) {

    /**
     * Indicates whether the current YAML instance can be represented as an object-like structure (e.g., a map or dictionary).
     * This variable evaluates to `true` if the deserialization of the YAML content via `toDataMap()` is successful.
     *
     * The underlying implementation relies on the result of a safe deserialization attempt using SnakeYAML.
     *
     * @since 3.0.0
     */
    val isObject = toDataMap().isLeft
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
     * @since 3.0.0
     */
    val isArray = toList<Any>().isLeft && value.trim() startsWith Char.HYPEN
    /**
     * Indicates whether the current YAML node represents a scalar value.
     * This property evaluates to `true` if the node is neither an array nor an object.
     * 
     * @since 3.0.0
     */
    val isValue = !isArray && !isObject

    /**
     * Constructs a new instance of the YAML class using a given [YamlNode].
     *
     * @param node The YAMLNode instance whose raw value will be used to initialize the YAML object.
     * The raw value is converted to a string and passed to the primary constructor of the YAML class.
     *
     * @since 3.0.0
     */
    constructor(node: YamlNode) : this(node.rawValue.toString())

    /**
     * Secondary constructor that initializes an instance using a `Code` object.
     *
     * @param code The `Code` object containing the value to initialize.
     * @since 3.0.0
     */
    constructor(code: Code) : this(code.value) {
        code.language.expect(Language.Yaml)
    }

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
        file.extension.validate(file::extension, "file") { it equalsIgnoreCase "yaml" || it equalsIgnoreCase "yml" }
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
        try {
            SNAKE_YAML.load<Any>(value)
        } catch (e: MarkedYAMLException) {
            throw MalformedInputException("${e.problem.sentenceCase()} at line ${e.problemMark.line + 1}, column ${e.problemMark.column + 1}${if (e.context != null) " ${e.context}" else String.EMPTY}")
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
                        it is YamlNode -> when {
                            it.isArray -> representSequence(Tag.SEQ, it.asList<Any>(), DumperOptions.FlowStyle.BLOCK)
                            it.isMap -> representMapping(Tag.MAP, it.asMap<Any>(), DumperOptions.FlowStyle.BLOCK)
                            it.isObject -> representMapping(Tag.MAP, it.toJson().toMap<Any>()(), DumperOptions.FlowStyle.BLOCK)
                            else -> representScalar(Tag.STR, it.rawValue.toString())
                        }
                        it is CharSequence -> representScalar(Tag.STR, it.toString())
                        it is Iterable<*> -> representSequence(Tag.SEQ, it, DumperOptions.FlowStyle.BLOCK)
                        it.toJson().isObject -> representMapping(Tag.MAP, it.toJson().toMap<Any>()(), DumperOptions.FlowStyle.BLOCK)
                        else -> representScalar(Tag.STR, it.serialize())
                    }
                }
            }
        }
        private val LOADER_OPTIONS = LoaderOptions().apply {
            tagInspector = TagInspector { _ -> true }
        }
        private val CONSTRUCTOR = Constructor(LOADER_OPTIONS)
        val SNAKE_YAML = SnakeYaml(CONSTRUCTOR, REPRESENTER, DUMPER_OPTIONS, LOADER_OPTIONS)

        /**
         * Checks if the String represents a valid YAML structure.
         * 
         * This method attempts to parse the String as YAML using the `YAML` class.
         * If the parsing succeeds, the String is considered valid YAML.
         * Otherwise, it is considered invalid.
         * 
         * @receiver The String to be validated as YAML.
         * @return `true` if the String is valid YAML; `false` otherwise.
         * @since 3.0.0
         */
        fun String.isValidYaml() = runCatching { Yaml(this) }.isSuccess

        /**
         * Converts the current File instance to a Yaml object.
         *
         * @return An Either containing the resulting Yaml object if the conversion is successful,
         *         or an InvalidConversion object in case of failure.
         * @since 6.1.0
         */
        fun File.toYaml(): Either<InvalidTypeConversion, Yaml> = either {
            catching({ Yaml(this@toYaml) }) { t: Throwable ->
                InvalidTypeConversion(this@toYaml, typeOf<File>(), typeOf<Yaml>(), t)
            }
        }
        /**
         * Converts the current [Path] instance to a [Yaml] representation.
         *
         * This method attempts to interpret the [Path] as a YAML type. If the conversion
         * is successful, the resulting [Yaml] object is returned wrapped in an [Either].
         * If the conversion fails, an [InvalidTypeConversion] error is returned containing
         * details about the failed conversion.
         *
         * @return An [Either] containing either the successfully converted [Yaml] object
         * or an [InvalidTypeConversion] error encapsulating the failure details.
         * @since 6.1.0
         */
        fun Path.toYaml(): Either<InvalidTypeConversion, Yaml> = either {
            catching({ Yaml(this@toYaml) }) { t: Throwable ->
                InvalidTypeConversion(this@toYaml, typeOf<Path>(), typeOf<Yaml>(), t)
            }
        }
        /**
         * Converts the annotated YAML string to a `Yaml` object.
         *
         * This extension function attempts to parse the current receiver string (annotated as YAML)
         * into a `Yaml` object. If the parsing fails due to an invalid format or any other error,
         * it wraps the exception in an `InvalidFormat` error result.
         *
         * @receiver A YAML-formatted string that will be parsed.
         * @return An `Either` instance containing the parsed `Yaml` object on success
         *         or an `InvalidFormat` error on failure.
         * @since 6.1.0
         */
        fun @receiver:IJLanguage("yaml") String.toYaml() = either {
            catching({ Yaml(this@toYaml) }) { t: Throwable ->
                InvalidTypeFormat(this@toYaml, typeOf<Yaml>(), t)
            }
        }
        /**
         * Converts the current `JSON` instance into its equivalent `YAML` representation.
         *
         * This function transforms the data held in the `JSON` object into the corresponding
         * `YAML` format, preserving the structure and content of the original data.
         *
         * @return A `YAML` instance representing the data structure of the original `JSON` input.
         * @since 3.0.0
         */
        @JvmName("jsonToYaml")
        fun Json.toYaml(): Yaml {
            val obj = toObject<Any>()()
            return obj.toYaml()
        }
        /**
         * Converts the contents of the Toml object to a YAML string representation.
         *
         * This method transforms the TOML structure into a map representation
         * using the `toDataMap` method, and subsequently serializes it to YAML
         * format by invoking the `toYaml` method on the resulting map.
         *
         * @receiver The Toml object to be converted.
         * @return A YAML string representation of the TOML data.
         * @since 3.11.0
         */
        @JvmName("tomlToYaml")
        fun Toml.toYaml() = toDataMap().toYaml()
        /**
         * Converts the current CSV instance into its YAML representation.
         *
         * @return A YAML instance representing the tabular data of the original CSV input.
         * @since 3.13.0
         */
        @JvmName("csvToYaml")
        fun Csv.toYaml(): Yaml = toJson().toYaml()
        /**
         * Converts the given object to its YAML representation.
         *
         * @param includeTag Specifies whether to include the YAML type tag in the generated output. 
         *                   If `true`, the type tag is included; if `false`, it is omitted. Default is `true`.
         * @return The YAML representation of the object as an instance of the `YAML` class.
         * @since 3.0.0
         */
        @JvmName("anyToYaml")
        fun Any.toYaml(includeTag: Boolean = true): Yaml {
            val value1 = SNAKE_YAML.dump(this)!!
            return Yaml(
                if (includeTag) value1
                else if (value1 startsWith "!!") value1 after Char.LF
                else value1
            )
        }

        /**
         * Reads and parses the content of the specified file into an instance of the specified type.
         *
         * @param file The file to be read and parsed.
         * @return An `Either` containing the parsed object of type `T` if successful, or an `InvalidFormat` error if parsing fails.
         * @since 6.1.0
         */
        inline fun <reified T> readFromFile(file: File): Either<InvalidTypeFormat, T> = either {
            catching({ SNAKE_YAML.load<T>(file.readText()) }) { t: Throwable ->
                InvalidTypeFormat(file, typeOf<T>(), t)
            }
        }
        /**
         * Reads a file and converts its content into an array of the specified type.
         *
         * @param file The file to be read and converted into an array.
         * @return An `Either` containing either an `Error` if the reading or conversion fails,
         *         or an array of type `T` if the operation is successful.
         * @since 6.1.0
         */
        inline fun <reified T> readArrayFromFile(file: File): Either<Error, Array<T>> = readListFromFile<T>(file).map { it.toTypedArray() }
        /**
         * Reads a list of elements of type [T] from the specified file.
         *
         * This method attempts to parse the file content as YAML and converts the data into a list of the specified type [T].
         * If the file content format is invalid, an error is returned.
         *
         * @param file The file to be read and parsed into a list of elements of type [T].
         * @return Either an [Error] if the parsing fails, or a [List] of elements of type [T] if the parsing is successful.
         * @since 6.1.0
         */
        inline fun <reified T> readListFromFile(file: File): Either<Error, List<T>> = (either {
            catching({ Yaml(file.readText()) }) { e: MalformedInputException ->
                InvalidTypeFormat(file, typeOf<Yaml>(), e)
            }
        } thenEither { it.toList<T>() }).flatten()
        /**
         * Reads a set of elements of type [T] from the specified file. The method attempts to deserialize
         * the contents of the file into a list and then converts that list into a set.
         *
         * @param file The file from which the elements should be read.
         * @return An [Either] containing a set of elements of type [T] if successful, or an [Error] if
         *         the operation fails.
         * @since 6.1.0
         */
        inline fun <reified T> readSetFromFile(file: File): Either<Error, Set<T>> = readListFromFile<T>(file).map { it.toSet() }
        /**
         * Reads and parses a YAML file into a map with string keys and values of the specified type.
         *
         * This function uses a YAML parser to deserialize the contents of the given file into a map.
         * If the file cannot be read or the contents cannot be properly deserialized into the expected
         * type, an error will be returned.
         *
         * @param T The type of the values in the map.
         * @param file The YAML file to read and parse.
         * @return Either a successfully parsed map or an error encapsulating the failure.
         * @since 6.1.0
         */
        inline fun <reified T> readMapFromFile(file: File) = either {
            @Suppress("USELESS_CAST")
            catching({
                val map = SNAKE_YAML.load<Map<String, T>>(file.readText())
                map.mapValues { it.value as T }
            }) { t: Throwable ->
                InvalidTypeFormat(file, typeOf<Map<String, T>>(), t)
            }
        }

        class Serializer : ValueSerializer<Yaml>() {
            override fun serialize(value: Yaml, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
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
                    else -> gen.writeRaw(value.toJson().value)
                }
            }
        }

        class Deserializer : ValueDeserializer<Yaml>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = Json(p.objectReadContext().readTree<JsonNode>(p).toString()).toYaml()
        }

        class OldSerializer : JsonSerializer<Yaml>() {
            override fun serialize(value: Yaml, gen: JsonGenerator, serializers: SerializerProvider) {
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
                    else -> gen.writeRaw(value.toJson().value)
                }
            }
        }

        class OldDeserializer : JsonDeserializer<Yaml>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Yaml = Json(p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p).toString()).toYaml()
        }

        /**
         * Converts the specified column in the current table to a JSONB field of type Yaml.
         *
         * @param name The name of the column to be converted into a JSONB field.
         * @since 5.5.0
         */
        fun Table.yaml(name: String) = jsonb<Yaml>(name)
    }

    /**
     * Converts the current object to an instance of the specified type.
     *
     * This method attempts to deserialize the current object into an instance of the type `T`.
     * If the deserialization fails, a `DeserializationError.MappingError` is returned.
     *
     * @return An `Either` containing the deserialization result. The left value indicates a `MappingError`,
     *         while the right value contains the successfully deserialized object of type `T`.
     * @since 6.1.0
     */
    inline fun <reified T> toObject(): Either<DeserializationError.MappingError, T> = tryOr({ toJson().toObject<T>() }) {
        SNAKE_YAML.loadAs(value, T::class.java)!!
    }

    /**
     * Converts a deserialized list into an array of the specified type.
     * Uses reified type parameters to create the array at runtime.
     *
     * @return Either a mapping error if deserialization fails, or an array of the specified type.
     * @since 6.1.0
     */
    inline fun <reified T> toArray(): Either<DeserializationError.MappingError, Array<T>> =
        toList<T>().map { it.toTypedArray() }

    /**
     * Converts the serialized YAML content into a strongly-typed list of the specified type.
     *
     * This method attempts to deserialize the YAML input into a `List` of type `T`.
     * If the conversion fails due to a mapping error, the result will contain a `DeserializationError.MappingError`.
     *
     * @return Either a `DeserializationError.MappingError` if the conversion fails, or a `List<T>` with the deserialized objects.
     * @since 6.1.0
     */
    inline fun <reified T> toList(): Either<DeserializationError.MappingError, List<T>> = either {
        catching({
            val raw = SNAKE_YAML.load<List<*>>(value)
            raw.map { it as T }
        }) { t: Throwable -> DeserializationError.MappingError(typeOf<List<T>>(), t) }
    }
    /**
     * Transforms a collection into a `NonEmptyList` wrapped in an `Either` type.
     *
     * This method attempts to convert the current collection to a `NonEmptyList`.
     * If the collection is empty, it returns a failure represented as an `Error`.
     * The success case contains the resulting `NonEmptyList` wrapped in an `Either.Right`.
     *
     * The operation ensures type-safety by leveraging reified type parameters
     * and catching potential exceptions during the conversion process.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @param T The type of elements within the collection.
     * @return An `Either<Error, List<T>>`, where a successful result contains
     *         the `NonEmptyList` and a failure contains an `IterableError.Empty`.
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptyList() =
        toList<T>() as Either<Error, List<T>> thenEither { catching({ it.toNonEmptyList() }) { _: Throwable -> IterableError.Empty } }
    /**
     * Converts a serialized input into an `MList` of type `T`, wrapped in an `Either` to handle possible errors.
     *
     * The method attempts to deserialize the input into a list of type `T` and then maps it into an `MList<T>`.
     * If the deserialization fails, a `DeserializationError.MappingError` is returned inside the `Either`.
     *
     * @return An `Either` containing a deserialization error of type `DeserializationError.MappingError`
     *         or a successfully mapped `MList<T>`.
     * @since 6.1.0
     */
    inline fun <reified T> toMList(): Either<DeserializationError.MappingError, MList<T>> =
        toList<T>().map { it.toMList() }
    /**
     * Converts the current context into a `NonEmptyMList<T>` if possible, wrapped in an `Either` type.
     * If conversion is successful, the resulting `NonEmptyMList<T>` is returned as `Right`.
     * If the conversion fails, an error of type `Error` is returned as `Left`.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An `Either` containing either a `NonEmptyMList<T>` on success (`Right`)
     *         or an error (`Left`) if the conversion could not be performed.
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptyMList(): Either<Error, NonEmptyMList<T>> =
        toMList<T>() as Either<Error, MList<T>> thenEither { catching({ it.toNonEmptyMList() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Transforms a deserialized list of elements into a set.
     *
     * This method attempts to deserialize a list of elements of type [T]
     * and converts it into a set, ensuring all elements are unique.
     *
     * @return Either a mapping error in case of deserialization failure
     *         or a set containing unique elements of type [T].
     * @since 6.1.0
     */
    inline fun <reified T> toSet(): Either<DeserializationError.MappingError, Set<T>> =
        toList<T>().map { it.toSet() }
    /**
     * Converts a collection to a `NonEmptySet` wrapped in an `Either`.
     * If the collection is empty, an error is returned instead.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An `Either` containing a `NonEmptySet` if the conversion is successful, or an `Error` if the collection is empty.
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptySet(): Either<Error, NonEmptySet<T>> =
        toSet<T>() as Either<Error, Set<T>> thenEither { catching({ it.toNonEmptySet() }) { _: Throwable -> IterableError.Empty } }
    /**
     * Converts the serialized data into a mutable set of type [T].
     *
     * This function attempts to deserialize the data into a list of elements of type [T],
     * then transforms the list into a mutable set (MSet).
     *
     * @return Either a [DeserializationError.MappingError] if deserialization fails, or a [MSet] of type [T]
     *         if the conversion is successful.
     * @since 6.1.0
     */
    inline fun <reified T> toMSet(): Either<DeserializationError.MappingError, MSet<T>> =
        toList<T>().map { it.toMSet() }
    /**
     * Converts a given collection to a `NonEmptyMSet`, ensuring the resulting set is non-empty.
     *
     * This method first transforms the current collection into an `MSet<T>`. If the `MSet<T>` is empty,
     * the conversion will fail, returning an error of type `IterableError.Empty`. Otherwise, it will
     * cast the result into a `NonEmptyMSet` wrapped in an `Either<Error, MSet<T>>`, ensuring type safety
     * and immutability.
     *
     * The method uses `Either` to encapsulate the result and provides error-handling capabilities if
     * the conversion fails. Additionally, type reification is used to retain type information of
     * the elements at runtime.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An `Either` containing `Error` if the input is empty, or a successfully created `NonEmptyMSet<T>`.
     * @throws Throwable If an unexpected exception occurs during the conversion.
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptyMSet() =
        toMSet<T>() as Either<Error, MSet<T>> thenEither { catching({ it.toNonEmptyMSet() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Deserializes the given YAML string into a map where each key is a string and the values are of the specified type.
     *
     * @return Either a successful map of string keys to values of type `V`, or an error of type `DeserializationError.MappingError`
     *         if the deserialization process fails.
     * @since 6.1.0
     */
    inline fun <reified V> toMap(): Either<DeserializationError.MappingError, Map<String, V>> = either {
        catching({
            val raw = SNAKE_YAML.load<Map<String, *>>(value)
            raw.mapValues { it.value as V }
        }) { t: Throwable -> DeserializationError.MappingError(typeOf<Map<String, V>>(), t) }
    }
    /**
     * Converts the receiver into a `NonEmptyMap` if possible.
     *
     * This method attempts to transform the current object into a `NonEmptyMap` of type `String` as the key
     * and a reified type `V` as the value. The result is wrapped in an `Either` type, where
     * the left side represents an error (`Error`) and the right side contains the successfully created `NonEmptyMap`.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An `Either` containing either the error (`Error`) if the transformation is not possible,
     *         or a `NonEmptyMap<String, V>` if the conversion succeeds.
     * @since 6.1.0
     */
    inline fun <reified V> toNonEmptyMap(): Either<Error, NonEmptyMap<String, V>> =
        toMap<V>() as Either<Error, Map<String, V>> thenEither { catching({ it.toNonEmptyMap() }) { _: Throwable -> IterableError.Empty } }
    /**
     * Transforms the current structure into an `MMap<String, V>` while handling possible deserialization errors.
     *
     * @return An `Either` representing either a `MappingError` if the transformation fails, or a resulting `MMap<String, V>` on success.
     * @since 6.1.0
     */
    inline fun <reified V> toMMap(): Either<DeserializationError.MappingError, MMap<String, V>> = toMap<V>().map { it.toMMap() }
    /**
     * Converts the current object to a NonEmptyMMap instance, ensuring that the resulting structure is non-empty.
     *
     * The method attempts to safely transform the underlying data into a NonEmptyMMap.
     * If the transformation fails or the structure is empty, it returns an error wrapped in an Either instance.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An Either containing a NonEmptyMMap<String, V> if successful, or an Error instance if the transformation fails or the map is empty.
     * @since 6.1.0
     */
    inline fun <reified V> toNonEmptyMMap(): Either<Error, NonEmptyMMap<String, V>> =
        toMMap<V>() as Either<Error, MMap<String, V>> thenEither { catching({ it.toNonEmptyMMap() }) { _: Throwable -> IterableError.Empty } }
    /**
     * Converts the current object to a map representation where keys are strings and values
     * can be nullable. Encodes potential errors encountered during the deserialization
     * process as a `MappingError`.
     *
     * @return An `Either` that contains a `MappingError` in case of failure or a map
     * representation of the object on success.
     * @since 6.1.0
     */
    fun toDataMap(): Either<DeserializationError.MappingError, Map<String, Any?>> = toMap<Any?>()
    /**
     * Converts the current structure into a `NonEmptyMap<String, Any?>` if possible.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An `Either` containing an `Error` if the conversion is not possible,
     *         or a `NonEmptyMap<String, Any?>` if the conversion succeeds.
     * @since 6.1.0
     */
    fun toNonEmptyDataMap(): Either<Error, NonEmptyMap<String, Any?>> = toNonEmptyMap<Any?>()
    /**
     * Converts the current object to an `MMap<String, Any?>` representation, encapsulated in an `Either`.
     * The conversion may result in a `MappingError` if deserialization fails.
     *
     * @return Either a `MappingError` encapsulating details of the deserialization failure,
     *         or a successfully converted `MMap` containing string keys and nullable values.
     * @since 6.1.0
     */
    fun toDataMMap(): Either<DeserializationError.MappingError, MMap<String, Any?>> = toMMap<Any?>()
    /**
     * Converts the current data into a NonEmptyMMap instance, ensuring that the resulting map has at least one entry.
     * If the conversion fails or the resulting map is empty, an Error is returned.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return Either an Error object if the conversion fails or an instance of NonEmptyMMap containing the data.
     * @since 6.1.0
     */
    fun toNonEmptyDataMMap(): Either<Error, NonEmptyMMap<String, Any?>> = toNonEmptyMMap<Any?>()
    /**
     * Converts the current object into a non-nullable data map representation.
     *
     * This method attempts to deserialize the current object into a map structure,
     * where the keys are strings and the values are of type `Any`. If the deserialization
     * process fails due to a mapping error, an instance of `DeserializationError.MappingError`
     * will be returned as the left value of the `Either` type.
     *
     * @return An `Either` containing a `DeserializationError.MappingError` on failure or
     *         a `Map<String, Any>` representing the deserialized object on success.
     * @since 6.1.0
     */
    fun toDataMapNN(): Either<DeserializationError.MappingError, Map<String, Any>> = toMap<Any>()
    /**
     * Converts the current object into an `Either` that contains a `NonEmptyMap` of key-value pairs.
     * The method ensures that the resulting map is not empty and associates `String` keys with `Any`-typed values.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return An `Either` containing an `Error` if the operation fails or a `NonEmptyMap` with one or more elements if successful.
     * @since 6.1.0
     */
    fun toNonEmptyDataMapNN(): Either<Error, NonEmptyMap<String, Any>> = toNonEmptyMap<Any>()
    /**
     * Converts the current instance into a memory-mapped representation of type `MMap<String, Any>`
     * while ensuring non-nullable keys and values.
     *
     * @return An `Either` containing a `MappingError` if deserialization fails, or a successfully
     *         deserialized `MMap<String, Any>`.
     * @since 6.1.0
     */
    fun toDataMMapNN(): Either<DeserializationError.MappingError, MMap<String, Any>> = toMMap<Any>()
    /**
     * Converts the current object to a `NonEmptyMMap` containing `String` keys and `Any` values,
     * ensuring that the resulting map is non-empty. If the conversion fails, an `Error` is returned
     * wrapped in an `Either`.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if the list is empty
     *
     * @return Either an `Error` if the conversion fails, or a non-empty `NonEmptyMMap` with `String`
     * keys and `Any` values if the conversion succeeds.
     * @since 6.1.0
     */
    fun toNonEmptyDataMMapNN(): Either<Error, NonEmptyMMap<String, Any>> = toNonEmptyMMap<Any>()

    /**
     * Retrieves the element at the specified index from the value.
     *
     * @param index The position of the element to retrieve. Must be within the bounds of the value.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     * @since 3.0.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence, starting at the specified 
     * [startIndex] (inclusive) and ending at the specified [endIndex] (exclusive).
     *
     * @param startIndex the start index (inclusive) of the subsequence.
     * @param endIndex the end index (exclusive) of the subsequence.
     * @since 3.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns a string representation of the object.
     *
     * This method is typically overridden to provide a meaningful description 
     * of the instance content or state.
     *
     * @return A string representation of the object.
     * @since 3.0.0
     */
    override fun toString() = value

    /**
     * Writes the content of a specified text value to the provided file.
     *
     * @param file The file to which the text content will be written.
     * @since 3.0.0
     */
    fun writeToFile(file: File) = file.writeText(value)

    /**
     * Retrieves a YAML node based on the provided dot-separated path.
     *
     * @param dotPath The dot-separated string representing the path to the desired YAML node.
     * @return The YAMLNode corresponding to the specified path.
     * @since 3.8.1
     */
    operator fun get(dotPath: String) = Yaml(getAsNode(dotPath))

    /**
     * Retrieves a YAML node based on the provided dot-separated path.
     *
     * @param dotPath The dot-separated string representing the path to the desired YAML node.
     * @return The YAMLNode corresponding to the specified path.
     * @since 3.8.1
     */
    fun getAsNode(dotPath: String) =
        if (Char.DOT !in dotPath) YamlNode(toDataMap()()[dotPath])
        else YamlNode(toDataMap()()[dotPath before Char.DOT])[dotPath after Char.DOT]

    /**
     * Sets the value at the specified dot-delimited path within the YAML structure.
     *
     * @param dotPath The dot-delimited path to identify the location in the YAML structure where the value should be set.
     * @param value The value to be set at the specified path. It can be null.
     * @since 3.0.0
     */
    operator fun set(dotPath: String, value: Any?) {
        val rootNode = YamlNode(toDataMap()())
        rootNode[dotPath] = value
        this.value = rootNode.rawValue!!.toYaml().value
    }

    /**
     * Removes comments from a given string based on the pattern defined by the regex.
     * The method specifically targets content starting with a `#` symbol and removes it,
     * along with everything that follows on the same line.
     *
     * This function modifies the string by eliminating segments that match the regex pattern.
     *
     * @since 3.0.0
     */
    fun removeComments() {
        value -= Regex("#.*")
    }

    /**
     * Merges the current YAML content with the provided patch following the merge-patch algorithm.
     * The patch is applied to the JSON representation of the YAML content and then converted back to YAML.
     *
     * @param patch the YAML content to be merged as a patch.
     * @return a new YAML object resulting from the merge-patch operation.
     * @since 6.1.0
     */
    infix fun mergePatch(patch: Yaml): Yaml = toJson().mergePatch(patch.toJson()).toYaml()
    /**
     * Applies a JSON Merge Patch operation on the current YAML instance with the provided patch.
     *
     * This method converts the current YAML instance to JSON, applies the specified patch using JSON
     * Merge Patch semantics, and then converts the result back to YAML.
     *
     * @param patch the JSON patch to be applied to the current object
     * @return the resulting YAML instance after applying the merge patch
     * @since 6.1.0
     */
    infix fun mergePatch(patch: Json): Yaml = toJson().mergePatch(patch).toYaml()
    /**
     * Applies a YAML patch to the current object and converts the result to TOML format.
     *
     * Possible erros:
     * - [InvalidTypeFormat] - if the patch is not a JSON array or a path is invalid.
     * - [RequiredProperty] - if a required property is missing in the patch.
     * - [YamlError.PathNotFound] - if the path specified in the patch does not exist in the target JSON.
     * - [IllegalOperation] - if you're trying to move a node into its own children.
     * - [ValidationError.ExpectationMismatch] - if the path specified in the patch does not match the expected value.
     * - [UnsupportedOperation] - if an unsupported operation is encountered in the patch.
     *
     * @param patch The YAML content representing the patch to be applied.
     * @return Either an `Error` if the operation fails, or a `Yaml` object if successful.
     * @since 6.1.0
     */
    infix fun yamlPatch(patch: Yaml): Either<Error, Yaml> = toJson().jsonPatch(patch.toJson()).map { it.toYaml() }.mapLeft { e ->
        e.letIf(e is JsonError.PathNotFound) { YamlError.PathNotFound(e.path) }
    }
    /**
     * Applies a JSON patch to the current YAML structure, transforming it into a YAML structure.
     *
     * Possible erros:
     * - [InvalidTypeFormat] - if the patch is not a JSON array or a path is invalid.
     * - [RequiredProperty] - if a required property is missing in the patch.
     * - [YamlError.PathNotFound] - if the path specified in the patch does not exist in the target JSON.
     * - [IllegalOperation] - if you're trying to move a node into its own children.
     * - [ValidationError.ExpectationMismatch] - if the path specified in the patch does not match the expected value.
     * - [UnsupportedOperation] - if an unsupported operation is encountered in the patch.
     *
     * @param patch the JSON patch to apply to the current YAML structure.
     * @return either an error if the operation fails, or a YAML structure resulting from the patch operation.
     * @since 6.1.0
     */
    infix fun yamlPatch(patch: Json): Either<Error, Yaml> = toJson().jsonPatch(patch.toJson()).map { it.toYaml() }.mapLeft { e ->
        e.letIf(e is JsonError.PathNotFound) { YamlError.PathNotFound(e.path) }
    }

    /**
     * Validates the current object against a provided JSON schema using a JSON serialization of the object.
     *
     * Possible errors:
     * - [InvalidTypeFormat] - if the input JSON schema is malformed.
     * - [YamlError.SchemaValidationFailed] - if the validation fails.
     *
     * @param jsonSchema The JSON schema to validate the object against.
     * @return A result indicating whether validation was successful or a failure containing schema validation errors.
     * @since 6.1.0
     */
    infix fun validateWithSchema(jsonSchema: JsonSchema) = toJson().validateWithSchema(jsonSchema).mapLeft { e ->
        YamlError.SchemaValidationFailed(e.errors)
    }
    /**
     * Validates the current object against the provided JSON schema.
     *
     * Possible errors:
     * - [InvalidTypeFormat] - if the input JSON schema is malformed.
     * - [YamlError.SchemaValidationFailed] - if the validation fails.
     *
     * @param jsonSchema The JSON schema to validate against.
     * @param version The version of the JSON schema to be used during validation.
     * @return A result mapping any validation errors, if present.
     * @since 6.1.0
     */
    fun validateWithSchema(jsonSchema: JsonSchema, version: JsonSchema.Version) = toJson().validateWithSchema(jsonSchema).mapLeft { e ->
        YamlError.SchemaValidationFailed(e.errors)
    }
}

/**
 * Represents a YAML Node, encapsulating a raw value that can be of various types and providing utilities for
 * interacting with data in a structured manner.
 *
 * @constructor Creates a new YAMLNode instance from a given raw value.
 * @param rawValue The raw value to be encapsulated by the node. May hold any type or be null.
 * @since 3.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "kutils_collection_declaration", "UNCHECKED_CAST")
class YamlNode(val rawValue: Any?) {
    /**
     * Indicates whether the current YAMLNode is missing a value.
     *
     * This property evaluates to `true` if the underlying `rawValue` is `null`, 
     * otherwise it evaluates to `false`. Can be utilized to quickly determine 
     * if the node lacks any assigned value.
     *
     * @since 3.0.0
     */
    val isMissing: Boolean = rawValue == null
    /**
     * Indicates whether the current YAML node represents an array-like structure.
     * This is determined by checking if the underlying raw value is an instance of `Iterable`.
     *
     * @since 3.0.0
     */
    val isArray: Boolean = rawValue is Iterable<*>
    /**
     * Indicates whether the underlying value of this YAMLNode instance is a Map.
     * This is determined by checking if the `rawValue` field is of type `Map<*, *>`.
     *
     * @return `true` if the `rawValue` is a Map, otherwise `false`.
     *
     * @since 3.0.0
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
     * @since 3.0.0
     */
    val isObject: Boolean = rawValue?.toJson()?.isObject == true
    /**
     * Indicates whether the underlying value of this `YAMLNode` instance is a `String`.
     *
     * The value is determined based on the runtime type of the internal `rawValue`.
     *
     * @return `true` if the `rawValue` is a `String`, otherwise `false`.
     *
     * @since 3.0.0
     */
    val isString: Boolean = rawValue is String
    /**
     * Indicates whether the underlying value of the YAML node is of type `Number`.
     * This property evaluates to `true` if the `rawValue` is an instance of a numeric type, 
     * otherwise it evaluates to `false`.
     *
     * @since 3.0.0
     */
    val isNumber: Boolean = rawValue is Number
    /**
     * Indicates whether the raw value of this YAML node represents an integer number.
     * The check ensures that the raw value is of type `Number` and it does not have a decimal component.
     *
     * @since 3.0.0
     */
    val isIntegerNumber: Boolean = rawValue is Number && rawValue.isNotDecimal
    /**
     * Determines whether the underlying `rawValue` represents a decimal number.
     *
     * The property evaluates to `true` if `rawValue` is of type `Number` and is explicitly a decimal
     * type, distinguishing it from integers. Otherwise, it evaluates to `false`.
     *
     * @since 3.0.0
     */
    val isDecimalNumber: Boolean = rawValue is Number && rawValue.isDecimal
    /**
     * Indicates whether the underlying value of the node is a Boolean type.
     *
     * This property evaluates `true` if the `rawValue` of the YAML node is a `Boolean`.
     * It can be used to determine the type of the value stored in the node for type-safe operations.
     *
     * @see YamlNode.asBoolean
     * @see YamlNode.rawValue
     * @since 3.0.0
     */
    val isBoolean: Boolean = rawValue is Boolean
    /**
     * Indicates whether the current YAML node represents a value that can be interpreted as a `LocalDate`.
     * 
     * The value is determined based on the type of the `rawValue` field.
     *
     * @since 3.0.0
     */
    val isDate: Boolean = rawValue is LocalDate
    /**
     * Indicates whether the raw value of this node represents a valid date-time or instant object.
     * The raw value is considered a date-time if it is an instance of [OffsetDateTime] or [Instant] or [LocalDateTime].
     *
     * @since 3.0.0
     */
    val isDateTime: Boolean = rawValue is OffsetDateTime || rawValue is LocalDateTime || rawValue is Instant
    /**
     * Indicates whether the current node represents a singular value in the YAML structure.
     * A node is considered a value if it is not an array, map, or missing.
     *
     * @since 3.0.0
     */
    val isValue: Boolean = !isArray && !isMap && !isMissing

    companion object {
        private fun missingNode() = YamlNode(null)
        private fun unwrap(value: Any?) = if (value is YamlNode) value.rawValue else value

        /**
         * Converts the current YAML instance into a `YAMLNode`.
         *
         * Depending on the structure of the YAML data, it creates a `YAMLNode` 
         * representation. If the data is an array, it is converted into a list. 
         * Otherwise, a map representation is created.
         *
         * @param includeTag Specifies whether the tag information should be included during the conversion. 
         *                   Defaults to true.
         * @since 3.0.0
         */
        fun Yaml.toYamlNode(includeTag: Boolean = true) = YamlNode(if (isArray) toList<Any>() else toDataMap()())
        /**
         * Converts the current object into a YAMLNode representation.
         *
         * @param includeTag Indicates whether to include the YAML tag in the resulting node. 
         *                   If true, the tag will be included; otherwise, it will be omitted.
         *                   Defaults to true.
         * @since 3.0.0
         */
        @JvmName("anyToYAMLNode")
        fun Any?.toYamlNode(includeTag: Boolean = true) = YamlNode(this)
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
     * @since 3.0.0
     */
    operator fun get(dotPath: String): YamlNode {
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
     * Retrieves the YAMLNode at the specified index within a list-like YAML structure.
     * If the index is out of bounds or the current node is not a list, a missing node is returned.
     *
     * @param index The position of the desired node in the list structure. 
     *              Must be a non-negative integer within the bounds of the list.
     * @return The YAMLNode at the specified index if the current node is a list 
     *         and the index is valid; otherwise, a missing node.
     * @since 3.0.0
     */
    operator fun get(index: Int): YamlNode {
        if (rawValue is List<*>)
            if (index >= 0 && index < rawValue.size) {
                return YamlNode(rawValue[index])
            }
        return missingNode()
    }
    @Suppress("FunctionName")
    private fun _get(key: String) = if (rawValue is Map<*, *>) YamlNode(rawValue[key]) else missingNode()

    /**
     * Sets a value in a nested data structure based on a dotted path representation.
     *
     * @param dotPath A string representing the path to the target property, where nested properties are separated by dots.
     * @param value The value to set at the specified path. Can be of any type.
     * @since 3.0.0
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
     * @param index The position in the list where the value should be set.
     * @param value The new value to be assigned at the specified index.
     * @throws IndexOutOfBoundsException If the specified index is out of the list's bounds.
     * @throws UnsupportedOperationException If the underlying raw value is not a mutable list.
     * @since 3.0.0
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
     * @since 3.0.0
     */
    fun asString() = rawValue?.toString()
    /**
     * Converts the value of the current YAMLNode to an integer.
     *
     * This method attempts to parse the node's value as a string, 
     * convert it to a number if possible.
     *
     * @return The integer representation of the node's value
     * @since 3.0.0
     */
    fun asInt() = asString()?.toDoubleOrNull()?.toInt()
    /**
     * Converts the current YAMLNode to a Long value.
     * 
     * If the node represents a numeric string (e.g., "10.0"), it will be parsed as a Double 
     * and converted to Long.
     * 
     * @return the Long representation of the YAMLNode's value
     * @since 3.0.0
     */
    fun asLong() = asString()?.toDoubleOrNull()?.toLong()
    /**
     * Converts the underlying value of the YAMLNode to a Double.
     * 
     * If the value is not directly convertible to a Double, it will return 0.0.
     * 
     * @return the value of the node as a Double, or 0.0 if conversion fails.
     * @since 3.0.0
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
     * @since 3.0.0
     */
    fun asBoolean() = asString().toBoolean()
    /**
     * Converts the current raw value of the YAML node into a list of YAMLNode objects,
     * if the raw value is iterables.
     * If the raw value is null or not iterables, returns an empty list.
     *
     * @return a list containing YAMLNode objects constructed from elements of the underlying iterables raw value,
     *         or an empty list if the raw value is not iterables.
     * @since 3.0.0
     */
    fun <T> asList(): List<T>? = (rawValue as? Iterable<*>)?.map { it as T }
    /**
     * Converts the current object to a [NonEmptyList] if possible.
     *
     * @return A [NonEmptyList] containing elements of type `T` if the object can be cast to an `Iterable` and is non-empty,
     * or `null` if the conversion is not possible or the iterables is empty.
     * @since 5.2.1
     */
    fun <T> asNonEmptyList(): NonEmptyList<T>? = (rawValue as? Iterable<*>)?.map { it as T }?.toNonEmptyList()
    /**
     * Converts the `rawValue` to an `MList` of type `T` if `rawValue` is an `Iterable`.
     *
     * @return An `MList` containing elements of type `T` if the conversion is successful, or `null` if `rawValue` is not an `Iterable`.
     * @since 5.2.1
     */
    fun <T> asMList(): MList<T>? = (rawValue as? Iterable<*>)?.map { it as T }?.toMList()
    /**
     * Converts the raw value to a `NonEmptyMList` if it can be interpreted as a non-empty list,
     * otherwise returns null.
     *
     * @return A `NonEmptyMList<T>` if the conversion is successful and the resulting list is non-empty,
     * or null if the conversion cannot occur or the list is empty.
     * @since 5.2.1
     */
    fun <T> asNonEmptyMList(): NonEmptyMList<T>? = (rawValue as? Iterable<*>)?.map { it as T }?.toNonEmptyMList()
    /**
     * Converts the rawValue, if it is an Iterable, into a Set of the specified type.
     *
     * The method attempts to cast the elements of the Iterable to the specified type
     * and returns a Set containing these elements. If the rawValue is not an Iterable
     * or if the casting fails, it returns null.
     *
     * @return A Set of type T containing the elements of the rawValue if it is an Iterable,
     *         or null if the conversion is not possible.
     * @since 5.2.1
     */
    fun <T> asSet(): Set<T>? = (rawValue as? Iterable<*>)?.map { it as T }?.toSet()
    /**
     * Converts the underlying raw value to a NonEmptySet if possible.
     *
     * @return A NonEmptySet containing the elements of the rawValue interpreted as an Iterable,
     *         or null if the conversion is not possible or the resulting set is empty.
     * @since 5.2.1
     */
    fun <T> asNonEmptySet(): NonEmptySet<T>? = (rawValue as? Iterable<*>)?.map { it as T }?.toNonEmptySet()
    /**
     * Converts the raw value to an instance of MSet with elements of type T,
     * if the raw value is an Iterable. If the conversion is not possible, returns null.
     *
     * @return an MSet containing the elements of the raw value cast to type T,
     * or null if the conversion is not possible.
     * @since 5.2.1
     */
    fun <T> asMSet(): MSet<T>? = (rawValue as? Iterable<*>)?.map { it as T }?.toMSet()
    /**
     * Converts the current object to a NonEmptyMSet<T> if possible.
     *
     * The method attempts to cast the rawValue to an Iterable, maps its elements to type T, and then converts
     * the resulting collection to a NonEmptyMSet<T>. If the conversion fails or the resulting collection is empty,
     * it returns null.
     *
     * @return A NonEmptyMSet<T> if the conversion is successful and the collection is non-empty, or null otherwise.
     * @since 5.2.1
     */
    fun <T> asNonEmptyMSet(): NonEmptyMSet<T>? = (rawValue as? Iterable<*>)?.map { it as T }?.toNonEmptyMSet()
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
     * @since 3.0.0
     */
    fun <T> asMap(): Map<String, T>? = (rawValue as? Map<*, *>)?.mapKeys { it.key.toString() }?.mapValues { it.value as T }
    /**
     * Converts the raw value to a `NonEmptyMap` with string keys and values of type `T`, if possible.
     *
     * If the raw value can be cast to a `Map`, this method transforms its keys to strings and attempts to create a `NonEmptyMap`.
     * If the resulting map is empty or the raw value cannot be cast to a `Map`, the method returns null.
     *
     * @return A `NonEmptyMap` with string keys and values of type `T`, or null if the conversion is not possible or the map is empty.
     * @since 5.2.1
     */
    fun <T> asNonEmptyMap(): NonEmptyMap<String, T>? = (rawValue as? Map<*, *>)?.mapKeys { it.key.toString() }?.mapValues { it.value as T }?.toNonEmptyMap()
    /**
     * Converts the underlying raw value to an MMap instance with String keys.
     *
     * This method attempts to cast the raw value to a Map and transforms its keys to strings, while maintaining the values as the specified generic type T.
     * If the conversion is not possible, it returns null.
     *
     * @return An MMap instance with String keys and values of type T, or null if the conversion is not applicable.
     * @since 5.2.1
     */
    fun <T> asMMap(): MMap<String, T>? = (rawValue as? Map<*, *>)?.mapKeys { it.key.toString() }?.mapValues { it.value as T }?.toMMap()
    /**
     * Converts the rawValue into a NonEmptyMMap<String, T> if possible.
     * The conversion involves typecasting the keys to strings and values to type T.
     * If the resulting map is empty or the conversion fails, returns null.
     *
     * @return A NonEmptyMMap<String, T> if the conversion succeeds and the map is not empty, otherwise null.
     * @since 5.2.1
     */
    fun <T> asNonEmptyMMap(): NonEmptyMMap<String, T>? = (rawValue as? Map<*, *>)?.mapKeys { it.key.toString() }?.mapValues { it.value as T }?.toNonEmptyMMap()
    /**
     * Converts the current object to a DataMap representation.
     *
     * @return a DataMap instance if the object can be represented as a map, or null if not.
     * @since 5.2.1
     */
    fun asDataMap(): DataMap? = asMap()
    /**
     * Converts the current object to a NonEmptyDataMap if possible.
     *
     * @return A NonEmptyDataMap instance if the conversion is successful, or null if the current object does not represent a non-empty map.
     * @since 5.2.1
     */
    fun asNonEmptyDataMap(): NonEmptyDataMap? = asNonEmptyMap()
    /**
     * Converts the current object to a non-data [DataMMap] instance if applicable.
     *
     * @return A [DataMMap] instance if the object can be represented as a non-data map,
     *         or null if the conversion is not possible.
     * @since 5.2.1
     */
    fun asNonDataMMap(): DataMMap? = asNonEmptyMMap()
    /**
     * Converts the current instance to a NonEmptyDataMMap if possible.
     *
     * @return A NonEmptyDataMMap instance if the conversion is successful, or null if the current instance cannot be represented as a NonEmptyDataMMap.
     * @since 5.2.1
     */
    fun asNonEmptyDataMMap(): NonEmptyDataMMap? = asNonEmptyMMap()
    /**
     * Converts the current object to a non-nullable DataMapNN representation, if possible.
     *
     * @return a DataMapNN instance if the conversion is successful, or null if the object cannot be converted.
     * @since 5.2.1
     */
    fun asDataMapNN(): DataMapNN? = asMap()
    /**
     * Converts the current object to a NonEmptyDataMapNN if possible.
     *
     * @return A NonEmptyDataMapNN instance if the conversion is successful, or null if the object cannot be converted.
     * @since 5.2.1
     */
    fun asNonEmptyDataMapNN(): NonEmptyDataMapNN? = asNonEmptyMap()
    /**
     * Converts the current instance to a non-data mutable map representation, ensuring null safety.
     *
     * @return A non-data mutable map instance if the current instance can be represented as such, or null otherwise.
     * @since 5.2.1
     */
    fun asNonDataMMapNN(): DataMMapNN? = asNonEmptyMMap()
    /**
     * Converts the current instance to a NonEmptyDataMMapNN type if possible.
     *
     * @return A NonEmptyDataMMapNN instance if the conversion is successful, or null otherwise.
     * @since 5.2.1
     */
    fun asNonEmptyDataMMapNN(): NonEmptyDataMMapNN? = asNonEmptyMMap()
    /**
     * Converts the current YAMLNode to a LocalDate object.
     *
     * This method interprets the YAMLNode's value as a string and attempts to parse it into a LocalDate.
     * The parsing uses the ISO_LOCAL_DATE format. If the string cannot be parsed, an exception is thrown.
     *
     * @return the LocalDate representation of the node's value.
     * @since 3.0.0
     */
    fun asDate() = asString()?.let(::LocalDate)
    /**
     * Converts the current YAMLNode to an OffsetDateTime representation.
     *
     * This function assumes the node's string value represents a valid date-time
     * with an offset in a format compatible with ISO_OFFSET_DATE_TIME.
     * An exception will be thrown if parsing fails.
     *
     * @return an OffsetDateTime object parsed from the node's value.
     * @since 3.0.0
     */
    fun asDateTime(): OffsetDateTime? = asString()?.let(::OffsetDateTime)
    /**
     * Converts the current node to an [Instant] if possible.
     *
     * This method attempts to parse the string representation of the current YAML node
     * as an [Instant] using the ISO-8601 format. If parsing fails or the value cannot be
     * represented as an [Instant], an exception is thrown.
     *
     * @return The parsed [Instant] instance representing the node's value.
     * @since 3.0.0
     */
    fun asInstant(): Instant? = asString()?.let(::Instant)

    /**
     * Returns a string representation of the YAMLNode object.
     *
     * This implementation delegates to the `toString` method
     * of the underlying raw value contained within the node.
     *
     * @return a string representation of the raw value encapsulated by this node.
     * @since 3.0.0
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
     * @since 3.0.0
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
     * @since 3.0.0
     */
    operator fun invoke(index: Int) = !get(index).isMissing
}