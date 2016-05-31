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
 *  Author(s)   : xn01598
 *
 *  Created     : 27.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.persistence.repositories;

import de.hf.marketdataprovider.domain.EndOfDayPrice;
import org.springframework.data.repository.CrudRepository;
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
public interface EndOfDayPriceRepository extends CrudRepository<EndOfDayPrice, Integer> {
    List<EndOfDayPrice> findByInstrumentIsin (String isin);
}
