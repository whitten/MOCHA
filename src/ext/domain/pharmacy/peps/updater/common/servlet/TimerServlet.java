/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.common.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import commonj.timers.TimerManager;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import EXT.DOMAIN.pharmacy.peps.updater.common.capability.DifUpdateCapability;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.Configuration;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.TimerFactory;
/**
 * Timer servlet for locating/configuring the Timer Manager.
 */
public class TimerServlet extends HttpServlet {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(TimerServlet.class);
    private static final String JNDI_NAME = "jndiName";
    /**
     * Lookup and launch the timer.
     * 
     * @param config servlet configuration
     * @throws ServletException shouldn't occur
     * 
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String jndiName = config.getInitParameter(JNDI_NAME);
        if ((jndiName == null) || "".equals(jndiName)) {
            LOG.error("Please specify a '" + JNDI_NAME + "' parameter to lookup the timer manager.");
            return;
        }
        try {
            TimerFactory.instance().setManager((TimerManager) (new InitialContext()).lookup(jndiName));
            // launch timer indirectly
            Configuration datupConfig = (Configuration) ContextSingletonBeanFactoryLocator.getInstance(
                "classpath*:beanRefContext.xml").useBeanFactory("businessBeanFactory").getFactory().getBean(
                "datupConfiguration");
            LOG.debug(datupConfig);
        }
        catch (Throwable t) {
            LOG.error("Unable to lookup the Timer Manager: " + jndiName, t);
        }
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
        /* @todo (mspears:Mar 29, 2010) testing purposes only, should be disabled in production */
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if (request.getParameter("lock") != null) {
            // DifUpdater.RUNCOUNTER = 1;
            // out.println("Order checks are locked out.");
        }
        else if (request.getParameter("unlock") != null) {
            // DifUpdater.RUNCOUNTER = 0;
            // out.println("Order checks are available.");
        }
        else {
            String runCommand = request.getParameter("run");
            if (runCommand == null) {
                out.println("<p>Manually <a href='timer?run'><b>execute</b></a> the FDB-DIF update process.</p>");
                out.println("<p>*This trigger is provided for testing purposes only.</p>");
            }
            else {
                try {
                    DifUpdateCapability capability = (DifUpdateCapability) ContextSingletonBeanFactoryLocator.getInstance(
                        "classpath*:beanRefContext.xml").useBeanFactory("businessBeanFactory").getFactory().getBean(
                        "difUpdateCapability");
                    if (capability.execute()) {
                        out.println("<font color='blue'><h2>Updater ran successfully.</h2></font>");
                        out.println("See log for more information.");
                    }
                    else {
                        out
                            .println("<font color='orange'><h2>Updater ran successfully but no updates were processed. Should there have been?</h2></font>");
                        out.println("See log for more information.");
                    }
                }
                catch (Throwable t) {
                    out.println("<font color='red'><h2>Updater run failed!</h2></font>");
                    out.println("See log for more information.");
                    out.println("<pre>");
                    t.printStackTrace(out);
                    out.println("</pre>");
                }
            }
        }
        out.flush();
        out.close();
    }
