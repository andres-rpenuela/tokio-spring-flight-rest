package org.tokio.spring.flight.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.flight.domain.Resource;
import org.tokio.spring.flight.dto.ResourceDto;
import org.tokio.spring.flight.repository.ResourceDao;
import org.tokio.spring.flight.service.ResourceService;
import org.tokio.spring.resources.dto.ResourceContentDTO;
import org.tokio.spring.resources.dto.ResourceIdDTO;

import java.util.Optional;
import java.util.UUID;

@Service("resource-service-mvc")
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final org.tokio.spring.resources.service.ResourceService storeService;

    private final ModelMapper modelMapper;
    private final ResourceDao resourceDao;

    @Override
    @Transactional
    public Optional<ResourceDto> save(MultipartFile multipartFile, String description) {

        return populationCreateOrUpdate(new Resource(), multipartFile, description);
    }

    @Override
    @Transactional
    public Optional<ResourceDto> update(Resource resource, MultipartFile multipartFile, String description) {
        final UUID resourceIdOld = resource.getResourceId();
         Optional<ResourceDto> resourceDtoOpt = populationCreateOrUpdate(resource, multipartFile, description);

        if(resourceDtoOpt.isPresent()) {
            storeService.deleteResource(resourceIdOld);
        }
        return resourceDtoOpt;
    }

    protected Optional<ResourceDto> populationCreateOrUpdate(@NonNull Resource resource, MultipartFile multipartFile, String description){
        final Optional<ResourceIdDTO> resourceIdDtoOpt = storeService.saveResource(multipartFile, description);

        if(resourceIdDtoOpt.isEmpty())
            return Optional.empty();

        ResourceContentDTO resourceContentDto = storeService.findResource(resourceIdDtoOpt.get().resourceId())
                .orElseThrow(()->new IllegalArgumentException("%s don't find in local, before created!"
                        .formatted(resourceIdDtoOpt.get().resourceId())));

        resource = buildResourceFromResourceContent(resourceContentDto,resourceIdDtoOpt.get().resourceId(),resource.getId());

        return Optional.of(modelMapper.map(resourceDao.save(resource), ResourceDto.class));
    }
    @Override
    public ResourceDto getResourceContent(UUID resourceId) {
        return storeService.findResource(resourceId).map(ResourceServiceImpl::mapperResourceContentToResourceDto)
                .orElseThrow(()-> new IllegalArgumentException("%s not found!".formatted(resourceId)));
    }

    @Override
    @Transactional
    public void deleteImage(UUID resourceId) {

        resourceDao.findByResourceId(resourceId)
                  .ifPresent(element -> {
                      resourceDao.delete(element);
                      storeService.deleteResource(resourceId);
                  });

    }

    private static ResourceDto mapperResourceContentToResourceDto(ResourceContentDTO resourceContentDto) {
        return ResourceDto.builder()
                .resourceId(resourceContentDto.resourceId())
                .filename(resourceContentDto.resourceName())
                .size(resourceContentDto.size())
                .contentType(resourceContentDto.contentType())
                .content(resourceContentDto.content())
                .build();
    }

    private static Resource buildResourceFromResourceContent(@NonNull ResourceContentDTO resourceContentDto,@NonNull UUID resourceId, @Nullable Long id) {
        return Resource.builder()
                .id(id)
                .fileName(resourceContentDto.resourceName())
                .resourceId(resourceId)
                .size(resourceContentDto.size())
                .contentType(resourceContentDto.contentType())
                .build();
    }
}
