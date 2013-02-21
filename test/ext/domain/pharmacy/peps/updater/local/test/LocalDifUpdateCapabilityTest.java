package EXT.DOMAIN.pharmacy.peps.updater.local.test;
import java.io.File;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.ftpserver.FtpServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.common.exception.PharmacyRuntimeException;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.test.stub.VersionCapabilityStub;
import EXT.DOMAIN.pharmacy.peps.updater.common.capability.impl.DifUpdateCapabilityImpl.FdbArchive;
import EXT.DOMAIN.pharmacy.peps.updater.common.database.SiteUpdate;
import EXT.DOMAIN.pharmacy.peps.updater.common.database.test.SiteUpdateTest;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.Configuration;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpClient;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpFactory;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpClient.FtpPath;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.test.FtpTest;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.test.stub.DifUpdaterStub;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.test.stub.VerificationTesterStub;
import EXT.DOMAIN.pharmacy.peps.updater.local.capability.impl.LocalDifUpdateCapabilityImpl;
import EXT.DOMAIN.pharmacy.peps.updater.local.messaging.test.stub.SendToNationalCapabilityStub;
import junit.framework.TestCase;
/**
 * Test the DIF update scheduler.
 */
public class LocalDifUpdateCapabilityTest extends TestCase {
    Configuration config;
    FtpServer server;
    FtpFactory ftp;
    LocalDifUpdateCapabilityImpl updateCapability;
    DifUpdaterStub updaterStub;
    VersionCapabilityStub versionStub;
    FtpPath datupBasePath;
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        System.out.println("------------------------- " + getName() + " -------------------------\n");
        // clear old home directory
        if (new File(FtpTest.FTP_HOME).exists()) {
            assertTrue("failed to clear FTP directory", FtpTest.delete(new File(FtpTest.FTP_HOME)));
        }
        ApplicationContext context = new ClassPathXmlApplicationContext("xml/spring/datup/local/test/InterfaceContext.xml");
        this.config = (Configuration) context.getBean("datupConfiguration");
        this.ftp = (FtpFactory) context.getBean("ftpFactory");
        this.server = (FtpServer) context.getBean("testFtpServer");
        this.updateCapability = (LocalDifUpdateCapabilityImpl) context.getBean("difUpdateCapability");
        ((SendToNationalCapabilityStub) context.getBean("sendToNationalCapability")).block = true;
        server.start();
        // config.load().setProperty(Configuration.FTP_USERNAME, "PECS");
        // config.load().setProperty(Configuration.FTP_PASSWORD, "");
        // config.load().setProperty(Configuration.FTP_HOSTNAME, "127.0.0.1");
        // config.load().setProperty(Configuration.FTP_PORT, "21");
        config.load().setProperty(Configuration.FTP_USERNAME, FtpTest.USER);
        config.load().setProperty(Configuration.FTP_PASSWORD, FtpTest.PASS);
        config.load().setProperty(Configuration.FTP_HOSTNAME, FtpTest.HOST);
        config.load().setProperty(Configuration.FTP_PORT, FtpTest.PORT);
        config.load().setProperty(Configuration.FTP_WORKING_DIRECTORY, "pharmacy");
        config.load().setProperty(Configuration.FTP_PENDING_DIRECTORY, "fdb_dif");
        config.load().setProperty(Configuration.EMAIL_HOSTNAME, "mail.datasys.swri.edu");
        config.load().setProperty(Configuration.EMAIL_USERNAME, "");
        config.load().setProperty(Configuration.EMAIL_PASSWORD, "");
        config.load().setProperty(Configuration.EMAIL_SENDER, "noreply@datasys.swri.edu");
        config.load().setProperty(Configuration.EMAIL_LIST_SUCCESS, "nobody@datasys.swri.edu");
        config.load().setProperty(Configuration.EMAIL_LIST_FAILURE, "nobody@datasys.swri.edu");
        config.load().setProperty(Configuration.EMAIL_LIST_UPDATE_AVAILABLE, "nobody@datasys.swri.edu");
        config.load().setProperty(Configuration.SCHEDULED_TIME, "0200");
        config.load().setProperty(Configuration.FDB_RETENTON, 1);
        config.load().setProperty(Configuration.FDB_TEST_COUNT, 2);
        config.load().setProperty(Configuration.FDB_BATCH_COMMIT_SIZE, 0);
        config.load().setProperty(Configuration.RDC_NAME, "SwRI RDC");
        config.load().setProperty(Configuration.SITE_NUMBER, "358,402,405");
        this.updaterStub = new DifUpdaterStub();
        updaterStub.response = true;
        updateCapability.setDifUpdater(updaterStub);
        this.versionStub = new VersionCapabilityStub();
        updateCapability.setVersionCapability(versionStub);
        datupBasePath = new FtpPath(new String[] {config.load().getString(Configuration.FTP_WORKING_DIRECTORY),
                                                  FtpClient.DATUP_DIRECTORY});
        ApplicationContext nationalContext = new ClassPathXmlApplicationContext(
            "xml/spring/datup/national/test/InterfaceContext.xml");
        SiteUpdate siteUpdate = (SiteUpdate) nationalContext.getBean("siteUpdate");
        JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) context.getBean("datupDataSource"));
        SiteUpdateTest.clearFdbVersionTable(jdbcTemplate);
        siteUpdate.insertFdbVersion(false, "20100129", new Date());
        siteUpdate.insertFdbVersion(true, "20100216", new Date());
    }
    /**
     * Test retrieving/sorting Local FDB archives.
     * 
     * @throws Exception
     */
    public void testFileRetrieveAndSort() throws Exception {
        FtpClient client = ftp.createConnection(false);
        client.changeTo(FtpClient.DATUP_DIRECTORY, true);
        client.changeTo(FtpClient.FULL_DIRECTORY, true);
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 DB.ZIP"), "F_3-2_20091113_20091113.zip");
        client.changeTo(FtpClient.BACK_DIRECTORY, false);
        client.changeTo(FtpClient.INCREMENTAL_DIRECTORY, true);
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 UPD.ZIP"), "I_3-2_20100122_20100129.zip");
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 UPD.ZIP"), "I_3-2_20100129_20100205.zip");
        client.changeTo(FtpClient.BACK_DIRECTORY, false);
        client.changeTo(FtpClient.CUSTOM_DIRECTORY, true);
        FtpTest.uploadFile(client, new File("etc/datup/CstmUpdFile_3.2.5_2010021610042945206.zip"),
            "I-C_3-2_20100216_20100224.zip");
        FtpTest.uploadFile(client, new File("etc/datup/CstmUpdFile_3.2.5_2010021610042945206.zip"),
            "I-C_3-2_20091002_20100216.zip");
        client.changeTo(FtpClient.BACK_DIRECTORY, false);
        List<FdbArchive> incrementalArchives = updateCapability.retrieveFdbArchive(client, new FtpPath(datupBasePath,
            new String[] {FtpClient.INCREMENTAL_DIRECTORY}), false);
        assertEquals("Wrong count", 2, incrementalArchives.size());
        System.out.println("before sorting: " + incrementalArchives);
        updateCapability.sortByDependencyOrder(incrementalArchives);
        System.out.println("after sorting: " + incrementalArchives);
        assertEquals("Wrong order", "20100122", incrementalArchives.get(0).getHeader().getPreviousSessionDate());
        assertEquals("Wrong order", "20100129", incrementalArchives.get(1).getHeader().getPreviousSessionDate());
        assertTrue("Wrong type", incrementalArchives.get(0).getHeader().isIncremental()
            && !incrementalArchives.get(0).getHeader().isCustom());
        assertTrue("Wrong type", incrementalArchives.get(1).getHeader().isIncremental()
            && !incrementalArchives.get(1).getHeader().isCustom());
        List<FdbArchive> customArchives = updateCapability.retrieveFdbArchive(client, new FtpPath(datupBasePath,
            new String[] {FtpClient.CUSTOM_DIRECTORY}), false);
        assertEquals("Wrong count", 2, customArchives.size());
        System.out.println("before sorting: " + customArchives);
        updateCapability.sortByDependencyOrder(customArchives);
        System.out.println("after sorting: " + customArchives);
        assertEquals("Wrong order", "20091002", customArchives.get(0).getHeader().getPreviousSessionDate());
        assertEquals("Wrong order", "20100216", customArchives.get(1).getHeader().getPreviousSessionDate());
        assertTrue("Wrong type", customArchives.get(0).getHeader().isIncremental()
            && customArchives.get(0).getHeader().isCustom());
        assertTrue("Wrong type", customArchives.get(1).getHeader().isIncremental()
            && customArchives.get(1).getHeader().isCustom());
    }
    /**
     * Test execute a normal local update.
     * 
     * @throws Exception
     */
    public void testNormalLocalUpdateProcess() throws Exception {
        FtpClient client = ftp.createConnection(false);
        client.changeTo(FtpClient.DATUP_DIRECTORY, true);
        client.changeTo(FtpClient.INCREMENTAL_DIRECTORY, true);
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 UPD.ZIP"), "I_3-2_20100122_20100129.zip");
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 UPD.ZIP"), "I_3-2_20100129_20100205.zip");
        client.changeTo(FtpClient.BACK_DIRECTORY, false);
        client.changeTo(FtpClient.CUSTOM_DIRECTORY, true);
        FtpTest.uploadFile(client, new File("etc/datup/CstmUpdFile_3.2.5_2010021610042945206.zip"),
            "I-C_3-2_20100216_20100224.zip");
        FtpTest.uploadFile(client, new File("etc/datup/CstmUpdFile_3.2.5_2010021610042945206.zip"),
            "I-C_3-2_20091002_20100216.zip");
        client.changeTo(FtpClient.BACK_DIRECTORY, false);
        assertTrue("did not execute", updateCapability.execute());
        assertEquals("wrong update count", 4, updaterStub.runCount);
    }
    /**
     * Test execute a failed verification test.
     * 
     * @throws Exception
     */
    public void testFailedVerificationTestAndRecovery() throws Exception {
        VerificationTesterStub testerStub = new VerificationTesterStub();
        updateCapability.setVerificationTester(testerStub);
        testerStub.failOnRunCount = 2;
        testerStub.response = true;
        FtpClient client = ftp.createConnection(false);
        client.changeTo(FtpClient.DATUP_DIRECTORY, true);
        client.changeTo(FtpClient.INCREMENTAL_DIRECTORY, true);
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 UPD.ZIP"), "I_3-2_20100122_20100129.zip");
        client.changeTo(FtpClient.BACK_DIRECTORY, false);
        try {
            updateCapability.execute();
            fail("Verification test should fail");
        }
        catch (InterfaceException e) {
            e.printStackTrace(System.out);
        }
        assertEquals("wrong update count", 1, updaterStub.runCount);
        assertEquals("wrong tester count", 2, testerStub.runCount);
        /* test recovery from previous failed verification test */
        updaterStub.runCount = 0;
        testerStub.runCount = 0;
        testerStub.response = true;
        testerStub.failOnRunCount = -1;
        versionStub.fdbIssueDate = "20200101"; // fake an update
        assertFalse("incremental files were processed", updateCapability.execute());
        assertEquals("wrong update count", 0, updaterStub.runCount);
        assertEquals("wrong tester count", 1, testerStub.runCount);
    }
    /**
     * Test execute a failed DIF updater.
     * 
     * @throws Exception
     */
    public void testFailedDifUpdaterAndRecovery() throws Exception {
        VerificationTesterStub testerStub = new VerificationTesterStub();
        updateCapability.setVerificationTester(testerStub);
        updaterStub.response = false;
        testerStub.response = true;
        FtpClient client = ftp.createConnection(false);
        client.changeTo(FtpClient.DATUP_DIRECTORY, true);
        client.changeTo(FtpClient.INCREMENTAL_DIRECTORY, true);
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 UPD.ZIP"), "I_3-2_20100122_20100129.zip");
        client.changeTo(FtpClient.BACK_DIRECTORY, false);
        try {
            updateCapability.execute();
            fail("DIF updater should fail");
        }
        catch (PharmacyRuntimeException e) {
            e.printStackTrace(System.out);
        }
        assertEquals("wrong update count", 1, updaterStub.runCount);
        assertEquals("wrong tester count", 1, testerStub.runCount);
        /* test recovery from previous failed DIF update */
        updaterStub.runCount = 0;
        testerStub.runCount = 0;
        updaterStub.response = true;
        assertTrue("no incremental files were processed", updateCapability.execute());
        assertEquals("wrong update count", 1, updaterStub.runCount);
        assertEquals("wrong tester count", 2, testerStub.runCount);
    }
