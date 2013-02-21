/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.session.impl;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import org.springframework.jndi.JndiTemplate;
import EXT.DOMAIN.pharmacy.peps.common.exception.FDBUpdateInProgressException;
import EXT.DOMAIN.pharmacy.peps.common.exception.MessageKey;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.ProcessOrderChecksCapability;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.session.OrderCheckService;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.drugcheck.DrugCheckXmlUtility;
/**
 * Perform an order check requested by VistA using XML.
 */
public class OrderCheckServiceImpl implements OrderCheckService {
    private static final Logger LOG = Logger.getLogger(OrderCheckServiceImpl.class);
    private static int RUNCOUNTER;
    private Long lastSynchonizationCheck = System.currentTimeMillis();
    private boolean updateInProgress;
    private ProcessOrderChecksCapability processOrderChecksCapability;
    private JndiTemplate jndiTemplate;
    /**
     * Atomic technique to check if an order check is active.
     * 
     * @return true if active, false otherwise
     */
    public static boolean isRunning() {
        return (RUNCOUNTER > 0);
    }
    /**
     * Perform and order check for the given XML request.
     * 
     * @param request request XML from VistA
     * @return response XML
     */
    public String performOrderCheck(String request) {
        try {
            // check DIF update status every 15 seconds
            if ((System.currentTimeMillis() - lastSynchonizationCheck) >= (1000 * 15)) {
                try {
                    updateInProgress = Boolean.TRUE.equals(jndiTemplate.lookup("DATUP_UPDATE_IN_PROGRESS"));
                }
                catch (NamingException e) {
                    LOG.debug("DATUP does not appear to be installed. Update synchronization check will not be performed.",
                        e);
                }
                lastSynchonizationCheck = System.currentTimeMillis();
            }
            // stop if DIF update is in progress
            if (updateInProgress) {
                throw new FDBUpdateInProgressException(new MessageKey("UPDATE_IN_PROGRESS"));
            }
            try {
                RUNCOUNTER++; // obtain order check lock
                return processOrderChecksCapability.handleRequest(request);
            }
            finally {
                RUNCOUNTER--; // re;ease order check lock
            }
        }
        catch (Throwable t) {
            String xmlError = DrugCheckXmlUtility.createXmlErrorMessage(t);
            LOG.error("Error message sent to VistA:");
            LOG.error(DrugCheckXmlUtility.prettyPrintException(xmlError));
            return xmlError;
        }
    }
    /**
     * 
     * @param processOrderChecksCapability processOrderChecksCapability property
     */
    public void setProcessOrderChecksCapability(ProcessOrderChecksCapability processOrderChecksCapability) {
        this.processOrderChecksCapability = processOrderChecksCapability;
    }
    /**
     * Returns a boolean if any updates are currently running
     * 
     * @return
     * 
     * @see EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.session.OrderCheckService#updatesRunning()
     */
    public boolean updatesRunning() {
        return RUNCOUNTER == 0;
    }
    /**
     * 
     * @param jndiTemplate jndiTemplate property
     */
    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate;
    }
    /**
     * 
     * @return jndiTemplate property
     */
    public JndiTemplate getJndiTemplate() {
        return jndiTemplate;
    }
