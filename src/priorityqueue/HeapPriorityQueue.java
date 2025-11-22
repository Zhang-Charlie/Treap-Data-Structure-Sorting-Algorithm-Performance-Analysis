package priorityqueue;

/*
 */

import interfaces.Entry;
import utils.Util;
import java.util.*;

//import tree.BinaryTreeTex;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Timer.*;


/**
 * An implementation of a priority queue using an array-based heap.
 */

public class HeapPriorityQueue<K extends Comparable<K>, V extends Comparable<V> > extends AbstractPriorityQueue<K, V> {

	protected ArrayList<Entry<K, V>> heap = new ArrayList<>();

	public ArrayList<K> keys() {
		ArrayList<K> keyList = new ArrayList<>();
		for (Entry<K, V> entry : heap) {
			keyList.add(entry.getKey());
		}
		return keyList;
	}

	public ArrayList<V> values() {
		ArrayList<V> valueList = new ArrayList<>();
		for(Entry<K,V> entry : heap){
			valueList.add(entry.getValue());
		}
		return valueList;
	}
	/**
	 * Creates an empty priority queue based on the natural ordering of its keys.
	 */
	public HeapPriorityQueue() {
		super();
	}

	/**
	 * Creates an empty priority queue using the given comparator to order keys.
	 *
	 * @param comp comparator defining the order of keys in the priority queue
	 */
	public HeapPriorityQueue(Comparator<K> comp) {
		super(comp);
	}

	/**
	 * Creates a priority queue initialized with the respective key-value pairs. The
	 * two arrays given will be paired element-by-element. They are presumed to have
	 * the same length. (If not, entries will be created only up to the length of
	 * the shorter of the arrays)
	 *
	 * @param keys   an array of the initial keys for the priority queue
	 * @param values an array of the initial values for the priority queue
	 */
	public HeapPriorityQueue(K[] keys, V[] values) {
		HeapPriorityQueue priorityQueue = new HeapPriorityQueue();
		for (int i = 0; i < keys.length && i < values.length; i++) {
			priorityQueue.insert(keys[i], values[i]);
		}
	}

	// protected utilities
	protected int parent(int j) {
		return Math.floorDiv(j-1, 2);
	}

	protected int left(int j) {
		return ((2 * j) + 1);

	}

	protected int right(int j) {
		return ((2 * j) + 2);
	}

	protected boolean hasLeft(int j) {
		if (left(j) < 0 || left(j) > heap.size()) {
			return false;
		}
		return true;
	}

	protected boolean hasRight(int j) {
		if (right(j) < 0 || right(j) > heap.size()) {
			return false;
		}
		return true;

	}

	/** Exchanges the entries at indices i and j of the array list. */
	protected void swap(int i, int j) {
		if (i != j) {
			Entry<K, V> holder = heap.get(i);
			heap.set(i, heap.get(j));
			heap.set(j, holder);
		}
	}

	/**
	 * Moves the entry at index j higher, if necessary, to restore the heap
	 * property.
	 */
	protected void upheap(int j) {

		while(j > 0){
			int parent = parent(j);
			if (compare(heap.get(parent), heap.get(j)) < 0) {
				break;
			}
			swap(parent, j);
			j = parent;
		}

	}

	/*
	heapsort in place
	 */
	static<V extends Comparable<V> > void heapsort(V[] arr) {
		int n = arr.length;
		for(int i = (n/2) - 1; i >= 0; i--) {
			downheap(arr, n, i);
		}
		for(int i = n - 1; i >= 0; i--) {
			V temp = arr[0];
			arr[0] = arr[i];
			arr[i] = temp;
			downheap(arr, i, 0);
		}

	}
	protected static<V extends Comparable<V> > void downheap(V[] arr, int start, int end) {

		int left = 2 * start + 1;
		int right = 2 * start + 2;
		int smallest = start;

		if(left < end && arr[left].compareTo(arr[smallest]) < 0){
			smallest = left;
		}
		if(right < end && arr[right].compareTo(arr[smallest]) < 0){
			smallest = right;
		}
		if(smallest == start){
			return;
		}
		V temp = arr[start];
		arr[start] = arr[smallest];
		arr[smallest] = temp;
		start = smallest;
	}

	/**
	 * Moves the entry at index j lower, if necessary, to restore the heap property.
	 */
	protected void downheap(int j) {
		while(hasLeft(j)) {
			int left = left(j);
			int right = right(j);
			int min = Math.min(left, right);
			if (compare(heap.get(min), heap.get(j)) > 0) {
				break;
			}
			swap(j, min);
			j = min;
		}

	}

	/** Performs a bottom-up construction of the heap in linear time. */
	protected void heapify() {
		int n = heap.size();
		for (int i = (n/2) - 1; i >= 0; i--) {
			downheap(i);
		}
	}

	// public methods

	/**
	 * Returns the number of items in the priority queue.
	 *
	 * @return number of items
	 */
	@Override
	public int size() {
		return heap.size();
	}

	/**
	 * Returns (but does not remove) an entry with minimal key.
	 *
	 * @return entry having a minimal key (or null if empty)
	 */
	@Override
	public Entry<K, V> min() {
		return heap.get(0);
	}

	/**
	 * Inserts a key-value pair and return the entry created.
	 *
	 * @param key   the key of the new entry
	 * @param value the associated value of the new entry
	 * @return the entry storing the new key-value pair
	 * @throws IllegalArgumentException if the key is unacceptable for this queue
	 */
	@Override
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		try{
		Entry<K, V> entry = new PQEntry<>(key, value);
        heap.add(entry);
		upheap(heap.size() - 1);
		return entry;}
		catch (IllegalArgumentException e){
			System.out.println("invalid key");
		}
		return null;
	}

	/**
	 * Removes and returns an entry with minimal key.
	 *
	 * @return the removed entry (or null if empty)
	 */
	@Override
	public Entry<K, V> removeMin() {
		Entry<K, V> min = heap.getFirst();
		heap.set(0, heap.getLast());
		heap.remove(heap.size() - 1);
		downheap(0);
		return min;
	}

	public String toString() {
		return heap.toString();
	}



	public static void main(String [] args) {
		Random rnd = new Random();
		rnd.setSeed(1024);
		int n_min = 1000, n_max = 1000000, n_samples = 80;
		double alpha = ( (Math.log(n_max) / Math.log(n_min)) - 1) / (n_samples-1);
		for(int i = 0; i < n_samples; ++i) {
			int n = (int) Math.pow(n_min, (1 + i * alpha));
			Integer[] arr = IntStream.rangeClosed(1, n).boxed().toArray(Integer[]::new);
			Runnable worker = () -> { Util.shuffle(arr); HeapPriorityQueue<Integer, Integer> pq = new HeapPriorityQueue<>(arr, arr);
				for(int k = 0; k < arr.length; ++k) {
					arr[k] = pq.removeMin().getKey();
				}
			};
		}
	}


}


