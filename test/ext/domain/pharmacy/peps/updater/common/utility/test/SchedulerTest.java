package EXT.DOMAIN.pharmacy.peps.updater.common.utility.test;
import java.util.Calendar;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.updater.common.capability.test.stub.DifUpdateCapabilityStub;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.Configuration;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DifUpdateScheduler;
import junit.framework.TestCase;
/**
 * Test the DIF update scheduler.
 */
public class SchedulerTest extends TestCase {
    DifUpdateScheduler scheduler;
    Configuration config;
    DifUpdateCapabilityStub stub;
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        System.out.println("------------------------- " + getName() + " -------------------------\n");
        // set a bogus configuration filename (shouldn't fail)
        System.setProperty("peps.datup.configuration", "does-not-exist-but-should-not-fail.xxx");
        ApplicationContext context = new ClassPathXmlApplicationContext("xml/spring/datup/local/test/InterfaceContext.xml");
        this.scheduler = (DifUpdateScheduler) context.getBean("difUpdateScheduler");
        this.config = (Configuration) context.getBean("datupConfiguration");
        config.load().setProperty(Configuration.SCHEDULED_TIME, "1600");
        
        this.stub = new DifUpdateCapabilityStub();
        scheduler.setDifUpdateCapability(stub);
    }
    /**
     * Test starting/stopping the scheduler.
     * 
     * @throws Exception
     */
    public void testStartStop() throws Exception {
        scheduler.start();
        assertTrue("scheduled hour of day", scheduler.getNextScheduledTime().get(Calendar.HOUR_OF_DAY) == 16);
        assertTrue("scheduler is stopped", scheduler.stop());
        assertFalse("Execute called", stub.didExecute);
    }
    /**
     * Test changing the time and reloading the configuration.
     * 
     * @throws Exception
     */
    public void testTimeChange() throws Exception {
        scheduler.start();
        assertTrue("scheduled hour of day", scheduler.getNextScheduledTime().get(Calendar.HOUR_OF_DAY) == 16);
        assertTrue("scheduler is stopped", scheduler.stop());
        assertFalse("Execute not called", stub.didExecute);
        config.load().setProperty("scheduled.time", "0300");
        scheduler.start();
        assertTrue("scheduled hour of day", scheduler.getNextScheduledTime().get(Calendar.HOUR_OF_DAY) == 3);
        assertTrue("scheduler is stopped", scheduler.stop());
        assertFalse("Execute not called", stub.didExecute);
    }
