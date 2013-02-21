package EXT.DOMAIN.pharmacy.peps.updater.national.capability.impl;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import EXT.DOMAIN.pharmacy.peps.common.exception.CommonException;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDataVendorVersionVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.FdbVersionVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.SiteUpdateReportVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.SiteVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.VersionCapability;
import EXT.DOMAIN.pharmacy.peps.updater.common.database.SiteUpdate;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DateTimeUtility;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DifUpdater;
import EXT.DOMAIN.pharmacy.peps.updater.national.capability.NationalDifReportCapability;
import EXT.DOMAIN.pharmacy.peps.updater.national.servlet.ReportServlet;
/**
 * Generate FDB-DIF update reports.
 */
public class NationalDifReportCapabilityImpl implements NationalDifReportCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(NationalDifReportCapabilityImpl.class);
    private static final String FDB_CUSTOM_TYPE = "FDB-Custom";
    private static final String FDB_TYPE = "FDB";
    private static final String SESSION_COLUMN = "Session";
    private static final String VERSION_COLUMN = "Version";
    private static final String UPDATE_TYPE_COLUMN = "Update Type";
    private static final String LAST_UPDATE_TIME_COLUMN = "Last Update Time";
    private static final String LAST_UPDATE_STATUS_COLUMN = "Last Update Status";
    private static final String SESSION_DATE_COLUMN = "Session Date";
    private static final String REGIONAL_DATA_CENTER_COLUMN = "Regional Data Center";
    private static final String LOCAL_SITE_COLUMN = "Local Site";
    private static final String FDB_CUSTOM_LAST_UPDATE_TIME_COLUMN = FDB_CUSTOM_TYPE + " " + LAST_UPDATE_TIME_COLUMN;
    private static final String FDB_CUSTOM_LAST_UPDATE_STATUS_COLUMN = FDB_CUSTOM_TYPE + " " + LAST_UPDATE_STATUS_COLUMN;
    private static final String FDB_CUSTOM_SESSION_DATE_COLUMN = FDB_CUSTOM_TYPE + " " + SESSION_DATE_COLUMN;
    private static final String FDB_LAST_UPDATE_TIME_COLUMN = FDB_TYPE + " " + LAST_UPDATE_TIME_COLUMN;
    private static final String FDB_LAST_UPDATE_STATUS_COLUMN = FDB_TYPE + " " + LAST_UPDATE_STATUS_COLUMN;
    private static final String FDB_SESSION_DATE_COLUMN = FDB_TYPE + " " + SESSION_DATE_COLUMN;
    private static final String FDB_CUSTOM_SESSION_LABEL = FDB_CUSTOM_TYPE + " " + SESSION_COLUMN + ": ";
    private static final String FDB_SESSION_LABEL = FDB_TYPE + " " + SESSION_COLUMN + ": ";
    private static final String FDB_VERSION_LABEL = FDB_TYPE + " " + VERSION_COLUMN + ": ";
    private static final DateFormat HTML_UPDATE_TIME_FORMAT = new SimpleDateFormat("MM/dd/yyyy'<br/>'hh:mm a z");
    private static final long WARNING_GRACE_PERIOD = 1000 * 60 * 60 * 36; // 36 hour grace period
    private static final Pattern TABLE_PATTERN = Pattern.compile(".*(<table\\s+class='report'>.*</table>).*",
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private SiteUpdate siteUpdate;
    private VersionCapability versionCapability;
    /**
     * Export site version report.
     * 
     * @param out printer
     * @param filterEnabled true if should filter on abnormal sites
     * @param gracePeriodDisabled true if grace period is disabled
     */
    public void exportSiteVersionReport(PrintWriter out, boolean filterEnabled, boolean gracePeriodDisabled) {
        DrugDataVendorVersionVo fdbDifVersion = versionCapability.retrieveDrugDataVendorVersion();
        // add header
        out.print(FDB_VERSION_LABEL + notNull(fdbDifVersion.getDataVersion()));
        out.print("," + FDB_SESSION_LABEL + notNull(DateTimeUtility.reformatFdbSessionDate(fdbDifVersion.getIssueDate())));
        out.print("," + FDB_CUSTOM_SESSION_LABEL
            + notNull(DateTimeUtility.reformatFdbSessionDate(fdbDifVersion.getCustomIssueDate())));
        out.println();
        out.println();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(2048);
        PrintWriter writer = new PrintWriter(buffer);
        generateSiteVersionReport(writer, filterEnabled, gracePeriodDisabled);
        writer.close();
        renderHtmlReportAsCsvReport(out, buffer.toString());
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
        // add header
        out.print("Site Number: " + site);
        out.print("," + "Date Range: " + (startMonth + 1) + "/" + startYear + " - " + (endMonth + 1) + "/" + endYear);
        out.println();
        out.println();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(2048);
        PrintWriter writer = new PrintWriter(buffer);
        generateSiteHistoryReport(writer, site, limit, startMonth, startYear, endMonth, endYear);
        writer.close();
        renderHtmlReportAsCsvReport(out, buffer.toString());
    }
    /**
     * Generate site version report.
     * 
     * @param out printer
     * @param filterEnabled true if should filter on abnormal sites
     * @param gracePeriodDisabled true if grace period is disabled
     */
    public void generateSiteVersionReport(PrintWriter out, boolean filterEnabled, boolean gracePeriodDisabled) {
        DrugDataVendorVersionVo fdbDifVersion = versionCapability.retrieveDrugDataVendorVersion();
        // Filter Form
        out.println("<table class='filter'><tr>");
        out.println("<td><form name='" + ReportServlet.SITE_VERSION_REPORT + "' action='report' method='get'>");
        out.println("Filter: <input type='checkbox' name='" + ReportServlet.FILTER_PARAMETER + "' value='true'"
            + (filterEnabled ? "checked" : "") + "/> Only Abnormal Sites");
        out.println("&nbsp;<input type='checkbox' name='" + ReportServlet.GRACE_PERIOD_PARAMETER + "' value='true'"
            + (gracePeriodDisabled ? "checked" : "") + "/> No Grace Period");
        out.println("<input type='hidden' name='name' value='" + ReportServlet.SITE_VERSION_REPORT + "'/>");
        out.println("<input type='submit' name='" + ReportServlet.REPORT_ACTION_PARAMETER
            + "' value='Apply' style='margin-left: 8px;'/>");
        out.println("<input type='submit' name='" + ReportServlet.REPORT_ACTION_PARAMETER
            + "' value='Export' style='margin-left: 8px;'/>");
        out.println("</form></td>");
        out.println("<td style='text-align: right; font-size: 90%;'>");
        out.print(FDB_VERSION_LABEL + notNull(fdbDifVersion.getDataVersion()));
        out.print(", " + FDB_SESSION_LABEL + notNull(DateTimeUtility.reformatFdbSessionDate(fdbDifVersion.getIssueDate())));
        out.print(", " + FDB_CUSTOM_SESSION_LABEL
            + notNull(DateTimeUtility.reformatFdbSessionDate(fdbDifVersion.getCustomIssueDate())));
        out.println("</td>");
        out.println("</tr>");
        FdbVersionVo datupFdbVersion = siteUpdate.retrieveLastFdbVersion(false);
        FdbVersionVo datupFdbCustomVersion = siteUpdate.retrieveLastFdbVersion(true);
        if (DifUpdater.isRunning()) {
            out
                .print("<tr><td colspan='2' style='text-align: center; color: red;'>Warning: A National FDB update is in progress.</td></tr>");
        }
        else {
            // print warning for FDB version mismatches
            if (!fdbDifVersion.getIssueDate().equals(datupFdbVersion.getFdbSessionDate())) {
                out
                    .print("<tr><td colspan='2' style='text-align: center; color: red;'>Warning: The FDB session date does not match the latest DATUP FDB session date (");
                out.print(DateTimeUtility.reformatFdbSessionDate(datupFdbVersion.getFdbSessionDate()));
                out.println(").</td></tr>");
            }
            // print warning for FDB-Custom version mismatches
            if (!fdbDifVersion.getCustomIssueDate().equals(datupFdbCustomVersion.getFdbSessionDate())) {
                out
                    .print("<tr><td colspan='2' style='text-align: center; color: red;'>Warning: The FDB-Custom session date does not match the latest DATUP FDB-Custom session date (");
                out.print(DateTimeUtility.reformatFdbSessionDate(datupFdbCustomVersion.getFdbSessionDate()));
                out.println(").</td></tr>");
            }
        }
        out.println("</table>");
        // Table
        out.println("<table class='report'>");
        out.println("<tr class='header'>");
        out.println("<th>" + REGIONAL_DATA_CENTER_COLUMN + "</th>");
        out.println("<th>" + LOCAL_SITE_COLUMN + "</th>");
        out.println("<th>" + FDB_SESSION_DATE_COLUMN + "</th>");
        out.println("<th>" + FDB_LAST_UPDATE_STATUS_COLUMN + "</th>");
        out.println("<th>" + FDB_LAST_UPDATE_TIME_COLUMN + "</th>");
        out.println("<th>" + FDB_CUSTOM_SESSION_DATE_COLUMN + "</th>");
        out.println("<th>" + FDB_CUSTOM_LAST_UPDATE_STATUS_COLUMN + "</th>");
        out.println("<th>" + FDB_CUSTOM_LAST_UPDATE_TIME_COLUMN + "</th>");
        out.println("</tr>");
        boolean isFdbWarningEnabled = gracePeriodDisabled || hasGracePeriodExpired(datupFdbVersion);
        boolean isFdbCustomWarningEnabled = gracePeriodDisabled || hasGracePeriodExpired(datupFdbCustomVersion);
        int row = 1;
        for (SiteUpdateReportVo vo : filterEnabled ? siteUpdate.retrieveAbnormalSiteUpdates(isFdbWarningEnabled,
            datupFdbVersion.getFdbSessionDate(), isFdbCustomWarningEnabled, datupFdbCustomVersion.getFdbSessionDate())
            : siteUpdate.retrieveSiteUpdates()) {
            out.print("<tr class='");
            out.print((row % 2 == 0) ? "plain" : "highlighted");
            out.println("'>");
            // Region Name
            out.print("<td>");
            out.print(notNull(vo.getRegionName()));
            out.println("</td>");
            // Local Site Name
            out.print("<td><a href='report?" + ReportServlet.REPORT_NAME_PARAMETER + "=" + ReportServlet.SITE_HISTORY_REPORT
                + "&" + ReportServlet.SITE_NUMBER_PARAMETER + "=" + vo.getSiteId() + "'>");
            out.print(vo.getSiteId() + " - ");
            out.println(vo.getSiteName());
            out.println("</a></td>");
            // FDB Session Date
            out.print("<td style='text-align: center; white-space: nowrap;'>");
            out.print(notNull(DateTimeUtility.reformatFdbSessionDate(vo.getFdbSessionDate())));
            out.println("</td>");
            // FDB Status
            generateStatus(out, FDB_TYPE, vo.isFdbSuccessful(), vo.getFdbLastUpdateTime(), vo.getFdbSessionDate(), vo
                .getFdbMessage(), datupFdbVersion, isFdbWarningEnabled);
            // FDB Update Time
            out.print("<td style='text-align: center; white-space: nowrap;'>");
            out.print(notNull(DateTimeUtility.toString(vo.getFdbLastUpdateTime(), vo.getFdbLastUpdateTimeZone(),
                HTML_UPDATE_TIME_FORMAT)));
            out.println("</td>");
            // FDB-Custom Session Date
            out.print("<td style='text-align: center; white-space: nowrap;'>");
            out.print(notNull(DateTimeUtility.reformatFdbSessionDate(vo.getFdbCustomSessionDate())));
            out.println("</td>");
            // FDB-Custom Status
            generateStatus(out, FDB_CUSTOM_TYPE, vo.isFdbCustomSuccessful(), vo.getFdbCustomLastUpdateTime(), vo
                .getFdbCustomSessionDate(), vo.getFdbCustomMessage(), datupFdbCustomVersion, isFdbCustomWarningEnabled);
            // FDB-Custom Update Time
            out.print("<td style='text-align: center; white-space: nowrap;'>");
            out.print(notNull(DateTimeUtility.toString(vo.getFdbCustomLastUpdateTime(), vo.getFdbCustomLastUpdateTimeZone(),
                HTML_UPDATE_TIME_FORMAT)));
            out.println("</td>");
            out.println("</tr>");
            row++;
        }
        out.println("</table>");
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
    public void generateSiteHistoryReport(PrintWriter out, long site, int limit, int startMonth, int startYear,
                                          int endMonth, int endYear) {
        Calendar[] dateRange = siteUpdate.retrieveUpdateDateRange();
        boolean dateRangeIsSameYear = dateRange[0].get(Calendar.YEAR) == dateRange[1].get(Calendar.YEAR);
        // check start year
        if ((startYear < dateRange[0].get(Calendar.YEAR)) || (startYear > dateRange[1].get(Calendar.YEAR))) {
            startYear = dateRange[0].get(Calendar.YEAR);
        }
        // check end year
        if ((endYear < dateRange[0].get(Calendar.YEAR)) || (endYear > dateRange[1].get(Calendar.YEAR))) {
            endYear = dateRange[1].get(Calendar.YEAR);
        }
        // adjust start year if greater than end year
        if (startYear > endYear) {
            startYear = endYear;
        }
        // check start month
        if ((startMonth < 0) || (startMonth > 11)
            || (startYear == dateRange[0].get(Calendar.YEAR) && startMonth < dateRange[0].get(Calendar.MONTH))) {
            startMonth = dateRange[0].get(Calendar.MONTH);
        }
        // check end month
        if ((endMonth < 0) || (endMonth > 11)
            || (endYear == dateRange[1].get(Calendar.YEAR) && endMonth > dateRange[1].get(Calendar.MONTH))) {
            endMonth = dateRange[1].get(Calendar.MONTH);
        }
        // adjust end month if less than the beginning of the date range
        if (endYear == dateRange[0].get(Calendar.YEAR) && endMonth < dateRange[0].get(Calendar.MONTH)) {
            endMonth = dateRange[0].get(Calendar.MONTH);
        }
        // adjust start month if greater than end month
        if (startYear == endYear && startMonth > endMonth) {
            startMonth = endMonth;
        }
        List<SiteVo> siteList = siteUpdate.retrieveSiteList();
        // select first site in list
        if (site < 0) {
            site = siteList.get(0).getId();
        }
        // Filter Form
        out.println("<table class='filter'><tr>");
        out.println("<td><form name='" + ReportServlet.SITE_VERSION_REPORT + "' action='report' method='get'>");
        out.println("Site: <select name='" + ReportServlet.SITE_NUMBER_PARAMETER + "'/>");
        for (SiteVo siteVo : siteList) {
            out.print("<option value='");
            out.print(siteVo.getId());
            out.print("'");
            if (site == siteVo.getId()) {
                out.print("selected");
            }
            out.print(">");
            out.print(siteVo.getId() + " - " + siteVo.getShortName());
            out.println("</option>");
        }
        out.println("</select>");
        out.println("&nbsp;Date Range: ");
        generateMonthDropdown(out, ReportServlet.START_MONTH_PARAMETER, dateRangeIsSameYear ? dateRange[0]
            .get(Calendar.MONTH) : Calendar.JANUARY, dateRangeIsSameYear ? dateRange[1].get(Calendar.MONTH)
            : Calendar.DECEMBER, startMonth);
        out.print(" / ");
        generateYearDropdown(out, ReportServlet.START_YEAR_PARAMETER, dateRange[0].get(Calendar.YEAR), dateRange[1]
            .get(Calendar.YEAR), startYear);
        out.print(" - ");
        generateMonthDropdown(out, ReportServlet.END_MONTH_PARAMETER, dateRangeIsSameYear ? dateRange[0].get(Calendar.MONTH)
            : Calendar.JANUARY, dateRangeIsSameYear ? dateRange[1].get(Calendar.MONTH) : Calendar.DECEMBER, endMonth);
        out.print(" / ");
        generateYearDropdown(out, ReportServlet.END_YEAR_PARAMETER, dateRange[0].get(Calendar.YEAR), dateRange[1]
            .get(Calendar.YEAR), endYear);
        out.println("&nbsp;History Limit: <input type='text' name='" + ReportServlet.LIMIT_PARAMETER
            + "' maxlength='6' size='6' value='" + limit + "'/>");
        out.println("<input type='hidden' name='name' value='" + ReportServlet.SITE_HISTORY_REPORT + "'/>");
        out.println("<input type='submit' name='" + ReportServlet.REPORT_ACTION_PARAMETER
            + "' value='Apply' style='margin-left: 8px;'/>");
        out.println("<input type='submit' name='" + ReportServlet.REPORT_ACTION_PARAMETER
            + "' value='Export' style='margin-left: 8px;'/>");
        out.println("</form></td>");
        out.println("</tr>");
        out.println("</table>");
        // Table
        out.println("<table class='report'>");
        out.println("<tr class='header'>");
        out.println("<th>" + UPDATE_TYPE_COLUMN + "</th>");
        out.println("<th>" + REGIONAL_DATA_CENTER_COLUMN + "</th>");
        out.println("<th>" + SESSION_DATE_COLUMN + "</th>");
        out.println("<th>" + LAST_UPDATE_STATUS_COLUMN + "</th>");
        out.println("<th>" + LAST_UPDATE_TIME_COLUMN + "</th>");
        out.println("</tr>");
        int row = 1;
        for (SiteUpdateReportVo vo : siteUpdate.retrieveSiteHistory(site, limit, startMonth, startYear, endMonth, endYear)) {
            boolean isFdbType = (vo.getFdbId() != null);
            out.print("<tr class='");
            out.print((row % 2 == 0) ? "plain" : "highlighted");
            out.println("'>");
            // Type
            out.print("<td>");
            out.print(isFdbType ? FDB_TYPE : FDB_CUSTOM_TYPE);
            out.println("</td>");
            // Region Name
            out.print("<td>");
            out.print(notNull(vo.getRegionName()));
            out.println("</td>");
            if (isFdbType) {
                // FDB Session Date
                out.print("<td style='text-align: center; white-space: nowrap;'>");
                out.print(notNull(DateTimeUtility.reformatFdbSessionDate(vo.getFdbSessionDate())));
                out.println("</td>");
                // FDB Status
                generateStatus(out, FDB_TYPE, vo.isFdbSuccessful(), vo.getFdbLastUpdateTime(), vo.getFdbSessionDate(), vo
                    .getFdbMessage(), null, false);
                // FDB Update Time
                out.print("<td style='text-align: center; white-space: nowrap;'>");
                out.print(notNull(DateTimeUtility.toString(vo.getFdbLastUpdateTime(), vo.getFdbLastUpdateTimeZone(),
                    HTML_UPDATE_TIME_FORMAT)));
                out.println("</td>");
            }
            else {
                // FDB-Custom Session Date
                out.print("<td style='text-align: center; white-space: nowrap;'>");
                out.print(notNull(DateTimeUtility.reformatFdbSessionDate(vo.getFdbCustomSessionDate())));
                out.println("</td>");
                // FDB-Custom Status
                generateStatus(out, FDB_CUSTOM_TYPE, vo.isFdbCustomSuccessful(), vo.getFdbCustomLastUpdateTime(), vo
                    .getFdbCustomSessionDate(), vo.getFdbCustomMessage(), null, false);
                // FDB-Custom Update Time
                out.print("<td style='text-align: center; white-space: nowrap;'>");
                out.print(notNull(DateTimeUtility.toString(vo.getFdbCustomLastUpdateTime(), vo
                    .getFdbCustomLastUpdateTimeZone(), HTML_UPDATE_TIME_FORMAT)));
                out.println("</td>");
            }
            out.println("</tr>");
            row++;
        }
        out.println("</table>");
    }
    /**
     * Database capability.
     * 
     * @param capability database capability
     */
    public void setSiteUpdate(SiteUpdate capability) {
        this.siteUpdate = capability;
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
     * Check if the FDB warning grace period has expired.
     * 
     * @param fdbVersion FDB version
     * @return true if grace period has expired
     */
    private boolean hasGracePeriodExpired(FdbVersionVo fdbVersion) {
        return new Date().after(new Date(fdbVersion.getWhen().getTime() + WARNING_GRACE_PERIOD));
    }
    /**
     * Pretty print null value.
     * 
     * @param s string to test
     * @return string value if not null, '?' otherwise
     */
    private String notNull(String s) {
        if (s == null || "".equals(s)) {
            return "?";
        }
        return s;
    }
    /**
     * Generate status message.
     * 
     * @param html, should output html
     * @param out printer
     * @param name status name
     * @param successful is successful update
     * @param lastUpdate last update time
     * @param sessionDate FDB version number
     * @param failureMessage failure message
     * @param baselineSessionDate baseline session dates
     */
    private void generateStatus(PrintWriter out, String name, boolean successful, Date lastUpdate, String sessionDate,
                                String failureMessage, FdbVersionVo latestFdbVersion, boolean warningEnabled) {
        out.println("<td><table class='status'><tr>");
        if (successful) {
            if (warningEnabled && !latestFdbVersion.getFdbSessionDate().equals(sessionDate)) {
                out.print("<td><img src='image/warning.png' class='status'/></td><td>");
                out.print(name + " update ");
                out.print(DateTimeUtility.reformatFdbSessionDate(sessionDate));
                out.print(" SUCCEDDED.");
                out.println("</td></tr><tr><td></td><td>");
                out.print("Warning: Session date is OUT-OF-SYNC with National.");
                out.print("</td>");
            }
            else {
                out.print("<td><img src='image/normal.png' class='status'/></td><td>");
                out.print(name + " update ");
                out.print(DateTimeUtility.reformatFdbSessionDate(sessionDate));
                out.print(" SUCCEEDED.");
                out.print("</td>");
            }
        }
        else if (!successful && (lastUpdate != null)) {
            out.print("<td><img src='image/abnormal.png' class='status'/></td><td>");
            out.print(name + " update ");
            out.print(DateTimeUtility.reformatFdbSessionDate(sessionDate));
            out.print(" FAILED.");
            out.println("</td></tr><tr><td></td><td>");
            out.print("Reason: ");
            out.print(failureMessage);
            out.print("</td>");
        }
        else {
            out.print("<td><img src='image/unknown.png' class='status'/></td><td>");
            out.print(name + " status UNKNOWN.");
            out.print("</td>");
        }
        out.println("</tr></table></td>");
    }
    /**
     * Generate month dropdown box.
     * 
     * @param out printer
     * @param name name
     * @param startMonth start month
     * @param stopMonth stop month
     * @param selectedMonth selected month
     */
    private void generateMonthDropdown(PrintWriter out, String name, int startMonth, int stopMonth, int selectedMonth) {
        out.print("<select name='");
        out.print(name);
        out.println("'>");
        for (int i = startMonth; i >= 0 && i < DateTimeUtility.MONTHS_IN_YEAR.length && i <= stopMonth; i++) {
            out.print("<option value='");
            out.print(DateTimeUtility.MONTHS_IN_YEAR[i].getKey());
            out.print("'");
            if (i == selectedMonth) {
                out.print("selected");
            }
            out.print(">");
            out.print(DateTimeUtility.MONTHS_IN_YEAR[i].getName());
            out.println("</option>");
        }
        out.println("</select>");
    }
    /**
     * Generate year dropdown box.
     * 
     * @param out printer
     * @param name name
     * @param startYear start year
     * @param stopYear end year
     * @param selectedYear selected year
     */
    private void generateYearDropdown(PrintWriter out, String name, int startYear, int stopYear, int selectedYear) {
        out.print("<select name='");
        out.print(name);
        out.println("'>");
        for (int i = startYear; i <= stopYear; i++) {
            out.print("<option value='");
            out.print(i);
            out.print("'");
            if (i == selectedYear) {
                out.print("selected");
            }
            out.print(">");
            out.print(i);
            out.println("</option>");
        }
        out.println("</select>");
    }
    /**
     * Re-render an HTML report as a CSV report. Basically, strip the HTML tags.
     * 
     * @param out CSV stream
     * @param htmlReport HTML report
     */
    private void renderHtmlReportAsCsvReport(PrintWriter out, String htmlReport) {
        Matcher matcher = TABLE_PATTERN.matcher(htmlReport);
        if (!matcher.matches()) {
            LOG.error("Unable to convert HTML report to CSV, pattern did not match: " + htmlReport);
            throw new CommonException(CommonException.RESOURCE_UNAVAILABLE, "CSV Report");
        }
        // remove newlines
        String csvReport = matcher.group(1).replaceAll("(\\r\\n)|(\\n)", "");
        // remove images
        csvReport = csvReport.replaceAll("<img\\s*[^>]*>\\s*</td>", "");
        // replace alignment rows
        csvReport = csvReport.replaceAll("</td>\\s*</tr>\\s*<tr>\\s*<td>\\s*</td>\\s*<td>", " ");
        // remove inner tables
        csvReport = csvReport.replaceAll("(</td>)?\\s*</tr>\\s*</table>", "");
        // replace column delimiter
        csvReport = csvReport.replaceAll("(</th>)|(</td>)", ",");
        // replace row delimiter
        csvReport = csvReport.replaceAll("</tr>", "\n");
        // replace breaks with spaces
        csvReport = csvReport.replaceAll("<br/?>", " ");
        // remove all remaining HTML tags
        csvReport = csvReport.replaceAll("(</?\\w+\\s*[^>]*>)+", "");
        out.print(csvReport);
    }
