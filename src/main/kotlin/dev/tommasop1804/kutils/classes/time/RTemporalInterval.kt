@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.time

import dev.tommasop1804.kutils.classes.time.Duration.Companion.durationTo
import dev.tommasop1804.kutils.classes.time.RTemporalInterval.Companion.restrictedIntervalTo
import dev.tommasop1804.kutils.classes.time.TemporalInterval.Companion.intervalTo
import java.time.temporal.Temporal
import java.time.temporal.TemporalUnit

/**
 * Represents a temporal interval between two points in time, defined by a start and an end.
 * This class provides operations to calculate the duration, manipulate the interval, and
 * supports immutability of start and end temporal points.
 *
 * @param T1 the type of the start temporal point, which must implement [Temporal].
 * @param T2 the type of the end temporal point, which must implement [Temporal].
 *
 * @property start the starting point of the temporal interval.
 * @property end the ending point of the temporal interval.
 * @since 3.4.0
 * @author Tommaso Pastorelli
 */
@Suppress("UNCHECKED_CAST", "RedundantValueArgument")
class RTemporalInterval<T1 : Temporal, T2 : Temporal> private constructor(
    override val start: T1,
    override val end: T2,
    private var startDuration: Boolean = false,
    private var endDuration: Boolean = false
) : TemporalInterval {

    /**
     * Represents the computed duration between the start and end points.
     *
     * The duration is calculated as the time interval from the designated
     * `start` point to the `end` point using the `durationTo` operation.
     * This value provides a measure of the elapsed time or distance depending
     * on the context in which it is used.
     * @since 3.4.0
     */
    override val duration = start durationTo end

    /**
     * Represents a temporal interval defined by a start and an end point in time.
     * The interval is typically used to denote a duration or range between two temporal boundaries.
     *
     * The `temporalInterval` assumes that both `start` and `end` are valid temporal values,
     * with `start` occurring before or simultaneously with `end`.
     * @since 3.4.0
     */
    val temporalInterval = start intervalTo end

    /**
     * Secondary constructor for the RTemporalInterval class.
     *
     * This constructor initializes an instance of RTemporalInterval with the specified start and end parameters
     * while defaulting the temporal bounds (inclusive or exclusive) to false.
     *
     * @param start The starting point of the temporal interval.
     * @param end The ending point of the temporal interval.
     * @since 3.4.0
     */
    constructor(start: T1, end: T2) : this(start, end, false, false)

    companion object {
        /**
         * Creates a temporal interval between the invoking temporal object and the specified end temporal object.
         *
         * This function constructs an instance of `RTemporalInterval` with the current temporal object as the start
         * and the provided temporal object as the end. The operation is done using infix notation for better readability
         * in interval creation scenarios.
         *
         * @param end The temporal object that marks the end of the interval.
         * @since 3.4.0
         */
        infix fun <T1 : Temporal, T2 : Temporal> T1.restrictedIntervalTo(end: T2) = RTemporalInterval(this, end)
        /**
         * Restricts an interval starting from the current temporal instance to the specified duration.
         *
         * @param duration The duration that limits the length of the resulting temporal interval.
         * @return A new instance of RTemporalInterval representing the interval from the current temporal instance
         *         to the calculated end point after adding the given duration.
         * @since 3.4.0
         */
        infix fun <T1 : Temporal> T1.restrictedIntervalTo(duration: Duration) =
            RTemporalInterval(this, plus(duration) as T1, endDuration = true)
        /**
         * Creates a restricted temporal interval using the specified duration and end temporal object.
         * The interval will start before the end by the given duration.
         *
         * @param end The temporal object that specifies the end point of the interval.
         *            Must be of a type that implements Temporal.
         * @return A new instance of RTemporalInterval representing the interval.
         * @since 3.4.0
         */
        infix fun <T2 : Temporal> Duration.restrictedIntervalTo(end: T2) =
            RTemporalInterval(minus(this) as T2, end, startDuration = true)
    }

    /**
     * Returns a string representation of the object.
     *
     * This method overrides the default toString implementation
     * to return the string representation of the `temporalInterval` property.
     *
     * @return A string describing the `temporalInterval`.
     * @since 3.4.0
     */
    override fun toString() = if (startDuration) toTemporalIntervalOfDurationTemporal().toString()
    else if (endDuration) toTemporalIntervalOfTemporalDuration().toString()
    else toTemporalIntervalOfTemporals().toString()

    /**
     * Retrieves the value associated with the specified temporal unit from the temporal interval.
     *
     * @param unit the TemporalUnit whose value is to be retrieved
     * @return the value of the specified TemporalUnit within the temporal interval
     * @since 3.4.0
     */
    override fun get(unit: TemporalUnit) = temporalInterval[unit]

    /**
     * Adds this temporal interval to the specified temporal object.
     *
     * This method applies the temporal interval encapsulated by the `temporalInterval`
     * to the provided temporal object, modifying it to reflect the addition.
     *
     * @param temporal the target temporal object to which the interval will be added
     * @return the modified temporal object after applying this interval
     * @throws java.time.DateTimeException if the addition produces an invalid or unsupported result
     * @throws ArithmeticException if adding the interval causes numeric overflow
     * @since 3.4.0
     */
    override fun addTo(temporal: Temporal) = temporalInterval.addTo(temporal)

    /**
     * Subtracts the temporal interval represented by this instance from the specified temporal object.
     *
     * The operation adjusts the provided temporal object by subtracting the duration of this interval.
     * The resulting temporal reflects the state after this subtraction is applied.
     *
     * @param temporal the temporal object from which the interval will be subtracted
     * @return the resulting temporal object after the subtraction
     * @since 3.4.0
     */
    override fun subtractFrom(temporal: Temporal) = temporalInterval.subtractFrom(temporal)

    /**
     * Retrieves the list of temporal units supported by this temporal interval.
     *
     * @return a list of TemporalUnit representing the units associated with this interval
     * @since 3.4.0
     */
    override fun getUnits(): List<TemporalUnit> = temporalInterval.getUnits()

    /**
     * Converts the current temporal interval into a representation based on its duration.
     *
     * This method delegates the conversion process to the `toTemporalIntervalOfDuration` method
     * of the `temporalInterval` property. It encapsulates the logic for deriving a repeated
     * temporal interval using the duration of the current interval.
     *
     * @return a repeated temporal interval derived from the duration of the current temporal interval
     * @since 3.4.0
     */
    override fun toTemporalIntervalOfDuration() = temporalInterval.toTemporalIntervalOfDuration()
    /**
     * Converts the current temporal interval into a representation that uses temporal durations.
     *
     * This method delegates the transformation to the `toTemporalIntervalOfTemporalDuration`
     * function of the `temporalInterval` property.
     *
     * @return A new instance representing the temporal interval in terms of temporal durations.
     * @since 3.4.0
     */
    override fun toTemporalIntervalOfTemporalDuration() = temporalInterval.toTemporalIntervalOfTemporalDuration()
    /**
     * Converts this instance's temporal interval representation into one that expresses
     * the interval as a combination of duration and start/end temporal values.
     *
     * @return A representation of the temporal interval in terms of its duration and start/end temporals.
     * @since 3.4.0
     */
    override fun toTemporalIntervalOfDurationTemporal() = temporalInterval.toTemporalIntervalOfDurationTemporal()
    /**
     * Converts the current temporal interval representation into a `TemporalInterval`
     * object defined by its temporal boundaries.
     *
     * This method operates on the underlying `temporalInterval` property and provides
     * an equivalent `TemporalInterval` representation derived from the current instance's
     * temporal boundaries.
     *
     * @return A `TemporalInterval` instance representing this object's temporal interval.
     * @since 3.4.0
     */
    override fun toTemporalIntervalOfTemporals() = temporalInterval.toTemporalIntervalOfTemporals()

    /**
     * Returns a new instance of RTemporalInterval with the specified start value while retaining the existing end value.
     *
     * @param start the new start value for the temporal interval.
     * @since 3.4.0
     */
    override fun withStart(start: Temporal) = RTemporalInterval(start, end)
    /**
     * Creates a new `RTemporalInterval` with the specified end temporal value, maintaining the existing start value.
     *
     * @param end The temporal value to set as the end of the interval.
     * @return A new `RTemporalInterval` instance with the specified end value.
     * @since 3.4.0
     */
    override fun withEnd(end: Temporal) = RTemporalInterval(start, end)
    /**
     * Creates a new instance of RTemporalInterval with the same start time and an updated end time
     * calculated by adding the specified duration to the current start time.
     *
     * @param duration The duration to add to the start time to compute the new end time.
     * @return A new RTemporalInterval instance with the updated duration.
     * @since 3.4.0
     */
    override fun withDuration(duration: Duration) = RTemporalInterval(start, start.plus(duration) as T1)

    /**
     * Compares this object with the specified object for equality.
     *
     * @param other The object to compare with this instance for equality.
     * @return `true` if the specified object is equal to this instance, otherwise `false`.
     * @since 3.4.0
     */
    override fun equals(other: Any?) = temporalInterval == other
    /**
     * Computes the hash code for the object based on its temporal interval property.
     *
     * @return The hash code value derived from the `temporalInterval` property.
     * @since 3.4.0
     */
    override fun hashCode() = temporalInterval.hashCode()
}

/**
 * Creates a restricted temporal interval based on a start time and a duration.
 *
 * @param T1 The type of the temporal object that defines the start time,
 *           which must implement the Temporal interface.
 * @param start The starting point of the temporal interval.
 * @param duration The duration of the temporal interval.
 * @return A restricted temporal interval derived from the start time and duration.
 * @since 3.4.0
 */
fun <T1: Temporal> RTemporalInterval(start: T1, duration: Duration) =
    start.restrictedIntervalTo(duration)
/**
 * Constructs a restricted temporal interval based on the provided duration and end temporal point.
 *
 * @param duration the duration representing the length of the interval.
 * @param end the temporal point marking the end of the interval.
 * @return a restricted temporal interval capped by the given duration and end point.
 * @since 3.4.0
 */
fun <T2: Temporal> RTemporalInterval(duration: Duration, end: T2) =
    duration.restrictedIntervalTo(end)