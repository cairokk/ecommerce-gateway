package br.unifor.ecommerce.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class RouteValidator {

    public final Predicate<ServerHttpRequest> isSecured =
            request -> request.getURI().getPath().matches("/clientes/.*|/pedidos/.*|/produtos/.*|/pagamentos/.*");
}

