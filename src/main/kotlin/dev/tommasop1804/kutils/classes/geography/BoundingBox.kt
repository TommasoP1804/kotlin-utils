package dev.tommasop1804.kutils.classes.geography

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.after
import dev.tommasop1804.kutils.before
import dev.tommasop1804.kutils.classes.geometry.Point
import dev.tommasop1804.kutils.classes.geometry.Rectangle
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KProperty

/**
 * Represents a rectangular geographical boundary defined by minimum and
 * maximum latitude and longitude coordinates. A `BoundingBox` is
 * commonly used to model spatial extents or regions on a map.
 *
 * This class provides functionality such as:
 * - Retrieving and updating the geographical boundaries.
 * - Checking containment or intersection with other bounding boxes or points.
 * - Converting to and from other geometric formats.
 *
 * The `BoundingBox` is immutable if not modified via set methods.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = BoundingBox.Companion.Serializer::class)
@JsonDeserialize(using = BoundingBox.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = BoundingBox.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = BoundingBox.Companion.OldDeserializer::class)
@Suppress("unused")
class BoundingBox(var min: GeoCoordinate, var max: GeoCoordinate): Serializable, Comparable<BoundingBox> {
    /**
     * Constructs a `BoundingBox` instance using minimum and maximum `Point` values.
     *
     * The provided `min` and `max` points are converted into `GeoCoordinate` objects
     * to represent the boundaries of the bounding box.
     *
     * @param min The minimum point of the bounding box.
     * @param max The maximum point of the bounding box.
     * @since 1.0.0
     */
    constructor(min: Point, max: Point) : this(GeoCoordinate(min), GeoCoordinate(max))
    /**
     * Constructs a `BoundingBox` instance from a `Rectangle` by using its top-left and
     * bottom-right corners to define the bounding box.
     *
     * 
     * @since 1.0.0
     */
    constructor(rectangle: Rectangle) : this(GeoCoordinate(rectangle.topLeft), GeoCoordinate(rectangle.bottomRight))

    /**
     * Represents the width of the bounding box, calculated as the difference between the longitude
     * of the maximum and minimum corners. The width can also be updated, which modifies
     * the `min` and `max` coordinates of the bounding box while keeping its center longitude constant.
     *
     * 
     * @since 1.0.0
     */
    var width: Double
        get() = max.longitude - min.longitude
        set(value) {
            val centerLon = (min.longitude + max.longitude) / 2.0
            max = GeoCoordinate(max.latitude, centerLon + value / 2.0)
            min = GeoCoordinate(min.latitude, centerLon - value / 2.0)
        }
    /**
     * Represents the height of the bounding box, defined as the difference between
     * the latitude of the maximum and minimum points.
     *
     * Modifying the height will adjust the bounding box proportionally
     * by recalculating its minimum and maximum latitude values,
     * while keeping the center latitude constant.
     *
     * 
     * @since 1.0.0
     */
    var height: Double
        get() = max.latitude - min.latitude
        set(value) {
            val centerLat = (min.latitude + max.latitude) / 2.0
            max = GeoCoordinate(centerLat + value / 2.0, max.longitude)
            min = GeoCoordinate(centerLat - value / 2.0, min.longitude)
        }
    /**
     * The geographical center point of the bounding box, represented as a `GeoCoordinate`.
     *
     * The `centroid` is calculated as the midpoint between the minimum (`min`) and maximum (`max`)
     * coordinates of the bounding box along both latitude and longitude dimensions.
     *
     * 
     * @since 1.0.0
     */
    val centroid: GeoCoordinate
        get() {
            val centerLat = (min.latitude + max.latitude) / 2.0
            val centerLon = (min.longitude + max.longitude) / 2.0
            return GeoCoordinate(centerLat, centerLon)
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

        class Serializer : ValueSerializer<BoundingBox>() {
            override fun serialize(
                value: BoundingBox,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                gen.writePOJOProperty("min", value.min)
                gen.writePOJOProperty("max", value.max)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<BoundingBox>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): BoundingBox {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return BoundingBox(
                    node.get("min").traverse(p.objectReadContext()).readValueAs(GeoCoordinate::class.java),
                    node.get("max").traverse(p.objectReadContext()).readValueAs(GeoCoordinate::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<BoundingBox>() {
            override fun serialize(value: BoundingBox, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeObjectField("min", value.min)
                gen.writeObjectField("max", value.max)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<BoundingBox>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): BoundingBox {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return BoundingBox(
                    node.get("min").traverse(p.codec).readValueAs(GeoCoordinate::class.java),
                    node.get("max").traverse(p.codec).readValueAs(GeoCoordinate::class.java)
                )
            }
        }

        /**
         * Creates a BoundingBox from a Well-Known Text (WKT) polygon string.
         *
         * @param wkt The WKT string representing a polygon
         * @return A new BoundingBox instance, wrapped in a [Result]
         * @since 1.0.0
         */
        infix fun fromWKT(wkt: String) = runCatching {
            val coordinates = wkt.after("((").before("))").split(",")
                .map { it.trim().split(" ").map { coord -> coord.toDouble() } }

            val minLon = coordinates.minOf { it[0] }
            val maxLon = coordinates.maxOf { it[0] }
            val minLat = coordinates.minOf { it[1] }
            val maxLat = coordinates.maxOf { it[1] }

            BoundingBox(
                GeoCoordinate(minLat, minLon),
                GeoCoordinate(maxLat, maxLon)
            )
        }

        /**
         * Creates a BoundingBox from a PostGIS polygon string.
         *
         * @param postgis The PostGIS string representing a polygon with SRID
         * @return A new BoundingBox instance, wrapped in a [Result]
         * @throws IllegalArgumentException if the PostGIS string is not valid
         * @since 1.0.0
         */
        infix fun fromPostGIS(postgis: String) = runCatching { fromWKT(postgis after ";") }

        /**
         * Creates a BoundingBox from a GeoJSON polygon string.
         *
         * @param geojson The GeoJSON string representing a polygon
         * @return A new BoundingBox instance, wrapped in a [Result]
         * @throws IllegalArgumentException if the GeoJSON string is not valid
         * @since 1.0.0
         */
        infix fun fromGeoJSON(geojson: String) = runCatching {
            val coords = geojson.after("[[").before("]]")
                .split("],[")
                .map { it.trim('[', ']').split(",").map { coord -> coord.toDouble() } }

            val minLon = coords.minOf { it[0] }
            val maxLon = coords.maxOf { it[0] }
            val minLat = coords.minOf { it[1] }
            val maxLat = coords.maxOf { it[1] }

            BoundingBox(
                GeoCoordinate(minLat, minLon),
                GeoCoordinate(maxLat, maxLon)
            )
        }
    }

    /**
     * Creates a copy of the current `BoundingBox` with the option to specify new minimum and maximum coordinates.
     *
     * @param min The minimum coordinate of the new bounding box. Defaults to the current bounding box's `min` value.
     * @param max The maximum coordinate of the new bounding box. Defaults to the current bounding box's `max` value.
     * @since 1.0.0
     */
    fun copy(min: GeoCoordinate = this.min, max: GeoCoordinate = this.max) = BoundingBox(min, max)

    /**
     * Compares this `BoundingBox` instance with the specified `BoundingBox` instance.
     * The comparison is based on the `min` field of both instances.
     *
     * 
     * @param other the `BoundingBox` instance to be compared with.
     * @return a negative integer if the `min` field of this `BoundingBox` is less than the `min` field of the other,
     * zero if they are equal, or a positive integer if the `min` field of this `BoundingBox`
     * is greater than the `min` field of the other.
     * @since 1.0.0
     */
    override operator fun compareTo(other: BoundingBox) = min.compareTo(other.min)
    /**
     * Provides a string representation of the bounding box, displaying the minimum and maximum coordinates.
     *
     * 
     * @return A string in the format "[min, max]" where `min` and `max` are the minimum and maximum coordinates of the bounding box.
     * @since 1.0.0
     */
    override fun toString(): String = "(($min), ($max))"

    /**
     * Converts the current BoundingBox instance into a Rectangle.
     * The Rectangle is created using the `min` and `max` points of the BoundingBox.
     *
     * 
     * @return A Rectangle constructed from the `min` and `max` points of this BoundingBox.
     * @since 1.0.0
     */
    fun toRectangle() = Rectangle(min.toPoint(), max.toPoint())

    /**
     * Checks whether the given GeoCoordinate is within the bounds of this BoundingBox.
     *
     * 
     * @param coordinate The GeoCoordinate to check for containment.
     * @return `true` if the coordinate is within the bounds of the bounding box, `false` otherwise.
     * @since 1.0.0
     */
    operator fun contains(coordinate: GeoCoordinate): Boolean {
        val latOk = coordinate.latitude >= min.latitude && coordinate.latitude <= max.latitude
        val lonOk = coordinate.longitude >= min.longitude && coordinate.longitude <= max.longitude
        return latOk && lonOk
    }

    /**
     * Checks whether the given bounding box is completely contained within this bounding box.
     * A bounding box is considered to be contained if both its minimum and maximum coordinates are contained within this bounding box.
     *
     * 
     * @param other The bounding box to verify if it is contained within this bounding box.
     * @since 1.0.0
     */
    operator fun contains(other: BoundingBox) = contains(other.min) && contains(other.max)

    /**
     * Determines whether this bounding box intersects with another bounding box.
     *
     * 
     * @param other The bounding box to check for intersection with.
     * @return `true` if this bounding box intersects with the specified bounding box, `false` otherwise.
     * @since 1.0.0
     */
    infix fun intersects(other: BoundingBox): Boolean {
        if (max.longitude < other.min.longitude) return false
        if (min.longitude > other.max.longitude) return false
        if (max.latitude < other.min.latitude) return false
        return !(min.latitude > other.max.latitude)
    }

    /**
     * Computes the intersection of this bounding box with another bounding box.
     * If the two bounding boxes do not intersect, it returns null.
     *
     * 
     * @param other The other bounding box with which to find the intersection.
     * @return A new bounding box representing the intersection of this bounding box and the specified one,
     * or null if they do not intersect.
     * @since 1.0.0
     */
    infix fun intersection(other: BoundingBox): BoundingBox? {
        if (!intersects(other)) return null

        val intersectMinLat = max(min.latitude, other.min.latitude)
        val intersectMinLon = max(min.longitude, other.min.longitude)
        val intersectMaxLat = min(max.latitude, other.max.latitude)
        val intersectMaxLon = min(max.longitude, other.max.longitude)

        val intersectMin = GeoCoordinate(intersectMinLat, intersectMinLon)
        val intersectMax = GeoCoordinate(intersectMaxLat, intersectMaxLon)

        return BoundingBox(intersectMin, intersectMax)
    }

    /**
     * Creates a new bounding box that represents the union of this bounding box and the given bounding box.
     *
     * 
     * @param other The bounding box to be unified with the current bounding box.
     * @return A new bounding box that fully encompasses both the current bounding box and the given bounding box.
     * @since 1.0.0
     */
    infix fun union(other: BoundingBox): BoundingBox {
        val unionMinLat = min(min.latitude, other.min.latitude)
        val unionMinLon = min(min.longitude, other.min.longitude)
        val unionMaxLat = max(max.latitude, other.max.latitude)
        val unionMaxLon = max(max.longitude, other.max.longitude)

        val unionMin = GeoCoordinate(unionMinLat, unionMinLon)
        val unionMax = GeoCoordinate(unionMaxLat, unionMaxLon)
        return BoundingBox(unionMin, unionMax)
    }

    /**
     * Checks if the current `BoundingBox` instance is equal to the specified object.
     *
     * Compares the `min` and `max` fields of the current `BoundingBox` with the fields of the other object
     * to determine equality. The `other` object must also be a `BoundingBox` for the comparison to succeed.
     *
     * 
     * @param other the object to compare with this `BoundingBox`.
     * @return `true` if the specified object is a `BoundingBox` with the same `min` and `max` values,
     *         `false` otherwise.
     * @since 1.0.0
     */
    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BoundingBox

        if (min != other.min) return false
        if (max != other.max) return false

        return true
    }

    /**
     * Computes the hash code for this instance of `BoundingBox`.
     *
     * The hash code is generated using the `min` and `max` properties,
     * ensuring consistency and suitability for usage in hash-based collections.
     *
     * 
     * @return the hash code value as an `Int`.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = min.hashCode()
        result = 31 * result + max.hashCode()
        return result
    }

    /**
     * Converts the bounding box to Well-Known Text (WKT) format.
     * The WKT representation is a POLYGON with coordinates ordered as:
     * (minLon minLat, maxLon minLat, maxLon maxLat, minLon maxLat, minLon minLat)
     *
     * @return A string containing the WKT representation of the bounding box.
     * @since 1.0.0
     */
    fun toWKT(): String {
        return "POLYGON ((" +
                "${min.longitude} ${min.latitude}, " +
                "${max.longitude} ${min.latitude}, " +
                "${max.longitude} ${max.latitude}, " +
                "${min.longitude} ${max.latitude}, " +
                "${min.longitude} ${min.latitude}" +
                "))"
    }

    /**
     * Converts the bounding box to PostGIS polygon format.
     * The format is similar to WKT but includes SRID specification for WGS84.
     *
     * @return A string containing the PostGIS representation of the bounding box.
     * @since 1.0.0
     */
    fun toPostGIS() = "SRID=4326;${toWKT()}"

    /**
     * Converts the bounding box to GeoJSON format.
     * The GeoJSON representation is a Polygon feature with coordinates ordered as:
     * [[minLon,minLat], [maxLon,minLat], [maxLon,maxLat], [minLon,maxLat], [minLon,minLat]]
     *
     * @return A string containing the GeoJSON representation of the bounding box.
     * @since 1.0.0
     */
    fun toGeoJSON(): String {
        return """
            {
                "type": "Polygon",
                "coordinates": [[
                    [${min.longitude}, ${min.latitude}],
                    [${max.longitude}, ${min.latitude}],
                    [${max.longitude}, ${max.latitude}],
                    [${min.longitude}, ${max.latitude}],
                    [${min.longitude}, ${min.latitude}]
                ]]
            }
        """.trimIndent()
    }

    /**
     * Converts the `BoundingBox` instance into a `Map` representation,
     * where each key corresponds to a property of the bounding box.
     *
     * @return A `Map` with keys: "min", "max", "width", "height", and "centroid",
     *         mapping to their respective values from the `BoundingBox` instance.
     * @since 1.0.0
     */
    @Suppress("functionName")
    fun _toMap() = mapOf(
        "min" to min,
        "max" to max,
        "width" to width,
        "height" to height,
        "centroid" to centroid,
        "wkt" to toWKT(),
        "postGIS" to toPostGIS(),
        "geoJSON" to toGeoJSON()
    )

    /**
     * Retrieves the value of the property with the given name from the map returned by `_toMap`.
     *
     * - `min` - TYPE: [GeoCoordinate]
     * - `max` - TYPE: [GeoCoordinate]
     * - `width` - TYPE: [Double]
     * - `height` - TYPE: [Double]
     * - `centroid` - TYPE: [GeoCoordinate]
     * - `wkt` - TYPE: [String]
     * - `postGIS` - TYPE: [String]
     * - `geoJSON` - TYPE: [String]
     *
     * @param thisRef The object that contains the property. This parameter is typically ignored in this context.
     * @param property The property whose value is being retrieved. The name of the property is used to look up the value in the map.
     * @return The value associated with the property's name in the map, cast to the expected type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}