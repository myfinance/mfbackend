/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.config;

import static com.google.common.collect.Lists.newArrayList;
import de.hf.marketdataprovider.security.LdapLoginModule;
import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
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
        http
            .authorizeRequests()
                //.antMatchers("/", "/home").permitAll() ((erlaubte Seiten
                .anyRequest().authenticated() //alle anderen müssen sich erst authentifizieren
                //.anyRequest().hasAnyRole("ADMIN")//alle anderen müssen sich erst authentifizieren
                .and()
            .formLogin() //definiert loginscreen
                //.loginPage("/login") //individuelle Login page sonst standard
                .permitAll(); //jeder ist berechtigt den Login screen aufzurufen
    }
    
    @Configuration
    protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

		/*@Override
		//gegen lokalen ldap -> funktioniert
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth
				.ldapAuthentication()
					.userDnPatterns("uid={0},ou=people")
					.groupSearchBase("ou=apps")
                                        //.userSearchFilter("(uid={0})").userSearchBase("ou=people,dc=springframework,dc=org")
                                        .groupRoleAttribute("cn").groupSearchFilter("uniqueMember={0}")
                                        //.contextSource().url("ldaps://ldap.xxxx.yyy:636/cn=cw-grpreader,ou=people,dc=xxx,dc=xxxx,dc=xxx")
					.contextSource().ldif("classpath:test-server.ldif").root("dc=hf,dc=org");
		}*/
        /*@Override
        //authorisierung -> funzt nicht
        public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth
				.ldapAuthentication()
					.userDnPatterns("(dzuid={0}),ou=People")
					//.groupSearchBase("ou=Rollen,ou=Test,ou=PoET,ou=Eigenentwicklungen,ou=Anwendungen,dc=dzbank,dc=vrnet")
                                        //.userSearchFilter("(uid={0})").userSearchBase("ou=people,dc=springframework,dc=org")
                                        //.groupRoleAttribute("cn").groupSearchFilter("uniqueMember={0}")
                                        //.contextSource().url("ldap://dfvvpldps1.dzbank.vrnet:389/ou=People,dc=dzbank,dc=vrnet")
                                        .contextSource().url("ldap://dfvvpldps1.dzbank.vrnet:389")
                                //ldap://dfvvpldps1.dzbank.vrnet:389/dc=dzbank,dc=vrnet??one?(objectClass=*)
                                            //.managerDn("uid=eigenentwicklungen_lesend,ou=Eigenentwicklungen,ou=Special Users,dc=dzbank,dc=vrnet")
                                            //.managerPassword("eigen310511")
                                            .root("dc=dzbank,dc=vrnet");
		}        */
        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .ldapAuthentication()
                .userSearchFilter("(sAMAccountName={0})")
                .userSearchBase("ou=Accounts,dc=dzag,dc=vrnet")
                .groupSearchFilter("(sAMAccountName={0})")
                .groupSearchBase("ou=FR,ou=Accounts,dc=dzag,dc=vrnet")
                .groupRoleAttribute("memberOf")
                .contextSource().url("ldap://dzag.vrnet:389")
                .root("dc=dzag,dc=vrnet")
                .managerDn("cn=XNMEE01,ou=FR,ou=Accounts,dc=dzag,dc=vrnet")
                .managerPassword("Ready2Work11#");
        }
    }
    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // mit den folgenden 2 zeilen wird die dzbank authorisierung durchgeführt
        // wie die authentifizierung durchgeführt wird ist dabei noch unklar
        // entsprechend wurde auch noch nicht untersucht wie dies mit spring security eingebunden werden kann
        // die einbindung an dieser Stelle ist nur für testzwecke da man hier maximal zum startzeitpunkt alle gültigen user ermiteln kann 
        //-> dauert lange und neue User werden nicht mitbekommen
        LdapLoginModule ldap = new LdapLoginModule();
        ldap.login("xn01598", "xy?8h:9i;0j1atest");
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }  */ 
    
    @Value("${name}")
    private String name;
    
    public String getName(){
        return name;
    }
}
