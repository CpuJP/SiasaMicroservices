package com.siasa.gateway.config;

import com.siasa.gateway.dto.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class AuthFilter implements GatewayFilter {

    private final WebClient.Builder webClient;

    @Value("${uri.auth.validate}")
    private String uriValidate;


    public AuthFilter(WebClient.Builder webClient) {
        this.webClient = webClient;
    }

    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return onError(exchange, HttpStatus.BAD_REQUEST);
        }
        String authToken = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
        String[] chunks = authToken.split(" ");
        if (chunks.length != 2 || !chunks[0].equals("Bearer")) {
            return onError(exchange, HttpStatus.BAD_REQUEST);
        }
        return webClient.build().get().uri(uriValidate + chunks[1]).retrieve()
                .bodyToMono(TokenDto.class)
                .map(t -> {
                    return exchange;
                }).flatMap(chain::filter);
    }
}
