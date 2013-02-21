/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.Date;
/**
 * Data representing site configuration
 */
public class SiteUpdateScheduleVo extends ValueObject {
    public static final String PEPS = "PEPS";
    public static final String COTS = "COTS";
    public static final String VISTA = "VISTA";
    private static final long serialVersionUID = 1L;
    private Long id;
    private String siteNumber;
    private String softwareUpdateType;
    private String softwareUpdateVersion;
    private String inProgress;
    private Date startDtm;
    private Date endDtm;
    private Date scheduleStartDtm;
    private String scheduleInterval;
    private String md5Sum;
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
     * @return siteNumber property
     */
    public String getSiteNumber() {
        return siteNumber;
    }
    /**
     * 
     * @param siteNumber siteNumber property
     */
    public void setSiteNumber(String siteNumber) {
        this.siteNumber = siteNumber;
    }
    /**
     * 
     * @return softwareUpdateType property
     */
    public String getSoftwareUpdateType() {
        return softwareUpdateType;
    }
    /**
     * 
     * @param softwareUpdateType softwareUpdateType property
     */
    public void setSoftwareUpdateType(String softwareUpdateType) {
        this.softwareUpdateType = softwareUpdateType;
    }
    /**
     * 
     * @return softwareUpdateVersion property
     */
    public String getSoftwareUpdateVersion() {
        return softwareUpdateVersion;
    }
    /**
     * 
     * @param softwareUpdateVersion softwareUpdateVersion property
     */
    public void setSoftwareUpdateVersion(String softwareUpdateVersion) {
        this.softwareUpdateVersion = softwareUpdateVersion;
    }
    /**
     * 
     * @return scheduleStartDtm property
     */
    public Date getScheduleStartDtm() {
        return scheduleStartDtm;
    }
    /**
     * 
     * @param scheduleStartDtm scheduleStartDtm property
     */
    public void setScheduleStartDtm(Date scheduleStartDtm) {
        this.scheduleStartDtm = scheduleStartDtm;
    }
    /**
     * 
     * @return scheduleInterval property
     */
    public String getScheduleInterval() {
        return scheduleInterval;
    }
    /**
     * 
     * @param scheduleInterval scheduleInterval property
     */
    public void setScheduleInterval(String scheduleInterval) {
        this.scheduleInterval = scheduleInterval;
    }
    /**
     * 
     * @return inProgress property
     */
    public String getInProgress() {
        return inProgress;
    }
    /**
     * 
     * @param inProgress inProgress property
     */
    public void setInProgress(String inProgress) {
        this.inProgress = inProgress;
    }
    /**
     * 
     * @return startDtm property
     */
    public Date getStartDtm() {
        return startDtm;
    }
    /**
     * 
     * @param startDtm startDtm property
     */
    public void setStartDtm(Date startDtm) {
        this.startDtm = startDtm;
    }
    /**
     * 
     * @return endDtm property
     */
    public Date getEndDtm() {
        return endDtm;
    }
    /**
     * 
     * @param endDtm endDtm property
     */
    public void setEndDtm(Date endDtm) {
        this.endDtm = endDtm;
    }
    /**
     * 
     * @return md5Sum property
     */
    public String getMd5Sum() {
        return md5Sum;
    }
    /**
     * 
     * @param md5Sum md5Sum property
     */
    public void setMd5Sum(String md5Sum) {
        this.md5Sum = md5Sum;
    }
