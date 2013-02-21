/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.Date;
import java.util.TimeZone;
/**
 * Data representing a site version update.
 */
public class SiteUpdateVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private String[] siteId;
    private String regionName;
    private boolean isSuccessful;
    private boolean isCustom;
    private String message;
    private Date lastUpdateTime;
    private TimeZone lastUpdateTimeZone;
    private String fdbDatabaseVersion;
    private String fdbSessionDate;
    /**
     * 
     * @return siteId property
     */
    public String[] getSiteId() {
        return siteId;
    }
    /**
     * 
     * @param siteId siteId property
     */
    public void setSiteId(String[] siteId) {
        this.siteId = siteId;
    }
    /**
     * 
     * @return isSuccessful property
     */
    public boolean getIsSuccessful() {
        return isSuccessful;
    }
    /**
     * 
     * @param isSuccessful isSuccessful property
     */
    public void setIsSuccessful(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }
    /**
     * 
     * @return message property
     */
    public String getMessage() {
        return message;
    }
    /**
     * 
     * @param message message property
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * 
     * @return lastUpdateTime property
     */
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }
    /**
     * 
     * @param lastUpdateTime lastUpdateTime property
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    /**
     * 
     * @return lastUpdateTimeZone property
     */
    public TimeZone getLastUpdateTimeZone() {
        return lastUpdateTimeZone;
    }
    /**
     * 
     * @param lastUpdateTimeZone lastUpdateTimeZone property
     */
    public void setLastUpdateTimeZone(TimeZone lastUpdateTimeZone) {
        this.lastUpdateTimeZone = lastUpdateTimeZone;
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
     * @return
     */
    public String getFdbDatabaseVersion() {
        return fdbDatabaseVersion;
    }
    /**
     * @param fdbDatabaseVersion
     */
    public void setFdbDatabaseVersion(String fdbDatabaseVersion) {
        this.fdbDatabaseVersion = fdbDatabaseVersion;
    }
    /**
     * 
     * @return regionName property
     */
    public String getRegionName() {
        return regionName;
    }
    /**
     * 
     * @param regionName regionName property
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
    /**
     * @param fdbSessionDate
     */
    public void setFdbSessionDate(String fdbSessionDate) {
        this.fdbSessionDate = fdbSessionDate;
    }
    /**
     * @return
     */
    public String getFdbSessionDate() {
        return fdbSessionDate;
    }
