/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

import kotlin.reflect.KType

/**
 * Represents an error occurring during the deserialization process.
 *
 * This class serves as a base for various types of deserialization errors, encapsulating
 * information about the target class that failed to deserialize and an optional reason
 * describing the issue. It can be extended to provide more specific deserialization error types.
 *
 * @property targetType The class that failed deserialization.
 * @property reason The optional reason describing why the deserialization failed.
 * @author Tommaso Pastorelli
 * @since 6.1.0
 */
@Suppress("unused")
open class DeserializationError(open val targetType: KType, open val reason: String? = null) : Error {
    /**
     * Secondary constructor for the `DeserializationError` class.
     *
     * This constructor allows initializing a `DeserializationError` using a target class and a throwable.
     *
     * @param targetType The Kotlin class associated with the deserialization error.
     * @param reason The throwable that caused the error. Its message will be used as the reason.
     * @since 6.1.0
     */
    constructor(targetType: KType, reason: Throwable) : this(targetType, reason.message)

    /**
     * Represents an error that occurs during the deserialization process when
     * a read operation on a specific target class fails.
     *
     * @property targetType The target class involved in the failed deserialization operation.
     * @property reason An optional message or explanation for why the read operation failed.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class ReadError(override val targetType: KType, override val reason: String? = null) : DeserializationError(targetType, reason) {
        /**
         * Secondary constructor for creating a [ReadError] instance using a [Throwable].
         * The throwable's message will be used as the reason.
         *
         * @param targetType The target class where the deserialization error occurred.
         * @param reason The throwable that caused the deserialization error.
         * @since 6.1.0
         */
        constructor(targetType: KType, reason: Throwable) : this(targetType, reason.message)
    }
    /**
     * Represents an error encountered during the mapping process when attempting to deserialize data into a specific target class.
     *
     * This error is a subtype of [DeserializationError] and signifies situations where the data could not be correctly mapped
     * to the provided target class during deserialization.
     *
     * @property targetType The target class into which deserialization was attempted.
     * @property reason A textual description of the reason for the error, or null if unspecified.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class MappingError(override val targetType: KType, override val reason: String? = null) : DeserializationError(targetType, reason) {
        /**
         * Secondary constructor for the MappingError class that allows creating an instance
         * using a target class and a throwable as the reason.
         *
         * @param targetType The Kotlin class for which the error occurred.
         * @param reason The throwable representing the cause of the error.
         * @since 6.1.0
         */
        constructor(targetType:KType, reason: Throwable) : this(targetType, reason.message)
    }

    open operator fun component1() = targetType
    open operator fun component2() = reason

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeserializationError

        if (targetType != other.targetType) return false
        if (reason != other.reason) return false

        return true
    }

    override fun hashCode(): Int {
        var result = targetType.hashCode()
        result = 31 * result + reason.hashCode()
        return result
    }

    override fun toString(): String {
        return "DeserializationError(targetClass=$targetType, reason=$reason)"
    }
}