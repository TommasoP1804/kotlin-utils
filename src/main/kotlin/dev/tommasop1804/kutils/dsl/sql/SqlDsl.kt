/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused", "kutils_collection_declaration", "SqlNoDatasourceInspection", "contains_as_in_operator",
    "kutils_ignore_case_function"
)

package dev.tommasop1804.kutils.dsl.sql

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.classes.constants.*
import dev.tommasop1804.kutils.exceptions.*
import org.intellij.lang.annotations.Language
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

// --- ENUMS ---

/**
 * Represents the types of SQL join operations.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@Suppress("SqlNoDataSourceInspection")
enum class JoinType(@param:Language("sql") val sqlKeyword: String, val onCondition: Boolean = true) {
    Inner("INNER"),
    LeftOuter("LEFT"),
    RightOuter("RIGHT"),
    FullOuter("FULL"),
    Cross("CROSS", false),
    Self("SELF"),
    Natural("NATURAL", false)
}

/**
 * Represents logical operators used in WHERE clause composition.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
enum class LogicOperator { And, Or }

/**
 * Represents drop behavior for DDL operations.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
enum class DropType { Cascade, Restrict }

/**
 * Represents trigger timing events.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
enum class TriggerEvent {
    BeforeInsert, AfterInsert,
    BeforeUpdate, AfterUpdate,
    BeforeDelete, AfterDelete;

    val sql: String get() = name.replace('_', ' ')
}

// --- @DSLMARKER ---

/**
 * DSL marker to prevent scope leaking between nested SQL DSL blocks.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@DslMarker
annotation class SqlDslMarker

// --- SUB-SCOPE BUILDERS ---

/**
 * Scope for building WHERE clause conditions.
 *
 * Supports `and`, `or`, `not`, and nested groups.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@SqlDslMarker
class WhereScope @PublishedApi internal constructor(val autoApplyOperator: LogicOperator?) {

    @PublishedApi
    internal val parts = emptyMList<String>()

    /**
     * Adds a raw condition.
     *
     * @since 3.6.0
     */
    fun condition(expr: String, autoApplyOperator: LogicOperator? = this.autoApplyOperator) {
        parts += if (parts.isEmpty()) expr else when (autoApplyOperator) {
            LogicOperator.And -> "AND $expr"
            LogicOperator.Or -> "OR $expr"
            null -> expr
        }
    }

    /**
     * Adds an AND condition.
     *
     * @since 3.6.0
     */
    fun and(expr: String) {
        parts += "AND $expr"
    }

    /**
     * Adds an OR condition.
     *
     * @since 3.6.0
     */
    fun or(expr: String) {
        parts += "OR $expr"
    }

    /**
     * Adds a NOT condition with an optional preceding logic operator.
     *
     * @since 3.6.0
     */
    fun not(expr: String, logicOperator: LogicOperator? = null) {
        val prefix = logicOperator?.let { "${it.name} " } ?: String.EMPTY
        parts += "${prefix}NOT ($expr)"
    }

    /**
     * Adds a nested group of conditions wrapped in parentheses with a preceding AND.
     *
     * ```kotlin
     * where {
     *     condition("status = 'ACTIVE'")
     *     andGroup {
     *         condition("age > 18")
     *         or("vip = true")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    inline fun andGroup(autoApplyOperator: LogicOperator? = this.autoApplyOperator, block: WhereScope.() -> Unit) {
        val inner = WhereScope(autoApplyOperator).apply(block)
        parts += "AND (${inner.build()})"
    }

    /**
     * Adds a nested group of conditions wrapped in parentheses with a preceding OR.
     *
     * @since 3.6.0
     */
    inline fun orGroup(autoApplyOperator: LogicOperator? = this.autoApplyOperator, block: WhereScope.() -> Unit) {
        val inner = WhereScope(autoApplyOperator).apply(block)
        parts += "OR (${inner.build()})"
    }

    /**
     * Adds an `IN` condition.
     *
     * ```kotlin
     * where { "status" `in` listOf("'ACTIVE'", "'PENDING'") }
     * ```
     *
     * @since 3.6.0
     */
    infix fun String.`in`(values: Collection<String>) {
        condition("$this IN (${values.joinToString(", ")})")
    }

    /**
     * Adds a `NOT IN` condition.
     *
     * @since 3.6.0
     */
    infix fun String.notIn(values: Collection<String>) {
        condition("$this NOT IN (${values.joinToString(", ")})")
    }

    /**
     * Adds a `BETWEEN` condition.
     *
     * ```kotlin
     * where { "age" between ("18" to "65") }
     * ```
     *
     * @since 3.6.0
     */
    infix fun String.between(range: String2) {
        condition("$this BETWEEN ${range.first} AND ${range.second}")
    }

    /**
     * Adds a `LIKE` condition.
     *
     * @since 3.6.0
     */
    infix fun String.like(pattern: String) {
        condition("$this LIKE '$pattern'")
    }

    /**
     * Adds an `ILIKE` condition (case-insensitive LIKE, PostgreSQL).
     *
     * @since 3.6.0
     */
    infix fun String.ilike(pattern: String) {
        condition("$this ILIKE '$pattern'")
    }

    /**
     * Adds an `IS NULL` condition.
     *
     * @since 3.6.0
     */
    fun isNullValue(column: String) {
        condition("$column IS NULL")
    }

    /**
     * Adds an `IS NOT NULL` condition.
     *
     * @since 3.6.0
     */
    fun isNotNullValue(column: String) {
        condition("$column IS NOT NULL")
    }

    /**
     * Adds an `EXISTS` subquery condition.
     *
     * @since 3.6.0
     */
    fun exists(@Language("sql") subquery: String) {
        condition("EXISTS ($subquery)")
    }
    /**
     * Adds an `EXISTS` subquery condition.
     *
     * @since 4.1.1
     */
    fun exists(subquery: SqlQuery) {
        condition("EXISTS (${subquery.value})")
    }

    /**
     * Adds a `NOT EXISTS` subquery condition.
     *
     * @since 3.6.0
     */
    fun notExists(@Language("sql") subquery: String) {
        condition("NOT EXISTS ($subquery)")
    }
    /**
     * Adds a `NOT EXISTS` subquery condition.
     *
     * @since 4.1.1
     */
    fun notExists(subquery: SqlQuery) {
        condition("NOT EXISTS (${subquery.value})")
    }

    @PublishedApi
    internal fun build(): String = parts.joinToString(" ")
}

/**
 * Scope for building JOIN clauses.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@SqlDslMarker
class JoinScope @PublishedApi internal constructor() {

    @PublishedApi
    internal val joins = emptyMList<String>()

    /**
     * Adds a join clause using a [JoinType] enum.
     *
     * @since 3.6.0
     */
    fun join(table: String, type: JoinType, on: String? = null) {
        if (type.onCondition && on.isNullOrBlank())
            throw IllegalOperationException("Cannot join table $table with type $type without onCondition. Please provide onCondition to join table $table with type $type")
        if (!type.onCondition && on.isNotNullOrBlank())
            throw IllegalOperationException("Cannot join table $table with type $type with onCondition. Please provide onCondition to join table $table with type $type")

        joins += " ${+type.sqlKeyword} JOIN $table${if (on.isNotNullOrBlank()) " ON $on" else String.EMPTY}"
    }

    /**
     * Adds an INNER JOIN.
     *
     * @since 3.6.0
     */
    fun inner(table: String, on: String) =
        join(table, JoinType.Inner, on.validate("inner", "on", predicate = String::isNotBlank))

    /**
     * Adds a LEFT JOIN.
     *
     * @since 3.6.0
     */
    fun left(table: String, on: String) =
        join(table, JoinType.LeftOuter, on.validate("left", "on", predicate = String::isNotBlank))

    /**
     * Adds a RIGHT JOIN.
     *
     * @since 3.6.0
     */
    fun right(table: String, on: String) =
        join(table, JoinType.RightOuter, on.validate("right", "on", predicate = String::isNotBlank))

    /**
     * Adds a FULL OUTER JOIN.
     *
     * @since 3.6.0
     */
    fun full(table: String, on: String) =
        join(table, JoinType.FullOuter, on.validate("full", "on", predicate = String::isNotBlank))

    /**
     * Adds a CROSS JOIN.
     *
     * @since 3.6.0
     */
    fun cross(table: String) {
        joins += " CROSS JOIN $table"
    }

    /**
     * Adds a NATURAL JOIN.
     *
     * @since 3.6.0
     */
    fun natural(table: String) {
        joins += " NATURAL JOIN $table"
    }

    /**
     * Adds a lateral join clause to the current SQL construct.
     *
     * A lateral join allows a subquery to refer to columns from the outer query's scope.
     * If the `type` of join requires an ON condition, the `on` parameter must be provided;
     * otherwise, the `on` parameter must be null or blank.
     *
     * WARNING: If `alias` parameter is provided, automatically the method puts parentheses around the query with the alias after `)`.
     * Else, the query is added directly without parentheses.
     *
     * @param query The SQL query to be joined laterally.
     * @param type The type of the lateral join, determined by the `JoinType` enum.
     * @param alias An optional alias for the lateral query. If provided, the query will be
     *              wrapped in parentheses and the alias will be added after the closing parenthesis.
     * @param on An optional SQL condition for the join. It must be specified if the `type` of
     *           the join requires a condition and omitted otherwise.
     * @throws IllegalOperationException If an invalid combination of `type` and `on` is provided.
     * @since 3.9.3
     */
    fun lateral(query: String, type: JoinType, alias: String? = null, on: String? = null) {
        if (type.onCondition && on.isNullOrBlank())
            throw IllegalOperationException("Cannot join with type $type without onCondition. Please provide onCondition to join with type $type")
        if (!type.onCondition && on.isNotNullOrBlank())
            throw IllegalOperationException("Cannot join with type $type with onCondition. Please provide onCondition to join with type $type")
        joins += " ${+type.sqlKeyword} JOIN LATERAL ${if (alias.isNotNullOrBlank()) "($query) $alias" else query}${if (on.isNotNullOrBlank()) " ON $on" else String.EMPTY}"
    }

    /**
     * Performs a lateral join operation with the given SQL query, join type, alias, and optional ON condition.
     *
     * WARNING: If `alias` parameter is provided, automatically the method puts parentheses around the query with the alias after `)`.
     * Else, the query is added directly without parentheses.
     *
     * @param query The SQL query to be used in the lateral join.
     * @param type The type of join to perform. This determines whether an ON condition is required.
     * @param alias An optional alias to use for the joined query. Defaults to null.
     * @param on An optional ON condition for the join. Must be provided if the join type requires it, otherwise must be null.
     * @throws IllegalOperationException Thrown if the provided ON condition does not satisfy the requirements of the specified join type.
     * @since 3.9.3
     */
    fun lateral(query: SqlQuery, type: JoinType, alias: String? = null, on: String? = null) {
        if (type.onCondition && on.isNullOrBlank())
            throw IllegalOperationException("Cannot join with type $type without onCondition. Please provide onCondition to join with type $type")
        if (!type.onCondition && on.isNotNullOrBlank())
            throw IllegalOperationException("Cannot join with type $type with onCondition. Please provide onCondition to join with type $type")
        joins += " ${+type.sqlKeyword} JOIN LATERAL ${if (alias.isNotNullOrBlank()) "(${query.value}) $alias" else query.value}${if (on.isNotNullOrBlank()) " ON $on" else String.EMPTY}"
    }
}

/**
 * Scope for building ORDER BY clauses.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@SqlDslMarker
class OrderByScope @PublishedApi internal constructor() {

    @PublishedApi
    internal val columns = emptyMList<String>()

    /**
     * Adds a column with a sorting direction.
     *
     * @since 3.6.0
     */
    fun column(name: String, direction: SortDirection = SortDirection.Ascending) {
        columns += name + if (direction == SortDirection.Descending) " DESC" else String.EMPTY
    }

    /**
     * Shorthand: ascending column.
     *
     * @since 3.6.0
     */
    fun asc(name: String) = column(name, SortDirection.Ascending)

    /**
     * Shorthand: descending column.
     *
     * @since 3.6.0
     */
    fun desc(name: String) = column(name, SortDirection.Descending)

    /**
     * Adds a column with NULLS FIRST directive.
     *
     * @since 3.6.0
     */
    fun nullsFirst(name: String, direction: SortDirection = SortDirection.Ascending) {
        columns += name + (if (direction == SortDirection.Descending) " DESC" else String.EMPTY) + " NULLS FIRST"
    }

    /**
     * Adds a column with NULLS LAST directive.
     *
     * @since 3.6.0
     */
    fun nullsLast(name: String, direction: SortDirection = SortDirection.Ascending) {
        columns += name + (if (direction == SortDirection.Descending) " DESC" else String.EMPTY) + " NULLS LAST"
    }
}

/**
 * Scope for building trigger definitions.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@SqlDslMarker
class TriggerScope @PublishedApi internal constructor() {
    internal var event: TriggerEvent? = null
    internal var table: String? = null
    internal var forEachRow: Boolean = false
    internal var whenCondition: String? = null
    internal val bodyParts = emptyMList<String>()

    /**
     * Sets the trigger timing/event.
     *
     * @since 3.6.0
     */
    fun event(event: TriggerEvent) {
        this.event = event
    }

    /** @since 3.6.0 */
    fun beforeInsert() { event = TriggerEvent.BeforeInsert }
    /** @since 3.6.0 */
    fun afterInsert() { event = TriggerEvent.AfterInsert }
    /** @since 3.6.0 */
    fun beforeUpdate() { event = TriggerEvent.BeforeUpdate }
    /** @since 3.6.0 */
    fun afterUpdate() { event = TriggerEvent.AfterUpdate }
    /** @since 3.6.0 */
    fun beforeDelete() { event = TriggerEvent.BeforeDelete }
    /** @since 3.6.0 */
    fun afterDelete() { event = TriggerEvent.AfterDelete }

    /**
     * Sets the table on which the trigger fires.
     *
     * @since 3.6.0
     */
    fun onTable(tableName: String) {
        table = tableName
    }

    /**
     * Enables FOR EACH ROW on the trigger.
     *
     * @since 3.6.0
     */
    fun forEachRow() {
        forEachRow = true
    }

    /**
     * Adds a WHEN condition to the trigger (PostgreSQL).
     *
     * @since 3.6.0
     */
    fun whenCondition(condition: String) {
        whenCondition = condition
    }

    /**
     * Adds a body statement to the trigger.
     *
     * @since 3.6.0
     */
    fun body(statement: String) {
        bodyParts += statement
    }
}

/**
 * Scope for building function definitions.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@SqlDslMarker
class FunctionScope @PublishedApi internal constructor() {
    internal val params = emptyMList<String>()
    internal var returnType: String? = null
    internal var language: String? = null
    internal var volatility: String? = null
    internal val bodyParts = emptyMList<String>()

    /**
     * Adds a parameter definition.
     *
     * @since 3.6.0
     */
    fun param(definition: String) {
        params += definition
    }

    /**
     * Sets the return type.
     *
     * @since 3.6.0
     */
    fun returns(type: String) {
        returnType = type
    }

    /**
     * Sets the function language (e.g., "plpgsql", "sql").
     *
     * @since 3.6.0
     */
    fun language(lang: String) {
        language = lang
    }

    /**
     * Sets the volatility category (IMMUTABLE, STABLE, VOLATILE).
     *
     * @since 3.6.0
     */
    fun immutable() { volatility = "IMMUTABLE" }
    /** @since 3.6.0 */
    fun stable() { volatility = "STABLE" }
    /** @since 3.6.0 */
    fun volatile() { volatility = "VOLATILE" }

    /**
     * Adds a body statement.
     *
     * @since 3.6.0
     */
    fun body(statement: String) {
        bodyParts += statement
    }
}

/**
 * Scope for building procedure definitions.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@SqlDslMarker
class ProcedureScope @PublishedApi internal constructor() {
    internal val params = emptyMList<String>()
    internal var language: String? = null
    internal val bodyParts = emptyMList<String>()

    /**
     * Adds a parameter definition.
     *
     * @since 3.6.0
     */
    fun param(definition: String) {
        params += definition
    }

    /**
     * Sets the procedure language.
     *
     * @since 3.6.0
     */
    fun language(lang: String) {
        language = lang
    }

    /**
     * Adds a body statement.
     *
     * @since 3.6.0
     */
    fun body(statement: String) {
        bodyParts += statement
    }
}

/**
 * Scope for building INSERT values – supports multi-row inserts.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@SqlDslMarker
class InsertValuesScope @PublishedApi internal constructor() {

    @PublishedApi
    internal val rows = emptyMList<List<String>>()

    /**
     * Adds a single row of values.
     *
     * ```kotlin
     * values {
     *     row("'Alice'", "30")
     *     row("'Bob'", "25")
     * }
     * ```
     *
     * @since 3.6.0
     */
    fun row(vararg values: String) {
        rows += values.toList()
    }
}

/**
 * Scope for building column definitions in CREATE TABLE.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@SqlDslMarker
class TableColumnsScope @PublishedApi internal constructor() {

    @PublishedApi
    internal val definitions = emptyMList<String>()

    /**
     * Adds a column definition.
     *
     * ```kotlin
     * createTable("users") {
     *     column("id SERIAL PRIMARY KEY")
     *     column("name VARCHAR(255) NOT NULL")
     *     column("email VARCHAR(255) UNIQUE")
     *     primaryKey("id")
     *     unique("email")
     * }
     * ```
     *
     * @since 3.6.0
     */
    fun column(definition: String) {
        definitions += definition
    }

    /**
     * Adds a PRIMARY KEY constraint.
     *
     * @since 3.6.0
     */
    fun primaryKey(vararg columns: String) {
        definitions += "PRIMARY KEY (${columns.joinToString(", ")})"
    }

    /**
     * Adds a UNIQUE constraint.
     *
     * @since 3.6.0
     */
    fun unique(vararg columns: String) {
        definitions += "UNIQUE (${columns.joinToString(", ")})"
    }

    /**
     * Adds a FOREIGN KEY constraint.
     *
     * @since 3.6.0
     */
    fun foreignKey(
        columns: String,
        references: String,
        onDelete: String? = null,
        onUpdate: String? = null
    ) {
        val fk = buildString {
            append("FOREIGN KEY ($columns) REFERENCES $references")
            onDelete?.let { append(" ON DELETE $it") }
            onUpdate?.let { append(" ON UPDATE $it") }
        }
        definitions += fk
    }

    /**
     * Adds a CHECK constraint.
     *
     * @since 3.6.0
     */
    fun check(expression: String) {
        definitions += "CHECK ($expression)"
    }

    /**
     * Adds a raw constraint definition.
     *
     * @since 3.6.0
     */
    fun constraint(definition: String) {
        definitions += definition
    }
}

// --- MAIN DSL BUILDER ---

/**
 * A DSL-based builder for generating SQL queries dynamically.
 *
 * Supports SELECT, INSERT, UPDATE, DELETE, TRUNCATE, and all DDL operations
 * (tables, views, materialized views, indexes, triggers, functions, procedures,
 * sequences, schemas, types, domains).
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@SqlDslMarker
class SqlBuilder @PublishedApi internal constructor() {

    @PublishedApi internal var queryType: QueryType? = null

    // SELECT
    @PublishedApi internal val selectColumns = emptyMList<String>()
    @PublishedApi internal var distinct = false
    @PublishedApi internal val fromTables = emptyMList<String>()
    @PublishedApi internal val joinParts = emptyMList<String>()
    @PublishedApi internal val groupByColumns = emptyMList<String>()
    @PublishedApi internal val havingConditions = emptyMList<String>()

    // INSERT
    @PublishedApi internal var insertTable: String? = null
    @PublishedApi internal val insertColumns = emptyMList<String>()
    @PublishedApi internal val insertRows = emptyMList<List<String>>()
    @PublishedApi internal var insertSelectQuery: String? = null

    // UPDATE
    @PublishedApi internal var updateTable: String? = null
    @PublishedApi internal val setExpressions = emptyMList<String>()

    // DELETE
    @PublishedApi internal var deleteTable: String? = null

    // DDL
    @PublishedApi internal val ddlParts = emptyMList<String>()

    // TRIGGER
    @PublishedApi internal var triggerName: String? = null
    @PublishedApi internal var triggerScope: TriggerScope? = null

    // FUNCTION
    @PublishedApi internal var functionName: String? = null
    @PublishedApi internal var functionIfNotExists = false
    @PublishedApi internal var functionScope: FunctionScope? = null

    // PROCEDURE
    @PublishedApi internal var procedureName: String? = null
    @PublishedApi internal var procedureIfNotExists = false
    @PublishedApi internal var procedureScope: ProcedureScope? = null

    // COMMON
    @PublishedApi internal val whereParts = emptyMList<String>()
    @PublishedApi internal val orderByParts = emptyMList<String>()
    @PublishedApi internal var limitValue: Int? = null
    @PublishedApi internal var offsetValue: Int? = null

    // SCHEMA
    @PublishedApi internal var setSchemaValue: String? = null

    enum class QueryType {
        Select, Insert, Update, Delete, Truncate,
        CreateView, AlterView, DropView,
        CreateMaterializedView, RefreshMaterializedView, AlterMaterializedView, DropMaterializedView,
        CreateTable, AlterTable, DropTable,
        CreateIndex, DropIndex,
        ShowTables, ShowTable, ShowColumnsFromTable, ShowIndexFromTable,
        CreateTrigger, DropTrigger,
        CreateFunction, DropFunction,
        CreateProcedure, DropProcedure,
        CreateSequence, AlterSequence, DropSequence,
        CreateSchema, AlterSchema, DropSchema, SetSchema,
        CreateType, AlterType, DropType,
        CreateDomain, ALterDomain, DropDomain,
        Raw
    }

    // -- Select --

    /**
     * Begins a SELECT query with optional columns.
     * If no columns are given, `*` is used.
     *
     * ```kotlin
     * sql {
     *     select("id", "name")
     *     from("users")
     * }
     * ```
     *
     * @since 3.6.0
     */
    fun select(vararg columns: String, distinct: Boolean = false) {
        queryType = QueryType.Select
        this.distinct = this.distinct || distinct
        if (columns.isEmpty()) {
            if (selectColumns.isEmpty()) selectColumns += "*"
        } else {
            // Remove "*" if we had it as placeholder and now have explicit columns
            if (selectColumns.size == 1 && selectColumns[0] == "*") selectColumns.clear()
            selectColumns += columns
        }
    }

    /**
     * Begins a `SELECT COUNT(expression)` query.
     *
     * @since 3.6.0
     */
    fun selectCount(expression: String = "*") {
        queryType = QueryType.Select
        selectColumns += "COUNT($expression)"
    }

    /**
     * Specifies the FROM tables.
     *
     * @since 3.6.0
     */
    fun from(vararg tables: String) {
        validate(tables.isNotEmpty())
        fromTables += tables
    }

    /**
     * Adds JOIN clauses using a dedicated [JoinScope].
     *
     * ```kotlin
     * sql {
     *     select("u.id", "o.total")
     *     from("users u")
     *     joins {
     *         inner("orders o", "o.user_id = u.id")
     *         left("payments p", "p.order_id = o.id")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    inline fun joins(block: JoinScope.() -> Unit) {
        joinParts += JoinScope().apply(block).joins
    }

    /**
     * Adds a single join clause (convenience shorthand).
     *
     * @since 3.6.0
     */
    fun join(table: String, type: JoinType, on: String? = null) {
        if (type.onCondition && on.isNullOrBlank())
            throw IllegalOperationException("Cannot join table $table with type $type without onCondition. Please provide onCondition to join table $table with type $type")
        if (!type.onCondition && on.isNotNullOrBlank())
            throw IllegalOperationException("Cannot join table $table with type $type with onCondition. Please provide onCondition to join table $table with type $type")
        joinParts += " ${+type.sqlKeyword} JOIN $table${if (on.isNotNullOrBlank()) " ON $on" else String.EMPTY}"
    }

    /**
     * Adds a lateral join clause.
     *
     * WARNING: If `alias` parameter is provided, automatically the method puts parentheses around the query with the alias after `)`.
     * Else, the query is added directly without parentheses.
     *
     * @since 3.9.3
     */
    fun lateralJoin(query: String, type: JoinType, alias: String? = null, on: String? = null) {
        if (type.onCondition && on.isNullOrBlank())
            throw IllegalOperationException("Cannot join with type $type without onCondition. Please provide onCondition to join with type $type")
        if (!type.onCondition && on.isNotNullOrBlank())
            throw IllegalOperationException("Cannot join with type $type with onCondition. Please provide onCondition to join with type $type")
        joinParts += " ${+type.sqlKeyword} JOIN LATERAL ${if (alias.isNotNullOrBlank()) "($query) $alias" else query}${if (on.isNotNullOrBlank()) " ON $on" else String.EMPTY}"
    }

    /**
     * Adds a lateral join clause using a [SqlQuery].
     *
     * WARNING: If `alias` parameter is provided, automatically the method puts parentheses around the query with the alias after `)`.
     * Else, the query is added directly without parentheses.
     *
     * @since 3.9.3
     */
    fun lateralJoin(query: SqlQuery, type: JoinType, alias: String? = null, on: String? = null) {
        if (type.onCondition && on.isNullOrBlank())
            throw IllegalOperationException("Cannot join with type $type without onCondition. Please provide onCondition to join with type $type")
        if (!type.onCondition && on.isNotNullOrBlank())
            throw IllegalOperationException("Cannot join with type $type with onCondition. Please provide onCondition to join with type $type")
        joinParts += " ${+type.sqlKeyword} JOIN LATERAL ${if (alias.isNotNullOrBlank()) "(${query.value}) $alias" else query.value}${if (on.isNotNullOrBlank()) " ON $on" else String.EMPTY}"
    }

    /**
     * Specifies GROUP BY columns.
     *
     * @since 3.6.0
     */
    fun groupBy(vararg columns: String) {
        groupByColumns += columns
    }

    /**
     * Adds a HAVING condition.
     *
     * @since 3.6.0
     */
    fun having(condition: String) {
        havingConditions += condition
    }

    // -- Where --

    /**
     * Adds WHERE conditions using a [WhereScope].
     *
     * ```kotlin
     * sql {
     *     select()
     *     from("users")
     *     where {
     *         condition("age > 18")
     *         and("status = 'ACTIVE'")
     *         or("vip = true")
     *         "role" `in` listOf("'ADMIN'", "'EDITOR'")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    inline fun where(autoApplyOperator: LogicOperator? = LogicOperator.And, block: WhereScope.() -> Unit) {
        val scope = WhereScope(autoApplyOperator).apply(block)
        whereParts += if (whereParts.isEmpty()) scope.parts
        else scope.parts.mapIndexed { index, string -> if (index.isPositive) string else autoApplyOperator?.let { "$it $string" } ?: string }
    }

    /**
     * Adds a simple WHERE condition (convenience for single-condition WHERE).
     *
     * @since 3.6.0
     */
    fun where(condition: String, autoApplyOperator: LogicOperator? = LogicOperator.And) {
        whereParts += if (whereParts.isEmpty()) condition else when(autoApplyOperator) {
            LogicOperator.And -> "AND $condition"
            LogicOperator.Or -> "OR $condition"
            null -> condition
        }
    }

    // -- Order by / Limit / Offset --

    /**
     * Adds ORDER BY clauses using an [OrderByScope].
     *
     * ```kotlin
     * sql {
     *     select()
     *     from("users")
     *     orderBy {
     *         desc("created_at")
     *         asc("name")
     *         nullsLast("email")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    inline fun orderBy(block: OrderByScope.() -> Unit) {
        orderByParts += OrderByScope().apply(block).columns
    }

    /**
     * Adds ORDER BY with pairs of column + direction.
     *
     * @since 3.6.0
     */
    fun orderBy(vararg columns: Pair<String, SortDirection>) {
        columns.forEach { [col, dir] ->
            orderByParts += col + if (dir == SortDirection.Descending) " DESC" else String.EMPTY
        }
    }

    /**
     * Adds ORDER BY with columns and a shared direction.
     *
     * @since 3.6.0
     */
    fun orderBy(vararg columns: String, direction: SortDirection = SortDirection.Ascending) {
        columns.forEach { col ->
            orderByParts += col + if (direction == SortDirection.Descending) " DESC" else String.EMPTY
        }
    }

    /**
     * Sets LIMIT.
     *
     * @since 3.6.0
     */
    fun limit(n: Int) {
        limitValue = n
    }

    /**
     * Sets OFFSET.
     *
     * @since 3.6.0
     */
    fun offset(n: Int) {
        offsetValue = n
    }

    /**
     * Sets LIMIT and OFFSET from an [IntRange].
     *
     * @since 3.6.0
     */
    fun range(range: IntRange) {
        limitValue = range.last - range.first + 1
        offsetValue = range.first
    }

    /**
     * Applies pagination to a dataset by specifying the range of items to retrieve based on the given page index and limit.
     *
     * @param pageIndex The index of the current page, starting from 0. Defaults to 0 if not provided.
     * @param limit The maximum number of items per page. Defaults to 10 if not provided.
     * @since 3.7.1
     */
    fun pagination(pageIndex: Int = 0, limit: Int = 10) {
        range((pageIndex * limit)..<(pageIndex * limit + limit))
    }

    // -- Insert --

    /**
     * Begins an INSERT INTO statement.
     *
     * ```kotlin
     * sql {
     *     insertInto("users")
     *     columns("name", "age")
     *     values {
     *         row("'Alice'", "30")
     *         row("'Bob'", "25")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    fun insertInto(table: String) {
        queryType = QueryType.Insert
        insertTable = table
    }

    /**
     * Specifies columns for the INSERT statement.
     *
     * @since 3.6.0
     */
    fun columns(vararg cols: String) {
        insertColumns += cols
    }

    /**
     * Adds values for the INSERT statement (single row, raw vararg).
     *
     * @since 3.6.0
     */
    fun values(vararg vals: String) {
        insertRows += vals.toList()
    }

    /**
     * Adds values using an [InsertValuesScope] for multi-row inserts.
     *
     * @since 3.6.0
     */
    inline fun values(block: InsertValuesScope.() -> Unit) {
        insertRows += InsertValuesScope().apply(block).rows
    }

    /**
     * INSERT ... SELECT support.
     *
     * ```kotlin
     * sql {
     *     insertInto("archive_users")
     *     columns("name", "age")
     *     fromSelect("SELECT name, age FROM users WHERE status = 'INACTIVE'")
     * }
     * ```
     *
     * @since 3.6.0
     */
    fun fromSelect(selectSql: String) {
        insertSelectQuery = selectSql
    }

    /**
     * INSERT ... SELECT with a nested DSL.
     *
     * ```kotlin
     * sql {
     *     insertInto("archive_users")
     *     columns("name", "age")
     *     fromSelect {
     *         select("name", "age")
     *         from("users")
     *         where("status = 'INACTIVE'")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    inline fun fromSelect(block: SqlBuilder.() -> Unit) {
        insertSelectQuery = SqlBuilder().apply(block).peek()
    }

    // -- Update --

    /**
     * Begins an UPDATE statement.
     *
     * ```kotlin
     * sql {
     *     update("users")
     *     set("name = 'Alice'", "age = 31")
     *     where("id = 1")
     * }
     * ```
     *
     * @since 3.6.0
     */
    fun update(table: String) {
        queryType = QueryType.Update
        updateTable = table
    }

    /**
     * Specifies SET expressions.
     *
     * @since 3.6.0
     */
    fun set(vararg expressions: String) {
        setExpressions += expressions
    }

    // -- Delete --

    /**
     * Begins a DELETE FROM statement.
     *
     * ```kotlin
     * sql {
     *     deleteFrom("users")
     *     where {
     *         condition("status = 'DELETED'")
     *         and("deleted_at < NOW() - INTERVAL '30 days'")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    fun deleteFrom(table: String) {
        queryType = QueryType.Delete
        deleteTable = table
    }

    // -- Truncate --

    /**
     * Builds a TRUNCATE TABLE statement.
     *
     * @since 3.6.0
     */
    fun truncate(table: String, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
        queryType = QueryType.Truncate
        ddlParts.clear()
        ddlParts += "TRUNCATE TABLE${if (ifExists) " IF EXISTS" else String.EMPTY} $table $dropType"
    }

    // -- View --

    /**
     * Builds a CREATE VIEW statement.
     *
     * @since 3.6.0
     */
    fun createView(viewName: String, orReplace: Boolean = false, selectSQL: String) {
        queryType = QueryType.CreateView
        ddlParts.clear()
        ddlParts += "CREATE${if (orReplace) " OR REPLACE" else String.EMPTY} VIEW $viewName AS $selectSQL"
    }

    /**
     * Builds a CREATE VIEW statement using a nested DSL for the SELECT part.
     *
     * ```kotlin
     * sql {
     *     createView("active_users") {
     *         select("id", "name")
     *         from("users")
     *         where("status = 'ACTIVE'")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    inline fun createView(viewName: String, orReplace: Boolean = false, block: SqlBuilder.() -> Unit) {
        createView(viewName, orReplace, SqlBuilder().apply(block).peek())
    }

    /**
     * Builds an ALTER VIEW statement.
     *
     * @since 3.6.0
     */
    fun alterView(viewName: String, ifExists: Boolean = false, selectSQL: String) {
        queryType = QueryType.AlterView
        ddlParts.clear()
        ddlParts += "ALTER VIEW${if (ifExists) " IF EXISTS" else String.EMPTY} $viewName AS $selectSQL"
    }

    /**
     * Builds a DROP VIEW statement.
     *
     * @since 3.6.0
     */
    fun dropView(viewName: String, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
        queryType = QueryType.DropView
        ddlParts.clear()
        ddlParts += "DROP VIEW${if (ifExists) " IF EXISTS" else String.EMPTY} $viewName $dropType"
    }

    // -- Materialized View --

    /**
     * Builds a CREATE MATERIALIZED VIEW statement.
     *
     * @since 3.6.0
     */
    fun createMaterializedView(viewName: String, orReplace: Boolean = false, withData: Boolean = true, selectSQL: String) {
        queryType = QueryType.CreateMaterializedView
        ddlParts.clear()
        ddlParts += "CREATE${if (orReplace) " OR REPLACE" else String.EMPTY} MATERIALIZED VIEW $viewName AS $selectSQL WITH ${if (!withData) "NO " else String.EMPTY}DATA"
    }

    /**
     * Builds a CREATE MATERIALIZED VIEW statement using a nested DSL.
     *
     * @since 3.6.0
     */
    inline fun createMaterializedView(viewName: String, orReplace: Boolean = false, withData: Boolean = true, block: SqlBuilder.() -> Unit) {
        createMaterializedView(viewName, orReplace, withData, SqlBuilder().apply(block).peek())
    }

    /**
     * Builds a REFRESH MATERIALIZED VIEW statement.
     *
     * @since 3.6.0
     */
    fun refreshMaterializedView(viewName: String, concurrently: Boolean = false) {
        queryType = QueryType.RefreshMaterializedView
        ddlParts.clear()
        ddlParts += "REFRESH MATERIALIZED VIEW${if (concurrently) " CONCURRENTLY" else String.EMPTY} $viewName"
    }

    /**
     * Builds an ALTER MATERIALIZED VIEW statement.
     *
     * @since 3.6.0
     */
    fun alterMaterializedView(viewName: String, ifExists: Boolean = false, action: String) {
        queryType = QueryType.AlterMaterializedView
        ddlParts.clear()
        ddlParts += "ALTER MATERIALIZED VIEW${if (ifExists) " IF EXISTS" else String.EMPTY} $viewName $action"
    }

    /**
     * Builds a DROP MATERIALIZED VIEW statement.
     *
     * @since 3.6.0
     */
    fun dropMaterializedView(viewName: String, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
        queryType = QueryType.DropMaterializedView
        ddlParts.clear()
        ddlParts += "DROP MATERIALIZED VIEW${if (ifExists) " IF EXISTS" else String.EMPTY} $viewName $dropType"
    }

    // -- Table --

    /**
     * Builds a CREATE TABLE statement with raw body.
     *
     * @since 3.6.0
     */
    fun createTable(tableName: String, body: String) {
        queryType = QueryType.CreateTable
        ddlParts.clear()
        ddlParts += "CREATE TABLE $tableName ($body)"
    }

    /**
     * Builds a CREATE TABLE statement using a [TableColumnsScope] DSL.
     *
     * ```kotlin
     * sql {
     *     createTable("users") {
     *         column("id SERIAL PRIMARY KEY")
     *         column("name VARCHAR(255) NOT NULL")
     *         column("email VARCHAR(255)")
     *         unique("email")
     *         foreignKey("department_id", "departments(id)", onDelete = "CASCADE")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    inline fun createTable(tableName: String, block: TableColumnsScope.() -> Unit) {
        createTable(tableName, TableColumnsScope().apply(block).definitions.joinToString(", "))
    }

    /**
     * Builds an ALTER TABLE statement.
     *
     * @since 3.6.0
     */
    fun alterTable(tableName: String, ifExists: Boolean = false, alteration: String) {
        queryType = QueryType.AlterTable
        ddlParts.clear()
        ddlParts += "ALTER TABLE${if (ifExists) " IF EXISTS" else String.EMPTY} $tableName $alteration"
    }

    /**
     * Builds a DROP TABLE statement.
     *
     * @since 3.6.0
     */
    fun dropTable(tableName: String, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
        queryType = QueryType.DropTable
        ddlParts.clear()
        ddlParts += "DROP TABLE${if (ifExists) " IF EXISTS" else String.EMPTY} $tableName $dropType"
    }

    // -- Index --

    /**
     * Builds a CREATE INDEX statement.
     *
     * @since 3.6.0
     */
    fun createIndex(indexName: String, table: String, columns: String, unique: Boolean = false) {
        queryType = QueryType.CreateIndex
        ddlParts.clear()
        ddlParts += "CREATE${if (unique) " UNIQUE" else String.EMPTY} INDEX $indexName ON $table ($columns)"
    }

    /**
     * Builds a DROP INDEX statement.
     *
     * @since 3.6.0
     */
    fun dropIndex(indexName: String, ifExists: Boolean = false, tableName: String) {
        queryType = QueryType.DropIndex
        ddlParts.clear()
        ddlParts += "DROP INDEX${if (ifExists) " IF EXISTS" else String.EMPTY} $indexName ON $tableName"
    }

    // -- Show --

    /** @since 3.6.0 */
    fun showTables() {
        queryType = QueryType.ShowTables
        ddlParts.clear()
        ddlParts += "SHOW TABLES"
    }

    /** @since 3.6.0 */
    fun showTable(tableName: String) {
        queryType = QueryType.ShowTable
        ddlParts.clear()
        ddlParts += "SHOW CREATE TABLE $tableName"
    }

    /** @since 3.6.0 */
    fun showColumnsFromTable(tableName: String) {
        queryType = QueryType.ShowColumnsFromTable
        ddlParts.clear()
        ddlParts += "SHOW COLUMNS FROM $tableName"
    }

    /** @since 3.6.0 */
    fun showIndexFromTable(tableName: String) {
        queryType = QueryType.ShowIndexFromTable
        ddlParts.clear()
        ddlParts += "SHOW INDEX FROM $tableName"
    }

    // -- Trigger --

    /**
     * Builds a CREATE TRIGGER statement using a [TriggerScope] DSL.
     *
     * ```kotlin
     * sql {
     *     createTrigger("audit_log") {
     *         afterInsert()
     *         onTable("orders")
     *         forEachRow()
     *         body("INSERT INTO audit_log(action) VALUES ('INSERT')")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    inline fun createTrigger(name: String, block: TriggerScope.() -> Unit) {
        queryType = QueryType.CreateTrigger
        triggerName = name
        triggerScope = TriggerScope().apply(block)
    }

    /**
     * Builds a DROP TRIGGER statement.
     *
     * @since 3.6.0
     */
    fun dropTrigger(name: String, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
        queryType = QueryType.DropTrigger
        ddlParts.clear()
        ddlParts += "DROP TRIGGER${if (ifExists) " IF EXISTS" else String.EMPTY} $name $dropType"
    }

    // -- Function --

    /**
     * Builds a CREATE FUNCTION statement using a [FunctionScope] DSL.
     *
     * ```kotlin
     * sql {
     *     createFunction("calculate_tax") {
     *         param("amount NUMERIC")
     *         returns("NUMERIC")
     *         language("plpgsql")
     *         stable()
     *         body("BEGIN RETURN amount * 0.22; END;")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    inline fun createFunction(name: String, ifNotExists: Boolean = false, block: FunctionScope.() -> Unit) {
        queryType = QueryType.CreateFunction
        functionName = name
        functionIfNotExists = ifNotExists
        functionScope = FunctionScope().apply(block)
    }

    /**
     * Builds a DROP FUNCTION statement.
     *
     * @since 3.6.0
     */
    fun dropFunction(name: String, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
        queryType = QueryType.DropFunction
        ddlParts.clear()
        ddlParts += "DROP FUNCTION${if (ifExists) " IF EXISTS" else String.EMPTY} $name $dropType"
    }

    // -- Procedure --

    /**
     * Builds a CREATE PROCEDURE statement using a [ProcedureScope] DSL.
     *
     * ```kotlin
     * sql {
     *     createProcedure("cleanup_old_users") {
     *         param("days_old INT")
     *         language("plpgsql")
     *         body("BEGIN DELETE FROM users WHERE created_at < NOW() - (days_old || ' days')::INTERVAL; END;")
     *     }
     * }
     * ```
     *
     * @since 3.6.0
     */
    inline fun createProcedure(name: String, ifNotExists: Boolean = false, block: ProcedureScope.() -> Unit) {
        queryType = QueryType.CreateProcedure
        procedureName = name
        procedureIfNotExists = ifNotExists
        procedureScope = ProcedureScope().apply(block)
    }

    /**
     * Builds a DROP PROCEDURE statement.
     *
     * @since 3.6.0
     */
    fun dropProcedure(name: String, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
        queryType = QueryType.DropProcedure
        ddlParts.clear()
        ddlParts += "DROP PROCEDURE${if (ifExists) " IF EXISTS" else String.EMPTY} $name $dropType"
    }

    // -- Sequence --

    /**
     * Builds a CREATE SEQUENCE statement.
     *
     * @since 3.6.0
     */
    fun createSequence(name: String, ifNotExists: Boolean = false, definition: String) {
        queryType = QueryType.CreateSequence
        ddlParts.clear()
        ddlParts += "CREATE SEQUENCE${if (ifNotExists) " IF NOT EXISTS" else String.EMPTY} $name $definition"
    }

    /**
     * Builds an ALTER SEQUENCE statement.
     *
     * @since 3.6.0
     */
    fun alterSequence(name: String, ifExists: Boolean = false, alteration: String) {
        queryType = QueryType.AlterSequence
        ddlParts.clear()
        ddlParts += "ALTER SEQUENCE${if (ifExists) " IF EXISTS" else String.EMPTY} $name $alteration"
    }

    /**
     * Builds a DROP SEQUENCE statement.
     *
     * @since 3.6.0
     */
    fun dropSequence(name: String, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
        queryType = QueryType.DropSequence
        ddlParts.clear()
        ddlParts += "DROP SEQUENCE${if (ifExists) " IF EXISTS" else String.EMPTY} $name $dropType"
    }

    // -- Schema --

    /**
     * Builds a CREATE SCHEMA statement.
     *
     * @since 3.6.0
     */
    fun createSchema(name: String, ifNotExists: Boolean = false, authorization: String? = null) {
        queryType = QueryType.CreateSchema
        ddlParts.clear()
        ddlParts += buildString {
            append("CREATE SCHEMA${if (ifNotExists) " IF NOT EXISTS" else String.EMPTY} $name")
            authorization?.let { append(" AUTHORIZATION $it") }
        }
    }

    /**
     * Builds an ALTER SCHEMA statement.
     *
     * @since 3.6.0
     */
    fun alterSchema(name: String, ifExists: Boolean = false, authorization: String? = null, rename: String? = null) {
        queryType = QueryType.AlterSchema
        ddlParts.clear()
        ddlParts += buildString {
            append("ALTER SCHEMA${if (ifExists) " IF EXISTS" else String.EMPTY} $name")
            authorization?.let { append(" AUTHORIZATION $it") }
            rename?.let { append(" RENAME TO $it") }
        }
    }

    /**
     * Builds a DROP SCHEMA statement.
     *
     * @since 3.6.0
     */
    fun dropSchema(name: String, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
        queryType = QueryType.DropSchema
        ddlParts.clear()
        ddlParts += "DROP SCHEMA${if (ifExists) " IF EXISTS" else String.EMPTY} $name $dropType"
    }

    /**
     * Builds a SET search_path statement.
     *
     * @since 3.6.0
     */
    fun setSchema(@Language("sql") searchPath: String, vararg schemas: String) {
        queryType = QueryType.SetSchema
        setSchemaValue = "SET $searchPath TO ${schemas.joinToString()}"
    }

    // -- Type --

    /**
     * Builds a CREATE TYPE ... AS ENUM statement.
     *
     * @since 3.6.0
     */
    fun createType(name: String, vararg entries: String) {
        queryType = QueryType.CreateType
        ddlParts.clear()
        ddlParts += "CREATE TYPE $name AS ENUM (${entries.joinToString { "'$it'" }})"
    }

    /**
     * Builds a CREATE TYPE ... AS (composite) statement.
     *
     * @since 3.6.0
     */
    fun createCompositeType(name: String, vararg columns: String) {
        queryType = QueryType.CreateType
        ddlParts.clear()
        ddlParts += "CREATE TYPE $name AS (${columns.joinToString()})"
    }

    /**
     * Builds an ALTER TYPE ... ADD VALUE statement.
     *
     * @since 3.6.0
     */
    fun addEntriesToType(typeName: String, vararg entries: String) {
        queryType = QueryType.AlterType
        ddlParts.clear()
        ddlParts += "ALTER TYPE $typeName ADD VALUE (${entries.joinToString { "'$it'" }})"
    }

    /**
     * Builds an ALTER TYPE ... ADD VALUE ... BEFORE statement.
     *
     * @since 3.6.0
     */
    fun addEntryBeforeOtherToType(typeName: String, entry: String, otherEntry: String) {
        queryType = QueryType.AlterType
        ddlParts.clear()
        ddlParts += "ALTER TYPE $typeName ADD VALUE '$entry' BEFORE $otherEntry"
    }

    /**
     * Builds an ALTER TYPE ... ADD VALUE IF NOT EXISTS statement.
     *
     * @since 3.6.0
     */
    fun addEntryToType(typeName: String, entry: String, ifNotExists: Boolean = false) {
        queryType = QueryType.AlterType
        ddlParts.clear()
        ddlParts += "ALTER TYPE $typeName ADD VALUE IF NOT EXISTS '$entry'"
    }

    /**
     * Builds an ALTER TYPE ... RENAME VALUE statement.
     *
     * @since 3.6.0
     */
    fun renameEntryInType(typeName: String, names: String2) {
        queryType = QueryType.AlterType
        ddlParts.clear()
        ddlParts += "ALTER TYPE $typeName RENAME VALUE ${names.first} TO ${names.second}"
    }

    /**
     * Builds an ALTER TYPE ... RENAME TO statement.
     *
     * @since 3.6.0
     */
    fun renameType(typeName: String, newName: String) {
        queryType = QueryType.AlterType
        ddlParts.clear()
        ddlParts += "ALTER TYPE $typeName RENAME TO $newName"
    }

    /**
     * Builds a DROP TYPE statement.
     *
     * @since 3.6.0
     */
    fun dropType(name: String, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
        queryType = QueryType.DropType
        ddlParts.clear()
        ddlParts += "DROP TYPE${if (ifExists) " IF EXISTS" else String.EMPTY} $name $dropType"
    }

    // -- Domain --

    /**
     * Builds a CREATE DOMAIN statement.
     *
     * @since 3.6.0
     */
    fun createDomain(name: String, `as`: String, check: String) {
        queryType = QueryType.CreateDomain
        ddlParts.clear()
        ddlParts += "CREATE DOMAIN $name AS $`as` CHECK ($check)"
    }

    /**
     * Builds an ALTER DOMAIN statement.
     *
     * @since 3.6.0
     */
    fun alterDomain(name: String, ifExists: Boolean = false, operation: String) {
        queryType = QueryType.ALterDomain
        ddlParts.clear()
        ddlParts += "ALTER DOMAIN${if (ifExists) " IF EXISTS" else String.EMPTY} $name $operation"
    }

    /**
     * Builds a DROP DOMAIN statement.
     *
     * @since 3.6.0
     */
    fun dropDomain(name: String, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
        queryType = QueryType.DropDomain
        ddlParts.clear()
        ddlParts += "DROP DOMAIN${if (ifExists) " IF EXISTS" else String.EMPTY} $name $dropType"
    }

    // -- Raw SQL --

    /**
     * Inserts a raw SQL statement. Use when no other method covers your need.
     *
     * @since 3.6.0
     */
    fun raw(@Language("sql") sql: String) {
        queryType = QueryType.Raw
        ddlParts.clear()
        ddlParts += sql
    }

    // -- Build --

    /**
     * Peeks at the generated SQL string without wrapping it in [SqlQuery].
     *
     * @since 3.6.0
     */
    fun peek(): String = when (queryType) {
        QueryType.Select -> buildString {
            append("SELECT ")
            if (distinct) append("DISTINCT ")
            append(selectColumns.joinToString(", "))
            if (fromTables.isNotEmpty()) append(" FROM ${fromTables.joinToString(", ")}")
            append(joinParts.joinToString(String.EMPTY))
            if (whereParts.isNotEmpty()) append(" WHERE ${whereParts.joinToString(" ")}")
            if (groupByColumns.isNotEmpty()) append(" GROUP BY ${groupByColumns.joinToString(", ")}")
            if (havingConditions.isNotEmpty()) append(" HAVING ${havingConditions.joinToString(" AND ")}")
            if (orderByParts.isNotEmpty()) append(" ORDER BY ${orderByParts.joinToString(", ")}")
            limitValue?.let { append(" LIMIT $it") }
            offsetValue?.let { append(" OFFSET $it") }
        }

        QueryType.Insert -> buildString {
            append("INSERT INTO $insertTable")
            if (insertColumns.isNotEmpty()) append(" (${insertColumns.joinToString(", ")})")
            if (insertSelectQuery.isNotNull()) {
                append(" $insertSelectQuery")
            } else {
                append(" VALUES ")
                append(insertRows.joinToString(", ") { row -> "(${row.joinToString(", ") { formatValue(it) }})" })
            }
        }

        QueryType.Update -> buildString {
            append("UPDATE $updateTable SET ${setExpressions.joinToString(", ")}")
            if (whereParts.isNotEmpty()) append(" WHERE ${whereParts.joinToString(" ")}")
        }

        QueryType.Delete -> buildString {
            append("DELETE FROM $deleteTable")
            if (whereParts.isNotEmpty()) append(" WHERE ${whereParts.joinToString(" ")}")
        }

        QueryType.CreateTrigger -> buildTrigger()
        QueryType.CreateFunction -> buildFunction()
        QueryType.CreateProcedure -> buildProcedure()

        QueryType.SetSchema -> setSchemaValue ?: error("SET SCHEMA not configured")

        else -> ddlParts.joinToString(String.EMPTY)
    }

    /**
     * Builds the final [SqlQuery].
     *
     * @since 3.6.0
     */
    fun build(): SqlQuery = SqlQuery(peek())

    /**
     * Builds and converts to [Code].
     *
     * @since 3.6.0
     */
    fun buildAsCode() = build().toCode()

    // ── private helpers ──────────────────────────────────────────────────────

    private fun buildTrigger(): String {
        val scope = triggerScope ?: error("Trigger scope not configured")
        return buildString {
            append("CREATE TRIGGER $triggerName")
            append(" ${scope.event?.sql ?: error("Trigger event not set")}")
            append(" ON ${scope.table ?: error("Trigger table not set")}")
            if (scope.forEachRow) append(" FOR EACH ROW")
            scope.whenCondition?.let { append(" WHEN ($it)") }
            if (scope.bodyParts.isNotEmpty()) {
                append(" BEGIN")
                scope.bodyParts.forEach { append(" $it") }
                append(" END")
            }
        }
    }

    private fun buildFunction(): String {
        val scope = functionScope ?: error("Function scope not configured")
        return buildString {
            append("CREATE FUNCTION${if (functionIfNotExists) " IF NOT EXISTS" else String.EMPTY} $functionName")
            append("(${scope.params.joinToString(", ")})")
            scope.returnType?.let { append(" RETURNS $it") }
            scope.language?.let { append(" LANGUAGE $it") }
            scope.volatility?.let { append(" $it") }
            if (scope.bodyParts.isNotEmpty()) {
                append(" AS $$")
                scope.bodyParts.forEach { append(" $it") }
                append(" $$")
            }
        }
    }

    private fun buildProcedure(): String {
        val scope = procedureScope ?: error("Procedure scope not configured")
        return buildString {
            append("CREATE PROCEDURE${if (procedureIfNotExists) " IF NOT EXISTS" else String.EMPTY} $procedureName")
            append("(${scope.params.joinToString(", ")})")
            scope.language?.let { append(" LANGUAGE $it") }
            if (scope.bodyParts.isNotEmpty()) {
                append(" AS $$")
                scope.bodyParts.forEach { append(" $it") }
                append(" $$")
            }
        }
    }

    private fun formatValue(v: Any): String = when (v) {
        is Number -> v.toString()
        else -> {
            val s = v.toString()
            // Don't re-quote if it's already a quoted string, function call, keyword, etc.
            if (s.startsWith("'") || s.contains("(") || s.equals("NULL", ignoreCase = true) ||
                s.equals("DEFAULT", ignoreCase = true) || s.equals("TRUE", ignoreCase = true) ||
                s.equals("FALSE", ignoreCase = true)
            ) s
            else "'" + s.replace("'", "''") + "'"
        }
    }
}

// --- TOP-LEVEL ENTRY POINTS ---

/**
 * Main DSL entry point. Constructs an [SqlQuery] from the DSL block.
 *
 * ```kotlin
 * val query = buildSql {
 *     select("u.id", "u.name", "o.total")
 *     from("users u")
 *     joins {
 *         inner("orders o", "o.user_id = u.id")
 *         left("payments p", "p.order_id = o.id")
 *     }
 *     where {
 *         condition("u.status = 'ACTIVE'")
 *         and("o.total > 100")
 *         orGroup {
 *             condition("u.vip = true")
 *             and("u.tier = 'GOLD'")
 *         }
 *     }
 *     orderBy {
 *         desc("o.total")
 *         nullsLast("u.name")
 *     }
 *     limit(50)
 *     offset(10)
 * }
 * ```
 *
 * @since 3.6.0
 */
@OptIn(ExperimentalContracts::class)
fun buildSql(block: SqlBuilder.() -> Unit): SqlQuery {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return SqlBuilder().apply(block).build()
}

/**
 * Main DSL entry point. Constructs an [SqlQuery] from the DSL block.
 *
 * ```kotlin
 * val query = buildSql {
 *     select("u.id", "u.name", "o.total")
 *     from("users u")
 *     joins {
 *         inner("orders o", "o.user_id = u.id")
 *         left("payments p", "p.order_id = o.id")
 *     }
 *     where {
 *         condition("u.status = 'ACTIVE'")
 *         and("o.total > 100")
 *         orGroup {
 *             condition("u.vip = true")
 *             and("u.tier = 'GOLD'")
 *         }
 *     }
 *     orderBy {
 *         desc("o.total")
 *         nullsLast("u.name")
 *     }
 *     limit(50)
 *     offset(10)
 * }
 * ```
 *
 * @since 3.6.0
 */
@OptIn(ExperimentalContracts::class)
fun buildSqlAsCode(block: SqlBuilder.() -> Unit): Code {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return SqlBuilder().apply(block).build().toCode()
}

/**
 * Main DSL entry point. Constructs an [SqlQuery] from the DSL block.
 *
 * ```kotlin
 * val query = initSql {
 *     select("u.id", "u.name", "o.total")
 *     from("users u")
 *     joins {
 *         inner("orders o", "o.user_id = u.id")
 *         left("payments p", "p.order_id = o.id")
 *     }
 *     where {
 *         condition("u.status = 'ACTIVE'")
 *         and("o.total > 100")
 *         orGroup {
 *             condition("u.vip = true")
 *             and("u.tier = 'GOLD'")
 *         }
 *     }
 *     orderBy {
 *         desc("o.total")
 *         nullsLast("u.name")
 *     }
 *     limit(50)
 *     offset(10)
 * }
 * ```
 *
 * @since 3.6.0
 */
@OptIn(ExperimentalContracts::class)
fun initSql(block: SqlBuilder.() -> Unit): SqlBuilder {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return SqlBuilder().apply(block)
}

/**
 * Combines multiple DSL-built queries into a single semicolon-separated string.
 *
 * ```kotlin
 * val migration = sqlBatch {
 *     +sql { createTable("users") { column("id SERIAL PRIMARY KEY") } }
 *     +sql { createIndex("idx_users_email", "users", "email", unique = true) }
 * }
 * ```
 *
 * @since 3.6.0
 */
@OptIn(ExperimentalContracts::class)
fun sqlBatch(block: SqlBatchScope.() -> Unit): String {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return SqlBatchScope().apply(block).build()
}

/**
 * Scope for combining multiple [SqlQuery] instances into a batch.
 *
 * @since 3.6.0
 */
@SqlDslMarker
class SqlBatchScope @PublishedApi internal constructor() {
    @PublishedApi
    internal val queries = emptyMList<SqlQuery>()

    /**
     * Adds a query to the batch.
     *
     * @since 3.6.0
     */
    operator fun SqlQuery.unaryPlus() {
        queries += this
    }

    /**
     * Adds a query built inline.
     *
     * @since 3.6.0
     */
    fun query(block: SqlBuilder.() -> Unit) {
        queries += buildSql(block)
    }

    @PublishedApi
    internal fun build() = queries.joinToString(";\n") { it.value }
}