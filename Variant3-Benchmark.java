import java.util.Arrays;

/**
 * Variant 3 - Comparing Growth Rates on the Same Plot
 *
 * Measures four workloads over exactly the same input sizes:
 * O(1), O(log n), O(n), and O(n^2).
 *
 * Output is CSV-friendly so the same measurements can be plotted.
 */
public class Variant3Benchmark {
    private static final int WARMUP_RUNS = 10;
    private static final int MEASURED_RUNS = 7;
    private static final long MIN_BATCH_NS = 2_000_000L;
    private static volatile long blackhole = 0;

    private static final int[] INPUT_SIZES = {
            100, 250, 500, 1_000, 2_000,
            4_000, 8_000, 12_000, 16_000, 20_000
    };

    public static void main(String[] args) {
        System.out.println("Variant 3 - Comparing Growth Rates on the Same Plot");
        System.out.println("Java: " + System.getProperty("java.version"));
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        System.out.println("Warm-ups: " + WARMUP_RUNS + ", measured runs: " + MEASURED_RUNS);
        System.out.println();
        System.out.println("n,O(1)_ns,O(log n)_ns,O(n)_ns,O(n^2)_ns");

        for (int n : INPUT_SIZES) {
            double constant = benchmarkConstant(n);
            double logarithmic = benchmarkBinarySearch(n);
            double linear = benchmarkLinear(n);
            double quadratic = benchmarkQuadratic(n);

            System.out.printf("%d,%.3f,%.3f,%.3f,%.3f%n",
                    n, constant, logarithmic, linear, quadratic);
        }

        if (blackhole == Long.MIN_VALUE) {
            System.out.println("ignore=" + blackhole);
        }
    }

    // O(1): fixed amount of work, independent of n.
    private static long constantWork(int n) {
        long x = n;
        x = x * 31L + 7L;
        x ^= (x >>> 3);
        return x;
    }

    private static double benchmarkConstant(int n) {
        for (int i = 0; i < WARMUP_RUNS; i++) blackhole ^= constantWork(n);
        int batch = calibrateConstantBatch(n);
        double[] samples = new double[MEASURED_RUNS];

        for (int r = 0; r < MEASURED_RUNS; r++) {
            long sink = 0;
            long start = System.nanoTime();
            for (int k = 0; k < batch; k++) sink ^= constantWork(n + (k & 1));
            long elapsed = System.nanoTime() - start;
            blackhole ^= sink;
            samples[r] = elapsed / (double) batch;
        }
        return median(samples);
    }

    private static int calibrateConstantBatch(int n) {
        int batch = 1;
        while (batch < 16_777_216) {
            long sink = 0;
            long start = System.nanoTime();
            for (int k = 0; k < batch; k++) sink ^= constantWork(n + (k & 1));
            long elapsed = System.nanoTime() - start;
            blackhole ^= sink;
            if (elapsed >= MIN_BATCH_NS) return batch;
            batch *= 2;
        }
        return batch;
    }

    // O(log n): unsuccessful binary search forces the full search path.
    private static double benchmarkBinarySearch(int n) {
        int[] data = new int[n];
        for (int i = 0; i < n; i++) data[i] = i;
        int target = -1;

        for (int i = 0; i < WARMUP_RUNS; i++) blackhole += Arrays.binarySearch(data, target);
        int batch = calibrateBinaryBatch(data, target);
        double[] samples = new double[MEASURED_RUNS];

        for (int r = 0; r < MEASURED_RUNS; r++) {
            long sink = 0;
            long start = System.nanoTime();
            for (int k = 0; k < batch; k++) sink += Arrays.binarySearch(data, target);
            long elapsed = System.nanoTime() - start;
            blackhole ^= sink;
            samples[r] = elapsed / (double) batch;
        }
        return median(samples);
    }

    private static int calibrateBinaryBatch(int[] data, int target) {
        int batch = 1;
        while (batch < 16_777_216) {
            long sink = 0;
            long start = System.nanoTime();
            for (int k = 0; k < batch; k++) sink += Arrays.binarySearch(data, target);
            long elapsed = System.nanoTime() - start;
            blackhole ^= sink;
            if (elapsed >= MIN_BATCH_NS) return batch;
            batch *= 2;
        }
        return batch;
    }

    // O(n): exactly n dependent updates.
    private static long linearWork(int n) {
        long x = 0;
        for (int i = 0; i < n; i++) x += (i & 7);
        return x;
    }

    private static double benchmarkLinear(int n) {
        for (int i = 0; i < WARMUP_RUNS; i++) blackhole ^= linearWork(n);
        int batch = calibrateLinearBatch(n);
        double[] samples = new double[MEASURED_RUNS];

        for (int r = 0; r < MEASURED_RUNS; r++) {
            long sink = 0;
            long start = System.nanoTime();
            for (int k = 0; k < batch; k++) sink ^= linearWork(n);
            long elapsed = System.nanoTime() - start;
            blackhole ^= sink;
            samples[r] = elapsed / (double) batch;
        }
        return median(samples);
    }

    private static int calibrateLinearBatch(int n) {
        int batch = 1;
        while (batch < 1_048_576) {
            long sink = 0;
            long start = System.nanoTime();
            for (int k = 0; k < batch; k++) sink ^= linearWork(n);
            long elapsed = System.nanoTime() - start;
            blackhole ^= sink;
            if (elapsed >= MIN_BATCH_NS) return batch;
            batch *= 2;
        }
        return batch;
    }

    // O(n^2): n*n simple updates. No early termination.
    private static long quadraticWork(int n) {
        long x = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                x += ((i + j) & 1);
            }
        }
        return x;
    }

    private static double benchmarkQuadratic(int n) {
        for (int i = 0; i < WARMUP_RUNS; i++) blackhole ^= quadraticWork(Math.min(n, 2_000));
        int batch = calibrateQuadraticBatch(n);
        double[] samples = new double[MEASURED_RUNS];
        for (int r = 0; r < MEASURED_RUNS; r++) {
            long sink = 0;
            long start = System.nanoTime();
            for (int k = 0; k < batch; k++) sink ^= quadraticWork(n);
            long elapsed = System.nanoTime() - start;
            blackhole ^= sink;
            samples[r] = elapsed / (double) batch;
        }
        return median(samples);
    }

    private static int calibrateQuadraticBatch(int n) {
        int batch = 1;
        while (batch < 65_536) {
            long sink = 0;
            long start = System.nanoTime();
            for (int k = 0; k < batch; k++) sink ^= quadraticWork(n);
            long elapsed = System.nanoTime() - start;
            blackhole ^= sink;
            if (elapsed >= MIN_BATCH_NS) return batch;
            batch *= 2;
        }
        return batch;
    }

    private static double median(double[] values) {
        double[] copy = values.clone();
        Arrays.sort(copy);
        return copy[copy.length / 2];
    }
}
