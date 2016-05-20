/** ----------------------------------------------------------------------------
 *
 * ---                                ---
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

package de.hf.marketdataprovider.springcommon.config.security;

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
            //.antMatchers("/", "/home").permitAll() // permit all to root and home page
            .anyRequest().authenticated() //for other pages is at least authentication necessary
            //.anyRequest().hasAnyRole("ADMIN")//permit only users with Role admin
            //.and().authorizeRequests().antMatchers("/console/**").permitAll()
            .and()
            .formLogin() //define login form
            //.loginPage("/login") //add non standard login page
            .permitAll(); //permit all to access login page

    }
}
