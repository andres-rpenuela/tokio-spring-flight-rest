package org.tokio.spring.resources.helper.ut;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.tokio.spring.resources.helper.StringHelper;

@ActiveProfiles("test")
class StringHelperTest {

    @Test
    void givenStringNull_whenVerifyStringRequired_returnError() {

        Assertions.assertThatThrownBy(() -> StringHelper.verifyStringRequired(" "))
                .isInstanceOf(NullPointerException.class);

    }
}