package org.store.clothstar.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/favicon.ico")
            .addResourceLocations("classpath:/static/")
        registry.addResourceHandler("/js/**")
            .addResourceLocations("classpath:/static/js/")
        registry.addResourceHandler("/css/**")
            .addResourceLocations("classpath:/static/css/")
        registry.addResourceHandler("/images/**")
            .addResourceLocations("classpath:/static/images/")
    }
}