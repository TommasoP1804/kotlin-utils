package dev.tommasop1804.kutils.classes.money

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.constants.TextCase
import dev.tommasop1804.kutils.classes.constants.TextCase.Companion.convertCase
import dev.tommasop1804.kutils.classes.geography.Country
import dev.tommasop1804.kutils.exceptions.ValidationFailedException
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
    AED("United Arab Emirates dirham", "784", Pair("Dh", "Dhs"), "Fils", 100, 2, setOf(Country.UNITED_ARAB_EMIRATES)),
	AFN("Afghan afghani", "971", "؋", "Pul", setOf(Country.AFGHANISTAN)),
	ALL("Albanian lek", "008", "L", "Qintar", setOf(Country.ALBANIA)),
	AMD("Armenian dram", "051", "֏", "Luma", setOf(Country.ARMENIA)),
	ANG("Netherlands Antillean guilder", "532", "Cg", "Cent", setOf(Country.CURACAO, Country.SINT_MAARTEN)),
	AOA("Angolan kwanza", "973", "Kz", "Cêntimo", setOf(Country.ANGOLA)),
	ARS("Argentine peso", "032", "$", "Centavo", setOf(Country.ARGENTINA)),
	AUD("Australian dollar", "036", "$", "Cent", setOf(Country.AUSTRALIA, Country.CHRISTMAS_ISLAND, Country.COCOS_ISLANDS, Country.HEARD_ISLAND_AND_MCDONALD_ISLANDS, Country.KIRIBATI, Country.NAURU, Country.NORFOLK_ISLAND, Country.TUVALU)),
	AWG("Aruba florin", "533", "ƒ", "Cent", setOf(Country.ARUBA)),
	AZN("Azerbaikani manat", "944", "₼", "Qəpik", setOf(Country.AZERBAIJAN)),
	BAM("Bosnia and Herzegovina convertible mark", "977", "KM", "Fening", setOf(Country.BOSNIA_AND_HERZEGOVINA)),
	BBD("Barbados dollar", "052", "$", "Cent", setOf(Country.BARBADOS)),
	BDT("Bangladeshi taka", "050", "৳", "Poisha", setOf(Country.BANGLADESH)),
	BGN("Bulgarian lev", "975", "lv.", "Stotinka", setOf(Country.BULGARIA)),
	BHD("Bahraini dinar", "048", "BD", "Fils", 1000, 3, setOf(Country.BAHRAIN)),
	BIF("Burundian franc", "108", "FBu", "Centime", 100, 0, setOf(Country.BURUNDI)),
	BMD("Bermudin dollar", "060", "$", "Cent", setOf(Country.BERMUDA)),
	BND("Brunei dollar", "096", "$", "Sen", setOf(Country.BRUNEI)),
	BOB("Bolivian boliviano", "068", "Bs", "Centavo", setOf(Country.BOLIVIA)),
	BRL("Brazilian real", "986", "R$", "Centavo", setOf(Country.BRAZIL)),
	BSD("Bahamian dollar", "044", "$", "Cent", setOf(Country.BAHAMAS)),
	BTN("Bhutanese ngultrum", "064", "Nu", "Chetrum", setOf(Country.BHUTAN)),
	BWP("Botswana pula", "072", "P", "Thebe", setOf(Country.BOTSWANA)),
	BYN("Belarusian ruble", "933", "Br", "Kopeck", setOf(Country.BELARUS)),
	BZD("Belize dollar", "084", "$", "Cent", setOf(Country.BELIZE)),
	CAD("Canadian dollar", "124", "$", "Cent", setOf(Country.CANADA)),
	CDF("Congolese franc", "976", "FC", "Centime", setOf(Country.DEMOCRATIC_REPUBLIC_OF_THE_CONGO)),
	CHF("Swiss franc", "756", "Fr", "Rappen", setOf(Country.SWITZERLAND, Country.LIECHTENSTEIN)),
	CLP("Chilean peso", "152", "$", "Centavo", 100, 0, setOf(Country.CHILE)),
	CNY("Renminbi", "156", "¥", "Jiao", 10, 2, setOf(Country.CHINA)),
	COP("Colombian peso", "170", "$", "Centavo", 100, 0, setOf(Country.COLOMBIA)),
	CRC("Costa Rican colón", "188", "₡", "Céntimo", setOf(Country.COSTA_RICA)),
	CUP("Cuban peso", "192", "$", "Centavo", setOf(Country.CUBA)),
	CVE("Cape Verdean escudo", "132", "$", "Centavo", setOf(Country.CABO_VERDE)),
	CZK("Czech koruna", "203", "Kč", "Haléř", setOf(Country.CZECH_REPUBLIC)),
	DJF("Djiboutian franc", "262", "Fdj", "Centime", 100, 0, setOf(Country.DJIBOUTI)),
	DKK("Danish krone", "208", "kr", "Øre", setOf(Country.DENMARK, Country.FAROE_ISLANDS, Country.GREENLAND)),
	DOP("Dominican peso", "214", "RD$", "Centavo", setOf(Country.DOMINICAN_REPUBLIC)),
	DZD("Algerian dinar", "012", "DA", "Centime", setOf(Country.ALGERIA)),
	EGP("Egyptian pound", "818", "£", "Piastre", setOf(Country.EGYPT)),
	ERN("Eritrean nakfa", "232", "Nfk", "Cent", setOf(Country.ERITREA)),
	ETB("Ethiopian birr", "230", "Br", "Santim", setOf(Country.ETHIOPIA)),
	EUR("Euro", "978", "€", "Cent", setOf(Country.ALAND_ISLAND, Country.ANDORRA, Country.AUSTRIA, Country.BELGIUM, Country.CROATIA, Country.CYPRUS, Country.ESTONIA, Country.FINLAND, Country.FRANCE, Country.FRENCH_GUIANA, Country.GERMANY, Country.GREECE, Country.GUADELOUPE, Country.IRELAND, Country.ITALY, Country.LATVIA, Country.LITHUANIA, Country.LUXEMBOURG, Country.MALTA, Country.MARTINIQUE, Country.MAYOTTE, Country.MONACO, Country.MONTENEGRO, Country.NETHERLANDS, Country.PORTUGAL, Country.REUNION, Country.SAINT_BARTHELEMY, Country.SAINT_MARTIN, Country.SAINT_PIERRE_AND_MIQUELON, Country.SAN_MARINO, Country.SLOVAKIA, Country.SLOVENIA, Country.SPAIN, Country.VATICAN_CITY)),
	FJD("Fiji dollar", "242", "$", "Cent", setOf(Country.FIJI)),
	FKP("Falkland Islands pound", "238", "£", "Penny", setOf(Country.FALKLAND_ISLANDS)),
	GBP("Pound sterling", "826", "£", "Penny", setOf(Country.UNITED_KINGDOM, Country.GUERNSEY, Country.ISLE_OF_MAN, Country.JERSEY, Country.SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA)),
	GEL("Georgian lari", "981", "₾", "Tetri", setOf(Country.GEORGIA)),
	GHS("Ghanaian cedi", "936", "₵", "Pesewa", setOf(Country.GHANA)),
	GIP("Gibraltar pound", "292", "£", "Penny", setOf(Country.GIBRALTAR)),
	GMD("Gambian dalasi", "270", "D", "Butut", setOf(Country.GAMBIA)),
	GNF("Guinean franc", "324", "Fr", "Centime", 100, 0, setOf(Country.GUINEA)),
	GTQ("Guatemalan quetzal", "320", "Q", "Centavo", setOf(Country.GUATEMALA)),
	GYD("Guyanese dollar", "328", "$", "Cent", setOf(Country.GUYANA)),
	HKD("Hong Kong dollar", "344", "$", "Cent", setOf(Country.HONG_KONG)),
	HNL("Honduran lempira", "340", "L", "Centavo", setOf(Country.HONDURAS)),
	HTG("Haitian gourde", "332", "G", "Centime", setOf(Country.HAITI)),
	HUF("Hungarian forint", "348", "Ft", "Fillér", 100, 2, setOf(Country.HUNGARY)),
	IDR("Indonesian rupiah", "360", "Rp", "Sen", setOf(Country.INDONESIA)),
	ILS("Israeli new shekel", "376", "₪", "Agora", setOf(Country.ISRAEL)),
	INR("Indian rupee", "356", "₹", "Paisa", setOf(Country.INDIA)),
	IQD("Iraqi dinar", "368", "ID", "Fils", 1000, 3, setOf(Country.IRAQ)),
	IRR("Iranian rial", "364", Pair("Rl", "Rls"), "Rial", 100, 2, setOf(Country.IRAN)),
	ISK("Icelandic króna", "352", "kr", "Eyrir", 100, 0, setOf(Country.ICELAND)),
	JMD("Jamaican dollar", "388", "$", "Cent", setOf(Country.JAMAICA)),
	JOD("Jordanian dinar", "400", "JD", "Piastre", 100, 3, setOf(Country.JORDAN)),
	JPY("Japanese yen", "392", "¥", "Sen", 100, 0, setOf(Country.JAPAN)),
	KES("Kenyan shilling", "404", Pair("Sh", "Shs"), "Cent", 100, 2, setOf(Country.KENYA)),
	KGS("Kyrgyzstani som", "417", "⃀", "Tyiyn", setOf(Country.KYRGYZSTAN)),
	KHR("Cambodian riel", "116", "៛", "Sen", setOf(Country.CAMBODIA)),
	KMF("Comorian franc", "174", "FC", "Centime", 100, 0, setOf(Country.COMOROS)),
	KPW("North Korean won", "408", "₩", "Chon", setOf(Country.NORTH_KOREA)),
	KRW("South Korean won", "410", "₩", "Jeon", 100, 0, setOf(Country.SOUTH_KOREA)),
	KWD("Kuwaiti dinar", "414", "KD", "Fils", 1000, 3, setOf(Country.KUWAIT)),
	KYD("Cayman Islands dollar", "136", "$", "Cent", setOf(Country.CAYMAN_ISLANDS)),
	KZT("Kazakhstani tenge", "398", "₸", "Tiyn", setOf(Country.KAZAKHSTAN)),
	LAK("Lao kip", "418", "₭", "Att", setOf(Country.LAOS)),
	LBP("Lebanese pound", "422", "LL", "Piastre", setOf(Country.LEBANON)),
	LKR("Sri Lankan rupee", "144", Pair("Re", "Rs"), "Cent", 100, 2, setOf(Country.SRI_LANKA)),
	LRD("Liberian dollar", "430", "$", "Cent", setOf(Country.LIBERIA)),
	LSL("Lesotho loti", "426", Pair("L", "M"), "Sente", 100, 2, setOf(Country.LESOTHO)),
	LYD("Libyan dinar", "434", "LD", "Dirham", 1000, 3, setOf(Country.LIBYA)),
	MAD("Moroccan dirham", "504", "DH", "Centime", setOf(Country.MOROCCO, Country.WESTERN_SAHARA)),
	MDL("Moldovan leu", "498", Pair("Leu", "Lei"), "Ban", 100, 2, setOf(Country.MOLDOVA)),
	MGA("Malagasy ariary", "969", "Ar", "Iraimbilanja", setOf(Country.MADAGASCAR)),
	MKD("Macedonian denar", "807", "DEN", "Deni", setOf(Country.NORTH_MACEDONIA)),
	MMK("Myanmar kyat", "104", Pair("K", "Ks"), "Pya", 100, 2, setOf(Country.MYANMAR)),
	MNT("Mongolian tögrög", "496", "₮", "Möngö", setOf(Country.MONGOLIA)),
	MOP("Macanese pataca", "446", "$", "Avo", setOf(Country.MACAO)),
	MRU("Mauritanian ouguiya", "929", "UM", "Khoums", setOf(Country.MAURITANIA)),
	MUR("Mauritian rupee", "480", Pair("Re", "Rs"), "Cent", 100, 2, setOf(Country.MAURITIUS)),
	MVR("Maldivian rufiyaa", "462", "Rf", "Laari", setOf(Country.MALDIVES)),
	MWK("Malawian kwacha", "454", "MK", "Tambala", setOf(Country.MALAWI)),
	MXN("Mexican peso", "484", "$", "Centavo", setOf(Country.MEXICO)),
	MYR("Malaysian ringgit", "458", "RM", "Sen",setOf(Country.MALAYSIA)),
	MZN("Mozambican metical", "943", "Mt", "Centavo", setOf(Country.MOZAMBIQUE)),
	NAD("Namibian dollar", "516", "$", "Cent", setOf(Country.NAMIBIA)),
	NGN("Nigerian naira", "566", "₦", "Kobo", setOf(Country.NIGERIA)),
	NIO("Nicaraguan córdoba", "558", "C$", "Centavo", setOf(Country.NICARAGUA)),
	NOK("Norwegian krone", "578", "kr", "Øre", setOf(Country.NORWAY, Country.BOUVET_ISLAND)),
	NPR("Nepalese rupee", "524", "रु", "Paisa", setOf(Country.NEPAL)),
	NZD("New Zealand dollar", "554", "$", "Cent", setOf(Country.NEW_ZEALAND, Country.COOK_ISLANDS, Country.NIUE, Country.PITCAIRN_ISLANDS, Country.TOKELAU)),
	OMR("Omani rial", "512", "RO", "Baisa", 1000, 3, setOf(Country.OMAN)),
	PAB("Panamanian balboa", "590", "B/", "Centésimo", setOf(Country.PANAMA)),
	PEN("Peruvian sol", "604", "S/", "Céntimo", setOf(Country.PERU)),
	PGK("Papua New Guinean kina", "598", "K", "Toea", setOf(Country.PAPUA_NEW_GUINEA)),
	PHP("Philippine peso", "608", "₱", "Sentimo", setOf(Country.PHILIPPINES)),
	PKR("Pakistani rupee", "586", Pair("Re", "Rs"), "Paisa", 100, 2, setOf(Country.PAKISTAN)),
	PLN("Polish złoty", "985", "zł", "Grosz", setOf(Country.POLAND)),
	PYG("Paraguayan guaraní", "600", "QR", "Céntimo", 100, 0, setOf(Country.PARAGUAY)),
	QAR("Qatari riyal", "634", "QR", "Dirham", setOf(Country.QATAR)),
	RON("Romanian leu", "946", Pair("Leu", "Lei"), "Ban", 100, 2, setOf(Country.ROMANIA)),
	RSD("Serbian dinar", "941", "DIN", "Para", setOf(Country.SERBIA)),
	RUB("Russian ruble", "643", "₽", "Kopeck", setOf(Country.RUSSIAN_FEDERATION)),
	RWF("Rwadan franc", "646", "FRw", "Centime", 100, 0, setOf(Country.RWANDA)),
	SAR("Saudi riyal", "682", "﷼", "Halala", setOf(Country.SAUDI_ARABIA)),
	SBD("Solomon Islands dollar", "090", "$", "Cent", setOf(Country.SOLOMON_ISLANDS)),
	SCR("Seychelles rupee", "690", Pair("Re", "Rs"), "Cent", 100, 2, setOf(Country.SEYCHELLES)),
	SDG("Sudanese pound", "938", "LS", "Piastre", setOf(Country.SUDAN)),
	SEK("Swedish krona", "752", "kr", "Öre", setOf(Country.SWEDEN)),
	SGD("Singapore dollar", "702", "$", "Cent", setOf(Country.SINGAPORE)),
	SHP("Saint Helena Pound", "654", "£", "Penny", setOf(Country.SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA)),
	SLE("Sierra Leonean leone", "925", "Le", "Cent", setOf(Country.SIERRA_LEONE)),
	SOS("Somalian shilling", "706", Pair("Sh", "Shs"), "Cent", 100, 2, setOf(Country.SOMALIA)),
	SRD("Surimanese dollar", "968", "$", "Cent", setOf(Country.SURINAME)),
	SSP("South Sudanese pound", "728", "SS£", "Piaster", setOf(Country.SUDAN)),
	STN("São Tomé and Príncipe dobra", "930", "Db", "Cêntimo", setOf(Country.SAO_TOME_AND_PRINCIPE)),
	SVC("Salvadoran colón", "222", "₡", "Centavo", setOf(Country.EL_SALVADOR)),
	SYP("Syrian pound", "760", "LS", "Piastre", setOf(Country.SYRIA)),
	SZL("Swazi lilangeni", "748", Pair("L", "E"), "Cent", 100, 2, setOf(Country.ESWATINI)),
	THB("Thai baht", "764", "฿", "Satang", setOf(Country.THAILAND)),
	TJS("Tajikistani somoni", "972", "SM", "Diram", setOf(Country.TAJIKISTAN)),
	TMT("Turkmenistan manat", "934", "m", "Tenge", setOf(Country.TURKMENISTAN)),
	TND("Tunisian dinar", "788", "DT", "Millime", 1000, 3, setOf(Country.TUNISIA)),
	TOP("Tongan pa'anga", "776", "T$", "Seniti", setOf(Country.TONGA)),
	TRY("Turkish lira", "949", "₺", "Kuruş", setOf(Country.TURKEY)),
	TTD("Trinidad and Tobago dollar", "780", "$", "Cent", setOf(Country.TRINIDAD_AND_TOBAGO)),
	TWD("New Taiwan dollar", "901", "$", "Cent", setOf(Country.TAIWAN)),
	TZS("Tanzanian shilling", "834", Pair("Sh", "Shs"), "Cent", 100, 2, setOf(Country.TANZANIA)),
	UAH("Ukrainian hryvnia", "980", "₴", "Kopeck", setOf(Country.UKRAINE)),
	UGX("Ugandan shilling", "800", Pair("Sh", "Shs"), null, 0, 0, setOf(Country.UGANDA)),
	USD("United States dollar", "840", "$", "Cent", setOf(Country.UNITED_STATES, Country.AMERICAN_SAMOA, Country.BRITISH_INDIAN_OCEAN_TERRITORY, Country.BRITISH_VIRGIN_ISLANDS, Country.BONAIRE_SINT_EUSTATIUS_AND_SABA, Country.ECUADOR, Country.EL_SALVADOR, Country.GUAM, Country.MARSHALL_ISLANDS, Country.MICRONESIA, Country.NORTHERN_MARIANA_ISLANDS, Country.PALAU, Country.PANAMA, Country.PUERTO_RICO, Country.TIMOR_LESTE, Country.TURKS_AND_CAICOS_ISLANDS, Country.US_VIRGIN_ISLANDS, Country.UNITED_STATES_MINOR_OUTLYING_ISLANDS)),
	UYU("Uruguayan peso", "940", "$", "Centésimo", setOf(Country.URUGUAY)),
	UZS("Uzbekistani sum", "860", "S", "Tiyin", setOf(Country.UZBEKISTAN)),
	VED("Venezuelan digital bolivar", "926", "Bs.D", "Céntimo", setOf(Country.VENEZUELA)),
	VES("Venezuelan sovereign bolivar", "928", "Bs.S", "Céntimo", setOf(Country.VENEZUELA)),
	VND("Vietnamese đồng", "704", "₫", "Hào", 10, 0, setOf(Country.VIETNAM)),
	VUV("Vanuatu vatu", "548", "VT", "Cent", 100, 0, setOf(Country.VANUATU)),
	WST("Samoan tālā", "882", "$", "Sene", setOf(Country.SAMOA)),
	XAF("CFA franc BEAC", "950", "F.CFA", "Centime", 100, 0, setOf(Country.CAMEROON, Country.CENTRAL_AFRICAN_REPUBLIC, Country.CONGO, Country.CHAD, Country.EQUATORIAL_GUINEA, Country.GABON)),
	XCD("East Caribbean dollar", "951", "EC$", "Cent", setOf(Country.ANGUILLA, Country.ANTIGUA_AND_BARBUDA, Country.DOMINICA, Country.GRENADA, Country.MONTSERRAT, Country.SAINT_KITTS_AND_NEVIS, Country.SAINT_LUCIA, Country.SAINT_VINCENT_AND_THE_GRENADINES)),
	XOF("CFA franc BCEAO", "952", "F.CFA", "Centime", 100, 0, setOf(Country.BENIN, Country.BURKINA_FASO, Country.COTE_D_IVORIE, Country.GUINEA_BISSAU, Country.MALI, Country.NIGER, Country.SENEGAL, Country.TOGO)),
	XPF("CFP franc", "953", "₣", "Centime", 100, 0, setOf(Country.FRENCH_POLYNESIA, Country.NEW_CALEDONIA, Country.WALLIS_AND_FUTUNA)),
	YER("Yemeni rial", "886", Pair("Rl", "Rls"), "Fils", 100, 2, setOf(Country.YEMEN)),
	ZAR("South African rand", "710", "R", "Cent", setOf(Country.SOUTH_AFRICA, Country.ESWATINI, Country.LESOTHO, Country.NAMIBIA)),
	ZMW("Zambian kwatcha", "967", "K", "Ngwee", setOf(Country.ZAMBIA)),
	ZWG("Zimbabwe Gold", "924", "ZiG", "Cent", setOf(Country.ZIMBABWE));

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
		fun byName(name: String, textCase: TextCase = TextCase.STANDARD) = entries
			.filter { (name.convertCase(textCase, TextCase.LOWER_CASE)) == -it.currencyName }

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
}
