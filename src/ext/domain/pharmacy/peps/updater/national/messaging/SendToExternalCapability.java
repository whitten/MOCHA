package EXT.DOMAIN.pharmacy.peps.updater.national.messaging;
import EXT.DOMAIN.pharmacy.peps.updater.common.capability.impl.DifUpdateCapabilityImpl.FdbArchive;
public interface SendToExternalCapability {
    /**
     * Send an XML message to an external topic.
     * 
     * @param FdbArchive archive
     */
    void sendToExternalTopic(FdbArchive archive);
    
