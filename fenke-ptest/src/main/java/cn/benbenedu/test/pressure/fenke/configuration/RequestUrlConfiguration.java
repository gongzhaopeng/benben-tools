package cn.benbenedu.test.pressure.fenke.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("request-url")
@Data
public class RequestUrlConfiguration {

    private String login;
    private String studentInfo;
    private String tests;
    private String assessment;
}
