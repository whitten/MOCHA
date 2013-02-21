/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.national.messaging.impl;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import EXT.DOMAIN.pharmacy.peps.common.exception.CommonException;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject;
import EXT.DOMAIN.pharmacy.peps.updater.national.messaging.ReceiveFromLocalCapability;
import EXT.DOMAIN.pharmacy.peps.updater.national.messaging.ReceiveFromLocalService;
/**
 * Receive response from local
 */
public class ReceiveFromLocalServiceImpl implements ReceiveFromLocalService {
    
    private ReceiveFromLocalCapability receiveFromLocalCapability;
    /**
     * Empty constructor
     */
    public ReceiveFromLocalServiceImpl() {
        super();
    }
    /**
     * Handling incoming JMS Message
     * 
     * @param message JMS Message
     * 
     * @throws CommonException if the message is invalid.
     */
    public void onMessage(Message message) throws CommonException {
        try {
            receiveFromLocalCapability.onMessage((ValueObject) ((ObjectMessage) message).getObject());
        }
        catch (JMSException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.MESSAGING_SERVICE);
        }
    }
    /**
     * 
     * @param capability capability property
     */
    public void setReceiveFromLocalCapability(ReceiveFromLocalCapability capability) {
        this.receiveFromLocalCapability = capability;
    }
