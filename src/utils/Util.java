package utils;

import java.util.Random;

public class Util<T> {

    static Random rand;

    public static <T> void shuffle(T[] arr) {
        if (rand == null) {
            rand = new Random();
        }

        for (int i = arr.length - 1; i > 0; i--) {
            swap(arr, i, rand.nextInt(i + 1));
        }
    }

    public static <T> void swap(T[] arr, int i, int j) {
        T tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    
}
