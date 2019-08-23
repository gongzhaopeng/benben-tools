package cn.benbenedu.test.pressure.fenke.service;

import cn.benbenedu.test.pressure.fenke.configuration.RequestUrlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class LoginService {

    private final RestTemplate restTemplate;
    private final RequestUrlConfiguration requestUrlConfiguration;

    public LoginService(
            final RestTemplate restTemplate,
            final RequestUrlConfiguration requestUrlConfiguration) {

        this.restTemplate = restTemplate;
        this.requestUrlConfiguration = requestUrlConfiguration;
    }

    public MultiValueMap<String, String> login(
            String email, String password) {

        final var form = new LinkedMultiValueMap<String, String>();
        form.add("email", email);
        form.add("password", password);
        form.add("urlType", "fenke.user");
        final var loginRequest =
                new HttpEntity<>(form, null);

        final var loginResultEntity = restTemplate.postForEntity(
                requestUrlConfiguration.getLogin(),
                loginRequest,
                String.class);

        final var cookie = new LinkedMultiValueMap<String, String>();
        loginResultEntity.getHeaders().get("Set-Cookie").forEach(
                item -> cookie.add("Cookie", item));

        return cookie;
    }
}
