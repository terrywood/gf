package com.gt.bmf.cronjob;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;


public abstract class TransactionalQuartzTask extends QuartzJobBean {

    private static final Logger log = Logger.getLogger(TransactionalQuartzTask.class);

    // spring injected reference
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Most of this method is copied from the HibernateInterceptor.
     */
    protected final void executeInternal(JobExecutionContext ctx)
            throws JobExecutionException {
        //Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        Session session =  sessionFactory.openSession();
        boolean existingTransaction = SessionFactoryUtils.isSessionTransactional(session, getSessionFactory());
        if (existingTransaction) {
            log.info("Found thread-bound Session for TransactionalQuartzTask");
        } else {
            TransactionSynchronizationManager.bindResource(getSessionFactory(),new SessionHolder(session));
        }
        try {
            executeTransactional(ctx);
        } catch (HibernateException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (existingTransaction) {
                log.debug("Not closing pre-bound Hibernate Session after TransactionalQuartzTask");
            } else {
                TransactionSynchronizationManager.unbindResource(getSessionFactory());
                SessionFactoryUtils.releaseSession(session, getSessionFactory());
            }
        }
    }

    /**
     * Implementing classes, implement this method.
     */
    protected abstract void executeTransactional(JobExecutionContext ctx)
            throws JobExecutionException;

}
