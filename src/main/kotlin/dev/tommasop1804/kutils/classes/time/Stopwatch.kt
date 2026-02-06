package dev.tommasop1804.kutils.classes.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.Instant
import dev.tommasop1804.kutils.isNotNull
import dev.tommasop1804.kutils.isNull
import dev.tommasop1804.kutils.then
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.node.ObjectNode
import java.io.Serial
import java.io.Serializable
import java.time.Instant
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalUnit
import kotlin.reflect.KProperty

/**
 * A class that represents a stopwatch for measuring elapsed time. It provides features
 * to start, stop, reset, and calculate the duration of elapsed time, and supports
 * comparisons based on durations.
 *
 * This class implements the `Serializable`, `Comparable<Duration>`, and `TemporalAmount`
 * interfaces to enable serialization and functionality for temporal operations.
 *
 * @property startTime The start time of the stopwatch in milliseconds. Defaults to null.
 * @propertyendTime The end time of the stopwatch in milliseconds. Defaults to null.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Stopwatch.Companion.Serializer::class)
@JsonDeserialize(using = Stopwatch.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Stopwatch.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Stopwatch.Companion.OldDeserializer::class)
@Suppress("unused")
class Stopwatch (var startTime: Long? = null, var endTime: Long? = null) : Serializable, Comparable<Duration>, TemporalAmount {
    var start: Instant?
        get() = startTime?.then { Instant(this) }
        set(value) {
            startTime = value?.toEpochMilli()
        }

    var end: Instant?
        get() = endTime?.then { Instant(this) }
        set(value) {
            endTime = value?.toEpochMilli()
        }

    /**
     * Time in pause.
     * @since 1.0.0
     */
    private var pause: Duration = Duration()
    
    /**
     * Start of the pause.
     * @since 1.0.0
     */
    private var pauseStart: Long? = null

    /**
     * Indicates whether the stopwatch is currently running.
     *
     * This property is `true` if the stopwatch has been started and not yet stopped,
     * and `false` otherwise. It is set internally and cannot be modified directly.
     *
     * @since 1.0.0
     */
    var isRunning: Boolean = false
        private set

    /**
     * Calculates the elapsed duration of the stopwatch. If the stopwatch is running,
     * it computes the duration from the start time up to the current system time.
     * If the stopwatch is stopped, it computes the duration between the start and
     * stop times.
     *
     * @return the duration representing the elapsed time while the stopwatch was active.
     * @since 1.0.0
     */
    val elapsed: Duration
        get() {
            if (startTime.isNull()) return Duration()
            if (isRunning) return Duration(nanos = (System.currentTimeMillis() - startTime!!) * NANOS_PER_MILLI)
            if (endTime.isNull()) return Duration()
            return Duration(nanos = (endTime!! - startTime!!) * NANOS_PER_MILLI) - pause
        }

    /**
     * Calculates the elapsed time in milliseconds since the stopwatch started.
     * If the stopwatch is currently running, calculates the elapsed time from the start time to now.
     * If the stopwatch is not running, calculates the elapsed time between the start time and the end time.
     *
     * @return The elapsed time in milliseconds.
     * @since 1.0.0
     */
    val elapsedMillis: Long
        get() {
            if (startTime.isNull()) return 0
            if (isRunning) return (System.currentTimeMillis() - startTime!!) - pause.toMillis(start!!)
            if (endTime.isNull()) return 0
            return (endTime!! - startTime!!)
        }

    /**
     * Constructs a `Stopwatch` instance using the specified start time and duration.
     *
     * This constructor initializes the `Stopwatch` with the provided start time,
     * and computes the end time by adding the given duration to the start time.
     * The duration is converted to milliseconds for precision.
     *
     * @param startTime The start time of the stopwatch, specified as a `Long` representing milliseconds.
     * @param duration The duration to calculate the end time, represented as a `Duration`.
     * @since 1.0.0
     */
    constructor(startTime: Long, duration: Duration): this(startTime, startTime + duration.toMillis(Instant(startTime)))

    private constructor(startTime: Long?, endTime: Long?, isRunning: Boolean, pause: Duration, pauseStart: Long?) : this(startTime, endTime) {
        this.isRunning = isRunning
        this.pause = pause
        this.pauseStart = pauseStart
    }

    companion object {
        /**
         * A unique identifier used during the deserialization process to ensure
         * that a loaded class corresponds exactly to a serialized object.
         * This field is commonly used in classes implementing the `Serializable` interface.
         *
         * @see Serializable
         *
         * @since 1.0.0
         */
        @Serial
        private const val serialVersionUID: Long = 1L

        /**
         * The constant NANOS_PER_MILLI represents the number of nanoseconds
         * in one millisecond (1,000,000 nanoseconds).
         *
         * This value is primarily used in calculations to convert between
         * nanoseconds and milliseconds within the Stopwatch class.
         *
         * @since 1.0.0
         */
        private const val NANOS_PER_MILLI: Long = 1000000

        class Serializer : ValueSerializer<Stopwatch>() {
            override fun serialize(
                value: Stopwatch,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                gen.writePOJOProperty("startTime", value.startTime)
                gen.writePOJOProperty("endtime", value.endTime)
                gen.writeBooleanProperty("isRunning", value.isRunning)
                gen.writePOJOProperty("pause", value.pause)
                gen.writePOJOProperty("pauseStart", value.pauseStart)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Stopwatch>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Stopwatch {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                return Stopwatch(
                    node.get("startTime").traverse(p.objectReadContext()).readValueAs(Long::class.java),
                    node.get("endtime").traverse(p.objectReadContext()).readValueAs(Long::class.java),
                    node.get("isRunning").traverse(p.objectReadContext()).readValueAs(Boolean::class.java),
                    node.get("pause").traverse(p.objectReadContext()).readValueAs(Duration::class.java),
                    node.get("pauseStart").traverse(p.objectReadContext()).readValueAs(Long::class.java),
                )
            }
        }

        class OldSerializer : JsonSerializer<Stopwatch>() {
            override fun serialize(value: Stopwatch, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeObjectField("startTime", value.startTime)
                gen.writeObjectField("endtime", value.endTime)
                gen.writeBooleanField("isRunning", value.isRunning)
                gen.writeObjectField("pause", value.pause)
                gen.writeObjectField("pauseStart", value.pauseStart)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Stopwatch>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Stopwatch {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                return Stopwatch(
                    node.get("startTime").traverse(p.codec).readValueAs(Long::class.java),
                    node.get("endtime").traverse(p.codec).readValueAs(Long::class.java),
                    node.get("isRunning").traverse(p.codec).readValueAs(Boolean::class.java),
                    node.get("pause").traverse(p.codec).readValueAs(Duration::class.java),
                    node.get("pauseStart").traverse(p.codec).readValueAs(Long::class.java),
                )
            }
        }
    }

    /**
     * Converts the state of the `Stopwatch` instance to a `Map` representation.
     *
     * The generated map includes the following key-value pairs:
     * - `startTime`: The start time of the stopwatch.
     * - `endTime`: The end time of the stopwatch.
     * - `pause`: The pause duration of the stopwatch.
     * - `elapsedTime`: The elapsed duration as calculated by the `elapsed()` method.
     * - `isRunning`: A boolean indicating whether the stopwatch is currently running.
     *
     * This method provides a simplified way to inspect the current state of the stopwatch.
     *
     * @return A map containing the current state of the `Stopwatch`.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "startTime" to startTime,
        "endTime" to endTime,
        "pause" to pause,
        "elapsed" to elapsed
    )

    /**
     * Retrieves the value associated with the property name from the internal map representation of the object.
     *
     * - `startTime` - TYPE: [Long]
     * - `endTime` - TYPE: [Long]
     * - `pause` - TYPE: [Duration]
     * - `elapsed` - TYPE: [Duration]
     *
     * @param thisRef The reference to the object for which the value is being retrieved. This parameter may be null.
     * @param property The property whose value is to be retrieved. The name of this property is used as the key in the map.
     * @return The value associated with the property name, cast to the generic type R.
     * @throws NoSuchElementException if the property name is not found
     * @throws ClassCastException if the property is cast to an uncastable type
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>): R = _toMap().getValue(property.name) as R

    /**
     * Starts the stopwatch by recording the current time. If the stopwatch is already running,
     * an exception is thrown unless explicitly overridden with the `force` parameter.
     *
     * @param force specifies whether to forcibly restart the stopwatch if it is already running.
     *              Set to `true` to restart; otherwise, an exception will be thrown if the stopwatch is running.
     * @throws UnsupportedOperationException if the stopwatch is already running and `force` is false.
     * @since 1.0.0
     */
    fun start(force: Boolean = false) {
        if (isRunning && !force) throw UnsupportedOperationException("The stopwatch is already running")
        startTime = System.currentTimeMillis()
        isRunning = true
    }
    
    /**
     * Pauses the stopwatch, recording the current time as the pause start time and setting the running status to false.
     * If the stopwatch is not running and `force` is false, an exception is thrown.
     *
     * @param force Whether to forcibly pause the stopwatch even if it is not running. Defaults to false.
     * @throws UnsupportedOperationException If the stopwatch is not running and `force` is false.
     * @since 1.0.0
     */
    fun pause(force: Boolean = false) {
        if (!isRunning && !force) throw UnsupportedOperationException("The stopwatch is not running")
        pauseStart = System.currentTimeMillis()
        isRunning = false
    }
    
    /**
     * Resumes the stopwatch from a paused state, allowing it to continue measuring time.
     * If the stopwatch is already running, an exception will be thrown unless the `force` parameter is set to `true`.
     *
     * @param force specifies whether to forcibly resume the stopwatch if it is already running.
     *              Set to `true` to resume forcibly; otherwise, an exception will be thrown if the stopwatch is running.
     * @throws UnsupportedOperationException if the stopwatch is already running and `force` is false.
     * @since 1.0.0
     */
    fun resume(force: Boolean = false) {
        if (isRunning && !force) throw UnsupportedOperationException("The stopwatch is already running")
        pause += Duration(nanos = (System.currentTimeMillis() - pauseStart!!) * NANOS_PER_MILLI)
        pauseStart = null
        isRunning = true
    }

    /**
     * Stops the stopwatch, recording the end time and calculating the elapsed duration.
     * Optionally forces the stop even if the stopwatch is not running.
     *
     * @param force If true, forces the stopwatch to stop even if it is not currently running. Defaults to false.
     * @return The duration that has elapsed between the start and stop of the stopwatch.
     * @throws UnsupportedOperationException If the stopwatch is not running and `force` is false.
     * 
     * @since 1.0.0
     */
    fun stop(force: Boolean = false): Duration {
        if (!isRunning && !force) throw UnsupportedOperationException("The stopwatch is not running")
        endTime = System.currentTimeMillis()
        isRunning = false
        if (pauseStart.isNotNull()) pause += Duration(nanos = (System.currentTimeMillis() - pauseStart!!) * NANOS_PER_MILLI)
        
        return elapsed
    }

    /**
     * Resets the internal state of the stopwatch by clearing the start and end times and stopping it.
     * After calling this method, the stopwatch will no longer be running.
     *
     * 
     * @since 1.0.0
     */
    fun reset() {
        startTime = null
        endTime = null
        isRunning = false
        pause = Duration()
    }

    /**
     * Compares the elapsed duration of the current stopwatch instance with another object.
     * Returns `true` if the other object is a `TemporalAmount` and represents the same duration
     * as returned by the `elapsed` method of this instance.
     *
     * 
     * @param other The object to compare with the elapsed duration of this instance.
     *              Typically expected to be an instance of `TemporalAmount`.
     * @return `true` if the other object is a `TemporalAmount` with a duration equal to the elapsed
     *         duration of this stopwatch instance; otherwise, `false`.
     * @since 1.0.0
     */
    fun equalsDuration(other: Any?): Boolean {
        if (this === other) return true
        if (other is TemporalAmount) return elapsed == Duration.from(other)
        return false
    }

    /**
     * Compares this `Stopwatch` instance to another object to determine equality.
     *
     * 
     * @param other The object to compare with this `Stopwatch` instance.
     * @return `true` if the specified object is equal to this `Stopwatch` instance, `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Stopwatch

        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false
        if (isRunning != other.isRunning) return false

        return true
    }

    /**
     * Computes a hash code for this Stopwatch instance based on its state, including
     * the `startTime`, `endTime`, and `isRunning` properties.
     *
     * @return the hash code value for this Stopwatch.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = startTime?.hashCode() ?: 0
        result = 31 * result + (endTime?.hashCode() ?: 0)
        result = 31 * result + isRunning.hashCode()
        return result
    }

    /**
     * Returns a string representation of the `Stopwatch` instance, including its start time,
     * end time, and running status.
     *
     * 
     * @return A string containing the values of `startTime`, `endTime`, and `isRunning`
     *         for this `Stopwatch` instance.
     * @since 1.0.0
     */
    override fun toString() = "Stopwatch(startTime=$startTime, endTime=$endTime, isRunning=$isRunning, elapsed=$elapsed)"

    /**
     * Compares the duration measured by the stopwatch with another given duration.
     *
     * 
     * @param other The duration to compare against.
     * @return A negative integer, zero, or a positive integer as this duration is less than,
     * equal to, or greater than the specified duration.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Duration) = elapsed.compareTo(other)

    /**
     * Compares this `Stopwatch` instance with another `Stopwatch` instance based on their elapsed durations.
     *
     * 
     * @param other The `Stopwatch` instance to compare this instance with.
     * @return A negative integer, zero, or a positive integer if the elapsed duration
     *         of this `Stopwatch` is less than, equal to, or greater than the elapsed duration
     *         of the specified `other` `Stopwatch`.
     * @since 1.0.0
     */
    operator fun compareTo(other: Stopwatch) = elapsed.compareTo(other.elapsed)

    /**
     * Retrieves the value of the specified temporal unit from the stopwatch's elapsed duration.
     *
     * 
     * @param unit The temporal unit whose value is to be retrieved.
     * @return The value of the specified temporal unit from the elapsed duration.
     * @since 1.0.0
     */
    override fun get(unit: TemporalUnit) = elapsed.get(unit)

    /**
     * Retrieves the list of supported temporal units for the duration elapsed by the stopwatch.
     *
     * This method delegates to the `getUnits` method of the `Duration` obtained via the `elapsed` method,
     * returning a list of `ChronoUnit` constants representing the time units supported by the retrieved duration.
     *
     * 
     * @return A list of `TemporalUnit` constants in descending order of granularity, as supported by the elapsed duration.
     * @since 1.0.0
     */
    override fun getUnits(): List<TemporalUnit> = elapsed.getUnits()

    /**
     * Adds the elapsed duration of the stopwatch to the specified temporal object.
     * The addition is based on the elapsed time computed by the `elapsed()` method of the stopwatch.
     *
     * 
     * @param temporal The temporal object to which the elapsed duration should be added.
     * @return A new temporal object with the elapsed duration added.
     * @since 1.0.0
     */
    override infix fun addTo(temporal: Temporal) = elapsed.addTo(temporal)

    /**
     * Subtracts the elapsed duration from the specified temporal object.
     *
     * 
     * @param temporal The temporal object from which the elapsed duration will be subtracted.
     * @return The resulting temporal object after subtracting the elapsed duration.
     * @since 1.0.0
     */
    override infix fun subtractFrom(temporal: Temporal) = elapsed.subtractFrom(temporal)
}