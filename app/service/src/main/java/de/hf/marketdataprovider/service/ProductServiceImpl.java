/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : ProductServiceImpl.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 20.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.service;

import de.hf.common.util.jpa.DatabaseInfo;
import de.hf.common.util.jpa.EntityManagerFactorySetup;
import de.hf.marketdataprovider.domain.Instrument;
import de.hf.marketdataprovider.domain.Product;
import de.hf.marketdataprovider.persistence.repositories.ProductRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import javax.transaction.Transactional;

import org.osgi.framework.Bundle;

/**
 *
 * @author surak
 */
@Slf4j
@Component(service = ProductService.class)
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;


    @Inject
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    EntityManagerFactorySetup efs;
    @Reference(cardinality = ReferenceCardinality.MANDATORY,
        policy = ReferencePolicy.STATIC)
    protected void setEntityManagerFactorySetup(EntityManagerFactorySetup efs) {
        this.efs = efs;
    }

    @Reference(target = "(osgi.unit.name=marketdatapostgres)")
    JpaTemplate jpa;


    /*DataSource dataSource;
    @Reference(target = "(dataSourceName=marketdatapostgres)")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }*/

    @Override
    public List<Product> listProducts() {
        Product p1 = new Product("1234", "Product 1 desc");
        Product p2 = new Product("1235", "Product 2 desc");
        ArrayList<Product> products = new ArrayList<>(2);
        products.add(p1);
        products.add(p2);

        Properties properties = new Properties();

        DatabaseInfo db = new DatabaseInfo("jdbc:postgresql://localhost:5432/marketdata", "postgres", "vulkan", "org.postgresql.Driver");

        Class<?>[] entities = new Class<?>[]{Product.class, Instrument.class};
        EntityManager em = null;
        EntityManagerFactory sdbFactory=null;
        try {
            sdbFactory = efs.buildEntityManagerFactory("ServiceConfiguration", entities, db,null );
            em = sdbFactory.createEntityManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        TypedQuery<Product> query = em.createNamedQuery(Product.findAll, Product.class);
        List oldproducts=query.getResultList();


        jpa.tx(TransactionType.Required,   ema -> {
            ema.persist(p1);
            ema.flush();

            JpaRepositoryFactory factory = new JpaRepositoryFactory(ema);
            final Bundle bundle = FrameworkUtil.getBundle(ProductRepository.class);
            final ClassLoader classLoader = bundle.adapt(BundleWiring.class).getClassLoader();
            factory.setBeanClassLoader(classLoader);

            productRepository = factory.getRepository(ProductRepository.class);
            List oldproductsRepo=productRepository.findAll();

            List savedObjects = productRepository.save(products);
        });

        //fetch from DB
        //Product fetchedProduct = productRepository.findOne(p1.getId());
        //log.debug("test:"+fetchedProduct.getId());

        return products;
    }
}
