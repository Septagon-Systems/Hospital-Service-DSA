package edu.ug.dcit308.hospital.ds.tree;

/**
 * A self-balancing Red-Black Tree implementation for O(log n) key-value lookup.
 * Keeps the tree from leaning like the Tower of Pisa during sequential inserts.
 *
 * @param <K> key type (must implement Comparable)
 * @param <V> value payload type
 */
public class RedBlackTree<K extends Comparable<K>, V> {

    /**
     * Node color representation enforcing Red-Black invariants.
     */
    public enum Color {
        RED,
        BLACK
    }

    /**
     * Internal tree node holding key, value, color, and structural pointers.
     *
     * @param <K> key type
     * @param <V> value type
     */
    public static class Node<K, V> {
        private K key;
        private V value;
        Color color;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        /**
         * Constructs a new node with a parent reference.
         *
         * @param key    sorting key
         * @param value  associated data payload
         * @param color  initial node color
         * @param parent reference to parent node
         */
        public Node(K key, V value, Color color, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
            this.left = null;
            this.right = null;
        }

        /**
         * @return the node's key
         */
        public K getKey() { return key; }

        /**
         * @return the node's stored value
         */
        public V getValue() { return value; }

        /**
         * @return the node's current color
         */
        public Color getColor() { return color; }
    }

    private Node<K, V> root;
    private int size;

    /**
     * Initializes an empty Red-Black Tree.
     */
    public RedBlackTree() {
        root = null;
        size = 0;
    }

    /**
     * Checks if the tree has no elements.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the total number of key-value pairs stored.
     *
     * @return current tree size
     */
    public int getSize() {
        return size;
    }

    /**
     * Inserts a key-value pair and rebalances the tree if necessary.
     * Updates the value in-place if the key already exists.
     * Duplicates not allowed
     *
     * @param key   non-null sorting key
     * @param value data payload to store
     */
    public void insert(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        if (root == null) {
            root = new Node<>(key, value, Color.BLACK, null);
            this.fixInsertion(root);
            size++;
            return;
        }
        Node<K, V> current = root;
        Node<K, V> parent = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                current.value = value;
                return;
            } else if (cmp < 0) {
                parent = current;
                current = current.left;
            } else {
                parent = current;
                current = current.right;
            }
        }
        Node<K, V> newNode = new Node<>(key, value, Color.RED, parent);
        if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        size++;
        this.fixInsertion(newNode);
    }

    /**
     * Searches for the value associated with a given key in O(log n) time.
     *
     * @param key key to locate
     * @return stored value if found, or null if it ghosted us
     */
    public V search(K key) {
        Node<K, V> node = findNode(key);
        return (node != null) ? node.value : null;
    }

    private Node<K, V> findNode(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current;
            }
            current = (cmp < 0) ? current.left : current.right;
        }
        return null;
    }

    /**
     * Checks if a key exists in the tree.
     *
     * @param key key to check
     * @return true if key exists, false otherwise
     */
    public boolean contains(K key) {
        return findNode(key) != null;
    }

    private void leftRotate(Node<K, V> x) {
        Node<K, V> y = x.right;
        if (y == null) return;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x.parent.left == x) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node<K, V> x) {
        Node<K, V> y = x.left;
        if (y == null) return;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x.parent.left == x) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.right = x;
        x.parent = y;
    }

    private void fixInsertion(Node<K, V> k) {
        while (isRed(k.parent)) {
            if (k.parent == k.parent.parent.left) {
                Node<K, V> g = k.parent.parent;
                Node<K, V> u = g.right;

                if (isRed(u)) {
                    // Red uncle...
                    k.parent.color = Color.BLACK;
                    u.color = Color.BLACK;
                    g.color = Color.RED;
                    k = g;
                } else {
                    if (k == k.parent.right) {
                        // Black Uncle, right parent node
                        k = k.parent;
                        this.leftRotate(k);
                    }
                    // Black uncle, left parent node
                    k.parent.color = Color.BLACK;
                    g.color = Color.RED;
                    this.rightRotate(g);
                }
            } else if (k.parent == k.parent.parent.right) {
                Node<K, V> g = k.parent.parent;
                Node<K, V> u = g.left;
                if (isRed(u)) {
                    k.parent.color = Color.BLACK;
                    u.color = Color.BLACK;
                    g.color = Color.RED;
                    k = g;
                } else {
                    // we rotate right to get the line form before rotating again
                    // to get the correct in order
                    if (k == k.parent.left) {
                        k = k.parent;
                        this.rightRotate(k);
                    }
                    k.parent.color = Color.BLACK;
                    g.color = Color.RED;
                    this.leftRotate(g);
                }
            }
        }
        root.color = Color.BLACK;
    }

    // Adding these here so I don't have to be checking for null every 5 lines!
    // And stop java from yapping about it
    private boolean isBlack(Node<K, V> node) {
        return node == null || node.color == Color.BLACK;
    }

    private boolean isRed(Node<K, V> node) {
        return node != null && node.color == Color.RED;
    }

    private Node<K, V> max(Node<K, V> x) {
        if (x.right != null) {
            return max(x.right);
        }
        return x;
    }

    private Node<K, V> min(Node<K, V> x) {
        if (x.left != null) {
            return min(x.left);
        }
        return x;
    }

    /**
     * Removes a key and its value from the tree, triggering fixDeletion if a black node is unlinked.
     *
     * @param key key of the node to remove
     * @return deleted node reference, or null if key was not found
     */
    public Node<K, V> delete(K key) {
        if (key == null || root == null) {
            return null;
        }

        Node<K, V> current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                Node<K, V> targetNode = current;
                Node<K, V> nodeToSplice;

                // Both nodes are filled
                if (targetNode.left != null && targetNode.right != null) {
                    nodeToSplice = this.min(targetNode.right);
                    targetNode.key = nodeToSplice.key;
                    targetNode.value = nodeToSplice.value;
                } else {
                    nodeToSplice = targetNode;
                }

                Color removedColor = nodeToSplice.color;
                Node<K, V> childNode = (nodeToSplice.left != null) ? nodeToSplice.left : nodeToSplice.right;
                Node<K, V> spliceNodeParent = nodeToSplice.parent;

                if (childNode != null) {
                    childNode.parent = spliceNodeParent;
                }

                // If parent ended up being null, then it means it's the root
                // so we make the child new root
                if (spliceNodeParent == null) {
                    root = childNode;
                } else if (nodeToSplice == spliceNodeParent.left) {
                    spliceNodeParent.left = childNode;
                } else {
                    spliceNodeParent.right = childNode;
                }
                size--;
                if (removedColor == Color.BLACK) {
                    this.fixDeletion(childNode, spliceNodeParent);
                }
                return targetNode;
            }
        }
        return null;
    }

    private void fixDeletion(Node<K, V> x, Node<K, V> parent) {
        while (x != this.root && isBlack(x)) {
            if (parent == null) break;

            if (x == parent.left) {
                Node<K, V> sibling = parent.right;
                if (isRed(sibling)) {
                    sibling.color = Color.BLACK;
                    parent.color = Color.RED;
                    this.leftRotate(parent);
                    sibling = parent.right;
                }

                if (sibling == null || (isBlack(sibling.left) && isBlack(sibling.right))) {
                    if (sibling != null) sibling.color = Color.RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (isRed(sibling.left) && isBlack(sibling.right)) {
                        sibling.left.color = Color.BLACK;
                        sibling.color = Color.RED;
                        this.rightRotate(sibling);
                        sibling = parent.right;
                    }

                    if (sibling != null) {
                        sibling.color = parent.color;
                        parent.color = Color.BLACK;
                        if (sibling.right != null) sibling.right.color = Color.BLACK;
                        this.leftRotate(parent);
                    }
                    x = root;
                }
            } else {
                Node<K, V> sibling = parent.left;
                if (isRed(sibling)) {
                    sibling.color = Color.BLACK;
                    parent.color = Color.RED;
                    this.rightRotate(parent);
                    sibling = parent.left;
                }

                if (sibling == null || (isBlack(sibling.left) && isBlack(sibling.right))) {
                    if (sibling != null) sibling.color = Color.RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (isRed(sibling.right) && isBlack(sibling.left)) {
                        sibling.right.color = Color.BLACK;
                        sibling.color = Color.RED;
                        this.leftRotate(sibling);
                        sibling = parent.left;
                    }

                    if (sibling != null) {
                        sibling.color = parent.color;
                        parent.color = Color.BLACK;
                        if (sibling.left != null) sibling.left.color = Color.BLACK;
                        this.rightRotate(parent);
                    }
                    x = root;
                }
            }
        }
        if (x != null) {
            x.color = Color.BLACK;
        }
    }
}
