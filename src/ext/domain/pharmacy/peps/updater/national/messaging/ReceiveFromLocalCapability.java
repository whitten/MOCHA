/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.national.messaging;
import EXT.DOMAIN.pharmacy.peps.common.exception.CommonException;
import EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject;
/**
 * Receive from Local capability.
 */
public interface ReceiveFromLocalCapability {
    /**
     * Handle the response sent from a local
     * 
     * @param valueObject ValueObject returned from local
     * @throws CommonException
     */
    void onMessage(ValueObject valueObject);
