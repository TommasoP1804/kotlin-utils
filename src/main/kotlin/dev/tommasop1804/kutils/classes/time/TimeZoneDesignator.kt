package dev.tommasop1804.kutils.classes.time

import dev.tommasop1804.kutils.StringList
import dev.tommasop1804.kutils.StringMap
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.*
import kotlin.reflect.KProperty

/**
 * Represents a military time zone, which is traditionally used in military, aviation,
 * and other contexts requiring standardized and clear time zone indications.
 * Each military time zone has a single-letter designator and may also have
 * a phonetic representation, offset value, and zone identifier.
 *
 * This class provides capabilities to retrieve information about the military time zone,
 * manage temporal adjustments, and represent the zone in various string formats.
 *
 * @property letter The single-letter designation of the military time zone.
 * @property phonetic The phonetic equivalent of the military time zone's letter designation.
 * @property offset The numerical offset from Coordinated Universal Time (UTC) in hours.
 * @property letterChar The letter designation represented as a character.
 * @property completeDesignation A full string representation combining the letter and phonetic.
 * @property utcOffset A human-readable string representation of the UTC offset.
 * @property timeZoneDesignator The current instance of the military time zone.
 * @property zoneId The unique identifier representing the time zone in standard formats.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "kutils_collection_declaration", "kutils_map_declaration")
enum class TimeZoneDesignator(
    /**
     * Military identifier letter.
     * @since 1.0.0
     */
    val letter: String,
    /**
     * Phonetic alphabet name.
     * @since 1.0.0
     */
    val phonetic: String,
    /**
     * Offset of the time zone from UTC (0) in hours.
     * @since 1.0.0
     */
    override val offset: ZoneOffset
): ZoneIdent, TemporalAccessor, TemporalAdjuster {
    	/**
	 * Coordinated Universal Time. Zulu Time Zone. UTC+0.
         * @since 1.0.0
	 */
	Z("Z", "Zulu", ZoneOffset.UTC),
	/**
	 * Alpha Time Zone. Central European Time. UTC+1.
     * @since 1.0.0
	 */
	A("A", "Alpha", ZoneOffset.ofHours(1)),
	/**
	 * Bravo Time Zone. Eastern European Time. UTC+2.
     * @since 1.0.0
	 */
	B("B", "Bravo", ZoneOffset.ofHours(2)),
	/**
	 * Charlie Time Zone. Moscow Standard Time. UTC+3.
     * @since 1.0.0
	 */
	C("C", "Charlie", ZoneOffset.ofHours(3)),
	/**
	 * Charlie Dagger Time Zone. Iran Time. UTC+3:30.
     * @since 1.0.0
	 */
	C_DAGGER("C†", "Charlie Dagger", ZoneOffset.ofHoursMinutes(3, 30)),
	/**
	 * Delta Time Zone. Armenia Time. UTC+4.
     * @since 1.0.0
	 */
	D("D", "Delta", ZoneOffset.ofHours(4)),
	/**
	 * Delta Dagger Time Zone. Afghanistan Time. UTC+4:30.
     * @since 1.0.0
	 */
	D_DAGGER("D†", "Delta Dagger", ZoneOffset.ofHoursMinutes(4, 30)),
	/**
	 * Echo Time Zone. Pakistan Time. UTC+5.
     * @since 1.0.0
	 */
	E("E", "Echo", ZoneOffset.ofHours(5)),
	/**
	 * Echo Dagger Time Zone. India Standard Time. UTC+5:30.
     * @since 1.0.0
	 */
	E_DAGGER("E†", "Echo Dagger", ZoneOffset.ofHoursMinutes(5, 30)),
	/**
	 * Echo Double Dagger Time Zone. Nepal Time. UTC+5:45.
     * @since 1.0.0
	 */
	E_DOUBLE_DAGGER("E‡", "Echo Double Dagger", ZoneOffset.ofHoursMinutes(5, 45)),
	/**
	 * Foxtrot Time Zone. Bangladesh Time. UTC+6.
     * @since 1.0.0
	 */
	F("F", "Foxtrot", ZoneOffset.ofHours(6)),
	/**
	 * Foxtrot Dagger Time Zone. Cocos Islands Time. UTC+6:30.
     * @since 1.0.0
	 */
	F_DAGGER("F†", "Foxtrot Dagger", ZoneOffset.ofHoursMinutes(6, 30)),
	/**
	 * Golf Time Zone. Indochina Time. UTC+7.
     * @since 1.0.0
	 */
	G("G", "Golf", ZoneOffset.ofHours(7)),
	/**
	 * Hotel Time Zone. Australian Western Standard Time. UTC+8.
     * @since 1.0.0
	 */
	H("H", "Hotel", ZoneOffset.ofHours(8)),
	/**
	 * Hotel Double Dagger Time Zone. Eucla Time. UTC+8:45.
     * @since 1.0.0
	 */
	H_DOUBLE_DAGGER("H‡", "Hotel Double Dagger", ZoneOffset.ofHoursMinutes(8, 45)),
	/**
	 * India Time Zone. Japan Standard Time. UTC+9.
     * @since 1.0.0
	 */
	I("I", "India", ZoneOffset.ofHours(9)),
	/**
	 * India Dagger Time Zone. Australian Central Standard Time. UTC+9:30.
     * @since 1.0.0
	 */
	I_DAGGER("I†", "India Dagger", ZoneOffset.ofHoursMinutes(9, 30)),
	/**
	 * Kilo Time Zone. Australian Eastern Standard Time. UTC+10.
     * @since 1.0.0
	 */
	K("K", "Kilo", ZoneOffset.ofHours(10)),
	/**
	 * Kilo Dagger Time Zone. Lord Howe Island Time. UTC+10:30.
     * @since 1.0.0
	 */
	K_DAGGER("K†", "Kilo Dagger", ZoneOffset.ofHoursMinutes(10, 30)),
	/**
	 * Lima Time Zone. Lord Howe Island Time. UTC+11.
     * @since 1.0.0
	 */
	L("L", "Lima", ZoneOffset.ofHours(11)),
	/**
	 * Mike Time Zone. New Zealand Time. UTC+12.
     * @since 1.0.0
	 */
	M("M", "Mike", ZoneOffset.ofHours(12)),
	/**
	 * Mike Dagger Time Zone. Chatham Islands Time. UTC+12:45.
     * @since 1.0.0
	 */
	M_DOUBLE_DAGGER("M‡", "Mike Double Dagger", ZoneOffset.ofHoursMinutes(12, 45)),
	/**
	 * Mike Dagger Time Zone. Tonga Time. UTC+13.
     * @since 1.0.0
	 */
	M_DAGGER_13("M†", "Mike Dagger", ZoneOffset.ofHours(13)),
	/**
	 * Mike Double Dagger Time Zone. Line Islands Time. UTC+13:45.
     * @since 1.0.0
	 */
	M_DOUBLE_DAGGER_13("M‡", "Mike Double Dagger", ZoneOffset.ofHoursMinutes(13, 45)),
	/**
	 * Mike Double Dagger Time Zone. Line Islands Time. UTC+14.
     * @since 1.0.0
	 */
	M_DOUBLE_DAGGER_14("M‡", "Mike Double Dagger", ZoneOffset.ofHours(14)),
	/**
	 * November Time Zone. Azores Time. UTC-1.
     * @since 1.0.0
	 */
	N("N", "November", ZoneOffset.ofHours(-1)),
	/**
	 * Oscar Time Zone. Fernando de Noronha Time. UTC-2.
     * @since 1.0.0
	 */
	O("O", "Oscar", ZoneOffset.ofHours(-2)),
	/**
	 * Papa Time Zone. Brasília Time. UTC-3.
     * @since 1.0.0
	 */
	P("P", "Papa", ZoneOffset.ofHours(-3)),
	/**
	 * Papa Dagger Time Zone. Newfoundland Standard Time. UTC-3:30.
     * @since 1.0.0
	 */
	P_DAGGER("P†", "Papa Dagger", ZoneOffset.ofHoursMinutes(-3, -30)),
	/**
	 * Quebec Time Zone. Atlantic Standard Time. UTC-4.
     * @since 1.0.0
	 */
	Q("Q", "Quebec", ZoneOffset.ofHours(-4)),
	/**
	 * Romeo Time Zone. Eastern Standard Time. UTC-5.
     * @since 1.0.0
	 */
	R("R", "Romeo", ZoneOffset.ofHours(-5)),
	/**
	 * Sierra Time Zone. Central Standard Time. UTC-6.
     * @since 1.0.0
	 */
	S("S", "Sierra", ZoneOffset.ofHours(-6)),
	/**
	 * Tango Time Zone. Mountain Standard Time. UTC-7.
     * @since 1.0.0
	 */
	T("T", "Tango", ZoneOffset.ofHours(-7)),
	/**
	 * Uniform Time Zone. Pacific Standard Time. UTC-8.
     * @since 1.0.0
	 */
	U("U", "Uniform", ZoneOffset.ofHours(-8)),
	/**
	 * Victor Time Zone. Alaska Standard Time. UTC-9.
     * @since 1.0.0
	 */
	V("V", "Victor", ZoneOffset.ofHours(-9)),
	/**
	 * Victor Dagger Time Zone. Marquesas Time. UTC-9:30.
     * @since 1.0.0
	 */
	V_DAGGER("V†", "Victor Dagger", ZoneOffset.ofHoursMinutes(-9, -30)),
	/**
	 * Whiskey Time Zone. Hawaii-Aleutian Standard Time. UTC-10.
     * @since 1.0.0
	 */
	W("W", "Whiskey", ZoneOffset.ofHours(-10)),
	/**
	 * X-ray Time Zone. Bering Standard Time. UTC-11.
     * @since 1.0.0
	 */
	X("X", "X-ray", ZoneOffset.ofHours(-11)),
	/**
	 * Yankee Time Zone. International Date Line West. UTC-12.
	 * @since 1.0.0
	 */
	Y("Y", "Yankee", ZoneOffset.ofHours(-12));

    /**
     * Represents the first character of the `letter` property for the current `MilitaryTimeZone` instance.
     * This provides a direct way to reference the primary letter designation of the time zone.
     *
     * @since 1.0.0
     */
    val letterChar = letter[0]

    /**
     * Represents the combined designation of a military time zone
     * by concatenating its phonetic name and specifying it as a "Time Zone".
     * This property provides a readable and complete textual representation
     * of the military time zone's name.
     *
     * @since 1.0.0
     */
    val completeDesignation = "$phonetic Time Zone"

    /**
     * Represents the UTC offset as a string for the current `MilitaryTimeZone` instance.
     *
     * The returned value includes the "UTC" prefix followed by the `ZoneOffset` identifier,
     * or only "UTC" for instances representing the zero offset (`ZoneOffset.UTC`).
     *
     * @return A string representation of the UTC offset, prefixed with "UTC".
     * @since 1.0.0
     */
    override val utcOffset: String
        @Suppress("IDENTITY_SENSITIVE_OPERATIONS_WITH_VALUE_TYPE")
        get() = "UTC" + if (offset == ZoneOffset.UTC) "" else offset.id

    /**
     * Retrieves the current instance of the `MilitaryTimeZone`, if available, or `null` otherwise.
     *
     * This property provides the military time zone representation for the current `ZoneIdent` implementation.
     * If the instance corresponds to a military time zone, this property will return the appropriate `MilitaryTimeZone` object,
     * otherwise it will return `null`.
     *
     * @return The military time zone associated with this instance, or `null` if it is not applicable.
     * @since 1.0.0
     */
	override val timeZoneDesignator: TimeZoneDesignator?
        get() = this

    /**
     * Provides the `ZoneId` representation of the military time zone based on the UTC offset
     * associated with this `MilitaryTimeZone` instance.
     *
     * @return A `ZoneId` initialized using the `utcOffset` of the military time zone.
     * @since 1.0.0
     */
    override val zoneId: ZoneId
        get() = ZoneId.of(utcOffset)

    /**
     * Represents the name of the current enum constant in the `MilitaryTimeZone` class.
     *
     * This property overrides the base `name` property, providing the name of the
     * enum instance as a string. It is useful for identifying specific enum instances
     * by their designated name.
     *
     * 
     * @since 1.0.0
     */
    override val enumName: String
        get() = name

    companion object {
		/**
		 * A filtered collection of entries where each entry's `name` property has a length of exactly one.
		 *
		 * The collection contains only the standard (one letter) military time zones.
		 *
		 * @since 1.0.0
		 */
		val standardZones = entries.filter { it.name.length == 1 }

        /**
         * Retrieves a MilitaryTimeZone instance corresponding to the provided letter.
         *
         * @param letter The letter representing the military time zone. Should be a single-character string.
         * @return The corresponding MilitaryTimeZone if the letter matches an entry or the system default for "J".
         * Returns null if no matching time zone is found.
         * @since 1.0.0
         */
        infix fun of(letter: String): TimeZoneDesignator? {
            if (letter == "J") return systemDefault()
            for (timeZone in entries) {
                if (timeZone.letter == letter) return timeZone
            }
            return null
        }

        /**
         * Creates an instance based on the given character by converting it to a string.
         * This method delegates the operation to another implementation handling strings.
         *
         * @param letter A single character to be converted to a string and processed.
         * @since 1.0.0
         */
        infix fun of(letter: Char) = of(letter.toString())

        /**
         * Converts the given `ZoneId` into a corresponding `MilitaryTimeZone` object, if applicable.
         *
         * @param zoneId The `ZoneId` to be converted into a `MilitaryTimeZone`.
         * @return The corresponding `MilitaryTimeZone` instance if the conversion is successful, or `null` if no match is found.
         * @since 1.0.0
         */
        infix fun from(zoneId: ZoneId): TimeZoneDesignator? = from(zoneId.rules.getOffset(java.time.Instant.now()))

        /**
         * Retrieves the military time zone that corresponds to the specified offset.
         *
         * @param offset The `ZoneOffset` for which the corresponding military time zone is to be found.
         * @return The `MilitaryTimeZone` corresponding to the provided offset, or `null` if no matching instance exists.
         * @since 1.0.0
         */
		infix fun from(offset: ZoneOffset): TimeZoneDesignator? {
            for (timeZone in entries) {
                if (timeZone.offset.id == offset.id) return timeZone
            }
            return null
        }

        /**
         * Retrieves a MilitaryTimeZone instance corresponding to the provided TimeZone.
         *
         * Internally, the method converts the given TimeZone's offset into a ZoneOffset
         * and attempts to match it to an existing MilitaryTimeZone.
         *
         * @param timeZone The TimeZone instance used to determine the corresponding MilitaryTimeZone.
         * @return The MilitaryTimeZone corresponding to the provided TimeZone, or null if no match is found.
         * @since 1.0.0
         */
		infix fun from(timeZone: TimeZone): TimeZoneDesignator? = from(timeZone.offset)

        /**
         * Retrieves a military time zone based on the system's default time zone.
         * If the system's default time zone cannot be matched to a military time zone, returns a specified default value.
         *
         * @param defaultValue The fallback military time zone to return if the system's default zone cannot be resolved.
         * @return A corresponding `MilitaryTimeZone` instance for the system's default time zone,
         * or the provided default value if no match is found.
         * @throws IllegalStateException if no system default time zone is found and no `defaultValue` is provided.
         * @since 1.0.0
         */
        fun systemDefault(defaultValue: TimeZoneDesignator? = null): TimeZoneDesignator = from(ZoneId.systemDefault()) ?: (defaultValue ?: throw IllegalStateException("System default zone is not a military zone."))

        /**
         * Provides a list of formatted string representations of all military time zone entries.
         *
         * Each entry is formatted using the `prettyPrint` method of the `MilitaryTimeZone` class,
         * resulting in a human-readable string containing the time zone's information.
         *
         * @return A list of strings, each representing a formatted military time zone entry.
         * @since 1.0.0
         */
        fun valuesPrettyPrint(): StringList {
            val result = mutableListOf<String>()
            for (entry in entries) {
                result.add(entry.prettyPrint())
            }
            return result
        }

        /**
         * Creates a map of string key-value pairs by transforming the entries of the current instance.
         * Each entry is mapped with its name as the key and the result of `prettyPrint()` as the value.
         *
         * @return A map where the keys are the names of the entries, and the values are their prettified string representations.
         * @since 1.0.0
         */
        fun prettyMap(): StringMap {
            val result = mutableMapOf<String, String>()
            for (entry in entries) {
                result[entry.name] = entry.prettyPrint()
            }
            return result
        }
    }

    /**
     * Returns the string representation of this MilitaryTimeZone instance.
     *
     * 
     * @return the string representation of the military time zone, which corresponds to its letter designation
     * @since 1.0.0
     */
    override fun toString(): String = letter

    /**
     * Provides a formatted string representation of the military time zone,
     * including its letter designation, phonetic equivalent, and UTC offset.
     *
     * 
     * @return A string in the format "<letter> (<phonetic>) - <utcOffset>".
     * @since 1.0.0
     */
    override fun prettyPrint(): String = "$letter ($phonetic) - $utcOffset"

    /**
     * Determines if the specified temporal field is supported by this instance.
     *
     * 
     * @param field The temporal field to be checked for support.
     * @return `true` if the field is supported, `false` otherwise.
     * @since 1.0.0
     */
    override infix fun isSupported(field: TemporalField): Boolean = if (field is ChronoField) field == ChronoField.OFFSET_SECONDS else field.isSupportedBy(this)

    /**
     * Retrieves the value of the specified temporal field as a `Long`.
     * The method supports the retrieval of the `ChronoField.OFFSET_SECONDS` field,
     * returning the total seconds of the offset. For other fields, an exception may be thrown
     * or the field's value will be derived from the current instance.
     *
     * 
     * @param field The temporal field to retrieve the value for.
     * @return The value of the specified temporal field as a `Long`.
     * @throws UnsupportedTemporalTypeException If the field is unsupported by this implementation.
     * @since 1.0.0
     */
    override infix fun getLong(field: TemporalField): Long {
        if (field === ChronoField.OFFSET_SECONDS) {
            return offset.totalSeconds.toLong()
        } else if (field is ChronoField) {
            throw UnsupportedTemporalTypeException("Unsupported field: $field")
        }
        return field.getFrom(this)
    }

    /**
     * Adjusts the given temporal object to the specified offset defined by the current instance.
     *
     * The adjustment modifies the provided temporal object by setting its offset to the total seconds
     * represented by this instance's `offset` field.
     *
     * 
     * @param temporal The temporal object to be adjusted. Must support being modified with an offset.
     * @return A temporal object updated to match the offset of the current `MilitaryTimeZone` instance.
     * @since 1.0.0
     */
    override infix fun adjustInto(temporal: Temporal): Temporal = temporal.with(ChronoField.OFFSET_SECONDS, offset.totalSeconds.toLong())

	/**
	 * Converts the properties of the MilitaryTimeZone instance into a Map.
	 *
	 * @return A Map containing key-value pairs of the instance's fields and their values.
	 * @since 1.0.0
	 */
	@Suppress("functionName")
	private fun _toMap() = mapOf(
		"letter" to letter,
		"phonetic" to phonetic,
		"offset" to offset,
		"completeDesignation" to completeDesignation,
		"letterChar" to letterChar,
		"utcOffset" to utcOffset,
		"zoneId" to zoneId,
		"enumName" to name
	)

	/**
	 * Retrieves the value of a property from the internal map representation of the MilitaryTimeZone instance.
	 *
	 * - `letter`: Represents the letter designation of the military time zone - TYPE: [String].
	 * - `phonetic`: Phonetic representation of the time zone's letter - TYPE: [String].
	 * - `offset`: The numerical UTC offset value - TYPE: [ZoneOffset].
	 * - `completeDesignation`: A complete string representation of the time zone - TYPE: [String].
	 * - `letterChar`: A single character representing the zone letter - TYPE: [Char].
	 * - `utcOffset`: The UTC offset in hours and minutes as a string - TYPE: [String].
	 * - `zoneId`: The corresponding ZoneId representation - TYPE: [ZoneId].
	 * - `enumName`: The name of the enumeration constant for the time zone - TYPE: [String].
	 *
	 * @param thisRef The reference to the object for which the property is being accessed. Can be null.
	 * @param property The property whose value is to be retrieved.
	 * @return The value of the property specified, cast to the desired type R.
	 * @since 1.0.0
	 */
	@Suppress("unchecked_cast")
	override operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}