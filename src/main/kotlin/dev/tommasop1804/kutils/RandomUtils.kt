@file:JvmName("RandomUtilsKt")
@file:Suppress("unused", "kutils_temporal_of_as_temporal")
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.classes.time.TimeZoneDesignator
import java.time.*
import kotlin.reflect.KClass

/**
 * A constant string containing a set of characters, including uppercase and lowercase alphabets,
 * digits, and special symbols. This variable can be utilized for operations requiring a predefined
 * collection of characters such as generating random strings, creating passwords, or validating inputs.
 *
 * @since 1.0.0
 */
private const val CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[{]}|;:'\\\",<.>/?`~"

/**
 * Generates a random boolean value.
 *
 * The function produces `true` or `false` based on a random floating-point value.
 * If the generated value is less than 0.5, the function returns `true`.
 * Otherwise, it returns `false`.
 *
 * @receiver The [Boolean.Companion] object
 * @return A random boolean value, either `true` or `false`.
 * @since 1.0.0
 */
fun Boolean.Companion.random() = Math.random() < 0.5

/**
 * Generates a random character based on the specified criteria.
 *
 * @receiver The [Char.Companion] object
 * @param acceptUpperLetters If true, includes uppercase letters in the character pool. Defaults to true.
 * @param acceptLowerLetters If true, includes lowercase letters in the character pool. Defaults to true.
 * @param acceptNumber If true, includes numbers in the character pool. Defaults to true.
 * @param acceptSpecialChars If true, includes special characters in the character pool. Defaults to false.
 * @return A randomly selected character from the generated character pool.
 * @since 1.0.0
 */
fun Char.Companion.random(acceptUpperLetters: Boolean = true, acceptLowerLetters: Boolean = true, acceptNumber: Boolean = true, acceptSpecialChars: Boolean = false): Char {
    val sb = StringBuilder()
    if (acceptUpperLetters) sb.append(CHARS, 0, 26)
    if (acceptLowerLetters) sb.append(CHARS, 26, 52)
    if (acceptNumber) sb.append(CHARS, 52, 62)
    if (acceptSpecialChars) sb.append((-62)(CHARS))
    return sb.toString()[sb.indices.random()]
}

/**
 * Generates a random string of the specified length based on the provided character set options.
 *
 * @receiver The [String.Companion] object
 * @param length The length of the random string to generate.
 * @param acceptUpperLetters Whether to include uppercase letters in the character set. Defaults to true.
 * @param acceptLowerLetters Whether to include lowercase letters in the character set. Defaults to true.
 * @param acceptNumber Whether to include numbers in the character set. Defaults to true.
 * @param acceptSpecialChars Whether to include special characters in the character set. Defaults to false.
 * @return The randomly generated string based on the given parameters.
 * @since 1.0.0
 */
fun String.Companion.random(length: Int, acceptUpperLetters: Boolean = true, acceptLowerLetters: Boolean = true, acceptNumber: Boolean = true, acceptSpecialChars: Boolean = false): String {
    val sb = StringBuilder()
    for (i in 1..length) {
        sb.append(Char.random(acceptUpperLetters, acceptLowerLetters, acceptNumber, acceptSpecialChars))
    }
    return sb.toString()
}

/**
 * Generates a random `LocalDateTime` within the specified range.
 *
 * @receiver The range
 * @return A randomly generated `LocalDateTime` within the specified range.
 * @since 1.0.0
 */
fun ClosedRange<LocalDateTime>.random(): LocalDateTime =
    start.plusSeconds((Math.random() * (endInclusive.toEpochSecond(TimeZoneDesignator.systemDefault(TimeZoneDesignator.Z).offset) - start.toEpochSecond(TimeZoneDesignator.systemDefault(TimeZoneDesignator.Z).offset)) + 1).toLong())

/**
 * Generates a random `LocalDateTime` within the specified range.
 *
 * @receiver The range
 * @return A randomly generated `LocalDateTime` within the specified range.
 * @since 1.0.0
 */
fun OpenEndRange<LocalDateTime>.random(): LocalDateTime =
    start.plusSeconds((Math.random() * (endExclusive.toEpochSecond(TimeZoneDesignator.systemDefault(TimeZoneDesignator.Z).offset) - start.toEpochSecond(TimeZoneDesignator.systemDefault(TimeZoneDesignator.Z).offset))).toLong())

/**
 * Generates a random date within the specified range.
 *
 * @receiver The range
 * @return A random LocalDate value within the specified range.
 * @since 1.0.0
 */
fun ClosedRange<LocalDate>.random(): LocalDate =
    start.plusDays((Math.random() * (endInclusive.toEpochDay() - start.toEpochDay() + 1)).toLong())

/**
 * Generates a random date within the specified range.
 *
 * @receiver The range
 * @return A random LocalDate value within the specified range.
 * @since 1.0.0
 */
fun OpenEndRange<LocalDate>.random(): LocalDate =
    start.plusDays((Math.random() * (endExclusive.toEpochDay() - start.toEpochDay())).toLong())

/**
 * Generates a random YearMonth within the specified range.
 *
 * @receiver The range
 * @return a randomly generated YearMonth within the specified range
 * @since 1.0.0
 */
fun ClosedRange<YearMonth>.random(): YearMonth =
    start.plusMonths((Math.random() * (endInclusive.monthValue - start.monthValue + 1)).toLong())

/**
 * Generates a random YearMonth within the specified range.
 *
 * @receiver The range
 * @return a randomly generated YearMonth within the specified range
 * @since 1.0.0
 */
fun OpenEndRange<YearMonth>.random(): YearMonth =
    start.plusMonths((Math.random() * (endExclusive.monthValue - start.monthValue)).toLong())

/**
 * Generates a random [MonthDay] within the specified range.
 *
 * @receiver The range
 * @return a randomly selected [MonthDay] within the specified range
 * @since 1.0.0
 */
fun ClosedRange<MonthDay>.random(): MonthDay {
    val minMonth = start.monthValue
    val maxMont = endInclusive.monthValue
    val randomMonth = (minMonth..maxMont).random()
    val minDay = if (randomMonth == minMonth) start.dayOfMonth else 1
    val maxDay = if (randomMonth == maxMont) endInclusive.dayOfMonth else MonthDay.of(randomMonth, 1).atYear(0).month.length(false)
    return MonthDay.of(randomMonth, (minDay..maxDay).random())
}

/**
 * Generates a random [MonthDay] within the specified range.
 *
 * @receiver The range
 * @return a randomly selected [MonthDay] within the specified range
 * @since 1.0.0
 */
fun OpenEndRange<MonthDay>.random(): MonthDay {
    val minMonth = start.monthValue
    val maxMonth = endExclusive.monthValue
    val randomMonth = (minMonth until maxMonth).random()
    val minDay = if (randomMonth == minMonth) start.dayOfMonth else 1
    val maxDay = if (randomMonth == maxMonth) endExclusive.dayOfMonth else MonthDay.of(randomMonth, 1).atYear(0).month.length(false)
    return MonthDay.of(randomMonth, (minDay until maxDay).random())

}

/**
 * Generates a random year within the specified range.
 *
 * @receiver The range
 * @return A randomly selected year within the given range.
 * @since 1.0.0
 */
fun ClosedRange<Year>.random(): Year = Year.of((start.value..endInclusive.value).random())

/**
 * Generates a random year within the specified range.
 *
 * @receiver The range
 * @return A randomly selected year within the given range.
 * @since 1.0.0
 */
fun OpenEndRange<Year>.random(): Year = Year.of((start.value until endExclusive.value).random())

/**
 * Generates a random month within the specified range of months.
 *
 * @receiver The range
 * @return A randomly selected Month within the provided range.
 * @since 1.0.0
 */
fun ClosedRange<Month>.random(): Month = Month.of((start.value..endInclusive.value).random())

/**
 * Generates a random month within the specified range of months.
 *
 * @receiver The range
 * @return A randomly selected Month within the provided range.
 * @since 1.0.0
 */
fun OpenEndRange<Month>.random(): Month = Month.of((start.value until endExclusive.value).random())

/**
 * Generates a random day of the week within the specified range of days.
 *
 * @receiver The range
 * @return A randomly selected DayOfWeek from the specified range.
 * @since 1.0.0
 */
fun ClosedRange<DayOfWeek>.random(): DayOfWeek = DayOfWeek.of((start.value..endInclusive.value).random())

/**
 * Generates a random day of the week within the specified range of days.
 *
 * @receiver The range
 * @return A randomly selected DayOfWeek from the specified range.
 * @since 1.0.0
 */
fun OpenEndRange<DayOfWeek>.random(): DayOfWeek = DayOfWeek.of((start.value until endExclusive.value).random())

/**
 * Generates a random LocalTime within the specified range.
 *
 * @receiver The time range within which a random LocalTime is generated.
 * @return A randomly generated LocalTime within the specified range.
 * @since 1.0.0
 */
fun ClosedRange<LocalTime>.random(): LocalTime = LocalTime.ofNanoOfDay((start.toNanoOfDay()..endInclusive.toNanoOfDay()).random())

/**
 * Generates a random LocalTime within the specified range.
 *
 * @receiver The time range within which a random LocalTime is generated.
 * @return A randomly generated LocalTime within the specified range.
 * @since 1.0.0
 */
fun OpenEndRange<LocalTime>.random(): LocalTime = LocalTime.ofNanoOfDay((start.toNanoOfDay() until endExclusive.toNanoOfDay()).random())

/**
 * Returns a random value from the specified `enumClass` within the given `range`.
 *
 * @receiver The class of the enum type from which a value is to be selected.
 * @param range The range of indices to pick from. Defaults to the full range of `enumClass` constants.
 * @return A randomly selected enum constant from the specified `enumClass` within the given `range`.
 * @since 1.0.0
 */
fun <T: Enum<*>> Class<T>.random(range: IntRange = enumConstants.indices): T = enumConstants[range.random()]

/**
 * Returns a random value from the specified `enumClass` within the given `range`.
 *
 * @receiver The class of the enum type from which a value is to be selected.
 * @param range The range of indices to pick from. Defaults to the full range of `enumClass` constants.
 * @return A randomly selected enum constant from the specified `enumClass` within the given `range`.
 * @since 1.0.0
 */
fun <T: Enum<T>> KClass<T>.random(range: IntRange = 0 until java.enumConstants.size): T = java.enumConstants[range.random()]

/**
 * Returns a random entry from the map.
 *
 * @receiver the map from which a random entry will be selected.
 * @return a random entry from the map.
 * @throws NoSuchElementException if the map is empty.
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.random() = entries.random()

/**
 * Retrieves a random entry from the map or returns null if the map is empty.
 *
 * This function is an extension to the Map interface.
 *
 * @receiver the map from which a random entry is selected
 * @return a random entry from the map, or null if the map is empty
 * @since 1.0.0
 */
fun <K, V> Map<K, V>.randomOrNull() = entries.randomOrNull()

/**
 * Retrieves a random key from the map.
 *
 * @receiver the map from which a random key will be selected
 * @return a randomly selected key from the map's keys
 * @throws NoSuchElementException if the map is empty.
 * @since 1.0.0
 */
fun <K> Map<K, *>.randomKey(): K = keys.random()

/**
 * Returns a random key from the map or `null` if the map is empty.
 *
 * @receiver The map from which a random key is to be retrieved.
 * @return A random key from the map, or `null` if the map is empty.
 * @since 1.0.0
 */
fun <K> Map<K, *>.randomKeyOrNull(): K? = keys.randomOrNull()

/**
 * Returns a random value from the map or null if the map is empty.
 *
 * @receiver The map from which a random value is to be retrieved.
 * @return A random value from the map or null if the map is empty.
 * @throws NoSuchElementException if the map is empty.
 * @since 1.0.0
 */
fun <V> Map<*, V>.randomValue(): V? = values.random()

/**
 * Returns a random value from the map, or null if the map is empty.
 *
 * @receiver the map from which a random value is retrieved
 * @return a random value of type V, or null if the map is empty
 * @since 1.0.0
 */
fun <V> Map<*, V>.randomValueOrNull(): V? = values.randomOrNull()