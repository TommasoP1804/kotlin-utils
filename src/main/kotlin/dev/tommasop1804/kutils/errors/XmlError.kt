/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

interface XmlError {
    data class InvalidXsltConfig(val reason: String? = null) : XmlError {
        constructor(thorwable: Throwable) : this(thorwable.message)
    }
    data class XsltTransfomationFailed(val reason: String? = null) : XmlError {
        constructor(thorwable: Throwable) : this(thorwable.message)
    }
    data class SchemaValidationFailed(val reason: String? = null) : XmlError, ValidationError
}