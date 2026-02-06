package dev.tommasop1804.kutils.classes.time

import dev.tommasop1804.kutils.exceptions.TemporalException
import dev.tommasop1804.kutils.repeat
import dev.tommasop1804.kutils.validate
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalUnit
import kotlin.math.abs

/**
 * Represents a temporal interval defined purely by a `Duration` and an optional repetition count,
 * without direct reference to start or end points in time.
 *
 * This class serves as an abstraction for working with intervals defined by length, repetition,
 * and other temporal properties.
 *
 * Fields:
 * - `duration`: The base duration of the interval.
 * - `start`: Represents the starting point of the interval, if applicable.
 * - `end`: Represents the ending point of the interval, if applicable.
 * - `repetition`: Defines how many times the interval repeats.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = RepeatedTemporalInterval.Companion.Serialize::class)
@JsonDeserialize(using = RepeatedTemporalInterval.Companion.Deserialize::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = RepeatedTemporalInterval.Companion.OldSerialize::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = RepeatedTemporalInterval.Companion.OldDeserialize::class)
@Suppress("unused")
internal class TemporalIntervalOfDuration(
    override val duration: Duration,
    override val repetition: Int = 0,
) : TemporalAmount, RepeatedTemporalInterval, Serializable {

    /**
     * Represents the start point of a temporal duration.
     * In this implementation, the property is overridden to always throw a
     * TemporalException when an attempt is made to set its value,
     * as a duration cannot have a start point.
     *
     * @throws TemporalException if a value is assigned to this property
     * @since 1.0.0
     */
    override val start: Temporal
        get() = throw TemporalException("A duration cannot have a start")
    /**
     * Represents the starting point of a temporal object with repetition.
     * This property is meant to denote the start of a recurring temporal pattern.
     * Accessing this property is not supported for a duration as a duration inherently
     * does not possess a starting point.
     *
     * @throws TemporalException if accessed, since this property is
     * not applicable for a duration.
     *
     * @since 1.0.0
     */
    override val startWithRepetition: Temporal
        get() = throw TemporalException("A duration cannot have a start")
    /**
     * Represents the endpoint of a temporal interval. The `end` of a duration is not supported
     * and attempting to set this property will result in a `TemporalException`.
     *
     * This property is always `null` and cannot be assigned a value.
     *
     * @since 1.0.0
     */
    override val end: Temporal
        get() = throw TemporalException("A duration cannot have an end")
    /**
     * Represents the end of a temporal entity with repetition semantics. This property is overridden
     * to enforce the notion that a duration cannot have a defined endpoint, as durations are based
     * on a length of time rather than a fixed moment in time.
     *
     * Attempting to access this property will result in a TemporalException being thrown.
     *
     * @throws TemporalException always thrown when accessed, indicating a logical constraint
     *         where a duration lacks an end point.
     * @since 1.0.0
     */
    override val endWithRepetition: Temporal
        get() = throw TemporalException("A duration cannot have an end")
    /**
     * Represents the total duration including repetitions of a specific duration.
     *
     * Calculates the repeated duration based on the number of repetitions. If the
     * number of repetitions is zero, the result is equivalent to no additional
     * repetition. For positive repetition values, the duration is accumulated for
     * the specified number of iterations. Negative repetitions are treated as
     * positive values minus one repetition.
     *
     * @return The total duration considering the repetitions.
     * @throws TemporalException if the interval has an infinite repetition count.
     * @since 1.0.0
     */
    override val durationWithRepetition: Duration
        get() = when (repetition) {
            0 -> Duration()
            -1 -> throw TemporalException("The interval has an infinite repetition count")
            else -> duration.repeat(if (repetition == 0) 0 else (abs(repetition) - 1)) { it.plus(duration) }
        }

    init {
        validate(repetition >= -1) { "The repetition must be greater than or equal to -1" }
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
    }

    /**
     * Retrieves the value of the specified temporal unit from the current instance, considering repetition if specified.
     *
     * @param unit The temporal unit whose value is to be retrieved.
     * @param considerRepetition Whether repetition should be considered when calculating the value.
     * @return The value of the specified temporal unit, potentially factoring in repetitions.
     * @throws java.time.temporal.UnsupportedTemporalTypeException if the specified unit is not supported by the instance.
     * @since 1.0.0
     */
    override fun get(unit: TemporalUnit, considerRepetition: Boolean): Long = (if (considerRepetition) durationWithRepetition else duration).get(unit)

    /**
     * Retrieves the value of the specified temporal unit from the duration.
     * Assumes repetition should be considered in computations.
     *
     * @param unit the temporal unit to query, not null
     * @return the value of the specified temporal unit as a long
     * @since 1.0.0
     */
    override fun get(unit: TemporalUnit): Long = get(unit, true)

    /**
     * Retrieves the list of temporal units supported by the duration of this temporal interval.
     *
     * This method returns the list of temporal units that the `duration` field supports.
     *
     * @return A list of temporal units describing the supported granularity of the duration.
     * @since 1.0.0
     */
    override fun getUnits(): List<TemporalUnit> = duration.units

    /**
     * Adds the duration of this temporal interval to the specified temporal object.
     * The addition considers the repetition of the interval when applicable.
     *
     * @param temporal The temporal object to which the duration should be added.
     * @param considerRepetition A Boolean flag determining whether to include the repetition
     * of the interval in the duration addition.
     * @return A new temporal object with the duration of this interval added.
     * @since 1.0.0
     */
    override fun addTo(temporal: Temporal, considerRepetition: Boolean) = (if (considerRepetition) durationWithRepetition else duration).addTo(temporal)
    /**
     * Adds the temporal interval to the specified temporal object.
     *
     * @param temporal the temporal object to which the interval should be added.
     * @since 1.0.0
     */
    override fun addTo(temporal: Temporal) = addTo(temporal, true)

    /**
     * Subtracts the duration, computed by the interval's properties, from the specified temporal object.
     *
     * @param temporal The temporal object from which the duration will be subtracted.
     * @param considerRepetition A flag indicating whether the interval's repetition should be considered when computing the duration to subtract.
     * @return The resulting temporal object after the specified duration has been subtracted.
     * @since 1.0.0
     */
    override fun subtractFrom(temporal: Temporal, considerRepetition: Boolean) = (if (considerRepetition) durationWithRepetition else duration).subtractFrom(temporal)
    /**
     * Subtracts this temporal interval from the given temporal, taking repetition into account.
     *
     * @param temporal The temporal object from which this interval's duration will be subtracted.
     * @since 1.0.0
     */
    override fun subtractFrom(temporal: Temporal) = subtractFrom(temporal, true)

    /**
     * Converts the current `TemporalIntervalOfDuration` object into its string representation.
     *
     * The string is composed of the repetition value (if present) and the duration. If the
     * `omitZeroRepetitions` flag is set to `true`, and the repetition is `0`, the repetition
     * is excluded from the string.
     *
     * The format of the string is as follows:
     * - If the repetition is included, it is prefixed with "R". If the repetition value is not
     *   `-1`, it is explicitly appended to the prefix. For example, "R3/" for a repetition of 3.
     * - If the repetition is not included or omitted, only the duration is represented.
     *
     * @param omit1Repetition A flag indicating whether to exclude the repetition part of the
     *                             string if the repetition value is `0`.
     * @return A string representation of the `TemporalIntervalOfDuration`.
     * @since 1.0.0
     */
    override fun toString(omit1Repetition: Boolean) =
        (if (omit1Repetition && repetition == 1) "" else "R" + (if (repetition != -1) repetition.toString() else "") + "/") + duration.toString()

    /**
     * Provides a string representation of the temporal interval of duration.
     *
     * The output string may vary depending on whether zero repetitions are omitted,
     * as determined by the default behavior of the auxiliary `toString(Boolean)` method.
     * This implementation forwards the call to `toString(true)` to include additional context.
     *
     * @return The string representation of the temporal interval of duration.
     * @see toString(Boolean)
     * @since 1.0.0
     */
    override fun toString() = toString(true)

    /**
     * Converts the current instance to a `TemporalIntervalOfDuration` representation.
     * This function returns the current instance as it is already in the appropriate form.
     *
     * @return The current instance as a `TemporalIntervalOfDuration`.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfDuration() = this

    /**
     * Converts the current instance to a temporal interval of duration temporal.
     *
     * This operation is not supported for this type and will always throw a
     * TemporalException when invoked.
     *
     * @throws TemporalException always thrown since a duration cannot be
     * converted to a temporal interval of duration temporal.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfDurationTemporal() = throw TemporalException("A duration cannot be converted to a temporal interval of duration temporal")

    /**
     * Converts the current duration to a temporal interval of temporal duration.
     *
     * This method is not supported for instances of this class as a duration
     * cannot be directly converted into a temporal interval of temporal duration.
     * To represent a temporal interval, both start and end points in time must
     * be defined, which is outside the scope of a duration.
     *
     * @throws TemporalException always, as this conversion is not supported.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfTemporalDuration() = throw TemporalException("A duration cannot be converted to a temporal interval of temporal duration")

    /**
     * Converts this instance to a temporal interval of temporals.
     *
     * This operation is not supported for durations, as durations do not inherently contain specific
     * temporal boundaries or intervals that can be represented as temporals.
     *
     * @throws TemporalException Always thrown, because a duration cannot be converted
     * into a temporal interval of temporals.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfTemporals() = throw TemporalException("A duration cannot be converted to a temporal interval of temporals")

    /**
     * Overrides the `withStart` method to throw an `UnsupportedOperationException` as a duration
     * cannot have a start.
     *
     * @param start the starting temporal value (this parameter is ignored as it is unsupported)
     * @throws TemporalException always thrown since a duration cannot have a start
     * @since 1.0.0
     */
    override fun withStart(start: Temporal) = throw TemporalException("A duration cannot have a start")

    /**
     * Throws an exception as a duration-based interval cannot have an end.
     *
     * @param end The temporal object representing the end point of the interval, which is not applicable for a duration-based interval.
     * @since 1.0.0
     */
    override fun withEnd(end: Temporal) = throw TemporalException("A duration cannot have an end")

    /**
     * Creates a new instance of TemporalIntervalOfDuration with the specified duration,
     * while retaining the current repetition value.
     *
     * @param duration the duration to apply to the new temporal interval
     * @since 1.0.0
     */
    override fun withDuration(duration: Duration) = TemporalIntervalOfDuration(duration, repetition)

    /**
     * Creates a new {@code TemporalIntervalOfDuration} with the specified number of repetitions.
     *
     * @param repetition the number of repetitions to associate with this interval
     * @since 1.0.0
     */
    override fun withRepetition(repetition: Int) = TemporalIntervalOfDuration(duration, repetition)

    /**
     * Compares this instance to another object for equality.
     * Returns `true` if the other object is of the same type and has the same
     * property values for `repetition` and `duration`.
     *
     * @param other The object to compare with this instance.
     * @return `true` if the objects are equal, otherwise `false`.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TemporalIntervalOfDuration

        if (repetition != other.repetition) return false
        if (duration != other.duration) return false

        return true
    }

    /**
     * Computes a hash code for the object based on its `repetition` and `duration` properties.
     * The method uses a multiplication factor of 31 to combine the hash codes, ensuring a well-distributed result.
     *
     * @return the computed hash code for this object
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = repetition
        result = 31 * result + duration.hashCode()
        return result
    }
}
