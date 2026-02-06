package dev.tommasop1804.kutils.classes.geometry

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.Double3
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import kotlin.math.abs
import kotlin.math.acos
import kotlin.reflect.KProperty

/**
 * Represents a triangle defined by three vertices `a`, `b`, and `c`. This class provides properties
 * and methods for computing geometric properties such as area, perimeter, side lengths, angles,
 * and triangle type.
 *
 * The triangle coordinates are provided as instances of the `Point` class, allowing for precision
 * geometric operations. This class implements `Shape2D`, providing compatibility with other 2D shapes.
 *
 * Serialization and deserialization are supported via custom serializers.
 *
 * @constructor Creates a triangle from three vertices `a`, `b`, and `c`. Defaults to a triangle with
 * all vertex coordinates at the origin.
 * @property a The first vertex of the triangle.
 * @property b The second vertex of the triangle.
 * @property c The third vertex of the triangle.
 * @since 1.0.0
 */
@JsonSerialize(using = Triangle.Companion.Serializer::class)
@JsonDeserialize(using = Triangle.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Triangle.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Triangle.Companion.OldDeserializer::class)
@Suppress("unused")
class Triangle (val a: Point = Point(), val b: Point = Point(), val c: Point = Point()) : Serializable, Comparable<Triangle>, Shape2D {
    /**
     * Represents the area of the triangle calculated using the coordinates of its vertices.
     * The formula utilizes the determinant method to ensure accuracy and accounts for
     * both clockwise and counterclockwise vertex ordering by taking the absolute value.
     *
     * 
     * @since 1.0.0
     */
    override val area: Double
        get() = 0.5 * abs((a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)))

    /**
     * The total length of the perimeter of this triangle, calculated as the sum of the distances between its vertices.
     *
     * 
     * @return The perimeter of the triangle as a Double.
     * @since 1.0.0
     */
    override val perimeter: Double
        get() = a.distanceTo(b) + b.distanceTo(c) + c.distanceTo(a)

    /**
     * Retrieves the lengths of the three sides of the triangle as a `Triple` of `Double` values.
     *
     * The side lengths are calculated as the Euclidean distances between the triangle's
     * vertices: `a` to `b`, `b` to `c`, and `c` to `a`.
     *
     * 
     * @return A `Triple` containing the lengths of the sides of the triangle,
     * where the first value represents the distance between `a` and `b`,
     * the second value represents the distance between `b` and `c`,
     * and the third value represents the distance between `c` and `a`.
     * @since 1.0.0
     */
    val sideLengths: Double3
        get() = Triple(a.distanceTo(b), b.distanceTo(c), c.distanceTo(a))

    /**
     * Provides the angles of the triangle in degrees calculated using the vertices' positions and side lengths.
     * The angles are represented as a [Triple], where the first, second, and third values represent the angles
     * at the vertices corresponding to points `a`, `b`, and `c` respectively.
     *
     * If the triangle is degenerate (having zero area or sides below the defined tolerance), the angles are
     * returned as `Double.NaN`.
     *
     * 
     * @return A [Triple] containing the three angles of the triangle in degrees.
     * @since 1.0.0
     */
    val angles: Double3
        get() {
            val ab = a.distanceTo(b)
            val bc = b.distanceTo(c)
            val ca = c.distanceTo(a)
            if (ab < TOLERANCE || bc < TOLERANCE || ca < TOLERANCE || area < TOLERANCE) return Triple(
                Double.NaN,
                Double.NaN,
                Double.NaN
            )

            val angleARad = acos((ab * ab + ca * ca - bc * bc) / (2 * ab * ca))
            val angleBRad = acos((ab * ab + bc * bc - ca * ca) / (2 * ab * bc))
            val angleCRad = acos((bc * bc + ca * ca - ab * ab) / (2 * bc * ca))

            return Triple(
                Math.toDegrees(angleARad),
                Math.toDegrees(angleBRad),
                Math.toDegrees(angleCRad)
            )
        }

    /**
     * Determines the type of the triangle based on the lengths of its sides.
     *
     * The classification is as follows:
     *
     * - `DEGENERATE`: The triangle is considered degenerate if the area is less than a defined tolerance
     *   or any of its sides are less than the same tolerance, indicating a "collapsed" triangle.
     * - `EQUILATERAL`: If all three sides have equal lengths.
     * - `ISOSCELES`: If at least two sides have equal lengths.
     * - `SCALENE`: If all sides have different lengths.
     *
     * The classification takes into account a tolerance value to handle floating-point imprecision.
     *
     * 
     * @since 1.0.0
     */
    val triangleType: TriangleType
        get() {
            val sides = sideLengths
            val ab: Double = sides.first
            val bc: Double = sides.second
            val ca: Double = sides.third

            if (ab < TOLERANCE || bc < TOLERANCE || ca < TOLERANCE || area < TOLERANCE) return TriangleType.DEGENERATE

            val abEqBc = abs(ab - bc) < TOLERANCE
            val bcEqCa = abs(bc - ca) < TOLERANCE
            val caEqAb = abs(ca - ab) < TOLERANCE

            if (abEqBc && bcEqCa) return TriangleType.EQUILATERAL
            if (abEqBc || bcEqCa || caEqAb) return TriangleType.ISOSCELES
            return TriangleType.SCALENE
        }

    val centroid: Point
        get() = Point((a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3)

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

        class Serializer : ValueSerializer<Triangle>() {
            override fun serialize(value: Triangle, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                gen.writePOJOProperty("a", value.a)
                gen.writePOJOProperty("b", value.b)
                gen.writePOJOProperty("c", value.c)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Triangle?>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Triangle {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return Triangle(
                    node.get("a").traverse(p.objectReadContext()).readValueAs(Point::class.java),
                    node.get("b").traverse(p.objectReadContext()).readValueAs(Point::class.java),
                    node.get("c").traverse(p.objectReadContext()).readValueAs(Point::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<Triangle>() {
            override fun serialize(value: Triangle, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeObjectField("a", value.a)
                gen.writeObjectField("b", value.b)
                gen.writeObjectField("c", value.c)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Triangle?>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Triangle {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Triangle(
                    node.get("a").traverse(p.codec).readValueAs(Point::class.java),
                    node.get("b").traverse(p.codec).readValueAs(Point::class.java),
                    node.get("c").traverse(p.codec).readValueAs(Point::class.java)
                )
            }
        }
    }

    /**
     * Creates a new instance of a triangle with optionally updated vertices.
     *
     * This method allows creating a copy of an existing triangle with one or more vertices replaced.
     * If no parameters are provided, the original vertices (`a`, `b`, `c`) are retained.
     *
     * @param a The new position for vertex `a`. Defaults to the current vertex `a`.
     * @param b The new position for vertex `b`. Defaults to the current vertex `b`.
     * @param c The new position for vertex `c`. Defaults to the current vertex `c`.
     * @return A new instance of [Triangle] with the specified vertices.
     * @since 1.0.0
     */
    fun copy(a: Point = this.a, b: Point = this.b, c: Point = this.c) = Triangle(a, b, c)

    /**
     * Determines if the specified point lies inside the triangle or not.
     *
     * 
     * @param point The point to be checked for containment within the triangle.
     * @return `true` if the point is inside or lies on the edge of the triangle; otherwise, `false`.
     * @since 1.0.0
     */
    override operator fun contains(point: Point): Boolean {
        val totalArea: Double = area

        val t1 = Triangle(point, b, c)
        val t2 = Triangle(a, point, c)
        val t3 = Triangle(a, b, point)

        val area1: Double = t1.area
        val area2: Double = t2.area
        val area3: Double = t3.area

        return abs(totalArea - (area1 + area2 + area3)) < TOLERANCE
    }

    /**
     * Compares this triangle with the specified other triangle based on their areas.
     *
     * 
     * @param other The other triangle to compare with.
     * @return A negative integer, zero, or a positive integer as this triangle's area
     *         is less than, equal to, or greater than the specified triangle's area.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Triangle) = area.compareTo(other.area)

    /**
     * Determines if this triangle is equal to another object, optionally considering vertex position.
     *
     * 
     * @param other An object to compare with this triangle.
     * @param considerPosition If `true`, considers vertex positions (`a`, `b`, `c`) in equality check;
     * otherwise, checks based on calculated properties (e.g., perimeter and area).
     * @return `true` if the specified object is equal to this triangle based on the criteria;
     * `false` otherwise.
     * @since 1.0.0
     */
    fun equals(other: Any?, considerPosition: Boolean): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Triangle

        if (considerPosition) {
            if (a != other.a) return false
            if (b != other.b) return false
            if (c != other.c) return false
        } else {
            if (perimeter != other.perimeter) return false
            if (area != other.area) return false
            if (triangleType != other.triangleType) return false
        }

        return true
    }

    /**
     * Compares this triangle to the specified object to determine if they are equal.
     *
     * 
     * @param other The object to be compared with this triangle.
     * @return `true` if the specified object is equal to this triangle; otherwise, `false`.
     * @since 1.0.0
     */
    override fun equals(other: Any?) = equals(other, true)

    /**
     * Computes the hash code for this triangle instance based on its properties.
     * The hash code is calculated by combining the hash codes of `a`, `b`, and `c`,
     * which represent key attributes of the triangle.
     *
     * 
     * @return An integer representing the hash code of this triangle.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = a.hashCode()
        result = 31 * result + b.hashCode()
        result = 31 * result + c.hashCode()
        return result
    }

    /**
     * Returns a string representation of the triangle.
     *
     * The string includes the lengths of the sides of the triangle in the format:
     * `Triangle[a, b, c]`.
     *
     * This method provides a concise summary of the triangle's side lengths
     * for debugging or logging purposes.
     *
     * 
     * @return A string describing the triangle's sides.
     * @since 1.0.0
     */
    override fun toString() = "Triangle[$a, $b, $c]"

    /**
     * Converts this triangle into a polygon representation using its vertices.
     *
     * This method constructs a [Polygon] instance using the triangle's vertices (`a`, `b`, `c`)
     * and returns it. The resulting polygon shares the same vertices as this triangle.
     *
     * 
     * @return A [Polygon] created using the vertices of this triangle.
     * @since 1.0.0
     */
    fun toPolygon() = Polygon(a, b, c)

    /**
     * Converts the properties of the triangle into a map representation.
     *
     * This method creates a map where each key corresponds to a property of the triangle
     * (e.g., vertices, area, perimeter, etc.), and the value is the associated value for that property.
     * It is intended for use in cases where a lightweight, key-value representation of the triangle is needed.
     *
     * @return A [Map] containing the properties of the triangle as key-value pairs.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "a" to a,
        "b" to b,
        "c" to c,
        "area" to area,
        "perimeter" to perimeter,
        "sideLengths" to sideLengths,
        "angles" to angles,
        "triangleType" to triangleType,
        "centroid" to centroid
    )

    /**
     * Provides the value of a specified property for the containing `Triangle` instance.
     *
     * The property values are extracted from a map representation of the triangle's attributes.
     *
     * - `a` - TYPE: [Point]
     * - `b` - TYPE: [Point]
     * - `c` - TYPE: [Point]
     * - `area` - TYPE: [Double]
     * - `perimeter` - TYPE: [Double]
     * - `sideLengths` - TYPE: `Triple<Double, Double, Double>`
     * - `angles` - TYPE: `Triple<Double, Double, Double>`
     * - `triangleType` - TYPE: [TriangleType]
     * - `centroid` - TYPE: [Point]
     *
     * @param thisRef The reference to the instance from which the property is retrieved. Can be `null`.
     * @param property A reference to the property whose value needs to be resolved.
     * @return The value of the specified property, cast to the expected type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R

    /**
     * Enum representing the different classifications of triangles based on their side lengths.
     *
     * @since 1.0.0
     * @author Tommaso Pastorelli
     */
    enum class TriangleType {
        /**
         * Represents a degenerate triangle.
         *
         * A triangle is classified as `DEGENERATE` when the sum of the lengths of two sides
         * is equal to the length of the third side. This results in a "collapsed" triangle, which
         * effectively forms a straight line.
         *
         * @since 1.0.0
         */
        DEGENERATE,
        /**
         * Represents a triangle where all three sides are of equal length.
         *
         * This type of triangle is characterized by equal angles of 60 degrees
         * and equal side lengths, making it highly symmetrical and unique among
         * triangle classifications.
         *
         * Belongs to the `TriangleType` enum, which categorizes triangles
         * based on their side lengths.
         * @since 1.0.0
         */
        EQUILATERAL,
        /**
         * Represents a type of triangle where at least two sides have equal length.
         *
         * An isosceles triangle is characterized by the following:
         *
         *  * It has two sides of equal length.
         *  * The angles opposite these equal sides are also equal.
         *
         * This is one of the four primary classifications of triangles based on side lengths.
         * @since 1.0.0
         */
        ISOSCELES,
        /**
         * Represents a triangle where all sides are of different lengths.
         *
         * A scalene triangle is characterized by having no equal sides and, consequently,
         * no equal angles. This classification is based solely on the distinctiveness of its side lengths.
         * @since 1.0.0
         */
        SCALENE
    }
}