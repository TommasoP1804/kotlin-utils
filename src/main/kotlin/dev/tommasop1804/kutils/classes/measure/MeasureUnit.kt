/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.measure

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
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
@MustUseReturnValues
open class MeasureUnit internal constructor(override val measure: String, override val unitName: String, override val isSIUnit: Boolean, override val isAcceptedBySI: Boolean, override val symbol: String?, override val knownSymbol: Boolean): ScalarUnit, Serializable {
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
            get() = TimeUnit.KNOWN_SYMBOLS
                .plus(LengthUnit.KNOWN_SYMBOLS)
                .plus(MassUnit.KNOWN_SYMBOLS)
                .plus(TemperatureUnit.KNOWN_SYMBOLS)
                .plus(PlaneAngleUnit.KNOWN_SYMBOLS)
                .plus(PressureUnit.KNOWN_SYMBOLS)
                .plus(EnergyUnit.KNOWN_SYMBOLS)
                .plus(PowerUnit.KNOWN_SYMBOLS)
                .plus(AreaUnit.KNOWN_SYMBOLS)
                .plus(VolumeUnit.KNOWN_SYMBOLS)
                .plus(SpeedUnit.KNOWN_SYMBOLS)
                .plus(AccelerationUnit.KNOWN_SYMBOLS)
                .plus(DensityUnit.KNOWN_SYMBOLS)
                .plus(DataSizeUnit.KNOWN_SYMBOLS)

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

        /**
         * Represents a set of scalar units that are derived from known unit constants.
         *
         * This property provides a collection of predefined scalar units, obtained by converting
         * `knownUnitsConsts` into a set. It serves as a reference for scalar units that are well-defined
         * and recognized within the scope of the `MeasureUnit` class.
         *
         * The backing field ensures that the returned set always reflects the current state
         * of `knownUnitsConsts`, providing a reliable and consistent snapshot of known scalar units.
         *
         * @return A set of scalar units derived from `knownUnitsConsts`.
         * @since 3.1.1
         */
        internal val knownUnitsScalar: Set<ScalarUnit>
            get() = knownUnitsConsts.toSet()

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
                if (value.symbol != null) gen.writePOJOProperty("symbol", value.symbol)
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
                if (value.symbol != null) gen.writeObjectField("symbol", value.symbol)
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
     * Provides the first component of a destructured object, which typically represents the `measure` property.
     * This function allows the object to be broken down into its individual components using destructuring declarations.
     *
     * @return The value of the `measure` property.
     * @since 3.1.0
     */
    operator fun component1() = measure
    /**
     * Retrieves the second component of a destructured pair or data object.
     *
     * This operator function is primarily used for enabling structured bindings
     * (destructuring declarations) in Kotlin, allowing access to the second
     * value defined by this component in the object.
     *
     * @return The second component, represented by `unitName`.
     * @since 3.1.0
     */
    operator fun component2() = unitName
    /**
     * Returns the third component of the instance using the destructuring declaration syntax.
     * Typically used to retrieve the `isSIUnit` property.
     *
     * @return The value of the `isSIUnit` property.
     * @since 3.1.0
     */
    operator fun component3() = isSIUnit
    /**
     * Returns the fourth component of the object when using destructuring declarations.
     * This operator function is typically used to provide an easier way to access a specific property
     * of the class in a destructured context.
     *
     * @return The value of the `isAcceptedBySI` property.
     * @since 3.1.0
     */
    operator fun component4() = isAcceptedBySI
    /**
     * Decomposes the object to provide the fifth component in a destructuring declaration.
     *
     * This operator function allows the usage of the object's properties within a destructuring declaration
     * by returning the value associated with the fifth component.
     *
     * @return The fifth component value, typically referred to as `symbol`.
     * @since 3.1.0
     */
    operator fun component5() = symbol
    /**
     * Provides the sixth component of a destructuring declaration.
     *
     * This operator function returns the value that corresponds to the sixth element
     * in a destructuring decomposition when the class supports it. Typically, this
     * function is used in conjunction with destructuring declarations to allow
     * retrieval of specific values from an object.
     *
     * @return The value corresponding to the sixth component of the destructuring.
     * @since 3.1.0
     */
    operator fun component6() = knownSymbol
    
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

    // ----------------

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class TimeUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInSeconds: Transformer<Number, Number>,
        @Transient private val unitFromSeconds: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "time", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForSeconds: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForSeconds.toDouble() }, { it.toDouble() / factorForSeconds.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)

        companion object {
            val NANOSECONDS = TimeUnit("nanoseconds", "ns", { it.toDouble() / 1e9 }, { it.toDouble() * 1e9 }, knownSymbol = true)
            val MILLISECONDS = TimeUnit("milliseconds", "ms", { it.toDouble() / 1e3 }, { it.toDouble() * 1e3 }, knownSymbol = true)
            val SECONDS = TimeUnit("seconds", "s", { it.toDouble() }, { it.toDouble() }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val MINUTES = TimeUnit("minutes", "min", { it.toDouble() * 60.0 }, { it.toDouble() / 60.0 }, isAcceptedBySI = true, knownSymbol = true)
            val HOURS = TimeUnit("hours", "h", { it.toDouble() * 3600.0 }, { it.toDouble() / 3600.0 }, isAcceptedBySI = true, knownSymbol = true)
            val DAYS = TimeUnit("days", "d", { it.toDouble() * 86400.0 }, { it.toDouble() / 86400.0 }, isAcceptedBySI = true, knownSymbol = true)
            val WEEKS = TimeUnit("weeks", "wk", { it.toDouble() * 604800.0 }, { it.toDouble() / 604800.0 }, knownSymbol = true)
            val MONTHS = TimeUnit("months", "mo", { it.toDouble() * 2592000.0 }, { it.toDouble() / 2592000.0 }, knownSymbol = true)
            val YEARS = TimeUnit("years", "yr", { it.toDouble() * 31536000.0 }, { it.toDouble() / 31536000.0 }, knownSymbol = true)

            internal val KNOWN_SYMBOLS = setOf(
                NANOSECONDS,
                MILLISECONDS,
                SECONDS,
                MINUTES,
                HOURS,
                DAYS,
                WEEKS,
                MONTHS,
                YEARS
            )
        }

        private fun toSeconds(value: Double) = unitInSeconds(value).toDouble()

        private fun fromSeconds(value: Double) = unitFromSeconds(value).toDouble()

        fun convertTo(value: Double, targetUnit: TimeUnit) = targetUnit.fromSeconds(toSeconds(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class LengthUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInMeters: Transformer<Number, Number>,
        @Transient private val unitFromMeters: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "length", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForMeters: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForMeters.toDouble() }, { it.toDouble() / factorForMeters.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)

        companion object {
            val METERS = LengthUnit("meters", "m", { it.toDouble() }, { it.toDouble() }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val KILOMETERS = LengthUnit("kilometers", "km", { it.toDouble() * 1000 }, { it.toDouble() / 1000 }, knownSymbol = true)
            val MILES = LengthUnit("miles", "mi", { it.toDouble() * 1609.34 }, { it.toDouble() / 1609.34 }, knownSymbol = true)
            val NAUTICAL_MILES = LengthUnit("nautical miles", "nm", { it.toDouble() * 1852 }, { it.toDouble() / 1852 }, isAcceptedBySI = true, knownSymbol = true)
            val FEET = LengthUnit("feet", "ft", { it.toDouble() * 0.3048 }, { it.toDouble() / 0.3048 }, knownSymbol = true)
            val INCHES = LengthUnit("inches", "in", { it.toDouble() * 0.0254 }, { it.toDouble() / 0.0254 }, knownSymbol = true)
            val YARDS = LengthUnit("yards", "yd", { it.toDouble() * 0.9144 }, { it.toDouble() / 0.9144 }, knownSymbol = true)
            val LIGHT_YEARS = LengthUnit("light years", "ly", { it.toDouble() * 9.4607e15 }, { it.toDouble() / 9.4607e15 }, knownSymbol = true)
            val ASTRONOMICAL_UNITS = LengthUnit("astronomical units", "au", { it.toDouble() * 1.496e11 }, { it.toDouble() / 1.496e11 }, isAcceptedBySI = true, knownSymbol = true)
            val ANGSTROMS = LengthUnit("ångströms", "Å", { it.toDouble() / 1e10 }, { it.toDouble() * 1e10 }, isAcceptedBySI = true, knownSymbol = true)

            internal val KNOWN_SYMBOLS = setOf(
                METERS,
                KILOMETERS,
                MILES,
                NAUTICAL_MILES,
                FEET,
                INCHES,
                YARDS,
                LIGHT_YEARS,
                ASTRONOMICAL_UNITS,
                ANGSTROMS
            )
        }

        private fun toMeters(value: Double) = unitInMeters(value).toDouble()

        private fun fromMeters(value: Double) = unitFromMeters(value).toDouble()

        fun convertTo(value: Double, targetUnit: LengthUnit) = targetUnit.fromMeters(toMeters(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class MassUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInKilograms: Transformer<Number, Number>,
        @Transient private val unitFromKilograms: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "mass", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForKilograms: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForKilograms.toDouble() }, { it.toDouble() / factorForKilograms.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)

        companion object {
            val KILOGRAMS = MassUnit("kilograms", "kg", { it.toDouble() }, { it.toDouble() }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val GRAMS = MassUnit("grams", "g", { it.toDouble() / 1000 }, { it.toDouble() * 1000 }, knownSymbol = true)
            val TONNES = MassUnit("tonnes", "t", { it.toDouble() * 1000 }, { it.toDouble() / 1000 }, isAcceptedBySI = true, knownSymbol = true)
            val POUNDS = MassUnit("pounds", "lb", { it.toDouble() * 0.45359237 }, { it.toDouble() / 0.45359237 }, knownSymbol = true)
            val OUNCES = MassUnit("ounces", "oz", { it.toDouble() * 0.028349523125 }, { it.toDouble() / 0.028349523125 }, knownSymbol = true)
            val STONES = MassUnit("stones", "st", { it.toDouble() * 6.35029318 }, { it.toDouble() / 6.35029318 }, knownSymbol = true)
            val CARATS = MassUnit("carats", "ct", { it.toDouble() * 0.002 }, { it.toDouble() / 0.002 }, knownSymbol = true)
            val SLUGS = MassUnit("slugs", unitInKilograms = { it.toDouble() * 14.5939 }, unitFromKilograms = { it.toDouble() / 14.5939 }, knownSymbol = true)
            val ATOMIC_MASS_UNITS = MassUnit("atomic mass units", "u", { it.toDouble() * 1.66053886e-27 }, { it.toDouble() / 1.66053886e-27 }, isAcceptedBySI = true, knownSymbol = true)

            internal val KNOWN_SYMBOLS = setOf(
                KILOGRAMS,
                GRAMS,
                TONNES,
                POUNDS,
                OUNCES,
                STONES,
                CARATS,
                SLUGS,
                ATOMIC_MASS_UNITS
            )
        }

        private fun toKilograms(value: Double) = unitInKilograms(value).toDouble()

        private fun fromKilograms(value: Double) = unitFromKilograms(value).toDouble()

        fun convertTo(value: Double, targetUnit: MassUnit) = targetUnit.fromKilograms(toKilograms(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class TemperatureUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInKelvins: Transformer<Number, Number>,
        @Transient private val unitFromKelvins: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "temperature", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForKelvins: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForKelvins.toDouble() }, { it.toDouble() / factorForKelvins.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)

        companion object {
            val KELVINS = TemperatureUnit("kelvins", "K", { it.toDouble() }, { it.toDouble() }, true, isAcceptedBySI = true, knownSymbol = true)
            val CELSIUS = TemperatureUnit("celsius", "°C", { it.toDouble() + 273.15 }, { it.toDouble() - 273.15 }, true, isAcceptedBySI = true, knownSymbol = true)
            val FAHRENHEIT = TemperatureUnit("fahrenheit", "°F", { (it.toDouble() - 32) * 5 / 9 + 273.15 }, { (it.toDouble() - 273.15) * 9 / 5 + 32 }, knownSymbol = true)

            internal val KNOWN_SYMBOLS = setOf(
                KELVINS,
                CELSIUS,
                FAHRENHEIT
            )
        }

        private fun toKelvins(value: Double) = unitInKelvins(value).toDouble()

        private fun fromKelvins(value: Double) = unitFromKelvins(value).toDouble()

        fun convertTo(value: Double, targetUnit: TemperatureUnit) = targetUnit.fromKelvins(toKelvins(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class PlaneAngleUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInRadians: Transformer<Number, Number>,
        @Transient private val unitFromRadians: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "plane angle", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForRadians: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForRadians.toDouble() }, { it.toDouble() / factorForRadians.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)


        companion object {
            val DEGREES_OF_ARC = PlaneAngleUnit("degrees of arc", "°", { Math.toRadians(it.toDouble()) }, { Math.toDegrees(it.toDouble()) }, isAcceptedBySI = true, knownSymbol = true)
            val MINUTES_OF_ARC = PlaneAngleUnit("minutes of arc", "'", { Math.toRadians(it.toDouble() / 60) }, { Math.toDegrees(it.toDouble() * 60) }, isAcceptedBySI = true, knownSymbol = true)
            val SECONDS_OF_ARC = PlaneAngleUnit("seconds of arc", "\"", { Math.toRadians(it.toDouble() / 3600) }, { Math.toDegrees(it.toDouble() * 3600) }, isAcceptedBySI = true, knownSymbol = true)
            val RADIANS = PlaneAngleUnit("radians", "rad", { it.toDouble() }, { it.toDouble() }, true, isAcceptedBySI = true, knownSymbol = true)
            val GRADIANS = PlaneAngleUnit("gradians", "gon", { it.toDouble() * (Math.PI / 200) }, { it.toDouble() * (200 / Math.PI) }, knownSymbol = true)

            internal val KNOWN_SYMBOLS = setOf(
                DEGREES_OF_ARC,
                MINUTES_OF_ARC,
                SECONDS_OF_ARC,
                RADIANS,
                GRADIANS
            )
        }

        private fun toRadians(value: Double) = unitInRadians(value).toDouble()

        private fun fromRadians(value: Double) = unitFromRadians(value).toDouble()

        fun convertTo(value: Double, targetUnit: PlaneAngleUnit) = targetUnit.fromRadians(toRadians(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class PressureUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInPascals: Transformer<Number, Number>,
        @Transient private val unitFromPascals: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "pressure", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForPascals: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForPascals.toDouble() }, { it.toDouble() / factorForPascals.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)


        companion object {
            val PASCALS = PressureUnit("pascals", "Pa", { it.toDouble() }, { it.toDouble() }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val BARS = PressureUnit("bars", "bar", { it.toDouble() * 100000 }, { it.toDouble() / 100000 }, isAcceptedBySI = true, knownSymbol = true)
            val ATMOSPHERES = PressureUnit("atmospheres", "atm", { it.toDouble() * 101325 }, { it.toDouble() / 101325 }, knownSymbol = true)
            val TECHNICAL_ATMOSPHERES = PressureUnit("technical atmospheres", "at", { it.toDouble() * 98066.5 }, { it.toDouble() / 98066.5 }, knownSymbol = true)
            val MILLIMTERS_OF_MERCURY = PressureUnit("millimeters of mercury", "mmHg", { it.toDouble() * 133.32236842105263 }, { it.toDouble() / 133.32236842105263 }, isAcceptedBySI = true, knownSymbol = true)
            val INCHES_OF_MERCURY = PressureUnit("inches of mercury", "inHg", { it.toDouble() * 3386.3886664184383 }, { it.toDouble() / 3386.3886664184383 }, isAcceptedBySI = true, knownSymbol = true)
            val POUNDS_PER_SQUARE_INCH = PressureUnit("pounds per square inch", "psi", { it.toDouble() * 6894.76 }, { it.toDouble() / 6894.76 }, knownSymbol = true)
            val TORRS = PressureUnit("torrs", "Torr", { (it.toDouble() * 101325) / 760 }, { (it.toDouble() * 760) / 101325 }, knownSymbol = true)

            internal val KNOWN_SYMBOLS = setOf(
                PASCALS,
                BARS,
                ATMOSPHERES,
                TECHNICAL_ATMOSPHERES,
                MILLIMTERS_OF_MERCURY,
                INCHES_OF_MERCURY,
                POUNDS_PER_SQUARE_INCH,
                TORRS
            )
        }

        private fun toPascals(value: Double) = unitInPascals(value).toDouble()

        private fun fromPascals(value: Double) = unitFromPascals(value).toDouble()

        fun convertTo(value: Double, targetUnit: PressureUnit) = targetUnit.fromPascals(toPascals(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class EnergyUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInJoules: Transformer<Number, Number>,
        @Transient private val unitFromJoules: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "energy", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForJoules: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForJoules.toDouble() }, { it.toDouble() / factorForJoules.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)


        companion object {
            val JOULES = EnergyUnit("joules", "J", { it.toDouble() }, { it.toDouble() }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val CALORIES = EnergyUnit("calories", "Cal", { it.toDouble() * 4184 }, { it.toDouble() / 4184 }, knownSymbol = true)
            val KILOCALORIES = EnergyUnit("kilocalories", "kCal", { it.toDouble() * 4184000 }, { it.toDouble() / 4184000 }, knownSymbol = true)
            val ELECTRONVOLTS = EnergyUnit("electronvolts", "eV", { it.toDouble() * 1.602176634e-19 }, { it.toDouble() / 1.602176634e-19 }, isAcceptedBySI = true, knownSymbol = true)
            val BRITISH_TERMAL_UNITS = EnergyUnit("British thermal units", "BTU", { it.toDouble() * 1055.06 }, { it.toDouble() / 1055.06 })
            val ERGS = EnergyUnit("ergs", "erg", { it.toDouble() * 1e-7 }, { it.toDouble() / 1e-7 })
            val FOOT_POUNDS_FORCE = EnergyUnit("foot-pounds force", "ft·lb", { it.toDouble() * 1.3558179483314003 }, { it.toDouble() / 1.3558179483314003 })
            val KILOWATT_HOURS = EnergyUnit("kilowatt hours", "kWh", { it.toDouble() * 3600000 }, { it.toDouble() / 3600000 })

            internal val KNOWN_SYMBOLS = setOf(
                JOULES,
                CALORIES,
                KILOCALORIES,
                ELECTRONVOLTS
            )
        }

        private fun toJoules(value: Double) = unitInJoules(value).toDouble()

        private fun fromJoules(value: Double) = unitFromJoules(value).toDouble()

        fun convertTo(value: Double, targetUnit: EnergyUnit) = targetUnit.fromJoules(toJoules(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class PowerUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInWatts: Transformer<Number, Number>,
        @Transient private val unitFromWatts: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "power", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForWatts: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForWatts.toDouble() }, { it.toDouble() / factorForWatts.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)

        companion object {
            val WATTS = PowerUnit("watts", "W", { it.toDouble() }, { it.toDouble() }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val HORSEPOWER = PowerUnit("horsepower", "HP", { it.toDouble() * 745.6998715822702 }, { it.toDouble() / 745.6998715822702 }, knownSymbol = true)
            val FOOT_POUNDS_PER_SECOND = PowerUnit("foot-pounds per second", "ft·lb/s", { it.toDouble() * 1.3558179483314003 }, { it.toDouble() / 1.3558179483314003 })
            val CALORIES_PER_SECOND = PowerUnit("calories per second", "Cal/s", { it.toDouble() * 4.184 }, { it.toDouble() / 4.184 })
            val BRITISH_TERMAL_UNITS_PER_HOUR = PowerUnit("British thermal units per hour", "BTU/h", { it.toDouble() * 0.293071 }, { it.toDouble() / 0.293071 })

            internal val KNOWN_SYMBOLS = setOf(
                WATTS,
                HORSEPOWER
            )
        }

        private fun toWatts(value: Double) = unitInWatts(value).toDouble()

        private fun fromWatts(value: Double) = unitFromWatts(value).toDouble()

        fun convertTo(value: Double, targetUnit: PowerUnit) = targetUnit.fromWatts(toWatts(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class AreaUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInSquareMeters: Transformer<Number, Number>,
        @Transient private val unitFromSquareMeters: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "area", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForSquareMeters: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForSquareMeters.toDouble() }, { it.toDouble() / factorForSquareMeters.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)


        companion object {
            val SQUARE_METERS = AreaUnit("square meters", "m²", { it.toDouble() }, { it.toDouble() }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val SQUARE_KILOMETERS = AreaUnit("square kilometers", "km²", { it.toDouble() * 1000000 }, { it.toDouble() / 1000000 }, knownSymbol = true)
            val ARES = AreaUnit("ares", "a", { it.toDouble() * 100 }, { it.toDouble() / 100 }, knownSymbol = true)
            val HECTARES = AreaUnit("hectares", "ha", { it.toDouble() * 10000 }, { it.toDouble() / 10000 }, isAcceptedBySI = true, knownSymbol = true)
            val SQUARE_FEET = AreaUnit("square feet", "ft²", { it.toDouble() * 0.09290304 }, { it.toDouble() / 0.09290304 }, knownSymbol = true)
            val SQUARE_INCHES = AreaUnit("square inches", "in²", { it.toDouble() * 0.00064516 }, { it.toDouble() / 0.00064516 }, knownSymbol = true)
            val SQUARE_YARDS = AreaUnit("square yards", "yd²", { it.toDouble() * 0.83612736 }, { it.toDouble() / 0.83612736 }, knownSymbol = true)
            val SQUARE_MILES = AreaUnit("square miles", "mi²", { it.toDouble() * 2589988.110336 }, { it.toDouble() / 2589988.110336 }, knownSymbol = true)
            val ACRES = AreaUnit("acres", "ac", { it.toDouble() * 4046.8564224 }, { it.toDouble() / 4046.8564224 }, knownSymbol = true)
            val BARNS = AreaUnit("barns", "b", { it.toDouble() * 1e-28 }, { it.toDouble() / 1e-28 }, isAcceptedBySI = true, knownSymbol = true)

            internal val KNOWN_SYMBOLS = setOf(
                SQUARE_METERS,
                SQUARE_KILOMETERS,
                ARES,
                HECTARES,
                SQUARE_FEET,
                SQUARE_INCHES,
                SQUARE_YARDS,
                SQUARE_MILES,
                ACRES,
                BARNS
            )
        }

        private fun toSquareMeters(value: Double) = unitInSquareMeters(value).toDouble()

        private fun fromSquareMeters(value: Double) = unitFromSquareMeters(value).toDouble()

        fun convertTo(value: Double, targetUnit: AreaUnit) = targetUnit.fromSquareMeters(toSquareMeters(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class VolumeUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInCubicMeters: Transformer<Number, Number>,
        @Transient private val unitFromCubicMeters: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "volume", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForCubicMeters: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForCubicMeters.toDouble() }, { it.toDouble() / factorForCubicMeters.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)

        companion object {
            val CUBIC_METERS = VolumeUnit("cubic meters", "m³", { it.toDouble() }, { it.toDouble() }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val LITERS = VolumeUnit("liters", "L",{ it.toDouble() * 0.001 }, { it.toDouble() / 0.001 }, isAcceptedBySI = true, knownSymbol = true)
            val CUBIC_CENTIMETERS = VolumeUnit("cubic centimeters", "cm³", { it.toDouble() * 1e-6 }, { it.toDouble() * 1e6 }, knownSymbol = true)
            val CUBIC_INCHES = VolumeUnit("cubic inches", "in³", { it.toDouble() * 0.0000163871 }, { it.toDouble() / 0.0000163871 }, knownSymbol = true)
            val CUBIC_FEET = VolumeUnit("cubic feet", "ft³", { it.toDouble() * 0.028316846592 }, { it.toDouble() / 0.028316846592 }, knownSymbol = true)
            val CUBIC_YARDS = VolumeUnit("cubic yards", "yd³", { it.toDouble() * 0.76455486927 }, { it.toDouble() / 0.76455486927 }, knownSymbol = true)
            val US_GALLONS = VolumeUnit("US gallons", "gal", { it.toDouble() * 0.003785411784 }, { it.toDouble() / 0.003785411784 })
            val IMPERIAL_GALLONS = VolumeUnit("imperial gallons", "gal", { it.toDouble() * 0.00454609 }, { it.toDouble() / 0.00454609 })
            val US_QUARTS = VolumeUnit("US quarts", "qt", { it.toDouble() * 0.000946352946 }, { it.toDouble() / 0.000946352946 })
            val IMPERIAL_QUARTS = VolumeUnit("imperial quarts", "qt", { it.toDouble() * 0.00113652 }, { it.toDouble() / 0.00113652 })
            val US_PINTS = VolumeUnit("US pints", "pt", { it.toDouble() * 0.000473176473 }, { it.toDouble() / 0.000473176473 })
            val IMPERIAL_PINTS = VolumeUnit("imperial pints", "pt", { it.toDouble() * 0.00056826125 }, { it.toDouble() / 0.00056826125 })
            val US_FLUID_OUNCES = VolumeUnit("US fluid ounces", "fl·oz", { it.toDouble() * 2.95735295625e-5 }, { it.toDouble() / 2.95735295625e-5 })
            val IMPERIAL_FLUID_OUNCES = VolumeUnit("imperial fluid ounces", "fl·oz", { it.toDouble() * 2.84131e-5 }, { it.toDouble() / 2.84131e-5 })

            internal val KNOWN_SYMBOLS = setOf(
                CUBIC_METERS,
                LITERS,
                CUBIC_CENTIMETERS,
                CUBIC_INCHES,
                CUBIC_FEET,
                CUBIC_YARDS
            )
        }

        private fun toCubicMeters(value: Double) = unitInCubicMeters(value).toDouble()

        private fun fromCubicMeters(value: Double) = unitFromCubicMeters(value).toDouble()

        fun convertTo(value: Double, targetUnit: VolumeUnit) = targetUnit.fromCubicMeters(toCubicMeters(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class SpeedUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInMetersPerSecond: Transformer<Number, Number>,
        @Transient private val unitFromMetersPerSecond: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "speed", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForMetersPerSecond: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForMetersPerSecond.toDouble() }, { it.toDouble() / factorForMetersPerSecond.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)

        companion object {
            val METERS_PER_SECOND = SpeedUnit("meters per second", "m/s", { it.toDouble() }, { it.toDouble() }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val KILOMETERS_PER_HOUR = SpeedUnit("kilometers per hour", "km/h", { it.toDouble() * (1 / 3.6) }, { it.toDouble() * 3.6 }, knownSymbol = true)
            val MILES_PER_HOUR = SpeedUnit("miles per hour", "mph", { it.toDouble() * 0.44704 }, { it.toDouble() / 0.44704 }, knownSymbol = true)
            val FEET_PER_SECOND = SpeedUnit("feet per second", "ft/s", { it.toDouble() * 0.3048 }, { it.toDouble() / 0.3048 }, knownSymbol = true)
            val KNOTS = SpeedUnit("knots", "kn", { it.toDouble() * 0.514444 }, { it.toDouble() / 0.514444 }, isAcceptedBySI = true, knownSymbol = true)
            val MACH = SpeedUnit("mach", "mach", { it.toDouble() * 343.2 }, { it.toDouble() / 343.2 }, knownSymbol = true)

            internal val KNOWN_SYMBOLS = setOf(
                METERS_PER_SECOND,
                KILOMETERS_PER_HOUR,
                MILES_PER_HOUR,
                FEET_PER_SECOND,
                KNOTS,
                MACH
            )
        }

        private fun toMetersPerSecond(value: Double) = unitInMetersPerSecond(value).toDouble()

        private fun fromMetersPerSecond(value: Double) = unitFromMetersPerSecond(value).toDouble()

        fun convertTo(value: Double, targetUnit: SpeedUnit) = targetUnit.fromMetersPerSecond(toMetersPerSecond(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class AccelerationUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInMetersPerSecondSquared: Transformer<Number, Number>,
        @Transient private val unitFromMetersPerSecondSquared: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "acceleration", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForMetersPerSecondSquared: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForMetersPerSecondSquared.toDouble() }, { it.toDouble() / factorForMetersPerSecondSquared.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)

        companion object {
            val METERS_PER_SECOND_SQUARED = AccelerationUnit("meters per second squared", "m/s²", { it.toDouble() }, { it.toDouble() }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val KILOMETERS_PER_HOUR_SQUARED = AccelerationUnit("kilometers per hour squared", "km/h²", { it.toDouble() / 12960 }, { it.toDouble() * 12960 }, knownSymbol = true)
            val MILES_PER_HOUR_SQUARED = AccelerationUnit("miles per hour squared", "mph²", { it.toDouble() * 0.000124228 }, { it.toDouble() / 0.000124228 }, knownSymbol = true)
            val MACH_PER_SECOND_SQUARED = AccelerationUnit("mach per second squared", "mach/s²", { it.toDouble() * 340.29 }, { it.toDouble() / 340.29 }, knownSymbol = true)
            val GALILEOS = AccelerationUnit("galileos", "gal", { it.toDouble() * 0.01 }, { it.toDouble() / 0.01 }, knownSymbol = true)
            val FEET_PER_SECOND_SQUARED = AccelerationUnit("feet per second squared", "ft/s²", { it.toDouble() * 0.3048006096 }, { it.toDouble() / 0.3048006096 }, knownSymbol = true)
            val STANDARD_GRAVITY = AccelerationUnit("standard gravity", "g", { it.toDouble() * 9.80665 }, { it.toDouble() / 9.80665 }, isAcceptedBySI = true, knownSymbol = true)

            internal val KNOWN_SYMBOLS = setOf(
                METERS_PER_SECOND_SQUARED,
                KILOMETERS_PER_HOUR_SQUARED,
                MILES_PER_HOUR_SQUARED,
                MACH_PER_SECOND_SQUARED,
                GALILEOS,
                FEET_PER_SECOND_SQUARED,
                STANDARD_GRAVITY
            )
        }

        private fun toMetersPerSecondSquared(value: Double) = unitInMetersPerSecondSquared(value).toDouble()

        private fun fromMetersPerSecondSquared(value: Double) = unitFromMetersPerSecondSquared(value).toDouble()

        fun convertTo(value: Double, targetUnit: AccelerationUnit) = targetUnit.fromMetersPerSecondSquared(toMetersPerSecondSquared(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class DensityUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInKilogramsPerCubicMeter: Transformer<Number, Number>,
        @Transient private val unitFromKilogramsPerCubicMeter: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "density", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForKilogramsPerCubicMeter: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForKilogramsPerCubicMeter.toDouble() }, { it.toDouble() / factorForKilogramsPerCubicMeter.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)

        companion object {
            val KILOGRAMS_PER_CUBIC_METER = DensityUnit("kilograms per cubic meter", "kg/m³", { it }, { it }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val GRAMS_PER_CUBIC_CENTIMETER = DensityUnit("grams per cubic centimeter", "g/cm³", { it.toDouble() * 1000 }, { it.toDouble() / 1000 }, knownSymbol = true)
            val GRAMS_PER_LITER = DensityUnit("grams per liter", "g/L", { it }, { it }, knownSymbol = true)
            val POUNDS_PER_CUBIC_FOOT = DensityUnit("pounds per cubic foot", "lb/ft³", { it.toDouble() * 16.0185 }, { it.toDouble() / 16.0185 })
            val POUNDS_PER_GALLON = DensityUnit("pounds per gallon", "lb/gal", { it.toDouble() * 119.82643 }, { it.toDouble() / 119.82643 })
            val OUNCES_PER_GALLON = DensityUnit("ounces per gallon", "oz/gal", { it.toDouble() * 8.2166693 }, { it.toDouble() / 8.2166693 })
            val SLUGS_PER_CUBIC_FOOT = DensityUnit("slugs per cubic foot", "sl/ft³", { it.toDouble() * 515.37882 }, { it.toDouble() / 515.37882 })

            internal val KNOWN_SYMBOLS = setOf(
                KILOGRAMS_PER_CUBIC_METER,
                GRAMS_PER_CUBIC_CENTIMETER,
                GRAMS_PER_LITER
            )
        }

        private fun toKilogramsPerCubicMeter(value: Double) = unitInKilogramsPerCubicMeter(value).toDouble()

        private fun fromKilogramsPerCubicMeter(value: Double) = unitFromKilogramsPerCubicMeter(value).toDouble()

        fun convertTo(value: Double, targetUnit: DensityUnit) = targetUnit.fromKilogramsPerCubicMeter(toKilogramsPerCubicMeter(value))
    }

    @JsonSerialize(using = Serializer::class)
    @JsonDeserialize(using = Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OldDeserializer::class)
    class DataSizeUnit(
        unitName: String,
        symbol: String? = null,
        @Transient private val unitInBits: Transformer<Number, Number>,
        @Transient private val unitFromBits: Transformer<Number, Number>,
        isSIUnit: Boolean = false,
        isAcceptedBySI: Boolean = false,
        knownSymbol: Boolean = false
    ) : MeasureUnit(unitName, "data size", symbol, isSIUnit, isAcceptedBySI, knownSymbol) {

        constructor(
            unitName: String,
            symbol: String? = null,
            factorForBits: Number,
            isSIUnit: Boolean = false,
            isAcceptedBySI: Boolean = false,
            knownSymbol: Boolean = false
        ) : this(unitName, symbol, { it.toDouble() * factorForBits.toDouble() }, { it.toDouble() / factorForBits.toDouble() }, isSIUnit, isAcceptedBySI, knownSymbol)

        companion object {
            val BITS = DataSizeUnit("bits", "b", { it }, { it }, isSIUnit = true, isAcceptedBySI = true, knownSymbol = true)
            val BYTES = DataSizeUnit("bytes", "B", { it.toDouble() * 8 }, { it.toDouble() / 8 }, knownSymbol = true)
            val NIBBLES = DataSizeUnit("nibbles", "nb", { it.toDouble() * 4 }, { it.toDouble() / 4 }, knownSymbol = true)

            val KILOBITS = DataSizeUnit("kilobits", "kb", { it.toDouble() * 10.0.pow(3) }, { it.toDouble() / 10.0.pow(3) }, knownSymbol = true)
            val MEGABITS = DataSizeUnit("megabits", "Mb", { it.toDouble() * 10.0.pow(6) }, { it.toDouble() / 10.0.pow(6) }, knownSymbol = true)
            val GIGABITS = DataSizeUnit("gigabits", "Gb", { it.toDouble() * 10.0.pow(9) }, { it.toDouble() / 10.0.pow(9) }, knownSymbol = true)
            val TERABITS = DataSizeUnit("terabits", "Tb", { it.toDouble() * 10.0.pow(12) }, { it.toDouble() / 10.0.pow(12) }, knownSymbol = true)
            val PETABITS = DataSizeUnit("petabits", "Pb", { it.toDouble() * 10.0.pow(15) }, { it.toDouble() / 10.0.pow(15) }, knownSymbol = true)
            val EXABITS = DataSizeUnit("exabits", "Eb", { it.toDouble() * 10.0.pow(18) }, { it.toDouble() / 10.0.pow(18) }, knownSymbol = true)
            val ZETTABITS = DataSizeUnit("zettabits", "Zb", { it.toDouble() * 10.0.pow(21) }, { it.toDouble() / 10.0.pow(21) }, knownSymbol = true)
            val YOTTABITS = DataSizeUnit("yottabits", "Yb", { it.toDouble() * 10.0.pow(24) }, { it.toDouble() / 10.0.pow(24) }, knownSymbol = true)

            val KIBIBITS = DataSizeUnit("kibibits", "Kib", { it.toDouble() * 2.0.pow(10) }, { it.toDouble() / 2.0.pow(10) }, knownSymbol = true)
            val MEBIBITS = DataSizeUnit("mebibits", "Mib", { it.toDouble() * 2.0.pow(20) }, { it.toDouble() / 2.0.pow(20) }, knownSymbol = true)
            val GIBIBITS = DataSizeUnit("gibibits", "Gib", { it.toDouble() * 2.0.pow(30) }, { it.toDouble() / 2.0.pow(30) }, knownSymbol = true)
            val TEBIBITS = DataSizeUnit("tebibits", "Tib", { it.toDouble() * 2.0.pow(40) }, { it.toDouble() / 2.0.pow(40) }, knownSymbol = true)
            val PEBIBITS = DataSizeUnit("pebibits", "Pib", { it.toDouble() * 2.0.pow(50) }, { it.toDouble() / 2.0.pow(50) }, knownSymbol = true)
            val EXBIBITS = DataSizeUnit("exbibits", "Eib", { it.toDouble() * 2.0.pow(60) }, { it.toDouble() / 2.0.pow(60) }, knownSymbol = true)
            val ZEBIBITS = DataSizeUnit("zebibits", "Zib", { it.toDouble() * 2.0.pow(70) }, { it.toDouble() / 2.0.pow(70) }, knownSymbol = true)
            val YOBIBITS = DataSizeUnit("yobibits", "Yib", { it.toDouble() * 2.0.pow(80) }, { it.toDouble() / 2.0.pow(80) }, knownSymbol = true)

            val KILOBYTES = DataSizeUnit("kilobytes", "kB", { it.toDouble() * 8 * 10.0.pow(3) }, { it.toDouble() / 8 / 10.0.pow(3) }, knownSymbol = true)
            val MEGABYTES = DataSizeUnit("megabytes", "MB", { it.toDouble() * 8 * 10.0.pow(6) }, { it.toDouble() / 8 / 10.0.pow(6) }, knownSymbol = true)
            val GIGABYTES = DataSizeUnit("gigabytes", "GB", { it.toDouble() * 8 * 10.0.pow(9) }, { it.toDouble() / 8 / 10.0.pow(9) }, knownSymbol = true)
            val TERABYTES = DataSizeUnit("terabytes", "TB", { it.toDouble() * 8 * 10.0.pow(12) }, { it.toDouble() / 8 / 10.0.pow(12) }, knownSymbol = true)
            val PETABYTES = DataSizeUnit("petabytes", "PB", { it.toDouble() * 8 * 10.0.pow(15) }, { it.toDouble() / 8 / 10.0.pow(15) }, knownSymbol = true)
            val EXABYTES = DataSizeUnit("exabytes", "EB", { it.toDouble() * 8 * 10.0.pow(18) }, { it.toDouble() / 8 / 10.0.pow(18) }, knownSymbol = true)
            val ZETTABYTES = DataSizeUnit("zettabytes", "ZB", { it.toDouble() * 8 * 10.0.pow(21) }, { it.toDouble() / 8 / 10.0.pow(21) }, knownSymbol = true)
            val YOTTABYTES = DataSizeUnit("yottabytes", "YB", { it.toDouble() * 8 * 10.0.pow(24) }, { it.toDouble() / 8 / 10.0.pow(24) }, knownSymbol = true)
            val RONNABYTES = DataSizeUnit("ronnabytes", "RB", { it.toDouble() * 8 * 10.0.pow(27) }, { it.toDouble() / 8 / 10.0.pow(27) }, knownSymbol = true)
            val QUETTABYTES = DataSizeUnit("quettabytes", "QB", { it.toDouble() * 8 * 10.0.pow(30) }, { it.toDouble() / 8 / 10.0.pow(30) }, knownSymbol = true)

            val KIBIBYTES = DataSizeUnit("kibibytes", "KiB", { it.toDouble() * 8 * 2.0.pow(10) }, { it.toDouble() / 8 / 2.0.pow(10) }, knownSymbol = true)
            val MEBIBYTES = DataSizeUnit("mebibytes", "MiB", { it.toDouble() * 8 * 2.0.pow(20) }, { it.toDouble() / 8 / 2.0.pow(20) }, knownSymbol = true)
            val GIBIBYTES = DataSizeUnit("gibibytes", "GiB", { it.toDouble() * 8 * 2.0.pow(30) }, { it.toDouble() / 8 / 2.0.pow(30) }, knownSymbol = true)
            val TEBIBYTES = DataSizeUnit("tebibytes", "TiB", { it.toDouble() * 8 * 2.0.pow(40) }, { it.toDouble() / 8 / 2.0.pow(40) }, knownSymbol = true)
            val PEBIBYTES = DataSizeUnit("pebibytes", "PiB", { it.toDouble() * 8 * 2.0.pow(50) }, { it.toDouble() / 8 / 2.0.pow(50) }, knownSymbol = true)
            val EXBIBYTES = DataSizeUnit("exbibytes", "EiB", { it.toDouble() * 8 * 2.0.pow(60) }, { it.toDouble() / 8 / 2.0.pow(60) }, knownSymbol = true)
            val ZEBIBYTES = DataSizeUnit("zebibytes", "ZiB", { it.toDouble() * 8 * 2.0.pow(70) }, { it.toDouble() / 8 / 2.0.pow(70) }, knownSymbol = true)
            val YOBIBYTES = DataSizeUnit("yobibytes", "YiB", { it.toDouble() * 8 * 2.0.pow(80) }, { it.toDouble() / 8 / 2.0.pow(80) }, knownSymbol = true)

            val BIT_DECIMAL = listOf(
                BITS,
                KILOBITS,
                MEGABITS,
                GIGABITS,
                TERABITS,
                PETABITS,
                EXABITS,
                ZETTABITS,
                YOTTABITS
            )
            val BIT_BINARY = listOf(
                BITS,
                KIBIBITS,
                MEBIBITS,
                GIBIBITS,
                TEBIBITS,
                PEBIBITS,
                EXBIBITS,
                ZEBIBITS,
                YOBIBITS
            )
            val BYTE_DECIMAL = listOf(
                BYTES,
                KILOBYTES,
                MEGABYTES,
                GIGABYTES,
                TERABYTES,
                PETABYTES,
                EXABYTES,
                ZETTABYTES,
                YOTTABYTES,
                RONNABYTES,
                QUETTABYTES
            )
            val BYTE_BINARY = listOf(
                BYTES,
                KIBIBYTES,
                MEBIBYTES,
                GIBIBYTES,
                TEBIBYTES,
                PEBIBYTES,
                EXBIBYTES,
                ZEBIBYTES,
                YOBIBYTES
            )

            internal val KNOWN_SYMBOLS = setOf(
                BITS,
                BYTES,
                NIBBLES,
                KILOBITS,
                MEGABITS,
                GIGABITS,
                TERABITS,
                PETABITS,
                EXABITS,
                ZETTABITS,
                YOTTABITS,
                KIBIBITS,
                MEBIBITS,
                GIBIBITS,
                TEBIBITS,
                PEBIBITS,
                EXBIBITS,
                ZEBIBITS,
                YOBIBITS,
                KILOBYTES,
                MEGABYTES,
                GIGABYTES,
                TERABYTES,
                PETABYTES,
                EXABYTES,
                ZETTABYTES,
                YOTTABYTES,
                RONNABYTES,
                QUETTABYTES,
                KIBIBYTES,
                MEBIBYTES,
                GIBIBYTES,
                TEBIBYTES,
                PEBIBYTES,
                EXBIBYTES,
                ZEBIBYTES,
                YOBIBYTES
            )
        }

        internal fun toBits(value: Double) = unitInBits(value).toDouble()

        internal fun fromBits(value: Double) = unitFromBits(value).toDouble()

        fun convertTo(value: Double, targetUnit: DataSizeUnit) = targetUnit.fromBits(toBits(value))
    }
}