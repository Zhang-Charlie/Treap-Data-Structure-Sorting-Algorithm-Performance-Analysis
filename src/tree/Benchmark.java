package tree;

import interfaces.Entry;
import interfaces.Position;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class Benchmark {

    public static void main(String[] args) throws IOException {
        // Test sizes
        int[] sizes = {100, 500, 1000, 2000, 5000, 10000};

        // Test patterns
        String[] patterns = {"random", "ascending", "descending", "partially_sorted"};

        for (int n : sizes) {
            System.out.println("\n=== Benchmarking with n = " + n + " ===");

            for (String pattern : patterns) {
                System.out.println("\nPattern: " + pattern);
                //generate data
                Integer[] data = generateData(n, pattern);

                // Run benchmarks
                benchmarkTreap(data);
                benchmarkAVLTreeMap(data);
                benchmarkTreeMap(data);
            }
        }
    }

    private static Integer[] generateData(int n, String pattern) {
        Integer[] data = new Integer[n];

        switch (pattern) {
            case "random":
                Random random = new Random();
                for (int i = 0; i < n; i++) {
                    data[i] = random.nextInt(n * 10);
                }
                break;

            case "ascending":
                for (int i = 0; i < n; i++) {
                    data[i] = i;
                }
                break;

            case "descending":
                for (int i = 0; i < n; i++) {
                    data[i] = n - i;
                }
                break;

            case "partially_sorted":
                for (int i = 0; i < n; i++) {
                    data[i] = i;
                }
                // Swap every 10th element
                for (int i = 9; i < n; i += 10) {
                    int temp = data[i];
                    data[i] = data[i-5];
                    data[i-5] = temp;
                }
                break;
        }

        return data;
    }


    private static void benchmarkTreap(Integer[] data) {
        System.out.println("\nTreap:");
        Treap<Integer, Integer> treap = new Treap<>();

        // Insertion
        long start = System.nanoTime();
        for (Integer key : data) {
            try {
                treap.put(key, key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long end = System.nanoTime();
        System.out.printf("Insertion time: %.3f ms%n", (end - start) / 1_000_000.0);

        // Search (successful)
        start = System.nanoTime();
        for (Integer key : data) {
            try {
                treap.get(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        end = System.nanoTime();
        System.out.printf("Successful search time: %.3f ms%n", (end - start) / 1_000_000.0);

        // Search (unsuccessful)
        start = System.nanoTime();
        for (Integer key : data) {
            try {
                treap.get(key + data.length); // Keys that don't exist
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        end = System.nanoTime();
        System.out.printf("Unsuccessful search time: %.3f ms%n", (end - start) / 1_000_000.0);

        // In-order traversal
        start = System.nanoTime();
        Iterable<Position<Entry<Integer, Integer>>> entries = treap.tree.inorder();
        for (Position<Entry<Integer, Integer>> entry : entries) {
            // Just iterate
        }
        end = System.nanoTime();
        System.out.printf("Traversal time: %.3f ms%n", (end - start) / 1_000_000.0);

        // Deletion
//        start = System.nanoTime();
//        for (Integer key : data) {
//            try {
//                treap.remove(key);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        end = System.nanoTime();
//        System.out.printf("Deletion time: %.3f ms%n", (end - start) / 1_000_000.0);
    }

    private static void benchmarkAVLTreeMap(Integer[] data) throws IOException {
        System.out.println("\nAVLTreeMap:");
        AVLTreeMap<Integer, Integer> avl = new AVLTreeMap<>();

        // Insertion
        long start = System.nanoTime();
        for (Integer key : data) {
            avl.put(key, key);
        }
        long end = System.nanoTime();
        System.out.printf("Insertion time: %.3f ms%n", (end - start) / 1_000_000.0);

        // Search (successful)
        start = System.nanoTime();
        for (Integer key : data) {
            avl.get(key);
        }
        end = System.nanoTime();
        System.out.printf("Successful search time: %.3f ms%n", (end - start) / 1_000_000.0);

        // Search (unsuccessful)
        start = System.nanoTime();
        for (Integer key : data) {
            avl.get(key + data.length); // Keys that don't exist
        }
        end = System.nanoTime();
        System.out.printf("Unsuccessful search time: %.3f ms%n", (end - start) / 1_000_000.0);

        // In-order traversal
        start = System.nanoTime();
        for (Entry<Integer, Integer> entry : avl.entrySet()) {
            // Just iterate
        }
        end = System.nanoTime();
        System.out.printf("Traversal time: %.3f ms%n", (end - start) / 1_000_000.0);

//        // Deletion
//        start = System.nanoTime();
//        for (Integer key : data) {
//            avl.remove(key);
//        }
//        end = System.nanoTime();
//        System.out.printf("Deletion time: %.3f ms%n", (end - start) / 1_000_000.0);
    }

    private static void benchmarkTreeMap(Integer[] data) throws IOException {
        System.out.println("\nTreeMap:");
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();

        // Insertion
        long start = System.nanoTime();
        for (Integer key : data) {
            treeMap.put(key, key);
        }
        long end = System.nanoTime();
        System.out.printf("Insertion time: %.3f ms%n", (end - start) / 1_000_000.0);

        // Search (successful)
        start = System.nanoTime();
        for (Integer key : data) {
            treeMap.get(key);
        }
        end = System.nanoTime();
        System.out.printf("Successful search time: %.3f ms%n", (end - start) / 1_000_000.0);

        // Search (unsuccessful)
        start = System.nanoTime();
        for (Integer key : data) {
            treeMap.get(key + data.length); // Keys that don't exist
        }
        end = System.nanoTime();
        System.out.printf("Unsuccessful search time: %.3f ms%n", (end - start) / 1_000_000.0);

        // In-order traversal
        start = System.nanoTime();
        for (Entry<Integer, Integer> entry : treeMap.entrySet()) {
            // Just iterate
        }
        end = System.nanoTime();
        System.out.printf("Traversal time: %.3f ms%n", (end - start) / 1_000_000.0);

        // Deletion
        start = System.nanoTime();
        for (Integer key : data) {
            treeMap.remove(key);
        }
        end = System.nanoTime();
        System.out.printf("Deletion time: %.3f ms%n", (end - start) / 1_000_000.0);
    }
}