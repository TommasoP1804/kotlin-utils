/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.pagination

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.constants.*
import dev.tommasop1804.kutils.classes.constants.SortDirection.Companion.ofOperator
import dev.tommasop1804.kutils.classes.pagination.FilterOperator.*
import dev.tommasop1804.kutils.classes.pagination.FilterOperator.Category.*
import dev.tommasop1804.kutils.classes.pagination.FilterOperator.Companion.byCategory
import dev.tommasop1804.kutils.dsl.sql.*
import dev.tommasop1804.kutils.exceptions.*
import jakarta.persistence.EntityManager
import net.sf.jsqlparser.parser.feature.Feature
import org.slf4j.Logger
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonSerialize
import kotlin.math.ceil
import kotlin.reflect.KProperty
import kotlin.reflect.full.NoSuchPropertyException
import kotlin.reflect.full.memberProperties

/**
 * Represents a chunked data set along with pagination, filtering, and sorting details.
 *
 * @param T The type of the data contained in the chunks.
 * @property totalPages The total number of pages in the chunked data.
 * @property pageIndex The current page index.
 * @property totalElements The total number of items in the chunked data.
 * @property limit The maximum number of items per page.
 * @property appliedFilters The list of filters applied to the data.
 * @property sort The sorting criteria applied to the data.
 * @property data The list of items in the current chunk.
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@Suppress("unused")
@JsonSerialize(using = Chunked.Companion.Serializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Chunked.Companion.OldSerializer::class)
@MustUseReturnValues
data class Chunked<T>(
    val totalPages: Int? = null,
    val pageIndex: Int? = null,
    @param:Since("2.10.7") val totalElements: Int? = null,
    val limit: Int? = null,
    val appliedFilters: Collection<FilterOption> = emptyList(),
    val sort: Collection<SortOption> = emptyList(),
    val data: List<T>?
) {
    companion object {
        /**
         * Generates a paginated dataset (`Chunked`) based on the given SQL query and parameters for filtering, sorting, and pagination.
         * Utilizes various filtering and transformation mechanisms to construct the result.
         *
         * Require a SqlDsl context, that can be created using [initSql] method.
         *
         * @param T The type of elements being returned in the resulting `Chunked` object.
         * @param callableName The invoking function reference, used for validation error reporting.
         * @param logger A logger instance used to log debug information.
         * @param entityManager The `EntityManager` instance used for executing SQL queries.
         * @param offset The offset or page index for pagination (default is 0).
         * @param limit The maximum number of elements per page (default is 10).
         * @param filter A collection of filter strings to apply for dataset refinement (default is an empty list).
         * @param sorting A collection of sorting options in the form of strings (default is an empty list).
         * @param dtoMapper A transformer to map individual database results (`DataMap`) into output objects of type `T`.
         * @param availGeneralFilteringFields A set of fields eligible for generalized filtering (default is an empty set).
         * @param availFilteringFields A set of specific fields allowed for filtering (default is an empty set).
         * @param availSortingFields A set of specific fields allowed for sorting (default is an empty set).
         * @param dbDictionary A mapping between field aliases and their database representations (default is an empty map).
         * @param dateFields A set of fields representing date values in the database (default is an empty set).
         * @param dateTimeFields A set of fields representing datetime values (default is an empty set).
         * @param dateTimeWithZoneFields A set of fields representing datetime values with time zone information (default is an empty set).
         * @param exceptionForInvalid A transformer function converting invalid input strings into custom exceptions.
         * @return A `Chunked<T>` object containing the paginated dataset, total pages, total elements, applied filters, sorting, and metadata.
         * @since 1.0.0
         */
        @OptIn(ConditionNotPreventingExceptions::class)
        @Suppress("SqlNoDataSourceInspection")
        context(context: SqlBuilder)
        inline fun <reified M : Any, T> generateFromQuery(
            callableName: String? = null,
            logger: Logger = Logger(),
            entityManager: EntityManager,
            offset: Int = 0,
            limit: Int? = null,
            filter: List<String> = emptyList(),
            sorting: List<String> = emptyList(),
            crossinline dtoMapper: Transformer<M, T>,
            availGeneralFilteringFields: Set<String> = emptySet(),
            availFilteringFields: Set<String> = emptySet(),
            availSortingFields: Set<String> = emptySet(),
            dbDictionary: StringMap = emptyMap(),
            dateFields: Set<String> = emptySet(),
            dateTimeFields: Set<String> = emptySet(),
            dateTimeWithZoneFields: Set<String> = emptySet(),
            crossinline exceptionForInvalid: Transformer<String, Throwable>,
            separatorSymbol: Regex = Regex(":"),
            generalFilterSymbol: String = String.STAR
        ): Chunked<T> {
            offset.validate(
                "generateFromQuery",
                "offset",
                message = "Offset must be greater than or equal to 0",
                causeOf = { exceptionForInvalid("Offset must be greater than or equal to 0") }
            ) { it >= 0 }

            val limit = limit ?: -1
            limit.validate(
                "generateFromQuery",
                "limit",
                message = "Limit must be greater than 0 or equal to -1",
                causeOf = { exceptionForInvalid("Limit must be greater than 0 or equal to -1") }
            ) { it > 0 || it == -1 }

            val generalFilterString = filter.find { (it / separatorSymbol).first() == generalFilterSymbol }
            val otherFilters = generalFilterString?.run { filter - this } ?: filter

            // GENERAL FILTER
            if (generalFilterString.isNotNull()) {
                val generalFilter = FilterOption.parse(generalFilterString).onlyElement()
                generalFilter.operator.validate(
                    lazyMessage = { "General filter operator in ${((FilterOperator byCategory Category.String) + (FilterOperator byCategory Equality)).map(FilterOperator::operator)}" },
                    causeOf = { exceptionForInvalid("General filter operator is invalid") }
                ) { it.category in arrayOf(String, Equality) }

                val getCondition = { it: String -> generalFilter.operator.sql(
                    "LOWER(CAST($it AS TEXT))",
                    "'${-generalFilter.value.toString()}'"
                ) }
                val generalFiltrable = dbDictionary.keys
                    .filter { it !in availGeneralFilteringFields }
                    .map { dbDictionary[it]!! }
                    .joinToString(transform = getCondition, separator = " OR ")

                context.where("($generalFiltrable)")
            }

            // OTHERS
            val parsedFilters = FilterOption.parse(otherFilters, separatorSymbol = separatorSymbol)
            parsedFilters.groupBy(FilterOption::field).forEach { parsedFilter ->
                val orFilters = emptyMList<String>()
                parsedFilter.value.forEach {
                    it.field.validate(
                        predicate = availFilteringFields::contains,
                        callableName = callableName,
                        parameterName = "sorting",
                        message = "in $availFilteringFields",
                        causeOf = { exceptionForInvalid("The sorting field $sorting is not supported.") }
                    )
                    if (it.value.isNotNull()) {
                        orFilters += it.operator.sql(
                            if (it.operator.category in arrayOf(String, Equality))
                                "LOWER(CAST(${(dbDictionary[it.field] ?: it.field)} AS TEXT))"
                            else (dbDictionary[it.field] ?: it.field),
                            when (it.operator.category)  {
                                Category.String, Equality -> "'${it.value?.toString()?.lowercase()}'"
                                Comparison if it.field in dateFields -> {
                                    if (YearMonth(it.value!!.toString()).isSuccess)
                                        "TO_DATE('${it.value.toString()}', 'YYYY-MM')"
                                    else "CAST('${it.value.toString()}' AS DATE)"
                                }
                                Comparison if it.field in dateTimeFields -> {
                                    if (YearMonth(it.value!!.toString()).isSuccess)
                                        "TO_DATE('${it.value.toString()}', 'YYYY-MM')"
                                    else "CAST('${it.value.toString()} ${if (it.operator in setOf(GreaterThan, GreaterThanOrEquals)) "00:00:00" else "23:59:59"}' AS TIMESTAMP)"
                                }
                                Comparison if it.field in dateTimeWithZoneFields -> {
                                    "CAST('${it.value.toString()} ${if (it.operator in setOf(GreaterThan, GreaterThanOrEquals)) "00:00:00" else "23:59:59"}' AS TIMESTAMP WITH TIME ZONE)"
                                }
                                else -> it.value.toString()
                            }
                        )
                    }
                }
                context.where("(${orFilters.onlyElementOr { orFilters.joinToString(" OR ") } })")
            }

            if (sorting.isNotEmpty()) {
                context.orderBy(*sorting.map {
                    val splitted = it / separatorSymbol
                    (dbDictionary[splitted.first()] ?: splitted.first()) to ((SortDirection ofOperator splitted[1])
                        ?: throw exceptionForInvalid("Invalid sorting direction"))
                }.toTypedArray())
            }
            val query = if (limit == -1) context.build() else context.apply {
                range(limit * offset ..< limit * offset + limit)
            }.build()

            val totalElements = (limit == -1)(onTrue = {
                with(entityManager) {
                    buildSql {
                        selectCount()
                        from("(${query.value.replaceAfter("LIMIT", String.EMPTY) - "LIMIT".length})")
                    }.executeCount()
                }
            })

            logger.debug("<> Query from ${Ansi.ITALIC}${this::class.simpleName}${Ansi.RESET}: {}", query.value)

            val filtered = with(entityManager) { query.executeSelectMultipleResult<M>().map(dtoMapper) }
            return Chunked(
                totalElements = totalElements ?: filtered.size,
                totalPages = if (limit == -1) 1 else ceil((totalElements ?: filtered.size).toDouble() / limit).toInt(),
                pageIndex = offset,
                limit = limit whenFalse (limit == -1),
                appliedFilters = parsedFilters + if (generalFilterString.isNotNull()) FilterOption(
                    operator = (generalFilterString / separatorSymbol)[1],
                    value = (generalFilterString / separatorSymbol)[2]
                ).asSingleList() else emptyList(),
                sort = SortOption.parse(sorting, separatorSymbol = separatorSymbol),
                data = filtered
            )
        }

        /**
         * Processes and filters a given base collection based on input parameters such as offset, limit, filtering, and sorting criteria.
         * Returns a chunked dataset encapsulating the filtered and sorted result.
         *
         * WARNING: Comparison filter operator are NOT supported for this method.
         *
         * @param T The type of elements in the base collection.
         * @param baseCollection The source collection from which the chunked result will be generated.
         * @param offset The starting index (zero-based) for the chunked result. Must be greater than or equal to 0.
         * @param limit The maximum number of elements to include in the chunked result. Use -1 for no limit. Must be greater than 0 or equal to -1.
         * @param filter A list of string-based filter expressions to be applied to the collection. The filters are parsed and validated against available fields.
         * @param sorting A list of string-based sorting expressions defining the order of elements in the chunked result. The sorting fields are validated against `availSortingFields
         * `.
         * @param availGeneralFilteringFields The set of fields that can be used for general filtering.
         * @param availFilteringFields The set of fields that are supported for specific filtering.
         * @param availSortingFields The set of fields that are supported for sorting.
         * @param exceptionForInvalid A transformer function to generate custom exceptions for invalid parameters or unsupported operations.
         * @param separatorSymbol A regex defining the separator used in parsing filter and sorting expressions.
         * @param generalFilterSymbol A string representing the special field used for general filtering.
         * @return A `Chunked` instance containing the filtered and sorted dataset, along with metadata such as total elements, applied filters, and sorting information.
         * @throws ValidationFailedException if `offset` or `limit` have invalid values.
         * @throws NoSuchPropertyException if a referenced filtering or sorting field does not exist in the element type.
         * @throws Throwable if an invalid filter or sorting expression is encountered, as determined by `exceptionForInvalid`.
         * @since 1.0.0
         */
        @OptIn(ConditionNotPreventingExceptions::class)
        @ComparisonOperatorUnsupported
        inline fun <reified T : Any> generateFromBaseCollection(
            baseCollection: Collection<T>,
            offset: Int = 0,
            limit: Int? = null,
            filter: List<String> = emptyList(),
            sorting: List<String> = emptyList(),
            availGeneralFilteringFields: Set<String> = emptySet(),
            availFilteringFields: Set<String> = emptySet(),
            availSortingFields: Set<String> = emptySet(),
            crossinline exceptionForInvalid: Transformer<String, Throwable>,
            separatorSymbol: Regex = Regex(":"),
            generalFilterSymbol: String = String.STAR
        ): Chunked<T> {
            offset.validate(
                "generateFromCollection",
                "offset",
                message = "Offset must be greater than or equal to 0",
                causeOf = { exceptionForInvalid("Offset must be greater than or equal to 0") }
            ) { it >= 0 }

            val limit = limit ?: -1
            limit.validate(
                "generateFromCollection",
                "limit",
                message = "Limit must be greater than 0 or equal to -1",
                causeOf = { exceptionForInvalid("Limit must be greater than 0 or equal to -1") }
            ) { it > 0 || it == -1 }

            var decomponedFilters = filter.map {
                val decomponed = it / separatorSymbol
                FilterOption(decomponed[0], decomponed[1], decomponed[2])
            }
            val generalFilter = decomponedFilters[{ it.field == generalFilterSymbol }]
            if (generalFilter.isNotNull()) decomponedFilters = decomponedFilters - generalFilter

            var baseCollection = baseCollection.toList()
            val sorting = SortOption.parse(sorting, separatorSymbol = separatorSymbol)
            sorting.all { it.field in availSortingFields } || throw exceptionForInvalid("Sorting field not supported")
            if (sorting.isNotEmpty() && baseCollection.isNotEmpty()) {
                val property = baseCollection.first()::class.memberProperties[{ it.name == sorting.first().field }] ?: throw NoSuchPropertyException()
                var comparator = if (sorting.first().direction == SortDirection.Descending)
                    compareByDescending<T> { property.call(it) as Comparable<*>? }
                else compareBy { property.call(it) as Comparable<*>? }
                for (sortOption in (-1)(sorting)) {
                    val property = baseCollection.first()::class.memberProperties[{ it.name == sortOption.field }] ?: throw NoSuchPropertyException()
                    comparator = if (sortOption.direction == SortDirection.Descending)
                        comparator.thenByDescending { property.call(it) as Comparable<*>? }
                    else comparator.thenBy { property.call(it) as Comparable<*>? }
                }
                baseCollection = baseCollection.sortedWith(comparator)
            }

            var goodCollection = emptyMList<T>()
            element@ for (element in baseCollection) {
                val properties = element::class.memberProperties
                var flag: Boolean? = null
                general@ for (property in properties) {
                    if (generalFilter.isNotNull() && property.name in availGeneralFilteringFields) {
                        flag = false
                        if (filter(property.call(element).toString(), generalFilter, exceptionForInvalid)) {
                            flag = true
                            break@general
                        }
                    }
                }
                if (flag == false) continue@element
                for (property in properties) {
                    if (decomponedFilters.isNotEmpty() && property.name in availFilteringFields)
                        decomponedFilters { it.field == property.name }.forEach {
                            it.field in availFilteringFields || throw exceptionForInvalid("Filtering field not supported")
                            if (!filter(property.call(element).toString(), it, exceptionForInvalid)) {
                                continue@element
                            }
                        }
                }
                goodCollection += element
            }
            val totalElements = goodCollection.size
            goodCollection = tryOr({ emptyMList() }, overwriteOnly = IndexOutOfBoundsException::class) {
                if (limit == -1) goodCollection else (goodCollection.toList() % limit)[offset].toMList()
            }

            return Chunked(
                totalElements = totalElements,
                totalPages = if (limit == -1) 1 else ceil(totalElements.toDouble() / limit).toInt(),
                pageIndex = offset,
                limit = limit whenFalse (limit == -1),
                appliedFilters = decomponedFilters.map { it.copy(field = "${T::class.simpleName}$${it.field}") } + generalFilter?.copy(field = null)?.asSingleList().orEmpty(),
                sort = sorting,
                data = goodCollection
            )
        }

        @InternalScope
        inline fun filter(value: String, fliterOption: FilterOption, exceptionForInvalid: Transformer<String, Throwable>) = when (fliterOption.operator) {
            Equals -> value equalsIgnoreCase fliterOption.value.toString()
            NotEquals -> value notEqualsIgnoreCase fliterOption.value.toString()
            In -> tryOr({ false }) { value inIgnoreCase (fliterOption.value as Iterable<*>).joinToString() }
            NotIn -> tryOr({ false }) { value notInIgnoreCase (fliterOption.value as Iterable<*>).joinToString() }
            StartsWith -> value startsWithIgnoreCase fliterOption.value.toString()
            NotStartsWith -> value startsWithIgnoreCase fliterOption.value.toString()
            EndsWith -> value endsWithIgnoreCase fliterOption.value.toString()
            NotEndsWith -> value endsWithIgnoreCase fliterOption.value.toString()
            Contains -> value containsIgnoreCase fliterOption.value.toString()
            NotContains -> value notContainsIgnoreCase fliterOption.value.toString()
            else -> throw exceptionForInvalid("Operator ${fliterOption.operator} not supported.")
        }

        class Serializer : ValueSerializer<Chunked<*>>() {
            override fun serialize(value: Chunked<*>, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                value.totalElements.ifNotNull { gen.writeNumberProperty("totalElements", this) }
                value.totalPages.ifNotNull { gen.writeNumberProperty("totalPages", this) }
                value.pageIndex.ifNotNull { gen.writeNumberProperty("pageIndex", this) }
                value.limit.ifNotNull { gen.writeNumberProperty("limit", this) }
                gen.writeArrayPropertyStart("appliedFilters")
                value.appliedFilters.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeArrayPropertyStart("sort")
                value.sort.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeArrayPropertyStart("data")
                value.data?.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeEndObject()
            }
        }

        class OldSerializer : JsonSerializer<Chunked<*>>() {
            override fun serialize(value: Chunked<*>, gen: JsonGenerator, serializers: SerializerProvider?) {
                gen.writeStartObject()
                value.totalElements.ifNotNull { gen.writeNumberField("totalElements", this) }
                value.totalPages.ifNotNull { gen.writeNumberField("totalPages", this) }
                value.pageIndex.ifNotNull { gen.writeNumberField("pageIndex", this) }
                value.limit.ifNotNull { gen.writeNumberField("limit", this) }
                gen.writeArrayFieldStart("appliedFilters")
                value.appliedFilters.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeArrayFieldStart("sort")
                value.sort.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeArrayFieldStart("data")
                value.data?.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeEndObject()
            }
        }
    }

    /**
     * Retrieves the value associated with the specified property name using reflection.
     * The property is accessed within the context of the current object and cast to the expected type.
     *
     * - `totalPages` - TYPE: [Int]
     * - `actualPage` - TYPE: [Int]
     * - `limit` - TYPE: [Int]
     * - `appliedFilters` - TYPE: `Collection<FilterOption<T>>`
     * - `sort` - TYPE: `Collection<SortOption>`
     * - `data` - TYPE: `List<T>`
     *
     * @param thisRef The object on which the property exists. Can be `null` if not needed.
     * @param property The property whose value needs to be retrieved.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = memberPropertiesMap.toDataMap().getValue(property.name) as R

    /**
     * Returns a string representation of the current Chunked instance.
     *
     * The string includes the values of the following properties:
     * - `totalPages`: The total number of pages.
     * - `pageIndex`: The current page index.
     * - `totalElements`: The total number of elements.
     * - `limit`: The maximum number of elements per page.
     * - `appliedFilters`: The filters applied to the dataset.
     * - `sort`: The sorting options applied to the dataset.
     * - `data`: The actual data in the current page.
     *
     * This method is primarily useful for debugging or logging purposes, providing a detailed
     * overview of the state of the object.
     *
     * @return A string representation of this Chunked instance in the format:
     * "Chunked(totalPages=<value>, pageIndex=<value>, totalElements=<value>, limit=<value>,
     * appliedFilters=<value>, sorting=<value>, data=<value>)".
     * @since 1.0.0
     */
    override fun toString() = "Chunked(totalPages=$totalPages, pageIndex=$pageIndex, totalElements=$totalElements, limit=${Feature.limit}, appliedFilters=$appliedFilters, sorting=$sort, data=$data)"
}