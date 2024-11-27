package org.tokio.spring.flight.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tokio.spring.flight.dto.ResourceDto;
import org.tokio.spring.flight.service.ResourceService;

import java.util.UUID;

@Controller
@RequestMapping("/flight/resources")
@RequiredArgsConstructor
public class ResourceMvcController {

    @Qualifier("resource-service-mvc")
    private final ResourceService resourceService;


    @GetMapping({"","/"})
    public ResponseEntity<byte[]> getResourceContentHandler(@RequestParam(value = "rsc") UUID resourceId){
        final ResourceDto resourceDto = resourceService.getResourceContent(resourceId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resourceDto.getContentType()))
                .contentLength(resourceDto.getSize())
                .body(resourceDto.getContent());
    }

    // handlker para borrar
    @GetMapping("/downloads")
    public ResponseEntity<byte[]> downloadResourceContentHandler(@RequestParam(value = "rsc") UUID resourceId){
        final ResourceDto resourceDto = resourceService.getResourceContent(resourceId);

        // wrapper para devoler objetos que Spring Serializa y los envía
        final HttpHeaders httpHeaders = new HttpHeaders();
        // fuerza la descarga del cotenido y le ponemos el nombre del archivo
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename= "+resourceDto.getFilename());
        // infromación del documenot en la cabecera (puede ponerse en el cuerpo)
        httpHeaders.add(HttpHeaders.CONTENT_LENGTH,String.valueOf(resourceDto.getSize()));
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,resourceDto.getContentType());

        return  ResponseEntity.ok()
                .headers(httpHeaders)
                .body(resourceDto.getContent());
    }
}
