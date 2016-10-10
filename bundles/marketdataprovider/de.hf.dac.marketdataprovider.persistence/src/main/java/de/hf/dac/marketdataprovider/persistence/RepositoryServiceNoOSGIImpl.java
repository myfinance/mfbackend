/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RepositoryServiceNoOSGIImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.persistence;

import de.hf.dac.marketdataprovider.api.persistence.RepositoryService;
import de.hf.dac.marketdataprovider.api.persistence.repositories.ProductRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;

public class RepositoryServiceNoOSGIImpl  implements RepositoryService {

    @Override
    public ProductRepository buildProductRepository(EntityManager entityManager) {
        JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
        //factory.setBeanClassLoader(ProductRepository.class.getClassLoader());
        return factory.getRepository(ProductRepository.class);
    }
}
