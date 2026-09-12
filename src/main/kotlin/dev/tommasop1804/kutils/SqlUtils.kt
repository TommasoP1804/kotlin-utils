/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("SqlUtilsKt")
@file:Suppress("unused")
@file:Since("5.4.1")
@file:MustUseReturnValues

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.JsonbColumnType.Companion.JSONB
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.classes.coding.Json.Companion.EMPTY_JSON
import dev.tommasop1804.kutils.classes.coding.Json.Companion.MAPPER
import dev.tommasop1804.kutils.classes.collections.*
import dev.tommasop1804.kutils.classes.collections.ResultRow
import dev.tommasop1804.kutils.classes.constants.*
import dev.tommasop1804.kutils.classes.identifiers.*
import dev.tommasop1804.kutils.exceptions.*
import org.intellij.lang.annotations.Language
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.core.statements.InsertStatement
import org.jetbrains.exposed.v1.core.statements.StatementType
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import org.jetbrains.exposed.v1.dao.InnerTableLink
import org.jetbrains.exposed.v1.dao.Referrers
import org.jetbrains.exposed.v1.exceptions.DuplicateColumnException
import org.jetbrains.exposed.v1.exceptions.LongQueryException
import org.jetbrains.exposed.v1.exceptions.UnsupportedByDialectException
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.transactions.transactionManager
import org.postgresql.util.PGobject
import tools.jackson.core.type.TypeReference
import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.sql.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.util.*
import kotlin.ranges.rangeTo
import kotlin.reflect.KClass
import kotlin.text.endsWith
import kotlin.toString

/**
 * Represents a database table with string-based primary keys.
 *
 * This class is designed to manage database tables where the primary key is a string column.
 * It allows configuration of column name, length, and collation.
 *
 * @constructor Creates a StringTable with the specified parameters.
 * @param name The name of the table. Defaults to an empty string.
 * @param columnName The name of the primary key column. Defaults to "id".
 * @param length The maximum length of the string column. Defaults to 255.
 * @param collate The collation applied to the column. Defaults to null, indicating no explicit collation.
 * @since 5.3.1
 * @author Tommaso Pastorelli
 */
open class StringTable(
    name: String = String.EMPTY,
    private val columnName: String = "id",
    private val length: Int = 255,
    private val collate: String? = null
) : IdTable<String>(name) {
    /**
     * Represents the unique identifier column for an entity in the database.
     * This column is defined as a `varchar` with a specified name, length, and collation.
     * The `entityId` function converts the `varchar` column into an `EntityID`, which is used as the primary key.
     * @since 5.3.1
     */
    override val id: Column<EntityID<String>> = varchar(columnName, length, collate).entityId()
    /**
     * Represents the primary key of the table.
     * Overrides the parent class's primary key definition.
     * It is initialized with the `id` column of the table.
     * @since 5.3.1
     */
    override val primaryKey = PrimaryKey(id)
}
/**
 * Represents an entity with a String-based primary key. This is an abstract class
 * intended to be extended by other entity classes that utilize a String as their ID type.
 *
 * @constructor Creates a new StringEntity with the specified primary key.
 * @param id The primary key of the entity, represented as an EntityID of type String.
 * @since 5.3.1
 * @author Tommaso Pastorelli
 */
abstract class StringEntity(id: EntityID<String>) : Entity<String>(id)
/**
 * Represents an abstract entity class where the primary key is of type String.
 * This class extends EntityClass and provides functionality to manage entities
 * with String-based primary keys.
 *
 * @param E The type of entity, which must inherit from StringEntity.
 * @param table The table associated with the entity type.
 * @param entityType The runtime class of the entity type, used for reflection. Defaults to null.
 * @param entityCtor A transformer function to create entity instances from EntityID objects. Defaults to null.
 * @since 5.3.1
 * @author Tommaso Pastorelli
 */
abstract class StringEntityClass<out E : StringEntity>(
    table: IdTable<String>,
    entityType: Class<E>? = null,
    entityCtor: Transformer<EntityID<String>, E>? = null
) : EntityClass<String, E>(table, entityType, entityCtor)

/**
 * Adds a VARCHAR column to the table with a specified name and optional collation.
 * The length of the VARCHAR column is fixed to 255 characters.
 *
 * @param name The name of the VARCHAR column.
 * @param collate An optional collation to apply to the column. Defaults to null.
 * @return The created VARCHAR column.
 * @since 5.3.1
 */
fun Table.varchar(name: String, collate: String? = null) = varchar(name, 255, collate)
/**
 * Maps a table column to a Kotlin Enum type using the enum's name for database storage.
 *
 * @param T The Enum type that this table column will map to.
 * @param name The name of the table column to map to the Enum type.
 * @since 5.3.5
 */
inline fun <reified T : Enum<T>> Table.enumerationByName(name: String) = enumerationByName<T>(name, 255)
/**
 * Maps a database column to an enumeration type using the provided name and enum class reference.
 *
 * @param name The name of the database column to map.
 * @param kClass The KClass reference of the Enum type to map to.
 * @since 5.3.5
 */
fun Table.enumerationByName(name: String, kClass: KClass<out Enum<*>>) = enumerationByName(name, 255, kClass)

/**
 * Represents a custom column type for handling JSONB data in a database using Exposed.
 * This class is parameterized to support deserialization of JSONB data into specified type [T].
 *
 * @param T The data type the JSONB data will be deserialized into.
 * @property typeRef The [TypeReference] implementation that facilitates type-specific deserialization.
 * @since 5.3.1
 * @author Tommaso Pastorelli
 */
class JsonbColumnType<T : Any>(private val typeRef: TypeReference<T>) : ColumnType<T>() {
    companion object {
        /**
         * A constant representing the "jsonb" string, commonly used as an indicator
         * of JSONB data type support. JSONB is a binary storage format for JSON data,
         * offering indexing and advanced querying capabilities in databases like PostgreSQL.
         * @since 5.3.1
         */
        const val JSONB = "jsonb"
    }

    /**
     * Returns the SQL type for the column as a string.
     *
     * This method specifies the SQL type used for storing JSONB data.
     * It is overridden to explicitly define that the column type
     * is `jsonb` in PostgreSQL, which is optimized for JSON storage
     * and operations.
     *
     * @return The SQL data type for the column, in this case, "jsonb".
     * @since 5.3.1
     */
    override fun sqlType() = JSONB
    /**
     * Converts a database value into the appropriate type `T`.
     *
     * @param value The database value to be converted. This can be an instance of
     *              supported types such as `PGobject`, `String`, `Json`, `ByteArray`,
     *              or a fallback to `toString()` for unsupported types.
     * @return The value converted to type `T` by applying the `read` function on the given input.
     * @since 5.3.1
     */
    override fun valueFromDB(value: Any): T = when (value) {
        is PGobject -> read(value.value)
        is String -> read(value)
        is Json -> read(value.value)
        is ByteArray -> read(value.decodeToString())
        else -> read(value.serialize())
    }
    /**
     * Converts a non-null Kotlin object to a database-compatible value using the JSONB format.
     * The object is serialized to a JSON string and encapsulated in a PostgreSQL PGobject.
     *
     * @param value The Kotlin object to be converted. Must not be null.
     * @return A PGobject instance containing the JSON representation of the input object.
     * @since 5.3.1
     */
    override fun notNullValueToDB(value: T): Any = PGobject().apply {
        type = JSONB
        this.value = MAPPER.writeValueAsString(value)
    }
    /**
     * Converts a non-null value of type [T] to its JSON string representation, wrapped in single quotes.
     *
     * @param value the non-null value to be converted to a JSON string
     * @return a string containing the JSON representation of the provided value, wrapped in single quotes
     * @since 5.3.1
     */
    override fun nonNullValueToString(value: T) = "'${MAPPER.writeValueAsString(value)}'"
    /**
     * Reads and deserializes the given JSON string into an object of type T.
     *
     * @param json the JSON string to be deserialized; if null or empty, a default empty JSON value will be used
     * @return the deserialized object of type T
     * @since 5.3.1
     */
    private fun read(json: String?): T = MAPPER.readValue(json, typeRef)
}

/**
 * Registers a column with JSONB (JSON binary) data type for the specified name in the table.
 * This method is designed for use with PostgreSQL databases and provides support for storing and retrieving
 * JSON objects in a strongly-typed manner based on the specified generic type [T].
 *
 * @param T The type of object that will be serialized to and deserialized from JSONB.
 * @param name The name of the column in the table.
 * @return A [Column] object representing the JSONB column with the specified type.
 * @since 5.3.1
 */
inline fun <reified T : Any> Table.jsonb(name: String): Column<T> =
    registerColumn(name, JsonbColumnType(object : TypeReference<T>() {}))

/**
 * Retrieves a JSONB column as a Kotlin list of the specified type.
 *
 * @param name The name of the column to be mapped as a JSONB list.
 * @return The JSONB column mapped as a list of the specified type.
 * @since 5.5.0
 */
inline fun <reified E> Table.list(name: String) = jsonb<List<E>>(name)
/**
 * Registers a column of type [MList] for the specified name in the table. This column uses
 * the JSONB data type to store the data, providing support for strongly-typed list structures
 * with enhanced capabilities.
 *
 * @param E The type of elements stored within the [MList].
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
inline fun <reified E> Table.mList(name: String) = jsonb<MList<E>>(name)
/**
 * Sets a column in the table with the specified name to be associated with
 * a JSONB data type representing a Set of elements of type [E].
 *
 * @param name The name of the column in the table to be set.
 * @since 5.5.0
 */
inline fun <reified E> Table.set(name: String) = jsonb<Set<E>>(name)
/**
 * Registers a column with the JSONB (JSON binary) data type that stores a set-like collection of elements.
 * This method is specifically designed for use with PostgreSQL databases and supports strongly-typed
 * storage and retrieval of JSONB-encoded sets based on the specified generic element type [E].
 *
 * @param E The type of elements contained in the set represented by the JSONB column.
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
inline fun <reified E> Table.mSet(name: String) = jsonb<MSet<E>>(name)
/**
 * Maps a column with a JSONB (JSON binary) data type to a strongly-typed map with keys of type [K]
 * and values of type [V]. This method registers the column in the table with the specified name.
 * Designed for use with PostgreSQL databases to enable typed storage and retrieval of JSON data.
 *
 * @param K The type of the keys in the map.
 * @param V The type of the values in the map.
 * @param name The name of the column in the table to be mapped.
 * @since 5.5.0
 */
inline fun <reified K, reified V> Table.map(name: String) = jsonb<Map<K, V>>(name)
/**
 * Registers a column with a JSONB (JSON binary) data type in the table for the specified name.
 * This column is designed to store and retrieve a map structure with generic key-value types
 * in a strongly-typed manner.
 *
 * @param K The type of keys in the map.
 * @param V The type of values in the map.
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
inline fun <reified K, reified V> Table.mMap(name: String) = jsonb<MMap<K, V>>(name)
/**
 * Registers a column with JSONB (JSON binary) data type in the current table for storing and retrieving
 * `DataMap` objects. This method facilitates strong typing for JSON data in PostgreSQL databases.
 *
 * @param name The name of the column to be created or accessed in the table.
 * @since 5.5.0
 */
fun Table.dataMap(name: String) = jsonb<DataMap>(name)
/**
 * Adds a JSONB column of type [NonEmptyDataMap] to the table with the given name.
 * This method is specific to PostgreSQL databases and enables strong typing
 * for columns storing JSON data with a fixed structure.
 *
 * @param name The name of the column to be added to the table.
 * @since 5.5.0
 */
fun Table.nonEmptyDataMap(name: String) = jsonb<NonEmptyDataMap>(name)
/**
 * Registers a column in the table with the `DataMap` type using the JSONB (JSON binary) data type.
 * This utility is specific to PostgreSQL databases and enables the storage and retrieval of objects
 * in a strongly-typed manner based on the `DataMap` class.
 *
 * @param name The name of the column to be added to the table.
 * @since 5.5.0
 */
fun Table.dataMMap(name: String) = jsonb<DataMap>(name)
/**
 * Registers a column in the table with the JSONB data type and maps it to a strongly-typed
 * [NonEmptyDataMap] object. This method allows seamless integration between the table schema
 * and the corresponding Kotlin type, enabling both serialization and deserialization of
 * the [NonEmptyDataMap] to and from JSONB format. Typically used for PostgreSQL databases.
 *
 * @param name The name of the JSONB column to be registered in the table.
 * @since 5.5.0
 */
fun Table.nonEmptyDataMMap(name: String) = jsonb<NonEmptyDataMap>(name)
/**
 * Creates a column in the current table that maps to a JSONB (JSON binary) field, specifically for handling
 * quantities stored in a [QuantityMap].
 *
 * @param name The name of the column in the table that will store the JSONB representation of the [QuantityMap] object.
 * @since 5.5.0
 */
fun Table.quantityMap(name: String) = jsonb<QuantityMap>(name)
/**
 * Registers a column in the table with a JSONB (JSON binary) data type that maps to a strongly-typed
 * representation of a non-empty quantity mapping.
 *
 * This method is specifically designed for use with PostgreSQL databases, ensuring the ability to work with
 * structured JSON objects while preserving type safety.
 *
 * @param name The name of the JSONB column in the table.
 * @since 5.5.0
 */
fun Table.nonEmptyQuantityMap(name: String) = jsonb<NonEmptyQuantityMap>(name)
/**
 * Registers a JSONB (JSON binary) column in the table to handle data of type [QuantityMMap].
 * This method is specifically designed for working with PostgreSQL databases, allowing the
 * storage and retrieval of JSONB-encoded objects with strong typing.
 *
 * @param name The name of the JSONB column to be registered in the table.
 * @since 5.5.0
 */
fun Table.quantityMMap(name: String) = jsonb<QuantityMMap>(name)
/**
 * Registers a JSONB column with the data type [NonEmptyQuantityMMap] in the table.
 * This method is designed for use with PostgreSQL databases and provides support for storing
 * and retrieving non-empty quantity mappings as strongly-typed JSON objects.
 *
 * @param name The name of the JSONB column to be registered in the table.
 * @since 5.5.0
 */
fun Table.nonEmptyQuantityMMap(name: String) = jsonb<NonEmptyQuantityMMap>(name)
/**
 * Registers a column in the table with the JSONB (JSON binary) data type that maps to a `CountMap` of the specified key type [K].
 * This method facilitates the storage and retrieval of JSON objects that represent maps where the keys are of type [K]
 * and the values are counts.
 *
 * @param K The type of the keys in the count map.
 * @param name The name of the column in the table to be associated with the count map.
 * @since 5.5.0
 */
inline fun <reified K> Table.countMap(name: String) = jsonb<CountMap<K>>(name)
/**
 * Creates a JSONB column in the table specifically mapped to a `NonEmptyCountMap` type with keys of type [K].
 * This method is designed to simplify the registration of JSONB columns for non-empty count maps.
 *
 * @param K The type of the keys in the `NonEmptyCountMap`.
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
inline fun <reified K> Table.nonEmptyCountMap(name: String) = jsonb<NonEmptyCountMap<K>>(name)
/**
 * Registers a JSONB column in the table to store and retrieve data of type [CountMMap].
 * This method utilizes a strongly-typed approach for working with JSON-encoded data,
 * specifically for the generic key type [K].
 *
 * @param K The type of key used in the [CountMMap].
 * @param name The name of the column in the table.
 * @return A [Column] object representing the JSONB column configured for [CountMMap] with the specified key type.
 * @since 5.5.0
 */
inline fun <reified K> Table.countMMap(name: String) = jsonb<CountMMap<K>>(name)
/**
 * Registers a column with the JSONB data type to store and retrieve values of type [NonEmptyCountMMap] with keys of type [K].
 * This method simplifies the creation of JSONB columns specifically for strongly-typed mappings where each key has a non-empty associated count.
 *
 * @param K The type of the keys in the [NonEmptyCountMMap].
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
inline fun <reified K> Table.nonEmptyCountMMap(name: String) = jsonb<NonEmptyCountMMap<K>>(name)
/**
 * Registers a column with the JSONB (JSON binary) data type for storing and retrieving
 * an `IndexMap` type with the specified value type [V] in the table.
 *
 * @param V The type of the values contained in the `IndexMap`.
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
inline fun <reified V> Table.indexMap(name: String) = jsonb<IndexMap<V>>(name)
/**
 * Creates a JSONB column in the table for a non-empty index map with values of type [V].
 * This method facilitates strong typing for storing and retrieving JSON objects
 * represented as non-empty index maps in PostgreSQL databases.
 *
 * @param V The type of the values contained in the non-empty index map.
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
inline fun <reified V> Table.nonEmptyIndexMap(name: String) = jsonb<NonEmptyIndexMap<V>>(name)
/**
 * Creates a JSONB column with a data type of [IndexMMap] for the specified name in the table.
 * This is intended for use with PostgreSQL databases to enable storing and retrieving
 * strongly-typed mapped index structures.
 *
 * @param V The type of values stored within the [IndexMMap].
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
inline fun <reified V> Table.indexMMap(name: String) = jsonb<IndexMMap<V>>(name)
/**
 * Registers a JSONB (binary JSON) column in the table that maps to a [NonEmptyIndexMMap] of type [V].
 * This method allows for strongly-typed storage and retrieval of non-empty index-to-map structures in PostgreSQL databases.
 *
 * @param V The value type contained in the [NonEmptyIndexMMap].
 * @param name The name of the JSONB column in the table.
 * @since 5.5.0
 */
inline fun <reified V> Table.nonEmptyIndexMMap(name: String) = jsonb<NonEmptyIndexMMap<V>>(name)
/**
 * Creates a column of type `StringMap` with JSONB data type in the table.
 * This method is specifically intended for handling `StringMap` structures,
 * which are JSON objects with string keys and string values, in a PostgreSQL database.
 *
 * @param name The name of the column to be created in the table.
 * @since 5.5.0
 */
fun Table.stringMap(name: String) = jsonb<StringMap>(name)
/**
 * Creates a JSONB column with the specified name in the table for a strongly-typed
 * map-like structure where keys and values are non-empty strings.
 *
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
fun Table.nonEmptyStringMap(name: String) = jsonb<NonEmptyStringMap>(name)
/**
 * Registers a column in the table with the JSONB (JSON binary) data type and maps it to a `StringMMap`.
 * This method is specifically designed for PostgreSQL databases and supports storing and retrieving
 * JSON objects as a strongly-typed `StringMMap`.
 *
 * @param name The name of the JSONB column to be created or accessed in the database table.
 * @since 5.5.0
 */
fun Table.stringMMap(name: String) = jsonb<StringMMap>(name)
/**
 * Registers a column with JSONB (JSON binary) data type for a non-empty string-to-map structure.
 * This method is intended to define a column that stores and retrieves strongly-typed
 * values of type [NonEmptyStringMMap] in PostgreSQL databases.
 *
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
fun Table.nonEmptyStringMMap(name: String) = jsonb<NonEmptyStringMMap>(name)
/**
 * Registers a JSONB (JSON binary) column in the table as a strongly-typed multi-map structure.
 * The `multiMap` method simplifies storing and retrieving data as a map structure
 * where both the keys and values are strongly typed.
 *
 * @param K The type of the keys in the multi-map.
 * @param V The type of the values in the multi-map.
 * @param name The name of the column in the table.
 * @return A [Column] of type [MultiMap] containing the specified key-value types.
 * @since 5.5.0
 */
inline fun <reified K, reified V> Table.multiMap(name: String) = jsonb<MultiMap<K, V>>(name)
/**
 * Registers a column with JSONB (JSON binary) data type in the table, specialized for a
 * non-empty multimap structure. This method allows strongly-typed interaction with
 * JSONB columns that store values conforming to the [NonEmptyMultiMap] type.
 *
 * @param K The type of keys in the non-empty multimap.
 * @param V The type of values associated with keys in the non-empty multimap.
 * @param name The name of the column being added to the table.
 * @since 5.5.0
 */
inline fun <reified K, reified V> Table.nonEmptyMultiMap(name: String) = jsonb<NonEmptyMultiMap<K, V>>(name)
/**
 * Registers a column in the table with a JSONB data type for handling a multi-map structure (`MultiMMap`)
 * with specified key and value types [K] and [V]. This method leverages PostgreSQL's JSONB functionality
 * for efficient storage and querying of complex data structures.
 *
 * @param K The key type of the multi-map structure.
 * @param V The value type of the multi-map structure.
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
inline fun <reified K, reified V> Table.multiMMap(name: String) = jsonb<MultiMMap<K, V>>(name)
/**
 * Registers a column to store a non-empty multi-map structure in JSONB format for the specified name.
 * This column supports key-value pairs where multiple values can be associated with the same key, and it ensures that
 * neither the map nor its value collections are empty. Values are serialized and deserialized in a strongly-typed manner.
 *
 * @param K The type of the keys in the multi-map.
 * @param V The type of the values in the multi-map.
 * @param name The name of the column in the table.
 * @return A [Column] object representing the JSONB column of type [NonEmptyMultiMMap].
 * @since 5.5.0
 */
inline fun <reified K, reified V> Table.nonEmptyMultiMMap(name: String) = jsonb<NonEmptyMultiMMap<K, V>>(name)
/**
 * Registers a table column for storing a strongly-typed map represented as a `SetMap<K, V>` object
 * in JSONB (JSON binary) format using a PostgreSQL database. This method allows you to create a column
 * where data is stored and retrieved as JSON, facilitating seamless interaction with structured data.
 *
 * @param K The type of the keys in the map.
 * @param V The type of the values in the map.
 * @param name The name of the JSONB column to be created in the table.
 * @since 5.5.0
 */
inline fun <reified K, reified V> Table.setMap(name: String) = jsonb<SetMap<K, V>>(name)
/**
 * Registers a column in the table with the JSONB (JSON binary) data type for non-empty set map values.
 * This method enables strongly-typed storage and retrieval of data where the specified type is a map
 * with non-empty sets as the values. It is designed for use with PostgreSQL databases to ensure
 * accurate serialization and deserialization of such structures.
 *
 * @param K The type of the keys in the map.
 * @param V The type of the values in the non-empty sets contained in the map.
 * @param name The name of the column in the table.
 * @since 5.5.0
 */
inline fun <reified K, reified V> Table.nonEmptySetMap(name: String) = jsonb<NonEmptySetMap<K, V>>(name)
/**
 * Registers a column in the table to store a map of sets (SetMMap) using the JSONB (JSON binary) data type.
 * This function is specifically tailored for PostgreSQL databases and enables type-safe storage
 * and retrieval of a map where keys are of type [K] and values are sets of elements of type [V].
 *
 * @param name The name of the column in the table.
 * @param K The type of the keys in the map.
 * @param V The type of the elements in the sets that are the values in the map.
 * @since 5.5.0
 */
inline fun <reified K, reified V> Table.setMMap(name: String) = jsonb<SetMMap<K, V>>(name)
/**
 * Registers a column in the table with the JSONB (JSON binary) data type that stores values as a
 * non-empty multi-map (i.e., a map where each key is associated with a non-empty set of values).
 * This method is commonly used with PostgreSQL databases to store strongly-typed non-empty multi-map
 * data structures in a JSONB column.
 *
 * @param K The type of the keys in the multi-map.
 * @param V The type of the values in the sets associated with the keys.
 * @param name The name of the column in the table where the non-empty multi-map data will be stored.
 * @since 5.5.0
 */
inline fun <reified K, reified V> Table.nonEmptySetMMap(name: String) = jsonb<NonEmptySetMMap<K, V>>(name)

/**
 * Represents a column type for handling PostgreSQL `int4range` data type.
 * This class is used to map `int4range` database values to Kotlin's `IntRange` type
 * and vice versa. It provides methods for converting database values into `IntRange`
 * objects, as well as serializing `IntRange` objects into the proper SQL representation.
 *
 * The `int4range` type in PostgreSQL represents a range of integer values.
 * The range can be inclusive or exclusive, and this class ensures proper handling
 * of these semantics during serialization and deserialization.
 *
 * @constructor Creates an instance of `IntRangeColumnType`.
 * @since 5.5.0
 * @author Tommaso Pastorelli
 */
class IntRangeColumnType : ColumnType<IntRange>() {
    companion object {
        /**
         * Represents the PostgreSQL range type `int4range`, which is used to define
         * ranges of 32-bit integers in the database.
         *
         * Typically used in database schema definitions or SQL operations involving
         * range checking and manipulation.
         *
         * @since 5.5.0
         */
        const val INT_RANGE = "int4range"
    }


    /**
     * Returns the SQL type specification for a PostgreSQL integer range (`int4range`).
     *
     * This method specifies the database column type used to represent an inclusive range
     * of integers. The PostgreSQL `int4range` type is commonly used to store integer ranges
     * with a defined start and end.
     *
     * @return The string "int4range", representing the SQL type for a PostgreSQL integer range.
     * @see IntRangeColumnType
     * @since 5.5.0
     */
    override fun sqlType() = INT_RANGE

    /**
     * Converts a database value into an [IntRange] representation.
     *
     * @param value the value retrieved from the database, expected to be either a `PGobject` or a `String`.
     * @return the corresponding [IntRange] parsed from the input value.
     * @throws IllegalStateException if the input value is not a supported type or cannot be parsed.
     * @since 5.5.0
     */
    override fun valueFromDB(value: Any): IntRange = when (value) {
        is PGobject -> parseRange(value.value!!)
        is String -> parseRange(value)
        else -> error("Unexpected value for int4range: $value")
    }

    /**
     * Converts a non-null [IntRange] value into a database-compatible representation as a PostgreSQL int4range.
     *
     * @param value the non-null [IntRange] to be converted, represented as an inclusive range.
     * @return a [PGobject] configured with the type "int4range" and the value formatted as "[start,end]".
     * @since 5.5.0
     */
    override fun notNullValueToDB(value: IntRange): Any = PGobject().apply {
        type = "int4range"
        this.value = "[${value.first},${value.last}]" // inclusive-inclusive
    }

    /**
     * Converts a non-null [IntRange] value into its string representation formatted as a PostgreSQL range.
     *
     * Represents the [IntRange] in the format '[start,end]', where the range is inclusive on both sides.
     *
     * @param value the non-null [IntRange] to be converted to a string representation
     * @since 5.5.0
     */
    override fun nonNullValueToString(value: IntRange) = "'[${value.first},${value.last}]'"

    /**
     * Parses a PostgreSQL range string into an [IntRange]. The input string is expected to be
     * in the format `"[start,end)"` or `"[start,end]"`, where the range is inclusive on the lower
     * bound and can be either inclusive or exclusive on the upper bound.
     *
     * @param s the PostgreSQL range string to be parsed. It must represent a range in one of the
     *          supported formats.
     * @return an [IntRange] representing the parsed range. The lower bound is always inclusive,
     *         and the upper bound is adjusted to be inclusive if originally exclusive in the input.
     * @since 5.5.0
     */
    private fun parseRange(s: String): IntRange {
        // formato Postgres: "[1,10)" oppure "[1,10]"
        val trimmed = s.trim('[', ']', '(', ')')
        val [start, endExclusive] = trimmed.split(",").map { it.trim().toInt() }
        val inclusiveEnd = if (s.endsWith(")")) endExclusive - 1 else endExclusive
        return start..inclusiveEnd
    }
}

/**
 * Registers a column in the table to handle PostgreSQL `int4range` data type,
 * mapping it to a Kotlin [IntRange].
 *
 * @param name The name of the column to be registered.
 * @return A [Column] of type [IntRange] associated with the specified column name.
 * @since 5.5.0
 */
fun Table.intRange(name: String): Column<IntRange> = registerColumn(name, IntRangeColumnType())

/**
 * Represents a column type that maps a `UIntRange` value to a database column with JSONB support.
 * This class is intended to handle serialization and deserialization of `UIntRange` objects
 * to and from the database in JSON format.
 *
 * The `UIntRangeColumnType` handles:
 * - Mapping the `UIntRange` values to a JSONB representation when storing them in the database.
 * - Parsing the stored JSONB data back into a `UIntRange` object when retrieving it from the database.
 *
 * It requires proper JSON format to represent the `UIntRange`, where the JSON structure must
 * include `start` and `endInclusive` fields as unsigned integers. For example:
 * ```json
 * {"start":1,"endInclusive":10}
 * ```
 *
 * This implementation assumes PostgreSQL's JSONB type for the column type and uses `PGobject` for conversion.
 *
 * Methods overridden:
 * - `sqlType`: Specifies the SQL type for the column as JSONB.
 * - `valueFromDB`: Converts database values into a `UIntRange` object.
 * - `notNullValueToDB`: Converts a `UIntRange` object into a JSONB representation for storage.
 * - `nonNullValueToString`: Provides a string representation of the `UIntRange` for SQL operations.
 *
 * @since 5.5.0
 * @author Tommaso Pastorelli
 */
class UIntRangeColumnType : ColumnType<UIntRange>() {
    /**
     * Returns the SQL type for the column represented by this `ColumnType` implementation.
     * This method specifies the use of the `JSONB` type, which is a PostgreSQL
     * JSON binary storage format that efficiently stores and processes JSON data.
     *
     * @return The SQL type as a string, specifically `JSONB`.
     * @since 5.5.0
     */
    override fun sqlType() = JSONB

    /**
     * Converts a database value into a UIntRange object based on its type.
     *
     * @param value The database value to be converted. Supported types are:
     *              PGobject, String, Json, ByteArray, or any other type that can be serialized.
     * @return A UIntRange object representing the deserialized range.
     * @since 5.5.0
     */
    override fun valueFromDB(value: Any): UIntRange = when (value) {
        is PGobject -> read(value.value)
        is String -> read(value)
        is Json -> read(value.value)
        is ByteArray -> read(value.decodeToString())
        else -> read(value.serialize())
    }

    /**
     * Converts a non-null `UIntRange` value into a database-acceptable representation.
     *
     * @param value The `UIntRange` to be converted. `value.first` represents the start of the range, and
     *              `value.last` represents the inclusive end of the range.
     * @return An `Any` representing a JSONB object configured to store the `UIntRange` as a JSON structure
     *         with `start` and `endInclusive` fields.
     * @since 5.5.0
     */
    override fun notNullValueToDB(value: UIntRange): Any = PGobject().apply {
        type = JSONB
        this.value = """{"start":${value.first},"endInclusive":${value.last}}"""
    }

    /**
     * Converts a non-null [UIntRange] value into its string representation in JSON format.
     * The function ensures both the start and the end of the range are serialized.
     *
     * @param value The [UIntRange] instance to be serialized to JSON. It must not be null.
     * @since 5.5.0
     */
    override fun nonNullValueToString(value: UIntRange) = """'{"start":${value.first},"endInclusive":${value.last}}'"""

    /**
     * Reads a JSON string and deserializes it into a `UIntRange` object.
     *
     * @param json The JSON string representing the range. The JSON must include
     *             fields "start" and "endInclusive" as unsigned integers.
     * @return A `UIntRange` object representing the range defined in the JSON string.
     * @throws IllegalArgumentException if the JSON string is null.
     * @throws IllegalStateException if the "start" or "endInclusive" fields are
     *                               missing or invalid in the JSON string.
     * @since 5.5.0
     */
    private fun read(json: String?): UIntRange {
        requireNotNull(json) { "Cannot deserialize UIntRange from null JSON" }
        val node = Json(json)
        val start = node.getAsNode("start")
            ?.asString()?.toUInt()
            ?: error("Missing or invalid 'start' in: $json")
        val end = node.getAsNode("endInclusive")
            ?.asString()?.toUInt()
            ?: error("Missing or invalid 'endInclusive' in: $json")
        return start..end
    }
}

/**
 * Registers a column in the table with the specified name to store values of type `UIntRange`.
 *
 * @param name The name of the column to be added to the table.
 * @return The newly created column of type `UIntRange`.
 * @since 5.5.0
 */
fun Table.uIntRange(name: String): Column<UIntRange> = registerColumn(name, UIntRangeColumnType())

/**
 * Represents a column type for PostgreSQL `int8range` (bigint range) data type.
 *
 * This class is used to map a PostgreSQL range type to a Kotlin `LongRange` during database
 * interaction. It provides mechanisms to serialize/deserialize the `LongRange` between the database
 * format and Kotlin data structures.
 *
 * Features:
 * - Handles conversion of PostgreSQL `int8range` data to Kotlin `LongRange`.
 * - Supports inclusive start and inclusive or exclusive end bound parsing.
 * - Customizes SQL type declaration for database schema generation.
 *
 * Key functionality includes:
 * - Parsing `int8range` strings from the database into `LongRange` objects.
 * - Serializing `LongRange` objects back into SQL-compatible `int8range` strings.
 *
 * PostgreSQL `int8range` format can use square brackets `[ ]` to denote inclusive bounds or
 * parentheses `( )` to denote exclusive bounds. For instance:
 * - `[1,10)` translates to a range from `1` to `9` (inclusive-exclusive).
 * - `[1,10]` translates to a range from `1` to `10` (inclusive-inclusive).
 *
 * @constructor Creates an instance of LongRangeColumnType.
 * @since 5.5.0
 * @author Tommaso Pastorelli
 */
class LongRangeColumnType : ColumnType<LongRange>() {
    companion object {
        /**
         * Represents the SQL data type for an 8-byte integer range column.
         * This constant is used to define the SQL type for columns
         * that store a range of integers within the 8-byte signed integer range.
         *
         * @since 5.5.0
         */
        const val LONG_RANGE = "int8range"
    }

    /**
     * Returns the SQL type representation for the `LongRangeColumnType`.
     * This method specifies the proper type mapping for storing `LongRange` values in a database.
     *
     * @return The SQL type constant corresponding to a long range.
     * @since 5.5.0
     */
    override fun sqlType() = LONG_RANGE

    /**
     * Converts a database value into a Kotlin `LongRange`.
     *
     * @param value The value retrieved from the database, expected to be of type `PGobject` or `String`.
     * @return A `LongRange` object representing the range value parsed from the database input.
     * @throws IllegalArgumentException if the input value type is unexpected.
     * @since 5.5.0
     */
    override fun valueFromDB(value: Any): LongRange = when (value) {
        is PGobject -> parseRange(value.value!!)
        is String -> parseRange(value)
        else -> error("Unexpected value for int4range: $value")
    }

    /**
     * Converts a non-null `LongRange` into a database-compatible representation.
     *
     * @param value the `LongRange` object to be converted. The range is inclusive on both ends and
     *              will be formatted as an "int8range" for use with PostgreSQL.
     * @return a `PGobject` containing the converted range in the required database format.
     * @since 5.5.0
     */
    override fun notNullValueToDB(value: LongRange): Any = PGobject().apply {
        type = "int8range"
        this.value = "[${value.first},${value.last}]" // inclusive-inclusive
    }

    /**
     * Converts a non-null LongRange value into its string representation.
     *
     * @param value the LongRange value to be converted, where the first element
     *              represents the starting value and the last element represents
     *              the ending value of the range.
     * @since 5.5.0
     */
    override fun nonNullValueToString(value: LongRange) = "'[${value.first},${value.last}]'"

    /**
     * Parses a PostgreSQL range string representation and converts it into a [LongRange].
     * The input can be in formats like "[1,10)" (exclusive upper bound) or "[1,10]" (inclusive upper bound).
     *
     * @param s the range string to be parsed. It must conform to the PostgreSQL range format.
     * @return a [LongRange] object representing the parsed range, with the correct bounds handling.
     * @since 5.5.0
     */
    private fun parseRange(s: String): LongRange {
        // formato Postgres: "[1,10)" oppure "[1,10]"
        val trimmed = s.trim('[', ']', '(', ')')
        val [start, endExclusive] = trimmed.split(",").map { it.trim().toLong() }
        val inclusiveEnd = if (s.endsWith(")")) endExclusive - 1 else endExclusive
        return start..inclusiveEnd
    }
}

/**
 * Registers a column of type `LongRange` in the table schema. This method maps the column to the
 * PostgreSQL `int8range` data type, which represents a range of 64-bit integers.
 *
 * @param name The name of the column to be registered in the database schema.
 * @return A `Column` instance that represents a database column storing `LongRange` values.
 * @since 5.5.0
 */
fun Table.longRange(name: String): Column<LongRange> = registerColumn(name, LongRangeColumnType())

/**
 * A custom column type for handling `ULongRange` objects in database operations.
 * This class is responsible for mapping `ULongRange` values to proper database types in both directions.
 * The underlying database representation uses the JSONB data type to store `ULongRange` as structured JSON.
 *
 * The `ULongRangeColumnType` handles serialization and deserialization of the `ULongRange` from JSON format.
 * It ensures correct behavior for database read and write operations, including value conversions.
 *
 * The JSON structure for a `ULongRange` is expected to have the following format:
 * ```
 * {
 *   "start": <ULong>,
 *   "endInclusive": <ULong>
 * }
 * ```
 *
 * This class inherits from `ColumnType<T>` to provide database type-specific customization for `ULongRange`.
 *
 * @constructor Creates a new instance of `ULongRangeColumnType`.
 * @since 5.5.0
 * @author Tommaso Pastorelli
 */
class ULongRangeColumnType : ColumnType<ULongRange>() {
    /**
     * Returns the SQL type representation for the `ULongRangeColumnType`.
     * This method is overridden to specify that the `ULongRangeColumnType`
     * uses the `JSONB` data type in the underlying database.
     *
     * @return A string constant `JSONB` representing the SQL type.
     * @since 5.5.0
     */
    override fun sqlType() = JSONB

    /**
     * Converts a value from the database into a ULongRange instance.
     *
     * @param value The database value to be converted. This can be of types PGobject, String, Json, ByteArray, or other serializable types.
     * @return A ULongRange instance representing the converted value.
     * @since 5.5.0
     */
    override fun valueFromDB(value: Any): ULongRange = when (value) {
        is PGobject -> read(value.value)
        is String -> read(value)
        is Json -> read(value.value)
        is ByteArray -> read(value.decodeToString())
        else -> read(value.serialize())
    }

    /**
     * Converts a non-null ULongRange value to a database-compatible format.
     *
     * The function transforms the ULongRange into a JSONB representation
     * with "start" and "endInclusive" keys indicating the range bounds.
     *
     * @param value The ULongRange value to be converted. It must not be null.
     * @return A PGobject instance containing the JSONB string representation of the ULongRange.
     * @since 5.5.0
     */
    override fun notNullValueToDB(value: ULongRange): Any = PGobject().apply {
        type = JSONB
        this.value = """{"start":${value.first},"endInclusive":${value.last}}"""
    }

    /**
     * Converts the given non-null `ULongRange` value into its string representation
     * formatted as a JSON object with "start" and "endInclusive" fields.
     *
     * @param value the non-null `ULongRange` instance to be converted to a string.
     * @since 5.5.0
     */
    override fun nonNullValueToString(value: ULongRange) = """'{"start":${value.first},"endInclusive":${value.last}}'"""

    /**
     * Deserializes a JSON string into a ULongRange object.
     *
     * @param json The JSON string representing the ULongRange.
     *             Must contain "start" and "endInclusive" fields with valid unsigned long values.
     * @return The ULongRange object created from the JSON string.
     * @throws IllegalArgumentException If the provided JSON is null.
     * @throws IllegalStateException If the JSON is missing required fields or if the fields contain invalid data.
     * @since 5.5.0
     */
    private fun read(json: String?): ULongRange {
        requireNotNull(json) { "Cannot deserialize ULongRange from null JSON" }
        val node = Json(json)
        val start = node.getAsNode("start")
            ?.asString()?.toULong()
            ?: error("Missing or invalid 'start' in: $json")
        val end = node.getAsNode("endInclusive")
            ?.asString()?.toULong()
            ?: error("Missing or invalid 'endInclusive' in: $json")
        return start..end
    }
}

/**
 * Creates and registers a column of type ULongRange in the table schema.
 *
 * @param name The name of the column to be created in the table.
 * @return The registered column as a `Column<ULongRange>`.
 * @since 5.5.0
 */
fun Table.uLongRange(name: String): Column<ULongRange> = registerColumn(name, ULongRangeColumnType())

/**
 * Executes a database transaction, propagates exceptions using a custom transformer, and supports configurable behavior.
 *
 * @param T The type of the result produced by the transaction block.
 * @param lazyException A transformer used to wrap exceptions that occur during the transaction.
 *                       The default implementation wraps exceptions in a `DatabaseOperationException`.
 * @param db The database instance in which the transaction should be executed. If null, the default database is used.
 * @param transactionIsolation The isolation level for the transaction. If null, the default isolation level of the database's transaction manager is used.
 * @param readOnly Indicates whether the transaction should be executed in read-only mode. If null, the default value of the database's transaction manager is used.
 * @param block The transactional block of code to be executed. It receives a `JdbcTransaction` as its receiver.
 * @throws Throwable The exception returned by the `lazyException` transformer if an error occurs during the transaction.
 *         Possible exceptions include `SQLException`, `UnsupportedByDialectException`, `DuplicateColumnException`, and `LongQueryException`.
 * @since 5.3.5
 */
fun <T> transactionOrThrow(
    lazyException: ThrowableTransformer = { DatabaseOperationException(it.message) },
    db: Database? = null,
    transactionIsolation: Int? = db?.transactionManager?.defaultIsolationLevel,
    readOnly: Boolean? = db?.transactionManager?.defaultReadOnly,
    block: ReceiverTransformer<JdbcTransaction, T>
) = try {
    transaction(db, transactionIsolation, readOnly, block)
} catch (e: SQLException) {
    throw lazyException(e)
} catch (e: UnsupportedByDialectException) {
    throw lazyException(e)
} catch (e: DuplicateColumnException) {
    throw lazyException(e)
} catch (e: LongQueryException) {
    throw lazyException(e)
}

/**
 * Converts a `Table.UuidVersion` instance to its equivalent `UuidVersion` representation.
 *
 * This method performs a mapping between the UUID versions defined in the `Table.UuidVersion` enum
 * and those in the `UuidVersion` enum. Currently, it supports mappings for version 4 (random-based UUID)
 * and version 7 (time-based and random UUID).
 *
 * @receiver The `Table.UuidVersion` instance to be converted.
 * @return The corresponding `UuidVersion` instance.
 * @since 5.3.0
 */
fun Table.UuidVersion.toKutilsUuidVersion() = when (this) {
    Table.UuidVersion.V4 -> UuidVersion.V4
    Table.UuidVersion.V7 -> UuidVersion.V7
}
/**
 * Converts a `UuidVersion` enum value to its corresponding `Table.UuidVersion` enum value.
 *
 * This method provides a mapping between the `UuidVersion` and `Table.UuidVersion` enumerations.
 * Currently, it supports mapping for `V4` and `V7` versions. If an unsupported version is passed,
 * a `NoSuchEntryException` will be thrown.
 *
 * @receiver The `UuidVersion` to be converted.
 * @return The corresponding `Table.UuidVersion` for the specified `UuidVersion`.
 * @throws NoSuchEntryException if the `UuidVersion` does not have a corresponding mapping in `Table.UuidVersion`.
 * @since 5.3.0
 */
fun UuidVersion.toExposedUuidVersion() = when (this) {
    UuidVersion.V4 -> Table.UuidVersion.V4
    UuidVersion.V7 -> Table.UuidVersion.V7
    else -> throw NoSuchEntryException(Table.UuidVersion::class, this)
}

/**
 * Converts a `SortOrder` enumeration value to its corresponding `SortDirection` value.
 *
 * This extension function maps specific `SortOrder` values to either
 * `SortDirection.Ascending` or `SortDirection.Descending` based on their
 * sorting order semantics. The mapping includes the following associations:
 *
 * - `SortOrder.ASC`, `SortOrder.ASC_NULLS_FIRST`, `SortOrder.ASC_NULLS_LAST`
 *   are mapped to `SortDirection.Ascending`.
 * - `SortOrder.DESC`, `SortOrder.DESC_NULLS_FIRST`, `SortOrder.DESC_NULLS_LAST`
 *   are mapped to `SortDirection.Descending`.
 *
 * @receiver A `SortOrder` value representing the desired sorting configuration.
 * @return The corresponding `SortDirection` value.
 * @since 5.3.0
 */
fun SortOrder.toKutilsSortDirection() = when (this) {
    SortOrder.ASC, SortOrder.ASC_NULLS_FIRST, SortOrder.ASC_NULLS_LAST -> SortDirection.Ascending
    SortOrder.DESC, SortOrder.DESC_NULLS_FIRST, SortOrder.DESC_NULLS_LAST -> SortDirection.Descending
}
/**
 * Converts a [SortDirection] instance to its corresponding Exposed framework's [SortOrder].
 *
 * This extension function maps the `SortDirection` values [SortDirection.Ascending]
 * and [SortDirection.Descending] to [SortOrder.ASC] and [SortOrder.DESC], respectively.
 *
 * @return The corresponding [SortOrder] value for the given [SortDirection].
 * @since 5.3.0
 */
fun SortDirection.toExposedSortOrder() = when (this) {
    SortDirection.Ascending -> SortOrder.ASC
    SortDirection.Descending -> SortOrder.DESC
}

/**
 * Adds a UUID column to the table with the specified name.
 *
 * @param name The name of the UUID column to be added to the table.
 * @since 5.3.0
 */
fun Table.javaUuid(name: String): Column<Uuid> = javaUUID(name)

/**
 * Specifies the sorting order for a window function based on the given column and sort direction.
 *
 * @param column The column to be used for ordering the rows in the window function result.
 * @param order The sorting direction, either [SortDirection.Ascending] or [SortDirection.Descending],
 *              indicating whether the rows should be ordered in ascending or descending order.
 * @since 5.3.0
 */
fun <T> WindowFunctionDefinition<T>.orderBy(column: Expression<*>, order: SortDirection) =
    orderBy(column, order.toExposedSortOrder())
/**
 * Specifies the order in which the window function should sort the results.
 *
 * @param order A variable number of pairs, where each pair consists of an [Expression]
 * representing the target column or expression to sort by, and a [SortDirection] indicating
 * the sorting order (e.g., ascending or descending) for that expression.
 * @since 5.3.0
 */
fun <T> WindowFunctionDefinition<T>.orderBy(vararg order: Pair<Expression<*>, SortDirection>) =
    orderBy(*order.map { it.first to it.second.toExposedSortOrder() }.toTypedArray())
/**
 * Adds an order by clause to the current inner table link based on the specified list of sorting expressions
 * and their respective sort directions.
 *
 * @param order A list of pairs where each pair consists of an expression to sort by and its corresponding
 * sort direction represented as a [SortDirection]. The expression defines the field or property to order by,
 * and the sort direction specifies whether the order should be ascending or descending.
 * @since 5.3.0
 */
infix fun <SID : Any, Source : Entity<SID>, ID : Any, Target : Entity<ID>> InnerTableLink<SID, Source, ID, Target>.orderBy(
    order: List<Pair<Expression<*>, SortDirection>>
) = orderBy(order.map { it.first to it.second.toExposedSortOrder() })
/**
 * Specifies the sorting order for a linked `InnerTableLink` entity.
 *
 * This function applies a sorting directive to a database query by pairing an expression
 * with a specific `SortDirection` and converting it into the corresponding sort order
 * recognized by the underlying Exposed framework.
 *
 * @param order A pair consisting of an [Expression] to sort by and the [SortDirection] to
 *              determine the sorting order (ascending or descending).
 * @since 5.3.0
 */
infix fun <SID : Any, Source : Entity<SID>, ID : Any, Target : Entity<ID>> InnerTableLink<SID, Source, ID, Target>.orderBy(
    order: Pair<Expression<*>, SortDirection>
) = orderBy(order.first to order.second.toExposedSortOrder())
/**
 * Adds an ordering condition to a query involving a referrer relationship between entities.
 *
 * This function allows specifying the sorting order for a query by associating an expression
 * with a sorting direction. It is utilized to ensure that the results of a query are sorted
 * according to the specified order in the context of a parent-child relationship.
 *
 * @param order A pair containing the expression to be ordered (`Expression<*>`) and the sorting
 * direction (`SortDirection`). The expression determines the field or property to sort by,
 * and the sorting direction determines whether the sorting is ascending or descending.
 * @since 5.3.0
 */
infix fun <ParentID : Any, Parent : Entity<ParentID>, ChildID : Any, Child : Entity<ChildID>, REF> Referrers<ParentID, Parent, ChildID, Child, REF>.orderBy(order: Pair<Expression<*>, SortDirection>) =
    orderBy(order.first to order.second.toExposedSortOrder())
/**
 * Orders the results of a query based on the specified sort conditions.
 *
 * This function allows sorting the results by providing one or more pairs of
 * expressions and their corresponding sort directions. Each pair consists of
 * an [Expression] to sort by and a [SortDirection] which determines whether the
 * sorting should be ascending or descending.
 *
 * @param order A vararg parameter representing pairs of [Expression] and
 * [SortDirection]. Each pair specifies the field to sort by and the desired order.
 * @since 5.3.0
 */
fun <ParentID : Any, Parent : Entity<ParentID>, ChildID : Any, Child : Entity<ChildID>, REF> Referrers<ParentID, Parent, ChildID, Child, REF>.orderBy(vararg order: Pair<Expression<*>, SortDirection>) =
    orderBy(*order.map { it.first to it.second.toExposedSortOrder() }.toTypedArray())
/**
 * Orders the elements of a `SizedIterable` based on the specified expressions and sort directions.
 *
 * Each element in the `order` parameter consists of a pair, where:
 * - The first item is an [Expression] to be used as a sorting criterion.
 * - The second item is a [SortDirection], determining whether the sorting is ascending or descending.
 *
 * The method applies the specified order criteria to the iterable and transforms the [SortDirection]
 * of each pair into the corresponding Exposed framework's [SortOrder].
 *
 * @param order One or more pairs of [Expression] and [SortDirection] defining the sorting conditions.
 *              The pairs specify which expressions should be used for sorting and their respective directions.
 * @return A new iterable with the elements sorted according to the provided order conditions.
 * @since 5.3.0
 */
fun <T> SizedIterable<T>.orderBy(vararg order: Pair<Expression<*>, SortDirection>) =
    orderBy(*order.map { it.first to it.second.toExposedSortOrder() }.toTypedArray())
/**
 * Orders a query by the specified column and sort direction.
 *
 * @param column The column to sort the query by. This is represented as an [Expression] object.
 * @param order The direction in which to sort the column, which is of type [SortDirection]
 * (e.g., [SortDirection.Ascending] or [SortDirection.Descending]).
 * @since 5.3.0
 */
fun Query.orderBy(column: Expression<*>, order: SortDirection) = orderBy(column, order.toExposedSortOrder())
/**
 * Adds sorting criteria to the query based on the specified expressions and their corresponding sort directions.
 *
 * This function allows chaining of multiple sorting conditions using pairs of expressions
 * and their associated sort directions. The pairs will be converted into the appropriate format
 * for the underlying Exposed framework.
 *
 * @param order A variable number of pairs, where each pair consists of an `Expression`
 *              representing the field to sort by, and a `SortDirection` indicating the sorting order.
 * @since 5.3.0
 */
fun Query.orderBy(vararg order: Pair<Expression<*>, SortDirection>) = orderBy(
    *order.map { it.first to it.second.toExposedSortOrder() }.toTypedArray()
)
/**
 * Adds an ordering clause to a set operation based on the specified column and sort direction.
 *
 * @param column The column expression that specifies the field on which the ordering is applied.
 * @param order The sort direction, either ascending or descending, represented by [SortDirection].
 * @since 5.3.0
 */
fun SetOperation.orderBy(column: Expression<*>, order: SortDirection) = orderBy(column, order.toExposedSortOrder())
/**
 * Adds an `ORDER BY` clause to a set operation using the specified expressions and sort directions.
 *
 * This function allows for the specification of sorting criteria for a set operation by
 * providing pairs of expressions and their corresponding sorting directions. The sort directions
 * are converted to the appropriate format that can be used within the Exposed framework.
 *
 * @param order A variable number of pairs where each pair consists of an expression
 * representing the column or field to sort by, and a [SortDirection] defining the
 * order (ascending or descending) for sorting that column or field.
 * @since 5.3.0
 */
fun SetOperation.orderBy(vararg order: Pair<Expression<*>, SortDirection>) = orderBy(
    *order.map { it.first to it.second.toExposedSortOrder() }.toTypedArray()
)

/**
 * Executes the specified SQL query within the context of the provided JDBC transaction.
 *
 * @param args A collection of pairs, where each pair consists of a column type and its associated value.
 *             These are the arguments to be used in the SQL query. Defaults to an empty list if no arguments are provided.
 * @param explicitStatementType An optional parameter to explicitly specify the type of SQL statement to execute.
 *                               Defaults to null if not provided.
 * @since 5.4.4
 */
@IgnorableReturnValue
context(transaction: JdbcTransaction)
fun SqlQuery.executeQuery(
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null
) = transaction.exec(this, args, explicitStatementType)
/**
 * Executes the provided SQL query within the given transactional context.
 *
 * @param args A list of pairs consisting of a column type and its corresponding value to be used as parameters for the query. Defaults to an empty list.
 * @param explicitStatementType An optional statement type to override the default behavior of the query execution.
 * @param transform A function that processes the query's result set and transforms it into the desired type.
 * @return Transformed result of the query.
 * @since 5.4.4
 */
context(transaction: JdbcTransaction)
fun <T> SqlQuery.executeQuery(
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null,
    transform: Transformer<ResultSet, T?>
) = transaction.exec(this, args, explicitStatementType, transform)

/**
 * Executes the calling SQL string within a `JdbcTransaction` context and converts the result set
 * into a table structure. Provides an abstraction for executing SQL queries and processing their
 * results into a structured table output.
 *
 * @param T The type of the table's cell values.
 * @param this The SQL query string to be executed. It must conform to SQL syntax.
 * @param args A collection of pairs, where each pair consists of a column type and a value
 *             to be bound to the respective parameter in the SQL query. Defaults to an empty list.
 * @param explicitStatementType An optional parameter to explicitly specify the type of SQL statement
 *                              being executed, such as SELECT, UPDATE, etc. Can be null.
 * @receiver The SQL query string to execute.
 * @since 5.4.4
 */
context(transaction: JdbcTransaction)
fun <T> @receiver:Language("sql") String.executeQueryToTable(
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null
) = transaction.execToTable<T>(this, args, explicitStatementType)
/**
 * Executes an SQL query string within a `JdbcTransaction` context and converts the resulting
 * `ResultSet` into a table structure. Utilizes a custom transformation function to map the
 * result set values into the desired type. If the query does not produce a result set,
 * an empty table is returned.
 *
 * @receiver The SQL query string to be executed. It must conform to valid SQL syntax.
 * @param args A collection of pairs where each pair consists of a column type and a value
 *             that will be bound to the SQL statement. Defaults to an empty list if no arguments are provided.
 * @param explicitStatementType An optional parameter to explicitly define the type of SQL statement
 *                              (e.g., SELECT, UPDATE). Can be null.
 * @param transform A transformation function applied to the `ResultSet` to map it into the desired type.
 *                  This function determines how each entry in the result set is processed.
 *
 * @return A table structure containing the transformed rows of the query result. If the query
 *         produces no result set, an empty table is returned.
 * @since 5.4.4
 */
context(transaction: JdbcTransaction)
fun <T, R> @receiver:Language("sql") String.executeQueryToTable(
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null,
    transform: Transformer<ResultRow<T>, R>
) = transaction.execToTable<T, R>(this, args, explicitStatementType, transform)

/**
 * Executes an SQL query within the provided transaction context and maps the result to a table structure.
 *
 * @param args A collection of column type and value pairs representing the query parameters. Defaults to an empty list if no parameters are provided.
 * @param explicitStatementType An optional explicit statement type for the query execution. If not provided, the default statement type is used.
 * @since 5.4.4
 */
context(transaction: JdbcTransaction)
fun <T> SqlQuery.executeQueryToTable(
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null
) = transaction.execToTable<T>(this, args, explicitStatementType)
/**
 * Executes a SQL query within the provided transaction context and maps the resulting data into a table format.
 *
 * @param args A list of pairs representing the column type and its corresponding value to be included in the query execution.
 * @param explicitStatementType An optional parameter to specify the explicit statement type, if needed.
 * @param transform A function that transforms a ResultSet into the desired type T or null.
 * @since 5.4.4
 */
context(transaction: JdbcTransaction)
fun <T, R> SqlQuery.executeQueryToTable(
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null,
    transform: Transformer<ResultRow<T>, R>
) = transaction.execToTable<T, R>(this, args, explicitStatementType, transform)

/**
 * Executes the given SQL query within the current JDBC transaction context.
 *
 * @param query The SQL query to be executed.
 * @param args A collection of pairs where each pair consists of a column type and its corresponding value,
 *             representing the parameters to be set in the query. Defaults to an empty list if no parameters are provided.
 * @param explicitStatementType An optional parameter that specifies the statement type (e.g., SELECT, INSERT, etc.) explicitly.
 *                              If not provided, the statement type will be inferred.
 * @return The result of executing the query.
 * @since 5.3.0
 */
@IgnorableReturnValue
fun JdbcTransaction.exec(
    query: SqlQuery,
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null
) = exec(query.value, args, explicitStatementType)
/**
 * Executes a SQL query within the context of a JDBC transaction.
 *
 * @param query The SQL query to be executed, represented as an instance of SqlQuery.
 * @param args An iterable collection of column type and value pairs to be used as parameters for the query.
 *             Defaults to an empty list if no parameters are specified.
 * @param explicitStatementType An optional parameter representing the statement type to be explicitly used
 *                               for the query execution. Defaults to null if not provided.
 * @param transform A lambda function that processes the ResultSet returned by the query and transforms it
 *                  into a desired type T.
 * @return A result of type T, which is the output of the transformation logic applied on the query's ResultSet.
 * @since 5.3.0
 */
fun <T> JdbcTransaction.exec(
    query: SqlQuery,
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null,
    transform: Transformer<ResultSet, T?>
) = exec(query.value, args, explicitStatementType, transform)
/**
 * Executes an SQL statement within a `JdbcTransaction` context and converts the resulting
 * `ResultSet` into a table structure. If the result is null, an empty table is returned.
 *
 * @param T The type of the table's cell values.
 * @param stmt The SQL statement to be executed. It must match the SQL syntax.
 * @param args A collection of pairs where each pair consists of a column type and a value
 *             to be bound to the SQL statement. Defaults to an empty list if no arguments are provided.
 * @param explicitStatementType An optional statement type to explicitly define whether
 *                              the statement is a SELECT, UPDATE, etc. Can be null.
 *
 * @return A table constructed from the executed SQL result set, or an empty table if the
 *         result set is null.
 * @since 5.3.5
 */
fun <T> JdbcTransaction.execToTable(
    @Language("sql") stmt: String,
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null
) = exec(stmt, args, explicitStatementType) { rs -> rs.toTable<T>() }.orEmpty()
/**
 * Executes the provided SQL query within a transaction and maps the result set into a transformed collection
 * of elements based on a specified transformation function. The result set is converted into a `Table`,
 * and each row is processed and transformed accordingly.
 *
 * @param stmt The SQL statement to be executed, represented as a string.
 * @param args Optional collection of pairs where each pair consists of an `IColumnType` and its
 *             corresponding value. These pairs dictate the parameters to be substituted in the SQL
 *             statement, if present. Defaults to an empty list.
 * @param explicitStatementType Optional parameter specifying the type of SQL statement (e.g., SELECT,
 *                              INSERT). If not provided, it will be inferred based on the query.
 *                              Defaults to null.
 * @param transform A transformation function that takes a row of the table (as an instance of
 *                  `Row<Int, String, T>`) and produces a result of type `R`. Each row in the table
 *                  will be transformed using this function.
 * @return A list containing transformed elements of type `R`, derived from the rows of the result set.
 * @since 5.3.5
 */
fun <T, R> JdbcTransaction.execToTable(
    @Language("sql") stmt: String,
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null,
    transform: Transformer<ResultRow<T>, R>
) = exec(stmt, args, explicitStatementType) { rs ->
    rs.toTable<T>().rows.map { it.value }.map(transform)
}.orEmpty()
/**
 * Executes the given SQL query within the current JDBC transaction and converts the result set into a data table.
 *
 * @param query The SQL query to execute, encapsulated in a SqlQuery object.
 * @param args A collection of column type and value pairs used as parameters for the query. Defaults to an empty list.
 * @param explicitStatementType An optional explicit statement type to override the default behavior. Defaults to null.
 * @return A data table of type T created from the result set, or an empty table if the result set is null or empty.
 * @since 5.3.5
 */
fun <T> JdbcTransaction.execToTable(
    query: SqlQuery,
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null
) = exec(query.value, args, explicitStatementType) { rs -> rs.toTable<T>() }.orEmpty()
/**
 * Executes a given SQL query within a `JdbcTransaction` and transforms the result into a list of objects of type `R`.
 * The result set is first converted into a table of rows and then transformed using the provided transformer function.
 *
 * @param T The generic type parameter for the table rows.
 * @param R The generic type parameter for the return type of the transformation.
 * @param query The SQL query to execute.
 * @param args A list of argument pairs consisting of an `IColumnType` and its corresponding value to bind to the query (optional, default is an empty list).
 * @param explicitStatementType The explicit statement type to use for the query execution (optional, default is `null`).
 * @param transform The transformation function to apply to each row in the table after conversion.
 * @return A list of transformed objects of type `R`, or an empty list if the query execution fails or produces no results.
 * @since 5.3.5
 */
fun <T, R> JdbcTransaction.execToTable(
    query: SqlQuery,
    args: Iterable<Pair<IColumnType<*>, Any?>> = emptyList(),
    explicitStatementType: StatementType? = null,
    transform: Transformer<ResultRow<T>, R>
) = exec(query.value, args, explicitStatementType) { rs ->
    rs.toTable<T>().rows.map { it.value }.map(transform)
}.orEmpty()

/**
 * Indicates whether the entity class contains no records.
 *
 * This property evaluates to `true` if the count of entities in the class is zero,
 * otherwise it evaluates to `false`.
 * @since 5.3.0
 */
val <ID : Any, T : Entity<ID>> EntityClass<ID, T>.isEmpty get() = count().isZero
/**
 * Provides a read-only property to determine if the entity class contains any entities.
 *
 * This property returns true if the entity class is not empty, meaning it has at least one entity,
 * and false if the entity class is empty.
 * @since 5.3.0
 */
val <ID : Any, T : Entity<ID>> EntityClass<ID, T>.isNotEmpty get() = !isEmpty

/**
 * Retrieves the first entity from the results of querying all entities in this entity class.
 *
 * @receiver The entity class from which all entities are queried.
 * @return The first entity from the queried results.
 * @throws NoSuchElementException if the collection of entities is empty.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.first() = all().limit(1).first()
/**
 * Retrieves the first entity that matches the given predicate from the collection of all entities.
 *
 * @param predicate A lambda function that specifies the condition to filter the entities.
 * @return The first entity matching the specified predicate.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.first(predicate: Predicate<T>) = all().limit(1).first(predicate)
/**
 * Returns the first entity from the result set or `null` if the result set is empty.
 *
 * This method fetches all entities managed by this [EntityClass], evaluates the sequence,
 * and retrieves the first element if available. If the sequence contains no elements, `null`
 * is returned.
 *
 * @receiver The [EntityClass] instance representing the type of entities being queried.
 * @return The first entity of type [T] if present, or `null` if no entities exist.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.firstOrNull() = all().limit(1).firstOrNull()
/**
 * Retrieves the first entity from the result of `all()` that matches the provided predicate.
 * If no entity matches the predicate, returns null.
 *
 * @param predicate A function that defines the condition to be matched by an entity.
 * @return The first entity matching the predicate, or null if no such entity is found.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.firstOrNull(predicate: Predicate<T>) = all().limit(1).firstOrNull(predicate)
/**
 * Retrieves the first entity in the result set or throws an exception if the result set is empty.
 *
 * @param lazyException a supplier function that creates the exception to be thrown if no entities are found
 * @receiver the entity class on which the operation is performed
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.firstOrThrow(lazyException: ThrowableSupplier) = all().limit(1).firstOrThrow(lazyException)
/**
 * Returns the first entity in the collection that matches the given [predicate].
 * If no such entity is found, an exception provided by [lazyException] is thrown.
 *
 * @param lazyException a supplier function that provides the exception to be thrown if no entity matches the predicate
 * @param predicate a condition used to determine the matching entity
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.firstOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<T>) = all().limit(1).firstOrThrow(lazyException, predicate)
/**
 * Returns the first entity in the query result if it exists, otherwise returns the value produced by the provided [default] function.
 *
 * @receiver the entity class on which the query is being executed
 * @param default a supplier function that provides a default entity if no results are found
 * @return the first entity from the query results or the value produced by the [default] function
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.firstOr(default: Supplier<T>) = all().limit(1).firstOr(default)
/**
 * Returns the first entity matching the specified [predicate], or the result of the [default] supplier
 * if no such entity is found.
 *
 * @param default a supplier function that provides a fallback entity if no entity matches the [predicate].
 * @param predicate a condition to be checked against each entity in the collection.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.firstOr(default: Supplier<T>, predicate: Predicate<T>) = all().limit(1).firstOr(default, predicate)

/**
 * Returns the single entity in the query result if it contains exactly one entity.
 * Throws an exception otherwise:
 * - [NoSuchElementException] if the query result is empty.
 * - [TooManyElementsException] if the query result contains more than one entity.
 *
 * @receiver The [EntityClass] representing the database table or view.
 * @return The single entity in the query result.
 * @throws NoSuchElementException if the query result is empty.
 * @throws TooManyElementsException if the query result contains more than one entity.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.onlyElement() = all().onlyElement()
/**
 * Retrieves a single element from the entity class collection that satisfies the given predicate.
 * If no elements or more than one element satisfy the predicate, an exception is thrown.
 *
 * @param predicate a condition to filter the elements in the entity class collection
 * @throws NoSuchElementException if no elements satisfy the predicate
 * @throws TooFewResultsException if the resulting size after filtering is less than the expected minimum (1)
 * @throws TooManyResultsException if more than one element satisfies the predicate
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.onlyElement(predicate: Predicate<T>) = all().onlyElement(predicate)
/**
 * Returns the single entity in the collection if it contains exactly one entity,
 * or `null` if the collection is empty or contains more than one entity.
 *
 * @receiver the [EntityClass] representing the database table
 * @return the single entity of type [T] if the collection size is exactly one, or `null` otherwise
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.onlyElementOrNull() = all().onlyElementOrNull()
/**
 * Returns the single entity matching the given [predicate], or `null` if no such entity exists
 * or if there is more than one matching entity in the collection.
 *
 * @param predicate A lambda function used to filter entities in the collection. The function
 * should return `true` for entities to be included in the operation.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.onlyElementOrNull(predicate: Predicate<T>) = all().onlyElementOrNull(predicate)
/**
 * Retrieves the only element in the entity query or throws an exception if the query does not yield exactly one result.
 *
 * @param lazyException a supplier function that provides the exception to be thrown when the query does not contain exactly one entity
 * @return the single element of the entity query if there is exactly one entity
 * @throws Throwable the exception supplied by the `lazyException` if the query does not contain exactly one entity
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.onlyElementOrThrow(lazyException: ThrowableSupplier) = all().onlyElementOrThrow(lazyException)
/**
 * Returns the single entity matching the given [predicate] or throws an exception
 * provided by [lazyException] if the condition is not met. Operates within the scope
 * of the current [EntityClass].
 *
 * @param lazyException a supplier for the exception to be thrown if the number of matching entities
 * is not exactly one.
 * @param predicate a condition to be checked for each entity in the [EntityClass].
 * @throws Throwable the exception supplied by [lazyException] if no entity or more than one entity matches.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.onlyElementOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<T>) = all().onlyElementOrThrow(lazyException, predicate)
/**
 * Returns the single entity in the query result if it contains exactly one entity; otherwise,
 * it returns the value supplied by the provided [default] supplier.
 *
 * @param ID the type of the entity identifier
 * @param T the type of the entity
 * @param default a supplier that provides a default entity if the query result does not contain exactly one entity
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.onlyElementOr(default: Supplier<T>) = all().onlyElementOr(default)
/**
 * Returns the single entity that matches the given predicate if exactly one entity matches,
 * otherwise returns the result from the default supplier.
 *
 * @param default A supplier function that provides a default value when no entity
 * or more than one entity matches the predicate.
 * @param predicate A predicate to filter the entities in the collection.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.onlyElementOr(default: Supplier<T>, predicate: Predicate<T>) = all().onlyElementOr(default, predicate)

/**
 * Retrieves the last entity from the collection of all entities managed by this [EntityClass].
 *
 * This function fetches all the entities of the specified type and returns the last one
 * in the sequence.
 *
 * @receiver The [EntityClass] instance used to query entities of type `T` identified by `ID`.
 * @return The last entity of type `T`.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.last() = all().last()
/**
 * Retrieves the last entity from the collection of entities that matches the given predicate.
 *
 * @param predicate A condition used to filter entities. The last entity satisfying this condition will be returned.
 * @return The last entity that matches the given predicate.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.last(predicate: Predicate<T>) = all().last(predicate)
/**
 * Returns the last entity in the result set of this EntityClass or `null` if the result set is empty.
 *
 * This function fetches all entities from the database and retrieves the last one based on the iteration order.
 * Can be useful for scenarios where the last entity in the dataset is required but the dataset may be empty.
 *
 * Note: Fetching all entities might be resource-intensive for large datasets. Use with caution.
 *
 * @receiver The EntityClass to query.
 * @return The last entity in the result set or `null` if no entities are found.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.lastOrNull() = all().lastOrNull()
/**
 * Returns the last entity in the collection that matches the given predicate,
 * or null if no such entity is found.
 *
 * @param predicate The condition used to filter the entities.
 * @return The last entity matching the predicate, or null if none match.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.lastOrNull(predicate: Predicate<T>) = all().lastOrNull(predicate)
/**
 * Returns the last entity in the query result or throws an exception provided by the given supplier if no entities are found.
 *
 * @param lazyException A supplier that provides the exception to be thrown if the query result is empty.
 * @throws Throwable If the query result is empty, the exception provided by the supplier is thrown.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.lastOrThrow(lazyException: ThrowableSupplier) = all().lastOrThrow(lazyException)
/**
 * Returns the last entity matching the provided [predicate] from the current entity collection,
 * or throws an exception supplied by [lazyException] if no such entity exists.
 *
 * @param lazyException A supplier function that provides the exception to be thrown if no matching entity is found.
 * @param predicate A condition used to determine the entity to select as the last match.
 * @throws Throwable The exception supplied by [lazyException] if no entity matches the given [predicate].
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.lastOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<T>) = all().lastOrThrow(lazyException, predicate)
/**
 * Returns the last entity of the collection or the result of invoking the specified default supplier
 * if the collection is empty.
 *
 * @param default A supplier function that is invoked to provide a default entity if the collection is empty.
 * @return The last entity of the collection or the result of the default supplier if the collection is empty.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.lastOr(default: Supplier<T>) = all().lastOr(default)
/**
 * Returns the last entity of the class that matches the specified [predicate].
 * If no such entity is found, returns the result of the [default] supplier.
 *
 * @param default A supplier function that provides a default value if no entity matches the [predicate].
 * @param predicate A predicate function to evaluate entities of the class.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.lastOr(default: Supplier<T>, predicate: Predicate<T>) = all().lastOr(default, predicate)

/**
 * Retrieves the first entity that matches the given operation condition or returns null if no match is found.
 *
 * @param op The operation condition used to filter entities.
 * @return The first entity matching the condition or null.
 * @since 5.3.6
 */
operator fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.get(op: Op<Boolean>) = find(op).firstOrNull()
/**
 * Retrieves the first entity matching the given condition, or null if no such entity exists.
 *
 * @param op A supplier providing the condition encapsulated in an [Op] object.
 *           This specifies the criteria used to filter entities.
 * @return The first entity that matches the supplied condition, or null if no match is found.
 * @since 5.3.6
 */
operator fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.get(op: Supplier<Op<Boolean>>) = find(op).firstOrNull()

/**
 * Checks if any entities in this EntityClass satisfy the given condition.
 *
 * @param op The condition represented as an instance of Op<Boolean>
 *           to evaluate against the entities.
 * @return True if at least one entity matches the condition, otherwise false.
 * @since 5.3.6
 */
operator fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.contains(op: Op<Boolean>) = count(op) > 0

/**
 * Checks whether an entity with the given ID exists in the associated entity class.
 *
 * @param id The ID of the entity to check for existence.
 * @return `true` if an entity with the specified ID exists, `false` otherwise.
 * @since 5.3.0
 */
operator fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.contains(id: ID) = existsById(id)
/**
 * Checks if any entity in the table satisfies the conditions defined by the given operation.
 *
 * @param op A supplier function that provides an operation (condition) to be applied to the table.
 * @return True if at least one entity matches the condition, false otherwise.
 * @since 5.5.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.exists(op: Supplier<Op<Boolean>>) = count(op()) > 0
/**
 * Checks if an entity with the specified ID exists.
 *
 * @param id The ID of the entity to check for existence.
 * @return `true` if an entity with the given ID exists, `false` otherwise.
 * @since 5.3.6
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.existsById(id: ID): Boolean {
    findById(id) ?: return false
    return true
}
/**
 * Checks whether an entity with the specified ID exists in the database.
 * If the entity does not exist, a given exception is thrown.
 *
 * @param id The identifier of the entity to look for.
 * @param lazyException A function that supplies the exception to be thrown
 *        if the entity with the given ID does not exist. Defaults to a
 *        `ResourceNotFoundException` with the provided ID and entity class type.
 * @return `true` if the entity with the specified ID exists.
 * @throws Throwable The exception provided by the `lazyException` supplier
 *         if the entity is not found.
 * @since 5.3.0
 */
@IgnorableReturnValue
inline fun <ID : Any, reified T : Entity<ID>> EntityClass<ID, T>.existsByIdOrThrow(id: ID, lazyException: ThrowableSupplier = { ResourceNotFoundException(id, T::class) }): Boolean {
    findById(id) ?: throw lazyException()
    return true
}
/**
 * Checks if an entity with the given ID exists in the database. If the entity does not exist, a `ResourceNotFoundException`
 * is thrown with the message provided by the `lazyMessage` supplier.
 *
 * @param id The ID of the entity to check.
 * @param lazyMessage A supplier that provides the exception message if the entity is not found.
 * @return `true` if the entity exists; otherwise, this method throws an exception.
 * @throws ResourceNotFoundException if the entity with the given ID is not found.
 * @since 5.3.0
 */
@IgnorableReturnValue
@JvmName("existsByIdOrThrowLazyMessage")
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.existsByIdOrThrow(id: ID, lazyMessage: Supplier<Any>): Boolean {
    findById(id) ?: throw ResourceNotFoundException(lazyMessage().toString())
    return true
}
/**
 * Checks for the existence of an entity by its ID, throwing a ResourceNotFoundException if not found.
 *
 * @param id The ID of the entity to check for existence.
 * @param internalErrorCode An optional error code for detailed error handling in case the entity is not found.
 * @return `true` if the entity exists, otherwise a ResourceNotFoundException is thrown.
 * @since 5.3.0
 */
@IgnorableReturnValue
inline fun <ID : Any, reified T : Entity<ID>> EntityClass<ID, T>.existsByIdOrThrow(id: ID, internalErrorCode: String?): Boolean {
    findById(id) ?: throw ResourceNotFoundException(id, T::class, internalErrorCode)
    return true
}
/**
 * Checks if an entity with the specified ID exists within the EntityClass.
 * If the entity does not exist, throws a ResourceNotFoundException with the provided error code
 * and message generated by the lazy message supplier.
 *
 * @param id The unique identifier of the entity to check for existence.
 * @param internalErrorCode The error code to include in the exception if the entity is not found. Can be null.
 * @param lazyMesage A supplier that provides the message for the exception when the entity is not found.
 * @return `true` if the entity exists; otherwise, a ResourceNotFoundException is thrown.
 * @since 5.3.0
 */
@IgnorableReturnValue
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.existsByIdOrThrow(id: ID, internalErrorCode: String?, lazyMesage: Supplier<Any>): Boolean {
    findById(id) ?: throw ResourceNotFoundException(lazyMesage().toString(), internalErrorCode)
    return true
}

/**
 * Finds an entity by its ID or throws an exception if the entity is not found.
 *
 * @param ID The type of the entity's ID.
 * @param T The type of the entity being retrieved.
 * @param id The unique identifier of the entity to find.
 * @param lazyException A lambda function that supplies the exception to be thrown if the entity is not found.
 * Defaults to throwing a [ResourceNotFoundException] with the ID and entity type.
 * @return The entity corresponding to the specified ID if found.
 * @throws Throwable The exception supplied by [lazyException] if the entity is not found.
 * @since 5.3.0
 */
inline fun <ID : Any, reified T : Entity<ID>> EntityClass<ID, T>.findByIdOrThrow(id: ID, lazyException: ThrowableSupplier = { ResourceNotFoundException(id, T::class) }) =
    findById(id) ?: throw lazyException()
/**
 * Finds an entity by its ID or throws a [ResourceNotFoundException] if not found.
 *
 * @param ID The type of the entity ID.
 * @param T The type of the entity.
 * @param id The ID of the entity to find.
 * @param lazyMessage A supplier function that provides the exception message when the entity is not found.
 * @throws ResourceNotFoundException If the entity with the specified ID is not found.
 * @since 5.3.0
 */
@JvmName("findByIdOrThrowLazyMessage")
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.findByIdOrThrow(id: ID, lazyMessage: Supplier<Any>) =
    findById(id) ?: throw ResourceNotFoundException(lazyMessage().toString())
/**
 * Finds an entity by its ID or throws a [ResourceNotFoundException] if the entity is not found.
 * This method is an inline extension for [EntityClass].
 *
 * @param ID The type of the ID used to identify the entity.
 * @param T The type of the entity being retrieved.
 * @param id The ID of the entity to find.
 * @param internalErrorCode An optional internal error code to include in the exception if the entity is not found.
 * @throws ResourceNotFoundException If the entity with the specified ID is not found.
 * @since 5.3.0
 */
inline fun <ID : Any, reified T : Entity<ID>> EntityClass<ID, T>.findByIdOrThrow(id: ID, internalErrorCode: String?) =
    findById(id) ?: throw ResourceNotFoundException(id, T::class, internalErrorCode)
/**
 * Finds an entity by its ID or throws a [ResourceNotFoundException] if the entity is not found.
 *
 * @param id The ID of the entity to find.
 * @param internalErrorCode An optional error code to include in the exception if the entity is not found.
 * @param lazyMesage A supplier function that provides the lazily computed message for the exception.
 * @throws ResourceNotFoundException If no entity with the given ID is found.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.findByIdOrThrow(id: ID, internalErrorCode: String?, lazyMesage: Supplier<Any>) =
    findById(id) ?: throw ResourceNotFoundException(lazyMesage().toString(), internalErrorCode)
/**
 * Finds an entity by its ID or throws a specified exception if it is not found, then applies
 * the provided update logic to the found entity.
 *
 * @param ID The type of the entity's identifier.
 * @param T The type of the entity being handled.
 * @param id The ID of the entity to look for.
 * @param lazyException A supplier that provides the exception to throw if the entity is not found.
 * Defaults to throwing a [ResourceNotFoundException] for the specified ID and entity type.
 * @param block A consumer function that specifies the update logic to apply to the found entity.
 * The entity is passed as a parameter to this block.
 * @return The updated entity after applying the update logic.
 * @throws Throwable If the entity with the specified ID is not found, the exception provided by
 * the `lazyException` supplier is thrown.
 * @since 5.3.0
 */
@IgnorableReturnValue
inline fun <ID : Any, reified T : Entity<ID>> EntityClass<ID, T>.findByIdOrThrowAndUpdate(
    id: ID,
    lazyException: ThrowableSupplier = { ResourceNotFoundException(id, T::class) },
    block: Consumer<T>
): T {
    val result = find(table.id eq id).forUpdate().singleOrNull() ?: throw lazyException()
    block(result)
    return result
}

/**
 * Finds an entity by its ID or returns a default value if the entity is not found.
 *
 * @param ID The type of the entity's ID.
 * @param T The type of the entity.
 * @param id The ID of the entity to find.
 * @param default A supplier function that provides a default entity if the entity is not found.
 * @return The entity with the specified ID or the default entity provided by the supplier.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.findByIdOr(id: ID, default: Supplier<T>) =
    findById(id) ?: default()
/**
 * Finds an entity by its ID or creates and saves a new entity if none exists.
 *
 * @param ID The type of the primary key of the entity.
 * @param T The type of the entity.
 * @param id The primary key of the entity to find.
 * @param new A lambda function that creates a new entity if none is found.
 * @return The existing or newly created entity.
 * @since 5.3.1
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.findByIdOrSave(id: ID, new: ReceiverConsumer<T>) =
    findById(id) ?: new(id, new)
/**
 * Finds an entity by its ID and locks it for update, or creates a new entity if none exists.
 * If a new entity is created, the provided `new` function is used to initialize it.
 * Once an entity is found or created, the specified `block` operation is applied to the entity.
 *
 * @param id The unique identifier of the entity to find or create.
 * @param new A function that handles the creation of a new entity when no matching entity is found.
 * @param block A function that performs an operation on the found or newly created entity.
 * @return The entity that was found or created and updated.
 * @since 5.3.1
 */
@IgnorableReturnValue
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.findByIdOrSaveAndUpdate(id: ID, new: ReceiverConsumer<T>, block: Consumer<T>): T {
    val result = find(table.id eq id).forUpdate().singleOrNull() ?: new(id, new)
    block(result)
    return result
}

/**
 * Creates a new entity of the specified type with the given ID.
 *
 * @param id The ID of the new entity to be created.
 * @return The newly created entity instance.
 * @since 5.5.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.new(id: ID) = new(id) {}

/**
 * Adds a new instance of an entity to the entity class using the specified initialization block.
 *
 * This operator function allows for the creation and addition of a new entity by applying the provided
 * initialization block to define its properties.
 *
 * @param init A lambda expression that initializes the properties of the newly created entity.
 * @since 5.3.0
 */
operator fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.plusAssign(init: ReceiverConsumer<T>) { new(init) }
/**
 * Adds and initializes a new entity in the EntityClass.
 * This operator function allows you to create and add a new entity
 * to the EntityClass using a pair consisting of an ID and an initialization block.
 *
 * @param init A pair where the first element is the ID of the new entity,
 * and the second element is a consumer function used to initialize the entity.
 * @since 5.3.0
 */
operator fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.plusAssign(init: Pair<ID, ReceiverConsumer<T>>) { new(init.first, init.second) }
/**
 * Adds a new entity with the specified ID to the entity class.
 *
 * @param id The unique identifier of the entity to be added.
 * @since 5.5.0
 */
operator fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.plusAssign(id: ID) { new(id) {} }

/**
 * Adds an insert operation to the table using the provided body.
 *
 * @param body A lambda function that consumes the table receiver and the insert statement,
 *             allowing configuration of the insert statement to specify the data to be inserted.
 * @since 5.3.0
 */
operator fun <T : Table> T.plusAssign(body: ReceiverBiConsumer<T, InsertStatement<Number>>) { insert(body) }
/**
 * Deletes an entity with the specified ID from the database. The entity is retrieved using the ID,
 * and an exception is thrown if the entity cannot be found.
 *
 * @param ID The type of the entity's ID.
 * @param T The type of the entity being deleted.
 * @param id The unique identifier of the entity to delete.
 * @throws Throwable If the entity with the specified ID is not found.
 * @since 5.3.0
 */
inline operator fun <ID : Any, reified T : Entity<ID>> EntityClass<ID, T>.minusAssign(id: ID) { findByIdOrThrow(id).delete() }
/**
 * Removes the specified entity from the database by invoking its delete method.
 *
 * @param entity The entity to be removed. It must be an instance of the type associated with this EntityClass.
 * @since 5.5.0
 */
inline operator fun <ID : Any, reified T : Entity<ID>> EntityClass<ID, T>.minusAssign(entity: T) { entity.delete() }
/**
 * Provides a shorthand operator for deleting rows from the table where the specified condition is met.
 *
 * @param op A lambda function that defines the condition for deleting rows. It takes the receiver table and returns a boolean operation.
 * @since 5.3.0
 */
operator fun <T : Table> T.minusAssign(op: ReceiverTransformer<T, Op<Boolean>>) { deleteWhere(op = op) }

/**
 * Retrieves the value of the specified column as a `Byte` from the current row of the `ResultSet`.
 *
 * @param column The column from which the `Byte` value is to be retrieved.
 *               This is typically an instance of `Column` containing metadata about the column.
 * @since 5.4.1
 */
fun ResultSet.getByte(column: Column<*>) = getByte(column.name)
/**
 * Retrieves the value of the specified column as an integer from the ResultSet.
 *
 * @param column The column whose integer value is to be retrieved.
 * @since 5.4.1
 */
fun ResultSet.getInt(column: Column<*>) = getInt(column.name)
/**
 * Retrieves the value of the specified column as a `Long` from the current row of the `ResultSet`.
 *
 * @param column The column whose value is to be retrieved. The column is represented as a `Column<*>` object.
 * @since 5.4.1
 */
fun ResultSet.getLong(column: Column<*>) = getLong(column.name)
/**
 * Retrieves the value of the specified column as a floating-point number from the ResultSet.
 *
 * @param column The column definition whose associated floating-point value should be retrieved.
 * @since 5.4.1
 */
fun ResultSet.getFloat(column: Column<*>) = getFloat(column.name)
/**
 * Retrieves the value of the specified column as a double from the ResultSet.
 *
 * @param column The column from which the double value is to be extracted.
 * @since 5.4.1
 */
fun ResultSet.getDouble(column: Column<*>) = getDouble(column.name)
/**
 * Retrieves the value of the specified column as a boolean.
 *
 * @param column The column whose value is to be retrieved.
 * @since 5.4.1
 */
fun ResultSet.getBoolean(column: Column<*>) = getBoolean(column.name)
/**
 * Retrieves the value of the specified column as a String.
 *
 * @param column The column whose value is to be retrieved.
 * @return The value of the column as a String, or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getString(column: Column<*>): String? = getString(column.name)
/**
 * Retrieves the SQL array value from the specified column in the ResultSet.
 *
 * @param column The column from which the SQL array is to be retrieved.
 * @return The SQL array value associated with the specified column.
 * @since 5.4.1
 */
fun ResultSet.getArray(column: Column<*>): java.sql.Array = getArray(column.name)
/**
 * Retrieves the value of the specified column as a byte array.
 *
 * @param column The column whose data is to be retrieved.
 * @return The value of the column as a ByteArray, or null if the column value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getBytes(column: Column<*>): ByteArray? = getBytes(column.name)
/**
 * Retrieves the value of the designated column as a `BigDecimal` object.
 *
 * @param column The column whose value is to be retrieved. This parameter provides
 *               the column's metadata, including its name.
 * @return The column value as a `BigDecimal`, or `null` if the SQL value is `NULL`.
 * @since 5.4.1
 */
fun ResultSet.getBigDecimal(column: Column<*>): BigDecimal? = getBigDecimal(column.name)
/**
 * Retrieves the value of the specified column as a `java.sql.Date` from the current row of this `ResultSet`.
 *
 * @param column The column from which the date value is to be retrieved.
 * @return The `java.sql.Date` value for the specified column, or `null` if the column value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getDate(column: Column<*>): java.sql.Date? = getDate(column.name)
/**
 * Retrieves the date value from the specified column in the ResultSet using the provided calendar.
 *
 * @param column The column from which the date value is to be retrieved.
 * @param cal The calendar to use for time zone and locale-specific conversions.
 * @return The date value of the specified column or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getDate(column: Column<*>, cal: Calendar): java.sql.Date? = getDate(column.name, cal)
/**
 * Retrieves the value of the specified column as a LocalDate object.
 *
 * @param columnIndex the index of the column starting from 1, from which the LocalDate value is to be retrieved.
 * @return the LocalDate value of the specified column, or null if the value is SQL NULL or cannot be converted.
 * @since 5.4.1
 */
fun ResultSet.getLocalDate(columnIndex: Int): LocalDate? = getDate(columnIndex)?.toLocalDate()
/**
 * Retrieves the value of the specified column as a `LocalDate` object.
 *
 * @param columnIndex the index of the column from which to retrieve the value, starting from 1.
 * @param cal the `Calendar` object to use for constructing the date.
 * @return the `LocalDate` value of the specified column, or `null` if the value is SQL `NULL`.
 * @since 5.4.1
 */
fun ResultSet.getLocalDate(columnIndex: Int, cal: Calendar): LocalDate? = getDate(columnIndex, cal)?.toLocalDate()
/**
 * Retrieves the value of the specified column as a LocalDate from the ResultSet.
 *
 * @param columnLabel the label for the column in the ResultSet from which the LocalDate is to be retrieved
 * @return the LocalDate value corresponding to the columnLabel, or null if the value is SQL NULL
 * @since 5.4.1
 */
fun ResultSet.getLocalDate(columnLabel: String): LocalDate? = getDate(columnLabel)?.toLocalDate()
/**
 * Retrieves the value of the specified column labeled `columnLabel` from the ResultSet as a LocalDate.
 *
 * @param columnLabel the label of the column from which the value is to be retrieved.
 * @param cal the Calendar object to use for constructing the LocalDate.
 * @return the LocalDate corresponding to the column value, or null if the column value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getLocalDate(columnLabel: String, cal: Calendar): LocalDate? = getDate(columnLabel, cal)?.toLocalDate()
/**
 * Retrieves the value of the specified column as a LocalDate.
 *
 * @param column The column from which to retrieve the date value.
 * @return The LocalDate value of the specified column, or null if the column value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getLocalDate(column: Column<*>): LocalDate? = getDate(column.name)?.toLocalDate()
/**
 * Retrieves a LocalDate value from the specified column of the ResultSet.
 *
 * @param column the column whose value should be retrieved
 * @param cal the calendar instance to use for time zone adjustments
 * @return the LocalDate value of the specified column, or null if the column value is SQL NULL
 * @since 5.4.1
 */
fun ResultSet.getLocalDate(column: Column<*>, cal: Calendar): LocalDate? = getDate(column.name, cal)?.toLocalDate()
/**
 * Retrieves the time value from the provided ResultSet column.
 *
 * @param column The column from which to retrieve the time value.
 * @return The time value as a Time object, or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getTime(column: Column<*>): Time? = getTime(column.name)
/**
 * Retrieves the value of the specified column as a Time object, using the provided Calendar
 * to interpret the time value if it contains timezone information.
 *
 * @param column The column whose value is to be retrieved.
 * @param cal The Calendar instance to use for timezone and locale information when interpreting the time value.
 * @return The column value as a Time object, or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getTime(column: Column<*>, cal: Calendar): Time? = getTime(column.name, cal)
/**
 * Retrieves the value of the designated column in the current row of this ResultSet
 * as a LocalTime object. If the SQL time at the specified column is null, this method
 * returns null.
 *
 * @param columnIndex the index of the column from which to retrieve the time value,
 *                    using a 1-based index as per JDBC conventions
 * @return the time value as a LocalTime object, or null if the value in the column is SQL NULL
 * @since 5.4.1
 */
fun ResultSet.getLocalTime(columnIndex: Int): LocalTime? = getTime(columnIndex)?.toLocalTime()
/**
 * Retrieves the LocalTime value from the specified column index of the current row in the ResultSet.
 *
 * @param columnIndex the index of the column from which to retrieve the time, starting from 1.
 * @param cal the Calendar instance to use for interpreting the time values.
 * @return a LocalTime object representing the time value of the specified column,
 *         or null if the SQL value is null.
 * @since 5.4.1
 */
fun ResultSet.getLocalTime(columnIndex: Int, cal: Calendar): LocalTime? = getTime(columnIndex, cal)?.toLocalTime()
/**
 * Retrieves the time value from the specified column in the ResultSet and converts it to a LocalTime.
 *
 * @param columnLabel The label for the column from which to retrieve the time.
 * @return The LocalTime representation of the time value in the specified column,
 *         or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getLocalTime(columnLabel: String): LocalTime? = getTime(columnLabel)?.toLocalTime()
/**
 * Retrieves the value of the specified column as a LocalTime from the ResultSet.
 *
 * @param columnLabel the label of the column whose value is to be retrieved
 * @param cal the Calendar instance to use for the timezone conversion
 * @return the LocalTime representation of the column's value, or null if the value is SQL NULL
 * @since 5.4.1
 */
fun ResultSet.getLocalTime(columnLabel: String, cal: Calendar): LocalTime? = getTime(columnLabel, cal)?.toLocalTime()
/**
 * Retrieves the value of the specified column as a LocalTime from the ResultSet.
 *
 * @param column The column whose value is to be retrieved as a LocalTime.
 * @return The LocalTime representation of the column value if it exists, or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getLocalTime(column: Column<*>): LocalTime? = getTime(column.name)?.toLocalTime()
/**
 * Retrieves the value of the specified column as a LocalTime object using the provided calendar.
 *
 * @param column The column whose value is to be retrieved.
 * @param cal The calendar to use for constructing the LocalTime object.
 * @return The column value as a LocalTime, or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getLocalTime(column: Column<*>, cal: Calendar): LocalTime? = getTime(column.name, cal)?.toLocalTime()
/**
 * Retrieves the value of the specified column as a `Timestamp` from the `ResultSet`.
 *
 * @param column The column from which to retrieve the timestamp. This should be an instance of `Column`.
 * @return The `Timestamp` value of the specified column, or `null` if the value is SQL `NULL`.
 * @since 5.4.1
 */
fun ResultSet.getDateTime(column: Column<*>): Timestamp? = getTimestamp(column.name)
/**
 * Retrieves the value of the specified column as a `Timestamp` object using the provided calendar.
 *
 * @param column The column whose value is to be retrieved.
 * @param cal The calendar to use for constructing the `Timestamp` object.
 * @return The `Timestamp` value of the specified column, or `null` if the value is SQL `NULL`.
 * @since 5.4.1
 */
fun ResultSet.getDateTime(column: Column<*>, cal: Calendar): Timestamp? = getTimestamp(column.name, cal)
/**
 * Retrieves a LocalDateTime value from the specified column in the current row of the ResultSet.
 *
 * @param columnIndex the index of the column from which to retrieve the LocalDateTime value.
 *                    The index is 1-based (the first column is at index 1).
 * @return the LocalDateTime value of the specified column if it exists, or null if the column value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getLocalDateTime(columnIndex: Int): LocalDateTime? = getTimestamp(columnIndex)?.toLocalDateTime()
/**
 * Retrieves the value of a specified column as a LocalDateTime object, using the provided Calendar
 * to construct the date-time representation of the SQL timestamp.
 *
 * @param columnIndex the index of the column from which the value is to be retrieved.
 * @param cal the Calendar to use for constructing the date-time, allowing for timezone corrections.
 * @return the LocalDateTime representation of the column value, or null if the column value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getLocalDateTime(columnIndex: Int, cal: Calendar): LocalDateTime? = getTimestamp(columnIndex, cal)?.toLocalDateTime()
/**
 * Retrieves a LocalDateTime from the specified column in the ResultSet.
 *
 * @param columnLabel the label of the column from which the LocalDateTime is to be retrieved
 * @return the LocalDateTime value of the specified column, or null if the value is SQL NULL
 * @since 5.4.1
 */
fun ResultSet.getLocalDateTime(columnLabel: String): LocalDateTime? = getTimestamp(columnLabel)?.toLocalDateTime()
/**
 * Retrieves a `LocalDateTime` value from the specified column of the `ResultSet`,
 * using the provided `Calendar` to interpret the timestamp. The method converts
 * the SQL `Timestamp` obtained from the column to a `LocalDateTime`.
 *
 * @param columnLabel the label for the column from which the timestamp is to be retrieved
 * @param cal the `Calendar` instance used to interpret the timestamp
 * @return the `LocalDateTime` representation of the timestamp, or `null` if the column value is SQL `NULL`
 * @since 5.4.1
 */
fun ResultSet.getLocalDateTime(columnLabel: String, cal: Calendar): LocalDateTime? = getTimestamp(columnLabel, cal)?.toLocalDateTime()
/**
 * Retrieves the value of the specified column as a LocalDateTime from the ResultSet.
 *
 * @param column The column whose value is to be retrieved.
 * @return The LocalDateTime representation of the column's value, or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getLocalDateTime(column: Column<*>): LocalDateTime? = getTimestamp(column.name)?.toLocalDateTime()
/**
 * Retrieves the value of the specified column as a LocalDateTime from the current ResultSet.
 *
 * @param column The column whose value is to be retrieved.
 * @param cal The calendar to be used for timezone conversions.
 * @return The LocalDateTime value of the specified column, or null if the column value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getLocalDateTime(column: Column<*>, cal: Calendar): LocalDateTime? = getTimestamp(column.name, cal)?.toLocalDateTime()
/**
 * Retrieves the value of the designated column in the current row of this `ResultSet` object
 * as an `OffsetDateTime` object or `null` if the SQL value is `NULL`.
 *
 * @param columnIndex the 1-based index of the column from which the data is to be retrieved.
 * @return the column value as an `OffsetDateTime` object, or `null` if the SQL value is `NULL`.
 * @since 5.4.1
 */
fun ResultSet.getOffsetDateTime(columnIndex: Int): OffsetDateTime? = getObject(columnIndex, OffsetDateTime::class.java)
/**
 * Retrieves the value of the specified column as an OffsetDateTime from the ResultSet.
 *
 * @param columnLabel the label for the column from which the OffsetDateTime is to be retrieved.
 * @return the OffsetDateTime value of the specified column or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getOffsetDateTime(columnLabel: String): OffsetDateTime? = getObject(columnLabel, OffsetDateTime::class.java)
/**
 * Retrieves the value of the specified column as an OffsetDateTime from the ResultSet.
 *
 * @param column The column from which to retrieve the OffsetDateTime value.
 * @return The OffsetDateTime value of the specified column, or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getOffsetDateTime(column: Column<*>): OffsetDateTime? = getObject(column.name, OffsetDateTime::class.java)
/**
 * Retrieves the value of the designated column in the current row of this `ResultSet` object
 * as an object of the specified type.
 *
 * @param T the type of the object to be retrieved
 * @param columnIndex the index of the column, starting from 1
 * @return the value of the column as an object of type `T`
 * @throws SQLException if a database access error occurs or the column index is invalid
 * @since 5.4.1
 */
inline fun <reified T> ResultSet.getObject(columnIndex: Int): T = getObject(columnIndex, T::class.java)
/**
 * Retrieves the value of the specified column in the current row of this ResultSet
 * as an object of the specified type.
 *
 * @param columnLabel the label of the column from which to retrieve the value
 * @return the value of the specified column of type T
 * @throws SQLException if a database access error occurs or the specified columnLabel is invalid
 * @throws ClassCastException if the retrieved value cannot be cast to the specified type T
 * @since 5.4.1
 */
inline fun <reified T> ResultSet.getObject(columnLabel: String): T = getObject(columnLabel, T::class.java)
/**
 * Retrieves the value of the specified column from the ResultSet and casts it to the provided type.
 *
 * @param T The type to which the object should be cast.
 * @param column The column descriptor containing the name of the column to retrieve.
 * @return The value of the specified column cast to the specified type.
 * @throws SQLException if a database access error occurs or the column label is invalid.
 * @throws ClassCastException if the object cannot be cast to the specified type.
 * @since 5.4.1
 */
inline fun <reified T> ResultSet.getObject(column: Column<*>): T = getObject(column.name, T::class.java)
/**
 * Retrieves the value of the specified column in the current row of this `ResultSet`
 * as an ASCII stream. This can be used to retrieve large ASCII character data.
 *
 * @param column The column whose value needs to be retrieved as an ASCII stream.
 * @return An `InputStream` containing the ASCII stream of the column value, or `null` if the value is SQL `NULL`.
 * @since 5.4.1
 */
fun ResultSet.getAsciiStream(column: Column<*>): InputStream? = getAsciiStream(column.name)
/**
 * Retrieves the binary stream for the specified column in the result set.
 *
 * @param column The column for which the binary stream is to be retrieved.
 * @return An InputStream representing the binary data of the specified column,
 * or null if the column value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getBinaryStream(column: Column<*>): InputStream? = getBinaryStream(column.name)
/**
 * Retrieves the value of the designated column in the current row of this ResultSet as a character stream.
 *
 * @param column The column whose character stream value is to be retrieved.
 * @return A Reader object that contains the column value as a character stream,
 *         or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getCharacterStream(column: Column<*>): Reader? = getCharacterStream(column.name)
/**
 * Retrieves the value of the designated column in the current row of this ResultSet
 * as a character stream for National Character Set (NCHAR, NVARCHAR, LONGNVARCHAR) values.
 *
 * @param column The column to retrieve the character stream from, represented as a Column object.
 * @return A Reader object that can be used to read the data of the specified column,
 *         or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getNCharacterStream(column: Column<*>): Reader? = getNCharacterStream(column.name)
/**
 * Retrieves the NClob value from the specified ResultSet column.
 *
 * @param column The column containing the NClob data.
 * @return The NClob value from the specified column.
 * @since 5.4.1
 */
fun ResultSet.getNClob(column: Column<*>): NClob = getNClob(column.name)
/**
 * Retrieves the value of the designated column in the current row of this ResultSet
 * as a String. The method is intended for use when the SQL type of the column is
 * NCHAR, NVARCHAR, or LONGNVARCHAR.
 *
 * @param column the column from which the value will be retrieved
 * @return the column value as a String, or null if the SQL value is SQL NULL
 * @since 5.4.1
 */
fun ResultSet.getNString(column: Column<*>): String? = getNString(column.name)
/**
 * Retrieves the value of the designated column in the current row of this ResultSet
 * as a Ref object.
 *
 * @param column The column whose value is to be retrieved, represented as a Column object.
 * @return The Ref object containing the SQL REF value of the specified column.
 * @since 5.4.1
 */
fun ResultSet.getRef(column: Column<*>): Ref = getRef(column.name)
/**
 * Retrieves the Clob object from the current row of this ResultSet for the specified column.
 *
 * @param column The column metadata object representing the target column.
 * @return The Clob object corresponding to the specified column.
 * @since 5.4.1
 */
fun ResultSet.getClob(column: Column<*>): Clob = getClob(column.name)
/**
 * Retrieves the RowId object for the specified column from the ResultSet.
 *
 * @param column The column whose RowId is to be retrieved.
 * @return The RowId object corresponding to the specified column, or null if it is not present.
 * @since 5.4.1
 */
fun ResultSet.getRowId(column: Column<*>): RowId? = getRowId(column.name)
/**
 * Retrieves the value of the designated column as an SQLXML object.
 *
 * @param column The specific column from which the SQLXML data is to be retrieved.
 * @return The SQLXML value of the specified column.
 * @since 5.4.1
 */
fun ResultSet.getSQLXML(column: Column<*>): SQLXML = getSQLXML(column.name)
/**
 * Retrieves the value of the specified column as a SQLXML object from the current row
 * of the ResultSet.
 *
 * @param columnIndex the column index (1-based) of the SQLXML value to retrieve
 * @return the SQLXML object representing the value of the specified column
 * @since 5.4.1
 */
fun ResultSet.getSqlXml(columnIndex: Int): SQLXML = getSQLXML(columnIndex)
/**
 * Retrieves the value of the specified column as an SQLXML object.
 *
 * @param columnLabel The label for the column from which to retrieve the value.
 * @return The SQLXML object representing the value of the specified column.
 * @since 5.4.1
 */
fun ResultSet.getSqlXml(columnLabel: String): SQLXML = getSQLXML(columnLabel)
/**
 * Retrieves the SQLXML object from the specified column in the ResultSet.
 *
 * @param column The Column object representing the column from which the SQLXML is to be retrieved.
 * @return The SQLXML object associated with the specified column.
 * @since 5.4.1
 */
fun ResultSet.getSqlXml(column: Column<*>): SQLXML = getSQLXML(column.name)
/**
 * Retrieves a URL object from the specified column in the ResultSet.
 *
 * @param column The column from which to retrieve the URL. It must be an instance of Column.
 * @return The URL object retrieved from the specified column, or null if the column value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getURL(column: Column<*>): Url? = getURL(column.name)
/**
 * Retrieves the value of the specified column as a Url object.
 *
 * @param columnIndex the index of the column, starting from 1
 * @return the Url object representing the value of the column, or null if the value is SQL NULL
 * @since 5.4.1
 */
fun ResultSet.getUrl(columnIndex: Int): Url? = getURL(columnIndex)
/**
 * Retrieves the value of the designated column in the current row of this ResultSet as a URL object.
 *
 * @param columnLabel the label for the column specified with the SQL AS clause. If the SQL AS clause was not
 *                    specified, then the label is the name of the column.
 * @return the column value as a Url object, or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getUrl(columnLabel: String): Url? = getURL(columnLabel)
/**
 * Retrieves the URL value from the specified column in the current row of the ResultSet.
 *
 * @param column The column from which the URL value is to be retrieved.
 * @return The URL value of the specified column, or null if the value is SQL NULL.
 * @since 5.4.1
 */
fun ResultSet.getUrl(column: Column<*>): Url? = getURL(column.name)

/**
 * Extracts a Byte value from the current row of a ResultSet at the specified column index.
 * If the ResultSet has no more rows, a default value is returned.
 *
 * @param columnIndex The column index to fetch the Byte value from, defaults to 1.
 * @param default A supplier function providing a default Byte value if the ResultSet has no more rows, defaults to null.
 * @return A Transformer that takes a ResultSet and returns a Byte value from the specified column or the default value.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun byteFromResultSet(columnIndex: Int = 1, default: Supplier<Byte?> = { null }): Transformer<ResultSet, Byte?> = {
    if (it.next()) it.getByte(columnIndex) else default()
}
/**
 * Extracts a Byte value from the current row of a ResultSet using the specified column label.
 * If the ResultSet is empty or the column value is unavailable, a default value is returned.
 *
 * @param columnLabel the label of the column from which the Byte value should be retrieved
 * @param default a supplier that provides a default value if the ResultSet is empty or the column is unavailable
 * @return a Transformer that processes a ResultSet and retrieves a Byte value or the default value
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun byteFromResultSet(columnLabel: String, default: Supplier<Byte?> = { null }): Transformer<ResultSet, Byte?> = {
    if (it.next()) it.getByte(columnLabel) else default()
}
/**
 * Retrieves a nullable `Byte` value from the specified column of a `ResultSet` while operating within
 * the context of a `JdbcTransaction`. If the `ResultSet` does not contain the value, a default value
 * provided by the `default` supplier is returned.
 *
 * @param column The column definition from which the `Byte` value is to be retrieved.
 * @param default A supplier providing a default `Byte?` value in case the `ResultSet` does not contain data.
 * @return A transformer that processes a `ResultSet` to return the `Byte?` value from the specified column.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun byteFromResultSet(column: Column<*>, default: Supplier<Byte?> = { null }): Transformer<ResultSet, Byte?> = {
    if (it.next()) it.getByte(column.name) else default()
}
/**
 * Transforms a ResultSet into an Int value by retrieving an integer from the specified column index,
 * or returns a default value if the ResultSet is empty.
 *
 * @param columnIndex The index of the column from which to retrieve the integer. Defaults to 1.
 * @param default A supplier function that provides a default integer value when the ResultSet is empty.
 * @return A transformer function that processes a ResultSet and returns an Int value or the default value.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun intFromResultSet(columnIndex: Int = 1, default: Supplier<Int?> = { 0 }): Transformer<ResultSet, Int?> = {
    if (it.next()) it.getInt(columnIndex) else default()
}
/**
 * Extracts an integer value from the provided `ResultSet` for the specified column label.
 *
 * @param columnLabel The label of the column from which to retrieve the integer value.
 * @return A transformer function that takes a `ResultSet` and returns an integer value or null
 *         if the result set has no more rows or the value is SQL NULL.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun intFromResultSet(columnLabel: String): Transformer<ResultSet, Int?> = {
    if (it.next()) it.getInt(columnLabel) else null
}
/**
 * Transforms a [ResultSet] to an optional integer value by extracting data from the specified column.
 *
 * @param column The column from which the integer value will be extracted.
 * @return A [Transformer] function that takes a [ResultSet] and returns an optional [Int].
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun intFromResultSet(column: Column<*>): Transformer<ResultSet, Int?> = {
    if (it.next()) it.getInt(column.name) else null
}
/**
 * Retrieves a nullable Long value from a `ResultSet` by the specified column index or provides a default value if no result is found.
 *
 * @param columnIndex The index of the column to retrieve the value from. Defaults to 1.
 * @param default A supplier function providing a default `Long` value in case the `ResultSet` has no result. Defaults to `0L`.
 * @return A transformer function that, when invoked with a `ResultSet`, retrieves the `Long` value or returns the default value.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun longFromResultSet(columnIndex: Int = 1, default: Supplier<Long?> = { 0L }): Transformer<ResultSet, Long?> = {
    if (it.next()) it.getLong(columnIndex) else default()
}
/**
 * Extracts a `Long` value from the specified column in the current row of a `ResultSet`.
 *
 * @param columnLabel The label of the column to retrieve the `Long` value from.
 * @param default A supplier function providing a default `Long` value if the column value is null or no rows exist.
 * @return A transformer function that processes a `ResultSet` and returns a `Long` value or null.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun longFromResultSet(columnLabel: String, default: Supplier<Long?> = { 0L }): Transformer<ResultSet, Long?> = {
    if (it.next()) it.getLong(columnLabel) else default()
}
/**
 * Extracts a `Long` value from the given `ResultSet` based on the specified column.
 *
 * @param column The column from which the value will be extracted.
 * @param default A supplier that provides a default `Long` value if the `ResultSet` does not contain
 *                any suitable value.
 * @return A transformer function that processes a `ResultSet` and returns the `Long` value
 *         from the specified column or the default value if no value is present.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun longFromResultSet(column: Column<*>, default: Supplier<Long?> = { 0L }): Transformer<ResultSet, Long?> = {
    if (it.next()) it.getLong(column.name) else default()
}
/**
 * Extracts a `Float` value from the given `ResultSet` using the specified column index.
 *
 * If the `ResultSet` does not contain a value at the specified column or no rows are available,
 * the provided default supplier will be used to return a fallback `Float?` value.
 *
 * @param columnIndex The index of the column in the `ResultSet` to retrieve the `Float` value from. Defaults to `1`.
 * @param default A supplier function providing the default `Float?` value to return if the `ResultSet` is empty or the column value is `null`. Defaults to a supplier returning `
 * 0f`.
 * @return A transformer function that takes a `ResultSet` and returns a `Float?` value from the specified column or the default value if applicable.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun floatFromResultSet(columnIndex: Int = 1, default: Supplier<Float?> = { 0f }): Transformer<ResultSet, Float?> = {
    if (it.next()) it.getFloat(columnIndex) else default()
}
/**
 * Extracts a floating-point value from the specified column in a `ResultSet`. If the `ResultSet`
 * has no more rows, the value provided by the default supplier is returned.
 *
 * @param columnLabel The label of the column from which the floating-point value is to be extracted.
 * @param default A supplier providing a default value to return if the `ResultSet` has no more rows.
 * @return A transformer that maps a `ResultSet` to a nullable `Float` extracted from the specified column.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun floatFromResultSet(columnLabel: String, default: Supplier<Float?> = { 0f }): Transformer<ResultSet, Float?> = {
    if (it.next()) it.getFloat(columnLabel) else default()
}
/**
 * Extracts a Float value from the current row of a ResultSet based on the specified column.
 *
 * @param column The column from which the float value will be retrieved.
 * @param default A supplier function that provides a fallback Float value if the column value is unavailable or no rows exist. The default implementation provides a value of 0f.
 * @return A Transformer that processes a ResultSet and retrieves a Float value, or the result of the default supplier if no value is available.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun floatFromResultSet(column: Column<*>, default: Supplier<Float?> = { 0f }): Transformer<ResultSet, Float?> = {
    if (it.next()) it.getFloat(column.name) else default()
}
/**
 * Transforms a given [ResultSet] to a nullable [Double] value by retrieving the value at the specified column index.
 *
 * @param columnIndex The index of the column to retrieve the [Double] value from. Defaults to 1.
 * @param default A supplier function providing a default [Double?] value to return if the [ResultSet] does not have a next entry. Defaults to 0.0.
 * @return A transformer function that takes a [ResultSet] and extracts the [Double?] value from the specified column,
 * or returns the supplied default value if no next entry exists in the result set.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun doubleFromResultSet(columnIndex: Int = 1, default: Supplier<Double?> = { 0.0 }): Transformer<ResultSet, Double?> = {
    if (it.next()) it.getDouble(columnIndex) else default()
}
/**
 * Extracts a `Double` value from the specified column in the current row of the given `ResultSet`.
 * If the `ResultSet` does not have any data or the column value is unavailable, the provided default value is used.
 *
 * @param columnLabel the label of the column from which the `Double` value should be extracted
 * @param default a supplier for the default `Double` value to return if the column value is unavailable or the `ResultSet` has no data
 * @return a transformer function that takes a `ResultSet` and returns a `Double?` value
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun doubleFromResultSet(columnLabel: String, default: Supplier<Double?> = { 0.0 }): Transformer<ResultSet, Double?> = {
    if (it.next()) it.getDouble(columnLabel) else default()
}
/**
 * Transforms a [ResultSet] to extract a `Double` from the specified column, or returns a default value if no results are found.
 *
 * @param column The column from which to extract the `Double` value.
 * @param default A supplier function returning the default value to use if the `ResultSet` is empty. Defaults to `0.0`.
 * @return A transformer function that processes a [ResultSet] and returns a `Double?`.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun doubleFromResultSet(column: Column<*>, default: Supplier<Double?> = { 0.0 }): Transformer<ResultSet, Double?> = {
    if (it.next()) it.getDouble(column.name) else default()
}
/**
 * Retrieves a boolean value from the current row in a ResultSet. If the ResultSet has no more rows,
 * the default value is returned.
 *
 * @param columnIndex The index of the column to retrieve the boolean value from. Defaults to 1.
 * @param default A supplier providing a default value if no rows are available in the ResultSet. Defaults to false.
 * @return A transformer function that takes a ResultSet and returns the retrieved boolean value.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun booleanFromResultSet(columnIndex: Int = 1, default: Supplier<Boolean?> = { false }): Transformer<ResultSet, Boolean?> = {
    if (it.next()) it.getBoolean(columnIndex) else default()
}
/**
 * Extracts a Boolean value from the given column in the ResultSet. If the column value is not found,
 * the provided default value supplier is used instead.
 *
 * @param columnLabel The label of the column from which the Boolean value should be retrieved.
 * @param default A supplier that provides a default Boolean value if the column value is not available.
 * @return A Transformer that processes the ResultSet to extract the Boolean value or default value.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun booleanFromResultSet(columnLabel: String, default: Supplier<Boolean?> = { false }): Transformer<ResultSet, Boolean?> = {
    if (it.next()) it.getBoolean(columnLabel) else default()
}
/**
 * Transforms a [ResultSet] into a nullable Boolean value based on the specified column.
 * If a value is present in the result set for the given column, it is returned as a Boolean.
 * Otherwise, the provided default function is used to supply a fallback Boolean value.
 *
 * @param column The column in the result set whose value needs to be retrieved as a Boolean.
 * @param default A supplier function that provides a default nullable Boolean value if the column is not found.
 * @return A transformer that processes a [ResultSet] and retrieves the Boolean value for the specified column
 *         or the default nullable Boolean value if the column is unavailable.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun booleanFromResultSet(column: Column<*>, default: Supplier<Boolean?> = { false }): Transformer<ResultSet, Boolean?> = {
    if (it.next()) it.getBoolean(column.name) else default()
}
/**
 * Extracts a string value from a given `ResultSet` based on the specified column index.
 * If the `ResultSet` has no more rows or the string value is null, a default value is provided.
 *
 * @param columnIndex The index of the column from which to fetch the string value. Defaults to 1.
 * @param default A supplier that provides the default value if the `ResultSet` has no rows or the value is null.
 * @return A transformer function that takes a `ResultSet` as input and returns the string value or the default.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun stringFromResultSet(columnIndex: Int = 1, default: Supplier<String?> = { null }): Transformer<ResultSet, String?> = {
    if (it.next()) it.getString(columnIndex) else default()
}
/**
 * Extracts a string value from the specified column in the given ResultSet. If the ResultSet is empty or the column value is null,
 * a default value provided by the supplier is returned.
 *
 * @param columnLabel The label of the column from which the string value is to be retrieved.
 * @param default A supplier function that provides a default value when the column value is null or the ResultSet is empty.
 *                Defaults to a supplier that returns `null`.
 * @return A transformer function that takes a ResultSet as input and returns a nullable string from the specified column,
 *         or the specified default value if the ResultSet is empty or the column value is null.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun stringFromResultSet(columnLabel: String, default: Supplier<String?> = { null }): Transformer<ResultSet, String?> = {
    if (it.next()) it.getString(columnLabel) else default()
}
/**
 * Transforms a ResultSet into a nullable String by extracting the value of the specified column.
 * If the ResultSet has no more rows, it returns the provided default value.
 *
 * @param column The column from which the String value will be extracted.
 * @param default A supplier function providing the default String value if the column value is not found.
 * @return A Transformer that processes a ResultSet and produces a nullable String.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun stringFromResultSet(column: Column<*>, default: Supplier<String?> = { null }): Transformer<ResultSet, String?> = {
    if (it.next()) it.getString(column.name) else default()
}
/**
 * Extracts a SQL array from the given `ResultSet` at the specified column index.
 * If the `ResultSet` has no more rows or the column value is null, a default value is returned.
 *
 * @param columnIndex The index of the column from which to retrieve the SQL array. Defaults to 1.
 * @param default A supplier function providing a default value if no array is found. Defaults to returning null.
 * @return A transformer function that processes a `ResultSet` and returns the extracted SQL array or the default value.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun arrayFromResultSet(columnIndex: Int = 1, default: Supplier<java.sql.Array?> = { null }): Transformer<ResultSet, java.sql.Array?> = {
    if (it.next()) it.getArray(columnIndex) else default()
}
/**
 * Extracts a SQL `Array` from the given `ResultSet` for the specified column label.
 * If the `ResultSet` does not have a value for the specified column or no rows
 * are available, the provided default supplier is used.
 *
 * @param columnLabel The label of the column from which to retrieve the SQL `Array`.
 * @param default A supplier function that provides a default `Array` value if no value
 *                is found in the `ResultSet`. Defaults to `null`.
 * @return A `Transformer` that takes a `ResultSet` and returns a `java.sql.Array?`
 *         for the given column label or the supplied default value.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun arrayFromResultSet(columnLabel: String, default: Supplier<java.sql.Array?> = { null }): Transformer<ResultSet, java.sql.Array?> = {
    if (it.next()) it.getArray(columnLabel) else default()
}
/**
 * Extracts a SQL Array from the provided ResultSet based on the specified column.
 * Returns the default value if the column value is unavailable.
 *
 * @param column The database column from which the SQL Array is extracted.
 * @param default A supplier that provides a default SQL Array to return if the ResultSet does not contain a value for the specified column.
 * @return A transformer function that takes a ResultSet and returns the SQL Array corresponding to the specified column, or the default value if not found.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun arrayFromResultSet(column: Column<*>, default: Supplier<java.sql.Array?> = { null }): Transformer<ResultSet, java.sql.Array?> = {
    if (it.next()) it.getArray(column.name) else default()
}
/**
 * Extracts a `ByteArray` from a `ResultSet` at the specified column index.
 * If the `ResultSet` does not have a next row, a default value is supplied.
 *
 * @param columnIndex The index of the column from which to extract the `ByteArray`. Defaults to 1.
 * @param default A supplier function that provides a default `ByteArray` value in case the `ResultSet`
 *                does not have a next row. Defaults to `null`.
 * @return A `Transformer` function that transforms a `ResultSet` into a `ByteArray?`.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun byteArrayFromResultSet(columnIndex: Int = 1, default: Supplier<ByteArray?> = { null }): Transformer<ResultSet, ByteArray?> = {
    if (it.next()) it.getBytes(columnIndex) else default()
}
/**
 * Transforms a `ResultSet` into a nullable `ByteArray` by extracting bytes from the specified column label.
 *
 * @param columnLabel The label of the column from which the byte array should be extracted.
 * @param default A supplier function that provides a default value if the `ResultSet` is empty or no data can be retrieved.
 * @return A transformer function that consumes a `ResultSet` and returns a nullable `ByteArray`.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun byteArrayFromResultSet(columnLabel: String, default: Supplier<ByteArray?> = { null }): Transformer<ResultSet, ByteArray?> = {
    if (it.next()) it.getBytes(columnLabel) else default()
}
/**
 * Extracts a `ByteArray` value from the given `ResultSet` for the specified column.
 * If the result set does not contain any rows, returns the default value provided.
 *
 * @param column The column from which to retrieve the `ByteArray` value.
 * @param default A supplier function providing the default value to return if no rows are available.
 * @return A transformer function that takes a `ResultSet` and returns the extracted `ByteArray` value, or the default value if no rows exist.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun byteArrayFromResultSet(column: Column<*>, default: Supplier<ByteArray?> = { null }): Transformer<ResultSet, ByteArray?> = {
    if (it.next()) it.getBytes(column.name) else default()
}
/**
 * Extracts a BigDecimal value from a ResultSet at the specified column index or
 * provides a default value if the column data is unavailable.
 *
 * @param columnIndex The column index from which the BigDecimal value is extracted. Defaults to 1.
 * @param default A supplier function providing a default BigDecimal value if the ResultSet does not contain a value
 *                at the specified column index.
 * @return A transformer function that takes a ResultSet as input and produces a BigDecimal value or null
 *         according to the extraction or default logic.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun bigDecimalFromResultSet(columnIndex: Int = 1, default: Supplier<BigDecimal?> = { null }): Transformer<ResultSet, BigDecimal?> = {
    if (it.next()) it.getBigDecimal(columnIndex) else default()
}
/**
 * Extracts a BigDecimal value from the specified column in the given ResultSet.
 * If the ResultSet has no more rows or the column value is null, a default value is returned.
 *
 * @param columnLabel The label of the column from which to retrieve the BigDecimal value.
 * @param default A supplier providing the default value to return if the column is null or if the ResultSet has no more rows.
 * @return A transformer function that takes a ResultSet and returns a nullable BigDecimal.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun bigDecimalFromResultSet(columnLabel: String, default: Supplier<BigDecimal?> = { null }): Transformer<ResultSet, BigDecimal?> = {
    if (it.next()) it.getBigDecimal(columnLabel) else default()
}
/**
 * Transforms a ResultSet to extract a BigDecimal value from the specified column.
 * If the ResultSet does not contain a value for the column, the default value supplier is used.
 *
 * @param column The column from which to extract the BigDecimal value.
 * @param default A supplier that provides a default BigDecimal value if no value is found
 *                in the ResultSet. Defaults to null.
 * @return A Transformer that processes the ResultSet and extracts the BigDecimal value.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun bigDecimalFromResultSet(column: Column<*>, default: Supplier<BigDecimal?> = { null }): Transformer<ResultSet, BigDecimal?> = {
    if (it.next()) it.getBigDecimal(column.name) else default()
}
/**
 * Extracts a LocalDate value from a ResultSet at the specified column index, or provides a default value if no date is found.
 *
 * @param columnIndex The index of the column in the ResultSet from which the date should be retrieved. Defaults to 1.
 * @param default A supplier function providing a default LocalDate value when the column does not contain a date or the ResultSet is empty.
 * @return A Transformer that processes a ResultSet and returns a LocalDate value or null, based on the ResultSet content and the default supplier.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun localDateFromResultSet(columnIndex: Int = 1, default: Supplier<java.time.LocalDate?> = { null }): Transformer<ResultSet, java.time.LocalDate?> = {
    if (it.next()) it.getDate(columnIndex)?.toLocalDate() else default()
}
/**
 * Extracts a `LocalDate` value from a `ResultSet` based on the specified column label.
 *
 * @param columnLabel the label of the column in the `ResultSet` from which the `LocalDate` value should be extracted.
 * @param default a supplier function that provides a default `LocalDate` value to return if the result set is empty
 *                or the column value is `null`. Defaults to `null` if not specified.
 * @return a transformer function that takes a `ResultSet` and produces a `LocalDate` value, or the default value if applicable.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun localDateFromResultSet(columnLabel: String, default: Supplier<java.time.LocalDate?> = { null }): Transformer<ResultSet, java.time.LocalDate?> = {
    if (it.next()) it.getDate(columnLabel)?.toLocalDate() else default()
}
/**
 * Extracts a LocalDate value from the specified column of a ResultSet.
 * If the value in the column is null or the ResultSet is empty, a default value is returned.
 *
 * @param column The column from which the LocalDate value will be retrieved.
 * @param default A supplier providing a default LocalDate value to return if the column value is null or the ResultSet is empty.
 * @return A transformer function that takes a ResultSet and returns a LocalDate value, or the default if no value is found.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localDateFromResultSet(column: Column<*>, default: Supplier<java.time.LocalDate?> = { null }): Transformer<ResultSet, java.time.LocalDate?> = {
    if (it.next()) it.getDate(column.name)?.toLocalDate() else default()
}
/**
 * Extracts a `java.time.LocalTime` from a `ResultSet` based on the specified column index.
 * If no value is found or if the `ResultSet` is empty, the provided default value supplier is used.
 *
 * @param columnIndex The index of the column in the `ResultSet` to be retrieved. Defaults to 1.
 * @param default A supplier providing the default value if no result is found in the `ResultSet`. Defaults to a supplier returning `null`.
 * @return A Transformer that converts a `ResultSet` into a `java.time.LocalTime?`, or the default value if no result is available.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun localTimeFromResultSet(columnIndex: Int = 1, default: Supplier<java.time.LocalTime?> = { null }): Transformer<ResultSet, java.time.LocalTime?> = {
    if (it.next()) it.getTime(columnIndex)?.toLocalTime() else default()
}
/**
 * Extracts a `LocalTime` value from the provided `ResultSet` based on the specified column label.
 * If the `ResultSet` is empty or the value is `null`, the provided default value is returned.
 *
 * @param columnLabel The label of the column from which the `LocalTime` value will be retrieved.
 * @param default A supplier function that provides the default value to return if the `ResultSet` does not have a valid `LocalTime` in the specified column.
 * @return A transformer function that processes a `ResultSet` and returns a `LocalTime` value, or the default value if applicable.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun localTimeFromResultSet(columnLabel: String, default: Supplier<java.time.LocalTime?> = { null }): Transformer<ResultSet, java.time.LocalTime?> = {
    if (it.next()) it.getTime(columnLabel)?.toLocalTime() else default()
}
/**
 * Retrieves a local time value from the specified column in the current row of the given ResultSet.
 * If the column value is null or the ResultSet is empty, the provided default will be used.
 *
 * @param column The column from which to retrieve the local time value.
 * @param default A supplier that provides a default value if the column value is null or the ResultSet is empty.
 * @return A transformer that extracts a LocalTime value from the ResultSet or uses the default value.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localTimeFromResultSet(column: Column<*>, default: Supplier<java.time.LocalTime?> = { null }): Transformer<ResultSet, java.time.LocalTime?> = {
    if (it.next()) it.getTime(column.name)?.toLocalTime() else default()
}
/**
 * Extracts a `LocalDateTime` value from the specified column index of a `ResultSet` within the current `JdbcTransaction`.
 *
 * @param columnIndex The column index from which the `LocalDateTime` value is extracted. Defaults to 1.
 * @param default A supplier function that provides a default `LocalDateTime?` value in case the result set does not contain a value
 *                or the column value is `null`. Defaults to a function returning `null`.
 * @return A transformer function that takes a `ResultSet` and returns the extracted `LocalDateTime?` value, or the default value
 *         provided by the supplier if the result is unavailable or null.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun localDateTimeFromResultSet(columnIndex: Int = 1, default: Supplier<java.time.LocalDateTime?> = { null }): Transformer<ResultSet, java.time.LocalDateTime?> = {
    if (it.next()) it.getTimestamp(columnIndex)?.toLocalDateTime() else default()
}
/**
 * Extracts a LocalDateTime value from the given ResultSet column based on the specified column label.
 * If the column value is null or the ResultSet has no more rows, a default value is returned.
 *
 * @param columnLabel The label of the column from which to extract the LocalDateTime value.
 * @param default A supplier providing a default LocalDateTime value to return if the column is null or no rows exist.
 * @return A Transformer that extracts the LocalDateTime value from the ResultSet or provides the default value.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
fun localDateTimeFromResultSet(columnLabel: String, default: Supplier<java.time.LocalDateTime?> = { null }): Transformer<ResultSet, java.time.LocalDateTime?> = {
    if (it.next()) it.getTimestamp(columnLabel)?.toLocalDateTime() else default()
}
/**
 * Extracts a `java.time.LocalDateTime` value from a `ResultSet` for the specified column.
 * If the column does not exist or the value is null, the provided default supplier is used.
 *
 * @param column The column from which the `LocalDateTime` value is to be extracted.
 * @param default A supplier providing the default `LocalDateTime` value to be used if the column value is absent or null. Defaults to `null`.
 * @return A transformer function that processes a `ResultSet` and produces a `LocalDateTime` value, or the default value if the column is null or does not exist.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localDateTimeFromResultSet(column: Column<*>, default: Supplier<java.time.LocalDateTime?> = { null }): Transformer<ResultSet, java.time.LocalDateTime?> = {
    if (it.next()) it.getTimestamp(column.name)?.toLocalDateTime() else default()
}
/**
 * Extracts an `OffsetDateTime` object from the specified column in the given `ResultSet`.
 *
 * @param columnIndex The index of the column from which the `OffsetDateTime` is retrieved. Defaults to 1.
 * @param default A supplier function that provides a default value of type `OffsetDateTime?` if the `ResultSet` is empty.
 * @return A transformer that retrieves the `OffsetDateTime` value from the `ResultSet` or returns the default value if no entry is found.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun offsetDateTimeFromResultSet(columnIndex: Int = 1, default: Supplier<OffsetDateTime?> = { null }): Transformer<ResultSet, java.time.OffsetDateTime?> = {
    if (it.next()) it.getObject(columnIndex, OffsetDateTime::class.java) else default()
}
/**
 * Extracts an `OffsetDateTime` value from the specified column of a `ResultSet`.
 * If the column does not exist or no value is found, a default value provided by the `default` supplier is returned.
 *
 * @param columnLabel The label of the column from which the `OffsetDateTime` value is to be extracted.
 * @param default A supplier that provides a default `OffsetDateTime` value if the column value is not found or is null.
 * @return A Transformer that extracts the `OffsetDateTime` value from the given `ResultSet` or returns the default value if unavailable.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun offsetDateTimeFromResultSet(columnLabel: String, default: Supplier<OffsetDateTime?> = { null }): Transformer<ResultSet, java.time.OffsetDateTime?> = {
    if (it.next()) it.getObject(columnLabel, OffsetDateTime::class.java) else default()
}
/**
 * Retrieves an OffsetDateTime value from the specified column in a ResultSet.
 * If no value is present, the provided default value is returned.
 *
 * @param column The column metadata specifying the name from which the OffsetDateTime value will be extracted.
 * @param default A supplier that provides a default OffsetDateTime value if the column is null or no data is available.
 * @return A Transformer function that processes a ResultSet to extract an OffsetDateTime value or returns the default.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun offsetDateTimeFromResultSet(column: Column<*>, default: Supplier<OffsetDateTime?> = { null }): Transformer<ResultSet, java.time.OffsetDateTime?> = {
    if (it.next()) it.getObject(column.name, OffsetDateTime::class.java) else default()
}
/**
 * Transforms a given `ResultSet` into an object of type `T` using the specified column index.
 *
 * @param columnIndex The index of the column in the `ResultSet` to retrieve the object. Defaults to `1`.
 * @param default A supplier that provides a default value of type `T?` in case the `ResultSet` has no more rows.
 * @return A `Transformer` that extracts an object of type `T?` from the `ResultSet` or provides the default value if no data is available.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
inline fun <reified T> objectFromResultSet(columnIndex: Int = 1, crossinline default: Supplier<T?> = { null }): Transformer<ResultSet, T?> = {
    if (it.next()) it.getObject(columnIndex, T::class.java) else default()
}
/**
 * Transforms a [ResultSet] to an object of the specified type [T] using the given column label.
 *
 * @param columnLabel The name of the column to extract the value from in the [ResultSet].
 * @param default A supplier for the default value to return if the [ResultSet] has no more rows.
 * @return A function that takes a [ResultSet] as input and produces an object of type [T], or the default value if no rows are available.
 * @since 5.4.0
 */
context(_: JdbcTransaction)
inline fun <reified T> objectFromResultSet(columnLabel: String, crossinline default: Supplier<T?> = { null }): Transformer<ResultSet, T?> = {
    if (it.next()) it.getObject(columnLabel, T::class.java) else default()
}
/**
 * Transforms a given database [ResultSet] into an object of the specified type [T] based on the provided column definition.
 *
 * @param column The database column to extract the value from. The name of this column is used to retrieve the corresponding object.
 * @param default A supplier function returning a default value of type [T?] if the [ResultSet] is empty or does not contain a value for the column.
 * @return A [Transformer] function that maps the [ResultSet] to an object of type [T?], based on the specified column and default value.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
inline fun <reified T> objectFromResultSet(column: Column<*>, crossinline default: Supplier<T?> = { null }): Transformer<ResultSet, T?> = {
    if (it.next()) it.getObject(column.name, T::class.java) else default()
}

/**
 * Transforms a [ResultSet] into a list of bytes extracted from a specified column.
 *
 * @param columnIndex The index of the column from which bytes are extracted. Defaults to 1 (the first column).
 * @return A transformer function that takes a [ResultSet] and produces a nullable list of bytes.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun byteListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<Byte>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getByte(columnIndex) }
        .toList()
}
/**
 * Transforms a `ResultSet` by extracting the specified column values as a list of `Byte`.
 *
 * @param columnString the name of the column in the `ResultSet` to extract byte values from
 * @return a transformer function that takes a `ResultSet` and returns a list of byte values
 *         extracted from the specified column, or `null` if no values are present
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun byteListFromResultSet(columnString: String): Transformer<ResultSet, List<Byte>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getByte(columnString) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a nullable list of bytes extracted from the specified column.
 *
 * @param column The column whose byte values will be extracted from each row of the `ResultSet`.
 * @return A `Transformer` that processes a `ResultSet` and produces a `List<Byte>?`. The resulting list contains
 *         the byte values of the specified column from each row, or `null` if the `ResultSet` is empty or inaccessible.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun byteListFromResultSet(column: Column<*>): Transformer<ResultSet, List<Byte>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getByte(column.name) }
        .toList()
}
/**
 * Transforms a given [ResultSet] into a list of integers extracted from the specified column index.
 *
 * @param columnIndex The index of the column in the result set from which integers are retrieved.
 * Defaults to 1 if not explicitly specified.
 * @return A transformer that converts the [ResultSet] into a nullable list of integers.
 * The resulting list may be null if no valid result set data exists.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun intListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<Int>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getInt(columnIndex) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a list of integers extracted from the specified column.
 *
 * @param columnString The name of the column whose integer values will be extracted from the `ResultSet`.
 * @return A `Transformer` function that takes a `ResultSet` and returns a list of integers from the specified column,
 * or `null` if the `ResultSet` is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun intListFromResultSet(columnString: String): Transformer<ResultSet, List<Int>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getInt(columnString) }
        .toList()
}
/**
 * Extracts a list of integers from the specified column in a ResultSet.
 *
 * @param column The column from which integer values will be retrieved.
 * @return A transformer function that takes a ResultSet and produces a list of integers
 *         extracted from the specified column, or null if processing is unsuccessful.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun intListFromResultSet(column: Column<*>): Transformer<ResultSet, List<Int>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getInt(column.name) }
        .toList()
}
/**
 * Transforms a ResultSet into a list of Long values extracted from a specified column index.
 *
 * @param columnIndex The 1-based index of the column from which Long values will be retrieved. Defaults to 1.
 * @return A Transformer that extracts Long values from the specified column of a ResultSet
 *         and returns them as a list, or null if the transformation is unsuccessful.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun longListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<Long>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLong(columnIndex) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a list of `Long` values extracted from the specified column.
 *
 * @param columnString The name of the column from which `Long` values will be extracted.
 * @return A transformer function that takes a `ResultSet` and produces a list of `Long` values, or `null` if no rows are present.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun longListFromResultSet(columnString: String): Transformer<ResultSet, List<Long>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLong(columnString) }
        .toList()
}
/**
 * Extracts a list of Long values from the specified column of a JDBC ResultSet.
 *
 * @param column The column from which the Long values will be extracted.
 * @return A transformer function that takes a ResultSet and returns a list of extracted Long values,
 * or null if the ResultSet is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun longListFromResultSet(column: Column<*>): Transformer<ResultSet, List<Long>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLong(column.name) }
        .toList()
}
/**
 * Transforms a given [ResultSet] into a list of Float values based on the specified column index.
 * This function reads the float values from the ResultSet starting from the specified column index
 * and generates a sequence which is then converted into a list. If the ResultSet is empty, it returns null.
 *
 * @param columnIndex The index of the column to read float values from, starting from 1 (default is 1).
 * @return A transformer function that takes a [ResultSet] and returns a list of Float values, or null if no data is available.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun floatListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<Float>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getFloat(columnIndex) }
        .toList()
}
/**
 * Transforms a ResultSet into a List of Float values extracted from a specific column.
 *
 * @param columnString The name of the column from which float values are to be retrieved.
 * @return A Transformer that processes a ResultSet and returns a List of Floats, or null if the ResultSet is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun floatListFromResultSet(columnString: String): Transformer<ResultSet, List<Float>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getFloat(columnString) }
        .toList()
}
/**
 * Transforms a ResultSet into a list of Float values by extracting the values
 * from the specified column in each row of the ResultSet.
 *
 * @param column The column from which the Float values will be extracted.
 * @return A Transformer that takes a ResultSet and produces a list of Float values,
 * or null if the ResultSet is empty or the sequence cannot be generated.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun floatListFromResultSet(column: Column<*>): Transformer<ResultSet, List<Float>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getFloat(column.name) }
        .toList()
}
/**
 * Extracts a list of Double values from a ResultSet based on the specified column index.
 *
 * The function processes the ResultSet row by row, retrieving the value of the specified column
 * as a Double and accumulating it into a list. If the column contains `NULL` values, the resulting
 * list may include default values for such entries.
 *
 * @param columnIndex The index of the column to extract values from. Defaults to 1 (the first column).
 * @return A Transformer that takes a ResultSet and produces a list of Double values, or null if processing fails.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun doubleListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<Double>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getDouble(columnIndex) }
        .toList()
}
/**
 * Transforms a ResultSet into a List of Double values extracted from the specified column.
 *
 * @param columnString The name of the column from which Double values should be extracted.
 * @return A Transformer that takes a ResultSet as input and produces a nullable List of Double values.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun doubleListFromResultSet(columnString: String): Transformer<ResultSet, List<Double>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getDouble(columnString) }
        .toList()
}
/**
 * Transforms a JDBC ResultSet into a list of Double values extracted from the specified column.
 *
 * @param column The column from which the Double values will be extracted.
 * @return A Transformer function that consumes a ResultSet and produces a list of Double values,
 *         or null if no rows are available.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun doubleListFromResultSet(column: Column<*>): Transformer<ResultSet, List<Double>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getDouble(column.name) }
        .toList()
}
/**
 * Retrieves a list of Boolean values from a ResultSet based on the specified column index.
 *
 * @param columnIndex The index of the column to extract Boolean values from. Defaults to 1 if not provided.
 * @return A Transformer function that processes a ResultSet and returns a list of Boolean values or null if the ResultSet is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun booleanListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<Boolean>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBoolean(columnIndex) }
        .toList()
}
/**
 * Transforms a JDBC ResultSet into a list of boolean values extracted from the specified column.
 *
 * @param columnString the name of the column to extract boolean values from.
 * @return a Transformer function that converts a ResultSet into a list of Boolean values, or null if the ResultSet is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun booleanListFromResultSet(columnString: String): Transformer<ResultSet, List<Boolean>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBoolean(columnString) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a list of Booleans by extracting the values
 * from the specified column throughout the sequence of rows available.
 *
 * @param column The column from which the boolean values will be extracted.
 * @return A transformer function that, given a `ResultSet`, produces
 * a list of Boolean values from the specified column, or null if no rows are present.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun booleanListFromResultSet(column: Column<*>): Transformer<ResultSet, List<Boolean>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBoolean(column.name) }
        .toList()
}
/**
 * Generates a list of strings from the specified column in the given ResultSet.
 *
 * @param columnIndex The index of the column to extract strings from. Defaults to 1 (first column).
 * @return A Transformer that transforms a ResultSet into a list of nullable strings. Returns null if the operation fails.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun stringListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<String?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getString(columnIndex) }
        .toList()
}
/**
 * Transforms a JDBC ResultSet into a list of nullable strings based on the specified column name.
 *
 * @param columnString The name of the column from which to extract string values.
 * @return A Transformer function that processes a ResultSet and converts it into a List of nullable strings extracted from the specified column.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun stringListFromResultSet(columnString: String): Transformer<ResultSet, List<String?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getString(columnString) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a list of nullable `String` values extracted
 * from a specified column.
 *
 * @param column The database column from which `String` values will be extracted.
 * @return A transformer function that takes a `ResultSet` and returns a list of*/
context(_: JdbcTransaction)
fun stringListFromResultSet(column: Column<*>): Transformer<ResultSet, List<String?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getString(column.name) }
        .toList()
}
/**
 * Transforms a ResultSet into a non-null list of strings extracted from the specified column index.
 *
 * @param columnIndex The index of the column from which to extract string values. Defaults to 1.
 * @return A transformer function that converts a ResultSet into a list of non-null strings. Returns null if no data is present in the ResultSet.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun stringNNListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<String>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getString(columnIndex)!! }
        .toList()
}
/**
 * Transforms a JDBC ResultSet into a List of non-null Strings for the given column.
 *
 * @param columnString The name of the column from which to retrieve values.
 * @return A transformer function that processes the ResultSet and produces a List of non-null Strings,
 * or null if the ResultSet is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun stringNNListFromResultSet(columnString: String): Transformer<ResultSet, List<String>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getString(columnString)!! }
        .toList()
}
/**
 * Retrieves a non-nullable list of strings from a specific column in a JDBC ResultSet
 * within the context of a database transaction.
 *
 * @param column The database column from which the strings will be extracted.
 * @return A transformer function that takes a ResultSet and produces a list of non-nullable strings,
 *         or null if no data is available.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun stringNNListFromResultSet(column: Column<*>): Transformer<ResultSet, List<String>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getString(column.name)!! }
        .toList()
}
/**
 * Transforms a [ResultSet] into a list of arrays retrieved from the specified column index.
 * The function generates a sequence of rows from the result set and maps each row
 * to retrieve an SQL array from the given column index.
 *
 * @param columnIndex The index of the column from which SQL arrays will be retrieved.
 *                    Defaults to 1 (the first column).
 * @return A transformer function that takes a [ResultSet] and produces a list of
 *         [java.sql.Array] objects or null if no rows exist in the result set.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun arrayListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<java.sql.Array>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getArray(columnIndex) }
        .toList()
}
/**
 * Transforms a ResultSet into a list of SQL Array objects extracted from the specified column.
 *
 * @param columnString The name of the column from which the SQL Array objects will be retrieved.
 * @return A transformer function that produces a list of SQL Array objects from the ResultSet, or null if no data is found.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun arrayListFromResultSet(columnString: String): Transformer<ResultSet, List<java.sql.Array>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getArray(columnString) }
        .toList()
}
/**
 * Transforms a JDBC ResultSet into a List of SQL Array objects for a specified column.
 *
 * @param column The column whose values are to be extracted as SQL Array objects from the ResultSet.
 * @return A Transformer that takes a ResultSet and returns a List of SQL Array objects or null if no data is available.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun arrayListFromResultSet(column: Column<*>): Transformer<ResultSet, List<java.sql.Array>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getArray(column.name) }
        .toList()
}
/**
 * Transforms a given JDBC ResultSet into a list of byte arrays by extracting values from the specified column.
 *
 * @param columnIndex The column index to retrieve the byte array values from. Defaults to 1.
 * @return A transformer that takes a ResultSet and produces a list of byte arrays, where each element corresponds
 *         to a row in the ResultSet. If the column has a null value, it will be represented as null in the list.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun byteArrayListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<ByteArray?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBytes(columnIndex) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a list of nullable `ByteArray` values by extracting
 * the specified column's data as `ByteArray` for each row.
 *
 * @param columnString The name of the column to extract data from in the `ResultSet`.
 * @return A transformer function that, given a `ResultSet`, produces a list of nullable `ByteArray` values
 *         corresponding to the entries in the specified column.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun byteArrayListFromResultSet(columnString: String): Transformer<ResultSet, List<ByteArray?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBytes(columnString) }
        .toList()
}
/**
 * Extracts a list of byte arrays from a given SQL ResultSet based on the specified column.
 *
 * @param column The column from which the byte array values are extracted.
 * @return A transformer function that, when provided a ResultSet, returns a list of ByteArray objects
 *         or null values extracted from the specified column. If the ResultSet is empty, it returns an empty list.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun byteArrayListFromResultSet(column: Column<*>): Transformer<ResultSet, List<ByteArray?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBytes(column.name) }
        .toList()
}
/**
 * Transforms a given [ResultSet] into a list of non-null byte arrays extracted from the specified column index.
 * The transformation is applied within a [JdbcTransaction] context.
 *
 * @param columnIndex The index of the column from which the byte arrays will be extracted. Defaults to 1.
 * @return A transformer function that takes a [ResultSet] and returns a list of non-null byte arrays.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun byteArrayNNListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<ByteArray>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBytes(columnIndex)!! }
        .toList()
}
/**
 * Transforms a JDBC ResultSet into a list of non-null byte arrays based on the specified column name.
 * The method ensures all byte arrays retrieved from the specified column are non-null.
 *
 * @param columnString The name of the column in the ResultSet from which byte array values are extracted.
 * @return A Transformer that processes the provided ResultSet and returns a list of non-null byte arrays, or null if the result cannot be transformed.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun byteArrayNNListFromResultSet(columnString: String): Transformer<ResultSet, List<ByteArray>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBytes(columnString)!! }
        .toList()
}
/**
 * Transforms a `ResultSet` into a list of non-null byte arrays for a given column.
 *
 * @param column The database column from which byte arrays will be extracted.
 *               This column must be specified as part of the transformation logic.
 * @return A `Transformer` function that, when applied to a `ResultSet`, produces a list of non-null byte arrays extracted from the specified column.
 *         Returns `null` if the `ResultSet` contains no data.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun byteArrayNNListFromResultSet(column: Column<*>): Transformer<ResultSet, List<ByteArray>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBytes(column.name)!! }
        .toList()
}
/**
 * Transforms a `ResultSet` into a list of nullable `BigDecimal` values
 * by extracting the value from the specified column index for each row.
 *
 * @param columnIndex the index of the column containing the `BigDecimal` values. Defaults to 1.
 * @return a transformer function that takes a `ResultSet` and returns a list of nullable `BigDecimal` values.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun bigDecimalListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<BigDecimal?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBigDecimal(columnIndex) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a nullable list of `BigDecimal` values extracted from the specified column.
 *
 * @param columnString The name of the column from which `BigDecimal` values will be retrieved.
 * @return A transformer function that takes a `ResultSet` and produces a nullable list of `BigDecimal` values.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun bigDecimalListFromResultSet(columnString: String): Transformer<ResultSet, List<BigDecimal?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBigDecimal(columnString) }
        .toList()
}
/**
 * Creates a transformer for extracting a list of BigDecimal values from a ResultSet
 * based on the specified column.
 *
 * @param column The column descriptor that provides the name of the column whose
 *               values will be extracted as BigDecimal.
 * @return A transformer function that takes a ResultSet and produces a List of
 *         nullable BigDecimal values. The list will contain one entry per row
 *         in the ResultSet or be null if the input ResultSet is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun bigDecimalListFromResultSet(column: Column<*>): Transformer<ResultSet, List<BigDecimal?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBigDecimal(column.name) }
        .toList()
}
/**
 * Extracts a non-null list of `BigDecimal` values from a `ResultSet`.
 *
 * @param columnIndex The index of the `ResultSet` column to extract `BigDecimal` values from. Defaults to 1.
 * @return A `Transformer` function that converts a `ResultSet` into a list of `BigDecimal` values, or null if transformation is not possible.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun bigDecimalNNListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<BigDecimal>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBigDecimal(columnIndex)!! }
        .toList()
}
/**
 * Transforms a given `ResultSet` into a list of non-null `BigDecimal` values extracted from a specified column.
 *
 * @param columnString the name of the column in the `ResultSet` from which `BigDecimal` values will be extracted.
 * @return a transformer function that converts a `ResultSet` into a list of non-null `BigDecimal` values, or null if no values are present.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun bigDecimalNNListFromResultSet(columnString: String): Transformer<ResultSet, List<BigDecimal>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBigDecimal(columnString)!! }
        .toList()
}
/**
 * Transforms a `ResultSet` into a non-nullable list of `BigDecimal` values from a specified column.
 *
 * @param column The column metadata used to fetch `BigDecimal` values from the `ResultSet`.
 * @return A transformer function that processes a `ResultSet` and produces a list of non-nullable `BigDecimal` values
 *         from the specified column, or null if the transformation cannot proceed.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun bigDecimalNNListFromResultSet(column: Column<*>): Transformer<ResultSet, List<BigDecimal>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getBigDecimal(column.name)!! }
        .toList()
}
/**
 * Constructs a transformer function that extracts a list of nullable LocalDate values
 * from the specified column of a ResultSet.
 *
 * @param columnIndex the index of the column starting from 1, from which the LocalDate values are to be retrieved. Defaults to 1.
 * @return a transformer function that takes a ResultSet and returns a list of nullable LocalDate values
 *         corresponding to the specified column.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localDateListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<LocalDate?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalDate(columnIndex) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a `List` of nullable `LocalDate` objects extracted from the specified column.
 *
 * @param columnString the name of the column from which `LocalDate` values will be extracted
 * @return a transformer function that maps a `ResultSet` to a list of nullable `LocalDate` values
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localDateListFromResultSet(columnString: String): Transformer<ResultSet, List<LocalDate?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalDate(columnString) }
        .toList()
}
/**
 * Generates a transformer function that extracts a list of `LocalDate` values from a `ResultSet`,
 * based on the specified database column.
 *
 * @param column the column from which `LocalDate` values are retrieved
 * @return a transformer function that processes a `ResultSet` into a list of `LocalDate` values, where nulls are allowed
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localDateListFromResultSet(column: Column<*>): Transformer<ResultSet, List<LocalDate?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalDate(column.name) }
        .toList()
}
/**
 * Transforms a JDBC ResultSet into a non-nullable list of LocalDate values, by reading each row
 * from the given starting column index until no more rows exist. Assumes that the column contains
 * non-null LocalDate values and will throw an exception if null is encountered.
 *
 * @param columnIndex the one-based index of the column to extract the LocalDate values from. Defaults to 1.
 * @return a Transformer that takes a ResultSet as input and returns a List of LocalDate values,
 *         or null if the ResultSet is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localDateNNListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<LocalDate>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalDate(columnIndex)!! }
        .toList()
}
/**
 * Retrieves a list of non-null LocalDate values from the specified column of a ResultSet.
 *
 * @param columnString the name of the column in the ResultSet from which LocalDate values are to be retrieved.
 *                     Must ensure the column contains valid SQL dates.
 * @return a Transformer function that processes the ResultSet and returns a List of non-null LocalDate values,
 *         or null if the ResultSet is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localDateNNListFromResultSet(columnString: String): Transformer<ResultSet, List<LocalDate>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalDate(columnString)!! }
        .toList()
}
/**
 * Transforms a given `ResultSet` into a non-nullable `List` of `LocalDate` values based on the specified column.
 *
 * @param column The database column from which `LocalDate` values are extracted.
 *               The column's `name` is used to retrieve data from the `ResultSet`.
 * @return A transformer that converts a `ResultSet` into a list of non-null `LocalDate` values extracted
 *         from the specified column. Returns null if no data is available.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localDateNNListFromResultSet(column: Column<*>): Transformer<ResultSet, List<LocalDate>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalDate(column.name)!! }
        .toList()
}
/**
 * Extracts a list of `LocalTime` objects (nullable) from a `ResultSet` starting at the specified column index.
 *
 * @param columnIndex The index of the column in the `ResultSet` to retrieve `LocalTime` values from. Defaults to 1.
 * @return A transformer function that takes a `ResultSet` and returns a list of `LocalTime?`, or null if the `ResultSet` is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localTimeListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<LocalTime?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalTime(columnIndex) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a list of nullable `LocalTime` values by extracting data from the specified column.
 *
 * @param columnString the name of the column from which `LocalTime` values are retrieved.
 * @return a `Transformer` function that takes a `ResultSet` and produces a nullable list of `LocalTime` values.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localTimeListFromResultSet(columnString: String): Transformer<ResultSet, List<LocalTime?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalTime(columnString) }
        .toList()
}
/**
 * Transforms a ResultSet into a list of LocalTime values. Handles nullable entries in the result set.
 *
 * @param column The database column to extract LocalTime values from.
 * @return A transformer function that converts a ResultSet into a list of LocalTime values, allowing nulls.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localTimeListFromResultSet(column: Column<*>): Transformer<ResultSet, List<LocalTime?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalTime(column.name) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a `List` of non-null `LocalTime` objects extracted from a
 * specified column index. The method iterates over all rows in the `ResultSet`, retrieves
 * `LocalTime` values from the specified column, and collects them as a list, excluding nulls.
 *
 * @param columnIndex the 1-based index of the column from which to extract `LocalTime` values. Defaults to 1.
 * @return a transformer function that takes a `ResultSet` and produces a list of `LocalTime` objects, or null if no data is available.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localTimeNNListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<LocalTime>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalTime(columnIndex)!! }
        .toList()
}
/**
 * Transforms a JDBC ResultSet into a list of non-nullable LocalTime objects
 * extracted from the specified column.
 *
 * @param columnString The name of the column containing LocalTime values.
 * @return A Transformer that converts a ResultSet into a list of LocalTime instances
 * extracted from the specified column. Returns null if the transformation is not successful.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localTimeNNListFromResultSet(columnString: String): Transformer<ResultSet, List<LocalTime>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalTime(columnString)!! }
        .toList()
}
/**
 * Generates a transformer to extract a non-nullable list of LocalTime objects from a ResultSet.
 *
 * @param column The column definition used to identify the target column in the ResultSet.
 * @return A transformer function that, when applied to a ResultSet, produces a list of LocalTime objects from
 *         the specified column. The list is non-nullable, and null values in the ResultSet are not allowed.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localTimeNNListFromResultSet(column: Column<*>): Transformer<ResultSet, List<LocalTime>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalTime(column.name)!! }
        .toList()
}

/**
 * Transforms a JDBC ResultSet into a list of non-null LocalDateTime values extracted from the specified column index.
 *
 * @param columnIndex The index of the column in the ResultSet from which LocalDateTime values are extracted. Defaults to 1.
 * @return A transformer function that converts the ResultSet into a list of non-null LocalDateTime values.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localDateTimeNNListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<LocalDateTime>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalDateTime(columnIndex)!! }
        .toList()
}
/**
 * Transforms a `ResultSet` into a non-nullable list of `LocalDateTime` objects, using the specified column name to extract the date-time values.
 *
 * @param columnString The name of the column from which `LocalDateTime` values will be extracted. This column must not contain null values.
 * @return A transformer function that takes a `ResultSet` and produces a list of `LocalDateTime` objects containing the non-null values extracted from the specified column, or `
 * null` if the `ResultSet` is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localDateTimeNNListFromResultSet(columnString: String): Transformer<ResultSet, List<LocalDateTime>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalDateTime(columnString)!! }
        .toList()
}
/**
 * Extracts a non-nullable list of LocalDateTime values from the result set based on the specified column.
 *
 * @param column The column from which LocalDateTime values will be retrieved.
 * @return A transformer function that processes a ResultSet and returns a list of LocalDateTime values, or null if no rows are found.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun localDateTimeNNListFromResultSet(column: Column<*>): Transformer<ResultSet, List<LocalDateTime>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getLocalDateTime(column.name)!! }
        .toList()
}
/**
 * Extracts a list of `OffsetDateTime` values from a `ResultSet`, starting at the specified column index.
 * The transformer processes each row of the result set and maps the specified column value to an `OffsetDateTime` object.
 *
 * @param columnIndex The column index to extract the `OffsetDateTime` value from, defaulting to 1 (the first column).
 * @return A transformer that converts a `ResultSet` into a `List` of nullable `OffsetDateTime` objects.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun offsetDateTimeListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<OffsetDateTime?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getOffsetDateTime(columnIndex) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a list of `OffsetDateTime` values by extracting
 * the values from the specified column.
 *
 * @param columnString The name of the column from which `OffsetDateTime` values
 * are to be extracted.
 * @return A transformer function that takes a `ResultSet` as input and returns
 * a nullable list of extracted `OffsetDateTime` values, or null if no values are found.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun offsetDateTimeListFromResultSet(columnString: String): Transformer<ResultSet, List<OffsetDateTime?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getOffsetDateTime(columnString) }
        .toList()
}
/**
 * Transforms a JDBC ResultSet into a list of nullable OffsetDateTime values extracted
 * from the specified column. The transformation is performed within the context of
 * a `JdbcTransaction`.
 *
 * @param column The column from which the OffsetDateTime values will be extracted.
 * @return A transformer function that takes a ResultSet and produces a list of
 * nullable OffsetDateTime values, or null if no data is present.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun offsetDateTimeListFromResultSet(column: Column<*>): Transformer<ResultSet, List<OffsetDateTime?>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getOffsetDateTime(column.name) }
        .toList()
}
/**
 * Transforms a JDBC ResultSet into a non-nullable list of OffsetDateTime values extracted from the specified column.
 * The transformation generates a sequence by iterating over the ResultSet and converts the specified column at each row
 * into an OffsetDateTime. Null values are not allowed in the specified column.
 *
 * @param columnIndex The index of the column in the ResultSet from which the OffsetDateTime values are extracted. Defaults to 1.
 * @return A Transformer function that converts a ResultSet into a List of non-null OffsetDateTime values.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun offsetDateTimeNNListFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<OffsetDateTime>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getOffsetDateTime(columnIndex)!! }
        .toList()
}
/**
 * Transforms a SQL `ResultSet` into a list of non-null `OffsetDateTime` objects
 * by extracting the values from a specified column.
 *
 * This function generates a sequence from the `ResultSet` and maps each valid row
 * to a non-null `OffsetDateTime` instance from the provided column name.
 *
 * @param columnString The name of the column containing `OffsetDateTime` values; the column must not contain null values.
 * @return A transformer function that takes a `ResultSet` and produces a list of non-null `OffsetDateTime` objects, or an empty list if no rows are present in the `ResultSet`.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun offsetDateTimeNNListFromResultSet(columnString: String): Transformer<ResultSet, List<OffsetDateTime>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getOffsetDateTime(columnString)!! }
        .toList()
}
/**
 * Extracts a non-nullable list of OffsetDateTime values from the specified ResultSet column.
 * The ResultSet is traversed and values from the given column are collected into a list.
 * A null return value indicates that the ResultSet does not contain any rows.
 *
 * @param column The column from which OffsetDateTime values should be extracted.
 * @return A transformer function that takes a ResultSet and produces a list of OffsetDateTime values, or null if no rows are present.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
fun offsetDateTimeNNListFromResultSet(column: Column<*>): Transformer<ResultSet, List<OffsetDateTime>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getOffsetDateTime(column.name)!! }
        .toList()
}
/**
 * Transforms the given ResultSet into a List of elements of type T by mapping the values
 * retrieved from a specified column index.
 *
 * @param columnIndex The index of the column in the ResultSet to retrieve values from. Defaults to 1.
 * @return A Transformer that converts the ResultSet into a list of type T or null if the ResultSet is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
inline fun <reified T> listFromResultSet(columnIndex: Int = 1): Transformer<ResultSet, List<T>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getObject(columnIndex, T::class.java) }
        .toList()
}
/**
 * Transforms a given JDBC ResultSet into a Kotlin list of a specified type, extracting data
 * from the specified column.
 *
 * @param T The type of the objects to be extracted from the ResultSet.
 * @param columnString The name of the database column to extract values from.
 * @return A Transformer that takes a ResultSet and converts it into a List of objects of type T, or null if the ResultSet is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
inline fun <reified T> listFromResultSet(columnString: String): Transformer<ResultSet, List<T>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getObject(columnString, T::class.java) }
        .toList()
}
/**
 * Transforms a `ResultSet` into a list of objects of a specified type.
 * This method utilizes the provided column metadata to extract data from the ResultSet.
 *
 * @param T The type of the objects to be extracted from the ResultSet.
 * @param column The column metadata that defines which column of the ResultSet
 * should be mapped to the desired type.
 * @return A transformer function that converts a ResultSet into a List of type T
 * or null if the ResultSet is empty.
 * @since 5.4.1
 */
context(_: JdbcTransaction)
inline fun <reified T> listFromResultSet(column: Column<*>): Transformer<ResultSet, List<T>?> = { rs ->
    generateSequence { if (rs.next()) rs else null }
        .map { it.getObject(column.name, T::class.java) }
        .toList()
}