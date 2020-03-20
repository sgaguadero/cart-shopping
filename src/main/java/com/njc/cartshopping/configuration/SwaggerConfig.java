package com.njc.cartshopping.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("com.njc.cartshopping"))
          .paths(PathSelectors.any())
          .build()
          .apiInfo(metadata());
    }

    /**
     * Adds metadata to Swagger
     *
     * @return
     */
    private ApiInfo metadata() {
      return new ApiInfoBuilder()
              .title("Basic Products and Orders API")
              .description("An API to support basic CRUD with Spring Boot for Products and Orders")
              .build();
  }
}
