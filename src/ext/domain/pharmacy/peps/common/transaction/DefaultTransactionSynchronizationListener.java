/**
 * Copyright 2008, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.transaction;
/**
 * Default implementation of a {@link TransactionSynchronizationListener}. Each of its methods are empty and do nothing.
 * <p>
 * Sub classes can use this class so that they do not need to implement all methods required by the
 * {@link TransactionSynchronizationListener} interface. Each sub class can override only the methods required for that
 * particular listener.
 */
public class DefaultTransactionSynchronizationListener implements TransactionSynchronizationListener {
    /**
     * Default constructor.
     */
    public DefaultTransactionSynchronizationListener() {
        super();
    }
    /**
     * Called just prior to transaction commit, but not when a transaction is going to rollback.
     */
    public void beforeCommit() {
    }
    /**
     * Called just after transaction committed, but not when a transaction has rolled back.
     * <p>
     * Note that this method is executed outside of a transactional context. No transaction is available for use, therefore
     * database operations and other transaction dependent operations cannot be executed. If a transaction is required, use
     * {@link #beforeCommit()}.
     */
    public void afterCommit() {
    }
    /**
     * Called just after transaction rollback, but not when a transaction has committed.
     * <p>
     * Note that this method is executed outside of a transactional context. No transaction is available for use, therefore
     * database operations and other transaction dependent operations cannot be executed.
     */
    public void afterRollback() {
    }
