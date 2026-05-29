/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.money

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.constants.*
import dev.tommasop1804.kutils.classes.constants.TextCase.Companion.convertCase
import dev.tommasop1804.kutils.classes.geography.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.reflect.KProperty

/**
 * Represents a currency with associated properties and methods to interact with its representation.
 *
 * @property currencyName The full name of the currency (e.g., "United States Dollar").
 * @property numericCode The numeric code assigned to the currency (e.g., 840 for USD).
 * @property symbol The symbol used to represent the currency (e.g., "$" for USD).
 * @property fractionalUnit The name of the fractional unit for the currency (e.g., "cent").
 * @property numberOfFractionalUnits The number of fractional units in one main unit of the currency.
 * @property digitsAfterDecimalPoint The number of digits after the decimal point, representing precision.
 * @property countries The list of countries where the currency is used.
 * @property code The ISO 4217 alphabetic code representing the currency (e.g., "USD").
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "kutils_collection_declaration")
enum class Currency (
	val currencyName: String,
	val numericCode: String,
	val symbol: String2,
	val fractionalUnit: String?,
	val numberOfFractionalUnits: Int,
	val digitsAfterDecimalPoint: Int,
	val countries: Set<Country>
) {
    AED("United Arab Emirates dirham", "784", Pair("Dh", "Dhs"), "Fils", 100, 2, setOf(Country.UnitedArabEmirates)),
	AFN("Afghan afghani", "971", "؋", "Pul", setOf(Country.Afghanistan)),
	ALL("Albanian lek", "008", "L", "Qintar", setOf(Country.Albania)),
	AMD("Armenian dram", "051", "֏", "Luma", setOf(Country.Armenia)),
	ANG("Netherlands Antillean guilder", "532", "Cg", "Cent", setOf(Country.Curacao, Country.SintMaarten)),
	AOA("Angolan kwanza", "973", "Kz", "Cêntimo", setOf(Country.Angola)),
	ARS("Argentine peso", "032", "$", "Centavo", setOf(Country.Argentina)),
	AUD("Australian dollar", "036", "$", "Cent", setOf(Country.Australia, Country.ChristmasIsland, Country.CocosIslands, Country.HeardIslandAndMcdonaldIslands, Country.Kiribati, Country.Nauru, Country.NorfolkIsland, Country.Tuvalu)),
	AWG("Aruba florin", "533", "ƒ", "Cent", setOf(Country.Aruba)),
	AZN("Azerbaikani manat", "944", "₼", "Qəpik", setOf(Country.Azerbaijan)),
	BAM("Bosnia and Herzegovina convertible mark", "977", "KM", "Fening", setOf(Country.BosniaAndHerzegovina)),
	BBD("Barbados dollar", "052", "$", "Cent", setOf(Country.Barbados)),
	BDT("Bangladeshi taka", "050", "৳", "Poisha", setOf(Country.Bangladesh)),
	BGN("Bulgarian lev", "975", "lv.", "Stotinka", setOf(Country.Bulgaria)),
	BHD("Bahraini dinar", "048", "BD", "Fils", 1000, 3, setOf(Country.Bahrain)),
	BIF("Burundian franc", "108", "FBu", "Centime", 100, 0, setOf(Country.Burundi)),
	BMD("Bermudin dollar", "060", "$", "Cent", setOf(Country.Bermuda)),
	BND("Brunei dollar", "096", "$", "Sen", setOf(Country.Brunei)),
	BOB("Bolivian boliviano", "068", "Bs", "Centavo", setOf(Country.Bolivia)),
	BRL("Brazilian real", "986", "R$", "Centavo", setOf(Country.Brazil)),
	BSD("Bahamian dollar", "044", "$", "Cent", setOf(Country.Bahamas)),
	BTN("Bhutanese ngultrum", "064", "Nu", "Chetrum", setOf(Country.Bhutan)),
	BWP("Botswana pula", "072", "P", "Thebe", setOf(Country.Botswana)),
	BYN("Belarusian ruble", "933", "Br", "Kopeck", setOf(Country.Belarus)),
	BZD("Belize dollar", "084", "$", "Cent", setOf(Country.Belize)),
	CAD("Canadian dollar", "124", "$", "Cent", setOf(Country.Canada)),
	CDF("Congolese franc", "976", "FC", "Centime", setOf(Country.DemocraticRepublicOfTheCongo)),
	CHF("Swiss franc", "756", "Fr", "Rappen", setOf(Country.Switzerland, Country.Liechtenstein)),
	CLP("Chilean peso", "152", "$", "Centavo", 100, 0, setOf(Country.Chile)),
	CNY("Renminbi", "156", "¥", "Jiao", 10, 2, setOf(Country.China)),
	COP("Colombian peso", "170", "$", "Centavo", 100, 0, setOf(Country.Colombia)),
	CRC("Costa Rican colón", "188", "₡", "Céntimo", setOf(Country.CostaRica)),
	CUP("Cuban peso", "192", "$", "Centavo", setOf(Country.Cuba)),
	CVE("Cape Verdean escudo", "132", "$", "Centavo", setOf(Country.CaboVerde)),
	CZK("Czech koruna", "203", "Kč", "Haléř", setOf(Country.CzechRepublic)),
	DJF("Djiboutian franc", "262", "Fdj", "Centime", 100, 0, setOf(Country.Djibouti)),
	DKK("Danish krone", "208", "kr", "Øre", setOf(Country.Denmark, Country.FaroeIslands, Country.Greenland)),
	DOP("Dominican peso", "214", "RD$", "Centavo", setOf(Country.DominicanRepublic)),
	DZD("Algerian dinar", "012", "DA", "Centime", setOf(Country.Algeria)),
	EGP("Egyptian pound", "818", "£", "Piastre", setOf(Country.Egypt)),
	ERN("Eritrean nakfa", "232", "Nfk", "Cent", setOf(Country.Eritrea)),
	ETB("Ethiopian birr", "230", "Br", "Santim", setOf(Country.Ethiopia)),
	EUR("Euro", "978", "€", "Cent", setOf(Country.AlandIsland, Country.Andorra, Country.Austria, Country.Belgium, Country.Croatia, Country.Cyprus, Country.Estonia, Country.Finland, Country.France, Country.FrenchGuiana, Country.Germany, Country.Greece, Country.Guadeloupe, Country.Ireland, Country.Italy, Country.Latvia, Country.Lithuania, Country.Luxembourg, Country.Malta, Country.Martinique, Country.Mayotte, Country.Monaco, Country.Montenegro, Country.Netherlands, Country.Portugal, Country.Reunion, Country.SaintBarthelemy, Country.SaintMartin, Country.SaintPierreAndMiquelon, Country.SanMarino, Country.Slovakia, Country.Slovenia, Country.Spain, Country.VaticanCity)),
	FJD("Fiji dollar", "242", "$", "Cent", setOf(Country.Fiji)),
	FKP("Falkland Islands pound", "238", "£", "Penny", setOf(Country.FalklandIslands)),
	GBP("Pound sterling", "826", "£", "Penny", setOf(Country.UnitedKingdom, Country.Guernsey, Country.IsleOfMan, Country.Jersey, Country.SaintHelenaAscensionAndTristanDaCunha)),
	GEL("Georgian lari", "981", "₾", "Tetri", setOf(Country.Georgia)),
	GHS("Ghanaian cedi", "936", "₵", "Pesewa", setOf(Country.Ghana)),
	GIP("Gibraltar pound", "292", "£", "Penny", setOf(Country.Gibraltar)),
	GMD("Gambian dalasi", "270", "D", "Butut", setOf(Country.Gambia)),
	GNF("Guinean franc", "324", "Fr", "Centime", 100, 0, setOf(Country.Guinea)),
	GTQ("Guatemalan quetzal", "320", "Q", "Centavo", setOf(Country.Guatemala)),
	GYD("Guyanese dollar", "328", "$", "Cent", setOf(Country.Guyana)),
	HKD("Hong Kong dollar", "344", "$", "Cent", setOf(Country.HongKong)),
	HNL("Honduran lempira", "340", "L", "Centavo", setOf(Country.Honduras)),
	HTG("Haitian gourde", "332", "G", "Centime", setOf(Country.Haiti)),
	HUF("Hungarian forint", "348", "Ft", "Fillér", 100, 2, setOf(Country.Hungary)),
	IDR("Indonesian rupiah", "360", "Rp", "Sen", setOf(Country.Indonesia)),
	ILS("Israeli new shekel", "376", "₪", "Agora", setOf(Country.Israel)),
	INR("Indian rupee", "356", "₹", "Paisa", setOf(Country.India)),
	IQD("Iraqi dinar", "368", "ID", "Fils", 1000, 3, setOf(Country.Iraq)),
	IRR("Iranian rial", "364", Pair("Rl", "Rls"), "Rial", 100, 2, setOf(Country.Iran)),
	ISK("Icelandic króna", "352", "kr", "Eyrir", 100, 0, setOf(Country.Iceland)),
	JMD("Jamaican dollar", "388", "$", "Cent", setOf(Country.Jamaica)),
	JOD("Jordanian dinar", "400", "JD", "Piastre", 100, 3, setOf(Country.Jordan)),
	JPY("Japanese yen", "392", "¥", "Sen", 100, 0, setOf(Country.Japan)),
	KES("Kenyan shilling", "404", Pair("Sh", "Shs"), "Cent", 100, 2, setOf(Country.Kenya)),
	KGS("Kyrgyzstani som", "417", "⃀", "Tyiyn", setOf(Country.Kyrgyzstan)),
	KHR("Cambodian riel", "116", "៛", "Sen", setOf(Country.Cambodia)),
	KMF("Comorian franc", "174", "FC", "Centime", 100, 0, setOf(Country.Comoros)),
	KPW("North Korean won", "408", "₩", "Chon", setOf(Country.NorthKorea)),
	KRW("South Korean won", "410", "₩", "Jeon", 100, 0, setOf(Country.SouthKorea)),
	KWD("Kuwaiti dinar", "414", "KD", "Fils", 1000, 3, setOf(Country.Kuwait)),
	KYD("Cayman Islands dollar", "136", "$", "Cent", setOf(Country.CaymanIslands)),
	KZT("Kazakhstani tenge", "398", "₸", "Tiyn", setOf(Country.Kazakhstan)),
	LAK("Lao kip", "418", "₭", "Att", setOf(Country.Laos)),
	LBP("Lebanese pound", "422", "LL", "Piastre", setOf(Country.Lebanon)),
	LKR("Sri Lankan rupee", "144", Pair("Re", "Rs"), "Cent", 100, 2, setOf(Country.SriLanka)),
	LRD("Liberian dollar", "430", "$", "Cent", setOf(Country.Liberia)),
	LSL("Lesotho loti", "426", Pair("L", "M"), "Sente", 100, 2, setOf(Country.Lesotho)),
	LYD("Libyan dinar", "434", "LD", "Dirham", 1000, 3, setOf(Country.Libya)),
	MAD("Moroccan dirham", "504", "DH", "Centime", setOf(Country.Morocco, Country.WesternSahara)),
	MDL("Moldovan leu", "498", Pair("Leu", "Lei"), "Ban", 100, 2, setOf(Country.Moldova)),
	MGA("Malagasy ariary", "969", "Ar", "Iraimbilanja", setOf(Country.Madagascar)),
	MKD("Macedonian denar", "807", "DEN", "Deni", setOf(Country.NorthMacedonia)),
	MMK("Myanmar kyat", "104", Pair("K", "Ks"), "Pya", 100, 2, setOf(Country.Myanmar)),
	MNT("Mongolian tögrög", "496", "₮", "Möngö", setOf(Country.Mongolia)),
	MOP("Macanese pataca", "446", "$", "Avo", setOf(Country.Macao)),
	MRU("Mauritanian ouguiya", "929", "UM", "Khoums", setOf(Country.Mauritania)),
	MUR("Mauritian rupee", "480", Pair("Re", "Rs"), "Cent", 100, 2, setOf(Country.Mauritius)),
	MVR("Maldivian rufiyaa", "462", "Rf", "Laari", setOf(Country.Maldives)),
	MWK("Malawian kwacha", "454", "MK", "Tambala", setOf(Country.Malawi)),
	MXN("Mexican peso", "484", "$", "Centavo", setOf(Country.Mexico)),
	MYR("Malaysian ringgit", "458", "RM", "Sen",setOf(Country.Malaysia)),
	MZN("Mozambican metical", "943", "Mt", "Centavo", setOf(Country.Mozambique)),
	NAD("Namibian dollar", "516", "$", "Cent", setOf(Country.Namibia)),
	NGN("Nigerian naira", "566", "₦", "Kobo", setOf(Country.Nigeria)),
	NIO("Nicaraguan córdoba", "558", "C$", "Centavo", setOf(Country.Nicaragua)),
	NOK("Norwegian krone", "578", "kr", "Øre", setOf(Country.Norway, Country.BouvetIsland)),
	NPR("Nepalese rupee", "524", "रु", "Paisa", setOf(Country.Nepal)),
	NZD("New Zealand dollar", "554", "$", "Cent", setOf(Country.NewZealand, Country.CookIslands, Country.Niue, Country.PitcairnIslands, Country.Tokelau)),
	OMR("Omani rial", "512", "RO", "Baisa", 1000, 3, setOf(Country.Oman)),
	PAB("Panamanian balboa", "590", "B/", "Centésimo", setOf(Country.Panama)),
	PEN("Peruvian sol", "604", "S/", "Céntimo", setOf(Country.Peru)),
	PGK("Papua New Guinean kina", "598", "K", "Toea", setOf(Country.PapuaNewGuinea)),
	PHP("Philippine peso", "608", "₱", "Sentimo", setOf(Country.Philippines)),
	PKR("Pakistani rupee", "586", Pair("Re", "Rs"), "Paisa", 100, 2, setOf(Country.Pakistan)),
	PLN("Polish złoty", "985", "zł", "Grosz", setOf(Country.Poland)),
	PYG("Paraguayan guaraní", "600", "QR", "Céntimo", 100, 0, setOf(Country.Paraguay)),
	QAR("Qatari riyal", "634", "QR", "Dirham", setOf(Country.Qatar)),
	RON("Romanian leu", "946", Pair("Leu", "Lei"), "Ban", 100, 2, setOf(Country.Romania)),
	RSD("Serbian dinar", "941", "DIN", "Para", setOf(Country.Serbia)),
	RUB("Russian ruble", "643", "₽", "Kopeck", setOf(Country.RussianFederation)),
	RWF("Rwadan franc", "646", "FRw", "Centime", 100, 0, setOf(Country.Rwanda)),
	SAR("Saudi riyal", "682", "﷼", "Halala", setOf(Country.SaudiArabia)),
	SBD("Solomon Islands dollar", "090", "$", "Cent", setOf(Country.SolomonIslands)),
	SCR("Seychelles rupee", "690", Pair("Re", "Rs"), "Cent", 100, 2, setOf(Country.Seychelles)),
	SDG("Sudanese pound", "938", "LS", "Piastre", setOf(Country.Sudan)),
	SEK("Swedish krona", "752", "kr", "Öre", setOf(Country.Sweden)),
	SGD("Singapore dollar", "702", "$", "Cent", setOf(Country.Singapore)),
	SHP("Saint Helena Pound", "654", "£", "Penny", setOf(Country.SaintHelenaAscensionAndTristanDaCunha)),
	SLE("Sierra Leonean leone", "925", "Le", "Cent", setOf(Country.SierraLeone)),
	SOS("Somalian shilling", "706", Pair("Sh", "Shs"), "Cent", 100, 2, setOf(Country.Somalia)),
	SRD("Surimanese dollar", "968", "$", "Cent", setOf(Country.Suriname)),
	SSP("South Sudanese pound", "728", "SS£", "Piaster", setOf(Country.Sudan)),
	STN("São Tomé and Príncipe dobra", "930", "Db", "Cêntimo", setOf(Country.SaoTomeAndPrincipe)),
	SVC("Salvadoran colón", "222", "₡", "Centavo", setOf(Country.ElSalvador)),
	SYP("Syrian pound", "760", "LS", "Piastre", setOf(Country.Syria)),
	SZL("Swazi lilangeni", "748", Pair("L", "E"), "Cent", 100, 2, setOf(Country.Eswatini)),
	THB("Thai baht", "764", "฿", "Satang", setOf(Country.Thailand)),
	TJS("Tajikistani somoni", "972", "SM", "Diram", setOf(Country.Tajikistan)),
	TMT("Turkmenistan manat", "934", "m", "Tenge", setOf(Country.Turkmenistan)),
	TND("Tunisian dinar", "788", "DT", "Millime", 1000, 3, setOf(Country.Tunisia)),
	TOP("Tongan pa'anga", "776", "T$", "Seniti", setOf(Country.Tonga)),
	TRY("Turkish lira", "949", "₺", "Kuruş", setOf(Country.Turkey)),
	TTD("Trinidad and Tobago dollar", "780", "$", "Cent", setOf(Country.TrinidadAndTobago)),
	TWD("New Taiwan dollar", "901", "$", "Cent", setOf(Country.Taiwan)),
	TZS("Tanzanian shilling", "834", Pair("Sh", "Shs"), "Cent", 100, 2, setOf(Country.Tanzania)),
	UAH("Ukrainian hryvnia", "980", "₴", "Kopeck", setOf(Country.Ukraine)),
	UGX("Ugandan shilling", "800", Pair("Sh", "Shs"), null, 0, 0, setOf(Country.Uganda)),
	USD("United States dollar", "840", "$", "Cent", setOf(Country.UnitedStates, Country.AmericanSamoa, Country.BritishIndianOceanTerritory, Country.BritishVirginIslands, Country.BonaireSintEustatiusAndSaba, Country.Ecuador, Country.ElSalvador, Country.Guam, Country.MarshallIslands, Country.Micronesia, Country.NorthernMarianaIslands, Country.Palau, Country.Panama, Country.PuertoRico, Country.TimorLeste, Country.TurksAndCaicosIslands, Country.UsVirginIslands, Country.UnitedStatesMinorOutlyingIslands)),
	UYU("Uruguayan peso", "940", "$", "Centésimo", setOf(Country.Uruguay)),
	UZS("Uzbekistani sum", "860", "S", "Tiyin", setOf(Country.Uzbekistan)),
	VED("Venezuelan digital bolivar", "926", "Bs.D", "Céntimo", setOf(Country.Venezuela)),
	VES("Venezuelan sovereign bolivar", "928", "Bs.S", "Céntimo", setOf(Country.Venezuela)),
	VND("Vietnamese đồng", "704", "₫", "Hào", 10, 0, setOf(Country.Vietnam)),
	VUV("Vanuatu vatu", "548", "VT", "Cent", 100, 0, setOf(Country.Vanuatu)),
	WST("Samoan tālā", "882", "$", "Sene", setOf(Country.Samoa)),
	XAF("CFA franc BEAC", "950", "F.CFA", "Centime", 100, 0, setOf(Country.Cameroon, Country.CentralAfricanRepublic, Country.Congo, Country.Chad, Country.EquatorialGuinea, Country.Gabon)),
	XCD("East Caribbean dollar", "951", "EC$", "Cent", setOf(Country.Anguilla, Country.AntiguaAndBarbuda, Country.Dominica, Country.Grenada, Country.Montserrat, Country.SaintKittsAndNevis, Country.SaintLucia, Country.SaintVincentAndTheGrenadines)),
	XOF("CFA franc BCEAO", "952", "F.CFA", "Centime", 100, 0, setOf(Country.Benin, Country.BurkinaFaso, Country.CoteDIvorie, Country.GuineaBissau, Country.Mali, Country.Niger, Country.Senegal, Country.Togo)),
	XPF("CFP franc", "953", "₣", "Centime", 100, 0, setOf(Country.FrenchPolynesia, Country.NewCaledonia, Country.WallisAndFutuna)),
	YER("Yemeni rial", "886", Pair("Rl", "Rls"), "Fils", 100, 2, setOf(Country.Yemen)),
	ZAR("South African rand", "710", "R", "Cent", setOf(Country.SouthAfrica, Country.Eswatini, Country.Lesotho, Country.Namibia)),
	ZMW("Zambian kwatcha", "967", "K", "Ngwee", setOf(Country.Zambia)),
	ZWG("Zimbabwe Gold", "924", "ZiG", "Cent", setOf(Country.Zimbabwe));

    constructor(currencyName: String, numericCode: String, symbol: String, fractionalUnit: String?, numberOfFractionalUnits: Int, digitsAfterDecimalPoint: Int, countries: Set<Country>) :
            this(currencyName, numericCode, Pair(symbol, symbol), fractionalUnit, numberOfFractionalUnits, digitsAfterDecimalPoint, countries)

    constructor(currencyName: String, numericCode: String, symbol: String, fractionalUnit: String?, countries: Set<Country>) :
            this(currencyName, numericCode, Pair(symbol, symbol), fractionalUnit, 100, 2, countries)

    /**
	 * A property that retrieves the `name` as its value.
	 *
	 * 
	 * @return The `name` value as a string.
	 * @since 1.0.0
	 */
	val code: String
        get() = name

	companion object {
        /**
		 * Retrieves an entry based on the provided ISO code. The method matches the ISO code with
		 * existing entries by name, ignoring case, or falls back to attempt retrieval by numeric ISO code.
		 *
		 * @param iso The ISO code to find the corresponding entry.
		 * @return The matching entry for the given ISO code.
		 * @since 1.0.0
		 */
		infix fun of(iso: String) = entries.firstOr({ ofNumeric(iso) }) { it?.name == +iso }

        /**
		 * Finds an entry in the collection with the specified numeric code.
		 *
		 * @param numericCode The numeric code of the entry to be located.
		 * @return The entry matching the specified numeric code, or null if no match is found.
		 * @since 1.0.0
		 */
		infix fun ofNumeric(numericCode: String) = entries.find { it.numericCode == numericCode }

        /**
		 * Retrieves a Currency instance corresponding to the provided numeric code.
		 *
		 * @param numericCode The three-digit numeric code of the currency, ranging from 000 to 999.
		 * @throws ValidationFailedException if the number is out of range
		 * @return The matching Currency instance if found, or null if no match exists.
		 * @since 1.0.0
		 */
		infix fun ofNumeric(numericCode: Int): Currency? {
			validate(numericCode in 0..999) { "Invalid numeric code: $numericCode" }
            return entries.firstOrNull { it.numericCode == String.format("%03d", numericCode) }
        }

		/**
		 * Retrieves a list of entries where the currency name matches the provided name,
		 * with consideration for the specified text case.
		 *
		 * @param name The name to match against the currency names in the entries.
		 * @param textCase The text case to apply to the provided name, defaulting to TextCase.STANDARD.
		 * @since 1.0.0
		 */
		fun byName(name: String, textCase: TextCase = TextCase.Standard) = entries
			.filter { (name.convertCase(textCase, TextCase.LowerCase)) == -it.currencyName }

		/**
		 * Filters and retrieves a list of entries that match the given symbol.
		 *
		 * @param symbol The symbol used to filter the entries. Matches either the first or second symbol of an entry.
		 * @return A list of entries where the symbol matches the first or second symbol of the entry.
		 * @since 1.0.0
		 */
		infix fun bySymbol(symbol: String) = entries.filter { it.symbol.first == symbol || it.symbol.second == symbol }

		/**
		 * Filters and returns a list of entries where the fractional unit matches the provided string.
		 *
		 * @param fractionalUnit The fractional unit string to match. The matching is case-insensitive and `null` is treated as a non-matching value.
		 * @since 1.0.0
		 */
		infix fun byFractionalUnit(fractionalUnit: String?) = entries.filter { it.fractionalUnit == fractionalUnit?.lowercase() }

		/**
		 * Filters and returns a list of entries where the number of fractional units matches the given value.
		 *
		 * @param numberOfFractionalUnits The specific number of fractional units used to filter the entries.
		 * @return A filtered list of entries whose number of fractional units matches the provided value.
		 * @since 1.0.0
		 */
		infix fun byNumberOfFractionalUnits(numberOfFractionalUnits: Int) = entries.filter { it.numberOfFractionalUnits == numberOfFractionalUnits }

		/**
		 * Filters a collection of entries, returning only those that have the specified number
		 * of digits after the decimal point.
		 *
		 * @param digitsAfterDecimalPoint The number of digits to match against the entries' decimal precision.
		 * @return A filtered list of entries matching the specified number of digits after the decimal point.
		 * @since 1.0.0
		 */
		infix fun byDigitsAfterDecimalPoint(digitsAfterDecimalPoint: Int) = entries.filter { it.digitsAfterDecimalPoint == digitsAfterDecimalPoint }

		/**
		 * Filters the entries to include only those associated with the given country.
		 *
		 * @param country The country to filter the entries by.
		 * @return A list of entries associated with the specified country.
		 * @since 1.0.0
		 */
		infix fun byCountry(country: Country) = entries.filter { country in it.countries }

		/**
		 * Filters the entries to only retain those whose associated countries are entirely contained in the given list of countries.
		 *
		 * @param countries An iterable of `Country` objects against which the entries will be matched.
		 * @since 1.0.0
		 */
		infix fun byCountries(countries: Iterable<Country>) = entries.filter { countries.toList().containsAll(it.countries) }

		/**
		 * Creates an instance or executes an operation using the provided list of countries.
		 *
		 * @param countries Vararg parameter representing the countries to be utilized.
		 * @since 1.0.0
		 */
		fun byCountries(vararg countries: Country) = byCountries(countries.toList())

		/**
		 * Converts a `java.util.Currency` instance into a constant currency representation
		 * of the Custom `Currency` class using its ISO-4217 currency code.
		 *
		 * @receiver The `java.util.Currency` instance to be converted.
		 * @return The constant currency representation of the given `Currency` using the ISO code.
		 * @since 1.0.0
		 */
		fun java.util.Currency.toConstCurrency() = of(this.currencyCode)
    }

	/**
	 * Converts the current currency name into a Java `Currency` object, if applicable.
	 *
	 * 
	 * @return The corresponding Java `Currency` object if the currency name is valid, or `null` otherwise.
	 * @since 1.0.0
	 */
	fun toJavaCurrency(): java.util.Currency? = tryOrNull { java.util.Currency.getInstance(name) }

	/**
	 * Converts the properties of the `Currency` class into a map representation.
	 *
	 * @return A map containing key-value pairs representing the `Currency` class fields.
	 * Each key corresponds to the field name, and the associated value is the field's value.
	 *
	 * @since 1.0.0
	 */
	@Suppress("functionName")
	private fun _toMap() = mapOf(
		"code" to code,
		"currencyName" to currencyName,
		"numericCode" to numericCode,
		"symbol" to symbol,
		"fractionalUnit" to fractionalUnit,
		"numberOfFractionalUnits" to numberOfFractionalUnits,
		"digitsAfterDecimalPoint" to digitsAfterDecimalPoint,
		"countries" to countries
	)

	/**
	 * Retrieves the value of a property from a map representation of the containing object.
	 *
	 * - `code` corresponds to the `Currency.code` property - TYPE: [String].
	 * - `currencyName` corresponds to the `Currency.currencyName` property - TYPE: [String].
	 * - `numericCode` corresponds to the `Currency.numericCode` property - TYPE: [String].
	 * - `symbol` corresponds to the `Currency.symbol` property - TYPE: `String2`.
	 * - `fractionalUnit` corresponds to the `Currency.fractionalUnit` property - TYPE: [String].
	 * - `numberOfFractionalUnits` corresponds to the `Currency.numberOfFractionalUnits` property - TYPE: [Int].
	 * - `digitsAfterDecimalPoint` corresponds to the `Currency.digitsAfterDecimalPoint` property - TYPE: [Int].
	 * - `countries` corresponds to the `Currency.countries` property - TYPE: `Set<Country>`.
	 *
	 * @param thisRef The reference to the object where this delegate is used. Can be null.
	 * @param property The metadata for the property being accessed.
	 * @return The value associated with the property name, casted to the desired type R.
	 * @since 1.0.0
	 */
	@Suppress("unchecked_cast")
	operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
	
	/**
	 * Provides the first component for destructuring declarations in a currency-related context.
	 * This operator enables extracting the `currencyName` property as the first component 
	 * when using destructuring syntax with the associated object.
	 *
	 * @return The name of the currency represented by this component.
	 * @since 3.1.0
	 */
	operator fun component1() = currencyName
	/**
	 * Provides the value of the second component of the data class, typically representing
	 * the `code` property of the object. This function enables destructuring declarations
	 * to access the second element of the data structure directly.
	 *
	 * @return The value of the `code` property.
	 * @since 3.1.0
	 */
	operator fun component2() = code
	/**
	 * Provides the third component of a data class decomposition.
	 * 
	 * This method is typically used for destructuring declarations, 
	 * enabling the retrieval of the `numericCode` property in 
	 * scenarios where destructuring is supported.
	 *
	 * @return The `numericCode` value associated with this instance.
	 * @since 3.1.0
	 */
	operator fun component3() = numericCode
	/**
	 * Provides the fourth component of the deconstructed data structure.
	 *
	 * This operator function allows the fourth value of the object to be accessed 
	 * using Kotlin's destructuring declarations. When used in a `destructuring declaration`,
	 * it represents the `symbol` property of the object.
	 *
	 * @return The fourth component represented by `symbol`.
	 * @since 3.1.0
	 */
	operator fun component4() = symbol
	/**
	 * Provides the fifth component of a destructured object.
	 *
	 * This operator function is typically used in destructuring declarations
	 * to retrieve the fractional unit value stored in the object.
	 *
	 * @return The fractional unit associated with the object.
	 * 
	 * @since 3.1.0
	 */
	operator fun component5() = fractionalUnit
	/**
	 * Decomposes and provides the sixth component of the current instance 
	 * when used in a destructuring declaration.
	 *
	 * This operator function is specifically implemented to retrieve the 
	 * `numberOfFractionalUnits` property, which generally represents the 
	 * fractional or sub-unit count associated with the instance.
	 *
	 * @return The value of `numberOfFractionalUnits`.
	 *
	 * @since 3.1.0
	 */
	operator fun component6() = numberOfFractionalUnits
	/**
	 * Provides access to the seventh component of a data structure.
	 * This method is typically used for destructuring declarations.
	 *
	 * @return The value stored in the `digitsAfterDecimalPoint` property.
	 * @since 3.1.0
	 */
	operator fun component7() = digitsAfterDecimalPoint
	/**
	 * Operator function `component8` that enables destructuring declaration syntax
	 * to retrieve the `countries` property as the eighth component of an object.
	 *
	 * This function is specifically used in conjunction with destructuring to 
	 * access this property in a concise manner.
	 *
	 * @return The `countries` property associated with the eighth component.
	 *
	 * @since 3.1.0
	 */
	operator fun component8() = countries
}
