/**
 * Copyright 2008, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.transaction;
import java.util.ArrayList;
import java.util.List;
/**
 * Utility class used to register {@link TransactionSynchronizationListener} instances and to make calls on those listeners.
 * <p>
 * A class which desires to add a {@link TransactionSynchronizationListener} must invoke the
 * {@link #addListener(TransactionSynchronizationListener)} method to register the given listener for the current
 * transaction.
 * <p>
 * A class which has access to the transaction uses the {@link #triggerAfterCommit()}, {@link #triggerAfterRollback()}, and
 * {@link #triggerBeforeCommit()} methods at the appropriate synchronization points. Once all synchronization is complete,
 * the {@link #clear()} method must be invoked.
 */
public class TransactionSynchronizationUtility {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(TransactionSynchronizationUtility.class);
    private static final ThreadLocal<List<TransactionSynchronizationListener>> SYNCHRONIZATIONS = new ThreadLocal<List<TransactionSynchronizationListener>>();
    /**
     * Set the current thread's List of {@link TransactionSynchronizationListener} to a new ArrayList.
     * <p>
     * Must be called prior to all transaction synchronization to initialize the List of
     * {@link TransactionSynchronizationListener}.
     */
    public static void init() {
        if (!isSynchronizationActive()) {
            SYNCHRONIZATIONS.set(new ArrayList<TransactionSynchronizationListener>());
        }
        else {
            LOG.warn("Transaction synchronization is already active. List of listeners has not been not re-initialized.");
        }
    }
    /**
     * Set the current thread's List of {@link TransactionSynchronizationListener} to null.
     * <p>
     * Must be called after all transaction synchronization is complete on the transaction to clean up ThreadLocal memory
     * references.
     */
    public static void clear() {
        SYNCHRONIZATIONS.set(null);
    }
    /**
     * Synchronization is active if the List of {@link TransactionSynchronizationListener} within the ThreadLocal variable is
     * not null.
     * <p>
     * Should always be true for all transactions as long as {@link #init()} has been called.
     * 
     * @return boolean Ture if synchronization is active
     */
    public static boolean isSynchronizationActive() {
        return SYNCHRONIZATIONS.get() != null;
    }
    /**
     * Add a listener for the current thread's transaction. The listener is added to the end of the current list.
     * <p>
     * After calling this method, the {@link TransactionSynchronizationListener} instance provided will be called at the
     * appropriate synchronization points.
     * 
     * @param listener {@link TransactionSynchronizationListener} to add
     * @return boolean true if the List of {@link TransactionSynchronizationListener} for the current transaction was changed
     * 
     * @see List#add(Object)
     */
    public static boolean addListener(TransactionSynchronizationListener listener) {
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active. Calling init() now prior to adding the given listener.");
            init();
        }
        return SYNCHRONIZATIONS.get().add(listener);
    }
    /**
     * Insert a listener for the current thread's transaction at the specified index in the list of
     * {@link TransactionSynchronizationListener}.
     * <p>
     * After calling this method, the {@link TransactionSynchronizationListener} instance provided will be called at the
     * appropriate synchronization points.
     * <p>
     * If the given index is less than zero, insert the given {@link TransactionSynchronizationListener} at the beginning of
     * the list. If the given index is greater than the size of the current list add the given
     * {@link TransactionSynchronizationListener} to the end of the list.
     * 
     * @param index position within the list of {@link TransactionSynchronizationListener} to insert given listener
     * @param listener {@link TransactionSynchronizationListener} to add
     * @return boolean true if the List of {@link TransactionSynchronizationListener} for the current transaction was changed
     * 
     * @see List#add(int, Object)
     * @see List#add(Object)
     */
    public static boolean addListener(int index, TransactionSynchronizationListener listener) {
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active. Calling init() now prior to adding the given listener.");
            init();
        }
        if (index < 0) {
            SYNCHRONIZATIONS.get().add(0, listener);
        }
        else if (index > SYNCHRONIZATIONS.get().size()) {
            SYNCHRONIZATIONS.get().add(listener);
        }
        else {
            SYNCHRONIZATIONS.get().add(index, listener);
        }
        return true;
    }
    /**
     * Add all of the given listeners for the current thread's transaction. The listeners are added to the end of the current
     * list.
     * <p>
     * After calling this method, the {@link TransactionSynchronizationListener} instance provided will be called at the
     * appropriate synchronization points.
     * 
     * @param listeners List of {@link TransactionSynchronizationListener} to add
     * @return boolean true if the List of {@link TransactionSynchronizationListener} for the current transaction was changed
     * 
     * @see List#addAll(java.util.Collection)
     */
    public static boolean addListeners(List<TransactionSynchronizationListener> listeners) {
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active. Calling init() now prior to adding the given listeners.");
            init();
        }
        return SYNCHRONIZATIONS.get().addAll(listeners);
    }
    /**
     * Insert all of the given listeners for the current thread's transaction at the given index within the current list.
     * <p>
     * After calling this method, the {@link TransactionSynchronizationListener} instance provided will be called at the
     * appropriate synchronization points.
     * <p>
     * If the given index is less than zero, insert the given List of {@link TransactionSynchronizationListener} at the
     * beginning of the list. If the given index is greater than the size of the current list add the given List of
     * {@link TransactionSynchronizationListener} to the end of the list.
     * 
     * @param index position within the list of {@link TransactionSynchronizationListener} to insert given listeners
     * @param listeners List of {@link TransactionSynchronizationListener} to add
     * @return boolean true if the List of {@link TransactionSynchronizationListener} for the current transaction was changed
     * 
     * @see List#addAll(int, java.util.Collection)
     */
    public static boolean addListeners(int index, List<TransactionSynchronizationListener> listeners) {
        boolean successful = false;
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active. Calling init() now prior to adding the given listeners.");
            init();
        }
        if (index < 0) {
            successful = SYNCHRONIZATIONS.get().addAll(0, listeners);
        }
        else if (index > SYNCHRONIZATIONS.get().size()) {
            successful = SYNCHRONIZATIONS.get().addAll(listeners);
        }
        else {
            successful = SYNCHRONIZATIONS.get().addAll(index, listeners);
        }
        return successful;
    }
    /**
     * Remove a listener for the current thread's transaction.
     * 
     * @param listener {@link TransactionSynchronizationListener} to remove
     * @return boolean true if the List of {@link TransactionSynchronizationListener} for the current transaction was changed
     * 
     * @see List#remove(Object)
     */
    public static boolean removeListener(TransactionSynchronizationListener listener) {
        boolean successful = false;
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active, so cannot remove given listener!");
        }
        else {
            successful = SYNCHRONIZATIONS.get().remove(listener);
        }
        return successful;
    }
    /**
     * Remove a listener from the current thread's transaction positioned at the given index within the current list.
     * 
     * @param index position within the current list to remove
     * @return {@link TransactionSynchronizationListener} that was at the given index
     * 
     * @see List#remove(int)
     */
    public static TransactionSynchronizationListener removeListener(int index) {
        TransactionSynchronizationListener listener = null;
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active, so cannot remove given listener!");
        }
        else {
            listener = SYNCHRONIZATIONS.get().remove(index);
        }
        return listener;
    }
    /**
     * Remove the given List of listeners from the current thread's transaction.
     * 
     * @param listeners List of {@link TransactionSynchronizationListener} to remove
     * @return boolean true if the List of {@link TransactionSynchronizationListener} for the current transaction was changed
     * 
     * @see List#removeAll(java.util.Collection)
     */
    public static boolean removeListeners(List<TransactionSynchronizationListener> listeners) {
        boolean successful = false;
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active, so cannot remove given listener!");
        }
        else {
            successful = SYNCHRONIZATIONS.get().removeAll(listeners);
        }
        return successful;
    }
    /**
     * Remove all listeners except those in the given List of {@link TransactionSynchronizationListener}.
     * 
     * @param listeners List of {@link TransactionSynchronizationListener} to retain
     * @return boolean true if the List of {@link TransactionSynchronizationListener} for the current transaction was changed
     * 
     * @see List#retainAll(java.util.Collection)
     */
    public static boolean retainListeners(List<TransactionSynchronizationListener> listeners) {
        boolean successful = false;
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active, so cannot remove given listener!");
        }
        else {
            successful = SYNCHRONIZATIONS.get().retainAll(listeners);
        }
        return successful;
    }
    /**
     * Returns true if the current list of {@link TransactionSynchronizationListener} for the current thread's transaction
     * includes the given listener.
     * 
     * @param listener {@link TransactionSynchronizationListener} to find
     * @return boolean if the given listener is in the current thread transaction's list of listeners
     * 
     * @see List#contains(Object)
     */
    public static boolean containsListener(TransactionSynchronizationListener listener) {
        boolean contains = false;
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active, so cannot test if listener is in the current list!");
        }
        else {
            contains = SYNCHRONIZATIONS.get().contains(listener);
        }
        return contains;
    }
    /**
     * Returns true if the current list of {@link TransactionSynchronizationListener} for the current thread's transaction
     * includes the given listeners.
     * 
     * @param listeners List of {@link TransactionSynchronizationListener} to find
     * @return boolean if the given listeners are in the current thread transaction's list of listeners
     * 
     * @see List#containsAll(java.util.Collection)
     */
    public static boolean containsListeners(List<TransactionSynchronizationListener> listeners) {
        boolean contains = false;
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active, so cannot test if listeners are in the current list!");
        }
        else {
            contains = SYNCHRONIZATIONS.get().containsAll(listeners);
        }
        return contains;
    }
    /**
     * Return the first index of the given {@link TransactionSynchronizationListener}. If the listener cannot be found,
     * return -1.
     * 
     * @param listener {@link TransactionSynchronizationListener} to find
     * @return first index of given listener, or -1 if it cannot be found
     * 
     * @see List#indexOf(Object)
     */
    public static int indexOf(TransactionSynchronizationListener listener) {
        int index = -1;
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active, so cannot find given listener!");
        }
        else {
            index = SYNCHRONIZATIONS.get().indexOf(listener);
        }
        return index;
    }
    /**
     * Return the last index of the given {@link TransactionSynchronizationListener}. If the listener cannot be found,
     * return -1.
     * 
     * @param listener {@link TransactionSynchronizationListener} to find
     * @return last index of given listener, or -1 if it cannot be found
     * 
     * @see List#lastIndexOf(Object)
     */
    public static int lastIndexOf(TransactionSynchronizationListener listener) {
        int index = -1;
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active, so cannot find given listener!");
        }
        else {
            index = SYNCHRONIZATIONS.get().lastIndexOf(listener);
        }
        return index;
    }
    /**
     * Return the {@link TransactionSynchronizationListener} at the given index. If the given index is less than zero or
     * greater than or equal to the current list's size, return null.
     * 
     * @param index int index of listener to return
     * @return {@link TransactionSynchronizationListener} if index is in bounds, else null
     */
    public static TransactionSynchronizationListener getListener(int index) {
        TransactionSynchronizationListener listener = null;
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active, so cannot find given listener!");
        }
        else if (index >= 0 && index < SYNCHRONIZATIONS.get().size()) {
            listener = SYNCHRONIZATIONS.get().get(index);
        }
        return listener;
    }
    /**
     * Set the {@link TransactionSynchronizationListener} at the given index (i.e., overwrite the current listener). Return
     * the previous {@link TransactionSynchronizationListener} at that index.
     * <p>
     * If the given index is less than zero, insert the given listener into the first position and return null.
     * <p>
     * If the given index is greater than or equal to the current list's size, add the to the end of the current list and
     * return null.
     * 
     * @param index int position in current list to reset
     * @param listener {@link TransactionSynchronizationListener} to set at given index
     * @return previous {@link TransactionSynchronizationListener} value or null
     * 
     * @see List#set(int, Object)
     * @see List#add(int, Object)
     * @see List#add(Object)
     */
    public static TransactionSynchronizationListener setListener(int index, TransactionSynchronizationListener listener) {
        TransactionSynchronizationListener previousListener = null;
        if (!isSynchronizationActive()) {
            LOG.warn("Transaction synchronization is not active, so cannot find given listener!");
        }
        else if (index < 0) {
            SYNCHRONIZATIONS.get().add(0, listener);
        }
        else if (index >= SYNCHRONIZATIONS.get().size()) {
            SYNCHRONIZATIONS.get().add(listener);
        }
        else {
            previousListener = SYNCHRONIZATIONS.get().set(index, listener);
        }
        return previousListener;
    }
    /**
     * Return the number of {@link TransactionSynchronizationListener} listening for events on the current transaction.
     * <p>
     * If transaction synchronization is not active the size is zero, but the size may be zero even if synchronization is
     * active and there are no registered listeners.
     * 
     * @return int size of List of {@link TransactionSynchronizationListener}
     * 
     * @see #isSynchronizationActive()
     */
    public static int size() {
        int size = 0;
        if (isSynchronizationActive()) {
            size = SYNCHRONIZATIONS.get().size();
        }
        return size;
    }
    /**
     * Call all registered {@link TransactionSynchronizationListener#beforeCommit()}.
     */
    public static void triggerBeforeCommit() {
        if (isSynchronizationActive()) {
            for (TransactionSynchronizationListener listener : SYNCHRONIZATIONS.get()) {
                listener.beforeCommit();
            }
        }
        else {
            LOG.error("Unable to invoke beforeCommit() methods because the synchronizations have not been initialized."
                + " Please be sure the TransactionSynchronizationUtility.init() method has been called");
        }
    }
    /**
     * Call all registered {@link TransactionSynchronizationListener#afterCommit()}.
     */
    public static void triggerAfterCommit() {
        if (isSynchronizationActive()) {
            for (TransactionSynchronizationListener listener : SYNCHRONIZATIONS.get()) {
                listener.afterCommit();
            }
        }
        else {
            LOG.error("Unable to invoke afterCommit() methods because the synchronizations have not been initialized."
                + " Please be sure the TransactionSynchronizationUtility.init() method has been called");
        }
    }
    /**
     * Call all registered {@link TransactionSynchronizationListener#afterRollback()}.
     */
    public static void triggerAfterRollback() {
        if (isSynchronizationActive()) {
            for (TransactionSynchronizationListener listener : SYNCHRONIZATIONS.get()) {
                listener.afterRollback();
            }
        }
        else {
            LOG.error("Unable to invoke afterRollback() methods because the synchronizations have not been initialized."
                + " Please be sure the TransactionSynchronizationUtility.init() method has been called");
        }
    }
    /**
     * Cannot instantiate.
     */
    private TransactionSynchronizationUtility() {
        super();
    }
