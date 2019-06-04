package com.benben.pt.intent.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pt")
@Data
public class PtConfiguration {

    private String specRecommendPath;
    private String specQueryPath;
    private String profileUpdatePath;
    private String answersUpdatePath;

    private String userId;
    private String assessmentId;
}
