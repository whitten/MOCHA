/**
 * Copyright 2008, Southwest Research Institute
 * 
 */
package EXT.DOMAIN.pharmacy.peps.updater.common.capability.impl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.net.ftp.FTPFile;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDataVendorVersionVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.VersionCapability;
import EXT.DOMAIN.pharmacy.peps.updater.common.capability.DifUpdateCapability;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.Configuration;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DateTimeUtility;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DifUpdater;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.EmailUtility;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FdbZipFileReader;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpClient;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpFactory;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.VerificationTester;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FdbZipFileReader.FdbHeader;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpClient.FtpPath;
/**
 * 
 * Site Update Schedule Capability contains code common for processing DIF updates at Local and National.
 */
public abstract class DifUpdateCapabilityImpl implements DifUpdateCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DifUpdateCapabilityImpl.class);
    protected static final Pattern ZIP_FILE_PATTERN = Pattern.compile("(.*\\.zip)(;\\d+)?", Pattern.CASE_INSENSITIVE);
    protected static final Pattern SESSION_DATE_PATTERN = Pattern.compile("\\d{8}", Pattern.CASE_INSENSITIVE);
    protected static final Pattern DATUP_ARCHIVE_PATTERN = Pattern.compile("(.+)_(\\d+-\\d+)_(\\d{8})_(\\d{8})\\.zip",
        Pattern.CASE_INSENSITIVE);
    protected FtpFactory ftpFactory;
    protected Configuration config;
    protected DifUpdater difUpdater;
    protected VerificationTester verificationTester;
    protected VersionCapability versionCapability;
    protected EmailUtility emailUtility;
    /**
     * Execute the update.
     * 
     * @return true is an update occurred, false otherwise
     */
    public abstract boolean execute() throws IOException;
    /**
     * FTP Factory.
     * 
     * @param factory factory
     */
    public void setFtpFactory(FtpFactory factory) {
        this.ftpFactory = factory;
    }
    /**
     * Configuration.
     * 
     * @param config configuration
     */
    public void setConfiguration(Configuration config) {
        this.config = config;
    }
    /**
     * DIF updater.
     * 
     * @param updater updater
     */
    public void setDifUpdater(DifUpdater updater) {
        this.difUpdater = updater;
    }
    /**
     * Set the verification tester.
     * 
     * @param tester tester
     */
    public void setVerificationTester(VerificationTester tester) {
        this.verificationTester = tester;
    }
    /**
     * Version capability.
     * 
     * @param capability capability
     */
    public void setVersionCapability(VersionCapability capability) {
        this.versionCapability = capability;
    }
    /**
     * Email utility.
     * 
     * @param emailUtility capability
     */
    public void setEmailUtility(EmailUtility emailUtility) {
        this.emailUtility = emailUtility;
    }
    /**
     * Verify FDB session and custom session dates.
     * 
     * @param fdbVersion FDB version
     * @return true if verified, false if failed verification
     */
    public boolean verifyFdbDifStatus(DrugDataVendorVersionVo fdbVersion) {
        if (!SESSION_DATE_PATTERN.matcher(fdbVersion.getIssueDate()).matches()) {
            LOG.error("FDB-DIF is not functioning correctly, incorrect FDB-DIF session date: " + fdbVersion.getIssueDate());
            return false;
        }
        if (!SESSION_DATE_PATTERN.matcher(fdbVersion.getCustomIssueDate()).matches()) {
            LOG.error("FDB-DIF is not functioning correctly, incorrect FDB-DIF custom session date: "
                + fdbVersion.getCustomIssueDate());
            return false;
        }
        return true;
    }
    /**
     * Retrieve files and store as pending local files.
     * 
     * @param paths directory
     * @return pending file array
     * @throws IOException FTP communication error
     */
    public List<FdbArchive> retrieveFdbArchive(FtpClient ftp, FtpPath path, boolean download) throws IOException {
        ftp.changeTo(path, true);
        FTPFile[] ftpFiles = ftp.listFiles();
        List<FdbArchive> pendingFiles = new ArrayList<FdbArchive>();
        for (int i = 0; i < ftpFiles.length; i++) {
            Matcher filenameMatcher = ZIP_FILE_PATTERN.matcher(ftpFiles[i].getName());
            // ignore if not a ZIP file
            if (ftpFiles[i].isDirectory() || !filenameMatcher.matches()) {
                LOG.debug("Skipping non-ZIP file: " + ftpFiles[i]);
                continue;
            }
            if (download) { // download and physically read the header
                try {
                    File localFile = ftp.retrieveToTemporaryFile(ftpFiles[i].getName());
                    pendingFiles.add(new FdbArchive(path, ftpFiles[i], localFile, new FdbZipFileReader(localFile)
                        .readHeader()));
                }
                catch (IOException e) {
                    LOG.error("Unable to retrieve file via FTP, skipping: " + ftpFiles[i], e);
                }
            }
            else { // attempt to parse the header from the filename (don't download)
                Matcher matcher = DATUP_ARCHIVE_PATTERN.matcher(filenameMatcher.group(1));
                if (!matcher.matches()) {
                    LOG.debug("File does not match DATUP filename pattern, skipping: " + ftpFiles[i]);
                    continue;
                }
                pendingFiles.add(new FdbArchive(path, ftpFiles[i], filenameMatcher.group(1)));
            }
        }
        return pendingFiles;
    }
    /**
     * Remove and physically delete archives with a previous session date older than the date specified.
     * 
     * @param ftp ftp client
     * @param archives archive list
     * @param fdbSessionDate FDB cutoff date
     * @param fdbSessionDate custom cutoff date
     * @param delete true if should physically delete
     * @return true if successful
     */
    public boolean removeOlderThan(FtpClient ftp, List<FdbArchive> archives, String fdbSessionDate,
                                   String customSessionDate, boolean delete) throws IOException {
        LOG.debug("Ignoring FDB archives older than " + fdbSessionDate);
        LOG.debug("Ignoring FDB-Custom archives older than " + customSessionDate);
        for (int i = archives.size() - 1; i >= 0; i--) {
            FdbArchive archive = archives.get(i);
            FdbHeader header = archive.getHeader();
            if ((!header.isCustom() && !SESSION_DATE_PATTERN.matcher(fdbSessionDate).matches())
                || (header.isCustom() && !SESSION_DATE_PATTERN.matcher(customSessionDate).matches())) {
                LOG.debug("Not processing archive because the associated database session date is invalid: " + archive);
                archives.remove(i);
            }
            else if ((!header.isCustom() && header.getPreviousSessionDate().compareTo(fdbSessionDate) < 0)
                || (header.isCustom() && header.getPreviousSessionDate().compareTo(customSessionDate) < 0)) {
                LOG.debug("Not processing out-of-date (older) archive: " + archive);
                archives.remove(i);
                if (delete) {
                    LOG.debug("Deleting archive from FTP server: " + archive);
                    if (!ftp.deleteFile(archive.getPath(), archive.getFtpFile().getName())) {
                        LOG.warn("Unable to delete archive: " + archive.getFtpFile());
                    }
                }
            }
        }
        return true;
    }
    /**
     * Build FDB version email template values.
     * 
     * @param fdbVersion FDB version
     * @return template values
     */
    public Map<String, String> buildEmailTemplateValues(DrugDataVendorVersionVo fdbVersion) {
        Map<String, String> emailValues = new HashMap<String, String>();
        emailValues.put(EmailUtility.TYPE_KEY, "FDB-DIF, VA-Custom");
        emailValues.put(EmailUtility.DATE_TIME_KEY, fdbVersion.getIssueDate() + ", " + fdbVersion.getCustomIssueDate());
        emailValues.put(EmailUtility.VERSION_KEY, fdbVersion.getDataVersion() + ", " + fdbVersion.getCustomDataVersion());
        return emailValues;
    }
    /**
     * Build email template values.
     * 
     * @param header FDB header
     * @return template values
     */
    public Map<String, String> buildEmailTemplateValues(FdbHeader header) {
        Map<String, String> emailValues = new HashMap<String, String>();
        emailValues.put(EmailUtility.TYPE_KEY, (header.isCustom() ? "VA-Custom" : "FDB-DIF") + " Incremental Data");
        emailValues.put(EmailUtility.DATE_TIME_KEY, DateTimeUtility.reformatFdbSessionDate(header.getNewSessionDate()));
        emailValues.put(EmailUtility.VERSION_KEY, header.getDatabaseVersion());
        return emailValues;
    }
    /**
     * Sort the archive list by previous session date, custom/non-custom, and upload timestamp.
     * 
     * @param archives unsorted archives
     */
    public void sortByDependencyOrder(List<FdbArchive> archives) {
        Collections.sort(archives, new Comparator<FdbArchive>() {
            /**
             * Sort by previous session date, ascending.
             * 
             * @param a archive 1
             * @param b archive 2
             * @return 0 if equal, -1 if less than, 1 if greater than
             */
            public int compare(FdbArchive a, FdbArchive b) {
                int previousSessionDate = a.getHeader().getPreviousSessionDate().compareTo(
                    b.getHeader().getPreviousSessionDate());
                // custom updates should be applied before incremental updates
                if (previousSessionDate == 0) {
                    if (a.getHeader().isCustom() && b.getHeader().isCustom()) {
                        // upload timestamp is tertiary sort for archives released on the same day
                        if ((a.getFtpFile() != null) && (b.getFtpFile() != null)) {
                            return a.getFtpFile().getTimestamp().compareTo(b.getFtpFile().getTimestamp());
                        }
                        return 0;
                    }
                    else if (a.header.isCustom() && !b.getHeader().isCustom()) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
                return previousSessionDate;
            }
        });
    }
    /**
     * Representation for an FDB zip file.
     */
    public class FdbArchive {
        private static final String CUSTOM_KEY = "C";
        private static final String FULL_KEY = "F";
        private static final String INCREMENTAL_KEY = "I";
        private FtpPath path;
        private String name;
        private FTPFile ftpFile;
        private File localFile;
        private FdbHeader header;
        /**
         * Physical archive constructor.
         * 
         * @param path root path
         * @param ftpFile ftp file
         * @param localFile local file
         */
        public FdbArchive(FtpPath path, FTPFile ftpFile, File localFile, FdbHeader header) {
            this.path = path;
            this.ftpFile = ftpFile;
            this.localFile = localFile;
            this.header = header;
            this.name = buildName(header);
        }
        /**
         * DATUP-named archive constructor.
         * 
         * @param path root path
         * @param ftpFile ftp file
         * @param fileName DATUP-encoded name
         */
        public FdbArchive(FtpPath path, FTPFile ftpFile, String filename) {
            this.path = path;
            this.ftpFile = ftpFile;
            this.header = buildHeader(filename);
            this.name = buildName(header);
        }
        /**
         * Root path.
         * 
         * @return root paths
         */
        public FtpPath getPath() {
            return path;
        }
        /**
         * FTP file.
         * 
         * @return ftp file
         */
        public FTPFile getFtpFile() {
            return ftpFile;
        }
        /**
         * Local file.
         * 
         * @return local file
         */
        public File getLocalFile() {
            return localFile;
        }
        /**
         * Get the FDB header.
         * 
         * @return FDB header
         */
        public FdbHeader getHeader() {
            return header;
        }
        /**
         * Archive name.
         * 
         * @return name
         */
        public String getName() {
            return name;
        }
        /**
         * Refresh the data for the archive.
         * 
         * @param ftp ftp client
         */
        public void refresh(FtpClient ftp) throws IOException {
            ftp.changeTo(path, true);
            this.localFile = ftp.retrieveToTemporaryFile(ftpFile.getName());
            this.name = buildName(header);
            this.header = new FdbZipFileReader(localFile).readHeader();
        }
        /**
         * Build the DATUP-encoded name, escaping characters as necessary.
         * 
         * @return archive name
         */
        private String buildName(FdbHeader header) {
            StringBuilder name = new StringBuilder();
            name.append(header.isIncremental() ? INCREMENTAL_KEY : FULL_KEY);
            if (header.isCustom()) {
                name.append("-").append(CUSTOM_KEY);
            }
            name.append("_").append(header.getDatabaseVersion().replace(".", "-"));
            name.append("_").append(header.getPreviousSessionDate());
            name.append("_").append(header.getNewSessionDate());
            name.append(".zip");
            return name.toString();
        }
        /**
         * Build the header from the DATUP-encoded name, replacing escaped characters.
         * 
         * @param fileName DATUP-encoded name
         * @return header
         */
        private FdbHeader buildHeader(String fileName) {
            Matcher matcher = DATUP_ARCHIVE_PATTERN.matcher(fileName.toUpperCase());
            if (!matcher.matches()) {
                return null;
            }
            return new FdbHeader(matcher.group(1).startsWith(INCREMENTAL_KEY), matcher.group(1).endsWith(CUSTOM_KEY),
                matcher.group(2).replace("-", "."), matcher.group(3), matcher.group(4), "");
        }
        /**
         * String representation.
         * 
         * @return string
         * 
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }
