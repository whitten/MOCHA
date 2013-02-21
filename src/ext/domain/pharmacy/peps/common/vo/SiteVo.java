/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
/**
 * Data representing a site version update.
 */
public class SiteVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String shortName;
    private String visn;
    /**
     * 
     * @param id id property
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * 
     * @return id property
     */
    public Long getId() {
        return id;
    }
    /**
     * 
     * @param name name property
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * 
     * @return name property
     */
    public String getName() {
        return name;
    }
    /**
     * 
     * @param visn visn property
     */
    public void setVisn(String visn) {
        this.visn = visn;
    }
    /**
     * 
     * @return visn property
     */
    public String getVisn() {
        return visn;
    }
    /**
     * 
     * @param shortName shortName property
     */
    public void setShortName(String shortName) {
        if (shortName != null) {
            shortName = shortName.replaceAll("(-|\\().*", "");
        }
        this.shortName = shortName;
    }
    /**
     * 
     * @return shortName property
     */
    public String getShortName() {
        return shortName;
    }
