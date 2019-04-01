package edu.benben.tools.readingclientmock.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Question {

    /**
     * 题目ID
     */
    public String id;
    /**
     * 题目的别名，主要用于题目作者对题目的管理
     */
    public String alias;
    /**
     * 主要用于存储材料题题干、长时记忆的主题等
     */
    public Question theme;
    /**
     * 题干
     */
    public List<Map<String, String>> body;
    /**
     * 题目描述
     */
    public String summary;
    /**
     * 题目选项
     */
    public List<Choice> choices = new ArrayList<>();
    /**
     * 题目答案
     */
    public List<String> answer;
    /**
     * 题目难度
     */
    public Integer difficulty;
    /**
     * 题目是否是练习题
     */
    public Boolean isPractice;
    /**
     * 题目开发状态
     */
    public String status;
    /**
     * 题目创建时间
     */
    public Long created_time;
    /**
     * 题目修改时间
     */
    public Long updated_time;
    /**
     * 题目版本号(Git库中的版本号)
     */
    public String version;
    /**
     * 题目标签，用于分类处理题目
     */
    public List<String> tags;

    /**
     * 题目所有者
     */
    public String owner;

    /**
     * 维度，可以表示不同维度划分下的结果，所以选用map
     */
    public Map<String, String> dimension;

    /**
     * 提示，有些题目可能需要提示。
     */
    public String prompt;

    /**
     * 题目类型
     */
    public String classify;
}
