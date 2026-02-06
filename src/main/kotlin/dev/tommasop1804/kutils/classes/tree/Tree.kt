package dev.tommasop1804.kutils.classes.tree

import dev.tommasop1804.kutils.*
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KProperty

/**
 * Represents a tree structure with a root, leaves, and a size property. Provides methods for
 * traversing, validating, and manipulating the tree.
 *
 * @property root The root node of the tree.
 * @property leaves A collection of all leaf nodes in the tree.
 * @property size The total number of nodes in the tree.
 * @param T The type of data stored in the tree nodes.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "kutils_drop_as_int_invoke", "kutils_collection_declaration")
class Tree<T> (var root: TreeNode<T?>): Iterable<TreeNode<T?>>/*, Serializable*/ {
    init {
        validate(checkValidIds()) { "Invalid node id" }
        validate(checkValidParentIds()) { "Invalid parent id" }
    }

    /**
     * A read-only property that retrieves all the leaf nodes of the tree.
     * Leaf nodes are defined as nodes that do not have any children.
     *
     * This property leverages the `collectLeaves` function starting from the root node
     * to gather and return all the terminal nodes of the tree as a list.
     *
     * 
     * @return A list of `TreeNode<T>` objects representing the leaf nodes in the tree.
     * @since 1.0.0
     */
    val leaves: List<TreeNode<T?>>
        get() = collectLeaves(root)

    /**
     * Represents the total number of nodes in the tree, including the root and its descendants.
     * This property delegates its value to the size of the root node.
     *
     * 
     * @since 1.0.0
     */
    val size: Int
        get() = root.size

    /*companion object {
        @Serial private const val serialVersionUID = 1L
    }*/

    /**
     * Creates a copy of the tree starting from the specified root node.
     *
     * @param root the root node from which the copy operation will start. Defaults to the current tree's root node.
     * @since 1.0.0
     */
    fun copy(root: TreeNode<T?> = this.root) = Tree(root)

    /**
     * Provides an iterator to traverse through the tree nodes in a breadth-first manner.
     *
     * 
     * @return An [Iterator] that iterates over the [TreeNode]s in the tree.
     * @since 1.0.0
     */
    override fun iterator(): Iterator<TreeNode<T?>> {
        return object : Iterator<TreeNode<T?>> {
            /**
             * A private queue used for managing tree nodes during traversal or processing.
             * Initialized with the root node of the tree.
             *
             * 
             * @since 1.0.0
             */
            private val queue: Queue<TreeNode<T?>> = LinkedList<TreeNode<T?>>().apply { add(root) }

            /**
             * Checks if there are more elements available in the queue to iterate over.
             *
             * 
             * @return `true` if there are remaining elements in the queue, `false` otherwise.
             * @since 1.0.0
             */
            override fun hasNext(): Boolean = queue.isNotEmpty()

            /**
             * Retrieves and removes the next `TreeNode` from the collection being iterated over.
             * If the current node has children, they are added to the queue to support further traversal.
             *
             * 
             * @return The next `TreeNode` in the iteration.
             * @throws NoSuchElementException if there are no elements remaining to iterate.
             * @since 1.0.0
             */
            override fun next(): TreeNode<T?> {
                if (!hasNext()) throw NoSuchElementException()
                val node = queue.poll()
                node.children.let { queue.addAll(it) }
                return node ?: throw NoSuchElementException()
            }
        }
    }


    /**
     * Validates that all node IDs in the tree are unique by verifying that the list
     * of IDs extracted through a depth-first search does not contain duplicates.
     *
     * 
     * @return A boolean value indicating whether all node IDs in the tree are unique.
     * @since 1.0.0
     */
    fun checkValidIds() = !(flatNodesDFS().mappedTo { it.id }.containsDuplicates)
    /**
     * Verifies if the parent IDs across all nodes in the tree are valid. The validity conditions are:
     * - The first node (typically the root node) must have a null parent ID.
     * - All other nodes must have non-null parent IDs.
     *
     * 
     * @return `true` if all parent IDs satisfy the specified conditions; otherwise, `false`.
     * @since 1.0.0
     */
    fun checkValidParentIds(): Boolean {
        val parentIds: List<UUID?> = flatNodesDFS().mappedTo { it.parentId }.toMutableList()
        if (parentIds.isEmpty()) return true
        if (parentIds.first().isNotNull()) return false
        return parentIds.drop(1).none { it.isNull() }
    }

    /**
     * Recursively collects all leaf nodes (nodes without children) of a tree starting from the given node.
     *
     * 
     * @param node the starting node from which to collect leaf nodes
     * @return a list of all collected leaf nodes
     * @since 1.0.0
     */
    private fun collectLeaves(node: TreeNode<T?>): List<TreeNode<T?>> {
        val leaves = mutableListOf<TreeNode<T?>>()
        if (node.children.isEmpty()) {
            leaves.add(node)
        } else {
            for (child in node.children) {
                leaves.addAll(collectLeaves(child))
            }
        }
        return leaves
    }

    /**
     * Retrieves the depth of a specified node in the tree. The depth is the number of edges from the root
     * to the given node.
     *
     * 
     * @param node The target TreeNode for which the depth is calculated.
     * @return The depth of the specified node in the tree, or -1 if the node cannot be found.
     * @since 1.0.0
     */
    fun getDepth(node: TreeNode<T?>): Int = computeDepth(root, node, 0)

    /**
     * Recursively computes the depth of the target node in the tree relative to the given current node.
     *
     * 
     * @param current The current node being processed in the tree traversal. If `null`, the method returns -1.
     * @param target The node whose depth is to be computed relative to the root of the tree.
     * @param depth The current depth during the traversal, starting from 0. This increments with each recursive call.
     * @return The depth of the target node if found, or -1 if the target is not in the subtree rooted at the current node.
     * @since 1.0.0
     */
    private fun computeDepth(current: TreeNode<T?>?, target: TreeNode<T?>, depth: Int): Int {
        if (current.isNull()) return -1
        if (current == target) return depth
        for (child in current.children) {
            val result = computeDepth(child, target, depth + 1)
            if (result != -1) return result
        }
        return -1
    }

    /**
     * Returns a string representation of the Tree object.
     *
     * 
     * @return A string in the format "Tree(rootNode)" where "rootNode" is the string representation of the root node.
     * @since 1.0.0
     */
    override fun toString() = "Tree($root)"

    /**
     * Compares this tree with another object for equality.
     *
     * 
     * @param other The object to compare with this instance.
     * @return `true` if the specified object is equal to this tree, otherwise `false`.
     * @since 1.0.0
     */
    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tree<*>

        return root == other.root
    }

    /**
     * Computes the hash code for the `Tree` instance based on its root node.
     *
     * 
     * @return The hash code of the root node of the tree.
     * @since 1.0.0
     */
    override fun hashCode() = root.hashCode()

    /**
     * Performs a breadth-first search (BFS) traversal of the tree starting from the root,
     * and returns a flat list of all nodes in the tree.
     *
     * 
     * @return A list of all nodes in the tree, traversed in BFS order.
     * @since 1.0.0
     */
    fun flatNodesBFS(): List<TreeNode<T?>> {
        val flatList = mutableListOf<TreeNode<T?>>()
        populateFlatNodeListBFS(root, flatList)
        return flatList
    }

    /**
     * Populates the given mutable list with all nodes of the tree using a Breadth-First Search (BFS) approach.
     * Nodes are added to the list in the order they are traversed.
     *
     * 
     * @param node The root node of the tree or subtree to be traversed using BFS.
     * @param flatList The mutable list where the nodes will be added as they are visited.
     * @since 1.0.0
     */
    private fun populateFlatNodeListBFS(node: TreeNode<T?>, flatList: MList<TreeNode<T?>>) {
        val queue: Queue<TreeNode<T?>> = ArrayDeque()
        queue.add(node)
        while (queue.isNotEmpty()) {
            val current = queue.remove()
            flatList.add(current)
            queue.addAll(current.children)
        }
    }

    /**
     * Traverses the tree in a depth-first search (DFS) manner and generates a flat list of all nodes.
     *
     * 
     * @return List<TreeNode<T>> A list containing all nodes within the tree in depth-first order.
     * @since 1.0.0
     */
    fun flatNodesDFS(): List<TreeNode<T?>> {
        val flatList = mutableListOf<TreeNode<T?>>()
        populateFlatNodeListDFS(root, flatList)
        return flatList
    }

    /**
     * Populates a flat list with the nodes of a tree using Depth-First Search (DFS) traversal.
     *
     * 
     * @param flatList A mutable list that will contain the flat representation of the tree nodes.
     * @since 1.0.0
     */
    private fun populateFlatNodeListDFS(node: TreeNode<T?>, flatList: MList<TreeNode<T?>>) {
        flatList.add(node)
        for (child in node.children) {
            populateFlatNodeListDFS(child, flatList)
        }
    }

    /**
     * Traverses the tree using a breadth-first search (BFS) approach and
     * collects all node values into a flat list.
     *
     * 
     * @return List<T> A list containing all node values in BFS traversal order.
     * @since 1.0.0
     */
    fun flatValuesBFS(): List<T?> {
        val flatList = mutableListOf<T?>()
        populateFlatValueListBFS(root, flatList)
        return flatList
    }

    /**
     * Populates a flat list of values from a tree structure using a breadth-first search (BFS) strategy.
     *
     * 
     * @param flatList The mutable list where values from the tree nodes will be added.
     * @since 1.0.0
     */
    private fun populateFlatValueListBFS(node: TreeNode<T?>, flatList: MList<T?>) {
        val queue: Queue<TreeNode<T?>> = ArrayDeque()
        queue.add(node)
        while (queue.isNotEmpty()) {
            val current = queue.remove()
            flatList.add(current.value)
            queue.addAll(current.children)
        }
    }

    /**
     * Retrieves a flattened list of all node values in the tree, traversed in depth-first order.
     *
     * 
     * @return A list containing all node values in the tree following depth-first traversal.
     * @since 1.0.0
     */
    fun flatValuesDFS(): List<T?> {
        val flatList = mutableListOf<T?>()
        populateFlatValueListDFS(root, flatList)
        return flatList
    }

    /**
     * Populates a given list with the values from a tree, using a depth-first search traversal.
     *
     * 
     * @param node The current node being visited during the depth-first search.
     * @param flatList A mutable list to which values from the tree nodes are added.
     * @since 1.0.0
     */
    private fun populateFlatValueListDFS(node: TreeNode<T?>, flatList: MList<T?>) {
        flatList.add(node.value)
        for (child in node.children) {
            populateFlatValueListDFS(child, flatList)
        }
    }

    /**
     * Removes a node with the specified value from the tree, if it exists.
     *
     * 
     * @param value The value of the node to be removed.
     * @return `true` if the node was successfully removed, or `false` if the node was not found.
     * @since 1.0.0
     */
    fun remove(value: T): Boolean {
        val nodeToRemove = findByValueBFS(data = value)
        return nodeToRemove?.let { remove(it) } ?: false
    }

    /**
     * Removes the specified node from the tree. If the node is found and removed,
     * the method returns true; otherwise, it returns false.
     * The root node cannot be removed and attempting to do so will result in an exception.
     *
     * 
     * @param nodeToRemove The node to be removed from the tree.
     * @return Boolean True if the node is successfully removed, false otherwise.
     * @throws UnsupportedOperationException If an attempt is made to remove the root node.
     * @since 1.0.0
     */
    fun remove(nodeToRemove: TreeNode<T?>): Boolean {
        if (nodeToRemove == root) throw UnsupportedOperationException("Can't remove the root node")
        val queue = ArrayDeque<TreeNode<T?>>()
        queue.add(root)

        while (queue.isNotEmpty()) {
            val current = queue.remove()
            if (current == nodeToRemove) {
                current.children.remove(current)
                return true
            }
            queue.addAll(current.children)
        }
        return false
    }

    /**
     * Checks whether a value exists within the tree using a breadth-first search (BFS) algorithm.
     *
     * 
     * @param value the value to search for in the tree.
     * @since 1.0.0
     */
    operator fun contains(value: T) = findByValueBFS(data = value).isNotNull()

    /**
     * Checks whether the specified TreeNode is contained within the tree.
     *
     * This function performs a breadth-first search (BFS) starting from the root node
     * to determine if the provided node exists in the tree structure.
     *
     * 
     * @param node The TreeNode to be checked for existence in the tree.
     * @return `true` if the specified node is found in the tree, `false` otherwise.
     * @since 1.0.0
     */
    operator fun contains(node: TreeNode<T?>): Boolean {
        val queue = ArrayDeque<TreeNode<T?>>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val current = queue.remove()
            if (current == node) return true
            queue.addAll(current.children)
        }
        return false
    }

    /**
     * Performs a Breadth-First Search (BFS) to locate a tree node with the specified ID.
     * Starting from the given node, the method traverses all the child nodes in a level-by-level approach
     * until the node with the specified ID is found or all nodes are visited.
     *
     * 
     * @param node The starting node for the search, default is the root node.
     * @param id The unique identifier of the node to find.
     * @return The tree node with the matching ID, or null if no such node exists in the tree.
     * @since 1.0.0
     */
    fun findByIdBFS(node: TreeNode<T?> = root, id: UUID?): TreeNode<T?>? {
        if (id.isNull()) return null
        if (node.id == id) return node

        val queue = ArrayDeque<TreeNode<T?>>()
        queue.add(node)
        while (queue.isNotEmpty()) {
            val current = queue.remove()
            for (child in current.children) {
                if (child.id == id) return child
                queue.add(child)
            }
        }
        return null
    }

    /**
     * Performs a depth-first search (DFS) to find a node with the specified ID in the tree.
     *
     * 
     * @param node The starting node for the search. Defaults to the root.
     * @param id The unique identifier of the TreeNode<T> to search for.
     * @return The TreeNode<T> with the given ID if found, or null if not found.
     * @since 1.0.0
     */
    fun findByIdDFS(node: TreeNode<T?> = root, id: UUID?): TreeNode<T?>? {
        if (node.id == id) return node

        for (child in node.children) {
            val result = findByIdDFS(child, id)
            if (result.isNotNull()) return result
        }
        return null
    }

    /**
     * Traverses the tree using Breadth-First Search (BFS) to find the node containing the specified value.
     *
     * 
     * @param node The starting node for the search, defaulting to the root node.
     * @param data The value to search for within the tree.
     * @return The node containing the specified value, or null if no such node is found.
     * @since 1.0.0
     */
    fun findByValueBFS(node: TreeNode<T?> = root, data: T): TreeNode<T?>? {
        val queue = ArrayDeque<TreeNode<T?>>()
        queue.add(node)
        while (queue.isNotEmpty()) {
            val current = queue.remove()
            if (current.value == data) return current
            queue.addAll(current.children)
        }
        return null
    }

    /**
     * Searches for a node in the tree with the specified value using a Depth-First Search (DFS) approach.
     *
     * 
     * @param node The tree node to begin the search from.
     * @param data The value to search for within the tree.
     * @return The first tree node with the specified value found during the traversal, or null if no such node exists.
     * @since 1.0.0
     */
    fun findByValueDFS(node: TreeNode<T?>, data: T): TreeNode<T?>? {
        if (node.value == data) return node
        for (child in node.children) {
            val result = findByValueDFS(child, data)
            if (result.isNotNull()) return result
        }
        return null
    }

    /**
     * Performs a breadth-first traversal starting from the given node and executes the provided block of code for each node's value.
     *
     * 
     * @param node The starting node for the traversal. Defaults to the root node of the tree.
     * @param block A lambda function to be executed on the value of each traversed node.
     * @since 1.0.0
     */
    fun traverseBFS(node: TreeNode<T?> = root, block: Consumer<T?>) {
        val queue = ArrayDeque<TreeNode<T?>>()
        queue.add(node)
        while (queue.isNotEmpty()) {
            val current = queue.remove()
            block(current.value)
            queue.addAll(current.children)
        }
    }

    /**
     * Traverses the tree using the Depth-First Search (DFS) strategy starting from the given node.
     * Applies the provided block function to the value of each visited node.
     *
     * 
     * @param node The starting node for the traversal. Defaults to the root node of the tree.
     * @param block A function to be applied to the value of each visited node.
     * @since 1.0.0
     */
    fun traverseDFS(node: TreeNode<T?> = root, block: Consumer<T?>) {
        block(node.value)
        for (child in node.children) traverseDFS(child, block)
    }

    /**
     * Reverses the order of the children of a tree node and recursively applies the same operation to its descendants.
     *
     * 
     * @param node The starting node to mirror, defaulting to the root node of the tree.
     * @since 1.0.0
     */
    fun mirror(node: TreeNode<T?> = root) {
        node.children.reverse()
        for (child in node.children) mirror(child)
    }

    /**
     * Builds a path of UUIDs from the given node to the root of the tree.
     *
     * 
     * @param node The node for which the path to the root is calculated.
     * @return A list of UUIDs representing the path from the given node to the root, starting with the given node's ID.
     * @since 1.0.0
     */
    fun pathToRootOf(node: TreeNode<T?>): List<UUID?> {
        var effNode: TreeNode<T?>? = node
        val path = mutableListOf<UUID?>()
        var id: UUID? = node.id
        while (effNode.isNotNull()) {
            path.add(id)
            id = effNode.parentId
            effNode = findByIdBFS(id = id)
        }
        return path
    }

    /**
     * Constructs the path from the root node to the specified node in the tree.
     * The path is represented as a list of UUIDs, starting from the root and ending at the specified node.
     *
     * 
     * @param node The node for which the path from the root is to be determined.
     * @since 1.0.0
     */
    fun pathFromRootOf(node: TreeNode<T?>) = !pathToRootOf(node)

    /**
     * Retrieves the siblings of the specified node. Siblings are nodes that share the same parent
     * but are not the node itself.
     *
     * 
     * @param node The node whose siblings are to be found.
     * @return A list of sibling nodes, or an empty list if the node has no parent.
     * @since 1.0.0
     */
    fun siblingsOf(node: TreeNode<T?>): List<TreeNode<T?>> {
        if (node.parentId.isNull()) return emptyList()
        return (findByIdBFS(id = node.parentId) ?: throw IllegalStateException("Parent node not found")).children.filter { it.id != node.id }
    }

    /**
     * Checks if the given node is an ancestor of another node.
     *
     * 
     * @param other The node to check if it is a descendant of the receiver.
     * @return `true` if the receiver is an ancestor of the given node, otherwise `false`.
     * @since 1.0.0
     */
    fun checkAncestorOf(node: TreeNode<T?>, other: TreeNode<T?>): Boolean = node.isAncestorOf(other)

    /**
     * Checks if the specified `node` is a descendant of the given `other` node.
     *
     * 
     * @param node The node to check as a potential descendant.
     * @param other The node to check as the potential ancestor.
     * @return `true` if `node` is a descendant of `other`, otherwise `false`.
     * @since 1.0.0
     */
    fun checkDescendantOf(node: TreeNode<T?>, other: TreeNode<T?>): Boolean = other.isAncestorOf(node)

    /**
     * Checks if the tree structure starting from the given node is balanced. A tree is considered balanced
     * if the difference between the maximum and minimum depth of its subtrees is less than or equal to the specified limit.
     *
     * 
     * @param node The root node of the subtree to check for balance. Defaults to the root node of the tree.
     * @param limit The maximum allowed difference between the heights of the deepest and shallowest subtrees. Defaults to 1.
     * @return True if the subtree is balanced according to the specified limit, false otherwise.
     * @since 1.0.0
     */
    fun isBalanced(node: TreeNode<T?> = root, limit: Int = 1): Boolean {
        var maxHeight = 0
        var minHeight = Int.MAX_VALUE

        for (child in node.children) {
            val childHeight = getDepth(child)
            maxHeight = max(maxHeight, childHeight)
            minHeight = min(minHeight, childHeight)
            if (maxHeight - minHeight > limit) return false
        }
        if (maxHeight - minHeight > limit) return true
        for (child in node.children) {
            if (!isBalanced(child, limit)) return false
        }
        return true
    }

    /**
     * Converts the object to a map representation with predefined keys.
     *
     * @return a map with keys "root" and "leaves" mapped to their respective properties.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _toMap() = mapOf(
        "root" to root,
        "leaves" to leaves
    )

    /**
     * Retrieves the value associated with the specified property name from the map.
     *
     * - `root` - TYPE: [TreeNode]
     * - `leaves` - TYPE: `List<TreeNode>`
     *
     * @param thisRef The reference to the object using the property. Can be null.
     * @param property The property metadata of the requested value.
     * @return The value associated with the property name, cast to the specified type R.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
}