@file:JvmName("ProgressionUtilsKt")
@file:Suppress("unused", "kutils_take_as_int_invoke", "kutils_drop_as_int_invoke")
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since

/**
 * Invokes the Int as a function to process an `IntProgression` by either taking or dropping elements
 * depending on the value of the invoking integer.
 *
 * @param progression an instance of `IntProgression` that specifies the sequence of integers to be processed
 * @return an `IntList` containing the modified sequence based on the invoking integer
 * @since 1.0.0
 */
operator fun Int.invoke(progression: IntProgression): IntList {
    return if (this > 0) progression.take(this)
    else progression.drop(-this)
}

/**
 * Invokes the function on a given character progression, either taking or dropping
 * a specified number of elements from the progression based on the receiver integer value.
 *
 * @param progression the CharProgression on which the operation will be applied
 * @return a CharList containing the result of the operation
 * @since 1.0.0
 */
operator fun Int.invoke(progression: CharProgression): CharList {
    return if (this > 0) progression.take(this)
    else progression.drop(-this)
}

/**
 * Invokes an operation on an [Int] receiver to either take or drop a specified number of elements
 * from the given [LongProgression], based on the receiver's value.
 *
 * @param progression the [LongProgression] from which elements are taken or dropped.
 * @return a [LongList] containing the resulting elements after taking or dropping.
 * @since 1.0.0
 */
operator fun Int.invoke(progression: LongProgression): LongList {
    return if (this > 0) progression.take(this)
    else progression.drop(-this)
}

/**
 * Invokes the specified operation where a progression is either taken or dropped based on the value of the integer.
 *
 * If the integer is positive, the function takes the first 'this' elements from the progression.
 * If the integer is negative, the function drops the first '-this' elements from the progression.
 *
 * @param progression the progression to take or drop elements from
 * @return a list of unsigned integers resulting from taking or dropping elements in the progression
 * @since 1.0.0
 */
operator fun Int.invoke(progression: UIntProgression): List<UInt> {
    return if (this > 0) progression.take(this)
    else progression.drop(-this)
}

/**
 * Invokes the progression based on the integer value.
 * If the integer is positive, it takes the specified number of elements from the progression.
 * If the integer is negative, it skips the specified number of elements from the progression.
 *
 * @param progression the progression of unsigned long values to be processed
 * @return a list of unsigned long values either taken or skipped based on the integer value
 * @since 1.0.0
 */
operator fun Int.invoke(progression: ULongProgression): List<ULong> {
    return if (this > 0) progression.take(this)
    else progression.drop(-this)
}