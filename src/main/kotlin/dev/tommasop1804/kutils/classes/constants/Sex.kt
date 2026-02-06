package dev.tommasop1804.kutils.classes.constants

/**
 * Represents the biological classification of sex, commonly used to define male and female.
 *
 * This enum provides a symbolic representation for each category.
 *
 * @constructor Defines a Sex with its respective symbol.
 * @param symbol The Unicode character symbol representing the sex type.
 * @since 1.0.0
 */
enum class Sex(val symbol: Char) {
    /**
     * Represents the male sex with its associated symbol.
     *
     * This enum constant is a member of the `Sex` enumeration.
     * It is typically used to distinguish male gender in contexts
     * where a symbol or gender specification is required.
     *
     * @property symbol The symbol associated with the male sex.
     * @since 1.0.0
     */
    MALE('♂'),
    /**
     * Represents the female sex with the corresponding symbol.
     *
     * @property symbol The symbol associated with the female sex.
     * @since 1.0.0
     */
    FEMALE('♀');

    companion object {
        /**
         * Maps a given character to its corresponding `Sex` enum constant.
         *
         * This method evaluates the input character and returns the `MALE` or `FEMALE` constant
         * based on predefined mappings. If the character does not match any of the specified symbols,
         * the function returns `null`.
         *
         * @param char The character to map to a `Sex` enum constant. Acceptable values include:
         * - '♂', 'M', 'm' for `MALE`
         * - '♀', 'F', 'f' for `FEMALE`
         * @return The corresponding `Sex` enum constant (`MALE` or `FEMALE`), or `null` if no match is found.
         * @since 1.0.0
         */
        infix fun of(char: Char) = when (char) {
            '♂', 'M', 'm' -> MALE
            '♀', 'F', 'f' -> FEMALE
            else -> null
        }
    }
}