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
 * Represents a temporal interval defined by a starting temporal value, a duration, and an optional repetition count.
 * This class models an interval with customization options such as repetition and various temporal operations.
 *
 * The interval's `start` represents the starting temporal value, and `duration` represents the time span of the base interval.
 * The `repetition` parameter determines how many times the interval repeats.
 *
 * Note that the class implements the `TemporalAmount`, `RepeatedTemporalInterval`, and `Serializable` interfaces,
 * allowing it to handle temporal amounts, repeatable intervals, and serialization for persistence and transfer.
 *
 * @property start Defines the starting point of the interval. It can be any object implementing the `Temporal` interface.
 * Changing the `start` value influences the calculations for the `end` and the overall interval.
 *
 * @property duration Represents the base duration of the interval. This is used to compute the total interval based
 * on repetition settings. It must not be negative.
 *
 * @property repetition Determines the number of repetitions for the interval. A repetition of -1 signifies infinite repetitions.
 * A positive value indicates repeated occurrences, while 0 denotes no repetition.
 *
 * This class includes several utility methods that allow manipulation and retrieval of interval-related properties,
 * including start, end, duration, and repetitions. It also supports operations like adding to or subtracting from a temporal,
 * transforming into different interval representations, and generating string representations.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = RepeatedTemporalInterval.Companion.Serialize::class)
@JsonDeserialize(using = RepeatedTemporalInterval.Companion.Deserialize::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = RepeatedTemporalInterval.Companion.OldSerialize::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = RepeatedTemporalInterval.Companion.OldDeserialize::class)
@Suppress("unused")
internal class TemporalIntervalOfTemporalDuration (
    override val start: Temporal,
    override val duration: Duration,
    override val repetition: Int = 0
) : TemporalAmount, RepeatedTemporalInterval, Serializable {

    /**
     * Represents the starting point with repetition for a temporal or time-based sequence.
     * The exact nature of the repetition is determined by the implementation.
     *
     * @return The starting temporal value associated with the sequence, taking into account repetition.
     * @since 1.0.0
     */
    override val startWithRepetition: Temporal
        get() = start
    /**
     * The ending temporal point calculated by adding the duration to the starting temporal point.
     * This property returns the computed value without storing it.
     *
     * @return the computed temporal point representing the end.
     * @since 1.0.0
     */
    override val end: Temporal
        get() = duration.addTo(start)
    /**
     * Represents the temporal value that marks the end of an interval,
     * calculated by considering the repetition count applied to its duration.
     *
     * If the `repetition` is less than 0, indicating an infinite repetition,
     * an exception is thrown as the interval cannot determine a finite end.
     *
     * @throws TemporalException if the repetition count is negative.
     * @return The calculated end temporal value of the interval.
     * @since 1.0.0
     */
    override val endWithRepetition: Temporal
        get() {
            if (repetition < 0) throw TemporalException("The interval has an infinite repetition")
            return durationWithRepetition.addTo(start)
        }
    /**
     * Represents the total duration calculated by applying a specified repetition
     * over a given duration starting from an initial time point.
     *
     * This property calculates the cumulative duration by iteratively adding a base
     * `duration` to the starting point `start` for a number of times defined by `repetition`.
     * The final duration is derived from the difference between the initial `start`
     * and the computed end time.
     *
     * @since 1.0.0
     */
    override val durationWithRepetition: Duration
        get() {
            var newTemporal = start
            for (i in 1..repetition)
                newTemporal = duration.addTo(newTemporal)
            return start durationTo newTemporal
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
     * Retrieves the value of the specified temporal unit from the current instance,
     * considering whether to account for repetitions in the temporal interval's duration.
     *
     * @param unit The temporal unit whose value is to be retrieved.
     * @param considerRepetition If `true`, includes the effect of repetitions when calculating the duration value.
     * @return The value of the specified temporal unit, potentially adjusted for repetitions.
     * @throws java.time.temporal.UnsupportedTemporalTypeException if the specified unit is not supported by the instance.
     * @since 1.0.0
     */
    override fun get(unit: TemporalUnit, considerRepetition: Boolean) = (if (considerRepetition) durationWithRepetition else duration).get(unit)

    /**
     * Retrieves the value of the specified temporal unit from the temporal interval.
     * By default, it includes the effect of repetitions.
     *
     * @param unit The temporal unit whose value is to be retrieved.
     * @return The long value of the specified temporal unit.
     * @since 1.0.0
     */
    override fun get(unit: TemporalUnit) = get(unit, true)

    /**
     * Retrieves the list of temporal units for the duration associated with this temporal interval.
     *
     * The returned list represents the time-based units supported by the duration,
     * allowing operations such as calculations and conversions to be performed
     * using these units.
     *
     * @return A list of `TemporalUnit` instances representing the supported time units for the duration.
     * @since 1.0.0
     */
    override fun getUnits(): List<TemporalUnit> = duration.units

    /**
     * Adds the calculated total duration of this temporal interval to the specified temporal object.
     * The duration is calculated using the `toDuration` method, considering whether repetitions should
     * be included in the calculation. The addition is performed in terms of the appropriate temporal units.
     *
     * @param temporal The temporal object to which the duration should be added.
     * @param considerRepetition If `true`, the total duration is calculated including repetitions. If `false`,
     * only the base duration is added to the temporal.
     * @since 1.0.0
     */
    override fun addTo(temporal: Temporal, considerRepetition: Boolean) = (if (considerRepetition) durationWithRepetition else duration).addTo(temporal)

    /**
     * Adds the temporal interval to the specified temporal object.
     *
     * This method applies the duration of the interval, including any repetitions if applicable,
     * to the provided temporal object.
     *
     * @param temporal The temporal object to which the interval will be added.
     * @since 1.0.0
     */
    override fun addTo(temporal: Temporal) = addTo(temporal, true)

    /**
     * Subtracts the calculated duration of the temporal interval from the specified temporal object.
     * The duration is determined using the `toDuration` method and optionally considers repetitions.
     *
     * @param temporal The temporal object from which the calculated duration will be subtracted.
     * @param considerRepetition Indicates whether to consider repetitions when calculating the duration.
     *                           If `true`, repetitions are included; otherwise, only the base duration is used.
     * @return The modified temporal object after subtracting the calculated duration.
     * @since 1.0.0
     */
    override fun subtractFrom(temporal: Temporal, considerRepetition: Boolean) = (if (considerRepetition) durationWithRepetition else duration).subtractFrom(temporal)

    /**
     * Subtracts the interval or duration represented by this instance from the given temporal object.
     *
     * @param temporal The temporal object from which the interval or duration will be subtracted.
     * @since 1.0.0
     */
    override fun subtractFrom(temporal: Temporal) = subtractFrom(temporal, true)

    /**
     * Converts the current temporal interval and its duration into a string representation.
     *
     * The resultant string is constructed based on the values of the `repetition`, `start`, and `duration` fields.
     * The format includes the prefix `R` followed by the repetition count (if applicable), the start temporal, and
     * the duration value.
     *
     * @param omit1Repetition If `true`, the repetition component is omitted from the string representation
     *                             when the repetition count is `0`.
     * @return A string representation of the temporal interval with its duration.
     * @since 1.0.0
     */
    override fun toString(omit1Repetition: Boolean) =
        (if (omit1Repetition && repetition == 1) "" else ("R" + if (repetition != -1) repetition.toString() else "") + "/") + start.toString() + "/" + duration.toString()

    /**
     * Returns a string representation of the temporal interval, optionally including details for zero repetitions.
     *
     * This method delegates to an overloaded `toString` function, passing `true` as the argument to include
     * the default behavior of handling zero repetitions in the string representation.
     *
     * @return A string that represents the temporal interval, including formatted details such as start, duration,
     * and repetition information, with adjustments for zero repetitions as applicable.
     * @since 1.0.0
     */
    override fun toString() = toString(true)

    /**
     * Converts the current temporal interval into an equivalent representation as a `TemporalIntervalOfDuration`.
     *
     * This method utilizes the current interval's duration and repetition values to generate a
     * `TemporalIntervalOfDuration` object. The resulting interval is based on the computed duration
     * (without considering zero repetitions) and the existing repetition count.
     *
     * @return An instance of `TemporalIntervalOfDuration` representing the current interval's duration and repetitions.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfDuration() = TemporalIntervalOfDuration(duration, repetition)

    /**
     * Converts the current instance to a `TemporalIntervalOfDurationTemporal`.
     *
     * The resulting object encapsulates the duration, end temporal, and repetition data
     * derived from the current interval.
     *
     * @return A `TemporalIntervalOfDurationTemporal` that represents the duration, end time, and repetitions of this interval.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfDurationTemporal() = TemporalIntervalOfDurationTemporal(duration, end, repetition)

    /**
     * Converts the current instance to a `TemporalIntervalOfTemporalDuration`.
     * This method effectively returns the current instance,
     * as it is already of the required type.
     *
     * @return The current `TemporalIntervalOfTemporalDuration` instance.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfTemporalDuration() = this

    /**
     * Converts the current temporal interval to an instance of `TemporalIntervalOfTemporals` by utilizing the calculated
     * start and end temporals. The start and end values are determined based on the given logic for repetitions.
     *
     * This method retrieves the start temporal without considering repetitions and the end temporal while considering them.
     * The resulting `TemporalIntervalOfTemporals` encapsulates these temporals, representing a specific temporal interval.
     *
     * @return A `TemporalIntervalOfTemporals` object representing the interval defined by the start and end temporals.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfTemporals() = TemporalIntervalOfTemporals(start, endWithRepetition)

    /**
     * Returns a new instance of `TemporalIntervalOfTemporalDuration` with the specified start temporal object.
     * The duration of the interval is recalculated using the new start temporal object and the current end temporal object.
     *
     * @param start The new start temporal object for the interval. Must not be null.
     * @return A new instance of `TemporalIntervalOfTemporalDuration` with the updated start temporal object.
     * @since 1.0.0
     */
    override fun withStart(start: Temporal) = TemporalIntervalOfTemporalDuration(start, start durationTo end, repetition)

    /**
     * Creates a new instance of the temporal interval with the specified end time.
     * The duration of the interval is recalculated from the start time to the provided end time.
     *
     * @param end The end temporal object used to define the new temporal interval.
     * @return A new instance of the temporal interval with the updated end time.
     * @since 1.0.0
     */
    override fun withEnd(end: Temporal) = TemporalIntervalOfTemporalDuration(start, start durationTo end, repetition)

    /**
     * Creates a new instance of TemporalIntervalOfTemporalDuration with the specified duration, maintaining the
     * same start time and repetition settings.
     *
     * @param duration The temporal duration to use for the new temporal interval. It specifies the length of the interval.
     * @since 1.0.0
     */
    override fun withDuration(duration: Duration) = TemporalIntervalOfTemporalDuration(start, duration, repetition)

    /**
     * Creates a copy of the current `TemporalIntervalOfTemporalDuration` instance with the specified repetition count.
     *
     * @param repetition The new repetition count to apply to the interval. Must be a non-negative integer.
     *                   Represents the number of times the duration of the interval is repeated.
     * @since 1.0.0
     */
    override fun withRepetition(repetition: Int) = TemporalIntervalOfTemporalDuration(start, duration, repetition)

    /**
     * Compares this instance of `TemporalIntervalOfTemporalDuration` with another object for equality.
     * Returns `true` if the other object is of the same type and all fields (`repetition`, `start`, and `duration`) are equal.
     *
     * @param other The object to compare with this instance.
     * @return `true` if the objects are equal, otherwise `false`.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TemporalIntervalOfTemporalDuration

        if (repetition != other.repetition) return false
        if (start != other.start) return false
        if (duration != other.duration) return false

        return true
    }

    /**
     * Computes a hash code for this object based on its properties.
     * The hash code is calculated using the values of `repetition`, `start`, and `duration`,
     * with a multiplication factor of 31 for combining their hash codes.
     *
     * @return the computed hash code value for this object
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = repetition
        result = 31 * result + (start.hashCode())
        result = 31 * result + duration.hashCode()
        return result
    }
}