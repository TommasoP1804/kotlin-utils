/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")
@file:Since("3.3.0")

package dev.tommasop1804.kutils.dsl.uischema

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.Json
import dev.tommasop1804.kutils.dsl.jsonschema.*

@DslMarker
annotation class UiSchemaDslMarker

// --- CORE ELEMENT MODEL ---

/**
 * Represents a UI element in the schema definition DSL.
 *
 * This sealed class serves as the base for all UI element types, providing
 * a common interface for constructing and serializing schema definitions.
 * Each specific type of UI element must implement the `build` method to produce
 * its corresponding representation in the schema.
 *
 * Classes inheriting from this base can define additional behavior or configurations
 * specific to their role within the UI schema.
 *
 * Marked with the `@UiSchemaDsl` annotation to enable usage within a UI schema DSL.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@UiSchemaDslMarker
sealed class UiElement {
    /**
     * Constructs and returns a DataMap instance configured with the current state of the builder.
     *
     * @return a DataMap instance containing the data from the builder.
     * @since 3.3.0
     */
    abstract fun build(): DataMap
}

/**
 * Represents a UI control element that can be configured with a label, options, and rules.
 *
 * A `ControlElement` is used to define the scope of a UI control and allows customization
 * for its label visibility, configuration options, and conditional behavior through rules.
 *
 * @constructor Initializes a `ControlElement` with a specific scope.
 * @param scope The scope of this control element, typically used to reference a specific schema path.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
class ControlElement(private val scope: String) : UiElement() {
    /**
     * Represents the label associated with a control element.
     *
     * The label can hold either a `String` value to specify the display text
     * or a `Boolean` value where `false` indicates that the label is hidden.
     *
     * This property is used during the building process to determine whether
     * a label is displayed and, if so, what text it contains.
     *
     * Acceptable values:
     * - `String`: Specifies the text to be displayed as the label.
     * - `Boolean` (`false`): Indicates that the label is hidden.
     *
     * The property is nullable, and if not explicitly set, no label information
     * will be included in the resulting data map.
     * @since 3.3.0
     */
    private var label: Any? = null  // String or Boolean (false = hidden)
    /**
     * A mutable map used to store configuration options for the `ControlElement`.
     *
     * This map holds key-value pairs where keys are of type `String` and values are of type `Any?`.
     * The options can be extended using the `options` function, which allows a block of configuration
     * to be added dynamically via an `OptionsBuilder`.
     *
     * Typical usage of this property involves defining custom options to control the behavior or
     * appearance of the `ControlElement`. The options are serialized into the resulting `DataMap`
     * during the `build` process.
     *
     * Key details:
     * - The map is initialized as an empty `LinkedHashMap`, ensuring that insert order is preserved.
     * - Modifications can include setting various details using the DSL-like utility methods offered
     *   by the linked `OptionsBuilder` class.
     * @since 3.3.0
     */
    private val options = linkedMapOf<String, Any?>()
    /**
     * Represents the rule configuration for the current UI element.
     *
     * This property is used to define rules that dictate the behavior or visibility
     * of the element based on specific conditions. Each rule includes details such
     * as the effect (e.g., SHOW, HIDE, ENABLE, DISABLE) and an optional condition
     * specifying when the rule should apply.
     *
     * The `rule` can be configured using the `rule` function, where a `RuleBuilder`
     * is used to construct the desired behavior.
     *
     * @since 3.3.0
     */
    private var rule: DataMap? = null

    /**
     * Sets the label for the control element.
     *
     * @param text the label text to be assigned. A non-null string value will set the label; a special value like `false` may indicate hiding the label.
     * @since 3.3.0
     */
    fun label(text: String) { label = text }
    /**
     * Hides the label associated with the current control element.
     *
     * This method sets the `label` property of the control element to `false`, effectively marking
     * the label as hidden. Once this method is invoked, the label will not be displayed in the UI.
     * @since 3.3.0
     */
    fun hideLabel() { label = false }

    /**
     * Configures and applies options using the provided [ReceiverConsumer] block.
     *
     * @param block A lambda function that receives an [OptionsBuilder] instance
     *              for configuring and applying options.
     * @since 3.3.0
     */
    fun options(block: ReceiverConsumer<OptionsBuilder>) {
        options.putAll(OptionsBuilder().apply(block).build())
    }

    /**
     * Configures a rule for the current element using the provided block.
     *
     * The rule defines the behavior or condition under which the current element
     * operates, such as visibility or state changes. The block consumes a
     * [RuleBuilder] instance to customize the rule's effect and conditions.
     *
     * @param block a lambda receiving a [RuleBuilder] instance for defining the rule.
     * @since 3.3.0
     */
    fun rule(block: ReceiverConsumer<RuleBuilder>) {
        rule = RuleBuilder().apply(block).build()
    }

    /**
     * Builds a DataMap containing key-value pairs based on the properties of the object.
     * The resulting map includes a mandatory "type" field set to "Control" and other optional fields such as "scope",
     * "label", "options", and "rule", if they are present and valid.
     *
     * @return A DataMap with the constructed key-value pairs.
     * @since 3.3.0
     */
    override fun build(): DataMap = buildMap {
        put("type", "Control")
        put("scope", scope)
        if (label.isNotNull()) put("label", label)
        if (options.isNotEmpty()) put("options", options.toMap())
        if (rule.isNotNull()) put("rule", rule)
    }
}

/**
 * Represents a layout element that is a part of a user interface schema.
 *
 * This class provides methods to configure layouts, group elements, and define
 * various UI components such as controls, labels, and categorizations.
 *
 * @property type The type of layout represented by this element.
 * @constructor Creates a new LayoutElement instance for the specified type.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
class LayoutElement(private val type: String) : UiElement() {
    /**
     * Holds a mutable list of UI elements to be used for building layouts and other UI structures.
     *
     * This list is updated dynamically as various UI elements are added through functions
     * such as `control`, `horizontalLayout`, `verticalLayout`, `group`, `label`, and others
     * defined in the containing class. These elements contribute to the hierarchical structure
     * of the UI schema.
     * @since 3.3.0
     */
    private val elements = emptyMList<UiElement>()
    /**
     * Represents the label associated with the layout element.
     *
     * This property is used to define an optional label text
     * for the layout element. If not set, the layout element
     * may render without a visible label.
     *
     * The label can be updated using the `label` function
     * within the containing class.
     * @since 3.3.0
     */
    private var label: String? = null
    /**
     * A mutable, ordered map for storing configuration options as key-value pairs.
     *
     * This property holds additional attributes or settings that customize the behavior
     * or appearance of the `LayoutElement`. The options can be populated using the `options` function,
     * which allows structured customization through the `OptionsBuilder` DSL.
     *
     * The map preserves the insertion order of the options, making it suitable for scenarios
     * where the order of configuration settings is significant.
     *
     * Key characteristics:
     * - Keys are of type `String` and represent the names of the options.
     * - Values are of type `Any?`, allowing for flexibility in the type of data stored.
     *
     * This property is used during the `build` process of the `LayoutElement` to include
     * the specified options in the generated data map if any are provided.
     * @since 3.3.0
     */
    private val options = linkedMapOf<String, Any?>()
    /**
     * Represents a rule configuration map for a layout element.
     *
     * This variable stores a data map that contains the rule definition applicable
     * to the layout element. Rules are generally used to manage visibility or interactivity
     * of UI elements based on specific conditions or effects.
     *
     * The rule can be configured using a `RuleBuilder` block or set directly as a data map
     * via the associated `rule` function. Its primary purpose is to facilitate dynamic
     * behavior within the layout structure.
     * @since 3.3.0
     */
    private var rule: DataMap? = null

    /**
     * Sets the label of the current layout element to the specified text.
     *
     * @param text The label text to be assigned to the layout element.
     * @since 3.3.0
     */
    fun label(text: String) { label = text }

    /**
     * Adds a control element with the specified scope to the layout and allows customization via a block.
     *
     * This method creates a new `ControlElement` with the provided scope, applies the given customization block to it,
     * and appends it to the current list of elements.
     *
     * @param scope The data-binding scope for this control element, determining the model property it is bound to.
     * @param block A lambda to configure additional properties or rules for the created `ControlElement`. Defaults to an empty block if not provided.
     * @since 3.3.0
     */
    fun control(scope: String, block: ReceiverConsumer<ControlElement> = {}) {
        elements += ControlElement(scope).apply(block)
    }
    
    /**
     * Adds a horizontal layout container to the current layout structure.
     *
     * This method initializes a new horizontal layout and allows adding child elements or
     * configuring the layout using the provided consumer block.
     *
     * @param block A lambda receiving a `ReceiverConsumer<LayoutElement>`, allowing the
     *              configuration of child elements within the horizontal layout.
     * @since 3.3.0
     */
    fun horizontalLayout(block: ReceiverConsumer<LayoutElement>) {
        elements += LayoutElement("HorizontalLayout").apply(block)
    }

    /**
     * Defines a vertical layout within a user interface schema.
     *
     * This function allows the creation of a vertical layout by adding layout-specific elements
     * through a provided block of configuration logic. The block specifies how the vertical layout
     * is structured and which child elements it contains.
     *
     * @param block A lambda function with a receiver of type `ReceiverConsumer<LayoutElement>`
     *              that allows configuration of the vertical layout's elements.
     * @since 3.3.0
     */
    fun verticalLayout(block: ReceiverConsumer<LayoutElement>) {
        elements += LayoutElement("VerticalLayout").apply(block)
    }

    /**
     * Creates a new group element with the specified label and configuration.
     * The group element is added to the layout elements list.
     *
     * @param label The label for the group element.
     * @param block A lambda function to configure the contents of the group element.
     * @since 3.3.0
     */
    fun group(label: String, block: ReceiverConsumer<LayoutElement>) {
        elements += LayoutElement("Group").apply {
            this.label(label)
            block()
        }
    }

    /**
     * Adds a new categorization definition to the current layout by applying the provided
     * configuration block. A categorization allows grouping related UI elements into
     * categories for better organization and navigation.
     *
     * @param block A lambda with receiver that provides configuration for the categorization element.
     * @since 3.3.0
     */
    fun categorization(block: ReceiverConsumer<CategorizationElement>) {
        elements += CategorizationElement().apply(block)
    }

    /**
     * Configures and adds a `LabelElement` to the current structure.
     *
     * The provided block allows applying custom configurations to the `LabelElement`.
     *
     * @param block A lambda that accepts a `ReceiverConsumer` of `LabelElement` for configuring the label.
     * @since 3.3.0
     */
    fun label(block: ReceiverConsumer<LabelElement>) {
        elements += LabelElement().apply(block)
    }

    /**
     * Configures and updates options using the provided builder block.
     *
     * @since 3.3.0
     * @param block a lambda with a receiver of type `OptionsBuilder` used to customize and build options.
     */
    fun options(block: ReceiverConsumer<OptionsBuilder>) {
        options.putAll(OptionsBuilder().apply(block).build())
    }

    /**
     * Configures a rule for the current layout element using the provided configuration block.
     * The rule defines conditional behavior such as visibility or enablement for the element.
     *
     * @param block A configuration block that receives an instance of [RuleBuilder]
     * allowing customization of the rule's effect and condition.
     * @since 3.3.0
     */
    fun rule(block: ReceiverConsumer<RuleBuilder>) {
        rule = RuleBuilder().apply(block).build()
    }

    /**
     * Adds a new RawElement to the elements collection using the provided DataMap.
     *
     * @param element The DataMap object to be wrapped into a RawElement and added to the collection.
     * @since 3.3.0
     */
    fun raw(element: DataMap) {
        elements += RawElement(element)
    }

    /**
     * Builds a map representation of the current object state.
     *
     * @return A map where keys represent property names and values represent their corresponding data.
     *         Includes "type" and "elements" by default. Optionally includes "label", "options",
     *         and "rule" if they are not null or empty.
     * @since 3.3.0
     */
    override fun build(): DataMap = buildMap {
        put("type", type)
        if (label.isNotNull()) put("label", label)
        put("elements", elements.map { it.build() })
        if (options.isNotEmpty()) put("options", options.toMap())
        if (rule.isNotNull()) put("rule", rule)
    }
}

/**
 * Represents a categorization UI element that organizes UI components into categories.
 *
 * This class allows defining categories and associating layouts with them. Categories are
 * constructed using a label and a configuration block, which specifies their layout.
 * Additional configuration options can also be set using a custom options builder.
 *
 * The resulting categorization element is serialized into a data structure when built,
 * including its type, categorized elements, and any specified options.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
class CategorizationElement : UiElement() {
    /**
     * Holds a mutable list of `CategoryElement` instances that represent the categories
     * within the `CategorizationElement` structure.
     *
     * This list stores the category definitions created through the `category` function. Each
     * `CategoryElement` includes its label and associated layout elements, enabling the dynamic
     * construction of categorized UI elements.
     *
     * Used by the `build` function to generate a structured representation of all categories
     * in a `DataMap`.
     * @since 3.3.0
     */
    private val categories = emptyMList<CategoryElement>()
    /**
     * Holds optional configuration settings for the `CategorizationElement`.
     *
     * This map is used to store key-value pairs where the key is a string
     * representing the option name, and the value can be any type or null.
     * It allows for dynamic customization of the categorization element
     * during the schema building process.
     *
     * The values are populated by invoking the `options` function with a
     * custom configuration block, which utilizes the `OptionsBuilder`
     * to construct and supply options to this map.
     * @since 3.3.0
     */
    private val options = linkedMapOf<String, Any?>()

    /**
     * Adds a category with the specified label and layout block to the categorization element.
     *
     * @param label The label for the category.
     * @param block A lambda that configures the layout elements within the category.
     * @since 3.3.0
     */
    fun category(label: String, block: ReceiverConsumer<LayoutElement>) {
        categories += CategoryElement(label).apply { layout(block) }
    }

    /**
     * Configures options for the current element using the provided block.
     *
     * This function allows defining a set of options that can be passed to the element,
     * by utilizing an `OptionsBuilder` to build key-value pairs or additional nested configurations.
     *
     * @param block A lambda with a receiver of type `OptionsBuilder`. Use this block to specify
     *              key-value pairs or other options to configure the element.
     * @since 3.3.0
     */
    fun options(block: ReceiverConsumer<OptionsBuilder>) {
        options.putAll(OptionsBuilder().apply(block).build())
    }

    /**
     * Constructs and returns a `DataMap` representation of the categorization element.
     *
     * This method generates a structured `DataMap` that contains the type of the element
     * as "Categorization," a collection of child category elements, and additional options
     * if defined.
     *
     * @return a `DataMap` representing the categorization element, including its type,
     *         child elements, and optional configuration.
     * @since 3.3.0
     */
    override fun build(): DataMap = buildMap {
        put("type", "Categorization")
        put("elements", categories.map { it.build() })
        if (options.isNotEmpty()) put("options", options.toMap())
    }
}

/**
 * Represents a category element in the UI schema definition DSL.
 *
 * This class allows for the creation of a categorized structure of UI elements,
 * enabling the organization of related elements under a common label.
 * A `CategoryElement` can include multiple child elements, such as layouts, and
 * provides methods to define the layout and build the serialized representation.
 *
 * @constructor Initializes the `CategoryElement` with the specified label.
 * @param label The text label for the category element.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
class CategoryElement(private val label: String) {
    /**
     * A mutable list for holding instances of `UiElement`.
     *
     * This property is used to manage and organize `UiElement` objects within a `CategoryElement`.
     * It acts as a container for UI schema components, allowing dynamic modifications and
     * the composition of elements for layout and build processes.
     *
     * Typically, elements are added to this list during the execution of layout-related operations
     * or while building the final representation using the `build` method.
     * @since 3.3.0
     */
    private val elements = emptyMList<UiElement>()

    /**
     * Defines a layout configuration within the current context. The layout is of type "VerticalLayout"
     * and allows customization of child elements through the provided receiver block.
     *
     * @param block A receiver function providing the scope to configure the layout element.
     *              Any elements defined within the block are added as children to the layout.
     * @since 3.3.0
     */
    fun layout(block: ReceiverConsumer<LayoutElement>) {
        val layout = LayoutElement("VerticalLayout").apply(block)
        elements += layout
    }

    /**
     * Builds and returns a `DataMap` representation of the `CategoryElement`.
     *
     * The resulting `DataMap` contains the following structure:
     * - `type`: Represents the type of the element, always set to "Category".
     * - `label`: The label of the category.
     * - `elements`: A collection of built elements derived from the nested `UiElement` instances.
     *
     * @return a `DataMap` instance representing this `CategoryElement` and its nested elements.
     * @since 3.3.0
     */
    fun build(): DataMap = buildMap {
        put("type", "Category")
        put("label", label)
        put("elements", elements.map { it.build() })
    }
}

/**
 * Represents a label element within a UI schema.
 *
 * This class is used to define a label that can be associated with
 * other UI elements or used independently for display purposes.
 * The `text` property is used to specify the content of the label.
 *
 * Inherits from the base class `UiElement` and overrides the `build` method
 * to provide its specific representation in the schema.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
class LabelElement : UiElement() {
    /**
     * Represents the textual content of a label element within a UI schema.
     *
     * This property is used to set or retrieve the text displayed by the label.
     * The value is later utilized during the build process to include the
     * label's content in the serialized representation of the UI definition.
     * @since 3.3.0
     */
    private var text: String = ""

    /**
     * Updates the text property with the given value.
     *
     * @param value The new text to set.
     * @since 3.3.0
     */
    fun text(value: String) { text = value }

    /**
     * Constructs and returns a DataMap representing the current state of the LabelElement.
     * The DataMap contains:
     * - "type": A constant identifying the element type as "Label".
     * - "text": The value of the `text` property set for the label.
     *
     * @return a DataMap containing the serialized representation of the LabelElement.
     * @since 3.3.0
     */
    override fun build(): DataMap = mapOf("type" to "Label", "text" to text)
}

/**
 * Represents a raw UI element within the schema definition DSL.
 *
 * RawElement is designed to directly encapsulate a `DataMap` object, bypassing
 * additional constraints or configurations of more specialized UI elements. This allows
 * manual customization of schema definitions by providing a raw data structure.
 *
 * This class extends `UiElement` and implements the `build` method to return
 * its internal `DataMap` as is, enabling seamless integration with the DSL.
 *
 * Key use cases include injecting predefined configurations or supporting advanced
 * scenarios requiring low-level customization of UI components.
 *
 * @constructor Initializes a new instance of `RawElement` with the provided `DataMap`.
 * @param data The `DataMap` encapsulated within this raw UI element.
 * @since 3.3.0
 */
class RawElement(private val data: DataMap) : UiElement() {
    /**
     * Constructs and returns the DataMap representing the current state of this RawElement.
     *
     * @return the DataMap instance encapsulating the data of this RawElement.
     * @since 3.3.0
     */
    override fun build(): DataMap = data
}

// --- OPTIONS ---

/**
 * A builder class used for constructing options maps for UI schema elements.
 * It provides DSL-style methods for defining key-value pairs and nested layout structures.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@UiSchemaDslMarker
class OptionsBuilder {
    /**
     * A mutable map used internally to store key-value pairs representing configuration options.
     * Keys are strings, and values can be of any nullable type. This map is primarily used
     * for building structured data in a DSL context.
     * @since 3.3.0
     */
    private val opts = linkedMapOf<String, Any?>()

    /**
     * Adds a key-value pair to the `opts` map using the calling string as the key.
     *
     * @param value The value to associate with the calling string as the key. May be null.
     * @since 3.3.0
     */
    infix fun String.to(value: Any?) { opts[this] = value }

    /**
     * Configures a "detail" section within the UI schema with the specified layout.
     *
     * @param block a lambda that defines the structure and content of the detail layout
     * @since 3.3.0
     */
    fun detail(block: ReceiverConsumer<LayoutElement>) {
        opts["detail"] = LayoutElement("VerticalLayout").apply(block).build()
    }

    /**
     * Constructs and returns a DataMap containing key-value pairs from the current options.
     *
     * @return a DataMap representing the configured options.
     * @since 3.3.0
     */
    fun build(): DataMap = opts.toMap()
}

// --- RULE ---

/**
 * A DSL builder class for configuring rules related to UI schema elements.
 *
 * The rules define behavior such as visibility or state changes for UI elements.
 * The `RuleBuilder` allows for setting an effect (e.g., "SHOW", "HIDE", "ENABLE",
 * "DISABLE") and specifying conditions under which the effect is applied.
 * The result is a `DataMap` containing the effect and an optional condition.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@UiSchemaDslMarker
class RuleBuilder {
    /**
     * Represents the current visual or functional effect for a UI rule.
     *
     * The `effect` is used to determine the behavior or visibility state of an element in the UI.
     * It can be dynamically updated to one of the predefined states: "SHOW", "HIDE", "ENABLE", or "DISABLE".
     *
     * Default value: "SHOW"
     * @since 3.3.0
     */
    private var effect: String = "SHOW"
    /**
     * Represents a condition associated with the rule being built.
     *
     * This variable holds a map-like structure (`DataMap`) that defines specific conditions
     * applied to the schema configuration. It may be set through functions that define
     * the condition parameters (`scope`, `schemaKey`, and `value`) using appropriate
     * configuration methods. If the condition is not initialized, it remains null.
     *
     * In the context of a `RuleBuilder`, this variable is utilized during the construction
     * of rules to apply conditional logic.
     * @since 3.3.0
     */
    private var condition: DataMap? = null

    /**
     * Sets the effect of the rule to "SHOW".
     *
     * This function modifies the internal state of the rule to indicate that the associated
     * UI element or operation should be shown.
     * @since 3.3.0
     */
    fun show() { effect = "SHOW" }
    /**
     * Sets the effect to "HIDE", indicating that the associated UI element should be hidden.
     *
     * This method modifies the internal state by updating the `effect` property
     * with the value "HIDE".
     * @since 3.3.0
     */
    fun hide() { effect = "HIDE" }
    /**
     * Sets the effect of the rule to "ENABLE".
     *
     * This function is used to mark the rule as enabling a particular behavior or state.
     * It modifies the rule's internal state by setting the `effect` property to "ENABLE".
     * @since 3.3.0
     */
    fun enable() { effect = "ENABLE" }
    /**
     * Sets the effect to "DISABLE" for this rule configuration.
     *
     * This method modifies the internal state of the `RuleBuilder` by changing the
     * `effect` to indicate that the associated UI element should be disabled.
     * @since 3.3.0
     */
    fun disable() { effect = "DISABLE" }

    /**
     * Defines a condition for a rule by associating a scope with a schema key and its corresponding value.
     *
     * @param scope The scope within which the condition will be evaluated.
     * @param schemaKey The key within the schema that the condition applies to.
     * @param value The value associated with the schema key for this condition.
     * @since 3.3.0
     */
    fun condition(scope: String, schemaKey: String, value: Any?) {
        condition = mapOf(
            "scope" to scope,
            "schema" to (schemaKey to value).asSingleMap()
        )
    }

    /**
     * Sets a condition in the form of a map with the specified scope and schema.
     *
     * @param scope A string that defines the scope for the condition.
     * @param schema A map containing the schema definition for the condition.
     * @since 3.3.0
     */
    fun condition(scope: String, schema: DataMap) {
        condition = mapOf("scope" to scope, "schema" to schema)
    }

    /**
     * Constructs and returns a `DataMap` object populated with specific key-value pairs.
     *
     * The resulting map will always contain the key "effect" with its corresponding value.
     * Optionally, it includes "condition" if the `condition` property is not null.
     *
     * @return A `DataMap` containing the populated key-value pairs.
     * @since 3.3.0
     */
    fun build(): DataMap = buildMap {
        put("effect", effect)
        if (condition.isNotNull()) put("condition", condition)
    }
}

// --- ROOT BUILDER ---

/**
 * A builder class for constructing UI schema definitions using a domain-specific language (DSL).
 *
 * This class provides methods for defining various UI elements and layouts, including vertical
 * and horizontal layouts, categorizations, controls, and rules. The constructed schema can be
 * serialized into a data map or converted to JSON.
 *
 * The class is annotated with `@UiSchemaDsl` to enable DSL-style usage.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@UiSchemaDslMarker
class UiSchemaBuilder {
    /**
     * Represents the root UI element of the schema being constructed.
     *
     * This variable holds the top-level `UiElement` instance within the `UiSchemaBuilder` context.
     * The value of `root` is dynamically assigned when defining the layout or structure
     * of the UI using the DSL methods (e.g., `verticalLayout`, `horizontalLayout`, `categorization`, `control`).
     *
     * If no top-level UI element has been defined, the value of this variable will remain `null`.
     * It is later utilized during the `build` process to generate the resulting schema representation.
     * @since 3.3.0
     */
    private var root: UiElement? = null
    /**
     * Holds global rules for the UI schema as a mutable list of key-value pairs,
     * where the key is a string representing the rule's scope and the value
     * is a `DataMap` that defines the rule's configuration.
     *
     * This property is used to collect and maintain a list of rules added
     * using the `rule` function within the `UiSchemaBuilder` class.
     * These rules are subsequently included in the final schema when
     * the `build` function is invoked. If no rules are added, the list remains empty.
     * @since 3.3.0
     */
    private val globalRules = emptyMList<Pair<String, DataMap>>()

    /**
     * Configures a vertical layout structure for UI schema elements.
     *
     * @param block A lambda function with a receiver of type LayoutElement that allows the caller to define
     *              the components and configurations within the vertical layout.
     * @since 3.3.0
     */
    fun verticalLayout(block: ReceiverConsumer<LayoutElement>) {
        root = LayoutElement("VerticalLayout").apply(block)
    }

    /**
     * Defines a horizontal layout structure within the user interface schema.
     *
     * @param block A lambda expression that configures the horizontal layout by
     *              applying modifications to a [LayoutElement] instance.
     * @since 3.3.0
     */
    fun horizontalLayout(block: ReceiverConsumer<LayoutElement>) {
        root = LayoutElement("HorizontalLayout").apply(block)
    }

    /**
     * Defines a categorization element in the UI schema. A categorization groups multiple categories,
     * each of which can contain specific layouts or controls.
     *
     * The provided lambda block allows configuring the categorization element and its child categories.
     *
     * @param block A lambda that configures the `CategorizationElement` by defining its child categories
     * and setting optional properties.
     * @since 3.3.0
     */
    fun categorization(block: ReceiverConsumer<CategorizationElement>) {
        root = CategorizationElement().apply(block)
    }

    /**
     * Configures a control element within the current UI schema context.
     *
     * The control element defines a specific UI component with a given scope and allows further customization
     * through the provided block. The scope is typically used to reference a specific part of the schema.
     *
     * @param scope The unique schema path or reference associated with this control element.
     * @param block An optional lambda for configuring the control element via a `ReceiverConsumer<ControlElement>`.
     * @since 3.3.0
     */
    fun control(scope: String, block: ReceiverConsumer<ControlElement> = {}) {
        root = ControlElement(scope).apply(block)
    }

    /**
     * Adds a rule to the global rules list in the schema.
     *
     * @param scope The scope associated with the rule, typically representing the target component or path.
     * @param block A lambda function that configures rule details using a `RuleBuilder`.
     * @since 3.3.0
     */
    fun rule(scope: String, block: ReceiverConsumer<RuleBuilder>) {
        globalRules += scope to RuleBuilder().apply(block).build()
    }

    /**
     * Builds and returns a DataMap representing the current UI schema configuration.
     * This includes the data from the root element, if defined, as well as any global rules.
     * If no root element is defined, an empty map is returned. If global rules are provided,
     * they are appended to the resulting DataMap under the "rules" key.
     *
     * @return a DataMap containing the constructed UI schema and global rules, if any.
     * @since 3.3.0
     */
    fun build(): DataMap {
        val result = root?.build() ?: emptyMap()
        return if (globalRules.isEmpty()) result
        else result + ("rules" to globalRules.associate { [scope, rule] -> scope to rule })
    }

    /**
     * Converts the current data structure into its JSON representation as a string.
     *
     * @param indent The number of spaces to use for indentation in the resulting JSON string. Defaults to 2.
     * @return A JSON-encoded string representation of the data.
     * @since 3.3.0
     */
    fun toJson(indent: Int = 2) = mapToJson(build(), indent, 0)
}

// --- ENTRY POINT ---

/**
 * Creates and configures a new instance of `UiSchemaBuilder` by applying the given block.
 *
 * The provided block is executed in the context of a `UiSchemaBuilder` instance, allowing
 * the user to define the structure and behavior of a UI schema.
 *
 * @param block A lambda function to define the desired configuration for the `UiSchemaBuilder`.
 *              The lambda operates as a receiver on the `UiSchemaBuilder` instance.
 * @return The configured `UiSchemaBuilder` instance after applying the provided block.
 * @since 3.3.0
 */
fun buildUiSchema(block: UiSchemaBuilder.() -> Unit): Json =
    UiSchemaBuilder().apply(block).toJson(2)

/**
 * Initializes a new UI schema builder and applies the provided block to configure it.
 *
 * This function creates an instance of `UiSchemaBuilder` and applies the given configuration lambda to it.
 * The resulting builder instance can be used to further modify or retrieve the constructed UI schema.
 *
 * @param block A DSL block that defines the configuration for the `UiSchemaBuilder`.
 * @return The configured instance of `UiSchemaBuilder`.
 * @since 3.6.4
 */
fun initUiSchema(block: UiSchemaBuilder.() -> Unit): UiSchemaBuilder =
    UiSchemaBuilder().apply(block)