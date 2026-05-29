/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.measure

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Represents a measure of physical quantities, detailing its classification as an SI (International System of Units) base unit,
 * associated unit class, and corresponding SI unit representation.
 *
 * @property si Boolean indicating whether the measure is a SI base unit.
 * @property siBase Boolean indicating whether the measure is a base unit in the International System of Units (SI).
 * @property unitClass The class of the associated scalar unit that defines the measure, or `null` if not applicable.
 * @property defaultUnit The scalar unit representing the SI unit of the measure, or (for the non-SI) the standard unit.
 * @since 1.0.0
 */
@Suppress("unused")
enum class Measure(
	val si: Boolean,
	val siBase: Boolean,
	val unitClass: KClass<out ScalarUnit>?,
	val defaultUnit: ScalarUnit?
) {
    Time(true, true, MeasureUnit.TimeUnit::class, MeasureUnit.TimeUnit.SECONDS),
	Length(true, true, MeasureUnit.LengthUnit::class, MeasureUnit.LengthUnit.METERS),
	Mass(true, true, MeasureUnit.MassUnit::class, MeasureUnit.MassUnit.KILOGRAMS),
	ElectricCurrent(true, true, null, MeasureUnit("ampere", "electric current", "A", isSIUnit = true, isAcceptedBySI = true)),
	Temperature(true, true, MeasureUnit.TemperatureUnit::class, MeasureUnit.TemperatureUnit.KELVINS),
	AmountOfSubstance(true, true, null, MeasureUnit("mole", "amount of substance", "mol", isSIUnit = true, isAcceptedBySI = true)),
	LuminousIntensity(true, true, null, MeasureUnit("candela", "luminous intensity", "cd", isSIUnit = true, isAcceptedBySI = true)),

	PlaneAngle(true, false, MeasureUnit.PlaneAngleUnit::class, MeasureUnit.PlaneAngleUnit.RADIANS),
	SolidAngle(true, false, null, MeasureUnit("steradian", "solid angle", "sr", isSIUnit = true, isAcceptedBySI = true)),
	Frequency(true, false, null, MeasureUnit("hertz", "frequency", "Hz", isSIUnit = true, isAcceptedBySI = true)),
	Force(true, false, null, MeasureUnit("newton", "force", "N", isSIUnit = true, isAcceptedBySI = true)),
	Pressure(true, false, MeasureUnit.PressureUnit::class, MeasureUnit.PressureUnit.PASCALS),
	Energy(true, false, MeasureUnit.EnergyUnit::class, MeasureUnit.EnergyUnit.JOULES),
	Power(true, false, MeasureUnit.PowerUnit::class, MeasureUnit.PowerUnit.WATTS),
	DyniamicViscosity(true, false, null, MeasureUnit("poiseuille", "dynamic viscosity", "μ", isSIUnit = true, isAcceptedBySI = true)),
	ElectricCharge(true, false, null, MeasureUnit("coulomb", "electric charge", "C", isSIUnit = true, isAcceptedBySI = true)),
	ElectricPotential(true, false, null, MeasureUnit("volt", "electric potential", "V", isSIUnit = true, isAcceptedBySI = true)),
	ElectricCapacitance(true, false, null, MeasureUnit("farad", "electric capacitance", "F", isSIUnit = true, isAcceptedBySI = true)),
	ElectricResistance(true, false, null, MeasureUnit("ohm", "electric resistance", "Ω", isSIUnit = true, isAcceptedBySI = true)),
	ElectricConductance(true, false, null, MeasureUnit("siemens", "electric conductance", "S", isSIUnit = true, isAcceptedBySI = true)),
	MagneticFluxDensity(true, false, null, MeasureUnit("tesla", "magnetic flux density", "T", isSIUnit = true, isAcceptedBySI = true)),
	MagneticFlux(true, false, null, MeasureUnit("weber", "magnetic flux", "Wb", isSIUnit = true, isAcceptedBySI = true)),
	Inductance(true, false, null, MeasureUnit("henry", "inductance", "H", isSIUnit = true, isAcceptedBySI = true)),
	LuminousFlux(true, false, null, MeasureUnit("lumen", "luminous flux", "lm", isSIUnit = true, isAcceptedBySI = true)),
	Illuminance(true, false, null, MeasureUnit("lux", "illuminance", "lx", isSIUnit = true, isAcceptedBySI = true)),
	AbsorbedDose(true, false, null, MeasureUnit("gray", "absorbed dose", "Gy", isSIUnit = true, isAcceptedBySI = true)),
	DoseEquivalent(true, false, null, MeasureUnit("sievert", "dose equivalent", "Sv", isSIUnit = true, isAcceptedBySI = true)),
	CatalyticActivity(true, false, null, MeasureUnit("katal", "catalytic activity", "kat", isSIUnit = true, isAcceptedBySI = true)),

	Area(true, false, MeasureUnit.AreaUnit::class, MeasureUnit.AreaUnit.SQUARE_METERS),
	Volume(true, false, MeasureUnit.VolumeUnit::class, MeasureUnit.VolumeUnit.CUBIC_METERS),
	Speed(true, false, MeasureUnit.SpeedUnit::class, MeasureUnit.SpeedUnit.METERS_PER_SECOND),
	Acceleration(true, false, MeasureUnit.AccelerationUnit::class, MeasureUnit.AccelerationUnit.METERS_PER_SECOND_SQUARED),
	AngularVelocity(true, false, null, MeasureUnit("radian per second", "angular velocity", "rpm", isSIUnit = true, isAcceptedBySI = true)),
	AngularAcceleration(true, false, null, MeasureUnit("radian per second squared", "angular acceleration", "rpm²", isSIUnit = true, isAcceptedBySI = true)),
	Density(true, false, MeasureUnit.DensityUnit::class, MeasureUnit.DensityUnit.KILOGRAMS_PER_CUBIC_METER),
	Molarity(true, false, null, MeasureUnit("mole per kilogram", "molarity", "mol/kg", isSIUnit = true, isAcceptedBySI = true)),
	MolarVolume(true, false, null, MeasureUnit("cubic meter per mole", "molar volume", "m³/mol", isSIUnit = true, isAcceptedBySI = true)),

	MomentOfForce(true, false, null, MeasureUnit("newton-meter", "moment of force", "N·m", isSIUnit = true, isAcceptedBySI = true)),
	SurfaceTension(true, false, null, MeasureUnit("newton per meter", "surface tension", "N/m", isSIUnit = true, isAcceptedBySI = true)),
	Entropy(true, false, null, MeasureUnit("joule per kelvin", "entropy", "J/K", isSIUnit = true, isAcceptedBySI = true)),
	HeatCapacity(true, false, null, MeasureUnit("joule per kelvin", "heat capacity", "J/K", isSIUnit = true, isAcceptedBySI = true)),
	SpecificHeatCapacity(true, false, null, MeasureUnit("joule per kilogram-kelvin", "specific heat capacity", "J/(kg·K)", isSIUnit = true, isAcceptedBySI = true)),
	SpecificEntropy(true, false, null, MeasureUnit("joule per kilogram-kelvin", "specific entropy", "J/(kg·K)", isSIUnit = true, isAcceptedBySI = true)),
	SpecificEnergy(true, false, null, MeasureUnit("joule per kilogram", "specific energy", "J/kg", isSIUnit = true, isAcceptedBySI = true)),
	ThermalConductivity(true, false, null, MeasureUnit("watt per metre-kelvin", "thermal conductivity", "W/(m·K)", isSIUnit = true, isAcceptedBySI = true)),
	EnergyDensity(true, false, null, MeasureUnit("joule per cubic metre", "energy density", "J/m³", isSIUnit = true, isAcceptedBySI = true)),
	ElectricFieldStrength(true, false, null, MeasureUnit("volt per metre", "electric field strength", "V/m", isSIUnit = true, isAcceptedBySI = true)),
	ElectricChargeDensity(true, false, null, MeasureUnit("coulomb per cubic metre", "electric charge density", "C/m³", isSIUnit = true, isAcceptedBySI = true)),
	SurfaceChargeDensity(true, false, null, MeasureUnit("coulomb per square metre", "surface charge density", "C/m²", isSIUnit = true, isAcceptedBySI = true)),
	ElectricFluxDensity(true, false, null, MeasureUnit("coulomb per square metre", "electric flux density", "C/m²", isSIUnit = true, isAcceptedBySI = true)),
	ElectricDisplacement(true, false, null, MeasureUnit("coulomb per square metre", "electric displacement", "C/m²", isSIUnit = true, isAcceptedBySI = true)),
	Permittivity(true, false, null, MeasureUnit("farad per metre", "permittivity", "F/m", isSIUnit = true, isAcceptedBySI = true)),
	Permeability(true, false, null, MeasureUnit("henry per metre", "permeability", "H/m", isSIUnit = true, isAcceptedBySI = true)),
	MolarEnergy(true, false, null, MeasureUnit("joule per mole", "molar energy", "J/mol", isSIUnit = true, isAcceptedBySI = true)),
	MolarEntropy(true, false, null, MeasureUnit("joule per mole-kelvin", "molar entropy", "J/(mol·K)", isSIUnit = true, isAcceptedBySI = true)),
	MolarHeatCapacity(true, false, null, MeasureUnit("joule per mole-kelvin", "molar heat capacity", "J/(mol·K)", isSIUnit = true, isAcceptedBySI = true)),
	Exposure(true, false, null, MeasureUnit("coulomb per kilogram", "exposure", "C/kg", isSIUnit = true, isAcceptedBySI = true)),
	AbsorbedDoseRate(true, false, null, MeasureUnit("gray per second", "absorbed dose rate", "Gy/s", isSIUnit = true, isAcceptedBySI = true)),
	RadiantIntensity(true, false, null, MeasureUnit("watt per steradian", "radiant intensity", "W/sr", isSIUnit = true, isAcceptedBySI = true)),
	Radiance(true, false, null, MeasureUnit("watt per square metre-steradian", "radiance", "W/(m²·sr)", isSIUnit = true, isAcceptedBySI = true)),
	CatalyticActivityConcentration(true, false, null, MeasureUnit("katal per cubic metre", "catalytic activity concentration", "kat/m³", isSIUnit = true, isAcceptedBySI = true)),

	Wavenumber(true, false, null, MeasureUnit("reciprocal metre", "wavenumber", "1/m")),
	Vergence(true, false, null, MeasureUnit("reciprocal metre", "vergence", "1/m")),
	SurfaceDensity(true, false, null, MeasureUnit("kilogram per square metre", "surface density", "kg/m²")),
	SpecificVolume(true, false, null, MeasureUnit("cubic metre per kilogram", "specific volume", "m³/kg")),
	CurrentDensity(true, false, null, MeasureUnit("ampere per square metre", "current density", "A/m²")),
	MagneticFieldStrenght(true, false, null, MeasureUnit("ampere per metre", "magnetic field strength", "A/m")),
	Concentration(true, false, null, MeasureUnit("mole per cubic metre", "concentration", "mol/m³")),
	MassConcentration(true, false, null, MeasureUnit("kilogram per cubic metre", "mass concentration", "kg/m³")),
	Luminance(true, false, null, MeasureUnit("candela per square metre", "luminance", "cd/m²")),

	// NOT SI
	DataSize(false, false, MeasureUnit.DataSizeUnit::class, MeasureUnit.DataSizeUnit.BITS);

	companion object {
		/**
		 * Filters and retrieves a set of entries where the `SI` property is `true`.
		 *
		 * This function processes the entries of the `Measure` class, identifies those
		 * where the `SI` field is marked as `true`, and returns the filtered entries as a set.
		 *
		 * @return A set containing all entries with the `SI` property set to `true`.
		 * @since 1.0.0
		 */
		val siMeasures = entries.filter { it.si }.toSet()
	}

	/**
	 * Converts the properties of the `Measure` class into a map representation.
	 *
	 * @return A map containing key-value pairs where keys are property names
	 * and values are their corresponding field values in the `Measure` class.
	 * @since 1.0.0
	 */
	@Suppress("functionName")
	private fun _toMap() = mapOf(
		"unitClass" to unitClass,
		"defaultUnit" to defaultUnit
	)

	/**
	 * Retrieves the value associated with the specified property name from the underlying map.
	 *
	 * - `unitClass` - TYPE: `KClass<out ScalarUnit>?`
	 * - `defaultUnit` - TYPE: `ScalarUnit`
	 *
	 * @param thisRef The object for which the property was accessed. This is not used in this implementation.
	 * @param property The metadata for the property being accessed, used to retrieve the associated value from the map.
	 * @return The value mapped to the property name, cast to the specified type [R].
	 * @since 1.0.0
	 */
	@Suppress("unchecked_cast")
	operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}