/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.impl;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceValidationException;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.PerformDrugChecksCapability;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.VersionCapability;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.ProcessOrderChecksCapability;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.drugcheck.DrugCheckXmlUtility;
/**
 * Parse the XML message and call the drug data vendor to perform the order checks.
 */
public class ProcessOrderChecksCapabilityImpl implements ProcessOrderChecksCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(ProcessOrderChecksCapabilityImpl.class);
    private PerformDrugChecksCapability performDrugChecksCapability;
    private VersionCapability versionCapability;
    /**
     * Empty constructor
     */
    public ProcessOrderChecksCapabilityImpl() {
        super();
    }
    /**
     * Parse the XML message and call the drug data vendor to perform the order checks.
     * 
     * @param xmlRequest Request from VistA
     * @return String XML response
     */
    public String handleRequest(String xmlRequest) {
        LOG.debug("Request received from VistA:");
        LOG.debug(xmlRequest);
        if (xmlRequest == null) {
            throw new InterfaceValidationException(InterfaceValidationException.XML_REQUEST_REQUIRED);
        }
        OrderCheckVo request = DrugCheckXmlUtility.toOrderCheckVo(xmlRequest);
        LOG.debug(request);
        String xmlResponse;
        // perform the order check if there are drugs to screen, even if this is a ping request (just to be nice)
        if (request.getHeader().isPingOnly() && !request.hasDrugsToScreen()) {
            xmlResponse = DrugCheckXmlUtility.createPingResponse(request.getHeader(), versionCapability
                .retrieveDrugDataVendorVersion());
        }
        else {
            // at least one order check type is required
            if (!request.isDrugDoseCheck() && !request.isDrugDrugCheck() && !request.isDrugTherapyCheck()) {
                throw new InterfaceValidationException(InterfaceValidationException.ORDER_CHECK_REQUERED);
            }
            // if not a ping request, there must be drugs to screen
            if (!request.hasDrugsToScreen()) {
                throw new InterfaceValidationException(InterfaceValidationException.DRUGS_TO_SCREEN_REQUIRED);
            }
            OrderCheckResultsVo response = performDrugChecksCapability.performDrugChecks(request);
            response.setHeader(request.getHeader());
            response.setDrugDataVendorVersion(versionCapability.retrieveDrugDataVendorVersion());
            xmlResponse = DrugCheckXmlUtility.toPepsResponse(response);
            // Prevent the XmlUtility from formatting the XML response with indents and the like if DEBUG is not turned on.
            // If this is not done, the expense of formatting will always be done. JAXB documentation recommends against
            // formatting in production code.
            if (LOG.isDebugEnabled()) {
                LOG.debug("Response sent back to VistA:");
                LOG.debug(DrugCheckXmlUtility.prettyPrintResponse(xmlResponse));
            }
        }
        return xmlResponse;
    }
    /**
     * 
     * @param versionCapability versionCapability property
     */
    public void setVersionCapability(VersionCapability versionCapability) {
        this.versionCapability = versionCapability;
    }
    /**
     * 
     * @param performDrugChecksCapability performDrugChecksCapability property
     */
    public void setPerformDrugChecksCapability(PerformDrugChecksCapability performDrugChecksCapability) {
        this.performDrugChecksCapability = performDrugChecksCapability;
    }
