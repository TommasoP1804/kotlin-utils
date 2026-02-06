package dev.tommasop1804.kutils.classes.time

import dev.tommasop1804.kutils.classes.time.Duration.Companion.durationTo
import dev.tommasop1804.kutils.exceptions.TemporalException
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalUnit

/**
 * Represents a temporal interval defined by a duration, start, and end, with optional repetition.
 *
 * This class provides methods to calculate the duration, manipulate start and end points,
 * and handle repetitions for structured temporal intervals.
 *
 * @property duration The duration of the temporal interval.
 * @property end The end temporal of the interval, defaulting to `LocalDate.MAX` if unset.
 * @property start The start temporal of the interval.
 * @property repetition The repetition factor for the interval, with -1 representing infinite repetition.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = RepeatedTemporalInterval.Companion.Serialize::class)
@JsonDeserialize(using = RepeatedTemporalInterval.Companion.Deserialize::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = RepeatedTemporalInterval.Companion.OldSerialize::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = RepeatedTemporalInterval.Companion.OldDeserialize::class)
@Suppress("unused")
internal class TemporalIntervalOfDurationTemporal(
    override val duration: Duration,
    override val end: Temporal,
    override val repetition: Int = 0
) : TemporalAmount, RepeatedTemporalInterval, Serializable {

    /**
     * Represents the starting temporal point of a time-based calculation or interval.
     * The value is derived by subtracting the specified duration from the end temporal point.
     *
     * @return the calculated starting temporal point
     * @since 1.0.0
     */
    override val start: Temporal
        get() = duration.subtractFrom(end)
    /**
     * Represents the starting temporal point after applying the repetition logic
     * on a specified duration and end temporal value. It calculates the starting
     * point by subtracting the duration (considering repetition) from the end temporal.
     * Throws an exception if the repetition is infinite (negative value).
     *
     * @throws TemporalException if the repetition is negative, indicating infinite repetition.
     * @since 1.0.0
     */
    override val startWithRepetition: Temporal
        get() {
            if (repetition < 0) throw TemporalException("The interval has an infinite repetition")
            return durationWithRepetition.subtractFrom(end)
        }
    /**
     * Represents the temporal endpoint with repetition for a given interval or duration.
     * This property holds the value of the `end` temporal with repetition context.
     * Overrides the base implementation to provide specific behavior.
     *
     * @return The temporal endpoint value with repetition applied.
     * @since 1.0.0
     */
    override val endWithRepetition: Temporal
        get() = end
    /**
     * Calculates and returns the total duration considering repetitions by iteratively subtracting the given duration.
     * The resulting duration is derived from applying the repetition factor to adjust the temporal frame.
     *
     * @return The computed duration adjusted for repetitions.
     * @since 1.0.0
     */
    override val durationWithRepetition: Duration
        get() {
            var newTemporal = end
            for (i in 1..repetition)
                newTemporal = duration.subtractFrom(newTemporal)
            return newTemporal durationTo end
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
     * Retrieves the value of the specified temporal unit from the duration of the temporal interval,
     * with an option to account for repetitions.
     *
     * @param unit The temporal unit whose value is to be retrieved.
     * @param considerRepetition If `true`, the computation includes repetitions of the base duration;
     * otherwise, only the base duration is considered.
     * @return The value of the specified temporal unit based on the duration, optionally including repetitions.
     * @since 1.0.0
     */
    override fun get(unit: TemporalUnit, considerRepetition: Boolean): Long = (if (considerRepetition) durationWithRepetition else duration).get(unit)

    /**
     * Retrieves the amount of time for the specified temporal unit, considering the base temporal interval.
     *
     * @param unit The temporal unit for which the amount of time is calculated.
     * @return The amount of time in the specified temporal unit.
     * @since 1.0.0
     */
    override fun get(unit: TemporalUnit): Long = get(unit, true)

    /**
     * Retrieves the list of temporal units that the duration is based on.
     *
     * This method returns the units in descending order of granularity, providing insight
     * into the structure of the duration for supported temporal operations.
     *
     * @return A list of temporal units defining the duration.
     * @since 1.0.0
     */
    override fun getUnits(): List<TemporalUnit> = duration.units

    /**
     * Adds the duration of this temporal interval to the specified temporal object, with an option
     * to consider repetitions. The method calculates the total duration based on whether repetitions
     * are to be included, and then performs the addition to the given temporal.
     *
     * @param temporal The temporal object to which the calculated duration should be added.
     * @param considerRepetition If `true`, the method includes the effect of repetitions when computing the duration;
     *                            otherwise, the base duration is used.
     * @since 1.0.0
     */
    override fun addTo(temporal: Temporal, considerRepetition: Boolean) = (if (considerRepetition) durationWithRepetition else duration).addTo(temporal)
    /**
     * Adds the temporal interval to the specified temporal object.
     *
     * Internally, this method uses a default behavior that includes consideration of the repetition
     * to calculate the updated temporal based on the overall interval configuration.
     *
     * @param temporal the temporal object to which the interval should be added
     * @return a new temporal object with the interval added
     * @since 1.0.0
     */
    override fun addTo(temporal: Temporal): Temporal = addTo(temporal, true)

    /**
     * Subtracts the total duration of the temporal interval from the given temporal object.
     * The duration calculation can optionally consider repetitions, in which case the base duration
     * is applied multiple times based on the repetition count.
     *
     * @param temporal The temporal object from which the duration will be subtracted.
     * @param considerRepetition If `true`, repetitions are considered when calculating the total duration;
     * otherwise, only the base duration is subtracted.
     * @return The resulting temporal object after subtracting the calculated duration.
     * @since 1.0.0
     */
    override fun subtractFrom(temporal: Temporal, considerRepetition: Boolean) = (if (considerRepetition) durationWithRepetition else duration).subtractFrom(temporal)
    /**
     * Subtracts the interval duration from the given temporal object. This method delegates to the
     * overloaded `subtractFrom` method and defaults to including repetitions in the calculation.
     *
     * @param temporal the temporal object from which the interval duration will be subtracted
     * @return a new temporal object after subtracting the interval duration
     * @since 1.0.0
     */
    override fun subtractFrom(temporal: Temporal): Temporal = subtractFrom(temporal, true)

    /**
     * Converts the temporal interval information to its string representation, optionally omitting
     * repetitions if their value is zero.
     *
     * The string returned is formatted as "R<repetition>/<duration>/<end>", where:
     * - "R<repetition>/" is excluded if `omitZeroRepetitions` is true and the repetition count is zero.
     * - "<repetition>" is excluded when its value is -1.
     *
     * @param omit1Repetition A boolean indicating whether to exclude repetitions in the string
     * representation when the repetition count is zero.
     * @return A string representation of the temporal interval.
     * @since 1.0.0
     */
    override fun toString(omit1Repetition: Boolean) = (if (omit1Repetition && repetition == 1) "" else ("R" + if (repetition != -1) repetition.toString() else "") + "/") + ("$duration/${end}")
    /**
     * Converts the current temporal interval object into its string representation.
     *
     * This method provides a default representation of the temporal interval
     * by calling the `toString` method with the `omitZeroRepetitions` parameter
     * set to `true`. The included details typically depend on the behavior implemented
     * in the `toString(omitZeroRepetitions: Boolean)` method of the class.
     *
     * @return A string representation of the temporal interval.
     * @since 1.0.0
     */
    override fun toString() = toString(true)

    /**
     * Converts the current temporal interval into a `TemporalIntervalOfDuration` representation.
     * This method utilizes the `duration` and `repetition` properties of the instance
     * to generate the corresponding interval.
     *
     * @return A `TemporalIntervalOfDuration` object representing the interval based on the current instance.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfDuration() = TemporalIntervalOfDuration(duration, repetition)

    /**
     * Converts the current instance to a temporal interval of type `TemporalIntervalOfDurationTemporal`.
     * Invoking this method on an instance of `TemporalIntervalOfDurationTemporal` will return the same instance.
     * This method is useful for type transitions or enforcing type consistency.
     *
     * @return The current instance as a `TemporalIntervalOfDurationTemporal`.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfDurationTemporal() = this

    /**
     * Converts the current temporal interval to an instance of `TemporalIntervalOfTemporalDuration`.
     *
     * This method creates a `TemporalIntervalOfTemporalDuration` object based on the starting temporal value
     * (`getStart(false)`), the total duration of the interval (`toDuration(false)`), and the repetition count.
     * The resulting object represents a temporal interval that encapsulates the temporal start, duration,
     * and repetition as temporal-based components.
     *
     * @return A `TemporalIntervalOfTemporalDuration` instance that represents the temporal interval.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfTemporalDuration() = TemporalIntervalOfTemporalDuration(start, duration, repetition)

    /**
     * Converts the current temporal interval, including its start and end temporals,
     * into an instance of the `TemporalIntervalOfTemporals` class.
     *
     * The method determines the starting temporal by invoking `getStart(true)` to consider
     * potential repetitions and uses the end temporal directly as configured in the interval.
     * It constructs a `TemporalIntervalOfTemporals` instance using these values.
     *
     * @return An instance of `TemporalIntervalOfTemporals` representing the current interval.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfTemporals() = TemporalIntervalOfTemporals(startWithRepetition, end)

    /**
     * Returns a new temporal interval with the specified start time.
     * The start parameter is used to calculate the duration between
     * the new start time and the current end time (or `LocalDate.MAX` if the end is not set).
     * The existing repetition count remains unchanged.
     *
     * @param start the new start temporal object to set for the interval.
     *              This should be a valid instance of [Temporal].
     * @return a new instance of `TemporalIntervalOfDurationTemporal` with the updated start value.
     * @since 1.0.0
     */
    override fun withStart(start: Temporal) = TemporalIntervalOfDurationTemporal(start durationTo end, end, repetition)

    /**
     * Creates a new instance of the temporal interval with the provided end temporal value, while maintaining the
     * original interval's duration and repetition.
     *
     * @param end The new end temporal value to be set for the interval. This value determines the exclusive end point
     *            of the interval duration computation.
     * @return A new instance of the temporal interval with the updated end temporal value.
     * @since 1.0.0
     */
    override fun withEnd(end: Temporal) = TemporalIntervalOfDurationTemporal(start durationTo end, end, repetition)

    /**
     * Creates a new temporal interval with the specified duration while retaining other properties
     * like end and repetition of the current instance.
     *
     * @param duration The new duration to set for the temporal interval.
     * @return A new instance of TemporalIntervalOfDurationTemporal with the updated duration.
     * @since 1.0.0
     */
    override fun withDuration(duration: Duration) = TemporalIntervalOfDurationTemporal(duration, end, repetition)

    /**
     * Creates a new instance of `TemporalIntervalOfDurationTemporal` with the specified repetition count.
     * The repetition count determines how many times the temporal interval should be considered for certain
     * operations, such as calculating total duration or repeating intervals.
     *
     * @param repetition The number of repetitions to apply to the temporal interval. Must be a non-negative integer.
     * @since 1.0.0
     */
    override fun withRepetition(repetition: Int) = TemporalIntervalOfDurationTemporal(duration, end, repetition)

    /**
     * Compares this instance with another object for equality.
     * This method checks if the other object has the same type and
     * corresponding values for all relevant fields of the temporal interval.
     *
     * @param other The object to compare with this instance.
     * @return `true` if the objects are equal, otherwise `false`.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TemporalIntervalOfDurationTemporal

        if (repetition != other.repetition) return false
        if (duration != other.duration) return false
        if (end != other.end) return false

        return true
    }

    /**
     * Computes the hash code for this object based on its properties.
     * The hash code is generated by combining the hash codes of the `repetition`, `duration`, and `end` fields.
     * A multiplication factor of 31 is used to ensure a well-distributed hash code.
     *
     * @return the hash code value for this object
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = repetition
        result = 31 * result + duration.hashCode()
        result = 31 * result + (end.hashCode())
        return result
    }
}