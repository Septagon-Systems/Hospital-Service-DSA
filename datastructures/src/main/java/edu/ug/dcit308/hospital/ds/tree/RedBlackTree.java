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
    }
}
