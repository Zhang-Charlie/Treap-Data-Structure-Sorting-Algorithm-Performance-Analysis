package tree;

import interfaces.Entry;
import interfaces.Map;
import interfaces.Position;


import java.io.IOException;
import java.util.*;
//import java.util.TreeMap;
import java.util.stream.Collectors;

public class SplayTreeMap<K extends Comparable<K>, V> extends TreeMap<K, V> {

    /**
     * Constructs an empty map using the natural ordering of keys.
     */
    public SplayTreeMap() {
        super();
    }

    /**
     * Constructs an empty map using the given comparator to order keys.
     *
     * @param comp comparator defining the order of keys in the map
     */
    public SplayTreeMap(Comparator<K> comp) {
        super(comp);
    }

    /**
     * Utility used to rebalance after a map operation.
     */
    private void splay(Position<Entry<K, V>> p) throws IOException {

        while(!tree.isRoot(p)){
            Position<Entry<K, V>> parent = tree.parent(p);
            Position<Entry<K,V>> grand  = tree.parent(parent);
            if (grand == null) {
                //zig
                rotate(p);
            } else if ((parent == tree.left(grand)) == (p == tree.left(parent))) {
                //zig-zig
                rotate(parent);
                rotate(p);
            } else {
                rotate(p);
                rotate(parent);
                //zig-zag
            }
        }

    }

    /**
     * Overrides the TreeMap rebalancing hook that is called after a node access.
     * @param p
     */
    //@Override
    protected void rebalanceAccess(Position<Entry<K, V>> p) throws IOException {
        if (tree.isExternal(p)){
            p = tree.parent(p);
        }
        if (p != null){
            splay(p);
        }
    }

    /**
     * Overrides the TreeMap rebalancing hook that is called after an insertion.
     * @param p
     */
    //@Override
    protected void rebalanceInsert(Position<Entry<K, V>> p) throws IOException {
        splay(p);
    }

    /**
     * Overrides the TreeMap rebalancing hook that is called after a deletion.
     * @param p
     */
    //@Override
    protected void rebalanceDelete(Position<Entry<K, V>> p) throws IOException {
        if (!tree.isRoot(p)) splay(tree.parent(p));
    }

    public String toString() {
        return new BinaryTreePrinter<>(tree).print();
    }
    public static void main(String [] args) throws IOException {
    }
}
