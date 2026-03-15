@file:JvmName("FileUtilsKt")
@file:Suppress("unused")
@file:Since("1.0.0")


package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.web.MediaType
import dev.tommasop1804.kutils.classes.web.MimeType
import java.io.File

/**
 * Retrieves the MIME type associated with the file based on its extension.
 *
 * This property is useful for determining the content type of a file,
 * which can be used in various scenarios such as HTTP requests or file compatibility checks.
 *
 * @receiver The file for which the MIME type is being determined.
 * @return The MIME type corresponding to the file's extension, or `null` if the MIME type cannot be resolved.
 * @since 3.0.0
 */
val File.mimeType: MimeType?
    get() = MimeType.fromExtension(extension)
/**
 * Represents the media type of a file, derived from its MIME type.
 *
 * This property retrieves the MIME type of the file, if available, and converts it to a `MediaType` object.
 * If the MIME type cannot be determined, this property will return `null`.
 *
 * Commonly used for determining the type of content a file holds, which is helpful in scenarios
 * such as HTTP requests or file processing where content type information is required.
 *
 * @receiver The file whose media type is being determined.
 * @return The media type of the file as a `MediaType` object, or `null` if the MIME type is not available.
 * @since 3.0.0
 */
val File.mediaType: MediaType?
    get() = mimeType?.toMediaType()

/**
 * Converts a [File] object to a [Uri].
 *
 * This extension function transforms the file path represented 
 * by a [File] instance into a [Uri] object, enabling usage 
 * in contexts requiring URI representations.
 *
 * @return [Uri] representation of the file path.
 * @since 3.0.0
 */
fun File.toUri(): Uri = toURI()
/**
 * Converts the File instance to a URL representation.
 *
 * This method first converts the file to a URI and then converts
 * the URI to a URL.
 *
 * @return the URL representation of the file.
 * @since 3.0.0
 */
fun File.toUrl(): Url = toURI().toURL()