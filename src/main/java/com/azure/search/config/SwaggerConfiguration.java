package com.azure.search.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.search.constant.Constant;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger definition configuration
 */

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage(Constant.Swagger.BASE_PACKAGE))
        .paths(PathSelectors.regex(Constant.Swagger.BASE_API)).build().apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
        Constant.Swagger.APP_INFO_TITLE, Constant.Swagger.API_INFO_DESCRIPTION,
        Constant.Swagger.API_INFO_VERSION, Constant.Swagger.TERMS_OF_SERVICE_URL,
        new Contact(Constant.Swagger.CONTACT_NAME, Constant.Swagger.CONTACT_URL, Constant.Swagger.CONTACT_EMAIL),
        Constant.Swagger.APP_INFO_LICENSE, Constant.Swagger.APP_INFO_LICENSE_URL,
        Collections.emptyList());
  }
}
