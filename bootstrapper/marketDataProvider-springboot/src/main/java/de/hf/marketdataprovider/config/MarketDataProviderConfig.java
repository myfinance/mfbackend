/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.config;

import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.env.context.ContextBuilder;
import de.hf.dac.io.efmb.EntityManagerFactorySetupImpl;
import de.hf.dac.io.env.ContextBuilderImpl;
import de.hf.dac.marketdataprovider.api.persistence.DaoBuilder;
import de.hf.dac.marketdataprovider.api.persistence.DaoContextBuilder;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.persistence.DaoBuilderImpl;
import de.hf.dac.marketdataprovider.persistence.DaoContextBuilderImpl;
import de.hf.dac.marketdataprovider.service.ProductServiceImpl;
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
public class MarketDataProviderConfig {
   @Bean
   public ProductService productService(){

      DaoBuilder daoBuilder = new SpringDaoBuilderImpl();
      ProductService productService = null;
      try {
         productService = new ProductServiceImpl(daoBuilder.buildProductDao(""));
      } catch(Exception e) {
         throw new RuntimeException(e);
      }

      return productService;
   }

   /*@Bean
   public DaoContextBuilder daoContextBuilder(){
      return new DaoContextBuilderImpl();
   }

   @Bean
   public ContextBuilder ContextBuilder(){
      return new ContextBuilderImpl();
   }

   @Bean
   public EntityManagerFactorySetup entityManagerFactorySetup(){
      return new EntityManagerFactorySetupImpl();
   }*/
            
}
