package edu.ug.dcit308.hospital.ds.tree;

public class RedBlackTree<K extends Comparable<K>, V> {
    public enum Color {
        RED,
        BLACK
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        Color color;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        public Node(K key, V value, Color color, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
            this.left = null;
            this.right = null;
        }
    }

    private Node<K, V> root;
    private int size;

    public RedBlackTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    public void insert(K key, V value) {
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
            size++;
        } else {
            parent.right = newNode;
            size++;
        }
        this.fixInsertion(newNode);
    }

    public V search(K key) {
        if (key == null) {
            return null;
        }
        Node<K, V> current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp == 0) {
                return current.value;
            }
            if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    public boolean contains(K key) {
        return search(key) != null;
    }

    private void leftRotate(Node<K, V> x) {
        Node<K, V> y = x.right;
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
        while (k.parent != null && k.parent.color == Color.RED) {
            if (k.parent == k.parent.parent.left) {
                Node<K, V> g = k.parent.parent;
                Node<K, V> u = g.right;
                boolean isUncleRed = (u != null && u.color == Color.RED);

                if (isUncleRed) {
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
                boolean isUncleRed = (u != null && u.color == Color.RED);
                if (isUncleRed) {
                    k.parent.color = Color.BLACK;
                    u.color = Color.BLACK;
                    g.color = Color.RED;
                    k = g;
                } else {
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
}
