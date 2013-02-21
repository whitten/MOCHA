package EXT.DOMAIN.pharmacy.peps.updater.national.session.impl;
import java.io.PrintWriter;
import EXT.DOMAIN.pharmacy.peps.updater.national.capability.NationalDifReportCapability;
import EXT.DOMAIN.pharmacy.peps.updater.national.session.DifReportService;
/**
 * National FDB-DIF reports.
 */
public class DifReportServiceImpl implements DifReportService {
    private NationalDifReportCapability difReportCapability;
    /**
     * Export site version report.
     * 
     * @param out printer
     * @param filterEnabled true if should filter on abnormal sites
     * @param gracePeriodDisabled true if grace period is disabled
     */
    public void exportSiteVersionReport(PrintWriter out, boolean filterEnabled, boolean gracePeriodDisabled) {
        difReportCapability.exportSiteVersionReport(out, filterEnabled, gracePeriodDisabled);
    }
    /**
     * Generate Site Version report.
     * 
     * @param out printer
     * @param filterEnabled true if should filter on abnormal sites
     * @param gracePeriodDisabled true if grace period is disabled
     * 
     * @see EXT.DOMAIN.pharmacy.peps.updater.national.session.DifReportService#generateSiteVersionReport(java.io.PrintWriter)
     */
    public void generateSiteVersionReport(PrintWriter out, boolean filterEnabled, boolean gracePeriodDisabled) {
        difReportCapability.generateSiteVersionReport(out, filterEnabled, gracePeriodDisabled);
    }
    /**
     * Generate site history report.
     * 
     * @param out printer
     * @param site site number
     * @param limit maximum number of updates to display
     * @param startMonth start month
     * @param startYear start year
     * @param endMonth end month
     * @param endYear end year
     */
    public void generateSiteHistoryReport(PrintWriter out, long site, int limit, int startMonth, int startYear, int endMonth,
                                          int endYear) {
        difReportCapability.generateSiteHistoryReport(out, site, limit, startMonth, startYear, endMonth, endYear);
    }
    /**
     * Export site history report.
     * 
     * @param out printer
     * @param site site number
     * @param limit maximum number of updates to display
     * @param startMonth start month
     * @param startYear start year
     * @param endMonth end month
     * @param endYear end year
     */
    public void exportSiteHistoryReport(PrintWriter out, long site, int limit, int startMonth, int startYear, int endMonth,
                                        int endYear) {
        difReportCapability.exportSiteHistoryReport(out, site, limit, startMonth, startYear, endMonth, endYear);
    }
    /**
     * Report capability.
     * 
     * @param capability report capability
     */
    public void setDifReportCapability(NationalDifReportCapability capability) {
        this.difReportCapability = capability;
    }
