# CPU Bottleneck Simulators

## Sample Execution & Telemetry Results 

### Demo 1: Simulating a CPU intensive operation from a simulated high throughput stream
<img width="1867" height="987" alt="CpuDecoder" src="https://github.com/user-attachments/assets/a3ce203b-a681-4340-b7d9-8c6d5673c06e" />

### Demo 2: Matrix Multiplication Bottleneck Scaling , why TPU needed  
<img width="1842" height="982" alt="MatrixMult_TPUJustification" src="https://github.com/user-attachments/assets/c0690465-f09f-4be9-bf62-2a30b95e6f49" />


## 📂 Project Structure

```text
cpu-tpu-bottleneck-simulator/
├── .gitignore              # Excludes IDE metadata (.idea/) and Maven build targets (target/)
├── pom.xml                 # Maven project configuration and Java 21 dependencies setup
└── src
    └── main
        └── java
            └── hamzah
                └── cpuhit
                    ├── CpuMonitor.java             # Background thread using OS MXBean to track peak CPU utilization
                    ├── TestDecoder.java            # Entry point for running decoder bottleneck simulations
                    ├── TestMatrixMult.java         # Entry point for running matrix multiplication workload tests
                    ├── decoder
                    │   └── CpuHeavyDecoderDemo.java # Simulates heavy string/byte decoding loads on the CPU
                    └── matrixmult
                        └── MatrixMultiBottleneck.java # Parallelized O(n^3) matrix multiplier to test core and memory saturation
```

## Project Overview
Modern software engineering requires a deep understanding of hardware constraints. High-throughput data pipelines, where some data processing involves the steps and are very CPU-intensive operations, they frequently run into hardware bottlenecks due to arithmetic logic unit (ALU) capacity and memory bandwidth.

This repository features two distinct Java 21 based projects to use its concurrency features to optimally use all cores with effective code and still get CPU saturated:
* **Parallel Stream CPU Core Saturation (`CpuHeavyDecoderDemo`)**: Simulates high-density streaming pipeline and then makes it CPU intensive operation, maximizing core utilization and tracking real-time telemetry.
* **Matrix Multiplication Memory Wall (`MatrixMultiBottleneck`)**: Analyzes $\mathcal{O}(n^3)$ computational scaling, basically it shows how CPU usage spikes as matrix multiplication scales up, illustrating why specialized chips like Google TPUs were developed. 

## Telemetry (`CpuMonitor`)
To measure performance objectively rather than relying on estimation, the project includes a dedicated telemetry component utilizing Java's `OperatingSystemMXBean`.  
`OperatingSystemMXBean` is like a built-in "stethoscope", a special Java Management API, allowing the code running within JVM to go outside via low footprint thread like this, to check with my OS that how hard the CPU is running and get the max CPU during the main run which is actually a JVM execution.

## How to Compile and Run 
### Prerequisites
* Java 21 (JDK 21 or compatible distribution like GraalVM)
* Maven (for dependency management and building)

### Step 1: Clone the Repository 
```bash
git clone https://github.com/HamzahtheKhan/cpu-hardware-bottleneck-simulator.git
cd cpu-hardware-bottleneck-simulator
```

### Step 2: Build with Maven 
```bash
mvn clean compile
```

### Step 3: Run the Simulation
You can execute the project via IntelliJ IDEA or Eclipse by running either of `TestDecoder.java` or `TestMatrixMult`, or via your terminal using Maven:
```bash
mvn exec:java -Dexec.mainClass="hamzah.cpuhit.TestDecoder"
mvn exec:java -Dexec.mainClass="hamzah.cpuhit.TestMatrixMult"
```

## Key Takeaways & Hardware Insights 
Not all CPU-intensive workloads are candidates for custom hardware acceleration. Tasks with known repetitive steps and rigid, predictable structures that bottleneck on arithmetic and memory can heavily impact data pipelines and other use cases, justifying the specialized shift from software to hardware ASICs. The choice also depends on cost and volume. However, a mix of IO or CPU tasks or irregular compute tasks remain in the domain of general-purpose processors, highlighting why modern engineering requires a careful convergence of disciplines.
