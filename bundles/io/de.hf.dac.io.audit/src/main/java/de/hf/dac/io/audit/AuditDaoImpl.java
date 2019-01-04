/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : AuditDaoImpl.java
 * Author(s)   : hf
 * Created     : 03.01.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.audit;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import de.hf.dac.api.io.audit.AuditDao;
import de.hf.dac.api.io.domain.DacMessages;

public class AuditDaoImpl implements AuditDao {
    private EntityManagerFactory emf;
    private EntityManager em;

    @Inject
    public AuditDaoImpl(@Named(ADB) EntityManagerFactory emf) {
        this.emf = emf;
        this.em = emf.createEntityManager();
    }

    public List<DacMessages> getAllMessages() {
        Query query = em.createQuery("SELECT c FROM DacMessages c");
        return query.getResultList();
    }

    public void saveMessage(DacMessages message) {
        em.getTransaction().begin();
        em.persist(message);
        em.getTransaction().commit();
    }
}
