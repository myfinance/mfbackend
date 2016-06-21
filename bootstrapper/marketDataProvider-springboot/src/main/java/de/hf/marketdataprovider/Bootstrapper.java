/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider;

import de.hf.marketdataprovider.domain.EndOfDayPrice;
import de.hf.marketdataprovider.domain.Instrument;
import de.hf.marketdataprovider.service.InstrumentService;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
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
@ComponentScan("de.hf.marketdataprovider,de.hf.marketdataprovider.persistence.repositories")
@EnableJpaRepositories("de.hf.marketdataprovider.persistence.repositories")
public class Bootstrapper {

    /**
     * for initialization
     * @param instrumentService
     * @return
     */
    @Bean
    CommandLineRunner init(InstrumentService instrumentService) {
        return (evt) -> Arrays.asList(
            "PP0123456789,PP0000000001".split(","))
            .forEach(
                a -> {
                    Instrument instrument = instrumentService.saveInstrument(new Instrument(1,1,a));
                    instrumentService.savePrice(new EndOfDayPrice(instrument, 100, LocalDate.now()));
                    instrumentService.savePrice(new EndOfDayPrice(instrument, 100, LocalDate.now().minusDays(1)));
                });
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(Bootstrapper.class, args);
    }
    
}
