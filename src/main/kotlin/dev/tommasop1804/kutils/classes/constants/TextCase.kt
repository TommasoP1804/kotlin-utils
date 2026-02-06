package dev.tommasop1804.kutils.classes.constants

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.exceptions.ConversionException
import kotlin.reflect.KProperty

/**
 * Enumeration that represent the possible text cases.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
enum class TextCase(
        /**
     * The validator that checks if a [CharSequence] is in this case.
     * @since 1.0.0
     */
    val validator: Predicate<String>,
    /**
     * The converter that converts a [CharSequence] to this case.
     * @since 1.0.0
     */
    val converter: Transformer<String, String>,
    /**
     * The separator of words.
     * @since 1.0.0
     */
    val separator: String
) {
    /**
     * LOREM IPSUM DOLOR SIT AMET
     * @since 1.0.0
     */
    UPPER_CASE(String::isUpperCase, String::uppercase, " "),

    /**
     * lorem ipsum dolor sit amet
     * @since 1.0.0
     */
    LOWER_CASE(String::isLowerCase, String::lowercase, " "),

    /**
     * Lorem ipsum dolor sit amet
     * @since 1.0.0
     */
    SENTENCE_CASE(String::isSentenceCase, String::sentenceCase, " "),

    /**
     * Lorem Ipsum Dolor Sit Amet
     * @since 1.0.0
     */
    TITLE_CASE(String::isTitleCase, String::titleCase, " "),

    /**
     * LOREM_IPSUM_DOLOR_SIT_AMET
     * @since 1.0.0
     */
    UPPER_SNAKE_CASE(String::isUpperSnakeCase, String::upperSnakeCase, "_"),

    /**
     * lorem_ipsum_dolor_sit_amet
     * @since 1.0.0
     */
    SNAKE_CASE(String::isSnakeCase, String::snakeCase, "_"),

    /**
     * Lorem_Ipsum_Dolor_Sit_Amet
     * @since 1.0.0
     */
    TITLE_SNAKE_CASE(String::isTitleSnakeCase, String::titleSnakeCase, "_"),

    /**
     * LOREM-IPSUM-DOLOR-SIT-AMET
     * @since 1.0.0
     */
    UPPER_KEBAB_CASE(String::isUpperKebabCase, String::upperKebabCase, "-"),

    /**
     * lorem-ipsum-dolor-sit-amet
     * @since 1.0.0
     */
    KEBAB_CASE(String::isKebabCase, String::kebabCase, "-"),

    /**
     * Lorem-Ipsum-Dolor-Sit-Amet
     * @since 1.0.0
     */
    TITLE_KEBAB_CASE(String::isTitleKebabCase, String::titleKebabCase, "-"),

    /**
     * loremIpsumDolorSitAmet
     * @since 1.0.0
     */
    CAMEL_CASE(String::isCamelCase, String::camelCase, ""),

    /**
     * LoremIpsumDolorSitAmet
     * @since 1.0.0
     */
    PASCAL_CASE(String::isPascalCase, String::pascalCase, ""),

    /**
     * lorem Ipsum doLor Sit ameT
     * @since 1.0.0
     */
    STANDARD({ true }, { it }, " ");

    companion object {
        /**
         * Converts a given text from one text case format to another, replacing the specified separator.
         *
         * @receiver  the input text to be converted, must not be null
         * @param from  the original [TextCase] format of the input text
         * @param to    the desired [TextCase] format to convert the text to
         * @return the converted text with the specified text case format
         * @throws ConversionException if conversion failed.
         * @since 1.0.0
        */
        fun String.convertCase(from: TextCase = STANDARD, to: TextCase): String {
            try {
                if (from == CAMEL_CASE || from == PASCAL_CASE) {
                    val sb = StringBuilder()
                    for ((i, c) in withIndex()) {
                        if (i != 0 && (c.isUpperCase() || (c.isDigit() && !this[i-1].isDigit()))) {
                            sb.append(to.separator)
                            sb.append(c.lowercaseChar())
                        } else {
                            sb.append(c)
                        }
                    }
                    return sb.toString().convertCase(LOWER_CASE, to)
                }
                return to.converter(replace(from.separator, " "))
            } catch (e: Exception) {
                throw ConversionException("Unable to convert text case from ${from.name} to ${to.name}", e)
            }
        }
    }

    /**
     * Checks if a [CharSequence] is in this case.
     * @param text the text to check
     * @return `true` if the text is in this case, `false` otherwise
     * @since 1.0.0
     */
    operator fun invoke(text: String) = validator(text)

    /**
     * Converts the `TextCase` properties into a `Map` representation.
     *
     * This function extracts internal properties of the `TextCase` class, namely `validator`,
     * `converter`, and `separator`, and maps them to their respective keys: "validator",
     * "converter", and "separator".
     *
     * @return a `Map` containing the key-value pairs of the `TextCase` instance properties.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "validator" to validator,
        "converter" to converter,
        "separator" to separator
    )

    /**
     * Retrieves the value mapped to the given property name from a backing map.
     *
     * @param thisRef The reference to the object for which the property is being delegated. It can be `null`.
     * @param property The metadata for the property being accessed.
     * @return The value associated with the property name, cast to the specified type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}