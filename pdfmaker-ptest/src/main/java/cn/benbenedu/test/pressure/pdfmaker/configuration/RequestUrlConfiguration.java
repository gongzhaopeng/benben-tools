package cn.benbenedu.test.pressure.pdfmaker.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("request-url")
@Data
public class RequestUrlConfiguration {

    private String inquirePdf;
}
