package dev.sunusante.gateway.domain;

import static dev.sunusante.gateway.domain.UserAccountTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import dev.sunusante.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAccount.class);
        UserAccount userAccount1 = getUserAccountSample1();
        UserAccount userAccount2 = new UserAccount();
        assertThat(userAccount1).isNotEqualTo(userAccount2);

        userAccount2.setId(userAccount1.getId());
        assertThat(userAccount1).isEqualTo(userAccount2);

        userAccount2 = getUserAccountSample2();
        assertThat(userAccount1).isNotEqualTo(userAccount2);
    }
}
