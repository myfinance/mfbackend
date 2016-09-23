/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentDAO.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 23.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.env;
import de.hf.dac.api.io.env.domain.DacEnvironmentConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

public class EnvironmentDAO {

    private EntityManagerFactory emf;

    public EnvironmentDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<DacEnvironmentConfiguration> getAllEnvironments() {
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("SELECT c FROM DacEnvironmentConfiguration c");

        return query.getResultList();
    }

    public String getEnvironmentByClientId(String clientId) {
        EntityManager em = emf.createEntityManager();
        String ret = null;

        Query query = em.createQuery("SELECT c.environment FROM DacEnvironmentConfiguration c where identifier=:clientId" +
            " and target='ClientId'");

        query.setParameter("clientId",clientId);
        ret = (String) query.getResultList().get(0);

        return ret;
    }

    private String getConfigurationId(EntityManager em, final String environment, final String target, final String type) {
        List<String> ret = null;

        Query query = em.createQuery("SELECT DISTINCT(c.identifier) FROM DacEnvironmentConfiguration c WHERE " +
            "c.target = :target and c.environment = :environment and c.type = :type");

        query.setParameter("environment",environment);
        query.setParameter("target",target);
        query.setParameter("type",type);

        ret = query.getResultList();

        return ret.get(0);
    }

}
