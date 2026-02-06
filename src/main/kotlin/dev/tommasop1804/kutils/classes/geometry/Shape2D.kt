package dev.tommasop1804.kutils.classes.geometry

/**
 * Represents a generic interface for two-dimensional shapes.
 * Provides basic properties and operations common to 2D geometric figures,
 * such as calculating area, perimeter, and checking if a point lies within the shape.
 *
 * Implementing classes should define the specifics of these calculations
 * based on their geometric properties.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
interface Shape2D {
    /**
     * Represents the calculated area of a 2D shape.
     * The value is determined based on the specific implementation
     * of the shape that this property is a member of.
     *
     * This property is read-only.
     *
     * 
     * @since 1.0.0
     */
    val area: Double
    /**
     * Represents the total length of the boundary of a two-dimensional shape.
     * This value is specific to the implementing shape and is calculated based on its geometric properties.
     *
     * 
     * @since 1.0.0
     */
    val perimeter: Double

    /**
     * Checks whether the specified point is contained within the boundaries of this shape.
     *
     * 
     * @param point The point to be checked if it lies inside the shape.
     * @return `true` if the point is within the boundaries of the shape, otherwise `false`.
     * @since 1.0.0
     */
    operator fun contains(point: Point): Boolean
}