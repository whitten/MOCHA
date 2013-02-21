package EXT.DOMAIN.pharmacy.peps.updater.common.utility;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
/**
 * Ftp client with extended functionality for handling directories.
 * 
 * We had to avoid using 
normal
 FTP path shortcuts and do everything the 
long
 way to be compatible with VMS based on our
 * testing. The long way means changing directory by directory instead of using absolute path names, avoiding the use of
 * periods in the filename, and avoiding the use of forward listings.
 * 
 * Things that didn
t work in our testing:
 * 
 * 1. Absolute path filenames. For example, get /pharmacy/fdb_dif/FDB.zip 2. Filenames with more than one period. For
 * example, F_3.2_20100101_20100111_.zip 3. Filename listings in forward directories. For example, ls pharmacy/fdb_dif
 */
public class FtpClient extends FTPClient {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FtpClient.class);
    public static final String ROOT_DIRECTORY = "/";
    public static final String BACK_DIRECTORY = "..";
    public static final String DATUP_DIRECTORY = "droid";
    public static final String FULL_DIRECTORY = "full";
    public static final String INCREMENTAL_DIRECTORY = "incremental";
    public static final String CUSTOM_DIRECTORY = "custom";
    public static final String FAILED_DIRECTORY = "failed";
    private KeepAliveThread keepAliveThread;
    /**
     * Constructor.
     */
    public FtpClient() {
        this.keepAliveThread = new KeepAliveThread();
    }
    /**
     * Overridden to check the reply code for a proper connection and start the keep-alive thread.
     * 
     * @param hostname server hostname
     * @param port server port number
     * 
     * @see org.apache.commons.net.SocketClient#connect(java.lang.String, int)
     */
    public void connect(String hostname, int port, boolean keepAlive) throws IOException {
        super.connect(hostname, port);
        // verify the connection
        if (!FTPReply.isPositiveCompletion(getReplyCode())) {
            disconnect();
            throw new IOException("FTP connection to \"" + hostname + ":" + port + "\" was refused");
        }
        if (keepAlive) {
            keepAliveThread.startKeepAlive();
        }
    }
    /**
     * Overridden to stop the keep-alive thread.
     * 
     * 
     * @see org.apache.commons.net.ftp.FTPClient#disconnect()
     */
    @Override
    public void disconnect() throws IOException {
        keepAliveThread.stopKeepAlive();
        super.disconnect();
    }
    /**
     * Change to the specified paths, creating them if necessary.
     * 
     * @param path one or more paths
     * @param create true if create
     * @return true is successful
     * @throws IOException if the path is not accessible or unable to create
     */
    public boolean changeTo(FtpPath path, boolean create) throws IOException {
        changeTo(FtpClient.ROOT_DIRECTORY, false);
        for (String part : path.getParts()) {
            changeTo(part, create);
        }
        return true;
    }
    /**
     * Change to the specified path, creating it if necessary.
     * 
     * @param path path to change to
     * @param create true if create
     * @return true if successful
     * @throws IOException if the path is not accessible or unable to create
     */
    public boolean changeTo(String path, boolean create) throws IOException {
        if (!changeWorkingDirectory(path)) {
            if (create) {
                if (!makeDirectory(path) || !changeWorkingDirectory(path)) {
                    throw new IOException("Unable to create the specified path: " + path);
                }
            }
            else {
                throw new IOException("Unable to change the working path to: " + path);
            }
        }
        LOG.debug("Current directory: " + printWorkingDirectory());
        return true;
    }
    /**
     * Store file stream to a directory.
     * 
     * @param path directory
     * @param name remote filename
     * @param in file stream
     * @return true if successful
     * @throws IOException path is not accessible or file stream is broken
     */
    public boolean storeTo(FtpPath path, String name, InputStream in) throws IOException {
        if (changeTo(path, true)) {
            return storeFile(name, in);
        }
        return false;
    }
    /**
     * Store file to a directory.
     * 
     * @param path directory
     * @param name remote filename
     * @param file file
     * @return true if successful
     * @throws FileNotFoundException file doesn't exist
     * @throws IOException path is not accessible or file stream is broken
     */
    public boolean storeTo(FtpPath path, String name, File file) throws FileNotFoundException, IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(file), 16384);
        try {
            return storeTo(path, name, in);
        }
        finally {
            in.close();
        }
    }
    /**
     * Delete files in a directory.
     * 
     * @param path path
     * @return true if successful
     * @throws IOException error occurs
     */
    public boolean deleteFiles(FtpPath path) throws IOException {
        boolean success = true;
        changeTo(path, true);
        for (FTPFile file : listFiles()) {
            success = deleteFile(file.getName());
        }
        return success;
    }
    /**
     * Delete file.
     * 
     * @param path path
     * @param name filename
     * @return true if successful
     * @throws IOException error occurs
     */
    public boolean deleteFile(FtpPath path, String name) throws IOException {
        if (changeTo(path, true)) {
            return deleteFile(name);
        }
        return false;
    }
    /**
     * Delete all files after the directory retention count is reached. If 7 is specified, files 8+ will be deleted.
     * 
     * @param ftp client connection
     * @param path path to delete from
     * @param retention retention count
     * @throws IOException FTP error occurs
     */
    public void deleteGreaterThan(FtpPath path, int retention) throws IOException {
        changeTo(path, true);
        List<FTPFile> ftpFiles = Arrays.asList(listFiles());
        // sort by date
        Collections.sort(ftpFiles, new Comparator<FTPFile>() {
            /**
             * Sort newest to oldest.
             * 
             * @param a
             * @param b
             * @return
             */
            public int compare(FTPFile a, FTPFile b) {
                return b.getTimestamp().compareTo(a.getTimestamp());
            }
        });
        // delete the oldest file(s) greater than the retention count
        for (int i = retention; i >= 0 && i < ftpFiles.size(); i++) {
            if (!deleteFile(ftpFiles.get(i).getName())) {
                LOG.error("Unable to apply retention policy (" + retention + ") in '" + path + "' to: " + ftpFiles.get(i));
            }
        }
    }
    /**
     * Retrieve remote file to temporary file.
     * 
     * @param remoteFile remote file path
     * @return temporary file
     * @throws FileNotFoundException file creation fails
     * @throws IOException FTP error occurs
     */
    public File retrieveToTemporaryFile(String remoteFile) throws FileNotFoundException, IOException {
        File temp = File.createTempFile("datup", ".zip");
        OutputStream out = new BufferedOutputStream(new FileOutputStream(temp), 16384);
        try {
            retrieveFile(remoteFile, out);
        }
        finally {
            out.flush();
            out.close();
        }
        return temp;
    }
    /**
     * Represents an FtpPath.
     */
    public static class FtpPath {
        private String[] parts;
        /**
         * Constructor.
         * 
         * @param parts parts
         */
        public FtpPath(String[] parts) {
            this.parts = parts;
        }
        /**
         * Constructor.
         * 
         * @param path parent path
         * @param parts parts
         */
        public FtpPath(FtpPath path, String[] parts) {
            this.parts = new String[path.parts.length + parts.length];
            System.arraycopy(path.parts, 0, this.parts, 0, path.parts.length);
            System.arraycopy(parts, 0, this.parts, path.parts.length, parts.length);
        }
        /**
         * Parts.
         * 
         * @return parts
         */
        public String[] getParts() {
            return parts;
        }
        /**
         * String representation.
         * 
         * @return string
         * 
         * @see java.lang.Object#toString()
         */
        public String toString() {
            StringBuilder builder = new StringBuilder("/");
            for (String part : parts) {
                builder.append(part).append("/");
            }
            return builder.toString();
        }
    }
    /**
     * Keep the FTP server connection open.
     */
    private class KeepAliveThread extends Thread {
        private boolean runnable;
        /**
         * Periodically send a no-op to the FTP server to keep the connection open. *
         * 
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            try {
                while (runnable) {
                    try {
                        Thread.sleep(1000 * 60 * 1); // 1 minute
                    }
                    catch (InterruptedException e) {
                        LOG.trace("FTP keep-alive thread interrupted", e);
                    }
                    synchronized (FtpClient.this) {
                        if (FtpClient.this.isConnected()) {
                            FtpClient.this.sendNoOp();
                        }
                    }
                }
            }
            catch (IOException e) {
                LOG.error("FTP keep-alive thread failed unexpectantly", e);
            }
        }
        /**
         * Start the keep-alive.
         */
        public void startKeepAlive() {
            synchronized (this) {
                if (runnable == true) {
                    return;
                }
                runnable = true;
            }
            start();
        }
        /**
         * Stop the keep-alive.
         */
        public synchronized void stopKeepAlive() {
            if (runnable == false) {
                return;
            }
            runnable = false;
        }
    }
