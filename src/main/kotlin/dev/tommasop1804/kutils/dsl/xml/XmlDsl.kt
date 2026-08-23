/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused", "UnusedReceiverParameter", "FunctionName")
@file:OptIn(Beta::class)

package dev.tommasop1804.kutils.dsl.xml

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*

@DslMarker
annotation class XmlDslMarker

// --- VALUE TYPES ---

/**
 * Represents a node in an XML structure.
 *
 * This sealed interface is the base type for every XML node that can appear as the child
 * of an element — elements, text, CDATA sections and comments.
 *
 * @since 3.9.0
 * @author Tommaso Pastorelli
 */
sealed interface XmlNode {
    /**
     * Converts the current XML node into its string representation.
     *
     * Indentation is applied to nested structures for human-readable formatting.
     *
     * @param indent The number of spaces to use for each level of indentation. Defaults to 2.
     *               If set to 0, no indentation is used and the output is compact/inline.
     * @param depth  The current depth level in the XML tree, used to compute indentation.
     *               Defaults to 0.
     * @return The XML string representation of the node.
     * @since 3.9.0
     */
    fun toXmlString(indent: Int = 2, depth: Int = 0): String
}

/**
 * Represents a textual value inside an XML element.
 *
 * The value is XML-escaped (`<`, `>`, `&`, `"`, `'`) when serialized.
 *
 * @property value The raw text content represented by this node.
 * @since 3.9.0
 */
data class XmlText(val value: String) : XmlNode {
    /**
     * Returns the XML-escaped text representation of this node.
     *
     * @since 3.9.0
     */
    override fun toXmlString(indent: Int, depth: Int): String = escapeXml(value)
}

/**
 * Represents a CDATA section inside an XML element.
 *
 * The content is emitted verbatim inside a `<![CDATA[ ... ]]>` block and is therefore
 * not escaped. Any `]]>` sequence inside the payload is split to keep the CDATA well-formed.
 *
 * @property value The raw content of the CDATA section.
 * @since 3.9.0
 */
data class XmlCData(val value: String) : XmlNode {
    /**
     * Returns the CDATA representation of this node, splitting any `]]>` sequence
     * contained in the payload to preserve well-formedness.
     *
     * @since 3.9.0
     */
    override fun toXmlString(indent: Int, depth: Int): String =
        "<![CDATA[${value.replace("]]>", "]]]]><![CDATA[>")}]]>"
}

/**
 * Represents an XML comment (`<!-- ... -->`).
 *
 * The content is not escaped, but any `--` sequence inside the comment is replaced
 * with `- -` to preserve well-formedness, as `--` is not allowed inside XML comments.
 *
 * @property value The raw content of the comment.
 * @since 3.9.0
 */
data class XmlComment(val value: String) : XmlNode {
    /**
     * Returns the comment representation of this node.
     *
     * @since 3.9.0
     */
    override fun toXmlString(indent: Int, depth: Int): String =
        "<!--${value.replace("--", "- -")}-->"
}

/**
 * Represents an XML element, with a tag name, attributes and child nodes.
 *
 * An element can contain text, CDATA, comments and/or other elements. When it has no
 * children and no text, it is serialized as a self-closing tag (`<name/>`).
 *
 * @property name       The tag name of the element.
 * @property attributes The attributes of the element, preserving insertion order.
 * @property children   The child nodes (elements, text, CDATA, comments) of the element.
 * @since 3.9.0
 */
data class XmlElement(
    val name: String,
    val attributes: LinkedHashMap<String, String> = LinkedHashMap(),
    val children: List<XmlNode> = emptyList()
) : XmlNode {

    /**
     * Converts this XML element (and its subtree) into a string representation.
     *
     * Elements whose children are only a single text/CDATA node are rendered inline
     * (`<tag>value</tag>`); elements with element children are rendered with indentation
     * when `indent > 0`. Empty elements are self-closed (`<tag/>`).
     *
     * @param indent The number of spaces to use for each level of indentation. `0` produces compact output.
     * @param depth  The current depth level in the XML tree.
     * @since 3.9.0
     */
    override fun toXmlString(indent: Int, depth: Int): String {
        val attrs = if (attributes.isEmpty()) String.EMPTY
        else attributes.entries.joinToString(String.EMPTY) { [k, v] -> " $k=\"${escapeXmlAttr(v)}\"" }

        if (children.isEmpty()) return "<$name$attrs/>"

        // If the only children are non-element nodes (text/cdata/comment), render inline.
        val hasElements = children.any { it is XmlElement }
        if (!hasElements) {
            val inline = children.joinToString(String.EMPTY) { it.toXmlString(0, 0) }
            return "<$name$attrs>$inline</$name>"
        }

        val pad = if (indent > 0) " ".repeat(indent * (depth + 1)) else String.EMPTY
        val padClose = if (indent > 0) " ".repeat(indent * depth) else String.EMPTY
        val nl = if (indent > 0) "\n" else String.EMPTY

        val body = children.joinToString(nl) { child -> "$pad${child.toXmlString(indent, depth + 1)}" }
        return "<$name$attrs>$nl$body$nl$padClose</$name>"
    }

    /**
     * Converts this element into an [Xml] wrapper, pretty-printed by default.
     *
     * @param indent The number of spaces to use for each level of indentation.
     *               Pass `0` to produce compact/inline output.
     * @return an [Xml] instance representing this element as root.
     * @since 3.9.0
     */
    fun toXml(indent: Int = 2): Xml = Xml(toXmlString(indent, 0))
}

// --- ESCAPE HELPERS -----------------------------------------------------------------------------

/**
 * Escapes text content for safe inclusion inside an XML element body.
 *
 * Replaces `&`, `<` and `>` with their XML entity references.
 *
 * @since 3.9.0
 */
internal fun escapeXml(value: String): String =
    value.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")

/**
 * Escapes text content for safe inclusion inside an XML attribute value.
 *
 * In addition to the rules of [escapeXml], it also escapes `"` and `'`.
 *
 * @since 3.9.0
 */
internal fun escapeXmlAttr(value: String): String =
    escapeXml(value)
        .replace("\"", "&quot;")
        .replace("'", "&apos;")

// --- CONVERSION ---

/**
 * Converts an arbitrary value into its XML-string representation for use inside an element.
 *
 * - `null` is rendered as an empty string.
 * - Numbers and booleans use their default `toString()`.
 * - Anything else is stringified via `toString()`.
 *
 * Escaping is performed by the caller (element builder), not here.
 *
 * @since 3.9.0
 */
internal fun Any?.toXmlText(): String = when (this) {
    null -> String.EMPTY
    else -> toString()
}

// --- ELEMENT BUILDER ---

/**
 * A builder for constructing an [XmlElement] in a structured and readable manner.
 *
 * The builder exposes three primary DSL operations:
 * - **Attributes** via the infix `=` operator on strings: `"id" `=` "42"`.
 * - **Text/CDATA/comment children** via [text], [cdata] and [comment].
 * - **Child elements** by calling [element] (or the shorthand [invoke] on a string tag name),
 *   each configured by another DSL block.
 *
 * Repeated invocations of the same tag name naturally produce an array-like sequence
 * (XML arrays are just repeated siblings).
 *
 * @property name The tag name of the element being built.
 * @since 3.9.0
 */
@XmlDslMarker
class XmlElementBuilder(val name: String) {

    /**
     * The attributes collected for the element being built, preserving insertion order.
     *
     * @since 3.9.0
     */
    var attributes = emptyMMap<String, Any?>()

    /**
     * The child nodes collected for the element being built, in insertion order.
     *
     * @since 3.9.0
     */
    var children = emptyMList<XmlNode>()

    // -- Attributes --

    /**
     * Assigns a string value to an attribute of the current element.
     *
     * Usage: `"id" `=` "42"`. If [value] is `null`, the attribute is omitted.
     *
     * @param value The attribute value. `null` omits the attribute.
     * @since 3.9.0
     */
    infix fun String.to(value: String?) {
        if (value != null) attributes[this] = value
    }

    /**
     * Assigns a numeric value to an attribute of the current element.
     *
     * @param value The attribute value. `null` omits the attribute.
     * @since 3.9.0
     */
    @JvmName("attrNumber")
    infix fun String.to(value: Number?) {
        if (value != null) attributes[this] = value.toString()
    }

    /**
     * Assigns a boolean value to an attribute of the current element.
     *
     * @param value The attribute value. `null` omits the attribute.
     * @since 3.9.0
     */
    @JvmName("attrBoolean")
    infix fun String.to(value: Boolean?) {
        if (value != null) attributes[this] = value.toString()
    }

    /**
     * Assigns an arbitrary value (stringified via `toString`) to an attribute.
     *
     * @param value The attribute value. `null` omits the attribute.
     * @since 3.9.0
     */
    @JvmName("attrAny")
    infix fun String.to(value: Any?) {
        if (value != null) attributes[this] = value.toString()
    }

    // -- Text Children --

    /**
     * Adds a text child to the current element. The value is XML-escaped on serialization.
     *
     * @param value The text content.
     * @since 3.9.0
     */
    fun text(value: String) { children += XmlText(value) }

    /**
     * Adds a text child to the current element using any value's `toString` representation.
     *
     * @param value The value to stringify and add as text content.
     * @since 3.9.0
     */
    fun text(value: Any?) { children += XmlText(value.toXmlText()) }

    /**
     * Adds a CDATA section child to the current element. The content is not escaped.
     *
     * @param value The raw CDATA content.
     * @since 3.9.0
     */
    fun cdata(value: String) { children += XmlCData(value) }

    /**
     * Adds an XML comment child (`<!-- ... -->`) to the current element.
     *
     * @param value The comment content.
     * @since 3.9.0
     */
    fun comment(value: String) { children += XmlComment(value) }

    /**
     * Adds a pre-built [XmlNode] as a child of the current element.
     *
     * @since 3.9.0
     */
    operator fun XmlNode.unaryPlus() { children += this }

    // -- Element Children --

    /**
     * Adds a child element with the given tag name, configured by the provided block.
     *
     * @param name  The tag name of the child element.
     * @param block DSL block to configure the child.
     * @return The constructed [XmlElement] child.
     * @since 3.9.0
     */
    fun element(name: String, block: ReceiverConsumer<XmlElementBuilder> = {}): XmlElement {
        val child = XmlElementBuilder(name).apply(block).build()
        children += child
        return child
    }

    /**
     * Shorthand for [element]: calling the tag name as a function produces a child element.
     *
     * Usage: `"book" { "title" { text("1984") } }`
     *
     * @param block DSL block to configure the child.
     * @since 3.9.0
     */
    operator fun String.invoke(block: ReceiverConsumer<XmlElementBuilder> = {}): XmlElement =
        element(this, block)

    /**
     * Adds a child element with the given tag name and a simple text content.
     *
     * Usage: `"title" _ "1984"` is equivalent to `"title" { text("1984") }`.
     *
     * @param value The text content of the child element.
     * @since 3.9.0
     */
    infix fun String.`_`(value: String?): XmlElement =
        element(this) { if (value != null) text(value) }

    /**
     * Adds a child element whose text content is the `toString` of the provided value.
     *
     * @param value The value to stringify and embed as the child's text content.
     * @since 3.9.0
     */
    @JvmName("textElementAny")
    infix fun String.`_`(value: Any?): XmlElement =
        element(this) { if (value != null) text(value) }

    /**
     * Adds one child element per item in [values], each with the same tag name and
     * a text content computed from the item (via `toString`). This is the canonical way
     * to produce "array-like" XML sections.
     *
     * Usage: `"tag" _ listOf("a", "b", "c")` produces `<tag>a</tag><tag>b</tag><tag>c</tag>`.
     *
     * @param values The items whose text representation will populate repeated sibling elements.
     * @return The list of created children.
     * @since 3.9.0
     */
    @JvmName("textElementIterable")
    infix fun String.`_`(values: Iterable<Any?>): List<XmlElement> =
        values.map { this _ it }

    /**
     * Adds a pre-built [XmlElement] as a child of the current element.
     *
     * @param element The element to append.
     * @since 3.9.0
     */
    operator fun plus(element: XmlElement): XmlElementBuilder {
        children += element
        return this
    }

    /**
     * Builds and returns an immutable [XmlElement] snapshot of the current builder state.
     *
     * @return The constructed [XmlElement].
     * @since 3.9.0
     */
    fun build(): XmlElement = XmlElement(
        name,
        attributes.mapToMap { Pair(it.key, it.value?.toString()) }
            .filterValues { it != null }
            .mapValues { it.value!! }
            .let { LinkedHashMap(it) },
        children.toList()
    )
}

// --- TOP-LEVEL ENTRY POINTS ---

/**
 * Builds an [Xml] document using a DSL block to define the root element's structure.
 *
 * @param name   The tag name of the root element.
 * @param indent Indentation spaces per nesting level. Use `0` for compact/inline output.
 *               Defaults to `2`.
 * @param block  A DSL block configuring the root element via an [XmlElementBuilder].
 * @return A fully-serialized [Xml] instance.
 * @since 3.9.0
 */
fun buildXml(name: String, indent: Int = 2, block: ReceiverConsumer<XmlElementBuilder> = {}): Xml =
    XmlElementBuilder(name).apply(block).build().toXml(indent)

/**
 * Constructs an [XmlElement] using a DSL block, without serializing it.
 *
 * Useful as a building-block for larger structures.
 *
 * @param name  The tag name of the element.
 * @param block A DSL block configuring the element.
 * @return The constructed [XmlElement].
 * @since 3.9.0
 */
fun xmlElement(name: String, block: ReceiverConsumer<XmlElementBuilder> = {}): XmlElement =
    XmlElementBuilder(name).apply(block).build()

/**
 * Initializes an [XmlElementBuilder] with the provided DSL block, without building it.
 *
 * Useful to keep a mutable reference to a builder that will be further composed before build.
 *
 * @param name  The tag name of the element being built.
 * @param block A DSL block configuring the builder.
 * @return The configured [XmlElementBuilder].
 * @since 3.9.0
 */
fun initXml(name: String, block: ReceiverConsumer<XmlElementBuilder> = {}): XmlElementBuilder =
    XmlElementBuilder(name).apply(block)

/**
 * Produces a CDATA node, usable with the unary plus operator inside a builder block.
 *
 * Usage: `+cdata("<raw/>")`.
 *
 * @param value The raw CDATA content.
 * @since 3.9.0
 */
fun cdata(value: String): XmlCData = XmlCData(value)

/**
 * Produces a comment node, usable with the unary plus operator inside a builder block.
 *
 * Usage: `+comment("COMMENT")`.
 *
 * @param value The comment content.
 * @since 3.9.0
 */
fun comment(value: String): XmlComment = XmlComment(value)