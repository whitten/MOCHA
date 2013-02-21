/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.local.messaging;
import EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject;
/**
 * Send messages to National PEPS instance
 */
public interface SendToNationalCapability {
    /**
     * Send valueObject to the National PEPS instance
     * 
     * @param valueObject ValueObject to send
     * @param queueName Queue National will send response on
     */
    void send(ValueObject valueObject, String queueName);
    /**
     * 
     * 
     * @param valueObject the object sent to national
     */
    void send(ValueObject valueObject);
