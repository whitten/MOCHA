package EXT.DOMAIN.pharmacy.peps.updater.common.utility;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
public class FtpFactory {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FtpFactory.class);
    private Configuration configuration;
    private void FtpFactory() {
    }
    /**
     * Connect to the FTP server.
     * 
     * @param keepAlive true if keep-alive should be enabled
     * @return FTP connection
     * @throws IOException FTP server errors occurs
     */
    public FtpClient createConnection(boolean keepAlive) throws IOException {
        FtpClient client = new FtpClient();
        LOG.debug("Connecting to " + configuration.load().getString(Configuration.FTP_HOSTNAME) + ":"
            + configuration.load().getString(Configuration.FTP_PORT));
        client.connect(configuration.load().getString(Configuration.FTP_HOSTNAME), configuration.load().getInt(
            Configuration.FTP_PORT), keepAlive);
        synchronized (client) {
            // login
            if (!client.login(configuration.load().getString(Configuration.FTP_USERNAME), configuration.load().getString(
                Configuration.FTP_PASSWORD))) {
                throw new IOException("Unable to login to the FTP server as \""
                    + configuration.load().getString(Configuration.FTP_USERNAME) + "/"
                    + configuration.load().getString(Configuration.FTP_PASSWORD) + "\"");
            }
            client.enterLocalPassiveMode();
            // binary only
            if (!client.setFileType(FTPClient.BINARY_FILE_TYPE)) {
                throw new IOException("Unable to set file transfer type to binary");
            }
            // check for and create the working directory (if necessary)
            if (configuration.load().containsKey(Configuration.FTP_WORKING_DIRECTORY)) {
                client.changeTo(configuration.load().getString(Configuration.FTP_WORKING_DIRECTORY), true);
            }
            LOG.debug("Current working directory: " + client.printWorkingDirectory());
        }
        return client;
    }
    /**
     * Configuration
     * 
     * @param configuration configuration
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
