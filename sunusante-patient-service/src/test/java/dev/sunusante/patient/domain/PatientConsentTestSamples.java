package dev.sunusante.patient.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PatientConsentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PatientConsent getPatientConsentSample1() {
        return new PatientConsent().id(1L).doctorLogin("doctorLogin1");
    }

    public static PatientConsent getPatientConsentSample2() {
        return new PatientConsent().id(2L).doctorLogin("doctorLogin2");
    }

    public static PatientConsent getPatientConsentRandomSampleGenerator() {
        return new PatientConsent().id(longCount.incrementAndGet()).doctorLogin(UUID.randomUUID().toString());
    }
}
