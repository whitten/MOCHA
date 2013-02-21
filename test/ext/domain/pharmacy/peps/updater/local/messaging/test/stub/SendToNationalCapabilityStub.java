/**
 * Copyright 2008, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.local.messaging.test.stub;
import javax.jms.ObjectMessage;
import EXT.DOMAIN.pharmacy.peps.common.messaging.test.stub.ObjectMessageStub;
import EXT.DOMAIN.pharmacy.peps.common.transaction.DefaultTransactionSynchronizationListener;
import EXT.DOMAIN.pharmacy.peps.common.transaction.TransactionSynchronizationUtility;
import EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.test.SpringTestConfigUtility;
import EXT.DOMAIN.pharmacy.peps.updater.local.messaging.SendToNationalCapability;
import EXT.DOMAIN.pharmacy.peps.updater.national.messaging.ReceiveFromLocalService;
/**
 * Stub implementation used when running "out of container" to send a message to national.
 */
public class SendToNationalCapabilityStub implements SendToNationalCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SendToNationalCapabilityStub.class);
    public boolean block;
    /**
     * "Send" the VO to National by directly calling the {@link ReceiveFromLocalService} retrieved via Spring.
     * 
     * Using this approach, we don't have a concept of any different queues, so it just calls {@link #send(ValueObject)}.
     * 
     * @param valueObject ValueObject to send
     * @param queueName String queue to send on
     * 
     * @see EXT.DOMAIN.pharmacy.peps.service.local.messagingservice.outbound.capability.SendToNationalCapability#send(EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject,
     *      java.lang.String)
     */
    public void send(ValueObject valueObject, String queueName) {
        send(valueObject);
    }
    /**
     * "Send" the VO to National by directly calling the {@link ReceiveFromLocalService} retrieved via Spring.
     * 
     * Use the {@link ObjectMessageStub} stubbed implementation of {@link ObjectMessage} that can be used outside of the
     * container, without actually using JMS.
     * 
     * @param valueObject ValueObject to send
     * 
     * @see EXT.DOMAIN.pharmacy.peps.service.local.messagingservice.outbound.capability.SendToNationalCapability#send(EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject)
     */
    public void send(final ValueObject valueObject) {
        if (TransactionSynchronizationUtility.isSynchronizationActive()) {
            LOG.debug("Transaction synchronization is active. Sending VO only if local transaction commits.");
            TransactionSynchronizationUtility.addListener(new SendToNational(valueObject));
        }
        else {
            LOG.debug("Transaction synchronization not active. Sending VO to national immediately.");
            new SendToNational(valueObject).send();
        }
    }
    /**
     * Class that synchronizes with the transaction to send a message to National.
     */
    private class SendToNational extends DefaultTransactionSynchronizationListener {
        private ValueObject valueObject;
        /**
         * Set the ValueObject to send.
         * 
         * @param valueObject ValueObject to send
         */
        public SendToNational(final ValueObject valueObject) {
            this.valueObject = valueObject;
        }
        /**
         * Only send the ValueObject to national if the transaction is committed.
         * 
         * @see org.springframework.transaction.support.TransactionSynchronizationAdapter#afterCompletion(int)
         */
        @Override
        public void afterCommit() {
            send();
        }
        /**
         * Log a warning message saying that the message wasn't sent.
         * 
         * @see EXT.DOMAIN.pharmacy.peps.common.transaction.DefaultTransactionSynchronizationListener#afterRollback()
         */
        @Override
        public void afterRollback() {
            LOG.warn("Local transaction was not committed, so ValueObject will not be sent to national!");
        }
        /**
         * Thread a call into National to send the ValueObject.
         */
        public void send() {
            SendToNationalRunnable sendToNational = new SendToNationalRunnable();
            Thread threadNational = new Thread(sendToNational);
            threadNational.setContextClassLoader(this.getClass().getClassLoader());
            // in case we aren't using transaction synchronization, attempt to "slow down" this thread
            threadNational.setPriority(Thread.MIN_PRIORITY + 2);
            threadNational.start();
            if (block) {
                while (threadNational.isAlive()) {
                    try {
                        Thread.currentThread().sleep(500);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (sendToNational.error != null) {
                    throw new RuntimeException("Send to national stub failed", sendToNational.error);
                }
            }
        }
        /**
         * Runnable.
         */
        private class SendToNationalRunnable implements Runnable {
            public Throwable error;
            /**
             * Call into National
             * 
             * @see java.lang.Runnable#run()
             */
            public void run() {
                try {
                    ObjectMessage message = new ObjectMessageStub();
                    message.setObject(valueObject);
                    ReceiveFromLocalService receiveFromLocalService = SpringTestConfigUtility
                        .getNationalSpringBean(ReceiveFromLocalService.class);
                    LOG
                        .debug("Sending asynchronous (threaded) message to National by calling ReceiveFromLocalServiceImpl directly");
                    receiveFromLocalService.onMessage(message);
                }
                catch (Throwable t) {
                    LOG
                        .error(
                            "Send to National threw an exception. National's transaction will be rolled back, but local's will not.",
                            t);
                    this.error = t;
                }
            }
        };
    }
