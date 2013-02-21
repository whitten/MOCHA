/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability;
/**
 * Retrieve the dose routes and dose types for a given list of GCN sequence numbers.
 */
public interface ProcessDrugInfoCapability {
    /**
     * Handle the XML request
     * 
     * @param request request XML from VistA
     * @return response XML
     */
    String handleRequest(String request);
