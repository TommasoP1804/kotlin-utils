/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.web

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.web.HttpStatus.Companion.toHttpStatus
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.node.ObjectNode
import java.net.URI

/**
 * A data class representing a standardized problem detail object, typically used in HTTP APIs
 * to provide machine-readable error information. The model aligns with the "Problem Details for HTTP APIs"
 * specification.
 *
 * @property status The HTTP status code associated with the problem, represented as an instance of [HttpStatus].
 *                  This provides a standardized way to communicate the nature of the problem.
 * @property type A URI identifying the type of problem. It is intended to provide additional context about
 *                the problem type. Defaults to `about:blank` if omitted.
 * @property title A short, human-readable summary of the problem. This string should not change
 *                 from occurrence to occurrence for the same type of problem.
 * @property detail A detailed, human-readable explanation of the problem. This provides more specific
 *                  context about the error.
 * @property instance A URI reference that identifies the specific occurrence of the problem. It is typically
 *                    used to point to a resource containing detailed information about the error.
 * @property extensions A map of additional, implementation-defined contextual information for the problem.
 *                      It can be used to provide domain-specific or custom metadata.
 * @since 2.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
@JsonSerialize(using = ProblemDetail.Companion.Serializer::class)
@JsonDeserialize(using = ProblemDetail.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = ProblemDetail.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = ProblemDetail.Companion.OldDeserializer::class)
@MustUseReturnValues
data class ProblemDetail(
    val status: HttpStatus? = null,
    val type: Uri = DEFAULT_TYPE,
    val title: String? = status?.reasonPhrase,
    val detail: String? = null,
    val instance: Uri? = null,
    val extensions: DataMapNN? = null
) {
    /**
     * Secondary constructor for creating a `ProblemDetail` instance.
     *
     * This constructor allows initializing a `ProblemDetail` object by providing essential problem attributes
     * such as HTTP status, type, title, detail, instance, and custom extensions. The HTTP status code provided
     * is converted to an `HttpStatus` instance using the `toHttpStatus` extension function, ensuring compatibility
     * with the internal representation.
     *
     * @param status The HTTP status code representing the problem.
     * @param type The URI identifying the problem type. Defaults to `DEFAULT_TYPE` if not specified.
     * @param title A short, summary-like explanation of the problem.
     * @param detail A detailed description of the problem.
     * @param instance A URI identifying the specific instance of the problem.
     * @param extensions Additional non-standard, application-specific key-value pairs related to the problem.
     * @since 2.0.0
     */
    constructor(
        status: Int,
        type: URI = DEFAULT_TYPE,
        title: String? = null,
        detail: String? = null,
        instance: URI? = null,
        extensions: DataMapNN? = null
    ) : this(status.toHttpStatus(), type, title, detail, instance, extensions)

    companion object {
        /**
         * The default URI used to represent the `type` field in the `ProblemDetail` class
         * when no specific type is provided.
         *
         * This value is initialized to "about:blank", as defined in RFC 7807, which represents
         * a generic problem type with no further details. It serves as a fallback or baseline
         * type for problem descriptions in cases where a more specific URI is not applicable.
         * @since 2.0.0
         */
        private val DEFAULT_TYPE = URI("about:blank")

        class Serializer : ValueSerializer<ProblemDetail>() {
            override fun serialize(value: ProblemDetail, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                value.type.ifNotNull { gen.writeStringProperty("type", this.toString()) }
                value.title.ifNotNull { gen.writeStringProperty("title", this) }
                value.status.ifNotNull { gen.writeNumberProperty("status", this.value) }
                value.detail.ifNotNull { gen.writeStringProperty("detail", this) }
                value.instance.ifNotNull { gen.writeStringProperty("instance", this.toString()) }
                value.extensions.ifNotNull { gen.writePOJOProperty("extensions", this) }
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<ProblemDetail>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): ProblemDetail {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                return ProblemDetail(
                    type = node.get("type")?.stringValue()?.let { URI(it) } ?: DEFAULT_TYPE,
                    title = node.get("title")?.stringValue(),
                    status = node.get("status")?.intValue()?.toHttpStatus(),
                    detail = node.get("detail")?.stringValue(),
                    instance = node.get("instance")?.stringValue()?.let { URI(it) },
                    extensions = node.get("extensions")?.let { ctxt.readValue(it.traverse(p.objectReadContext()), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                )
            }
        }

        class OldSerializer : JsonSerializer<ProblemDetail>() {
            override fun serialize(value: ProblemDetail, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                value.type.ifNotNull { gen.writeStringField("type", this.toString()) }
                value.title.ifNotNull { gen.writeStringField("title", this) }
                value.status.ifNotNull { gen.writeNumberField("status", this.value) }
                value.detail.ifNotNull { gen.writeStringField("detail", this) }
                value.instance.ifNotNull { gen.writeStringField("instance", this.toString()) }
                value.extensions.ifNotNull { gen.writePOJOField("extensions", this) }
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<ProblemDetail>() {
            override fun deserialize(p: com.fasterxml.jackson.core.JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): ProblemDetail {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                return ProblemDetail(
                    type = node.get("type")?.textValue()?.let { URI(it) } ?: DEFAULT_TYPE,
                    title = node.get("title")?.textValue(),
                    status = node.get("status")?.intValue()?.toHttpStatus(),
                    detail = node.get("detail")?.textValue(),
                    instance = node.get("instance")?.textValue()?.let { URI(it) },
                    extensions = node.get("extensions")?.let { ctxt.readValue(it.traverse(p.codec), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                )
            }
        }
    }
}
