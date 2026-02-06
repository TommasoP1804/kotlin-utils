package dev.tommasop1804.kutils.classes.geometry

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.StringList
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.isNull
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import kotlin.math.acos
import kotlin.math.sqrt
import kotlin.reflect.KProperty

/**
 * Represents a point in a three-dimensional space with x, y, and z coordinates.
 *
 * @property x The x-coordinate of the point.
 * @property y The y-coordinate of the point.
 * @property z The z-coordinate of the point.
 * @property isOrigin A boolean indicating whether the point is at the origin (0, 0, 0).
 * @property isOnXYPlane A boolean indicating whether the point lies in the XY plane (z == 0).
 * @property isOnXZPlane A boolean indicating whether the point lies in the XZ plane (y == 0).
 * @property isOnYZPlane A boolean indicating whether the point lies in the YZ plane (x == 0).
 * @property isOnPlane A boolean indicating whether the point lies on any of the coordinate planes.
 * @property isOnVertex A boolean indicating whether the point lies directly at a coordinate vertex (origin or axis intersections).
 * @property isOnXAxis A boolean indicating whether the point lies on the X-axis (y == 0, z == 0).
 * @property isOnYAxis A boolean indicating whether the point lies on the Y-axis (x == 0, z == 0).
 * @property isOnZAxis A boolean indicating whether the point lies on the Z-axis (x == 0, y == 0).
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Point.Companion.Serializer::class)
@JsonDeserialize(using = Point.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Point.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Point.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_getorthrow_as_invoke", "kutils_substring_as_get_intprogression")
class Point private constructor(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0) : Serializable, Comparable<Point> {
    /**
     * Secondary constructor to initialize a point using a `java.awt.Point` object.
     * Converts the `x` and `y` properties of the `java.awt.Point` to `Double`.
     *
     * @param point The `java.awt.Point` instance to extract `x` and `y` coordinates from.
     * @since 1.0.0
     */
    constructor(point: java.awt.Point) : this(point.x.toDouble(), point.y.toDouble())

    /**
     * Constructs a new instance by initializing the x, y, and z coordinates with the specified values.
     * Converts the provided numbers to double before assignment.
     *
     * @param x the x-coordinate, default is 0.0
     * @param y the y-coordinate, default is 0.0
     * @param z the z-coordinate, default is 0.0
     * @since 1.0.0
     */
    constructor(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0) : this(x.toDouble(), y.toDouble(), z.toDouble())

    /**
     * Indicates whether the current point is located at the origin of the coordinate system.
     * The origin is defined as the point where all coordinates (x, y, z) are equal to zero.
     *
     * 
     * @return `true` if the point is on the origin, `false` otherwise.
     * @since 1.0.0
     */
    val isOrigin: Boolean
        get() = x == 0.0 && y == 0.0 && z == 0.0
    /**
     * Indicates whether the point lies on the XY plane.
     *
     * The value is `true` if the z-coordinate of the point is `0.0`, indicating it is on the XY plane.
     * Otherwise, the value is `false`.
     *
     * 
     * @return `true` if the point is on the XY plane, `false` otherwise.
     * @since 1.0.0
     */
    val isOnXYPlane: Boolean
        get() = z == 0.0
    /**
     * Indicates whether the point lies on the XZ plane.
     * A point is considered to be on the XZ plane if its y-coordinate is equal to 0.0.
     *
     * 
     * @return `true` if the point lies on the XZ plane; `false` otherwise.
     * @since 1.0.0
     */
    val isOnXZPlane: Boolean
        get() = y == 0.0
    /**
     * Indicates whether the point lies on the YZ plane in a 3D coordinate system.
     *
     * The YZ plane is defined as the plane where the x-coordinate is equal to 0.
     * This property evaluates to `true` if the x-coordinate of the point is 0.0, and `false` otherwise.
     *
     * 
     * @return `true` if the point is on the YZ plane, `false` otherwise.
     * @since 1.0.0
     */
    val isOnYZPlane: Boolean
        get() = x == 0.0
    /**
     * Indicates whether the object is positioned on the plane defined by specific conditions.
     *
     * This property evaluates to `true` if the x-coordinate equals 0.0 and the y-coordinate equals 0.0,
     * meaning the object is located at the origin point in the plane.
     *
     * 
     * @return `true` if the object is on the plane; otherwise, `false`.
     * @since 1.0.0
     */
    val isOnPlane: Boolean
        get() = x == 0.0 && y == 0.0
    /**
     * A read-only property that determines if the current state represents being
     * located on a vertex. The condition is true when the x, y, and z coordinates
     * are equal.
     *
     * 
     *           an entity with x, y, and z properties.
     * @return `true` if the coordinates x, y, and z are identical; `false` otherwise.
     * @since 1.0.0
     */
    val isOnVertex: Boolean
        get() = x == y && y == z
    /**
     * Indicates whether the point lies on the X-axis in a 3-dimensional Cartesian coordinate system.
     * A point is considered to be on the X-axis if both its Y and Z coordinates are equal to zero.
     *
     * 
     * @return `true` if the point is on the X-axis, otherwise `false`.
     * @since 1.0.0
     */
    val isOnXAxis: Boolean
        get() = y == 0.0 && z == 0.0
    /**
     * Determines if a point lies on the Y-axis in a 3D coordinate system.
     * The condition for being on the Y-axis is that both the X and Z coordinates
     * of the point are equal to zero.
     *
     * 
     * @return `true` if the X and Z coordinates are zero, indicating the point is on the Y-axis,
     *         otherwise `false`.
     * @since 1.0.0
     */
    val isOnYAxis: Boolean
        get() = x == 0.0 && z == 0.0
    /**
     * Indicates whether the current point lies on the Z-axis.
     *
     * This property evaluates to `true` if both the x and y coordinates are equal to 0.0,
     * meaning the point is aligned along the Z-axis. Otherwise, it evaluates to `false`.
     *
     * 
     * @return `true` if the X and Y coordinates are zero, indicating the point is on the Z-axis,
     *         otherwise `false`.
     * @since 1.0.0
     */
    val isOnZAxis: Boolean
        get() = x == 0.0 && y == 0.0

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
         * Parses a string representation of a point and returns a [Point] object.
         * The input string can represent a 2D or 3D point, optionally enclosed with parentheses or brackets.
         * The coordinates are expected to be separated by either a comma or a semicolon.
         *
         * @param string The input string to parse, representing a point in 2D or 3D space.
         * @return A [Point] object created from the parsed string, containing the coordinates, wrapped in a [Result].
         * @throws MalformedInputException If the input string format is invalid or cannot be parsed.
         * @since 1.0.0
         */
        fun parse(string: String) = runCatching {
            val s = string.trim()
            val values: StringList = if (s.startsWith("(") || s.startsWith("[")) s.substring(1, s.length - 1)
                .split((if ("," in s) "," else (if (";" in s) ";" else "{2}")).toRegex())
                .dropLastWhile { it.isEmpty() }
            else s.split((if ("," in s) "," else (if (";" in s) ";" else "{2}")).toRegex())
                .dropLastWhile { it.isEmpty() }

            values.size in 2..3 || throw MalformedInputException("Invalid point format: $s")

            Point(
                values[0].trim().toDouble(),
                values[1].trim().toDouble(),
                if (values.size == 3) values[2].trim().toDouble() else 0.0
            )
        }

        class Serializer : ValueSerializer<Point>() {
            override fun serialize(value: Point, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toSimpleString())
            }
        }

        class Deserializer : ValueDeserializer<Point>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Point = parse(p.string).getOrThrow()
        }

        class OldSerializer : JsonSerializer<Point>() {
            override fun serialize(value: Point, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.toSimpleString())
            }
        }

        class OldDeserializer : JsonDeserializer<Point>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext?) = parse(p.text).getOrThrow()
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Point?, String?> {
            override fun convertToDatabaseColumn(attribute: Point?) = attribute?.toSimpleString()
            override fun convertToEntityAttribute(dbData: String?) = if (dbData.isNull()) null else parse(dbData).getOrThrow()
        }
    }

    /**
     * Creates a copy of the current point with the option to override individual coordinates.
     *
     * @param x The x-coordinate for the new point. Defaults to the x-coordinate of the current point.
     * @param y The y-coordinate for the new point. Defaults to the y-coordinate of the current point.
     * @param z The z-coordinate for the new point. Defaults to the z-coordinate of the current point.
     * @return A new Point object with the specified coordinates.
     * @since 1.0.0
     */
    fun copy(x: Double = this.x, y: Double = this.y, z: Double = this.z) = Point(x, y, z)

    /**
     * Compares this object with another for equality.
     * Two `Point` objects are considered equal if they
     * have the same `x`, `y`, and `z` coordinate values.
     *
     * 
     * @param other An instance of `Any?` to compare with this `Point` object.
     * @return `true` if the specified object is equal to this `Point`, `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    /**
     * Computes the hash code for this instance based on its properties.
     * The hash code is calculated by combining the hash codes of the `x`, `y`,
     * and `z` coordinates, ensuring a unique value for different positions.
     *
     * 
     * @return An integer representing the hash code of this instance.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    /**
     * Converts the `Point` instance to its string representation.
     *
     * The string representation includes the x, y, and z coordinates of the point
     * in the format `Point(x=valueX, y=valueY, z=valueZ)`.
     *
     * z is omitted if equals to 0.
     *
     * 
     * @return A string that represents the coordinates of the `Point`.
     * @since 1.0.0
     */
    override fun toString() = "Point(x=$x, y=$y" + (if (z != 0.0) ", z=$z" else "") + ")"

    /**
     * Converts the current `Point` instance to a simplified string representation.
     * The resulting string will be in the format "(x, y)" if the `z` coordinate is `0.0`,
     * or "(x, y, z)" otherwise.
     *
     * 
     * @return A string representation of the point in the format "(x, y)" or "(x, y, z)".
     * @since 1.0.0
     */
    fun toSimpleString() = "($x, $y" + if (z != 0.0) ", $z)" else ")"

    /**
     * Compares this `Point` instance with another `Point`. The comparison is done
     * lexicographically, first comparing the `x` coordinates, then the `y`, and finally
     * the `z` coordinates if needed.
     *
     * 
     * @param other The other `Point` to compare with this instance.
     * @return A negative integer if this point is less than the other point,
     *         zero if they are equal,
     *         and a positive integer if this point is greater than the other point.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Point): Int {
        val xComparison = x.compareTo(other.x)
        if (xComparison != 0) return xComparison

        val yComparison = y.compareTo(other.y)
        if (yComparison != 0) return yComparison

        return z.compareTo(other.z)
    }

    /**
     * Translates the point by the specified amounts along each axis.
     *
     * Modifies the current point by adding the given values to its x, y, and z coordinates.
     *
     * 
     * @param dx The translation along the x-axis. Defaults to 0.0 if not specified.
     * @param dy The translation along the y-axis. Defaults to 0.0 if not specified.
     * @param dz The translation along the z-axis. Defaults to 0.0 if not specified.
     * @since 1.0.0
     */
    fun translate(dx: Double = 0.0, dy: Double = 0.0, dz: Double = 0.0) {
        x += dx
        y += dy
        z += dz
    }

    /**
     * Translates the current point by the specified `point`.
     * The coordinates of the input point (`x`, `y`, `z`) are added to the current point's coordinates.
     *
     * 
     * @param point The point whose coordinates are used to translate the current point.
     * @since 1.0.0
     */
    fun translate(point: Point) = translate(point.x, point.y, point.z)

    /**
     * Calculates the Euclidean distance between this point and another point in a 3D space.
     *
     * 
     * @param other The point to which the distance is calculated.
     * @return The Euclidean distance between the current point and the specified point as a Double.
     * @since 1.0.0
     */
    fun distanceTo(other: Point): Double {
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z
        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    /**
     * Computes the magnitude (or length) of the point vector originating from the origin (0, 0, 0).
     * The magnitude is calculated as the square root of the sum of the squares of the x, y, and z coordinates.
     *
     * 
     * @return The magnitude of the vector.
     * @since 1.0.0
     */
    fun magnitude() = sqrt(x * x + y * y + z * z)

    /**
     * Normalizes the point by dividing it by its magnitude.
     * This method assumes the receiver has a non-zero magnitude.
     *
     * 
     * @return The normalized point with a magnitude of 1.
     * @since 1.0.0
     */
    fun normalize() = this / magnitude()

    /**
     * Adds the coordinates of the given point to the current point, returning a new point
     * with the resulting coordinates.
     *
     * 
     * @param other The point to be added to the current point.
     * @return A new point with coordinates equal to the sum of the respective
     *         coordinates of the current point and the given point.
     * @since 1.0.0
     */
    operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)

    /**
     * Subtracts the coordinates of the given point from the coordinates of the current point
     * and returns a new Point instance representing the result.
     *
     * 
     * @param other The point whose coordinates will be subtracted from the current point.
     * @return A new Point instance representing the result of the subtraction.
     * @since 1.0.0
     */
    operator fun minus(other: Point) = Point(x - other.x, y - other.y, z - other.z)

    /**
     * Multiplies the coordinates of this point by a scalar value and returns the resulting point.
     *
     * 
     * @param scalar The scalar value by which the point's coordinates will be multiplied.
     * @return A new Point instance with scaled coordinates.
     * @since 1.0.0
     */
    operator fun times(scalar: Double) = Point(x * scalar, y * scalar, z * scalar)

    /**
     * Divides each coordinate of the point by the given scalar value.
     *
     * 
     * @param scalar The scalar value by which to divide each coordinate of the point.
     * @return A new point with coordinates resulting from the division.
     * @since 1.0.0
     */
    operator fun div(scalar: Double) = Point(x / scalar, y / scalar, z / scalar)

    /**
     * Represents the unary minus operator for the `Point` class.
     * This operator negates the coordinates of the current point,
     * resulting in a new point with each coordinate inverted.
     *
     * 
     * @return A new `Point` instance with negated coordinates.
     * @since 1.0.0
     */
    operator fun unaryMinus() = Point(-x, -y, -z)

    /**
     * Creates a new `Point` instance by incrementing each coordinate (`x`, `y`, and `z`) of the current point by 1.
     *
     * 
     * @return A new `Point` with coordinates incremented by 1.
     * @since 1.0.0
     */
    operator fun inc() = Point(x + 1, y + 1, z + 1)

    /**
     * Decrements the coordinates of the point by 1 in each dimension (x, y, and z).
     * Provides a new instance of the `Point` class with updated coordinates.
     *
     * 
     * @return A new `Point` instance with the x, y, and z coordinates decremented by 1.
     * @since 1.0.0
     */
    operator fun dec() = Point(x - 1, y - 1, z - 1)

    /**
     * Calculates the midpoint between the current point and the specified point.
     *
     * 
     * @param other The other `Point` instance used to compute the midpoint.
     * @return A new `Point` instance representing the midpoint between the two points.
     * @since 1.0.0
     */
    fun midpoint(other: Point) = Point((x + other.x) / 2, (y + other.y) / 2, (z + other.z) / 2)

    /**
     * Computes the dot product of the current point with another point.
     * The dot product is calculated as the sum of the pairwise products
     * of the corresponding coordinates of the two points.
     *
     * 
     * @param other The other point to compute the dot product with.
     * @return The scalar value resulting from the dot product calculation.
     * @since 1.0.0
     */
    fun dotProduct(other: Point) = x * other.x + y * other.y + z * other.z

    /**
     * Calculates the cross product of the current point with another point.
     *
     * 
     * @param other The other point to calculate the cross product with.
     * @return The result of the cross product as a scalar value.
     * @since 1.0.0
     */
    fun crossProduct(other: Point) = x * other.y - y * other.x + z * other.z

    /**
     * Computes a new point by linearly interpolating between the current point and another point.
     * The interpolation is determined by the given factor.
     *
     * 
     * @param other The target point towards which the interpolation is performed.
     * @param factor The interpolation factor, where 0.0 corresponds to the current point
     * and 1.0 corresponds to the target point. Values between 0.0 and 1.0 result in a point
     * between the two, while values outside this range extrapolate.
     * @since 1.0.0
     */
    fun interpolate(other: Point, factor: Double) = Point(x + (other.x - x) * factor, y + (other.y - y) * factor, z + (other.z - z) * factor)

    /**
     * Calculates the angle in radians between the current point and another point
     * in a three-dimensional space. The angle is determined using the dot product
     * and the magnitudes of the points.
     *
     * 
     * @param other The other point with which the angle is calculated.
     * @return The angle in radians between the two points.
     * @since 1.0.0
     */
    fun angleBetween(other: Point) = acos(dotProduct(other) / (magnitude() * other.magnitude()))

    /**
     * Converts this point to a `java.awt.Point` object.
     * The `x` and `y` coordinates of this point are cast to `Int` and used to create the resulting `java.awt.Point`.
     *
     * 
     * @return A `java.awt.Point` instance with the `x` and `y` values derived from this point, cast to integers.
     * @since 1.0.0
     */
    fun toJavaAwtPoint() = java.awt.Point(x.toInt(), y.toInt())

    /**
     * Converts the current `Point` instance into a `Pair` containing the `x` and `y` coordinates.
     *
     * 
     * @return A `Pair` where the first element corresponds to the `x` coordinate and the second element corresponds to the `y` coordinate.
     * @since 1.0.0
     */
    fun toPair() = Pair(x, y)

    /**
     * Converts the `Point` instance into a `Triple` containing its x, y, and z coordinates.
     *
     * 
     * @return A `Triple` where the first, second, and third values correspond to the x, y, and z coordinates respectively.
     * @since 1.0.0
     */
    fun toTriple() = Triple(x, y, z)

    /**
     * Converts the `Point` instance into an array of type `DoubleArray` containing its `x`, `y`, and `z` coordinates.
     *
     * 
     * @return A `DoubleArray` where the first element is the `x` coordinate, the second element is the `y` coordinate,
     *         and the third element is the `z` coordinate of the `Point`.
     * @since 1.0.0
     */
    fun toTypedArray() = doubleArrayOf(x, y, z)

    /**
     * Converts the components of the current point into a list representation.
     *
     * 
     * @return A list containing the x, y, and z coordinates of the point in order.
     * @since 1.0.0
     */
    fun toList() = listOf(x, y, z)

    /**
     * Converts the properties `x`, `y`, and `z` into a map where the keys
     * are the property names and the values are the corresponding property values.
     *
     * @return A map containing the properties `x`, `y`, and `z`, with their names as keys.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "x" to x,
        "y" to y,
        "z" to z
    )

    /**
     * Provides a delegated property for retrieving a value from a mapped structure.
     *
     * - `x`
     * - `y`
     * - `z`
     *
     * No type specification required: [Double]
     *
     * @param thisRef The object in which the property is referenced. Can be null.
     * @param property The metadata of the property being delegated.
     * @return The value associated with the name of the provided property in the mapped structure.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name)
}