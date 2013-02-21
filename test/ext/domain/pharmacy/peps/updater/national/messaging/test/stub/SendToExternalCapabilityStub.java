package EXT.DOMAIN.pharmacy.peps.updater.national.messaging.test.stub;
import EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject;
import EXT.DOMAIN.pharmacy.peps.updater.common.capability.impl.DifUpdateCapabilityImpl.FdbArchive;
import EXT.DOMAIN.pharmacy.peps.updater.national.messaging.SendToExternalCapability;
public class SendToExternalCapabilityStub implements SendToExternalCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SendToExternalCapabilityStub.class);
    
    public void sendToExternalTopic(FdbArchive archive) {
        LOG.debug("Sending to external topic: " + archive); 
    }
