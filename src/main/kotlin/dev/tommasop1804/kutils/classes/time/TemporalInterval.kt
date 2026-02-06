package dev.tommasop1804.kutils.classes.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
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
import kotlin.reflect.KProperty

/**
 * Represents a temporal interval, defined by a start and an end temporal value.
 * This interface provides methods for managing, manipulating, and interacting
 * with temporal intervals in a consistent and flexible manner.
 *
 * A `TemporalInterval` enables operations such as adding or subtracting intervals
 * from temporal objects, checking if a temporal is within the interval, and converting
 * the interval into a duration.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = TemporalInterval.Companion.Serialize::class)
@JsonDeserialize(using = TemporalInterval.Companion.Deserialize::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = TemporalInterval.Companion.OldSerialize::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = TemporalInterval.Companion.OldDeserialize::class)
@Suppress("unused", "kutils_getorthrow_as_invoke")
interface TemporalInterval : Serializable {
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
    val start: Temporal
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
    val end: Temporal
    /**
     * Represents the duration of the temporal interval.
     * The value is computed as the difference between the start and end points of the interval.
     * This property provides a concise way to access the length of the interval as a [Duration].
     *
     * @since 1.0.0
     */
    val duration: Duration

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
         * Creates a new `TemporalInterval` instance with the specified start and end temporal values.
         *
         * This method initializes a temporal interval by taking two `Temporal` objects, defining
         * the boundaries of the interval.
         *
         * @param start the starting `Temporal` instance of the interval
         * @param end the ending `Temporal` instance of the interval
         * @return a `TemporalInterval` representing the interval between the specified start and end
         * @since 1.0.0
         */
        internal fun of(start: Temporal, end: Temporal): TemporalInterval = TemporalIntervalOfTemporals(start, end)

        /**
         * Creates a `TemporalInterval` instance using the specified start temporal and duration.
         *
         * This method initializes a temporal interval starting at the given temporal instance
         * and extending for the specified duration.
         *
         * @param start the starting temporal instance of the interval
         * @param duration the duration of the temporal interval
         * @return a new `TemporalInterval` instance representing the interval defined by the start and duration
         * @since 1.0.0
         */
        internal fun of(start: Temporal, duration: Duration): TemporalInterval = RepeatedTemporalInterval.of(start, duration)

        /**
         * Creates a new `TemporalInterval` based on the specified duration and ending temporal value.
         *
         * This method constructs a temporal interval that spans the given duration
         * and ends at the specified temporal point.
         *
         * @param duration the duration of the interval
         * @param end the ending temporal value of the interval
         * @return a `TemporalInterval` instance representing the interval defined by the specified duration and end
         * @since 1.0.0
         */
        internal fun of(duration: Duration, end: Temporal): TemporalInterval = RepeatedTemporalInterval.of(duration, end)

        /**
         * Creates a new instance of `TemporalInterval` from the given duration.
         *
         * This factory method initializes a `TemporalInterval` represented by a repeated temporal interval
         * based on the provided duration. It serves as a utility for converting a `Duration` into its
         * corresponding temporal interval representation.
         *
         * @param duration the duration to be used for creating the temporal interval
         * @return a `TemporalInterval` instance derived from the specified duration
         * @since 1.0.0
         */
        internal fun of(duration: Duration): TemporalInterval = RepeatedTemporalInterval.of(duration)

        /**
         * Creates a temporal interval between the invoking temporal object and the specified end temporal.
         *
         * This function is an infix operator that produces a `TemporalInterval` instance using
         * the current `Temporal` as the starting point and the provided `end` as the ending point.
         *
         * @param end the ending temporal object that defines the boundary of the interval
         * @return a `TemporalInterval` instance representing the interval between the invoking temporal and the specified end temporal
         * @since 1.0.0
         */
        infix fun Temporal.intervalTo(end: Temporal) = of(this, end)

        /**
         * Parses a string representation of a temporal interval and returns a `Result` containing a `TemporalInterval`.
         *
         * The method analyzes the input string to create a valid `TemporalInterval` or `RepeatedTemporalInterval`.
         * It supports various formats, including durations and combinations of temporal values and durations.
         *
         * @param s the string representation of the temporal interval to be parsed
         * @return a `Result` containing the parsed `TemporalInterval` if successful, or an exception if parsing fails
         * @throws dev.tommasop1804.kutils.exceptions.MalformedInputException if the input string does not conform to the expected format
         * @since 1.0.0
         */
        infix fun parse(s: String): Result<TemporalInterval> = runCatching {
            if (s.startsWith("R")) RepeatedTemporalInterval.parse(s).getOrThrow()
            else {
                if (s.startsWith("R")) RepeatedTemporalInterval.parse(s).getOrThrow()
                else {
                    val parts = s.splitAndTrim("/")
                    validateInputFormat(!(parts.isEmpty() || parts.size > 2)) { "Invalid time interval: $s. Should be not empty or with at most two parts." }
                    if (parts.size == 1) of(Duration.parse(parts[0]).getOrThrow())
                    else if (parts[0].startsWith("P")) {
                        of(
                            Duration.parse(parts[0]).getOrThrow(),
                            parseTemporal(parts[1]) as Temporal
                        )
                    } else if (parts[1].startsWith("P")) {
                        of(
                            parseTemporal(parts[0]) as Temporal,
                            Duration.parse(parts[1]).getOrThrow()
                        )
                    } else {
                        of(
                            parseTemporal(parts[0]) as Temporal,
                            parseTemporal(parts[1]) as Temporal
                        )
                    }
                }
            }
        }

        /**
         * Parses a string representation of a temporal value and returns a specific type of Temporal object.
         * The method determines how to parse the input based on its content, such as containing date, time,
         * or offset indicators, and converts it into an appropriate temporal type.
         *
         * @param input the string representation of a temporal value to be parsed
         * @return the parsed temporal object of type T
         * @throws java.time.DateTimeException if the input string does not correspond to any temporal type
         * @throws ClassCastException if the casting to the specified generic type T fails
         * @since 1.0.0
         */
        @Suppress("UNCHECKED_CAST")
        private fun <T: Temporal> parseTemporal(input: String): T {
            if ("T" in input) {
                return if ("Z" in input || "+" in input || "-" in (-10)(input)) {
                    (input.parseToOffsetDateTime()).getOrThrow() as T
                } else {
                    (input.parseToLocalDateTime()).getOrThrow() as T
                }
            } else if ("Z" in input || "+" in input || "-" in input) {
                return try {
                    (input.parseToOffsetTime()).getOrThrow() as T
                } catch (_: Exception) {
                    (input.parseToLocalDate()).getOrThrow() as T
                }
            }
            return try {
                (input.parseToLocalTime()).getOrThrow() as T
            } catch (_: Exception) {
                (input.parseToLocalDate()).getOrThrow() as T
            }
        }

        class Serialize : ValueSerializer<TemporalInterval>() {
            override fun serialize(
                value: TemporalInterval,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeString(value.toString())
            }
        }

        class Deserialize : ValueDeserializer<TemporalInterval>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = parse(p.string).getOrThrow()
        }

        class OldSerialize : JsonSerializer<TemporalInterval>() {
            override fun serialize(value: TemporalInterval, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.toString())
            }
        }

        class OldDeserialize : JsonDeserializer<TemporalInterval>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext?) = parse(p.text).getOrThrow()
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<TemporalInterval?, String?> {
            override fun convertToDatabaseColumn(attribute: TemporalInterval?) = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?) = if (dbData.isNull()) null else parse(dbData).getOrThrow()
        }
    }

    /**
     * Converts the temporal interval into a map representation.
     *
     * This method creates a map containing the following key-value pairs:
     * - "start": The starting temporal value of this interval, retrieved via the `start()` method.
     * - "end": The ending temporal value of this interval, retrieved via the `end()` method.
     * - "duration": The duration of the temporal interval, computed using the `toDuration()` method.
     *
     * The resulting map provides a structured representation of the temporal interval's
     * key properties, which can be useful for serialization or debugging purposes.
     *
     * @return a map containing the `start`, `end`, and `duration` of the temporal interval.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "start" to start,
        "end" to end,
        "duration" to duration
    )

    /**
     * Retrieves the value associated with the specified property name from the map representation
     * of this temporal interval.
     *
     * This function accesses the temporal interval's internal map representation and extracts
     * the value corresponding to the requested property. The retrieved value is cast to the
     * expected return type.
     *
     * - `start`
     * - `end`
     * - `duration` - TYPE: [Duration]
     *
     * @param thisRef the reference to the object that owns the property
     * @param property the metadata for the property being accessed
     * @return the value associated with the given property name, cast to type `R`
     * @throws NoSuchElementException if the property name is not found in the map
     * @throws ClassCastException if the property is cast to an uncastable type
     * @throws UnsupportedOperationException in case of [RepeatedTemporalInterval] and repetition is undefined;
     * in case of [TemporalIntervalOfDuration] and searching for `start` or `end`
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>): R = _toMap().getValue(property.name) as R

    /**
     * Adds this temporal interval to the specified temporal object.
     *
     * The method applies the temporal interval represented by this instance
     * to the provided temporal object, producing a new temporal object that
     * reflects the addition.
     *
     * @param temporal the temporal object to which the interval will be added
     * @return a new temporal object resulting from the addition of this interval
     * @throws java.time.DateTimeException if the addition results in an invalid or unsupported date-time
     * @throws ArithmeticException if numeric overflow occurs during the operation
     * @since 1.0.0
     */
    infix fun addTo(temporal: Temporal): Temporal

    /**
     * Subtracts this temporal interval from the specified temporal and returns the resulting temporal.
     *
     * This method subtracts the duration of this interval from the supplied temporal object.
     * The behavior is equivalent to reversing the addition of this interval's duration to the supplied temporal.
     *
     * @param temporal the temporal object from which the interval should be subtracted
     * @return a new temporal object resulting from subtracting this interval's duration from the provided temporal
     * @since 1.0.0
     */
    infix fun subtractFrom(temporal: Temporal): Temporal

    /**
     * Checks if the given temporal instance is within the range defined by the `start` and `end` boundaries.
     * The method returns true if the provided `temporal` is inclusive between `start` and `end`.
     *
     * @param temporal the temporal instance to check against the range
     * @return `true` if the specified temporal is within the range, `false` otherwise
     * @since 1.0.0
     */
    operator fun contains(temporal: Temporal) = !Duration(start, temporal).isNegative && !Duration(temporal, end).isNegative

    /**
     * Compares this object with the specified object for equality.
     *
     * @param other The object to be compared with this instance.
     * @return `true` if the specified object is equal to this instance, `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean

    /**
     * Computes the hash code for this TemporalInterval object. The hash code is typically derived
     * from the start and end fields to ensure that equal objects produce the same hash code.
     *
     * @return an integer hash code representing this TemporalInterval object.
     * @since 1.0.0
     */
    override fun hashCode(): Int

    /**
     * Returns a string representation of the object.
     *
     * The returned string typically provides a concise but informative
     * representation that is easy to read and useful for debugging.
     *
     * @return A string representation of the object.
     * @since 1.0.0
     */
    override fun toString(): String

    /**
     * Retrieves the starting temporal value of the interval.
     *
     * This method allows deconstruction of the `TemporalInterval` object, specifically to access its
     * `start` property in a concise and structured manner.
     *
     * @return the starting temporal value of this interval.
     * @since 1.0.0
     */
    operator fun component1() = start

    /**
     * Returns the `end` component of the TemporalInterval.
     *
     * This function is typically used to destructure a TemporalInterval into its second component.
     *
     * @return the second component, `end`, of the TemporalInterval.
     * @since 1.0.0
     */
    operator fun component2() = end

    /**
     * Provides the third component of the instance, based on the destructuring declaration order.
     * This function returns the `duration` property of the object.
     *
     * @return the value of the `duration` property.
     * @since 1.0.0
     */
    operator fun component3() = duration

    /**
     * Retrieves the value of the specified temporal unit.
     *
     * @param unit the temporal unit for which the value is to be retrieved
     * @return the value of the temporal unit as a long
     * @since 1.0.0
     */
    operator fun get(unit: TemporalUnit): Long

    /**
     * Retrieves the list of temporal units supported by the `TemporalInterval`.
     *
     * @return a list of `TemporalUnit` representing the units supported by this interval
     * @since 1.0.0
     */
    fun getUnits(): List<TemporalUnit>

    /**
     * Converts the current temporal interval into a repeated temporal interval
     * representation based on its duration.
     *
     * @return a repeated temporal interval derived from the duration of the current temporal interval
     * @since 1.0.0
     */
    fun toTemporalIntervalOfDuration(): RepeatedTemporalInterval

    /**
     * Converts the current `TemporalInterval` instance into a `RepeatedTemporalInterval`
     * that represents the temporal interval in terms of a temporal duration.
     *
     * @return A new `RepeatedTemporalInterval` instance representing the interval as a temporal duration.
     * @since 1.0.0
     */
    fun toTemporalIntervalOfTemporalDuration(): RepeatedTemporalInterval

    /**
     * Converts this TemporalInterval into a RepeatedTemporalInterval where the interval is represented
     * as a combination of a duration and start/end temporals.
     *
     * @return a RepeatedTemporalInterval constructed using the duration and the start and end points
     * of this TemporalInterval.
     * @since 1.0.0
     */
    fun toTemporalIntervalOfDurationTemporal(): RepeatedTemporalInterval

    /**
     * Converts the current instance to a temporal interval defined by its temporal boundaries.
     *
     * @return A TemporalInterval object representing the temporal interval of the current instance.
     * @since 1.0.0
     */
    fun toTemporalIntervalOfTemporals(): TemporalInterval

    /**
     * Returns a new instance of TemporalInterval with the start value replaced by the specified Temporal.
     *
     * @param start the new start value to be set for the TemporalInterval
     * @return a new TemporalInterval instance with the updated start value
     * @since 1.0.0
     */
    infix fun withStart(start: Temporal): TemporalInterval

    /**
     * Creates a new TemporalInterval with the specified end temporal.
     *
     * @param end the temporal to set as the end of the interval
     * @return a new TemporalInterval instance with the updated end value
     * @since 1.0.0
     */
    infix fun withEnd(end: Temporal): TemporalInterval

    /**
     * Returns a new TemporalInterval instance with the given duration applied.
     *
     * @param duration the duration to set for the interval
     * @return a TemporalInterval instance with the modified duration
     * @since 1.0.0
     */
    infix fun withDuration(duration: Duration): TemporalInterval
}