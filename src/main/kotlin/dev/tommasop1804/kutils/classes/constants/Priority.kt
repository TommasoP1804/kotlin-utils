package dev.tommasop1804.kutils.classes.constants

/**
 * Represents the priority levels.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
enum class Priority {
    /**
     * Represents a low priority level in the `Priority` enum.
     *
     * This level is typically associated with tasks or situations
     * that require minimal attention or can be addressed after higher-priority items.
     *
     * @since 1.0.0
     */
    LOW,
    /**
     * Represents a medium priority level within the `Priority` enum.
     * Its default color is set to yellow, which can be used for displaying or categorizing
     * medium-priority tasks or messages visually.
     *
     * @since 1.0.0
     */
    MEDIUM,
    /**
     * Represents the high priority level, used to categorize tasks or items
     * with a significant level of importance or urgency.
     *
     * @since 1.0.0
     */
    HIGH,
    /**
     * The URGENT priority represents the highest level of urgency or importance
     * in a priority system.
     *
     * @since 1.0.0
     */
    URGENT
}