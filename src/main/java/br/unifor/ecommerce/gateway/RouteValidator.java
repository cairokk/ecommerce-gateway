package br.unifor.ecommerce.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class RouteValidator {

    public Predicate<ServerHttpRequest> isSecured = request -> {
        String path = request.getURI().getPath();
        String method = request.getMethod() != null ? request.getMethod().name() : "";
        // Libera login e registro
        if (path.startsWith("/auth/login") || path.startsWith("/auth/registrar")) {
            return false;
        }

        // Libera consulta pública de produtos
        if (path.equals("/produtos") && method.equals("GET")) {
            return false;
        }

        return true; // tudo mais exige token
    };
}

