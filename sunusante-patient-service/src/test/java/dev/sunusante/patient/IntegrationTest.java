package dev.sunusante.patient;

import dev.sunusante.patient.config.AsyncSyncConfiguration;
import dev.sunusante.patient.config.EmbeddedKafka;
import dev.sunusante.patient.config.EmbeddedSQL;
import dev.sunusante.patient.config.JacksonConfiguration;
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
@SpringBootTest(classes = { SunusantePatientServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
