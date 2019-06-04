package com.benben.pt.intent;

import com.benben.pt.intent.service.ProfileUpdateInvokeMocker;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
public class ProfileUpdateBenchmark {

    private ConfigurableApplicationContext applicationContext;

    private ProfileUpdateInvokeMocker invokeMocker;

    public static void main(String[] args) throws Exception {

        final var opt = new OptionsBuilder()
                .include(ProfileUpdateBenchmark.class.getSimpleName())
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
                .getBean(ProfileUpdateInvokeMocker.class);
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
