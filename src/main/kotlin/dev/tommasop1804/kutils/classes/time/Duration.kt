package dev.tommasop1804.kutils.classes.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.LocalDateTime
import dev.tommasop1804.kutils.RiskyApproximationOfTemporal
import dev.tommasop1804.kutils.before
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.invoke
import dev.tommasop1804.kutils.isNull
import dev.tommasop1804.kutils.validate
import dev.tommasop1804.kutils.validateInputFormat
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
import java.time.chrono.ChronoPeriod
import java.time.chrono.Chronology
import java.time.chrono.IsoChronology
import java.time.temporal.*
import kotlin.reflect.KProperty
import kotlin.text.isBlank
import kotlin.text.isEmpty
import kotlin.text.startsWith
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.DurationUnit

/**
 * Represents a duration of time, defined in terms of years, months, days, hours, minutes, seconds, and nanoseconds.
 *
 * This class provides a variety of methods to perform calculations, including addition, subtraction, and truncation,
 * as well as methods to retrieve the duration converted to specific time units.
 *
 * @property years The number of years in this duration.
 * @property months The number of months in this duration.
 * @property days The number of days in this duration.
 * @property hours The number of hours in this duration.
 * @property minutes The number of minutes in this duration.
 * @property seconds The number of seconds in this duration.
 * @property nanos The number of nanoseconds in this duration.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Duration.Companion.Serializer::class)
@JsonDeserialize(using = Duration.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Duration.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Duration.Companion.OldDeserializer::class)
@Suppress("unused", "LocalVariableName", "kutils_getorthrow_as_invoke", "contains_as_in_operator", "kutils_substring_as_int_invoke")
open class Duration (years: Number = 0, months: Number = 0, weeks: Number = 0, days: Number = 0, hours: Number = 0, minutes: Number = 0, seconds: Number = 0, nanos: Number = 0) :
    ChronoPeriod, TemporalAmount, Comparable<Duration>, Serializable
{
    /**
     * Represents the number of years in the duration.
     *
     * This property indicates the component of the duration that expresses the count of years.
     *
     *
     * @since 1.0.0
     */
    private val years: Long
    /**
     * Represents the number of months in this [Duration].
     *
     * This value contributes to the length of the duration in terms of months. It can be combined with
     * other components such as years, days, hours, minutes, seconds, and nanoseconds to form the total
     * duration.
     *
     * The value may be positive, negative, or zero depending on the context in which the duration is used.
     *
     *
     * @since 1.0.0
     */
    private val months: Long
    /**
     * Represents the number of whole days in the duration.
     *
     * This property provides access to the days component of a duration instance.
     * It can be used to retrieve or manipulate the specific day count in various operations
     * related to temporal calculations.
     *
     *
     * @return The number of days as a [Long].
     * @since 1.0.0
     */
    private val days: Long
    /**
     * Represents the number of hours in the [Duration].
     * This value reflects the total count of hours stored in the current duration instance.
     *
     *
     * @since 1.0.0
     */
    private val hours: Long
    /**
     * Represents the number of minutes in the duration.
     *
     * This property specifies the amount of time in minutes that this `Duration` instance holds.
     * The value is a signed long, where positive values indicate a forward duration
     * and negative values indicate a backward duration.
     *
     * This property can be used to retrieve the number of minutes or to define a custom duration
     * by using relevant modification methods such as `withMinutes`, `plusMinutes`, or `minusMinutes`.
     *
     *
     * @since 1.0.0
     */
    private val minutes: Long
    /**
     * Represents the number of seconds in this duration.
     *
     * This property provides the total count of seconds as a [Long], which, along with
     * nanoseconds, forms the basis for a precise representation of this duration of time.
     *
     * The [seconds] value may be negative if the duration represents a negative time span.
     *
     *
     * @since 1.0.0
     */
    private val seconds: Long
    /**
     * Represents the nanoseconds part of this duration. This value is always within the range from 0 to 999,999,999
     * and is used alongside other time components such as seconds to construct the total duration represented by the instance.
     *
     * This property is immutable and forms part of the overall duration representation for more precise temporal calculations.
     *
     * 
     * @since 1.0.0
     */
    private val nanos: Long

    /**
     * A computed property that checks if the current `Duration` instance represents a zero duration.
     * The property evaluates to `true` if the `Duration` is equal to `Duration.ZERO`, otherwise `false`.
     *
     * @return `true` if the `Duration` is zero, `false` otherwise.
     * @since 1.0.0
     */
    val isZero
        @JvmName("isZeroProperty")
        get() = this == Duration()

    /**
     * Private secondary constructor that initializes an instance using a [Duration] object.
     * Assigns values from the [Duration] components such as years, months, days, hours, minutes, seconds, and nanoseconds
     * to the corresponding fields in the primary constructor.
     *
     * @param duration The [Duration] object containing the time components used for initialization.
     * @since 1.0.0
     */
    private constructor(duration: Duration) : this(duration.years, duration.months, days = duration.days, hours = duration.hours, minutes = duration.minutes, seconds = duration.seconds, nanos = duration.nanos)

    /**
     * Constructs a Duration object by calculating the temporal amount between two temporal instances.
     *
     * The resulting duration represents the exact amount of time that occurs between the provided
     * start and end temporals.
     *
     * @param start the starting temporal object
     * @param end the ending temporal object
     * @since 1.0.0
     */
    constructor(start: Temporal, end: Temporal) : this(between(start, end))

    /**
     * Constructs an instance using a given character sequence.
     *
     * @param cs The character sequence to be parsed and used for initialization.
     * @since 1.0.0
     */
    constructor(cs: CharSequence) : this(parse(cs)())

    init {
        //OVERFLOW - NO DAYS
        var _nanos = nanos.toLong()
        var _seconds = seconds.toDouble()
        var _minutes = minutes.toDouble()
        var _hours = hours.toDouble()
        var _days = days.toDouble()
        var _months = months.toDouble()
        var _years = years.toDouble()

        _days += weeks.toDouble() * 7

        val yearFraction = _years - _years.toLong()
        _years = _years.toLong().toDouble()
        _months += yearFraction * 12

        val monthFraction = _months - _months.toLong()
        _months = _months.toLong().toDouble()
        _days += monthFraction * 30

        val dayFraction = _days - _days.toLong()
        _days = _days.toLong().toDouble()
        _hours += dayFraction * 24

        val hourFraction = _hours - _hours.toLong()
        _hours = _hours.toLong().toDouble()
        _minutes += hourFraction * 60

        val minuteFraction = _minutes - _minutes.toLong()
        _minutes = _minutes.toLong().toDouble()
        _seconds += minuteFraction * 60

        val secondFraction = _seconds - _seconds.toLong()
        _seconds = _seconds.toLong().toDouble()
        _nanos += (secondFraction * 1_000_000_000).toLong()
        
        _seconds += _nanos / 1_000_000_000
        _nanos %= 1_000_000_000

        _minutes += _seconds.toLong() / 60
        _seconds = (_seconds.toLong() % 60).toDouble()

        _hours += _minutes.toLong() / 60
        _minutes = (_minutes.toLong() % 60).toDouble()

        _days += _hours.toLong() / 24
        _hours = (_hours.toLong() % 24).toDouble()

        _years += _months.toLong() / 12
        _months = (_months.toLong() % 12).toDouble()
        
        this.years = _years.toLong()
        this.months = _months.toLong()
        this.days = _days.toLong()
        this.hours = _hours.toLong()
        this.minutes = _minutes.toLong()
        this.seconds = _seconds.toLong()
        this.nanos = _nanos
    }
    
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
         * Calculates the precise duration between the given start timestamp and the timestamp obtained
         * by adding the specified years, months, days, hours, minutes, seconds, and nanoseconds to it.
         *
         * @param years The number of years to add to the start timestamp. Default is 0.
         * @param months The number of months to add to the start timestamp. Default is 0.
         * @param weeks The number of the weeks to add to the start timestamp. Default is 0.
         * @param days The number of days to add to the start timestamp. Default is 0.
         * @param hours The number of hours to add to the start timestamp. Default is 0.
         * @param minutes The number of minutes to add to the start timestamp. Default is 0.
         * @param seconds The number of seconds to add to the start timestamp. Default is 0.
         * @param nanos The number of nanoseconds to add to the start timestamp. Default is 0.
         * @param startTs The starting timestamp for the duration calculation. Defaults to the current `LocalDateTime`.
         * @return The precise duration
         * @since 1.0.0
         */
        fun precise(years: Long = 0, months: Long = 0, weeks: Long = 0, days: Long = 0, hours: Long = 0, minutes: Long = 0, seconds: Long = 0, nanos: Long = 0, startTs: Temporal = LocalDateTime()): Duration {
            val start = LocalDateTime.from(startTs)
            validate(nanos in 0..999999999) { "Nanoseconds must be in the range of 0 to 999999999" }
            validate(seconds in 0..59) { "Seconds must be in the range of 0 to 59" }
            validate(minutes in 0..59) { "Minutes must be in the range of 0 to 59" }
            validate(hours in 0..23) { "Hours must be in the range of 0 to 23" }
            validate(days in 0..start.month.length(Year.of(start.year).isLeap)) { "Days must be in the range of 0 to 28, 29, 30 or 31 (depending on the month and year)" }
            validate(months in 0..11) { "Months must be in the range of 0 to 11" }
            validate(years in 0..Int.MAX_VALUE) { "Years must be in the range of 0 to 2,147,483,647" }
            return between(start,
                start.plusYears(years).plusMonths(months).plusWeeks(weeks).plusDays(days).plusHours(hours)
                    .plusMinutes(minutes).plusSeconds(seconds).plusNanos(nanos)
            )
        }
        
        /**
         * Creates a new instance of `Duration` based on the specified temporal amount.
         *
         * This method iterates through the units of the given temporal amount
         * and adds the respective amounts to the resulting duration.
         *
         * 
         * @param amount The temporal amount specifying the units and values to be added to the resulting duration.
         * @return A new `Duration` instance that represents the combined values of the provided temporal amount.
         * @since 1.0.0
         */
        infix fun from(amount: TemporalAmount): Duration {
            var duration = Duration()
            for (unit in amount.units)
                duration = duration.plus(amount.get(unit), unit)
            return duration
        }
        
        /**
         * Parses the input string to calculate a time-based duration.
         *
         * The input string should adhere to the ISO-8601 duration format, starting with 'P'.
         * It may include designators for years, months, days, hours, minutes, seconds, and fractions of seconds.
         * Negative durations are supported by using a '-' prefix before 'P'.
         *
         * @param a the input string representing the duration in ISO-8601 format.
         *          For example, "P1Y2M3DT4H5M6.789S" would represent a duration of
         *          1 year, 2 months, 3 days, 4 hours, 5 minutes, and 6.789 seconds.
         * @return a [Result] containing the calculated [Duration] if parsing is successful,
         *         or an error if the input string is invalid.
         * @throws MalformedInputException if the input string does not conform to the ISO-8601 duration format.
         * @since 1.0.0
         */
        infix fun parse(a: CharSequence) = runCatching {
            var duration = Duration()
            var text = a
            if (text.isBlank()) duration
            else {

                val negative = text.startsWith("-")

                validateInputFormat(!((text.contains("H") || text.contains("S")) && !text.contains("T"))) { "Text must contain 'T' if it contains 'H' or 'S'" }
                if (text.startsWith("P") || text.startsWith("-P")) {
                    text = if (text.startsWith("-P")) text.substring(2) else text.substring(1)
                    if (text.contains("Y")) {
                        val y = (text before "Y").toLong()
                        duration = duration.plusYears(if (negative) -y else y)
                        text = text.substring(text.indexOf("Y") + 1)
                    }
                    if (if (text.contains("T")) (text before "T")
                            .contains("M") else text.contains("M")
                    ) {
                        val m = (text before "M").toLong()
                        duration = duration.plusMonths(if (negative) -m else m)
                        text = text.substring(text.indexOf("M") + 1)
                    }
                    if (text.contains("W")) {
                        val w = (text before "W").toLong()
                        duration = duration.plusDays(if (negative) (-w * 7) else (w * 7))
                        text = text.substring(text.indexOf("W") + 1)
                    }
                    if (text.contains("D")) {
                        val d = (text before "D").toLong()
                        duration = duration.plusDays(if (negative) -d else d)
                        text = text.substring(text.indexOf("D") + 1)
                    }
                    if (text.contains("T")) {
                        text = text.substring(text.indexOf("T") + 1)
                        if (text.contains("H")) {
                            val h = (text before "H").toLong()
                            duration = duration.plusHours(if (negative) -h else h)
                            text = text.substring(text.indexOf("H") + 1)
                        }
                        if (text.contains("M")) {
                            val m = (text before "M").toLong()
                            duration = duration.plusMinutes(if (negative) -m else m)
                            text = text.substring(text.indexOf("M") + 1)
                        }
                        if (text.contains("S")) {
                            if (text.contains(".")) {
                                val s = if ((text before ".").isEmpty()) 0 else (text before ".").toLong()
                                duration = duration.plusSeconds(if (negative) -s else s)
                                text = text.substring(text.indexOf(".") + 1)
                                val n = if (text.isEmpty()) 0 else (text before "S")
                                    .replace("0*$".toRegex(), "").toLong()
                                val missingZeros = 9 - n.toString().length
                                val nanosWithZeros = StringBuilder(n.toString())
                                for (i in 0..<missingZeros) {
                                    nanosWithZeros.append("0")
                                }
                                duration = duration.plusNanos(
                                    if (negative) -nanosWithZeros.toString().toLong() else nanosWithZeros.toString()
                                        .toLong()
                                )
                            } else {
                                val n = (text before "S").toLong()
                                duration = duration.plusSeconds(if (negative) -n else n)
                            }
                        }
                    }
                } else throw MalformedInputException("Text must start with 'P'")
                duration
            }
        }

        /**
         * Calculates the duration between this temporal object and the specified end temporal object.
         *
         * This method computes the duration as a `Duration` object, representing the amount of time in terms of seconds and nanoseconds
         * between the two temporal objects.
         *
         * @param end the temporal object to calculate the duration to
         * @return the duration between this temporal object and the provided end temporal object
         * @since 1.0.0
         */
        infix fun Temporal.durationTo(end: Temporal): Duration = between(this, end)

        /**
         * Converts the current number to a `Duration` object based on the specified [unit].
         * This function interprets the number in the context of the given [DurationUnit].
         *
         * @receiver The number to convert.
         * @param unit The [DurationUnit] in which the number should be interpreted (e.g., nanoseconds, seconds, days).
         * @since 1.0.0
         */
        infix fun Number.toDurationOf(unit: DurationUnit) = when(unit) {
            DurationUnit.NANOSECONDS -> Duration(nanos = this)
            DurationUnit.MICROSECONDS -> Duration(nanos = toDouble() * 1000)
            DurationUnit.MILLISECONDS -> Duration(nanos = toDouble() * 1_000_000)
            DurationUnit.SECONDS -> Duration(seconds = this)
            DurationUnit.MINUTES -> Duration(minutes = this)
            DurationUnit.HOURS -> Duration(hours = this)
            DurationUnit.DAYS -> Duration(days = this)
        }

        /**
         * Converts the current number to a `Duration` instance based on the specified temporal unit.
         *
         * @receiver The number to convert.
         * @param unit The temporal unit representing the time measurement (e.g., years, months, days, etc.).
         * @throws UnsupportedTemporalTypeException if the provided temporal unit is not supported by this method.
         * @since 1.0.0
         */
        infix fun Number.toDurationOf(unit: TemporalUnit) = when(unit) {
            ChronoUnit.YEARS -> Duration(years = this)
            ChronoUnit.MONTHS -> Duration(months = this)
            ChronoUnit.WEEKS -> Duration(weeks = this)
            ChronoUnit.DAYS -> Duration(days = this)
            ChronoUnit.HOURS -> Duration(hours = this)
            ChronoUnit.MINUTES -> Duration(minutes = this)
            ChronoUnit.SECONDS -> Duration(seconds = this)
            ChronoUnit.MILLIS -> Duration(nanos = toDouble() * 1_000_000)
            ChronoUnit.MICROS -> Duration(nanos = toDouble() * 1000)
            ChronoUnit.NANOS -> Duration(nanos = toDouble())
            else -> throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
        }

        /**
         * Converts the number instance to a Duration object representing the equivalent number of years.
         *
         * This method treats the number as the count of full years to create a Duration instance.
         * It can be used for converting numeric values into meaningful duration representations.
         *
         * @receiver The number to be interpreted as years.
         * @return A Duration instance representing the given number of years.
         * @since 1.0.0
         */
        fun Number.asYearsOfDuration() = Duration(years = this)
        /**
         * Converts the calling [Number] instance into a [Duration] instance represented as months.
         *
         * The value of the calling number represents the quantity of months
         * which will be used to construct the [Duration] object.
         *
         * @receiver Number The number of months to form a [Duration].
         * @return A [Duration] instance with the specified months.
         * @since 1.0.0
         */
        fun Number.asMonthsOfDuration() = Duration(months = this)
        /**
         * Converts the current numeric value to a `Duration` object representing the equivalent number of weeks.
         *
         * @receiver The numeric value to be converted, expressed as a `Number`.
         * @return A `Duration` object that represents the number of weeks based on the given numeric value.
         * @since 1.0.0
         */
        fun Number.asWeeksOfDuration() = Duration(weeks = this)
        /**
         * Converts the given number to a duration expressed in days.
         *
         * This extension function interprets the number as a number of days and 
         * creates a corresponding [Duration] object.
         *
         * @receiver Number The number representing the days to be converted.
         * @return A [Duration] instance representing the specified number of days.
         * @since 1.0.0
         */
        fun Number.asDaysOfDuration() = Duration(days = this)
        /**
         * Converts the current [Number] value to a [Duration] in terms of hours.
         * The [Number] is interpreted as the total number of hours and is used to create a [Duration] instance.
         *
         * @receiver The numeric value representing the number of hours.
         * @return A [Duration] instance representing the specified number of hours.
         *
         * @since 1.0.0
         */
        fun Number.asHoursOfDuration() = Duration(hours = this)
        /**
         * Converts a number to a `Duration` object where the number represents the value in minutes.
         *
         * This function interprets the number as the length of time in minutes and returns a `Duration`
         * object with the specified duration. Useful for constructing durations directly based on 
         * numeric values.
         *
         * @receiver the numeric value to be converted to a duration in minutes.
         * @return a `Duration` object representing the duration of the given number in minutes.
         * @since 1.0.0
         */
        fun Number.asMinutesOfDuration() = Duration(minutes = this)
        /**
         * Converts a [Number] to a [Duration] object representing seconds.
         * 
         * This method interprets the numerical value of the invoking [Number] 
         * instance as a duration in seconds and constructs a [Duration] object.
         * It simplifies the process of creating a [Duration] by directly using 
         * the numerical value as the seconds' parameter.
         *
         * @receiver [Number] The number to be converted into a seconds-based duration.
         * @return A [Duration] object representing the duration in seconds.
         * @since 1.0.0
         */
        fun Number.asSecondsOfDuration() = Duration(seconds = this)
        /**
         * Converts this [Number] into a [Duration] object representing the equivalent duration in milliseconds.
         * The conversion assumes the [Number] represents a floating-point or integer value 
         * of milliseconds, which is then converted into the corresponding nanoseconds.
         *
         * @receiver The [Number] value to be converted into milliseconds duration.
         * @return A [Duration] object with the value in nanoseconds derived from this [Number].
         * @since 1.0.0
         */
        fun Number.asMillisOfDuration() = Duration(nanos = toDouble() * 1_000_000)
        /**
         * Converts the numeric value to a Duration expressed in microseconds.
         * This method interprets the number as a count of microseconds and converts it
         * to an equivalent Duration representation in nanoseconds.
         *
         * @receiver The numeric value to be converted to Duration in microseconds.
         * @return A Duration object representing the specified time in microseconds.
         * @since 1.0.0
         */
        fun Number.asMicrosOfDuration() = Duration(nanos = toDouble() * 1000)
        /**
         * Converts the current number to a `Duration` instance representing nanoseconds.
         *
         * This function interprets the number as a quantity of nanoseconds and returns
         * a `Duration` object with that value.
         *
         * @receiver The number to be converted into a duration in nanoseconds.
         * @return A `Duration` instance representing the nanoseconds equivalent of the receiver number.
         * @since 1.0.0
         */
        fun Number.asNanosOfDuration() = Duration(nanos = this)

        /**
         * Calculates the duration between two temporal objects, where the start is inclusive and the end is exclusive.
         *
         * The method determines the duration by accounting for the differences in years, months, days, hours,
         * minutes, seconds, and nanoseconds between the two temporal objects.
         *
         * @param startInclusive the starting temporal object, inclusive.
         * @param endExclusive the ending temporal object, exclusive.
         * @return the duration between the two temporal objects as a [Duration].
         * @since 1.0.0
         */
        private fun between(startInclusive: Temporal, endExclusive: Temporal): Duration {
            var _startInclusive = startInclusive
            var _endExclusive = endExclusive
            if (_startInclusive is LocalTime && _endExclusive is LocalTime) {
                return Duration(nanos = startInclusive.until(_endExclusive, ChronoUnit.NANOS))
            }
            if (_startInclusive is LocalMonthDayTime && _endExclusive !is LocalMonthDayTime) _startInclusive =
                _startInclusive.toLocalDateTime()
            if (_endExclusive is LocalMonthDayTime && _startInclusive !is LocalMonthDayTime) _endExclusive =
                _endExclusive.toLocalDateTime()
            if (_startInclusive is YearMonth && _endExclusive !is YearMonth) _endExclusive =
                YearMonth.from(_endExclusive)
            if (_endExclusive is YearMonth && _startInclusive !is YearMonth) _startInclusive =
                YearMonth.from(_startInclusive)
            if (_startInclusive is LocalDate) _startInclusive = LocalDateTime(_startInclusive,LocalTime.MIDNIGHT)
            if (_endExclusive is LocalDate) _endExclusive = LocalDateTime(_endExclusive,LocalTime.MIDNIGHT)

            val years: Long = _startInclusive.until(_endExclusive, ChronoUnit.YEARS)
            _startInclusive = _startInclusive.plus(years, ChronoUnit.YEARS)

            val months: Long = _startInclusive.until(_endExclusive, ChronoUnit.MONTHS)
            _startInclusive = _startInclusive.plus(months, ChronoUnit.MONTHS)

            var days: Long = 0
            if (_startInclusive !is YearMonth && _endExclusive !is YearMonth) {
                days = _startInclusive.until(_endExclusive, ChronoUnit.DAYS)
                _startInclusive = _startInclusive.plus(days, ChronoUnit.DAYS)
            }

            var hours: Long = 0
            var minutes: Long = 0
            var seconds: Long = 0
            var nanos: Long = 0
            if (_startInclusive !is YearMonth && _endExclusive !is YearMonth) {
                hours = _startInclusive.until(_endExclusive, ChronoUnit.HOURS)
                _startInclusive = _startInclusive.plus(hours, ChronoUnit.HOURS)

                minutes = _startInclusive.until(_endExclusive, ChronoUnit.MINUTES)
                _startInclusive = _startInclusive.plus(minutes, ChronoUnit.MINUTES)

                seconds = _startInclusive.until(_endExclusive, ChronoUnit.SECONDS)
                _startInclusive = _startInclusive.plus(seconds, ChronoUnit.SECONDS)

                nanos = _startInclusive.until(_endExclusive, ChronoUnit.NANOS)
            }

            return Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)
        }

        class Serializer : ValueSerializer<Duration>() {
            override fun serialize(value: Duration, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<Duration>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = parse(p.string).getOrThrow()
        }

        class OldSerializer : JsonSerializer<Duration>() {
            override fun serialize(value: Duration, gen: JsonGenerator, serializers: SerializerProvider) = gen.writeString(value.toString())
        }

        class OldDeserializer : JsonDeserializer<Duration>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Duration = parse(p.text).getOrThrow()
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Duration?, String?> {
            override fun convertToDatabaseColumn(attribute: Duration?) = attribute?.toString()

            override fun convertToEntityAttribute(dbData: String?): Duration? = if (dbData.isNull()) null else parse(dbData).getOrThrow()
        }
    }

    /**
     * Creates a copy of the current `Duration` instance with the specified component values.
     *
     * @param years the number of years for the new `Duration`, defaults to the value of the current instance.
     * @param months the number of months for the new `Duration`, defaults to the value of the current instance.
     * @param days the number of days for the new `Duration`, defaults to the value of the current instance.
     * @param hours the number of hours for the new `Duration`, defaults to the value of the current instance.
     * @param minutes the number of minutes for the new `Duration`, defaults to the value of the current instance.
     * @param seconds the number of seconds for the new `Duration`, defaults to the value of the current instance.
     * @param nanos the number of nanoseconds for the new `Duration`, defaults to the value of the current instance.
     * @return a new `Duration` instance with the specified or default values for its components.
     * @since 1.0.0
     */
    fun copy(
        years: Long = this.years,
        months: Long = this.months,
        days: Long = this.days,
        hours: Long = this.hours,
        minutes: Long = this.minutes,
        seconds: Long = this.seconds,
        nanos: Long = this.nanos
    ): Duration = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Retrieves the duration value converted to specified temporal units.
     *
     * - `years`
     * - `months`
     * - `days`
     * - `hours`
     * - `minutes`
     * - `seconds`
     * - `millis`
     * - `micros`
     * - `nanos`
     *
     * All as [Double]. Not needed to specify.
     *
     * @param thisRef the reference to the object that delegates to this function, can be null.
     * @param property the metadata of the delegated property.
     * @return the value associated with the given property name as a Double.
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = toTotalsMap().getValue(property.name)

    /**
     * Returns a copy of this duration with the specified number of years added.
     *
     * 
     * @param years The number of years to add to the duration.
     * @return A new duration instance with the specified years added.
     * @since 1.0.0
     */
    infix fun plusYears(years: Long): Duration = Duration(this.years + years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a new Duration instance with the specified number of months added to this duration.
     *
     * 
     * @param months The number of months to add.
     * @return A new Duration instance with the specified months added.
     * @since 1.0.0
     */
    infix fun plusMonths(months: Long): Duration = Duration(years, this.months + months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a new `Duration` by adding the specified number of weeks to this duration.
     *
     * @param weeks the number of weeks to add, may be negative
     * @return a new `Duration` based on this duration with the specified weeks added
     * @since 1.0.0
     */
    infix fun plusWeeks(weeks: Long): Duration = plusDays(weeks * 7)

    /**
     * Returns a new `Duration` instance with the specified number of days added to the current duration.
     *
     * 
     * @param days The number of days to add.
     * @return A new `Duration` instance with the added days.
     * @since 1.0.0
     */
    infix fun plusDays(days: Long): Duration = Duration(years, months, days = this.days + days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Adds the specified number of hours to this duration and returns a new `Duration` instance.
     *
     * 
     * @param hours The number of hours to add to the current duration.
     * @return A new `Duration` instance with the added hours.
     * @since 1.0.0
     */
    infix fun plusHours(hours: Long): Duration = Duration(years, months, days = days, hours = this.hours + hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a new `Duration` instance by adding the specified number of minutes to this `Duration`.
     *
     * 
     * @param minutes the number of minutes to be added
     * @return a new `Duration` with the specified minutes added
     * @since 1.0.0
     */
    infix fun plusMinutes(minutes: Long): Duration = Duration(years, months, days = days, hours = hours, minutes = this.minutes + minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a new duration with the specified number of seconds added to this duration.
     *
     * 
     * @param seconds The number of seconds to add. Can be positive or negative.
     * @return A new Duration instance with the updated time.
     * @since 1.0.0
     */
    infix fun plusSeconds(seconds: Long): Duration = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = this.seconds + seconds, nanos = nanos)

    /**
     * Returns a new Duration by adding the specified number of milliseconds to this Duration.
     *
     * The resulting duration is computed by converting the given milliseconds
     * into nanoseconds and adding it to the current duration.
     *
     * @param milliseconds the number of milliseconds to add. Can be positive or negative.
     * @return a new Duration instance with the specified milliseconds added.
     * @since 1.0.0
     */
    infix fun plusMillis(milliseconds: Long): Duration = plusNanos(1_000_000 * milliseconds)

    /**
     * Adds the specified number of microseconds to this duration.
     *
     * The operation converts the given number of microseconds into nanoseconds and applies it to the current duration.
     *
     * @param microseconds the number of microseconds to be added
     * @return a new Duration instance with the added microseconds
     * @since 1.0.0
     */
    infix fun plusMicros(microseconds: Long): Duration = plusNanos(1_000 * microseconds)

    /**
     * Returns a new `Duration` instance with the specified number of nanoseconds added to this duration.
     *
     * The addition does not affect other components of the duration such as years, months, days, hours, minutes, or seconds.
     *
     * 
     * @param nanos The number of nanoseconds to add to this duration. Can be positive, negative, or zero.
     * @return A new `Duration` instance with the adjusted nanoseconds value.
     * @since 1.0.0
     */
    infix fun plusNanos(nanos: Long): Duration = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = this.nanos + nanos)

    /**
     * Adds the given amount of time to this duration for the specified temporal unit.
     * If the temporal unit is not supported, an UnsupportedTemporalTypeException is thrown.
     *
     * 
     * @param amountToAdd The amount of the specified unit to add to this duration.
     * @param unit The temporal unit defining the time measurement to be added.
     * @throws UnsupportedTemporalTypeException if the unit is not supported.
     * @since 1.0.0
     */
    fun plus(amountToAdd: Long, unit: TemporalUnit) = when(unit) {
        ChronoUnit.YEARS -> plusYears(amountToAdd)
        ChronoUnit.MONTHS -> plusMonths(amountToAdd)
        ChronoUnit.WEEKS -> plusWeeks(amountToAdd)
        ChronoUnit.DAYS -> plusDays(amountToAdd)
        ChronoUnit.HOURS -> plusHours(amountToAdd)
        ChronoUnit.MINUTES -> plusMinutes(amountToAdd)
        ChronoUnit.SECONDS -> plusSeconds(amountToAdd)
        ChronoUnit.MILLIS -> plusMillis(amountToAdd)
        ChronoUnit.MICROS -> plusMicros(amountToAdd)
        ChronoUnit.NANOS -> plusNanos(amountToAdd)
        else -> throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
    }

    /**
     * Adds the specified amount and unit to the current temporal object.
     *
     * @param amoutToAddAndUnit a pair containing the amount to add and the temporal unit
     * @since 1.0.0
     */
    operator fun plus(amoutToAddAndUnit: Pair<Long, TemporalUnit>) = amoutToAddAndUnit.run { plus(first, second) }

    /**
     * Adds the specified amount of time to this duration in the given time unit.
     * The addition is performed in the most precise unit available for the given `DurationUnit`.
     *
     * 
     * @param amountToAdd The amount of time to add. May be positive or negative.
     * @param unit The unit of the time to add, defines the granularity of the addition.
     * @since 1.0.0
     */
    fun plus(amountToAdd: Long, unit: DurationUnit) = when(unit) {
        DurationUnit.NANOSECONDS -> plusNanos(amountToAdd)
        DurationUnit.MICROSECONDS -> plusMicros(amountToAdd)
        DurationUnit.MILLISECONDS -> plusMillis(amountToAdd)
        DurationUnit.SECONDS -> plusSeconds(amountToAdd)
        DurationUnit.MINUTES -> plusMinutes(amountToAdd)
        DurationUnit.HOURS -> plusHours(amountToAdd)
        DurationUnit.DAYS -> plusDays(amountToAdd)
    }

    /**
     * Adds the components of a specified `TemporalAmount` to this duration, creating a new `Duration` instance.
     *
     * The resulting duration is calculated by first converting the `TemporalAmount` to a `Duration`
     * and then adding its corresponding components (years, months, days, hours, minutes, seconds, nanoseconds)
     * to the current instance.
     *
     * 
     * @param amount The `TemporalAmount` to be added to this duration.
     * @return A new `Duration` instance representing the sum of the current instance and the specified `TemporalAmount`.
     * @since 1.0.0
     */
    override operator fun plus(amount: TemporalAmount) = from(amount).plusYears(years).plusMonths(months).plusDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds).plusNanos(nanos)

    /**
     * Returns a new `Duration` by subtracting the specified number of years from the current duration.
     *
     * 
     * @param years The number of years to subtract.
     * @return A new `Duration` instance with the specified years subtracted.
     * @since 1.0.0
     */
    infix fun minusYears(years: Long) = Duration(this.years - years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a copy of this duration with the specified number of months subtracted.
     *
     * 
     * @param months The number of months to subtract.
     * @return A new `Duration` with the specified months subtracted.
     * @since 1.0.0
     */
    infix fun minusMonths(months: Long) = Duration(years, this.months - months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a copy of this duration with the specified number of weeks subtracted.
     *
     * The calculation is performed by converting the specified weeks into days
     * (1 week = 7 days) and subtracting the equivalent number of days from the duration.
     *
     * @param weeks the number of weeks to subtract, may be negative to add weeks
     * @return a new Duration instance with the specified weeks subtracted
     * @since 1.0.0
     */
    infix fun minusWeeks(weeks: Long) = minusDays(weeks * 7)

    /**
     * Subtracts the specified number of days from this duration and returns a new instance.
     *
     * 
     * @param days The number of days to subtract.
     * @return A new instance of [Duration] with the specified number of days subtracted.
     * @since 1.0.0
     */
    infix fun minusDays(days: Long) = Duration(years, months, days = this.days - days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Subtracts the specified number of hours from the current duration.
     *
     * 
     * @param hours The number of hours to subtract.
     * @return A new `Duration` instance with the specified number of hours subtracted.
     * @since 1.0.0
     */
    infix fun minusHours(hours: Long) = Duration(years, months, days = days, hours = this.hours - hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a copy of this duration with the specified number of minutes subtracted.
     *
     * 
     * @param minutes The number of minutes to subtract from the duration.
     * @return A new `Duration` instance with the specified minutes subtracted.
     * @since 1.0.0
     */
    infix fun minusMinutes(minutes: Long) = Duration(years, months, days = days, hours = hours, minutes = this.minutes - minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a copy of this duration with the specified number of seconds subtracted.
     *
     * 
     * @param seconds The number of seconds to subtract. Can be positive or negative.
     * @return A new Duration instance with the specified number of seconds subtracted.
     * @since 1.0.0
     */
    infix fun minusSeconds(seconds: Long) = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = this.seconds - seconds, nanos = nanos)

    /**
     * Subtracts the specified amount of milliseconds from the current time representation.
     *
     * This function adjusts the current time by decreasing it by the given number of milliseconds.
     *
     * @param milliseconds the number of milliseconds to subtract
     * @since 1.0.0
     */
    infix fun minusMillis(milliseconds: Long) = minusNanos(1_000_000 * milliseconds)

    /**
     * Subtracts the given number of microseconds from the current duration.
     *
     * The operation converts microseconds to nanoseconds internally
     * by multiplying the input value by 1,000 before subtraction.
     *
     * @param microseconds the number of microseconds to subtract
     * @since 1.0.0
     */
    infix fun minusMicros(microseconds: Long) = minusNanos(1_000 * microseconds)

    /**
     * Returns a copy of this duration with the specified number of nanoseconds subtracted.
     *
     * 
     * @param nanos The number of nanoseconds to subtract.
     * @return A new `Duration` instance with the resulting value after subtracting the specified nanoseconds.
     * @since 1.0.0
     */
    infix fun minusNanos(nanos: Long) = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = this.nanos - nanos)

    /**
     * Subtracts the specified amount from this duration, using the specified temporal unit.
     *
     * 
     * @param amountToSubtract The amount to subtract from the duration.
     * @param unit The unit of the amount to subtract, such as years, months, days, hours, minutes,
     * seconds, or nanoseconds. Only `ChronoUnit` values are supported.
     * @throws UnsupportedTemporalTypeException If the provided unit is not supported.
     * @since 1.0.0
     */
    fun minus(amountToSubtract: Long, unit: TemporalUnit) = when(unit) {
        ChronoUnit.YEARS -> minusYears(amountToSubtract)
        ChronoUnit.MONTHS -> minusMonths(amountToSubtract)
        ChronoUnit.WEEKS -> minusWeeks(amountToSubtract)
        ChronoUnit.DAYS -> minusDays(amountToSubtract)
        ChronoUnit.HOURS -> minusHours(amountToSubtract)
        ChronoUnit.MINUTES -> minusMinutes(amountToSubtract)
        ChronoUnit.SECONDS -> minusSeconds(amountToSubtract)
        ChronoUnit.MILLIS -> minusMillis(amountToSubtract)
        ChronoUnit.MICROS -> minusMicros(amountToSubtract)
        ChronoUnit.NANOS -> minusNanos(amountToSubtract)
        else -> throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
    }

    /**
     * Subtracts the specified amount and unit from the current temporal object.
     *
     * The method takes a pair where the first element represents the amount
     * to subtract, and the second element is the unit of the temporal amount.
     *
     * @param amountToSubtractAndUnit a pair consisting of the amount to subtract as a `Long`,
     * and the temporal unit as a `TemporalUnit`
     * @since 1.0.0
     */
    operator fun minus(amountToSubtractAndUnit: Pair<Long, TemporalUnit>) = amountToSubtractAndUnit.run { minus(first, second) }

    /**
     * Subtracts the specified amount of time in the given unit from this duration.
     *
     * 
     * @param amountToSubtract The amount of the unit to subtract.
     * @param unit The unit of the amount to subtract, represented as a [DurationUnit].
     * @since 1.0.0
     */
    fun minus(amountToSubtract: Long, unit: DurationUnit) = when(unit) {
        DurationUnit.NANOSECONDS -> minusNanos(amountToSubtract)
        DurationUnit.MICROSECONDS -> minusMicros(amountToSubtract)
        DurationUnit.MILLISECONDS -> minusMillis(amountToSubtract)
        DurationUnit.SECONDS -> minusSeconds(amountToSubtract)
        DurationUnit.MINUTES -> minusMinutes(amountToSubtract)
        DurationUnit.HOURS -> minusHours(amountToSubtract)
        DurationUnit.DAYS -> minusDays(amountToSubtract)
    }

    /**
     * Subtracts the specified temporal amount from the current duration.
     *
     * This method creates a new `Duration` by negating the values of the specified temporal amount
     * and applying them to the current instance. The resulting duration represents the difference.
     *
     * @param amount The temporal amount to be subtracted from the current duration.
     * @return A new `Duration` instance representing the result of subtracting the specified amount.
     * 
     * @since 1.0.0
     */
    @OptIn(RiskyApproximationOfTemporal::class)
    override operator fun minus(amount: TemporalAmount): Duration {
        if (amount is Duration) return Duration(nanos = toNanos().minus(amount.toNanos()))
        return minusNanos(amount.get(ChronoUnit.NANOS))
            .minusSeconds(amount.get(ChronoUnit.SECONDS))
            .minusMinutes(amount.get(ChronoUnit.MINUTES))
            .minusHours(amount.get(ChronoUnit.HOURS))
            .minusDays(amount.get(ChronoUnit.DAYS))
            .minusMonths(amount.get(ChronoUnit.MONTHS))
            .minusYears(amount.get(ChronoUnit.YEARS))
    }

    /**
     * Multiplies the current instance by the provided scalar value.
     *
     * @param scalar The scalar value to multiply with this instance.
     * @since 1.0.0
     */
    operator fun times(scalar: Long): Duration {
        if (this == Duration() || scalar == 1L) return this
        return Duration(years = years * scalar, months = months * scalar, days = days * scalar, hours = hours * scalar, minutes = minutes * scalar, seconds = seconds * scalar, nanos = nanos * scalar)
    }

    /**
     * Returns a new ChronoPeriod where the specified scalar multiplies all the time units of this duration.
     *
     * @param scalar the multiplier to apply to all time units of the current ChronoPeriod
     * @return a new ChronoPeriod with all its time units multiplied by the given scalar
     * @since 1.0.0
     */
    override infix fun multipliedBy(scalar: Int): ChronoPeriod = times(scalar.toLong()) as ChronoPeriod

    /**
     * Returns a normalized instance of the current `ChronoPeriod`.
     * Normalization ensures that the period is in its most standard form
     * based on the rules defined by the specific chronology.
     *
     * @return the normalized `ChronoPeriod` instance.
     * @since 1.0.0
     */
    override fun normalized(): ChronoPeriod = this

    /**
     * Operator function that represents the unary minus operation for this object,
     * effectively negating its value by calling the `negated()` function.
     *
     * @return The negated value of this object.
     * @since 1.0.0
     */
    operator fun unaryMinus(): Duration = negated() as Duration

    /**
     * Returns a new `Duration` truncated to the specified `TemporalUnit`.
     *
     * This method removes all units of time smaller than the specified unit, resetting their values to zero.
     *
     * 
     * @param unit The `TemporalUnit` to which the duration should be truncated.
     * Supported units include `ChronoUnit.YEARS`, `ChronoUnit.MONTHS`, `ChronoUnit.DAYS`,
     * `ChronoUnit.HOURS`, `ChronoUnit.MINUTES`, `ChronoUnit.SECONDS`, and `ChronoUnit.NANOS`.
     * @throws UnsupportedTemporalTypeException if the specified unit is not supported for truncation.
     * @since 1.0.0
     */
    infix fun truncatedTo(unit: TemporalUnit) = when(unit) {
        ChronoUnit.YEARS -> Duration(years)
        ChronoUnit.MONTHS -> Duration(years, months)
        ChronoUnit.DAYS -> Duration(years, months, days = days)
        ChronoUnit.HOURS -> Duration(years, months, days = days, hours = hours)
        ChronoUnit.MINUTES -> Duration(years, months, days = days, hours = hours, minutes = minutes)
        ChronoUnit.SECONDS -> Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds)
        ChronoUnit.MILLIS -> Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = (nanos / 1_000_000) * 1000000L)
        ChronoUnit.MICROS -> Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = (nanos / 1_000) * 1000L)
        ChronoUnit.NANOS -> Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)
        else -> throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
    }

    /**
     * Truncates this duration to the specified unit of time. Any finer-grained time units will be set to zero.
     *
     * 
     * @param unit the `DurationUnit` to which this duration is truncated
     * @since 1.0.0
     */
    infix fun truncatedTo(unit: DurationUnit) = when(unit) {
        DurationUnit.NANOSECONDS -> Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)
        DurationUnit.MICROSECONDS -> Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = (nanos / 1000L) * 1000L)
        DurationUnit.MILLISECONDS -> Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = (nanos / 1000000L) * 1000000L)
        DurationUnit.SECONDS -> Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds)
        DurationUnit.MINUTES -> Duration(years, months, days = days, hours = hours, minutes = minutes)
        DurationUnit.HOURS -> Duration(years, months, days = days, hours = hours)
        DurationUnit.DAYS -> Duration(years, months, days = days)
    }

    /**
     * Returns a new `Duration` instance with its years set to the specified value.
     *
     * 
     * @param years The number of years to use for the new `Duration`.
     * @since 1.0.0
     */
    infix fun withYears(years: Long) = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Creates a new Duration instance with the specified number of months,
     * replacing the current month field value while retaining all other fields.
     *
     * 
     * @param months The number of months to set in the new Duration.
     * @return A new Duration instance with the updated number of months.
     * @since 1.0.0
     */
    infix fun withMonths(months: Long) = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a new `Duration` instance with the specified number of days,
     * while retaining the other components of the current duration.
     *
     * 
     * @param days The number of days to set in the new `Duration`.
     * @since 1.0.0
     */
    infix fun withDays(days: Long) = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a new `Duration` instance with the specified number of hours.
     *
     * 
     * @param hours The number of hours to set in the new `Duration`.
     * @since 1.0.0
     */
    infix fun withHours(hours: Long) = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a copy of this Duration instance with the specified number of minutes.
     *
     * @param minutes The number of minutes to set in the resulting Duration instance.
     * @since 1.0.0
     */
    infix fun withMinutes(minutes: Long) = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Creates a new Duration instance with the specified seconds while retaining the other time components.
     *
     * 
     * @param seconds The number of seconds to set in the new Duration instance.
     * @return A new Duration instance with the updated seconds value.
     * @since 1.0.0
     */
    infix fun withSeconds(seconds: Long) = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a new `Duration` instance with the specified number of milliseconds.
     *
     * This method creates a new duration by modifying the milliseconds value of the existing duration.
     * It keeps other time components such as years, months, days, hours, minutes, and seconds unchanged.
     *
     * @param milliseconds the number of milliseconds to be included in the duration
     * @since 1.0.0
     */
    infix fun withMillis(milliseconds: Long) = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = milliseconds * 1_000_000L)

    /**
     * Returns a new Duration instance with the specified number of microseconds.
     *
     * @param microseconds The number of microseconds to include in the Duration.
     * @since 1.0.0
     */
    infix fun withMicros(microseconds: Long) = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = microseconds * 1_000L)

    /**
     * Creates a new `Duration` instance with the specified nanoseconds component,
     * keeping all other time components (years, months, days, hours, minutes, and seconds) unchanged.
     *
     * 
     * except for the nanoseconds.
     * @param nanos The new nanoseconds value to set in the resulting `Duration` instance.
     * @return A new `Duration` instance with the updated nanoseconds value.
     * @since 1.0.0
     */
    infix fun withNanos(nanos: Long) = Duration(years, months, days = days, hours = hours, minutes = minutes, seconds = seconds, nanos = nanos)

    /**
     * Returns a new `Duration` instance with the specified temporal field updated to the given value.
     * The method supports updating fields such as year, month, day, hour, minute, second, and nanosecond.
     * If the temporal field is not supported, an `UnsupportedTemporalTypeException` is thrown.
     *
     * 
     * @param field The temporal field to update.
     * @param value The new value for the specified field.
     * @throws UnsupportedTemporalTypeException if the specified field is not supported.
     * @return A new `Duration` instance with the updated field.
     * @since 1.0.0
     */
    fun with(field: TemporalField, value: Long) = when(field) {
        ChronoField.YEAR_OF_ERA -> withYears(value)
        ChronoField.MONTH_OF_YEAR -> withMonths(value)
        ChronoField.DAY_OF_MONTH -> withDays(value)
        ChronoField.HOUR_OF_DAY -> withHours(value)
        ChronoField.MINUTE_OF_HOUR -> withMinutes(value)
        ChronoField.SECOND_OF_MINUTE -> withSeconds(value)
        ChronoField.MILLI_OF_SECOND -> withMillis(value)
        ChronoField.MICRO_OF_SECOND -> withMicros(value)
        ChronoField.NANO_OF_SECOND -> withNanos(value)
        else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
    }

    /**
     * Updates the value of a specified field within this temporal object.
     *
     * This infix function applies the provided pair containing a temporal field and its value
     * to update the corresponding field in this temporal object.
     *
     * @param fieldAndValue a pair where the first element is the temporal field to modify
     * and the second element is the value to set for that field
     * @since 1.0.0
     */
    infix fun with(fieldAndValue: Pair<TemporalField, Long>) = fieldAndValue.run { with(first, second) }

    /**
     * Converts the duration represented by this [Duration] instance into its approximate equivalent in years.
     *
     * The calculation accounts for the contribution of years, months, days, hours, minutes, seconds, and nanoseconds
     * by dividing each component by the respective time units in a year. The resulting value represents the total
     * duration in terms of years as a floating-point number.
     *
     * This method is dangerous due to overflow:
     * - Months are calculated as 12 units
     * - Days are calculated as 365 units
     * - Hours are calculated as 8760 units
     * - Minutes are calculated as 525600 units
     * - Seconds are calculated as 31536000 units
     * - Seconds are calculated as 31536000000000 units
     * 
     * @return The approximate equivalent duration in years as a [Double].
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    fun toYears() = years + (months.toDouble() / 12) + (days.toDouble() / 365) + (hours.toDouble() / 8760) + (minutes.toDouble() / 525600L) + (seconds.toDouble() / 31536000L) + (nanos.toDouble() / 31536000000000L)
    /**
     * Converts the duration of the calling object to a number of full years starting from the given temporal object.
     *
     * @param startTs The starting temporal object from which the duration will be measured.
     * @since 1.0.0
     */
    fun toYears(startTs: Temporal) = startTs.until(startTs + this, ChronoUnit.YEARS)

    /**
     * Converts the duration represented by the receiver [Duration] into an approximate value in months.
     *
     * This method is dangerous due to overflow.
     *
     * The conversion takes into account the following components of the duration:
     * - Years, converted to months by multiplying by 12.
     * - Months, directly added to the result.
     * - Days, approximated to months by dividing by 30.
     * - Hours, approximated to months by dividing by 720 (30 days * 24 hours).
     * - Minutes, approximated to months by dividing by 43,200 (30 days * 24 hours * 60 minutes).
     * - Seconds, approximated to months by dividing by 2,592,000 (30 days * 24 hours * 60 minutes * 60 seconds).
     * - Nanoseconds, approximated to months by dividing by 2,592,000,000,000 (30 days * 24 hours * 60 minutes * 60 seconds * 1,000,000,000 nanoseconds).
     *
     * The result is a sum of all these components as a `Double`, representing the total duration in months.
     *
     *
     * @return The total duration represented in months as a [Double].
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    fun toMonths() = years * 12 + months + (days.toDouble() / 30) + (hours.toDouble() / 720) + (minutes.toDouble() / 43200) + (seconds.toDouble() / 2592000L) + (nanos.toDouble() / 2592000000000L)
    /**
     * Calculates the number of months between the current instance and the specified start temporal.
     *
     * @param startTs the starting point as a Temporal instance
     * @return the number of months between the two temporal objects
     * @since 1.0.0
     */
    fun toMonths(startTs: Temporal) = startTs.until(startTs + this, ChronoUnit.MONTHS)

    /**
     * Converts a duration represented in days to an approximate number of weeks.
     * The conversion is performed by dividing the number of days by 7.
     *
     * This method assumes that a week is exactly 7 days, which might not account for
     * variations in calendar systems or leap seconds.
     *
     * @return the duration in weeks as a double value.
     * @since 1.0.0
     */
    @OptIn(RiskyApproximationOfTemporal::class)
    fun toWeeks() = toDays() / 7
    /**
     * Converts the duration specified by the current object to a number of whole weeks.
     *
     * @param startTs the starting temporal object to perform the calculation from
     * @since 1.0.0
     */
    fun toWeeks(startTs: Temporal) = startTs.until(startTs + this, ChronoUnit.WEEKS)

    /**
     * Converts the duration represented by the fields in the receiver object to its
     * equivalent value in whole and fractional days.
     *
     * This method is dangerous due to overflow.
     *
     * The conversion considers the following:
     * - Each year is approximated as 365 days.
     * - Each month is approximated as 30 days.
     * - Hours, minutes, seconds, and nanoseconds are converted to their fractional contribution to a day.
     *
     * 
     * @return The total equivalent duration in days, represented as a `Double`.
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    fun toDays() = years * 365 + months * 30 + days + (hours.toDouble() / 24) + (minutes.toDouble() / 1440) + (seconds.toDouble() / 86400) + (nanos.toDouble() / 86400000000000L)
    /**
     * Converts the given temporal value to days by calculating the difference
     * between the starting temporal value and the time resulting from adding
     * this duration to the starting point.
     *
     * @param startTs the starting temporal value used as the reference point.
     * @since 1.0.0
     */
    fun toDays(startTs: Temporal) = startTs.until(startTs + this, ChronoUnit.DAYS)

    /**
     * Converts the duration represented by the receiver object into hours as a `Double` value.
     * The calculation accounts for all components of the duration: years, months, days, hours,
     * minutes, seconds, and nanoseconds.
     *
     * This method is dangerous due to overlflow.
     *
     * Assumes:
     * - 1 year = 8760 hours
     * - 1 month = 720 hours
     * - 1 day = 24 hours
     * - 1 minute = 1/60 hours
     * - 1 second = 1/3600 hours
     * - 1 nanosecond = 1/36000000000000 hours
     *
     * 
     * @return The total duration in hours as a `Double`.
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    fun toHours() = years * 8760 + months * 720 + days * 24 + hours + (minutes.toDouble() / 60) + (seconds.toDouble() / 3600) + (nanos.toDouble() / 36000000000000L)
    /**
     * Converts the duration represented by this object to the total number of hours.
     *
     * @param startTs the starting temporal instance used as the base for the calculation
     * @since 1.0.0
     */
    fun toHours(startTs: Temporal) = startTs.until(startTs + this, ChronoUnit.HOURS)

    /**
     * Converts the duration represented by the receiver in years, months, days, hours, minutes,
     * seconds, and nanoseconds to a total value expressed in minutes, as a `Double`.
     * Fractions of a minute are calculated from seconds and nanoseconds.
     *
     * This method is dangerous due to overflow.
     *
     * Assumes:
     * - 1 year = 525600 minutes
     * - 1 month = 43200 minutes
     * - 1 day = 1440 minutes
     * - 1 hour = 60 minutes
     * - 1 second = 1/60 minutes
     * - 1 nanos = 1/60000000000000 minutes
     * 
     * @return The total duration in minutes as a `Double`.
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    fun toMinutes() = years * 525600L + months * 43200 + days * 1440 + hours * 60 + minutes + (seconds.toDouble() / 60) + (nanos.toDouble() / 60000000000000L)
    /**
     * Converts the duration represented by the receiver to minutes relative
     * to the provided start temporal value.
     *
     * @param startTs The starting temporal value from which the duration is calculated.
     * @return The duration in minutes from the given start temporal.
     * @since 1.0.0
     */
    fun toMinutes(startTs: Temporal) = startTs.until(startTs + this, ChronoUnit.MINUTES)

    /**
     * Converts the duration represented by the current instance into its total equivalent in seconds as a `Double`.
     *
     * This method is dangerous due to overflow.
     *
     * The calculation takes into account the following fields:
     * - `years`, assuming 1 year is equivalent to 31,536,000 seconds (365 days).
     * - `months`, assuming 1 month is equivalent to 2,592,000 seconds (30 days).
     * - `days`, where 1 day is equivalent to 86,400 seconds.
     * - `hours`, where 1 hour is equivalent to 3,600 seconds.
     * - `minutes`, where 1 minute is equivalent to 60 seconds.
     * - `seconds`, as-is.
     * - `nanos`, converted to its fractional equivalent in seconds.
     *
     * 
     * @return The total duration in seconds, considering all fields combined.
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    fun toSeconds() = years * 31536000L + months * 2592000L + days * 86400 + hours * 3600 + minutes * 60 + seconds + (nanos.toDouble() / 1000000000L)
    /**
     * Converts the current duration to seconds from the given starting temporal point.
     *
     * @param startTs the temporal point from which the duration is calculated.
     * @return the total duration in seconds.
     * @since 1.0.0
     */
    fun toSeconds(startTs: Temporal) = startTs.until(startTs + this, ChronoUnit.SECONDS)

    /**
     * Converts the current time duration to milliseconds by dividing the
     * nanoseconds value returned by `toNanos()` by 1,000,000.
     *
     * Use this method carefully, as it involves integer division and may
     * potentially suffer from overflow limitations if the internal
     * representation surpasses the maximum value for this calculation.
     *
     * @return The duration in milliseconds as a `Long`.
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    fun toMillis() = toNanos() / 1000000.0
    /**
     * Converts the temporal duration to milliseconds by calculating the time difference
     * between the provided start temporal and the duration added to it.
     *
     * @param startTs the starting temporal value used as reference for the calculation
     * @since 1.0.0
     */
    fun toMillis(startTs: Temporal) = startTs.until(startTs + this, ChronoUnit.MILLIS)

    /**
     * Converts the current value to microseconds by dividing the result
     * of `toNanos()` by 1000.
     *
     * Note: Ensure to review the method's bytecode due to potential
     * overflow limitations.
     *
     * @return The equivalent value in microseconds.
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    fun toMicros() = toNanos() / 1000.0
    /**
     * Calculates the duration in microseconds between the given temporal instance `startTs`
     * and the result of adding the duration represented by `this` to `startTs`.
     *
     * @param startTs the starting temporal instance from which the duration is calculated
     * @return the duration in microseconds as a long value
     * @since 1.0.0
     */
    fun toMicros(startTs: Temporal) = startTs.until(startTs + this, ChronoUnit.MICROS)

    /**
     * Converts the duration represented by the current [Duration] instance
     * to its total equivalent in nanoseconds.
     *
     * This method computes the total nanoseconds by aggregating the contributions
     * of the years, months, days, hours, minutes, seconds, and nanoseconds
     * components of the duration.
     *
     * This method is dangerous due to overflow.
     *
     * The calculation takes into account the following fields:
     * - `years`, assuming 1 year is equivalent to 31536000000000000 nanos.
     * - `months`, assuming 1 month is equivalent to 2592000000000000 nanos.
     * - `days`, where 1 day is equivalent to 86400000000000 nanos.
     * - `hours`, where 1 hour is equivalent to 3600000000000 nanos.
     * - `minutes`, where 1 minute is equivalent to 60000000000 nanos.
     * - `seconds`, where 1 second is equivalent to 1000000000 nanos.
     * - `nanos`, as-is.
     *
     * @return the total duration in nanoseconds as a [Long].
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    fun toNanos() = years * 31536000000000000L + months * 2592000000000000L + days * 86400000000000L + hours * 3600000000000L + minutes * 60000000000L + seconds * 1000000000L + nanos
    /**
     * Calculates the number of nanoseconds between a given start timestamp and the end timestamp,
     * obtained by adding this duration to the start timestamp.
     *
     * @param startTs the starting temporal object to calculate the nanos from
     * @return the number of nanoseconds between the given start and end timestamps
     * @since 1.0.0
     */
    fun toNanos(startTs: Temporal) = startTs.until(startTs + this, ChronoUnit.NANOS)

    /**
     * Converts this duration to the specified temporal unit.
     *
     * This method is dangerous due to overflow.
     * 
     * @param unit The temporal unit to which the duration will be converted.
     * Supported units include ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS,
     * ChronoUnit.HOURS, ChronoUnit.MINUTES, ChronoUnit.SECONDS, and ChronoUnit.NANOS.
     * @return The duration expressed in the given temporal unit.
     * @throws UnsupportedTemporalTypeException if the specified unit is not supported.
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    infix fun toUnit(unit: TemporalUnit) = when(unit) {
        ChronoUnit.YEARS -> toYears()
        ChronoUnit.MONTHS -> toMonths()
        ChronoUnit.WEEKS -> toWeeks()
        ChronoUnit.DAYS -> toDays()
        ChronoUnit.HOURS -> toHours()
        ChronoUnit.MINUTES -> toMinutes()
        ChronoUnit.SECONDS -> toSeconds()
        ChronoUnit.MILLIS -> toMillis()
        ChronoUnit.MICROS -> toMicros()
        ChronoUnit.NANOS -> toNanos().toDouble()
        else -> throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
    }

    /**
     * Converts this instance of [Duration] to a Kotlin [kotlin.time.Duration].
     *
     * 
     * @return A [kotlin.time.Duration] representing the same duration as this [Duration] instance.
     * @since 1.0.0
     */
    @OptIn(RiskyApproximationOfTemporal::class)
    fun toKotlinDuration(): kotlin.time.Duration = toNanos().nanoseconds

    /**
     * Converts this Duration instance to a Java `java.time.Duration` object.
     *
     * 
     * @return A `java.time.Duration` equivalent to this Duration.
     * @since 1.0.0
     */
    @OptIn(RiskyApproximationOfTemporal::class)
    fun toJavaDuration(): java.time.Duration = java.time.Duration.ofNanos(toNanos())

    /**
     * Converts this Duration instance to a `java.time.Period` representation.
     *
     * 
     * to create an equivalent `java.time.Period` instance.
     * @return A `java.time.Period` object representing the same number of years, months, and days
     * as this Duration instance.
     * @since 1.0.0
     */
    fun toJavaPeriod(): Period = Period.of(years.toInt(), months.toInt(), days.toInt())

    /**
     * Converts the properties of the class into a map where the keys are their respective names
     * and the values are the corresponding property values.
     *
     * @return A map containing the properties `years`, `months`, `days`, `hours`, `minutes`, `seconds`,
     * and `nanos` as key-value pairs.
     * @since 1.0.0
     */
    fun toMap() = mapOf(
        "years" to years,
        "months" to months,
        "days" to days,
        "hours" to hours,
        "minutes" to minutes,
        "seconds" to seconds,
        "millis" to nanos / 1000000L,
        "micros" to nanos / 1000L,
        "nanos" to nanos
    )

    /**
     * Converts various time units into a map representation, where each key corresponds to a unit of time
     * (e.g., years, months, weeks) and its value represents the total quantity for that unit.
     *
     * The method assumes transformations from the calling context to calculate the respective totals for
     * each unit. The key-value pairs are represented as "unit" to its respective calculated value.
     *
     * Note: This function is marked with `@InvokeCarefully` due to potential limitations or considerations
     * in bytecode, such as overflow. It is recommended to analyze or test the implementation within specific
     * use cases to ensure no unintended behavior arises.
     *
     * @return A map where keys are time units ("years", "months", "weeks", etc.) and values represent
     *         the total amount within each respective unit.
     *
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    fun toTotalsMap() = mapOf(
        "years" to toYears(),
        "months" to toMonths(),
        "weeks" to toWeeks(),
        "days" to toDays(),
        "hours" to toHours(),
        "minutes" to toMinutes(),
        "seconds" to toSeconds(),
        "millis" to toMillis(),
        "micros" to toMicros(),
        "nanos" to toNanos().toDouble()
    )

    /**
     * Retrieves the value of the specified temporal unit from the current instance.
     *
     * 
     * @param unit The temporal unit whose value is to be retrieved.
     * @return The value of the specified temporal unit.
     * @throws UnsupportedTemporalTypeException if the specified unit is not supported by the instance.
     * @since 1.0.0
     */
    override fun get(unit: TemporalUnit) = when(unit) {
        ChronoUnit.YEARS -> years
        ChronoUnit.MONTHS -> months
        ChronoUnit.DAYS -> days
        ChronoUnit.HOURS -> hours
        ChronoUnit.MINUTES -> minutes
        ChronoUnit.SECONDS -> seconds
        ChronoUnit.MILLIS -> nanos / 1000000
        ChronoUnit.MICROS -> nanos / 1000
        ChronoUnit.NANOS -> nanos
        else -> throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
    }

    /**
     * Retrieves the duration value in the specified time unit.
     * 
     * @param unit The unit of time for which the duration value should be retrieved.
     *             Supported units are NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, and DAYS.
     * @return The duration value in the specified unit.
     * @since 1.0.0
     */
    @RiskyApproximationOfTemporal
    operator fun get(unit: DurationUnit) = when(unit) {
        DurationUnit.NANOSECONDS -> toNanos().toDouble()
        DurationUnit.MICROSECONDS -> toMicros()
        DurationUnit.MILLISECONDS -> toMillis()
        DurationUnit.SECONDS -> toSeconds()
        DurationUnit.MINUTES -> toMinutes()
        DurationUnit.HOURS -> toHours()
        DurationUnit.DAYS -> toDays()
    }

    /**
     * Retrieves the value of the specified temporal field.
     *
     * 
     * @param field The temporal field whose value is requested.
     * @return The value of the specified temporal field.
     * @throws UnsupportedTemporalTypeException If the specified field is not supported.
     * @since 1.0.0
     */
    operator fun get(field: TemporalField) = when(field) {
        ChronoField.YEAR_OF_ERA -> years
        ChronoField.MONTH_OF_YEAR -> months
        ChronoField.DAY_OF_MONTH -> days
        ChronoField.HOUR_OF_DAY -> hours
        ChronoField.MINUTE_OF_HOUR -> minutes
        ChronoField.SECOND_OF_MINUTE -> seconds
        ChronoField.MILLI_OF_SECOND -> nanos / 1000000
        ChronoField.MICRO_OF_SECOND -> nanos / 1000
        ChronoField.NANO_OF_SECOND -> nanos
        else -> throw UnsupportedTemporalTypeException("Unsupported field: $field")
    }

    /**
     * Retrieves the list of supported time units for the `Duration` class.
     *
     * This method returns a list of `ChronoUnit` constants representing the time-based units
     * that can be used with this `Duration`: years, months, days, hours, minutes, seconds, and nanoseconds.
     *
     * 
     * @return A list of `ChronoUnit` constants in descending order of granularity.
     * @since 1.0.0
     */
    override fun getUnits() = listOf(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS, ChronoUnit.HOURS, ChronoUnit.MINUTES, ChronoUnit.SECONDS, ChronoUnit.MILLIS, ChronoUnit.MICROS, ChronoUnit.NANOS)

    /**
     * Returns the chronology being used.
     *
     * @return the chronology, typically an instance of IsoChronology
     * @since 1.0.0
     */
    override fun getChronology(): Chronology = IsoChronology.INSTANCE

    /**
     * Adds this duration to the specified temporal object. The addition is performed in terms of
     * years, months, days, hours, minutes, seconds, and nanoseconds, depending on the type of
     * the temporal.
     *
     * 
     * @param temporal The temporal object to which the duration should be added.
     * @return A new temporal object with the duration added.
     * @since 1.0.0
     */
    override infix fun addTo(temporal: Temporal): Temporal {
        var result = temporal
        if (result !is LocalTime) {
            result = result.plus(years, ChronoUnit.YEARS).plus(months, ChronoUnit.MONTHS)
            if (result !is YearMonth) result = result.plus(days, ChronoUnit.DAYS)
        }
        if (result !is YearMonth && result !is LocalDate)
            result = result.plus(hours, ChronoUnit.HOURS).plus(minutes, ChronoUnit.MINUTES).plus(seconds, ChronoUnit.SECONDS).plus(nanos, ChronoUnit.NANOS)
        return result
    }

    /**
     * Subtracts the duration specified by the years, months, days, hours, minutes, seconds,
     * and nanoseconds fields from the given temporal object.
     *
     * 
     * @param temporal The temporal object from which the duration will be subtracted.
     * @return The resulting temporal object after subtracting the specified duration.
     * @since 1.0.0
     */
    override infix fun subtractFrom(temporal: Temporal): Temporal {
        var result = temporal
        if (result !is YearMonth && result !is LocalDate)
            result = result.minus(nanos, ChronoUnit.NANOS).minus(seconds, ChronoUnit.SECONDS).minus(minutes, ChronoUnit.MINUTES).minus(hours, ChronoUnit.HOURS)
        if (result !is YearMonth) result = result.minus(days, ChronoUnit.DAYS)
        result = result.minus(months, ChronoUnit.MONTHS).minus(years, ChronoUnit.YEARS)
        return result
    }

    /**
     * Converts the current `Duration` object into its ISO-8601 string representation.
     *
     * The format adheres to the pattern `PnYnMnWnDTnHnMnS`, where:
     * - `P` denotes the start of the duration representation.
     * - `nY` specifies the number of years if applicable.
     * - `nM` specifies the number of months if applicable.
     * - `nW` specifies the number of weeks if applicable.
     * - `nD` specifies the number of days if applicable.
     * - `T` separates the date components from the time components.
     * - `nH` specifies the number of hours if applicable.
     * - `nM` specifies the number of minutes if applicable.
     * - `nS` specifies the number of seconds if applicable, including up to 9 fractional digits for nanoseconds.
     *
     * If the duration is equivalent to zero, the method returns "PT0S" as a special case.
     *
     * 
     * @return A string representation of this `Duration` in ISO-8601 format.
     * @since 1.0.0
     */
    override fun toString() = toString(true)

    /**
     * Converts the current `Duration` object into its ISO-8601 string representation.
     *
     * The format adheres to the pattern `PnYnMnWnDTnHnMnS`, where:
     * - `P` denotes the start of the duration representation.
     * - `nY` specifies the number of years if applicable.
     * - `nM` specifies the number of months if applicable.
     * - `nW` specifies the number of weeks if applicable.
     * - `nD` specifies the number of days if applicable.
     * - `T` separates the date components from the time components.
     * - `nH` specifies the number of hours if applicable.
     * - `nM` specifies the number of minutes if applicable.
     * - `nS` specifies the number of seconds if applicable, including up to 9 fractional digits for nanoseconds.
     *
     * If the duration is equivalent to zero, the method returns "PT0S" as a special case.
     *
     *
     * @return A string representation of this `Duration` in ISO-8601 format.
     * @since 1.0.0
     */
    fun toString(weeks: Boolean) = if (this == Duration()) "PT0S" else buildString {
        append('P')
        if (years != 0L) append(years).append('Y')
        if (months != 0L) append(months).append('M')
        if (weeks) {
            var _days = days
            var _weeks = 0L
            while (_days >= 7) {
                _days -= 7
                _weeks++
            }
            if (_weeks != 0L) append(_weeks).append('W')
            if (_days != 0L) append(_days).append('D')
        } else {
            if (days != 0L) append(days).append('D')
        }
        if (hours != 0L || minutes != 0L || seconds != 0L || nanos != 0L) append('T')
        if (hours != 0L) append(hours).append('H')
        if (minutes != 0L) append(minutes).append('M')
        if (seconds != 0L || nanos != 0L) append(seconds)
            .append(if (nanos != 0L) "." + String.format("%09d", nanos).replace(Regex("0*$"), "") else "")
            .append('S')
    }

    /**
     * Compares this `Duration` instance with another `Duration` instance for order.
     *
     * 
     * @param other The `Duration` instance to compare this instance with.
     * @return A negative integer, zero, or a positive integer if this `Duration`
     *         is less than, equal to, or greater than the specified `other` `Duration`.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Duration) = when {
        years != other.years -> years.compareTo(other.years)
        months != other.months -> months.compareTo(other.months)
        days != other.days -> days.compareTo(other.days)
        hours != other.hours -> hours.compareTo(other.hours)
        minutes != other.minutes -> minutes.compareTo(other.minutes)
        seconds != other.seconds -> seconds.compareTo(other.seconds)
        else -> nanos.compareTo(other.nanos)
    }

    /**
     * Compares this duration instance to another object for equality.
     * Returns `true` if the other object is of the same type and has the same
     * values for all the duration components (years, months, days, hours,
     * minutes, seconds, and nanoseconds).
     *
     * 
     * @param other The object to compare with this duration instance.
     * @return `true` if the objects are equal, otherwise `false`.
     * @since 1.0.0
     */
    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Duration

        if (years != other.years) return false
        if (months != other.months) return false
        if (days != other.days) return false
        if (hours != other.hours) return false
        if (minutes != other.minutes) return false
        if (seconds != other.seconds) return false
        if (nanos != other.nanos) return false

        return true
    }

    /**
     * Generates a hash code for the object based on its properties.
     * Combines the hash codes of `years`, `months`, `days`, `hours`, `minutes`, `seconds`, and `nanos`
     * using a multiplication factor of 31 to ensure a well-distributed hash code.
     *
     * @return the hash code value for this object
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = years.hashCode()
        result = 31 * result + months.hashCode()
        result = 31 * result + days.hashCode()
        result = 31 * result + hours.hashCode()
        result = 31 * result + minutes.hashCode()
        result = 31 * result + seconds.hashCode()
        result = 31 * result + nanos.hashCode()
        return result
    }
}