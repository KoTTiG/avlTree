package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.TreeSet;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@State(Scope.Thread)
public class AvlTreeBenchmarkTest {
    @Param({"0", "1000", "10000", "100000"})
    public int treeSize;
    public AvlTree<Integer> testTree = new AvlTree<>();
    public TreeSet<Integer> treeSet = new TreeSet<>();

    int intToFind;
    @Setup(Level.Trial)
    public void setUp() {
        for (int i = 0; i < treeSize; i++) {
            testTree.add(i);
            treeSet.add(i);
        }

        intToFind = (int)(Math.random()*treeSize);
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1, time = 500, timeUnit = MILLISECONDS)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = MILLISECONDS)
    public void testContains(Blackhole blackhole) {
        blackhole.consume(testTree.contains(intToFind));
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1, time = 500, timeUnit = MILLISECONDS)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = MILLISECONDS)
    public void testRemove(Blackhole blackhole) {
        blackhole.consume(testTree.remove(intToFind));
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1, time = 500, timeUnit = MILLISECONDS)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = MILLISECONDS)
    public void testAdd(Blackhole blackhole) {
        blackhole.consume(testTree.add(treeSize));
    }


    /*
    Сравнение с библиотечным методом TreeSet, в основе которого лежит красночерное дерево поиска
     */
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1, time = 500, timeUnit = MILLISECONDS)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = MILLISECONDS)
    public void treeAdd(Blackhole blackhole) {
        blackhole.consume(treeSet.add(treeSize));
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1, time = 500, timeUnit = MILLISECONDS)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = MILLISECONDS)
    public void treeContains(Blackhole blackhole) {
        blackhole.consume(treeSet.remove(intToFind));
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1, time = 500, timeUnit = MILLISECONDS)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(value = MICROSECONDS)
    @Measurement(iterations = 5, time = 200, timeUnit = MILLISECONDS)
    public void treeRemove(Blackhole blackhole) {
        blackhole.consume(treeSet.remove(intToFind));
    }
}
