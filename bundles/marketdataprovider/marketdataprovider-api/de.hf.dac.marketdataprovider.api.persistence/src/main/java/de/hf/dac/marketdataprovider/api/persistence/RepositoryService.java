/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RepositoryService.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 04.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.persistence;

import de.hf.dac.marketdataprovider.api.persistence.repositories.ProductRepository;
import javax.persistence.EntityManager;

public interface RepositoryService {
    ProductRepository buildProductRepository(EntityManager entityManager);
}
