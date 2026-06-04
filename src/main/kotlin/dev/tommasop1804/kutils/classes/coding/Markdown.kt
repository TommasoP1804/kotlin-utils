/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("java_integer_as_kotlin_int")
@file:OptIn(Beta::class)

package dev.tommasop1804.kutils.classes.coding

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.time.*
import dev.tommasop1804.kutils.classes.time.Duration.Companion.asMinutesOfDuration
import dev.tommasop1804.kutils.exceptions.*
import jakarta.persistence.AttributeConverter
import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.File
import java.nio.file.Path
import org.intellij.lang.annotations.Language as IJLanguage

/**
 * The `Markdown` class is a representation of Markdown-encoded text. Unlike the structured data formats of the
 * coding family (`Json`, `Yaml`, `Toml`, `Xml`, `Csv`), Markdown is a prose-oriented markup language, so the
 * class focuses on *analysing* and *manipulating* the document rather than mapping it to objects, lists or maps.
 *
 * Features include:
 * - Inspection of the document structure: headings (and the document title), links, images, fenced code blocks,
 *   inline code, task-list items, block quotes and tables.
 * - YAML front matter detection and extraction.
 * - Derived metrics such as word count, line count and an estimated reading time.
 * - Generation of a table of contents and extraction of the content under a given heading.
 * - Conversion to plain text, comment stripping and writing to files.
 *
 * As Markdown is an intentionally permissive format, virtually any string is a valid Markdown document; the
 * primary constructor therefore performs no structural validation. Heading detection is ATX-style (`#` … `######`)
 * and is fence-aware (headings inside fenced code blocks are ignored).
 *
 * The class is compatible with the JSON serialization libraries used across the project; serialization treats the
 * document as an opaque string (there is no meaningful structural mapping to JSON).
 *
 * @param value The raw Markdown content as a string.
 * @constructor Creates an instance of the Markdown class with the given string content.
 * @since 4.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Markdown.Companion.Serializer::class)
@JsonDeserialize(using = Markdown.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Markdown.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Markdown.Companion.OldDeserializer::class)
@Suppress("unused", "UNCHECKED_CAST", "kutils_collection_declaration")
@MustUseReturnValues
class Markdown(@param:IJLanguage("Markdown") override var value: String) : CharSequence, Code(value, Language.Markdown) {

    /**
     * The YAML front matter block delimited by a leading `---` fence, or `null` when the document has none.
     * The returned string is the raw YAML content between the fences (the fences themselves are excluded).
     *
     * @since 4.0.0
     */
    val frontMatter: String? get() = stripFrontMatter().first
    /**
     * Indicates whether the document begins with a YAML front matter block.
     *
     * @since 4.0.0
     */
    val hasFrontMatter: Boolean get() = stripFrontMatter().first.isNotNull()
    /**
     * The document body, i.e. the content with any leading YAML front matter removed.
     *
     * @since 4.0.0
     */
    val body: Markdown get() = Markdown(stripFrontMatter().second)

    /**
     * All ATX headings (`#` … `######`) found in the document body, in document order. Headings located inside
     * fenced code blocks are not reported.
     *
     * @since 4.0.0
     */
    val headings: List<Heading> get() = scan().headings
    /**
     * The document title: the text of the first level-1 heading (`#`) if present, otherwise the text of the first
     * heading of any level, or `null` when the document contains no headings.
     *
     * @since 4.0.0
     */
    val title: String? get() = headings.firstOrNull { it.level == 1 }?.text ?: headings.firstOrNull()?.text

    /**
     * All inline links `[text](url)` in the document body, excluding any that appear inside code spans or
     * fenced code blocks.
     *
     * @since 4.0.0
     */
    val links: List<Link>
        get() = LINK_REGEX.findAll(scan().textOutsideCode.replace(INLINE_CODE_REGEX, " "))
            .map { Link(it.groupValues[1], it.groupValues[2], it.groupValues[3].ifBlank { null }) }
            .toList()
    /**
     * All images `![alt](url)` in the document body, excluding any that appear inside code spans or fenced code
     * blocks.
     *
     * @since 4.0.0
     */
    val images: List<Image>
        get() = IMAGE_REGEX.findAll(scan().textOutsideCode.replace(INLINE_CODE_REGEX, " "))
            .map { Image(it.groupValues[1], it.groupValues[2], it.groupValues[3].ifBlank { null }) }
            .toList()

    /**
     * All fenced code blocks in the document body, in document order.
     *
     * @since 4.0.0
     */
    val codeBlocks: List<CodeBlock> get() = scan().codeBlocks
    /**
     * All inline code spans `` `code` `` in the document body.
     *
     * @since 4.0.0
     */
    val inlineCode: List<String>
        get() = INLINE_CODE_REGEX.findAll(stripFrontMatter().second).map { it.groupValues[1] }.toList()

    /**
     * All task-list items in the document body, in document order.
     *
     * @since 4.0.0
     */
    val tasks: List<Task> get() = scan().tasks
    /**
     * The text of every block quote line (with the leading `>` markers removed) in the document body.
     *
     * @since 4.0.0
     */
    val blockquotes: List<String> get() = scan().blockquotes

    /**
     * Indicates whether the document body contains at least one GitHub-flavoured Markdown table, detected by the
     * presence of a delimiter row (a line composed solely of `|`, `-`, `:` and whitespace, containing both a pipe
     * and a dash).
     *
     * @since 4.0.0
     */
    val hasTables: Boolean
        get() = stripFrontMatter().second.lines().any {
            val t = it.trim()
            t.contains('|') && t.contains('-') && t.replace(Regex("[\\s|:-]"), "").isEmpty()
        }

    /**
     * The number of words in the document, computed from its plain-text representation
     * (see [toPlainText]).
     *
     * @since 4.0.0
     */
    val wordCount: Int get() = toPlainText().split(Regex("\\s+")).count { it.isNotBlank() }
    /**
     * The total number of lines in the raw document content.
     *
     * @since 4.0.0
     */
    val lineCount: Int get() = value.lines().size
    /**
     * An estimated reading time, assuming an average reading speed of 200 words per minute
     * (rounded up, with a minimum of one minute for any non-empty document).
     *
     * @since 4.0.0
     */
    val estimatedReadingTime: Duration get() = (if (wordCount == 0) 0 else (wordCount + 199) / 200).asMinutesOfDuration()

    /**
     * Secondary constructor that initializes an instance using a [Code] object.
     *
     * @param code The [Code] object containing the value to initialize.
     * @since 4.0.0
     */
    constructor(code: Code) : this(code.value) {
        code.language.expect(Language.Markdown)
    }

    /**
     * Creates an instance by reading the content of the specified file and passing it as a
     * parameter to the primary constructor.
     *
     * @param file The file whose content will be read and used to initialize the instance.
     * @since 4.0.0
     */
    constructor(file: File) : this(file.readText()) {
        file.exists().expect(true)
        file.isFile.expect(true)
        file.canRead().expect(true)
        file.extension.validate(file::extension, "file") { it equalsIgnoreCase "md" || it equalsIgnoreCase "markdown" }
    }
    /**
     * Creates an instance by reading the content of the file at the specified path and passing it as a
     * parameter to the primary constructor.
     *
     * @param path The path of the file whose content will be read and used to initialize the instance.
     * @since 4.0.0
     */
    constructor(path: Path) : this(path.toFile())

    companion object {
        private val LINK_REGEX = Regex("(?<!!)\\[([^]]*)]\\(\\s*([^)\\s]+)(?:\\s+\"([^\"]*)\")?\\s*\\)")
        private val IMAGE_REGEX = Regex("!\\[([^]]*)]\\(\\s*([^)\\s]+)(?:\\s+\"([^\"]*)\")?\\s*\\)")
        private val INLINE_CODE_REGEX = Regex("`([^`\\n]+)`")
        private val FENCE_REGEX = Regex("^\\s{0,3}(`{3,}|~{3,})\\s*([^`\\s]*)\\s*$")
        private val ATX_REGEX = Regex("^\\s{0,3}(#{1,6})\\s+(.*?)\\s*#*\\s*$")
        private val TASK_REGEX = Regex("^\\s*[-*+]\\s+\\[([ xX])]\\s+(.*)$")
        private val FRONT_MATTER_REGEX = Regex("^---\\r?\\n(.*?)\\r?\\n(?:---|\\.\\.\\.)[ \\t]*\\r?\\n?", RegexOption.DOT_MATCHES_ALL)

        private fun slugify(text: String): String = text
            .lowercase()
            .replace(Regex("[^\\w\\s-]"), "")
            .trim()
            .replace(Regex("\\s+"), "-")
            .replace(Regex("-+"), "-")

        /**
         * Checks if the String represents a valid Markdown document.
         *
         * As Markdown is an intentionally permissive format, the construction essentially always succeeds; the
         * method is provided for API symmetry with the rest of the coding family.
         *
         * @receiver The String to be validated as Markdown.
         * @return A `Result` wrapping the parsed Markdown object on success, or an exception otherwise.
         * @since 4.0.0
         */
        fun String.isValidMarkdown() = runCatching { Markdown(this) }

        /**
         * Converts the current file to a `Markdown` instance encapsulated within a `Result`.
         *
         * @receiver The file whose content is to be parsed as Markdown.
         * @return A `Result` wrapping the `Markdown` instance if the parsing succeeds, or an exception if it fails.
         * @since 4.0.0
         */
        fun File.toMarkdown() = runCatching { Markdown(this) }
        /**
         * Converts the content of the given file path to its Markdown representation, wrapped in a `Result`.
         *
         * @receiver The file path to be read and converted into Markdown.
         * @return A `Result` containing the Markdown representation of the file content, or an error if it fails.
         * @since 4.0.0
         */
        fun Path.toMarkdown() = runCatching { Markdown(this) }
        /**
         * Converts the current `String` into a Markdown representation and wraps the operation in a `Result`.
         *
         * @receiver The `String` to be converted to Markdown.
         * @return A `Result` that either contains the parsed Markdown object or an exception if parsing fails.
         * @since 4.0.0
         */
        fun @receiver:IJLanguage("Markdown") String.toMarkdown() = runCatching { Markdown(this) }
        /**
         * Converts the current instance of `Code` to a `Markdown` object if the language is Markdown.
         * If the language is not Markdown, an [ExpectationMismatchException] is thrown (into the result).
         *
         * @receiver The `Code` instance that contains the details necessary for conversion.
         * @return A `Result` wrapping the resulting `Markdown` instance or an exception if the conversion fails.
         * @since 4.0.0
         */
        fun Code.toMarkdown() = runCatching {
            if (language == Language.Markdown) Markdown(value)
            else throw ExpectationMismatchException("Language must be Markdown")
        }

        /**
         * Reads the content of the specified file and parses it into a Markdown object.
         *
         * @param file the file to be read and parsed as Markdown.
         * @return a Result containing the parsed Markdown object if the operation is successful,
         *         or an exception if an error occurs.
         * @since 4.0.0
         */
        fun readFromFile(file: File): Result<Markdown> = runCatching { Markdown(file) }

        class Serializer : ValueSerializer<Markdown>() {
            override fun serialize(value: Markdown, gen: JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<Markdown>() {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = Markdown(p.string)
        }

        class OldSerializer : JsonSerializer<Markdown>() {
            override fun serialize(value: Markdown, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.value)
            }
        }

        class OldDeserializer : JsonDeserializer<Markdown>() {
            override fun deserialize(p: com.fasterxml.jackson.core.JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext) = Markdown(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Markdown?, String?> {
            override fun convertToDatabaseColumn(attribute: Markdown?) = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?) = dbData?.let { Markdown(it) }
        }
    }

    private fun stripFrontMatter(): Pair<String?, String> {
        val match = FRONT_MATTER_REGEX.find(value)
        return if (match != null && match.range.first == 0) {
            match.groupValues[1] to value.substring(match.range.last + 1)
        } else null to value
    }

    private fun scan(): Scan {
        val headings = mutableListOf<Heading>()
        val codeBlocks = mutableListOf<CodeBlock>()
        val tasks = mutableListOf<Task>()
        val blockquotes = mutableListOf<String>()
        val outside = StringBuilder()

        var fence: String? = null
        var fenceLang = ""
        val buffer = StringBuilder()

        for (line in stripFrontMatter().second.lines()) {
            val fenceMatch = FENCE_REGEX.find(line)
            val open = fence
            if (open == null) {
                if (fenceMatch != null) {
                    fence = fenceMatch.groupValues[1]
                    fenceLang = fenceMatch.groupValues[2]
                    buffer.clear()
                    continue
                }
                ATX_REGEX.find(line)?.let {
                    val text = it.groupValues[2].trim()
                    headings += Heading(it.groupValues[1].length, text, slugify(text))
                }
                TASK_REGEX.find(line)?.let {
                    tasks += Task(it.groupValues[1].equals("x", ignoreCase = true), it.groupValues[2].trim())
                }
                if (line.trimStart().startsWith(">")) blockquotes += line.trimStart().removePrefix(">").trim()
                outside.appendLine(line)
            } else {
                val closing = fenceMatch != null &&
                        fenceMatch.groupValues[1].first() == open.first() &&
                        fenceMatch.groupValues[1].length >= open.length &&
                        fenceMatch.groupValues[2].isEmpty()
                if (closing) {
                    codeBlocks += CodeBlock(fenceLang.ifBlank { null }?.toEnumConst(), buffer.toString().trimEnd('\n'))
                    fence = null
                } else {
                    buffer.appendLine(line)
                }
            }
        }
        if (fence.isNotNull()) codeBlocks += CodeBlock(fenceLang.ifBlank { null }?.toEnumConst(), buffer.toString().trimEnd('\n'))
        return Scan(headings, codeBlocks, tasks, blockquotes, outside.toString())
    }

    /**
     * Checks if the content of the Markdown document is empty.
     *
     * This method evaluates whether the document, after removing any front matter,
     * contains only blank content. It does not consider the presence of front matter
     * itself as part of the document's content.
     *
     * @return `true` if the document has no meaningful content other than front matter,
     *         or if the content is entirely blank; otherwise, `false`.
     * @since 4.0.0
     */
    fun isEmptyMarkdown() = stripFrontMatter().second.isBlank()

    /**
     * Extracts the content located under the first heading whose text matches [title] (case-insensitive), down to
     * (but not including) the next heading of the same or a higher level.
     *
     * @param title The heading text to search for.
     * @return A `Markdown` instance with the section content, or `null` when no matching heading is found.
     * @since 4.0.0
     */
    infix fun section(title: String): Markdown? {
        val collected = mutableListOf<String>()
        var startLevel = -1
        var collecting = false
        var fence: String? = null

        for (line in stripFrontMatter().second.lines()) {
            val fenceMatch = FENCE_REGEX.find(line)
            if (fence == null) {
                if (fenceMatch != null) {
                    fence = fenceMatch.groupValues[1]
                    if (collecting) collected += line
                    continue
                }
                val heading = ATX_REGEX.find(line)
                if (heading != null) {
                    val level = heading.groupValues[1].length
                    val text = heading.groupValues[2].trim()
                    if (!collecting) {
                        if (text.equals(title, ignoreCase = true)) {
                            collecting = true
                            startLevel = level
                        }
                        continue
                    } else if (level <= startLevel) {
                        break
                    }
                }
            } else if (fenceMatch != null &&
                fenceMatch.groupValues[1].first() == fence.first() &&
                fenceMatch.groupValues[1].length >= fence.length &&
                fenceMatch.groupValues[2].isEmpty()
            ) {
                fence = null
            }
            if (collecting) collected += line
        }
        return if (collecting) Markdown(collected.joinToString("\n").trim()) else null
    }

    /**
     * Generates a Markdown table of contents from the document headings, as a nested bullet list of anchor links.
     *
     * @param maxLevel The deepest heading level to include (inclusive). Defaults to `3`.
     * @return A `Markdown` instance containing the generated table of contents.
     * @since 4.0.0
     */
    fun toc(maxLevel: Int = 3): Markdown {
        val included = headings.filter { it.level <= maxLevel }
        val minLevel = included.minOfOrNull { it.level } ?: 1
        val toc = included.joinToString("\n") {
            "${"  ".repeat(it.level - minLevel)}- [${it.text}](#${it.slug})"
        }
        return Markdown(toc)
    }

    /**
     * Produces a best-effort plain-text representation of the document by stripping front matter, fenced and inline
     * code, images, link syntax (keeping the link text), heading and list markers, block quote markers, emphasis,
     * strikethrough, horizontal rules and HTML comments.
     *
     * @return The plain-text content of the document.
     * @since 4.0.0
     */
    fun toPlainText(): String {
        var t = stripFrontMatter().second
        t = t.replace(Regex("(?m)^\\s{0,3}(`{3,}|~{3,}).*?\\n([\\s\\S]*?)\\n\\s{0,3}\\1\\s*$"), "$2")
        t = t.replace(Regex("<!--[\\s\\S]*?-->"), "")
        t = t.replace(INLINE_CODE_REGEX, "$1")
        t = t.replace(IMAGE_REGEX, "")
        t = t.replace(LINK_REGEX, "$1")
        t = t.replace(Regex("(?m)^\\s{0,3}#{1,6}\\s+"), "")
        t = t.replace(Regex("(?m)^\\s*>\\s?"), "")
        t = t.replace(Regex("(?m)^\\s*[-*+]\\s+\\[[ xX]]\\s+"), "")
        t = t.replace(Regex("(?m)^\\s*[-*+]\\s+"), "")
        t = t.replace(Regex("(?m)^\\s*\\d+\\.\\s+"), "")
        t = t.replace(Regex("(?m)^\\s{0,3}([-*_])(\\s*\\1){2,}\\s*$"), "")
        t = t.replace(Regex("(\\*\\*|__)(.*?)\\1"), "$2")
        t = t.replace(Regex("(?<!\\w)([*_])(.+?)\\1(?!\\w)"), "$2")
        t = t.replace(Regex("~~(.*?)~~"), "$1")
        return t.lines().joinToString("\n") { it.trim() }.replace(Regex("\\n{3,}"), "\n\n").trim()
    }

    /**
     * Removes HTML comments (`<!-- … -->`) from the document content, mutating this instance in place.
     *
     * @since 4.0.0
     */
    fun removeComments() {
        value -= Regex("<!--[\\s\\S]*?-->")
    }

    /**
     * Writes the raw Markdown content of this instance to the specified file.
     *
     * @param file The destination file.
     * @since 4.0.0
     */
    fun writeToFile(file: File) = file.writeText(value)

    /**
     * Creates a new `Markdown` by appending the given content to this document, separated by a single newline.
     *
     * @param other The Markdown text to append.
     * @return A new `Markdown` instance with the combined content.
     * @since 4.0.0
     */
    operator fun plus(@IJLanguage("Markdown") other: String): Markdown = Markdown("$value\n$other")
    /**
     * Creates a new `Markdown` by appending another document to this one, separated by a blank line.
     *
     * @param other The `Markdown` document to append.
     * @return A new `Markdown` instance with the combined content.
     * @since 4.0.0
     */
    operator fun plus(other: Markdown): Markdown = Markdown("$value\n\n${other.value}")

    /**
     * Returns the raw Markdown content of this instance.
     *
     * @return The string representation of the document.
     * @since 4.0.0
     */
    override fun toString(): String = value

    /**
     * Checks whether the document contains a heading whose text matches the given value (case-insensitive).
     *
     * @param heading The heading text to look for.
     * @return `true` if a matching heading exists, `false` otherwise.
     * @since 4.0.0
     */
    operator fun contains(heading: String): Boolean = headings.any { it.text.equals(heading, ignoreCase = true) }

    /**
     * Represents a single ATX heading parsed from the document.
     *
     * @property level The heading level, from `1` (`#`) to `6` (`######`).
     * @property text The textual content of the heading, with the leading hashes and any trailing hashes removed.
     * @property slug A GitHub-style anchor slug derived from [text] (lowercased, non-word characters removed,
     *               whitespace collapsed to hyphens).
     * @since 4.0.0
     */
    data class Heading(val level: Int, val text: String, val slug: String)

    /**
     * Represents an inline link `[text](url "title")`.
     *
     * @property text The visible link text.
     * @property url The link destination.
     * @property title The optional link title, or `null` when absent.
     * @since 4.0.0
     */
    data class Link(val text: String, val url: String, val title: String? = null)

    /**
     * Represents an image `![alt](url "title")`.
     *
     * @property alt The alternative text of the image.
     * @property url The image source.
     * @property title The optional image title, or `null` when absent.
     * @since 4.0.0
     */
    data class Image(val alt: String, val url: String, val title: String? = null)

    /**
     * Represents a fenced code block delimited by ```` ``` ```` or `~~~`.
     *
     * @property language The info string declared after the opening fence (e.g. `kotlin`), or `null` when none.
     * @property content The raw content of the block, without the surrounding fences.
     * @since 4.0.0
     */
    data class CodeBlock(val language: Language?, val content: String)

    /**
     * Represents a task-list item `- [ ] text` / `- [x] text`.
     *
     * @property checked `true` when the item is marked as done (`[x]`), `false` otherwise (`[ ]`).
     * @property text The textual content of the task item.
     * @since 4.0.0
     */
    data class Task(val checked: Boolean, val text: String)

    private data class Scan(
        val headings: List<Heading>,
        val codeBlocks: List<CodeBlock>,
        val tasks: List<Task>,
        val blockquotes: List<String>,
        val textOutsideCode: String
    )
}