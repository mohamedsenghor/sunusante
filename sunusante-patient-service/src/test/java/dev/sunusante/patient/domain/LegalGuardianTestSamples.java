package dev.sunusante.patient.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LegalGuardianTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LegalGuardian getLegalGuardianSample1() {
        return new LegalGuardian().id(1L).guardianType("guardianType1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static LegalGuardian getLegalGuardianSample2() {
        return new LegalGuardian().id(2L).guardianType("guardianType2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static LegalGuardian getLegalGuardianRandomSampleGenerator() {
        return new LegalGuardian()
            .id(longCount.incrementAndGet())
            .guardianType(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
