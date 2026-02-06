@file:JvmName("CharUtilsKt")
@file:Suppress("unused")
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since


/**
 * A constant representing the single space character (' ').
 * This property provides a convenient way to access the space character
 * without needing to explicitly declare it in the code.
 *
 * @receiver The [Char.Companion] object.
 * @return The single space character.
 * @since 1.0.0
 */
val Char.Companion.SPACE get() = ' '

/**
 * Represents the hypen character ('-') as a constant property of the Char companion object.
 *
 * This property provides a convenient way to reference the hypen character without
 * explicitly writing its literal value in the code.
 *
 * @since 1.0.0
 */
val Char.Companion.HYPEN get() = '-'

/**
 * Represents the Unicode EN DASH character (U+2013).
 *
 * The EN DASH is commonly used to represent ranges, such as dates or numbers, 
 * and is slightly longer than a hyphen but shorter than an em dash.
 *
 * @since 1.0.0
 */
val Char.Companion.EN_DASH get() = '–'
/**
 * Represents the Unicode character 'EM DASH' (U+2014).
 * This is a punctuation mark used to create a strong break in the structure of a sentence.
 * It is wider than a hyphen and en dash.
 *
 * @since 1.0.0
 */
val Char.Companion.EM_DASH get() = '—'

/**
 * Represents the underscore character ('_').
 * This constant is defined as a property of the `Char` companion object
 * for convenience, allowing access without directly using a literal.
 *
 * @receiver The companion object of the `Char` class.
 * @return The underscore character.
 * @since 1.0.0
 */
val Char.Companion.UNDERSCORE get() = '_'

/**
 * A constant representing the forward slash character ('/').
 * This is often used as a directory path separator in Unix-based systems
 * or in URLs to separate path segments.
 *
 * @receiver Char.Companion
 * @since 1.0.0
 */
val Char.Companion.SLASH get() = '/'

/**
 * Represents the backslash character ('\') as a constant for convenient reuse.
 *
 * This constant can be used wherever the backslash character is needed,
 * instead of repeatedly specifying the character literal.
 *
 * @since 1.0.0
 */
val Char.Companion.BACKSLASH get() = '\\'

/**
 * Represents the pipe character ('|') as a constant value associated with the Char.Companion object.
 * This can be useful for improving code readability or using consistent symbolic constants.
 *
 * @since 1.0.0
 */
val Char.Companion.PIPE get() = '|'

/**
 * A constant property representing the asterisk (*) character.
 * This character is commonly used as a wildcard or delimiter in various contexts.
 *
 * @since 1.0.0
 */
val Char.Companion.STAR get() = '*'

/**
 * Represents the dot character ('.') as a constant property of the Char.Companion object.
 *
 * This property provides a convenient way to reference the dot character
 * without directly using the character literal.
 *
 * @since 1.0.0
 */
val Char.Companion.DOT get() = '.'

/**
 * A constant representing the comma character (',').
 *
 * This is a common delimiter character used in various programming contexts,
 * particularly in CSV (Comma-Separated Values) files, separating elements in
 * a list, or within language-specific syntax structures.
 *
 * @since 1.0.0
 */
val Char.Companion.COMMA get() = ','

/**
 * Represents the semicolon character (';').
 * This is a shorthand property for accessing the semicolon character in a concise and readable manner.
 *
 * @receiver Char.Companion
 * @return The semicolon character.
 * @since 1.0.0
 */
val Char.Companion.SEMICOLON get() = ';'

/**
 * A constant property representing the colon character `':'`.
 *
 * This property is part of the `Char` companion object and can be used
 * wherever a colon character is needed in a Kotlin program.
 *
 * @since 1.0.0
 */
val Char.Companion.COLON get() = ':'

/**
 * A constant representing the plus character ('+').
 * This is a companion property of the `Char` class.
 *
 * @since 1.0.0
 */
val Char.Companion.PLUS get() = '+'

/**
 * Represents the equals sign character ('=').
 * This property is a shorthand for accessing the equals sign.
 *
 * @receiver Char.Companion
 * @return The equals sign character.
 * @since 1.0.0
 */
val Char.Companion.EQUALS_SIGN get() = '='

/**
 * Represents the exclamation mark character ('!').
 * This property provides a convenient way to access the exclamation mark
 * as a constant value.
 *
 * @receiver Char.Companion The companion object of the Char class.
 * @return The exclamation mark character.
 * @since 1.0.0
 */
val Char.Companion.EXCLAMATION_MARK get() = '!'

/**
 * Represents the question mark character ('?').
 *
 * This constant provides a shorthand way to access the question
 * mark character for use in various operations such as
 * string manipulation, pattern matching, or symbol reference.
 *
 * @since 1.0.0
 */
val Char.Companion.QUESTION_MARK get() = '?'

/**
 * Represents the hash character ('#'), commonly used in various contexts such as social media,
 * hashtags, or as a special symbol in programming or formatted strings.
 *
 * This property is a part of the Char companion object, providing a quick and accessible
 * reference to the hash character without the need to define it explicitly in code.
 *
 * @since 1.0.0
 */
val Char.Companion.HASH get() = '#'

/**
 * Represents the '@' character as a constant property within the `Char` companion object.
 *
 * This property provides a convenient way to access the '@' character without
 * the need to explicitly define it elsewhere in the code.
 *
 * @since 1.0.0
 */
val Char.Companion.AT get() = '@'

/**
 * Represents the ampersand character ('&').
 *
 * This property provides a convenient way to access the ampersand character
 * as a companion object property of the `Char` type.
 *
 * @since 1.0.0
 */
val Char.Companion.AND get() = '&'

/**
 * Represents the dollar sign ('$') character as a constant for the Char companion object.
 *
 * This property provides a convenient way to access the dollar sign character without
 * needing to manually specify it each time.
 *
 * @receiver Char.Companion
 * @return The dollar sign character ('$').
 * @since 1.0.0
 */
val Char.Companion.DOLLAR get() = '$'

/**
 * A constant property representing the percent (%) symbol.
 * It is a shorthand for the '%' character, providing an easily
 * identifiable and reusable symbol for percentage-related operations.
 *
 * @since 1.0.0
 */
val Char.Companion.PERCENT get() = '%'

/**
 * A constant property representing the apostrophe character ('\'').
 *
 * This property can be used wherever a single-quote character is needed
 * in the context of operations related to `Char`.
 *
 * @since 1.0.0
 */
val Char.Companion.APOSTROPHE get() = '\''

/**
 * Represents the quotation mark character ('"') as a read-only property of the [Char] companion object.
 * This can be used when a quotation mark character is needed, avoiding the direct use of string literals.
 *
 * @since 1.0.0
 */
val Char.Companion.QUOTATION_MARK get() = '"'

/**
 * Represents the null character (NUL) with the Unicode value '\u0000'.
 * This is often used as a string terminator in programming and
 * is the first character in the ASCII table.
 *
 * @since 1.0.0
 */
val Char.Companion.NUL get() = '\u0000'

/**
 * Represents the "Start of Header" control character in the ASCII table.
 * This is a non-printable character with the Unicode value '\u0001'.
 *
 * Commonly used in communication protocols to signify the beginning of a header segment.
 *
 * @since 1.0.0
 */
val Char.Companion.START_OF_HEADER get() = '\u0001'
/**
 * Represents the Start of Header (SOH) control character in ASCII,
 * also known as Control-A. Its Unicode code point is U+0001.
 *
 * Commonly used in text-based communication protocols to indicate
 * the beginning of a header section.
 *
 * @since 1.0.0
 */
val Char.Companion.SOH get() = '\u0001'

/**
 * Represents the 'Start of Text' (STX) control character in the ASCII table.
 * This character is part of the C0 control set and is commonly used
 * in communication protocols to denote the beginning of a text block.
 *
 * The Unicode code point for this character is U+0002.
 *
 * @since 1.0.0
 */
val Char.Companion.START_OF_TEXT get() = '\u0002'
/**
 * Represents the Start of Text (STX) control character,
 * used in communication protocols to indicate the beginning of a text block.
 *
 * Unicode value: U+0002.
 *
 * @since 1.0.0
 */
val Char.Companion.STX get() = '\u0002'

/**
 * Represents the 'End of Text' control character in Unicode, with the code point U+0003.
 * This control character is commonly used to indicate the end of a text block in certain protocols.
 *
 * @since 1.0.0
 */
val Char.Companion.END_OF_TEXT get() = '\u0003'
/**
 * Represents the End of Text (ETX) control character in ASCII, with a Unicode value of '\u0003'.
 * Commonly used to signify the end of a text transmission or content in communication protocols.
 *
 * @since 1.0.0
 */
val Char.Companion.ETX get() = '\u0003'

/**
 * Represents the End-of-Transmission (EOT) character in ASCII with code point U+0004.
 *
 * This character is typically used to indicate the end of a data transmission
 * or communication session, often in telecommunication contexts.
 *
 * It is a control character and not intended to be printable.
 *
 * @since 1.0.0
 */
val Char.Companion.END_OF_TRANSMISSION get() = '\u0004'
/**
 * Represents the End-of-Transmission (EOT) control character, with Unicode value `\u0004`.
 * EOT is commonly used to signify the end of a transmission or input in communication protocols.
 *
 * It is a part of the ASCII control characters set.
 *
 * @since 1.0.0
 */
val Char.Companion.EOT get() = '\u0004'

/**
 * Represents the ENQUIRY (ENQ) control character in ASCII.
 *
 * ENQUIRY is a communication control character intended to request a response
 * from a remote station to confirm that it is still present or to prompt it
 * to send its next message. It is typically used in telecommunication protocols.
 *
 * @return The ENQUIRY control character as a Unicode character literal.
 * @since 1.0.0
 */
val Char.Companion.ENQUIRY get() = '\u0005'
/**
 * Represents the End of Medium (ENQ) control character in the ASCII table.
 * This character has a Unicode value of `\u0005`.
 * Typically used in communication protocols to request a response from the receiving station,
 * ensuring that the connection is still active.
 *
 * @since 1.0.0
 */
val Char.Companion.ENQ get() = '\u0005'

/**
 * Represents the ACK (acknowledge) control character in the ASCII character set.
 * This character is often used in communication protocols to indicate a positive acknowledgment of received data.
 *
 * The Unicode code point for this character is U+0006.
 *
 * @since 1.0.0
 */
val Char.Companion.ACKNOWLEDGE get() = '\u0006'
/**
 * Represents the ASCII "Acknowledgment" control character, often used in communication
 * protocols to signify a positive acknowledgment of a message.
 *
 * Unicode value: U+0006.
 *
 * @since 1.0.0
 */
val Char.Companion.ACK get() = '\u0006'

/**
 * Represents the bell/control character in ASCII, corresponding to the Unicode value '\u0007'.
 * Commonly used to produce an audible beep or signal in legacy systems and terminals.
 *
 * @since 1.0.0
 */
val Char.Companion.BELL get() = '\u0007'
/**
 * Represents the BEL (bell) control character, which is commonly used to trigger an audible or visual alert.
 * It is defined by the Unicode escape '\u0007'.
 *
 * This character belongs to the set of ASCII control characters and is often used in terminal or console outputs.
 *
 * @since 1.0.0
 */
val Char.Companion.BEL get() = '\u0007'

/**
 * Represents the backspace character ('\b') as a constant within the `Char` companion object.
 * This character is often used to indicate a deletion or to move the cursor back one position in text processing.
 *
 * @since 1.0.0
 */
val Char.Companion.BACKSPACE get() = '\b'
/**
 * Represents the backspace control character (`'\b'`).
 * This character is commonly used to indicate a non-printed character
 * that moves the cursor one position backwards in a text or console output.
 *
 * @since 1.0.0
 */
val Char.Companion.BS get() = '\b'

/**
 * A constant holding the horizontal tab character (`'\t'`).
 * It is used to insert a horizontal tab in text or strings.
 *
 * @since 1.0.0
 */
val Char.Companion.HORIZONTAL_TAB get() = '\t'
/**
 * Represents the horizontal tab (HT) character as a `Char` constant.
 *
 * The horizontal tab character is commonly used for spacing and formatting text,
 * aligning columns, or indentation in various textual data and documents.
 *
 * This property is part of the `Char.Companion` object, enabling
 * the use of `Char.HT` to refer to the tab character directly.
 *
 * @see Char
 * @since 1.0.0
 */
val Char.Companion.HT get() = '\t'
/**
 * Represents the tabulation character '\t' as a constant property
 * in the [Char.Companion] object. This property provides a simple
 * and readable way to access the tab character.
 *
 * @since 1.0.0
 */
val Char.Companion.TAB get() = '\t'

/**
 * Represents the line feed character (`\n`), commonly used as a newline indicator
 * in many operating systems and programming contexts.
 *
 * This is a constant property defined in the [Char.Companion] object, providing
 * a convenient way to reference the line feed character without hardcoding it
 * directly in the code.
 *
 * @since 1.0.0
 */
val Char.Companion.LINE_FEED get() = '\n'
/**
 * A constant holding the line feed character (`\n`), representing a newline
 * in text.
 *
 * This character is often used to denote the end of a line in text files or
 * strings. It is commonly utilized in various platforms and programming
 * environments to signify line breaks.
 *
 * @receiver Char.Companion - Companion object of the Char class.
 * @since 1.0.0
 */
val Char.Companion.LF get() = '\n'

/**
 * Represents the vertical tabulation character (`VT`) in Unicode, with the value of `\u000B`.
 * Commonly used as a control character to move the printing position to the same column
 * on the next line in some text-based interfaces or formats.
 *
 * @since 1.0.0
 */
val Char.Companion.VERTICAL_TAB get() = '\u000B'
/**
 * Represents the vertical tab character (VT), which is a control character
 * with Unicode code point U+000B. It is used to move the cursor to the next
 * vertical tab stop.
 *
 * @since 1.0.0
 */
val Char.Companion.VT get() = '\u000B'

/**
 * Represents the form feed control character ('\u000C').
 * Commonly used in text processing to indicate a page break in printing contexts
 * or as a delimiter in various systems.
 *
 * @since 1.0.0
 */
val Char.Companion.FORM_FEED get() = '\u000C'
/**
 * Represents the form feed (`\u000C`) Unicode character.
 * The form feed character is used to indicate a page break in text processing.
 *
 * @since 1.0.0
 */
val Char.Companion.FF get() = '\u000C'

/**
 * Represents the carriage return character ('\r') as a constant for the Char type.
 *
 * Commonly used in text processing and formatting to indicate a return to the
 * beginning of a line, often in combination with a line feed ('\n') for line breaks
 * in some operating systems.
 *
 * @receiver Char.Companion
 * @since 1.0.0
 */
val Char.Companion.CARRIAGE_RETURN get() = '\r'
/**
 * Represents the carriage return character ('\r'), commonly used to reset
 * the position of the cursor to the beginning of the line without advancing
 * to the next line. Often used in text formatting and communication protocols.
 *
 * @since 1.0.0
 */
val Char.Companion.CR get() = '\r'

/**
 * Represents the SHIFT OUT control character (U+000E) in the Unicode standard.
 *
 * The SHIFT OUT character is a control character used to switch to an alternate character set.
 *
 * @since 1.0.0
 */
val Char.Companion.SHIFT_OUT get() = '\u000E'
/**
 * Represents the Shift Out (SO) control character in Unicode,
 * which is used to indicate a shift to an alternate character set.
 *
 * This character has the Unicode code point U+000E.
 *
 * @since 1.0.0
 */
val Char.Companion.SO get() = '\u000E'

/**
 * Represents the ASCII control character "SHIFT IN" (SI),
 * with the Unicode code point U+000F. This character is
 * commonly used to switch to an alternate character set
 * or mode in certain protocols and systems.
 *
 * @since 1.0.0
 */
val Char.Companion.SHIFT_IN get() = '\u000F'
/**
 * A Unicode character constant representing a specific control character
 * in the ASCII table, known as Shift In (SI). The SI character is used
 * to switch to an alternate character set.
 *
 * @receiver Char.Companion
 * @return The SI control character ('\u000F').
 * @since 1.0.0
 */
val Char.Companion.SI get() = '\u000F'

/**
 * Represents the Data Link Escape (DLE) control character in the Unicode standard.
 * Commonly used in communication protocols for signaling control sequences or escaping
 * special characters in data streams.
 *
 * Unicode value: U+0010
 *
 * @since 1.0.0
 */
val Char.Companion.DATA_LINK_ESCAPE get() = '\u0010'
/**
 * Represents the Data Link Escape (DLE) control character in the Unicode standard.
 * It is commonly used to signal that the following characters should be treated
 * as data rather than control instructions in data communication protocols.
 *
 * Unicode value: U+0010
 *
 * @since 1.0.0
 */
val Char.Companion.DLE get() = '\u0010'

/**
 * Represents the Negative Acknowledge (NAK) control character,
 * which is part of the ASCII control code set and commonly used
 * for signaling a negative response or indicating the presence
 * of an error in communication protocols.
 *
 * This constant corresponds to the Unicode code point U+0015.
 *
 * @since 1.0.0
 */
val Char.Companion.NEGATIVE_ACKNOWLEDGE get() = '\u0015'
/**
 * Represents the ASCII Negative Acknowledgment (NAK) control character.
 * This character is used in communication protocols to indicate a
 * negative response or that an error has occurred.
 *
 * Unicode: U+0015
 *
 * @since 1.0.0
 */
val Char.Companion.NAK get() = '\u0015'

/**
 * Represents the SYNCHRONOUS IDLE character in the ASCII control characters set.
 * This character is typically used as a filler character in synchronous data
 * transmission systems to maintain timing synchronization between devices.
 *
 * Unicode: U+0016
 *
 * @since 1.0.0
 */
val Char.Companion.SYNCHRONOUS_IDLE get() = '\u0016'
/**
 * Represents the synchronous idle control character (SYN) in ASCII.
 *
 * This control character is typically used in synchronous communication systems
 * to provide synchronization.
 *
 * Unicode: U+0016
 * Decimal value: 22
 *
 * @since 1.0.0
 */
val Char.Companion.SYN get() = '\u0016'

/**
 * Represents the "End of Transmission Block" control character in Unicode.
 * This character is used to signify the end of a block of data transmission.
 *
 * Unicode: U+0017
 *
 * @since 1.0.0
 */
val Char.Companion.END_OF_TRANSMISSION_BLOCK get() = '\u0017'
/**
 * Represents the End of Transmit Block (ETB) control character.
 * Unicode value: U+0017.
 *
 * This character is used to indicate the end of a transmission block
 * in communication protocols or data streams.
 *
 * @receiver Companion object of the Char class.
 * @return A Char representing the ETB control character.
 * @since 1.0.0
 */
val Char.Companion.ETB get() = '\u0017'

/**
 * Represents the CANCEL control character, which is a non-printable character
 * with the Unicode value U+0018. It is commonly used to indicate a cancel
 * operation in communication protocols or data transmission.
 *
 * @since 1.0.0
 */
val Char.Companion.CANCEL get() = '\u0018'
/**
 * Represents the Control Character "Cancel" (CAN) with the Unicode value `\u0018`.
 * This character is used to indicate that the data or operation being transmitted is no longer needed or valid.
 *
 * @since 1.0.0
 */
val Char.Companion.CAN get() = '\u0018'

/**
 * Represents the 'END OF MEDIUM' control character in the Unicode standard.
 * This character is typically used to indicate the conclusion of a data medium.
 *
 * The Unicode code point for this character is U+0019.
 *
 * @since 1.0.0
 */
val Char.Companion.END_OF_MEDIUM get() = '\u0019'
/**
 * Represents the "EM" control character as defined in the Unicode standard.
 * The EM (End of Medium) character is used to indicate the end of a physical medium,
 * often associated with communication or data transfer operations.
 *
 * @receiver Char.Companion
 * @return The Unicode character '\u0019'.
 * @since 1.0.0
 */
val Char.Companion.EM get() = '\u0019'

/**
 * Represents the SUBSTITUTE character (Unicode U+001A).
 * It is commonly used as a substitute for a character that is invalid,
 * unrecognized, or in error.
 *
 * @since 1.0.0
 */
val Char.Companion.SUBSTITUTE get() = '\u001A'
/**
 * Represents the substitute control character (SUB), which is a Unicode character
 * with the code point \u001A. This character is often used in text streams
 * to indicate the substitution of erroneous or invalid data.
 *
 * @since 1.0.0
 */
val Char.Companion.SUB get() = '\u001A'

/**
 * Represents the ASCII escape character, often used for initiating escape sequences.
 * The escape character is commonly employed in terminal control sequences
 * and other text encoding mechanisms.
 *
 * @since 1.0.0
 */
val Char.Companion.ESCAPE get() = '\u001B'
/**
 * Represents the ASCII escape character (ESC), which is commonly used
 * for control sequences in text formatting, terminal instructions, or
 * communication protocols.
 *
 * This is a constant property of the `Char.Companion` object and provides
 * a convenient way to access the escape character.
 *
 * @return The escape character ('\u001B').
 * @since 1.0.0
 */
val Char.Companion.ESC get() = '\u001B'

/**
 * A custom-defined constant that represents the file separator character.
 *
 * This character can be utilized to separate file path components or for other
 * use cases where a delimiter is needed to indicate a file boundary or structure
 * within strings.
 *
 * The file separator character is represented as the Unicode character '\u001C'.
 *
 * @since 1.0.0
 */
val Char.Companion.FILE_SEPARATOR get() = '\u001C'
/**
 * A constant representing the File Separator (FS) character, which is
 * ASCII control code 28 (U+001C). It is used as a data delimiter in
 * certain text-processing contexts.
 *
 * @since 1.0.0
 */
val Char.Companion.FS get() = '\u001C'

/**
 * Represents the group separator control character (ASCII: 29).
 * Commonly used as a delimiter to separate groups of data in text processing.
 *
 * @since 1.0.0
 */
val Char.Companion.GROUP_SEPARATOR get() = '\u001D'
/**
 * Represents the ASCII Group Separator (GS) control character with the Unicode value '\u001D'.
 * This character is used as a delimiter to separate groups of data in text processing or communication,
 * particularly in data interchange formats.
 *
 * @since 1.0.0
 */
val Char.Companion.GS get() = '\u001D'

/**
 * Represents the ASCII 'Record Separator' control character (␞),
 * which is used to separate records in text data.
 * Commonly utilized in file and data stream processing.
 *
 * @since 1.0.0
 */
val Char.Companion.RECORD_SEPARATOR get() = '\u001e'
/**
 * Represents the Record Separator (RS) control character in Unicode.
 * The RS character is commonly used as a delimiter to separate records in data streams or files.
 *
 * This property is a part of the `Char` companion object, providing convenient access
 * to the control character '\u001e'.
 *
 * @return The Unicode character '\u001e', representing the Record Separator.
 * @since 1.0.0
 */
val Char.Companion.RS get() = '\u001e'

/**
 * Represents the Unit Separator control character (US) in ASCII with the Unicode value `'\u001F'`.
 * This character is primarily used as a control character to separate units of text in data streams.
 *
 * @since 1.0.0
 */
val Char.Companion.UNIT_SEPARATOR get() = '\u001F'
/**
 * A read-only property that represents the `Unit Separator` (US) control character
 * in the Unicode standard. The US character is primarily used in data structures
 * as a separator to divide units of information.
 *
 * Unicode value: U+001F
 *
 * @since 1.0.0
 */
val Char.Companion.US get() = '\u001F'

/**
 * Represents the DELETE control character in the Unicode standard.
 * DELETE is often used as a control character to indicate the deletion
 * of a character or data in certain contexts.
 *
 * Unicode value: U+007F.
 *
 * @since 1.0.0
 */
val Char.Companion.DELETE get() = '\u007F'
/**
 * Represents the 'DEL' control character in the Unicode standard, which has a code point of 127.
 * This character is historically used to indicate a deleted or ignored character in telecommunication systems.
 *
 * @return the 'DEL' control character as a Char.
 * @since 1.0.0
 */
val Char.Companion.DEL get() = '\u007F'

/**
 * A predefined array of characters representing the hexadecimal digits (0-9, a-f).
 * This array is typically used for operations that involve hexadecimal conversions
 * or representations in base-16 numbering systems.
 *
 * @since 1.0.0
 */

val Char.Companion.LOWER_HEX_DIGIT
    get() = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
/**
 * A constant array of characters representing the hexadecimal digits in uppercase.
 * This array includes the digits `0` through `9` and the letters `A` through `F`.
 * It is commonly used for operations involving hexadecimal representations,
 * such as encoding or mathematical conversions.
 *
 * @since 1.0.0
 */
val Char.Companion.UPPER_HEX_DIGIT
    get() = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
/**
 * A constant array representing the vowels in the English alphabet.
 *
 * This array contains the lowercase vowels: 'a', 'e', 'i', 'o', 'u'.
 * It is intended for use in text processing or validation tasks where
 * identifying vowels is required.
 *
 * @since 1.0.0
 */
val Char.Companion.LOWER_VOWEL
    get() = charArrayOf('a', 'e', 'i', 'o', 'u')
/**
 * A constant array representing the vowels in the English alphabet.
 *
 * This array contains the lowercase vowels: 'a', 'e', 'i', 'o', 'u'.
 * It is intended for use in text processing or validation tasks where
 * identifying vowels is required.
 *
 * @since 1.0.0
 */
val Char.Companion.UPPER_VOWEL
    get() = charArrayOf('A', 'E', 'I', 'O', 'U')
/**
 * Defines an array of characters representing the English alphabet consonants.
 *
 * This array includes all lowercase consonant letters in the standard English alphabet,
 * excluding vowels (a, e, i, o, u). It can be used for operations or processing where a
 * predefined set of consonants is required.
 *
 * @since 1.0.0
 */
val Char.Companion.LOWER_CONSONANT
    get() = charArrayOf('b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z')
/**
 * Defines an array of characters representing the English alphabet consonants.
 *
 * This array includes all lowercase consonant letters in the standard English alphabet,
 * excluding vowels (a, e, i, o, u). It can be used for operations or processing where a
 * predefined set of consonants is required.
 *
 * @since 1.0.0
 */
val Char.Companion.UPPER_CONSONANT
    get() = charArrayOf('B', 'C', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z')

/**
 * Determines whether the character is an ASCII character.
 *
 * An ASCII character is defined as any character with a Unicode code point
 * value between 0 and 127 inclusive.
 *
 * @receiver The character to check.
 * @return `true` if the character is an ASCII character, `false` otherwise.
 * @since 1.0.0
 */
val Char.isAscii get() = this < 128.toChar()

/**
 * Checks if the character is an ASCII alphabetic character.
 *
 * This includes both uppercase [A-Z] and lowercase [a-z] ASCII characters.
 *
 * @receiver The character to check.
 * @return `true` if the character is an ASCII alphabetic character, `false` otherwise.
 * @since 1.0.0
 */
val Char.isAsciiAlpha get() = isAsciiAlphaUpper || isAsciiAlphaLower

/**
 * Checks if the character is a lowercase ASCII alphabetic character
 * (ranging from 'a' to 'z').
 *
 * @receiver The character to check.
 * @return `true` if the character is a lowercase ASCII letter, `false` otherwise.
 * @since 1.0.0
 */
val Char.isAsciiAlphaLower get() = this in 'a'..'z'

/**
 * Checks if the character is an uppercase ASCII alphabetic character.
 *
 * This function determines whether the character falls within the range of 'A' to 'Z',
 * inclusive, and returns `true` if it does, otherwise `false`.
 *
 * @receiver The character to be checked.
 * @return `true` if the character is an uppercase ASCII alphabetic character, `false` otherwise.
 * @since 1.0.0
 */
val Char.isAsciiAlphaUpper get() = this in 'A'..'Z'

/**
 * Checks if this character is an ASCII alphanumeric character.
 *
 * An ASCII alphanumeric character is defined as either an ASCII letter
 * (uppercase or lowercase) or an ASCII digit (0-9).
 *
 * @receiver The character to be checked.
 * @return `true` if the character is an ASCII alphanumeric character, `false` otherwise.
 * @since 1.0.0
 */
val Char.isAsciiAlphanumeric get() = isAsciiAlpha || isAsciiDigit

/**
 * Checks if the character is an ASCII digit.
 *
 * An ASCII digit is any character between '0' and '9' inclusive.
 *
 * @receiver The character to check.
 * @return `true` if the character is an ASCII digit, `false` otherwise.
 * @since 1.0.0
 */
val Char.isAsciiDigit get() = this in '0'..'9'

/**
 * Determines whether the character is an ASCII control character.
 *
 * ASCII control characters are non-printable characters in the ASCII table.
 * These include characters with values in the range 0 to 31 inclusive and the
 * character with a value of 127 (DEL).
 *
 * @receiver The character to check.
 * @return `true` if the character is an ASCII control character, `false` otherwise.
 * @since 1.0.0
 */
val Char.isAsciiControl get() = this < ' ' || this == 127.toChar()

/**
 * Checks if the character is an ASCII printable character.
 *
 * An ASCII printable character is any character in the range
 * from 32 (inclusive) to 127 (exclusive) in the ASCII table.
 *
 * @receiver The character to check.
 * @return `true` if the character is an ASCII printable character, otherwise `false`.
 * @since 1.0.0
 */
val Char.isAsciiPrintable get() = this >= ' ' && this < 127.toChar()

/**
 * Determines if the character is an ASCII whitespace character.
 *
 * An ASCII whitespace character is one of the following:
 * - Horizontal Tab (`\t`)
 * - New Line (`\n`)
 * - Vertical Tab (`\u000b`)
 * - Form Feed (`\u000c`)
 * - Carriage Return (`\r`)
 * - Space (` `)
 *
 * @receiver The character to check.
 * @return `true` if the character is an ASCII whitespace character, `false` otherwise.
 * @since 1.0.0
 */
val Char.isAsciiWhitespace get() = this in "\t\n\u000b\u000c\r "

/**
 * Checks if the character is an ASCII punctuation character.
 *
 * The method determines if the character belongs to the set of characters
 * traditionally considered as punctuation in the ASCII standard. This
 * includes symbols such as !, ", #, $, %, &, ', (, ), *, +, ,, -, ., /, :, ;, <,
 * =, >, ?, @, [, \, ], ^, _, `, {, |, }, and ~.
 *
 * @receiver The character to check.
 * @return `true` if the character is an ASCII punctuation character; otherwise, `false`.
 * @since 1.0.0
 */
val Char.isAsciiPunctation get() = this in "!'\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}"

/**
 * Checks if the character is a valid hexadecimal digit.
 *
 * A hexadecimal digit is defined as a character that belongs to
 * the set of valid characters for hexadecimal values, which
 * includes '0'-'9', 'a'-'f', and 'A'-'F'.
 *
 * @receiver The character to check.
 * @return `true` if the character is a hexadecimal digit, `false` otherwise.
 * @since 1.0.0
 */
val Char.isHexDigit get() = this in Char.LOWER_HEX_DIGIT || this in Char.UPPER_HEX_DIGIT

/**
 * Checks if the current character is a lowercase hexadecimal digit.
 *
 * A lowercase hexadecimal digit is defined as one of the characters
 * `'0'` through `'9'` or `'a'` through `'f'`.
 *
 * @return `true` if the character is a lowercase hexadecimal digit, `false` otherwise.
 * @since 1.0.0
 */
val Char.isLowerHexDigit get() = this in Char.LOWER_HEX_DIGIT

/**
 * Checks if the character is an uppercase hexadecimal digit.
 *
 * An uppercase hexadecimal digit is defined as one of the following characters:
 * '0'-'9' or 'A'-'F'.
 *
 * @receiver The character to be checked.
 * @return `true` if the character is an uppercase hexadecimal digit, `false` otherwise.
 * @since 1.0.0
 */
val Char.isUpperHexDigit get() = this in Char.UPPER_HEX_DIGIT

/**
 * Checks if the character is a vowel.
 *
 * A character is considered a vowel if it appears in a predefined set of vowel characters.
 * The set of vowel characters may include both uppercase and lowercase vowels (e.g., 'A', 'a', 'E', 'e').
 *
 * @receiver The character to be checked.
 * @return `true` if the character is a vowel; `false` otherwise.
 * @since 1.0.0
 */
val Char.isVowel get() = this in Char.LOWER_VOWEL || this in Char.UPPER_VOWEL

/**
 * Checks if the character is a lowercase vowel (a, e, i, o, u).
 *
 * This method determines whether the given character is a member of
 * the predefined set of lowercase vowels. The check is case-sensitive
 * and does not consider uppercase vowels.
 *
 * @receiver The character to check.
 * @return `true` if the character is a lowercase vowel, `false` otherwise.
 * @since 1.0.0
 */
val Char.isLowerVowel get() = this in Char.LOWER_VOWEL

/**
 * Determines if a character is an uppercase vowel.
 *
 * This method checks if the character belongs to the set of uppercase vowels,
 * which typically includes 'A', 'E', 'I', 'O', and 'U'.
 *
 * @receiver The character to check.
 * @return `true` if the character is an uppercase vowel, otherwise `false`.
 * @since 1.0.0
 */
val Char.isUpperVowel get() = this in Char.UPPER_VOWEL

/**
 * Checks whether the character is a consonant.
 *
 * Determines if the character belongs to the set of consonants,
 * excluding vowels and non-alphabetic characters.
 *
 * @receiver The character to be examined.
 * @return `true` if the character is a consonant, `false` otherwise.
 * @since 1.0.0
 */
val Char.isConsonant get() = this in Char.LOWER_CONSONANT || this in Char.UPPER_CONSONANT

/**
 * Checks if the invoking `Char` is a lowercase consonant.
 *
 * This function verifies whether the character belongs to the set of predefined
 * lowercase consonants. If the character matches any of the consonants within
 * the `LOWER_CONSONANT` collection, the function returns `true`; otherwise, it
 * returns `false`.
 *
 * @receiver The character to be evaluated.
 * @return `true` if the character is a lowercase consonant, otherwise `false`.
 * @since 1.0.0
 */
val Char.isLowerConsonant get() = this in Char.LOWER_CONSONANT

/**
 * Checks if the character is an uppercase consonant.
 *
 * A character is considered an uppercase consonant if it is
 * an uppercase letter in the English alphabet that is not a vowel
 * (A, E, I, O, U).
 *
 * @receiver The character to be evaluated.
 * @return `true` if the character is an uppercase consonant; otherwise, `false`.
 * @since 1.0.0
 */
val Char.isUpperConsonant get() = this in Char.UPPER_CONSONANT

/**
 * Appends the current character to the provided elements and returns the resulting string.
 *
 * This function creates a new string by appending the character the method is called on
 * followed by the specified elements.
 *
 * @param elements The elements to append after the character. Can be zero or more arguments of any type.
 * @since 1.0.0
 */
fun Char.append(vararg elements: Any) = buildString { append(this@append, *elements) }
/**
 * Appends the string representation of the given element to the character.
 *
 * Combines the character on which this function is called with the provided element
 * and returns the resulting string.
 *
 * @param element The element to append to the character. Its string representation will be used.
 * @since 1.0.0
 */
infix fun Char.append(element: Any) = buildString { append(this@append, element) }

/**
 * Creates a string consisting of the character repeated the specified number of times.
 *
 * @param times the number of times to repeat the character. Must be non-negative.
 * @since 1.0.0
 */
operator fun Char.times(times: Int) = buildString { repeat(times) { append(this@times) } }

/**
 * Returns the uppercase equivalent of the character on which the unary plus operator is applied.
 * This operator function is used to convert a character to its uppercase form based on the default locale.
 *
 * @receiver The character to be converted to uppercase.
 * @return The uppercase form of the character.
 * @since 1.0.0
 */
operator fun Char.unaryPlus() = uppercase()

/**
 * Returns the lowercase form of the character on which the unary minus operator is invoked.
 * This operator simplifies the conversion of a character to its lowercase equivalent.
 *
 * @receiver Char The character to convert to lowercase.
 * @return Char The lowercase equivalent of the receiver character.
 * @since 1.0.0
 */
operator fun Char.unaryMinus() = lowercase()

/**
 * Invokes the functionality to find the index of the character in the given character sequence.
 * Depending on the value of the `last` parameter, it finds either the first or the last occurrence
 * of this character in the provided `cs` starting from the specified index.
 *
 * @param cs the character sequence to search within
 * @param last if `true`, searches for the last occurrence; otherwise, searches for the first occurrence
 * @param startIndex the index to start the search from
 * @param ignoreCase if `true`, the search ignores character case; otherwise, case matters
 * @since 1.0.0
 */
operator fun Char.invoke(cs: CharSequence, last: Boolean = false, startIndex: Int = 0, ignoreCase: Boolean = false) =
    if (!last) cs.indexOf(this, startIndex, ignoreCase) else cs.lastIndexOf(this, startIndex, ignoreCase)