package org.tokio.spring.resources.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Slf4j
public class StringHelper {

    private StringHelper() {}

    /**
     * Checks that the specified object reference is not {@code null} and contains black spaces.
     *
     * @param field the object reference to check for nullity
     * @return {@code field} if not {@code null}
     * @throws NullPointerException if {@code field} is {@code null}
     */
    public static String verifyStringRequired(String field) throws NullPointerException {
        return Objects.requireNonNull(StringUtils.stripToNull(field));
    }
}
