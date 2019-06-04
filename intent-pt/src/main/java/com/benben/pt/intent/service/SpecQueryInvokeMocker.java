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

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class SpecQueryInvokeMocker {

    private static final List<Integer> YEAR_DOMAIN =
            List.of(2015, 2016);

    private final RestTemplate restTemplate;
    private final PtConfiguration ptConfiguration;
    private final ProvinceConfiguration provinceConfiguration;
    private final MajorConfiguration majorConfiguration;
    private final JsonUtility jsonUtility;

    @Autowired
    public SpecQueryInvokeMocker(
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

        final var reqBody = new RequestBody();

        final var provinceInfos = provinceConfiguration.getProvinceInfos();
        final var selectedProvince = provinceInfos.get(
                new Random().nextInt(provinceInfos.size()));
        final var province = selectedProvince.getName().substring(0, 2);
        reqBody.setProvince(province);

        final var levelChoice = new Random().nextInt(3);
        if (levelChoice == 0) {
            reqBody.setIs211(true);
            reqBody.setIs985(false);
            reqBody.setCollegeLevel(null);
        } else if (levelChoice == 1) {
            reqBody.setIs211(false);
            reqBody.setIs985(true);
            reqBody.setCollegeLevel(null);
        } else {
            reqBody.setIs211(false);
            reqBody.setIs985(false);
            reqBody.setCollegeLevel("普通本科");
        }

        reqBody.setYear(YEAR_DOMAIN.get(
                new Random().nextInt(YEAR_DOMAIN.size())));
        reqBody.setBottom(500);
        reqBody.setTop(600);

        final var majorInfos = majorConfiguration.getMajorInfos();
        final var selectedMajor = majorInfos.get(
                new Random().nextInt(majorInfos.size()));
        reqBody.setMajorName(selectedMajor.getName());

        reqBody.setPageSize(20);

        final var respEntity = restTemplate.postForEntity(
                ptConfiguration.getSpecQueryPath(), reqBody, String.class);
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

        private String province;

        /**
         * one in three.
         */
        private Boolean is985;
        private Boolean is211;
        private String collegeLevel;

        private Integer year;
        private Integer bottom;
        private Integer top;
        private String majorName;

        private Integer pageSize;
    }
}
