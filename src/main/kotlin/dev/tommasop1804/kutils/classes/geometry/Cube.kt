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

/**
 * Represents a Cube in 3D space. A Cube is a special type of Cuboid where
 * all three dimensions (width, height, and depth) are equal.
 *
 * @param corner the corner point from which the Cube is defined
 * @param sideLength the length of each side of the Cube
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Cube.Companion.Serializer::class)
@JsonDeserialize(using = Cube.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Cube.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Cube.Companion.OldDeserializer::class)
@Suppress("unused")
class Cube (corner: Point = Point(), sideLength: Double = 0.0) : Cuboid(corner, sideLength, sideLength, sideLength), Serializable, Shape3D {
    /**
     * Represents the side length of a cube. Changing the side length automatically updates
     * the width, height, and depth of the cube to ensure it remains a cube, where all three dimensions are equal.
     * The property ensures that the cube's geometry is consistent when the side length is modified.
     *
     * 
     * @since 1.0.0
     */
    var sideLength: Double
        get() = super.width
        set(value) { super.width = value; super.height = value; super.depth = value }

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

        class Serializer : ValueSerializer<Cube>() {
            override fun serialize(value: Cube, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                gen.writePOJOProperty("corner", value.minCorner)
                gen.writePOJOProperty("sideLength", value.sideLength)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Cube>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Cube {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return Cube(
                    node.get("corner").traverse(p.objectReadContext()).readValueAs(Point::class.java),
                    node.get("sideLength").traverse(p.objectReadContext()).readValueAs(Double::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<Cube>() {
            override fun serialize(value: Cube, gen: JsonGenerator, serializers: SerializerProvider?) {
                gen.writeStartObject()
                gen.writeObjectField("corner", value.minCorner)
                gen.writeObjectField("sideLength", value.sideLength)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Cube>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext?): Cube {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Cube(
                    node.get("corner").traverse(p.codec).readValueAs(Point::class.java),
                    node.get("sideLength").traverse(p.codec).readValueAs(Double::class.java)
                )
            }
        }
    }

    /**
     * Returns a string representation of the Cube object. The string includes
     * the values of the cube's corner point and its side length.
     *
     * 
     * @return a string representation of the Cube object
     * @since 1.0.0
     */
    override fun toString() = "Cube(corner=${minCorner}, sideLength=$sideLength)"
}