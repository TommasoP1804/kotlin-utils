package dev.tommasop1804.kutils.classes.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.classes.time.LocalMonthDayTime.Companion.toLocalMonthDayTime
import dev.tommasop1804.kutils.firstOrThrow
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import java.time.*
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalField
import kotlin.reflect.KProperty

/**
 * Time zones interface.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = ZoneIdent.Serializer::class)
@JsonDeserialize(using = ZoneIdent.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = ZoneIdent.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = ZoneIdent.OldDeserializer::class)
@Suppress("unused")
interface ZoneIdent : TemporalAccessor, TemporalAdjuster, Serializable {
    /**
     * Represents the time-zone offset for this `ZoneIdent`.
     * The offset defines the difference between UTC and local time
     * in terms of hours and minutes. This value is used to determine
     * the time-zone rules and conversions associated with the instance.
     *
     * 
     * @since 1.0.0
     */
    val offset: ZoneOffset
    /**
     * Represents the UTC offset of a specific zone in string format.
     * The offset is typically expressed as a string in the format "+HH:mm" or "-HH:mm".
     * It provides a human-readable representation of the time zone's offset from UTC.
     *
     * This property is part of the ZoneIdent class, which helps in handling time zone information
     * and conversions across various date and time representations.
     *
     * 
     * @since 1.0.0
     */
    val utcOffset: String
    /**
     * Represents the military time zone associated with the ZoneIdent instance.
     *
     * The military time zone provides a standardized designation used in military and aviation domains.
     * This property holds a nullable reference to a [TimeZoneDesignator] object if available, or null otherwise.
     *
     * The value of this property is meant to encapsulate the specific military time zone designation
     * that corresponds to the instance of the ZoneIdent.
     *
     * 
     * @since 1.0.0
     */
    val timeZoneDesignator: TimeZoneDesignator?
    /**
     * Represents the ZoneId associated with the ZoneIdent instance.
     * This identifies a time-zone, used to handle conversions and computations
     * involving date-time values within the specified time-zone context.
     *
     * 
     * @since 1.0.0
     */
    val zoneId: ZoneId
    /**
     * Represents the name of the enumeration associated with a given zone identifier.
     * This property is part of the class ZoneIdent, which encapsulates information
     * about various zone identifiers and their related attributes.
     *
     * 
     * @since 1.0.0
     */
    val enumName: String

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
        @Serial private const val serialVersionUID = 1L

        /**
         * Retrieves a `ZoneIdent` based on the given identifier string.
         *
         * The identifier can represent either a military time zone or a standard time zone.
         * If the identifier has a length of 1 or 2, it is interpreted as a potential military time zone.
         * Otherwise, it is treated as a standard time zone.
         *
         * @param id The string identifier of the zone to be resolved.
         * @return The corresponding `ZoneIdent` for the provided identifier.
         * @throws IllegalArgumentException if the identifier cannot be resolved into a valid time zone.
         * @since 1.0.0
         */
        infix fun of(id: String): ZoneIdent = TimeZoneDesignator.of(id) ?: TimeZone.of(id).firstOrThrow { IllegalArgumentException("Invalid ZoneIdent: $id") }

        /**
         * Converts the provided name of a time zone or military time zone into a `ZoneIdent` instance.
         *
         * The method first attempts to match the provided name with a time zone in the `TimeZone` enum.
         * If no match is found, it then attempts to match it with a military time zone in the `MilitaryTimeZone` enum.
         * If the name cannot be matched to either, an `IllegalArgumentException` is thrown.
         *
         * @param name The name of the time zone or military time zone to convert.
         * @return A `ZoneIdent` instance corresponding to the provided name.
         * @since 1.0.0
         * @throws IllegalArgumentException If the name does not correspond to any valid `TimeZone` or `MilitaryTimeZone`.
         */
        infix fun ofEnumName(name: String): ZoneIdent {
            return try {
                TimeZone.valueOf(name)
            } catch (_: Exception) {
                try {
                    TimeZoneDesignator.valueOf(name)
                } catch(_: Exception) {
                    throw IllegalArgumentException("Invalid ZoneIdent name: $name")
                }
            }
        }

        /**
         * Checks if the current [OffsetDateTime] has the same offset as the provided [ZoneIdent].
         *
         * @receiver The [OffsetDateTime] instance being checked.
         * @param zoneIdent The [ZoneIdent] against which the offset will be compared.
         * @return `true` if the offsets are identical, `false` otherwise.
         * @since 1.0.0
         */
        infix fun OffsetDateTime.hasOffsetAs(zoneIdent: ZoneIdent): Boolean = offset.id == zoneIdent.offset.id
        /**
         * Checks if the offset of the current [OffsetMonthDayTime] instance matches the offset of the specified [ZoneIdent].
         *
         * @receiver The [OffsetMonthDayTime] instance whose offset needs to be compared.
         * @param zoneIdent The [ZoneIdent] instance whose offset will be used for comparison.
         * @return `true` if the offset of the [OffsetMonthDayTime] matches the offset of the [ZoneIdent], `false` otherwise.
         * @since 1.0.0
         */
        infix fun OffsetMonthDayTime.hasOffsetAs(zoneIdent: ZoneIdent): Boolean = offset.id == zoneIdent.offset.id
        /**
         * Checks if the receiver OffsetTime instance has the same offset as the offset defined in the specified ZoneIdent instance.
         *
         * @receiver The OffsetTime instance to be checked.
         * @param zoneIdent The ZoneIdent instance whose offset will be compared.
         * @return `true` if the offset of the receiver matches the offset of the specified ZoneIdent, `false` otherwise.
         * @since 1.0.0
         */
        infix fun OffsetTime.hasOffsetAs(zoneIdent: ZoneIdent): Boolean = this.offset.id == zoneIdent.offset.id
    }

    /**
     * Converts the given [ZonedMonthDayTime] to the same instant in the time zone specified
     * by the `offset` of the containing class instance.
     *
     * 
     * @param zonedMonthDayTime the [ZonedMonthDayTime] instance to be converted.
     * @since 1.0.0
     */
    infix fun convert(zonedMonthDayTime: ZonedMonthDayTime): ZonedMonthDayTime = zonedMonthDayTime.withZoneSameInstant(offset)
    /**
     * Converts an instance of [OffsetMonthDayTime] to a new instance with the same instant adjusted to the receiver's offset.
     *
     * 
     * @param offsetMonthDayTime The [OffsetMonthDayTime] instance to be converted.
     * @return A new [OffsetMonthDayTime] instance adjusted to the receiver's offset.
     * @since 1.0.0
     */
    infix fun convert(offsetMonthDayTime: OffsetMonthDayTime): OffsetMonthDayTime = offsetMonthDayTime.atOffsetSameInstant(offset)
    /**
     * Converts a LocalMonthDayTime instance to a new instance with the zone
     * settings applied from the current ZoneIdent configuration, aligning it
     * to the corresponding zone and instant.
     *
     * 
     * the conversion.
     * @param localMonthDayTime The LocalMonthDayTime instance to be converted.
     * @since 1.0.0
     */
    infix fun convert(localMonthDayTime: LocalMonthDayTime): LocalMonthDayTime = localMonthDayTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(zoneId).toLocalMonthDayTime()
    /**
     * Converts the provided `ZonedDateTime` to a new `ZonedDateTime` instance with the same instant
     * but in the time zone specified by the `toZoneId()` of this instance.
     *
     * 
     * @param dateTime The `ZonedDateTime` to be converted to the target time zone.
     * @since 1.0.0
     */
    infix fun convert(dateTime: ZonedDateTime): ZonedDateTime = dateTime.withZoneSameInstant(zoneId)
    /**
     * Converts the given [OffsetDateTime] to the same instant in time, but with the offset of this ZoneIdent.
     *
     * 
     * @param dateTime The input [OffsetDateTime] to be converted.
     * @since 1.0.0
     */
    infix fun convert(dateTime: OffsetDateTime): OffsetDateTime = dateTime.withOffsetSameInstant(offset)
    /**
     * Converts the given [LocalDateTime] instance to a new [LocalDateTime] instance adjusted to the
     * time zone defined by the `zoneId` property of the containing class.
     *
     * 
     * @param dateTime The [LocalDateTime] to be converted to the target time zone.
     * @return The converted [LocalDateTime] adjusted to the target time zone.
     * @since 1.0.0
     */
    infix fun convert(dateTime: LocalDateTime): LocalDateTime = dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(zoneId).toLocalDateTime()

    /**
     * Formats and returns a human-readable string representation of this ZoneIdent instance.
     *
     * The method combines relevant properties of the ZoneIdent class, such as offset,
     * utcOffset, militaryTimeZone, zoneId, and enumName, to generate a descriptive string.
     *
     * 
     * @return a formatted string representation of the ZoneIdent instance
     * @since 1.0.0
     */
    fun prettyPrint(): String

    /**
     * Checks if the given temporal field is supported.
     *
     * 
     * @param field A temporal field to be checked for support.
     * @return `true` if the given temporal field is supported, `false` otherwise.
     * @since 1.0.0
     */
    override infix fun isSupported(field: TemporalField): Boolean

    /**
     * Retrieves the value of the specified temporal field as a `Long`.
     *
     * This method is intended for use with fields supported by this object.
     *
     * 
     * @param field The temporal field to be queried, not null.
     * @return The value of the specified field as a `Long`.
     * @throws java.time.temporal.UnsupportedTemporalTypeException If the field is not supported.
     * @throws DateTimeException If a field-supported value cannot be obtained.
     * @since 1.0.0
     */
    override infix fun getLong(field: TemporalField): Long

    /**
     * Adjusts the specified temporal object to have the same state as this `ZoneIdent` instance.
     * The returned object will have the date-time adjusted to match the current properties of `ZoneIdent`.
     *
     * 
     * @param temporal The temporal object to adjust, not null.
     * @return A temporal object adjusted to match the current state of this `ZoneIdent` instance.
     * @since 1.0.0
     */
    override infix fun adjustInto(temporal: Temporal): Temporal

    /**
     * Provides the delegated value for the specified property accessed in the context of this class.
     *
     * This operator function is used for delegated property retrieval by customizing
     * how the value of the property is obtained.
     *
     * @param thisRef The reference to the object that contains the delegated property.
     * @param property The metadata for the property being accessed.
     * @return The value of the property as specified by the delegator.
     * @since 1.0.0
     * @see TimeZoneDesignator.getValue
     * @see TimeZone.getValue
     */
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>): R

    class Serializer : ValueSerializer<ZoneIdent>() {
        override fun serialize(value: ZoneIdent, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
            gen.writeString(value.toString())
        }
    }

    class Deserializer : ValueDeserializer<ZoneIdent>() {
        override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = of(p.string)
    }

    class OldSerializer : JsonSerializer<ZoneIdent>() {
        override fun serialize(value: ZoneIdent, gen: JsonGenerator, serializers: SerializerProvider) = gen.writeString(value.toString())
    }

    class OldDeserializer : JsonDeserializer<ZoneIdent>() {
        override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext) = of(p.text)
    }

    @jakarta.persistence.Converter(autoApply = true)
    class Converter : AttributeConverter<ZoneIdent?, String?> {
        override fun convertToDatabaseColumn(attribute: ZoneIdent?) = attribute?.enumName
        override fun convertToEntityAttribute(dbData: String?) = dbData?.let { ofEnumName(it) }
    }
}