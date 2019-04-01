package edu.benben.tools.readingclientmock.model;

import lombok.Data;

import java.util.List;

@Data
public class CommittingAnswerWrapper {

    private Boolean over;
    private AnswerSet.Answer answer;
    private String path;
    private ReadTempStateDto readTempStateDto;

    @Data
    public static class ReadTempStateDto {

        private int remainTime;
        private Coordinate coordinate;
    }

    @Data
    public static class Coordinate {

        private List<String> fullPath;
        private List<String> questionId;
        private String presentPath;
        private Integer answeredCount;
        private Integer rightCount;
    }
}
