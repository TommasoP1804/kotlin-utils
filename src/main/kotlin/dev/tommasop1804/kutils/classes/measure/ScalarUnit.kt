package dev.tommasop1804.kutils.classes.measure

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.classes.measure.MeasureUnit.*
import dev.tommasop1804.kutils.exceptions.UnitConversionException
import dev.tommasop1804.kutils.isNotNull
import dev.tommasop1804.kutils.unaryMinus
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.node.ObjectNode
import java.io.Serial
import java.io.Serializable

/**
 * Represents a general interface for units of measurement, providing methods for retrieving
 * their associated properties, such as the {@link MeasureUnit}, name, symbol, and classification
 * within the International System of Units (SI).
 *
 * Implementations of this interface typically define specific measurement units within
 * various domains (e.g., time, mass, length).
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = ScalarUnit.Serializer::class)
@JsonDeserialize(using = ScalarUnit.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = ScalarUnit.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = ScalarUnit.OldDeserializer::class)
@Suppress("unused")
interface ScalarUnit : Serializable {
    /**
     * Represents the unit of measurement associated with a specific scalar value.
     * It defines the standard or system in which the scalar quantity is expressed.
     *
     * 
     * @since 1.0.0
     */
    val measureUnit: MeasureUnit
    /**
     * Indicates whether the scalar unit associated with the current instance is part
     * of the International System of Units (IS).
     *
     * This property determines if the unit adheres to the IS standard, which governs
     * the standardization of physical quantity measurements. IS units are widely used
     * in scientific, industrial, and everyday measurement systems.
     *
     * 
     * @since 1.0.0
     */
    val isSIUnit: Boolean
        get() = measureUnit.isSIUnit
    /**
     * Indicates whether the associated scalar unit is recognized as accepted by the International System of Units (IS).
     *
     * This property reflects if the unit, while not necessarily a base IS unit, is officially accepted for use
     * with the International System of Units.
     *
     * 
     * @since 1.0.0
     */
    val isAcceptedBySI: Boolean
        get() = measureUnit.isAcceptedBySI
    /**
     * Retrieves the name of the unit associated with this scalar unit.
     *
     * This property references the `measureUnit` of the enclosing `ScalarUnit` instance
     * and returns its human-readable name representation.
     *
     * 
     * @return The name of the unit represented by this scalar unit.
     * @since 1.0.0
     */
    val unitName: String
        get() = measureUnit.unitName
    /**
     * Retrieves the corresponding symbol for the scalar unit represented by the current measure unit.
     * The symbol provides a concise identification, often used in scientific or technical notations.
     * The result is `null` if the scalar unit does not have an associated symbol.
     *
     * 
     * @since 1.0.0
     */
    val symbol: String?
        get() = measureUnit.symbol
    /**
     * Retrieves the measurement representation of the associated scalar unit.
     * This value is derived from the `measure` property of the `measureUnit` field within the `ScalarUnit` class.
     *
     * 
     * @return A string representing the measurement identifier or description.
     * @since 1.0.0
     */
    val measure: String
        get() = measureUnit.measure

    /**
     * Indicates whether the measure unit has a recognized or standardized symbol.
     * This property retrieves its value from the associated `measureUnit`.
     *
     * @return `true` if the measure unit has a known standardized symbol, otherwise `false`.
     * @since 1.0.0
     */
    val knownSymbol: Boolean
        get() = measureUnit.knownSymbol

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
        @JsonIgnore @Serial private const val serialVersionUID = 1L

        /**
         * Converts a given measurement to a specified scalar unit.
         *
         * This function performs a unit conversion for measurements of various types such as length, time, mass, temperature, and more.
         * The conversion is done only if the measure type of the input measurement matches the measure type of the target scalar unit.
         *
         * @receiver Scalar unit representing the target unit for the conversion.
         * @param measurement The input measurement to be converted, consisting of a value and its associated unit.
         * @param to The scalar unit to which the measurement should be converted. This must have the same measure as the input measurement.
         * @return A `Measurement` object that holds the converted value along with the target unit.
         * @throws UnitConversionException If the measure of the input measurement does not match the measure of the target scalar unit.
         * @since 1.0.0
         */
        fun convert(measurement: Measurement, to: ScalarUnit) = runCatching {
            val message = "Cannot convert " + measurement.measure + " to " + to.measure
            if (measurement.measure != to.measure) {
                throw UnitConversionException(message)
            }

            Measurement(when (val unit = measurement.unit) {
                is TimeUnit -> unit.convertTo(measurement.value, to as TimeUnit)
                is LengthUnit -> unit.convertTo(measurement.value, to as LengthUnit)
                is MassUnit -> unit.convertTo(measurement.value, to as MassUnit)
                is TemperatureUnit -> unit.convertTo(measurement.value, to as TemperatureUnit)
                is PlaneAngleUnit -> unit.convertTo(measurement.value, to as PlaneAngleUnit)
                is PressureUnit -> unit.convertTo(measurement.value, to as PressureUnit)
                is EnergyUnit -> unit.convertTo(measurement.value, to as EnergyUnit)
                is PowerUnit -> unit.convertTo(measurement.value, to as PowerUnit)
                is AreaUnit -> unit.convertTo(measurement.value, to as AreaUnit)
                is VolumeUnit -> unit.convertTo(measurement.value, to as VolumeUnit)
                is SpeedUnit -> unit.convertTo(measurement.value, to as SpeedUnit)
                is AccelerationUnit -> unit.convertTo(measurement.value, to as AccelerationUnit)
                is DensityUnit -> unit.convertTo(measurement.value, to as DensityUnit)
                is DataSizeUnit -> unit.convertTo(measurement.value, to as DataSizeUnit)
                else -> throw UnitConversionException(message)
            }, to)
        }

        /**
         * Attempts to retrieve a scalar unit by its known unit name.
         * Searches through various unit categories (e.g., time, length, mass, etc.)
         * to find a matching unit name. The unit name comparison is case-insensitive
         * and trims any extra spaces.
         *
         * 
         * @param name The name of the unit to search for.
         * @return The matching ScalarUnit if found, or null if no matching unit is identified.
         * @since 1.0.0
         */
        infix fun ofKnownUnitName(name: String): ScalarUnit? {
            val effName = (-name).trim()

            TimeUnit.ofName(effName)?.let { return it }
            LengthUnit.ofName(effName)?.let { return it }
            MassUnit.ofName(effName)?.let { return it }
            TemperatureUnit.ofName(effName)?.let { return it }
            PlaneAngleUnit.ofName(effName)?.let { return it }
            PressureUnit.ofName(effName)?.let { return it }
            EnergyUnit.ofName(effName)?.let { return it }
            PowerUnit.ofName(effName)?.let { return it }
            AreaUnit.ofName(effName)?.let { return it }
            VolumeUnit.ofName(effName)?.let { return it }
            SpeedUnit.ofName(effName)?.let { return it }
            AccelerationUnit.ofName(effName)?.let { return it }
            DensityUnit.ofName(effName)?.let { return it }
            return null
        }

        /**
         * Converts a given unit symbol into a corresponding known `ScalarUnit` instance if it matches
         * a predefined symbol of a supported unit category such as time, length, mass, etc.
         *
         * 
         * @param symbol The string representation of the unit symbol to be converted.
         * @return The corresponding `ScalarUnit` if the symbol matches a known unit, or `null` if no match is found.
         * @since 1.0.0
         */
        infix fun ofKnownUnitSymbol(symbol: String): ScalarUnit? {
            val effSymbol = (-symbol).trim()

            TimeUnit.ofSymbol(effSymbol)?.let { return it }
            LengthUnit.ofSymbol(effSymbol)?.let { return it }
            MassUnit.ofSymbol(effSymbol)?.let { return it }
            TemperatureUnit.ofSymbol(effSymbol)?.let { return it }
            PlaneAngleUnit.ofSymbol(effSymbol)?.let { return it }
            PressureUnit.ofSymbol(effSymbol)?.let { return it }
            EnergyUnit.ofSymbol(effSymbol)?.let { return it }
            PowerUnit.ofSymbol(effSymbol)?.let { return it }
            AreaUnit.ofSymbol(effSymbol)?.let { return it }
            VolumeUnit.ofSymbol(effSymbol)?.let { return it }
            SpeedUnit.ofSymbol(effSymbol)?.let { return it }
            AccelerationUnit.ofSymbol(effSymbol)?.let { return it }
            DensityUnit.ofSymbol(effSymbol)?.let { return it }
            return null
        }
    }

    class Serializer : ValueSerializer<ScalarUnit>() {
        override fun serialize(value: ScalarUnit, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
            gen.writeStartObject()
            gen.writePOJOProperty("measure", value.measure)
            gen.writePOJOProperty("name", value.unitName)
            gen.writeBooleanProperty("isSIUnit", value.isSIUnit)
            gen.writeBooleanProperty("isAcceptedBySI", value.isAcceptedBySI)
            if (value.symbol.isNotNull()) gen.writePOJOProperty("symbol", value.symbol)
            else gen.writeNullProperty("symbol")
            gen.writeEndObject()
        }
    }

    class Deserializer : ValueDeserializer<ScalarUnit>() {
        override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): ScalarUnit {
            val node = p.objectReadContext().readTree<ObjectNode>(p)
            return MeasureUnit(
                node.get("name").asString(),
                node.get("measure").asString(),
                if (node.get("symbol").isNull) null else node.get("symbol").asString(),
                node.get("isSIUnit").asBoolean(),
                node.get("isAcceptedBySI").asBoolean()
            )
        }
    }

    class OldSerializer : JsonSerializer<ScalarUnit>() {
        override fun serialize(value: ScalarUnit, gen: JsonGenerator, serializerProvider: SerializerProvider?) {
            gen.writeStartObject()
            gen.writeObjectField("measure", value.measure)
            gen.writeObjectField("name", value.unitName)
            gen.writeBooleanField("isSIUnit", value.isSIUnit)
            gen.writeBooleanField("isAcceptedBySI", value.isAcceptedBySI)
            if (value.symbol.isNotNull()) gen.writeObjectField("symbol", value.symbol)
            else gen.writeNullField("symbol")
            gen.writeEndObject()
        }
    }

    class OldDeserializer : JsonDeserializer<ScalarUnit>() {
        override fun deserialize(p: JsonParser, deserializationContext: com.fasterxml.jackson.databind.DeserializationContext?): ScalarUnit {
            val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
            return MeasureUnit(
                node.get("name").asText(),
                node.get("measure").asText(),
                if (node.get("symbol").isNull) null else node.get("symbol").asText(),
                node.get("isSIUnit").asBoolean(),
                node.get("isAcceptedBySI").asBoolean()
            )
        }
    }
}