package dev.tommasop1804.kutils.classes.tree

import dev.tommasop1804.kutils.MList
import dev.tommasop1804.kutils.UUID
import dev.tommasop1804.kutils.isNotNull
import dev.tommasop1804.kutils.isNull
import java.util.*
import kotlin.reflect.KProperty

/**
 * Represents a node in a tree data structure.
 *
 * This class is designed to hold hierarchical data, where each node can have
 * any number of child nodes, a reference to its parent, and a specific value.
 * It also provides various convenience methods to manipulate the tree structure
 * and query relationships between nodes.
 *
 * @param T the type of data stored in the tree node
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "kutils_collection_declaration")
class TreeNode<T> private constructor(id: UUID, var parentId: UUID?, var value: T?, children: MList<TreeNode<T>>)/*: Serializable*/ {
    /**
     * Represents the unique identifier for the TreeNode instance.
     * This identifier helps distinguish the node within a tree structure.
     *
     * 
     * @since 1.0.0
     */
    var id: UUID = id
        private set
    /**
     * Represents the mutable list of child nodes for this `TreeNode`.
     *
     * When the variable is assigned a new value, it is converted to a mutable list.
     * If the new value is empty, the parent ID of each child node is updated to match the ID of the current `TreeNode`.
     *
     * 
     * @since 1.0.0
     */
    var children = children
        set(value) {
            field = value.toMutableList()
            if (value.isEmpty()) children.forEach { it.parentId = id }
        }
    /**
     * Indicates whether this tree node is a leaf node.
     *
     * A leaf node is defined as a node that has no children.
     *
     * 
     * @return `true` if this node has no children, `false` otherwise.
     * @since 1.0.0
     */
    val isLeaf: Boolean
        get() = children.isEmpty()
    /**
     * Indicates whether this TreeNode instance has any child nodes.
     *
     * This property evaluates to true if the `children` collection is not empty,
     * and false otherwise.
     *
     * 
     * @return true if the node has one or more children, false otherwise.
     * @since 1.0.0
     */
    val hasChildren: Boolean
        get() = children.isNotEmpty()
    /**
     * Indicates whether the current tree node has a parent node.
     *
     * 
     * @return `true` if the node has a parent (i.e., the `parentId` is not null), otherwise `false`.
     * @since 1.0.0
     */
    val hasParent: Boolean
        get() = parentId.isNotNull()
    /**
     * Indicates whether this node is the root node in the tree structure.
     * A node is considered the root if it does not have a parent, in which case its `parentId` is `null`.
     *
     * 
     * @since 1.0.0
     */
    val isRoot: Boolean
        get() = parentId.isNull()
    /**
     * Represents the total number of nodes within the tree, including the current node and all its descendants.
     *
     * The size is calculated dynamically by summing up the current node and recursively traversing
     * its child nodes to include all their sizes.
     *
     * 
     * @since 1.0.0
     */
    val size: Int
        get() {
            var size = 1
            for (child in children) {
                size += child.size
            }
            return size
        }
    /**
     * Retrieves a flattened list of all descendant nodes of the current TreeNode,
     * including the immediate children and their respective descendants.
     *
     * 
     * @return A list of TreeNode objects representing the flattened hierarchy of descendants.
     * @since 1.0.0
     */
    val flatChildren: List<TreeNode<T>>
        get() {
            val list = mutableListOf<TreeNode<T>>()
            if (hasChildren)
                for (child in children) {
                    list.add(child)
                    list.addAll(child.flatChildren)
                }
            return list.toList()
        }

    /**
     * Constructs a `TreeNode` with an optional parent ID, value, and list of children.
     * If no parent ID is provided, the node is assumed to have no parent.
     * If no value is provided, the node is initialized with a null value.
     * The list of children defaults to an empty mutable list if none are supplied.
     * A new unique ID is generated automatically for this node.
     *
     * @param parentId The unique identifier of the parent node, or null if this node has no parent.
     * @param value The value held by this node, or null if the node does not hold a value.
     * @param children The list of child nodes, defaulting to an empty mutable list if not provided.
     * @since 1.0.0
     */
    constructor(parentId: UUID? = null, value: T? = null, children: List<TreeNode<T>> = mutableListOf<TreeNode<T>>()): this(UUID(), parentId, value, children.toMutableList())

    /*companion object {
        @Serial private const val serialVersionUID = 1L
    }*/

    /**
     * Creates a new TreeNode instance by copying the current node with specified or default properties.
     *
     * @param parentId The unique identifier of the parent node. Defaults to the current node's parentId.
     * @param value The value of the node. Defaults to the current node's value.
     * @param children The list of child nodes. Defaults to the current node's children.
     * @since 1.0.0
     */
    fun copy(parentId: UUID? = this.parentId, value: T? = this.value, children: List<TreeNode<T>> = this.children) = TreeNode(parentId, value, children)

    /**
     * Converts the current `TreeNode` instance into a `Map` representation.
     *
     * The resulting map includes the following key-value pairs:
     * - `id`: The unique identifier of the node.
     * - `parentId`: The unique identifier of the parent node, if any.
     * - `value`: The value stored in the node.
     * - `children`: A collection of child nodes associated with the current node.
     *
     * This method is primarily intended for internal use and may be used to facilitate
     * serialization or data transformation of the `TreeNode` structure.
     *
     * @return A `Map` representation of the current `TreeNode` instance.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "id" to id,
        "parentId" to parentId,
        "value" to value,
        "children" to children,
    )

    /**
     * Retrieves the value associated with the given property name from the internal map.
     *
     * - `id` - TYPE: [UUID]
     * - `parentId` - TYPE: [UUID]
     * - `value`
     * - `children` - TYPE: `List<TreeNode>`
     *
     * @param thisRef The reference to the object where the property is declared. Can be null.
     * @param property The metadata for the property being accessed.
     * @return The value associated with the property name in the internal map.
     * @throws NoSuchElementException if the property name is not found
     * @throws ClassCastException if the property is cast to an uncastable type
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>): R = _toMap().getValue(property.name) as R
    
    /**
     * Adds a child node at the specified index within the current node's children list.
     * The parentId of the added child is set to the id of this node.
     *
     * 
     * @param index The position at which the child node should be inserted in the children list.
     * @param child The child TreeNode to be added.
     * @since 1.0.0
     */
    fun addChild(index: Int, child: TreeNode<T>) {
        child.parentId = id
        children.add(index, child)
    }

    /**
     * Adds a child node to the current node, establishing a parent-child relationship.
     *
     * 
     * @param child The child node to be added.
     * @since 1.0.0
     */
    fun addChild(child: TreeNode<T>) {
        child.parentId = id
        children.add(child)
    }
    
    /**
     * Removes the specified child node from the current node's children.
     * Updates the parent reference of the child node to null.
     *
     * 
     * @param child The child node to be removed from the current node's children.
     * @since 1.0.0
     */
    fun removeChild(child: TreeNode<T>) {
        child.parentId = null
        children.remove(child)
    }
    
    /**
     * Removes the child node at the specified index from the list of children.
     * Additionally, clears the parent link of the child node being removed.
     *
     * 
     * @param index The zero-based position of the child node to remove.
     * @throws IndexOutOfBoundsException If the index is out of bounds for the children list.
     * @since 1.0.0
     */
    fun removeChildAt(index: Int) {
        children[index].parentId = null
        children.removeAt(index)
    }
    
    /**
     * Checks if the given value is equal to the value of this tree node.
     *
     * 
     * @param value The value to compare against the value of this tree node.
     * @since 1.0.0
     */
    operator fun contains(value: T) = this.value == value
    
    /**
     * Checks if the specified value exists in the node's children or, optionally, includes the current node itself.
     *
     * 
     * @param value The value to search for in the children of this node.
     * @param considerNode If true, includes the current node in the search. Defaults to false.
     * @return True if the value is found in the children or, if `considerNode` is true, in the current node; false otherwise.
     * @since 1.0.0
     */
    fun childrenContains(value: T, considerNode: Boolean = false): Boolean {
        if (considerNode && this.value == value) return true
        return children.any { it.childrenContains(value, true) }
    }

    /**
     * Adds a child node to the current node, establishing a parent-child relationship.
     * Provides a shorthand operator for adding a new child node.
     *
     * @param child The child TreeNode to be added to the current node.
     * @since 1.0.0
     */
    operator fun plus(child: TreeNode<T>) = addChild(child)

    /**
     * Removes the specified child node from the current node's children using the `-` operator.
     * This method is a shorthand for calling the `removeChild` function.
     *
     * @param child The child node to be removed from the current node's children.
     * @since 1.0.0
     */
    operator fun minus(child: TreeNode<T>) = removeChild(child)

    /**
     * Retrieves the child node at the specified index from the list of children.
     *
     * @param index The zero-based index of the child node to be retrieved.
     * @return The child node at the specified index in the children list.
     * @throws IndexOutOfBoundsException If the index is out of bounds for the children list.
     * @since 1.0.0
     */
    operator fun get(index: Int) = children[index]

    /**
     * Replaces the child node at the specified index with the given child node.
     * Removes the existing child at the given index and inserts the new child at the same position.
     *
     * @param index The zero-based position of the child node to replace.
     * @param child The new child TreeNode to replace the existing child at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds for the children list.
     * @since 1.0.0
     */
    operator fun set(index: Int, child: TreeNode<T>) {
        removeChildAt(index)
        addChild(index, child)
    }

    operator fun iterator() = children.iterator()
    
    /**
     * Provides a string representation of the TreeNode instance, including its id, parentId, value, and children.
     *
     * 
     * @return A string describing the current state of the TreeNode instance.
     * @since 1.0.0
     */
    override fun toString(): String = "TreeNode(id=$id, parentId=$parentId, value=$value, children=$children)"
    
    /**
     * Compares this TreeNode instance with another object for equality.
     *
     * 
     * @param other The object to be compared with the current TreeNode instance.
     * @return `true` if the other object is a TreeNode instance and all significant properties
     *         (parentId, value, id, children) are equal; `false` otherwise.
     * @since 1.0.0
     */
    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TreeNode<*>

        if (parentId != other.parentId) return false
        if (value != other.value) return false
        if (id != other.id) return false
        if (children != other.children) return false

        return true
    }

    /**
     * Computes the hash code for the [TreeNode] instance based on its properties.
     *
     * The hash code is calculated using the `parentId`, `value`, `id`, and `children` properties.
     * The result ensures consistency with the `equals` method and can be used effectively in hash-based collections.
     *
     * 
     * @return The hash code value for the [TreeNode] instance.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = parentId?.hashCode() ?: 0
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + id.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }
    
    /**
     * Computes the depth of the current node in the given tree.
     *
     * 
     * @param tree The tree structure containing the node.
     * @return An integer representing the depth of the current node in the tree.
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    fun depth(tree: Tree<T>) = tree.getDepth(this as TreeNode<T?>)
    
    /**
     * Determines if the current node is an ancestor of the specified node.
     *
     * 
     * @param other The node to be checked if it is a descendant of the current node.
     * @return `true` if the current node is an ancestor of the specified node, `false` otherwise.
     * @since 1.0.0
     */
    infix fun isAncestorOf(other: TreeNode<T>): Boolean {
        for (child in flatChildren)
            if (child == other) return true
        return false
    }
    
    /**
     * Determines if the current node is a descendant of the specified node within the given tree.
     *
     * 
     * @param other The node that might be an ancestor of the current node.
     * @param tree The tree structure within which the relationship is evaluated.
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    fun isDescendantOf(other: TreeNode<T?>, tree: Tree<T>) = tree.checkDescendantOf(this as TreeNode<T?>, other)
    
    /**
     * Checks whether the current tree node shares the same parent as the specified node.
     *
     * 
     * @param other The tree node to compare against for a sibling relationship.
     * @return `true` if both nodes have the same parent ID, otherwise `false`.
     * @since 1.0.0
     */
    infix fun isSiblingOf(other: TreeNode<T>) = parentId == other.parentId
    
    /**
     * Determines if this node is the parent of the given node.
     *
     * 
     * @param other The TreeNode instance to check if it is a child of the current node.
     * @since 1.0.0
     */
    infix fun isParentOf(other: TreeNode<T>) = parentId == other.id
    
    /**
     * Computes the path from the current node to the root of the given tree.
     *
     * 
     * @param tree The tree structure containing the current node.
     * @return A list of nodes representing the path from the current node to the root.
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    fun pathToRoot(tree: Tree<T>) = tree.pathToRootOf(this as TreeNode<T?>)
    
    /**
     * Retrieves the path of this `TreeNode` instance from the root node within the given tree.
     *
     * 
     * @param tree The tree structure in which the root path is to be determined.
     * @since 1.0.0
     */
    @Suppress("UNCHECKED_CAST")
    fun pathFromRoot(tree: Tree<T>) = tree.pathFromRootOf(this as TreeNode<T?>)
}