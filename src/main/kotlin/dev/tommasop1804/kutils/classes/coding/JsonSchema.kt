/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.coding

import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import com.networknt.schema.ValidationMessage
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.exceptions.*
import org.intellij.lang.annotations.Language
import tools.jackson.databind.JsonNode
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize

/**
 * Represents a JSON Schema parsed and evaluated from a JSON structure.
 *
 * This class provides access to various properties and attributes defined by the JSON Schema
 * specification. It supports multiple schema versions, including Draft 7, Draft 6, Draft 4,
 * and later versions.
 *
 * @constructor Creates a `JsonSchema` instance from a JSON object or string.
 * @property json The JSON object representing the schema.
 * @since 3.8.1
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Json.Companion.Serializer::class)
@JsonDeserialize(using = Json.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Json.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Json.Companion.OldDeserializer::class)
@Suppress("unused")
class JsonSchema(val json: Json) : Json(json) {
    /**
     * Provides a lazily evaluated JsonNode representation of the associated `json` field.
     * This is achieved by calling the `toJsonNode` method on the `json` instance.
     * @since 3.8.1
     */
    private val node: JsonNode get() = json.toJsonNode()
    
    /**
     * Retrieves the value of the `$schema` keyword in the JSON Schema, if present.
     *
     * The `$schema` keyword is used to declare the version of the JSON Schema
     * used for validation, allowing tools to process the document accordingly.
     * This property is nullable and will return `null` if the `$schema` keyword
     * is not defined in the schema.
     * @since 3.8.1
     */
    val schema: String? get() = node.get($$"$schema")?.asString()
    /**
     * Retrieves the optional identifier (`$id`) of the JSON schema,
     * as defined in the JSON Schema specification.
     *
     * This identifier is used to uniquely reference the schema within
     * a larger context or for resolving relative URIs. If the `$id`
     * property is not present in the schema, the value returned will
     * be `null`.
     * @since 3.8.1
     */
    val id: String? get() = node.get($$"$id")?.asString()
    /**
     * Retrieves the value of the `$ref` property from the underlying JSON node, if it exists.
     * This property is used to reference definitions or schemas elsewhere within the document
     * or external resources.
     *
     * The value is returned as a nullable string. If the `$ref` property is not present
     * in the current JSON node, this property will return `null`.
     * @since 3.8.1
     */
    val ref: String? get() = node.get($$"$ref")?.asString()
    /**
     * Provides access to the `$defs` or `definitions` field within a JSON structure,
     * retrieving their content as a map where keys are strings and values are instances of [JsonSchema].
     *
     * The function prioritizes the `$defs` field, attempting to parse its contents as JSON and converting it
     * into a strongly-typed map. If the `$defs` field is not present or fails to parse, the `definitions`
     * field is used as a fallback.
     *
     * The retrieval and conversion operations are encapsulated in a series of steps, each guarded by
     * error handling to ensure robustness. If none of the fields are present or successfully parsed,
     * `null` is returned.
     *
     * @return A nullable [Map] of string keys to [JsonSchema] values, or `null` if neither `$defs` nor
     * `definitions` exist or can be parsed.
     * @since 3.8.1
     */
    val defs: Map<String, JsonSchema>? get() = node.get($$"$defs")?.asJson()?.getOrThrow()?.toMap<JsonSchema>()?.getOrThrow()
        ?: node.get("definitions")?.asJson()?.getOrThrow()?.toMap<JsonSchema>()?.getOrThrow()
    /**
     * The `comment` property retrieves the value of the "$comment" key from the JSON node as a nullable string.
     * "$comment" is a standard keyword in JSON Schema used to add comments or annotations within the schema.
     * This property can be used to access descriptive or explanatory information intended for human readers.
     * @since 3.8.1
     */
    val comment: String? get() = node.get($$"$comment")?.asString()
    
    /**
     * Represents the title of a node, if available.
     * The value is retrieved from the "title" property of the node.
     * If the "title" property does not exist or is not a valid string, this will be null.
     * @since 3.8.1
     */
    val title: String? get() = node.get("title")?.asString()
    /**
     * Retrieves the "description" field from the underlying JSON node, if present.
     * Returns the value of the "description" field as a string or `null` if the field does not exist.
     * @since 3.8.1
     */
    val description: String? get() = node.get("description")?.asString()
    /**
     * Gets the value associated with the "default" key from the underlying JSON node.
     *
     * @return The JSON node corresponding to the "default" key, or null if the key is not present.
     * @since 3.8.1
     */
    val default: JsonNode? get() = node.get("default")
    /**
     * Represents a list of example values defined in the JSON Schema.
     * This is retrieved from the "examples" property of the underlying JSON node,
     * if it exists. The list is nullable, indicating that the "examples" property
     * may not be present in the schema.
     * @since 3.8.1
     */
    val examples: List<JsonNode>? get() = node.get("examples")?.toList()
    /**
     * Indicates whether the schema defines this property as read-only.
     * A read-only property is intended to be used in responses only and
     * should not be included in requests.
     *
     * The value is extracted from the JSON node associated with the schema,
     * if available, and represents it as a boolean.
     * A `null` value indicates that the "readOnly" property is not defined in the schema.
     * @since 3.8.1
     */
    val readOnly: Boolean? get() = node.get("readOnly")?.asBoolean()
    /**
     * A flag indicating whether the schema property or element is write-only.
     *
     * If this value is `true`, the associated property is intended to be written to,
     * but not read from. Typically, this can be used to signal that the property
     * contains sensitive information like passwords or secrets.
     *
     * A `null` value signifies that the write-only characteristic is unspecified.
     * @since 3.8.1
     */
    val writeOnly: Boolean? get() = node.get("writeOnly")?.asBoolean()
    /**
     * Indicates whether the schema or its property is marked as deprecated.
     *
     * This flag, if present, signifies that the associated element should no longer be used
     * and may be removed or become unsupported in future versions.
     * @since 3.8.1
     */
    val deprecated: Boolean? get() = node.get("deprecated")?.asBoolean()

    /**
     * Represents a nullable Type object, derived from the JSON node "type".
     * The value is extracted only if the "type" field exists in the node,
     * is a string, and can be converted into a Type instance using the
     * Type::of function.
     * @since 3.8.1
     */
    val type: Type? get() = node.get("type")
        ?.takeIf { it.isString }
        ?.asString()
        ?.let(Type::of)

    /**
     * Retrieves the `const` value from the JSON schema node, if present.
     *
     * The `const` keyword is used to restrict a value in a JSON schema to a single specified value.
     * If the keyword is present in the schema, this property will return the corresponding value,
     * otherwise it will return `null`.
     * @since 3.8.1
     */
    val const: JsonNode? get() = node.get("const")

    /**
     * The `minLength` property in a JSON schema represents the minimum number of characters
     * that a string must contain to be considered valid. This value is optional and may be null
     * if the property is not explicitly defined in the schema.
     *
     * @return The minimum length for a string, or null if not specified in the schema.
     * @since 3.8.1
     */
    val minLength: Int? get() = node.get("minLength")?.asInt()
    /**
     * Represents the optional maximum length constraint for a string value in the JSON Schema.
     *
     * The `maxLength` constraint specifies the maximum number of characters allowed for a string.
     * Retrieving this value will return an integer if the constraint is defined within the schema,
     * or `null` if the constraint is not specified.
     *
     * This property is typically utilized for validating string data types in JSON Schema-based
     * processing or validation.
     * @since 3.8.1
     */
    val maxLength: Int? get() = node.get("maxLength")?.asInt()
    /**
     * Represents the regular expression pattern for the associated JSON schema.
     * This pattern is typically used to constrain string values, ensuring they match
     * the defined regular expression.
     *
     * The value is retrieved dynamically from the underlying JSON node, and it may be null
     * if the pattern is not defined in the schema.
     * @since 3.8.1
     */
    val pattern: String? get() = node.get("pattern")?.asString()
    /**
     * Represents the format of the node, if available.
     *
     * This property retrieves the value associated with the "format" key
     * from the node and returns it as a nullable String. If the key does
     * not exist or the value is not a string, the result will be null.
     * @since 3.8.1
     */
    val format: String? get() = node.get("format")?.asString()
    /**
     * Retrieves the "contentEncoding" attribute from the JSON schema node, if available.
     *
     * The "contentEncoding" attribute is used in JSON Schema to specify the encoding format
     * for a string value. For example, it may indicate that the string is Base64-encoded.
     * This property is optional and may return null if the "contentEncoding" attribute
     * is not present in the schema node.
     *
     * @return The value of the "contentEncoding" attribute as a string, or null if not defined.
     * @since 3.8.1
     */
    val contentEncoding: String? get() = node.get("contentEncoding")?.asString()
    /**
     * Retrieves the media type for the content described by this JSON Schema, if specified.
     *
     * This property corresponds to the `contentMediaType` keyword in JSON Schema,
     * which is used to indicate the MIME type of the data expected to conform to the schema.
     *
     * @return The media type as a string, or `null` if no media type is specified.
     * @since 3.8.1
     */
    val contentMediaType: String? get() = node.get("contentMediaType")?.asString()

    /**
     * Represents the minimum value constraint defined in the JSON Schema for numeric types.
     * It is retrieved from the "minimum" field in the schema's JSON node, if present.
     * If the "minimum" field is not defined, this property returns `null`.
     * @since 3.8.1
     */
    val minimum: Number? get() = node.get("minimum")?.numberValue()
    /**
     * Represents the maximum value constraint in a JSON Schema.
     * This value restricts the allowed values for a numeric type to
     * be less than or equal to the specified maximum.
     *
     * The value is retrieved from the `maximum` field of the underlying JSON node.
     * If the field is not present, `null` is returned.
     * @since 3.8.1
     */
    val maximum: Number? get() = node.get("maximum")?.numberValue()
    /**
     * Represents the "exclusiveMinimum" constraint in a JSON Schema.
     * This property, when specified, indicates that the value of the associated data
     * must be strictly greater than this number to be considered valid.
     * The value is optional and may be null if the "exclusiveMinimum" constraint
     * is not defined in the schema.
     * @since 3.8.1
     */
    val exclusiveMinimum: Number? get() = node.get("exclusiveMinimum")?.numberValue()
    /**
     * Represents the `exclusiveMaximum` constraint in a JSON Schema, which specifies an upper bound
     * for a numeric value that must not be inclusive.
     *
     * This property corresponds to a field in the JSON Schema definition and retrieves the value
     * from the underlying JSON node if it exists.
     *
     * The `exclusiveMaximum` is typically used to enforce stricter constraints on a number by
     * ensuring that its value must be strictly less than the defined threshold.
     *
     * @return The numeric value of the `exclusiveMaximum` constraint if defined, or `null` otherwise.
     * @since 3.8.1
     */
    val exclusiveMaximum: Number? get() = node.get("exclusiveMaximum")?.numberValue()
    /**
     * Retrieves the `multipleOf` constraint from the JSON schema, if present.
     *
     * The `multipleOf` constraint is used to specify that a numeric value must be a multiple of
     * a specified number. If the constraint is not defined in the schema, this property will return `null`.
     *
     * @return The numeric value representing the `multipleOf` constraint, or `null` if not specified.
     * @since 3.8.1
     */
    val multipleOf: Number? get() = node.get("multipleOf")?.numberValue()

    /**
     * Represents the `items` property of a JSON schema node.
     *
     * This property is typically used to describe the allowed schema of the items
     * within an array in JSON. If the `items` property is present in the node,
     * it is converted into an instance of [JsonSchema]. If the `items` property
     * is not present, the value will be `null`.
     * @since 3.8.1
     */
    val items: JsonSchema? get() = node.get("items")?.let(::JsonSchema)
    /**
     * A property that retrieves the "contains" schema from a JSON node, if present.
     *
     * This property attempts to access a child node named "contains" from the current JSON structure
     * and interprets it as a `JsonSchema` object. If the "contains" node is not present, the property will return `null`.
     *
     * @return A `JsonSchema` instance representing the "contains" node, or `null` if the node is absent.
     * @since 3.8.1
     */
    val contains: JsonSchema? get() = node.get("contains")?.let(::JsonSchema)
    /**
     * Represents the minimum number of items that an array must contain
     * to satisfy a JSON Schema validation constraint.
     * This value is extracted from the "minItems" property in the JSON node.
     * A null value implies that the constraint is not specified in the schema.
     * @since 3.8.1
     */
    val minItems: Int? get() = node.get("minItems")?.asInt()
    /**
     * Represents the maximum number of items allowed in an array.
     * This value is retrieved from the "maxItems" field in the JSON schema node,
     * and is parsed as an integer if the field is present.
     *
     * If the "maxItems" field is not defined in the schema, this property will return null.
     * @since 3.8.1
     */
    val maxItems: Int? get() = node.get("maxItems")?.asInt()
    /**
     * Specifies the minimum number of occurrences of a contained schema
     * that must be satisfied in a JSON array for the schema to be considered valid.
     *
     * This property directly maps to the "minContains" keyword in a JSON Schema.
     * It is nullable, meaning that if the value is not explicitly defined,
     * the property will return `null`.
     *
     * Typically used in conjunction with the "contains" keyword.
     * @since 3.8.1
     */
    val minContains: Int? get() = node.get("minContains")?.asInt()
    /**
     * Represents the maximum number of elements required in case the `contains`
     * constraint is used within the JSON schema. This corresponds to the `maxContains`
     * keyword in JSON Schema specifications.
     *
     * If defined, this value indicates the upper limit for the number of items
     * meeting the `contains` condition. If not explicitly specified in the schema,
     * the value will be `null`.
     * @since 3.8.1
     */
    val maxContains: Int? get() = node.get("maxContains")?.asInt()
    /**
     * Indicates whether all items in an array instance must be unique.
     *
     * This property is a JSON Schema attribute that enforces the uniqueness of array elements
     * when its value is `true`. If omitted or set to `false`, array elements are not required
     * to be unique.
     *
     * @return A nullable boolean value representing the uniqueness requirement for array items.
     *         Returns `null` if the `uniqueItems` property is not defined in the schema.
     * @since 3.8.1
     */
    val uniqueItems: Boolean? get() = node.get("uniqueItems")?.asBoolean()

    /**
     * Represents the schema definition for validating additional properties in a JSON object.
     * If present, this specifies the schema that all additional properties in the JSON object
     * must conform to. If absent, additional properties are allowed without restriction by
     * default unless otherwise specified.
     *
     * This property is derived from the "additionalProperties" key of the JSON schema node.
     * If the "additionalProperties" key exists, its schema is parsed and returned as a
     * `JsonSchema` instance.
     * @since 3.8.1
     */
    val additionalProperties: JsonSchema? get() = node.get("additionalProperties")?.let(::JsonSchema)
    /**
     * Retrieves the unevaluated properties from the JSON Schema.
     *
     * This property represents a `unevaluatedProperties` definition in a JSON Schema,
     * which specifies a schema that any properties not evaluated by the current schema must conform to.
     * If `unevaluatedProperties` is not defined within the schema, this value will be `null`.
     * @since 3.8.1
     */
    val unevaluatedProperties: JsonSchema? get() = node.get("unevaluatedProperties")?.let(::JsonSchema)
    /**
     * Retrieves the `propertyNames` keyword of a JSON Schema. The `propertyNames` keyword,
     * if present, defines a schema that all property names in an object must conform to.
     *
     * This property returns a `JsonSchema` representation of the `propertyNames` field in
     * the JSON Schema node, or `null` if the field is not present.
     * @since 3.8.1
     */
    val propertyNames: JsonSchema? get() = node.get("propertyNames")?.let(::JsonSchema)
    /**
     * Represents the minimum number of properties that an object must have to satisfy the schema.
     *
     * This value corresponds to the "minProperties" keyword in a JSON Schema. If set, it defines a
     * constraint that requires the object to contain at least the specified number of properties.
     * If the JSON node does not specify this value, it defaults to `null`, indicating no such
     * constraint is defined in the schema.
     * @since 3.8.1
     */
    val minProperties: Int? get() = node.get("minProperties")?.asInt()
    /**
     * Represents the maximum number of properties allowed in a JSON object
     * as specified by the `maxProperties` keyword in a JSON Schema.
     *
     * If the `maxProperties` keyword is not defined in the schema, this property will be `null`.
     * The value is extracted from the corresponding JSON node and interpreted as an integer.
     * @since 3.8.1
     */
    val maxProperties: Int? get() = node.get("maxProperties")?.asInt()

    /**
     * Retrieves the JSON Schema associated with the "if" keyword within the current schema node.
     *
     * The "if" keyword is used in conditional subschemas to define a schema that should be applied
     * when certain conditions are met.
     *
     * @return An instance of [JsonSchema] representing the schema linked with the "if" keyword,
     * or `null` if the "if" keyword is not defined in the current schema node.
     * @since 3.8.1
     */
    val `if`: JsonSchema? get() = node.get("if")?.let(::JsonSchema)
    /**
     * Retrieves the JSON Schema defined under the "then" keyword within the current schema,
     * if present. The "then" keyword is used in conditional subschemas, specifying the schema
     * to apply if the "if" condition in the same schema is met.
     *
     * If the "then" keyword is not defined, this property will return `null`.
     *
     * @return A `JsonSchema` instance representing the "then" subschema, or `null` if undefined.
     * @since 3.8.1
     */
    val then: JsonSchema? get() = node.get("then")?.let(::JsonSchema)
    /**
     * Represents the "else" keyword in a JSON Schema, used to define the schema
     * that should apply when the "if" condition is not satisfied.
     *
     * This property will return a `JsonSchema` instance corresponding to the "else"
     * keyword if it exists in the JSON node; otherwise, it returns `null`.
     * @since 3.8.1
     */
    val `else`: JsonSchema? get() = node.get("else")?.let(::JsonSchema)

    /**
     * Represents a `not` constraint in a JSON schema, which specifies that the data
     * must not validate against the schema defined within this property.
     *
     * This property is optionally retrieved from the `not` node of the JSON schema tree.
     * If the `not` node exists, it is wrapped as a [JsonSchema], otherwise, it is null.
     * @since 3.8.1
     */
    val not: JsonSchema? get() = node.get("not")?.let(::JsonSchema)

    /**
     * Indicates whether the underlying JSON node represents a boolean value.
     *
     * This property evaluates the type of the JSON node and returns `true`
     * if it is specifically classified as a boolean type, and `false` otherwise.
     * @since 3.8.1
     */
    val isBoolean: Boolean get() = node.isBoolean
    /**
     * Evaluates whether the associated JSON node represents a boolean value
     * and returns its corresponding boolean value.
     *
     * This property performs a check to ensure the node is of type boolean
     * and extracts its value if the check passes.
     *
     * @return `true` if the node is a boolean and its value is `true`,
     *         `false` otherwise.
     * @since 3.8.1
     */
    val isTrue: Boolean get() = node.isBoolean && node.asBoolean()
    /**
     * Evaluates whether the underlying JSON node represents a boolean value that is explicitly `false`.
     * This property checks if the node is of boolean type and has a value of `false`.
     * @since 3.8.1
     */
    val isFalse: Boolean get() = node.isBoolean && !node.asBoolean()
    /**
     * Indicates whether the JSON schema node contains a reference (`$ref`) property.
     *
     * This property checks if the underlying JSON schema node defines a reference by
     * utilizing the `$ref` keyword, which is used to resolve external or internal schema references.
     *
     * A value of `true` means the schema node has a `$ref` property, while `false` indicates
     * otherwise.
     * @since 3.8.1
     */
    val isRef: Boolean get() = node.has($$"$ref")

    /**
     * The size of the `prefixItems` array in the JSON schema.
     *
     * This property retrieves the number of elements in the `prefixItems` keyword of a JSON Schema.
     * If the `prefixItems` keyword is not present, it will return 0.
     *
     * The `prefixItems` keyword is typically used to define a fixed list of schemas
     * that correspond to specific positions in an array instance.
     * @since 3.8.1
     */
    val prefixItemsSize: Int get() = node.get("prefixItems")?.size() ?: 0

    /**
     * Retrieves a list of required property names as defined in the JSON schema.
     *
     * The property names are extracted from the "required" field within the JSON schema.
     * If the "required" field is missing or not an array, the result will be an empty list.
     *
     * @return A list of strings representing the required property names, or an empty list
     *         if no required properties are specified.
     * @since 3.8.1
     */
    val requiredNames: List<String> get() = node.get("required")?.asArray()?.elements()?.map { it.asString() } ?: emptyList()
    /**
     * Retrieves the list of property names defined in the "properties" field of the schema.
     *
     * If the "properties" field is not present or cannot be parsed as an array,
     * an empty list is returned.
     *
     * This property provides a read-only view of the schema's declared properties, if available.
     * @since 3.8.1
     */
    val propertiesNames: List<String> get() = node.get("properties")?.asArray()?.propertyNames()?.toList() ?: emptyList()

    /**
     * A sequence of pairs representing the properties of a JSON Schema.
     * Each pair contains a string representing the property name and an instance of the `JsonSchema` class
     * representing the associated schema for that property.
     *
     * This sequence is derived from the "properties" node in the JSON Schema, if present. If the "properties" node
     * is not present, an empty sequence is returned.
     * @since 3.8.1
     */
    val propertiesSequence: Sequence<Pair<String, JsonSchema>> get() =
        node.get("properties")?.properties()?.asSequence()
            ?.map { [k, v] -> k to JsonSchema(v) } ?: emptySequence()

    /**
     * A lazily evaluated sequence of pairs representing the definition schemas within the `$defs`
     * or `definitions` property of the current JSON Schema node. Each pair consists of the
     * definition name (`String`) and its corresponding `JsonSchema` object.
     *
     * If neither the `$defs` nor `definitions` property exists, the sequence will be empty.
     *
     * The sequence is derived by accessing the properties of `$defs` or `definitions`,
     * if available, and mapping them into key-value pairs where the key is the property name
     * and the value is a `JsonSchema` object created from the corresponding JSON node.
     * @since 3.8.1
     */
    val defsSequence: Sequence<Pair<String, JsonSchema>> get() =
        (node.get($$"$defs") ?: node.get("definitions"))?.properties()?.asSequence()
            ?.map { [k, v] -> k to JsonSchema(v) } ?: emptySequence()

    /**
     * A sequence of JSON Schema objects derived from the `allOf` keyword in the schema definition.
     *
     * The `allOf` keyword is used in JSON Schema to require that the data must satisfy all the schemas
     * specified in the list under the `allOf` keyword. This property processes the `allOf` field of
     * the JSON node representing the schema, mapping each child schema under `allOf` to a
     * `JsonSchema` object.
     *
     * If the `allOf` field is missing or is not a valid array, this sequence will be empty.
     * @since 3.8.1
     */
    val allOfSequence: Sequence<JsonSchema> get() =
        node.get("allOf")?.asSequence()?.map(::JsonSchema) ?: emptySequence()

    /**
     * Represents a sequence of `JsonSchema` objects derived from the `anyOf` property in the JSON schema definition.
     *
     * The `anyOf` property in a JSON schema specifies an array of schemas, where at least one of the schemas must be valid
     * for the supplied data. This property is commonly used for defining a set of alternative validation constraints.
     *
     * If the `anyOf` property is not present in the JSON schema, an empty sequence is returned.
     * @since 3.8.1
     */
    val anyOfSequence: Sequence<JsonSchema> get() =
        node.get("anyOf")?.asSequence()?.map(::JsonSchema) ?: emptySequence()

    /**
     * Represents a sequence of `JsonSchema` objects extracted from the `oneOf` keyword in the JSON schema definition.
     * The `oneOf` keyword defines an array of schema alternatives, where the valid JSON instance must validate
     * against exactly one of the schemas in the array.
     *
     * If the `oneOf` keyword is not present in the schema, an empty sequence is returned.
     * @since 3.8.1
     */
    val oneOfSequence: Sequence<JsonSchema> get() =
        node.get("oneOf")?.asSequence()?.map(::JsonSchema) ?: emptySequence()

    /**
     * A sequence representing the values of the "enum" keyword in a JSON Schema.
     *
     * The "enum" keyword defines a set of fixed values that an instance property is
     * allowed to have. This property retrieves those values, if present, from the
     * underlying JSON node. If the "enum" keyword is not defined in the schema, an
     * empty sequence is returned.
     * @since 3.8.1
     */
    val enumSequence: Sequence<JsonNode> get() =
        node.get("enum")?.asSequence() ?: emptySequence()

    /**
     * Provides a sequence of example values specified in the JSON schema.
     *
     * This sequence retrieves the "examples" attribute from the schema's JSON object.
     * If the "examples" attribute is not present, an empty sequence is returned.
     * @since 3.8.1
     */
    val examplesSequence: Sequence<JsonNode> get() =
        node.get("examples")?.asSequence() ?: emptySequence()

    /**
     * A sequence of `Type` objects derived from the "type" node.
     *
     * The sequence is constructed by evaluating the "type" node within the `node` object.
     * It checks if the node is an array and converts its elements into a sequence of `Type`.
     * Non-representable elements are excluded from the sequence.
     *
     * If the "type" node does not exist or is not an array, an empty sequence is returned.
     * @since 3.8.1
     */
    val typesSequence: Sequence<Type> get() =
        node.get("type")?.takeIf { it.isArray }?.asSequence()
            ?.mapNotNull { Type.of(it.asString()) } ?: emptySequence()

    /**
     * Represents the JSON Schema version inferred from the given schema identifier.
     *
     * Maps specific schema URIs to their corresponding `Version` enumeration value.
     * If the schema identifier does not match any of the predefined mappings,
     * the default version `Version.V2020_12` is returned.
     * @since 3.8.1
     */
    val version: Version get() = when (schema) {
        "https://json-schema.org/draft/2020-12/schema" -> Version.V2020_12
        "https://json-schema.org/draft/2019-09/schema" -> Version.V2019_09
        "http://json-schema.org/draft-07/schema#" -> Version.V7
        "http://json-schema.org/draft-06/schema#" -> Version.V6
        "http://json-schema.org/draft-04/schema#" -> Version.V4
        else -> Version.V2020_12
    }

    /**
     * Secondary constructor that initializes the object using a JSON string.
     *
     * @param json A JSON-formatted string used to initialize the instance.
     * @since 3.8.1
     */
    constructor(@Language("json") json: String) : this(Json(json))
    /**
     * Constructs an instance using the provided JsonNode.
     *
     * @param json The JsonNode object to be parsed and used for initialization.
     * @since 3.8.1
     */
    constructor(json: JsonNode) : this(json.asJson()())
    /**
     * Constructs an instance using the provided JsonNode.
     *
     * @param json The JsonNode object to be parsed and used for initialization.
     * @since 3.8.1
     */
    constructor(json: com.fasterxml.jackson.databind.JsonNode) : this(json.asJson()())

    init {
        try {
            JsonSchemaFactory
                .getInstance(version.toVersionFlag())
                .getSchema(OLD_MAPPER.readTree(json.value))
        } catch (e: Exception) {
            throw MalformedInputException("Input is not a valid JsonSchema", e)
        }
        val errors = validateAgainstMetaSchema(json)
        errors.validateInputFormat(
            message = "Input is not a valid JsonSchema:\n${errors.joinToString(";\n") { it.message }}",
            predicate = Set<ValidationMessage>::isEmpty
        )
    }

    companion object {
        /**
         * Checks if the string is a valid JSON by attempting to parse it and verifying if the result
         * is either a JSON object or a JSON array.
         *
         * @receiver The string to validate as JSON.
         * @return `true` if the string represents valid JSON and is either an object or an array,
         *         otherwise `false`.
         * @since 3.8.1
         */
        fun String.isValidJsonSchema() = runCatching { JsonSchema(this) }.isSuccess
        /**
         * Validates whether the current JSON node adheres to the rules of a valid JSON Schema.
         *
         * This function attempts to construct a `JsonSchema` object using the current JSON node. If the
         * construction succeeds, the JSON node is considered a valid JSON Schema. Otherwise, it is deemed invalid.
         *
         * @receiver The JSON node to be validated as a JSON Schema.
         * @return `true` if the JSON node is a valid schema, `false` otherwise.
         * @since 3.8.1
         */
        fun Json.isValidJsonSchema() = runCatching { JsonSchema(this) }.isSuccess
        /**
         * Validates whether the current `JsonNode` represents a valid JSON Schema.
         *
         * This function attempts to create a `JsonSchema` instance using the current `JsonNode`.
         * If the instantiation succeeds without throwing an exception, the method returns `true`,
         * indicating that the `JsonNode` is a valid JSON Schema. If an exception is thrown during
         * instantiation, the method returns `false`.
         *
         * @return `true` if the `JsonNode` is a valid JSON Schema, otherwise `false`.
         * @since 3.8.1
         */
        fun JsonNode.isValidJsonSchema() = runCatching { JsonSchema(this) }.isSuccess
        /**
         * Checks if the current JsonNode represents a valid JSON Schema.
         *
         * This function attempts to create a `JsonSchema` instance from the receiver `JsonNode`.
         * If the instantiation is successful, it indicates that the node is a valid JSON Schema.
         *
         * @receiver The `JsonNode` to validate as a JSON Schema.
         * @return `true` if the `JsonNode` is a valid JSON Schema, `false` otherwise.
         * @since 3.8.1
         */
        fun com.fasterxml.jackson.databind.JsonNode.isValidJsonSchema() = runCatching { JsonSchema(this) }.isSuccess
        /**
         * Converts a string containing a JSON schema definition into a `JsonSchema` object.
         *
         * This method attempts to parse the string as a JSON Schema and wraps the result
         * in a `Result` object. If the operation succeeds, the `Result` will contain the
         * `JsonSchema` instance; otherwise, it will contain the exception that was thrown
         * during parsing.
         *
         * @receiver The JSON schema in string format to be parsed.
         * @return A `Result` object containing either the successfully parsed `JsonSchema`
         *         or an exception if parsing fails.
         * @since 3.8.1
         */
        fun String.toJsonSchema() = runCatching { JsonSchema(this) }
        /**
         * Converts a `JsonNode` into a `JsonSchema` object wrapped in a `Result`.
         *
         * This method attempts to construct a `JsonSchema` instance based on the provided
         * JSON node. If the conversion is successful, the `JsonSchema` instance is returned
         * within a `Result`. In case of failure, the exception is captured inside the `Result`.
         *
         * @receiver The `JsonNode` that is being transformed into a `JsonSchema`.
         * @return A `Result` containing the `JsonSchema` instance if the operation is successful,
         *         or an exception if the conversion fails.
         * @since 3.8.1
         */
        fun JsonNode.toJsonSchema() = runCatching { JsonSchema(this) }
        /**
         * Converts the current `JsonNode` instance into a `JsonSchema`.
         *
         * This extension function attempts to create a `JsonSchema` object
         * using the invoking JSON node. The operation is performed within
         * a `runCatching` block, which captures any exceptions that might occur
         * during the schema creation process and returns the result as a `Result` object.
         *
         * @receiver The `JsonNode` from which a `JsonSchema` instance is derived.
         * @return A `Result` containing the created `JsonSchema` instance if successful,
         *         or a failure with the thrown exception if the operation fails.
         * @since 3.8.1
         */
        fun com.fasterxml.jackson.databind.JsonNode.toJsonSchema() = runCatching { JsonSchema(this) }
        /**
         * Converts the current `Json` instance into a `JsonSchema`.
         *
         * This function attempts to create a `JsonSchema` object from the current `Json` instance.
         * The operation is encapsulated in a `Result` to handle potential failures during the conversion process.
         *
         * @receiver The `Json` instance to be converted into a `JsonSchema`.
         * @return A `Result` encapsulating the created `JsonSchema` on success, or a failure otherwise.
         * @since 3.8.1
         */
        fun Json.toJsonSchema() = runCatching { JsonSchema(this) }

        private fun validateAgainstMetaSchema(json: Json): Set<ValidationMessage> {
            val specVersion = detectSpecVersionFor(json)
            val factory = JsonSchemaFactory.getInstance(specVersion)
            val metaSchemaUri = when (specVersion) {
                SpecVersion.VersionFlag.V202012 -> "https://json-schema.org/draft/2020-12/schema"
                SpecVersion.VersionFlag.V201909 -> "https://json-schema.org/draft/2019-09/schema"
                SpecVersion.VersionFlag.V7 -> "http://json-schema.org/draft-07/schema#"
                SpecVersion.VersionFlag.V6 -> "http://json-schema.org/draft-06/schema#"
                SpecVersion.VersionFlag.V4 -> "http://json-schema.org/draft-04/schema#"
            }
            return factory.getSchema(java.net.URI(metaSchemaUri)).validate(json.toFasterXmlJsonNode())
        }

        private fun detectSpecVersionFor(json: Json): SpecVersion.VersionFlag =
            when (json.getAsNode($$"$schema")?.asString()) {
                "https://json-schema.org/draft/2020-12/schema" -> SpecVersion.VersionFlag.V202012
                "https://json-schema.org/draft/2019-09/schema" -> SpecVersion.VersionFlag.V201909
                "http://json-schema.org/draft-07/schema#" -> SpecVersion.VersionFlag.V7
                "http://json-schema.org/draft-06/schema#" -> SpecVersion.VersionFlag.V6
                "http://json-schema.org/draft-04/schema#" -> SpecVersion.VersionFlag.V4
                else -> SpecVersion.VersionFlag.V202012
            }
    }

    /**
     * Retrieves the JsonSchema for a specified property name in a JSON object.
     *
     * @param name The name of the property to retrieve the schema for.
     * @return The JsonSchema associated with the specified property name, or null if no schema is found.
     * @since 3.8.1
     */
    infix fun property(name: String): JsonSchema? =
        node.get("properties")?.get(name)?.let(::JsonSchema)

    /**
     * Retrieves a definition schema by its name from the JSON Schema definitions or `$defs` node.
     *
     * @param name The name of the definition to be retrieved.
     * @return The corresponding `JsonSchema` if found, or null if not present.
     * @since 3.8.1
     */
    infix fun definition(name: String): JsonSchema? =
        (node.get($$"$defs") ?: node.get("definitions"))?.get(name)?.let(::JsonSchema)

    /**
     * Retrieves a `JsonSchema` instance associated with a specific pattern property.
     *
     * This function looks up the "patternProperties" in the current JSON schema node and attempts
     * to retrieve the schema associated with the given pattern. If the pattern exists, a `JsonSchema`
     * instance is returned; otherwise, `null` is returned.
     *
     * @param pattern The pattern key to look up within the "patternProperties".
     * @return A `JsonSchema` instance if the pattern property exists, or `null` if it does not.
     * @since 3.8.1
     */
    infix fun patternProperty(pattern: String): JsonSchema? =
        node.get("patternProperties")?.get(pattern)?.let(::JsonSchema)

    /**
     * Retrieves a dependent schema associated with the specified name from the "dependentSchemas" property
     * of the current JSON schema node, if it exists.
     *
     * @param name The name of the dependent schema to retrieve.
     * @return The corresponding JsonSchema if it exists, or null otherwise.
     * @since 3.8.1
     */
    infix fun dependentSchema(name: String): JsonSchema? =
        node.get("dependentSchemas")?.get(name)?.let(::JsonSchema)

    /**
     * Checks if the specified property name is marked as required in the schema.
     *
     * @param name The name of the property to check.
     * @return `true` if the property name is listed as required in the schema, `false` otherwise.
     * @since 3.8.1
     */
    infix fun isRequired(name: String): Boolean =
        node.get("required")?.any { it.asString() == name } == true

    /**
     * Retrieves the item at the specified index from the "prefixItems" array in the JSON schema, if present.
     *
     * @param index The zero-based index of the item to retrieve from the "prefixItems" array.
     * @return A JsonSchema object representing the item at the specified index, or null if the "prefixItems" array
     *         is not defined or the index is out of bounds.
     * @since 3.8.1
     */
    infix fun prefixItem(index: Int): JsonSchema? =
        node.get("prefixItems")?.get(index)?.let(::JsonSchema)

    /**
     * Validates a JSON object against this JSON Schema.
     *
     * @param json The JSON object to be validated.
     * @since 3.8.1
     */
    infix fun validateJson(json: Json) = json.validateWithSchema(this, version)

    /**
     * Represents a set of predefined types with their corresponding string values.
     * This enum is used to categorize and manage data types in a structured way.
     * @since 3.8.1
     */
    enum class Type(val value: String) {
        /**
         * Represents a string type for the enumeration.
         *
         * This type is used to define a specific category with the value "string".
         * @since 3.8.1
         */
        STRING("string"),
        /**
         * Represents the "number" type in the enumeration.
         * @since 3.8.1
         */
        NUMBER("number"),
        /**
         * Represents the 'integer' type in the Type enumeration.
         * This type is used to indicate values that are whole numbers.
         * @since 3.8.1
         */
        INTEGER("integer"),
        /**
         * Represents a type corresponding to a boolean value.
         *
         * This type is commonly used to signify a true/false or yes/no state.
         * @since 3.8.1
         */
        BOOLEAN("boolean"),
        /**
         * Represents the `null` type in the enumeration.
         * @since 3.8.1
         */
        NULL("null"),
        /**
         * Represents the JSON array type in the Type enumeration.
         * This type is used to define or validate structures containing ordered collections of elements.
         * @since 3.8.1
         */
        ARRAY("array"),
        /**
         * Represents a type with the value "object".
         * @since 3.8.1
         */
        OBJECT("object"),
        /**
         * Represents a type that can accept any value.
         *
         * This type is used to signify flexibility or absence of constraints regarding the value type.
         * @since 3.8.1
         */
        ANY("any");

        companion object {
            /**
             * Finds a matching entry in the `Type` enum whose `value` equals the provided string, ignoring case.
             *
             * @param value The string to match against the `value` property of entries in the `Type` enum.
             * @return The matching `Type` entry, or `null` if no match is found.
             * @since 3.8.1
             */
            infix fun of(value: String) = Type.entries.find { it.value equalsIgnoreCase value }
        }
    }

    /**
     * Enumerates the supported versions of a JSON schema specification.
     * @since 3.8.1
     * @author Tommaso Pastorelli
     */
    enum class Version {
        /**
         * Represents version 4 of the specification.
         * @since 3.8.1
         */
        V4,
        /**
         * Represents version 6 in the set of supported specification versions.
         * @since 3.8.1
         */
        V6,
        /**
         * Represents the version identifier V7.
         *
         * This version corresponds to a specific flag used internally
         * for identifying and handling versioned behavior.
         * @since 3.8.1
         */
        V7,
        /**
         * Represents the V2019_09 version of the specification.
         * This version is part of the supported enumeration values for the `Version` enum.
         * It maps to the corresponding version flag in the `SpecVersion.VersionFlag` representation.
         * @since 3.8.1
         */
        V2019_09,
        /**
         * Represents the version identifier corresponding to the December 2020 specification.
         * This enum constant is part of the `Version` enumeration, used to specify different
         * supported versions of a specification.
         * @since 3.8.1
         */
        V2020_12;

        /**
         * Converts the current [Version] enum instance into its corresponding [SpecVersion.VersionFlag] representation.
         *
         * This function maps each version of the [Version] enum to a specific value in the [SpecVersion.VersionFlag] enum.
         *
         * @return The [SpecVersion.VersionFlag] that corresponds to the current [Version] instance.
         * @since 3.8.1
         */
        internal fun toVersionFlag() = when (this) {
            V4 -> SpecVersion.VersionFlag.V4
            V6 -> SpecVersion.VersionFlag.V6
            V7 -> SpecVersion.VersionFlag.V7
            V2019_09 -> SpecVersion.VersionFlag.V201909
            V2020_12 -> SpecVersion.VersionFlag.V202012
        }
    }
}