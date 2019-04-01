package com.benben.tools.answersheetpusher.scheduler;

import com.benben.tools.answersheetpusher.service.AdmissionTicketService;
import com.benben.tools.answersheetpusher.service.AnswerSheetPushService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class AnswerSheetPushScheduler {

    private final AdmissionTicketService admissionTicketService;

    private final AnswerSheetPushService answerSheetPushService;

    private final ObjectMapper objectMapper;

    private List<Object> sampleAnswerSheets;

    private AtomicInteger counter;

    @Autowired
    public AnswerSheetPushScheduler(AdmissionTicketService admissionTicketService,
                                    AnswerSheetPushService answerSheetPushService)
            throws Exception {

        this.admissionTicketService = admissionTicketService;
        this.answerSheetPushService = answerSheetPushService;

        objectMapper = new ObjectMapper();

        counter = new AtomicInteger(0);

        sampleAnswerSheets = new ArrayList<>();
        parseSampleAnswerSheets("answer_sheet_jssw.json");
        parseSampleAnswerSheets("answer_sheet_zhsz.json");
    }

    @Scheduled(fixedRate = 1)
    public void pushAnswerSheet() {
        var chosenAnswerSheet = (Map) sampleAnswerSheets.get(0);

        try {

            chosenAnswerSheet.put("id", UUID.randomUUID().toString());
            var tickerId = admissionTicketService.acquireAdmissionTicket();
            chosenAnswerSheet.put("refAdmissionTicket", tickerId);

            var jsonStringToPost =
                    objectMapper.writeValueAsString(chosenAnswerSheet);

            answerSheetPushService.commitRawAnswerSheet(jsonStringToPost
                    , tickerId);

            log.info("Count posted: {}", counter.addAndGet(1));
        } catch (Exception e) {

            log.info("Fail to push: ", e);
        }
    }

    private void parseSampleAnswerSheets(String rawFile)
            throws Exception {

        var rawResource = new ClassPathResource(rawFile);
        var rawString = new String(
                rawResource.getInputStream().readAllBytes());
        var jsonParser = JsonParserFactory.getJsonParser();
        jsonParser.parseList(rawString).forEach(sampleAnswerSheets::add);
    }
}
