/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.springcommon.config;


import java.time.LocalDate;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author xn01598
 * unabhÃƒÂ¤ngig vom Profil immer gÃƒÂ¼ltig
 */
@Configuration
//to Enable Swagger: http://localhost:8080/swagger-ui.html
@EnableSwagger2
@PropertySource("classpath:application.properties")
public class CommonConfig {
    
    //Swagger Config
    @Bean
    public Docket petApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                        .apis(RequestHandlerSelectors.any())
                        .paths(PathSelectors.any())
                        .build()
                    .pathMapping("/")
                    .directModelSubstitute(LocalDate.class,String.class)
                    .genericModelSubstitutes(ResponseEntity.class)
                    .useDefaultResponseMessages(false)
                    .globalResponseMessage(RequestMethod.GET,
                        Lists.newArrayList(new ResponseMessageBuilder()
                            .code(500)
                            .message("500 message")
                            .responseModel(new ModelRef("Error"))
                            .build()))
                    //.securitySchemes(newArrayList(apiKey()))
                    //.securityContexts(newArrayList(securityContext()))
                    //.enableUrlTemplating(true)
                    ;
    }  

    @Value("${name}")
    private String name;
    
    public String getName(){
        return name;
    }
}
