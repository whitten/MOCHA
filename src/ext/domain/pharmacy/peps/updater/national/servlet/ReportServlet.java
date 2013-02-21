/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.national.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import EXT.DOMAIN.pharmacy.peps.updater.national.session.DifReportService;
/**
 * Receive a request from VistA, retrieve the XML request parameter, and forward it on to a subclass. Then write the String
 * return value to the response.
 */
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ReportServlet.class);
    public static final String SITE_VERSION_REPORT = "SiteVersionReport";
    public static final String SITE_HISTORY_REPORT = "SiteHistoryReport";
    public static final String REPORT_NAME_PARAMETER = "name";
    public static final String REPORT_ACTION_PARAMETER = "action";
    public static final String REPORT_ACTION_EXPORT = "export";
    public static final String FILTER_PARAMETER = "filter";
    public static final String GRACE_PERIOD_PARAMETER = "nograce";
    public static final String SITE_NUMBER_PARAMETER = "site";
    public static final String LIMIT_PARAMETER = "limit";
    public static final int LIMIT_PARAMETER_DEFAULT = 100;
    public static final String START_MONTH_PARAMETER = "smonth";
    public static final String START_YEAR_PARAMETER = "syear";
    public static final String END_MONTH_PARAMETER = "emonth";
    public static final String END_YEAR_PARAMETER = "eyear";
    private static final String HTML_CONTENT_TYPE = "text/html";
    private static final String CSV_CONTENT_TYPE = "text/x-csv";
    @EJB
    private DifReportService difReportService;
    /**
     * Delegate DELETE requests to {@link #doService(ServletRequest, ServletResponse)}.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if error
     * @throws IOException if error
     * 
     * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        doService(request, response);
    }
    /**
     * Delegate GET requests to {@link #doService(ServletRequest, ServletResponse)}.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if error
     * @throws IOException if error
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        doService(request, response);
    }
    /**
     * Delegate POST requests to {@link #doService(ServletRequest, ServletResponse)}.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if error
     * @throws IOException if error
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        doService(request, response);
    }
    /**
     * Delegate PUT requests to {@link #doService(ServletRequest, ServletResponse)}.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if error
     * @throws IOException if error
     * 
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        doService(request, response);
    }
    /**
     * Get the XML request parameter from the HttpServletRequest and forward it to the subclass via the
     * {@link #getResponse(String)} method.
     * 
     * Then write the String return value to the response.
     * 
     * @param request HttpServletRequest from VistA
     * @param response HttpServletResponse with XML response for VistA
     * @throws IOException If error processing request or writing response
     * 
     * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    private void doService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String reportName = request.getParameter(REPORT_NAME_PARAMETER);
        boolean export = REPORT_ACTION_EXPORT.equalsIgnoreCase(request.getParameter(REPORT_ACTION_PARAMETER));
        // set headers
        if (export) {
            response.setContentType(CSV_CONTENT_TYPE);
            response.setHeader("content-disposition", "attachment; filename=" + reportName + ".csv");
        }
        else {
            response.setContentType(HTML_CONTENT_TYPE);
            out
                .println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
            out.println("<html lang=\"en-US\" xml:lang=\"en-US\" xmlns=\"http://www.w3.org/1999/xhtml\">");
            out.println("<head>");
            out.print("<title>");
            out.print("DATUP Report - " + reportName);
            out.println("</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/report.css\" />");
            out.println("</head>");
            out.println("<body>");
        }
        // generate report
        if (SITE_VERSION_REPORT.equalsIgnoreCase(reportName)) {
            if (export) {
                difReportService.exportSiteVersionReport(out, "true"
                    .equalsIgnoreCase(request.getParameter(FILTER_PARAMETER)), "true".equalsIgnoreCase(request
                    .getParameter(GRACE_PERIOD_PARAMETER)));
            }
            else {
                difReportService.generateSiteVersionReport(out, "true".equalsIgnoreCase(request
                    .getParameter(FILTER_PARAMETER)), "true".equalsIgnoreCase(request.getParameter(GRACE_PERIOD_PARAMETER)));
            }
        }
        else if (SITE_HISTORY_REPORT.equalsIgnoreCase(reportName)) {
            if (export) {
                difReportService.exportSiteHistoryReport(out, parseIntParameter(request, SITE_NUMBER_PARAMETER, -1),
                    parseIntParameter(request, LIMIT_PARAMETER, LIMIT_PARAMETER_DEFAULT), parseIntParameter(request,
                        START_MONTH_PARAMETER, -1), parseIntParameter(request, START_YEAR_PARAMETER, -1), parseIntParameter(
                        request, END_MONTH_PARAMETER, -1), parseIntParameter(request, END_YEAR_PARAMETER, -1));
            }
            else {
                difReportService.generateSiteHistoryReport(out, parseIntParameter(request, SITE_NUMBER_PARAMETER, -1),
                    parseIntParameter(request, LIMIT_PARAMETER, LIMIT_PARAMETER_DEFAULT), parseIntParameter(request,
                        START_MONTH_PARAMETER, -1), parseIntParameter(request, START_YEAR_PARAMETER, -1), parseIntParameter(
                        request, END_MONTH_PARAMETER, -1), parseIntParameter(request, END_YEAR_PARAMETER, -1));
            }
        }
        else {
            out.println("Unknown report: " + reportName);
        }
        if (!export) {
            out.println("</body>");
            out.println("</html>");
        }
        out.flush();
        out.close();
    }
    /**
     * Set report service.
     * 
     * @param service report service
     */
    public void setDifReportService(DifReportService service) {
        this.difReportService = service;
    }
    /**
     * Parse integer parameter.
     * 
     * @param request request
     * @param parameter parameter name
     * @param defaultValue returned if the integer is not valid
     * @return integer value
     */
    private int parseIntParameter(ServletRequest request, String parameter, int defaultValue) {
        try {
            return Integer.parseInt(request.getParameter(parameter));
        }
        catch (NumberFormatException e) {
            LOG.trace("Unable to parse integer parameter: " + parameter, e);
        }
        return defaultValue;
    }
