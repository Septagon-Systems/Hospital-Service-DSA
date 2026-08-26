package edu.ug.dcit308.hospital.ds.tree;

import java.util.NoSuchElementException;

/**
 * Generic self-balancing B-Tree implementation.
 * Zero {@code java.util} collection classes are used.
 *
 * @param <K> the key type; must be {@link Comparable}
 * @param <V> the value type associated with each key
 */
public class BTree<K extends Comparable<K>, V> {

    public static final int DEFAULT_MIN_DEGREE = 2;

    static final class BTreeNode<K extends Comparable<K>, V> {
        int numKeys;
        boolean isLeaf;
        K[] keys;
        V[] values;
        BTreeNode<K, V>[] children;

        @SuppressWarnings("unchecked")
        BTreeNode(int t, boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.numKeys = 0;
            this.keys = (K[]) new Comparable[2 * t - 1];
            this.values = (V[]) new Object[2 * t - 1];
            this.children = (BTreeNode<K, V>[]) new BTreeNode[2 * t];
        }

        int findKeyIndex(K key) {
            int idx = 0;
            while (idx < numKeys && keys[idx].compareTo(key) < 0) {
                idx++;
            }
            return idx;
        }
    }

    private final int t;
    private BTreeNode<K, V> root;
    private int size;

    public BTree() {
        this(DEFAULT_MIN_DEGREE);
    }

    public BTree(int minDegree) {
        if (minDegree < 2) {
            throw new IllegalArgumentException("Minimum degree t must be >= 2, got: " + minDegree);
        }
        this.t = minDegree;
        this.root = new BTreeNode<>(t, true);
        this.size = 0;
    }

    public int getMinDegree() {
        return t;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int height() {
        if (size == 0) {
            return -1;
        }
        int h = 0;
        BTreeNode<K, V> curr = root;
        while (!curr.isLeaf) {
            curr = curr.children[0];
            h++;
        }
        return h;
    }

    public void clear() {
        this.root = new BTreeNode<>(t, true);
        this.size = 0;
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        return search(root, key);
    }

    private V search(BTreeNode<K, V> node, K key) {
        int i = node.findKeyIndex(key);
        if (i < node.numKeys && key.compareTo(node.keys[i]) == 0) {
            return node.values[i];
        }
        if (node.isLeaf) {
            return null;
        }
        return search(node.children[i], key);
    }

    public K minKey() {
        if (isEmpty()) {
            throw new NoSuchElementException("B-Tree is empty");
        }
        BTreeNode<K, V> curr = root;
        while (!curr.isLeaf) {
            curr = curr.children[0];
        }
        return curr.keys[0];
    }

    public K maxKey() {
        if (isEmpty()) {
            throw new NoSuchElementException("B-Tree is empty");
        }
        BTreeNode<K, V> curr = root;
        while (!curr.isLeaf) {
            curr = curr.children[curr.numKeys];
        }
        return curr.keys[curr.numKeys - 1];
    }

    public void insert(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }

        BTreeNode<K, V> r = root;
        if (r.numKeys == 2 * t - 1) {
            BTreeNode<K, V> s = new BTreeNode<>(t, false);
            root = s;
            s.children[0] = r;
            splitChild(s, 0, r);
            insertNonFull(s, key, value);
        } else {
            insertNonFull(r, key, value);
        }
    }

    private void splitChild(BTreeNode<K, V> parent, int childIndex, BTreeNode<K, V> fullChild) {
        BTreeNode<K, V> newSibling = new BTreeNode<>(t, fullChild.isLeaf);
        newSibling.numKeys = t - 1;

        for (int j = 0; j < t - 1; j++) {
            newSibling.keys[j] = fullChild.keys[j + t];
            newSibling.values[j] = fullChild.values[j + t];
            fullChild.keys[j + t] = null;
            fullChild.values[j + t] = null;
        }

        if (!fullChild.isLeaf) {
            for (int j = 0; j < t; j++) {
                newSibling.children[j] = fullChild.children[j + t];
                fullChild.children[j + t] = null;
            }
        }

        fullChild.numKeys = t - 1;

        for (int j = parent.numKeys; j >= childIndex + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[childIndex + 1] = newSibling;

        for (int j = parent.numKeys - 1; j >= childIndex; j--) {
            parent.keys[j + 1] = parent.keys[j];
            parent.values[j + 1] = parent.values[j];
        }

        parent.keys[childIndex] = fullChild.keys[t - 1];
        parent.values[childIndex] = fullChild.values[t - 1];
        fullChild.keys[t - 1] = null;
        fullChild.values[t - 1] = null;

        parent.numKeys++;
    }

    private void insertNonFull(BTreeNode<K, V> node, K key, V value) {
        int i = node.findKeyIndex(key);

        if (i < node.numKeys && key.compareTo(node.keys[i]) == 0) {
            node.values[i] = value;
            return;
        }

        if (node.isLeaf) {
            for (int j = node.numKeys - 1; j >= i; j--) {
                node.keys[j + 1] = node.keys[j];
                node.values[j + 1] = node.values[j];
            }
            node.keys[i] = key;
            node.values[i] = value;
            node.numKeys++;
            size++;
        } else {
            if (node.children[i].numKeys == 2 * t - 1) {
                if (key.compareTo(node.children[i].keys[t - 1]) == 0) {
                    node.children[i].values[t - 1] = value;
                    return;
                }

                splitChild(node, i, node.children[i]);
                if (key.compareTo(node.keys[i]) > 0) {
                    i++;
                } else if (key.compareTo(node.keys[i]) == 0) {
                    node.values[i] = value;
                    return;
                }
            }
            insertNonFull(node.children[i], key, value);
        }
    }

    @SuppressWarnings("unchecked")
    public Comparable<K>[] inOrder() {
        Comparable<K>[] result = (Comparable<K>[]) new Comparable[size];
        int[] index = new int[1];
        inOrderTraversal(root, result, index);
        return result;
    }

    private void inOrderTraversal(BTreeNode<K, V> node, Comparable<K>[] result, int[] index) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < node.numKeys; i++) {
            if (!node.isLeaf) {
                inOrderTraversal(node.children[i], result, index);
            }
            result[index[0]++] = (Comparable<K>) node.keys[i];
        }
        if (!node.isLeaf) {
            inOrderTraversal(node.children[node.numKeys], result, index);
        }
    }
}
