package EXT.DOMAIN.pharmacy.peps.updater.national.capability;
import java.io.PrintWriter;
/**
 * Generate FDB-DIF update reports.
 */
public interface NationalDifReportCapability {
    /**
     * Export site version report.
     * 
     * @param out printer
     * @param filterEnabled true if should filter on abnormal sites
     * @param gracePeriodDisabled true if grace period is disabled
     */
    public void exportSiteVersionReport(PrintWriter out, boolean filterEnabled, boolean gracePeriodDisabled);
    /**
     * Generate site version report.
     * 
     * @param out printer
     * @param filterEnabled true if should filter on abnormal sites
     * @param gracePeriodDisabled true if grace period is disabled
     */
    public void generateSiteVersionReport(PrintWriter out, boolean filterEnabled, boolean gracePeriodDisabled);
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
    public void generateSiteHistoryReport(PrintWriter out, long site, int limit, int startMonth, int startYear,
                                          int endMonth, int endYear);
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
                                        int endYear);
