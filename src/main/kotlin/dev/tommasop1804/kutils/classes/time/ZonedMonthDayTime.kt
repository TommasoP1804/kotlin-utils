package dev.tommasop1804.kutils.classes.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.LocalMonthDayTime
import dev.tommasop1804.kutils.classes.time.LocalMonthDayTime.Companion.toLocalMonthDayTime
import dev.tommasop1804.kutils.get
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
 * `ZonedMonthDayTime` is a class that represents a date and time defined by a month-day combination,
 * along with a time zone and an offset from UTC. This class is designed to provide operations for temporal
 * calculations and comparisons, considering leap year adjustments where applicable.
 *
 * @property monthDayTime the date and time represented as a month-day instance
 * @property timeZone the time zone in which the date and time is situated
 * @property offset the offset from UTC used for the temporal instance
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = ZonedMonthDayTime.Companion.Serializer::class)
@JsonDeserialize(using = ZonedMonthDayTime.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = ZonedMonthDayTime.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = ZonedMonthDayTime.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_getorthrow_as_invoke", "kutils_temporal_now_as_temporal", "kutils_temporal_of_as_temporal", "kutils_temporal_parse_as_temporal", "kutils_take_as_int_invoke")
class ZonedMonthDayTime private constructor(val monthDayTime: LocalMonthDayTime, val timeZone: ZoneId) : Temporal, TemporalAccessor, TemporalAdjuster, Comparable<ZonedMonthDayTime>, Serializable {
    val offset: ZoneOffset = timeZone.rules.getOffset(monthDayTime.toLocalDateTime())

    /**
     * Creates an instance of `ZonedMonthDayTime` from the specified `MonthDay`, `LocalTime`, and `ZoneOffset`.
     *
     * @param monthDay the month-day part of the combined date-time, not null
     * @param localTime the local-time part of the combined date-time, not null
     * @param timeZone the time-zone, not null
     * @return a new `ZonedMonthDayTime` instance representing the specified `MonthDay`, `LocalTime`, and `ZoneOffset`
     * @since 1.0.0
     */
    constructor(monthDay: MonthDay, localTime: LocalTime, timeZone: ZoneId) : this(LocalMonthDayTime(monthDay, localTime), timeZone)

    /**
     * Creates a `ZonedMonthDayTime` instance from the provided `MonthDay`, `LocalTime`, and `ZoneIdent`.
     *
     * @param monthDay the month and day combination, not null
     * @param localTime the local time representation, not null
     * @param zoneIdent the time zone identifier, not null
     * @return a `ZonedMonthDayTime` object representing the combination of the provided inputs
     * @since 1.0.0
     */
    constructor(monthDay: MonthDay, localTime: LocalTime, zoneIdent: ZoneIdent) : this(LocalMonthDayTime(monthDay, localTime), zoneIdent.offset)

    /**
     * Creates an instance of `ZonedMonthDayTime` from the specified date, time, and time zone.
     *
     * @param month the month of the specified date, from 1 (January) to 12 (December)
     * @param day the day of the month, from 1 to 31 depending on the month
     * @param hour the hour of the day, from 0 to 23 (default is 0)
     * @param minute the minute of the hour, from 0 to 59 (default is 0)
     * @param second the second of the minute, from 0 to 59 (default is 0)
     * @param nanoOfSecond the nanosecond of the second, from 0 to 999,999,999 (default is 0)
     * @param timeZone the time zone information, not null
     * @return a new `ZonedMonthDayTime` instance with the given parameters
     * @since 1.0.0
     */
    constructor(
        month: Int,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        nanoOfSecond: Int = 0,
        timeZone: ZoneId
    ) : this(
        LocalMonthDayTime(
            MonthDay.of(month, day),
            LocalTime.of(hour, minute, second, nanoOfSecond)
        ), timeZone
    )

    /**
     * Creates an instance of `ZonedMonthDayTime` with the specified month, day, time components, and zone identifier.
     *
     * @param month the month of the year, from 1 (January) to 12 (December)
     * @param day the day of the month, from 1 to 31, depending on the month
     * @param hour the hour of the time component, from 0 to 23, default is 0
     * @param minute the minute of the time component, from 0 to 59, default is 0
     * @param second the second of the time component, from 0 to 59, default is 0
     * @param nanoOfSecond the nanoseconds within the second, from 0 to 999,999,999, default is 0
     * @param zoneIdent the zone identifier specifying the time zone
     * @return a new instance of `ZonedMonthDayTime` representing the specified month, day, time, and time zone
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
    ) : this(
        LocalMonthDayTime(
            MonthDay.of(month, day),
            LocalTime.of(hour, minute, second, nanoOfSecond)
        ),
        zoneIdent.zoneId
    )

    /**
     * Creates an instance of `ZonedMonthDayTime` from the specified month, day, time, and time zone.
     *
     * @param month the month field, not null
     * @param day the day of the month, from 1 to 31
     * @param hour the hour-of-day, from 0 to 23, default is 0
     * @param minute the minute-of-hour, from 0 to 59, default is 0
     * @param second the second-of-minute, from 0 to 59, default is 0
     * @param nanoOfSecond the nanosecond-of-second, from 0 to 999,999,999, default is 0
     * @param timeZone the time zone identifier, not null
     * @return a new `ZonedMonthDayTime` instance representing the specified values
     * @since 1.0.0
     */
    constructor(
        month: Month,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        nanoOfSecond: Int = 0,
        timeZone: ZoneId
    ) : this(
        LocalMonthDayTime(
            MonthDay.of(month, day),
            LocalTime.of(hour, minute, second, nanoOfSecond)
        ), timeZone
    )

    /**
     * Creates a new instance of `ZonedMonthDayTime` from the supplied values.
     *
     * @param month The month of the year, not null.
     * @param day The day of the month, from 1 to the maximum number of days in the specified month.
     * @param hour The hour of the day, from 0 to 23. Default is 0.
     * @param minute The minute of the hour, from 0 to 59. Default is 0.
     * @param second The second of the minute, from 0 to 59. Default is 0.
     * @param nanoOfSecond The nanosecond of the second, from 0 to 999,999,999. Default is 0.
     * @param zoneIdent The zone identifier indicating the time zone, not null.
     * @return A `ZonedMonthDayTime` instance representing the combined month, day, time, and zone information.
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
    ) : this(
        LocalMonthDayTime(
            MonthDay.of(month, day),
            LocalTime.of(hour, minute, second, nanoOfSecond)
        ),
        zoneIdent.zoneId
    )

    /**
     * Creates an instance of `ZonedMonthDayTime` from the specified `LocalDate`, `LocalTime`, and `ZoneId`.
     *
     * @param localDate the local date part of the combined date-time, not null
     * @param localTime the local time part of the combined date-time, not null
     * @param timeZone the time zone part of the combined date-time, not null
     * @return a new `ZonedMonthDayTime` instance representing the specified `LocalDate`, `LocalTime`, and `ZoneId`
     * @since 1.0.0
     */
    constructor(localDate: LocalDate, localTime: LocalTime, timeZone: ZoneId) : this(LocalMonthDayTime(MonthDay.from(localDate), localTime), timeZone)

    /**
     * Creates a new instance of `ZonedMonthDayTime` based on the provided local date, local time,
     * and zone identifier.
     *
     * @param localDate The local date component of the desired `ZonedMonthDayTime`, used to derive the month and day.
     * @param localTime The local time component of the desired `ZonedMonthDayTime`.
     * @param zoneIdent The time zone identifier, providing the associated time zone for this instance.
     * @return A new `ZonedMonthDayTime` with the specified date, time, and zone information.
     * @since 1.0.0
     */
    constructor(localDate: LocalDate, localTime: LocalTime, zoneIdent: ZoneIdent) : this(LocalMonthDayTime(MonthDay.from(localDate), localTime), zoneIdent.zoneId)

    /**
     * Creates a ZonedMonthDayTime instance from the specified instant and time zone.
     *
     * This function combines an `Instant` and a `ZoneId` to create a ZonedMonthDayTime representation
     * by converting the instant to a corresponding local date-time in the specified time zone.
     *
     * @param instant the instant to convert, not null
     * @param zoneId the time zone to use for the conversion, not null
     * @return a ZonedMonthDayTime representation of the given instant in the specified time zone
     * @since 1.0.0
     */
    constructor(instant: Instant, zoneId: ZoneId) : this(from(LocalDateTime.ofInstant(instant, zoneId)))
    /**
     * Constructs an instance using the specified `Instant` and `ZoneIdent`.
     *
     * @param instant The `Instant` representing an exact point in time.
     * @param zoneId The `ZoneIdent` used to determine the time-zone for the instant.
     * @since 1.0.0
     */
    constructor(instant: Instant, zoneId: ZoneIdent) : this(from(LocalDateTime.ofInstant(instant, zoneId.zoneId)))
    /**
     * Constructs an instance of the class using a specified instant and time zone.
     *
     * Uses the provided `Instant` and `ZoneId` to create a `LocalDateTime` that
     * represents the same moment in time within the provided time zone.
     * This constructor utilizes Kotlin's experimental time API.
     *
     * @param instant The point in time to be converted.
     * @param zoneId The time zone used to interpret the instant into a local date-time.
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(instant: kotlin.time.Instant, zoneId: ZoneId) : this(from(LocalDateTime.ofInstant(instant.toJavaInstant(), zoneId)))
    /**
     * Constructs an instance using the given [instant] and [zoneId].
     *
     * This constructor converts the provided [instant] into a [LocalDateTime] using the specified [zoneId].
     *
     * @param instant The instant representing a specific point in time.
     * @param zoneId The time-zone identifier used for the conversion.
     *
     * @throws DateTimeException if the instant cannot be converted to a date-time.
     *
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(instant: kotlin.time.Instant, zoneId: ZoneIdent) : this(from(LocalDateTime.ofInstant(instant.toJavaInstant(), zoneId.zoneId)))

    /**
     * Constructs an instance of the class using the current time as the default value.
     *
     * Delegates to the primary constructor by passing the result of `now()` as an argument.
     *
     * @since 1.0.0
     */
    constructor() : this(now())
    /**
     * Secondary constructor that initializes an instance using the current time from the provided clock.
     *
     * @param clock A `Clock` instance used to retrieve the current time.
     * @since 1.0.0
     */
    constructor(clock: Clock) : this(now(clock))
    /**
     * Constructs a new instance initialized with the current date-time
     * from the specified time zone.
     *
     * @param zoneId the time zone identifier to use for obtaining the current date-time
     * @since 1.0.0
     */
    constructor(zoneId: ZoneId) : this(now(zoneId))
    /**
     * Constructs an instance with the current date-time in the specified time-zone.
     *
     * @param zoneId the time-zone to use, not null
     * @throws DateTimeException if unable to obtain the current date-time
     * @since 1.0.0
     */
    constructor(zoneId: ZoneIdent) : this(now(zoneId))

    /**
     * Private constructor for creating an instance using a ZonedMonthDayTime object.
     * Initializes the current class with the provided `monthDayTime` and `timeZone` from the ZonedMonthDayTime object.
     *
     * @param zmdt An instance of ZonedMonthDayTime from which the `monthDayTime` and `timeZone` will be extracted.
     * @since 1.0.0
     */
    private constructor(zmdt: ZonedMonthDayTime) : this(zmdt.monthDayTime, zmdt.timeZone)
    
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
         * Represents the minimum possible value for a `ZonedMonthDayTime` instance.
         *
         * This value is defined with the earliest possible `MonthDay` (January 1st) combined
         * with the minimum local time (`LocalTime.MIN`) and the offset of a military time zone
         * (`MilitaryTimeZone.Z.offset`).
         *
         * The `MIN` constant is particularly useful for comparison, validation, or as a
         * baseline in temporal operations where an initial minimum bound is required.
         *
         * @since 1.0.0
         */
        val MIN: ZonedMonthDayTime =
            ZonedMonthDayTime(LocalMonthDayTime(MonthDay.of(1, 1), LocalTime.MIN), TimeZoneDesignator.Z.offset)

        /**
         * Represents the maximum possible value for a `ZonedMonthDayTime` instance.
         *
         * This constant defines a `ZonedMonthDayTime` set to the last day of the year (December 31)
         * at the maximum possible `LocalTime` (23:59:59.999999999) within the MilitaryTimeZone offset (Z).
         *
         * It can be used to represent upper bounds in temporal calculations or limit scenarios involving
         * combinations of month, day, and time with a specific time zone.
         *
         * @since 1.0.0
         */
        val MAX: ZonedMonthDayTime =
            ZonedMonthDayTime(LocalMonthDayTime(MonthDay.of(12, 31), LocalTime.MAX), TimeZoneDesignator.Z.offset)

        /**
         * Represents the default leap year used within the `ZonedMonthDayTime` class.
         * This value is primarily used for handling operations or calculations related
         * to leap years when no specific leap year is provided.
         *
         * @since 1.0.0
         */
        private const val DEFAULT_LEAP_YEAR: Int = 2024

        /**
         * Represents the default non-leap year value used in calculations and date/time operations.
         *
         * This constant is typically used when a fixed non-leap year is required for operations or as
         * a baseline for time-related calculations.
         *
         * @since 1.0.0
         */
        private const val DEFAULT_NON_LEAP_YEAR: Int = 2025

        /**
         * Represents the constant value for the number of hours in a standard day.
         * This value is universally fixed to 24, based on the conventional division
         * of a day into equal parts for calendrical and time management purposes.
         *
         * @since 1.0.0
         */
        private const val HOURS_PER_DAY: Int = 24

        /**
         * Constant representing the number of minutes in one hour.
         *
         * This value is used to facilitate conversions or calculations involving
         * time when the concept of minutes in an hour is required.
         *
         * @since 1.0.0
         */
        private const val MINUTES_PER_HOUR: Int = 60

        /**
         * Represents the number of minutes in a single day.
         * This constant is calculated as the product of `MINUTES_PER_HOUR`
         * and `HOURS_PER_DAY`.
         *
         * @since 1.0.0
         */
        private const val MINUTES_PER_DAY: Int = MINUTES_PER_HOUR * HOURS_PER_DAY

        /**
         * Represents the number of seconds in a single minute.
         * Used as a constant value for time-related calculations within the `ZonedMonthDayTime` class.
         *
         * @since 1.0.0
         */
        private const val SECONDS_PER_MINUTE: Int = 60

        /**
         * Represents the number of seconds in one hour.
         * This constant is derived by multiplying the number of seconds in a minute
         * with the number of minutes in an hour.
         *
         * This value is used internally for time-related calculations involving
         * hour-based conversions in time operations.
         *
         * @since 1.0.0
         */
        private const val SECONDS_PER_HOUR: Int = SECONDS_PER_MINUTE * MINUTES_PER_HOUR

        /**
         * Represents the total number of seconds in a standard day.
         *
         * This constant is calculated by multiplying the number of seconds in an hour
         * by the number of hours in a day.
         *
         * @since 1.0.0
         */
        private const val SECONDS_PER_DAY: Int = SECONDS_PER_HOUR * HOURS_PER_DAY

        /**
         * Constant representing the number of milliseconds in one second.
         *
         * This value is commonly used in time calculations where millisecond-level precision
         * is required, such as converting between seconds and milliseconds or delay intervals.
         *
         * @since 1.0.0
         */
        private const val MILLIS_PER_SECOND: Long = 1000L

        /**
         * A constant representing the number of milliseconds in a day.
         * Calculated using the product of `MILLIS_PER_SECOND` and `SECONDS_PER_DAY`.
         *
         * This value is used for operations that involve time calculations,
         * where the duration or interval of a single day in milliseconds is required.
         *
         * @since 1.0.0
         */
        private const val MILLIS_PER_DAY: Long = MILLIS_PER_SECOND * SECONDS_PER_DAY

        /**
         * Represents the number of microseconds in one second.
         *
         * This constant is useful for time-based calculations where conversions
         * between seconds and microseconds are required.
         *
         * @since 1.0.0
         */
        private const val MICROS_PER_SECOND: Long = 1000000L

        /**
         * Represents the total number of microseconds in a day.
         *
         * This constant is calculated as the product of `MICROS_PER_SECOND` and `SECONDS_PER_DAY`
         * to provide a standardized representation for one day's duration in microseconds.
         *
         * @since 1.0.0
         */
        private const val MICROS_PER_DAY: Long = MICROS_PER_SECOND * SECONDS_PER_DAY

        /**
         * Represents the number of nanoseconds in one second.
         *
         * This constant is used to perform time-related calculations,
         * particularly when converting or comparing time durations
         * at the nanosecond scale.
         *
         * @since 1.0.0
         */
        private const val NANOS_PER_SECOND: Long = 1000000000L

        /**
         * The number of nanoseconds in a minute.
         * This constant is derived by multiplying the number of nanoseconds in a second (`NANOS_PER_SECOND`)
         * by the number of seconds in a minute (`SECONDS_PER_MINUTE`).
         *
         * @since 1.0.0
         */
        private const val NANOS_PER_MINUTE: Long = NANOS_PER_SECOND * SECONDS_PER_MINUTE

        /**
         * Represents the number of nanoseconds in an hour.
         *
         * This constant is derived by multiplying `NANOS_PER_MINUTE`
         * by `MINUTES_PER_HOUR`.
         * It is used in calculations involving time with nanosecond precision.
         *
         * @since 1.0.0
         */
        private const val NANOS_PER_HOUR: Long = NANOS_PER_MINUTE * MINUTES_PER_HOUR

        /**
         * Represents the number of nanoseconds in a day.
         *
         * Calculated as the product of `NANOS_PER_HOUR` (number of nanoseconds in an hour)
         * and `HOURS_PER_DAY` (number of hours in a day).
         *
         * This constant is used in the `ZonedMonthDayTime` class for performing time-based
         * calculations and transformations that depend on precise nanosecond-based time
         * intervals within a day.
         *
         * @since 1.0.0
         */
        private const val NANOS_PER_DAY: Long = NANOS_PER_HOUR * HOURS_PER_DAY

        /**
         * Creates an instance of `ZonedMonthDayTime` from a `TemporalAccessor`.
         *
         * The method extracts the `MonthDay`, `LocalTime`, and `ZoneOffset` components from the provided
         * `TemporalAccessor` to construct a new `ZonedMonthDayTime` instance.
         *
         * @param temporalAccessor the temporal object to convert, not null
         * @return a new `ZonedMonthDayTime` instance representing the extracted components
         *         from the provided `TemporalAccessor`
         * @since 1.0.0
         */
        @JvmStatic
        fun from(temporalAccessor: TemporalAccessor) =
            ZonedMonthDayTime(
                LocalMonthDayTime(MonthDay.from(temporalAccessor), LocalTime.from(temporalAccessor)),
                ZoneOffset.from(temporalAccessor)
            )

        /**
         * Creates an instance of `ZonedMonthDayTime` from the provided `Temporal` object.
         * The given `Temporal` must support the extraction of `MonthDay`, `LocalTime`, and `ZoneOffset`.
         *
         * @param temporal the temporal object to convert, must not be null and must support the required fields
         * @return a new `ZonedMonthDayTime` instance based on the provided temporal object
         * @since 1.0.0
         */
        @JvmStatic
        fun from(temporal: Temporal) =
            ZonedMonthDayTime(LocalMonthDayTime(MonthDay.from(temporal), LocalTime.from(temporal)), ZoneOffset.from(temporal))

        /**
         * Creates a new instance of `ZonedMonthDayTime` representing the current date and time
         * based on the specified clock.
         *
         * The returned instance will use the `MonthDay` and `LocalTime` derived from the provided
         * `Clock` and the time-zone offset associated with the clock.
         *
         * @param clock the clock from which the current date, time, and time-zone offset are to be derived, not null
         * @return a new instance of `ZonedMonthDayTime` representing the current date, time, and time-zone offset
         * @since 1.0.0
         */
        @JvmStatic
        fun now(clock: Clock) = ZonedMonthDayTime(
            LocalMonthDayTime(MonthDay.now(clock), LocalTime.now(clock)),
            clock.zone.rules.getOffset(clock.instant())
        )

        /**
         * Creates a new `ZonedMonthDayTime` instance with the current date and time for the specified time zone.
         *
         * @param timeZone the time zone to associate with the current date and time, must not be null
         * @return a `ZonedMonthDayTime` instance representing the current date and time in the specified time zone
         * @since 1.0.0
         */
        @JvmStatic
        fun now(timeZone: ZoneId) = ZonedMonthDayTime(
            LocalMonthDayTime(
                MonthDay.now(),
                LocalTime.now()
            ), timeZone)

        /**
         * Creates a new `ZonedMonthDayTime` instance representing the current date (month and day) and time,
         * using the specified time zone.
         *
         * @param zoneIdent the `ZoneIdent` defining the time zone in which the current date and time should be captured
         * @return a `ZonedMonthDayTime` instance representing the current date and time in the specified time zone
         * @since 1.0.0
         */
        @JvmStatic
        fun now(zoneIdent: ZoneIdent) = ZonedMonthDayTime(
            LocalMonthDayTime(
                MonthDay.now(),
                LocalTime.now()
            ), zoneIdent.zoneId)


        /**
         * Constructs a `ZonedMonthDayTime` instance representing the current date and time in the system default time zone.
         *
         * The method uses the current system clock to generate the `MonthDay` and `LocalTime` components, along with the
         * appropriate time zone offset derived from the system default `ZoneId`.
         *
         * @return a new `ZonedMonthDayTime` instance based on the current date, time, and time zone
         * @since 1.0.0
         */
        @JvmStatic
        fun now() = ZonedMonthDayTime(
            LocalMonthDayTime(MonthDay.now(), LocalTime.now()),
            ZoneId.systemDefault()
        )

        /**
         * Parses the given char sequence into a [ZonedMonthDayTime] instance. The input can include timezone
         * or offset information. If the char sequence is blank, the current date and time (based on the current
         * timezone) are returned.
         *
         * @param cs the char sequence to be parsed; it can include a timezone in square brackets or an offset.
         * @return a [Result] containing the parsed [ZonedMonthDayTime] on success, or an exception on failure.
         * @since 1.0.0
         */
        @JvmStatic
        fun parse(cs: CharSequence): Result<ZonedMonthDayTime> = runCatching {
            val s = cs.toString()
            if (s.isBlank()) now()
            else {
                val zoned = "[" in s
                val zonePart = if (zoned) s[(s.indexOf("[") + 1)..<(s.indexOf("]"))] else ""
                val zone = if (zoned) ZoneId.of(zonePart) else null
                val offsetMonthDayTime =
                    OffsetMonthDayTime.parse(s.take(if (zoned) s.lastIndexOf("[") else s.length)).getOrThrow()
                ZonedMonthDayTime(offsetMonthDayTime.toLocalMonthDayTime(),
                    (if (zoned) zone else offsetMonthDayTime.offset)
                        ?: throw IllegalArgumentException("Invalid zone identifier: $zonePart")
                )
            }
        }

        /**
         * Converts a given [Temporal] instance to a [ZonedMonthDayTime] by extracting the respective
         * month, day, time, and zone offset information from it.
         *
         * The resulting [ZonedMonthDayTime] is constructed using the components derived from the provided
         * [Temporal], including the [MonthDay], [LocalTime], and [ZoneOffset].
         *
         * @receiver The [Temporal] instance to be converted into a [ZonedMonthDayTime].
         * @return A [ZonedMonthDayTime] representation of the given [Temporal].
         * @throws DateTimeException If the provided [Temporal] cannot supply the required fields.
         * @since 1.0.0
         */
        fun Temporal.toZonedMonthDayTime() = from(this)
        /**
         * Converts a {@link TemporalAccessor} to a {@code ZonedMonthDayTime} object.
         *
         * This function creates an instance of {@code ZonedMonthDayTime} by extracting
         * the {@code MonthDay}, {@code LocalTime}, and {@code ZoneOffset} from the given {@code TemporalAccessor}.
         * The resulting object represents a specific combination of month-day, time, and time zone offset.
         * It can be used for date-time operations and transformations that are sensitive to time zones.
         *
         * @receiver TemporalAccessor to be converted into a ZonedMonthDayTime instance.
         * @return A ZonedMonthDayTime representation of the given TemporalAccessor.
         * @throws DateTimeException if the TemporalAccessor cannot be converted to a ZonedMonthDayTime.
         *
         * @since 1.0.0
         */
        fun TemporalAccessor.toZonedMonthDayTime() = from(this)

        class Serializer : ValueSerializer<ZonedMonthDayTime>() {
            override fun serialize(
                value: ZonedMonthDayTime,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<ZonedMonthDayTime>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): ZonedMonthDayTime = parse(p.string).getOrThrow()
        }

        class OldSerializer : JsonSerializer<ZonedMonthDayTime>() {
            override fun serialize(value: ZonedMonthDayTime, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.toString())
            }
        }

        class OldDeserializer : JsonDeserializer<ZonedMonthDayTime>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext) = parse(p.text).getOrThrow()
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<ZonedMonthDayTime?, String?> {
            override fun convertToDatabaseColumn(attribute: ZonedMonthDayTime?) = if (Objects.isNull(attribute)) null else attribute.toString()
            override fun convertToEntityAttribute(dbData: String?) = if (dbData.isNull()) null else parse(dbData).getOrThrow()
        }
    }

    /**
     * Creates a new instance of `ZonedMonthDayTime` with the specified `monthDayTime` and `timeZone`.
     *
     * @param monthDayTime The `LocalMonthDayTime` value to use for the new instance. Defaults to the `monthDayTime` of the current instance.
     * @param timeZone The `ZoneId` value to use for the new instance. Defaults to the `timeZone` of the current instance.
     * @return A new `ZonedMonthDayTime` instance with the provided or default values for `monthDayTime` and `timeZone`.
     * @since 1.0.0
     */
    fun copy(monthDayTime: LocalMonthDayTime = this.monthDayTime, timeZone: ZoneId = this.timeZone) = ZonedMonthDayTime(monthDayTime, timeZone)
    /**
     * Creates a new instance of `ZonedMonthDayTime` with the specified `monthDayTime` and `timeZone`.
     * If no parameters are provided, it defaults to the current object's values.
     *
     * @param monthDayTime the `LocalMonthDayTime` to be used in the new instance. Defaults to this instance's `monthDayTime`.
     * @param timeZone the `ZoneIdent` to be used in the new instance. Defaults to this instance's `timeZone`.
     * @since 1.0.0
     */
    fun copy(monthDayTime: LocalMonthDayTime = this.monthDayTime, timeZone: ZoneIdent = ZoneIdent.of(this.timeZone.id)) = ZonedMonthDayTime(monthDayTime, timeZone.zoneId)

    /**
     * Determines if the current `ZonedMonthDayTime` is chronologically earlier than the specified `other`
     * based on their converted `LocalDateTime` representations, considering whether the years are leap years.
     *
     * @param other the `LocalMonthDayTime` to compare against
     * @param firstLeap a flag indicating if this time should be treated as part of a leap year
     * @param secondLeap a flag indicating if `other` should be treated as part of a leap year
     * @since 1.0.0
     */
    fun isBefore(other: ZonedMonthDayTime, firstLeap: Boolean, secondLeap: Boolean) =
        TimeZoneDesignator.Z.convert(toLocalDateTime(if (firstLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR))
            .isBefore(TimeZoneDesignator.Z.convert(other.toLocalDateTime(if (secondLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR)))

    /**
     * Compares if this instance represents a date and time that is after the specified `other` instance,
     * considering optional leap year adjustments for both the current instance and the `other` instance.
     *
     * @param other The `LocalMonthDayTime` instance to compare against.
     * @param firstLeap A Boolean flag indicating whether the current instance should be evaluated as a leap year.
     * @param secondLeap A Boolean flag indicating whether the `other` instance should be evaluated as a leap year.
     * @return `true` if this instance is after `other`, `false` otherwise.
     * @since 1.0.0
     */
    fun isAfter(other: ZonedMonthDayTime, firstLeap: Boolean, secondLeap: Boolean) =
        TimeZoneDesignator.Z.convert(toLocalDateTime(if (firstLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR))
            .isAfter(TimeZoneDesignator.Z.convert(other.toLocalDateTime(if (secondLeap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR)))

    /**
     * Compares this ZonedMonthDayTime instance with another LocalMonthDayTime instance to determine
     * if they are equal based on their converted local representations and leap configurations.
     *
     * @param other the LocalMonthDayTime instance to compare against
     * @param firstLeap a boolean indicating the leap year configuration for this instance
     * @param secondLeap a boolean indicating the leap year configuration for the other instance
     * @return true if both instances are considered equal based on their local representations
     *         and leap year configurations, otherwise false
     * @since 1.0.0
     */
    fun isEqual(other: ZonedMonthDayTime, firstLeap: Boolean, secondLeap: Boolean): Boolean {
        return TimeZoneDesignator.Z.convert(this.toLocalMonthDayTime())
            .isEqual(TimeZoneDesignator.Z.convert(other.toLocalMonthDayTime()), firstLeap, secondLeap)
    }

    /**
     * Returns a temporal object representing the start of the day for the given class type.
     * The specific behavior depends on the type of `Temporal` specified.
     *
     * @param T the type of the temporal object
     * @param `class` the class of the type to convert to, must implement `Temporal`
     * @return an instance of the specified temporal type, representing the start of the day
     * @throws DateTimeException if the class type is not supported
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
                    monthDayTime.monthDay,
                    LocalTime.MIDNIGHT,
                    offset
                )
            )
            "dev.tommasop1804.kutils.classes.time.ZonedMonthDayTime" -> `class`.cast(
                ZonedMonthDayTime(
                    LocalMonthDayTime(
                        monthDayTime.monthDay,
                        LocalTime.MIDNIGHT
                    ), timeZone
                )
            )
            else -> throw DateTimeException("Unsupported class: " + `class`.getName())
        }
    }

    /**
     * Returns an instance of the specified temporal type adjusted to the end of the day.
     *
     * This method provides behavior based on the provided class type:
     * - For `LocalDate`, it sets the time to the last day of the month at 00:00.
     * - For `LocalDateTime`, it sets the time to the last day of the month at 23:59:59.999999999.
     * - For `LocalTime`, it returns the maximum possible time: `23:59:59.999999999`.
     * - For custom local or offset month-day-time implementations, it creates instances representing their corresponding
     *   temporal type at the maximum possible time on the specified day and with the relevant offset or zone.
     *
     * @param T the type of temporal class
     * @param `class` the temporal class type to be returned, not null
     * @return an instance of the specified temporal type at the end of the day, adjusted as per the type
     * @throws DateTimeException if the class type is unsupported
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
                ZonedMonthDayTime(
                    LocalMonthDayTime(
                        monthDayTime.monthDay,
                        LocalTime.MAX
                    ), offset
                )
            )
            else -> throw DateTimeException("Unsupported class: " + `class`.getName())
        }
    }

    /**
     * Adds the specified number of months to this instance, returning a new instance with the updated value.
     *
     * The addition considers whether the operation is being performed in a leap year based on the `leap` flag.
     * If the resulting day in the target month does not exist (e.g., adding months to February 29th in
     * a non-leap year), it adjusts to the last valid day of the month.
     *
     * @param amountToAdd the number of months to add, positive or negative.
     * @param leap a flag indicating whether leap year logic should be applied for the computation. Defaults to false.
     * @return a new `ZonedMonthDayTime` instance with the added months.
     * @since 1.0.0
     */
    fun plusMonths(amountToAdd: Long, leap: Boolean = false) = ZonedMonthDayTime(
        LocalMonthDayTime(
            MonthDay.from(
                LocalDate.of(
                    if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
                    monthDayTime.monthDay.month,
                    monthDayTime.monthDay.dayOfMonth
                ).plusMonths(amountToAdd)
            ),
            monthDayTime.localTime
        ), timeZone
    )

    /**
     * Adds the specified number of weeks to the current date and time, while considering leap year adjustments if specified.
     *
     * @param amountToAdd the number of weeks to add, which can be negative for subtraction.
     * @param leap a flag indicating whether leap year adjustments should be considered. Defaults to false.
     * @since 1.0.0
     */
    fun plusWeeks(amountToAdd: Long, leap: Boolean = false) = ZonedMonthDayTime(
        LocalMonthDayTime(
            MonthDay.from(
                LocalDate.of(
                    if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
                    monthDayTime.monthDay.month,
                    monthDayTime.monthDay.dayOfMonth
                ).plusWeeks(amountToAdd)
            ),
            monthDayTime.localTime
        ), timeZone
    )

    /**
     * Adds the specified number of days to the current `ZonedMonthDayTime` instance.
     * This function considers the leap year context when required.
     *
     * @param amountToAdd the number of days to add
     * @param leap indicates if the calculation should consider a leap year; defaults to `false`
     * @since 1.0.0
     */
    fun plusDays(amountToAdd: Long, leap: Boolean = false) = ZonedMonthDayTime(
        LocalMonthDayTime(
            MonthDay.from(
                LocalDate.of(
                    if (leap) DEFAULT_LEAP_YEAR else DEFAULT_NON_LEAP_YEAR,
                    monthDayTime.monthDay.month,
                    monthDayTime.monthDay.dayOfMonth
                ).plusDays(amountToAdd)
            ),
            monthDayTime.localTime
        ), timeZone
    )

    /**
     * Adds the specified number of hours to this ZonedMonthDayTime instance.
     *
     * @param amountToAdd the number of hours to add, may be negative.
     * @param leap whether to consider leap year calculations when performing the addition. Defaults to false.
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
     * Adds the specified number of minutes to this `ZonedMonthDayTime` instance.
     *
     * @param amountToAdd The number of minutes to add, positive or negative.
     * @param leap A boolean flag indicating whether to calculate the addition in the context of a leap year.
     * Defaults to `false`.
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
     * Adds the specified number of seconds to this instance, accounting for leap year if specified.
     *
     * @param amountToAdd the number of seconds to add.
     * @param leap a boolean indicating whether to consider leap year rules. Defaults to `false`.
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
     * Adds the specified amount of nanoseconds to this `ZonedMonthDayTime`, optionally considering leap years.
     *
     * @param amountToAdd the number of nanoseconds to add, may be negative
     * @param leap whether to consider a leap year for the operation; `false` by default
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
     * Adjusts the given `newDate` by adding the specified hours, minutes, seconds, and nanoseconds
     * while accounting for potential overflow into a new day. The resulting `ZonedMonthDayTime`
     * encapsulates the modified date and time adjusted for the time zone.
     *
     * @param newDate the base date to which the time adjustments will be applied
     * @param hours the number of hours to add
     * @param minutes the number of minutes to add
     * @param seconds the number of seconds to add
     * @param nanos the number of nanoseconds to add
     * @return a `ZonedMonthDayTime` representing the updated date and time after applying the additions
     * @since 1.0.0
     */
    private fun plusWithOverflow(
        newDate: LocalDate,
        hours: Long,
        minutes: Long,
        seconds: Long,
        nanos: Long
    ): ZonedMonthDayTime {
        if ((hours or minutes or seconds or nanos) == 0L) return ZonedMonthDayTime(
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
        return ZonedMonthDayTime(LocalMonthDayTime(MonthDay.from(newDate.plusDays(totDays)), newTime), timeZone)
    }

    /**
     * Adds the specified amount of time to this `ZonedMonthDayTime` object based on the given temporal unit.
     *
     * @param amountToAdd the amount of the specified unit to add
     * @param unit the temporal unit to use for the addition
     * @param leap a flag indicating if leap year adjustments should be considered during computation
     * @return a new `ZonedMonthDayTime` instance with the added amount of time
     * @since 1.0.0
     */
    fun plus(amountToAdd: Long, unit: TemporalUnit, leap: Boolean): ZonedMonthDayTime {
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
     * Adds the specified amount to this temporal object using the specified unit.
     *
     * @param amountToAdd the amount to add, may be negative
     * @param unit the unit of the amount to add, not null
     * @return a copy of this temporal object with the specified amount added, not null
     * @since 1.0.0
     */
    override fun plus(amountToAdd: Long, unit: TemporalUnit) = plus(amountToAdd, unit, false)

    /**
     * Subtracts the specified number of months from this instance, accounting for leap year logic if applicable.
     *
     * @param amountToSubtract the number of months to subtract, may be negative
     * @param leap whether to apply leap year adjustments; defaults to false
     * @since 1.0.0
     */
    fun minusMonths(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusMonths(MAX_VALUE, leap).plusMonths(1, leap)
        else plusMonths(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of weeks from the current instance.
     * If the subtraction results in overflow of the supported range, calculations will be adjusted accordingly.
     *
     * @param amountToSubtract the number of weeks to subtract; may be negative to perform addition
     * @param leap a boolean indicating if leap year adjustments should be applied during the calculation
     * @since 1.0.0
     */
    fun minusWeeks(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusWeeks(MAX_VALUE, leap).plusWeeks(1, leap)
        else plusWeeks(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of days from this instance, optionally considering leap years.
     *
     * @param amountToSubtract the number of days to subtract. If the value is equal to `Long.MIN_VALUE`,
     *                         the logic ensures correct subtraction by breaking it into multiple operations.
     * @param leap a flag indicating whether leap years should be considered in the operation.
     *             Defaults to `false` if not specified.
     * @since 1.0.0
     */
    fun minusDays(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusDays(MAX_VALUE, leap).plusDays(1, leap)
        else plusDays(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of hours from this instance while considering
     * the optional leap year adjustment.
     *
     * @param amountToSubtract the number of hours to subtract, which can be negative
     * @param leap whether to consider leap year adjustments, default is `false`
     * @since 1.0.0
     */
    fun minusHours(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusHours(MAX_VALUE, leap).plusHours(1, leap)
        else plusHours(-amountToSubtract, leap)

    /**
     * Subtracts a specified number of minutes from the current instance.
     * If the `amountToSubtract` value equals `MIN_VALUE`, the operation is
     * internally converted to equivalent `plusMinutes` calls to handle
     * overflow scenarios.
     *
     * @param amountToSubtract the number of minutes to subtract, may be negative
     * @param leap a flag to indicate if a leap year should be used for calculations, defaults to `false`
     * @since 1.0.0
     */
    fun minusMinutes(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusMinutes(MAX_VALUE, leap).plusMinutes(1, leap)
        else plusMinutes(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of seconds from the current instance.
     *
     * The operation considers the optional `leap` parameter which determines
     * if leap years are taken into account during the calculation.
     *
     * @param amountToSubtract the number of seconds to subtract.
     * Positive values reduce the time, and negative values add seconds.
     * @param leap optional parameter indicating if leap year adjustment
     * is applied. Defaults to `false`.
     * @since 1.0.0
     */
    fun minusSeconds(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusSeconds(MAX_VALUE, leap).plusSeconds(1, leap)
        else plusSeconds(-amountToSubtract, leap)

    /**
     * Subtracts the specified number of nanoseconds from the current instance.
     * If the specified amount is equal to `Long.MIN_VALUE`, the subtraction is handled
     * by adding `Long.MAX_VALUE` nanoseconds, followed by an additional subtraction of 1 nanosecond.
     *
     * @param amountToSubtract the number of nanoseconds to subtract. Can be a negative value.
     * @param leap indicates whether the operation should account for leap years. Default is `false`.
     * @since 1.0.0
     */
    fun minusNanos(amountToSubtract: Long, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plusNanos(MAX_VALUE, leap).plusNanos(1, leap)
        else plusNanos(-amountToSubtract, leap)

    /**
     * Subtracts the specified amount of time from this instance using the given temporal unit.
     *
     * The subtraction is performed based on the provided amount and unit,
     * optionally accounting for leap behavior. If the amount to subtract equals
     * the minimum possible value, the result is calculated to avoid numeric overflow.
     *
     * @param amountToSubtract the amount of time to subtract, measured in terms of the specified unit
     * @param unit the temporal unit in which the subtraction will be performed
     * @param leap a boolean value indicating whether leap behavior should be considered; defaults to false
     * @since 1.0.0
     */
    fun minus(amountToSubtract: Long, unit: TemporalUnit, leap: Boolean = false) =
        if (amountToSubtract == MIN_VALUE) plus(MAX_VALUE, unit, leap).plus(1, unit, leap)
        else plus(-amountToSubtract, unit, leap)

    /**
     * Converts the given `year` into a `LocalDateTime` instance by combining it with the month, day, and time
     * components from the `ZonedMonthDayTime` instance.
     *
     * @param year The year to be used in constructing the `LocalDateTime`.
     * @return A `LocalDateTime` instance representing the specified year and the month, day, and time
     * of the given `ZonedMonthDayTime` instance.
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
     * Converts the current ZonedMonthDayTime instance to a LocalDateTime object using the specified Year.
     *
     * @param year The Year to be used for the conversion. Defaults to the current year if not specified.
     * @return A LocalDateTime representing the date and time of this ZonedMonthDayTime instance.
     * @since 1.0.0
     */
    fun toLocalDateTime(year: Year = Year.now()): LocalDateTime = toLocalDateTime(year.value)

    /**
     * Converts the provided year and the internal state of the containing object into an OffsetDateTime.
     *
     * @param year The year to use for constructing the OffsetDateTime.
     * @return The constructed OffsetDateTime object based on the provided year and the internal state.
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
     * Converts the current instance to an OffsetDateTime using the specified or current year.
     *
     * @param year the year to use for the conversion. Defaults to the current year if not specified.
     * @return an OffsetDateTime representing the current instance for the specified year.
     * @since 1.0.0
     */
    fun toOffsetDateTime(year: Year = Year.now()): OffsetDateTime = toOffsetDateTime(year.value)

    /**
     * Converts the specified year along with the stored month, day, and time information
     * into a `ZonedDateTime` in the associated time zone.
     *
     * @param year the year value to be used in the resulting `ZonedDateTime`
     * @return a `ZonedDateTime` representing the complete date-time information in the appropriate time zone
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
        timeZone
    )

    /**
     * Converts the current object to a `ZonedDateTime` using the specified year.
     *
     * @param year The `Year` to use when converting to a `ZonedDateTime`. Defaults to the current year.
     * @return A `ZonedDateTime` representation of this object with the provided year.
     * @since 1.0.0
     */
    fun toZonedDateTime(year: Year = Year.now()): ZonedDateTime = toZonedDateTime(year.value)

    /**
     * Converts the current object to a LocalDate instance using the specified year.
     *
     * @param year the year to be used in the resulting LocalDate
     * @return a LocalDate instance created from the provided year and the existing month and day
     * @since 1.0.0
     */
    fun toLocalDate(year: Int): LocalDate =
        LocalDate.of(year, monthDayTime.monthDay.month, monthDayTime.monthDay.dayOfMonth)

    /**
     * Converts the given year or the current year (if not provided) into a LocalDate object.
     *
     * @param year the year to be used for the LocalDate conversion. Defaults to the current year if not specified.
     * @return a LocalDate representation of the provided or default year.
     * @since 1.0.0
     */
    fun toLocalDate(year: Year = Year.now()): LocalDate = toLocalDate(year.value)

    /**
     * Converts the instance to a `MonthDay` object, representing the month and day of this object.
     *
     * @return a `MonthDay` object representing the month and day.
     * @since 1.0.0
     */
    fun toMonthDay(): MonthDay = monthDayTime.monthDay

    /**
     * Converts the current instance to a `LocalTime` representation.
     *
     * @return the `LocalTime` part of this instance.
     * @since 1.0.0
     */
    fun toLocalTime(): LocalTime = monthDayTime.localTime

    /**
     * Converts the current date-time object to an `OffsetTime` instance.
     * The resulting `OffsetTime` represents the time component of the current instance
     * combined with its associated time-zone offset.
     *
     * @return an `OffsetTime` instance containing the local time and offset of this date-time object.
     * @since 1.0.0
     */
    fun toOffsetTime(): OffsetTime = OffsetTime.of(monthDayTime.localTime, offset)

    /**
     * Returns a string representation of the object by combining the local date-time
     * and offset in a specific format.
     *
     * The method uses `toLocalMonthDayTime()` to get the local representation
     * of the date and time, converts it to a string, and appends the string
     * representation of the offset.
     *
     * @return A string combining the local month-day-time and offset.
     * @since 1.0.0
     */
    @Suppress("IDENTITY_SENSITIVE_OPERATIONS_WITH_VALUE_TYPE")
    override fun toString() =
        (monthDayTime.toString() + if (timeZone.toString().startsWith("UTC") || timeZone.toString()
                .startsWith("+") || timeZone.toString().startsWith("-") || timeZone == ZoneOffset.UTC
        ) "" else ("[$timeZone]"))

    /**
     * Compares this [ZonedMonthDayTime] instance with another [ZonedMonthDayTime] instance.
     * The comparison is performed by converting both instances to a common time zone and
     * comparing them as [LocalMonthDayTime].
     *
     * @param other The [ZonedMonthDayTime] instance to be compared with this instance.
     * @return A negative integer, zero, or a positive integer as this object is less than,
     * equal to, or greater than the specified object.
     * @since 1.0.0
     */
    override operator fun compareTo(other: ZonedMonthDayTime) =
        LocalMonthDayTime.from(TimeZoneDesignator.Z.convert(toLocalDateTime()))
            .compareTo(LocalMonthDayTime.from(TimeZoneDesignator.Z.convert(other.toLocalDateTime())))

    /**
     * Checks if the specified temporal unit is supported by this instance.
     *
     * This method verifies whether the given temporal unit is supported for operations
     * such as addition, subtraction, or querying.
     *
     * @param unit the temporal unit to check for support
     * @return `true` if the temporal unit is supported, `false` otherwise
     * @since 1.0.0
     */
    override fun isSupported(unit: TemporalUnit) = when (unit) {
        ChronoUnit.NANOS, ChronoUnit.MICROS, ChronoUnit.MILLIS, ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS, ChronoUnit.HALF_DAYS, ChronoUnit.DAYS, ChronoUnit.WEEKS, ChronoUnit.MONTHS -> true
        else -> false
    }

    /**
     * Checks if the given temporal field is supported by this instance.
     *
     * @param field the temporal field to check, may be null
     * @return true if the field is supported, false otherwise
     * @since 1.0.0
     */
    override fun isSupported(field: TemporalField?) = when (field) {
        ChronoField.DAY_OF_MONTH, ChronoField.MONTH_OF_YEAR, ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.NANO_OF_DAY, ChronoField.NANO_OF_SECOND, ChronoField.OFFSET_SECONDS -> true
        else -> false
    }

    /**
     * Returns a new `ZonedMonthDayTime` instance with the specified `MonthDay` while retaining the existing time
     * and time zone information from the current instance.
     *
     * @param monthDay the `MonthDay` to replace in this instance, not null
     * @return a new `ZonedMonthDayTime` instance with the specified `MonthDay`
     * @since 1.0.0
     */
    fun withMontDay(monthDay: MonthDay) = ZonedMonthDayTime(
        LocalMonthDayTime(
            monthDay,
            monthDayTime.localTime
        ), timeZone)

    /**
     * Returns a copy of this `ZonedMonthDayTime` with the month altered to the specified value.
     * The resulting instance will have the same day, time, and time zone, but the month will be replaced
     * with the given value.
     *
     * @param month The new month value to set, where 1 represents January and 12 represents December.
     * Must be in the valid range of 1 to 12.
     * @return A new `ZonedMonthDayTime` instance with the updated month.
     * @since 1.0.0
     * @throws DateTimeException if the month value is invalid (not in the range 1–12).
     */
    fun withMonth(month: Int): ZonedMonthDayTime {
        month in 1..12 || throw DateTimeException("Invalid value for MonthOfYear: $month")
        return ZonedMonthDayTime(
            LocalMonthDayTime(
                MonthDay.of(month, monthDayTime.monthDay.dayOfMonth),
                monthDayTime.localTime
            ),
            timeZone
        )
    }

    /**
     * Returns a copy of this `ZonedMonthDayTime` with the specified month.
     * The day within the month remains unchanged, unless it is invalid
     * for the new month, in which case the last valid day of the new month is used.
     *
     * @param month the month to set, not null
     * @since 1.0.0
     */
    fun withMonth(month: Month) = withMonth(month.value)

    /**
     * Returns a new `ZonedMonthDayTime` instance with the day-of-month updated to the specified day.
     *
     * The method validates whether the specified day is valid for the current month. If the day is
     * out of range, a `DateTimeException` is thrown.
     *
     * @param day The day-of-month value to set, must be within the valid range for the current month.
     * @return A new instance of `ZonedMonthDayTime` with the day-of-month updated.
     * @throws DateTimeException If the day is not valid for the current month.
     * @since 1.0.0
     */
    fun withDay(day: Int): ZonedMonthDayTime {
        day < 1 || day > monthDayTime.monthDay.month.maxLength() || throw DateTimeException("Invalid value for DayOfMonth: $day")
        return ZonedMonthDayTime(
            LocalMonthDayTime(
                MonthDay.of(monthDayTime.monthDay.month, day),
                monthDayTime.localTime
            ), timeZone
        )
    }

    /**
     * Returns a new instance of ZonedMonthDayTime with the specified local time.
     *
     * @param localTime the local time to associate with the current month-day and time zone, not null
     * @since 1.0.0
     */
    fun withTime(localTime: LocalTime) = ZonedMonthDayTime(
        LocalMonthDayTime(
            monthDayTime.monthDay,
            localTime
        ), timeZone)

    /**
     * Returns a new `ZonedMonthDayTime` instance with the hour-of-day updated to the specified value.
     *
     * @param hour The hour-of-day to set, ranging from 0 to 23.
     * @return A new `ZonedMonthDayTime` instance with the updated hour value.
     * @throws DateTimeException if the specified hour is not within the valid range of 0 to 23.
     * @since 1.0.0
     */
    fun withHour(hour: Int): ZonedMonthDayTime {
        hour in 0..23 || throw DateTimeException("Invalid value for HourOfDay: $hour")
        return ZonedMonthDayTime(
            LocalMonthDayTime(
                MonthDay.from(toLocalDate()),
                LocalTime.of(
                    hour,
                    monthDayTime.localTime.minute,
                    monthDayTime.localTime.second,
                    monthDayTime.localTime.nano
                )
            ), timeZone
        )
    }

    /**
     * Returns a copy of this `ZonedMonthDayTime` with the minute-of-hour altered.
     *
     * This operation changes the minute-of-hour of the underlying `LocalTime`,
     * while keeping the other fields unchanged. If the input minute is not
     * within the valid range (0-59), a `DateTimeException` will be thrown.
     *
     * @param minute the minute-of-hour to set in the new instance, from 0 to 59
     * @return a new `ZonedMonthDayTime` instance with the specified minute-of-hour
     * @throws DateTimeException if the minute is invalid
     * @since 1.0.0
     */
    fun withMinute(minute: Int): ZonedMonthDayTime {
        minute in 0..59 || throw DateTimeException("Invalid value for MinuteOfHour: $minute")
        return ZonedMonthDayTime(
            LocalMonthDayTime(
                MonthDay.from(toLocalDate()),
                LocalTime.of(
                    monthDayTime.localTime.hour,
                    minute,
                    monthDayTime.localTime.second,
                    monthDayTime.localTime.nano
                )
            ), timeZone
        )
    }

    /**
     * Returns a new instance of `ZonedMonthDayTime` with the second-of-minute field updated.
     *
     * The valid range for the second value is from 0 to 59. If the provided value is
     * out of bounds, a `DateTimeException` is thrown.
     *
     * @param second The new second value to set, from 0 to 59.
     * @return A new `ZonedMonthDayTime` instance with the updated second-of-minute field.
     * @since 1.0.0
     * @throws DateTimeException if the second value is not within the valid range (0-59).
     */
    fun withSecond(second: Int): ZonedMonthDayTime {
        second in 0..59 || throw DateTimeException("Invalid value for SecondOfMinute: $second")
        return ZonedMonthDayTime(
            LocalMonthDayTime(
                MonthDay.from(toLocalDate()),
                LocalTime.of(
                    monthDayTime.localTime.hour,
                    monthDayTime.localTime.minute,
                    second,
                    monthDayTime.localTime.nano
                )
            ), timeZone
        )
    }

    /**
     * Returns a copy of this `ZonedMonthDayTime` with the specified nano-of-second.
     *
     * This method updates the nano-of-second field within the time portion of this object, keeping
     * all other fields unchanged. The valid range for nano-of-second is from 0 to 999,999,999.
     *
     * @param nanoOfSecond the nano-of-second to be set, from 0 to 999,999,999
     * @return a new `ZonedMonthDayTime` instance with the specified nano-of-second
     * @throws DateTimeException if the nano-of-second value is invalid
     * @since 1.0.0
     */
    fun withNano(nanoOfSecond: Int): ZonedMonthDayTime {
        nanoOfSecond in 0..999999999 || throw DateTimeException("Invalid value for NanoOfSecond: $nanoOfSecond")
        return ZonedMonthDayTime(
            LocalMonthDayTime(
                MonthDay.from(toLocalDate()),
                LocalTime.of(
                    monthDayTime.localTime.hour,
                    monthDayTime.localTime.minute,
                    monthDayTime.localTime.second,
                    nanoOfSecond
                )
            ), timeZone
        )
    }

    /**
     * Returns a new `ZonedMonthDayTime` instance with the specified `ZoneOffset`.
     *
     * @param offset the `ZoneOffset` to apply to the instance, not null
     * @since 1.0.0
     */
    fun withOffset(offset: ZoneOffset) = ZonedMonthDayTime(
        LocalMonthDayTime(
            monthDayTime.monthDay,
            monthDayTime.localTime
        ), offset)
    /**
     * Adjusts the time-zone offset based on the provided ZoneIdent.
     *
     * @param zoneIdent The ZoneIdent object containing the offset to be applied.
     * @since 1.0.0
     */
    fun withOffset(zoneIdent: ZoneIdent) = withOffset(zoneIdent.offset)

    /**
     * Returns a new instance of `ZonedMonthDayTime` with the specified time zone.
     *
     * This method modifies the time zone of the current `ZonedMonthDayTime` without altering
     * the local time or the month-day component. The resulting instance will reflect the same
     * local date-time but associated with the given time zone.
     *
     * @param zone the desired time zone, not null
     * @return a new `ZonedMonthDayTime` instance with the specified time zone
     * @since 1.0.0
     */
    fun withZone(zone: ZoneId) = ZonedMonthDayTime(
        LocalMonthDayTime(
            monthDayTime.monthDay,
            monthDayTime.localTime
        ), zone)
    /**
     * Returns a new instance of this object with the specified time zone applied.
     *
     * @param zoneIdent The ZoneIdent object representing the desired time zone to be applied.
     * @since 1.0.0
     */
    fun withZone(zoneIdent: ZoneIdent) = withZone(zoneIdent.zoneId)

    /**
     * Updates the value of the specified temporal field with the given new value and returns a new instance
     * of the Temporal object with the updated field value.
     *
     * @param field the temporal field to update, must not be null and supported by the instance
     * @param newValue the new value to set for the specified temporal field
     * @return a new Temporal instance with the updated field value
     * @throws UnsupportedTemporalTypeException if the specified field is unsupported
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
            ChronoField.NANO_OF_DAY -> ZonedMonthDayTime(LocalMonthDayTime(
                MonthDay.from(toLocalDate()),
                LocalTime.ofNanoOfDay(newValue)
            ), timeZone)
            ChronoField.NANO_OF_SECOND -> withNano(newValue.toInt())
            ChronoField.OFFSET_SECONDS -> withOffset(ZoneOffset.ofTotalSeconds(newValue.toInt()))
            else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
    }

    /**
     * Calculates the amount of time between this temporal object and another temporal object of the same type.
     *
     * The calculation is based on the specified unit and the temporal object to compare against.
     *
     * @param endExclusive the end temporal object, exclusive, to which the difference is calculated
     * @param unit the unit in which the difference is measured
     * @since 1.0.0
     */
    override fun until(endExclusive: Temporal, unit: TemporalUnit) =
        TimeZoneDesignator.Z.convert(toLocalDateTime()).until(
            TimeZoneDesignator.Z.convert(LocalMonthDayTime.from(endExclusive).toLocalDateTime()),
            unit
        )

    /**
     * Retrieves the value of the specified temporal field from the ZonedMonthDayTime instance as a `Long`.
     *
     * Supported fields include:
     * - `DAY_OF_MONTH`: Returns the day of the month.
     * - `MONTH_OF_YEAR`: Returns the month of the year.
     * - `HOUR_OF_DAY`: Returns the hour of the day.
     * - `MINUTE_OF_HOUR`: Returns the minute within the hour.
     * - `SECOND_OF_MINUTE`: Returns the second within the minute.
     * - `NANO_OF_SECOND`: Returns the nanosecond within the second.
     * - `OFFSET_SECONDS`: Returns the total seconds of the time zone offset.
     *
     * If the provided field is not supported, an `UnsupportedTemporalTypeException` will be thrown.
     *
     * @param field The temporal field to retrieve the value for.
     * @return The value of the specified temporal field as a `Long`.
     * @throws UnsupportedTemporalTypeException If the field is not supported.
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
     * Adjusts the specified temporal object to have the fields of this instance,
     * including the day of the month, month of the year, time components, and offset.
     *
     * @param temporal the temporal object to modify, not null
     * @return a temporal object of the same type, with the adjustments made, not null
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
     * Adjusts the time-zone of the instance to the specified zone,
     * retaining the same instant on the time-line. This method returns
     * a new instance with the specified time-zone, ensuring that the
     * instant remains unaltered while altering the representation as
     * per the new zone.
     *
     * @param zone the time-zone to change to, not null
     * @return a new instance with the updated time-zone
     * @since 1.0.0
     */
    fun withZoneSameInstant(zone: ZoneId) = from(toZonedDateTime().withZoneSameInstant(zone))

    /**
     * Adjusts this object to the same instant in the specified time-zone.
     *
     * This method converts the current object's time to the specified time-zone,
     * ensuring that the resulting date-time represents the same moment in time
     * but in the context of a different zone.
     *
     * @param zoneIdent the target time-zone to be applied to this object
     * @since 1.0.0
     */
    fun withZoneSameInstant(zoneIdent: ZoneIdent) = from(toZonedDateTime().withZoneSameInstant(zoneIdent.zoneId))

    /**
     * Adjusts the time-zone of this object to the specified zone, retaining the local date-time.
     * This method changes the time-zone, while keeping the local date and time unchanged.
     *
     * @param zone the time-zone to apply, not null
     * @since 1.0.0
     */
    fun withZoneSameLocal(zone: ZoneId) = from(toZonedDateTime().withZoneSameLocal(zone))

    /**
     * Returns a copy of this instance with the time-zone changed, retaining the local time and date.
     * This method is used to adjust the time-zone to the one specified while keeping the same local
     * date and time in the newly assigned time-zone.
     *
     * @param zoneIdent the time-zone identifier specifying the new time-zone to use, not null
     * @since 1.0.0
     */
    fun withZoneSameLocal(zoneIdent: ZoneIdent) = from(toZonedDateTime().withZoneSameLocal(zoneIdent.zoneId))

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
        "offset" to offset,
        "timeZone" to timeZone,
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
     * - `timeZone`: The time zone - TYPE: [ZoneId].
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
     * Provides the seventh component of the object, represented by the `timeZone` property.
     * This enables destructuring declarations to access the `timeZone` value directly.
     *
     * @return The value of the `timeZone` property.
     * @since 1.0.0
     */
    operator fun component7() = timeZone
    /**
     * Returns the seventh component of the instance, represented by its offset value.
     * This function is a part of the componentN operator conventions and is typically used in destructuring declarations.
     *
     * @return The offset value as the seventh component.
     * @since 1.0.0
     */
    operator fun component8() = offset
}