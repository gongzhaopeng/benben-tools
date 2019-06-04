package com.benben.pt.intent.configuration;

import com.benben.pt.intent.model.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;

@Component
public class ModuleAnswersSampleConfiguration implements InitializingBean {

    @Value("classpath:moduleAnswers.json")
    private Resource moduleResource;

    final private ObjectMapper objectMapper;

    @Getter
    private Module module;

    public ModuleAnswersSampleConfiguration(
            final ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        module = objectMapper.readValue(
                Files.readString(moduleResource.getFile().toPath()),
                Module.class);

        assert module != null;
    }
}
