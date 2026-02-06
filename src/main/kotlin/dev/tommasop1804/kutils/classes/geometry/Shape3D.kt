package dev.tommasop1804.kutils.classes.geometry

/**
 * Represents a generic interface for three-dimensional shapes.
 * Provides basic properties and operations common to 3D geometric figures,
 * such as calculating area, volune, and checking if a point lies within the shape.
 *
 * Implementing classes should define the specifics of these calculations
 * based on their geometric properties.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
interface Shape3D {
    /**
     * Represents the outer surface area of a three-dimensional shape.
     * This value is specific to the implementing 3D shape and is calculated
     * based on its geometric properties.
     *
     * This property is read-only.
     *
     * 
     * @since 1.0.0
     */
    val surfaceArea: Double
    /**
     * Represents the calculated volume of a three-dimensional shape.
     * The value is determined based on the specific implementation
     * of the shape that this property is a member of.
     *
     * This property is read-only.
     *
     * 
     * @since 1.0.0
     */
    val volume: Double

    /**
     * Checks whether the specified point is contained within the boundaries of this 3D shape.
     *
     * 
     * @param point The point to be checked if it lies inside the 3D shape.
     * @return `true` if the point is within the boundaries of the shape, otherwise `false`.
     * @since 1.0.0
     */
    operator fun contains(point: Point): Boolean
}