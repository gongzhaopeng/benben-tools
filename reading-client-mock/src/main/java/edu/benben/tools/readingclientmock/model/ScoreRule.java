package edu.benben.tools.readingclientmock.model;

import lombok.Data;

@Data
public class ScoreRule {

    private String id;
    private String title;
    private String description;
    private Integer score;
    private String custom;
    private Long created_time;
    private Long updated_time;
}
