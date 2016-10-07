/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : ProductServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.service;

import com.google.inject.Inject;
import de.hf.dac.marketdataprovider.api.persistence.RepositoryService;
import de.hf.dac.marketdataprovider.api.persistence.repositories.ProductRepository;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.domain.Product;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.osgi.framework.BundleContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author surak
 */
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Inject
    protected BundleContext bundleContext;

    @Inject
    EntityManagerFactory marketDataEmf;

    @Inject
    protected RepositoryService repositoryService;

    @Override
    public List<Product> listProducts() {
        List products = null;
        try {

            EntityManager entityManager = marketDataEmf.createEntityManager();
            //TypedQuery<Product> query = entityManager.createNamedQuery(Product.findAll, Product.class);
            //products=query.getResultList();
            ProductRepository productRepository = repositoryService.buildProductRepository(entityManager);
            products = productRepository.findAll();

            //List savedObjects = productRepository.save(products);
            //Product fetchedProduct = productRepository.findOne(p1.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public void saveProduct(Product product) {
        UserTransaction utx = null;
        try {

            EntityManager entityManager = marketDataEmf.createEntityManager();
            //utx = (UserTransaction) entityManager.getTransaction();
            utx = (UserTransaction) bundleContext.getService(bundleContext.getServiceReference(UserTransaction.class.getName()));
            utx.begin();

            //ProductRepository productRepository = repositoryService.buildProductRepository(entityManager);
            //productRepository.save(product);
            entityManager.persist(product);


            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(utx!=null) {
                try {
                    utx.rollback();
                } catch (SystemException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
