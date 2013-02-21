/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.national.session.bean;
import java.io.PrintWriter;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import EXT.DOMAIN.pharmacy.peps.updater.national.session.DifReportService;
/**
 * Retrieve the dose routes and dose types for a given list of GCN sequence numbers.
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class DifReportServiceBean implements DifReportService {
    @Autowired
    private DifReportService difReportService;
    /**
     * Export site version report.
     * 
     * @param out printer
     * @param filterEnabled true if should filter on abnormal sites
     * @param gracePeriodDisabled true if grace period is disabled
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void exportSiteVersionReport(PrintWriter out, boolean filterEnabled, boolean gracePeriodDisabled) {
        difReportService.exportSiteVersionReport(out, filterEnabled, gracePeriodDisabled);
    }
    /**
     * Generate site version report.
     * 
     * @param out printer
     * @param filterEnabled true if should filter on abnormal sites
     * @param gracePeriodDisabled true if grace period is disabled
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void generateSiteVersionReport(PrintWriter out, boolean filterEnabled, boolean gracePeriodDisabled) {
        difReportService.generateSiteVersionReport(out, filterEnabled, gracePeriodDisabled);
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
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void generateSiteHistoryReport(PrintWriter out, long site, int limit, int startMonth, int startYear, int endMonth,
                                          int endYear) {
        difReportService.generateSiteHistoryReport(out, site, limit, startMonth, startYear, endMonth, endYear);
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
        difReportService.exportSiteHistoryReport(out, site, limit, startMonth, startYear, endMonth, endYear);
    }
    /**
     * 
     * @param drugInfoService drugInfoService property
     */
    public void setDifReportService(DifReportService service) {
        this.difReportService = service;
    }
