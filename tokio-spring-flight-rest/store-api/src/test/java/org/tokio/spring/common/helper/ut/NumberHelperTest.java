package org.tokio.spring.common.helper.ut;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInfo;
import org.tokio.spring.common.helper.NumberHelper;

class NumberHelperTest {

    //@Test
    @RepeatedTest(50)
    void givenMax_whenIntegerRandom_thenReturnSuccessANumberBetweenZeroAndMax(TestInfo testInfo) {

        int result = NumberHelper.integerRandom(NumberHelper.THEN);
        Assertions.assertThat(result).isGreaterThanOrEqualTo(NumberHelper.ZERO);
        Assertions.assertThat(result).isLessThanOrEqualTo(NumberHelper.THEN);
    }

    //@Test
    @RepeatedTest(50)
    void givenMinAndMax_whenIntegerRandom_thenReturnSuccessANumberBetweenMinZeroAndMax() {
        int result = NumberHelper.integerRandom(NumberHelper.THEN);
        Assertions.assertThat(result).isGreaterThanOrEqualTo(NumberHelper.ZERO);
        Assertions.assertThat(result).isLessThanOrEqualTo(NumberHelper.THEN);
    }
}