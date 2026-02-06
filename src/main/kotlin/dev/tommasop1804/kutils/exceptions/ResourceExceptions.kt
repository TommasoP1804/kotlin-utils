@file:Suppress("unused")

package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.EMPTY
import dev.tommasop1804.kutils.before
import dev.tommasop1804.kutils.isNotNull
import kotlin.reflect.KClass
import kotlin.uuid.ExperimentalUuidApi

/**
 * Base class for all exceptions related to resources.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@OptIn(ExperimentalUuidApi::class)
abstract class ResourceException : RuntimeException {
    val internalErrorCode: String?
        get() = message?.before(" @@@ ")?.ifBlank { null }

    constructor() : super()
    constructor(message: String?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + message)
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super(internalErrorCode?.plus(" @@@ "), cause)
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super((internalErrorCode?.plus(" @@@ ") ?: String.EMPTY) + message, cause)
}

/**
 * Represents an exception that is thrown when a resource is not found.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@OptIn(ExperimentalUuidApi::class)
open class ResourceNotFoundException : ResourceException {
    constructor() : super()
    constructor(type: KClass<*>, internalErrorCode: String? = null) : super("Resource of type `${type.simpleName}` not found.", internalErrorCode = internalErrorCode)
    constructor(id: Any, type: KClass<*>?, internalErrorCode: String? = null) : super("Resource${if (type.isNotNull()) " of type `${type.simpleName}`" else String.EMPTY} with id `$id` not found.", internalErrorCode = internalErrorCode)
    constructor(message: String?, internalErrorCode: String? = null) : super(message, internalErrorCode = internalErrorCode)
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super(message, cause, internalErrorCode = internalErrorCode)
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super(cause, internalErrorCode = internalErrorCode)
}

/**
 * Represents an exception that is thrown when a resource already exists.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@OptIn(ExperimentalUuidApi::class)
open class ResourceAlreadyExistsException : ResourceException {
    constructor() : super()
    constructor(type: KClass<*>, internalErrorCode: String? = null) : super("Resource of type `${type.simpleName}` already exists.", internalErrorCode = internalErrorCode)
    constructor(id: Any, type: KClass<*>?, internalErrorCode: String? = null) : super("Resource${if (type.isNotNull()) " of type `${type.simpleName}`" else String.EMPTY} with id `$id` already exists.", internalErrorCode = internalErrorCode)
    constructor(message: String?, internalErrorCode: String? = null) : super(message, internalErrorCode = internalErrorCode)
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super(message, cause, internalErrorCode = internalErrorCode)
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super(cause, internalErrorCode = internalErrorCode)
}

/**
 * Represents an exception that is thrown when a resource is locked, maybe
 * during an edit from some user or process, or a manual lock.
 * @since 1.0.0
 * @author
 */
@OptIn(ExperimentalUuidApi::class)
open class ResourceLockedException : ResourceException {
    constructor() : super()
    constructor(type: KClass<*>, internalErrorCode: String? = null) : super("Resource of type `${type.simpleName}` is locked.", internalErrorCode = internalErrorCode)
    constructor(id: Any, type: KClass<*>?, internalErrorCode: String? = null) : super("Resource${if (type.isNotNull()) " of type `${type.simpleName}`" else String.EMPTY} with id `$id` is locked.", internalErrorCode = internalErrorCode)
    constructor(message: String?, internalErrorCode: String? = null) : super(message, internalErrorCode = internalErrorCode)
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super(message, cause, internalErrorCode = internalErrorCode)
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super(cause, internalErrorCode = internalErrorCode)
}

/**
 * Represents an exception that is thrown when a resource cannot be accessed.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@OptIn(ExperimentalUuidApi::class)
open class ResourceUnaccessibleException : ResourceException {
    constructor() : super()
    constructor(type: KClass<*>, internalErrorCode: String? = null) : super("Resource of type `${type.simpleName}` is unaccessible.", internalErrorCode = internalErrorCode)
    constructor(id: Any, type: KClass<*>?, internalErrorCode: String? = null) : super("Resource${if (type.isNotNull()) " of type `${type.simpleName}`" else String.EMPTY} with id `$id` is unaccessible.", internalErrorCode = internalErrorCode)
    constructor(message: String?, internalErrorCode: String? = null) : super(message, internalErrorCode = internalErrorCode)
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super(message, cause, internalErrorCode = internalErrorCode)
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super(cause, internalErrorCode = internalErrorCode)
}

/**
 * Represents an exception that is thrown when a resource is not acceptable.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@OptIn(ExperimentalUuidApi::class)
open class ResourceNotAcceptableException : ResourceException {
    constructor() : super()
    constructor(type: KClass<*>, internalErrorCode: String? = null) : super("Resource of type `${type.simpleName}` is not acceptable.", internalErrorCode = internalErrorCode)
    constructor(id: Any, type: KClass<*>?, internalErrorCode: String? = null) : super("Resource${if (type.isNotNull()) " of type `${type.simpleName}`" else String.EMPTY} with id `$id` is not acceptable.", internalErrorCode = internalErrorCode)
    constructor(message: String?, internalErrorCode: String? = null) : super(message, internalErrorCode = internalErrorCode)
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super(message, cause, internalErrorCode = internalErrorCode)
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super(cause, internalErrorCode = internalErrorCode)
}

/**
 * Represents an exception that is thrown when a resource is in conflict with the current state of the resource.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@OptIn(ExperimentalUuidApi::class)
open class ResourceConflictException : ResourceException {
    constructor() : super()
    constructor(type: KClass<*>, internalErrorCode: String? = null) : super("Conflict with actual state of the resource `${type.simpleName}`.", internalErrorCode = internalErrorCode)
    constructor(id: Any, type: KClass<*>?, internalErrorCode: String? = null) : super("Edit of the resource${if (type.isNotNull()) " of type `${type.simpleName}`" else String.EMPTY} with id `$id` is in conflict with the current state of the resource.", internalErrorCode = internalErrorCode)
    constructor(message: String?, internalErrorCode: String? = null) : super(message, internalErrorCode = internalErrorCode)
    constructor(message: String?, cause: Throwable?, internalErrorCode: String? = null) : super(message, cause, internalErrorCode = internalErrorCode)
    constructor(cause: Throwable?, internalErrorCode: String? = null) : super(cause, internalErrorCode = internalErrorCode)
}