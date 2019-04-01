package edu.benben.tools.readingclientmock.model;

import lombok.Data;

@Data
public class Choice {

    private String key;
    private String value;
    private String valueType;
    private Boolean isAnswer;
    private String score;
}
