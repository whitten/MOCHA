/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.messaging;
import javax.jms.Message;
import EXT.DOMAIN.pharmacy.peps.common.exception.CommonException;
/**
 * Receive a JMS message
 */
public interface MessagingServiceMdb {
    /**
     * Receive a JMS message
     * 
     * @param message JMS message
     * @throws CommonException
     */
    void onMessage(Message message) throws CommonException;
