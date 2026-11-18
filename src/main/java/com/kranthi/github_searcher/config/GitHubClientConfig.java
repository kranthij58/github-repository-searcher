package com.kranthi.github_searcher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GitHubClientConfig {

    @Bean
    public RestClient gitHubRestClient(){
        return RestClient.builder()
                .baseUrl("https://api.github.com")
                .build();

    }

}
