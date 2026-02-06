package dev.tommasop1804.kutils.classes.geography

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.geometry.Point
import dev.tommasop1804.kutils.classes.measure.MeasureUnit
import dev.tommasop1804.kutils.classes.measure.RMeasurement
import dev.tommasop1804.kutils.exceptions.ClassMismatchException
import dev.tommasop1804.kutils.exceptions.ExpectationMismatchException
import dev.tommasop1804.kutils.exceptions.LocationException
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import jakarta.persistence.AttributeConverter
import org.geolatte.geom.G2D
import org.geolatte.geom.Geometries
import org.geolatte.geom.Geometry
import org.geolatte.geom.codec.Wkt
import org.geolatte.geom.crs.CoordinateReferenceSystem
import org.geolatte.geom.crs.CrsRegistry
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import java.util.*
import kotlin.math.*
import kotlin.reflect.KProperty

/**
 * Represents a geographical coordinate with latitude and longitude.
 *
 * @property latitude the latitude of the coordinate as a `Double`.
 * @property longitude the longitude of the coordinate as a `Double`.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = GeoCoordinate.Companion.Serializer::class)
@JsonDeserialize(using = GeoCoordinate.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = GeoCoordinate.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = GeoCoordinate.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_take_as_int_invoke", "kutils_ignorecase_function")
class GeoCoordinate(latitude: Double = 0.0, longitude: Double = 0.0): Serializable, Comparable<GeoCoordinate> {
    /**
     * Represents the geographical latitude of a location in degrees.
     * Latitude values range from -90.0 to 90.0, where positive values indicate north
     * and negative values indicate south. Setting a value outside this range
     * will throw a LocationException.
     *
     * @throws LocationException if the latitude is not in -90..90
     * @since 1.0.0
     */
    var latitude: Double = 0.0
        set(value) {
            isValidLatitude(value) || throw LocationException("Latitude must be between -90.0 and 90.0")
            field = value
        }
    /**
     * Represents the geographical longitude in degrees.
     *
     * This variable stores the longitude value which must be within the valid range
     * of -180.0 to 180.0 to conform to geographical standards.
     * Longitude values are generally used to specify locations on the Earth's surface.
     *
     * @throws LocationException if the value is not within the range of -180.0 to 180.0.
     * @since 1.0.0
     */
    var longitude: Double = 0.0
        set(value) {
            isValidLongitude(value) || throw LocationException("Longitude must be between -180.0 and 180.0")
            field = value
        }

    init {
        latitude in -90.0..90.0 || throw LocationException("Latitude must be between -90.0 and 90.0")
        longitude in -180.0..180.0 || throw LocationException("Longitude must be between -180.0 and 180.0")

        this.latitude = latitude
        this.longitude = longitude
    }

    /**
     * Secondary constructor that initializes an instance using a given pair of doubles.
     * This constructor delegates to the primary constructor with the values extracted
     * from the provided pair.
     *
     * 
     * @param pair A pair containing two double values used for initialization.
     * @since 1.0.0
     */
    constructor(pair: Double2) : this(pair.first, pair.second)
    /**
     * Constructs an instance by swapping the x and y coordinates of the given point.
     *
     * @param point The point containing the original x and y coordinates.
     * @since 1.0.0
     */
    constructor(point: Point) : this(point.y, point.x)
    /**
     * Constructs an instance using the provided geometry object.
     *
     * This constructor initializes the instance with properties extracted
     * from a `Point` geometry, such as longitude and latitude.
     * If the provided geometry is not of type `org.geolatte.geom.Point`, an exception is thrown.
     *
     * @param geometry the geometry object to initialize the instance from
     * @throws ClassMismatchException if the geometry is not of type `Point`
     * @throws ExpectationMismatchException if the SRID of the geometry is not 4326
     * @since 1.0.0
     */
    constructor(geometry: Geometry<*>) : this() {
        when (geometry) {
            is org.geolatte.geom.Point -> {
                geometry.srid == 4326 || throw ExpectationMismatchException("SRID must be 4326")
                val position = geometry.position
                longitude = position.getCoordinate(0)
                latitude = position.getCoordinate(1)
            }
            else -> throw ClassMismatchException("Only Point geometries are supported")
        }
    }

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
         * Represents the approximate radius of the Earth in kilometers.
         *
         * This constant is widely used in geodesy and geographic calculations,
         * such as estimating distances between two points on the Earth's surface using
         * the haversine formula or other spherical trigonometric methods.
         *
         * The value of 6371 km is an approximation of the Earth's mean radius,
         * as the actual shape of the Earth is an oblate spheroid (slightly flattened at the poles).
         *
         * @since 1.0.0
         */
        const val EARTH_RADIUS_KM: Int = 6371

        /**
         * Validates whether the given latitude and longitude values fall within their respective valid geographic ranges.
         *
         *
         * Latitude must be in the range [-90, 90] and longitude must be in the range [-180, 180].
         *
         * @param latitude  the latitude value to validate, measured in degrees. Valid range: -90 to 90.
         * @param longitude the longitude value to validate, measured in degrees. Valid range: -180 to 180.
         * @return `true` if both latitude and longitude values are within their valid ranges; `false` otherwise.
         * @since 1.0.0
         */
        fun isValid(latitude: Double, longitude: Double): Boolean = latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180

        /**
         * Checks if the provided latitude value is valid.
         *
         *
         * A valid latitude is a value within the range of -90 to 90 degrees,
         * inclusive. Values outside this range are considered invalid.
         *
         * @param latitude the latitude value to validate, in degrees. It must be
         * within the range [-90, 90].
         * @return `true` if the latitude is valid (within [-90, 90]),
         * `false` otherwise.
         * @since 1.0.0
         */
        fun isValidLatitude(latitude: Double): Boolean = latitude >= -90 && latitude <= 90

        /**
         * Validates whether the provided longitude value is within the valid geographical range.
         *
         *
         * Longitude values represent the east-west position of a point on the Earth's surface
         * and must fall within the range of -180° to 180°.
         *
         * @param longitude the longitude value to check, in degrees.
         * Valid values must be within the range -180 to 180, inclusive.
         * @return `true` if the longitude is valid (within the range -180 to 180, inclusive),
         * `false` otherwise.
         * @since 1.0.0
         */
        fun isValidLongitude(longitude: Double): Boolean = longitude >= -180 && longitude <= 180

        /**
         * Parses a string representation of a geo-coordinate and converts it into a [GeoCoordinate] object.
         *
         * @param coordinate The string representation of the geo-coordinate. The format can either be a PostGIS SRID format
         * or a delimited string with latitude and longitude values (separated by ";" or ",").
         * @return A [Result] containing a [GeoCoordinate] object if parsing is successful, or an exception if parsing fails.
         * @since 1.0.0
         */
        fun parse(coordinate: String) = runCatching {
            if (coordinate.startsWith("SRID"))
                parsePostGIS(coordinate)()
            else {
                val parts = coordinate.split(
                    (if (";" in coordinate) ";" else (if ("," in coordinate) "," else "")).toRegex()
                ).dropLastWhile { it.isEmpty() }.toTypedArray()
                GeoCoordinate(parts[0].trim().toDouble(), parts[1].trim().toDouble())
            }
        }

        /**
         * Parses a coordinate string in Degree-Minute-Second (DMS) format to a GeoCoordinate object.
         * The format should include degrees, minutes, seconds, and an identifier (N, S, E, W) for direction.
         *
         * @param coordinate A string representing the coordinate in DMS format. Examples include
         *                   "40°44'55\" N, 73°59'11\" W" or "40°44'55\" N;73°59'11\" W".
         *                   The input is expected to contain valid latitude and longitude parts.
         *                   Latitude must use 'N' or 'S', and longitude must use 'E' or 'W'.
         * @return A GeoCoordinate object representing the parsed latitude and longitude values wrapped in a Result.
         *         On error, the result will contain an exception indicating invalid input format or parsing failure.
         * @since 1.0.0
         */
        fun parseDMS(coordinate: String) = runCatching {
            val s = coordinate.trim()
            var parts = s.split((if (";" in s) ";" else ",").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (parts.size == 1) {
                val index = if ("N" in s) s.indexOf("N") + 1 else s.indexOf("S") + 1
                parts = arrayOf(s.take(index), (-index)(s))
            }
            parts.size == 2 || throw MalformedInputException("Invalid format")
            val lat = parts[0].trim()
            val lon = parts[1].trim()

            !("°" !in lat || "'" !in lat || "\"" !in lat || ("N" !in lat && "S" !in lat)) || throw MalformedInputException(
                "Invalid latitude format (#°#'#\" N|S)"
            )
            !((("°" !in lon) || ("'" !in lon) || ("\"" !in lon) || ("E" !in lon && "W" !in lon))) || throw MalformedInputException(
                "Invalid longitude format (#°#'#\" E|W)"
            )

            val latDegrees: Double = parseDegree(lat, 'S')
            val latMinutes: Double = parseMinutes(lat)
            val latSeconds: Double = parseSeconds(lat)
            val latitude = latDegrees + latMinutes / 60 + latSeconds / 3600

            val lonDegrees: Double = parseDegree(lon, 'W')
            val lonMinutes: Double = parseMinutes(lon)
            val lonSeconds: Double = parseSeconds(lon)
            val longitude = lonDegrees + lonMinutes / 60 + lonSeconds / 3600

            GeoCoordinate(latitude, longitude)
        }

        /**
         * Parses a coordinate string in Degree-Decimal Minutes (DM) format into a GeoCoordinate object.
         * The input string should specify latitude and longitude in the DM format, with the latitude
         * followed by the longitude separated by a semicolon or comma.
         *
         * Example formats:
         * - "45°30.0' N, 9°30.0' E"
         * - "45°30.0' N; 9°30.0' E"
         * - "45°30.0' N9°30.0' E" (no delimiter)
         *
         * The latitude must contain "°", "'", and either "N" or "S". The longitude must contain "°", "'", and
         * either "E" or "W". If the format is invalid, the method will throw an exception.
         *
         * @param coordinate the coordinate string in Degree-Decimal Minutes (DM) format to be parsed
         * @return a Result containing the parsed GeoCoordinate object if successful, or an exception if the parsing fails
         * @throws MalformedInputException if the input coordinate string is invalid
         * @since 1.0.0
         */
        fun parseDM(coordinate: String) = runCatching {
            val s = coordinate.trim()
            var parts = s.split((if (";" in s) ";" else ",").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (parts.size == 1) {
                val index = if ("N" in s) s.indexOf("N") + 1 else s.indexOf("S") + 1
                parts = arrayOf(
                    s.take(index),
                    (-index)(s)
                )
            }
            parts.size == 2 || throw MalformedInputException("Invalid format")
            val lat = parts[0].trim()
            val lon = parts[1].trim()

            !("°" !in lat || "'" !in lat || ("N" !in lat && "S" !in lat)) || throw MalformedInputException("Invalid latitude format (example: #°#.#' N|S)")

            validateInputFormat(!("°" !in lon || "'" !in lon || ("E" !in lon && "W" !in lon))) {
                "Invalid longitude format (example: #°#.#' E|W)"
            }

            val latDegrees: Double = parseDegree(lat, 'S')
            val latMinutes: Double = parseMinutes(lat)
            val latitude = latDegrees + latMinutes / 60.0

            val lonDegrees: Double = parseDegree(lon, 'W')
            val lonMinutes: Double = parseMinutes(lon)
            val longitude = lonDegrees + lonMinutes / 60.0

            GeoCoordinate(latitude, longitude)
        }

        /**
         * Parses the degree part of a coordinate string and returns its numeric value.
         *
         * The method extracts the degree value from the given coordinate string and converts
         * it to a `Double`. If the coordinate string contains the specified negative direction
         * character, the value is negated.
         *
         * 
         * @param coordinate the coordinate string to parse. It must contain a degree value
         * followed by appropriate directional markers (e.g., 'N', 'S', 'E', 'W').
         * @param negativeDirection the character indicating the negative direction (e.g., 'S' for south or 'W' for west).
         * @return the parsed degree value as a `Double`. Returns a negative value if the
         * negative direction marker is present.
         * @since 1.0.0
         */
        private fun parseDegree(coordinate: String, negativeDirection: Char): Double {
            val degreePart = coordinate.split("[°']".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim()
            var degree = degreePart.toDouble()

            if (negativeDirection.toString() in coordinate) degree = -degree
            return degree
        }

        /**
         * Parses the minutes component from a coordinate string in Degrees, Minutes,
         * and Seconds (DMS) format.
         *
         * This method extracts and converts the minutes part of a DMS-formatted
         * coordinate string into a `Double`.
         *
         * 
         * @param coordinate the coordinate string containing the minutes component in the format `#°#'#"#`.
         * @return the minutes component as a `Double`.
         * @since 1.0.0
         */
        private fun parseMinutes(coordinate: String): Double {
            val minutesPart =
                coordinate.split("[°']".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split("'".toRegex())
                    .dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim()
            return minutesPart.toDouble()
        }

        /**
         * Parses the seconds component from a coordinate string formatted in Degrees, Minutes, and Seconds (DMS).
         *
         * This method extracts and converts the seconds part of the coordinate into a numeric value.
         *
         * 
         * and seconds (") components.
         * @param coordinate the coordinate string in DMS format to parse for the seconds component.
         * @return the numeric value of the seconds component from the coordinate string as a double.
         * @since 1.0.0
         */
        private fun parseSeconds(coordinate: String): Double {
            val secondsPart =
                coordinate.split("'".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split("\"".toRegex())
                    .dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim()
            return secondsPart.toDouble()
        }

        /**
         * Converts UTM (Universal Transverse Mercator) coordinate values to a geographic coordinate.
         *
         * This method takes UTM zone as a string and the respective easting and northing values to calculate
         * the latitude and longitude of the geographic coordinate.
         *
         * @param zone the UTM zone string representing the zone number and latitude band.
         * @param easting the easting value in meters, indicating the eastward distance from the central meridian.
         * @param northing the northing value in meters, indicating the northward distance from the equator.
         * @return a `GeoCoordinate` object containing the latitude and longitude values in degrees.
         * @since 1.0.0
         */
        @Suppress("LocalVariableName")
        fun ofUTM(zone: String, easting: Double, northing: Double): GeoCoordinate {
            val zoneNumber = zone.dropLast(1).toInt()

            val a = 6378137.0 // WGS84 equatorial radius
            val f = 1 / 298.257223563
            val k0 = 0.9996

            val e = sqrt(f * (2 - f))

            val lonOrigin = ((zoneNumber - 1) * 6 - 180 + 3).toDouble()
            val lonOriginRad = Math.toRadians(lonOrigin)

            val M = northing / k0
            val mu = M / (a * (1 - e.pow(2.0) / 4 - 3 * e.pow(4.0) / 64 - 5 * e.pow(6.0) / 256))

            val sqrt = sqrt(1 - e.pow(2.0))
            val e1 = (1 - sqrt) / (1 + sqrt)

            val phi1 =
                (mu + (3 * e1 / 2 - 27 * e1.pow(3.0) / 32) * sin(2 * mu) + (21 * e1.pow(2.0) / 16 - 55 * e1.pow(4.0) / 32) * sin(
                    4 * mu
                ) + (151 * e1.pow(3.0) / 96) * sin(6 * mu))
            val sinPhi1 = sin(phi1)
            val cosPhi1 = cos(phi1)

            val n1 = a / sqrt(1 - (e * sinPhi1).pow(2.0))
            val t1 = tan(phi1).pow(2.0)
            val c1 = (e * cosPhi1).pow(2.0) / (1 - e.pow(2.0))
            val r1 = a * (1 - e * e) / (1 - (e * sinPhi1).pow(2.0)).pow(1.5)
            val d: Double = (easting - 500000) / (n1 * k0)

            val latitude = (phi1 - (n1 * tan(phi1) / r1)
                    * (d.pow(2.0) / 2
                    - (5 + 3 * t1 + 10 * c1 - 4 * c1.pow(2.0) - 9 * e1.pow(2.0)) * d.pow(4.0) / 24
                    + (61 + 90 * t1 + 298 * c1 + 45 * t1.pow(2.0) - 252 * e1.pow(2.0)) * d.pow(6.0) / 720))

            val longitude = lonOriginRad + (d - (1 + 2 * t1 + c1) * d.pow(3.0) / 6
                    + (5 - 2 * c1 + 28 * t1 - 3 * c1.pow(2.0) + 8 * e1.pow(2.0) + 24 * t1.pow(2.0))
                    * d.pow(5.0) / 120) / cos(phi1)

            return GeoCoordinate(Math.toDegrees(latitude), Math.toDegrees(longitude))
        }

        /**
         * Converts a UTM (Universal Transverse Mercator) coordinate represented as a `Triple`
         * of zone, easting, and northing into a `GeoCoordinate`.
         *
         * This is a utility function that delegates to the `ofUTM(zone: String, easting: Double, northing: Double)` method.
         *
         * 
         * @param utm a `Triple` containing the UTM coordinates:
         * - `utm.first`: the UTM zone as a `String`.
         * - `utm.second`: the easting value as a `Double`.
         * - `utm.third`: the northing value as a `Double`.
         * @since 1.0.0
         */
        fun ofUTM(utm: Triple<String, Double, Double>) = ofUTM(utm.first, utm.second, utm.third)

        /**
         * Creates a GeoCoordinate using the UTM (Universal Transverse Mercator) coordinate system.
         *
         * This method leverages a UTM zone number, latitude band, easting, and northing to construct
         * a GeoCoordinate in geographic latitude and longitude.
         *
         * @param zone the UTM zone number, representing the 6-degree longitudinal segment. Must be between 1 and 60 inclusive.
         * @param latitudeBand the latitude band character, identifying the region's north-south hemispheric zone.
         * @param easting the easting value (in meters) relative to the central meridian of the UTM zone.
         * @param northing the northing value (in meters) relative to the Equator for the specified latitude band.
         * @since 1.0.0
         */
        fun ofUTM(zone: Int, latitudeBand: Char, easting: Double, northing: Double) = ofUTM("$zone$latitudeBand", easting, northing)

        /**
         * Parses a UTM (Universal Transverse Mercator) coordinate string and converts it into a structured representation.
         * The input string should consist of three components: the zone, easting, and northing values, separated by spaces.
         *
         * @param utm The UTM coordinate string to parse. Expected to be in the format "zone easting northing".
         * @return A Result wrapping the parsed UTM object if successful, or an exception if the input format is invalid.
         * @since 1.0.0
         */
        fun parseUTM(utm: String) = runCatching {
            val parts = utm.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            parts.size == 3 || throw MalformedInputException("Invalid UTM format: $utm")

            ofUTM(parts[0], parts[1].toDouble(), parts[2].toDouble())
        }

        /**
         * Parses a WKT (Well-Known Text) representation of a POINT geometry
         * and converts it into a [GeoCoordinate] object.
         *
         * @param wkt The input Well-Known Text (WKT) representation of a POINT.
         *            It should follow the format "POINT(x y)" where x and y
         *            are the coordinates.
         * @return A [GeoCoordinate] object representing the parsed coordinate.
         * @since 1.0.0
         */
        fun parseWKT(wkt: String) = runCatching {
            wkt.contains("POINT(", true) || throw MalformedInputException("Invalid WKT format: $wkt")
            val parts = wkt.replace("POINT(", "").replace(")", "").split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            GeoCoordinate(parts[0].toDouble(), parts[1].toDouble())
        }

        /**
         * Parses a GeoJSON string representing a Point and converts it into a [GeoCoordinate] instance.
         * This function ensures the input string is in the correct GeoJSON format and extracts the latitude
         * and longitude values.
         *
         * @param geoJson The GeoJSON string that must start with '{"type":"Point"}' and contain valid coordinates.
         * @return A [Result] containing a [GeoCoordinate] if parsing and validation are successful; otherwise, an error.
         * @since 1.0.0
         */
        fun parseGeoJSON(geoJson: String) = runCatching {
            geoJson.startsWith("{\"type\":\"Point\"") || throw MalformedInputException("Invalid GeoJSON format: $geoJson")
            val parts =
                geoJson.replace("{\"type\":\"Point\",\"coordinates\":[", "").replace("]}", "").split(",".toRegex())
                    .dropLastWhile { it.isEmpty() }.toTypedArray()
            GeoCoordinate(parts[1].toDouble(), parts[0].toDouble())
        }

        /**
         * Parses a PostGIS string into a [GeoCoordinate] object.
         *
         * The input string must follow the PostGIS format including SRID and POINT definition,
         * otherwise an [MalformedInputException] will be thrown. It extracts the SRID, latitude,
         * and longitude values from the input string and constructs a [GeoCoordinate] object.
         *
         * @param postgis The PostGIS formatted string containing SRID and POINT coordinates.
         * @return A [Result] wrapping a [GeoCoordinate] object if parsing is successful, or an exception if parsing fails.
         * @throws MalformedInputException if the input string does not follow the PostGIS format.
         * @throws ExpectationMismatchException if the SRID is not 4326.
         * @since 1.0.0
         */
        fun parsePostGIS(postgis: String) = runCatching {
            if (postgis.startsWith("SRID") && ";POINT(" in postgis) {
                val srid = postgis[5..<';'.code.toChar()(postgis)].toInt()
                srid == 4326 || throw ExpectationMismatchException("SRID must be 4326")
                val coordinates = postgis[16..<postgis.length - 1]

                val parts = coordinates.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (parts.size == 2) {
                    try {
                        val longitude = parts[0].toDouble()
                        val latitude = parts[1].toDouble()
                        GeoCoordinate(latitude, longitude)
                    } catch (e: NumberFormatException) {
                        throw MalformedInputException("Invalid coordinate format")
                    }
                } else throw MalformedInputException("Invalid coordinates format in PostGIS string")
            } else throw MalformedInputException("Invalid PostGIS string format")
        }

        class Serializer : ValueSerializer<GeoCoordinate>() {
            override fun serialize(
                value: GeoCoordinate,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<GeoCoordinate>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): GeoCoordinate = parse(p.string)()
        }

        class OldSerializer : JsonSerializer<GeoCoordinate>() {
            override fun serialize(value: GeoCoordinate, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.toString())
            }
        }

        class OldDeserializer : JsonDeserializer<GeoCoordinate>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): GeoCoordinate = parse(p.text)()
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<GeoCoordinate?, String?> {
            override fun convertToDatabaseColumn(attribute: GeoCoordinate?): String? = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?): GeoCoordinate? = dbData?.let { parse(it)() }
        }
    }

    /**
     * Creates a copy of the GeoCoordinate instance with optionally updated properties.
     *
     * @param latitude The latitude value for the new GeoCoordinate. Defaults to the current instance's latitude.
     * @param longitude The longitude value for the new GeoCoordinate. Defaults to the current instance's longitude.
     * @since 1.0.0
     */
    fun copy(latitude: Double = this.latitude, longitude: Double = this.longitude) =
        GeoCoordinate(latitude, longitude)

    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "latitude" to latitude,
        "longitude" to longitude,
        "listDMS" to toListDMS(),
        "listNumericDMS" to toListNumericDMS(),
        "listDM" to toListDM(),
        "listNumericDM" to toListNumericDM(),
        "utm" to toUTM(),
        "wkt" to toWKT(),
        "geoJSON" to toGeoJSON(),
        "postgis" to toPostGIS(),
        "geoLatteGeom" to toGeoLatteGeom(),
        "geoLattePoint" to toGeoLattePoint(),
    )

    /**
     * Operator function to retrieve the value of a property with a delegated instance based on the property name.
     *
     * - `latitude` - TYPE: [Double]
     * - `longitude` - TYPE: [Double]
     * - `listDMS` - TYPE: [dev.tommasop1804.kutils.StringList]
     * - `listNumericDMS` - TYPE: `List<DoubleArray>`
     * - `listDM` - TYPE: [dev.tommasop1804.kutils.StringList]
     * - `listNumericDM` - TYPE: `List<DoubleArray>`
     * - `utm` - TYPE: `Triple<String, Double, Double>`
     * - `wkt` - TYPE: [String]
     * - `geoJSON` - TYPE: [String]
     * - `postgis` - TYPE: [String]
     * - `geoLatteGeom` - TYPE: [Geometry]
     * - `geoLattePoint` - TYPE: `Point<G2D>`
     *
     * @param thisRef the reference to the object for which the property is being delegated.
     * @param property the metadata of the property being accessed.
     * @return the value associated with the property name, cast to the generic number type R.
     * @throws NoSuchElementException if the property name is not found
     * @throws ClassCastException if the property is cast to an uncastable type
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Number?, property: KProperty<*>) = _toMap().getValue(property.name) as R

    /**
     * Converts the latitude and longitude properties of the `GeoCoordinate` into a `Pair`.
     *
     * 
     * @return a `Pair` where the first element is the latitude and the second is the longitude.
     * @since 1.0.0
     */
    fun toPair(): Double2 = latitude to longitude

    /**
     * Compares this `GeoCoordinate` with the specified object for equality.
     *
     * The comparison includes the latitude and longitude.
     * Additionally, it supports comparing this `GeoCoordinate` object with
     * a `Double2` (representing latitude and longitude).
     *
     * 
     * @param other the object to compare with this `GeoCoordinate`.
     *              It can be an instance of `GeoCoordinate`, `Double2`,
     *              or `Triple<Double, Double, Int>`.
     * @return `true` if the `other` object is considered equal to this object,
     *         `false` otherwise.
     * @since 1.0.0
     */
    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is Pair<*, *>?) return latitude == other?.first && longitude == other.second

        if (javaClass != other.javaClass) return false

        other as GeoCoordinate

        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false

        return true
    }

    /**
     * Generates a hash code for this instance of `GeoCoordinate`.
     *
     * The hash code is computed based on the `latitude` and `longitude`
     * properties of the `GeoCoordinate` instance. This method
     * ensures consistent hash code generation for use in hash-based collections.
     *
     * 
     * @return the hash code value as an `Int`.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }

    /**
     * Operator function that provides destructuring declaration support for retrieving the `latitude`.
     *
     * When used in a destructuring declaration, this function allows the first component
     * to represent the `latitude` value of the object.
     *
     * @return the latitude value of the object.
     * @since 1.0.0
     */
    operator fun component1() = latitude
    /**
     * Provides the second component of a data class, typically used in destructuring declarations.
     * This function returns the `longitude` property of the class.
     *
     * @return The `longitude` value of the object.
     * @since 1.0.0
     */
    operator fun component2() = longitude

    /**
     * Returns the instance of the object itself.
     * This operator function defines the unary plus operation for the object.
     *
     * @return The current instance of the object.
     * @since 1.0.0
     */
    operator fun unaryPlus() = this
    /**
     * Returns the geographical coordinate opposite to the current one.
     * This operator function negates the geographical coordinate by inverting
     * the latitude and adjusting the longitude to point to the opposite side
     * of the globe. Longitude is recalculated based on its current value,
     * ensuring it remains valid within the latitude and longitude range.
     *
     * @return A new GeoCoordinate object representing the opposite coordinate.
     * @since 1.0.0
     */
    operator fun unaryMinus() = GeoCoordinate(-latitude, if (longitude >= 0) longitude - 180 else 180 - longitude)

    /**
     * Converts the geographical coordinates of the `GeoCoordinate` object to a string
     * representation.
     *
     * 
     * @param separator the character used to separate latitude and longitude in the output string.
     * @return a string representation of the geographical coordinates.
     * @since 1.0.0
     */
    fun toString(separator: Char) = String.format(Locale.US, "%.6f%c%.6f", latitude, separator, longitude)

    /**
     * Converts the `GeoCoordinate` object to a string representation:
     * The latitude and longitude will be formatted with six decimal places and separated by the provided `separator`.
     *
     * @param separator the string used to separate the latitude and longitude in the output.
     * @return a string representation of the `GeoCoordinate`
     * @since 1.0.0
     */
    fun toString(separator: String) = String.format(Locale.US, "%.6f%s%.6f", latitude, separator, longitude)

    /**
     * Generates a string representation of the `GeoCoordinate` object.
     *
     * The string is formatted as "latitude, longitude"
     * with each value rounded to six decimal places.
     * @return the formatted string representation of the `GeoCoordinate` object.
     * @since 1.0.0
     */
    override fun toString() = String.format(Locale.US, "%.6f, %.6f", latitude, longitude)

    /**
     * Compares this `GeoCoordinate` instance with the given `GeoCoordinate` instance.
     * The comparison is performed first by latitude, and if they are equal, by longitude.
     *
     * 
     * @param other the `GeoCoordinate` instance to be compared with.
     * @return a negative integer if this `GeoCoordinate` is less than the other,
     * zero if they are equal, or a positive integer if this `GeoCoordinate`
     * is greater than the other.
     * @since 1.0.0
     */
    override operator fun compareTo(other: GeoCoordinate): Int {
        val latitudeComparison = latitude.compareTo(other.latitude)
        if (latitudeComparison != 0) return latitudeComparison
        return longitude.compareTo(other.longitude)
    }

    /**
     * Calculates the distance between this `GeoCoordinate` and another `GeoCoordinate`.
     *
     * The calculation is based on the Haversine formula, which determines the great-circle distance
     * between two points on the surface of a sphere. The result is returned in the specified unit of length.
     *
     * 
     * @param other the target `GeoCoordinate` to which the distance is calculated.
     * @param unit the unit of length in which the distance should be returned. Defaults to kilometers.
     * @return a `Measurement` object representing the distance between the two `GeoCoordinate` points in the specified unit.
     * @since 1.0.0
     */
    fun distanceTo(other: GeoCoordinate, unit: MeasureUnit.LengthUnit = MeasureUnit.LengthUnit.KILOMETER): RMeasurement<MeasureUnit.LengthUnit> {
        val dLat = Math.toRadians(other.latitude - latitude)
        val dLon = Math.toRadians(other.longitude - longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(latitude)) * cos(Math.toRadians(other.latitude)) * sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return RMeasurement(MeasureUnit.LengthUnit.KILOMETER.convertTo(EARTH_RADIUS_KM * c, unit), unit)
    }

    /**
     * Converts the latitude and longitude of a geographic coordinate into a list of strings
     * in Degrees, Minutes, and Seconds (DMS) format.
     *
     * The latitude and longitude are formatted with directional indicators (e.g., N, S, E, W)
     * based on their respective hemispheres.
     *
     * The resulting list contains two strings:
     * - The first string represents the latitude in DMS format.
     * - The second string represents the longitude in DMS format.
     *
     * This method internally uses the `convertToDMS` function to perform the conversion for
     * both latitude and longitude.
     *
     * 
     * @return a list of two strings containing the latitude and longitude in DMS format.
     * @since 1.0.0
     */
    fun toListDMS(): StringList = listOf(convertToDMS(latitude, true), convertToDMS(longitude, false))

    /**
     * Converts the latitude and longitude values of the `GeoCoordinate` object into a list of numeric Degree, Minute, and Second (DMS) components.
     *
     * Each geographic coordinate (latitude and longitude) is transformed into a `DoubleArray` of three elements:
     * degrees, minutes, and seconds. The resulting lists for latitude and longitude are then combined into a list.
     *
     * 
     * @return a `List<DoubleArray>` containing two arrays:
     * the first array represents the numeric DMS components of the latitude,
     * and the second array represents the numeric DMS components of the longitude.
     * @since 1.0.0
     */
    fun toListNumericDMS() = listOf(convertToDMSParts(latitude), convertToDMSParts(longitude))

    /**
     * Converts the latitude and longitude of the current `GeoCoordinate` instance
     * to a string in Degrees, Minutes, and Seconds (DMS) format, using the specified separator.
     *
     * 
     * @param separator the character used to separate the latitude and longitude values in the resulting string.
     * @return a string representation of the latitude and longitude in DMS format, separated by the given character.
     * @since 1.0.0
     */
    fun toStringDMS(separator: Char) = convertToDMS(latitude, true) + separator + convertToDMS(longitude, false)
    /**
     * Converts the latitude and longitude of the current `GeoCoordinate` object into a
     * Degrees, Minutes, and Seconds (DMS) formatted string, separated by the specified separator.
     *
     * 
     * @param separator the separator used to divide the latitude and longitude values in the resulting string.
     * @return a string representing the latitude and longitude in DMS format separated by the specified separator.
     * @since 1.0.0
     */
    fun toStringDMS(separator: String) = convertToDMS(latitude, true) + separator + convertToDMS(longitude, false)
    /**
     * Converts the geographical coordinates into a string formatted in Degrees, Minutes, and Seconds (DMS).
     *
     * The method combines the latitude and longitude values of the receiver `GeoCoordinate` object,
     * converting each to the DMS format and appending appropriate directional markers (N, S, E, W).
     * It uses the `convertToDMS` helper method for this conversion.
     *
     * 
     * @return A string representing the latitude and longitude in DMS format, separated by a space.
     * @since 1.0.0
     */
    fun toStringDMS() = convertToDMS(latitude, true) + " " + convertToDMS(longitude, false)

    /**
     * Converts the latitude and longitude of the `GeoCoordinate` instance into a list
     * of strings formatted in Degrees and Minutes (DM) format.
     *
     * The latitude and longitude are processed separately using the `convertToDM` method,
     * where each value is converted and appended with its directional indicator
     * (e.g., "N", "S", "E", "W"). Latitude direction is determined as "N" for positive
     * values and "S" for negative values, whereas longitude uses "E" and "W" respectively.
     *
     * 
     * @return a `StringList` where:
     * - The first element is the formatted latitude in DM with directional indicator.
     * - The second element is the formatted longitude in DM with directional indicator.
     * @since 1.0.0
     */
    fun toListDM(): StringList = listOf(convertToDM(latitude, true), convertToDM(longitude, false))

    /**
     * Converts the geographical coordinates (latitude and longitude) of the current `GeoCoordinate`
     * instance into a list of numeric Degrees and Minutes (DM) arrays.
     *
     * The latitude and longitude values from the `GeoCoordinate` object are processed separately,
     * where each value is converted into two numeric components:
     * - Degrees: The integer part of the absolute value of the coordinate.
     * - Minutes: The fractional part of the coordinate multiplied by 60.
     *
     * Each resulting array consists of two elements: the degrees and minutes.
     *
     * 
     * @return A list of two numeric arrays, where the first array contains the DM components of latitude
     * and the second array contains the DM components of longitude.
     * @since 1.0.0
     */
    fun toListNumericDM() = listOf(convertToDMParts(latitude), convertToDMParts(longitude))

    /**
     * Converts the current geographic coordinate to a Degrees and Minutes (DM) string format.
     *
     * The latitude and longitude values of the coordinate are transformed into DM string notation
     * and concatenated with the specified separator.
     *
     * 
     * @param separator a character used to separate the formatted latitude and longitude strings.
     * @return a string representation of the coordinate in DM format, with the latitude and longitude
     * separated by the specified separator.
     * @since 1.0.0
     */
    fun toStringDM(separator: Char) = convertToDM(latitude, true) + separator + convertToDM(longitude, false)
    /**
     * Converts the latitude and longitude of the GeoCoordinate object to a string
     * representation in Degrees and Minutes (DM) format, separated by the specified separator.
     *
     * 
     * @param separator a string used to separate the latitude and longitude representations.
     * @return a string in Degrees and Minutes (DM) format for both latitude and longitude,
     * separated by the specified separator.
     * @since 1.0.0
     */
    fun toStringDM(separator: String) = convertToDM(latitude, true) + separator + convertToDM(longitude, false)
    /**
     * Converts the geographic coordinates of the current object into a string representation in Degrees and Minutes (DM) format.
     *
     * The result is formatted such that latitude and longitude are represented with degrees, fractional minutes,
     * and their respective cardinal directions (N/S for latitude and E/W for longitude).
     *
     * For instance, latitude and longitude values are individually converted using the DM format and concatenated
     * with a single space separator in the final string representation.
     *
     * This method relies on the internal `convertToDM` function to handle the transformation of decimal degrees
     * into DM format for both latitude and longitude.
     *
     * 
     * @return a string representing the current coordinate in DM format, including directional annotations.
     * @since 1.0.0
     */
    fun toStringDM() = convertToDM(latitude, true) + " " + convertToDM(longitude, false)

    /**
     * Converts a decimal degree value into Degrees, Minutes, and Seconds (DMS) format.
     *
     * This method formats a given decimal degree value (latitude or longitude) into a
     * human-readable DMS string representation, including the directional indicator
     * (N, S, E, W) based on the hemisphere.
     *
     * @param decimalDegree the decimal degree value to convert. Can represent latitude
     * or longitude.
     * @param isLatitude a boolean indicating whether the value represents latitude
     * (`true`) or longitude (`false`). Determines the directional indicator used.
     * @return a `String` representing the coordinate in the format `° ' " {N/S/E/W}`.
     * @since 1.0.0
     */
    private fun convertToDMS(decimalDegree: Double, isLatitude: Boolean): String {
        val abs = abs(decimalDegree)

        val degrees = abs.toInt()
        val fractional = (abs - degrees) * 60
        val minutes = fractional.toInt()
        val seconds = (fractional - minutes) * 60

        val direction = if (isLatitude) if (decimalDegree >= 0) "N" else "S"
        else if (decimalDegree >= 0) "E" else "W"

        return String.format(Locale.US, "%d° %d' %2.4f\" %s", degrees, minutes, seconds, direction)
    }

    /**
     * Converts a decimal degree value into its Degrees, Minutes, and Seconds (DMS) components.
     *
     * The method calculates the absolute values of degrees, minutes, and seconds,
     * separating each component into a double array. This is useful for geographical
     * coordinate conversion and represents the DMS format.
     *
     * @param decimalDegree the decimal degree value to convert.
     * @return a double array containing the degrees, minutes, and seconds components, in that order.
     * @since 1.0.0
     */
    private fun convertToDMSParts(decimalDegree: Double): DoubleArray {
        val abs = abs(decimalDegree)

        val degrees = abs.toInt()
        val fractional = (abs - degrees) * 60
        val minutes = fractional.toInt()
        val seconds = (fractional - minutes) * 60

        return doubleArrayOf(degrees.toDouble(), minutes.toDouble(), seconds)
    }

    /**
     * Converts a decimal degree value to a Degrees and Minutes (DM) formatted string.
     *
     * The resulting string includes the degrees, minutes, and direction
     * (North/South for latitude, East/West for longitude).
     *
     * 
     * @param decimalDegree the value of the coordinate in decimal degrees to convert.
     * @param isLatitude a boolean flag indicating whether the coordinate is latitude (`true` for latitude, `false` for longitude).
     * @return a DM formatted string representation of the coordinate, including the directional symbol.
     * @since 1.0.0
     */
    private fun convertToDM(decimalDegree: Double, isLatitude: Boolean): String {
        val abs = abs(decimalDegree)

        val degrees = abs.toInt()
        val fractional = (abs - degrees) * 60

        val direction = if (isLatitude) if (decimalDegree >= 0) "N" else "S"
        else if (decimalDegree >= 0) "E" else "W"

        return String.format(Locale.US, "%d° %2.4f' %s", degrees, fractional, direction)
    }

    /**
     * Converts a decimal degree value into its Degrees and Minutes (DM) components.
     *
     * The method returns a two-element array where the first element is the degrees
     * component, and the second element is the remaining fractional minutes.
     *
     * 
     * @param decimalDegree the decimal degree coordinate value to convert. This
     * value should be a `Double`, representing a latitude or longitude in decimal format.
     * @return a `DoubleArray` containing two elements: the degrees component as the first
     * element, and the minutes component as the second element.
     * @since 1.0.0
     */
    private fun convertToDMParts(decimalDegree: Double): DoubleArray {
        val abs = abs(decimalDegree)

        val degrees = abs.toInt()
        val fractional = (abs - degrees) * 60

        return doubleArrayOf(degrees.toDouble(), fractional)
    }

    /**
     * Converts geographic coordinates (latitude and longitude) into UTM (Universal Transverse Mercator) coordinates.
     *
     * 
     * @return A [Triple] containing the UTM zone as a [String] (e.g., "33T"), the easting as [Double] (in meters), and the northing as [Double] (in meters).
     * @since 1.0.0
     */
    @Suppress("LocalVariableName")
    fun toUTM(): Triple<String, Double, Double> {
        val latRad = Math.toRadians(latitude)
        val lonRad = Math.toRadians(longitude)

        // UTM zone calculation
        val zoneNumber = ((longitude + 180) / 6).toInt() + 1
        val zoneLetter: Char = latitudeToZoneLetter(latitude)

        val a = 6378137.0 // raggio equatoriale WGS84
        val f = 1 / 298.257223563
        val k0 = 0.9996

        val e = sqrt(f * (2 - f))

        val lonOrigin = ((zoneNumber - 1) * 6 - 180 + 3).toDouble()
        val lonOriginRad = Math.toRadians(lonOrigin)

        val n = a / sqrt(1 - (e * sin(latRad)).pow(2.0))
        val t = tan(latRad).pow(2.0)
        val c = (e * cos(latRad)).pow(2.0) / (1 - e.pow(2.0))
        val _a = cos(latRad) * (lonRad - lonOriginRad)

        val m = a * ((1 - e.pow(2.0) / 4 - 3 * e.pow(4.0) / 64 - 5 * e.pow(6.0) / 256) * latRad
                - (3 * e.pow(2.0) / 8 + 3 * e.pow(4.0) / 32 + 45 * e.pow(6.0) / 1024) * sin(2 * latRad)
                + (15 * e.pow(4.0) / 256 + 45 * e.pow(6.0) / 1024) * sin(4 * latRad)
                - (35 * e.pow(6.0) / 3072) * sin(6 * latRad))

        val easting = k0 * n * (_a + (1 - t + c) * _a.pow(3.0) / 6 + ((5 - 18 * t + t * t + 72 * c - 58 * e.pow(2.0) / (1 - e.pow(2.0))) * _a.pow(5.0) / 120)) + 500000

        var northing = k0 * (m + n * tan(latRad) * (_a.pow(2.0) / 2 + (5 - t + 9 * c + 4 * c * c) * _a.pow(4.0) / 24
                + ((61 - 58 * t + t * t + 600 * c - 330 * e.pow(2.0) / (1 - e.pow(2.0))) * _a.pow(6.0) / 720)))

        if (latitude < 0) northing += 10000000.0 // for sud-emisphere

        return Triple(String.format("%d%c", zoneNumber, zoneLetter), easting, northing)
    }

    /**
     * Converts the current location to its UTM (Universal Transverse Mercator) string representation.
     *
     * 
     * @return A string representing the UTM coordinates, formatted with the zone,
     *         easting, and northing values with three decimal places of precision.
     * @since 1.0.0
     */
    fun toStringUTM(): String {
        val utm = toUTM()
        return utm.first + " " + String.format("%.3f", utm.second) + " " + String.format("%.3f", utm.third)
    }

    /**
     * Determines the UTM zone letter corresponding to the given latitude.
     *
     * 
     *           Valid latitude range is -80 to +84 degrees inclusive.
     * @param latitude The latitude in degrees. Must be within the supported UTM range (-80 to +84).
     * @return A character representing the UTM zone letter associated with the given latitude.
     *         Returns a letter between 'C' and 'X', excluding 'I' and 'O'.
     * @since 1.0.0
     * @throws LocationException if the latitude is outside the supported UTM range (-80 to +84).
     */
    private fun latitudeToZoneLetter(latitude: Double): Char {
        !(latitude >= 84 || latitude <= -80) || throw LocationException("Latitudine fuori dal range UTM supportato (-80 a +84)")

        val letters = "CDEFGHJKLMNPQRSTUVWXX" // X repetition for include 80-84
        val index = ((latitude + 80) / 8).toInt()
        return letters[index]
    }

    /**
     * Converts longitude and latitude coordinates into a WKT (Well-Known Text) representation
     * formatted as "POINT(longitude latitude)".
     *
     * 
     * @return The WKT formatted string representation of the point.
     * @since 1.0.0
     */
    fun toWKT() = "POINT($longitude $latitude)"

    /**
     * Converts the spatial information of the object into a GeoJSON `Point` representation.
     *
     * This method assumes the spatial reference system is WGS84 (EPSG:4326).
     *
     * 
     * @return A `String` representing the GeoJSON `Point` format for the given spatial data.
     * @since 1.0.0
     */
    fun toGeoJSON(): String = "{\"type\":\"Point\",\"coordinates\":[$longitude,$latitude]}"

    /**
     * Converts the current geographical coordinates into a PostGIS-compatible representation.
     *
     * 
     * @return A string formatted as a PostGIS-compatible representation containing the SRID and the POINT coordinates (longitude and latitude).
     * @since 1.0.0
     */
    fun toPostGIS() = "SRID=4326;POINT($longitude $latitude)"

    /**
     * Converts the current object to a Point instance using its longitude and latitude properties.
     *
     * 
     * @return A Point object created from the longitude and latitude of the receiver.
     * @since 1.0.0
     */
    fun toPoint() = Point(longitude, latitude)

    /**
     * Converts the current object into a GeoLatte geometry representation.
     * The conversion is performed by generating the WKT (Well-Known Text) representation
     * of the object and using it to produce the corresponding GeoLatte geometry.
     *
     * @return a GeoLatte geometry object representing the current object.
     *
     * @since 1.0.0
     */
    fun toGeoLatteGeom(): Geometry<*> = Wkt.fromWkt(toWKT())

    /**
     * Converts the geographic coordinates into a GeoLatte Point.
     *
     * The resulting Point is constructed using the specified longitude and latitude,
     * ensuring it adheres to the appropriate coordinate reference system.
     *
     * @return a GeoLatte Point object representing the geographic location with its spatial reference.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    fun toGeoLattePoint(): org.geolatte.geom.Point<G2D> = Geometries.mkPoint(
        G2D(longitude, latitude),
        CrsRegistry.getCoordinateReferenceSystemForEPSG(4326, null) as CoordinateReferenceSystem<G2D>
    )

/*    *//**
     * Changes the SRID (Spatial Reference System Identifier) of the current geometry to the specified SRID.
     *
     * This method transforms the geometry from its current coordinate reference system (CRS)
     * to the CRS associated with the new SRID while preserving the spatial relationships.
     *
     * @since 1.0.0
     *//*
    fun changeSRID(srid: Int) = runCatching { GeoCoordinate(toGeoLatteGeom().changeSRID(srid)()) }*/
}