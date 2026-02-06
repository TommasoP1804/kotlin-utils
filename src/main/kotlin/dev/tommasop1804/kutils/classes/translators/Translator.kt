package dev.tommasop1804.kutils.classes.translators

import dev.tommasop1804.kutils.StringList
import dev.tommasop1804.kutils.annotations.Beta
import dev.tommasop1804.kutils.classes.coding.JSON
import dev.tommasop1804.kutils.classes.coding.YAML
import dev.tommasop1804.kutils.exceptions.ConfigurationException
import dev.tommasop1804.kutils.exceptions.TranslationException
import dev.tommasop1804.kutils.tryOrThrow
import java.io.File
import java.nio.file.Path
import kotlin.enums.EnumEntries

/**
 * A class that provides translation capabilities by reading and extracting data from configuration files
 * in YAML or JSON format. The translation is performed using keys that map to corresponding values in the file.
 *
 * @property file The file object representing the configuration file to be used for translation.
 * @constructor Initializes the `Translator` with the specified `File`, `Path`, or `String` representation of the file path.
 * @throws ConfigurationException if an unsupported file extension is encountered or if the configuration file is invalid.
 * @since 1.0.0
 */
@Suppress("unused")
open class Translator(
    val file: File
) {
    /**
     * Secondary constructor for the `Translator` class that initializes an instance
     * using a `Path` object. The `Path` is converted to a `File` instance internally.
     *
     * @param file The `Path` object representing the configuration file to be used for translation.
     * @since 1.0.0
     */
    constructor(file: Path) : this(file.toFile())
    /**
     * Initializes a `Translator` instance using a file path represented as a string.
     *
     * This constructor converts the provided string file path into a `Path` object and initializes
     * the `Translator` instance accordingly. The file path must point to a valid configuration file with
     * supported extensions (e.g., YAML or JSON) for successful usage.
     *
     * @param file the string representation of the file path to be used as the configuration source
     * @since 1.0.0
     */
    constructor(file: String) : this(Path.of(file))

    companion object {
        /**
         * Translates the specified key to its corresponding value by reading the content of a configuration
         * file in YAML or JSON format. The translation operation retrieves the value associated with the
         * provided key from the file.
         *
         * @return The value associated with the specified key in the configuration file.
         * @throws ConfigurationException if the file extension is unsupported or if the file content
         * is not valid YAML or JSON.
         * @throws TranslationException if the key is not found in the configuration file.
         * @since 1.0.0
         */
        @OptIn(Beta::class)
        infix fun String.translatedWith(translator: Translator): String {
            if (translator.file.extension == "yaml" || translator.file.extension == "yml") {
                val yaml = tryOrThrow({ -> ConfigurationException("Not a valid YAML") }) { YAML(translator.file) }
                return yaml[this].asString() ?: throw TranslationException("Key '$this' not found in YAML file")
            }
            else if (translator.file.extension == "json") {
                val json = tryOrThrow({ -> ConfigurationException("Not a valid JSON") }) { JSON(translator.file) }
                return json[this]?.asString() ?: throw TranslationException("Key '$this' not found in JSON file")
            }
            throw ConfigurationException("Unsupported file extension: ${translator.file.extension}")
        }
        /**
         * Translates the specified key to its corresponding value by reading the content of a configuration
         * file in YAML or JSON format. The translation operation retrieves the value associated with the
         * provided key from the file.
         *
         * @return The value associated with the specified key in the configuration file.
         * @throws ConfigurationException if the file extension is unsupported or if the file content
         * is not valid YAML or JSON.
         * @throws TranslationException if the key is not found in the configuration file.
         * @since 1.0.0
         */
        @OptIn(Beta::class)
        infix fun Enum<*>.translatedWith(translator: Translator) = name.translatedWith(translator)
        /**
         * Translates a collection of keys into their corresponding values from a configuration file.
         *
         * This method processes each key in the provided iterable and retrieves the mapped value
         * from the file associated with the provided `Translator` instance. Supported configuration file
         * formats include YAML and JSON. If a key is not found, a `TranslationException` is thrown.
         *
         * @return A list of strings containing the translated values for the provided keys.
         * @throws ConfigurationException If the file format is unsupported or the configuration file is invalid.
         * @throws TranslationException If any of the keys are not found in the configuration file.
         * @since 1.0.0
         */
        @OptIn(Beta::class)
        infix fun Iterable<String>.translatedWith(translator: Translator): StringList = map { it.translatedWith(translator) }
        /**
         * Translates the specified enumeration entries to their corresponding values
         * by reading the content of a configuration file in YAML or JSON format.
         *
         * The translation operation maps each enumeration name from the provided entries
         * to its associated value retrieved from the configuration file.
         *
         * @return A list of strings containing the translated values for the provided enumeration entries.
         * @throws ConfigurationException If the file format is unsupported or the configuration file is invalid.
         * @throws TranslationException If any of the keys derived from enumeration entries are not found in the configuration file.
         * @since 1.0.0
         */
        @OptIn(Beta::class)
        infix fun EnumEntries<*>.translatedWith(translator: Translator): StringList = map { it.translatedWith(translator) }
    }

    /**
     * Translates the specified key to its corresponding value by reading the content of a configuration
     * file in YAML or JSON format. The translation operation retrieves the value associated with the
     * provided key from the file.
     *
     * @param key The key whose corresponding value is to be retrieved from the configuration file.
     * @return The value associated with the specified key in the configuration file.
     * @throws ConfigurationException if the file extension is unsupported or if the file content
     * is not valid YAML or JSON.
     * @throws TranslationException if the key is not found in the configuration file.
     * @since 1.0.0
     */
    @OptIn(Beta::class)
    infix fun translate(key: String) = key.translatedWith(this)
    /**
     * Translates the specified key to its corresponding value by reading the content of a configuration
     * file in YAML or JSON format. The translation operation retrieves the value associated with the
     * provided key from the file.
     *
     * @param key The key whose corresponding value is to be retrieved from the configuration file.
     * @return The value associated with the specified key in the configuration file.
     * @throws ConfigurationException if the file extension is unsupported or if the file content
     * is not valid YAML or JSON.
     * @throws TranslationException if the key is not found in the configuration file.
     * @since 1.0.0
     */
    @OptIn(Beta::class)
    infix fun translate(key: Enum<*>) = key.translatedWith(this)
    /**
     * Translates a collection of keys into their corresponding values from a configuration file.
     *
     * This method processes each key in the provided iterable and retrieves the mapped value
     * from the file associated with this `Translator` instance. Supported configuration file
     * formats include YAML and JSON. If a key is not found, a `NoSuchElementException` is thrown.
     *
     * @param keys An iterable collection of keys to be translated.
     * @return A list of strings containing the translated values for the provided keys.
     * @throws ConfigurationException If the file format is unsupported or the configuration file is invalid.
     * @throws TranslationException If any of the keys are not found in the configuration file.
     * @since 1.0.0
     */
    @OptIn(Beta::class)
    infix fun translate(keys: Iterable<String>): StringList = keys.map { translate(it) }
    /**
     * Translates the specified enumeration entries to their corresponding values
     * by reading the content of a configuration file in YAML or JSON format.
     *
     * The translation operation maps each enumeration name from the provided entries
     * to its associated value retrieved from the configuration file.
     *
     * @param keys The enumeration entries whose corresponding values are to be retrieved from the configuration file.
     * @return A list of strings containing the translated values for the provided enumeration entries.
     * @throws ConfigurationException If the file format is unsupported or the configuration file is invalid.
     * @throws TranslationException If any of the keys derived from enumeration entries are not found in the configuration file.
     * @since 1.0.0
     */
    @OptIn(Beta::class)
    infix fun translate(keys: EnumEntries<*>): StringList = keys.map { translate(it.name) }
}