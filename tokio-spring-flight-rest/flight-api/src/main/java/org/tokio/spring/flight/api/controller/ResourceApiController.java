package org.tokio.spring.flight.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tokio.spring.flight.api.dto.ResourceDTO;
import org.tokio.spring.flight.api.service.ManagementResourceService;
import org.tokio.spring.resources.dto.ResourceContentDTO;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/resources")
@Slf4j
@RequiredArgsConstructor
public class ResourceApiController {

    private final ManagementResourceService managementResourceService;

    @GetMapping({"","/"})
    private ResponseEntity<byte[]> getResourceByResourceIdHandler(
            @RequestParam(value = "resource_id") UUID resourceId){
        final ResourceDTO resourceDTO = managementResourceService.getResourceContent(resourceId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resourceDTO.getContentType()))
                .contentLength(resourceDTO.getSize())
                .body(resourceDTO.getContent());
    }

}
