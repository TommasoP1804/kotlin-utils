/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")
@file:MustUseReturnValues

package dev.tommasop1804.kutils.classes.time

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.time.Duration.Companion.durationTo
import dev.tommasop1804.kutils.classes.time.RTemporalInterval.Companion.intervalTo
import dev.tommasop1804.kutils.classes.time.TemporalInterval.Companion.intervalToUnrestricted
import dev.tommasop1804.kutils.classes.time.TemporalInterval.Companion.parseTemporal
import jakarta.persistence.AttributeConverter
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.time.*
import java.time.temporal.Temporal
import java.time.temporal.TemporalUnit

/**
 * Represents a temporal interval between two points in time, defined by a start and an end.
 * This class provides operations to calculate the duration, manipulate the interval, and
 * supports immutability of start and end temporal points.
 *
 * @param T1 the type of the start temporal point, which must implement [Temporal].
 * @param T2 the type of the end temporal point, which must implement [Temporal].
 *
 * @property start the starting point of the temporal interval.
 * @property end the ending point of the temporal interval.
 * @since 3.4.0
 * @author Tommaso Pastorelli
 */
@Suppress("UNCHECKED_CAST", "RedundantValueArgument")
@JsonSerialize(using = RTemporalInterval.Companion.Serializer::class)
@JsonDeserialize(using = RTemporalInterval.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = RTemporalInterval.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = RTemporalInterval.Companion.OldDeserializer::class)
class RTemporalInterval<T1 : Temporal, T2 : Temporal> private constructor(
    override val start: T1,
    override val end: T2,
    private var startDuration: Boolean = false,
    private var endDuration: Boolean = false
) : TemporalInterval {

    /**
     * Represents the computed duration between the start and end points.
     *
     * The duration is calculated as the time interval from the designated
     * `start` point to the `end` point using the `durationTo` operation.
     * This value provides a measure of the elapsed time or distance depending
     * on the context in which it is used.
     * @since 3.4.0
     */
    override val duration = start durationTo end

    /**
     * Represents a temporal interval defined by a start and an end point in time.
     * The interval is typically used to denote a duration or range between two temporal boundaries.
     *
     * The `temporalInterval` assumes that both `start` and `end` are valid temporal values,
     * with `start` occurring before or simultaneously with `end`.
     * @since 3.4.0
     */
    val temporalInterval = start intervalToUnrestricted end

    /**
     * Secondary constructor for the RTemporalInterval class.
     *
     * This constructor initializes an instance of RTemporalInterval with the specified start and end parameters
     * while defaulting the temporal bounds (inclusive or exclusive) to false.
     *
     * @param start The starting point of the temporal interval.
     * @param end The ending point of the temporal interval.
     * @since 3.4.0
     */
    constructor(start: T1, end: T2) : this(start, end, false, false)

    companion object {
        /**
         * Creates a `LocalDateInterval` representing the inclusive interval between the calling `LocalDate`
         * and the specified `end` date.
         *
         * @param end The ending date of the interval.
         * @return A `LocalDateInterval` spanning from the calling `LocalDate` to the specified `end` date.
         * @since 5.2.2
         */
        infix fun LocalDate.intervalTo(end: LocalDate): LocalDateInterval = RTemporalInterval(this, end)
        /**
         * Calculates the temporal interval between this `LocalDateTime` and the specified end `LocalDateTime`.
         *
         * @param end The `LocalDateTime` representing the end of the interval.
         * @return A `LocalDateTimeInterval` representing the interval between the two `LocalDateTime` instances.
         * @since 5.2.2
         */
        infix fun LocalDateTime.intervalTo(end: LocalDateTime): LocalDateTimeInterval = RTemporalInterval(this, end)
        /**
         * Creates an interval between the current `OffsetDateTime` instance and the specified `end` parameter.
         *
         * This method returns an `OffsetDateTimeInterval` that represents the temporal interval
         * starting from the current `OffsetDateTime` (receiver) to the given `end` parameter.
         *
         * @param end The `OffsetDateTime` representing the end of the interval.
         * @return An `OffsetDateTimeInterval` representing the interval between the receiver and the `end`.
         * @since 5.2.2
         */
        infix fun OffsetDateTime.intervalTo(end: OffsetDateTime): OffsetDateTimeInterval = RTemporalInterval(this, end)
        /**
         * Creates a `ZonedDateTimeInterval` representing the interval between this `ZonedDateTime` and the specified end `ZonedDateTime`.
         *
         * @param end The end of the interval, represented as a `ZonedDateTime`.
         * @return A `ZonedDateTimeInterval` object representing the interval between the start and end points.
         * @since 5.2.2
         */
        infix fun ZonedDateTime.intervalTo(end: ZonedDateTime): ZonedDateTimeInterval = RTemporalInterval(this, end)
        /**
         * Creates a `LocalTimeInterval` representing the interval between the current `LocalTime` instance and the specified end time.
         *
         * This method computes a temporal interval starting from the current `LocalTime` (the receiver) to the given `end` time.
         *
         * @param end The `LocalTime` marking the end of the interval.
         * @return A `LocalTimeInterval` instance representing the interval between the start and end times.
         * @since 5.2.2
         */
        infix fun LocalTime.intervalTo(end: LocalTime): LocalTimeInterval = RTemporalInterval(this, end)
        /**
         * Constructs an `OffsetTimeInterval` between this `OffsetTime` and the specified end `OffsetTime`.
         *
         * @param end The end `OffsetTime` for the interval.
         * @return An `OffsetTimeInterval` representing the interval from this `OffsetTime` to the specified end `OffsetTime`.
         * @since 5.2.2
         */
        infix fun OffsetTime.intervalTo(end: OffsetTime): OffsetTimeInterval = RTemporalInterval(this, end)
        /**
         * Creates an interval between the current `Instant` and the specified end `Instant`.
         *
         * @param end The ending point of the temporal interval.
         * @return An `InstantInterval` representing the interval from the current `Instant` to the specified end `Instant`.
         * @since 5.2.2
         */
        infix fun Instant.intervalTo(end: Instant): InstantInterval = RTemporalInterval(this, end)
        /**
         * Creates a `YearInterval` between this `Year` and the specified end `Year`.
         *
         * @param end The ending year of the interval.
         * @return A `YearInterval` instance representing the interval between the two years.
         * @since 5.2.2
         */
        infix fun Year.intervalTo(end: Year): YearInterval = RTemporalInterval(this, end)
        /**
         * Creates a `YearMonthInterval` representing the interval between two `YearMonth` instances.
         *
         * @param end The ending `YearMonth` of the interval.
         * @return A `YearMonthInterval` instance encapsulating the interval between the start (this) and the specified end.
         * @since 5.2.2
         */
        infix fun YearMonth.intervalTo(end: YearMonth): YearMonthInterval = RTemporalInterval(this, end)
        /**
         * Creates a temporal interval between the invoking temporal object and the specified end temporal object.
         *
         * This function constructs an instance of `RTemporalInterval` with the current temporal object as the start
         * and the provided temporal object as the end. The operation is done using infix notation for better readability
         * in interval creation scenarios.
         *
         * @param end The temporal object that marks the end of the interval.
         * @since 5.2.2
         */
        infix fun <T1 : Temporal, T2 : Temporal> T1.intervalTo(end: T2) = RTemporalInterval(this, end)
        /**
         * Restricts an interval starting from the current temporal instance to the specified duration.
         *
         * @param duration The duration that limits the length of the resulting temporal interval.
         * @return A new instance of RTemporalInterval representing the interval from the current temporal instance
         *         to the calculated end point after adding the given duration.
         * @since 5.2.2
         */
        infix fun <T1 : Temporal> T1.intervalTo(duration: Duration) =
            RTemporalInterval(this, plus(duration) as T1, endDuration = true)
        /**
         * Creates a restricted temporal interval using the specified duration and end temporal object.
         * The interval will start before the end by the given duration.
         *
         * @param end The temporal object that specifies the end point of the interval.
         *            Must be of a type that implements Temporal.
         * @return A new instance of RTemporalInterval representing the interval.
         * @since 5.2.2
         */
        infix fun <T2 : Temporal> Duration.intervalTo(end: T2) =
            RTemporalInterval(minus(this) as T2, end, startDuration = true)

        /**
         * Converts the current `ClosedRange<LocalDate>` instance into a `LocalDateInterval`.
         *
         * The resulting interval is defined by the `start` and `endInclusive` values of the range,
         * creating an equivalent representation as a `LocalDateInterval`.
         *
         * @return A `LocalDateInterval` representing the same start and end dates as the original range.
         * @since 5.2.2
         */
        @JvmName("toIntervalLocalDate")
        fun ClosedRange<LocalDate>.toInterval(): LocalDateInterval = RTemporalInterval(start, endInclusive)
        /**
         * Converts the current `ClosedRange<LocalDateTime>` into an instance of `LocalDateTimeInterval`.
         *
         * This method maps the start and endInclusive properties of the `ClosedRange`
         * to the corresponding start and end values of a `LocalDateTimeInterval`.
         *
         * @return A `LocalDateTimeInterval` representing the interval defined by the `ClosedRange`.
         * @since 5.2.2
         */
        @JvmName("toIntervalLocalDateTime")
        fun ClosedRange<LocalDateTime>.toInterval(): LocalDateTimeInterval = RTemporalInterval(start, endInclusive)
        /**
         * Converts the current ClosedRange of OffsetDateTime instances into an OffsetDateTimeInterval.
         *
         * The resulting interval is derived from the start and endInclusive properties of the ClosedRange.
         *
         * @return An instance of OffsetDateTimeInterval representing the temporal interval defined by
         *         the start and endInclusive values of this ClosedRange.
         * @since 5.2.2
         */
        @JvmName("toIntervalOffsetDateTime")
        fun ClosedRange<OffsetDateTime>.toInterval(): OffsetDateTimeInterval = RTemporalInterval(start, endInclusive)
        /**
         * Converts the current [ClosedRange] of [ZonedDateTime] into a [ZonedDateTimeInterval].
         *
         * This method generates a temporal interval corresponding to the start and end points
         * of the [ClosedRange], creating an [RTemporalInterval] instance.
         *
         * @return A [ZonedDateTimeInterval] representing the temporal interval defined by the [ClosedRange].
         * @since 5.2.2
         */
        @JvmName("toIntervalZonedDateTime")
        fun ClosedRange<ZonedDateTime>.toInterval(): ZonedDateTimeInterval = RTemporalInterval(start, endInclusive)
        /**
         * Converts a `ClosedRange` of `LocalTime` into a `LocalTimeInterval`.
         *
         * This method transforms the range defined by the `start` and `endInclusive` of the `ClosedRange`
         * into an instance of `LocalTimeInterval`, preserving the temporal boundaries.
         *
         * @return A `LocalTimeInterval` representing the interval defined by this `ClosedRange`.
         * @since 5.2.2
         */
        @JvmName("toIntervalLocalTime")
        fun ClosedRange<LocalTime>.toInterval(): LocalTimeInterval = RTemporalInterval(start, endInclusive)
        /**
         * Converts the current `ClosedRange<OffsetTime>` instance into an `OffsetTimeInterval`.
         *
         * This function maps the start and endInclusive values of the closed range to create an interval
         * that represents the temporal span between them as an `OffsetTimeInterval`.
         *
         * @return An `OffsetTimeInterval` instance representing the temporal interval derived from the range's start and endInclusive values.
         * @since 5.2.2
         */
        @JvmName("toIntervalOffsetTime")
        fun ClosedRange<OffsetTime>.toInterval(): OffsetTimeInterval = RTemporalInterval(start, endInclusive)
        /**
         * Converts the current `ClosedRange` of `Instant` into an `InstantInterval`.
         *
         * @return an instance of `InstantInterval` representing the same temporal range as the `ClosedRange`,
         * where `start` maps to the range's `start` and `endInclusive` maps to the range's `end`.
         * @since 5.2.2
         */
        @JvmName("toIntervalInstant")
        fun ClosedRange<Instant>.toInterval(): InstantInterval = RTemporalInterval(start, endInclusive)
        /**
         * Converts a `ClosedRange<Year>` into a `YearInterval`.
         *
         * This method creates a `YearInterval` object using the start and
         * end points of the closed range.
         *
         * @return A `YearInterval` instance representing the same range of years
         *         as the original `ClosedRange<Year>`.
         * @since 5.2.2
         */
        @JvmName("toIntervalYear")
        fun ClosedRange<Year>.toInterval(): YearInterval = RTemporalInterval(start, endInclusive)
        /**
         * Converts a `ClosedRange<YearMonth>` to a `YearMonthInterval`.
         *
         * The resulting interval is defined by the start and end points
         * of the provided `ClosedRange<YearMonth>`, where the start corresponds
         * to the beginning of the range and the end corresponds to the inclusive endpoint.
         *
         * @return A `YearMonthInterval` representing the interval derived from the range.
         * @since 5.2.2
         */
        @JvmName("toIntervalYearMonth")
        fun ClosedRange<YearMonth>.toInterval(): YearMonthInterval = RTemporalInterval(start, endInclusive)

        /**
         * Converts the current `ClosedRange` instance into a `MonoTemporalInterval`.
         *
         * The resulting interval represents the temporal range defined by the start and end values
         * of the `ClosedRange`.
         *
         * @return A new instance of `MonoTemporalInterval` initialized with the `start` and `endInclusive`
         * values of the `ClosedRange`.
         * @since 5.2.2
         */
        fun <T> ClosedRange<T>.toInterval(): MonoTemporalInterval<T> where T : Comparable<T>, T : Temporal =
            RTemporalInterval(start, endInclusive)


        /**
         * Parses a string representation of a temporal interval into an `RTemporalInterval` instance.
         *
         * This function supports intervals defined with temporal start and end values,
         * or duration-based definitions. Repeated intervals that start with "R" are not supported.
         *
         * @param s The string representation of the temporal interval to parse.
         *          It should be formatted as "start/duration", "duration/end", or "start/end".
         *          Repeated intervals, starting with "R", are not supported.
         * @throws UnsupportedOperationException If the input string indicates a repeated interval
         *                                       or contains only a duration.
         * @throws dev.tommasop1804.kutils.exceptions.MalformedInputException If the format of the input string is invalid.
         * @return A result of type `Result<RTemporalInterval>` containing the parsed interval,
         *         or an exception if errors occur during parsing.
         * @return 3.4.1
         */
        infix fun <T1 : Temporal, T2 : Temporal> parse(s: String): Result<RTemporalInterval<T1, T2>> = runCatching {
            if (s.startsWith("R")) throw UnsupportedOperationException("Repeated intervals are not supported. Use RepeatedTemporalInterval.parse(s).")
            else {
                val parts = s.splitAndTrim("/")
                validateInputFormat(!(parts.isEmpty() || parts.size > 2)) { "Invalid time interval: $s. Should be not empty or with at most two parts." }
                if (parts.size == 1) throw UnsupportedOperationException("Invalid time interval: $s. Should be not empty or with at most two parts. For only duration, use TemporalInterval.parse(s).")
                else if (parts[0].startsWith("P")) {
                    RTemporalInterval(
                        Duration.parse(parts[0])(),
                        parseTemporal(parts[1]) as T2
                    ) as RTemporalInterval<T1, T2>
                } else if (parts[1].startsWith("P")) {
                    RTemporalInterval(
                        parseTemporal(parts[0]) as T1,
                        Duration.parse(parts[1])()
                    ) as RTemporalInterval<T1, T2>
                } else {
                    RTemporalInterval(
                        parseTemporal(parts[0]) as T1,
                        parseTemporal(parts[1]) as T2
                    )
                }
            }
        }

        class Serializer : ValueSerializer<TemporalInterval>() {
            override fun serialize(
                value: TemporalInterval,
                gen: JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<RTemporalInterval<*, *>>() {
            private var startType: Class<*>? = null
            private var endType: Class<*>? = null

            override fun createContextual(
                ctxt: DeserializationContext,
                property: BeanProperty?
            ): ValueDeserializer<*> {
                val javaType = property?.type ?: ctxt.contextualType
                val copy = Deserializer()
                if (javaType != null && javaType.containedTypeCount() >= 2) {
                    copy.startType = javaType.containedType(0).rawClass
                    copy.endType = javaType.containedType(1).rawClass
                }
                return copy
            }
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): RTemporalInterval<*, *> {
                val result = parse<Temporal, Temporal>(p.string)()
                startType?.let {
                    require(it.isInstance(result.start)) {
                        "Cannot deserialize: expected start type ${it.simpleName}, got ${result.start::class.simpleName}"
                    }
                }
                endType?.let {
                    require(it.isInstance(result.end)) {
                        "Cannot deserialize: expected end type ${it.simpleName}, got ${result.end::class.simpleName}"
                    }
                }
                return result
            }
        }

        class OldSerializer : JsonSerializer<RTemporalInterval<*, *>>() {
            override fun serialize(value: RTemporalInterval<*, *>, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.toString())
            }
        }

        class OldDeserializer : JsonDeserializer<RTemporalInterval<*, *>>(), ContextualDeserializer {
            private var startType: Class<*>? = null
            private var endType: Class<*>? = null

            override fun createContextual(ctxt: com.fasterxml.jackson.databind.DeserializationContext, property: com.fasterxml.jackson.databind.BeanProperty?): JsonDeserializer<*> {
                val javaType = property?.type ?: ctxt.contextualType
                val copy = OldDeserializer()
                if (javaType != null && javaType.containedTypeCount() >= 2) {
                    copy.startType = javaType.containedType(0).rawClass
                    copy.endType = javaType.containedType(1).rawClass
                }
                return copy
            }

            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): RTemporalInterval<*, *> {
                val result = parse<Temporal, Temporal>(p.text)()
                startType?.let {
                    require(it.isInstance(result.start)) {
                        "Cannot deserialize: expected start type ${it.simpleName}, got ${result.start::class.simpleName}"
                    }
                }
                endType?.let {
                    require(it.isInstance(result.end)) {
                        "Cannot deserialize: expected end type ${it.simpleName}, got ${result.end::class.simpleName}"
                    }
                }
                return result
            }
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<RTemporalInterval<*, *>?, String?> {
            override fun convertToDatabaseColumn(attribute: RTemporalInterval<*, *>?) = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?) = if (dbData == null) null else parse<Temporal, Temporal>(dbData)()
        }

        /**
         * Creates a column in the table to represent a temporal interval with the specified name and length.
         * The column values are transformed into an object representation of a temporal interval when read
         * and back into a string representation when written.
         *
         * @param T1 the type of the start temporal (e.g., LocalDate, LocalDateTime).
         * @param T2 the type of the end temporal (e.g., LocalDate, LocalDateTime).
         * @param name the name of the column to be created in the table.
         * @param length the maximum length of the column's values, defaults to 100.
         * @return a column definition for temporal interval representation.
         * @since 5.5.0
         */
        inline fun <reified T1 : Temporal, reified T2 : Temporal> Table.rTemporalInteval(name: String, length: Int = 100) = varchar(name, length)
            .transform({ parse<T1, T2>(it)() }) { it.toString() }
        /**
         * Defines a mono-temporal interval column for a table, representing a time-based interval of type `T`.
         * The method maps the column to a database `varchar` and applies transformations to parse and format the value.
         *
         * @param T The specific temporal type the interval represents (e.g., `LocalDate`, `LocalDateTime`).
         * @param name The name of the column in the database.
         * @param length The length of the `varchar` column in the database, with a default value of 100.
         * @return A `Column` representing a `MonoTemporalInterval` of type `T`.
         * @since 5.5.0
         */
        inline fun <reified T : Temporal> Table.monoTemporalInteval(name: String, length: Int = 100): Column<MonoTemporalInterval<T>> = varchar(name, length)
            .transform({ parse<T, T>(it)() }) { it.toString() }
        /**
         * Creates a column in the database capable of storing `LocalDateInterval` values.
         *
         * This function defines a database column of type `varchar` with the specified name and length.
         * It provides custom transformations for parsing and converting `LocalDateInterval` objects
         * to and from `String` representations.
         *
         * @param name The name of the database column to be created.
         * @param length The maximum length of the column in characters (default is 100).
         * @return A `Column` that stores `LocalDateInterval` values, with transformations applied to parse
         * from and convert to string representations.
         * @since 5.5.0
         */
        fun Table.localDateInteval(name: String, length: Int = 100): Column<LocalDateInterval> = varchar(name, length)
            .transform({ parse<LocalDate, LocalDate>(it)() }) { it.toString() }
        /**
         * Adds a column to the table that represents a `LocalDateTimeInterval`.
         * This method transforms a varchar column into a column of `LocalDateTimeInterval`
         * using the specified name and length.
         *
         * @param name The name of the column to be created.
         * @param length The maximum length of the varchar representation. Defaults to 100 if not specified.
         * @return A column of type `LocalDateTimeInterval` that is added to the table.
         * @since 5.5.0
         */
        fun Table.localDateTimeInteval(name: String, length: Int = 100): Column<LocalDateTimeInterval> = varchar(name, length)
            .transform({ parse<LocalDateTime, LocalDateTime>(it)() }) { it.toString() }
        /**
         * Defines a column within a table that represents an `OffsetDateTimeInterval`.
         * This method leverages transformation logic to map string values to `OffsetDateTimeInterval`
         * and vice versa when interacting with the database.
         *
         * @param name The name of the column.
         * @param length The length of the column to store the interval as a string. Defaults to 100.
         * @return A column object representing an `OffsetDateTimeInterval` with the specified attributes.
         * @since 5.5.0
         */
        fun Table.offsetDateTimeInteval(name: String, length: Int = 100): Column<OffsetDateTimeInterval> = varchar(name, length)
            .transform({ parse<OffsetDateTime, OffsetDateTime>(it)() }) { it.toString() }
        /**
         * Adds a column to the table that stores a `ZonedDateTimeInterval` as a string.
         *
         * This method enables the storage of a custom-defined interval of
         * ZonedDateTime in the database by transforming the data to and from its string representation.
         *
         * @param name the name of the column to be added in the table.
         * @param length the maximum length of the string to store the ZonedDateTimeInterval.
         * Default value is 100.
         * @return the column of type `Column<ZonedDateTimeInterval>` that has been added to the table.
         * @since 5.5.0
         */
        fun Table.zonedDateTimeInteval(name: String, length: Int = 100): Column<ZonedDateTimeInterval> = varchar(name, length)
            .transform({ parse<ZonedDateTime, ZonedDateTime>(it)() }) { it.toString() }
        /**
         * Creates a column that represents a local time interval within a database table.
         *
         * The local time interval is stored as a string representation in the database and is mapped
         * to a custom `LocalTimeInterval` type in the application. The method leverages transformations
         * for parsing and formatting the time interval to and from the database format.
         *
         * @param name the name of the column to be created.
         * @param length the maximum length of the string representation of the interval in the database; defaults to 100.
         * @return a column that represents a local time interval in the database table.
         * @since 5.5.0
         */
        fun Table.localTimeInteval(name: String, length: Int = 100): Column<LocalTimeInterval> = varchar(name, length)
            .transform({ parse<LocalTime, LocalTime>(it)() }) { it.toString() }
        /**
         * Creates a column within the table to store an offset-based time interval.
         * The column is defined as a `varchar` type, with transformations applied to
         * parse and convert the value to and from the `OffsetTimeInterval` type.
         *
         * @param name The name of the column to be created.
         * @param length The maximum length of the column (default is 100).
         * @return A `Column` instance of type `OffsetTimeInterval` representing the created column.
         * @since 5.5.0
         */
        fun Table.offsetTimeInteval(name: String, length: Int = 100): Column<OffsetTimeInterval> = varchar(name, length)
            .transform({ parse<OffsetTime, OffsetTime>(it)() }) { it.toString() }
        /**
         * Creates a database column for storing `InstantInterval` values with transformation logic.
         *
         * This method defines a `varchar` column and applies transformations to convert the value
         * between its database representation and `InstantInterval` type.
         *
         * @param name The name of the column in the database table.
         * @param length The maximum length of the column. Defaults to `100`.
         * @return A `Column` instance configured to handle `InstantInterval` values.
         * @since 5.5.0
         */
        fun Table.instantInteval(name: String, length: Int = 100): Column<InstantInterval> = varchar(name, length)
            .transform({ parse<Instant, Instant>(it)() }) { it.toString() }
        /**
         * Defines a column in the table representing a year interval, with support for custom length.
         * The column is internally treated as a varchar and transformed for YearInterval representation.
         *
         * @param name The name of the column.
         * @param length The maximum length of the varchar column. Defaults to 100 if not specified.
         * @return A column of type `YearInterval`.
         * @since 5.5.0
         */
        fun Table.yearInteval(name: String, length: Int = 100): Column<YearInterval> = varchar(name, length)
            .transform({ parse<Year, Year>(it)() }) { it.toString() }
        /**
         * Creates a column in the database table that represents a year-month interval.
         * This method maps the year-month interval to a string column while providing
         * transformation logic to parse the string into a `YearMonth` and back.
         *
         * @param name The name of the database column.
         * @param length The length of the column for storing the string representation of the year-month interval. Defaults to 100.
         * @return A column object representing the year-month interval in the database table.
         * @since 5.5.0
         */
        fun Table.yearMonthInterval(name: String, length: Int = 100): Column<YearMonthInterval> = varchar(name, length)
            .transform({ parse<YearMonth, YearMonth>(it)() }) { it.toString() }
    }

    /**
     * Returns a string representation of the object.
     *
     * This method overrides the default toString implementation
     * to return the string representation of the `temporalInterval` property.
     *
     * @return A string describing the `temporalInterval`.
     * @since 3.4.0
     */
    override fun toString() = if (startDuration) toTemporalIntervalOfDurationTemporal().toString()
    else if (endDuration) toTemporalIntervalOfTemporalDuration().toString()
    else toTemporalIntervalOfTemporals().toString()

    /**
     * Retrieves the value associated with the specified temporal unit from the temporal interval.
     *
     * @param unit the TemporalUnit whose value is to be retrieved
     * @return the value of the specified TemporalUnit within the temporal interval
     * @since 3.4.0
     */
    override fun get(unit: TemporalUnit) = temporalInterval[unit]

    /**
     * Adds this temporal interval to the specified temporal object.
     *
     * This method applies the temporal interval encapsulated by the `temporalInterval`
     * to the provided temporal object, modifying it to reflect the addition.
     *
     * @param temporal the target temporal object to which the interval will be added
     * @return the modified temporal object after applying this interval
     * @throws java.time.DateTimeException if the addition produces an invalid or unsupported result
     * @throws ArithmeticException if adding the interval causes numeric overflow
     * @since 3.4.0
     */
    override fun addTo(temporal: Temporal) = temporalInterval.addTo(temporal)

    /**
     * Subtracts the temporal interval represented by this instance from the specified temporal object.
     *
     * The operation adjusts the provided temporal object by subtracting the duration of this interval.
     * The resulting temporal reflects the state after this subtraction is applied.
     *
     * @param temporal the temporal object from which the interval will be subtracted
     * @return the resulting temporal object after the subtraction
     * @since 3.4.0
     */
    override fun subtractFrom(temporal: Temporal) = temporalInterval.subtractFrom(temporal)

    /**
     * Retrieves the list of temporal units supported by this temporal interval.
     *
     * @return a list of TemporalUnit representing the units associated with this interval
     * @since 3.4.0
     */
    override fun getUnits(): List<TemporalUnit> = temporalInterval.getUnits()

    /**
     * Converts the current temporal interval into a representation based on its duration.
     *
     * This method delegates the conversion process to the `toTemporalIntervalOfDuration` method
     * of the `temporalInterval` property. It encapsulates the logic for deriving a repeated
     * temporal interval using the duration of the current interval.
     *
     * @return a repeated temporal interval derived from the duration of the current temporal interval
     * @since 3.4.0
     */
    override fun toTemporalIntervalOfDuration() = temporalInterval.toTemporalIntervalOfDuration()
    /**
     * Converts the current temporal interval into a representation that uses temporal durations.
     *
     * This method delegates the transformation to the `toTemporalIntervalOfTemporalDuration`
     * function of the `temporalInterval` property.
     *
     * @return A new instance representing the temporal interval in terms of temporal durations.
     * @since 3.4.0
     */
    override fun toTemporalIntervalOfTemporalDuration() = temporalInterval.toTemporalIntervalOfTemporalDuration()
    /**
     * Converts this instance's temporal interval representation into one that expresses
     * the interval as a combination of duration and start/end temporal values.
     *
     * @return A representation of the temporal interval in terms of its duration and start/end temporals.
     * @since 3.4.0
     */
    override fun toTemporalIntervalOfDurationTemporal() = temporalInterval.toTemporalIntervalOfDurationTemporal()
    /**
     * Converts the current temporal interval representation into a `TemporalInterval`
     * object defined by its temporal boundaries.
     *
     * This method operates on the underlying `temporalInterval` property and provides
     * an equivalent `TemporalInterval` representation derived from the current instance's
     * temporal boundaries.
     *
     * @return A `TemporalInterval` instance representing this object's temporal interval.
     * @since 3.4.0
     */
    override fun toTemporalIntervalOfTemporals() = temporalInterval.toTemporalIntervalOfTemporals()

    /**
     * Returns a new instance of RTemporalInterval with the specified start value while retaining the existing end value.
     *
     * @param start the new start value for the temporal interval.
     * @since 3.4.0
     */
    override fun withStart(start: Temporal) = RTemporalInterval(start, end)
    /**
     * Creates a new `RTemporalInterval` with the specified end temporal value, maintaining the existing start value.
     *
     * @param end The temporal value to set as the end of the interval.
     * @return A new `RTemporalInterval` instance with the specified end value.
     * @since 3.4.0
     */
    override fun withEnd(end: Temporal) = RTemporalInterval(start, end)
    /**
     * Creates a new instance of RTemporalInterval with the same start time and an updated end time
     * calculated by adding the specified duration to the current start time.
     *
     * @param duration The duration to add to the start time to compute the new end time.
     * @return A new RTemporalInterval instance with the updated duration.
     * @since 3.4.0
     */
    override fun withDuration(duration: Duration) = RTemporalInterval(start, start.plus(duration) as T1)

    /**
     * Compares this object with the specified object for equality.
     *
     * @param other The object to compare with this instance for equality.
     * @return `true` if the specified object is equal to this instance, otherwise `false`.
     * @since 3.4.0
     */
    override fun equals(other: Any?) = temporalInterval == other
    /**
     * Computes the hash code for the object based on its temporal interval property.
     *
     * @return The hash code value derived from the `temporalInterval` property.
     * @since 3.4.0
     */
    override fun hashCode() = temporalInterval.hashCode()
}

/**
 * Creates a restricted temporal interval based on a start time and a duration.
 *
 * @param T1 The type of the temporal object that defines the start time,
 *           which must implement the Temporal interface.
 * @param start The starting point of the temporal interval.
 * @param duration The duration of the temporal interval.
 * @return A restricted temporal interval derived from the start time and duration.
 * @since 3.4.0
 */
fun <T1: Temporal> RTemporalInterval(start: T1, duration: Duration) =
    start.intervalTo(duration)
/**
 * Constructs a restricted temporal interval based on the provided duration and end temporal point.
 *
 * @param duration the duration representing the length of the interval.
 * @param end the temporal point marking the end of the interval.
 * @return a restricted temporal interval capped by the given duration and end point.
 * @since 3.4.0
 */
fun <T2: Temporal> RTemporalInterval(duration: Duration, end: T2) =
    duration.intervalTo(end)

/**
 * A list of all the dates within the interval defined by this LocalDateInterval.
 *
 * The list is constructed by iterating from the start date (inclusive) to the end date (exclusive)
 * using the `datesUntil` method. The result includes each date in sequential order.
 * @since 5.2.2
 */
val LocalDateInterval.dates: List<LocalDate> get() = start.datesUntil(end.plusDays(1)).toList()
/**
 * A list of `Year` objects representing the years within the interval defined
 * by this `YearInterval`. The sequence starts from the `start` year and
 * increments by one year at a time until the `end` year (inclusive).
 * @since 5.2.2
 */
val YearInterval.years: List<Year> get() = generateSequence(start) { it.plusYears(1) }
    .takeWhile { it <= end }
    .toList()

/**
 * A typealias that represents a temporal interval where both the lower and upper bounds
 * share the same temporal type.
 *
 * This typealias simplifies the representation of temporal intervals in cases where
 * the start and end points must be of the same type.
 *
 * @param T The type used for the temporal bounds of the interval (e.g., LocalDate, LocalDateTime).
 * @since 3.4.1
 */
typealias MonoTemporalInterval<T> = RTemporalInterval<T, T>
/**
 * A typealias for an interval of time represented by start and end points,
 * where both the start and end are instances of LocalDate.
 *
 * This alias simplifies the usage of RTemporalInterval with LocalDate
 * as the bounds for the temporal interval.
 * @since 3.4.2
 */
typealias LocalDateInterval = RTemporalInterval<LocalDate, LocalDate>
/**
 * A type alias representing a temporal interval where both the start and end points are
 * instances of `LocalDateTime`.
 *
 * This alias simplifies working with a `RTemporalInterval` that specifically operates on
 * `LocalDateTime` objects, ensuring type safety and readability in contexts where time intervals
 * with precise date-time values are required.
 * @since 3.4.2
 */
typealias LocalDateTimeInterval = RTemporalInterval<LocalDateTime, LocalDateTime>
/**
 * A type alias representing a temporal interval with `OffsetDateTime` as both the start and end types.
 *
 * This type alias is for a `RTemporalInterval` that specifies the usage of `OffsetDateTime`
 * for defining the bounds of the interval. It is useful for working with time intervals
 * where both the starting and ending points are represented as `OffsetDateTime`.
 * @since 3.4.2
 */
typealias OffsetDateTimeInterval = RTemporalInterval<OffsetDateTime, OffsetDateTime>
/**
 * A type alias for `RTemporalInterval` with both the start and end points being `ZonedDateTime` objects.
 *
 * Represents a temporal interval where the boundaries are defined using `ZonedDateTime`.
 * @since 3.4.2
 */
typealias ZonedDateTimeInterval = RTemporalInterval<ZonedDateTime, ZonedDateTime>
/**
 * A typealias representing a temporal interval with a start and end value of type `LocalTime`.
 * `RTemporalInterval` is a generic class that accepts two type parameters,
 * both of which are specified as `LocalTime` in this alias.
 * @since 3.4.2
 */
typealias LocalTimeInterval = RTemporalInterval<LocalTime, LocalTime>
/**
 * A type alias representing a temporal interval with `OffsetTime` as the start and end type.
 *
 * This type alias simplifies the usage of `RTemporalInterval` when working specifically with
 * temporal intervals defined by `OffsetTime` instances.
 * @since 3.4.2
 */
typealias OffsetTimeInterval = RTemporalInterval<OffsetTime, OffsetTime>
/**
 * A type alias for `RTemporalInterval` with both the type parameters set to `Instant`.
 *
 * Represents a temporal interval where both the start and end points are `Instant` instances.
 * @since 3.4.2
 */
typealias InstantInterval = RTemporalInterval<Instant, Instant>
/**
 * A typealias representing a temporal interval specifically constrained to the `Year` type.
 *
 * This is a specialization of the generic `RTemporalInterval` class where both the starting
 * and ending bounds are defined using the `Year` type. It is useful for cases where the
 * temporal interval solely involves years.
 * @since 3.4.2
 */
typealias YearInterval = RTemporalInterval<Year, Year>
/**
 * A type alias representing a temporal interval with `YearMonth` as both the start and end type.
 *
 * This alias simplifies the usage of `RTemporalInterval` when working specifically
 * with intervals defined by `YearMonth` objects.
 *
 * Example use cases include representing a range of months within a year or across multiple years.
 * @since 3.4.2
 */
typealias YearMonthInterval = RTemporalInterval<YearMonth, YearMonth>