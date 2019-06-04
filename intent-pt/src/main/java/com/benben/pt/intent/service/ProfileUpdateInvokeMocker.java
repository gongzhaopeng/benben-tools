package com.benben.pt.intent.service;

import com.benben.pt.intent.configuration.PtConfiguration;
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
public class ProfileUpdateInvokeMocker {

    private final RestTemplate restTemplate;
    private final PtConfiguration ptConfiguration;

    @Autowired
    public ProfileUpdateInvokeMocker(
            final RestTemplate restTemplate,
            final PtConfiguration ptConfiguration) {

        this.restTemplate = restTemplate;
        this.ptConfiguration = ptConfiguration;
    }

    public void mock() {

        final var profile = new CustomProfile();
        profile.setGender("男");
        profile.setSubject("science");
        profile.setFavorCourses(List.of(
                "语文", "数学", "英语", "物理"));
        profile.setFavorSpecs(List.of(
                "物流管理", "文物与博物馆学", "汉语国际教育", "政治学与行政学"));
        profile.setLocation(List.of(
                "北京市", "北京市", "东城区"));
        profile.setSchool("北京四中");

        final var respEntity = restTemplate.exchange(
                ptConfiguration.getProfileUpdatePath(),
                HttpMethod.PUT,
                new HttpEntity<>(profile),
                String.class,
                ptConfiguration.getUserId());

        assert respEntity.getStatusCode() == HttpStatus.OK;
    }

    @Data
    static class CustomProfile {

        private String gender;
        /**
         * 文,理...分科
         */
        private String subject;
        /**
         * 语文,数学,英语,化学...
         */
        private List<String> favorCourses;
        /**
         * 偏爱的专业
         */
        private List<String> favorSpecs;
        private List<String> location;
        private String school;
    }
}
