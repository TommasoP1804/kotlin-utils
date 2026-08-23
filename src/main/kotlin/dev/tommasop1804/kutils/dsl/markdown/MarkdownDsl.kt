/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")
@file:Since("3.3.0")

package dev.tommasop1804.kutils.dsl.markdown

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.classes.coding.Markdown.Companion.toMarkdown
import org.intellij.lang.annotations.Language

@DslMarker
annotation class MarkdownDslMarker

// --- BLOCK ELEMENTS ---

/**
 * Represents a Markdown element that can be rendered into a Markdown-compatible string.
 *
 * Implementations of this interface define specific types of Markdown elements, such as headings,
 * paragraphs, lists, and other block or inline structures. Each implementation is responsible
 * for defining how the element should be rendered into Markdown format.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
sealed interface MdElement {
    /**
     * Renders the content and returns the result as a string.
     *
     * @return A string representation of the rendered content.
     * @since 3.3.0
     */
    fun render(): String
}

/**
 * Represents a markdown heading element with a specified level and text content.
 *
 * @property level The level of the heading, where 1 represents the topmost heading (#),
 * and higher numbers represent subsequent heading levels (e.g., ##, ###, etc.).
 * @property text The text content of the heading.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdHeading(val level: Int, val text: String) : MdElement {
    /**
     * Transforms the Markdown heading into its string representation.
     *
     * The method generates a Markdown heading by repeating the '#' character
     * based on the heading's level, followed by a space and the heading's text content.
     *
     * @return the string representation of the Markdown heading
     * @since 3.3.0
     */
    override fun render() = "${"#".repeat(level)} $text"
}

/**
 * Represents a Markdown paragraph, which is a container for inline elements.
 *
 * @property segments A list of inline elements that make up the paragraph.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdParagraph(val segments: List<MdInline>) : MdElement {
    /**
     * Renders the paragraph by concatenating the rendered output of its inline segments.
     *
     * This method iterates through all inline elements in the `segments` list,
     * invokes their respective `render` methods, and combines the results into a single string.
     *
     * @return a string representation of the paragraph, formed by the combined rendered output
     *         of its inline elements.
     * @since 3.3.0
     */
    override fun render() = segments.joinToString("") { it.render() }
}

/**
 * Represents a Markdown code block element.
 *
 * This class is used to create a code block in Markdown syntax for a specified programming language.
 * The block includes both the language declaration and the code content.
 *
 * @param language The programming language or identifier for the code block.
 * @param code The actual code content to be rendered within the Markdown code block.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdCodeBlock(val language: dev.tommasop1804.kutils.classes.coding.Language? = null, val code: Code) : MdElement {
    /**
     * Renders the content of a Markdown code block by formatting it according to the specified language.
     *
     * This method wraps the code content within Markdown code block syntax using triple backticks
     * and the declared programming language. Any trailing whitespace in the code string is trimmed.
     *
     * @return A properly formatted Markdown code block as a string.
     * @since 3.3.0
     */
    override fun render() = "```${language?.displayName}\n${code.trimEnd()}\n```"
}

/**
 * Represents a Markdown blockquote element.
 *
 * A blockquote element is used to style quoted text with a `>` marker at the beginning
 * of each line. The content of the blockquote is stored as a list of strings, with each string
 * representing a line of text within the blockquote.
 *
 * @property lines The lines of text that constitute the blockquote content.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdBlockquote(val lines: List<String>) : MdElement {
    /**
     * Renders the blockquote as a string by prepending each line with a `>` character
     * followed by a single space, and joins the lines using a newline separator.
     *
     * @return the formatted blockquote as a string
     * @since 3.3.0
     */
    override fun render() = lines.joinToString("\n") { "> $it" }
}

/**
 * Represents a horizontal rule element in markdown.
 *
 * A horizontal rule is commonly used to visually separate content sections
 * in a markdown document. The `style` property determines the representation
 * of the horizontal rule.
 *
 * @property style The string used to render the horizontal rule. Defaults to `---`.
 * Implements the [MdElement] interface to support rendering in markdown format.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdHorizontalRule(val style: String = "---") : MdElement {
    /**
     * Renders the horizontal rule element as a string representation based on its style.
     *
     * @return The string representation of the horizontal rule's style.
     * @since 3.3.0
     */
    override fun render() = style
}

/**
 * Represents an unordered list in Markdown.
 *
 * This class is used to construct and render an unordered list (`ul`) where each list item
 * can optionally contain nested sub-items. The list marker can be customized, with a default
 * value of `-`.
 *
 * @property items The list of items to be included in the unordered list. Each item may contain
 * nested sub-items.
 * @property marker The string used as a marker for each list item. Defaults to `-`.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdUnorderedList(val items: List<MdListItem>, val marker: String = "-") : MdElement {
    /**
     * Renders the unordered markdown list by converting the list of items into a properly formatted
     * string representation. Uses a specified marker for list items and delegates the actual rendering
     * logic to the `renderList` function.
     *
     * @return The string representation of the unordered markdown list.
     * @since 3.3.0
     */
    override fun render() = renderList(items, marker, 0)
}

/**
 * Represents an ordered list in Markdown.
 *
 * An ordered list consists of a collection of list items, each represented by `MdListItem`.
 * Items are rendered in a numbered fashion with increasing indices.
 *
 * @property items The list of ordered list items (`MdListItem`) to be rendered.
 *                  Each item can contain nested child items forming a hierarchical structure.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdOrderedList(val items: List<MdListItem>) : MdElement {
    /**
     * Renders the ordered list represented by the current object as a formatted string.
     *
     * This method generates a textual representation of the ordered list, recursively
     * rendering child list items where applicable. The items are prefixed with indices
     * reflecting their order and adjusted for nesting depth.
     *
     * @return A formatted string representing the ordered list.
     * @since 3.3.0
     */
    override fun render() = renderOrderedList(items, 0)
}

/**
 * Represents an item in a Markdown list. Each item contains a text value and an optional list of child items
 * to support nested list structures.
 *
 * This data class is designed to model both ordered and unordered Markdown list items and allows for hierarchical
 * nesting of list content.
 *
 * @param text The text content of the list item.
 * @param children A list of child items representing nested Markdown list items. Defaults to an empty list.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdListItem(val text: String, val children: List<MdListItem> = emptyList())

/**
 * Represents a Markdown table element consisting of headers, rows, and optional alignments for columns.
 *
 * This class allows for the creation and rendering of Markdown tables. Each table consists of:
 * - Headers: A list representing the column headers.
 * - Rows: A list of rows where each row is a list of string values.
 * - Alignments: An optional list specifying the alignment for each column (left, center, right). Defaults to left-aligned if not specified.
 *
 * @property headers A `List<String>` representing the column headers of the table.
 * @property rows A list of `List<String>` representing the rows of the table.
 * @property alignments A list of `Align` representing the alignment for each column. Defaults to an empty list.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdTable(val headers: List<String>, val rows: List<List<String>>, val alignments: List<Align> = emptyList()) : MdElement {
    /**
     * Renders a Markdown table as a string.
     *
     * The generated table includes headers, rows, and alignment for each column as specified.
     * Each cell is adjusted to fit the required column width while maintaining consistent formatting.
     *
     * @return the string representation of the rendered Markdown table, including headers, separators, and rows.
     * @since 3.3.0
     */
    override fun render(): String {
        val allRows = listOf(headers) + rows
        val colWidths = headers.indices.map { col ->
            allRows.maxOf { row -> row.getOrElse(col) { "" }.length }.coerceAtLeast(3)
        }

        val headerLine = headers.mapIndexed { i, h -> h.padEnd(colWidths[i]) }
            .joinToString(" | ", "| ", " |")
        val separatorLine = colWidths.mapIndexed { i, w ->
            val align = alignments.getOrElse(i) { Align.Left }
            when (align) {
                Align.Left -> "-".repeat(w)
                Align.Center -> ":${"-".repeat((w - 2).coerceAtLeast(1))}:"
                Align.Right -> "${"-".repeat((w - 1).coerceAtLeast(1))}:"
            }
        }.joinToString(" | ", "| ", " |")
        val dataLines = rows.map { row ->
            row.mapIndexed { i, cell -> cell.padEnd(colWidths.getOrElse(i) { cell.length }) }
                .joinToString(" | ", "| ", " |")
        }

        return (listOf(headerLine, separatorLine) + dataLines).joinToString("\n")
    }
}

/**
 * Represents the alignment options that can be applied to columns of a Markdown table.
 *
 * This enum is used to specify the horizontal alignment of column contents. It provides
 * three alignment options: `LEFT`, `CENTER`, and `RIGHT`. These options determine how
 * the content within a column is justified in relation to its horizontal space.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
enum class Align {
    /**
     * Represents left alignment for elements such as table columns.
     * Used in conjunction with other alignment types (CENTER, RIGHT)
     * to define text alignment within Markdown tables or similar structures.
     * @since 4.0.0
     */
    Left,
    /**
     * Represents center alignment within a table column.
     *
     * This alignment type is used to center content within a table column
     * when rendering markdown tables. It ensures that the content of the column
     * is equally spaced from both the left and right edges of the column.
     *
     * Typically used in conjunction with the `Align` enum and associated table rendering functionality.
     * @since 4.0.0
     */
    Center,
    /**
     * Represents right alignment for elements in a Markdown table.
     *
     * This alignment type ensures that the content of the corresponding column
     * is aligned to the right, typically by adding padding spaces to the left
     * of the text content when rendered.
     *
     * Used in conjunction with rendering logic, it determines the visual
     * alignment in Markdown table generation.
     * @since 4.0.0
     */
    Right
}

/**
 * Represents a Markdown task list, composed of multiple individual tasks.
 *
 * This data class implements the `MdElement` interface and provides functionality
 * to render a task list in Markdown format. Each task in the list is represented
 * by an `MdTask`, which includes the text of the task and its checked state.
 *
 * The rendered task list follows the Markdown syntax for tasks:
 * - Each task is prefixed with `- [ ]` for unchecked tasks and `- [x]` for checked tasks.
 * - Tasks are separated by newline characters.
 *
 * @constructor Creates a new `MdTaskList` with the specified list of tasks.
 * @param tasks The list of `MdTask` objects that make up the task list.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdTaskList(val tasks: List<MdTask>) : MdElement {
    /**
     * Renders the task list as a Markdown-formatted string.
     *
     * Each task is represented as a list item prefixed with a checkbox indicator:
     * - `[x]` for completed tasks.
     * - `[ ]` for incomplete tasks.
     *
     * Tasks are separated by newline characters.
     *
     * @return A string representation of the task list in Markdown format.
     * @since 3.3.0
     */
    override fun render() = tasks.joinToString("\n") { task ->
        val check = if (task.checked) "[x]" else "[ ]"
        "- $check ${task.text}"
    }
}

/**
 * Represents a markdown task with a description and completion status.
 *
 * This class is typically used to model a single item in a task list within
 * a markdown document. Each task consists of a textual description and a boolean
 * indicating whether the task is marked as completed.
 *
 * @property text The description of the task.
 * @property checked A flag indicating whether the task is completed. Defaults to `false`.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdTask(val text: String, val checked: Boolean = false)

/**
 * Represents an image element in Markdown, including an optional title.
 *
 * This class is responsible for generating the Markdown syntax for an image element,
 * which can have alternative text, a URL for the image, and an optional title
 * describing the image.
 *
 * @property alt The alternative text for the image.
 * @property url The URL of the image.
 * @property title An optional title for the image, providing additional context.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdImage(val alt: String, val url: String, val title: String? = null) : MdElement {
    /**
     * Renders a Markdown image string based on the provided attributes.
     *
     * This method generates a Markdown inline image representation. If the `title` is not null,
     * the rendered string includes the title attribute for the image. Otherwise, it excludes
     * the title.
     *
     * The output format is:
     * - With title: `![alt](url "title")`
     * - Without title: `![alt](url)`
     *
     * @return A string representing the image in Markdown syntax.
     * @since 3.3.0
     */
    override fun render() = if (title != null) "![${alt}](${url} \"${title}\")" else "![$alt]($url)"
}

/**
 * Represents a Markdown details element, commonly rendered as a collapsible section
 * with a summary visible by default and additional content revealed upon interaction.
 *
 * @property summary The text of the summary element, used as the visible title for the details block.
 * @property content A list of Markdown elements to be included within the collapsible content.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdDetails(val summary: String, val content: List<MdElement>) : MdElement {
    /**
     * Renders this `MdDetails` instance into its Markdown string representation.
     *
     * Constructs a Markdown `<details>` block with a summary and detailed content.
     * The `summary` is rendered inside the `<summary>` tag, while each item in
     * the `content` list is rendered recursively by calling their respective `render` method.
     *
     * @return A `String` containing the Markdown representation of the `<details>` element.
     * @since 3.3.0
     */
    override fun render() = buildString {
        appendLine("<details>")
        appendLine("<summary>$summary</summary>")
        appendLine()
        content.forEach { appendLine(it.render()); appendLine() }
        append("</details>")
    }
}

/**
 * Represents a raw Markdown element containing plain text that is directly
 * included in the final rendered Markdown output without additional formatting.
 *
 * This class is useful for embedding custom Markdown content that may not
 * fit into predefined structures of other Markdown elements.
 *
 * @property text The raw Markdown string that will be rendered as-is.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdRaw(val text: String) : MdElement {
    /**
     * Renders the Markdown raw element as a string.
     *
     * This implementation returns the raw text of the element without
     * applying any additional formatting or transformations.
     *
     * @return the raw text content of the Markdown element
     * @since 3.3.0
     */
    override fun render() = text
}

// --- INLINE ELEMENTS ---

/**
 * Represents an inline Markdown element. Implementations of this interface
 * define specific types of inline Markdown content such as text, emphasis, or links.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
sealed interface MdInline {
    /**
     * Renders the current instance into its string representation.
     *
     * @return the string representation of the current instance.
     * @since 3.3.0
     */
    fun render(): String
}

/**
 * Represents an inline Markdown text element.
 *
 * This data class models a simple Markdown text segment that can be used
 * in inline formatting or as part of other Markdown elements.
 *
 * @property text The content of the Markdown text.
 * @constructor Creates an instance of `MdText` with the specified text content.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdText(val text: String) : MdInline {
    /**
     * Renders the textual content of the implementing instance as a string.
     *
     * @return The textual representation of the instance.
     * @since 3.3.0
     */
    override fun render() = text
}

/**
 * Represents a bold inline Markdown element.
 *
 * This class is used to define bold text in Markdown by wrapping the provided text
 * with double asterisks (**). It implements the [MdInline] interface, making it a valid
 * component of a Markdown document's inline element structure.
 *
 * @property text The text to be rendered as bold.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdBold(val text: String) : MdInline {
    /**
     * Renders the text within the bold Markdown syntax.
     * The output is formatted as the text surrounded by double asterisks (**).
     *
     * @return A string representing the text in bold Markdown format.
     * @since 3.3.0
     */
    override fun render() = "**$text**"
}

/**
 * Represents an italic inline text segment in Markdown formatting.
 *
 * This class is used to wrap a given text in asterisks (*) to denote italics
 * when rendered as Markdown. It implements the `MdInline` interface, making it
 * compatible with other Markdown inline elements.
 *
 * @property text The text content to be rendered as italic in Markdown.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdItalic(val text: String) : MdInline {
    /**
     * Renders the text surrounded by asterisks, formatting it as italic in Markdown.
     *
     * @return A string where the input text is wrapped with asterisks.
     * @since 3.3.0
     */
    override fun render() = "*$text*"
}

/**
 * Represents a strikethrough inline Markdown element.
 *
 * This class is used to wrap a text string in the Markdown strikethrough syntax `~~`.
 * The rendered result will be the input text surrounded by double tildes `~~`.
 *
 * @property text The text content to be rendered with strikethrough styling.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdStrikethrough(val text: String) : MdInline {
    /**
     * Renders the text with strikethrough Markdown syntax.
     *
     * Surrounds the text with double tildes ("~~") to represent a strikethrough
     * in Markdown formatting.
     *
     * @return The rendered strikethrough representation of the text.
     * @since 3.3.0
     */
    override fun render() = "~~$text~~"
}

/**
 * Represents an inline Markdown code segment.
 *
 * This class is used to create inline code segments in Markdown by encapsulating a code string.
 * The `render` function formats the code string with surrounding backticks (`).
 *
 * @constructor Creates an instance of `MdInlineCode` with the provided code string.
 * @property code The code string to be rendered as inline code in Markdown.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdInlineCode(val code: String) : MdInline {
    /**
     * Renders the inline code element as a Markdown-formatted string.
     *
     * This implementation wraps the `code` value in backticks (`) to produce
     * the Markdown inline code representation.
     *
     * @return A Markdown-formatted string representing the inline code.
     * @since 3.3.0
     */
    override fun render() = "`$code`"
}

/**
 * Represents a Markdown hyperlink as an inline element.
 *
 * This class is responsible for rendering a Markdown hyperlink in the format
 * `[text](url)`. It is a data structure that holds the display text and the
 * target URL of the link and implements the `MdInline` interface.
 *
 * @property text The display text of the hyperlink.
 * @property uri The target URL of the hyperlink.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class MdLink(val text: String, val uri: Uri) : MdInline {
    /**
     * Renders the Markdown link as a string in the format [text](url).
     *
     * @return The rendered Markdown link.
     * @since 3.3.0
     */
    override fun render() = "[$text]($uri)"
}

// --- LIST RENDERING HELPERS ---

/**
 * Recursively renders a list of Markdown list items into a formatted string representation.
 *
 * Each list item is prefixed with the specified marker and indented based on its depth within
 * the nested structure. Child items are rendered using the same logic.
 *
 * @param items The list of Markdown list items to render.
 * @param marker The marker to use for rendering each list item (e.g., "-", "*", or "1.").
 * @param depth The current depth of the list in the nested structure, used for indentation.
 * @return A string representation of the rendered Markdown list.
 * @since 3.3.0
 */
private fun renderList(items: List<MdListItem>, marker: String, depth: Int): String {
    val pad = "  ".repeat(depth)
    return items.joinToString("\n") { item ->
        val line = "$pad$marker ${item.text}"
        if (item.children.isEmpty()) line
        else "$line\n${renderList(item.children, marker, depth + 1)}"
    }
}

/**
 * Renders a Markdown ordered list recursively as a formatted string.
 *
 * Each list item is prefixed with an incrementing number, reflective of its order within the list,
 * and indented based on its depth. Items with nested children are rendered below their parent
 * with increased indentation.
 *
 * @param items The list of `MdListItem` objects representing the ordered list to be rendered.
 *              Each item can contain nested child items.
 * @param depth The current depth of the list being rendered, used to determine indentation.
 * @return A formatted string representing the ordered list, including nested child items if present.
 * @since 3.3.0
 */
private fun renderOrderedList(items: List<MdListItem>, depth: Int): String {
    val pad = "  ".repeat(depth)
    return items.mapIndexed { i, item ->
        val line = "$pad${i + 1}. ${item.text}"
        if (item.children.isEmpty()) line
        else "$line\n${renderOrderedList(item.children, depth + 1)}"
    }.joinToString("\n")
}

// --- INLINE PARAGRAPH BUILDER ---

/**
 * A DSL builder used for creating inline Markdown elements such as bold, italic, and links.
 * Provides methods for adding various types of inline Markdown formatting and constructs
 * an internal list of elements that can be retrieved using the `build` function.
 * Typically used in conjunction with `MarkdownBuilder` for constructing more complex Markdown documents.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@MarkdownDslMarker
class InlineBuilder {
    /**
     * Internal mutable list of inline Markdown elements that are used to construct
     * the contents of the inline builder.
     *
     * This list is populated by the various methods of [InlineBuilder] (e.g., `bold`, `italic`)
     * to define the sequence of Markdown inline elements that will make up the final
     * Markdown content.
     *
     * The list is initialized as empty and is modified internally when different
     * inline components are added to the builder. It is exposed through the `build`
     * function as a read-only list of `MdInline` elements.
     * @since 3.3.0
     */
    @PublishedApi
    internal val segments = emptyMList<MdInline>()

    /**
     * Adds a Markdown text element to the inline content.
     *
     * This operator function allows the addition of a plain text segment
     * in a type-safe and declarative way. It creates an `MdText` instance
     * with the specified string and appends it to the collection of inline segments.
     *
     * The operation is designed to simplify the construction of structured
     * Markdown content by enabling direct addition of text strings to a builder.
     * @since 3.3.0
     */
    operator fun String.unaryPlus() { segments += MdText(this) }
    /**
     * Adds a bold Markdown element containing the specified text to the list of inline segments.
     *
     * The `bold` function wraps the provided text in double asterisks (`**`) using the `MdBold` class.
     * The resulting bold element is appended to the internal list of Markdown inline elements.
     *
     * @param text The text to be styled as bold in Markdown format.
     * @since 3.3.0
     */
    fun bold(text: String) { segments += MdBold(text) }
    /**
     * Adds an italic Markdown segment to the inline element structure.
     *
     * This method wraps the given text in an `MdItalic` instance and appends it to the
     * list of Markdown inline segments maintained by the `InlineBuilder`.
     *
     * @param text The text content to be formatted as italic.
     * @since 3.3.0
     */
    fun italic(text: String) { segments += MdItalic(text) }
    /**
     * Adds a strikethrough inline Markdown element containing the given text.
     *
     * This method constructs a `MdStrikethrough` instance with the specified text and
     * appends it to the internal list of inline segments, making it part of the Markdown
     * content being built.
     *
     * @param text The text to be rendered with strikethrough formatting in Markdown.
     * @since 3.3.0
     */
    fun strikethrough(text: String) { segments += MdStrikethrough(text) }
    /**
     * Adds a Markdown inline code segment to the current collection of segments.
     *
     * @param text the text to be formatted as inline code
     * @since 3.3.0
     */
    fun code(text: String) { segments += MdInlineCode(text) }
    /**
     * Adds a Markdown hyperlinked text element to the inline content.
     *
     * This function appends a hyperlink element to the builder's list of inline segments.
     * The hyperlink is defined by a display text and a target URL.
     *
     * @param text The display text of the hyperlink.
     * @param uri The target URL of the hyperlink.
     * @since 3.3.0
     */
    fun link(text: String, uri: Uri) { segments += MdLink(text, uri) }
    /**
     * Adds a Markdown hyperlinked text element to the inline content.
     *
     * This function appends a hyperlink element to the builder's list of inline segments.
     * The hyperlink is defined by a display text and a target URL.
     *
     * @param text The display text of the hyperlink.
     * @param url The target URL of the hyperlink.
     * @since 3.3.0
     */
    fun link(text: String, url: Url) { segments += MdLink(text, url.toUri()) }

    /**
     * Builds and returns a List of MdInline elements based on the current segments.
     *
     * @return A List containing MdInline elements created from the current segments.
     * @since 3.3.0
     */
    fun build(): List<MdInline> = segments.toList()
}

// --- LIST BUILDER ---

/**
 * Builder class for constructing hierarchical Markdown lists.
 *
 * This class is used within a DSL context to create ordered or unordered
 * lists by defining list items and their potential nested children.
 * The resulting structure is a list of `MdListItem` objects, representing
 * Markdown list elements in a nested format.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@MarkdownDslMarker
class ListBuilder {
    /**
     * A mutable list of Markdown list items that represents the container for constructing Markdown list structures.
     *
     * This internal property is utilized in the `ListBuilder` class to accumulate instances of `MdListItem`,
     * enabling the creation of hierarchical or flat Markdown lists. Items can be added to this list using
     * the `li` function, and the list can be finalized with the `build` function to produce an immutable list.
     *
     * Marked with `@PublishedApi` to allow inline functions within the `MarkdownDsl` to access it.
     * @since 3.3.0
     */
    @PublishedApi
    internal val items = emptyMList<MdListItem>()

    /**
     * Adds a new list item with the given text content to the current list.
     *
     * This operator function allows for a concise and expressive way to add
     * a `MdListItem` to the `items` collection within the `ListBuilder`.
     * Each invocation of this function creates a new `MdListItem` with the
     * specified text and appends it to the list being constructed.
     */
    operator fun String.unaryPlus() { items += MdListItem(this) }

    /**
     * Adds a new list item with the specified text and an optional block for defining nested list items.
     *
     * This function is used to construct hierarchical list structures by adding a list item to the current
     * builder with the provided text content. Optionally, a nested block can be defined to build child list items.
     *
     * @param text The text content for the list item being added.
     * @param block An optional lambda to define child list items. Defaults to an empty block.
     * @since 3.3.0
     */
    fun li(text: String, block: ReceiverConsumer<ListBuilder> = {}) {
        val children = ListBuilder().apply(block).items
        items += MdListItem(text, children)
    }

    /**
     * Constructs and returns a list of `MdListItem` objects
     * based on the current state of the builder.
     *
     * @return a new list containing the `MdListItem` objects.
     * @since 3.3.0
     */
    fun build(): List<MdListItem> = items.toList()
}

// --- TABLE BUILDER ---

/**
 * A builder class for constructing Markdown tables in a declarative way.
 * This class provides methods for defining table headers, rows, and column alignments.
 * Once all configurations are specified, use the `build` function to generate an instance of `MdTable`.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@MarkdownDslMarker
class TableBuilder {
    /**
     * Holds the list of column headers for the Markdown table being constructed.
     *
     * This property is used to store the headers provided by the user when defining the table.
     * These headers correspond to the column names and are included in the rendered table's
     * header row. By default, this property is an empty list, but it can be populated using the
     * `headers(vararg cols: String)` function.
     *
     * It is utilized as an input during the construction of the `MdTable` instance
     * by the `build()` function, where it is combined with the rows and alignments
     * to generate the final Markdown table representation.
     * @since 3.3.0
     */
    private var headers = listOf<String>()
    /**
     * Holds the data rows of the table being built.
     *
     * Each entry in the list represents a single row of the table, with the row being
     * represented as a list of string values (`List<String>`). Rows are added using the
     * `row` function, which appends the provided cells as a new row to this list.
     *
     * The rows are included in the final Markdown table produced by the `build` function.
     * @since 3.3.0
     */
    private val rows = emptyMList<List<String>>()
    /**
     * Specifies the alignment settings for the columns of the Markdown table being built.
     *
     * The alignments define how the content in each column is positioned horizontally.
     * Possible alignment values are defined in the `Align` enum and include:
     * - `Align.LEFT`: Aligns content to the left.
     * - `Align.CENTER`: Centers content.
     * - `Align.RIGHT`: Aligns content to the right.
     *
     * If not specified, the default alignment for all columns is left-aligned.
     *
     * This property is populated via the `alignments` function
     * and is used during table construction in the `build` method of the `TableBuilder`.
     * @since 3.3.0
     */
    private var alignments = listOf<Align>()

    /**
     * Sets the headers for a collection by converting the provided vararg of column names into a list.
     *
     * @param cols Variable number of string arguments representing the column names.
     * @since 3.3.0
     */
    fun headers(vararg cols: String) { headers = cols.toList() }
    /**
     * Adds a new row to a collection of rows using the provided cells.
     *
     * @param cells A variable number of strings representing the values of the cells in the row.
     * @since 3.3.0
     */
    fun row(vararg cells: String) { rows += cells.toList() }
    /**
     * Specifies the alignment configuration for the columns of the table.
     *
     * This method accepts a variable number of `Align` values corresponding to each column's alignment
     * in the table. The alignments are applied in the order specified. If no alignments are specified
     * for some columns, a default alignment (typically left-aligned) may be used during table rendering.
     *
     * @param aligns A variable number of `Align` values representing the alignment for each column.
     *               Accepted values are `Align.LEFT`, `Align.CENTER`, or `Align.RIGHT`.
     * @since 3.3.0
     */
    fun alignments(vararg aligns: Align) { alignments = aligns.toList() }

    /**
     * Constructs and returns an instance of [MdTable] using the provided headers, rows, and alignments.
     *
     * @return an initialized [MdTable] object containing the specified headers, rows, and alignments.
     * @since 3.3.0
     */
    fun build(): MdTable = MdTable(headers, rows.toList(), alignments)
}

// --- TASK LIST BUILDER ---

/**
 * A DSL builder class for constructing a list of markdown tasks.
 *
 * This builder allows you to define tasks with text and completion status,
 * and compile them into a list of tasks usable in markdown documents.
 *
 * Usage of the builder is typically managed within the `MarkdownBuilder` class.
 * The resulting list of tasks can be rendered or integrated into markdown content.
 *
 * This class supports the use of the `taskList` function in Markdown DSL
 * for creating task lists within markdown documents.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@MarkdownDslMarker
class TaskListBuilder {
    /**
     * Holds a mutable list of markdown tasks (`MdTask`) within a `TaskListBuilder`.
     *
     * This internal property is utilized to collect tasks added through the DSL
     * functions. Each task represents an item with a textual description and a
     * completion status. The tasks are stored in an `MList` and can be converted to
     * an immutable list when needed.
     *
     * The property is marked as `@PublishedApi` to enable usage within inline functions
     * defined in the same module.
     * @since 3.3.0
     */
    @PublishedApi
    internal val tasks = emptyMList<MdTask>()

    /**
     * Adds a markdown task to the task list with the specified description and completion status.
     *
     * @param text The description of the task to be added.
     * @param checked Indicates whether the task is completed. Defaults to `false`.
     * @since 3.3.0
     */
    fun task(text: String, checked: Boolean = false) { tasks += MdTask(text, checked) }

    /**
     * Builds and returns a list of markdown tasks.
     *
     * This method collects all the tasks added to the builder and returns them as an immutable list.
     * Tasks are represented as instances of the `MdTask` class, which include a text description
     * and a completion status.
     *
     * @return A list containing all tasks added to the builder.
     * @since 3.3.0
     */
    fun build(): List<MdTask> = tasks.toList()
}

// --- DOCUMENT BUILDER ---

/**
 * DSL Builder for constructing Markdown documents programmatically.
 *
 * The `MarkdownBuilder` class provides methods to create various Markdown elements
 * such as headings, paragraphs, code blocks, lists, tables, images, and more. It allows
 * chaining transformations and appending new elements to build a structured Markdown document.
 *
 * Each method in this class corresponds to a specific type of Markdown element or structure.
 * The builder collects all elements in sequence and provides options to render the
 * final document as a string or retrieve the list of constructed elements.
 *
 * This builder supports various Markdown features, including headings of different levels,
 * unordered and ordered lists, blockquotes, inline and block code, images, and raw Markdown content.
 *
 * Markdown elements must be added through the provided methods to maintain a consistent
 * structure. The class also provides helper methods for generating specific styles (e.g., task lists)
 * and handling nested structures (e.g., details and summaries).
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@MarkdownDslMarker
class MarkdownBuilder {
    /**
     * A mutable list of `MdElement` instances that represents the internal structure of the Markdown content.
     *
     * This list is used to store and build the sequence of Markdown elements, such as headings, paragraphs,
     * code blocks, and other Markdown-compatible components, that form the content being constructed.
     *
     * The list is initialized as empty via the `emptyMList` function and is populated incrementally
     * through various methods in the `MarkdownBuilder` class.
     *
     * This property is marked `internal` and `@PublishedApi`, making it accessible for inline functions
     * while restricting its visibility outside the module.
     * @since 3.3.0
     */
    @PublishedApi
    internal val elements = emptyMList<MdElement>()

    // Headings
    /**
     * Adds a level-1 heading to the Markdown output with the specified text content.
     *
     * @param text The text to be included in the heading.
     * @since 3.3.0
     */
    fun h1(text: String) { elements += MdHeading(1, text) }
    /**
     * Adds a level-2 Markdown heading to the document with the specified text.
     *
     * @param text The text content of the heading.
     * @since 3.3.0
     */
    fun h2(text: String) { elements += MdHeading(2, text) }
    /**
     * Adds a level 3 Markdown heading with the specified text content.
     *
     * @param text The text content of the heading.
     * @since 3.3.0
     */
    fun h3(text: String) { elements += MdHeading(3, text) }
    /**
     * Adds a level-4 Markdown heading to the document.
     *
     * This method appends a `MdHeading` element with a level of 4 and the specified text content
     * to the list of elements in the Markdown document.
     *
     * @param text The text content of the heading to be added.
     * @since 3.3.0
     */
    fun h4(text: String) { elements += MdHeading(4, text) }
    /**
     * Adds a level-5 Markdown heading with the specified text to the elements list.
     *
     * @param text The text content of the heading to be added.
     * @since 3.3.0
     */
    fun h5(text: String) { elements += MdHeading(5, text) }
    /**
     * Adds a level-6 heading to the Markdown document with the specified text.
     *
     * @param text The content of the level-6 heading.
     * @since 3.3.0
     */
    fun h6(text: String) { elements += MdHeading(6, text) }

    // Paragraphs
    /**
     * Adds a paragraph element to the document with the given text content.
     *
     * @param text The textual content to include in the paragraph.
     * @since 3.3.0
     */
    fun p(text: String) { elements += MdParagraph(listOf(MdText(text))) }
    /**
     * Adds a paragraph element to the list of elements by applying the provided block to an InlineBuilder.
     *
     * @param block A lambda function with `InlineBuilder` as its receiver, allowing configuration or content creation
     * within the builder. The result of the block is added as a paragraph element.
     * @since 3.3.0
     */
    fun p(block: ReceiverConsumer<InlineBuilder>) {
        elements += MdParagraph(InlineBuilder().apply(block).build())
    }

    // Code
    /**
     * Appends a code block to the collection of elements.
     *
     * @param language the programming language of the code block. Defaults to an empty string.
     * @param block a supplier that provides the content of the code block as a string.
     * @since 3.3.0
     */
    fun code(language: dev.tommasop1804.kutils.classes.coding.Language? = null, block: Supplier<Code>) {
        elements += MdCodeBlock(language, block())
    }

    /**
     * Adds a code block element to the list of elements.
     *
     * @param language The programming language of the code block. Defaults to an empty string if not specified.
     * @param code The code to be included within the code block.
     * @since 3.3.0
     */
    fun code(language: dev.tommasop1804.kutils.classes.coding.Language? = null, code: Code) {
        elements += MdCodeBlock(language, code)
    }

    // Blockquote
    /**
     * Adds a blockquote to the Markdown document.
     *
     * The provided text is split into lines, and each line is
     * added to the blockquote element. Blockquotes are utilized
     * to represent quoted text, typically displayed with a `>` marker
     * at the beginning of each line in Markdown.
     *
     * @param text The text content to be included in the blockquote.
     * @since 3.3.0
     */
    fun blockquote(text: String) { elements += MdBlockquote(text.lines()) }
    /**
     * Adds a blockquote element to the Markdown document.
     *
     * The content of the blockquote is generated by invoking the provided block function,
     * which should return a string. Each line of the returned string becomes a line in the
     * blockquote, prefixed with a `>` marker when rendered.
     *
     * @param block A lambda function that returns the content of the blockquote as a string.
     * @since 3.3.0
     */
    fun blockquote(block: () -> String) { elements += MdBlockquote(block().lines()) }

    // Horizontal rule
    /**
     * Adds a horizontal rule to the Markdown document.
     *
     * A horizontal rule is a visual separator typically rendered as a line,
     * used to divide sections of content in the Markdown format.
     *
     * Invoking this method appends an instance of `MdHorizontalRule`
     * to the list of elements being constructed.
     * @since 3.3.0
     */
    fun hr() { elements += MdHorizontalRule() }

    // Lists
    /**
     * Constructs an unordered list in the Markdown structure by applying the specified block to
     * a `ListBuilder` instance. The resulting list items and their potential nested elements
     * are added to the Markdown document as an unordered list.
     *
     * @param block A lambda with receiver to define the items of the unordered list and their
     * potential nested lists. The lambda operates on an instance of `ListBuilder`.
     * @since 3.3.0
     */
    fun ul(block: ReceiverConsumer<ListBuilder>) {
        elements += MdUnorderedList(ListBuilder().apply(block).build())
    }

    /**
     * Adds an ordered list to the Markdown content by applying the provided configuration block.
     *
     * This function allows for the creation of an ordered list within the Markdown document.
     * The configuration block is executed in the context of a `ListBuilder`, enabling the definition
     * of list items and their potential nested child items.
     *
     * @param block A lambda with `ListBuilder` as its receiver, used to define the ordered list items
     *              and any nested structures.
     * @since 3.3.0
     */
    fun ol(block: ReceiverConsumer<ListBuilder>) {
        elements += MdOrderedList(ListBuilder().apply(block).build())
    }

    // Table
    /**
     * Adds a table element to the current structure by applying the provided configuration block.
     *
     * @param block A lambda with a receiver of type TableBuilder used to define the properties of the table.
     * @since 3.3.0
     */
    fun table(block: ReceiverConsumer<TableBuilder>) {
        elements += TableBuilder().apply(block).build()
    }

    // Task list
    /**
     * Appends a Markdown task list to the current content by applying the specified builder block.
     *
     * Each task list is composed of individual tasks, which can be marked as completed or incomplete.
     * The builder block is used to define and configure the tasks to be included in the task list.
     *
     * @param block A lambda that provides a `TaskListBuilder` for configuring the task list.
     * @since 3.3.0
     */
    fun taskList(block: ReceiverConsumer<TaskListBuilder>) {
        elements += MdTaskList(TaskListBuilder().apply(block).build())
    }

    // Image
    /**
     * Adds an image element to the Markdown content with specified alternative text, URL,
     * and an optional title.
     *
     * @param alt The alternative text displayed if the image cannot be loaded.
     * @param url The URL of the image to be included in the Markdown content.
     * @param title An optional title providing additional context for the image.
     * @since 3.3.0
     */
    fun image(alt: String, url: String, title: String? = null) {
        elements += MdImage(alt, url, title)
    }

    // Details/Summary (HTML)
    /**
     * Adds a details block with a summary and collapsible content to the Markdown output.
     *
     * @param summary The text for the summary element, which is always visible and acts as the title for the collapsible block.
     * @param block A lambda for defining the content of the details block, using a `MarkdownBuilder` instance.
     * @since 3.3.0
     */
    fun details(summary: String, block: ReceiverConsumer<MarkdownBuilder>) {
        elements += MdDetails(summary, MarkdownBuilder().apply(block).elements.toList())
    }

    // Raw markdown
    /**
     * Adds a raw markdown element to the list of elements.
     *
     * @param text The raw markdown content to be added.
     * @since 3.3.0
     */
    fun raw(@Language("markdown") text: String) { elements += MdRaw(text) }

    // Render
    /**
     * Constructs and returns a list of Markdown elements.
     *
     * @return A new list containing all the elements in the current context.
     * @since 3.3.0
     */
    fun build(): List<MdElement> = elements.toList()

    /**
     * Renders the Markdown elements contained within the builder into a single
     * Markdown-formatted string.
     *
     * This method iterates through all elements in the builder, invokes their
     * respective `render` methods, and concatenates the results, separated by
     * double line breaks. The resulting string represents the complete
     * Markdown document or fragment.
     *
     * @return A string representation of the rendered Markdown content.
     * @since 3.3.0
     */
    fun render(): String = elements.joinToString("\n\n") { it.render() }

    /**
     * Converts the rendered content into its Markdown representation.
     *
     * This method processes the output from the `render` function and transforms it
     * into a Markdown-formatted string using the appropriate conversion logic.
     *
     * @return The Markdown representation of the rendered content as a string.
     * @since 4.0.0
     */
    fun toMarkdown() = render().toMarkdown()()

    /**
     * Converts the rendered Markdown content into a `Code` instance in markdown format.
     *
     * @return An instance of `Code` representing the current Markdown content rendered as a markdown code block.
     * @since 3.6.4
     */
    fun toCode(): Code = Code.markdown(render())
}

// --- ENTRY POINT ---

/**
 * Builds a Markdown document using a DSL-style builder.
 *
 * This method initializes a new instance of `MarkdownBuilder`, applies the given operations
 * defined in the `block` parameter to it, and returns the configured `MarkdownBuilder` instance.
 * The returned builder contains the structure and content of the Markdown document created using
 * the provided block of operations.
 *
 * @param block A lambda expression with a receiver of type `MarkdownBuilder` that defines the
 *              operations to configure the Markdown document.
 * @return A configured instance of `MarkdownBuilder` containing the constructed Markdown elements.
 * @since 3.3.0
 */
fun buildMarkdown(block: ReceiverConsumer<MarkdownBuilder>) =
    MarkdownBuilder().apply(block).toMarkdown()

/**
 * Initializes a MarkdownBuilder instance and applies the given block to it.
 *
 * @param block A lambda function that receives a MarkdownBuilder instance,
 * which is used to build and configure the markdown content.
 * @since 3.6.4
 */
fun initMarkdown(block: ReceiverConsumer<MarkdownBuilder>) =
    MarkdownBuilder().apply(block)