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
 * Represents a square, which is a specialized form of a rectangle where both sides
 * have the same length. This class provides functionality to manipulate and serialize
 * square geometries.
 *
 * A square is defined by its top-left corner and the length of its side.
 * Serialization and deserialization are supported through the custom
 * `Serializer` and `Deserializer` companion classes.
 *
 * @param topLeft The top-left corner of the square. Defaults to a new `Point` instance.
 * @param sideLength The length of the square's sides. Defaults to 0.0.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Square.Companion.Serializer::class)
@JsonDeserialize(using = Square.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Square.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Square.Companion.OldDeserializer::class)
@Suppress("unused")
class Square (topLeft: Point = Point(), sideLength: Double = 0.0) : Rectangle(topLeft, sideLength, sideLength), Serializable, Comparable<Rectangle> {
    var sideLength: Double
        get() = super.width
        set(value) { super.width = value; super.height = value }

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

        class Serializer : ValueSerializer<Square>() {
            override fun serialize(value: Square, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                gen.writePOJOProperty("topLeft", value.topLeft)
                gen.writeNumberProperty("sideLength", value.width)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Square>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Square {
                val node = p.objectReadContext().readTree<JsonNode>(p)
                return Square(
                    node.get("topLeft").traverse(p.objectReadContext()).readValueAs(Point::class.java),
                    node.get("sideLenghth").traverse(p.objectReadContext()).readValueAs(Double::class.java)
                )
            }
        }

        class OldSerializer : JsonSerializer<Square>() {
            override fun serialize(value: Square, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeObjectField("topLeft", value.topLeft)
                gen.writeObjectField("sideLength", value.width)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Square>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Square {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Square(
                    node.get("topLeft").traverse(p.codec).readValueAs(Point::class.java),
                    node.get("sideLenghth").traverse(p.codec).readValueAs(Double::class.java)
                )
            }
        }
    }

    /**
     * Returns a string representation of the `Square` instance.
     * The string includes the `topLeft` point and the `sideLength` value.
     *
     * 
     * @return A string describing the square's properties.
     * @since 1.0.0
     */
    override fun toString() = "Square(topLeft=$topLeft, sideLength=$sideLength)"
}