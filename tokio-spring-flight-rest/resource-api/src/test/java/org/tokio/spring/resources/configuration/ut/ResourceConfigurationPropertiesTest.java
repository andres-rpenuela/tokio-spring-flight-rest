package org.tokio.spring.resources.configuration.ut;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.tokio.spring.resources.configuration.ResourceConfigurationProperties;
import org.tokio.spring.resources.helper.FileHelper;

import java.io.IOException;
import java.nio.file.Path;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ResourceConfigurationPropertiesTest {

    @Autowired
    ResourceConfigurationProperties resourceConfigurationProperties;

    @Test
    @Order(1)
    void givenPathRelative_whenGetResourcePathFromRelativePath_returnOk() throws IOException {
        final Path path = resourceConfigurationProperties.buildResourcePathFromRelativePathGivenNameResource();

        FileHelper.createWorkingIfNotExits(path);

        Assertions.assertThat(path)
                .exists()
                .isDirectory();

        FileHelper.deleteWorkIfNotExists(path);
    }

    @Test
    @Order(2)
    void givenPathRelative_whenGetResourcePathFromAbsolute_returnOk() throws IOException {
        final Path path = resourceConfigurationProperties.buildResourcePathFromAbsolutePath();

        FileHelper.createWorkingIfNotExits(path);

        Assertions.assertThat(path)
                .exists()
                .isDirectory();

        FileHelper.deleteWorkIfNotExists(path);
    }

    @Test
    @Order(3)
    void givenResourceName_whenGetResourcePathFromRelativePath_returnOk() throws IOException {
        final Path path = resourceConfigurationProperties.getResourcePathFromRelativePathGivenNameResource("file.txt");

        FileHelper.createWorkingIfNotExits(path);

        Assertions.assertThat(path)
                .exists()
                .isDirectory();

        FileHelper.deleteWorkIfNotExists(path);
    }

}