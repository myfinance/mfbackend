/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider;

import de.hf.dac.io.config.resfile.ResfileConfigurationImpl;
import de.hf.dac.io.efmb.EntityManagerFactorySetupImpl;
import de.hf.dac.io.efmb.impl.EntityManagerFactoryBuilderImpl;
import de.hf.dac.io.env.EnvironmentServiceImpl;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.persistence.ProductDaoImpl;
import de.hf.marketdataprovider.config.CommonConfig;
import de.hf.marketdataprovider.controllers.MyRestController;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author xn01598
 */
@EnableAutoConfiguration
@SpringBootApplication
//verwendet alle mit Configuration annotierten Klassen im Package zur konfiguration des Appcontext. 
//die Controller unter de.hf.marketdataprovider.controllers werden ebenfalls registriert
@EntityScan("de.hf.dac.marketdataprovider.api.domain")
@ComponentScan(basePackageClasses = {CommonConfig.class, Product.class, MyRestController.class })
//@EnableJpaRepositories("de.hf.dac.marketdataprovider.api.persistence.repositories")
public class Bootstrapper {

    /*
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

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(Bootstrapper.class, args);
    }
    
}
