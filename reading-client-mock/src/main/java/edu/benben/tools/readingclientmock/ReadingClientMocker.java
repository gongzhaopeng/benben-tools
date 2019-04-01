package edu.benben.tools.readingclientmock;

import edu.benben.tools.readingclientmock.configuration.ReadingClientMockConfig;
import edu.benben.tools.readingclientmock.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class ReadingClientMocker {

    private final Random random = new Random();

    private ReadingClientMockConfig clientMockConfig;
    private RestTemplate restTemplate;
    private String openid;
    private MultiValueMap<String, String> requestHeaders;
    private ReadingUser readingUser;

    @Autowired
    public ReadingClientMocker(
            RestTemplate restTemplate,
            ReadingClientMockConfig clientMockConfig) {

        this.clientMockConfig = clientMockConfig;
        this.restTemplate = restTemplate;

        this.requestHeaders = new LinkedMultiValueMap<>();
    }

    public void init(String openid) {

        this.openid = openid;

        var loginResultEntity = restTemplate.getForEntity(
                clientMockConfig.getServerBasePath() + clientMockConfig.getLoginPath(),
                String.class,
                openid);

        loginResultEntity.getHeaders().get("Set-Cookie").forEach(
                item -> requestHeaders.add("Cookie", item));

        var confirmUserRequestEntity = new HttpEntity<>(
                Map.of("name", UUID.randomUUID().toString().replaceAll("-", "")),
                requestHeaders);
        readingUser = restTemplate.exchange(
                clientMockConfig.getServerBasePath() + clientMockConfig.getConfirmUserPath(),
                HttpMethod.POST,
                confirmUserRequestEntity,
                ReadingUser.class).getBody();
    }

    public void mock() {

        mockReadingPart();
        mockRecognitionPart();
        mockParagraphPart();
        mockVocabularyPart();
    }

    private void mockReadingPart() {

        var random = new Random();
        var chosenPaperId = clientMockConfig.getReadingPaperIds().get(
                random.nextInt(clientMockConfig.getReadingPaperIds().size()));

        var assessment = fetchAssessment(chosenPaperId);

        commitInitialResult(chosenPaperId);

        var questionSet = assessment.getQuestionSet().getQuestionSet().get(1);
        questionSet.getQuestions().subList(0, questionSet.getQuestions().size() - 1)
                .forEach(question ->
                        commitRandomAnswerByQuestion(questionSet, question, false)
                );
        commitRandomAnswerByQuestion(
                questionSet,
                questionSet.getQuestions().get(questionSet.getQuestions().size() - 1),
                true);
    }

    private void mockRecognitionPart() {

        var paperId = clientMockConfig.getRecognitionPaperId();

        var assessment = fetchAssessment(paperId);

        commitInitialResult(paperId);

        var validQuestionSet = assessment.getQuestionSet().getQuestionSet().get(1).getQuestionSet();
        validQuestionSet.subList(2, 11).forEach(questionSet ->
                questionSet.getQuestions().forEach(question ->
                        commitRandomAnswerByQuestion(questionSet, question, false)
                )
        );

        var lastQuestionSet = validQuestionSet.get(11);
        lastQuestionSet.getQuestions().subList(0, lastQuestionSet.getQuestions().size() - 1)
                .forEach(question ->
                        commitRandomAnswerByQuestion(lastQuestionSet, question, false)
                );
        commitRandomAnswerByQuestion(
                lastQuestionSet,
                lastQuestionSet.getQuestions().get(lastQuestionSet.getQuestions().size() - 1),
                true);
    }

    private void mockParagraphPart() {

        var paperId = clientMockConfig.getParagraphPaperId();

        var assessment = fetchAssessment(paperId);

        commitInitialResult(paperId);

        var questionSet = assessment.getQuestionSet().getQuestionSet().get(2);
        questionSet.getQuestions().subList(0, questionSet.getQuestions().size() - 1)
                .forEach(question ->
                        commitRandomAnswerByQuestion(questionSet, question, false)
                );
        commitRandomAnswerByQuestion(
                questionSet,
                questionSet.getQuestions().get(questionSet.getQuestions().size() - 1),
                true);
    }

    private void mockVocabularyPart() {

        var paperId = clientMockConfig.getVocabularyPaperId();

        var assessment = fetchAssessment(paperId);

        commitInitialResult(paperId);

        var validQuestionSet = assessment.getQuestionSet().getQuestionSet();
        validQuestionSet.subList(2, validQuestionSet.size() - 1).forEach(questionSet ->
                questionSet.getQuestions().forEach(question ->
                        commitRandomAnswerByQuestion(questionSet, question, false)
                )
        );

        var lastQuestionSet = validQuestionSet.get(validQuestionSet.size() - 1);
        lastQuestionSet.getQuestions().subList(0, lastQuestionSet.getQuestions().size() - 1)
                .forEach(question ->
                        commitRandomAnswerByQuestion(lastQuestionSet, question, false)
                );
        commitRandomAnswerByQuestion(
                lastQuestionSet,
                lastQuestionSet.getQuestions().get(lastQuestionSet.getQuestions().size() - 1),
                true);
    }

    private Assessment fetchAssessment(String paperId) {

        var requestEntity = new HttpEntity<>(null, requestHeaders);
        return restTemplate.exchange(
                clientMockConfig.getServerBasePath() + clientMockConfig.getExamPaperPath(),
                HttpMethod.GET,
                requestEntity,
                Assessment.class,
                paperId).getBody();
    }

    private void commitInitialResult(String paperId) {

        var infoAnswerSet = new AnswerSet();

        infoAnswerSet.setPath("/info");

        var answers = List.of(
                new AnswerSet.Answer("58d225ea5bb5c31c603770d1", readingUser.getId()),
                new AnswerSet.Answer("5923db4f02dc7527f8a1ea4d", readingUser.getRegion().concat()),
                new AnswerSet.Answer("58d225ea5bb5c31c603770d2", readingUser.getName()),
                new AnswerSet.Answer("58d225ea5bb5c31c603770d3", readingUser.getGrade()),
                new AnswerSet.Answer("58d225ea5bb5c31c603770d4", readingUser.getSex())
        );
        infoAnswerSet.setAnswers(answers);

        var initialResult = new InitialResult();
        initialResult.setTestId(paperId);
        initialResult.setInfoAnswerSet(infoAnswerSet);

        var commitInitialInfoRequestEntity = new HttpEntity<>(
                initialResult,
                requestHeaders);
        restTemplate.exchange(
                clientMockConfig.getServerBasePath() + clientMockConfig.getResultInitPath(),
                HttpMethod.POST,
                commitInitialInfoRequestEntity,
                String.class);
    }

    private void commitRandomAnswerByQuestion(QuestionSet questionSet, Question question, Boolean over) {

        var questionWrapper = new CommittingAnswerWrapper();

        questionWrapper.setOver(over);

        var answer = new AnswerSet.Answer();
        answer.setAddition(UUID.randomUUID().toString());
        answer.setQuestion(new AnswerSet.Question(question.getId()));
        answer.setTime((long) random.nextInt(100));
        var choiceIndex = random.nextInt(question.getChoices().size());
        var choice = question.getChoices().get(choiceIndex);
        answer.setAnswer(choice.getKey());
        questionWrapper.setAnswer(answer);

        questionWrapper.setPath(questionSet.getPath());

        var readTempStateDto = new CommittingAnswerWrapper.ReadTempStateDto();
        readTempStateDto.setRemainTime(random.nextInt(1000));
        var coordinate = new CommittingAnswerWrapper.Coordinate();
        coordinate.setAnsweredCount(random.nextInt(150));
        coordinate.setFullPath(List.of("path-1", "path-2", "path-3"));
        coordinate.setPresentPath("present-path");
        coordinate.setQuestionId(List.of("mock-q-1", "mock-q-2", "mock-q-3", "mock-q4", "mock-q-5"));
        coordinate.setRightCount(random.nextInt(100));
        readTempStateDto.setCoordinate(coordinate);
        questionWrapper.setReadTempStateDto(readTempStateDto);

        var commitAnswerRequestEntity = new HttpEntity<>(
                questionWrapper,
                requestHeaders);
        restTemplate.exchange(
                clientMockConfig.getServerBasePath() + clientMockConfig.getCommitSingleAnswerPath(),
                HttpMethod.POST,
                commitAnswerRequestEntity,
                String.class);

        try {
            TimeUnit.MILLISECONDS.sleep(clientMockConfig.getAnswerCommitDelay());
        } catch (Exception e) {

        }
    }
}
