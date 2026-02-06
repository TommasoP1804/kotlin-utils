package dev.tommasop1804.kutils.classes.geometry

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.MonoPair
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.reflect.KProperty

/**
 * Represents an ellipse defined by a center point, and two radii (`xRadius` and `yRadius`).
 * It implements `Serializable`, `Comparable<Ellipse>`, and `Shape2D` to provide functionality
 * for persistence, comparison based on area, and shape-related operations.
 *
 * @property center The center point of the ellipse.
 * @property xRadius The horizontal radius extending from the center.
 * @property yRadius The vertical radius extending from the center.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Ellipse.Companion.Serializer::class)
@JsonDeserialize(using = Ellipse.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Ellipse.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Ellipse.Companion.OldDeserializer::class)
@Suppress("unused")
class Ellipse (var center: Point = Point(), var xRadius: Double = 0.0, var yRadius: Double = 0.0) : Serializable, Comparable<Ellipse>, Shape2D {
    /**
     * Represents the topmost point of the ellipse.
     * The `top` property is a `Point` located above the `center` of the ellipse
     * by the amount specified in `yRadius`. Updating the `top` point adjusts
     * the `yRadius` of the ellipse to maintain the new position.
     *
     * 
     * @since 1.0.0
     */
    var top: Point
        get() = Point(center.x, center.y + yRadius)
        set(value) { yRadius = value.y - center.y }
    /**
     * Represents the bottommost point of the ellipse.
     * The `bottom` property is calculated based on the center of the ellipse
     * and its vertical radius (`yRadius`). Modifying the `bottom` point
     * will also update the `yRadius`, keeping the center constant.
     *
     * 
     * @since 1.0.0
     */
    var bottom: Point
        get() = Point(center.x, center.y - yRadius)
        set(value) { yRadius = center.y - value.y }
    /**
     * Represents the leftmost point of the ellipse on the horizontal axis.
     * This property dynamically calculates its position based on the current center
     * and `xRadius` of the ellipse, and allows updating the `xRadius` based on a new left point.
     *
     * 
     * @since 1.0.0
     */
    var left: Point
        get() = Point(center.x - xRadius, center.y)
        set(value) { xRadius = center.x - value.x }
    /**
     * Represents the rightmost point on the ellipse.
     *
     * This property calculates the rightmost point by adding the x-radius to the x-coordinate
     * of the ellipse's center. Setting this property adjusts the x-radius of the ellipse
     * proportionally by determining the distance between the new value and the center's x-coordinate.
     *
     * 
     * @since 1.0.0
     */
    var right: Point
        get() = Point(center.x + xRadius, center.y)
        set(value) { xRadius = value.x - center.x }

    /**
     * Represents the area of the ellipse calculated as π multiplied by the x-radius and y-radius.
     *
     * The value is computed dynamically based on the current dimensions of the ellipse.
     *
     * 
     * @since 1.0.0
     */
    override val area: Double
        get() = Math.PI * xRadius * yRadius
    /**
     * Computes the perimeter of the ellipse based on its `xRadius` and `yRadius`.
     * This property calculates an approximation of the perimeter using the formula derived
     * from Ramanujan's second approximation, which is accurate for most cases.
     *
     * The calculation ensures robustness by handling edge cases such as when one or both of the radii are zero,
     * indicating degenerate ellipses.
     *
     * The approximation considers the larger radius (`xRadius` or `yRadius`) as `a` and the smaller as `b`.
     * It swaps the values internally if `xRadius` is less than `yRadius`.
     *
     * 
     * @return The computed perimeter of the ellipse as a `Double`.
     * @since 1.0.0
     */
    override val perimeter: Double
        get() {
            var a: Double = xRadius
            var b: Double = yRadius

            if (a == 0.0 && b == 0.0) return 0.0
            if (a < b) {
                val temp = a
                a = b
                b = temp
            }
            if (b == 0.0) return 4 * a
            val h = ((a - b) / (a + b)).pow(2.0)
            return Math.PI * (a + b) * (1 + (3 * h) / (10 + sqrt(4 - 3 * h)))
        }
    /**
     * Calculates the foci of the ellipse based on its center, x-radius, and y-radius.
     * The foci are the two principal points such that the sum of their distances
     * to any point on the ellipse remains constant.
     *
     * When the ellipse is a circle (`xRadius` equals `yRadius`) or has no dimensions
     * (either `xRadius` or `yRadius` is zero), the foci are positioned at the center of the ellipse.
     * Otherwise, they are calculated based on the major and minor axes.
     *
     * 
     * @since 1.0.0
     */
    val foci: MonoPair<Point>
        get() {
            val a: Double
            val b: Double
            val majorAxisIsX: Boolean

            if (xRadius >= yRadius) {
                a = xRadius
                b = yRadius
                majorAxisIsX = true
            } else {
                a = yRadius
                b = xRadius
                majorAxisIsX = false
            }

            if (a == b || a == 0.0) return Pair(center, center)

            val cSquared = a * a - b * b
            val c = if (cSquared > TOLERANCE) sqrt(cSquared) else 0.0
            val focus1: Point
            val focus2: Point

            if (majorAxisIsX) {
                focus1 = Point(center.x - c, center.y)
                focus2 = Point(center.x + c, center.y)
            } else {
                focus1 = Point(center.x, center.y - c)
                focus2 = Point(center.x, center.y + c)
            }

            return Pair(focus1, focus2)
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
         * A constant representing the tolerance level used for floating-point comparisons or other calculations
         * in the `Ellipse` class. This value helps mitigate issues caused by precision errors.
         *
         * @since 1.0.0
         */
        private const val TOLERANCE = 1e-12

        class Serializer : ValueSerializer<Ellipse>() {
            override fun serialize(value: Ellipse, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                gen.writePOJOProperty("center", value.center)
                gen.writeNumberProperty("radiusX", value.xRadius)
                gen.writeNumberProperty("radiusY", value.yRadius)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Ellipse>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Ellipse {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return Ellipse(
                    node.get("center").traverse(p.objectReadContext()).readValueAs(Point::class.java),
                    node.get("radiusX").traverse(p.objectReadContext()).readValueAs(Double::class.java),
                    node.get("radiusY").traverse(p.objectReadContext()).readValueAs(Double::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<Ellipse>() {
            override fun serialize(value: Ellipse, gen: JsonGenerator, serializers: SerializerProvider?) {
                gen.writeStartObject()
                gen.writeObjectField("center", value.center)
                gen.writeNumberField("radiusX", value.xRadius)
                gen.writeNumberField("radiusY", value.yRadius)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Ellipse>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Ellipse {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Ellipse(
                    node.get("center").traverse(p.codec).readValueAs(Point::class.java),
                    node.get("radiusX").traverse(p.codec).readValueAs(Double::class.java),
                    node.get("radiusY").traverse(p.codec).readValueAs(Double::class.java)
                )
            }
        }
    }

    /**
     * Creates a copy of the current `Ellipse` instance with the option to modify its properties.
     *
     * @param center The center point of the copied ellipse. Defaults to the center of the current ellipse.
     * @param xRadius The x-axis radius of the copied ellipse. Defaults to the x-axis radius of the current ellipse.
     * @param yRadius The y-axis radius of the copied ellipse. Defaults to the y-axis radius of the current ellipse.
     * @since 1.0.0
     */
    fun copy(center: Point = this.center, xRadius: Double = this.xRadius, yRadius: Double = this.yRadius) = Ellipse(center, xRadius, yRadius)

    /**
     * Checks if the given point is contained within the bounds of the ellipse.
     *
     * 
     * @param point The point to check for containment within the ellipse.
     * @return `true` if the point lies within or on the boundary of the ellipse; `false` otherwise.
     * @since 1.0.0
     */
    override operator fun contains(point: Point): Boolean {
        val x: Double = point.x
        val y: Double = point.y

        var a: Double = xRadius
        var b: Double = yRadius

        if (a == 0.0 && b == 0.0) return false
        if (a < b) {
            val temp = a
            a = b
            b = temp
        }
        if (b == 0.0) return x >= -a && x <= a

        val h = ((a - b) / (a + b)).pow(2.0)
        val x2 = (x - center.x).pow(2.0)
        val y2 = (y - center.y).pow(2.0)

        return (x2 / (a * a)) + (y2 / (b * b)) <= 1 - h
    }

    /**
     * Compares this ellipse's area with the area of another ellipse for order.
     *
     * 
     * @param other the ellipse to be compared with this ellipse
     * @return a negative integer, zero, or a positive integer as the area of this ellipse
     *         is less than, equal to, or greater than the area of the specified ellipse
     * @since 1.0.0
     */
    override fun compareTo(other: Ellipse) = area.compareTo(other.area)

    /**
     * Compares this `Ellipse` object with another object for equality.
     * Two `Ellipse` objects are considered equal if their `xRadius` and `yRadius` are the same.
     * Optionally, their center positions are also compared if `considerPosition` is `true`.
     *
     * 
     * @param other An instance of `Any?` to compare with this `Ellipse` object.
     * @param considerPosition A `Boolean` indicating whether the positions (center) of the ellipses
     * should also be included in the equality check.
     * @return `true` if the specified object is equal to this `Ellipse`, `false` otherwise.
     * @since 1.0.0
     */
    fun equals(other: Any?, considerPosition: Boolean): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ellipse

        if (xRadius != other.xRadius) return false
        if (yRadius != other.yRadius) return false
        if (considerPosition && (center != other.center)) return false

        return true
    }

    /**
     * Checks whether the current object is equal to the specified object.
     * Delegates the equality check to the `equals(other: Any?, considerPosition: Boolean)` method with the "consider position" option set to true.
     *
     * 
     * @param other The object to compare with the current instance.
     * @since 1.0.0
     */
    override fun equals(other: Any?) = equals(other, true)

    /**
     * Computes the hash code for this instance of Ellipse based on its `xRadius`, `yRadius`,
     * and `center` properties. The hash code is generated using a combination of these
     * properties to ensure consistent and unique values for equivalent instances.
     *
     * 
     * @return An integer representing the hash code of this instance.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = xRadius.hashCode()
        result = 31 * result + yRadius.hashCode()
        result = 31 * result + center.hashCode()
        return result
    }

    /**
     * Returns a string representation of the ellipse, including its center point,
     * x-radius, and y-radius.
     *
     * 
     * @return A string representation of the ellipse in the format:
     * "Ellipse(center=<center>, xRadius=<xRadius>, yRadius=<yRadius>)".
     * @since 1.0.0
     */
    override fun toString() = "Ellipse(center=$center, xRadius=$xRadius, yRadius=$yRadius)"

    /**
     * Converts the properties of the current `Ellipse` instance into a map representation.
     *
     * This method is intended for internal use only.
     *
     * @return A map containing the properties of the ellipse.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "center" to center,
        "xRadius" to xRadius,
        "yRadius" to yRadius,
        "top" to top,
        "bottom" to bottom,
        "left" to left,
        "right" to right,
        "area" to area,
        "perimeter" to perimeter,
        "foci" to foci
    )

    /**
     * Allows a delegated property to retrieve its value from a map representation of the `Ellipse` instance.
     *
     * - `center`: The center point of the ellipse - TYPE: [Point].
     * - `xRadius`: The x-axis radius of the ellipse - TYPE: [Double].
     * - `yRadius`: The y-axis radius of the ellipse - TYPE: [Double].
     * - `top`: The top boundary of the ellipse - TYPE: [Point].
     * - `bottom`: The bottom boundary of the ellipse - TYPE: [Point].
     * - `left`: The left boundary of the ellipse - TYPE: [Point].
     * - `right`: The right boundary of the ellipse - TYPE: [Point].
     * - `area`: The area of the ellipse - TYPE: [Double].
     * - `perimeter`: The perimeter of the ellipse - TYPE: [Double].
     * - `foci`: The focal points of the ellipse - TYPE: `Pair<Point, Point>`.
     *
     * @param thisRef The object for which the value is being retrieved. Can be `null`.
     * @param property The metadata of the property being accessed.
     * @return The value of the specified property, cast to the expected type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}