/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")

package dev.tommasop1804.kutils.errors

import dev.tommasop1804.kutils.classes.time.*
import dev.tommasop1804.kutils.exceptions.*
import java.time.temporal.Temporal

/**
 * Represents a validation error that occurs due to temporal conditions or constraints.
 *
 * This error is used to signal validation failures related to temporal data,
 * such as dates, times, or duration constraints. The `temporal` property provides
 * context about the specific temporal instance involved in the validation error,
 * and the optional `reason` property provides additional information describing
 * why the validation failed.
 *
 * @property temporal The temporal instance that caused the validation error.
 * @property reason An optional explanation or description of the failure.
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
open class TemporalError(open val temporal: Temporal, open val reason: String? = null) : ValidationError {
    override val linkedException get() = TemporalException(reason)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TemporalError

        if (temporal != other.temporal) return false
        if (reason != other.reason) return false

        return true
    }

    override fun hashCode(): Int {
        var result = temporal.hashCode()
        result = 31 * result + reason.hashCode()
        return result
    }

    override fun toString(): String {
        return "TemporalError(temporal=$temporal, reason=$reason)"
    }
}

/**
 * Represents a validation error specifically related to temporal intervals.
 *
 * This class is used to indicate issues that arise when handling temporal intervals
 * that do not conform to certain validation rules or when operations are not permitted
 * due to constraints on temporal intervals.
 *
 * @property interval The temporal interval associated with the validation error.
 * @property reason An optional explanation providing additional context for the error.
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
open class TemporalIntervalError(open val interval: TemporalInterval, open val reason: String? = null) : ValidationError {
    override val linkedException get() = TemporalException(reason)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TemporalError

        if (interval != other.temporal) return false
        if (reason != other.reason) return false

        return true
    }

    override fun hashCode(): Int {
        var result = interval.hashCode()
        result = 31 * result + reason.hashCode()
        return result
    }

    override fun toString(): String {
        return "TemporalIntervalError(interval=$interval, reason=$reason)"
    }

    /**
     * Represents a specific type of validation error that occurs when an operation or validation
     * is unsupported for temporal intervals that are repeated or recurring in nature.
     *
     * This class is used to capture and convey errors related to cases where a repeated interval
     * is encountered, but the operation or validation logic does not support such intervals.
     *
     * @property interval The temporal interval that caused this error.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class UnsupportedForRepeatedInterval(override val interval: TemporalInterval) : TemporalIntervalError(interval)
}