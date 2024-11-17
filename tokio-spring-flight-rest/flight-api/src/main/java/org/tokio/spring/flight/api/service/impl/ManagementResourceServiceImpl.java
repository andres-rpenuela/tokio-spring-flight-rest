package org.tokio.spring.flight.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.api.domain.Resource;
import org.tokio.spring.flight.api.dto.ResourceDTO;
import org.tokio.spring.flight.api.report.ResourceReport;
import org.tokio.spring.flight.api.service.ManagementResourceService;
import org.tokio.spring.resources.core.exception.ResourceException;
import org.tokio.spring.resources.dto.ResourceContentDTO;
import org.tokio.spring.resources.dto.ResourceIdDTO;
import org.tokio.spring.resources.service.ResourceService;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagementResourceServiceImpl implements ManagementResourceService {

    private final ResourceReport resourceReport;
    private final ResourceService resourceService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Optional<ResourceDTO> save(MultipartFile multipartFile, String description) {

        Optional<ResourceIdDTO> resourceIdDTOOptional = resourceService.saveResource(multipartFile,description);
        Optional<Resource> resourceOptional = resourceIdDTOOptional.map(resourceIdDTO -> Resource.builder()
                .size( (int)multipartFile.getSize() )
                .contentType(multipartFile.getContentType())
                .resourceId(resourceIdDTO.resourceId())
                .fileName(multipartFile.getOriginalFilename())
                .build());


        if(resourceOptional.isPresent()){
            Resource resource = resourceReport.save(resourceOptional.get());
            return Optional.of(modelMapper.map(resource,ResourceDTO.class));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ResourceDTO> update(Resource resource, MultipartFile multipartFile, String description) {
        // TODO
        return Optional.empty();
    }

    @Override
    public ResourceDTO getResourceContent(UUID resourceId) throws ResourceException {
        return resourceService.findResource(resourceId)
                .map(ManagementResourceServiceImpl::mapperResourceContentToResourceDto)
                .orElseThrow(()->new ResourceException("Resource don't find!"));
    }

    @Override
    @Transactional
    public void deleteImage(UUID resourceId) {
        if(Objects.isNull( resourceId) ){
            return;
        }
        resourceService.deleteResource(resourceId);
        resourceReport.findByResourceId(resourceId)
                .ifPresent(resourceReport::delete);

    }

    private static ResourceDTO mapperResourceContentToResourceDto(ResourceContentDTO resourceContentDto) {
        return ResourceDTO.builder()
                .resourceId(resourceContentDto.resourceId())
                .filename(resourceContentDto.resourceName())
                .size(resourceContentDto.size())
                .contentType(resourceContentDto.contentType())
                .content(resourceContentDto.content())
                .build();
    }
}
