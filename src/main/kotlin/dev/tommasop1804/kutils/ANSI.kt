package dev.tommasop1804.kutils

/**
 * ANSI escape code for setting the background color to yellow in terminal outputs.
 * This constant can be used to style text with a yellow background color.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
object ANSI {
    /**
     * ANSI escape code for resetting text formatting in console output.
     * This constant can be used to revert any applied styles, such as colors or text decorations, to default settings.
     *
     * @since 1.0.0
     */
    const val RESET = "\u001B[0m"
    /**
     * ANSI escape code for enabling bold text styling in console output.
     * It is widely used for adding emphasis to text or headings.
     *
     * @since 1.0.0
     */
    const val BOLD = "\u001B[1m"
    /**
     * ANSI escape code for applying italic text style in terminal output.
     *
     * Can be used to format console output by enabling the italic style.
     *
     * @since 1.0.0
     */
    const val ITALIC = "\u001B[3m"
    /**
     * ANSI escape sequence representing the underline text style.
     * This can be used to apply underline formatting to console output.
     *
     * @since 1.0.0
     */
    const val UNDERLINE = "\u001B[4m"
    /**
     * ANSI escape code for applying the blink text effect in terminal output.
     * It enables a blinking display of text when supported by the terminal.
     * The effect may not be supported in all terminal environments.
     *
     * @since 1.0.0
     */
    const val BLINK = "\u001B[5m"
    /**
     * ANSI escape code representing the rapid blink text effect.
     * When applied, it causes the text to blink at a rapid rate on supported terminals.
     *
     * Note: This effect may not be supported on all terminal emulators.
     *
     * @since 1.0.0
     */
    const val RAPID_BLINK = "\u001B[6m"
    /**
     * ANSI escape code representing the reverse video effect.
     * This effect swaps the foreground and background colors when applied to text in a terminal supporting ANSI escape codes.
     *
     * @since 1.0.0
     */
    const val REVERSE = "\u001B[7m"
    /**
     * ANSI escape code used to hide text in terminal-based applications.
     * When applied, the text will not be visible on the terminal output.
     * Depending on the terminal emulator, this feature may or may not be supported.
     *
     * @since 1.0.0
     */
    const val HIDDEN = "\u001B[8m"
    /**
     * ANSI escape code for applying strikethrough (crossed out) text formatting in terminal outputs.
     * This can be used to style text with a strikethrough effect.
     *
     * @since 1.0.0
     */
    const val CROSSED_OUT = "\u001B[9m"
    /**
     * ANSI escape code string representing the primary font settings.
     * It is used to apply the default terminal font style in environments
     * that support ANSI escape codes.
     *
     * @since 1.0.0
     */
    const val PRIMARY_FONT = "\u001B[10m"
    /**
     * ANSI escape code string representing the "Fraktur" text style.
     * This style is primarily used in terminals or consoles that support ANSI escape code formatting.
     *
     * @since 1.0.0
     */
    const val FRAKTUR = "\u001B[21m"
    /**
     * ANSI escape code representing the normal intensity text attribute in terminal outputs.
     * It resets bold or faint formatting, returning the text to its default weight.
     *
     * @since 1.0.0
     */
    const val NORMAL = "\u001B[22m"
    /**
     * ANSI escape code for applying a framed text style in terminal output.
     * When used, it applies a framing effect around the text.
     *
     * @since 1.0.0
     */
    const val FRAMED = "\u001B[51m"
    /**
     * ANSI escape code for applying the "encircled" text style in supported terminals.
     * This constant can be used to format text output with a circular text effect.
     *
     * Note: Support for this style may vary depending on the terminal or console.
     *
     * @since 1.0.0
     */
    const val ENCIRCLED = "\u001B[52m"
    /**
     * ANSI escape code for applying the "overlined" text style in terminal output.
     * This style adds a line above the text.
     *
     * Useful for styling console or terminal outputs when supported by the terminal.
     *
     * @since 1.0.0
     */
    const val OVERLINED = "\u001B[53m"
    /**
     * ANSI escape code for black-colored text. This constant can be used
     * to apply black color formatting to terminal or console text output.
     *
     * @since 1.0.0
     */
    const val BLACK_TEXT = "\u001B[30m"
    /**
     * ANSI escape code string that applies red color to text in terminal output.
     * It can be used for styling console messages to enhance readability or highlight errors.
     *
     * @since 1.0.0
     */
    const val RED_TEXT = "\u001B[31m"
    /**
     * ANSI escape code to set the text color to green in terminal outputs.
     * This can be used to enhance terminal display by applying green coloring to the text.
     *
     * @since 1.0.0
     */
    const val GREEN_TEXT = "\u001B[32m"
    /**
     * ANSI escape code for setting the text color to yellow in terminal outputs.
     * Can be used to style text in console applications or logs that support ANSI colors.
     *
     * @since 1.0.0
     */
    const val YELLOW_TEXT = "\u001B[33m"
    /**
     * ANSI escape code for blue text formatting.
     * This constant can be used to apply blue color to console output text.
     *
     * @since 1.0.0
     */
    const val BLUE_TEXT = "\u001B[34m"
    /**
     * ANSI escape code for setting the text color to magenta in terminal outputs.
     * Can be used to style text with the magenta color.
     *
     * @since 1.0.0
     */
    const val MAGENTA_TEXT = "\u001B[35m"
    /**
     * ANSI escape code for cyan-colored text.
     * This constant can be used to format console output with cyan color.
     *
     * @since 1.0.0
     */
    const val CYAN_TEXT = "\u001B[36m"
    /**
     * ANSI escape code for setting the text color to white in terminal output.
     * This constant can be used to format console text with white color
     * when printed on supported terminals.
     *
     * @since 1.0.0
     */
    const val WHITE_TEXT = "\u001B[37m"
    /**
     * Represents the default ANSI escape code for resetting text color to its default state in terminal output.
     * This constant can be used to revert text color back to its original state after applying other color codes.
     *
     * @since 1.0.0
     */
    const val DEFAULT_TEXT = "\u001B[39m"
    /**
     * ANSI escape code for setting the background color to black
     * in terminal output. This can be used to style console text
     * with a black background within supported terminals.
     *
     * @since 1.0.0
     */
    const val BLACK_BACKGROUND = "\u001B[40m"
    /**
     * ANSI escape code for setting the background color to red in terminal output.
     * This constant can be used to style text output with a red background.
     *
     * @since 1.0.0
     */
    const val RED_BACKGROUND = "\u001B[41m"
    /**
     * ANSI escape code for setting the background color to green in terminal output.
     * It is typically used in environments that support ANSI escape codes
     * to format text color and background for better visibility and emphasis.
     *
     * @since 1.0.0
     */
    const val GREEN_BACKGROUND = "\u001B[42m"
    /**
     * ANSI escape code for applying a yellow background color to the console output.
     *
     * This constant can be used to change the background color of printed text
     * to yellow when working with ANSI-compatible terminals.
     *
     * @since 1.0.0
     */
    const val YELLOW_BACKGROUND = "\u001B[43m"
    /**
     * ANSI escape code for applying a blue background color in terminal text formatting.
     * This constant can be used to style terminal output with a blue background.
     *
     * @since 1.0.0
     */
    const val BLUE_BACKGROUND = "\u001B[44m"
    /**
     * ANSI escape code for setting the background color to magenta in terminal text formatting.
     * This constant can be used to style text output in CLI applications by applying a magenta
     * background to the text that follows it.
     *
     * @since 1.0.0
     */
    const val MAGENTA_BACKGROUND = "\u001B[45m"
    /**
     * ANSI escape code for setting the background color of terminal text to cyan.
     * This constant can be used to style terminal output with a cyan background.
     *
     * @since 1.0.0
     */
    const val CYAN_BACKGROUND = "\u001B[46m"
    /**
     * ANSI escape code for setting the background color to white in terminal text formatting.
     * Can be used to enhance the visual representation of text in supported terminal environments.
     *
     * @since 1.0.0
     */
    const val WHITE_BACKGROUND = "\u001B[47m"
    /**
     * Represents the ANSI escape code for the default background color.
     * Used to reset the background color in terminal or console output.
     *
     * 
     * @since 1.0.0
     */
    const val DEFAULT_BACKGROUND = "\u001B[49m"
    /**
     * Represents the ANSI escape code for black bright text formatting.
     * Can be used to style console output with the specified text color.
     *
     * @since 1.0.0
     */
    const val BALCK_BRIGHT_TEXT = "\u001B[90m"
    /**
     * ANSI escape code for bright red text color.
     * This constant can be used to apply bright red coloring to text in console outputs.
     *
     * @since 1.0.0
     */
    const val RED_BRIGHT_TEXT = "\u001B[91m"
    /**
     * ANSI escape code representing bright green text color.
     * This can be used to format console output with a bright green text style.
     *
     * @since 1.0.0
     */
    const val GREEN_BRIGHT_TEXT = "\u001B[92m"
    /**
     * Represents the ANSI escape code for bright yellow text color in terminal output.
     * This constant can be used to style text output in supported terminal environments.
     *
     * @since 1.0.0
     */
    const val YELLOW_BRIGHT_TEXT = "\u001B[93m"
    /**
     * Represents the ANSI escape code for setting bright blue text color in terminal outputs.
     * This constant can be used to style text with a vivid blue color in console applications.
     *
     * @since 1.0.0
     */
    const val BLUE_BRIGHT_TEXT = "\u001B[94m"
    /**
     * A constant string representing the ANSI escape code for bright magenta text formatting.
     * This can be used in console or terminal outputs to apply bright magenta color to text.
     *
     * The ANSI escape code is "\u001B[95m".
     *
     * @since 1.0.0
     */
    const val MAGENTA_BRIGHT_TEXT = "\u001B[95m"
    /**
     * ANSI escape code for bright cyan text color.
     * This constant can be used to style console outputs with
     * a bright cyan color. It is compatible with terminals that
     * support ANSI coloring.
     *
     * @since 1.0.0
     */
    const val CYAN_BRIGHT_TEXT = "\u001B[96m"
    /**
     * ANSI escape code for setting the text color to bright white in terminal or console outputs.
     *
     * This constant can be used to enhance text visibility by applying a bright white color formatting.
     * It is commonly utilized in conjunction with other ANSI codes to style terminal output.
     *
     * @since 1.0.0
     */
    const val WHITE_BRIGHT_TEXT = "\u001B[97m"
    /**
     * Represents the default ANSI escape code for setting bright text in a terminal output.
     * This constant can be used to enhance readability by applying a brighter text style.
     *
     * The ANSI escape code is widely supported by many terminal emulators but may not be
     * effective on platforms or environments that do not support ANSI codes.
     *
     * @since 1.0.0
     */
    const val DEFAULT_BRIGHT_TEXT = "\u001B[99m"
    /**
     * ANSI escape code for black bright background color in terminal text formatting.
     * This constant represents the ANSI code `"\u001B[100m"` which, when applied,
     * changes the background color of text to bright black in supported terminal environments.
     *
     * @since 1.0.0
     */
    const val BALCK_BRIGHT_BACKGROUND = "\u001B[100m"
    /**
     * ANSI escape code for setting the background color to bright red in terminal output.
     * This value changes the background color of the text to bright red until the formatting is reset.
     *
     * @since 1.0.0
     */
    const val RED_BRIGHT_BACKGROUND = "\u001B[101m"
    /**
     * ANSI escape code for applying a bright green background color in terminal output.
     * This constant can be used for enhancing the appearance of text by adding a bright green background.
     *
     * @since 1.0.0
     */
    const val GREEN_BRIGHT_BACKGROUND = "\u001B[102m"
    /**
     * ANSI escape code for setting the background color to bright yellow in terminal output.
     * It modifies the background color of the subsequent text when printed to terminals
     * that support ANSI escape codes.
     *
     * @since 1.0.0
     */
    const val YELLOW_BRIGHT_BACKGROUND = "\u001B[103m"
    /**
     * ANSI escape code for setting the terminal background color to bright blue.
     * This code can be used to apply a bright blue background in terminal outputs
     * that support ANSI escape codes.
     *
     * @since 1.0.0
     */
    const val BLUE_BRIGHT_BACKGROUND = "\u001B[104m"
    /**
     * ANSI escape code for setting the background color to bright magenta in terminal text styling.
     *
     * This constant can be used to apply a bright magenta background to text in supported terminal environments.
     *
     * Note that the appearance of this color may vary depending on the terminal emulator being used.
     *
     * @since 1.0.0
     */
    const val MAGENTA_BRIGHT_BACKGROUND = "\u001B[105m"
    /**
     * ANSI escape code for setting the terminal text background color to bright cyan.
     * This constant can be used to format strings with a bright cyan background
     * when printed in ANSI-supported terminal environments.
     *
     * @since 1.0.0
     */
    const val CYAN_BRIGHT_BACKGROUND = "\u001B[106m"
    /**
     * ANSI escape code for setting the background color to bright white in terminal text formatting.
     * Commonly used to enhance text appearance by altering the background color.
     *
     * @since 1.0.0
     */
    const val WHITE_BRIGHT_BACKGROUND = "\u001B[107m"
    /**
     * Represents the default ANSI escape code for setting a bright background color in terminal output.
     * This value can be used to customize or reset the background color to the bright variant.
     *
     * The escape code corresponds to `\u001B[109m`.
     *
     * @since 1.0.0
     */
    const val DEFAULT_BRIGHT_BACKGROUND = "\u001B[109m"
    
    /**
     * Represents an ANSI code interface in which each implementation provides a specific ANSI code.
     * ANSI codes are typically used for styling console text, such as changing colors or formatting.
     *
     * @since 1.0.0
     * @author Tommaso Pastorelli
     */
    interface ANSICodes {
        /**
         * Represents the ANSI code associated with a specific terminal formatting or color configuration.
         * Typically used in terminal or console-based applications to apply formatting effects.
         *
         * @since 1.0.0
         */
        val code: Int
    }

    /**
     * Enum representing various ANSI text effects used for terminal text formatting.
     * Each enum constant corresponds to a specific ANSI effect code that modifies the
     * appearance of text, such as bold, italic, underline, etc.
     *
     *
     * This enum implements the [ANSICodes] interface to provide ANSI code values
     * corresponding to each effect. These codes can be utilized in composing ANSI escape
     * sequences for text formatting in terminal environments.
     *
     * @see ANSICodes
     *
     * @see ANSI
     *
     * @since 1.0.0
     */
    enum class Effect(
        /**
         * The ANSI effect code associated with this effect.
         * This value is used to define the ANSI escape sequence
         * for the specific text formatting effect represented by the enum constant.
         *
         * @since 1.0.0
         */
        override val code: Int
    ) : ANSICodes {
        /**
         * ANSI effect code representing the reset effect. This effect restores
         * all terminal text formatting to its default state, effectively
         * clearing any previously applied styles such as bold, italic,
         * underline, or color.
         *
         * @since 1.0.0
         */
        RESET(0),

        /**
         * Represents the ANSI effect code for bold text formatting.
         * This effect makes the text appear bold in terminal environments
         * where ANSI escape sequences are supported.
         *
         * @since 1.0.0
         */
        BOLD(1),

        /**
         * Represents the ANSI effect for rendering text in a dim or faint style.
         * The DIM effect makes the text appear less prominent compared to regular text.
         *
         * @since 1.0.0
         */
        DIM(2),

        /**
         * Represents the ANSI text effect for italicized text in terminal formatting.
         * When applied, the text appears in an italic style, depending on terminal support.
         *
         * @since 1.0.0
         */
        ITALIC(3),

        /**
         * Represents the ANSI text effect for underlining text in terminal environments.
         * The ANSI code associated with this effect is 4.
         *
         * @since 1.0.0
         */
        UNDERLINE(4),

        /**
         * Represents the ANSI text effect for blinking text.
         * This effect corresponds to the ANSI escape code that applies a slow
         * blinking effect to terminal text.
         *
         * @since 1.0.0
         */
        BLINK(5),

        /**
         * Represents the ANSI code for enabling rapid blinking of text in terminal environments.
         * This effect is intended for scenarios where text requires high visual emphasis by blinking
         * at a faster rate compared to standard blinking. Note that support for this effect may
         * vary across different terminal implementations.
         *
         * @since 1.0.0
         */
        RAPID_BLINK(6),

        /**
         * Represents the ANSI text effect for reversing the foreground and background colors
         * in terminal text formatting. This effect swaps the colors, making the foreground color
         * become the background color and vice versa.
         *
         *
         * Commonly used for creating visual emphasis or highlighting text in terminal applications.
         *
         * @since 1.0.0
         */
        REVERSE(7),

        /**
         * Represents hidden text effect in ANSI terminal formatting.
         * This effect makes the text invisible but still occupies space in the terminal.
         *
         * @since 1.0.0
         */
        HIDDEN(8),

        /**
         * Represents the ANSI effect code for rendering text as crossed out or strikethrough in terminal environments.
         * This effect is commonly used to indicate deleted or invalid text.
         *
         * @since 1.0.0
         */
        CROSSED_OUT(9),

        /**
         * Represents the ANSI text effect code for selecting the primary font.
         * This effect is used in terminal text formatting to apply the primary font
         * style as specified in the terminal settings.
         *
         * @since 1.0.0
         */
        PRIMARY_FONT(10),

        /**
         * Represents the ANSI effect code for Fraktur text styling.
         * Fraktur is a text effect commonly used to render stylized, Gothic-like fonts in terminal environments.
         * This effect is part of ANSI text formatting options but may not be supported across all terminals.
         *
         * @since 1.0.0
         */
        FRAKTUR(21),

        /**
         * ANSI text effect representing "normal" or "intensity off" style.
         * This effect resets the text intensity to the default, removing the bold or dim effect.
         *
         * @since 1.0.0
         */
        NORMAL(22),

        /**
         * Represents the ANSI text effect for applying a framed style to text in terminal
         * environments. The framed effect surrounds text with a border-like appearance,
         * enhancing its visibility or highlighting its importance.
         *
         *
         * This constant corresponds to the ANSI effect code 51.
         *
         * @since 1.0.0
         */
        FRAMED(51),

        /**
         * Represents the ANSI effect code for encircled text.
         * This constant is used to apply an encircled text effect in terminal environments
         * that support ANSI escape sequences.
         *
         * @since 1.0.0
         */
        ENCIRCLED(52),

        /**
         * Represents the ANSI effect code for rendering text with an overlined appearance.
         * This effect adds a horizontal line above the text.
         *
         * @since 1.0.0
         */
        OVERLINED(53);
    }

    /**
     * Enum representing various text colors according to ANSI color codes.
     *
     *
     * This enum provides a set of constants that correspond to ANSI escape codes
     * for setting text colors in terminal output. It implements the [ANSICodes]
     * interface, allowing integration with ANSI-based text formatting.
     *
     *
     * Each constant is associated with a specific numeric code that determines
     * the foreground color. These codes can be used to compose ANSI escape sequences,
     * which modify the appearance of text displayed in supported terminal environments.
     *
     *
     * The corresponding integer ANSI codes can be retrieved using the [.code]
     * method, allowing these enums to be combined and used with other ANSI codes
     * for styling terminal output.
     *
     * @see ANSICodes
     * @author Tommaso Pastorelli
     * @since 1.0.0
     */
    enum class TextColor (
        /**
         * Represents the numeric ANSI escape code associated with a specific text color.
         *
         *
         * This field stores the integer code used to define the foreground color for terminal text
         * according to ANSI standards. It enables the creation of escape sequences that
         * modify text appearance when printed to terminal outputs supporting ANSI codes.
         *
         *
         * This field is immutable and assigned during the construction of the [TextColor] enum constants.
         *
         * @since 1.0.0
         */
        override val code: Int
    ) : ANSICodes {
        /**
         * Represents the color black in ANSI text color codes.
         *
         *
         * This constant corresponds to the ANSI code `30`, used to set the
         * text color to black in terminal output. It can be utilized to create ANSI
         * escape sequences for styling text color when supported by the terminal.
         *
         * @since 1.0.0
         */
        BLACK(30),

        /**
         * Represents the ANSI color code for red text in terminal output.
         *
         *
         * The `RED` constant corresponds to the ANSI color code `31`, which modifies
         * the text color to red. This can be used to style text in supported ANSI-capable terminal
         * environments by incorporating it into ANSI escape sequences.
         *
         *
         *  * Foreground color: Red
         *  * ANSI Code: 31
         *
         *
         * @since 1.0.0
         */
        RED(31),

        /**
         * Represents the green text color according to the ANSI color code (32).
         *
         *
         * This constant can be used to set the foreground color of text to green
         * in terminal output that supports ANSI escape codes. It corresponds to the
         * ANSI code 32.
         *
         *
         * Green is commonly used to signify success or positive confirmations
         * in terminal-based messages.
         *
         * @see TextColor
         *
         * @since 1.0.0
         */
        GREEN(32),

        /**
         * Represents the ANSI color code for yellow text in terminal output.
         *
         *
         * This constant corresponds to the ANSI numeric code `33`, which
         * is used to set the foreground color of text to yellow in ANSI-compatible
         * terminal environments. When combined with other codes in an ANSI escape
         * sequence, it can modify the appearance of terminal text.
         *
         *
         * Yellow is often used to represent warning messages or to draw attention
         * to specific information in terminal output.
         *
         * @see TextColor
         *
         * @since 1.0.0
         */
        YELLOW(33),

        /**
         * Represents the color blue in the ANSI text color coding system.
         *
         *
         * This constant is used to apply the blue foreground color
         * to terminal output that supports ANSI escape codes. It corresponds
         * to the ANSI color code `34`.
         *
         *
         * Blue is commonly used to emphasize informational or neutral
         * content in text output.
         *
         * @since 1.0.0
         */
        BLUE(34),

        /**
         * Represents the ANSI color code for magenta text.
         *
         *
         * This constant is a member of the [TextColor] enum, which defines
         * ANSI-colored text formatting options for terminal output. It corresponds
         * to the magenta foreground color in the ANSI color palette, represented by
         * the integer code `35`.
         *
         *
         * When used in conjunction with ANSI escape sequences, this color can
         * be applied to text to render it in magenta on terminals that support
         * ANSI color codes.
         *
         * @see TextColor
         *
         * @since 1.0.0
         */
        MAGENTA(35),

        /**
         * Represents the ANSI color code for cyan text.
         *
         *
         * The constant is used in terminal environments that support ANSI escape codes to style text with a cyan foreground color.
         * It corresponds to the numeric code `36`, which can be used in constructing ANSI escape sequences for text formatting.
         *
         * @see TextColor
         *
         * @see ANSICodes
         *
         * @since 1.0.0
         */
        CYAN(36),

        /**
         * Represents the ANSI color code for the WHITE text color.
         *
         *
         * This constant is part of the [TextColor] enum and corresponds
         * to the ANSI code `37`, which sets the text color to white in
         * terminal output. It can be used in combination with other ANSI codes
         * to format text in supported terminal environments.
         *
         * @see TextColor
         *
         * @since 1.0.0
         */
        WHITE(37),

        /**
         * Represents the default text color according to ANSI color codes.
         *
         *
         * The DEFAULT color uses the ANSI code 39 and signifies the default color
         * for text as determined by the terminal or environment settings. It is used
         * when no other specific color is set or to reset the color formatting to the default state.
         *
         *
         * Can be used in combination with other ANSI codes to reset styled terminal output
         * to the terminal's predefined color scheme.
         *
         * @since 1.0.0
         */
        DEFAULT(39),

        /**
         * Represents the bright black text color in the ANSI escape code system.
         *
         *
         * The color black is one of the predefined bright text colors in ANSI, corresponding
         * to the numeric code `90`. This constant can be used to format text output
         * in terminal environments when using ANSI escape sequences.
         *
         * @see ANSICodes
         *
         * @since 1.0.0
         */
        BRIGHT_BLACK(90),

        /**
         * Represents the bright red text color in the ANSI escape codes.
         *
         *
         * This constant is used to format text with a bright red foreground color
         * in terminal environments supporting ANSI codes.
         *
         * @since 1.0.0
         */
        BRIGHT_RED(91),

        /**
         * Represents the ANSI escape code for bright green text color.
         *
         *
         * This constant is part of the `TextBrightColor` enumeration and corresponds
         * to the numeric value `92`, which is used to apply a bright green color
         * to text when displayed in terminal environments that support ANSI escape codes.
         *
         *
         * Typically used to enhance text visibility or signify importance in terminal outputs.
         *
         * @since 1.0.0
         */
        BRIGHT_GREEN(92),

        /**
         * Represents the ANSI escape code for the bright variant of the yellow text color.
         *
         *  * ANSI Code: 93
         *  * Description: Bright yellow color used for text formatting in terminal outputs.
         *
         *
         *
         * This constant is a part of the `TextBrightColor` enumeration, which supports the
         * integration of bright text colors into ANSI escape sequences for enhanced text styling.
         *
         * @since 1.0.0
         */
        BRIGHT_YELLOW(93),

        /**
         * Represents the bright blue text color in ANSI escape codes.
         *
         *
         * The numeric code for this color is `94`, which is part
         * of the ANSI standard for specifying text formatting in terminal
         * environments. This constant can be used to render text
         * in bright blue when constructing ANSI escape sequences.
         *
         * @since 1.0.0
         */
        BRIGHT_BLUE(94),

        /**
         * Bright magenta text color represented in ANSI escape codes.
         *
         *  * Corresponds to the numeric code `95` in the ANSI standard.
         *  * This color is part of the bright variant set of text colors.
         *
         * Used for formatting text output in terminal environments.
         *
         * @since 1.0.0
         */
        BRIGHT_MAGENTA(95),

        /**
         * Represents the bright cyan text color in ANSI escape codes.
         *
         *
         * This constant is part of the ANSI bright color palette that can be used
         * for formatting text in terminal environments. It corresponds to the
         * numeric ANSI code `96`, which applies a bright cyan color to text.
         *
         * @since 1.0.0
         */
        BRIGHT_CYAN(96),

        /**
         * Represents the bright white text color in ANSI escape codes.
         *
         *
         * This constant corresponds to the ANSI numeric code `97`, which is
         * used to format text with a bright white foreground in terminal environments.
         *
         * @since 1.0.0
         */
        BRIGHT_WHITE(97),

        /**
         * Represents the default terminal text color in the ANSI bright color set.
         *
         *
         * This color corresponds to the standard terminal default color,
         * mapped to the ANSI numeric code 99.
         *
         *
         * It is typically used when no specific text color formatting is desired
         * or to reset to the terminal's default text color after applying other
         * color codes.
         *
         * @since 1.0.0
         */
        BRIGHT_DEFAULT(99);
    }

    /**
     * Enumeration of ANSI background color codes used for terminal text styling.
     * Each constant corresponds to a specific ANSI code that sets the background
     * color of the terminal text.
     *
     *
     * This enum implements [ANSICodes] to provide the ANSI code
     * associated with each background color. The numeric values align with the
     * standard ANSI escape code sequences for terminal styling.
     *
     *
     * This enum is typically used to compose ANSI escape code sequences that
     * can style terminal output by defining background colors.
     *
     * @see ANSICodes
     * @author Tommaso Pastorelli
     * @since 1.0.0
     */
    enum class BackgroundColor(
        /**
         * The ANSI code representing the background color in the terminal text styling.
         *
         *
         * This code is used to define the specific ANSI escape sequence
         * corresponding to a terminal background color.
         *
         *
         * The integer value adheres to the standard ANSI escape code format for text styling.
         * It is associated with a specific background color as defined in the `BackgroundColor` enum.
         *
         * @since 1.0.0
         */
        override val code: Int
    ) : ANSICodes {
        /**
         * Represents the ANSI background color code for black.
         *
         *
         * The constant `BLACK(40)` corresponds to the ANSI code used to set the
         * background color of terminal text to black. This ANSI color code is typically
         * used in terminal text styling to define the background appearance of textual
         * output.
         *
         *
         * Part of the [BackgroundColor] enumeration, which provides a collection
         * of ANSI background color codes.
         *
         * @since 1.0.0
         */
        BLACK(40),

        /**
         * Represents the ANSI background color code for red in terminal text styling.
         *
         *
         * The constant value corresponds to the ANSI escape code sequence
         * used to set the background color of terminal text to red.
         *
         *
         * This enum member is a part of the `BackgroundColor` enumeration
         * which provides various ANSI codes for different terminal background colors.
         * It is commonly used to apply a red background to text in terminal outputs
         * formatted with ANSI escape sequences.
         *
         * @see BackgroundColor
         *
         * @see ANSICodes
         *
         * @since 1.0.0
         */
        RED(41),

        /**
         * Represents the ANSI escape code for setting the background color to green
         * in terminal text styling.
         *
         *
         * When used in composing ANSI escape sequences, this color sets the
         * background of text to green in compatible terminal environments.
         *
         * @since 1.0.0
         */
        GREEN(42),

        /**
         * Represents the ANSI background color code for yellow.
         *
         *
         * This enum constant is part of the `BackgroundColor` enumeration, which defines
         * standard ANSI codes for terminal text background color styling.
         *
         *
         * The YELLOW color corresponds to the ANSI escape code `43`.
         *
         *
         *  * Use this constant to style the terminal text background color as yellow.
         *  * Can be used in conjunction with other ANSI escape sequences for comprehensive
         * terminal text customization.
         *
         *
         * @see BackgroundColor
         *
         * @see ANSICodes
         *
         * @since 1.0.0
         */
        YELLOW(43),

        /**
         * Represents the ANSI background color code for blue in terminal text styling.
         * The value of this constant corresponds to the ANSI standard code for setting
         * the terminal background color to blue.
         *
         *
         *  * ANSI code: 44
         *  * Usage in terminal styling for blue background color
         *
         *
         * @see BackgroundColor
         *
         * @since 1.0.0
         */
        BLUE(44),

        /**
         * Represents the ANSI background color code for MAGENTA.
         *
         *
         * This constant corresponds to the ANSI escape code `45`, which sets the background
         * color of terminal text to magenta. It is part of the [BackgroundColor] enumeration
         * and is primarily used to style text in terminal output using ANSI escape sequences.
         *
         *
         *
         * The MAGENTA background color is typically used in terminal or console applications
         * where text needs to be highlighted or differentiated with a magenta background.
         *
         * @see BackgroundColor
         *
         * @see ANSICodes
         *
         * @since 1.0.0
         */
        MAGENTA(45),

        /**
         * Represents the ANSI code for the cyan background color used in terminal text styling.
         * This constant corresponds to the ANSI escape code sequence for setting a cyan background.
         *
         *
         * Typically used to style terminal outputs by defining a cyan background,
         * enhancing readability or visual emphasis.
         *
         * @since 1.0.0
         */
        CYAN(46),

        /**
         * Represents the ANSI background color code for white.
         * This code applies a white background to terminal text output.
         *
         *
         * ANSI escape codes are used for styling terminal output text, and this
         * specific code corresponds to the white background color.
         *
         *
         * Typically utilized in combination with other ANSI codes to produce
         * styled terminal output.
         *
         * @since 1.0.0
         */
        WHITE(47),

        /**
         * Represents the ANSI escape code for resetting the terminal text background
         * color to its default setting.
         *
         *
         * This constant corresponds to the ANSI code value `49`, which resets
         * the background color to the terminal’s default configuration. It is
         * typically used to remove any previously applied background color styling.
         *
         *
         * Useful for scenarios where reverting to the default background is
         * required during terminal output customization, ensuring a clean
         * appearance or restoring default behavior.
         *
         * @since 1.0.0
         */
        DEFAULT(49),

        /**
         * Represents the BRIGHT_BLACK color with an intensity level of 100.
         * It may be used in color-related operations or representations that
         * require extended color palettes.
         *
         * @since 1.0.0
         */
        BRIGHT_BLACK(100),

        /**
         * Represents a high-intensity red color constant, indicated by the value `101`.
         *
         * This constant can be used in contexts where a strong or vibrant red tone is required.
         *
         * @since 1.0.0
         */
        BRIGHT_RED(101),

        /**
         * Represents a bright green color with its corresponding intensity value.
         * This constant is likely used for color representation and rendering purposes.
         *
         * @since 1.0.0
         */
        BRIGHT_GREEN(102),

        /**
         * Constant representing the bright yellow color with the associated code 103.
         * It may be used in various contexts requiring a representation of bright yellow.
         *
         * @since 1.0.0
         */
        BRIGHT_YELLOW(103),

        /**
         * Represents a bright blue color with a value of 104. Typically used for text styling
         * or enumeration purposes referencing color codes.
         *
         * @since 1.0.0
         */
        BRIGHT_BLUE(104),

        /**
         * Constant representing the color bright magenta with an associated code value of 105.
         * This value may be used for text or background color settings where bright magenta is required.
         *
         * @since 1.0.0
         */
        BRIGHT_MAGENTA(105),

        /**
         * Represents the color code for bright cyan.
         *
         * This predefined constant can be used for representing the bright cyan color
         * in contexts where numeric color codes are utilized.
         *
         * @since 1.0.0
         */
        BRIGHT_CYAN(106),

        /**
         * Represents the bright white color with its corresponding code.
         * This constant is typically used for color management or terminal coloring.
         *
         * @since 1.0.0
         */
        BRIGHT_WHITE(107),

        /**
         * Default brightness level setting used across the application.
         *
         * This represents the standard brightness configuration
         * that is used when no specific brightness level is provided.
         *
         * @since 1.0.0
         */
        BRIGHT_DEFAULT(109);
    }
    
    /**
     * Composes an ANSI escape code sequence using a combination of numeric values
     * and objects implementing the [ANSICodes] interface.
     *
     *
     * This method builds an ANSI escape code sequence based on the provided input,
     * which can include integers representing ANSI codes, or objects implementing the
     * [ANSICodes] interface. The method concatenates these codes and returns
     * the resulting ANSI escape sequence as a string.
     *
     * @param codes an array of input values, where each element may be:
     *
     *  * An integer representing an ANSI code.
     *  * An object implementing [ANSICodes], which returns an ANSI code via
     * the [ANSICodes.code] method.
     *
     * Passing any other type of object will result in an `IllegalArgumentException`.
     *
     * @return a `String` representing the composed ANSI escape code sequence, suitable
     * for applying text formatting in terminal environments.
     *
     * @throws IllegalArgumentException if an element in the input array is neither an integer
     * nor an object implementing the [ANSICodes] interface.
     *
     * @since 1.0.0
     */
    fun compose(vararg codes: Any?) = buildString {
        append("\u001B[")
        for (code in codes) {
            when (code) {
                is Number -> append(code.toInt())
                is ANSICodes -> append(code.code)
                else -> throw IllegalArgumentException("Invalid code: $code")
            }
            append(';')
        }
        deleteCharAt(length - 1)
        append("m")
    }

    /**
     * Moves the cursor to the specified position in the terminal.
     *
     * @param row The row number (1-based) to move the cursor to.
     * @param column The column number (1-based) to move the cursor to.
     * @since 1.0.0
     */
    fun moveCursor(row: Int, column: Int) = "\u001B[$row;${column}H"

    /**
     * Clears the terminal screen by sending an ANSI escape sequence to the console.
     * The behavior of the screen clearing depends on the mode parameter.
     *
     * @param mode The ANSI clear screen mode. Default is 2, which clears the entire screen and moves the cursor to the top-left.
     * Supported values:
     * - 0: Clears everything from the cursor to the end of the screen.
     * - 1: Clears everything from the cursor to the beginning of the screen.
     * - 2: Clears the entire screen and moves the cursor to the top-left.
     * @since 1.0.0
     */
    fun clearScreen(mode: Int = 2) = "\u001B[${mode}J"

    /**
     * Clears a specific portion of the current line in the terminal output based on the provided mode.
     *
     * This function generates an ANSI escape sequence that instructs the terminal to clear:
     * - The entire line (`mode = 2`)
     * - From the cursor to the end of the line (`mode = 0`)
     * - From the beginning of the line to the cursor (`mode = 1`)
     *
     * @param mode An integer defining the clearing mode:
     * - 0: Clears from the cursor to the end of the line.
     * - 1: Clears from the beginning of the line to the cursor.
     * - 2: Clears the entire line. Default is `2`.
     * @since 1.0.0
     */
    fun clearLine(mode: Int = 2) = "\u001B[${mode}K"

    /**
     * Moves the cursor up by the specified number of lines in the terminal.
     * This function returns an ANSI escape code string that can be used
     * to manipulate the cursor position. The default value for the number
     * of lines is 1.
     *
     * @param lines The number of lines to move the cursor up. Defaults to 1.
     * @since 1.0.0
     */
    fun cursorUp(lines: Int = 1) = "\u001B[${lines}A"

    /**
     * Moves the cursor down by the specified number of lines in a terminal or console output.
     *
     * @param lines The number of lines to move the cursor down. Defaults to 1.
     * @since 1.0.0
     */
    fun cursorDown(lines: Int = 1) = "\u001B[${lines}B"

    /**
     * Moves the cursor forward by the specified number of columns in the terminal.
     * The cursor is moved using ANSI escape codes.
     *
     * @param columns The number of columns to move the cursor forward. Defaults to 1.
     * @return A string containing the ANSI escape code to move the cursor forward.
     * @since 1.0.0
     */
    fun cursorForward(columns: Int = 1) = "\u001B[${columns}C"

    /**
     * Moves the cursor backward by a specified number of columns in a terminal.
     *
     * By default, the cursor moves backward by 1 column.
     *
     * @param columns The number of columns to move the cursor backward. Defaults to 1.
     * @since 1.0.0
     */
    fun cursorBackward(columns: Int = 1) = "\u001B[${columns}D"

    /**
     * Saves the current cursor position in the terminal.
     *
     * This method outputs the ANSI escape code for saving the cursor's current position.
     * The saved position can later be restored using a corresponding restore command
     * (e.g., `"\u001B[u"`).
     *
     * @return The ANSI escape code as a string that, when used in a terminal, saves the current cursor position.
     * @since 1.0.0
     */
    fun saveCursorPosition() = "\u001B[s"

    /**
     * Restores the cursor position to the last saved location in the terminal.
     *
     * This function returns a control sequence that directs the terminal to move
     * the cursor to the position previously stored using `saveCursorPosition`.
     * It is commonly used in terminal-based applications for cursor manipulation.
     *
     * @return A string containing the ANSI escape sequence for restoring the cursor position.
     * @since 1.0.0
     */
    fun restoreCursorPosition() = "\u001B[u"

    /**
     * Hides the cursor in a terminal or console output by returning the ANSI escape sequence
     * that disables the visible cursor.
     *
     * The returned escape sequence is commonly used in terminal-based applications
     * to improve visual feedback, especially for animations or dynamic content updates.
     *
     * @since 1.0.0
     */
    fun hideCursor() = "\u001B[?25l"

    /**
     * Makes the terminal cursor visible by returning the associated ANSI escape code.
     * Useful for restoring the cursor visibility after it has been hidden.
     *
     * @return A string containing the ANSI escape code to show the cursor.
     * @since 1.0.0
     */
    fun showCursor() = "\u001B[?25h"
}