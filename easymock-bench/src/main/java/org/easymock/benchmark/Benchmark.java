package pro.tremblay;

import org.easymock.EasyMock;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Fork(2)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 4, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class Benchmark {

    private Class<?> anInterface = List.class;

    private Class<?> aClass = ArrayList.class;

    @GenerateMicroBenchmark
    public Object list() {
        return EasyMock.createMock(anInterface);
    }

    @GenerateMicroBenchmark
    public Object arrayList() {
        return EasyMock.createMock(aClass);
    }
}
