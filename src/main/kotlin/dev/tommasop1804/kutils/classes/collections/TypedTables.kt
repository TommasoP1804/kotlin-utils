/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("TypedTablesKt")
@file:Suppress("unused")
@file:Since("5.5.0")

package dev.tommasop1804.kutils.classes.collections

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass

/**
 * A column of a [TypedTable]: a key that carries, at compile time and at runtime, the type of the values
 * stored under it.
 *
 * Columns are meant to be declared once and reused as keys, so that every read and write on a table is
 * checked against the declared type.
 *
 * @param T the type of the values stored in this column.
 * @property name the name of the column.
 * @property type the runtime type of the values, used for checked casts.
 * @since 5.5.0
 */
class TypedColumn<T : Any> @PublishedApi internal constructor(val name: String, val type: KClass<T>) {
    /**
     * Casts [value] to [T] if it is an instance of [type], returning `null` otherwise.
     *
     * @param value the value to cast, may be null.
     * @return the value typed as [T], or `null` if it is null or of another type.
     * @since 5.5.0
     */
    @Suppress("unchecked_cast")
    fun cast(value: Any?): T? = if (type.isInstance(value)) value as T else null

    override fun toString(): String = "$name: ${type.simpleName}"
    override fun equals(other: Any?): Boolean = other is TypedColumn<*> && name == other.name && type == other.type
    override fun hashCode(): Int = 31 * name.hashCode() + type.hashCode()
}

/**
 * Creates a [TypedColumn] of type [T] with the given [name].
 *
 * @since 5.5.0
 */
inline fun <reified T : Any> column(name: String): TypedColumn<T> = TypedColumn(name, T::class)

/**
 * Creates a [TypedColumn] of type [T] whose name is taken from the property it is delegated to.
 *
 * ```
 * object Person {
 *     val name by column<String>()
 *     val age by column<Int>()
 * }
 * ```
 *
 * @since 5.5.0
 */
inline fun <reified T : Any> column(): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, TypedColumn<T>>> =
    PropertyDelegateProvider { _, property ->
        val declared = column<T>(property.name)
        ReadOnlyProperty { _, _ -> declared }
    }

/**
 * A single typed cell: a row key, a [TypedColumn] and a value whose type is the one declared by the column.
 *
 * @param R the type of the row key.
 * @param T the type of the value.
 * @since 5.5.0
 */
data class TypedCell<R, T : Any>(val rowKey: R, val column: TypedColumn<T>, val value: T?) {
    /**
     * Converts this cell into a [Triple] of row key, column and value.
     *
     * @since 5.5.0
     */
    fun toTriple(): Triple<R, TypedColumn<T>, T?> = Triple(rowKey, column, value)
}

/**
 * A row of a [TypedTable]: an ordered set of values, each one reachable only through its own [TypedColumn].
 *
 * @param R the type of the row key.
 * @since 5.5.0
 */
class TypedRow<R> @PublishedApi internal constructor(
    val key: R,
    @PublishedApi internal val values: Map<TypedColumn<*>, Any?>
) : Iterable<TypedCell<R, Any>> {

    /**
     * The columns present in this row, in insertion order.
     *
     * @since 5.5.0
     */
    val columns: List<TypedColumn<*>> get() = values.keys.toList()

    /**
     * The number of cells in this row.
     *
     * @since 5.5.0
     */
    val size: Int get() = values.size

    /**
     * Whether this row holds no cells.
     *
     * @since 5.5.0
     */
    val isEmpty: Boolean get() = values.isEmpty()

    /**
     * Returns the value stored under [column], typed as [T].
     *
     * @since 5.5.0
     */
    operator fun <T : Any> get(column: TypedColumn<T>): T? = column.cast(values[column])

    /**
     * Infix alias of [get].
     *
     * @since 5.5.0
     */
    infix fun <T : Any> at(column: TypedColumn<T>): T? = get(column)

    /**
     * Returns the value stored under [column], or [default] when absent or null.
     *
     * @since 5.5.0
     */
    fun <T : Any> getOrDefault(column: TypedColumn<T>, default: T): T = get(column) ?: default

    /**
     * Returns the value stored under [column], or the result of [default] when absent or null.
     *
     * @since 5.5.0
     */
    fun <T : Any> getOrElse(column: TypedColumn<T>, default: Transformer<TypedColumn<T>, T>): T = get(column) ?: default(column)

    /**
     * Returns the value stored under [column], throwing when absent or null.
     *
     * @throws NoSuchElementException if the column is missing or its value is null.
     * @since 5.5.0
     */
    fun <T : Any> getOrThrow(column: TypedColumn<T>, lazyException: ThrowableSupplier = { NoSuchElementException("Column '${column.name}' is missing or null in row '$key'") }): T =
        get(column) ?: throw lazyException()

    /**
     * Whether this row holds a cell for [column].
     *
     * @since 5.5.0
     */
    operator fun contains(column: TypedColumn<*>): Boolean = column in values

    /**
     * Returns a copy of this row with `value` set on `column`.
     *
     * @since 5.5.0
     */
    operator fun <T : Any> plus(cell: TypedCell<R, T>): TypedRow<R> =
        TypedRow(key, values + (cell.column to cell.value))

    /**
     * Returns a copy of this row without [column].
     *
     * @since 5.5.0
     */
    operator fun minus(column: TypedColumn<*>): TypedRow<R> = TypedRow(key, values - column)

    /**
     * Returns this row as a map of column name to value.
     *
     * @since 5.5.0
     */
    fun toMap(): Map<String, Any?> = values.mapKeys { [column, _] -> column.name }

    @Suppress("unchecked_cast")
    override fun iterator(): Iterator<TypedCell<R, Any>> =
        values.map { [column, value] -> TypedCell(key, column as TypedColumn<Any>, value) }.iterator()

    override fun equals(other: Any?): Boolean = other is TypedRow<*> && key == other.key && values == other.values
    override fun hashCode(): Int = 31 * key.hashCode() + values.hashCode()
    override fun toString(): String = "TypedRow($key) ${toMap()}"
}

/**
 * An immutable table whose columns each declare their own value type: a heterogeneous collection where the
 * type of every value is known from the [TypedColumn] used to reach it.
 *
 * @param R the type of the row key.
 * @since 5.5.0
 */
class TypedTable<R> @PublishedApi internal constructor(
    @PublishedApi internal val data: Map<R, Map<TypedColumn<*>, Any?>>
) : Iterable<TypedRow<R>> {

    /**
     * The keys of every row, in insertion order.
     *
     * @since 5.5.0
     */
    val rowKeys: Set<R> get() = data.keys

    /**
     * Every distinct column appearing in at least one row.
     *
     * @since 5.5.0
     */
    val columns: Columns<*> get() = data.values.flatMap { it.keys }.distinct()

    /**
     * The rows of this table, in insertion order.
     *
     * @since 5.5.0
     */
    val rows: Rows<R> get() = data.map { [key, values] -> TypedRow(key, values) }

    /**
     * The number of rows.
     *
     * @since 5.5.0
     */
    val size: Int get() = data.size

    /**
     * Whether this table holds no rows.
     *
     * @since 5.5.0
     */
    val isEmpty: Boolean get() = data.isEmpty()

    companion object {
        /**
         * Represents a list of [TypedRow] objects associated with a [TypedTable].
         *
         * @param R the type of the row key.
         * @since 5.5.0
         */
        typealias Rows<R> = List<TypedRow<R>>
        /**
         * Represents a list of columns, where each column is associated with a specific type.
         *
         * This alias is used to simplify handling collections of `TypedColumn` objects in a `TypedTable`.
         *
         * @param C The type parameter associated with the table or data structure where these columns are being used.
         * @since 5.5.0
         */
        typealias Columns<C> = List<TypedColumn<C>>
    }

    /**
     * Returns the value stored at [rowKey] under [column], typed as [T].
     *
     * @since 5.5.0
     */
    operator fun <T : Any> get(rowKey: R, column: TypedColumn<T>): T? = column.cast(data[rowKey]?.get(column))

    /**
     * Returns the row identified by [rowKey], or `null` when absent.
     *
     * @since 5.5.0
     */
    operator fun get(rowKey: R): TypedRow<R>? = data[rowKey]?.let { TypedRow(rowKey, it) }

    /**
     * Returns the value stored at [rowKey] under [column], throwing when absent or null.
     *
     * @throws NoSuchElementException if the row or the column is missing, or the value is null.
     * @since 5.5.0
     */
    fun <T : Any> getOrThrow(rowKey: R, column: TypedColumn<T>, lazyException: ThrowableSupplier = { NoSuchElementException("Column '${column.name}' is missing or null in row '$rowKey'") }): T =
        get(rowKey, column) ?: throw lazyException()

    /**
     * Whether this table holds a row identified by [rowKey].
     *
     * @since 5.5.0
     */
    operator fun contains(rowKey: R): Boolean = rowKey in data

    /**
     * Returns the whole [column] as a map of row key to typed value, skipping rows where it is missing or null.
     *
     * @since 5.5.0
     */
    infix fun <T : Any> columnOf(column: TypedColumn<T>): Map<R, T> =
        data.mapNotNull { [key, values] -> column.cast(values[column])?.let { key to it } }.toMap()

    /**
     * Returns the values of [column] across all rows, skipping the missing and null ones.
     *
     * @since 5.5.0
     */
    infix fun <T : Any> valuesOf(column: TypedColumn<T>): List<T> = data.values.mapNotNull { column.cast(it[column]) }

    /**
     * Returns a copy of this table with [row] added or replaced.
     *
     * @since 5.5.0
     */
    operator fun plus(row: TypedRow<R>): TypedTable<R> = TypedTable(data + (row.key to row.values))

    /**
     * Returns a copy of this table merged with [other], where rows of [other] win on conflicting keys.
     *
     * @since 5.5.0
     */
    operator fun plus(other: TypedTable<R>): TypedTable<R> = TypedTable(data + other.data)

    /**
     * Returns a copy of this table without the row identified by [rowKey].
     *
     * @since 5.5.0
     */
    operator fun minus(rowKey: R): TypedTable<R> = TypedTable(data - rowKey)

    /**
     * Returns the rows matching [predicate] as a new table.
     *
     * @since 5.5.0
     */
    fun filterRows(predicate: (TypedRow<R>) -> Boolean): TypedTable<R> =
        TypedTable(data.filter { [key, values] -> predicate(TypedRow(key, values)) })

    /**
     * Maps every row through [transform].
     *
     * @since 5.5.0
     */
    fun <O> mapRows(transform: (TypedRow<R>) -> O): List<O> = rows.map(transform)

    /**
     * Sorts the rows of this table by the values of [column], keeping rows without a value at the end.
     *
     * @since 5.5.0
     */
    fun <T : Comparable<T>> sortedBy(column: TypedColumn<T>): TypedTable<R> =
        TypedTable(data.entries.sortedBy { [_, values] -> column.cast(values[column]) }.associate { it.key to it.value })

    /**
     * Flattens this table into its cells.
     *
     * @since 5.5.0
     */
    fun toCells(): List<TypedCell<R, Any>> = rows.flatten()

    /**
     * Returns a mutable copy of this table.
     *
     * @since 5.5.0
     */
    fun toMTypedTable(): MTypedTable<R> =
        MTypedTable(data.mapValuesTo(LinkedHashMap()) { [_, values] -> LinkedHashMap(values) })

    override fun iterator(): Iterator<TypedRow<R>> = rows.iterator()

    override fun equals(other: Any?): Boolean = other is TypedTable<*> && data == other.data
    override fun hashCode(): Int = data.hashCode()
    override fun toString(): String = rows.joinToString(separator = ",\n  ", prefix = "TypedTable[\n  ", postfix = "\n]")
}

/**
 * A mutable [TypedTable].
 *
 * @param R the type of the row key.
 * @since 5.5.0
 */
class MTypedTable<R> @PublishedApi internal constructor(
    private val data: MutableMap<R, MutableMap<TypedColumn<*>, Any?>>
) : Iterable<TypedRow<R>> {

    /**
     * Creates an empty mutable typed table.
     *
     * @since 5.5.0
     */
    constructor() : this(LinkedHashMap())

    /**
     * The keys of every row, in insertion order.
     *
     * @since 5.5.0
     */
    val rowKeys: Set<R> get() = data.keys

    /**
     * Every distinct column appearing in at least one row.
     *
     * @since 5.5.0
     */
    val columns: TypedTable.Companion.Columns<*> get() = data.values.flatMap { it.keys }.distinct()

    /**
     * The rows of this table, in insertion order.
     *
     * @since 5.5.0
     */
    val rows: TypedTable.Companion.Rows<R> get() = data.map { [key, values] -> TypedRow(key, values) }

    /**
     * The number of rows.
     *
     * @since 5.5.0
     */
    val size: Int get() = data.size

    /**
     * Whether this table holds no rows.
     *
     * @since 5.5.0
     */
    val isEmpty: Boolean get() = data.isEmpty()

    /**
     * Returns the value stored at [rowKey] under [column], typed as [T].
     *
     * @since 5.5.0
     */
    operator fun <T : Any> get(rowKey: R, column: TypedColumn<T>): T? = column.cast(data[rowKey]?.get(column))

    /**
     * Returns the row identified by [rowKey], or `null` when absent.
     *
     * @since 5.5.0
     */
    operator fun get(rowKey: R): TypedRow<R>? = data[rowKey]?.let { TypedRow(rowKey, it) }

    /**
     * Sets the value of [column] on the row identified by [rowKey], creating the row when needed.
     *
     * @since 5.5.0
     */
    operator fun <T : Any> set(rowKey: R, column: TypedColumn<T>, value: T?) {
        data.getOrPut(rowKey) { LinkedHashMap() }[column] = value
    }

    /**
     * Adds or replaces [row].
     *
     * @since 5.5.0
     */
    operator fun plusAssign(row: TypedRow<R>) {
        data[row.key] = LinkedHashMap(row.values)
    }

    /**
     * Removes the row identified by [rowKey].
     *
     * @since 5.5.0
     */
    operator fun minusAssign(rowKey: R) {
        data.remove(rowKey)
    }

    /**
     * Removes [column] from the row identified by [rowKey], returning the removed value.
     *
     * @since 5.5.0
     */
    fun <T : Any> remove(rowKey: R, column: TypedColumn<T>): T? = column.cast(data[rowKey]?.remove(column))

    /**
     * Removes every row.
     *
     * @since 5.5.0
     */
    fun clear() = data.clear()

    /**
     * Whether this table holds a row identified by [rowKey].
     *
     * @since 5.5.0
     */
    operator fun contains(rowKey: R): Boolean = rowKey in data

    /**
     * Returns an immutable copy of this table.
     *
     * @since 5.5.0
     */
    fun toTypedTable(): TypedTable<R> = TypedTable(data.mapValues { [_, values] -> values.toMap() })

    override fun iterator(): Iterator<TypedRow<R>> = rows.iterator()

    override fun equals(other: Any?): Boolean = other is MTypedTable<*> && data == other.data
    override fun hashCode(): Int = data.hashCode()
    override fun toString(): String = toTypedTable().toString()
}

/**
 * Marker of the [TypedTable] building DSL.
 *
 * @since 5.5.0
 */
@DslMarker
annotation class TypedTableDsl

/**
 * Builder of a single row: only values matching each column's declared type are accepted.
 *
 * @since 5.5.0
 */
@TypedTableDsl
class TypedRowBuilder internal constructor(private val cells: MutableMap<TypedColumn<*>, Any?>) {
    /**
     * Associates [value] to this column.
     *
     * @since 5.5.0
     */
    infix fun <T : Any> TypedColumn<T>.to(value: T?) {
        cells[this] = value
    }

    /**
     * Associates [value] to this column.
     *
     * @since 5.5.0
     */
    operator fun <T : Any> TypedColumn<T>.invoke(value: T?) {
        cells[this] = value
    }

    /**
     * Removes this column from the row being built.
     *
     * @since 5.5.0
     */
    operator fun TypedColumn<*>.unaryMinus() {
        cells.remove(this)
    }
}

/**
 * Builder used by [typedTableOf].
 *
 * @param R the type of the row key.
 * @since 5.5.0
 */
@TypedTableDsl
class TypedTableBuilder<R> internal constructor() {
    private val rows = LinkedHashMap<R, MutableMap<TypedColumn<*>, Any?>>()

    /**
     * The number of rows declared so far.
     *
     * @since 5.5.0
     */
    val rowCount: Int get() = rows.size

    /**
     * Declares, or extends, the row identified by [key].
     *
     * @since 5.5.0
     */
    fun row(key: R, build: TypedRowBuilder.() -> Unit) {
        TypedRowBuilder(rows.getOrPut(key) { LinkedHashMap() }).build()
    }

    /**
     * Adds [row] to the table being built.
     *
     * @since 5.5.0
     */
    operator fun TypedRow<R>.unaryPlus() {
        rows[key] = LinkedHashMap(values)
    }

    internal fun build(): TypedTable<R> = TypedTable(rows.mapValues { [_, values] -> values.toMap() })
}

/**
 * Builds a [TypedTable] through the typed DSL.
 *
 * ```
 * val table = typedTableOf<String> {
 *     row("tommaso") {
 *         Person.name to "Tommaso"
 *         Person.age to 27
 *     }
 * }
 * ```
 *
 * @since 5.5.0
 */
fun <R> typedTableOf(build: TypedTableBuilder<R>.() -> Unit): TypedTable<R> =
    TypedTableBuilder<R>().apply(build).build()

/**
 * Builds a [TypedTable] out of the given [rows].
 *
 * @since 5.5.0
 */
fun <R> typedTableOf(vararg rows: TypedRow<R>): TypedTable<R> = typedTableOf { rows.forEach { +it } }

/**
 * Declares a row using the current row count as its key.
 *
 * @since 5.5.0
 */
fun TypedTableBuilder<Int>.row(build: ReceiverConsumer<TypedRowBuilder>) = row(rowCount, build)

/**
 * Builds a standalone [TypedRow] identified by [key].
 *
 * @since 5.5.0
 */
fun <R> typedRowOf(key: R, build: ReceiverConsumer<TypedRowBuilder>): TypedRow<R> =
    TypedRow(key, LinkedHashMap<TypedColumn<*>, Any?>().also { TypedRowBuilder(it).build() })

/**
 * Builds a [MTypedTable] through the given [build] block.
 *
 * @since 5.5.0
 */
fun <R> mTypedTableOf(build: ReceiverConsumer<MTypedTable<R>>): MTypedTable<R> = MTypedTable<R>().apply(build)

/**
 * Returns an empty [TypedTable].
 *
 * @since 5.5.0
 */
fun <R> emptyTypedTable(): TypedTable<R> = TypedTable(emptyMap())

/**
 * Returns an empty [MTypedTable].
 *
 * @since 5.5.0
 */
fun <R> emptyMTypedTable(): MTypedTable<R> = MTypedTable()

/**
 * Returns this table when not null, an empty one otherwise.
 *
 * @since 5.5.0
 */
fun <R> TypedTable<R>?.orEmpty(): TypedTable<R> = this ?: emptyTypedTable()

/**
 * Returns `null` when this table is empty, the table itself otherwise.
 *
 * @since 5.5.0
 */
fun <R> TypedTable<R>.orNullIfEmpty(): TypedTable<R>? = if (isEmpty) null else this

/**
 * Returns this mutable table when not null, an empty one otherwise.
 *
 * @since 5.5.0
 */
fun <R> MTypedTable<R>?.orEmpty(): MTypedTable<R> = this ?: emptyMTypedTable()

/**
 * Converts a collection of typed rows into a [TypedTable].
 *
 * @since 5.5.0
 */
@JvmName("typedRowsToTypedTable")
fun <R> Collection<TypedRow<R>>.toTypedTable(): TypedTable<R> = typedTableOf { forEach { +it } }

/**
 * Converts a collection of typed cells into a [TypedTable], grouping them by row key.
 *
 * @since 5.5.0
 */
@JvmName("typedCellsToTypedTable")
fun <R> Collection<TypedCell<R, *>>.toTypedTable(): TypedTable<R> = TypedTable(
    groupBy { it.rowKey }.mapValues { [_, cells] -> cells.associate { it.column to it.value } }
)