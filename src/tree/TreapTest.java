package tree;

import interfaces.Entry;
import interfaces.Position;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreapTest {

    @Test
    void size() throws IOException {
        Treap<Integer, String> treap = new Treap<>();

        treap.put(10, "A");
        treap.put(20, "B");
        treap.put(60, "C");
        treap.put(40, "D");
        treap.put(70, "E");
        treap.put(80, "F");
        treap.put(90, "G");
        treap.put(100, "H");
        treap.put(200, "I");

        assertEquals(treap.size(), 9);

        treap.remove(200);

        assertEquals(treap.size(), 8);

    }

    @Test
    void get() throws IOException {

        Treap<Integer, String> treap = new Treap<>();

        treap.put(10, "A");
        treap.put(20, "B");
        treap.put(60, "C");
        treap.put(40, "D");
        treap.put(70, "E");
        treap.put(80, "F");
        treap.put(90, "G");
        treap.put(100, "H");
        treap.put(200, "I");
        assertEquals(treap.get(10), "A");
        assertEquals(treap.get(100), "H");
        System.out.println(treap.tree.toBinaryTreeString());
        System.out.println(treap.tree.inorder());

    }

    @Test
    void put() throws IOException {
        Treap<Integer, String> treap = new Treap<>();

        assertNull(treap.put(10, "A"));
        assertEquals(treap.put(10, "B"), "A");
    }

    @Test
    void remove() throws IOException {
        Treap<Integer, String> treap = new Treap<>();


        treap.put(10, "A");
        treap.put(20, "B");
        treap.put(30, "C");
        treap.put(40, "D");
        treap.put(50, "E");
        treap.put(60, "F");
        treap.put(70, "G");
        treap.put(80, "H");
        treap.put(90, "I");
        treap.put(100, "J");
        Position<Entry<Integer, String>> p = treap.tree.root();
        treap.tree.setAux(p, 2100000000);
        System.out.println(treap.tree);
        assertEquals(treap.remove(10), "A");
        assertEquals(treap.remove(100), "J");
        assertEquals(treap.size(), 8);
        System.out.println(treap.tree);
    }

    @Test
    public void testHeapPropertyAfterInsert() throws IOException {
        Treap<Integer, String> treap = new Treap<>();
        treap.put(1, "one");
        treap.put(2, "two");
        assertTrue(treap.checkHeap());
    }

    @Test
    public void testBSTProperty() throws IOException {
        Treap<Integer, String> treap = new Treap<>();
        treap.put(2, "two");
        treap.put(1, "one");
        treap.put(3, "three");

        Iterable<Position<Entry<Integer, String>>> positions = treap.tree.inorder();

        List<Integer> keys = new ArrayList<>();
        for (Position<Entry<Integer, String>> pos : positions) {
            keys.add(pos.getElement().getKey());
        }

        List<Integer> expected = List.of(1, 2, 3);
        assertEquals(expected, keys);
    }

    @Test
    public void treeMin() throws IOException {
        Treap<Integer, String> treap = new Treap<>();
        treap.put(1, "one");
        treap.put(2, "two");
        treap.put(3, "three");
        treap.put(4, "four");
        treap.put(5, "five");
        treap.put(6, "six");

        assertEquals(treap.treeMin(treap.tree.root()).getElement().getKey(), 1);
    }

    @Test
    public void treeMax() throws IOException {
        Treap<Integer, String> treap = new Treap<>();
        treap.put(1, "one");
        treap.put(2, "two");
        treap.put(3, "three");
        treap.put(4, "four");
        treap.put(5, "five");
        treap.put(6, "six");

        assertEquals(6, treap.treeMax(treap.tree.root()).getElement().getKey());
    }

   }