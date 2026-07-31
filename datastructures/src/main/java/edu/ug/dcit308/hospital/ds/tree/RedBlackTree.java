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

    public Node<K, V> delete(K key) {
        if (key == null) {
            return null;
        }

        Node<K, V> current = root;
        Node<K, V> parent = null;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                parent = current;
                current = current.left;
            } else if (cmp > 0) {
                parent = current;
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
        return current;
    }

    private void fixDeletion(Node<K, V> x, Node<K, V> parent) {
        while (x != this.root && this.isBlack(x)) {
            if (x == parent.left) {
                Node<K, V> sibling = parent.right;
                if (isRed(sibling)) {
                    sibling.color = Color.BLACK;
                    parent.color = Color.RED;
                    this.leftRotate(parent);
                } else {
                    if ((sibling.left == null && sibling.right == null) ||
                            (isBlack(sibling.right) && isBlack(sibling.left))) {
                        sibling.color = Color.RED;
                        x = parent;
                        parent = x.parent;
                    } else if (isRed(sibling.left) && isBlack(sibling.right)) {
                        sibling.left.color = Color.BLACK;
                        sibling.color = Color.RED;
                        this.rightRotate(sibling);
                        sibling = parent.right;
                    }

                    if (isRed(sibling.right)) {
                        parent.color = Color.BLACK;
                        sibling.right.color = Color.BLACK;
                        this.leftRotate(parent);
                    }
                    x = root;

                }
            } else if (x == parent.right) {
                Node<K, V> sibling = parent.left;
                if (sibling.color == Color.RED) {
                    sibling.color = Color.BLACK;
                    parent.color = Color.RED;
                    this.rightRotate(parent);
                } else {
                    if ((sibling.left == null && sibling.right == null) ||
                            (isBlack(sibling.right) && isBlack(sibling.left))) {
                        sibling.color = Color.RED;
                        x = parent;
                        parent = x.parent;
                    } else if (isRed(sibling.right) && isBlack(sibling.left)) {
                        sibling.right.color = Color.BLACK;
                        sibling.color = Color.RED;
                        this.leftRotate(sibling);
                        sibling = parent.left;
                    }

                    if (isRed(sibling.left)) {
                        sibling.color = parent.color;
                        parent.color = Color.BLACK;
                        sibling.left.color = Color.BLACK;
                        this.rightRotate(parent);
                        x = root;
                    }

                }
            }
        }
        if (x != null) {
            x.color = Color.BLACK;
        }
    }


}
