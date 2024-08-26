package com.example.pingpong;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for testing and understanding the behavior of Java memory management,
 * specifically focusing on heap and stack size limits. This class performs experiments
 * to determine the effects of heavy memory usage and deep recursion on application performance
 * and stability.
 */
public class HeapStackTest {

    /**
     * Tests the limits of JVM heap memory by repeatedly allocating large arrays and monitoring
     * memory usage and average allocation time until an OutOfMemoryError is thrown.
     */
    public static void testHeapSize() {
        Runtime runtime = Runtime.getRuntime();
        long startTime = System.currentTimeMillis();
        long endTime;
        List<long[]> memoryHog = new ArrayList<>();  // List to hold references to arrays
        long iterations = 0;

        // To calculate average creation time
        long totalCreationTime = 0;

        try {
            while (true) {
                long arrayStartTime = System.currentTimeMillis();  // Start time for array creation

                long[] largeArray = new long[100000];
                memoryHog.add(largeArray);

                iterations++;

                long arrayEndTime = System.currentTimeMillis();  // End time for array creation
                totalCreationTime += (arrayEndTime - arrayStartTime);
                double averageCreationTime = (double) totalCreationTime / iterations;

                if (iterations % 100 == 0) {
                    long currentMemory = runtime.totalMemory() - runtime.freeMemory();
                    System.out.println("Heap Iterations: " + iterations + ", Memory used: " + currentMemory / 1024 / 1024 + " MB");
                    System.out.println("Average Array Creation Time: " + averageCreationTime + " ms");
                }
            }
        } catch (OutOfMemoryError e) {
            memoryHog.clear();  // Clear references to allow garbage collection
            endTime = System.currentTimeMillis();
            double averageCreationTime = iterations > 0 ? (double) totalCreationTime / iterations : 0;
            System.out.println("Out of Memory after " + iterations + " iterations");
            System.out.println("Average Array Creation Time: " + averageCreationTime + " ms");
            System.out.println("Total time: " + (endTime - startTime) / 1000.0 + " seconds");
        }
    }

    /**
     * Tests the JVM stack size limits by performing a deep recursive method call and records
     * the depth of recursion at which a StackOverflowError occurs.
     */
    public static void testStackSize() {
        Runtime runtime = Runtime.getRuntime();
        long startTime = System.currentTimeMillis();
        long endTime;

        try {
            int depth = recursiveStackCall(0, 0);  // Start recursion from depth 0 with no initial time
            endTime = System.currentTimeMillis();
            System.out.println("Stack Overflow at depth: " + depth);
            System.out.println("Stack test duration: " + (endTime - startTime) + " ms");
        } catch (StackOverflowError e) {
            endTime = System.currentTimeMillis();
            System.out.println("Stack Overflow Error caught");
            System.out.println("Stack test duration: " + (endTime - startTime) + " ms");
        }
    }

    /**
     * A recursive method to increment the call depth and track the time spent on each call.
     * This method aids in measuring the time complexity and stack growth.
     *
     * @param depth             Current depth of recursion.
     * @param totalRecursionTime Cumulative time spent in recursion to this point.
     * @return The depth of recursion when a StackOverflowError occurs.
     */
    private static int recursiveStackCall(int depth, long totalRecursionTime) {
        long callStartTime = System.currentTimeMillis();

        if (depth % 100 == 0) {
            System.out.println("Stack depth: " + depth);
            double averageCreationTime = depth > 0 ? (double) totalRecursionTime / depth : 0;
            System.out.println("Average Recursion Time: " + averageCreationTime + " ms");
        }

        long callEndTime = System.currentTimeMillis();
        totalRecursionTime += (callEndTime - callStartTime);

        return recursiveStackCall(depth + 1, totalRecursionTime);  // Recursive call
    }

    /**
     * The main method to execute the tests. Change the value of heapTest to switch between
     * testing heap and stack capabilities.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        boolean heapTest = true;  // Change false to run stack test
        if (heapTest) {
            System.out.println("Starting heap size test...");
            testHeapSize();
        } else {
            System.out.println("Starting stack size test...");
            testStackSize();
        }
    }
}
