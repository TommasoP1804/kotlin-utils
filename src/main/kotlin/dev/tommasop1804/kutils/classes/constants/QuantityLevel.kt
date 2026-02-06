package dev.tommasop1804.kutils.classes.constants

/**
 * Represents different levels of quantity used to define constraints or expectations.
 *
 * This enum is typically utilized in contexts where a specific level of quantity
 * needs to be defined, such as in validations, limits, or threshold settings.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
enum class QuantityLevel {
    /**
     * Represents the minimum quantity level.
     *
     * This enumeration constant is used to specify the lowest or minimum level
     * in contexts where quantity levels are represented.
     *
     * @since 1.0.0
     */
    MIN,
    /**
     * Represents the maximum level of quantity.
     *
     * The `MAX` constant is used to define situations where the maximum permissible
     * or possible quantity level is required. This can be useful in scenarios
     * where upper bounds or limits need to be specified explicitly.
     *
     * @since 1.0.0
     */
    MAX,
    /**
     * Represents the EXACTLY quantity level within the `QuantityLevel` enumeration.
     *
     * This enum constant is used to indicate an exact or specific quantity level.
     *
     * @since 1.0.0
     */
    EXACTLY
}