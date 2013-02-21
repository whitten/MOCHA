/**
 * Copyright 2008, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.common.utility.test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.updater.common.servlet.TimerServlet;
import EXT.DOMAIN.pharmacy.peps.updater.national.servlet.ReportServlet;
import EXT.DOMAIN.pharmacy.peps.updater.national.session.DifReportService;
/**
 * Start Jetty, setting the port, context, web app directory, and web xml file. This allows us to run PRE V. 1.0 without
 * deploying to WebLogic.
 */
public class LaunchNationalJetty {
    private static final String PRE_CONTEXT_PATH = "/PRE";
    private static final String PRE_WAR = "./web";
    /**
     * Run PRE V. 1.0 in Jetty. Override the EvironmentUtility's set environment depending on what web.xml file is used
     * (local or national).
     * 
     * This method expects four command line arguments in the order and type specified. No verification that the arguments
     * are valid is done. If improper arguments are set, expect NullPointerExceptions, IOExceptions, String parsing
     * exceptions, or that the server started simply does not operate as expected.
     * 
     * @param args [0] (int) port, [1] (String) PRE web.xml file path, [2] (String) FDB Images web application directory
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server(Integer.parseInt(args[0]));
        ApplicationContext context = new ClassPathXmlApplicationContext(
            "xml/spring/datup/national/test/InterfaceContext.xml");
        DifReportService reportService = (DifReportService) context.getBean("difReportService");
        WebAppContext pre = new WebAppContext();
        pre.setContextPath(PRE_CONTEXT_PATH);
        pre.setWar(PRE_WAR);
        // pre.setOverrideDescriptor(args[1]);
        ReportServlet reportServlet = new ReportServlet();
        reportServlet.setDifReportService(reportService);
        pre.addServlet(new ServletHolder(reportServlet), "/datup");
        TimerServlet timerServlet = new TimerServlet();
        pre.addServlet(new ServletHolder(timerServlet), "/datup/timer");
        server.addHandler(pre);
        server.start();
        System.out.println("Jetty server started on port " + args[0]);
    }
