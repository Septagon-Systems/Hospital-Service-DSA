package edu.ug.dcit308.hospital.ds.map;

import edu.ug.dcit308.hospital.ds.hash.CustomHashTable;
import edu.ug.dcit308.hospital.ds.tree.BinarySearchTree;

/**
 * Generic map wrapper that encapsulates the underlying hash and tree storage
 * structures implemented elsewhere in {@code edu.ug.dcit308.hospital.ds.*}.
 * No {@code java.util} collection classes are used.
 *
 * <p>Two storage strategies are supported:</p>
 * <ul>
 *   <li>{@link StorageStrategy#HASH_TABLE} (default) – O(1) amortised
 *       operations backed by {@link CustomHashTable}.</li>
 *   <li>{@link StorageStrategy#BINARY_SEARCH_TREE} – O(log n) operations
 *       backed by {@link BinarySearchTree}.  Requires keys to implement
 *       {@link Comparable}; inserting a non-{@code Comparable} key throws
 *       {@link ClassCastException}.</li>
 * </ul>
 *
 * <p>Time-complexity summary (hash operations are amortised; h = tree height):</p>
 * <ul>
 *   <li>{@link #put(Object, Object)} – O(1) amortised / O(h)</li>
 *   <li>{@link #get(Object)}         – O(1) amortised / O(h)</li>
 *   <li>{@link #remove(Object)}      – O(1) amortised / O(h)</li>
 *   <li>{@link #containsKey(Object)} – O(1) amortised / O(h)</li>
 *   <li>{@link #size()} / {@link #isEmpty()} / {@link #clear()} – O(1)</li>
 * </ul>
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class CustomMap<K, V> {

    /** Selects which custom structure backs the map. */
    public enum StorageStrategy {
        /** Backed by {@link CustomHashTable} (default). */
        HASH_TABLE,
        /** Backed by {@link BinarySearchTree}; keys must be {@link Comparable}. */
        BINARY_SEARCH_TREE
    }

    /**
     * Uniform contract over the underlying custom structures so the map can
     * switch storage strategies without duplicating delegation logic.
     */
    private interface Backend<K, V> {
        void put(K key, V value);
        V get(K key);
        V remove(K key);
        boolean containsKey(K key);
        int size();
        boolean isEmpty();
        void clear();
    }

    /**
     * Hash-based backend delegating to {@link CustomHashTable}.
     */
    private static final class HashTableBackend<K, V> implements Backend<K, V> {
        private final CustomHashTable<K, V> table = new CustomHashTable<>();

        public void put(K key, V value)            { table.put(key, value); }
        public V get(K key)                        { return table.get(key); }
        public V remove(K key)                     { return table.remove(key); }
        public boolean containsKey(K key)          { return table.containsKey(key); }
        public int size()                          { return table.size(); }
        public boolean isEmpty()                   { return table.isEmpty(); }
        public void clear()                        { table.clear(); }
    }

    /**
     * Tree-based backend delegating to {@link BinarySearchTree}.  The casts to
     * {@link Comparable} form the runtime boundary that rejects non-Comparable
     * keys with a {@link ClassCastException} before they reach the tree.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static final class BinarySearchTreeBackend<K, V> implements Backend<K, V> {
        private final BinarySearchTree tree = new BinarySearchTree<>();

        public void put(K key, V value)            { tree.insert((Comparable) key, value); }
        public V get(K key)                        { return (V) tree.search((Comparable) key); }
        public V remove(K key) {
            if (key == null) throw new IllegalArgumentException("Key must not be null");
            if (!tree.containsKey((Comparable) key)) return null;
            V value = (V) tree.search((Comparable) key);
            tree.delete((Comparable) key);
            return value;
        }
        public boolean containsKey(K key)          { return tree.containsKey((Comparable) key); }
        public int size()                          { return tree.size(); }
        public boolean isEmpty()                   { return tree.isEmpty(); }
        public void clear()                        { tree.clear(); }
    }

    private final StorageStrategy strategy;
    private final Backend<K, V> backend;

    /**
     * Constructs an empty map backed by {@link StorageStrategy#HASH_TABLE}.
     */
    public CustomMap() {
        this(StorageStrategy.HASH_TABLE);
    }

    /**
     * Constructs an empty map backed by the given storage strategy.
     *
     * @param strategy the backing storage strategy
     * @throws IllegalArgumentException if {@code strategy} is null
     */
    public CustomMap(StorageStrategy strategy) {
        if (strategy == null) throw new IllegalArgumentException("Storage strategy must not be null");
        this.strategy = strategy;
        this.backend = (strategy == StorageStrategy.BINARY_SEARCH_TREE)
                ? new BinarySearchTreeBackend<K, V>()
                : new HashTableBackend<K, V>();
    }

    /**
     * Associates {@code value} with {@code key}.  If the key already exists its
     * value is replaced (the entry count is unchanged).
     *
     * @param key   the key (non-null)
     * @param value the value to associate with the key
     * @throws IllegalArgumentException if {@code key} is null
     * @throws ClassCastException        if {@link StorageStrategy#BINARY_SEARCH_TREE}
     *                                   is used with a key that is not {@link Comparable}
     */
    public void put(K key, V value) {
        backend.put(key, value);
    }

    /**
     * Returns the value mapped to {@code key}, or {@code null} if not present.
     *
     * @param key the key to look up
     * @return the associated value, or {@code null}
     * @throws IllegalArgumentException if {@code key} is null
     */
    public V get(K key) {
        return backend.get(key);
    }

    /**
     * Removes the mapping for {@code key}.
     *
     * @param key the key whose mapping should be removed
     * @return the previously associated value, or {@code null} if not found
     * @throws IllegalArgumentException if {@code key} is null
     */
    public V remove(K key) {
        return backend.remove(key);
    }

    /**
     * Returns {@code true} if the map contains a mapping for {@code key}.
     *
     * @param key the key to test
     * @return {@code true} if the key is present
     */
    public boolean containsKey(K key) {
        return backend.containsKey(key);
    }

    /** Returns the number of key-value pairs in the map. */
    public int size() {
        return backend.size();
    }

    /** Returns {@code true} if the map contains no mappings. */
    public boolean isEmpty() {
        return backend.isEmpty();
    }

    /** Removes all mappings from the map. */
    public void clear() {
        backend.clear();
    }

    /**
     * Returns the backing storage strategy in use.
     *
     * @return the storage strategy of this map
     */
    public StorageStrategy storageStrategy() {
        return strategy;
    }
}
