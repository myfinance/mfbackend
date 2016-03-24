/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.config;

import static com.google.common.collect.Lists.newArrayList;
import java.time.LocalDate;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class CommonConfig extends WebSecurityConfigurerAdapter {
    
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
                        newArrayList(new ResponseMessageBuilder()
                            .code(500)
                            .message("500 message")
                            .responseModel(new ModelRef("Error"))
                            .build()))
                    //.securitySchemes(newArrayList(apiKey()))
                    //.securityContexts(newArrayList(securityContext()))
                    //.enableUrlTemplating(true)
                    ;
    }  
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if(isScurityEnabled()) {
            http
                .authorizeRequests()
                //.antMatchers("/", "/home").permitAll() ((erlaubte Seiten
                //.anyRequest().authenticated() //alle anderen müssen sich erst authentifizieren
                .anyRequest().hasAnyRole("ADMIN")//alle anderen müssen sich erst authentifizieren
                .and()
                .formLogin() //definiert loginscreen
                //.loginPage("/login") //individuelle Login page sonst standard
                .permitAll(); //jeder ist berechtigt den Login screen aufzurufen
        }
    }

    @Value("${name}")
    private String name;
    
    public String getName(){
        return name;
    }

    @Value("${securityEnabled}")
    private Boolean securityEnabled;

    public Boolean isScurityEnabled(){
        return securityEnabled;
    }

}
