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

## Measured Results

The table displays the time complexity for various operations, ranging from O(1) nanoseconds to O(n^2) nanoseconds.
| n | O(1) ns | O(log n) ns | O(n) ns | O(n²) ns |
|---:|---:|---:|---:|---:|
| 100 | 0.751 | 6.244 | 41.792 | 3,196.172 |
| 250 | 1.021 | 5.212 | 76.592 | 19,770.313 |
| 500 | 0.756 | 5.654 | 142.784 | 84,004.188 |
| 1,000 | 0.746 | 6.054 | 285.289 | 319,548.500 |
| 2,000 | 0.749 | 6.612 | 563.904 | 1,247,664 |
| 4,000 | 0.759 | 7.585 | 1,157.271 | 5,243,401 |
| 8,000 | 0.773 | 8.143 | 2,243.576 | 20,714,216 |
| 12,000 | 0.755 | 8.554 | 3,333.608 | 45,659,515 |
| 16,000 | 0.738 | 8.552 | 4,431.387 | 80,595,716 |
| 20,000 | 0.736 | 9.121 | 5,593.215 | 125,798,075 |

The complete benchmark output is available in:

```text
results/benchmark_results. txt
```

---

## Growth Comparison

The measurements show four different growth patterns.

O(1) — Constant Operation

The execution time remains roughly constant as n increases. This is consistent with O(1) because the amount of work does not depend on the input size.

O(log n) — Binary Search

Binary Search grows very slowly as n increases. This aligns with logarithmic growth, as each comparison cuts the remaining search space in half.

O(n) — Linear Workload

The execution time increases approximately linearly with `n`. Consequently, increasing the input size results in a proportional increase in the amount of work performed.

O(n2) — Nested Loops

The quadratic workload increases at a significantly faster rate than the other workloads. As n increases, its execution time quickly diverges from linear, logarithmic, and constant growth patterns.

---

## Separation Between Growth Rates

The gap between O(n) and O(n2) widens significantly as the input size increases.

At `n = 100`:

```text
O(n2) / O(n)
= 3196. 172 / 41. 792
≈ 76. 5
```

The quadratic workload is approximately 77 times slower than the linear workload.

At `n = 20,000`:

```text
O(n2) / O(n)
= 125798075 / 5593. 215
≈ 22491
```

The quadratic workload is approximately 22,491 times slower than the linear workload.

There is no literal crossover between O(n) and O(n^2) within the selected input range because the quadratic workload is already slower at the smallest measured value.

Instead, the experiment shows that the performance gap widens progressively as `n` increases.

---

## Theoretical Interpretation

The theoretical growth functions depicted in the experiment are:

```text
O(1)     -> 1
O(log n) -> log2(n)
O(n)     -> n
O(n2)    -> n2
```

The empirical results follow the expected qualitative behaviour:

```text
O(1) < O(log n) < O(n) < O(n2)
```

Big-O notation describes the dominant growth rate rather than the exact execution time.

Absolute measurements are influenced by various factors, including the JVM, JIT compilation, processor caches, memory behavior, operating system scheduling, and the costs associated with constant implementation.

Consequently, the primary goal of the experiment is not to compare individual nanosecond values, but to observe how the execution-time curves diverge as the input size increases.

---

## Running the Benchmark

Compile the Java source file:

```bash
javac Variant3Benchmark.java
```

Run the benchmark:

```bash
java Variant3Benchmark
```

The program executes all four workloads over the common input sequence and prints the measured execution times.

---

## Repository Structure

```text
Variant3/
├── Variant3Benchmark.java
├── README.md
└── results/
    └── benchmark_results.txt
```

---

## Environment

The benchmark was executed using:

```text
Java: OpenJDK 21.0.11
Operating System: 64-bit Linux
Timer: System.nanoTime()
```

Execution times are environment-specific and should not be interpreted as universal performance values.

---

## Author

**Paula Hernández Varela**  
Data Science and Engineering  
Big Data  
ULPGC  
Academic Year 2026–2027
