package dev.sunusante.gateway.web.rest;

import dev.sunusante.gateway.service.MfaService;
import dev.sunusante.gateway.security.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/account")
public class MfaResource {

    private final MfaService mfaService;

    public MfaResource(MfaService mfaService) {
        this.mfaService = mfaService;
    }

    @PostMapping("/mfa/setup")
    public Mono<ResponseEntity<String>> setupMfa() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(mfaService::setupMfa)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/mfa/validate")
    public Mono<ResponseEntity<Boolean>> validateMfa(@RequestBody int code, org.springframework.web.server.ServerWebExchange exchange) {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(login -> mfaService.validateMfa(login, code)
                .flatMap(isValid -> {
                    if (isValid) {
                        return exchange.getSession().map(session -> {
                            session.getAttributes().put("MFA_VALIDATED", true);
                            return ResponseEntity.ok(true);
                        });
                    }
                    return Mono.just(ResponseEntity.ok(false));
                })
            )
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
