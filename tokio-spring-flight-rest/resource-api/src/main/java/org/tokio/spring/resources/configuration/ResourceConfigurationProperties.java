package org.tokio.spring.resources.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.tokio.spring.resources.helper.FileHelper;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "path.resource.content")
public record ResourceConfigurationProperties(Path absolutePath,String relativePath) {

    /**
     * Return the path of resources from relative path read in the properties
     * @return path of resource
     *
     * @see {@link FileHelper#getCurrentWorking(String)}
     */
    public Path buildResourcePathFromRelativePathGivenNameResource(){
        return FileHelper.getCurrentWorking(relativePath);
    }

    /**
     * Return the path of resources from relative path absolute in the properties
     * @return path of resource
     */
    public Path buildResourcePathFromAbsolutePath(){
        return absolutePath;
    }

    /**
     * Return the path of resource given from relative path read in the properties
     * @return path of resource given
     *
     * @param nameResource name of resource for get
     * @see {@link FileHelper#getCurrentWorking(String)}
     *
     * Other option:
     *  return Path.of(getResourcePathFromRelativePathGivenNameResource().toString(),nameResource);
     */
    public Path getResourcePathFromRelativePathGivenNameResource(@NonNull String nameResource){
        return buildResourcePathFromRelativePathGivenNameResource().resolve(nameResource);

    }

}
