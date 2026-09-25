/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

import dev.tommasop1804.kutils.exceptions.*

/**
 * Represents errors related to XML processing and transformations.
 * This interface serves as the base for defining various types of XML-specific errors,
 * providing a structured framework for handling XML-related issues such as invalid configurations,
 * transformation failures, schema validation errors, and missing paths.
 *
 * Each implementing class encapsulates the specific context and details of the error scenario.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
interface XmlError : Error {
    /**
     * Represents an error indicating an invalid configuration for XSLT processing.
     *
     * This class provides details about issues encountered while handling XSLT configurations.
     * The `reason` parameter specifies an optional message describing the nature of the invalid
     * configuration. This can be initialized directly or constructed using a `Throwable` to
     * derive the cause's message as the reason.
     *
     * @property reason An optional message describing why the XSLT configuration is considered invalid.
     * If null, no specific details about the invalid configuration are provided.
     *
     * @constructor Creates an instance with a specific reason.
     * @constructor Creates an instance with a reason derived from a `Throwable`'s message.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class InvalidXsltConfig(val reason: String? = null) : XmlError {
        override val linkedException = IllegalStateException("Invalid XSLT config: $reason")

        /**
         * Secondary constructor for the InvalidXsltConfig class, allowing initialization using a Throwable instance.
         * Extracts the throwable message and delegates to the primary constructor that accepts a reason string.
         *
         * @param thorwable The throwable instance whose message will be used as the reason for the error.
         * @since 6.1.0
         */
        constructor(thorwable: Throwable) : this(thorwable.message)
    }
    /**
     * Represents an error that occurs during the execution of an XSLT transformation.
     *
     * This data class is used to provide detailed information about failures encountered
     * while applying an XSLT transformation to an XML document. It encapsulates an optional
     * reason message detailing the cause of the failure.
     *
     * @property reason An optional string describing the reason for the XSLT transformation failure.
     * Defaults to null if no specific reason is provided.
     *
     * @constructor Creates an instance of `XsltTransfomationFailed` with a reason message.
     *
     * @constructor Creates an instance of `XsltTransfomationFailed` based on a throwable,
     * using its message to populate the reason property.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class XsltTransfomationFailed(val reason: String? = null) : XmlError {
        override val linkedException = IllegalStateException("XSLT transformation failed: $reason")

        /**
         * Secondary constructor for the `XsltTransfomationFailed` class, allowing initialization using a `Throwable` instance.
         * This constructor extracts the message from the provided throwable and passes it to the primary constructor.
         *
         * @param thorwable The throwable object whose message will be used as the reason for this error.
         * @since 6.1.0
         */
        constructor(thorwable: Throwable) : this(thorwable.message)
    }
    /**
     * Represents an error that occurs when schema validation fails.
     *
     * This data class is a specific type of error that implements both the [XmlError] and
     * [ValidationError] interfaces, providing structured representation and categorization
     * of schema validation issues. The `reason` property contains an optional message describing
     * the cause of the validation failure.
     *
     * Schema validation failures typically arise from discrepancies between an XML document and
     * its corresponding schema definition, such as missing required elements, unexpected data
     * types, or invalid structural configurations.
     *
     * @property reason An optional string providing details about the cause of the schema validation failure.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class SchemaValidationFailed(val reason: String? = null) : XmlError, ValidationError {
        override val linkedException = XmlSchemaValidationException(reason)
    }
    /**
     * Represents an error occurring when a specific XML path is not found.
     *
     * This data class is a specific implementation of the [XmlError] interface. It provides
     * detailed information about a scenario where an expected path in an XML structure
     * cannot be located. The `path` property specifies the missing XML path as a string.
     *
     * @property path The XML path that could not be found.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class PathNotFound(val path: String) : XmlError {
        override val linkedException = NoSuchYamlPathException(path)
    }
}