package dev.tommasop1804.kutils.classes.geometry

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.exceptions.GeometryException
import dev.tommasop1804.kutils.expect
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * Represents a 2D rectangle defined by its top-left corner, width, and height.
 *
 * The rectangle is limited to 2D space, so the z-coordinate of any point
 * involved must always be `0.0`.
 *
 * The class provides properties to access and modify the rectangle's
 * dimensions, corner points, and utility functions for geometric operations
 * such as intersection and containment checks.
 *
 * @property topLeft The top-left corner of the rectangle.
 * @property width The width of the rectangle. Must be non-negative.
 * @property height The height of the rectangle. Must be non-negative.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Rectangle.Companion.Serializer::class)
@JsonDeserialize(using = Rectangle.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Rectangle.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Rectangle.Companion.OldDeserializer::class)
@Suppress("unused")
open class Rectangle (var topLeft: Point = Point(), var width: Double = 0.0, var height: Double = 0.0) : Serializable, Comparable<Rectangle>, Shape2D {
    init {
        !(width < 0 || height < 0) || throw GeometryException("Width and height must be greater than zero.")
        topLeft.z == 0.0 || throw GeometryException("Top left point z-coordinate must be zero.")
    }

    /**
     * Constructs a rectangle using two diagonal points: top-left and bottom-right.
     *
     * Calculates the rectangle's dimensions using the horizontal distance between the
     * x-coordinates and the vertical distance between the y-coordinates of the two points.
     *
     * 
     * @param topLeft The top-left vertex of the rectangle.
     * @param bottomRight The bottom-right vertex of the rectangle.
     * @since 1.0.0
     */
    constructor(topLeft: Point, bottomRight: Point): this(topLeft, bottomRight.x - topLeft.x, topLeft.y - bottomRight.y)

    /**
     * The `topRight` property represents the top-right corner of the rectangle.
     *
     * This property is computed based on the `topLeft` position and the `width` of the rectangle.
     * When setting this property, the z-coordinate of the provided value must be zero,
     * and the width of the rectangle is adjusted accordingly to maintain the specified top-right position.
     *
     * 
     * @throws GeometryException if the z-coordinate of the given point is not zero.
     * @since 1.0.0
     */
    var topRight: Point
        get() = Point(topLeft.x + width, topLeft.y)
        set(value) {
            value.z == 0.0 || throw GeometryException("Top right point z-coordinate must be zero.")
            width = value.x - topLeft.x
        }

    /**
     * Represents the bottom-left corner of the rectangle.
     *
     * The bottom-left corner is computed dynamically based on the rectangle's
     * `topLeft` position and its `height`. The `z` coordinate of this point
     * must always be zero. Updating this property adjusts the rectangle's
     * height to ensure consistency with its dimensions.
     *
     * 
     * @throws GeometryException if the `z` coordinate of the assigned value is not zero.
     * @since 1.0.0
     */
    var bottomLeft: Point
        get() = Point(topLeft.x, topLeft.y - height)
        set(value) {
            value.z == 0.0 || throw GeometryException("Bottom left point z-coordinate must be zero.")
            height = topLeft.y - value.y
        }

    /**
     * Represents the coordinates of the bottom-right corner of the rectangle.
     *
     * Setting this property dynamically adjusts the rectangle's width and height while keeping
     * its top-left position unchanged. The z-coordinate of the assigned value must be zero.
     * Getting this property calculates the bottom-right point based on the rectangle's dimensions.
     *
     * 
     * @throws GeometryException If the z-coordinate of the value being assigned is not equal to zero.
     * @since 1.0.0
     */
    var bottomRight: Point
        get() = Point(topLeft.x + width, topLeft.y - height)
        set(value) {
            value.z == 0.0 || throw GeometryException("Bottom right point z-coordinate must be zero.")
            height = topLeft.y - value.y
            width = value.x - topLeft.x
        }
    /**
     * Computes and provides the centroid (geometric center) of the rectangle.
     *
     * The centroid is calculated based on the rectangle's `topLeft` corner, `width`, and `height`.
     * It represents the central point within the rectangle, where its x-coordinate is
     * horizontally centered and its y-coordinate is vertically centered.
     *
     * 
     * @return A `Point` representing the centroid of the rectangle.
     * @since 1.0.0
     */
    val centroid: Point
        get() = Point(topLeft.x + width / 2, topLeft.y - height / 2)

    /**
     * Represents the area of the rectangle calculated based on its width and height.
     *
     * 
     * @return The computed area as a [Double].
     * @since 1.0.0
     */
    override val area: Double
        get() = width * height

    /**
     * Calculates the perimeter of the rectangle.
     *
     * The perimeter is computed as the sum of twice the width and twice the height.
     *
     * 
     * @since 1.0.0
     */
    override val perimeter: Double
        get() = 2 * (width + height)

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

        class Serializer : ValueSerializer<Rectangle>() {
            override fun serialize(
                value: Rectangle,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                gen.writePOJOProperty("topLeft", value.topLeft)
                gen.writeNumberProperty("width", value.width)
                gen.writeNumberProperty("height", value.height)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Rectangle>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Rectangle {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return Rectangle(
                    node.get("topLeft").traverse(p.objectReadContext()).readValueAs(Point::class.java),
                    node.get("width").traverse(p.objectReadContext()).readValueAs(Double::class.java),
                    node.get("height").traverse(p.objectReadContext()).readValueAs(Double::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<Rectangle>() {
            override fun serialize(value: Rectangle, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeObjectField("topLeft", value.topLeft)
                gen.writeObjectField("width", value.width)
                gen.writeObjectField("height", value.height)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Rectangle>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext?): Rectangle {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Rectangle(
                    node.get("topLeft").traverse(p.codec).readValueAs(Point::class.java),
                    node.get("width").traverse(p.codec).readValueAs(Double::class.java),
                    node.get("height").traverse(p.codec).readValueAs(Double::class.java)
                )
            }
        }
    }

    /**
     * Creates a new rectangle by copying this rectangle and optionally modifying its properties.
     *
     * @param topLeft The top-left corner for the new rectangle. Defaults to the current rectangle's top-left corner.
     * @param width The width of the new rectangle. Defaults to the current rectangle's width.
     * @param height The height of the new rectangle. Defaults to the current rectangle's height.
     * @since 1.0.0
     */
    fun copy(topLeft: Point = this.topLeft, width: Double = this.width, height: Double = this.height) = Rectangle(topLeft, width, height)

    /**
     * Checks whether the given point is contained within the rectangle.
     *
     * The point's `z` coordinate must be zero for it to be considered valid.
     *
     * 
     * @param point The point to be checked for containment within the rectangle.
     * @return `true` if the point is contained within the rectangle, `false` otherwise.
     * @throws GeometryException if the z-coordinate of the given point is not zero.
     * @since 1.0.0
     */
    override operator fun contains(point: Point): Boolean {
        point.z.expect(0.0, causeOf = GeometryException("Point z-coordinate must be zero."))

        val x: Double = point.x
        val y: Double = point.y
        return x >= topLeft.x && x <= topLeft.x + width && y >= topLeft.y && y <= topLeft.y + height
    }

    /**
     * Checks if this rectangle intersects with another rectangle.
     *
     * 
     * @param other the other rectangle to check for intersection
     * @return true if the rectangles intersect, false otherwise
     * @since 1.0.0
     */
    fun intersects(other: Rectangle): Boolean {
        val x1 = other.topLeft.x
        val y1 = other.topLeft.y
        val x2 = x1 + other.width
        val y2 = y1 + other.height

        return x2 > topLeft.x && x1 < topLeft.x + width && y2 > topLeft.y && y1 < topLeft.y + height
    }

    /**
     * Compares this rectangle with another rectangle based on their areas.
     *
     * 
     * @param other the rectangle to compare with.
     * @return a negative integer, zero, or a positive integer if this rectangle's area is less than,
     * equal to, or greater than the other rectangle's area.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Rectangle) = area.compareTo(other.area)

    /**
     * Checks whether this `Rectangle` is equal to another object, with an option
     * to consider the position of the rectangle.
     *
     * 
     * @param other An instance of `Any?` to compare with this `Rectangle`.
     * @param considerPosition If `true`, the position of the rectangle (top-left corner)
     * is considered in the equality check. If `false`, only width and height are compared.
     * @return `true` if the specified object is equal to this `Rectangle`, `false` otherwise.
     * @since 1.0.0
     */
    fun equals(other: Any?, considerPosition: Boolean): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rectangle

        if (considerPosition && (topLeft != other.topLeft)) return false
        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    /**
     * Checks if this rectangle is equal to another object.
     * Delegates the comparison to the `equals(other: Any?, considerPosition: Boolean)` method
     * with `considerPosition` set to `true`.
     *
     * 
     * @param other the object to compare with this rectangle.
     * @return `true` if the objects are considered equal, `false` otherwise.
     * @since 1.0.0
     */
    override operator fun equals(other: Any?) = equals(other, true)

    /**
     * Computes the hash code for this instance based on the `topLeft`, `width`, and `height` properties.
     * This ensures that instances with the same property values generate the same hash code.
     *
     * @return An integer representing the hash code of this rectangle.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = topLeft.hashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + height.hashCode()
        return result
    }

    /**
     * Returns a string representation of the rectangle, including its position and dimensions.
     *
     * The resulting string contains the top-left position of the rectangle, along with
     * its width and height values, formatted as:
     * "Rectangle(topLeft=Point(x, y), width=w, height=h)".
     *
     * 
     * @return a string describing the rectangle
     * @since 1.0.0
     */
    override fun toString() = "Rectangle(topLeft=$topLeft, width=$width, height=$height)"

    /**
     * Converts the rectangle to a [Polygon] representation using its corner points:
     * [topLeft], [topRight], [bottomRight], and [bottomLeft].
     *
     * 
     * @return A [Polygon] composed of the rectangle's corner points.
     * @since 1.0.0
     */
    fun toPolygon() = Polygon(topLeft, topRight, bottomRight, bottomLeft)

    /**
     * Converts the rectangle's properties into a map representation.
     *
     * @return A map containing the rectangle's properties as key-value pairs.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "topLeft" to topLeft,
        "width" to width,
        "height" to height,
        "topRight" to topRight,
        "bottomLeft" to bottomLeft,
        "bottomRight" to bottomRight,
        "centroid" to centroid,
        "area" to area,
        "perimeter" to perimeter
    )

    /**
     * Provides a delegate for accessing property values from a map representation of the object.
     * The method retrieves a value corresponding to the property name from the map
     * and casts it to the expected type.
     *
     * - `topLeft`: The top-left corner of the rectangle - TYPE: [Point].
     * - `width`: The width of the rectangle - TYPE: [Double].
     * - `height`: The height of the rectangle - TYPE: [Double].
     * - `topRight`: The top-right corner of the rectangle - TYPE: [Point].
     * - `bottomLeft`: The bottom-left corner of the rectangle - TYPE: [Point].
     * - `bottomRight`: The bottom-right corner of the rectangle - TYPE: [Point].
     * - `centroid`: The centroid (center point) of the rectangle - TYPE: [Point].
     * - `area`: The area of the rectangle - TYPE: [Double].
     * - `perimeter`: The perimeter of the rectangle - TYPE: [Double].
     *
     * @param thisRef The receiver object for the property delegate. Can be `null`.
     * @param property The metadata of the property for which the value is being retrieved.
     * @return The value of the property, cast to the expected type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}