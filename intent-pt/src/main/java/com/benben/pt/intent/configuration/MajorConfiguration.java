package com.benben.pt.intent.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.List;

@Component
public class MajorConfiguration implements InitializingBean {

    @Value("classpath:major.json")
    private Resource majorResource;

    final private ObjectMapper objectMapper;

    @Getter
    private List<MajorInfo> majorInfos;

    @Autowired
    public MajorConfiguration(
            final ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        majorInfos = objectMapper.readValue(
                Files.readString(majorResource.getFile().toPath()),
                new TypeReference<List<MajorInfo>>() {
                });

        assert majorInfos.size() > 0;
    }

    @Data
    public static class MajorInfo {

        private String name;
    }
}
