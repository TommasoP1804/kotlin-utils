@file:Suppress("unused")

package dev.tommasop1804.kutils.exceptions

/**
 * Exception about a feature
 *
 * @author Tommaso Pastorelli
 * @since 1.0.0
 */
abstract class FeatureException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Exception thrown when a feature is not available.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
open class FeatureNotAvailableException : FeatureException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}

/**
 * Exception thrown when a feature is not enabled.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
open class FeatureNotEnabledException : FeatureException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}