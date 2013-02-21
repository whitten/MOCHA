package EXT.DOMAIN.pharmacy.peps.updater.national.messaging.impl;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.springframework.jndi.JndiTemplate;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.updater.common.capability.impl.DifUpdateCapabilityImpl.FdbArchive;
import EXT.DOMAIN.pharmacy.peps.updater.common.vo.external.update.DatupUpdate;
import EXT.DOMAIN.pharmacy.peps.updater.national.messaging.SendToExternalCapability;
import EXT.DOMAIN.pharmacy.peps.updater.national.messaging.utility.DatupUpdateXmlUtility;
public class SendToExternalCapabilityImpl implements SendToExternalCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SendToExternalCapabilityImpl.class);
    
    private JndiTemplate jndiTemplate;
    private String connectionFactory;
    private String topicName;
    /**
     * Send an XML message to an external topic.
     * 
     * @param FdbArchive archive
     */
    public void sendToExternalTopic(FdbArchive archive) {
        EXT.DOMAIN.pharmacy.peps.updater.common.vo.external.update.FdbArchive updateArchive = new EXT.DOMAIN.pharmacy.peps.updater.common.vo.external.update.FdbArchive();
        updateArchive.setCustom(archive.getHeader().isCustom()); 
        updateArchive.setDbVersion(archive.getHeader().getDatabaseVersion());
        updateArchive.setIncremental(archive.getHeader().isIncremental());
        updateArchive.setMessage(archive.getHeader().getMessage());
        updateArchive.setNewSessionDate(archive.getHeader().getNewSessionDate());
        updateArchive.setPreviousSessionDate(archive.getHeader().getPreviousSessionDate());
        
        DatupUpdate update = new DatupUpdate();
        update.getFdbArchive().add(updateArchive);
        
        String xmlMessage = DatupUpdateXmlUtility.marshal(update, true);
        LOG.debug("Sending to external interface: \n" + xmlMessage);
        
        try {
            TopicConnectionFactory factory = (TopicConnectionFactory) jndiTemplate.lookup(connectionFactory);
            Topic topic = (Topic) PortableRemoteObject.narrow(jndiTemplate.lookup(topicName), Topic.class);
            TopicConnection connection = factory.createTopicConnection();
            try {
                TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
                try {
                    TopicPublisher sender = session.createPublisher(topic);
                    try {
                        ObjectMessage message = session.createObjectMessage(xmlMessage);
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
     * Topic name.
     * 
     * @param topicName name
     */
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
