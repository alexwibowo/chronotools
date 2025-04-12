# ChronoTools

A collection of tools for working with time and date in Java.

One main driver for this project is to provide a garbage free way to calculate nanoseconds since epoch time, for a given date String.
This is most useful when working with latency sensitive applications, such as in a trading system, where you want to do very minimum
object allocation. 

For such system, we want to avoid using the standard Java Date and Calendar classes, as they are not garbage free. Furthermore,
constructing a Java Date is an expensive operation. 


# Performance 
Here is a JMH test comparison of the performance of ChronoTools vs Java Date and Calendar classes. You can find this test under the [jmh](src/jmh) folder.

## Using Java Date Formatter
```
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: io.github.alexwibowo.chronotools.UTCTimeJmhTest.jdk_benchmark

# Run progress: 50.00% complete, ETA 00:01:40
# Fork: 1 of 1
# Warmup Iteration   1: 3715.772 ops/ms
# Warmup Iteration   2: 3782.352 ops/ms
# Warmup Iteration   3: 3719.773 ops/ms
# Warmup Iteration   4: 3732.944 ops/ms
# Warmup Iteration   5: 3766.126 ops/ms
Iteration   1: 3704.710 ops/ms[2m 31s]
Iteration   2: 3732.308 ops/ms[2m 41s]
Iteration   3: 3757.198 ops/ms[2m 51s]
Iteration   4: 3769.365 ops/ms[3m 1s]
Iteration   5: 3752.061 ops/ms[3m 11s]


Result "io.github.alexwibowo.chronotools.UTCTimeJmhTest.jdk_benchmark":
  3743.129 ±(99.9%) 97.399 ops/ms [Average]
  (min, avg, max) = (3704.710, 3743.129, 3769.365), stdev = 25.294
  CI (99.9%): [3645.730, 3840.527] (assumes normal distribution)
```

## Using ChronoTools
``` 
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: io.github.alexwibowo.chronotools.UTCTimeJmhTest.chronotools_benchmark

# Run progress: 0.00% complete, ETA 00:03:20
# Fork: 1 of 1
# Warmup Iteration   1: 18650.618 ops/ms
# Warmup Iteration   2: 18867.112 ops/ms
# Warmup Iteration   3: 19287.294 ops/ms
# Warmup Iteration   4: 19131.448 ops/ms
# Warmup Iteration   5: 19165.050 ops/ms
Iteration   1: 18666.409 ops/ms51s]
Iteration   2: 18212.425 ops/ms1m 1s]
Iteration   3: 19302.991 ops/ms1m 11s]
Iteration   4: 19223.356 ops/ms1m 21s]
Iteration   5: 19096.801 ops/ms1m 31s]


Result "io.github.alexwibowo.chronotools.UTCTimeJmhTest.chronotools_benchmark":
  18900.396 ±(99.9%) 1757.235 ops/ms [Average]
  (min, avg, max) = (18212.425, 18900.396, 19302.991), stdev = 456.348
  CI (99.9%): [17143.162, 20657.631] (assumes normal distribution)
```