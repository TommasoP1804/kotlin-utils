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
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.reflect.KProperty

/**
 * Represents a right circular cone in 3-dimensional space.
 *
 * A cone is defined by its base center, radius, and height.
 * Additional properties and methods allow for the computation of its
 * geometric properties, such as surface area, volume, centroid, and containment of points.
 *
 * @property baseCenter The 3D coordinates of the center of the cone's base.
 * @property radius The radius of the cone's circular base.
 * @property height The perpendicular distance from the base to the apex of the cone.
 * @throws GeometryException if radius or height is lower than zero
 * @since 1.0.0
 */
@JsonSerialize(using = Cone.Companion.Serializer::class)
@JsonDeserialize(using = Cone.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Cone.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Cone.Companion.OldDeserializer::class)
@Suppress("unused")
class Cone (var baseCenter: Point = Point(), var radius: Double = 0.0, var height: Double = 0.0) : Serializable, Comparable<Cone>, Shape3D {
    init {
        radius >= 0 || throw GeometryException("Radius must be greater than zero")
        height >= 0 || throw GeometryException("Height must be greater than zero")
    }

    /**
     * The area of the base of the cone.
     *
     * This property represents the circular area spanned by the base of the cone,
     * calculated as π multiplied by the square of the cone's radius.
     *
     * 
     * @since 1.0.0
     */
    val baseArea: Double
        get() = PI * radius * radius
    /**
     * Computes the slant height of the cone.
     *
     * The slant height represents the length of the straight line from the apex of the cone
     * to a point on the edge of the base. It is calculated as the square root of the sum of
     * the squares of the cone's radius and its height.
     *
     * 
     * @return The computed slant height as a `Double`.
     * @since 1.0.0
     */
    private val slantHeight: Double
        get() = sqrt(radius * radius + height * height)
    /**
     * The lateral surface area of the cone.
     *
     * The lateral area is calculated considering the cone's radius and slant height,
     * using the formula π * radius * slantHeight.
     *
     * 
     * @since 1.0.0
     */
    val lateralArea: Double
        get() = PI * radius * slantHeight
    /**
     * Represents the total surface area of the cone, combining the base area and the lateral area.
     *
     * This property computes the surface area dynamically based on the values
     * of `baseArea` and `lateralArea` of the `Cone` instance.
     *
     * 
     * @since 1.0.0
     */
    override val surfaceArea: Double
        get() = baseArea + lateralArea
    /**
     * Computes the volume of the cone using the formula: `(1/3) * π * radius² * height`.
     *
     * The volume represents the three-dimensional space enclosed by the cone,
     * assuming it is a right circular cone with a base radius and height as given.
     *
     * 
     * @return the volume of the cone.
     * @since 1.0.0
     */
    override val volume: Double
        get() = (1.0 / 3.0) * PI * radius * radius * height
    /**
     * Calculates the centroid of the cone.
     *
     * The centroid is computed as a point located at a specific height
     * above the base center, determined by the formula `z = baseCenter.z + height / 4.0`.
     * The `x` and `y` coordinates of the centroid match the `x` and `y` coordinates of the base center.
     *
     * 
     * @return A `Point` object representing the centroid of the cone.
     * @since 1.0.0
     */
    val centroid: Point
        get() = Point(baseCenter.x, baseCenter.y, baseCenter.z + height / 4.0)

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
         * Represents the tolerance value used for numerical approximations and floating-point computations.
         * This constant is commonly applied within calculations to account for precision errors or
         * to define boundaries where slight variation is acceptable.
         *
         * @since 1.0.0
         */
        private const val TOLERANCE = 1e-9

        class Serializer : ValueSerializer<Cone>() {
            override fun serialize(value: Cone, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                gen.writePOJOProperty("baseCenter", value.baseCenter)
                gen.writeNumberProperty("radius", value.radius)
                gen.writeNumberProperty("height", value.height)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Cone>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Cone {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return Cone(
                    node.get("baseCenter").traverse(p.objectReadContext()).readValueAs(Point::class.java),
                    node.get("radius").traverse(p.objectReadContext()).readValueAs(Double::class.java),
                    node.get("height").traverse(p.objectReadContext()).readValueAs(Double::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<Cone>() {
            override fun serialize(value: Cone, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeObjectField("baseCenter", value.baseCenter)
                gen.writeNumberField("radius", value.radius)
                gen.writeNumberField("height", value.height)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Cone>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Cone {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Cone(
                    node.get("baseCenter").traverse(p.codec).readValueAs(Point::class.java),
                    node.get("radius").traverse(p.codec).readValueAs(Double::class.java),
                    node.get("height").traverse(p.codec).readValueAs(Double::class.java)
                )
            }
        }
    }

    /**
     * Creates a new `Cone` instance with optionally updated properties.
     *
     * This method allows for the modification of the `baseCenter`, `radius`, or `height`
     * properties of a cone. If no arguments are provided, the current `Cone` instance's
     * properties are reused.
     *
     * @param baseCenter The new center of the base of the cone. Defaults to the current cone's `baseCenter`.
     * @param radius The new radius of the base of the cone. Defaults to the current cone's `radius`.
     * @param height The new height of the cone. Defaults to the current cone's `height`.
     * @return A new `Cone` instance with the updated properties.
     * @since 1.0.0
     */
    fun copy(baseCenter: Point = this.baseCenter, radius: Double = this.radius, height: Double = this.height) = Cone(baseCenter, radius, height)

    /**
     * Compares this `Cone` object with another object for equality.
     * This method checks if the `radius` and `height` properties are equal.
     * If `considerPosition` is `true`, it also checks whether the `baseCenter` property is equal.
     *
     * 
     * @param other An instance of `Any?` to compare with this `Cone` object.
     * @param considerPosition If `true`, the `baseCenter` property will be included in the comparison.
     * @return `true` if the specified object is equal to this `Cone`, `false` otherwise.
     * @since 1.0.0
     */
    fun equals(other: Any?, considerPosition: Boolean): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cone

        if (radius != other.radius) return false
        if (height != other.height) return false
        if (considerPosition && (baseCenter != other.baseCenter)) return false

        return true
    }

    /**
     * Compares this `Cone` instance with another object for equality.
     * The comparison considers additional constraints based on the underlying `equals` logic
     * with the parameter `considerPosition` set to `true`.
     *
     * 
     * @param other The object to compare with the current `Cone` instance.
     * @return `true` if the objects are considered equal, `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?) = equals(other, true)

    /**
     * Computes the hash code for this cone instance based on its properties.
     * The hash code is generated by combining the hash codes of `radius`, `height`,
     * and `baseCenter`, ensuring a consistent and unique value for each instance.
     *
     * 
     * @return An integer representing the hash code of this cone.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = radius.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + baseCenter.hashCode()
        return result
    }

    /**
     * Returns a string representation of the Cone object.
     * The string includes the base center, radius, and height of the cone.
     *
     * @return the string representation of the cone with its key properties
     * @since 1.0.0
     */
    override fun toString() = "Cone(baseCenter=$baseCenter, radius=$radius, height=$height)"

    /**
     * Checks whether a given point is contained within this cone.
     *
     * This method determines if the specified `point` lies inside the bounds of
     * the cone described by its base center, height, and radius. It accounts
     * for cases where the point lies on the boundary of the cone within a certain
     * tolerance.
     *
     * 
     * @param point the point to check for containment
     * @return `true` if the point lies within or on the boundary of the cone, `false` otherwise
     * @since 1.0.0
     */
    override operator fun contains(point: Point): Boolean {
        val px: Double = point.x
        val py: Double = point.y
        val pz: Double = point.z
        val baseX = baseCenter.x
        val baseY = baseCenter.y
        val baseZ = baseCenter.z
        val apexZ = baseZ + height

        if (pz < baseZ - TOLERANCE || pz > apexZ + TOLERANCE) {
            return false
        }
        if (height < TOLERANCE) {
            if (abs(pz - baseZ) < TOLERANCE) {
                val distSqXY = (px - baseX).pow(2.0) + (py - baseY).pow(2.0)
                return distSqXY <= radius * radius + TOLERANCE
            }
            return false
        }
        if (radius < TOLERANCE) return abs(px - baseX) < TOLERANCE && abs(py - baseY) < TOLERANCE && (pz >= baseZ - TOLERANCE && pz <= apexZ + TOLERANCE)

        val radiusAtPz = radius * (apexZ - pz) / height
        val distSqXY = (px - baseX).pow(2.0) + (py - baseY).pow(2.0)
        return distSqXY <= radiusAtPz * radiusAtPz + TOLERANCE
    }

    /**
     * Compares this cone with the specified cone for order based on their volumes.
     * Returns a negative integer, zero, or a positive integer as the volume of this
     * cone is less than, equal to, or greater than the volume of the specified cone.
     *
     * 
     * @param other the other cone to be compared with this cone.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Cone) = volume.compareTo(other.volume)

    /**
     * Converts the properties of the `Cone` object into a map structure.
     *
     * This method maps the cone's attributes, including `baseCenter`,
     * `radius`, `height`, `baseArea`, `lateralArea`, `surfaceArea`,
     * `volume`, and `centroid`, to their corresponding key-value pairs.
     * It is primarily used for internal representations and computations.
     *
     * @return A map containing the properties of the cone and their values.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "baseCenter" to baseCenter,
        "radius" to radius,
        "height" to height,
        "baseArea" to baseArea,
        "lateralArea" to lateralArea,
        "surfaceArea" to surfaceArea,
        "volume" to volume,
        "centroid" to centroid
    )

    /**
     * Retrieves the value of the property referenced by the given `KProperty`.
     *
     * This operator function enables property delegation, allowing a `Cone` instance
     * to dynamically fetch values associated with specific properties from an internal map.
     * The property name is used as the key to look up the value in the map.
     *
     * - `baseCenter` - TYPE: [Point]
     * - `radius` - TYPE: [Double]
     * - `height` - TYPE: [Double]
     * - `baseArea` - TYPE: [Double]
     * - `lateralArea` - TYPE: [Double]
     * - `surfaceArea` - TYPE: [Double]
     * - `volume` - TYPE: [Double]
     * - `centroid` - TYPE: [Point]
     *
     * @param thisRef The reference to the object containing the property, can be `null`.
     * @param property The property whose value is being accessed.
     * @return The value corresponding to the provided property's name.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}