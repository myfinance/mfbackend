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

import javax.inject.Inject;
import de.hf.dac.marketdataprovider.api.persistence.RepositoryService;
import de.hf.dac.marketdataprovider.api.persistence.repositories.ProductRepository;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.domain.Product;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author hf
 */
@Slf4j
public class ProductServiceImpl implements ProductService {


    private EntityManagerFactory marketDataEmf;
    @Inject
    public void setMarketDataEmf(EntityManagerFactory marketDataEmf){
        this.marketDataEmf = marketDataEmf;
    }

    @Inject
    private RepositoryService repositoryService;
    @Inject
    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

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
        EntityManager em = null;
        try {

            em = marketDataEmf.createEntityManager();
            em.getTransaction().begin();
            //ProductRepository productRepository = repositoryService.buildProductRepository(em);
            //productRepository.save(product);
            em.persist(product);
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            if(em!=null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }
}
