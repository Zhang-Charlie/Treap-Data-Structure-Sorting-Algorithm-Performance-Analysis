package tree;

import interfaces.Entry;
import interfaces.Position;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * An implementation of a sorted map using a red-black tree.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class RBTreeMap<K extends Comparable<K>,V> extends TreeMap<K,V> {

    /** Constructs an empty map using the natural ordering of keys. */
    public RBTreeMap() { super(); }

    /**
     * Constructs an empty map using the given comparator to order keys.
     * @param comp comparator defining the order of keys in the map
     */
    public RBTreeMap(Comparator<K> comp) { super(comp); }

    // we use the inherited aux field with convention that 0=black and 1=red
    // (note that new leaves will be black by default, as aux=0)
    private boolean isBlack(Position<Entry<K,V>> p) { return tree.getAux(p)==0;}

    private boolean isRed(Position<Entry<K,V>> p) { return tree.getAux(p)==1; }

    private void makeBlack(Position<Entry<K,V>> p) { tree.setAux(p, 0); }

    private void makeRed(Position<Entry<K,V>> p) { tree.setAux(p, 1); }

    private void setColor(Position<Entry<K,V>> p, boolean toRed) {
        tree.setAux(p, toRed ? 1 : 0);
    }

    /** Overrides the TreeMap rebalancing hook that is called after an insertion. */
    protected void rebalanceInsert(Position<Entry<K,V>> p) throws IOException {
        if (!tree.isRoot(p)) {
            makeRed(p);                   // the new internal node is initially colored red
            resolveRed(p);                // but this may cause a double-red problem
        }
    }

    /** Remedies potential double-red violation above red position p. */
    private void resolveRed(Position<Entry<K,V>> p) throws IOException {
        //System.out.println("RBTreeMap::resolveRed before: " + p + "\n" + this.toBinaryTreeString());
        //this.toTikz("");

        Position<Entry<K,V>> parent,uncle,middle,grand; // used in case analysis
        parent = tree.parent(p);
        if (isRed(parent)) {                              // double-red problem exists
            uncle = tree.sibling(parent);
            if (isBlack(uncle)) {                           // Case 1: misshapen 4-node
                middle = restructure(p);                      // do trinode restructuring
                makeBlack(middle);
                makeRed(tree.left(middle));
                makeRed(tree.right(middle));
                //System.out.println("RBTreeMap::resolveRed before: " + p + "\n" + this.toBinaryTreeString());
                //this.toTikz("");
            } else {                                        // Case 2: overfull 5-node
                makeBlack(parent);                            // perform recoloring
                makeBlack(uncle);
                grand = tree.parent(parent);
                if (!tree.isRoot(grand)) {
                    makeRed(grand);                             // grandparent becomes red
                    resolveRed(grand);                          // recur at red grandparent
                    //System.out.println("RBTreeMap::resolveRed before: " + p + "\n" + this.toBinaryTreeString());
                    //this.toTikz("");
                }
            }
        }
        //System.out.println("RBTreeMap::resolveRed before: " + p + "\n" + this.toBinaryTreeString());
        //this.toTikz("");
    }

    /** Overrides the TreeMap rebalancing hook that is called after a deletion. */
    @Override
    protected void rebalanceDelete(Position<Entry<K,V>> p) throws IOException {
        if (isRed(p))                        // deleted parent was black
            makeBlack(p);                      // so this restores black depth
        else if (!tree.isRoot(p)) {
            Position<Entry<K,V>> sib = tree.sibling(p);
            if (tree.isInternal(sib) && (isBlack(sib) || tree.isInternal(tree.left(sib))))
                remedyDoubleBlack(p);            // sib's subtree has nonzero black height
        }
    }

    /** Remedies a presumed double-black violation at the given (nonroot) position. */
    private void remedyDoubleBlack(Position<Entry<K,V>> p) throws IOException {
        Position<Entry<K,V>> z = tree.parent(p);
        Position<Entry<K,V>> y = tree.sibling(p);
        if (isBlack(y)) {
            if (isRed(tree.left(y)) || isRed(tree.right(y))) { // Case 1: trinode restructuring
                Position<Entry<K,V>> x = (isRed(tree.left(y)) ? tree.left(y) : tree.right(y));
                Position<Entry<K,V>> middle = restructure(x);
                setColor(middle, isRed(z)); // root of restructured subtree gets z's old color
                makeBlack(tree.left(middle));
                makeBlack(tree.right(middle));
            } else {                           // Case 2: recoloring
                makeRed(y);
                if (isRed(z))
                    makeBlack(z);                  // problem is resolved
                else if (!tree.isRoot(z))
                    remedyDoubleBlack(z);          // propagate the problem
            }
        } else {                             // Case 3: reorient 3-node
            rotate(y);
            makeBlack(y);
            makeRed(z);
            remedyDoubleBlack(p);              // restart the process at p
        }
    }

    /** Ensure that current tree structure is valid RB tree (for debugging only)*/
    private boolean sanityCheck() {
        if (sanityRecurse(tree.root()) == -1) {
            System.out.println("VIOLATION of RB tree properties");
            //dump();
            return false;
        } else
            return true;
    }

    /** Returns black depth of subtree, if valid, or -1 if invalid. */
    private int sanityRecurse(Position<Entry<K,V>> p) {
        if (tree.isExternal(p)) {
            if (isRed(p)) return -1;      // invalid; should be black
            else return 0;                // valid, with black-depth 0
        } else {
            if (tree.isRoot(p) && isRed(p)) return -1;    // root must be black
            Position<Entry<K,V>> left = tree.left(p);
            Position<Entry<K,V>> right = tree.right(p);
            if (isRed(p) && (isRed(left) || isRed(right))) return -1;   // cannot have double red

            int a = sanityRecurse(left);
            if (a == -1) return -1;
            int b = sanityRecurse(right);
            if (a != b) return -1;          // two subtrees must have identical black depth

            return a + (isRed(p) ? 0 : 1);   // our black depth might be one greater
        }
    }
//    public void toTikz(String fname) throws IOException {
//        StringBuffer s = new StringBuffer();
////        var positerable = breadthfirst();
////        ArrayList<Position<E>> positions = new ArrayList<>();
////        positerable.forEach(positions::add);
//
//        String header = """
//                %\\documentclass[a4,landscape,margin=1mm]{article}
//                \\documentclass{standalone}
//                \\usepackage[utf8]{luainputenc}
//                \\usepackage{luatextra, luaotfload}
//                \\usepackage[]{unicode-math} %
//
//                \\usepackage{graphicx}
//                \\usepackage{adjustbox}
//                \\usepackage{tikz}
//                \\usetikzlibrary{graphs,graphdrawing,arrows.meta}
//                \\usegdlibrary{trees}
//                \\usepackage{forest}
//                \\usepackage{fontspec}
//                \\setmainfont{Hack}
//                \\pagestyle{empty}
//                \\begin{document}%
//                \\tikzstyle{vertex}=[draw,fill=black!10,circle,minimum size=15pt,inner sep=0pt]
//                \\tikzstyle{nullvertex}=[draw,fill=black!5,circle,minimum size=2pt,inner sep=0pt]
//                \\tikzstyle{state}=[draw,fill=black!5,circle,minimum size=10pt,inner sep=0pt]
//                \\tikzstyle{marked}=[draw=green]
//
//                \\tikzset{
//                    every node/.style={draw, circle, very thick,minimum size=2mm},
//                    marked/.style={fill=harlequin!50, circle},
//                    marked2/.style={fill=cadmiumorange!50, circle},
//                    null/.style={draw, fill=black!5, minimum size=2pt},
//                    invisible/.style={opacity=0},
//                }
//
//                \\pagestyle{empty}
//                %\\begin{figure}[!h]
//                %\\centering
//                \\begin{adjustbox}{width=16cm,keepaspectratio}
//                \\begin{tikzpicture}
//                  \\graph[binary tree layout, grow=down, fresh nodes,
//                    level/.style={
//                      sibling
//                      distance = 3cm/#1,
//                      level distance = 0.8cm
//                  }] {%
//                        """;
//        String footer = """
//                };%
//                \\end{tikzpicture}
//                \\end{adjustbox}
//                %\\end{figure}
//                \\end{document}
//                """;
//        String res = toTikzNode(root());
//        System.out.println("tikz:\n" + res);
//        try {
//            Path path = Paths.get(fname);
//            Files.write(path, (header + res + footer).getBytes());
//        } catch(Exception e) {
//            System.out.println("no file " + fname);
//        }
//    }
//
//    public String toTikzNode(Position<Entry<K,V>> node) {
//        StringBuffer s = new StringBuffer();
//        if(node == null) {
//            return "";
//        }
//
//        String res = "";
//        if(node.getElement() != null) {
//            res = node.toString() + (isRed(node) ? "[nodered] " : "[nodeblack] ");
//        }
//
//        s.append(String.format("%s -- { ", res));
//        s.append(toTikzNode(left(node)) + ", " + toTikzNode(right(node)));
//        s.append("} ");
//        return s.toString();
//    }

    public String toString() {
        return new BinaryTreePrinter<>(tree).print();
    }
    
    public static void main(String [] args) throws IOException {
        //main_lecture_1();
    }

//    public static void main_lecture_1() throws IOException {
//        TreeMap<Integer, Integer> tree = new RBTreeMap<>();
//       // Integer [] arr = new Integer[] {5,3,10,2,4,7,11,1,6,9,12,8};
//        //Integer [] arr = {73, 55, 86, 88, 68, 65, 2, 42, 91, 20, 22, 75, 57, 37, 49, 76, 67, 78, 10, 25, 70, 29, 71, 50, 52, 61, 95, 4, 79, 1, 26, 48, 81, 45, 9, 56, 97, 3, 84, 16, 34, 64, 41, 58, 24, 35, 96, 5, 100, 31, 27, 82, 11, 93, 6, 14, 43, 33, 51, 30, 8, 17, 66, 18, 7, 59, 12, 19, 83, 85, 46, 77, 99, 47, 87};
//        //for(Integer i : arr) tree.put(i,i);
//        Random rnd = new Random();
//        rnd.setSeed(1025);
//        //int n_max = 500, n = 20;
//        int n_max = 500, n = 200;
//        List<Integer> arr = rnd.ints(1, n_max)
//                .limit(n)
//                .distinct()
//                .boxed()
//                .sorted()
//                .collect(Collectors.toList());
//
//        for(Integer i : arr) {
//            tree.put(i,i);
//        }
//        System.out.println("inorder: " + tree.tree.inorder());
//        System.out.println(tree.toString());
//        tree.toTikz("");
//
//        tree.put(13,13);
//        System.out.println(tree.toString());
//        tree.toTikz("");
//    }
}
