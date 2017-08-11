/** ----------------------------------------------------------------------------
 *
 * ---          Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : EntityManagerSetupIT.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 14.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketDataProvider.paxexam.io.emfb;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.TransactionManager;

import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.api.io.efmb.tx.WrappedEntityManagerFactory;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.marketdataprovider.api.application.EnvTarget;
import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import de.hf.dac.marketdataprovider.persistence.ProductDaoImpl;
import de.hf.marketDataProvider.paxexam.support.PAXExamTestSetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.osgi.framework.Bundle;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(PaxExam.class)
public class EntityManagerSetupIT extends PAXExamTestSetup {

    @Inject
    EnvironmentService envService;

    @Inject
    EntityManagerFactorySetup emfb;

    @Inject
    TransactionManager jtaManager;


    @Configuration
    public Option[] config() {
        return new Option[] {
            super.configDefaults(), persistenceFeatures()
        };
    }


    @Test
    public void testWriteDatabase() throws Exception{

        assertNotNull(bundleContext);

        for(Bundle b : bundleContext.getBundles()){
            System.out.println("bundle:"+b.toString());
        }

        Assert.assertNotNull(envService);
        Assert.assertNotNull(emfb);
        Assert.assertNotNull(jtaManager);

        DatabaseInfo dbi = inMemoryH2DatabaseInfo();

        EntityManagerFactory emf = new WrappedEntityManagerFactory(jtaManager,
            emfb.getOrCreateEntityManagerFactory(EnvTarget.MDB,
                EntityManagerFactorySetup.PoolSize.SMALL,
                new Class[] {},
                new ClassLoader[] { Product.class.getClassLoader() },
                dbi));
        assertNotNull(emf);
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        Product p1 = new Product("1234", "Product 1 desc");
        Product p2 = new Product("1235", "Product 2 desc");
        ArrayList<Product> products = new ArrayList<>(2);

        products.add(p1);
        products.add(p2);

        entityManager.persist(p1);
        entityManager.persist(p2);

        entityManager.getTransaction().commit();

        List<Product> result = new ArrayList<>();
        Query query = entityManager.createQuery("select a FROM Product a");
        List<Object> queryResult = (List<Object>) query.getResultList();
        for (Object object : queryResult) {
            result.add((Product)object);
        }

        Assert.assertEquals(2, result.size());

    }

    @Test
    public void testDaoTest() throws Exception {
        assertNotNull(emfb);
        assertNotNull(envService);
        assertNotNull(jtaManager);

        DatabaseInfo dbi = inMemoryH2DatabaseInfo();
        EntityManagerFactory emf = new WrappedEntityManagerFactory(jtaManager,
            emfb.getOrCreateEntityManagerFactory(EnvTarget.MDB,
                EntityManagerFactorySetup.PoolSize.SMALL,
                new Class[] {},
                new ClassLoader[] { Product.class.getClassLoader() },
                dbi));
        assertNotNull(emf);

        Product p1 = new Product("1234", "Product 1 desc");
        Product p2 = new Product("1235", "Product 2 desc");
        ArrayList<Product> products = new ArrayList<>(2);

        products.add(p1);
        products.add(p2);

        ProductDao productDao = new ProductDaoImpl(emf);

        productDao.saveProduct(p1);
        productDao.saveProduct(p2);

        List<Product> productsResult = productDao.listProducts();
        Assert.assertEquals(2, productsResult.size());
    }

    @Test
    public void testDaoRepositoryTest() throws Exception {
        assertNotNull(emfb);
        assertNotNull(envService);
        assertNotNull(jtaManager);

        DatabaseInfo dbi = inMemoryH2DatabaseInfo();
        EntityManagerFactory emf = new WrappedEntityManagerFactory(jtaManager,
            emfb.getOrCreateEntityManagerFactory(EnvTarget.MDB,
                EntityManagerFactorySetup.PoolSize.SMALL,
                new Class[] {},
                new ClassLoader[] { Product.class.getClassLoader() },
                dbi));
        assertNotNull(emf);

        Product p1 = new Product("1234", "Product 1 desc");
        Product p2 = new Product("1235", "Product 2 desc");
        ArrayList<Product> products = new ArrayList<>(2);

        products.add(p1);
        products.add(p2);

        ProductDao productDao = new ProductDaoImpl(emf);

        productDao.saveProducts(products);

        List<Product> productsResult = productDao.listProducts();
        Assert.assertEquals(2, productsResult.size());

    }

}

