@file:JvmName("TablesKt")
@file:Suppress("unused", "localVariableName", "kutils_collection_declaration", "kutils_map_declaration")
@file:Since("1.0.0")

package dev.tommasop1804.kutils.classes.collections

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.Since
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.node.ObjectNode
import java.io.Serial
import java.io.Serializable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.NoSuchPropertyException
import kotlin.reflect.full.memberProperties

/**
 * Represents a generic interface for a cell structure that holds a row key, column key,
 * and a value. This can be used in various tabular or grid-based data-structures.
 *
 * @param R The type of the row key.
 * @param C The type of the column key.
 * @param V The type of the value stored in the cell. Nullable to allow empty cells.
 * @since 1.0.0
 */
interface CellInterface<R, C, V> : Serializable {
    /**
     * Represents the unique identifier for a specific row in a collection or data structure.
     * This is commonly used to differentiate and operate on individual rows within a dataset.
     *
     * @since 1.0.0
     */
    val rowKey: R
    /**
     * Represents a key or identifier for a column, typically used to reference
     * a specific column in a table or a dataset.
     *
     * This property is designed to act as a unique identifier or key for
     * distinguishing columns within a data structure when performing
     * operations like sorting, filtering, or mapping data.
     *
     * @since 1.0.0
     */
    val columnKey: C
    /**
     * A nullable variable representing a value of type `V`.
     * This variable can either hold a value of type `V` or be null.
     *
     * @since 1.0.0
     */
    val value: V?

    /**
     * Converts the current object into a map representation.
     *
     * This method constructs a map with the keys "rowKey", "columnKey", and "value",
     * assigning their corresponding properties from the object as values.
     *
     * @return a map containing the row key, column key, and value of the current object.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf("rowKey" to rowKey, "columnKey" to columnKey, "value" to value)

    /**
     * Provides the value associated with the specified property name in the underlying map implementation.
     * The value is cast to the expected return type.
     *
     * @param thisRef the reference to the object for which the property is retrieved. This parameter is not used in this implementation.
     * @param property the metadata of the property whose associated value is retrieved from the map.
     * @return the value associated with the property name, cast to the expected type.
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>): R = _toMap().getValue(property.name) as R

    /**
     * Converts the current object representing a cell into a [Triple] containing the row key, column key,
     * and value of the cell.
     *
     * @return a [Triple] containing the row key, column key, and value of the cell, where the value may be null.
     * @since 1.0.0
     */
    fun toTriple(): Triple<R, C, V?> = Triple(rowKey, columnKey, value)

    /**
     * Compares the keys of the current cell with the keys of the provided cell.
     *
     * This method checks if both the rowKey and columnKey of the current cell are the same as those of the given cell.
     *
     * @param other the cell to compare with the current cell
     * @return true if both the rowKey and columnKey are identical between the two cells, false otherwise
     * @since 1.0.0
     */
    fun sameKeys(other: CellInterface<R, C, *>): Boolean = rowKey == other.rowKey && columnKey == other.columnKey

    /**
     * Converts the current object into a Cell instance with the specified row key, column key, and value.
     *
     * @return a Cell containing the row key, column key, and value of the current object.
     * @since 1.0.0
     */
    fun toCell(): Cell<R, C, V?> = Cell(rowKey, columnKey, value)

    /**
     * Converts the current immutable cell instance into a mutable cell.
     *
     * A mutable cell allows for changes to the value, row key, or column key
     * after its creation, whereas an immutable cell keeps these properties fixed.
     *
     * @return a new instance of MutableCell with the current row key, column key,
     * and value from this immutable cell.
     * @since 1.0.0
     */
    fun toMCell(): MCell<R, C, V?> = MCell(rowKey, columnKey, value)
}

/**
 * Converts this [Triple] into a [Cell] object.
 *
 * The [Triple] represents the row key, column key, and value of a cell.
 * This function creates a corresponding [Cell] object using the components of the [Triple].
 *
 * @return A [Cell] object containing the row key, column key, and value from the [Triple].
 * @since 1.0.0
 */
fun <R, C, V> Triple<R, C, V?>.toCell(): Cell<R, C, V?> = Cell(first, second, third)
/**
 * Converts a [Triple] containing a row key, column key, and value into a [MCell].
 *
 * @return a [MCell] instance initialized with the row key, column key, and value from this [Triple].
 * @since 1.0.0
 */
fun <R, C, V> Triple<R, C, V?>.toMCell(): MCell<R, C, V?> = MCell(first, second, third)

/**
 * A generic data structure for representing a cell in a grid or matrix, identified
 * by a row key, a column key, and optionally storing a value.
 *
 * @param R the type of the row key
 * @param C the type of the column key
 * @param V the type of the value
 * @property rowKey the key identifying the row of the cell
 * @property columnKey the key identifying the column of the cell
 * @property value the value stored in the cell, can be null
 * @since 1.0.0
 */
@JsonSerialize(using = Cell.Companion.Serializer::class)
@JsonDeserialize(using = Cell.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Cell.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Cell.Companion.OldDeserializer::class)
data class Cell<R, C, V>(override val rowKey: R, override val columnKey: C, override val value: V?) : CellInterface<R, C, V?>, Serializable {
    /**
     * Secondary constructor that initializes the class using a provided [Triple].
     * This constructor extracts the first, second, and third elements of the Triple
     * to initialize the corresponding properties of the class.
     *
     * @param triple A [Triple] containing the row key, column key, and value to initialize the instance.
     * @since 1.0.0
     */
    constructor(triple: Triple<R, C, V?>) : this(triple.first, triple.second, triple.third)
    /**
     * Secondary constructor that initializes a `Cell` instance with the specified row key and column key,
     * while assigning a `null` value to the cell.
     *
     * @param rowKey The row key associated with the cell.
     * @param columnKey The column key associated with the cell.
     * @since 1.0.0
     */
    constructor(rowKey: R, columnKey: C) : this(rowKey, columnKey, null)
    /**
     * Constructs a `Cell` instance using a provided pair of row and column keys.
     *
     * This constructor initializes the `Cell` object by extracting the first element
     * from the pair as the `rowKey` and the second element as the `columnKey`.
     * The value is initialized as `null`.
     *
     * @param pair the pair consisting of the row key and column key.
     * @since 1.0.0
     */
    constructor(pair: Pair<R, C>) : this(pair.first, pair.second, null)
    /**
     * Constructs a new instance of `Cell` by copying the row key, column key,
     * and value from the provided `CellInterface` instance.
     *
     * @param cell The `CellInterface` instance whose properties are used to
     * initialize this `Cell`.
     * @since 1.0.0
     */
    constructor(cell: CellInterface<R, C, V?>) : this(cell.rowKey, cell.columnKey, cell.value)

    /**
     * Companion object for the `Cell` class. Provides helper classes and utilities for
     * serialization and deserialization of the `Cell` object.
     *
     * @since 1.0.0
     */
    companion object {
        /**
         * A unique identifier for serializable classes used to verify compatibility
         * during the deserialization process. It ensures that a loaded class
         * is compatible with the serialized object.
         *
         * Any modification to the class that affects the serialized form in an
         * incompatible way should result in a change to this value to prevent
         * deserialization errors and maintain version control.
         *
         * @since 1.0.0
         */
        @Serial private const val serialVersionUID = 1L

        class Serializer : ValueSerializer<Cell<Any, Any, Any?>>() {
            override fun serialize(
                value: Cell<Any, Any, Any?>,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                gen.writePOJOProperty("rowKey", value.rowKey)
                gen.writePOJOProperty("columnKey", value.columnKey)
                gen.writePOJOProperty("value", value.value)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Cell<Any, Any, Any?>>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Cell<Any, Any, Any?> {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                return Cell(
                    node.get("rowKey"),
                    node.get("columnKey"),
                    node.get("value")
                )
            }
        }

        class OldSerializer : JsonSerializer<Cell<Any, Any, Any?>>() {
            override fun serialize(value: Cell<Any, Any, Any?>, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeObjectField("rowKey", value.rowKey)
                gen.writeObjectField("columnKey", value.columnKey)
                gen.writeObjectField("value", value.value)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Cell<Any, Any, Any?>>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Cell<Any, Any, Any?> {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                return Cell(
                    node.get("rowKey"),
                    node.get("columnKey"),
                    node.get("value")
                )
            }
        }
    }

    /**
     * Returns a string representation of the Cell object.
     *
     * @return A string containing the rowKey, columnKey, and value of the Cell in the format:
     * "Cell(rowKey=ROW_KEY, columnKey=COLUMN_KEY, value=VALUE)".
     * @since 1.0.0
     */
    override fun toString(): String = "Cell(rowKey=$rowKey, columnKey=$columnKey, value=$value)"

    /**
     * Compares this object with the specified object for equality.
     * Returns true if the given object is of the same type and
     * has identical properties (rowKey, columnKey, and value) as this object.
     *
     * @param other The object to compare this instance with.
     * @return `true` if the specified object is equal to this instance; `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass.superclass != other?.javaClass?.superclass) return false

        other as CellInterface<*, *, *>

        if (rowKey != other.rowKey) return false
        if (columnKey != other.columnKey) return false
        if (value != other.value) return false

        return true
    }

    /**
     * Returns the hash code value for this Cell instance. The hash code is computed
     * based on the `rowKey`, `columnKey`, and `value` properties, ensuring consistent
     * and unique hash codes for instances with the same state.
     *
     * @return the hash code for this cell as an integer.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = rowKey?.hashCode() ?: 0
        result = 31 * result + (columnKey?.hashCode() ?: 0)
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }
}

/**
 * A mutable implementation of a cell structure that holds a row key, column key, and a value.
 * This class allows modification of the contained row key, column key, and value post instantiation.
 *
 * @param R The type of the row key.
 * @param C The type of the column key.
 * @param V The type of the value stored in the cell. Nullable to allow empty cells.
 * @since 1.0.0
 */
@JsonSerialize(using = MCell.Companion.Serializer::class)
@JsonDeserialize(using = MCell.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = MCell.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = MCell.Companion.OldDeserializer::class)
data class MCell<R, C, V>(override var rowKey: R, override var columnKey: C, override var value: V?) : CellInterface<R, C, V?>, Serializable {
    /**
     * Constructs a [MCell] instance using a [Triple] consisting of a row key, column key,
     * and an optional value.
     *
     * The constructor extracts the first, second, and third components of the [Triple] to initialize
     * the row key, column key, and value of the [MCell], respectively.
     *
     * @param triple a [Triple] containing the row key, column key, and an optional value.
     * @since 1.0.0
     */
    constructor(triple: Triple<R, C, V?>) : this(triple.first, triple.second, triple.third)
    /**
     * Constructs a new instance of `MutableCell` with the specified row key, column key, and a null value.
     *
     * This constructor provides a shortcut for initializing a cell when only the row and column keys are known,
     * leaving the value as null. It delegates to the main constructor with the null value.
     *
     * @param rowKey the row key for the cell
     * @param columnKey the column key for the cell
     * @since 1.0.0
     */
    constructor(rowKey: R, columnKey: C) : this(rowKey, columnKey, null)
    /**
     * Constructs a [MCell] instance from a given [Pair].
     *
     * This constructor initializes the [MCell] using the first and second elements
     * of the provided [pair] as the row key and column key, respectively. The value is
     * initialized to `null` by default.
     *
     * @param pair a [Pair] containing the row key (first) and the column key (second)
     * @since 1.0.0
     */
    constructor(pair: Pair<R, C>) : this(pair.first, pair.second, null)
    /**
     * Constructor for creating a [MCell] instance from a [CellInterface].
     *
     * This constructor extracts the row key, column key, and value from the provided
     * [CellInterface] instance and initializes a new [MCell] instance with those values.
     *
     * @param cell The [CellInterface] from which the row key, column key, and value are retrieved
     *             for initializing the mutable cell.
     * @since 1.0.0
     */
    constructor(cell: CellInterface<R, C, V?>) : this(cell.rowKey, cell.columnKey, cell.value)

    companion object {
        /**
         * A unique identifier used for serialization and deserialization of the [MCell] class.
         *
         * This constant is primarily utilized to ensure compatibility during the serialization process,
         * maintaining the consistency of serialized data even when the class definition changes over time.
         *
         * If the serialized object version does not match the class version,
         * an `InvalidClassException` is thrown, preventing deserialization.
         *
         * @since 1.0.0
         */
        @Serial private const val serialVersionUID = 1L

        /**
         * Converts the [Triple] instance into a [MCell] object with the first element as the row key,
         * the second element as the column key, and the third element as the value.
         *
         * This method simplifies the creation of a [MCell] from a [Triple], allowing seamless
         * interoperability between the two structures.
         *
         * @receiver the [Triple] object, where the first element represents the row key,
         * the second element represents the column key, and the third element represents the value.
         * @return a new [MCell] instance with the row key, column key, and value derived from the [Triple].
         * @since 1.0.0
         */
        fun <R, C, V> Triple<R, C, V>.toMCell() = MCell(first, second, third)

        class Serializer : ValueSerializer<MCell<Any, Any, Any?>>() {
            override fun serialize(
                value: MCell<Any, Any, Any?>,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                gen.writePOJOProperty("rowKey", value.rowKey)
                gen.writePOJOProperty("columnKey", value.columnKey)
                gen.writePOJOProperty("value", value.value)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<MCell<Any, Any, Any?>>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): MCell<Any, Any, Any?> {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                return MCell(
                    node.get("rowKey"),
                    node.get("columnKey"),
                    node.get("value")
                )
            }
        }

        class OldSerializer : JsonSerializer<MCell<Any, Any, Any?>>() {
            override fun serialize(value: MCell<Any, Any, Any?>, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeObjectField("rowKey", value.rowKey)
                gen.writeObjectField("columnKey", value.columnKey)
                gen.writeObjectField("value", value.value)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<MCell<Any, Any, Any?>>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): MCell<Any, Any, Any?> {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                return MCell(
                    node.get("rowKey"),
                    node.get("columnKey"),
                    node.get("value")
                )
            }
        }
    }

    /**
     * Returns a string representation of the MutableCell instance.
     *
     * The returned string includes the row key, column key, and value of the cell,
     * formatted as: `MutableCell(rowKey=..., columnKey=..., value=...)`.
     *
     * @return a string representation of the MutableCell instance
     * @since 1.0.0
     */
    override fun toString(): String = "MCell(rowKey=$rowKey, columnKey=$columnKey, value=$value)"

    /**
     * Checks if the current cell has the same row key and column key as the specified cell.
     *
     * This method is used to compare the keys of two cells to determine if they refer
     * to the same position in a table or grid structure.
     *
     * @param other the cell to compare with the current cell
     * @return true if both rowKey and columnKey are equal in the two cells, false otherwise
     * @since 1.0.0
     */
    fun sameKeys(other: MCell<R, C, *>) = rowKey == other.rowKey && columnKey == other.columnKey

    /**
     * Checks equality between the current object and the specified object.
     *
     * @param other The object to compare with the current object.
     * @return `true` if the objects are equal, `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass.superclass != other?.javaClass?.superclass) return false

        other as CellInterface<*, *, *>

        if (rowKey != other.rowKey) return false
        if (columnKey != other.columnKey) return false
        if (value != other.value) return false

        return true
    }

    /**
     * Generates a hash code for the `MutableCell` instance based on its `rowKey`,
     * `columnKey`, and `value` properties.
     *
     * The hash code is computed using the standard formula that combines the hash code of each field:
     * `31 * result + (field?.hashCode() ?: 0)` for each field.
     *
     * @return an integer representing the hash code of the `MutableCell` instance
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = rowKey?.hashCode() ?: 0
        result = 31 * result + (columnKey?.hashCode() ?: 0)
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }
}

/**
 * Represents a table structure consisting of rows, columns, and values,
 * encapsulating relationships between the respective combinations of row and column keys.
 *
 * @param R the type of row keys
 * @param C the type of column keys
 * @param V the type of values stored in the table
 * @param entries the list of cells composing the table
 * @since 1.0.0
 */
@JsonSerialize(using = Table.Companion.Serializer::class)
@JsonDeserialize(using = Table.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Table.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Table.Companion.OldDeserializer::class)
open class Table<R, C, V> internal constructor(entries: List<Cell<R, C, V?>>) : Collection<Cell<R, C, V?>>, Serializable {
    /**
     * Represents the size of a certain collection or data structure.
     * This value is derived from the total number of elements in the `cells` structure.
     *
     * @return The number of elements contained in `cells`.
     * @since 1.0.0
     */
    override val size: Int
        get() = cells.size
    /**
     * Represents the number of cells in the table that have a non-null value.
     *
     * This property calculates the count of cells where the value is not null
     * by filtering the collection of `cells` for entries whose `value` is non-null.
     * It provides a quick way to determine the total number of meaningful entries
     * in the table, excluding any entries with null values.
     *
     * @since 1.0.0
     */
    val sizeNotNull: Int
        get() = cells.filter { it.value.isNotNull() }.size
    /**
     * A collection of distinct row keys present in the table, representing all unique rows defined by the cells.
     * The row keys are derived from the `rowKey` property of each cell in the table.
     *
     * @property rowKeys A list containing the distinct row keys of the table.
     * @since 1.0.0
     */
    val rowKeys: List<R>
        get() = cells.mappedTo { it.rowKey }.distinct()
    /**
     * A list of distinct column keys present in the table.
     * The column keys are extracted from the cells of the table
     * and are guaranteed to not contain duplicates.
     *
     * This property provides a convenient way to access all unique
     * column keys in the table without individually iterating through
     * the cells.
     *
     * @return A list of unique column keys of type `C`.
     * @since 1.0.0
     */
    val columnKeys: List<C>
        get() = cells.mappedTo { it.columnKey }.distinct()
    /**
     * Retrieves a list of distinct nullable values of type `V` from the `cells` collection by mapping
     * each element's `value` property.
     *
     * This property ensures that the resulting list contains only unique values derived from the
     * underlying collection.
     *
     * @return A list of unique nullable values of type `V`.
     * @since 1.0.0
     */
    val values: List<V?>
        get() = cells.mappedTo { it.value }.distinct()
    /**
     * A read-only property that returns a list of non-null elements from the `values` collection.
     * Filters the `values` and excludes any elements that are null, ensuring the returned list
     * contains only non-null items.
     *
     * @since 1.0.0
     */
    val valuesNotNull: List<V>
        get() = values.filterNotNull()
    /**
     * Provides a mapping of row keys to the corresponding list of cell values for each row in the table.
     * The values within each list may include nulls, indicating the absence of a value in the corresponding cell.
     * This property is computed based on the underlying row data in the table.
     *
     * @return A map where the keys represent the row identifiers, and the values are lists of cell values
     * (which may include null elements) for the respective rows.
     * @since 1.0.0
     */
    val valuesByRows: MultiMap<R, V?>
        get() = rows.mapValues { it.value.mappedTo { cell -> cell.value } }
    /**
     * Represents a derived map that provides the values of the table grouped by their respective columns.
     * The keys in the map correspond to the column identifiers, while the values are lists containing
     * the respective cell values for those columns. The lists include nullable values where the table
     * contains null entries.
     *
     * This property processes the internal column structure of the table to map column keys to their
     * associated values.
     *
     * @receiver A table structure containing rows, columns, and their respective values.
     * @return A map where each key is a column key, and the associated value is a list of nullable values
     *         for that column.
     * @since 1.0.0
     */
    val valuesByColumns: MultiMap<C, V?>
        get() = columns.mapValues { it.value.mappedTo { cell -> cell.value } }
    /**
     * A list of all cells in a `Table`. Each cell contains a combination of
     * a row key, column key, and optionally a value.
     *
     * This property provides access to the complete set of cells that constitute
     * the table's data structure. Each cell may contain a `null` value.
     *
     * @since 1.0.0
     */
    val cells: List<Cell<R, C, V?>>
    /**
     * A computed property that returns a list of complete rows in the table.
     * A row is considered complete if all its associated cells contain non-null values.
     *
     * @return A list of row keys of type `R` representing the complete rows in the table.
     * @since 1.0.0
     */
    val completeRows: List<R>
        get() {
            val rows = mutableListOf<R>()
            rowKeys.forEach { rowKey ->
                val all = cells.filter { it.rowKey == rowKey }
                if (all.size == all.filter { it.value.isNotNull() }.size) rows.add(rowKey)
            }
            return rows
        }
    /**
     * A computed property that retrieves a list of column keys where each column has all of its cells filled with non-null values.
     *
     * The property iterates over all the column keys present in the table and filters out the columns where all cells have non-null values.
     * It returns a list of such column keys that are considered complete.
     *
     * @return A list of column keys representing columns with all non-null values.
     *
     * @since 1.0.0
     */
    val completeColumns: List<C>
        get() {
            val columns = mutableListOf<C>()
            columnKeys.forEach { columnKey ->
                val all = cells.filter { it.columnKey == columnKey }
                if (all.size == all.filter { it.value.isNotNull() }.size) columns.add(columnKey)
            }
            return columns
        }
    /**
     * Returns a list containing all the cells in the table that do not have an assigned value (i.e., their value is `null`).
     *
     * This property is a read-only view that filters the current cells of the table
     * to identify the cells that are considered empty. Empty cells are defined as cells
     * where the `value` is explicitly `null`. The result reflects the current state of
     * the table at the time of the property access and is dynamically computed.
     *
     * @return A list of `Cell` instances with a `null` value, representing empty cells in the table.
     * @see Table.cells
     * @since 1.0.0
     */
    val emptyCells: List<Cell<R, C, V?>>
        get() = cells.filter { it.value.isNull() }
    /**
     * A computed property that provides a list of row keys where all corresponding cell values are null.
     *
     * The property iterates over all the row keys in the table, filtering keys where all cells in the row have
     * null values. Rows containing at least one non-null value are excluded from the result.
     *
     * @return A list of row keys representing rows where all cell values are null.
     * @since 1.0.0
     */
    val emptyRows: List<R>
        get() {
            val rows = mutableListOf<R>()
            rowKeys.forEach { rowKey ->
                val all = cells.filter { it.rowKey == rowKey }
                if (all.size == all.filter { it.value.isNull() }.size) rows.add(rowKey)
            }
            return rows
        }
    /**
     * A list of column keys where all cell values are `null`.
     *
     * This property iterates over all columns in the table, filtering their corresponding cells.
     * A column is considered "empty" if all its cells have a `null` value.
     * The result is a list containing only the keys of these empty columns.
     *
     * @receiver Table instance for which the empty columns are calculated.
     * @return A list of column keys (`C`) that correspond to empty columns in the table.
     * @since 1.0.0
     */
    val emptyColumns: List<C>
        get() {
            val columns = mutableListOf<C>()
            columnKeys.forEach { columnKey ->
                val all = cells.filter { it.columnKey == columnKey }
                if (all.size == all.filter { it.value.isNull() }.size) columns.add(columnKey)
            }
            return columns
        }
    /**
     * Provides a map representation of the table's rows, where each key is a row key and the value is a map
     * representing the columns and their corresponding values for that row.
     *
     * For each row key, a nested map is created where the column keys serve as the keys and the associated values
     * in the table serve as the values. If a cell has a null value, it is retained in the nested map.
     *
     * This property is useful for retrieving all the data associated with a specific row in a structured format.
     *
     * The resulting map is built dynamically based on the current state of the table.
     *
     * @return A map of rows, where each row is represented as a map of column keys to their corresponding values.
     * @since 1.0.0
     */
    val rowsAsMap: Map<R, Map<C, V?>>
        get() {
            val rows = mutableMapOf<R, Map<C, V?>>()
            rowKeys.forEach { rowKey -> rows.getOrPut(rowKey) { cells.filter { it.rowKey == rowKey }.associate { it.columnKey to it.value}}; }
            return rows
        }
    /**
     * A computed property that represents the table's structure as a map of column keys to their associated rows and values.
     *
     * This property creates a map where each key corresponds to a column key from the table, and each value
     * is another map. The inner map associates row keys with their respective values for the given column.
     * If a cell in the table does not have a value, it is represented as `null` in the inner map.
     *
     * @return A map of column keys to their respective maps of row keys and values.
     * @since 1.0.0
     */
    val columnsAsMap: Map<C, Map<R, V?>>
        get() {
            val columns = mutableMapOf<C, Map<R, V?>>()
            columnKeys.forEach { columnKey -> columns.getOrPut(columnKey) { cells.filter { it.columnKey == columnKey }.associate { it.rowKey to it.value}} }
            return columns
        }
    /**
     * Provides a mapping of row keys to their associated cells in the table.
     * Each key in the map corresponds to a row key, and its value is a list
     * of cells that belong to that row. Rows are dynamically constructed from
     * the table's cells and are filtered to include only entries matching the given row key.
     *
     * @return A map where keys are row identifiers and values are lists of cells for those rows.
     * @since 1.0.0
     */
    val rows: MultiMap<R, Cell<R, C, V?>>
        get() {
            val rows = mutableMapOf<R, List<Cell<R, C, V?>>>()
            rowKeys.forEach { rowKey -> rows.getOrPut(rowKey) { cells.filter { it.rowKey == rowKey }} }
            return rows
        }
    /**
     * A map representation where each key corresponds to a column key of type `C`,
     * and the associated value is a list of `Cell` objects that belong to that column.
     *
     * The `columns` property is designed to group all the `Cell` objects by their column keys.
     * It filters and organizes the `Cell` instances of type `Cell<R, C, V?>` based on their `columnKey`.
     * Each key in the map corresponds to a distinct column, and its value contains the list of cells
     * belonging to that column.
     *
     * The property is derived dynamically using the available `columnKeys` and filters the `cells`
     * collection to group cells by their respective column keys.
     *
     * @since 1.0.0
     */
    val columns: MultiMap<C, Cell<R, C, V?>>
        get() {
            val columns = mutableMapOf<C, List<Cell<R, C, V?>>>()
            columnKeys.forEach { columnKey -> columns.getOrPut(columnKey) { cells.filter { it.columnKey == columnKey }} }
            return columns
        }
    /**
     * Retrieves the first cell in the table, or returns `null` if the table is empty.
     *
     * The first cell corresponds to the first element in the sequence of all cells
     * stored in the table, as determined by the order stored in the `cells` collection.
     * The cell contains row and column keys, as well as an optional value, which may be `null`.
     *
     * @return the first cell in the table if it exists, or `null` otherwise.
     * @since 1.0.0
     */
    val firstCell: Cell<R, C, V?>?
        get() = cells.firstOrNull()
    /**
     * Retrieves the last cell within the `Table`, or `null` if the table contains no cells.
     *
     * This property provides access to the final cell in the list of all cells, maintaining the
     * insertion order. If the table is empty, it safely returns `null`.
     *
     * @return The last cell of the table, or `null` if the table is empty.
     * @since 1.0.0
     */
    val lastCell: Cell<R, C, V?>?
        get() = cells.lastOrNull()
    /**
     * Retrieves the cells corresponding to the first column in the table.
     *
     * The first column is determined based on the column key of the first cell
     * (`firstCell`). If there are no cells or the `firstCell` is null, the result
     * will be an empty list or null. Otherwise, this property filters the table's
     * cells and includes only those that share the same column key as the first cell.
     *
     * @return A list of cells in the first column of the table, or null if the table
     * is empty or lacks a valid first cell.
     * @since 1.0.0
     */
    val firstColumn: List<Cell<R, C, V?>>
        get() = cells.filter { it.columnKey == firstCell?.columnKey }
    /**
     * Retrieves the first row of cells in the table as a list. The row is determined
     * by filtering cells with the same row key as the first cell in the table, if it exists.
     * If the table is empty or the first cell is not available, the result is `null`.
     *
     * @return A list of cells corresponding to the first row, or `null` if no such row exists.
     * @since 1.0.0
     */
    val firstRow: List<Cell<R, C, V?>>
        get() = cells.filter { it.rowKey == firstCell?.rowKey }
    /**
     * Retrieves the list of cells from the table that belong to the same column as the last cell.
     * The column is identified by matching the column key of the last cell.
     *
     * If there is no `lastCell` or `lastCell` does not have a column key, the result will be null.
     * This property serves as a helper for quickly accessing all cells in the last column seen in the table.
     *
     * @return A list of cells belonging to the same column as the last cell, or null if no such column exists.
     * @since 1.0.0
     */
    val lastColumn: List<Cell<R, C, V?>>
        get() = cells.filter { it.columnKey == lastCell?.columnKey }
    /**
     * Retrieves a list of cells that belong to the last row of the table, based on the `rowKey`
     * of the last cell in the table.
     *
     * If there is no last cell in the table (i.e., the table is empty), this property will return `null`.
     *
     * @return A list of cells in the last row of the table, or `null` if the table is empty.
     * @since 1.0.0
     */
    val lastRow: List<Cell<R, C, V?>>
        get() = cells.filter { it.rowKey == lastCell?.rowKey }
    /**
     * A read-only map where each entry corresponds to a row key and its associated first cell within the row.
     * The value of the map is derived by selecting the first cell in each row.
     *
     * @receiver The table from which the first cell of each row is retrieved.
     * @return A map with row keys as keys and their first respective cells as values.
     * @since 1.0.0
     */
    val firstCellOfRows: Map<R, Cell<R, C, V?>>
        get() =  rows.mapValues { it.value.first() }
    /**
     * Represents a read-only map where each column key in the table is associated
     * with the first cell of that column. The map's keys correspond to the column keys
     * of the table, and the values represent the first cell in the respective columns.
     *
     * This property is derived by extracting the first cell for each column
     * from the underlying data structure. If a column is empty, it will not
     * be included in the resulting map.
     *
     * @return A map associating each column key in the table with the first cell of that column.
     * @since 1.0.0
     */
    val firstCellOfColumns: Map<C, Cell<R, C, V?>>
        get() = columns.mapValues { it.value.first() }
    /**
     * Represents a map of the last cell in each column of the table.
     *
     * The map's keys correspond to the column keys, and the values are the last `Cell` objects
     * found in their respective columns. The last cell is determined based on the order
     * of cells in the data structure representing the columns.
     *
     * @return A map where each key is a column key, and each value is the last `Cell`
     * associated with the respective column key.
     *
     * @since 1.0.0
     */
    val lastCellOfColumns: Map<C, Cell<R, C, V?>>
        get() = columns.mapValues { it.value.last() }
    /**
     * Provides a mapping of the last cell for each row in the table.
     *
     * Each entry in the map represents a row in the table, where the key is the row identifier
     * and the value is the last cell in that row. The last cell is determined by the order
     * of cells within the row.
     *
     * This property is useful when operation or inspection requires focusing on
     * the last elements of each row.
     *
     * @return a map where each entry consists of a row key and the last cell of that row.
     * @since 1.0.0
     */
    val lastCellOfRows: Map<R, Cell<R, C, V?>>
        get() = rows.mapValues { it.value.last() }

    init {
        cells = recheckEntries(entries)
    }

    companion object {
        class Serializer : ValueSerializer<Table<Any, Any, Any?>>() {
            override fun serialize(
                value: Table<Any, Any, Any?>,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartArray()
                value.cells.forEach {
                    gen.writeStartObject()
                    gen.writePOJOProperty("rowKey", it.rowKey)
                    gen.writePOJOProperty("columnKey", it.columnKey)
                    gen.writePOJOProperty("value", it.value)
                    gen.writeEndObject()
                }
                gen.writeEndArray()
            }
        }

        class Deserializer : ValueDeserializer<Table<Any, Any, Any?>>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Table<Any, Any, Any?> {
                val entries = mutableListOf<Cell<Any, Any, Any?>>()
                p.objectReadContext().readTree<ObjectNode>(p).forEach { node ->
                    entries.add(
                        Cell(
                            if (node.get("rowKey").toString().startsWith("\"") && node.get("rowKey").toString().endsWith("\"")) node.get("rowKey").toString()[1..<node.get("rowKey").toString().length - 1] else node.get("rowKey").toString(),
                            if (node.get("columnKey").toString().startsWith("\"") && node.get("columnKey").toString().endsWith("\"")) node.get("columnKey").toString()[1..<node.get("columnKey").toString().length - 1] else node.get("columnKey").toString(),
                            if (node.get("value").toString().startsWith("\"") && node.get("value").toString().endsWith("\"")) node.get("value").toString()[1..<node.get("value").toString().length - 1] else (if (node.get("value").toString() == "null") null else node.get("value").toString())
                        )
                    )
                }
                return Table(entries)
            }
        }

        class OldSerializer : JsonSerializer<Table<Any, Any, Any?>>() {
            override fun serialize(value: Table<Any, Any, Any?>, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartArray()
                value.cells.forEach {
                    gen.writeStartObject()
                    gen.writeObjectField("rowKey", it.rowKey)
                    gen.writeObjectField("columnKey", it.columnKey)
                    gen.writeObjectField("value", it.value)
                    gen.writeEndObject()
                }
                gen.writeEndArray()
            }
        }

        class OldDeserializer : JsonDeserializer<Table<Any, Any, Any?>>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Table<Any, Any, Any?> {
                val entries = mutableListOf<Cell<Any, Any, Any?>>()
                p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p).forEach { node ->
                    entries.add(
                        Cell(
                            if (node.get("rowKey").toString().startsWith("\"") && node.get("rowKey").toString().endsWith("\"")) node.get("rowKey").toString()[1..<node.get("rowKey").toString().length - 1] else node.get("rowKey").toString(),
                            if (node.get("columnKey").toString().startsWith("\"") && node.get("columnKey").toString().endsWith("\"")) node.get("columnKey").toString()[1..<node.get("columnKey").toString().length - 1] else node.get("columnKey").toString(),
                            if (node.get("value").toString().startsWith("\"") && node.get("value").toString().endsWith("\"")) node.get("value").toString()[1..<node.get("value").toString().length - 1] else (if (node.get("value").toString() == "null") null else node.get("value").toString())
                        )
                    )
                }
                return Table(entries)
            }
        }
    }

    /**
     * Rechecks the provided list of entries, ensuring that each cell is unique by its row and column keys.
     * The method ensures entries are sorted and fills in any missing cells with a null value where appropriate.
     *
     * @param entries the list of cells to be rechecked, sorted, and completed
     * @return a new list of cells, sorted and containing any missing cells filled with null values
     * @since 1.0.0
     */
    private fun recheckEntries(entries: List<Cell<R, C, V?>>): List<Cell<R, C, V?>> {
        val _entries = entries.toMutableList()
        val _entries2 = entries.toMutableList()
        val alreadyTaken = mutableListOf<Cell<R, C, V?>>()
        _entries2.forEach { cell ->
            val old: Cell<R, C, V?>? = alreadyTaken.forEachWithReturn { if (it.sameKeys(cell)) breakLoop(it) }
            if (old.isNotNull()) _entries.remove(old)
            alreadyTaken.add(cell)
        }
        val sortedEntries = _entries.sortedWith(
            compareBy<Cell<R, C, V?>> { it.rowKey.toString() }
                .thenBy { it.columnKey.toString() }
        )
        _entries.clear()
        _entries.addAll(sortedEntries)
        val groupedByRow = _entries.groupedBy { it.rowKey }
        val allColumnsKeys = _entries.groupedBy { it.columnKey }.keys
        allColumnsKeys.forEach { columnKey ->
            groupedByRow.forEach { (rowKey, cells) ->
                val isPresent: Boolean = cells.forEachWithReturn { if (it.columnKey == columnKey) breakLoop(true) } ?: false
                if (!isPresent) _entries.add(Cell(rowKey, columnKey, null))
            }
        }
        return _entries
    }

    /**
     * Creates a new table instance as a copy of the provided table.
     *
     * @param table The table to be copied. Must not be null.
     * @return A new table containing the same cells as the provided table.
     * @since 1.0.0
     */
    fun copyOf(table: Table<R, C, V?>): Table<R, C, V?> = Table(table.cells)

    /**
     * Creates a mutable copy of the given table.
     * The returned mutable table allows modifications such as adding, removing, or updating cells.
     *
     * @param table The source table to copy. This table remains unchanged.
     * @return A new instance of MutableTable containing all cells from the provided table.
     * @since 1.0.0
     */
    fun mutableCopyOf(table: Table<R, C, V?>): MTable<R, C, V?> = MTable(table.cells.mappedTo { it.toMCell() })

    /**
     * Checks if the table is empty.
     *
     * @return true if the table contains no cells, false otherwise.
     * @since 1.0.0
     */
    override fun isEmpty() = cells.isEmpty()

    // `isNullOrEmpty` and `isNotNullOrEmpty` as extension

    /**
     * Returns an iterator over the collection of cells in this data structure.
     * Each cell is represented as an object containing a row index, column index, and a value.
     *
     * @return an iterator to iterate through the cells of the data structure.
     * @since 1.0.0
     */
    override fun iterator(): Iterator<Cell<R, C, V?>> = cells.iterator()

    /**
     * Checks if the given cell is contained within the table.
     *
     * This method evaluates whether the specified [element] exists in the set of cells
     * defined within the table, based on equality.
     *
     * @param element the cell to check for existence within the table
     * @since 1.0.0
     */
    operator fun contains(element: MCell<R, C, V?>) = cells.any { element == it }
    /**
     * Checks if the given cell is contained within the table.
     *
     * This method evaluates whether the specified [element] exists in the set of cells
     * defined within the table, based on equality.
     *
     * @param element the cell to check for existence within the table
     * @since 1.0.0
     */
    override operator fun contains(element: Cell<R, C, V?>) = cells.any { element == it }
    /**
     * Checks if the [cell] in the form of a [Triple] is contained within the table.
     *
     * @param cell The cell represented as a [Triple] consisting of a row key, column key, and value to be checked for existence within the table.
     * @return `true` if the specified cell exists in the table, `false` otherwise.
     * @since 1.0.0
     */
    operator fun contains(cell: Triple<R, C, V?>) = cells.any { Cell(cell) == it }
    /**
     * Checks if the table contains a cell with the specified row key and column key.
     *
     * @param rowKey The row key of the cell to check.
     * @param columnKey The column key of the cell to check.
     * @since 1.0.0
     */
    fun contains(rowKey: R, columnKey: C) = cells.any { it.rowKey == rowKey && it.columnKey == columnKey }
    /**
     * Checks whether the specified pair of row and column keys exists in the table.
     *
     * @param keys A pair containing the row key and the column key to check for presence in the table.
     * @return `true` if the table contains an entry with the specified row and column keys, otherwise `false`.
     * @since 1.0.0
     */
    operator fun contains(keys: Pair<R, C>) = cells.any { it.rowKey == keys.first && it.columnKey == keys.second }

    /**
     * Checks if all elements in the specified collection are present within this container.
     *
     * @param elements the collection of elements to be checked for containment
     * @return `true` if this container contains all elements, `false` otherwise
     * @since 1.0.0
     */
    override fun containsAll(elements: Collection<Cell<R, C, V?>>) = cells.containsAll(elements)

    /**
     * Retrieves the first cell of the specified row.
     *
     * @param rowKey the key of the row for which the first cell is to be retrieved
     * @return the first cell of the given row, or null if the row or cell does not exist
     * @since 1.0.0
     */
    fun getFirstCellOfRow(rowKey: R): Cell<R, C, V?>? = firstCellOfRows[rowKey]

    /**
     * Retrieves the last cell of the specified row.
     *
     * @param rowKey The key of the row for which the last cell is to be retrieved.
     * @return The last cell of the specified row, or null if the row does not exist or is empty.
     * @since 1.0.0
     */
    fun getLastCellOfRow(rowKey: R): Cell<R, C, V?>? = lastCellOfRows[rowKey]

    /**
     * Retrieves the first cell of the specified column.
     *
     * @param columnKey the key identifying the column whose first cell is to be returned
     * @return the first cell of the specified column, or null if the column does not exist or is empty
     * @since 1.0.0
     */
    fun getFirstCellOfColumn(columnKey: C): Cell<R, C, V?>? = firstCellOfColumns[columnKey]

    /**
     * Retrieves the last cell associated with the specified column key.
     *
     * @param columnKey The key of the column whose last cell is to be retrieved.
     * @return The last cell of the specified column, or null if the columnKey does not exist.
     * @since 1.0.0
     */
    fun getLastCellOfColumn(columnKey: C): Cell<R, C, V?>? = lastCellOfColumns[columnKey]

    /**
     * Retrieves the row associated with the specified key from the dataset.
     *
     * @param rowKey The key identifying the row to retrieve.
     * @return A list of cells corresponding to the requested row, or null if the key does not exist.
     * @since 1.0.0
     */
    fun getRow(rowKey: R): List<Cell<R, C, V?>>? = rows[rowKey]

    /**
     * Retrieves the column associated with the specified column key.
     *
     * The method returns a list of cells corresponding to the specified column,
     * or null if the column key is not present in the table.
     *
     * @param columnKey the key identifying the column to retrieve
     * @return a list of cells in the specified column, or null if the column key is not found
     * @since 1.0.0
     */
    fun getColumn(columnKey: C): List<Cell<R, C, V?>>? = columns[columnKey]

    /**
     * Retrieves the value associated with the specified row key.
     *
     * @param rowKey the key of the row to retrieve
     * @return the value associated with the specified row key
     * @since 1.0.0
     */
    operator fun get(rowKey: R) = getRow(rowKey)
    /**
     * Retrieves the value associated with the given row and column keys.
     *
     * @param rowKey The key identifying the row of the desired value.
     * @param columnKey The key identifying the column of the desired value.
     * @return The value corresponding to the specified row and column keys, or `null` if no such value exists.
     * @since 1.0.0
     */
    operator fun get(rowKey: R, columnKey: C): V? = cells.find { it.rowKey == rowKey && it.columnKey == columnKey }.let { it?.value }
    /**
     * Retrieves the value associated with the given pair of row and column keys in the table.
     *
     * @param keys A pair consisting of the row key and column key used to locate the desired value.
     * @return The value associated with the specified keys, or `null` if no such value exists.
     * @since 1.0.0
     */
    operator fun get(keys: Pair<R, C>): V? = cells.find { it.rowKey == keys.first && it.columnKey == keys.second }.let { it?.value }

    /**
     * Retrieves a cell from the table that matches the specified row key, column key, and value.
     * If no matching cell is found, returns null.
     *
     * @param rowKey the key identifying the row to search for
     * @param columnKey the key identifying the column to search for
     * @param value the value associated with the cell to search for, may be nullable
     * @return the cell that matches the given row key, column key, and value, or null if no such cell exists
     * @since 1.0.0
     */
    fun getCell(rowKey: R, columnKey: C, value: V?): Cell<R, C, V?>? = cells.find { it.rowKey == rowKey && it.columnKey == columnKey && it.value == value }
    /**
     * Retrieves a cell from the table that matches the specified row key and column key.
     *
     * @param rowKey the key identifying the row of the cell to retrieve
     * @param columnKey the key identifying the column of the cell to retrieve
     * @return the matching cell if found, or null if no such cell exists
     * @since 1.0.0
     */
    fun getCell(rowKey: R, columnKey: C): Cell<R, C, V?>? = cells.find { it.rowKey == rowKey && it.columnKey == columnKey }
    /**
     * Retrieves a cell from the collection of cells based on the provided row and column keys.
     *
     * @param keys a pair representing the row key and column key of the desired cell
     * @return the cell matching the given row and column keys, or null if no such cell exists
     * @since 1.0.0
     */
    fun getCell(keys: Pair<R, C>): Cell<R, C, V?>? = cells.find { it.rowKey == keys.first && it.columnKey == keys.second }

     /**
      * Retrieves a map of column keys and their corresponding values for the specified row key.
      *
      * @param rowKey the key of the row for which the column-value mapping is to be retrieved.
      * @return a map of column keys to their associated values for the given row.
      * @since 1.0.0
      */
     fun getFromRow(rowKey: R): Map<C, V?> = cells.filter { it.rowKey == rowKey }.associate { it.columnKey to it.value }

    /**
     * Retrieves a map of column keys to non-null values for a given row key.
     *
     * This method filters out all entries where the value is null, ensuring
     * that only non-null values are included in the returned map.
     *
     * @param rowKey The key of the row from which to retrieve non-null values.
     * @return A map of column keys to non-null values for the specified row key.
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    fun getFromRowNotNull(rowKey: R): Map<C, V> = getFromRow(rowKey).filterValues { it.isNotNull() } as Map<C, V>

    /**
     * Retrieves a map of row keys to their associated values for a specified column key.
     *
     * @param columnKey the key representing the column from which to retrieve data
     * @return a map where the keys are row identifiers and the values are the associated values for the given column key
     * @since 1.0.0
     */
    fun getFromColumn(columnKey: C): Map<R, V?> = cells.filter { it.columnKey == columnKey }.associate { it.rowKey to it.value }

    /**
     * Retrieves a map of row keys to their corresponding non-null values for a specified column key.
     * Filters out any entries where the value is null.
     *
     * @param columnKey The key representing the column from which the data is retrieved.
     * @return A map where the keys are the row keys and the values are the non-null corresponding values for the specified column key.
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    fun getFromColumnNotNull(columnKey: C): Map<R, V> = getFromColumn(columnKey).filterValues { it.isNotNull() } as Map<R, V>

    /**
     * Generates a string representation of the table, formatted in a structured and visually readable way.
     *
     * @return A string that represents the current state of the table including its rows, columns, and values.
     *         If the table is empty, it returns "Empty Table".
     * @since 1.0.0
     */
    override fun toString(): String {
        if (isEmpty()) return "Empty Table"
        val width = valuesByColumns.mapKeys { it.key.toString() }
            .mapValues { (key, list) ->
                (list + key).maxOf { it?.toString()?.length ?: 0 }
            }
        val keyWidth = rowKeys.maxOfOrNull { it.toString().length } ?: 0

        return buildString {
            append(ANSI.RESET + " ")
            append(" ".repeat(keyWidth)).append("┏")
            for (col in columnKeys.mappedTo { it.toString() })
                append("━".repeat(width[col]!!)).append("┯")
            setLength(length - 1)
            append("┓")
            appendLine()
            append(" ")
            append(" ".repeat(keyWidth)).append("┃")
            for (col in columnKeys.mappedTo { it.toString() })
                append(ANSI.BOLD + col.center(width[col]!!) + ANSI.RESET).append("│")
            setLength(length - 1)
            append("┃")
            appendLine()
            append("┏")
            append("━".repeat(keyWidth)).append("╋")
            for (col in columnKeys.mappedTo { it.toString() })
                append("╍".repeat(width[col]!!)).append("┿")
            setLength(length - 1)
            append("┫")
            for ((index, row) in rowKeys.withIndex()) {
                appendLine()
                append("┃").append(ANSI.BOLD + row.toString().padEnd(keyWidth) + ANSI.RESET).append("┋")
                for (column in columnKeys) {
                    val value = this@Table[row, column]?.toString()
                    append(value?.padEnd(width[column.toString()]!!)?: (ANSI.BLACK_BACKGROUND + " ".repeat(width[column.toString()]!!) + ANSI.RESET))
                    append("│")
                }
                setLength(length - 1)
                append("┃")
                if (index != rowKeys.size - 1) {
                    appendLine()
                    append("┃")
                    append("─".repeat(keyWidth)).append("╂")
                    for (col in columnKeys.mappedTo { it.toString() })
                        append("─".repeat(width[col]!!)).append("┼")
                    setLength(length - 1)
                    append("┨")
                }
            }
            appendLine()
            append("┗")
            append("━".repeat(keyWidth)).append("┻")
            for (col in columnKeys.mappedTo { it.toString() })
                append("━".repeat(width[col]!!)).append("┷")
            setLength(length - 1)
            append("┛")
        }
    }

    /**
     * Compares this instance with the specified object for equality.
     *
     * @param other the object to be compared with this instance.
     * @return true if the specified object is equal to this instance, false otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Table<*, *, *>

        return cells == other.cells
    }

    /**
     * Generates a hash code for this object based on its internal state.
     *
     * @return an integer representing the hash code of the object.
     * @since 1.0.0
     */
    override fun hashCode(): Int = cells.hashCode()

    /**
     * Converts the current table into a mutable table instance.
     *
     * This method creates a new MutableTable instance containing
     * mutable cells derived from the original table's cells.
     *
     * @return a new MutableTable containing mutable cells with the same data as the original.
     * @since 1.0.0
     */
    fun toMTable(): MTable<R, C, V?> = MTable(cells.mappedTo { it.toMCell() })
}

/**
 * A mutable two-dimensional table structure that stores data mapped by row and column keys.
 * Allows efficient access, modification, and iteration over its elements.
 *
 * @param R The type of the row keys.
 * @param C The type of the column keys.
 * @param V The type of the cell values.
 *
 * @since 1.0.0
 */
@JsonSerialize(using = MTable.Companion.Serializer::class)
@JsonDeserialize(using = MTable.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = MTable.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = MTable.Companion.OldDeserializer::class)
open class MTable<R, C, V> internal constructor(entries: List<MCell<R, C, V?>>) : Collection<MCell<R, C, V?>>, Serializable {
    /**
     * Represents the total number of elements or items contained within a collection or structure.
     * This property is dynamically computed based on the underlying dataset.
     *
     * @return the number of items in the associated collection or structure.
     * @since 1.0.0
     */
    override val size: Int
        get() = cells.size
    /**
     * Represents the count of non-null values in the table.
     *
     * This property calculates the size based on the number of cells in the table
     * that have a non-null value. It dynamically filters the cells to evaluate
     * which ones contain a value that is not null.
     *
     * @see cells
     * @since 1.0.0
     */
    val sizeNotNull: Int
        get() = cells.filter { it.value.isNotNull() }.size
    /**
     * Provides a distinct list of all row keys present within the table.
     * The row keys are extracted from the underlying cells of the table and
     * are guaranteed to be unique.
     *
     * @return A list of unique row keys of type [R].
     * @since 1.0.0
     */
    val rowKeys: List<R>
        get() = cells.mappedTo { it.rowKey }.distinct()
    /**
     * A property that retrieves a distinct list of column keys from the table's cells.
     * Each column key represents a unique identifier for a column in the table.
     *
     * This property is computed by mapping the table's cells to their column keys and
     * then removing duplicate values to provide a unique list.
     *
     * @since 1.0.0
     */
    val columnKeys: List<C>
        get() = cells.mappedTo { it.columnKey }.distinct()
    /**
     * Provides a list of distinct values contained in the table's cells.
     * The values are extracted from all cells and filtered to ensure uniqueness.
     * Null values are included in the resulting list if present in the cells.
     *
     * @return A list of distinct values, which can include null.
     * @since 1.0.0
     */
    val values: List<V?>
        get() = cells.mappedTo { it.value }.distinct()
    /**
     * Provides a filtered list of non-null values from the table.
     *
     * This property excludes any null values from the original `values` list,
     * ensuring that the resulting collection only contains valid entries.
     *
     * @return A list containing all non-null values present in the table.
     * @since 1.0.0
     */
    val valuesNotNull: List<V>
        get() = values.filterNotNull()
    /**
     * Provides a view of the table as a map where each row is associated with a list of its corresponding cell values.
     * Each key in the map represents a row, and the associated value is a list of the cell values in that row.
     *
     * Null values within the cells are preserved in the resulting lists.
     *
     * The map is derived from the internal representation of rows in the table.
     *
     * @return A map where the keys are row identifiers and the values are lists of cell values.
     * @since 1.0.0
     */
    val valuesByRows: MultiMap<R, V?>
        get() = rows.mapValues { it.value.mappedTo { cell -> cell.value } }
    /**
     * Provides a mapping of column keys to their respective lists of cell values, where each list
     * contains the values of all cells in the corresponding column. The values can be nullable.
     *
     * This property aggregates values from the `columns` property, mapping each column key to a
     * list of its associated cell values.
     *
     * @return A map where keys represent column identifiers (`C`) and values are lists of cell
     *         values (`List<V?>`) from each column.
     * @since 1.0.0
     */
    val valuesByColumns: MultiMap<C, V?>
        get() = columns.mapValues { it.value.mappedTo { cell -> cell.value } }
    /**
     * The collection of mutable cells representing the data within the mutable table. Each cell corresponds to
     * a specific row and column pair, along with the associated value which may be `null`.
     *
     * This list allows you to access or manipulate the table's structure by directly interacting with its cells.
     * Though this property is private for setting purposes, it can still be utilized to iterate through or review the table's state.
     *
     * @property cells The mutable list containing the table's cells, with each entry being a [MCell] that encapsulates
     * the row, column, and value.
     * @since 1.0.0
     */
    var cells: MutableList<MCell<R, C, V?>>
        private set
    /**
     * Retrieves a list of row keys representing rows in the table where all corresponding values are non-null.
     * Iterates through the row keys and checks if every value in the row is not null.
     * If all values in a row are non-null, the row key is added to the result.
     *
     * @return A list of row keys for rows with no null values.
     * @since 1.0.0
     */
    val completeRows: List<R>
        get() {
            val rows = mutableListOf<R>()
            rowKeys.forEach { rowKey ->
                val all = cells.filter { it.rowKey == rowKey }
                if (all.size == all.filter { it.value.isNotNull() }.size) rows.add(rowKey)
            }
            return rows
        }
    /**
     * A read-only property that provides all column keys where every value in the column is non-null.
     *
     * The `completeColumns` property iterates through all column keys in the table and evaluates
     * the corresponding cells. Columns for which all cells have non-null values are considered
     * "complete" and are included in the resulting list.
     *
     * @return A list of column keys representing the complete columns in the table.
     * @since 1.0.0
     */
    val completeColumns: List<C>
        get() {
            val columns = mutableListOf<C>()
            columnKeys.forEach { columnKey ->
                val all = cells.filter { it.columnKey == columnKey }
                if (all.size == all.filter { it.value.isNotNull() }.size) columns.add(columnKey)
            }
            return columns
        }
    /**
     * Returns a list of all cells in the table that have a `null` value.
     * This property is useful for identifying uninitialized or empty cells
     * within a mutable table structure.
     *
     * @receiver The table instance from which to retrieve the empty cells.
     * @return A list of [MCell] objects representing the cells with `null` values.
     * @since 1.0.0
     */
    val emptyCells: List<MCell<R, C, V?>>
        get() = cells.filter { it.value.isNull() }
    /**
     * Provides a list of row keys where all their corresponding cell values are null.
     *
     * This property iterates through all rows in the table and checks if every cell
     * within a row contains a null value. If so, the row is added to the list of
     * empty rows.
     *
     * @receiver A mutable table instance.
     * @return A list of row keys where all cells are null.
     * @since 1.0.0
     */
    val emptyRows: List<R>
        get() {
            val rows = mutableListOf<R>()
            rowKeys.forEach { rowKey ->
                val all = cells.filter { it.rowKey == rowKey }
                if (all.size == all.filter { it.value.isNull() }.size) rows.add(rowKey)
            }
            return rows
        }
    /**
     * A computed property that provides a list of column keys where all cells in each column have null values.
     *
     * This property iterates over all column keys in the table and checks associated cells to determine whether
     * all their values are `null`. If so, the column key is added to the resulting list, indicating an "empty" column.
     *
     * @return A list of column keys representing empty columns in the table.
     *
     * @since 1.0.0
     */
    val emptyColumns: List<C>
        get() {
            val columns = mutableListOf<C>()
            columnKeys.forEach { columnKey ->
                val all = cells.filter { it.columnKey == columnKey }
                if (all.size == all.filter { it.value.isNull() }.size) columns.add(columnKey)
            }
            return columns
        }
    /**
     * Represents a view of the table as a map of rows, where each row is mapped to another map representing
     * its columns and their corresponding values. The outer map uses row keys as keys, and the inner map
     * represents column keys associated with their corresponding values within that row.
     *
     * This property computes the mapping by filtering the table's internal cells for each distinct row key
     * and organizing the data into a nested map structure.
     *
     * The resulting map is immutable and reflects the current state of the table at the time it is accessed.
     * Any updates to the table will not be reflected in this view once obtained.
     *
     * @return A map where each entry represents a row, containing another map that represents its columns and values.
     * @since 1.0.0
     */
    val rowsAsMap: Map<R, Map<C, V?>>
        get() {
            val rows = mutableMapOf<R, Map<C, V?>>()
            rowKeys.forEach { rowKey -> rows.getOrPut(rowKey) { cells.filter { it.rowKey == rowKey }.associate { it.columnKey to it.value}}; }
            return rows
        }
    /**
     * A computed property that provides a mapping of the table's columns.
     * The keys of the outer map represent the column keys, and the associated values
     * are maps where each key represents a row key, and the value is the cell value
     * at the intersection of the respective column and row.
     *
     * @return A map with column keys as keys and corresponding maps of row keys to cell values as values.
     * @since 1.0.0
     */
    val columnsAsMap: Map<C, Map<R, V?>>
        get() {
            val columns = mutableMapOf<C, Map<R, V?>>()
            columnKeys.forEach { columnKey -> columns.getOrPut(columnKey) { cells.filter { it.columnKey == columnKey }.associate { it.rowKey to it.value}} }
            return columns
        }
    /**
     * Provides a mapping of row keys to their corresponding lists of mutable cells within the table.
     * This property organizes the table's data structure by grouping cells based on their row keys.
     *
     * The resulting map contains each row key as its key and a list of cells belonging to that row as its value.
     *
     * @return A map associating each row key with a list of mutable cells that are part of that row.
     * @since 1.0.0
     */
    val rows: MultiMap<R, MCell<R, C, V?>>
        get() {
            val rows = mutableMapOf<R, List<MCell<R, C, V?>>>()
            rowKeys.forEach { rowKey -> rows.getOrPut(rowKey) { cells.filter { it.rowKey == rowKey }} }
            return rows
        }
    /**
     * Represents a mapping of column keys to their associated lists of mutable cells.
     *
     * Each column key is mapped to a filtered list of cells belonging to that specific column.
     * This allows quick access and organization of all the cells within a particular column.
     *
     * Note that the cell lists provided are mutable, enabling modifications to the underlying data structure.
     *
     * @see columnKeys
     * @see cells
     * @since 1.0.0
     */
    val columns: MultiMap<C, MCell<R, C, V?>>
        get() {
            val columns = mutableMapOf<C, List<MCell<R, C, V?>>>()
            columnKeys.forEach { columnKey -> columns.getOrPut(columnKey) { cells.filter { it.columnKey == columnKey }} }
            return columns
        }
    /**
     * Returns the first cell in the table, or `null` if the table is empty.
     *
     * This property provides access to the first mutable cell within the `MutableTable`.
     * The cell contains information such as row key, column key, and associated value,
     * if any exist. If the table has no cells, the result will be `null`.
     *
     * @return The first [MCell] of type `<R, C, V?>`, or `null` if there are no cells.
     * @since 1.0.0
     */
    val firstCell: MCell<R, C, V?>?
        get() = cells.firstOrNull()
    /**
     * Retrieves the last cell in the table, or `null` if the table is empty.
     * This function provides read-only access to the last element in the collection of cells.
     *
     * @return The last cell in the table or `null` if no cells are present.
     * @since 1.0.0
     */
    val lastCell: MCell<R, C, V?>?
        get() = cells.lastOrNull()
    /**
     * Retrieves a filtered list of mutable table cells belonging to the first column in the table.
     * The first column is determined based on the column key of the first cell in the table.
     * If the table is empty or `firstCell` is null, the result will be null.
     *
     * @return A list of cells in the first column of the table, or null if no such column exists.
     * @since 1.0.0
     */
    val firstColumn: List<MCell<R, C, V?>>
        get() = cells.filter { it.columnKey == firstCell?.columnKey }
    /**
     * Retrieves the list of all mutable cells located in the first row of the table.
     * The first row is identified based on the row key of the first cell in the table, if it exists.
     * If the table is empty or the first cell is null, the result will be null.
     *
     * @return A list of mutable cells belonging to the first row, or null if the table is empty.
     * @since 1.0.0
     */
    val firstRow: List<MCell<R, C, V?>>
        get() = cells.filter { it.rowKey == firstCell?.rowKey }
    /**
     * Retrieves a list of all mutable cells that belong to the last column in the table.
     * The last column is identified based on the column key of the `lastCell` property.
     *
     * If there is no `lastCell` or if the table has no cells, the returned value is `null`.
     *
     * @return A nullable list containing all mutable cells that reside in the last column; `null` if no last column exists.
     * @since 1.0.0
     */
    val lastColumn: List<MCell<R, C, V?>>
        get() = cells.filter { it.columnKey == lastCell?.columnKey }
    /**
     * Represents the last row in the mutable table. This property retrieves all mutable cells
     * that belong to the same row as the last cell (`lastCell`) in the table, based on the row key.
     * If no cells are present, the value will be null.
     *
     * Note: The determination of the last row is tied directly to the `lastCell` property of the table.
     *
     * @return A list of all mutable cells (`MutableCell`) in the last row, or null if there are no cells.
     * @since 1.0.0
     */
    val lastRow: List<MCell<R, C, V?>>
        get() = cells.filter { it.rowKey == lastCell?.rowKey }
    /**
     * A property providing a mapping of each row in the table to its first cell.
     *
     * This property retrieves the first cell of each row as a map where the keys are the row identifiers,
     * and the values are the first cell within each corresponding row. The cells are ordered based on the
     * internal row structure of the table.
     *
     * @return A map containing the first cell for each row in the table.
     * @since 1.0.0
     */
    val firstCellOfRows: Map<R, MCell<R, C, V?>>
        get() =  rows.mapValues { it.value.first() }
    /**
     * Provides a map representing the first cell of each column in the table.
     *
     * The keys of the map correspond to the column identifiers, and the values
     * represent the first cell in each respective column. This allows access
     * to the leading entries of every column efficiently.
     *
     * @return A map where each entry consists of a column key and its respective
     * first mutable cell in the column.
     * @since 1.0.0
     */
    val firstCellOfColumns: Map<C, MCell<R, C, V?>>
        get() = columns.mapValues { it.value.first() }
    /**
     * Represents a map containing the last cell of each column in a mutable table.
     *
     * Each entry in the map corresponds to a column in the table, where the key is the column identifier
     * and the value is the last cell in that column as a `MutableCell`.
     *
     * This property provides a quick way to retrieve the last cell from each column without
     * manually iterating over all cells of the table.
     *
     * @return A map where the keys are column identifiers and the values are the last cells in those columns.
     * @since 1.0.0
     */
    val lastCellOfColumns: Map<C, MCell<R, C, V?>>
        get() = columns.mapValues { it.value.last() }
    /**
     * A computed property that retrieves the last cell of each row in the mutable table.
     *
     * This property returns a map where each key is a row key, and the corresponding value
     * is the last cell of that row. The last cell is determined based on the natural order
     * of column keys within a given row.
     *
     * @return a map associating each row key with its respective last cell.
     * @since 1.0.0
     */
    val lastCellOfRows: Map<R, MCell<R, C, V?>>
        get() = rows.mapValues { it.value.last() }

    init {
        cells = recheckEntries(entries.toMutableList())
    }

    companion object {
        class Serializer : ValueSerializer<MTable<Any, Any, Any?>>() {
            override fun serialize(
                value: MTable<Any, Any, Any?>,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartArray()
                value.cells.forEach {
                    gen.writeStartObject()
                    gen.writePOJOProperty("rowKey", it.rowKey)
                    gen.writePOJOProperty("columnKey", it.columnKey)
                    gen.writePOJOProperty("value", it.value)
                    gen.writeEndObject()
                }
                gen.writeEndArray()
            }
        }

        class Deserializer : ValueDeserializer<MTable<Any, Any, Any?>>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): MTable<Any, Any, Any?> {
                val entries = mutableListOf<MCell<Any, Any, Any?>>()
                p.objectReadContext().readTree<ObjectNode>(p).forEach { node ->
                    entries.add(
                        MCell(
                            if (node.get("rowKey").toString().startsWith("\"") && node.get("rowKey").toString().endsWith("\"")) node.get("rowKey").toString()[1..<node.get("rowKey").toString().length - 1] else node.get("rowKey").toString(),
                            if (node.get("columnKey").toString().startsWith("\"") && node.get("columnKey").toString().endsWith("\"")) node.get("columnKey").toString()[1..<node.get("columnKey").toString().length - 1] else node.get("columnKey").toString(),
                            if (node.get("value").toString().startsWith("\"") && node.get("value").toString().endsWith("\"")) node.get("value").toString()[1..<node.get("value").toString().length - 1] else (if (node.get("value").toString() == "null") null else node.get("value").toString())
                        )
                    )
                }
                return MTable(entries)
            }
        }

        class OldSerializer : JsonSerializer<MTable<Any, Any, Any?>>() {
            override fun serialize(value: MTable<Any, Any, Any?>, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartArray()
                value.cells.forEach {
                    gen.writeStartObject()
                    gen.writeObjectField("rowKey", it.rowKey)
                    gen.writeObjectField("columnKey", it.columnKey)
                    gen.writeObjectField("value", it.value)
                    gen.writeEndObject()
                }
                gen.writeEndArray()
            }
        }

        class OldDeserializer : JsonDeserializer<MTable<Any, Any, Any?>>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): MTable<Any, Any, Any?> {
                val entries = mutableListOf<MCell<Any, Any, Any?>>()
                p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p).forEach { node ->
                    entries.add(
                        MCell(
                            if (node.get("rowKey").toString().startsWith("\"") && node.get("rowKey").toString().endsWith("\"")) node.get("rowKey").toString()[1..<node.get("rowKey").toString().length - 1] else node.get("rowKey").toString(),
                            if (node.get("columnKey").toString().startsWith("\"") && node.get("columnKey").toString().endsWith("\"")) node.get("columnKey").toString()[1..<node.get("columnKey").toString().length - 1] else node.get("columnKey").toString(),
                            if (node.get("value").toString().startsWith("\"") && node.get("value").toString().endsWith("\"")) node.get("value").toString()[1..<node.get("value").toString().length - 1] else (if (node.get("value").toString() == "null") null else node.get("value").toString())
                        )
                    )
                }
                return MTable(entries)
            }
        }
    }

    /**
     * Rechecks the given list of mutable cells by removing duplicates, sorting them based on their row and column keys,
     * and ensuring that all row and column combinations are present with null values for missing combinations.
     *
     * @param entries A mutable list of mutable cells representing the current table state.
     * @return A mutable list of mutable cells after rechecking and modifications.
     * @since 1.0.0
     */
    private fun recheckEntries(entries: MutableList<MCell<R, C, V?>>): MutableList<MCell<R, C, V?>> {
        val _entries = entries.toMutableList()
        val _entries2 = entries.toMutableList()
        val alreadyTaken = mutableListOf<MCell<R, C, V?>>()
        _entries2.forEach { cell ->
            val old: MCell<R, C, V?>? = alreadyTaken.forEachWithReturn { if (it.sameKeys(cell)) breakLoop(it) }
            if (old.isNotNull()) _entries.remove(old)
            alreadyTaken.add(cell)
        }
        val sortedEntries = _entries.sortedWith(
            compareBy<MCell<R, C, V?>> { it.rowKey.toString() }
                .thenBy { it.columnKey.toString() }
        )
        _entries.clear()
        _entries.addAll(sortedEntries)
        val groupedByRow = _entries.groupedBy { it.rowKey }
        val allColumnsKeys = _entries.groupedBy { it.columnKey }.keys
        allColumnsKeys.forEach { columnKey ->
            groupedByRow.forEach { (rowKey, cells) ->
                val isPresent: Boolean = cells.forEachWithReturn { if (it.columnKey == columnKey) breakLoop(true) } ?: false
                if (!isPresent) _entries.add(MCell(rowKey, columnKey, null))
            }
        }
        return _entries
    }

    /**
     * Creates an immutable copy of the provided mutable table.
     *
     * This method transforms the mutable table into an immutable representation, preserving
     * the structure and contents of the original table. The returned object reflects the
     * current state of the input table but cannot be modified.
     *
     * @param table the mutable table to be copied
     * @return an immutable table containing the same data as the input table
     * @since 1.0.0
     */
    fun copyOf(table: MTable<R, C, V?>): Table<R, C, V?> = Table(table.cells.mappedTo { it.toCell() })

    /**
     * Creates a mutable copy of the provided table.
     *
     * @param table the original MutableTable to copy from
     * @return a new MutableTable containing the same cells as the original
     * @since 1.0.0
     */
    fun mutableCopyOf(table: MTable<R, C, V?>): MTable<R, C, V?> = MTable(table.cells)

    /**
     * Checks whether the collection of cells is empty.
     *
     * This method evaluates if the `cells` collection contains no elements.
     *
     * @return `true` if the collection is empty, otherwise `false`.
     * @since 1.0.0
     */
    override fun isEmpty(): Boolean = cells.isEmpty()

    // empty and null checks as extension

    /**
     * Returns an iterator over the mutable cells of the table.
     *
     * @return an Iterator over MutableCell objects representing the rows, columns, and values of the table.
     * @since 1.0.0
     */
    override fun iterator(): Iterator<MCell<R, C, V?>> = cells.iterator()

    /**
     * Checks if the specified cell is present in the collection of cells.
     *
     * @param element the cell to check for existence in the collection
     * @return true if the cell is found, false otherwise
     * @since 1.0.0
     */
    override operator fun contains(element: MCell<R, C, V?>) = cells.any { element == it }
    /**
     * Checks if the specified cell is present in the collection of cells.
     *
     * @param element the cell to check for existence in the collection
     * @return true if the cell is found, false otherwise
     * @since 1.0.0
     */
    operator fun contains(element: Cell<R, C, V?>) = cells.any { element == it }
    /**
     * Checks if the provided cell exists within the collection of cells.
     *
     * @param cell the Triple containing row, column, and value to check for existence
     * @return true if the specified cell is found, false otherwise
     * @since 1.0.0
     */
    operator fun contains(cell: Triple<R, C, V?>) = cells.any { Cell(cell) == it }
    /**
     * Checks if the cell defined by the given row key and column key exists in the collection of cells.
     *
     * @param rowKey the key identifying the row to search for
     * @param columnKey the key identifying the column to search for
     * @return true if a cell with the specified row key and column key exists, false otherwise
     * @since 1.0.0
     */
    fun contains(rowKey: R, columnKey: C) = cells.any { it.rowKey == rowKey && it.columnKey == columnKey }
    /**
     * Checks if a specific pair of keys (row and column) is contained within the cells.
     *
     * @param keys A pair consisting of the row key and column key to search for.
     * @return `true` if the pair of keys is found in the cells, otherwise `false`.
     * @since 1.0.0
     */
    operator fun contains(keys: Pair<R, C>) = cells.any { it.rowKey == keys.first && it.columnKey == keys.second }

    /**
     * Checks if the current collection contains all elements from the specified collection.
     *
     * @param elements The collection of elements to check for presence in the current collection.
     * @return `true` if all elements are present, `false` otherwise.
     * @since 1.0.0
     */
    override fun containsAll(elements: Collection<MCell<R, C, V?>>) = cells.containsAll(elements)

    /**
     * Retrieves the first cell of the specified row.
     *
     * @param rowKey the key of the row for which the first cell is to be retrieved
     * @return the first cell of the given row, or null if the row or cell does not exist
     * @since 1.0.0
     */
    fun getFirstCellOfRow(rowKey: R): MCell<R, C, V?>? = firstCellOfRows[rowKey]

    /**
     * Retrieves the last cell of the specified row.
     *
     * @param rowKey The key of the row for which the last cell is to be retrieved.
     * @return The last cell of the specified row, or null if the row does not exist or is empty.
     * @since 1.0.0
     */
    fun getLastCellOfRow(rowKey: R): MCell<R, C, V?>? = lastCellOfRows[rowKey]

    /**
     * Retrieves the first cell of the specified column.
     *
     * @param columnKey the key identifying the column whose first cell is to be returned
     * @return the first cell of the specified column, or null if the column does not exist or is empty
     * @since 1.0.0
     */
    fun getFirstCellOfColumn(columnKey: C): MCell<R, C, V?>? = firstCellOfColumns[columnKey]

    /**
     * Retrieves the last cell associated with the specified column key.
     *
     * @param columnKey The key of the column whose last cell is to be retrieved.
     * @return The last cell of the specified column, or null if the columnKey does not exist.
     * @since 1.0.0
     */
    fun getLastCellOfColumn(columnKey: C): MCell<R, C, V?>? = lastCellOfColumns[columnKey]

    /**
     * Retrieves the row associated with the specified key from the dataset.
     *
     * @param rowKey The key identifying the row to retrieve.
     * @return A list of cells corresponding to the requested row, or null if the key does not exist.
     * @since 1.0.0
     */
    fun getRow(rowKey: R): List<MCell<R, C, V?>>? = rows[rowKey]

    /**
     * Retrieves the column associated with the specified column key.
     *
     * The method returns a list of cells corresponding to the specified column,
     * or null if the column key is not present in the table.
     *
     * @param columnKey the key identifying the column to retrieve
     * @return a list of cells in the specified column, or null if the column key is not found
     * @since 1.0.0
     */
    fun getColumn(columnKey: C): List<MCell<R, C, V?>>? = columns[columnKey]

    /**
     * Retrieves the value associated with the specified row key.
     *
     * @param rowKey the key of the row to retrieve
     * @return the value associated with the specified row key
     * @since 1.0.0
     */
    operator fun get(rowKey: R) = getRow(rowKey)
    /**
     * Retrieves the value corresponding to the specified row and column keys.
     *
     * @param rowKey the key representing the row
     * @param columnKey the key representing the column
     * @return the value associated with the given row and column keys, or null if no such value exists
     * @since 1.0.0
     */
    operator fun get(rowKey: R, columnKey: C): V? = cells.find { it.rowKey == rowKey && it.columnKey == columnKey }.let { it?.value }
    /**
     * Retrieves the value associated with the specified pair of row and column keys in the table.
     *
     * @param keys A pair consisting of the row key and the column key for which the value is to be retrieved.
     * @return The value corresponding to the specified row and column keys, or null if no value is present for the keys.
     * @since 1.0.0
     */
    operator fun get(keys: Pair<R, C>): V? = cells.find { it.rowKey == keys.first && it.columnKey == keys.second }.let { it?.value }

    /**
     * Retrieves a mutable cell from the table that matches the specified row key, column key, and value.
     * If no such cell is found, returns null.
     *
     * @param rowKey the key identifying the row of the cell to retrieve
     * @param columnKey the key identifying the column of the cell to retrieve
     * @param value the value of the cell to match, or null to match cells with a null value
     * @return a [MCell] that matches the provided row key, column key, and value, or null if no such cell exists
     * @since 1.0.0
     */
    fun getCell(rowKey: R, columnKey: C, value: V?): MCell<R, C, V?>? = cells.find { it.rowKey == rowKey && it.columnKey == columnKey && it.value == value }
    /**
     * Retrieves a mutable cell from the table that matches the specified row key and column key.
     *
     * @param rowKey the key representing the row in which the target cell is located
     * @param columnKey the key representing the column in which the target cell is located
     * @return the mutable cell corresponding to the provided row and column keys, or null if no such cell exists
     * @since 1.0.0
     */
    fun getCell(rowKey: R, columnKey: C): MCell<R, C, V?>? = cells.find { it.rowKey == rowKey && it.columnKey == columnKey }
    /**
     * Retrieves the mutable cell from the table that corresponds to the specified row and column keys.
     *
     * @param keys a pair containing the row key and column key to identify the desired cell
     * @return the mutable cell corresponding to the specified keys if found, or null otherwise
     * @since 1.0.0
     */
    fun getCell(keys: Pair<R, C>): MCell<R, C, V?>? = cells.find { it.rowKey == keys.first && it.columnKey == keys.second }

    /**
     * Retrieves all the column-value pairs for a given row key.
     *
     * @param rowKey the key of the row to retrieve data for
     * @return a map where keys are column keys and values are the corresponding values in that row; values may be null
     * @since 1.0.0
     */
    fun getFromRow(rowKey: R): Map<C, V?> = cells.filter { it.rowKey == rowKey }.associate { it.columnKey to it.value }

    /**
     * Retrieves a non-null mapping of columns to values for a given row key.
     * Filters out any entries with null values from the result.
     *
     * @param rowKey The key identifying the row from which the data is retrieved.
     * @return A map of column keys to non-null values for the specified row key.
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    fun getFromRowNotNull(rowKey: R): Map<C, V> = getFromRow(rowKey).filterValues { it.isNotNull() } as Map<C, V>

    /**
     * Retrieves a map of row keys to their corresponding values for a given column key.
     *
     * @param columnKey The key of the column to retrieve data from.
     * @return A map where the keys are row keys and the values are the corresponding values in the specified column.
     *         The values may include `null` if no value is associated with a given row key.
     * @since 1.0.0
     */
    fun getFromColumn(columnKey: C): Map<R, V?> = cells.filter { it.columnKey == columnKey }.associate { it.rowKey to it.value }

    /**
     * Retrieves a map of row keys to non-null values from the specified column.
     * The column is identified by the provided column key.
     * This method filters out any entries where the value is `null`.
     *
     * @param columnKey the key of the column from which to retrieve the map.
     * @return a map where each entry consists of a row key mapped to a non-null value from the specified column.
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    fun getFromColumnNotNull(columnKey: C): Map<R, V> = getFromColumn(columnKey).filterValues { it.isNotNull() } as Map<R, V>

    /**
     * Associates the specified value with the specified pair of keys in this structure.
     * If the structure previously contained a mapping for the pair of keys,
     * the old value is replaced by the specified value.
     *
     * @param keys a pair of keys where the first element represents the row key
     *             and the second element represents the column key.
     * @param value the value to be associated with the specified pair of keys,
     *              or null if the mapping is to be removed.
     * @since 1.0.0
     */
    operator fun set(keys: Pair<R, C>, value: V?) = put(MCell(keys.first, keys.second, value))
    /**
     * Sets the value at the specified row and column key. If a value already exists at the specified
     * position, it will be replaced with the new value.
     *
     * @param rowKey the key representing the row where the value is to be set
     * @param columnKey the key representing the column where the value is to be set
     * @param value the value to be set at the specified row and column key; can be null
     * @since 1.0.0
     */
    operator fun set(rowKey: R, columnKey: C, value: V?) = put(MCell(rowKey, columnKey, value))

    /**
     * Returns a string representation of the object.
     * Typically, this representation is derived from converting the object
     * into a table format and subsequently converting the table to a string.
     *
     * @return A string representation of the object.
     * @since 1.0.0
     */
    override fun toString(): String = toTable().toString()

    /**
     * Compares this object with the specified object for equality.
     *
     * @param other The object to be compared with this instance for equality.
     * @return `true` if the specified object is equal to this instance, `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Table<*, *, *>

        return cells == other.cells
    }

    /**
     * Computes the hash code for the table based on its cells.
     *
     * @return the hash code of the table, derived from the hash code of its cells
     * @since 1.0.0
     */
    override fun hashCode(): Int = cells.hashCode()

    /**
     * Inserts or updates a value in the table associated with the specified row and column keys.
     *
     * @param rowKey the key identifying the row where the value should be placed
     * @param columnKey the key identifying the column where the value should be placed
     * @param value the value to associate with the specified row and column keys; may be null
     * @since 1.0.0
     */
    fun put(rowKey: R, columnKey: C, value: V? = null) {
        cells.add(MCell(rowKey, columnKey, value))
        cells = recheckEntries(cells)
    }
    /**
     * Adds or updates the value in the table at the specified row and column keys, using the provided cell.
     *
     * @param cell the cell containing the row key, column key, and value to be added or updated in the table
     * @since 1.0.0
     */
    fun put(cell: CellInterface<R, C, V?>) = put(cell.rowKey, cell.columnKey, cell.value)
    /**
     * Adds or updates a value in the table at the specified row and column keys provided as a pair.
     *
     * @param pair A pair containing the row key and column key where the value will be inserted or updated.
     * @param value The value to be associated with the specified row and column keys. Defaults to null if not provided.
     * @since 1.0.0
     */
    fun put(pair: Pair<R, C>, value: V? = null) = put(pair.first, pair.second, value)
    /**
     * Inserts a value into the table based on the specified row key, column key, and value encapsulated in a Triple.
     * This is a shorthand for calling the primary `put` method with individual parameters.
     *
     * @param triple A Triple containing the row key, column key, and value to insert into the table.
     * @since 1.0.0
     */
    fun put(triple: Triple<R, C, V?>) = put(triple.first, triple.second, triple.third)

    /**
     * Copies all the cell entries from the provided table into the current table.
     * If a cell in the provided table matches a cell already present in the current table,
     * its value will be overwritten in the current table.
     *
     * @param table The source table containing the cells to be added to the current table.
     * @since 1.0.0
     */
    fun putAll(table: Table<R, C, V?>) {
        table.cells.forEach { put(it.rowKey, it.columnKey, it.value) }
    }
    /**
     * Inserts all the specified cells into the structure. Each cell is defined by its row key,
     * column key, and the associated value. If a cell with the same row and column key already
     * exists, its value will be updated.
     *
     * @param cells an iterable collection of cells to be added, where each cell contains
     * row key, column key, and a nullable value
     * @since 1.0.0
     */
    fun putAll(cells: Iterable<CellInterface<R, C, V?>>) = cells.forEach { put(it.rowKey, it.columnKey, it.value) }
    /**
     * Copies all entries from the provided table into this table.
     *
     * @param table the mutable table whose entries will be added to this table.
     * @since 1.0.0
     */
    fun putAll(table: MTable<R, C, V?>) {
        table.cells.forEach { put(it.rowKey, it.columnKey, it.value) }
    }

    /**
     * Adds the given cell's value to the data structure using its row key and column key.
     *
     * @param cell The cell containing the row key, column key, and the value to be added.
     * @since 1.0.0
     */
    operator fun plusAssign(cell: CellInterface<R, C, V?>) = put(cell.rowKey, cell.columnKey, cell.value)
    /**
     * Adds the provided pair of key and value to the collection.
     *
     * @param pair A pair consisting of a key of type R and a value of type C to be added.
     * @since 1.0.0
     */
    operator fun plusAssign(pair: Pair<R, C>) = put(pair.first, pair.second)
    /**
     * Adds a value to the collection using the specified triple of row, column, and value.
     *
     * @param triple A triple containing the row, column, and value to be added.
     *               The first element represents the row, the second element represents the column,
     *               and the third element represents the value to be inserted or updated.
     * @since 1.0.0
     */
    operator fun plusAssign(triple: Triple<R, C, V?>) = put(triple.first, triple.second, triple.third)
    /**
     * Combines the entries of the current table with the entries from the provided table.
     * This operation effectively adds all key-value pairs from the specified table into
     * the current table, potentially overwriting any existing entries with matching keys.
     *
     * @param table The table containing entries to be added to the current table.
     * @since 1.0.0
     */
    operator fun plusAssign(table: Table<R, C, V?>) = putAll(table)
    /**
     * Adds all the elements from the given iterable to the current collection.
     *
     * This operator function provides a convenient way to combine the elements of the current
     * collection with the provided iterable by invoking the `putAll` method.
     *
     * @param cells an iterable collection of `CellInterface` instances to be added to the current collection.
     * @since 1.0.0
     */
    operator fun plusAssign(cells: Iterable<CellInterface<R, C, V?>>) = putAll(cells)
    /**
     * Merges the contents of the given MutableTable into the current table.
     * All entries from the provided table will be added to this table, replacing existing entries with matching keys.
     *
     * @param table the MutableTable containing the entries to be added to this table
     * @since 1.0.0
     */
    operator fun plusAssign(table: MTable<R, C, V?>) = putAll(table)

    /**
     * Removes a cell from the table that matches the provided row and column keys.
     * The internal list of cells is updated to ensure consistency after the removal.
     *
     * @param rowKey the row key of the cell to be removed
     * @param columnKey the column key of the cell to be removed
     * @since 1.0.0
     */
    fun remove(rowKey: R, columnKey: C) {
        cells.removeIf { it.rowKey == rowKey && it.columnKey == columnKey }
        cells = recheckEntries(cells)
    }
    /**
     * Removes the specified cell from the table by using its row key and column key.
     *
     * This method locates the cell in the table using the provided cell's row key and
     * column key, and removes it. If no cell matching the row key and column key is found,
     * no action is performed.
     *
     * @param cell The cell to be removed, containing the row key, column key, and optionally a value.
     * @since 1.0.0
     */
    fun remove(cell: CellInterface<R, C, V?>) = remove(cell.rowKey, cell.columnKey)
    /**
     * Removes an entry identified by the specified pair of keys from the collection.
     *
     * @param pair A pair of keys where the first element corresponds to the row key
     *             and the second element corresponds to the column key of the entry to be removed.
     * @since 1.0.0
     */
    fun remove(pair: Pair<R, C>) = remove(pair.first, pair.second)

    /**
     * Removes all the entries in the specified table from this table.
     *
     * This method iterates over the cells of the given table and removes the corresponding entries
     * from this table. After removing, it rechecks and updates the internal cell list.
     *
     * @param table the table containing the entries to be removed from this table
     * @since 1.0.0
     */
    fun removeAll(table: Table<R, C, V?>) {
        table.cells.forEach { remove(it.rowKey, it.columnKey) }
        cells = recheckEntries(cells)
    }
    /**
     * Removes all entries from the data structure that are represented by the given collection of cells.
     *
     * @param cells The collection of cells to be removed, where each cell provides the row key and column key of the entries to be removed.
     * @since 1.0.0
     */
    fun removeAll(cells: Iterable<CellInterface<R, C, V?>>) = cells.forEach { remove(it.rowKey, it.columnKey) }
    /**
     * Removes all entries from the given mutable table by iterating through its cells
     * and invoking the `remove` method for each cell.
     *
     * The method also recalculates and updates the list of cells in the table
     * after completing the removal of entries.
     *
     * @param table the mutable table from which all entries are to be removed
     * @since 1.0.0
     */
    fun removeAll(table: MTable<R, C, V?>) {
        table.cells.forEach { remove(it.rowKey, it.columnKey) }
        cells = recheckEntries(cells)
    }

    /**
     * Removes a cell identified by its row key and column key from the current structure.
     *
     * @param cell The cell to be removed, providing its row key and column key.
     * @since 1.0.0
     */
    operator fun minusAssign(cell: CellInterface<R, C, V?>) = remove(cell.rowKey, cell.columnKey)
    /**
     * Removes the element specified by the given pair of row and column from the collection.
     *
     * @param pair A pair consisting of the row (`R`) and column (`C`) of the element to be removed.
     * @since 1.0.0
     */
    operator fun minusAssign(pair: Pair<R, C>) = remove(pair.first, pair.second)
    /**
     * Subtracts the elements of the specified table from the current table.
     * Removes all entries from the current table that are also present in the given table.
     *
     * @param table the table whose elements are to be removed from the current table
     * @since 1.0.0
     */
    operator fun minusAssign(table: Table<R, C, V?>) = removeAll(table)
    /**
     * Subtracts the contents of the specified [table] from this table by removing all matching entries.
     *
     * @param table the table whose entries are to be removed from this table
     * @since 1.0.0
     */
    operator fun minusAssign(table: MTable<R, C, V?>) = removeAll(table)
    /**
     * Removes all elements in the given iterable from the collection.
     *
     * @param cells An iterable collection of elements to be removed.
     * @since 1.0.0
     */
    operator fun minusAssign(cells: Iterable<CellInterface<R, C, V?>>) = removeAll(cells)

    /**
     * Removes all elements from the collection or container, effectively clearing it.
     * This method empties the underlying storage, leaving it without any data.
     *
     * @since 1.0.0
     */
    fun clear() {
        cells.clear()
    }

    /**
     * Converts a collection of cells into a table structure.
     *
     * This method processes a collection of cells and transforms them into a `Table` object containing the specified rows, columns, and values.
     *
     * @return A `Table` containing the rows (`R`), columns (`C`), and nullable values (`V?`) mapped from the provided cells.
     * @since 1.0.0
     */
    fun toTable(): Table<R, C, V?> = Table(cells.mappedTo { it.toCell() })
}

/**
 * Creates an empty table with rows, columns, and corresponding values.
 *
 * @return A new instance of an empty [Table] with rows of type [R], columns of type [C],
 *         and values of type [V?].
 * @since 1.0.0
 */
fun <R, C, V> tableOf(): Table<R, C, V?> = Table(emptyList<Cell<R, C, V?>>())
/**
 * Constructs a table from the provided cell entries.
 *
 * This method takes a vararg parameter of `CellInterface` types and returns an
 * immutable `Table` containing the entries. Each entry is converted into a `Cell`.
 *
 * @param R The type of the row key.
 * @param C The type of the column key.
 * @param V The type of the value stored in the cell.
 * @param entries The cell entries to initialize the table with.
 * @return A `Table` containing the provided cell entries.
 * @since 1.0.0
 */
fun <R, C, V> tableOf(vararg entries: CellInterface<R, C, V?>): Table<R, C, V?> = Table(entries.toList().mappedTo { it.toCell() })
/**
 * Creates a table from the provided iterable of CellInterface instances.
 *
 * @param R the type of the row keys in the table.
 * @param C the type of the column keys in the table.
 * @param V the type of the values in the table, can be nullable.
 * @param entries an iterable collection of CellInterface objects representing the rows, columns, and values for the table.
 * @return a Table object constructed from the given entries.
 * @since 1.0.0
 */
fun <R, C, V> tableOf(entries: Iterable<CellInterface<R, C, V?>>): Table<R, C, V?> = Table(entries.toList().mappedTo { it.toCell() })

/**
 * Creates and returns an empty mutable table.
 *
 * @return A new instance of MutableTable with no elements.
 * @since 1.0.0
 */
fun <R, C, V> mTableOf(): MTable<R, C, V?> = MTable(emptyList<MCell<R, C, V?>>())
/**
 * Creates a new mutable table from the provided cell entries.
 * Each entry represents a cell in the table containing a row key, column key, and value.
 *
 * @param R The type of the row keys in the table.
 * @param C The type of the column keys in the table.
 * @param V The type of the values in the table. The values can be nullable.
 * @param entries A variable number of cell entries implementing the [CellInterface],
 *                which define the rows, columns, and values to initialize the table.
 * @return A new instance of [MTable] containing the given entries, where each cell is mutable.
 * @since 1.0.0
 */
fun <R, C, V> mTableOf(vararg entries: CellInterface<R, C, V?>): MTable<R, C, V?> = MTable(entries.toList().mappedTo { it.toMCell() })
/**
 * Creates a new instance of a mutable table using the specified iterable collection of cell entries.
 *
 * This method constructs a `MutableTable` by converting provided cell entries into their mutable counterparts.
 * It can be used to initialize a mutable table with predefined cells.
 *
 * @param entries an iterable collection of cell entries represented by instances of `CellInterface`,
 * where each cell contains a row key, column key, and a nullable value.
 * @return a mutable table (`MutableTable`) containing the provided cells as mutable entries.
 * @since 1.0.0
 */
fun <R, C, V> mTableOf(entries: Iterable<CellInterface<R, C, V?>>): MTable<R, C, V?> = MTable(entries.toList().mappedTo { it.toMCell() })

/**
 * Converts a nested map structure into a `Table` representation.
 * The outer map's keys are treated as row keys, the inner map's keys as column keys,
 * and the inner map's values as the table cell values.
 *
 * @return a `Table` containing all entries of the nested map structure,
 * preserving the mapping of rows, columns, and their corresponding values.
 * @since 1.0.0
 */
fun <R, C, V> Map<R, Map<C, V>>.toTable(): Table<R, C, V?> {
    val list = mutableListOf<Cell<R, C, V?>>()
    forEach { (rowKey, columnMap) -> columnMap.forEach { (columnKey, value) -> list.add(Cell(rowKey, columnKey, value)) } }
    return Table(list)
}
/**
 * Converts a nested map structure into a mutable table.
 *
 * This extension function transforms a map of maps into a new mutable table instance,
 * preserving the row and column key mappings along with their associated values.
 * The original data remains unaltered, and the resulting table provides mutability
 * for its contained cells, allowing modifications to both structure and data.
 *
 * @receiver The map of maps to be converted into a mutable table.
 * @return A MutableTable containing the same data as the original map of maps.
 * @since 1.0.0
 */
fun <R, C, V> Map<R, Map<C, V>>.toMTable() = toTable().toMTable()

/**
 * Creates an empty table without any rows, columns, or values.
 *
 * This function is a shorthand for creating an empty [Table] with specified generic types.
 * The resulting table will have a nullable value type.
 *
 * @return A new instance of an empty [Table] with rows of type [R], columns of type [C],
 *         and values of type [V?].
 * @since 1.0.0
 */
fun <R, C, V> emptyTable() = tableOf<R, C, V?>()
/**
 * Creates and returns an empty mutable table.
 *
 * The returned table is a mutable structure that supports insertion, modification, and retrieval
 * of elements, organized into rows and columns. The table is initially empty, with no rows,
 * columns, or values.
 *
 * @return A new instance of a mutable table with no pre-existing elements.
 * @since 1.0.0
 */
fun <R, C, V> emptyMTable() = mTableOf<R, C, V?>()

/**
 * Checks if the table is empty.
 *
 * @return true if the table contains no cells, false otherwise.
 * @since 1.0.0
 */
val Table<*, *, *>.isEmpty: Boolean
    get() = cells.isEmpty()
/**
 * Checks if the current object is not empty.
 *
 * This method evaluates the condition by negating the result of `isEmpty()`
 * and returns a boolean indicating whether the object contains elements or not.
 *
 * @return `true` if the object is not empty, `false` otherwise
 * @since 1.0.0
 */
val Table<*, * ,*>.isNotEmpty get() = !isEmpty()

/**
 * Checks if a given Table instance is not null and not empty.
 *
 * @receiver a nullable `Table<R, C, V>` instance
 * @return true if the Table instance is not null and contains elements, false otherwise.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
@Suppress("kutils_null_check")
val Table<*, *, *>?.isNotNullOrEmpty: Boolean
    get() {
        contract {
            returns(true) implies (this@isNotNullOrEmpty != null)
        }
        return isNotNull() && this.isNotEmpty
    }
/**
 * Checks if a nullable `Table<R, C, V>` is either null or empty.
 *
 * This method evaluates whether the table is null or contains no entries.
 * Returns `true` if the table is null or empty, and `false` otherwise.
 *
 * @receiver a nullable `Table<R, C, V>` instance
 * @return `true` if the table is null or has no entries, otherwise `false`
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
@Suppress("kutils_null_check")
val Table<*, *, *>?.isNullOrEmpty: Boolean
    get() {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }
    return isNull() || this.isEmpty
}

/**
 * Checks if the Table instance is either `null` or empty.
 *
 * This function uses an operator convention to provide syntactic sugar
 * for verifying whether the Table instance is in a non-usable state.
 * A `null` or empty Table will return `true`, while a non-empty Table will return `false`.
 *
 * @return `true` if the Table is `null` or empty, `false` otherwise.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
@Suppress("kutils_null_check")
operator fun <R, C, V> Table<R, C, V>?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNull() || this.isEmpty
}

/**
 * Checks if the table is empty.
 *
 * @return true if the table contains no cells, false otherwise.
 * @since 1.0.0
 */
val MTable<*, *, *>.isEmpty: Boolean
    get() = cells.isEmpty()
/**
 * Checks if the current object is not empty.
 *
 * This method evaluates the condition by negating the result of `isEmpty()`
 * and returns a boolean indicating whether the object contains elements or not.
 *
 * @return `true` if the object is not empty, `false` otherwise
 * @since 1.0.0
 */
val MTable<*, * ,*>.isNotEmpty get() = !isEmpty()

/**
 * Checks if a given Table instance is not null and not empty.
 *
 * @receiver a nullable `Table<R, C, V>` instance
 * @return true if the Table instance is not null and contains elements, false otherwise.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
@Suppress("kutils_null_check")
val MTable<*, *, *>?.isNotNullOrEmpty: Boolean
    get() {
        contract {
            returns(true) implies (this@isNotNullOrEmpty != null)
        }
        return isNotNull() && this.isNotEmpty
    }
/**
 * Checks if a nullable `Table<R, C, V>` is either null or empty.
 *
 * This method evaluates whether the table is null or contains no entries.
 * Returns `true` if the table is null or empty, and `false` otherwise.
 *
 * @receiver a nullable `Table<R, C, V>` instance
 * @return `true` if the table is null or has no entries, otherwise `false`
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
@Suppress("kutils_null_check")
val MTable<*, *, *>?.isNullOrEmpty: Boolean
    get() {
        contract {
            returns(false) implies (this@isNullOrEmpty != null)
        }
        return isNull() || this.isEmpty
    }

/**
 * Checks if the Table instance is either `null` or empty.
 *
 * This function uses an operator convention to provide syntactic sugar
 * for verifying whether the Table instance is in a non-usable state.
 * A `null` or empty Table will return `true`, while a non-empty Table will return `false`.
 *
 * @return `true` if the Table is `null` or empty, `false` otherwise.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
@Suppress("kutils_null_check")
operator fun <R, C, V> MTable<R, C, V>?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNull() || this.isEmpty
}

/**
 * Returns the current table if it is not null, or an empty table if it is null.
 *
 * This function ensures that the result is non-null, providing a default empty table
 * in cases where the original table is null.
 *
 * @receiver The table that might be null.
 * @return The original table if it is non-null, otherwise an empty table.
 * @since 1.0.0
 */
fun <R, C, V> Table<R, C, V?>?.orEmpty() = this ?: emptyTable()
/**
 * Returns the current instance of a nullable `MutableTable<R, C, V?>` if it is not null,
 * or an empty `MutableTable<R, C, V?>` if the current instance is null.
 *
 * This function provides a safe way to work with nullable `MutableTable` instances,
 * ensuring that operations can continue without explicit null checks.
 *
 * @receiver a nullable `MutableTable<R, C, V?>` instance.
 * @return the current instance if it is not null, or an empty `MutableTable` if it is null.
 * @since 1.0.0
 */
fun <R, C, V> MTable<R, C, V?>?.orEmpty() = this ?: emptyMTable()

/**
 * Converts a collection of objects into a table representation where each object is represented as a row.
 * Each cell in the table is mapped from the properties of the objects in the collection.
 *
 * @param T the type of elements in the collection.
 * @param R the type of the row identifier.
 * @param rowProperty the property that uniquely identifies rows in the table.
 * @return a table representation containing rows and columns derived from the collection's elements.
 * @throws NoSuchPropertyException if the provided rowProperty is not found in the object's properties.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
inline fun <reified T : Any, R> Collection<T>.toTable(rowProperty: KProperty<R>): Table<R, String, Any?> {
    val properties = T::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .mappedTo { it as KProperty<T> }

    properties.any { it == rowProperty } || throw NoSuchPropertyException()

    val cellList = emptyMList<Cell<R, String, Any?>>()
    forEach { element ->
        val row = element.getPropertyValue<T, R>(rowProperty.name) ?: throw NoSuchPropertyException()
        element::class.memberProperties.forEach { property ->
            cellList += Cell(row, property.name, property.call(element))
        }
    }
    return tableOf(cellList.toList())
}

/**
 * Converts a collection of objects into a mutable table representation.
 *
 * Each object in the collection is represented as a row in the table.
 * Rows are identified using the specified rowProperty, and the table
 * consists of cells containing data derived from the elements' properties.
 *
 * @param T the type of elements in the collection.
 * @param R the type of the row identifier.
 * @param rowProperty the property used to uniquely identify the rows in the mutable table.
 * @return a mutable table representation containing rows and columns derived from the collection's elements.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
inline fun <reified T : Any, R> Collection<T>.toMTable(rowProperty: KProperty<R>) = toTable<T, R>(rowProperty).toMTable()