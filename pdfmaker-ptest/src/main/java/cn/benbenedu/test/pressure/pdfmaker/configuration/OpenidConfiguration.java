package cn.benbenedu.test.pressure.pdfmaker.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.List;

@Component
public class OpenidConfiguration implements InitializingBean {

    @Value("classpath:openids.json")
    private Resource openidResource;

    final private ObjectMapper objectMapper;

    @Getter
    private List<String> openids;

    @Autowired
    public OpenidConfiguration(
            final ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        openids = objectMapper.readValue(
                Files.readString(openidResource.getFile().toPath()),
                new TypeReference<List<String>>() {
                });

        assert openids.size() > 0;
    }
}
