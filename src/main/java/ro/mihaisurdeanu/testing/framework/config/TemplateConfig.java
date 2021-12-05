package ro.mihaisurdeanu.testing.framework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @author Mihai Surdeanu
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "config.template")
public class TemplateConfig {

    @Getter
    @Setter
    private int connectionTimeout = 15; // in seconds

    @Getter
    @Setter
    private int readTimeout = 120; // in seconds

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(connectionTimeout))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }

}
