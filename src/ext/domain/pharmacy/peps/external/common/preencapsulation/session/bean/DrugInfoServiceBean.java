/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.session.bean;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.transaction.annotation.Transactional;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.session.DrugInfoService;
/**
 * Retrieve the dose routes and dose types for a given list of GCN sequence numbers.
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class DrugInfoServiceBean implements DrugInfoService {
    @Autowired
    private DrugInfoService drugInfoService;
    /**
     * Retrieve the dose routes and dose types for the given XML request.
     * 
     * @param request request XML from VistA
     * @return response XML
     */
    @Transactional(readOnly = true)
    public String retrieveDrugInformation(String request) {
        return drugInfoService.retrieveDrugInformation(request);
    }
    /**
     * 
     * @param drugInfoService drugInfoService property
     */
    public void setDrugInfoService(DrugInfoService drugInfoService) {
        this.drugInfoService = drugInfoService;
    }
