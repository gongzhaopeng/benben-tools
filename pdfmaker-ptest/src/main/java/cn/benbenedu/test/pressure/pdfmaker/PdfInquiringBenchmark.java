package cn.benbenedu.test.pressure.pdfmaker;

import cn.benbenedu.test.pressure.pdfmaker.configuration.OpenidConfiguration;
import cn.benbenedu.test.pressure.pdfmaker.service.PdfAcquiringService;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
@Slf4j
public class PdfInquiringBenchmark {

    private ConfigurableApplicationContext applicationContext;

    private PdfAcquiringService pdfAcquiringService;

    private OpenidConfiguration openidConfiguration;

    private AtomicInteger curOpenidPos = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {

        final var opt = new OptionsBuilder()
                .include(PdfInquiringBenchmark.class.getSimpleName())
                .shouldFailOnError(false)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setUp() {

        if (applicationContext == null) {
            applicationContext = SpringApplication.run(
                    PdfmakerPtestApplication.class);
        }

        pdfAcquiringService = applicationContext
                .getBean(PdfAcquiringService.class);
        openidConfiguration = applicationContext
                .getBean(OpenidConfiguration.class);
    }

    @TearDown
    public void tearDown() {

        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    @Benchmark
    @Warmup(iterations = 0)
    @Measurement(iterations = 4)
    @Fork(0)
    @Threads(10)
    public void test() throws Exception {

        final var openidPos = curOpenidPos.getAndIncrement();

        if (openidPos >= openidConfiguration.getOpenids().size()) {

            log.warn(
                    "Openid position overflow: {} >= {}",
                    openidPos,
                    openidConfiguration.getOpenids().size());

            return;
        }

        final var openid =
                openidConfiguration.getOpenids().get(openidPos);

        pdfAcquiringService.inquirePdf(openid);
    }
}
