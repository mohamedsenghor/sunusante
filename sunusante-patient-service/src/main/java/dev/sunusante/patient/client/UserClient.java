package dev.sunusante.patient.client;

import dev.sunusante.patient.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sunusanteGatewayService", configuration = FeignConfiguration.class)
public interface UserClient {
    @GetMapping("/api/users/{login}/exists")
    Boolean checkUserExists(@PathVariable("login") String login);
}
