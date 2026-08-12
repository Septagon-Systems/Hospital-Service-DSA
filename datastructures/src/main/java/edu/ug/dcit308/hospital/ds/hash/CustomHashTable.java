package edu.ug.dcit308.hospital.ds.hash;

/**
 * Generic hash table using <em>separate chaining</em> for collision resolution.
 * All internal data structures are custom-built; no {@code java.util} collection
 * classes are used.
 *
 * <p>Capacity doubles and a full rehash is performed whenever the load factor
 * exceeds {@code 0.75}, keeping average-case performance at O(1) per operation.</p>
 *
 * <p>Time-complexity summary:</p>
 * <ul>
 *   <li>{@link #put(Object, Object)}         – O(1) amortised</li>
 *   <li>{@link #get(Object)}                 – O(1) amortised</li>
 *   <li>{@link #remove(Object)}              – O(1) amortised</li>
 *   <li>{@link #containsKey(Object)}         – O(1) amortised</li>
 *   <li>{@link #size()} / {@link #isEmpty()} / {@link #clear()} – O(1)</li>
 * </ul>
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class CustomHashTable<K, V> {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int    DEFAULT_CAPACITY   = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    // -------------------------------------------------------------------------
    // Inner chain-entry type (singly linked)
    // -------------------------------------------------------------------------

    private static final class Entry<K, V> {
        final K key;
        V       value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key   = key;
            this.value = value;
        }
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private Entry<K, V>[] buckets = new Entry[DEFAULT_CAPACITY];
    private int capacity = DEFAULT_CAPACITY;
    private int size     = 0;

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Inserts or updates a key-value mapping.
     *
     * @param key   the key (non-null)
     * @param value the value to associate with the key
     * @throws IllegalArgumentException if {@code key} is null
     */
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Key must not be null");

        // Rehash before insertion if load factor would be exceeded.
        if ((double) (size + 1) / capacity > LOAD_FACTOR_THRESHOLD) {
            rehash();
        }

        int index = bucketIndex(key);
        Entry<K, V> head = buckets[index];

        // Walk the chain; update if key already exists.
        Entry<K, V> current = head;
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // Prepend new entry to chain (O(1)).
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = head;
        buckets[index] = newEntry;
        size++;
    }

    /**
     * Returns the value mapped to {@code key}, or {@code null} if not present.
     *
     * @param key the key to look up
     * @return the associated value, or {@code null}
     * @throws IllegalArgumentException if {@code key} is null
     */
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("Key must not be null");
        Entry<K, V> entry = findEntry(key);
        return (entry == null) ? null : entry.value;
    }

    /**
     * Removes the mapping for {@code key}.
     *
     * @param key the key whose mapping should be removed
     * @return the previously associated value, or {@code null} if not found
     * @throws IllegalArgumentException if {@code key} is null
     */
    public V remove(K key) {
        if (key == null) throw new IllegalArgumentException("Key must not be null");

        int index = bucketIndex(key);
        Entry<K, V> current = buckets[index];
        Entry<K, V> prev    = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev    = current;
            current = current.next;
        }
        return null; // key not found
    }

    /**
     * Returns {@code true} if the table contains a mapping for {@code key}.
     *
     * @param key the key to test
     * @return {@code true} if the key is present
     */
    public boolean containsKey(K key) {
        if (key == null) return false;
        return findEntry(key) != null;
    }

    /** Returns the number of key-value pairs in the table. */
    public int size() {
        return size;
    }

    /** Returns {@code true} if the table contains no mappings. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Removes all mappings from the table and resets capacity to the default. */
    @SuppressWarnings("unchecked")
    public void clear() {
        capacity = DEFAULT_CAPACITY;
        buckets  = new Entry[capacity];
        size     = 0;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Maps a key to a bucket index using its {@code hashCode()}, ensuring a
     * non-negative result for any key (including those with a negative hash).
     */
    private int bucketIndex(K key) {
        return (key.hashCode() & 0x7fff_ffff) % capacity;
    }

    private Entry<K, V> findEntry(K key) {
        Entry<K, V> current = buckets[bucketIndex(key)];
        while (current != null) {
            if (current.key.equals(key)) return current;
            current = current.next;
        }
        return null;
    }

    /** Doubles capacity and redistributes all existing entries. */
    @SuppressWarnings("unchecked")
    private void rehash() {
        int newCapacity = capacity * 2;
        Entry<K, V>[] newBuckets = new Entry[newCapacity];

        for (int i = 0; i < capacity; i++) {
            Entry<K, V> current = buckets[i];
            while (current != null) {
                Entry<K, V> next  = current.next;
                int newIndex = (current.key.hashCode() & 0x7fff_ffff) % newCapacity;
                current.next = newBuckets[newIndex];
                newBuckets[newIndex] = current;
                current = next;
            }
        }

        buckets  = newBuckets;
        capacity = newCapacity;
    }
}
