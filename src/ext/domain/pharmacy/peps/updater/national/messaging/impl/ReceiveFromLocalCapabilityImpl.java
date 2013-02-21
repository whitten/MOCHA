/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.national.messaging.impl;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import EXT.DOMAIN.pharmacy.peps.common.exception.CommonException;
import EXT.DOMAIN.pharmacy.peps.common.vo.SiteUpdateVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject;
import EXT.DOMAIN.pharmacy.peps.updater.common.database.SiteUpdate;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DateTimeUtility;
import EXT.DOMAIN.pharmacy.peps.updater.national.messaging.ReceiveFromLocalCapability;
/**
 * Receive messages from Local
 */
public class ReceiveFromLocalCapabilityImpl implements ReceiveFromLocalCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(ReceiveFromLocalCapabilityImpl.class);
    private SiteUpdate siteUpdate;
    /**
     * Empty constructor
     */
    public ReceiveFromLocalCapabilityImpl() {
        super();
    }
    /**
     * Handle the response sent from a local
     * 
     * @param vo ValueObject returned from local
     * @throws CommonException if error
     * 
     * @see EXT.DOMAIN.pharmacy.peps.external.national.messagingservice.inbound.capability.
     *      ReceiveResponseCapability#receiveResponse(EXT.DOMAIN.pharmacy.peps.common.vo.ValueObject)
     */
    public void onMessage(ValueObject vo) {
        LOG.debug("Received message from Local: " + vo);
        try {
            if (vo instanceof SiteUpdateVo) {
                processSiteUpdate((SiteUpdateVo) vo);
            }
        }
        catch (Throwable t) {
            LOG.error("Unable to process message from Local: " + vo, t);
        }
    }
    /**
     * Database capability.
     * 
     * @param capability database capability
     */
    public void setSiteUpdate(SiteUpdate capability) {
        this.siteUpdate = capability;
    }
    /**
     * Process the site update from Local.
     * 
     * @param vo site update
     */
    private void processSiteUpdate(SiteUpdateVo vo) {
        String[] sites = vo.getSiteId();
        long versionId = siteUpdate.retrieveFdbVersion(vo.getIsCustom(), vo.getFdbSessionDate());
        for (String site : sites) {
            LOG.debug("Processing site update: " + site);
            try {
                siteUpdate.insertFdbSiteUpdate(Long.parseLong(site), vo.getRegionName(), versionId, vo.getIsSuccessful(), vo
                    .getMessage(), vo.getLastUpdateTime(), vo.getLastUpdateTimeZone());
            }
            catch (DataAccessException e) {
                LOG.error("Unable to insert site update record. Is the site number valid? " + site, e);
            }
        }
    }
