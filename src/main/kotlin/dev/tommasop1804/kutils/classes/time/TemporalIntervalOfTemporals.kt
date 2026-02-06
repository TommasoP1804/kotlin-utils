package dev.tommasop1804.kutils.classes.time

import dev.tommasop1804.kutils.classes.time.Duration.Companion.durationTo
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import java.time.LocalDate
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalUnit

/**
 * Represents a temporal interval defined by a start and end [Temporal] object.
 * This class provides a variety of functions to manipulate and transform temporal
 * intervals into different representations or formats. It implements [TemporalAmount],
 * [TemporalInterval], and supports serialization.
 *
 * This class considers the `start` as inclusive and `end` as exclusive when computing
 * intervals or durations.
 *
 * @constructor Creates a temporal interval with a specified [start] and [end] temporal.
 * Both [start] and [end] can be `null`, in which case a default value of [LocalDate.MIN]
 * will be used where applicable.
 *
 * @property start the starting [Temporal] of this interval, inclusive.
 * @property end the ending [Temporal] of this interval, exclusive.
 * @since 1.0.0
 */
@JsonSerialize(using = TemporalInterval.Companion.Serialize::class)
@JsonDeserialize(using = TemporalInterval.Companion.Deserialize::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = TemporalInterval.Companion.OldSerialize::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = TemporalInterval.Companion.OldDeserialize::class)
@Suppress("unused")
internal class TemporalIntervalOfTemporals (
    override val start: Temporal,
    override val end: Temporal
) : TemporalAmount, TemporalInterval, Serializable {

    /**
     * Represents the duration of the temporal interval, calculated as the time difference
     * between the `start` and `end` fields.
     *
     * This property computes the duration using the `durationTo` extension function, which calculates the
     * `Duration` between the `start` and `end` temporal objects.
     *
     * @return The duration of the interval as a `Duration` object.
     * @since 1.0.0
     */
    override val duration
        get() = start durationTo end

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
     * Retrieves the value of the specified temporal unit from the current instance.
     *
     * @param unit The temporal unit whose value is to be retrieved.
     * @return The value of the specified temporal unit.
     * @throws java.time.temporal.UnsupportedTemporalTypeException if the specified unit is not supported by the instance.
     * @since 1.0.0
     */
    override fun get(unit: TemporalUnit) = duration.get(unit)

    /**
     * Returns the list of time units supported by the duration of this temporal interval.
     *
     * This method delegates to the `toDuration` function to compute the duration of the interval
     * and retrieves the associated units from the `Duration` object.
     *
     * @return A list of `TemporalUnit` instances, representing the supported time units.
     * @since 1.0.0
     */
    override fun getUnits() = duration.units

    /**
     * Returns the current instance as a TemporalIntervalOfTemporals.
     *
     * This method is intended to provide a type-safe conversion or representation of the current object in the context of temporal intervals.
     * It ensures that the object can be treated explicitly as a TemporalIntervalOfTemporals without altering its state.
     *
     * @return the current instance as a TemporalIntervalOfTemporals
     *
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfTemporals() = this

    /**
     * Converts the current temporal interval into an interval represented as a duration.
     * This method computes the interval in terms of its duration by delegating to the
     * `toDuration` function of the class and assigns an anchored value of `0` for the interval.
     *
     * @return A [TemporalIntervalOfDuration] representing the interval as a duration.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfDuration() = TemporalIntervalOfDuration(duration)

    /**
     * Converts the current temporal interval instance into a `TemporalIntervalOfDurationTemporal`,
     * which pairs the duration of the interval with its end point.
     *
     * This method computes the duration of the interval using the `toDuration` method
     * and combines it with the `end` temporal to create the resulting object.
     *
     * @return a `TemporalIntervalOfDurationTemporal` instance representing the duration and end point of the interval.
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfDurationTemporal() = TemporalIntervalOfDurationTemporal(duration, end)

    /**
     * Converts the current `TemporalIntervalOfTemporals` instance into a
     * `TemporalIntervalOfTemporalDuration`, using the starting temporal
     * value and deriving a duration based on the implementation of `toDuration`.
     *
     * This method provides a representation of the interval in terms of a start
     * temporal and a calculated duration.
     *
     * @return an instance of `TemporalIntervalOfTemporalDuration` representing
     *         the temporal interval as a start temporal and a duration.
     *
     * @since 1.0.0
     */
    override fun toTemporalIntervalOfTemporalDuration() = TemporalIntervalOfTemporalDuration(start, duration)

    /**
     * Returns a new instance of `TemporalIntervalOfTemporals` with the specified start temporal value.
     *
     * @param start the temporal object representing the new start point of the interval
     * @since 1.0.0
     */
    override fun withStart(start: Temporal) = TemporalIntervalOfTemporals(start, end)

    /**
     * Returns a new TemporalIntervalOfTemporals instance with the specified end temporal.
     *
     * @param end the Temporal instance that represents the end of the interval
     * @since 1.0.0
     */
    override fun withEnd(end: Temporal) = TemporalIntervalOfTemporals(start, end)

    /**
     * Creates a new instance of TemporalIntervalOfTemporals with the specified duration added to the start.
     *
     * @param duration the duration to add to the start temporal. Must not be null.
     * @return a new TemporalIntervalOfTemporals instance with the specified duration applied.
     * @since 1.0.0
     */
    override fun withDuration(duration: Duration) = TemporalIntervalOfTemporals(start, end.plus(duration))

    /**
     * Adds the duration represented by this temporal interval to the specified temporal object.
     * The addition is based on the duration deriving from the start and end of this interval.
     *
     * @param temporal The temporal object to which the duration will be added.
     * @return A new temporal object with the duration added.
     * @since 1.0.0
     */
    override fun addTo(temporal: Temporal) = duration.addTo(temporal)

    /**
     * Subtracts the duration represented by this interval from the specified temporal object.
     * The operation uses the duration of this interval, computed based on its start and end fields,
     * and proceeds to subtract it from the given temporal object.
     *
     * @param temporal The temporal object from which the duration of this interval will be subtracted.
     * @return The resulting temporal object after subtracting the interval's duration.
     * @since 1.0.0
     */
    override fun subtractFrom(temporal: Temporal) = duration.subtractFrom(temporal)

    /**
     * Returns a string representation of the temporal interval by concatenating the `start` and `end` fields
     * separated by a forward slash (`/`).
     *
     * This representation is intended to provide a human-readable summary of the interval.
     *
     * @return A string combining the `start` and `end` values with a `/` delimiter.
     * @since 1.0.0
     */
    override fun toString() = "${start}/${end}"

    /**
     * Compares this instance with the specified object to determine if they are equal.
     *
     * @param other The object to be compared with this instance.
     * @return `true` if the specified object is equal to this instance, `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TemporalIntervalOfTemporals

        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    /**
     * Returns the hash code value for this object.
     * The hash code is calculated based on the `start` and `end` fields,
     * ensuring consistent behavior with the `equals` method.
     *
     * @return The hash code value for this object.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + (end.hashCode())
        return result
    }
}