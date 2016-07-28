/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : EntityManagerActivator.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 27.07.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.service;

import de.hf.common.util.jpa.DatabaseInfo;
import de.hf.common.util.jpa.EntityManagerFactorySetup;
import de.hf.marketdataprovider.domain.Instrument;
import de.hf.marketdataprovider.domain.Product;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

@Component
public class EntityManagerActivator implements BundleActivator {

    private ServiceRegistration registration;

    EntityManagerFactorySetup efs;
    /*@Reference(cardinality = ReferenceCardinality.MANDATORY,
        policy = ReferencePolicy.STATIC)
    protected void setEntityManagerFactorySetup(EntityManagerFactorySetup efs) {
        this.efs = efs;
    }*/

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        DatabaseInfo db = new DatabaseInfo("jdbc:postgresql://localhost:5432/marketdata", "postgres", "vulkan", "org.postgresql.Driver");
        Class<?>[] entities = new Class<?>[]{Product.class, Instrument.class};
        EntityManagerFactory sdbFactory=null;
        EntityManager em = null;
        ServiceReference ref  = bundleContext.getServiceReference(EntityManagerFactorySetup.class);
        efs=(EntityManagerFactorySetup)bundleContext.getService(ref);
        try {
            sdbFactory = efs.buildEntityManagerFactory("ServiceConfiguration", entities, db,null );
            em = sdbFactory.createEntityManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put("osgi.unit.name", "mymanager");
        registration = bundleContext.registerService(
            EntityManager.class.getName(),
            em,
            props);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        registration.unregister();
    }
}
