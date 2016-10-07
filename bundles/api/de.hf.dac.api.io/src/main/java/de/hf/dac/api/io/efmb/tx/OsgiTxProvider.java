/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : OsgiTxrovider.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 07.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.efmb.tx;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.Stack;

public class OsgiTxProvider implements EntityTransaction {

    private final TransactionManager jtaManager;
    private final UserTransaction utx;

    private final Stack<ActiveTransaction> stackedActiveTransactions = new Stack<>();

    public OsgiTxProvider(TransactionManager jtaManager, UserTransaction utx) {
        this.jtaManager = jtaManager;
        this.utx = utx;
    }

    public void begin() {

    }

    public void begin(EntityManager em) {
        try {
            // is one stacked (active)
            if (stackedActiveTransactions.size() != 0) {
                // mark as suspended
                ActiveTransaction active = stackedActiveTransactions.peek();
                active.setSuspended(jtaManager.suspend());
            }

            if (utx.getStatus() == Status.STATUS_NO_TRANSACTION || utx.getStatus() == Status.STATUS_UNKNOWN) {
                utx.begin();
                // just keep a reminder
                stackedActiveTransactions.push(new ActiveTransaction(em.hashCode()));
            }
        } catch (NotSupportedException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit() {

        try {
            if (utx.getStatus() == Status.STATUS_ACTIVE) {
                // remove from stack as this needs to be an active, unresumed one
                ActiveTransaction active = stackedActiveTransactions.pop();
                utx.commit();
            }
            resumeSuspendedTransactionIfAnyAvailable();

        } catch (HeuristicMixedException e) {
            e.printStackTrace();
        } catch (HeuristicRollbackException e) {
            e.printStackTrace();
        } catch (RollbackException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        } catch (InvalidTransactionException e) {
            e.printStackTrace();
        }
    }

    private void resumeSuspendedTransactionIfAnyAvailable() throws InvalidTransactionException, SystemException {
        // check if there is one to be resumed
        ActiveTransaction active = stackedActiveTransactions.empty() ? null : stackedActiveTransactions.peek();
        if (active != null) {
            if (active.getSuspended() != null) {
                jtaManager.resume(active.getSuspended());
                active.setSuspended(null);
            }
        }
    }

    @Override
    public void rollback() {

        try {
            if (utx.getStatus() == Status.STATUS_ACTIVE) {
                ActiveTransaction active = stackedActiveTransactions.pop();
                utx.rollback();
            }

            resumeSuspendedTransactionIfAnyAvailable();

        } catch (SystemException e) {
            e.printStackTrace();
        } catch (InvalidTransactionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRollbackOnly() {
        try {
            utx.setRollbackOnly();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean getRollbackOnly() {
        return false;
    }

    @Override
    public boolean isActive() {
        try {
            return utx.getStatus() == Status.STATUS_ACTIVE;
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeTxForEntityManager(WrappedEntityManager wrappedEntityManager) {
        while (!stackedActiveTransactions.empty()) {
            if (stackedActiveTransactions.peek().getEmHash() == wrappedEntityManager.hashCode()) {
                stackedActiveTransactions.pop();
            } else {
                break;
            }
        }
    }
}

class ActiveTransaction {

    private int emHash;

    private Transaction suspended;

    public ActiveTransaction(int emHash) {
        this.emHash = emHash;
    }

    public int getEmHash() {
        return emHash;
    }

    public void setSuspended(Transaction suspended) {
        this.suspended = suspended;
    }

    public Transaction getSuspended() {
        return suspended;
    }
}
