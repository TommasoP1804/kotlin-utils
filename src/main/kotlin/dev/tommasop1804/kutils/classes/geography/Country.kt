package dev.tommasop1804.kutils.classes.geography

import dev.tommasop1804.kutils.StringList
import dev.tommasop1804.kutils.classes.time.TimeZone
import dev.tommasop1804.kutils.classes.time.TimeZoneDesignator
import dev.tommasop1804.kutils.classes.time.ZoneIdent
import dev.tommasop1804.kutils.classes.translators.Translator
import dev.tommasop1804.kutils.equalsIgnoreCase
import dev.tommasop1804.kutils.mappedTo
import dev.tommasop1804.kutils.unaryMinus
import dev.tommasop1804.kutils.unaryPlus
import java.time.ZoneOffset
import java.util.*
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.text.startsWith

/**
 * Represents a country with various attributes such as alpha codes, numeric codes,
 * names, time zones, and related locale information.
 *
 * @property alpha2 The two-letter country code based on ISO 3166-1 alpha-2.
 * @property alpha3 The three-letter country code based on ISO 3166-1 alpha-3.
 * @property numeric The numeric country code based on ISO 3166-1 numeric-3.
 * @property countryName The common name of the country or state.
 * @property officialName The official name of the country or state.
 * @property tlds The list of top-level domains associated with the country.
 * @property locale The locale identifier for the country.
 * @property timeZones A list of time zones available for the country.
 * @property timeZoneDesignators A list of military time zone designations for the country.
 * @property zoneOffsets A list of GMT offsets for the country's time zones.
 * @property zoneUtcOffsets A list of UTC offsets for the country's time zones.
 * @property phoneCodes The country's phone codes.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
enum class Country(
	val alpha2: String,
	val alpha3: String,
	val numeric: String,
	val countryName: String,
	val officialName: String,
	val tlds: StringList,
	val locale: Locale,
	val timeZones: List<ZoneIdent>,
	val phoneCodes: StringList
) {
    AFGHANISTAN("AF", "AFG", "004", "Afghanistan", "The Islamic Republic of Afghanistan", listOf(".af"), Locale.of("fa", "AF"), listOf(TimeZone.AFT), listOf("93")),
	ALAND_ISLAND("AX", "ALA", "248", "Åland Islands", "Åland", listOf(".ax"), Locale.of("sv", "AX"), listOf(TimeZone.EET), listOf("358 (18)")),
	ALBANIA("AL", "ALB", "008", "Albania", "The Republic of Albania", listOf(".al"), Locale.of("sq", "AL"), listOf(TimeZone.CET), listOf("355")),
	ALGERIA("DZ", "DZA", "012", "Algeria", "The People's Democratic Republic of Algeria", listOf(".dz"), Locale.of("ar", "DZ"), listOf(TimeZone.CET), listOf("213")),
	AMERICAN_SAMOA("AS", "ASM", "016", "American Samoa", "American Samoa", listOf(".as"), Locale.of("en", "AS"), listOf(TimeZone.SST), listOf("1 (684)")),
	ANDORRA("AD", "AND", "020", "Andorra", "The Principality of Andorra", listOf(".ad"), Locale.of("ca", "AD"), listOf(TimeZone.CET), listOf("376")),
	ANGOLA("AO", "AGO", "024", "Angola", "The Republic of Angola", listOf(".ao"), Locale.of("pt", "AO"), listOf(TimeZone.WAT), listOf("244")),
	ANGUILLA("AI", "AIA", "660", "Anguilla", "Anguilla", listOf(".ai"), Locale.of("en", "AI"), listOf(TimeZone.AST_M4), listOf("1 (264)")),
	ANTARCTICA("AQ", "ATA", "010", "Antarctica", "Antarctica", listOf(".aq"), Locale.of("en", "AQ"), listOf(TimeZone.ART, TimeZone.GMT, TimeZoneDesignator.C, TimeZoneDesignator.E, TimeZoneDesignator.F, TimeZoneDesignator.G, TimeZoneDesignator.H, TimeZoneDesignator.K, TimeZoneDesignator.L, TimeZoneDesignator.M), listOf()),
	ANTIGUA_AND_BARBUDA("AG", "ATG", "028", "Antigua and Barbuda", "Antigua and Barbuda", listOf(".ag"), Locale.of("en", "AG"), listOf(TimeZone.AST_M4), listOf("1 (268)")),
	ARGENTINA("AR", "ARG", "032", "Argentina", "The Argentine Republic", listOf(".ar"), Locale.of("es", "AR"), listOf(TimeZone.ART), listOf("54")),
	ARMENIA("AM", "ARM", "051", "Armenia", "The Republic of Armenia", listOf(".am"), Locale.of("hy", "AM"), listOf(TimeZone.AMT_4), listOf("374")),
	ARUBA("AW", "ABW", "533", "Aruba", "The Country of Aruba", listOf(".aw"), Locale.of("nl", "AW"), listOf(TimeZone.AST_M4), listOf("297")),
	AUSTRALIA("AU", "AUS", "036", "Australia", "The Commonwealth of Australia", listOf(".au"), Locale.of("en", "AU"), listOf(TimeZone.AWST, TimeZoneDesignator.E, TimeZoneDesignator.F_DAGGER, TimeZone.CXT, TimeZone.ACWST, TimeZone.ACST, TimeZone.AEST, TimeZone.ACDT, TimeZone.NFT), listOf("61")),
	AUSTRIA("AT", "AUT", "040", "Austria", "The Republic of Austria", listOf(".at"), Locale.of("de", "AT"), listOf(TimeZone.CET), listOf("43")),
	AZERBAIJAN("AZ", "AZE", "031", "Azerbaijan", "The Republic of Azerbaijan", listOf(".az"), Locale.of("az", "AZ"), listOf(TimeZone.AZT), listOf("994")),
	BAHAMAS("BS", "BHS", "044", "Bahamas", "The Commonwealth of The Bahamas", listOf(".bs"), Locale.of("en", "BS"), listOf(TimeZone.EST), listOf("1 (242)")),
	BAHRAIN("BH", "BHR", "048", "Bahrain", "The Kingdom of Bahrain", listOf(".bh"), Locale.of("ar", "BH"), listOf(TimeZone.AST_3), listOf("973")),
	BANGLADESH("BD", "BGD", "050", "Bangladesh", "The People's Republic of Bangladesh", listOf(".bd"), Locale.of("bn", "BD"), listOf(TimeZone.BST_6), listOf("880")),
	BARBADOS("BB", "BRB", "052", "Barbados", "Barbados", listOf(".bb"), Locale.of("en", "BB"), listOf(TimeZone.AST_M4), listOf("1 (246)")),
	BELARUS("BY", "BLR", "112", "Belarus", "The Republic of Belarus", listOf(".by"), Locale.of("be", "BY"), listOf(TimeZone.FET), listOf("375")),
	BELGIUM("BE", "BEL", "056", "Belgium", "The Kingdom of Belgium", listOf(".be"), Locale.of("nl", "BE"), listOf(TimeZone.CET), listOf("32")),
	BELIZE("BZ", "BLZ", "084", "Belize", "Belize", listOf(".bz"), Locale.of("en", "BZ"), listOf(TimeZone.CST_M6), listOf("501")),
	BENIN("BJ", "BEN", "204", "Benin", "The Republic of Benin", listOf(".bj"), Locale.of("fr", "BJ"), listOf(TimeZone.WAT), listOf("229")),
	BERMUDA("BM", "BMU", "060", "Bermuda", "Bermuda", listOf(".bm"), Locale.of("en", "BM"), listOf(TimeZone.AST_M4), listOf("1 (441)")),
	BHUTAN("BT", "BTN", "064", "Bhutan", "The Kingdom of Bhutan", listOf(".bt"), Locale.of("dz", "BT"), listOf(TimeZone.BTT), listOf("975")),
	BOLIVIA("BO", "BOL", "068", "Bolivia", "The Plurinational State of Bolivia", listOf(".bo"), Locale.of("es", "BO"), listOf(TimeZone.BOT), listOf("591")),
	BONAIRE_SINT_EUSTATIUS_AND_SABA("BQ", "BES", "535", "Bonaire, Sint Eustatius and Saba", "Bonaire, Sint Eustatius and Saba", listOf(".bq", ".nl"), Locale.of("nl", "BQ"), listOf(TimeZone.AST_M4), listOf("599")),
	BOSNIA_AND_HERZEGOVINA("BA", "BIH", "070", "Bosnia and Herzegovina", "Bosnia and Herzegovina", listOf(".ba"), Locale.of("bs", "BA"), listOf(TimeZone.CET), listOf("387")),
	BOTSWANA("BW", "BWA", "072", "Botswana", "The Republic of Botswana", listOf(".bw"), Locale.of("en", "BW"), listOf(TimeZone.CAT), listOf("267")),
	BOUVET_ISLAND("BV", "BVT", "074", "Bouvet Island", "Bouvet Island", listOf(".bv"), Locale.of("no", "BV"), listOf(TimeZone.CET), listOf()),
	BRAZIL("BR", "BRA", "076", "Brazil", "The Federative Republic of Brazil", listOf(".br"), Locale.of("pt", "BR"), listOf(TimeZone.BRT, TimeZoneDesignator.R, TimeZoneDesignator.Q, TimeZoneDesignator.O), listOf("55")),
	BRITISH_INDIAN_OCEAN_TERRITORY("IO", "IOT", "086", "British Indian Ocean Territory", "The British Indian Ocean Territory", listOf(".io"), Locale.of("en", "IO"), listOf(TimeZone.BIOT), listOf("246")),
	BRUNEI("BN", "BRN", "096", "Brunei Darussalam", "The Nation of Brunei, the Abode of Peace", listOf(".bn"), Locale.of("ms", "BN"), listOf(TimeZone.BDT), listOf("673")),
	BULGARIA("BG", "BGR", "100", "Bulgaria", "The Republic of Bulgaria", listOf(".bg"), Locale.of("bg", "BG"), listOf(TimeZone.EET), listOf("359")),
	BURKINA_FASO("BF", "BFA", "854", "Burkina Faso", "Burkina Faso", listOf(".bf"), Locale.of("fr", "BF"), listOf(TimeZone.GMT), listOf("226")),
	BURUNDI("BI", "BDI", "108", "Burundi", "The Republic of Burundi", listOf(".bi"), Locale.of("fr", "BI"), listOf(TimeZone.CAT), listOf("257")),
	CABO_VERDE("CV", "CPV", "132", "Cabo Verde", "The Republic of Cabo Verde", listOf(".cv"), Locale.of("pt", "CV"), listOf(TimeZone.CVT), listOf("238")),
	CAMBODIA("KH", "KHM", "116", "Cambodia", "The Kingdom of Cambodia", listOf(".kh"), Locale.of("km", "KH"), listOf(TimeZone.ICT), listOf("855")),
	CAMEROON("CM", "CMR", "120", "Cameroon", "The Republic of Cameroon", listOf(".cm"), Locale.of("fr", "CM"), listOf(TimeZone.WAT), listOf("237")),
	CANADA("CA", "CAN", "124", "Canada", "Canada", listOf(".ca"), Locale.of("en", "CA"), listOf(TimeZone.EST, TimeZone.PST, TimeZone.MST_M7, TimeZone.CST_M6,TimeZone. AST_M4, TimeZone.NT), listOf("1")),
	CAYMAN_ISLANDS("KY", "CYM", "136", "Cayman Islands", "The Cayman Islands", listOf(".ky"), Locale.of("en", "KY"), listOf(TimeZone.EST), listOf("1 (345)")),
	CENTRAL_AFRICAN_REPUBLIC("CF", "CAF", "140", "Central African Republic", "The Central African Republic", listOf(".cf"), Locale.of("fr", "CF"), listOf(TimeZone.WAT), listOf("236")),
	CHAD("TD", "TCD", "148", "Chad", "The Republic of Chad", listOf(".td"), Locale.of("fr", "TD"), listOf(TimeZone.WAT), listOf("235")),
	CHILE("CL", "CHL", "152", "Chile", "The Republic of Chile", listOf(".cl"), Locale.of("es", "CL"), listOf(TimeZone.CLT), listOf("56")),
	CHINA("CN", "CHN", "156", "China", "The People's Republic of China", listOf(".cn"), Locale.of("zh", "CN"), listOf(TimeZone.CST_8), listOf("86")),
	CHRISTMAS_ISLAND("CX", "CXR", "162", "Christmas Island", "Christmas Island", listOf(".cx"), Locale.of("en", "CX"), listOf(TimeZone.CXT), listOf("61 (8 9164)")),
	COCOS_ISLANDS("CC", "CCK", "166", "Cocos (Keeling) Islands", "The Territory of Cocos (Keeling) Islands", listOf(".cc"), Locale.of("en", "CC"), listOf(TimeZone.CCT), listOf("61 (8 9162)")),
	COLOMBIA("CO", "COL", "170", "Colombia", "The Republic of Colombia", listOf(".co"), Locale.of("es", "CO"), listOf(TimeZone.COT), listOf("57")),
	COMOROS("KM", "COM", "174", "Comoros", "The Union of the Comoros", listOf(".km"), Locale.of("ar", "KM"), listOf(TimeZone.EAT), listOf("269")),
	DEMOCRATIC_REPUBLIC_OF_THE_CONGO("CD", "COD", "180", "Congo (The Democratic Republic of the)", "The Democratic Republic of the Congo", listOf(".cd"), Locale.of("fr", "CD"), listOf(TimeZone.WAT, TimeZone.CAT), listOf("243")),
	CONGO("CG", "COG", "178", "Congo", "The Republic of the Congo", listOf(".cg"), Locale.of("fr", "CG"), listOf(TimeZone.WAT), listOf("242")),
	COOK_ISLANDS("CK", "COK", "184", "Cook Islands", "The Cook Islands", listOf(".ck"), Locale.of("en", "CK"), listOf(TimeZone.CKT), listOf("682")),
	COSTA_RICA("CR", "CRI", "188", "Costa Rica", "The Republic of Costa Rica", listOf(".cr"), Locale.of("es", "CR"), listOf(TimeZone.CST_M6), listOf("506")),
	COTE_D_IVORIE("CI", "CIV", "384", "Côte d'Ivoire", "The Republic of Côte d'Ivoire", listOf(".ci"), Locale.of("fr", "CI"), listOf(TimeZone.GMT), listOf("225")),
	CROATIA("HR", "HRV", "191", "Croatia", "The Republic of Croatia", listOf(".hr"), Locale.of("hr", "HR"), listOf(TimeZone.CET), listOf("385")),
	CUBA("CU", "CUB", "192", "Cuba", "The Republic of Cuba", listOf(".cu"), Locale.of("es", "CU"), listOf(TimeZone.CST_M5), listOf("53")),
	CURACAO("CW", "CUW", "531", "Curaçao", "The Country of Curaçao", listOf(".cw"), Locale.of("nl", "CW"), listOf(TimeZone.AST_M4), listOf("599")),
	CYPRUS("CY", "CYP", "196", "Cyprus", "The Republic of Cyprus", listOf(".cy"), Locale.of("el", "CY"), listOf(TimeZone.EET), listOf("357")),
	CZECH_REPUBLIC("CZ", "CZE", "203", "Czech Republic", "The Czech Republic", listOf(".cz"), Locale.of("cs", "CZ"), listOf(TimeZone.CET), listOf("420")),
	DENMARK("DK", "DNK", "208", "Denmark", "The Kingdom of Denmark", listOf(".dk"), Locale.of("da", "DK"), listOf(TimeZone.CET), listOf("45")),
	DJIBOUTI("DJ", "DJI", "262", "Djibouti", "The Republic of Djibouti", listOf(".dj"), Locale.of("fr", "DJ"), listOf(TimeZone.EAT), listOf("253")),
	DOMINICA("DM", "DMA", "212", "Dominica", "The Commonwealth of Dominica", listOf(".dm"), Locale.of("en", "DM"), listOf(TimeZone.AST_M4), listOf("1 (767)")),
	DOMINICAN_REPUBLIC("DO", "DOM", "214", "Dominican Republic", "The Dominican Republic", listOf(".do"), Locale.of("es", "DO"), listOf(TimeZone.AST_M4), listOf("1 (809)", "1 (829)", "1 (849)")),
	ECUADOR("EC", "ECU", "218", "Ecuador", "The Republic of Ecuador", listOf(".ec"), Locale.of("es", "EC"), listOf(TimeZone.GALT, TimeZone.ECT_M5), listOf("593")),
	EGYPT("EG", "EGY", "818", "Egypt", "The Arab Republic of Egypt", listOf(".eg"), Locale.of("ar", "EG"), listOf(TimeZone.EET), listOf("20")),
	EL_SALVADOR("SV", "SLV", "222", "El Salvador", "The Republic of El Salvador", listOf(".sv"), Locale.of("es", "SV"), listOf(TimeZone.CST_M6), listOf("503")),
	EQUATORIAL_GUINEA("GQ", "GNQ", "226", "Equatorial Guinea", "The Republic of Equatorial Guinea", listOf(".gq"), Locale.of("es", "GQ"), listOf(TimeZone.WAT), listOf("240")),
	ERITREA("ER", "ERI", "232", "Eritrea", "The State of Eritrea", listOf(".er"), Locale.of("ti", "ER"), listOf(TimeZone.EAT), listOf("291")),
	ESTONIA("EE", "EST", "233", "Estonia", "The Republic of Estonia", listOf(".ee"), Locale.of("et", "EE"), listOf(TimeZone.EET), listOf("372")),
	ESWATINI("SZ", "SWZ", "748", "Eswatini", "The Kingdom of Eswatini", listOf(".sz"), Locale.of("ss", "SZ"), listOf(TimeZone.SAST), listOf("268")),
	ETHIOPIA("ET", "ETH", "231", "Ethiopia", "The Federal Democratic Republic of Ethiopia", listOf(".et"), Locale.of("am", "ET"), listOf(TimeZone.EAT), listOf("251")),
	FALKLAND_ISLANDS("FK", "FLK", "238", "Falkland Islands (Malvinas)", "The Falkland Islands", listOf(".fk"), Locale.of("en", "FK"), listOf(TimeZone.FKST), listOf("500")),
	FAROE_ISLANDS("FO", "FRO", "234", "Faroe Islands", "The Faroe Islands", listOf(".fo"), Locale.of("fo", "FO"), listOf(TimeZone.WET), listOf("298")),
	FIJI("FJ", "FJI", "242", "Fiji", "The Republic of Fiji", listOf(".fj"), Locale.of("en", "FJ"), listOf(TimeZone.FJT), listOf("679")),
	FINLAND("FI", "FIN", "246", "Finland", "The Republic of Finland", listOf(".fi"), Locale.of("fi", "FI"), listOf(TimeZone.EET), listOf("358")),
	FRANCE("FR", "FRA", "250", "France", "The French Republic", listOf(".fr"), Locale.of("fr", "FR"), listOf(TimeZone.CET), listOf("33")),
	FRENCH_GUIANA("GF", "GUF", "254", "French Guiana", "Guyane", listOf(".gf"), Locale.of("fr", "GF"), listOf(TimeZone.GFT), listOf("594")),
	FRENCH_POLYNESIA("PF", "PYF", "258", "French Polynesia", "Overseas Lands of French Polynesia", listOf(".pf"), Locale.of("fr", "PF"), listOf(TimeZoneDesignator.W, TimeZoneDesignator.V_DAGGER, TimeZoneDesignator.V), listOf("689")),
	FRENCH_SOUTHERN_TERRITORIES("TF", "ATF", "260", "French Southern Territories", "The French Southern and Antarctic Lands", listOf(".tf"), Locale.of("fr", "TF"), listOf(TimeZone.TFT), listOf()),
	GABON("GA", "GAB", "266", "Gabon", "The Gabonese Republic", listOf(".ga"), Locale.of("fr", "GA"), listOf(TimeZone.WAT), listOf("241")),
	GAMBIA("GM", "GMB", "270", "Gambia", "The Republic of The Gambia", listOf(".gm"), Locale.of("en", "GM"), listOf(TimeZone.GMT), listOf("220")),
	GEORGIA("GE", "GEO", "268", "Georgia", "Georgia", listOf(".ge"), Locale.of("ka", "GE"), listOf(TimeZone.GET), listOf("995")),
	GERMANY("DE", "DEU", "276", "Germany", "The Federal Republic of Germany", listOf(".de"), Locale.of("de", "DE"), listOf(TimeZone.CET), listOf("49")),
	GHANA("GH", "GHA", "288", "Ghana", "The Republic of Ghana", listOf(".gh"), Locale.of("en", "GH"), listOf(TimeZone.GMT), listOf("233")),
	GIBRALTAR("GI", "GIB", "292", "Gibraltar", "Gibraltar", listOf(".gi"), Locale.of("en", "GI"), listOf(TimeZone.CET), listOf("350")),
	GREECE("GR", "GRC", "300", "Greece", "The Hellenic Republic", listOf(".gr"), Locale.of("el", "GR"), listOf(TimeZone.EET), listOf("30")),
	GREENLAND("GL", "GRL", "304", "Greenland", "Greenland", listOf(".gl"), Locale.of("kl", "GL"), listOf(TimeZone.WGST, TimeZone.WGT, TimeZone.EGT, TimeZone.EGST), listOf("299")),
	GRENADA("GD", "GRD", "308", "Grenada", "Grenada", listOf(".gd"), Locale.of("en", "GD"), listOf(TimeZone.AST_M4), listOf("1 (473)")),
	GUADELOUPE("GP", "GLP", "312", "Guadeloupe", "Guadeloupe", listOf(".gp"), Locale.of("fr", "GP"), listOf(TimeZone.AST_M4), listOf("590")),
	GUAM("GU", "GUM", "316", "Guam", "Guam", listOf(".gu"), Locale.of("en", "GU"), listOf(TimeZone.CHST), listOf("1 (671)")),
	GUATEMALA("GT", "GTM", "320", "Guatemala", "The Republic of Guatemala", listOf(".gt"), Locale.of("es", "GT"), listOf(TimeZone.CST_M6), listOf("502")),
	GUERNSEY("GG", "GGY", "831", "Guernsey", "The Bailiwick of Guernsey", listOf(".gg"), Locale.of("en", "GG"), listOf(TimeZone.GMT), listOf("44 (1481)")),
	GUINEA("GN", "GIN", "324", "Guinea", "The Republic of Guinea", listOf(".gn"), Locale.of("fr", "GN"), listOf(TimeZone.GMT), listOf("224")),
	GUINEA_BISSAU("GW", "GNB", "624", "Guinea-Bissau", "The Republic of Guinea-Bissau", listOf(".gw"), Locale.of("pt", "GW"), listOf(TimeZone.GMT), listOf("245")),
	GUYANA("GY", "GUY", "328", "Guyana", "The Co-operative Republic of Guyana", listOf(".gy"), Locale.of("en", "GY"), listOf(TimeZone.GYT), listOf("592")),
	HAITI("HT", "HTI", "332", "Haiti", "The Republic of Haiti", listOf(".ht"), Locale.of("fr", "HT"), listOf(TimeZone.EST), listOf("509")),
	HEARD_ISLAND_AND_MCDONALD_ISLANDS("HM", "HMD", "334", "Heard Island and McDonald Islands", "The Territory of Heard Island and McDonald Islands", listOf(".hm"), Locale.of("en", "HM"), listOf(TimeZone.TFT), listOf()),
	HONDURAS("HN", "HND", "340", "Honduras", "The Republic of Honduras", listOf(".hn"), Locale.of("es", "HN"), listOf(TimeZone.CST_M6), listOf("504")),
	HONG_KONG("HK", "HKG", "344", "Hong Kong", "Hong Kong", listOf(".hk"), Locale.of("zh", "HK"), listOf(TimeZone.HKT), listOf("852")),
	HUNGARY("HU", "HUN", "348", "Hungary", "Hungary", listOf(".hu"), Locale.of("hu", "HU"), listOf(TimeZone.CET), listOf("36")),
	ICELAND("IS", "ISL", "352", "Iceland", "Iceland", listOf(".is"), Locale.of("is", "IS"), listOf(TimeZone.GMT), listOf("354")),
	INDIA("IN", "IND", "356", "India", "The Republic of India", listOf(".in"), Locale.of("hi", "IN"), listOf(TimeZone.IST_5_30), listOf("91")),
	INDONESIA("ID", "IDN", "360", "Indonesia", "The Republic of Indonesia", listOf(".id"), Locale.of("id", "ID"), listOf(TimeZone.WIB, TimeZone.WITA, TimeZone.WIT), listOf("62")),
	IRAN("IR", "IRN", "364", "Iran", "The Islamic Republic of Iran", listOf(".ir"), Locale.of("fa", "IR"), listOf(TimeZone.IRST), listOf("98")),
	IRAQ("IQ", "IRQ", "368", "Iraq", "The Republic of Iraq", listOf(".iq"), Locale.of("ar", "IQ"), listOf(TimeZone.AST_3), listOf("964")),
	IRELAND("IE", "IRL", "372", "Ireland", "Ireland", listOf(".ie"), Locale.of("ga", "IE"), listOf(TimeZone.WET), listOf("353")),
	ISLE_OF_MAN("IM", "IMN", "833", "Isle of Man", "The Isle of Man", listOf(".im"), Locale.of("en", "IM"), listOf(TimeZone.GMT), listOf("44 (1624)")),
	ISRAEL("IL", "ISR", "376", "Israel", "The State of Israel", listOf(".il"), Locale.of("he", "IL"), listOf(TimeZone.IST_2), listOf("972")),
	ITALY("IT", "ITA", "380", "Italy", "The Italian Republic", listOf(".it"), Locale.of("it", "IT"), listOf(TimeZone.CET), listOf("39")),
	JAMAICA("JM", "JAM", "388", "Jamaica", "Jamaica", listOf(".jm"), Locale.of("en", "JM"), listOf(TimeZone.EST), listOf("1 (658)", "1 (876)")),
	JAPAN("JP", "JPN", "392", "Japan", "Japan", listOf(".jp"), Locale.of("ja", "JP"), listOf(TimeZone.JST), listOf("81")),
	JERSEY("JE", "JEY", "832", "Jersey", "The Bailiwick of Jersey", listOf(".je"), Locale.of("en", "JE"), listOf(TimeZone.GMT), listOf("44 (1534)")),
	JORDAN("JO", "JOR", "400", "Jordan", "The Hashemite Kingdom of Jordan", listOf(".jo"), Locale.of("ar", "JO"), listOf(TimeZoneDesignator.C), listOf("962")),
	KAZAKHSTAN("KZ", "KAZ", "398", "Kazakhstan", "The Republic of Kazakhstan", listOf(".kz"), Locale.of("kk", "KZ"), listOf(TimeZoneDesignator.E), listOf("7")),
	KENYA("KE", "KEN", "404", "Kenya", "The Republic of Kenya", listOf(".ke"), Locale.of("sw", "KE"), listOf(TimeZone.EAT), listOf("254")),
	KIRIBATI("KI", "KIR", "296", "Kiribati", "The Republic of Kiribati", listOf(".ki"), Locale.of("en", "KI"), listOf(TimeZoneDesignator.M, TimeZoneDesignator.M_DAGGER_13, TimeZoneDesignator.M_DOUBLE_DAGGER_14), listOf("686")),
	NORTH_KOREA("KP", "PRK", "408", "Korea (The Democratic People's Republic of)", "The Democratic People's Republic of Korea", listOf(".kp"), Locale.of("ko", "KP"), listOf(TimeZone.PYT_9), listOf("850")),
	SOUTH_KOREA("KR", "KOR", "410", "Korea (The Republic of)", "The Republic of Korea", listOf(".kr"), Locale.of("ko", "KR"), listOf(TimeZone.KST), listOf("82")),
	KOSOVO("XK", "XKX", "410", "Kosovo", "The Republic of Kosovo", listOf(".xk"), Locale.of("sq", "XK"), listOf(TimeZone.CET), listOf("383")),
	KUWAIT("KW", "KWT", "414", "Kuwait", "The State of Kuwait", listOf(".kw"), Locale.of("ar", "KW"), listOf(TimeZone.AST_3), listOf("965")),
	KYRGYZSTAN("KG", "KGZ", "417", "Kyrgyzstan", "The Kyrgyz Republic", listOf(".kg"), Locale.of("ky", "KG"), listOf(TimeZone.KGT), listOf("996")),
	LAOS("LA", "LAO", "418", "Lao People's Democratic Republic", "The Lao People's Democratic Republic", listOf(".la"), Locale.of("lo", "LA"), listOf(TimeZone.ICT), listOf("856")),
	LATVIA("LV", "LVA", "428", "Latvia", "The Republic of Latvia", listOf(".lv"), Locale.of("lv", "LV"), listOf(TimeZone.EET), listOf("371")),
	LEBANON("LB", "LBN", "422", "Lebanon", "The Lebanese Republic", listOf(".lb"), Locale.of("ar", "LB"), listOf(TimeZone.EET), listOf("961")),
	LESOTHO("LS", "LSO", "426", "Lesotho", "The Kingdom of Lesotho", listOf(".ls"), Locale.of("en", "LS"), listOf(TimeZone.SAST), listOf("266")),
	LIBERIA("LR", "LBR", "430", "Liberia", "The Republic of Liberia", listOf(".lr"), Locale.of("en", "LR"), listOf(TimeZone.GMT), listOf("231")),
	LIBYA("LY", "LBY", "434", "Libya", "The State of Libya", listOf(".ly"), Locale.of("ar", "LY"), listOf(TimeZone.EET), listOf("218")),
	LIECHTENSTEIN("LI", "LIE", "438", "Liechtenstein", "The Principality of Liechtenstein", listOf(".li"), Locale.of("de", "LI"), listOf(TimeZone.CET), listOf("423")),
	LITHUANIA("LT", "LTU", "440", "Lithuania", "The Republic of Lithuania", listOf(".lt"), Locale.of("lt", "LT"), listOf(TimeZone.EET), listOf("370")),
	LUXEMBOURG("LU", "LUX", "442", "Luxembourg", "The Grand Duchy of Luxembourg", listOf(".lu"), Locale.of("lb", "LU"), listOf(TimeZone.CET), listOf("352")),
	MACAO("MO", "MAC", "446", "Macao", "The Macao Special Administrative Region of China", listOf(".mo"), Locale.of("zh", "MO"), listOf(TimeZone.MST_8), listOf("853")),
	MADAGASCAR("MG", "MDG", "450", "Madagascar", "The Republic of Madagascar", listOf(".mg"), Locale.of("mg", "MG"), listOf(TimeZone.EAT), listOf("261")),
	MALAWI("MW", "MWI", "454", "Malawi", "The Republic of Malawi", listOf(".mw"), Locale.of("en", "MW"), listOf(TimeZone.CAT), listOf("265")),
	MALAYSIA("MY", "MYS", "458", "Malaysia", "Malaysia", listOf(".my"), Locale.of("ms", "MY"), listOf(TimeZone.MYT), listOf("60")),
	MALDIVES("MV", "MDV", "462", "Maldives", "The Republic of Maldives", listOf(".mv"), Locale.of("dv", "MV"), listOf(TimeZone.MVT), listOf("960")),
	MALI("ML", "MLI", "466", "Mali", "The Republic of Mali", listOf(".ml"), Locale.of("fr", "ML"), listOf(TimeZone.GMT), listOf("223")),
	MALTA("MT", "MLT", "470", "Malta", "Malta", listOf(".mt"), Locale.of("mt", "MT"), listOf(TimeZone.CET), listOf("356")),
	MARSHALL_ISLANDS("MH", "MHL", "584", "Marshall Islands", "The Republic of the Marshall Islands", listOf(".mh"), Locale.of("en", "MH"), listOf(TimeZone.MHT), listOf("691")),
	MARTINIQUE("MQ", "MTQ", "474", "Martinique", "Martinique", listOf(".mq"), Locale.of("fr", "MQ"), listOf(TimeZone.AST_M4), listOf("596")),
	MAURITANIA("MR", "MRT", "478", "Mauritania", "The Islamic Republic of Mauritania", listOf(".mr"), Locale.of("ar", "MR"), listOf(TimeZone.GMT), listOf("222")),
	MAURITIUS("MU", "MUS", "480", "Mauritius", "The Republic of Mauritius", listOf(".mu"), Locale.of("en", "MU"), listOf(TimeZone.MUT), listOf("230")),
	MAYOTTE("YT", "MYT", "175", "Mayotte", "Mayotte", listOf(".yt"), Locale.of("fr", "YT"), listOf(TimeZone.EAT), listOf("262 (269)", "262 (639)")),
	MEXICO("MX", "MEX", "484", "Mexico", "The United Mexican States", listOf(".mx"), Locale.of("es", "MX"), listOf(TimeZone.EST, TimeZone.CST_M6, TimeZone.MST_M7, TimeZone.PST), listOf("52")),
	MICRONESIA("FM", "FSM", "583", "Micronesia (Federated States of)", "The Federated States of Micronesia", listOf(".fm"), Locale.of("en", "FM"), listOf(TimeZoneDesignator.K, TimeZoneDesignator.L), listOf("691")),
	MOLDOVA("MD", "MDA", "498", "Moldova", "The Republic of Moldova", listOf(".md"), Locale.of("ro", "MD"), listOf(TimeZone.FET), listOf("373")),
	MONACO("MC", "MCO", "492", "Monaco", "The Principality of Monaco", listOf(".mc"), Locale.of("fr", "MC"), listOf(TimeZone.CET), listOf("377")),
	MONGOLIA("MN", "MNG", "496", "Mongolia", "Mongolia", listOf(".mn"), Locale.of("mn", "MN"), listOf(TimeZoneDesignator.G, TimeZoneDesignator.H), listOf("976")),
	MONTENEGRO("ME", "MNE", "499", "Montenegro", "Montenegro", listOf(".me"), Locale.of("sr", "ME"), listOf(TimeZone.CET), listOf("382")),
	MONTSERRAT("MS", "MSR", "500", "Montserrat", "Montserrat", listOf(".ms"), Locale.of("en", "MS"), listOf(TimeZone.AST_M4), listOf("1 (664)")),
	MOROCCO("MA", "MAR", "504", "Morocco", "The Kingdom of Morocco", listOf(".ma"), Locale.of("ar", "MA"), listOf(TimeZone.CET), listOf("211")),
	MOZAMBIQUE("MZ", "MOZ", "508", "Mozambique", "The Republic of Mozambique", listOf(".mz"), Locale.of("pt", "MZ"), listOf(TimeZone.CAT), listOf("258")),
	MYANMAR("MM", "MMR", "104", "Myanmar", "The Republic of the Union of Myanmar", listOf(".mm"), Locale.of("my", "MM"), listOf(TimeZone.MMT), listOf("95")),
	NAMIBIA("NA", "NAM", "516", "Namibia", "The Republic of Namibia", listOf(".na"), Locale.of("en", "NA"), listOf(TimeZone.CAT), listOf("264")),
	NAURU("NR", "NRU", "520", "Nauru", "The Republic of Nauru", listOf(".nr"), Locale.of("na", "NR"), listOf(TimeZoneDesignator.M), listOf("674")),
	NEPAL("NP", "NPL", "524", "Nepal", "The Federal Democratic Republic of Nepal", listOf(".np"), Locale.of("ne", "NP"), listOf(TimeZone.NPT), listOf("977")),
	NETHERLANDS("NL", "NLD", "528", "Netherlands", "The Kingdom of the Netherlands", listOf(".nl"), Locale.of("nl", "NL"), listOf(TimeZone.CET, TimeZone.AST_M4), listOf("31")),
	NEW_CALEDONIA("NC", "NCL", "540", "New Caledonia", "New Caledonia", listOf(".nc"), Locale.of("fr", "NC"), listOf(TimeZoneDesignator.L), listOf("687")),
	NEW_ZEALAND("NZ", "NZL", "554", "New Zealand", "New Zealand", listOf(".nz"), Locale.of("en", "NZ"), listOf(TimeZone.NZST), listOf("64")),
	NICARAGUA("NI", "NIC", "558", "Nicaragua", "The Republic of Nicaragua", listOf(".ni"), Locale.of("es", "NI"), listOf(TimeZone.CST_M6), listOf("505")),
	NIGER("NE", "NER", "562", "Niger", "The Republic of the Niger", listOf(".ne"), Locale.of("fr", "NE"), listOf(TimeZone.WAT), listOf("227")),
	NIGERIA("NG", "NGA", "566", "Nigeria", "The Federal Republic of Nigeria", listOf(".ng"), Locale.of("en", "NG"), listOf(TimeZone.WAT), listOf("234")),
	NIUE("NU", "NIU", "570", "Niue", "Niue", listOf(".nu"), Locale.of("en", "NU"), listOf(TimeZoneDesignator.X), listOf("683")),
	NORFOLK_ISLAND("NF", "NFK", "574", "Norfolk Island", "The Territory of Norfolk Island", listOf(".nf"), Locale.of("en", "NF"), listOf(TimeZone.NFT), listOf("672")),
	NORTH_MACEDONIA("MK", "MKD", "807", "North Macedonia", "The Republic of North Macedonia", listOf(".mk"), Locale.of("mk", "MK"), listOf(TimeZone.CET), listOf("389")),
	NORTHERN_MARIANA_ISLANDS("MP", "MNP", "580", "Northern Mariana Islands", "The Commonwealth of the Northern Mariana Islands", listOf(".mp"), Locale.of("en", "MP"), listOf(TimeZone.CHST), listOf("1 (670)")),
	NORWAY("NO", "NOR", "578", "Norway", "The Kingdom of Norway", listOf(".no"), Locale.of("no", "NO"), listOf(TimeZone.CET), listOf("47")),
	OMAN("OM", "OMN", "512", "Oman", "The Sultanate of Oman", listOf(".om"), Locale.of("ar", "OM"), listOf(TimeZone.GST_4), listOf("968")),
	PAKISTAN("PK", "PAK", "586", "Pakistan", "The Islamic Republic of Pakistan", listOf(".pk"), Locale.of("ur", "PK"), listOf(TimeZone.PKT), listOf("92")),
	PALAU("PW", "PLW", "585", "Palau", "The Republic of Palau", listOf(".pw"), Locale.of("en", "PW"), listOf(TimeZone.PWT), listOf("680")),
	PALESTINE("PS", "PSE", "275", "Palestine", "The State of Palestine", listOf(".ps"), Locale.of("ar", "PS"), listOf(TimeZone.PSST), listOf("970", "972")),
	PANAMA("PA", "PAN", "591", "Panama", "The Republic of Panama", listOf(".pa"), Locale.of("es", "PA"), listOf(TimeZone.EST), listOf("507")),
	PAPUA_NEW_GUINEA("PG", "PNG", "598", "Papua New Guinea", "The Independent State of Papua New Guinea", listOf(".pg"), Locale.of("en", "PG"), listOf(TimeZone.PGT), listOf("675")),
	PARAGUAY("PY", "PRY", "600", "Paraguay", "The Republic of Paraguay", listOf(".py"), Locale.of("es", "PY"), listOf(TimeZone.PYT_M4), listOf("595")),
	PERU("PE", "PER", "604", "Peru", "The Republic of Peru", listOf(".pe"), Locale.of("es", "PE"), listOf(TimeZone.PET), listOf("51")),
	PHILIPPINES("PH", "PHL", "608", "Philippines", "The Republic of the Philippines", listOf(".ph"), Locale.of("en", "PH"), listOf(TimeZone.PHT), listOf("63")),
	PITCAIRN_ISLANDS("PN", "PCN", "612", "Pitcairn", "The Pitcairn, Henderson, Ducie and Oeno Islands", listOf(".pn"), Locale.of("en", "PN"), listOf(TimeZoneDesignator.U), listOf("64")),
	POLAND("PL", "POL", "616", "Poland", "The Republic of Poland", listOf(".pl"), Locale.of("pl", "PL"), listOf(TimeZone.CET), listOf("48")),
	PORTUGAL("PT", "PRT", "620", "Portugal", "The Portuguese Republic", listOf(".pt"), Locale.of("pt", "PT"), listOf(TimeZone.WET, TimeZoneDesignator.N), listOf("351")),
	PUERTO_RICO("PR", "PRI", "630", "Puerto Rico", "The Commonwealth of Puerto Rico", listOf(".pr"), Locale.of("es", "PR"), listOf(TimeZone.AST_M4), listOf("1 (787)", "1 (939)")),
	QATAR("QA", "QAT", "634", "Qatar", "The State of Qatar", listOf(".qa"), Locale.of("ar", "QA"), listOf(TimeZone.AST_3), listOf("974")),
	REUNION("RE", "REU", "638", "Réunion", "Réunion", listOf(".re"), Locale.of("fr", "RE"), listOf(TimeZone.RET), listOf("262")),
	ROMANIA("RO", "ROU", "642", "Romania", "Romania", listOf(".ro"), Locale.of("ro", "RO"), listOf(TimeZone.EET), listOf("40")),
	RUSSIAN_FEDERATION("RU", "RUS", "643", "Russian Federation", "The Russian Federation", listOf(".ru"), Locale.of("ru", "RU"), listOf(TimeZone.MSK, TimeZoneDesignator.B, TimeZoneDesignator.C, TimeZoneDesignator.D, TimeZoneDesignator.E, TimeZoneDesignator.F, TimeZoneDesignator.G, TimeZoneDesignator.H, TimeZoneDesignator.I, TimeZoneDesignator.K, TimeZoneDesignator.L, TimeZoneDesignator.I), listOf("7")),
	RWANDA("RW", "RWA", "646", "Rwanda", "The Republic of Rwanda", listOf(".rw"), Locale.of("rw", "RW"), listOf(TimeZone.CAT), listOf("250")),
	SAINT_BARTHELEMY("BL", "BLM", "652", "Saint Barthélemy", "The Collectivity of Saint-Barthélemy", listOf(".bl"), Locale.of("fr", "BL"), listOf(TimeZone.AST_M4), listOf("590")),
	SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA("SH", "SHN", "654", "Saint Helena, Ascension and Tristan da Cunha", "Saint Helena, Ascension and Tristan da Cunha", listOf(".sh"), Locale.of("en", "SH"), listOf(TimeZone.GMT), listOf("247", "290")),
	SAINT_KITTS_AND_NEVIS("KN", "KNA", "659", "Saint Kitts and Nevis", "The Federation of Saint Kitts and Nevis", listOf(".kn"), Locale.of("en", "KN"), listOf(TimeZone.AST_M4), listOf("1 (869)")),
	SAINT_LUCIA("LC", "LCA", "662", "Saint Lucia", "Saint Lucia", listOf(".lc"), Locale.of("en", "LC"), listOf(TimeZone.AST_M4), listOf("1 (758)")),
	SAINT_MARTIN("MF", "MAF", "663", "Saint Martin", "The Collectivity of Saint-Martin", listOf(".mf"), Locale.of("fr", "MF"), listOf(TimeZone.AST_M4), listOf("590")),
	SAINT_PIERRE_AND_MIQUELON("PM", "SPM", "666", "Saint Pierre and Miquelon", "The Overseas Collectivity of Saint Pierre and Miquelon", listOf(".pm"), Locale.of("fr", "PM"), listOf(TimeZoneDesignator.P), listOf("508")),
	SAINT_VINCENT_AND_THE_GRENADINES("VC", "VCT", "670", "Saint Vincent and the Grenadines", "Saint Vincent and the Grenadines", listOf(".vc"), Locale.of("en", "VC"), listOf(TimeZone.AST_M4), listOf("1 (784)")),
	SAMOA("WS", "WSM", "882", "Samoa", "The Independent State of Samoa", listOf(".ws"), Locale.of("sm", "WS"), listOf(TimeZoneDesignator.M_DAGGER_13), listOf("685")),
	SAN_MARINO("SM", "SMR", "674", "San Marino", "The Republic of San Marino", listOf(".sm"), Locale.of("it", "SM"), listOf(TimeZone.CET), listOf("378", "39")),
	SAO_TOME_AND_PRINCIPE("ST", "STP", "678", "Sao Tome and Principe", "The Democratic Republic of São Tomé and Príncipe", listOf(".st"), Locale.of("pt", "ST"), listOf(TimeZone.GMT), listOf("239")),
	SAUDI_ARABIA("SA", "SAU", "682", "Saudi Arabia", "The Kingdom of Saudi Arabia", listOf(".sa"), Locale.of("ar", "SA"), listOf(TimeZone.AST_3), listOf("966")),
	SENEGAL("SN", "SEN", "686", "Senegal", "The Republic of Senegal", listOf(".sn"), Locale.of("fr", "SN"), listOf(TimeZone.GMT), listOf("221")),
	SERBIA("RS", "SRB", "688", "Serbia", "The Republic of Serbia", listOf(".rs"), Locale.of("sr", "RS"), listOf(TimeZone.CET), listOf("381")),
	SEYCHELLES("SC", "SYC", "690", "Seychelles", "The Republic of Seychelles", listOf(".sc"), Locale.of("en", "SC"), listOf(TimeZone.SCT), listOf("248")),
	SIERRA_LEONE("SL", "SLE", "694", "Sierra Leone", "The Republic of Sierra Leone", listOf(".sl"), Locale.of("en", "SL"), listOf(TimeZone.GMT), listOf("232")),
	SINGAPORE("SG", "SGP", "702", "Singapore", "The Republic of Singapore", listOf(".sg"), Locale.of("en", "SG"), listOf(TimeZone.SGT), listOf("65")),
	SINT_MAARTEN("SX", "SXM", "534", "Sint Maarten", "Sint Maarten", listOf(".sx"), Locale.of("nl", "SX"), listOf(TimeZone.AST_M4), listOf("1 (758)")),
	SLOVAKIA("SK", "SVK", "703", "Slovakia", "The Slovak Republic", listOf(".sk"), Locale.of("sk", "SK"), listOf(TimeZone.CET), listOf("421")),
	SLOVENIA("SI", "SVN", "705", "Slovenia", "The Republic of Slovenia", listOf(".si"), Locale.of("sl", "SI"), listOf(TimeZone.CET), listOf("386")),
	SOLOMON_ISLANDS("SB", "SLB", "090", "Solomon Islands", "The Solomon Islands", listOf(".sb"), Locale.of("en", "SB"), listOf(TimeZone.SBT), listOf("677")),
	SOMALIA("SO", "SOM", "706", "Somalia", "The Federal Republic of Somalia", listOf(".so"), Locale.of("so", "SO"), listOf(TimeZone.EAT), listOf("252")),
	SOUTH_AFRICA("ZA", "ZAF", "710", "South Africa", "The Republic of South Africa", listOf(".za"), Locale.of("en", "ZA"), listOf(TimeZone.SAST), listOf("27")),
	SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS("GS", "SGS", "239", "South Georgia and the South Sandwich Islands", "South Georgia and the South Sandwich Islands", listOf(".gs"), Locale.of("en", "GS"), listOf(TimeZone.GST_M2), listOf("500")),
	SOUTH_SUDAN("SS", "SSD", "728", "South Sudan", "The Republic of South Sudan", listOf(".ss"), Locale.of("en", "SS"), listOf(TimeZone.CAT), listOf("211")),
	SPAIN("ES", "ESP", "724", "Spain", "The Kingdom of Spain", listOf(".es"), Locale.of("es", "ES"), listOf(TimeZone.CET), listOf("34")),
	SRI_LANKA("LK", "LKA", "144", "Sri Lanka", "The Democratic Socialist Repuiblic of Sri Lanka", listOf(".lk"), Locale.of("si", "LK"), listOf(TimeZone.SLST), listOf("94")),
	SUDAN("SD", "SDN", "729", "Sudan", "The Republic of the Sudan", listOf(".sd"), Locale.of("ar", "SD"), listOf(TimeZone.CAT), listOf("249")),
	SURINAME("SR", "SUR", "740", "Suriname", "The Republic of Suriname", listOf(".sr"), Locale.of("nl", "SR"), listOf(TimeZone.SRT), listOf("597")),
	SALVBARD_AND_JAN_MAYEN("SJ", "SJM", "744", "Svalbard and Jan Mayen", "Svalbard and Jan Mayen", emptyList(), Locale.of("no", "SJ"), listOf(TimeZone.CET), listOf("47 (79)")),
	SWEDEN("SE", "SWE", "752", "Sweden", "The Kingdom of Sweden", listOf(".se"), Locale.of("sv", "SE"), listOf(TimeZone.CET), listOf("46")),
	SWITZERLAND("CH", "CHE", "756", "Switzerland", "The Swiss Confederation", listOf(".ch"), Locale.of("de", "CH"), listOf(TimeZone.CET), listOf("41")),
	SYRIA("SY", "SYR", "760", "Syria", "The Syrian Arab Republic", listOf(".sy"), Locale.of("ar", "SY"), listOf(TimeZone.EET), listOf("963")),
	TAIWAN("TW", "TWN", "158", "Taiwan", "The Republic of China", listOf(".tw"), Locale.of("zh", "TW"), listOf(TimeZone.TST), listOf("886")),
	TAJIKISTAN("TJ", "TJK", "762", "Tajikistan", "The Republic of Tajikistan", listOf(".tj"), Locale.of("tg", "TJ"), listOf(TimeZone.TJT), listOf("992")),
	TANZANIA("TZ", "TZA", "834", "Tanzania", "The United Republic of Tanzania", listOf(".tz"), Locale.of("sw", "TZ"), listOf(TimeZone.EAT), listOf("255")),
	THAILAND("TH", "THA", "764", "Thailand", "The Kingdom of Thailand", listOf(".th"), Locale.of("th", "TH"), listOf(TimeZone.THA), listOf("66")),
	TIMOR_LESTE("TL", "TLS", "626", "Timor-Leste", "The Democratic Republic of Timor-Leste", listOf(".tl"), Locale.of("pt", "TL"), listOf(TimeZone.TLT), listOf("670")),
	TOGO("TG", "TGO", "768", "Togo", "The Togolese Republic", listOf(".tg"), Locale.of("fr", "TG"), listOf(TimeZone.GMT), listOf("228")),
	TOKELAU("TK", "TKL", "772", "Tokelau", "Tokelau", listOf(".tk"), Locale.of("tk", "TK"), listOf(TimeZoneDesignator.M_DAGGER_13), listOf("690")),
	TONGA("TO", "TON", "776", "Tonga", "The Kingdom of Tonga", listOf(".to"), Locale.of("to", "TO"), listOf(TimeZone.TOT), listOf("676")),
	TRINIDAD_AND_TOBAGO("TT", "TTO", "780", "Trinidad and Tobago", "The Republic of Trinidad and Tobago", listOf(".tt"), Locale.of("en", "TT"), listOf(TimeZone.AST_M4), listOf("1 (868)")),
	TUNISIA("TN", "TUN", "788", "Tunisia", "The Republic of Tunisia", listOf(".tn"), Locale.of("ar", "TN"), listOf(TimeZone.CET), listOf("216")),
	TURKEY("TR", "TUR", "792", "Turkey", "The Republic of Turkey", listOf(".tr"), Locale.of("tr", "TR"), listOf(TimeZone.TRT), listOf("90")),
	TURKMENISTAN("TM", "TKM", "795", "Turkmenistan", "Turkmenistan", listOf(".tm"), Locale.of("tk", "TM"), listOf(TimeZone.TMT), listOf("993")),
	TURKS_AND_CAICOS_ISLANDS("TC", "TCA", "796", "Turks and Caicos Islands", "The Turks and Caicos Islands", listOf(".tc"), Locale.of("en", "TC"), listOf(TimeZone.EST), listOf("1 (649)")),
	TUVALU("TV", "TUV", "798", "Tuvalu", "Tuvalu", listOf(".tv"), Locale.of("en", "TV"), listOf(TimeZone.TVT), listOf("688")),
	UGANDA("UG", "UGA", "800", "Uganda", "The Republic of Uganda", listOf(".ug"), Locale.of("en", "UG"), listOf(TimeZone.EAT), listOf("256")),
	UKRAINE("UA", "UKR", "804", "Ukraine", "Ukraine", listOf(".ua"), Locale.of("uk", "UA"), listOf(TimeZone.EET), listOf("380")),
	UNITED_ARAB_EMIRATES("AE", "ARE", "784", "United Arab Emirates", "The United Arab Emirates", listOf(".ae"), Locale.of("ar", "AE"), listOf(TimeZone.GST_4), listOf("971")),
	UNITED_KINGDOM("GB", "GBR", "826", "United Kingdom", "The United Kingdom of Great Britain and Northern Ireland", listOf(".uk"), Locale.of("en", "GB"), listOf(TimeZone.GMT), listOf("44")),
	UNITED_STATES_MINOR_OUTLYING_ISLANDS("UM", "UMI", "581", "United States Minor Outlying Islands", "United States Pacific Island Wildlife Refuges, Navassa Island, and Wake Island", emptyList(), Locale.of("en", "UM"), listOf(TimeZoneDesignator.Y, TimeZoneDesignator.X, TimeZoneDesignator.W, TimeZoneDesignator.R, TimeZoneDesignator.M), listOf("1")),
	UNITED_STATES("US", "USA", "840", "United States", "The United States of America", listOf(".us"), Locale.of("en", "US"), listOf(TimeZone.EST, TimeZone.BIT, TimeZone.SST, TimeZone.HST, TimeZone.AKST, TimeZone.PST, TimeZone.MST_M7, TimeZone.CST_M6, TimeZone.AST_M4, TimeZone.CHST, TimeZone.WAKT), listOf("1")),
	URUGUAY("UY", "URY", "858", "Uruguay", "The Oriental Republic of Uruguay", listOf(".uy"), Locale.of("es", "UY"), listOf(TimeZone.UYT), listOf("598")),
	UZBEKISTAN("UZ", "UZB", "860", "Uzbekistan", "The Republic of Uzbekistan", listOf(".uz"), Locale.of("uz", "UZ"), listOf(TimeZone.UZT), listOf("998")),
	VANUATU("VU", "VUT", "548", "Vanuatu", "The Republic of Vanuatu", listOf(".vu"), Locale.of("bi", "VU"), listOf(TimeZone.VUT), listOf("678")),
	VATICAN_CITY("VA", "VAT", "336", "Vatican City", "The Vatican City", listOf(".va"), Locale.of("it", "VA"), listOf(TimeZone.CET), listOf("379", "39")),
	VENEZUELA("VE", "VEN", "862", "Venezuela", "The Bolivarian Republic of Venezuela", listOf(".ve"), Locale.of("es", "VE"), listOf(TimeZone.VET), listOf("58")),
	VIETNAM("VN", "VNM", "704", "Vietnam", "The Socialist Republic of Vietnam", listOf(".vn"), Locale.of("vi", "VN"), listOf(TimeZone.ICT), listOf("84")),
	BRITISH_VIRGIN_ISLANDS("VG", "VGB", "092", "Virgin Islands (British)", "The British Virgin Islands", listOf(".vg"), Locale.of("en", "VG"), listOf(TimeZone.AST_M4), listOf("1 (284)")),
	US_VIRGIN_ISLANDS("VI", "VIR", "850", "Virgin Islands (U.S.)", "The Virgin Islands of the United States", listOf(".vi"), Locale.of("en", "VI"), listOf(TimeZone.AST_M4), listOf("1 (340)")),
	WALLIS_AND_FUTUNA("WF", "WLF", "876", "Wallis and Futuna", "Wallis and Futuna", listOf(".wf"), Locale.of("fr", "WF"), listOf(TimeZoneDesignator.M), listOf("681")),
	WESTERN_SAHARA("EH", "ESH", "732", "Western Sahara", "The Sahrawi Arab Democratic Republic", emptyList(), Locale.of("ar", "EH"), listOf(TimeZone.WAT), listOf("212")),
	YEMEN("YE", "YEM", "887", "Yemen", "The Republic of Yemen", listOf(".ye"), Locale.of("ar", "YE"), listOf(TimeZone.AST_3), listOf("967")),
	ZAMBIA("ZM", "ZMB", "894", "Zambia", "The Republic of Zambia", listOf(".zm"), Locale.of("en", "ZM"), listOf(TimeZone.CAT), listOf("260")),
	ZIMBABWE("ZW", "ZWE", "716", "Zimbabwe", "The Republic of Zimbabwe", listOf(".zw"), Locale.of("en", "ZW"), listOf(TimeZone.CAT), listOf("263"));

	/**
	 * A list of distinct military time zones derived from the existing time zones.
	 * The list is created by mapping over the available time zones to extract their military time zone representation
	 * and filtering out duplicates.
	 *
	 * 
	 * @return A list of unique [TimeZoneDesignator] instances.
	 * @since 1.0.0
	 */
	val timeZoneDesignators: List<TimeZoneDesignator>
		get() = timeZones.mapNotNull { it.timeZoneDesignator }.distinct()

	/**
	 * Retrieves a unique list of `ZoneOffset` values derived from the time zones associated with a country.
	 *
	 * This property maps over the `timeZones` field, extracting distinct offsets for each time zone.
	 *
	 * 
	 * @since 1.0.0
	 */
	val zoneOffsets: List<ZoneOffset>
		get() = timeZones.mappedTo { it.offset }.distinct()

	/**
	 * A list of distinct UTC offsets derived from available time zones.
	 *
	 * This property retrieves all valid UTC offset values from the list
	 * of time zones, filters out duplicates, and provides a distinct list
	 * of UTC offsets as strings.
	 *
	 * 
	 * the distinct UTC offsets.
	 * @return A list of unique UTC offsets as strings.
	 * @since 1.0.0
	 */
	val zoneUtcOffsets: StringList
		get() = timeZones.mappedTo { it.utcOffset }.distinct()


    companion object {
		/**
		 * Retrieves a list of all state names from the entries.
		 *
		 * Maps through the collection of entries and extracts the `countryName`
		 * property from each entry to produce a list of state names.
		 *
		 * @return A list containing all state names.
		 * @since 1.0.0
		 */
		val countriesNames = entries.mappedTo { it.countryName }

		/**
		 * Retrieves a list of all official names from the entries.
		 *
		 * This function maps through the entries collection and extracts the
		 * official name of each entry by accessing its `officialName` property.
		 *
		 * @return A list of official names extracted from the entries.
		 * @since 1.0.0
		 */
		val officialNames = entries.mappedTo { it.officialName }

		/**
		 * Returns the corresponding country or territory based on the input string.
		 * This function attempts to match the input with predefined mappings of country names and their alternate names.
		 *
		 * 
		 * @param s The string to be matched against predefined country or territory names, alternate spellings, or codes.
		 * @return The matched country or territory, or null if no match is found.
		 * @since 1.0.0
		 */
		infix fun of(s: String) = when (-s) {
			"brithish virgin islands" -> BRITISH_VIRGIN_ISLANDS
			"united states virgin islands" -> US_VIRGIN_ISLANDS
			"cape verde" -> CABO_VERDE
			"caribbean netherlands", "saba" -> BONAIRE_SINT_EUSTATIUS_AND_SABA
			"east timor" -> TIMOR_LESTE
			"great britain" -> UNITED_KINGDOM
			"holy see" -> VATICAN_CITY
			"ivory coast" -> COTE_D_IVORIE
			"jan mayen" -> SALVBARD_AND_JAN_MAYEN
			"north korea" -> NORTH_KOREA
			"people's republic of china" -> CHINA
			"republic of china" -> TAIWAN
			"republic of korea", "south korea" -> SOUTH_KOREA
			"republic of the congo" -> CONGO
			"sahrawi arab democratic republic" -> WESTERN_SAHARA
			"saint helena", "ascension island", "tristan da cunha" -> SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA
			"viet nam" -> VIETNAM
			else -> entries.firstOrNull {
				it.countryName.equalsIgnoreCase(s)
						|| it.alpha2.equalsIgnoreCase(s)
                        || it.alpha3.equalsIgnoreCase(s)
                        || it.numeric == s
                        || it.name.replace('_', ' ') == +s
                        || it.officialName.equalsIgnoreCase(s)
            }
        }
		/**
		 * Retrieves the first entry that matches the given two-letter country code (alpha-2).
		 *
		 * The search is case-insensitive and returns the first matching entry or null if no matches are found.
		 *
		 * @param alpha2 The two-letter country code (alpha-2) to search for.
		 * @return The first matching entry or null if no match is found.
		 * @since 1.0.0
		 */
		infix fun ofAlpha2(alpha2: String) = entries.firstOrNull { it.alpha2 equalsIgnoreCase alpha2 }
		/**
		 * Retrieves the first entry from the collection that matches the given three-letter alpha3 code, ignoring case.
		 *
		 * @param alpha3 The three-letter code to be matched, case-insensitively.
		 * @return The first matching entry, or null if no match is found.
		 * @since 1.0.0
		 */
		infix fun ofAlpha3(alpha3: String) = entries.firstOrNull { it.alpha3 equalsIgnoreCase alpha3 }
		/**
		 * Retrieves an entry from the collection that matches the given numeric value.
		 *
		 * @param numeric The numeric value to match against the entries.
		 * @return The first entry that matches the numeric value, or null if no match is found.
		 * @since 1.0.0
		 */
		infix fun ofNumeric(numeric: String) = entries.firstOrNull { it.numeric == numeric }
		/**
		 * Retrieves the first entry from the collection where the `countryName` matches the provided input,
		 * ignoring case.
		 *
		 * @param countryName The name of the state to match against.
		 * @return The first matching entry, or null if no match is found.
		 * @since 1.0.0
		 */
		infix fun ofCountryName(countryName: String) = entries.firstOrNull { it.countryName equalsIgnoreCase countryName }
		/**
		 * Finds the first entry in the list of entries where the official name matches
		 * the specified official name, ignoring case.
		 *
		 * @param officialName The official name to search for.
		 * @return The first matching entry, or null if no match is found.
		 * @since 1.0.0
		 */
		infix fun ofOfficialName(officialName: String) = entries.firstOrNull { it.officialName equalsIgnoreCase officialName }
		/**
		 * Retrieves the first entry that matches the specified top-level domain (TLD).
		 *
		 * @param tld The top-level domain to search for. If the input does not start
		 * with a dot (.), it will be prepended automatically before matching.
		 * @return The first entry that matches the given TLD, or null if no match is found.
		 * @since 1.0.0
		 */
		infix fun fromTld(tld: String) = entries.firstOrNull { e -> e.tlds.any { it equalsIgnoreCase if (tld.startsWith(".")) tld else ".$tld" } }
		/**
		 * Retrieves the first matching entry from the collection based on the provided locale.
		 *
		 * @param locale The locale used to find the matching entry.
		 * @return The first entry matching the provided locale, or null if no match is found.
		 * @since 1.0.0
		 */
		infix fun fromLocale(locale: Locale) = entries.firstOrNull { it.locale == locale }
		/**
		 * Filters entries by the specified time zone.
		 *
		 * @param timeZone The time zone used to filter the entries.
		 * @return A list of entries that contain the specified time zone.
		 * @since 1.0.0
		 */
		infix fun byTimeZone(timeZone: TimeZone) = entries.filter { it.timeZones.contains(timeZone) }

		/**
		 * Filters and retrieves a list of countries whose phone codes match the specified phone code.
		 *
		 * @param phoneCode The phone code to filter by. If the phone code starts with '+', it will be trimmed before processing.
		 * @return A list of countries that match the specified phone code.
		 * @since 1.0.0
		 */
		infix fun byPhoneCode(phoneCode: String): List<Country> {
			val phoneCode = if (phoneCode.startsWith("+")) phoneCode.substring(1).trim() else phoneCode
			return entries.filter { phoneCode in it.phoneCodes }
		}
		/**
		 * Retrieves a list of countries associated with the given phone code.
		 *
		 * This method converts the provided integer phone code to a string and delegates
		 * the operation to the overloaded `byPhoneCode` method that accepts a string parameter.
		 *
		 * @param phoneCode the phone code as an integer to filter the countries.
		 * @since 1.0.0
		 */
		infix fun byPhoneCode(phoneCode: Int) = byPhoneCode(phoneCode.toString())
		/**
		 * Retrieves a list of countries that match the specified phone code.
		 *
		 * @param phoneCode The phone code as a long integer to be used for filtering countries.
		 * @return A list of countries matching the provided phone code.
		 * @since 1.0.0
		 */
		infix fun byPhoneCode(phoneCode: Long) = byPhoneCode(phoneCode.toString())

		/**
		 * Retrieves the system's default country based on the current locale setting.
		 * If no country mapping is found for the locale, a provided default value
		 * is returned. If no default value is provided and no country is found,
		 * an exception is thrown.
		 *
		 * @param defaultValue An optional fallback country to use if no default country
		 * is found in the system's locale settings. Defaults to null.
		 * @since 1.0.0
		 */
		fun systemDefault(defaultValue: Country? = null) = fromLocale(Locale.getDefault()) ?: (defaultValue ?: IllegalStateException("No default country found."))

		/**
		 * Translates a list of state names using the provided translator.
		 *
		 * @param translator The translator object responsible for translating the state names.
		 * @param property The property to use as translation key
		 * @since 1.0.0
		 */
		fun translateCountryNamesWith(translator: Translator, property: KProperty1<Country, String> = Country::countryName) = translator.translate(entries mappedTo property)
    }

	/**
	 * Translates the given state name using the provided translator.
	 *
	 * @param translator The translator instance used to perform the translation.
	 * @param property The property to use as translation key
	 * @since 1.0.0
	 */
	fun translateCountryNameWith(translator: Translator, property: KProperty1<Country, String> = Country::countryName) = translator.translate(property.get(this))

	/**
     * Returns the string representation of the country object.
     *
     * This method provides the value of the `countryName` property, which represents
     * the name of the state associated with the country.
     *
     * @return The `countryName` of the country as a [String].
     * @since 1.0.0
     */
    override fun toString() = countryName

	/**
	 * Converts the current country object into a map representation where the field names are used as keys
	 * and their corresponding values as map values.
	 *
	 * @return A map containing key-value pairs representing the properties of the country object.
	 * @since 1.0.0
	 */
	@Suppress("functionName")
	private fun _toMap() = mapOf(
		"alpha2" to alpha2,
		"alpha3" to alpha3,
		"numeric" to numeric,
		"countryName" to countryName,
		"officialName" to officialName,
		"enumName" to name,
		"tlds" to tlds,
		"locale" to locale,
		"timeZones" to timeZones,
		"phoneCodes" to phoneCodes,
		"militaryTimeZones" to timeZoneDesignators,
		"zoneOffset" to zoneOffsets,
		"zoneUtcOffsets" to zoneUtcOffsets
	)

	/**
	 * Provides the value associated with the property name from the underlying map.
	 *
	 * This function retrieves the value corresponding to the name of the property from a mapped data structure.
	 * The result is cast to the expected type.
	 *
	 * - `alpha2` - The two-letter country code (alpha-2) associated with the country. - TYPE: [String]
	 * - `alpha3` - The three-letter code associated with the country. - TYPE: [String]
	 * - `numeric` - The numeric value associated with the country. - TYPE: [String]
	 * - `countryName` - The name of the state associated with the country. - TYPE: [String]
	 * - `officialName` - The official name associated with the country. - TYPE: [String]
	 * - `enumName` - The name of the country as an enum entry. - TYPE: [String]
	 * - `tlds` - The list of top-level domain names associated with the country. - TYPE: [StringList]
	 * - `locale` - The locale associated with the country. - TYPE: [Locale]
	 * - `timeZones` - The list of time zones associated with the country. - TYPE: `List<TimeZone>`
	 * - `phoneCodes` - The list of phone codes associated with the country. - TYPE: [StringList]
	 * - `militaryTimeZones` - The list of military time zones associated with the country. - TYPE: `List<MilitaryTimeZone>`
	 * - `zoneOffsets` - The list of zone offsets associated with the country. - TYPE: `List<ZoneOffset>`
	 * - `zoneUtcOffsets` - The list of zone UTC offsets associated with the country. - TYPE: [StringList]
	 *
	 * @param thisRef an optional reference to the object this property is bound to; can be null in some cases.
	 * @param property the metadata for the property for which the value is being retrieved.
	 * @return the value of the property name cast to type R.
	 * @since 1.0.0
	 */
	@Suppress("unchecked_cast")
	operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}