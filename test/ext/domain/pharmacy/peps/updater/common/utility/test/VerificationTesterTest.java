package EXT.DOMAIN.pharmacy.peps.updater.common.utility.test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.Configuration;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.VerificationTester;
import junit.framework.TestCase;
/**
 * Test the DIF update scheduler.
 */
public class VerificationTesterTest extends TestCase {
    VerificationTester tester;
    Configuration config;
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        System.out.println("------------------------- " + getName() + " -------------------------\n");
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        ApplicationContext context = new ClassPathXmlApplicationContext("xml/spring/datup/local/test/InterfaceContext.xml");
        this.tester = (VerificationTester) context.getBean("verificationTester");
        this.config = (Configuration) context.getBean("datupConfiguration");
        config.load().setProperty(Configuration.FDB_TEST_COUNT, 1);
    }
    /**
     * Test executing the verification tests.
     * 
     * @throws Exception
     */
    public void testExecution() throws Exception {
        assertTrue("Verification test failed!", tester.executeOrderCheckTests());
    }
