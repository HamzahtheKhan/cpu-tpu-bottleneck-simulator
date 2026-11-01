package hamzah.cpuhit.matrixmult;

import hamzah.cpuhit.CpuMonitor;

public class MatrixMultiBottleneck {

    public static void runDemo() {
        System.out.println("************ Matrix Multiplication CPU Bottleneck (TPU Context) ************");

        System.out.println("Watch how quickly a standard CPU hits a brick wall as the grid grows.\n");

        int[] sizes = {500, 1000, 3000};

        for (int n : sizes) {
            System.out.println("Multiplying two " + n + " x " + n + " grids...");

            double[][] matrixA = generateMatrix(n);
            double[][] matrixB = generateMatrix(n);
            double[][] result = new double[n][n];

            // Begin a background CPU monitoring thread for Matrix Operation Peak CPU
            CpuMonitor monitor = new CpuMonitor();
            monitor.startMonitoring();

            long startTime = System.currentTimeMillis();

            // Triple Nested Loop:: that is an O(n^3) CPU nightmare
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double sum = 0;
                    for (int k = 0; k < n; k++) {
                        sum += matrixA[i][k] * matrixB[k][j];
                    }
                    result[i][j] = sum;
                }
            }

            long duration = System.currentTimeMillis() - startTime;

            // stop CPU Monitoring thread and know the Peak CPU of this one
            double peakCpu = monitor.stopMonitoring();

            System.out.println("Time taken: " + duration + " ms");
            System.out.println("Peak CPU Consumption Recorded: " + String.format("%.2f", peakCpu) + "%\n");
        }

        System.out.println("#CONCLUSION: CPUs loop sequentially through memory. Google built");
        System.out.println("TPUs with thousands of multipliers to stream data continuously!");
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