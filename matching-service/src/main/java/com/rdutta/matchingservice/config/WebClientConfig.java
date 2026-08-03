package com.rdutta.matchingservice.config;

import com.rdutta.matchingservice.adapter.output.http.DriverLocationClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Value("${driver.location.service.url}")
    private String driverLocationServiceUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public DriverLocationClient driverLocationClient(WebClient.Builder builder) {
        WebClient webClient = builder.baseUrl(driverLocationServiceUrl).build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();

        return factory.createClient(DriverLocationClient.class);
    }
}