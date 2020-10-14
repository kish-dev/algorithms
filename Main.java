package com.company;

import java.util.Arrays;
import java.util.Scanner;


public class Main {

    public static double majorityRepresentative(int[] array,
                                                int leftIndex, int rightIndex) {
        //right index is arrayLength - 1
        if (rightIndex - leftIndex > 0) {
            int mid = (rightIndex + leftIndex) / 2;
            double majorRepFirst = majorityRepresentative(array, leftIndex, mid);
            double majorRepSecond = majorityRepresentative(array, mid + 1, rightIndex);

            int countMR1 = 0;   //константное число операций
            int countMR2 = 0;   //константное число операций

            if (!Double.isNaN(majorRepFirst)) {  //c*n операций
                countMR1 = getCountMR(array, leftIndex, rightIndex, mid, majorRepFirst, countMR1);
            }
            if (!Double.isNaN(majorRepSecond)) { //c*n операций
                countMR2 = getCountMR(array, leftIndex, rightIndex, mid, majorRepSecond, countMR2);
            }

            int half = (rightIndex - leftIndex + 1) / 2; //количество элементов в массиве / 2

            if (countMR1 >= half + 1) {     //константное число операций
                return majorRepFirst;
            }
            if (countMR2 >= half + 1) {     //константное число операций
                return majorRepSecond;
            }
            return Double.NaN;
        }
        return array[leftIndex];
    }

    private static int getCountMR(int[] array, int leftIndex, int rightIndex, int mid, double majorRep, int countMR) {
        for (int i = leftIndex; i <= mid; ++i) {
            if (majorRep == array[i]) {
                ++countMR;
            }
        }
        for (int i = mid + 1; i <= rightIndex; ++i) {
            if (majorRep == array[i]) {
                ++countMR;
            }
        }
        return countMR;
    }

    public static int select(int[] array, int k, int left, int right, int index) {

        Pair pair = divide(array, left, right, index);

        int postLess = pair.getPostLess();
        int postEqual = pair.getPostEqual();

        if (k <= postLess) {
            return select(array, k, left, postLess + left, -1);
        }

        if (k > postEqual) {
            return select(array, k - postEqual, postEqual + left, right, -1);
        }

        return array[postEqual - 1 + left];
    }

    public static Pair divide(int[] array, int left, int right, int index) {
        int randomIndex;
        if (index == -1) {
            randomIndex = (int) (Math.random() * (right - left - 1));    //смещение индекса
        } else {
            randomIndex = index;
        }

        int elemAtRandomIndex = array[randomIndex + left];   //смещение индекса
        int postLess = 0;      //количество меньших элементов

        for (int i = left; i < right; ++i) {
            if (array[i] < elemAtRandomIndex) {
                int temp = array[i];
                array[i] = array[postLess + left];
                array[postLess + left] = temp;
                ++postLess;
            }
        }
        int postEqual = postLess;      // количество меньших или равных

        for (int i = postEqual + left; i < right && postEqual + left < right; ++i) {
            if (array[i] == elemAtRandomIndex) {
                int temp = array[postEqual + left];
                array[postEqual + left] = array[i];
                array[i] = temp;
                ++postEqual;
            }
        }
        if (index != -1) {
            for (int j : array) {
                System.out.print(j + " ");
            }
            System.out.println("\n" + postLess + " " + (postEqual - 1));
        }
        return new Pair(postLess, postEqual);
    }

    public static void task2() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int[] array = new int[n];

        for (int i = 0; i < n; ++i) {
            array[i] = scanner.nextInt();
        }

        double k = majorityRepresentative(array, 0, array.length - 1);
        if (!Double.isNaN(k)) {
            System.out.println((int) k);
        } else {
            System.out.println(k);
        }
    }

    public static void task3() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int[] array = new int[n];

        for (int i = 0; i < n; ++i) {
            array[i] = scanner.nextInt();
        }

        int index = scanner.nextInt();

        int indexOfMedian = array.length / 2;
        if (array.length % 2 != 0) {
            ++indexOfMedian;
        }

        System.out.print("median: " + select(array, indexOfMedian, 0, array.length, index));
    }


    public static void main(String[] args) {
//        task2();
        task3();

    }
}

class Pair {

    private final int postLess;
    private final int postEqual;

    public Pair(int postLess, int postEqual) {
        this.postLess = postLess;
        this.postEqual = postEqual;
    }

    public int getPostLess() {
        return postLess;
    }

    public int getPostEqual() {
        return postEqual;
    }
}
