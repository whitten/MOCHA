/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.Date;
/**
 * Data representing a site version update.
 */
public class FdbVersionVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private long fdbId;
    private boolean isCustom;
    private String fdbSessionDate;
    private Date when;
    /**
     * 
     * @return id property
     */
    public long getFdbId() {
        return fdbId;
    }
    /**
     * 
     * @param siteId siteId property
     */
    public void setFdbId(long fdbId) {
        this.fdbId = fdbId;
    }
    /**
     * 
     * @return lastUpdateTime property
     */
    public String getFdbSessionDate() {
        return fdbSessionDate;
    }
    /**
     * 
     * @param lastUpdateTime lastUpdateTime property
     */
    public void setFdbSessionDate(String versionDate) {
        this.fdbSessionDate = versionDate;
    }
    /**
     * 
     * @return isCustom property
     */
    public boolean getIsCustom() {
        return isCustom;
    }
    /**
     * 
     * @param isCustom isCustom property
     */
    public void setIsCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }
    /**
     * 
     * 
     * @return when
     */
    public Date getWhen() {
        return when;
    }
    /**
     * 
     * 
     * @param when when
     */
    public void setWhen(Date when) {
        this.when = when;
    }
