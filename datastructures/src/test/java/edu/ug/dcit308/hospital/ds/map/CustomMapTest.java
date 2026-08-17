package edu.ug.dcit308.hospital.ds.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomMapTest {

    private CustomMap<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new CustomMap<>();
    }

    // ------------------------------------------------------------------
    // put & get
    // ------------------------------------------------------------------

    @Test
    void testPutAndGet() {
        map.put("Korle Bu", 100);
        map.put("Ridge",    200);

        assertEquals(100, map.get("Korle Bu"));
        assertEquals(200, map.get("Ridge"));
    }

    @Test
    void testGetMissingKeyReturnsNull() {
        map.put("Korle Bu", 100);
        assertNull(map.get("NoSuchHospital"));
    }

    @Test
    void testPutNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> map.put(null, 1));
    }

    @Test
    void testGetNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> map.get(null));
    }

    @Test
    void testPutOverwritesExistingKey() {
        map.put("Korle Bu", 100);
        map.put("Korle Bu", 999);

        assertEquals(999, map.get("Korle Bu"));
        assertEquals(1, map.size()); // still only one entry
    }

    // ------------------------------------------------------------------
    // containsKey
    // ------------------------------------------------------------------

    @Test
    void testContainsKey() {
        map.put("Tema General", 50);

        assertTrue(map.containsKey("Tema General"));
        assertFalse(map.containsKey("Komfo Anokye"));
        assertFalse(map.containsKey(null));
    }

    // ------------------------------------------------------------------
    // remove
    // ------------------------------------------------------------------

    @Test
    void testRemoveExistingKey() {
        map.put("Ridge", 200);
        int removed = map.remove("Ridge");

        assertEquals(200, removed);
        assertNull(map.get("Ridge"));
        assertFalse(map.containsKey("Ridge"));
        assertEquals(0, map.size());
    }

    @Test
    void testRemoveMissingKeyReturnsNull() {
        assertNull(map.remove("NoSuchKey"));
    }

    @Test
    void testRemoveNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> map.remove(null));
    }

    // ------------------------------------------------------------------
    // size / isEmpty / clear
    // ------------------------------------------------------------------

    @Test
    void testSizeAndIsEmpty() {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());

        map.put("A", 1);
        map.put("B", 2);

        assertFalse(map.isEmpty());
        assertEquals(2, map.size());
    }

    @Test
    void testClearResetsMap() {
        map.put("Korle Bu", 100);
        map.put("Ridge",    200);
        map.clear();

        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("Korle Bu"));
    }

    @Test
    void testPutAfterClear() {
        map.put("Korle Bu", 100);
        map.clear();
        map.put("Ridge", 50);

        assertEquals(50, map.get("Ridge"));
        assertEquals(1, map.size());
    }

    // ------------------------------------------------------------------
    // Storage strategy selection
    // ------------------------------------------------------------------

    @Test
    void testDefaultStrategyIsHashTable() {
        assertEquals(CustomMap.StorageStrategy.HASH_TABLE, map.storageStrategy());
    }

    @Test
    void testNullStrategyThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new CustomMap<>(null));
    }

    // ------------------------------------------------------------------
    // Rehashing (load factor) under hash table strategy
    // ------------------------------------------------------------------

    @Test
    void testRehashPreservesData() {
        // Default capacity is 16; load factor 0.75 triggers rehash at 13 entries.
        for (int i = 0; i < 100; i++) {
            map.put("hospital_" + i, i * 10);
        }

        assertEquals(100, map.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i * 10, map.get("hospital_" + i));
        }
    }

    @Test
    void testLargeScaleInsertionAndRemoval() {
        for (int i = 0; i < 20_000; i++) {
            map.put("bed_" + i, i);
        }
        assertEquals(20_000, map.size());

        for (int i = 0; i < 20_000; i += 2) {
            assertEquals(i, map.remove("bed_" + i));
        }
        assertEquals(10_000, map.size());
        assertFalse(map.containsKey("bed_0"));
        assertTrue(map.containsKey("bed_1"));
    }

    // ------------------------------------------------------------------
    // Binary search tree strategy
    // ------------------------------------------------------------------

    @Test
    void testBstStrategyBasicOperations() {
        CustomMap<Integer, String> bstMap =
                new CustomMap<>(CustomMap.StorageStrategy.BINARY_SEARCH_TREE);

        assertEquals(CustomMap.StorageStrategy.BINARY_SEARCH_TREE, bstMap.storageStrategy());
        assertTrue(bstMap.isEmpty());

        bstMap.put(30, "Ridge");
        bstMap.put(10, "Korle Bu");
        bstMap.put(50, "Tema General");

        assertEquals(3, bstMap.size());
        assertEquals("Korle Bu",     bstMap.get(10));
        assertEquals("Ridge",        bstMap.get(30));
        assertEquals("Tema General", bstMap.get(50));
        assertTrue(bstMap.containsKey(50));
        assertFalse(bstMap.containsKey(99));
        assertFalse(bstMap.containsKey(null));
    }

    @Test
    void testBstStrategyOverwriteDoesNotGrowSize() {
        CustomMap<Integer, String> bstMap =
                new CustomMap<>(CustomMap.StorageStrategy.BINARY_SEARCH_TREE);

        bstMap.put(10, "OldName");
        bstMap.put(10, "NewName");

        assertEquals("NewName", bstMap.get(10));
        assertEquals(1, bstMap.size());
    }

    @Test
    void testBstStrategyRemove() {
        CustomMap<Integer, String> bstMap =
                new CustomMap<>(CustomMap.StorageStrategy.BINARY_SEARCH_TREE);

        bstMap.put(10, "Korle Bu");
        bstMap.put(20, "Ridge");

        assertEquals("Korle Bu", bstMap.remove(10));
        assertNull(bstMap.get(10));
        assertNull(bstMap.remove(99)); // missing key
        assertEquals(1, bstMap.size());
    }

    @Test
    void testBstStrategyNullKeyThrows() {
        CustomMap<Integer, String> bstMap =
                new CustomMap<>(CustomMap.StorageStrategy.BINARY_SEARCH_TREE);

        assertThrows(IllegalArgumentException.class, () -> bstMap.put(null, "X"));
        assertThrows(IllegalArgumentException.class, () -> bstMap.get(null));
        assertThrows(IllegalArgumentException.class, () -> bstMap.remove(null));
    }

    @Test
    void testBstStrategyClear() {
        CustomMap<Integer, String> bstMap =
                new CustomMap<>(CustomMap.StorageStrategy.BINARY_SEARCH_TREE);

        bstMap.put(10, "A");
        bstMap.put(20, "B");
        bstMap.clear();

        assertTrue(bstMap.isEmpty());
        assertEquals(0, bstMap.size());
        assertNull(bstMap.get(10));
    }

    @Test
    void testBstStrategyLargeScale() {
        CustomMap<Integer, String> bstMap =
                new CustomMap<>(CustomMap.StorageStrategy.BINARY_SEARCH_TREE);

        for (int i = 0; i < 1_000; i++) {
            bstMap.put(i, "v" + i);
        }
        assertEquals(1_000, bstMap.size());
        assertEquals("v500", bstMap.get(500));
        assertEquals("v0",   bstMap.get(0));
        assertEquals("v999", bstMap.get(999));

        bstMap.remove(500);
        assertNull(bstMap.get(500));
        assertEquals(999, bstMap.size());
    }

    @Test
    void testBstStrategyRejectsNonComparableKeys() {
        CustomMap<NonComparable, String> bstMap =
                new CustomMap<>(CustomMap.StorageStrategy.BINARY_SEARCH_TREE);

        // The Comparable boundary check rejects non-Comparable keys immediately.
        assertThrows(ClassCastException.class,
                () -> bstMap.put(new NonComparable("Korle Bu"), "A"));
    }

    /**
     * Key type deliberately lacking {@link Comparable} to verify the
     * {@code BINARY_SEARCH_TREE} strategy's runtime key requirement.
     */
    private static final class NonComparable {
        private final String name;

        NonComparable(String name) {
            this.name = name;
        }
    }
}
