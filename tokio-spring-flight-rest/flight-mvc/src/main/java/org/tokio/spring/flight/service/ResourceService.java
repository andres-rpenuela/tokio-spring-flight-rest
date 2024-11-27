package org.tokio.spring.flight.service;

import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.domain.Resource;
import org.tokio.spring.flight.dto.ResourceDto;

import java.util.Optional;
import java.util.UUID;

public interface ResourceService {

    Optional<ResourceDto> save(MultipartFile multipartFile, String description);
    Optional<ResourceDto> update(Resource resource, MultipartFile multipartFile, String description);

    ResourceDto getResourceContent(UUID resourceId);

    void deleteImage(UUID resourceId);
}
