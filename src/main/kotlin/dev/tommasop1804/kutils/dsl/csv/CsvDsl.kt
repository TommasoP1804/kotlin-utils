/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused", "UnusedReceiverParameter", "PropertyName")
@file:Since("3.13.0")
@file:OptIn(Beta::class)

package dev.tommasop1804.kutils.dsl.csv

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*

@DslMarker
annotation class CsvDslMarker

// --- VALUE TYPES ---

/**
 * Represents a value in a CSV field.
 *
 * This interface is the base type for all CSV field values, including strings, numbers,
 * booleans, and null (empty) values. Each implementation handles its own rendering
 * according to RFC 4180 escaping rules.
 *
 * @since 3.13.0
 * @author Tommaso Pastorelli
 */
sealed interface CsvValue {
    /**
     * Renders this CSV value as a field string, applying RFC 4180 escaping where needed.
     *
     * Fields containing the separator, double quotes, or newlines are enclosed in double quotes,
     * and embedded double quotes are escaped by doubling them.
     *
     * @param separator The column separator character used for escaping. Defaults to `,`.
     * @return The properly escaped CSV field string.
     * @since 3.13.0
     */
    fun toCsvField(separator: Char = ','): String
}

/**
 * Represents a CSV string field value.
 *
 * This class wraps a raw string value and provides RFC 4180 escaping.
 * Fields containing commas, double quotes, or line breaks are enclosed in double
 * quotes, with internal double quotes doubled.
 *
 * @property value The raw string that this instance represents.
 * @since 3.13.0
 */
data class CsvString(val value: String) : CsvValue {
    /**
     * Converts the string value to its properly escaped CSV field representation.
     *
     * @param separator The column separator character used for escaping. Defaults to `,`.
     * @return The CSV-escaped string representation of the value.
     * @since 3.13.0
     */
    override fun toCsvField(separator: Char): String {
        return if (value.contains(separator) || value.contains('"') || value.contains('\n') || value.contains('\r')) {
            "\"${value.replace("\"", "\"\"")}\""
        } else value
    }
}

/**
 * Represents a CSV numeric field value.
 *
 * This class encapsulates a numeric value. When rendered, floating-point numbers
 * with no fractional component are emitted as integers.
 *
 * @property value The numeric value to be represented.
 * @since 3.13.0
 */
data class CsvNumber(val value: Number) : CsvValue {
    /**
     * Converts the numeric value to its CSV field representation.
     *
     * @param separator The column separator character (unused for numbers). Defaults to `,`.
     * @return The string representation of the numeric value.
     * @since 3.13.0
     */
    override fun toCsvField(separator: Char): String = when (value) {
        is Float -> if (value % 1.0f == 0f && !value.isInfinite()) value.toLong().toString() else value.toString()
        is Double -> if (value % 1.0 == 0.0 && !value.isInfinite()) value.toLong().toString() else value.toString()
        else -> value.toString()
    }
}

/**
 * Represents a CSV boolean field value.
 *
 * @property value The boolean value represented by this instance.
 * @since 3.13.0
 */
data class CsvBoolean(val value: Boolean) : CsvValue {
    /**
     * Converts the boolean value to its CSV field representation (`true` or `false`).
     *
     * @param separator The column separator character (unused for booleans). Defaults to `,`.
     * @return The string representation of the boolean value.
     * @since 3.13.0
     */
    override fun toCsvField(separator: Char): String = value.toString()
}

/**
 * Represents an empty/missing CSV field value.
 *
 * Rendered as an empty string in the CSV output.
 *
 * @since 3.13.0
 */
data object CsvNull : CsvValue {
    /**
     * Returns an empty string representing a missing value.
     *
     * @param separator The column separator character (unused for null). Defaults to `,`.
     * @return An empty string.
     * @since 3.13.0
     */
    override fun toCsvField(separator: Char): String = ""
}

// --- CONTAINER TYPES ---

/**
 * Represents a single CSV row (record) as an ordered list of field values.
 *
 * @property fields The list of [CsvValue] fields in this row.
 * @since 3.13.0
 */
data class CsvRecord(val fields: List<CsvValue>) {
    /**
     * Renders this record as a single CSV line.
     *
     * @param separator The column separator character. Defaults to `,`.
     * @return The separated field values for this row.
     * @since 3.13.0
     */
    fun toCsvLine(separator: Char = ','): String = fields.joinToString(separator.toString()) { it.toCsvField(separator) }
}

/**
 * Represents a complete CSV document with headers and data rows.
 *
 * @property headers The list of column header names.
 * @property rows The list of data rows, each represented as a [CsvRecord].
 * @since 3.13.0
 */
data class CsvTable(
    val headers: List<String>,
    val rows: List<CsvRecord>,
    val separator: Char = ',',
    val hasHeaders: Boolean = true
) {
    /**
     * Renders this table as a complete CSV document.
     *
     * When [hasHeaders] is `true`, the first line contains the headers followed by data rows.
     * When `false`, only data rows are emitted.
     *
     * @return A [Csv] instance containing the rendered CSV string.
     * @since 3.13.0
     */
    fun toCsv(): Csv {
        val sep = separator.toString()
        if (hasHeaders && headers.isNotEmpty()) {
            val headerLine = headers.joinToString(sep) { CsvString(it).toCsvField(separator) }
            if (rows.isEmpty()) return Csv(headerLine, separator)
            val dataLines = rows.joinToString("\n") { it.toCsvLine(separator) }
            return Csv("$headerLine\n$dataLines", separator)
        }
        if (rows.isEmpty()) return Csv("", separator, false)
        val dataLines = rows.joinToString("\n") { it.toCsvLine(separator) }
        return Csv(dataLines, separator, false)
    }

    /**
     * The number of data rows in this table.
     *
     * @since 3.13.0
     */
    val rowCount: Int get() = rows.size

    /**
     * The number of columns in this table.
     *
     * @since 3.13.0
     */
    val columnCount: Int get() = headers.size
}

// --- CONVERSION ---

/**
 * Converts an arbitrary object into a CSV-compatible [CsvValue].
 *
 * - `null` is mapped to [CsvNull].
 * - Instances of [CsvValue] are returned as is.
 * - Strings are wrapped in [CsvString].
 * - Numeric types are wrapped in [CsvNumber].
 * - Booleans are wrapped in [CsvBoolean].
 * - Any other object is converted to a string via `toString()` and wrapped in [CsvString].
 *
 * @return A [CsvValue] representing the CSV field form of this object.
 * @since 3.13.0
 */
fun Any?.toCsvValue(): CsvValue = when (this) {
    null -> CsvNull
    is CsvValue -> this
    is String -> CsvString(this)
    is Int -> CsvNumber(this)
    is Long -> CsvNumber(this)
    is Double -> CsvNumber(this)
    is Float -> CsvNumber(this)
    is Boolean -> CsvBoolean(this)
    is Number -> CsvNumber(this)
    else -> CsvString(toString())
}

// --- ROW BUILDER ---

/**
 * A builder for constructing a single CSV row in a DSL-friendly manner.
 *
 * Fields are added in order using the unary plus operator or the [field] method.
 *
 * @since 3.13.0
 */
@CsvDslMarker
class CsvRowBuilder {
    @PublishedApi
    internal val fields = emptyMList<CsvValue>()

    /** Appends the string as a [CsvString] field.
     * @since 3.13.0
     */
    operator fun String.unaryPlus() { fields += CsvString(this) }

    /** Appends the number as a [CsvNumber] field.
     * @since 3.13.0
     */
    operator fun Number.unaryPlus() { fields += CsvNumber(this) }

    /** Appends the boolean as a [CsvBoolean] field.
     * @since 3.13.0
     */
    operator fun Boolean.unaryPlus() { fields += CsvBoolean(this) }

    /** Appends a [CsvValue] directly.
     * @since 3.13.0
     */
    operator fun CsvValue.unaryPlus() { fields += this }

    /**
     * Adds a field by converting any value to a [CsvValue].
     *
     * @param value The value to add as a field. Can be of any type or null.
     * @since 3.13.0
     */
    fun field(value: Any?) { fields += value.toCsvValue() }

    /**
     * Adds an empty (null) field.
     *
     * @since 3.13.0
     */
    fun nullField() { fields += CsvNull }

    /**
     * Builds and returns a [CsvRecord] containing all the fields added.
     *
     * @return A [CsvRecord] containing the fields.
     * @since 3.13.0
     */
    fun build(): CsvRecord = CsvRecord(fields.toList())
}

// --- TABLE BUILDER ---

/**
 * A builder for constructing a complete CSV table with headers and rows.
 *
 * Use [headers] to define column names, then add rows using [row] with either
 * a DSL block or varargs.
 *
 * @since 3.13.0
 */
@CsvDslMarker
class CsvTableBuilder {
    private val _headers = emptyMList<String>()

    @PublishedApi
    internal val _rows = emptyMList<CsvRecord>()

    /**
     * The column separator character for the CSV output. Defaults to `,`.
     *
     * @since 3.13.0
     */
    var separator: Char = ','

    /**
     * Whether the CSV output should include a header row. Defaults to `true`.
     *
     * @since 3.13.0
     */
    var hasHeaders: Boolean = true

    /**
     * Defines the column headers for this CSV table.
     *
     * @param names The header names, in order.
     * @since 3.13.0
     */
    fun headers(vararg names: String) { _headers.addAll(names) }

    /**
     * Defines the column headers for this CSV table from a list.
     *
     * @param names The list of header names.
     * @since 3.13.0
     */
    fun headers(names: List<String>) { _headers.addAll(names) }

    /**
     * Adds a row using a DSL block to define its fields.
     *
     * @param block A DSL block applied to a [CsvRowBuilder] to define the row's fields.
     * @since 3.13.0
     */
    fun row(block: ReceiverConsumer<CsvRowBuilder>) {
        _rows += CsvRowBuilder().apply(block).build()
    }

    /**
     * Adds a row from varargs values, converting each to a [CsvValue].
     *
     * @param values The values for each field in the row.
     * @since 3.13.0
     */
    fun row(vararg values: Any?) {
        _rows += CsvRecord(values.map { it.toCsvValue() })
    }

    /**
     * Builds and returns a [CsvTable] with the defined headers and rows.
     *
     * @return A [CsvTable] containing the headers and all added rows.
     * @since 3.13.0
     */
    fun build(): CsvTable = CsvTable(_headers.toList(), _rows.toList(), separator, hasHeaders)
}

// --- TOP-LEVEL ENTRY POINTS ---

/**
 * Builds a complete CSV document using the provided DSL block.
 *
 * The block is applied to a [CsvTableBuilder] where you define headers and rows.
 *
 * @param block A DSL block defining the structure of the CSV document.
 * @return A [Csv] object representing the complete CSV document.
 * @since 3.13.0
 */
fun buildCsv(block: ReceiverConsumer<CsvTableBuilder>): Csv =
    CsvTableBuilder().apply(block).build().toCsv()

/**
 * Initializes and returns a [CsvTable] using the specified DSL block, without
 * rendering it to a string. Useful when the table will be further manipulated.
 *
 * @param block A DSL block defining the structure of the table.
 * @return The constructed [CsvTable].
 * @since 3.13.0
 */
fun initCsv(block: ReceiverConsumer<CsvTableBuilder>): CsvTable =
    CsvTableBuilder().apply(block).build()

/**
 * Creates a CSV table using the provided DSL block.
 *
 * @param block A lambda with a [CsvTableBuilder] receiver defining the table contents.
 * @return The constructed [CsvTable].
 * @since 3.13.0
 */
fun csvTable(block: ReceiverConsumer<CsvTableBuilder>): CsvTable =
    CsvTableBuilder().apply(block).build()

/**
 * Creates a single CSV row using the provided DSL block.
 *
 * @param block A lambda with a [CsvRowBuilder] receiver defining the row's fields.
 * @return The constructed [CsvRecord].
 * @since 3.13.0
 */
fun csvRow(block: ReceiverConsumer<CsvRowBuilder>): CsvRecord =
    CsvRowBuilder().apply(block).build()
