package edu.benben.tools.readingclientmock;

import edu.benben.tools.readingclientmock.configuration.ReadingClientMockConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
@Slf4j
public class ReadingClientMockMachine implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private ReadingClientMockConfig clientMockConfig;

    public ReadingClientMockMachine(ReadingClientMockConfig clientMockConfig) {

        this.clientMockConfig = clientMockConfig;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }

    public void launch() {

        var clientIds = clientMockConfig.getClientIds();

        var executorService = Executors.newFixedThreadPool(clientIds.size());

        clientIds.forEach(clientId ->
                executorService.execute(() -> {

                    Long startTime = System.currentTimeMillis();

                    var clientMocker = applicationContext.getBean(ReadingClientMocker.class);

                    clientMocker.init(clientId);

                    clientMocker.mock();

                    Long endTime = System.currentTimeMillis();

                    log.info(
                            "Client-ID: {}, Time-Elapsed: {}",
                            clientId,
                            endTime - startTime
                    );
                })
        );

//        executorService.shutdown();
    }
}
