package edu.benben.tools.readingclientmock.model;

import lombok.Data;

@Data
public class Rule {

    public enum LEVEL{
        AVAILABLE, TEST, UNAVAILABLE
    }

    private String id;
    private String name;
    private String description;
    private LEVEL level;
    private String method;
}
