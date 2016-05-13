/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.controllers;

import de.hf.marketdataprovider.config.CommonConfig;
import de.hf.marketdataprovider.domain.Product;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 *
 * @author xn01598
 */
@RestController 
public class MyRestController {
    private static final Logger log = LoggerFactory.getLogger(MyRestController.class);
    private MyController controller;
    private String name;
        
    @Autowired
    public void setName(CommonConfig config) {
        this.name = config.getName();
    }
    
    @Autowired
    public void setController(MyController controller) {
        this.controller = controller;
    }
    
    @RequestMapping("/")
    String home() {
        
        return "Hello: " + name;
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("/getProducts")
    List<Product> getProducts(Principal principal) {
        getUserRoles(principal);
        List<Product> products = controller.getProducts();
        products.stream().forEach((product) -> log.info(product.getDescription()));
        return products;
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("/getFirstProduct")
    Product getFirstProduct(Principal principal) {
        getUserRoles(principal);
        List<Product> products = controller.getProducts();
        log.info("first:"+products.get(0).getDescription());
        return products.get(0);
    }

    private Collection<String> getUserRoles(Principal principal) {
        if (principal == null) {
            return Arrays.asList("none");
        } else {
            Set<String> roles = new HashSet<String>();
            final UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
            roles.addAll(authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
            return roles;
        }
    }    
}
