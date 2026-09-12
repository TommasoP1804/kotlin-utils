/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("TypedTablesJdbcKt")
@file:Suppress("unused")
@file:Since("5.5.0")

package dev.tommasop1804.kutils.classes.collections

import dev.tommasop1804.kutils.annotations.*
import java.math.BigDecimal
import java.sql.ResultSet
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import kotlin.reflect.KClass
import kotlin.uuid.Uuid

/**
 * An immutable set of conversions from raw, untyped values (typically the ones a JDBC driver hands back)
 * to the types declared by a [TypedColumn].
 *
 * Instances are values: [with] returns a new set rather than mutating the current one.
 *
 * @since 5.5.0
 */
class RawReaders @PublishedApi internal constructor(
    @PublishedApi internal val readers: Map<KClass<*>, (Any) -> Any?>
) {
    /**
     * Reads [raw] as the type declared by [column]: returns it as-is when it already matches, converts it
     * through the registered reader otherwise, and falls back on enum constants by name.
     *
     * @return the typed value, or `null` when [raw] is null or no conversion applies.
     * @since 5.5.0
     */
    fun <T : Any> read(column: TypedColumn<T>, raw: Any?): T? = raw?.let { value ->
        column.cast(value)
            ?: column.cast(readers[column.type]?.invoke(value))
            ?: column.cast(column.type.java.enumConstants?.firstOrNull { (it as Enum<*>).name == value.toString().trim() })
    }

    companion object {
        /**
         * The conversions applied when none is specified: the common JDBC types plus the `java.time` ones,
         * tolerant to values handed back as strings.
         *
         * @since 5.5.0
         */
        val DEFAULT: RawReaders = RawReaders(
            mapOf(
                String::class to { raw: Any -> raw.toString() },
                Boolean::class to { raw: Any -> raw.toString().trim().lowercase() in setOf("true", "t", "yes", "y", "1") },
                Int::class to { raw: Any -> (raw as? Number)?.toInt() ?: raw.toString().trim().toIntOrNull() },
                Long::class to { raw: Any -> (raw as? Number)?.toLong() ?: raw.toString().trim().toLongOrNull() },
                Short::class to { raw: Any -> (raw as? Number)?.toShort() ?: raw.toString().trim().toShortOrNull() },
                Double::class to { raw: Any -> (raw as? Number)?.toDouble() ?: raw.toString().trim().toDoubleOrNull() },
                Float::class to { raw: Any -> (raw as? Number)?.toFloat() ?: raw.toString().trim().toFloatOrNull() },
                BigDecimal::class to { raw: Any -> raw.toString().trim().toBigDecimalOrNull() },
                UUID::class to { raw: Any -> runCatching { UUID.fromString(raw.toString().trim()) }.getOrNull() },
                Uuid::class to { raw: Any -> runCatching { Uuid.parse(raw.toString().trim()) }.getOrNull() },
                Instant::class to { raw: Any -> raw.asOffsetDateTime().toInstant() },
                OffsetDateTime::class to { raw: Any -> raw.asOffsetDateTime() },
                LocalDateTime::class to { raw: Any -> raw.asOffsetDateTime().toLocalDateTime() },
                LocalDate::class to { raw: Any -> raw.asOffsetDateTime().toLocalDate() },
                LocalTime::class to { raw: Any -> raw.asOffsetDateTime().toLocalTime() }
            )
        )
    }
}

/**
 * Returns a copy of these readers with [reader] registered for [T], replacing any previous one.
 *
 * ```
 * val readers = RawReaders.Default.with<Money> { Money.parse(it.toString()) }
 * ```
 *
 * @since 5.5.0
 */
inline fun <reified T : Any> RawReaders.with(noinline reader: (Any) -> T?): RawReaders =
    RawReaders(readers + (T::class to reader))

private val TIMESTAMP: DateTimeFormatter = DateTimeFormatterBuilder()
    .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    .optionalStart().appendPattern("[XXX][XX][X]").optionalEnd()
    .toFormatter()

/**
 * Converts a raw temporal value into an [OffsetDateTime], assuming [zone] when the source carries no offset.
 *
 * Handles the `java.time` and `java.sql` types, plus textual values in ISO form or in the
 * `yyyy-MM-dd HH:mm:ss[.SSS][+HH]` form some drivers hand back.
 *
 * @since 5.5.0
 */
fun Any.asOffsetDateTime(zone: ZoneOffset = ZoneOffset.UTC): OffsetDateTime = when (this) {
    is OffsetDateTime -> this
    is ZonedDateTime -> toOffsetDateTime()
    is Instant -> atOffset(zone)
    is LocalDateTime -> atOffset(zone)
    is LocalDate -> atStartOfDay().atOffset(zone)
    is java.sql.Timestamp -> toInstant().atOffset(zone)
    is java.sql.Date -> toLocalDate().atStartOfDay().atOffset(zone)
    is Date -> toInstant().atOffset(zone)
    else -> toString().trim().replace(' ', 'T').let { text ->
        runCatching { OffsetDateTime.parse(text, TIMESTAMP) }.getOrElse { LocalDateTime.parse(text).atOffset(zone) }
    }
}

/**
 * Returns the subset of [columns] whose label actually appears in this result set, so that reading a column
 * absent from the query never throws.
 *
 * @since 5.5.0
 */
private fun ResultSet.presentColumns(
    columns: Array<out TypedColumn<*>>,
    label: (TypedColumn<*>) -> String
): List<TypedColumn<*>> = metaData
    .let { meta -> (1..meta.columnCount).mapTo(HashSet()) { meta.getColumnLabel(it).lowercase() } }
    .let { labels -> columns.filter { label(it).lowercase() in labels } }

private fun <R, T : Any> MTypedTable<R>.put(rowKey: R, column: TypedColumn<T>, raw: Any?, readers: RawReaders) {
    this[rowKey, column] = readers.read(column, raw)
}

private fun <R> ResultSet.readRow(
    key: R,
    columns: List<TypedColumn<*>>,
    readers: RawReaders,
    label: (TypedColumn<*>) -> String
): TypedRow<R> = emptyMTypedTable<R>()
    .also { table -> columns.forEach { column -> table.put(key, column, getObject(label(column)), readers) } }[key]
    ?: typedRowOf(key) {}

/**
 * Walks this result set lazily, yielding one [TypedRow] per record, keyed by its zero-based index.
 *
 * The sequence consumes the result set as it is iterated, so it can be traversed only once and must be
 * consumed before the statement is closed.
 *
 * @param columns the columns to read; the ones absent from the result set are skipped.
 * @param readers the conversions applied to the raw values.
 * @param label maps a column to the label to look up in the result set, by default the column name.
 * @since 5.5.0
 */
fun ResultSet.typedRowSequence(
    vararg columns: TypedColumn<*>,
    readers: RawReaders = RawReaders.DEFAULT,
    label: (TypedColumn<*>) -> String = { it.name }
): Sequence<TypedRow<Int>> = presentColumns(columns, label).let { present ->
    var index = 0
    generateSequence { if (next()) readRow(index++, present, readers, label) else null }
}

/**
 * Reads this result set into a list of [TypedRow], keyed by zero-based index.
 *
 * @since 5.5.0
 */
fun ResultSet.toTypedRows(
    vararg columns: TypedColumn<*>,
    readers: RawReaders = RawReaders.DEFAULT,
    label: (TypedColumn<*>) -> String = { it.name }
): List<TypedRow<Int>> = typedRowSequence(columns = columns, readers = readers, label = label).toList()

/**
 * Reads this result set into a [TypedTable] keyed by zero-based row index.
 *
 * @since 5.5.0
 */
fun ResultSet.toTypedTable(
    vararg columns: TypedColumn<*>,
    readers: RawReaders = RawReaders.DEFAULT,
    label: (TypedColumn<*>) -> String = { it.name }
): TypedTable<Int> = emptyMTypedTable<Int>()
    .also { table -> typedRowSequence(columns = columns, readers = readers, label = label).forEach { table += it } }
    .toTypedTable()

/**
 * Reads this result set into a [TypedTable] keyed by the value of [key], which must be present and non-null
 * on every record.
 *
 * @throws NoSuchElementException if a record carries no value for [key].
 * @since 5.5.0
 */
fun <K : Any> ResultSet.toTypedTableBy(
    key: TypedColumn<K>,
    vararg columns: TypedColumn<*>,
    readers: RawReaders = RawReaders.DEFAULT,
    label: (TypedColumn<*>) -> String = { it.name }
): TypedTable<K> = emptyMTypedTable<K>()
    .also { table ->
        val all = arrayOf(key, *columns)
        val present = presentColumns(all, label)
        while (next()) {
            val rowKey = readers.read(key, getObject(label(key)))
                ?: throw NoSuchElementException("Column '${label(key)}' is missing or null, cannot be used as row key")
            present.forEach { column -> table.put(rowKey, column, getObject(label(column)), readers) }
        }
    }
    .toTypedTable()

/**
 * Maps every record of this result set through [transform], reading only the given [columns].
 *
 * @since 5.5.0
 */
fun <T> ResultSet.mapTypedRows(
    vararg columns: TypedColumn<*>,
    readers: RawReaders = RawReaders.DEFAULT,
    label: (TypedColumn<*>) -> String = { it.name },
    transform: (TypedRow<Int>) -> T
): List<T> = typedRowSequence(columns = columns, readers = readers, label = label).map(transform).toList()