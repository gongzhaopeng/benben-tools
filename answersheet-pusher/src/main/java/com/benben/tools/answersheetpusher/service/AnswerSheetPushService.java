package com.benben.tools.answersheetpusher.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

@Service
@Slf4j
public class AnswerSheetPushService {

    @Value("${service.exam.answersheet.address}")
    private String answerSheetPostAddress;

    private final RestTemplate restTemplate;

    @Autowired
    public AnswerSheetPushService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public void commitRawAnswerSheet(String rawAnswerSheet, String ticketId) {

        log.info("Posting with ticketId:{}", ticketId);

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        var httpEntity = new HttpEntity<>(rawAnswerSheet, httpHeaders);

        restTemplate.postForEntity(
                answerSheetPostAddress,
                httpEntity,
                String.class,
                ticketId);
    }
}
