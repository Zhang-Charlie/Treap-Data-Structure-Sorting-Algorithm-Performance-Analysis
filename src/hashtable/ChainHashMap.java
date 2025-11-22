package hashtable;

import interfaces.Entry;
import interfaces.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Map implementation using hash table with separate chaining.
 */

public class ChainHashMap<K extends Comparable<K>, V> extends AbstractHashMap<K, V> {
	// a fixed capacity array of UnsortedTableMap that serve as buckets
	private UnsortedTableMap<K, V>[] table; // initialized within createTable

	/** Creates a hash table with capacity 11 and prime factor 109345121. */
	public ChainHashMap() {
		super();
	}

	/** Creates a hash table with given capacity and prime factor 109345121. */
	public ChainHashMap(int cap) {
		super(cap);
	}

	/** Creates a hash table with the given capacity and prime factor. */
	public ChainHashMap(int cap, int p) {
		super(cap, p);
	}

	/** Creates an empty table having length equal to current capacity. */
	@Override
	@SuppressWarnings({ "unchecked" })
	protected void createTable() {
 		table = new UnsortedTableMap[capacity];
 	}

	@Override
	public double loadFactor() {
		return ((double) n / capacity);
	}

	@Override
	public int numCollisions() {
		int collisions = 0;
		for (UnsortedTableMap<K,V> bucket : table) {
			if (bucket != null && bucket.size() > 1) {
				collisions += bucket.size() - 1;
			}
		}
		return collisions;
	}
	/**
	 * Returns value associated with key k in bucket with hash value h. If no such
	 * entry exists, returns null.
	 * 
	 * @param h the hash value of the relevant bucket
	 * @param k the key of interest
	 * @return associate value (or null, if no such entry)
	 */
	@Override
	protected V bucketGet(int h, K k) {
		UnsortedTableMap<K, V> bucket = table[h];
		if (bucket == null) {
			return null;
		}
		return bucket.get(k);
	}

	/**
	 * Associates key k with value v in bucket with hash value h, returning the
	 * previously associated value, if any.
	 * 
	 * @param h the hash value of the relevant bucket
	 * @param k the key of interest
	 * @param v the value to be associated
	 * @return previous value associated with k (or null, if no such entry)
	 */
	@Override
	protected V bucketPut(int h, K k, V v) {
		UnsortedTableMap<K, V> bucket = table[h];
		if (bucket == null) {
			bucket = table[h] = new UnsortedTableMap<>();
		}
		int oldSize = bucket.size();
		V previous = bucket.put(k, v);
		n += bucket.size() - oldSize;
		return previous;
	}		


	/**
	 * Removes entry having key k from bucket with hash value h, returning the
	 * previously associated value, if found.
	 * 
	 * @param h the hash value of the relevant bucket
	 * @param k the key of interest
	 * @return previous value associated with k (or null, if no such entry)
	 */
	@Override
	protected V bucketRemove(int h, K k) {
		UnsortedTableMap<K, V> bucket = table[h];
		if (bucket == null) {
			return null;
		}
		V previous = bucketGet(h, k);
		bucket.remove(k);
		return previous;
	}

	/**
	 * Returns an iterable collection of all key-value entries of the map.
	 *
	 * @return iterable collection of the map's entries
	 */
	@Override
	public Iterable<Entry<K, V>> entrySet() {
		ArrayList<Entry<K, V>> buffer = new ArrayList<>();
		for (UnsortedTableMap<K, V> bucket : table) {
			if (bucket == null) {
				continue;
			}
			for (Entry<K, V> entry : bucket.entrySet()) {
				if (entry.getValue() != null) {
					buffer.add(entry);
				}
			}
		}
		return buffer;
	}
	
	public String toString() {
		return entrySet().toString();
	}

	public static void main(String []args) throws FileNotFoundException {
		File f = new File("C:/Users/andre/Downloads/sample_text.txt"); // check the path to the file
		ChainHashMap<String, Integer> counter = new ChainHashMap<String,
				Integer>();

		// use a Scanner to read words from the file
		Scanner scanner = new Scanner(f);
		while (scanner.hasNext()) { // read the file word at a time
			String word = scanner.next();

			// if word is not in the hashmap, add it with count=1
			// otherwise, find the entry for this word and increment
			Integer count = counter.get(word);
			if (count == null) {
				counter.put(word, 1);
			} else {
				counter.put(word, count + 1);
			}
		}

		ArrayList<Entry<String, Integer>> entries = new ArrayList<>();
		for (Entry<String, Integer> entry : counter.entrySet()) {
			entries.add(entry);
		}
		for (int i = 1; i < entries.size(); i++) {
			if (entries.get(i - 1).getValue() < entries.get(i).getValue()) {
				while(i != 0 && entries.get(i - 1).getValue() < entries.get(i).getValue()) {
					Entry<String, Integer> entry = entries.get(i);
					entries.set(i, entries.get(i - 1));
					entries.set(i - 1, entry);
					i--;
				}
			}
		}
		
		// Print the word counts
		System.out.println("\nWord counts:");
		for (Entry<String, Integer> entry : entries) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		
		scanner.close();
	}

		 // sort the key, values...
		 // Can you sort the Entries by the value?
}
