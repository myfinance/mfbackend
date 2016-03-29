/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;



 /**
 *
 * @author xn01598
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"de.hf.marketdataprovider.domain"})
@EnableJpaRepositories(basePackages = {"de.hf.marketdataprovider.repositories"})
@EnableTransactionManagement
@Profile({"h2", "h2persist", "h2dev"})
public class H2Config {
     /**
     * Registriert die Web-Console für die H2 Database
     * @return 
     */
    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}
