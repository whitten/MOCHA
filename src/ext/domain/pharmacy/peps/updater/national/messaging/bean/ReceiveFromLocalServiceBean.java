/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.national.messaging.bean;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.interceptor.Interceptors;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import EXT.DOMAIN.pharmacy.peps.updater.national.messaging.ReceiveFromLocalService;
/**
 * EJB implementation of InboundMessagingService.
 */
@MessageDriven(name = "ReceiveFromLocalService", activationConfig = {
                                                                     @ActivationConfigProperty(propertyName = "connectionFactoryJndiName", propertyValue = "jms/EXT/DOMAIN/pharmacy/peps/messagingservice/factory"),
                                                                     @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
                                                                     @ActivationConfigProperty(propertyName = "destinationName", propertyValue = "DatupQueue")
}, mappedName = "jms/EXT/DOMAIN/pharmacy/peps/messagingservice/queue/national/datup/receive")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ReceiveFromLocalServiceBean implements ReceiveFromLocalService, MessageListener {
    private static final long serialVersionUID = 1L;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ReceiveFromLocalServiceBean.class);
    @Autowired
    private ReceiveFromLocalService receiveFromLocalService;
    /**
     * 
     * Called automatically by server.
     * 
     * @param message Message from Local.
     * 
     * @see EXT.DOMAIN.pharmacy.peps.external.local.messagingservice.depricated.inbound.message.MessagingServiceMdb#onMessage(javax.jms.Message)
     */
    @Resource
    javax.ejb.MessageDrivenContext mc;
    public void onMessage(Message message) {
        try {
            receiveFromLocalService.onMessage(message);
        }
        catch (Exception e) {
            LOG.error("Rolling back transaction for message: " + message, e);
            mc.setRollbackOnly();
        }
    }
