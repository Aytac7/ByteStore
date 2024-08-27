package com.example.startapp.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

public class MultipartConfigElement {
    @Bean
    public jakarta.servlet.MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("1MB"));
        factory.setMaxRequestSize(DataSize.parse("10MB"));
        return factory.createMultipartConfig();

}}
