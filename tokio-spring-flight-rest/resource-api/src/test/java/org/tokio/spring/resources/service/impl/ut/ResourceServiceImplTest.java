package org.tokio.spring.resources.service.impl.ut;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.tokio.spring.resources.configuration.ResourceConfigurationProperties;
import org.tokio.spring.resources.dto.ResourceContentDTO;
import org.tokio.spring.resources.dto.ResourceIdDTO;
import org.tokio.spring.resources.service.ResourceService;
import org.tokio.spring.resources.service.impl.ResourceServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@ActiveProfiles("test")
class ResourceServiceImplTest {

    private ResourceService resourceService;

    @TempDir
    public Path temporalPath;

    // CONSTANTS
    public static final String EXT_TXT = ".txt";
    private static final String FILE_NAME = "file%s".formatted(EXT_TXT);
    private static final String CONTENT = "HOLA";
    private static final String CONTENT_TYPE = MediaType.TEXT_PLAIN_VALUE;

    @BeforeEach
    void beforeEach() throws Exception {
        // add dependencies to service
        final ResourceConfigurationProperties resourceConfigProperty =
                new ResourceConfigurationProperties(temporalPath, temporalPath.toAbsolutePath().toString());

        final ObjectMapper objectMapper = new ObjectMapper();
        resourceService = new ResourceServiceImpl(objectMapper,resourceConfigProperty);
    }

    @Test
    void givenResource_whenSaveResource_thenReturnOk() {
        final MockMultipartFile mockMultipartFile = getMockMultipartFile();

        Optional<ResourceIdDTO> optionalResourceIdDto = resourceService.saveResource(mockMultipartFile,"description");

        final ResourceConfigurationProperties resourceConfigProperty =
                new ResourceConfigurationProperties(temporalPath, temporalPath.toAbsolutePath().toString());

        Assertions.assertThat(optionalResourceIdDto)
                .isNotNull()
                .isNotEmpty()
                .matches(resourceIdDto -> Objects.nonNull(resourceIdDto.get().resourceId()))
                .matches(resourceIdDto ->
                        Files.exists(resourceConfigProperty
                                .getResourcePathFromRelativePathGivenNameResource(
                                        resourceIdDto.get().resourceId().toString())
                        )
                ).matches(resourceIdDto ->
                        Files.exists(resourceConfigProperty
                                .getResourcePathFromRelativePathGivenNameResource("%s.json"
                                        .formatted(resourceIdDto.get().resourceId().toString())
                                )
                        )
                );

    }

    @Test
    void givenResource_whenFindResource_thenReturnOk() {
        final ResourceIdDTO resourceIdDto = resourceService
                .saveResource(
                        getMockMultipartFile(),
                        "description").get();

        final Optional<ResourceContentDTO> optionalResourceContentDTO = resourceService
                .findResource(resourceIdDto.resourceId());

        Assertions.assertThat(optionalResourceContentDTO)
                .isPresent()
                .isNotEmpty()
                .get()
                .returns(resourceIdDto.resourceId(),ResourceContentDTO::resourceId)
                .returns(CONTENT.getBytes(),ResourceContentDTO::content)
                .returns(FILE_NAME,ResourceContentDTO::resourceName)
                .returns(CONTENT_TYPE,ResourceContentDTO::contentType);
    }

    @Test
    void givenResource_whenDeleteResource_thenReturnOk() {
        final ResourceIdDTO resourceIdDto = resourceService
                .saveResource(getMockMultipartFile(), "description").get();

        resourceService.deleteResource(resourceIdDto.resourceId());

        // verify
        final Optional<ResourceContentDTO> optionalResourceContentDTO = resourceService
                .findResource(resourceIdDto.resourceId());

        Assertions.assertThat(optionalResourceContentDTO)
                .isEmpty();
    }

    private static MockMultipartFile getMockMultipartFile() {

        return new MockMultipartFile(
                FILE_NAME.replace(EXT_TXT, StringUtils.EMPTY),
                FILE_NAME,
                CONTENT_TYPE,
                CONTENT.getBytes());

    }

}