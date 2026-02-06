package dev.tommasop1804.kutils.classes.measure

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
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
import kotlin.math.pow
import kotlin.reflect.KProperty

/**
 * Represents a unit of measurement associated with a specific measure type.
 * Provides functionality to define, serialize, and deserialize measurement units,
 * including optional properties such as symbols and their acceptance as International
 * System of Units (IS) measurements.
 *
 * This class is immutable and designed to be used in contexts requiring serialization
 * to and deserialization from JSON.
 *
 * @property measure The type of physical measurement (e.g., length, time).
 * @property unitName The unique name of the measurement unit.
 * @property isSIUnit Indicates whether this unit is part of the International System of Units (IS).
 * @property isAcceptedBySI Indicates whether this unit is accepted but not part of IS.
 * @property symbol An optional abbreviation or symbol for the measurement unit.
 * @property knownSymbol If the symbol can be printed as himself without other informations.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = MeasureUnit.Companion.Serializer::class)
@JsonDeserialize(using = MeasureUnit.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = MeasureUnit.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = MeasureUnit.Companion.OldDeserializer::class)
@Suppress("unused")
class MeasureUnit internal constructor(override val measure: String, override val unitName: String, override val isSIUnit: Boolean, override val isAcceptedBySI: Boolean, override val symbol: String?, override val knownSymbol: Boolean): ScalarUnit, Serializable {
    /**
     * Provides access to the current measurement unit instance. This property ensures
     * the returned value is the specific `MeasureUnit` associated with the implementing object.
     *
     * 
     * @return the current `MeasureUnit` instance.
     * @since 1.0.0
     */
    override val measureUnit: MeasureUnit
        get() = this

    /**
     * Constructs a new instance of `MeasureUnit` with the specified attributes.
     *
     * This constructor is used to create a `MeasureUnit` by providing the name of the unit,
     * its associated measurement category, optionally its symbol, and flags indicating whether
     * the unit is an International System (IS) unit and/or accepted by the IS.
     *
     * 
     * @param unitName The lowercase name of the unit.
     * @param measure The measurement category to which the unit belongs.
     * @param symbol The optional symbol representing the unit; defaults to `null` if unspecified.
     * @param isSIUnit A flag indicating whether the unit is defined as an IS unit; defaults to `false`.
     * @param isAcceptedBySI A flag indicating whether the unit is accepted by the IS; defaults to `false`.
     * @since 1.0.0
     */
    constructor(unitName: String, measure: String, symbol: String? = null, isSIUnit: Boolean = false, isAcceptedBySI: Boolean = false)
            : this(measure, -unitName, isSIUnit, isAcceptedBySI, symbol, false)
    /**
     * Constructs a new instance of `MeasureUnit` with the specified attributes.
     *
     * This constructor is used to create a `MeasureUnit` by providing the name of the unit,
     * its associated measurement category, optionally its symbol, and flags indicating whether
     * the unit is an International System (IS) unit and/or accepted by the IS.
     *
     *
     * @param unitName The lowercase name of the unit.
     * @param measure The measurement category to which the unit belongs.
     * @param symbol The optional symbol representing the unit; defaults to `null` if unspecified.
     * @param isSIUnit A flag indicating whether the unit is defined as an IS unit; defaults to `false`.
     * @param isAcceptedBySI A flag indicating whether the unit is accepted by the IS; defaults to `false`.
     * @property knownSymbol If the symbol can be printed as himself without other informations.
     * @since 1.0.0
     */
    constructor(unitName: String, measure: String, symbol: String? = null, isSIUnit: Boolean = false, isAcceptedBySI: Boolean = false, knownSymbol: Boolean)
            : this(measure, -unitName, isSIUnit, isAcceptedBySI, symbol, knownSymbol)
    /**
     * Constructs a new instance of `MeasureUnit` using the provided parameters.
     * This constructor is mainly utilized to define characteristics of a unit
     * associated with a specific [Measure].
     *
     * @param unitName the name of the unit. This value is converted to lowercase internally.
     * @param measure the [Measure] associated with this unit, representing its type or category.
     * @param symbol an optional symbol or abbreviation representing the unit, defaulting to `null` if not provided.
     * @param isSIUnit a flag indicating whether the unit is part of the International System of Units (SI), defaulting to `false`.
     * @param isAcceptedBySI a flag specifying whether the unit is officially accepted or recognized by the International System of Units (SI), defaulting to `false`.
     * @since 1.0.0
     */
    constructor(unitName: String, measure: Measure, symbol: String? = null, isSIUnit: Boolean = false, isAcceptedBySI: Boolean = false)
            : this(measure.name, -unitName, isSIUnit, isAcceptedBySI, symbol, false)

    /**
     * Secondary constructor for the `MeasureUnit` class, allowing initialization with a specific set of parameters.
     *
     * @param unitName The name of the unit as a string.
     * @param measure The measure associated with the unit.
     * @param symbol The optional symbol representing the unit, defaults to `null`.
     * @param isSIUnit A boolean indicating whether the unit is part of the International System of Units (SI), defaults to `false`.
     * @param isAcceptedBySI A boolean indicating whether the unit is accepted by the International System of Units (SI), defaults to `false`.
     * @param knownSymbol A boolean indicating whether the unit is represented by a known symbol.
     * @since 1.0.0
     */
    constructor(unitName: String, measure: Measure, symbol: String? = null, isSIUnit: Boolean = false, isAcceptedBySI: Boolean = false, knownSymbol: Boolean)
            : this(measure.name, -unitName, isSIUnit, isAcceptedBySI, symbol, knownSymbol)

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
         * A collection of known scalar units categorized by their respective measures.
         *
         * This property combines the `knownSymbol` sets from various unit types like
         * time, length, mass, temperature, plane angle, pressure, energy, power, area,
         * volume, speed, acceleration, density, and data size.
         *
         * The resulting set includes the scalar units defined and recognized in each
         * category, facilitating unified access to all known scalar units.
         *
         * @return A set containing instances of [ScalarUnit] representing all known scalar unit symbols.
         * @since 1.0.0
         */
        val knownUnitsConsts: Set<ScalarUnit>
            get() = TimeUnit.knownSymbol
            .plus(LengthUnit.knownSymbol)
            .plus(MassUnit.knownSymbol)
            .plus(TemperatureUnit.knownSymbol)
            .plus(PlaneAngleUnit.knownSymbol)
            .plus(PressureUnit.knownSymbol)
            .plus(EnergyUnit.knownSymbol)
            .plus(PowerUnit.knownSymbol)
            .plus(AreaUnit.knownSymbol)
            .plus(VolumeUnit.knownSymbol)
            .plus(SpeedUnit.knownSymbol)
            .plus(AccelerationUnit.knownSymbol)
            .plus(DensityUnit.knownSymbol)
            .plus(DataSizeUnit.knownSymbol)

        /**
         * A computed property that provides a unique set of all known `MeasureUnit` instances.
         *
         * The `knownUnits` set is derived by mapping `ScalarUnit` instances to their associated `MeasureUnit`
         * and converting the result into a `Set` to ensure uniqueness. This property acts as a central
         * collection of recognized measurement units available in the system.
         *
         * @return A `Set` containing unique `MeasureUnit` instances.
         * @since 1.0.0
         */
        val knownUnits: Set<MeasureUnit>
            get() = knownUnitsConsts
                .map(ScalarUnit::measureUnit)
                .toSet()

        class Serializer : ValueSerializer<MeasureUnit>() {
            override fun serialize(
                value: MeasureUnit,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
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

        class Deserializer : ValueDeserializer<MeasureUnit>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): MeasureUnit {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                return MeasureUnit(
                    node.get("name").asString(),
                    Measure.valueOf(node.get("measure").asString()),
                    if (node.get("symbol").isNull) null else node.get("symbol").asString(),
                    node.get("isSIUnit").asBoolean(),
                    node.get("isAcceptedBySI").asBoolean()
                )
            }
        }

        class OldSerializer : JsonSerializer<MeasureUnit>() {
            override fun serialize(value: MeasureUnit, gen: JsonGenerator, serializers: SerializerProvider?) {
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

        class OldDeserializer : JsonDeserializer<MeasureUnit>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext?): MeasureUnit {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                return MeasureUnit(
                    node.get("name").asText(),
                    Measure.valueOf(node.get("measure").asText()),
                    if (node.get("symbol").isNull) null else node.get("symbol").asText(),
                    node.get("isSIUnit").asBoolean(),
                    node.get("isAcceptedBySI").asBoolean()
                )
            }
        }
    }

    /**
     * Creates a copy of the current `MeasureUnit` instance with the option to replace specific properties.
     *
     * @param measure The measure associated with the unit. Defaults to the measure of the current instance.
     * @param unitName The name of the unit. Defaults to the unit name of the current instance.
     * @param isISUnit A boolean indicating whether the unit is part of the International System of Units (IS). Defaults to the value of the current instance.
     * @param isAcceptedByIS A boolean indicating whether the unit is accepted by the International System of Units (IS). Defaults to the value of the current instance.
     * @param symbol The symbol representing the unit. Defaults to the symbol of the current instance.
     * @return A new `MeasureUnit` instance with the updated or original properties.
     * @since 1.0.0
     */
    fun copy(measure: String = this.measure, unitName: String = this.unitName, isISUnit: Boolean = isSIUnit, isAcceptedByIS: Boolean = isAcceptedBySI, symbol: String? = this.symbol): MeasureUnit =
        MeasureUnit(measure, unitName, isISUnit, isAcceptedByIS, symbol, knownSymbol)

    /**
     * Returns the string representation of the `MeasureUnit` instance.
     *
     * The output includes the measure, unit name, IS unit status, IS acceptance status, and the symbol
     * of the measure unit.
     *
     * 
     * @return A string representation of this `MeasureUnit` instance.
     * @since 1.0.0
     */
    override fun toString(): String {
        return "MeasureUnit(" +
                "measure=" + measure +
                ", name='" + unitName + '\'' +
                ", isSIUnit=" + isSIUnit +
                ", isAcceptedBySI=" + isAcceptedBySI +
                ", symbol='" + symbol + '\'' +
                ')'
    }

    /**
     * Determines whether the specified object is equal to this `MeasureUnit` instance.
     *
     * The comparison checks if the `other` object is of the same type and whether all
     * the corresponding fields of the `MeasureUnit` class (such as `isISUnit`, `isAcceptedByIS`,
     * `measure`, `unitName`, and `symbol`) are equal.
     *
     * 
     * @param other The object to compare with this instance for equality.
     * @return `true` if the `other` object is equal to this instance; `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MeasureUnit

        if (isSIUnit != other.isSIUnit) return false
        if (isAcceptedBySI != other.isAcceptedBySI) return false
        if (measure != other.measure) return false
        if (unitName != other.unitName) return false
        if (symbol != other.symbol) return false

        return true
    }

    /**
     * Computes a hash code for the `MeasureUnit` instance based on its properties.
     * The hash code is generated using the values of `isISUnit`, `isAcceptedByIS`,
     * `measure`, `unitName`, and `symbol`, ensuring unique hash code generation
     * for distinct instances.
     *
     * 
     * @return An integer value representing the hash code of the current instance.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = isSIUnit.hashCode()
        result = 31 * result + isAcceptedBySI.hashCode()
        result = 31 * result + measure.hashCode()
        result = 31 * result + unitName.hashCode()
        result = 31 * result + (symbol?.hashCode() ?: 0)
        return result
    }

    /**
     * Converts the current instance of `MeasureUnit` into a map representation.
     *
     * This method creates a map where specific properties of the `MeasureUnit` class
     * are represented as key-value pairs. The keys in the map are `"measure"`, `"unitName"`,
     * and `"symbol"`. The associated values are the corresponding property values of the
     * `MeasureUnit` instance.
     *
     * @return A map with keys `"measure"`, `"unitName"`, and `"symbol"` representing
     *         the state of the `MeasureUnit` instance.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "measure" to measure,
        "unitName" to unitName,
        "symbol" to symbol
    )

    /**
     * Retrieves the value of the specified property from the internal map representation of the `MeasureUnit`.
     *
     * - `measure` - The measure associated with the unit - TYPE: [String].
     * - `unitName` - The name of the unit - TYPE: [String].
     * - `symbol` - The symbol representing the unit - TYPE: `String?`.
     *
     * @param thisRef The reference to the object for which the property is being accessed. Can be `null` as it's not used in this implementation.
     * @param property The metadata for the property being accessed. The property's name is used to look up the value in the internal map.
     * @return The value of the specified property cast to the generic type parameter `R`.
     * @throws NoSuchElementException If the property's name is not present in the internal map.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R

    /**
     * Represents units of time measurement, defined with their respective attributes
     * such as name, measurement category, symbol, and IS unit characteristics.
     *
     * Each `TimeUnit` corresponds to a predefined measurement unit (e.g., second, minute, etc.)
     * and provides functionality for conversion between units.
     *
     * @property measureUnit The underlying `MeasureUnit` object describing the properties of the time unit.
     * @since 1.0.0
     */
    enum class TimeUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        NANOSECOND(MeasureUnit("nanosecond", "time", "ns", knownSymbol = true)),
        MILLISECOND(MeasureUnit("millisecond", "time", "ms", knownSymbol = true)),
        SECOND(MeasureUnit("second", "time", "s", isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)),
		MINUTE(MeasureUnit("minute", "time", "min", isAcceptedBySI = true, knownSymbol = true)),
		HOUR(MeasureUnit("hour", "time", "h", isAcceptedBySI = true, knownSymbol = true)),
		DAY(MeasureUnit("day", "time", "d", isAcceptedBySI = true, knownSymbol = true)),
		WEEK(MeasureUnit("week", "time", "wk", knownSymbol = true)),
		MONTH(MeasureUnit("month", "time", "mo", knownSymbol = true)),
		YEAR(MeasureUnit("year", "time", "yr", knownSymbol = true));

        companion object {
            infix fun ofName(name: String): TimeUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): TimeUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<TimeUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()
            
            val knownSymbol: Set<TimeUnit>
                get() = entries.filter { it.knownSymbol }.toSet()
        }

        private fun toSeconds(value: Double) = when (this) {
            NANOSECOND -> value / 1e9
            MILLISECOND -> value / 1e3
            SECOND -> value
            MINUTE -> value * 60
            HOUR -> value * 3600
            DAY -> value * 86400
            WEEK -> value * 604800
            MONTH -> value * 2592000
            YEAR -> value * 31536000
        }

        private fun fromSeconds(value: Double) = when (this) {
            NANOSECOND -> value * 1e9
            MILLISECOND -> value * 1e3
            SECOND -> value
            MINUTE -> value / 60
            HOUR -> value / 3600
            DAY -> value / 86400
            WEEK -> value / 604800
            MONTH -> value / 2592000
            YEAR -> value / 31536000
        }

        fun convertTo(value: Double, targetUnit: TimeUnit) = targetUnit.fromSeconds(toSeconds(value))
    }

    /**
     * Represents different units of length, each associated with a `MeasureUnit`
     * containing detailed information like its name, measurement type, symbol, and
     * whether it's an International System (IS) unit or accepted by the IS.
     *
     * This enumeration provides functionality to retrieve accepted IS units and
     * to convert values between different length units, utilizing internal
     * conversion methods to and from meters as a base unit.
     *
     * @property measureUnit The underlying `MeasureUnit` associated with this `LengthUnit`.
     * @since 1.0.0
     */
    enum class LengthUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        METER(MeasureUnit("meter", "length", "m", isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)),
		KILOMETER(MeasureUnit("kilometer", "length", "km", knownSymbol = true)),
		MILE(MeasureUnit("mile", "length", "mi", knownSymbol = true)),
		NAUTICAL_MILE(MeasureUnit("nautical mile", "length", "nm", isAcceptedBySI = true, knownSymbol = true)),
		FOOT(MeasureUnit("foot", "length", "ft", knownSymbol = true)),
		INCH(MeasureUnit("inch", "length", "in", knownSymbol = true)),
		YARD(MeasureUnit("yard", "length", "yd", knownSymbol = true)),
		LIGHT_YEAR(MeasureUnit("light year", "length", "ly", knownSymbol = true)),
		ASTRONOMICAL_UNIT(MeasureUnit("astronomical unit", "length", "au", isAcceptedBySI = true, knownSymbol = true)),
		ANGSTROM(MeasureUnit("ångström", "length", "Å", isAcceptedBySI = true, knownSymbol = true));

        companion object {
            infix fun ofName(name: String): LengthUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): LengthUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<LengthUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<LengthUnit>
                get() = LengthUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toMeters(value: Double) = when (this) {
            METER -> value
            KILOMETER -> value * 1000
            MILE -> value * 1609.34
            NAUTICAL_MILE -> value * 1852
            FOOT -> value * 0.3048
            INCH -> value * 0.0254
            YARD -> value * 0.9144
            LIGHT_YEAR -> value * 9.4607e15
            ASTRONOMICAL_UNIT -> value * 1.496e11
            ANGSTROM -> value / 1e10
        }

        private fun fromMeters(value: Double) = when (this) {
            METER -> value
            KILOMETER -> value / 1000
            MILE -> value / 1609.34
            NAUTICAL_MILE -> value / 1852
            FOOT -> value / 0.3048
            INCH -> value / 0.0254
            YARD -> value / 0.9144
            LIGHT_YEAR -> value / 9.4607e15
            ASTRONOMICAL_UNIT -> value / 1.496e11
            ANGSTROM -> value * 1e10
        }

        fun convertTo(value: Double, targetUnit: LengthUnit) = targetUnit.fromMeters(toMeters(value))
    }

    /**
     * Represents a collection of mass units for measurement.
     * Each unit includes its measure, name, symbol, and International System (IS) compliance properties.
     * Supports unit conversion and retrieval based on name or symbol.
     *
     * All units are derived from the [MeasureUnit] class and implement the [ScalarUnit] interface.
     * This class also provides functionalities for inter-unit conversions via `toKilograms` and `fromKilograms` methods.
     *
     * @property measureUnit The measure unit object containing corresponding attributes such as name, symbol, and compliance statuses.
     * @since 1.0.0
     */
    enum class MassUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        KILOGRAM(MeasureUnit("kilogram", "mass", "kg", isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)),
		GRAM(MeasureUnit("gram", "mass", "g", knownSymbol = true)),
		TONNE(MeasureUnit("tonne", "mass", "t", isAcceptedBySI = true, knownSymbol = true)),
		POUND(MeasureUnit("pound", "mass", "lb", knownSymbol = true)),
		OUNCE(MeasureUnit("ounce", "mass", "oz", knownSymbol = true)),
		STONE(MeasureUnit("stone", "mass", "st", knownSymbol = true)),
		CARAT(MeasureUnit("carat", "mass", "ct", knownSymbol = true)),
		SLUG(MeasureUnit("slug", "mass", knownSymbol = true)),
		ATOMIC_MASS_UNIT(MeasureUnit("atomic mass unit", "mass", "u", isAcceptedBySI = true, knownSymbol = true));

        companion object {
            infix fun ofName(name: String): MassUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): MassUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<MassUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<MassUnit>
                get() = MassUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toKilograms(value: Double) = when (this) {
           KILOGRAM -> value
           GRAM -> value / 1000
           TONNE -> value * 1000
           POUND -> value * 0.45359237
           OUNCE -> value * 0.028349523125
           STONE -> value * 6.35029318
           CARAT -> value * 0.002
           SLUG -> value * 14.5939
           ATOMIC_MASS_UNIT -> value * 1.66053886e-27
        }

        private fun fromKilograms(value: Double) = when (this) {
            KILOGRAM -> value
            GRAM -> value * 1000
            TONNE -> value / 1000
            POUND -> value / 0.45359237
            OUNCE -> value / 0.028349523125
            STONE -> value / 6.35029318
            CARAT -> value / 0.002
            SLUG -> value / 14.5939
            ATOMIC_MASS_UNIT -> value / 1.66053886e-27
        }

        fun convertTo(value: Double, targetUnit: MassUnit) = targetUnit.fromKilograms(toKilograms(value))
    }

    /**
     * Represents an enumeration of temperature units used for measurements.
     * Each unit is associated with a `MeasureUnit` object containing its properties.
     *
     * @property measureUnit The `MeasureUnit` instance associated with the temperature unit.
     * @since 1.0.0
     */
    enum class TemperatureUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        KELVIN(MeasureUnit("kelvin", "temperature", "K", true, isAcceptedBySI = true, knownSymbol = true)),
		CELSIUS(MeasureUnit("celsius", "temperature", "°C", isAcceptedBySI = true, knownSymbol = true)),
		FAHRENHEIT(MeasureUnit("fahrenheit", "temperature", "°F", knownSymbol = true));

        companion object {
            infix fun ofName(name: String): TemperatureUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): TemperatureUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<TemperatureUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<TemperatureUnit>
                get() = TemperatureUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toKelvin(value: Double) = when (this) {
            KELVIN -> value
            CELSIUS -> value + 273.15
            FAHRENHEIT -> (value - 32) * 5 / 9 + 273.15
        }

        private fun fromKelvin(value: Double) = when (this) {
            KELVIN -> value
            CELSIUS -> value - 273.15
            FAHRENHEIT -> (value - 273.15) * 9 / 5 + 32
        }

        fun convertTo(value: Double, targetUnit: TemperatureUnit) = targetUnit.fromKelvin(toKelvin(value))
    }

    /**
     * Represents the different units of plane angles. Implements the `ScalarUnit` interface,
     * allowing interaction with broader scalar measurement concepts.
     *
     * Each unit is defined with a corresponding `MeasureUnit` instance containing its
     * properties, including name, category, symbol, and IS standard compliance.
     *
     * @param measureUnit The related `MeasureUnit` instance containing properties
     * of the specific unit.
     * @since 1.0.0
     */
    enum class PlaneAngleUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        DEGREE_OF_ARC(MeasureUnit("degree of arc", "PLANE_ANGLE", "°", isAcceptedBySI = true, knownSymbol = true)),
		MINUTE_OF_ARC(MeasureUnit("minute of arc", "PLANE_ANGLE", "'", isAcceptedBySI = true, knownSymbol = true)),
		SECOND_OF_ARC(MeasureUnit("second of arc", "PLANE_ANGLE", "\"", isAcceptedBySI = true, knownSymbol = true)),
		RADIAN(MeasureUnit("radian", "PLANE_ANGLE", "rad", true, isAcceptedBySI = true, knownSymbol = true)),
		GRADIAN(MeasureUnit("gradian", "PLANE_ANGLE", "grad", knownSymbol = true));

        companion object {
            infix fun ofName(name: String): PlaneAngleUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): PlaneAngleUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<PlaneAngleUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<PlaneAngleUnit>
                get() = PlaneAngleUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toRadians(value: Double) = when (this) {
            DEGREE_OF_ARC -> Math.toRadians(value)
            MINUTE_OF_ARC -> Math.toRadians(value / 60)
            SECOND_OF_ARC -> Math.toRadians(value / 3600)
            RADIAN -> value
            GRADIAN -> Math.toRadians(value / 200) / 10
        }

        private fun fromRadians(value: Double) = when (this) {
            DEGREE_OF_ARC -> Math.toDegrees(value)
            MINUTE_OF_ARC -> Math.toDegrees(value) * 60
            SECOND_OF_ARC -> Math.toDegrees(value) * 3600
            RADIAN -> value
            GRADIAN -> Math.toDegrees(value) * 200 / 10
        }

        fun convertTo(value: Double, targetUnit: PlaneAngleUnit) = targetUnit.fromRadians(toRadians(value))
    }

    /**
     * Represents a pressure measurement unit, implementing the `ScalarUnit` interface.
     * Each unit has associated metadata, such as its name, symbol, and whether it is an
     * International System (IS) unit or accepted by the IS.
     *
     * @property measureUnit The underlying `MeasureUnit` associated with the given pressure unit.
     * @since 1.0.0
     */
    enum class PressureUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        PASCAL(MeasureUnit("pascal", "pressure", "Pa", isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)),
		BAR(MeasureUnit("bar", "pressure", "bar", isAcceptedBySI = true, knownSymbol = true)),
		ATMOSPHERE(MeasureUnit("atmosphere", "pressure", "atm", knownSymbol = true)),
		TECHNICAL_ATMOSPHERE(MeasureUnit("technical atmosphere", "pressure", "at", knownSymbol = true)),
		MILLIMTER_OF_MERCURY(MeasureUnit("millimeter of mercury", "pressure", "mmHg", isAcceptedBySI = true, knownSymbol = true)),
		INCH_OF_MERCURY(MeasureUnit("inch of mercury", "pressure", "inHg", isAcceptedBySI = true, knownSymbol = true)),
		POUND_PER_SQUARE_INCH(MeasureUnit("pound per square inch", "pressure", "psi", knownSymbol = true)),
		TORR(MeasureUnit("torr", "pressure", "Torr", knownSymbol = true));

        companion object {
            infix fun ofName(name: String): PressureUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): PressureUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<PressureUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<PressureUnit>
                get() = PressureUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toPascal(value: Double) = when (this) {
            PASCAL -> value
			BAR -> value * 100000
			ATMOSPHERE -> value * 101325
			TECHNICAL_ATMOSPHERE -> value * 98066.5
			MILLIMTER_OF_MERCURY -> value * 133.32236842105263
			INCH_OF_MERCURY -> value * 3386.3886664184383
			POUND_PER_SQUARE_INCH -> value * 6894.76
			TORR -> (value * 101325) / 760
        }

        private fun fromPascals(value: Double) = when (this) {
            PASCAL -> value
            BAR -> value / 100000
            ATMOSPHERE -> value / 101325
            TECHNICAL_ATMOSPHERE -> value / 98066.5
            MILLIMTER_OF_MERCURY -> value / 133.32236842105263
            INCH_OF_MERCURY -> value / 3386.3886664184383
            POUND_PER_SQUARE_INCH -> value / 6894.76
            TORR -> 760 * (value / (101325))
        }

        fun convertTo(value: Double, targetUnit: PressureUnit) = targetUnit.fromPascals(toPascal(value))
    }

    /**
     * Represents the enumeration of energy units and their associated properties and functionalities.
     * Each energy unit is defined with its respective `MeasureUnit`, which includes its name, category,
     * symbol, and specifications regarding IS (International System) compliance or acceptance.
     *
     * @property measureUnit The underlying `MeasureUnit` instance representing the unit's attributes.
     * @since 1.0.0
     */
    enum class EnergyUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        JOULE(MeasureUnit("joule", "energy", "J", isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)),
        CALORIE(MeasureUnit("calorie", "energy", "Cal", knownSymbol = true)),
        KILOCALORIE(MeasureUnit("kilocalorie", "energy", "kCal", knownSymbol = true)),
        ELECTRONVOLT(MeasureUnit("electronvolt", "energy", "eV", isAcceptedBySI = true, knownSymbol = true)),
        BRITISH_TERMAL_UNIT(MeasureUnit("British thermal unit", "energy", "BTU")),
        ERG(MeasureUnit("erg", "energy", "erg")),
        FOOT_POUND_FORCE(MeasureUnit("foot-pound force", "energy", "F·lb")),
        KILOWATT_HOUR(MeasureUnit("kilowatt-hour", "energy", "kWh"));

        companion object {
            infix fun ofName(name: String): EnergyUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): EnergyUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<EnergyUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<EnergyUnit>
                get() = EnergyUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toJoules(value: Double) = when (this) {
            JOULE -> value
            CALORIE -> value * 4184
            KILOCALORIE -> value * 4184000
            ELECTRONVOLT -> value * 1.602176634e-19
            BRITISH_TERMAL_UNIT -> value * 1055.06
            ERG -> value * 1e-7
            FOOT_POUND_FORCE -> value * 1.3558179483314003
            KILOWATT_HOUR -> value * 3600000
        }

        private fun fromJoules(value: Double) = when (this) {
            JOULE -> value
            CALORIE -> value / 4184
            KILOCALORIE -> value / 4184000
            ELECTRONVOLT -> value / 1.602176634e-19
            BRITISH_TERMAL_UNIT -> value / 1055.06
            ERG -> value / 1e-7
            FOOT_POUND_FORCE -> value / 1.3558179483314003
            KILOWATT_HOUR -> value / 3600000
        }

        fun convertTo(value: Double, targetUnit: EnergyUnit) = targetUnit.fromJoules(toJoules(value))
    }

    /**
     * Represents a set of power units, each defined with its respective `MeasureUnit` instance.
     * This enumeration provides conversions between various power units and utilities to retrieve
     * specific units by their name or symbol.
     *
     * @property measureUnit The underlying `MeasureUnit` instance associated with the power unit.
     * @since 1.0.0
     */
    enum class PowerUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        WATT(MeasureUnit("watt", "power", "W", isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)),
		HORSEPOWER(MeasureUnit("horsepower", "power", "HP", knownSymbol = true)),
		FOOT_POUND_PER_SECOND(MeasureUnit("foot-pound per second", "power", "ft·lb/s")),
		CALORIE_PER_SECOND(MeasureUnit("calorie per second", "power", "Cal/s")),
		BTU_PER_HOUR(MeasureUnit("British thermal unit per hour", "power", "BTU/h"));

        companion object {
            infix fun ofName(name: String): PowerUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): PowerUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<PowerUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<PowerUnit>
                get() = PowerUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toWatts(value: Double) = when (this) {
            WATT -> value
            HORSEPOWER -> value * 745.6998715822702
            FOOT_POUND_PER_SECOND -> value * 1.3558179483314003
            CALORIE_PER_SECOND -> value * 4.184
            BTU_PER_HOUR -> value * 0.293071
        }

        private fun fromWatts(value: Double) = when (this) {
            WATT -> value
            HORSEPOWER -> value / 745.6998715822702
            FOOT_POUND_PER_SECOND -> value / 1.3558179483314003
            CALORIE_PER_SECOND -> value / 4.184
            BTU_PER_HOUR -> value / 0.293071
        }

        fun convertTo(value: Double, targetUnit: PowerUnit) = targetUnit.fromWatts(toWatts(value))
    }

    /**
     * Represents a set of area measurement units along with their properties and conversion logic.
     *
     * Each unit is associated with a `MeasureUnit` that defines its characteristics such as name, symbol,
     * whether it is an International System (IS) unit, and whether it is accepted by the IS.
     *
     * Implements the `ScalarUnit` interface to provide standard unit properties.
     *
     * @property measureUnit The underlying `MeasureUnit` instance for this scalar unit.
     * @since 1.0.0
     */
    enum class AreaUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        SQUARE_METER(MeasureUnit("square meter", "area", "m²", isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)),
        SQUARE_KILOMETER(MeasureUnit("square kilometer", "area", "km²", knownSymbol = true)),
		ARE(MeasureUnit("are", "area", "a", knownSymbol = true)),
		HECTARE(MeasureUnit("hectare", "area", "ha", isAcceptedBySI = true, knownSymbol = true)),
		SQUARE_FOOT(MeasureUnit("square foot", "area", "ft²", knownSymbol = true)),
		SQUARE_INCH(MeasureUnit("square inch", "area", "in²", knownSymbol = true)),
		SQUARE_YARD(MeasureUnit("square yard", "area", "yd²", knownSymbol = true)),
		SQUARE_MILE(MeasureUnit("square mile", "area", "mi²", knownSymbol = true)),
		ACRE(MeasureUnit("acre", "area", "ac", knownSymbol = true)),
		BARN(MeasureUnit("barn", "area", "b", isAcceptedBySI = true, knownSymbol = true));

        companion object {
            infix fun ofName(name: String): AreaUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): AreaUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<AreaUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<AreaUnit>
                get() = AreaUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toSquareMeters(value: Double) = when (this) {
            SQUARE_METER -> value
            SQUARE_KILOMETER -> value * 1000000.0
            ARE -> value * 100
            HECTARE -> value * 10000
            SQUARE_FOOT -> value * 0.09290304
            SQUARE_INCH -> value * 0.00064516
            SQUARE_YARD -> value * 0.83612736
            SQUARE_MILE -> value * 2589988.110336
            ACRE -> value * 4046.8564224
            BARN -> value * 1e-28
        }

        private fun fromSquareMeters(value: Double) = when (this) {
            SQUARE_METER -> value
            SQUARE_KILOMETER -> value / 1000000.0
            ARE -> value / 100
            HECTARE -> value / 10000
            SQUARE_FOOT -> value / 0.09290304
            SQUARE_INCH -> value / 0.00064516
            SQUARE_YARD -> value / 0.83612736
            SQUARE_MILE -> value / 2589988.110336
            ACRE -> value / 4046.8564224
            BARN -> value / 1e-28
        }

        fun convertTo(value: Double, targetUnit: AreaUnit) = targetUnit.fromSquareMeters(toSquareMeters(value))
    }

    /**
     * Represents a set of volume units and provides methods for unit conversion.
     *
     * Each volume unit is associated with a specific `MeasureUnit` that contains
     * its name, measurement category, symbol, and its status as an International System (IS)
     * unit or its acceptance by IS.
     *
     * This enum implements `ScalarUnit` and provides functionality to convert between
     * different volume units by leveraging their equivalent values in cubic meters
     * as a base reference.
     *
     * @property measureUnit The `MeasureUnit` instance associated with the volume unit.
     * @since 1.0.0
     */
    enum class VolumeUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        CUBIC_METER(MeasureUnit("cubic meter", "volume", "m³", isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)),
		LITER(MeasureUnit("liter", "volume", "L", isAcceptedBySI = true, knownSymbol = true)),
		CUBIC_CENTIMETER(MeasureUnit("cubic centimeter", "volume", "cm³", knownSymbol = true)),
		CUBIC_INCH(MeasureUnit("cubic inch", "volume", "in³", knownSymbol = true)),
		CUBIC_FOOT(MeasureUnit("cubic foot", "volume", "ft³", knownSymbol = true)),
		CUBIC_YARD(MeasureUnit("cubic yard", "volume", "yd³", knownSymbol = true)),
		US_GALLON(MeasureUnit("US gallon", "volume", "gal")),
		IMPERIAL_GALLON(MeasureUnit("imperial gallon", "volume", "gal")),
		US_QUART(MeasureUnit("US quart", "volume", "qt")),
		IMPERIALI_QUART(MeasureUnit("imperial quart", "volume", "qt")),
		US_PINT(MeasureUnit("US pint", "volume", "pt")),
		IMPERIAL_PINT(MeasureUnit("imperial pint", "volume", "pt")),
		US_FLUID_OUNCE(MeasureUnit("US fluid ounce", "volume", "fl oz")),
		IMPERIAL_FLUID_OUNCE(MeasureUnit("imperial fluid ounce", "volume", "fl oz"));

        companion object {
            infix fun ofName(name: String): VolumeUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): VolumeUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<VolumeUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<VolumeUnit>
                get() = VolumeUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toCubicMeters(value: Double) = when (this) {
            CUBIC_METER -> value
            LITER -> value * 0.001
            CUBIC_CENTIMETER -> value * 1e-6
            CUBIC_INCH -> value * 0.0000163871
            CUBIC_FOOT -> value * 0.028316846592
            CUBIC_YARD -> value * 0.76455486927
            US_GALLON -> value * 0.003785411784
            IMPERIAL_GALLON -> value * 0.00454609
            US_QUART -> value * 0.000946352946
            IMPERIALI_QUART -> value * 0.00113652
            US_PINT -> value * 0.000473176473
            IMPERIAL_PINT -> value * 0.00056826125
            US_FLUID_OUNCE -> value * 2.95735295625e-5
            IMPERIAL_FLUID_OUNCE -> value * 2.84131e-5
        }

        private fun fromCubicMeters(value: Double) = when (this) {
            CUBIC_METER -> value
            LITER -> value / 0.001
            CUBIC_CENTIMETER -> value / 1e-6
            CUBIC_INCH -> value / 0.0000163871
            CUBIC_FOOT -> value / 0.028316846592
            CUBIC_YARD -> value / 0.76455486927
            US_GALLON -> value / 0.003785411784
            IMPERIAL_GALLON -> value / 0.00454609
            US_QUART -> value / 0.000946352946
            IMPERIALI_QUART -> value / 0.00113652
            US_PINT -> value / 0.000473176473
            IMPERIAL_PINT -> value / 0.00056826125
            US_FLUID_OUNCE -> value / 2.95735295625e-5
            IMPERIAL_FLUID_OUNCE -> value / 2.84131e-5
        }

        fun convertTo(value: Double, targetUnit: VolumeUnit) = targetUnit.fromCubicMeters(toCubicMeters(value))
    }

    /**
     * Represents a set of units for measuring speed, each associated with a corresponding `MeasureUnit`.
     *
     * Each `SpeedUnit` provides methods to handle conversions between other speed units, including
     * conversion to and from meters per second as the canonical unit of measurement.
     *
     * @property measureUnit The underlying `MeasureUnit` instance associated with the speed unit.
     * @since 1.0.0
     */
    enum class SpeedUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        METER_PER_SECOND(MeasureUnit("meter per second", "speed", "m/s", isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)),
		KILOMETER_PER_HOUR(MeasureUnit("kilometer per hour", "speed", "km/h", knownSymbol = true)),
		MILE_PER_HOUR(MeasureUnit("mile per hour", "speed", "mph", knownSymbol = true)),
		FOOT_PER_SECOND(MeasureUnit("foot per second", "speed", "ft/s", knownSymbol = true)),
		KNOT(MeasureUnit("knot", "speed", "kn", isAcceptedBySI = true, knownSymbol = true)),
		MACH(MeasureUnit("mach", "speed", "M", knownSymbol = true));
        
        companion object {
            infix fun ofName(name: String): SpeedUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): SpeedUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<SpeedUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<SpeedUnit>
                get() = SpeedUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toMetersPerSecond(value: Double) = when (this) {
            METER_PER_SECOND -> value
            KILOMETER_PER_HOUR -> value * (1 / 3.6)
            MILE_PER_HOUR -> value * 0.44704
            FOOT_PER_SECOND -> value * 0.3048
            KNOT -> value * 0.514444
            MACH -> value * 343.2
        }

        private fun fromMetersPerSecond(value: Double) = when (this) {
            METER_PER_SECOND -> value
            KILOMETER_PER_HOUR -> value / (1 / 3.6)
            MILE_PER_HOUR -> value / 0.44704
            FOOT_PER_SECOND -> value / 0.3048
            KNOT -> value / 0.514444
            MACH -> value / 343.2
        }

        fun convertTo(value: Double, targetUnit: SpeedUnit) = targetUnit.fromMetersPerSecond(toMetersPerSecond(value))
    }

    /**
     * Represents various units of acceleration and provides functionality for
     * conversion between these units. Each unit is defined with its respective
     * `MeasureUnit` properties, including its name, symbol, and whether it is an
     * International System (IS) unit or accepted by the IS.
     *
     * This enum implements the `ScalarUnit` interface, which allows access to
     * metadata associated with each acceleration unit, such as the measurement
     * category, unit name, and symbol.
     *
     * @property measureUnit The underlying `MeasureUnit` instance associated with the acceleration unit.
     * @since 1.0.0
     */
    enum class AccelerationUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        METER_PER_SECOND_SQUARED(MeasureUnit("meter per second squared", "acceleration", "m/s²", isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)),
		GAL(MeasureUnit("gal", "acceleration", "gal")),
		FOOT_PER_SECOND_SQUADRED(MeasureUnit("foot per second squared", "acceleration", "ft/s²", knownSymbol = true)),
		STANDARD_GRAVITY(MeasureUnit("standard_gravity", "acceleration", "g₀", knownSymbol = true));

        companion object {
            infix fun ofName(name: String): AccelerationUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): AccelerationUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<AccelerationUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<AccelerationUnit>
                get() = AccelerationUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toMetersPerSecondSquared(value: Double) = when (this) {
            METER_PER_SECOND_SQUARED -> value
            GAL -> value * 0.01
            FOOT_PER_SECOND_SQUADRED -> value * 0.3048006096
            STANDARD_GRAVITY -> value * 9.80665
        }

        private fun fromMetersPerSecondSquared(value: Double) = when (this) {
            METER_PER_SECOND_SQUARED -> value
            GAL -> value / 0.01
            FOOT_PER_SECOND_SQUADRED -> value / 0.3048006096
            STANDARD_GRAVITY -> value / 9.80665
        }

        fun convertTo(value: Double, targetUnit: AccelerationUnit) = targetUnit.fromMetersPerSecondSquared(toMetersPerSecondSquared(value))
    }

    /**
     * Enum class representing various units of density measurement.
     *
     * Each density unit is associated with a `MeasureUnit` instance that defines
     * its properties, including name, category, symbol, and IS unit status.
     *
     * This class allows converting between density units and provides methods to
     * retrieve density units by name or symbol. It also identifies units that
     * are accepted by the International System (IS).
     *
     * @property measureUnit The underlying `MeasureUnit` instance representing the density unit.
     * @since 1.0.0
     */
    enum class DensityUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        KILOGRAM_PER_CUBIC_METER(MeasureUnit("kilogram per cubic meter", "density", "kg/m³", isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)),
		GRAM_PER_CUBIC_CENTIMETER(MeasureUnit("gram per cubic centimeter", "density", "g/cm³", knownSymbol = true)),
		GRAM_PER_LITER(MeasureUnit("gram per liter", "density", "g/L", knownSymbol = true)),
		POUND_PER_CUBIC_FOOT(MeasureUnit("pound per cubic foot", "density", "lb/ft³")),
		POUND_PER_GALLON(MeasureUnit("pound per gallon", "density", "lb/gal")),
		OUNCE_PER_GALLON(MeasureUnit("ounce per gallon", "density", "oz/gal")),
		SLUG_PER_CUBIC_FOOT(MeasureUnit("slug per cubic foot", "density", "sl/ft³"));

        companion object {
            infix fun ofName(name: String): DensityUnit? = entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): DensityUnit? = entries.find { it.symbol == symbol }

            val acceptedBySI: Set<DensityUnit>
                get() = entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<DensityUnit>
                get() = DensityUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toKilogramsPerCubicMeter(value: Double) = when (this) {
            KILOGRAM_PER_CUBIC_METER, GRAM_PER_LITER -> value
            GRAM_PER_CUBIC_CENTIMETER -> value * 1000
            POUND_PER_CUBIC_FOOT -> value * 16.0185
            POUND_PER_GALLON -> value * 119.82643
            OUNCE_PER_GALLON -> value * 8.2166693
            SLUG_PER_CUBIC_FOOT -> value * 515.37882
        }

        private fun fromKilogramsPerCubicMeter(value: Double) = when (this) {
            KILOGRAM_PER_CUBIC_METER, GRAM_PER_LITER -> value
            GRAM_PER_CUBIC_CENTIMETER -> value / 1000
            POUND_PER_CUBIC_FOOT -> value / 16.0185
            POUND_PER_GALLON -> value / 119.82643
            OUNCE_PER_GALLON -> value / 8.2166693
            SLUG_PER_CUBIC_FOOT -> value / 515.37882
        }

        fun convertTo(value: Double, targetUnit: DensityUnit) = targetUnit.fromKilogramsPerCubicMeter(toKilogramsPerCubicMeter(value))
    }

    /**
     * Represents the unit of quantity of information. This enumeration contains commonly used
     * units such as bit, byte, nibble, and their respective multiples and submultiples
     * in both decimal (base 10) and binary (base 2) systems.
     *
     * Every unit is associated with a specific [MeasureUnit] that describes its name, category, and symbol.
     *
     * This enum also provides companion object utilities to retrieve a specific unit based on its name
     * or symbol and internal methods for unit conversion to and from bits for consistency.
     *
     * @constructor Accepts a [MeasureUnit] representing the underlying detailed attributes of the unit.
     *
     * @since 1.0.0
     */
    enum class DataSizeUnit(override val measureUnit: MeasureUnit) : ScalarUnit {
        BIT(MeasureUnit("bit", "quantity of information", "b", knownSymbol = true)),
        BYTE(MeasureUnit("byte", "quantity of information", "B", knownSymbol = true)),
        NIBBLE(MeasureUnit("nibble", "quantity of information", "nibble", knownSymbol = true)),

        // in that case, are reported also multiple and submultiple of the units, due to average use
        KILOBIT(MeasureUnit("kilobit", "data size", "kb", knownSymbol = true)),
        MEGABIT(MeasureUnit("megabit", "data size", "Mb", knownSymbol = true)),
        GIGABIT(MeasureUnit("gigabit", "data size", "Gb", knownSymbol = true)),
        TERABIT(MeasureUnit("terabit", "data size", "Tb", knownSymbol = true)),
        PETABIT(MeasureUnit("petabit", "data size", "Pb", knownSymbol = true)),
        EXABIT(MeasureUnit("exabit", "data size", "Eb", knownSymbol = true)),
        ZETTABIT(MeasureUnit("zettabit", "data size", "Zb", knownSymbol = true)),
        YOTTABIT(MeasureUnit("yottabit", "data size", "Yb", knownSymbol = true)),

        KIBIBIT(MeasureUnit("kibibit", "data size", "Kib", knownSymbol = true)),
        MEBIBIT(MeasureUnit("mebibit", "data size", "Mib", knownSymbol = true)),
        GIBIBIT(MeasureUnit("gibibit", "data size", "Gib", knownSymbol = true)),
        TEBIBIT(MeasureUnit("tebibit", "data size", "Tib", knownSymbol = true)),
        PEBIBIT(MeasureUnit("pebibit", "data size", "Pib", knownSymbol = true)),
        EXBIBIT(MeasureUnit("exbibit", "data size", "Eib", knownSymbol = true)),
        ZEBIBIT(MeasureUnit("zebibit", "data size", "Zib", knownSymbol = true)),
        YOBIBIT(MeasureUnit("yobibit", "data size", "Yib", knownSymbol = true)),

        KILOBYTE(MeasureUnit("kilobyte", "data size", "kB", knownSymbol = true)),
        MEGABYTE(MeasureUnit("megabyte", "data size", "MB", knownSymbol = true)),
        GIGABYTE(MeasureUnit("gigabyte", "data size", "GB", knownSymbol = true)),
        TERABYTE(MeasureUnit("terabyte", "data size", "TB", knownSymbol = true)),
        PETABYTE(MeasureUnit("petabyte", "data size", "PB", knownSymbol = true)),
        EXABYTE(MeasureUnit("exabyte", "data size", "EB", knownSymbol = true)),
        ZETTABYTE(MeasureUnit("zettabyte", "data size", "ZB", knownSymbol = true)),
        YOTTABYTE(MeasureUnit("yottabyte", "data size", "YB", knownSymbol = true)),
        RONNABYTE(MeasureUnit("ronnabyte", "data size", "RB", knownSymbol = true)),
        QUETTABYTE(MeasureUnit("quettabyte", "data size", "QB", knownSymbol = true)),

        KIBIBYTE(MeasureUnit("kibibyte", "data size", "KiB", knownSymbol = true)),
        MEBIBYTE(MeasureUnit("mebibyte", "data size", "MiB", knownSymbol = true)),
        GIBIBYTE(MeasureUnit("gibibyte", "data size", "GiB", knownSymbol = true)),
        TEBIBYTE(MeasureUnit("tebibyte", "data size", "TiB", knownSymbol = true)),
        PEBIBYTE(MeasureUnit("pebibyte", "data size", "PiB", knownSymbol = true)),
        EXBIBYTE(MeasureUnit("exbibyte", "data size", "EiB", knownSymbol = true)),
        ZEBIBYTE(MeasureUnit("zebibyte", "data size", "ZiB", knownSymbol = true)),
        YOBIBYTE(MeasureUnit("yobibyte", "data size", "YiB", knownSymbol = true));

        companion object {
            infix fun ofName(name: String): DataSizeUnit? = DataSizeUnit.entries.find { it.unitName == -name }
            infix fun ofSymbol(symbol: String): DataSizeUnit? = DataSizeUnit.entries.find { it.symbol == symbol }

            val acceptedBySI: Set<DataSizeUnit>
                get() = DataSizeUnit.entries.filter { it.isAcceptedBySI }.toSet()

            val knownSymbol: Set<DataSizeUnit>
                get() = DataSizeUnit.entries.filter { it.knownSymbol }.toSet()
        }

        private fun toBits(value: Double) = when (this) {
            BIT -> value
            BYTE -> value * 8
            NIBBLE -> value * 4
            KILOBIT -> value * 10.0.pow(3)
            MEGABIT -> value * 10.0.pow(6)
            GIGABIT -> value * 10.0.pow(9)
            TERABIT -> value * 10.0.pow(12)
            PETABIT -> value * 10.0.pow(15)
            EXABIT -> value * 10.0.pow(18)
            ZETTABIT -> value * 10.0.pow(21)
            YOTTABIT -> value * 10.0.pow(24)
            KIBIBIT -> value * 2.0.pow(10)
            MEBIBIT -> value * 2.0.pow(20)
            GIBIBIT -> value * 2.0.pow(30)
            TEBIBIT -> value * 2.0.pow(40)
            PEBIBIT -> value * 2.0.pow(50)
            EXBIBIT -> value * 2.0.pow(60)
            ZEBIBIT -> value * 2.0.pow(70)
            YOBIBIT -> value * 2.0.pow(80)
            KILOBYTE -> value * 8 * 10.0.pow(3)
            MEGABYTE -> value * 8 * 10.0.pow(6)
            GIGABYTE -> value * 8 * 10.0.pow(9)
            TERABYTE -> value * 8 * 10.0.pow(12)
            PETABYTE -> value * 8 * 10.0.pow(15)
            EXABYTE -> value * 8 * 10.0.pow(18)
            ZETTABYTE -> value * 8 * 10.0.pow(21)
            YOTTABYTE -> value * 8 * 10.0.pow(24)
            RONNABYTE -> value * 8 * 10.0.pow(27)
            QUETTABYTE -> value * 8 * 10.0.pow(30)
            KIBIBYTE -> value * 8 * 2.0.pow(10)
            MEBIBYTE -> value * 8 * 2.0.pow(20)
            GIBIBYTE -> value * 8 * 2.0.pow(30)
            TEBIBYTE -> value * 8 * 2.0.pow(40)
            PEBIBYTE -> value * 8 * 2.0.pow(50)
            EXBIBYTE -> value * 8 * 2.0.pow(60)
            ZEBIBYTE -> value * 8 * 2.0.pow(70)
            YOBIBYTE -> value * 8 * 2.0.pow(80)
        }

        private fun fromBits(value: Double) = when (this) {
            BIT -> value
            BYTE -> value / 8
            NIBBLE -> value / 4
            KILOBIT -> value / 10.0.pow(3)
            MEGABIT -> value / 10.0.pow(6)
            GIGABIT -> value / 10.0.pow(9)
            TERABIT -> value / 10.0.pow(12)
            PETABIT -> value / 10.0.pow(15)
            EXABIT -> value / 10.0.pow(18)
            ZETTABIT -> value / 10.0.pow(21)
            YOTTABIT -> value / 10.0.pow(24)
            KIBIBIT -> value / 2.0.pow(10)
            MEBIBIT -> value / 2.0.pow(20)
            GIBIBIT -> value / 2.0.pow(30)
            TEBIBIT -> value / 2.0.pow(40)
            PEBIBIT -> value / 2.0.pow(50)
            EXBIBIT -> value / 2.0.pow(60)
            ZEBIBIT -> value / 2.0.pow(70)
            YOBIBIT -> value / 2.0.pow(80)
            KILOBYTE -> (value / 10.0.pow(3)) / 8
            MEGABYTE -> (value / 10.0.pow(6)) / 8
            GIGABYTE -> (value / 10.0.pow(9)) / 8
            TERABYTE -> (value / 10.0.pow(12)) / 8
            PETABYTE -> (value / 10.0.pow(15)) / 8
            EXABYTE -> (value / 10.0.pow(18)) / 8
            ZETTABYTE -> (value / 10.0.pow(21)) / 8
            YOTTABYTE -> (value / 10.0.pow(24)) / 8
            RONNABYTE -> (value / 10.0.pow(27)) / 8
            QUETTABYTE -> (value / 10.0.pow(30)) / 8
            KIBIBYTE -> (value / 2.0.pow(10)) / 8
            MEBIBYTE -> (value / 2.0.pow(20)) / 8
            GIBIBYTE -> (value / 2.0.pow(30)) / 8
            TEBIBYTE -> (value / 2.0.pow(40)) / 8
            PEBIBYTE -> (value / 2.0.pow(50)) / 8
            EXBIBYTE -> (value / 2.0.pow(60)) / 8
            ZEBIBYTE -> (value / 2.0.pow(70)) / 8
            YOBIBYTE -> (value / 2.0.pow(80)) / 8
        }

        fun convertTo(value: Double, targetUnit: DataSizeUnit) = targetUnit.fromBits(toBits(value))
    }
}