package dev.tommasop1804.kutils.classes.geometry

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.exceptions.GeometryException
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * Represents a 3-dimensional cuboid defined by a minimum corner point and dimensions: width, height, and depth.
 * The class ensures all dimensions are non-negative using validation in the initializer block.
 *
 * @constructor Initializes a cuboid with the given minimum corner, width, height, and depth.
 * @property minCorner The minimum corner point of the cuboid. Default is the origin point.
 * @property width The width of the cuboid along the x-axis. Must be non-negative.
 * @property height The height of the cuboid along the y-axis. Must be non-negative.
 * @property depth The depth of the cuboid along the z-axis. Must be non-negative.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Cuboid.Companion.Serializer::class)
@JsonDeserialize(using = Cuboid.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Cuboid.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Cuboid.Companion.OldDeserializer::class)
@Suppress("unused")
open class Cuboid (var minCorner: Point = Point(), var width: Double = 0.0, var height: Double = 0.0, var depth: Double = 0.0) : Serializable, Comparable<Cuboid>, Shape3D {
    init {
        width >= 0 && height >= 0 && depth >= 0 || throw GeometryException("Width, height and depth must be greater than zero")
    }

    /**
     * Represents the total surface area of the cuboid, calculated as the sum of the areas
     * of all six rectangular faces. The surface area is determined using the dimensions
     * of the cuboid: width, height, and depth.
     *
     * 
     * @return The computed surface area of the cuboid as a `Double`.
     * @since 1.0.0
     */
    override val surfaceArea: Double
        get() = 2 * ((width * height) + (width * depth) + (height * depth))
    /**
     * Represents the volume of the cuboid.
     * The volume is calculated as the product of the cuboid's width, height, and depth.
     *
     * 
     * @since 1.0.0
     */
    override val volume: Double
        get() = width * height * depth
    /**
     * Represents the centroid (geometric center) of the cuboid.
     * The centroid is calculated as the midpoint of the cuboid along each axis.
     *
     * 
     * @return A `Point` object representing the geometric center of the cuboid.
     * @since 1.0.0
     */
    val centroid: Point
        get() = Point(minCorner.x + width / 2, minCorner.y + height / 2, minCorner.z + depth / 2)

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
         * A predefined constant representing the tolerance value used for floating-point
         * comparisons in cuboid operations. This value indicates the acceptable threshold
         * for imprecision due to floating-point arithmetic.
         *
         * 
         * containment checks, equality, or other calculations.
         * @since 1.0.0
         */
        private const val TOLERANCE = 1e-9

        class Serializer : ValueSerializer<Cuboid>() {
            override fun serialize(value: Cuboid, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                gen.writePOJOProperty("minCorner", value.minCorner)
                gen.writeNumberProperty("width", value.width)
                gen.writeNumberProperty("height", value.height)
                gen.writeNumberProperty("depth", value.depth)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Cuboid>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Cuboid {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return Cuboid(
                    node.get("center").traverse(p.objectReadContext()).readValueAs(Point::class.java),
                    node.get("width").traverse(p.objectReadContext()).readValueAs(Double::class.java),
                    node.get("height").traverse(p.objectReadContext()).readValueAs(Double::class.java),
                    node.get("depth").traverse(p.objectReadContext()).readValueAs(Double::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<Cuboid>() {
            override fun serialize(value: Cuboid, gen: JsonGenerator, serializers: SerializerProvider?) {
                gen.writeStartObject()
                gen.writeObjectField("minCorner", value.minCorner)
                gen.writeNumberField("width", value.width)
                gen.writeNumberField("height", value.height)
                gen.writeNumberField("depth", value.depth)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Cuboid>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Cuboid {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Cuboid(
                    node.get("center").traverse(p.codec).readValueAs(Point::class.java),
                    node.get("width").traverse(p.codec).readValueAs(Double::class.java),
                    node.get("height").traverse(p.codec).readValueAs(Double::class.java),
                    node.get("depth").traverse(p.codec).readValueAs(Double::class.java)
                )
            }
        }
    }

    /**
     * Creates and returns a new `Cuboid` instance based on the provided dimensions and position values.
     * If no new values are provided, defaults to the current cuboid's respective properties.
     *
     * @param minCorner The bottom-left-front corner of the cuboid. Defaults to the current cuboid's `minCorner`.
     * @param width The width of the cuboid. Defaults to the current cuboid's `width`.
     * @param height The height of the cuboid. Defaults to the current cuboid's `height`.
     * @param depth The depth of the cuboid. Defaults to the current cuboid's `depth`.
     * @since 1.0.0
     */
    fun copy(minCorner: Point = this.minCorner, width: Double = this.width, height: Double = this.height, depth: Double = this.depth) = Cuboid(minCorner, width, height, depth)

    /**
     * Determines whether the specified point is contained within the cuboid.
     *
     * 
     * @param point The point to check for containment.
     * @return `true` if the point lies within or on the boundary of the cuboid, considering a tolerance; `false` otherwise.
     * @since 1.0.0
     */
    override operator fun contains(point: Point): Boolean {
        val px: Double = point.x
        val py: Double = point.y
        val pz: Double = point.z
        val minX = minCorner.x
        val minY = minCorner.y
        val minZ = minCorner.z
        val maxX = minX + width
        val maxY = minY + height
        val maxZ = minZ + depth

        return px >= minX - TOLERANCE && px <= maxX + TOLERANCE && py >= minY - TOLERANCE && py <= maxY + TOLERANCE && pz >= minZ - TOLERANCE && pz <= maxZ + TOLERANCE
    }

    /**
     * Compares the current cuboid instance with another cuboid based on their volume.
     * Returns a negative integer, zero, or a positive integer as the volume of this cuboid
     * is less than, equal to, or greater than the specified cuboid's volume.
     *
     * 
     * @param other The other cuboid to compare the current cuboid with.
     * @since 1.0.0
     */
    override fun compareTo(other: Cuboid) = volume.compareTo(other.volume)

    /**
     * Compares this cuboid with another object for equality. Two cuboids are considered equal if they
     * have the same dimensions (width, height, depth) and, optionally, the same position.
     *
     * 
     * @param other An instance of `Any?` to compare with this `Cuboid` instance.
     * @param considerPosition A flag indicating whether the position (minCorner) of the cuboids
     * should be considered in the comparison.
     * @return `true` if the specified object is a `Cuboid` and is considered equal to this instance,
     * `false` otherwise.
     * @since 1.0.0
     */
    fun equals(other: Any?, considerPosition: Boolean): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cuboid

        if (width != other.width) return false
        if (height != other.height) return false
        if (depth != other.depth) return false
        if (considerPosition && (minCorner != other.minCorner)) return false

        return true
    }

    /**
     * Returns a string representation of the `Cuboid` instance. The representation includes the
     * minimum corner position, width, height, and depth of the cuboid.
     *
     * @return A string describing the cuboid with its minimum corner and dimensions.
     * @since 1.0.0
     */
    override fun toString() = "Cuboid(minCorner=$minCorner, width=$width, height=$height, depth=$depth)"

    /**
     * Converts the properties of the current `Cuboid` instance into a map representation.
     * The map includes the cuboid's minimum corner position, dimensions, surface area, volume, and centroid.
     *
     * @return A map containing the keys "minCorner", "width", "height", "depth", "surfaceArea", "volume", and "centroid",
     * mapped to their respective property values.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "minCorner" to minCorner,
        "width" to width,
        "height" to height,
        "depth" to depth,
        "surfaceArea" to surfaceArea,
        "volume" to volume,
        "centroid" to centroid
    )

    /**
     * Provides the backing property value of a `Cuboid` field using delegated property access.
     * Retrieves the value from a map where property names are mapped to their respective values.
     *
     * - `minCorner` - TYPE: [Point]
     * - `width` - TYPE: [Double]
     * - `height` - TYPE: [Double]
     * - `depth` - TYPE: [Double]
     * - `surfaceArea` - TYPE: [Double]
     * - `volume` - TYPE: [Double]
     * - `centroid` - TYPE: [Point]
     *
     * @param thisRef The reference to the parent object the property belongs to. It can be `null`.
     * @param property An instance of `KProperty` representing the property being accessed.
     * @return The value of the requested property, cast to the expected type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}