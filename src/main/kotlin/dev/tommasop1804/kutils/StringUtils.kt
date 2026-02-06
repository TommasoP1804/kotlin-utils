@file:JvmName("StringUtilsKt")
@file:Suppress("unused", "kutils_indexof_as_char_invoke", "kutils_ignorecase_function", "kutils_drop_as_int_invoke", "kutils_substring_as_int_invoke",
    "kutils_take_as_int_invoke", "kutils_null_check"
)
@file:Since("1.0.0")
@file:OptIn(ExperimentalExtendedContracts::class)

package dev.tommasop1804.kutils

import com.github.lalyos.jfiglet.FigletFont
import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.base.Base62
import dev.tommasop1804.kutils.classes.coding.JSON.Companion.MAPPER
import dev.tommasop1804.kutils.classes.registry.Contact.Email.Companion.EMAIL_REGEX
import dev.tommasop1804.kutils.exceptions.*
import org.apache.commons.codec.binary.Base32
import org.bouncycastle.jcajce.provider.digest.*
import org.bouncycastle.util.encoders.Hex
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.security.MessageDigest
import java.util.*
import java.util.Locale.getDefault
import java.util.regex.Pattern
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.ExperimentalExtendedContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Provides an empty string as a constant property of the String companion object.
 * This can be used to avoid creating multiple instances of empty strings in the code,
 * improving performance and readability.
 *
 * @return An empty string.
 * @since 1.0.0
 */
val String.Companion.EMPTY: String get() = ""

/**
 * A constant property representing a single space character.
 * This can be used for operations requiring a space string.
 *
 * @since 1.0.0
 */
val String.Companion.SPACE: String get() = " "

/**
 * Represents the hypen character ('-') as a constant property of the Char companion object.
 *
 * This property provides a convenient way to reference the hypen character without
 * explicitly writing its literal value in the code.
 *
 * @since 1.0.0
 */
val String.Companion.HYPEN get() = "-"

/**
 * Represents the Unicode EN DASH character (U+2013).
 *
 * The EN DASH is commonly used to represent ranges, such as dates or numbers,
 * and is slightly longer than a hyphen but shorter than an em dash.
 *
 * @since 1.0.0
 */
val String.Companion.EN_DASH get() = '–'
/**
 * Represents the Unicode character 'EM DASH' (U+2014).
 * This is a punctuation mark used to create a strong break in the structure of a sentence.
 * It is wider than a hyphen and en dash.
 *
 * @since 1.0.0
 */
val String.Companion.EM_DASH get() = '—'

/**
 * Represents the underscore character ('_').
 * This constant is defined as a property of the `Char` companion object
 * for convenience, allowing access without directly using a literal.
 *
 * @receiver The companion object of the `Char` class.
 * @return The underscore character.
 * @since 1.0.0
 */
val String.Companion.UNDERSCORE get() = "_"

/**
 * A constant representing the forward slash character ('/').
 * This is often used as a directory path separator in Unix-based systems
 * or in URLs to separate path segments.
 *
 * @receiver String.Companion
 * @since 1.0.0
 */
val String.Companion.SLASH get() = "/"

/**
 * Represents the backslash character ('\') as a constant for convenient reuse.
 *
 * This constant can be used wherever the backslash character is needed,
 * instead of repeatedly specifying the character literal.
 *
 * @since 1.0.0
 */
val String.Companion.BACKSLASH get() = "\\"

/**
 * Represents the pipe character ('|') as a constant value associated with the String.Companion object.
 * This can be useful for improving code readability or using consistent symbolic constants.
 *
 * @since 1.0.0
 */
val String.Companion.PIPE get() = "|"

/**
 * A constant property representing the asterisk (*) character.
 * This character is commonly used as a wildcard or delimiter in various contexts.
 *
 * @since 1.0.0
 */
val String.Companion.STAR get() = "*"

/**
 * Represents the dot character ('.') as a constant property of the String.Companion object.
 *
 * This property provides a convenient way to reference the dot character
 * without directly using the character literal.
 *
 * @since 1.0.0
 */
val String.Companion.DOT get() = "."

/**
 * A constant representing the comma character (',').
 *
 * This is a common delimiter character used in various programming contexts,
 * particularly in CSV (Comma-Separated Values) files, separating elements in
 * a list, or within language-specific syntax structures.
 *
 * @since 1.0.0
 */
val String.Companion.COMMA get() = ","

/**
 * Represents the semicolon character (';').
 * This is a shorthand property for accessing the semicolon character in a concise and readable manner.
 *
 * @receiver String.Companion
 * @return The semicolon character.
 * @since 1.0.0
 */
val String.Companion.SEMICOLON get() = ";"

/**
 * A constant property representing the colon character `':'`.
 *
 * This property is part of the `Char` companion object and can be used
 * wherever a colon character is needed in a Kotlin program.
 *
 * @since 1.0.0
 */
val String.Companion.COLON get() = ":"

/**
 * A constant representing the plus character ('+').
 * This is a companion property of the `Char` class.
 *
 * @since 1.0.0
 */
val String.Companion.PLUS get() = "+"

/**
 * Represents the equals sign character ('=').
 * This property is a shorthand for accessing the equals sign.
 *
 * @receiver String.Companion
 * @return The equals sign character.
 * @since 1.0.0
 */
val String.Companion.EQUALS_SIGN get() = "="

/**
 * Represents the exclamation mark character ('!').
 * This property provides a convenient way to access the exclamation mark
 * as a constant value.
 *
 * @receiver String.Companion The companion object of the Char class.
 * @return The exclamation mark character.
 * @since 1.0.0
 */
val String.Companion.EXCLAMATION_MARK get() = "!"

/**
 * Represents the question mark character ('?').
 *
 * This constant provides a shorthand way to access the question
 * mark character for use in various operations such as
 * string manipulation, pattern matching, or symbol reference.
 *
 * @since 1.0.0
 */
val String.Companion.QUESTION_MARK get() = "?"

/**
 * Represents the hash character ('#'), commonly used in various contexts such as social media,
 * hashtags, or as a special symbol in programming or formatted strings.
 *
 * This property is a part of the Char companion object, providing a quick and accessible
 * reference to the hash character without the need to define it explicitly in code.
 *
 * @since 1.0.0
 */
val String.Companion.HASH get() = "#"

/**
 * Represents the '@' character as a constant property within the `Char` companion object.
 *
 * This property provides a convenient way to access the '@' character without
 * the need to explicitly define it elsewhere in the code.
 *
 * @since 1.0.0
 */
val String.Companion.AT get() = "@"

/**
 * Represents the ampersand character ('&').
 *
 * This property provides a convenient way to access the ampersand character
 * as a companion object property of the `Char` type.
 *
 * @since 1.0.0
 */
val String.Companion.AND get() = "&"

/**
 * Represents the dollar sign ('$') character as a constant for the Char companion object.
 *
 * This property provides a convenient way to access the dollar sign character without
 * needing to manually specify it each time.
 *
 * @receiver String.Companion
 * @return The dollar sign character ('$').
 * @since 1.0.0
 */
val String.Companion.DOLLAR get() = "$"

/**
 * A constant property representing the percent (%) symbol.
 * It is a shorthand for the '%' character, providing an easily
 * identifiable and reusable symbol for percentage-related operations.
 *
 * @since 1.0.0
 */
val String.Companion.PERCENT get() = '%'

/**
 * A constant property representing the apostrophe character ('\'').
 *
 * This property can be used wherever a single-quote character is needed
 * in the context of operations related to `Char`.
 *
 * @since 1.0.0
 */
val String.Companion.APOSTROPHE get() = '\''

/**
 * Represents the quotation mark character ('"') as a read-only property of the [Char] companion object.
 * This can be used when a quotation mark character is needed, avoiding the direct use of string literals.
 *
 * @since 1.0.0
 */
val String.Companion.QUOTATION_MARK get() = '"'

/**
 * Checks if the given CharSequence is not null and not empty.
 *
 * This function utilizes Kotlin contracts to provide better null-safety inference
 * when used in conditional checks.
 *
 * @return true if the CharSequence is not null and not empty, false otherwise.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNotNullOrEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrEmpty != null)
    }
    return !isNullOrEmpty()
}

/**
 * Checks if the given [CharSequence] is either `null` or consists solely of whitespace characters.
 *
 * @receiver The [CharSequence] to be checked.
 * @return `true` if the [CharSequence] is `null` or blank (only contains whitespace characters),
 *         `false` otherwise.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNullOrBlank(): Boolean {
    contract {
        returns(false) implies (this@isNullOrBlank != null)
    }
    return isNull() || isBlank()
}
/**
 * Checks if the given CharSequence is not null and not blank.
 *
 * A CharSequence is considered "not blank" if it contains at least one non-whitespace character.
 * This function provides a concise way to ensure that a nullable CharSequence contains meaningful content.
 *
 * @return `true` if the CharSequence is not null and contains at least one non-whitespace character,
 *         otherwise `false`.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNotNullOrBlank(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrBlank != null)
    }
    return !isNullOrBlank()
}

/**
 * Checks if the string contains only lowercase characters.
 *
 * This function returns true if the string is not empty and all characters are in lowercase.
 * An empty string will return false.
 *
 * @receiver The string to be checked for lowercase characters.
 * @return `true` if the string is not empty and all characters are lowercase, `false` otherwise.
 * @since 1.0.0
 */
val String.isLowerCase get() = isNotEmpty() && this == lowercase()
/**
 * Checks if the entire string is in uppercase.
 *
 * This function evaluates whether all characters in the string are uppercase.
 * It returns false if the string is empty or if any character within the
 * string is not uppercase.
 *
 * @receiver The string to be checked.
 * @return `true` if the string is non-empty and entirely uppercase,
 *         `false` otherwise.
 * @since 1.0.0
 */
val String.isUpperCase get() = isNotEmpty() && this == uppercase()
/**
 * Checks if the string contains both uppercase and lowercase characters.
 * A string is considered mixed case if it is not empty, contains at least one uppercase character,
 * and at least one lowercase character. If the string is entirely uppercase, entirely lowercase,
 * or empty, this method will return false.
 *
 * @receiver the string to be checked.
 * @return `true` if the string is mixed case, otherwise `false`.
 * @since 1.0.0
 */
val String.isMixedCase get() = isNotEmpty() && !isLowerCase && !isUpperCase

/**
 * Determines if the string meets the criteria for sentence case.
 *
 * A string is considered to be in sentence case if it is not empty,
 * the first character is an uppercase letter, and all subsequent
 * characters are not uppercase.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string is in sentence case, otherwise `false`.
 * @since 1.0.0
 */
val String.isSentenceCase get() = isNotEmpty() && this[0].isUpperCase() && toCharArray().drop(1).none { it.isUpperCase() }

/**
 * Checks if all words in the string are in title case format.
 *
 * A string is considered to be in title case if it is not empty
 * and each word within the string starts with an uppercase letter
 * followed by only lowercase letters.
 *
 * The method splits the string by spaces to evaluate each word
 * individually and verifies that all words conform to the title case format.
 *
 * @receiver the string to check.
 * @return true if the string adheres to the title case format; false otherwise.
 * @since 1.0.0
 */
val String.isTitleCase get() = isNotEmpty() && split(" ").all { it.isSentenceCase }

/**
 * Checks if the string is in UPPER_SNAKE_CASE format.
 *
 * A string is considered to be in UPPER_SNAKE_CASE if it is non-empty and consists
 * only of uppercase letters, underscores, or digits.
 *
 * @receiver The string to check.
 * @return `true` if the string is in UPPER_SNAKE_CASE, otherwise `false`.
 * @since 1.0.0
 */
val String.isUpperSnakeCase get() = isNotEmpty() && toCharArray().all { it == '_' || it.isUpperCase() || it.isDigit() }

/**
 * Checks if the string is in snake_case format.
 *
 * A string is considered to be in snake_case if it meets the following criteria:
 * - It is not empty.
 * - It only contains lowercase letters, digits, and underscores.
 *
 * @receiver The string to be evaluated.
 * @return true if the string conforms to the snake_case format; false otherwise.
 * @since 1.0.0
 */
val String.isSnakeCase get() = isNotEmpty() && toCharArray().all { it == '_' || it.isLowerCase() || it.isDigit() }

/**
 * Checks if a string adheres to the title snake case format.
 *
 * A string is considered to be in title snake case if:
 * - It is not empty.
 * - It contains only valid characters: underscores (`_`), letters, or digits.
 * - Words separated by underscores follow the title case convention.
 *
 * This function evaluates the string by first ensuring each character
 * is valid and then replacing any hyphens (`-`) with spaces to check if
 * the resulting words conform to the title case format.
 *
 * @receiver The string to evaluate.
 * @return `true` if the string adheres to title snake case format; otherwise, `false`.
 * @since 1.0.0
 */
val String.isTitleSnakeCase get() = isNotEmpty() && toCharArray().all { (it == '_' || it.isLetter() || it.isDigit()) && replace('-', ' ').isTitleCase }

/**
 * Checks if the string is in upper kebab case format.
 *
 * Upper kebab case is defined as a string where all characters are either
 * uppercase letters, digits, or hyphens ('-'), and the string is not empty.
 *
 * @receiver the string to check
 * @return true if the string is in upper kebab case format, false otherwise
 * @since 1.0.0
 */
val String.isUpperKebabCase get() = isNotEmpty() && toCharArray().all { it == '-' || it.isUpperCase() || it.isDigit() }

/**
 * Checks if the current string is in kebab-case format.
 *
 * A string is considered to be in kebab-case if it is not empty and consists only of lowercase
 * letters, numeric digits, or hyphen (`-`) characters.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string is in kebab-case format, `false` otherwise.
 * @since 1.0.0
 */
val String.isKebabCase get() = isNotEmpty() && toCharArray().all { it == '-'  || it.isLowerCase() || it.isDigit() }

/**
 * Checks if the string is formatted in Title Kebab Case.
 *
 * A string in Title Kebab Case must:
 * - Not be empty.
 * - Contain only letters, digits, and hyphens.
 * - Represent a sequence of "words" separated by hyphens, where each word
 *   starts with an uppercase letter followed by lowercase letters.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string adheres to the Title Kebab Case format, otherwise `false`.
 * @since 1.0.0
 */
val String.isTitleKebabCase get() = isNotEmpty() && toCharArray().all { it == '-'  || it.isLetterOrDigit() } && replace('-', ' ').isTitleCase

/**
 * Checks if the string follows camelCase naming convention.
 * A camelCase string starts with a lowercase letter or a digit, contains only alphanumeric characters,
 * and does not allow non-alphanumeric symbols or whitespace.
 *
 * @receiver The string to be checked.
 * @return True if the string satisfies camelCase constraints, otherwise false.
 * @since 1.0.0
 */
val String.isCamelCase get() = isNotEmpty() && toCharArray().all { it.isLetterOrDigit() } && (get(0).isLowerCase() || get(0).isDigit())

/**
 * Checks if the string follows the PascalCase naming convention.
 *
 * A string is considered to be in PascalCase if it is not empty,
 * contains only alphanumeric characters, and starts with either
 * an uppercase letter or a digit.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string follows PascalCase rules, otherwise `false`.
 * @since 1.0.0
 */
val String.isPascalCase get() = isNotEmpty() && toCharArray().all { it.isLetterOrDigit() } && (get(0).isUpperCase() || get(0).isDigit())

/**
 * Checks if the string consists only of alphabetic characters and is not empty.
 *
 * This function evaluates whether all characters in the string are alphabetic
 * (letters) and ensures that the string contains at least one character.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string contains only alphabetic characters and is not empty,
 * otherwise `false`.
 *
 * @since 1.0.0
 */
val String.isAlphabetic get() = isNotEmpty() && all { it.isLetter() }

/**
 * Checks if the string contains only alphabetic characters and whitespace, and is not empty.
 *
 * This function evaluates whether each character in the string is either a letter
 * or a whitespace character. An empty string will return false.
 *
 * @receiver The string to check.
 * @return `true` if the string is non-empty and all characters are alphabetic or whitespace, otherwise `false`.
 * @since 1.0.0
 */
val String.isAlphabeticSpace get() = isNotEmpty() && all { it.isLetter() || it.isWhitespace() }

/**
 * Checks if the string contains only alphabetic characters or dots, and is not empty.
 *
 * This function verifies that every character in the string is either a letter or a dot ('.').
 * The string must also not be empty for this method to return true.
 *
 * @receiver The string to be evaluated.
 * @return true if the string is non-empty and composed solely of alphabetic characters or dots, false otherwise.
 * @since 1.0.0
 */
val String.isAlphabeticDot get() = isNotEmpty() && all { it.isLetter() || it == '.' }

/**
 * Determines whether a string contains only alphabetic characters, periods ('.'),
 * or whitespace characters and is not empty.
 *
 * The method checks if every character in the string satisfies the condition
 * of being a letter, a period, or a whitespace character. Returns `false` if the string
 * is empty or contains any other type of character.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string is not empty and only contains alphabetic characters,
 * periods, or whitespace; `false` otherwise.
 * @since 1.0.0
 */
val String.isAlphabeticDotSpace get() = isNotEmpty() && all { it.isLetter() || it == '.' || it.isWhitespace() }

/**
 * Checks if the string contains only alphabetic characters, dots (.), commas (,),
 * and ensures the string is not empty.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string is not empty and consists solely of alphabetic characters, dots, or commas; `false` otherwise.
 * @since 1.0.0
 */
val String.isAlphabeticDotComma get() = isNotEmpty() && all { it.isLetter() || it == '.' || it == ',' }

/**
 * Checks if the string is non-empty and contains only alphabetic characters, dots, commas, or whitespace characters.
 *
 * @receiver The string to be evaluated.
 * @return true if the string meets the criteria, otherwise false.
 * @since 1.0.0
 */
val String.isAlphabeticDotCommaSpace get() = isNotEmpty() && all { it.isLetter() || it == '.' || it == ',' || it.isWhitespace() }

/**
 * Checks if the string contains only alphanumeric characters (letters and digits).
 * The method ensures that the string is not empty and all characters are
 * either letters (uppercase or lowercase) or digits.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string is not empty and contains only alphanumeric characters,
 *         `false` otherwise.
 * @since 1.0.0
 */
val String.isAlphanumeric get() = isNotEmpty() && matches(Regex("^[a-zA-Z0-9]*$"))

/**
 * Checks if the string consists only of alphanumeric characters and spaces.
 *
 * The method verifies that all characters in the string are either letters
 * (uppercase or lowercase), digits, or spaces. It returns `true` if the
 * entire string satisfies the condition, otherwise `false`.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string is alphanumeric and may include spaces, `false` otherwise.
 * @since 1.0.0
 */
val String.isAlphanumericSpace get() = matches(Regex("^[a-zA-Z0-9 ]*$"))

/**
 * Checks if the string consists only of alphanumeric characters (a-z, A-Z, 0-9) and dots ('.').
 *
 * This function verifies that the string matches the specified pattern where only
 * letters, digits, and dot characters are allowed from start to end.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string consists solely of alphanumeric characters and dots; `false` otherwise.
 * @since 1.0.0
 */
val String.isAlphanumericDot get() = matches(Regex("^[a-zA-Z0-9.]*$"))

/**
 * Checks if the string contains only alphanumeric characters, dots, and spaces.
 *
 * The method validates the string against a regular expression that allows
 * alphabetical characters (both upper and lower case), numerical digits,
 * dots ('.'), and spaces (' ').
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string contains only allowed characters, `false` otherwise.
 * @since 1.0.0
 */
val String.isAlphanumericDotSpace get() = matches(Regex("^[a-zA-Z0-9. ]*$"))

/**
 * Checks if the string contains only numeric characters [0-9].
 *
 * This function evaluates the current string against a regular expression
 * to determine if it consists solely of numeric characters.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string contains only numeric characters, otherwise `false`.
 * @since 1.0.0
 */
val String.isNumeric get() = matches(Regex("^[0-9]*$"))

/**
 * Determines if the string consists only of numeric characters (0-9) and/or space characters.
 *
 * This method checks whether the string matches the criteria of containing only digits
 * and space characters. It returns true if the string entirely matches the pattern,
 * otherwise returns false.
 *
 * @receiver the string to evaluate
 * @return `true` if the string contains only numeric and space characters, `false` otherwise
 * @since 1.0.0
 */
val String.isNumericSpace get() = matches(Regex("^[0-9 ]*$"))

/**
 * Checks if the entire string contains only numeric characters or dots.
 *
 * The function evaluates whether all characters in the string satisfy the condition
 * of being either a digit or the dot character '.'.
 *
 * @receiver The string to be checked.
 * @return `true` if the string consists solely of numeric characters and/or dots, `false` otherwise.
 * @since 1.0.0
 */
val String.isNumericDot get() = all { it.isDigit() || it == '.' }

/**
 * Checks if the string consists solely of digit characters, dot ('.') characters, or whitespace characters.
 *
 * @receiver The string to be checked.
 * @return `true` if the string only contains digit characters, dots, or whitespace; otherwise, `false`.
 * @since 1.0.0
 */
val String.isNumericDotSpace get() = all { it.isDigit() || it == '.' || it.isWhitespace() }

/**
 * Checks if a string contains only digits or commas.
 *
 * This function verifies whether all characters in the string are either numeric digits
 * or the comma character (','). It iterates through each character in the string
 * and ensures that it meets the specified criteria. If all characters are valid,
 * the function returns true; otherwise, it returns false.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string consists solely of numeric characters and/or commas, `false` otherwise.
 * @since 1.0.0
 */
val String.isNumericComma get() = all { it.isDigit() || it == ',' }

/**
 * Checks if the string contains only numeric digits, commas, or whitespace characters.
 *
 * This function verifies that every character in the string is either a digit
 * (as determined by `Character.isDigit`), a comma, or a whitespace character.
 *
 * @receiver The string to be validated.
 * @return True if all characters in the string are digits, commas, or whitespace. False otherwise.
 * @since 1.0.0
 */
val String.isNumericCommaSpace get() = all { it.isDigit() || it == ',' || it.isWhitespace() }

/**
 * Checks if the current string contains only numeric characters,
 * dots ('.'), and commas (',').
 *
 * This method evaluates every character in the string and ensures
 * that all characters are either digits, a dot, or a comma.
 *
 * @receiver The string to be evaluated.
 * @return `true` if the string consists exclusively of numeric characters,
 * dots, or commas; `false` otherwise.
 * @since 1.0.0
 */
val String.isNumericDotComma get() = all { it.isDigit() || it == '.' || it == ',' }

/**
 * Checks if the string contains only numeric characters, dots, commas, or whitespace.
 *
 * The method evaluates each character in the string to ensure it is a digit,
 * a period ('.'), a comma (','), or a whitespace character. If all characters
 * meet the criteria, the method returns true; otherwise, it returns false.
 *
 * @receiver The string to be evaluated.
 * @return true if the string consists exclusively of digits, dots, commas, or whitespace; false otherwise.
 * @since 1.0.0
 */
val String.isNumericDotCommaSpace get() = all { it.isDigit() || it == '.' || it == ',' || it.isWhitespace() }

/**
 * Checks if the string is a valid email address based on the defined email regex pattern.
 *
 * The method utilizes a predefined regex pattern to evaluate if the string matches the
 * general structure of an email address.
 *
 * @receiver The string to be evaluated as an email address.
 * @return `true` if the string matches the email pattern, `false` otherwise.
 * @since 1.0.0
 */
val String.isEmail get() = EMAIL_REGEX.matches(this)

/**
 * Checks if the string matches the pattern of a valid URL.
 *
 * This function determines whether the string conforms to a common URL format,
 * supporting protocols such as HTTP, HTTPS, FTP, and FILE.
 *
 * @receiver The string to be checked.
 * @return `true` if the string matches a valid URL pattern, `false` otherwise.
 * @since 1.0.0
 */
val String.isURL get() = matches(Regex("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"))

/**
 * Checks if the string contains all the specified substrings.
 *
 * @param substrings The substrings to check for in the string.
 * @return `true` if the string contains all the specified substrings, or `false` if any of them are missing. Returns `false` if the string is null.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.containsAll(vararg substrings: CharSequence): Boolean {
    if (isNull()) return false
    return substrings.all { contains(it) }
}

/**
 * Checks whether the nullable string contains all the specified characters.
 *
 * @param subchars A vararg parameter representing the characters to check for in the string.
 * @return `true` if the nullable string is not null and contains all the specified characters,
 *         `false` otherwise.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.containsAll(vararg subchars: Char): Boolean {
    if (isNull()) return false
    return subchars.all { contains(it) }
}

/**
 * Checks if the string contains any of the specified substrings.
 *
 * @param substrings The substrings to look for within the string.
 * @return `true` if the string contains at least one of the specified substrings,
 *         `false` if it does not, or if the string is `null`.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.containsAny(vararg substrings: CharSequence): Boolean {
    if (isNull()) return false
    return substrings.any { contains(it) }
}

/**
 * Checks if the string contains any of the specified characters.
 *
 * @param subchars The characters to check for in the string.
 * @return `true` if the string contains any of the specified characters, or `false` if none are found or if the string is null.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.containsAny(vararg subchars: Char): Boolean {
    if (isNull()) return false
    return subchars.any { contains(it) }
}

/**
 * Checks if the nullable string does not contain any of the provided substrings.
 *
 * @param substrings the substrings to check against.
 * @return `true` if none of the provided substrings are present in the string,
 *         `false` otherwise.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.containsNone(vararg substrings: CharSequence): Boolean = !containsAny(*substrings)

/**
 * Checks if the string does not contain any of the provided characters.
 *
 * @param subchars the characters to check for exclusion in the string
 * @return true if none of the provided characters are found in the string, otherwise false
 * @receiver The string.
 * @since 1.0.0
 */
fun String.containsNone(vararg subchars: Char): Boolean = !containsAny(*subchars)

/**
 * Checks if the string contains only characters from the given set of valid characters.
 *
 * @param valid The sequences of valid characters to check against. Each sequence can contain
 *              one or more characters that are considered valid.
 * @return `true` if the string contains only characters from the valid set and is not null,
 *         otherwise returns `false`.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.containsOnly(vararg valid: CharSequence): Boolean {
    if (isNull()) return false
    for (char in this) {
        var contains = false
        for (c in valid) {
            if (c.indexOf(char) >= 0) {
                contains = true
                break
            }
        }
        if (!contains) return false
    }
    return true
}

/**
 * Checks if the string contains only the specified valid characters.
 *
 * @param valid the characters to validate against
 * @return true if the string contains only the specified valid characters, false otherwise or if the string is null
 * @receiver The string.
 * @since 1.0.0
 */
fun String.containsOnly(vararg valid: Char): Boolean {
    if (isNull()) return false
    for (char in this) {
        var contains = false
        for (c in valid) {
            if (c == char) {
                contains = true
                break
            }
        }
        if (!contains) return false
    }
    return true
}

// OPERATION

/**
 * Abbreviates a String to a specified maximum width, appending an abbreviation marker (default is "...")
 * at the appropriate position if necessary.
 *
 * If the specified `maxWidth` is smaller than the abbreviation marker's length plus one,
 * or if the `maxWidth` with the offset is insufficient, an exception is thrown.
 *
 * @param abbrevMarker The abbreviation marker to append to the String when it is abbreviated.
 *                      Defaults to "...".
 * @param offset The starting point within the String for abbreviation. If the offset is beyond
 *               the String's length, it is adjusted to the String's end.
 * @param maxWidth The maximum allowed width of the result, including the abbreviation marker if applied.
 *                 Must be greater than the abbreviation marker's length plus one.
 * @return The abbreviated String if the length exceeds `maxWidth`, or the original String if no abbreviation is necessary.
 *         Returns null if the original String is null.
 * @throws IllegalArgumentException If `maxWidth` is less than the minimum required width (marker length plus one),
 *                                  or if `maxWidth` with offset is insufficient.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.abbreviate(abbrevMarker: CharSequence = "...", offset: Int = 0, maxWidth: Int): String {
    if (isNotEmpty() && "" == abbrevMarker && maxWidth > 0)
        return take(maxWidth)

    if (isEmpty() || abbrevMarker.isEmpty()) return this

    val abbrevMarkerLength = abbrevMarker.length
    val minAbbrevWidth = abbrevMarkerLength + 1
    val minAbbrevWidthOffset = abbrevMarkerLength + abbrevMarkerLength + 1
    if (maxWidth < minAbbrevWidth) throw IllegalArgumentException("Minimum abbreviation width is $minAbbrevWidth")

    var newOffset = offset
    if (length <= maxWidth) return this
    if (offset > length) newOffset = length
    if (length - newOffset <= maxWidth - abbrevMarkerLength) newOffset = length - (maxWidth - abbrevMarkerLength)
    if (newOffset < abbrevMarkerLength + 1) return substring(0, maxWidth - abbrevMarkerLength) + abbrevMarker
    if (maxWidth < minAbbrevWidthOffset) throw kotlin.IllegalArgumentException("Minimum abbreviation width with offset is $minAbbrevWidthOffset")

    val result1 = abbrevMarker.toString() + substring(newOffset, length).abbreviate(abbrevMarker = abbrevMarker, maxWidth = maxWidth - abbrevMarkerLength)
    val result2 = abbrevMarker.toString() + substring(length - (maxWidth - abbrevMarkerLength), length)
    return if ((newOffset + maxWidth - abbrevMarkerLength) < length) result1 else result2
}

/**
 * Abbreviates the string by replacing its middle part with the specified sequence if the string exceeds the given length.
 *
 * @param middle the sequence that will replace the middle part of the string if it exceeds the specified length
 * @param length the maximum permissible length of the resulting string, including the middle sequence
 * @return the abbreviated string if the conditions are met; otherwise, the original string or null if the receiver is null
 * @receiver The string.
 * @since 1.0.0
 */
fun String.abbreviateMiddle(middle: CharSequence, length: Int): String {
    if (isNotEmpty() && middle.isNotEmpty() && length < this.length && length >= middle.length + 2) {
        val targetSting = length - middle.length
        val startOffset = targetSting / 2 + targetSting % 2
        val endOffset = this.length - targetSting / 2

        return substring(0, startOffset) + middle + substring(endOffset, this.length)
    }
    return this
}

/**
 * Appends the specified suffix to the string if it is not already present.
 * Allows case-insensitive comparison and checks against multiple optional suffixes.
 *
 * @param suffix the primary suffix to append if it is missing
 * @param ignoreCase whether the comparison should ignore case considerations
 * @param suffixes optional additional suffixes to check before appending
 * @return the original string with the suffix appended if missing, or the original string unchanged if null or the suffix already exists
 * @receiver The string.
 * @since 1.0.0
 */
fun String.appendIfMissing(suffix: String, ignoreCase: Boolean = false, vararg suffixes: CharSequence): String {
    if (isEmpty() || suffix.isEmpty() || endsWith(suffix, ignoreCase)) return this
    if (suffixes.any { endsWith(it, ignoreCase) }) return this
    return this + suffix
}

/**
 * Centers the string within a field of a given size by padding it with a specified character.
 * If the string is null, it returns null. If the string is empty or the specified size
 * is less than or equal to the length of the string, the original string is returned.
 *
 * @param size the total size of the field within which the string is to be centered
 * @param padChar the character to use for padding, defaults to a space character
 * @return the centered string padded with the specified character, or null if the receiver is null
 * @receiver The string.
 * @since 1.0.0
 */
fun String.center(size: Int, padChar: Char = ' '): String {
    if (isEmpty()) return this
    val pads = size - length
    if (pads <= 0) return this

    return padStart(length + (pads / 2), padChar).padEnd(size, padChar)
}

/**
 * Removes the newline characters (`\n` or `\r\n`) from the end of the string, if present.
 * If the string does not end with a newline, the original string is returned.
 * If the string is null, null is returned.
 *
 * @return the string with the trailing newline characters removed, or the original string
 *         if no newline was present, or null if the input string is null.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.chomp(): String {
    if (isEmpty()) return this

    if (length == 1) {
        val c = get(0)
        return if (c != '\r' && c != '\n') this else ""
    }
    var lastIdx = length - 1
    val last = get(lastIdx)
    if (last == '\n') {
        if (get(lastIdx - 1) == '\r') --lastIdx
    } else if (last == '\r') ++lastIdx
    return substring(0, lastIdx)
}

/**
 * Returns a copy of this string having its first character capitalized
 * if it is a lowercase character. If the first character is not lowercase
 * or the string is null, the original string is returned.
 *
 * @return A new string with the first character capitalized if applicable,
 * or the original string if no changes are necessary.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.capitalize(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }

/**
 * Joins the current string with the specified elements using the given delimiter.
 *
 * @param delimiter the sequence of characters to act as a separator. Defaults to a single space if not specified.
 * @param elements a vararg parameter representing the elements to be joined with the current string.
 * @return the resulting string after joining with the delimiter and elements, or null if the original string is null.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.join(delimiter: CharSequence? = " ", vararg elements: String): String {
    if (elements.isEmpty()) return this
    return this + delimiter + elements.joinToString(delimiter?:"") { it }
}

/**
 * Joins the elements of the provided array into a single string, with the given delimiter separating each element.
 * If the receiver string is null, returns null. If no elements are provided, returns the receiver string.
 *
 * @param delimiter The character used to separate elements in the resulting string. Defaults to a space (' ').
 * @param elements Variable number of elements to be joined with the receiver string.
 * @return The resulting concatenated string, or null if the receiver string is null.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.join(delimiter: Char? = ' ', vararg elements: String): String = join(delimiter.toString(), *elements)

/**
 * Joins the current string with the given elements, separated by the specified delimiter.
 *
 * @param delimiter the character used to separate the elements in the resulting string. Defaults to a space character if not provided.
 * @param elements an iterable collection of elements to be joined with the current string.
 * @return a new string representing the current string concatenated with the joined elements,
 *         separated by the delimiter. Returns null if the current string is null.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.join(delimiter: Char? = ' ', elements: Iterable<*>): String {
    if (elements.count() == 0) return this
    return this + delimiter + elements.joinToString(delimiter?.toString() ?: "") { it?.toString() ?: "" }
}

/**
 * Splits the nullable string using the provided regular expression and trims whitespace
 * from each resulting substring. If the string is null, an empty list is returned.
 *
 * @param regex The regular expression used to split the string.
 * @param limit The maximum number of substrings to return. A value of zero means no limit.
 * @return A list of trimmed substrings, or an empty list if the string is null.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.splitAndTrim(regex: Regex, limit: Int = 0): StringList = split(regex, limit).mappedTo { it.trim() }

/**
 * Splits the nullable string using the provided `Pattern` and trims each resulting substring.
 *
 * If the string is `null`, the method returns an empty list.
 * The splitting respects the specified `limit`.
 *
 * @param regex the `Pattern` used for splitting the string.
 * @param limit the maximum number of substrings to include in the result, where the
 * last substring contains the remaining input (default is 0, meaning no limit).
 * @return a list of trimmed substrings resulting from the split, or an empty list if the string is `null`.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.splitAndTrim(regex: Pattern, limit: Int = 0): StringList =
    splitAndTrim(regex.toRegex(), limit)

/**
 * Splits the given nullable string into a list of substrings around occurrences of the specified delimiters
 * and trims leading and trailing whitespace from each resulting substring.
 *
 * If the string is null, an empty list is returned.
 *
 * @param delimiters The delimiters to use for splitting the string.
 * @param ignoreCase If true, the delimiters are matched ignoring character case. Default is false.
 * @param limit The maximum number of substrings to return. Zero means no limit. Default is 0.
 * @return A list of trimmed substrings resulting from the split operation.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.splitAndTrim(vararg delimiters: String, ignoreCase: Boolean = false, limit: Int = 0): StringList =
    split(delimiters = delimiters, ignoreCase, limit).mappedTo { it.trim() }

/**
 * Splits the string into substrings using the specified delimiters and trims all resulting substrings.
 *
 * @param delimiters The characters to use as delimiters for splitting the string.
 * @param ignoreCase If true, the case of characters is ignored when matching delimiters. Default is false.
 * @param limit The maximum number of substrings to return. Zero or negative value means no limit. Default is 0.
 * @return A list of trimmed substrings resulting from the split operation. If the string is null, returns an empty list.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.splitAndTrim(vararg delimiters: Char, ignoreCase: Boolean = false, limit: Int = 0): StringList =
    split(delimiters = delimiters, ignoreCase, limit).mappedTo { it.trim() }

/**
 * Converts the string to sentence case where the first character is uppercase
 * and the rest of the characters are lowercase. If the string is null, it
 * returns null. If the string is empty, it returns the string as is.
 *
 * @return the sentence-cased string or null if the string is null
 * @receiver The string.
 * @since 1.0.0
 */
fun String.sentenceCase(): String {
    if (isEmpty()) return this

    return +this[0] + -substring(1)
}

/**
 * Converts the string to title case, where the first letter of each word is capitalized,
 * and the rest of the letters are in lowercase. Words are assumed to be separated by whitespace.
 *
 * @return The string converted to title case, or null if the string is null.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.titleCase(): String {
    if (isEmpty()) return this

    val words = split(Regex("\\s+")).toMutableList()
    for ((i, _) in words.withIndex())
        words[i] = +words[i][0] + -words[i].substring(1)

    return words.joinToString(" ")
}

/**
 * Converts the given string to upper snake case format.
 * If the string is null, it returns null. If the string is empty, it returns the original string.
 * Replaces all whitespace characters with underscores and converts all letters to uppercase.
 *
 * @return the string in upper snake case format or null if the input string is null
 * @receiver The string.
 * @since 1.0.0
 */
fun String.upperSnakeCase(): String {
    if (isEmpty()) return this
    return uppercase().replace(Regex("\\s+"), "_")
}

/**
 * Converts a nullable string to snake_case format by replacing all whitespace with underscores.
 * If the string is null, it returns null. An empty string remains unchanged.
 *
 * @return the converted string in snake_case format, or null if the input is null
 * @receiver The string.
 * @since 1.0.0
 */
fun String.snakeCase(): String {
    if (isEmpty()) return this
    return replace(Regex("\\s+"), "_")
}

/**
 * Converts the string to Title Snake Case by replacing spaces with underscores
 * and capitalizing the first letter of each word. If the string is null, the result is null.
 * If the string is empty, it returns the empty string.
 *
 * @return The string converted to Title Snake Case or null if the input is null.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.titleSnakeCase(): String {
    if (isEmpty()) return this
    return replace(Regex("\\s+"), "_").titleCase()
}

/**
 * Converts the given string to upper kebab case.
 * The method will convert all characters to uppercase
 * and replace all whitespace sequences with hyphens ("-").
 *
 * @return The upper kebab case representation of the string,
 *         or null if the input string is null. If the string is empty,
 *         the original string is returned unchanged.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.upperKebabCase(): String {
    if (isEmpty()) return this
    return uppercase().replace(Regex("\\s+"), "-")
}

/**
 * Converts the given string to kebab-case by replacing all whitespace characters
 * with a hyphen ("-"). If the string is null, the method returns null. If the string
 * is empty, it returns the original empty string.
 *
 * @receiver The string.
 * @return the string converted to kebab-case, or null if the input is null.
 * @since 1.0.0
 */
fun String.kebabCase(): String {
    if (isEmpty()) return this
    return replace(Regex("\\s+"), "-")
}

/**
 * Converts the string to a "Title-Kebab-Case" format. The method replaces all spaces
 * in the string with hyphens ('-') and capitalizes the first letter of each word,
 * while converting the remaining letters of each word to lowercase.
 *
 * @return The transformed string in "Title-Kebab-Case" format, or null if the input is null.
 * @receiver The string.
 * @since 1.0.0
 */
fun String.titleKebabCase(): String {
    if (isEmpty()) return this
    return replace(Regex("\\s+"), "-").titleCase()
}

/**
 * Converts the string to PascalCase format. Each word in the string will have its first letter
 * capitalized and the remaining letters converted to lowercase. All words are then concatenated
 * without spaces.
 *
 * @receiver The string.
 * @return The PascalCase representation of the string, or null if the string is null.
 * @since 1.0.0
 */
fun String.pascalCase(): String {
    if (isEmpty()) return this

    val words = split(Regex("\\s+")).toMutableList()
    for ((i, _) in words.withIndex())
        words[i] = +words[i][0] + -words[i].substring(1)

    return words.joinToString("")
}

/**
 * Converts a nullable or blank-separated string to camel case format.
 * The first word is fully lowercase, and subsequent words start with an uppercase
 * letter followed by lowercase letters.
 *
 * @receiver The string.
 * @return A camel case representation of the string or null if the string is null.
 * @since 1.0.0
 */
fun String.camelCase(): String {
    if (isEmpty()) return this

    val words = split(Regex("\\s+")).toMutableList()
    for ((i, word) in words.withIndex())
        words[i] = if (i == 0) -words[i] else +word[0] + -word.substring(1)

    return words.joinToString("")
}

/**
 * Deserializes the JSON string into an object of the specified type [T].
 *
 * This method uses a generic type parameter [T] to determine the target type
 * during deserialization. It leverages the `MAPPER` object configured for
 * JSON processing to read and convert the JSON string into the desired type.
 *
 * If the deserialization process encounters any errors, such as invalid JSON
 * structure or type mismatches, the operation will return a failed `Result`
 * wrapping the exception.
 *
 * @receiver The JSON string to be deserialized.
 * @return A [Result] containing the deserialized object of type [T] if successful,
 * or an exception if deserialization fails.
 * @since 1.0.0
 */
inline fun <reified T> String.deserialize() = runCatching { MAPPER.readValue(this, T::class.java) as T }

/**
 * Creates a Map by associating each character in the CharSequence with a value
 * produced by applying the [key] and [value] functions respectively.
 *
 * @param K the type of keys in the resulting map.
 * @param V the type of values in the resulting map.
 * @param key a function to transform each character into a key.
 * @param value a function to transform each character into a value.
 * @since 1.0.0
 */
fun <K, V> CharSequence.associate(key: Transformer<Char, K>, value: Transformer<Char, V>) =
    associate { key(it) to value(it) }

/**
 * Populates the given mutable map with key-value pairs,
 * where the keys and values are generated by applying the provided key and value functions
 * to each character of the CharSequence.
 *
 * @param K the type of keys in the destination map
 * @param V the type of values in the destination map
 * @param M the type of the destination map
 * @param destination the mutable map to be populated with the key-value pairs
 * @param key a function that takes a character as an argument and returns a key
 * @param value a function that takes a character as an argument and returns a value
 * @since 1.0.0
 */
fun <K, V, M : MutableMap<in K, in V>> CharSequence.associateTo(
    destination: M,
    key: Transformer<Char, K>,
    value: Transformer<Char, V>
) = associateTo(destination) { key(it) to value(it) }

/**
 * Returns the single character from the `CharSequence` if it contains exactly one character.
 *
 * Throws a [NoSuchElementException] if the `CharSequence` is empty.
 * Throws a [TooManyElementsException] if the `CharSequence` contains more than one character.
 *
 * @receiver The source `CharSequence` to evaluate.
 * @return The single character present in the `CharSequence`.
 * @throws NoSuchElementException If the `CharSequence` is empty.
 * @throws TooManyElementsException If the `CharSequence` contains more than one character.
 * @since 1.0.0
 */
fun CharSequence.onlyChar() = toList().run {
    if (isEmpty()) throw NoSuchElementException()
    else if (size == 1) first() else throw TooManyElementsException(size)
}
/**
 * Returns the single character in the CharSequence if it contains exactly one character,
 * or `null` if the CharSequence is empty or contains more than one character.
 *
 * This function converts the CharSequence into a list of characters. If the resulting list
 * has precisely one element, that element is returned. Otherwise, it returns `null`.
 *
 * @receiver the CharSequence to be evaluated
 * @return the single character if the CharSequence has exactly one character, or `null` otherwise
 * @since 1.0.0
 */
fun CharSequence.onlyCharOrNull() = toList().run { if (size == 1) first() else null }
/**
 * Returns the single character from the CharSequence if it contains exactly one character,
 * otherwise invokes the provided default supplier and returns its result.
 *
 * @param default A supplier that provides a default character to return if the CharSequence
 *                does not contain exactly one character.
 * @since 1.0.0
 */
fun CharSequence.onlyCharOr(default: Supplier<Char>) = toList().run { if (size == 1) first() else default() }
/**
 * Ensures that the given CharSequence contains exactly one character; otherwise, throws an exception.
 *
 * @param lazyException A supplier function providing the exception to be thrown if there is not exactly one character.
 * @throws Throwable The exception provided by lazyException if the CharSequence does not have exactly one character.
 * @since 1.0.0
 */
fun CharSequence.onlyCharOrThrow(lazyException: ThrowableSupplier) = toList().run { if (size == 1) first() else throw lazyException() }
/**
 * Filters the characters in the CharSequence based on the given predicate and ensures that exactly one result is returned.
 * If no characters match the predicate, a `NoSuchElementException` is thrown.
 * If more than one character matches the predicate, a `TooManyResultsException` is thrown.
 *
 * @param predicate a predicate used to filter the characters in the CharSequence.
 * @return the single character that matches the predicate.
 * @throws NoSuchElementException if no characters match the predicate.
 * @throws TooManyResultsException if more than one character matches the predicate.
 * @throws TooFewResultsException if there are no results after filtering.
 * @since 1.0.0
 */
fun CharSequence.onlyChar(predicate: Predicate<Char>) = toList()
    .requireOrThrow({ NoSuchElementException() }, { isNotEmpty() })
    .filter(predicate).run {
        if (size == 1) first()
        else throw if (size > 1) TooManyResultsException(size) else TooFewResultsException(size)
    }
/**
 * Returns the single character from the CharSequence that matches the given [predicate], or `null` if there are
 * no matches or more than one character matches the [predicate].
 *
 * @param predicate A function that determines whether a character should be included in the result.
 * @return The single character from the CharSequence that satisfies the [predicate], or `null` if no single match is found.
 * @since 1.0.0
 */
fun CharSequence.onlyCharOrNull(predicate: Predicate<Char>) = filter(predicate).toList().run { if (size == 1) first() else null }
/**
 * Returns the only character that matches the given [predicate] if there is exactly one such character,
 * or the result provided by the [default] supplier if there are none or more than one characters matching the [predicate].
 *
 * @param default a supplier function that provides a default character when the conditions are not met.
 * @param predicate a predicate that determines whether a character in the CharSequence should be included.
 * @return the single character matching the predicate or the default character from the supplier.
 * @since 1.0.0
 */
fun CharSequence.onlyCharOr(default: Supplier<Char>, predicate: Predicate<Char>) = filter(predicate).toList().run { if (size == 1) first() else default() }
/**
 * Filters the characters of the given CharSequence based on the provided predicate
 * and ensures there is exactly one matching character. If there is not exactly one,
 * the method throws an exception provided by the lazyException supplier.
 *
 * @param lazyException a supplier function that returns the exception to throw if
 * there is not exactly one character matching the predicate
 * @param predicate a condition that each character in the CharSequence is tested against
 * @since 1.0.0
 */
fun CharSequence.onlyCharOrThrow(lazyException: ThrowableSupplier, predicate: Predicate<Char>) = filter(predicate).toList().run { if (size == 1) first() else throw lazyException() }

/**
 * Applies the given action to each character of the string and
 * retrieves the original string.
 *
 * @param action the action to apply to each character of the string
 * @since 1.0.0
 */
fun String.peek(action: Consumer<Char>) = toList().peek(action).joinToString(String.EMPTY)

/**
 * Returns the current `CharSequence` if it is not null or empty, otherwise invokes the provided `defaultValue`
 * function and returns its result.
 *
 * This function simplifies the handling of potentially null or empty `CharSequence` values by allowing
 * a default value to be dynamically supplied when needed.
 *
 * @receiver The `CharSequence` to test.
 * @param defaultValue A lambda function that provides the default `CharSequence` value in case the current
 * receiver is null or empty.
 * @return The current `CharSequence` if it's neither null nor empty; otherwise, the result of the `defaultValue` function.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
inline fun <C : CharSequence> C?.ifNullOrEmpty(defaultValue: Supplier<C>): C {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
        (this@ifNullOrEmpty == null) holdsIn defaultValue
    }
    return if (isNullOrEmpty()) defaultValue() else this
}

/**
 * Returns this character sequence if it is not null and not blank; otherwise, evaluates and returns
 * the result of the [defaultValue] function.
 *
 * This function ensures that a non-null, non-blank value is returned, using a lambda function to
 * supply a default value when necessary.
 *
 * @receiver The `CharSequence` to test.
 * @param defaultValue A lambda function that provides a default value if the character sequence
 * is null or blank.
 * @return The original character sequence if it is not null and not blank, otherwise the result
 * of [defaultValue].
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
inline fun <C : CharSequence> C?.ifNullOrBlank(defaultValue: Supplier<C>): C {
    contract {
        callsInPlace(defaultValue, InvocationKind.AT_MOST_ONCE)
        (this@ifNullOrBlank == null) holdsIn defaultValue
    }
    return if (isNullOrBlank()) defaultValue() else this
}

/**
 * Replaces all occurrences of a specified character in the string with a given string value.
 *
 * @param oldChar the character to be replaced in the string.
 * @param newValue the string value that will replace each occurrence of the old character.
 * @param ignoreCase whether to perform a case-insensitive replacement. Defaults to false.
 * @return a new string with all occurrences of the specified character replaced by the given string value.
 * @since 1.0.0
 */
fun String.replace(oldChar: Char, newValue: String, ignoreCase: Boolean = false) =
    replace(oldChar.toString(), newValue, ignoreCase)
/**
 * Replaces all occurrences of the specified [oldValue] string in the original string
 * with the specified [newChar]. The search is case-sensitive by default, but can be
 * made case-insensitive by setting [ignoreCase] to `true`.
 *
 * @param oldValue The string to be replaced in the original string.
 * @param newChar The character that will replace each occurrence of [oldValue].
 * @param ignoreCase A boolean value that determines if the search should ignore case.
 * Defaults to `false` (case-sensitive).
 * @since 1.0.0
 */
fun String.replace(oldValue: String, newChar: Char, ignoreCase: Boolean = false) =
    replace(oldValue, newChar.toString(), ignoreCase)
/**
 * Replaces all occurrences of the regular expression [regex] in the string with a specified character [newChar].
 *
 * @param regex the regular expression to match parts of the string that should be replaced.
 * @param newChar the character to replace the matched parts of the string with.
 * @return a new string with all occurrences of the matched pattern replaced by [newChar].
 * @since 1.0.0
 */
fun String.replace(regex: Regex, newChar: Char) =
    replace(regex, newChar.toString())

/**
 * Replaces the first occurrence of the specified character [oldChar] in the string with the specified [newValue].
 * The case of the characters can be ignored based on [ignoreCase].
 *
 * @param oldChar The character to be replaced.
 * @param newValue The string that will replace the first occurrence of [oldChar].
 * @param ignoreCase `true` to ignore character case when matching [oldChar], `false` otherwise. Default is `false`.
 * @since 1.0.0
 */
fun String.replaceFirst(oldChar: Char, newValue: String, ignoreCase: Boolean = false) =
    replaceFirst(oldChar.toString(), newValue, ignoreCase)
/**
 * Replaces the first occurrence of the specified [oldValue] in the string with the string representation
 * of the specified [newChar]. The search can be optionally case-insensitive.
 *
 * @param oldValue The substring to be replaced.
 * @param newChar The character to replace the first occurrence of [oldValue].
 * @param ignoreCase `true` to perform a case-insensitive replacement; `false` otherwise.
 * @since 1.0.0
 */
fun String.replaceFirst(oldValue: String, newChar: Char, ignoreCase: Boolean = false) =
    replaceFirst(oldValue, newChar.toString(), ignoreCase)
/**
 * Replaces the first substring that matches the specified regular expression with the specified character.
 *
 * @param regex The regular expression to match the substring to be replaced.
 * @param newChar The character to replace the first matched substring with.
 * @since 1.0.0
 */
fun String.replaceFirst(regex: Regex, newChar: Char) =
    replaceFirst(regex, newChar.toString())

/**
 * Replaces the last occurrence of the specified substring [oldValue] in the string
 * with the specified [newValue]. If the [oldValue] does not exist in the string,
 * the original string is returned unchanged.
 *
 * @param oldValue The substring to be replaced.
 * @param newValue The substring to replace the [oldValue] with.
 * @since 1.0.0
 */
fun String.replaceLast(oldValue: String, newValue: String) =
    replaceRange(lastIndexOf(oldValue), lastIndexOf(oldValue) + oldValue.length, newValue)
/**
 * Replaces the last occurrence of a character in a string with a specified string.
 *
 * @param oldChar the character to be replaced in the string.
 * @param newValue the string that will replace the last occurrence of the specified character.
 * @since 1.0.0
 */
fun String.replaceLast(oldChar: Char, newValue: String) =
    replaceLast(oldChar.toString(), newValue)
/**
 * Replaces the last occurrence of the specified substring [oldValue] in this string
 * with the specified character [newChar].
 *
 * @param oldValue the substring to be replaced in the string
 * @param newChar the character that will replace the last occurrence of [oldValue]
 * @return a new string with the last occurrence of [oldValue] replaced
 * @since 1.0.0
 */
fun String.replaceLast(oldValue: String, newChar: Char) =
    replaceLast(oldValue, newChar.toString())
/**
 * Replaces the last occurrence of the substring matching the given [regex]
 * within the string with the specified [newValue]. If no match is found, the
 * string remains unchanged.
 *
 * @param regex The regular expression to match the last occurrence of a substring in the string.
 * @param newValue The replacement string to substitute for the last matched substring.
 * @since 1.0.0
 */
fun String.replaceLast(regex: Regex, newValue: String) =
    replaceRange(lastIndexOf(regex.find(this)?.value ?: ""), lastIndexOf(regex.find(this)?.value ?: "") + (regex.find(this)?.value?.length ?: 0), newValue)
/**
 * Replaces the last occurrence of a substring that matches the given regular expression with the specified character.
 *
 * @param regex The regular expression to find the substring to replace.
 * @param newChar The character to replace the matched substring with.
 * @since 1.0.0
 */
fun String.replaceLast(regex: Regex, newChar: Char) =
    replaceRange(lastIndexOf(regex.find(this)?.value ?: ""), lastIndexOf(regex.find(this)?.value ?: "") + (regex.find(this)?.value?.length ?: 0), newChar.toString())

/**
 * Returns the substring of this string that starts at the character index specified
 * by the first element of the given range and ends at the character index specified
 * by the last element of the range, inclusive.
 *
 * @param range the range of character indices to include in the substring. The range's
 *              start must not be negative, and the end must not exceed the string's length.
 * @return a substring defined by the specified range of character indices.
 * @throws IndexOutOfBoundsException if the range is invalid or out of bounds for this string.
 * @since 1.0.0
 */
@Deprecated("Use get(range) instead.", replaceWith = ReplaceWith("this[range]", "dev.tommasop1804.kutils.get"))
fun String.substring(range: IntProgression): String = substring(range.first, range.last + 1) step range.step

/**
 * Removes the specified number of characters from the end of the string.
 *
 * @param dropLast The number of characters to remove from the end of the string.
 * @return A new string with the specified number of characters removed from the end.
 * @since 1.0.0
 */
operator fun String.minus(dropLast: Int) = dropLast(dropLast)
/**
 * Removes all occurrences of the specified substring [toRemove] from this string.
 *
 * This function replaces every occurrence of [toRemove] within the original string
 * with an empty string, effectively removing it.
 *
 * @param toRemove the substring to be removed from this string
 * @return a new string with all instances of [toRemove] removed
 * @since 1.0.0
 */
operator fun String.minus(toRemove: String) = replace(toRemove, String.EMPTY)
/**
 * Removes all occurrences of the specified character from the string.
 *
 * @param toRemove the character to be removed from the string.
 * @since 1.0.0
 */
operator fun String.minus(toRemove: Char) = replace(toRemove.toString(), String.EMPTY)
/**
 * Removes all occurrences of the specified regular expression from the string.
 *
 * @param toRemove the regular expression to match and remove from the string.
 * @return a new string with all matches of the provided regular expression removed.
 * @since 1.0.0
 */
operator fun String.minus(toRemove: Regex) = replace(toRemove, String.EMPTY)

/**
 * Repeats the string a specified number of times and returns the concatenated result.
 *
 * @param n The number of times the string should be repeated. Must be a non-negative integer.
 * @return A new string consisting of the original string repeated `n` times.
 * @since 1.0.0
 */
operator fun String.times(n: Int) = buildString {
    for (i in 1..n) {
        append(this@times)
    }
}

/**
 * Splits the string using the provided regular expression and trims whitespace
 * from each resulting substring.
 *
 * @param regex The regular expression used to split the string.
 * @return A list of trimmed substrings.
 * @receiver The string to be split.
 * @since 1.0.0
 */
operator fun String.div(regex: Regex) = splitAndTrim(regex)
/**
 * Operator function to split and trim the string using a pair consisting of a regular expression
 * and a limit value. The string is split according to the provided regex with the specified limit,
 * and each resulting substring is trimmed of whitespace.
 *
 * @param regex A pair where the first element is the regular expression used to split the string,
 * and the second element is the maximum number of substrings to return. A value of zero means no limit.
 * @receiver The string to be processed.
 * @since 1.0.0
 */
@JvmName("StringDivPairRegex")
operator fun String.div(regex: Pair<Regex, Int>) = splitAndTrim(regex.first, regex.second)
/**
 * Splits the string using the specified regular expression pattern, trims each resulting substring,
 * and returns the list of trimmed substrings.
 *
 * @receiver The string to be split and processed.
 * @param regex The `Pattern` used for splitting the string.
 * @return A list of trimmed substrings resulting from the split.
 * @since 1.0.0
 */
operator fun String.div(regex: Pattern) = splitAndTrim(regex)
/**
 * Splits the string using the provided `Pattern` from the pair and trims the resulting substrings.
 *
 * This operator function allows using the `/` operator with a string and a pair, where the pair contains
 * a `Pattern` used for splitting and an integer denoting the maximum number of resulting substrings.
 *
 * @param regex a pair containing the `Pattern` to be used for splitting the string and the limit on the
 * number of substrings. The first element of the pair is the `Pattern`, and the second is the limit.
 * @receiver The string to be split and trimmed.
 * @return a list of trimmed substrings resulting from the split.
 * @since 1.0.0
 */
@JvmName("StringDivPairPattern")
operator fun String.div(regex: Pair<Pattern, Int>) = splitAndTrim(regex.first, regex.second)
/**
 * Divides the string into substrings using the specified delimiter and trims all resulting substrings.
 *
 * @param delimiter The character used as a delimiter for splitting the string.
 * @return A list of trimmed substrings resulting from the split operation.
 * @receiver The original string to be divided.
 * @since 1.0.0
 */
operator fun String.div(delimiter: Char) = splitAndTrim(delimiter)
/**
 * Splits the string into substrings using the specified delimiter and trims all resulting substrings.
 * The operation is restricted to a maximum number of substrings, as specified in the delimiter pair.
 *
 * @param delimiter A pair where the first component is the delimiter character used to split the string,
 * and the second component is the maximum number of substrings to return.
 * @return A list of trimmed substrings resulting from the split operation.
 * @receiver The string that will be split and trimmed.
 * @since 1.0.0
 */
@JvmName("StringDivPairChar")
operator fun String.div(delimiter: Pair<Char, Int>) = splitAndTrim(delimiter.first, limit = delimiter.second)
/**
 * Splits the string into substrings using the specified delimiter and trims all resulting substrings.
 *
 * @param delimiter A [Triple] consisting of:
 *   - The character used as the delimiter for splitting the string.
 *   - A Boolean indicating whether the case of the character should be ignored.
 *   - An integer specifying the maximum number of substrings to return. A non-positive value means no limit.
 * @return A list of trimmed substrings resulting from the split operation.
 * @receiver The string to be split and trimmed.
 * @since 1.0.0
 */
@JvmName("StringDivTripleChar")
operator fun String.div(delimiter: Triple<Char, Boolean, Int>) = splitAndTrim(delimiter.first, ignoreCase = delimiter.second, limit = delimiter.third)
/**
 * Splits the string into a list of substrings around occurrences of the specified delimiter
 * and trims leading and trailing whitespace from each resulting substring.
 *
 * @param delimiter The delimiter used to split the string.
 * @return A list of trimmed substrings resulting from the split operation.
 * @receiver The string.
 * @since 1.0.0
 */
operator fun String.div(delimiter: String) = splitAndTrim(delimiter)
/**
 * Splits the string into substrings using the specified delimiter and trims each resulting substring.
 * The `Pair` provides the delimiter string and the maximum number of substrings to return.
 *
 * @param delimiter A pair where the first value is the string delimiter used for splitting,
 * and the second value is the limit on the number of resulting substrings.
 * A limit of zero means no limit on the number of substrings.
 * @return A list of trimmed substrings resulting from the split operation.
 * @receiver The string to be split.
 * @since 1.0.0
 */
@JvmName("StringDivPairString")
operator fun String.div(delimiter: Pair<String, Int>) = splitAndTrim(delimiter.first, limit = delimiter.second)
/**
 * Splits the string into a list of trimmed substrings using the specified delimiter information encapsulated
 * in a Triple. The process removes leading and trailing whitespace from each resulting substring.
 *
 * @param delimiter A Triple containing:
 *  - The delimiter string to use for splitting.
 *  - A Boolean indicating whether the delimiter should be matched ignoring character case.
 *  - An Int specifying the maximum number of substrings to return (0 means no limit).
 * @return A list of trimmed substrings resulting from the split operation.
 * @receiver The string to be split and trimmed.
 * @since 1.0.0
 */
operator fun String.div(delimiter: Triple<String, Boolean, Int>) = splitAndTrim(delimiter.first, ignoreCase = delimiter.second, limit = delimiter.third)

/**
 * Splits the string into chunks of the specified size and returns the result as a StringList.
 *
 * @param n The size of each chunk. Must be a positive integer.
 * @return StringList containing chunks of the original string.
 * @since 1.0.0
 */
operator fun String.rem(n: Int): StringList = chunked(n)

/**
 * Checks whether the CharSequence is null or blank.
 * The blank check involves verifying if the string is either empty
 * or contains only whitespace characters.
 *
 * @return `false` if the CharSequence is not null and not blank.
 *         Otherwise, returns `true`.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
@Suppress("kutils_null_check")
operator fun CharSequence?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNullOrBlank()
}

/**
 * An operator function that transforms the string to its lowercase equivalent.
 * This method is invoked using the unary minus operator on a String instance.
 *
 * @receiver The original string to be converted to lowercase.
 * @return A new string that is the lowercase version of the receiver string.
 * @since 1.0.0
 */
operator fun String.unaryMinus() = lowercase()

/**
 * Returns the uppercase representation of the string instance
 * on which the unary plus operator is applied.
 *
 * This operator function provides a shorthand way to convert
 * a string to its uppercase form.
 *
 * @receiver The string instance that will be converted to uppercase.
 * @return A new string containing the uppercase letters of the receiver.
 *
 * @since 1.0.0
 */
operator fun String.unaryPlus() = uppercase()

/**
 * Decrements a string by removing its last character. If the string is empty,
 * it will return the string as is.
 *
 * @return A new string with the last character removed, or the original string
 * if it is empty.
 * @since 1.0.0
 */
operator fun String.dec() = if (isNotEmpty()) this.dropLast(1) else this

/**
 * Returns a new string composed of characters from the specified range of indices in the original string.
 *
 * @param range The range of indices used to extract characters. Must be within the bounds of the string.
 * @since 1.0.0
 */
operator fun String.get(range: IntProgression) = buildString {
    for (i in range) append(this@get[i])
}

/**
 * Extracts a substring from the current string based on the specified range of strings.
 *
 * This function retrieves the portion of the string that occurs after the starting string
 * provided in the range and before the ending string inclusive.
 *
 * @param substring A closed range specifying the start and end strings to delimit the substring.
 * @param includeDelimiters Whether to include the start and end strings in the extracted substring.
 * @param last Whether to extract the substring from the last occurrence of the start string.
 * @return The substring contained within the specified range.
 * @since 1.0.0
 */
operator fun String.get(substring: ClosedRange<String>, includeDelimiters: Boolean = true, last: Boolean = false) =
    if (includeDelimiters) {
        if (last)
            afterLastIncluding(substring.start).beforeLastIncluding(substring.endInclusive)
        else afterIncluding(substring.start).beforeIncluding(substring.endInclusive)
    } else {
        if (last)
            afterLast(substring.start).beforeLast(substring.endInclusive)
        else after(substring.start).before(substring.endInclusive)
    }
/**
 * Extracts a substring from the current string that lies between the specified start and end characters, inclusive.
 *
 * @param substring A range of characters that defines the boundaries of the substring to extract.
 * @param includeDelimiters Whether to include the start and end strings in the extracted substring.
 * @param last Whether to extract the substring from the last occurrence of the start string.
 * @return A substring that begins after the start character and ends before the end character of the specified range.
 * @since 1.0.0
 */
@JvmName("getCharClosedRange")
operator fun String.get(substring: ClosedRange<Char>, includeDelimiters: Boolean = true, last: Boolean = false) =
    if (includeDelimiters) {
        if (last)
            afterLastIncluding(substring.start).beforeLastIncluding(substring.endInclusive)
        else afterIncluding(substring.start).beforeIncluding(substring.endInclusive)
    } else {
        if (last)
            afterLast(substring.start).beforeLast(substring.endInclusive)
        else after(substring.start).before(substring.endInclusive)
    }

/**
 * Replaces placeholders in the string with corresponding values provided in the arguments.
 * The placeholders must follow the pattern `"{key}"` where `key` is the first element of a pair in the arguments.
 *
 * @param args A vararg of pairs where each pair consists of a placeholder key as the first element
 * and its corresponding replacement value as the second element.
 * @return A new string with the placeholders replaced by their respective values.
 * @since 1.0.0
 */
operator fun String.invoke(vararg args: Pair<Any, Any?>): String {
    var result = this
    args.forEach { result = result.replace("{${it.first}}", it.second.toSafeString()) }
    return result
}
/**
 * Allows a string to be used as a function to replace placeholders (by default matching the pattern "{...}")
 * with the corresponding arguments provided.
 *
 * @param args The arguments to replace the placeholders in the string. Each argument corresponds sequentially to a detected placeholder.
 * @since 1.0.0
 */
operator fun String.invoke(vararg args: String?) = invoke(args = args.toList())
/**
 * Replaces substrings in the string, identified by specified delimiters, with corresponding values from the provided argument list.
 *
 * @param fromTo A pair of regular expressions that define the start and end delimiters for substrings to be replaced. Defaults to `{` as the start delimiter and `}` as the end delimiter
 * .
 * @param args A list of arguments used to replace the identified substrings. Each argument corresponds to a match found between the specified delimiters.
 * @return A new string with the substrings replaced by the provided arguments, maintaining their order. If no matches are found, the original string is returned.
 * @since 1.0.0
 */
operator fun String.invoke(
    fromTo: MonoPair<Regex> = Regex("\\{") to Regex("}"),
    args: Iterable<Any?>
): String {
    val (startRegex, endRegex) = fromTo

    // Trova tutte le occorrenze che corrispondono a {qualcosa}
    val pattern = Regex("${startRegex.pattern}(.*?)${endRegex.pattern}")
    val matches = pattern.findAll(this)

    // Estrai le chiavi
    val keys = matches.map { it.groupValues[1] }.toList()

    // Verifica se tutte le chiavi sono numeri
    val allNumeric = keys.all { it.matches(Regex("\\d+")) }
    val orderedKeys = if (allNumeric) keys.distinct().sortedBy { it.toInt() } else keys.distinct().sorted()

    // Mappa chiave -> arg corrispondente
    val substitutionMap = orderedKeys.zip(args).toMap()

    // Sostituisci tutte le chiavi con gli argomenti
    return pattern.replace(this) { matchResult ->
        val key = matchResult.groupValues[1]
        substitutionMap[key]?.toString() ?: matchResult.value // fallback se mancante
    }
}
/**
 * Replaces substrings in the string that are identified by the specified delimiters with corresponding values
 * from the provided argument list.
 *
 * @param fromTo A pair of strings representing the start and end delimiters for substrings to be replaced.
 * These delimiters will be converted to regular expressions for matching.
 * @param args A list of nullable values used to replace the identified substrings. The values are applied
 * in the order they appear in the list.
 * @since 1.0.0
 */
@JvmName("stringInvokeFromToStringPair")
operator fun String.invoke(fromTo: String2, args: List<Any?>) = invoke(Regex(Regex.escape(fromTo.first)) to Regex(Regex.escape(fromTo.second)), args)
/**
 * Invokes the string as a templated formatter, replacing substrings within specified delimiters
 * with corresponding values from the provided arguments list.
 *
 * @param fromTo A pair of characters representing the start and end delimiters to identify the substrings
 * that need to be replaced with values from the `args` list.
 * @param args A list of arguments to replace the delimited substrings. Each argument corresponds to
 * a match found between the specified delimiters. If arguments are missing for matched delimiters, they will be replaced with an empty string.
 * @since 1.0.0
 */
@JvmName("stringInvokeFromToCharPair")
operator fun String.invoke(fromTo: MonoPair<Char>, args: List<Any?>) = invoke(Regex(Regex.escape(fromTo.first.toString())) to Regex(Regex.escape(fromTo.second.toString())), args)

/**
 * Returns a new string containing every `step`-th character from the original string, starting with the first character.
 *
 * @param step The step interval for selecting characters from the string. Must be greater than 0.
 * @since 1.0.0
 */
infix fun String.step(step: Int) =  filterIndexed { index, c -> index % step == 0 }

/**
 * Converts the current string into ASCII art using the Figlet font style.
 *
 * This function utilizes the FigletFont library to transform the string
 * into an artistic representation using ASCII characters. It ensures that
 * the input string is processed and displayed as stylized text.
 *
 * @receiver String The string to be converted into ASCII art.
 * @return String The ASCII art representation of the input string.
 * @throws NullPointerException If the conversion fails and results in a null value.
 * @since 1.0.0
 */
fun String.toAsciiArt() = let(FigletFont::convertOneLine)!!

/**
 * Converts the given string to ASCII art representation using the specified font.
 *
 * @param font The font file to be used for generating the ASCII art.
 * @return The ASCII art representation of the string.
 * @throws IllegalStateException If the font file is invalid or conversion fails.
 * @since 1.0.0
 */
fun String.toAsciiArt(font: File) = FigletFont.convertOneLine(font, this)!!

/**
 * Converts the given string into an ASCII art representation using the specified font.
 *
 * @param font The font to use for generating the ASCII art.
 * @return The ASCII art string representation of this string.
 * @since 1.0.0
 */
fun String.toAsciiArt(font: AsciiArtFont): String {
    var s = FigletFont.convertOneLine(font.inputStream(), this)!!
    if (font == AsciiArtFont.SLANT_RELIEF)
        s = s.replace(Regex("_ {2,}_"), "")
    return s
}

/**
 * Enum class representing different ASCII art fonts.
 * Each font is associated with an InputStream provider to load its respective Figlet font file.
 *
 * @property inputStream A lambda providing an InputStream to access the font file.
 * @since 1.0.0
 */
enum class AsciiArtFont(val inputStream: Supplier<InputStream>) {
    /**
     * Represents the LARRY3D font for ASCII art generation.
     * Utilizes a specific resource file (`larry3d.flf`) to define the font style.
     *
     * Throws:
     * - FileNotFoundException if the font resource file `larry3d.flf` is not found in the classpath.
     *
     * @since 1.0.0
     */
    LARRY3D({
        FigletFont::class.java.getResourceAsStream("/larry3d.flf")
            ?: throw FileNotFoundException("Resource larry3d.flf not found")
    }),
    /**
     * Represents the "Slant Relief" font styling for ASCII art generation.
     *
     * This enum constant provides an input stream for the "Slant_Relief.flf" resource file,
     * which contains the definitions and layout specifications of the font.
     *
     * The associated resource file must be available in the classpath. If it is not found,
     * a FileNotFoundException is thrown during instantiation.
     *
     * @since 1.0.0
     */
    SLANT_RELIEF({
        FigletFont::class.java.getResourceAsStream("/Slant_Relief.flf")
            ?: throw FileNotFoundException("Resource Slant_Relief.flf not found")
    })
}

/**
 * Appends the specified string to this StringBuilder.
 *
 * @param s the string to append. If the string is null, "null" will be appended.
 * @return this StringBuilder instance after appending the string.
 * @since 1.0.0
 */
operator fun StringBuilder.plus(s: String?): StringBuilder = append(s)
/**
 * Appends the string representation of the specified [any] object to this [StringBuilder].
 *
 * This operator function provides a convenient way to concatenate any object to a [StringBuilder].
 *
 * @param any The object whose string representation is to be appended. If null, "null" will be appended.
 * @return The same [StringBuilder] instance with the appended content.
 * @since 1.0.0
 */
operator fun StringBuilder.plus(any: Any?): StringBuilder = append(any)
/**
 * Concatenates the content of the specified [StringBuffer] to this [StringBuilder].
 *
 * @param sb The [StringBuffer] whose content is to be appended. If null, no operation is performed.
 * @return This [StringBuilder] instance after appending the content of [sb].
 * @since 1.0.0
 */
operator fun StringBuilder.plus(sb: StringBuffer?): StringBuilder = append(sb)
/**
 * Appends the specified character sequence to this StringBuilder.
 *
 * @param s the character sequence to append. If null, "null" will be appended.
 * @return this StringBuilder instance with the appended content.
 * @since 1.0.0
 */
operator fun StringBuilder.plus(s: CharSequence?): StringBuilder = append(s)
/**
 * Appends a subsequence of the provided character sequence to this StringBuilder.
 *
 * @param s A Triple where:
 *          - The first element is the character sequence to append.
 *          - The second element is the start index of the subsequence to append (inclusive).
 *          - The third element is the end index of the subsequence to append (exclusive).
 * @return This StringBuilder with the specified subsequence appended.
 * @since 1.0.0
 */
@JvmName("StringBuilderPlusTripleCharSequenceIntInt")
@Suppress("kutils_tuple_declaration")
operator fun StringBuilder.plus(s: Triple<CharSequence?, Int, Int>): StringBuilder = append(s.first, s.second, s.third)
/**
 * Appends the specified character array to this StringBuilder.
 *
 * If the provided character array is null, the method appends "null" to the StringBuilder.
 *
 * @param str the character array to be appended to this StringBuilder
 * @return the StringBuilder instance after appending the character array
 * @since 1.0.0
 */
operator fun StringBuilder.plus(str: CharArray?): StringBuilder = append(str.contentToString())
/**
 * Appends a subsequence of the specified character array to the current StringBuilder.
 *
 * The subsequence to be appended is defined by the `str` parameter, which contains
 * a Triple consisting of the character array, the starting offset within the array,
 * and the number of characters to append.
 *
 * If the character array in the Triple is null, no operation is performed.
 *
 * @param str A Triple where the first element is the character array to append from,
 * the second element is the starting offset within the array,
 * and the third element is the number of characters to append.
 * @return This StringBuilder with the specified subsequence appended.
 * @since 1.0.0
 */
@JvmName("StringBuilderPlusTripleCharArrayIntInt")
@Suppress("kutils_tuple_declaration")
operator fun StringBuilder.plus(str: Triple<CharArray?, Int, Int>): StringBuilder = appendRange(
    str.first!!, str.second, str.second + str.third
)
/**
 * Appends the string representation of the specified boolean value to this StringBuilder.
 *
 * @param b the boolean value to append
 * @return this StringBuilder object with the appended boolean value
 * @since 1.0.0
 */
operator fun StringBuilder.plus(b: Boolean): StringBuilder = append(b)
/**
 * Appends the specified character to this StringBuilder.
 *
 * @param c the character to append to this StringBuilder
 * @return the modified StringBuilder with the appended character
 * @since 1.0.0
 */
operator fun StringBuilder.plus(c: Char): StringBuilder = append(c)
/**
 * Appends the string representation of the given integer to this StringBuilder.
 *
 * @param i the integer to append to this StringBuilder
 * @return the same StringBuilder instance with the appended integer
 * @since 1.0.0
 */
operator fun StringBuilder.plus(i: Int): StringBuilder = append(i)
/**
 * Appends the string representation of the given [lng] value to this StringBuilder.
 *
 * @param lng the long value to be appended.
 * @return the StringBuilder instance with the appended value.
 * @since 1.0.0
 */
operator fun StringBuilder.plus(lng: Long): StringBuilder = append(lng)
/**
 * Appends the string representation of the given float value to the StringBuilder.
 *
 * @param f The float value to append to the StringBuilder.
 * @return The updated StringBuilder instance with the appended float value.
 * @since 1.0.0
 */
operator fun StringBuilder.plus(f: Float): StringBuilder = append(f)
/**
 * Appends the string representation of the given Double to this StringBuilder.
 *
 * @param d the Double value to append.
 * @return the StringBuilder instance with the appended Double value.
 * @since 1.0.0
 */
operator fun StringBuilder.plus(d: Double): StringBuilder = append(d)
/**
 * Removes a sequence of characters from this `StringBuilder` starting from the `first` index (inclusive)
 * up to the `second` index (exclusive), as specified by the `startAndEnd` parameter.
 *
 * @param startAndEnd A pair of integers, where `first` represents the starting index (inclusive)
 * and `second` represents the ending index (exclusive) of the characters to be removed.
 * @return The modified instance of `StringBuilder` after removing the specified range of characters.
 * @since 1.0.0
 */
operator fun StringBuilder.minus(startAndEnd: Int2): StringBuilder = delete(startAndEnd.first, startAndEnd.second)
/**
 * Removes the character at the specified index from the current StringBuilder instance.
 *
 * @param charAtIndex the index of the character to be removed.
 * @return the same StringBuilder instance after the character at the specified index has been removed.
 * @since 1.0.0
 */
operator fun StringBuilder.minus(charAtIndex: Int): StringBuilder = deleteCharAt(charAtIndex)

/**
 * Compares two nullable strings for equality, ignoring case considerations.
 *
 * This function determines if the current string is equal to the specified [other] string,
 * without considering case differences. Null values are also handled safely.
 *
 * @param other the string to compare with the current string, may be null
 * @return `true` if the strings are equal ignoring case, or both are null; `false` otherwise
 * @since 1.0.0
 */
infix fun String?.equalsIgnoreCase(other: String?) = equals(other, true)
/**
 * Compares two strings, ignoring case considerations, and determines if they are not equal.
 * This function is provided as an infix operator for cleaner readability.
 *
 * @param other The string to compare with the receiver string, ignoring case.
 * @return `true` if the strings are not equal, ignoring case considerations; `false` otherwise.
 * @since 1.0.0
 */
infix fun String?.notEqualsIgnoreCase(other: String?) = !equalsIgnoreCase(other)

/**
 * Checks if the current character sequence contains the specified character sequence,
 * ignoring case differences.
 *
 * @param other the character sequence to find within the current character sequence
 * @since 1.0.0
 */
infix fun CharSequence.containsIgnoreCase(other: CharSequence) = contains(other, true)
/**
 * Checks if the current CharSequence does not contain the specified character sequence,
 * ignoring case considerations. This function is case-insensitive.
 *
 * @param other The character sequence to check for in the current CharSequence.
 * @return `true` if the current CharSequence does not contain [other], ignoring case;
 *         `false` otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.notContainsIgnoreCase(other: CharSequence) = !containsIgnoreCase(other)
/**
 * Checks if the given character is present in the CharSequence, ignoring case considerations.
 *
 * This function performs a case-insensitive search for the specified character
 * within the CharSequence.
 *
 * @param char the character to be searched for in the CharSequence.
 * @since 1.0.0
 */
infix fun CharSequence.containsIgnoreCase(char: Char) = contains(char, true)
/**
 * Checks if the given character is not present in the CharSequence, ignoring case considerations.
 *
 * This function performs a case-insensitive search for the specified character
 * within the CharSequence and determines if the character is not present.
 *
 * @param char the character to be checked for absence in the CharSequence.
 * @since 1.0.0
 */
infix fun CharSequence.notContainsIgnoreCase(char: Char) = !containsIgnoreCase(char)

/**
 * Checks whether this character sequence exists as a substring in the specified character sequence,
 * ignoring case considerations.
 *
 * @param cs the character sequence to check for the presence of this character sequence.
 * @return `true` if this character sequence is found within the specified character sequence,
 * ignoring case; `false` otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.inIgnoreCase(cs: CharSequence) = cs.contains(this, true)
/**
 * Checks whether this character sequence does not exist as a substring in the specified character sequence,
 * ignoring case considerations.
 *
 * @param cs the character sequence to check against this character sequence.
 * @since 1.0.0
 */
infix fun CharSequence.notInIgnoreCase(cs: CharSequence) = !inIgnoreCase(cs)
/**
 * Checks if the character is contained in the specified character sequence, ignoring case.
 *
 * @param cs the character sequence to check for the presence of this character
 * @since 1.0.0
 */
infix fun Char.inIgnoreCase(cs: CharSequence) = cs.contains(this, true)
/**
 * Checks if the character is not contained in the specified character sequence, ignoring case.
 *
 * @param cs the character sequence to check for the absence of this character
 * @since 1.0.0
 */
infix fun Char.notInIgnoreCase(cs: CharSequence) = !inIgnoreCase(cs)

/**
 * Checks if this character sequence starts with the specified character.
 *
 * @param char the character to check for at the start of the character sequence.
 * @return `true` if the character sequence starts with the specified character, `false` otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.startsWith(char: Char): Boolean = startsWith(char, false)
/**
 * Checks if this character sequence starts with the specified prefix.
 *
 * @param prefix The sequence to check as a prefix of this character sequence.
 * @return `true` if this character sequence starts with the specified prefix, `false` otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.startsWith(prefix: CharSequence): Boolean = startsWith(prefix, false)
/**
 * Checks if the given character sequence does not start with the specified character.
 *
 * This operation is case-sensitive.
 *
 * @param char The character to check against the start of the character sequence.
 * @return `true` if the character sequence does not start with the specified character, `false` otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.notStartsWith(char: Char): Boolean = !startsWith(char, false)
/**
 * Checks if the character sequence does not start with the specified prefix.
 *
 * The comparison is case-sensitive.
 *
 * @param prefix the prefix to check this character sequence against
 * @return `true` if this character sequence does not start with the specified prefix, `false` otherwise
 * @since 1.0.0
 */
infix fun CharSequence.notStartsWith(prefix: CharSequence): Boolean = !startsWith(prefix, false)
/**
 * Checks if the character sequence starts with the specified character,
 * ignoring character case.
 *
 * @param char The character to compare with the start of this character sequence.
 * @return `true` if this character sequence starts with the specified character,
 * ignoring case differences; `false` otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.startsWithIgnoreCase(char: Char): Boolean = startsWith(char, true)
/**
 * Checks if this character sequence starts with the specified prefix, ignoring case.
 *
 * @param prefix the character sequence to check as the potential prefix.
 * @return `true` if this character sequence starts with the specified prefix, ignoring case, `false` otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.startsWithIgnoreCase(prefix: CharSequence): Boolean = startsWith(prefix, true)
/**
 * Checks if the character sequence does not start with the specified character, ignoring case considerations.
 *
 * @param char The character to check against the beginning of the character sequence.
 * @return `true` if the character sequence does not start with the specified character (ignoring case), `false` otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.notStartsWithIgnoreCase(char: Char): Boolean = !startsWith(char, true)
/**
 * Checks if this character sequence does not start with the specified prefix, ignoring case considerations.
 *
 * @param prefix the prefix to check for at the start of this character sequence.
 * @return `true` if this character sequence does not start with the specified prefix (case-insensitive), `false` otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.notStartsWithIgnoreCase(prefix: CharSequence): Boolean = !startsWith(prefix, true)

/**
 * Checks if this character sequence ends with the specified character.
 *
 * @param char the character to be checked as the suffix of this character sequence
 * @return `true` if the character sequence ends with the specified character, `false` otherwise
 * @since 1.0.0
 */
infix fun CharSequence.endsWith(char: Char): Boolean = endsWith(char, false)
/**
 * Checks if this character sequence ends with the specified [prefix].
 * The comparison is case-sensitive.
 *
 * @param prefix the character sequence to check as the suffix.
 * @return `true` if this character sequence ends with the specified [prefix], `false` otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.endsWith(prefix: CharSequence): Boolean = endsWith(prefix, false)
/**
 * Checks if the character sequence does not end with the specified character.
 *
 * @param char the character to check against the end of the character sequence.
 * @return true if the character sequence does not end with the specified character, false otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.notEndsWith(char: Char): Boolean = !endsWith(char, false)
/**
 * Checks if this character sequence does not end with the specified prefix.
 *
 * This function operates case-sensitively and determines whether the calling character sequence
 * lacks the specified suffix. If the suffix is present at the end of the character sequence,
 * the function will return false; otherwise, it will return true.
 *
 * @param prefix The character sequence to check as the potential suffix.
 * @return `true` if this character sequence does not end with the specified prefix, otherwise `false`.
 * @since 1.0.0
 */
infix fun CharSequence.notEndsWith(prefix: CharSequence): Boolean = !endsWith(prefix, false)
/**
 * Checks if the character sequence ends with the specified character, ignoring case considerations.
 *
 * @param char the character to check for at the end of the character sequence
 * @return `true` if the character sequence ends with the specified character ignoring case, `false` otherwise
 * @since 1.0.0
 */
infix fun CharSequence.endsWithIgnoreCase(char: Char): Boolean = endsWith(char, true)
/**
 * Checks if this character sequence ends with the specified prefix,
 * optionally ignoring character case.
 *
 * @param prefix the character sequence to check as a suffix
 * @return `true` if this character sequence ends with the specified prefix, ignoring case if specified; `false` otherwise
 * @since 1.0.0
 */
infix fun CharSequence.endsWithIgnoreCase(prefix: CharSequence): Boolean = endsWith(prefix, true)
/**
 * Checks if the character sequence does not end with the specified character, ignoring case sensitivity.
 *
 * @param char The character to check against the end of the character sequence.
 * @return `true` if the character sequence does not end with the specified character (case insensitive), otherwise `false`.
 * @since 1.0.0
 */
infix fun CharSequence.notEndsWithIgnoreCase(char: Char): Boolean = !endsWith(char, true)
/**
 * Checks if this character sequence does not end with the specified prefix, ignoring character case.
 *
 * This function performs a case-insensitive comparison to determine if the character sequence does not
 * terminate with the provided prefix.
 *
 * @param prefix the character sequence to check against the end of this character sequence.
 * @return `true` if this character sequence does not end with the specified prefix, ignoring case; `false` otherwise.
 * @since 1.0.0
 */
infix fun CharSequence.notEndsWithIgnoreCase(prefix: CharSequence): Boolean = !endsWith(prefix, true)

/**
 * Finds the first occurrence of the specified character in the character sequence,
 * ignoring case considerations.
 *
 * @param char The character to search for within the character sequence.
 * @return The index of the first matching character, or -1 if the character is not found.
 * @since 1.0.0
 */
infix fun CharSequence.indexOfIgnoreCase(char: Char): Int = indexOf(char, ignoreCase = true)
/**
 * Finds the first occurrence of the specified substring within the current character sequence,
 * ignoring case differences.
 *
 * @param other The substring to search for within the character sequence.
 * @return The index of the first occurrence of the specified substring,
 * or -1 if the substring is not found. Comparison is case-insensitive.
 * @since 1.0.0
 */
infix fun CharSequence.indexOfIgnoreCase(other: String): Int = indexOf(other, ignoreCase = true)

/**
 * Returns the substring of the original string that precedes the first occurrence of the specified delimiter.
 * If the delimiter is not found in the string, an empty string is returned.
 *
 * @param delimiter the string to find within the original string
 * @since 1.0.0
 */
infix fun String.before(delimiter: String) = substringBefore(delimiter, String.EMPTY)
/**
 * Returns the part of the string that is before the first occurrence of the specified delimiter.
 * If the delimiter is not found, the original string is returned.
 *
 * @param delimiter the character to search for in the string
 * @return the substring before the first occurrence of the delimiter, or an empty string
 * @since 1.0.0
 */
infix fun String.before(delimiter: Char) = substringBefore(delimiter, String.EMPTY)
/**
 * Returns the substring of the receiver string that precedes the first occurrence of the specified regular expression.
 *
 * @param delimiter The regular expression used to locate the position in the string before which the substring is extracted.
 * @since 1.0.0
 */
infix fun String.before(delimiter: Regex) = if (delimiter.find(this).isNull()) String.EMPTY else get(0..<(delimiter.find(this)!!.range.first))
/**
 * Returns a substring of the original string before the first occurrence of the specified delimiter,
 * including the delimiter itself. If the delimiter is not present in the string, an empty string
 * is returned.
 *
 * @param delimiter The substring to search for within the original string.
 * @since 1.0.0
 */
infix fun String.beforeIncluding(delimiter: String): String {
    val result = substringBefore(delimiter, String.EMPTY)
    return if (result == String.EMPTY) result
    else result + delimiter
}
/**
 * Returns a substring of the original string up to and including the first occurrence of the specified delimiter.
 * If the delimiter is not present, an empty string is returned.
 *
 * @param delimiter the character used to determine the endpoint of the substring.
 * @return a substring from the beginning of the input string up to and including the first occurrence of the delimiter.
 * @since 1.0.0
 */
infix fun String.beforeIncluding(delimiter: Char): String {
    val result = substringBefore(delimiter, String.EMPTY)
    return if (result == String.EMPTY) result
    else result + delimiter
}
/**
 * Returns a substring of the original string starting from the beginning and ending
 * at the last character matched by the specified regular expression, inclusive.
 * If the regular expression is not matched, returns an empty string.
 *
 * @param delimiter A regular expression used to determine the endpoint of the substring.
 * @since 1.0.0
 */
infix fun String.beforeIncluding(delimiter: Regex) = get(0..(delimiter.find(this)?.range?.last ?: 0))
/**
 * Returns the substring of this string before the last occurrence of the specified [delimiter].
 * If the [delimiter] is not found in the string, returns an empty string.
 *
 * @param delimiter The substring to search for.
 * @return The substring before the last occurrence of [delimiter], or an empty string if [delimiter] is not found.
 * @since 1.0.0
 */
infix fun String.beforeLast(delimiter: String) = substringBeforeLast(delimiter, String.EMPTY)
/**
 * Returns the part of the string before the last occurrence of the given delimiter.
 *
 * @param delimiter the character to find the last occurrence of in the string.
 * @since 1.0.0
 */
infix fun String.beforeLast(delimiter: Char) = substringBeforeLast(delimiter, String.EMPTY)
/**
 * Returns the substring of the string instance that occurs before the last match of the specified regular expression.
 * If the regular expression is not found in the string, it returns the entire string.
 *
 * @param delimiter The regular expression used to identify the last occurrence. Determines the split point.
 * @since 1.0.0
 */
infix fun String.beforeLast(delimiter: Regex) = if (delimiter.find(this).isNull()) String.EMPTY else get(0..<(delimiter.findAll(this).last().range.first))
/**
 * Returns a substring of the original string that includes the part before the last occurrence
 * of the specified [delimiter], including the [delimiter] itself.
 * If the delimiter is not present in the string, returns an empty string.
 *
 * @param delimiter the string to search for. The last occurrence of this delimiter in the string
 * determines the point up to which the substring is returned, including the [delimiter] itself.
 * @since 1.0.0
 */
infix fun String.beforeLastIncluding(delimiter: String): String {
    val result = substringBeforeLast(delimiter, String.EMPTY)
    return if (result == String.EMPTY) result
    else result + delimiter
}
/**
 * Returns the substring of the original string up to and including the specified delimiter,
 * starting from the end of the string.
 * If the delimiter is not present, an empty string is returned.
 *
 * @param delimiter The character that marks the boundary of the substring to extract.
 * The resulting substring will include this delimiter.
 * @return A substring of the original string ending with the specified delimiter.
 * @since 1.0.0
 */
infix fun String.beforeLastIncluding(delimiter: Char): String {
    val result = substringBeforeLast(delimiter, String.EMPTY)
    return if (result == String.EMPTY) result
    else result + delimiter
}
/**
 * Returns a substring of the original string that includes all characters
 * up to and including the last occurrence of the specified delimiter pattern.
 * If the delimiter is not found in the string, the result will include an empty string.
 *
 * @param delimiter A regular expression pattern used to find the last occurrence in the string.
 * @since 1.0.0
 */
infix fun String.beforeLastIncluding(delimiter: Regex) = if (delimiter.find(this).isNull()) String.EMPTY else get(0..(delimiter.findAll(this).last().range.last))
/**
 * Returns a substring of this string starting from the first occurrence of the specified delimiter.
 * If the delimiter is not found, this string is returned as empty.
 *
 * @param delimiter The substring to search for.
 * @since 1.0.0
 */
infix fun String.after(delimiter: String) = substringAfter(delimiter, String.EMPTY)
/**
 * Returns a substring of the given string that occurs after the first occurrence of the specified delimiter.
 * If the delimiter is not found in the string, an empty string is returned.
 *
 * @param delimiter the character used to find the partition point of the string.
 * @return the substring after the first occurrence of the delimiter, or the original string if the delimiter is not found.
 * @since 1.0.0
 */
infix fun String.after(delimiter: Char) = substringAfter(delimiter, String.EMPTY)
/**
 * Returns the substring of this string that occurs after the first match of the given delimiter.
 * If the delimiter is not found, it returns the an empty string.
 *
 * @param delimiter A regular expression to identify the part of the string after which the substring should be taken.
 * @since 1.0.0
 */
infix fun String.after(delimiter: Regex) = if (delimiter.find(this).isNull()) String.EMPTY else get((delimiter.find(this)!!.range.last) + 1..<length)
/**
 * Returns a string starting from and including the first occurrence of the specified [delimiter].
 * If the [delimiter] is not found in the string, an empty string is returned.
 *
 * @param delimiter The string to search for within the receiver string.
 * @since 1.0.0
 */
infix fun String.afterIncluding(delimiter: String): String {
    val result = substringAfter(delimiter, String.EMPTY)
    return if (result == String.EMPTY) result
    else delimiter + result
}
/**
 * Returns a new string starting from the first occurrence of the specified delimiter,
 * including the delimiter itself. If the delimiter is not found in the string,
 * returns an empty string.
 *
 * @param delimiter the character used to determine the starting point of the returned substring.
 * @since 1.0.0
 */
infix fun String.afterIncluding(delimiter: Char): String {
    val result = substringAfter(delimiter, String.EMPTY)
    return if (result == this) this
    else delimiter + result
}
/**
 * Returns a substring of the caller string starting from the character after the first match of the given delimiter,
 * including any remainder of the string. If the delimiter is not found, an empty string is returned.
 *
 * @param delimiter A regular expression used to find a match within the string. The substring starts immediately after the first match.
 * @since 1.0.0
 */
infix fun String.afterIncluding(delimiter: Regex) = if (delimiter.find(this).isNull()) String.EMPTY else get(delimiter.find(this)!!.range.first..<length)
/**
 * Returns a substring of this string obtained by taking the part after the last occurrence of the given [delimiter].
 * If the [delimiter] is not present in the string, an empty string is returned.
 *
 * @param delimiter The string to find the last occurrence of.
 * @since 1.0.0
 */
infix fun String.afterLast(delimiter: String) = substringAfterLast(delimiter, String.EMPTY)
/**
 * Returns the part of the string after the last occurrence of the specified delimiter.
 * If the delimiter is not present in the string, an empty string is returned.
 *
 * @param delimiter The character to find the last occurrence of in the string.
 * @return A substring starting after the last occurrence of the delimiter.
 * @since 1.0.0
 */
infix fun String.afterLast(delimiter: Char) = substringAfterLast(delimiter, this)
/**
 * Returns the substring of the string that occurs after the last occurrence of the specified delimiter.
 *
 * @param delimiter A regular expression that determines the delimiter after which the substring is extracted.
 * @since 1.0.0
 */
infix fun String.afterLast(delimiter: Regex) = if (delimiter.find(this).isNull()) String.EMPTY else get((delimiter.findAll(this).lastOrNull()!!.range.last) + 1..<length)
/**
 * Returns a substring of the original string starting from and including the last occurrence
 * of the specified delimiter. If the delimiter is not found in the string, an empty string
 * is returned.
 *
 * @param delimiter the string to search for in the original string. The returned substring will start
 *                  from the last occurrence of this delimiter, including the delimiter itself.
 * @since 1.0.0
 */
infix fun String.afterLastIncluding(delimiter: String): String {
    val result = substringAfterLast(delimiter, String.EMPTY)
    return if (result == String.EMPTY) result
    else delimiter + result
}
/**
 * Returns a string that consists of the specified delimiter followed by the substring of the original string
 * that comes after the last occurrence of the delimiter. If the delimiter is not found in the string,
 * an empty string is returned.
 *
 * @param delimiter The character used to find the last occurrence in the string and construct the resulting substring.
 * @since 1.0.0
 */
infix fun String.afterLastIncluding(delimiter: Char): String {
    val result = substringAfterLast(delimiter, String.EMPTY)
    return if (result == String.EMPTY) result
    else delimiter + result
}
/**
 * Finds the last occurrence of the specified delimiter in the string using the provided regex
 * and returns a substring starting from the start of that delimiter (inclusive) to the end
 * of the original string.
 *
 * If the delimiter is not found, an empty string is returned.
 *
 * @param delimiter The regular expression used to find the last occurrence in the string.
 * @since 1.0.0
 */
infix fun String.afterLastIncluding(delimiter: Regex) = if (delimiter.find(this).isNull()) String.EMPTY else get((delimiter.findAll(this).last().range.first)..<length)

/**
 * Inserts the specified string at the given index in the current string instance.
 *
 * @param index The zero-based index position at which the specified string will be inserted.
 *              Must be within the range [0, length] of the current string.
 * @param value The string to insert into the original string at the specified index.
 * @since 1.0.0
 * @return The new string with the specified string inserted at the provided index.
 */
fun String.insert(index: Int, value: Any?) = substring(0, index) + value + substring(index)
/**
 * Inserts the given [value] immediately before the first occurrence of the specified [element]
 * in the original string. The resulting string includes the [value] preceding the [element]
 * while preserving the rest of the content.
 *
 * @param element the string to locate within the original string before which the [value] will be inserted
 * @param value the string to insert before the specified [element]
 * @since 1.0.0
 */
fun String.insertBefore(element: String, value: String) = before(element) + value + afterIncluding(element)
/**
 * Inserts the given string `value` before the first occurrence of the specified `element`
 * in the original string while retaining the remaining part of the string as is.
 * If the `element` is not found, the `value` is appended to the original string.
 *
 * @param element the character in the string before which the `value` should be inserted
 * @param value the string to insert before the specified `element`
 * @since 1.0.0
 */
fun String.insertBefore(element: Char, value: String) = before(element) + value + afterIncluding(element)
/**
 * Inserts the specified [value] into the receiver string before the first occurrence of the substring
 * that matches the given [element] regular expression.
 *
 * Combines the substring preceding the match of [element], the [value], and the remainder of the string,
 * starting from the match and including it as returned by [afterIncluding].
 *
 * @param element A regular expression used to locate the position in the string where the insertion should occur.
 * @param value The string to be inserted into the receiver string.
 * @since 1.0.0
 */
fun String.insertBefore(element: Regex, value: String) = before(element) + value + afterIncluding(element)
/**
 * Inserts the specified value into the string immediately after the first occurrence of the specified element.
 * If the element is not found in the string, the value is appended to the end of the string.
 *
 * @param element The substring after which the value should be inserted.
 * @param value The string to insert after the specified element.
 * @since 1.0.0
 */
fun String.insertAfter(element: String, value: String) = beforeIncluding(element) + value + after(element)
/**
 * Inserts the specified string value immediately after the first occurrence of the given character
 * in the original string.
 *
 * @param element the character after which the value should be inserted.
 * @param value the string to insert after the specified character in the original string.
 * @since 1.0.0
 */
fun String.insertAfter(element: Char, value: String) = beforeIncluding(element) + value + after(element)
/**
 * Inserts the specified value into this string immediately after the first occurrence
 * of the element matched by the provided regular expression.
 * If the regular expression does not match any part of the string, the original string will remain unchanged.
 *
 * @param element A regular expression used to locate the position in the string where the value should be inserted.
 * @param value The string to insert after the match of the regular expression.
 * @since 1.0.0
 */
fun String.insertAfter(element: Regex, value: String) = beforeIncluding(element) + value + after(element)

/**
 * Invokes the custom behavior of the Int receiver where it manipulates the input string
 * based on the value of the integer. For positive integers, it takes a substring of
 * length equal to the integer value. For negative integers, it drops a prefix of
 * length equal to the absolute value of the integer. If the integer value is zero,
 * it returns an empty string.
 *
 * @param string the input string to be manipulated based on the integer value
 * @return the manipulated string based on the logic described
 * @since 1.0.0
 */
operator fun Int.invoke(string: String): String {
    if (this == 0) return String.EMPTY
    if (isPositive) return string.take(this)
    return string.drop(-this)
}

/**
 * Removes multiple consecutive spaces in a string and replaces them with a single space.
 * This function operates on the receiver string, ensuring that any sequences of whitespace
 * characters are compacted into a single space, making the string more concise and standardized.
 *
 * The function preserves single spaces and trims excess spaces caused by multiple consecutive spaces
 * within a given string, but does not alter the original string's non-space characters.
 *
 * @receiver The string from which multiple spaces will be removed.
 * @return A new string with all consecutive space sequences replaced by a single space.
 * @since 1.0.0
 */
fun String.removeMultipleSpaces() = replace("\\s+".toRegex(), String.SPACE)
/**
 * Trims leading and trailing whitespace from the string and replaces
 * multiple consecutive whitespace characters with a single space.
 *
 * The method removes all redundant spaces within the text
 * while ensuring that spacing remains consistent.
 *
 * @receiver The string on which the operation is performed.
 * @return A new string with leading and trailing whitespace removed
 *         and multiple spaces replaced with a single space.
 * @since 1.0.0
 */
fun String.trimAndremoveMultipleSpaces() = trim().replace("\\s+".toRegex(), String.SPACE)

/**
 * Surrounds the current string with the specified wrapper sequence.
 *
 * @param wrapper The character sequence to surround the current string with.
 * @since 1.0.0
 */
infix fun String.surroundWith(wrapper: CharSequence) = "$wrapper$this$wrapper"
/**
 * Surrounds the current string with the specified character by placing the character at both the beginning and
 * the end of the string.
 *
 * @param char The character to surround the string with.
 * @since 1.0.0
 */
infix fun String.surroundWith(char: Char) = "$char$this$char"

/**
 * Encodes the current string into a Base32 encoded string using UTF-8 character encoding.
 *
 * This method converts the string into bytes using UTF-8 encoding and then
 * encodes those bytes using the Base32 encoding scheme.
 *
 * @return the Base32 encoded representation of the original string
 * @throws EncodeException if the encoding failed
 * @since 1.0.0
 */
fun String.base32Encode(): String = try {
    Base32().encodeAsString(toByteArray(Charsets.UTF_8))
} catch (e: Exception) {
    throw EncodeException("Unable to encode string using Base32", e)
}
/**
 * Decodes a Base32-encoded string and returns the decoded result as a string.
 *
 * This function uses a Base32 decoder to interpret the current string and converts it
 * into its corresponding byte array, which is then translated into a UTF-8 string.
 *
 * @return The decoded string from the Base32-encoded input string.
 * @throws DecodeException if the decoding failed
 * @since 1.0.0
 */
fun String.base32Decode(): String = try {
    String(Base32().decode(this))
} catch (e: Exception) {
    throw DecodeException("Unable to decode string using Base32", e)
}

/**
 * Encodes the given string into a Base64 encoded string using UTF-8 character encoding.
 *
 * @return A Base64 encoded representation of the original string.
 * @throws EncodeException if the encoding failed
 * @since 1.0.0
 */
fun String.base64Encode(): String = try {
    Base64.getEncoder().encodeToString(toByteArray(Charsets.UTF_8))
} catch (e: Exception) {
    throw EncodeException("Unable to encode string using Base64", e)
}
/**
 * Decodes the given Base64 encoded string and returns the resulting decoded string.
 *
 * @return The decoded string derived from the Base64 input.
 * @throws DecodeException if the decoding failed
 * @since 1.0.0
 */
fun String.base64Decode(): String = try {
    String(Base64.getDecoder().decode(this))
} catch (e: Exception) {
    throw DecodeException("Unable to decode string using Base64", e)
}

/**
 * Encodes the given string into a Base62-encoded string representation.
 *
 * Converts the string into a byte array and applies the Base62 encoding algorithm to produce the encoded result.
 *
 * @return A Base62-encoded string representation of the input string.
 * @throws EncodeException if the encoding failed
 * @since 1.0.0
 */
fun String.base62Encode(): String = try {
    Base62.encode(toByteArray())
} catch (e: Exception) {
    throw EncodeException("Unable to encode string using Base62", e)
}
/**
 * Decodes a Base62-encoded string into its original string representation.
 *
 * This method uses the `Base62.decode` function to convert the Base62-encoded string
 * into a byte array and then constructs a string using UTF-8 character encoding.
 *
 * @return The decoded string obtained from the provided Base62-encoded input.
 * @throws DecodeException if the encoding failed
 * @since 1.0.0
 */
fun String.base62Decode(): String = try {
    String(Base62.decode(this), Charsets.UTF_8)
} catch (e: Exception) {
    throw DecodeException("Unable to decode string using Base62", e)
}

/**
 * Computes the hash of the invoking ByteArray using the specified hashing algorithm.
 *
 * @param algorithm The hashing algorithm to be used for generating the hash.
 * @return A ByteArray containing the computed hash based on the specified algorithm.
 * @throws HashingException if an error occurs during the hashing process.
 * @since 1.0.0
 */
infix fun ByteArray.hashing(algorithm: HashingAlgorithm): ByteArray {
    try {
        val digest = when (algorithm) {
            HashingAlgorithm.BLAKE2B_160 -> Blake2b.Blake2b160()
            HashingAlgorithm.BLAKE2B_256 -> Blake2b.Blake2b256()
            HashingAlgorithm.BLAKE2B_384 -> Blake2b.Blake2b384()
            HashingAlgorithm.BLAKE2B_512 -> Blake2b.Blake2b512()
            HashingAlgorithm.BLAKE2S_128 -> Blake2s.Blake2s128()
            HashingAlgorithm.BLAKE2S_160 -> Blake2s.Blake2s160()
            HashingAlgorithm.BLAKE2S_224 -> Blake2s.Blake2s224()
            HashingAlgorithm.BLAKE2S_256 -> Blake2s.Blake2s256()
            HashingAlgorithm.BLAKE3_256 -> Blake3.Blake3_256()
            HashingAlgorithm.DSTU7564_256 -> DSTU7564.Digest256()
            HashingAlgorithm.DSTU7564_384 -> DSTU7564.Digest384()
            HashingAlgorithm.DSTU7564_512 -> DSTU7564.Digest512()
            HashingAlgorithm.GOST3411 -> GOST3411.Digest()
            HashingAlgorithm.GOST3411_2012_256 -> GOST3411.Digest2012_256()
            HashingAlgorithm.GOST3411_2012_512 -> GOST3411.Digest2012_512()
            HashingAlgorithm.HARAKA_256 -> Haraka.Digest256()
            HashingAlgorithm.HARAKA_512 -> Haraka.Digest512()
            HashingAlgorithm.KECCAK_224 -> Keccak.Digest224()
            HashingAlgorithm.KECCAK_256 -> Keccak.Digest256()
            HashingAlgorithm.KECCAK_288 -> Keccak.Digest288()
            HashingAlgorithm.KECCAK_384 -> Keccak.Digest384()
            HashingAlgorithm.KECCAK_512 -> Keccak.Digest512()
            HashingAlgorithm.MD2 -> MessageDigest.getInstance("MD2")
            HashingAlgorithm.MD4 -> MessageDigest.getInstance("MD4", "BC")
            HashingAlgorithm.MD5 -> MessageDigest.getInstance("MD5")
            HashingAlgorithm.PARALLELHASH128_256 -> SHA3.DigestParallelHash128_256()
            HashingAlgorithm.PARALLELHASH256_512 -> SHA3.DigestParallelHash256_512()
            HashingAlgorithm.RIPEMD_128 -> RIPEMD128.Digest()
            HashingAlgorithm.RIPEMD_160 -> RIPEMD160.Digest()
            HashingAlgorithm.RIPEMD_256 -> RIPEMD256.Digest()
            HashingAlgorithm.RIPEMD_320 -> RIPEMD320.Digest()
            HashingAlgorithm.SHA_1 -> MessageDigest.getInstance("SHA-1")
            HashingAlgorithm.SHA_224 -> MessageDigest.getInstance("SHA-224")
            HashingAlgorithm.SHA_256 -> MessageDigest.getInstance("SHA-256")
            HashingAlgorithm.SHA_384 -> MessageDigest.getInstance("SHA-384")
            HashingAlgorithm.SHA_512 -> MessageDigest.getInstance("SHA-512")
            HashingAlgorithm.SHA_512_224 -> MessageDigest.getInstance("SHA-512/224")
            HashingAlgorithm.SHA_512_256 -> MessageDigest.getInstance("SHA-512/256")
            HashingAlgorithm.SHA3_224 -> SHA3.Digest224()
            HashingAlgorithm.SHA3_256 -> SHA3.Digest256()
            HashingAlgorithm.SHA3_384 -> SHA3.Digest384()
            HashingAlgorithm.SHA3_512 -> SHA3.Digest512()
            HashingAlgorithm.SHAKE128_256 -> SHA3.DigestShake128_256()
            HashingAlgorithm.SHAKE256_512 -> SHA3.DigestShake256_512()
            HashingAlgorithm.SKEIN_1024_1024 -> Skein.Digest_1024_1024()
            HashingAlgorithm.SKEIN_1024_384 -> Skein.Digest_1024_384()
            HashingAlgorithm.SKEIN_1024_512 -> Skein.Digest_1024_512()
            HashingAlgorithm.SKEIN_256_128 -> Skein.Digest_256_128()
            HashingAlgorithm.SKEIN_256_160 -> Skein.Digest_256_160()
            HashingAlgorithm.SKEIN_256_224 -> Skein.Digest_256_224()
            HashingAlgorithm.SKEIN_256_256 -> Skein.Digest_256_256()
            HashingAlgorithm.SKEIN_512_128 -> Skein.Digest_512_128()
            HashingAlgorithm.SKEIN_512_160 -> Skein.Digest_512_160()
            HashingAlgorithm.SKEIN_512_256 -> Skein.Digest_512_256()
            HashingAlgorithm.SKEIN_512_384 -> Skein.Digest_512_384()
            HashingAlgorithm.SKEIN_512_512 -> Skein.Digest_512_512()
            HashingAlgorithm.SM3 -> SM3.Digest()
            HashingAlgorithm.TIGER -> Tiger.Digest()
            HashingAlgorithm.TUPLEHASH128_256 -> SHA3.DigestTupleHash128_256()
            HashingAlgorithm.TUPLEHASH256_512 -> SHA3.DigestTupleHash256_512()
            HashingAlgorithm.WHIRLPOOL -> Whirlpool.Digest()
        }

        return digest.digest(this)!!
    } catch (e: Exception) {
        throw HashingException(algorithm, e)
    }
}
/**
 * Computes the hash of the current string using the specified hashing algorithm.
 *
 * @param algorithm The hashing algorithm to use for computing the hash.
 * @throws HashingException if the hashing failed
 * @return A byte array representing the hash of the string computed with the given algorithm.
 * @since 1.0.0
 */
infix fun String.hashing(algorithm: HashingAlgorithm): ByteArray = toByteArray().hashing(algorithm)
/**
 * Computes the hash of the current ByteArray using the provided hashing algorithm.
 *
 * @param algorithm The message digest algorithm to use for hashing.
 * @return The computed hash as a ByteArray.
 * @since 1.0.0
 */
infix fun ByteArray.hashing(algorithm: MessageDigest) = algorithm.digest(this)!!
/**
 * Generates a hashed byte array for the given string using the specified message digest algorithm.
 *
 * This function applies the provided hashing algorithm on the string's byte representation and
 * returns the resulting hash as a byte array.
 *
 * @param algorithm The MessageDigest instance representing the hashing algorithm to use.
 * @return The hashed byte array obtained after applying the specified algorithm.
 * @since 1.0.0
 */
infix fun String.hashing(algorithm: MessageDigest) = algorithm.digest(toByteArray())!!

/**
 * Converts the hash result of the current `ByteArray` into a hexadecimal string representation
 * using the specified hashing algorithm.
 *
 * @param algorithm the cryptographic hashing algorithm to be applied to the `ByteArray`
 * @return the hexadecimal string representation of the hashed value
 * @since 1.0.0
 */
infix fun ByteArray.hashingToString(algorithm: HashingAlgorithm): String =
    Hex.toHexString(hashing(algorithm))
/**
 * Computes the cryptographic hash of the string using the specified hashing algorithm
 * and returns the hash as a hexadecimal string.
 *
 * @param algorithm the cryptographic hashing algorithm to use for computing the hash
 * @throws HashingException if the hashing failed
 * @return the hexadecimal string representation of the hash
 * @since 1.0.0
 */
infix fun String.hashingToString(algorithm: HashingAlgorithm): String =
    Hex.toHexString(hashing(algorithm))
/**
 * Converts a ByteArray into its hex string representation after hashing it using the specified digest algorithm.
 *
 * @param algorithm The message digest algorithm to use for hashing.
 * @return A String representing the hex value of the hashed ByteArray.
 * @since 1.0.0
 */
infix fun ByteArray.hashingToString(algorithm: MessageDigest): String = Hex.toHexString(hashing(algorithm))
/**
 * Converts the hashed byte array of the current string, generated using the specified
 * {@code hashing} method, into its hexadecimal representation as a string.
 *
 * This method applies the hashing algorithm to the current string and formats the resulting
 * hashed byte array as a hexadecimal string.
 *
 * @param algorithm The MessageDigest instance representing the hashing algorithm to use.
 * @return The hexadecimal string representation of the hashed value.
 * @since 1.0.0
 */
infix fun String.hashingToString(algorithm: MessageDigest): String = Hex.toHexString(hashing(algorithm))

/**
 * Converts the result of hashing the current byte array using the specified hashing algorithm
 * into a hexadecimal representation.
 *
 * The method applies the cryptographic hash function specified by the input algorithm
 * to the byte array and returns the hash result as a hexadecimal string encapsulated
 * in a `Hex` object.
 *
 * @param algorithm The hashing algorithm to be used for generating a cryptographic hash of the byte array.
 * @return A `Hex` object containing the hexadecimal string representation of the computed hash.
 * @since 1.0.0
 */
infix fun ByteArray.hashingToHex(algorithm: HashingAlgorithm): dev.tommasop1804.kutils.classes.numbers.Hex =
    dev.tommasop1804.kutils.classes.numbers.Hex(Hex.toHexString(hashing(algorithm)))
/**
 * Converts the result of hashing the current string using the specified algorithm
 * into a hexadecimal representation.
 *
 * @param algorithm The cryptographic hashing algorithm to be used for hashing the string.
 *                  The supported algorithms are defined by the `HashingAlgorithm` enum.
 * @return A hexadecimal representation of the hash output computed with the specified algorithm.
 * @since 1.0.0
 */
infix fun String.hashingToHex(algorithm: HashingAlgorithm): dev.tommasop1804.kutils.classes.numbers.Hex =
    dev.tommasop1804.kutils.classes.numbers.Hex(Hex.toHexString(hashing(algorithm)))
/**
 * Converts a ByteArray to a hexadecimal representation after applying the specified hashing algorithm.
 *
 * @param algorithm The MessageDigest algorithm to use for hashing the ByteArray.
 * @return A Hex object containing the hexadecimal representation of the hashed ByteArray.
 * @since 1.0.0
 */
infix fun ByteArray.hashingToHex(algorithm: MessageDigest): dev.tommasop1804.kutils.classes.numbers.Hex =
    dev.tommasop1804.kutils.classes.numbers.Hex(Hex.toHexString(hashing(algorithm)))
/**
 * Generates a hexadecimal representation of a hash computed using the specified hashing algorithm.
 *
 * @param algorithm the MessageDigest instance defining the hashing algorithm to use.
 * @return a Hex instance representing the hexadecimal value of the computed hash.
 * @since 1.0.0
 */
infix fun String.hashingToHex(algorithm: MessageDigest): dev.tommasop1804.kutils.classes.numbers.Hex =
    dev.tommasop1804.kutils.classes.numbers.Hex(Hex.toHexString(hashing(algorithm)))

/**
 * Compares the hash of the current string, produced using the specified hashing algorithm,
 * with a provided hashed string.
 *
 * @param algorithm The hashing algorithm to be used for generating the hash.
 * @param hashedString The hashed string to compare against.
 * @throws HashingException if the hashing failed
 * @return `true` if the hash of the current string matches the provided hashed string, otherwise `false`.
 * @since 1.0.0
 */
fun String.hashingCompare(algorithm: HashingAlgorithm, hashedString: String) = hashingToString(algorithm) == hashedString
/**
 * Compares the hashed output of the current string, using the specified hashing algorithm, with a provided hashed value in hexadecimal format.
 *
 * @param algorithm The cryptographic hashing algorithm used to hash the current string.
 * @param hashedHex The hexadecimal representation of the hash value to compare against.
 * @return `true` if the hashed value of the string matches the provided hashed hexadecimal value, `false` otherwise.
 * @since 1.0.0
 */
fun String.hashingCompare(algorithm: HashingAlgorithm, hashedHex: dev.tommasop1804.kutils.classes.numbers.Hex) = hashingToString(algorithm) equalsIgnoreCase hashedHex.toString()
/**
 * Compares the content of the given byte array with a hashed value generated using the specified hashing algorithm.
 *
 * The method computes the hash of the current `ByteArray` using the provided `algorithm` and compares it
 * to the given `hashed` byte array to determine equality.
 *
 * @param algorithm The hashing algorithm to be used for generating the hash.
 * @param hashed The precomputed hash to be compared with the generated hash.
 * @since 1.0.0
 */
fun ByteArray.hashingCompare(algorithm: HashingAlgorithm, hashed: ByteArray) = hashing(algorithm).contentEquals(hashed)
/**
 * Compares the hash of the current `ByteArray` with a given hashed string using the specified
 * cryptographic hashing algorithm.
 *
 * @param algorithm the hashing algorithm used to generate the hash of the `ByteArray`
 * @param hashedString the hashed string value to compare against
 * @return `true` if the hash of the `ByteArray` matches the provided `hashedString`, otherwise `false`
 * @since 1.0.0
 */
fun ByteArray.hashingCompare(algorithm: HashingAlgorithm, hashedString: String) = hashingToString(algorithm) == hashedString
/**
 * Compares the cryptographic hash of the current `ByteArray` to a given hashed value represented as a hexadecimal string.
 *
 * This method hashes the `ByteArray` using the specified cryptographic hashing algorithm and checks if the result matches
 * the provided hashed hexadecimal value.
 *
 * @param algorithm The cryptographic hashing algorithm to use for generating the hash of the `ByteArray`.
 * @param hashedHex The expected hash value, represented as a hexadecimal string.
 * @return `true` if the hash of the `ByteArray` matches the given hexadecimal hash, `false` otherwise.
 * @since 1.0.0
 */
fun ByteArray.hashingCompare(algorithm: HashingAlgorithm, hashedHex: dev.tommasop1804.kutils.classes.numbers.Hex) = hashingToString(algorithm) equalsIgnoreCase hashedHex.toString()
/**
 * Compares the hash of the current string, computed using the specified hashing algorithm,
 * with a given hashed byte array for equality.
 *
 * @param algorithm The hashing algorithm to use for computing the hash of the current string.
 * @param hashed A byte array representing the precomputed hash to compare against.
 * @since 1.0.0
 */
fun String.hashingCompare(algorithm: HashingAlgorithm, hashed: ByteArray) = hashing(algorithm).contentEquals(hashed)

/**
 * Represents a collection of cryptographic hashing algorithms.
 *
 * Each algorithm in this enumeration serves a specific purpose and varies by
 * output size, structure, speed, and use case. These hashing algorithms are used
 * widely in security-related applications such as data integrity checks, digital
 * signatures, password hashing, and more.
 *
 * Commonly supported hashing standards include:
 * - Variants of SHA (SHA-1, SHA-2, and SHA-3 families)
 * - BLAKE2 and BLAKE3 family of algorithms
 * - RIPEMD family of hash functions
 * - Tiger, MD2, MD4, MD5, and WHIRLPOOL
 * - Skein family of cryptographic hashes
 * - GOST and DSTU standards hash functions
 * - Specialized algorithms like KECCAK, SHAKE, ParallelHash, TupleHash, and Haraka
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
enum class HashingAlgorithm {
    BLAKE2B_160,
    BLAKE2B_256,
    BLAKE2B_384,
    BLAKE2B_512,
    BLAKE2S_128,
    BLAKE2S_160,
    BLAKE2S_224,
    BLAKE2S_256,
    BLAKE3_256,
    DSTU7564_256,
    DSTU7564_384,
    DSTU7564_512,
    GOST3411,
    GOST3411_2012_256,
    GOST3411_2012_512,
    HARAKA_256,
    HARAKA_512,
    KECCAK_224,
    KECCAK_256,
    KECCAK_288,
    KECCAK_384,
    KECCAK_512,
    MD2,
    MD4,
    MD5,
    PARALLELHASH128_256,
    PARALLELHASH256_512,
    RIPEMD_128,
    RIPEMD_160,
    RIPEMD_256,
    RIPEMD_320,
    SHA_1,
    SHA_224,
    SHA_256,
    SHA_384,
    SHA_512,
    SHA_512_224,
    SHA_512_256,
    SHA3_224,
    SHA3_256,
    SHA3_384,
    SHA3_512,
    SHAKE128_256,
    SHAKE256_512,
    SKEIN_1024_1024,
    SKEIN_1024_384,
    SKEIN_1024_512,
    SKEIN_256_128,
    SKEIN_256_160,
    SKEIN_256_224,
    SKEIN_256_256,
    SKEIN_512_128,
    SKEIN_512_160,
    SKEIN_512_256,
    SKEIN_512_384,
    SKEIN_512_512,
    SM3,
    TIGER,
    TUPLEHASH128_256,
    TUPLEHASH256_512,
    WHIRLPOOL
}