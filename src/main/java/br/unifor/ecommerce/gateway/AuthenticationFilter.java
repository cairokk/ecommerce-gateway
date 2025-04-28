package br.unifor.ecommerce.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final JwtUtil jwtUtil;
    private final RouteValidator routeValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (routeValidator.isSecured.test(exchange.getRequest())) {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (!authHeader.startsWith("Bearer ")) {
                return handleUnauthorized(exchange, "Token JWT ausente ou inválido!");
            }

            authHeader = authHeader.substring(7);

            try {
                jwtUtil.validateToken(authHeader);
            } catch (Exception e) {
                return handleUnauthorized(exchange, e.getMessage());
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String error) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        byte[] bytes = error.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        exchange.getResponse().getHeaders().add("Content-Type", "text/plain");
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}

