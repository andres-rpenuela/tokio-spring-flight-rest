package org.tokio.spring.flight.api.service;

import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.api.domain.Resource;
import org.tokio.spring.flight.api.dto.ResourceDTO;

import java.util.Optional;
import java.util.UUID;

public interface ManagementResourceService {
    Optional<ResourceDTO> save(MultipartFile multipartFile, String description);
    Optional<ResourceDTO> update(Resource resource, MultipartFile multipartFile, String description);
    ResourceDTO getResourceContent(UUID resourceId);
    void deleteImage(UUID resourceId);
}
