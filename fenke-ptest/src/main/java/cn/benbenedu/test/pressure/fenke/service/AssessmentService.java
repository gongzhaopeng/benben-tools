package cn.benbenedu.test.pressure.fenke.service;

import cn.benbenedu.test.pressure.fenke.configuration.RequestUrlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AssessmentService {

    private final RestTemplate restTemplate;
    private final RequestUrlConfiguration requestUrlConfiguration;

    public AssessmentService(
            final RestTemplate restTemplate,
            final RequestUrlConfiguration requestUrlConfiguration) {

        this.restTemplate = restTemplate;
        this.requestUrlConfiguration = requestUrlConfiguration;
    }

    public String fetchAssessment(
            final MultiValueMap<String, String> cookie) {

        final var reqEntity = new HttpEntity<>(
                null, cookie);

        final var respEntity = restTemplate.exchange(
                requestUrlConfiguration.getAssessment(),
                HttpMethod.GET,
                reqEntity,
                String.class);

        assert respEntity.getStatusCode().is2xxSuccessful();

        return respEntity.getBody();
    }
}
