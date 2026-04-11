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
enum class JoinType(@param:Language("sql") val sqlKeyword: String) {
    INNER("INNER"),
    LEFT_OUTER("LEFT"),
    RIGHT_OUTER("RIGHT"),
    FULL_OUTER("FULL"),
    CROSS("CROSS"),
    SELF("SELF"),
    NATURAL("NATURAL")
}

/**
 * Represents logical operators used in WHERE clause composition.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
enum class LogicOperator { AND, OR }

/**
 * Represents drop behavior for DDL operations.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
enum class DropType { CASCADE, RESTRICT }

/**
 * Represents trigger timing events.
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
enum class TriggerEvent {
    BEFORE_INSERT, AFTER_INSERT,
    BEFORE_UPDATE, AFTER_UPDATE,
    BEFORE_DELETE, AFTER_DELETE;

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
    fun condition(@Language("sql") expr: String, autoApplyOperator: LogicOperator? = this.autoApplyOperator) {
        parts += if (parts.isEmpty()) expr else when (autoApplyOperator) {
            LogicOperator.AND -> "AND $expr"
            LogicOperator.OR -> "OR $expr"
            null -> expr
        }
    }

    /**
     * Adds an AND condition.
     *
     * @since 3.6.0
     */
    fun and(@Language("sql") expr: String) {
        parts += "AND $expr"
    }

    /**
     * Adds an OR condition.
     *
     * @since 3.6.0
     */
    fun or(@Language("sql") expr: String) {
        parts += "OR $expr"
    }

    /**
     * Adds a NOT condition with an optional preceding logic operator.
     *
     * @since 3.6.0
     */
    fun not(@Language("sql") expr: String, logicOperator: LogicOperator? = null) {
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
    infix fun String.`in`(values: StringCollection) {
        condition("$this IN (${values.joinToString(", ")})")
    }

    /**
     * Adds a `NOT IN` condition.
     *
     * @since 3.6.0
     */
    infix fun String.notIn(values: StringCollection) {
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
     * Adds a `NOT EXISTS` subquery condition.
     *
     * @since 3.6.0
     */
    fun notExists(@Language("sql") subquery: String) {
        condition("NOT EXISTS ($subquery)")
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
    fun join(@Language("sql") table: String, type: JoinType, @Language("sql") on: String) {
        joins += " ${+type.sqlKeyword} JOIN $table ON $on"
    }

    /**
     * Adds a join clause using a raw join type string.
     *
     * @since 3.6.0
     */
    fun join(@Language("sql") table: String, @Language("sql") type: String, @Language("sql") on: String) {
        joins += " ${+type} JOIN $table ON $on"
    }

    /**
     * Adds an INNER JOIN.
     *
     * @since 3.6.0
     */
    fun inner(@Language("sql") table: String, @Language("sql") on: String) =
        join(table, JoinType.INNER, on)

    /**
     * Adds a LEFT JOIN.
     *
     * @since 3.6.0
     */
    fun left(@Language("sql") table: String, @Language("sql") on: String) =
        join(table, JoinType.LEFT_OUTER, on)

    /**
     * Adds a RIGHT JOIN.
     *
     * @since 3.6.0
     */
    fun right(@Language("sql") table: String, @Language("sql") on: String) =
        join(table, JoinType.RIGHT_OUTER, on)

    /**
     * Adds a FULL OUTER JOIN.
     *
     * @since 3.6.0
     */
    fun full(@Language("sql") table: String, @Language("sql") on: String) =
        join(table, JoinType.FULL_OUTER, on)

    /**
     * Adds a CROSS JOIN.
     *
     * @since 3.6.0
     */
    fun cross(@Language("sql") table: String) {
        joins += " CROSS JOIN $table"
    }

    /**
     * Adds a NATURAL JOIN.
     *
     * @since 3.6.0
     */
    fun natural(@Language("sql") table: String) {
        joins += " NATURAL JOIN $table"
    }

    /**
     * Adds a LATERAL JOIN using a raw SQL subquery string and [JoinType].
     *
     * @since 3.6.0
     */
    fun lateral(@Language("sql") query: String, type: JoinType, @Language("sql") on: String) {
        joins += " ${+type.sqlKeyword} JOIN LATERAL ($query) ON $on"
    }

    /**
     * Adds a LATERAL JOIN using a raw SQL subquery string and raw join type string.
     *
     * @since 3.6.0
     */
    fun lateral(@Language("sql") query: String, @Language("sql") type: String, @Language("sql") on: String) {
        joins += " ${+type} JOIN LATERAL ($query) ON $on"
    }

    /**
     * Adds a LATERAL JOIN using a [SqlQuery] object and [JoinType].
     *
     * @since 3.6.0
     */
    fun lateral(query: SqlQuery, type: JoinType, @Language("sql") on: String) {
        joins += " ${+type.sqlKeyword} JOIN LATERAL (${query.value}) ON $on"
    }

    /**
     * Adds a LATERAL JOIN using a [SqlQuery] object and raw join type string.
     *
     * @since 3.6.0
     */
    fun lateral(query: SqlQuery, @Language("sql") type: String, @Language("sql") on: String) {
        joins += " ${+type} JOIN LATERAL (${query.value}) ON $on"
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
    fun column(@Language("sql") name: String, direction: SortDirection = SortDirection.ASCENDING) {
        columns += name + if (direction == SortDirection.DESCENDING) " DESC" else String.EMPTY
    }

    /**
     * Shorthand: ascending column.
     *
     * @since 3.6.0
     */
    fun asc(@Language("sql") name: String) = column(name, SortDirection.ASCENDING)

    /**
     * Shorthand: descending column.
     *
     * @since 3.6.0
     */
    fun desc(@Language("sql") name: String) = column(name, SortDirection.DESCENDING)

    /**
     * Adds a column with NULLS FIRST directive.
     *
     * @since 3.6.0
     */
    fun nullsFirst(@Language("sql") name: String, direction: SortDirection = SortDirection.ASCENDING) {
        columns += name + (if (direction == SortDirection.DESCENDING) " DESC" else String.EMPTY) + " NULLS FIRST"
    }

    /**
     * Adds a column with NULLS LAST directive.
     *
     * @since 3.6.0
     */
    fun nullsLast(@Language("sql") name: String, direction: SortDirection = SortDirection.ASCENDING) {
        columns += name + (if (direction == SortDirection.DESCENDING) " DESC" else String.EMPTY) + " NULLS LAST"
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
    fun beforeInsert() { event = TriggerEvent.BEFORE_INSERT }
    /** @since 3.6.0 */
    fun afterInsert() { event = TriggerEvent.AFTER_INSERT }
    /** @since 3.6.0 */
    fun beforeUpdate() { event = TriggerEvent.BEFORE_UPDATE }
    /** @since 3.6.0 */
    fun afterUpdate() { event = TriggerEvent.AFTER_UPDATE }
    /** @since 3.6.0 */
    fun beforeDelete() { event = TriggerEvent.BEFORE_DELETE }
    /** @since 3.6.0 */
    fun afterDelete() { event = TriggerEvent.AFTER_DELETE }

    /**
     * Sets the table on which the trigger fires.
     *
     * @since 3.6.0
     */
    fun onTable(@Language("sql") tableName: String) {
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
    fun whenCondition(@Language("sql") condition: String) {
        whenCondition = condition
    }

    /**
     * Adds a body statement to the trigger.
     *
     * @since 3.6.0
     */
    fun body(@Language("sql") statement: String) {
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
    fun param(@Language("sql") definition: String) {
        params += definition
    }

    /**
     * Sets the return type.
     *
     * @since 3.6.0
     */
    fun returns(@Language("sql") type: String) {
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
    fun body(@Language("sql") statement: String) {
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
    fun param(@Language("sql") definition: String) {
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
    fun body(@Language("sql") statement: String) {
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
    fun row(@Language("sql") vararg values: String) {
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
    fun column(@Language("sql") definition: String) {
        definitions += definition
    }

    /**
     * Adds a PRIMARY KEY constraint.
     *
     * @since 3.6.0
     */
    fun primaryKey(@Language("sql") vararg columns: String) {
        definitions += "PRIMARY KEY (${columns.joinToString(", ")})"
    }

    /**
     * Adds a UNIQUE constraint.
     *
     * @since 3.6.0
     */
    fun unique(@Language("sql") vararg columns: String) {
        definitions += "UNIQUE (${columns.joinToString(", ")})"
    }

    /**
     * Adds a FOREIGN KEY constraint.
     *
     * @since 3.6.0
     */
    fun foreignKey(
        @Language("sql") columns: String,
        @Language("sql") references: String,
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
    fun check(@Language("sql") expression: String) {
        definitions += "CHECK ($expression)"
    }

    /**
     * Adds a raw constraint definition.
     *
     * @since 3.6.0
     */
    fun constraint(@Language("sql") definition: String) {
        definitions += definition
    }
}

// --- MAIN DSL BUILDER ---

/**
 * A DSL-based builder for generating SQL queries dynamically.
 *
 * Replaces the fluent-API [dev.tommasop1804.kutils.classes.builder.SqlQueryBuilder] with idiomatic Kotlin DSL scopes,
 * `@DslMarker` safety, and nested lambda support.
 *
 * Supports SELECT, INSERT, UPDATE, DELETE, TRUNCATE, and all DDL operations
 * (tables, views, materialized views, indexes, triggers, functions, procedures,
 * sequences, schemas, types, domains).
 *
 * @since 3.6.0
 * @author Tommaso Pastorelli
 */
@SqlDslMarker
class SqlDsl @PublishedApi internal constructor() {

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
        SELECT, INSERT, UPDATE, DELETE, TRUNCATE,
        CREATE_VIEW, ALTER_VIEW, DROP_VIEW,
        CREATE_MATERIALIZED_VIEW, REFRESH_MATERIALIZED_VIEW, ALTER_MATERIALIZED_VIEW, DROP_MATERIALIZED_VIEW,
        CREATE_TABLE, ALTER_TABLE, DROP_TABLE,
        CREATE_INDEX, DROP_INDEX,
        SHOW_TABLES, SHOW_TABLE, SHOW_COLUMNS_FROM_TABLE, SHOW_INDEX_FROM_TABLE,
        CREATE_TRIGGER, DROP_TRIGGER,
        CREATE_FUNCTION, DROP_FUNCTION,
        CREATE_PROCEDURE, DROP_PROCEDURE,
        CREATE_SEQUENCE, ALTER_SEQUENCE, DROP_SEQUENCE,
        CREATE_SCHEMA, ALTER_SCHEMA, DROP_SCHEMA, SET_SCHEMA,
        CREATE_TYPE, ALTER_TYPE, DROP_TYPE,
        CREATE_DOMAIN, ALTER_DOMAIN, DROP_DOMAIN,
        RAW
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
    fun select(@Language("sql") vararg columns: String, distinct: Boolean = false) {
        queryType = QueryType.SELECT
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
    fun selectCount(@Language("sql") expression: String = "*") {
        queryType = QueryType.SELECT
        selectColumns += "COUNT($expression)"
    }

    /**
     * Specifies the FROM tables.
     *
     * @since 3.6.0
     */
    fun from(@Language("sql") vararg tables: String) {
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
    fun join(@Language("sql") table: String, type: JoinType, @Language("sql") on: String) {
        joinParts += " ${+type.sqlKeyword} JOIN $table ON $on"
    }

    /**
     * Adds a single join clause with a raw join type string.
     *
     * @since 3.6.0
     */
    fun join(@Language("sql") table: String, @Language("sql") type: String, @Language("sql") on: String) {
        joinParts += " ${+type} JOIN $table ON $on"
    }

    /**
     * Adds a lateral join clause.
     *
     * @since 3.6.0
     */
    fun lateralJoin(@Language("sql") query: String, type: JoinType, @Language("sql") on: String) {
        joinParts += " ${+type.sqlKeyword} JOIN LATERAL ($query) ON $on"
    }

    /**
     * Adds a lateral join clause with a raw join type string.
     *
     * @since 3.6.0
     */
    fun lateralJoin(@Language("sql") query: String, @Language("sql") type: String, @Language("sql") on: String) {
        joinParts += " ${+type} JOIN LATERAL ($query) ON $on"
    }

    /**
     * Adds a lateral join clause using a [SqlQuery].
     *
     * @since 3.6.0
     */
    fun lateralJoin(query: SqlQuery, type: JoinType, @Language("sql") on: String) {
        joinParts += " ${+type.sqlKeyword} JOIN LATERAL (${query.value}) ON $on"
    }

    /**
     * Adds a lateral join clause using a [SqlQuery] and raw join type string.
     *
     * @since 3.6.0
     */
    fun lateralJoin(query: SqlQuery, @Language("sql") type: String, @Language("sql") on: String) {
        joinParts += " ${+type} JOIN LATERAL (${query.value}) ON $on"
    }

    /**
     * Specifies GROUP BY columns.
     *
     * @since 3.6.0
     */
    fun groupBy(@Language("sql") vararg columns: String) {
        groupByColumns += columns
    }

    /**
     * Adds a HAVING condition.
     *
     * @since 3.6.0
     */
    fun having(@Language("sql") condition: String) {
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
    inline fun where(autoApplyOperator: LogicOperator? = LogicOperator.AND, block: WhereScope.() -> Unit) {
        val scope = WhereScope(autoApplyOperator).apply(block)
        whereParts += if (whereParts.isEmpty()) scope.parts
        else scope.parts.mapIndexed { index, string -> if (index.isPositive) string else autoApplyOperator?.let { "$it $string" } ?: string }
    }

    /**
     * Adds a simple WHERE condition (convenience for single-condition WHERE).
     *
     * @since 3.6.0
     */
    fun where(@Language("sql") condition: String) {
        whereParts += condition
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
            orderByParts += col + if (dir == SortDirection.DESCENDING) " DESC" else String.EMPTY
        }
    }

    /**
     * Adds ORDER BY with columns and a shared direction.
     *
     * @since 3.6.0
     */
    fun orderBy(@Language("sql") vararg columns: String, direction: SortDirection = SortDirection.ASCENDING) {
        columns.forEach { col ->
            orderByParts += col + if (direction == SortDirection.DESCENDING) " DESC" else String.EMPTY
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
        queryType = QueryType.INSERT
        insertTable = table
    }

    /**
     * Specifies columns for the INSERT statement.
     *
     * @since 3.6.0
     */
    fun columns(@Language("sql") vararg cols: String) {
        insertColumns += cols
    }

    /**
     * Adds values for the INSERT statement (single row, raw vararg).
     *
     * @since 3.6.0
     */
    fun values(@Language("sql") vararg vals: String) {
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
    fun fromSelect(@Language("sql") selectSql: String) {
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
    inline fun fromSelect(block: SqlDsl.() -> Unit) {
        insertSelectQuery = SqlDsl().apply(block).peek()
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
        queryType = QueryType.UPDATE
        updateTable = table
    }

    /**
     * Specifies SET expressions.
     *
     * @since 3.6.0
     */
    fun set(@Language("sql") vararg expressions: String) {
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
        queryType = QueryType.DELETE
        deleteTable = table
    }

    // -- Truncate --

    /**
     * Builds a TRUNCATE TABLE statement.
     *
     * @since 3.6.0
     */
    fun truncate(@Language("sql") table: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT) {
        queryType = QueryType.TRUNCATE
        ddlParts.clear()
        ddlParts += "TRUNCATE TABLE${if (ifExists) " IF EXISTS" else String.EMPTY} $table $dropType"
    }

    // -- View --

    /**
     * Builds a CREATE VIEW statement.
     *
     * @since 3.6.0
     */
    fun createView(viewName: String, orReplace: Boolean = false, @Language("sql") selectSQL: String) {
        queryType = QueryType.CREATE_VIEW
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
    inline fun createView(viewName: String, orReplace: Boolean = false, block: SqlDsl.() -> Unit) {
        createView(viewName, orReplace, SqlDsl().apply(block).peek())
    }

    /**
     * Builds an ALTER VIEW statement.
     *
     * @since 3.6.0
     */
    fun alterView(viewName: String, ifExists: Boolean = false, @Language("sql") selectSQL: String) {
        queryType = QueryType.ALTER_VIEW
        ddlParts.clear()
        ddlParts += "ALTER VIEW${if (ifExists) " IF EXISTS" else String.EMPTY} $viewName AS $selectSQL"
    }

    /**
     * Builds a DROP VIEW statement.
     *
     * @since 3.6.0
     */
    fun dropView(viewName: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT) {
        queryType = QueryType.DROP_VIEW
        ddlParts.clear()
        ddlParts += "DROP VIEW${if (ifExists) " IF EXISTS" else String.EMPTY} $viewName $dropType"
    }

    // -- Materialized View --

    /**
     * Builds a CREATE MATERIALIZED VIEW statement.
     *
     * @since 3.6.0
     */
    fun createMaterializedView(viewName: String, orReplace: Boolean = false, withData: Boolean = true, @Language("sql") selectSQL: String) {
        queryType = QueryType.CREATE_MATERIALIZED_VIEW
        ddlParts.clear()
        ddlParts += "CREATE${if (orReplace) " OR REPLACE" else String.EMPTY} MATERIALIZED VIEW $viewName AS $selectSQL WITH ${if (!withData) "NO " else String.EMPTY}DATA"
    }

    /**
     * Builds a CREATE MATERIALIZED VIEW statement using a nested DSL.
     *
     * @since 3.6.0
     */
    inline fun createMaterializedView(viewName: String, orReplace: Boolean = false, withData: Boolean = true, block: SqlDsl.() -> Unit) {
        createMaterializedView(viewName, orReplace, withData, SqlDsl().apply(block).peek())
    }

    /**
     * Builds a REFRESH MATERIALIZED VIEW statement.
     *
     * @since 3.6.0
     */
    fun refreshMaterializedView(viewName: String, concurrently: Boolean = false) {
        queryType = QueryType.REFRESH_MATERIALIZED_VIEW
        ddlParts.clear()
        ddlParts += "REFRESH MATERIALIZED VIEW${if (concurrently) " CONCURRENTLY" else String.EMPTY} $viewName"
    }

    /**
     * Builds an ALTER MATERIALIZED VIEW statement.
     *
     * @since 3.6.0
     */
    fun alterMaterializedView(viewName: String, ifExists: Boolean = false, @Language("sql") action: String) {
        queryType = QueryType.ALTER_MATERIALIZED_VIEW
        ddlParts.clear()
        ddlParts += "ALTER MATERIALIZED VIEW${if (ifExists) " IF EXISTS" else String.EMPTY} $viewName $action"
    }

    /**
     * Builds a DROP MATERIALIZED VIEW statement.
     *
     * @since 3.6.0
     */
    fun dropMaterializedView(viewName: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT) {
        queryType = QueryType.DROP_MATERIALIZED_VIEW
        ddlParts.clear()
        ddlParts += "DROP MATERIALIZED VIEW${if (ifExists) " IF EXISTS" else String.EMPTY} $viewName $dropType"
    }

    // -- Table --

    /**
     * Builds a CREATE TABLE statement with raw body.
     *
     * @since 3.6.0
     */
    fun createTable(tableName: String, @Language("sql") body: String) {
        queryType = QueryType.CREATE_TABLE
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
    fun alterTable(tableName: String, ifExists: Boolean = false, @Language("sql") alteration: String) {
        queryType = QueryType.ALTER_TABLE
        ddlParts.clear()
        ddlParts += "ALTER TABLE${if (ifExists) " IF EXISTS" else String.EMPTY} $tableName $alteration"
    }

    /**
     * Builds a DROP TABLE statement.
     *
     * @since 3.6.0
     */
    fun dropTable(tableName: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT) {
        queryType = QueryType.DROP_TABLE
        ddlParts.clear()
        ddlParts += "DROP TABLE${if (ifExists) " IF EXISTS" else String.EMPTY} $tableName $dropType"
    }

    // -- Index --

    /**
     * Builds a CREATE INDEX statement.
     *
     * @since 3.6.0
     */
    fun createIndex(indexName: String, table: String, @Language("sql") columns: String, unique: Boolean = false) {
        queryType = QueryType.CREATE_INDEX
        ddlParts.clear()
        ddlParts += "CREATE${if (unique) " UNIQUE" else String.EMPTY} INDEX $indexName ON $table ($columns)"
    }

    /**
     * Builds a DROP INDEX statement.
     *
     * @since 3.6.0
     */
    fun dropIndex(indexName: String, ifExists: Boolean = false, tableName: String) {
        queryType = QueryType.DROP_INDEX
        ddlParts.clear()
        ddlParts += "DROP INDEX${if (ifExists) " IF EXISTS" else String.EMPTY} $indexName ON $tableName"
    }

    // -- Show --

    /** @since 3.6.0 */
    fun showTables() {
        queryType = QueryType.SHOW_TABLES
        ddlParts.clear()
        ddlParts += "SHOW TABLES"
    }

    /** @since 3.6.0 */
    fun showTable(tableName: String) {
        queryType = QueryType.SHOW_TABLE
        ddlParts.clear()
        ddlParts += "SHOW CREATE TABLE $tableName"
    }

    /** @since 3.6.0 */
    fun showColumnsFromTable(tableName: String) {
        queryType = QueryType.SHOW_COLUMNS_FROM_TABLE
        ddlParts.clear()
        ddlParts += "SHOW COLUMNS FROM $tableName"
    }

    /** @since 3.6.0 */
    fun showIndexFromTable(tableName: String) {
        queryType = QueryType.SHOW_INDEX_FROM_TABLE
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
        queryType = QueryType.CREATE_TRIGGER
        triggerName = name
        triggerScope = TriggerScope().apply(block)
    }

    /**
     * Builds a DROP TRIGGER statement.
     *
     * @since 3.6.0
     */
    fun dropTrigger(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT) {
        queryType = QueryType.DROP_TRIGGER
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
        queryType = QueryType.CREATE_FUNCTION
        functionName = name
        functionIfNotExists = ifNotExists
        functionScope = FunctionScope().apply(block)
    }

    /**
     * Builds a DROP FUNCTION statement.
     *
     * @since 3.6.0
     */
    fun dropFunction(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT) {
        queryType = QueryType.DROP_FUNCTION
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
        queryType = QueryType.CREATE_PROCEDURE
        procedureName = name
        procedureIfNotExists = ifNotExists
        procedureScope = ProcedureScope().apply(block)
    }

    /**
     * Builds a DROP PROCEDURE statement.
     *
     * @since 3.6.0
     */
    fun dropProcedure(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT) {
        queryType = QueryType.DROP_PROCEDURE
        ddlParts.clear()
        ddlParts += "DROP PROCEDURE${if (ifExists) " IF EXISTS" else String.EMPTY} $name $dropType"
    }

    // -- Sequence --

    /**
     * Builds a CREATE SEQUENCE statement.
     *
     * @since 3.6.0
     */
    fun createSequence(name: String, ifNotExists: Boolean = false, @Language("sql") definition: String) {
        queryType = QueryType.CREATE_SEQUENCE
        ddlParts.clear()
        ddlParts += "CREATE SEQUENCE${if (ifNotExists) " IF NOT EXISTS" else String.EMPTY} $name $definition"
    }

    /**
     * Builds an ALTER SEQUENCE statement.
     *
     * @since 3.6.0
     */
    fun alterSequence(name: String, ifExists: Boolean = false, @Language("sql") alteration: String) {
        queryType = QueryType.ALTER_SEQUENCE
        ddlParts.clear()
        ddlParts += "ALTER SEQUENCE${if (ifExists) " IF EXISTS" else String.EMPTY} $name $alteration"
    }

    /**
     * Builds a DROP SEQUENCE statement.
     *
     * @since 3.6.0
     */
    fun dropSequence(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT) {
        queryType = QueryType.DROP_SEQUENCE
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
        queryType = QueryType.CREATE_SCHEMA
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
        queryType = QueryType.ALTER_SCHEMA
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
    fun dropSchema(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT) {
        queryType = QueryType.DROP_SCHEMA
        ddlParts.clear()
        ddlParts += "DROP SCHEMA${if (ifExists) " IF EXISTS" else String.EMPTY} $name $dropType"
    }

    /**
     * Builds a SET search_path statement.
     *
     * @since 3.6.0
     */
    fun setSchema(@Language("sql") searchPath: String, vararg schemas: String) {
        queryType = QueryType.SET_SCHEMA
        setSchemaValue = "SET $searchPath TO ${schemas.joinToString()}"
    }

    // -- Type --

    /**
     * Builds a CREATE TYPE ... AS ENUM statement.
     *
     * @since 3.6.0
     */
    fun createType(name: String, vararg entries: String) {
        queryType = QueryType.CREATE_TYPE
        ddlParts.clear()
        ddlParts += "CREATE TYPE $name AS ENUM (${entries.joinToString { "'$it'" }})"
    }

    /**
     * Builds a CREATE TYPE ... AS (composite) statement.
     *
     * @since 3.6.0
     */
    fun createCompositeType(name: String, @Language("sql") vararg columns: String) {
        queryType = QueryType.CREATE_TYPE
        ddlParts.clear()
        ddlParts += "CREATE TYPE $name AS (${columns.joinToString()})"
    }

    /**
     * Builds an ALTER TYPE ... ADD VALUE statement.
     *
     * @since 3.6.0
     */
    fun addEntriesToType(typeName: String, vararg entries: String) {
        queryType = QueryType.ALTER_TYPE
        ddlParts.clear()
        ddlParts += "ALTER TYPE $typeName ADD VALUE (${entries.joinToString { "'$it'" }})"
    }

    /**
     * Builds an ALTER TYPE ... ADD VALUE ... BEFORE statement.
     *
     * @since 3.6.0
     */
    fun addEntryBeforeOtherToType(typeName: String, entry: String, otherEntry: String) {
        queryType = QueryType.ALTER_TYPE
        ddlParts.clear()
        ddlParts += "ALTER TYPE $typeName ADD VALUE '$entry' BEFORE $otherEntry"
    }

    /**
     * Builds an ALTER TYPE ... ADD VALUE IF NOT EXISTS statement.
     *
     * @since 3.6.0
     */
    fun addEntryToType(typeName: String, entry: String, ifNotExists: Boolean = false) {
        queryType = QueryType.ALTER_TYPE
        ddlParts.clear()
        ddlParts += "ALTER TYPE $typeName ADD VALUE IF NOT EXISTS '$entry'"
    }

    /**
     * Builds an ALTER TYPE ... RENAME VALUE statement.
     *
     * @since 3.6.0
     */
    fun renameEntryInType(typeName: String, names: String2) {
        queryType = QueryType.ALTER_TYPE
        ddlParts.clear()
        ddlParts += "ALTER TYPE $typeName RENAME VALUE ${names.first} TO ${names.second}"
    }

    /**
     * Builds an ALTER TYPE ... RENAME TO statement.
     *
     * @since 3.6.0
     */
    fun renameType(typeName: String, newName: String) {
        queryType = QueryType.ALTER_TYPE
        ddlParts.clear()
        ddlParts += "ALTER TYPE $typeName RENAME TO $newName"
    }

    /**
     * Builds a DROP TYPE statement.
     *
     * @since 3.6.0
     */
    fun dropType(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT) {
        queryType = QueryType.DROP_TYPE
        ddlParts.clear()
        ddlParts += "DROP TYPE${if (ifExists) " IF EXISTS" else String.EMPTY} $name $dropType"
    }

    // -- Domain --

    /**
     * Builds a CREATE DOMAIN statement.
     *
     * @since 3.6.0
     */
    fun createDomain(name: String, @Language("sql") `as`: String, @Language("sql") check: String) {
        queryType = QueryType.CREATE_DOMAIN
        ddlParts.clear()
        ddlParts += "CREATE DOMAIN $name AS $`as` CHECK ($check)"
    }

    /**
     * Builds an ALTER DOMAIN statement.
     *
     * @since 3.6.0
     */
    fun alterDomain(name: String, ifExists: Boolean = false, @Language("sql") operation: String) {
        queryType = QueryType.ALTER_DOMAIN
        ddlParts.clear()
        ddlParts += "ALTER DOMAIN${if (ifExists) " IF EXISTS" else String.EMPTY} $name $operation"
    }

    /**
     * Builds a DROP DOMAIN statement.
     *
     * @since 3.6.0
     */
    fun dropDomain(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT) {
        queryType = QueryType.DROP_DOMAIN
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
        queryType = QueryType.RAW
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
        QueryType.SELECT -> buildString {
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

        QueryType.INSERT -> buildString {
            append("INSERT INTO $insertTable")
            if (insertColumns.isNotEmpty()) append(" (${insertColumns.joinToString(", ")})")
            if (insertSelectQuery.isNotNull()) {
                append(" $insertSelectQuery")
            } else {
                append(" VALUES ")
                append(insertRows.joinToString(", ") { row -> "(${row.joinToString(", ") { formatValue(it) }})" })
            }
        }

        QueryType.UPDATE -> buildString {
            append("UPDATE $updateTable SET ${setExpressions.joinToString(", ")}")
            if (whereParts.isNotEmpty()) append(" WHERE ${whereParts.joinToString(" ")}")
        }

        QueryType.DELETE -> buildString {
            append("DELETE FROM $deleteTable")
            if (whereParts.isNotEmpty()) append(" WHERE ${whereParts.joinToString(" ")}")
        }

        QueryType.CREATE_TRIGGER -> buildTrigger()
        QueryType.CREATE_FUNCTION -> buildFunction()
        QueryType.CREATE_PROCEDURE -> buildProcedure()

        QueryType.SET_SCHEMA -> setSchemaValue ?: error("SET SCHEMA not configured")

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
 * val query = sql {
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
fun buildSql(block: SqlDsl.() -> Unit): SqlQuery {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return SqlDsl().apply(block).build()
}

/**
 * Main DSL entry point. Constructs an [SqlQuery] from the DSL block.
 *
 * ```kotlin
 * val query = sql {
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
fun buildSqlAsCode(block: SqlDsl.() -> Unit): Code {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return SqlDsl().apply(block).build().toCode()
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
    fun query(block: SqlDsl.() -> Unit) {
        queries += buildSql(block)
    }

    @PublishedApi
    internal fun build() = queries.joinToString(";\n") { it.value }
}

/**
 * Extension on [SqlQuery.Companion] for backwards compatibility.
 *
 * @since 3.6.0
 */
fun SqlQuery.Companion.build() = SqlDsl()