package dev.tommasop1804.kutils.classes.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.classes.time.LocalMonthDayTime.Companion.MICROS_PER_SECOND
import dev.tommasop1804.kutils.classes.time.LocalMonthDayTime.Companion.NANOS_PER_SECOND
import dev.tommasop1804.kutils.classes.time.LocalMonthDayTime.Companion.SECONDS_PER_DAY
import dev.tommasop1804.kutils.classes.time.LocalMonthDayTime.Companion.SECONDS_PER_MINUTE
import dev.tommasop1804.kutils.isNull
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.*
import java.util.*
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE
import kotlin.reflect.KProperty
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

/**
 * Represents a combination of a month-day and a local time, where the month-day is independent
 * of a specific year, and the local time is represented without reference to any time zone.
 *
 * This class provides utilities for handling comparisons, time manipulations, and leap year
 * adjustments when dealing with month-day and time components.
 *
 * @property monthDay Represents the combination of month and day. This is independent of any specific year.
 * @property localTime Represents the time part of this object. It is independent of time zones.
 * @since 1.0.0
 */
@JsonSerialize(using = LocalMonthDayTime.Companion.Serializer::class)
@JsonDeserialize(using = LocalMonthDayTime.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = LocalMonthDayTime.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = LocalMonthDayTime.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_getorthrow_as_invoke", "kutils_temporal_now_as_temporal", "kutils_temporal_of_as_temporal", "kutils_temporal_parse_as_temporal", "kutils_take_as_int_invoke")
class LocalMonthDayTime(val monthDay: MonthDay, val localTime: LocalTime) : Temporal, TemporalAccessor, TemporalAdjuster, Comparable<LocalMonthDayTime>, Serializable {
    /**
     * Constructs an instance of `LocalMonthDayTime` using the provided `LocalDate` and `LocalTime`.
     *
     * The `LocalDate` is converted to a `MonthDay`, and the resulting instance combines it
     * with the specified `LocalTime` to form a `LocalMonthDayTime`.
     *
     * @param localDate the `LocalDate` to extract the `MonthDay` from
     * @param localTime the `LocalTime` to associate with the `MonthDay`
     * @return a new `LocalMonthDayTime` instance containing the extracted `MonthDay` from the
     *         `localDate` and the specified `localTime`
     * @since 1.0.0
     */
    constructor(localDate: LocalDate, localTime: LocalTime) : this(MonthDay.from(localDate), localTime)

    /**
     * Private secondary constructor for initializing the class by combining a LocalMonthDayTime instance.
     *
     * This constructor delegates to the primary constructor by extracting the `monthDay` and `localTime`
     * properties from the provided `lmdt` object.
     *
     * @param lmdt An instance of LocalMonthDayTime whose properties are used to initialize the class.
     * @since 1.0.0
     */
    private constructor(lmdt: LocalMonthDayTime) : this(lmdt.monthDay, lmdt.localTime)

    /**
     * Creates an instance of `LocalMonthDayTime` with the specified month, day, and optional time components.
     *
     * @param month the month of the year, from 1 (January) to 12 (December)
     * @param day the day of the month, from 1 to the maximum allowed for the month
     * @param hour the hour of the day, from 0 to 23 (default is 0)
     * @param minute the minute of the hour, from 0 to 59 (default is 0)
     * @param second the second of the minute, from 0 to 59 (default is 0)
     * @param nanoOfSecond the nanosecond within the second, from 0 to 999,999,999 (default is 0)
     * @return an instance of `LocalMonthDayTime` representing the specified month, day, and time
     * @since 1.0.0
     */
    constructor(month: Int, day: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, nanoOfSecond: Int = 0) : this(MonthDay.of(month, day), LocalTime.of(hour, minute, second, nanoOfSecond))
    /**
     * Creates an instance of `LocalMonthDayTime` from the given month, day, and optional time components.
     *
     * @param month the month of the year, not null
     * @param day the day of the month, must be valid for the specified month
     * @param hour the hour of the day, from 0 to 23, default is 0
     * @param minute the minute of the hour, from 0 to 59, default is 0
     * @param second the second of the minute, from 0 to 59, default is 0
     * @param nanoOfSecond the nanosecond of the second, from 0 to 999,999,999, default is 0
     * @return a new `LocalMonthDayTime` instance representing the specified values
     * @since 1.0.0
     */
    constructor(month: Month, day: Int, hour: Int = 0, minute: Int = 0, second: Int = 0, nanoOfSecond: Int = 0) : this(MonthDay.of(month, day), LocalTime.of(hour, minute, second, nanoOfSecond))

    /**
     * Creates a `LocalMonthDayTime` instance from the given `Instant` and `ZoneId`.
     * The method computes the `LocalDateTime` associated with the provided `Instant` in the specified time zone
     * and converts it to a `LocalMonthDayTime` instance.
     *
     * @param instant the instant to convert, not null
     * @param zoneId the time zone to use for converting the instant, not null
     * @return a `LocalMonthDayTime` instance representing the provided instant in the specified time zone
     * @since 1.0.0
     */
    constructor(instant: Instant, zoneId: ZoneId) : this(from(LocalDateTime.ofInstant(instant, zoneId)))
    /**
     * Constructs an instance using the provided Instant and ZoneIdent.
     *
     * The provided Instant is converted to a LocalDateTime based on the time zone specified
     * in the ZoneIdent, and this LocalDateTime is used to initialize the instance.
     *
     * @param instant the instant in time to use for this constructor
     * @param zoneId the time zone identifier to interpret the instant
     * @since 1.0.0
     */
    constructor(instant: Instant, zoneId: ZoneIdent) : this(from(LocalDateTime.ofInstant(instant, zoneId.zoneId)))
    /**
     * Constructs an instance by converting an Instant and ZoneId into a LocalDateTime.
     *
     * This constructor takes a Kotlin Instant and a ZoneId, converts the Instant
     * to a Java Instant, and then constructs a LocalDateTime from the specified
     * instant and time zone.
     *
     * @param instant The Kotlin Instant to be converted.
     * @param zoneId The time zone associated with the LocalDateTime to be created.
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(instant: kotlin.time.Instant, zoneId: ZoneId) : this(from(LocalDateTime.ofInstant(instant.toJavaInstant(), zoneId)))
    /**
     * Constructs an instance based on a Kotlin Time Instant and a specified time zone.
     *
     * @param instant The `kotlin.time.Instant` representing the specific point in time.
     * @param zoneId The `ZoneIdent` representing the time zone to associate with the instant.
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(instant: kotlin.time.Instant, zoneId: ZoneIdent) : this(from(LocalDateTime.ofInstant(instant.toJavaInstant(), zoneId.zoneId)))

    /**
     * Primary constructor that initializes the class instance with the current date and time.
     *
     * This constructor provides a default initialization mechanism that uses the `now()`
     * function to supply the necessary parameter for the secondary constructor.
     *
     * @since 1.0.0
     */
    constructor() : this(now())
    /**
     * Constructor that initializes the instance with the current time derived from the provided clock.
     * The `now(clock)` function is used to compute the current time.
     *
     * @param clock The clock implementation used to determine the current time.
     * @since 1.0.0
     */
    constructor(clock: Clock) : this(now(clock))
    /**
     * Constructs an instance using the provided ZoneId.
     *
     * @param zone the time zone identifier used for determining the current time
     * @since 1.0.0
     */
    constructor(zone: ZoneId) : this(now(zone))
    /**
     * Constructs an instance using the specified time zone.
     *
     * This constructor initializes the object by creating the instance
     * based on the current date and time of the provided `ZoneIdent`.
     *
     * @param zone the time zone identifier to initialize with
     * @since 1.0.0
     */
    constructor(zone: ZoneIdent) : this(now(zone))

    companion object {
        /**
         * A unique identifier for serializable classes used to verify compatibility
         * during the deserialization process. It ensures that a loaded class
         * is compatible with the serialized object.
         *
         * Any modification to the class that is incompatible with the serialized
         * form should result in a change to the serialVersionUID value to prevent
         * deserialization errors.
         *
         * @since 1.0.0
         */
        @Serial private const val serialVersionUID = 4954918890077093841L

        /**
         * Represents the minimal allowed value of `LocalMonthDayTime`.
         * This constant is initialized with the first day of the first month (January 1st)
         * and the minimum of `LocalTime` (00:00:00.000000000).
         *
         * @see LocalMonthDayTime
         * @see LocalTime.MIN
         * @since 1.0.0
         */
        val MIN: LocalMonthDayTime = LocalMonthDayTime(MonthDay.of(1, 1), LocalTime.MIN)
        /**
         * Represents the maximum possible value for a `LocalMonthDayTime`.
         * This constant is defined with the maximum day of the year (December 31)
         * and the latest possible local time (23:59:59.999999999).
         *
         * The `MAX` value can be used in operations that require an upper bound or
         * when comparing against other `LocalMonthDayTime` instances.
         *
         * @since 1.0.0
         */
        val MAX: LocalMonthDayTime = LocalMonthDayTime(MonthDay.of(12, 31), LocalTime.MAX)

        /**
         * Represents the default leap year used within the `LocalMonthDayTime` class for date and time calculations
         * that depend on whether a given year is a leap year or not. This value acts as a reference point.
         *
         * The year 2024 is chosen as it is the next immediate leap year at the time of implementation. A leap year
         * is defined as a year divisible by 4, except for years divisible by 100 that are not divisible by 400.
         *
         * This constant is generally used in operations that require leap year adjustments, such as adding or subtracting
         * dates, or for checking comparisons across specific date and time boundaries.
         *
         * @since 1.0.0
         */
        private const val DEFAULT_LEAP_YEAR: Int = 2024
        /**
         * Represents the default year to be used when a non-leap year is required.
         *
         * This constant is used in operations and calculations within the `LocalMonthDayTime` class
         * where the distinction between leap years and non-leap years is significant.
         * The chosen value, 2025, is a non-leap year.
         *
         * @since 1.0.0
         */
        private const val DEFAULT_NON_LEAP_YEAR: Int = 2025
        /**
         * Represents the number of hours in a standard day.
         *
         * This constant is used in time calculations where the standard duration of a day,
         * consisting of 24 hours, is required.
         *
         * @see LocalMonthDayTime.plusHours
         * @see LocalMonthDayTime.minusHours
         * @since 1.0.0
         */
        private const val HOURS_PER_DAY: Int = 24
        /**
         * Represents the number of minutes in one hour.
         *
         * This constant is used across the application to ensure consistency when performing
         * calculations or operations involving time, specifically when converting between
         * hours and minutes.
         *
         * @since 1.0.0
         */
        private const val MINUTES_PER_HOUR: Int = 60
        /**
         * Represents the number of minutes in a day, derived from the number of minutes in an hour
         * multiplied by the number of hours in a day.
         *
         * This constant is utilized in calculations involving daily time-related operations.
         *
         * @since 1.0.0
         */
        private const val MINUTES_PER_DAY: Int = MINUTES_PER_HOUR * HOURS_PER_DAY
        /**
         * Represents the number of seconds in one minute.
         *
         * This constant is used in time calculations within the `LocalMonthDayTime` class
         * to define the relationship between minutes and seconds.
         *
         * @since 1.0.0
         */
        private const val SECONDS_PER_MINUTE: Int = 60
        /**
         * A constant representing the number of seconds in an hour.
         *
         * Combines the number of seconds in a minute and the number of minutes in an hour
         * to calculate the total seconds within a single hour.
         *
         * This value is derived as `SECONDS_PER_MINUTE * MINUTES_PER_HOUR`.
         *
         * @since 1.0.0
         */
        private const val SECONDS_PER_HOUR: Int = SECONDS_PER_MINUTE * MINUTES_PER_HOUR
        /**
         * Represents the number of seconds in a single day.
         *
         * This constant is derived by multiplying the number of seconds in an hour
         * (normally defined as 3600) by the number of hours in a day (normally defined as 24).
         *
         * It is used in time-related computations where the total number of seconds in a day needs
         * to be referenced or calculated efficiently.
         *
         * @since 1.0.0
         */
        private const val SECONDS_PER_DAY: Int = SECONDS_PER_HOUR * HOURS_PER_DAY
        /**
         * Represents the number of milliseconds in one second.
         *
         * This constant can be used in time-related calculations or conversions
         * where a time duration needs to be expressed in milliseconds.
         *
         * @since 1.0.0
         */
        private const val MILLIS_PER_SECOND: Long = 1000L
        /**
         * Represents the number of milliseconds in a single day.
         * The value is derived by multiplying the number of milliseconds in a second by the number of seconds in a day.
         * This constant is useful for time-based calculations that involve daily intervals.
         *
         * @since 1.0.0
         */
        private const val MILLIS_PER_DAY: Long = MILLIS_PER_SECOND * SECONDS_PER_DAY
        /**
         * Represents the number of microseconds in one second.
         *
         * This constant can be used for time-based operations or calculations
         * that require conversions or comparisons involving microsecond precision.
         *
         * @since 1.0.0
         */
        private const val MICROS_PER_SECOND: Long = 1000000L
        /**
         * Represents the constant value of the total number of microseconds in a day.
         * This value is calculated based on the number of microseconds per second and the
         * total number of seconds in a day.
         *
         * @see MICROS_PER_SECOND
         * @see SECONDS_PER_DAY
         * @since 1.0.0
         */
        private const val MICROS_PER_DAY: Long = MICROS_PER_SECOND * SECONDS_PER_DAY
        /**
         * Represents the number of nanoseconds in one second.
         * This constant is used for time-related calculations where nanosecond precision is required.
         *
         * @since 1.0.0
         */
        private const val NANOS_PER_SECOND: Long = 1000000000L
        /**
         * Represents the number of nanoseconds in a single minute.
         *
         * This constant is derived by multiplying the number of nanoseconds in a second
         * ([NANOS_PER_SECOND]) by the number of seconds in a minute ([SECONDS_PER_MINUTE]).
         *
         * Used for precise time calculations and conversions within the `LocalMonthDayTime` class.
         *
         * @see LocalMonthDayTime.plusMinutes
         * @see LocalMonthDayTime.minusMinutes
         * @see LocalMonthDayTime.plusNanos
         * @see LocalMonthDayTime.minusNanos
         * @since 1.0.0
         */
        private const val NANOS_PER_MINUTE: Long = NANOS_PER_SECOND * SECONDS_PER_MINUTE
        /**
         * Represents the number of nanoseconds in one hour.
         * This constant is derived by multiplying the number of nanoseconds in a minute (`NANOS_PER_MINUTE`)
         * by the number of minutes in an hour (`MINUTES_PER_HOUR`).
         *
         * @since 1.0.0
         */
        private const val NANOS_PER_HOUR: Long = NANOS_PER_MINUTE * MINUTES_PER_HOUR
        /**
         * The constant representing the number of nanoseconds in a day.
         * This value is calculated as the product of nanoseconds in an hour (`NANOS_PER_HOUR`)
         * and the number of hours in a day (`HOURS_PER_DAY`).
         *
         * @since 1.0.0
         */
        private const val NANOS_PER_DAY: Long = NANOS_PER_HOUR * HOURS_PER_DAY

        /**
         * Creates a new instance of `LocalMonthDayTime` from the specified TemporalAccessor.
         *
         * This method extracts the `MonthDay` and `LocalTime` components from the given temporal accessor
         * and uses them to initialize a `LocalMonthDayTime` instance.
         *
         * @param temporalAccessor the temporal object to extract `MonthDay` and `LocalTime` from
         * @return a new `LocalMonthDayTime` instance based on the specified temporal accessor
         * @throws DateTimeException if unable to extract the required fields
         * @since 1.0.0
         */
        @JvmStatic
        fun from(temporalAccessor: TemporalAccessor) = LocalMonthDayTime(MonthDay.from(temporalAccessor), LocalTime.from(temporalAccessor))

        /**
         * Creates an instance of LocalMonthDayTime from a given Temporal object.
         *
         * This method extracts the `MonthDay` and `LocalTime` components
         * from the provided `temporal` object to construct a new `LocalMonthDayTime` instance.
         *
         * @param temporal the temporal object to convert, not null.
         *                 Must be supported by both `MonthDay.from` and `LocalTime.from`.
         * @return a new instance of LocalMonthDayTime based on the temporal input.
         * @throws DateTimeException if unable to extract the MonthDay or LocalTime from the temporal input.
         *
         * @since 1.0.0
         */
        @JvmStatic
        fun from(temporal: Temporal) = LocalMonthDayTime(MonthDay.from(temporal), LocalTime.from(temporal))

        /**
         * Creates an instance of `LocalMonthDayTime` representing the current date and time
         * in the specified time zone.
         *
         * @param zoneId the time zone ID to use for obtaining the current date and time
         * @return an instance of `LocalMonthDayTime` containing the current `MonthDay` and `LocalTime` in the specified time zone
         * @since 1.0.0
         */
        @JvmStatic
        fun now(zoneId: ZoneId) = LocalMonthDayTime(MonthDay.now(zoneId), LocalTime.now(zoneId))
        /**
         * Obtains the current month, day, and time for the specified time zone.
         *
         * @param zoneId the zone identifier specifying the time zone for which
         *               the current month, day, and time is to be retrieved
         * @return a LocalMonthDayTime instance representing the current month and day
         *         as a MonthDay, and the current time as a LocalTime, for the given time zone
         * @since 1.0.0
         */
        fun now(zoneId: ZoneIdent) = LocalMonthDayTime(MonthDay.now(zoneId.zoneId), LocalTime.now(zoneId.zoneId))

        /**
         * Creates an instance of `LocalMonthDayTime` using the current date and time from the specified clock.
         *
         * @param clock the clock to obtain the current date and time, not null
         * @return a `LocalMonthDayTime` object containing the current `MonthDay` and `LocalTime` as per the clock
         * @since 1.0.0
         */
        @JvmStatic
        fun now(clock: Clock) = LocalMonthDayTime(MonthDay.now(clock), LocalTime.now(clock))

        /**
         * Creates a new `LocalMonthDayTime` instance initialized with the current date (MonthDay)
         * and the current time (LocalTime) based on the system clock in the default time zone.
         *
         * This method is useful for obtaining a `LocalMonthDayTime` representing the current moment.
         *
         * @return A `LocalMonthDayTime` instance with the current MonthDay and LocalTime.
         *
         * @since 1.0.0
         */
        @JvmStatic
        fun now() = LocalMonthDayTime(MonthDay.now(), LocalTime.now())

        /**
         * Parses the given string into a `LocalMonthDayTime` instance or generates the current date and time if the string is blank.
         * The string can represent partial date-time formats, including optional time zone or offset information.
         *
         * @param s The string to be parsed. It supports various formats:
         *          - `MM-dd` or `MM-ddTHH:mm` for plain month-day and optional time.
         *          - `-MM-dd` or `-MM-ddTHH:mm` for month-day with a leading dash and optional time.
         *          - `--MM-dd` or `--MM-ddTHH:mm` for month-day in ISO representation and optional time.
         *          - An empty or blank string will default to the current date and time.
         * @return A result encapsulating a `LocalMonthDayTime` object if parsed successfully or an exception if parsing fails.
         * @since 1.0.0
         */
        @JvmStatic
        fun parse(s: CharSequence) = runCatching {
            if (s.isBlank()) LocalMonthDayTime()
            else {
                val finalIndex = if (s.endsWith("Z")) s.length - 1
                else
                    if ("+" in s.substring(6))
                        s.indexOf('+', 6)
                    else (if ("-" in s.substring(6)) s.indexOf('-', 6) else s.length)

                if (s.startsWith("--")) LocalMonthDayTime(
                    MonthDay.parse(s.take(7), DateTimeFormatter.ofPattern("--MM-dd")),
                    LocalTime.parse(s.substring(8, finalIndex))
                )
                else if (s.startsWith("-")) LocalMonthDayTime(
                    MonthDay.parse(s.take(6), DateTimeFormatter.ofPattern("-MM-dd")),
                    LocalTime.parse(s.substring(7, finalIndex))
                )
                else LocalMonthDayTime(
                    MonthDay.parse(s.take(5), DateTimeFormatter.ofPattern("MM-dd")),
                    LocalTime.parse(s.substring(6, finalIndex))
                )
            }
        }

        /**
         * Converts a [Temporal] object to a local month, day, and time representation.
         *
         * This function takes a [Temporal] instance and extracts the local month,
         * day, and time from it, creating a representation that aligns with the local
         * time zone or specified temporal context.
         *
         * @receiver Temporal instance to be converted.
         * @return An object representing the local month, day, and time derived from the receiver.
         * @throws DateTimeException if unable to convert to the local month, day, and time.
         * @throws UnsupportedTemporalTypeException if the [Temporal] does not support the required fields.
         *
         * @since 1.0.0
         */
        fun Temporal.toLocalMonthDayTime() = from(this)
        /**
         * Converts a [TemporalAccessor] instance to its corresponding local date-time representation,
         * focusing on the month, day, and time components. This excludes information about the year
         * or timezone, delivering localized month, day, and time information.
         *
         * The method utilizes the `from` function to interpret the [TemporalAccessor] and extract
         * the desired local components.
         *
         * @receiver The [TemporalAccessor] instance to convert into a local month-day-time representation.
         * @return A temporal instance containing the localized month, day, and time components extracted
         *         from the receiver.
         * @throws DateTimeException If the conversion cannot be completed due to insufficient or
         *         inconsistent temporal information within the receiver.
         *
         * @since 1.0.0
         */
        fun TemporalAccessor.toLocalMonthDayTime() = from(this)

        class Serializer : ValueSerializer<LocalMonthDayTime>() {
            override fun serialize(
                value: LocalMonthDayTime,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<LocalMonthDayTime>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): LocalMonthDayTime = parse(p.string).getOrThrow()
        }

        class OldSerializer : JsonSerializer<LocalMonthDayTime>() {
            override fun serialize(value: LocalMonthDayTime, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.toString())
            }
        }

        class OldDeserializer : JsonDeserializer<LocalMonthDayTime>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext) = parse(p.text).getOrThrow()
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<LocalMonthDayTime?, String?> {
            override fun convertToDatabaseColumn(attribute: LocalMonthDayTime?) = if (Objects.isNull(attribute)) null else attribute.toString()

            override fun convertToEntityAttribute(dbData: String?) = if (dbData.isNull()) null else parse(dbData).getOrThrow()
        }
    }

    /**
     * Creates a copy of the current LocalMonthDayTime instance with the specified or default properties.
     *
     * @param monthDay the MonthDay component to use in the new instance. Defaults to the MonthDay of the current instance.
     * @param localTime the LocalTime component to use in the new instance. Defaults to the LocalTime of the current instance.
     * @return a new LocalMonthDayTime instance with the specified or default MonthDay and LocalTime.
     * @since 1.0.0
     */
    fun copy(monthDay: MonthDay = this.monthDay, localTime: LocalTime = this.localTime): LocalMonthDayTime = LocalMonthDayTime(monthDay, localTime)

    /**
     * Determines whether the current `LocalMonthDayTime` instance occurs before the given `other` instance.
     * The comparison takes into account whether each instance corresponds to a leap year or a non-leap year.
     *
     * @param other The `LocalMonthDayTime` instance to compare against.
     * @param firstLeap A boolean indicating if the current instance should be treated as occurring in a leap year.
     * @param secondLeap A boolean indicating if the `other` instance should be treated as occurring in a leap year.
     * @return `true` if the current instance occurs before the `other` instance; `false` otherwise.
     * @since 1.0.0
     */
    fun isBefore(other: LocalMonthDayTime, firstLeap: Boolean, secondLeap: Boolean): Boolean {
        val firstDate = LocalDateTime.of(LocalDate.of(if (firstLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR, monthDay.month, monthDay.dayOfMonth), localTime)
        val secondDate = LocalDateTime.of(LocalDate.of(if (secondLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR, other.monthDay.month, other.monthDay.dayOfMonth), other.localTime)
        return firstDate.isBefore(secondDate)
    }

    /**
     * Determines if this instance is after the specified `other` instance.
     *
     * Comparison considers both the date and time components of the two instances.
     * Leap year handling can be optionally configured based on `firstLeap` and `secondLeap` parameters.
     *
     * @param other The `LocalMonthDayTime` instance to compare against.
     * @param firstLeap Indicates whether the current instance should be treated in the context of a leap year.
     * @param secondLeap Indicates whether the `other` instance should be treated in the context of a leap year.
     * @return `true` if this instance is after the specified `other` instance; `false` otherwise.
     * @since 1.0.0
     */
    fun isAfter(other: LocalMonthDayTime, firstLeap: Boolean, secondLeap: Boolean): Boolean {
        val firstDate = LocalDateTime.of(LocalDate.of(if (firstLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR, monthDay.month, monthDay.dayOfMonth), localTime)
        val secondDate = LocalDateTime.of(LocalDate.of(if (secondLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR, other.monthDay.month, other.monthDay.dayOfMonth), other.localTime)
        return firstDate.isAfter(secondDate)
    }

    /**
     * Compares this `LocalMonthDayTime` instance with another `LocalMonthDayTime` instance to determine if they represent
     * the same point in time. The comparison is performed based on the specified leap year configurations.
     *
     * @param other The `LocalMonthDayTime` instance to compare with.
     * @param firstLeap A boolean indicating whether the first instance (this object) should be treated as if it is in a leap year.
     * @param secondLeap A boolean indicating whether the second instance (other object) should be treated as if it is in a leap year.
     * @return `true` if the two instances represent the same point in time, `false` otherwise.
     * @since 1.0.0
     */
    fun isEqual(other: LocalMonthDayTime, firstLeap: Boolean, secondLeap: Boolean): Boolean {
        val firstDate = LocalDateTime.of(LocalDate.of(if (firstLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR, monthDay.month, monthDay.dayOfMonth), localTime)
        val secondDate = LocalDateTime.of(LocalDate.of(if (secondLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR, other.monthDay.month, other.monthDay.dayOfMonth), other.localTime)
        return firstDate.isEqual(secondDate)
    }

    /**
     * Returns a temporal object of class `class` representing the start of the day.
     *
     * This method supports `LocalDate`, `LocalDateTime`, `LocalTime`,
     * and `LocalMonthDayTime` classes, casting the output to the requested temporal type.
     *
     * @param `class` the class of the desired temporal type
     * @return an instance of the specified temporal type at the start of the day
     * @throws DateTimeException if the given class is unsupported
     * @since 1.0.0
     */
    fun <T: Temporal> atStartOfDay(`class`: Class<T>): T {
        return when (`class`.name) {
            "java.time.LocalDate" -> `class`.cast(
                LocalDate.of(
                    monthDay.month.value,
                    monthDay.dayOfMonth,
                    monthDay.month.minLength()
                )
            )
            "java.time.LocalDateTime" -> `class`.cast(
                LocalDateTime.of(
                    Year.now().value,
                    monthDay.dayOfMonth,
                    monthDay.month.minLength(),
                    0, 0, 0, 0
                )
            )
            "java.time.LocalTime" -> `class`.cast(LocalTime.MIDNIGHT)
            "dev.tommasop1804.kutils.classes.time.LocalMonthDayTime" -> `class`.cast(
                LocalMonthDayTime(
                    monthDay,
                    LocalTime.MIDNIGHT
                )
            )
            else -> throw DateTimeException("Unsupported class: " + `class`.getName())
        }
    }

    /**
     * Returns a `Temporal` object representing the end of the day for the specified temporal class.
     * The specific behavior depends on the provided class type.
     *
     * Supported types include:
     * - `LocalDate` returns the last day of the current month and year.
     * - `LocalDateTime` returns the date and time as 23:59:59.999999999 for the last day of the current month and year.
     * - `LocalTime` returns the maximal time value of 23:59:59.999999999.
     * - `LocalMonthDayTime` includes `monthDay` and `LocalTime.MAX`.
     *
     * @param `class` A `Class` object representing the type of `Temporal` to create. Supported types are `LocalDate`, `LocalDateTime`, `LocalTime`, and `LocalMonthDayTime`.
     * @return An instance of the specified `Temporal` class representing the end of the day.
     * @throws DateTimeException if the provided class type is unsupported.
     * @since 1.0.0
     */
    fun <T: Temporal> atEndOfDay(`class`: Class<T>): T {
        return when (`class`.name) {
            "java.time.LocalDate" -> `class`.cast(
                LocalDate.of(
                    Year.now().value,
                    monthDay.dayOfMonth,
                    monthDay.month.maxLength()
                )
            )
            "java.time.LocalDateTime" -> `class`.cast(
                LocalDateTime.of(
                    Year.now().value,
                    monthDay.dayOfMonth,
                    monthDay.month.maxLength(),
                    23, 59, 59, 999999999
                )
            )
            "java.time.LocalTime" -> `class`.cast(LocalTime.MAX)
            "dev.tommasop1804.kutils.classes.time.LocalMonthDayTime" -> `class`.cast(
                LocalMonthDayTime(
                    monthDay,
                    LocalTime.MAX
                )
            )
            else -> throw DateTimeException("Unsupported class: " + `class`.getName())
        }
    }

    /**
     * Returns a new LocalMonthDayTime with the specified number of months added.
     * Optionally, the calculation can account for leap years based on a provided flag.
     *
     * @param amountToAdd the number of months to add, positive or negative
     * @param leap whether the calculation should consider leap years; defaults to false
     * @since 1.0.0
     */
    fun plusMonths(amountToAdd: Long, leap: Boolean = false) = LocalMonthDayTime(
        MonthDay.from(
            LocalDate.of(
                if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
                monthDay.month,
                monthDay.dayOfMonth
            ).plusMonths(amountToAdd)
        ),
        localTime
    )

    /**
     * Adds the specified number of weeks to this instance and returns a modified copy with the updated date and time.
     *
     * @param amountToAdd The number of weeks to add. Can be positive or negative.
     * @param leap Indicates if the year should be treated as a leap year for date calculations. Defaults to false.
     * @since 1.0.0
     */
    fun plusWeeks(amountToAdd: Long, leap: Boolean = false) = LocalMonthDayTime(
        MonthDay.from(
            LocalDate.of(
                if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
                monthDay.month,
                monthDay.dayOfMonth
            ).plusWeeks(amountToAdd)
        ),
        localTime
    )

    /**
     * Adds a specified number of days to the current LocalMonthDayTime instance and returns a new instance
     * with the updated date. Optionally accounts for leap year adjustments if specified.
     *
     * @param amountToAdd The number of days to add. Can be negative to subtract days.
     * @param leap Indicates whether leap year adjustments should be considered. Defaults to false.
     * @return A new LocalMonthDayTime instance with the modified date.
     * @since 1.0.0
     */
    fun plusDays(amountToAdd: Long, leap: Boolean = false) = LocalMonthDayTime(
        MonthDay.from(
            LocalDate.of(
                if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
                monthDay.month,
                monthDay.dayOfMonth
            ).plusDays(amountToAdd)
        ),
        localTime
    )

    /**
     * Adds the specified number of hours to this object, optionally accounting for leap years.
     *
     * @param amountToAdd the number of hours to add, may be negative
     * @param leap a boolean flag indicating whether the calculation should consider a leap year
     * @since 1.0.0
     */
    fun plusHours(amountToAdd: Long, leap: Boolean = false) = plusWithOverflow(
        LocalDate.of(if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR, monthDay.month, monthDay.dayOfMonth),
        amountToAdd,
        0,
        0,
        0
    )

    /**
     * Adds the specified number of minutes to this instance, adjusting for overflow and maintaining
     * the integrity of the date and time representation. The leap year consideration allows proper
     * handling of February 29th in leap years.
     *
     * @param amountToAdd the number of minutes to add, may be negative
     * @param leap indicates if a leap year should be considered for the calculation. Defaults to `false`
     * @since 1.0.0
     */
    fun plusMinutes(amountToAdd: Long, leap: Boolean = false) = plusWithOverflow(
        LocalDate.of(if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR, monthDay.month, monthDay.dayOfMonth),
        0,
        amountToAdd,
        0,
        0
    )

    /**
     * Adds the specified number of seconds to this `LocalMonthDayTime` instance, optionally considering leap years.
     *
     * @param amountToAdd the number of seconds to add, positive or negative
     * @param leap indicates if leap year rules should be considered during calculations, defaults to `false`
     * @since 1.0.0
     */
    fun plusSeconds(amountToAdd: Long, leap: Boolean = false) = plusWithOverflow(
        LocalDate.of(if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR, monthDay.month, monthDay.dayOfMonth),
        0,
        0,
        amountToAdd,
        0
    )

    /**
     * Adds the specified number of nanoseconds to this instance, optionally considering leap years.
     *
     * @param amountToAdd the number of nanoseconds to add; can be negative to subtract nanoseconds
     * @param leap a flag indicating whether to compute using a leap year, default is false
     * @since 1.0.0
     */
    fun plusNanos(amountToAdd: Long, leap: Boolean = false) = plusWithOverflow(
        LocalDate.of(if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR, monthDay.month, monthDay.dayOfMonth),
        0,
        0,
        0,
        amountToAdd
    )

    /**
     * Adds the specified hours, minutes, seconds, and nanoseconds to a date, handling overflow and adjusting both
     * the date and time appropriately. Overflow in time units will propagate correctly into higher units such as
     * days.
     *
     * @param newDate The starting date to which the time components will be added.
     * @param hours The number of hours to add.
     * @param minutes The number of minutes to add.
     * @param seconds The number of seconds to add.
     * @param nanos The number of nanoseconds to add.
     * @return A new `LocalMonthDayTime` instance representing the resulting date and time after applying the addition.
     * @since 1.0.0
     */
    private fun plusWithOverflow(newDate: LocalDate, hours: Long, minutes: Long, seconds: Long, nanos: Long): LocalMonthDayTime {
        if ((hours or minutes or seconds or nanos) == 0L) return LocalMonthDayTime(MonthDay.from(newDate), localTime)
        var totDays = nanos / NANOS_PER_DAY + seconds / SECONDS_PER_DAY + minutes / MINUTES_PER_DAY + hours / HOURS_PER_DAY
        var totNanos = nanos % NANOS_PER_DAY + (seconds % SECONDS_PER_DAY) * NANOS_PER_SECOND + (minutes % MINUTES_PER_DAY) * NANOS_PER_MINUTE + (hours % HOURS_PER_DAY) * NANOS_PER_HOUR
        val curNoD = localTime.toNanoOfDay()
        totNanos += curNoD
        totDays += Math.floorDiv(totNanos, NANOS_PER_DAY)
        val newNoD = Math.floorMod(totNanos, NANOS_PER_DAY)
        val newTime = if (newNoD == curNoD) localTime else LocalTime.ofNanoOfDay(newNoD)
        return LocalMonthDayTime(MonthDay.from(newDate.plusDays(totDays)), newTime)
    }

    /**
     * Adds an amount of time, specified by the given `amountToAdd` and `unit`, to this LocalMonthDayTime instance.
     * A leap flag is used to handle cases where leap years or leap seconds apply.
     *
     * @param amountToAdd The amount of time to add, measured in terms of the specified `unit`.
     * @param unit The temporal unit by which the `amountToAdd` is measured. This can include common ChronoUnit options such as days, hours, months, etc.
     * @param leap A boolean indicating if leap conditions (e.g., leap year, leap seconds) should be accounted for during the calculation.
     * @return A new `LocalMonthDayTime` instance with the specified amount of time added.
     * @since 1.0.0
     */
    fun plus(amountToAdd: Long, unit: TemporalUnit, leap: Boolean): LocalMonthDayTime {
        if (unit is ChronoUnit) {
            return when (unit) {
                ChronoUnit.NANOS -> plusNanos(amountToAdd, leap)
                ChronoUnit.MICROS -> plusDays(amountToAdd / MICROS_PER_DAY, leap).plusNanos((amountToAdd % MICROS_PER_DAY) * 1000, leap)
                ChronoUnit.MILLIS -> plusDays(amountToAdd / MILLIS_PER_DAY, leap).plusNanos((amountToAdd % MILLIS_PER_DAY) * 1000000, leap)
                ChronoUnit.SECONDS -> plusSeconds(amountToAdd, leap)
                ChronoUnit.MINUTES -> plusMinutes(amountToAdd, leap)
                ChronoUnit.HOURS -> plusHours(amountToAdd, leap)
                ChronoUnit.HALF_DAYS -> plusDays(amountToAdd / 256, leap).plusHours((amountToAdd % 256) * 12, leap)
                ChronoUnit.DAYS -> plusDays(amountToAdd, leap)
                ChronoUnit.WEEKS -> plusWeeks(amountToAdd, leap)
                ChronoUnit.MONTHS -> plusMonths(amountToAdd, leap)
                ChronoUnit.YEARS, ChronoUnit.ERAS, ChronoUnit.DECADES, ChronoUnit.MILLENNIA, ChronoUnit.FOREVER, ChronoUnit.CENTURIES -> this
            }
        }
        return unit.addTo(this, amountToAdd)
    }

    /**
     * Adds the specified amount to the temporal object in terms of the specified unit.
     *
     * @param amountToAdd the amount of the specified unit to add, may be negative
     * @param unit the unit of the amount to add, not null
     * @return a copy of the temporal object with the amount added
     * @since 1.0.0
     */
    override fun plus(amountToAdd: Long, unit: TemporalUnit) = plus(amountToAdd, unit, false)

    /**
     * Subtracts the specified number of months from this object, considering the provided leap year setting.
     *
     * @param amountToSubtract the number of months to subtract, may be negative to add months instead
     * @param leap specifies whether to use leap year considerations, default is false
     * @since 1.0.0
     */
    fun minusMonths(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusMonths(MAX_VALUE, leap).plusMonths(1, leap)
        else plusMonths(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of weeks from this instance, adjusting for leap years if required.
     *
     * @param amountToSubtract the number of weeks to subtract, may be negative
     * @param leap specifies whether to consider leap year adjustments
     * @since 1.0.0
     */
    fun minusWeeks(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusWeeks(MAX_VALUE, leap).plusWeeks(1, leap)
        else plusWeeks(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of days from this instance.
     *
     * If the amount to subtract equals `Long.MIN_VALUE`, the calculation is
     * performed in two steps to avoid overflow. The `leap` parameter determines
     * whether calculations consider leap years.
     *
     * @param amountToSubtract the number of days to subtract, may be negative
     * @param leap whether to consider leap years in calculations, defaults to `false`
     * @since 1.0.0
     */
    fun minusDays(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusDays(MAX_VALUE, leap).plusDays(1, leap)
        else plusDays(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of hours from this instance.
     *
     * This method is a wrapper around `plusHours` with a negated value
     * to perform subtraction of hours. Handles special cases where
     * the amount to subtract is `Long.MIN_VALUE`.
     *
     * @param amountToSubtract the number of hours to subtract, may be negative
     * @param leap indicates whether the operation should consider leap years
     * @since 1.0.0
     */
    fun minusHours(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusHours(MAX_VALUE, leap).plusHours(1, leap)
        else plusHours(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of minutes from the current instance and returns
     * a new instance with the adjusted value. If the specified amount is the minimum value
     * of a long, the method handles the subtraction by first adding the maximum value
     * and then subtracting the remaining one minute to avoid overflow issues.
     *
     * @param amountToSubtract the number of minutes to subtract; may be negative
     *                         to actually add minutes instead.
     * @param leap indicates whether leap year adjustments should be considered.
     *             Defaults to `false` if not specified.
     * @since 1.0.0
     */
    fun minusMinutes(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusMinutes(MAX_VALUE, leap).plusMinutes(1, leap)
        else plusMinutes(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of seconds from this object. Optionally adjusts for leap years.
     *
     * @param amountToSubtract the number of seconds to subtract, can be negative
     * @param leap specifies whether the calculation should account for leap years, default is false
     * @since 1.0.0
     */
    fun minusSeconds(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusSeconds(MAX_VALUE, leap).plusSeconds(1, leap)
        else plusSeconds(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of nanoseconds from this `LocalMonthDayTime`.
     * If the amount to subtract is equal to `Long.MIN_VALUE`, it handles the overflow
     * by first adding `Long.MAX_VALUE` nanoseconds and then adding 1 additional nanosecond.
     *
     * @param amountToSubtract the number of nanoseconds to subtract, may be negative
     * @param leap specifies if the subtraction should consider leap year logic; defaults to `false`
     * @since 1.0.0
     */
    fun minusNanos(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusNanos(MAX_VALUE, leap).plusNanos(1, leap)
        else plusNanos(-amountToSubtract, leap)

    /**
     * Subtracts the specified amount from this instance using the specified temporal unit and leap year considerations.
     * If the amount to subtract equals `Long.MIN_VALUE`, it will adjust the computation to avoid overflow.
     *
     * @param amountToSubtract the amount of the unit to subtract; negative values are treated as addition
     * @param unit the temporal unit specifying the amount to subtract
     * @param leap indicates if the calculation should consider leap years during the subtraction
     * @since 1.0.0
     */
    fun minus(amountToSubtract: Long, unit: TemporalUnit, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plus(MAX_VALUE, unit, leap).plus(1, unit, leap)
        else plus(-amountToSubtract, unit, leap)

    /**
     * Converts the specified year, along with the month, day, and time defined in the
     * current instance, into a `LocalDateTime`.
     *
     * @param year the year to be included in the resulting `LocalDateTime`
     * @return a `LocalDateTime` instance created from the specified year, as well as
     *         the month, day, and time components of the current instance
     * @since 1.0.0
     */
    fun toLocalDateTime(year: Int): LocalDateTime = LocalDateTime.of(year, monthDay.month, monthDay.dayOfMonth, localTime.hour, localTime.minute, localTime.second, localTime.nano)
    /**
     * Converts the current instance to a LocalDateTime object using the specified Year.
     *
     * @param year The Year to be used in the conversion. Defaults to the current year if not provided.
     * @return A LocalDateTime representation of the current instance.
     * @since 1.0.0
     */
    fun toLocalDateTime(year: Year = Year.now()): LocalDateTime = toLocalDateTime(year.value)

    /**
     * Converts the specified year and the current month and day values to a `LocalDate`.
     *
     * @param year The year value to be used in the resulting `LocalDate`.
     * @return A `LocalDate` object representing the specified year along with the current month and day values.
     * @since 1.0.0
     */
    fun toLocalDate(year: Int): LocalDate = LocalDate.of(year, monthDay.month, monthDay.dayOfMonth)
    /**
     * Converts the current instance to a `LocalDate` using the specified year.
     *
     * @param year The `Year` to use for the conversion. Defaults to the current year if not specified.
     * @return A `LocalDate` representing the date of this instance combined with the provided year.
     * @since 1.0.0
     */
    fun toLocalDate(year: Year = Year.now()): LocalDate = toLocalDate(year.value)

    /**
     * Converts this instance to a MonthDay representation.
     *
     * @return the MonthDay extracted from this instance
     * @since 1.0.0
     */
    fun toMonthDay(): MonthDay = monthDay

    /**
     * Retrieves the `LocalTime` component of this `LocalMonthDayTime` instance.
     *
     * @return the `LocalTime` part of this instance.
     * @since 1.0.0
     */
    fun toLocalTime(): LocalTime = localTime

    /**
     * Returns a string representation of the object, which consists of the
     * string representation of the `monthDay` field followed by the literal "T"
     * and the string representation of the `localTime` field.
     *
     * This method combines the values of the `monthDay` and `localTime` fields
     * into a single string in a format that is suitable for debugging and logging purposes.
     *
     * @return A string representation of this object in the format "MonthDayTLocalTime".
     * @since 1.0.0
     */
    override fun toString() = monthDay.toString() + "T" + localTime.toString()

    /**
     * Converts the properties of an object into a map representation.
     *
     * @return A map containing the extracted values from MonthDay and LocalTime.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "monthDay" to monthDay,
        "localTime" to localTime,
        "month" to monthDay.month!!,
        "day" to monthDay.dayOfMonth,
        "hour" to localTime.hour,
        "minute" to localTime.minute,
        "second" to localTime.second,
        "milli" to localTime.nano / 1000000,
        "nano" to localTime.nano,
    )

    /**
     * Retrieves the value associated with a property name from an internal map.
     *
     * - `monthDay`: The MonthDay object - TYPE: [MonthDay].
     * - `localTime`: The LocalTime object - TYPE: [LocalTime].
     * - `month`: The value of the month from the MonthDay object - TYPE: [Month].
     * - `day`: The day of the month from the MonthDay object - TYPE: [Int].
     * - `hour`: The hour component of the LocalTime object - TYPE: [Int].
     * - `minute`: The minute component of the LocalTime object - TYPE: [Int].
     * - `second`: The second component of the LocalTime object - TYPE: [Int].
     * - `milli`: The millisecond part of the nanoseconds from the LocalTime object - TYPE: [Int].
     * - `nano`: The nanosecond component of the LocalTime object - TYPE: [Int].
     *
     * @param R The expected type of the value being retrieved.
     * @param thisRef The reference to the object in which the property is defined. Can be null.
     * @param property The metadata for the property whose value is being retrieved.
     * @return The value associated with the property name, cast to the expected type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R

    /**
     * Compares this `LocalMonthDayTime` object with the specified `LocalMonthDayTime` object for order.
     *
     * @param other The `LocalMonthDayTime` object to be compared.
     * @return A negative integer, zero, or a positive integer if this object is less than, equal to, or greater than the specified object.
     * @since 1.0.0
     */
    override fun compareTo(other: LocalMonthDayTime): Int {
        val monthDayComparison = monthDay.compareTo(other.monthDay)
        return if (monthDayComparison == 0) localTime.compareTo(other.localTime) else monthDayComparison
    }

    /**
     * Checks if the specified `TemporalUnit` is supported by this `LocalMonthDayTime`.
     *
     * The supported units are:
     * - NANOS
     * - MICROS
     * - MILLIS
     * - SECONDS
     * - MINUTES
     * - HOURS
     * - HALF_DAYS
     * - DAYS
     * - WEEKS
     * - MONTHS
     *
     * @param unit the temporal unit to check, not null
     * @return `true` if the unit is supported, `false` otherwise
     * @since 1.0.0
     */
    override fun isSupported(unit: TemporalUnit) = when (unit) {
            ChronoUnit.NANOS, ChronoUnit.MICROS, ChronoUnit.MILLIS, ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS, ChronoUnit.HALF_DAYS, ChronoUnit.DAYS, ChronoUnit.WEEKS, ChronoUnit.MONTHS -> true
            else -> false
    }

    /**
     * Checks if the specified temporal field is supported.
     *
     * @param field the temporal field to check, may be null
     * @return true if the field is supported; false otherwise
     * @since 1.0.0
     */
    override fun isSupported(field: TemporalField?) = when (field) {
        ChronoField.DAY_OF_MONTH, ChronoField.MONTH_OF_YEAR, ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_SECOND -> true
        else -> false
    }

    /**
     * Returns a new instance of `LocalMonthDayTime` with the specified `MonthDay`.
     *
     * @param monthDay the `MonthDay` to set in the resulting `LocalMonthDayTime` instance.
     * @return a new `LocalMonthDayTime` instance with the given `MonthDay`.
     * @since 1.0.0
     */
    fun withMontDay(monthDay: MonthDay) = LocalMonthDayTime(monthDay, localTime)

    /**
     * Returns a copy of this `LocalMonthDayTime` with the month-of-year altered.
     *
     * @param month the month to set in the result, from 1 (January) to 12 (December)
     * @return a new `LocalMonthDayTime` instance with the specified month value
     * @throws DateTimeException if the resulting date is invalid
     * @since 1.0.0
     */
    fun withMonth(month: Int): LocalMonthDayTime {
        month in 1..12 || throw DateTimeException("Invalid value for MonthOfYear: $month")
        return LocalMonthDayTime(MonthDay.of(month, monthDay.dayOfMonth), localTime)
    }
    /**
     * Creates a new instance of `LocalMonthDayTime` with the specified month, keeping the current day of the month
     * and local time unchanged.
     *
     * @param month the month to set in the new instance
     * @return a new `LocalMonthDayTime` instance with the specified month
     * @since 1.0.0
     */
    fun withMonth(month: Month) = LocalMonthDayTime(MonthDay.of(month, monthDay.dayOfMonth), localTime)

    /**
     * Returns a new instance of `LocalMonthDayTime` with the specified day of the month.
     *
     * This method ensures that the provided day is valid for the current month, throwing a
     * `DateTimeException` if the value is out of range.
     *
     * @param day the day of the month to set, must be between 1 and the maximum number of days in the current month
     * @return a new instance of `LocalMonthDayTime` with the specified day of the month
     * @since 1.0.0
     */
    fun withDay(day: Int): LocalMonthDayTime {
        day < 1 || day > monthDay.month.maxLength() || throw DateTimeException("Invalid value for DayOfMonth: $day")
        return LocalMonthDayTime(MonthDay.of(monthDay.month, day), localTime)
    }

    /**
     * Returns a new instance of `LocalMonthDayTime` with the specified time.
     *
     * @param localTime the local time to set in the resulting `LocalMonthDayTime` instance
     * @since 1.0.0
     */
    fun withTime(localTime: LocalTime) = LocalMonthDayTime(monthDay, localTime)

    /**
     * Returns a new instance of `LocalMonthDayTime` with the hour-of-day updated.
     *
     * This function modifies the hour-of-day while retaining all other components of the current instance.
     * The hour must be in the range of 0 to 23 inclusive. If the provided hour is invalid, an exception is thrown.
     *
     * @param hour The new hour-of-day to set, from 0 to 23 inclusive.
     * @return A new instance of `LocalMonthDayTime` with the updated hour-of-day.
     * @throws DateTimeException if the hour is not within the valid range.
     * @since 1.0.0
     */
    fun withHour(hour: Int): LocalMonthDayTime {
        hour in 0..23 || throw DateTimeException("Invalid value for HourOfDay: $hour")
        return LocalMonthDayTime(
            MonthDay.from(toLocalDate()),
            LocalTime.of(hour, localTime.minute, localTime.second, localTime.nano)
        )
    }

    /**
     * Returns a copy of this `LocalMonthDayTime` with the specified minute-of-hour value.
     *
     * The minute value must be valid, ranging from 0 to 59 inclusive. If the provided
     * `minute` parameter is out of range, an exception will be thrown.
     *
     * @param minute The minute-of-hour to set in the result, from 0 to 59.
     * @return A new instance of `LocalMonthDayTime` with the specified minute value.
     * @throws DateTimeException If the provided minute value is invalid.
     * @since 1.0.0
     */
    fun withMinute(minute: Int): LocalMonthDayTime {
        minute in 0..59 || throw DateTimeException("Invalid value for MinuteOfHour: $minute")
        return LocalMonthDayTime(
            MonthDay.from(toLocalDate()),
            LocalTime.of(localTime.hour, minute, localTime.second, localTime.nano)
        )
    }

    /**
     * Returns a new `LocalMonthDayTime` instance with the second-of-minute value updated
     * to the specified value, maintaining all other date and time components unchanged.
     *
     * @param second the second-of-minute to set, from 0 to 59 (inclusive)
     * @return a new `LocalMonthDayTime` with the updated second value
     * @throws DateTimeException if the provided second value is invalid (less than 0 or greater than 59)
     * @since 1.0.0
     */
    fun withSecond(second: Int): LocalMonthDayTime {
        second in 0..59 || throw DateTimeException("Invalid value for SecondOfMinute: $second")
        return LocalMonthDayTime(
            MonthDay.from(toLocalDate()),
            LocalTime.of(localTime.hour, localTime.minute, second, localTime.nano)
        )
    }

    /**
     * Returns a copy of this `LocalMonthDayTime` with the specified nanosecond-of-second.
     *
     * The value of `nanoOfSecond` must be in the range 0 to 999,999,999; otherwise,
     * an exception is thrown.
     *
     * @param nanoOfSecond the new nanosecond value to set, from 0 to 999,999,999
     * @return a new `LocalMonthDayTime` instance with the updated nanosecond value
     * @throws DateTimeException if the `nanoOfSecond` value is invalid
     * @since 1.0.0
     */
    fun withNano(nanoOfSecond: Int): LocalMonthDayTime {
        nanoOfSecond in 0..999999999 || throw DateTimeException("Invalid value for NanoOfSecond: $nanoOfSecond")
        return LocalMonthDayTime(
            MonthDay.from(toLocalDate()),
            LocalTime.of(localTime.hour, localTime.minute, localTime.second, nanoOfSecond)
        )
    }

    /**
     * Returns a copy of this temporal object with the specified field set to a new value.
     *
     * This method supports fields from the `ChronoField` enumeration, such as `DAY_OF_MONTH`,
     * `MONTH_OF_YEAR`, `HOUR_OF_DAY`, `MINUTE_OF_HOUR`, `SECOND_OF_MINUTE`, and `NANO_OF_SECOND`.
     * If the field is not supported by this object, an `UnsupportedTemporalTypeException` is thrown.
     *
     * @param field the field to set in the result, not null
     * @param newValue the new value for the field
     * @return a copy of this object with the specified field changed, not null
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @since 1.0.0
     */
    override fun with(field: TemporalField, newValue: Long): Temporal {
        if (!isSupported(field)) throw UnsupportedTemporalTypeException("Unsupported field: $field")
        return when (field as ChronoField) {
            ChronoField.DAY_OF_MONTH -> withDay(newValue.toInt())
            ChronoField.MONTH_OF_YEAR -> withMonth(newValue.toInt())
            ChronoField.HOUR_OF_DAY -> withHour(newValue.toInt())
            ChronoField.MINUTE_OF_HOUR -> withMinute(newValue.toInt())
            ChronoField.SECOND_OF_MINUTE -> withSecond(newValue.toInt())
            ChronoField.NANO_OF_DAY -> LocalMonthDayTime(
                MonthDay.from(toLocalDate()),
                LocalTime.ofNanoOfDay(newValue)
            )
            ChronoField.NANO_OF_SECOND -> withNano(newValue.toInt())
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }

    /**
     * Calculates the amount of time until another temporal object in terms of a specified unit.
     *
     * This method computes the time difference between the current `LocalMonthDayTime` instance
     * and the provided `endExclusive` temporal object, in the specified `unit`.
     * If the provided temporal object belongs to a different class, it is converted
     * to a compatible `LocalMonthDayTime` instance before the calculation.
     *
     * @param endExclusive the end temporal object, exclusive, not null.
     *                     Must be compatible with or convertible to `LocalMonthDayTime`.
     * @param unit the temporal unit in which to measure the amount of time, not null.
     * @return the amount of time between this temporal object and the specified temporal
     *         object in terms of the provided unit. The return value may be negative
     *         if the `endExclusive` temporal object occurs before this instance.
     *
     * @since 1.0.0
     */
    @Suppress("LocalVariableName")
    override fun until(endExclusive: Temporal, unit: TemporalUnit): Long {
        var _endExclusive = endExclusive
        if (javaClass != _endExclusive.javaClass)
            _endExclusive = LocalMonthDayTime.from(_endExclusive)
        return LocalDateTime.of(
            Year.now().value,
            monthDay.month,
            monthDay.dayOfMonth,
            localTime.hour,
            localTime.minute,
            localTime.second,
            localTime.nano
        ).until(
            LocalDateTime.of(
                Year.now().value,
                (_endExclusive as LocalMonthDayTime).monthDay.month,
                _endExclusive.monthDay.dayOfMonth,
                _endExclusive.localTime.hour,
                _endExclusive.localTime.minute,
                _endExclusive.localTime.second,
                _endExclusive.localTime.nano
            ), unit
        )
    }

    /**
     * Retrieves the value of the specified `TemporalField` as a `Long`.
     * The `field` must be one of the supported fields: DAY_OF_MONTH, MONTH_OF_YEAR,
     * HOUR_OF_DAY, MINUTE_OF_HOUR, SECOND_OF_MINUTE, or NANO_OF_SECOND.
     *
     * @param field the `TemporalField` to be retrieved. Must be compatible with `ChronoField`.
     * @throws UnsupportedTemporalTypeException if the field is not supported.
     * @return the value of the specified field as a `Long`.
     * @since 1.0.0
     */
    override fun getLong(field: TemporalField) = when (field as ChronoField) {
        ChronoField.DAY_OF_MONTH -> monthDay.dayOfMonth
        ChronoField.MONTH_OF_YEAR -> monthDay.monthValue
        ChronoField.HOUR_OF_DAY -> localTime.hour
        ChronoField.MINUTE_OF_HOUR -> localTime.minute
        ChronoField.SECOND_OF_MINUTE -> localTime.second
        ChronoField.NANO_OF_DAY -> localTime.toNanoOfDay()
        ChronoField.NANO_OF_SECOND -> localTime.nano
        else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
    }.toLong()

    /**
     * Adjusts the specified temporal object to match the date and time components of this instance.
     * This adjusts the temporal object by setting its fields for day of month, month, hour, minute,
     * second, and nanosecond to the corresponding values from this instance.
     *
     * @param temporal the temporal object to adjust, not null
     * @return the adjusted temporal object, not null
     * @since 1.0.0
     */
    override fun adjustInto(temporal: Temporal): Temporal = temporal
        .with(ChronoField.DAY_OF_MONTH, monthDay.dayOfMonth.toLong())
        .with(ChronoField.MONTH_OF_YEAR, monthDay.monthValue.toLong())
        .with(ChronoField.HOUR_OF_DAY, localTime.hour.toLong())
        .with(ChronoField.MINUTE_OF_HOUR, localTime.minute.toLong())
        .with(ChronoField.SECOND_OF_MINUTE, localTime.second.toLong())
        .with(ChronoField.NANO_OF_DAY, localTime.toNanoOfDay())
        .with(ChronoField.NANO_OF_SECOND, localTime.nano.toLong())

    /**
     * Converts the current `LocalMonthDayTime` instance to an `OffsetMonthDayTime` at the specified `ZoneOffset`.
     *
     * @param offset The `ZoneOffset` to apply to the current `LocalMonthDayTime` instance.
     * @since 1.0.0
     */
    fun atOffset(offset: ZoneOffset) = OffsetMonthDayTime.from(toLocalDateTime().atOffset(offset))
    /**
     * Adjusts the current `LocalMonthDayTime` to a specific `OffsetMonthDayTime` based on the
     * offset provided by the given `ZoneIdent`.
     *
     * @param zoneIdent the zone identifier containing the offset to be applied
     * @since 1.0.0
     */
    fun atOffset(zoneIdent: ZoneIdent) = OffsetMonthDayTime.from(toLocalDateTime().atOffset(zoneIdent.offset))

    /**
     * Combines this `LocalMonthDayTime` with a specified time-zone to produce a `ZonedDateTime`.
     *
     * @param zone the time-zone to use, not null
     * @return a `ZonedDateTime` representing the same local date and time, but in the specified time-zone
     * @since 1.0.0
     */
    fun atZone(zone: ZoneId): ZonedDateTime = ZonedDateTime.of(toLocalDateTime(), zone)
    /**
     * Converts the current `LocalMonthDayTime` instance to a `ZonedDateTime` by associating it with the specified time zone.
     *
     * @param zoneIdent the `ZoneIdent` representing the time zone to associate with this `LocalMonthDayTime` instance.
     * @return a `ZonedDateTime` instance with the specified time zone.
     * @since 1.0.0
     */
    fun atZone(zoneIdent: ZoneIdent): ZonedDateTime = ZonedDateTime.of(toLocalDateTime(), zoneIdent.zoneId)

    /**
     * Deconstructs the `monthDay` property to extract and return its `month` value.
     *
     * This function allows the `component1` operator to be used for destructuring declarations,
     * specifically to retrieve the `month` property from the `monthDay` object.
     *
     * @return The `month` component of the `monthDay`.
     *
     * @since 1.0.0
     */
    operator fun component1() = monthDay.month!!
    /**
     * Provides the day of the month as part of the decomposition of a date or time object.
     * This function acts as a component function, often used in destructuring declarations
     * to directly access the `dayOfMonth` field of the `monthDay` property.
     *
     * @return The day of the month, extracted from the `monthDay` property.
     * @since 1.0.0
     */
    operator fun component2() = monthDay.dayOfMonth
    /**
     * Provides the third component of a time-based destructuring declaration,
     * representing the hour portion of the local time.
     *
     * This function is commonly used in destructuring declarations to access
     * the hour value of a `LocalTime` instance for further operation or processing.
     *
     * @return The current hour of the local time as an integer.
     * @since 1.0.0
     */
    operator fun component3() = localTime.hour
    /**
     * Provides the minute component of the `localTime` value.
     *
     * This function is a component function commonly used in destructuring declarations.
     * It returns the `minute` property from the `localTime`.
     *
     * @return the minute component of the `localTime`.
     * @since 1.0.0
     */
    operator fun component4() = localTime.minute
    /**
     * Component function that extracts the `second` value from the `localTime` property.
     * This function is a part of the destructuring declaration mechanism.
     *
     * @return The second component of the `localTime`, as an integer.
     * @since 1.0.0
     */
    operator fun component5() = localTime.second
    /**
     * Provides the nanosecond component of the `localTime` property.
     * This operator function is typically used for destructuring declarations
     * to extract the sixth component from an object.
     *
     * @return The nanoseconds part of the `localTime` property.
     * @since 1.0.0
     */
    operator fun component6() = localTime.nano
}