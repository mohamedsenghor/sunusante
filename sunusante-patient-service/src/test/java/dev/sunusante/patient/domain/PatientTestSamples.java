package dev.sunusante.patient.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PatientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Patient getPatientSample1() {
        return new Patient().id(1L).login("login1").pseudo("pseudo1").firstName("firstName1").lastName("lastName1").idValue("idValue1");
    }

    public static Patient getPatientSample2() {
        return new Patient().id(2L).login("login2").pseudo("pseudo2").firstName("firstName2").lastName("lastName2").idValue("idValue2");
    }

    public static Patient getPatientRandomSampleGenerator() {
        return new Patient()
            .id(longCount.incrementAndGet())
            .login(UUID.randomUUID().toString())
            .pseudo(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .idValue(UUID.randomUUID().toString());
    }
}
