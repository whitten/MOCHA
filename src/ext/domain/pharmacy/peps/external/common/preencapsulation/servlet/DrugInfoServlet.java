/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.servlet;
import javax.ejb.EJB;
import EXT.DOMAIN.pharmacy.peps.common.servlet.AbstractPepsServlet;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.session.DrugInfoService;
/**
 * Retrieve the dose routes and dose types for a given list of GCN sequence numbers.
 */
public class DrugInfoServlet extends AbstractPepsServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private DrugInfoService drugInfoService;
    /**
     * Retrieve the dose routes and dose types for a given list of GCN sequence numbers.
     * 
     * @param xmlRequest String request from Vista
     * @return String XML Response for Vista
     */
    @Override
    protected String getResponse(String xmlRequest) {
        return drugInfoService.retrieveDrugInformation(xmlRequest);
    }
