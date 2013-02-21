/**
 * Copyright 2008, Southwest Research Institute
 * 
 */
package EXT.DOMAIN.pharmacy.peps.updater.national.capability.impl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceValidationException;
import EXT.DOMAIN.pharmacy.peps.common.exception.PharmacyRuntimeException;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDataVendorVersionVo;
import EXT.DOMAIN.pharmacy.peps.updater.common.capability.impl.DifUpdateCapabilityImpl;
import EXT.DOMAIN.pharmacy.peps.updater.common.database.SiteUpdate;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.Configuration;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DateTimeUtility;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.EmailUtility;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpClient;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.MailingListType;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpClient.FtpPath;
import EXT.DOMAIN.pharmacy.peps.updater.national.capability.NationalDifUpdateCapability;
import EXT.DOMAIN.pharmacy.peps.updater.national.messaging.SendToExternalCapability;
/**
 * 
 * Business logic for processing DIF updates at National.
 */
public class NationalDifUpdateCapabilityImpl extends DifUpdateCapabilityImpl implements NationalDifUpdateCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(NationalDifUpdateCapabilityImpl.class);
    private SiteUpdate siteUpdate;
    private SendToExternalCapability externalCapability;
    /**
     * Set the site update.
     * 
     * @param siteUpdate capability
     */
    public void setSiteUpdate(SiteUpdate siteUpdate) {
        this.siteUpdate = siteUpdate;
    }
    
    /**
     * External capability.
     * 
     * @param externalCapability externalCapability property
     */
    public void setExternalCapability(SendToExternalCapability externalCapability) {
        this.externalCapability = externalCapability;
    }
    /**
     * Execute the National update.
     */
    @Override
    public boolean execute() throws IOException {
        DrugDataVendorVersionVo fdbVersion = versionCapability.retrieveDrugDataVendorVersion();
        if (!verifyFdbDifStatus(fdbVersion)) {
            Map<String, String> emailValues = buildEmailTemplateValues(fdbVersion);
            emailValues.put(EmailUtility.REASON_KEY, "National FDB-DIF is not available.");
            notifyManagers(false, emailValues);
            throw new InterfaceException(InterfaceException.INTERFACE_ERROR, InterfaceException.DRUG_DATA_VENDOR);
        }
        FtpPath basePath = new FtpPath(new String[] {config.load().getString(Configuration.FTP_WORKING_DIRECTORY)});
        FtpPath datupBasePath = new FtpPath(basePath, new String[] {FtpClient.DATUP_DIRECTORY});
        FtpClient ftp = ftpFactory.createConnection(true);
        try {
            List<FdbArchive> pendingArchives = Collections.emptyList();
            synchronized (ftp) {
                processLastFailedDatabase(ftp, datupBasePath);
                // retrieve pending archives
                pendingArchives = retrieveFdbArchive(ftp, new FtpPath(basePath, new String[] {config.load().getString(
                    Configuration.FTP_PENDING_DIRECTORY)}), true);
                // delete old archives
                removeOlderThan(ftp, pendingArchives, fdbVersion.getIssueDate(), fdbVersion.getCustomIssueDate(), true);
            }
            if (pendingArchives.isEmpty()) {
                LOG.debug("No valid FDB-DIF archives detected on FTP server in the '"
                    + config.load().getString(Configuration.FTP_PENDING_DIRECTORY) + "' directory");
                return false;
            }
            LOG.debug("Processing archives: " + pendingArchives);
            try {
                synchronized (ftp) {
                    processFullDatabase(ftp, datupBasePath, pendingArchives);
                }
                processIncrementalDatabase(ftp, datupBasePath, pendingArchives);
            }
            finally {
                synchronized (ftp) {
                    applyRetention(ftp, datupBasePath);
                }
            }
        }
        finally {
            synchronized (ftp) {
                ftp.logout();
                ftp.disconnect();
            }
        }
        return true;
    }
    /**
     * Apply archive retention policy.
     * 
     * @param ftp FTP client
     * @param datupBasePath base path
     * @throws IOException error occurs
     */
    private void applyRetention(FtpClient ftp, FtpPath datupBasePath) throws IOException {
        ftp.deleteGreaterThan(new FtpPath(datupBasePath, new String[] {FtpClient.INCREMENTAL_DIRECTORY}), config.load()
            .getInt(Configuration.FDB_RETENTON));
        ftp.deleteGreaterThan(new FtpPath(datupBasePath, new String[] {FtpClient.CUSTOM_DIRECTORY}), config.load().getInt(
            Configuration.FDB_RETENTON));
    }
    /**
     * Check for and re-process failed FDB-DIF archives.
     * 
     * @param ftp client connection
     * @param datupBasePath base path
     * @throws IOException FTP error occurs
     */
    private void processLastFailedDatabase(FtpClient ftp, FtpPath datupBasePath) throws IOException {
        List<FdbArchive> failedArchives = retrieveFdbArchive(ftp, new FtpPath(datupBasePath,
            new String[] {FtpClient.FAILED_DIRECTORY}), true);
        // last update was unsuccessful
        if (!failedArchives.isEmpty()) {
            LOG.debug("Processing failed archive: " + failedArchives);
            if (failedArchives.size() > 1) {
                LOG.warn("Only one failed archive should exist at any given time");
            }
            // get failed archive
            FdbArchive failedArchive = failedArchives.get(0);
            Map<String, String> emailValues = buildEmailTemplateValues(failedArchive.getHeader());
            // execute the verification tests to check for existing errors from last run
            if (!verificationTester.executeOrderCheckTests()) {
                LOG.error("Unable to re-process the last National FDB-DIF update: " + failedArchive);
                emailValues.put(EmailUtility.REASON_KEY, "One or more FDB-DIF verification tests failed.");
                notifyManagers(false, emailValues);
                throw new InterfaceValidationException(InterfaceValidationException.FDB_VERIFICATION, DateTimeUtility
                    .reformatFdbSessionDate(failedArchive.getHeader().getNewSessionDate()));
            }
            // make available to local sites
            moveArchive(ftp, failedArchive, failedArchive.getHeader().isCustom() ? new FtpPath(datupBasePath,
                new String[] {FtpClient.CUSTOM_DIRECTORY}) : new FtpPath(datupBasePath,
                new String[] {FtpClient.INCREMENTAL_DIRECTORY}));
            notifyManagers(true, emailValues);
            applyRetention(ftp, datupBasePath);
            
            try {
                externalCapability.sendToExternalTopic(failedArchive);
            }
            catch(InterfaceException e) {
                LOG.warn("Unable to notify external interface topic about update: " + failedArchive, e);
            }
        }
    }
    /**
     * Check for pending FDB full database and move to the appropriate directory. Note, full updates are not processed, only
     * moved.
     * 
     * @param ftp client connection
     * @param datupBasePath base path
     * @param pendingArchives list of pending archives
     * @throws IOException FTP error occurs
     * @throws FileNotFoundException file doesn't exist
     */
    private void processFullDatabase(FtpClient ftp, FtpPath datupBasePath, List<FdbArchive> pendingArchives)
        throws IOException, FileNotFoundException {
        FtpPath fullPath = new FtpPath(datupBasePath, new String[] {FtpClient.FULL_DIRECTORY});
        // retrieve existing full archives
        List<FdbArchive> existingFullArchives = retrieveFdbArchive(ftp, fullPath, false);
        for (int i = pendingArchives.size() - 1; i >= 0; i--) {
            FdbArchive pendingArchive = pendingArchives.get(i);
            // move full archive, do not process
            if (!pendingArchive.getHeader().isIncremental()) {
                FdbArchive fullArchive = pendingArchives.remove(i);
                // delete older full archive(s) of same type
                for (int j = existingFullArchives.size() - 1; j >= 0; j--) {
                    FdbArchive existingFullArchive = existingFullArchives.get(j);
                    if (existingFullArchive.getHeader().isIncremental()) {
                        LOG.warn("Found incremental FDB archive in FULL directory, deleting: "
                            + existingFullArchive.getFtpFile());
                        existingFullArchives.remove(j);
                        // delete misplaced incremental archive
                        if (!ftp.deleteFile(existingFullArchive.getPath(), existingFullArchive.getFtpFile().getName())) {
                            LOG.error("Unable to remove misplaced incremental archive: " + existingFullArchive.getFtpFile());
                        }
                    }
                    else if (fullArchive.getHeader().isCustom() == existingFullArchive.getHeader().isCustom()) {
                        LOG.warn("Deleting older FDB archive in FULL directory: " + existingFullArchive.getFtpFile());
                        existingFullArchives.remove(j);
                        // delete existing older full archive
                        if (!ftp.deleteFile(existingFullArchive.getPath(), existingFullArchive.getFtpFile().getName())) {
                            LOG.error("Unable to remove existing full archive: " + existingFullArchive.getFtpFile());
                        }
                    }
                }
                // save full archive
                if (!ftp.storeTo(fullPath, fullArchive.getName(), fullArchive.getLocalFile())) {
                    LOG.error("Unable to save archive to directory '" + fullPath + "': " + fullArchive);
                }
                // delete full archive
                if (!ftp.deleteFile(fullArchive.getPath(), fullArchive.getFtpFile().getName())) {
                    LOG.error("Unable to remove processed file from pending directory: " + fullArchive.getFtpFile());
                }
                // attempt to delete temporary file
                if (!fullArchive.getLocalFile().delete()) {
                    LOG.warn("Unable to delete temporary file: " + fullArchive.getLocalFile().getAbsolutePath());
                }
                
                try {
                    externalCapability.sendToExternalTopic(fullArchive);
                }
                catch(InterfaceException e) {
                    LOG.warn("Unable to notify external interface topic about update: " + fullArchive, e);
                }
            }
        }
    }
    /**
     * Process incremental FDB and FDB-Custom updates.
     * 
     * @param ftp client connection
     * @param datupBasePath base path
     * @param pendingArchives unsorted list of archives
     * @throws FileNotFoundException file doesn't exist
     * @throws IOException FTP error occurs
     */
    private void processIncrementalDatabase(FtpClient ftp, FtpPath datupBasePath, List<FdbArchive> pendingArchives)
        throws FileNotFoundException, IOException {
        // sort incremental updates by session date (oldest first)
        sortByDependencyOrder(pendingArchives);
        LOG.debug("Processing incremental (in order): " + pendingArchives);
        for (FdbArchive archive : pendingArchives) {
            Map<String, String> emailValues = buildEmailTemplateValues(archive.getHeader());
            try {
                // perform the DIF update
                difUpdater.runUpdater(archive.getLocalFile());
            }
            catch (PharmacyRuntimeException e) {
                LOG.error("DIF Updater failed. Unable to process FDB-DIF update: " + archive, e);
                emailValues.put(EmailUtility.REASON_KEY, "FDB-DIF Updater tool failed.");
                notifyManagers(false, emailValues);
                // stop processing
                throw e;
            }
            // create FDB version record
            siteUpdate.insertFdbVersion(archive.getHeader().isCustom(), archive.getHeader().getNewSessionDate(), new Date());
            // execute the verification tests
            if (!verificationTester.executeOrderCheckTests()) {
                LOG.error("Verification tests failed. Unable to process FDB-DIF update: " + archive);
                synchronized (ftp) {
                    // do not make available to local sites
                    moveArchive(ftp, archive, new FtpPath(datupBasePath, new String[] {FtpClient.FAILED_DIRECTORY}));
                }
                emailValues.put(EmailUtility.REASON_KEY, "One or more FDB-DIF verification tests failed.");
                notifyManagers(false, emailValues);
                throw new InterfaceValidationException(InterfaceValidationException.FDB_VERIFICATION, DateTimeUtility
                    .reformatFdbSessionDate(archive.getHeader().getNewSessionDate()));
            }
            synchronized (ftp) {
                // make available to local sites
                moveArchive(ftp, archive, archive.getHeader().isCustom() ? new FtpPath(datupBasePath,
                    new String[] {FtpClient.CUSTOM_DIRECTORY}) : new FtpPath(datupBasePath,
                    new String[] {FtpClient.INCREMENTAL_DIRECTORY}));
            }
            notifyManagers(true, emailValues);
            
            try {
                externalCapability.sendToExternalTopic(archive);
            }
            catch(InterfaceException e) {
                LOG.warn("Unable to notify external interface topic about update: " + archive, e);
            }
        }
    }
    /**
     * Notify the managers of success/failure.
     * 
     * @param success true for success, false for failure
     * @param emailValues template key/value pairs
     */
    private void notifyManagers(boolean success, Map<String, String> emailValues) {
        if (success) {
            // notify national managers of successful update
            if (!emailUtility.sendEmail(MailingListType.NATIONAL_SUCCESS, emailValues)) {
                LOG.error("Unable to send National success email");
            }
            // notify local managers of available update
            if (!emailUtility.sendEmail(MailingListType.UPDATE_AVAILABLE, emailValues)) {
                LOG.error("Unable to send National update available email");
            }
        }
        else {
            if (!emailUtility.sendEmail(MailingListType.NATIONAL_FAILURE, emailValues)) {
                LOG.error("Unable to send National failure email");
            }
        }
    }
    /**
     * Move an archive from one location to another (FTP has no "mv" command).
     * 
     * @param ftp client connection
     * @param archive archive to be moved
     * @param path new location
     * @throws FileNotFoundException file doesn't exist
     * @throws IOException FTP error occurs
     */
    private void moveArchive(FtpClient ftp, FdbArchive archive, FtpPath path) throws FileNotFoundException, IOException {
        // save new archive
        if (!ftp.storeTo(path, archive.getName(), archive.getLocalFile())) {
            LOG.error("Unable to save FTP file to directory '" + path + "': " + archive);
        }
        // delete old archive
        if (!ftp.deleteFile(archive.getPath(), archive.getFtpFile().getName())) {
            LOG.error("Unable to remove archive from FTP server: " + archive.getFtpFile());
        }
        // delete temporary file
        if (!archive.getLocalFile().delete()) {
            LOG.warn("Unable to delete temporary file: " + archive.getLocalFile().getAbsolutePath());
        }
    }
