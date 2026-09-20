/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.errors.iterable

import dev.tommasop1804.kutils.errors.iterable.IterableErrors.*

sealed interface IterableErrors {
    sealed interface IndexOutOfBoundsErrors
    sealed interface NotInnerElementErrors
    sealed interface NotInnerResultErrors
    sealed interface NotFirstResultErrors
    typealias NotLastResultsErrors = NotFirstResultErrors
    sealed interface NotOnlyElementErrors
    sealed interface NotOnlyResultErrors
}

data class NotFound(val element: Any?) : IterableErrors
object Empty : IterableErrors, NotOnlyElementErrors, NotOnlyResultErrors, NotFirstResultErrors, NotInnerElementErrors, NotInnerResultErrors, IndexOutOfBoundsErrors
data class NoSuchElement(val index: Int) : IterableErrors, NotInnerElementErrors
object NoResults : IterableErrors, NotOnlyResultErrors, NotFirstResultErrors, NotInnerResultErrors
object TooManyElement : IterableErrors, NotOnlyElementErrors
object TooManyResults : IterableErrors, NotOnlyResultErrors
object TooFewResults : IterableErrors, NotOnlyResultErrors, NotInnerResultErrors
data class IndexOutOfBounds(val index: Int? = null) : IterableErrors, IndexOutOfBoundsErrors