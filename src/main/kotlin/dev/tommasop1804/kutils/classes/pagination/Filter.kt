@file:JvmName("PaginationFilterKt")
@file:Suppress("unused", "sqlDialectInspection")
@file:Since("1.0.0")

package dev.tommasop1804.kutils.classes.pagination

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonSerialize
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Represents a filter option used for querying or filtering data based on a specific field,
 * operator, and value. The class supports custom operators and allows dynamic evaluation
 * of property values using Kotlin reflection.
 *
 * Provides functionality to construct filter criteria and retrieve property values dynamically.
 *
 * This class is Serializable but not Deserializable.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = FilterOption.Companion.Serializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = FilterOption.Companion.OldSerializer::class)
data class FilterOption(
    var field: String? = null,
    var operator: FilterOperator = FilterOperator.EQUALS,
    var value: Any?
) {
    /**
     * Secondary constructor for initializing a filter object with a specific field, operator, and value.
     *
     * @param field The property on which the filter is based. It determines the field to be filtered.
     * @param operator The filter operator used for comparison. Defaults to `FilterOperator.EQUALS`.
     * @param value The value specified for the filtering condition.
     * @since 1.0.0
     */
    constructor(field: KProperty<*>, operator: FilterOperator = FilterOperator.EQUALS, value: Any?) : this(
        field.run {
            val type1 = ownerClass?.simpleName
            if (type1.isNotNull()) $$"$$type1$$${name}" else name
        },
        operator,
        value
    )

    /**
     * Constructs a `FilterOption` instance by converting an operator string to its corresponding
     * `FilterOperator` enum value. If the specified operator is invalid or cannot be resolved, an
     * `IllegalArgumentException` is thrown.
     *
     * - `field`: Represents the field name on which filtering is applied. Default is `null`.
     * - `operator`: Defines the filtering operation as a string. Defaults to `"eq"`.
     * - `value`: The value to filter against.
     *
     * Delegates to another primary constructor of `FilterOption` after resolving the operator.
     *
     * @param field The name of the field on which the filter is applied. Can be `null`.
     * @param operator The operator for filtering, represented as a string. Defaults to `"eq"`.
     * @param value The value against which the field is filtered.
     * @throws IllegalArgumentException If the provided operator string cannot be resolved to a `FilterOperator`.
     * @since 1.0.0
     */
    constructor(field: String? = null, operator: String = "eq", value: Any?) : this(
        field,
        FilterOperator.ofOperator(operator) ?: throw IllegalArgumentException("Invalid operator"),
        value
    )

    /**
     * Constructs a `FilterOption` instance from a field, operator string, and value.
     *
     * This constructor allows creating a `FilterOption` object by specifying a field, a string
     * representation of an operator, and a value. The operator string is converted into its corresponding
     * `FilterOperator` enum value. If the provided operator string does not match any predefined operator,
     * an `IllegalArgumentException` is thrown.
     *
     * @param field The property reference (field) to which the filter should apply.
     * @param operator The string representation of the operator, defaulting to "eq" (equals).
     * @param value The value against which the filter is applied. Can be of any type.
     * @throws IllegalArgumentException If the provided operator string is invalid or does not correspond to a valid `FilterOperator`.
     * @since 1.0.0
     */
    constructor(field: KProperty<*>, operator: String = "eq", value: Any?) : this(
        field.run {
            val type1 = (parameters.firstOrNull()?.type?.classifier as? KClass<*>)?.simpleName
            if (type1.isNotNull()) $$"$$type1$$${field.name}" else name
        },
        FilterOperator.ofOperator(operator) ?: throw IllegalArgumentException("Invalid operator"),
        value
    )

    /**
     * Constructs a `FilterOption` instance by parsing the input string.
     *
     * This constructor utilizes the `parse` function to interpret the input string
     * and derive the field, operator, and value required to initialize the `FilterOption`.
     *
     * @param stringToParse The input string to parse for initializing the `FilterOption`.
     *                      The string should be in the format "field:operator:value".
     * @throws IllegalArgumentException If the input string does not contain a valid operator.
     * @since 1.0.0
     */
    constructor(stringToParse: String) : this(parse(stringToParse).onlyElement())

    /**
     * Private secondary constructor for initializing the class with an instance of [FilterOption].
     *
     * This constructor reuses properties of the provided [filter] instance
     * to initialize the primary constructor of the class.
     *
     * @param filter An instance of [FilterOption] used to initialize the object.
     * @since 1.0.0
     */
    private constructor(filter: FilterOption) : this(filter.field, filter.operator, filter.value)

    companion object {
        /**
         * Parses a string into a `FilterOption` object by extracting the field, operator, and value
         * components from the input string based on specific delimiters.
         *
         * The input string is split into parts using a separator as the delimiter up to a maximum of three parts:
         * - The first part represents the field name.
         * - The second part is converted into a `FilterOperator`.
         * - The third part represents the value.
         *
         * @param strings The input string to be parsed into a `FilterOption` object. Should be in the format "field:operator:value".
         * @param separatorSymbol The regular expression pattern used to split the input strings. Defaults to a colon (:).
         * @return A `FilterOption` object containing the parsed field, operator, and value.
         * @throws IllegalArgumentException If the input string does not contain a valid operator.
         * @throws MalformedInputException If the input string contains an invalid field name.
         * @since 1.0.0
         */
        fun parse(vararg strings: String, separatorSymbol: Regex = Regex(":")) = strings.map {
            val list = it.splitAndTrim(separatorSymbol, limit = 3)
            list.size == 3 || throw MalformedInputException("Invalid filter option string: $it")
            FilterOption(
                list.first(),
                FilterOperator.ofOperator(list[1]) ?: throw IllegalArgumentException("Invalid operator"),
                list[2]
            )
        }

        /**
         * Parses a string into a `FilterOption` object by extracting the field, operator, and value
         * components from the input string based on specific delimiters.
         *
         * The input string is split into parts using a colon as the delimiter up to a maximum of three parts:
         * - The first part represents the field name.
         * - The second part is converted into a `FilterOperator`.
         * - The third part represents the value.
         *
         * @param strings The input string to be parsed into a `FilterOption` object. Should be in the format "field:operator:value".
         * @param separatorSymbol The regular expression pattern used to split the input strings. Defaults to a colon (:).
         * @return A `FilterOption` object containing the parsed field, operator, and value.
         * @throws IllegalArgumentException If the input string does not contain a valid operator.
         * @throws MalformedInputException If the input string contains an invalid field name.
         * @since 1.0.0
         */
        fun parse(strings: Iterable<String>, separatorSymbol: Regex = Regex(":")) = parse(*strings.toList().toTypedArray(), separatorSymbol = separatorSymbol)

        class Serializer : ValueSerializer<FilterOption>() {
            override fun serialize(
                value: FilterOption,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                if (value.field.isNotNull())
                    gen.writeStringProperty("field", "${value.field}")
                gen.writeStringProperty("operator", value.operator.name)
                gen.writeStringProperty("value", value.value?.toString())
                gen.writeEndObject()
            }
        }

        class OldSerializer : JsonSerializer<FilterOption>() {
            override fun serialize(value: FilterOption, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                if (value.field.isNotNull())
                    gen.writeStringField("field", "${value.field}")
                gen.writeStringField("operator", value.operator.name)
                gen.writeStringField("value", value.value?.toString())
                gen.writeEndObject()
            }
        }
    }

    /**
     * Retrieves the value of a specified property based on its name using reflection.
     *
     * This function operates as a delegated property getter and leverages a reflective approach
     * to convert an object's properties to a map and resolve the value of the requested property.
     *
     * - `field` - TYPE: `KProperty?`
     * - `operator` - TYPE: [FilterOperator]
     * - `value` - TYPE: `Any?`
     *
     * @param thisRef The object from which the property value is retrieved. Can be `null`.
     * @param property The metadata of the property being accessed.
     * @return The value of the property cast to the specified type `R`.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = toReflectionMap().getValue(property.name) as R

    /**
     * Returns a string representation of the FilterOption instance.
     *
     * The returned string includes details about the field, operator, and value.
     *
     * @return A string representation of the FilterOption in the format "FilterOption(field=<field_name>, operator=<operator>, value=<value>)".
     * @since 1.0.0
     */
    override fun toString(): String = "FilterOption(field=$field, operator=$operator, value=$value)"
}

/**
 * Represents a set of filter operations that can be used for building queries or applying
 * filtering logic to data. Each filter operator corresponds to a symbolic operator
 * and an associated SQL-like representation.
 *
 * This enum provides a comprehensive list of commonly used filter operations such as
 * equality, comparison, logical connectors, and string matchers.
 *
 * @property operator A symbolic representation of the filter operation, typically short and
 *                    concise, used as an identifier.
 * @property sql      A SQL-like string template that represents the filter operation
 *                    in query syntax. It may contain placeholders like `{field}`, `{value}`,
 *                    or `{condition}` that can be replaced to build concrete expressions.
 * @property category Since 4.6.8, a category that groups filter operations based on their functionalities.
 *                    This property is used to categorize filter operations for better organization
 *                    and filtering logic management.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
enum class FilterOperator(val operator: String, val sql: String, @field:Since("4.6.8") val category: Category) {
    /**
     * Represents the equality filter operation within the `FilterOperator` class.
     *
     * This operation is used to filter datasets or collections by comparing a field with a specific value
     * and returning only those elements where the field's value matches the provided value.
     *
     * The SQL representation of this operator is defined as "{field} = {value}".
     *
     * @property operator The string representation of the filter operator, typically "eq" for equality.
     * @property sql The SQL syntax associated with the filter operation.
     * @property category Since 4.6.8, the category of the filter operation
     * @since 1.0.0
     */
    EQUALS("eq", "{field} = {value}", Category.EQUALITY),
    /**
     * Represents the "not equals" filter operator in a query.
     *
     * The `NOT_EQUALS` operator is used to filter data where the value of a specified field
     * is not equal to a given value. This operator is commonly utilized in query-building
     * scenarios for data filtering purposes.
     *
     * @property operator The shorthand notation for the operator, typically used in query language syntax.
     * @property sql The SQL representation of the operator.
     * @property category Since 4.6.8, the category of the filter operation
     * @since 1.0.0
     */
    NOT_EQUALS("ne", "{field} <> {value}", Category.EQUALITY),
    /**
     * Represents the "in" operator used for filtering.
     *
     * The `IN` operator is used to check if a value is present within a specific set of values provided in a filter.
     *
     * @property operator A string representation of the operator, in this case, "in".
     * @property sql The SQL representation of the operator, formatted as "{value} in {field}".
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    IN("in", "{value} IN {field}", Category.ARRAY_SEARCH),
    /**
     * Represents the `NOT IN` filter operator used to determine if a given value does not exist
     * within a specified set of values or collection.
     *
     * The operator is typically used in query generation or filtering workflows, where
     * conditions such as "value NOT IN (set)" need to be represented in a structured format.
     *
     * The `operator` ("nin") defines the symbolic representation of this filter operator, while
     * the `sql` pattern ("{value} NOT IN {field}") specifies how the operator should be formatted during translation to SQL or similar query languages.
     *
     * @param operator The symbolic representation of the `NOT IN` filter operator.
     * @param sql The pattern defining how the operator is expressed in a query language.
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    NOT_IN("nin", "{value} NOT IN {field}", Category.ARRAY_SEARCH),
    /**
     * Represents a "less than" filter operation in a query or filtering system.
     *
     * The `LESS_THAN` operation is used to evaluate whether a specific field's value
     * is less than the given comparison value. It is typically utilized in filtering scenarios
     * to include only records that satisfy the specified "less than" condition.
     *
     * @property operator The string representation of the filter operator.
     * @property sql The corresponding SQL representation for the operator.
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    LESS_THAN("lt", "{field} < {value}", Category.COMPARISON),
    /**
     * Represents a "greater than" filter operator for use in filtering operations.
     *
     * The `GREATER_THAN` filtering operator is used to identify and select records
     * where the value of a specified field is greater than a given comparison value.
     * This is particularly useful in query-building scenarios where conditional
     * filtering by field value is needed.
     *
     * The operator has a symbolic representation `>` for SQL and similar query languages.
     * @property operator The string representation of the filter operator.
     * @property sql The corresponding SQL representation for the operator.
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    GREATER_THAN("gt", "{field} > {value}", Category.COMPARISON),
    /**
     * Represents a filtering operator for "less than or equal to" comparisons.
     *
     * The `LESS_THAN_OR_EQUALS` filter operator is used to compare a field's value
     * to a specified value, evaluating to true if the field's value is less than or equal
     * to the given value. It is commonly utilized in database queries or filtering mechanisms.
     *
     * @param operator The symbolic representation of the operator, i.e., "le".
     * @param sql The SQL representation used in query generation, i.e., "{field} <= {value}".
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    LESS_THAN_OR_EQUALS("le", "{field} <= {value}", Category.COMPARISON),
    /**
     * A filter operator representing the "greater than or equals" comparison.
     *
     * The `GREATER_THAN_OR_EQUALS` operator is used to filter data where a field value is greater than or equal to a specified value.
     * It is commonly employed in filtering criteria in data retrieval or query-building scenarios.
     *
     * @property operator The shorthand identifier for the "greater than or equals" operation.
     * @property sql The SQL representation of the "greater than or equals" operation.
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    GREATER_THAN_OR_EQUALS("ge", "{field} >= {value}", Category.COMPARISON),
    /**
     * Represents a `STARTS_WITH` filter operation for querying datasets.
     *
     * The `STARTS_WITH` filter checks if a field's value begins with the specified value.
     * This operator generates an SQL `LIKE` expression using the format:
     * `{field} LIKE CONCAT({value}, '%')`.
     *
     * @param operator The representation of the operator as a string, used for logical identification.
     * @param sql The corresponding SQL fragment for the filter operation.
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    STARTS_WITH("startswith", "{field} LIKE CONCAT({value}, '%')", Category.STRING),
    /**
     * Represents a filter operator that matches values where the specified field does not start with the given value.
     *
     * This operator constructs an SQL condition using the "NOT LIKE" clause with a wildcard (%) appended
     * to the provided value, ensuring that the field does not start with the specified pattern.
     *
     * @property operator The symbolic representation of the filter operator.
     * @property sql The SQL expression that corresponds to the operator.
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    NOT_STARTS_WITH("nstartswith", "{field} NOT LIKE CONCAT({value}, '%')", Category.STRING),
    /**
     * Represents a filter operator that checks if the field value ends with the specified value.
     *
     * The `ENDS_WITH` operator is translated into a SQL `LIKE` clause that matches records
     * where the field's value ends with the given string. This allows for filtering data based
     * on string suffix matching.
     *
     * Operator: `endswith`
     * SQL Translation: `{field} LIKE CONCAT('%', {value})`
     *
     * @property operator The string representation of the filter operator.
     * @property sql The corresponding SQL representation for the operator.
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    ENDS_WITH("endswith", "{field} LIKE CONCAT('%', {value})", Category.STRING),
    /**
     * Represents a filter operator that checks if a field's value does not end with the specified value.
     *
     * The `NOT_ENDS_WITH` operator is used to construct an SQL condition using the "NOT LIKE" clause
     * with a wildcard (%) appended to the provided value, ensuring that the field's value does not end with the specified pattern.
     *
     * Operator: `notendwith`
     * SQL Translation: `{field} NOT LIKE CONCAT('%', {value})`
     *
     * @property operator The string representation of the filter operator.
     * @property sql The corresponding SQL representation for the operator.
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    NOT_ENDS_WITH("nendswith", "{field} NOT LIKE CONCAT('%', {value})", Category.STRING),
    /**
     * An operator used for filtering data by checking if a field contains a specified value.
     *
     * `CONTAINS` is used to construct SQL-like filter operations where the value
     * is checked to be a substring of the field's content. The operation format
     * is represented as `{field} LIKE CONCAT('%', {value}, '%')`.
     *
     * @property operator The name of the filter operation, represented as "contains".
     * @property sql The SQL representation of the filter operation.
     * @property category Since 4.6.8, the category of the filter operation
     *
     *
     * @since 1.0.0
     */
    CONTAINS("contains", "{field} LIKE CONCAT('%', {value}, '%')", Category.STRING),
    /**
     * Represents a filter operator that checks if a field does not contain a specified value.
     *
     * The `NOT_CONTAINS` operator is used in filtering expressions to exclude records where
     * a specified field contains a particular substring. It constructs a SQL condition
     * using the `NOT LIKE` operator with wildcard patterns.
     *
     * For instance, given a field and a value, the SQL condition generated will look like:
     * `{field} NOT LIKE CONCAT('%', {value}, '%')`.
     *
     * This is useful for scenarios where you need to filter out records based on
     * partial matches within string fields.
     *
     * @param operator The name of the operator as a string.
     * @param sql The SQL representation of the operator condition.
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    NOT_CONTAINS("ncontains", "{field} NOT LIKE CONCAT('%', {value}, '%')", Category.STRING),
    /**
     * Represents a SQL `LIKE` operator used for filtering data where a field matches a specified pattern.
     *
     * This operator is generally used in query conditions to perform pattern matching
     * against string data types. The comparison is case-sensitive in most SQL implementations.
     *
     * @property operator The string representing the operator, in this case "like".
     * @property sql The SQL representation of the operator, including placeholders for the field and value.
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    LIKE("like", "{field} LIKE {value}", Category.STRING),
    /**
     * Represents an operator used for filtering entities based on a NOT LIKE condition.
     *
     * This class is part of the `FilterOperator` family, which defines various operations for data filtering.
     * The `NOT_LIKE` operator determines whether a specified field's value does not match a given pattern
     * using a SQL `NOT LIKE` expression.
     *
     * @property operator The symbolic representation of the operation, in this case, "nlike".
     * @property sql The SQL-compatible template for the NOT LIKE operation, "{field} NOT LIKE {value}".
     * @property category Since 4.6.8, the category of the filter operation
     *
     * @since 1.0.0
     */
    NOT_LIKE("nlike", "{field} NOT LIKE {value}", Category.STRING);

    companion object {
        /**
         * Converts a given operator string to its corresponding `FilterOperator` enum value.
         *
         * This function checks if the provided operator string matches the `operator` property
         * of any `FilterOperator` enumerations. If a match is found, it returns the corresponding
         * `FilterOperator`, otherwise it returns `null`.
         *
         * @param operator The string representation of an operator to be converted.
         * @return The corresponding `FilterOperator` if the input string matches an operator in the
         *         enum.
         * @since 1.0.0
         */
        infix fun ofOperator(operator: String) = operator.run { entries.find { equalsIgnoreCase(it.operator) } }

        /**
         * Filters the entries of `FilterOperator` based on the specified category.
         *
         * This method selects and returns filter operators that belong to the given
         * `Category`. It allows for easy grouping and identification of operators
         * based on predefined categories like equality, comparison, string operations,
         * and array searches.
         *
         * @param category The category to filter the operators by. Must be a valid
         *        instance of the `Category` enum class.
         * @since 1.0.0
         */
        infix fun byCategory(category: Category) = FilterOperator.entries.filter { it.category == category }
    }

    /**
     * Enum representing categories for different types of operations.
     *
     * The `Category` enum class is used to categorize specific filter operations,
     * enabling them to be grouped and identified based on their respective contexts.
     *
     * - `EQUALITY`: Represents operations that check for equality.
     * - `COMPARISON`: Represents operations involving comparison (e.g., greater-than, less-than).
     * - `STRING`: Represents operations specifically for string manipulation or evaluation.
     * - `ARRAY_SEARCH`: Represents operations that involve searching within arrays or collections.
     *
     * This enum is primarily used within the context of filter operators.
     *
     * @since 1.0.0
     */
    enum class Category {
        /**
         * Represents the EQUALITY category within the Category enumeration.
         *
         * This category typically encompasses functionalities related to equality operations
         * or checks, providing a concise and clear distinction within the broader scope of
         * categorized logic.
         *
         * Usage of this category can denote operations or processes focusing on asserting
         * or evaluating equivalence between entities, values, or properties.
         *
         * @since 1.0.0
         */
        EQUALITY,
        /**
         * Represents the `COMPARISON` category used for comparing elements or entities.
         *
         * This category is typically used to define or represent operations that involve
         * comparing values, such as greater than, less than, or equality checks.
         * It is part of the `Category` enum class.
         *
         * @since 1.0.0
         */
        COMPARISON,
        /**
         * Represents the type of a category linked specifically to string operations.
         *
         * The `STRING` category is part of an enumerated classification system
         * meant to define groups of operations or functionalities related to strings.
         *
         * @since 1.0.0
         */
        STRING,
        /**
         * Represents an operation or concept related to searching within arrays.
         *
         * The `ARRAY_SEARCH` classification within the `Category` enumeration is utilized to denote functionality
         * or operations that deal specifically with array-based search algorithms or processes. This can include
         * equality-based searches, range searches, or complex patterns applied to arrays or list-like data structures.
         *
         * @since 1.0.0
         */
        ARRAY_SEARCH
    }
    
    /*    /*
     * Represents a filter operator for checking the existence of a specific condition.
     *
     * The `HAS` operator is used to verify the presence of records that match a certain condition within a sub-query.
     * It is typically applied in filtering scenarios where an "EXISTS" condition is required in a SQL-like query language.
     *
     * @property operator The name of the operator as a string.
     * @property sql The corresponding SQL representation of the filter operator, which allows dynamic substitution of placeholders.
     *
     * @since 1.0.0
     */
    HAS("has", "EXISTS (SELECT 1 FROM {field} WHERE {value})"),*/
    /*
     * Represents the logical negation operation in a filter context.
     *
     * The logical negation, identified by the keyword "not", is used to negate a specified condition.
     * It is often utilized in expressions to invert the result of a logical evaluation.
     *
     * @property operator The keyword representing the logical negation operation, e.g., "not".
     * @property sql The SQL representation of the logical negation operation, e.g., "NOT ({condition})".
     * @since 1.0.0
     *//*

    LOGICAL_NEGATION("not", "NOT ({condition})"),

        /*
     * Represents a filter operator used in querying data.
     *
     * The `ANY` operator checks if a condition is met for at least one element in a collection.
     * It is typically utilized in queries where an `EXISTS` clause is required.
     *
     * - The operator keyword is "any".
     * - The SQL representation of the condition is "EXISTS (SELECT 1 FROM {field} f WHERE f IN ({value}))".
     *
     * @param operator The keyword representing the filter operator.
     * @param sql The SQL representation of the filter condition.
     *
     * @since 1.0.0
     */
    ANY("any", "EXISTS (SELECT 1 FROM {field} f WHERE f IN ({value}))"),
    /*
     * Represents the "ALL" filter operator used in SQL operations.
     *
     * The `ALL` operator is used to filter a dataset by checking that all sub-values
     * of a certain field satisfy a specific condition when compared to a given set of values.
     *
     * The SQL equivalent of the `ALL` operator is implemented as:
     * `NOT EXISTS (SELECT 1 FROM {field} f WHERE f NOT IN ({value}))`.
     *
     * @property operator The string representation of the operator ("all").
     * @property sql The SQL equivalent of the operator's logic.
     *
     * @since 1.0.0
     */
    ALL("all", "NOT EXISTS (SELECT 1 FROM {field} f WHERE f NOT IN ({value}))"),

    /*
     * Represents the logical AND filter operator in query construction or filtering conditions.
     *
     * This operator is used to combine multiple conditions logically, ensuring that all the conditions
     * must be true for a record to match the filter.
     *
     * @property operator The string representation of the operator.
     * @property sql The equivalent SQL representation of the operator.
     * @since 1.0.0
     */
    AND("and", "AND"),
    /*
     * Represents the logical OR operator in filtering operations.
     *
     * The `OR` operator is used to combine multiple filter conditions, where the overall
     * condition is true if at least one of the individual conditions is true.
     *
     * This operator is typically used in scenarios where multiple filtering criteria need
     * to be evaluated in a disjunctive manner.
     *
     * @property operator The string representation of the operator.
     * @property sql The SQL representation of the operator.
     *
     * @since 1.0.0
     */
    OR("or", "OR"),
*/
}