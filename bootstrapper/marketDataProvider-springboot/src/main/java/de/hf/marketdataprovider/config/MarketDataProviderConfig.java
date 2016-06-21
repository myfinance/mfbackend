/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.config;

import de.hf.marketdataprovider.service.ProductService;
import de.hf.marketdataprovider.service.ProductServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author surak
 */
@Configuration
@Profile("production")
@ImportResource("classpath:META-INF/beans.xml")
public class MarketDataProviderConfig {
   @Bean 
   public ProductService productService(){
      return new ProductServiceImpl();
   }
            
}
