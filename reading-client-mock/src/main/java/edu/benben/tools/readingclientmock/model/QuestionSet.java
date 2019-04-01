package edu.benben.tools.readingclientmock.model;

import lombok.Data;

import java.util.List;

@Data
public class QuestionSet {

    /**
     * questionSet路径
     */
    private String path;
    /**
     * QuestionSet标题
     */
    private String title;
    /**
     * 子标题
     */
    private String subtitle;
    /**
     * 总结
     */
    private String summary;
    /**
     * 详细描述
     */
    private String description;
    /**
     * 子题集
     */
    private List<QuestionSet> questionSet;
    /**
     * 包含的题目
     */
    private List<Question> questions;
    /**
     * 本题集计分规则
     */
    private Rule scoreRule;
    /**
     * 时间限制
     */
    private Integer limited_time;
}
