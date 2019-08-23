package cn.benbenedu.test.pressure.fenke;

import cn.benbenedu.test.pressure.fenke.service.AccountService;
import cn.benbenedu.test.pressure.fenke.service.LoginService;
import cn.benbenedu.test.pressure.fenke.service.StudentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
@Slf4j
public class StudentInfoFetchingBenchmark {

    private static final int ACCOUNT_NUMBER = 100;

    private ConfigurableApplicationContext applicationContext;

    private StudentInfoService studentInfoService;

    private List<MultiValueMap<String, String>> cookies;

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

        final var loginService = applicationContext
                .getBean(LoginService.class);
        final var accountService = applicationContext
                .getBean(AccountService.class);
        studentInfoService = applicationContext
                .getBean(StudentInfoService.class);

        cookies = loginService.batchLogin(
                accountService.acquireAccounts(ACCOUNT_NUMBER));
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

        final var cookie = cookies.get(
                new Random().nextInt(cookies.size()));

        studentInfoService.fetchStudentInfo(cookie);
    }
}
