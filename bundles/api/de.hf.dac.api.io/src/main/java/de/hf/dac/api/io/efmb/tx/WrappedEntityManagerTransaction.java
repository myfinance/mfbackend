/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : WrappedEntityManagerTransaction.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.efmb.tx;

import javax.persistence.EntityTransaction;

public class WrappedEntityManagerTransaction implements EntityTransaction {
    private final WrappedEntityManager wrappedEntityManager;
    private final OsgiTxProvider txProvider;

    public WrappedEntityManagerTransaction(WrappedEntityManager wrappedEntityManager, OsgiTxProvider txProvider) {
        this.wrappedEntityManager = wrappedEntityManager;
        this.txProvider = txProvider;
    }

    @Override
    public void begin() {
        txProvider.begin(wrappedEntityManager);
    }

    @Override
    public void commit() {
        txProvider.commit();
    }

    @Override
    public void rollback() {
        txProvider.rollback();
    }

    @Override
    public void setRollbackOnly() {
        txProvider.setRollbackOnly();
    }

    @Override
    public boolean getRollbackOnly() {
        return txProvider.getRollbackOnly();
    }

    @Override
    public boolean isActive() {
        return txProvider.isActive();
    }
}
