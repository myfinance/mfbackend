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

/*import de.hf.common.util.jpa.DatabaseInfo;
import de.hf.common.util.jpa.EntityManagerFactorySetup;*/
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.api.io.DatabaseInfo;
import de.hf.dac.api.io.EntityManagerFactorySetup;
import de.hf.dac.marketdataprovider.api.domain.Product;
//import de.hf.marketdataprovider.persistence.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
/*import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Reference;*/
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.osgi.framework.BundleContext;
/*import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;*/

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
/*import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;*/

//import javax.transaction.Transactional;

import javax.inject.Singleton;

/**
 *
 * @author surak
 */
@Slf4j
//@Component(service = ProductService.class)
//@Transactional
@OsgiServiceProvider(classes = {ProductService.class})
@Singleton
public class ProductServiceImpl implements ProductService {

    //private ProductRepository productRepository;


    //@Inject @OsgiService
    //public void setProductRepository(ProductRepository productRepository) {this.productRepository = productRepository; }

    @Inject @OsgiService
    EntityManagerFactorySetup emfb;

    @Inject
    protected BundleContext bundleContext;

   /* EntityManagerFactorySetup efs;
    @Reference(cardinality = ReferenceCardinality.MANDATORY,
        policy = ReferencePolicy.STATIC)
    protected void setEntityManagerFactorySetup(EntityManagerFactorySetup efs) {
        this.efs = efs;
    }

    @Reference(target = "(osgi.unit.name=marketdatapostgres)")
    JpaTemplate jpa;*/

    //@Reference(target = "(component.name=de.hf.common.util.jpa.GenericEntityManager)")
    //EntityManager em;

    /*@PersistenceContext(unitName="tasklist")
    EntityManager emTest;*/


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


        DatabaseInfo dbi = new DatabaseInfo("jdbc:postgresql://localhost:5432/marketdata","postgres", "vulkan", "org.postgresql.Driver");
        dbi.setDialect("org.hibernate.dialect.PostgreSQL82Dialect");
        // force schema / table creation
        dbi.getExtraHibernateProperties().put("hibernate.hbm2ddl.auto","create-drop");
        EntityManagerFactory testunit = null;
        UserTransaction utx = null;
        try {

            testunit = emfb.buildEntityManagerFactory("testunit", new Class[] { Product.class }, new ClassLoader[] {}, dbi);
            EntityManager entityManager = testunit.createEntityManager();
            utx = (UserTransaction) bundleContext.getService(bundleContext.getServiceReference(UserTransaction.class.getName()));
            utx.begin();


            entityManager.persist(p1);
            entityManager.persist(p2);


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




       // TypedQuery<Product> query = em.createNamedQuery(Product.findAll, Product.class);
        //List oldproducts=query.getResultList();


        /*jpa.tx(TransactionType.Required,   ema -> {
            ema.persist(p1);
            ema.flush();

            JpaRepositoryFactory factory = new JpaRepositoryFactory(ema);
            final Bundle bundle = FrameworkUtil.getBundle(ProductRepository.class);
            final ClassLoader classLoader = bundle.adapt(BundleWiring.class).getClassLoader();
            factory.setBeanClassLoader(classLoader);

            productRepository = factory.getRepository(ProductRepository.class);
            List oldproductsRepo=productRepository.findAll();

            List savedObjects = productRepository.save(products);
        });*/

        //fetch from DB
        //Product fetchedProduct = productRepository.findOne(p1.getId());
        //log.debug("test:"+fetchedProduct.getId());

        return products;
    }
}
