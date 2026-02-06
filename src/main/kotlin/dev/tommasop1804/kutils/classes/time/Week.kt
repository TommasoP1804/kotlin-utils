package dev.tommasop1804.kutils.classes.time

import dev.tommasop1804.kutils.MonoPair
import dev.tommasop1804.kutils.expect
import dev.tommasop1804.kutils.invoke
import dev.tommasop1804.kutils.toYear
import java.time.*
import java.time.temporal.*
import kotlin.reflect.KProperty
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

@Suppress("unused", "kutils_temporal_now_as_temporal", "kutils_temporal_of_as_temporal")
class Week private constructor(val firstDay: LocalDate): TemporalAccessor, Comparable<Week> {
    /**
     * Represents the last day of a specific week, calculated as six days
     * after the first day of the same week.
     *
     * This value is derived by adding six days to the value of `firstDay`.
     *
     * @return the calculated last day of the week as a LocalDate
     * @since 1.0.0
     */
    val lastDay: LocalDate
        get() = firstDay.plusDays(6)
    /**
     * Represents the ISO week number of the year for the first day of a specific time period.
     *
     * This property retrieves the week number based on the ISO-8601 standard,
     * where weeks start on a Monday and have a minimum of 4 days.
     *
     * @return The week number of the year as an integer value.
     * @since 1.0.0
     */
    val weekOfYear: Int
        get() = firstDay.get(WeekFields.ISO.weekOfWeekBasedYear())
    /**
     * Represents the week of the month based on the `WeekFields.ISO` standard.
     * This value is calculated dynamically using the first day of the desired period.
     *
     * The returned value indicates which week in the month the specified date falls into,
     * following the ISO-8601 week-based calendar system.
     *
     * @return An integer value representing the current week of the month.
     * @since 1.0.0
     */
    val weekOfMonth: Int
        get() = firstDay.get(WeekFields.ISO.weekOfMonth())
    /**
     * Represents the year component retrieved from the `firstDay` property.
     * Converts the year value to a `Year` object.
     *
     * @return the year as a `Year` instance.
     * @since 1.0.0
     */
    val year: Year
        get() = firstDay.get(WeekFields.ISO.weekBasedYear()).toYear()()
    /**
     * Indicates whether the range of days spans across multiple months.
     *
     * This property evaluates to `true` if the `firstDay` and `lastDay`
     * belong to different months, and `false` otherwise.
     *
     * @since 1.0.0
     */
    val containsDaysOfOtherMonths: Boolean
        get() = firstDay.month != lastDay.month
    /**
     * Represents a property that provides a pair of months derived from the first and last days of a period.
     * The property retrieves the `Month` value from both `firstDay` and `lastDay`.
     *
     * @return A pair of `Month` values, where the first represents the month of the first day
     * and the second represents the month of the last day.
     * @since 1.0.0
     */
    val month: MonoPair<Month>
        get() = firstDay.month to lastDay.month
    /**
     * Retrieves the aligned week of the year for the first day of a specified period.
     * The aligned week-of-year is represented as an integer value derived from the associated
     * `firstDay` object's `ChronoField.ALIGNED_WEEK_OF_YEAR` field. This value aligns with
     * the ISO-8601 definition of week numbering.
     *
     * @return The aligned week-of-year of the first day as an integer.
     * @since 1.0.0
     */
    val alignedWeekOfYearOfFirstDay: Int
        get() = firstDay.get(ChronoField.ALIGNED_WEEK_OF_YEAR)
    /**
     * Represents the aligned week of the month for the first day of a given date.
     * The aligned week is a sequential number within the current month, starting from 1,
     * where each week starts on the same aligned day of the week.
     *
     * This value is calculated based on the `ChronoField.ALIGNED_WEEK_OF_MONTH` field
     * from the first day of the relevant month.
     *
     * @return The aligned week of the month for the first day, as an integer.
     * @since 1.0.0
     */
    val alignedWeekOfMonthOfFirstDay: Int
        get() = firstDay.get(ChronoField.ALIGNED_WEEK_OF_MONTH)
    /**
     * Represents the aligned week of the year for the last day of a specific period or date.
     * The value is derived from the last day's `ChronoField.ALIGNED_WEEK_OF_YEAR`.
     *
     * This property provides the aligned week number within the current year, where
     * the first aligned week starts from the first day of the year and aligns properly
     * across the weeks in sequential order.
     *
     * @return The aligned week of the year for the last day as an integer.
     * @since 1.0.0
     */
    val alignedWeekOfYearOfLastDay: Int
        get() = lastDay.get(ChronoField.ALIGNED_WEEK_OF_YEAR)
    /**
     * Represents the aligned week number within the current month for the last day
     * of a specific period, determined using the `ChronoField.ALIGNED_WEEK_OF_MONTH`.
     * The aligned week is a non-overlapping week boundary count that resets each month,
     * starting from 1 for the first week of the month.
     *
     * This property is computed based on the first day's aligned week in the context
     * of the relevant period from which it is derived.
     *
     * @return The aligned week of the month for the last day as an integer value.
     * @since 1.0.0
     */
    val alignedWeekOfMonthOfLastDay: Int
        get() = firstDay.get(ChronoField.ALIGNED_WEEK_OF_MONTH)
    /**
     * A read-only property that provides a list of `LocalDate` objects, representing
     * a sequence of seven consecutive days starting from `firstDay`.
     *
     * The days are calculated dynamically based on the `firstDay` property.
     *
     * @receiver This property relies on the existence of `firstDay` to compute the values.
     * @return A list containing seven `LocalDate` objects in ascending order.
     * @since 1.0.0
     */
    val days: List<LocalDate>
        get() = (0..6).map { firstDay.plusDays(it.toLong()) }
    /**
     * Represents a range of dates from a starting date (`firstDay`) to an ending date (`lastDay`), inclusive.
     *
     * The `daysRange` property provides a convenient way to define and access this range of dates,
     * which can be used for filtering, validation, or computations over a specific period.
     *
     * @return A `ClosedRange` of `LocalDate` representing the inclusive range of days.
     * @since 1.0.0
     */
    val daysRange: ClosedRange<LocalDate>
        get() = firstDay..lastDay

    /**
     * Constructs an instance using the specified year and week number.
     *
     * @param year the year to use
     * @param weekNumber the week number within the year
     * @since 1.0.0
     */
    constructor(year: Year, weekNumber: Int) : this(of(year, weekNumber))

    /**
     * Constructor initializes an instance using a specified year and week number.
     *
     * @param year the year for the instance
     * @param weekNumber the week number of the year
     * @since 1.0.0
     */
    constructor(year: Int, weekNumber: Int) : this(of(year, weekNumber))

    /**
     * Constructs an instance using the specified year and month, and the given week number.
     * Delegates to another constructor with the instance created using the provided parameters.
     *
     * @param yearMonth the year and month from which the instance will be created
     * @param weekNumber the week number associated with the specified year and month
     * @since 1.0.0
     */
    constructor(yearMonth: YearMonth, weekNumber: Int) : this(of(yearMonth, weekNumber))

    /**
     * Constructs an instance using the provided instant and time-zone.
     *
     * @param instant the instant to convert, not null
     * @param zone the time-zone to use, not null
     * @since 1.0.0
     */
    constructor(instant: Instant, zone: ZoneId) : this(from(instant.atZone(zone)))
    /**
     * Constructs an instance using the given `Instant` and `ZoneIdent`.
     *
     * The provided `instant` represents a moment on the timeline, and the `zone`
     * specifies the time zone to be associated with this instance.
     *
     * @param instant the instant to be converted, providing the moment in time
     * @param zone the time zone identifier to be used for the conversion
     * @since 1.0.0
     */
    constructor(instant: Instant, zone: ZoneIdent) : this(from(instant.atZone(zone.zoneId)))
    /**
     * Constructs a new instance using a given `Instant` and `ZoneId`.
     * The `Instant` represents a specific moment in time, and the `ZoneId` specifies the time zone.
     *
     * @param instant The time instant to initialize the instance.
     * @param zone The time zone to associate with the instant.
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(instant: kotlin.time.Instant, zone: ZoneId) : this(from(instant.toJavaInstant().atZone(zone)))
    /**
     * Secondary constructor to initialize an instance from a given `Instant` and `ZoneIdent`.
     *
     * @param instant The `Instant` object representing a specific point in time.
     * @param zone The `ZoneIdent` representing the time-zone information.
     * @since 1.0.0
     */
    @OptIn(ExperimentalTime::class)
    constructor(instant: kotlin.time.Instant, zone: ZoneIdent) : this(from(instant.toJavaInstant().atZone(zone.zoneId)))

    /**
     * Constructs an instance initializing it to the current time by default.
     * This constructor delegates to another constructor with the current time.
     *
     * @since 1.0.0
     */
    constructor() : this(now())
    /**
     * Secondary constructor that initializes the instance using the current time
     * obtained from the provided clock.
     *
     * @param clock The clock used to retrieve the current time.
     * @since 1.0.0
     */
    constructor(clock: Clock) : this(now(clock))
    /**
     * Constructs an instance of the class using the current date and time
     * from the specified time zone.
     *
     * @param zone The time zone from which the current date and time will be retrieved.
     * @since 1.0.0
     */
    constructor(zone: ZoneId) : this(now(zone))
    /**
     * Constructs an instance using the current time in the specified time zone.
     *
     * @param zone the time zone to extract the current time from
     * @since 1.0.0
     */
    constructor(zone: ZoneIdent) : this(now(zone))

    /**
     * Private secondary constructor for creating an instance using a `Week` object.
     * Calls the primary constructor with the first day of the provided `Week`.
     *
     * @param week The week object from which the first day will be extracted.
     * @since 1.0.0
     */
    private constructor(week: Week) : this(week.firstDay)

    init {
        expect(firstDay.dayOfWeek == DayOfWeek.MONDAY) { "The first day of week must be a Monday" }
    }

    companion object {
        /**
         * Creates a `Week` instance from the provided `Temporal` object.
         *
         * The method extracts the date from the given temporal object and determines the week of the year
         * based on the ISO-8601 standard or equivalent week fields. The resulting `Week` object will
         * represent the week containing the converted date, starting from the first day of the week.
         *
         * @param temporal the temporal object to convert into a `Week`. Must not be null and should be
         *                 compatible with `LocalDate`.
         * @return a `Week` instance representing the week of the provided temporal object.
         * @since 1.0.0
         */
        infix fun from(temporal: Temporal): Week {
            val localDate = LocalDate.from(temporal)
            val weekFields = WeekFields.ISO
            return Week(localDate.with(weekFields.dayOfWeek(), 1))
        }
        /**
         * Constructs a `Week` object using the specified year and week number.
         *
         * @param year the year of the week
         * @param weekNumber the ISO 8601 week number within the specified year
         * @return the `Week` object representing the specified year and week number
         * @since 1.0.0
         */
        private fun of(year: Year, weekNumber: Int): Week {
            val weekFields = WeekFields.ISO
            return Week(LocalDate.of(year.value, 1, 4)
                .with(weekFields.weekOfWeekBasedYear(), weekNumber.toLong())
                .with(weekFields.dayOfWeek(), 1)
            )
        }
        /**
         * Creates an instance using the specified year and week number.
         *
         * @param year the year to represent, must be a valid calendar year.
         * @param weekNumber the week number to represent within the specified year, must be valid as per ISO-8601 standards.
         * @return a new instance created using the provided year and week number.
         * @since 1.0.0
         */
        private fun of(year: Int, weekNumber: Int) = of(year.toYear()(), weekNumber)
        /**
         * Creates a `Week` object based on the provided `YearMonth` and week number.
         *
         * @param yearMonth The `YearMonth` representing the year and month for which the week is created.
         * @param weekNumber The week number within the specified month.
         * @return A `Week` object representing the specified week of the given year and month.
         * @since 1.0.0
         */
        private fun of(yearMonth: YearMonth, weekNumber: Int): Week {
            val weekFields = WeekFields.ISO
            return Week(LocalDate.of(yearMonth.year, yearMonth.month, 1)
                .with(weekFields.weekOfMonth(), weekNumber.toLong())
                .with(weekFields.dayOfWeek(), 1)
            )
        }

        /**
         * Creates an instance of the object using the current date
         * from the system clock in the default time-zone.
         *
         * The method retrieves the current date and constructs the object accordingly.
         * It is designed to work in scenarios where the current date
         * needs to be dynamically fetched and processed.
         *
         * @return A new instance of the object based on the current date.
         * @since 1.0.0
         */
        fun now() = from(LocalDate.now())
        /**
         * Converts the current date provided by the specified clock into a custom date format.
         *
         * @param clock the clock used to obtain the current date
         * @return the custom date representation of the current date
         * @since 1.0.0
         */
        fun now(clock: Clock) = from(LocalDate.now(clock))
        /**
         * Creates an instance of a custom date type for the current date in the specified time zone.
         *
         * @param zone the time zone identifier used to determine the current date
         * @return an instance of the custom date type representing the current date
         * @since 1.0.0
         */
        fun now(zone: ZoneId) = from(LocalDate.now(zone))
        /**
         * Retrieves the current date based on the provided time zone.
         *
         * @param zoneIdent the identifier for the specific time zone to use in determining the current date
         * @return the current date as a custom object derived from the provided time zone
         * @since 1.0.0
         */
        fun now(zoneIdent: ZoneIdent) = from(LocalDate.now(zoneIdent.zoneId))

        /**
         * Returns the first week of the current month represented by the receiver [YearMonth].
         *
         * The first week is evaluated based on the first day of the month and is returned
         * as a [Week] instance. This provides information about the first aligned week
         * within the specific month and year.
         *
         * @receiver The [YearMonth] instance representing the year and month from which
         * the first week should be determined.
         * @return A [Week] instance representing the first week of the specified month.
         *
         * @since 1.0.0
         */
        val YearMonth.firstWeekOfMonth: Week
            get() = from(LocalDate.of(year, month, 1))
        /**
         * Provides the last week of the current month for the given `YearMonth` instance.
         *
         * This property calculates the `Week` representation of the last seven-day period
         * within the month of this `YearMonth`. The calculation respects leap years and
         * retrieves the appropriate `Week` based on the total days in the month.
         *
         * @receiver The `YearMonth` instance used to determine the last week of the month.
         * @return A `Week` instance representing the last week of the month.
         * @since 1.0.0
         */
        val YearMonth.lastWeekOfMonth: Week
            get() = from(LocalDate.of(year, month, month.length(isLeapYear)))
        /**
         * A property that retrieves the first week of a given year.
         *
         * This property represents the week containing the first day of the year
         * as defined by the ISO-8601 calendar system. The week starts on Monday
         * and aligns with the week-based year definition.
         *
         * @receiver A [Year] instance for which the first week is determined.
         * @return The [Week] instance representing the first week of the year.
         * @since 1.0.0
         */
        val Year.firstWeek: Week
            get() = of(this, 1)
        /**
         * Returns the 52nd week of the current year represented by this `Year` instance.
         * This property provides convenient access to the last ISO-8601 week of the given year.
         *
         * The resulting `Week` object corresponds to the ISO week-based calendar system,
         * where weeks are numbered according to the ISO-8601 standard.
         * The last week of the year (week 52) may contain days from the following calendar year.
         *
         * @receiver The `Year` instance on which the last week is being calculated.
         * @return A `Week` object representing the 52nd week of the provided year.
         * @throws DateTimeException if the specified year does not include a 52nd ISO week.
         * @since 1.0.0
         */
        val Year.lastWeek: Week
            get() = of(this, 52)

        /**
         * Converts the current `LocalDateTime` instance to a `Week` object.
         *
         * This function derives the `Week` representation from the given
         * `LocalDateTime` instance, encapsulating the week-related details of
         * the date-time.
         *
         * @receiver the `LocalDateTime` instance to convert.
         * @return the `Week` object representing the week of the `LocalDateTime`.
         *
         * @since 1.0.0
         */
        val LocalDateTime.week
            get() = from(this)
        /**
         * Converts the current `LocalDate` into a `Week` instance based on the date provided.
         *
         * This method extracts the week information from the `LocalDate` object and returns
         * a corresponding `Week` instance that encapsulates this information. It can be used
         * to derive the week representation for any given date in the `LocalDate` format.
         *
         * @receiver The `LocalDate` instance from which the week information is derived.
         * @return A `Week` instance representing the week associated with the given `LocalDate`.
         * @since 1.0.0
         */
        val LocalDate.week
            get() = from(this)
        /**
         * Converts the current `ZonedDateTime` instance to a `Week` representation.
         *
         * This method utilizes the `Week.from` function to derive the week corresponding
         * to the provided `ZonedDateTime`.
         *
         * @receiver the `ZonedDateTime` instance to be converted to `Week`.
         * @return the `Week` associated with the given `ZonedDateTime`.
         * @since 1.0.0
         */
        val ZonedDateTime.week
            get() = from(this)
        /**
         * Converts the OffsetDateTime instance to a Week representation.
         *
         * This method uses the OffsetDateTime object to determine the week-based representation,
         * encapsulated in the Week class, providing a standardized way to work with weeks.
         *
         * @receiver The OffsetDateTime instance to be converted.
         * @return The Week instance corresponding to the provided OffsetDateTime.
         * @since 1.0.0
         */
        val OffsetDateTime.week
            get() = from(this)
        /**
         * Converts the given `Instant` to its corresponding `Week` representation.
         *
         * This function maps the `Instant` to a `Week` object, which represents
         * the week of the year in which the `Instant` occurs. The `Week` class
         * provides a structured representation of a specific week based on
         * the system-defined calendar rules.
         *
         * @receiver the `Instant` to convert to a `Week`
         * @return the `Week` object corresponding to the given `Instant`
         * @throws DateTimeException if the instant cannot be converted to a valid `Week`
         * @since 1.0.0
         */
        val Instant.week
            get() = from(this)
    }

    /**
     * Returns a new instance of `Week` resulting from adding the specified number of weeks
     * to the current week.
     *
     * @param weeks The number of weeks to add, positive or negative.
     * @return A new `Week` instance representing the resulting week.
     * @since 1.0.0
     */
    infix fun plusWeeks(weeks: Long): Week = from(firstDay.plusWeeks(weeks))
    /**
     * Returns a new instance of Week with the specified number of weeks subtracted.
     *
     * @param weeks the number of weeks to subtract, may be negative to add weeks instead
     * @return a new Week with the specified number of weeks subtracted
     * @since 1.0.0
     */
    infix fun minusWeeks(weeks: Long): Week = from(firstDay.minusWeeks(weeks))

    /**
     * Returns a new Week instance that is the result of adding the specified number of months
     * to the current week. The calculation is based on the first day of the current week.
     *
     * @param months The number of months to add. Can be positive or negative.
     * @return A new Week instance shifted by the given number of months.
     * @since 1.0.0
     */
    infix fun plusMonths(months: Long): Week {
        val newFirstDay = firstDay.plusMonths(months)
        return from(newFirstDay)
    }
    /**
     * Returns a new instance of Week obtained by subtracting the specified number of months from the current week.
     *
     * This function adjusts the first day of the current week by decreasing the given number of months, and then returns a new Week instance based on the adjusted first day.
     *
     * @param months the number of months to subtract
     * @return a new Week instance with the adjusted first day
     * @since 1.0.0
     */
    infix fun minusMonths(months: Long): Week {
        val newFirstDay = firstDay.minusMonths(months)
        return from(newFirstDay)
    }

    /**
     * Returns a new `Week` instance with the specified number of years added to the current week.
     *
     * This method calculates the week that is the specified number of years forward
     * from the current week using the first day of the current week as the base date.
     *
     * @param years the number of years to add, positive to move forward in time and
     * negative to move backward
     * @return a `Week` instance representing the week after adding the specified number of years
     * @since 1.0.0
     */
    infix fun plusYears(years: Long): Week {
        val newFirstDay = firstDay.plusYears(years)
        return from(newFirstDay)
    }
    /**
     * Returns a new Week instance with the specified number of years subtracted.
     *
     * This method computes a new Week object by subtracting the given number of years
     * from the first day of the current week. The resulting Week instance is determined
     * based on the adjusted date.
     *
     * @param years the number of years to subtract from the current Week's first day
     * @return a new Week object representing the week after subtracting the specified number of years
     * @since 1.0.0
     */
    infix fun minusYears(years: Long): Week {
        val newFirstDay = firstDay.minusYears(years)
        return from(newFirstDay)
    }

    /**
     * Adds the specified number of weeks to this instance and returns the result.
     *
     * The input is treated as a count of weeks to be added. Negative values can be used
     * to subtract weeks.
     *
     * @param weeks the number of weeks to add, which can be negative
     * @since 1.0.0
     */
    operator fun plus(weeks: Int) = plusWeeks(weeks.toLong())
    /**
     * Returns a copy of this temporal object with the specified number of weeks added.
     *
     * This method adds the specified number of weeks to the current temporal object
     * and returns the resulting object. The input value can be negative to subtract weeks.
     *
     * @param weeks the number of weeks to add, may be negative
     * @since 1.0.0
     */
    operator fun plus(weeks: Long) = plusWeeks(weeks)
    /**
     * Adds the specified number of weeks to the current value and returns the result.
     *
     * @param weeks the number of weeks to add
     * @return the result after adding the specified weeks
     * @since 1.0.0
     */
    operator fun plus(weeks: Week) = plusWeeks(untilWeek(weeks))
    /**
     * Subtracts the specified number of weeks from the current instance.
     *
     * @param weeks The number of weeks to subtract. A negative value will add weeks instead.
     * @since 1.0.0
     */
    operator fun minus(weeks: Int) = minusWeeks(weeks.toLong())
    /**
     * Subtracts the specified number of weeks from this object.
     *
     * @param weeks The number of weeks to subtract. A positive value decreases the amount of weeks,
     * and a negative value increases the amount of weeks.
     * @since 1.0.0
     */
    operator fun minus(weeks: Long) = minusWeeks(weeks)
    /**
     * Subtracts the given `weeks` value from the current value.
     *
     * @param weeks the amount of weeks to subtract.
     * @since 1.0.0
     */
    operator fun minus(weeks: Week) = minusWeeks(untilWeek(weeks))

    /**
     * Increments the current instance by one week.
     *
     * This operator function adds one week to the current instance and returns the new value.
     *
     * @return a new instance incremented by one week.
     * @since 1.0.0
     */
    operator fun inc() = plusWeeks(1)
    /**
     * Decrements the current temporal value by one week.
     *
     * This operator function provides a shorthand for subtracting one week
     * from the current instance of the temporal object.
     *
     * @return A new instance of the object decreased by one week.
     * @since 1.0.0
     */
    operator fun dec() = minusWeeks(1)

    /**
     * Retrieves a `LocalDate` corresponding to the given day of the week
     * from the list of days within this `Week`.
     *
     * @param dayOfWeek the specific day of the week to search for, represented as a `DayOfWeek`.
     * @return the `LocalDate` matching the specified `dayOfWeek` if found; `null` otherwise.
     * @since 1.0.0
     */
    infix fun getDay(dayOfWeek: DayOfWeek): LocalDate? = days.find { it.dayOfWeek == dayOfWeek }

    /**
     * Checks if the current week instance is before the specified week instance based on their first days.
     *
     * @param other the week to compare against
     * @return `true` if the current week is before the specified week, `false` otherwise
     * @since 1.0.0
     */
    infix fun isefore(other: Week): Boolean = firstDay.isBefore(other.firstDay)

    /**
     * Determines if the current week is after another specified week.
     *
     * @param other the other week to compare against
     * @return `true` if the current week is after the specified week, `false` otherwise
     * @since 1.0.0
     */
    infix fun isAfter(other: Week): Boolean = firstDay.isAfter(other.firstDay)

    /**
     * Calculates the number of weeks from the current week to the specified week.
     *
     * @param other the target week to calculate the number of weeks until
     * @return the number of weeks between the current week and the specified week
     * @since 1.0.0
     */
    infix fun untilWeek(other: Week): Long = ChronoUnit.WEEKS.between(firstDay, other.firstDay)

    /**
     * Compares this instance with the specified `other` instance for order.
     *
     * @param other The `Week` instance to compare with this instance.
     * @return A negative integer, zero, or a positive integer as this instance
     *         is less than, equal to, or greater than the specified `other` instance.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Week): Int = firstDay.compareTo(other.firstDay)

    /**
     * Checks if the given temporal object falls within the range defined by `firstDay` and `lastDay`, inclusive.
     *
     * @param temporal the temporal object to check, which will be converted to a LocalDate
     * @return `true` if the temporal is within the range (inclusive), `false` otherwise
     * @since 1.0.0
     */
    operator fun contains(temporal: Temporal): Boolean {
        val localDate = LocalDate.from(temporal)
        return !localDate.isBefore(firstDay) && !localDate.isAfter(lastDay)
    }

    /**
     * Checks if the specified TemporalField is supported by this object.
     *
     * This method evaluates whether the given field is either `ChronoField.ALIGNED_WEEK_OF_YEAR`
     * or `ChronoField.ALIGNED_WEEK_OF_MONTH`.
     *
     * @param field the field to check for support, may be null
     * @return true if the field is supported, false otherwise
     * @since 1.0.0
     */
    override infix fun isSupported(field: TemporalField?) = field == ChronoField.ALIGNED_WEEK_OF_YEAR || field == ChronoField.ALIGNED_WEEK_OF_MONTH

    /**
     * Retrieves the value of the specified temporal field as a long value.
     *
     * This method supports specific fields such as `ChronoField.ALIGNED_WEEK_OF_YEAR` and
     * `ChronoField.ALIGNED_WEEK_OF_MONTH`. For unsupported fields, an exception is thrown.
     *
     * @param field the temporal field for which the value is to be retrieved. Only certain fields
     * are supported, including `ChronoField.ALIGNED_WEEK_OF_YEAR` and `ChronoField.ALIGNED_WEEK_OF_MONTH`.
     * @throws UnsupportedTemporalTypeException if the specified field is not supported.
     * @return the value of the specified field as a long.
     * @since 1.0.0
     */
    override infix fun getLong(field: TemporalField?) = when (field) {
        ChronoField.ALIGNED_WEEK_OF_YEAR -> alignedWeekOfYearOfFirstDay.toLong()
        ChronoField.ALIGNED_WEEK_OF_MONTH -> alignedWeekOfMonthOfFirstDay.toLong()
        else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
    }

    /**
     * Compares this object with the specified object for equality.
     *
     * Determines whether the provided object is equal to the current instance
     * based on their class type and property values.
     *
     * @param other the object to compare with this instance for equality
     * @return true if the specified object is equal to this object, false otherwise
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Week

        @Suppress("IDENTITY_SENSITIVE_OPERATIONS_WITH_VALUE_TYPE")
        return firstDay == other.firstDay
    }

    /**
     * Generates a hash code for the Week instance based on the `firstDay` property.
     *
     * @return the hash code value as an integer.
     * @since 1.0.0
     */
    override fun hashCode(): Int = firstDay.hashCode()

    /**
     * Returns a string representation of the object.
     *
     * This method formats the object as a string in the pattern "YYYY-Www", where `YYYY` represents
     * the year and `Www` represents the week of the year, zero-padded to two digits.
     *
     * @return the string representation of the object in the pattern "YYYY-Www".
     * @since 1.0.0
     */
    override fun toString(): String = "${year.value}-W${"%02d".format(weekOfYear)}"

    /**
     * Converts the current object properties into a map where keys represent property names
     * and values represent the corresponding property values.
     *
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "year" to year,
        "month" to month,
        "weekOfYear" to weekOfYear,
        "weekOfMonth" to weekOfMonth,
        "firstDay" to firstDay,
        "lastDay" to lastDay,
        "days" to days.toList(),
        "days" to days,
        "daysRange" to daysRange,
    )

    /**
     * Retrieves the value associated with a property name from a mapped data structure.
     *
     * - `firstDay` - TYPE: [LocalDate]
     * - `lastDay` - TYPE: [LocalDate]
     * - `weekOfYear` - TYPE: [Int]
     * - `weekOfMonth` - TYPE: [Int]
     * - `year` - TYPE: [Year]
     * - `month` - TYPE: `Pair<Month, Month>`
     * - `days` - TYPE: `List<LocalDate>`
     * - `daysRange` - TYPE: `ClosedRange<LocalDate>`
     *
     * @param thisRef the reference to the object on which the property is being called; can be null.
     * @param property the metadata of the property whose value is being retrieved.
     * @return the value associated with the property name in the mapped data structure.
     * @since 1.0.0
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name)
}