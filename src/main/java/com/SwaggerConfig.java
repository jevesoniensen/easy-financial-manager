package com;

import com.google.common.base.Predicates;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;

import static springfox.documentation.builders.PathSelectors.any;

@EnableAutoConfiguration
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket consumerApi() {
        ApiInfo info = new ApiInfoBuilder()
                .title("ERP - Transformation")
                .version("1.0")
                .build();

        HashSet<String> mediaTypes = new HashSet<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_VALUE);

        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
                .produces(mediaTypes)
                .consumes(mediaTypes)
                .useDefaultResponseMessages(false)
                .groupName("EMF")
                .apiInfo(info)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.efm.controller"))
                .paths(any())
                .build();
    }

    @Bean
    public Docket actuator() {

        HashSet<String> mediaTypes = new HashSet<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_VALUE);

        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
                .produces(mediaTypes)
                .consumes(mediaTypes)
                .useDefaultResponseMessages(false)
                .groupName("Tools - Actuator")
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.springframework.boot.actuate"))
                .paths(Predicates.not(PathSelectors.regex("(.*).json")))
                .build();
    }
}
