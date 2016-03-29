/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : CommonSecurityConfig.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 29.03.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Profile({"!nosecurity"})
public class CommonSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            //.antMatchers("/", "/home").permitAll() //erlaubte Seiten
            .anyRequest().authenticated() //alle anderen müssen sich erst authentifizieren
            //.anyRequest().hasAnyRole("ADMIN")//alle anderen müssen sich erst authentifizieren
            .and()
            .formLogin() //definiert loginscreen
            //.loginPage("/login") //individuelle Login page sonst standard
            .permitAll(); //jeder ist berechtigt den Login screen aufzurufen

    }
}
