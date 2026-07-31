package edu.ug.dcit308.hospital.ds.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Red-Black Tree Comprehensive Data Structure Tests")
class RedBlackTreeTest {

    private RedBlackTree<Integer, String> tree;

    @BeforeEach
    void setUp() {
        tree = new RedBlackTree<>();
    }

    @Test
    @DisplayName("Normal Case: Insert and retrieve items successfully")
    void testInsertAndSearchNormalCase() {
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.getSize());

        tree.insert(10, "Korle Bu Hospital");
        tree.insert(20, "Ridge Hospital");
        tree.insert(5, "KATH Kumasi");

        assertEquals(3, tree.getSize());
        assertFalse(tree.isEmpty());

        assertEquals("Korle Bu Hospital", tree.search(10));
        assertEquals("Ridge Hospital", tree.search(20));
        assertEquals("KATH Kumasi", tree.search(5));
        assertTrue(tree.contains(10));
        assertTrue(tree.contains(20));
        assertTrue(tree.contains(5));
    }

    @Test
    @DisplayName("Boundary Case: Update existing key value without expanding size (including null values)")
    void testUpdateExistingKey() {
        tree.insert(100, "Old Name");
        assertEquals(1, tree.getSize());
        assertEquals("Old Name", tree.search(100));

        tree.insert(100, "Updated Hospital Name");
        assertEquals(1, tree.getSize());
        assertEquals("Updated Hospital Name", tree.search(100));

        // Storing null value should still return contains(key) == true!
        tree.insert(100, null);
        assertEquals(1, tree.getSize());
        assertNull(tree.search(100));
        assertTrue(tree.contains(100));
    }

    @Test
    @DisplayName("Boundary Case: Sequential sorted insertion (Increasing & Decreasing) triggers rebalancing")
    void testSequentialInsertionsTriggerRebalancing() {
        // Test increasing order insertion
        for (int i = 1; i <= 100; i++) {
            tree.insert(i, "Hospital #" + i);
        }
        assertEquals(100, tree.getSize());
        for (int i = 1; i <= 100; i++) {
            assertEquals("Hospital #" + i, tree.search(i));
        }

        // Test decreasing order insertion in fresh tree
        RedBlackTree<Integer, String> descTree = new RedBlackTree<>();
        for (int i = 100; i >= 1; i--) {
            descTree.insert(i, "Hospital #" + i);
        }
        assertEquals(100, descTree.getSize());
        for (int i = 1; i <= 100; i++) {
            assertEquals("Hospital #" + i, descTree.search(i));
        }
    }

    @Test
    @DisplayName("Edge Case: Delete single root node until tree becomes empty")
    void testDeleteSingleRootNode() {
        tree.insert(42, "Single Node");
        assertEquals(1, tree.getSize());

        assertEquals("Single Node", tree.delete(42));
        assertEquals(0, tree.getSize());
        assertTrue(tree.isEmpty());
        assertNull(tree.search(42));
    }

    @Test
    @DisplayName("Edge Case: Delete node with 2 children (In-order successor replacement preserves original value)")
    void testDeleteNodeWithTwoChildren() {
        tree.insert(50, "Root");
        tree.insert(25, "Left Child");
        tree.insert(75, "Right Child");
        tree.insert(12, "Left-Left");
        tree.insert(37, "Left-Right");
        tree.insert(62, "Right-Left");
        tree.insert(87, "Right-Right");

        assertEquals(7, tree.getSize());

        // Delete node 25 (which has 2 children: 12 and 37) and verify original value returned
        assertEquals("Left Child", tree.delete(25));
        assertEquals(6, tree.getSize());
        assertFalse(tree.contains(25));

        // Ensure remaining children are intact
        assertEquals("Left-Left", tree.search(12));
        assertEquals("Left-Right", tree.search(37));
        assertEquals("Root", tree.search(50));
    }

    @Test
    @DisplayName("Edge Case: Delete multiple nodes in random order returning original values")
    void testDeleteMultipleNodesRandomOrder() {
        List<Integer> keys = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            keys.add(i);
            tree.insert(i, "Value " + i);
        }

        // Shuffle keys to delete in random order
        Collections.shuffle(keys);
        for (int key : keys) {
            assertEquals("Value " + key, tree.delete(key));
            assertFalse(tree.contains(key));
        }

        assertEquals(0, tree.getSize());
        assertTrue(tree.isEmpty());
    }

    @Test
    @DisplayName("Invalid Input Case: Search, delete, or insert non-existent and null keys")
    void testSearchAndDeleteNonExistentAndNullKey() {
        tree.insert(10, "Hospital A");

        assertNull(tree.search(999));
        assertFalse(tree.contains(999));
        assertNull(tree.search(null));
        assertFalse(tree.contains(null));

        assertNull(tree.delete(999));
        assertNull(tree.delete(null));
        assertEquals(1, tree.getSize());

        assertThrows(IllegalArgumentException.class, () -> tree.insert(null, "Invalid Null Key"));
    }

    @Test
    @DisplayName("Normal Case: Calculate tree height for empty, single-node, and balanced trees")
    void testGetHeight() {
        assertEquals(0, tree.getHeight());

        tree.insert(10, "Root");
        assertEquals(1, tree.getHeight());

        tree = new RedBlackTree<>();
        // Insert 100 sequential elements and verify self-balancing bounds height <= 2 * log2(101) ~ 14
        for (int i = 1; i <= 100; i++) {
            tree.insert(i, "Hospital #" + i);
        }
        assertTrue(tree.getHeight() <= 14);
    }
}
