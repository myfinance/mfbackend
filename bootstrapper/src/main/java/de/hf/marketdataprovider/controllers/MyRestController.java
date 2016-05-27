/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.controllers;

import de.hf.marketdataprovider.service.ProductService;
import de.hf.marketdataprovider.springcommon.config.CommonConfig;
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



/** Rest methods
 *
 * Authorization is handeled via annotations on the service controller. It can not handled here because:
 * "A common problem with using PrePost annotations on controllers is that Spring method security is based on Spring AOP, which is by default implemented with JDK proxies.
 * That means that it works fine on the service layer which is injected in controller layer as interfaces,
 * but it is ignored on controller layer because controller generally do not implement interfaces"
 * and its not working on an RestControllerinterface:
 * "Put them on the class not an interface (basically inheritance of annotations from interfaces to classes isn't supported, spring uses a work around to make it work, interfaces
 * ike this use proxies which in turn lead to issues with the request mapping. You need to force the use of class based proxies instead of interface based proxies to make it work"
 *
 * @author xn01598
 */
@RestController
public class MyRestController {
    private static final Logger log = LoggerFactory.getLogger(MyRestController.class);
    private String name;
    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService =productService;
    }
        
    @Autowired
    public void setName(CommonConfig config) {
        this.name = config.getName();
    }

    @RequestMapping("/")
    String home() {
        
        return "Hello: " + name;
    }


    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("/get/products")
    public List<Product> getProducts() {
        return productService.listProducts();
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @RequestMapping("/get/roles")
    public Collection<String> getRoles(Principal principal) {
        getUserRoles(principal);
        return getUserRoles(principal);
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
