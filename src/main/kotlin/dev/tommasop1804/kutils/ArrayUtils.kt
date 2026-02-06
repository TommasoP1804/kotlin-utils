@file:JvmName("ArrayUtilsKt")
@file:Since("1.0.0")
@file:Suppress("unused", "kutils_null_check")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Negates the state of the array by returning `true` if the array is either `null` or empty.
 *
 * This operator checks if the array reference is `null` or if it contains no elements.
 *
 * @return `true` if the array is `null` or empty, otherwise `false`.
 * @since 1.0.0
 */
@OptIn(ExperimentalContracts::class)
operator fun Array<*>?.not(): Boolean {
    contract {
        returns(false) implies (this@not != null)
    }
    return isNull() || isEmpty()
}