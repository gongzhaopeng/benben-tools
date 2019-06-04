package com.benben.pt.intent;

import com.benben.pt.intent.service.SpecRecommendInvokeMocker;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
public class SpecRecommendBenchmark {

    private ConfigurableApplicationContext applicationContext;

    private SpecRecommendInvokeMocker invokeMocker;

    public static void main(String[] args) throws Exception {

        final var opt = new OptionsBuilder()
                .include(SpecRecommendBenchmark.class.getSimpleName())
                .shouldFailOnError(false)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setUp() {

        if (applicationContext == null) {
            applicationContext = SpringApplication.run(
                    IntentPtApplication.class);
        }

        invokeMocker = applicationContext
                .getBean(SpecRecommendInvokeMocker.class);
    }

    @TearDown
    public void tearDown() {

        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 10)
    @Threads(100)
    public void test() {
        invokeMocker.mock();
    }
}
