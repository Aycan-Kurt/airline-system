package com.aycan.airlinegateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SearchRateLimitFilter implements GatewayFilter {

    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private LocalDate currentDay = LocalDate.now();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LocalDate today = LocalDate.now();

        if (!today.equals(currentDay)) {
            requestCounts.clear();
            currentDay = today;
        }

        String ip = exchange.getRequest().getRemoteAddress() != null
                && exchange.getRequest().getRemoteAddress().getAddress() != null
                ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                : "unknown";

        requestCounts.putIfAbsent(ip, new AtomicInteger(0));

        int count = requestCounts.get(ip).incrementAndGet();

        if (count > 3) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}