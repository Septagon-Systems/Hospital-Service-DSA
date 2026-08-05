package edu.ug.dcit308.hospital.ds.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {

    private BinarySearchTree<Integer, String> bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
    }

    // ------------------------------------------------------------------
    // Insert & search
    // ------------------------------------------------------------------

    @Test
    void testInsertAndSearch() {
        bst.insert(10, "Korle Bu");
        bst.insert(5,  "Ridge");
        bst.insert(15, "Komfo Anokye");

        assertEquals("Korle Bu",    bst.search(10));
        assertEquals("Ridge",       bst.search(5));
        assertEquals("Komfo Anokye", bst.search(15));
    }

    @Test
    void testSearchMissingKeyReturnsNull() {
        bst.insert(10, "Korle Bu");
        assertNull(bst.search(99));
    }

    @Test
    void testInsertNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> bst.insert(null, "X"));
    }

    @Test
    void testSearchNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> bst.search(null));
    }

    @Test
    void testInsertUpdatesValueForDuplicateKey() {
        bst.insert(10, "OldName");
        bst.insert(10, "NewName");

        assertEquals("NewName", bst.search(10));
        assertEquals(1, bst.size()); // no duplicate nodes
    }

    // ------------------------------------------------------------------
    // containsKey
    // ------------------------------------------------------------------

    @Test
    void testContainsKey() {
        bst.insert(20, "Tema General");
        assertTrue(bst.containsKey(20));
        assertFalse(bst.containsKey(99));
        assertFalse(bst.containsKey(null));
    }

    // ------------------------------------------------------------------
    // min / max
    // ------------------------------------------------------------------

    @Test
    void testMinAndMax() {
        bst.insert(20, "A");
        bst.insert(10, "B");
        bst.insert(30, "C");
        bst.insert(5,  "D");
        bst.insert(25, "E");

        assertEquals(5,  bst.min());
        assertEquals(30, bst.max());
    }

    @Test
    void testMinMaxOnEmptyTreeThrows() {
        assertThrows(NoSuchElementException.class, () -> bst.min());
        assertThrows(NoSuchElementException.class, () -> bst.max());
    }

    // ------------------------------------------------------------------
    // inOrder traversal
    // ------------------------------------------------------------------

    @Test
    void testInOrderReturnsSortedKeys() {
        int[] toInsert = {50, 30, 70, 20, 40, 60, 80};
        for (int k : toInsert) {
            bst.insert(k, "v" + k);
        }

        Integer[] sorted = bst.inOrder();
        assertEquals(toInsert.length, sorted.length);
        for (int i = 1; i < sorted.length; i++) {
            assertTrue(sorted[i - 1] < sorted[i],
                    "inOrder not sorted at index " + i);
        }
    }

    @Test
    void testInOrderOnEmptyTreeReturnsEmptyArray() {
        Integer[] result = bst.inOrder();
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ------------------------------------------------------------------
    // Delete – leaf node
    // ------------------------------------------------------------------

    @Test
    void testDeleteLeafNode() {
        bst.insert(10, "A");
        bst.insert(5,  "B");
        bst.insert(15, "C");

        bst.delete(5);

        assertNull(bst.search(5));
        assertFalse(bst.containsKey(5));
        assertEquals(2, bst.size());
    }

    // ------------------------------------------------------------------
    // Delete – node with one child
    // ------------------------------------------------------------------

    @Test
    void testDeleteNodeWithOneChild() {
        bst.insert(10, "A");
        bst.insert(5,  "B");
        bst.insert(3,  "C"); // left child of 5

        bst.delete(5);

        assertNull(bst.search(5));
        assertEquals("C", bst.search(3)); // 3 should now be child of 10
        assertEquals(2, bst.size());
    }

    // ------------------------------------------------------------------
    // Delete – node with two children
    // ------------------------------------------------------------------

    @Test
    void testDeleteNodeWithTwoChildren() {
        bst.insert(50, "root");
        bst.insert(30, "L");
        bst.insert(70, "R");
        bst.insert(20, "LL");
        bst.insert(40, "LR");

        bst.delete(30); // has two children

        assertNull(bst.search(30));
        assertEquals(4, bst.size());

        // Tree must still satisfy BST property (verified via inOrder sort)
        Integer[] sorted = bst.inOrder();
        for (int i = 1; i < sorted.length; i++) {
            assertTrue(sorted[i - 1] < sorted[i]);
        }
    }

    @Test
    void testDeleteMissingKeyThrows() {
        bst.insert(10, "A");
        assertThrows(NoSuchElementException.class, () -> bst.delete(99));
    }

    @Test
    void testDeleteNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> bst.delete(null));
    }

    // ------------------------------------------------------------------
    // size / isEmpty / clear
    // ------------------------------------------------------------------

    @Test
    void testSizeTracking() {
        assertEquals(0, bst.size());
        assertTrue(bst.isEmpty());

        bst.insert(1, "A");
        bst.insert(2, "B");
        assertEquals(2, bst.size());
        assertFalse(bst.isEmpty());

        bst.delete(1);
        assertEquals(1, bst.size());
    }

    @Test
    void testClearResetsTree() {
        bst.insert(1, "A");
        bst.insert(2, "B");
        bst.clear();

        assertTrue(bst.isEmpty());
        assertEquals(0, bst.size());
        assertNull(bst.search(1));
    }
}
