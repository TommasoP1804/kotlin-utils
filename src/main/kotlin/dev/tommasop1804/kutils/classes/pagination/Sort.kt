@file:JvmName("PaginationSortKt")
@file:Suppress("unused")
@file:Since("1.0.0")

package dev.tommasop1804.kutils.classes.pagination

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.constants.SortDirection
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.isNotNull
import dev.tommasop1804.kutils.onlyElement
import dev.tommasop1804.kutils.splitAndTrim
import dev.tommasop1804.kutils.toReflectionMap
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonSerialize
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Represents an option for sorting a collection or dataset by a specific field and order.
 *
 * The `SortOption` class encapsulates a field to sort by and an enumerated direction specifying
 * the sorting direction (ascending or descending). It is commonly used in data retrieval or
 * manipulation scenarios where sorting is required.
 *
 * This class is serializable but not deserializable.
 *
 * @param field The name of the field to sort by.
 * @param direction The direction of sorting to apply. Defaults to [SortDirection.ASCENDING].
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@JsonSerialize(using = SortOption.Companion.Serializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = SortOption.Companion.OldSerializer::class)
data class SortOption(
    var field: String,
    var direction: SortDirection = SortDirection.ASCENDING
) {
    /**
     * Constructs a SortOption instance based on the provided field and sorting direction.
     *
     * The constructor processes the field property to compute a unique identifier by appending the
     * simple name of the first parameter type (if available and non-null) to the field name. If
     * the simple name is null, the field name is used directly without modification.
     *
     * @param field The property that defines what to sort. Must be of type [KProperty].
     * @param direction The direction in which the sorting is to be performed. Defaults to [SortDirection.ASCENDING].
     * @since 1.0.0
     */
    constructor(field: KProperty<*>, direction: SortDirection = SortDirection.ASCENDING) : this(
        field.run {
            val type1 = (parameters.firstOrNull()?.type?.classifier as? KClass<*>)?.simpleName
            if (type1.isNotNull()) "$type1$${field.name}" else name
        },
        direction
    )

    /**
     * Constructs a [SortOption] instance by parsing the provided string.
     *
     * This constructor utilizes the `parse` method to process the input string,
     * extract the field name and sorting direction, and initialize the corresponding
     * properties of the `SortOption` instance.
     *
     * @param stringToParse The string to be parsed. It should follow the format "field:operator",
     * where `field` represents the field name and `operator` specifies the sorting direction.
     * @since 1.0.0
     */
    constructor(stringToParse: String) : this(parse(stringToParse).onlyElement())

    /**
     * Private constructor for creating a SortOption instance using another SortOption.
     *
     * This constructor allows the creation of a new SortOption instance by
     * copying the field and direction properties of an existing SortOption.
     *
     * @param sortOption The instance of SortOption from which the field and
     * direction are copied.
     * @since 1.0.0
     */
    private constructor(sortOption: SortOption) : this(sortOption.field, sortOption.direction)
    
    companion object {
        /**
         * Parses a variable number of string inputs and maps them to a list of SortOption instances.
         *
         * Each string input is split into two parts using a delimiter and trimmed.
         * The first part is used as the field name, and the second part is used to determine the sorting direction.
         * If the operator in the second part is invalid, an IllegalArgumentException is thrown.
         *
         * @param strings A variable number of string inputs, each representing a field and sorting operator separated by a separator.
         * @param separatorSymbol The regular expression pattern used to split the input strings. Defaults to a colon (:).
         * @return A list of SortOption instances created based on the input strings.
         * @throws IllegalArgumentException if an invalid operator is encountered in the input strings.
         * @throws MalformedInputException If the input string contains an invalid field name.
         * @since 1.0.0
         */
        fun parse(vararg strings: String, separatorSymbol: Regex = Regex(":")) = strings.map {
            val list = it.splitAndTrim(separatorSymbol, limit = 2)
            list.size == 2 || throw MalformedInputException("Invalid sort option string: $it")
            SortOption(
                list.first(),
                SortDirection.ofOperator(list[1]) ?: throw IllegalArgumentException("Invalid operator"),
            )
        }
        /**
         * Parses a variable number of string inputs and maps them to a list of SortOption instances.
         *
         * Each string input is split into two parts using a separator delimiter and trimmed.
         * The first part is used as the field name, and the second part is used to determine the sorting direction.
         * If the operator in the second part is invalid, an IllegalArgumentException is thrown.
         *
         * @param strings A variable number of string inputs, each representing a field and sorting operator separated by a separator.
         * @param separatorSymbol The regular expression pattern used to split the input strings. Defaults to a colon (:).
         * @return A list of SortOption instances created based on the input strings.
         * @throws IllegalArgumentException if an invalid operator is encountered in the input strings.
         * @throws MalformedInputException If the input string contains an invalid field name.
         * @since 1.0.0
         */
        fun parse(strings: Iterable<String>, separatorSymbol: Regex = Regex(":")) = parse(*strings.toList().toTypedArray(), separatorSymbol = separatorSymbol)

        class Serializer : ValueSerializer<SortOption>() {
            override fun serialize(
                value: SortOption,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                gen.writeStringProperty("field", value.field)
                gen.writeStringProperty("direction", value.direction.name)
                gen.writeEndObject()
            }
        }

        class OldSerializer : JsonSerializer<SortOption>() {
            override fun serialize(value: SortOption, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeStringField("field", value.field)
                gen.writeStringField("direction", value.direction.name)
                gen.writeEndObject()
            }
        }
    }

    /**
     * Provides the ability to retrieve the value of a property using the reflection-based mapping of property names to their values.
     *
     * - `field` - TYPE: [KProperty]
     * - `type` - TYPE: [SortDirection]
     *
     * @param R The expected return type of the property value.
     * @param thisRef The reference to the object from which the property value is obtained. Can be null.
     * @param property The property for which the value is retrieved.
     * @return The value of the property cast to the specified type `R`.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = toReflectionMap().getValue(property.name) as R

    /**
     * Returns a string representation of the SortOption instance.
     *
     * The returned string includes the field name and the sorting type.
     *
     * @return A string representation of the SortOption in the format "SortOption(field=<field_name>, type=<sorting_type>)".
     * @since 1.0.0
     */
    override fun toString(): String = "SortOption(field=$field, direction=$direction)"
}