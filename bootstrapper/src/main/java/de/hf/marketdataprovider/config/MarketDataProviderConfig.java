/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.config;

import de.hf.marketdataprovider.services.ProductService;
import de.hf.marketdataprovider.services.ProductServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author surak
 */
@Configuration
@Profile("production")
public class MarketDataProviderConfig {
   @Bean 
   public ProductService productService(){
      return new ProductServiceImpl();
   }
            
}
