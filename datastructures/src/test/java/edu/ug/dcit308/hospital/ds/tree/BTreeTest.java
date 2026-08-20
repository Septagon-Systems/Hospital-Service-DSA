package edu.ug.dcit308.hospital.ds.tree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BTree<K, V> Unit Tests")
class BTreeTest {

    private BTree<Integer, String> tree;

    @BeforeEach
    void setUp() {
        tree = new BTree<>(2); // t = 2 (2-3-4 tree)
    }

    @Nested
    @DisplayName("Constructor and Invariant Validation")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor initializes degree t=2")
        void testDefaultConstructor() {
            BTree<String, String> defaultTree = new BTree<>();
            assertEquals(2, defaultTree.getMinDegree());
            assertEquals(0, defaultTree.size());
            assertTrue(defaultTree.isEmpty());
            assertEquals(-1, defaultTree.height());
        }

        @Test
        @DisplayName("Custom valid minimum degree initializes correctly")
        void testCustomValidDegree() {
            BTree<Integer, String> t3 = new BTree<>(3);
            assertEquals(3, t3.getMinDegree());
            assertEquals(0, t3.size());
        }

        @Test
        @DisplayName("Degree less than 2 throws IllegalArgumentException")
        void testInvalidDegreeThrows() {
            assertThrows(IllegalArgumentException.class, () -> new BTree<>(1));
            assertThrows(IllegalArgumentException.class, () -> new BTree<>(0));
            assertThrows(IllegalArgumentException.class, () -> new BTree<>(-5));
        }
    }

    @Nested
    @DisplayName("Empty Tree Behavior")
    class EmptyTreeTests {

        @Test
        @DisplayName("Empty tree reports correct metadata")
        void testEmptyMetadata() {
            assertEquals(0, tree.size());
            assertTrue(tree.isEmpty());
            assertEquals(-1, tree.height());
            assertNull(tree.get(10));
            assertFalse(tree.contains(10));
            assertEquals(0, tree.inOrder().length);
        }

        @Test
        @DisplayName("minKey and maxKey on empty tree throw NoSuchElementException")
        void testMinMaxOnEmptyThrows() {
            assertThrows(NoSuchElementException.class, () -> tree.minKey());
            assertThrows(NoSuchElementException.class, () -> tree.maxKey());
        }
    }

    @Nested
    @DisplayName("Insertion and Search Operations")
    class InsertionTests {

        @Test
        @DisplayName("Insert single element")
        void testInsertSingle() {
            tree.insert(10, "Ten");
            assertEquals(1, tree.size());
            assertFalse(tree.isEmpty());
            assertEquals(0, tree.height());
            assertEquals("Ten", tree.get(10));
            assertTrue(tree.contains(10));
            assertEquals(10, tree.minKey());
            assertEquals(10, tree.maxKey());
        }

        @Test
        @DisplayName("Insert up to full root without split (t=2, capacity=3 keys)")
        void testInsertRootCapacity() {
            tree.insert(20, "V20");
            tree.insert(10, "V10");
            tree.insert(30, "V30");

            assertEquals(3, tree.size());
            assertEquals(0, tree.height());
            assertEquals(10, tree.minKey());
            assertEquals(30, tree.maxKey());

            Comparable<Integer>[] inOrder = tree.inOrder();
            assertArrayEquals(new Integer[]{10, 20, 30}, inOrder);
        }

        @Test
        @DisplayName("4th key triggers proactive root split (height increases to 1)")
        void testRootSplit() {
            tree.insert(10, "V10");
            tree.insert(20, "V20");
            tree.insert(30, "V30");
            tree.insert(40, "V40");

            assertEquals(4, tree.size());
            assertEquals(1, tree.height());
            assertEquals("V10", tree.get(10));
            assertEquals("V20", tree.get(20));
            assertEquals("V30", tree.get(30));
            assertEquals("V40", tree.get(40));

            Comparable<Integer>[] inOrder = tree.inOrder();
            assertArrayEquals(new Integer[]{10, 20, 30, 40}, inOrder);
        }

        @Test
        @DisplayName("Sequential ascending insertions maintain sorted in-order traversal and balanced height")
        void testSequentialAscending() {
            int n = 50;
            for (int i = 1; i <= n; i++) {
                tree.insert(i, "Val" + i);
            }

            assertEquals(n, tree.size());
            assertEquals(1, tree.minKey());
            assertEquals(n, tree.maxKey());
            assertTrue(tree.height() <= 5);

            Comparable<Integer>[] inOrder = tree.inOrder();
            assertEquals(n, inOrder.length);
            for (int i = 0; i < n; i++) {
                assertEquals(i + 1, inOrder[i]);
                assertEquals("Val" + (i + 1), tree.get(i + 1));
            }
        }

        @Test
        @DisplayName("Sequential descending insertions maintain sorted in-order traversal")
        void testSequentialDescending() {
            int n = 50;
            for (int i = n; i >= 1; i--) {
                tree.insert(i, "Val" + i);
            }

            assertEquals(n, tree.size());
            assertEquals(1, tree.minKey());
            assertEquals(n, tree.maxKey());

            Comparable<Integer>[] inOrder = tree.inOrder();
            assertEquals(n, inOrder.length);
            for (int i = 0; i < n; i++) {
                assertEquals(i + 1, inOrder[i]);
            }
        }

        @Test
        @DisplayName("Duplicate key updates value in-place without altering tree size")
        void testDuplicateKeyUpdate() {
            tree.insert(10, "Original");
            tree.insert(20, "Twenty");
            tree.insert(10, "Updated");

            assertEquals(2, tree.size());
            assertEquals("Updated", tree.get(10));
            assertEquals("Twenty", tree.get(20));
        }

        @Test
        @DisplayName("Null key insertion throws IllegalArgumentException")
        void testNullKeyThrows() {
            assertThrows(IllegalArgumentException.class, () -> tree.insert(null, "Value"));
            assertThrows(IllegalArgumentException.class, () -> tree.get(null));
            assertThrows(IllegalArgumentException.class, () -> tree.contains(null));
        }
    }

    @Nested
    @DisplayName("Hospital Operations Domain Indexing Scenario")
    class HospitalIndexScenarioTests {

        @Test
        @DisplayName("Ghana Hospital Facility Code Primary Index Simulation")
        void testHospitalFacilityCodeIndex() {
            BTree<String, String> facilityIndex = new BTree<>(2);

            String[] codes = {
                "KBTH-ACC-01",
                "37MH-ACC-02",
                "KATH-KMS-01",
                "UGMC-ACC-03",
                "TTH-TAM-01",
                "CCTH-CAP-01",
                "EFTH-TAK-01",
                "TGH-TEM-01",
                "BRH-BOL-01",
                "WRH-WA-01"
            };

            String[] names = {
                "Korle Bu Teaching Hospital",
                "37 Military Hospital",
                "Komfo Anokye Teaching Hospital",
                "University of Ghana Medical Centre",
                "Tamale Teaching Hospital",
                "Cape Coast Teaching Hospital",
                "Effia Nkwanta Regional Hospital",
                "Tema General Hospital",
                "Bolgatanga Regional Hospital",
                "Wa Regional Hospital"
            };

            for (int i = 0; i < codes.length; i++) {
                facilityIndex.insert(codes[i], names[i]);
            }

            assertEquals(10, facilityIndex.size());
            assertEquals("37MH-ACC-02", facilityIndex.minKey());
            assertEquals("WRH-WA-01", facilityIndex.maxKey());

            assertEquals("Korle Bu Teaching Hospital", facilityIndex.get("KBTH-ACC-01"));
            assertEquals("Tamale Teaching Hospital", facilityIndex.get("TTH-TAM-01"));
            assertNull(facilityIndex.get("UNKNOWN-99"));

            Comparable<String>[] sortedCodes = facilityIndex.inOrder();
            for (int i = 0; i < sortedCodes.length - 1; i++) {
                assertTrue(sortedCodes[i].compareTo((String) sortedCodes[i + 1]) < 0,
                    "In-order violation at index " + i);
            }

            facilityIndex.clear();
            assertEquals(0, facilityIndex.size());
            assertTrue(facilityIndex.isEmpty());
        }
    }
}
