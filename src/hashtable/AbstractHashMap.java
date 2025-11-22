package hashtable;

import interfaces.AbstractMap;
import interfaces.Entry;

import java.util.ArrayList;
import java.util.Random;

/**
 * An abstract base class supporting Map implementations that use hash
 * tables with MAD compression.
 * <p>
 * The base class provides the following means of support:
 * 1) Support for calculating hash values with MAD compression
 * 2) Support for resizing table when load factor reaches 1/2
 * <p>
 * Subclass is responsible for providing abstract methods:
 * createTable(), bucketGet(h,k), bucketPut(h,k,v),
 * bucketRemove(h,k), and entrySet()
 * and for accurately maintaining the protected member, n,
 * to reflect changes within bucketPut and bucketRemove.
 */
public abstract class AbstractHashMap<K, V> extends AbstractMap<K, V> {
    protected int n = 0;                 // number of entries in the dictionary
    protected int capacity;              // length of the table
    private int prime = 0;                   // prime factor
    private long scale = 0;
    private long shift = 0;           // the shift and scaling factors

    /**
     * Creates a hash table with the given capacity and prime factor.
     */
    public AbstractHashMap(int cap, int p) {
        this.capacity = cap;
        this.prime = p;
        Random rand = new Random();
        this.scale = rand.nextInt(prime - 1) + 1;
        this.shift = rand.nextInt(prime);
        createTable();
    }

    /**
     * Creates a hash table with given capacity and prime factor 109345121.
     */
    public AbstractHashMap(int cap) {
        this(cap, 109345121);
    }  // default prime

    /**
     * Creates a hash table with capacity 17 and prime factor 109345121.
     */
    public AbstractHashMap() {
        this(17);
    }                     // default capacity

    // public methods

    /**
     * Tests whether the map is empty.
     *
     * @return true if the map is empty, false otherwise
     */
    @Override
    public int size() {
        return n;
    }

    /**
     * Returns the value associated with the specified key, or null if no such entry exists.
     *
     * @param key the key whose associated value is to be returned
     * @return the associated value, or null if no such entry exists
     */
    @Override
    public V get(K key) {
        return bucketGet(hashValue(key), key);
    }

    /**
     * Removes the entry with the specified key, if present, and returns
     * its associated value. Otherwise does nothing and returns null.
     *
     * @param key the key whose entry is to be removed from the map
     * @return the previous value associated with the removed key, or null if no such entry exists
     */
    @Override
    public V remove(K key) {
        V toRemove = bucketGet(hashValue(key), key);
        if (toRemove != null) {
            bucketRemove(hashValue(key), key);
            n--;
            return toRemove;
        }
        return null;
    }

    /**
     * Associates the given value with the given key. If an entry with
     * the key was already in the map, this replaced the previous value
     * with the new one and returns the old value. Otherwise, a new
     * entry is added and null is returned.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the key (or null, if no such entry)
     */
    @Override
    public V put(K key, V value) {
        V oldvalue = bucketGet(hashValue(key), key);
        bucketPut(hashValue(key), key, value);
        return oldvalue;
    }

    // private utilities

    /**
     * Hash function applying MAD method to default hash code.
     */
    private int hashValue(K key) {
    	return (int) (( Math.abs(key.hashCode() * scale + shift) % prime ) % capacity);
    }



    /**
     * Updates the size of the hash table and rehashes all entries.
     */
    private void resize(int newCap) {
        this.capacity = newCap;
    }

    // protected abstract methods to be implemented by subclasses

    /**
     * Creates an empty table having length equal to current capacity.
     */
    protected abstract void createTable();

    /**
     * Returns value associated with key k in bucket with hash value h.
     * If no such entry exists, returns null.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @return associate value (or null, if no such entry)
     */
    protected abstract V bucketGet(int h, K k);

    /**
     * Associates key k with value v in bucket with hash value h, returning
     * the previously associated value, if any.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @param v the value to be associated
     * @return previous value associated with k (or null, if no such entry)
     */
    protected abstract V bucketPut(int h, K k, V v);

    /**
     * Removes entry having key k from bucket with hash value h, returning
     * the previously associated value, if found.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @return previous value associated with k (or null, if no such entry)
     */
    protected abstract V bucketRemove(int h, K k);
}
