/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("PaginationFilterKt")
@file:Suppress("unused", "sqlDialectInspection")
@file:Since("1.0.0")
@file:MustUseReturnValues

package dev.tommasop1804.kutils.classes.pagination

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.exceptions.*
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Represents a filter option used for querying or filtering data based on a specific property,
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
@JsonDeserialize(using = FilterOption.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = FilterOption.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = FilterOption.Companion.OldDeserializer::class)
data class FilterOption(
    var property: String? = null,
    var operator: FilterOperator = FilterOperator.Equals,
    var value: Any?
) {
    /**
     * Secondary constructor for initializing a filter object with a specific property, operator, and value.
     *
     * @param property The property on which the filter is based. It determines the property to be filtered.
     * @param operator The filter operator used for comparison. Defaults to `FilterOperator.EQUALS`.
     * @param value The value specified for the filtering condition.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>, operator: FilterOperator = FilterOperator.Equals, value: Any?) : this(
        property.run {
            val type1 = ownerClass?.simpleName
            if (type1 != null) $$"$$type1$$${name}" else name
        },
        operator,
        value
    )

    /**
     * Constructs a `FilterOption` instance by converting an operator string to its corresponding
     * `FilterOperator` enum value. If the specified operator is invalid or cannot be resolved, an
     * `IllegalArgumentException` is thrown.
     *
     * - `property`: Represents the property name on which filtering is applied. Default is `null`.
     * - `operator`: Defines the filtering operation as a string. Defaults to `"eq"`.
     * - `value`: The value to filter against.
     *
     * Delegates to another primary constructor of `FilterOption` after resolving the operator.
     *
     * @param property The name of the property on which the filter is applied. Can be `null`.
     * @param operator The operator for filtering, represented as a string. Defaults to `"eq"`.
     * @param value The value against which the property is filtered.
     * @throws IllegalArgumentException If the provided operator string cannot be resolved to a `FilterOperator`.
     * @since 1.0.0
     */
    constructor(property: String? = null, operator: String = FilterOperator.Equals.operator, value: Any?) : this(
        property,
        FilterOperator.ofOperator(operator) ?: throw IllegalArgumentException("Invalid operator"),
        value
    )

    /**
     * Constructs a `FilterOption` instance from a property, operator string, and value.
     *
     * This constructor allows creating a `FilterOption` object by specifying a property, a string
     * representation of an operator, and a value. The operator string is converted into its corresponding
     * `FilterOperator` enum value. If the provided operator string does not match any predefined operator,
     * an `IllegalArgumentException` is thrown.
     *
     * @param property The property reference to which the filter should apply.
     * @param operator The string representation of the operator, defaulting to "eq" (equals).
     * @param value The value against which the filter is applied. Can be of any type.
     * @throws IllegalArgumentException If the provided operator string is invalid or does not correspond to a valid `FilterOperator`.
     * @since 1.0.0
     */
    constructor(property: KProperty<*>, operator: String = FilterOperator.Equals.operator, value: Any?) : this(
        property.run {
            val type1 = (parameters.firstOrNull()?.type?.classifier as? KClass<*>)?.simpleName
            if (type1 != null) $$"$$type1$$${property.name}" else name
        },
        FilterOperator.ofOperator(operator) ?: throw IllegalArgumentException("Invalid operator"),
        value
    )

    /**
     * Constructs a `FilterOption` instance by parsing the input string.
     *
     * This constructor utilizes the `parse` function to interpret the input string
     * and derive the property, operator, and value required to initialize the `FilterOption`.
     *
     * @param stringToParse The input string to parse for initializing the `FilterOption`.
     *                      The string should be in the format "property:operator:value".
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
    private constructor(filter: FilterOption) : this(filter.property, filter.operator, filter.value)

    companion object {
        /**
         * Parses a string into a `FilterOption` object by extracting the property, operator, and value
         * components from the input string based on specific delimiters.
         *
         * The input string is split into parts using a separator as the delimiter up to a maximum of three parts:
         * - The first part represents the property name.
         * - The second part is converted into a `FilterOperator`.
         * - The third part represents the value.
         *
         * @param strings The input string to be parsed into a `FilterOption` object. Should be in the format "property:operator:value".
         * @param separatorSymbol The regular expression pattern used to split the input strings. Defaults to a colon (:).
         * @return A `FilterOption` object containing the parsed property, operator, and value.
         * @throws IllegalArgumentException If the input string does not contain a valid operator.
         * @throws MalformedInputException If the input string contains an invalid property name.
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
         * Parses a string into a `FilterOption` object by extracting the property, operator, and value
         * components from the input string based on specific delimiters.
         *
         * The input string is split into parts using a colon as the delimiter up to a maximum of three parts:
         * - The first part represents the property name.
         * - The second part is converted into a `FilterOperator`.
         * - The third part represents the value.
         *
         * @param strings The input string to be parsed into a `FilterOption` object. Should be in the format "property:operator:value".
         * @param separatorSymbol The regular expression pattern used to split the input strings. Defaults to a colon (:).
         * @return A `FilterOption` object containing the parsed property, operator, and value.
         * @throws IllegalArgumentException If the input string does not contain a valid operator.
         * @throws MalformedInputException If the input string contains an invalid property name.
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
                if (value.property != null)
                    gen.writeStringProperty("property", "${value.property}")
                gen.writeStringProperty("operator", value.operator.operator)
                gen.writeStringProperty("value", value.value?.toString())
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<FilterOption>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): FilterOption {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return FilterOption(
                    node.get("property").traverse(p.objectReadContext()).readValueAs(String::class.java),
                    FilterOperator.ofOperator(node.get("operator").traverse(p.objectReadContext()).readValueAs(String::class.java))!!,
                    node.get("value").traverse(p.objectReadContext()).readValueAs(String::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<FilterOption>() {
            override fun serialize(value: FilterOption, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                if (value.property != null)
                    gen.writeStringField("property", "${value.property}")
                gen.writeStringField("operator", value.operator.operator)
                gen.writeStringField("value", value.value?.toString())
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<FilterOption>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): FilterOption {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return FilterOption(
                    node.get("property").asText(),
                    FilterOperator.ofOperator(node.get("operator").asText())!!,
                    node.get("value").asText()
                )
            }
        }
    }

    /**
     * Retrieves the value of a specified property based on its name using reflection.
     *
     * This function operates as a delegated property getter and leverages a reflective approach
     * to convert an object's properties to a map and resolve the value of the requested property.
     *
     * - `property` - TYPE: `KProperty?`
     * - `operator` - TYPE: [FilterOperator]
     * - `value` - TYPE: `Any?`
     *
     * @param thisRef The object from which the property value is retrieved. Can be `null`.
     * @param property The metadata of the property being accessed.
     * @return The value of the property cast to the specified type `R`.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = memberPropertiesMap.toDataMap().getValue(property.name) as R

    /**
     * Returns a string representation of the FilterOption instance.
     *
     * The returned string includes details about the property, operator, and value.
     *
     * @return A string representation of the FilterOption in the format "FilterOption(property=<property_name>, operator=<operator>, value=<value>)".
     * @since 1.0.0
     */
    override fun toString(): String = "FilterOption(property=$property, operator=$operator, value=$value)"
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
 *                    in query syntax. It may contain placeholders like `{property}`, `{value}`,
 *                    or `{condition}` that can be replaced to build concrete expressions.
 * @property category Since 4.6.8, a category that groups filter operations based on their functionalities.
 *                    This property is used to categorize filter operations for better organization
 *                    and filtering logic management.
 * @property symbol   A symbol or shorthand notation for the operator, for visual representation only.
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
enum class FilterOperator(
    val operator: String,
    val sql: String,
    val category: Category,
    val symbol: String
) {
    /**
     * Represents the equality filter operation within the `FilterOperator` class.
     *
     * This operation is used to filter datasets or collections by comparing a property with a specific value
     * and returning only those elements where the property's value matches the provided value.
     *
     * The SQL representation of this operator is defined as "{property} = {value}".
     *
     * @since 4.0.0
     */
    Equals("eq", "{property} = {value}", Category.Equality, "="),
    /**
     * Represents the "not equals" filter operator in a query.
     *
     * The `NotEquals` operator is used to filter data where the value of a specified property
     * is not equal to a given value. This operator is commonly utilized in query-building
     * scenarios for data filtering purposes.
     *
     * @since 4.0.0
     */
    NotEquals("ne", "{property} <> {value}", Category.Equality, "≠"),
    /**
     * Represents the "in" operator used for filtering.
     *
     * The `In` operator is used to check if a value is present within a specific set of values provided in a filter.
     *
     * @since 4.0.0
     */
    In("in", "{value} IN {property}", Category.ArraySearch, "∈"),
    /**
     * Represents the `NOT IN` filter operator used to determine if a given value does not exist
     * within a specified set of values or collection.
     *
     * The operator is typically used in query generation or filtering workflows, where
     * conditions such as "value NOT IN (set)" need to be represented in a structured format.
     *
     * The `operator` ("nin") defines the symbolic representation of this filter operator, while
     * the `sql` pattern ("{value} NOT IN {property}") specifies how the operator should be formatted during translation to SQL or similar query languages.
     *
     * @since 4.0.0
     */
    NotIn("nin", "{value} NOT IN {property}", Category.ArraySearch, "∉"),
    /**
     * Represents a "less than" filter operation in a query or filtering system.
     *
     * The `LessThan` operation is used to evaluate whether a specific property's value
     * is less than the given comparison value. It is typically utilized in filtering scenarios
     * to include only records that satisfy the specified "less than" condition.
     *
     * @since 4.0.0
     */
    LessThan("lt", "{property} < {value}", Category.Comparison, "<"),
    /**
     * Represents a "greater than" filter operator for use in filtering operations.
     *
     * The `GreaterThan` filtering operator is used to identify and select records
     * where the value of a specified property is greater than a given comparison value.
     * This is particularly useful in query-building scenarios where conditional
     * filtering by property value is needed.
     *
     * The operator has a symbolic representation `>` for SQL and similar query languages.
     * @since 4.0.0
     */
    GreaterThan("gt", "{property} > {value}", Category.Comparison, ">"),
    /**
     * Represents a filtering operator for "greater than or equal to" comparisons.
     *
     * The `GreaterThan` filter operator is used to compare a property's value
     * to a specified value, evaluating to true if the property's value is greater than or equal
     * to the given value. It is commonly utilized in database queries or filtering mechanisms.
     *
     * @since 4.0.0
     */
    LessThanOrEquals("le", "{property} <= {value}", Category.Comparison, "≤"),
    /**
     * A filter operator representing the "greater than or equals" comparison.
     *
     * The `GreaterThanOrEquals` operator is used to filter data where a property value is greater than or equal to a specified value.
     * It is commonly employed in filtering criteria in data retrieval or query-building scenarios.
     *
     * @since 4.0.0
     */
    GreaterThanOrEquals("ge", "{property} >= {value}", Category.Comparison, "≥"),
    /**
     * Represents a `StartsWith` filter operation for querying datasets.
     *
     * The `StartsWith` filter checks if a property's value begins with the specified value.
     * This operator generates an SQL `LIKE` expression using the format:
     * `{property} LIKE CONCAT({value}, '%')`.
     *
     * @since 4.0.0
     */
    StartsWith("startswith", "{property} LIKE CONCAT({value}, '%')", Category.String, "starts with"),
    /**
     * Represents a filter operator that matches values where the specified property does not start with the given value.
     *
     * This operator constructs an SQL condition using the "NOT LIKE" clause with a wildcard (%) appended
     * to the provided value, ensuring that the property does not start with the specified pattern.
     *
     * @since 4.0.0
     */
    NotStartsWith("nstartswith", "{property} NOT LIKE CONCAT({value}, '%')", Category.String, "not starts with"),
    /**
     * Represents a filter operator that checks if the property value ends with the specified value.
     *
     * The `EndsWith` operator is translated into a SQL `LIKE` clause that matches records
     * where the property's value ends with the given string. This allows for filtering data based
     * on string suffix matching.
     *
     * Operator: `endswith`
     * SQL Translation: `{property} LIKE CONCAT('%', {value})`
     *
     * @since 4.0.0
     */
    EndsWith("endswith", "{property} LIKE CONCAT('%', {value})", Category.String, "ends with"),
    /**
     * Represents a filter operator that checks if a property's value does not end with the specified value.
     *
     * The `NotEndsWith` operator is used to construct an SQL condition using the "NOT LIKE" clause
     * with a wildcard (%) appended to the provided value, ensuring that the property's value does not end with the specified pattern.
     *
     * Operator: `notendwith`
     * SQL Translation: `{property} NOT LIKE CONCAT('%', {value})`
     *
     * @since 4.0.0
     */
    NotEndsWith("nendswith", "{property} NOT LIKE CONCAT('%', {value})", Category.String, "not ends with"),
    /**
     * An operator used for filtering data by checking if a property contains a specified value.
     *
     * `Contains` is used to construct SQL-like filter operations where the value
     * is checked to be a substring of the property's content. The operation format
     * is represented as `{property} LIKE CONCAT('%', {value}, '%')`.
     *
     * @since 4.0.0
     */
    Contains("contains", "{property} LIKE CONCAT('%', {value}, '%')", Category.String, "⊇"),
    /**
     * Represents a filter operator that checks if a property does not contain a specified value.
     *
     * The `NotContains` operator is used in filtering expressions to exclude records where
     * a specified property contains a particular substring. It constructs a SQL condition
     * using the `NOT LIKE` operator with wildcard patterns.
     *
     * For instance, given a property and a value, the SQL condition generated will look like:
     * `{property} NOT LIKE CONCAT('%', {value}, '%')`.
     *
     * This is useful for scenarios where you need to filter out records based on
     * partial matches within string property.
     *
     * @since 4.0.0
     */
    NotContains("ncontains", "{property} NOT LIKE CONCAT('%', {value}, '%')", Category.String, "⊉"),
    /**
     * Represents a SQL `LIKE` operator used for filtering data where a property matches a specified pattern.
     *
     * This operator is generally used in query conditions to perform pattern matching
     * against string data types. The comparison is case-sensitive in most SQL implementations.
     *
     * @since 4.0.0
     */
    Like("like", "{property} LIKE {value}", Category.String, "~"),
    /**
     * Represents an operator used for filtering entities based on a NOT LIKE condition.
     *
     * This class is part of the `FilterOperator` family, which defines various operations for data filtering.
     * The `NotLike` operator determines whether a specified property's value does not match a given pattern
     * using a SQL `NOT LIKE` expression.
     *
     * @since 4.0.0
     */
    NotLike("nlike", "{property} NOT LIKE {value}", Category.String, "≁");

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

        /**
         * Finds a FilterOperator that matches the given symbol.
         *
         * @param symbol The symbol to search for within the list of FilterOperator entries.
         * @return The matching FilterOperator if found, or null if no match is found.
         * @since 3.0.2
         */
        infix fun ofSymbol(symbol: String) = FilterOperator.entries.find { it.symbol equalsIgnoreCase symbol }
    }

    /**
     * Provides the first component of a destructured declaration for this object.
     *
     * Typically used in conjunction with destructuring declarations to extract specific 
     * values or properties of this object.
     *
     * @return The first component defined in this object.
     * @since 3.1.0
     */
    operator fun component1() = operator
    /**
     * Operator function that defines the `component2` functionality for destructuring declarations.
     * This function allows accessing the second component of an object.
     *
     * @return The value of the `sql` property.
     * @since 3.1.0
     */
    operator fun component2() = sql
    /**
     * Operator function that retrieves the third component of a destructured object.
     * This function is typically used in destructuring declarations to access the `category` property.
     *
     * @return The value of the `category` property.
     * @since 3.1.0
     */
    operator fun component3() = category
    /**
     * Deconstructs the object to retrieve its fourth component, typically representing the `symbol` property.
     * This operator function is intended to be used in destructuring declarations.
     *
     * @return The `symbol` property associated with this component.
     * @since 3.1.0
     */
    operator fun component4() = symbol

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
         * @since 4.0.0
         */
        Equality,
        /**
         * Represents the `COMPARISON` category used for comparing elements or entities.
         *
         * This category is typically used to define or represent operations that involve
         * comparing values, such as greater than, less than, or equality checks.
         * It is part of the `Category` enum class.
         *
         * @since 4.0.0
         */
        Comparison,
        /**
         * Represents the type of a category linked specifically to string operations.
         *
         * The `STRING` category is part of an enumerated classification system
         * meant to define groups of operations or functionalities related to strings.
         *
         * @since 4.0.0
         */
        String,
        /**
         * Represents an operation or concept related to searching within arrays.
         *
         * The `ARRAY_SEARCH` classification within the `Category` enumeration is utilized to denote functionality
         * or operations that deal specifically with array-based search algorithms or processes. This can include
         * equality-based searches, range searches, or complex patterns applied to arrays or list-like data structures.
         *
         * @since 4.0.0
         */
        ArraySearch
    }
}