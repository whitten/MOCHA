/**
 * Copyright 2008, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.transaction;
/**
 * A class can implement this interface to listen for three transaction events:
 * <ul>
 * <li>Just prior to the transaction committing.</li>
 * <li>Just after the transaction commits.</li>
 * <li>Just after the transaction rolls back.</li>
 * </ul>
 * 
 * Instances of this interface must not be managed by Spring as they will have to hold onto a state. For example, a message
 * to send. Instead, each use of a listener must create a new instance for each transaction.
 */
public interface TransactionSynchronizationListener {
    /**
     * Called just prior to transaction commit, but not when a transaction is going to rollback.
     */
    void beforeCommit();
    /**
     * Called just after transaction committed, but not when a transaction has rolled back.
     * <p>
     * Note that this method is executed outside of a transactional context. No transaction is available for use, therefore
     * database operations and other transaction dependent operations cannot be executed. If a transaction is required, use
     * {@link #beforeCommit()}.
     */
    void afterCommit();
    /**
     * Called just after transaction rollback, but not when a transaction has committed.
     * <p>
     * Note that this method is executed outside of a transactional context. No transaction is available for use, therefore
     * database operations and other transaction dependent operations cannot be executed.
     */
    void afterRollback();
