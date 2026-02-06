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
import kotlin.reflect.KProperty

/**
 * Represents a circle in a 2D space with a specified radius and center point.
 * Provides methods for geometric calculations and comparisons, including area, perimeter, and intersection checks.
 *
 * @property radius the radius of the circle, must be non-negative
 * @property center the center point of the circle

 * @throws GeometryException if the radius is negative
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Circle.Companion.Serializer::class)
@JsonDeserialize(using = Circle.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Circle.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Circle.Companion.OldDeserializer::class)
@Suppress("unused")
class Circle (var radius: Double = 0.0, var center: Point = Point()) : Serializable, Comparable<Circle>, Shape2D {
    init {
        radius >= 0 || throw GeometryException("Radius must be greater than zero")
    }

    /**
     * Represents the area of the circle.
     *
     * The area is computed using the formula `π * radius^2`.
     *
     * 
     * @since 1.0.0
     */
    override val area: Double
        get() = PI * radius * radius

    /**
     * Represents the perimeter of the circle, calculated as `2 * π * radius`.
     *
     * The perimeter is derived dynamically based on the value of the circle's radius at the time of access.
     *
     * 
     * @since 1.0.0
     */
    override val perimeter: Double
        get() = 2 * PI * radius

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

        class Serializer : ValueSerializer<Circle>() {
            override fun serialize(value: Circle, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                gen.writeNumberProperty("radius", value.radius)
                gen.writePOJOProperty("center", value.center)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Circle>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Circle {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return Circle(
                    node.get("radius").traverse(p.objectReadContext()).readValueAs(Double::class.javaPrimitiveType),
                    node.get("center").traverse(p.objectReadContext()).readValueAs(Point::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<Circle>() {
            override fun serialize(value: Circle, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeNumberField("radius", value.radius)
                gen.writeObjectField("center", value.center)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Circle>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Circle {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Circle(
                    node.get("radius").traverse(p.codec).readValueAs(Double::class.javaPrimitiveType),
                    node.get("center").traverse(p.codec).readValueAs(Point::class.java)
                )
            }
        }
    }

    /**
     * Creates a new instance of `Circle` with updated properties.
     *
     * This method returns a copy of the current `Circle` with optional modifications
     * to its `radius` and/or `center`. If no arguments are provided, the original
     * `radius` and `center` of the circle are retained.
     *
     * @param radius The radius of the new circle. Defaults to the current circle's radius.
     * @param center The center of the new circle. Defaults to the current circle's center.
     * @since 1.0.0
     */
    fun copy(radius: Double = this.radius, center: Point = this.center) = Circle(radius, center)

    /**
     * Compares this circle with the specified circle based on their radii.
     * The comparison result is determined using the natural ordering of the radii.
     *
     * 
     * @param other the circle to compare with this circle
     * @return a negative integer, zero, or a positive integer as this circle's radius is less than,
     * equal to, or greater than the specified circle's radius
     * @since 1.0.0
     */
    override operator fun compareTo(other: Circle) = radius.compareTo(other.radius)

    /**
     * Returns a string representation of the Circle instance.
     *
     * The output includes the radius and center, providing a clear textual representation
     * of the Circle's current state. This method is overridden to enhance debugging and logging.
     *
     * 
     * @return a string representation in the format "Circle(radius=..., center=...)"
     * @since 1.0.0
     */
    override fun toString() = "Circle(radius=$radius, center=$center)"

    /**
     * Compares this `Circle` instance with another object for equality.
     * The comparison checks if the radii are equal and, optionally,
     * compares the positions of the centers if `considerPosition` is true.
     *
     * 
     * @param other An instance of `Any?` to compare with this `Circle` object.
     * @param considerPosition A boolean flag indicating whether to include the center position in the equality check.
     * Defaults to `true`, meaning the center position is included in the comparison by default.
     * @return `true` if the specified object is a `Circle` with the same radius and, optionally, the same center when `considerPosition` is true;
     * `false` otherwise.
     * @since 1.0.0
     */
    fun equals(other: Any?, considerPosition: Boolean) : Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Circle

        if (radius != other.radius) return false
        if (considerPosition && (center != other.center)) return false

        return true
    }

    /**
     * Compares this Circle with another object for equality. The comparison can optionally consider the
     * position of the Circle based on its center.
     *
     * 
     * @param other The object to compare with the current Circle instance.
     * @return `true` if the specified object is equal to the current Circle instance, otherwise `false`.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean = equals(other, true)

    /**
     * Computes the hash code for this Circle instance based on its properties.
     * The hash code is calculated by combining the hash codes of the `radius` and `center`
     * to ensure a consistent and unique value.
     *
     * 
     * @return An integer representing the hash code of this Circle instance.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = radius.hashCode()
        result = 31 * result + center.hashCode()
        return result
    }

    /**
     * Determines whether the specified point is contained within or lies on the boundary of this circle.
     *
     * 
     * @param point The point to check for containment within the circle.
     * @return `true` if the point is within or on the boundary of the circle; `false` otherwise.
     * @since 1.0.0
     */
    override operator fun contains(point: Point) = center.distanceTo(point) <= radius

    /**
     * Determines whether the current circle intersects with another circle.
     * Two circles intersect if the distance between their centers is less than or equal to the sum of their radii.
     *
     * 
     * @param other The other circle to check for intersection with the current circle.
     * @since 1.0.0
     */
    fun intersects(other: Circle) = center.distanceTo(other.center) <= (radius + other.radius)

    /**
     * Converts the properties of the Circle to a map representation.
     *
     * This method creates a key-value map containing the following Circle properties:
     * - "radius" representing the radius of the circle.
     * - "center" representing the center of the circle as a Point.
     * - "area" representing the calculated area of the circle.
     * - "perimeter" representing the calculated perimeter of the circle.
     *
     * @return A map with the Circle's properties as keys and their corresponding values.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "radius" to radius,
        "center" to center,
        "area" to area,
        "perimeter" to perimeter
    )

    /**
     * Retrieves the value of a property from a map representation of the enclosing object.
     *
     * This operator function supports delegation by fetching a property value from
     * the result of the `_toMap` method, based on the property's name.
     *
     * - `radius` - TYPE: [Double]
     * - `center` - TYPE: [Point]
     * - `area` - TYPE: [Double]
     * - `perimeter` - TYPE: [Double]
     *
     * @param R The expected type of the property value.
     * @param thisRef The reference to the object containing the delegated property, or `null` for top-level or local properties.
     * @param property The metadata for the delegated property, including its name.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}