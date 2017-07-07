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

import de.hf.dac.api.base.exceptions.DACException;
import de.hf.dac.api.base.exceptions.DACMsgKey;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.InvalidTransactionException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.Stack;

public class OsgiTxProvider implements EntityTransaction {

    public static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(OsgiTxProvider.class);

    private final TransactionManager jtaManager;
    private final UserTransaction utx;

    private final ThreadLocal<Stack<ActiveTransaction>> stackedActiveTransactions = ThreadLocal.withInitial(() ->  new Stack<>() );

    public OsgiTxProvider(TransactionManager jtaManager, UserTransaction utx) {
        this.jtaManager = jtaManager;
        this.utx = utx;
    }

    @Override
    public void begin() {
        // not implemented
        throw new DACException("Should not be called in OSGI Environment");
    }

    public synchronized void begin(EntityManager em) {

        LOG.trace(">>>> TX BEGIN <<<<<");
        LOG.trace(getStatus(em));
        LOG.trace(">>>> TX BEGIN <<<<<");
        if (em == null)
            throw new DACException(DACMsgKey.ENTITYMANAGER_NULL, "Can't begin without EntityManager");
        try {

            if (utx.getStatus() != Status.STATUS_NO_TRANSACTION && stackedActiveTransactions.get().isEmpty()) {
                // active at thread but not stacked here
                // just keep a reminder and register synchronizer
                stackedActiveTransactions.get().push(new ActiveTransaction(em, jtaManager.getTransaction()));
            }

            // is one stacked (active)
            if (!stackedActiveTransactions.get().isEmpty()) {
                // mark as suspended
                ActiveTransaction active = stackedActiveTransactions.get().peek();
                active.setSuspended(jtaManager.suspend());

                // check if there was a suspendable active transaction. If not, warn and remove this from stack
                if (active.getSuspended() == null) {
                    stackedActiveTransactions.get().pop();
                    LOG.warn("Removed Transaction from Stack as it's not associated to current thread. But was supposed to be. \n%s:%s",
                        active.getEmHash(), active.getTxHash());
                }
            }
            if (utx.getStatus() == Status.STATUS_NO_TRANSACTION) {
                utx.begin();
            }
            // just keep a reminder and register synchronizer
            stackedActiveTransactions.get().push(new ActiveTransaction(em, jtaManager.getTransaction()));
            // make sure EM know about it
            em.joinTransaction();

            LOG.trace(">>>> END TX BEGIN <<<<<");
            LOG.trace(getStatus(em));
            LOG.trace(">>>> END TX BEGIN <<<<<");

        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new DACException(e);
        }
    }

    private String getStatus(EntityManager em) {
        StringBuilder buff = new StringBuilder("OsgiTxProvider: " + this.toString() + " ");
        try {
            if (em != null)
                buff.append("EM: ").append(em.toString()).append("\tStackedTxs: ").append(stackedActiveTransactions.get().size()).append("\n");
            buff.append("UTX Status: ").append(Integer.toString(utx.getStatus()));
            // check if all stacked txs contain a suspended transaction

            for (int count = stackedActiveTransactions.get().size(); count > 0; count--) {
                ActiveTransaction stackedActiveTransaction = stackedActiveTransactions.get().get(count - 1);
                if (stackedActiveTransaction.getSuspended() != null) {
                    buff.append("\n\tSuspended Tx: ").append(stackedActiveTransaction.toString()).append("\t");
                } else {
                    buff.append("\n\tActive TX: ").append(stackedActiveTransaction.toString()).append("\t");
                    if (count != stackedActiveTransactions.get().size()) {
                        LOG.trace("Expected Suspended Transaction  on Stack");
                        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                            LOG.trace(">>>" + stackTraceElement);
                        }
                        buff.append("\n\tWARNING: Should have found a suspended Tx at Stack Level ").append(count);
                    }
                }
            }
        } catch (SystemException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new DACException(e);
        }

        return buff.toString();
    }

    @Override
    public synchronized void commit() {
        LOG.trace(">>>> TX COMMIT <<<<<");
        LOG.trace(getStatus(null));
        LOG.trace(">>>> TX COMMIT <<<<<");
        try {
            if (utx.getStatus() == Status.STATUS_NO_TRANSACTION) {
                // cannot commit this
                return;
            }

            if (utx.getStatus() != Status.STATUS_NO_TRANSACTION) {
                if (!stackedActiveTransactions.get().isEmpty()) {
                    stackedActiveTransactions.get().pop();
                } else {
                    LOG.error("Expected active Transaction on Stack!!");
                    LOG.error(getStatus(null));
                }
                if (utx.getStatus() != Status.STATUS_NO_TRANSACTION) {
                    utx.commit();
                }
            }
            resumeSuspendedTransactionIfAnyAvailable();

            LOG.trace(">>>> END TX COMMIT <<<<<");
            LOG.trace(getStatus(null));
            LOG.trace(">>>> END TX COMMIT <<<<<");

        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new DACException(e);
        }
    }

    private void resumeSuspendedTransactionIfAnyAvailable() throws InvalidTransactionException, SystemException {
        // check if there is one to be resumed
        ActiveTransaction active = stackedActiveTransactions.get().empty() ? null : stackedActiveTransactions.get().peek();
        if (active != null && active.getSuspended() != null) {
            jtaManager.resume(active.getSuspended());
            active.setSuspended(null);
            if (utx.getStatus() != Status.STATUS_NO_TRANSACTION) {
                active.getEm().joinTransaction();
            }
        }
    }

    @Override
    public synchronized void rollback() {

        try {
            if (utx.getStatus() == Status.STATUS_ACTIVE || utx.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                stackedActiveTransactions.get().pop();
                utx.rollback();
            }

            resumeSuspendedTransactionIfAnyAvailable();

        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new DACException(e);
        }
    }

    @Override
    public synchronized void setRollbackOnly() {
        try {
            utx.setRollbackOnly();
        } catch (SystemException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new DACException(e);
        }
    }

    @Override
    public synchronized boolean getRollbackOnly() {
        try {
            return utx.getStatus() == Status.STATUS_MARKED_ROLLBACK;
        } catch (SystemException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new DACException(e);
        }
    }

    @Override
    public synchronized boolean isActive() {
        try {
            return utx.getStatus() != Status.STATUS_NO_TRANSACTION;
        } catch (SystemException e) {
            LOG.error(e.getLocalizedMessage(), e);
            throw new DACException(e);
        }
    }

    public synchronized Transaction removeTxForEntityManager(WrappedEntityManager wrappedEntityManager) throws SystemException {
        while (!stackedActiveTransactions.get().empty()) {
            if (stackedActiveTransactions.get().peek().getEmHash() == wrappedEntityManager.hashCode()) {
                stackedActiveTransactions.get().pop();
            } else {
                break;
            }
        }

        // make sure the em is not joined with current (resumed) transaction
        if (isActive()) {
            // we may have a resumed transaction attached here already.
            LOG.trace("Closing EM with active Transaction. Need to suspend this before closing em.");
            return jtaManager.suspend();
        }
        // no transaction suspended during em close.
        return null;
    }

    public void resumeAndJoin(EntityManager em, Transaction tx) {
        if (em != null && tx != null) {
            try {
                jtaManager.resume(tx);
            } catch (InvalidTransactionException | SystemException e) {
                throw DACException.getFromThrowable(e);
            }
        }
    }

}

class ActiveTransaction implements Synchronization {

    private final EntityManager em;
    private final int emHash;
    private final String txHash;

    private Transaction suspended;

    public ActiveTransaction(EntityManager em, Transaction tx) throws RollbackException, SystemException {
        if (tx != null) {
            tx.registerSynchronization(this);
            this.txHash = tx.toString();
        } else {
            this.txHash = "NULL";
        }

        this.emHash = em.hashCode();
        this.em = em;
    }

    public int getEmHash() {
        return emHash;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setSuspended(Transaction suspended) {
        this.suspended = suspended;
    }

    public Transaction getSuspended() {
        return suspended;
    }

    @Override
    public void beforeCompletion() {
        if (getEm() != null) {
            // unmanage all
            getEm().flush();
        }
    }

    @Override
    public void afterCompletion(int i) {
        // nothing to be done here
    }

    public EntityManager getEm() {
        return em;
    }

    @Override
    public String toString() {
        return "ActiveTransaction{" + "emHash=" + emHash + ", txHash='" + txHash + '\'' + ", suspended=" + suspended + '}';
    }
}
