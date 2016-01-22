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
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @RequestMapping("/getProducts")
    List<Product> getProducts(Principal principal) {
        //getUserRoles(principal);
        List<Product> products = controller.getProducts();
        products.stream().forEach((product) -> {
            System.out.println(product.getDescription());
        });
        return products;
    }  
    
    private Collection<String> getUserRoles(Principal principal) {
        if (principal == null) {
            return Arrays.asList("none");
        } else {
            Set<String> roles = new HashSet<String>();
            final UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                roles.add(grantedAuthority.getAuthority());
            }
            return roles;
        }
    }    
}
