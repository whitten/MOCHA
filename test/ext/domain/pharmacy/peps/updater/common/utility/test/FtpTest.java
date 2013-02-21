package EXT.DOMAIN.pharmacy.peps.updater.common.utility.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.apache.ftpserver.FtpServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.Configuration;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpClient;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpFactory;
import junit.framework.TestCase;
/**
 * Test the DIF update scheduler.
 */
public class FtpTest extends TestCase {
    public static final String PORT = "2021";
    public static final String HOST = "127.0.0.1";
    public static final String ANONYMOUS_PASS = "datup@DOMAIN.EXT";
    public static final String ANONYMOUS_USER = "anonymous";
    public static final String USER = "peps";
    public static final String PASS = "1234";
    public static final String FTP_HOME = "build/test/ftp";
    FtpFactory ftp;
    Configuration config;
    FtpServer server;
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        System.out.println("------------------------- " + getName() + " -------------------------\n");
        // clear old home directory
        if (new File(FTP_HOME).exists()) {
            assertTrue("failed to clear FTP directory", delete(new File(FTP_HOME)));
        }
        ApplicationContext context = new ClassPathXmlApplicationContext("xml/spring/datup/local/test/InterfaceContext.xml");
        this.ftp = (FtpFactory) context.getBean("ftpFactory");
        this.config = (Configuration) context.getBean("datupConfiguration");
        this.server = (FtpServer) context.getBean("testFtpServer");
        server.start();
        config.load().setProperty(Configuration.FTP_USERNAME, USER);
        config.load().setProperty(Configuration.FTP_PASSWORD, PASS);
        config.load().setProperty(Configuration.FTP_HOSTNAME, HOST);
        config.load().setProperty(Configuration.FTP_PORT, PORT);
        config.load().setProperty(Configuration.FTP_WORKING_DIRECTORY, "pharmacy");
        config.load().setProperty(Configuration.FTP_PENDING_DIRECTORY, "pending");
    }
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        server.stop();
    }
    /**
     * Test authenticated user account.
     * 
     * @throws Exception
     */
    public void testAuthenticatedAccount() throws Exception {
        FtpClient client = ftp.createConnection(false);
        uploadFile(client, new File("etc/datup/DIF32 UPD.ZIP"));
        Set<String> files = new HashSet<String>(Arrays.asList(client.listNames()));
        assertTrue("failed file upload", files.size() == 1);
        client.logout();
        client.disconnect();
    }
    /**
     * Test anonymous user account.
     * 
     * @throws Exception
     */
    public void testAnonymousAccount() throws Exception {
        testAuthenticatedAccount(); // upload DIF update
        config.load().setProperty(Configuration.FTP_USERNAME, ANONYMOUS_USER);
        config.load().setProperty(Configuration.FTP_PASSWORD, ANONYMOUS_PASS);
        FtpClient client = ftp.createConnection(false);
        File destinationFile = new File("build/tmp/", "1_DIF32-UPDATE_2010-29-01.zip");
        FileOutputStream out = new FileOutputStream(destinationFile);
        try {
            client.retrieveFile(client.listNames()[0], out);
        }
        finally {
            out.flush();
            out.close();
        }
        client.logout();
        client.disconnect();
        assertTrue("FTP file retrieval failed", destinationFile.canRead() && destinationFile.length() > 0);
    }
    /**
     * Recursively delete directory.
     * 
     * @param directory directory to delete
     * @return true is successful
     */
    public static boolean delete(File directory) {
        if (directory.isDirectory()) {
            String[] children = directory.list();
            for (int i = 0; i < children.length; i++) {
                if (!delete(new File(directory, children[i]))) {
                    return false;
                }
            }
        }
        return directory.delete();
    }
    /**
     * 
     * 
     * @param client
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void uploadFile(FtpClient client, File file) throws FileNotFoundException, IOException {
        uploadFile(client, file, Math.abs(new Random(System.currentTimeMillis()).nextInt()) + ".zip");
    }
    /**
     * 
     * 
     * @param client
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void uploadFile(FtpClient client, File file, String remoteName) throws FileNotFoundException, IOException {
        FileInputStream stream = new FileInputStream(file);
        try {
            client.storeFile(remoteName, stream);
        }
        finally {
            stream.close();
        }
    }
