package teamit.hust.ktxcdshustbe.config;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Log4j2
@Configuration
public class RestTemplateCustomizeConfig {

    @Value("${fpt.pay-gate.root.url.staging}")
    public String PFT_GATE_ROOT_URL;
    private static Integer TIMEOUT = 120000;
    @Bean
    RestTemplate restTemplateWithConnectReadTimeout() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(TIMEOUT))
                .setReadTimeout(Duration.ofMillis(TIMEOUT))
                .build();
    }

    @Bean
    RestTemplate restTemplateWithConnectTimeout() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(TIMEOUT))
                .build();
    }

    @Bean
    RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(TIMEOUT);
        requestFactory.setReadTimeout(TIMEOUT);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setUriTemplateHandler(new RootUriTemplateHandler(PFT_GATE_ROOT_URL));
        return restTemplate;
    }
}
