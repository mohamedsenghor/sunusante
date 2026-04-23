package dev.sunusante.dmp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MedicalEntryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MedicalEntry getMedicalEntrySample1() {
        return new MedicalEntry()
            .id(1L)
            .patientPseudo("patientPseudo1")
            .confidentialityLevel(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static MedicalEntry getMedicalEntrySample2() {
        return new MedicalEntry()
            .id(2L)
            .patientPseudo("patientPseudo2")
            .confidentialityLevel(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static MedicalEntry getMedicalEntryRandomSampleGenerator() {
        return new MedicalEntry()
            .id(longCount.incrementAndGet())
            .patientPseudo(UUID.randomUUID().toString())
            .confidentialityLevel(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
