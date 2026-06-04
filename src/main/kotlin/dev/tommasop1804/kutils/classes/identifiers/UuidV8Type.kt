/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.identifiers

/**
 * Represents the different types of UUIDv8 generation types.
 * Each type defines a distinct way to construct and format UUIDs based on string, timestamp,
 * and random components.
 *
 * @since 3.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
@MustUseReturnValues
enum class UuidV8Type(subvariant: Int) {
    /**
     * Represents a UUIDv8 type that encodes data using a single string format.
     * Use this type when the value should be stored or represented as a single string.
     *
     * @since 3.0.0
     */
    STRING(0),
    /**
     * Represents a UUIDv8 type that combines a timestamp with a string value.
     *
     * This type is used to generate UUIDs that embed a timestamp alongside
     * a unique string to ensure temporal and unique identification within the system.
     *
     * @since 3.0.0
     */
    TS_STRING(1),
    /**
     * Represents a UUIDv8 type that combines a string and random elements.
     *
     * This type is designed to generate unique identifiers consisting of a string
     * component concatenated with a random sequence of characters, ensuring a high degree
     * of randomness and uniqueness.
     *
     * Suitable for use cases where a hybrid of predefined string data and randomness
     * is needed for identifier generation.
     *
     * @since 3.0.0
     */
    STRING_RANDOM(2),
    /**
     * Represents a UUID type indicating a pair of string components as its format.
     * This type is used to define a UUIDv8 configuration where two string values
     * are combined to generate a UUID.
     *
     * @since 3.0.0
     */
    STRING_STRING(0),
    /**
     * Represents a UUID version 8 type where the structure consists of a timestamp and two string components.
     * This type is typically used when a hybrid identifier combining both time-based data
     * and user-defined or context-specific string data is required.
     *
     * @since 3.0.0
     */
    TS_STRING_STRING(1),
    /**
     * Represents a UUIDv8 type that combines two string components with a random element.
     * This type is used to generate a UUID that consists of two string fields separated
     * by a random identifier segment.
     *
     * This enum value is part of the `UUIDv8Type` classification.
     *
     * @since 3.0.0
     */
    STRING_STRING_RANDOM(2)
}