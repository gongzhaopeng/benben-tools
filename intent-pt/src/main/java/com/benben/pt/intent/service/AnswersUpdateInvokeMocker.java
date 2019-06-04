package com.benben.pt.intent.service;

import com.benben.pt.intent.configuration.ModuleAnswersSampleConfiguration;
import com.benben.pt.intent.configuration.PtConfiguration;
import com.benben.pt.intent.model.Module;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class AnswersUpdateInvokeMocker {

    private final RestTemplate restTemplate;
    private final PtConfiguration ptConfiguration;
    private final ModuleAnswersSampleConfiguration moduleAnswersSampleConfiguration;

    @Autowired
    public AnswersUpdateInvokeMocker(
            final RestTemplate restTemplate,
            final PtConfiguration ptConfiguration,
            final ModuleAnswersSampleConfiguration moduleAnswersSampleConfiguration) {

        this.restTemplate = restTemplate;
        this.ptConfiguration = ptConfiguration;
        this.moduleAnswersSampleConfiguration = moduleAnswersSampleConfiguration;
    }

    public void mock() {

        final var reqBody = new RequestBody();
        reqBody.setOwnerId(ptConfiguration.getUserId());
        reqBody.setAnswers(moduleAnswersSampleConfiguration.getModule().getAnswers());

        final var respEntity = restTemplate.exchange(
                ptConfiguration.getAnswersUpdatePath(),
                HttpMethod.PUT,
                new HttpEntity<>(reqBody),
                String.class,
                ptConfiguration.getAssessmentId(),
                moduleAnswersSampleConfiguration.getModule().getId());

        assert respEntity.getStatusCode() == HttpStatus.OK;
    }

    @Data
    static class RequestBody {

        private String ownerId;

        private List<Module.Answer> answers;
    }
}
