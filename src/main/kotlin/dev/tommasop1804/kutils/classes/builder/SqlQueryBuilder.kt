@file:Suppress("unused", "kutils_collection_declaration", "SqlNoDatasourceInspection")

package dev.tommasop1804.kutils.classes.builder

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.coding.SqlQuery
import dev.tommasop1804.kutils.classes.constants.SortDirection
import org.intellij.lang.annotations.Language
import kotlin.apply
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * A builder class for generating SQL queries dynamically.
 *
 * This class provides a fluent API to construct various types of SQL queries, such as
 * SELECT, INSERT, UPDATE, DELETE, TRUNCATE, and more. It supports additional SQL features
 * like JOINs, WHERE clauses, GROUP BY, ORDER BY, LIMIT, functions, procedures, triggers,
 * table management, views, and indexing.
 *
 * Upon building the query using the `build()` method, the constructed SQL query is
 * returned as a string.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
class SqlQueryBuilder {

    private lateinit var type: QueryType

    // SELECT
    private var selectClause = StringBuilder()
    private var fromClause = StringBuilder()
    private val joinClause = emptyMList<String>()
    private var groupByClause = StringBuilder()
    private var havingClause = StringBuilder()

    // INSERT
    private var insertClause = StringBuilder()
    private val insertColumns = emptyMList<String>()
    private val insertValues = emptyMList<String>()

    // UPDATE
    private var updateClause = StringBuilder()
    private var setClause = StringBuilder()

    // DDL, SHOW, TRUNCATE
    private var ddlClause = StringBuilder()

    // DELETE
    private var deleteClause = StringBuilder()

    // TRIGGER, FUNCTION, PROCEDURE
    private var bodyClause = StringBuilder()
    private var triggerTiming: String? = null
    private var triggerTable: String? = null
    private var triggerForEachRow: Boolean = false
    private var functionParams: String? = null
    private var functionReturns: String? = null
    private var procedureParams: String? = null

    // COMMON
    private var whereClause = StringBuilder()
    private var orderByClause = StringBuilder()
    private var limitClause = StringBuilder()
    private var offsetClause = StringBuilder()

    private var setSchemaClause = StringBuilder()

    private var hasWhere = false

    companion object {
        /**
         * Combines multiple SQL query builders into a single string where each query is separated by a semicolon and a newline.
         *
         * @param sqlQueryBuilder A variable number of `SqlQueryBuilder` instances whose built queries will be combined.
         * @return A single string containing the built SQL queries, separated by `;\n`.
         * @since 1.0.0
         */
        fun buildMoreThanOneQuery(vararg sqlQueryBuilder: SqlQueryBuilder) = sqlQueryBuilder.joinToString(";\n") { it.build() }
    }

    // SELECT
    /**
     * Specifies the columns to include in the SELECT statement.
     * If no columns are provided, a wildcard (*) is used, selecting all available columns.
     *
     * @param columns The names of the columns to include in the SELECT query. If empty, all columns will be selected.
     * @param distinct Specifies if the query should be distinct.
     * @return The current instance of SQLQueryBuilder for method chaining.
     * @since 1.0.0
     */
    fun select(@Language("sql") vararg columns: String, distinct: Boolean = false): SqlQueryBuilder {
        type = QueryType.SELECT
        selectClause += "SELECT "
        if (distinct) selectClause += "DISTINCT "
        selectClause += if (columns.isEmpty()) "*" else columns.joinToString(", ")
        return this
    }

    /**
     * Specifies the tables for the SQL query. This method appends a `FROM` clause to the query,
     * associating it with the specified tables. Must be used following a call to the `select()` method.
     *
     * @param tables The names of the tables to include in the `FROM` clause of the SQL query.
     * @return The current instance of `SQLQueryBuilder` to allow method chaining.
     * @throws UnsupportedOperationException If the query type is not `SELECT`, indicating that
     * the `select()` method must be invoked before calling this method.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if the list of tables is empty.
     * @since 1.0.0
     */
    fun from(@Language("sql") vararg tables: String): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call from() method before calling select() method.")
        validate(tables.isNotEmpty())
        fromClause += " FROM ${tables.joinToString()}"
        return this
    }

    /**
     * Adds a join clause to the current SQL query.
     * This method allows specifying the type of join, the table to join with,
     * and the condition for the join operation. It can only be used after calling `select()`.
     *
     * @param table The name of the table to be joined.
     * @param joinType The type of join (e.g., "INNER", "LEFT", "RIGHT", "FULL").
     * @param onCondition The condition on which the join will be based.
     * @return The current instance of `SQLQueryBuilder` with the join clause appended.
     * @since 1.0.0
     */
    fun join(@Language("sql") table: String, @Language("sql") joinType: String, @Language("sql") onCondition: String): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call join() method before calling select() method.")
        joinClause += " ${+joinType} JOIN $table ON $onCondition"
        return this
    }

    /**
     * Adds a join clause to the SQL query builder for combining rows from two tables
     * based on a related column between them.
     *
     * @param table The name of the table to join with the current query table.
     * @param joinType The type of join to perform, represented as a `JoinType`
     * (e.g., INNER, LEFT, RIGHT, FULL).
     * @param onCondition The condition specifying the column relationship between
     * the tables to perform the join operation.
     * @return The current instance of `SQLQueryBuilder` for chaining further query
     * building operations.
     * @throws UnsupportedOperationException If the `join` method is called before
     * the query type is set to SELECT.
     * @since 1.0.0
     */
    fun join(@Language("sql") table: String, joinType: JoinType, @Language("sql") onCondition: String): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call join() method before calling select() method.")
        joinClause += " ${+joinType.sqlKeyWord} JOIN $table ON $onCondition"
        return this
    }

    /**
     * Adds a lateral join clause to the current SQL query.
     *
     * The lateral join allows joining a result set produced by a subquery
     * with the current query. The type of join and the on-condition specify
     * how the rows from the two data sources should be combined.
     *
     * @param query The query representing the subquery to join laterally.
     * @param joinType The type of join to perform (e.g., INNER, OUTER).
     * @param onCondition The condition specifying how rows from the two data
     * sources should be matched.
     * @return An instance of `SQLQueryBuilder` with the updated lateral join clause.
     * @since 1.0.0
     */
    fun lateralJoin(@Language("sql") query: String, @Language("sql") joinType: String, @Language("sql") onCondition: String): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call join() method before calling select() method.")
        joinClause += " ${+joinType} JOIN LATERAL ($query) ON $onCondition"
        return this
    }

    /**
     * Adds a lateral join clause to the SQL query under construction.
     *
     * @param query the SQL query to be joined laterally.
     * @param joinType the type of join operation to be performed (e.g., INNER, OUTER).
     * @param onCondition the condition to specify how the tables are joined.
     * @return the current instance of SQLQueryBuilder with the updated join clause.
     * @since 1.0.0
     */
    fun lateralJoin(@Language("sql") query: String, joinType: JoinType, @Language("sql") onCondition: String): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call join() method before calling select() method.")
        joinClause += " ${+joinType.sqlKeyWord} JOIN LATERAL ($query) ON $onCondition"
        return this
    }

    /**
     * Adds a lateral join clause to the current SQL query.
     *
     * This method modifies the current SQL query by adding a lateral join clause
     * with the specified join type and condition on a subquery.
     *
     * @param query the SQL subquery, represented as a `Code` object, to be used in the lateral join.
     *              The subquery must be in SQL language.
     * @param joinType the type of join to include (e.g., INNER, LEFT, RIGHT).
     * @param onCondition the condition that specifies how the tables are joined.
     * @return the updated `SQLQueryBuilder` instance with the lateral join clause added.
     * @throws UnsupportedOperationException if the current query type is not SELECT.
     * @throws IllegalArgumentException if the provided subquery is not in SQL language.
     * @since 1.0.0
     */
    fun lateralJoin(query: SqlQuery, @Language("sql") joinType: String, @Language("sql") onCondition: String): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call join() method before calling select() method.")
        joinClause += " ${+joinType} JOIN LATERAL (${query.value}) ON $onCondition"
        return this
    }

    /**
     * Performs a lateral join operation on the current SQL query. A lateral join allows joining a table
     * expression which depends on columns of preceding joins in the query.
     *
     * @param query the SQL code block representing the join table or subquery. It must be in SQL language.
     * @param joinType the type of join to perform, such as INNER, LEFT, or RIGHT, represented by the JoinType enum.
     * @param onCondition the condition used to join the tables, specified as a string.
     *
     * @return the updated SQLQueryBuilder instance containing the added lateral join clause.
     *
     * @throws UnsupportedOperationException if the method is called before a SELECT operation has been specified.
     * @throws IllegalArgumentException if the provided code block is not in SQL language.
     *
     * @since 1.0.0
     */
    fun lateralJoin(query: SqlQuery, joinType: JoinType, @Language("sql") onCondition: String): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call join() method before calling select() method.")
        joinClause += " ${+joinType.sqlKeyWord} JOIN LATERAL (${query.value}) ON $onCondition"
        return this
    }

    /**
     * Adds a `GROUP BY` clause to the current SQL query with the specified columns.
     *
     * This method specifies the columns to be used for grouping the query results.
     * The method must be called after a `SELECT` query is initiated; otherwise, it will throw
     * an exception if called without a valid `SELECT` query context.
     *
     * @param columns The columns to group the query results by. Each column should correspond to a valid
     * name in the queried table or result set.
     * @return The current instance of `SQLQueryBuilder` for chaining further query building.
     * @throws UnsupportedOperationException If this method is called before invoking the `select()` method.
     * @since 1.0.0
     */
    fun groupBy(@Language("sql") vararg columns: String): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call groupBy() method before calling select() method.")
        groupByClause += if (groupByClause.isEmpty()) " GROUP BY ${columns.joinToString(", ")}" else ", ${
            columns.joinToString(
                ", "
            )
        }"
        return this
    }

    /**
     * Appends a `HAVING` clause to the current SQL query builder.
     *
     * This method should only be used after a `SELECT` query has been initiated.
     * It allows you to specify a condition to filter the results after grouping.
     *
     * @param condition The condition to be used in the HAVING clause.
     *                  This is typically a logical expression or aggregation function filter.
     * @return The current instance of `SQLQueryBuilder` with the appended `HAVING` clause.
     * @throws UnsupportedOperationException If called before a `SELECT` query is initiated.
     * @since 1.0.0
     */
    fun having(@Language("sql") condition: String): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call having() method before calling select() method.")
        havingClause += if (havingClause.isEmpty()) " HAVING $condition" else " AND $condition"
        return this
    }

    /**
     * Configures the query to select a count of the specified expression.
     *
     * This method modifies the query type to SELECT and appends a count selection clause to the SELECT statement.
     *
     * @param expression the column or expression to count in the resultant query.
     * @return the current instance of SQLQueryBuilder for method chaining.
     * @since 1.0.0
     */
    fun selectCount(@Language("sql") expression: String = "*"): SqlQueryBuilder {
        type = QueryType.SELECT
        selectClause += "SELECT COUNT($expression)"
        return this
    }

    // INSERT
    /**
     * Adds an "INSERT INTO" clause to the SQL query for specifying the target table for insertion.
     *
     * @param table the name of the table into which records will be inserted.
     * @return the updated SQLQueryBuilder instance with the "INSERT INTO" clause added.
     * @since 1.0.0
     */
    fun insertInto(table: String): SqlQueryBuilder {
        type = QueryType.INSERT
        insertClause += "INSERT INTO $table"
        return this
    }

    /**
     * Specifies the columns to be used in an `INSERT` SQL query.
     *
     * This method is used to define one or more column names for the `INSERT` statement
     * of the SQL query. It can only be invoked after calling the `insertInto()` method
     * to ensure the query is in a valid state.
     *
     * @param columns A variable number of column names to be included in the `INSERT` statement.
     * @return The current instance of `SQLQueryBuilder` to allow method chaining.
     * @throws UnsupportedOperationException if this method is called before `insertInto()`.
     * @since 1.0.0
     */
    fun columns(@Language("sql") vararg columns: String): SqlQueryBuilder {
        type == QueryType.INSERT || throw UnsupportedOperationException("You can't call columns() method before calling insertInto() method.")
        insertColumns += columns
        return this
    }

    /**
     * Appends the specified values to be inserted into the respective columns of the target table.
     * This method is only applicable for INSERT queries and must be called after `insertInto()` method.
     *
     * @param values The list of values to be inserted into the target columns. Each value corresponds to
     *               a specific column in the INSERT statement.
     * @return The current instance of `SQLQueryBuilder` to allow method chaining.
     * @throws UnsupportedOperationException If this method is called without first calling `insertInto()`.
     * @since 1.0.0
     */
    fun values(@Language("sql") vararg values: String): SqlQueryBuilder {
        type == QueryType.INSERT || throw UnsupportedOperationException("You can't call values() method before calling insertInto() method.")
        insertValues += values
        return this
    }

    // UPDATE
    /**
     * Configures the query builder to construct an UPDATE SQL statement for the specified table.
     *
     * @param table the name of the table to update.
     * @return the current instance of the query builder with the update clause configured.
     * @since 1.0.0
     */
    fun update(table: String): SqlQueryBuilder {
        type = QueryType.UPDATE
        updateClause += "UPDATE $table"
        return this
    }

    /**
     * Specifies the columns and values to be updated in the SQL query. This method appends the provided column-value expressions
     * to the `SET` clause of the query. It can only be invoked after the `update()` method has been called.
     *
     * @param setExpression one or more column-value expressions to be included in the `SET` clause
     * @return the current instance of `SQLQueryBuilder` for method chaining
     * @throws UnsupportedOperationException if called before the `update()` method
     * @since 1.0.0
     */
    fun set(@Language("sql") vararg setExpression: String): SqlQueryBuilder {
        type == QueryType.UPDATE || throw UnsupportedOperationException("You can't call set() method before calling update() method.")
        setClause += setExpression.joinToString(", ")
        return this
    }

    // DELETE
    /**
     * Configures a DELETE SQL query by specifying the table to delete rows from.
     *
     * @param table the name of the table from which rows will be deleted.
     * @return the current instance of the SQLQueryBuilder with the DELETE clause configured.
     * @since 1.0.0
     */
    fun deleteFrom(table: String): SqlQueryBuilder {
        type = QueryType.DELETE
        deleteClause += "DELETE FROM $table"
        return this
    }

    // COMMON
    /**
     * Adds a WHERE condition to the SQL query. This method is used to specify conditions
     * for SELECT, UPDATE, or DELETE queries. It appends the condition to the existing
     * WHERE clause or adds a new one if no clause exists yet.
     *
     * @param condition the WHERE condition to be added to the query
     * @param autoApplyOperator whether to automatically apply the logical operator (AND, OR) to the next condition.
     * Auto is `AND`. For no-auto-apply operator use `null`.
     * @return the SQLQueryBuilder instance for method chaining
     * @throws UnsupportedOperationException if the query is not of DELETE, UPDATE or SELECT
     * @since 1.0.0
     */
    fun where(@Language("sql") condition: String, autoApplyOperator: LogicOperator? = LogicOperator.AND): SqlQueryBuilder {
        (type == QueryType.DELETE
                || type == QueryType.UPDATE
                || type == QueryType.SELECT) || throw UnsupportedOperationException("You can't call where() method before calling from() method.")

        whereClause += if (whereClause.isEmpty()) " WHERE $condition" else when (autoApplyOperator) {
            LogicOperator.AND -> " AND $condition"
            LogicOperator.OR -> " OR $condition"
            null -> " WHERE $condition"
        }

        hasWhere = true
        return this
    }

    /**
     * Adds a condition to the current SQL query's `WHERE` clause, combining it with an `AND` operator.
     *
     * This method requires that a `WHERE` clause has already been initiated using the `where()` method.
     * It can only be used with `SELECT`, `UPDATE`, or `DELETE` query types after the `from()` method is called.
     *
     * @param condition The SQL condition to be added to the `WHERE` clause with an `AND` operator.
     * @return The current instance of `SQLQueryBuilder` for method chaining.
     * @throws UnsupportedOperationException if `where()` has not been called before using this method.
     * @throws UnsupportedOperationException if the query type is not `SELECT`, `UPDATE`, or `DELETE`.
     * @since 1.0.0
     */
    fun and(@Language("sql") condition: String): SqlQueryBuilder {
        hasWhere || throw UnsupportedOperationException("You can't call and() method before calling where() method.")
        (type == QueryType.DELETE
                || type == QueryType.UPDATE
                || type == QueryType.SELECT
                ) || throw UnsupportedOperationException("You can't call and() method before calling from() method.")
        whereClause += " AND $condition"
        return this
    }

    /**
     * Adds an OR condition to the SQL queries WHERE clause.
     * This method appends the specified condition using the OR logical operator.
     *
     * @param condition The condition to be appended with the OR operator.
     * @return The current instance of `SQLQueryBuilder` for method chaining.
     * @throws UnsupportedOperationException If called before `where()` or `from()` method depending on the query type.
     * @since 1.0.0
     */
    fun or(@Language("sql") condition: String): SqlQueryBuilder {
        hasWhere || throw UnsupportedOperationException("You can't call or() method before calling where() method.")
        (type == QueryType.DELETE
                || type == QueryType.UPDATE
                || type == QueryType.SELECT
                ) || throw UnsupportedOperationException("You can't call or() method before calling from() method.")
        whereClause += " OR $condition"
        return this
    }

    /**
     * Adds a NOT clause to the SQL query being built. The condition specified will be negated in the final SQL query.
     * This method must be called after a `where()` method and, depending on the query type, after a `from()` method.
     *
     * @param condition The SQL condition to negate. Must be provided as a valid SQL string.
     * @param logicOperator Optional logic operator (`AND`, `OR`, etc.) to prepend to the negated condition. If null, no operator will be added.
     * @return The current instance of `SqlQueryBuilder` for method chaining.
     * @throws UnsupportedOperationException If called before the `where()` method.
     * @throws UnsupportedOperationException If called before the `from()` method on `DELETE`, `UPDATE`, or `SELECT` query types.
     * @since 1.0.0
     */
    fun not(@Language("sql") condition: String, logicOperator: LogicOperator? = null): SqlQueryBuilder {
        hasWhere || throw UnsupportedOperationException("You can't call or() method before calling where() method.")
        (type == QueryType.DELETE
                || type == QueryType.UPDATE
                || type == QueryType.SELECT
                ) || throw UnsupportedOperationException("You can't call not() method before calling from() method.")
        whereClause += " ${if (logicOperator.isNotNull()) "${logicOperator.name} " else String.EMPTY}NOT ($condition)"
        return this
    }

    /**
     * Adds an ORDER BY clause to the SQL query, specifying the columns and their sorting directions.
     *
     * This method is only applicable for SELECT queries. It throws an UnsupportedOperationException if called before
     * a FROM clause is defined.
     *
     * @param columns a variable number of pairs, where each pair consists of a column name (String) and its sorting direction (SortingDirection)
     * @return the updated SQLQueryBuilder instance with the ORDER BY clause appended
     * @since 1.0.0
     */
    fun orderBy(vararg columns: Pair<String, SortDirection>): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call orderBy() method before calling from() method.")
        orderByClause += " ORDER BY "
        columns.forEachIndexed { i, it ->
            orderByClause += it.first + (if (it.second == SortDirection.ASCENDING) String.EMPTY else " DESC") + (if (i == columns.size - 1) String.EMPTY else ", ")
        }
        return this
    }

    /**
     * Appends an ORDER BY clause to the SQL query based on the provided columns and sorting direction.
     *
     * @param columns the columns to order the result set by. Multiple columns can be provided as vararg arguments.
     * @param direction the direction of sorting, by default set to `SortingDirection.ASCENDING`.
     * @return the updated `SQLQueryBuilder` instance for chaining further query configurations.
     * @throws UnsupportedOperationException if called before invoking the `from()` method on a SELECT query.
     * @since 1.0.0
     */
    fun orderBy(@Language("sql") vararg columns: String, direction: SortDirection = SortDirection.ASCENDING): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call orderBy() method before calling from() method.")
        orderByClause += " ORDER BY "
        columns.forEachIndexed { i, it ->
            orderByClause += it + (if (direction == SortDirection.ASCENDING) String.EMPTY else " DESC") + (if (i == columns.size - 1) String.EMPTY else ", ")
        }
        return this
    }

    /**
     * Adds a LIMIT clause to the SQL query, restricting the maximum number of records returned.
     *
     * This method can only be used with SELECT queries. If it is called before a `from()` method,
     * an exception will be thrown.
     *
     * @param limit the maximum number of records to return
     * @return the current instance of SQLQueryBuilder with the LIMIT clause applied
     * @throws UnsupportedOperationException if this method is called before defining a SELECT query
     * @since 1.0.0
     */
    fun limit(limit: Int): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call limit() method before calling from() method.")
        limitClause += " LIMIT $limit"
        return this
    }

    /**
     * Adds an OFFSET clause to the SQL query, specifying the number of rows to skip before starting to return rows.
     *
     * @param offset The number of rows to skip.
     * @return The current instance of SQLQueryBuilder with the updated OFFSET clause.
     * @since 1.0.0
     */
    fun offset(offset: Int): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call offset() method before calling from() method.")
        offsetClause += " OFFSET $offset"
        return this
    }

    /**
     * Sets a range of rows to select from the SQL query by specifying a limit and an offset.
     * This method modifies the query to include a LIMIT and OFFSET clause based on the provided range.
     *
     * @param range the range of rows to select, where `range.first` specifies the starting offset
     * and `(range.last - range.first + 1)` specifies the number of rows to retrieve.
     * @return the updated instance of SQLQueryBuilder with the applied range.
     * @throws UnsupportedOperationException if the method is called before defining a source table using `from()`.
     * @since 1.0.0
     */
    fun range(range: IntRange): SqlQueryBuilder {
        type == QueryType.SELECT || throw UnsupportedOperationException("You can't call limit() method before calling from() method.")
        limitClause += " LIMIT ${range.last - range.first + 1}"
        limitClause += " OFFSET ${range.first}"
        return this
    }

    // TRUNCATE
    /**
     * Modifies the query to truncate the specified table.
     * This operation removes all rows from a table without logging individual row deletions.
     *
     * @param table the name of the table to truncate
     * @param ifExists whether to include the "IF EXISTS" clause, allowing the operation to succeed if the table does not exist
     * @param dropType the type of drop operation to perform (CASCADE, RESTRICT)
     * @return the updated SQLQueryBuilder instance
     * @since 1.0.0
     */
    fun truncate(@Language("sql") table: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT): SqlQueryBuilder {
        type = QueryType.TRUNCATE
        ddlClause += "TRUNCATE TABLE${if (ifExists) " IF EXISTS" else String.EMPTY} $table"
        ddlClause += " $dropType"
        return this
    }

    // VIEW
    /**
     * Constructs a SQL query to create a view with the specified name and SQL definition.
     *
     * @param viewName The name of the view to be created.
     * @param orReplace Whether to replace an existing view with the same name (default: false).
     * @param selectSQL The SQL query that defines the content of the view.
     * @return The current instance of SQLQueryBuilder for method chaining.
     * @since 1.0.0
     */
    fun createView(viewName: String, orReplace: Boolean = false, @Language("sql") selectSQL: String): SqlQueryBuilder {
        type = QueryType.CREATE_VIEW
        ddlClause += ("CREATE" + (if (orReplace) " OR REPLACE" else String.EMPTY) +  " VIEW $viewName AS $selectSQL")
        return this
    }

    /**
     * Modifies an existing database view with the specified `viewName` by changing its definition
     * to the SQL query provided in `selectSQL`.
     *
     * @param viewName the name of the view to be altered
     * @param ifExists whether to include the "IF EXISTS" clause, allowing the operation to succeed if the view does not exist
     * @param selectSQL the new SQL query definition for the view
     * @return the updated instance of `SQLQueryBuilder` for method chaining
     * @since 1.0.0
     */
    fun alterView(viewName: String, ifExists: Boolean = false, @Language("sql") selectSQL: String): SqlQueryBuilder {
        type = QueryType.ALTER_VIEW
        ddlClause += "ALTER VIEW${if (ifExists) " IF EXISTS" else String.EMPTY} $viewName AS $selectSQL"
        return this
    }

    /**
     * Constructs a query to drop a view in the database.
     *
     * @param viewName the name of the view to be dropped
     * @param ifExists whether to include the "IF EXISTS" clause, allowing the operation to succeed if the view does not exist
     * @param dropType the type of drop operation to perform (CASCADE, RESTRICT)
     * @return the instance of SQLQueryBuilder with the modified query
     * @since 1.0.0
     */
    fun dropView(viewName: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT): SqlQueryBuilder {
        type = QueryType.DROP_VIEW
        ddlClause += "DROP VIEW${if (ifExists) " IF EXISTS" else String.EMPTY} $viewName"
        ddlClause += " $dropType"
        return this
    }

    // MATERIALIZED VIEW
    /**
     * Creates a SQL statement to define a materialized view.
     *
     * @param viewName The name of the materialized view to be created.
     * @param orReplace Determines whether to include the "OR REPLACE" clause, allowing an existing materialized view 
     *        with the same name to be replaced. Defaults to `false`.
     * @param withData Indicates whether to include the "WITH DATA" clause to populate the view with data immediately.
     *        If set to `false`, the "WITH NO DATA" clause will be included. Defaults to `true`.
     * @param selectSQL A SQL query string used to define the materialized view's data.
     * @return The instance of `SqlQueryBuilder` with the constructed SQL statement for the materialized view.
     * @since 1.0.0
     */
    fun createMaterializedView(viewName: String, orReplace: Boolean = false, withData: Boolean = true, @Language("sql") selectSQL: String): SqlQueryBuilder {
        type = QueryType.CREATE_MATERIALIZED_VIEW
        ddlClause += ("CREATE" + (if (orReplace) " OR REPLACE" else String.EMPTY) +  " MATERIALIZED VIEW $viewName AS $selectSQL WITH ${if (!withData) "NO " else String.EMPTY}DATA")
        return this
    }

    /**
     * Refreshes the specified materialized view in the database. 
     * Optionally allows the operation to be performed concurrently.
     *
     * @param viewName The name of the materialized view to be refreshed.
     * @param concurrently Whether the refresh should be performed concurrently. Defaults to `false`.
     * @return An instance of `SqlQueryBuilder` with the constructed query.
     * @since 1.0.0
     */
    fun refreshMaterializedView(viewName: String, concurrently: Boolean = false): SqlQueryBuilder {
        type = QueryType.REFRESH_MATERIALIZED_VIEW
        ddlClause += "REFRESH MATERIALIZED VIEW${if (concurrently) " CONCURRENTLY" else String.EMPTY} $viewName$"
        return this
    }

    /**
     * Alters the definition of an existing materialized view in the database.
     *
     * @param viewName The name of the materialized view to alter.
     * @param ifExists A flag indicating whether the query should include the "IF EXISTS" clause to avoid errors 
     *                 if the materialized view does not exist. Defaults to `false`.
     * @param action   The SQL action to be performed on the materialized view, such as renaming or changing its properties.
     * @return Returns an instance of `SqlQueryBuilder` with the constructed ALTER MATERIALIZED VIEW query.
     * @since 1.0.0
     */
    fun alterMaterializedView(viewName: String, ifExists: Boolean = false, @Language("sql") action: String): SqlQueryBuilder {
        type = QueryType.ALTER_MATERIALIZED_VIEW
        ddlClause += "ALTER MATERIALIZED VIEW ${if (ifExists) "IF EXISTS " else String.EMPTY}$viewName $action"
        return this
    }

    /**
     * Drops a materialized view specified by the given view name.
     *
     * This method constructs an SQL query to drop the specified materialized view 
     * and updates the query builder's state.
     *
     * @param viewName The name of the materialized view to be dropped.
     * @param ifExists whether to include the "IF EXISTS" clause, allowing the operation to succeed if the view does not exist
     * @param dropType the type of drop operation to perform (CASCADE, RESTRICT)
     * @return An instance of SqlQueryBuilder with the updated SQL query for dropping the materialized view.
     * @since 1.0.0
     */
    fun dropMaterializedView(viewName: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT): SqlQueryBuilder {
        type = QueryType.DROP_MATERIALIZED_VIEW
        ddlClause += "DROP MATERIALIZED VIEW ${if (ifExists) "IF EXISTS " else String.EMPTY}$viewName"
        ddlClause += " $dropType"
        return this
    }

    // TABLE
    /**
     * Constructs a SQL query to create a new table with the specified name and columns.
     *
     * @param tableName The name of the table to be created.
     * @param body The definition of the columns in the table, including their types and constraints.
     * @return The current instance of SQLQueryBuilder with the CREATE TABLE query appended.
     * @since 1.0.0
     */
    fun createTable(tableName: String, @Language("sql") body: String): SqlQueryBuilder {
        type = QueryType.CREATE_TABLE
        ddlClause += "CREATE TABLE $tableName ($body)"
        return this
    }

    /**
     * Modifies an existing table in the database with the specified alterations.
     *
     * @param tableName the name of the table to be altered
     * @param ifExists whether to include the "IF EXISTS" clause, allowing the operation to succeed if the table does not exist
     * @param alteration the specific alteration or modification to be applied to the table
     * @return the instance of the SQLQueryBuilder for method chaining
     * @since 1.0.0
     */
    fun alterTable(tableName: String, ifExists: Boolean = false, @Language("sql") alteration: String): SqlQueryBuilder {
        type = QueryType.ALTER_TABLE
        ddlClause += "ALTER TABLE${if (ifExists) " IF EXISTS" else String.EMPTY} $tableName $alteration"
        return this
    }

    /**
     * Appends a SQL "DROP TABLE" statement for the given table name to the query builder.
     * This method modifies the internal state of the SQL query builder to include
     * the appropriate "DROP TABLE" clause.
     *
     * @param tableName The name of the table to be dropped.
     * @param ifExists whether to include the "IF EXISTS" clause, allowing the operation to succeed if the table does not exist
     * @param dropType the type of drop operation to perform (CASCADE, RESTRICT)
     * @return The current instance of the SQLQueryBuilder with the updated query.
     * @since 1.0.0
     */
    fun dropTable(tableName: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT): SqlQueryBuilder {
        type = QueryType.DROP_TABLE
        ddlClause += "DROP TABLE${if (ifExists) " IF EXISTS" else String.EMPTY} $tableName"
        return this
    }

    // INDEX
    /**
     * Constructs a SQL query to create an index on a specified table and columns.
     *
     * @param indexName the name of the index to be created
     * @param table the name of the table on which the index will be created
     * @param columns the columns on which the index will be based
     * @return an instance of SQLQueryBuilder with the constructed CREATE INDEX query
     * @since 1.0.0
     */
    fun createIndex(indexName: String, table: String, @Language("sql") columns: String): SqlQueryBuilder {
        type = QueryType.CREATE_INDEX
        ddlClause += "CREATE INDEX $indexName ON $table ($columns)"
        return this
    }

    /**
     * Executes a query to drop an index from a specified table.
     *
     * @param indexName The name of the index to be dropped.
     * @param ifExists whether to include the "IF EXISTS" clause, allowing the operation to succeed if the index does not exist
     * @param tableName The name of the table from which the index will be dropped.
     * @return An instance of SQLQueryBuilder after modifying the query.
     * @since 1.0.0
     */
    fun dropIndex(indexName: String, ifExists: Boolean = false, tableName: String): SqlQueryBuilder {
        type = QueryType.DROP_INDEX
        ddlClause += "DROP INDEX${if (ifExists) " IF EXISTS" else String.EMPTY} $indexName ON $tableName"
        return this
    }

    // SHOW
    /**
     * Constructs an SQL query to display all tables in the database.
     *
     * The method sets the query type to `SHOW_TABLES` and appends the corresponding
     * SQL clause `SHOW TABLES` to the query builder.
     *
     * @return The instance of SQLQueryBuilder with the configured query for showing tables.
     * @since 1.0.0
     */
    fun showTables(): SqlQueryBuilder {
        type = QueryType.SHOW_TABLES
        ddlClause += "SHOW TABLES"
        return this
    }

    /**
     * Appends a SQL 'SHOW CREATE TABLE' statement to the query for the specified table.
     *
     * @param tableName the name of the table for which the 'SHOW CREATE TABLE' statement will be generated
     * @return the modified SQLQueryBuilder instance with the appended 'SHOW CREATE TABLE' statement
     * @since 1.0.0
     */
    fun showTable(tableName: String): SqlQueryBuilder {
        type = QueryType.SHOW_TABLE
        ddlClause += "SHOW CREATE TABLE $tableName"
        return this
    }

    /**
     * Constructs an SQL query to show columns from a specified table.
     *
     * @param tableName the name of the table whose columns are to be listed.
     * @return the SQLQueryBuilder instance with the constructed query.
     * @since 1.0.0
     */
    fun showColumnsFromTable(tableName: String): SqlQueryBuilder {
        type = QueryType.SHOW_COLUMNS_FROM_TABLE
        ddlClause += "SHOW COLUMNS FROM $tableName"
        return this
    }

    /**
     * Constructs a SQL query to display index information for a specified table.
     *
     * @param tableName the name of the table for which to show index details
     * @return the updated instance of SQLQueryBuilder with the query to show indexes from the specified table
     * @since 1.0.0
     */
    fun showIndexFromTable(tableName: String): SqlQueryBuilder {
        type = QueryType.SHOW_INDEX_FROM_TABLE
        ddlClause += "SHOW INDEX FROM $tableName"
        return this
    }

    // TRIGGER
    /**
     * Adds a CREATE TRIGGER statement to the SQL query with the specified trigger name.
     *
     * @param name The name of the trigger to be created.
     * @return The SQLQueryBuilder instance for chaining further SQL query operations.
     * @since 1.0.0
     */
    fun createTrigger(name: String): SqlQueryBuilder {
        type = QueryType.CREATE_TRIGGER
        ddlClause += "CREATE TRIGGER $name"
        return this
    }

    /**
     * Sets the trigger timing to "BEFORE INSERT" for the SQL query builder.
     *
     * @return the updated instance of the SQLQueryBuilder with "BEFORE INSERT" timing.
     * @since 1.0.0
     */
    fun beforeInsert(): SqlQueryBuilder {
        triggerTiming = "BEFORE INSERT"
        return this
    }

    /**
     * Sets the trigger timing to execute after an insert operation.
     *
     * Modifies the internal state of the SQL query builder to indicate
     * the trigger will be activated after a row has been inserted into
     * the table.
     *
     * @return The updated instance of the SQLQueryBuilder for chaining further operations.
     * @since 1.0.0
     */
    fun afterInsert(): SqlQueryBuilder {
        triggerTiming = "AFTER INSERT"
        return this
    }

    /**
     * Sets the trigger timing to "BEFORE UPDATE" for the SQL query being built.
     * This method is typically used to specify that the trigger should fire before
     * an update operation is performed on the database.
     *
     * @return The current instance of the SQLQueryBuilder for method chaining.
     * @since 1.0.0
     */
    fun beforeUpdate(): SqlQueryBuilder {
        triggerTiming = "BEFORE UPDATE"
        return this
    }

    /**
     * Specifies that the trigger should be executed after an update operation.
     *
     * Configures the trigger timing to occur after an update operation in the SQL query builder.
     *
     * @return The current instance of the [SqlQueryBuilder] with the "AFTER UPDATE" trigger timing configured.
     * @since 1.0.0
     */
    fun afterUpdate(): SqlQueryBuilder {
        triggerTiming = "AFTER UPDATE"
        return this
    }

    /**
     * Configures the SQL trigger timing to be executed before a DELETE operation.
     *
     * This method sets the trigger timing context to "BEFORE DELETE" for use
     * in SQL operations, enabling the setup of specific logic to execute prior
     * to the deletion of records.
     *
     * @return the current instance of SQLQueryBuilder with updated trigger timing.
     * @since 1.0.0
     */
    fun beforeDelete(): SqlQueryBuilder {
        triggerTiming = "BEFORE DELETE"
        return this
    }

    /**
     * Sets the trigger timing to "AFTER DELETE" for the query builder.
     *
     * This method is used to configure the SQL query builder to execute
     * a trigger or specific functionality after a DELETE operation.
     *
     * @return the current instance of SQLQueryBuilder with the updated trigger timing.
     * @since 1.0.0
     */
    fun afterDelete(): SqlQueryBuilder {
        triggerTiming = "AFTER DELETE"
        return this
    }

    /**
     * Specifies the database table on which the SQL trigger is to be applied.
     * This method must be called after specifying the trigger timing using methods such as
     * beforeInsert(), afterInsert(), beforeUpdate(), afterUpdate(), beforeDelete(), or afterDelete().
     *
     * @param table the name of the database table to apply the trigger to
     * @return the current instance of the SQLQueryBuilder for chaining further configurations
     * @since 1.0.0
     * @throws UnsupportedOperationException if this method is called before specifying the trigger timing
     */
    fun onTable(@Language("sql") table: String): SqlQueryBuilder {
        triggerTiming.isNotNull() || throw UnsupportedOperationException("You can't call onTable() method before calling beforeInsert(), afterInsert(), beforeUpdate(), afterUpdate(), beforeDelete() or afterDelete() method.")
        triggerTable = table
        return this
    }

    /**
     * Configures the SQL trigger to execute the defined action for each row that the trigger affects.
     *
     * This method ensures that the trigger operates at a per-row level rather than at a statement level.
     * It must be called after specifying the timing of the trigger operation, such as by using
     * `beforeInsert()`, `afterInsert()`, `beforeUpdate()`, `afterUpdate()`, `beforeDelete()`, or `afterDelete()`.
     *
     * @throws UnsupportedOperationException if this method is called without first defining the trigger timing.
     * @return the current instance of `SQLQueryBuilder` for method chaining.
     * @since 1.0.0
     */
    fun forEachRow(): SqlQueryBuilder {
        triggerTiming.isNotNull() || throw UnsupportedOperationException("You can't call forEachRow() method before calling beforeInsert(), afterInsert(), beforeUpdate(), afterUpdate(), beforeDelete() or afterDelete() method.")
        triggerForEachRow = true
        return this
    }

    /**
     * Appends the "BEGIN" clause to the query body. This method is intended
     * to be used only when the current query type is triggering, function,
     * or procedure creation.
     *
     * @throws UnsupportedOperationException if the method is not called within the context of creating a trigger, function, or procedure.
     * @return The current instance of SQLQueryBuilder with the updated query body containing the "BEGIN" clause.
     * @since 1.0.0
     */
    fun begin(): SqlQueryBuilder {
        (type == QueryType.CREATE_TRIGGER
                || type == QueryType.CREATE_FUNCTION
                || type == QueryType.CREATE_PROCEDURE
                ) || throw UnsupportedOperationException("You can't call begin() method if is not called from createTrigger() method.")
        bodyClause += " BEGIN"
        return this
    }

    /**
     * Adds the specified body content to the trigger being constructed.
     * This method is only usable when the query type is set to `CREATE_TRIGGER`.
     * Throws an exception if called in an invalid context.
     *
     * @param body the content to be added to the trigger body
     * @return the current instance of `SQLQueryBuilder` for method chaining
     * @since 1.0.0
     */
    fun addTriggerBody(@Language("sql") body: String): SqlQueryBuilder {
        type == QueryType.CREATE_TRIGGER || throw UnsupportedOperationException("You can't call addTriggerBody() method if is not called from createTrigger() method.")
        bodyClause += " $body"
        return this
    }

    /**
     * Finalizes the SQL query by appending the "END" clause. This method is intended to be used
     * within the context of creating triggers, functions, or procedures.
     * Throws an exception if called outside the correct context.
     *
     * @return The updated instance of SQLQueryBuilder with the "END" clause appended.
     * @since 1.0.0
     */
    fun end(): SqlQueryBuilder {
        (type == QueryType.CREATE_TRIGGER
                || type == QueryType.CREATE_FUNCTION
                || type == QueryType.CREATE_PROCEDURE
                ) || throw UnsupportedOperationException("You can't call end() method if is not called from createTrigger() method.")
        bodyClause += " END"
        return this
    }

    /**
     * Drops a database trigger with the specified name.
     *
     * Constructs and appends a `DROP TRIGGER` statement to the current SQL query.
     *
     * @param name the name of the trigger to be dropped
     * @param ifExists whether to include the "IF EXISTS" clause, allowing the operation to succeed if the trigger does not exist
     * @param dropType the type of drop operation to perform (CASCADE, RESTRICT)
     * @return the current instance of SQLQueryBuilder for method chaining
     * @since 1.0.0
     */
    fun dropTrigger(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT): SqlQueryBuilder {
        type = QueryType.DROP_TRIGGER
        ddlClause += "DROP TRIGGER${if (ifExists) " IF EXISTS" else String.EMPTY} $name"
        ddlClause += " $dropType"
        return this
    }

    // FUNCTION
    /**
     * Constructs a SQL CREATE FUNCTION query with the specified function name and parameters.
     *
     * @param name The name of the function to be created.
     * @param ifNotExists whether to include the "IF NOT EXISTS" clause, allowing the operation to succeed if the function already exists
     * @param params A variable number of parameter strings to define the function's parameters.
     * @return Returns the current instance of SQLQueryBuilder with the CREATE FUNCTION query constructed.
     * @since 1.0.0
     */
    fun createFunction(name: String, ifNotExists: Boolean = false, @Language("sql") vararg params: String): SqlQueryBuilder {
        type = QueryType.CREATE_FUNCTION
        ddlClause += "CREATE FUNCTION${if (ifNotExists) " IF NOT EXISTS" else String.EMPTY} $name"
        functionParams = params.joinToString(", ")
        return this
    }

    /**
     * Specifies the return type for a database function in the SQL query being built.
     * This method should only be called after creating a function using the createFunction() method.
     * If called beforehand, it will throw an UnsupportedOperationException.
     *
     * @param returnType The return type of the SQL function to be set, represented as a string.
     * @return This SQLQueryBuilder instance for method chaining.
     * @since 1.0.0
     */
    fun returns(@Language("sql") returnType: String): SqlQueryBuilder {
        type == QueryType.CREATE_FUNCTION || throw UnsupportedOperationException("You can't call returns() method before calling createFunction() method.")
        functionReturns = returnType
        return this
    }

    /**
     * Adds a function body to an SQL function creation query. This method should only be used
     * after specifying a CREATE FUNCTION query type.
     *
     * @param body the body of the SQL function to be added
     * @return the updated SQLQueryBuilder instance for method chaining
     * @since 1.0.0
     */
    fun addFunctionBody(@Language("sql") body: String): SqlQueryBuilder {
        type == QueryType.CREATE_FUNCTION || throw UnsupportedOperationException("You can't call addFunctionBody() method before calling createFunction() method.")
        bodyClause += " $body"
        return this
    }

    /**
     * Appends a 'DROP FUNCTION' clause to the SQL query being built.
     *
     * This method is used to specify that the query should drop a function with the given name.
     *
     * @param name the name of the function to be dropped
     * @param ifExists whether to include the "IF EXISTS" clause, allowing the operation to succeed if the function already exists
     * @param dropType the type of drop operation to perform (CASCADE, RESTRICT)
     * @return the current instance of SQLQueryBuilder with the updated query
     * @since 1.0.0
     */
    fun dropFunction(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT): SqlQueryBuilder {
        type = QueryType.DROP_FUNCTION
        ddlClause += "DROP FUNCTION ${if (ifExists) "IF EXISTS " else ""}$name"
        ddlClause += " $dropType"
        return this
    }

    // PROCEDURE
    /**
     * Constructs a SQL query for creating a stored procedure with the given name and parameters.
     *
     * @param name The name of the procedure to be created.
     * @param ifNotExists whether to include the "IF NOT EXISTS" clause
     * @param params A variable number of parameters for the procedure.
     * @return The updated SQLQueryBuilder instance with the constructed CREATE PROCEDURE clause.
     * @since 1.0.0
     */
    fun createProcedure(name: String, ifNotExists: Boolean = false, @Language("sql") vararg params: String): SqlQueryBuilder {
        type = QueryType.CREATE_PROCEDURE
        ddlClause += "CREATE PROCEDURE${if (ifNotExists) " IF NOT EXISTS" else String.EMPTY} $name"
        procedureParams = params.joinToString(", ")
        return this
    }

    /**
     * Adds a procedure body to the SQL query being built. This method should only be called
     * after initiating the creation of a stored procedure using the `createProcedure()` method.
     * Throws an exception if used in an invalid context.
     *
     * @param body The SQL procedure body to be added to the query.
     * @return The current instance of `SQLQueryBuilder` to allow for method chaining.
     * @since 1.0.0
     */
    fun addProcedureBody(@Language("sql") body: String): SqlQueryBuilder {
        type == QueryType.CREATE_PROCEDURE || throw UnsupportedOperationException("You can't call addProcedureBody() method before calling createProcedure() method.")
        bodyClause += " $body"
        return this
    }

    /**
     * Configures the query builder to drop a stored procedure with the specified name.
     *
     * @param name the name of the stored procedure to be dropped.
     * @param ifExists whether to include the "IF EXISTS" clause, allowing the operation to succeed if the procedure does not exist
     * @param dropType the type of drop operation to perform (CASCADE, RESTRICT)
     * @return the instance of SQLQueryBuilder with the configured DROP PROCEDURE query.
     * @since 1.0.0
     */
    fun dropProcedure(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT): SqlQueryBuilder {
        type = QueryType.DROP_PROCEDURE
        ddlClause += "DROP PROCEDURE ${if (ifExists) "IF EXISTS " else ""}$name"
        ddlClause += " $dropType"
        return this
    }

    // SEQUENCES
    /**
     * Constructs a SQL query to create a sequence with the specified parameters.
     *
     * @param name The name of the sequence to be created.
     * @param ifNotExists Indicates whether the "IF NOT EXISTS" clause should be included in the SQL query. Defaults to `false`.
     * @param creationContent The SQL content that specifies additional sequence creation parameters.
     * @return An instance of the `SqlQueryBuilder` updated with the create sequence query.
     * @since 1.0.0
     */
    fun createSequenece(name: String, ifNotExists: Boolean = false, @Language("sql") creationContent: String): SqlQueryBuilder {
        type = QueryType.CREATE_SEQUENCE
        ddlClause += "CREATE SEQUENCE${if (ifNotExists) " IF NOT EXISTS" else String.EMPTY} $name $creationContent"
        return this
    }

    /**
     * Alters an existing database sequence with the specified properties.
     *
     * @param name The name of the sequence to be altered.
     * @param ifExists Determines whether the clause `IF EXISTS` should be included in the statement. Defaults to `false`.
     * @param alterationContent The SQL content specifying the alterations to be applied to the sequence.
     * @return Returns an instance of `SqlQueryBuilder` with the altered sequence SQL statement.
     * @since 1.0.0
     */
    fun alterSequenece(name: String, ifExists: Boolean = false, @Language("sql") alterationContent: String): SqlQueryBuilder {
        type = QueryType.ALTER_SEQUENCE
        ddlClause += "ALTER SEQUENCE${if (ifExists) " IF EXISTS" else String.EMPTY} $name $alterationContent"
        return this
    }

    /**
     * Drops a database sequence with the specified name.
     *
     * @param name The name of the sequence to drop.
     * @param ifExists A flag indicating whether the command should include the `IF EXISTS` clause. Defaults to `false`.
     * @param dropType the type of drop operation to perform (CASCADE, RESTRICT)
     * @return The current instance of `SqlQueryBuilder` with the drop sequence clause appended.
     * @since 1.0.0
     */
    fun dropSequence(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT): SqlQueryBuilder {
        type = QueryType.DROP_SEQUENCE
        ddlClause += "DROP SEQUENCE${if (ifExists) " IF EXISTS" else String.EMPTY} $name"
        ddlClause += " $dropType"
        return this
    }

    // SCHEMA
    /**
     * Constructs a SQL query to create a database schema with optional parameters
     * for existence check and authorization.
     *
     * @param name The name of the schema to be created.
     * @param ifNotExists A flag indicating whether to include the "IF NOT EXISTS" clause. Defaults to false.
     * @param authorization An optional authorization identifier for the schema.
     * @return An instance of `SqlQueryBuilder` containing the constructed SQL query.
     * @since 1.0.0
     */
    fun createSchema(name: String, ifNotExists: Boolean = false, authorization: String? = null): SqlQueryBuilder {
        type = QueryType.CREATE_SCHEMA
        ddlClause += "CREATE SCHEMA${if (ifNotExists) " IF NOT EXISTS" else String.EMPTY} $name"
        if (authorization.isNotNull()) ddlClause += "AUTHORIZATION $authorization"
        return this
    }

    /**
     * Modifies the definition of an existing database schema.
     *
     * @param name The name of the schema to be altered.
     * @param ifExists Indicates whether to include the "IF EXISTS" clause in the query. Default is false.
     * @param authorization Optional parameter specifying the new authorization role for the schema.
     * @param rename Optional parameter specifying the new name for the schema.
     * @return An instance of [SqlQueryBuilder] representing the constructed query.
     * @since 1.0.0
     */
    fun alterSchema(name: String, ifExists: Boolean = false, authorization: String? = null, rename: String? = null): SqlQueryBuilder {
        type = QueryType.ALTER_SCHEMA
        ddlClause += "ALTER SCHEMA ${if (ifExists) "IF EXISTS " else String.EMPTY}$name"
        if (authorization.isNotNull()) ddlClause += " AUTHORIZATION $authorization"
        if (rename.isNotNull()) ddlClause += " RENAME TO $rename"
        return this
    }

    /**
     * Constructs a SQL query to drop a schema.
     *
     * @param name The name of the schema to be dropped.
     * @param ifExists Indicates whether to include the "IF EXISTS" clause in the query. Default is false.
     * @param dropType the type of drop operation to perform (CASCADE, RESTRICT)
     * @return An instance of SqlQueryBuilder with the constructed DROP SCHEMA query.
     * @since 1.0.0
     */
    fun dropSchema(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT): SqlQueryBuilder {
        type = QueryType.DROP_SCHEMA
        ddlClause += "DROP SCHEMA${if (ifExists) " IF EXISTS" else String.EMPTY} $name"
        ddlClause += " $dropType"
        return this
    }

    /**
     * Configures the schema search path for the SQL query builder.
     *
     * @param searchPath The search path to be set, specified as a SQL-compliant string.
     * @param schemas A variable number of schema names to be included in the search path.
     * @return The current instance of SqlQueryBuilder with the updated schema configuration.
     * @since 1.0.0
     */
    fun setSchema(@Language("sql") searchPath: String, vararg schemas: String): SqlQueryBuilder {
        type = QueryType.SET_SCHEMA
        setSchemaClause += "SET $searchPath TO ${schemas.joinToString()}"
        return this
    }
    
    // TYPE
    
    /**
     * Constructs a SQL query to create a new ENUM type in the database.
     *
     * @param name The name of the ENUM type to be created.
     * @param entries The ENUM values to be included in the type.
     * @return The updated SqlQueryBuilder instance with the constructed CREATE TYPE query.
     * @since 1.0.0
     */
    fun createType(name: String, vararg entries: String): SqlQueryBuilder {
        type = QueryType.CREATE_TYPE
        ddlClause += "CREATE TYPE $name AS ENUM (${entries.joinToString { "'$it'" }})"
        return this
    }
    
    /**
     * Creates a composite type in the underlying database.
     *
     * This method allows the definition of a composite type with a specified name and columns.
     *
     * @param name The name of the composite type to be created.
     * @param columns The column definitions for the composite type, written as SQL.
     * @return This instance of `SqlQueryBuilder` for method chaining.
     * @since 1.0.0
     */
    fun createCompositeType(name: String, @Language("sql") vararg columns: String): SqlQueryBuilder {
        type = QueryType.CREATE_TYPE
        ddlClause += "CREATE TYPE $name AS (${columns.joinToString()})"
        return this
    }
    
    /**
     * Appends an SQL statement to alter a database type by adding new entries to it.
     *
     * @param typeName the name of the database type to which entries will be added.
     * @param entries the entries to be added to the specified type.
     * @return the current instance of [SqlQueryBuilder] with the "ALTER TYPE" clause appended.
     * @since 1.0.0
     */
    fun addEntriesToType(typeName: String, vararg entries: String): SqlQueryBuilder {
        type = QueryType.ALTER_TYPE
        ddlClause += "ALTER TYPE $typeName ADD VALUE (${entries.joinToString { "'$it'" }})"
        return this
    }

    /**
     * Adds a new entry to a specified type before an existing entry.
     *
     * This method modifies the given type by adding a new entry before another specified entry. 
     * It builds an SQL query for altering a type in the database.
     *
     * @param typeName the name of the type to be modified
     * @param entry the new entry to be added to the type
     * @param otherEntry the existing entry before which the new entry will be added
     * @return an instance of SqlQueryBuilder with the updated modification query
     * @since 1.0.0
     */
    fun addEntryBeforeOtherToType(typeName: String, entry: String, otherEntry: String): SqlQueryBuilder {
        type = QueryType.ALTER_TYPE
        ddlClause += "ALTER TYPE $typeName ADD VALUE '$entry' BEFORE $otherEntry"
        return this
    }

    /**
     * Adds a new entry to the specified type in the database schema. 
     * Optionally checks if the entry already exists before adding it.
     *
     * @param typeName the name of the type to which the entry will be added
     * @param entry the value of the new entry to be added
     * @param ifNotExists a flag indicating whether to add the entry only if it does not already exist (default is false)
     * @return the updated SqlQueryBuilder instance with the generated DDL clause
     * @since 1.0.0
     */
    fun addEntryToType(typeName: String, entry: String, ifNotExists: Boolean = false): SqlQueryBuilder { 
        type = QueryType.ALTER_TYPE
        ddlClause += "ALTER TYPE $typeName ADD VALUE IF NOT EXISTS '$entry'"
        return this
    }
    
    /**
     * Renames an entry in a database type by altering the specified type name and renaming the value
     * from a given old name to a new name.
     *
     * @param typeName The name of the database type whose entry is being renamed.
     * @param names A pair of strings where the first element is the existing entry name, and the second
     *              element is the new name for the entry.
     * @return An updated instance of the SqlQueryBuilder with the rename operation appended.
     * @since 1.0.0
     */
    fun renameEntryInType(typeName: String, names: String2): SqlQueryBuilder {
        type = QueryType.ALTER_TYPE
        ddlClause += "ALTER TYPE $typeName RENAME VALUE ${names.first} TO ${names.second}"
        return this
    }
    
    /**
     * Alters the name of an existing database type.
     *
     * @param typeName The current name of the database type to be renamed.
     * @param newName The new name to assign to the database type.
     * @return An instance of SqlQueryBuilder with the updated SQL query for renaming the type.
     * @since 1.0.0
     */
    fun renameType(typeName: String, newName: String): SqlQueryBuilder {
        type = QueryType.ALTER_TYPE
        ddlClause += "ALTER TYPE $typeName RENAME TO $newName"
        return this
    }

    /**
     * Builds a SQL query to drop a specified user-defined type from the database.
     *
     * @param name The name of the type to drop.
     * @param ifExists A boolean flag indicating whether to add the "IF EXISTS" clause
     *                 to the query. Defaults to false.
     * @param dropType Specifies the type of drop behavior, such as RESTRICT or CASCADE.
     *                 Defaults to DropType.RESTRICT.
     * @return Returns the updated instance of the SqlQueryBuilder with the constructed
     *         DROP TYPE statement appended to the query.
     * @since 1.0.0
     */
    fun dropType(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT): SqlQueryBuilder {
        type = QueryType.DROP_TYPE
        ddlClause += "DROP TYPE ${if (ifExists) "IF EXISTS " else ""}$name"
        ddlClause += " $dropType"
        return this
    }

    // DOMAIN

    /**
     * Creates a SQL domain with the specified name, data type, and a check constraint.
     *
     * @param name The name of the domain to be created.
     * @param as The data type of the domain.
     * @param check The check constraint to be applied on the domain.
     * @return An instance of SqlQueryBuilder with the CREATE DOMAIN query appended.
     * @since 1.0.0
     */
    fun createDomain(name: String, @Language("sql") `as`: String, @Language("sql") check: String): SqlQueryBuilder {
        type = QueryType.CREATE_DOMAIN
        ddlClause += "CREATE DOMAIN $name AS $`as`"
        ddlClause += " CHECK ($check)"
        return this
    }

    /**
     * Modifies the definition of an existing domain by applying the specified SQL operation.
     *
     * @param name the name of the domain to be altered
     * @param ifExists Indicates whether to include the "IF EXISTS" clause in the query. Default is false.
     * @param operation the SQL operation to be applied to the domain, specified as a string
     * @return a reference to the updated SqlQueryBuilder instance
     * @since 1.0.0
     */
    fun alterDomain(name: String, ifExists: Boolean = false, @Language("sql") operation: String): SqlQueryBuilder {
        type = QueryType.ALTER_DOMAIN
        ddlClause += "ALTER DOMAIN ${if (ifExists) "IF EXISTS " else String.EMPTY}$name $operation"
        return this
    }
    
    /**
     * Drops a domain from the database with optional constraints.
     *
     * @param name The name of the domain to drop.
     * @param ifExists Indicates whether to include the `IF EXISTS` clause to avoid errors if the domain does not exist. Defaults to `false`.
     * @param dropType Specifies the drop type, such as `CASCADE` or `RESTRICT`. Defaults to `DropType.RESTRICT`.
     * @return An instance of `SqlQueryBuilder` representing the drop domain query.
     * @since 1.0.0
     */
    fun dropDomain(name: String, ifExists: Boolean = false, dropType: DropType = DropType.RESTRICT): SqlQueryBuilder {
        type = QueryType.DROP_DOMAIN
        ddlClause += "DROP DOMAIN ${if (ifExists) "IF EXISTS " else ""}$name"
        ddlClause += " $dropType"
        return this
    }

    // BUILD
    /**
     * Constructs and returns a query or DDL (Data Definition Language) statement based on the type of query.
     * The method dynamically builds and formats the query string depending on the `type` parameter,
     * which defines the query type (e.g., SELECT, INSERT, UPDATE, DELETE, CREATE_TRIGGER, CREATE_FUNCTION, or CREATE_PROCEDURE).
     *
     * For each query type:
     * - **QueryType.SELECT**: Builds a complete SELECT query including clauses like SELECT, FROM, JOIN, WHERE, GROUP BY, HAVING, ORDER BY, LIMIT, and OFFSET.
     * - **QueryType.INSERT**: Constructs an INSERT statement with columns and corresponding values.
     * - **QueryType.UPDATE**: Builds an UPDATE query with SET and WHERE clauses.
     * - **QueryType.DELETE**: Builds a DELETE statement with a WHERE clause.
     * - **QueryType.CREATE_TRIGGER**: Constructs a statement to define a database trigger, including its timing, table, and body.
     * - **QueryType.CREATE_FUNCTION**: Builds a CREATE FUNCTION statement, including parameters, return type, and the function body.
     * - **QueryType.CREATE_PROCEDURE**: Constructs a CREATE PROCEDURE statement with parameters and body.
     * - For other query types, returns the DDL clause string directly.
     *
     * The resulting string is constructed using `StringBuilder` to efficiently concatenate the parts of the query or DDL statement.
     *
     * @return A fully constructed query or DDL statement as a string.
     *
     * @since 1.0.0
     */
    fun peek() = when(type) {
        QueryType.SELECT -> buildString {
            append(selectClause)
            append(fromClause)
            append(joinClause.joinToString(String.EMPTY))
            append(whereClause)
            append(groupByClause)
            append(havingClause)
            append(orderByClause)
            append(limitClause)
            append(offsetClause)
        }
        QueryType.INSERT -> buildString {
            append(insertClause)
            append(" (")
            append(insertColumns.joinToString(", "))
            append(") VALUES (")
            append(formatValue(insertValues))
            append(")")
        }
        QueryType.UPDATE -> buildString {
            append(updateClause)
            append(setClause)
            append(whereClause)
        }
        QueryType.DELETE -> buildString {
            append(deleteClause)
            append(whereClause)
        }
        QueryType.CREATE_TRIGGER -> buildString {
            append(ddlClause)
            append(Char.SPACE)
            append(triggerTiming)
            append(" ON ")
            append(triggerTable)
            if (triggerForEachRow) append(" FOR EACH ROW")
            append(bodyClause)
        }
        QueryType.CREATE_FUNCTION -> buildString {
            append(ddlClause)
            append(Char.SPACE)
            append(functionParams)
            append("RETURNS ")
            append(functionReturns)
            append(bodyClause)
        }
        QueryType.CREATE_PROCEDURE -> buildString {
            append(ddlClause)
            append(Char.SPACE)
            append(procedureParams)
            append(bodyClause)
        }
        else -> ddlClause.toString()
    }

    /**
     * Builds and returns a [SqlQuery] object based on the current query type and its related clauses or parameters.
     *
     * The method processes different types of SQL queries such as SELECT, INSERT, UPDATE, DELETE,
     * and DDL operations (e.g., CREATE TRIGGER, CREATE FUNCTION, CREATE PROCEDURE) based on the
     * specified query type. Each query type assembles a corresponding SQL statement by combining
     * the relevant clauses, parameters, or elements.
     *
     * @return A [SqlQuery] instance containing the SQL query string for the specified query type.
     * @since 1.0.0
     */
    fun build() = SqlQuery(peek())

    /**
     * Builds the current object and converts it to a code representation.
     * This function combines the build operation with the transformation
     * of the built object into its code equivalent, effectively generating
     * a code representation in a single call.
     *
     * @return The code representation of the built object.
     * @since 1.0.0
     */
    fun buildAsCode() = build().toCode()

    /**
     * Formats a collection of values into a single string, joining each formatted value with a comma.
     *
     * @param values the collection of values to be formatted; each value must not be null.
     * @return a string containing the formatted values, separated by commas.
     * @since 1.0.0
     */
    private fun formatValues(values: Collection<*>): String {
        val formatted = emptyMList<String>()
        for (v in values) formatted += formatValue(v!!)
        return formatted.joinToString(", ")
    }

    /**
     * Formats the given value into a string representation. If the value is a number, it returns
     * the string value of the number. Otherwise, it converts the value to a string, escaping
     * single quotes by doubling them, and wraps the result in single quotes.
     *
     * @param v the value to format, can be of any type
     * @since 1.0.0
     */
    private fun formatValue(v: Any) = when (v) {
        is Number -> v.toString()
        else -> "'" + v.toString().replace("'", "''") + "'"
    }

    /**
     * Represents the various types of queries that can be executed in a database system.
     * Each query type corresponds to a specific operation or command that interacts with
     * the database schema, data, or structures.
     *
     * @since 1.0.0
     */
    enum class QueryType {
        /**
         * Represents a query type used to retrieve data from a database.
         * Typically associated with SELECT statements in SQL, which are utilized
         * to fetch specific columns or rows from one or more tables based on defined conditions.
         *
         * Commonly used in data retrieval operations requiring filtering,
         * sorting, and joining of datasets.
         *
         * @since 1.0.0
         */
        SELECT,
        /**
         * Represents the INSERT query type in the SQL language.
         * This type is used to add new rows to a table.
         *
         * @since 1.0.0
         */
        INSERT,
        /**
         * Represents an UPDATE operation in the query type enumeration.
         *
         * This query type is used to modify existing records in a database table.
         *
         * @since 1.0.0
         */
        UPDATE,
        /**
         * Represents a DELETE operation in the context of database queries.
         * This query type is used to delete records from a table.
         *
         * @since 1.0.0
         */
        DELETE,
        /**
         * Represents the TRUNCATE operation, typically used for removing all rows from a table
         * in database operations while maintaining the structure of the table.
         * This operation is more efficient than a DELETE without a WHERE clause
         * as it does not log individual row deletions.
         *
         * @since 1.0.0
         */
        TRUNCATE,
        /**
         * Represents the query type for creating a view in a database.
         *
         * This query type is used when defining and saving a database query
         * as a virtual table, which can then be referenced in subsequent queries.
         *
         * @since 1.0.0
         */
        CREATE_VIEW,
        /**
         * Represents the query type for altering an existing database view.
         * This operation allows modifications to the definition of a database view.
         *
         * @since 1.0.0
         */
        ALTER_VIEW,
        /**
         * Represents a query type for dropping a view in a database.
         *
         * Queries of this type are used to remove an existing database view.
         *
         * @since 1.0.0
         */
        DROP_VIEW,
        /**
         * Represents the type of SQL command used to create a materialized view in a database.
         *
         * A materialized view is a database object that contains the results of a query and can
         * be updated periodically via refresh operations. It is used to improve performance
         * for queries that are expensive to compute in real time.
         *
         * This symbol is part of the `QueryType` enumeration and is used to indicate
         * the intention to create such a view while interacting with the database.
         *
         * @since 1.0.0
         */
        CREATE_MATERIALIZED_VIEW,
        /**
         * Enum value that represents an operation to refresh a materialized view in the database.
         * This is typically used to trigger updates on materialized views to ensure they reflect
         * the latest data from the original tables.
         *
         * This type can be applied when querying or interfacing with database operations that
         * involve materialized views.
         *
         * @since 1.0.0
         */
        REFRESH_MATERIALIZED_VIEW,
        /**
         * Represents an operation type for altering an existing materialized view
         * within a database schema. This typically includes modifying the structure,
         * refresh options, or other properties of the materialized view without
         * dropping and recreating it.
         *
         * This query type is commonly associated with commands in database systems that
         * support materialized views, allowing flexible configuration and optimization
         * of data storage and retrieval.
         *
         * @since 1.0.0
         */
        ALTER_MATERIALIZED_VIEW,
        /**
         * Represents an enumeration constant for dropping a materialized view in the context
         * of database query types. This is typically used to signify the operation of
         * removing a materialized view from a database schema.
         *
         * @since 1.0.0
         */
        DROP_MATERIALIZED_VIEW,
        /**
         * Represents the database operation to create a new schema. This operation allows 
         * defining a namespace or organizational structure for a collection of tables, views, 
         * and other database objects.
         *
         * This query type is typically used when setting up a new database or organizing 
         * the database structure into logical groupings.
         *
         * @since 1.0.0
         */
        CREATE_SCHEMA,
        /**
         * Represents an enumerated value used to specify operations related to schema alteration
         * within the context of a database query. This value is part of the QueryType class
         * and is utilized to identify or handle schema modification tasks such as altering
         * table structures or database properties.
         *
         * @since 1.0.0
         */
        ALTER_SCHEMA,
        /**
         * Represents an operation for dropping a database schema.
         *
         * This type is used to signify the `DROP SCHEMA` query in database manipulation. 
         * It is typically utilized in contexts where schema removal from a database is needed.
         *
         * @since 1.0.0
         */
        DROP_SCHEMA,
        /**
         * Represents a query type for generating a sequence of data or items.
         * This query type is primarily used in database operations or other scenarios 
         * where sequential data generation is required.
         *
         * @since 1.0.0
         */
        CREATE_SEQUENCE,
        /**
         * Represents a specific query type that alters the properties of a database sequence.
         * This type is typically used when modifying the behavior or structure of an existing sequence,
         * such as changing increment values, bounds, or reset options.
         *
         * This query type is often part of a larger set of query operations aimed at 
         * manipulating database schema objects programmatically.
         *
         * @since 1.0.0
         */
        ALTER_SEQUENCE,
        /**
         * Represents a query type used to drop a sequence in a database.
         *
         * This query type is typically utilized in database management operations
         * where a sequence object is no longer needed and should be removed.
         *
         * @since 1.0.0
         */
        DROP_SEQUENCE,
        /**
         * Represents a query type for creating an index in a database.
         *
         * This enum constant is part of the QueryType enumeration and is used to define
         * operations that create indexes to optimize database query performance.
         *
         * @since 1.0.0
         */
        CREATE_INDEX,
        /**
         * Represents the operation for removing an existing index from a table or view.
         *
         * This is used to improve performance by dropping unnecessary or obsolete indexes,
         * which can also save storage space in the database.
         *
         * @since 1.0.0
         */
        DROP_INDEX,
        /**
         * Represents the SQL operation for creating a database trigger.
         *
         * Triggers are database objects that automatically execute specified
         * actions in response to certain events on a table or view.
         *
         * This query type is typically used to define triggers that are
         * executed before or after an INSERT, UPDATE, or DELETE operation.
         *
         * @since 1.0.0
         */
        CREATE_TRIGGER,
        /**
         * Represents the `DROP TRIGGER` query type, which is used to remove an existing trigger
         * from the database. A trigger is a database object automatically executed or fired
         * when certain events occur in a table.
         *
         * @since 1.0.0
         */
        DROP_TRIGGER,
        /**
         * Represents the SQL operation for creating a user-defined function in a database.
         *
         * This type is part of the `QueryType` enumeration and is used to specify
         * queries related to the creation of functions within a database schema.
         *
         * Typical usage may include defining reusable routines or logic
         * that can be invoked as part of database queries or operations.
         *
         * @since 1.0.0
         */
        CREATE_FUNCTION,
        /**
         * Represents a query type for dropping a function in a database.
         *
         * This query type is used to remove or delete a user-defined function from the database.
         *
         * @since 1.0.0
         */
        DROP_FUNCTION,
        /**
         * Represents the query type for creating a stored procedure in a database system.
         *
         * This query type is used to define and register a procedural routine in the database,
         * allowing encapsulation of complex operations and logic that can be reused.
         *
         * Typically, a `CREATE PROCEDURE` statement specifies input and output parameters,
         * procedural flow, and associated SQL statements, enabling execution of stored procedures
         * with dynamic input values.
         *
         * @since 1.0.0
         */
        CREATE_PROCEDURE,
        /**
         * Represents a query type used to drop an existing procedure in the database.
         *
         * This query type is used when there is a need to remove a stored procedure.
         *
         * @since 1.0.0
         */
        DROP_PROCEDURE,
        /**
         * Represents the type of column creation within a query.
         * This can be used to define the structure and data type
         * of columns when creating or modifying database tables.
         *
         * @since 1.0.0
         */
        CREATE_TYPE, // type of columns
        /**
         * Represents the type of column modification in a database query.
         *
         * This can be used to alter the structure and data type
         * of columns when creating or modifying database tables.
         *
         * @since 1.0.0
         */
        ALTER_TYPE, // type of columns
        /**
         * Drop a custom type of column.
         *
         * @since 1.0.0
         */
        DROP_TYPE, // type of columns
        /**
         * Represents a constant used for specifying the operation of creating a domain.
         * Typically associated with the management or configuration of domain-related entities.
         *
         * @since 1.0.0
         */
        CREATE_DOMAIN,
        /**
         * Represents an operation to modify an existing domain in the database schema.
         * This operation is typically used to alter the definition of a domain 
         * by changing constraints, data types, or other properties associated with it.
         *
         * This symbol is part of the {@link QueryType} class and serves as an enumeration 
         * value to identify the specific query type related to altering domains.
         *
         * @since 1.0.0
         */
        ALTER_DOMAIN,
        /**
         * Represents a specific SQL query type used to drop a domain.
         *
         * This query type is typically used in the context of database schema management
         * when there is a need to remove a domain definition from the database.
         *
         * It is essential to ensure that the domain to be dropped is not in use before
         * executing this query type to avoid integrity or dependency issues.
         *
         * @since 1.0.0
         */
        DROP_DOMAIN,
        /**
         * Represents the creation of a new database table.
         *
         * This query type is used to define the structure of a table,
         * including its columns, data types, constraints, and other properties
         * that determine how data is stored and accessed.
         *
         * @since 1.0.0
         */
        CREATE_TABLE,
        /**
         * Represents a query type for altering the structure of an existing table in the database.
         *
         * This query type is typically used to add, modify, or delete table columns,
         * as well as to apply other structural changes to the table.
         *
         * @since 1.0.0
         */
        ALTER_TABLE,
        /**
         * Represents an operation to drop an existing table from the database.
         *
         * This query type is used to delete a table along with all of its data, indexes, constraints,
         * and triggers permanently from the schema. After execution, the table becomes unavailable
         * unless recreated.
         *
         * Use with caution as it results in irreversible data loss.
         *
         * @since 1.0.0
         */
        DROP_TABLE,
        /**
         * Represents the query type for listing all tables in a database.
         *
         * This query type is typically used to retrieve a list of available tables
         * within the connected database schema.
         *
         * It is part of the QueryType enumeration and provides functionality for
         * metadata inspection of the database structure.
         *
         * @since 1.0.0
         */
        SHOW_TABLES,
        /**
         * Represents a query type for displaying details about a specific table in a database.
         * Typically used to retrieve information such as the table structure, columns, or configuration details.
         * Part of the QueryType enumeration, which categorizes different SQL query operations.
         *
         * @since 1.0.0
         */
        SHOW_TABLE,
        /**
         * Represents a query type for retrieving the columns of a specific table.
         * Used to describe metadata-related operations for a database table.
         *
         * @since 1.0.0
         */
        SHOW_COLUMNS_FROM_TABLE,
        /**
         * Represents the query type for showing index metadata from a table in the database.
         * This query type is typically used to retrieve information about indexes in a specified table,
         * including index name, column list, uniqueness, and other related details.
         *
         * This is part of the QueryType enumeration, which categorizes various types of SQL queries.
         *
         * @since 1.0.0
         */
        SHOW_INDEX_FROM_TABLE,
        /**
         * Represents a specific schema definition used within the context of database queries.
         *
         * This class provides the foundational structure and operations to define or modify
         * the underlying schema for executing structured queries in a database system.
         *
         * Instances of this class are primarily used to ensure conformity to certain configurations
         * or to alter predefined database schema rules dynamically during runtime.
         *
         * @since 1.0.0
         */
        SET_SCHEMA
    }

    /**
     * Represents the types of drop operations that can be performed in the context of database schema management
     * or similar operations where constraints or dependencies might exist.
     *
     * @since 1.0.0
     * @author Tommaso Pastoreli
     */
    enum class DropType {
        /**
         * Represents a drop type where the removal of an entity will also
         * result in the removal of all dependent entities.
         *
         * This is typically used in contexts where cascading deletions are needed,
         * ensuring that related entities are cleaned up automatically.
         *
         * @since 1.0.0
         */
        CASCADE,
        /**
         * Represents a restriction type used in operations where cascading effects are not allowed.
         * Typically utilized to enforce constraints during deletion or modification processes.
         *
         * @since 1.0.0
         */
        RESTRICT
    }
    
    /**
     * Represents different types of SQL join operations that can be performed between database tables.
     *
     * This enum provides a set of constants for specifying the type of join operation utilized
     * during database queries. Each type corresponds to a specific SQL join variant, allowing
     * for flexible table merging and result customization.
     *
     * @since 1.0.0
     */
    @Suppress("SqlNoDataSourceInspection", "unused")
    enum class JoinType(@param:Language("sql") val sqlKeyWord: String) {
        /**
         * Represents an inner join type in SQL, where only the matching rows from the joined tables
         * are included in the result set.
         *
         * This is one of the most commonly used join types in relational database queries.
         *
         * @since 1.0.0
         */
        INNER("INNER"),
        /**
         * Represents a LEFT OUTER join type, where all records from the left table
         * are included in the result set, along with matching records from the
         * right table. If no matches exist, NULL values are included for the right
         * table's columns.
         *
         * @since 1.0.0
         */
        LEFT_OUTER("LEFT"),
        /**
         * Represents a right outer join type in a database query.
         * A right outer join retrieves all records from the right table
         * and the matched records from the left table. If there is no match,
         * the result will contain nulls for columns from the left table.
         *
         * @since 1.0.0
         */
        RIGHT_OUTER("RIGHT"),
        /**
         * Represents a full outer join type in a database join operation.
         * A full outer join returns all records when there is a match in either
         * the left or right table. Non-matching rows will have nulls in place
         * for the missing data.
         *
         * Typically used in queries where a complete dataset is needed, including
         * all matches from both sides and any unmatched rows.
         *
         * @since 1.0.0
         */
        FULL_OUTER("FULL"),
        /**
         * Represents a CROSS join type in database operations.
         *
         * A CROSS join generates a Cartesian product of two tables, combining
         * each row of the first table with each row of the second table.
         * Typically used when no matching condition exists between two tables.
         *
         * This join type may potentially result in large datasets, as it
         * produces all possible combinations of rows.
         *
         * @since 1.0.0
         */
        CROSS("CROSS"),
        /**
         * Represents a join type in which a table is joined with itself.
         *
         * This join type is commonly used when there is a need to compare rows within the same table.
         *
         * @since 1.0.0
         */
        SELF("SELF"),
        /**
         * This class represents the "NATURAL" join type used in relational database operations.
         * It is typically used to denote a natural join, a type of SQL join that automatically matches
         * columns between two tables based on identical column names without explicitly specifying
         * the columns in the join condition.
         *
         * @since 1.0.0
         */
        NATURAL("NATURAL")
    }
    
    /**
     * Represents logical operators used in boolean logic operations.
     *
     * This enum defines two operators:
     * - `AND`: Represents logical conjunction where the result is true if both operands are true.
     * - `OR`: Represents logical disjunction where the result is true if at least one of the operands is true.
     *
     * @since 1.0.0
     */
    enum class LogicOperator {
        AND, OR
    }
}

/**
 * Creates a new instance of [SqlQueryBuilder] to construct SQL query objects.
 *
 * This method provides a convenient way to initialize a builder for creating and customizing
 * instances of [SqlQuery]. The builder ensures flexibility and a fluent interface
 * for constructing queries.
 *
 * @receiver The companion object of [SqlQuery].
 * @return A new [SqlQueryBuilder] instance for constructing SQL query objects.
 * @since 1.0.0
 */
fun SqlQuery.Companion.builder() = SqlQueryBuilder()

/**
 * Constructs an SQL query using the provided builder action.
 *
 * This method initializes an SQLQueryBuilder, applies the given builder action to it,
 * and then constructs the query code.
 *
 * @param builderAction A lambda that defines how the SQLQueryBuilder should construct the query.
 * @return A constructed SQL query.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
fun buildSqlQuery(builderAction: ReceiverConsumer<SqlQueryBuilder>): SqlQuery {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return SqlQueryBuilder().apply(builderAction).build()
}

/**
 * Constructs an SQL query by applying the specified action to the provided SQLQueryBuilder.
 *
 * @param builder The SQLQueryBuilder instance used to build the SQL query.
 * @param builderAction A lambda function that defines modifications and configurations to be applied to the SQLQueryBuilder.
 * @return An object representing the built SQL query.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
fun buildSqlQuery(builder: SqlQueryBuilder, builderAction: ReceiverConsumer<SqlQueryBuilder>): SqlQuery {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return builder.apply(builderAction).build()
}