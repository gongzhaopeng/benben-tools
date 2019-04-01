package edu.benben.tools.readingclientmock.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class AnswerSet {

    private String path;
    private List<Answer> answers;
    private Long createdTime;
    private Long updateTime;

    @Data
    @NoArgsConstructor
    public static class Answer {

        public Answer(String questionId, String answer) {

            this.question = new Question();
            this.question.setId(questionId);

            this.answer = answer;
        }

        private Question question;
        private String answer;
        private Long time;
        private String addition;
    }

    @Data
    @NoArgsConstructor
    public static class Question {

        public Question(String id) {

            this.id = id;
        }

        private String id;
    }
}
