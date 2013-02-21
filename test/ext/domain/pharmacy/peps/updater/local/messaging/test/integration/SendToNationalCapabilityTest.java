/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.local.messaging.test.integration;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.NamingException;
import org.springframework.jndi.JndiTemplate;
import EXT.DOMAIN.pharmacy.peps.common.test.integration.IntegrationTestCase;
import EXT.DOMAIN.pharmacy.peps.common.vo.DoseTypeVo;
import EXT.DOMAIN.pharmacy.peps.updater.local.messaging.SendToNationalCapability;
import EXT.DOMAIN.pharmacy.peps.updater.local.messaging.impl.SendToNationalCapabilityImpl;
/**
 * Verify sending reply to national works as expected
 */
public class SendToNationalCapabilityTest extends IntegrationTestCase {
    public static final String CONNECTION_FACTORY = "jms/EXT/DOMAIN/pharmacy/peps/messagingservice/factory";
    public static final String DATUP_QUEUE = "jms/EXT/DOMAIN/pharmacy/peps/messagingservice/queue/national/datup/receive";
    private SendToNationalCapability sendToNationalCapability;
    private QueueReceiver receiver;
    private Queue queue;
    private QueueConnection queueConnection;
    private QueueSession queueSession;
    private JndiTemplate context;
    /**
     * Setup the Test
     * 
     * @throws JMSException
     * @throws NamingException
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws JMSException, NamingException {
        this.context = getNationalJndiTemplate();
        SendToNationalCapabilityImpl impl = new SendToNationalCapabilityImpl();
        impl.setJndiTemplate(context);
        this.sendToNationalCapability = impl;
        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) context.lookup(CONNECTION_FACTORY);
        this.queueConnection = queueConnectionFactory.createQueueConnection();
        queueConnection.start();
        this.queue = (Queue) context.lookup(DATUP_QUEUE);
        this.queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        this.receiver = queueSession.createReceiver(queue);
    }
    /**
     * Close the subscriber.
     * 
     * @throws JMSException
     * @throws NamingException
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws JMSException, NamingException {
        receiver.close();
        queueSession.close();
        queueConnection.close();
    }
    /**
     * Test sending to the default queue at National
     */
    public void testSendSpecificQueue() {
        try {
            sendToNationalCapability.send(new DoseTypeVo(), DATUP_QUEUE);
        }
        catch (Exception e) {
            fail();
        }
    }
