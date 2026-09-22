/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:OptIn(Beta::class)

package dev.tommasop1804.kutils.classes.coding

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.Json.Companion.MAPPER
import dev.tommasop1804.kutils.classes.coding.Json.Companion.toJson
import dev.tommasop1804.kutils.classes.collections.NonEmptyMList.Companion.toNonEmptyMList
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.errors.*
import dev.tommasop1804.kutils.exceptions.*
import org.jetbrains.exposed.v1.core.Table
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.File
import java.nio.file.Path
import kotlin.reflect.typeOf

/**
 * Represents a CSV (Comma-Separated Values) dataset with various utility methods for processing
 * and converting the content into different data structures or formats.
 *
 * @property value The underlying string representation of the CSV content.
 * @property separator The character used as the delimiter for separating values within the CSV content.
 * @property hasHeaders A flag indicating whether the first row of the CSV content should be treated as headers.
 * @property withoutHeaders A flag indicating whether the CSV content lacks headers.
 * @property headers A list of header names extracted from the CSV content, if available.
 * @property rowCount The total number of rows in the CSV content, including the header row if present.
 */
@JsonSerialize(using = Csv.Companion.Serializer::class)
@JsonDeserialize(using = Csv.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Csv.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Csv.Companion.OldDeserializer::class)
@Suppress("unused", "UNCHECKED_CAST", "kutils_collection_declaration")
@Beta(since = "3.13.0")
@MustUseReturnValues
class Csv(override var value: String, val separator: Char = Char.COMMA, val hasHeaders: Boolean = true) : CharSequence, Code(value, Language.Csv) {

    /**
     * Retrieves a version of the CSV content without its headers.
     *
     * If the current CSV instance has headers, this property returns a new `Csv` object
     * with the headers removed. If the current CSV instance does not have headers, it
     * returns the same instance.
     *
     * @return A `Csv` object representing the same content but without headers,
     *         or the original instance if no headers are present.
     * @since 3.13.0
     */
    val withoutHeaders get() = if (!hasHeaders) this else Csv(value.substringAfter("\n"), separator, false)

    /**
     * Secondary constructor that initializes an instance using a [Code] object.
     *
     * @param code The [Code] object containing the value to initialize.
     * @since 3.13.0
     */
    constructor(code: Code, separator: Char = Char.COMMA, hasHeaders: Boolean = true) : this(code.value, separator, hasHeaders) {
        code.language.expect(Language.Csv)
    }

    /**
     * Creates an instance by reading the content of the specified file.
     *
     * @param file The file whose content will be read and used to initialize the instance.
     * @since 3.13.0
     */
    constructor(file: File, separator: Char = Char.COMMA, hasHeaders: Boolean = true) : this(file.readText(), separator, hasHeaders) {
        file.exists().expect(true)
        file.isFile.expect(true)
        file.canRead().expect(true)
        file.extension.validate(file::extension, "file") { it equalsIgnoreCase "csv" }
    }

    /**
     * Creates an instance by reading the content of the file at the specified path.
     *
     * @param path The path of the file whose content will be read and used to initialize the instance.
     * @since 3.13.0
     */
    constructor(path: Path, separator: Char = Char.COMMA, hasHeaders: Boolean = true) : this(path.toFile(), separator, hasHeaders)

    init {
        if (value.isNotBlank()) {
            runCatching {
                val schema = CsvSchema.emptySchema().withColumnSeparator(separator)
                CSV_MAPPER.readerFor(Array<String>::class.java)
                    .with(schema)
                    .readValues<Array<String>>(value)
                    .readAll()
            }.onFailure { e ->
                throw MalformedInputException("Invalid CSV: ${e.message}")
            }
        }
    }

    /**
     * The list of column headers extracted from the first row of the CSV content.
     *
     * @since 3.13.0
     */
    val headers: List<String>
        get() {
            if (!hasHeaders || value.isBlank()) return emptyList()
            val schema = CsvSchema.emptySchema().withColumnSeparator(separator)
            val rows = CSV_MAPPER.readerFor(Array<String>::class.java)
                .with(schema)
                .readValues<Array<String>>(value)
                .readAll()
            return if (rows.isEmpty()) emptyList() else rows.first().toList()
        }

    /**
     * The number of data rows in the CSV (excluding the header row).
     *
     * @since 3.13.0
     */
    val rowCount: Int
        get() {
            if (value.isBlank()) return 0
            val schema = CsvSchema.emptySchema().withColumnSeparator(separator)
            val rows = CSV_MAPPER.readerFor(Array<String>::class.java)
                .with(schema)
                .readValues<Array<String>>(value)
                .readAll()
            return if (hasHeaders) (if (rows.size > 1) rows.size - 1 else 0) else rows.size
        }

    companion object {
        /**
         * The Jackson [CsvMapper] used to read/write CSV content.
         *
         * @since 3.13.0
         */
        val CSV_MAPPER: CsvMapper = CsvMapper().apply {
            enable(CsvParser.Feature.WRAP_AS_ARRAY)
        }

        /**
         * Checks if the string is in a valid CSV format based on the provided separator and header configuration.
         *
         * @param separator The character used to separate fields in the CSV. Defaults to ','.
         * @param hasHeaders Indicates whether the CSV content includes a header row. Defaults to true.
         * @return A boolean indicating whether the string is valid CSV content.
         * @since 6.1.0
         */
        fun String.isValidCsv(separator: Char = Char.COMMA, hasHeaders: Boolean = true) =
            runCatching { Csv(this, separator, hasHeaders) }.isSuccess

        /**
         * Converts the current [File] into a [Csv] representation.
         *
         * The method parses the file content, applying the specified separator
         * and handling headers if applicable. The resulting [Csv] object can be
         * used for further manipulation or data extraction.
         *
         * @param separator The character used to separate values in the CSV file. Defaults to ','.
         * @param hasHeaders A boolean indicating whether the CSV file contains headers. Defaults to true.
         * @return An `Either` wrapping a [Csv] object on success, or an [InvalidConversionBetweenTypes] error if the conversion fails.
         * @since 6.1.0
         */
        fun File.toCsv(separator: Char = Char.COMMA, hasHeaders: Boolean = true) = either {
            catching({ Csv(this@toCsv, separator, hasHeaders) }) { t: Throwable ->
                InvalidConversionBetweenTypes(this@toCsv, typeOf<File>(), typeOf<Csv>(), t)
            }
        }
        /**
         * Converts the content of the file at the given path into a CSV representation.
         *
         * This method attempts to parse the file at the specified `Path` into a `Csv` object.
         * It allows customization of the separator character and whether headers are present in the data.
         * If the conversion fails, an `InvalidConversion` error is returned wrapped in an `Either`.
         *
         * @param separator The character used to separate CSV values. Defaults to `Char.COMMA`.
         * @param hasHeaders A flag indicating whether the CSV data contains headers. Defaults to `true`.
         * @return An `Either` containing a `Csv` object on success, or an `InvalidConversion` error on failure.
         * @since 6.1.0
         */
        fun Path.toCsv(separator: Char = Char.COMMA, hasHeaders: Boolean = true) = either {
            catching({ Csv(this@toCsv, separator, hasHeaders) }) { t: Throwable ->
                InvalidConversionBetweenTypes(this@toCsv, typeOf<Path>(), typeOf<Csv>(), t)
            }
        }

        /**
         * Converts the string to a CSV representation, creating an instance of the `Csv` class.
         * The method handles any exceptions by returning an error representation.
         *
         * @param separator The character used to separate values in the CSV. Defaults to `Char.COMMA`.
         * @param hasHeaders Whether the CSV content includes headers. Defaults to `true`.
         * @return An `Either` wrapping the result, where the left side contains an `InvalidFormat` error
         *         in case of failure, and the right side contains the successfully created `Csv` instance.
         * @since 6.1.0
         */
        fun String.toCsv(separator: Char = Char.COMMA, hasHeaders: Boolean = true) = either {
            catching({ Csv(this@toCsv, separator, hasHeaders) }) { t: Throwable ->
                InvalidFormatOfType(this@toCsv, typeOf<Csv>(), t)
            }
        }

        /**
         * Converts the current JSON instance into its equivalent CSV representation.
         *
         * The JSON must be an array of objects. Each object becomes a CSV row, with
         * the union of all keys used as the header row.
         *
         * @return A [Csv] instance representing the tabular data of the original JSON input.
         * @since 3.13.0
         */
        @JvmName("jsonToCsv")
        fun Json.toCsv(separator: Char = Char.COMMA, hasHeaders: Boolean = true): Csv {
            val list = MAPPER.readValue(value, List::class.java) as List<*>
            val maps = list.map { item ->
                when (item) {
                    is Map<*, *> -> item.entries.associate { it.key.toString() to it.value }
                    else -> mapOf("value" to item)
                }
            }
            return listOfMapsToCsv(maps, separator, hasHeaders)
        }

        /**
         * Converts the current YAML instance into its equivalent CSV representation.
         *
         * @return A [Csv] instance representing the tabular data of the original YAML input.
         * @since 3.13.0
         */
        @JvmName("yamlToCsv")
        fun Yaml.toCsv(separator: Char = Char.COMMA, hasHeaders: Boolean = true): Csv = toJson().toCsv(separator, hasHeaders)

        /**
         * Converts the current TOML instance into its equivalent CSV representation.
         *
         * @return A [Csv] instance representing the tabular data of the original TOML input.
         * @since 3.13.0
         */
        @JvmName("tomlToCsv")
        fun Toml.toCsv(separator: Char = Char.COMMA, hasHeaders: Boolean = true): Csv = toJson().toCsv(separator, hasHeaders)

        /**
         * Converts the current XML instance into its equivalent CSV representation.
         *
         * @return A [Csv] instance representing the tabular data of the original XML input.
         * @since 3.13.0
         */
        @JvmName("xmlToCsv")
        fun Xml.toCsv(separator: Char = Char.COMMA, hasHeaders: Boolean = true): Csv = toJson().toCsv(separator, hasHeaders)

        /**
         * Converts an arbitrary object to its CSV representation.
         *
         * @return A [Csv] instance representing the object as CSV.
         * @since 3.13.0
         */
        @JvmName("anyToCsv")
        fun Any?.toCsv(separator: Char = Char.COMMA, hasHeaders: Boolean = true): Csv = when (this) {
            null -> Csv("", separator, hasHeaders)
            is Csv -> this
            is Json -> this.toCsv(separator, hasHeaders)
            is Yaml -> this.toCsv(separator, hasHeaders)
            is Toml -> this.toCsv(separator, hasHeaders)
            is Xml -> this.toCsv(separator, hasHeaders)
            is List<*> -> {
                if (isEmpty()) Csv("", separator, hasHeaders)
                else if (first() is Map<*, *>) listOfMapsToCsv(
                    (this as List<Map<*, *>>).map { m -> m.entries.associate { it.key.toString() to it.value } },
                    separator, hasHeaders
                )
                else Csv(joinToString("\n") { it.toString() }, separator, hasHeaders)
            }
            is Map<*, *> -> listOfMapsToCsv(listOf(entries.associate { it.key.toString() to it.value }), separator, hasHeaders)
            else -> Json(MAPPER.writeValueAsString(this)).toCsv(separator, hasHeaders)
        }

        /**
         * Reads an array of the specified type from the given CSV file.
         *
         * @param T The type of elements to be read and stored in the resulting array.
         * @param file The file from which the array is read.
         * @return A [Result] containing the array of type [T].
         * @since 3.13.0
         */
        inline fun <reified T> readArrayFromFile(file: File, separator: Char = Char.COMMA, hasHeaders: Boolean = true): Result<Array<T>> =
            runCatching { readListFromFile<T>(file, separator, hasHeaders)().toTypedArray() }

        /**
         * Reads and parses a list of objects from the specified CSV file.
         *
         * @param file The file to read from. It should contain CSV-formatted data.
         * @return A [Result] containing the parsed list of objects of type [T].
         * @since 3.13.0
         */
        inline fun <reified T> readListFromFile(file: File, separator: Char = Char.COMMA, hasHeaders: Boolean = true): Result<List<T>> =
            runCatching { Csv(file.readText(), separator, hasHeaders).toList<T>()() }

        /**
         * Reads the content of a given file, parses it as CSV, and converts it to a set of type [T].
         *
         * @param file The file to be read, whose content is expected to be in CSV format.
         * @return A [Result] containing a [Set] of elements of type [T].
         * @since 3.13.0
         */
        inline fun <reified T> readSetFromFile(file: File, separator: Char = Char.COMMA, hasHeaders: Boolean = true): Result<Set<T>> = runCatching { Csv(file.readText(), separator, hasHeaders).toSet<T>()() }

        class Serializer : ValueSerializer<Csv>() {
            override fun serialize(value: Csv, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writePOJO(value.toListOfMaps())
            }
        }

        class Deserializer : ValueDeserializer<Csv>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Csv =
                Json(p.objectReadContext().readTree<JsonNode>(p).toString()).toCsv()
        }

        class OldSerializer : JsonSerializer<Csv>() {
            override fun serialize(value: Csv, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeObject(value.toListOfMaps())
            }
        }

        class OldDeserializer : JsonDeserializer<Csv>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Csv =
                Json(p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p).toString()).toCsv()
        }

        internal fun escapeCsvField(field: String, separator: Char = ','): String {
            return if (field.contains(separator) || field.contains('"') || field.contains('\n') || field.contains('\r')) {
                "\"${field.replace("\"", "\"\"")}\""
            } else field
        }

        internal fun listOfMapsToCsv(list: List<Map<String, Any?>>, separator: Char = ',', hasHeaders: Boolean = true): Csv {
            if (list.isEmpty()) return Csv("", separator, hasHeaders)
            val headers = list.flatMap { it.keys }.distinct()
            val sep = separator.toString()
            val lines = buildList {
                if (hasHeaders) add(headers.joinToString(sep) { escapeCsvField(it, separator) })
                list.forEach { row ->
                    add(headers.joinToString(sep) { h -> escapeCsvField(row[h]?.toString() ?: "", separator) })
                }
            }
            return Csv(lines.joinToString("\n"), separator, hasHeaders)
        }

        /**
         * Registers a column with CSV data type for the specified name in the table.
         * This method is tailored for handling CSV content stored in a PostgreSQL database,
         * providing type-safe interaction with the CSV data through the [Csv] class.
         *
         * @param name The name of the CSV column to be registered in the table.
         * @since 5.5.0
         */
        fun Table.csv(name: String) = jsonb<Csv>(name)
    }

    /**
     * Converts a collection into a list of arrays of the specified type.
     *
     * This function processes the elements in the underlying collection
     * and maps each item to a typed array of the specified generic type [T].
     * It uses the `toList` function to gather elements and applies
     * a transformation using `map` to create the result.
     *
     * The function requires the reified type parameter [T] to ensure
     * type information is available at runtime.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @param T The type of the elements in the resulting arrays.
     * @return A list of arrays, where each array is of type [T].
     *
     * @since 6.1.0
     */
    inline fun <reified T> toArray() = toJson().toArray<T>()

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
    inline fun <reified T> toList() = toJson().toList<T>()
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
    inline fun <reified T> toNonEmptyList() = toJson().toNonEmptyList<T>()

    /**
     * Transforms the elements of a list to a mutable list of the specified type.
     *
     * This function leverages reified type parameters to infer the type of the list's elements at runtime.
     * It converts the elements of the source list to mutable lists using the provided mapping logic.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @param T The type of elements contained in the resulting mutable list.
     * @return A new list where each element is a mutable list of type T.
     *
     * @since 6.1.0
     */
    inline fun <reified T> toMList() = toJson().toMList<T>()
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
    inline fun <reified T> toNonEmptyMList() = toNonEmptyList<T>().map { it.toNonEmptyMList() }

    /**
     * Converts a collection of collections into a list of corresponding sets.
     *
     * This function iterates through a collection of collections, converts each
     * individual collection into a set, and returns the results as a list.
     * The operation ensures that elements in each inner collection are unique
     * within their respective sets.
     *
     * Inline and reified modifiers allow for type inference at compile time,
     * ensuring the function works generically with the type parameter [T].
     *
     * @return A list of sets, where each set corresponds to a distinct collection
     *         converted from the original data.
     *
     * @throws UnsupportedOperationException If the function is not called
     *         on a collection or list-like structure.
     *
     * @since 6.1.0
     */
    inline fun <reified T> toSet() = toJson().toSet<T>()
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
    inline fun <reified T> toNonEmptySet() = toJson().toNonEmptySet<T>()

    /**
     * Converts elements of type `T` into a mutable set (`MSet`).
     *
     * This function operates on a collection or sequence to transform its elements into
     * a mutable set. The method utilizes the reified type parameter `T` to ensure type safety
     * and implicitly infers the type, making it convenient for generic conversions.
     *
     * It starts by creating a standard set from the elements and then applies the `toMSet`
     * conversion on the resulting set.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     *
     * @return A new mutable set (`MSet`) containing the transformed elements.
     * @param T The type of elements included in the set.
     *
     * @since 6.1.0
     */
    inline fun <reified T> toMSet() = toJson().toMSet<T>()
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
    inline fun <reified T> toNonEmptyMSet() = toJson().toNonEmptyMSet<T>()

    /**
     * Converts the CSV content into a list of maps, where each map represents a row of the CSV data.
     * If headers are present, the map keys will correspond to the header names.
     * Otherwise, the map keys will be generated as "col0", "col1", etc., based on column indices.
     *
     * @return A list of maps where each map represents a row of CSV data.
     *         Returns an empty list if the CSV content is blank or contains no rows.
     * @since 3.13.0
     */
    fun toListOfMaps(): List<StringMap> {
        if (value.isBlank()) return emptyList()
        val baseSchema = CsvSchema.emptySchema().withColumnSeparator(separator)
        if (hasHeaders) {
            return CSV_MAPPER.readerFor(Map::class.java)
                .with(baseSchema.withHeader())
                .readValues<StringMap>(value)
                .readAll()
        }
        val rows = CSV_MAPPER.readerFor(Array<String>::class.java)
            .with(baseSchema)
            .readValues<Array<String>>(value)
            .readAll()
        if (rows.isEmpty()) return emptyList()
        val colCount = rows.maxOf { it.size }
        return rows.map { row ->
            (0 until colCount).associate { i -> "col$i" to row.getOrElse(i) { "" } }
        }
    }

    /**
     * Retrieves the character at the specified index.
     *
     * @param index The position of the character to retrieve.
     * @return The character at the specified index.
     * @since 3.13.0
     */
    override fun get(index: Int) = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence.
     *
     * @since 3.13.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns the raw CSV string representation.
     *
     * @since 3.13.0
     */
    override fun toString() = value

    /**
     * Writes the CSV content to the provided file.
     *
     * @param file The file to which the content will be written.
     * @since 3.13.0
     */
    fun writeToFile(file: File) = file.writeText(value)

    /**
     * Creates a new instance of the Csv class with the specified separator character.
     *
     * @param separator The character to use as the CSV field separator.
     * @since 3.13.0
     */
    infix fun withSeparator(separator: Char) = Csv(value, separator, hasHeaders)
}
