/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("java_integer_as_kotlin_int")
@file:OptIn(Beta::class)

package dev.tommasop1804.kutils.classes.coding

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.Json.Companion.toJson
import dev.tommasop1804.kutils.classes.coding.Xml.Companion.DEFAULT_SEPARATOR
import dev.tommasop1804.kutils.classes.coding.Yaml.Companion.toYaml
import dev.tommasop1804.kutils.exceptions.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.dataformat.xml.XmlMapper
import tools.jackson.dataformat.xml.XmlModule
import tools.jackson.module.kotlin.KotlinModule
import java.io.File
import java.io.StringReader
import java.io.StringWriter
import java.nio.file.Path
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper as OldXmlMapper
import org.intellij.lang.annotations.Language as IJLanguage

/**
 * Represents an XML document with various utilities for parsing, serializing, validating, and converting XML strings.
 *
 * Features include:
 * - Validation of XML content.
 * - Conversion between XML and other data formats, including JSON and YAML.
 * - Writing and reading XML from files.
 * - Accessing and modifying nested XML structures via dot-path or XPath.
 * - XSLT transformations.
 * - XSD schema validation.
 * - Merge patch and XML patch operations (delegated to JSON patch).
 *
 * The class is compatible with JSON serialization and deserialization libraries and leverages
 * Jackson XML for underlying parsing and serialization logic, and the JDK `javax.xml` APIs for
 * XPath, XSLT and XSD validation.
 *
 * @property value The string representation of the XML.
 * @property length The length of the XML string.
 * @property pretty Indicates whether the XML string is formatted in a human-readable way.
 * @property isArray Indicates whether the XML root contains a repeated-element sequence (array-like).
 * @property isObject Indicates whether the XML root represents an object-like structure.
 * @property rootName The name of the XML root element.
 * @property fieldsNames A list of child element names of the root, if the XML represents an object.
 * @since 3.9.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Xml.Companion.Serializer::class)
@JsonDeserialize(using = Xml.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Xml.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Xml.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_collection_declaration", "kutils_getorthrow_as_invoke", "RedundantSuppression")
@Beta(since = "3.9.0")
open class Xml private constructor(@param:IJLanguage("XML") override val value: String) : CharSequence, Code(value, Language.Xml) {

    /**
     * Represents the length of the underlying string value.
     *
     * @return the total number of characters in the string.
     * @since 3.9.0
     */
    override val length: Int
        get() = value.length

    /**
     * Provides a prettified XML representation of the underlying value.
     * This lazily computed property formats the XML in a human-readable way
     * with appropriate indentation and spacing.
     *
     * @since 3.9.0
     */
    val pretty: Xml
        get() = Xml(prettify(value))

    /**
     * Indicates whether the provided XML value represents an array-like structure.
     * An XML is considered "array-like" when the root element contains two or more
     * direct child elements that share the same tag name and no distinct siblings.
     *
     * @return `true` if the XML root wraps a repeated sequence, `false` otherwise.
     * @since 3.9.0
     */
    val isArray: Boolean
        get() {
            val children = toDocument().documentElement.childrenAsList()
            if (children.size < 2) return false
            val firstName = children.first().nodeName
            return children.all { it.nodeName == firstName }
        }

    /**
     * Indicates whether the parsed XML value represents an object-like structure.
     * An XML is considered "object-like" when the root element has at least one child element
     * and the children do not form an homogeneous array-like sequence.
     *
     * @return `true` if the XML value is an object, `false` otherwise.
     * @since 3.9.0
     */
    val isObject: Boolean
        get() = toDocument().documentElement.childrenAsList().isNotEmpty() && !isArray

    /**
     * The name of the XML root element.
     *
     * @return the tag name of the root element.
     * @since 3.9.0
     */
    val rootName: String
        get() = toDocument().documentElement.nodeName

    /**
     * A set of direct child element names of the XML root.
     *
     * @return a set of strings representing the direct child element names of the root.
     * @since 3.9.0
     */
    val fieldsNames: Set<String>
        get() = toDocument().documentElement.childrenAsList().map { it.nodeName }.toSet()

    /**
     * Secondary constructor that initializes an instance using a `Code` object.
     * It internally delegates to the primary constructor with the code's value.
     *
     * @param code The `Code` object containing the value and the language information.
     * @throws ExpectationMismatchException if the `Code` object does not have a language equal to `Language.XML`.
     * @since 3.9.0
     */
    constructor(code: Code) : this(code.value) {
        code.language.expect(Language.Xml)
    }

    /**
     * Constructs an instance by parsing the given XML input. The input is validated and converted
     * to an XML string representation. If the input is malformed, a `MalformedInputException` is thrown.
     *
     * @param xml The input XML as a character sequence to be parsed and validated.
     * @throws MalformedInputException If the provided XML input is invalid or malformed.
     * @since 3.9.0
     */
    constructor(@IJLanguage("XML") xml: CharSequence) : this(
        tryOrThrow({ -> MalformedInputException("Input is not a valid XML") }) {
            documentToString(parseDocument(xml.toString()))
        }
    )

    /**
     * Creates an instance by reading the content of the specified file.
     *
     * @param file The file whose content will be read and used to initialize the instance.
     * @since 3.9.0
     */
    constructor(file: File) : this(file.readText()) {
        file.exists().expect(true)
        file.isFile.expect(true)
        file.canRead().expect(true)
        file.extension.validate(file::extension, "file") { it equalsIgnoreCase "xml" }
    }

    /**
     * Creates an instance by reading the content of the specified path.
     *
     * @param path The path of the file whose content will be read and used to initialize the instance.
     * @since 3.9.0
     */
    constructor(path: Path) : this(path.toFile())

    init {
        tryOrThrow({ -> MalformedInputException(Xml::class) }) {
            parseDocument(value)
        }
    }

    companion object {
        /**
         * The primary XML mapper using the new `tools.jackson` API.
         * @since 3.9.0
         */
        val MAPPER: XmlMapper = XmlMapper.builder()
            .addModule(XmlModule())
            .addModule(KotlinModule.Builder().build())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false)
            .build()

        /**
         * The legacy XML mapper using the `com.fasterxml.jackson` API, kept for backwards compatibility.
         * @since 3.9.0
         */
        val OLD_MAPPER: OldXmlMapper = OldXmlMapper().apply {
            configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_SELF_REFERENCES, false)
        }

        private val DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance().apply {
            isNamespaceAware = true
            try {
                setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
                setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
                setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, String.EMPTY)
                setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, String.EMPTY)
            } catch (_: Exception) {
                // best-effort: some implementations may not support all features
            }
        }

        private val TRANSFORMER_FACTORY: TransformerFactory = TransformerFactory.newInstance().apply {
            try {
                setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, String.EMPTY)
                setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, String.EMPTY)
            } catch (_: Exception) { /* ignore */ }
        }

        private val XPATH_FACTORY: XPathFactory = XPathFactory.newInstance()

        /**
         * An empty XML root element (`<root/>`), used as a placeholder or default value.
         * @since 3.9.0
         */
        val EMPTY_XML = Xml("<root/>")

        /**
         * The default separator used for splitting dot-paths in element navigation.
         * @since 3.9.0
         */
        val DEFAULT_SEPARATOR = Regex("\\.")

        /**
         * Parses a raw XML string into a [Document].
         * @since 3.9.0
         */
        @PublishedApi
        internal fun parseDocument(xml: String): Document =
            DOCUMENT_BUILDER_FACTORY.newDocumentBuilder().parse(InputSource(StringReader(xml)))

        /**
         * Serializes a [Document] back to a compact XML string.
         * @since 3.9.0
         */
        @PublishedApi
        internal fun documentToString(doc: Document, pretty: Boolean = false): String {
            if (!pretty) stripWhitespaceNodes(doc.documentElement)
            val transformer = TRANSFORMER_FACTORY.newTransformer().apply {
                setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
                setOutputProperty(OutputKeys.ENCODING, "UTF-8")
                if (pretty) {
                    setOutputProperty(OutputKeys.INDENT, "yes")
                    setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
                } else {
                    setOutputProperty(OutputKeys.INDENT, "no")
                }
            }
            val writer = StringWriter()
            transformer.transform(DOMSource(doc), StreamResult(writer))
            return writer.toString().trim()
        }

        private fun stripWhitespaceNodes(node: Node) {
            val children = node.childNodes
            val toRemove = mutableListOf<Node>()
            for (i in 0 until children.length) {
                val child = children.item(i)
                when (child.nodeType) {
                    Node.TEXT_NODE if child.textContent.isBlank() -> toRemove.add(child)
                    Node.ELEMENT_NODE -> stripWhitespaceNodes(child)
                }
            }
            toRemove.forEach { node.removeChild(it) }
        }

        /**
         * Checks if the string is a valid XML document.
         *
         * @receiver The string to be checked for validity as XML.
         * @return `true` if the string is a valid XML document, `false` otherwise.
         * @since 3.9.0
         */
        fun String.isValidXml(): Boolean = try {
            parseDocument(this)
            true
        } catch (_: Exception) {
            false
        }

        /**
         * Formats a given XML string into a human-readable, pretty-printed format.
         *
         * @param xml The XML string to be pretty-printed.
         * @return A formatted XML string with indentation and line breaks to improve readability.
         * @since 3.9.0
         */
        fun prettify(@IJLanguage("XML") xml: String): String = documentToString(parseDocument(xml), pretty = true)

        /**
         * Converts the current file into an instance of `Xml`.
         * The operation is wrapped in a `Result` to handle any potential exceptions that
         * may occur during the conversion process.
         *
         * @return A `Result` containing the `Xml` representation of the file if successful,
         * or an exception if an error occurs during processing.
         * @since 3.13.0
         */
        fun File.toXml() = runCatching { Xml(this) }
        /**
         * Converts the current Path object to an XML representation.
         *
         * Returns a Result object that contains the XML representation of
         * the Path if the operation is successful, or an exception if an
         * error occurs during the conversion.
         * @since 3.13.0
         */
        fun Path.toXml() = runCatching { Xml(this) }
        /**
         * Converts a String into an [Xml] object, wrapping the operation in a [Result].
         *
         * @receiver The string to be converted into XML.
         * @return A [Result] containing the parsed [Xml] or an exception if parsing fails.
         * @since 3.9.0
         */
        fun @receiver:IJLanguage("XML") String.toXml() = runCatching { Xml(this) }
        /**
         * Converts a JSON object to its XML representation.
         *
         * The root element name defaults to `"root"` and can be customized.
         *
         * @receiver The JSON object to be converted to XML.
         * @param rootName The name of the XML root element (default: `"root"`).
         * @return An [Xml] instance representing the data structure of the JSON input.
         * @since 3.9.0
         */
        @JvmName("jsonToXml")
        fun Json.toXml(rootName: String = "root"): Xml {
            val tree = Json.MAPPER.readTree(value)
            val obj = Json.MAPPER.convertValue(tree, object : TypeReference<Any>() {})
            return Xml(MAPPER.writer().withRootName(rootName).writeValueAsString(obj))
        }
        /**
         * Converts the current TOML data to its XML representation.
         *
         * @param rootName The name to be used for the root element in the resulting XML. Defaults to "root" if not specified.
         * @return The XML representation of the TOML data.
         * @since 3.11.0
         */
        @JvmName("tomlToXml")
        fun Toml.toXml(rootName: String = "root"): Xml = toJson().toXml(rootName)
        /**
         * Converts a YAML object to its XML representation.
         *
         * @receiver The YAML object to be converted to XML.
         * @param rootName The name of the XML root element (default: `"root"`).
         * @return An [Xml] instance representing the data structure of the YAML input.
         * @since 3.9.0
         */
        @JvmName("yamlToXml")
        @OptIn(Beta::class)
        fun Yaml.toXml(rootName: String = "root"): Xml = toJson().toXml(rootName)
        /**
         * Converts the current CSV instance into its XML representation.
         *
         * @param rootName The name of the XML root element (default: `"root"`).
         * @return An [Xml] instance representing the data structure of the CSV input.
         * @since 3.13.0
         */
        @JvmName("csvToXml")
        @OptIn(Beta::class)
        fun Csv.toXml(rootName: String = "root"): Xml = toJson().toXml(rootName)
        /**
         * Converts the Iterable into an XML representation.
         *
         * @param rootName The name of the root element in the generated XML. Defaults to "items".
         * @param itemName The name of each item element in the generated XML. Defaults to "item".
         * @return An XML object representing the Iterable's contents as XML.
         * @since 3.9.0
         */
        fun Iterable<*>.toXml(rootName: String = "items", itemName: String = "item"): Xml {
            val items = toList()
            val inner = items.joinToString(String.EMPTY) { el ->
                if (el == null) "<$itemName/>"
                else {
                    val serialized = MAPPER.writer().withRootName(itemName).writeValueAsString(el)
                    serialized
                }
            }
            return Xml("<$rootName>$inner</$rootName>")
        }
        /**
         * Converts the array into an XML representation.
         *
         * @param rootName The name of the root element in the generated XML. Defaults to "items".
         * @param itemName The name of each item element in the generated XML. Defaults to "item".
         * @return An XML object representing the array's contents as XML.
         * @since 3.9.0
         */
        fun Array<*>.toXml(rootName: String = "items", itemName: String = "item") = toList().toXml(rootName, itemName)
        /**
         * Converts the given object to an XML representation using the predefined XML mapper.
         *
         * @receiver The object to be converted into XML.
         * @param rootName The name of the XML root element (default: `"root"`).
         * @return An [Xml] instance containing the serialized XML string.
         * @since 3.9.0
         */
        @JvmName("anyToXml")
        fun Any.toXml(rootName: String = "root"): Xml =
            Xml(MAPPER.writer().withRootName(rootName).writeValueAsString(this))

        /**
         * Converts the current file into an instance of `Xml`.
         * The operation is wrapped in a `Result` to handle any potential exceptions that
         * may occur during the conversion process.
         *
         * @return A `Result` containing the `Xml` representation of the file if successful,
         * or an exception if an error occurs during processing.
         * @since 3.13.0
         */
        fun File.toPrettyXml() = runCatching { Xml(this).pretty }
        /**
         * Converts the current Path object to an XML representation.
         *
         * Returns a Result object that contains the XML representation of
         * the Path if the operation is successful, or an exception if an
         * error occurs during the conversion.
         * @since 3.13.0
         */
        fun Path.toPrettyXml() = runCatching { Xml(this).pretty }
        /**
         * Converts a String into an [Xml] object, wrapping the operation in a [Result].
         *
         * @receiver The string to be converted into XML.
         * @return A [Result] containing the parsed [Xml] or an exception if parsing fails.
         * @since 3.13.0
         */
        fun @receiver:IJLanguage("XML") String.toPrettyXml() = runCatching { Xml(this).pretty }
        /**
         * Converts a JSON object to its XML representation.
         *
         * The root element name defaults to `"root"` and can be customized.
         *
         * @receiver The JSON object to be converted to XML.
         * @param rootName The name of the XML root element (default: `"root"`).
         * @return An [Xml] instance representing the data structure of the JSON input.
         * @since 3.13.0
         */
        @JvmName("jsonToPrettyXml")
        fun Json.toPrettyXml(rootName: String = "root"): Xml {
            val tree = Json.MAPPER.readTree(value)
            val obj = Json.MAPPER.convertValue(tree, object : TypeReference<Any>() {})
            return Xml(MAPPER.writer().withRootName(rootName).writeValueAsString(obj)).pretty
        }
        /**
         * Converts the current TOML data to its XML representation.
         *
         * @param rootName The name to be used for the root element in the resulting XML. Defaults to "root" if not specified.
         * @return The XML representation of the TOML data.
         * @since 3.13.0
         */
        @JvmName("tomlToPrettyXml")
        fun Toml.toPrettyXml(rootName: String = "root"): Xml = toJson().toXml(rootName).pretty
        /**
         * Converts a YAML object to its XML representation.
         *
         * @receiver The YAML object to be converted to XML.
         * @param rootName The name of the XML root element (default: `"root"`).
         * @return An [Xml] instance representing the data structure of the YAML input.
         * @since 3.13.0
         */
        @JvmName("yamlToPrettyXml")
        @OptIn(Beta::class)
        fun Yaml.toPrettyXml(rootName: String = "root"): Xml = toJson().toXml(rootName).pretty
        /**
         * Converts the current CSV instance into its XML representation.
         *
         * @param rootName The name of the XML root element (default: `"root"`).
         * @return An [Xml] instance representing the data structure of the CSV input.
         * @since 3.13.0
         */
        @JvmName("csvToPrettyXml")
        @OptIn(Beta::class)
        fun Csv.toPrettyXml(rootName: String = "root"): Xml = toJson().toXml(rootName).pretty
        /**
         * Converts the Iterable into an XML representation.
         *
         * @param rootName The name of the root element in the generated XML. Defaults to "items".
         * @param itemName The name of each item element in the generated XML. Defaults to "item".
         * @return An XML object representing the Iterable's contents as XML.
         * @since 3.13.0
         */
        fun Iterable<*>.toPrettyXml(rootName: String = "items", itemName: String = "item"): Xml {
            val items = toList()
            val inner = items.joinToString(String.EMPTY) { el ->
                if (el == null) "<$itemName/>"
                else {
                    val serialized = MAPPER.writer().withRootName(itemName).writeValueAsString(el)
                    serialized
                }
            }
            return Xml("<$rootName>$inner</$rootName>").pretty
        }
        /**
         * Converts the array into an XML representation.
         *
         * @param rootName The name of the root element in the generated XML. Defaults to "items".
         * @param itemName The name of each item element in the generated XML. Defaults to "item".
         * @return An XML object representing the array's contents as XML.
         * @since 3.13.0
         */
        fun Array<*>.toPrettyXml(rootName: String = "items", itemName: String = "item") = toList().toXml(rootName, itemName).pretty
        /**
         * Converts the given object to an XML representation using the predefined XML mapper.
         *
         * @receiver The object to be converted into XML.
         * @param rootName The name of the XML root element (default: `"root"`).
         * @return An [Xml] instance containing the serialized XML string.
         * @since 3.13.0
         */
        @JvmName("anyToPrettyXml")
        fun Any.toPrettyXml(rootName: String = "root"): Xml =
            Xml(MAPPER.writer().withRootName(rootName).writeValueAsString(this)).pretty

        /**
         * Reads an XML file and deserializes its content into an object of the specified type.
         *
         * @param file The file containing the XML data.
         * @return A [Result] wrapping the deserialized object of type [T].
         * @since 3.9.0
         */
        inline fun <reified T> readFromFile(file: File): Result<T> =
            runCatching { MAPPER.readValue(file, T::class.java) }

        /**
         * Reads an XML file and deserializes its content into a list of objects of the specified type.
         *
         * @param file The XML file to be read.
         * @return A [Result] wrapping the deserialized list of type [T].
         * @since 3.9.0
         */
        inline fun <reified T> readListFromFile(file: File): Result<List<T>> = runCatching {
            MAPPER.readValue(file, MAPPER.typeFactory.constructCollectionType(List::class.java, T::class.java))
        }

        /**
         * Reads an XML file and deserializes its content into a set of objects of the specified type.
         *
         * @param file The XML file to be read.
         * @return A [Result] wrapping the deserialized set of type [T].
         * @since 3.9.0
         */
        inline fun <reified T> readSetFromFile(file: File): Result<Set<T>> = runCatching {
            MAPPER.readValue(file, MAPPER.typeFactory.constructCollectionType(Set::class.java, T::class.java))
        }

        /**
         * Reads an XML file and deserializes its content into a map with string keys and values of a generic type.
         *
         * @param file The XML file to be read.
         * @return A [Result] wrapping the deserialized map.
         * @since 3.9.0
         */
        fun <T> readMapFromFile(file: File): Result<Map<String, T>> = runCatching {
            MAPPER.readValue(file, object : TypeReference<Map<String, T>>() {})
        }

        class Serializer : ValueSerializer<Xml>() {
            override fun serialize(value: Xml, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                // When serializing inside a JSON context, we emit the XML as its JSON projection.
                val jsonValue = value.toJson().value
                val node = Json.MAPPER.readTree(jsonValue)
                when {
                    node.isArray -> gen.writePOJO(Json.MAPPER.convertValue(node, object : TypeReference<List<Any>>() {}))
                    node.isObject -> gen.writePOJO(Json.MAPPER.convertValue(node, object : TypeReference<Map<String, Any>>() {}))
                    else -> gen.writeRaw(value.value)
                }
            }
        }

        class Deserializer : ValueDeserializer<Xml>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Xml =
                Json(p.objectReadContext().readTree<JsonNode>(p).toString()).toXml()
        }

        class OldSerializer : JsonSerializer<Xml>() {
            override fun serialize(value: Xml, gen: JsonGenerator, serializers: SerializerProvider) {
                val jsonValue = value.toJson().value
                val node = Json.OLD_MAPPER.readTree(jsonValue)
                when {
                    node.isArray -> gen.writeObject(Json.OLD_MAPPER.convertValue(node, object : com.fasterxml.jackson.core.type.TypeReference<List<Any>>() {}))
                    node.isObject -> gen.writeObject(Json.OLD_MAPPER.convertValue(node, object : com.fasterxml.jackson.core.type.TypeReference<Map<String, Any>>() {}))
                    else -> gen.writeRaw(value.value)
                }
            }
        }

        class OldDeserializer : JsonDeserializer<Xml>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Xml =
                Json(p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p).toString()).toXml()
        }
    }

    /**
     * Retrieves the character at the specified index.
     * @since 3.9.0
     */
    override operator fun get(index: Int) = value[index]

    /**
     * Returns a new character subsequence.
     * @since 3.9.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int) = value.subSequence(startIndex, endIndex)

    /**
     * Returns the string representation of this XML value.
     * @since 3.9.0
     */
    override fun toString() = value

    // CONVERSIONS ---------------------------------------------------------------------------------

    /**
     * Converts the stored XML string into an object of the specified type [T].
     *
     * @return a [Result] containing the deserialized object or an exception.
     * @since 3.9.0
     */
    inline fun <reified T> toObject() = runCatching { MAPPER.readValue(value, T::class.java) as T }

    /**
     * Converts the XML content into a typed array of the specified type [T].
     *
     * @return a [Result] containing the typed array of type [T].
     * @since 3.9.0
     */
    inline fun <reified T> toArray() = runCatching { toList<T>().getOrThrow().toTypedArray() }

    /**
     * Converts the XML content into a list of objects of type [T].
     *
     * This interprets the XML as an array-like structure: the root element's children are
     * each deserialized into an instance of [T].
     *
     * @return a [Result] containing the deserialized list.
     * @since 3.9.0
     */
    inline fun <reified T> toList(): Result<List<T>> = runCatching {
        val children = parseDocument(value).documentElement.childrenAsList()
        if (children.isEmpty()) emptyList()
        else {
            val firstName = children.first().nodeName
            children.filter { it.nodeName == firstName }.map { elementToObject<T>(it) }
        }
    }

    /**
     * Converts the XML content into a mutable list of objects of type [T].
     *
     * @return a [Result] containing the mutable list.
     * @since 3.9.0
     */
    inline fun <reified T> toMList(): Result<MList<T>> = runCatching { toList<T>().getOrThrow().toMList() }

    /**
     * Converts the XML content into a set of objects of type [T].
     *
     * @return a [Result] containing the deserialized set.
     * @since 3.9.0
     */
    inline fun <reified T> toSet(): Result<Set<T>> = runCatching { toList<T>().getOrThrow().toSet() }

    /**
     * Converts the XML content into a mutable set of objects of type [T].
     *
     * @return a [Result] containing the mutable set.
     * @since 3.9.0
     */
    inline fun <reified T> toMSet(): Result<MSet<T>> = runCatching { toSet<T>().getOrThrow().toMSet() }

    /**
     * Converts the XML content into a map with string keys and values of type [V].
     *
     * @return a [Result] containing the deserialized map.
     * @since 3.9.0
     */
    inline fun <reified V> toMap(): Result<Map<String, V>> = runCatching {
        MAPPER.readValue(value, object : TypeReference<Map<String, V>>() {}) as Map<String, V>
    }

    /**
     * Converts the XML content into a mutable map with string keys and values of type [V].
     *
     * @return a [Result] containing the mutable map.
     * @since 3.9.0
     */
    inline fun <reified V> toMMap(): Result<MMap<String, V>> = runCatching { toMap<V>().getOrThrow().toMMap() }

    /**
     * Converts the XML content into a [DataMap].
     *
     * @return a [Result] containing the [DataMap].
     * @since 3.9.0
     */
    fun toDataMap(): Result<DataMap> = runCatching {
        MAPPER.readValue(value, object : TypeReference<DataMap>() {}) as DataMap
    }

    /**
     * Converts the XML content into a [DataMMap].
     *
     * @return a [Result] containing the [DataMMap].
     * @since 3.9.0
     */
    fun toDataMMap(): Result<DataMMap> = runCatching {
        MAPPER.readValue(value, object : TypeReference<DataMMap>() {}) as DataMMap
    }

    /**
     * Converts the XML content into a [DataMapNN].
     *
     * @return a [Result] containing the [DataMapNN].
     * @since 3.9.0
     */
    fun toDataMapNN(): Result<DataMapNN> = runCatching {
        MAPPER.readValue(value, object : TypeReference<DataMapNN>() {}) as DataMapNN
    }

    /**
     * Converts the XML content into a [DataMMapNN].
     *
     * @return a [Result] containing the [DataMMapNN].
     * @since 3.9.0
     */
    fun toDataMMapNN(): Result<DataMMapNN> = runCatching {
        MAPPER.readValue(value, object : TypeReference<DataMMapNN>() {}) as DataMMapNN
    }

    /**
     * Converts the provided value into a JSON object, optionally replacing specific placeholders in the output.
     *
     * @param valueKey Optional key to replace placeholders (default is an empty string).
     * @return A JSON object containing the processed value.
     * @since 3.9.0
     */
    fun toJson(valueKey: String = String.EMPTY): Json {
        val tree = MAPPER.readTree(value)
        return Json(Json.MAPPER.writeValueAsString(tree).replace(Regex("\"\":"), "\"$valueKey\":"))
    }

    /**
     * Converts the data represented by the given value key into a Yaml object.
     *
     * @param valueKey The key used to retrieve the value to be converted. Defaults to an empty string.
     * @return A Yaml object constructed from the input value.
     * @since 3.9.0
     */
    @OptIn(Beta::class)
    fun toYaml(valueKey: String = String.EMPTY): Yaml = toJson(valueKey).toYaml()

    /**
     * Converts this XML into a W3C [Document].
     *
     * @return the parsed DOM [Document].
     * @since 3.9.0
     */
    fun toDocument(): Document = parseDocument(value)

    /**
     * Writes the current XML value to the specified file.
     *
     * @param file The file to which the XML should be written.
     * @since 3.9.0
     */
    fun writeToFile(file: File) = file.writeText(value)

    // --- NAVIGATION ---

    /**
     * Retrieves a nested XML fragment using a dot-path.
     *
     * The path is relative to the XML root (the root name is NOT the first segment);
     * if an index is needed (e.g. picking the n-th `<item>` when multiple exist),
     * use an integer segment: `"items.0.title"`.
     *
     * @param dotPath The dot-separated path to the desired element.
     * @param regexSeparator The regex used to split the path. Defaults to [DEFAULT_SEPARATOR].
     * @return The matched node wrapped in an [Xml], or `null` if the path does not exist.
     * @since 3.9.0
     */
    operator fun get(dotPath: String, regexSeparator: Regex = DEFAULT_SEPARATOR): Xml? {
        val node = getAsNode(dotPath, regexSeparator) ?: return null
        return Xml(documentToString(nodeToDocument(node)))
    }

    /**
     * Retrieves a DOM [Node] by dot-path. See [get] for path semantics.
     *
     * @since 3.9.0
     */
    fun getAsNode(dotPath: String, regexSeparator: Regex = DEFAULT_SEPARATOR): Node? {
        var current: Node? = toDocument().documentElement
        if (dotPath.isBlank()) return current

        val segments = dotPath.split(regexSeparator)
        var i = 0
        while (i < segments.size) {
            val node = current ?: return null
            val segment = segments[i]
            val index = segment.toIntOrNull()

            current = if (index.isNotNull()) {
                node.childrenAsList().getOrNull(index)
            } else {
                val next = segments.getOrNull(i + 1)?.toIntOrNull()
                if (next.isNotNull()) {
                    val matching = node.childrenAsList().filter { it.nodeName == segment }
                    val picked = matching.getOrNull(next)
                    i++
                    picked
                } else {
                    node.childrenAsList().firstOrNull { it.nodeName == segment }
                }
            }
            i++
        }
        return current
    }

    /**
     * Retrieves the text content of a nested element via dot-path.
     *
     * @since 3.9.0
     */
    infix fun getTextAsNode(dotPath: String): String? = getAsNode(dotPath)?.textContent

    /**
     * Retrieves a list of nodes matching the given XPath expression.
     *
     * @param xpath The XPath expression to evaluate.
     * @return a [Result] containing the list of matching [Xml] fragments.
     * @since 3.9.0
     */
    fun xpath(xpath: String): Result<List<Xml>> = runCatching {
        val doc = toDocument()
        val expr = XPATH_FACTORY.newXPath().compile(xpath)
        val result = expr.evaluate(doc, XPathConstants.NODESET) as NodeList
        (0 until result.length).map { i ->
            val node = result.item(i)
            Xml(documentToString(nodeToDocument(node)))
        }
    }

    /**
     * Retrieves the single node matching the given XPath expression, or `null` if none match.
     *
     * @param xpath The XPath expression to evaluate.
     * @return a [Result] containing the single [Xml] fragment or `null`.
     * @since 3.9.0
     */
    fun xpathFirst(xpath: String): Result<Xml?> = runCatching { xpath(xpath).getOrThrow().firstOrNull() }

    /**
     * Evaluates an XPath expression that returns a string value.
     *
     * @param xpath The XPath expression to evaluate.
     * @return a [Result] containing the string value.
     * @since 3.9.0
     */
    fun xpathString(xpath: String): Result<String> = runCatching {
        val doc = toDocument()
        val expr = XPATH_FACTORY.newXPath().compile(xpath)
        expr.evaluate(doc, XPathConstants.STRING) as String
    }

    /**
     * Checks whether the XML contains a node at the specified dot-path.
     *
     * @since 3.9.0
     */
    operator fun invoke(dotPath: String, regexSeparator: Regex = DEFAULT_SEPARATOR): Boolean =
        getAsNode(dotPath, regexSeparator).isNotNull()

    // --- STRUCTURAL OPERATIONS ---

    /**
     * Merges this XML with another XML. The merge is performed by converting both sides to JSON,
     * applying a JSON merge (via [Json.plus]) and converting the result back to XML, preserving the
     * root name of the left-hand side.
     *
     * @param other the XML to merge with this one.
     * @return a new XML with the merged content.
     * @since 3.9.0
     */
    operator fun plus(other: Xml): Xml {
        val root = rootName
        val merged = (toJson() + other.toJson())
        return merged.toXml(root)
    }

    /**
     * Removes a top-level element by name.
     *
     * @param fieldName the name of the element to remove.
     * @return a new XML without the specified element.
     * @since 3.9.0
     */
    operator fun minus(fieldName: String): Xml {
        val root = rootName
        val json = toJson() - fieldName
        return json.toXml(root)
    }

    /**
     * Checks if the XML root has no child elements and no text content.
     *
     * @since 3.9.0
     */
    fun isEmptyXml(): Boolean {
        val root = toDocument().documentElement
        return root.childrenAsList().isEmpty() && root.textContent.isNullOrBlank()
    }

    /**
     * Negates [isEmptyXml].
     *
     * @since 3.9.0
     */
    fun isNotEmpty(): Boolean = !isEmptyXml()

    /**
     * Operator form of [isEmptyXml].
     *
     * @since 3.9.0
     */
    operator fun not(): Boolean = isEmptyXml()

    // --- PATCH OPERATIONS ---

    /**
     * Applies a JSON Merge Patch (RFC 7386) to this XML and returns the result.
     * The patch is expressed as [Json] since there is no standard XML merge-patch spec.
     *
     * @param patch the JSON merge patch to apply.
     * @return a [Result] containing the patched [Xml].
     * @since 3.9.0
     */
    infix fun mergePatch(patch: Json): Result<Xml> = runCatching {
        val root = rootName
        toJson().mergePatch(patch).getOrThrow().toXml(root)
    }

    /**
     * Applies a JSON Merge Patch to this XML, using another [Xml] as patch.
     *
     * @since 3.9.0
     */
    infix fun mergePatch(patch: Xml): Result<Xml> = mergePatch(patch.toJson())

    /**
     * Applies a JSON Merge Patch to this XML, using a [Yaml] as patch.
     *
     * @since 3.9.0
     */
    @OptIn(Beta::class)
    infix fun mergePatch(patch: Yaml): Result<Xml> = mergePatch(patch.toJson())

    /**
     * Applies a JSON Patch (RFC 6902) to this XML and returns the result.
     *
     * @param patch the JSON patch to apply.
     * @return a [Result] containing the patched [Xml].
     * @since 3.9.0
     */
    infix fun xmlPatch(patch: Json): Result<Xml> = runCatching {
        val root = rootName
        toJson().jsonPatch(patch).getOrThrow().toXml(root)
    }

    /**
     * Applies a JSON Patch (RFC 6902) using another [Xml] as patch.
     *
     * @since 3.9.0
     */
    infix fun xmlPatch(patch: Xml): Result<Xml> = xmlPatch(patch.toJson())

    /**
     * Applies a JSON Patch (RFC 6902) using a [Yaml] as patch.
     *
     * @since 3.9.0
     */
    @OptIn(Beta::class)
    infix fun xmlPatch(patch: Yaml): Result<Xml> = xmlPatch(patch.toJson())

    // --- XSLT ---

    /**
     * Applies an XSLT transformation to this XML, returning the transformed document as a string.
     *
     * @param xslt The XSLT stylesheet as an [Xml] document.
     * @return a [Result] containing the transformed output as a string.
     * @since 3.9.0
     */
    infix fun transform(xslt: Xml): Result<String> = runCatching {
        val transformer = TRANSFORMER_FACTORY.newTransformer(StreamSource(StringReader(xslt.value)))
        val writer = StringWriter()
        transformer.transform(StreamSource(StringReader(value)), StreamResult(writer))
        writer.toString()
    }

    /**
     * Applies an XSLT transformation to this XML, expecting XML output.
     *
     * @param xslt The XSLT stylesheet as an [Xml] document.
     * @return a [Result] containing the transformed [Xml].
     * @since 3.9.0
     */
    infix fun transformToXml(xslt: Xml): Result<Xml> = runCatching { Xml(transform(xslt).getOrThrow()) }

    // --- XSD VALIDATION ---

    /**
     * Validates this XML against the provided XSD schema.
     *
     * @param xsd The XSD schema as an [Xml] document.
     * @return a [Result] containing this [Xml] if validation succeeds, or a failure with the
     *         validation errors otherwise.
     * @since 3.9.0
     */
    infix fun validateWithSchema(xsd: Xml): Result<Xml> = runCatching {
        val factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).apply {
            try {
                setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, String.EMPTY)
                setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, String.EMPTY)
            } catch (_: Exception) { /* ignore */ }
        }
        val schema = factory.newSchema(StreamSource(StringReader(xsd.value)))
        val validator = schema.newValidator()
        try {
            validator.validate(StreamSource(StringReader(value)))
            this
        } catch (e: Exception) {
            throw XmlSchemaValidationException(e.message.orEmpty(), e)
        }
    }
}

@PublishedApi
internal fun nodeToDocument(node: Node): Document {
    val doc = DocumentBuilderFactory.newInstance().apply { isNamespaceAware = true }
        .newDocumentBuilder()
        .newDocument()
    val imported = doc.importNode(node, true)
    doc.appendChild(imported)
    return doc
}

/**
 * Collects the direct element children of a [Node] as a list, skipping text/comment nodes.
 * @since 3.9.0
 */
@PublishedApi
internal fun Node.childrenAsList(): List<Element> {
    val result = mutableListOf<Element>()
    val children = childNodes
    for (i in 0 until children.length) {
        val child = children.item(i)
        if (child.nodeType == Node.ELEMENT_NODE) result.add(child as Element)
    }
    return result
}

@PublishedApi
internal inline fun <reified T> elementToObject(element: Element): T {
    val xml = Xml.documentToString(nodeToDocument(element))
    return Xml.MAPPER.readValue(xml, T::class.java)
}