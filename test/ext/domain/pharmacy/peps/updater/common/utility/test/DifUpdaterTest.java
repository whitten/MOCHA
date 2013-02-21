package EXT.DOMAIN.pharmacy.peps.updater.common.utility.test;
import java.io.File;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jndi.JndiTemplate;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.Configuration;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DifUpdater;
import junit.framework.TestCase;
/**
 * Test the DIF updater.
 */
public class DifUpdaterTest extends TestCase {
    DifUpdater updater;
    Configuration config;
    JndiTemplate template;
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        System.out.println("------------------------- " + getName() + " -------------------------\n");
        ApplicationContext context = new ClassPathXmlApplicationContext("xml/spring/datup/local/test/InterfaceContext.xml");
        updater = (DifUpdater) context.getBean("difUpdater");
        config = (Configuration) context.getBean("datupConfiguration");
        template = (JndiTemplate) context.getBean("jndiTemplate");
        
        config.load().setProperty(Configuration.FDB_BATCH_COMMIT_SIZE, 0);
    }
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
    }
    /**
     * Test custom update file.
     * 
     * Note, the custom tables in the FDB database must be empty! Run the "refreshDatabase" Ant target.
     * 
     * @throws Exception
     */
    public void testFdbCustomUpdate() throws Exception {
        assertTrue("bad JNDI", Boolean.FALSE.equals(template.lookup("DATUP_UPDATE_IN_PROGRESS")));
        
        updater.runUpdater(new File("etc/datup/CstmUpdFile_3.2.5_2010021610042945206.zip"));
        
        assertTrue("failed to reset JNDI", Boolean.FALSE.equals(template.lookup("DATUP_UPDATE_IN_PROGRESS")));
    }
