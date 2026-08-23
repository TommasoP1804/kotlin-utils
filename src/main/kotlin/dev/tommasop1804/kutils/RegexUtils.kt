/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("RegexUtilsKt")
@file:Suppress("unused")
@file:Since("1.0.0")
@file:MustUseReturnValues

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Creates a [Regex] instance from the provided [Code] object.
 * Ensures that the language of the code is [Language.Regex].
 *
 * @param code The [Code] object containing the regex value and its associated language.
 * @throws ValidationFailedException if the language of the provided code is not [Language.Regex].
 * @since 1.0.0
 */
infix fun Regex.Companion.fromCode(code: Code) = if (code.language != Language.Regex) throw ValidationFailedException("code must be a regex code") else Regex(code.value)

/**
 * Checks whether the given string matches the regular expression.
 *
 * @param s The string to be checked against the regular expression.
 * @return `true` if the string matches the regular expression, `false` otherwise.
 * @since 1.0.0
 */
operator fun Regex.invoke(s: CharSequence) = this matches s

/**
 * Converts the current regular expression into a `Code` object.
 *
 * This method utilizes the pattern of the regular expression and associates it
 * with the `Language.REGEX` to create an instance of the `Code` class.
 * It facilitates the representation of the regular expression pattern as a `Code` object.
 *
 * @return a `Code` object initialized with the given regular expression pattern
 *         and the `Language.REGEX`.
 * @since 1.0.0
 */
fun Regex.toCode() = Code(pattern, Language.Regex)

/**
 * Checks if the given character sequence contains a match for this regular expression.
 *
 * @param cs The character sequence to check for a match.
 * @return `true` if the character sequence contains a match, `false` otherwise.
 * @since 3.12.4
 */
operator fun Regex.contains(cs: CharSequence) = containsMatchIn(cs)

/**
 * Executes the given action if the regular expression matches the provided character sequence.
 *
 * @param cs The character sequence to be matched against the regular expression.
 * @param action The action to be executed if the regular expression matches.
 * @return The original regular expression.
 * @since 5.0.0
 */
@ExperimentalContracts
inline fun Regex.ifMatches(cs: CharSequence, action: Consumer<Regex>): Regex {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this matches cs) action(this)
    return this
}
/**
 * Executes the given [action] if the regular expression does not match the provided character sequence [cs].
 *
 * @param cs The character sequence to be checked against the regular expression.
 * @param action A lambda that will be invoked with the current regular expression if it does not match [cs].
 * @return The current regular expression instance.
 * @since 5.0.0
 */
@ExperimentalContracts
inline fun Regex.ifNotMatches(cs: CharSequence, action: Consumer<Regex>): Regex {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (!(this matches cs)) action(this)
    return this
}
/**
 * Executes the given action if the regular expression contains a match in the provided character sequence.
 *
 * @param cs The character sequence to search for matches of this regular expression.
 * @param action The action to perform with this regular expression if a match is found.
 * @return The current regular expression instance.
 * @since 5.0.0
 */
@ExperimentalContracts
inline fun Regex.ifContainsMatchIn(cs: CharSequence, action: Consumer<Regex>): Regex {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (containsMatchIn(cs)) action(this)
    return this
}
/**
 * Executes the given action on the current Regex instance if no match is found in the specified character sequence.
 *
 * @param cs The character sequence to search for matches.
 * @param action The action to invoke on the Regex instance if no matches are found in the character sequence.
 * @return The current Regex instance.
 * @since 5.0.0
 */
@ExperimentalContracts
inline fun Regex.ifNotContainsMatchIn(cs: CharSequence, action: Consumer<Regex>): Regex {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (!containsMatchIn(cs)) action(this)
    return this
}