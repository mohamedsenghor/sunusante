package dev.sunusante.dmp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PrescriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Prescription getPrescriptionSample1() {
        return new Prescription().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Prescription getPrescriptionSample2() {
        return new Prescription().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Prescription getPrescriptionRandomSampleGenerator() {
        return new Prescription()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
