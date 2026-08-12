package edu.ug.dcit308.hospital.ds.set;

import edu.ug.dcit308.hospital.ds.map.CustomMap;

/**
 * Generic set wrapper storing a collection of unique elements on top of
 * {@link CustomMap}.  Each element is stored as a key mapped to {@code true};
 * the underlying map in turn delegates to the custom hash table or binary
 * search tree structures.  No {@code java.util} collection classes are used.
 *
 * <p>Time-complexity summary (hash-backed map operations are amortised;
 * h = tree height):</p>
 * <ul>
 *   <li>{@link #add(Object)}     – O(1) amortised / O(h)</li>
 *   <li>{@link #remove(Object)}  – O(1) amortised / O(h)</li>
 *   <li>{@link #contains(Object)}– O(1) amortised / O(h)</li>
 *   <li>{@link #size()} / {@link #isEmpty()} / {@link #clear()} – O(1)</li>
 * </ul>
 *
 * @param <T> the element type
 */
public class CustomSet<T> {

    private final CustomMap<T, Boolean> map;

    /**
     * Constructs an empty set backed by a hash-based {@link CustomMap}.
     */
    public CustomSet() {
        this.map = new CustomMap<>();
    }

    /**
     * Constructs an empty set backed by a {@link CustomMap} using the given
     * storage strategy.  See {@link CustomMap.StorageStrategy}.
     *
     * @param strategy the backing storage strategy for the internal map
     * @throws IllegalArgumentException if {@code strategy} is null
     */
    public CustomSet(CustomMap.StorageStrategy strategy) {
        this.map = new CustomMap<>(strategy);
    }

    /**
     * Adds {@code element} to the set if it is not already present.
     *
     * @param element the element to add (non-null)
     * @return {@code true} if the element was added, {@code false} if it was
     *         already present
     * @throws IllegalArgumentException if {@code element} is null
     */
    public boolean add(T element) {
        if (element == null) throw new IllegalArgumentException("Element must not be null");
        if (map.containsKey(element)) {
            return false;
        }
        map.put(element, Boolean.TRUE);
        return true;
    }

    /**
     * Removes {@code element} from the set if present.
     *
     * @param element the element to remove (non-null)
     * @return {@code true} if the element was removed, {@code false} if it was
     *         not present
     * @throws IllegalArgumentException if {@code element} is null
     */
    public boolean remove(T element) {
        if (element == null) throw new IllegalArgumentException("Element must not be null");
        if (!map.containsKey(element)) {
            return false;
        }
        map.remove(element);
        return true;
    }

    /**
     * Returns {@code true} if the set contains {@code element}.
     *
     * @param element the element to test
     * @return {@code true} if the element is present
     */
    public boolean contains(T element) {
        return map.containsKey(element);
    }

    /** Returns the number of unique elements in the set. */
    public int size() {
        return map.size();
    }

    /** Returns {@code true} if the set contains no elements. */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /** Removes all elements from the set. */
    public void clear() {
        map.clear();
    }

    /**
     * Returns the backing storage strategy of the internal map.
     *
     * @return the storage strategy in use
     */
    public CustomMap.StorageStrategy storageStrategy() {
        return map.storageStrategy();
    }
}
