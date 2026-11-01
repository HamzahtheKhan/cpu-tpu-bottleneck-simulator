package hamzah.cpuhit.decoder;

import hamzah.cpuhit.CpuMonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CpuHeavyDecoderDemo {

    //Extreme computation to max out ALU
    public static SecureRecord decodeAndCompute(String line) {
        String id = line.substring(0, 8).trim();
        String payload = line.substring(8, 64).trim();

        double val = 0.0;
        for (int i = 1; i <= 80_000; i++) {
            val += Math.sin(i) * Math.cos(i) * Math.tan(i) * Math.sqrt(i);
        }

        return new SecureRecord(id, payload, val);
    }

    public static void runDemo() {
        System.out.println("=========High-Intensity, CPU Saturation========");

        int totalRecords = 500_000;
        List<String> imitateStream = new ArrayList<>(totalRecords);
        for (int i = 0; i < totalRecords; i++) {
            imitateStream.add(String.format("%-8d%-56s", i, "STREAM_PAYLOAD_TEST_" + i));
        }

        int availableCores = Runtime.getRuntime().availableProcessors();
        System.out.println("Processing " + totalRecords + " records intensely using " + availableCores + " CPU cores...");

        // Begin a background CPU monitoring thread
        CpuMonitor monitor = new CpuMonitor();
        monitor.startMonitoring();

        long startTime = System.currentTimeMillis();

        CompletableFuture<List<SecureRecord>> cpuTask = CompletableFuture.supplyAsync(() ->
                imitateStream.parallelStream()
                        .map(record -> CpuHeavyDecoderDemo.decodeAndCompute(record))
                        .collect(Collectors.toList())
        );

        List<SecureRecord> results = cpuTask.join();

        // stop CPU Monitoring thread and know the Peak CPU
        double peakCpu = monitor.stopMonitoring();

        long duration = System.currentTimeMillis() - startTime;

        System.out.println("Successfully processed " + results.size() + " CPU intensive records!");
        System.out.println("Time taken: " + duration + " ms");
        System.out.println("Throughput: " + (totalRecords * 1000L / duration) + " records/sec");
        System.out.println("Peak CPU Consumption Recorded: " + String.format("%.2f", peakCpu) + "%\n");
    }

    static class SecureRecord {
        String recordId;
        String payload;
        double computedValue;

        public SecureRecord(String recordId, String payload, double computedValue) {
            this.recordId = recordId;
            this.payload = payload;
            this.computedValue = computedValue;
        }
    }
}