/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Result from duplicate therapy check
 */
public class DrugTherapyCheckResultVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private Collection<DrugCheckVo> drugs = new ArrayList<DrugCheckVo>();
    private String duplicateClass = "";
    private String shortText = "";
    private long allowance;
    private long id;
    private boolean custom;
    /**
     * 
     */
    public DrugTherapyCheckResultVo() {
        super();
    }
    /**
     * 
     * @return allowance property
     */
    public long getAllowance() {
        return allowance;
    }
    /**
     * 
     * @param allowance allowance property
     */
    public void setAllowance(long allowance) {
        this.allowance = allowance;
    }
    /**
     * 
     * @return drugs property
     */
    public Collection<DrugCheckVo> getDrugs() {
        return drugs;
    }
    /**
     * 
     * @param drugs drugs property
     */
    public void setDrugs(Collection<DrugCheckVo> drugs) {
        this.drugs = drugs;
    }
    /**
     * 
     * @return duplicateClass property
     */
    public String getDuplicateClass() {
        return duplicateClass;
    }
    /**
     * 
     * @param duplicateClass duplicateClass property
     */
    public void setDuplicateClass(String duplicateClass) {
        this.duplicateClass = duplicateClass;
    }
    /**
     * 
     * @return shortText property
     */
    public String getShortText() {
        return shortText;
    }
    /**
     * 
     * @param shortText shortText property
     */
    public void setShortText(String shortText) {
        this.shortText = shortText;
    }
    /**
     * 
     * @return id property
     */
    public long getId() {
        return id;
    }
    /**
     * 
     * @param id id property
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * 
     * @return custom property
     */
    public boolean isCustom() {
        return custom;
    }
    /**
     * 
     * @param custom custom property
     */
    public void setCustom(boolean custom) {
        this.custom = custom;
    }
