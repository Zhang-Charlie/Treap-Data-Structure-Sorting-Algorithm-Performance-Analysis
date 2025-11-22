package hashtable;

import org.junit.jupiter.api.Test;
import interfaces.Entry;
import priorityqueue.DefaultComparator;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProbeHashMapTest {



	@Test
	void testSize() {
		ProbeHashMap<Integer, String> map = new ProbeHashMap<Integer, String>();

		int n = 10;
		for(int i = 0; i < n; ++i) {
			map.put(i, Integer.toString(i));
		}
		assertEquals(n, map.size());
	}

	@Test
	void testGet() {
		ProbeHashMap<String, Integer> map = new ProbeHashMap<String, Integer>();

		int n = 10;
		for(int i = 0; i < n; ++i) {
			map.put(Integer.toString(i), i);
		}
		assertEquals(5, map.get("5"));
		assertEquals(2, map.get("2"));
	}

	@Test
	void testRemove() {
		ProbeHashMap<String, Integer> map = new ProbeHashMap<String, Integer>();

		int n = 10;
		for(int i = 0; i < n; ++i) {
			map.put(Integer.toString(i), i);
		}
		assertEquals(5, map.remove("5"));
		assertEquals(n-1, map.size());
	}

	@Test
	void testPut() {
		ProbeHashMap<String, Integer> map = new ProbeHashMap<String, Integer>();

		int n = 10;
		for(int i = 0; i < n; ++i) {
			map.put(Integer.toString(i), i);
		}
		assertEquals(n, map.size());		
	}

	@Test
	void testIsEmpty() {
		ProbeHashMap<String, Integer> map = new ProbeHashMap<String, Integer>();

		assertEquals(true, map.isEmpty());
		
		int n = 10;
		for(int i = 0; i < n; ++i) {
			map.put(Integer.toString(i), i);
		}
		assertEquals(false, map.isEmpty());	
	}

	@Test
	void testKeySet() {
		ProbeHashMap<String, Integer> map = new ProbeHashMap<String, Integer>();

		map.put("one", 1);
		map.put("two", 2);
		map.put("three", 3);
		ArrayList<String> buf = new ArrayList<>();
		for(String s : map.keySet()) buf.add(s);
		buf.sort(new DefaultComparator<String>());
		assertEquals("[one, three, two]", buf.toString());
	}

	@Test
	void testEntrySet() {
		ProbeHashMap<String, Integer> map = new ProbeHashMap<String, Integer>(10);

//		map.put("one", 1);
//		map.put("two", 2);
//		map.put("three", 3);
		for(int i = 0; i < 5; ++i) {
			map.put(Integer.toString(i), i);
		}
		System.out.println(map.capacity + " " + map.size());
		ArrayList<String> buf = new ArrayList<>();
		for(Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
			var key = entry.getKey();
			var val = entry.getValue();
			//assertEquals(
		}
	}

	@Test
	void testValues() {
		ProbeHashMap<String, Integer> map = new ProbeHashMap<String, Integer>();

		int n = 10;
		for(int i = 0; i < n; ++i) {
			map.put(Integer.toString(i), i);
		}
		ArrayList<Integer> buf = new ArrayList<>();
		for(Integer s : map.values()) buf.add(s);
		buf.sort(new DefaultComparator<Integer>());
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", buf.toString());
	}

}
