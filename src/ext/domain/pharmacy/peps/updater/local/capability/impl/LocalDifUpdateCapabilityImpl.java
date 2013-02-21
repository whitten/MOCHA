/**
 * Copyright 2008, Southwest Research Institute
 * 
 */
package EXT.DOMAIN.pharmacy.peps.updater.local.capability.impl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceValidationException;
import EXT.DOMAIN.pharmacy.peps.common.exception.PharmacyRuntimeException;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDataVendorVersionVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.SiteUpdateVo;
import EXT.DOMAIN.pharmacy.peps.updater.common.capability.impl.DifUpdateCapabilityImpl;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.Configuration;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DateTimeUtility;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.EmailUtility;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpClient;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.MailingListType;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FdbZipFileReader.FdbHeader;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FtpClient.FtpPath;
import EXT.DOMAIN.pharmacy.peps.updater.local.capability.LocalDifUpdateCapability;
import EXT.DOMAIN.pharmacy.peps.updater.local.messaging.SendToNationalCapability;
/**
 * 
 * Business logic for processing DIF updates at Local.
 */
public class LocalDifUpdateCapabilityImpl extends DifUpdateCapabilityImpl implements LocalDifUpdateCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LocalDifUpdateCapabilityImpl.class);
    private SendToNationalCapability sendToNationalCapability;
    /**
     * Execute the Local update.
     */
    @Override
    public boolean execute() throws IOException {
        DrugDataVendorVersionVo fdbVersion = versionCapability.retrieveDrugDataVendorVersion();
        if (!verifyFdbDifStatus(fdbVersion)) {
            Map<String, String> emailValues = buildEmailTemplateValues(fdbVersion);
            emailValues.put(EmailUtility.REASON_KEY, "Local FDB-DIF is not available.");
            notifyManagers(false, emailValues);
            throw new InterfaceException(InterfaceException.INTERFACE_ERROR, InterfaceException.DRUG_DATA_VENDOR);
        }
        // execute the verification tests to check for existing errors from last run
        if (!verificationTester.executeOrderCheckTests()) {
            LOG.error("Unable to verify the last FDB-DIF update");
            Map<String, String> emailValues = buildEmailTemplateValues(new FdbHeader(true, false, fdbVersion
                .getDataVersion(), fdbVersion.getIssueDate(), fdbVersion.getIssueDate(), ""));
            emailValues.put(EmailUtility.REASON_KEY, "One or more FDB-DIF verification tests failed.");
            notifyManagers(false, emailValues);
            throw new InterfaceValidationException(InterfaceValidationException.FDB_VERIFICATION, DateTimeUtility
                .reformatFdbSessionDate(fdbVersion.getIssueDate()));
        }
        FtpClient ftp = ftpFactory.createConnection(true);
        FtpPath datupBasePath = new FtpPath(new String[] {config.load().getString(Configuration.FTP_WORKING_DIRECTORY),
                                                          FtpClient.DATUP_DIRECTORY});
        List<FdbArchive> archives = Collections.emptyList();
        try {
            synchronized (ftp) {
                // retrieve incremental and custom archives
                archives = retrieveFdbArchive(ftp,
                    new FtpPath(datupBasePath, new String[] {FtpClient.INCREMENTAL_DIRECTORY}), false);
                archives.addAll(retrieveFdbArchive(ftp,
                    new FtpPath(datupBasePath, new String[] {FtpClient.CUSTOM_DIRECTORY}), false));
                // remove old archives (first pass)
                removeOlderThan(ftp, archives, fdbVersion.getIssueDate(), fdbVersion.getCustomIssueDate(), false);
                // retrieve actual file headers
                for (FdbArchive archive : archives) {
                    archive.refresh(ftp);
                }
                // remove old archives (second pass)
                removeOlderThan(ftp, archives, fdbVersion.getIssueDate(), fdbVersion.getCustomIssueDate(), false);
            }
        }
        finally {
            synchronized (ftp) {
                ftp.logout();
                ftp.disconnect();
            }
        }
        if (archives.isEmpty()) {
            LOG.debug("No valid FDB-DIF incremental updates detected on FTP server in the '"
                + FtpClient.INCREMENTAL_DIRECTORY + " | " + FtpClient.CUSTOM_DIRECTORY + "' directories");
            return false;
        }
        LOG.debug("Processing archives: " + archives);
        processIncrementalDatabase(archives);
        return true;
    }
    /**
     * Capability.
     * 
     * @param capability capability
     */
    public void setSendToNationalCapability(SendToNationalCapability capability) {
        this.sendToNationalCapability = capability;
    }
    /**
     * Build FDB version email template values.
     * 
     * @param fdbVersion FDB version
     * @return template values
     */
    @Override
    public Map<String, String> buildEmailTemplateValues(DrugDataVendorVersionVo fdbVersion) {
        Map<String, String> emailValues = super.buildEmailTemplateValues(fdbVersion);
        addSiteList(emailValues);
        return emailValues;
    }
    /**
     * Build email template values.
     * 
     * @param header FDB header
     * @return template values
     */
    @Override
    public Map<String, String> buildEmailTemplateValues(FdbHeader header) {
        Map<String, String> emailValues = super.buildEmailTemplateValues(header);
        addSiteList(emailValues);
        return emailValues;
    }
    /**
     * Add the list of the sites.
     * 
     * @param emailValues email map
     */
    private void addSiteList(Map<String, String> emailValues) {
        String[] sites = config.load().getStringArray(Configuration.SITE_NUMBER);
        if ((sites != null) && (sites.length > 0)) {
            StringBuilder siteList = new StringBuilder();
            for (int i = 0; i < sites.length; i++) {
                siteList.append(sites[i]);
                if ((i + 1) < sites.length) {
                    siteList.append(", ");
                }
            }
            emailValues.put(EmailUtility.SITE_KEY, siteList.toString());
        }
    }
    /**
     * Process incremental FDB and FDB-Custom updates.
     * 
     * @param ftp client connection
     * @param archives unsorted list of archives
     * @throws FileNotFoundException file doesn't exist
     * @throws IOException FTP error occurs
     */
    private void processIncrementalDatabase(List<FdbArchive> archives) throws FileNotFoundException, IOException {
        // sort incremental updates by session date (oldest first)
        sortByDependencyOrder(archives);
        LOG.debug("Processing incremental updates (in order): " + archives);
        for (FdbArchive archive : archives) {
            Map<String, String> emailValues = buildEmailTemplateValues(archive.getHeader());
            try {
                // perform the DIF update
                difUpdater.runUpdater(archive.getLocalFile());
            }
            catch (PharmacyRuntimeException e) {
                LOG.error("DIF updater failed. Unable to process FDB-DIF update: " + archive, e);
                emailValues.put(EmailUtility.REASON_KEY, "FDB-DIF Updater tool failed.");
                notifyNational(false, archive, emailValues);
                notifyManagers(false, emailValues);
                // stop processing
                throw e;
            }
            // execute the verification tests
            if (!verificationTester.executeOrderCheckTests()) {
                LOG.error("Verification tests failed. Unable to process FDB-DIF update: " + archive);
                emailValues.put(EmailUtility.REASON_KEY, "One or more FDB-DIF verification tests failed.");
                notifyNational(false, archive, emailValues);
                notifyManagers(false, emailValues);
                throw new InterfaceValidationException(InterfaceValidationException.FDB_VERIFICATION, DateTimeUtility
                    .reformatFdbSessionDate(archive.getHeader().getNewSessionDate()));
            }
            notifyNational(true, archive, emailValues);
            notifyManagers(true, emailValues);
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
            // notify local managers of successful update
            if (!emailUtility.sendEmail(MailingListType.LOCAL_SUCCESS, emailValues)) {
                LOG.error("Unable to send Local success email");
            }
        }
        else {
            if (!emailUtility.sendEmail(MailingListType.LOCAL_FAILURE, emailValues)) {
                LOG.error("Unable to send Local failure email");
            }
        }
    }
    /**
     * Notify national about this site update.
     * 
     * @param success true is successful
     * @param archive archive
     * @param emailValues current email template values
     */
    private void notifyNational(boolean success, FdbArchive archive, Map<String, String> emailValues) {
        try {
            SiteUpdateVo siteUpdate = new SiteUpdateVo();
            siteUpdate.setSiteId(config.load().getStringArray(Configuration.SITE_NUMBER));
            siteUpdate.setRegionName(config.load().getString(Configuration.RDC_NAME));
            siteUpdate.setIsSuccessful(success);
            siteUpdate.setIsCustom(archive.getHeader().isCustom());
            siteUpdate.setFdbDatabaseVersion(archive.getHeader().getDatabaseVersion());
            siteUpdate.setFdbSessionDate(archive.getHeader().getNewSessionDate());
            siteUpdate.setLastUpdateTime(new Date(System.currentTimeMillis()));
            siteUpdate.setLastUpdateTimeZone(TimeZone.getDefault());
            siteUpdate.setMessage(success ? "Update successful." : emailValues.get(EmailUtility.REASON_KEY));
            sendToNationalCapability.send(siteUpdate);
        }
        catch (InterfaceException e) {
            LOG.error("Failed to enqueue JMS message for update: " + archive, e);
            StringBuilder chainedReason = new StringBuilder("The FDB-DIF update ");
            if (success) {
                chainedReason.append("succeeded. However,");
            }
            else {
                chainedReason.append("failed because: ").append(emailValues.get(EmailUtility.REASON_KEY)).append(
                    " In addition,");
            }
            chainedReason.append(" the system failed to notify National due to a JMS failure.");
            emailValues.put(EmailUtility.REASON_KEY, chainedReason.toString());
            notifyManagers(false, emailValues);
            // stop processing
            throw e;
        }
    }
