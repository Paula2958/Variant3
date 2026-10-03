# Big-O Complexity — Variant 3

## Comparing Growth Rates on the Same Plot

This project experimentally compares different algorithmic complexity classes using identical input sequences.

The goal is to visualize how varying growth rates respond to increasing input sizes and to compare these empirical measurements against their theoretical Big-O complexities.

---

## Algorithms

Four workloads with different complexity classes are evaluated:

| Algorithm / Workload | Complexity | Description |.
|---|---|---|
Constant Operation | O(1) | Performs a fixed amount of work independently of n |.
Binary Search achieves O(log n) time complexity by repeatedly halving the search space.
Linear Workload performs one operation per iteration, resulting in O(n) time complexity.
Nested Loops have a time complexity of O(n2) because they perform operations using two levels of nested iterations.

The expected theoretical ordering for sufficiently large inputs is:

```text
O(1) < O(log n) < O(n) < O(n2)
```

---

## Experimental Objective

The experiment addresses the following question:

How do the execution times of different complexity classes diverge when measured using the same sequence of input sizes?

All four workloads are evaluated using identical values of `n`.

```text
100
250
500
1000
2000
4000
8000
12000
16000
20000
```

This enables a direct comparison of their growth rates.

---

## Measurement Methodology

Execution time is measured using:

```java
System.nanoTime()
```

The benchmark incorporates multiple metrics designed to enhance the reliability of the experiment.

- 10 JVM warm-up executions before measurement.
- 7 measured samples for each configuration.
- The median execution time is retained.
- The same input sizes are applied to all complexity classes.
- Batches of O(1) and O(log n) operations are executed to minimize timer overhead.
- A volatile result sink is used to prevent the JVM from eliminating computations whose results would otherwise be unused.
- Binary Search employs a deterministic input and an unsuccessful search to demonstrate the logarithmic search path.

The reported execution times are expressed in nanoseconds.

---
