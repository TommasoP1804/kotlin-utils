/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused", "UnusedReceiverParameter")
@file:Since("3.7.0")
@file:OptIn(Beta::class)

package dev.tommasop1804.kutils.dsl.toml

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*

@DslMarker
annotation class TomlDslMarker

// --- VALUE TYPES ---

/**
 * Represents a value in a TOML structure.
 *
 * This interface is the base type for all TOML values, including strings, integers, floats,
 * booleans, datetimes, tables, arrays, and arrays of tables. Unlike JSON or YAML, TOML
 * distinguishes between *inline* values (rendered on a single line as part of a `key = value`
 * assignment) and *table* values (rendered as standalone `[header]` sections). Each value
 * implementation handles its own inline rendering; full-document rendering with section
 * headers is performed by [TomlObject.toToml].
 *
 * @since 3.11.0
 * @author Tommaso Pastorelli
 */
sealed interface TomlValue {
    /**
     * Renders this TOML value in inline form, suitable for use on the right-hand side
     * of a `key = value` assignment or as an element inside an inline array.
     *
     * @return The inline TOML string representation of the value.
     * @since 3.11.0
     */
    fun toInline(): String
}

/**
 * Represents a TOML string value.
 *
 * This class wraps a raw string value and provides functionality to properly
 * encode the value for TOML serialization. The encoder selects between basic
 * (double-quoted with escapes) and multi-line basic (`"""..."""`) styles based
 * on whether the value contains newlines.
 *
 * @property value The raw string that this `TomlString` instance represents.
 * @since 3.11.0
 */
data class TomlString(val value: String) : TomlValue {
    /**
     * Converts the TOML string to its properly encoded TOML representation.
     *
     * Strings containing newline characters are emitted using multi-line basic
     * string syntax (`"""..."""`). All other strings are emitted as basic strings,
     * with control characters, backslashes, and double quotes escaped.
     *
     * @return The TOML-formatted string representation of the value.
     * @since 3.11.0
     */
    override fun toInline(): String {
        if (value.contains('\n')) {
            // Multi-line basic string: only escape backslashes and triple-quote sequences
            val escaped = value.replace("\\", "\\\\").replace("\"\"\"", "\"\"\\\"")
            return "\"\"\"\n$escaped\"\"\""
        }
        return "\"${escapeBasic(value)}\""
    }

    private fun escapeBasic(s: String): String = buildString {
        for (c in s) {
            when (c) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\b' -> append("\\b")
                '\u000C' -> append("\\f")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> if (c.code < 0x20 || c.code == 0x7F) append("\\u%04X".format(c.code)) else append(c)
            }
        }
    }
}

/**
 * Represents a TOML number within a TOML structure.
 *
 * This class is used to encapsulate numeric values in TOML format. It supports integer
 * and floating-point numbers. When serialized to TOML, floating-point numbers are
 * converted to integers if they have no fractional component, unless they are infinite
 * or NaN. Special floating-point values use TOML's reserved representation
 * (`nan`, `inf`, `-inf`).
 *
 * @constructor Creates a TomlNumber with the specified numeric value.
 * @param value The numeric value to be represented as a TOML number.
 * @since 3.11.0
 */
data class TomlNumber(val value: Number) : TomlValue {
    /**
     * Converts the value of this TomlNumber instance to its TOML string representation.
     *
     * TOML requires floating-point values to contain either a decimal point or an
     * exponent, so a `.0` suffix is appended to whole-number floats.
     *
     * @return The TOML string representation of the value.
     * @since 3.11.0
     */
    override fun toInline(): String = when (value) {
        is Float -> when {
            value.isNaN() -> "nan"
            value.isInfinite() -> if (value > 0) "inf" else "-inf"
            value % 1.0f == 0f -> "${value.toLong()}.0"
            else -> value.toString()
        }
        is Double -> when {
            value.isNaN() -> "nan"
            value.isInfinite() -> if (value > 0) "inf" else "-inf"
            value % 1.0 == 0.0 -> "${value.toLong()}.0"
            else -> value.toString()
        }
        else -> value.toString()
    }
}

/**
 * Represents a TOML boolean value.
 *
 * This class encapsulates a boolean value as a TOML-compatible representation. It implements the `TomlValue` interface,
 * enabling it to be serialized into a TOML string. Instances of this class are immutable and directly map to a TOML
 * boolean (`true` or `false`).
 *
 * @property value The boolean value represented by this TOML boolean instance.
 * @since 3.11.0
 */
data class TomlBoolean(val value: Boolean) : TomlValue {
    /**
     * Converts the boolean value to its TOML string representation.
     *
     * @return The TOML string representation of the boolean value.
     * @since 3.11.0
     */
    override fun toInline(): String = value.toString()
}

/**
 * Represents a TOML datetime value (offset, local, date, or time).
 *
 * TOML has no `null` literal, so this class also serves as the placeholder for
 * temporal types. The raw string is emitted verbatim and is expected to already
 * conform to one of the four TOML datetime formats (RFC 3339 offset, local
 * datetime, local date, or local time).
 *
 * @property value The raw, pre-formatted datetime string.
 * @since 3.11.0
 */
data class TomlDateTime(val value: String) : TomlValue {
    override fun toInline(): String = value
}

/**
 * Represents a TOML table (mapping), a collection of key-value pairs where
 * each key is a string and each value is a [TomlValue].
 *
 * Keys are unique and maintain insertion order. Unlike [TomlString] or [TomlNumber],
 * a table can be rendered in two distinct ways depending on context: as a standalone
 * `[header]` section at document level (handled by [toToml]), or as an inline table
 * `{ k = v, ... }` when used as an array element or nested under another inline table
 * (handled by [toInline]).
 *
 * @property entries The internal map storing the key-value pairs of the TOML table.
 * @since 3.11.0
 */
data class TomlObject(val entries: LinkedHashMap<String, TomlValue>) : TomlValue {
    /**
     * Renders this table as an inline table `{ k = v, k2 = v2 }`.
     *
     * Used when the table appears as an element inside an inline array or as a
     * value inside another inline table. For document-level rendering with section
     * headers, see [toToml].
     *
     * @since 3.11.0
     */
    override fun toInline(): String {
        if (entries.isEmpty()) return "{}"
        return entries.entries.joinToString(", ", "{ ", " }") { [k, v] ->
            "${encodeKey(k)} = ${v.toInline()}"
        }
    }

    /**
     * Renders this table as a complete TOML document, emitting top-level scalars
     * first, then nested tables as `[header]` sections, and finally arrays of
     * tables as `[[header]]` sections, recursively.
     *
     * Tables that contain only scalar/inline values are flattened into their
     * parent section using dotted keys when nested deeply, but the default rendering
     * places each non-trivial table in its own header for readability.
     *
     * @return The full TOML document representation of this table.
     * @since 3.11.0
     */
    fun toToml(): Toml {
        val sb = StringBuilder()
        renderTable(sb, this, emptyList())
        return Toml(sb.toString().trimEnd())
    }

    /**
     * Retrieves the value associated with the specified key from this table.
     *
     * @param key the key whose associated value is to be returned
     * @return the value associated with the given key, or null if the key is not present
     * @since 3.11.0
     */
    operator fun get(key: String): TomlValue? = entries[key]

    private fun renderTable(sb: StringBuilder, table: TomlObject, path: List<String>) {
        // 1. Scalars and inline-rendered values first
        val scalars = table.entries.filter { [_, v] -> !isHeaderRendered(v) }
        if (scalars.isNotEmpty()) {
            if (path.isNotEmpty()) {
                sb.append("[").append(path.joinToString(".") { encodeKey(it) }).append("]\n")
            }
            for ([k, v] in scalars) {
                sb.append("${encodeKey(k)} = ${v.toInline()}\n")
            }
            sb.append("\n")
        } else if (path.isNotEmpty() && table.entries.isEmpty()) {
            // Empty named table still gets a header
            sb.append("[").append(path.joinToString(".") { encodeKey(it) }).append("]\n\n")
        }

        // 2. Nested tables and arrays of tables
        for ([k, v] in table.entries) {
            @Suppress("IntroduceWhenSubject")
            when {
                v is TomlObject && shouldRenderAsHeader(v) -> {
                    renderTable(sb, v, path + k)
                }
                v is TomlArray && v.isArrayOfTables() -> {
                    for (el in v.elements) {
                        val tbl = el as TomlObject
                        sb.append("[[").append((path + k).joinToString(".") { encodeKey(it) }).append("]]\n")
                        // Inline scalars
                        val inlineEntries = tbl.entries.filter { [_, vv] -> !isHeaderRendered(vv) }
                        for ([ik, iv] in inlineEntries) {
                            sb.append("${encodeKey(ik)} = ${iv.toInline()}\n")
                        }
                        sb.append("\n")
                        // Nested headers
                        for ([sk, sv] in tbl.entries) {
                            when (sv) {
                                is TomlObject if shouldRenderAsHeader(sv) ->
                                    renderTable(sb, sv, path + k + sk)

                                is TomlArray if sv.isArrayOfTables() -> {
                                    for (subEl in sv.elements) {
                                        val subTbl = subEl as TomlObject
                                        renderArrayOfTablesItem(sb, subTbl, path + k + sk)
                                    }
                                }

                                else -> { /* already handled inline */ }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderArrayOfTablesItem(sb: StringBuilder, tbl: TomlObject, path: List<String>) {
        sb.append("[[").append(path.joinToString(".") { encodeKey(it) }).append("]]\n")
        val inlineEntries = tbl.entries.filter { [_, vv] -> !isHeaderRendered(vv) }
        for ([ik, iv] in inlineEntries) {
            sb.append("${encodeKey(ik)} = ${iv.toInline()}\n")
        }
        sb.append("\n")
        for ([sk, sv] in tbl.entries) {
            when (sv) {
                is TomlObject if shouldRenderAsHeader(sv) ->
                    renderTable(sb, sv, path + sk)

                is TomlArray if sv.isArrayOfTables() ->
                    for (subEl in sv.elements) renderArrayOfTablesItem(sb, subEl as TomlObject, path + sk)

                else -> { /* inline */ }
            }
        }
    }

    private fun isHeaderRendered(v: TomlValue): Boolean = when (v) {
        is TomlObject -> shouldRenderAsHeader(v)
        is TomlArray -> v.isArrayOfTables()
        else -> false
    }

    private fun shouldRenderAsHeader(t: TomlObject): Boolean {
        // A table is rendered as a header section unless it was explicitly
        // built as inline. We default to header rendering for nested tables.
        return !t.inline
    }

    /**
     * When `true`, this table is rendered in inline form `{ ... }` when nested,
     * rather than as a `[header]` section. Defaults to `false`.
     * @since 3.11.0
     */
    var inline: Boolean = false
        internal set

    private fun encodeKey(k: String): String {
        if (k.isEmpty()) return "\"\""
        if (BARE_KEY_REGEX.matches(k)) return k
        return "\"${k.replace("\\", "\\\\").replace("\"", "\\\"")}\""
    }

    companion object {
        private val BARE_KEY_REGEX = Regex("^[A-Za-z0-9_-]+$")
    }
}

/**
 * Represents a TOML array, holding an ordered collection of TOML values.
 *
 * A TOML array can be either an *inline array* (rendered as `[a, b, c]`, mixing
 * scalar types is technically permitted in TOML 1.0 but discouraged) or an
 * *array of tables* (rendered as repeated `[[header]]` sections). The renderer
 * automatically chooses the array-of-tables form when every element is a
 * non-empty [TomlObject].
 *
 * @property elements The list of TOML values contained in the array.
 * @since 3.11.0
 * @author Tommaso Pastorelli
 */
data class TomlArray(val elements: List<TomlValue>) : TomlValue {
    /**
     * Renders this array in inline form `[a, b, c]`. For arrays of tables,
     * the surrounding [TomlObject.toToml] uses `[[header]]` syntax instead;
     * this method is only used when the array appears nested inside another
     * inline value.
     *
     * @since 3.11.0
     */
    override fun toInline(): String {
        if (elements.isEmpty()) return "[]"
        return elements.joinToString(", ", "[", "]") { it.toInline() }
    }

    /**
     * Returns `true` when every element is a non-inline [TomlObject], indicating
     * this array should be rendered using `[[header]]` syntax at document level.
     * @since 3.11.0
     */
    fun isArrayOfTables(): Boolean =
        elements.isNotEmpty() && elements.all { it is TomlObject && !it.inline }
}

// --- CONVERSION ---

/**
 * Converts an arbitrary object into a TOML-compatible [TomlValue].
 *
 * This function provides a generic mechanism for transforming any object
 * into a corresponding TOML representation. Specifically:
 * - `null` is **not supported** (TOML has no null literal); a [NullPointerException] is thrown.
 *   Callers should omit null entries instead.
 * - Instances of [TomlValue] are returned as is.
 * - Strings are wrapped in [TomlString].
 * - Numeric types (`Int`, `Long`, `Double`, `Float`, `Number`) are wrapped in [TomlNumber].
 * - Booleans are wrapped in [TomlBoolean].
 * - Maps are recursively converted into [TomlObject], with their keys converted to strings and
 *   their values converted into [TomlValue] instances.
 * - Lists and arrays are recursively converted into [TomlArray], with each element converted
 *   into a [TomlValue].
 * - Any other object is converted to a string via its `toString` method and wrapped in [TomlString].
 *
 * @return A [TomlValue] representing the TOML-serialized form of this object.
 * @since 3.11.0
 */
fun Any?.toTomlValue(): TomlValue = when (this) {
    null -> throw IllegalArgumentException("TOML has no null type; omit the key instead.")
    is TomlValue -> this
    is String -> TomlString(this)
    is Int -> TomlNumber(this)
    is Long -> TomlNumber(this)
    is Double -> TomlNumber(this)
    is Float -> TomlNumber(this)
    is Boolean -> TomlBoolean(this)
    is Number -> TomlNumber(this)
    is Map<*, *> -> TomlObject(LinkedHashMap(entries.associate { [k, v] -> k.toString() to v.toTomlValue() }))
    is List<*> -> TomlArray(map { it.toTomlValue() })
    is Array<*> -> TomlArray(map { it.toTomlValue() })
    else -> TomlString(toString())
}

// --- OBJECT BUILDER ---

/**
 * A builder for constructing TOML tables in a structured and readable manner.
 *
 * This builder provides DSL functions to associate keys with various types of
 * values, including strings, numbers, booleans, datetimes, and nested TOML structures.
 * Unlike JSON/YAML, **null values are not supported** in TOML and are silently skipped
 * when assigned via the infix `to` functions; this matches TOML semantics where the
 * absence of a key is the only way to express absence.
 *
 * The keys maintain their insertion order.
 * @since 3.11.0
 */
@TomlDslMarker
class TomlObjectBuilder(internal val inline: Boolean = false) {
    @PublishedApi
    internal val entries = LinkedHashMap<String, TomlValue>()

    /**
     * Associates the current string (key) with the provided string value.
     * If the value is null, the key is omitted (TOML has no null literal).
     * @since 3.11.0
     */
    infix fun String.to(value: String?) { if (value != null) entries[this] = TomlString(value) }
    /**
     * Associates the current string (key) with the provided numeric value.
     * If the value is null, the key is omitted (TOML has no null literal).
     * @since 3.11.0
     */
    infix fun String.to(value: Number?) { if (value != null) entries[this] = TomlNumber(value) }
    /**
     * Associates the current string (key) with the provided boolean value.
     * If the value is null, the key is omitted (TOML has no null literal).
     * @since 3.11.0
     */
    infix fun String.to(value: Boolean?) { if (value != null) entries[this] = TomlBoolean(value) }
    /**
     * Associates the current string (key) with the provided [TomlValue].
     * If the value is null, the key is omitted (TOML has no null literal).
     * @since 3.11.0
     */
    infix fun String.to(value: TomlValue?) { if (value != null) entries[this] = value }
    /**
     * No-op: TOML has no null type. Provided for source compatibility with JSON/YAML DSLs.
     * @since 3.11.0
     */
    @JvmName("toNullable")
    infix fun String.to(value: Nothing?) { /* TOML has no null */ }

    /**
     * Associates the current string (key) with an inline TOML array of strings.
     * @since 3.11.0
     */
    @JvmName("toIterableString")
    infix fun String.to(array: Iterable<String>) { entries[this] = TomlArray(array.map { TomlString(it) }) }
    /**
     * Associates the current string (key) with an inline TOML array of numbers.
     * @since 3.11.0
     */
    @JvmName("toIterableNumber")
    infix fun String.to(array: Iterable<Number>) { entries[this] = TomlArray(array.map { TomlNumber(it) }) }
    /**
     * Associates the current string (key) with an inline TOML array of booleans.
     * @since 3.11.0
     */
    @JvmName("toIterableBoolean")
    infix fun String.to(array: Iterable<Boolean>) { entries[this] = TomlArray(array.map { TomlBoolean(it) }) }
    /**
     * Associates the current string (key) with an array of arbitrary [TomlValue]s.
     * If every element is a [TomlObject], the result is rendered as an array of
     * tables (`[[header]]`); otherwise it is rendered inline.
     * @since 3.11.0
     */
    @JvmName("toIterableTomlValue")
    infix fun String.to(array: Iterable<TomlValue>) { entries[this] = TomlArray(array.toList()) }

    /**
     * Builds and returns a new [TomlObject] using the key-value pairs collected
     * by this builder. The resulting object inherits the inline flag of the builder,
     * which controls whether nested rendering uses `{...}` or `[header]` syntax.
     * @since 3.11.0
     */
    fun build(): TomlObject = TomlObject(entries).also { it.inline = inline }
}

// --- ARRAY BUILDER ---

/**
 * A builder for constructing TOML arrays in a DSL-friendly manner.
 *
 * This class provides utility functions to construct TOML arrays by appending
 * various types of TOML-compatible values. When every appended element is a
 * [TomlObject], the resulting array is automatically rendered as an array of
 * tables at document level.
 *
 * @since 3.11.0
 */
@TomlDslMarker
class TomlArrayBuilder {
    @PublishedApi
    internal val elements = emptyMList<TomlValue>()

    /** Appends the string as a [TomlString].
     * @since 3.11.0
     */
    operator fun String.unaryPlus() { elements += TomlString(this) }
    /** Appends the number as a [TomlNumber].
     * @since 3.11.0
     */
    operator fun Number.unaryPlus() { elements += TomlNumber(this) }
    /** Appends the boolean as a [TomlBoolean].
     * @since 3.11.0
     */
    operator fun Boolean.unaryPlus() { elements += TomlBoolean(this) }
    /** Appends a [TomlValue] directly.
     * @since 3.11.0
     */
    operator fun TomlValue.unaryPlus() { elements += this }

    /**
     * Adds a new value to the array by converting it to a TOML-compatible form.
     * Throws if the value is null, since TOML arrays cannot contain null elements.
     * @since 3.11.0
     */
    fun add(value: Any?) { elements += value.toTomlValue() }

    /**
     * Builds and returns a [TomlArray] containing all the elements added.
     * @since 3.11.0
     */
    fun build(): TomlArray = TomlArray(elements.toList())
}

// --- TOP-LEVEL ENTRY POINTS ---

/**
 * Builds a complete TOML document using the provided DSL block.
 *
 * The block is applied to a [TomlObjectBuilder] representing the document's
 * root table. Nested tables are rendered as `[header]` sections, and arrays
 * of tables as `[[header]]` sections.
 *
 * @param block A DSL block defining the structure of the TOML document.
 * @return A [Toml] object representing the complete TOML document.
 * @since 3.11.0
 */
fun buildToml(block: ReceiverConsumer<TomlObjectBuilder>): Toml =
    TomlObjectBuilder().apply(block).build().toToml()

/**
 * Initializes and returns a [TomlObject] using the specified DSL block, without
 * rendering it to a string. Useful when the table will be embedded into a larger
 * structure or further manipulated.
 * @since 3.11.0
 */
fun initToml(block: ReceiverConsumer<TomlObjectBuilder>) =
    TomlObjectBuilder().apply(block).build()

/**
 * Creates a TOML table using the provided block. By default the table is rendered
 * as a `[header]` section when nested. Pass `inline = true` to render it as an
 * inline table `{ ... }` instead.
 *
 * @param inline When `true`, the resulting table is rendered inline at document level.
 * @param block A lambda with a [TomlObjectBuilder] receiver defining the table contents.
 * @return The constructed [TomlObject].
 * @since 3.11.0
 */
fun tomlObject(inline: Boolean = false, block: ReceiverConsumer<TomlObjectBuilder>) =
    TomlObjectBuilder(inline).apply(block).build()

/**
 * Creates a TOML array using the provided DSL block.
 * @since 3.11.0
 */
fun tomlArray(block: ReceiverConsumer<TomlArrayBuilder>) =
    TomlArrayBuilder().apply(block).build()

/**
 * Initializes and returns a configured [TomlArrayBuilder] for further manipulation.
 * @since 3.11.0
 */
fun initTomlArray(block: ReceiverConsumer<TomlArrayBuilder>) =
    TomlArrayBuilder().apply(block)

/**
 * Creates a nested table inside the current [TomlObjectBuilder]. By default, the
 * nested table is rendered as a `[parent.child]` section header. Pass `inline = true`
 * for an inline `{ ... }` table.
 *
 * @since 3.11.0
 */
fun TomlObjectBuilder.obj(inline: Boolean = false, block: ReceiverConsumer<TomlObjectBuilder>): TomlObject =
    tomlObject(inline, block)

/**
 * Adds an array to the current [TomlObjectBuilder] using a DSL block.
 * If every element of the array is a [TomlObject], the array is rendered
 * as an array of tables (`[[header]]`).
 *
 * @since 3.11.0
 */
fun TomlObjectBuilder.array(block: ReceiverConsumer<TomlArrayBuilder>): TomlArray =
    tomlArray(block)

/**
 * Convenience builder for an array of tables. Each call to [TomlArrayOfTablesBuilder.item]
 * within the block adds one table to the resulting array.
 *
 * @since 3.11.0
 */
fun TomlObjectBuilder.arrayOfTables(block: ReceiverConsumer<TomlArrayOfTablesBuilder>): TomlArray =
    TomlArrayOfTablesBuilder().apply(block).build()

/**
 * Builder dedicated to producing arrays of tables (`[[header]]`).
 *
 * Each [item] call appends one [TomlObject] entry. The resulting [TomlArray] is
 * always rendered as an array of tables when used at document level, since every
 * element is a non-inline table.
 *
 * @since 3.11.0
 */
@TomlDslMarker
class TomlArrayOfTablesBuilder {
    @PublishedApi
    internal val tables = emptyMList<TomlObject>()

    /** Adds one table to the array of tables.
     * @since 3.11.0
     */
    fun item(block: ReceiverConsumer<TomlObjectBuilder>) {
        tables += TomlObjectBuilder().apply(block).build()
    }

    /** Builds the resulting [TomlArray].
     * @since 3.11.0
     */
    fun build(): TomlArray = TomlArray(tables.toList())
}