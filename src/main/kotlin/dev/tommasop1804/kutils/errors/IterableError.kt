/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors

import dev.tommasop1804.kutils.exceptions.*

/**
 * Represents errors related to iterable operations.
 * This interface serves as the base for various specific iterable error types,
 * providing a structured approach to handling errors encountered during iterable-related operations.
 *
 * Subtypes of this interface define distinct error scenarios, such as invalid index operations,
 * missing results, or violations of cardinality expectations within an iterable.
 *
 * This interface follows a hierarchical structure, allowing nested and reusable types for better
 * error composition and categorization.
 *
 * @since 6.1.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
interface IterableError : Error {
    /**
     * Represents an error that occurs when an attempt is made to access an index
     * outside the bounds of a collection or an iterable structure.
     *
     * This interface serves as a marker for errors related to index-based access
     * violations in data structures and operations involving iterables.
     *
     * It is part of the `IterableError` hierarchy, allowing for structured
     * handling of different types of iterable-related errors.
     *
     * Subtypes of this interface may include specific representations of
     * index-related errors, such as cases where the index is explicitly provided
     * or where the operation inherently results in an out-of-bounds error.
     *
     * Implementations of this interface may include:
     * - Specific objects or data classes that encapsulate details about the index error.
     * - High-level marker objects for more generic error classifications.
     *
     * @see IterableError
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    sealed interface IndexOutOfBoundsError : IterableError
    /**
     * Represents an error that occurs when a specific element within an iterable structure
     * is expected to be an "inner element" but does not satisfy this requirement.
     *
     * This interface extends [IterableError], serving as a marker for errors related
     * to operations on iterable elements that contradict expectations about their
     * structural or positional context.
     *
     * Known direct implementers of this error include:
     * - [IterableError.Empty]: Indicates that the iterable is empty and hence no "inner element" exists.
     * - [IterableError.NoSuchElement]: Indicates that a specific index does not correspond to an "inner element."
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    sealed interface NotInnerElementError : IterableError
    /**
     * Represents an error that occurs when an operation involving an "inner result" within an iterable
     * structure fails or does not meet the required condition.
     *
     * This interface is part of the `IterableError` hierarchy and is specifically used to describe
     * errors related to scenarios where the expected or valid "inner result" is absent, invalid, or does
     * not conform to the expected constraints.
     *
     * Common subtypes include specific cases such as `Empty`, `NoResults`, and `TooFewResults`,
     * which indicate different reasons why the inner result operation might fail.
     *
     * @see IterableError
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    sealed interface NotInnerResultError : IterableError
    /**
     * Represents an error that occurs when the first result in an iterable context is not the expected result.
     * This error type signals that an operation requiring the first result to meet a specific condition has failed.
     *
     * As part of the IterableError hierarchy, this interface groups errors related to the validation or expectations of results
     * within an iterable data structure or process.
     *
     * Common scenarios for this error may include:
     * - Operations that mandate a specific element as the leading result.
     * - Failures due to the absence of the expected first result in a sequence.
     *
     * This type is sealed, meaning all implementations must be declared in the same module, offering a controlled extension mechanism.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    sealed interface NotFirstResultError : IterableError
    /**
     * Typealias for `NotFirstResultError` used to represent an error type.
     * This alias serves as a specific naming convention to differentiate cases
     * where the error pertains to results that are not the last in a sequence.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    typealias NotLastResultsError = NotFirstResultError
    /**
     * Represents an error that occurs when an operation expecting a sole element
     * encounters multiple elements in a collection.
     *
     * This is a specialized form of an IterableError that is used to handle cases
     * where the assumption of a single element being present in the iterable is violated.
     *
     * Implementations of this interface can provide additional context or details
     * regarding why multiple elements being present constitutes an error in the given scenario.
     *
     * @see IterableError
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    sealed interface NotOnlyElementError : IterableError
    /**
     * Represents an error that occurs when an operation expecting an iterable
     * to produce exactly one result does not fulfill such criteria. This is a
     * marker interface used to classify various specific errors related to
     * unexpected results in iterable operations.
     *
     * The following subtypes are commonly associated with this error:
     * - [IterableError.Empty]
     * - [IterableError.NoResults]
     * - [IterableError.TooManyResults]
     * - [IterableError.TooFewResults]
     *
     * This interface is part of the hierarchical structure of error types dedicated
     * to scenarios where iterable operations fail or behave contrary to expected.
     *
     * @see IterableError
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    sealed interface NotOnlyResultError : IterableError

    /**
     * Represents an error indicating that a specific element was not found.
     *
     * This class is typically used to signal that an expected item or resource
     * could not be located during an operation. It contains the element that was
     * not found.
     *
     * @property element The element that was not found. It may be null if no specific
     * information about the missing element is available.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class NotFound(val element: Any?) : IterableError {
        override val linkedException = NoSuchElementException("Element $element not found")
    }
    /**
     * Represents an object that acts as a marker for error conditions in cases where a container
     * or collection is expected to be non-empty but is found empty.
     *
     * This object implements multiple interfaces to signify its compatibility with various
     * error conditions related to collection operations or element retrieval.
     *
     * Implements:
     * - IterableError: Indicates an error related to iterable operations on collections.
     * - NotOnlyElementError: Denotes conditions where an expected sole element is absent.
     * - NotOnlyResultError: Signifies the lack of a single expected result.
     * - NotFirstResultError: Indicates that an expected first result is not present.
     * - NotInnerElementError: Represents an error when the expected inner element is missing.
     * - NotInnerResultError: Marks an error concerning the absence of inner results.
     * - IndexOutOfBoundsError: Used when an operation exceeds the bounds of a collection index.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    object Empty : IterableError, NotOnlyElementError, NotOnlyResultError, NotFirstResultError, NotInnerElementError, NotInnerResultError, IndexOutOfBoundsError {
        override val linkedException = NoSuchElementException()
    }
    /**
     * Represents an error indicating that a requested element does not exist.
     *
     * This class is primarily used to signal cases where an operation attempts
     * to access an element at a specific index, but the element is unavailable.
     *
     * @property index The index of the element that was not found.
     * @constructor Creates an instance of NoSuchElement with the specified index.
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class NoSuchElement(val index: Int) : IterableError, NotInnerElementError {
        override val linkedException = IndexOutOfBoundsException("Index $index is out of bounds")
    }
    /**
     * Represents an error case where no results are returned from an operation or query.
     * This object is used as an indication of the absence of any results matching the expected criteria.
     *
     * Implements the following interfaces:
     * - IterableError: Denotes an error in an iterable context.
     * - NotOnlyResultError: Indicates that the result is not the only one expected.
     * - NotFirstResultError: Indicates that the result is not the first one as expected.
     * - NotInnerResultError: Denotes an error in an expected nested or inner result structure.
     *
     * This object is immutable and serves as a common singleton representation for such error cases.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    object NoResults : IterableError, NotOnlyResultError, NotFirstResultError, NotInnerResultError {
        override val linkedException = NoResultsException()
    }
    /**
     * This object represents an error that occurs when there are more elements
     * present in a collection or sequence than expected. It is used to indicate
     * scenarios where the operation requires only one element, but multiple elements
     * are encountered.
     *
     * Implements [IterableError] and [NotOnlyElementError] to provide an error type
     * specific to iterable-related operations and cases where excessive elements
     * are detected.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    object TooManyElements : IterableError, NotOnlyElementError {
        override val linkedException = TooManyElementsException()
    }
    /**
     * Represents an error scenario where an operation yields too many results
     * instead of the expected single or a limited number of results.
     *
     * This object is used to signify that the result set exceeds the acceptable threshold or limit
     * that can be conveniently handled by the consuming operation or module.
     *
     * Implements `IterableError` to indicate the error's relevance in iterable operations
     * and `NotOnlyResultError` for scenarios where the expectation of a singular or limited result is violated.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    object TooManyResults : IterableError, NotOnlyResultError {
        override val linkedException = TooManyResultsException()
    }
    /**
     * Represents an error condition where there are too few results in a collection or sequence.
     *
     * This object implements the `IterableError`, `NotOnlyResultError`, and `NotInnerResultError`
     * interfaces, indicating that it can be used in scenarios where an insufficient number of
     * results leads to an error state.
     *
     * It is commonly utilized in contexts where a minimum number of results is required
     * but not met, and appropriate handling of such a condition is necessary.
     *
     * Implements:
     * - `IterableError`: Marks errors related to iterables.
     * - `NotOnlyResultError`: Indicates that the collection or sequence contains more than just the expected result.
     * - `NotInnerResultError`: Specifies that the expected result is not nested or within the proper depth.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    object TooFewResults : IterableError, NotOnlyResultError, NotInnerResultError {
        override val linkedException = TooFewResultsException()
    }
    /**
     * Represents an error that occurs when an operation attempts to access an index
     * that is outside the valid bounds of a collection or array.
     *
     * This class is commonly used to indicate situations where an invalid index,
     * either negative or exceeding the allowable range, is accessed.
     *
     * @constructor Initializes the exception with the specified out-of-bounds index.
     * @param index The index that is out of bounds. Defaults to null if not provided.
     *
     * @since 6.1.0
     * @author Tommaso Pastorelli
     */
    data class IndexOutOfBounds(val index: Int? = null) : IterableError, IndexOutOfBoundsError {
        override val linkedException = IndexOutOfBoundsException("Index $index is out of bounds")
    }
}