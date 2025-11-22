package priorityqueue;


import interfaces.Entry;
import interfaces.PriorityQueue;

import java.util.Comparator;


public abstract class AbstractPriorityQueue<K, V> implements PriorityQueue<K, V> {
    //---------------- nested PQEntry class ----------------

    /**
     * A concrete implementation of the Entry interface to be used within
     * a PriorityQueue implementation.
     */
    protected static class PQEntry<K extends Comparable<K>, V> implements Entry<K, V> {
        private K k;  // key
        private V v;  // value

        public PQEntry(K key, V value) {
            k = key;
            v = value;
        }

        // methods of the Entry interface
        public K getKey() {
            return k;
        }

        public V getValue() {
            return v;
        }

        // utilities not exposed as part of the Entry interface
        protected void setKey(K key) {
            k = key;
        }

        protected void setValue(V value) {
            v = value;
        }

        public String toString() {
            //return "<" + k + ", " + v + ">";
            return "" + k;
        }

        @Override
        public int compareTo(Entry<K, V> o) {
            return getKey().compareTo(o.getKey());
        }
    } //----------- end of nested PQEntry class -----------

    // instance variable for an AbstractPriorityQueue
    /**
     * The comparator defining the ordering of keys in the priority queue.
     */
    private final Comparator<K> comp;

    /**
     * Creates an empty priority queue using the given comparator to order keys.
     *
     * @param c comparator defining the order of keys in the priority queue
     */
    protected AbstractPriorityQueue(Comparator<K> c) {
        comp = c;
    }

    /**
     * Creates an empty priority queue based on the natural ordering of its keys.
     */
    protected AbstractPriorityQueue() {
        this(new DefaultComparator<K>());
    }

    /**
     * Method for comparing two entries according to key
     */
    protected int compare(Entry<K, V> a, Entry<K, V> b) {
        return comp.compare(a.getKey(), b.getKey());
    }


    /**
     * Tests whether the priority queue is empty.
     *
     * @return true if the priority queue is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
}
