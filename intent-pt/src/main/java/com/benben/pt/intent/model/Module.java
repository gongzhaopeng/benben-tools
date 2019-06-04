package com.benben.pt.intent.model;


import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Module {

    private String id;
    private List<Answer> answers;

    @Data
    public static class Answer {

        private String questionId;
        private Map<String, Object> content;
    }
}
