package dev.sunusante.gateway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserAccountTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserAccount getUserAccountSample1() {
        return new UserAccount().id(1L).mfaSecret("mfaSecret1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static UserAccount getUserAccountSample2() {
        return new UserAccount().id(2L).mfaSecret("mfaSecret2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static UserAccount getUserAccountRandomSampleGenerator() {
        return new UserAccount()
            .id(longCount.incrementAndGet())
            .mfaSecret(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
