package com.benben.pt.intent.configuration;

import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @Primary
    public RestTemplate restTemplate(
            RestTemplateBuilder builder) throws Exception {

        final var sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .build();

        final var client = HttpClients.custom()
                .setSSLContext(sslContext)
                .build();

        final var restTemplate = builder.requestFactory(() ->
                new HttpComponentsClientHttpRequestFactory(client))
                .build();

        setDefaultCharsetOfStringHttpMessageConverter(restTemplate,
                Charset.forName("UTF-8"));

        return restTemplate;
    }

    private void setDefaultCharsetOfStringHttpMessageConverter(
            RestTemplate restTemplate, Charset defaultCharset) {

        for (final var converter : restTemplate.getMessageConverters()) {

            if (converter instanceof StringHttpMessageConverter) {

                final var stringConverter =
                        (StringHttpMessageConverter) converter;

                stringConverter.setDefaultCharset(defaultCharset);
            }
        }
    }
}
