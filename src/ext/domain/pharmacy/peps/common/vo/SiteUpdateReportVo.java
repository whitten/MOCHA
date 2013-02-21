/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.Date;
import java.util.TimeZone;
/**
 * Data representing site update report.
 */
public class SiteUpdateReportVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private Long siteId;
    private String siteName;
    private String regionName;
    private boolean isSuccessful;
    private String message;
    private Date lastUpdateTime;
    private TimeZone lastUpdateTimeZone;
    private String fdbSessionDate;
    private Long fdbId;
    private Long id;
    private String fdbCustomSessionDate;
    private String fdbCustomMessage;
    private boolean isFdbCustomSuccessful;
    private Long fdbCustomId;
    private Date customLastUpdateTime;
    private TimeZone customLastUpdateTimeZone;
    /**
     * 
     * @return id property
     */
    public Long getId() {
        return id;
    }
    /**
     * 
     * @param id id property
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * 
     * @return siteId property
     */
    public Long getSiteId() {
        return siteId;
    }
    /**
     * 
     * @param siteId siteId property
     */
    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }
    /**
     * 
     * @return siteName property
     */
    public String getSiteName() {
        return siteName;
    }
    /**
     * 
     * @param siteName siteName property
     */
    public void setSiteName(String siteName) {
        if (siteName != null) {
            siteName = siteName.replaceAll("(-|\\(|,).*", "").trim();
        }
        this.siteName = siteName;
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
     * 
     * @return isSuccessful property
     */
    public boolean isFdbSuccessful() {
        return isSuccessful;
    }
    /**
     * 
     * @param isSuccessful isSuccessful property
     */
    public void setFdbSuccessful(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }
    /**
     * 
     * @return message property
     */
    public String getFdbMessage() {
        return message;
    }
    /**
     * 
     * @param message message property
     */
    public void setFdbMessage(String message) {
        this.message = message;
    }
    /**
     * 
     * @return lastUpdateTime property
     */
    public Date getFdbLastUpdateTime() {
        return lastUpdateTime;
    }
    /**
     * 
     * @param lastUpdateTime lastUpdateTime property
     */
    public void setFdbLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    /**
     * 
     * @return lastUpdateTimeZone property
     */
    public TimeZone getFdbLastUpdateTimeZone() {
        return lastUpdateTimeZone;
    }
    /**
     * 
     * @param lastUpdateTimeZone lastUpdateTimeZone property
     */
    public void setFdbLastUpdateTimeZone(TimeZone lastUpdateTimeZone) {
        this.lastUpdateTimeZone = lastUpdateTimeZone;
    }
    /**
     * 
     * @return fdbVersionNumber property
     */
    public String getFdbSessionDate() {
        return fdbSessionDate;
    }
    /**
     * 
     * @param fdbSessionDate fdbVersionNumber property
     */
    public void setFdbSessionDate(String fdbSessionDate) {
        this.fdbSessionDate = fdbSessionDate;
    }
    /**
     * 
     * @return fdbId property
     */
    public Long getFdbId() {
        return fdbId;
    }
    /**
     * 
     * @param fdbId fdbId property
     */
    public void setFdbId(Long fdbId) {
        this.fdbId = fdbId;
    }
    /**
     * 
     * @return fdbCustomVersionNumber property
     */
    public String getFdbCustomSessionDate() {
        return fdbCustomSessionDate;
    }
    /**
     * 
     * @param fdbCustomSessionDate fdbCustomVersionNumber property
     */
    public void setFdbCustomSessionDate(String fdbCustomSessionDate) {
        this.fdbCustomSessionDate = fdbCustomSessionDate;
    }
    /**
     * 
     * @return fdbCustomMessage property
     */
    public String getFdbCustomMessage() {
        return fdbCustomMessage;
    }
    /**
     * 
     * @param fdbCustomMessage fdbCustomMessage property
     */
    public void setFdbCustomMessage(String fdbCustomMessage) {
        this.fdbCustomMessage = fdbCustomMessage;
    }
    /**
     * 
     * @return isFdbCustomSuccessful property
     */
    public boolean isFdbCustomSuccessful() {
        return isFdbCustomSuccessful;
    }
    /**
     * 
     * @param isFdbCustomSuccessful isFdbCustomSuccessful property
     */
    public void setFdbCustomSuccessful(boolean isFdbCustomSuccessful) {
        this.isFdbCustomSuccessful = isFdbCustomSuccessful;
    }
    /**
     * 
     * @return fdbCustomId property
     */
    public Long getFdbCustomId() {
        return fdbCustomId;
    }
    /**
     * 
     * @param fdbCustomId fdbCustomId property
     */
    public void setFdbCustomId(Long fdbCustomId) {
        this.fdbCustomId = fdbCustomId;
    }
    /**
     * 
     * @return lastUpdateTime property
     */
    public Date getFdbCustomLastUpdateTime() {
        return customLastUpdateTime;
    }
    /**
     * 
     * @param lastUpdateTime lastUpdateTime property
     */
    public void setFdbCustomLastUpdateTime(Date lastUpdateTime) {
        this.customLastUpdateTime = lastUpdateTime;
    }
    /**
     * 
     * @return customLastUpdateTimeZone property
     */
    public TimeZone getFdbCustomLastUpdateTimeZone() {
        return customLastUpdateTimeZone;
    }
    /**
     * 
     * @param customLastUpdateTimeZone customLastUpdateTimeZone property
     */
    public void setFdbCustomLastUpdateTimeZone(TimeZone customLastUpdateTimeZone) {
        this.customLastUpdateTimeZone = customLastUpdateTimeZone;
    }
