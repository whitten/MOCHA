/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
/**
 * Drug data vendor version information
 */
public class DrugDataVendorVersionVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private String buildVersion = "";
    private String dataVersion = "";
    private String issueDate = "";
    private String customBuildVersion = "";
    private String customDataVersion = "";
    private String customIssueDate = "";
    /**
     * 
     * @return buildVersion property
     */
    public String getBuildVersion() {
        return buildVersion;
    }
    /**
     * 
     * @param buildVersion buildVersion property
     */
    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }
    /**
     * 
     * @return dataVersion property
     */
    public String getDataVersion() {
        return dataVersion;
    }
    /**
     * 
     * @param dataVersion dataVersion property
     */
    public void setDataVersion(String dataVersion) {
        this.dataVersion = dataVersion;
    }
    /**
     * 
     * @return issueDate property
     */
    public String getIssueDate() {
        return issueDate;
    }
    /**
     * 
     * @param issueDate issueDate property
     */
    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
    /**
     * 
     * @return customBuildVersion property
     */
    public String getCustomBuildVersion() {
        return customBuildVersion;
    }
    /**
     * 
     * @param customBuildVersion customBuildVersion property
     */
    public void setCustomBuildVersion(String customBuildVersion) {
        this.customBuildVersion = customBuildVersion;
    }
    /**
     * 
     * @return customDataVersion property
     */
    public String getCustomDataVersion() {
        return customDataVersion;
    }
    /**
     * 
     * @param customDataVersion customDataVersion property
     */
    public void setCustomDataVersion(String customDataVersion) {
        this.customDataVersion = customDataVersion;
    }
    /**
     * 
     * @return customIssueDate property
     */
    public String getCustomIssueDate() {
        return customIssueDate;
    }
    /**
     * 
     * @param customIssueDate customIssueDate property
     */
    public void setCustomIssueDate(String customIssueDate) {
        this.customIssueDate = customIssueDate;
    }
