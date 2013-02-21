package EXT.DOMAIN.pharmacy.peps.updater.national.test;
import java.io.File;
import java.util.Arrays;
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
import EXT.DOMAIN.pharmacy.peps.updater.common.database.test.SiteUpdateTest;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.Configuration;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpClient;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpFactory;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpClient.FtpPath;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.test.FtpTest;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.test.stub.DifUpdaterStub;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.test.stub.VerificationTesterStub;
import EXT.DOMAIN.pharmacy.peps.updater.national.capability.impl.NationalDifUpdateCapabilityImpl;
import junit.framework.TestCase;
/**
 * Test the DIF update scheduler.
 */
public class NationalDifUpdateCapabilityTest extends TestCase {
    Configuration config;
    FtpServer server;
    FtpFactory ftp;
    NationalDifUpdateCapabilityImpl updateCapability;
    DifUpdaterStub updaterStub;
    VersionCapabilityStub versionStub;
    JdbcTemplate jdbcTemplate;
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
        ApplicationContext context = new ClassPathXmlApplicationContext(
            "xml/spring/datup/national/test/InterfaceContext.xml");
        this.config = (Configuration) context.getBean("datupConfiguration");
        this.ftp = (FtpFactory) context.getBean("ftpFactory");
        this.server = (FtpServer) context.getBean("testFtpServer");
        this.updateCapability = (NationalDifUpdateCapabilityImpl) context.getBean("difUpdateCapability");
        jdbcTemplate = new JdbcTemplate((DataSource) context.getBean("datupDataSource"));
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
        this.updaterStub = new DifUpdaterStub();
        updaterStub.response = true;
        updateCapability.setDifUpdater(updaterStub);
        this.versionStub = new VersionCapabilityStub();
        updateCapability.setVersionCapability(versionStub);
    }
    /**
     * Test retrieving/sorting National FDB archives.
     * 
     * @throws Exception
     */
    public void testFileRetrieveAndSort() throws Exception {
        FtpClient client = ftp.createConnection(false);
        client.changeTo(config.load().getString(Configuration.FTP_PENDING_DIRECTORY), true);
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 UPD.ZIP"));
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 DB.ZIP"));
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 DB_BAD.ZIP"));
        FtpTest.uploadFile(client, new File("etc/datup/CstmUpdFile_3.2.5_2010021610042945206.zip"));
        client.changeTo(FtpClient.BACK_DIRECTORY, false);
        List<FdbArchive> archives = updateCapability.retrieveFdbArchive(client, new FtpPath(
            new String[] {config.load().getString(Configuration.FTP_WORKING_DIRECTORY),
                          config.load().getString(Configuration.FTP_PENDING_DIRECTORY)}), true);
        assertEquals("Wrong count", 3, archives.size());
        System.out.println("before sorting: " + archives);
        updateCapability.sortByDependencyOrder(archives);
        System.out.println("after sorting: " + archives);
        assertEquals("Wrong order", "20091002", archives.get(0).getHeader().getPreviousSessionDate());
        assertEquals("Wrong order", "20091113", archives.get(1).getHeader().getPreviousSessionDate());
        assertEquals("Wrong order", "20100122", archives.get(2).getHeader().getPreviousSessionDate());
    }
    /**
     * Test execute a normal national update.
     * 
     * @throws Exception
     */
    public void testNormalNationalUpdateProcess() throws Exception {
        SiteUpdateTest.clearFdbVersionTable(jdbcTemplate);
        FtpClient client = ftp.createConnection(false);
        client.changeTo(config.load().getString(Configuration.FTP_PENDING_DIRECTORY), true);
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 UPD.ZIP"));
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 DB.ZIP"));
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 DB_BAD.ZIP"));
        FtpTest.uploadFile(client, new File("etc/datup/CstmUpdFile_3.2.5_2010021610042945206.zip"));
        client.changeTo(FtpClient.BACK_DIRECTORY, false);
        boolean result = updateCapability.execute();
        client.changeTo(new FtpPath(new String[] {config.load().getString(Configuration.FTP_WORKING_DIRECTORY),
                                                  FtpClient.DATUP_DIRECTORY}), false);
        System.out.println("Full: " + Arrays.asList(client.listNames(FtpClient.FULL_DIRECTORY)));
        System.out.println("Custom: " + Arrays.asList(client.listNames(FtpClient.CUSTOM_DIRECTORY)));
        System.out.println("Incremental: " + Arrays.asList(client.listNames(FtpClient.INCREMENTAL_DIRECTORY)));
        System.out.println("Failed: " + Arrays.asList(client.listNames(FtpClient.FAILED_DIRECTORY)));
        assertTrue("pending files were not processed", result);
        assertEquals("wrong update count", 2, updaterStub.runCount);
        assertEquals("wrong retention count", 1, client.listNames(FtpClient.FULL_DIRECTORY).length);
        assertEquals("wrong retention count", 1, client.listNames(FtpClient.CUSTOM_DIRECTORY).length);
        assertEquals("wrong retention count", 1, client.listNames(FtpClient.INCREMENTAL_DIRECTORY).length);
    }
    /**
     * Test execute a failed verification test.
     * 
     * @throws Exception
     */
    public void testFailedVerificationTestAndRecovery() throws Exception {
        SiteUpdateTest.clearFdbVersionTable(jdbcTemplate);
        VerificationTesterStub testerStub = new VerificationTesterStub();
        updateCapability.setVerificationTester(testerStub);
        testerStub.response = false;
        FtpClient client = ftp.createConnection(false);
        client.changeTo(config.load().getString(Configuration.FTP_PENDING_DIRECTORY), true);
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 UPD.ZIP"));
        client.changeTo(FtpClient.BACK_DIRECTORY, false);
        try {
            updateCapability.execute();
            fail("Verification test should fail");
        }
        catch (InterfaceException e) {
            e.printStackTrace(System.out);
        }
        client.changeTo(FtpClient.DATUP_DIRECTORY, false);
        System.out.println("Full: " + Arrays.asList(client.listFiles(FtpClient.FULL_DIRECTORY)));
        System.out.println("Custom: " + Arrays.asList(client.listFiles(FtpClient.CUSTOM_DIRECTORY)));
        System.out.println("Incremental: " + Arrays.asList(client.listFiles(FtpClient.INCREMENTAL_DIRECTORY)));
        System.out.println("Failed: " + Arrays.asList(client.listFiles(FtpClient.FAILED_DIRECTORY)));
        assertEquals("wrong update count", 1, updaterStub.runCount);
        assertEquals("wrong tester count", 1, testerStub.runCount);
        assertEquals("wrong count", 0, client.listFiles(FtpClient.FULL_DIRECTORY).length);
        assertEquals("wrong count", 0, client.listFiles(FtpClient.CUSTOM_DIRECTORY).length);
        assertEquals("wrong count", 0, client.listFiles(FtpClient.INCREMENTAL_DIRECTORY).length);
        assertEquals("should be 1 failed", 1, client.listFiles(FtpClient.FAILED_DIRECTORY).length);
        /* test recovery from previous failed verification test */
        updaterStub.runCount = 0;
        testerStub.runCount = 0;
        testerStub.response = true;
        assertFalse("pending files were processed", updateCapability.execute());
        assertEquals("wrong update count", 0, updaterStub.runCount);
        assertEquals("wrong tester count", 1, testerStub.runCount);
        assertEquals("wrong count", 0, client.listFiles(FtpClient.FULL_DIRECTORY).length);
        assertEquals("wrong count", 0, client.listFiles(FtpClient.CUSTOM_DIRECTORY).length);
        assertEquals("wrong count", 1, client.listFiles(FtpClient.INCREMENTAL_DIRECTORY).length);
        assertEquals("should be 0 failed", 0, client.listFiles(FtpClient.FAILED_DIRECTORY).length);
    }
    /**
     * Test execute a failed DIF updater.
     * 
     * @throws Exception
     */
    public void testFailedDifUpdaterAndRecovery() throws Exception {
        SiteUpdateTest.clearFdbVersionTable(jdbcTemplate);
        VerificationTesterStub testerStub = new VerificationTesterStub();
        updateCapability.setVerificationTester(testerStub);
        testerStub.response = true;
        updaterStub.response = false;
        FtpClient client = ftp.createConnection(false);
        client.changeTo(config.load().getString(Configuration.FTP_PENDING_DIRECTORY), true);
        FtpTest.uploadFile(client, new File("etc/datup/DIF32 UPD.ZIP"));
        client.changeTo(FtpClient.BACK_DIRECTORY, false);
        try {
            updateCapability.execute();
            fail("DIF updater should fail");
        }
        catch (PharmacyRuntimeException e) {
            e.printStackTrace(System.out);
        }
        client.changeTo(FtpClient.DATUP_DIRECTORY, false);
        System.out.println("Full: " + Arrays.asList(client.listNames(FtpClient.FULL_DIRECTORY)));
        System.out.println("Custom: " + Arrays.asList(client.listNames(FtpClient.CUSTOM_DIRECTORY)));
        System.out.println("Incremental: " + Arrays.asList(client.listNames(FtpClient.INCREMENTAL_DIRECTORY)));
        System.out.println("Failed: " + Arrays.asList(client.listNames(FtpClient.FAILED_DIRECTORY)));
        assertEquals("wrong update count", 1, updaterStub.runCount);
        assertEquals("wrong tester count", 0, testerStub.runCount);
        assertEquals("wrong count", 0, client.listNames(FtpClient.FULL_DIRECTORY).length);
        assertEquals("wrong count", 0, client.listNames(FtpClient.CUSTOM_DIRECTORY).length);
        assertEquals("wrong count", 0, client.listNames(FtpClient.INCREMENTAL_DIRECTORY).length);
        assertEquals("should be 0 failed", 0, client.listNames(FtpClient.FAILED_DIRECTORY).length);
        /* test recovery from previous failed DIF updater */
        updaterStub.runCount = 0;
        testerStub.runCount = 0;
        updaterStub.response = true;
        assertTrue("no pending files were processed", updateCapability.execute());
        assertEquals("wrong update count", 1, updaterStub.runCount);
        assertEquals("wrong tester count", 1, testerStub.runCount);
        assertEquals("wrong count", 0, client.listNames(FtpClient.FULL_DIRECTORY).length);
        assertEquals("wrong count", 0, client.listNames(FtpClient.CUSTOM_DIRECTORY).length);
        assertEquals("wrong count", 1, client.listNames(FtpClient.INCREMENTAL_DIRECTORY).length);
        assertEquals("should be 0 failed", 0, client.listNames(FtpClient.FAILED_DIRECTORY).length);
    }
