/** ----------------------------------------------------------------------------
 *
 * ---                                ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : EndOfDayPriceRepository.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.persistence.repositories;

import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * The Spring Data JPA CRUD Repository is a feature of Spring Data JPA.
 * Similar to coding with a Spring Integration Gateway, you can just define an interface.
 * Spring Data JPA uses generics and reflection to generate the concrete implementation of the interface we define.
 * Defining a repository for our EndOfDayPrice domain class is as simple as defining a interface and extending the CrudRepository interface.
 * You need to declare two classes in the generics for this interface.
 * They are used for the domain class the repository is supporting, and the type of the id declared of the domain class.
 * @author xn01598
 */
public interface EndOfDayPriceRepository extends JpaRepository<EndOfDayPrice, Integer> {
    List<EndOfDayPrice> findByInstrumentIsin (String isin);
}
