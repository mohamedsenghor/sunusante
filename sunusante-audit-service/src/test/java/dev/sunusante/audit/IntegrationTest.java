package dev.sunusante.audit;

import dev.sunusante.audit.config.AsyncSyncConfiguration;
import dev.sunusante.audit.config.EmbeddedKafka;
import dev.sunusante.audit.config.EmbeddedSQL;
import dev.sunusante.audit.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { SunusanteAuditServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
