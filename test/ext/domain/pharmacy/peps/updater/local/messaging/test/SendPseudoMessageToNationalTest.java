package EXT.DOMAIN.pharmacy.peps.updater.local.messaging.test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.common.vo.DoseTypeVo;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.test.SpringTestConfigUtility;
import EXT.DOMAIN.pharmacy.peps.updater.local.messaging.SendToNationalCapability;
import EXT.DOMAIN.pharmacy.peps.updater.local.messaging.test.integration.SendToNationalCapabilityTest;
import EXT.DOMAIN.pharmacy.peps.updater.local.messaging.test.stub.SendToNationalCapabilityStub;
import EXT.DOMAIN.pharmacy.peps.updater.national.messaging.ReceiveFromLocalService;
import junit.framework.TestCase;
/**
 * Test the DIF update scheduler.
 */
public class SendPseudoMessageToNationalTest extends TestCase {
    SendToNationalCapability capability;
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        System.out.println("------------------------- " + getName() + " -------------------------\n");
        ApplicationContext context = new ClassPathXmlApplicationContext("xml/spring/datup/local/test/InterfaceContext.xml");
        this.capability = (SendToNationalCapability) context.getBean("sendToNationalCapability");
        ((SendToNationalCapabilityStub) capability).block = true;
    }
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
    }
    public void testSend() throws Exception {
        ReceiveFromLocalService receiveFromLocalService = SpringTestConfigUtility
            .getNationalSpringBean(ReceiveFromLocalService.class);
        capability.send(new DoseTypeVo(), SendToNationalCapabilityTest.DATUP_QUEUE);
    }
