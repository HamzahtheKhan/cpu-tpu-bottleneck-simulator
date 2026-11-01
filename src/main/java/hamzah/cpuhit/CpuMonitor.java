package hamzah.cpuhit;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicBoolean;

public class CpuMonitor implements Runnable {
    //OperatingSystemMXBean is like a built-in "stethoscope", a special Java Management API,
    // allowing the code running within JVM to go outside via low footprint thread like this,
    // to check with my OS that how hard the CPU is running and get the max CPU in the entire life cycle
    // of main run or jvm execution <Hamzah>

    private final OperatingSystemMXBean osBean;
    private final AtomicBoolean isRunning;
    private double maxCpuPeak;
    private Thread monitorThread;

    public CpuMonitor() {
        this.osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        this.isRunning = new AtomicBoolean(false);
        this.maxCpuPeak = 0.0;
    }

    public void startMonitoring() {
        isRunning.set(true);
        maxCpuPeak = 0.0;
        monitorThread = new Thread(this, "CpuMonitor-Thread");
        monitorThread.start();
    }

    public double stopMonitoring() {
        isRunning.set(false);
        try {
            if (monitorThread != null) {
                monitorThread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return maxCpuPeak;
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            double cpuLoad = osBean.getCpuLoad() * 100.0;
            if (cpuLoad > 0 && cpuLoad > maxCpuPeak) {
                maxCpuPeak = cpuLoad;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}