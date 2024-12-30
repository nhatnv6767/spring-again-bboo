package com.ra.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocalResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
    
}
