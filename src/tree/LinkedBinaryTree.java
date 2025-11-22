package tree;

import interfaces.BinaryTree;
import interfaces.List;
import interfaces.Position;
import list.SinglyLinkedList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.random.*;

/**
 * Concrete implementation of a binary tree using a node-based, linked
 * structure.
 */
public class LinkedBinaryTree<E extends Comparable<E>> implements BinaryTree<E> {


    private static final Random rand = new Random();

    protected Node<E> root = null; // root of the tree

    // LinkedBinaryTree instance variables
    protected int size = 0; // number of nodes in the tree

    public LinkedBinaryTree() {
    } // constructs an empty binary tree


    public static <E extends Comparable<E>> LinkedBinaryTree<E> makeRandom(int n, E[] arr) {
        LinkedBinaryTree<E> bt = new LinkedBinaryTree<>();
        bt.root = randomTree(null, 0, n - 1, arr);
        bt.size = n;
        return bt;
    }

    public static LinkedBinaryTree<Integer> makeRandom(int n) {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();
        Integer[] arr = IntStream.range(0, n).boxed().toArray(Integer[]::new);
        bt.root = randomTree(null, 0, n - 1, arr);
        bt.size = n;
        return bt;
    }

    public static <E> Node<E> randomTree(Node<E> parent, int first, int last, E[] arr) {
        java.util.Random rnd = new java.util.Random();

        if (first > last) return null;
        else {
            int treeSize = last - first + 1;
            //int leftCount = rnd.nextInt(Math.max(1, (int) (0.2 * treeSize))); // for unbalanced trees
            int leftCount = rnd.nextInt(treeSize);
            int rightCount = treeSize - leftCount - 1;
            int index = first + leftCount;
            Node<E> n = new Node<E>(arr[index], parent, null, null);

            n.setLeft(randomTree(n, first, first + leftCount - 1, arr));
            n.setRight(randomTree(n, first + leftCount + 1, last, arr));
            return n;
        }
    }


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the Position of p's sibling (or null if no sibling exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the sibling (or null if no sibling exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    public Position<E> sibling(Position<E> p) {
        Position<E> parent = parent(p);
        if (parent == null) return null;                  // p must be the root
        if (p == left(parent))                            // p is a left child
            return right(parent);                           // (right child might be null)
        else                                              // p is a right child
            return left(parent);                            // (left child might be null)
    }

    @Override
    public Iterator<E> iterator() {
        return new ElementIterator();
    }

    /**
     * Returns true if Position p has one or more children.
     *
     * @param p A valid Position within the tree
     * @return true if p has at least one child, false otherwise
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    @Override
    public boolean isInternal(Position<E> p) {
        return numChildren(p) > 0;
    }

    /**
     * Adds positions of the subtree rooted at Position p to the given
     * snapshot using an inorder traversal
     *
     * @param p        Position serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void inorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        if (left(p) != null) inorderSubtree(left(p), snapshot);
        if (p.getElement() != null) snapshot.addLast(p);
        if (right(p) != null) inorderSubtree(right(p), snapshot);
    }

    /**
     * Adds positions of the subtree rooted at Position p to the given
     * snapshot using a preorder traversal
     *
     * @param p        Position serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void preorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        if(p.getElement() != null) {
            snapshot.addLast(p);
        }// for preorder, we add position p before exploring subtrees
        for (Position<E> c : children(p)) {
            preorderSubtree(c, snapshot);
        }
    }

    /**
     * Returns an iterable collection of positions of the tree, reported in preorder.
     *
     * @return iterable collection of the tree's positions in preorder
     */
    public Iterable<Position<E>> preorder() {
        //List<Position<E>> snapshot = new ArrayList<>();
        List<Position<E>> snapshot = new SinglyLinkedList<>();

        if (!isEmpty()) {
            preorderSubtree(root(), snapshot);   // fill the snapshot recursively
        }
        return snapshot;
    }

    /**
     * Returns an iterable collection of positions of the tree, reported in inorder.
     *
     * @return iterable collection of the tree's positions reported in inorder
     */
    public Iterable<Position<E>> inorder() {
        List<Position<E>> snapshot = new SinglyLinkedList<>();
        if (!isEmpty()) {
            inorderSubtree(root(), snapshot);   // fill the snapshot recursively
        }
        return snapshot;
    }

    /**
     * Adds positions of the subtree rooted at Position p to the given
     * snapshot using a postorder traversal
     *
     * @param p        Position serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void postorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        for (Position<E> c : children(p))
            postorderSubtree(c, snapshot);
        snapshot.addLast(p);
    }

    /**
     * Returns an iterable collection of positions of the tree, reported in postorder.
     *
     * @return iterable collection of the tree's positions in postorder
     */
    public Iterable<Position<E>> postorder() {
        List<Position<E>> snapshot = new SinglyLinkedList<>();
        if (!isEmpty()) postorderSubtree(root(), snapshot);   // fill the snapshot recursively
        return snapshot;
    }

    public Iterable<Position<E>> positions() {
        return inorder();
        //return breadthfirst();
    }

    /**
     * Returns the number of levels separating Position p from the root.
     *
     * @param p A valid Position within the tree
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public int depth(Position<E> p) throws IllegalArgumentException {
        if (isRoot(p)) {
            return 0;
        } else {
            return 1 + depth(parent(p));
        }
    }

    /**
     * Returns the height of the tree.
     * <p>
     * Note: This implementation works, but runs in O(n^2) worst-case time.
     */
    private int heightBad() {             // works, but quadratic worst-case time
        int h = 0;
        for (Position<E> p : positions()) {
            if (isExternal(p)) {                // only consider leaf positions
                h = Math.max(h, depth(p));
            }
        }
        return h;
    }

    /**
     * Returns the height of the subtree rooted at Position p.
     *
     * @param p A valid Position within the tree
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public int height(Position<E> p) throws IllegalArgumentException {
        int h = 0;                          // base case if p is external
        for (Position<E> c : children(p)) {
            h = Math.max(h, 1 + height(c));
        }
        return h;
    }

    /**
     * Returns true if Position p represents the root of the tree.
     *
     * @param p A valid Position within the tree
     * @return true if p is the root of the tree, false otherwise
     */
    public boolean isRoot(Position<E> p) {
        return p == root();
    }
    // nonpublic utility

    /**
     * Returns true if Position p does not have any children.
     *
     * @param p A valid Position within the tree
     * @return true if p has zero children, false otherwise
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public boolean isExternal(Position<E> p) {
        Node<E> node = validate(p);
        return node.left == null && node.right == null;
    }
    /**
     * Returns an iterable collection of the Positions representing p's children.
     *
     * @param p A valid Position within the tree
     * @return iterable collection of the Positions of p's children
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public Iterable<Position<E>> children(Position<E> p) {
        List<Position<E>> snapshot = new SinglyLinkedList<>();    // max capacity of 2
        if (p != null && left(p) != null) snapshot.addLast(left(p));
        if (p != null && right(p) != null) snapshot.addLast(right(p));
        return snapshot;
    }

    /**
     * Returns the number of children of Position p.
     *
     * @param p A valid Position within the tree
     * @return number of children of Position p
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public int numChildren(Position<E> p) {
        int count = 0;
        for (Position child : children(p)) {
            count++;
        }
        return count;
    }

    // Function to find minimum value node in a given BST
    private Node<E> findMinimum(Node<E> n) {
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }

    // Function to find minimum value node in a given BST
    private Node<E> findMaximum(Node<E> n) {
        while (n.right != null) {
            n = n.right;
        }
        return n;
    }

    // Recursive function to find an inorder successor
    private Node<E> inorderSuccessor(Node<E> node, Node<E> succ, E key) {
        // base case
        if (node == null) {
            return succ;
        }
        // if a node with the desired value is found, the successor is the minimum
        if (node.element.compareTo(key) == 0) {
            if (node.right != null) {
                return findMinimum(node.right);
            }
        }
        // if the given key is less than the root node, consider the left subtree
        else if (key.compareTo(node.element) < 0) {
            succ = node;
            return inorderSuccessor(node.left, succ, key);
        }
        // if the given key is more than the root node, consider the right subtree
        else {
            return inorderSuccessor(node.right, succ, key);
        }
        return succ;
    }

    private Node<E> inorderPredecessor(Node<E> node, Node<E> pred, E key) {
        // base case
        if (node == null) {
            return pred;
        }
        // if a node with the desired value is found, the successor is the minimum
        if (node.element.compareTo(key) == 0) {
            if (node.left != null) {
                return findMaximum(node.left);
            }
        }
        // if the given key is more than the root node, consider the right subtree
        else if (key.compareTo(node.element) < 0) {
            return inorderPredecessor(node.left, pred, key);
        }
        // if the given key is less than the root node, consider the left subtree
        else {
            pred = node;
            return inorderPredecessor(node.right, pred, key);
        }
        return pred;
    }

    public Position<E> inorderSuccessor(E key) {
        return inorderSuccessor(root, null, key);
    }

    public Position<E> inorderPredecessor(E key) {
        return inorderPredecessor(root, null, key);
    }

    private int getDiameter(Node<E> n, AtomicInteger diameter) {
        if (n == null) {
            return 0;
        }

        int h_left = getDiameter(n.left, diameter);
        int h_right = getDiameter(n.right, diameter);

        //System.out.println("getHeight: " + n.element + " " + h_left + " " + h_right);
        // update the answer, because diameter = h_left + h_right + 1
        diameter.set(Math.max(diameter.get(), 1 + h_left + h_right));
        return 1 + Math.max(h_left, h_right);
    }

    public int diameter() {
        if (root == null) {
            return 0;
        }
        AtomicInteger result = new AtomicInteger(0);
        int h = getDiameter(root, result);
        return result.get();
    }

    /**
     * Returns an iterable collection of positions of the tree in breadth-first order.
     *
     * @return iterable collection of the tree's positions in breadth-first order
     */
    public Iterable<Position<E>> breadthfirst() {
        List<Position<E>> snapshot = new SinglyLinkedList<>();
        if (!isEmpty()) {
            java.util.Queue<Position<E>> q = new java.util.LinkedList<>();
            q.add(root());                 // start with the root
            while (!q.isEmpty()) {
                Position<E> p = q.remove();     // remove from front of the queue
                snapshot.addLast(p);                      // report this position
                children(p).forEach(c -> q.add(c));           // add children to back of queue
            }
        }
        return snapshot;
    }


    public void constructTree(E[] inorder, E[] preorder) {

    }

    private Node<E> constructTree_helper(E[] inorder, E[] preorder, int left, int right) {
        return null;
    }

    public void construct(E[] inorder, E[] preorder) {
        // java.util.Map<E, Integer> m = new java.util.TreeMap<E, Integer>();
        // A, 5
        // B, 1
        // for(int i = 0; i < inorder.length; i++) m.put(inorder[i], i);
        // this.root = construct_tree(pre_arr, 0, in_arr.length-1, m);
        this.root = construct_tree(inorder, preorder, 0, preorder.length, 0, inorder.length);
    }

    private Node<E> construct_tree(E[] inorder, E[] preorder, int pStart, int pEnd, int iStart, int iEnd) {
        if (pStart >= pEnd || iStart >= iEnd) return null;

        E element = preorder[pStart];
        Node<E> n = createNode(element, null, null, null);
        size++;
        for (int i = iStart; i < iEnd; i++) {
            if (element == inorder[i]) {
                n.left = construct_tree(inorder, preorder, pStart + 1, i - iStart + (pStart + 1), iStart, i);
                n.right = construct_tree(inorder, preorder, (i + 1) - iEnd + pEnd, pEnd, i + 1, iEnd);
                if (n.left != null) n.left.parent = n;
                if (n.right != null) n.right.parent = n;
                break;
            }
        }
        return n;
    }


    /**
     * Factory function to create a new node storing element e.
     */
    protected Node<E> createNode(E e, Node<E> parent, Node<E> left, Node<E> right) {
        return new Node<E>(e, parent, left, right);
    }

    /**
     * Verifies that a Position belongs to the appropriate class, and is not one
     * that has been previously removed. Note that our current implementation does
     * not actually verify that the position belongs to this particular list
     * instance.
     *
     * @param p a Position (that should belong to this tree)
     * @return the underlying Node instance for the position
     * @throws IllegalArgumentException if an invalid position is detected
     */
    protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if (!(p instanceof Node<E> node)) throw new IllegalArgumentException("Not valid position type");
        // safe cast
        if (node.getParent() == node) // our convention for defunct node
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }

    /**
     * Returns the number of nodes in the tree.
     *
     * @return number of nodes in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Returns the root Position of the tree (or null if tree is empty).
     *
     * @return root Position of the tree (or null if tree is empty)
     */
    public Position<E> root() {
        return root;
    }

    /**
     * Returns the Position of p's parent (or null if p is root).
     *
     * @param p A valid Position within the tree
     * @return Position of p's parent (or null if p is root)
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public Position<E> parent(Position<E> p) throws IllegalArgumentException {
        return ((Node<E>) p).getParent();
    }

    /**
     * Returns the Position of p's left child (or null if no child exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the left child (or null if no child exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    public Position<E> left(Position<E> p) throws IllegalArgumentException {
        return ((Node<E>) p).getLeft();
    }

    // update methods supported by this class

    /**
     * Returns the Position of p's right child (or null if no child exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the right child (or null if no child exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    public Position<E> right(Position<E> p) throws IllegalArgumentException {
        return ((Node<E>) p).getRight();
    }

    /**
     * Places element e at the root of an empty tree and returns its new Position.
     *
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalStateException if the tree is not empty
     */
    public Position<E> addRoot(E e) throws IllegalStateException {
        if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
        this.root = createNode(e, null, null, null);
        this.size = 1;
        return this.root;
    }

    /*
     * Create a detached node!
     */
    public Position<E> add(E e, Position<E> parent, Position<E> left, Position<E> right) {
        Node<E> node = createNode(e, (Node<E>) parent, (Node<E>) left, (Node<E>) right);
        this.size += 1;
        return node;
    }

    /**
     * Creates a new left child of Position p storing element e and returns its
     * Position.
     *
     * @param p the Position to the left of which the new element is inserted
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     * @throws IllegalArgumentException if p already has a left child
     */
    public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> n = validate(p);
        if(n.getLeft() != null){
            throw new IllegalStateException("Left node already exists");
        }
        n.setLeft(createNode(e, n, null, null));
        this.size += 1;
        return n.getLeft();
    }
    public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> n = validate(p);
        if(n.getRight() != null){
            throw new IllegalStateException("Right node already exists");
        }
        n.setRight(createNode(e, n, null, null));
        this.size += 1;
        return n.getRight();
    }

    /**
     * Replaces the element at Position p with element e and returns the replaced
     * element.
     *
     * @param p the relevant Position
     * @param e the new element
     * @return the replaced element
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public E set(Position<E> p, E e) throws IllegalArgumentException {
        E old = p.getElement();
        p.setElement(e);
        return old;
    }

    public void setRoot(Position<E> e) throws IllegalArgumentException {
        if(this.isEmpty()){
            throw new IllegalArgumentException("Root does not exist");
        }
        this.root = validate(e);
    }

    /**
     * Removes the node at Position p and replaces it with its child, if any.
     *
     * @param p the relevant Position
     * @return element that was removed
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     * @throws IllegalArgumentException if p has two children.
     */
    public E remove(Position<E> p) throws IllegalArgumentException {
        Node<E> n = (Node<E>) p;
        if (numChildren(n) == 2) {
            throw new IllegalArgumentException("Cant remove node with 2 children");
        }

        // find the child node
        Node<E> child = n.getLeft() != null ? n.getLeft() : n.getRight();
        if (child != null) {
            child.setParent(n.getParent()); // the child's grandparent becomes its parent
        }
        if (n == root) {
            root = child;
        } else {
            Node<E> parent = n.getParent();
            if (n == parent.getLeft()) {
                parent.setLeft(child);
            } else {
                parent.setRight(child);
            }
        }
        size -= 1;
        return n.getElement();
    }

    public String toString() {
        return positions().toString();
    }

    public void createLevelOrder(ArrayList<E> l) {
        root = createLevelOrderHelper(l, root, 0);
    }

    private Node<E> createLevelOrderHelper(java.util.ArrayList<E> l, Node<E> p, int i) {
        if (i < l.size()) {
            Node<E> n = null;
            if (l.get(i) != null) {
                n = createNode(l.get(i), p, null, null);
                n.left = createLevelOrderHelper(l, n, 2 * i + 1);
                n.right = createLevelOrderHelper(l, n, 2 * i + 2);
                ++size;
            }
            return n;
        }
        return p;
    }

    public void createLevelOrder(E[] arr) {
        root = createLevelOrderHelper(arr, root, 0);
    }

    private Node<E> createLevelOrderHelper(E[] arr, Node<E> parent, int i) {
        // corrected 2024
        if (i < arr.length && arr[i] != null) {
            Node<E> n = createNode(arr[i], parent, null, null);
            n.left = createLevelOrderHelper(arr, n, 2 * i + 1);
            n.right = createLevelOrderHelper(arr, n, 2 * i + 2);
            ++size;
            return n;
        }
        return null;
    }

    public String toBinaryTreeString() {
        BinaryTreePrinter<E> btp = new BinaryTreePrinter<>(this);
        return btp.print();
    }

    /*
     * Nested static class for a binary tree node.
     */
    protected static class Node<E> implements Position<E> {
        private E element;
        private Node<E> left, right, parent;

        public Node(E e, Node<E> p, Node<E> l, Node<E> r) {
            element = e;
            left = l;
            right = r;
            parent = p;

        }

        // accessor
        public E getElement() {
            return element;
        }

        // modifiers
        public void setElement(E e) {
            element = e;
        }

        public Node<E> getLeft() {
            return left;
        }

        public void setLeft(Node<E> n) {
            left = n;
        }

        public Node<E> getRight() {
            return right;
        }

        public void setRight(Node<E> n) {
            right = n;
        }

        public Node<E> getParent() {
            return parent;
        }

        public void setParent(Node<E> n) {
            parent = n;
        }



        public String toString() {
            // (e)
            StringBuilder sb = new StringBuilder();
            if (element == null) {
                sb.append("\u29B0");
            } else {
                sb.append(element);
            }
            // sb.append(" l:").append(left.element).append(" r:").append(right.element);
            // sb.append();
            return sb.toString();
        }
    }

    /* This class adapts the iteration produced by positions() to return elements. */
    private class ElementIterator implements Iterator<E> {
        Iterator<Position<E>> posIterator = positions().iterator();

        public boolean hasNext() {
            return posIterator.hasNext();
        }

        public E next() {
            return posIterator.next().getElement();
        }

        public void remove() {
            posIterator.remove();
        }
    }

}
