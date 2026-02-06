@file:JvmName("TemporalUtilsKt")
@file:Suppress(
    "unused",
    "functionName",
    "kutils_take_as_int_invoke",
    "kutils_indexof_as_char_invoke",
    "kutils_getorthrow_as_invoke",
    "kutils_substring_as_get_intprogression",
    "kutils_temporal_now_as_temporal",
    "kutils_temporal_of_as_temporal",
    "kutils_temporal_parse_as_temporal",
    "kutils_substring_as_int_invoke"
)
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.time.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.format.SignStyle
import java.time.temporal.ChronoField
import java.time.zone.ZoneRules
import java.util.regex.Pattern
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlin.reflect.KProperty
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

/**
 * Regular expression for ISO 8601 date-time format.
 * @since 1.0.0
 */
val ISO_DATE_TIME_REGEX = Regex("^\\d{4}-((0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])|(W([0-4]\\d|5[0-3])-[0-7])|([0-2]\\d{2}|3[0-5]\\d|36[0-6]))T([01]\\d|2[0-3]):([0-5]\\d)(:([0-5]\\d)(\\.\\d{1,3})?)?(Z|[+-]\\d{2}(:\\d{2})?)?$|^\\d{4}((0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])|(W([0-4]\\d|5[0-3])[0-7])|([0-2]\\d{2}|3[0-5]\\d|36[0-6]))T([01]\\d|2[0-3])([0-5]\\d)(([0-5]\\d)(\\.\\d{1,3})?)?(Z|[+-]\\d{2}(\\d{2})?)?$")
/**
 * Regular expression pattern for ISO 8601 date-time format.
 * @since 1.0.0
 */
val ISO_DATE_TIME_PATTERN: Pattern = Pattern.compile(ISO_DATE_TIME_REGEX.pattern)
/**
 * Predicate for ISO 8601 date-time format.
 * @since 1.0.0
 */
val ISO_DATE_TIME_VALIDATOR = { s: String -> ISO_DATE_TIME_REGEX.matches(s) }
/**
 * Regular expression for ISO 8601 date-time format (only with separator '-' and ':').
 * @since 1.0.0
 */
val ISO_DATE_TIME_REGEX_ONLY_SEPARATOR = Regex("^\\d{4}-((0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])|(W([0-4]\\d|5[0-3])-[0-7])|([0-2]\\d{2}|3[0-5]\\d|36[0-6]))T([01]\\d|2[0-3]):([0-5]\\d)(:([0-5]\\d)(\\.\\d{1,3})?)?(Z|[+-]\\d{2}(:\\d{2})?)?$")
/**
 * Regular expression pattern for ISO 8601 date-time format (only with separator '-' and ':').
 * @since 1.0.0
 */
val ISO_DATE_TIME_PATTERN_ONLY_SEPARATOR: Pattern = Pattern.compile(ISO_DATE_TIME_REGEX_ONLY_SEPARATOR.pattern)
/**
 * Predicate for ISO 8601 date-time format (only with separator '-' and ':').
 * @since 1.0.0
 */
val ISO_DATE_TIME_VALIDATOR_ONLY_SEPARATOR = { s: String -> ISO_DATE_TIME_REGEX_ONLY_SEPARATOR.matches(s) }
/**
 * Regular expression for ISO 8601 date-time format with exclusion for weeks format and day-of-year format.
 * @since 1.0.0
 */
val ISO_DATE_TIME_STANDARD_REGEX = Regex("^\\d{4}-((0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))T([01]\\d|2[0-3]):([0-5]\\d)(:([0-5]\\d)(\\.\\d{1,3})?)?(Z|[+-]\\d{2}(:\\d{2})?)?$|^\\d{4}((0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01]))T([01]\\d|2[0-3])([0-5]\\d)(([0-5]\\d)(\\.\\d{1,3})?)?(Z|[+-]\\d{2}(\\d{2})?)?$")
/**
 * Regular expression pattern for ISO 8601 date-time format with exclusion for weeks format and day-of-year format.
 * @since 1.0.0
 */
val ISO_DATE_TIME_STANDARD_PATTERN: Pattern = Pattern.compile(ISO_DATE_TIME_STANDARD_REGEX.pattern)
/**
 * Predicate for ISO 8601 date-time format with exclusion for weeks format and day-of-year format.
 * @since 1.0.0
 */
val ISO_DATE_TIME_STANDARD_VALIDATOR = { s: String -> ISO_DATE_TIME_STANDARD_REGEX.matches(s) }
/**
 * Regular expression for ISO 8601 date-time format with exclusion for weeks format and day-of-year format (only with separator '-' and ':').
 * @since 1.0.0
 */
val ISO_DATE_TIME_STANDARD_REGEX_ONLY_SEPARATOR = Regex("^\\d{4}-((0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))T([01]\\d|2[0-3]):([0-5]\\d)(:([0-5]\\d)(\\.\\d{1,3})?)?(Z|[+-]\\d{2}(:\\d{2})?)?$")
/**
 * Regular expression pattern for ISO 8601 date-time format with exclusion for weeks format and day-of-year format (only with separator '-' and ':').
 * @since 1.0.0
 */
val ISO_DATE_TIME_STANDARD_PATTERN_ONLY_SEPARATOR: Pattern = Pattern.compile(ISO_DATE_TIME_STANDARD_REGEX_ONLY_SEPARATOR.pattern)
/**
 * Predicate for ISO 8601 date-time format with exclusion for weeks format and day-of-year format (only with separator '-' and ':').
 * @since 1.0.0
 */
val ISO_DATE_TIME_STANDARD_VALIDATOR_ONLY_SEPARATOR = { s: String -> ISO_DATE_TIME_STANDARD_REGEX_ONLY_SEPARATOR.matches(s) }
/**
 * Regular expression for ISO 8601 date format.
 * @since 1.0.0
 */
val ISO_DATE_REGEX = Regex("^\\d{4}-((0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])|(W([0-4]\\d|5[0-3])-[0-7])|([0-2]\\d{2}|3[0-5]\\d|36[0-6]))$|^\\d{4}((0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])|(W([0-4]\\d|5[0-3])[0-7])|([0-2]\\d{2}|3[0-5]\\d|36[0-6]))$")
/**
 * Regular expression pattern for ISO 8601 date format.
 * @since 1.0.0
 */
val ISO_DATE_PATTERN: Pattern = Pattern.compile(ISO_DATE_REGEX.pattern)
/**
 * Predicate for ISO 8601 date format.
 * @since 1.0.0
 */
val ISO_DATE_VALIDATOR = { s: String -> ISO_DATE_REGEX.matches(s) }
/**
 * Regular expression for ISO 8601 date format (only with separator '-').
 * @since 1.0.0
 */
val ISO_DATE_REGEX_ONLY_SEPARATOR = Regex("^\\d{4}-((0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))|(W([0-4]\\d|5[0-3])-[0-7])|([0-2]\\d{2}|3[0-5]\\d|36[0-6])$")
/**
 * Regular expression pattern for ISO 8601 date format (only with separator '-').
 * @since 1.0.0
 */
val ISO_DATE_PATTERN_ONLY_SEPARATOR: Pattern = Pattern.compile(ISO_DATE_REGEX_ONLY_SEPARATOR.pattern)
/**
 * Predicate for ISO 8601 date format (only with separator '-').
 * @since 1.0.0
 */
val ISO_DATE_VALIDATOR_ONLY_SEPARATOR = { s: String -> ISO_DATE_REGEX_ONLY_SEPARATOR.matches(s) }
/**
 * Regular expression for ISO 8601 date format with exclusion for weeks format and day-of-year format.
 * @since 1.0.0
 */
val ISO_DATE_STANDARD_REGEX = Regex("^\\d{4}-((0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$|^\\d{4}((0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01]))$")
/**
 * Regular expression pattern for ISO 8601 date format with exclusion for weeks format and day-of-year format.
 * @since 1.0.0
 */
val ISO_DATE_STANDARD_PATTERN: Pattern = Pattern.compile(ISO_DATE_STANDARD_REGEX.pattern)
/**
 * Predicate for ISO 8601 date format with exclusion for weeks format and day-of-year format.
 * @since 1.0.0
 */
val ISO_DATE_STANDARD_VALIDATOR = { s: String -> ISO_DATE_STANDARD_REGEX.matches(s) }
/**
 * Regular expression for ISO 8601 date format with exclusion for weeks format and day-of-year format (only with separator '-').
 * @since 1.0.0
 */
val ISO_DATE_STANDARD_REGEX_ONLY_SEPARATOR = Regex("^\\d{4}-((0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$")
/**
 * Regular expression pattern for ISO 8601 date format with exclusion for weeks format and day-of-year format (only with separator '-').
 * @since 1.0.0
 */
val ISO_DATE_STANDARD_PATTERN_ONLY_SEPARATOR: Pattern = Pattern.compile(ISO_DATE_STANDARD_REGEX_ONLY_SEPARATOR.pattern)
/**
 * Predicate for ISO 8601 date format with exclusion for weeks format and day-of-year format (only with separator '-').
 * @since 1.0.0
 */
val ISO_DATE_STANDARD_VALIDATOR_ONLY_SEPARATOR = { s: String -> ISO_DATE_STANDARD_REGEX_ONLY_SEPARATOR.matches(s) }
/**
 * Regular expression for ISO 8601 time format.
 * @since 1.0.0
 */
val ISO_TIME_REGEX = Regex("^([01]\\d|2[0-3]):([0-5]\\d)(:([0-5]\\d)(\\.\\d{1,3})?)?(Z|[+-]\\d{2}(:\\d{2})?)?$|([01]\\d|2[0-3])(([0-5]\\d)(([0-5]\\d)(\\.\\d{1,3})?)?)(Z|[+-]\\d{2}(\\d{2})?)?$")
/**
 * Regular expression pattern for ISO 8601 time format.
 * @since 1.0.0
 */
val ISO_TIME_PATTERN: Pattern = Pattern.compile(ISO_TIME_REGEX.pattern)
/**
 * Predicate for ISO 8601 time format.
 * @since 1.0.0
 */
val ISO_TIME_VALIDATOR = { s: String -> ISO_TIME_REGEX.matches(s) }
/**
 * Regular expression for ISO 8601 time format (only with separator ':').
 * @since 1.0.0
 */
val ISO_TIME_REGEX_ONLY_SEPARATOR = Regex("^([01]\\d|2[0-3]):([0-5]\\d)(:([0-5]\\d)(\\.\\d{1,3})?)?(Z|[+-]\\d{2}(:\\d{2})?)?$")
/**
 * Regular expression pattern for ISO 8601 time format (only with separator ':').
 * @since 1.0.0
 */
val ISO_TIME_PATTERN_ONLY_SEPARATOR: Pattern = Pattern.compile(ISO_TIME_REGEX_ONLY_SEPARATOR.pattern)
/**
 * Predicate for ISO 8601 time format (only with separator ':').
 * @since 1.0.0
 */
val ISO_TIME_VALIDATOR_ONLY_SEPARATOR = { s: String -> ISO_TIME_REGEX_ONLY_SEPARATOR.matches(s) }
/**
 * Regular expression for ISO 8601 period format.
 * @since 1.0.0
 */
val ISO_PERIOD_REGEX = Regex("^P(\\d+Y)?(\\d+M)?(\\d+D)?(T(?=.*\\d+H|.*\\d+M|.*\\d+(\\.\\d+)?S)(\\d+H)?(\\d+M)?(\\d+(\\.\\d+)?S)?)?$")
/**
 * Regular expression pattern for ISO 8601 period format.
 * @since 1.0.0
 */
val ISO_PERIOD_PATTERN: Pattern = Pattern.compile(ISO_PERIOD_REGEX.pattern)
/**
 * Predicate for ISO 8601 period format.
 * @since 1.0.0
 */
val ISO_PERIOD_VALIDATOR = { s: String -> ISO_PERIOD_REGEX.matches(s) }

/**
 * Parses the current [CharSequence] to a [LocalDateTime] object.
 *
 * This method attempts to interpret the [CharSequence] as an ISO-8601 compliant date-time string.
 * It supports various formats, including those with fractional seconds and time zone designators (e.g., 'Z' or offsets like '+01:00').

 * @return The parsed [LocalDateTime] if the input is valid, wrapped in a [Result].
 * @since 1.0.0
 */
fun CharSequence.parseToLocalDateTime(): Result<LocalDateTime> {
    if (isEmpty()) return failure(DateTimeParseException("The input is empty.", toString(), 0))
    var dateTimeString = toString().trim()

    if ("." in dateTimeString) {
        val decimal = dateTimeString.substring(
            dateTimeString.indexOf('.'),
            if ("Z" in dateTimeString) dateTimeString.length - 1 else if ("+" in dateTimeString) dateTimeString.indexOf("+") else if ("-" in (-10)(dateTimeString))
                dateTimeString.indexOf("-") else dateTimeString.length
        )
        dateTimeString = when (decimal.length) {
            1 -> dateTimeString.take(dateTimeString.indexOf('.') + 1) + "000"
            2 -> dateTimeString.take(dateTimeString.indexOf('.') + 2) + "00"
            3 -> dateTimeString.take(dateTimeString.indexOf('.') + 3) + "0"
            else -> dateTimeString.take(dateTimeString.indexOf('.') + 4)
        }
    }

    val subDateTimeString = dateTimeString.take(
        if ("Z" in dateTimeString) dateTimeString.length - 1 else if ("+" in dateTimeString) dateTimeString.indexOf("+") else if ("-" in (-10)(dateTimeString))
            dateTimeString.indexOf("-")
        else dateTimeString.length
    )
    if (ISO_DATE_TIME_STANDARD_VALIDATOR(dateTimeString)) {
        if ("-" in dateTimeString && ":" in dateTimeString) return success(LocalDateTime.of(
            LocalDate.of(
                dateTimeString.take(4).toInt(),
                dateTimeString.substring(5, 7).toInt(),
                dateTimeString.substring(8, 10).toInt()
            ), LocalTime.of(
                dateTimeString.substring(11, 13).toInt(),
                dateTimeString.substring(14, 16).toInt(),
                if (subDateTimeString.length >= 17 && dateTimeString[16] == ':') dateTimeString.substring(17, 19).toInt() else 0,
                if (subDateTimeString.length >= 17 && dateTimeString[16] == ':') (if ("." in dateTimeString) dateTimeString.substring(20, 23).toInt() * 1000000 else 0) else 0
            )
        ))
        return success(LocalDateTime.of(
            LocalDate.of(
                dateTimeString.take(4).toInt(),
                dateTimeString.substring(4, 6).toInt(),
                dateTimeString.substring(6, 8).toInt()
            ), LocalTime.of(
                dateTimeString.substring(9, 11).toInt(),
                dateTimeString.substring(11, 13).toInt(),
                if (subDateTimeString.length > 14 && dateTimeString[13].toString().isNumber())
                    dateTimeString.substring(13, 15).toInt() else 0,
                if (subDateTimeString.length > 14 && dateTimeString[13].toString().isNumber())
                    (if ("." in dateTimeString) dateTimeString.substring(16, 19).toInt() * 1000000 else 0)
                else 0
            )
        ))
    }
    return failure(DateTimeParseException("Invalid ISO-8601 date-time format: $dateTimeString", dateTimeString, 0))
}

/**
 * Parses the given [CharSequence] to an [OffsetDateTime].
 * This method attempts to interpret the input as a date-time with an offset,
 * conforming to ISO-8601 standards. The input string is trimmed of leading
 * and trailing spaces before processing. If the string contains UTC (Z)
 * or a specified offset (+/-), the offset will be applied to produce an
 * [OffsetDateTime].
 *
 * @return the parsed [OffsetDateTime] instance, wrapped in a [Result].
 * @since 1.0.0
 */
fun CharSequence.parseToOffsetDateTime(): Result<OffsetDateTime> {
    if (isEmpty()) return failure(DateTimeParseException("The input is empty.", toString(), 0))
    val dateTimeString = toString().trim()

    return runCatching {
        dateTimeString.parseToLocalDateTime().getOrThrow().atOffset(
            if ("Z" in dateTimeString) ZoneOffset.UTC
            else ZoneOffset.of(
                dateTimeString.substring(
                    if ("+" in dateTimeString) dateTimeString.indexOf("+") else dateTimeString.lastIndexOf("-")
                )
            )
        )
    }
}

/**
 * Parses the current CharSequence into a LocalDate object if it is in a valid ISO date or datetime format.
 *
 * The method supports both `yyyy-MM-dd` and `yyyyMMdd` formats.
 *
 * @return a LocalDate object representing the parsed date, wrapped in a [Result].
 * @since 1.0.0
 */
fun CharSequence.parseToLocalDate(): Result<LocalDate> {
    if (isEmpty()) return failure(DateTimeParseException("The input is empty.", toString(), 0))
    val dateString = toString().trim()

    return if (ISO_DATE_TIME_STANDARD_VALIDATOR(dateString) || ISO_DATE_STANDARD_VALIDATOR(dateString)) {
        success(if ("-" in dateString) LocalDate.of(
            dateString.take(4).toInt(),
            dateString.substring(5, 7).toInt(),
            dateString.substring(8, 10).toInt()
        )
        else LocalDate.of(
            dateString.take(4).toInt(),
            dateString.substring(4, 6).toInt(),
            dateString.substring(6, 8).toInt()
        ))
    } else failure(DateTimeParseException("Invalid ISO-8601 date format: $dateString", dateString, 0))
}

/**
 * Parses the current [CharSequence] to a [LocalTime] object.
 *
 * The method processes the input [CharSequence] to handle ISO 8601 time and date-time formats.
 * It trims and validates the input, handling fractional seconds and optional timezone or offset designators.
 *
 * @return a [LocalTime] instance if the input [CharSequence] is valid, wrapped in a [Result].
 * @since 1.0.0
 */
fun CharSequence.parseToLocalTime(): Result<LocalTime> {
    if (isEmpty()) return failure(DateTimeParseException("The input is empty.", toString(), 0))
    var timeString = toString().trim()

    if ("." in timeString) {
        val decimal = timeString.substring(
            timeString.indexOf('.'),
            if ("Z" in timeString) timeString.length - 1 else if ("+" in timeString)
                timeString.indexOf("+")
            else if ("-" in (-10)(timeString)) timeString.indexOf("-")
            else timeString.length
        )
        timeString = when (decimal.length) {
            1 -> timeString.take(timeString.indexOf('.') + 1) + "000"
            2 -> timeString.take(timeString.indexOf('.') + 2) + "00"
            3 -> timeString.take(timeString.indexOf('.') + 3) + "0"
            else -> timeString.take(timeString.indexOf('.') + 4)
        }
    }

    val subTimeString = timeString.take(if ("Z" in timeString) length - 1 else if ("+" in timeString) timeString.indexOf("+")
    else if ("-" in timeString) timeString.indexOf("-") else timeString.length)
    if (ISO_DATE_TIME_STANDARD_VALIDATOR(timeString) || ISO_TIME_VALIDATOR(timeString)) {
        if (":" in timeString) return success(LocalTime.of(
            timeString.take(2).toInt(),
            timeString.substring(3, 5).toInt(),
            if (subTimeString.length > 6 && timeString[6].toString().isNumber()) timeString.substring(6, 8).toInt() else 0,
            if (subTimeString.length > 8 && timeString[9].toString().isNumber())
                (if ("." in timeString) timeString.substring(9, 12).toInt() * 1000000 else 0)
            else 0
        ))
        return success(LocalTime.of(
            timeString.take(2).toInt(),
            timeString.substring(2, 4).toInt(),
            if (subTimeString.length > 4) timeString.substring(4, 6).toInt() else 0,
            if (subTimeString.length > 6 && timeString[7].toString().isNumber())
                (if ("." in timeString) timeString.substring(7, 10).toInt() * 1000000 else 0)
            else 0
        ))
    }
    return failure(DateTimeParseException("Invalid ISO-8601 time format: $timeString", timeString, 0))
}

/**
 * Parses the [CharSequence] into an [OffsetTime].
 *
 * The input string should represent a valid ISO-8601 time with an offset, such as "10:15:30+01:00" or "10:15:30Z".
 *
 * @return the parsed [OffsetTime] if the input is valid, wrapped in a [Result].
 * @since 1.0.0
 */
fun CharSequence.parseToOffsetTime(): Result<OffsetTime> {
    if (isEmpty()) return failure(DateTimeParseException("The input is empty.", toString(), 0))
    val dateTimeString = toString().trim()

    return runCatching {
        dateTimeString.parseToLocalTime().getOrThrow().atOffset(
            if ("Z" in dateTimeString) ZoneOffset.UTC
            else ZoneOffset.of(
                dateTimeString.substring(
                    if ("+" in dateTimeString) dateTimeString.indexOf("+") else dateTimeString.lastIndexOf("-")
                )
            )
        )
    }
}

/**
 * Converts the properties of an object into a map representation.
 *
 * @return A map containing the extracted values from LocalDate and LocalTime.
 * @since 1.0.0
 */
@Suppress("functionName")
private fun LocalDateTime._toMap() = mapOf(
    "localDate" to toLocalDate(),
    "localTime" to toLocalTime(),
    "year" to Year.of(year),
    "month" to month!!,
    "day" to dayOfMonth,
    "hour" to hour,
    "minute" to minute,
    "second" to second,
    "milli" to nano / 1000000,
    "nano" to nano
)

/**
 * Retrieves the value associated with a property name from an internal map.
 *
 * - `localDate`: The LocalDate object - TYPE: [LocalDate].
 * - `localTime`: The LocalTime object - TYPE: [LocalTime].
 * - `year`: The value of the year from the LocalDate object - TYPE: [Month].
 * - `month`: The value of the month from the LocalDate object - TYPE: [Month].
 * - `day`: The day of the month from the LocalDate object - TYPE: [Int].
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
operator fun <R> LocalDateTime.getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R

/**
 * Converts the properties of an object into a map representation.
 *
 * @return A map containing the extracted values from LocalDate and LocalTime and offset.
 * @since 1.0.0
 */
@Suppress("functionName")
private fun OffsetDateTime._toMap() = mapOf(
    "localDate" to toLocalDate(),
    "localTime" to toLocalTime(),
    "year" to Year.of(year),
    "month" to month!!,
    "day" to dayOfMonth,
    "hour" to hour,
    "minute" to minute,
    "second" to second,
    "milli" to nano / 1000000,
    "nano" to nano,
    "offset" to offset
)

/**
 * Retrieves the value associated with a property name from an internal map.
 *
 * - `localDate`: The LocalDate object - TYPE: [LocalDate].
 * - `localTime`: The LocalTime object - TYPE: [LocalTime].
 * - `year`: The value of the year from the LocalDate object - TYPE: [Month].
 * - `month`: The value of the month from the LocalDate object - TYPE: [Month].
 * - `day`: The day of the month from the LocalDate object - TYPE: [Int].
 * - `hour`: The hour component of the LocalTime object - TYPE: [Int].
 * - `minute`: The minute component of the LocalTime object - TYPE: [Int].
 * - `second`: The second component of the LocalTime object - TYPE: [Int].
 * - `milli`: The millisecond part of the nanoseconds from the LocalTime object - TYPE: [Int].
 * - `nano`: The nanosecond component of the LocalTime object - TYPE: [Int].
 * - `offset`: The [ZoneOffset] object - TYPE: [ZoneOffset].
 *
 * @param R The expected type of the value being retrieved.
 * @param thisRef The reference to the object in which the property is defined. Can be null.
 * @param property The metadata for the property whose value is being retrieved.
 * @return The value associated with the property name, cast to the expected type.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
operator fun <R> OffsetDateTime.getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R

/**
 * Converts the properties of an object into a map representation.
 *
 * @return A map containing the extracted values from LocalDate and LocalTime, offset and timeZone.
 * @since 1.0.0
 */
@Suppress("functionName")
private fun ZonedDateTime._toMap() = mapOf(
    "localDate" to toLocalDate(),
    "localTime" to toLocalTime(),
    "year" to Year.of(year),
    "month" to month!!,
    "day" to dayOfMonth,
    "hour" to hour,
    "minute" to minute,
    "second" to second,
    "milli" to nano / 1000000,
    "nano" to nano,
    "offset" to offset,
    "timeZone" to zone,
)

/**
 * Retrieves the value associated with a property name from an internal map.
 *
 * - `localDate`: The LocalDate object - TYPE: [LocalDate].
 * - `localTime`: The LocalTime object - TYPE: [LocalTime].
 * - `year`: The value of the year from the LocalDate object - TYPE: [Month].
 * - `month`: The value of the month from the LocalDate object - TYPE: [Month].
 * - `day`: The day of the month from the LocalDate object - TYPE: [Int].
 * - `hour`: The hour component of the LocalTime object - TYPE: [Int].
 * - `minute`: The minute component of the LocalTime object - TYPE: [Int].
 * - `second`: The second component of the LocalTime object - TYPE: [Int].
 * - `milli`: The millisecond part of the nanoseconds from the LocalTime object - TYPE: [Int].
 * - `nano`: The nanosecond component of the LocalTime object - TYPE: [Int].
 * - `offset`: The [ZoneOffset] object - TYPE: [ZoneOffset].
 * - `timeZone`: The [ZoneId] object - TYPE: [ZoneId].
 *
 * @param R The expected type of the value being retrieved.
 * @param thisRef The reference to the object in which the property is defined. Can be null.
 * @param property The metadata for the property whose value is being retrieved.
 * @return The value associated with the property name, cast to the expected type.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
operator fun <R> ZonedDateTime.getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R

/**
 * Extracts the year component from the LocalDateTime instance.
 *
 * This function serves as a component operator and retrieves the year
 * as a Year instance from the given LocalDateTime object. It can be
 * used in destructuring declarations or for directly obtaining the year.
 *
 * @receiver The LocalDateTime instance from which the year is extracted.
 * @return The year as a Year instance.
 * @since 1.0.0
 */
operator fun LocalDateTime.component1() = Year.of(year)!!
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
operator fun LocalDateTime.component2() = month!!
/**
 * Provides the day of the month as part of the decomposition of a date or time object.
 * This function acts as a component function, often used in destructuring declarations
 * to directly access the `dayOfMonth` field of the `monthDay` property.
 *
 * @return The day of the month, extracted from the `monthDay` property.
 * @since 1.0.0
 */
operator fun LocalDateTime.component3() = dayOfMonth
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
operator fun LocalDateTime.component4() = hour
/**
 * Provides the minute component of the `localTime` value.
 *
 * This function is a component function commonly used in destructuring declarations.
 * It returns the `minute` property from the `localTime`.
 *
 * @return the minute component of the `localTime`.
 * @since 1.0.0
 */
operator fun LocalDateTime.component5() = minute
/**
 * Component function that extracts the `second` value from the `localTime` property.
 * This function is a part of the destructuring declaration mechanism.
 *
 * @return The second component of the `localTime`, as an integer.
 * @since 1.0.0
 */
operator fun LocalDateTime.component6() = second
/**
 * Provides the nanosecond component of the `localTime` property.
 * This operator function is typically used for destructuring declarations
 * to extract the sixth component from an object.
 *
 * @return The nanoseconds part of the `localTime` property.
 * @since 1.0.0
 */
operator fun LocalDateTime.component7() = nano

/**
 * Extracts the year component from the LocalDateTime instance.
 *
 * This function serves as a component operator and retrieves the year
 * as a Year instance from the given LocalDateTime object. It can be
 * used in destructuring declarations or for directly obtaining the year.
 *
 * @receiver The LocalDateTime instance from which the year is extracted.
 * @return The year as a Year instance.
 * @since 1.0.0
 */
operator fun OffsetDateTime.component1() = Year.of(year)!!
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
operator fun OffsetDateTime.component2() = month!!
/**
 * Provides the day of the month as part of the decomposition of a date or time object.
 * This function acts as a component function, often used in destructuring declarations
 * to directly access the `dayOfMonth` field of the `monthDay` property.
 *
 * @return The day of the month, extracted from the `monthDay` property.
 * @since 1.0.0
 */
operator fun OffsetDateTime.component3() = dayOfMonth
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
operator fun OffsetDateTime.component4() = hour
/**
 * Provides the minute component of the `localTime` value.
 *
 * This function is a component function commonly used in destructuring declarations.
 * It returns the `minute` property from the `localTime`.
 *
 * @return the minute component of the `localTime`.
 * @since 1.0.0
 */
operator fun OffsetDateTime.component5() = minute
/**
 * Component function that extracts the `second` value from the `localTime` property.
 * This function is a part of the destructuring declaration mechanism.
 *
 * @return The second component of the `localTime`, as an integer.
 * @since 1.0.0
 */
operator fun OffsetDateTime.component6() = second
/**
 * Provides the nanosecond component of the `localTime` property.
 * This operator function is typically used for destructuring declarations
 * to extract the sixth component from an object.
 *
 * @return The nanoseconds part of the `localTime` property.
 * @since 1.0.0
 */
operator fun OffsetDateTime.component7() = nano
/**
 * Returns the seventh component of the instance, represented by its offset value.
 * This function is a part of the componentN operator conventions and is typically used in destructuring declarations.
 *
 * @return The offset value as the seventh component.
 * @since 1.0.0
 */
operator fun OffsetDateTime.component8() = offset!!

/**
 * Extracts the year component from the LocalDateTime instance.
 *
 * This function serves as a component operator and retrieves the year
 * as a Year instance from the given LocalDateTime object. It can be
 * used in destructuring declarations or for directly obtaining the year.
 *
 * @receiver The LocalDateTime instance from which the year is extracted.
 * @return The year as a Year instance.
 * @since 1.0.0
 */
operator fun ZonedDateTime.component1() = Year.of(year)!!
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
operator fun ZonedDateTime.component2() = month!!
/**
 * Provides the day of the month as part of the decomposition of a date or time object.
 * This function acts as a component function, often used in destructuring declarations
 * to directly access the `dayOfMonth` field of the `monthDay` property.
 *
 * @return The day of the month, extracted from the `monthDay` property.
 * @since 1.0.0
 */
operator fun ZonedDateTime.component3() = dayOfMonth
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
operator fun ZonedDateTime.component4() = hour
/**
 * Provides the minute component of the `localTime` value.
 *
 * This function is a component function commonly used in destructuring declarations.
 * It returns the `minute` property from the `localTime`.
 *
 * @return the minute component of the `localTime`.
 * @since 1.0.0
 */
operator fun ZonedDateTime.component5() = minute
/**
 * Component function that extracts the `second` value from the `localTime` property.
 * This function is a part of the destructuring declaration mechanism.
 *
 * @return The second component of the `localTime`, as an integer.
 * @since 1.0.0
 */
operator fun ZonedDateTime.component6() = second
/**
 * Provides the nanosecond component of the `localTime` property.
 * This operator function is typically used for destructuring declarations
 * to extract the sixth component from an object.
 *
 * @return The nanoseconds part of the `localTime` property.
 * @since 1.0.0
 */
operator fun ZonedDateTime.component7() = nano
/**
 * Provides the seventh component of the object, represented by the `timeZone` property.
 * This enables destructuring declarations to access the `timeZone` value directly.
 *
 * @return The value of the `timeZone` property.
 * @since 1.0.0
 */
operator fun ZonedDateTime.component8() = zone!!
/**
 * Returns the seventh component of the instance, represented by its offset value.
 * This function is a part of the componentN operator conventions and is typically used in destructuring declarations.
 *
 * @return The offset value as the seventh component.
 * @since 1.0.0
 */
operator fun ZonedDateTime.component9() = offset!!

/**
 * Extracts the year component as a `Year` instance from the `LocalDate`.
 *
 * This function enables destructuring of `LocalDate` objects, allowing the extraction
 * of the year value in a structured format.
 *
 * @receiver The `LocalDate` instance from which the year component is retrieved.
 * @return The year of this `LocalDate` as a `Year` object.
 * @since 1.0.0
 */
operator fun LocalDate.component1() = Year.of(year)!!
/**
 * Returns the month component of this `LocalDate` instance.
 * Can be used to destructure the date into its components.
 *
 * @receiver the `LocalDate` instance from which the value is retrieved
 * @return the month part of the `LocalDate`
 * @since 1.0.0
 */
operator fun LocalDate.component2() = month!!
/**
 * Operator function that provides destructuring support for `LocalDate`.
 * This method corresponds to the `dayOfMonth` property of `LocalDate`,
 * allowing it to be extracted as the third component in destructuring declarations.
 *
 * @return the day of the month component of the `LocalDate`.
 * @since 1.0.0
 */
operator fun LocalDate.component3() = dayOfMonth

/**
 * Provides the `hour` component of the `LocalTime` instance,
 * allowing destructuring declarations to extract the hour value.
 *
 * @receiver LocalTime instance from which the hour component is extracted.
 * @return The hour of this `LocalTime`, as an integer.
 * @since 1.0.0
 */
operator fun LocalTime.component1() = hour
/**
 * Returns the minute component of this `LocalTime` instance.
 * This operator function enables destructuring declarations
 * to extract the minute part of a `LocalTime` object.
 *
 * @receiver The `LocalTime` instance from which the minute is extracted.
 * @return The minute of the hour as an integer.
 * @since 1.0.0
 */
operator fun LocalTime.component2() = minute
/**
 * Extracts the seconds component from this `LocalTime` instance.
 *
 * This operator function allows deconstructing `LocalTime` instances
 * into their third component, which represents the number of seconds
 * within the time.
 *
 * @receiver The `LocalTime` instance from which the seconds component is extracted.
 * @return The second component of this `LocalTime`.
 * @since 1.0.0
 */
operator fun LocalTime.component3() = second
/**
 * Returns the nanosecond component of this `LocalTime` instance.
 *
 * This operator function allows destructuring declarations to access
 * the nanosecond value as the fourth component of `LocalTime`.
 *
 * @return the nanosecond part of this `LocalTime`.
 * @since 1.0.0
 */
operator fun LocalTime.component4() = nano

/**
 * Returns the hour component of this `OffsetTime`.
 *
 * This operator function allows destructuring the `OffsetTime` instance to retrieve its hour value.
 *
 * @receiver The `OffsetTime` instance from which the hour is extracted.
 * @return The hour of the `OffsetTime`.
 * @since 1.0.0
 */
operator fun OffsetTime.component1() = hour
/**
 * Extracts the minute component from this `OffsetTime` instance.
 *
 * This function allows the `minute` component of the time with offset to be accessed using
 * destructuring declarations.
 *
 * @receiver The `OffsetTime` instance from which the minute is extracted.
 * @return The minute component of the time.
 * @since 1.0.0
 */
operator fun OffsetTime.component2() = minute
/**
 * Extracts the second component from the OffsetTime instance.
 *
 * This operator function allows destructuring declarations to retrieve the second (seconds field)
 * of the OffsetTime.
 *
 * @receiver OffsetTime instance from which the second value is extracted.
 * @return The second component of the OffsetTime as an integer.
 * @since 1.0.0
 */
operator fun OffsetTime.component3() = second
/**
 * Extracts the nano-of-second component from the `OffsetTime`.
 *
 * This operator function allows destructuring the `OffsetTime` instance to retrieve
 * its nanosecond precision within the second.
 *
 * @receiver the `OffsetTime` instance from which the nano-of-second value is extracted
 * @return the nanosecond part of the time
 * @since 1.0.0
 */
operator fun OffsetTime.component4() = nano
/**
 * Returns the offset of this OffsetTime as part of the destructuring declaration.
 *
 * This function is invoked as the fifth component function when destructuring an OffsetTime.
 * The offset represents the difference between the local time zone and UTC for this OffsetTime instance.
 *
 * @return the ZoneOffset of this OffsetTime
 * @since 1.0.0
 */
operator fun OffsetTime.component5() = offset!!

/**
 * Increments this `LocalDate` instance by one day.
 *
 * This operator function allows the `++` operator to be used on LocalDate objects.
 * The operation returns a new `LocalDate` instance that represents the day
 * immediately following the current date.
 *
 * @receiver The `LocalDate` instance to be incremented.
 * @return A new `LocalDate` instance representing the incremented date.
 * @throws DateTimeException if a calculation exceeds the supported date range.
 * @since 1.0.0
 */
operator fun LocalDate.inc() = plusDays(1)!!

/**
 * Formats the `LocalDate` instance using the specified pattern.
 *
 * @param pattern the desired format pattern following the rules of `DateTimeFormatter`
 * @return the formatted date string
 * @throws IllegalArgumentException if the pattern is invalid
 * @since 1.0.0
 */
operator fun LocalDate.invoke(pattern: String) = format(DateTimeFormatter.ofPattern(pattern))!!
/**
 * Formats the `LocalDateTime` instance using the specified pattern.
 * This operator function allows the invocation of a `LocalDateTime` with a pattern string to format its value.
 *
 * @param pattern The pattern string used for formatting, compliant with the `DateTimeFormatter` patterns.
 * @return The formatted date-time string based on the provided pattern.
 * @throws IllegalArgumentException If the pattern is invalid or unsupported.
 * @since 1.0.0
 */
operator fun LocalDateTime.invoke(pattern: String) = format(DateTimeFormatter.ofPattern(pattern))!!
/**
 * Formats the current `OffsetDateTime` using the provided pattern.
 *
 * @param pattern the date-time pattern to format the `OffsetDateTime`.
 * @since 1.0.0
 */
operator fun OffsetDateTime.invoke(pattern: String) = format(DateTimeFormatter.ofPattern(pattern))!!
/**
 * Formats the ZonedDateTime object to a string based on the given pattern.
 *
 * @param pattern the date-time formatting pattern to apply
 * @return the formatted date-time string
 * @throws IllegalArgumentException if the pattern is invalid
 * @since 1.0.0
 */
operator fun ZonedDateTime.invoke(pattern: String) = format(DateTimeFormatter.ofPattern(pattern))!!
/**
 * Formats the instance of `LocalTime` using the specified pattern.
 *
 * @param pattern the pattern to format the `LocalTime` instance. The pattern must be a valid
 *                `DateTimeFormatter` pattern.
 * @since 1.0.0
 */
operator fun LocalTime.invoke(pattern: String) = format(DateTimeFormatter.ofPattern(pattern))!!
/**
 * Formats the `OffsetTime` instance using the specified pattern.
 *
 * @param pattern the pattern to use for formatting the `OffsetTime` instance
 * @return the formatted time as a string
 * @since 1.0.0
 */
operator fun OffsetTime.invoke(pattern: String) = format(DateTimeFormatter.ofPattern(pattern))!!

/**
 * Adjusts this `LocalDateTime` instance to a specified `ZoneOffset`.
 *
 * Converts the current `LocalDateTime` from a given source time zone to the specified
 * destination zone offset, returning a new `LocalDateTime` instance with the adjusted
 * offset applied.
 *
 * @param fromOffset the source time zone ID from which the `LocalDateTime` should be converted.
 * Defaults to the system's default time zone.
 * @param toOffset the destination `ZoneOffset` to which the `LocalDateTime` should be converted.
 * @since 1.0.0
 */
fun LocalDateTime.atOffset(fromOffset: ZoneId = ZoneId.systemDefault(), toOffset: ZoneOffset) =
    atZone(fromOffset).withZoneSameInstant(toOffset).toLocalDateTime()!!

/**
 * Returns a `OffsetDateTime` created by combining this `LocalDateTime` with the specified time-zone offset.
 *
 * @param offset the zone identifier from which the time-zone offset is derived to combine with this date-time.
 * @return the resulting offset date-time.
 * @throws DateTimeException if the offset cannot be obtained from the zone identifier.
 * @since 1.0.0
 */
fun LocalDateTime.atOffset(offset: ZoneIdent) = atOffset(offset.offset)!!

/**
 * Adjusts the LocalDateTime from one time-zone offset to another and returns the resulting [OffsetDateTime].
 *
 * @param fromOffset the initial time-zone offset of the LocalDateTime, defaults to the system's default zone ID
 * @param toOffset the target time-zone offset to which the LocalDateTime needs to be converted
 * @since 1.0.0
 */
fun LocalDateTime.atOffset(fromOffset: ZoneId = ZoneId.systemDefault(), toOffset: ZoneIdent) =
    atZone(fromOffset).withZoneSameInstant(toOffset.offset).toOffsetDateTime()!!

/**
 * Adjusts the LocalDateTime from one time-zone offset to another and returns the resulting [OffsetDateTime].
 *
 * @param fromOffset the initial time-zone offset of the LocalDateTime, defaults to the system's default zone ID
 * @param toOffset the target time-zone offset to which the LocalDateTime needs to be converted
 * @since 1.0.0
 */
fun LocalDateTime.atOffset(fromOffset: ZoneIdent, toOffset: ZoneIdent) =
    atZone(fromOffset).withZoneSameInstant(toOffset.offset).toOffsetDateTime()!!

/**
 * Combines this LocalDateTime instance with the specified time zone to create a ZonedDateTime.
 *
 * @param zone the ZoneIdent object representing the time zone to be applied to this LocalDateTime.
 * @since 1.0.0
 */
fun LocalDateTime.atZone(zone: ZoneIdent) = atZone(zone.zoneId)!!

/**
 * Converts the `LocalDateTime` to an `OffsetDateTime` by applying the specified time zones.
 *
 * This method first assigns the `fromZone` to the `LocalDateTime` and then converts
 * it to the specified `toZone` time zone, ensuring the same instant in time is represented.
 *
 * @param fromZone the source time zone to assign to the `LocalDateTime`. Defaults to the system default time zone.
 * @param toZone the target zone to which the instance should be converted.
 * @return the resulting `OffsetDateTime` with the specified zones applied.
 * @since 1.0.0
 */
fun LocalDateTime.atZone(fromZone: ZoneId = ZoneId.systemDefault(), toZone: ZoneIdent) =
    atZone(fromZone).withZoneSameInstant(toZone.zoneId).toOffsetDateTime()!!

/**
 * Converts this LocalDateTime from one time zone to another and returns the resulting OffsetDateTime.
 *
 * @param fromZone The ZoneIdent representing the original time zone of the LocalDateTime.
 * @param toZone The ZoneIdent representing the target time zone to which the LocalDateTime should be converted.
 * @since 1.0.0
 */
fun LocalDateTime.atZone(fromZone: ZoneIdent, toZone: ZoneIdent) =
    atZone(fromZone).withZoneSameInstant(toZone.zoneId).toOffsetDateTime()!!

/**
 * Converts this `Instant` to a `LocalDateTime` at the specified `ZoneOffset`,
 * adjusting from the given `ZoneId` if necessary.
 *
 * @param fromOffset The source time zone identifier used to interpret the `Instant`.
 * Defaults to the system's default time zone.
 * @param toOffset The target `ZoneOffset` to which the date-time will be adjusted.
 * @return A `LocalDateTime` representing the same moment in time as this `Instant`,
 * adjusted to the target time zone offset.
 * @since 1.0.0
 */
fun Instant.atOffset(fromOffset: ZoneId = ZoneId.systemDefault(), toOffset: ZoneOffset) =
    atZone(fromOffset).withZoneSameInstant(toOffset).toLocalDateTime()!!

/**
 * Converts this `Instant` to an `OffsetDateTime` at the specified offset.
 *
 * This method takes the given offset and combines it with the instant to produce
 * a corresponding `OffsetDateTime` representation. The offset determines the total
 * time-zone adjustment that the `Instant` would have in the resulting date-time.
 *
 * @param offset The `ZoneIdent` specifying the timezone offset to apply.
 * @return An `OffsetDateTime` object representing the `Instant` adjusted to the specified offset.
 * @since 1.0.0
 */
fun Instant.atOffset(offset: ZoneIdent) = atOffset(offset.offset)!!

/**
 * Adjusts the given `Instant` by converting to the specified zone offsets.
 *
 * Converts the `Instant` to the target offset from the original zone and returns an `OffsetDateTime`
 * representation based on the adjusted offset.
 *
 * @param fromOffset ZoneId representing the starting time zone. Defaults to the system default time zone.
 * @param toOffset ZoneIdent specifying the target offset for conversion.
 * @since 1.0.0
 */
fun Instant.atOffset(fromOffset: ZoneId = ZoneId.systemDefault(), toOffset: ZoneIdent) =
    atZone(fromOffset).withZoneSameInstant(toOffset.offset).toOffsetDateTime()!!

/**
 * Converts the given `Instant` to an `OffsetDateTime` at the specified target offset using the specified source offset.
 *
 * The method first converts the `Instant` to a `ZonedDateTime` using the source offset (`fromOffset`),
 * then adjusts the time to the same instant in the target offset (`toOffset`).
 * Finally, it returns the resulting `OffsetDateTime`.
 *
 * @param fromOffset the source time zone identifier for the conversion
 * @param toOffset the target time zone identifier for the conversion
 * @since 1.0.0
 */
fun Instant.atOffset(fromOffset: ZoneIdent, toOffset: ZoneIdent) =
    atZone(fromOffset).withZoneSameInstant(toOffset.offset).toOffsetDateTime()!!

/**
 * Converts this `Instant` to a `ZonedDateTime` at the specified time zone.
 *
 * @param zone the time zone to use for the conversion
 * @since 1.0.0
 */
fun Instant.atZone(zone: ZoneIdent) = atZone(zone.zoneId)!!

/**
 * Converts the current `Instant` instance to an `OffsetDateTime`, applying the specified time zones.
 *
 * @param fromZone the initial time zone to be applied to the `Instant`, defaults to the system's default time zone.
 * @param toZone the target time zone to which the instant's time will be converted.
 * @return the `OffsetDateTime` representing the instant in the specified target time zone.
 * @since 1.0.0
 */
fun Instant.atZone(fromZone: ZoneId = ZoneId.systemDefault(), toZone: ZoneIdent) =
    atZone(fromZone).withZoneSameInstant(toZone.zoneId).toOffsetDateTime()!!

/**
 * Converts this `Instant` from the given source time zone to the specified target time zone
 * and returns the corresponding `OffsetDateTime`.
 *
 * @param fromZone The time zone from which the instant is being converted.
 * @param toZone The target time zone to which the instant is being converted.
 *
 * @since 1.0.0
 */
fun Instant.atZone(fromZone: ZoneIdent, toZone: ZoneIdent) =
    atZone(fromZone).withZoneSameInstant(toZone.zoneId).toOffsetDateTime()!!

/**
 * Converts this `OffsetDateTime` to a `ZonedDateTime` at the same instant in the specified time zone.
 *
 * This method keeps the instant (point on the timeline) unchanged while adjusting the time zone,
 * ensuring the corresponding time in the target zone reflects the same moment.
 *
 * @param zoneId the target time zone to apply when adjusting this `OffsetDateTime`
 * @since 1.0.0
 */
fun OffsetDateTime.atZoneSameInstant(zoneId: ZoneIdent) = atZoneSameInstant(zoneId.zoneId)!!

/**
 * Combines this `OffsetDateTime` with a specified time-zone to create a `ZonedDateTime`,
 * attempting to retain the same local date-time when possible.
 * If the local date-time is unresolvable within the target time-zone due to reasons such as
 * daylight saving time gaps, exceptions may be thrown or adjustments will be applied
 * to resolve the local date-time.
 *
 * @param zoneId the target time-zone to be applied, not null
 * @since 1.0.0
 */
fun OffsetDateTime.atZoneSimilarLocal(zoneId: ZoneIdent) = atZoneSimilarLocal(zoneId.zoneId)!!

/**
 * Adjusts the LocalDate to the start of the day based on the specified time zone.
 *
 * @param zoneId the ZoneIdent representing the time zone to use for computing the start of the day
 * @since 1.0.0
 */
fun LocalDate.atStartOfDay(zoneId: ZoneIdent) = atStartOfDay(zoneId.zoneId)!!

/**
 * Converts the current `LocalDate` instance to a `LocalDateTime` set at the end of the day.
 * The resulting time will be 23 hours, 59 minutes, 59 seconds, and 999999999 nanoseconds.
 *
 * @return A `LocalDateTime` instance representing the end of the specified day.
 *
 * @since 1.0.0
 */
fun LocalDate.atEndOfDay() = atTime(23, 59, 59, 999999999)!!
/**
 * Converts the given `LocalDate` instance to the end of the day in the specified time zone.
 *
 * The end of the day is represented as the last moment of the date, up to but not including midnight.
 *
 * @param zoneId The time zone to consider when determining the end of the day.
 * @since 1.0.0
 */
fun LocalDate.atEndOfDay(zoneId: ZoneIdent) = atEndOfDay(zoneId.zoneId)
/**
 * Computes the ZonedDateTime representing the end of the day for the given LocalDate
 * in the specified time zone. If there is a gap due to a time zone transition,
 * the adjusted datetime after the gap is returned.
 *
 * @param zoneId the time zone ID to use for computing the ZonedDateTime
 * @return the ZonedDateTime representing the end of the current day in the specified zone
 * @since 1.0.0
 */
fun LocalDate.atEndOfDay(zoneId: ZoneId): ZonedDateTime {
    var ldt = atTime(LocalTime.MIDNIGHT)
    if (zoneId !is ZoneOffset) {
        val rules: ZoneRules = zoneId.rules
        val trans = rules.getTransition(ldt)
        if (trans.isNotNull() && trans.isGap) {
            ldt = trans.dateTimeAfter
        }
    }
    return ZonedDateTime.of(ldt, zoneId)
}

/**
 * Converts an integer to a [Year] instance.
 *
 * This extension function attempts to interpret the integer as a year and constructs a [Year] object.
 * If the integer does not represent a valid year, the result will be a [Result] encapsulating the exception.
 *
 * @receiver An integer representing the year.
 * @return A [Result] wrapping a [Year] object or an exception if the conversion fails.
 * @throws DateTimeException If the receiver is not a valid year.
 * @since 1.0.0
 */
fun Int.toYear() = runCatching { Year.of(this)!! }
/**
 * Converts the [Long] value to a [Year] instance if possible.
 * This method attempts to interpret the [Long] value as a year by converting it to an integer.
 * The conversion is wrapped in a result to handle potential exceptions,
 * such as when the [Long] value is not within the valid range of an integer.
 *
 * @receiver The [Long] value to be converted to a [Year].
 * @return A [Result] object containing the [Year] instance if the conversion succeeds,
 *         or a failure if an exception occurs.
 * @since 1.0.0
 */
fun Long.toYear() = runCatching { Year.of(toInt())!! }

/**
 * Converts an integer representation of a month to a [Month] enum value.
 *
 * This function attempts to map an integer value (1 to 12) to its corresponding
 * [Month] enumerated constant. The function safely wraps the conversion operation
 * using [runCatching], allowing it to handle invalid month numbers by encapsulating
 * the result as a [Result] type.
 *
 * @receiver The integer value to be converted to a [Month].
 * @return A [Result] containing the corresponding [Month] instance if the integer
 *         is valid, or a failure if the integer is out of range.
 * @throws DateTimeException If the integer is not in the range 1 to 12.
 * @since 1.0.0
 */
fun Int.toMonth() = runCatching { Month.of(this)!! }
/**
 * Converts the Long value to a `Month` enum, if possible.
 *
 * This function attempts to map the Long value to a valid `Month`
 * enumeration constant using its integer representation.
 * It returns a `Result` object, which may contain the `Month` value
 * if the conversion is successful or an exception if the conversion fails.
 *
 * @receiver Long value representing the numerical month (1 for January, 2 for February, etc.)
 * @return A `Result` encapsulating the `Month` enum if the conversion is valid, or an exception otherwise.
 *
 * @throws DateTimeException if the Long value does not correspond to a valid `Month`.
 *
 * @since 1.0.0
 */
fun Long.toMonth() = runCatching { Month.of(toInt())!! }

/**
 * Converts the current `Instant` to a `LocalDateTime` instance using the "Z" (UTC) time-zone designator.
 *
 * This method creates a `LocalDateTime` by combining the given `Instant` with the UTC time-zone.
 * The resultant `LocalDateTime` represents the same point in time, adjusted to the specified time-zone.
 *
 * @receiver the `Instant` to be converted to `LocalDateTime`.
 * @return a `LocalDateTime` representing the same moment in time in UTC.
 * @since 1.0.0
 */
fun Instant.toLocalDateTime() = LocalDateTime(this, TimeZoneDesignator.Z)
/**
 * Converts this `Instant` to an `OffsetDateTime` with a UTC (Z) time zone offset.
 *
 * The method interprets the provided `Instant` in the UTC (Z) time zone and creates
 * an `OffsetDateTime` representation of it.
 *
 * @receiver the `Instant` to be converted
 * @return an `OffsetDateTime` instance representing the same point in time as the `Instant`,
 * adjusted to the UTC (Z) time zone
 * @since 1.0.0
 */
fun Instant.toOffsetDateTime() = OffsetDateTime(this, TimeZoneDesignator.Z)

/**
 * Constructs an instance of `LocalDate` with the specified year, month, and day.
 *
 * @param year The year to represent in the date.
 * @param month The month to represent, as a number from 1 (January) to 12 (December).
 * @param day The day of the month to represent, from 1 to 31.
 * @since 1.0.0
 */
fun LocalDate(year: Int, month: Int, day: Int) = LocalDate.of(year, month, day)!!
/**
 * Constructs an instance of `LocalDate` with the specified year, month, and day.
 *
 * @param year the year to represent, from MIN_YEAR to MAX_YEAR
 * @param month the month-of-year to represent, not null
 * @param day the day-of-month to represent, from 1 to 31
 * @since 1.0.0
 */
fun LocalDate(year: Int, month: Month, day: Int) = LocalDate.of(year, month, day)!!
/**
 * Constructs an instance of `LocalDate` using the specified year, month, and day.
 *
 * @param year the year of the date to represent, not null
 * @param month the month of the year to represent, from 1 (January) to 12 (December)
 * @param day the day of the month to represent, from 1 to the maximum number of days in the specified month
 * @return a `LocalDate` instance representing the specified date
 * @since 1.0.0
 */
fun LocalDate(year: Year, month: Int, day: Int) = LocalDate.of(year.value, month, day)!!
/**
 * Creates an instance of `LocalDate` using the specified year, month, and day.
 *
 * @param year the year; must not be null
 * @param month the month of the year; must not be null
 * @param day the day of the month; must be valid for the given year and month
 * @return an instance of `LocalDate` representing the specified date
 * @throws DateTimeException if the day or month values are invalid or if the combination
 * of year, month, and day is invalid
 * @since 1.0.0
 */
fun LocalDate(year: Year, month: Month, day: Int) = LocalDate.of(year.value, month, day)!!
/**
 * Constructs an instance of `LocalDate` based on the year and day of the year.
 *
 * @param year the year to represent, from MIN_YEAR to MAX_YEAR.
 * @param day the day-of-year to represent, from 1 to 365-366 depending on the year.
 * @return a LocalDate instance representing the specified year and day.
 * @throws DateTimeException if the day-of-year is invalid for the given year.
 * @since 1.0.0
 */
fun LocalDate(year: Int, day: Int) = LocalDate.ofYearDay(year, day)!!
/**
 * Constructs a `LocalDate` using the specified `Year` and day of the year.
 *
 * @param year the `Year` instance representing the year for the date
 * @param day the day of the year, from 1 to 365 (or 366 for leap years)
 * @since 1.0.0
 */
fun LocalDate(year: Year, day: Int) = LocalDate.ofYearDay(year.value, day)!!
/**
 * Constructs a `LocalDate` from the given `Instant` and `ZoneId`.
 *
 * @param instant the instant to convert to a LocalDate, must not be null
 * @param zone the time-zone to use for the conversion, must not be null
 * @return a LocalDate representing the same date in the specified time-zone
 * @throws DateTimeException if the conversion fails due to invalid date or time-zone
 * @since 1.0.0
 */
fun LocalDate(instant: Instant, zone: ZoneId) = LocalDate.ofInstant(instant, zone)!!
/**
 * Creates an instance of `LocalDate` from a given `Instant` and `ZoneIdent`.
 * The method converts the specified instant to a local date in the provided time zone.
 *
 * @param instant the instant to be converted to a `LocalDate`
 * @param zone the time zone information used for the conversion
 * @since 1.0.0
 */
fun LocalDate(instant: Instant, zone: ZoneIdent) = LocalDate.ofInstant(instant, zone.zoneId)!!
/**
 * Creates a `LocalDate` instance from the given `Instant` and `ZoneId`.
 *
 * @param instant The `kotlin.time.Instant` representing a specific moment in time.
 * @param zone The `ZoneId` specifying the time zone to use for the conversion.
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun LocalDate(instant: kotlin.time.Instant, zone: ZoneId) = LocalDate.ofInstant(instant.toJavaInstant(), zone)!!
/**
 * Converts the given Kotlin Instant and ZoneIdent to a LocalDate.
 *
 * @param instant The instance of Kotlin Instant to be converted.
 * @param zone The timezone information encapsulated in a ZoneIdent object.
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun LocalDate(instant: kotlin.time.Instant, zone: ZoneIdent) = LocalDate.ofInstant(instant.toJavaInstant(), zone.zoneId)!!
/**
 * Constructs an instance of `LocalDate` using the epoch day count,
 * which is based on the number of days elapsed since 1970-01-01.
 *
 * @param epochDay the number of days since the epoch 1970-01-01, where negative values represent days before this date
 * @since 1.0.0
 */
fun LocalDate(epochDay: Long) = LocalDate.ofEpochDay(epochDay)!!
/**
 * Parses the given character sequence into a LocalDate using the specified DateTimeFormatter.
 * If parsing is unsuccessful, an exception is encapsulated in the Result.
 *
 * @param cs the character sequence to be parsed into a LocalDate
 * @param formatter the DateTimeFormatter to be used for parsing. Defaults to ISO_LOCAL_DATE if not provided
 * @return a Result containing the LocalDate if parsing succeeds, or an exception if it fails
 * @since 1.0.0
 */
fun LocalDate(cs: CharSequence, formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE): Result<LocalDate> = runCatching { LocalDate.parse(cs, formatter) }
/**
 * Creates an instance of `LocalDate` initialized to the current date.
 *
 * This method provides a concise way to retrieve the current date
 * according to the system clock in the default time-zone.
 *
 * @return The current date as a `LocalDate` instance.
 * @since 1.0.0
 */
fun LocalDate() = LocalDate.now()!!
/**
 * Creates a `LocalDate` instance based on the current date according to the specified clock.
 *
 * @param clock the clock to use for obtaining the current date
 * @since 1.0.0
 */
fun LocalDate(clock: Clock) = LocalDate.now(clock)!!
/**
 * Creates a `LocalDate` instance using the specified `ZoneId`.
 * This method retrieves the current date in the specified time-zone.
 *
 * @param zone the time-zone to use, not null
 * @since 1.0.0
 */
fun LocalDate(zone: ZoneId) = LocalDate.now(zone)!!
/**
 * Constructs a `LocalDate` instance using the current date in the specified time zone.
 *
 * @param zone the time zone identifier specifying the time zone to be used
 * @since 1.0.0
 */
fun LocalDate(zone: ZoneIdent) = LocalDate.now(zone.zoneId)!!

/**
 * Constructs a `LocalTime` instance from the provided hour, minute, second, and nanosecond parameters.
 *
 * @param hour the hour-of-day, from 0 to 23
 * @param minute the minute-of-hour, from 0 to 59
 * @param second the second-of-minute, from 0 to 59, default is 0
 * @param nano the nanosecond-of-second, from 0 to 999,999,999, default is 0
 * @since 1.0.0
 */
fun LocalTime(hour: Int, minute: Int, second: Int = 0, nano: Int = 0) = LocalTime.of(hour, minute, second, nano)!!
/**
 * Obtains an instance of LocalTime from an Instant and a time-zone.
 *
 * The Instant represents a point on the timeline, and the ZoneId specifies the time-zone.
 * This method converts the provided instant to a local time in the given time-zone.
 *
 * @param instant the instant to convert, not null
 * @param zone the time-zone to use, not null
 * @return the local time resulting from the given instant and time-zone
 * @since 1.0.0
 */
fun LocalTime(instant: Instant, zone: ZoneId): LocalTime = LocalTime.ofInstant(instant, zone)
/**
 * Constructs a `LocalTime` instance from the given `Instant` and `ZoneIdent`.
 *
 * @param instant the instant to convert, representing a point on the timeline
 * @param zone the time zone information used to interpret the instant
 * @return the local time corresponding to the instant and time zone provided
 * @since 1.0.0
 */
fun LocalTime(instant: Instant, zone: ZoneIdent): LocalTime = LocalTime.ofInstant(instant, zone.zoneId)
/**
 * Converts the given [instant] and [zone] into a `LocalTime`.
 *
 * @param instant The `kotlin.time.Instant` to be converted.
 * @param zone The time zone to be used for the conversion.
 * @return The resulting `LocalTime` object derived from the provided [instant] and [zone].
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun LocalTime(instant: kotlin.time.Instant, zone: ZoneId): LocalTime = LocalTime.ofInstant(instant.toJavaInstant(), zone)
/**
 * Converts a given `kotlin.time.Instant` to a `LocalTime` in the specified time zone.
 *
 * @param instant The instance of `kotlin.time.Instant` representing a specific moment in time.
 * @param zone The time zone represented by `ZoneIdent` where the resulting `LocalTime` will be computed.
 * @return A `LocalTime` object representing the time derived from the provided instant and zone.
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun LocalTime(instant: kotlin.time.Instant, zone: ZoneIdent): LocalTime = LocalTime.ofInstant(instant.toJavaInstant(), zone.zoneId)
/**
 * Parses a character sequence to a [LocalTime] using the specified [DateTimeFormatter].
 *
 * This method attempts to parse the provided character sequence into a [LocalTime] object
 * using the specified formatter. If the parsing fails, the result will contain the exception that occurred.
 *
 * @param cs the character sequence to be parsed into a [LocalTime]
 * @param formatter the formatter to use for parsing, defaults to [DateTimeFormatter.ISO_LOCAL_TIME]
 * @return a [Result] containing a [LocalTime] object if parsing is successful, otherwise an exception
 * @since 1.0.0
 */
fun LocalTime(cs: CharSequence, formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME): Result<LocalTime> = runCatching { LocalTime.parse(cs, formatter) }
/**
 * Returns the current time from the system clock in the default time-zone.
 *
 * This method retrieves the current time based on the system clock and the default time-zone.
 *
 * @return the current local time
 * @since 1.0.0
 */
fun LocalTime(): LocalTime = LocalTime.now()
/**
 * Creates an instance of `LocalTime` using the provided `Clock` object.
 *
 * This method extracts the current time from the specified clock and returns a `LocalTime`
 * instance representing that time.
 *
 * @param clock the `Clock` to retrieve the current time from
 * @return a `LocalTime` instance representing the current time from the given `Clock`
 * @since 1.0.0
 */
fun LocalTime(clock: Clock): LocalTime = LocalTime.now(clock)
/**
 * Retrieves the current time in the specified time zone.
 *
 * @param zone the time zone identifier for which the current time is to be obtained
 * @return the current time in the specified time zone
 * @since 1.0.0
 */
fun LocalTime(zone: ZoneId): LocalTime = LocalTime.now(zone)
/**
 * Returns the current local time for the specified time zone.
 *
 * @param zone the time zone identifier used to determine the current time
 * @return the current local time for the given time zone
 * @since 1.0.0
 */
fun LocalTime(zone: ZoneIdent): LocalTime = LocalTime.now(zone.zoneId)

/**
 * Creates an instance of `OffsetTime` from the given hour, minute, second, nanosecond, and time zone offset.
 *
 * @param hour the hour of the day, from 0 to 23
 * @param minute the minute of the hour, from 0 to 59
 * @param second the second of the minute, from 0 to 59 (default is 0)
 * @param nano the nanosecond of the second, from 0 to 999,999,999 (default is 0)
 * @param zoneOffset the time zone offset to apply
 * @since 1.0.0
 */
fun OffsetTime(hour: Int, minute: Int, second: Int = 0, nano: Int = 0, zoneOffset: ZoneOffset) = OffsetTime.of(hour, minute, second, nano, zoneOffset)!!
/**
 * Creates an instance of `OffsetTime` using the specified hour, minute, second, nanosecond,
 * and zone offset.
 *
 * @param hour the hour of the day, from 0 to 23
 * @param minute the minute of the hour, from 0 to 59
 * @param second the second of the minute, from 0 to 59, default is 0
 * @param nano the nanosecond of the second, from 0 to 999,999,999, default is 0
 * @param zoneOffset the zone offset information to define the offset time
 * @since 1.0.0
 */
fun OffsetTime(hour: Int, minute: Int, second: Int = 0, nano: Int = 0, zoneOffset: ZoneIdent) = OffsetTime.of(hour, minute, second, nano, zoneOffset.offset)!!
/**
 * Creates an instance of OffsetTime from the specified local time and zone offset.
 *
 * @param localTime the local time to represent, not null
 * @param zoneOffset the zone offset to use, not null
 * @return a valid instance of OffsetTime representing the specified local time and offset
 * @since 1.0.0
 */
fun OffsetTime(localTime: LocalTime, zoneOffset: ZoneOffset) = OffsetTime.of(localTime, zoneOffset)!!
/**
 * Constructs an instance of OffsetTime using the given local time and zone offset.
 *
 * @param localTime the local time component of the offset time
 * @param zoneOffset the zone offset to be applied to the local time
 * @since 1.0.0
 */
fun OffsetTime(localTime: LocalTime, zoneOffset: ZoneIdent) = OffsetTime.of(localTime, zoneOffset.offset)!!
/**
 * Converts the given instant and time zone to an OffsetTime instance.
 *
 * @param instant the instant to convert, must not be null
 * @param zone the time zone to apply, must not be null
 * @return the resulting OffsetTime instance
 * @since 1.0.0
 */
fun OffsetTime(instant: Instant, zone: ZoneId): OffsetTime = OffsetTime.ofInstant(instant, zone)
/**
 * Creates an `OffsetTime` instance based on the provided instant and zone.
 *
 * @param instant the instant to be converted to `OffsetTime`
 * @param zone the time zone to be applied for the conversion
 * @return an `OffsetTime` instance representing the given instant in the specified zone
 * @since 1.0.0
 */
fun OffsetTime(instant: Instant, zone: ZoneIdent): OffsetTime = OffsetTime.ofInstant(instant, zone.zoneId)
/**
 * Constructs an `OffsetTime` instance from the provided `Instant` and `ZoneId`.
 *
 * @param instant the instant in time from which the `OffsetTime` will be created
 * @param zone the time zone to associate with the provided instant
 * @return the `OffsetTime` calculated from the given instant and time zone
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun OffsetTime(instant: kotlin.time.Instant, zone: ZoneId): OffsetTime = OffsetTime.ofInstant(instant.toJavaInstant(), zone)
/**
 * Converts the given instant and zone information into an OffsetTime instance.
 *
 * @param instant The instant representing a specific point on the time-line, measured from the epoch.
 * @param zone The time zone identifier that determines the offset to use.
 * @return An OffsetTime instance representing the time adjusted to the specified zone.
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun OffsetTime(instant: kotlin.time.Instant, zone: ZoneIdent): OffsetTime = OffsetTime.ofInstant(instant.toJavaInstant(), zone.zoneId)
/**
 * Parses the given character sequence to an `OffsetTime` object using the specified formatter.
 *
 * @param cs the character sequence to parse into an `OffsetTime` object
 * @param formatter the formatter to use for parsing the character sequence,
 * defaults to `DateTimeFormatter.ISO_OFFSET_TIME` if not provided
 * @return a `Result` containing the successfully parsed `OffsetTime` object or an exception if parsing fails
 * @since 1.0.0
 */
fun OffsetTime(cs: CharSequence, formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_TIME): Result<OffsetTime> = runCatching { OffsetTime.parse(cs, formatter) }
/**
 * Returns the current offset time from the system clock in the default time-zone.
 *
 * This method combines the date-time from the system clock with the default time-zone offset
 * to produce an `OffsetTime` instance representing the current time with the offset.
 *
 * @return the current `OffsetTime` based on the system clock and default time-zone
 * @since 1.0.0
 */
fun OffsetTime(): OffsetTime = OffsetTime.now()
/**
 * Creates an instance of OffsetTime based on the current time from the provided clock.
 *
 * @param clock the clock to determine the current time, not null
 * @return an instance of OffsetTime representing the current time as per the specified clock
 * @since 1.0.0
 */
fun OffsetTime(clock: Clock): OffsetTime = OffsetTime.now(clock)
/**
 * Creates an instance of OffsetTime based on the current time in the specified time zone.
 *
 * @param zone the time zone to use for retrieving the current time
 * @return an instance of OffsetTime representing the current time in the specified time zone
 * @since 1.0.0
 */
fun OffsetTime(zone: ZoneId): OffsetTime = OffsetTime.now(zone)
/**
 * Creates an instance of OffsetTime based on the provided ZoneIdent.
 *
 * @param zone the ZoneIdent representing the time zone to be used for creating the OffsetTime
 * @return an OffsetTime instance representing the current offset time in the specified time zone
 * @since 1.0.0
 */
fun OffsetTime(zone: ZoneIdent): OffsetTime = OffsetTime.now(zone.zoneId)

/**
 * Creates an instance of [LocalDateTime] using the specified year, month, day, hour, minute, second, and nanosecond.
 *
 * @param year The year to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month-of-year to represent, not null.
 * @param dayOfMonth The day-of-month to represent, from 1 to 31.
 * @param hour The hour-of-day to represent, from 0 to 23.
 * @param minute The minute-of-hour to represent, from 0 to 59.
 * @param second The second-of-minute to represent, from 0 to 59. Defaults to 0.
 * @param nano The nano-of-second to represent, from 0 to 999,999,999. Defaults to 0.
 * @since 1.0.0
 */
fun LocalDateTime(year: Int, month: Month, dayOfMonth: Int, hour: Int, minute: Int, second: Int = 0, nano: Int = 0) = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nano)!!
/**
 * Constructs a `LocalDateTime` instance using the specified date and time values.
 *
 * @param year the year to represent, from MIN_YEAR to MAX_YEAR
 * @param month the month-of-year to represent, from 1 (January) to 12 (December)
 * @param dayOfMonth the day-of-month to represent, from 1 to 31
 * @param hour the hour-of-day to represent, from 0 to 23
 * @param minute the minute-of-hour to represent, from 0 to 59
 * @param second the second-of-minute to represent, from 0 to 59 (default is 0)
 * @param nano the nanosecond-of-second to represent, from 0 to 999,999,999 (default is 0)
 * @since 1.0.0
 */
fun LocalDateTime(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int = 0, nano: Int = 0) = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nano)!!
/**
 * Creates an instance of `LocalDateTime` using the specified date and time values.
 *
 * @param year The year as a `Year` object, representing the year of the date.
 * @param month The month as a `Month` object, representing the month of the year.
 * @param dayOfMonth The day of the month, with the range depending on the specific month and year.
 * @param hour The hour of the day, from 0 to 23.
 * @param minute The minute of the hour, from 0 to 59.
 * @param second The second of the minute, from 0 to 59. Defaults to 0 if not specified.
 * @param nano The nanosecond of the second, from 0 to 999,999,999. Defaults to 0 if not specified.
 * @since 1.0.0
 */
fun LocalDateTime(year: Year, month: Month, dayOfMonth: Int, hour: Int, minute: Int, second: Int = 0, nano: Int = 0) = LocalDateTime.of(year.value, month, dayOfMonth, hour, minute, second, nano)!!
/**
 * Constructs an instance of `LocalDateTime` using the provided year, month, day, hour, minute, second, and nanosecond values.
 *
 * @param year the year, as a `Year` instance, to represent in the `LocalDateTime`
 * @param month the month-of-year, from 1 (January) to 12 (December)
 * @param dayOfMonth the day-of-month, from 1 to 31
 * @param hour the hour-of-day, from 0 to 23
 * @param minute the minute-of-hour, from 0 to 59
 * @param second the second-of-minute, from 0 to 59, default is 0
 * @param nano the nanosecond-of-second, from 0 to 999,999,999, default is 0
 * @since 1.0.0
 */
fun LocalDateTime(year: Year, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int = 0, nano: Int = 0) = LocalDateTime.of(year.value, month, dayOfMonth, hour, minute, second, nano)!!
/**
 * Combines a specified {@link LocalDate} and {@link LocalTime} to create a new {@link LocalDateTime} instance.
 *
 * @param date the date part of the {@link LocalDateTime}, must not be null
 * @param time the time part of the {@link LocalDateTime}, must not be null
 * @since 1.0.0
 */
fun LocalDateTime(date: LocalDate, time: LocalTime) = LocalDateTime.of(date, time)!!
/**
 * Creates a `LocalDateTime` from the specified `Instant` and `ZoneId`.
 *
 * @param instant the instant to convert, not null
 * @param zone the time zone to use, not null
 * @return the local date-time, not null
 * @since 1.0.0
 */
fun LocalDateTime(instant: Instant, zone: ZoneId): LocalDateTime = LocalDateTime.ofInstant(instant, zone)
/**
 * Creates a `LocalDateTime` object from the given `Instant` and `ZoneIdent`.
 *
 * @param instant the `Instant` representing the exact point in time.
 * @param zone the `ZoneIdent` providing the time-zone information.
 * @return a new `LocalDateTime` instance representing the time-zone adjusted datetime.
 * @since 1.0.0
 */
fun LocalDateTime(instant: Instant, zone: ZoneIdent): LocalDateTime = LocalDateTime.ofInstant(instant, zone.zoneId)
/**
 * Converts a given `kotlin.time.Instant` and `ZoneId` into a `LocalDateTime` instance.
 *
 * @param instant The `kotlin.time.Instant` representing the precise moment in time.
 * @param zone The `ZoneId` representing the time zone to apply to the instant.
 * @return A `LocalDateTime` instance representing the specified instant adjusted to the given time zone.
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun LocalDateTime(instant: kotlin.time.Instant, zone: ZoneId): LocalDateTime = LocalDateTime.ofInstant(instant.toJavaInstant(), zone)
/**
 * Creates a `LocalDateTime` from the provided `Instant` and `ZoneIdent`.
 *
 * @param instant The `Instant` representing a point in time to be converted.
 * @param zone The `ZoneIdent` specifying the time-zone rules to use for the conversion.
 * @return The corresponding `LocalDateTime` in the specified time-zone.
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun LocalDateTime(instant: kotlin.time.Instant, zone: ZoneIdent): LocalDateTime = LocalDateTime.ofInstant(instant.toJavaInstant(), zone.zoneId)
/**
 * Parses the given character sequence to a `LocalDateTime` object using the provided `DateTimeFormatter`.
 * Returns the result of the parsing operation, either a successful `LocalDateTime` or an exception.
 *
 * @param cs the character sequence representing the date-time to be parsed
 * @param formatter the `DateTimeFormatter` used to parse the character sequence, defaults to `DateTimeFormatter.ISO_LOCAL_DATE_TIME`
 * @return a `Result` containing the parsed `LocalDateTime` if successful, or an exception if parsing fails
 * @since 1.0.0
 */
fun LocalDateTime(cs: CharSequence, formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME): Result<LocalDateTime> = runCatching { LocalDateTime.parse(cs, formatter) }
/**
 * Provides the current date and time as a LocalDateTime instance.
 *
 * @return The current date and time represented by a LocalDateTime object.
 * @since 1.0.0
 */
fun LocalDateTime(): LocalDateTime = LocalDateTime.now()
/**
 * Creates a `LocalDateTime` instance based on the current time from the given clock.
 *
 * @param clock the clock providing the current time. It must not be null.
 * @return a `LocalDateTime` instance representing the current date-time provided by the clock.
 * @since 1.0.0
 */
fun LocalDateTime(clock: Clock): LocalDateTime = LocalDateTime.now(clock)
/**
 * Creates a LocalDateTime object initialized with the current date and time in the specified time zone.
 *
 * @param zone the time zone identifier to retrieve the current date and time for
 * @return a LocalDateTime instance set to the current date and time in the provided time zone
 * @since 1.0.0
 */
fun LocalDateTime(zone: ZoneId): LocalDateTime = LocalDateTime.now(zone)
/**
 * Creates a `LocalDateTime` instance set to the current date and time in the specified time zone.
 *
 * @param zone the time zone to be used for determining the current date and time
 * @return a `LocalDateTime` instance reflecting the current date and time in the specified time zone
 * @since 1.0.0
 */
fun LocalDateTime(zone: ZoneIdent): LocalDateTime = LocalDateTime.now(zone.zoneId)

/**
 * Combines a LocalDate, LocalTime, and a ZoneIdent to create an instance of OffsetDateTime.
 *
 * @param date the LocalDate instance representing the date part
 * @param time the LocalTime instance representing the time part
 * @param offset the offset
 * @since 1.0.0
 */
fun OffsetDateTime(date: LocalDate, time: LocalTime, offset: ZoneOffset) = OffsetDateTime.of(date, time, offset)!!
/**
 * Combines a LocalDate, LocalTime, and a ZoneIdent to create an instance of OffsetDateTime.
 *
 * @param date the LocalDate instance representing the date part
 * @param time the LocalTime instance representing the time part
 * @param offset the ZoneIdent instance representing the time-zone offset
 * @since 1.0.0
 */
fun OffsetDateTime(date: LocalDate, time: LocalTime, offset: ZoneIdent) = OffsetDateTime.of(date, time, offset.offset)!!
/**
 * Creates an instance of `OffsetDateTime` using the specified `LocalDateTime` and `ZoneIdent`.
 *
 * This function is a convenience for creating an `OffsetDateTime` with the given date-time and offset.
 *
 * @param dateTime the `LocalDateTime` to represent the date and time without an associated offset, not null
 * @param offset the offset, not null
 * @since 1.0.0
 */
fun OffsetDateTime(dateTime: LocalDateTime, offset: ZoneOffset) = OffsetDateTime.of(dateTime, offset)!!
/**
 * Creates an instance of `OffsetDateTime` using the specified `LocalDateTime` and `ZoneIdent`.
 *
 * This function is a convenience for creating an `OffsetDateTime` with the given date-time and offset.
 *
 * @param dateTime the `LocalDateTime` to represent the date and time without an associated offset, not null
 * @param offset the `ZoneIdent` whose offset will be used, not null
 * @since 1.0.0
 */
fun OffsetDateTime(dateTime: LocalDateTime, offset: ZoneIdent) = OffsetDateTime.of(dateTime, offset.offset)!!
/**
 * Creates an instance of `OffsetDateTime` using the provided date, time, and time offset information.
 *
 * @param year The year of the date-time to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month of the date-time to represent, from 1 (January) to 12 (December).
 * @param dayOfMonth The day of the month of the date-time to represent, from 1 to 31 depending on the month.
 * @param hour The hour of the time to represent, from 0 to 23.
 * @param minute The minute of the time to represent, from 0 to 59.
 * @param second The second of the time to represent, from 0 to 59. Defaults to 0.
 * @param nanoOfSecond The nanosecond of the time to represent, from 0 to 999,999,999. Defaults to 0.
 * @param offset The offset.
 *
 * @since 1.0.0
 */
fun OffsetDateTime(
    year: Int, month: Int, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, offset: ZoneOffset
) = OffsetDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, offset)!!
/**
 * Creates an instance of `OffsetDateTime` using the provided date, time, and time offset information.
 *
 * @param year The year of the date-time to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month of the date-time to represent, from 1 (January) to 12 (December).
 * @param dayOfMonth The day of the month of the date-time to represent, from 1 to 31 depending on the month.
 * @param hour The hour of the time to represent, from 0 to 23.
 * @param minute The minute of the time to represent, from 0 to 59.
 * @param second The second of the time to represent, from 0 to 59. Defaults to 0.
 * @param nanoOfSecond The nanosecond of the time to represent, from 0 to 999,999,999. Defaults to 0.
 * @param offset The offset.
 *
 * @since 1.0.0
 */
fun OffsetDateTime(
    year: Year, month: Int, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, offset: ZoneOffset
) = OffsetDateTime.of(year.value, month, dayOfMonth, hour, minute, second, nanoOfSecond, offset)!!
/**
 * Creates an instance of `OffsetDateTime` using the provided date, time, and time offset information.
 *
 * @param year The year of the date-time to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month of the date-time to represent, from 1 (January) to 12 (December).
 * @param dayOfMonth The day of the month of the date-time to represent, from 1 to 31 depending on the month.
 * @param hour The hour of the time to represent, from 0 to 23.
 * @param minute The minute of the time to represent, from 0 to 59.
 * @param second The second of the time to represent, from 0 to 59. Defaults to 0.
 * @param nanoOfSecond The nanosecond of the time to represent, from 0 to 999,999,999. Defaults to 0.
 * @param offset The time-zone offset to be applied, represented as a `ZoneIdent`.
 *
 * @since 1.0.0
 */
fun OffsetDateTime(
    year: Int, month: Int, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, offset: ZoneIdent
) = OffsetDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, offset.offset)!!
/**
 * Creates an instance of `OffsetDateTime` using the provided date, time, and time offset information.
 *
 * @param year The year of the date-time to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month of the date-time to represent, from 1 (January) to 12 (December).
 * @param dayOfMonth The day of the month of the date-time to represent, from 1 to 31 depending on the month.
 * @param hour The hour of the time to represent, from 0 to 23.
 * @param minute The minute of the time to represent, from 0 to 59.
 * @param second The second of the time to represent, from 0 to 59. Defaults to 0.
 * @param nanoOfSecond The nanosecond of the time to represent, from 0 to 999,999,999. Defaults to 0.
 * @param offset The time-zone offset to be applied, represented as a `ZoneIdent`.
 *
 * @since 1.0.0
 */
fun OffsetDateTime(
    year: Year, month: Int, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, offset: ZoneIdent
) = OffsetDateTime.of(year.value, month, dayOfMonth, hour, minute, second, nanoOfSecond, offset.offset)!!
/**
 * Creates an instance of `OffsetDateTime` using the provided date, time, and time offset information.
 *
 * @param year The year of the date-time to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month of the date-time to represent, from 1 (January) to 12 (December).
 * @param dayOfMonth The day of the month of the date-time to represent, from 1 to 31 depending on the month.
 * @param hour The hour of the time to represent, from 0 to 23.
 * @param minute The minute of the time to represent, from 0 to 59.
 * @param second The second of the time to represent, from 0 to 59. Defaults to 0.
 * @param nanoOfSecond The nanosecond of the time to represent, from 0 to 999,999,999. Defaults to 0.
 * @param offset The offset.
 *
 * @since 1.0.0
 */
fun OffsetDateTime(
    year: Int, month: Month, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, offset: ZoneOffset
) = OffsetDateTime.of(year, month.value, dayOfMonth, hour, minute, second, nanoOfSecond, offset)!!
/**
 * Creates an instance of `OffsetDateTime` using the provided date, time, and time offset information.
 *
 * @param year The year of the date-time to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month of the date-time to represent, from 1 (January) to 12 (December).
 * @param dayOfMonth The day of the month of the date-time to represent, from 1 to 31 depending on the month.
 * @param hour The hour of the time to represent, from 0 to 23.
 * @param minute The minute of the time to represent, from 0 to 59.
 * @param second The second of the time to represent, from 0 to 59. Defaults to 0.
 * @param nanoOfSecond The nanosecond of the time to represent, from 0 to 999,999,999. Defaults to 0.
 * @param offset The offset.
 *
 * @since 1.0.0
 */
fun OffsetDateTime(
    year: Year, month: Month, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, offset: ZoneOffset
) = OffsetDateTime.of(year.value, month.value, dayOfMonth, hour, minute, second, nanoOfSecond, offset)!!
/**
 * Creates an instance of `OffsetDateTime` using the provided date, time, and time offset information.
 *
 * @param year The year of the date-time to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month of the date-time to represent, from 1 (January) to 12 (December).
 * @param dayOfMonth The day of the month of the date-time to represent, from 1 to 31 depending on the month.
 * @param hour The hour of the time to represent, from 0 to 23.
 * @param minute The minute of the time to represent, from 0 to 59.
 * @param second The second of the time to represent, from 0 to 59. Defaults to 0.
 * @param nanoOfSecond The nanosecond of the time to represent, from 0 to 999,999,999. Defaults to 0.
 * @param offset The time-zone offset to be applied, represented as a `ZoneIdent`.
 *
 * @since 1.0.0
 */
fun OffsetDateTime(
    year: Int, month: Month, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, offset: ZoneIdent
) = OffsetDateTime.of(year, month.value, dayOfMonth, hour, minute, second, nanoOfSecond, offset.offset)!!
/**
 * Creates an instance of `OffsetDateTime` using the provided date, time, and time offset information.
 *
 * @param year The year of the date-time to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month of the date-time to represent, from 1 (January) to 12 (December).
 * @param dayOfMonth The day of the month of the date-time to represent, from 1 to 31 depending on the month.
 * @param hour The hour of the time to represent, from 0 to 23.
 * @param minute The minute of the time to represent, from 0 to 59.
 * @param second The second of the time to represent, from 0 to 59. Defaults to 0.
 * @param nanoOfSecond The nanosecond of the time to represent, from 0 to 999,999,999. Defaults to 0.
 * @param offset The time-zone offset to be applied, represented as a `ZoneIdent`.
 *
 * @since 1.0.0
 */
fun OffsetDateTime(
    year: Year, month: Month, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, offset: ZoneIdent
) = OffsetDateTime.of(year.value, month.value, dayOfMonth, hour, minute, second, nanoOfSecond, offset.offset)!!
/**
 * Creates an instance of OffsetDateTime from the given instant and time-zone.
 *
 * @param instant the instant to use to create the date-time, not null
 * @param zone the time-zone to apply, not null
 * @return the created OffsetDateTime
 * @since 1.0.0
 */
fun OffsetDateTime(instant: Instant, zone: ZoneId): OffsetDateTime = OffsetDateTime.ofInstant(instant, zone)
/**
 * Creates an `OffsetDateTime` from the given `Instant` and `ZoneIdent`.
 *
 * The method converts the specified instant to an `OffsetDateTime` using
 * the provided time zone information.
 *
 * @param instant the instant to convert, not null
 * @param zone the time zone identifier to use, not null
 * @return the resulting `OffsetDateTime` instance
 * @since 1.0.0
 */
fun OffsetDateTime(instant: Instant, zone: ZoneIdent): OffsetDateTime = OffsetDateTime.ofInstant(instant, zone.zoneId)
/**
 * Creates an `OffsetDateTime` instance from the given `Instant` and `ZoneId`.
 *
 * @param instant The `Instant` representing the specific point in time.
 * @param zone The `ZoneId` representing the time zone to associate with the instant.
 * @return An `OffsetDateTime` instance corresponding to the provided `Instant` and `ZoneId`.
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun OffsetDateTime(instant: kotlin.time.Instant, zone: ZoneId): OffsetDateTime = OffsetDateTime.ofInstant(instant.toJavaInstant(), zone)
/**
 * Creates an `OffsetDateTime` from the given `Instant` and `ZoneIdent`.
 *
 * @param instant the instant to convert, representing a point on the timeline.
 * @param zone the time zone to apply to the instant.
 * @return an `OffsetDateTime` representing the instant at the specified zone offset.
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun OffsetDateTime(instant: kotlin.time.Instant, zone: ZoneIdent): OffsetDateTime = OffsetDateTime.ofInstant(instant.toJavaInstant(), zone.zoneId)
/**
 * Parses the given character sequence into an OffsetDateTime object using the specified formatter.
 *
 * @param cs the character sequence to parse, representing a date-time with an offset.
 * @param formatter the DateTimeFormatter to use for parsing, defaults to ISO_OFFSET_DATE_TIME.
 * @return a Result containing the parsed OffsetDateTime if successful, or an exception if parsing fails.
 * @since 1.0.0
 */
fun OffsetDateTime(cs: CharSequence, formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME): Result<OffsetDateTime> = runCatching { OffsetDateTime.parse(cs, formatter) }
/**
 * Creates a new instance of `OffsetDateTime` representing the current date-time
 * with an offset from UTC/Greenwich.
 *
 * The returned `OffsetDateTime` is initialized with the system's current clock and default time zone.
 *
 * @return the current `OffsetDateTime` instance
 * @since 1.0.0
 */
fun OffsetDateTime(): OffsetDateTime = OffsetDateTime.now()
/**
 * Creates an instance of OffsetDateTime using the current instant from the specified clock.
 *
 * @param clock the clock providing the current instant and time-zone, not null
 * @return an OffsetDateTime representing the current instant from the specified clock
 * @since 1.0.0
 */
fun OffsetDateTime(clock: Clock): OffsetDateTime = OffsetDateTime.now(clock)
/**
 * Returns an instance of OffsetDateTime for the current moment in the specified time zone.
 *
 * @param zone the time zone ID used to create the OffsetDateTime instance.
 * @return a new instance of OffsetDateTime set to the current time in the specified time zone.
 * @since 1.0.0
 */
fun OffsetDateTime(zone: ZoneId): OffsetDateTime = OffsetDateTime.now(zone)
/**
 * Obtains the current date-time from the system clock in the specified time-zone.
 *
 * @param zone the time-zone to use, not null
 * @return the current date-time using the specified time-zone, not null
 * @since 1.0.0
 */
fun OffsetDateTime(zone: ZoneIdent): OffsetDateTime = OffsetDateTime.now(zone.zoneId)

/**
 * Constructs a {@code ZonedDateTime} based on the specified date, time, and time zone.
 *
 * @param date the local date to represent, not null.
 * @param time the local time to represent, not null.
 * @param zone the time zone to use, not null.
 * @since 1.0.0
 */
fun ZonedDateTime(date: LocalDate, time: LocalTime, zone: ZoneId) = ZonedDateTime.of(date, time, zone)!!
/**
 * Creates a `ZonedDateTime` instance from the provided date, time, and zone information.
 *
 * @param date the `LocalDate` representing the date component
 * @param time the `LocalTime` representing the time component
 * @param zone the `ZoneIdent` representing the time zone
 * @since 1.0.0
 */
fun ZonedDateTime(date: LocalDate, time: LocalTime, zone: ZoneIdent) = ZonedDateTime.of(date, time, zone.zoneId)!!
/**
 * Constructs a `ZonedDateTime` object by combining a `LocalDateTime` and a `ZoneId`.
 *
 * @param dateTime the local date-time to combine with a time-zone to create a ZonedDateTime
 * @param zone the time-zone to apply, which may be an offset
 * @since 1.0.0
 */
fun ZonedDateTime(dateTime: LocalDateTime, zone: ZoneId) = ZonedDateTime.of(dateTime, zone)!!
/**
 * Constructs an instance of ZonedDateTime by combining a LocalDateTime and a ZoneIdent.
 *
 * @param dateTime the local date-time object to represent, not null
 * @param zone the zone identifier specifying the time zone rules, not null
 * @since 1.0.0
 */
fun ZonedDateTime(dateTime: LocalDateTime, zone: ZoneIdent) = ZonedDateTime.of(dateTime, zone.zoneId)!!
/**
 * Creates a ZonedDateTime instance with the specified date, time, and time zone information.
 *
 * @param year the year to represent, from MIN_YEAR to MAX_YEAR.
 * @param month the month-of-year to represent, from 1 (January) to 12 (December).
 * @param dayOfMonth the day-of-month to represent, from 1 to 31.
 * @param hour the hour-of-day to represent, from 0 to 23.
 * @param minute the minute-of-hour to represent, from 0 to 59.
 * @param second the second-of-minute to represent, from 0 to 59, default is 0.
 * @param nanoOfSecond the nanosecond-of-second to represent, from 0 to 999,999,999, default is 0.
 * @param zone the time-zone, which is a unique identifier for a specific time-zone region.
 * @return a ZonedDateTime instance reflecting the specified date-time and zone.
 * @throws DateTimeException if the values are outside the valid ranges for a ZonedDateTime.
 * @since 1.0.0
 */
fun ZonedDateTime(
    year: Int, month: Int, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, zone: ZoneId
) = ZonedDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, zone)!!
/**
 * Creates a `ZonedDateTime` instance from the specified date-time fields and time-zone.
 * The created `ZonedDateTime` represents the specified local date and time
 * in the provided zone with nanosecond precision.
 *
 * @param year The year to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month-of-year to represent, from 1 (January) to 12 (December).
 * @param dayOfMonth The day-of-month to represent, from 1 to 31, depending on the month and year.
 * @param hour The hour-of-day to represent, from 0 to 23.
 * @param minute The minute-of-hour to represent, from 0 to 59.
 * @param second The second-of-minute to represent, from 0 to 59. Default value is 0.
 * @param nanoOfSecond The nanosecond-of-second to represent, from 0 to 999,999,999. Default value is 0.
 * @param zone The time-zone, not null, which determines the time-zone rules to apply.
 * @since 1.0.0
 */
fun ZonedDateTime(
    year: Int, month: Int, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, zone: ZoneIdent
) = ZonedDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, zone.zoneId)!!
/**
 * Constructs an instance of ZonedDateTime from the specified date and time details and zone information.
 *
 * @param year The year to represent, not null.
 * @param month The month-of-year to represent, from 1 (January) to 12 (December).
 * @param dayOfMonth The day-of-month to represent, from 1 to 31.
 * @param hour The hour-of-day to represent, from 0 to 23.
 * @param minute The minute-of-hour to represent, from 0 to 59.
 * @param second The second-of-minute to represent, from 0 to 59. Defaults to 0 if not provided.
 * @param nanoOfSecond The nanosecond-of-second to represent, from 0 to 999,999,999. Defaults to 0 if not provided.
 * @param zone The time-zone, not null.
 * @since 1.0.0
 */
fun ZonedDateTime(
    year: Year, month: Int, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, zone: ZoneId
) = ZonedDateTime.of(year.value, month, dayOfMonth, hour, minute, second, nanoOfSecond, zone)!!
/**
 * Constructs an instance of `ZonedDateTime` using the provided year, month, day, time, and time zone.
 *
 * @param year The year component of the date.
 * @param month The month component of the date (from 1 to 12).
 * @param dayOfMonth The day of the month component of the date.
 * @param hour The hour component of the time (from 0 to 23).
 * @param minute The minute component of the time (from 0 to 59).
 * @param second The second component of the time (from 0 to 59), defaults to 0 if not specified.
 * @param nanoOfSecond The nanosecond component of the time, defaults to 0 if not specified.
 * @param zone The time zone identifier for the `ZonedDateTime`.
 * @since 1.0.0
 */
fun ZonedDateTime(
    year: Year, month: Int, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, zone: ZoneIdent
) = ZonedDateTime.of(year.value, month, dayOfMonth, hour, minute, second, nanoOfSecond, zone.zoneId)!!
/**
 * Creates an instance of `ZonedDateTime` using the provided parameters.
 *
 * @param year The year to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month-of-year, not null.
 * @param dayOfMonth The day-of-month to represent, from 1 to 31.
 * @param hour The hour-of-day to represent, from 0 to 23.
 * @param minute The minute-of-hour to represent, from 0 to 59.
 * @param second The second-of-minute to represent, from 0 to 59. Default is 0.
 * @param nanoOfSecond The nanosecond within the second to represent, from 0 to 999,999,999. Default is 0.
 * @param zone The time-zone, not null.
 * @since 1.0.0
 */
fun ZonedDateTime(
    year: Int, month: Month, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, zone: ZoneId
) = ZonedDateTime.of(year, month.value, dayOfMonth, hour, minute, second, nanoOfSecond, zone)!!
/**
 * Constructs a ZonedDateTime instance with the specified year, month, day, time, and time zone.
 *
 * @param year the year to represent, from MIN_YEAR to MAX_YEAR
 * @param month the month-of-year, not null
 * @param dayOfMonth the day-of-month, from 1 to 31
 * @param hour the hour-of-day, from 0 to 23
 * @param minute the minute-of-hour, from 0 to 59
 * @param second the second-of-minute, from 0 to 59, default is 0
 * @param nanoOfSecond the nanosecond-of-second, from 0 to 999,999,999, default is 0
 * @param zone the time-zone to use, not null
 * @since 1.0.0
 */
fun ZonedDateTime(
    year: Int, month: Month, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, zone: ZoneIdent
) = ZonedDateTime.of(year, month.value, dayOfMonth, hour, minute, second, nanoOfSecond, zone.zoneId)!!
/**
 * Creates an instance of `ZonedDateTime` with the specified date-time fields and zone ID.
 *
 * @param year the year, specified as a `Year` object
 * @param month the month, specified as a `Month` object
 * @param dayOfMonth the day of the month, from 1 to 31
 * @param hour the hour of the day, from 0 to 23
 * @param minute the minute of the hour, from 0 to 59
 * @param second the second of the minute, from 0 to 59 (default is 0)
 * @param nanoOfSecond the nanosecond of the second, from 0 to 999,999,999 (default is 0)
 * @param zone the time zone, specified as a `ZoneId` object
 * @return a non-null instance of `ZonedDateTime`
 * @since 1.0.0
 */
fun ZonedDateTime(
    year: Year, month: Month, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, zone: ZoneId
) = ZonedDateTime.of(year.value, month.value, dayOfMonth, hour, minute, second, nanoOfSecond, zone)!!
/**
 * Constructs a new instance of ZonedDateTime with the specified date, time, and time zone information.
 *
 * @param year The year of the date, represented as a Year object.
 * @param month The month of the date, represented as a Month object.
 * @param dayOfMonth The day of the month of the date, specified as an integer.
 * @param hour The hour of the time, specified as an integer.
 * @param minute The minute of the time, specified as an integer.
 * @param second The second of the time, specified as an integer. Defaults to 0.
 * @param nanoOfSecond The nanosecond of the time, specified as an integer. Defaults to 0.
 * @param zone The time zone information, represented as a ZoneIdent object.
 * @since 1.0.0
 */
fun ZonedDateTime(
    year: Year, month: Month, dayOfMonth: Int,
    hour: Int, minute: Int, second: Int = 0, nanoOfSecond: Int = 0, zone: ZoneIdent
) = ZonedDateTime.of(year.value, month.value, dayOfMonth, hour, minute, second, nanoOfSecond, zone.zoneId)!!
/**
 * Constructs a `ZonedDateTime` from the given `Instant` and `ZoneId`.
 *
 * This function combines the specified instant with the time zone represented by the given `ZoneId` to produce
 * a `ZonedDateTime` object that is fully aware of its offset and zone rules.
 *
 * @param instant the instant to represent, not null
 * @param zone the time-zone, not null
 * @return a `ZonedDateTime` instance representing the same point on the timeline as the provided `Instant`,
 * adjusted to the rules of the given time-zone
 * @since 1.0.0
 */
fun ZonedDateTime(instant: Instant, zone: ZoneId): ZonedDateTime = ZonedDateTime.ofInstant(instant, zone)
/**
 * Creates a `ZonedDateTime` instance from the specified `Instant` and `ZoneIdent`.
 *
 * @param instant the instant in time from which the zoned date-time is to be created
 * @param zone the zone identifier that will determine the zone offset and rules
 * @return a `ZonedDateTime` instance representing the instant adjusted to the specified zone
 * @since 1.0.0
 */
fun ZonedDateTime(instant: Instant, zone: ZoneIdent): ZonedDateTime = ZonedDateTime.ofInstant(instant, zone.zoneId)
/**
 * Constructs a `ZonedDateTime` instance representing the given instant in the specified time zone.
 *
 * @param instant The instant of time to be converted, represented using `kotlin.time.Instant`.
 * @param zone The time zone in which the `ZonedDateTime` should be represented, specified as a `ZoneId`.
 * @return A `ZonedDateTime` instance configured for the provided instant and time zone.
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun ZonedDateTime(instant: kotlin.time.Instant, zone: ZoneId): ZonedDateTime = ZonedDateTime.ofInstant(instant.toJavaInstant(), zone)
/**
 * Creates a ZonedDateTime instance from the given instant and time zone.
 *
 * @param instant The instant from which to create the ZonedDateTime.
 * @param zone The time zone to apply to the instant.
 * @return A ZonedDateTime object representing the given instant in the specified time zone.
 * @since 1.0.0
 */
@OptIn(ExperimentalTime::class)
fun ZonedDateTime(instant: kotlin.time.Instant, zone: ZoneIdent): ZonedDateTime = ZonedDateTime.ofInstant(instant.toJavaInstant(), zone.zoneId)
/**
 * Parses the given character sequence into a ZonedDateTime using the specified formatter.
 *
 * @param cs the character sequence to parse
 * @param formatter the DateTimeFormatter to use for parsing; defaults to ISO_ZONED_DATE_TIME
 * @return a Result containing the successfully parsed ZonedDateTime or a failure if parsing fails
 * @since 1.0.0
 */
fun ZonedDateTime(cs: CharSequence, formatter: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME): Result<ZonedDateTime> = runCatching { ZonedDateTime.parse(cs, formatter) }
/**
 * Creates and returns the current date-time with a time zone from the system clock in the default time zone.
 *
 * @return A ZonedDateTime representing the current date and time in the default time zone.
 * @since 1.0.0
 */
fun ZonedDateTime(): ZonedDateTime = ZonedDateTime.now()
/**
 * Creates a `ZonedDateTime` instance based on the provided clock.
 *
 * @param clock the clock to be used for creating the `ZonedDateTime` instance. It provides the current date-time
 * and timezone information.
 * @return a `ZonedDateTime` instance set to the current date-time and zone provided by the given clock.
 * @since 1.0.0
 */
fun ZonedDateTime(clock: Clock): ZonedDateTime = ZonedDateTime.now(clock)
/**
 * Creates a `ZonedDateTime` instance representing the current date and time in the specified time zone.
 *
 * @param zone the time-zone identifier, such as 'Europe/Paris', not null
 * @return a `ZonedDateTime` representing the current date and time in the specified zone
 * @since 1.0.0
 */
fun ZonedDateTime(zone: ZoneId): ZonedDateTime = ZonedDateTime.now(zone)
/**
 * Creates a new instance of ZonedDateTime using the provided ZoneIdent.
 *
 * @param zone the ZoneIdent specifying the time zone to be used when creating the ZonedDateTime instance
 * @return a ZonedDateTime instance set to the current date-time in the specified time zone
 * @since 1.0.0
 */
fun ZonedDateTime(zone: ZoneIdent): ZonedDateTime = ZonedDateTime.now(zone.zoneId)

/**
 * Creates a `LocalMonthDayTime` instance by parsing the provided character sequence.
 *
 * @param cs The input character sequence representing the month, day, and optional time information.
 *           The sequence can conform to various formats, such as:
 *           - `MM-dd` or `MM-ddTHH:mm` for plain month-day and optional time.
 *           - `-MM-dd` or `-MM-ddTHH:mm` for month-day with a leading dash and optional time.
 *           - `--MM-dd` or `--MM-ddTHH:mm` for month-day in ISO representation and optional time.
 *           - An empty or blank sequence defaults to the current date and time.
 * @since 1.0.0
 */
fun LocalMonthDayTime(cs: CharSequence) = LocalMonthDayTime.parse(cs)
/**
 * Creates an instance of [OffsetMonthDayTime] by parsing the provided character sequence.
 *
 * The character sequence should represent a local month-day-time combined with an offset.
 * If the sequence ends with 'Z', it is interpreted as having a UTC offset. If the sequence
 * is blank, the method behavior depends on the underlying implementation of the `parse` function.
 *
 * @param cs the character sequence to parse, representing a local month-day-time with an offset
 * @since 1.0.0
 */
fun OffsetMonthDayTime(cs: CharSequence) = OffsetMonthDayTime.parse(cs)
/**
 * Parses the given [CharSequence] into a [ZonedMonthDayTime] instance. The input can include
 * timezone or offset information. If the input is blank, the current date and time (based on
 * the current timezone) is used.
 *
 * @param cs the character sequence to be parsed into a [ZonedMonthDayTime]. It can include a
 * timezone in square brackets or an offset.
 * @since 1.0.0
 */
fun ZonedMonthDayTime(cs: CharSequence) = ZonedMonthDayTime.parse(cs)

/**
 * Creates an instance of the Year class using the given integer value.
 *
 * @param value The year value to be used for creating the Year instance.
 * @return A non-null instance of the Year class representing the specified year.
 * @throws DateTimeException if the year value is invalid.
 * @since 1.0.0
 */
fun Year(value: Int) = Year.of(value)!!

/**
 * Constructs a YearMonth instance using the specified year and month.
 *
 * @param year the year to represent, from MIN_YEAR to MAX_YEAR
 * @param month the month-of-year to represent, not null
 * @return the YearMonth instance created from the given year and month
 * @since 1.0.0
 */
fun YearMonth(year: Int, month: Month) = YearMonth.of(year, month.value)!!
/**
 * Creates an instance of YearMonth from the specified year and month.
 *
 * @param year The year to represent, from MIN_YEAR to MAX_YEAR.
 * @param month The month-of-year to represent, from 1 (January) to 12 (December).
 * @return The YearMonth instance representing the specified year and month, not null.
 * @since 1.0.0
 */
fun YearMonth(year: Int, month: Int) = YearMonth.of(year, month)!!
/**
 * Creates an instance of `YearMonth` using the specified `Year` and `Month`.
 *
 * @param year the year part of the `YearMonth`, must not be null
 * @param month the month part of the `YearMonth`, must not be null
 * @since 1.0.0
 */
fun YearMonth(year: Year, month: Month) = YearMonth.of(year.value, month.value)!!
/**
 * Creates an instance of `YearMonth` using the specified year and month.
 *
 * @param year The `Year` object representing the year part of the `YearMonth`.
 * @param month The month of the year, from 1 (January) to 12 (December).
 * @since 1.0.0
 */
fun YearMonth(year: Year, month: Int) = YearMonth.of(year.value, month)!!
/**
 * Parses a `CharSequence` into a `YearMonth` using the provided `DateTimeFormatter`.
 *
 * @param cs the input character sequence representing the year and month, in a format supported by the provided parser
 * @param parser the formatter to parse the year-month input, defaults to a formatter with "yyyy-MM" pattern
 * @return a `YearMonth` instance representing the parsed year and month
 * @since 1.0.0
 */
fun YearMonth(cs: CharSequence, parser: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
    .appendLiteral('-')
    .appendValue(ChronoField.MONTH_OF_YEAR, 2)
    .toFormatter()
): Result<YearMonth> = runCatching { YearMonth.parse(cs, parser) }
/**
 * Creates an instance of `YearMonth` set to the current year and month
 * based on the system default time-zone and clock.
 *
 * This function fetches the current date and time and extracts the year
 * and month information, creating a `YearMonth` object with the
 * corresponding values.
 *
 * @return The current `YearMonth` instance.
 * @since 1.0.0
 */
fun YearMonth() = YearMonth.now()!!
/**
 * Returns a YearMonth instance for the current year and month determined by the specified clock.
 *
 * @param clock the clock providing the current time, used to determine the year and month
 * @since 1.0.0
 */
fun YearMonth(clock: Clock) = YearMonth.now(clock)!!
/**
 * Creates a `YearMonth` object representing the current year and month
 * in the specified time-zone.
 *
 * @param zone the time-zone to use for determining the current year and month
 * @since 1.0.0
 */
fun YearMonth(zone: ZoneId) = YearMonth.now(zone)!!
/**
 * Returns the current YearMonth instance for the specified time zone.
 *
 * @param zone The ZoneIdent specifying the time zone for which the current YearMonth should be retrieved.
 * @since 1.0.0
 */
fun YearMonth(zone: ZoneIdent) = YearMonth.now(zone.zoneId)!!

/**
 * Creates an instance of `MonthDay` representing the given month and day.
 *
 * @param month the month of the year, from 1 (January) to 12 (December)
 * @param day the day of the month, from 1 to 31, subject to the valid range for the given month
 * @since 1.0.0
 */
fun MonthDay(month: Int, day: Int) = MonthDay.of(month, day)!!
/**
 * Creates an instance of `MonthDay` using the provided month and day.
 *
 * @param month the month of the year, represented as a `Month` enum.
 * @param day the day of the month, represented as an integer.
 * @since 1.0.0
 */
fun MonthDay(month: Month, day: Int) = MonthDay.of(month.value, day)!!
/**
 * Parses a `CharSequence` to create a `MonthDay` instance.
 *
 * @param cs the `CharSequence` to parse, representing a date in the format `--MM-dd`
 * @param parser an optional `DateTimeFormatter` to use for parsing, defaults to a formatter that parses the `--MM-dd` format
 * @return a `MonthDay` instance representing the parsed date
 * @since 1.0.0
 */
fun MonthDay(cs: CharSequence, parser: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendLiteral("--")
    .appendValue(ChronoField.MONTH_OF_YEAR, 2)
    .appendLiteral('-')
    .appendValue(ChronoField.DAY_OF_MONTH, 2)
    .toFormatter()
): Result<MonthDay> = runCatching { MonthDay.parse(cs, parser) }
/**
 * Factory method to create an instance of `MonthDay` representing the current month and day
 * using the system default time-zone.
 *
 * @return A `MonthDay` instance representing the current month and day.
 * @throws DateTimeException if the current date cannot be obtained due to invalid system clock or time-zone setting.
 * @since 1.0.0
 */
fun MonthDay() = MonthDay.now()!!
/**
 * Creates a `MonthDay` instance using the specified clock.
 *
 * This method uses the current date-time from the provided clock
 * to extract the month and day-of-month information, creating a
 * `MonthDay` object representing that information.
 *
 * @param clock the clock to retrieve the current date-time information, not null
 * @since 1.0.0
 */
fun MonthDay(clock: Clock) = MonthDay.now(clock)!!
/**
 * Returns the current month and day in the specified time-zone.
 *
 * This function retrieves the current month and day based on the given time-zone
 * by using the `MonthDay` class.
 *
 * @param zone the ZoneId representing the time-zone; not null
 * @return a MonthDay object representing the current month and day in the time-zone
 * @throws DateTimeException if the current date cannot be obtained in the specified zone
 * @since 1.0.0
 */
fun MonthDay(zone: ZoneId) = MonthDay.now(zone)!!
/**
 * Constructs a `MonthDay` instance based on the current date in the specified time zone.
 *
 * @param zone the time zone to be used to determine the current date
 * @since 1.0.0
 */
fun MonthDay(zone: ZoneIdent) = MonthDay.now(zone.zoneId)!!

/**
 * Creates an `Instant` from the given epoch time in milliseconds.
 *
 * @param epochMilliseconds The number of milliseconds since the Unix epoch (1970-01-01T00:00:00Z).
 * @since 1.0.0
 */
fun Instant(epochMilliseconds: Long) = Instant.ofEpochMilli(epochMilliseconds)!!
/**
 * Creates an instance of `Instant` using the provided epoch seconds and optional nanoseconds adjustment.
 *
 * @param epochSeconds the number of seconds since the epoch of 1970-01-01T00:00:00Z
 * @param nanosecondsOfSecond the nanosecond adjustment to the number of seconds, from 0 to 999,999,999 (default is 0)
 * @since 1.0.0
 */
fun Instant(epochSeconds: Long, nanosecondsOfSecond: Int = 0) = Instant.ofEpochSecond(epochSeconds, nanosecondsOfSecond.toLong())!!
/**
 * Parses the given [CharSequence] into an [Instant] instance.
 * The method attempts to parse the provided [cs] using the standard ISO-8601 instant format.
 * Returns a [Result] encapsulating the parsed [Instant] or the parsing error.
 *
 * @param cs the character sequence representing the instant in ISO-8601 format
 * @return a [Result] containing the parsed [Instant] on success or an exception on failure
 * @since 1.0.0
 */
fun Instant(cs: CharSequence): Result<Instant> = runCatching { Instant.parse(cs) }
/**
 * Creates and returns the current instant of the clock system. This function delegates to `Instant.now()!!`
 * to provide the exact current moment in time based on the system's default clock.
 *
 * This can be used to fetch the current timestamp with timezone-independent precision.
 *
 * Note that a null value will not be returned, as the result of `Instant.now()` is non-null and
 * this function enforces non-nullability with `!!`.
 *
 * @return The current point on the timeline as an `Instant`.
 *
 * @since 1.0.0
 */
fun Instant() = Instant.now()!!
/**
 * Creates an instance of `Instant` using the provided clock.
 *
 * @param clock The clock to define the specific point on the time-line.
 * @return The current instant based on the provided clock.
 * @since 1.0.0
 */
fun Instant(clock: Clock) = Instant.now(clock)!!

/**
 * Extension property to check if the current `LocalDate` instance represents today's date.
 *
 * This property compares the `LocalDate` instance to the system's current date using `LocalDate.now()`.
 * It returns `true` if the dates are the same, otherwise `false`.
 *
 * @receiver The `LocalDate` instance to be checked.
 * @return `true` if the date is today, `false` otherwise.
 * @since 1.0.0
 */
val LocalDate.isToday
    get() = this == LocalDate.now()
/**
 * Extension property for the `LocalDate` class to check if the current
 * date instance represents the date of tomorrow relative to the system's
 * current date.
 *
 * @receiver The `LocalDate` instance being checked.
 * @return `true` if the date is equal to tomorrow's date, otherwise `false`.
 * @since 1.0.0
 */
val LocalDate.isTomorrow
    get() = this == LocalDate.now().plusDays(1)
/**
 * Extension property for `LocalDate` that checks if the given date is equal to yesterday's date.
 *
 * This property evaluates whether the current instance of `LocalDate` is exactly one day
 * prior to the current date on the system's clock in the default time zone.
 *
 * @receiver The `LocalDate` instance to be checked.
 * @return `true` if the `LocalDate` instance represents yesterday's date, otherwise `false`.
 * @since 1.0.0
 */
val LocalDate.isYesterday
    get() = this == LocalDate.now().minusDays(1)
/**
 * Checks if the current `LocalDateTime` instance represents a date
 * that is the same as the current date in the system's default time zone.
 *
 * @receiver The `LocalDateTime` instance to be evaluated.
 * @return `true` if the date portion of the `LocalDateTime` matches the current date,
 *         `false` otherwise.
 * @since 1.0.0
 */
val LocalDateTime.isToday
    get() = toLocalDate() == LocalDate.now()
/**
 * Extension property to determine if the current `LocalDateTime` instance 
 * represents a date that corresponds to "tomorrow" relative to the current date.
 *
 * This property returns `true` if the date part of the `LocalDateTime` is 
 * one day ahead of the current system date, and `false` otherwise.
 *
 * @receiver The `LocalDateTime` instance being evaluated.
 * @return `true` if the date part of the receiver is tomorrow's date, `false` otherwise.
 * @since 1.0.0
 */
val LocalDateTime.isTomorrow
    get() = toLocalDate() == LocalDate.now().plusDays(1)
/**
 * Extension property to determine if a given `LocalDateTime` instance represents a date that was yesterday.
 *
 * This property compares the local date part of the `LocalDateTime` instance with the local date of the system's
 * current time minus one day.
 *
 * @return `true` if the local date corresponds to yesterday, otherwise `false`.
 * @since 1.0.0
 */
val LocalDateTime.isYesterday
    get() = toLocalDate() == LocalDate.now().minusDays(1)
/**
 * A read-only property that checks whether the `OffsetDateTime` instance represents the current date
 * in the system's default time zone.
 *
 * The property compares the local date portion of the `OffsetDateTime` with the current system date.
 * 
 * @receiver The `OffsetDateTime` instance to be checked.
 * @return `true` if the `OffsetDateTime` corresponds to the current date, otherwise `false`.
 * @since 1.0.0
 */
val OffsetDateTime.isToday
    get() = toLocalDate() == LocalDate.now()
/**
 * Checks if the current `OffsetDateTime` instance represents a date that occurs tomorrow
 * relative to the current system date.
 *
 * This is determined by comparing the `LocalDate` representation of the `OffsetDateTime`
 * instance to the system's current date incremented by one day.
 *
 * @return `true` if the date of this `OffsetDateTime` is tomorrow, otherwise `false`.
 * @since 1.0.0
 */
val OffsetDateTime.isTomorrow
    get() = toLocalDate() == LocalDate.now().plusDays(1)
/**
 * Determines whether the given `OffsetDateTime` instance represents a date
 * that corresponds to "yesterday" based on the system's current date.
 *
 * @receiver The `OffsetDateTime` instance to evaluate.
 * @return `true` if the date part of the instance matches yesterday's date; `false` otherwise.
 * @since 1.0.0
 */
val OffsetDateTime.isYesterday
    get() = toLocalDate() == LocalDate.now().minusDays(1)
/**
 * Extension property that determines if the current ZonedDateTime instance
 * represents a date that is the same as today's date in the system default time zone.
 *
 * It compares the local date part of the ZonedDateTime with the current local date.
 *
 * @receiver The ZonedDateTime instance to check.
 * @return `true` if the ZonedDateTime instance represents today's date, `false` otherwise.
 * @since 1.0.0
 */
val ZonedDateTime.isToday
    get() = toLocalDate() == LocalDate.now()
/**
 * Extension property for `ZonedDateTime` to determine if the date represented
 * by this instance corresponds to "tomorrow" relative to the current system date.
 *
 * The comparison is performed based on the local date without considering the time component.
 *
 * @receiver The `ZonedDateTime` instance to evaluate.
 * @return `true` if the local date of the `ZonedDateTime` is equal to the local date
 *         of tomorrow, `false` otherwise.
 * @since 1.0.0
 */
val ZonedDateTime.isTomorrow
    get() = toLocalDate() == LocalDate.now().plusDays(1)
/**
 * Extension property to determine if the instance of `ZonedDateTime` represents a date that was yesterday.
 *
 * This property compares the `ZonedDateTime`'s local date to the local date of the current moment
 * minus one day, considering the system's default time zone.
 *
 * @receiver `ZonedDateTime` instance to be checked.
 * @return `true` if the local date of the `ZonedDateTime` is equal to yesterday's local date;
 *         `false` otherwise.
 * @since 1.0.0
 */
val ZonedDateTime.isYesterday
    get() = toLocalDate() == LocalDate.now().minusDays(1)

/**
 * Extension property to check if the `LocalTime` instance represents midnight.
 *
 * This property returns `true` if the `LocalTime` object is equal to `LocalTime.MIDNIGHT`,
 * indicating that it corresponds to 00:00 (midnight time), and `false` otherwise.
 *
 * @receiver LocalTime instance to be evaluated.
 * @return Boolean value indicating whether the time is midnight.
 * @since 1.0.0
 */
val LocalTime.isMidnight get() = this == LocalTime.MIDNIGHT
/**
 * Extension property to determine if the current [LocalTime] instance represents noon.
 *
 * The property checks whether the [LocalTime] is equal to [LocalTime.NOON].
 *
 * @receiver LocalTime instance to be checked.
 * @return `true` if the [LocalTime] is noon, `false` otherwise.
 *
 * @since 1.0.0
 */
val LocalTime.isNoon get() = this == LocalTime.NOON
/**
 * Extension property that determines whether the given `OffsetTime` instance represents midnight.
 *
 * This property checks if the local time component of the `OffsetTime` instance is equal to
 * `LocalTime.MIDNIGHT`.
 *
 * @receiver The `OffsetTime` instance to be checked.
 * @return `true` if the `OffsetTime` instance represents midnight, `false` otherwise.
 * @since 1.0.0
 */
val OffsetTime.isMidnight get() = toLocalTime() == LocalTime.MIDNIGHT
/**
 * Checks if the current `OffsetTime` instance represents noon (12:00 PM) in local time.
 *
 * @return `true` if the local time component of this `OffsetTime` is exactly noon, otherwise `false`.
 * @since 1.0.0
 */
val OffsetTime.isNoon get() = toLocalTime() == LocalTime.NOON

/**
 * Indicates whether the `OffsetDateTime` has a UTC offset.
 *
 * This property evaluates to `true` if the offset of the `OffsetDateTime` is `ZoneOffset.UTC`,
 * otherwise it evaluates to `false`.
 *
 * @return `true` if the offset is `ZoneOffset.UTC`, otherwise `false`.
 * @since 1.0.0
 */
val OffsetDateTime.isUTC get() = offset == ZoneOffset.UTC
/**
 * Indicates whether the `ZonedDateTime` is in the UTC time zone.
 *
 * This property evaluates to `true` if the offset associated with the
 * `ZonedDateTime` is equal to `ZoneOffset.UTC`. Otherwise, it returns `false`.
 *
 * @return `true` if the `ZonedDateTime` represents the UTC time zone; `false` otherwise.
 * @since 1.0.0
 */
val ZonedDateTime.isUTC get() = offset == ZoneOffset.UTC
/**
 * Indicates whether the current `OffsetTime` instance is in the UTC time zone.
 *
 * This property evaluates to `true` if the `OffsetTime`'s offset is equal to
 * `ZoneOffset.UTC`, otherwise it returns `false`.
 *
 * @receiver The `OffsetTime` instance to check for UTC offset.
 * @return `true` if the offset is `ZoneOffset.UTC`, `false` otherwise.
 * @since 1.0.0
 */
val OffsetTime.isUTC get() = offset == ZoneOffset.UTC
/**
 * Indicates whether the `OffsetMonthDayTime` instance represents a time in the UTC time zone.
 *
 * This property evaluates to `true` if the associated offset is equal to `ZoneOffset.UTC`.
 *
 * @receiver OffsetMonthDayTime instance whose offset is being checked.
 * @return `true` if the offset is `ZoneOffset.UTC`, otherwise `false`.
 * @since 1.0.0
 */
val OffsetMonthDayTime.isUTC get() = offset == ZoneOffset.UTC
/**
 * Indicates whether the current `ZonedMonthDayTime` instance is in the UTC timezone.
 *
 * This property evaluates to `true` if the offset of the current time is equal to `ZoneOffset.UTC`,
 * otherwise it returns `false`. It is used to determine if the time representation aligns with
 * Universal Coordinated Time (UTC).
 *
 * @since 1.0.0
 */
val ZonedMonthDayTime.isUTC get() = offset == ZoneOffset.UTC