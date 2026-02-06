package dev.tommasop1804.kutils.classes.geometry

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.exceptions.GeometryException
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KProperty

/**
 * Represents a polygon, defined by a list of vertices. The polygon may be convex or concave,
 * with properties and methods to manipulate, evaluate, or modify its structure and behavior.
 *
 * @property vertices The list of points defining the vertices of the polygon.
 * @property verticesNumber The number of vertices in the polygon.
 * @property area The area enclosed by the polygon.
 * @property perimeter The total length of the polygon's edges.
 * @property isConvex A boolean value indicating whether the polygon is convex or not.
 * @property sideLengths A list of lengths of the polygon's edges.
 * @since 1.0.0
 */
@JsonSerialize(using = Polygon.Companion.Serializer::class)
@JsonDeserialize(using = Polygon.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Polygon.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Polygon.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_collection_declaration")
class Polygon (vertices: MList<Point> = emptyMList()): Serializable, Comparable<Polygon>, Shape2D {
    /**
     * Represents a mutable list of points that define the vertices of the polygon.
     *
     * This property contains the points that make up the shape of the polygon.
     * A minimum of three vertices is required for a valid polygon. If an attempt
     * is made to assign a list with fewer than three vertices, a [GeometryException]
     * will be thrown.
     *
     * Updating this property will replace the existing vertices with the new list of points.
     *
     * @since 1.0.0
     */
    var vertices: MList<Point> = vertices
        set(value) {
            value.size >= 3 || throw GeometryException("A polygon must have at least 3 vertices")
            field = value
        }

    init {
        vertices.size >= 3 || throw GeometryException("A polygon must have at least 3 vertices")
        this.vertices = vertices
    }

    /**
     * Secondary constructor for the [Polygon] class that initializes the polygon
     * with a variable number of [Point] objects as vertices.
     *
     * @param vertices Vararg parameter representing the initial set of vertices for the polygon.
     * They are converted into a mutable list and passed to the primary constructor.
     * 
     * @since 1.0.0
     */
    constructor(vararg vertices: Point): this(vertices.toMutableList())

    /**
     * The number of vertices in the polygon.
     *
     * Retrieves the total count of points that define the polygon.
     * This property dynamically calculates the size of the internal vertex list.
     *
     * 
     * @since 1.0.0
     */
    val verticesNumber: Int
        get() = vertices.size
    /**
     * Calculates the area of the polygon using the shoelace formula.
     * The area is derived based on the vertices of the polygon.
     * This property assumes that the vertices of the polygon are ordered
     * in a clockwise or counterclockwise manner.
     *
     * 
     * @return The absolute value of the calculated area divided by 2.
     * @since 1.0.0
     */
    override val area: Double
        get() {
            var area = 0.0
            val n: Int = verticesNumber

            for (i in 0..<n) {
                val current = vertices[i]
                val next = vertices[(i + 1) % n]
                area += (current.x * next.y) - (next.x * current.y)
            }
            return abs(area) / 2.0
        }
    /**
     * Represents the perimeter of the polygon, calculated as the sum of the lengths of its sides.
     *
     * The perimeter is computed by iterating through the vertices of the polygon and summing up
     * the Euclidean distances between consecutive vertices. The distance between the last
     * vertex and the first vertex is also included to close the polygon.
     *
     * 
     * @return The perimeter of the polygon as a Double.
     * @since 1.0.0
     */
    override val perimeter: Double
        get() {
            var perimeter = 0.0
            val n: Int = verticesNumber

            for (i in 0..<n) perimeter += vertices[i].distanceTo(vertices[(i + 1) % n])
            return perimeter
        }
    /**
     * Indicates whether the polygon represented by the [vertices] property is convex.
     * A polygon is considered convex if all internal angles are less than 180 degrees,
     * which is determined by analyzing the cross products of consecutive edges.
     *
     * For a polygon to be convex:
     * - It must have at least 3 vertices.
     * - All the cross products of consecutive edges must have the same sign, ensuring
     *   no change in the direction of the angles.
     *
     * If the polygon has fewer than 3 vertices, it cannot form a valid polygon,
     * and the property value will return `false`.
     *
     * 
     * @return `true` if the polygon is convex, `false` otherwise.
     * @since 1.0.0
     */
    val isConvex: Boolean
        get() {
            val n = vertices.size
            if (n < 3) return false
            var sign: Boolean? = null

            for (i in 0..<n) {
                val p1 = vertices[i]
                val p2 = vertices[(i + 1) % n]
                val p3 = vertices[(i + 2) % n]

                val crossProductZ = (p2.x - p1.x) * (p3.y - p2.y) - (p2.y - p1.y) * (p3.x - p2.x)

                if (abs(crossProductZ) > TOLERANCE) {
                    val currentSignPositive = crossProductZ > 0
                    if (sign.isNull()) sign = currentSignPositive
                    else if (sign != currentSignPositive) return false
                }
            }
            return sign.isNotNull()
        }
    /**
     * A computed property that returns a list of the lengths of the sides of the polygon.
     * Each side is represented as the Euclidean distance between two consecutive vertices.
     * The vertices are considered in a cyclic order, where the last vertex connects back to the first.
     *
     * 
     * @return A list of Double values, each representing the length of a side of the polygon.
     * @since 1.0.0
     */
    val sideLengths: DoubleList
        get() {
            val lengths = mListOf<Double>()
            val n = vertices.size
            for (i in 0..<n) {
                val p1 = vertices[i]
                val p2 = vertices[(i + 1) % n]
                lengths.add(p1.distanceTo(p2))
            }
            return lengths.toList()
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
         * A constant value used as a tolerance threshold for floating-point comparisons
         * to mitigate precision errors resulting from numerical operations.
         *
         * Typically employed in geometric computations where exact equality is
         * impractical due to the nature of floating-point arithmetic.
         *
         * @since 1.0.0
         */
        private const val TOLERANCE = 1e-9

        class Serializer : ValueSerializer<Polygon>() {
            override fun serialize(value: Polygon, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartArray()
                for (vertex in value.vertices) {
                    gen.writePOJO(vertex)
                }
                gen.writeEndArray()
            }
        }

        class Deserializer : ValueDeserializer<Polygon>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Polygon {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                val vertices: MutableList<Point> = ArrayList()
                for (vertexNode in node) {
                    vertices.add(ctxt.readValue(vertexNode.traverse(p.objectReadContext()), Point::class.java))
                }
                return Polygon(vertices)
            }
        }

        class OldSerializer : JsonSerializer<Polygon>() {
            override fun serialize(value: Polygon, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartArray()
                for (vertex in value.vertices) {
                    gen.writeObject(vertex)
                }
                gen.writeEndArray()
            }
        }

        class OldDeserializer : JsonDeserializer<Polygon>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Polygon {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                val vertices: MutableList<Point> = ArrayList()
                for (vertexNode in node) {
                    vertices.add(ctxt.readValue(vertexNode.traverse(p.codec), Point::class.java))
                }
                return Polygon(vertices)
            }
        }
    }

    /**
     * Creates a copy of the polygon. If no vertices are provided, it copies the existing vertices of the polygon.
     *
     * @param vertices A list of [Point] representing the vertices to define the new polygon. Defaults to the current polygon's vertices.
     * @return A new instance of [Polygon] containing the specified or existing vertices.
     * @since 1.0.0
     */
    fun copy(vertices: List<Point> = this.vertices) = Polygon(vertices.toMutableList())

    /**
     * Adds one or more vertices to the existing list of vertices in the polygon.
     *
     * 
     * @param vertices The vertices to be added to the polygon.
     * @since 1.0.0
     */
    fun addVertices(vararg vertices: Point) = this.vertices.addAll(vertices)

    /**
     * Adds a collection of vertices to the current list of vertices in the polygon.
     *
     * 
     * @param vertices A collection of points to be added as vertices to the polygon.
     * @since 1.0.0
     */
    fun addVertices(vertices: Collection<Point>) = this.vertices.addAll(vertices)

    /**
     * Adds the specified vertices to the polygon at the given index.
     *
     * 
     * @param index the position in the vertex list where the new vertices will be inserted.
     * @param vertices the vertices to be added to the polygon.
     * @since 1.0.0
     */
    fun addVertices(index: Int, vararg vertices: Point) = this.vertices.addAll(index, vertices.toMutableList())

    /**
     * Adds a collection of vertices to the polygon at the specified index.
     *
     * 
     * @param index The index at which the vertices should be inserted.
     * @param vertices A collection of [Point] objects representing the vertices to be added.
     * @since 1.0.0
     */
    fun addVertices(index: Int, vertices: Collection<Point>) = this.vertices.addAll(index, vertices)

    /**
     * Removes the specified vertices from the polygon.
     *
     * 
     * @param vertices the vertices to be removed from the polygon.
     * @since 1.0.0
     */
    fun removeVertices(vararg vertices: Point) = this.vertices.removeAll(vertices.toSet())

    /**
     * Removes a collection of specified vertices from the polygon.
     *
     * 
     * @param vertices The collection of vertices to be removed.
     * @since 1.0.0
     */
    fun removeVertices(vertices: Collection<Point>) = this.vertices.removeAll(vertices)

    /**
     * Removes a specified number of vertices from the polygon starting at the given index.
     * Ensures the polygon retains a minimum of three vertices after removal.
     *
     * 
     * @param index The starting index of the vertices to be removed.
     * @param count The number of consecutive vertices to remove starting from the given index. Defaults to 1.
     * @throws GeometryException If the resulting number of vertices is less than three.
     * @since 1.0.0
     */
    fun removeVertices(index: Int, count: Int = 1) {
        if (count == 0) return

        val temp = vertices
        temp.subList(index, index + count).clear()
        temp.size >= 3 || throw GeometryException("A polygon must have at least 3 vertices")
        vertices = temp.toMutableList()
    }

    /**
     * Removes specified vertices from the polygon based on their indices.
     * Ensures the polygon maintains a minimum of 3 vertices after removal.
     *
     * 
     * @param indices The indices of the vertices to remove.
     * @throws GeometryException if the resulting polygon has fewer than 3 vertices.
     * @since 1.0.0
     */
    fun removeVertices(vararg indices: Int) {
        val temp = vertices
        indices.forEach { temp.removeAt(it) }
        temp.size >= 3 || throw GeometryException("A polygon must have at least 3 vertices")
        vertices = temp.toMutableList()
    }

    /**
     * Adds a new vertex to the current polygon, creating a new polygon object that includes
     * the original vertices and the additional vertex.
     *
     * 
     * @param vertex The [Point] object representing the vertex to be added to the polygon.
     * @since 1.0.0
     */
    operator fun plus(vertex: Point) = Polygon((vertices + vertex).toMutableList())

    /**
     * Combines the vertices of this polygon with the vertices of another polygon,
     * creating a new polygon that contains all the vertices from both.
     *
     * 
     * @param other The Polygon whose vertices will be combined with this polygon.
     * @return A new Polygon instance containing the combined vertices of both polygons.
     * @since 1.0.0
     */
    operator fun plus(other: Polygon) = Polygon((vertices + other.vertices).toMutableList())

    /**
     * Adds multiple vertices to the polygon to create a new polygon instance
     * with the combined vertices from the current polygon and the provided vertices.
     *
     * 
     * @param vertices The iterable collection of [Point] objects to be added as vertices.
     * @since 1.0.0
     */
    operator fun plus(vertices: Iterable<Point>) = Polygon((this.vertices + vertices).toMutableList())

    /**
     * Removes a specified vertex from the polygon's list of vertices and creates a new polygon.
     *
     * 
     * @param vertex The vertex to be removed from the polygon.
     * @since 1.0.0
     */
    operator fun minus(vertex: Point) = Polygon((vertices - vertex).toMutableList())

    /**
     * Subtracts the vertices of another polygon from the current polygon, resulting in a new polygon
     * with a modified set of vertices.
     *
     * 
     * @param other The Polygon whose vertices will be removed from the current Polygon.
     * @return A new Polygon instance resulting from the subtraction.
     * @since 1.0.0
     */
    operator fun minus(other: Polygon) = Polygon((vertices - other.vertices.toSet()).toMutableList())

    /**
     * Subtracts a collection of vertices from the current polygon, removing the specified vertices.
     *
     * 
     * @param vertices The collection of vertices to be subtracted from the polygon.
     * @return A new Polygon instance after removing the specified vertices.
     * @since 1.0.0
     */
    operator fun minus(vertices: Iterable<Point>) = Polygon((this.vertices - vertices.toSet()).toMutableList())

    /**
     * Checks if the given point is located inside the polygon.
     *
     * The method determines whether the specified point lies within the boundaries
     * of the polygon defined by its vertices. The polygon is assumed to be a simple
     * polygon, which may be convex or concave, but must not self-intersect.
     *
     * 
     * @param point The point to verify if it is inside the polygon.
     * @return `true` if the point is inside the polygon; `false` otherwise.
     * @since 1.0.0
     */
    override operator fun contains(point: Point): Boolean {
        val n = vertices.size
        if (n < 3) return false

        var isInside = false
        val px: Double = point.x
        val py: Double = point.y

        var i = 0
        var j = n - 1
        while (i < n) {
            val p1 = vertices[i]
            val p2 = vertices[j]
            val p1x = p1.x
            val p1y = p1.y
            val p2x = p2.x
            val p2y = p2.y

            if (((py in p1y..<p2y) || (py in p2y..<p1y))) {
                if (px < (p2x - p1x) * (py - p1y) / (p2y - p1y) + p1x)
                    isInside = !isInside
            }
            j = i++
        }

        return isInside
    }

    /**
     * Computes the smallest axis-aligned rectangle that completely contains all the vertices of the polygon.
     *
     * 
     * @return A Rectangle object representing the bounding box of the polygon. If the polygon has no vertices,
     * returns a rectangle with all coordinates set to NaN.
     * @since 1.0.0
     */
    fun boundingBox(): Rectangle? {
        if (vertices.isEmpty()) return null

        var minX: Double = vertices.first().x
        var minY: Double = vertices.first().y
        var maxX = minX
        var maxY = minY
        for (i in 1..<vertices.size) {
            val p = vertices[i]
            minX = min(minX, p.x)
            minY = min(minY, p.y)
            maxX = max(maxX, p.x)
            maxY = max(maxY, p.y)
        }
        return Rectangle(Point(minX, minY), maxX, maxY)
    }


    /**
     * Compares this Polygon with the specified object for equality. Two Polygons are considered
     * equal if they are of the same class and have the same vertices.
     *
     * 
     * @param other The object to be compared with this instance for equality.
     * @return `true` if the specified object is equal to this Polygon, `false` otherwise.
     * @since 1.0.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Polygon

        return vertices == other.vertices
    }

    /**
     * Computes the hash code for the Polygon based on its vertices.
     *
     * 
     * @return The hash code of the Polygon, determined by its vertices.
     * @since 1.0.0
     */
    override fun hashCode() = vertices.hashCode()

    /**
     * Returns a string representation of the Polygon object, including its vertices.
     *
     * 
     * @return A string describing the Polygon's vertices.
     * @since 1.0.0
     */
    override fun toString() = "Polygon[$vertices]"

    /**
     * Compares this Polygon with another Polygon based on their area.
     *
     * 
     * @param other The Polygon with which the comparison is being made.
     * @return A negative integer, zero, or a positive integer as this Polygon's area
     *         is less than, equal to, or greater than the specified Polygon's area.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Polygon) = area.compareTo(other.area)

    /**
     * Converts the properties of the `Polygon` into a map representation.
     * The returned map includes details about the vertices, area, perimeter,
     * and the side lengths of the polygon.
     *
     * The mapping ties specific keys to corresponding values:
     *
     * @return A map containing the polygon's properties and their respective values.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "vertices" to vertices,
        "verticesNumber" to verticesNumber,
        "area" to area,
        "perimeter" to perimeter,
        "sideLengths" to sideLengths
    )

    /**
     * Retrieves the value of a property from a map representation based on its name.
     *
     * - `vertices`: The vertices of the polygon - TYPE: `MutableList<Double>`.
     * - `verticesNumber`: The number of vertices - TYPE: [Int].
     * - `area`: The area of the polygon - TYPE: [Double].
     * - `perimeter`: The perimeter of the polygon - TYPE: [Double].
     * - `sideLengths`: The lengths of each side of the polygon - TYPE: `List<Double>`.
     *
     * @param thisRef The object on which the property is accessed. Can be `null`.
     * @param property The property whose value is being retrieved.
     * @return The value of the property cast to the specified type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}