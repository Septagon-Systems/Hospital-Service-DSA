package edu.ug.dcit308.hospital.ds.unionfind;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisjointSetTest {

    private DisjointSet ds;

    @BeforeEach
    void setUp() {
        ds = new DisjointSet(6);
    }

    @Test
    void testInitialComponentCount() {
        assertEquals(6, ds.componentCount());
    }

    @Test
    void testInitialSize() {
        assertEquals(6, ds.size());
    }

    @Test
    void testInitiallyNotConnected() {
        assertFalse(ds.connected(0, 1));
        assertFalse(ds.connected(2, 5));
    }

    @Test
    void testFindReturnsOwnIndexInitially() {
        for (int i = 0; i < 6; i++) {
            assertEquals(i, ds.find(i));
        }
    }

    @Test
    void testUnionMergesTwoComponents() {
        assertTrue(ds.union(0, 1));
        assertTrue(ds.connected(0, 1));
        assertEquals(5, ds.componentCount());
    }

    @Test
    void testUnionSameComponentReturnsFalse() {
        ds.union(0, 1);
        assertFalse(ds.union(0, 1));
        assertEquals(5, ds.componentCount());
    }

    @Test
    void testUnionTransitivity() {
        ds.union(0, 1);
        ds.union(1, 2);
        assertTrue(ds.connected(0, 2));
        assertEquals(4, ds.componentCount());
    }

    @Test
    void testUnionChain() {
        for (int i = 0; i < 5; i++) {
            ds.union(i, i + 1);
        }
        assertEquals(1, ds.componentCount());
        assertTrue(ds.connected(0, 5));
    }

    @Test
    void testFindIsSameAfterUnion() {
        ds.union(0, 1);
        assertEquals(ds.find(0), ds.find(1));
    }

    @Test
    void testFindPathCompression() {
        ds.union(0, 1);
        ds.union(1, 2);
        ds.union(2, 3);
        int root = ds.find(0);
        assertEquals(root, ds.find(3));
    }

    @Test
    void testConnectedAfterMultipleUnions() {
        ds.union(0, 2);
        ds.union(3, 5);
        assertTrue(ds.connected(0, 2));
        assertTrue(ds.connected(3, 5));
        assertFalse(ds.connected(0, 3));
    }

    @Test
    void testElementConnectedToItself() {
        assertTrue(ds.connected(4, 4));
    }

    @Test
    void testSingleElementSet() {
        DisjointSet single = new DisjointSet(1);
        assertEquals(1, single.size());
        assertEquals(1, single.componentCount());
        assertEquals(0, single.find(0));
        assertTrue(single.connected(0, 0));
    }

    @Test
    void testInvalidSizeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new DisjointSet(0));
        assertThrows(IllegalArgumentException.class, () -> new DisjointSet(-3));
    }

    @Test
    void testFindOutOfRangeThrows() {
        assertThrows(IndexOutOfBoundsException.class, () -> ds.find(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> ds.find(6));
    }

    @Test
    void testUnionOutOfRangeThrows() {
        assertThrows(IndexOutOfBoundsException.class, () -> ds.union(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> ds.union(0, 10));
    }
}
