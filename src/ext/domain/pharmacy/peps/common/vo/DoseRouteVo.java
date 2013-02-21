/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
/**
 * Data representing a dose route for a drug
 */
public class DoseRouteVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    /**
     * 
     * @return id property
     */
    public String getId() {
        return id;
    }
    /**
     * 
     * @param id id property
     */
    public void setId(String id) {
        this.id = id;
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
     * @param name name property
     */
    public void setName(String name) {
        this.name = name;
    }
