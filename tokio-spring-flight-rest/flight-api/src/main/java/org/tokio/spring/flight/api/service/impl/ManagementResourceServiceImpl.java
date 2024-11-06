package org.tokio.spring.flight.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.api.domain.Resource;
import org.tokio.spring.flight.api.dto.ResourceDTO;
import org.tokio.spring.flight.api.service.ManagementResourceService;

import java.util.Optional;
import java.util.UUID;

@Service
public class ManagementResourceServiceImpl implements ManagementResourceService {

    @Override
    public Optional<ResourceDTO> save(MultipartFile multipartFile, String description) {
        // TODO
        return Optional.empty();
    }

    @Override
    public Optional<ResourceDTO> update(Resource resource, MultipartFile multipartFile, String description) {
        // TODO
        return Optional.empty();
    }

    @Override
    public ResourceDTO getResourceContent(UUID resourceId) {
        // TODO
        return null;
    }

    @Override
    public void deleteImage(UUID resourceId) {
        // TODO
    }
}
