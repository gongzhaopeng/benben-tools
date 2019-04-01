package com.benben.tools.answersheetpusher.service;

import com.benben.tools.answersheetpusher.model.AdmissionTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class AdmissionTicketService {

    @Value("${service.exam.admission_ticket.address}")
    private String admissionTicketAddress;

    @Value("${test.testid}")
    private String testId;

    private final RestTemplate restTemplate;

    @Autowired
    public AdmissionTicketService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public String acquireAdmissionTicket() {

        var requestBody = Map.of(
                "foreign_id", UUID.randomUUID().toString(),
                "identity", Map.of(
                        "birthday", "2019-03-08",
                        "first_name", "string",
                        "id_number", "string",
                        "last_name", "string",
                        "region", "string",
                        "sex", "string"
                ));

        return restTemplate.postForObject(admissionTicketAddress, requestBody,
                AdmissionTicket.class, testId).getId();
    }
}
