package edu.ug.dcit308.hospital.ds.hash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomHashTableTest {

    private CustomHashTable<String, Integer> table;

    @BeforeEach
    void setUp() {
        table = new CustomHashTable<>();
    }

    // ------------------------------------------------------------------
    // put & get
    // ------------------------------------------------------------------

    @Test
    void testPutAndGet() {
        table.put("Korle Bu", 100);
        table.put("Ridge",    200);

        assertEquals(100, table.get("Korle Bu"));
        assertEquals(200, table.get("Ridge"));
    }

    @Test
    void testGetMissingKeyReturnsNull() {
        table.put("Korle Bu", 100);
        assertNull(table.get("NoSuchHospital"));
    }

    @Test
    void testPutNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.put(null, 1));
    }

    @Test
    void testGetNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.get(null));
    }

    @Test
    void testPutOverwritesExistingKey() {
        table.put("Korle Bu", 100);
        table.put("Korle Bu", 999);

        assertEquals(999, table.get("Korle Bu"));
        assertEquals(1, table.size()); // still only one entry
    }

    // ------------------------------------------------------------------
    // containsKey
    // ------------------------------------------------------------------

    @Test
    void testContainsKey() {
        table.put("Tema General", 50);

        assertTrue(table.containsKey("Tema General"));
        assertFalse(table.containsKey("Komfo Anokye"));
        assertFalse(table.containsKey(null));
    }

    // ------------------------------------------------------------------
    // remove
    // ------------------------------------------------------------------

    @Test
    void testRemoveExistingKey() {
        table.put("Ridge", 200);
        int removed = table.remove("Ridge");

        assertEquals(200, removed);
        assertNull(table.get("Ridge"));
        assertFalse(table.containsKey("Ridge"));
        assertEquals(0, table.size());
    }

    @Test
    void testRemoveMissingKeyReturnsNull() {
        assertNull(table.remove("NoSuchKey"));
    }

    @Test
    void testRemoveNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> table.remove(null));
    }

    // ------------------------------------------------------------------
    // Collision handling
    // ------------------------------------------------------------------

    /**
     * Keys whose {@code hashCode() % 16} collide are placed in the same
     * bucket. We verify both can be stored and retrieved independently.
     *
     * <p>Note: String hashCodes in Java are deterministic (JLS §15.18.1),
     * so we compute the bucket of "Aa" and "BB", which are well-known
     * Java hash-collision pairs.</p>
     */
    @Test
    void testCollisionChaining() {
        // "Aa" and "BB" have the same Java hashCode (2112).
        String k1 = "Aa";
        String k2 = "BB";
        assertEquals(k1.hashCode(), k2.hashCode(), "Pre-condition: keys must collide");

        table.put(k1, 1);
        table.put(k2, 2);

        assertEquals(1, table.get(k1));
        assertEquals(2, table.get(k2));
        assertEquals(2, table.size());
    }

    @Test
    void testCollisionRemoveOneLeaveOther() {
        String k1 = "Aa";
        String k2 = "BB";

        table.put(k1, 1);
        table.put(k2, 2);

        table.remove(k1);

        assertNull(table.get(k1));
        assertEquals(2, table.get(k2));
        assertEquals(1, table.size());
    }

    // ------------------------------------------------------------------
    // Rehashing (load factor)
    // ------------------------------------------------------------------

    @Test
    void testRehashOccursAndDataIsPreserved() {
        // Default capacity is 16; load factor 0.75 triggers rehash at 13 entries.
        // Insert 20 entries to force at least one rehash.
        for (int i = 0; i < 20; i++) {
            table.put("hospital_" + i, i * 10);
        }

        assertEquals(20, table.size());
        for (int i = 0; i < 20; i++) {
            assertEquals(i * 10, table.get("hospital_" + i));
        }
    }

    // ------------------------------------------------------------------
    // size / isEmpty / clear
    // ------------------------------------------------------------------

    @Test
    void testSizeAndIsEmpty() {
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());

        table.put("A", 1);
        table.put("B", 2);

        assertFalse(table.isEmpty());
        assertEquals(2, table.size());
    }

    @Test
    void testClearResetsTable() {
        table.put("Korle Bu", 100);
        table.put("Ridge",    200);
        table.clear();

        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
        assertNull(table.get("Korle Bu"));
    }

    @Test
    void testPutAfterClear() {
        table.put("Korle Bu", 100);
        table.clear();
        table.put("Ridge", 50);

        assertEquals(50, table.get("Ridge"));
        assertEquals(1, table.size());
    }

    // ------------------------------------------------------------------
    // Generic key type (Integer keys)
    // ------------------------------------------------------------------

    @Test
    void testIntegerKeys() {
        CustomHashTable<Integer, String> intTable = new CustomHashTable<>();
        intTable.put(1, "One");
        intTable.put(2, "Two");
        intTable.put(1, "Updated");

        assertEquals("Updated", intTable.get(1));
        assertEquals("Two",     intTable.get(2));
        assertEquals(2, intTable.size());
    }
}
