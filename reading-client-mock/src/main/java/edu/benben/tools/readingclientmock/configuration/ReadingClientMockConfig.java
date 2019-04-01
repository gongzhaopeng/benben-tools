package edu.benben.tools.readingclientmock.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("reading.client.mock")
@Data
public class ReadingClientMockConfig {

    private String serverBasePath;
    private String loginPath;
    private String confirmUserPath;
    private String examPaperPath;
    private String resultInitPath;
    private String commitSingleAnswerPath;
    private List<String> readingPaperIds;
    private String recognitionPaperId;
    private String paragraphPaperId;
    private String vocabularyPaperId;
    private List<String> clientIds;
    private Integer answerCommitDelay;
}
