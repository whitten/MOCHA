/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.impl;
import java.util.Collection;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceValidationException;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.DrugInfoCapability;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.ProcessDrugInfoCapability;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.druginfo.DrugInfoXmlUtility;
/**
 * Retrieve the dose routes and dose types for a given list of GCN sequence numbers.
 */
public class ProcessDrugInfoCapabilityImpl implements ProcessDrugInfoCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(ProcessDrugInfoCapabilityImpl.class);
    private DrugInfoCapability drugInfoCapability;
    /**
     * Handle the XML request
     * 
     * @param xmlRequest request XML from VistA
     * @return response XML
     */
    public String handleRequest(String xmlRequest) {
        LOG.debug("Request received from VistA:");
        LOG.debug(xmlRequest);
        if (xmlRequest == null) {
            throw new InterfaceValidationException(InterfaceValidationException.XML_REQUEST_REQUIRED);
        }
        Collection<DrugInfoVo> drugs = DrugInfoXmlUtility.toDrugInfoVo(xmlRequest);
        DrugInfoResultsVo results = drugInfoCapability.processDrugInfoRequest(drugs);
        String xmlResponse = DrugInfoXmlUtility.toDrugInfoResponse(results);
        // Prevent the XmlUtility from formatting the XML response with indents and the like if DEBUG is not turned on.
        // If this is not done, the expense of formatting will always be done. JAXB documentation recommends against
        // formatting in production code.
        if (LOG.isDebugEnabled()) {
            LOG.debug("Response sent back to VistA:");
            LOG.debug(DrugInfoXmlUtility.prettyPrintResponse(xmlResponse));
        }
        return xmlResponse;
    }
    /**
     * 
     * @param drugInfoCapability drugInfoCapability property
     */
    public void setDrugInfoCapability(DrugInfoCapability drugInfoCapability) {
        this.drugInfoCapability = drugInfoCapability;
    }
