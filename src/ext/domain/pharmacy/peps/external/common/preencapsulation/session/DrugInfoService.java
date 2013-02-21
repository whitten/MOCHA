/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.session;
/**
 * Retrieve the dose routes and dose types for a given list of GCN sequence numbers.
 */
public interface DrugInfoService {
    /**
     * Retrieve the dose routes and dose types for the given XML request.
     * 
     * @param request request XML from VistA
     * @return response XML
     */
    String retrieveDrugInformation(String request);
