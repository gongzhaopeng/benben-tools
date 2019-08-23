package cn.benbenedu.test.pressure.fenke.service;

import cn.benbenedu.test.pressure.fenke.configuration.RequestUrlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;

@Service
@Slf4j
public class ResultsService implements InitializingBean {

    @Value("classpath:exampleResult.json")
    private Resource exampleResultResource;

    private String exampleResult;

    private final RestTemplate restTemplate;
    private final RequestUrlConfiguration requestUrlConfiguration;

    public ResultsService(
            final RestTemplate restTemplate,
            final RequestUrlConfiguration requestUrlConfiguration) {

        this.restTemplate = restTemplate;
        this.requestUrlConfiguration = requestUrlConfiguration;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        exampleResult =
                Files.readString(exampleResultResource.getFile().toPath());

        log.info("Example Result: {}", exampleResult);
    }

    public void submitResult(
            final MultiValueMap<String, String> cookie) {

        final var reqHeaders = new HttpHeaders(
                new LinkedMultiValueMap<>(cookie));
        reqHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        final var reqEntity = new HttpEntity<>(
                exampleResult, reqHeaders);

        final var respEntity = restTemplate.exchange(
                requestUrlConfiguration.getResults(),
                HttpMethod.POST,
                reqEntity,
                String.class);

        assert respEntity.getStatusCode().is2xxSuccessful();
    }
}
