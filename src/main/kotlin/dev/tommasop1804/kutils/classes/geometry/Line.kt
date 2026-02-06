package dev.tommasop1804.kutils.classes.geometry

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.classes.geometry.Line.Companion.TOLERANCE
import dev.tommasop1804.kutils.exceptions.GeometryException
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import kotlin.math.*
import kotlin.reflect.KProperty

/**
 * Represents a geometric line in a 2D or 3D space.
 *
 * @property start The starting point of the line.
 * @property end The ending point of the line.
 * @property length The length of the line segment.
 * @property midpoint The midpoint of the line segment.
 * @property isHorizontal Indicates whether the line is horizontal.
 * @property isVertical Indicates whether the line is vertical.
 * @property directionVector A vector indicating the direction of the line.
 * @property slope The slope of the line (if applicable in 2D space).
 * @property yIntercept The y-intercept of the line (if applicable in 2D space).
 * @property xIntercept The x-intercept of the line (if applicable in 2D space).
 * @property intercept A pair representing the x and y intercepts of the line (applicable in 2D space).
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Line.Companion.Serializer::class)
@JsonDeserialize(using = Line.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Line.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Line.Companion.OldDeserializer::class)
@Suppress("unused")
class Line (var start: Point = Point(), var end: Point = Point()) : Serializable, Comparable<Line> {
    /**
     * The length of the line, calculated as the Euclidean distance between its start and end points.
     *
     * This property provides a read-only calculation of the straight-line distance in 3D space.
     *
     * 
     * @return The Euclidean distance between the start and end points as a Double.
     * @since 1.0.0
     */
    val length: Double
        get() = start.distanceTo(end)
    /**
     * The midpoint of the line segment defined by the `start` and `end` points.
     * Calculates the exact center point between the two ends of the line.
     *
     * 
     * @since 1.0.0
     */
    val midpoint: Point
        get() = start.midpoint(end)
    /**
     * Indicates whether the line is horizontal in the coordinate system.
     *
     * This property checks if the z-coordinates of the start and end points are either both zero (indicating a 2D horizontal line)
     * or differ by a value that is within an acceptable [TOLERANCE]. For such cases, it further verifies if the y-coordinates
     * of the start and end points are equal.
     *
     * This property is read-only and determined dynamically based on the coordinates
     * of the line's start and end points.
     *
     * 
     * @return `true` if the line is considered horizontal; otherwise, `false`.
     * @since 1.0.0
     */
    val isHorizontal: Boolean
        get() = if (start.z == 0.0 && end.z == 0.0) start.y == end.y else abs(start.z - end.z) < TOLERANCE
    /**
     * Indicates whether the line is vertical. A line is considered vertical if its start and end points
     * share the same x-coordinate, provided both z-coordinates are zero, or if the absolute difference
     * between the x-coordinates and y-coordinates is within the defined tolerance when projected into
     * the plane.
     *
     * The calculation accounts for a tolerance to handle potential floating-point inaccuracies.
     *
     * 
     * @since 1.0.0
     */
    val isVertical: Boolean
        get() = if (start.z == 0.0 && end.z == 0.0) start.x == end.x else abs(start.x - end.x) < TOLERANCE && abs(start.y - end.y) < TOLERANCE
    /**
     * Returns the direction vector of the line as a `Point`, representing the vector
     * from the `start` point to the `end` point of the line.
     *
     * The direction vector is calculated by subtracting the coordinates of the `start`
     * point from the coordinates of the `end` point:
     * - x-component: `end.x - start.x`
     * - y-component: `end.y - start.y`
     * - z-component: `end.z - start.z`
     *
     * 
     * @since 1.0.0
     */
    val directionVector: Point
        get() {
            val dx = end.x - start.x
            val dy = end.y - start.y
            val dz = end.z - start.z
            return Point(dx, dy, dz)
        }
    /**
     * Calculates the slope of a 2D line segment defined by the [start] and [end] points.
     *
     * The slope is computed as the change in y-coordinates divided by the change in x-coordinates.
     * For vertical lines, the slope is considered undefined and represented by `Double.NaN`.
     *
     * Throws a [GeometryException] if the line is not a 2D line,
     * i.e., if the z-coordinates of the [start] or [end] points are non-zero.
     *
     * 
     * @return The slope of the line as a [Double].
     *         Returns `Double.NaN` if the line is vertical.
     * @since 1.0.0
     */
    val slope: Double
        get() {
            if (start.z != 0.0 || end.z != 0.0) throw GeometryException("Only for 2D lines")
            if (isVertical) return Double.NaN
            return (end.y - start.y) / (end.x - start.x)
        }
    /**
     * Represents the y-intercept of a two-dimensional line. The y-intercept is the point at which the
     * line crosses the y-axis (x = 0). For vertical lines, this value is not defined and will return NaN.
     *
     * Accessing this property will throw an exception if the line is not in a two-dimensional space
     * (i.e., if either the `z` coordinate of the `start` or `end` point is non-zero).
     *
     * The value is calculated using the formula `yIntercept = start.y - slope * start.x`.
     *
     * 
     * @throws GeometryException if the line is not confined to a 2D plane
     * @since 1.0.0
     */
    val yIntercept: Double
        get() {
            if (start.z != 0.0 || end.z != 0.0) throw GeometryException("Only for 2D lines")
            if (isVertical) return Double.NaN
            return start.y - slope * start.x
        }
    /**
     * The x-intercept of the line, representing the x-coordinate where the line crosses the x-axis.
     * This property is intended for 2-dimensional lines only and will throw an exception if applied to a 3-dimensional line.
     * If the line is horizontal, the x-intercept is undefined, represented by `Double.NaN`.
     *
     * 
     * @throws GeometryException if the line is not strictly two-dimensional.
     * @since 1.0.0
     */
    val xIntercept: Double
        get() {
            if (start.z != 0.0 || end.z != 0.0) throw GeometryException("Only for 2D lines")
            if (isHorizontal) return Double.NaN
            return start.x - slope * start.y
        }
    /**
     * Calculates the intercept of the line with the axes for 2D lines.
     *
     * 
     * @throws GeometryException if the line is not 2D (z-coordinates for start or end are non-zero).
     * @return A [Point] instance representing the intercept of the line.
     *         If the line is vertical, the intercept is with the x-axis.
     *         If the line is horizontal, the intercept is with the y-axis.
     *         For other lines, this represents the intersection of the line with the axes.
     * @since 1.0.0
     */
    val intercept: Point
        get() {
            if (start.z != 0.0 || end.z != 0.0) throw GeometryException("Only for 2D lines")

            if (isVertical) return Point(start.x, yIntercept)
            if (isHorizontal) return Point(xIntercept, start.y)
            return Point(xIntercept, yIntercept)
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
         * Represents the tolerance value used for comparison
         * where precision is a key factor for computations
         * within the context of the `Line` class.
         *
         * This value is commonly employed to account for
         * floating-point arithmetic errors, providing a
         * margin within which values are considered equal.
         *
         * @since 1.0.0
         */
        private const val TOLERANCE = 1e-9
        /**
         * Defines a very strict tolerance value used to determine precision or
         * equality in mathematical or geometric operations. This is suitable for
         * use cases requiring extremely high accuracy.
         *
         * @since 1.0.0
         */
        private const val STRICT_TOLERANCE = 1e-12
        /**
         * Represents an extremely small tolerance value used for precision-sensitive
         * comparisons or calculations within the `Line` class.
         *
         * This constant defines the strictest tolerance level, allowing for highly accurate
         * evaluations where minimal differences are critical to avoid rounding errors or
         * precision issues.
         *
         * @since 1.0.0
         */
        private const val SO_STRICT_TOLERANCE = 1e-18

        class Serializer : ValueSerializer<Line>() {
            override fun serialize(value: Line, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                gen.writePOJOProperty("start", value.start)
                gen.writePOJOProperty("end", value.end)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Line>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Line {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return Line(
                    node.get("start").traverse(p.objectReadContext()).readValueAs(Point::class.java),
                    node.get("end").traverse(p.objectReadContext()).readValueAs(Point::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<Line>() {
            override fun serialize(value: Line, gen: JsonGenerator, serializers: SerializerProvider?) {
                gen.writeStartObject()
                gen.writeObjectField("start", value.start)
                gen.writeObjectField("end", value.end)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Line>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Line {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Line(
                    node.get("start").traverse(p.codec).readValueAs(Point::class.java),
                    node.get("end").traverse(p.codec).readValueAs(Point::class.java)
                )
            }
        }
    }

    /**
     * Creates a copy of the current line with updated start and/or end points.
     *
     * @param start The new starting point of the line. Defaults to the current line's start point.
     * @param end The new ending point of the line. Defaults to the current line's end point.
     * @return A new Line instance with the specified or default start and end points.
     * @since 1.0.0
     */
    fun copy(start: Point = this.start, end: Point = this.end): Line = Line(start, end)

    /**
     * Checks whether the current line is parallel to the specified line.
     *
     * 
     * @param other The line to compare against for parallelism.
     * @return `true` if the current line is parallel to the specified line, otherwise `false`.
     * @since 1.0.0
     */
    fun isParallel(other: Line): Boolean {
        if (start.z == 0.0 && end.z == 0.0) {
            if (isVertical && other.isVertical) return true
            if (isHorizontal && other.isHorizontal) return true
            return slope == other.slope
        }

        val dir1: Point = directionVector
        val dir2: Point = other.directionVector

        // Handle zero vectors (lines defined by single points)
        val mag1Sq = dir1.x * dir1.x + dir1.y * dir1.y + dir1.z * dir1.z
        val mag2Sq = dir2.x * dir2.x + dir2.y * dir2.y + dir2.z * dir2.z

        if (mag1Sq < SO_STRICT_TOLERANCE || mag2Sq < SO_STRICT_TOLERANCE) {
            // Define behavior: Are points parallel to everything/nothing?
            // Option 1: A point is parallel only to another point or a zero-length line.
            // Option 2: Consider them non-parallel to actual lines (more common)
            // return false; if either is zero length but not both? Needs clarification.
            // Let's assume standard definition: need non-zero vectors for parallelism direction.
            // If both are points, they could be considered "parallel" in a degenerate sense.
            return mag1Sq < SO_STRICT_TOLERANCE && mag2Sq < SO_STRICT_TOLERANCE // Treat points as parallel only to other points
        }

        // Calculate cross product: dir1 x dir2
        val crossX = dir1.y * dir2.z - dir1.z * dir2.y
        val crossY = dir1.z * dir2.x - dir1.x * dir2.z
        val crossZ = dir1.x * dir2.y - dir1.y * dir2.x

        // Check if the cross product is the zero vector (within tolerance)
        return abs(crossX) < TOLERANCE && abs(crossY) < TOLERANCE && abs(crossZ) < TOLERANCE
    }

    /**
     * Determines if the current line is perpendicular to the given line.
     *
     * 
     * @param other The other line to check for perpendicularity.
     * @return True if the current line is perpendicular to the given line, false otherwise.
     * @since 1.0.0
     */
    fun isPerpendicular(other: Line): Boolean {
        if (start.z == 0.0 && end.z == 0.0) {
            if (isVertical && other.isHorizontal) return true
            if (isHorizontal && other.isVertical) return true
            return slope == -other.slope
        }

        val dir1: Point = directionVector
        val dir2: Point = other.directionVector

        // Dot product requires non-zero vectors to define angle meaningfully
        val mag1Sq = dir1.x * dir1.x + dir1.y * dir1.y + dir1.z * dir1.z
        val mag2Sq = dir2.x * dir2.x + dir2.y * dir2.y + dir2.z * dir2.z
        if (mag1Sq < SO_STRICT_TOLERANCE || mag2Sq < SO_STRICT_TOLERANCE) {
            return false // Cannot determine perpendicularity with zero-length lines
        }

        // Calculate dot product: dir1 · dir2
        val dotProduct = dir1.x * dir2.x + dir1.y * dir2.y + dir1.z * dir2.z

        // Check if the dot product is close to zero (within tolerance)
        return abs(dotProduct) < TOLERANCE
    }

    /**
     * Determines whether the current line intersects with another line.
     *
     * 
     * @param other The other line to check for intersection.
     * @return True if the two lines intersect, false otherwise.
     * @since 1.0.0
     */
    fun intersects(other: Line): Boolean {
        val d1: Double = direction(other.start, other.end, start)
        val d2: Double = direction(other.start, other.end, end)
        val d3: Double = direction(start, end, other.start)
        val d4: Double = direction(start, end, other.end)

        return d1 != d2 && d3 != d4
    }

    /**
     * Calculates the direction value for three points, which can determine the relative orientation of the points.
     *
     * 
     * @param p1 The first point.
     * @param p2 The second point.
     * @param p3 The third point.
     * @return A value representing the clockwise or counterclockwise orientation of the three points.
     * @since 1.0.0
     */
    private fun direction(p1: Point, p2: Point, p3: Point) = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)

    /**
     * Determines the intersection point of the current line with another line.
     * If the lines are parallel, horizontal, or vertical, the result is null.
     * This function supports only 2D lines. If any of the involved lines has a non-zero z-coordinate,
     * the operation will throw an exception.
     *
     * 
     * @param other The other line to intersect with.
     * @return The intersection point as a [Point], or null if the lines do not intersect.
     * @throws GeometryException If any of the lines are not 2D.
     * @since 1.0.0
     */
    fun intersection(other: Line): Point? {
        if (start.z != 0.0 || end.z != 0.0 || other.start.z != 0.0 || other.end.z != 0.0)
            throw GeometryException("Only for 2D lines")

        if (isVertical || other.isVertical) return null
        if (isHorizontal && other.isHorizontal) return null
        if (isHorizontal) return Point(other.xIntercept, start.y)
        if (other.isHorizontal) return Point(start.x, other.yIntercept)
        val x: Double = (other.yIntercept - yIntercept) / (slope - other.slope)
        val y: Double = slope * x + yIntercept
        return Point(x, y)
    }

    /**
     * Checks whether the specified point lies on the line segment defined by this line.
     *
     * This function determines if the given point is collinear with the line and lies within the segment bounds.
     *
     * 
     * @param point The point to be checked for containment on the line segment.
     * @return `true` if the point lies on the line segment, `false` otherwise.
     * @since 1.0.0
     */
    operator fun contains(point: Point): Boolean {
        if (start.z == 0.0 && end.z == 0.0) {
            val crossProduct = (point.y - start.y) * (end.x - start.x) - (point.x - start.x) * (end.y - start.y)
            if (abs(crossProduct) > TOLERANCE) return false

            val dotProduct = (point.x - start.x) * (end.x - start.x) +
                    (point.y - start.y) * (end.y - start.y)
            if (dotProduct < 0) return false

            val squaredLength = (end.x - start.x).pow(2.0) + (end.y - start.y).pow(2.0)
            return dotProduct <= squaredLength
        }

        val vecSp = Point(point.x - start.x, point.y - start.y, point.z - start.z)
        val vecSe: Point = directionVector

        // 1. Check Collinearity (Cross product vec_sp x vec_se == 0)
        val crossX = vecSp.y * vecSe.z - vecSp.z * vecSe.y
        val crossY = vecSp.z * vecSe.x - vecSp.x * vecSe.z
        val crossZ = vecSp.x * vecSe.y - vecSp.y * vecSe.x

        // Use tolerance for floating point comparisons
        val crossMagSq = crossX * crossX + crossY * crossY + crossZ * crossZ
        if (crossMagSq > STRICT_TOLERANCE) { // Adjust tolerance as needed
            return false // Not collinear
        }

        // Handle case where line is just a point
        val segLenSq = vecSe.x * vecSe.x + vecSe.y * vecSe.y + vecSe.z * vecSe.z
        if (segLenSq < SO_STRICT_TOLERANCE) {
            // Segment is a point, contains only if point is the same point
            return vecSp.x * vecSp.x + vecSp.y * vecSp.y + vecSp.z * vecSp.z < SO_STRICT_TOLERANCE
        }

        // 2. Check if point is within the segment bounds using dot product
        // Dot product: (point - start) · (end - start)
        val dotProduct = vecSp.x * vecSe.x + vecSp.y * vecSe.y + vecSp.z * vecSe.z

        if (dotProduct < -TOLERANCE) { // Use tolerance
            return false // Point is "before" start relative to segment direction
        }

        // Squared length of the segment vector (already calculated as segLenSq)
        // double squaredLength = vec_se.getX()*vec_se.getX() + vec_se.getY()*vec_se.getY() + vec_se.getZ()*vec_se.getZ();

        // Check if dot product is beyond the segment length (use tolerance)
        return dotProduct <= segLenSq + TOLERANCE // point lies between start and end (inclusive)
    }

    /**
     * Translates the start and end points by the specified offsets in the x, y, and z directions.
     *
     * 
     * @param dx The offset value along the x-axis. Defaults to 0.0.
     * @param dy The offset value along the y-axis. Defaults to 0.0.
     * @param dz The offset value along the z-axis. Defaults to 0.0.
     * @since 1.0.0
     */
    fun translate(dx: Double = 0.0, dy: Double = 0.0, dz: Double = 0.0) {
        start.translate(dx, dy, dz)
        end.translate(dx, dy, dz)
    }

    /**
     * Scales the distance between the start and end points by the specified factor.
     *
     * Adjusts the position of the `end` point by multiplying the vector from `start` to `end`
     * with the given `factor`. The `start` point remains unchanged.
     *
     * 
     * @param factor The factor by which the distance to the `end` point should be scaled.
     * @since 1.0.0
     */
    fun scale(factor: Double) {
        val newX = start.x + factor * (end.x - start.x)
        val newY = start.y + factor * (end.y - start.y)
        end = Point(newX, newY)
    }

    /**
     * Rotates the line by a specified angle around its starting point.
     *
     * This operation assumes the line is in a 2D coordinate system. If the line
     * has non-zero z-coordinates for either the start or the end point,
     * the operation is not supported and an exception is thrown.
     *
     * 
     * @param angle The angle in radians by which the line should be rotated.
     * @throws GeometryException If either the start or end point has a non-zero z-coordinate.
     * @since 1.0.0
     */
    fun rotate(angle: Double) {
        if (start.z != 0.0 || end.z != 0.0) throw GeometryException("Only for 2D lines")

        val x = start.x + (end.x - start.x) * cos(angle) - (end.y - start.y) * sin(angle)
        val y = start.y + (end.x - start.x) * sin(angle) + (end.y - start.y) * cos(angle)
        end = Point(x, y)
    }

    /**
     * Calculates the angle in radians between the current line and the specified line.
     * The result is a value in the range [0, π]. Handles both 2D and 3D lines.
     * For zero-length lines, the method returns Double.NaN as the angle is undefined.
     *
     * 
     * @param other The line with which the angle is to be calculated.
     * @return The angle in radians between the two lines. Returns Double.NaN if either line has zero length.
     * @since 1.0.0
     */
    fun angleWith(other: Line): Double {
        if (start.z == 0.0 || end.z == 0.0 || other.start.z == 0.0 || other.end.z == 0.0) {
            val dx1 = end.x - start.x
            val dy1 = end.y - start.y
            val dx2 = other.end.x - other.start.x
            val dy2 = other.end.y - other.start.y

            val dotProduct = dx1 * dx2 + dy1 * dy2
            val magnitude1 = sqrt(dx1 * dx1 + dy1 * dy1)
            val magnitude2 = sqrt(dx2 * dx2 + dy2 * dy2)

            return acos(dotProduct / (magnitude1 * magnitude2))
        }

        val dir1: Point = directionVector
        val dir2: Point = other.directionVector

        val dx1 = dir1.x
        val dy1 = dir1.y
        val dz1 = dir1.z
        val dx2 = dir2.x
        val dy2 = dir2.y
        val dz2 = dir2.z

        val magnitude1 = sqrt(dx1 * dx1 + dy1 * dy1 + dz1 * dz1)
        val magnitude2 = sqrt(dx2 * dx2 + dy2 * dy2 + dz2 * dz2)

        // Check for zero-length lines
        if (magnitude1 < STRICT_TOLERANCE || magnitude2 < STRICT_TOLERANCE) {
            return Double.NaN // Angle undefined for zero-length lines
        }
        val dotProduct = dx1 * dx2 + dy1 * dy2 + dz1 * dz2

        // Clamp the value to [-1, 1] due to potential floating point errors
        val cosTheta = max(-1.0, min(1.0, dotProduct / (magnitude1 * magnitude2)))

        return acos(cosTheta)
    }

    /**
     * Compares the current line instance with another line instance based on their lengths.
     *
     * 
     * @param other the line instance to compare against.
     * @return a negative integer, zero, or a positive integer as this line's length
     * is less than, equal to, or greater than the specified line's length.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Line) = length.compareTo(other.length)

    /**
     * Compares this object to the specified object for equality. The comparison can optionally
     * consider only the length property.
     *
     * 
     * @param other the object to compare this instance with.
     * @param considerOnlyLength if true, only the length property will be compared; otherwise,
     *        all properties (start, end, and length) will be compared.
     * @return true if the objects are considered equal based on the comparison parameters, false otherwise.
     * @since 1.0.0
     */
    fun equals(other: Any?, considerOnlyLength: Boolean): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Line

        if (considerOnlyLength) return length == other.length
        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    /**
     * Compares this object with the specified object for equality.
     * This method overrides the default implementation to perform
     * the comparison with an additional context parameter.
     *
     * 
     * @param other The object to compare with this instance.
     * @since 1.0.0
     */
    override fun equals(other: Any?) = equals(other, false)

    /**
     * Computes the hash code for this line based on its properties.
     * The hash code is derived from the `start` and `end` points of the line,
     * ensuring consistency with the `equals` method.
     *
     * 
     * @return An integer representing the hash code of this line.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }

    /**
     * Returns a string representation of the current line object.
     *
     * The format of the returned string includes details of the start and end points.
     *
     * 
     * @return A string representation of the line object.
     * @since 1.0.0
     */
    override fun toString() = "Line(start=$start, end=$end)"

    /**
     * Converts the properties of the `Line` class into a map representation.
     *
     * The resulting map contains the following key-value pairs:
     *
     * This method is a utility function marked as private and is intended for internal use within the `Line` class.
     *
     * @return A map containing key-value pairs representing the properties of the line.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "start" to start,
        "end" to end,
        "length" to length,
        "midpoint" to midpoint,
        "directionVector" to directionVector,
        "slope" to slope,
        "yIntercept" to yIntercept,
        "xIntercept" to xIntercept,
        "intercept" to intercept
    )

    /**
     * Provides a mechanism to retrieve the value of a property dynamically from a backing map.
     * Uses the property's name to fetch the corresponding value from the map.
     *
     * - `start`: The starting point of the line - TYPE: [Point].
     * - `end`: The ending point of the line - TYPE: [Point].
     * - `length`: The length of the line - TYPE: [Double].
     * - `midpoint`: The midpoint of the line - TYPE: [Point].
     * - `directionVector`: The directional vector of the line - TYPE: [Point].
     * - `slope`: The slope of the line - TYPE: [Double].
     * - `yIntercept`: The y-axis intercept of the line if applicable - TYPE: [Double].
     * - `xIntercept`: The x-axis intercept of the line if applicable - TYPE: [Double].
     * - `intercept`: The general intercept of the line - TYPE: [Point].
     *
     * @param thisRef The reference to the object on which the property is being accessed. Can be null.
     * @param property The metadata for the property being accessed.
     * @return The value of the property cast to the expected type [R].
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}