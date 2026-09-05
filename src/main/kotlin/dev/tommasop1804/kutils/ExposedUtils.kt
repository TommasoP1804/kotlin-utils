/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("ExposedUtilsKt")
@file:Suppress("unused")
@file:Since("5.3.0")
@file:MustUseReturnValues

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.classes.constants.*
import dev.tommasop1804.kutils.classes.identifiers.*
import dev.tommasop1804.kutils.exceptions.*
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.core.statements.InsertStatement
import org.jetbrains.exposed.v1.core.statements.StatementType
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import org.jetbrains.exposed.v1.dao.InnerTableLink
import org.jetbrains.exposed.v1.dao.Referrers
import org.jetbrains.exposed.v1.jdbc.*
import java.sql.ResultSet

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
fun <T> JdbcTransaction.exec(
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
 * Checks whether an entity with the given ID exists in the associated entity class.
 *
 * @param id The ID of the entity to check for existence.
 * @return `true` if an entity with the specified ID exists, `false` otherwise.
 * @since 5.3.0
 */
operator fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.contains(id: ID) = exists(id)
/**
 * Checks if an entity with the specified ID exists.
 *
 * @param id The ID of the entity to check for existence.
 * @return `true` if an entity with the given ID exists, `false` otherwise.
 * @since 5.3.0
 */
fun <ID : Any, T : Entity<ID>> EntityClass<ID, T>.exists(id: ID): Boolean {
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
inline fun <ID : Any, reified T : Entity<ID>> EntityClass<ID, T>.existsByIdOrThrow(id: ID, internalErrorCode: String?, lazyMesage: Supplier<Any>): Boolean {
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
inline fun <ID : Any, reified T : Entity<ID>> EntityClass<ID, T>.findByIdOrThrow(id: ID, internalErrorCode: String?, lazyMesage: Supplier<Any>) =
    findById(id) ?: throw ResourceNotFoundException(lazyMesage().toString(), internalErrorCode)
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
inline fun <ID : Any, reified T : Entity<ID>> EntityClass<ID, T>.findByIdOr(id: ID, default: Supplier<T>) =
    findById(id) ?: default()

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
 * Provides a shorthand operator for deleting rows from the table where the specified condition is met.
 *
 * @param op A lambda function that defines the condition for deleting rows. It takes the receiver table and returns a boolean operation.
 * @since 5.3.0
 */
operator fun <T : Table> T.minusAssign(op: ReceiverTransformer<T, Op<Boolean>>) { deleteWhere(op = op) }