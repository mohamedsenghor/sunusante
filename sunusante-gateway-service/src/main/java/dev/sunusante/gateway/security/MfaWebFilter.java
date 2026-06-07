package dev.sunusante.gateway.security;

import dev.sunusante.gateway.repository.UserAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class MfaWebFilter implements WebFilter {

    private final UserAccountRepository userAccountRepository;

    public MfaWebFilter(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        
        // Protect sensitive paths
        if (path.startsWith("/api/medical-entries/")) {
            return SecurityUtils.getCurrentUserLogin()
                .flatMap(userAccountRepository::findByInternalUserLogin)
                .flatMap(userAccount -> {
                    // Check if MFA is configured
                    if (userAccount.getMfaSecret() != null) {
                        // Check if validated in session (simplified: check a session attribute)
                        return exchange.getSession().map(session -> {
                            Boolean validated = (Boolean) session.getAttribute("MFA_VALIDATED");
                            return Boolean.TRUE.equals(validated);
                        });
                    }
                    return Mono.just(true); // MFA not configured, pass
                })
                .defaultIfEmpty(true)
                .flatMap(isAllowed -> {
                    if (isAllowed) {
                        return chain.filter(exchange);
                    } else {
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                });
        }
        return chain.filter(exchange);
    }
}
