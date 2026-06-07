package dev.sunusante.patient.config;

import dev.sunusante.patient.client.UserFeignClientInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableFeignClients(basePackages = "dev.sunusante.patient")
@Import(FeignClientsConfiguration.class)
public class FeignConfiguration {

    @Bean
    public UserFeignClientInterceptor getUserFeignClientInterceptor() {
        return new UserFeignClientInterceptor();
    }

    /**
     * Set the Feign specific log level to log client REST requests.
     */
    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.BASIC;
    }
}
