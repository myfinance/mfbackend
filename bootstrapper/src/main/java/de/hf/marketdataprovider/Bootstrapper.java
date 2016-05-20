/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author xn01598
 */
@EnableAutoConfiguration
@SpringBootApplication
//verwendet alle mit Configuration annotierten Klassen im Package zur konfiguration des Appcontext. 
//die Controller unter de.hf.marketdataprovider.controllers werden ebenfalls registriert
@ComponentScan("de.hf.marketdataprovider,de.hf.marketdataprovider.persistence.repositories")
@EnableJpaRepositories("de.hf.marketdataprovider.persistence.repositories")
public class Bootstrapper {

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(Bootstrapper.class, args);
    }
    
}
