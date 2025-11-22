package tree;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RBTreeMapTest {

    @Test
    void testGet() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

        for(Integer i : arr) {
            map.put(i, Integer.toString(i));
        }
        assertEquals("15", map.tree.root().getElement().toString());
        assertEquals("15", map.get(15));
        assertEquals("24", map.get(24));
        assertNull(map.get(-1));
        assertEquals("15", map.tree.root().getElement().toString());
    }

    @Test
    void testPut() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

        for(Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        Iterator<Integer> keys = map.keySet().iterator();
        List<Integer> list = new ArrayList<>();
        keys.forEachRemaining(list::add);

        // probably the best test is to check the inorder + preorder
        assertEquals("[1, 2, 4, 5, 12, 15, 21, 23, 24, 26, 33, 35]", list.toString());
        assertEquals(4, map.tree.height(map.tree.root()));
        assertEquals("[1, 2, 4, 5, 12, 15, 21, 23, 24, 26, 33, 35]", map.tree.inorder().toString());
        assertEquals("[15, 4, 1, 2, 12, 5, 26, 23, 21, 24, 35, 33]", map.tree.preorder().toString());
    }

    @Test
    void testRemoveK() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

        for(Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(12, map.size());
        assertEquals("26", map.remove(26));
        assertEquals(11, map.size());
    }

    @Test
    void testFirstEntry() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

        for(Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(1, map.firstEntry().getKey());
    }

    @Test
    void testLastEntry() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

        for(Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(35, map.lastEntry().getKey());
    }

    @Test
    void testCeilingEntry() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

        for(Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(12, map.ceilingEntry(11).getKey());

        assertEquals(2, map.ceilingEntry(2).getKey());
    }

    @Test
    void testFloorEntry() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

        for(Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(5, map.floorEntry(11).getKey());
        assertEquals(5, map.floorEntry(5).getKey());
    }

    @Test
    void testLowerEntry() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

        for(Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(23, map.lowerEntry(24).getKey());
        assertEquals(26, map.lowerEntry(31).getKey());
    }

    @Test
    void testHigherEntry() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

        for(Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals(12, map.higherEntry(11).getKey());
    }

    @Ignore
    @Test
    void testEntrySet() {
        System.out.println("testEntrySet:: Not yet implemented");
    }

    @Test
    void testCollections() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        java.util.TreeMap<Integer, String> treeMap = new java.util.TreeMap<>();
        //treeMap.
    }
    @Test
    void testToString() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

        for(Integer i : arr) {
            map.put(i, Integer.toString(i));
        }
        //assertEquals("[⦰, 1, ⦰, 2, ⦰, 4, ⦰, 5, ⦰, 12, ⦰, 15, ⦰, 21, ⦰, 23, ⦰, 24, ⦰, 26, ⦰, 33, ⦰, 35, ⦰]", map.toString());
        assertEquals("[1, 2, 4, 5, 12, 15, 21, 23, 24, 26, 33, 35]", map.tree.inorder().toString());
    }

    @Test
    void testSubMap() throws IOException {
        RBTreeMap<Integer, String> map = new RBTreeMap<>();
        Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

        for(Integer i : arr) {
            map.put(i, Integer.toString(i));
        }

        assertEquals("[12, 15, 21, 23, 24, 26, 33]", map.subMap(12, 34).toString());
    }

}
