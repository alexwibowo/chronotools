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
# Warmup Iteration   1: 4034.922 ops/ms
# Warmup Iteration   2: 4010.390 ops/ms
# Warmup Iteration   3: 4004.818 ops/ms
# Warmup Iteration   4: 4006.061 ops/ms
# Warmup Iteration   5: 3999.892 ops/ms
Iteration   1: 3994.122 ops/ms[2m 31s]
Iteration   2: 4003.357 ops/ms[2m 41s]
Iteration   3: 4008.009 ops/ms[2m 51s]
Iteration   4: 4006.652 ops/ms[3m 1s]
Iteration   5: 4004.807 ops/ms[3m 11s]


Result "io.github.alexwibowo.chronotools.UTCTimeJmhTest.jdk_benchmark":
  4003.390 ±(99.9%) 21.081 ops/ms [Average]
  (min, avg, max) = (3994.122, 4003.390, 4008.009), stdev = 5.475
  CI (99.9%): [3982.309, 4024.470] (assumes normal distribution)
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
# Warmup Iteration   1: 31998.031 ops/ms
# Warmup Iteration   2: 27321.759 ops/ms
# Warmup Iteration   3: 27299.161 ops/ms
# Warmup Iteration   4: 27310.512 ops/ms
# Warmup Iteration   5: 27314.632 ops/ms
Iteration   1: 27341.579 ops/ms51s]
Iteration   2: 27314.717 ops/ms1m 1s]
Iteration   3: 27324.069 ops/ms1m 11s]
Iteration   4: 27297.717 ops/ms1m 21s]
Iteration   5: 27353.011 ops/ms1m 31s]


Result "io.github.alexwibowo.chronotools.UTCTimeJmhTest.chronotools_benchmark":
  27326.219 ±(99.9%) 83.989 ops/ms [Average]
  (min, avg, max) = (27297.717, 27326.219, 27353.011), stdev = 21.812
  CI (99.9%): [27242.229, 27410.208] (assumes normal distribution).
```