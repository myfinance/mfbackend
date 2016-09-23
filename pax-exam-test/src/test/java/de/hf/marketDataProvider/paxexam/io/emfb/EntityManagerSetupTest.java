/** ----------------------------------------------------------------------------
 *
 * ---          Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : EntityManagerSetupTest.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 14.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketDataProvider.paxexam.io.emfb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import de.hf.marketDataProvider.paxexam.support.PAXExamTestSetup;
import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.marketdataprovider.api.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;

import static org.junit.Assert.assertNotNull;

@RunWith(PaxExam.class)
public class EntityManagerSetupTest extends PAXExamTestSetup {


    @Inject
    @OsgiService
    EntityManagerFactorySetup emfb;


    @Configuration
    public Option[] config() {
        return new Option[] { //
            super.configDefaults(),
            // enable this to be able to debug
            // first start this unit test, and then connect using remote debugging.
            //     getDebugOption(),
            // enable this to be able to debug
            // first start this unit test, and then connect using remote debugging.
            restFeatures()}; //, composite(vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005")) };
    }



    @Test
    public void testIoFeature()
        throws SQLException, SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        assertNotNull(emfb);

        //DatabaseInfo dbi = inMemoryH2DatabaseInfo();
        DatabaseInfo dbi = inMemoryH2DatabaseInfo();

        EntityManagerFactory testunit = emfb.buildEntityManagerFactory("testunit", new Class[] { Product.class }, new ClassLoader[] {Product.class.getClassLoader()}, dbi);
        EntityManager entityManager = testunit.createEntityManager();

        UserTransaction utx = (UserTransaction) bundleContext.getService(bundleContext.getServiceReference(UserTransaction.class.getName()));
        utx.begin();

        Product p1 = new Product("1234", "Product 1 desc");
        Product p2 = new Product("1235", "Product 2 desc");
        ArrayList<Product> products = new ArrayList<>(2);

        products.add(p1);
        products.add(p2);

        entityManager.persist(p1);
        entityManager.persist(p2);

        utx.commit();

        TypedQuery<Product> query = entityManager.createNamedQuery(Product.findAll, Product.class);
        List newproducts=query.getResultList();

        //Assert.assertEquals(2, newproducts.size());
    }

    /*@Test
    public void testIoFeature2()
        throws SQLException, SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        assertNotNull(productService);

        String value = productService.listProducts().toString();

    }*/


}

