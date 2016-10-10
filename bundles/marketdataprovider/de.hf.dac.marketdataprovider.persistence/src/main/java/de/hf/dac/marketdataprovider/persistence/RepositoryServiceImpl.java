/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RepositoryServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 04.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.persistence;

import de.hf.dac.marketdataprovider.api.persistence.RepositoryService;
import de.hf.dac.marketdataprovider.api.persistence.repositories.ProductRepository;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

@OsgiServiceProvider(classes = {RepositoryService.class})
@Singleton
public class RepositoryServiceImpl implements RepositoryService{

    @Override
    public ProductRepository buildProductRepository(EntityManager entityManager) {
        JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
        final Bundle bundle = FrameworkUtil.getBundle(ProductRepository.class);
        //OSGI-Context?
        if(bundle!=null) {
            final ClassLoader classLoader = bundle.adapt(BundleWiring.class).getClassLoader();
            factory.setBeanClassLoader(classLoader);
        }
        return factory.getRepository(ProductRepository.class);
    }
}
