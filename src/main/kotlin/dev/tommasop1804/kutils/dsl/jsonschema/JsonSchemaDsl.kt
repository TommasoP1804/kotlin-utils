/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused", "UnusedReceiverParameter")
@file:Since("3.3.0")

package dev.tommasop1804.kutils.dsl.jsonschema

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*

@DslMarker
annotation class JsonSchemaDslMarker

/**
 * A builder class for constructing JSON Schema objects using a DSL-like syntax.
 * This class provides methods to configure various aspects of a JSON Schema definition.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@JsonSchemaDslMarker
class SchemaBuilder {
    /**
     * A mutable container that holds the properties of a JSON schema being constructed.
     * This map associates property names with their corresponding values, which can be of
     * any type, including nullable types. It is used internally by the SchemaBuilder
     * to store the schema's attributes and metadata dynamically.
     * @since 3.3.0
     */
    private val props = linkedMapOf<String, Any?>()

    // ── Meta ──
    /**
     * Sets the `$schema` property in the JSON Schema to define the schema's version or dialect.
     * This property is used to specify the meta-schema against which the JSON Schema is validated.
     *
     * @param uri The URI representing the meta-schema. Defaults to "https://json-schema.org/draft/2020-12/schema".
     * @since 3.3.0
     */
    fun schema(uri: Uri = Uri("https://json-schema.org/draft/2020-12/schema")) { props[$$"$schema"] = uri }
    /**
     * Sets the `$id` keyword of the JSON Schema to the given URI.
     *
     * The `$id` keyword is used to define a unique identifier for the schema, allowing
     * for referencing and reuse within other schemas or documents.
     *
     * @param uri The URI that represents the unique identifier for the schema.
     * @since 3.3.0
     */
    fun id(uri: Uri) { props[$$"$id"] = uri }
    /**
     * Adds a `$ref` property to the schema with the specified URI reference.
     *
     * @param uri The URI reference to be assigned to the `$ref` property.
     * @since 3.3.0
     */
    fun ref(uri: Uri) { props[$$"$ref"] = uri }
    /**
     * Sets the title of the schema.
     *
     * @param value The title to assign to the schema.
     * @since 3.3.0
     */
    fun title(value: String) { props["title"] = value }
    /**
     * Sets the description property in the `props` map to the specified value.
     *
     * @param value The description to be set in the `props` map.
     * @since 3.3.0
     */
    fun description(value: String) { props["description"] = value }
    /**
     * Sets a comment for the schema definition.
     *
     * @param value The comment to be associated with the schema.
     * @since 3.3.0
     */
    fun comment(value: String) { props[$$"$comment"] = value }
    /**
     * Marks the schema as deprecated.
     *
     * @param value Indicates whether the schema is deprecated. Defaults to `true`.
     * @since 3.3.0
     */
    fun deprecated(value: Boolean = true) { props["deprecated"] = value }
    /**
     * Sets the `readOnly` property in the JSON Schema to the specified value.
     *
     * @param value Indicates whether the property is read-only. Defaults to `true`.
     * @since 3.3.0
     */
    fun readOnly(value: Boolean = true) { props["readOnly"] = value }
    /**
     * Sets the `writeOnly` property to the specified value within the schema being built.
     *
     * The `writeOnly` property, when set to `true`, indicates that the data is intended to be written 
     * and not read. This is commonly used to designate sensitive information such as passwords.
     *
     * @param value A boolean indicating whether the property should be marked as write-only. Defaults to `true`.
     * @since 3.3.0
     */
    fun writeOnly(value: Boolean = true) { props["writeOnly"] = value }
    /**
     * Adds example values to the schema. These are used to provide illustrative examples of the expected data.
     *
     * @param values The example values to associate with the schema, provided as a vararg of any type.
     * @since 3.3.0
     */
    fun examples(vararg values: Any?) { props["examples"] = values.toList() }

    // ── Type ──
    /**
     * Sets the `type` keyword of the schema. This can specify one or more data types 
     * that the schema can validate against. If a single type is provided, it is set 
     * as a string. If multiple types are provided, they are stored as a list.
     *
     * @param types One or more string values representing the data types for the schema.
     * @since 3.3.0
     */
    fun type(vararg types: String) {
        props["type"] = if (types.size == 1) types[0] else types.toList()
    }

    /**
     * Sets a constant value in the schema. The value provided will constrain the data to be 
     * exactly equal to the given value.
     *
     * @param value The constant value that the schema should enforce. This can be any type of object.
     * @since 3.3.0
     */
    fun const(value: Any?) { props["const"] = value }
    /**
     * Specifies a set of allowed values for a schema using the `enum` keyword.
     *
     * @param values The array of allowed values to be assigned to the `enum` property of the schema.
     * @since 3.3.0
     */
    fun enum(vararg values: Any?) { props["enum"] = values.toList() }
    /**
     * Sets the "default" property in the `props` map to the specified value.
     *
     * @param value The value to assign to the "default" property. Can be any type, including null.
     * @since 3.3.0
     */
    fun default(value: Any?) { props["default"] = value }

    // ── String ──
    /**
     * Specifies the minimum number of characters that a string value must contain.
     *
     * @param value The minimum length of the string. Must be a non-negative integer.
     * @since 3.3.0
     */
    fun minLength(value: Int) { props["minLength"] = value }
    /**
     * Sets the maximum length constraint for a string value.
     *
     * @param value The maximum allowed length for the string. Must be a non-negative integer.
     * @since 3.3.0
     */
    fun maxLength(value: Int) { props["maxLength"] = value }
    /**
     * Sets a regular expression pattern to be used for validation or matching.
     *
     * @param regex The regular expression pattern to be applied.
     * @since 3.3.0
     */
    fun pattern(regex: Regex) { props["pattern"] = regex.toString() }
    /**
     * Sets the format string for the underlying properties map.
     *
     * @param fmt The format string to be assigned.
     * @since 3.3.0
     */
    fun format(fmt: String) { props["format"] = fmt }
    /**
     * Sets the media type for the content associated with this schema.
     *
     * @param value The media type to be assigned. It is typically a string representation of a MIME type 
     *              (e.g., "application/json", "text/plain").
     * @since 3.3.0
     */
    fun contentMediaType(value: String) { props["contentMediaType"] = value }
    /**
     * Sets the content encoding for the schema being built.
     *
     * @param value The encoding value to be applied (e.g., "base64"). It specifies how the content within the schema
     * should be encoded when transmitted or processed.
     * @since 3.3.0
     */
    fun contentEncoding(value: String) { props["contentEncoding"] = value }

    // ── Numeric ──
    /**
     * Sets the minimum value constraint for a numeric type in the schema.
     * 
     * @param value The minimum value to be applied.
     * @since 3.3.0
     */
    fun minimum(value: Number) { props["minimum"] = value }
    /**
     * Sets the maximum value constraint for a numeric schema.
     *
     * @param value The maximum allowable value for the schema element.
     * @since 3.3.0
     */
    fun maximum(value: Number) { props["maximum"] = value }
    /**
     * Specifies an exclusive minimum value for a numeric property in the schema.
     * The value defined must be strictly less than the input number for the schema validation to pass.
     *
     * @param value The exclusive minimum numeric value to be set.
     * @since 3.3.0
     */
    fun exclusiveMinimum(value: Number) { props["exclusiveMinimum"] = value }
    /**
     * Sets the `exclusiveMaximum` constraint for the schema. This specifies an upper limit
     * for a numeric value that must not be equal to or exceed the given number.
     *
     * @param value The upper limit that the numeric value must be strictly less than.
     * @since 3.3.0
     */
    fun exclusiveMaximum(value: Number) { props["exclusiveMaximum"] = value }
    /**
     * Specifies that a numeric instance must be a multiple of the provided value.
     *
     * @param value The numeric value that the instance must be a multiple of.
     * @since 3.3.0
     */
    fun multipleOf(value: Number) { props["multipleOf"] = value }

    // ── Object ──
    /**
     * Configures the properties using the provided builder block.
     *
     * @param block A lambda with receiver of type [PropertiesBuilder] used to define and build properties.
     * @since 3.3.0
     */
    fun properties(block: ReceiverConsumer<PropertiesBuilder>) {
        props["properties"] = PropertiesBuilder().apply(block).build()
    }

    /**
     * Adds pattern-based property schemas to the schema definition. 
     * This method is used to define a set of property schemas where the property names 
     * match specific regular expression patterns.
     *
     * @param block A lambda with receiver of type `PropertiesBuilder` used to configure 
     *              the pattern-based property schemas. Use this block to define mappings 
     *              of regular expression patterns to their corresponding schemas.
     * @since 3.3.0
     */
    fun patternProperties(block: ReceiverConsumer<PropertiesBuilder>) {
        props["patternProperties"] = PropertiesBuilder().apply(block).build()
    }

    /**
     * Configures whether additional properties are allowed in the schema.
     *
     * @param allowed A boolean indicating whether additional properties are permitted. 
     *                If true, additional properties are allowed; otherwise, they are not.
     * @since 3.3.0
     */
    fun additionalProperties(allowed: Boolean) { props["additionalProperties"] = allowed }
    /**
     * Configures the `additionalProperties` schema for the current JSON schema being built.
     * This method allows for the specification of additional properties in the schema
     * as either a new sub-schema or a more complex structure.
     *
     * @param schema a lambda receiving a `SchemaBuilder` used to define the schema for the additional properties.
     * @since 3.3.0
     */
    fun additionalProperties(schema: ReceiverConsumer<SchemaBuilder>) {
        props["additionalProperties"] = SchemaBuilder().apply(schema).build()
    }

    /**
     * Defines a set of properties that are required for the schema.
     * The names of the required properties are passed as vararg parameters.
     *
     * @param names The names of the properties that are required.
     * @since 3.3.0
     */
    fun required(vararg names: String) { props["required"] = names.toList() }
    /**
     * Sets the minimum number of properties that an object must have to satisfy the schema.
     *
     * @param value The minimum number of properties required. Must be a non-negative integer.
     * @since 3.3.0
     */
    fun minProperties(value: Int) { props["minProperties"] = value }
    /**
     * Sets the maximum number of properties that an object can have.
     *
     * @param value The maximum number of properties allowed for the object.
     * @since 3.3.0
     */
    fun maxProperties(value: Int) { props["maxProperties"] = value }
    /**
     * Configures the `propertyNames` constraint in the JSON Schema.
     * This constraint is used to validate the names of the properties of an object
     * against a specified schema.
     *
     * @param block A lambda that defines the schema structure for validating property names.
     * @since 3.3.0
     */
    fun propertyNames(block: ReceiverConsumer<SchemaBuilder>) {
        props["propertyNames"] = SchemaBuilder().apply(block).build()
    }

    /**
     * Configures dependent required properties using a builder pattern. The dependent required feature is used
     * to specify that the presence of a particular property in an object requires the presence of certain other 
     * properties.
     *
     * @param block the lambda function used to define the dependencies between properties, using the 
     *              `DependentRequiredBuilder` DSL.
     * @since 3.3.0
     */
    fun dependentRequired(block: ReceiverConsumer<DependentRequiredBuilder>) {
        props["dependentRequired"] = DependentRequiredBuilder().apply(block).build()
    }

    /**
     * Specifies a set of dependent schemas for properties of the current schema. This function allows defining
     * relationships where the presence of a property in an instance requires validation against a specific schema.
     *
     * @param block A lambda expression where dependent schemas are defined using the `PropertiesBuilder` DSL.
     * @since 3.3.0
     */
    fun dependentSchemas(block: ReceiverConsumer<PropertiesBuilder>) {
        props["dependentSchemas"] = PropertiesBuilder().apply(block).build()
    }

    /**
     * Sets the schema for items in an array.
     *
     * @param schema The schema definition to be applied to each item in the array.
     */
// ── Array ──
    fun items(schema: DataMap) { props["items"] = schema }
    /**
     * Configures items for a JSON schema array. The `block` parameter allows customization of the schema
     * for items within the array. This method is typically used to specify the structure or constraints
     * of elements in an array type schema.
     *
     * @param block A lambda that defines the schema for the array items using a `SchemaBuilder`. The schema
     *              is constructed and applied to the `items` property of the JSON schema being built.
     */
    fun items(block: ReceiverConsumer<SchemaBuilder>) { props["items"] = SchemaBuilder().apply(block).build() }
    /**
     * Adds the provided schemas to the `prefixItems` property of the underlying properties map.
     *
     * @param schemas A variable number of DataMap objects to be set as the value for `prefixItems`.
     */
    fun prefixItems(vararg schemas: DataMap) { props["prefixItems"] = schemas.toList() }
    /**
     * Adds a "contains" constraint to the current schema by applying the provided block to a SchemaBuilder.
     *
     * This method allows defining a schema constraint where an array must contain at least one element
     * conforming to the schema defined within the block.
     *
     * @param block A lambda function used to configure the SchemaBuilder for the "contains" constraint.
     */
    fun contains(block: ReceiverConsumer<SchemaBuilder>) { props["contains"] = SchemaBuilder().apply(block).build() }
    /**
     * Sets the minimum number of items allowed.
     *
     * @param value The minimum number of items to be set.
     */
    fun minItems(value: Int) { props["minItems"] = value }
    /**
     * Sets the maximum number of items allowed.
     *
     * @param value The maximum number of items to be set.
     */
    fun maxItems(value: Int) { props["maxItems"] = value }
    /**
     * Sets whether array items in the schema must be unique.
     *
     * @param value a Boolean indicating whether the items in the array should be unique. Defaults to `true`.
     */
    fun uniqueItems(value: Boolean = true) { props["uniqueItems"] = value }
    /**
     * Sets the minimum number of occurrences of the item that must satisfy the `contains` condition
     * within an array. This is part of JSON Schema validation for arrays.
     *
     * @param value The minimum number of items required. Must be a non-negative integer.
     */
    fun minContains(value: Int) { props["minContains"] = value }
    /**
     * Sets the maximum number of occurrences of schema-defined elements that must be present 
     * in an array for the array to be considered valid.
     *
     * @param value The maximum number of occurrences.
     */
    fun maxContains(value: Int) { props["maxContains"] = value }

    /**
     * Adds a composition constraint where all of the provided schemas must be valid.
     * The `allOf` keyword is used to combine multiple schema definitions, and an
     * instance must validate against all the schemas for the overall validation to succeed.
     *
     * @param schemas A variable number of schema definitions represented as `DataMap` 
     *                instances. These schemas are combined under the `allOf` keyword.
     */
// ── Composition ──
    fun allOf(vararg schemas: DataMap) { props["allOf"] = schemas.toList() }
    /**
     * Adds all the schemas defined in the provided configuration block to the "allOf" property.
     *
     * @param block A lambda with `SchemaListBuilder` as its receiver, used to define the schemas to include in the "allOf" property.
     */
    fun allOf(block: SchemaListBuilder.() -> Unit) { props["allOf"] = SchemaListBuilder().apply(block).build() }
    /**
     * Adds an "anyOf" property, containing a list of schemas, to the props map.
     *
     * @param schemas A variable number of DataMap instances representing the schema definitions
     *                to be included in the "anyOf" list.
     */
    fun anyOf(vararg schemas: DataMap) { props["anyOf"] = schemas.toList() }
    /**
     * Defines an "anyOf" composition constraint for the schema. The schema will be valid if at least one of
     * the subschemas specified within the builder is valid.
     *
     * @param block A lambda used to define subschemas. The receiver of the lambda is a `SchemaListBuilder`, which
     *              allows defining multiple subschemas that will be included in the "anyOf" composition.
     */
    fun anyOf(block: SchemaListBuilder.() -> Unit) { props["anyOf"] = SchemaListBuilder().apply(block).build() }
    /**
     * Updates the `props` map by associating the key "oneOf" with a list of provided schemas.
     *
     * @param schemas Vararg parameter representing one or more `DataMap` objects to be set under the "oneOf" key.
     */
    fun oneOf(vararg schemas: DataMap) { props["oneOf"] = schemas.toList() }
    /**
     * Defines a schema property "oneOf" which is set by applying the provided block
     * to an instance of SchemaListBuilder.
     *
     * @param block A lambda with SchemaListBuilder as the receiver, allowing the
     *              configuration of the "oneOf" property content.
     */
    fun oneOf(block: SchemaListBuilder.() -> Unit) { props["oneOf"] = SchemaListBuilder().apply(block).build() }
    /**
     * Adds a "not" constraint to the schema by applying the given configuration block.
     *
     * @param block A lambda function used to configure the SchemaBuilder for the "not" constraint.
     */
    fun not(block: ReceiverConsumer<SchemaBuilder>) { props["not"] = SchemaBuilder().apply(block).build() }

    /**
     * Defines a conditional schema using the "if" keyword in JSON Schema. The provided block allows
     * customization of the schema by using the fluent DSL provided by the `SchemaBuilder` class.
     *
     * @param block A lambda with `SchemaBuilder` as the receiver, enabling the construction of a conditional schema.
     */
// ── Conditional ──
    fun ifSchema(block: ReceiverConsumer<SchemaBuilder>) { props["if"] = SchemaBuilder().apply(block).build() }
    /**
     * Assigns a schema to the "then" property after applying the provided consumer block to a new SchemaBuilder instance.
     *
     * @param block A lambda with a receiver of type SchemaBuilder that allows configuration of the schema.
     */
    fun then(block: ReceiverConsumer<SchemaBuilder>) { props["then"] = SchemaBuilder().apply(block).build() }
    /**
     * Defines an "else" schema for conditional JSON Schema validation. 
     * The "else" schema is applied when the conditions defined in the "if" schema do not match the input.
     *
     * @param block A lambda receiving a `SchemaBuilder` instance, to define the schema to use in the "else" case.
     */
    fun elseSchema(block: ReceiverConsumer<SchemaBuilder>) { props["else"] = SchemaBuilder().apply(block).build() }

    // ── Definitions ──
    /**
     * Configures and builds a set of properties specific to 'defs'.
     *
     * @param block A lambda with receiver providing a `PropertiesBuilder` to define the properties.
     * @since 3.3.0
     */
    fun defs(block: ReceiverConsumer<PropertiesBuilder>) {
        props[$$"$defs"] = PropertiesBuilder().apply(block).build()
    }

    // ── Raw extension ──
    /**
     * Adds or updates a key-value pair in the `props` collection.
     *
     * @param key The key associated with the value to be added or updated.
     * @param value The value to associate with the given key. If null, the key is set with a null value.
     * @since 3.3.0
     */
    fun raw(key: String, value: Any?) { props[key] = value }

    /**
     * Builds and returns a DataMap instance based on the current state of properties.
     *
     * @return a DataMap containing the key-value pairs from the properties.
     * @since 3.3.0
     */
    fun build(): DataMap = props.toMap()

    /**
     * Converts the data structure to its JSON string representation.
     *
     * @param indent The number of spaces to use for indentation in the JSON output. Defaults to 2.
     * @return The JSON string representation of the data structure.
     * @since 3.3.0
     */
    fun toJson(indent: Int = 2) = mapToJson(build(), indent, 0)
}

/**
 * A builder class for constructing property schemas in a JSON schema.
 * This class allows defining and associating schema definitions with property names
 * and provides shorthand methods to define common data types or structure types.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@JsonSchemaDslMarker
class PropertiesBuilder {
    /**
     * A map that associates property names with their corresponding `DataMap` schema representations.
     *
     * This map acts as an internal storage for schemas defined within the `PropertiesBuilder` class.
     * It enables the creation and management of schema definitions for various data types, allowing properties
     * to be mapped to their respective schema configurations.
     *
     * Schemas can be added to this map using the `String.to()` infix functions, which support both direct
     * `DataMap` assignments and schema construction with a `SchemaBuilder`. The content of this map
     * can be finalized and retrieved using the `build()` function.
     * @since 3.3.0
     */
    private val schemas = linkedMapOf<String, DataMap>()

    /**
     * Associates the given schema with the current string key in the schemas map.
     *
     * @param schema The DataMap to associate with the current string.
     * @since 3.3.0
     */
    infix fun String.to(schema: DataMap) { schemas[this] = schema }
    /**
     * Maps the current string to a schema built using the provided block.
     *
     * @param block A lambda function that takes a ReceiverConsumer of SchemaBuilder,
     *              allowing the schema to be configured and built.
     * @since 3.3.0
     */
    infix fun String.to(block: ReceiverConsumer<SchemaBuilder>) { schemas[this] = SchemaBuilder().apply(block).build() }

    // ── Shorthand type constructors ──
    /**
     * Creates a schema with the type "string" and applies additional configurations
     * using the provided block of code, if specified.
     *
     * @param block an optional lambda with receiver that configures the schema
     *              via the SchemaBuilder instance.
     * @return a DataMap representing the constructed schema.
     * @since 3.3.0
     */
    fun string(block: ReceiverConsumer<SchemaBuilder> = {}): DataMap =
        SchemaBuilder().apply { type("string"); block() }.build()

    /**
     * Constructs a schema definition for an integer type.
     *
     * @param block a lambda to customize the schema configuration using a [SchemaBuilder].
     * @return a [DataMap] representing the integer type schema.
     * @since 3.3.0
     */
    fun integer(block: ReceiverConsumer<SchemaBuilder> = {}): DataMap =
        SchemaBuilder().apply { type("integer"); block() }.build()

    /**
     * Creates a schema definition for the "number" type and applies the provided configuration.
     *
     * @param block A lambda to configure the schema for the "number" type. The lambda receives a [SchemaBuilder]
     *              instance, allowing further customization of the schema.
     * @return A [DataMap] representing the schema definition for the "number" type.
     * @since 3.3.0
     */
    fun number(block: ReceiverConsumer<SchemaBuilder> = {}): DataMap =
        SchemaBuilder().apply { type("number"); block() }.build()

    /**
     * Constructs a schema with type "boolean" and applies the given configuration block.
     *
     * @param block A receiver consumer for configuring the `SchemaBuilder` instance, defaulting to an empty block.
     * @return A `DataMap` representing the constructed boolean type schema.
     * @since 3.3.0
     */
    fun boolean(block: ReceiverConsumer<SchemaBuilder> = {}): DataMap =
        SchemaBuilder().apply { type("boolean"); block() }.build()

    /**
     * Constructs a schema of type "object" and allows further customization through a builder block.
     *
     * @param block A lambda with a receiver of type `SchemaBuilder` used to define additional properties or behaviors of the object schema.
     * @return A `DataMap` representing the constructed object schema.
     * @since 3.3.0
     */
    fun obj(block: ReceiverConsumer<SchemaBuilder> = {}): DataMap =
        SchemaBuilder().apply { type("object"); block() }.build()

    /**
     * Creates a JSON Schema definition for an array type.
     *
     * @param block A lambda with receiver that allows for additional customization of the schema.
     * @return A [DataMap] containing the schema definition for the array type.
     * @since 3.3.0
     */
    fun array(block: ReceiverConsumer<SchemaBuilder> = {}): DataMap =
        SchemaBuilder().apply { type("array"); block() }.build()

    /**
     * Creates a `DataMap` representation of an enum with the specified values.
     *
     * @param values The possible values of the enum. These may include any type of values.
     * @return A `DataMap` containing a single key-value pair where the key is "enum" and the value
     *         is a list of the provided values.
     * @since 3.3.0
     */
    fun enum(vararg values: Any?): DataMap = ("enum" to values.toList()).asSingleMap()

    /**
     * Creates a `$ref` reference in the JSON Schema and maps it to the specified URI.
     *
     * @param uri The URI of the schema to reference.
     * @return A map containing a single key-value pair where the key is `$ref` and the value is the provided URI.
     * @since 3.3.0
     */
    fun ref(uri: String): DataMap = ($$"$ref" to uri).asSingleMap()

    /**
     * Builds and returns a map representation of the current schemas.
     *
     * @return A map where keys are strings and values are instances of DataMap,
     *         representing the schemas.
     * @since 3.3.0
     */
    fun build(): Map<String, DataMap> = schemas.toMap()
}

/**
 * A builder class for constructing a list of schemas represented as `DataMap` objects.
 * This class is part of the JSON schema DSL, allowing users to define and collect schemas
 * in a fluent and declarative manner.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@JsonSchemaDslMarker
class SchemaListBuilder {
    /**
     * Internal mutable list utilized for managing a collection of `DataMap` elements.
     *
     * This property initializes to an empty mutable list and serves as the underlying container
     * for storing schema objects within the `SchemaListBuilder` context.
     *
     * It acts as the central storage mechanism for schema-building operations, including
     * adding new schemas and combining existing `DataMap` instances into the list.
     * @since 3.3.0
     */
    private val schemas = emptyMList<DataMap>()

    /**
     * Adds a schema to the schema list being built by processing the provided block.
     *
     * @param block A lambda with a receiver of type SchemaBuilder, used to define and configure a schema.
     * @since 3.3.0
     */
    fun schema(block: ReceiverConsumer<SchemaBuilder>) { schemas += SchemaBuilder().apply(block).build() }

    /**
     * Overloads the unary plus operator for the DataMap class.
     * This operator adds the current instance of DataMap to a collection of schemas.
     *
     * This method modifies the `schemas` collection by appending the provided DataMap instance.
     * @since 3.3.0
     */
    operator fun DataMap.unaryPlus() { schemas += this }

    /**
     * Builds and returns a list of DataMap objects based on the current state of schemas.
     *
     * @return A list containing DataMap objects created from the schemas collection.
     * @since 3.3.0
     */
    fun build(): List<DataMap> = schemas.toList()
}

/**
 * A builder class to construct dependent-required relationships in a JSON schema.
 *
 * This class facilitates defining dependencies between fields in a JSON schema,
 * where one field's presence necessitates the presence of other specified fields.
 * @since 3.3.0
 */
@JsonSchemaDslMarker
class DependentRequiredBuilder {
    /**
     * A mapping of fields to their respective lists of required fields.
     *
     * The keys in this map represent field names, and their corresponding values
     * specify the list of fields that are required when the key field is present.
     * @since 3.3.0
     */
    private val deps = linkedMapOf<String, List<String>>()

    /**
     * Specifies that the current string (representing a field) requires the provided list of fields
     * to be present as dependencies.
     *
     * This infix function allows defining dependencies in a declarative manner within the DSL.
     *
     * @param fields The list of fields that are dependencies for the current field.
     * @since 3.3.0
     */
    infix fun String.requires(fields: List<String>) { deps[this] = fields }
    /**
     * Adds a dependency mapping between the specified field and the required fields.
     *
     * @param field the name of the field that declares dependencies.
     * @param required the fields that are required by the specified field.
     * @since 3.3.0
     */
    fun dep(field: String, vararg required: String) { deps[field] = required.toList() }

    /**
     * Builds and returns a MultiMap containing the current dependencies.
     *
     * @return A MultiMap instance representing the mapped dependencies.
     * @since 3.3.0
     */
    fun build(): MultiMap<String, String> = deps.toMap()
}

// --- TOP-LEVEL SHORTHAND TYPE BUILDERS (FOR USE INSIDE SCHEMABUILDER) ---

/**
 * Defines a schema builder for a string type and applies the provided block to configure it further.
 *
 * @param block A lambda function used to define additional configurations on the schema builder. Defaults to an empty block.
 * @return A DataMap representing the constructed schema with the "string" type applied.
 * @since 3.3.0
 */
fun SchemaBuilder.string(block: ReceiverConsumer<SchemaBuilder> = {}): DataMap =
    SchemaBuilder().apply { type("string"); block() }.build()

/**
 * Defines an integer type in the schema builder.
 *
 * @param block A lambda function providing additional configurations
 *              to the schema builder.
 * @return A DataMap representing the constructed schema.
 * @since 3.3.0
 */
fun SchemaBuilder.integer(block: ReceiverConsumer<SchemaBuilder> = {}): DataMap =
    SchemaBuilder().apply { type("integer"); block() }.build()

/**
 * Defines a schema of type "number" and applies an optional receiver block
 * to configure additional properties or behaviors for the schema.
 *
 * @param block An optional lambda function used to configure the schema
 *              further. The lambda has a receiver of type `SchemaBuilder`.
 * @return A `DataMap` representing the finalized schema with the applied configurations.
 * @since 3.3.0
 */
fun SchemaBuilder.number(block: ReceiverConsumer<SchemaBuilder> = {}): DataMap =
    SchemaBuilder().apply { type("number"); block() }.build()

/**
 * Constructs a DataMap object defining a boolean type schema.
 *
 * @return A DataMap representing a schema with the "boolean" type.
 * @since 3.3.0
 */
fun SchemaBuilder.boolean(): DataMap =
    SchemaBuilder().apply { type("boolean") }.build()

/**
 * Adds an object type to the schema by applying the specified consumer block to this SchemaBuilder.
 *
 * @param block A consumer block that allows for customizing the SchemaBuilder for the object type. Defaults to an empty block.
 * @return A DataMap representing the constructed object schema.
 * @since 3.3.0
 */
fun SchemaBuilder.obj(block: ReceiverConsumer<SchemaBuilder> = {}): DataMap =
    SchemaBuilder().apply { type("object"); block() }.build()

/**
 * Configures a schema to represent an array type and allows additional schema modifications
 * through the provided [block].
 *
 * @param block A lambda with a [SchemaBuilder] receiver that can be used to define or configure
 *              additional properties for the array schema. Defaults to an empty block.
 * @return A [DataMap] representing the configured array schema.
 * @since 3.3.0
 */
fun SchemaBuilder.array(block: ReceiverConsumer<SchemaBuilder> = {}): DataMap =
    SchemaBuilder().apply { type("array"); block() }.build()


// --- ENTRY POINT ---

/**
 * Constructs and returns a JSON schema using the provided configuration block.
 *
 * @param block A lambda function that receives a SchemaBuilder instance to configure the JSON schema.
 * @return The configured SchemaBuilder instance representing the JSON schema.
 * @since 3.3.0
 */
fun buildJsonSchema(block: ReceiverConsumer<SchemaBuilder>) =
    SchemaBuilder().apply {
        schema()
        block()
    }.toJson()

/**
 * Initializes a JSON schema by applying a provided block to a SchemaBuilder instance.
 *
 * @param block A lambda function that operates on a SchemaBuilder to define the schema structure.
 * @return A configured SchemaBuilder instance representing the constructed JSON schema.
 * @since 3.6.4
 */
fun initJsonSchema(block: ReceiverConsumer<SchemaBuilder>): SchemaBuilder =
    SchemaBuilder().apply {
        schema()
        block()
    }

// --- JSON SERIALIZATION (ZERO DEPENDENCIES) ---

/**
 * Converts the given value to a JSON representation.
 *
 * @param value The value to be converted. Can be a primitive type, string, list, map, or null.
 * @param indent The number of spaces used for indentation. A value of 0 disables pretty-printing.
 * @param depth The current depth level in the JSON hierarchy, used to calculate indentation.
 * @return A Json object representing the JSON serialization of the input value.
 * @since 3.3.0
 */
internal fun mapToJson(value: Any?, indent: Int, depth: Int): Json {
    val pad = if (indent > 0) " ".repeat(indent * (depth + 1)) else ""
    val padClose = if (indent > 0) " ".repeat(indent * depth) else ""
    val nl = if (indent > 0) "\n" else ""
    val sep = if (indent > 0) ", " else ","

    return Json(
        when (value) {
            null -> "null"
            is Boolean -> value.toString()
            is Number -> value.toString()
            is String -> "\"${escapeJson(value)}\""
            is Map<*, *> -> {
                if (value.isEmpty()) "{}"
                else value.entries.joinToString(",$nl", "{$nl", "$nl$padClose}") { [k, v] ->
                    "$pad\"${escapeJson(k.toString())}\": ${mapToJson(v, indent, depth + 1)}"
                }
            }

            is List<*> -> {
                if (value.isEmpty()) "[]"
                else if (value.all { it is String || it is Number || it is Boolean || it.isNull() }) {
                    // Compact for simple arrays
                    value.joinToString(sep, "[", "]") { mapToJson(it, 0, 0) }
                } else {
                    value.joinToString(",$nl", "[$nl", "$nl$padClose]") { "$pad${mapToJson(it, indent, depth + 1)}" }
                }
            }

            else -> "\"${escapeJson(value.toString())}\""
        }
    )
}

/**
 * Escapes special characters in a JSON string to ensure it is properly formatted.
 *
 * This method replaces special characters in the input string with their escaped equivalents:
 * - Backslashes (`\`) are escaped as `\\`.
 * - Double quotes (`"`) are escaped as `\"`.
 * - Newline characters (`\n`) are escaped as `\\n`.
 * - Carriage return characters (`\r`) are escaped as `\\r`.
 * - Tab characters (`\t`) are escaped as `\\t`.
 *
 * @param s The input string to be escaped.
 * @return A string where special characters have been replaced with their escaped representations.
 * @since 3.3.0
 */
internal fun escapeJson(s: String): String = s
    .replace("\\", "\\\\")
    .replace("\"", "\\\"")
    .replace("\n", "\\n")
    .replace("\r", "\\r")
    .replace("\t", "\\t")