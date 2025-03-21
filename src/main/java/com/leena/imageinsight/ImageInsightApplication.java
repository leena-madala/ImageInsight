package com.leena.imageinsight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ImageInsightApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageInsightApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
