/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.session.bean;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.transaction.annotation.Transactional;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.session.OrderCheckService;
/**
 * Perform an order check requested by VistA using XML.
 */
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class OrderCheckServiceBean implements OrderCheckService {
    @Autowired
    private OrderCheckService orderCheckService;
    /**
     * Perform and order check for the given XML request.
     * 
     * @param request request XML from VistA
     * @return response XML
     */
    @Transactional(readOnly = true)
    public String performOrderCheck(String request) {
        return orderCheckService.performOrderCheck(request);
    }
    /**
     * 
     * @param orderCheckService orderCheckService property
     */
    public void setOrderCheckService(OrderCheckService orderCheckService) {
        this.orderCheckService = orderCheckService;
    }
    
    
    public boolean updatesRunning() {
        return this.orderCheckService.updatesRunning();
    }
