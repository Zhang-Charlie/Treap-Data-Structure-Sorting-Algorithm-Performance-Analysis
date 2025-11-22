package hashtable;

import interfaces.Entry;
import interfaces.Position;
import utils.MapEntry;

import java.io.IOException;
import java.util.ArrayList;

public class ProbeHashMap<K extends Comparable<K>, V> extends AbstractHashMap<K, V> {
    private MapEntry<K, V>[] table;
    private MapEntry<K,V> DEFUNCT = new MapEntry<>(null, null);
    public ProbeHashMap() {
        super();
    }

    /** Creates a hash table with given capacity and prime factor 109345121. */
    public ProbeHashMap(int cap) {
        super(cap);
    }

    /** Creates a hash table with the given capacity and prime factor. */
    public ProbeHashMap(int cap, int p) {
        super(cap, p);
    }
    @Override
    protected void createTable() {
        table = new MapEntry[capacity];
    }

    int findSlot(int h, K k) {
        // TODO
        return 0;

    }
    @Override
    protected V bucketGet(int h, K k) {
        // TODO
        return null;
    }

    @Override
    protected V bucketPut(int h, K k, V v) {
        // TODO
        return null;
    }

    @Override
    protected V bucketRemove(int h, K k) {
        // TODO
        return null;
    }

    /** Returns true if location is either empty or the "defunct" sentinel. */
    private boolean isAvailable(int j) {
        // TODO
        return false;
    }
    
    @Override
    public Iterable<Entry<K,V>> entrySet() {
        // TODO
        return null;
    }

    @Override
    public double loadFactor() {
        // TODO
        return 0;
    }

    @Override
    public int numCollisions() {
        // TODO
        return 0;
    }
}
