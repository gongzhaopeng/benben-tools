package cn.benbenedu.test.pressure.pdfmaker.service;

import cn.benbenedu.test.pressure.pdfmaker.configuration.RequestUrlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PdfAcquiringService {

    private final RestTemplate restTemplate;
    private final RequestUrlConfiguration requestUrlConfiguration;

    private final String PDF_GENERATING = "100001";
    private final String PDF_FAILURE = "100002";

    private final Duration POLL_INTERVAL = Duration.ofSeconds(5);

    public PdfAcquiringService(
            final RestTemplate restTemplate,
            final RequestUrlConfiguration requestUrlConfiguration) {

        this.restTemplate = restTemplate;
        this.requestUrlConfiguration = requestUrlConfiguration;
    }

    public void inquirePdf(final String openid)
            throws InterruptedException {

        ResponseEntity<String> respEntity;

        try {
            respEntity = restTemplate.exchange(
                    requestUrlConfiguration.getInquirePdf(),
                    HttpMethod.GET,
                    null,
                    String.class,
                    openid,
                    openid);
        } catch (RestClientException rce) {

            log.error(
                    String.format("Fail to inquire FDF for openid: %s", openid),
                    rce);

            throw rce;
        }

        assert respEntity.getStatusCode().is2xxSuccessful();

        final var respBody = respEntity.getBody();
        if (PDF_GENERATING.equals(respBody)) {

            TimeUnit.SECONDS.sleep(POLL_INTERVAL.toSeconds());

            inquirePdf(openid);
        } else if (PDF_FAILURE.equals(respBody)) {

            log.error("Fail to generate PDF for openid: {}", openid);
        } else {

            log.info("URL of the PDF for openid {}: {}", openid, respBody);
        }
    }
}
