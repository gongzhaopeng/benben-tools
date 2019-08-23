package cn.benbenedu.test.pressure.fenke;

import cn.benbenedu.test.pressure.fenke.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
@Slf4j
public class LoginBenchmark {

    private ConfigurableApplicationContext applicationContext;

    private LoginService loginService;

    public static void main(String[] args) throws Exception {

        final var opt = new OptionsBuilder()
                .include(LoginBenchmark.class.getSimpleName())
                .shouldFailOnError(false)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setUp() {

        if (applicationContext == null) {
            applicationContext = SpringApplication.run(
                    FenkePtestApplication.class);
        }

        loginService = applicationContext
                .getBean(LoginService.class);
    }

    @TearDown
    public void tearDown() {

        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    @Benchmark
    @Warmup(iterations = 5)
    @Measurement(iterations = 10)
    @Threads(100)
    public void test() {

        loginService.login("1177@test.com", "123456");
    }
}
