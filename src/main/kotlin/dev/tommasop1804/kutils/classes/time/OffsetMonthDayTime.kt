package dev.tommasop1804.kutils.classes.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.classes.time.LocalMonthDayTime.Companion.toLocalMonthDayTime
import dev.tommasop1804.kutils.classes.time.OffsetMonthDayTime.Companion.MICROS_PER_SECOND
import dev.tommasop1804.kutils.classes.time.OffsetMonthDayTime.Companion.NANOS_PER_SECOND
import dev.tommasop1804.kutils.classes.time.OffsetMonthDayTime.Companion.SECONDS_PER_DAY
import dev.tommasop1804.kutils.classes.time.OffsetMonthDayTime.Companion.SECONDS_PER_MINUTE
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
import java.time.temporal.*
import java.util.*
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE
import kotlin.reflect.KProperty
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

/**
 * Represents a date-time object that includes a month, day, time, and an offset.
 * This class provides various methods to manipulate and compare instances
 * based on leap year settings and temporal adjustments.
 *
 * @property monthDayTime the underlying combination of the month, day, and time
 * @property offset the offset from UTC/Greenwich
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = OffsetMonthDayTime.Companion.Serializer::class)
@JsonDeserialize(using = OffsetMonthDayTime.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OffsetMonthDayTime.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OffsetMonthDayTime.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_getorthrow_as_invoke", "kutils_temporal_now_as_temporal", "kutils_temporal_of_as_temporal", "kutils_temporal_parse_as_temporal", "kutils_take_as_int_invoke")
class OffsetMonthDayTime(val monthDayTime: LocalMonthDayTime, val offset: ZoneOffset) : Temporal, TemporalAccessor, TemporalAdjuster, Comparable<OffsetMonthDayTime>, Serializable {
    /**
     * Creates a new instance of `OffsetMonthDayTime` based on the given `MonthDay`, `LocalTime`, and `ZoneIdent`.
     *
     * This method combines the provided `MonthDay` and `LocalTime` into a `LocalMonthDayTime`,
     * and uses the `ZoneIdent` to determine the offset for the resulting `OffsetMonthDayTime`.
     *
     * @param monthDay the `MonthDay` representing the month and day part of the date-time, not null
     * @param localTime the `LocalTime` representing the time part of the date-time, not null
     * @param zoneIdent the `ZoneIdent` containing the time-zone and offset information, not null
     * @return a new `OffsetMonthDayTime` instance combining the specified components
     * @since 1.0.0
     */
    constructor(monthDay: MonthDay, localTime: LocalTime, zoneIdent: ZoneIdent) : this(LocalMonthDayTime(monthDay, localTime), zoneIdent.offset)
    /**
     * Constructs an instance by combining a MonthDay and a LocalTime with the provided ZoneOffset.
     *
     * This constructor initializes the object using a LocalMonthDayTime instance created
     * from the given MonthDay and LocalTime, and then associates it with the specified ZoneOffset.
     *
     * @param monthDay the month and day, not null
     * @param localTime the local time, not null
     * @param zoneIdent the time zone offset, not null
     * @since 1.0.0
     */
    constructor(monthDay: MonthDay, localTime: LocalTime, zoneIdent: ZoneOffset) : this(LocalMonthDayTime(monthDay, localTime), zoneIdent)

    /**
     * Creates an instance of `OffsetMonthDayTime` from the specified date, time components, and offset.
     *
     * @param month the month of the year, from 1 (January) to 12 (December)
     * @param day the day of the month, from 1 to the maximum allowed for the specified month
     * @param hour the hour of the day, from 0 to 23 (optional, default is 0)
     * @param minute the minute of the hour, from 0 to 59 (optional, default is 0)
     * @param second the second of the minute, from 0 to 59 (optional, default is 0)
     * @param nanoOfSecond the nanosecond of the second, from 0 to 999,999,999 (optional, default is 0)
     * @param offset the offset from UTC/Greenwich, not null
     * @return a new `OffsetMonthDayTime` instance representing the specified date, time, and offset
     * @since 1.0.0
     */
    constructor(
        month: Int,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        nanoOfSecond: Int = 0,
        offset: ZoneOffset
    ) : this(LocalMonthDayTime(MonthDay.of(month, day), LocalTime.of(hour, minute, second, nanoOfSecond)), offset)

    /**
     * Creates an instance of `OffsetMonthDayTime` using the provided month, day, time components, and time zone.
     *
     * @param month The month of the year, from 1 (January) to 12 (December).
     * @param day The day of the month, from 1 to 31, depending on the month.
     * @param hour The hour of the day, from 0 to 23. Defaults to 0 if not provided.
     * @param minute The minute of the hour, from 0 to 59. Defaults to 0 if not provided.
     * @param second The second of the minute, from 0 to 59. Defaults to 0 if not provided.
     * @param nanoOfSecond The nanosecond within the second, from 0 to 999,999,999. Defaults to 0 if not provided.
     * @param zoneIdent The `ZoneIdent` representing the time zone to apply to the date-time.
     * @return A new `OffsetMonthDayTime` instance representing the given date-time and time zone.
     * @since 1.0.0
     */
    constructor(
        month: Int,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        nanoOfSecond: Int = 0,
        zoneIdent: ZoneIdent
    ) : this(LocalMonthDayTime(MonthDay.of(month, day), LocalTime.of(hour, minute, second, nanoOfSecond)),zoneIdent.offset)

    /**
     * Constructs a new `OffsetMonthDayTime` by combining the specified `Month`, day of the month,
     * time components (hour, minute, second, and nanoseconds), and a specific `ZoneOffset`.
     *
     * @param month the month of the year to use, not null
     * @param day the day of the month to use, must be valid for the specified month
     * @param hour the hour to use, default is 0
     * @param minute the minute to use, default is 0
     * @param second the second to use, default is 0
     * @param nanoOfSecond the nanosecond to use, default is 0
     * @param offset the time zone offset to use, not null
     * @return a new `OffsetMonthDayTime` instance combining the provided components
     * @since 1.0.0
     */
    constructor(
        month: Month,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        nanoOfSecond: Int = 0,
        offset: ZoneOffset
    ) : this(LocalMonthDayTime(MonthDay.of(month, day), LocalTime.of(hour, minute, second, nanoOfSecond)), offset)

    /**
     * Creates an instance of `OffsetMonthDayTime` based on the specified month, day, time components, and zone identifier.
     *
     * @param month the month of the year, not null
     * @param day the day of the month, must be valid for the month
     * @param hour the hour of the day, from 0 to 23 (default is 0)
     * @param minute the minute of the hour, from 0 to 59 (default is 0)
     * @param second the second of the minute, from 0 to 59 (default is 0)
     * @param nanoOfSecond the nanosecond of the second, from 0 to 999,999,999 (default is 0)
     * @param zoneIdent the zone identifier providing the offset information, not null
     * @return the created `OffsetMonthDayTime` instance
     * @since 1.0.0
     */
    constructor(
        month: Month,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        nanoOfSecond: Int = 0,
        zoneIdent: ZoneIdent
    ) : this(LocalMonthDayTime(MonthDay.of(month, day), LocalTime.of(hour, minute, second, nanoOfSecond)), zoneIdent.offset)

    /**
     * Creates an instance of `OffsetMonthDayTime` from the specified `LocalDate`, `LocalTime`, and `ZoneOffset`.
     *
     * @param localDate the local date part of the combined date-time, not null
     * @param localTime the local time part of the combined date-time, not null
     * @param offset the zone offset to associate with the combined date-time, not null
     * @return a new `OffsetMonthDayTime` instance representing the specified `LocalDate`, `LocalTime`, and `ZoneOffset`
     * @since 1.0.0
     */
    constructor(localDate: LocalDate, localTime: LocalTime, offset: ZoneOffset) : this(LocalMonthDayTime(MonthDay.from(localDate), localTime), offset)

    /**
     * Creates an instance of `OffsetMonthDayTime` from the given date, time, and zone identifier.
     *
     * @param localDate The `LocalDate` instance representing the date part. Must not be null.
     * @param localTime The `LocalTime` instance representing the time part. Must not be null.
     * @param zoneIdent The `ZoneIdent` instance representing the zone identifier, which includes the offset. Must not be null.
     * @return A new instance of `OffsetMonthDayTime` with the specified date, time, and offset information derived from the zone identifier.
     * @since 1.0.0
     */
    constructor(localDate: LocalDate, localTime: LocalTime, zoneIdent: ZoneIdent) : this(LocalMonthDayTime(MonthDay.from(localDate), localTime), zoneIdent.offset)

    /**
     * Creates an instance of `OffsetMonthDayTime` using the provided `Instant` and `ZoneId`.
     *
     * @param instant the instant to convert, not null
     * @param zoneId the time zone to apply, not null
     * @return an instance of `OffsetMonthDayTime` based on the provided instant and zone
     * @since 1.0.0
     */
    constructor(instant: Instant, zoneId: ZoneId) : this(from(LocalDateTime.ofInstant(instant, zoneId)))
    /**
     * Constructs an instance by converting the given `Instant` and `ZoneIdent`
     * into a LocalDateTime representation and initializes the object using
     * the converted value.
     *
     * @param instant the instant to be converted
     * @param zoneIdent the zone identification containing the offset to
     *                  interpret the instant into a LocalDateTime
     * @since 1.0.0
     */
    constructor(instant: Instant, zoneIdent: ZoneIdent) : this(from(LocalDateTime.ofInstant(instant, zoneIdent.offset)))
    /**
     * Creates an instance of `OffsetMonthDayTime` using the provided `Instant` and `ZoneId`.
     *
     * @param instant the instant to convert, not null
     * @param zoneId the time zone to apply, not null
     * @return an instance of `OffsetMonthDayTime` based on the provided instant and zone
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(instant: kotlin.time.Instant, zoneId: ZoneId) : this(from(LocalDateTime.ofInstant(instant.toJavaInstant(), zoneId)))
    /**
     * Constructs an instance by converting the given `Instant` and `ZoneIdent`
     * into a LocalDateTime representation and initializes the object using
     * the converted value.
     *
     * @param instant the instant to be converted
     * @param zoneIdent the zone identification containing the offset to
     *                  interpret the instant into a LocalDateTime
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(instant: kotlin.time.Instant, zoneIdent: ZoneIdent) : this(from(LocalDateTime.ofInstant(instant.toJavaInstant(), zoneIdent.offset)))

    /**
     * Secondary constructor that initializes an instance of the class
     * with the current timestamp or time generated by the `now()` function.
     *
     * @since 1.0.0
     */
    constructor() : this(now())
    /**
     * Secondary constructor that initializes the object with the current instant
     * based on the provided Clock.
     *
     * @param clock The clock instance used to determine the current instant.
     * @since 1.0.0
     */
    constructor(clock: Clock) : this(now(clock))
    /**
     * Constructs a new instance of the class using the specified time zone.
     *
     * @param zone The time zone to be used to determine the current time.
     * @since 1.0.0
     */
    constructor(zone: ZoneId) : this(now(zone))
    /**
     * Constructs an instance using the current date-time in the specified time zone.
     *
     * @param zone the time zone to be used for determining the current date-time, not null
     * @since 1.0.0
     */
    constructor(zone: ZoneIdent) : this(now(zone))

    /**
     * Private secondary constructor for creating an instance from an existing OffsetMonthDayTime object.
     *
     * This constructor initializes the instance using the monthDayTime and offset properties
     * of the provided OffsetMonthDayTime instance.
     *
     * @param omdt The OffsetMonthDayTime instance from which to construct a new object.
     * @since 1.0.0
     */
    private constructor(omdt: OffsetMonthDayTime) : this(omdt.monthDayTime, omdt.offset)

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
        @Serial
        private const val serialVersionUID = 4954918890077093841L

        /**
         * Represents the minimum possible value of `OffsetMonthDayTime`.
         *
         * The value is created by combining the earliest possible `MonthDay` (January 1st) and
         * the minimum possible `LocalTime` (00:00:00.000), with the offset defined by `MilitaryTimeZone.Z`.
         *
         * This constant can be used as a reference point for calculations or comparisons involving
         * `OffsetMonthDayTime` instances.
         *
         * @since 1.0.0
         */
        val MIN: OffsetMonthDayTime =
            OffsetMonthDayTime(LocalMonthDayTime(MonthDay.of(1, 1), LocalTime.MIN), TimeZoneDesignator.Z.offset)

        /**
         * A predefined constant representing the maximum supported value for an instance of `OffsetMonthDayTime`.
         * This value is set to the combination of the last possible `MonthDay` (December 31) with the maximum
         * supported local time of the day (`LocalTime.MAX`) and the largest valid timezone offset (`MilitaryTimeZone.Z.offset`).
         *
         * This constant can be used for comparisons, boundary conditions, or as a replacement for undefined or maximum
         * representation of an `OffsetMonthDayTime`.
         *
         * @since 1.0.0
         */
        val MAX: OffsetMonthDayTime =
            OffsetMonthDayTime(LocalMonthDayTime(MonthDay.of(12, 31), LocalTime.MAX), TimeZoneDesignator.Z.offset)

        /**
         * Represents the default leap year to be used in calculations involving leap years.
         *
         * This constant is primarily utilized within the context of date and time operations that
         * require consistent reference to a predetermined leap year. By default, the year 2024
         * is chosen, as it is a nearby leap year adhering to the Gregorian calendar rules.
         *
         * @since 1.0.0
         */
        private const val DEFAULT_LEAP_YEAR: Int = 2024

        /**
         * Represents the default year to use for operations when a non-leap year context is required.
         *
         * This constant is primarily utilized within the `OffsetMonthDayTime` class
         * for date and time calculations that depend on whether the year is a leap year or not.
         * When a specific year is not provided in such scenarios, this default non-leap year value is used.
         *
         * The chosen year, 2025, is a non-leap year in the Gregorian calendar.
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
         * Creates an instance of `OffsetMonthDayTime` from the specified `TemporalAccessor`.
         *
         * The method extracts the `MonthDay`, `LocalTime`, and `ZoneOffset` from the given
         * `TemporalAccessor` to form a corresponding `OffsetMonthDayTime` instance.
         *
         * @param temporalAccessor the temporal accessor to extract the `MonthDay`, `LocalTime`,
         * and `ZoneOffset` from, not null
         * @return a new `OffsetMonthDayTime` instance representing the extracted values
         * @since 1.0.0
         */
        @JvmStatic
        fun from(temporalAccessor: TemporalAccessor) =
            OffsetMonthDayTime(
                LocalMonthDayTime(MonthDay.from(temporalAccessor), LocalTime.from(temporalAccessor)),
                ZoneOffset.from(temporalAccessor)
            )

        /**
         * Creates an `OffsetMonthDayTime` instance from the specified temporal object.
         * Extracts the `MonthDay` and `LocalTime` components, along with the `ZoneOffset`,
         * to construct a new `OffsetMonthDayTime`.
         *
         * @param temporal the temporal object to derive the `OffsetMonthDayTime` from, not null
         * @return a new `OffsetMonthDayTime` instance based on the specified temporal object
         * @since 1.0.0
         */
        @JvmStatic
        fun from(temporal: Temporal) =
            OffsetMonthDayTime(LocalMonthDayTime(MonthDay.from(temporal), LocalTime.from(temporal)), ZoneOffset.from(temporal))

        /**
         * Creates an instance of `OffsetMonthDayTime` based on the current date, time, and offset obtained from the specified clock.
         *
         * @param clock the clock to obtain the current date, time, and zone offset, not null
         * @return a new `OffsetMonthDayTime` instance representing the current `MonthDay`, `LocalTime`, and `ZoneOffset` derived from the clock
         * @since 1.0.0
         */
        @JvmStatic
        fun now(clock: Clock) = OffsetMonthDayTime(
            LocalMonthDayTime(MonthDay.now(clock), LocalTime.now(clock)),
            clock.zone.rules.getOffset(clock.instant())
        )

        /**
         * Creates a new instance of `OffsetMonthDayTime` using the current month, day, and time
         * retrieved from the system clock in the specified time zone.
         *
         * @param zoneIdent The time zone identifier, which determines the time zone offset to be used.
         * @return A new `OffsetMonthDayTime` instance representing the current date and time in the given offset.
         * @since 1.0.0
         */
        @JvmStatic
        fun now(zoneIdent: ZoneIdent) = OffsetMonthDayTime(LocalMonthDayTime(MonthDay.now(zoneIdent.zoneId), LocalTime.now(zoneIdent.zoneId)), zoneIdent.offset)

        /**
         * Obtains the current OffsetMonthDayTime for the specified time zone.
         *
         * @param zoneId the time zone ID to use for determining the current date and time
         * @since 1.0.0
         */
        @JvmStatic
        fun now(zoneId: ZoneId) = OffsetMonthDayTime(LocalMonthDayTime(MonthDay.now(zoneId), LocalTime.now(zoneId)), zoneId.rules.getOffset(Instant.now()))

        /**
         * Returns the current date and time as an instance of `OffsetMonthDayTime`.
         * The `MonthDay` and `LocalTime` components are based on the system's default clock,
         * while the offset is derived from the system's current default time zone rules.
         *
         * @return an instance of `OffsetMonthDayTime` representing the current date, time, and offset
         * @since 1.0.0
         */
        @JvmStatic
        fun now() = OffsetMonthDayTime(
            LocalMonthDayTime(MonthDay.now(), LocalTime.now()),
            ZoneId.systemDefault().rules.getOffset(Instant.now())
        )

        /**
         * Parses the given string to create an instance of [OffsetMonthDayTime].
         *
         * The string input should represent a local month-day-time and an offset,
         * separated into distinct parts. If the string ends with 'Z', it is treated as
         * having a UTC offset. If the input is blank, the current instance is returned.
         *
         * @param cs the string to parse, representing a local month-day-time with an offset
         * @return an [OffsetMonthDayTime] instance parsed from the given string
         * @since 1.0.0
         */
        @JvmStatic
        fun parse(cs: CharSequence) = runCatching {
            if (cs.isBlank()) now()
            else {
                val symbol = if (cs.endsWith("Z")) 'Z' else if (cs.lastIndexOf('+') > cs.lastIndexOf('-')) '+' else '-'
                val offsetPart = cs.substring(cs.lastIndexOf(symbol))
                val offset = ZoneOffset.of(offsetPart)
                val localMonthDayTime = LocalMonthDayTime.parse(cs.take(cs.lastIndexOf(symbol))).getOrThrow()
                OffsetMonthDayTime(localMonthDayTime, offset)
            }
        }

        /**
         * Converts a [Temporal] object into an instance of [OffsetMonthDayTime].
         *
         * This method extracts the Month, Day, LocalTime, and ZoneOffset from the given [Temporal] object
         * and creates a new [OffsetMonthDayTime] instance representing this information.
         *
         * The returned instance encapsulates the combined concept of a specific month, day, time,
         * and offset from UTC.
         *
         * @receiver A [Temporal] object from which the [OffsetMonthDayTime] will be created. The temporal must
         * contain sufficient information to extract a [MonthDay], [LocalTime], and [ZoneOffset].
         *
         * @return An instance of [OffsetMonthDayTime] derived from the provided [Temporal].
         * @throws DateTimeException if the input [Temporal] does not contain the required information.
         *
         * @since 1.0.0
         */
        fun Temporal.toOffsetMonthDayTime() = from(this)
        /**
         * Converts a given [TemporalAccessor] to an instance of [OffsetMonthDayTime].
         *
         * This method extracts the [MonthDay], [LocalTime], and [ZoneOffset] from the specified
         * [TemporalAccessor] and combines them into an [OffsetMonthDayTime] instance.
         *
         * @receiver The [TemporalAccessor] from which the [OffsetMonthDayTime] will be created.
         * @return An [OffsetMonthDayTime] instance representing the month, day, time, and offset from the provided [TemporalAccessor].
         * @throws DateTimeException If any required fields are invalid or missing from the [TemporalAccessor].
         * @since 1.0.0
         */
        fun TemporalAccessor.toOffsetMonthDayTime() = from(this)

        class Serializer : ValueSerializer<OffsetMonthDayTime>() {
            override fun serialize(
                value: OffsetMonthDayTime,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<OffsetMonthDayTime>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = parse(p.string).getOrThrow()
        }

        class OldSerializer : JsonSerializer<OffsetMonthDayTime>() {
            override fun serialize(value: OffsetMonthDayTime, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.toString())
            }
        }

        class OldDeserializer : JsonDeserializer<OffsetMonthDayTime>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext) = parse(p.text).getOrThrow()
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<OffsetMonthDayTime?, String?> {
            override fun convertToDatabaseColumn(attribute: OffsetMonthDayTime?) = if (Objects.isNull(attribute)) null else attribute.toString()
            override fun convertToEntityAttribute(dbData: String?) = if (dbData.isNull()) null else parse(dbData).getOrThrow()
        }
    }

    /**
     * Creates a copy of this `OffsetMonthDayTime` instance with the specified `monthDayTime` and `offset`.
     * If no parameters are provided, the original values are retained.
     *
     * @param monthDayTime the `LocalMonthDayTime` to be used in the new instance, defaulting to the current instance's `monthDayTime`
     * @param offset the `ZoneOffset` to be used in the new instance, defaulting to the current instance's `offset`
     * @return a new `OffsetMonthDayTime` instance with the specified or retained attributes
     * @since 1.0.0
     */
    fun copy(monthDayTime: LocalMonthDayTime = this.monthDayTime, offset: ZoneOffset = this.offset) = OffsetMonthDayTime(monthDayTime, offset)

    /**
     * Checks if the current instance is before the specified `other` instance
     * when converted to the LocalDateTime using the specified leap year settings.
     *
     * The comparison uses a `MilitaryTimeZone.Z` conversion for both instances,
     * adjusting the year for leap year considerations based on the provided parameters.
     *
     * @param other The `LocalMonthDayTime` instance to compare with.
     * @param firstLeap Boolean indicating whether the current instance should consider a leap year.
     * @param secondLeap Boolean indicating whether the `other` instance should consider a leap year.
     * @return `true` if the current instance is before the `other` instance; `false` otherwise.
     * @since 1.0.0
     */
    fun isBefore(other: OffsetMonthDayTime, firstLeap: Boolean, secondLeap: Boolean) =
        TimeZoneDesignator.Z.convert(toLocalDateTime(if (firstLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR))
            .isBefore(TimeZoneDesignator.Z.convert(other.toLocalDateTime(if (secondLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR)))

    /**
     * Determines whether the current instance occurs after the specified `other` instance.
     * The comparison takes into account the provided `firstLeap` and `secondLeap` flags
     * to handle leap year adjustments during the computation.
     *
     * @param other the `LocalMonthDayTime` instance to compare against.
     * @param firstLeap a flag indicating whether the current instance should consider a leap year adjustment.
     * @param secondLeap a flag indicating whether the `other` instance should consider a leap year adjustment.
     * @return `true` if the current instance is after the `other` instance after the conversion; `false` otherwise.
     * @since 1.0.0
     */
    fun isAfter(other: OffsetMonthDayTime, firstLeap: Boolean, secondLeap: Boolean) =
        TimeZoneDesignator.Z.convert(toLocalDateTime(if (firstLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR))
            .isAfter(TimeZoneDesignator.Z.convert(other.toLocalDateTime(if (secondLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR)))

    /**
     * Compares this OffsetMonthDayTime instance to another LocalMonthDayTime instance
     * to determine if they are equal, considering the specified leap year rules.
     *
     * @param other the other LocalMonthDayTime to compare with.
     * @param firstLeap a flag indicating if the first instance should consider leap year adjustments.
     * @param secondLeap a flag indicating if the second instance should consider leap year adjustments.
     * @return `true` if both instances are equal based on the comparison rules, otherwise `false`.
     * @since 1.0.0
     */
    fun isEqual(other: OffsetMonthDayTime, firstLeap: Boolean, secondLeap: Boolean): Boolean {
        return TimeZoneDesignator.Z.convert(this.toLocalMonthDayTime())
            .isEqual(TimeZoneDesignator.Z.convert(other.toLocalMonthDayTime()), firstLeap, secondLeap)
    }

    /**
     * Generates the start of the day representation for the provided temporal class type.
     *
     * This method calculates a "start of day" instance for various temporal classes by setting the time
     * to the beginning of the day (e.g., midnight or zero-hour values), depending on the type
     * of the temporal class provided.
     *
     * @param T the temporal type for which the start of the day is to be calculated
     * @param `class` the class object representing the temporal type (e.g., `LocalDate`, `LocalDateTime`, etc.)
     * @return an instance of the specified temporal type representing the start of the day
     * @throws DateTimeException if the provided class type is not supported
     * @since 1.0.0
     */
    fun <T : Temporal> atStartOfDay(`class`: Class<T>): T {
        return when (`class`.name) {
            "java.time.LocalDate" -> `class`.cast(
                LocalDate.of(
                    monthDayTime.monthDay.month.value,
                    monthDayTime.monthDay.dayOfMonth,
                    monthDayTime.monthDay.month.minLength()
                )
            )

            "java.time.LocalDateTime" -> `class`.cast(
                LocalDateTime.of(
                    Year.now().value,
                    monthDayTime.monthDay.dayOfMonth,
                    monthDayTime.monthDay.month.minLength(),
                    0, 0, 0, 0
                )
            )

            "java.time.LocalTime" -> `class`.cast(LocalTime.MIDNIGHT)
            "dev.tommasop1804.kutils.classes.time.LocalMonthDayTime" -> `class`.cast(
                LocalMonthDayTime(
                    monthDayTime.monthDay,
                    LocalTime.MIDNIGHT
                )
            )

            "dev.tommasop1804.kutils.classes.time.OffsetMonthDayTime" -> `class`.cast(
                OffsetMonthDayTime(
                    LocalMonthDayTime(
                        monthDayTime.monthDay,
                        LocalTime.MIDNIGHT
                    ), offset
                )
            )

            "dev.tommasop1804.kutils.classes.time.ZonedMonthDayTime" -> `class`.cast(
                ZonedMonthDayTime(
                    monthDayTime.monthDay,
                    LocalTime.MIDNIGHT,
                    offset
                )
            )

            else -> throw DateTimeException("Unsupported class: " + `class`.getName())
        }
    }

    /**
     * Calculates the end of the day representation for the specified temporal class type.
     *
     * This method determines the end of day value based on the provided `Temporal` class type,
     * such as `LocalDate`, `LocalDateTime`, `LocalTime`, or other custom temporal classes.
     *
     * @param T the type of `Temporal` for which the end of the day is calculated
     * @param `class` the class of the `Temporal` for which the end of the day should be computed
     * @return the end of the day instance of the given `Temporal` class
     * @throws DateTimeException if the specified class is not supported
     * @since 1.0.0
     */
    fun <T : Temporal> atEndOfDay(`class`: Class<T>): T {
        return when (`class`.name) {
            "java.time.LocalDate" -> `class`.cast(
                LocalDate.of(
                    Year.now().value,
                    monthDayTime.monthDay.dayOfMonth,
                    monthDayTime.monthDay.month.maxLength()
                )
            )
            "java.time.LocalDateTime" -> `class`.cast(
                LocalDateTime.of(
                    Year.now().value,
                    monthDayTime.monthDay.dayOfMonth,
                    monthDayTime.monthDay.month.maxLength(),
                    23, 59, 59, 999999999
                )
            )
            "java.time.LocalTime" -> `class`.cast(LocalTime.MAX)
            "dev.tommasop1804.kutils.classes.time.LocalMonthDayTime" -> `class`.cast(LocalMonthDayTime(monthDayTime.monthDay, LocalTime.MAX))
            "dev.tommasop1804.kutils.classes.time.OffsetMonthDayTime" -> `class`.cast(
                OffsetMonthDayTime(
                    LocalMonthDayTime(
                        monthDayTime.monthDay,
                        LocalTime.MAX
                    ), offset
                )
            )
            "dev.tommasop1804.kutils.classes.time.ZonedMonthDayTime" -> `class`.cast(
                ZonedMonthDayTime(
                    monthDayTime.monthDay,
                    LocalTime.MAX,
                    offset
                )
            )
            else -> throw DateTimeException("Unsupported class: " + `class`.getName())
        }
    }

    /**
     * Returns a new OffsetMonthDayTime instance with the specified number of months added.
     * If the `leap` parameter is true, operations are performed considering a leap year;
     * otherwise, a non-leap year is used.
     *
     * @param amountToAdd the number of months to add, may be negative
     * @param leap indicates whether to use a leap year for calculations, default is false
     * @since 1.0.0
     */
    fun plusMonths(amountToAdd: Long, leap: Boolean = false) = OffsetMonthDayTime(
        LocalMonthDayTime(
            MonthDay.from(
                LocalDate.of(
                    if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
                    monthDayTime.monthDay.month,
                    monthDayTime.monthDay.dayOfMonth
                ).plusMonths(amountToAdd)
            ),
            monthDayTime.localTime
        ), offset
    )

    /**
     * Adds the specified number of weeks to this `OffsetMonthDayTime` instance, returning a new instance.
     * The `leap` parameter indicates whether leap years should be considered during the calculation.
     *
     * @param amountToAdd the number of weeks to add, positive or negative
     * @param leap whether to consider leap years during the calculation
     * @return a new `OffsetMonthDayTime` instance with the specified number of weeks added
     * @since 1.0.0
     */
    fun plusWeeks(amountToAdd: Long, leap: Boolean = false) = OffsetMonthDayTime(
        LocalMonthDayTime(
            MonthDay.from(
                LocalDate.of(
                    if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
                    monthDayTime.monthDay.month,
                    monthDayTime.monthDay.dayOfMonth
                ).plusWeeks(amountToAdd)
            ),
            monthDayTime.localTime
        ), offset
    )

    /**
     * Adds the specified number of days to the current instance, adjusting the result based on whether the year
     * is a leap year or not.
     *
     * @param amountToAdd the number of days to add, positive or negative
     * @param leap specifies if the operation should consider a leap year; defaults to false
     * @return a new OffsetMonthDayTime instance with the specified number of days added
     * @since 1.0.0
     */
    fun plusDays(amountToAdd: Long, leap: Boolean = false) = OffsetMonthDayTime(
        LocalMonthDayTime(
            MonthDay.from(
                LocalDate.of(
                    if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
                    monthDayTime.monthDay.month,
                    monthDayTime.monthDay.dayOfMonth
                ).plusDays(amountToAdd)
            ),
            monthDayTime.localTime
        ), offset
    )

    /**
     * Adds the specified number of hours to this instance of `OffsetMonthDayTime`.
     *
     * The method adjusts the time based on the provided number of hours and whether the leap year
     * adjustment is applied. The result is calculated with potential overflow management.
     *
     * @param amountToAdd the number of hours to add, may be negative.
     * @param leap whether the calculation should consider leap year (default is false).
     * @since 1.0.0
     */
    fun plusHours(amountToAdd: Long, leap: Boolean = false) = plusWithOverflow(
        LocalDate.of(
            if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
            monthDayTime.monthDay.month,
            monthDayTime.monthDay.dayOfMonth
        ),
        amountToAdd,
        0,
        0,
        0
    )

    /**
     * Adds the specified number of minutes to the current OffsetMonthDayTime instance.
     *
     * @param amountToAdd The number of minutes to add, positive or negative.
     * @param leap Indicates whether to consider a leap year for date calculations. Default is false.
     * @since 1.0.0
     */
    fun plusMinutes(amountToAdd: Long, leap: Boolean = false) = plusWithOverflow(
        LocalDate.of(
            if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
            monthDayTime.monthDay.month,
            monthDayTime.monthDay.dayOfMonth
        ),
        0,
        amountToAdd,
        0,
        0
    )

    /**
     * Adds the specified number of seconds to this OffsetMonthDayTime instance, taking into account leap year handling.
     *
     * @param amountToAdd The number of seconds to add.
     * @param leap Whether the operation should consider a leap year. Defaults to false.
     * @since 1.0.0
     */
    fun plusSeconds(amountToAdd: Long, leap: Boolean = false) = plusWithOverflow(
        LocalDate.of(
            if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
            monthDayTime.monthDay.month,
            monthDayTime.monthDay.dayOfMonth
        ),
        0,
        0,
        amountToAdd,
        0
    )

    /**
     * Adds the specified number of nanoseconds to this `OffsetMonthDayTime` instance.
     *
     * @param amountToAdd The number of nanoseconds to be added. It can be positive or negative.
     * @param leap Indicates whether the addition should be computed using leap year rules.
     *             If true, the operation takes leap year variations into consideration;
     *             otherwise, it assumes a non-leap year.
     * @since 1.0.0
     */
    fun plusNanos(amountToAdd: Long, leap: Boolean = false) = plusWithOverflow(
        LocalDate.of(
            if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
            monthDayTime.monthDay.month,
            monthDayTime.monthDay.dayOfMonth
        ),
        0,
        0,
        0,
        amountToAdd
    )

    /**
     * Adds the specified time components (hours, minutes, seconds, and nanoseconds) to the given date,
     * computing the resultant `OffsetMonthDayTime` while handling potential overflow of time units.
     *
     * @param newDate the base date to which the time components will be added
     * @param hours the number of hours to add
     * @param minutes the number of minutes to add
     * @param seconds the number of seconds to add
     * @param nanos the number of nanoseconds to add
     * @return an `OffsetMonthDayTime` object that represents the result of adding the specified time components to the base date
     * @since 1.0.0
     */
    private fun plusWithOverflow(
        newDate: LocalDate,
        hours: Long,
        minutes: Long,
        seconds: Long,
        nanos: Long
    ): OffsetMonthDayTime {
        if ((hours or minutes or seconds or nanos) == 0L) return OffsetMonthDayTime(
            LocalMonthDayTime(
                MonthDay.from(newDate),
                monthDayTime.localTime
            ), offset
        )
        var totDays =
            nanos / NANOS_PER_DAY + seconds / SECONDS_PER_DAY + minutes / MINUTES_PER_DAY + hours / HOURS_PER_DAY
        var totNanos =
            nanos % NANOS_PER_DAY + (seconds % SECONDS_PER_DAY) * NANOS_PER_SECOND + (minutes % MINUTES_PER_DAY) * NANOS_PER_MINUTE + (hours % HOURS_PER_DAY) * NANOS_PER_HOUR
        val curNoD = monthDayTime.localTime.toNanoOfDay()
        totNanos += curNoD
        totDays += Math.floorDiv(totNanos, NANOS_PER_DAY)
        val newNoD = Math.floorMod(totNanos, NANOS_PER_DAY)
        val newTime = if (newNoD == curNoD) monthDayTime.localTime else LocalTime.ofNanoOfDay(newNoD)
        return OffsetMonthDayTime(LocalMonthDayTime(MonthDay.from(newDate.plusDays(totDays)), newTime), offset)
    }

    /**
     * Adds the specified amount of time to this `OffsetMonthDayTime` instance using the provided `TemporalUnit` and
     * returns a new instance with the applied adjustment.
     *
     * The unit determines the type of duration to add (e.g., days, months, hours), while the `leap` flag determines
     * whether leap year rules are applied when the date is adjusted.
     *
     * @param amountToAdd the amount of the specified unit to add, may be negative
     * @param unit the `TemporalUnit` defining the unit of time to add, not null
     * @param leap whether leap year rules are applied during adjustment
     * @return a new `OffsetMonthDayTime` instance with the specified adjustment
     * @since 1.0.0
     */
    fun plus(amountToAdd: Long, unit: TemporalUnit, leap: Boolean): OffsetMonthDayTime {
        if (unit is ChronoUnit) {
            return when (unit) {
                ChronoUnit.NANOS -> plusNanos(amountToAdd, leap)
                ChronoUnit.MICROS -> plusDays(
                    amountToAdd / MICROS_PER_DAY,
                    leap
                ).plusNanos((amountToAdd % MICROS_PER_DAY) * 1000, leap)

                ChronoUnit.MILLIS -> plusDays(
                    amountToAdd / MILLIS_PER_DAY,
                    leap
                ).plusNanos((amountToAdd % MILLIS_PER_DAY) * 1000000, leap)

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
     * Adds the specified amount to this temporal object using the specified temporal unit.
     *
     * @param amountToAdd the amount of the specified unit to add, may be negative
     * @param unit the unit of the amount to add, not null
     * @return an updated copy of this temporal object with the specified amount added
     * @since 1.0.0
     */
    override fun plus(amountToAdd: Long, unit: TemporalUnit) = plus(amountToAdd, unit, false)

    /**
     * Subtracts the specified number of months from this instance, considering whether the year is a leap year.
     * If the specified amount to subtract equals `Long.MIN_VALUE`, the operation is adjusted to avoid overflow.
     *
     * @param amountToSubtract the number of months to subtract, can be negative to effectively add months
     * @param leap a flag indicating whether the computation should consider a leap year
     * @since 1.0.0
     */
    fun minusMonths(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusMonths(MAX_VALUE, leap).plusMonths(1, leap)
        else plusMonths(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of weeks from this OffsetMonthDayTime instance.
     * If the value to subtract is {@code Long.MIN_VALUE}, it handles overflow by adding the maximum value and subtracting one extra week.
     *
     * @param amountToSubtract the number of weeks to subtract, may be negative
     * @param leap a boolean indicating whether to adjust for leap year specifics. Default is {@code false}
     * @since 1.0.0
     */
    fun minusWeeks(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusWeeks(MAX_VALUE, leap).plusWeeks(1, leap)
        else plusWeeks(-amountToSubtract, leap)

    /**
     * Subtracts a specified number of days from the current instance.
     *
     * @param amountToSubtract the number of days to subtract, may be negative
     * @param leap specifies whether to consider leap year adjustments; defaults to false
     * @since 1.0.0
     */
    fun minusDays(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusDays(MAX_VALUE, leap).plusDays(1, leap)
        else plusDays(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of hours from the date-time.
     *
     * @param amountToSubtract the number of hours to subtract, may be negative.
     * @param leap a boolean flag indicating if leap year adjustments should be considered, defaults to false.
     * @since 1.0.0
     */
    fun minusHours(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusHours(MAX_VALUE, leap).plusHours(1, leap)
        else plusHours(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of minutes from this object.
     *
     * This method allows for subtracting a specific number of minutes, with an optional
     * configuration for leap years. If `amountToSubtract` equals `Long.MIN_VALUE`,
     * the method handles the underflow condition by using the maximum value plus an additional minute.
     *
     * @param amountToSubtract the number of minutes to subtract, can be positive or negative
     * @param leap determines if the calculation should consider a leap year context, defaults to `false`
     * @since 1.0.0
     */
    fun minusMinutes(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusMinutes(MAX_VALUE, leap).plusMinutes(1, leap)
        else plusMinutes(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of seconds from this instance.
     * If the given amount equals the minimum possible value, the method handles it
     * by adding the maximum value first and then adding one additional second.
     * The behavior may depend on whether the context involves a leap year or not.
     *
     * @param amountToSubtract the number of seconds to subtract, may be negative
     * @param leap indicates whether the operation should consider leap year rules; default is false
     * @since 1.0.0
     */
    fun minusSeconds(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusSeconds(MAX_VALUE, leap).plusSeconds(1, leap)
        else plusSeconds(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of nanoseconds from this instance.
     * If the amount to subtract is `Long.MIN_VALUE`, the method applies the maximum
     * amount of nanoseconds and an additional value of 1 nanosecond in the opposite
     * direction to handle overflow.
     *
     * @param amountToSubtract the number of nanoseconds to subtract, negative values will add nanoseconds
     * @param leap whether leap year adjustments are considered during the operation
     * @since 1.0.0
     */
    fun minusNanos(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusNanos(MAX_VALUE, leap).plusNanos(1, leap)
        else plusNanos(-amountToSubtract, leap)

    /**
     * Returns a new instance with the specified amount subtracted from this object.
     *
     * This method subtracts the given amount of time units from this instance.
     * If the amount to be subtracted equals `Long.MIN_VALUE`, the maximum value will be added first,
     * followed by an additional unit to handle overflow safely.
     *
     * @param amountToSubtract the amount of time units to subtract, may be negative
     * @param unit the unit of the time to subtract, not null
     * @param leap whether the calculation should account for leap adjustments
     * @return a new instance with the subtracted value applied
     * @since 1.0.0
     */
    fun minus(amountToSubtract: Long, unit: TemporalUnit, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plus(MAX_VALUE, unit, leap).plus(1, unit, leap)
        else plus(-amountToSubtract, unit, leap)

    /**
     * Converts this instance to a LocalDateTime for the specified year.
     *
     * @param year the year to be used in the resulting LocalDateTime
     * @return the resulting LocalDateTime with the specified year and the month, day, and time information derived from this instance
     * @since 1.0.0
     */
    fun toLocalDateTime(year: Int): LocalDateTime = LocalDateTime.of(
        year,
        monthDayTime.monthDay.month,
        monthDayTime.monthDay.dayOfMonth,
        monthDayTime.localTime.hour,
        monthDayTime.localTime.minute,
        monthDayTime.localTime.second,
        monthDayTime.localTime.nano
    )

    /**
     * Converts the current instance to a LocalDateTime using the specified year or the current year by default.
     *
     * @param year The year to use for the conversion. Defaults to the current year if not specified.
     * @return A LocalDateTime representation of this instance with the provided or default year.
     * @since 1.0.0
     */
    fun toLocalDateTime(year: Year = Year.now()): LocalDateTime = toLocalDateTime(year.value)

    /**
     * Converts the current instance to an `OffsetDateTime` using the provided `year`.
     *
     * @param year The year component to be used for constructing the `OffsetDateTime`.
     * @return An `OffsetDateTime` instance combining the provided year with the existing
     *         month, day, time, and offset components.
     * @since 1.0.0
     */
    fun toOffsetDateTime(year: Int): OffsetDateTime = OffsetDateTime.of(
        year,
        monthDayTime.monthDay.month.value,
        monthDayTime.monthDay.dayOfMonth,
        monthDayTime.localTime.hour,
        monthDayTime.localTime.minute,
        monthDayTime.localTime.second,
        monthDayTime.localTime.nano,
        offset
    )

    /**
     * Converts the current instance to an [OffsetDateTime] for the specified [Year].
     *
     * If no year is provided, the current year is used by default.
     *
     * @param year the year to be used in the resulting [OffsetDateTime]; defaults to the current year.
     * @return the resulting [OffsetDateTime] with the specified or current year.
     * @since 1.0.0
     */
    fun toOffsetDateTime(year: Year = Year.now()): OffsetDateTime = toOffsetDateTime(year.value)

    /**
     * Converts the current date and time fields to a ZonedDateTime for the specified year.
     *
     * @param year the year to be used in the ZonedDateTime.
     * @return a ZonedDateTime instance built from the current date and time fields with the specified year.
     * @since 1.0.0
     */
    fun toZonedDateTime(year: Int): ZonedDateTime = ZonedDateTime.of(
        year,
        monthDayTime.monthDay.month.value,
        monthDayTime.monthDay.dayOfMonth,
        monthDayTime.localTime.hour,
        monthDayTime.localTime.minute,
        monthDayTime.localTime.second,
        monthDayTime.localTime.nano,
        offset
    )

    /**
     * Converts this instance to a `ZonedDateTime` object using the provided year or the current year by default.
     *
     * @param year the year to use for creating the `ZonedDateTime`. Defaults to the current year if not provided.
     * @return a `ZonedDateTime` representing this instance at the specified or current year.
     * @since 1.0.0
     */
    fun toZonedDateTime(year: Year = Year.now()): ZonedDateTime = toZonedDateTime(year.value)

    /**
     * Converts the given year and the MonthDay stored in the `monthDayTime` property of this instance
     * into a LocalDate representation.
     *
     * @param year the year to be used in the resulting LocalDate.
     * @return a LocalDate object representing the combination of the given year and the MonthDay.
     * @since 1.0.0
     */
    fun toLocalDate(year: Int): LocalDate =
        LocalDate.of(year, monthDayTime.monthDay.month, monthDayTime.monthDay.dayOfMonth)

    /**
     * Converts the current object to a LocalDate representation using the specified year.
     *
     * @param year The year to use for the LocalDate conversion. Defaults to the current year.
     * @return The resulting LocalDate representation.
     * @since 1.0.0
     */
    fun toLocalDate(year: Year = Year.now()): LocalDate = toLocalDate(year.value)

    /**
     * Converts the given instance to a MonthDay representation.
     *
     * @return the MonthDay representation derived from the instance.
     * @since 1.0.0
     */
    fun toMonthDay(): MonthDay = monthDayTime.monthDay

    /**
     * Converts the current MonthDayTime instance to a LocalTime object.
     *
     * @return a LocalTime instance representing the time component of the MonthDayTime.
     * @since 1.0.0
     */
    fun toLocalTime(): LocalTime = monthDayTime.localTime

    /**
     * Converts the current instance of OffsetMonthDayTime to an OffsetTime instance
     * based on the local time and offset values of this object.
     *
     * @return an OffsetTime instance derived from the local time and offset of this object.
     * @since 1.0.0
     */
    fun toOffsetTime(): OffsetTime = OffsetTime.of(monthDayTime.localTime, offset)

    /**
     * Returns a string representation of this `OffsetMonthDayTime` instance by combining the
     * local month, day, and time components with the associated offset value.
     *
     * The string representation consists of the result of `toLocalMonthDayTime()`, followed
     * by the string representation of the `offset`.
     *
     * This method is useful for debugging or logging to easily identify the state of the
     * `OffsetMonthDayTime` instance.
     *
     * @return A string representation of this object combining local month-day-time and offset.
     * @since 1.0.0
     */
    override fun toString() = monthDayTime.toString() + offset.toString()

    /**
     * Compares this `OffsetMonthDayTime` instance with another `OffsetMonthDayTime` instance.
     * The comparison is based on the local date-time values of both instances after being
     * converted using the `MilitaryTimeZone.Z` time zone.
     *
     * @param other the other `OffsetMonthDayTime` instance to be compared with this instance.
     * @return a negative integer, zero, or a positive integer as this instance is less than,
     *         equal to, or greater than the specified `other` instance.
     * @since 1.0.0
     */
    override fun compareTo(other: OffsetMonthDayTime) =
        LocalMonthDayTime.from(TimeZoneDesignator.Z.convert(toLocalDateTime()))
            .compareTo(LocalMonthDayTime.from(TimeZoneDesignator.Z.convert(other.toLocalDateTime())))

    /**
     * Checks if the specified temporal unit is supported by this instance.
     *
     * The method will return true for supported units including nanos, micros, millis, seconds, minutes, hours,
     * half-days, days, weeks, and months. Any other unit will return false.
     *
     * @param unit the temporal unit to check, not null
     * @return true if the specified unit is supported, false otherwise
     * @since 1.0.0
     */
    override fun isSupported(unit: TemporalUnit) = when (unit) {
        ChronoUnit.NANOS, ChronoUnit.MICROS, ChronoUnit.MILLIS, ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS, ChronoUnit.HALF_DAYS, ChronoUnit.DAYS, ChronoUnit.WEEKS, ChronoUnit.MONTHS -> true
        else -> false
    }

    /**
     * Checks if the specified `TemporalField` is supported by this class.
     *
     * Supported fields include:
     * - DAY_OF_MONTH
     * - MONTH_OF_YEAR
     * - HOUR_OF_DAY
     * - MINUTE_OF_HOUR
     * - SECOND_OF_MINUTE
     * - NANO_OF_SECOND
     * - OFFSET_SECONDS
     *
     * @param field the `TemporalField` to check for support, may be null
     * @return true if the specified field is supported, false otherwise
     * @since 1.0.0
     */
    override fun isSupported(field: TemporalField?) = when (field) {
        ChronoField.DAY_OF_MONTH, ChronoField.MONTH_OF_YEAR, ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_DAY, ChronoField.NANO_OF_SECOND, ChronoField.OFFSET_SECONDS -> true
        else -> false
    }

    /**
     * Returns a new instance of `OffsetMonthDayTime` with the specified `MonthDay` while retaining the local time and offset of the current instance.
     *
     * @param monthDay the `MonthDay` to use for the new instance, not null
     * @since 1.0.0
     */
    fun withMontDay(monthDay: MonthDay) = OffsetMonthDayTime(LocalMonthDayTime(monthDay, monthDayTime.localTime), offset)

    /**
     * Returns a copy of this `OffsetMonthDayTime` instance with the month-of-year altered to the specified value.
     * The day-of-month and other properties remain unchanged. If the new month does not support the current
     * day-of-month (e.g., changing from March 31 to February), an exception will be thrown.
     *
     * @param month The value of the month-of-year to set, from 1 (January) to 12 (December).
     * @return A new `OffsetMonthDayTime` instance with the modified month value.
     * @throws DateTimeException if the resulting date is invalid, such as February 30.
     * @since 1.0.0
     */
    fun withMonth(month: Int): OffsetMonthDayTime {
        month in 1..12 || throw DateTimeException("Invalid value for MonthOfYear: $month")
        return OffsetMonthDayTime(
            LocalMonthDayTime(MonthDay.of(month, monthDayTime.monthDay.dayOfMonth), monthDayTime.localTime),
            offset
        )
    }

    /**
     * Returns a copy of this OffsetMonthDayTime with the specified month of the year.
     * This adjusts the month while retaining the other fields in the same state.
     * If the specified month is out of the valid range (1-12), an exception will be thrown.
     *
     * @param month the Month instance to set the month field to, not null
     * @since 1.0.0
     */
    fun withMonth(month: Month) = withMonth(month.value)

    /**
     * Returns a copy of this `OffsetMonthDayTime` with the specified day of the month.
     *
     * The day of the month is validated to ensure it is within the range of valid days
     * for the corresponding month. If the value is invalid, an exception is thrown.
     *
     * @param day the day of the month to set, from 1 to the maximum length of the month
     * @return a new `OffsetMonthDayTime` instance with the specified day of the month
     * @throws DateTimeException if the `day` is invalid for the current month
     * @since 1.0.0
     */
    fun withDay(day: Int): OffsetMonthDayTime {
        day < 1 || day > monthDayTime.monthDay.month.maxLength() || throw DateTimeException("Invalid value for DayOfMonth: $day")
        return OffsetMonthDayTime(
            LocalMonthDayTime(
                MonthDay.of(monthDayTime.monthDay.month, day),
                monthDayTime.localTime
            ), offset
        )
    }

    /**
     * Returns a new instance of `OffsetMonthDayTime` with the same `MonthDay` and `ZoneOffset`,
     * but with the time part replaced by the specified `LocalTime`.
     *
     * @param localTime the new time value to be associated with this instance, not null
     * @since 1.0.0
     */
    fun withTime(localTime: LocalTime) = OffsetMonthDayTime(LocalMonthDayTime(monthDayTime.monthDay, localTime), offset)

    /**
     * Returns a copy of this `OffsetMonthDayTime` with the hour-of-day altered.
     *
     * The hour value must be within the valid range from 0 to 23. If the provided `hour`
     * is outside this range, a `DateTimeException` will be thrown.
     *
     * @param hour The hour-of-day to set in the new instance, must be from 0 to 23.
     * @return A new `OffsetMonthDayTime` instance with the modified hour-of-day.
     * @throws DateTimeException If the provided `hour` is invalid.
     * @since 1.0.0
     */
    fun withHour(hour: Int): OffsetMonthDayTime {
        hour in 0..23 || throw DateTimeException("Invalid value for HourOfDay: $hour")
        return OffsetMonthDayTime(
            LocalMonthDayTime(
                MonthDay.from(toLocalDate()),
                LocalTime.of(
                    hour,
                    monthDayTime.localTime.minute,
                    monthDayTime.localTime.second,
                    monthDayTime.localTime.nano
                )
            ), offset
        )
    }

    /**
     * Returns a new instance of OffsetMonthDayTime with the minute value altered.
     *
     * The minute must be a valid value between 0 and 59. If the provided value is
     * outside this range, a DateTimeException is thrown.
     *
     * @param minute the minute to set in the new instance, must be between 0 and 59
     * @return a new OffsetMonthDayTime instance with the specified minute value
     * @throws DateTimeException if the minute value is invalid
     * @since 1.0.0
     */
    fun withMinute(minute: Int): OffsetMonthDayTime {
        minute in 0..59 || throw DateTimeException("Invalid value for MinuteOfHour: $minute")
        return OffsetMonthDayTime(
            LocalMonthDayTime(
                MonthDay.from(toLocalDate()),
                LocalTime.of(
                    monthDayTime.localTime.hour,
                    minute,
                    monthDayTime.localTime.second,
                    monthDayTime.localTime.nano
                )
            ), offset
        )
    }

    /**
     * Returns a copy of this `OffsetMonthDayTime` with the second-of-minute updated.
     *
     * @param second the second-of-minute to set, must be from 0 to 59
     * @return a new instance of `OffsetMonthDayTime` with the second-of-minute updated
     * @throws DateTimeException if the second is invalid
     * @since 1.0.0
     */
    fun withSecond(second: Int): OffsetMonthDayTime {
        second in 0..59 || throw DateTimeException("Invalid value for SecondOfMinute: $second")
        return OffsetMonthDayTime(
            LocalMonthDayTime(
                MonthDay.from(toLocalDate()),
                LocalTime.of(
                    monthDayTime.localTime.hour,
                    monthDayTime.localTime.minute,
                    second,
                    monthDayTime.localTime.nano
                )
            ), offset
        )
    }

    /**
     * Returns a new `OffsetMonthDayTime` instance with the specified nano-of-second value.
     * This method allows modification of the nanosecond component of the time while retaining
     * all other fields in the current `OffsetMonthDayTime` instance.
     *
     * @param nanoOfSecond The new nanosecond value to set. It must be within the range 0 to 999,999,999.
     *                     If the value is out of range, a `DateTimeException` will be thrown.
     * @return A new `OffsetMonthDayTime` with the updated nanosecond value.
     * @since 1.0.0
     */
    fun withNano(nanoOfSecond: Int): OffsetMonthDayTime {
        nanoOfSecond in 0..999999999 || throw DateTimeException("Invalid value for NanoOfSecond: $nanoOfSecond")
        return OffsetMonthDayTime(
            LocalMonthDayTime(
                MonthDay.from(toLocalDate()),
                LocalTime.of(
                    monthDayTime.localTime.hour,
                    monthDayTime.localTime.minute,
                    monthDayTime.localTime.second,
                    nanoOfSecond
                )
            ), offset
        )
    }

    /**
     * Returns a new instance of `OffsetMonthDayTime` with the specified `ZoneOffset`.
     *
     * @param offset the `ZoneOffset` to associate with this `OffsetMonthDayTime`, not null
     * @since 1.0.0
     */
    fun withOffset(offset: ZoneOffset) = OffsetMonthDayTime(LocalMonthDayTime(monthDayTime.monthDay, monthDayTime.localTime), offset)
    /**
     * Returns a new instance of `OffsetMonthDayTime` with the specified offset from the given `ZoneIdent`.
     *
     * @param zoneIdent the `ZoneIdent` containing the offset to apply
     * @since 1.0.0
     */
    fun withOffset(zoneIdent: ZoneIdent) = withOffset(zoneIdent.offset)

    /**
     * Adjusts the specified temporal field to a new value in this temporal object.
     * This method overrides the `with` method to support specific TemporalField modifications.
     *
     * @param field the temporal field to be adjusted, must not be null
     * @param newValue the new value to set the field to, must be a valid value for the field
     * @return a new Temporal object with the specified field adjusted to the new value
     * @throws UnsupportedTemporalTypeException if the field is unsupported or not adjustable in this context
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
            ChronoField.NANO_OF_DAY -> OffsetMonthDayTime(LocalMonthDayTime(
                MonthDay.from(toLocalDate()),
                LocalTime.ofNanoOfDay(newValue)
            ), offset)
            ChronoField.NANO_OF_SECOND -> withNano(newValue.toInt())
            ChronoField.OFFSET_SECONDS -> withOffset(ZoneOffset.ofTotalSeconds(newValue.toInt()))
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }

    /**
     * Calculates the amount of time until the specified end temporal in terms of the specified unit.
     *
     * @param endExclusive the end temporal that is exclusive, not null
     * @param unit the unit to measure the amount of time in, not null
     * @since 1.0.0
     */
    override fun until(endExclusive: Temporal, unit: TemporalUnit) =
        TimeZoneDesignator.Z.convert(toLocalDateTime()).until(
            TimeZoneDesignator.Z.convert(LocalMonthDayTime.from(endExclusive).toLocalDateTime()),
            unit
        )

    /**
     * Retrieves the value of the specified temporal field as a `Long`.
     * The temporal fields supported by this method include day of month, month of year,
     * hour of day, minute of hour, second of minute, nano of second, and offset seconds.
     *
     * @param field the temporal field to retrieve the value for; must be castable to `ChronoField`
     * @return the value of the specified field as a `Long`
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @since 1.0.0
     */
    override fun getLong(field: TemporalField) = when (field as ChronoField) {
        ChronoField.DAY_OF_MONTH -> monthDayTime.monthDay.dayOfMonth
        ChronoField.MONTH_OF_YEAR -> monthDayTime.monthDay.monthValue
        ChronoField.HOUR_OF_DAY -> monthDayTime.localTime.hour
        ChronoField.MINUTE_OF_HOUR -> monthDayTime.localTime.minute
        ChronoField.SECOND_OF_MINUTE -> monthDayTime.localTime.second
        ChronoField.NANO_OF_DAY -> monthDayTime.localTime.toNanoOfDay()
        ChronoField.NANO_OF_SECOND -> monthDayTime.localTime.nano
        ChronoField.OFFSET_SECONDS -> offset.totalSeconds.toLong()
        else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
    }.toLong()

    /**
     * Adjusts the specified `Temporal` object by applying the values of this instance.
     * The adjustment ensures the temporal object is updated with the corresponding
     * date, time, and offset properties encapsulated in `monthDayTime` and `offset`.
     *
     * @param temporal the temporal object to be adjusted, not null
     * @return the adjusted temporal object with updated fields from this instance
     * @since 1.0.0
     */
    override fun adjustInto(temporal: Temporal): Temporal = temporal
        .with(ChronoField.DAY_OF_MONTH, monthDayTime.monthDay.dayOfMonth.toLong())
        .with(ChronoField.MONTH_OF_YEAR, monthDayTime.monthDay.monthValue.toLong())
        .with(ChronoField.HOUR_OF_DAY, monthDayTime.localTime.hour.toLong())
        .with(ChronoField.MINUTE_OF_HOUR, monthDayTime.localTime.minute.toLong())
        .with(ChronoField.SECOND_OF_MINUTE, monthDayTime.localTime.second.toLong())
        .with(ChronoField.NANO_OF_DAY, monthDayTime.localTime.toNanoOfDay())
        .with(ChronoField.NANO_OF_SECOND, monthDayTime.localTime.nano.toLong())
        .with(ChronoField.OFFSET_SECONDS, offset.totalSeconds.toLong())

    /**
     * Adjusts the current instance to a specific ZoneOffset while preserving the same instant in time.
     * Converts the instance to a local month-day-time representation, aligns it to the system's default time zone,
     * and then adjusts it to the specified ZoneOffset, returning the result as an offset-based month-day-time.
     *
     * @param offset the ZoneOffset to which the time should be adjusted while keeping the same instant.
     * @return the adjusted offset-based month-day-time.
     * @since 1.0.0
     */
    fun atOffsetSameInstant(offset: ZoneOffset) =
        toLocalMonthDayTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(offset).toOffsetMonthDayTime()

    /**
     * Returns an `OffsetMonthDayTime` with the same local month-day-time and the specified time-zone offset.
     * This method adjusts the current instance with the same local time and month-day to match the provided
     * `ZoneIdent`, keeping the same instant in time.
     *
     * @param zoneIdent The `ZoneIdent` representing the time zone offset to be applied, must not be null.
     * @return An `OffsetMonthDayTime` instance adjusted with the specified time-zone offset but retaining
     *         the same instant in time.
     * @since 1.0.0
     */
    fun atOffsetSameInstant(zoneIdent: ZoneIdent): OffsetMonthDayTime {
        val localMonthDayTime = toLocalMonthDayTime()
        return OffsetMonthDayTime(localMonthDayTime.monthDay, localMonthDayTime.localTime, offset)
    }

    /**
     * Converts the current month-day-time object to a `ZonedDateTime` at the same instant in time
     * for the specified time zone.
     *
     * @param zone the time zone to interpret the current time at the same instant
     * @return a `ZonedDateTime` representing the same instant in the given time zone
     * @since 1.0.0
     */
    fun atZoneSameInstant(zone: ZoneId): ZonedMonthDayTime {
        val localMonthDayTime = toLocalMonthDayTime()
        return ZonedMonthDayTime(localMonthDayTime.monthDay, localMonthDayTime.localTime, zone)
    }

    /**
     * Converts the current instance to a ZonedDateTime at the same instant in the specified time zone.
     *
     * @param zoneIdent the time zone identifier to apply to the current instance
     * @return the ZonedDateTime representation at the same instant in the specified time zone
     * @since 1.0.0
     */
    fun atZoneSameInstant(zoneIdent: ZoneIdent): ZonedMonthDayTime {
        val localMonthDayTime = toLocalMonthDayTime()
        return ZonedMonthDayTime(localMonthDayTime.monthDay, localMonthDayTime.localTime, zoneIdent)
    }

    /**
     * Converts the properties of an object into a map representation.
     *
     * @return A map containing the extracted values from MonthDay and LocalTime.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "monthDay" to monthDayTime.monthDay,
        "localTime" to monthDayTime.localTime,
        "month" to monthDayTime.monthDay.month!!,
        "day" to monthDayTime.monthDay.dayOfMonth,
        "hour" to monthDayTime.localTime.hour,
        "minute" to monthDayTime.localTime.minute,
        "second" to monthDayTime.localTime.second,
        "milli" to monthDayTime.localTime.nano / 1000000,
        "nano" to monthDayTime.localTime.nano,
        "offset" to offset
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
     * - `offset`: The offset - TYPE: [ZoneOffset].
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
     * Deconstructs the `monthDay` property to extract and return its `month` value.
     *
     * This function allows the `component1` operator to be used for destructuring declarations,
     * specifically to retrieve the `month` property from the `monthDay` object.
     *
     * @return The `month` component of the `monthDay`.
     *
     * @since 1.0.0
     */
    operator fun component1() = monthDayTime.monthDay.month!!
    /**
     * Provides the day of the month as part of the decomposition of a date or time object.
     * This function acts as a component function, often used in destructuring declarations
     * to directly access the `dayOfMonth` field of the `monthDay` property.
     *
     * @return The day of the month, extracted from the `monthDay` property.
     * @since 1.0.0
     */
    operator fun component2() = monthDayTime.monthDay.dayOfMonth
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
    operator fun component3() = monthDayTime.localTime.hour
    /**
     * Provides the minute component of the `localTime` value.
     *
     * This function is a component function commonly used in destructuring declarations.
     * It returns the `minute` property from the `localTime`.
     *
     * @return the minute component of the `localTime`.
     * @since 1.0.0
     */
    operator fun component4() = monthDayTime.localTime.minute
    /**
     * Component function that extracts the `second` value from the `localTime` property.
     * This function is a part of the destructuring declaration mechanism.
     *
     * @return The second component of the `localTime`, as an integer.
     * @since 1.0.0
     */
    operator fun component5() = monthDayTime.localTime.second
    /**
     * Provides the nanosecond component of the `localTime` property.
     * This operator function is typically used for destructuring declarations
     * to extract the sixth component from an object.
     *
     * @return The nanoseconds part of the `localTime` property.
     * @since 1.0.0
     */
    operator fun component6() = monthDayTime.localTime.nano
    /**
     * Returns the seventh component of the instance, represented by its offset value.
     * This function is a part of the componentN operator conventions and is typically used in destructuring declarations.
     *
     * @return The offset value as the seventh component.
     * @since 1.0.0
     */
    operator fun component7() = offset
}