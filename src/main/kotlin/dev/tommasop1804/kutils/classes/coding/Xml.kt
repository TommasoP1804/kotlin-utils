/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("java_integer_as_kotlin_int")
@file:OptIn(Beta::class)
@file:MustUseReturnValues

package dev.tommasop1804.kutils.classes.coding

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.Json.Companion.toJson
import dev.tommasop1804.kutils.classes.coding.Yaml.Companion.toYaml
import dev.tommasop1804.kutils.classes.collections.*
import dev.tommasop1804.kutils.classes.collections.NonEmptyList.Companion.toNonEmptyList
import dev.tommasop1804.kutils.classes.collections.NonEmptyMList.Companion.toNonEmptyMList
import dev.tommasop1804.kutils.classes.collections.NonEmptyMSet.Companion.toNonEmptyMSet
import dev.tommasop1804.kutils.classes.collections.NonEmptySet.Companion.toNonEmptySet
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.classes.maps.*
import dev.tommasop1804.kutils.classes.maps.NonEmptyMMap.Companion.toNonEmptyMMap
import dev.tommasop1804.kutils.classes.maps.NonEmptyMap.Companion.toNonEmptyMap
import dev.tommasop1804.kutils.errors.*
import dev.tommasop1804.kutils.exceptions.*
import org.jetbrains.exposed.v1.core.Table
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import tools.jackson.core.JacksonException
import tools.jackson.core.exc.StreamReadException
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.dataformat.xml.XmlMapper
import tools.jackson.dataformat.xml.XmlModule
import tools.jackson.module.kotlin.KotlinModule
import tools.jackson.module.kotlin.readValue
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
import kotlin.reflect.typeOf
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
     * Indicates whether the underlying document structure is considered empty.
     *
     * This property returns `true` if the document's root element has no child elements
     * and its textual content is either null or entirely blank. Otherwise, it returns `false`.
     *
     * @since 6.1.0
     */
    @get:JvmName("isEmptyXml")
    val isEmpty: Boolean get() {
        val root = toDocument().documentElement
        return root.childrenAsList().isEmpty() && root.textContent.isNullOrBlank()
    }
    @get:JvmName("isNotEmptyXml")
    /**
     * Represents whether a collection or sequence is not empty.
     * Returns `true` if the collection or sequence contains at least one element.
     * Returns `false` if the collection or sequence is empty.
     * This property is the negation of `isEmpty`.
     * @since 6.1.0
     */
    val isNotEmpty: Boolean get() = !isEmpty

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
        tryOrThrow({ MalformedInputException("Input is not a valid XML") }) {
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
        tryOrThrow({ MalformedInputException(Xml::class) }) {
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
            val _ = parseDocument(this)
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
         * Converts the current `File` instance to an XML representation.
         * This function uses a type-safe operation to parse the file as TOML and transform it into XML format.
         *
         * @receiver the file to be converted
         * @return a result wrapped in an `Either` type, representing either a successful conversion
         *         or an `InvalidConversion` error if the operation fails
         * @since 6.1.0
         */
        fun File.toXml() = either {
            catching({ Xml(this@toXml) }) { t: Throwable ->
                InvalidTypeConversion(this@toXml, typeOf<File>(), typeOf<Xml>(), t)
            }
        }
        /**
         * Converts the current Path instance to its XML representation.
         *
         * This function attempts to parse the Path as TOML format and then transforms
         * it into an XML representation. If the conversion fails, an `InvalidConversion`
         * error is returned containing details about the failure.
         *
         * @return An `Either` instance representing the successful conversion to XML
         *         or an `InvalidConversion` error if the operation fails.
         * @since 6.1.0
         */
        fun Path.toXml() = either {
            catching({ Xml(this@toXml) }) { t: Throwable ->
                InvalidTypeConversion(this@toXml, typeOf<Path>(), typeOf<Xml>(), t)
            }
        }
        /**
         * Converts the current TOML string to its XML representation.
         *
         * This extension function parses the receiver string, which is expected to be in TOML format,
         * and converts it to an XML object. If the input is invalid or the conversion fails,
         * an error encapsulating the issue will be returned.
         *
         * @receiver The TOML string to be converted.
         * @return Either the parsed XML object or an error indicating the failure reason.
         * @since 6.1.0
         */
        fun @receiver:IJLanguage("XML") String.toXml() = either {
            catching({ Xml(this@toXml) }) { t: Throwable ->
                InvalidFormatOfType(this@toXml, typeOf<Xml>(), t)
            }
        }
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
         * Converts the current file into a pretty-printed XML representation.
         *
         * The method first converts the file content to XML and then applies
         * formatting to enhance readability.
         *
         * @return Either an error of type InvalidConversion if the file cannot
         *         be converted to XML, or a formatted Xml instance.
         * @since 6.1.0
         */
        fun File.toPrettyXml(): Either<InvalidTypeConversion, Xml> = toXml().map(Xml::pretty)
        /**
         * Converts the content of the given Path to a prettified XML representation if valid.
         *
         * @return Either an InvalidConversion error if the content cannot be parsed as XML, or a prettified Xml object.
         * @since 6.1.0
         */
        fun Path.toPrettyXml(): Either<InvalidTypeConversion, Xml> = toXml().map(Xml::pretty)
        /**
         * Converts the current XML string into a pretty-printed XML format.
         *
         * This method parses the receiver string as XML and, if successful, returns
         * a properly indented and human-readable version of the XML content.
         *
         * @receiver The XML string to be converted to a pretty-printed format.
         * @return Either an instance of InvalidFormat if the receiver string is not valid XML,
         *         or a pretty-printed Xml object on successful conversion.
         * @since 6.1.0
         */
        fun @receiver:IJLanguage("XML") String.toPrettyXml(): Either<InvalidFormatOfType, Xml> = toXml().map(Xml::pretty)
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
         * Reads and deserializes the content of a specified file into an instance of the specified type.
         *
         * This method uses a JSON deserializer to parse the file content and map it to the desired type.
         * Errors during the deserialization process are wrapped in a `DeserializationError`.
         *
         * Possible errors:
         * - [DeserializationError.ReadError] - if an error occurs during file reading
         * - [DeserializationError.MappingError] - if conversion failed
         * - [DeserializationError] - if any other error occurs during jackson deserialization
         *
         * @param file The file to be read and deserialized.
         * @return An instance of `Either` containing the deserialized object of type `T` if successful,
         *         or a `DeserializationError` if an error occurs during the reading or mapping process.
         * @since 6.1.0
         */
        inline fun <reified T> readFromFile(file: File): Either<DeserializationError, T> = either {
            catching({ MAPPER.readValue(file, T::class.java) }) { e: Exception -> when (e) {
                is StreamReadException -> DeserializationError.ReadError(typeOf<T>(), e)
                is DatabindException -> DeserializationError.MappingError(typeOf<T>(), e)
                is JacksonException -> DeserializationError(typeOf<T>(), e)
                else -> throw e
            } }
        }

        /**
         * Reads a list of objects of type [T] from the specified file and deserializes it using the configured object mapper.
         *
         * @param file The file from which the list is to be read.
         * @return An [Either] that contains the successfully deserialized list of type [T] if the operation succeeds,
         * or a [DeserializationError] if an error occurs during the deserialization process.
         * @since 6.1.0
         */
        inline fun <reified T> readListFromFile(file: File): Either<DeserializationError, T> = either {
            catching({ MAPPER.readValue(file, MAPPER.typeFactory.constructCollectionType(List::class.java, T::class.java)) }) { e: Exception -> when (e) {
                is StreamReadException -> DeserializationError.ReadError(typeOf<List<T>>(), e)
                is DatabindException -> DeserializationError.MappingError(typeOf<List<T>>(), e)
                is JacksonException -> DeserializationError(typeOf<List<T>>(), e)
                else -> throw e
            } }
        }

        /**
         * Reads a set of objects of type [T] from the specified file.
         *
         * This method deserializes the file content into a `Set` of type [T]
         * using a Jackson object mapper. In case of deserialization errors,
         * the error is wrapped in an `Either` as a `DeserializationError`.
         *
         * @param file The file to read and deserialize the content from.
         * @return An `Either` containing a `Set` of type [T] on success, or
         *         a `DeserializationError` in case of a failure.
         * @since 6.1.0
         */
        inline fun <reified T> readSetFromFile(file: File): Either<DeserializationError, Set<T>> = either {
            catching({ MAPPER.readValue(file, MAPPER.typeFactory.constructCollectionType(Set::class.java, T::class.java)) }) { e: Exception -> when (e) {
                is StreamReadException -> DeserializationError.ReadError(typeOf<Set<T>>(), e)
                is DatabindException -> DeserializationError.MappingError(typeOf<Set<T>>(), e)
                is JacksonException -> DeserializationError(typeOf<Set<T>>(), e)
                else -> throw e
            } }
        }

        /**
         * Reads a map from the specified file and deserializes its content into a map with `String` keys
         * and values of the specified generic type `T`.
         *
         * @param file The file to be read and deserialized into a map.
         * @return An `Either` containing a `DeserializationError` if deserialization fails,
         *         or a `Map<String, T>` if the operation is successful.
         * @since 6.1.0
         */
        fun <T> readMapFromFile(file: File): Either<DeserializationError, Map<String, T>> = either {
            catching({ MAPPER.readValue(file, object : TypeReference<Map<String, T>>() {}) }) { e: Exception -> when (e) {
                is StreamReadException -> DeserializationError.ReadError(typeOf<Map<String, T>>(), e)
                is DatabindException -> DeserializationError.MappingError(typeOf<Map<String, T>>(), e)
                is JacksonException -> DeserializationError(typeOf<Map<String, T>>(), e)
                else -> throw e
            } }
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

        /**
         * Converts the specified column in the table to the XML format.
         *
         * @param name The name of the column to be converted.
         * @since 5.5.0
         */
        fun Table.xml(name: String) = jsonb<Xml>(name)
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
     * Attempts to map the provided value to an object of the specified type T using the MAPPER.
     * If the mapping process fails due to a DatabindException, wraps the failure information into a MappingError.
     *
     * @return Either a successful result of type T or a DeserializationError.MappingError containing details of the failure.
     * @since 6.1.0
     */
    inline fun <reified T> toObject(): Either<DeserializationError.MappingError, T> = either {
        catching({ MAPPER.readValue<T>(value) as T }) { e: DatabindException ->
            DeserializationError.MappingError(typeOf<T>(), e.message)
        }
    }

    /**
     * Converts the data to an array of the specified type.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [DeserializationError] - if any other error occurs during jackson deserialization
     *
     * @return Either a deserialization error or an array of type T.
     * @since 6.1.0
     */
    inline fun <reified T> toArray(): Either<DeserializationError, Array<T>> = toList<T>().map { it.toTypedArray() }

    /**
     * Transforms the current object into an `Either` containing a list of deserialized objects of type `T`
     * or a `DeserializationError`. This function processes the document, matches children nodes with the
     * same name, and maps them to objects of the specified type.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [DeserializationError] - if any other error occurs during jackson deserialization
     *
     * @return An `Either<DeserializationError, List<T>>` where the right side contains a list of successfully
     * deserialized objects of type `T`, and the left side contains a `DeserializationError` in case of failure.
     * @since 6.1.0
     */
    inline fun <reified T> toList(): Either<DeserializationError, List<T>> = either {
        catching({
            val children = parseDocument(value).documentElement.childrenAsList()
            if (children.isEmpty()) emptyList()
            else {
                val firstName = children.first().nodeName
                children.filter { it.nodeName == firstName }.map { elementToObject<T>(it) }
            }
        }) { e: Exception -> when (e) {
            is DatabindException -> DeserializationError.MappingError(typeOf<T>(), e)
            is JacksonException -> DeserializationError(typeOf<T>(), e)
            else -> throw e
        } }
    }
    /**
     * Converts a collection or sequence of elements into a non-empty list of elements of the specified type [T].
     *
     * This function attempts to transform the elements into a non-empty list. If the conversion process fails for
     * any element, this function will capture the exception and return the result of the operation wrapped
     * in a Result type.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [DeserializationError] - if any other error occurs during jackson deserialization
     * - [IterableError.Empty] - if the resulting list is empty
     *
     * @param T The type of elements expected in the non-empty list.
     * @return A [Result] containing the successfully transformed list if all the elements are valid; otherwise,
     *         a failure if an exception is encountered during the transformation process.
     * @since 5.2.1
     */
    inline fun <reified T> toNonEmptyList(): Either<Error, NonEmptyList<T>> =
        toList<T>() as Either<Error, List<T>> thenEither { catching({ it.toNonEmptyList() }) { _: Throwable -> IterableError.Empty } }


    /**
     * Converts a list of type `T` into an `MList<T>` by attempting deserialization.
     * The result is wrapped in an `Either` type that represents either
     * the successful conversion with an `MList<T>` or a `DeserializationError`.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [DeserializationError] - if any other error occurs during jackson deserialization
     *
     * @return an `Either` containing a `DeserializationError` if deserialization
     *         fails, or an `MList<T>` if the conversion is successful.
     * @since 6.1.0
     */
    inline fun <reified T> toMList(): Either<DeserializationError, MList<T>> = toList<T>().map { it.toMList() }
    /**
     * Converts an instance into a non-empty mutable list of the specified type [T].
     *
     * This method wraps the conversion process in an `Either`, allowing safe handling of errors
     * during the transformation. The function first attempts to cast the object to an `MList` of type [T]
     * and then further validates its non-emptiness. If the list is empty or an exception occurs,
     * it returns an error encapsulated in an `Either`.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [DeserializationError] - if any other error occurs during jackson deserialization
     * - [IterableError.Empty] - if the resulting list is empty
     *
     * @param T The type of elements in the resulting non-empty mutable list.
     * @return An `Either` that contains an `MList` of type [T] if successful, or an appropriate `Error` otherwise.
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptyMList() =
        toMList<T>() as Either<Error, MList<T>> thenEither { catching({ it.toNonEmptyMList() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Converts a collection of collections into a list of sets, where each inner collection
     * is transformed into a set. The operation guarantees that duplicate elements
     * within each inner collection are removed, resulting in distinct elements for each set.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [DeserializationError] - if any other error occurs during jackson deserialization
     *
     * @return A new list where each element is a set containing the distinct elements
     *         of the corresponding original collection.
     * @since 6.1.0
     */
    inline fun <reified T> toSet() = toList<T>().map { it.toSet() }
    /**
     * Converts the current collection into a `NonEmptySet` wrapped in an `Either`.
     * If the collection is empty, returns an `Either` containing an error.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [DeserializationError] - if any other error occurs during jackson deserialization
     * - [IterableError.Empty] - if the resulting list is empty
     *
     * @return An `Either` containing a `NonEmptySet` if the collection is non-empty,
     * or an error if the collection is empty.
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptySet(): Either<Error, NonEmptySet<T>> =
        toSet<T>() as Either<Error, Set<T>> thenEither { catching({ it.toNonEmptySet() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Converts a deserialized list of type [T] into an [MSet] while encapsulating the result in an [Either].
     * This method applies the `toMSet` transformation on the resulting list.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [DeserializationError] - if any other error occurs during jackson deserialization
     *
     * @return An [Either] containing a [DeserializationError] if deserialization fails, or an [MSet] of type [T] if successful.
     * @since 6.1.0
     */
    inline fun <reified T> toMSet(): Either<DeserializationError, MSet<T>> = toList<T>().map { it.toMSet() }
    /**
     * Converts a collection into a `NonEmptyMSet` wrapped in an `Either` type.
     * The conversion ensures that the resulting multiset is non-empty. If the operation fails
     * (e.g., when the original collection is empty), it returns an `Error`.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [DeserializationError] - if any other error occurs during jackson deserialization
     * - [IterableError.Empty] - if the resulting list is empty
     *
     * @return `Either<Error, NonEmptyMSet<T>>` where the left side contains an `Error` if the
     *         conversion fails, and the right side contains a `NonEmptyMSet<T>` if the conversion succeeds.
     * @since 6.1.0
     */
    inline fun <reified T> toNonEmptyMSet(): Either<Error, NonEmptyMSet<T>> =
        toMSet<T>() as Either<Error, MSet<T>> thenEither { catching({ it.toNonEmptyMSet() }) { _: Throwable -> IterableError.Empty } }

    /**
     * Converts the current value to a `Map<String, V>` type using Jackson ObjectMapper.
     *
     * This function attempts to deserialize the provided value into a map where the keys are strings
     * and the values are of type `V`. It uses Jackson's `ObjectMapper` for the deserialization process
     * and captures any exceptions that may occur during this operation.
     *
     * @param V The type of the values in the resulting map.
     * @return A result type indicating either a successful `Map<String, V>` conversion
     *         or a deserialization error. The possible errors include:
     *         - [DeserializationError.ReadError] for issues during stream reading.
     *         - [DeserializationError.MappingError] for issues during data mapping.
     *         - [DeserializationError] for general Jackson-related exceptions.
     * @since 6.1.0
     */
    inline fun <reified V> toMap() = either {
        catching({ MAPPER.readValue(value, object : TypeReference<Map<String, V>>() {}) as Map<String, V> }) { e: Exception -> when (e) {
            is StreamReadException -> DeserializationError.ReadError(typeOf<Map<String, V>>(), e)
            is DatabindException -> DeserializationError.MappingError(typeOf<Map<String, V>>(), e)
            is JacksonException -> DeserializationError(typeOf<Map<String, V>>(), e)
            else -> throw e
        } }
    }
    /**
     * Converts the current structure into a non-empty map of type [Map<String, V>].
     * If the conversion is successful, it returns a `Right` containing the map.
     * If any exception is thrown during conversion or the resulting map is empty, it returns a `Left` with an appropriate error.
     *
     * Uses `toMap<V>()` for the conversion and ensures the resulting map is non-empty by wrapping it in an `Either` structure
     * with error handling for possible exceptions or empty results.
     *
     * @return An `Either` where the right value is a non-empty map and the left value represents an error.
     * @since 6.1.0
     */
    inline fun <reified V> toNonEmptyMap() =
        toMap<V>() as Either<Error, Map<String, V>> thenEither { catching({ it.toNonEmptyMap() }) { _: Exception -> IterableError.Empty } }

    /**
     * Converts the current value into a DataMap representation. This method utilizes the Jackson ObjectMapper
     * for deserialization and wraps the result in an Either type to handle potential errors.
     *
     * @return Either a successfully deserialized [DataMap] or a [DeserializationError] in case of a failure.
     * @since 6.1.0
     */
    fun toDataMap(): Either<DeserializationError, DataMap> = either {
        catching({ MAPPER.readValue(value, object : TypeReference<DataMap>() {}) as DataMap }) { e: Exception -> when (e) {
            is StreamReadException -> DeserializationError.ReadError(typeOf<DataMap>(), e)
            is DatabindException -> DeserializationError.MappingError(typeOf<DataMap>(), e)
            is JacksonException -> DeserializationError(typeOf<DataMap>(), e)
            else -> throw e
        } }
    }
    /**
     * Converts the current instance to a [NonEmptyDataMap] wrapped in an [Either].
     *
     * This method attempts to transform the existing DataMap into a [NonEmptyDataMap].
     * If the conversion fails or the resulting map is empty, an [Error] is returned.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if set is empty
     *
     * @return [Either] an [Error] if the conversion fails or the result is empty,
     *         or a successfully created [NonEmptyDataMap] instance.
     * @since 6.1.0
     */
    fun toNonEmptyDataMap(): Either<Error, NonEmptyDataMap> =
        toDataMap() as Either<Error, DataMap> thenEither { catching({ it.toNonEmptyMap() }) { _: Exception -> IterableError.Empty } }
    /**
     * Converts the current value into a `DataMapNN` instance, handling potential deserialization errors.
     *
     * This method attempts to deserialize the value into a `DataMapNN` object using a pre-configured
     * Jackson object mapper. If the deserialization succeeds, it returns the resulting `DataMapNN`
     * instance wrapped in an `Either.Right`. If deserialization fails, an appropriate `DeserializationError`
     * is returned wrapped in an `Either.Left`.
     *
     * @return `Either.Left` containing a `DeserializationError` if deserialization fails, or
     *         `Either.Right` containing a `DataMapNN` instance if deserialization is successful.
     * @since 6.1.0
     */
    fun toDataMapNN(): Either<DeserializationError, DataMapNN> = either {
        catching({ MAPPER.readValue(value, object : TypeReference<DataMapNN>() {}) as DataMapNN }) { e: Exception -> when (e) {
            is StreamReadException -> DeserializationError.ReadError(typeOf<DataMapNN>(), e)
            is DatabindException -> DeserializationError.MappingError(typeOf<DataMapNN>(), e)
            is JacksonException -> DeserializationError(typeOf<DataMapNN>(), e)
            else -> throw e
        } }
    }
    /**
     * Converts the current object into a `NonEmptyDataMapNN` wrapped in an `Either` type.
     * The method ensures that the resulting map is non-empty, and returns an appropriate error if this condition is not met.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if set is empty
     *
     * @return An instance of `Either` holding either an `Error` or a non-empty `NonEmptyDataMapNN`.
     * @since 6.1.0
     */
    fun toNonEmptyDataMapNN(): Either<Error, NonEmptyDataMapNN> =
        toDataMapNN() as Either<Error, DataMapNN> thenEither { catching({ it.toNonEmptyMap() }) { _: Exception -> IterableError.Empty } }

    /**
     * Converts the current map structure into a mutable map of type `Map<String, V>`.
     * The function is inline and reified, preserving the type `V` at runtime.
     * This transformation allows for further operations on the map in a mutable context.
     *
     * @param V The type of the values in the resulting map.
     * @return A mutable map transformed into the desired structure.
     * @since 6.1.0
     */
    inline fun <reified V> toMMap() = toMap<V>().map(Map<String, V>::toMMap)
    /**
     * Converts the current structure into a `NonEmptyMMap<String, V>`, wrapped in an `Either` type for error handling.
     *
     * This method attempts to transform the current structure into a non-empty multimap.
     * If the structure is empty or an error occurs during transformation, it returns an `Error` encapsulated in the `Either` type.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if set is empty
     *
     * @return An `Either` containing either an `Error` in case of failure or a successfully created `NonEmptyMMap<String, V>`.
     * @since 6.1.0
     */
    inline fun <reified V> toNonEmptyMMap(): Either<Error, NonEmptyMMap<String, V>> =
        toMMap<V>() as Either<Error, MMap<String, V>> thenEither { catching({ it.toNonEmptyMMap() }) { _: Exception -> IterableError.Empty } }

    /**
     * Converts a given value to a `DataMMap` instance by attempting to deserialize it
     * using the predefined object mapper. In case of a failure during deserialization,
     * an appropriate `DeserializationError` is returned.
     *
     * @return An `Either` instance containing `DataMMap` on successful deserialization
     * or `DeserializationError` on failure.
     * @since 6.1.0
     */
    fun toDataMMap(): Either<DeserializationError, DataMMap> = either {
        catching({ MAPPER.readValue(value, object : TypeReference<DataMMap>() {}) as DataMMap }) { e: Exception -> when (e) {
            is StreamReadException -> DeserializationError.ReadError(typeOf<DataMMap>(), e)
            is DatabindException -> DeserializationError.MappingError(typeOf<DataMMap>(), e)
            is JacksonException -> DeserializationError(typeOf<DataMMap>(), e)
            else -> throw e
        } }
    }
    /**
     * Converts the current instance into a `NonEmptyDataMMap`.
     * This method attempts to transform the data structure, ensuring it is non-empty.
     * If the transformation is unsuccessful, an error is returned.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if set is empty
     *
     * @return An `Either` containing an `Error` if the conversion fails, or a `NonEmptyDataMMap` if it succeeds.
     * @since 6.1.0
     */
    fun toNonEmptyDataMMap(): Either<Error, NonEmptyDataMMap> =
        toDataMMap() as Either<Error, DataMMap> thenEither { catching({ it.toNonEmptyMMap() }) { _: Exception -> IterableError.Empty } }
    /**
     * Converts the current object into a `DataMMapNN` instance.
     *
     * This method attempts to deserialize the current object into a `DataMMapNN` type
     * using the Jackson library. If the deserialization process encounters an error,
     * the method will return a `DeserializationError` indicating the specific issue.
     *
     * @return An `Either` containing either a successfully deserialized `DataMMapNN` instance
     * or a `DeserializationError` describing the failure encountered during the deserialization process.
     * @since 6.1.0
     */
    fun toDataMMapNN(): Either<DeserializationError, DataMMapNN> = either {
        catching({ MAPPER.readValue(value, object : TypeReference<DataMMapNN>() {}) as DataMMapNN }) { e: Exception -> when (e) {
            is StreamReadException -> DeserializationError.ReadError(typeOf<DataMMapNN>(), e)
            is DatabindException -> DeserializationError.MappingError(typeOf<DataMMapNN>(), e)
            is JacksonException -> DeserializationError(typeOf<DataMMapNN>(), e)
            else -> throw e
        } }
    }
    /**
     * Converts the current object into a `NonEmptyDataMMapNN` wrapped in an `Either` type.
     * This method attempts to transform the object into a `DataMMapNN`, then ensures
     * that the resulting map is non-empty.
     *
     * Possible errors:
     * - [DeserializationError.MappingError] - if conversion failed
     * - [IterableError.Empty] - if set is empty
     *
     * @return An `Either` containing an `Error` in case of failure, or a `NonEmptyDataMMapNN` upon successful transformation.
     * @since 6.1.0
     */
    fun toNonEmptyDataMMapNN(): Either<Error, NonEmptyDataMMapNN> =
        toDataMMapNN() as Either<Error, DataMMapNN> thenEither { catching({ it.toNonEmptyMMap() }) { _: Exception -> IterableError.Empty } }

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

            current = if (index != null) {
                node.childrenAsList().getOrNull(index)
            } else {
                val next = segments.getOrNull(i + 1)?.toIntOrNull()
                if (next != null) {
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
        getAsNode(dotPath, regexSeparator) != null

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
     * Operator form of [isEmpty].
     *
     * @since 3.9.0
     */
    operator fun not(): Boolean = isEmpty

    // --- PATCH OPERATIONS ---

    /**
     * Applies a JSON Merge Patch (RFC 7386) to this XML and returns the result.
     * The patch is expressed as [Json] since there is no standard XML merge-patch spec.
     *
     * @param patch the JSON merge patch to apply.
     * @return the patched [Xml].
     * @since 6.1.0
     */
    infix fun mergePatch(patch: Json): Xml = toJson().mergePatch(patch).toXml(rootName)

    /**
     * Applies a JSON Merge Patch to this XML, using another [Xml] as patch.
     *
     * @since 6.1.0
     */
    infix fun mergePatch(patch: Xml): Xml = mergePatch(patch.toJson())

    /**
     * Applies a JSON Merge Patch to this XML, using a [Yaml] as patch.
     *
     * @since 6.1.0
     */
    @OptIn(Beta::class)
    infix fun mergePatch(patch: Yaml): Xml = mergePatch(patch.toJson())

    /**
     * Applies a JSON Patch (RFC 6902) to this XML and returns the result.
     *
     * Possible erros:
     * - [InvalidFormatOfType] - if the patch is not a JSON array or a path is invalid.
     * - [RequiredProperty] - if a required property is missing in the patch.
     * - [XmlError.PathNotFound] - if the path specified in the patch does not exist in the target JSON.
     * - [IllegalOperation] - if you're trying to move a node into its own children.
     * - [ValidationError.ExpectationMismatch] - if the path specified in the patch does not match the expected value.
     * - [UnsupportedOperation] - if an unsupported operation is encountered in the patch.
     *
     * @param patch the JSON patch to apply.
     * @since 6.1.0
     */
    infix fun xmlPatch(patch: Json) = toJson().jsonPatch(patch).map { it.toXml(rootName) }.mapLeft { e ->
        e.letIf(e is JsonError.PathNotFound) { XmlError.PathNotFound(e.path) }
    }

    /**
     * Applies a JSON Patch (RFC 6902) using another [Xml] as patch.
     *
     * Possible erros:
     * - [InvalidFormatOfType] - if the patch is not a JSON array or a path is invalid.
     * - [RequiredProperty] - if a required property is missing in the patch.
     * - [XmlError.PathNotFound] - if the path specified in the patch does not exist in the target JSON.
     * - [IllegalOperation] - if you're trying to move a node into its own children.
     * - [ValidationError.ExpectationMismatch] - if the path specified in the patch does not match the expected value.
     * - [UnsupportedOperation] - if an unsupported operation is encountered in the patch.
     *
     * @since 6.1.0
     */
    infix fun xmlPatch(patch: Xml) = xmlPatch(patch.toJson()).mapLeft { e ->
        e.letIf(e is JsonError.PathNotFound) { XmlError.PathNotFound(e.path) }
    }

    /**
     * Applies a JSON Patch (RFC 6902) using a [Yaml] as patch.
     *
     * Possible erros:
     * - [InvalidFormatOfType] - if the patch is not a JSON array or a path is invalid.
     * - [RequiredProperty] - if a required property is missing in the patch.
     * - [XmlError.PathNotFound] - if the path specified in the patch does not exist in the target JSON.
     * - [IllegalOperation] - if you're trying to move a node into its own children.
     * - [ValidationError.ExpectationMismatch] - if the path specified in the patch does not match the expected value.
     * - [UnsupportedOperation] - if an unsupported operation is encountered in the patch.
     *
     * @since 6.1.0
     */
    @OptIn(Beta::class)
    infix fun xmlPatch(patch: Yaml) = xmlPatch(patch.toJson()).mapLeft { e ->
        e.letIf(e is JsonError.PathNotFound) { XmlError.PathNotFound(e.path) }
    }

    // --- XSLT ---

    /**
     * Transforms the given XML using the specified XSLT configuration.
     *
     * Possible erros:
     * - [XmlError.InvalidXsltConfig] - if the XSLT configuration is invalid.
     * - [XmlError.XsltTransfomationFailed] - if the transformation fails.
     *
     * @param xslt The XSLT configuration used for transformation.
     * @return Either an XmlError representing a failure during transformation, or the transformed XML string.
     * @since 6.1.0
     */
    infix fun transform(xslt: Xml): Either<XmlError, String> = either {
        val transformer = catching({
            TRANSFORMER_FACTORY.newTransformer(StreamSource(StringReader(xslt.value)))
        }) { e: Exception -> XmlError.InvalidXsltConfig(e) }
        val writer = StringWriter()
        catching({
            transformer.transform(StreamSource(StringReader(value)), StreamResult(writer))
        }) { e: Exception -> XmlError.XsltTransfomationFailed(e) }
        writer.toString()
    }

    /**
     * Transforms the given XML using the specified XSLT configuration.
     *
     * Possible erros:
     * - [XmlError.InvalidXsltConfig] - if the XSLT configuration is invalid.
     * - [XmlError.XsltTransfomationFailed] - if the transformation fails.
     * - [InvalidFormatOfType] - if produced an invalid XML.
     *
     * @param xslt The XSLT configuration used for transformation.
     * @return Either an XmlError representing a failure during transformation, or the transformed XML string.
     * @since 6.1.0
     */
    @Suppress("UNCHECKED_CAST")
    infix fun transformToXml(xslt: Xml) = transform(xslt) as Either<Error, String> thenMergeWith { it.toXml() }

    // --- XSD VALIDATION ---

    /**
     * Validates the XML instance using the provided XML Schema Definition (XSD).
     *
     * This method uses the W3C XML Schema (XSD) to validate the structure and content
     * of the current XML object. If validation fails, it returns an appropriate error
     * wrapped in an Either type.
     *
     * Possible errors:
     * - [XmlError.SchemaValidationFailed] - if the validation fails.
     * - [InvalidFormatOfType] - if produced an invalid XML.
     * - [GenericError] - if an unexpected error occurs.
     *
     * @param xsd The XSD used to validate the current XML object.
     * @since 6.1.0
     */
    infix fun validateWithSchema(xsd: Xml) = either {
        val factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).apply {
            try {
                setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, String.EMPTY)
                setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, String.EMPTY)
            } catch (_: Exception) { /* ignore */ }
        }
        val schema = factory.newSchema(StreamSource(StringReader(xsd.value)))
        val validator = schema.newValidator()
        catching({ validator.validate(StreamSource(StringReader(value))) }) { e: Exception -> when (e) {
            is IllegalArgumentException -> XmlError.SchemaValidationFailed(e.message)
            is SAXException -> InvalidFormatOfType(xsd, typeOf<Xml>(), e.message)
            else -> GenericError
        } }
        this@Xml
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