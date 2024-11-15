package org.tokio.spring.resources.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.resources.core.exception.ResourceException;
import org.tokio.spring.resources.dto.ResourceContentDTO;
import org.tokio.spring.resources.dto.ResourceIdDTO;

import java.util.Optional;
import java.util.UUID;

public interface ResourceService {
    Optional<ResourceIdDTO> saveResource(@NonNull MultipartFile multipartFile, @Nullable String description) throws ResourceException;
    Optional<ResourceContentDTO> findResource(@NonNull UUID resourceId);
    void deleteResource(@NonNull UUID resourceId) throws ResourceException;

}
