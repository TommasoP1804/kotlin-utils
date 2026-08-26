/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("PaginationSortKt")
@file:Suppress("unused")
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
import dev.tommasop1804.kutils.classes.constants.*
import dev.tommasop1804.kutils.exceptions.*
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Represents an option for sorting a collection or dataset by a specific property and order.
 *
 * The `SortOption` class encapsulates a property to sort by and an enumerated direction specifying
 * the sorting direction (ascending or descending). It is commonly used in data retrieval or
 * manipulation scenarios where sorting is required.
 *
 * This class is serializable but not deserializable.
 *
 * @param property The name of the property to sort by.
 * @param direction The direction of sorting to apply. Defaults to [SortDirection.Ascending].
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@JsonSerialize(using = SortOption.Companion.Serializer::class)
@JsonDeserialize(using = SortOption.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = SortOption.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = SortOption.Companion.OldDeserializer::class)
data class SortOption(
    var property: String,
    var direction: SortDirection = SortDirection.Ascending
) {
    /**
     * Constructs a SortOption instance based on the provided property and sorting direction.
     *
     * The constructor processes the property to compute a unique identifier by appending the
     * simple name of the first parameter type (if available and non-null) to the property name. If
     * the simple name is null, the property name is used directly without modification.
     *
     * @param property The property that defines what to sort. Must be of type [KProperty].
     * @param direction The direction in which the sorting is to be performed. Defaults to [SortDirection.Ascending].
     * @since 1.0.0
     */
    constructor(property: KProperty<*>, direction: SortDirection = SortDirection.Ascending) : this(
        property.run {
            val type1 = (parameters.firstOrNull()?.type?.classifier as? KClass<*>)?.simpleName
            if (type1 != null) "$type1$${property.name}" else name
        },
        direction
    )

    /**
     * Constructs a new instance of the `SortOption` class with the specified property
     * and sorting direction. Validates the provided direction string and maps it to
     * the corresponding `SortDirection` value.
     *
     * @param property The name of the property to be used for sorting.
     * @param direction The sorting direction as a string, which is validated and
     *                  converted to a `SortDirection`. Throws an `IllegalArgumentException`
     *                  if the direction is invalid.
     * @throws IllegalArgumentException If an invalid direction string is provided.
     * @since 5.1.0
     */
    constructor(property: String, direction: String) : this(
        property,
        SortDirection.ofOperator(direction) ?: throw IllegalArgumentException("Invalid direction")
    )

    /**
     * Secondary constructor for the SortOption class.
     *
     * Initializes a SortOption instance with the provided property and sorting direction.
     *
     * @param property A [KProperty] representing the property for sorting.
     * @param direction A [String] representing the direction of sorting. Must be a valid sort direction.
     * @throws IllegalArgumentException If the provided direction string is invalid or cannot be mapped to a [SortDirection].
     * @since 5.1.0
     */
    constructor(property: KProperty<*>, direction: String) : this(
        property,
        SortDirection.ofOperator(direction) ?: throw IllegalArgumentException("Invalid direction")
    )

    /**
     * Constructs a [SortOption] instance by parsing the provided string.
     *
     * This constructor utilizes the `parse` method to process the input string,
     * extract the property name and sorting direction, and initialize the corresponding
     * properties of the `SortOption` instance.
     *
     * @param stringToParse The string to be parsed. It should follow the format "property:operator",
     * where `property` represents the property name and `operator` specifies the sorting direction.
     * @since 1.0.0
     */
    constructor(stringToParse: String) : this(parse(stringToParse).onlyElement())

    /**
     * Private constructor for creating a SortOption instance using another SortOption.
     *
     * This constructor allows the creation of a new SortOption instance by
     * copying the property and direction properties of an existing SortOption.
     *
     * @param sortOption The instance of SortOption from which the property and
     * direction are copied.
     * @since 1.0.0
     */
    private constructor(sortOption: SortOption) : this(sortOption.property, sortOption.direction)

    companion object {
        /**
         * Parses a variable number of string inputs and maps them to a list of SortOption instances.
         *
         * Each string input is split into two parts using a delimiter and trimmed.
         * The first part is used as the property name, and the second part is used to determine the sorting direction.
         * If the operator in the second part is invalid, an IllegalArgumentException is thrown.
         *
         * @param strings A variable number of string inputs, each representing a property and sorting operator separated by a separator.
         * @param separatorSymbol The regular expression pattern used to split the input strings. Defaults to a colon (:).
         * @return A list of SortOption instances created based on the input strings.
         * @throws IllegalArgumentException if an invalid operator is encountered in the input strings.
         * @throws MalformedInputException If the input string contains an invalid property name.
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
         * The first part is used as the property name, and the second part is used to determine the sorting direction.
         * If the operator in the second part is invalid, an IllegalArgumentException is thrown.
         *
         * @param strings A variable number of string inputs, each representing a property and sorting operator separated by a separator.
         * @param separatorSymbol The regular expression pattern used to split the input strings. Defaults to a colon (:).
         * @return A list of SortOption instances created based on the input strings.
         * @throws IllegalArgumentException if an invalid operator is encountered in the input strings.
         * @throws MalformedInputException If the input string contains an invalid property name.
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
                gen.writeStringProperty("property", value.property)
                gen.writeStringProperty("direction", value.direction.preferred)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<SortOption>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): SortOption {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return SortOption(
                    node.get("property").asString(),
                    SortDirection.ofOperator(node.get("direction").asString())!!
                )
            }
        }

        class OldSerializer : JsonSerializer<SortOption>() {
            override fun serialize(value: SortOption, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeStringField("property", value.property)
                gen.writeStringField("direction", value.direction.preferred)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<SortOption>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): SortOption {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return SortOption(
                    node.get("property").asText(),
                    SortDirection.ofOperator(node.get("direction").asText())!!
                )
            }
        }
    }

    /**
     * Provides the ability to retrieve the value of a property using the reflection-based mapping of property names to their values.
     *
     * - `property` - TYPE: [KProperty]
     * - `type` - TYPE: [SortDirection]
     *
     * @param R The expected return type of the property value.
     * @param thisRef The reference to the object from which the property value is obtained. Can be null.
     * @param property The property for which the value is retrieved.
     * @return The value of the property cast to the specified type `R`.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = memberPropertiesMap.toDataMap().getValue(property.name) as R

    /**
     * Returns a string representation of the SortOption instance.
     *
     * The returned string includes the property name and the sorting type.
     *
     * @return A string representation of the SortOption in the format "SortOption(property=<property_name>, type=<sorting_type>)".
     * @since 1.0.0
     */
    override fun toString(): String = "SortOption(property=$property, direction=$direction)"
}