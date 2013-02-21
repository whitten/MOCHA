/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability;
/**
 * Parse the XML message and call the drug data vendor to perform the order checks.
 */
public interface ProcessOrderChecksCapability {
    /**
     * Handle the XML request
     * 
     * @param request request XML from VistA
     * @return response XML
     */
    String handleRequest(String request);
