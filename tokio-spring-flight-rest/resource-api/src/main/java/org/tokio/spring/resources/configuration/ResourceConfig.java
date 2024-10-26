package org.tokio.spring.resources.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value=ResourceConfigurationProperties.class)
public class ResourceConfig {
}
