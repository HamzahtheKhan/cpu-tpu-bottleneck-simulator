package hamzah.cpuhit.matrixmult;

import hamzah.cpuhit.CpuMonitor;
import java.util.stream.IntStream;

public class MatrixMultiBottleneck {

    public static void runDemo() {
        System.out.println("************ Matrix Multiplication CPU Bottleneck (Architecture Context) ************");
        System.out.println("Watch how quickly a processor hits a brick wall as grid sizes grow O(n^3).\n");

        int[] sizes = {500, 1000, 2000};

        for (int n : sizes) {
            // Compact single-line status update
            System.out.print("Multiplying " + n + " x " + n + " grid... ");

            double[][] matrixA = generateMatrix(n);
            double[][] matrixB = generateMatrix(n);
            double[][] result = new double[n][n];

            // Begin background CPU monitoring thread
            CpuMonitor monitor = new CpuMonitor();
            monitor.startMonitoring();

            long startTime = System.currentTimeMillis();

            // Parallelize outer loop and distribute rows on to CPU cores
            // to saturate capacity <Hamzah>
            IntStream.range(0, n).parallel().forEach(i -> {
                for (int j = 0; j < n; j++) {
                    double sum = 0;
                    for (int k = 0; k < n; k++) {
                        sum += matrixA[i][k] * matrixB[k][j];
                    }
                    result[i][j] = sum;
                }
            });

            long duration = System.currentTimeMillis() - startTime;
            double peakCpu = monitor.stopMonitoring();

            System.out.println("Took " + duration + " ms | Peak CPU: " + String.format("%.2f", peakCpu) + "%");
        }

        System.out.println();
        System.out.println("#CONCLUSION: Standard multi-core CPUs handle parallel loops well up to a point,");
        System.out.println("but O(n^3) matrix multiplication eventually saturates memory bandwidth and core capacity.");
        System.out.println("This structural hardware limit is why specialized processors (like TPUs) use dedicated systolic arrays");
        System.out.println("instead of general-purpose loops to process massive data grids.");
        System.out.println("*****************************************************************");
    }

    private static double[][] generateMatrix(int n) {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Math.random() * 10;
            }
        }
        return matrix;
    }
}