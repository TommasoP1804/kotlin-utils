package dev.tommasop1804.kutils.classes.builder

import dev.tommasop1804.kutils.Int2
import dev.tommasop1804.kutils.unaryPlus
import dev.tommasop1804.kutils.validate
import java.time.DayOfWeek
import java.time.Month

@Suppress("unused", "kutils_take_as_int_invoke")
/**
 * A builder class for constructing cron expressions with flexible configurations.
 * This class provides methods for specifying values, ranges, or step intervals
 * for each part of a cron expression including seconds, minutes, hours, days,
 * months, days of the week, and years.
 *
 * Fields:
 * - `type`: Represents the type of cron expression being built.
 * - `seconds`: Represents the seconds part of the cron expression.
 * - `minutes`: Represents the minutes part of the cron expression.
 * - `hours`: Represents the hours part of the cron expression.
 * - `daysOfMonth`: Represents the days of the month in the cron expression.
 * - `months`: Represents the months in the cron expression.
 * - `daysOfWeek`: Represents the days of the week in the cron expression.
 * - `years`: Represents the years in the cron expression.
 *
 * Available methods allow for precise control over cron parameter values, enabling
 * the specification of exact values, ranges, steps, or other special configurations.
 *
 * The `build` method finalizes the configuration and generates the corresponding
 * cron expression.
 *
 * @constructor Creates an empty Builder to begin constructing the cron expression.
 * @since 1.0.0
 */
class CronExpressionBuilder(private val type: Type) {
    /**
     * Represents the seconds configuration for a builder.
     * This variable typically stores the list of seconds or a pattern
     * describing the second values to use during the build process.
     * It is internally set and managed by the relevant `second` and `allSeconds` methods.
     *
     * @since 1.0.0
     */
    private var seconds = ""
    /**
     * Represents the configuration for specifying minute values in a scheduling context.
     * This variable is part of the `Builder` class and is used to store minute-related definitions
     * that may include specific values, ranges, step increments, or other conditions.
     *
     * The intended usage of this variable is through the functional methods within the `Builder` class,
     * such as `minute` or `allMinutes`, which allow fluent API-like configurations.
     *
     * Variable is initialized as an empty string and is expected to be updated during the building process.
     *
     * @since 1.0.0
     */
    private var minutes = ""
    /**
     * Represents the selected hours for scheduling or time-related operations in the `Builder` class.
     * This variable is used to store a String representation of the specified hours.
     *
     * The expected format for the hours is determined by the logic of the `Builder` class,
     * typically set by the associated methods like `hour(...)` or `allHours(...)`.
     *
     * The value of this variable influences the behavior of the resulting configurations created by the builder.
     *
     * @since 1.0.0
     */
    private var hours = ""
    /**
     * Represents the days of a month specified for scheduling or building
     * a custom time-based configuration. The value is a string pattern
     * that defines the specific days or ranges of days within a month
     * to be selected.
     *
     * This variable stores the internal representation of the days of a month
     * and is utilized by the `Builder` class to construct the desired configuration.
     *
     * @property daysOfMonth A string describing days of a month, supporting
     * patterns such as specific days, ranges, and stepped intervals.
     * The actual pattern format is defined by the `Builder` implementation.
     *
     * @receiver Builder context for constructing time-related configurations.
     * @since 1.0.0
     */
    private var daysOfMonth = ""
    /**
     * Holds the months expression as a string for building a schedule or pattern.
     * This variable is used internally to manage and represent the months portion of the schedule.
     *
     * @since 1.0.0
     */
    private var months = ""
    /**
     * Stores the days of the week as part of a scheduling or configuration process.
     *
     * This variable is intended to hold a representation of days in a specific context,
     * and is likely modified through specific builder methods within the enclosing class.
     *
     * @since 1.0.0
     */
    private var daysOfWeek = ""
    /**
     * Represents the years component in the builder. Used to specify the year values
     * within the configured range or pattern. This variable is mutable and initialized
     * as an empty string by default. It is intended to be updated using relevant methods
     * of the builder class.
     *
     * @since 1.0.0
     */
    private var years = ""

    /**
     * Adds the provided seconds to the schedule configuration.
     *
     * This method allows you to specify one or more second values in the range of 0 to 59.
     * If the current configuration type is UNIX or CRON4J, an exception will be thrown since second-based scheduling
     * is not supported for these types.
     *
     * @param values the second values to add, each within the range of 0 to 59
     * @return the updated Builder instance
     * @throws UnsupportedOperationException if the configuration type does not support second-based scheduling
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any of the provided values are not within the valid range
     * @since 1.0.0
     */
    fun second(vararg values: Int): CronExpressionBuilder {
        if (type == Type.UNIX || type == Type.CRON4J) throw UnsupportedOperationException("Second is not supported by $type")

        values.forEach { it.validate(lazyMessage = { "Invalid range of second" }) { this in 0..59 } }

        seconds = when (seconds) {
            "" -> values.joinToString(",")
            else -> "$seconds,${values.joinToString(",")}"
        }
        return this
    }
    /**
     * Specifies the seconds within a minute for the cron expression. Accepts a variable number of pairs,
     * where each pair consists of a range of seconds and an optional step value.
     *
     * The range must be within the interval 0 to 59 (inclusive), and the step value must be
     * between 1 and 59. For each pair, the method appends the specified second ranges
     * and steps to the cron expression's second field.
     *
     * This method is not supported for cron types `Type.UNIX` or `Type.CRON4J`.
     *
     * @param rangeAndStep Variable number of pairs, each containing an `IntRange` specifying the range of seconds,
     *                     and an `Int` specifying the step value. The step value must fall between 1 and 59.
     * @return The current instance of `CronExpressionBuilder` with the updated seconds configuration.
     * @throws UnsupportedOperationException If the cron type is `Type.UNIX` or `Type.CRON4J`.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the specified second range or step value is invalid.
     * @since 1.0.0
     */
    @JvmName("secondIntRangeStep")
    fun second(vararg rangeAndStep: Pair<IntRange, Int>): CronExpressionBuilder {
        if (type == Type.UNIX || type == Type.CRON4J) throw UnsupportedOperationException("Second is not supported by $type")

        rangeAndStep.forEach {
            it.validate(lazyMessage = { "Invalid range of second" }) { first.first in 0..59 && first.last in 0..59 }
            it.validate(lazyMessage = { "Invalid range of step" }) { second in 1..59 }
        }

        seconds = when (seconds) {
            "" -> rangeAndStep.joinToString(",") { "${it.first}-${it.first.last}" + if (it.second > 1) "/${it.second}" else "" }
            else -> "$seconds,${rangeAndStep.joinToString(",") { "${it.first}-${it.first.last}" + if (it.second > 1) "/${it.second}" else "" }}"
        }
        return this
    }
    /**
     * Sets the seconds field of the cron expression with the specified ranges.
     * This method is unsupported for UNIX or CRON4J types and will throw an exception if used with these types.
     *
     * @param range Vararg parameter specifying one or more ranges of seconds. Each range must be within 0 to 59.
     *              For instance, a range might represent a sequence of valid second values in a cron expression.
     * @return The updated CronExpressionBuilder instance for method chaining.
     * @throws UnsupportedOperationException If the cron type is UNIX or CRON4J, as seconds field is not supported.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If any specified range has start or last values outside the 0 to 59 range.
     * @since 1.0.0
     */
    fun second(vararg range: IntRange): CronExpressionBuilder {
        if (type == Type.UNIX || type == Type.CRON4J) throw UnsupportedOperationException("Second is not supported by $type")

        range.forEach {
            it.validate(lazyMessage = { "Invalid range of second" }) { first in 0..59 && last in 0..59 }
        }

        seconds = when (seconds) {
            "" -> range.joinToString(",") { "${it.first}-${it.last}" }
            else -> "$seconds,${range.joinToString(",") { "${it.first}-${it.last}" }}"
        }
        return this
    }
    /**
     * Configures the seconds for a schedule using step values. The method can accept multiple pairs
     * of seconds and steps and modifies the builder configuration accordingly. Each pair represents
     * a starting second and a step value that determines the interval between occurrences.
     *
     * @param secondAndStep A vararg of pairs, where each pair consists of a starting second and a step value.
     *                      The starting second must be within the range 0-59, and the step value must be
     *                      within the range 1-59.
     * @return The updated Builder instance for chained configuration.
     * @throws UnsupportedOperationException if the schedule type does not support seconds configuration.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any of the provided seconds or step values are out of range.
     * @since 1.0.0
     */
    @JvmName("secondIntStep")
    fun second(vararg secondAndStep: Int2): CronExpressionBuilder {
        if (type == Type.UNIX || type == Type.CRON4J) throw UnsupportedOperationException("Second is not supported by $type")

        secondAndStep.forEach { it.validate(lazyMessage = { "Invalid second" }) { first in 0..59 && second in 1..59 } }

        seconds = when (seconds) {
            "" -> secondAndStep.joinToString(",") { "${it.first}/${it.second}" }
            else -> "$seconds,${secondAndStep.joinToString(",") { "${it.first}/${it.second}" }}"
        }
        return this
    }
    /**
     * Specifies all seconds with an optional step value.
     * This method sets the "seconds" field to denote every second, considering the specified step.
     * Throws an exception if the type does not support seconds.
     *
     * @param step The step interval for seconds. Must be in the range 1 to 59. Defaults to 1.
     * @return An instance of the Builder class with updated second settings.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if the provided step value are out of range.
     * @since 1.0.0
     */
    fun allSeconds(step: Int = 1): CronExpressionBuilder {
        if (type == Type.UNIX || type == Type.CRON4J) throw UnsupportedOperationException("Second is not supported by $type")
        step.validate(lazyMessage = { "Invalid range of step" }) { this in 1..59 }

        seconds = "*" + if (step > 1) "/$step" else ""
        return this
    }

    /**
     * Adds the provided minute values to the builder. Ensures each value is within the range of 0 to 59.
     * If the builder already contains minute values, the new values are appended to the existing ones.
     *
     * @param values The minute values to add to the builder. Each value must be in the range 0 to 59.
     * @return The current instance of the [CronExpressionBuilder], with the specified minutes added.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any of the provided values are not within the valid range
     * @since 1.0.0
     */
    fun minute(vararg values: Int): CronExpressionBuilder {
        values.forEach { it.validate(lazyMessage = { "Invalid range of minutes" }) { this in 0..59 } }

        minutes = when (minutes) {
            "" -> values.joinToString(",")
            else -> "$minutes,${values.joinToString(",")}"
        }
        return this
    }
    /**
     * Specifies the minute(s) in a cron expression with optional step values.
     * This allows defining multiple minute ranges and steps to be included in the cron schedule.
     *
     * @param rangeAndStep A vararg of pairs where:
     *   - The first element is an [IntRange] defining the range of minutes (valid range: 0-59).
     *   - The second element is an [Int] specifying the step value to increment within the range (valid step: 1-59).
     * @return The updated instance of [CronExpressionBuilder] with the specified minute(s) applied to the cron expression.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any provided range or step value is invalid.
     * @since 1.0.0
     */
    @JvmName("minuteIntRangeStep")
    fun minute(vararg rangeAndStep: Pair<IntRange, Int>): CronExpressionBuilder {
        rangeAndStep.forEach {
            it.validate(lazyMessage = { "Invalid range of minutes" }) { first.first in 0..59 && first.last in 0..59 }
            it.validate(lazyMessage = { "Invalid range of step" }) { second in 1..59 }
        }

        minutes = when (minutes) {
            "" -> rangeAndStep.joinToString(",") { "${it.first}-${it.first.last}" + if (it.first.last > 1) "/${it.second}" else "" }
            else -> "$minutes,${rangeAndStep.joinToString(",") { "${it.first}-${it.first.last}" + if (it.second > 1) "/${it.second}" else "" }}"
        }
        return this
    }
    /**
     * Adds minute ranges to the cron expression being constructed.
     * The ranges must be between 0 and 59.
     *
     * @param range A variable number of integer ranges specifying valid minute intervals.
     *              Each range should have its start and end within the range of 0 to 59.
     * @return An instance of [CronExpressionBuilder] with the specified minute ranges added.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any of the provided ranges are invalid.
     * @since 1.0.0
     */
    fun minute(vararg range: IntRange): CronExpressionBuilder {
        range.forEach {
            it.validate(lazyMessage = { "Invalid range of minutes" }) { first in 0..59 && last in 0..59 }
        }

        minutes = when (minutes) {
            "" -> range.joinToString(",") { "${it.first}-${it.last}" }
            else -> "$minutes,${range.joinToString(",") { "${it.first}-${it.last}" }}"
        }
        return this
    }
    /**
     * Updates the minute configuration for the Builder object based on the provided minute and step pairs.
     * Each pair represents a starting minute and an incremental step for scheduling purposes.
     *
     * @param minuteAndStep A variable number of pairs, where each pair contains:
     * - The starting minute (an integer between 0 and 59, inclusive).
     * - The step increment (an integer between 1 and 59, inclusive).
     * @return The current Builder instance with the updated minute configuration.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If any starting minute is not in the range 0 to 59 or
     * if any step increment is not in the range 1 to 59.
     * @since 1.0.0
     */
    @JvmName("minuteIntStep")
    fun minute(vararg minuteAndStep: Int2): CronExpressionBuilder {
        minuteAndStep.forEach {
            it.validate(lazyMessage = { "Invalid minutes" }) { first in 0..59 && second in 1..59 }
        }

        minutes = when (minutes) {
            "" -> minuteAndStep.joinToString(",") { "${it.first}/${it.second}" }
            else -> "$minutes,${minuteAndStep.joinToString(",") { "${it.first}/${it.second}" }}"
        }
        return this
    }
    /**
     * Configures the schedule to include all minutes within an hour, optionally allowing a step interval.
     *
     * @param step the step interval between minutes, ranging from 1 to 59. Defaults to 1.
     * @return the Builder instance with the updated minutes configuration.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the provided step value is outside the valid range of 1 to 59.
     * @since 1.0.0
     */
    fun allMinutes(step: Int = 1): CronExpressionBuilder {
        step.validate(lazyMessage = { "Invalid range of step" }) { this in 1..59 }

        minutes = "*" + if (step > 1) "/$step" else ""
        return this
    }

    /**
     * Sets specific hours for the builder. Accepts a variable number of integer inputs and validates
     * that each hour lies within the valid range (0 to 23).
     *
     * @param values Int representing the specific hours to be set, where each value must be in the range 0 to 23.
     * @return Returns the updated Builder instance with the specified hours added.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any of the provided values are not within the valid range
     * @since 1.0.0
     */
    fun hour(vararg values: Int): CronExpressionBuilder {
        values.forEach { it.validate(lazyMessage = { "Invalid range of hours" }) { this in 0..23 } }

        hours = when (hours) {
            "" -> values.joinToString(",")
            else -> "$hours,${values.joinToString(",")}"
        }
        return this
    }
    /**
     * Configures the allowed hour values for a cron expression. Accepts one or more pairs
     * of hour ranges and step values to define which hours are included in the cron expression.
     * Hour ranges must lie between 0 and 23, and step values must be between 1 and 23.
     *
     * @param rangeAndStep A vararg of pairs, where each pair consists of an [IntRange] representing
     *                     the allowed hours and an [Int] step interval to specify how often within
     *                     the range the hours are included.
     * @return The updated [CronExpressionBuilder] with the configured hour values.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if the hour range or step values are invalid.
     * @since 1.0.0
     */
    @JvmName("hourIntStep")
    fun hour(vararg rangeAndStep: Pair<IntRange, Int>): CronExpressionBuilder {
        rangeAndStep.forEach {
            it.validate(lazyMessage = { "Invalid range of hours" }) { first.first in 0..23 && first.last in 0..23 }
            it.validate(lazyMessage = { "Invalid range of steps" }) { second in 1..23 }
        }

        hours = when (hours) {
            "" -> rangeAndStep.joinToString(",") { "${it.first.first}-${it.first.last}" + if (it.second > 1) "/${it.second}" else "" }
            else -> "$hours,${rangeAndStep.joinToString(",") { "${it.first.first}-${it.first.last}" + if (it.second > 1) "/${it.second}" else "" }}"
        }
        return this
    }
    /**
     * Sets the hour(s) to the provided range(s) for the cron expression.
     * Each range of hours must be within the valid range of 0 to 23.
     *
     * @param range Vararg parameter specifying the inclusive ranges of hours to set.
     * Each range must have both its start and end within 0 to 23.
     * @return The updated instance of [CronExpressionBuilder].
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If any range has values outside the valid range of 0 to 23.
     * @since 1.0.0
     */
    fun hour(vararg range: IntRange): CronExpressionBuilder {
        range.forEach {
            it.validate(lazyMessage = { "Invalid range of hours" }) { first in 0..23 && last in 0..23 }
        }

        hours = when (hours) {
            "" -> range.joinToString(",") { "${it.first}-${it.last}" }
            else -> "$hours,${range.joinToString(",") { "${it.first}-${it.last}" }}"
        }
        return this
    }
    /**
     * Adds specified hours with steps to the builder configuration.
     *
     * Each hour and step pair represents an hour value and the corresponding step value.
     * The hour value should be within the range 0 to 23, and the step must be between 1 and 23.
     *
     * @param hourAndStep Vararg of pairs where the first value specifies the hour (0-23)
     *                    and the second value specifies the step (1-23).
     * @return The updated Builder instance.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any of the hour values are not in the range 0-23,
     *                                  or if steps are not in the range 1-23.
     * @since 1.0.0
     */
    @JvmName("hourIntRangeStep")
    fun hour(vararg hourAndStep: Int2): CronExpressionBuilder {
        hourAndStep.forEach {
            it.validate(lazyMessage = { "Invalid hours" }) { first in 0..23 && second in 1..23 }
        }

        hours = when (hours) {
            "" -> hourAndStep.joinToString(",") { "${it.first}/${it.second}" }
            else -> "$hours,${hourAndStep.joinToString(",") { "${it.first}/${it.second}" }}"
        }
        return this
    }
    /**
     * Sets the `hours` field to represent all hours in a day with an optional step to specify intervals.
     *
     * @param step the interval in hours for the specification. Must be between 1 and 23 inclusive. Default is 1.
     * @return the modified Builder instance.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if the step value is outside the valid range.
     * @since 1.0.0
     */
    fun allHours(step: Int = 1): CronExpressionBuilder {
        step.validate(lazyMessage = { "Invalid range of step" }) { this in 1..23 }

        hours = "*" + if (step > 1) "/$step" else ""
        return this
    }

    /**
     * Adds specific days of the month to the builder. This method accepts one or more integer values representing
     * the days of the month. Each value must be within the range from 1 to 31, inclusive. If the input contains
     * invalid day values, an exception will be thrown.
     *
     * @param values one or more integers representing the days of the month to be added to the builder
     * @return the updated instance of the Builder
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any value is outside the valid range of 1 to 31
     * @since 1.0.0
     */
    fun dayOfMonth(vararg values: Int): CronExpressionBuilder {
        values.forEach { it.validate(lazyMessage = { "Invalid range of days of month" }) { this in 1..31 } }

        daysOfMonth = when (daysOfMonth) {
            "" -> values.joinToString(",")
            else -> "$daysOfMonth,${values.joinToString(",")}"
        }
        return this
    }
    /**
     * Configures the days of the month for the cron expression by specifying ranges and optional steps.
     *
     * @param rangeAndStep Variable number of pairs where each pair contains an `IntRange` representing
     * the range of days and an `Int` representing the step. The range must be within 1 to 31 and the
     * step must be between 1 and 31.
     * @return An updated instance of `CronExpressionBuilder` with the configured days of the month.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any of the provided ranges or steps are invalid.
     * @since 1.0.0
     */
    @JvmName("dayOfMonthIntRangeStep")
    fun dayOfMonth(vararg rangeAndStep: Pair<IntRange, Int>): CronExpressionBuilder {
        rangeAndStep.forEach {
            it.validate(lazyMessage = { "Invalid range of days of month" }) { first.first in 1..31 && first.last in 1..31 }
            it.validate(lazyMessage = { "Invalid range of steps" }) { second in 1..31 }
        }

        daysOfMonth = when (daysOfMonth) {
            "" -> rangeAndStep.joinToString(",") { "${it.first}-${it.first.last}" + if (it.second > 1) "/${it.second}" else "" }
            else -> "$daysOfMonth,${rangeAndStep.joinToString(",") { "${it.first}-${it.first.last}" + if (it.second > 1) "/${it.second}" else "" }}"
        }
        return this
    }
    /**
     * Specifies the day(s) of the month to include in the cron expression. Accepts one or more ranges
     * of days, where each range should fall within the valid days of a month (1 to 31).
     *
     * @param range one or more ranges of integers, each representing a range of days of the month.
     * Each range must have both its start and end values within 1 to 31, inclusive.
     * @return the current instance of [CronExpressionBuilder] with the day(s) of the month updated.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any range is outside the valid range of 0 to 31.
     * @since 1.0.0
     */
    fun dayOfMonth(vararg range: IntRange): CronExpressionBuilder {
        range.forEach {
            it.validate(lazyMessage = { "Invalid range of days of month" }) { first in 1..31 && last in 0..31 }
        }

        daysOfMonth = when (daysOfMonth) {
            "" -> range.joinToString(",") { "${it.first}-${it.last}" }
            else -> "$daysOfMonth,${range.joinToString(",") { "${it.first}-${it.last}" }}"
        }
        return this
    }
    /**
     * Configures the days of the month with specified day and step pairs.
     *
     * Each pair consists of:
     * - the day of the month (0-31 where 0 indicates an undefined day)
     * - the step between occurrences (1-31).
     *
     * @param dayAndStep vararg of pairs where the first value represents the day of the month
     * within the range 1 to 31, and the second value represents the step value within the
     * range 1 to 31.
     * @return the current Builder instance with the updated days of the month configuration.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any of the provided day and step values are invalid.
     * @since 1.0.0
     */
    @JvmName("dayOfMonthIntStep")
    fun dayOfMonth(vararg dayAndStep: Int2): CronExpressionBuilder {
        dayAndStep.forEach { it.validate(lazyMessage = { "Invalid days of month" }) { first in 1..31 && second in 1..31 } }

        daysOfMonth = when (daysOfMonth) {
            "" -> dayAndStep.joinToString(",")  { "${it.first}/${it.second}" }
            else -> "$daysOfMonth,${dayAndStep.joinToString(",") { "${it.first}/${it.second}" }}"
        }
        return this
    }
    /**
     * Configures the schedule to include all days of the month with an optional step value.
     *
     * @param step The interval of days to include within the month. Must be in the range 1 to 31.
     *             Defaults to 1, meaning every day of the month is included.
     * @return The updated Builder instance.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the provided step value is outside the valid range of 1 to 31.
     * @since 1.0.0
     */
    fun allDaysOfMonth(step: Int = 1): CronExpressionBuilder {
        step.validate(lazyMessage = { "Invalid range of step" }) { this in 1..31 }

        daysOfMonth = "*" + if (step > 1) "/$step" else ""
        return this
    }
    /**
     * Sets the days of the month to the last day of the month ("L") in the cron expression.
     * This method is only supported for the `QUARTZ`, `CRON4J`, and `SPRING` types.
     * Throws an exception if the type is not supported.
     *
     * @return The current instance of the Builder, with the days of the month set to "L".
     * @throws UnsupportedOperationException If the current type does not support the last day of the month.
     * @since 1.0.0
     */
    fun lastDayOfMonth(): CronExpressionBuilder {
        type == Type.QUARTZ || type == Type.CRON4J || type == Type.SPRING || throw UnsupportedOperationException("Last day of month is not supported by $type")
        daysOfMonth = "L"
        return this
    }
    /**
     * Specifies an undefined day of the month ("?") for scheduling expressions.
     * This is primarily supported by specific scheduling types such as QUARTZ, CRON4J, SPRING, or SPRING_BEFORE_5_3.
     * If the current type is not one of the supported types, an exception will be thrown.
     *
     * @return The current Builder instance, allowing for method chaining.
     * @throws UnsupportedOperationException If the current type does not support an undefined day of the month.
     * @since 1.0.0
     */
    fun undefinedDayOfMonth(): CronExpressionBuilder {
        type == Type.QUARTZ || type == Type.CRON4J || type == Type.SPRING || type == Type.SPRING_BEFORE_5_3 || throw UnsupportedOperationException(
            "Undefined day of month is not supported by $type"
        )
        daysOfMonth = "?"
        return this
    }
    /**
     * Specifies the nearest weekday to the given day of the month.
     * The method updates the `daysOfMonth` field with the formatted representation
     * of the nearest weekday for the given day.
     *
     * This function only supports the `QUARTZ` and `SPRING` types of schedules.
     * Attempts to use this method with unsupported types will result in an
     * `UnsupportedOperationException`.
     *
     * @param day The day of the week for which the nearest weekday is specified.
     * @return The updated Builder instance.
     * @since 1.0.0
     */
    fun nearestWeekday(day: DayOfWeek): CronExpressionBuilder {
        type == Type.QUARTZ || type == Type.SPRING || throw UnsupportedOperationException("Nearest weekday of is not supported by $type")
        daysOfMonth = "${day.value}W"
        return this
    }
    /**
     * Configures the schedule to represent the last weekday of the month.
     *
     * This method modifies the underlying `daysOfMonth` property to indicate
     * that the schedule should target the last weekday of a given month. It is
     * supported only for `Type.QUARTZ` and `Type.SPRING`. If invoked for an
     * unsupported type, an `UnsupportedOperationException` is thrown.
     *
     * @return The updated `Builder` instance with the last weekday of the month configuration applied.
     * @throws UnsupportedOperationException if the current type is not `Type.QUARTZ` or `Type.SPRING`.
     * @since 1.0.0
     */
    fun lastWeekdayOfMonth(): CronExpressionBuilder {
        type == Type.QUARTZ || type == Type.SPRING || throw UnsupportedOperationException("Last weekday of month is not supported by $type")
        daysOfMonth = "LW"
        return this
    }

    /**
     * Appends one or more specified months to the existing list of months, maintaining the cron-like format.
     *
     * @param values One or more months to be added. Each month is represented as an instance of the `Month` enum.
     * @return The current instance of `Builder` with the updated month configuration.
     * @since 1.0.0
     */
    fun month(vararg values: Month): CronExpressionBuilder {
        months = when (months) {
            "" -> values.map { it.value }.joinToString(",")
            else -> "$months,${values.map { it.value }.joinToString(",")}"
        }
        return this
    }
    /**
     * Configures months with specified ranges and steps.
     *
     * @param rangeAndStep A variable number of pairs where each pair consists of a closed range of months
     * and a step value. The step value must be within the range 1 to 12.
     * @return The current instance of the Builder.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If any step value is outside the range 1 to 12.
     * @since 1.0.0
     */
    @JvmName("monthClosedRangeStep")
    fun month(vararg rangeAndStep: Pair<ClosedRange<Month>, Int>): CronExpressionBuilder {
        rangeAndStep.validate(lazyMessage = { "Invalid range of month step" }) { all { it.second in 1..12 } }

        months = when (months) {
            "" -> rangeAndStep.joinToString(",") { "${it.first.start.value}-${it.first.endInclusive.value}" + if (it.second > 1) "/${it.second}" else "" }
            else -> "$months,${rangeAndStep.joinToString(",") { "${it.first.start.value}-${it.first.endInclusive.value}" + if (it.second > 1) "/${it.second}" else "" }}"
        }
        return this
    }
    /**
     * Specifies the months of the year for the cron expression using the given ranges.
     *
     * This method allows defining multiple ranges of months with optional steps. The generated
     * ranges will be appended to the current list of months if previously defined.
     *
     * @param range A variable number of closed ranges representing the months to include.
     *                     Each range specifies the start and end months, inclusive.
     *                     The `start` and `endInclusive` properties of the range indicate
     *                     the months, using a `Month` enumeration.
     * @return The current instance of `CronExpressionBuilder` with the updated months configuration.
     * @since 1.0.0
     */
    fun month(vararg range: ClosedRange<Month>): CronExpressionBuilder {
        months = when (months) {
            "" -> range.joinToString(",") { "${it.start.value}-${it.endInclusive.value}" }
            else -> "$months,${range.joinToString(",") { "${it.start.value}-${it.endInclusive.value}" }}"
        }
        return this
    }
    /**
     * Configures the months and steps for the builder using the specified ranges and step values.
     *
     * @param rangeAndStep A vararg parameter where each pair consists of an open-ended range of months and a step value.
     *                     The range defines the months to configure, and the step defines the interval to apply.
     *                     The step value must be in the range 1 to 12.
     * @return The updated Builder instance.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If any step value is outside the range 1 to 12.
     * @since 1.0.0
     */
    @JvmName("monthStepOpenEndRange")
    fun month(vararg rangeAndStep: Pair<OpenEndRange<Month>, Int>): CronExpressionBuilder {
        rangeAndStep.validate(lazyMessage = { "Invalid range of month step" }) { all { it.second in 1..12 } }

        months = when (months) {
            "" -> rangeAndStep.joinToString(",") { "${it.first.start.value}-${it.first.endExclusive.value - 1}" + if (it.second > 1) "/${it.second}" else "" }
            else -> "$months,${rangeAndStep.joinToString(",") { "${it.first.start.value}-${it.first.endExclusive.value - 1}" + if (it.second > 1) "/${it.second}" else "" }}"
        }
        return this
    }
    /**
     * Adds specific month ranges with optional steps to the cron expression.
     *
     * @param range A variable number of `OpenEndRange<Month>` values specifying the start
     *                     and exclusive end of the month ranges to be added. This defines the
     *                     months the cron expression should match.
     * @return The current `CronExpressionBuilder` instance with the updated month configuration.
     * @since 1.0.0
     */
    fun month(vararg range: OpenEndRange<Month>): CronExpressionBuilder {
        months = when (months) {
            "" -> range.joinToString(",") { "${it.start.value}-${it.endExclusive.value - 1}" }
            else -> "$months,${range.joinToString(",") { "${it.start.value}-${it.endExclusive.value - 1}" }}"
        }
        return this
    }
    /**
     * Configures the months and corresponding step values into the builder.
     *
     * @param monthAndStep A vararg of [Pair]s where each pair consists of a [Month] and an [Int] value representing the step (must be between 1 and 12).
     * @return The instance of the [CronExpressionBuilder] to enable method chaining.
     * @since 1.0.0
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If any step value is outside the range of 1 to 12.
     */
    fun month(vararg monthAndStep: Pair<Month, Int>): CronExpressionBuilder {
        monthAndStep.validate(lazyMessage = { "Invalid range of month step" }) { all { it.second in 1..12 } }

        months = when (months) {
            "" -> monthAndStep.joinToString(",") { "${it.first.value}/${it.second}" }
            else -> "$months,${monthAndStep.joinToString(",") { "${it.first.value}/${it.second}" }}"
        }
        return this
    }
    /**
     * Configures the schedule to include all months with the specified step.
     *
     * @param step the interval between months, must be in the range 1 to 12 (inclusive). Default is 1.
     * @return the updated Builder instance with the months configuration applied.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the provided step value is outside the valid range of 1 to 12.
     * @since 1.0.0
     */
    fun allMonths(step: Int = 1): CronExpressionBuilder {
        step.validate(lazyMessage = { "Invalid range of month step" }) { this in 1..12 }

        months = "*" + if (step > 1) "/$step" else ""
        return this
    }

    /**
     * Specifies the days of the week to be included in the schedule.
     *
     * @param values The days of the week to be included, represented as a vararg of DayOfWeek.
     * @return The Builder instance for method chaining.
     * @since 1.0.0
     */
    fun dayOfWeek(vararg values: DayOfWeek): CronExpressionBuilder {
        daysOfWeek = when (daysOfWeek) {
            "" -> values.joinToString(",") { +it.name.take(3) }
            else -> "$daysOfWeek,${values.joinToString(",") { +it.name.take(3) }}"
        }
        return this
    }
    /**
     * Configures the day of the week with specified ranges of months and step values for a recurring schedule.
     *
     * @param rangeAndStep pairs of a closed range of months and an associated step value.
     *        Each step value must be between 1 and 7 to represent valid intervals.
     * @return the updated Builder instance with the day of the week configuration applied.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any of the provided ranges or steps are invalid.
     * @since 1.0.0
     */
    @JvmName("dayOfWeekWithStepClosedRange")
    fun dayOfWeek(vararg rangeAndStep: Pair<ClosedRange<DayOfWeek>, Int>): CronExpressionBuilder {
        rangeAndStep.validate(lazyMessage = { "Invalid range of day of week step" }) { all { it.second in 1..7 } }

        daysOfWeek = when (daysOfWeek) {
            "" -> rangeAndStep.joinToString(",") { "${+it.first.start.name.take(3)}-${+it.first.endInclusive.name.take(3)}" + if (it.second > 1) "/${it.second}" else "" }
            else -> "$daysOfWeek,${rangeAndStep.joinToString(",") { "${+it.first.start.name.take(3)}-${+it.first.endInclusive.name.take(3)}" + if (it.second > 1) "/${it.second}" else "" }}"
        }
        return this
    }
    /**
     * Sets the days of the week in the Cron expression based on the provided ranges.
     *
     * The method accepts one or more ranges of [Month], specifying a start and end month for the range.
     * The range can also optionally include a step value for finer control over the increments.
     *
     * @param range one or more [ClosedRange] of [Month], representing the start and end bounds for
     * the days of the week with optional step increments.
     * @return the updated [CronExpressionBuilder] instance.
     * @since 1.0.0
     */
    fun dayOfWeek(vararg range: ClosedRange<DayOfWeek>): CronExpressionBuilder {
        daysOfWeek = when (daysOfWeek) {
            "" -> range.joinToString(",") { "${+it.start.name.take(3)}-${+it.endInclusive.name.take(3)}" }
            else -> "$daysOfWeek,${range.joinToString(",") { "${+it.start.name.take(3)}-${+it.endInclusive.name.take(3)}" }}"
        }
        return this
    }
    /**
     * Configures the day of the week with specified ranges of months and steps.
     *
     * @param rangeAndStep Variable number of pairs where each pair consists of an open-end range of months and a step size.
     * The step size determines the interval between days within the range, and it must be between 1 and 7 inclusive.
     * @return The updated builder with the specified day of the week configuration.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If any step size is not between 1 and 7.
     * @since 1.0.0
     */
    @JvmName("dayOfWeekWithStepOpenEndRange")
    fun dayOfWeek(vararg rangeAndStep: Pair<OpenEndRange<DayOfWeek>, Int>): CronExpressionBuilder {
        rangeAndStep.validate(lazyMessage = { "Invalid range of day of week step" }) { all { it.second in 1..7 } }

        daysOfWeek = when (daysOfWeek) {
            "" -> rangeAndStep.joinToString(",") { "${+it.first.start.name.take(3)}-${+DayOfWeek.entries[it.first.endExclusive.ordinal - 1].name.take(3)}" + if (it.second > 1) "/${it.second}" else "" }
            else -> "$daysOfWeek,${rangeAndStep.joinToString(",") { "${+it.first.start.name.take(3)}-${+DayOfWeek.entries[it.first.endExclusive.ordinal - 1].name.take(3)}" + if (it.second > 1) "/${it.second}" else "" }}"
        }
        return this
    }
    /**
     * Configures the day-of-week field for a cron expression using a variadic parameter of open-end ranges of months.
     *
     * @param range A variadic parameter specifying open-end ranges of months for which the days of the week should be configured.
     * Each range defines the start and exclusive end months. The corresponding days of the week for the range are represented
     * in a cron-compatible format.
     *
     * @return The updated instance of [CronExpressionBuilder] with the modified day-of-week field.
     *
     * @since 1.0.0
     */
    fun dayOfWeek(vararg range: OpenEndRange<DayOfWeek>): CronExpressionBuilder {
        daysOfWeek = when (daysOfWeek) {
            "" -> range.joinToString(",") { "${+it.start.name.take(3)}-${+DayOfWeek.entries[it.endExclusive.ordinal - 1].name.take(3)}" }
            else -> "$daysOfWeek,${range.joinToString(",") { "${+it.start.name.take(3)}-${+DayOfWeek.entries[it.endExclusive.ordinal - 1].name.take(3)}" }}"
        }
        return this
    }
    /**
     * Specifies a combination of months and corresponding weekdays within a valid range (1..7).
     * Allows chaining of multiple month/day-of-week pairs to build a schedule.
     *
     * @param monthAndStep Vararg parameter of pairs where each `Pair` consists of a `Month` and an `Int`
     *                     representing the day of the week (1 for Monday through 7 for Sunday).
     *                     All `Int` values must be within the range 1 to 7; otherwise, an exception is thrown.
     * @return The current `Builder` instance with the updated month/day-of-week values.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if any provided day of the week is outside the range 1..7.
     * @since 1.0.0
     */
    fun dayOfWeek(vararg monthAndStep: Pair<DayOfWeek, Int>): CronExpressionBuilder {
        monthAndStep.validate(lazyMessage = { "Invalid range of day of week step" }) { all { it.second in 1..7 } }

        daysOfWeek = when (daysOfWeek) {
            "" -> monthAndStep.joinToString(",") { "${+it.first.name.take(3)}/${it.second}" }
            else -> "$daysOfWeek,${monthAndStep.joinToString(",") { "${+it.first.name.take(3)}/${it.second}" }}"
        }
        return this
    }
    /**
     * Configures the builder to use all days of the week with the specified step interval.
     * Days of the week will be represented in the format where an optional step is applied to define the separation between days.
     *
     * @param step the interval between each day of the week. Must be in the range [1, 7]. Defaults to 1.
     * @return the current Builder instance with the updated configuration for days of the week.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if the step value is not within the valid range of 1 to 7.
     * @since 1.0.0
     */
    fun allDaysOfWeek(step: Int = 1): CronExpressionBuilder {
        step.validate(lazyMessage = { "Invalid range of day of week step" }) { this in 1..7 }

        daysOfWeek = "*" + if (step > 1) "/$step" else ""
        return this
    }
    /**
     * Marks the day of the week as undefined for the schedule configuration.
     * This method is only supported for specific types: `QUARTZ`, `SPRING_BEFORE_5_3`, and `SPRING`.
     * If invoked on an unsupported type, an `UnsupportedOperationException` is thrown.
     *
     * @return The current `Builder` instance with the day of the week set to undefined.
     * @since 1.0.0
     */
    fun undefinedDayOfWeek(): CronExpressionBuilder {
        type == Type.QUARTZ || type == Type.SPRING_BEFORE_5_3 || type == Type.SPRING || throw UnsupportedOperationException(
            "Undefined day of week is not supported by $type"
        )
        daysOfWeek = "?"
        return this
    }
    /**
     * Specifies the nth occurrence of a given day of the week in a schedule.
     * This method is supported only for the `QUARTZ`, `SPRING_BEFORE_5_3`, and `SPRING` types.
     *
     * @param n The occurrence number of the day of the week (e.g., 1 for the first occurrence, 2 for the second).
     * @param dayOfWeek The day of the week to be set (e.g., `DayOfWeek.MONDAY`).
     * @return The `Builder` instance with the configured nth occurrence of the specified day of the week.
     * @throws UnsupportedOperationException if the current type is not `QUARTZ`, `SPRING_BEFORE_5_3`, or `SPRING`.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException if the provided nth occurrence is outside the valid range of 1 to 5.
     * @since 1.0.0
     */
    fun nOccuranceOfWeekday(n: Int, dayOfWeek: DayOfWeek): CronExpressionBuilder {
        type == Type.QUARTZ || type == Type.SPRING_BEFORE_5_3 || type == Type.SPRING || throw UnsupportedOperationException(
            "Nth occurence of day of week is not supported by $type"
        )
        n.validate(lazyMessage = { "Invalid nth occurrence of day of week" }) { this in 1..5 }
        daysOfWeek = "${+dayOfWeek.name.take(3)}#$n"
        return this
    }
    /**
     * Configures the builder to specify the last occurrence of a given day of the week
     * for specific scheduling types (QUARTZ or SPRING).
     *
     * This method modifies the state of the builder to reflect the required scheduling
     * for the last instance of the specified weekday.
     *
     * @param dayOfWeek The day of the week for which the last occurrence is to be specified.
     *                  This must be a valid instance of DayOfWeek (e.g., MONDAY, TUESDAY).
     * @return The Builder instance with updated scheduling information.
     * @throws UnsupportedOperationException If the type of the builder is not QUARTZ or SPRING.
     * @since 1.0.0
     */
    fun lastOccuranceOfWeekday(dayOfWeek: DayOfWeek): CronExpressionBuilder {
        type == Type.QUARTZ || type == Type.SPRING || throw UnsupportedOperationException("Last day of week is not supported by $type")
        daysOfWeek = "${+dayOfWeek.name.take(3)}L"
        return this
    }

    /**
     * Sets the specified years for the builder configuration.
     * The method validates the provided years to ensure they fall within the range of 1970 to 2099.
     * It is applicable only when the type is set to `Type.QUARTZ`.
     *
     * @param values The years to set, which must be within the range 1970 to 2099.
     * @return The current instance of the Builder to allow for method chaining.
     * @throws UnsupportedOperationException If the current type is not `Type.QUARTZ`.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If any of the provided years are out of the valid range.
     * @since 1.0.0
     */
    fun year(vararg values: Int): CronExpressionBuilder {
        type == Type.QUARTZ || throw UnsupportedOperationException("Year is not supported by $type")

        values.forEach { it.validate(lazyMessage = { "Invalid range of years" }) { this in 1970..2099 } }

        years = when (years) {
            "" -> values.joinToString(",")
            else -> "$years,${values.joinToString(",")}"
        }
        return this
    }
    /**
     * Adds year specifications to the CronExpressionBuilder. The year ranges and step values are validated and formatted,
     * then appended to the current expression.
     *
     * @param values A vararg parameter, where each pair consists of an `IntRange` representing the range of years
     * (both start and end years must be between 1970 and 2099) and an `Int` representing the step interval (must be between 1 and 130).
     * @return Returns the current CronExpressionBuilder instance with the updated year configuration.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If any of the provided ranges or steps are invalid.
     * @since 1.0.0
     */
    @JvmName("yearIntRangeStep")
    fun year(vararg values: Pair<IntRange, Int>): CronExpressionBuilder {
        type == Type.QUARTZ || throw UnsupportedOperationException("Year is not supported by $type")

        values.forEach {
            it.validate(lazyMessage = { "Invalid range of years" }) { first.first in 1970..2099 && first.last in 1970..2099 }
            it.second.validate(lazyMessage = { "Invalid range of steps" }) { this in 1..130 }
        }

        years = when (years) {
            "" -> values.joinToString(",") { "${it.first}-${it.first.last}" + if (it.second > 1) "/${it.second}" else "" }
            else -> "$years,${values.joinToString(",") { "${it.first}-${it.first.last}" + if (it.second > 1) "/${it.second}" else "" }}"
        }
        return this
    }
    /**
     * Configures the years component for the Cron expression. This method accepts one or more
     * ranges of years, ensuring that the specified values are within the supported range (1970 to 2099).
     * If the type of Cron expression is not QUARTZ, an exception will be thrown.
     *
     * @param values One or more integer ranges representing the years to be included in the Cron expression.
     *               Each range must have its start and end values within the range of 1970 to 2099.
     * @return An instance of [CronExpressionBuilder] with the updated years component.
     * @throws UnsupportedOperationException If the type is not QUARTZ.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If any provided range falls outside the supported year range (1970 to 2099).
     * @since 1.0.0
     */
    fun year(vararg values: IntRange): CronExpressionBuilder {
        type == Type.QUARTZ || throw UnsupportedOperationException("Year is not supported by $type")

        values.forEach { it.validate(lazyMessage = { "Invalid range of years" }) { first in 1970..2099 && last in 1970..2099 } }

        years = when (years) {
            "" -> values.joinToString(",") { "${it.first}-${it.last}" }
            else -> "$years,${values.joinToString(",") { "${it.first}-${it.last}" }}"
        }
        return this
    }
    /**
     * Configures the year(s) with specified intervals in a Quartz-style schedule configuration.
     *
     * This method accepts pairs of integers where the first integer in the pair represents a starting
     * year, and the second integer represents the step or interval by which the subsequent years are calculated.
     * Only supported when the `Type` of the builder is `QUARTZ`.
     * Throws an `UnsupportedOperationException` if called for an unsupported type.
     * Valid years are from 1970 to 2099, and valid step values range from 1 to 130.
     *
     * @param yearAndStep A variable number of pairs, each consisting of a starting year and step interval.
     * The starting year must be between 1970 and 2099, and the step interval must be between 1 and 130.
     * @return The current instance of `Builder` with the configured year(s).
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If any provided year or step is invalid.
     * @since 1.0.0
     */
    @JvmName("yearIntStep")
    fun year(vararg yearAndStep: Int2): CronExpressionBuilder {
        type == Type.QUARTZ || throw UnsupportedOperationException("Year is not supported by $type")

        yearAndStep.forEach { it.validate(lazyMessage = { "Invalid years" }) { first in 1970..2099 && second in 1..130 } }

        years = when (years) {
            "" -> yearAndStep.joinToString(",") { "${it.first}/${it.second}" }
            else -> "$years,${yearAndStep.joinToString(",") { "${it.first}/${it.second}" }}"
        }
        return this
    }
    /**
     * Configures the Builder instance to include all years, optionally with a step value.
     *
     * This method sets the year configuration for a scheduling object, specifically for instances
     * where the type is `Type.QUARTZ`. It uses a step interval to determine the frequency of
     * selected years, where the default step is 1. The method ensures that the step value is within
     * the valid range of 1 to 130.
     *
     * @param step The interval between years to include, with a default value of 1.
     *             Must be between 1 and 130, inclusive.
     * @return The updated Builder instance for chaining further configurations.
     * @throws UnsupportedOperationException If the Builder instance is not of type `Type.QUARTZ`.
     * @throws dev.tommasop1804.kutils.exceptions.ValidationFailedException If the provided step parameter is outside the valid range.
     * @since 1.0.0
     */
    fun allYears(step: Int = 1): CronExpressionBuilder {
        type == Type.QUARTZ || throw UnsupportedOperationException("Year is not supported by $type")

        step.validate(lazyMessage = { "Invalid range of years" }) { this in 1..130 }

        years = "*" + if (step > 1) "/$step" else ""
        return this
    }

    /**
     * Builds a CronExpression string using the parameters provided to the builder instance.
     * The resulting expression represents a cron schedule defined by the builder fields.
     *
     * The generated cron expression includes the components for seconds, minutes, hours,
     * days of the month, months, and optionally years, formatted as follows:
     * ```text
     * [seconds (not for all)] [minutes] [hours] [daysOfMonth] [months] [years (optional)]
     * ```
     * Empty or unspecified components are replaced with their default wildcard values as needed.
     *
     * @return A [String] representing the created cron schedule.
     * @since 1.0.0
     */
    fun build() = buildString {
        append(seconds)
        append(if (type == Type.QUARTZ || type == Type.SPRING_BEFORE_5_3 || type == Type.SPRING) { if (seconds.isEmpty()) "* " else " " } else " ")
        append(minutes)
        append(if (minutes.isEmpty()) "* " else " ")
        append(hours)
        append(if (hours.isEmpty()) "* " else " ")
        append(daysOfMonth)
        append(if (daysOfMonth.isEmpty()) "* " else " ")
        append(months)
        append(if (months.isEmpty()) "* " else " ")
        append(daysOfWeek)
        append(if (daysOfWeek.isEmpty()) "*" else "")
        append(if (years.isEmpty()) "" else " $years")
    }

    /**
     * Represents various types of scheduling or task execution formats.
     *
     * The enum defines multiple formats commonly used for defining
     * execution schedules or cron expressions in different frameworks
     * and systems.
     *
     * @since 1.0.0
     */
    enum class Type {
        UNIX,
        CRON4J,
        QUARTZ,
        SPRING_BEFORE_5_3,
        SPRING
    }
}