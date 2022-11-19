package com.project.doctorhub.auth.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsPolicyConfig implements WebMvcConfigurer {

    private final String baseClientUrl;

    public CorsPolicyConfig(@Value("${client.url}") String baseClientUrl) {
        this.baseClientUrl = baseClientUrl;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(baseClientUrl)
                .allowCredentials(true)
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
