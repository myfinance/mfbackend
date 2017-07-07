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
import javax.transaction.TransactionManager;

import de.hf.dac.api.io.efmb.tx.WrappedEntityManagerFactory;
import de.hf.dac.api.io.env.EnvironmentService;
import de.hf.dac.marketdataprovider.api.application.EnvTarget;
import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import de.hf.dac.marketdataprovider.persistence.ProductDaoImpl;
import de.hf.marketDataProvider.paxexam.support.PAXExamTestSetup;
import de.hf.dac.api.io.efmb.DatabaseInfo;
import de.hf.dac.api.io.efmb.EntityManagerFactorySetup;
import de.hf.dac.marketdataprovider.api.domain.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.util.Filter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;


@RunWith(PaxExam.class)
public class EntityManagerSetupTest extends PAXExamTestSetup {


    @Inject
    EntityManagerFactorySetup emfb;

    @Inject
    EnvironmentService envService;

    //@Inject
    //@Filter(timeout = 60000)
    TransactionManager jtaManager;


    @Configuration
    public Option[] config() {
        return new Option[] { //
            super.configDefaults(),
            ioFeatures(), restFeatures(), jdbcFeatures()
        , mavenBundle("org.jboss.narayana.osgi", "narayana-osgi-jta", "5.6.2.Final")

        };
    }


    @Test
    public void testWriteDatabase()
        throws SQLException, SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        assertNotNull(emfb);
        assertNotNull(envService);
        ServiceReference<TransactionManager> transactionRef=null;
        for(Bundle b : bundleContext.getBundles()){
            System.out.println("bundle:"+b.toString());
            if(b.toString().startsWith("narayana-osgi-jta")){
                for(ServiceReference s : b.getRegisteredServices()){
                    //System.out.println("service:"+s.toString());
                    if(s.toString().contains("javax.transaction.TransactionManager")){
                        transactionRef=s;
                    }
                }
            }
        }
//todo warum wird der Transactionmanager nicht gefunden? Es sieht so aus als ob c3po etc gar nicht geladen werden

        BundleContext thisBundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

        ServiceReference<TransactionManager> transactionRef2;
        System.out.println("name:"+TransactionManager.class.getName());
        transactionRef2=bundleContext.getServiceReference(TransactionManager.class);
        transactionRef2=thisBundleContext.getServiceReference(TransactionManager.class);
        Object jtaManagerRaw= bundleContext.getService(transactionRef);
        //OsgiTransactionManager jtaManagerRaw=(OsgiTransactionManager) bundleContext.getService(transactionRef);
        //assertNotNull(transactionRef2);

        //jtaManager = (OsgiTransactionManager)jtaManagerRaw;
         //   .getServiceReference(clazz.getName())

        /*jtaManager=getService(javax.transaction.TransactionManager.class);
        assertNotNull(jtaManager);
*/
        /*DatabaseInfo dbi = inMemoryH2DatabaseInfo();
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

        TypedQuery<Product> query = entityManager.createNamedQuery(Product.findAll, Product.class);
        List newproducts=query.getResultList();

        Assert.assertEquals(2, newproducts.size());*/

    }

    //@Test
    public void testDaoTest()
        throws SQLException, SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        assertNotNull(emfb);
        assertNotNull(envService);
        //jtaManager=getService(TransactionManager.class);
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

    //@Test
    public void testDaoRepositoryTest()
        throws SQLException, SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        assertNotNull(emfb);
        assertNotNull(envService);
        //jtaManager=getService(TransactionManager.class);
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

