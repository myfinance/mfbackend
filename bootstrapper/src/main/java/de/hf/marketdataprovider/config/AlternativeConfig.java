/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.config;

import de.hf.marketdataprovider.service.ProductService;
import de.hf.marketdataprovider.service.ProductServiceAlternativeImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author surak
 */
@Configuration
@Profile("test")
public class AlternativeConfig  {
            
   @Bean 
   public ProductService productService(){
      return new ProductServiceAlternativeImpl();
   }
            
}
