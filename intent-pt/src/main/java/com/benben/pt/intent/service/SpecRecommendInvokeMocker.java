package com.benben.pt.intent.service;

import com.benben.pt.intent.component.JsonUtility;
import com.benben.pt.intent.configuration.MajorConfiguration;
import com.benben.pt.intent.configuration.ProvinceConfiguration;
import com.benben.pt.intent.configuration.PtConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
@Slf4j
public class SpecRecommendInvokeMocker {

    private final RestTemplate restTemplate;
    private final PtConfiguration ptConfiguration;
    private final ProvinceConfiguration provinceConfiguration;
    private final MajorConfiguration majorConfiguration;
    private final JsonUtility jsonUtility;

    @Autowired
    public SpecRecommendInvokeMocker(
            final RestTemplate restTemplate,
            final PtConfiguration ptConfiguration,
            final ProvinceConfiguration provinceConfiguration,
            final MajorConfiguration majorConfiguration,
            final JsonUtility jsonUtility) {

        this.restTemplate = restTemplate;
        this.ptConfiguration = ptConfiguration;
        this.provinceConfiguration = provinceConfiguration;
        this.majorConfiguration = majorConfiguration;
        this.jsonUtility = jsonUtility;
    }

    public void mock() {

        final var provinceInfos = provinceConfiguration.getProvinceInfos();
        final var selectedProvince = provinceInfos.get(
                new Random().nextInt(provinceInfos.size()));

        final var majorInfos = majorConfiguration.getMajorInfos();
        final var selectedMajor = majorInfos.get(
                new Random().nextInt(majorInfos.size()));

        final var reqBody = new RequestBody();
        reqBody.setProvince(selectedProvince.getName());
        reqBody.setRank(
                new Random().nextInt(selectedProvince.getCount()) + 1);
        reqBody.setMajorName(selectedMajor.getName());

        final var respEntity = restTemplate.postForEntity(
                ptConfiguration.getSpecRecommendPath(), reqBody, String.class);
        if (!respEntity.getStatusCode().equals(HttpStatus.OK)) {
            log.warn("Status code is {} for request: {}",
                    respEntity.getStatusCode(),
                    jsonUtility.toJsonString(reqBody));
        }
//        log.info("\nResponse:\n{}\nfor request:\n{}",
//                respEntity.getBody(),
//                jsonUtility.toJsonString(reqBody));
    }

    @Data
    static class RequestBody {

        private Integer rank;
        private String province;
        private String majorName;
    }
}
