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
import kotlin.reflect.KProperty

/**
 * Represents a three-dimensional geometric sphere with a center and a radius.
 * This class includes functionality for serialization and deserialization,
 * surface area and volume calculations, geometric containment checks,
 * and equality/comparison operations.
 *
 * This class implements the `Serializable` interface for object serialization,
 * the `Comparable` interface for comparing spheres based on their radius, and
 * the `Shape3D` interface for general 3D geometric operations.
 *
 * @property center The center of the sphere, represented as a `Point`.
 * @property radius The radius of the sphere.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Sphere.Companion.Serializer::class)
@JsonDeserialize(using = Sphere.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Sphere.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Sphere.Companion.OldDeserializer::class)
@Suppress("unused")
class Sphere (var center: Point = Point(), var radius: Double = 0.0) : Serializable, Comparable<Sphere>, Shape3D {
    /**
     * The surface area of the sphere.
     * It is calculated using the formula 4 * π * radius^2.
     *
     * 
     * @since 1.0.0
     */
    override val surfaceArea: Double
        get() = 4 * PI * (radius * radius)

    /**
     * Represents the calculated volume of the sphere.
     * The volume is determined using the formula: (4/3) * π * r³,
     * where r is the radius of the sphere.
     *
     * 
     * @since 1.0.0
     */
    override val volume: Double
        get() = (4.0 / 3.0) * PI * (radius * radius * radius)

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
         * A constant value used as a tolerance threshold for floating-point comparisons
         * to mitigate precision errors resulting from numerical operations.
         *
         * Typically employed in geometric computations where exact equality is
         * impractical due to the nature of floating-point arithmetic.
         *
         * @since 1.0.0
         */
        private const val TOLERANCE = 1e-9

        class Serializer : ValueSerializer<Sphere>() {
            override fun serialize(value: Sphere, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                gen.writePOJOProperty("center", value.center)
                gen.writeNumberProperty("radius", value.radius)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Sphere>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Sphere {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return Sphere(
                    node.get("center").traverse(p.objectReadContext()).readValueAs(Point::class.java),
                    node.get("radius").traverse(p.objectReadContext()).readValueAs(Double::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<Sphere>() {
            override fun serialize(value: Sphere, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeObjectField("center", value.center)
                gen.writeNumberField("radius", value.radius)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Sphere>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Sphere {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Sphere(
                    node.get("center").traverse(p.codec).readValueAs(Point::class.java),
                    node.get("radius").traverse(p.codec).readValueAs(Double::class.java)
                )
            }
        }
    }

    /**
     * Creates a copy of the current `Sphere` instance, optionally replacing the `center` and/or `radius` values.
     *
     * @param center The new center position of the sphere. Defaults to the current sphere's center.
     * @param radius The new radius of the sphere. Defaults to the current sphere's radius.
     * @return A new `Sphere` instance with the specified or default values.
     * @since 1.0.0
     */
    fun copy(center: Point = this.center, radius: Double = this.radius) = Sphere(center, radius)

    /**
     * Compares this `Sphere` object with another object for equality.
     * The comparison checks if the other object is also a `Sphere` and,
     * depending on the `considerPosition` parameter, verifies if both
     * the radius and optionally the center position are equal.
     *
     * 
     * @param other An instance of `Any?` to compare with this `Sphere` object.
     * @param considerPosition A `Boolean` specifying whether to include center position in equality check.
     * @return `true` if the specified object is equal to this `Sphere`, `false` otherwise.
     * @since 1.0.0
     */
    fun equals(other: Any?, considerPosition: Boolean): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sphere

        if (considerPosition && (center != other.center)) return false
        if (radius != other.radius) return false

        return true
    }

    /**
     * Checks whether this `Sphere` is equal to another object.
     * The comparison is delegated to the implementation that considers
     * optional position comparison if necessary.
     *
     * 
     * @param other The object to compare with this `Sphere`.
     * @return `true` if the objects are considered equal, `false` otherwise.
     * @since 1.0.0
     */
    override operator fun equals(other: Any?) = equals(other, true)

    /**
     * Checks whether a given point is within or on the boundary of the sphere.
     *
     * 
     * @param point the point to check for containment
     * @return `true` if the point lies within or on the boundary of the sphere, otherwise `false`
     * @since 1.0.0
     */
    override operator fun contains(point: Point) = center.distanceTo(point) <= (radius + TOLERANCE)

    /**
     * Computes the hash code for this Sphere instance based on its properties.
     * The hash code is calculated by combining the hash codes of the `radius`
     * and `center` properties to ensure a consistent and unique value
     * corresponding to the state of the object.
     *
     * 
     * @return An integer representing the hash code of this Sphere instance.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = radius.hashCode()
        result = 31 * result + center.hashCode()
        return result
    }

    /**
     * Returns a string representation of the Sphere object, including its center and radius values.
     *
     * 
     * @return A string in the format "Sphere(center=[center], radius=[radius])".
     * @since 1.0.0
     */
    override fun toString() = "Sphere(center=$center, radius=$radius)"

    /**
     * Compares this `Sphere` instance with another `Sphere` instance based on their radius values.
     * The comparison is performed using the natural ordering of the radius property.
     *
     * 
     * @param other The other `Sphere` instance to compare against.
     * @return A negative integer, zero, or a positive integer as this `Sphere` is
     *         less than, equal to, or greater than the specified `Sphere` based on radius.
     * @since 1.0.0
     */
    override fun compareTo(other: Sphere) = radius.compareTo(other.radius)

    /**
     * Converts the properties of the `Sphere` object into a Map representation.
     * The resulting map includes the center, radius, surface area, and volume of the sphere.
     *
     * @return A map containing key-value pairs
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "center" to center,
        "radius" to radius,
        "surfaceArea" to surfaceArea,
        "volume" to volume
    )

    /**
     * Retrieves the value associated with a property from a mapped representation of a `Sphere` instance.
     *
     * This operator function is used to delegate property access to an internal mapping function `_toMap`.
     * It locates the property by its name and casts the value to the expected type.
     *
     * - `center`: The center position of the sphere - TYPE: [Point].
     * - `radius`: The radius of the sphere - TYPE: [Double].
     * - `surfaceArea`: The calculated surface area of the sphere - TYPE: [Double].
     * - `volume`: The calculated volume of the sphere - TYPE: [Double].
     *
     * @param thisRef The object for which the property is being resolved. Can be `null`.
     * @param property The metadata of the property being accessed.
     * @return The value corresponding to the property name, cast to the expected type `R`.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}