/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.translators

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.classes.functional.*
import dev.tommasop1804.kutils.errors.*
import dev.tommasop1804.kutils.exceptions.*
import java.io.File
import java.nio.file.Path
import kotlin.enums.EnumEntries
import kotlin.reflect.typeOf

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
@MustUseReturnValues
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
         * Translates the invoking string by leveraging the provided translator instance, which reads
         * configuration data from a YAML or JSON file. The translation process attempts to map the
         * string to its corresponding value within the file's structure.
         *
         * Possible errors:
         * - [InvalidFormatOfType] - the file is not matching the expected format (YAML or JSON).
         * - [YamlError.PathNotFound] - the key specified does not exist in the file, and the file is a YAML.
         * - [JsonError.PathNotFound] - the key specified does not exist in the file, and the file is a JSON.
         *
         * @param translator The `Translator` instance that defines the configuration file and
         * handles the logic for translating the string using either YAML or JSON file formats.
         * @return An `Either` containing either the successfully translated string or an error,
         * depending on the outcome of the translation process.
         * @since 6.1.0
         */
        infix fun String.translatedWith(translator: Translator): Either<Error, String> = either {
            when (translator.file.extension) {
                "yaml", "yml" -> {
                    val yaml = tryOrRaise({ InvalidFormatOfType(translator.file, typeOf<Yaml>(), it.message) }) {
                        Yaml(translator.file)
                    }
                    yaml.getAsNode(this).asString().orRaise { YamlError.PathNotFound(this) }
                }
                "json" -> {
                    val json = tryOrRaise({ InvalidFormatOfType(translator.file, typeOf<Json>(), it.message) }) {
                        Json(translator.file)
                    }
                    json.getAsNode(this)?.asString().orRaise { JsonError.PathNotFound(this) }
                }
                else -> raise(FileError.InvalidExtension(translator.file))
            }
        }
        /**
         * Translates the invoking string by leveraging the provided translator instance, which reads
         * configuration data from a YAML or JSON file. The translation process attempts to map the
         * string to its corresponding value within the file's structure.
         *
         * Possible errors:
         * - [InvalidFormatOfType] - the file is not matching the expected format (YAML or JSON).
         * - [YamlError.PathNotFound] - the key specified does not exist in the file, and the file is a YAML.
         * - [JsonError.PathNotFound] - the key specified does not exist in the file, and the file is a JSON.
         *
         * @param translator The `Translator` instance that defines the configuration file and
         * handles the logic for translating the string using either YAML or JSON file formats.
         * @return An `Either` containing either the successfully translated string or an error,
         * depending on the outcome of the translation process.
         * @since 6.1.0
         */
        infix fun Enum<*>.translatedWith(translator: Translator) = name.translatedWith(translator)
        /**
         * Translates a collection of string keys into their corresponding values using a specified translator.
         * The translation process returns a list where each element is either a successfully translated value
         * or an error indicating why a key could not be translated.
         *
         * Possible errors for each:
         * - [InvalidFormatOfType] - the file is not matching the expected format (YAML or JSON).
         * - [YamlError.PathNotFound] - the key specified does not exist in the file, and the file is a YAML.
         * - [JsonError.PathNotFound] - the key specified does not exist in the file, and the file is a JSON.
         *
         * @param translator The translator instance used to resolve the translation for each string key in the collection.
         * @return A list of `Either` objects where each element represents either a successful translation (`String`)
         * or an error (`Error`) for the corresponding key in the original collection.
         * @since 6.1.0
         */
        infix fun Iterable<String>.translatedWith(translator: Translator): List<Either<Error, String>> = map { it.translatedWith(translator) }
        /**
         * Translates a collection of string keys into their corresponding values using a specified translator.
         * The translation process returns a list where each element is either a successfully translated value
         * or an error indicating why a key could not be translated.
         *
         * Possible errors for each:
         * - [InvalidFormatOfType] - the file is not matching the expected format (YAML or JSON).
         * - [YamlError.PathNotFound] - the key specified does not exist in the file, and the file is a YAML.
         * - [JsonError.PathNotFound] - the key specified does not exist in the file, and the file is a JSON.
         *
         * @param translator The translator instance used to resolve the translation for each string key in the collection.
         * @return A list of `Either` objects where each element represents either a successful translation (`String`)
         * or an error (`Error`) for the corresponding key in the original collection.
         * @since 6.1.0
         */
        infix fun EnumEntries<*>.translatedWith(translator: Translator): List<Either<Error, String>> = map { it.translatedWith(translator) }
    }

    /**
     * Translates the invoking string by leveraging the provided translator instance, which reads
     * configuration data from a YAML or JSON file. The translation process attempts to map the
     * string to its corresponding value within the file's structure.
     *
     * Possible errors:
     * - [InvalidFormatOfType] - the file is not matching the expected format (YAML or JSON).
     * - [YamlError.PathNotFound] - the key specified does not exist in the file, and the file is a YAML.
     * - [JsonError.PathNotFound] - the key specified does not exist in the file, and the file is a JSON.
     *
     * @param key The key whose corresponding value is to be retrieved from the configuration file.
     * @return An `Either` containing either the successfully translated string or an error,
     * depending on the outcome of the translation process.
     * @since 6.1.0
     */
    infix fun translate(key: String) = key.translatedWith(this)
    /**
     * Translates the invoking string by leveraging the provided translator instance, which reads
     * configuration data from a YAML or JSON file. The translation process attempts to map the
     * string to its corresponding value within the file's structure.
     *
     * Possible errors:
     * - [InvalidFormatOfType] - the file is not matching the expected format (YAML or JSON).
     * - [YamlError.PathNotFound] - the key specified does not exist in the file, and the file is a YAML.
     * - [JsonError.PathNotFound] - the key specified does not exist in the file, and the file is a JSON.
     *
     * @param key The key whose corresponding value is to be retrieved from the configuration file.
     * @return An `Either` containing either the successfully translated string or an error,
     * depending on the outcome of the translation process.
     * @since 6.1.0
     */
    infix fun translate(key: Enum<*>) = key.translatedWith(this)
    /**
     * Translates a collection of string keys into their corresponding values using a specified translator.
     * The translation process returns a list where each element is either a successfully translated value
     * or an error indicating why a key could not be translated.
     *
     * Possible errors for each:
     * - [InvalidFormatOfType] - the file is not matching the expected format (YAML or JSON).
     * - [YamlError.PathNotFound] - the key specified does not exist in the file, and the file is a YAML.
     * - [JsonError.PathNotFound] - the key specified does not exist in the file, and the file is a JSON.
     *
     * @param keys The collection of string keys to be translated.
     * @return A list of `Either` objects where each element represents either a successful translation (`String`)
     * or an error (`Error`) for the corresponding key in the original collection.
     * @since 6.1.0
     */
    infix fun translate(keys: Iterable<String>): List<Either<Error, String>> = keys.map { translate(it) }
    /**
     * Translates a collection of string keys into their corresponding values using a specified translator.
     * The translation process returns a list where each element is either a successfully translated value
     * or an error indicating why a key could not be translated.
     *
     * Possible errors for each:
     * - [InvalidFormatOfType] - the file is not matching the expected format (YAML or JSON).
     * - [YamlError.PathNotFound] - the key specified does not exist in the file, and the file is a YAML.
     * - [JsonError.PathNotFound] - the key specified does not exist in the file, and the file is a JSON.
     *
     * @param keys The collection of string keys to be translated.
     * @return A list of `Either` objects where each element represents either a successful translation (`String`)
     * or an error (`Error`) for the corresponding key in the original collection.
     * @since 6.1.0
     */
    infix fun translate(keys: EnumEntries<*>): List<Either<Error, String>> = keys.map { translate(it.name) }
}