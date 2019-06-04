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
public class ProvinceConfiguration implements InitializingBean {

    @Value("classpath:province.json")
    private Resource provinceResource;

    final private ObjectMapper objectMapper;

    @Getter
    private List<ProvinceInfo> provinceInfos;

    @Autowired
    public ProvinceConfiguration(
            final ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        provinceInfos = objectMapper.readValue(
                Files.readString(provinceResource.getFile().toPath()),
                new TypeReference<List<ProvinceInfo>>() {
                });

        assert provinceInfos.size() > 0;
    }

    @Data
    public static class ProvinceInfo {

        private String name;
        private Integer count;
    }
}
