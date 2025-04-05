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
# Warmup: 2 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: org.isolution.chronotools.UTCTimeJmhTest.test_using_jdk

# Run progress: 0.00% complete, ETA 00:02:20
# Fork: 1 of 1
# Warmup Iteration   1: 3894.102 ops/ms
# Warmup Iteration   2: 3940.189 ops/ms
Iteration   1: 3936.466 ops/ms[21s]
Iteration   2: 3939.484 ops/ms[31s]
Iteration   3: 3942.406 ops/ms[41s]
Iteration   4: 3942.789 ops/ms[51s]
Iteration   5: 3944.909 ops/ms[1m 1s]


Result "org.isolution.chronotools.UTCTimeJmhTest.test_using_jdk":
  3941.211 ±(99.9%) 12.638 ops/ms [Average]
  (min, avg, max) = (3936.466, 3941.211, 3944.909), stdev = 3.282
  CI (99.9%): [3928.572, 3953.849] (assumes normal distribution)
```

## Using ChronoTools
``` 
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 2 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: org.isolution.chronotools.UTCTimeJmhTest.test_using_library

# Run progress: 50.00% complete, ETA 00:01:10
# Fork: 1 of 1
# Warmup Iteration   1: 17860.947 ops/ms
# Warmup Iteration   2: 17960.721 ops/ms
Iteration   1: 17920.501 ops/ms1m 31s]
Iteration   2: 17951.683 ops/ms1m 41s]
Iteration   3: 17930.246 ops/ms1m 51s]
Iteration   4: 17926.574 ops/ms2m 1s]
Iteration   5: 17955.161 ops/ms2m 11s]


Result "org.isolution.chronotools.UTCTimeJmhTest.test_using_library":
  17936.833 ±(99.9%) 60.020 ops/ms [Average]
  (min, avg, max) = (17920.501, 17936.833, 17955.161), stdev = 15.587
  CI (99.9%): [17876.813, 17996.853] (assumes normal distribution)

```