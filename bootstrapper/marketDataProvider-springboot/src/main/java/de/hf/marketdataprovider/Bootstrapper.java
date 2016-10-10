/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider;

import de.hf.dac.marketdataprovider.api.domain.EndOfDayPrice;
import de.hf.dac.marketdataprovider.api.domain.Instrument;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.persistence.RepositoryServiceImpl;
import de.hf.dac.marketdataprovider.persistence.RepositoryServiceNoOSGIImpl;
import de.hf.dac.marketdataprovider.service.InstrumentService;
import de.hf.marketdataprovider.config.CommonConfig;
import de.hf.marketdataprovider.controllers.MyRestController;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.util.Arrays;

/**
 *
 * @author xn01598
 */
@EnableAutoConfiguration
@SpringBootApplication
//verwendet alle mit Configuration annotierten Klassen im Package zur konfiguration des Appcontext. 
//die Controller unter de.hf.marketdataprovider.controllers werden ebenfalls registriert
//@ComponentScan({"de.hf.dac.marketdataprovider.persistence", "de.hf.marketdataprovider.config"})
@ComponentScan(basePackageClasses = {CommonConfig.class, Product.class, RepositoryServiceNoOSGIImpl.class, MyRestController.class})
@EntityScan("de.hf.dac.marketdataprovider.api.domain")
//@EnableJpaRepositories("de.hf.dac.marketdataprovider.api.persistence.repositories")
public class Bootstrapper {

    /**
     * for initialization
     * @param instrumentService
     * @return
     */
    /*@Bean
    CommandLineRunner init(InstrumentService instrumentService) {
        return (evt) -> Arrays.asList(
            "PP0123456789,PP0000000001".split(","))
            .forEach(
                a -> {
                    Instrument instrument = instrumentService.saveInstrument(new Instrument(1,1,a));
                    instrumentService.savePrice(new EndOfDayPrice(instrument, 100, LocalDate.now()));
                    instrumentService.savePrice(new EndOfDayPrice(instrument, 100, LocalDate.now().minusDays(1)));
                });
    }*/

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(Bootstrapper.class, args);
    }
    
}
