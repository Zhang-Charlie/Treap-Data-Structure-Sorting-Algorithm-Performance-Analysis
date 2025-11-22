package tree;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SplayTreeMapTest {

	@Test
	void testGet() throws IOException {
		SplayTreeMap<Integer, String> map = new SplayTreeMap<>();
		Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

		for(Integer i : arr) {
			map.put(i, Integer.toString(i));
		}
		assertEquals("5", map.tree.root().getElement().toString());
		assertEquals("15", map.get(15));
		assertEquals("24", map.get(24));
        assertNull(map.get(-1));
		assertEquals("1", map.tree.root().getElement().toString());
	}

	@Test
	void testPut() throws IOException {
		SplayTreeMap<Integer, String> map = new SplayTreeMap<>();
		Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

		for(Integer i : arr) {
			map.put(i, Integer.toString(i));
		}

		Iterator<Integer> keys = map.keySet().iterator();
		List<Integer> list = new ArrayList<>();
		keys.forEachRemaining(list::add);

		// probably the best test is to check the inorder + preorder
		assertEquals("[1, 2, 4, 5, 12, 15, 21, 23, 24, 26, 33, 35]", list.toString());
		assertEquals(7, map.tree.height(map.tree.root()));
		assertEquals("[1, 2, 4, 5, 12, 15, 21, 23, 24, 26, 33, 35]", map.tree.inorder().toString());
		assertEquals("[5, 2, 1, 4, 15, 12, 21, 23, 24, 33, 26, 35]", map.tree.preorder().toString());
	}

	@Test
	void testRemoveK() throws IOException {
		SplayTreeMap<Integer, String> map = new SplayTreeMap<>();
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
		SplayTreeMap<Integer, String> map = new SplayTreeMap<>();
		Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

		for(Integer i : arr) {
			map.put(i, Integer.toString(i));
		}
		
		assertEquals(1, map.firstEntry().getKey());
	}

	@Test
	void testLastEntry() throws IOException {
		SplayTreeMap<Integer, String> map = new SplayTreeMap<>();
		Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

		for(Integer i : arr) {
			map.put(i, Integer.toString(i));
		}
		
		assertEquals(35, map.lastEntry().getKey());
	}

	@Test
	void testCeilingEntry() throws IOException {
		SplayTreeMap<Integer, String> map = new SplayTreeMap<>();
		Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

		for(Integer i : arr) {
			map.put(i, Integer.toString(i));
		}
		
		assertEquals(12, map.ceilingEntry(11).getKey());
		
		assertEquals(2, map.ceilingEntry(2).getKey());
	}

	@Test
	void testFloorEntry() throws IOException {
		SplayTreeMap<Integer, String> map = new SplayTreeMap<>();
		Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

		for(Integer i : arr) {
			map.put(i, Integer.toString(i));
		}
		
		assertEquals(5, map.floorEntry(11).getKey());
		assertEquals(5, map.floorEntry(5).getKey());
	}

	@Test
	void testLowerEntry() throws IOException {
		SplayTreeMap<Integer, String> map = new SplayTreeMap<>();
		Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

		for(Integer i : arr) {
			map.put(i, Integer.toString(i));
		}
		
		assertEquals(23, map.lowerEntry(24).getKey());
		assertEquals(26, map.lowerEntry(31).getKey());
	}

	@Test
	void testHigherEntry() throws IOException {
		SplayTreeMap<Integer, String> map = new SplayTreeMap<>();
		Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

		for(Integer i : arr) {
			map.put(i, Integer.toString(i));
		}
		
		assertEquals(12, map.higherEntry(11).getKey());
	}

	@Test
	void testEntrySet() {
		//fail("Not yet implemented");
		System.out.println("testEntrySet not implemented yet");
	}

	@Test
	void testToString() throws IOException {
		SplayTreeMap<Integer, String> map = new SplayTreeMap<>();
		//java.util.TreeMap<Integer, String> map = new java.util.TreeMap<>();
		Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

		for(Integer i : arr) {
			map.put(i, Integer.toString(i));
		}
		//assertEquals("[⦰, 1, ⦰, 2, ⦰, 4, ⦰, 5, ⦰, 12, ⦰, 15, ⦰, 21, ⦰, 23, ⦰, 24, ⦰, 26, ⦰, 33, ⦰, 35, ⦰]", map.toString());
		assertEquals("[1, 2, 4, 5, 12, 15, 21, 23, 24, 26, 33, 35]", map.tree.inorder().toString());
	}

	@Test
	void testSubMap() throws IOException {
		SplayTreeMap<Integer, String> map = new SplayTreeMap<>();
		Integer[] arr = new Integer[] {35,26,15,24,33,4,12,1,23,21,2,5};

		for(Integer i : arr) {
			map.put(i, Integer.toString(i));
		}
				
		assertEquals("[12, 15, 21, 23, 24, 26, 33]", map.subMap(12, 34).toString());
	}

}
