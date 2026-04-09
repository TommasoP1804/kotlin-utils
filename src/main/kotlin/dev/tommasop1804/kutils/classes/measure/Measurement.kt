/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.measure

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.Beta
import dev.tommasop1804.kutils.exceptions.*
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.node.ObjectNode
import java.io.Serial
import java.io.Serializable
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.reflect.KProperty

/**
 * Represents a measurement with a numeric value and an associated unit.
 *
 * This class provides functionality for performing arithmetic operations,
 * unit conversions, and comparisons between measurements.
 *
 * For a restricted version, see [RMeasurement].
 *
 * @property value The numeric value of the measurement.
 * @property unit The unit associated with the measurement's value.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Measurement.Companion.Serializer::class)
@JsonDeserialize(using = Measurement.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Measurement.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Measurement.Companion.OldDeserializer::class)
open class Measurement(val value: Double, val unit: ScalarUnit) : Number(), Serializable, Comparable<Measurement> {
    /**
     * Retrieves the measurement unit as a string representation.
     *
     * This property provides access to the measurement unit associated with the current context
     * through the backing `unit.measure` property.
     *
     * 
     * @return A string representing the measurement unit.
     * @since 1.0.0
     */
    val measure: String
        get() = unit.measure

    /**
     * Constructor for creating a `Measurement` instance using a numeric value and a specified measure.
     * Automatically initializes the measurement using the associated `SIUnit` of the provided `Measure`.
     *
     * @param value The numeric value for the measurement.
     * @param measure The measure of the physical quantity, which specifies the corresponding `SIUnit`.
     * @since 1.0.0
     */
    constructor(value: Double, measure: Measure): this(value, measure.defaultUnit ?: throw ValidationFailedException("Measure must be associated with a SIUnit."))

    companion object {
        /**
         * A unique identifier for the `MeasureUnit` class, used during the serialization
         * and deserialization processes to verify that a serialized object is compatible
         * with the loaded class definition. This enables detection of version mismatches
         * between serialized data and the current class structure.
         *
         * Modifications to the class that are not backward-compatible should involve
         * updating this identifier to prevent runtime deserialization errors or to signal
         * incompatibility.
         *
         * @since 1.0.0
         */
        @Serial private const val serialVersionUID = 1L

        /**
         * Converts a numerical value to a measurement with the specified scalar unit.
         *
         * This function uses an infix notation to create a Measurement object,
         * associating a numeric value with a given unit of measurement.
         *
         * @param unit The scalar unit associated with the numerical value.
         * @since 1.0.0
         */
        infix fun <T : ScalarUnit> Number.ofUnitUnrestricted(unit: T) = RMeasurement(toDouble(), unit)

        /**
         * Parses an unrestricted measurement string into a Measurement object.
         *
         * The input string is expected to contain a numeric value followed by a unit symbol,
         * separated by one or more spaces.
         *
         * @param string the measurement string to be parsed, containing a number and a unit symbol.
         * @return a Result wrapping the parsed Measurement object or an exception if parsing fails.
         * @throws NoSuchEntryException if the unit symbol in the input string does not match any known units.
         * @since 3.1.1
         */
        @Suppress("UNCHECKED_CAST")
        infix fun parseUnrestricted(string: String) = runCatching {
            val [number, unit] = string / (Char.SPACE to 2)
            Measurement(
                number.toDouble(),
                MeasureUnit.knownUnitsScalar.find { it.knownSymbol && it.symbol == unit } ?: throw NoSuchEntryException("Unknown symbol unit: $unit")
            )
        }

        /**
         * Converts the current string to a measurement by parsing it using the `parseUnrestricted` function.
         *
         * The input string should represent a valid measurement format that is compatible with parsing.
         * The function provides a way to interpret such strings into a measurement object.
         *
         * @receiver The string to be converted into a measurement.
         * @return A parsed measurement based on the input string wrapped in a Result.
         *
         * @since 3.1.1
         */
        fun String.toMeasurement() = parseUnrestricted(this)

        class Serializer : ValueSerializer<Measurement>() {
            override fun serialize(
                value: Measurement,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                gen.writePOJOProperty("value", value.value)
                if (value.unit.knownSymbol)
                    gen.writeStringProperty("unit", value.unit.symbol ?: value.unit.unitName)
                else gen.writePOJOProperty("unit", value.unit)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Measurement>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Measurement {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                val unit: MeasureUnit = if (node.get("unit").isString)
                    MeasureUnit.knownUnits.find { it.symbol == node.get("unit").asString() } ?: MeasureUnit.knownUnits.find { it.unitName == node.get("unit").asString() } ?: throw NoSuchEntryException("Symbol of scalar unit not found: ${node.get("unit").asString()}")
                else MeasureUnit(
                    node.get("unit").get("name").asString(),
                    node.get("unit").get("measure").asString(),
                    if (node.get("unit").get("symbol").isNull) null else node.get("unit").get("symbol").asString(),
                    node.get("unit").get("isSIUnit").asBoolean(),
                    node.get("unit").get("isAcceptedBySI").asBoolean()
                )
                return Measurement(
                    node.get("value").asDouble(),
                    unit
                )
            }
        }

        class OldSerializer : JsonSerializer<Measurement>() {
            override fun serialize(value: Measurement, gen: JsonGenerator, serializers: SerializerProvider?) {
                gen.writeStartObject()
                gen.writeObjectField("value", value.value)
                if (value.unit.knownSymbol)
                    gen.writeStringField("unit", value.unit.symbol ?: value.unit.unitName)
                else gen.writeObjectField("unit", value.unit)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Measurement>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext?): Measurement {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                val unit: MeasureUnit = if (node.get("unit").isTextual)
                    MeasureUnit.knownUnits.find { it.symbol == node.get("unit").asText() } ?: MeasureUnit.knownUnits.find { it.unitName == node.get("unit").asText() } ?: throw NoSuchEntryException("Symbol of scalar unit not found: ${node.get("unit").asText()}")
                else MeasureUnit(
                    node.get("unit").get("name").asText(),
                    node.get("unit").get("measure").asText(),
                    if (node.get("unit").get("symbol").isNull) null else node.get("unit").get("symbol").asText(),
                    node.get("unit").get("isSIUnit").asBoolean(),
                    node.get("unit").get("isAcceptedBySI").asBoolean()
                )
                return Measurement(
                    node.get("value").asDouble(),
                    unit
                )
            }
        }
    }

    /**
     * Creates a copy of the current `Measurement` instance with optional modifications.
     *
     * @param value The new value to use for the copied `Measurement`. Defaults to the current value.
     * @param unit The new unit to use for the copied `Measurement`. Defaults to the current unit.
     * @return A new `Measurement` instance with the specified or default values.
     * @since 1.0.0
     */
    fun copy(value: Double = this.value, unit: ScalarUnit = this.unit) = Measurement(value, unit)
    
    /**
     * Returns the first component of the object, typically used for destructuring declarations.
     *
     * This operator function allows the object to be destructured, providing
     * access to its primary value or property in the first position.
     *
     * @return The value associated with the first component of the object.
     * @since 3.1.0
     */
    operator fun component1() = value
    /**
     * Provides the second component of a destructured object.
     *
     * This operator function enables the use of destructuring declarations to extract 
     * the second property or value from an object. It allows concise access to this 
     * information in destructuring contexts, improving code readability and usability.
     *
     * @return The second component of the object, represented by `unit`.
     * @since 3.1.0
     */
    operator fun component2() = unit

    /**
     * Converts the current measurement to a `Byte` value by truncating the internal `value`
     * to fit into the `Byte` range.
     *
     * 
     * @return The `Byte` representation of the measurement's value.
     * @since 1.0.0
     */
    override fun toByte(): Byte = value.toInt().toByte()

    /**
     * Converts the measurement to its double representation.
     *
     * 
     * @return The double value representing the measurement.
     * @since 1.0.0
     */
    override fun toDouble(): Double = value

    /**
     * Converts the measurement's value to a floating-point number.
     *
     * 
     * @return The value of the measurement as a Float.
     * @since 1.0.0
     */
    override fun toFloat(): Float = value.toFloat()

    /**
     * Converts the value of this measurement to an integer representation.
     *
     * 
     * @return The integer representation of the measurement's value.
     * @since 1.0.0
     */
    override fun toInt(): Int = value.toInt()

    /**
     * Converts the value of this Measurement to a `Long`.
     *
     * 
     * @return The value of this Measurement as a `Long`.
     * @since 1.0.0
     */
    override fun toLong(): Long = value.toLong()

    /**
     * Converts the measurement value to a Short representation.
     *
     * 
     * @return The Short representation of the measurement value.
     * @since 1.0.0
     */
    override fun toShort(): Short = value.toInt().toShort()

    /**
     * Checks if this object is equal to the specified object.
     *
     * 
     * @param other the object to be compared with this instance.
     * @return true if the objects are equal, false otherwise.
     * @since 1.0.0
     */
    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Measurement

        if (value != other.value) return false
        if (unit != other.unit) return false

        return true
    }

    /**
     * Computes a hash code for the `Measurement` instance based on its `value` and `unit` properties.
     *
     * 
     * @return The hash code value as an integer.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + unit.hashCode()
        return result
    }

    /**
     * Converts the current Measurement instance into its string representation.
     *
     * The output is composed of the numeric value and the symbol of the associated measurement unit,
     * concatenated with a space in between. This representation provides a human-readable format
     * of the Measurement instance combining both its magnitude and unit.
     *
     * 
     * @return A string representing the value and the unit of the measurement.
     * @since 1.0.0
     */
    override fun toString() = value.toString() + " " + (unit.measureUnit.symbol ?: unit.measureUnit.unitName)

    /**
     * Converts the measurement to its string representation. The string consists of the measurement value appended
     * with either the unit symbol or the unit name, based on the provided `symbol` parameter.
     *
     * 
     * @param symbol A boolean indicating whether to use the unit's symbol (if true) or the unit's full name (if false) in the string representation.
     * @return A string representation of the measurement.
     * @since 1.0.0
     */
    fun toString(symbol: Boolean) = value.toString() + " " + if (symbol) unit.measureUnit.symbol else unit.measureUnit.unitName

    /**
     * Converts the current measurement to a different scalar unit.
     *
     * Uses the static conversion method available in the `ScalarUnit` interface
     * to convert the current measurement's unit to the specified target unit.
     *
     * @param to the target `ScalarUnit` to which the current measurement unit should be converted.
     * @return A new `Measurement` instance representing the current measurement in the specified unit, wrapped in a [Result]
     * @throws dev.tommasop1804.kutils.exceptions.UnitConversionException if conversion failed.
     * @since 1.0.0
     */
    @Beta
    infix fun convertTo(to: ScalarUnit) = ScalarUnit.convert(this, to)

    /**
     * Adds the value of another `Measurement` to this `Measurement`.
     * The operation ensures that the measurements are of the same type before performing the addition.
     *
     * 
     * @param other The `Measurement` to be added to this `Measurement`.
     * @return A new `Measurement` representing the sum of the two measurements.
     * @throws UnsupportedOperationException If the measurements are of different types.
     * @since 1.0.0
     */
    @OptIn(Beta::class)
    operator fun plus(other: Measurement): Measurement {
        if (unit.measure != other.unit.measure) throw UnsupportedOperationException("Can't sum measurement of different type.")
        return Measurement(value + ScalarUnit.convert(other, unit)().value, unit)
    }

    /**
     * Adds a given numeric value to the current `Measurement` instance and returns a new `Measurement` object.
     * The `unit` of the result will be the same as the `unit` of the current instance.
     *
     * 
     * @param other The numeric value to add to the current measurement.
     * @since 1.0.0
     */
    operator fun plus(other: Double) = this + Measurement(other, unit)

    /**
     * Subtracts the given measurement from the current measurement. This operation is valid only
     * when both measurements are of the same type.
     *
     * 
     * @param other The measurement to subtract.
     * @return A new measurement resulting from the subtraction.
     * @throws UnsupportedOperationException If the measurements are of different types.
     * @since 1.0.0
     */
    @OptIn(Beta::class)
    operator fun minus(other: Measurement): Measurement {
        if (unit.measure != other.unit.measure) throw UnsupportedOperationException("Can't sum measurement of different type.")
        return Measurement(value - ScalarUnit.convert(other, unit)().value, unit)
    }

    /**
     * Subtracts a given numeric value from this `Measurement` instance, converting
     * the numeric value into a `Measurement` with the same unit as this instance.
     *
     * 
     * @param other The numeric value to be subtracted, which will be converted into
     * a `Measurement` using the same unit as the receiver.
     * @since 1.0.0
     */
    operator fun minus(other: Double) = this - Measurement(other, unit)

    /**
     * Multiplies the instance with a given factor and unit.
     *
     * @param factorAndUnit A pair where the first element is the factor (Double)
     * and the second element represents the unit (ScalarUnit) to multiply with.
     * @since 1.0.0
     */
    operator fun times(factorAndUnit: Pair<Double, ScalarUnit>) = times(factorAndUnit.first, factorAndUnit.second)

    /**
     * Multiplies the current measurement by a specified factor and assigns a new unit to the resultant measurement.
     *
     * @param factor The factor by which the current measurement's value is multiplied.
     * @param newUnit The new unit of the resultant measurement after multiplication.
     * @return A new Measurement instance with the updated value and unit.
     * @since 1.0.0
     */
    fun times(factor: Double, newUnit: ScalarUnit) = Measurement(value * factor, newUnit)

    /**
     * Divides the current object by the given factor and unit pair.
     *
     * @param factorAndUnit A pair where the first element is a factor of type Double, and the second element is a ScalarUnit.
     * @since 1.0.0
     */
    operator fun div(factorAndUnit: Pair<Double, ScalarUnit>) = div(factorAndUnit.first, factorAndUnit.second)

    /**
     * Divides the current measurement value by the specified factor and converts it to the specified unit.
     *
     * @param factor The divisor by which the measurement value is divided. Must not be zero.
     * @param newUnit The unit to which the measurement will be converted.
     * @return A new Measurement object with the divided value and the specified unit.
     * @throws ArithmeticException If the given factor is zero.
     * @since 1.0.0
     */
    fun div(factor: Double, newUnit: ScalarUnit): Measurement {
        if (factor == 0.0) throw ArithmeticException("Can't divide by zero.")
        return Measurement(value / factor, newUnit)
    }

    /**
     * Increments the value of the current `Measurement` object by 1 while maintaining the same unit.
     *
     * 
     * @return A new `Measurement` instance with its value incremented by 1.
     * @since 1.0.0
     */

    operator fun inc() = Measurement(value + 1, unit)
    /**
     * Decrements the current `Measurement` by 1 unit of its value.
     * This operator function creates and returns a new `Measurement` instance
     * with the decremented value, while preserving the unit of the original instance.
     *
     * 
     * @return A new `Measurement` instance with its value decreased by 1.
     * @since 1.0.0
     */
    operator fun dec() = Measurement(value - 1, unit)

    /**
     * Raises the current measurement value to the specified exponent and updates its unit.
     *
     * 
     * @param exponent the power to which the measurement's value will be raised.
     * @param newUnit the new scalar unit to be associated with the resulting measurement.
     * @return a new `Measurement` instance with the value raised to the given exponent and using the specified unit.
     * @since 1.0.0
     */
    fun pow(exponent: Double, newUnit: ScalarUnit) = Measurement(value.pow(exponent), newUnit)

    /**
     * Returns a new [Measurement] instance with the absolute value of the current measurement.
     *
     * This function creates a measurement where the numeric value is the absolute (non-negative) value
     * of the original instance while retaining the same unit.
     *
     * 
     * @return A [Measurement] with the absolute value of the current measurement.
     * @since 1.0.0
     */
    fun abs() = Measurement(value.absoluteValue, unit)

    /**
     * Negates the value of the current `Measurement` instance.
     * The resulting `Measurement` will have the same unit but with the negated value.
     *
     * 
     * @return A new `Measurement` instance with the negated value.
     * @since 1.0.0
     */
    operator fun unaryMinus() = Measurement(-value, unit)

    /**
     * Returns the absolute value of the number.
     * This operator function allows using the unary `+` operator to achieve this behavior.
     *
     * 
     * @return The absolute value of the receiver.
     * @since 1.0.0
     */
    operator fun unaryPlus() = abs()

    /**
     * Rounds the numerical value of the measurement to the nearest integer
     * while retaining the original unit.
     *
     * This function uses the standard mathematical rounding rules.
     *
     * 
     * @return A new `Measurement` object with the rounded value and the same unit as the original.
     * @since 1.0.0
     */
    fun round() = Measurement(kotlin.math.round(value), unit)

    /**
     * Rounds up the value of the current measurement to the nearest integer, preserving the original unit.
     *
     * 
     * @return A new measurement instance with its value rounded up to the nearest integer.
     * @since 1.0.0
     */
    fun ceil() = Measurement(kotlin.math.ceil(value), unit)

    /**
     * Calculates the largest integer value less than or equal to the current measurement's value
     * and returns a new `Measurement` instance with the floored value and the original unit.
     *
     * 
     * @return A new `Measurement` instance with the floored value.
     * @since 1.0.0
     */
    fun floor() = Measurement(kotlin.math.floor(value), unit)

    /**
     * Compares this measurement with the specified measurement for order.
     * An exception is thrown if the two measurements are of different units.
     *
     * 
     * @param other the specified measurement to compare to this measurement
     * @return a negative integer, zero, or a positive integer as this measurement is less than,
     *         equal to, or greater than the specified measurement
     * @throws IllegalOperationException if the measurement units are different
     * @since 1.0.0
     */
    @OptIn(Beta::class)
    override operator fun compareTo(other: Measurement): Int {
        if (other.unit != unit) throw IllegalOperationException("Can't compare measurement of different type.")
        return value.compareTo(ScalarUnit.convert(other, unit)().value)
    }

    /**
     * Calculates the average of the provided measurements. All measurements must have the same unit of measure.
     *
     * 
     * @param measurements A variable number of measurements to be averaged. All measurements must share the same unit of measure.
     * @return A new Measurement representing the average of the input measurements.
     * @throws IllegalOperationException If the units of the provided measurements are not the same.
     * @since 1.0.0
     */
    @OptIn(Beta::class)
    fun average(vararg measurements: Measurement): Measurement {
        var sum = 0.0
        for (measurement in measurements) {
            if (measurements[0].unit.measure == measurement.unit.measure)
                throw IllegalOperationException("Cannot average measurements of different units")
            sum += ScalarUnit.convert(measurement, measurements[0].unit)().value
        }
        return Measurement(sum / measurements.size, measurements[0].unit)
    }

    /**
     * Converts the object's properties into a map representation.
     *
     * This function is intended for internal use only and is annotated with @Suppress("functionName").
     *
     * @return A map containing the "value", "unit", and "measure" properties.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "value" to value,
        "unit" to unit.measureUnit,
        "measure" to unit.measureUnit.measure
    )

    /**
     * Retrieves the value associated with the given property name from the underlying map.
     * This operator function is typically used to delegate property values.
     *
     * - `value` - TYPE: [Double]
     * - `unit` - TYPE: [ScalarUnit]
     * - `measure` - TYPE: [String]
     *
     * @param thisRef the object for which the property is being accessed, can be null
     * @param property the metadata for the property being accessed
     * @return the value associated with the property name cast to the specified type
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}

/**
 * A generic class representing a measurement associated with a specific scalar unit.
 * The `RMeasurement` class extends the base `Measurement` class, providing a more
 * specialized representation of a value paired with its respective unit.
 *
 * `R` stands for `Restricted`.
 *
 * This class supports serialization and deserialization with custom serializers, making it
 * suitable for use in systems requiring standardized data exchange formats.
 *
 * @param T The scalar unit type associated with the measurement, extending `ScalarUnit`.
 * @param value The numerical value of the measurement.
 * @param unit The unit in which the measurement is expressed.
 * @see Measurement
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
@JsonSerialize(using = RMeasurement.Companion.Serializer::class)
@JsonDeserialize(using = RMeasurement.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = RMeasurement.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = RMeasurement.Companion.OldDeserializer::class)
class RMeasurement<T : ScalarUnit>(value: Double, unit: T) : Measurement(value, unit), Serializable {
    companion object {
        /**
         * Converts a numerical value to a measurement with the specified scalar unit.
         *
         * This function uses an infix notation to create a Measurement object,
         * associating a numeric value with a given unit of measurement.
         *
         * @param unit The scalar unit associated with the numerical value.
         * @since 1.0.0
         */
        infix fun <T : ScalarUnit> Number.ofUnit(unit: T) = RMeasurement(toDouble(), unit)
        /**
         * Converts this numeric value into a `DataSize` instance of the specified unit.
         *
         * @param unit the measurement unit to associate with this numeric value, representing a unit of data size.
         * @return a `DataSize` instance representing this numeric value in the specified data size unit.
         * @since 1.0.0
         */
        infix fun Number.ofUnit(unit: MeasureUnit.DataSizeUnit): DataSize = RMeasurement(toDouble(), unit)
        /**
         * Converts a number into a measurement of the specified length unit.
         *
         * @param unit The length unit to associate with the number.
         * @return A Length instance representing the number in the specified length unit.
         * @since 3.4.0
         */
        infix fun Number.ofUnit(unit: MeasureUnit.LengthUnit): Length = RMeasurement(toDouble(), unit)
        /**
         * Creates a Temperature measurement by associating a numeric value with a specific temperature unit.
         *
         * @param unit The temperature unit to associate with the numeric value.
         * @return A Temperature instance representing the measurement in the specified unit.
         * @since 3.4.0
         */
        infix fun Number.ofUnit(unit: MeasureUnit.TemperatureUnit): Temperature = RMeasurement(toDouble(), unit)
        /**
         * Converts the current number to an area measurement with the specified unit.
         *
         * @param unit The unit of measurement to associate with the number.
         * @return The resulting area as an instance of the Area class.
         * @since 3.4.0
         */
        infix fun Number.ofUnit(unit: MeasureUnit.AreaUnit): Area = RMeasurement(toDouble(), unit)
        /**
         * Converts a numeric value to a measurement of the specified volume unit.
         *
         * @param unit The volume unit to associate with the numeric value.
         * @return A Volume instance representing the numeric value in the specified unit.
         * @since 3.4.0
         */
        infix fun Number.ofUnit(unit: MeasureUnit.VolumeUnit): Volume = RMeasurement(toDouble(), unit)
        /**
         * Converts the number to a Speed value in the specified unit of measurement.
         *
         * @param unit The unit of speed to associate with the number.
         * @return A Speed instance representing the number in the specified unit.
         * @since 3.4.0
         */
        infix fun Number.ofUnit(unit: MeasureUnit.SpeedUnit): Speed = RMeasurement(toDouble(), unit)
        /**
         * Converts a numeric value into an instance of the Acceleration measurement with the specified unit.
         *
         * @param unit The measurement unit of type MeasureUnit.AccelerationUnit to associate with the value.
         * @return An instance of Acceleration representing the numeric value in the specified unit.
         * @since 3.4.0
         */
        infix fun Number.ofUnit(unit: MeasureUnit.AccelerationUnit): Acceleration = RMeasurement(toDouble(), unit)
        /**
         * Converts the current number to a `Density` value expressed in the specified density unit.
         *
         * @param unit The density unit to associate with the numeric value.
         * @return A `Density` instance representing the given numeric value and the specified density unit.
         * @since 3.4.0
         */
        infix fun Number.ofUnit(unit: MeasureUnit.DensityUnit): Density = RMeasurement(toDouble(), unit)
        /**
         * Creates a `PlaneAngle` measurement from this number and the specified plane angle unit.
         *
         * @param unit The plane angle unit to associate with this number.
         * @return A `PlaneAngle` representation of this number in the specified unit.
         * @since 3.4.0
         */
        infix fun Number.ofUnit(unit: MeasureUnit.PlaneAngleUnit): PlaneAngle = RMeasurement(toDouble(), unit)
        /**
         * Converts a numeric value to a Mass measurement with the specified unit.
         *
         * @param unit The mass unit to associate with the numeric value.
         * @return A Mass instance representing the value and its associated unit.
         * @since 3.4.0
         */
        infix fun Number.ofUnit(unit: MeasureUnit.MassUnit): Mass = RMeasurement(toDouble(), unit)


        /**
         * Parses the given string into a measurement object with a numeric value and a unit.
         *
         * @param string The input string in the format "number unit", where number is a numeric value
         * and unit is a valid unit symbol. The unit symbol must correspond to a known measurement unit.
         * @return A [Result] wrapping the measurement object with the parsed value and associated unit.
         *         If the parsing fails or an unknown unit is provided, an exception is thrown and captured in the [Result].
         * @since 3.1.1
         */
        @Suppress("UNCHECKED_CAST")
        infix fun <T : ScalarUnit> parse(string: String) = runCatching {
            val [number, unit] = string / (Char.SPACE to 2)
            RMeasurement(
                number.toDouble(),
                MeasureUnit.knownUnitsScalar.find { it.knownSymbol && it.symbol == unit } as T? ?: throw NoSuchEntryException("Unknown symbol unit: $unit")
            )
        }

        /**
         * Converts the current string to a measurement by parsing it using the `parse` function.
         *
         * The input string should represent a valid measurement format that is compatible with parsing.
         * The function provides a way to interpret such strings into a measurement object.
         *
         * @receiver The string to be converted into a measurement.
         * @return A parsed measurement based on the input string wrapped in a Result.
         *
         * @since 3.1.1
         */
        fun <T : ScalarUnit> String.toMeasurement() = parse<T>(this)

        class Serializer : ValueSerializer<RMeasurement<*>>() {
            override fun serialize(
                value: RMeasurement<*>,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                gen.writePOJOProperty("value", value.value)
                if (value.unit.knownSymbol)
                    gen.writeStringProperty("unit", value.unit.symbol ?: value.unit.unitName)
                else gen.writePOJOProperty("unit", value.unit)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<RMeasurement<*>>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): RMeasurement<*> {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                val unit: MeasureUnit = if (node.get("unit").isString)
                    MeasureUnit.knownUnits.find { it.symbol == node.get("unit").asString() } ?: MeasureUnit.knownUnits.find { it.unitName == node.get("unit").asString() } ?: throw NoSuchEntryException("Symbol of scalar unit not found: ${node.get("unit").asString()}")
                else MeasureUnit(
                    node.get("unit").get("name").asString(),
                    node.get("unit").get("measure").asString(),
                    if (node.get("unit").get("symbol").isNull) null else node.get("unit").get("symbol").asString(),
                    node.get("unit").get("isSIUnit").asBoolean(),
                    node.get("unit").get("isAcceptedBySI").asBoolean()
                )
                return RMeasurement(
                    node.get("value").asDouble(),
                    unit
                )
            }
        }

        class OldSerializer : JsonSerializer<RMeasurement<*>>() {
            override fun serialize(value: RMeasurement<*>, gen: JsonGenerator, serializers: SerializerProvider?) {
                gen.writeStartObject()
                gen.writeObjectField("value", value.value)
                if (value.unit.knownSymbol)
                    gen.writeStringField("unit", value.unit.symbol ?: value.unit.unitName)
                else gen.writeObjectField("unit", value.unit)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<RMeasurement<*>>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext?): RMeasurement<*> {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                val unit: MeasureUnit = if (node.get("unit").isTextual)
                    MeasureUnit.knownUnits.find { it.symbol == node.get("unit").asText() } ?: MeasureUnit.knownUnits.find { it.unitName == node.get("unit").asText() } ?: throw NoSuchEntryException("Symbol of scalar unit not found: ${node.get("unit").asText()}")
                else MeasureUnit(
                    node.get("unit").get("name").asText(),
                    node.get("unit").get("measure").asText(),
                    if (node.get("unit").get("symbol").isNull) null else node.get("unit").get("symbol").asText(),
                    node.get("unit").get("isSIUnit").asBoolean(),
                    node.get("unit").get("isAcceptedBySI").asBoolean()
                )
                return RMeasurement(
                    node.get("value").asDouble(),
                    unit
                )
            }
        }
    }
}

/**
 * Defines a type alias for `RMeasurement` specifically for measurements of data size.
 *
 * This alias simplifies the usage of the `RMeasurement` class when associated with the
 * `MeasureUnit.DataSizeUnit` unit, representing various units of data size such as bytes,
 * kilobytes, megabytes, etc.
 *
 * @see RMeasurement
 * @see MeasureUnit.DataSizeUnit
 * @since 3.0.0
 */
typealias DataSize = RMeasurement<MeasureUnit.DataSizeUnit>
/**
 * A type alias for `RMeasurement` specialized with length units.
 *
 * This type alias simplifies the usage of `RMeasurement` when working specifically
 * with physical measurements expressed in length units. The `MeasureUnit.LengthUnit`
 * type parameter ensures that only length-related units are used in conjunction with
 * this type alias.
 *
 * @see RMeasurement
 * @see MeasureUnit.LengthUnit
 * @since 3.0.0
 */
typealias Length = RMeasurement<MeasureUnit.LengthUnit>
/**
 * A type alias representing a measurement specifically for temperature-related units.
 *
 * This alias simplifies working with temperature measurements by explicitly associating
 * them with the `MeasureUnit.TemperatureUnit` scalar type. It is backed by the `RMeasurement`
 * class, enabling the use of the encapsulated functionalities like serialization support
 * and type safety for temperature measurements.
 *
 * @see RMeasurement
 * @see MeasureUnit.TemperatureUnit
 * @since 3.0.0
 */
typealias Temperature = RMeasurement<MeasureUnit.TemperatureUnit>
/**
 * A type alias for `RMeasurement` with `MeasureUnit.AreaUnit` as the scalar unit type.
 *
 * This alias represents a measurement of area, wherein the unit of measurement
 * is constrained to `MeasureUnit.AreaUnit`. It simplifies access to area-related
 * measurements by reducing verbosity and enhancing readability.
 *
 * @see RMeasurement
 * @see MeasureUnit.AreaUnit
 * @since 3.0.0
 */
typealias Area = RMeasurement<MeasureUnit.AreaUnit>
/**
 * A type alias representing a measurement of volume.
 *
 * The `Volume` type is a specialized usage of the `RMeasurement` class,
 * bound specifically to scalar units categorized as `MeasureUnit.VolumeUnit`.
 * It represents a numerical value paired with a unit of volume measurement.
 *
 * This type alias enables more meaningful and concise code, improving readability
 * when working with measurements of volume.
 *
 * @since 3.0.0
 */
typealias Volume = RMeasurement<MeasureUnit.VolumeUnit>
/**
 * A type alias for `RMeasurement` with `MeasureUnit.SpeedUnit` as the scalar unit type.
 *
 * This alias represents a speed measurement, where the numerical value is paired
 * with a unit of speed. It simplifies working with speed-related measurements by
 * providing a more descriptive type name.
 *
 * @since 3.0.0
 */
typealias Speed = RMeasurement<MeasureUnit.SpeedUnit>
/**
 * A type alias representing a measurement of acceleration with an associated unit of measurement.
 *
 * This alias simplifies the usage of `RMeasurement` with `MeasureUnit.AccelerationUnit`,
 * which represents acceleration units within the measurement framework.
 *
 * @see RMeasurement
 * @see MeasureUnit.AccelerationUnit
 * @since 3.4.0
 */
typealias Acceleration = RMeasurement<MeasureUnit.AccelerationUnit>
/**
 * A typealias representing a density measurement with units restricted to those of type `MeasureUnit.DensityUnit`.
 *
 * This typealias simplifies the use of `RMeasurement` for operations specifically related to density values,
 * where the unit of measurement is expected to be one of the predefined `DensityUnit`s in the `MeasureUnit` hierarchy.
 *
 * @see RMeasurement
 * @see MeasureUnit.DensityUnit
 * @since 3.0.0
 */
typealias Density = RMeasurement<MeasureUnit.DensityUnit>
/**
 * Typealias representing a plane angle measurement.
 *
 * `PlaneAngle` is a specific type of `RMeasurement` where the units are restricted to
 * those defined under `MeasureUnit.PlaneAngleUnit`. This allows for precise and
 * specialized handling of measurements related to plane angles.
 *
 * @see RMeasurement
 * @see MeasureUnit.PlaneAngleUnit
 * @since 3.0.0
 */
typealias PlaneAngle = RMeasurement<MeasureUnit.PlaneAngleUnit>
/**
 * A type alias representing a restricted measurement of mass.
 *
 * This alias is defined for `RMeasurement` using `MeasureUnit.MassUnit` as the scalar unit type,
 * specialized for measurements that pertain to mass. It simplifies working with mass units while
 * ensuring type safety and adherence to the `RMeasurement` structure.
 *
 * @see RMeasurement
 * @see MeasureUnit.MassUnit
 * @since 3.0.0
 */
typealias Mass = RMeasurement<MeasureUnit.MassUnit>