package dev.sunusante.dmp;

import dev.sunusante.dmp.config.AsyncSyncConfiguration;
import dev.sunusante.dmp.config.EmbeddedKafka;
import dev.sunusante.dmp.config.EmbeddedSQL;
import dev.sunusante.dmp.config.JacksonConfiguration;
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
@SpringBootTest(classes = { SunusanteDmpServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
