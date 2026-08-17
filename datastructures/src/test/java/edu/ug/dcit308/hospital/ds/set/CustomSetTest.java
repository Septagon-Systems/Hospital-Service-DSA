package edu.ug.dcit308.hospital.ds.set;

import edu.ug.dcit308.hospital.ds.map.CustomMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomSetTest {

    private CustomSet<String> set;

    @BeforeEach
    void setUp() {
        set = new CustomSet<>();
    }

    // ------------------------------------------------------------------
    // add
    // ------------------------------------------------------------------

    @Test
    void testAddNewElementReturnsTrue() {
        assertTrue(set.add("Korle Bu"));
        assertTrue(set.add("Ridge"));
        assertEquals(2, set.size());
    }

    @Test
    void testAddDuplicateReturnsFalseAndKeepsSize() {
        assertTrue(set.add("Korle Bu"));
        assertFalse(set.add("Korle Bu"));

        assertEquals(1, set.size());
    }

    @Test
    void testAddNullElementThrows() {
        assertThrows(IllegalArgumentException.class, () -> set.add(null));
    }

    // ------------------------------------------------------------------
    // contains
    // ------------------------------------------------------------------

    @Test
    void testContains() {
        set.add("Tema General");

        assertTrue(set.contains("Tema General"));
        assertFalse(set.contains("Komfo Anokye"));
        assertFalse(set.contains(null));
    }

    @Test
    void testContainsAfterAddAndRemove() {
        set.add("Ridge");
        assertTrue(set.contains("Ridge"));

        set.remove("Ridge");
        assertFalse(set.contains("Ridge"));
    }

    // ------------------------------------------------------------------
    // remove
    // ------------------------------------------------------------------

    @Test
    void testRemoveExistingElementReturnsTrue() {
        set.add("Ridge");
        assertTrue(set.remove("Ridge"));

        assertFalse(set.contains("Ridge"));
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    void testRemoveMissingElementReturnsFalse() {
        assertFalse(set.remove("NoSuchElement"));
    }

    @Test
    void testRemoveNullElementThrows() {
        assertThrows(IllegalArgumentException.class, () -> set.remove(null));
    }

    // ------------------------------------------------------------------
    // Uniqueness invariant
    // ------------------------------------------------------------------

    @Test
    void testSetGuaranteesUniqueElements() {
        set.add("Korle Bu");
        set.add("Korle Bu");
        set.add("Ridge");
        set.add("Korle Bu");

        assertEquals(2, set.size());
        assertTrue(set.contains("Korle Bu"));
        assertTrue(set.contains("Ridge"));
    }

    // ------------------------------------------------------------------
    // size / isEmpty / clear
    // ------------------------------------------------------------------

    @Test
    void testSizeAndIsEmpty() {
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());

        set.add("A");
        set.add("B");

        assertFalse(set.isEmpty());
        assertEquals(2, set.size());
    }

    @Test
    void testClearResetsSet() {
        set.add("Korle Bu");
        set.add("Ridge");
        set.clear();

        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertFalse(set.contains("Korle Bu"));
    }

    @Test
    void testAddAfterClear() {
        set.add("Korle Bu");
        set.clear();
        set.add("Ridge");

        assertTrue(set.contains("Ridge"));
        assertFalse(set.contains("Korle Bu"));
        assertEquals(1, set.size());
    }

    // ------------------------------------------------------------------
    // Large scale (boundary)
    // ------------------------------------------------------------------

    @Test
    void testLargeScaleInsertion() {
        for (int i = 0; i < 20_000; i++) {
            assertTrue(set.add("ward_" + i));
        }
        assertEquals(20_000, set.size());

        for (int i = 0; i < 20_000; i += 2) {
            assertTrue(set.remove("ward_" + i));
        }
        assertEquals(10_000, set.size());
        assertFalse(set.contains("ward_0"));
        assertTrue(set.contains("ward_1"));
    }

    // ------------------------------------------------------------------
    // Binary search tree backing strategy
    // ------------------------------------------------------------------

    @Test
    void testBstBackedSetOperations() {
        CustomSet<Integer> bstSet =
                new CustomSet<>(CustomMap.StorageStrategy.BINARY_SEARCH_TREE);

        assertEquals(CustomMap.StorageStrategy.BINARY_SEARCH_TREE, bstSet.storageStrategy());
        assertTrue(bstSet.isEmpty());

        assertTrue(bstSet.add(10));
        assertTrue(bstSet.add(20));
        assertFalse(bstSet.add(10)); // duplicate rejected

        assertEquals(2, bstSet.size());
        assertTrue(bstSet.contains(10));
        assertTrue(bstSet.contains(20));
        assertFalse(bstSet.contains(30));

        assertTrue(bstSet.remove(20));
        assertFalse(bstSet.contains(20));
        assertEquals(1, bstSet.size());
    }

    @Test
    void testBstBackedSetLargeScale() {
        CustomSet<Integer> bstSet =
                new CustomSet<>(CustomMap.StorageStrategy.BINARY_SEARCH_TREE);

        for (int i = 0; i < 1_000; i++) {
            bstSet.add(i);
        }
        assertEquals(1_000, bstSet.size());
        assertTrue(bstSet.contains(500));

        for (int i = 0; i < 1_000; i += 2) {
            bstSet.remove(i);
        }
        assertEquals(500, bstSet.size());
    }

    @Test
    void testNullStorageStrategyThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new CustomSet<>(null));
    }

    @Test
    void testDefaultStrategyIsHashTable() {
        assertEquals(CustomMap.StorageStrategy.HASH_TABLE, set.storageStrategy());
    }
}
