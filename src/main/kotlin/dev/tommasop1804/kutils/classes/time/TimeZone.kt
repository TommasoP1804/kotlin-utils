package dev.tommasop1804.kutils.classes.time

import dev.tommasop1804.kutils.StringList
import dev.tommasop1804.kutils.StringMap
import dev.tommasop1804.kutils.unaryPlus
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.*
import java.util.*
import kotlin.reflect.KProperty

/**
 * Represents a time zone with an identifier, zone name, UTC offset,
 * and related information.
 *
 * @property id The unique string identifier for the time zone.
 * @property zoneName The name of the time zone.
 * @property offset The associated offset for the time zone.
 * @property zoneIdByAbbreviation A mapping of zone abbreviations to zone IDs.
 * @property zoneIdByOffset A mapping of offsets to zone IDs.
 * @property zoneId The corresponding `ZoneId` object for this time zone.
 * @property utcOffset The string representation of the UTC offset.
 * @property timeZoneDesignator The corresponding military time zone, if applicable.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "kutils_collection_declaration", "kutils_map_declaration", "kutils_temporal_now_as_temporal")
enum class TimeZone(
    val id: String,
    val zoneName: String,
    override val offset: ZoneOffset,
): ZoneIdent, TemporalAccessor, TemporalAdjuster {
    ACDT("ACDT", "Australian Central Daylight Saving Time", ZoneOffset.ofHoursMinutes(10, 30)),
	ACST("ACST", "Australian Central Standard Time", ZoneOffset.ofHoursMinutes(9, 30)),
	ACT("ACT", "Acre Time", ZoneOffset.ofHours(-5)),
	ACWST("ACWST", "Australian Central Western Standard Time", ZoneOffset.ofHoursMinutes(8, 45)),
	ADT("ADT", "Atlantic Daylight Time", ZoneOffset.ofHours(-3)),
	AEDT("AEDT", "Australian Eastern Daylight Saving Time", ZoneOffset.ofHours(11)),
	AEST("AEST", "Australian Eastern Standard Time", ZoneOffset.ofHours(10)),
	AFT("AFT", "Afghanistan Time", ZoneOffset.ofHoursMinutes(4, 30)),
	AKDT("AKDT", "Alaska Daylight Time", ZoneOffset.ofHours(-8)),
	AKST("AKST", "Alaska Standard Time", ZoneOffset.ofHours(-9)),
	AMST("AMST", "Amazon Summer Time", ZoneOffset.ofHours(-3)),
	AMT_M4("AMT", "Amazon Time", ZoneOffset.ofHours(-4)),
	AMT_4("AMT", "Armenia Time", ZoneOffset.ofHours(4)),
	ANAT("ANAT", "Anadyr Time", ZoneOffset.ofHours(12)),
	AQTT("AQTT", "Aqtobe Time", ZoneOffset.ofHours(5)),
	ART("ART", "Argentina Time", ZoneOffset.ofHours(-3)),
	AST_3("AST", "Arabian Standard Time", ZoneOffset.ofHours(3)),
	AST_M4("AST", "Atlantic Standard Time", ZoneOffset.ofHours(-4)),
	AWST("AWST", "Australian Western Standard Time", ZoneOffset.ofHours(8)),
	AZOST("AZOST", "Azores Summer Time", ZoneOffset.ofHours(0)),
	AZOT("AZOT", "Azores Standard Time", ZoneOffset.ofHours(-1)),
	AZT("AZT", "Azerbaijan Time", ZoneOffset.ofHours(4)),
	BDT("BDT", "Brunei Time", ZoneOffset.ofHours(8)),
	BIOT("BIOT", "British Indian Ocean Time", ZoneOffset.ofHours(6)),
	BIT("BIT", "Baker Island Time", ZoneOffset.ofHours(-12)),
	BOT("BOT", "Bolivia Time", ZoneOffset.ofHours(-4)),
	BRST("BRST", "Brasília Summer Time", ZoneOffset.ofHours(-2)),
	BRT("BRT", "Brasília Time", ZoneOffset.ofHours(-3)),
	BST_6("BST", "Bangladesh Standard Time", ZoneOffset.ofHours(6)),
	BST_11("BST", "Bougainville Standard Time", ZoneOffset.ofHours(11)),
	BST_1("BST", "British Summer Time", ZoneOffset.ofHours(1)),
	BTT("BTT", "Bhutan Time", ZoneOffset.ofHours(6)),
	CAT("CAT", "Central Africa Time", ZoneOffset.ofHours(2)),
	CCT("CCT", "Cocos Islands Time", ZoneOffset.ofHoursMinutes(6, 30)),
	CDT_M5("CDT", "Central Daylight Time", ZoneOffset.ofHours(-5)),
	CDT_M4("CDT", "Cuba Daylight Time", ZoneOffset.ofHours(-4)),
	CEST("CEST", "Central European Summer Time", ZoneOffset.ofHours(2)),
	CET("CET", "Central European Time", ZoneOffset.ofHours(1)),
	CHADT("CHADT", "Chatham Daylight Time", ZoneOffset.ofHoursMinutes(13, 45)),
	CHAST("CHAST", "Chatham Standard Time", ZoneOffset.ofHoursMinutes(12, 45)),
	CHOT("CHOT", "Choibalsan Time", ZoneOffset.ofHours(8)),
	CHOST("CHOST", "Choibalsan Summer Time", ZoneOffset.ofHours(9)),
	CHST("CHST", "Chamorro Standard Time", ZoneOffset.ofHours(10)),
	CHUT("CHUT", "Chuuk Time", ZoneOffset.ofHours(10)),
	CIST("CIST", "Clipperton Island Standard Time", ZoneOffset.ofHours(-8)),
	CKT("CKT", "Cook Island Time", ZoneOffset.ofHours(-10)),
	CLST("CLST", "Chile Summer Time", ZoneOffset.ofHours(-3)),
	CLT("CLT", "Chile Standard Time", ZoneOffset.ofHours(-4)),
	COST("COST", "Colombia Summer Time", ZoneOffset.ofHours(-4)),
	COT("COT", "Colombia Time", ZoneOffset.ofHours(-5)),
	CST_M6("CST", "Central Standard Time", ZoneOffset.ofHours(-6)),
	CST_8("CST", "China Standard Time", ZoneOffset.ofHours(8)),
	CST_M5("CST", "Cuba Standard Time", ZoneOffset.ofHours(-5)),
	CVT("CVT", "Cape Verde Time", ZoneOffset.ofHours(-1)),
	CWST("CWST", "Central Western Standard Time", ZoneOffset.ofHoursMinutes(8, 45)),
	CXT("CXT", "Christmas Island Time", ZoneOffset.ofHours(7)),
	DAVT("DAVT", "Davis Time", ZoneOffset.ofHours(7)),
	DDUT("DDUT", "Dumont d'Urville Time", ZoneOffset.ofHours(10)),
	DFT("DFT", "AIX-specific equivalent of Central European Time", ZoneOffset.ofHours(1)),
	EASST("EASST", "Easter Island Summer Time", ZoneOffset.ofHours(-5)),
	EAST("EAST", "Easter Island Standard Time", ZoneOffset.ofHours(-6)),
	EAT("EAT", "East Africa Time", ZoneOffset.ofHours(3)),
	ECT_M4("ECT", "Eastern Caribbean Time", ZoneOffset.ofHours(-4)),
	ECT_M5("ECT", "Ecuador Time", ZoneOffset.ofHours(-5)),
	EEST("EEST", "Eastern European Summer Time", ZoneOffset.ofHours(3)),
	EET("EET", "Eastern European Time", ZoneOffset.ofHours(2)),
	EGST("EGST", "Eastern Greenland Summer Time", ZoneOffset.ofHours(0)),
	EGT("EGT", "Eastern Greenland Time", ZoneOffset.ofHours(-1)),
	EST("EST", "Eastern Standard Time", ZoneOffset.ofHours(-5)),
	FET("FET", "Further-eastern European Time", ZoneOffset.ofHours(3)),
	FJT("FJT", "Fiji Time", ZoneOffset.ofHours(12)),
	FKST("FKST", "Falkland Islands Standard Time", ZoneOffset.ofHours(-3)),
	FNT("FNT", "Fernando de Noronha Time", ZoneOffset.ofHours(-2)),
	GALT("GALT", "Galápagos Time", ZoneOffset.ofHours(-6)),
	GAMT("GAMT", "Gambier Islands Time", ZoneOffset.ofHours(-9)),
	GET("GET", "Georgia Standard Time", ZoneOffset.ofHours(4)),
	GFT("GFT", "French Guiana Time", ZoneOffset.ofHours(-3)),
	GILT("GILT", "Gilbert Island Time", ZoneOffset.ofHours(12)),
	GIT("GIT", "Gambier Island Time", ZoneOffset.ofHours(-9)),
	GMT("GMT", "Greenwich Mean Time", ZoneOffset.ofHours(0)),
	GST_M2("GST", "South Georgia and the South Sandwich Islands Time", ZoneOffset.ofHours(-2)),
	GST_4("GST", "Gulf Standard Time", ZoneOffset.ofHours(4)),
	GYT("GYT", "Guyana Time", ZoneOffset.ofHours(-4)),
	HDT("HDT", "Hawaii–Aleutian Daylight Time", ZoneOffset.ofHours(-9)),
	HAEC("HAEC", "Heure Avancée d'Europe Centrale", ZoneOffset.ofHours(2)),
	HST("HST", "Hawaii–Aleutian Standard Time", ZoneOffset.ofHours(-10)),
	HKT("HKT", "Hong Kong Time", ZoneOffset.ofHours(8)),
	HMT("HMT", "Heard and McDonald Islands Time", ZoneOffset.ofHours(5)),
	HOVST("HOVST", "Hovd Summer Time", ZoneOffset.ofHours(8)),
	HOVT("HOVT", "Hovd Time", ZoneOffset.ofHours(7)),
	ICT("ICT", "Indochina Time", ZoneOffset.ofHours(7)),
	IDLW("IDLW", "International Day Line West time zone", ZoneOffset.ofHours(-12)),
	IDT("IDT", "Israel Daylight Time", ZoneOffset.ofHours(3)),
	IOT("IOT", "Indian Ocean Time", ZoneOffset.ofHours(6)),
	IRDT("IRDT", "Iran Daylight Time", ZoneOffset.ofHoursMinutes(4, 30)),
	IRKT("IRKT", "Irkutsk Time", ZoneOffset.ofHours(8)),
	IRST("IRST", "Iran Standard Time", ZoneOffset.ofHoursMinutes(3, 30)),
	IST_5_30("IST", "Indian Standard Time", ZoneOffset.ofHoursMinutes(5, 30)),
	IST_1("IST", "Irish Standard Time", ZoneOffset.ofHours(1)),
	IST_2("IST", "Israel Standard Time", ZoneOffset.ofHours(2)),
	JST("JST", "Japan Standard Time", ZoneOffset.ofHours(9)),
	KALT("KALT", "Kaliningrad Time", ZoneOffset.ofHours(2)),
	KGT("KGT", "Kyrgyzstan Time", ZoneOffset.ofHours(6)),
	KOST("KOST", "Kosrae Time", ZoneOffset.ofHours(11)),
	KRAT("KRAT", "Krasnoyarsk Time", ZoneOffset.ofHours(7)),
	KST("KST", "Korea Standard Time", ZoneOffset.ofHours(9)),
	LHST_10_30("LHST", "Lord Howe Standard Time", ZoneOffset.ofHoursMinutes(10, 30)),
	LHST_11("LHST", "Lord Howe Summer Time", ZoneOffset.ofHours(11)),
	LINT("LINT", "Line Islands Time", ZoneOffset.ofHours(14)),
	MAGT("MAGT", "Magadan Time", ZoneOffset.ofHours(11)),
	MART("MART", "Marquesas Islands Time", ZoneOffset.ofHoursMinutes(-9, -30)),
	MAWT("MAWT", "Mawson Station Time", ZoneOffset.ofHours(5)),
	MDT("MDT", "Mountain Daylight Time", ZoneOffset.ofHours(-6)),
	MET("MET", "Middle European Time", ZoneOffset.ofHours(1)),
	MEST("MEST", "Middle European Summer Time", ZoneOffset.ofHours(2)),
	MHT("MHT", "Marshall Islands Time", ZoneOffset.ofHours(12)),
	MIST("MIST", "Macquarie Island Station Time", ZoneOffset.ofHours(11)),
	MIT("MIT", "Marquesas Islands Time", ZoneOffset.ofHoursMinutes(-9, -30)),
	MMT("MMT", "Myanmar Standard Time", ZoneOffset.ofHoursMinutes(6, 30)),
	MSK("MSK", "Moscow Time", ZoneOffset.ofHours(3)),
	MST_8("MST", "Macau Standard Time", ZoneOffset.ofHours(8)),
	MST_M7("MST", "Mountain Standard Time", ZoneOffset.ofHours(-7)),
	MUT("MUT", "Mauritius Time", ZoneOffset.ofHours(4)),
	MVT("MVT", "Maldives Time", ZoneOffset.ofHours(5)),
	MYT("MYT", "Malaysia Time", ZoneOffset.ofHours(8)),
	NCT("NCT", "New Caledonia Time", ZoneOffset.ofHours(11)),
	NDT("NDT", "Newfoundland Daylight Time", ZoneOffset.ofHoursMinutes(-2, -30)),
	NFT("NFT", "Norfolk Island Time", ZoneOffset.ofHours(11)),
	NOVT("NOVT", "Novosibirsk Time", ZoneOffset.ofHours(7)),
	NPT("NPT", "Nepal Time", ZoneOffset.ofHoursMinutes(5, 45)),
	NST("NST", "Newfoundland Standard Time", ZoneOffset.ofHoursMinutes(-3, -30)),
	NT("NT", "Newfoundland Time", ZoneOffset.ofHoursMinutes(-3, -30)),
	NUT("NUT", "Niue Time", ZoneOffset.ofHours(-11)),
	NZDT("NZDT", "New Zealand Daylight Time", ZoneOffset.ofHours(13)),
	NZDST("NZDST", "New Zealand Daylight Saving Time", ZoneOffset.ofHours(13)),
	NZST("NZST", "New Zealand Standard Time", ZoneOffset.ofHours(12)),
	OMST("OMST", "Omsk Time", ZoneOffset.ofHours(6)),
	ORAT("ORAT", "Oral Time", ZoneOffset.ofHours(5)),
	PDT("PDT", "Pacific Daylight Time", ZoneOffset.ofHours(-7)),
	PET("PET", "Peru Time", ZoneOffset.ofHours(-5)),
	PETT("PETT", "Kamchatka Time", ZoneOffset.ofHours(12)),
	PGT("PGT", "Papua New Guinea Time", ZoneOffset.ofHours(10)),
	PHOT("PHOT", "Phoenix Island Time", ZoneOffset.ofHours(13)),
	PHT("PHT", "Philippine Time", ZoneOffset.ofHours(8)),
	PHST("PHST", "Philippine Standard Time", ZoneOffset.ofHours(8)),
	PKT("PKT", "Pakistan Standard Time", ZoneOffset.ofHours(5)),
	PMDT("PMDT", "Saint Pierre and Miquelon Daylight Time", ZoneOffset.ofHours(-2)),
	PMST("PMST", "Saint Pierre and Miquelon Standard Time", ZoneOffset.ofHours(-3)),
	PONT("PONT", "Pohnpei Standard Time", ZoneOffset.ofHours(11)),
	PST("PST", "Pacific Standard Time", ZoneOffset.ofHours(-8)),
	PSST("PSST", "Palestine Standard Time", ZoneOffset.ofHours(2)),
	PWT("PWT", "Palau Time", ZoneOffset.ofHours(9)),
	PYST("PYST", "Paraguay Summer Time", ZoneOffset.ofHours(-3)),
	PYT_M4("PYT", "Paraguay Time", ZoneOffset.ofHours(-4)),
	PYT_9("PYT", "Pyongyang Time", ZoneOffset.ofHours(9)),
	RET("RET", "Réunion Time", ZoneOffset.ofHours(4)),
	ROTT("ROTT", "Rothera Research Station Time", ZoneOffset.ofHours(-3)),
	SAKT("SAKT", "Sakhalin Time", ZoneOffset.ofHours(11)),
	SAMT("SAMT", "Samara Time", ZoneOffset.ofHours(4)),
	SAST("SAST", "South African Standard Time", ZoneOffset.ofHours(2)),
	SBT("SBT", "Solomon Islands Time", ZoneOffset.ofHours(11)),
	SCT("SCT", "Seychelles Time", ZoneOffset.ofHours(4)),
	SDT("SDT", "Samoa Daylight Time", ZoneOffset.ofHours(-10)),
	SGT("SGT", "Singapore Time", ZoneOffset.ofHours(8)),
	SLST("SLST", "Sri Lanka Standard Time", ZoneOffset.ofHoursMinutes(5, 30)),
	SRET("SRET", "Srednekolymsk Time", ZoneOffset.ofHours(11)),
	SRT("SRT", "Suriname Time", ZoneOffset.ofHours(-3)),
	SST("SST", "Samoa Standard Time", ZoneOffset.ofHours(-11)),
	SYOT("SYOT", "Showa Station Time", ZoneOffset.ofHours(3)),
	TAHT("TAHT", "Tahiti Time", ZoneOffset.ofHours(-10)),
	THA("THA", "Thailand Standard Time", ZoneOffset.ofHours(7)),
	TFT("TFT", "French Southern and Antarctic Time", ZoneOffset.ofHours(5)),
	TJT("TJT", "Tajikistan Time", ZoneOffset.ofHours(5)),
	TKT("TKT", "Tokelau Time", ZoneOffset.ofHours(13)),
	TLT("TLT", "East Timor Time", ZoneOffset.ofHours(9)),
	TMT("TMT", "Turkmenistan Time", ZoneOffset.ofHours(5)),
	TRT("TRT", "Turkey Time", ZoneOffset.ofHours(3)),
	TOT("TOT", "Tonga Time", ZoneOffset.ofHours(13)),
	TST("TST", "Taiwan Standard Time", ZoneOffset.ofHours(8)),
	TVT("TVT", "Tuvalu Time", ZoneOffset.ofHours(12)),
	ULAST("ULAST", "Ulaanbaatar Summer Time", ZoneOffset.ofHours(9)),
	ULAT("ULAT", "Ulaanbaatar Time", ZoneOffset.ofHours(8)),
	UTC("UTC", "Coordinated Universal Time", ZoneOffset.ofHours(0)),
	UYST("UYST", "Uruguay Summer Time", ZoneOffset.ofHours(-2)),
	UYT("UYT", "Uruguay Standard Time", ZoneOffset.ofHours(-3)),
	UZT("UZT", "Uzbekistan Time", ZoneOffset.ofHours(5)),
	VET("VET", "Venezuelan Standard Time", ZoneOffset.ofHours(-4)),
	VLAT("VLAT", "Vladivostok Time", ZoneOffset.ofHours(10)),
	VOLT("VOLT", "Volgograd Time", ZoneOffset.ofHours(3)),
	VUT("VUT", "Vanuatu Time", ZoneOffset.ofHours(11)),
	WAKT("WAKT", "Wake Island Time", ZoneOffset.ofHours(12)),
	WAST("WAST", "West Africa Summer Time", ZoneOffset.ofHours(2)),
	WAT("WAT", "West Africa Time", ZoneOffset.ofHours(1)),
	WEST("WEST", "Western European Summer Time", ZoneOffset.ofHours(1)),
	WET("WET", "Western European Time", ZoneOffset.ofHours(0)),
	WIB("WIB", "Western Indonesian Time", ZoneOffset.ofHours(7)),
	WIT("WIT", "Eastern Indonesian Time", ZoneOffset.ofHours(9)),
	WITA("WITA", "Central Indonesian Time", ZoneOffset.ofHours(8)),
	WGST("WGST", "Western Greenland Summer Time", ZoneOffset.ofHours(-2)),
	WGT("WGT", "Western Greenland Time", ZoneOffset.ofHours(-3)),
	WST("WST", "Western Sahara Summer Time", ZoneOffset.ofHours(8)),
	YAKT("YAKT", "Yakutsk Time", ZoneOffset.ofHours(9)),
	YEKT("YEKT", "Yekaterinburg Time", ZoneOffset.ofHours(5));

    /**
     * A derived property that provides a `ZoneId` instance based on the `id` of the `TimeZone`.
     * If the `id` is not recognized as a valid Zone ID, this property returns `null`.
     *
     * This property attempts to resolve the current `id` to a valid `ZoneId` using the `ZoneId.of` method.
     * If an exception occurs during this process (e.g., the `id` is invalid), the result will be `null`.
     *
     * 
     * @return A `ZoneId` representing the time zone, or `null` if the `id` is invalid or cannot be resolved.
     * @since 1.0.0
     */
    val zoneIdByAbbreviation: ZoneId?
        get() {
            return try {
                ZoneId.of(id)
            } catch (_: Exception) {
                null
            }
        }

    /**
     * Provides the [ZoneId] corresponding to the [ZoneIdent.offset].
     * If the [ZoneId] cannot be resolved, it returns `null`.
     *
     * This is a computed property that attempts to derive the [ZoneId] based on the [offset] associated
     * with the current [ZoneIdent] object.
     *
     * 
     * @return The [ZoneId] corresponding to the [offset], or `null` if the [offset] cannot be resolved.
     * @since 1.0.0
     */
    val zoneIdByOffset: ZoneId?
        get() {
    		return try {
    			ZoneId.of(offset.id)
    		} catch (_: Exception) {
                null
            }
    }

    /**
     * Retrieves the zone ID associated with the `TimeZone` instance. The value is resolved in the
     * following order of precedence:
     * 1. The `zoneIdByAbbreviation` field, if available.
     * 2. The `zoneIdByOffset` field, if available.
     * 3. Throws an `IllegalStateException` if neither of the above are defined for the instance.
     *
     * This property provides a platform-specific representation of the time zone and is integral to
     * conversions and operations requiring time zone adjustments.
     *
     * 
     * @return A `ZoneId` representing the time zone.
     * @throws IllegalStateException if the `id` is not recognized as a valid Zone ID and neither of the
     * `zoneIdByAbbreviation` or `zoneIdByOffset` fields are defined for the current instance.
     * @since 1.0.0
     */
    override val zoneId: ZoneId
        get() = zoneIdByAbbreviation ?: zoneIdByOffset ?: throw IllegalStateException("ZoneId not found for $this")

    /**
     * Represents the UTC offset as a string for the current `MilitaryTimeZone` instance.
     *
     * The returned value includes the "UTC" prefix followed by the `ZoneOffset` identifier,
     * or only "UTC" for instances representing the zero offset (`ZoneOffset.UTC`).
     *
     * @return A string representation of the UTC offset, prefixed with "UTC".
     * @since 1.0.0
     */
    @Suppress("IDENTITY_SENSITIVE_OPERATIONS_WITH_VALUE_TYPE")
    override val utcOffset: String
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
        get() = TimeZoneDesignator.from(this)

    /**
     * Retrieves the name of the enum representation of the current time zone.
     * This property is overridden to return the `name` field of the associated enum.
     *
     * 
     * @return The name of the enum representation.
     * @since 1.0.0
     */
    override val enumName: String
        get() = name

    companion object {
        /**
         * Retrieves a set of time zones based on the provided identifier.
         *
         * @param id A string representing the identifier of the desired time zone(s).
         * @return A set of time zones that match the specified identifier. If the identifier is "Z", the set contains only the UTC time zone.
         * @since 1.0.0
         */
        infix fun of(id: String): Set<TimeZone> {
            if (id == "Z") return setOf(UTC)

            val result = mutableSetOf<TimeZone>()
            for (timeZone in entries) {
                if (timeZone.id == +id) {
                    result.add(timeZone)
                }
            }
            return result
        }

        /**
         * Finds a `TimeZone` instance based on its zone name.
         *
         * @param name The name of the time zone to search for.
         * @return The `TimeZone` instance with the matching zone name, or `null` if no match is found.
         * @since 1.0.0
         */
        infix fun ofName(name: String) = entries.find { it.zoneName == name }

        /**
         * Creates a `TimeZone` instance from the provided `ZoneId`.
         * The method attempts to match the offset of the given `ZoneId` with the system's time zone.
         *
         * @param zoneId The `ZoneId` for which the corresponding `TimeZone` is to be created.
         * @return A `TimeZone` object matching the provided `ZoneId`, or `null` if no match is found.
         * @since 1.0.0
         */
        @Suppress("IDENTITY_SENSITIVE_OPERATIONS_WITH_VALUE_TYPE")
        infix fun from(zoneId: ZoneId): TimeZone? = (of(
            java.util.TimeZone.getTimeZone(zoneId).getDisplayName(java.util.TimeZone.getDefault().inDaylightTime(Date()), java.util.TimeZone.SHORT))
                .find { it.offset == zoneId.rules.getOffset(Instant.now()) }
        )

        /**
         * Filters and retrieves a set of TimeZone instances that match the specified ZoneOffset.
         *
         * @param offset The ZoneOffset to match against the TimeZone instances.
         * @return A set of TimeZone instances that have the specified ZoneOffset.
         * @since 1.0.0
         */
        @Suppress("IDENTITY_SENSITIVE_OPERATIONS_WITH_VALUE_TYPE")
        infix fun byOffset(offset: ZoneOffset): Set<TimeZone> = entries.filter { it.offset == offset }.toSet()

        /**
         * Determines the system's default time zone. If the default time zone cannot be resolved,
         * the fallback provided in the argument `defaultValue` is used. If no defaultValue is supplied
         * and the resolution fails, an exception is thrown.
         *
         * 
         * @param defaultValue An optional fallback `TimeZone` to be returned if the system default cannot be resolved.
         *                     If null, the method will throw an `IllegalStateException` when the system default time zone is not found.
         * @return The system's default time zone or the provided fallback `defaultValue` if applicable.
         * @throws IllegalStateException if no system default time zone is found and no `defaultValue` is provided.
         * @since 1.0.0
         */
        fun systemDefault(defaultValue: TimeZone? = null): TimeZone = (of(java.util.TimeZone.getDefault().getDisplayName(java.util.TimeZone.getDefault().inDaylightTime(Date()), java.util.TimeZone.SHORT))
                ).firstOrNull() ?: (defaultValue ?: throw IllegalStateException("System default time zone not found"))

        /**
         * Generates a list of strings by applying the `prettyPrint` function to all entries in the containing class.
         *
         * @return A list of strings where each string represents the pretty-printed output of an entry.
         * @since 1.0.0
         */
        fun valuesPrettyPrint(): StringList {
            val result = mutableListOf<String>()
            for (timeZone in entries) {
                result.add(timeZone.prettyPrint())
            }
            return result.toList()
        }

        /**
         * Generates and returns a map where the keys correspond to the IDs of time zones
         * and the values are their pretty-printed representations.
         *
         * @return A map with time zone IDs as keys and their pretty-printed string representations as values.
         * @since 1.0.0
         */
        fun prettyMap(): StringMap {
            val result = mutableMapOf<String, String>()
            for (timeZone in entries) {
                result[timeZone.id] = timeZone.prettyPrint()
            }
            return result.toMap()
        }
    }

    /**
     * Returns the string representation of the instance, which is the time zone identifier.
     *
     * The result is typically the zone's unique identifier (such as `Europe/London`) and
     * may vary depending on the implementation.
     *
     * 
     * @return The string representation of the time zone identifier.
     * @since 1.0.0
     */
    override fun toString() = id

    /**
     * Provides a human-readable representation of the TimeZone instance,
     * including its ID, name, and UTC offset.
     *
     * 
     * @return A string representation in the format "id (name) - utcOffset".
     * @since 1.0.0
     */
    override fun prettyPrint(): String = "$id ($name) - $utcOffset"

    /**
     * Determines whether the given temporal field is supported by this time zone.
     *
     * 
     * @param field The temporal field to check for support.
     * @return `true` if the specified field is supported, `false` otherwise.
     * @since 1.0.0
     */
    override fun isSupported(field: TemporalField): Boolean = if (field is ChronoField) field == ChronoField.OFFSET_SECONDS else field.isSupportedBy(this)

    /**
     * Retrieves the value of the specified temporal field from this instance.
     * If the field is `OFFSET_SECONDS`, the total seconds of the offset is returned.
     * If the field is unsupported, an exception is thrown. Otherwise, the field's value
     * is retrieved from this temporal object.
     *
     * 
     * @param field The temporal field to retrieve.
     * @return The value of the specified temporal field.
     * @throws UnsupportedTemporalTypeException If the specified field is not supported.
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
     * Adjusts the specified temporal object to have the same offset as this instance.
     * The adjustment is performed by setting the temporal's `OFFSET_SECONDS` field
     * to the total number of seconds represented by the offset of the current instance.
     *
     * 
     * @param temporal The target temporal object to be adjusted.
     * @return The adjusted temporal object.
     * @since 1.0.0
     */
    override infix fun adjustInto(temporal: Temporal): Temporal = temporal.with(ChronoField.OFFSET_SECONDS, offset.totalSeconds.toLong())

	/**
	 * Converts the current `TimeZone` instance's properties into a map representation.
	 *
	 * This method creates a map where the keys represent the property names and the
	 * values represent their corresponding values for the `TimeZone` instance.
	 *
	 * @return A map containing the `TimeZone` instance's property names as keys and their respective values.
	 * @since 1.0.0
	 */
	@Suppress("functionName")
	private fun _toMap() = mapOf(
		"id" to id,
		"zoneName" to zoneName,
		"offset" to offset,
		"zoneIdByAbbreviation" to zoneIdByAbbreviation,
		"zoneIdByOffset" to zoneIdByOffset,
		"zoneId" to zoneId,
		"utcOffset" to utcOffset,
		"militaryTimeZone" to timeZoneDesignator,
		"enumName" to enumName
	)

	/**
	 * Provides a value from an internal map based on the name of the property.
	 *
	 * This operator function retrieves a value associated with the given property name from an internal map.
	 * The type of the value is cast to the type parameter `R`.
	 *
	 * - `id` - TYPE: [String]
	 * - `zoneName` - TYPE: [String]
	 * - `offset` - TYPE: [ZoneOffset]
	 * - `zoneIdByAbbreviation` - TYPE: `ZoneId?`
	 * - `zoneIdByOffset` - TYPE: `ZoneId?`
	 * - `zoneId` - TYPE: [ZoneId]
	 * - `utcOffset` - TYPE: [String]
	 * - `militaryTimeZone` - TYPE: [TimeZoneDesignator]
	 * - `enumName` - TYPE: [String]
	 *
	 * @param R The type to which the value will be cast.
	 * @param thisRef The reference to the object for which this property is retrieved. Can be null.
	 * @param property The metadata for the property being accessed, used to obtain its name.
	 * @return The value from the internal map associated with the property name, cast to type `R`.
	 * @since 1.0.0
	 */
	@Suppress("unchecked_cast")
	override operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}