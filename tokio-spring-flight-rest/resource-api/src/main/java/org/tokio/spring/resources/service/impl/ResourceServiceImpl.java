package org.tokio.spring.resources.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.resources.configuration.ResourceConfigurationProperties;
import org.tokio.spring.resources.core.exception.ResourceException;
import org.tokio.spring.resources.domain.ResourceDescription;
import org.tokio.spring.resources.dto.ResourceContentDTO;
import org.tokio.spring.resources.dto.ResourceIdDTO;
import org.tokio.spring.resources.helper.FileHelper;
import org.tokio.spring.resources.helper.StringHelper;
import org.tokio.spring.resources.service.ResourceService;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    private final ObjectMapper objectMapper;
    private final ResourceConfigurationProperties resourceConfigurationProperties;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<ResourceIdDTO> saveResource(@NonNull MultipartFile multipartFile, String description) throws ResourceException {
        if(multipartFile.isEmpty()){
            log.error("MultipartFile is empty");
            return Optional.empty();
        }

        final ResourceDescription resourceDescription;
        try {
            resourceDescription = ResourceDescription.builder()
                    .resourceName(StringHelper.verifyStringRequired(multipartFile.getOriginalFilename()))
                    .resourceType(StringHelper.verifyStringRequired(multipartFile.getContentType()))
                    .size((int) multipartFile.getSize())
                    .description(description)
                    .build();
        }catch (NullPointerException e){
            log.error("Error to builder ResourceDescription of the MultipartFile {0}",e);
            return Optional.empty();
        }

        final ResourceIdDTO resourceIdDTO = new ResourceIdDTO(UUID.randomUUID());
        final String fileResourceName = "%s".formatted(resourceIdDTO.resourceId());
        final String fileDescriptionName = "%s.json".formatted(resourceIdDTO.resourceId());

        final Path pathResourceContent = resourceConfigurationProperties.getResourcePathFromRelativePathGivenNameResource(fileResourceName);
        final Path pathResourceDescription = resourceConfigurationProperties.getResourcePathFromRelativePathGivenNameResource(fileDescriptionName);
        try{
            Files.write(pathResourceContent,multipartFile.getBytes());
        }catch (IOException e){
            log.error("Error to save Resource Content of the MultipartFile {0}",e);
            return Optional.empty();
        }

        try{
            objectMapper.writeValue(pathResourceDescription.toFile(),resourceDescription);
        }catch (IOException e) {
            log.error("Error to save Resource Description of the MultipartFile {0}",e);
            try {
                FileHelper.deleteWorkIfNotExists(pathResourceContent);
            } catch (IOException ex) {
                log.error("Error to remove Resource Content {0}",ex);
            }
            return Optional.empty();
        }

        return Optional.of(resourceIdDTO);
    }


    @Override
    public Optional<ResourceContentDTO> findResource(@NonNull UUID resourceId) {
        final FilenameFilter filenameFilter = (dir, name) -> name.contains("%s".formatted(resourceId));
        final Optional<File> fileResourceOpt;
        final Optional<File> fileDescriptionResourceOpt;

        final File[] filesByName = resourceConfigurationProperties.buildResourcePathFromRelativePathGivenNameResource()
                .toFile().listFiles(filenameFilter);

        if(Objects.isNull(filesByName)){
            log.debug("No files found");
            return Optional.empty();
        }

        fileResourceOpt = Arrays
                .stream(filesByName)
                .filter(file -> file.getName().equals(resourceId.toString())).findFirst();

        fileDescriptionResourceOpt = Arrays
                .stream(filesByName)
                .filter(file -> file.getName().contains(".json")).findFirst();

        if(fileResourceOpt.isEmpty() || fileDescriptionResourceOpt.isEmpty()){
            log.debug("No files found");
            return Optional.empty();
        }

        try {
            final byte[] content = Files.readAllBytes(fileResourceOpt.get().toPath());

            final ResourceDescription resourceDescription = this.objectMapper
                    .readValue(fileDescriptionResourceOpt.get(), ResourceDescription.class);

            // build the result end, after read resources
            final ResourceContentDTO resourceContentDto = ResourceContentDTO.builder()
                    .contentType(resourceDescription.getResourceType())
                    .description(resourceDescription.getDescription())
                    .resourceName(resourceDescription.getResourceName())
                    .size(resourceDescription.getSize())
                    .content(content)
                    .resourceId(resourceId)
                    .build();

            return Optional.of(resourceContentDto);

        } catch (IOException e) {
            log.error("Error to read resource or description resource, cause {0}",e );
            return Optional.empty();
        }

    }

    @Override
    public void deleteResource(@NonNull UUID resourceId) {
        final FilenameFilter filenameFilter = (dir, name) -> name.contains("%s".formatted(resourceId));
        final File[] filesByName = resourceConfigurationProperties
                .buildResourcePathFromRelativePathGivenNameResource()
                .toFile()
                .listFiles(filenameFilter::accept);

        Arrays.stream(filesByName).forEach(file->{
            try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                log.error("Error in deleteResource, cause: {0}",e);
            }
        });

    }
}
