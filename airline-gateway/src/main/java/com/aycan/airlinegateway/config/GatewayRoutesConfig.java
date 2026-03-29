package com.aycan.airlinegateway.config;

import com.aycan.airlinegateway.filter.SearchRateLimitFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Value("${backend.base-url}")
    private String backendBaseUrl;

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder, SearchRateLimitFilter searchRateLimitFilter) {
        return builder.routes()
                .route("flight-search-rate-limited", r -> r
                        .path("/api/v1/flights/search")
                        .filters(f -> f.filter(searchRateLimitFilter))
                        .uri(backendBaseUrl))
                .route("airline-api", r -> r
                        .path("/api/v1/**")
                        .uri(backendBaseUrl))
                .build();
    }
}