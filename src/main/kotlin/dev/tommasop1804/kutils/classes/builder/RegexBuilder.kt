@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.builder

import dev.tommasop1804.kutils.EMPTY
import dev.tommasop1804.kutils.ReceiverConsumer
import dev.tommasop1804.kutils.classes.coding.Code
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.exceptions.NumberSignException
import dev.tommasop1804.kutils.isNotNegative
import dev.tommasop1804.kutils.isPositive
import dev.tommasop1804.kutils.toCode
import dev.tommasop1804.kutils.tryOrThrow
import kotlin.apply
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * A utility class for incrementally building and customizing regular expressions in a structured and fluent manner.
 *
 * This class provides a chainable API, allowing various regex components to be added step-by-step to construct
 * complex regex patterns. The generated pattern can be converted to a Kotlin [Regex] object using the [build] method.
 *
 * You can customize your patterns with literals, character classes, quantifiers, groups, lookaheads, flags, and more.
 * This class encapsulates various regex-related operations for easier and more readable construction.
 *
 * @since 1.0.0
 */
class RegexBuilder {
    /**
     * A `StringBuilder` instance used internally to construct the regular expression pattern.
     * This variable accumulates components of the regex as methods of the `RegexBuilder` class are called.
     * It serves as the foundation for dynamically building complex regex patterns.
     *
     * @since 1.0.0
     */
    private val pattern = StringBuilder()

    private var isInCharClass: Boolean = false

    /**
     * Appends a literal string to the current regex pattern.
     * The provided string will be escaped to ensure that its special characters
     * are treated as literal characters in the resulting regex.
     *
     * @param text the string to be added to the regex pattern as a literal.
     * @return the current instance of [RegexBuilder] for method chaining.
     * @since 1.0.0
     */
    fun literal(text: String): RegexBuilder {
        pattern.append(Regex.escape(text))
        return this
    }

    /**
     * Appends a pattern that matches any single character to the regex being built.
     *
     * @return The current instance of [RegexBuilder] for method chaining.
     * @since 1.0.0
     */
    fun anyChar(): RegexBuilder {
        pattern.append(".")
        return this
    }

    /**
     * Adds a specific character to the regex pattern, escaping it if necessary.
     *
     * @param c The character to be added to the regex pattern.
     * @return The current instance of [RegexBuilder] for method chaining.
     * @since 1.0.0
     */
    fun char(c: Char): RegexBuilder {
        pattern.append(if (isInCharClass) c.toString() else Regex.escape(c.toString()))
        return this
    }

    /**
     * Appends the specified characters to the regex pattern in an escaped format.
     *
     * @param chars The characters to be added to the regex pattern.
     * @return The current instance of [RegexBuilder] with the updated pattern.
     * @since 1.0.0
     */
    fun chars(vararg chars: Char): RegexBuilder {
        chars.forEach { pattern.append(if (isInCharClass) it.toString() else Regex.escape(it.toString())) }
        return this
    }

    /**
     * Appends a character class to the current regex pattern with the specified characters.
     *
     * @param chars the string containing characters to include in the character class
     * @return the updated instance of [RegexBuilder]
     * @since 1.0.0
     */
    fun charClass(chars: String): RegexBuilder {
        pattern.append("[").append(chars).append("]")
        return this
    }

    /**
     * Appends a negated character class to the regex pattern. The negated character class matches
     * any character not listed in the provided string.
     *
     * @param chars the characters to exclude from the match. Each character listed will be negated in the pattern.
     * @return the current instance of [RegexBuilder] for method chaining.
     * @since 1.0.0
     */
    fun notCharClass(chars: String): RegexBuilder {
        pattern.append("[^").append(chars).append("]")
        return this
    }

    /**
     * Adds a character range to the regex pattern, defined by the `from` and `to` characters.
     * The range includes all characters from `from` to `to`, inclusive.
     *
     * @param range the [CharRange] defining the boundaries of the character range to be added to the regex pattern.
     * @param omitSquareBrackets if `true`, the character range will be added to the regex pattern without square brackets.
     * @return the current instance of [RegexBuilder] for method chaining
     * @since 1.0.0
     */
    fun charRange(range: CharRange, omitSquareBrackets: Boolean): RegexBuilder {
        pattern.append(if (omitSquareBrackets) String.EMPTY else "[").append(range.first).append("-").append(range.last).append(if (omitSquareBrackets) String.EMPTY else "]")
        return this
    }

    /**
     * Adds a character range to the regex pattern, defined by the `from` and `to` characters.
     * The range includes all characters from `from` to `to`, inclusive.
     *
     * @param range the [CharRange] defining the boundaries of the character range to be added to the regex pattern.
     * @return the current instance of [RegexBuilder] for method chaining
     * @since 1.0.0
     */
    fun charRange(range: CharRange): RegexBuilder {
        pattern.append(if (isInCharClass) String.EMPTY else "[").append(range.first).append("-").append(range.last).append(if (isInCharClass) String.EMPTY else "]")
        return this
    }
    
    /**
     * Appends a pattern representing a range of lowercase letters (a-z) to the current RegexBuilder instance.
     *
     * @param omitSquareBrackets If true, omits square brackets around the range in the resulting regex pattern. Defaults to false.
     * Default is `false` but when is inside a char class.
     * @return The instance of the RegexBuilder with the updated pattern.
     * @since 1.0.0
     */
    fun lowerLetterRange(omitSquareBrackets: Boolean): RegexBuilder {
        pattern.append(if (omitSquareBrackets) String.EMPTY else "[").append("a").append("-").append("z").append(if (omitSquareBrackets) String.EMPTY else "]")
        return this
    }

    /**
     * Appends a pattern representing a range of lowercase letters (a-z) to the current RegexBuilder instance.
     *
     * @return The instance of the RegexBuilder with the updated pattern.
     * @since 1.0.0
     */
    fun lowerLetterRange(): RegexBuilder {
        pattern.append(if (isInCharClass) String.EMPTY else "[").append("a").append("-").append("z").append(if (isInCharClass) String.EMPTY else "]")
        return this
    }
    
    /**
     * Appends a range of uppercase letters (from 'A' to 'Z') to the pattern being built. Optionally, the
     * brackets around this range can be omitted based on the specified parameter.
     *
     * @param omitSquareBrackets Boolean flag indicating whether to omit square brackets around the range.
     * @return The updated `RegexBuilder` instance with the appended range.
     * @since 1.0.0
     */
    fun upperLetterRange(omitSquareBrackets: Boolean): RegexBuilder {
        pattern.append(if (omitSquareBrackets) String.EMPTY else "[").append("a").append("-").append("z").append(if (omitSquareBrackets) String.EMPTY else "]")
        return this
    }

    /**
     * Appends a range of uppercase letters (from 'A' to 'Z') to the pattern being built. Optionally, the
     * brackets around this range can be omitted based on the specified parameter.
     *
     * @return The updated `RegexBuilder` instance with the appended range.
     * @since 1.0.0
     */
    fun upperLetterRange(): RegexBuilder {
        pattern.append(if (isInCharClass) String.EMPTY else "[").append("a").append("-").append("z").append(if (isInCharClass) String.EMPTY else "]")
        return this
    }
    
    /**
     * Appends a pattern that represents a range of letters (both uppercase and lowercase) to the current regex pattern.
     * Allows an option to omit square brackets from the resulting pattern.
     *
     * @param omitSquareBrackets A boolean flag indicating whether to omit square brackets around the letter range.
     * Default is `false` but when is inside a char class.
     * @return The same instance of [RegexBuilder] for method chaining.
     * @since 1.0.0
     */
    fun letterRange(omitSquareBrackets: Boolean): RegexBuilder {
        pattern.append(if (omitSquareBrackets) String.EMPTY else "[").append("a-zA-Z").append(if (omitSquareBrackets) String.EMPTY else "]")
        return this
    }

    /**
     * Appends a pattern that represents a range of letters (both uppercase and lowercase) to the current regex pattern.
     * Allows an option to omit square brackets from the resulting pattern.
     *
     * @return The same instance of [RegexBuilder] for method chaining.
     * @since 1.0.0
     */
    fun letterRange(): RegexBuilder {
        pattern.append(if (isInCharClass) String.EMPTY else "[").append("a-zA-Z").append(if (isInCharClass) String.EMPTY else "]")
        return this
    }
    
    /**
     * Appends a digit range pattern `[0-9]` to the current RegexBuilder pattern. 
     * Optionally allows omission of square brackets in the appended pattern.
     *
     * @param omitSquareBrackets specifies whether square brackets should be excluded. 
     * Default is `false`, which includes the square brackets.
     * @return the updated RegexBuilder with the appended digit range pattern.
     * @since 1.0.0
     */
    fun digitRange(omitSquareBrackets: Boolean): RegexBuilder {
        pattern.append(if (omitSquareBrackets) String.EMPTY else "[").append("0-9").append(if (omitSquareBrackets) String.EMPTY else "]")
        return this
    }

    /**
     * Appends a digit range pattern `[0-9]` to the current RegexBuilder pattern.
     * Optionally allows omission of square brackets in the appended pattern.
     *
     * @return the updated RegexBuilder with the appended digit range pattern.
     * @since 1.0.0
     */
    fun digitRange(): RegexBuilder {
        pattern.append(if (isInCharClass) String.EMPTY else "[").append("0-9").append(if (isInCharClass) String.EMPTY else "]")
        return this
    }
    
    /**
     * Appends a character range for lowercase letters and digits to the regex pattern.
     *
     * @param omitSquareBrackets If true, omits square brackets around the character range; otherwise, includes them.
     * Default is `false` but when is inside a char class.
     * @return The updated RegexBuilder instance with the appended pattern.
     * @since 1.0.0
     */
    fun lowerLetterOrDigitRange(omitSquareBrackets: Boolean): RegexBuilder {
        pattern.append(if (omitSquareBrackets) String.EMPTY else "[").append("a-z0-9").append(if (omitSquareBrackets) String.EMPTY else "]")
        return this
    }

    /**
     * Appends a character range for lowercase letters and digits to the regex pattern.
     *
     * @return The updated RegexBuilder instance with the appended pattern.
     * @since 1.0.0
     */
    fun lowerLetterOrDigitRange(): RegexBuilder {
        pattern.append(if (isInCharClass) String.EMPTY else "[").append("a-z0-9").append(if (isInCharClass) String.EMPTY else "]")
        return this
    }
    
    /**
     * Appends a regular expression pattern that matches uppercase letters (A-Z) 
     * and digits (0-9) to the current RegexBuilder instance. The optional 
     * square brackets can be omitted from the pattern string.
     *
     * @param omitSquareBrackets if true, the square brackets surrounding the 
     *        character range are omitted. Defaults to false.
     * Default is `false` but when is inside a char class.
     * @return the updated RegexBuilder instance.
     * @since 1.0.0
     */
    fun upperLetterOrDigitRange(omitSquareBrackets: Boolean = false): RegexBuilder {
        pattern.append(if (omitSquareBrackets) String.EMPTY else "[").append("A-Z0-9").append(if (omitSquareBrackets) String.EMPTY else "]")
        return this
    }

    /**
     * Appends a regular expression pattern that matches uppercase letters (A-Z)
     * and digits (0-9) to the current RegexBuilder instance. The optional
     * square brackets can be omitted from the pattern string.
     *
     * @return the updated RegexBuilder instance.
     * @since 1.0.0
     */
    fun upperLetterOrDigitRange(): RegexBuilder {
        pattern.append(if (isInCharClass) String.EMPTY else "[").append("A-Z0-9").append(if (isInCharClass) String.EMPTY else "]")
        return this
    }
    
    /**
     * Appends a range of letters (both uppercase and lowercase) and digits (0-9) to the pattern.
     * Optionally omits square brackets around the range based on the parameter value.
     *
     * @param omitSquareBrackets A boolean flag indicating whether to exclude the square brackets surrounding the range. 
     *                            If true, square brackets will not be included; otherwise, they will be.
     * Default is `false` but when is inside a char class.
     * @return The updated instance of the RegexBuilder for chaining further modifications.
     * @since 1.0.0
     */
    fun letterOrDigitRange(omitSquareBrackets: Boolean = false): RegexBuilder {
        pattern.append(if (omitSquareBrackets) String.EMPTY else "[").append("a-zA-Z0-9").append(if (omitSquareBrackets) String.EMPTY else "]")
        return this
    }

    /**
     * Appends a range of letters (both uppercase and lowercase) and digits (0-9) to the pattern.
     * Optionally omits square brackets around the range based on the parameter value.
     *
     * @return The updated instance of the RegexBuilder for chaining further modifications.
     * @since 1.0.0
     */
    fun letterOrDigitRange(): RegexBuilder {
        pattern.append(if (isInCharClass) String.EMPTY else "[").append("a-zA-Z0-9").append(if (isInCharClass) String.EMPTY else "]")
        return this
    }

    /**
     * Appends the pattern for a digit (`\d`) to the current regular expression.
     *
     * @return The RegexBuilder instance with the updated pattern including the digit match.
     * @since 1.0.0
     */
    fun digit(): RegexBuilder {
        pattern.append("\\d")
        return this
    }

    /**
     * Appends the pattern for matching any character that is not a digit (equivalent to `\D` in regex)
     * to the current regex pattern.
     *
     * @return The current instance of [RegexBuilder].
     * @since 1.0.0
     */
    fun notDigit(): RegexBuilder {
        pattern.append("\\D")
        return this
    }

    /**
     * Appends the pattern for a word character (`\w`) to the current regex.
     *
     * A word character typically represents an alphanumeric character
     * including the underscore, depending on the regex flavor.
     *
     * @return The current instance of the RegexBuilder for chaining further calls.
     * @since 1.0.0
     */
    fun wordChar(): RegexBuilder {
        pattern.append("\\w")
        return this
    }

    /**
     * Appends a pattern matching any non-word character to the regex.
     *
     * Non-word characters include any character that is not a letter (`A-Za-z`), digit (`0-9`), or underscore (`_`).
     *
     * @return The current instance of [RegexBuilder] with the updated pattern.
     * @since 1.0.0
     */
    fun notWordChar(): RegexBuilder {
        pattern.append("\\W")
        return this
    }

    /**
     * Appends the pattern representing a whitespace character to the current regex.
     *
     * A whitespace character is defined by the regular expression `\s`.
     * This includes spaces, tabs, and other forms of whitespace.
     *
     * @return The current instance of the `RegexBuilder` for method chaining.
     * @since 1.0.0
     */
    fun whitespace(): RegexBuilder {
        pattern.append("\\s")
        return this
    }

    /**
     * Appends a pattern that matches any non-whitespace character to the regex being built.
     *
     * @return This RegexBuilder instance, allowing for method chaining.
     * @since 1.0.0
     */
    fun notWhitespace(): RegexBuilder {
        pattern.append("\\S")
        return this
    }

    /**
     * Appends the pattern to match the start of a string.
     *
     * @return the current instance of [RegexBuilder] to allow method chaining.
     * @since 1.0.0
     */
    fun startOfString(): RegexBuilder {
        pattern.append("^")
        return this
    }

    /**
     * Appends the anchor for the end of the string (`$`) to the regex pattern.
     *
     * @return The current instance of [RegexBuilder].
     * @since 1.0.0
     */
    fun endOfString(): RegexBuilder {
        pattern.append("$")
        return this
    }

    /**
     * Starts a character class in the regex pattern by appending the opening bracket "[".
     * This method modifies the current pattern by adding the given character class delimiter.
     *
     * @return The updated instance of [RegexBuilder] for further chaining of regex building methods.
     * @since 1.0.0
     */
    fun startOfCharClass(): RegexBuilder {
        pattern.append("[")
        isInCharClass = true
        return this
    }

    /**
     * Appends the closing bracket of a character class to the current regex pattern.
     *
     * This method finalizes the definition of a character class and prepares the builder
     * for further construction of the regex pattern.
     *
     * @return The current instance of [RegexBuilder] to enable method chaining.
     * @since 1.0.0
     */
    fun endOfCharClass(): RegexBuilder {
        pattern.append("]")
        isInCharClass = false
        return this
    }

    /**
     * Appends a word boundary (`\b`) to the regex pattern. A word boundary matches positions where
     * a word character is adjacent to a non-word character or the start/end of the string.
     *
     * @return The current instance of [RegexBuilder] to allow method chaining.
     * @since 1.0.0
     */
    fun wordBoundary(): RegexBuilder {
        pattern.append("\\b")
        return this
    }

    /**
     * Repeats the pattern exactly `n` times.
     *
     * @param n The number of times the pattern should be repeated.
     * @return Returns the updated instance of the `RegexBuilder` with the repeated pattern appended.
     * @throws NumberSignException if the number of repetitions is not positive.
     * @since 1.0.0
     */
    operator fun times(n: Int): RegexBuilder {
        n.isPositive || throw NumberSignException("The number of repetitions must be positive.")
        pattern.append("{").append(n).append("}")
        return this
    }

    /**
     * Appends a quantifier to the regex pattern indicating that the preceding element
     * must occur within the specified range of occurrences.
     *
     * @param range the range of occurrences (inclusive) that the preceding element should match.
     * @return the updated RegexBuilder instance with the applied quantifier.
     * @throws NumberSignException if the range of repetitions is not positive.
     * @since 1.0.0
     */
    operator fun times(range: IntRange): RegexBuilder {
        (range.first.isPositive && range.last.isPositive) || throw NumberSignException("The number of repetitions must be positive.")
        pattern.append("{").append(range.first).append(",").append(range.last).append("}")
        return this
    }
    
    /**
     * Appends a quantifier to the regex pattern that matches the preceding element 
     * a minimum number of times specified by the `min` parameter, with no upper limit.
     *
     * @param min The minimum number of times the preceding element must match. Must be a non-negative integer.
     * @return The updated RegexBuilder instance with the appended quantifier.
     * @throws NumberSignException if the minimum number of repetitions is not positive or zero.
     * @since 1.0.0
     */
    fun timesOrMore(min: Int): RegexBuilder {
        min.isNotNegative || throw NumberSignException("The minimum number of repetitions must be positive or zero.")
        pattern.append("{").append(min).append(",}")
        return this
    }
    
    /**
     * Appends a quantifier to the current regex pattern, specifying a range of 0 up to the given maximum number of occurrences.
     *
     * @param max The maximum number of times the preceding element is matched. Must be a non-negative integer.
     * @return The current instance of RegexBuilder with the updated pattern.
     * @throws NumberSignException if the maximum number of repetitions is not positive.
     * @since 1.0.0
     */
    fun timesOrLess(max: Int): RegexBuilder {
        max.isPositive || throw NumberSignException("The maximum number of repetitions must be positive.")
        pattern.append("{0,").append(max).append("}")
        return this
    }

    /**
     * Appends a zero-or-more quantifier (`*`) to the current regex pattern,
     * which matches the preceding element zero or more times.
     *
     * @return The current instance of [RegexBuilder] with the updated pattern.
     * @since 1.0.0
     */
    fun zeroOrMore(): RegexBuilder {
        pattern.append("*")
        return this
    }

    /**
     * Appends the quantifier for matching one or more occurrences of the preceding element
     * to the regular expression pattern.
     *
     * @return The current instance of [RegexBuilder] with the updated pattern.
     * @since 1.0.0
     */
    fun oneOrMore(): RegexBuilder {
        pattern.append("+")
        return this
    }

    /**
     * Appends the quantifier for making the preceding element optional to the pattern.
     *
     * The optional quantifier matches the preceding element zero or one time.
     *
     * @return The current instance of [RegexBuilder] to allow method chaining.
     * @since 1.0.0
     */
    fun optional(): RegexBuilder {
        pattern.append("?")
        return this
    }

    /**
     * Starts a capturing group in the regex pattern by appending an opening parenthesis `(`.
     *
     * @return The current instance of [RegexBuilder] to allow method chaining.
     * @since 1.0.0
     */
    fun startOfGroup(): RegexBuilder {
        pattern.append("(")
        return this
    }

    /**
     * Ends the most recent capturing or non-capturing group in the regex pattern.
     *
     * This method appends a closing parenthesis ")" to the regular expression being built,
     * marking the end of a group. Groups allow sections of the pattern to be logically
     * grouped together or captured for later use.
     *
     * @return The current instance of [RegexBuilder] for method chaining.
     * @since 1.0.0
     */
    fun endOfGroup(): RegexBuilder {
        pattern.append(")")
        return this
    }

    /**
     * Adds an "OR" condition to the current regex pattern by appending the "|" operator.
     *
     * @return The current instance of [RegexBuilder] with the updated pattern.
     * @since 1.0.0
     */
    fun or(): RegexBuilder {
        pattern.append("|")
        return this
    }

    /**
     * Appends the provided raw regular expression to the current pattern.
     *
     * @param regex The raw regular expression to be appended to the pattern.
     * @return The updated RegexBuilder instance with the appended pattern.
     * @since 1.0.0
     */
    fun raw(regex: CharSequence): RegexBuilder {
        pattern.append(regex)
        return this
    }

    /**
     * Starts a non-capturing group in the regular expression pattern.
     * A non-capturing group is used to group parts of a regex without creating a capturing group.
     *
     * @return The current instance of [RegexBuilder], allowing for method chaining.
     * @since 1.0.0
     */
    fun startOfNonCapturingGroup(): RegexBuilder {
        pattern.append("(?:")
        return this
    }

    /**
     * Appends the start of a positive lookahead assertion to the regex pattern.
     * A positive lookahead ensures that the specified subpattern exists at the current position
     * without including it in the matched result.
     *
     * @return The current instance of [RegexBuilder] to enable method chaining.
     * @since 1.0.0
     */
    fun startOfPositiveLookahead(): RegexBuilder {
        pattern.append("(?=")
        return this
    }

    /**
     * Starts the definition of a negative lookahead in the regular expression pattern.
     * A negative lookahead ensures that the following part of the string does not match
     * the specified pattern within the lookahead.
     *
     * @return the current [RegexBuilder] instance for method chaining
     * @since 1.0.0
     */
    fun startOfNegativeLookahead(): RegexBuilder {
        pattern.append("(?!")
        return this
    }

    /**
     * Enables the "dot all" mode in the regex pattern, allowing the dot `.` character
     * to match newline characters in addition to any other character.
     *
     * @return the current instance of [RegexBuilder] for method chaining
     * @since 1.0.0
     */
    fun dotAllMode(): RegexBuilder {
        pattern.insert(0, "(?s)")
        return this
    }

    /**
     * Enables case-insensitive matching for the pattern being built.
     *
     * This method modifies the current pattern by appending the case-insensitivity flag,
     * allowing the resulting regular expression to match text regardless of letter case.
     *
     * @return The instance of [RegexBuilder] with case-insensitive mode enabled.
     * @since 1.0.0
     */
    fun enableCaseInsensitiveMode(): RegexBuilder {
        pattern.insert(0, "(?i)")
        return this
    }

    /**
     * Disables case-insensitive matching for the regular expression being built.
     * This method prepends a case-sensitivity flag modifier to the pattern.
     *
     * @return The current instance of [RegexBuilder] for method chaining.
     * @since 1.0.0
     */
    fun disableCaseInsensitiveMode(): RegexBuilder {
        pattern.insert(0, "(?-i)")
        return this
    }
    
    /**
     * Enables multiline mode for the regex pattern being built. 
     * In multiline mode, the `^` and `$` meta-characters match the start and end of a line
     * respectively, instead of the start and end of the entire input.
     *
     * @return This `RegexBuilder` instance with multiline mode enabled.
     * @since 1.0.0
     */
    fun multilineMode(): RegexBuilder {
        pattern.insert(0, "(?m)")
        return this
    }
    
    /**
     * Enables Unicode-aware mode for the regular expression being constructed.
     * This modifies the pattern to treat character classes and metacharacters
     * in a Unicode-compliant manner.
     *
     * @return The current instance of [RegexBuilder], allowing for method chaining.
     * @since 1.0.0
     */
    fun unicodeMode(): RegexBuilder {
        pattern.insert(0, "(?u)")
        return this
    }

    /**
     * Starts a named capturing group in the regex pattern.
     *
     * @param name The name of the capturing group to be started. Must be a valid identifier for group naming.
     * @return The current instance of RegexBuilder for method chaining.
     * @since 1.0.0
     */
    fun startOfNamedGroup(name: String): RegexBuilder {
        pattern.append("(?<").append(name).append(">")
        return this
    }

    /**
     * Appends a character class to the regular expression pattern, escaping special characters
     * as needed to ensure proper syntax.
     *
     * @param chars a string containing the characters to include in the character class
     * @return the current instance of RegexBuilder for method chaining
     * @since 1.0.0
     */
    fun charClassEscaped(chars: String): RegexBuilder {
        pattern.append("[")
        chars.forEach {
            if (it in "\\^[]-") pattern.append("\\")
            pattern.append(it)
        }
        pattern.append("]")
        return this
    }

    /**
     * Adds a backreference to a previously captured group in the regular expression being constructed.
     * A backreference allows matching the same text as a previously captured group based on its index.
     *
     * @param index The index of the previously captured group to reference. Indices start from 1.
     * @return The current instance of [RegexBuilder] to allow method chaining.
     * @since 1.0.0
     */
    fun backreference(index: Int): RegexBuilder {
        pattern.append("\\").append(index)
        return this
    }

    /**
     * Adds a named backreference to the built regex pattern using the provided group name.
     * Named backreferences allow referencing a named capturing group within the regex.
     *
     * @param name The name of the capturing group to reference.
     * @return The current instance of [RegexBuilder] for method chaining.
     * @since 1.0.0
     */
    fun namedBackreference(name: String): RegexBuilder {
        pattern.append("\\k<").append(name).append(">")
        return this
    }

    /**
     * Enables extended mode for the current regex pattern.
     * Extended mode allows for improved readability in regex patterns by ignoring
     * whitespace and enabling comments within the pattern.
     *
     * @return The current instance of RegexBuilder with extended mode applied.
     * @since 1.0.0
     */
    fun extendedMode(): RegexBuilder {
        pattern.insert(0, "(?x)")
        return this
    }

    /**
     * Returns the string representation of the underlying `pattern`.
     * This method does not modify the state of the object.
     *
     * @return The string representation of the `pattern`.
     * @since 1.0.0
     */
    fun peek() = pattern.toString()

    /**
     * Constructs a new regular expression with the specified pattern and options.
     *
     * @param options a vararg of [RegexOption] that configures the behavior of the regular expression.
     * @return a [Regex] instance created with the specified pattern and options.
     * @throws MalformedInputException if the provided pattern is invalid.
     * @since 1.0.0
     */
    fun build(vararg options: RegexOption): Regex = tryOrThrow({ -> MalformedInputException("Invalid regular expression") }) {
        Regex(pattern.toString(), options.toSet())
    }

    /**
     * Constructs a `Code` object by building and converting the provided regular expression options.
     *
     * @param options A variable number of `RegexOption` flags used to configure the regular expression.
     * @return A `Code` object generated based on the provided options.
     * @since 1.0.0
     */
    fun buildAsCode(vararg options: RegexOption): Code = build(*options).toCode()
}

/**
 * Creates and returns a new instance of [RegexBuilder].
 *
 * This method provides a convenient way to construct a custom regular expression
 * using the [RegexBuilder] API. The builder allows step-by-step configuration
 * of the desired pattern before generating the final [Regex] instance.
 *
 * @receiver [Regex.Companion] The companion object of [Regex].
 * @return A new [RegexBuilder] instance for constructing regular expressions.
 * @since 1.0.0
 */
fun Regex.Companion.builder() = RegexBuilder()

/**
 * Builds a regular expression using the provided builder action.
 *
 * This method allows for concise and structured construction of regular expressions.
 *
 * @param options An optional array of [RegexOption]s that will be applied to the regular expression.
 * @param builderAction A lambda with a receiver of type RegexBuilder, used to define the
 *   desired regular expression structure.
 * @return The constructed [Regex] instance based on the provided builder action.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
fun buildRegex(vararg options: RegexOption, builderAction: ReceiverConsumer<RegexBuilder>): Regex {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return RegexBuilder().apply(builderAction).build(*options)
}

/**
 * Constructs and returns a `Regex` object using the provided `RegexBuilder` and a configuration action.
 *
 * @param builder The `RegexBuilder` instance used to create the `Regex`.
 * @param options An optional array of [RegexOption]s that will be applied to the regular expression.
 * @param builderAction A lambda or function that configures the `RegexBuilder`.
 * @return A compiled `Regex` based on the configuration provided by the `builderAction`.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
fun buildRegex(builder: RegexBuilder, vararg options: RegexOption, builderAction: ReceiverConsumer<RegexBuilder>): Regex {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return builder.apply(builderAction).build(*options)
}