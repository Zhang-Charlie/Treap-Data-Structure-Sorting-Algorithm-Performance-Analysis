package tree;

import interfaces.Entry;
import interfaces.List;
import interfaces.Position;
import utils.MapEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Treap <K extends Comparable<K>, V> extends TreeMap<K, V> {
    //protected BalanceableBinaryTree<K,V> tree = new BalanceableBinaryTree<>();

    private final Random random;

    public Treap() {
        super();
        random = new Random();
    }

    @Override
    public V get(K key) throws IllegalArgumentException, IOException {
        Position<Entry<K, V>> p = treeSearch(tree.root(), key);
        return tree.isExternal(p) ? null : p.getElement().getValue();
    }

    @Override
    public V put(K key, V value) throws IOException {
        Entry<K, V> entry = new MapEntry<>(key, value);
        if (tree.isEmpty()) {
            tree.addRoot(entry);
            return null;
        }
        Position<Entry<K,V>> p = treeSearch(tree.root(), key);
        if(tree.isExternal(p)){
            expandExternal(p, entry);
            tree.setAux(p, random.nextInt());
            rebalanceInsert(p);
            return null;
        } else {
            V old = p.getElement().getValue();
            tree.set(p, entry);
            tree.setAux(p, random.nextInt());
            //rebalanceInsert(p);
            return old;
        }
    }

    @Override
    public V remove(K key) throws IllegalArgumentException, IOException {
        Position<Entry<K, V>> p = treeSearch(tree.root(), key);
        if (!tree.isExternal(p)) {
            V oldValue = p.getElement().getValue();
            if (tree.isInternal(tree.left(p)) && tree.isInternal(tree.right(p))) {
                Position<Entry<K, V>> r = treeMin(tree.right(p));
                tree.set(p, r.getElement());
                p = r;
            }
            Position<Entry<K, V>> leaf = tree.isExternal(tree.left(p)) ? tree.left(p) : tree.right(p);
            Position<Entry<K, V>> sibling = tree.sibling(leaf);
            tree.remove(leaf);
            tree.remove(p);
            priorityBalance(sibling);
            return oldValue;
        }
        return null;
    }
    private void priorityBalance(Position<Entry<K, V>> p) {
        // Loop as long as p is not the root and the parent has less priority than p
        while (!tree.isRoot(p) && tree.getAux(tree.parent(p)) < tree.getAux(p)) {
            tree.rotate(p);
        }
    }

    public void checkHeapSubTree(Position<Entry<K,V>> position, ArrayList<String> list){
        if (tree.isInternal(position)) {
            Position<Entry<K,V>> left = tree.left(position);
            Position<Entry<K,V>> right = tree.right(position);
            
            // Check left child
            if (tree.isInternal(left)) {
                if (tree.getAux(left) > tree.getAux(position)) {
                    list.add("Heap property violated at position " + position.getElement().getKey() + 
                            ": left child priority " + tree.getAux(left) + 
                            " > parent priority " + tree.getAux(position));
                }
                checkHeapSubTree(left, list);
            }
            
            // Check right child
            if (tree.isInternal(right)) {
                if (tree.getAux(right) > tree.getAux(position)) {
                    list.add("Heap property violated at position " + position.getElement().getKey() + 
                            ": right child priority " + tree.getAux(right) + 
                            " > parent priority " + tree.getAux(position));
                }
                checkHeapSubTree(right, list);
            }
        }
    }

    public boolean checkHeap(){
        if (tree.isEmpty()) {
            return true;
        }
        ArrayList<String> violations = new ArrayList<>();
        checkHeapSubTree(tree.root(), violations);
        if (!violations.isEmpty()) {
            System.out.println("Heap violations found:");
            for (String violation : violations) {
                System.out.println(violation);
            }
            return false;
        }
        return true;
    }

    public BalanceableBinaryTree<K,V> makeTree(){
        return new BalanceableBinaryTree<>();
    }

    @Override
    protected void rebalanceInsert(Position<Entry<K,V>> p) throws IOException {
        // Keep rotating up while not root and priority is greater than parent's priority
        while (!tree.isRoot(p) && tree.getAux(tree.parent(p)) < tree.getAux(p)) {
            tree.rotate(p);
        }
    }
}
