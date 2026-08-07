package edu.ug.dcit308.hospital.ds.tree;

import java.util.NoSuchElementException;

/**
 * Generic Binary Search Tree (BST) backed entirely by custom node objects.
 * No {@code java.util} collection classes are used.
 *
 * <p>Key constraint: {@code K} must implement {@link Comparable} so that the
 * BST ordering property can be maintained with no external comparator.</p>
 *
 * <p>Time-complexity summary (h = tree height; h = O(log n) for balanced trees,
 * O(n) worst-case for sorted input):</p>
 * <ul>
 *   <li>{@link #insert(Comparable, Object)} – O(h)</li>
 *   <li>{@link #search(Comparable)}         – O(h)</li>
 *   <li>{@link #delete(Comparable)}         – O(h)</li>
 *   <li>{@link #inOrder()}                  – O(n)</li>
 *   <li>{@link #min()} / {@link #max()}     – O(h)</li>
 *   <li>{@link #size()} / {@link #isEmpty()} / {@link #clear()} – O(1)</li>
 * </ul>
 *
 * @param <K> the key type; must be {@link Comparable}
 * @param <V> the value type stored alongside each key
 */
public class BinarySearchTree<K extends Comparable<K>, V> {

    // -------------------------------------------------------------------------
    // Inner node type
    // -------------------------------------------------------------------------

    private static final class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;

        Node(K key, V value) {
            this.key   = key;
            this.value = value;
        }
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private Node<K, V> root;
    private int size;

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Inserts a key-value pair.  If the key already exists its value is updated.
     *
     * @param key   the key (non-null)
     * @param value the value to associate with the key
     * @throws IllegalArgumentException if {@code key} is null
     */
    public void insert(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Key must not be null");
        root = insertRec(root, key, value);
    }

    /**
     * Searches for a key and returns the associated value.
     *
     * @param key the key to search for
     * @return the value mapped to {@code key}, or {@code null} if not found
     * @throws IllegalArgumentException if {@code key} is null
     */
    public V search(K key) {
        if (key == null) throw new IllegalArgumentException("Key must not be null");
        Node<K, V> node = searchRec(root, key);
        return (node == null) ? null : node.value;
    }

    /**
     * Returns {@code true} if the tree contains the specified key.
     *
     * @param key the key to test
     * @return {@code true} if the key exists in the tree
     */
    public boolean containsKey(K key) {
        if (key == null) return false;
        return searchRec(root, key) != null;
    }

    /**
     * Deletes the node with the given key.  Uses the <em>in-order successor</em>
     * (minimum of the right subtree) strategy when deleting a node with two children.
     *
     * @param key the key to delete
     * @throws NoSuchElementException   if the key is not present in the tree
     * @throws IllegalArgumentException if {@code key} is null
     */
    public void delete(K key) {
        if (key == null) throw new IllegalArgumentException("Key must not be null");
        if (!containsKey(key)) throw new NoSuchElementException("Key not found: " + key);
        root = deleteRec(root, key);
    }

    /**
     * Returns an array of keys in ascending (in-order) sequence.
     * The array has length equal to {@link #size()}.
     *
     * @return sorted array of all keys
     */
    @SuppressWarnings("unchecked")
    public K[] inOrder() {
        Comparable[] result = new Comparable[size];
        inOrderRec(root, result, new int[]{0});
        return (K[]) result;
    }

    /**
     * Returns the smallest key in the tree.
     *
     * @return minimum key
     * @throws NoSuchElementException if the tree is empty
     */
    public K min() {
        if (isEmpty()) throw new NoSuchElementException("Tree is empty");
        return minNode(root).key;
    }

    /**
     * Returns the largest key in the tree.
     *
     * @return maximum key
     * @throws NoSuchElementException if the tree is empty
     */
    public K max() {
        if (isEmpty()) throw new NoSuchElementException("Tree is empty");
        Node<K, V> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    /** Returns the number of key-value pairs stored in the tree. */
    public int size() {
        return size;
    }

    /** Returns {@code true} if the tree contains no elements. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Removes all elements from the tree. */
    public void clear() {
        root = null;
        size = 0;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private Node<K, V> insertRec(Node<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new Node<>(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left  = insertRec(node.left,  key, value);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, key, value);
        } else {
            // Key already exists – update value only (no size change).
            node.value = value;
        }
        return node;
    }

    private Node<K, V> searchRec(Node<K, V> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return searchRec(node.left,  key);
        if (cmp > 0) return searchRec(node.right, key);
        return node;
    }

    private Node<K, V> deleteRec(Node<K, V> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left  = deleteRec(node.left,  key);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, key);
        } else {
            // Found the node to delete.
            size--;
            if (node.left == null)  return node.right;
            if (node.right == null) return node.left;

            // Two children: replace with in-order successor (min of right subtree).
            Node<K, V> successor = minNode(node.right);
            node.key   = successor.key;
            node.value = successor.value;
            // Delete successor from right subtree; it was already counted once.
            size++;  // compensate: deleteRec will decrement size again.
            node.right = deleteRec(node.right, successor.key);
        }
        return node;
    }

    private Node<K, V> minNode(Node<K, V> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void inOrderRec(Node<K, V> node, Object[] result, int[] index) {
        if (node == null) return;
        inOrderRec(node.left,  result, index);
        result[index[0]++] = node.key;
        inOrderRec(node.right, result, index);
    }
}
