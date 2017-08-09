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

import de.hf.dac.api.io.domain.DacServiceconfiguration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

public class EnvironmentDAO {

    private EntityManagerFactory emf;

    public EnvironmentDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<DacServiceconfiguration> getAllEnvironments() {
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("SELECT c FROM DacServiceconfiguration c");

        return query.getResultList();
    }
}
