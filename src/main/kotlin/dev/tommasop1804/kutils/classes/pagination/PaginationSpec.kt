/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.pagination

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import tools.jackson.databind.*

/**
 * Represents the specification for pagination, including filter and sort parameters.
 * @since 4.5.0
 * @author Tommaso Pastorelli
 */
interface PaginationSpec {
    /**
     * Represents a collection of filter criteria to apply to a dataset or query.
     * Each filter is encapsulated as an instance of `FilterOption`, specifying
     * the filtering condition and value.
     *
     * This variable is typically used to dynamically compose filtering logic
     * based on user input or predefined parameters.
     * @since 4.5.0
     */
    val filter: List<FilterOption>
    /**
     * Represents the sorting options to be applied when fetching paginated data.
     * Provides a list of criteria defining the sorting order of the results.
     * @since 4.5.0
     */
    val sort: List<SortOption>
}

/**
 * Represents a specification for paginated requests, including page number, limit, filter options, and sort options.
 *
 * @property page The page number to be retrieved. Defaults to 0. Must be a non-negative integer.
 * @property limit The maximum number of items to be returned per page. Can be null, -1 for unlimited, or a positive integer.
 * @property filter A list of filter options to apply to the paginated data. Defaults to an empty list.
 * @property sort A list of sorting options to define the order of the paginated data. Defaults to an empty list.
 * @since 4.5.0
 * @author Tommaso Pastorelli
 */
open class PageLimitPaginationSpec(
    val page: Int = 0,
    limit: Int? = null,
    override val filter: List<FilterOption> = emptyList(),
    override val sort: List<SortOption> = emptyList(),
) : PaginationSpec {

    val limit: Int?

    init {
        page.validateNotNegative(::page)
        limit.validate(::limit, message = "must be null, -1 or positive") {
            it.isNull() || it == -1 || it.isPositive
        }
        this.limit = limit.letIf(limit == -1) { null }
    }

    companion object {
        class Serializer : ValueSerializer<PageLimitPaginationSpec>() {
            override fun serialize(
                value: PageLimitPaginationSpec,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                gen.writeNumberProperty("page", value.page)
                if (value.limit.isNull()) gen.writeNullProperty("limit") else gen.writeNumberProperty("limit", value.limit)
                gen.writeArrayPropertyStart("filter")
                value.filter.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeArrayPropertyStart("sort")
                value.sort.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<PageLimitPaginationSpec>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): PageLimitPaginationSpec {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                val tf = ctxt.typeFactory
                return PageLimitPaginationSpec(
                    node.get("page").asInt(),
                    node.get("limit")?.takeUnless { it.isNull }?.asInt(),
                    ctxt.readTreeAsValue(node.get("filter"), tf.constructCollectionType(List::class.java, FilterOption::class.java)),
                    ctxt.readTreeAsValue(node.get("sort"), tf.constructCollectionType(List::class.java, SortOption::class.java)),
                )
            }
        }

        class OldSerializer : JsonSerializer<PageLimitPaginationSpec>() {
            override fun serialize(value: PageLimitPaginationSpec, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeNumberField("page", value.page)
                if (value.limit.isNull()) gen.writeNullField("limit") else gen.writeNumberField("limit", value.limit)
                gen.writeArrayFieldStart("filter")
                value.filter.forEach { gen.writeObject(it) }
                gen.writeEndArray()
                gen.writeArrayFieldStart("sort")
                value.sort.forEach { gen.writeObject(it) }
                gen.writeEndArray()
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<PageLimitPaginationSpec>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): PageLimitPaginationSpec {
                val node = ctxt.readTree(p)
                val tf = ctxt.typeFactory
                return PageLimitPaginationSpec(
                    node.get("page").asInt(),
                    node.get("limit")?.takeIf { !it.isNull }?.asInt(),
                    ctxt.readTreeAsValue(node.get("filter"), tf.constructCollectionType(List::class.java, FilterOption::class.java)),
                    ctxt.readTreeAsValue(node.get("sort"), tf.constructCollectionType(List::class.java, SortOption::class.java)),
                )
            }
        }
    }

    /**
     * Compares this object with the specified object for equality.
     *
     * @param other the object to be compared for equality with this object.
     * @return `true` if the specified object is equal to this object, `false` otherwise.
     * @since 4.5.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PageLimitPaginationSpec

        if (page != other.page) return false
        if (limit != other.limit) return false
        if (filter != other.filter) return false
        if (sort != other.sort) return false

        return true
    }

    /**
     * Computes the hash code for this instance of the class.
     * The hash code is calculated based on the values of the `page`, `limit`, `filter`, and `sort` properties.
     *
     * @return an integer hash code value for the object.
     * @since 4.5.0
     */
    override fun hashCode(): Int {
        var result = page
        result = 31 * result + limit.hashCode()
        result = 31 * result + filter.hashCode()
        result = 31 * result + sort.hashCode()
        return result
    }

    /**
     * Returns a string representation of the PaginationSpec object, including its page, limit, filter, and sort values.
     * @return A string representation of the PaginationSpec instance.
     * @since 4.5.0
     */
    override fun toString(): String {
        return "PaginationSpec(page=$page, limit=$limit, filter=$filter, sort=$sort)"
    }

    /**
     * Deconstructs the object to provide the `page` property.
     * This operator function allows support for destructuring declarations
     * by returning the `page` value when invoked as the first component.
     * @since 4.5.0
     */
    operator fun component1() = page
    /**
     * Operator function `component2` which enables destructuring declarations to access the second component.
     * Typically used in scenarios where the class this function belongs to supports destructuring,
     * and this function corresponds to the second value in the pair or data set.
     *
     * @return The `limit` value associated with this component.
     * @since 4.5.0
     */
    operator fun component2() = limit
    /**
     * A component operator function that allows destructuring declarations to access the third component.
     *
     * @return The value of `filter`, representing the third component in the destructuring process.
     * @since 4.5.0
     */
    operator fun component3() = filter
    /**
     * Decomposes this object to its fourth component.
     * This operator function is typically used in destructuring declarations
     * to retrieve the fourth property of the object.
     *
     * @return The value of the fourth component, represented by `sort`.
     * @since 4.5.0
     */
    operator fun component4() = sort
}

/**
 * Represents a pagination specification using a cursor-based approach.
 * This class is typically used to define pagination parameters for query results.
 *
 * @property cursor An optional value representing the starting point for fetching the results.
 *                  This can be any type and is often used as a point of reference to continue fetching records.
 * @property limit An optional value that indicates the maximum number of items to fetch.
 *                Valid values are `null`, `-1` for no limit, or a positive integer.
 * @property filter A list of filtering criteria applied to the results. Each filter is defined as a `FilterOption`.
 * @property sort A list of sorting criteria applied to the results. Each sort option is defined as a `SortOption`.
 * @since 4.5.0
 * @author Tommaso Pastorelli
 */
open class CursorPaginationSpec(
    val cursor: Any? = null,
    limit: Int? = null,
    override val filter: List<FilterOption> = emptyList(),
    override val sort: List<SortOption> = emptyList(),
) : PaginationSpec {

    val limit: Int?

    init {
        limit.validate(::limit, message = "must be null, -1 or positive") {
            it.isNull() || it == -1 || it.isPositive
        }
        this.limit = limit.letIf(limit == -1) { null }
    }

    companion object {
        class Serializer : ValueSerializer<CursorPaginationSpec>() {
            override fun serialize(
                value: CursorPaginationSpec,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                if (value.cursor.isNull()) gen.writeNullProperty("cursor") else gen.writeStringProperty(
                    "cursor",
                    value.cursor.toString()
                )
                if (value.limit.isNull()) gen.writeNullProperty("limit") else gen.writeNumberProperty(
                    "limit",
                    value.limit
                )
                gen.writeArrayPropertyStart("filter")
                value.filter.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeArrayPropertyStart("sort")
                value.sort.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<CursorPaginationSpec>() {
            override fun deserialize(
                p: tools.jackson.core.JsonParser,
                ctxt: DeserializationContext
            ): CursorPaginationSpec {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                val tf = ctxt.typeFactory
                return CursorPaginationSpec(
                    node.get("cursor")?.takeUnless { it.isNull }?.asString(),
                    node.get("limit")?.takeUnless { it.isNull }?.asInt(),
                    ctxt.readTreeAsValue(
                        node.get("filter"),
                        tf.constructCollectionType(List::class.java, FilterOption::class.java)
                    ),
                    ctxt.readTreeAsValue(
                        node.get("sort"),
                        tf.constructCollectionType(List::class.java, SortOption::class.java)
                    ),
                )
            }
        }

        class OldSerializer : JsonSerializer<CursorPaginationSpec>() {
            override fun serialize(value: CursorPaginationSpec, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                if (value.cursor.isNull()) gen.writeNullField("cursor") else gen.writeStringField(
                    "cursor",
                    value.cursor.toString()
                )
                if (value.limit.isNull()) gen.writeNullField("limit") else gen.writeNumberField("limit", value.limit)
                gen.writeArrayFieldStart("filter")
                value.filter.forEach { gen.writeObject(it) }
                gen.writeEndArray()
                gen.writeArrayFieldStart("sort")
                value.sort.forEach { gen.writeObject(it) }
                gen.writeEndArray()
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<CursorPaginationSpec>() {
            override fun deserialize(
                p: JsonParser,
                ctxt: com.fasterxml.jackson.databind.DeserializationContext
            ): CursorPaginationSpec {
                val node = ctxt.readTree(p)
                val tf = ctxt.typeFactory
                return CursorPaginationSpec(
                    node.get("cursor")?.takeUnless { it.isNull }?.asText(),
                    node.get("limit")?.takeUnless { it.isNull }?.asInt(),
                    ctxt.readTreeAsValue(
                        node.get("filter"),
                        tf.constructCollectionType(List::class.java, FilterOption::class.java)
                    ),
                    ctxt.readTreeAsValue(
                        node.get("sort"),
                        tf.constructCollectionType(List::class.java, SortOption::class.java)
                    ),
                )
            }
        }
    }

    /**
     * Compares this object with the specified object for equality.
     *
     * @param other the object to be compared for equality with this object.
     * @return `true` if the specified object is equal to this object, `false` otherwise.
     * @since 4.5.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CursorPaginationSpec

        if (cursor != other.cursor) return false
        if (limit != other.limit) return false
        if (filter != other.filter) return false
        if (sort != other.sort) return false

        return true
    }

    /**
     * Generates a hash code for the `CursorPaginationSpec` instance based on its properties.
     *
     * @return the hash code value of the object.
     * @since 4.5.0
     */
    override fun hashCode(): Int {
        var result = limit.hashCode()
        result = 31 * result + cursor.hashCode()
        result = 31 * result + filter.hashCode()
        result = 31 * result + sort.hashCode()
        return result
    }

    /**
     * Returns a string representation of the `CursorPaginationSpec` instance, including its properties.
     *
     * @return a string containing the `cursor`, `limit`, `filter`, and `sort` values of the object.
     * @since 4.5.0
     */
    override fun toString(): String {
        return "CursorPaginationSpec(cursor=$cursor, limit=$limit, filter=$filter, sort=$sort)"
    }

    /**
     * Returns the value of the `cursor` property. This function is part of the
     * decomposition mechanism in Kotlin, allowing the use of destructuring declarations
     * on an instance of the `CursorPaginationSpec` class.
     *
     * @return the value of the `cursor` field.
     * @since 4.5.0
     */
    operator fun component1() = cursor

    /**
     * Provides the second component of the CursorPaginationSpec instance when destructured.
     * Typically represents the `limit` property of the object.
     *
     * @return the value of the `limit` property.
     * @since 4.5.0
     */
    operator fun component2() = limit

    /**
     * Provides the third component of the `CursorPaginationSpec` instance.
     * This operator function is typically used for destructuring declarations.
     *
     * @return the value of the `filter` property associated with this instance.
     * @since 4.5.0
     */
    operator fun component3() = filter

    /**
     * Provides the fourth component of the `CursorPaginationSpec` instance.
     * This operator function is commonly used in destructuring declarations.
     *
     * @return the `sort` property of the `CursorPaginationSpec` instance.
     * @since 4.5.0
     */
    operator fun component4() = sort
}