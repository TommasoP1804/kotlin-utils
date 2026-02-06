package dev.tommasop1804.kutils.classes.geometry

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import kotlin.math.PI
import kotlin.math.pow
import kotlin.reflect.KProperty

/**
 * Represents a three-dimensional cylinder characterized by its base center, radius, and height.
 * It supports operations for computing geometrical attributes such as surface area, volume,
 * and checking containment of points within its bounds.
 *
 * The cylinder is defined with a base aligned on the XY plane and extends along the positive Z direction.
 *
 * @property baseCenter The center point of the cylinder's base.
 * @property radius The radius of the cylinder's base.
 * @property height The height of the cylinder, measured from the base along the Z-axis.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Cylinder.Companion.Serializer::class)
@JsonDeserialize(using = Cylinder.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Cylinder.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Cylinder.Companion.OldDeserializer::class)
@Suppress("unused")
class Cylinder (var baseCenter: Point = Point(), var radius: Double = 0.0, var height: Double = 0.0) : Serializable, Comparable<Cylinder>, Shape3D {
    /**
     * Represents the geometric center of the Cylinder.
     * The centroid is the midpoint along the height of the cylinder, located directly above the center of its base.
     *
     * 
     * @return A `Point` representing the centroid of the cylinder.
     * @since 1.0.0
     */
    val centroid: Point
        get() = Point(baseCenter.x, baseCenter.y, baseCenter.z + height / 2)
    /**
     * Represents the area of the base of the cylinder. The base is treated as a circle
     * with its area computed using the formula: π * radius².
     *
     * 
     * @return The calculated area of the cylinder's base as a `Double`.
     * @since 1.0.0
     */
    val baseArea: Double
        get() = PI * radius * radius
    /**
     * Represents the lateral surface area of the cylinder.
     * The lateral surface area is calculated as `2 * π * radius * height`, where `radius` is the radius of the base
     * and `height` is the height of the cylinder.
     *
     * 
     * @return The lateral surface area of the cylinder as a `Double`.
     * @since 1.0.0
     */
    val lateralArea: Double
        get() = 2 * PI * radius * height
    /**
     * Represents the surface area of the cylinder, which is calculated as the sum of
     * twice the base area and the lateral area.
     *
     * 
     * @since 1.0.0
     */
    override val surfaceArea: Double
        get() = 2 * baseArea + lateralArea
    /**
     * The volume of the cylinder, calculated as π * radius² * height.
     *
     * 
     * @since 1.0.0
     */
    override val volume: Double
        get() = PI * radius * radius * height

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
         * Represents the numerical tolerance used for floating-point comparisons in the context of the `Cylinder` class.
         * This value is used to handle precision issues when comparing floating-point numbers, particularly for geometrical
         * computations involving the `Cylinder` class properties and methods.
         *
         * @since 1.0.0
         */
        private const val TOLERANCE = 1e-9

        class Serializer : ValueSerializer<Cylinder>() {
            override fun serialize(value: Cylinder, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                gen.writePOJOProperty("baseCenter", value.baseCenter)
                gen.writeNumberProperty("radius", value.radius)
                gen.writeNumberProperty("height", value.height)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Cylinder?>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Cylinder {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return Cylinder(
                    node.get("baseCenter").traverse(p.objectReadContext()).readValueAs(Point::class.java),
                    node.get("radius").traverse(p.objectReadContext()).readValueAs(Double::class.java),
                    node.get("height").traverse(p.objectReadContext()).readValueAs(Double::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<Cylinder>() {
            override fun serialize(value: Cylinder, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeObjectField("baseCenter", value.baseCenter)
                gen.writeObjectField("radius", value.radius)
                gen.writeObjectField("height", value.height)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Cylinder?>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Cylinder {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Cylinder(
                    node.get("baseCenter").traverse(p.codec).readValueAs(Point::class.java),
                    node.get("radius").traverse(p.codec).readValueAs(Double::class.java),
                    node.get("height").traverse(p.codec).readValueAs(Double::class.java)
                )
            }
        }
    }

    /**
     * Creates a copy of the current Cylinder instance with optional modifications to its properties.
     *
     * @param baseCenter The center point of the base of the cylinder. Defaults to the `baseCenter` of the current cylinder.
     * @param radius The radius of the cylinder. Defaults to the `radius` of the current cylinder.
     * @param height The height of the cylinder. Defaults to the `height` of the current cylinder.
     * @return A new Cylinder instance with the specified properties.
     * @since 1.0.0
     */
    fun copy(baseCenter: Point = this.baseCenter, radius: Double = this.radius, height: Double = this.height) = Cylinder(baseCenter, radius, height)

    /**
     * Indicates whether some other object is "equal to" this one. The comparison can optionally consider the position
     * of the cylinder (its `baseCenter`) in addition to its geometric properties (`radius` and `height`).
     *
     * 
     * @param other The object to compare with this cylinder for equality.
     * @return `true` if the `other` object is considered equal to this cylinder, `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?) = equals(other, true)

    /**
     * Determines whether the current cylinder is equal to another object. The comparison can optionally
     * consider the position of the cylinder's base center.
     *
     * 
     * @param other An instance of `Any?` to compare with this `Cylinder` object.
     * @param considerPosition A boolean flag indicating whether the position of the cylinder's base center
     * should be considered in the equality check.
     * @return `true` if the specified object is equal to this `Cylinder` based on the given conditions, `false` otherwise.
     * @since 1.0.0
     */
    fun equals(other: Any?, considerPosition: Boolean): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cylinder

        if (radius != other.radius) return false
        if (height != other.height) return false
        if (considerPosition && (baseCenter != other.baseCenter)) return false

        return true
    }

    /**
     * Computes the hash code for this instance of Cylinder based on its properties.
     * The hash code is calculated using the hash codes of `radius`, `height`, and `baseCenter`.
     *
     * 
     * @return An integer representing the hash code of this Cylinder instance.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = radius.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + baseCenter.hashCode()
        return result
    }

    /**
     * Returns a string representation of the Cylinder object, including its base center, radius, and height values.
     *
     * @return a string representation of the Cylinder in the format "Cylinder(baseCenter=?, radius=?, height=?)"
     * @since 1.0.0
     */
    override fun toString() = "Cylinder(baseCenter=$baseCenter, radius=$radius, height=$height)"

    /**
     * Checks if the specified point is contained within this cylinder.
     *
     * 
     * @param point The point to check for containment.
     * @return `true` if the point lies within the cylinder, `false` otherwise.
     * @since 1.0.0
     */
    override operator fun contains(point: Point): Boolean {
        val px: Double = point.x
        val py: Double = point.y
        val pz: Double = point.z
        val baseX = baseCenter.x
        val baseY = baseCenter.y
        val baseZ = baseCenter.z

        val zOk = (pz >= baseZ - TOLERANCE && pz <= baseZ + height + TOLERANCE)
        if (!zOk) return false

        val distSqXY = (px - baseX).pow(2.0) + (py - baseY).pow(2.0)
        return distSqXY <= radius.pow(2.0) + TOLERANCE
    }

    /**
     * Compares this cylinder to another cylinder based on their volumes.
     *
     * 
     * @param other the other cylinder whose volume is compared to the volume of the current cylinder
     * @return a negative integer, zero, or a positive integer as the volume of this cylinder
     * is less than, equal to, or greater than the volume of the specified cylinder
     * @since 1.0.0
     */
    override operator fun compareTo(other: Cylinder) = volume.compareTo(other.volume)

    /**
     * Converts the properties of the Cylinder instance into a map representation.
     *
     * The keys in the resulting map correspond to the property names of the Cylinder:
     *
     * @return A map where the keys are property names and the values represent the
     *    corresponding property values of the Cylinder instance.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "baseCenter" to baseCenter,
        "radius" to radius,
        "height" to height,
        "centroid" to centroid,
        "baseArea" to baseArea,
        "lateralArea" to lateralArea,
        "surfaceArea" to surfaceArea,
        "volume" to volume
    )

    /**
     * Retrieves the property value associated with the given property name from the internal map of the class.
     * The value is cast to the expected type.
     *
     * - `baseCenter`: The center point of the base of the cylinder - TYPE: [Point].
     * - `radius`: The radius of the cylinder - TYPE: [Double].
     * - `height`: The height of the cylinder - TYPE: [Double].
     * - `centroid`: The centroid of the cylinder - TYPE: [Point].
     * - `baseArea`: The base area of the cylinder - TYPE: [Double].
     * - `lateralArea`: The lateral area of the cylinder - TYPE: [Double].
     * - `surfaceArea`: The total surface area of the cylinder - TYPE: [Double].
     * - `volume`: The volume of the cylinder - TYPE: [Double].
     *
     * @param thisRef The reference to the object for which the property is being accessed. Can be `null`.
     * @param property The property for which the value is being retrieved.
     * @return The value of the property, cast to the expected type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}