/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.constants

import org.jetbrains.exposed.v1.core.Table

/**
 * Represents the priority levels.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
@MustUseReturnValues
enum class Priority(val level: Int) {
    /**
     * Represents a low priority level in the `Priority` enum.
     *
     * This level is typically associated with tasks or situations
     * that require minimal attention or can be addressed after higher-priority items.
     *
     * @since 4.0.0
     */
    Low(1),
    /**
     * Represents a medium priority level within the `Priority` enum.
     * Its default color is set to yellow, which can be used for displaying or categorizing
     * medium-priority tasks or messages visually.
     *
     * @since 4.0.0
     */
    Medium(2),
    /**
     * Represents the high priority level, used to categorize tasks or items
     * with a significant level of importance or urgency.
     *
     * @since 4.0.0
     */
    High(3),
    /**
     * The URGENT priority represents the highest level of urgency or importance
     * in a priority system.
     *
     * @since 4.0.0
     */
    Urgent(4);

    companion object {
        /**
         * Finds a `Priority` enum constant by its corresponding level value.
         *
         * This function searches the `Priority` entries and returns the one that matches
         * the provided level. If no match is found, the function returns `null`.
         *
         * @param level The integer value representing the level of the desired `Priority` enum.
         * @return The `Priority` enum constant with the corresponding level, or `null` if no match is found.
         * @since 5.5.0
         */
        fun ofLevel(level: Int) = entries.find { it.level == level.coerceIn(1, 4) }!!

        /**
         * Maps a table column to the `Priority` enum using either the enum's name
         * or ordinal for database storage, based on the provided configuration.
         *
         * @param name The name of the table column to map to the `Priority` enum.
         * @param byName A flag indicating whether the enum should be mapped using its name.
         *               If `true`, the mapping uses the enum's name.
         *               If `false`, the mapping uses the enum's ordinal instead.
         * @since 5.5.0
         */
        fun Table.priority(name: String, byName: Boolean = true) =
            if (byName) enumerationByName<Priority>(name, 6) else enumeration<Priority>(name)
    }
}