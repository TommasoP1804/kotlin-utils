package dev.tommasop1804.kutils.classes.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.exceptions.TemporalException
import dev.tommasop1804.kutils.invoke
import dev.tommasop1804.kutils.isNull
import dev.tommasop1804.kutils.splitAndTrim
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
import java.time.temporal.Temporal
import java.time.temporal.TemporalUnit

/**
 * Represents a temporal interval that can repeat over time, defining a start, end, and optional repetition factor.
 *
 * This class allows manipulation and querying of temporal intervals, including repetition-aware operations
 * such as calculating durations, adjusting endpoints, and determining the inclusion of specific temporal points.
 *
 * Key features include:
 * - Support for repeated intervals, with adjustable repetition settings.
 * - Flexible manipulation and querying of temporal values, such as duration, start, and end points.
 * - Integration with Temporal and TemporalUnit for fine-grained time-based operations.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = RepeatedTemporalInterval.Companion.Serialize::class)
@JsonDeserialize(using = RepeatedTemporalInterval.Companion.Deserialize::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = RepeatedTemporalInterval.Companion.OldSerialize::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = RepeatedTemporalInterval.Companion.OldDeserialize::class)
@Suppress("unused", "kutils_getorthrow_as_invoke")
interface RepeatedTemporalInterval : TemporalInterval, Serializable {
    /**
     * Specifies the number of repetitions for a given temporal interval.
     *
     * This variable determines how many times the associated interval
     * is repeated in operations or computations involving repeatable durations.
     *
     * - -1 means that the interval is repeated indefinitely.
     * - 0 means that the interval is not repeated (executed 0 times).
     * - 1 means that the interval is repeated once (standard).
     * - ...
     *
     * @since 1.0.0
     */
    val repetition: Int

    /**
     * Represents the starting point of a temporal interval.
     * This variable defines the beginning boundary of the interval and is used in combination
     * with the `end` property to determine the duration and range of the interval.
     *
     * It can be updated using the `withStart` method or accessed for various operations
     * such as interval inclusion checks, duration calculation, or interval transformation.
     *
     * @since 1.0.0
     */
    override val start: Temporal
    /**
     * Represents the starting point of the temporal interval, considering repetitions.
     * This value integrates the interval's start and repetition parameters to determine an adjusted
     * temporal starting position based on the specific properties or behavior of the interval.
     *
     * Typically used to calculate or retrieve the effective starting point of the interval
     * when working with temporal operations that involve repeated intervals.
     *
     * @since 1.0.0
     */
    val startWithRepetition: Temporal
    /**
     * Represents the ending temporal value of a `TemporalInterval`.
     *
     * This property defines the upper boundary of the interval and is used in conjunction
     * with the `start` property to form the range of the interval. The value of `end`
     * may be used to calculate the duration of the interval or determine its relationship
     * to other temporal objects.
     *
     * @since 1.0.0
     */
    override val end: Temporal
    /**
     * Represents the ending point of a temporal interval, optionally accounting
     * for repetitions. This value takes into consideration any repeated intervals
     * defined within the `RepeatedTemporalInterval` and allows for determining
     * the final point of the entire interval sequence.
     *
     * Typically used alongside other properties and methods in the class to calculate
     * or manipulate time-based intervals affected by repetition logic.
     *
     * @since 1.0.0
     */
    val endWithRepetition: Temporal
    /**
     * Represents the duration of the repeated temporal interval.
     *
     * The duration is derived from the interval's start and end points, optionally considering repetitions.
     * This value provides a consistent and straightforward way to access the total duration of the interval.
     *
     * @since 1.0.0
     */
    override val duration: Duration
    /**
     * Represents the total duration of a repeated temporal interval, factoring in the effect of repetitions.
     *
     * The calculated duration includes the cumulative effect introduced by the repetition
     * of the temporal interval, if applicable.
     *
     * @since 1.0.0
     */
    val durationWithRepetition: Duration

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
         * Creates a new instance of `RepeatedTemporalInterval` using the specified starting temporal value, duration,
         * and an optional repetition count.
         *
         * @param start The starting temporal value for the interval.
         * @param duration The duration of the interval.
         * @param repetition The number of repetitions for the interval. Defaults to 0, meaning no repetitions.
         * @return A new instance of `RepeatedTemporalInterval` configured with the provided parameters.
         * @since 1.0.0
         */
        internal fun of(start: Temporal, duration: Duration, repetition: Int = 1): RepeatedTemporalInterval =
            TemporalIntervalOfTemporalDuration(start, duration, repetition)

        /**
         * Creates a new instance of `RepeatedTemporalInterval` based on the specified duration, end temporal,
         * and optional repetition count.
         *
         * @param duration The duration of the temporal interval. Must not be null.
         * @param end The end temporal object that defines the endpoint of the interval. Must not be null.
         * @param repetition The number of repetitions for the interval. Defaults to 0 if not specified.
         *                   Must be a non-negative integer.
         * @return A new `RepeatedTemporalInterval` instance initialized with the provided duration, end temporal,
         *         and repetition count.
         * @since 1.0.0
         */
        internal fun of(duration: Duration, end: Temporal, repetition: Int = 1): RepeatedTemporalInterval =
            TemporalIntervalOfDurationTemporal(duration, end, repetition)

        /**
         * Creates a new instance of `RepeatedTemporalInterval` with the specified duration and repetition count.
         * The duration defines the time span of the interval, and the repetition count specifies how many times
         * the interval is repeated.
         *
         * @param duration The duration of the temporal interval. Must not be null.
         * @param repetition The number of repetitions for the interval. Defaults to 0 if not provided.
         *                   A repetition count of 0 means the interval is not repeated.
         * @return A new instance of `RepeatedTemporalInterval` configured with the provided duration and repetition count.
         * @since 1.0.0
         */
        internal fun of(duration: Duration, repetition: Int = 1): RepeatedTemporalInterval =
            TemporalIntervalOfDuration(duration, repetition)

        /**
         * Creates a new instance of `RepeatedTemporalInterval` based on the provided `TemporalInterval` and repetition count.
         * The method processes specific types of `TemporalInterval` and applies the given repetition count to generate the new instance.
         *
         * @param interval The temporal interval used to create the repeated temporal interval instance.
         *                 Must be one of the following types: TemporalIntervalOfDuration, TemporalIntervalOfDurationTemporal,
         *                 or TemporalIntervalOfTemporalDuration.
         * @param repetition The number of repetitions to apply to the interval. Defaults to `0` if not explicitly provided.
         *                   Must be a non-negative integer.
         * @return A new instance of `RepeatedTemporalInterval` using the provided interval and repetition.
         * @throws UnsupportedOperationException If the provided interval type is not supported.
         * @since 1.0.0
         */
        fun from(interval: TemporalInterval, repetition: Int = 1) = runCatching { when(interval) {
            is TemporalIntervalOfDuration, is TemporalIntervalOfDurationTemporal, is TemporalIntervalOfTemporalDuration -> interval.withRepetition(repetition)
            else -> throw TemporalException("The interval is not supported")
        } }

        /**
         * Creates a temporal interval starting from this temporal and lasting for the given duration.
         *
         * This function combines the current temporal instance with the provided duration
         * to create a temporal interval representation.
         * @param duration the duration of the interval to be created
         * @return a [RepeatedTemporalInterval] instance representing the interval starting from this temporal and lasting for the specified duration.
         * @since 1.0.0
         */
        infix fun Temporal.intervalTo(duration: Duration) = of(this, duration)
        /**
         * Creates a temporal interval between the current `Duration` instance and the specified `Temporal` object.
         *
         * The method generates an interval starting at the current `Duration` and ending at the provided `end` temporal.
         * This can be useful for defining ranges or periods in terms of a start `Duration` and an `end` `Temporal`.
         *
         * @param end the temporal object marking the endpoint of the interval
         * @return a [`RepeatedTemporalInterval`] instance representing the interval between the current `Duration` and the specified `Temporal` object.
         * @since 1.0.0
         */
        infix fun Duration.intervalTo(end: Temporal) = of(this, end)

        /**
         * Converts a `Duration` object into a `TemporalInterval` representation.
         *
         * This method creates a temporal interval using the provided duration
         * and encapsulates it as a `TemporalInterval`.
         *
         * @return a new [RepeatedTemporalInterval] based on this duration
         * @since 1.0.0
         */
        fun Duration.toInterval() = of(this)

        /**
         * Repeats the given temporal interval a specified number of times.
         * This allows the interval to represent a repeated sequence of occurrences.
         *
         * @param repetition The number of times the temporal interval should be repeated.
         * - -1 means that the interval is repeated indefinitely.
         * - 0 means that the interval is not repeated (executed 0 times).
         * - 1 means that the interval is repeated once (standard).
         * - ...
         * @since 1.0.0
         */
        infix fun RepeatedTemporalInterval.forTimes(repetition: Int) = withRepetition(repetition)

        /**
         * Creates a repeated temporal interval by specifying the number of repetitions for a duration.
         * This operation infixes the number of repetitions to the current duration, resulting
         * in a repeated interval.
         *
         * - -1 means that the interval is repeated indefinitely.
         * - 0 means that the interval is not repeated (executed 0 times).
         * - 1 means that the interval is repeated once (standard).
         * - ...
         *
         * @param repetition the number of times the duration should be repeated; must be non-negative.
         * @since 1.0.0
         */
        infix fun Duration.forTimes(repetition: Int) = toInterval().withRepetition(repetition)

        /**
         * Returns a new `RepeatedTemporalInterval` instance based on the current temporal interval
         * and an incremented repetition count.
         *
         * This infix function generates a repeated temporal interval by increasing the repetition count
         * by one from the value provided.
         *
         * @param repetition The current repetition count of the temporal interval.
         * - -1 means that the interval is not repeated (executed 0 times).
         * - 0 means that the interval is repeated once (standard).
         * - 1 means that the interval is repeated twice.
         * - ...
         * @since 1.0.0
         */
        infix fun RepeatedTemporalInterval.forOtherTimes(repetition: Int) = withRepetition(repetition + 1)

        /**
         * Parses the given string representation of a repeated temporal interval and returns a [Result] containing
         * a [RepeatedTemporalInterval] instance or an exception if the input is invalid.
         *
         * The method supports parsing repeated and non-repeated temporal intervals with specific formatting rules:
         * - -1 means that the interval is repeated indefinitely.
         * - 0 means that the interval is not repeated (executed 0 times).
         * - 1 means that the interval is repeated once (standard).
         * - ...
         *
         * @param s the string representation of the repeated temporal interval to parse
         * @return a [Result] containing the parsed [RepeatedTemporalInterval] object or an exception if the input is invalid
         * @since 1.0.0
         */
        fun parse(s: String): Result<RepeatedTemporalInterval> = runCatching {
            val repeated = "R" in s
            val parts = s.splitAndTrim("/")
            validateInputFormat(!(repeated && (parts.size !in 2..3))) { "Invalid repeated time interval: $s" }
            validateInputFormat(!(!repeated && (parts.isEmpty() || parts.size > 2))) { "Invalid repeated time interval: $s" }

            if (repeated && parts.size == 2) {
                of(
                    Duration.parse(parts[1]).getOrThrow(),
                    if (parts[0].length == 1) -1 else (-1)(parts[0]).toInt()
                )
            } else if (!repeated && parts.size == 1) {
                of(Duration.parse(parts[0]).getOrThrow())
            } else if ((if (repeated) parts[1] else parts[0]).startsWith("P")) {
                of(
                    Duration.parse(((if (repeated) parts[1] else parts[0]))).getOrThrow(),
                    TemporalInterval.parse(if (repeated) (parts[2] + "/" + parts[2]) else (parts[1] + "/" + parts[1])).getOrThrow().start,
                    if (!repeated) 0 else (if (parts[0].length == 1) -1 else (-1)(parts[0]).toInt())
                )
            } else if ((if (repeated) parts[2] else parts[1]).startsWith("P")) {
                of(
                    TemporalInterval.parse(if (repeated) (parts[1] + "/" + parts[1]) else (parts[0] + "/" + parts[0])).getOrThrow().end,
                    Duration.parse(((if (repeated) parts[2] else parts[1]))).getOrThrow(),
                    if (!repeated) 0 else if (parts[0].length == 1) -1 else (-1)(parts[0]).toInt()
                )
            } else throw MalformedInputException("Invalid time interval: $s")
        }

        class Serialize : ValueSerializer<RepeatedTemporalInterval>() {
            override fun serialize(
                value: RepeatedTemporalInterval,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeString(value.toString())
            }
        }

        class Deserialize : ValueDeserializer<RepeatedTemporalInterval>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext)= parse(p.string).getOrThrow()
        }

        class OldSerialize : JsonSerializer<RepeatedTemporalInterval>() {
            override fun serialize(value: RepeatedTemporalInterval, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.toString())
            }
        }

        class OldDeserialize : JsonDeserializer<RepeatedTemporalInterval>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext?) = parse(p.text).getOrThrow()
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<RepeatedTemporalInterval?, String?> {
            override fun convertToDatabaseColumn(attribute: RepeatedTemporalInterval?) = attribute?.toString()

            override fun convertToEntityAttribute(dbData: String?) = if (dbData.isNull()) null else parse(dbData).getOrThrow()
        }
    }

    /**
     * Adds the interval to the provided temporal object, optionally considering repetitions.
     *
     * If the `considerRepetition` parameter is set to true, the addition takes into account
     * the repetitions defined within the interval.
     *
     * @param temporal the temporal object to which the interval should be added
     * @param considerRepetition whether to consider the repetition count during the addition
     * @return a new temporal object resulting from the addition
     * @since 1.0.0
     */
    fun addTo(temporal: Temporal, considerRepetition: Boolean): Temporal
    /**
     * Adds this temporal interval to the specified Temporal object, altering it based on the interval's
     * internal state and behavior. The operation is typically dependent on the implementation details
     * and may involve modifications such as adding the specified repetition or duration to the temporal input.
     *
     * @param temporal the Temporal object to which this interval is to be added
     * @return a new Temporal instance with the interval applied
     * @since 1.0.0
     */
    override fun addTo(temporal: Temporal): Temporal

    /**
     * Subtracts the interval from the given temporal object, with an optional consideration of repetitions.
     *
     * @param temporal the temporal object to subtract the interval from
     * @param considerRepetition whether or not to account for repeated intervals during the subtraction process
     * @return a modified Temporal object with the interval subtracted
     * @since 1.0.0
     */
    fun subtractFrom(temporal: Temporal, considerRepetition: Boolean): Temporal
    /**
     * Subtracts the specified temporal object from this one, applying the logic
     * associated with the implementation of a repeated temporal interval.
     *
     * @param temporal the temporal object to subtract from this instance
     * @return a new Temporal instance that results from subtracting
     *         the specified temporal from this instance
     * @since 1.0.0
     */
    override fun subtractFrom(temporal: Temporal): Temporal

    /**
     * Determines if the given `temporal` is within the interval defined by the start and end points
     * of this temporal interval. The evaluation can optionally consider repetitions of the interval.
     *
     * @param temporal the temporal object to check against the interval; must not be null.
     * @param considerRepetition whether to consider repetitions of the interval when checking
     * the temporal's inclusion within the interval. Defaults to `true`.
     * @return `true` if the `temporal` is within the start and end points of the interval (including
     * boundary points), otherwise `false`.
     * @since 1.0.0
     */
    fun contains(temporal: Temporal, considerRepetition: Boolean = true): Boolean {
        val start = if (considerRepetition) startWithRepetition else start
        val end = if (considerRepetition) endWithRepetition else end

        if (temporal == start || temporal == end)
            return true

        val isAfterStart = java.time.Duration.between(start, temporal).isNegative
        val isBeforeEnd = java.time.Duration.between(temporal, end).isNegative
        return !isAfterStart && !isBeforeEnd
    }

    /**
     * Retrieves the amount of time for the given temporal unit, based on the current interval,
     * and optionally considers repetition.
     *
     * @param unit the temporal unit for which the amount of time is calculated
     * @param considerRepetition if true, repetitions are taken into account when calculating the time
     * @return the calculated time amount as a long
     * @since 1.0.0
     */
    operator fun get(unit: TemporalUnit, considerRepetition: Boolean): Long
    /**
     * Retrieves the value of the specified temporal unit for this repeated temporal interval.
     *
     * @param unit the {@link TemporalUnit} for which the value is to be retrieved.
     * @return the value of the specified temporal unit, considering any repetitions.
     * @since 1.0.0
     */
    override operator fun get(unit: TemporalUnit): Long

    /**
     * Returns a string representation of the RepeatedTemporalInterval instance.
     * By default, includes zero repetitions in the output unless specified otherwise.
     *
     * @param omit1Repetition if true, zero repetitions are omitted from the string representation;
     *                             otherwise, zero repetitions are included.
     * @return a string representation of the current instance, optionally excluding zero repetitions.
     * @since 1.0.0
     */
    fun toString(omit1Repetition: Boolean = false): String
    /**
     * Returns a string representation of the RepeatedTemporalInterval instance.
     *
     * @return A string describing the interval, its repetition, and temporal range.
     * @since 1.0.0
     */
    override fun toString(): String

    /**
     * Returns a new instance of `RepeatedTemporalInterval` with the specified number of repetitions.
     *
     * @param repetition the number of repetitions to be set in the new instance.
     * @return a new `RepeatedTemporalInterval` instance with the updated repetition value.
     * @since 1.0.0
     */
    fun withRepetition(repetition: Int): RepeatedTemporalInterval
}