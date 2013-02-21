/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.local.messaging.impl;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.springframework.jndi.JndiTemplate;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject;
import EXT.DOMAIN.pharmacy.peps.updater.local.messaging.SendToNationalCapability;
/**
 * Send to National using JMS.
 */
public class SendToNationalCapabilityImpl implements SendToNationalCapability {
    private JndiTemplate jndiTemplate;
    private String connectionFactory;
    private String queueName;
    /**
     * Empty constructor
     */
    public SendToNationalCapabilityImpl() {
        super();
    }
    /**
     * Send to National using JMS.
     * 
     * @param valueObject object to send
     * @param queueName JMS queue to send reply on
     * 
     * @see EXT.DOMAIN.pharmacy.peps.external.local.messagingservice.depricated.outbound.capability.SendResponse#send(EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject)
     */
    public void send(ValueObject valueObject, String queueName) {
        try {
            QueueConnectionFactory factory = (QueueConnectionFactory) jndiTemplate.lookup(connectionFactory);
            Queue queue = (Queue) PortableRemoteObject.narrow(jndiTemplate.lookup(queueName), Queue.class);
            QueueConnection connection = factory.createQueueConnection();
            try {
                QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                try {
                    QueueSender sender = session.createSender(queue);
                    try {
                        ObjectMessage message = session.createObjectMessage(valueObject);
                        sender.send(message);
                    }
                    finally {
                        sender.close();
                    }
                }
                finally {
                    session.close();
                }
            }
            finally {
                connection.close();
            }
        }
        catch (NamingException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.MESSAGING_SERVICE, e
                .getLocalizedMessage());
        }
        catch (JMSException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.MESSAGING_SERVICE, e
                .getLocalizedMessage());
        }
    }
    /**
     * 
     * Send a ValueObject using to National.
     * 
     * @param valueObject The object to send to National.
     * 
     * @see EXT.DOMAIN.pharmacy.peps.service.local.messagingservice.outbound.capability.SendToNationalCapability#send(EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject)
     */
    public void send(ValueObject valueObject) {
        send(valueObject, queueName);
    }
    /**
     * Sets the JNDI template.
     * 
     * @param jndiTemplate JNDI
     */
    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate;
    }
    /**
     * Connection factory.
     * 
     * @param connectionFactory name
     */
    public void setConnectionFactory(String connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    /**
     * Queue name.
     * 
     * @param queueName name
     */
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
