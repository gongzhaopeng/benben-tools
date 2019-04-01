package edu.benben.tools.readingclientmock.model;

import lombok.Data;

import java.util.List;

@Data
public class Assessment {

    private String id;
    /**
     * 测评问卷标题
     */
    private String title;
    /**
     * 测评问卷子标题
     */
    private String subtitle;
    /**
     * 总结（参考Test）
     */
    private String summary;
    /**
     * 描述（参考Test）
     */
    private String description;
    /**
     * 测评问卷封面（目前尚未使用）
     */
    private String profile_pic;
    /**
     * 题目集合（是题目树形结构）
     */
    private QuestionSet questionSet;
    /**
     * 计分规则（弃用，转到QuestionSet内部设置）
     */
    private ScoreRule scoreRule;
    /**
     * 创建时间
     */
    private Long created_time;
    /**
     * 最近修改时间
     */
    private Long updated_time;
    /**
     * 期望的作答时间
     */
    private Long expired_time;
    /**
     * 问卷你状态
     */
    private String status;
    /**
     * 问卷所有者
     */
    private String owner;
    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 测试类型
     */
    private String configId;
}
