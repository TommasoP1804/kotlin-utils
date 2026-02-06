package dev.tommasop1804.kutils.classes.coding

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.AnyList
import dev.tommasop1804.kutils.COLON
import dev.tommasop1804.kutils.DataMap
import dev.tommasop1804.kutils.MList
import dev.tommasop1804.kutils.StringList
import dev.tommasop1804.kutils.ThrowableTransformer
import dev.tommasop1804.kutils.exceptions.*
import dev.tommasop1804.kutils.invoke
import dev.tommasop1804.kutils.minus
import dev.tommasop1804.kutils.startsWith
import dev.tommasop1804.kutils.startsWithIgnoreCase
import dev.tommasop1804.kutils.then
import dev.tommasop1804.kutils.tryOrThrow
import dev.tommasop1804.kutils.tryTrueOrFalse
import dev.tommasop1804.kutils.validate
import jakarta.persistence.AttributeConverter
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.NonUniqueResultException
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import org.intellij.lang.annotations.Language
import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import kotlin.reflect.KClass
import kotlin.text.endsWith

/**
 * A value class representing an SQL query string with type-safe construction and validation.
 *
 * This class provides a lightweight wrapper around SQL query strings, ensuring compile-time
 * safety and runtime validation while avoiding the overhead of traditional wrapper classes
 * through Kotlin's value class mechanism.
 *
 * @property value The raw SQL query string
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused", "SqlNoDataSourceInspection", "SqlSourceToSinkFlow", "UNCHECKED_CAST", "functionName")
@JsonSerialize(using = SqlQuery.Companion.Serializer::class)
@JsonDeserialize(using = SqlQuery.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = SqlQuery.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = SqlQuery.Companion.OldDeserializer::class)
class SqlQuery(@param:Language("sql") override val value: String): CharSequence, Code(value, dev.tommasop1804.kutils.classes.coding.Language.SQL) {
    /**
     * Validates that the query does not contain obvious SQL injection patterns.
     *
     * @return true if the query appears safe, false otherwise
     * @since 1.0.0
     */
    val isSafe: Boolean = run {
        val dangerousPatterns = listOf(
            Regex("--;", RegexOption.IGNORE_CASE),
            Regex("/\\*.*?\\*/", RegexOption.IGNORE_CASE),
            Regex("xp_", RegexOption.IGNORE_CASE),
            Regex("sp_", RegexOption.IGNORE_CASE)
        )
        dangerousPatterns.none { it.containsMatchIn(value) }
    }
    /**
     * Returns the query type (SELECT, INSERT, UPDATE, DELETE, etc.).
     *
     * @return The query type in uppercase, or `null` if not recognized
     * @since 1.0.0
     */
    val type = run {
        val trimmed = value.trim()
        when {
            trimmed startsWithIgnoreCase "SELECT" -> Type.SELECT
            trimmed startsWithIgnoreCase "INSERT" -> Type.INSERT
            trimmed startsWithIgnoreCase "UPDATE" -> Type.UPDATE
            trimmed startsWithIgnoreCase "DELETE" -> Type.DELETE
            trimmed startsWithIgnoreCase "CREATE" -> Type.CREATE
            trimmed startsWithIgnoreCase "DROP" -> Type.DROP
            trimmed startsWithIgnoreCase "ALTER" -> Type.ALTER
            trimmed startsWithIgnoreCase "TRUNCATE" -> Type.TRUNCATE
            else -> null
        }
    }
    
    /**
     * Indicates whether the SQL query contains a "LIMIT" clause.
     *
     * The presence of the "LIMIT" keyword is determined using a case-insensitive regular expression
     * match within the SQL query string.
     *
     * This property is useful for identifying queries that limit the number of results returned.
     *
     * @since 1.0.0
     */
    val hasLimit = Regex("\\bLIMIT\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)
    
    /**
     * Indicates whether the SQL query represented by this instance includes the keyword "DISTINCT".
     * The check is case-insensitive and determines if the keyword "DISTINCT" is present as a standalone word.
     *
     * This property is useful for determining whether the SQL query is intended to return distinct rows.
     *
     * @since 1.0.0
     */
    val isDistinct = Regex("\\bDISTINCT\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)
    
    /**
     * Indicates whether the SQL query contains the keyword "COUNT" (case-insensitive).
     * This property evaluates the content of the SQL query and checks for the presence
     * of the "COUNT" keyword using a regular expression.
     * 
     * @since 1.0.0
     */
    val isCount = Regex("\\bCOUNT\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)
    
    /**
     * Indicates whether the SQL query represented by this instance contains a "GROUP BY" clause.
     *
     * This property evaluates the presence of the "GROUP BY" clause in the SQL query string, 
     * case-insensitively, by searching for the regex pattern `\bGROUP\s+BY\b`.
     *
     * @since 1.0.0
     */
    val hasGroupBy = Regex("\\bGROUP\\s+BY\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)
    
    /**
     * Indicates whether the SQL query contains a window function.
     *
     * A window function is typically associated with the `OVER` clause and is
     * a key feature in advanced SQL queries that perform calculations across a set of table rows 
     * related to the current row.
     *
     * @return `true` if the query contains a window function; `false` otherwise.
     * @since 1.0.0
     */
    val isWindowFunction = Regex("\\bOVER\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)
    
    /**
     * Indicates whether the SQL query represented by this instance contains an `ORDER BY` clause.
     *
     * The check is case-insensitive and searches for the presence of the keyword "ORDER BY" 
     * in the query string.
     *
     * @since 1.0.0
     */
    val hasOrderBy = Regex("\\bORDER\\s+BY\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)
    
    /**
     * Indicates whether the SQL query contains a `WHERE` clause.
     *
     * The determination is performed by checking if the query string
     * matches the keyword `WHERE`, regardless of case.
     *
     * @since 1.0.0
     */
    val hasWhere = Regex("\\bWHERE\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)
    
    /**
     * Indicates whether the SQL query represented by this instance contains
     * a "FROM" clause. The check is case-insensitive and identifies the presence
     * of the keyword "FROM" as a separate word in the query.
     *
     * This property provides a quick way to determine if the query includes
     * a table or dataset reference, which is typically central to most 
     * SQL queries involving data retrieval or manipulation.
     *
     * @since 1.0.0
     */
    val hasFrom = Regex("\\bFROM\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)
    
    /**
     * Indicates whether the SQL query contains the "HAVING" clause.
     *
     * This property uses a regular expression to check for the presence
     * of the "HAVING" keyword in a case-insensitive manner within the SQL query string.
     *
     * @since 1.0.0
     */
    val hasHaving = Regex("\\bHAVING\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)
    
    /**
     * Indicates whether the SQL query contains the keyword "JOIN".
     *
     * The check is performed using a case-insensitive regular expression
     * to match the word "JOIN" bounded by word boundaries in the SQL string.
     *
     * @return `true` if the query contains the keyword "JOIN"; otherwise, `false`.
     * @since 1.0.0
     */
    val hasJoin = Regex("\\bJOIN\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)
    
    /**
     * Extracts and returns a list of unique table names referenced in the SQL query.
     *
     * The function identifies table names by searching for patterns associated with `FROM` and `JOIN` clauses 
     * within the query string. Table names are extracted using a case-insensitive regex pattern, and duplicates 
     * are removed from the resulting list.
     *
     * @since 1.0.0
     */
    val tables: StringList = run {
        val regex = Regex("\\bFROM\\s+([a-zA-Z0-9_.]+)|\\bJOIN\\s+([a-zA-Z0-9_.]+)", RegexOption.IGNORE_CASE)
        regex.findAll(value)
            .flatMap { match -> (-1)(match.groupValues).filter { it.isNotEmpty() } }
            .distinct()
            .toList()
    }
    
    /**
     * A computed property that returns a version of the SQL query with all string literals and numeric literals
     * replaced by placeholders (`?`). This property is useful for scenarios where SQL queries need to be 
     * masked to remove sensitive or unnecessary literal values while maintaining the structure of the query.
     *
     * String literals enclosed in single quotes are replaced, as well as standalone numeric values.
     * The resulting masked query ensures anonymization while retaining semantic integrity for processing or logging.
     *
     * @return A `SqlQuery` instance with masked literals in the SQL.
     * @since 1.0.0
     */
    val withMaskedLiterals: SqlQuery
        get() {
            val masked = value
                .replace(Regex("'[^']*'"), "?")
                .replace(Regex("\\b\\d+\\b"), "?")
            return SqlQuery(masked)
        }

    /**
     * Counts the number of parameter placeholders (?) in the query.
     *
     * @return The number of placeholders found
     * @since 1.0.0
     */
    val placeholderCount = value.count { it == '?' }

    /**
     * Represents the number of non-blank lines in the SQL query.
     * The lines are determined by splitting the query on semicolons (`;`) or newline characters (`\n`).
     * Blank lines are excluded from the count.
     *
     * @since 1.0.0
     */
    val lineCount = value.split(Regex("[;\n]")).count { it.isNotBlank() }

    /**
     * Indicates whether the SQL query represented by this instance ends with a semicolon (`;`).
     *
     * This property evaluates to `true` if the SQL query, after trimming any trailing whitespace, ends
     * with a semicolon. Otherwise, it evaluates to `false`.
     *
     * @since 1.0.0
     */
    val endsWithSemicolon: Boolean
        get() = value.trimEnd().endsWith(";")

    /**
     * Represents the count of SQL `JOIN` keywords within the query string.
     * The counting is performed using a case-insensitive regular expression pattern.
     *
     * @since 1.0.0
     */
    val joinCount = Regex("\\bJOIN\\b", RegexOption.IGNORE_CASE).findAll(value).count()

    /**
     * Indicates whether the SQL query includes both a LIMIT and an OFFSET clause.
     *
     * @return `true` if the query contains both LIMIT and OFFSET keywords (case insensitive), `false` otherwise.
     * @since 1.0.0
     */
    val hasPagination = Regex("\\bLIMIT\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)
            && Regex("\\bOFFSET\\b", RegexOption.IGNORE_CASE).containsMatchIn(value)

    /**
     * Indicates whether the SQL query contains any aggregation functions.
     *
     * Aggregation functions are detected by matching common SQL aggregation function patterns
     * such as COUNT, SUM, AVG, MIN, or MAX, case-insensitively and followed by a parenthesis.
     *
     * @return `true` if the query contains one or more aggregation functions, `false` otherwise.
     * @since 1.0.0
     */
    val hasAggregation = Regex("\\b(COUNT|SUM|AVG|MIN|MAX)\\s*\\(", RegexOption.IGNORE_CASE)
        .containsMatchIn(value)

    /**
     * Indicates whether the SQL query represented by this instance contains
     * a nested SELECT statement. A nested SELECT is identified as a subquery
     * enclosed in parentheses immediately following the `SELECT` keyword.
     *
     * This property evaluates to `true` if a nested SELECT statement is
     * found in the SQL query string, otherwise `false`.
     *
     * @since 1.0.0
     */
    val hasNestedSelect = Regex("\\(\\s*SELECT\\s", RegexOption.IGNORE_CASE).containsMatchIn(value)

    /**
     * Indicates if the SQL query contains destructive operations.
     *
     * This property evaluates whether the SQL query includes statements such as
     * `DROP`, `TRUNCATE`, `ALTER`, or `DELETE`. It helps to identify queries that
     * might alter or remove database schema or data.
     *
     * @return `true` if the query contains destructive operations, `false` otherwise.
     * @since 1.0.0
     */
    val isDestructive = Regex("\\b(DROP|TRUNCATE|ALTER|DELETE)\\b", RegexOption.IGNORE_CASE)
        .containsMatchIn(value)

    /**
     * Converts the query to a pretty-formatted string with proper indentation.
     *
     * @return A formatted version of the query
     * @since 1.0.0
     */
    val pretty: SqlQuery
        get() = value
            .replace(Regex("\\s+"), " ")
            .replace(" SELECT ", "\nSELECT ")
            .replace(" FROM ", "\nFROM ")
            .replace(" WHERE ", "\nWHERE ")
            .replace(" AND ", "\n  AND ")
            .replace(" OR ", "\n  OR ")
            .replace(" ORDER BY ", "\nORDER BY ")
            .replace(" GROUP BY ", "\nGROUP BY ")
            .replace(" HAVING ", "\nHAVING ")
            .replace(" JOIN ", "\nJOIN ")
            .replace(" LEFT JOIN ", "\nLEFT JOIN ")
            .replace(" RIGHT JOIN ", "\nRIGHT JOIN ")
            .replace(" INNER JOIN ", "\nINNER JOIN ")
            .trim() then ::SqlQuery

    /**
     * Constructs a new instance of SqlQuery by converting the provided Code instance
     * into an SQL query string representation using the `toSqlQuery` method.
     *
     * @param code The Code instance to be converted into an SQL query.
     * @since 1.0.0
     */
    constructor(code: Code) : this(code.toSqlQuery()())

    /**
     * Secondary private constructor for the `SqlQuery` class.
     * This constructor initializes a new instance by using the value of the passed `SqlQuery` object.
     * The constructor is intended for internal use and is not exposed publicly.
     *
     * @param query The `SqlQuery` instance whose value will be used to initialize the new instance.
     * @since 1.0.0
     */
    private constructor(query: SqlQuery) : this(query.value)

    init {
        validate(value.isNotBlank()) { "SQL query cannot be blank" }
        tryOrThrow({ it -> MalformedInputException("Invalid SQL query: ${it.message?.minus("net.sf.jsqlparser.parser.ParseException: ")}") }, includeCause = false) {
            CCJSqlParserUtil.parse(value)
        }
    }

    companion object {
        /**
         * Validates whether the given SQL query string is syntactically correct.
         *
         * This function utilizes a SQL parser to analyze the provided query string
         * and determines if it is valid without executing it. The validation process
         * is designed to detect syntax errors and other structural issues in the SQL query.
         *
         * @receiver The SQL query string to validate.
         * @return `true` if the SQL query is valid, otherwise `false`.
         * @since 1.0.0
         */
        fun String.isValidSqlQuery() = tryTrueOrFalse { SqlQuery(this) }
        
        /**
         * Converts the invoking string into an instance of `SqlQuery` wrapped in a `Result`.
         * This function leverages the `runCatching` scope to capture any potential exceptions
         * that might occur during the creation of the `SqlQuery` instance.
         *
         * @receiver The string to be transformed into an `SqlQuery`.
         * @return A `Result` containing the successfully constructed `SqlQuery` instance
         *         or the exception encountered during the attempt.
         * @since 1.0.0
         */
        fun String.toSqlQuery() = runCatching { SqlQuery(this) }

        /**
         * Converts the current instance of `Code` to an `SqlQuery` object if the language is SQL.
         * If the language is not SQL, an [ExpectationMismatchException] is thrown (into the result).
         *
         * @receiver The `Code` instance that contains the details necessary for conversion.
         * @return A `Result` wrapping the resulting `SqlQuery` instance or an exception if the conversion fails.
         * @since 1.0.0
         */
        fun Code.toSqlQuery() = runCatching {
            if (language == dev.tommasop1804.kutils.classes.coding.Language.SQL) SqlQuery(value)
            else throw ExpectationMismatchException("Language must be SQL")
        }

        class Serializer : ValueSerializer<SqlQuery>() {
            override fun serialize(value: SqlQuery, gen: JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<SqlQuery>() {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = SqlQuery(p.string)
        }

        class OldSerializer : JsonSerializer<SqlQuery>() {
            override fun serialize(value: SqlQuery, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.value)
            }
        }

        class OldDeserializer : JsonDeserializer<SqlQuery>() {
            override fun deserialize(p: com.fasterxml.jackson.core.JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext) = SqlQuery(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<SqlQuery?, String?> {
            override fun convertToDatabaseColumn(attribute: SqlQuery?) = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?) = dbData?.let { SqlQuery(it) }
        }
    }

    /**
     * Creates a new SqlQuery by appending additional SQL to this query.
     *
     * @param other The SQL string to append
     * @return A new SqlQuery with the combined SQL
     * @since 1.0.0
     */
    operator fun plus(@Language("sql") other: String): SqlQuery = SqlQuery("$value $other")

    /**
     * Creates a new SqlQuery by appending another SqlQuery to this query.
     *
     * A `;` will separate the queries.
     *
     * @param other The SqlQuery to append
     * @return A new SqlQuery with the combined SQL
     * @since 1.0.0
     */
    operator fun plus(other: SqlQuery): SqlQuery = SqlQuery("$value; ${other.value}")
    
    /**
     * Removes inline and multiline comments from the SQL query represented by this instance.
     * Any whitespace around the cleaned SQL query is also trimmed.
     *
     * @return A new SqlQuery instance containing the cleaned SQL query without comments.
     * @since 1.0.0
     */
    fun removeComments(): SqlQuery {
        val cleaned = value
            .replace(Regex("--.*?(\\r?\\n|$)"), "")     // Commenti inline
            .replace(Regex("/\\*.*?\\*/", RegexOption.DOT_MATCHES_ALL), "") // Commenti multilinea
            .trim()
        return SqlQuery(cleaned)
    }

    /**
     * Returns a string representation of the object. The returned string
     * provides a meaningful textual representation of the current instance.
     *
     * @return the string representation of the object.
     * @since 1.0.0
     */
    override fun toString(): String = value

    /**
     * Converts the current instance of SqlQuery into a `Code` object representation.
     * The resulting `Code` object includes the SQL language identifier and the associated SQL query value.
     *
     * @return A `Code` object encapsulating the language and query value of this SqlQuery instance.
     * @since 1.0.0
     */
    fun toCode() = Code(language = dev.tommasop1804.kutils.classes.coding.Language.SQL, value = value)

    /**
     * Executes a count query on the implemented code instance within the given context of an EntityManager.
     * This method ensures the query language is SQL and supports handling specified exceptions and time limits.
     *
     * @param parameters The parameters to be used in the query. Defaults to an empty list.
     * @param defaultException a transformer function to handle general exceptions during query execution.
     * Defaults to wrapping the caught exception into a DatabaseOperationException.
     * @param specificCases a map of specific exceptions to their corresponding transformer functions. Used for tailored
     * exception handling.
     * @param includeCause a boolean indicating whether the cause of exceptions should be included in the transformed
     * exceptions. Defaults to true.
     * @param overwriteOnly a set of exception classes for which only the specified transformations should be applied.
     * @param dontOverwrite a set of exception classes excluded from custom transformations.
     *
     * @return the result of the count query as an integer.
     *
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    fun executeCount(
        parameters: AnyList = emptyList(),
        defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        dontOverwrite: Set<KClass<out Throwable>> = emptySet()
    ): Int {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = dontOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(
                value,
                Int::class.java
            )
            parameters.forEachIndexed { i, it -> nativeQuery.setParameter(i + 1, it) }
            nativeQuery.singleResult as Int
        }
    }
    /**
     * Executes a count query on the implemented code instance within the given context of an EntityManager.
     * This method ensures the query language is SQL and supports handling specified exceptions and time limits.
     *
     * @param parameters The parameters to be used in the query. Defaults to an empty list.
     * @param defaultException a transformer function to handle general exceptions during query execution.
     * Defaults to wrapping the caught exception into a DatabaseOperationException.
     * @param specificCases a map of specific exceptions to their corresponding transformer functions. Used for tailored
     * exception handling.
     * @param includeCause a boolean indicating whether the cause of exceptions should be included in the transformed
     * exceptions. Defaults to true.
     * @param overwriteOnly a set of exception classes for which only the specified transformations should be applied.
     * @param notOverwrite a set of exception classes excluded from custom transformations.
     *
     * @return the result of the count query as an integer.
     *
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    fun executeCount(
        parameters: DataMap,
        defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        notOverwrite: Set<KClass<out Throwable>> = emptySet()
    ): Int {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = notOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(
                value,
                Int::class.java
            )
            parameters.mapKeys { if (it.key startsWith Char.COLON) (-1)(it.key) else it.key }.forEach(nativeQuery::setParameter)
            nativeQuery.singleResult as Int
        }
    }

    /**
     * Executes a SQL query with the specified parameters and returns a single result of the given type.
     * Handles exceptions using configurable strategies for specific scenarios, including no result
     * or too many results.
     *
     * @param parameters The parameters to be used in the query. Defaults to an empty list.
     * @param defaultException A lambda that transforms an exception into another throwable. Defaults to creating a `DatabaseOperationException`.
     * @param specificCases A map of specific exception types and their corresponding transformers. Defaults to an empty map.
     * @param includeCause A flag indicating whether to include the cause of exceptions. Defaults to `true`.
     * @param overwriteOnly A set of exception types for which handling should be explicitly overwritten. Defaults to an empty set.
     * @param notOverwrite A set of exception types that should not be overwritten. Defaults to an empty set.
     * @param noResultBehaviour Defines the behavior when no result is found. Defaults to `InvalidResultBehaviour.RETURN_NULL`.
     * @param tooManyResultsBehaviour Defines the behavior when too many results are found. Defaults to `InvalidResultBehaviour.THROW_EXCEPTION`.
     * @return A single result of the type `T`, or `null` if `noResultBehaviour` is set to `RETURN_NULL` and no results are found.
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    inline fun <reified T : Any> executeSelectSingleResult(
        parameters: AnyList = emptyList(),
        noinline defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        notOverwrite: Set<KClass<out Throwable>> = emptySet(),
        noResultBehaviour: InvalidResultBehaviour = InvalidResultBehaviour.RETURN_NULL,
        tooManyResultsBehaviour: InvalidResultBehaviour = InvalidResultBehaviour.THROW_EXCEPTION
    ): T? {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = notOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(
                value,
                T::class.java
            )
            parameters.forEachIndexed { i, it -> nativeQuery.setParameter(i + 1, it) }
            try {
                nativeQuery.singleResult as? T
            } catch (e: NoResultException) {
                when(noResultBehaviour) {
                    InvalidResultBehaviour.THROW_EXCEPTION -> throw NoResultsException(e.message)
                    InvalidResultBehaviour.THROW_DEFAULT_EXCEPTION -> throw defaultException(e)
                    InvalidResultBehaviour.THROW_ORIGINAL_EXCEPTION -> throw e
                    InvalidResultBehaviour.RETURN_NULL -> null
                }
            } catch (e: NonUniqueResultException) {
                when(tooManyResultsBehaviour) {
                    InvalidResultBehaviour.THROW_EXCEPTION -> throw TooManyResultsException(e.message)
                    InvalidResultBehaviour.THROW_DEFAULT_EXCEPTION -> throw defaultException(e)
                    InvalidResultBehaviour.THROW_ORIGINAL_EXCEPTION -> throw e
                    InvalidResultBehaviour.RETURN_NULL -> null
                }
            }
        }
    }
    /**
     * Executes a SQL query with the specified parameters and returns a single result of the given type.
     * Handles exceptions using configurable strategies for specific scenarios, including no result
     * or too many results.
     *
     * @param parameters The parameters to be used in the query. Defaults to an empty list.
     * @param defaultException A lambda that transforms an exception into another throwable. Defaults to creating a `DatabaseOperationException`.
     * @param specificCases A map of specific exception types and their corresponding transformers. Defaults to an empty map.
     * @param includeCause A flag indicating whether to include the cause of exceptions. Defaults to `true`.
     * @param overwriteOnly A set of exception types for which handling should be explicitly overwritten. Defaults to an empty set.
     * @param notOverwrite A set of exception types that should not be overwritten. Defaults to an empty set.
     * @param noResultBehaviour Defines the behavior when no result is found. Defaults to `InvalidResultBehaviour.RETURN_NULL`.
     * @param tooManyResultBehaviour Defines the behavior when too many results are found. Defaults to `InvalidResultBehaviour.THROW_EXCEPTION`.
     * @return A single result of the type `T`, or `null` if `noResultBehaviour` is set to `RETURN_NULL` and no results are found.
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    inline fun <reified T : Any> executeSelectSingleResult(
        parameters: DataMap,
        noinline defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        notOverwrite: Set<KClass<out Throwable>> = emptySet(),
        noResultBehaviour: InvalidResultBehaviour = InvalidResultBehaviour.RETURN_NULL,
        tooManyResultBehaviour: InvalidResultBehaviour = InvalidResultBehaviour.THROW_EXCEPTION
    ): T? {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = notOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(
                value,
                T::class.java
            )
            parameters.mapKeys { if (it.key startsWith Char.COLON) (-1)(it.key) else it.key }.forEach(nativeQuery::setParameter)
            try {
                nativeQuery.singleResult as? T
            } catch (e: NoResultException) {
                when(noResultBehaviour) {
                    InvalidResultBehaviour.THROW_EXCEPTION -> throw NoResultsException(e.message)
                    InvalidResultBehaviour.THROW_DEFAULT_EXCEPTION -> throw defaultException(e)
                    InvalidResultBehaviour.THROW_ORIGINAL_EXCEPTION -> throw e
                    InvalidResultBehaviour.RETURN_NULL -> null
                }
            } catch (e: NonUniqueResultException) {
                when(tooManyResultBehaviour) {
                    InvalidResultBehaviour.THROW_EXCEPTION -> throw TooManyResultsException(e.message)
                    InvalidResultBehaviour.THROW_DEFAULT_EXCEPTION -> throw defaultException(e)
                    InvalidResultBehaviour.THROW_ORIGINAL_EXCEPTION -> throw e
                    InvalidResultBehaviour.RETURN_NULL -> null
                }
            }
        }
    }

    /**
     * Executes a SQL query and returns the results as a list of the specified type [T].
     *
     * This method performs the execution of a given SQL query in a context bound to an
     * EntityManager. It supports exception handling with customizable behaviors, a time limit
     * for query execution, and the application of specific exception transformation rules.
     *
     * @param parameters A list of parameters to be supplied to the SQL query. Defaults to an empty list.
     * @param defaultException A transformer function to apply for exceptions that are not explicitly
     *                          mapped in [specificCases]. Defaults to a transformation generating a
     *                          [DatabaseOperationException].
     * @param specificCases A map of exception types to their corresponding transformer functions for
     *                      handling specific exceptions in a custom way. Defaults to an empty map.
     * @param includeCause A flag indicating whether the original cause should be included
     *                     when exceptions are transformed. Defaults to true.
     * @param overwriteOnly A set of exception types that should only be transformed if they are
     *                      explicitly listed here. Defaults to an empty set.
     * @param notOverwrite A set of exception types that should not be transformed. Defaults
     *                     to an empty set.
     * @return A list of results of type [T] obtained from the executed query.
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    inline fun <reified T : Any> executeSelectMultipleResult(
        parameters: AnyList = emptyList(),
        noinline defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        notOverwrite: Set<KClass<out Throwable>> = emptySet()
    ): List<T> {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = notOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(
                value,
                T::class.java
            )
            parameters.forEachIndexed { i, it -> nativeQuery.setParameter(i + 1, it) }
            nativeQuery.resultList as List<T>
        }
    }
    /**
     * Executes a SQL query and returns the results as a list of the specified type [T].
     *
     * This method performs the execution of a given SQL query in a context bound to an
     * EntityManager. It supports exception handling with customizable behaviors, a time limit
     * for query execution, and the application of specific exception transformation rules.
     *
     * @param parameters A list of parameters to be supplied to the SQL query. Defaults to an empty list.
     * @param defaultException A transformer function to apply for exceptions that are not explicitly
     *                          mapped in [specificCases]. Defaults to a transformation generating a
     *                          [DatabaseOperationException].
     * @param specificCases A map of exception types to their corresponding transformer functions for
     *                      handling specific exceptions in a custom way. Defaults to an empty map.
     * @param includeCause A flag indicating whether the original cause should be included
     *                     when exceptions are transformed. Defaults to true.
     * @param overwriteOnly A set of exception types that should only be transformed if they are
     *                      explicitly listed here. Defaults to an empty set.
     * @param notOverwrite A set of exception types that should not be transformed. Defaults
     *                     to an empty set.
     * @return A list of results of type [T] obtained from the executed query.
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    inline fun <reified T : Any> executeSelectMultipleResult(
        parameters: DataMap,
        noinline defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        notOverwrite: Set<KClass<out Throwable>> = emptySet()
    ): List<T> {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = notOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(
                value,
                T::class.java
            )
            parameters.mapKeys { if (it.key startsWith Char.COLON) (-1)(it.key) else it.key }.forEach(nativeQuery::setParameter)
            nativeQuery.resultList as List<T>
        }
    }

    /**
     * Executes a SQL query and returns the results as a set of the specified type [T].
     *
     * This method performs the execution of a given SQL query in a context bound to an
     * EntityManager. It supports exception handling with customizable behaviors, a time limit
     * for query execution, and the application of specific exception transformation rules.
     *
     * @param parameters A list of parameters to be supplied to the SQL query. Defaults to an empty list.
     * @param defaultException A transformer function to apply for exceptions that are not explicitly
     *                          mapped in [specificCases]. Defaults to a transformation generating a
     *                          [DatabaseOperationException].
     * @param specificCases A map of exception types to their corresponding transformer functions for
     *                      handling specific exceptions in a custom way. Defaults to an empty map.
     * @param includeCause A flag indicating whether the original cause should be included
     *                     when exceptions are transformed. Defaults to true.
     * @param overwriteOnly A set of exception types that should only be transformed if they are
     *                      explicitly listed here. Defaults to an empty set.
     * @param notOverwrite A set of exception types that should not be transformed. Defaults
     *                     to an empty set.
     * @return A list of results of type [T] obtained from the executed query.
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    inline fun <reified T : Any> executeSelectMultipleResultToSet(
        parameters: AnyList = emptyList(),
        noinline defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        notOverwrite: Set<KClass<out Throwable>> = emptySet()
    ): Set<T> {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = notOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(
                value,
                T::class.java
            )
            parameters.forEachIndexed { i, it -> nativeQuery.setParameter(i + 1, it) }
            (nativeQuery.resultList as List<T>).toSet()
        }
    }
    /**
     * Executes a SQL query and returns the results as a set of the specified type [T].
     *
     * This method performs the execution of a given SQL query in a context bound to an
     * EntityManager. It supports exception handling with customizable behaviors, a time limit
     * for query execution, and the application of specific exception transformation rules.
     *
     * @param parameters A list of parameters to be supplied to the SQL query. Defaults to an empty list.
     * @param defaultException A transformer function to apply for exceptions that are not explicitly
     *                          mapped in [specificCases]. Defaults to a transformation generating a
     *                          [DatabaseOperationException].
     * @param specificCases A map of exception types to their corresponding transformer functions for
     *                      handling specific exceptions in a custom way. Defaults to an empty map.
     * @param includeCause A flag indicating whether the original cause should be included
     *                     when exceptions are transformed. Defaults to true.
     * @param overwriteOnly A set of exception types that should only be transformed if they are
     *                      explicitly listed here. Defaults to an empty set.
     * @param notOverwrite A set of exception types that should not be transformed. Defaults
     *                     to an empty set.
     * @return A list of results of type [T] obtained from the executed query.
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    inline fun <reified T : Any> executeSelectMultipleResultToSet(
        parameters: DataMap,
        noinline defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        notOverwrite: Set<KClass<out Throwable>> = emptySet()
    ): Set<T> {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = notOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(
                value,
                T::class.java
            )
            parameters.mapKeys { if (it.key startsWith Char.COLON) (-1)(it.key) else it.key }.forEach(nativeQuery::setParameter)
            (nativeQuery.resultList as List<T>).toSet()
        }
    }

    /**
     * Executes a SQL query and returns the results as a collection of the specified type [T].
     *
     * This method performs the execution of a given SQL query in a context bound to an
     * EntityManager. It supports exception handling with customizable behaviors, a time limit
     * for query execution, and the application of specific exception transformation rules.
     *
     * @param collection A mutable collection of type [C] in which the results will be stored.
     * @param parameters A list of parameters to be supplied to the SQL query. Defaults to an empty list.
     * @param defaultException A transformer function to apply for exceptions that are not explicitly
     *                          mapped in [specificCases]. Defaults to a transformation generating a
     *                          [DatabaseOperationException].
     * @param specificCases A map of exception types to their corresponding transformer functions for
     *                      handling specific exceptions in a custom way. Defaults to an empty map.
     * @param includeCause A flag indicating whether the original cause should be included
     *                     when exceptions are transformed. Defaults to true.
     * @param overwriteOnly A set of exception types that should only be transformed if they are
     *                      explicitly listed here. Defaults to an empty set.
     * @param notOverwrite A set of exception types that should not be transformed. Defaults
     *                     to an empty set.
     * @return A list of results of type [T] obtained from the executed query.
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    inline fun <reified T : Any, C : MutableCollection<T>> executeSelectMultipleResultTo(
        collection: C,
        parameters: AnyList = emptyList(),
        noinline defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        notOverwrite: Set<KClass<out Throwable>> = emptySet()
    ): C {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = notOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(
                value,
                T::class.java
            )
            parameters.forEachIndexed { i, it -> nativeQuery.setParameter(i + 1, it) }
            val result = nativeQuery.resultList as MList<T>
            collection += result
            when (collection) {
                is Set<*> -> collection.toSet()
                else -> collection.toList()
            } as C
        }
    }
    /**
     * Executes a SQL query and returns the results as a collection of the specified type [T].
     *
     * This method performs the execution of a given SQL query in a context bound to an
     * EntityManager. It supports exception handling with customizable behaviors, a time limit
     * for query execution, and the application of specific exception transformation rules.
     *
     * @param collection A mutable collection of type [C] in which the results will be stored.
     * @param parameters A list of parameters to be supplied to the SQL query. Defaults to an empty list.
     * @param defaultException A transformer function to apply for exceptions that are not explicitly
     *                          mapped in [specificCases]. Defaults to a transformation generating a
     *                          [DatabaseOperationException].
     * @param specificCases A map of exception types to their corresponding transformer functions for
     *                      handling specific exceptions in a custom way. Defaults to an empty map.
     * @param includeCause A flag indicating whether the original cause should be included
     *                     when exceptions are transformed. Defaults to true.
     * @param overwriteOnly A set of exception types that should only be transformed if they are
     *                      explicitly listed here. Defaults to an empty set.
     * @param notOverwrite A set of exception types that should not be transformed. Defaults
     *                     to an empty set.
     * @return A list of results of type [T] obtained from the executed query.
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    inline fun <reified T : Any, C : MutableCollection<T>> executeSelectMultipleResultTo(
        collection: C,
        parameters: DataMap,
        noinline defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        notOverwrite: Set<KClass<out Throwable>> = emptySet()
    ): C {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = notOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(
                value,
                T::class.java
            )
            parameters.mapKeys { if (it.key startsWith Char.COLON) (-1)(it.key) else it.key }.forEach(nativeQuery::setParameter)
            val result = nativeQuery.resultList as MList<T>
            collection += result
            when (collection) {
                is Set<*> -> collection.toSet()
                else -> collection.toList()
            } as C
        }
    }

    /**
     * Executes an update query in the database using the provided SQL code and parameters.
     *
     * @param parameters A list of parameters to bind to the query. Defaults to an empty list.
     * @param defaultException A lambda function to transform any exception into a specific throwable. Defaults to a `DatabaseOperationException`.
     * @param specificCases A map of specific exception types and their corresponding transformers. Defaults to an empty map.
     * @param includeCause Determines whether the original cause of the exception should be included. Defaults to true.
     * @param overwriteOnly A set of exception classes for which the default exception transformation is strictly applied. Defaults to an empty set.
     * @param notOverwrite A set of exception classes to exclude from being overwritten by the default exception transformation. Defaults to an empty set.
     * @return The number of rows affected by the update query.
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    fun executeUpdate(
        parameters: AnyList = emptyList(),
        defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        notOverwrite: Set<KClass<out Throwable>> = emptySet()
    ): Int {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = notOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(value)
            parameters.forEachIndexed { i, it -> nativeQuery.setParameter(i + 1, it) }
            nativeQuery.executeUpdate()
        }
    }
    /**
     * Executes an update query in the database using the provided SQL code and parameters.
     *
     * @param parameters A list of parameters to bind to the query. Defaults to an empty list.
     * @param defaultException A lambda function to transform any exception into a specific throwable. Defaults to a `DatabaseOperationException`.
     * @param specificCases A map of specific exception types and their corresponding transformers. Defaults to an empty map.
     * @param includeCause Determines whether the original cause of the exception should be included. Defaults to true.
     * @param overwriteOnly A set of exception classes for which the default exception transformation is strictly applied. Defaults to an empty set.
     * @param notOverwrite A set of exception classes to exclude from being overwritten by the default exception transformation. Defaults to an empty set.
     * @return The number of rows affected by the update query.
     * @since 1.0.0
     */
    context(entityManager: EntityManager)
    fun executeUpdate(
        parameters: DataMap,
        defaultException: ThrowableTransformer = { _ -> DatabaseOperationException(this) },
        specificCases: Map<KClass<out Throwable>, ThrowableTransformer> = emptyMap(),
        includeCause: Boolean = true,
        overwriteOnly: Set<KClass<out Throwable>> = emptySet(),
        notOverwrite: Set<KClass<out Throwable>> = emptySet()
    ): Int {
        return tryOrThrow(defaultException, specificCases = specificCases, includeCause = includeCause, overwriteOnly = overwriteOnly, notOverwrite = notOverwrite) {
            val nativeQuery = entityManager.createNativeQuery(value)
            parameters.mapKeys { if (it.key startsWith Char.COLON) (-1)(it.key) else it.key }.forEach(nativeQuery::setParameter)
            nativeQuery.executeUpdate()
        }
    }

    /**
     * Represents the types of SQL operations that can be performed within a query.
     *
     * Instances of this enum are used to denote the specific SQL command type 
     * such as `SELECT`, `INSERT`, or `UPDATE`. This enables categorization and
     * handling of SQL queries corresponding to their operation type.
     *
     * @since 1.0.0
     */
    enum class Type {
        /**
         * Represents the SELECT type in a SQL operation.
         *
         * SELECT is used to retrieve data from a database table.
         *
         * @since 1.0.0
         */
        SELECT,
        /**
         * Represents an SQL INSERT operation.
         *
         * Used to define the insertion of new records into a database table.
         *
         * @since 1.0.0
         */
        INSERT,
        /**
         * Represents the UPDATE operation type, commonly associated with modifying existing data in a database table.
         *
         * This operation is used to change one or more records in a table based on a specified condition.
         *
         * @since 1.0.0
         */
        UPDATE,
        /**
         * Represents the DELETE operation typically used for removing records 
         * from a database table.
         *
         * @since 1.0.0
         */
        DELETE,
        /**
         * Represents the `CREATE` operation in the SQL command type enumeration.
         *
         * Typically used to define or create new database objects such as tables, views, or indexes.
         *
         * @since 1.0.0
         */
        CREATE,
        /**
         * Represents an operation to remove a database object such as a table, index, or view.
         *
         * This type is commonly used in database management systems to specify
         * that a specific entity should be permanently deleted.
         *
         * Use with caution as this operation is irreversible.
         *
         * @since 1.0.0
         */
        DROP,
        /**
         * Represents the `ALTER` operation which is part of the SQL statement types.
         *
         * The `ALTER` operation is used to modify the structure of an existing database object,
         * such as a table or column, without removing it.
         *
         * This is typically used in database management systems to change table structures,
         * add or drop columns, modify data types, or apply constraints.
         *
         * @since 1.0.0
         */
        ALTER,
        /**
         * Represents the TRUNCATE operation, typically used in the context of database operations
         * to quickly remove all rows from a table while preserving the structure and data definitions.
         *
         * This operation is more efficient than DELETE as it bypasses transactional logs 
         * and does not physically delete each row. However, it requires specific privileges and 
         * is irreversible.
         *
         * @since 1.0.0
         */
        TRUNCATE
    }

    /**
     * Specifies the behavior to follow when there is no result returned
     * from a specific operation or process. This can be used to handle
     * scenarios where the outcome is undefined or an exception occurs.
     *
     * @since 1.0.0
     * @author Tommaso Pastorelli
     */
    enum class InvalidResultBehaviour {
        /**
         * Represents a behavior where an exception is thrown when no result is available or retrievable.
         * Typically used to signal an error or unexpected condition in processes where a return value is mandatory.
         *
         * @since 1.0.0
         */
        THROW_EXCEPTION,
        /**
         * Indicates that the operation will return null when no result can be obtained.
         *
         * Typically used in scenarios where an absence of a result is considered acceptable behavior
         * instead of throwing an exception.
         *
         * @since 1.0.0
         */
        RETURN_NULL,
        /**
         * Represents a behavior where a default exception is thrown when no result is available.
         * This is typically used to dictate behavior in scenarios where an expected result is missing.
         *
         * @since 1.0.0
         */
        THROW_DEFAULT_EXCEPTION,
        /**
         * Represents a behavior when no result is available and the original exception
         * [jakarta.persistence.NoResultException] should be thrown in response to such a scenario.
         *
         * This behavior is typically part of the [InvalidResultBehaviour] enumeration and is
         * meant to ensure that the system reacts by rethrowing the original exception
         * encountered during execution or processing in the context where no result is deliverable.
         *
         * @since 1.0.0
         */
        THROW_ORIGINAL_EXCEPTION
    }
}