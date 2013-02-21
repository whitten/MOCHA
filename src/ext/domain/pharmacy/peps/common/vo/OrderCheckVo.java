/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Contains data required to perform order checks
 */
public class OrderCheckVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private boolean prospectiveOnly;
    private boolean drugDrugCheck;
    private boolean drugTherapyCheck;
    private boolean drugDoseCheck;
    private Collection<DrugCheckVo> drugsToScreen = new ArrayList<DrugCheckVo>();
    private long ageInDays;
    private double bodySurfaceAreaInSqM;
    private double weightInKg;
    private boolean duplicateAllowance;
    private OrderCheckHeaderVo header;
    private boolean customTables;
    /**
     * 
     */
    public OrderCheckVo() {
        super();
    }
    /**
     * 
     * @return ageInDays property
     */
    public long getAgeInDays() {
        return ageInDays;
    }
    /**
     * 
     * @param ageInDays ageInDays property
     */
    public void setAgeInDays(long ageInDays) {
        this.ageInDays = ageInDays;
    }
    /**
     * 
     * @return bodySurfaceAreaInSqM property
     */
    public double getBodySurfaceAreaInSqM() {
        return bodySurfaceAreaInSqM;
    }
    /**
     * 
     * @param bodySurfaceAreaInSqM bodySurfaceAreaInSqM property
     */
    public void setBodySurfaceAreaInSqM(double bodySurfaceAreaInSqM) {
        this.bodySurfaceAreaInSqM = bodySurfaceAreaInSqM;
    }
    /**
     * 
     * @return drugDoseCheck property
     */
    public boolean isDrugDoseCheck() {
        return drugDoseCheck;
    }
    /**
     * 
     * @param drugDoseCheck drugDoseCheck property
     */
    public void setDrugDoseCheck(boolean drugDoseCheck) {
        this.drugDoseCheck = drugDoseCheck;
    }
    /**
     * 
     * @return drugDrugCheck property
     */
    public boolean isDrugDrugCheck() {
        return drugDrugCheck;
    }
    /**
     * 
     * @param drugDrugCheck drugDrugCheck property
     */
    public void setDrugDrugCheck(boolean drugDrugCheck) {
        this.drugDrugCheck = drugDrugCheck;
    }
    /**
     * 
     * @return prospectiveOnly property
     */
    public boolean isProspectiveOnly() {
        return prospectiveOnly;
    }
    /**
     * 
     * @param prospectiveOnly prospectiveOnly property
     */
    public void setProspectiveOnly(boolean prospectiveOnly) {
        this.prospectiveOnly = prospectiveOnly;
    }
    /**
     * 
     * @return therapyCheck property
     */
    public boolean isDrugTherapyCheck() {
        return drugTherapyCheck;
    }
    /**
     * 
     * @param therapyCheck therapyCheck property
     */
    public void setDrugTherapyCheck(boolean therapyCheck) {
        this.drugTherapyCheck = therapyCheck;
    }
    /**
     * 
     * @return weightInKg property
     */
    public double getWeightInKg() {
        return weightInKg;
    }
    /**
     * 
     * @param weightInKg weightInKg property
     */
    public void setWeightInKg(double weightInKg) {
        this.weightInKg = weightInKg;
    }
    /**
     * 
     * @return drugsToScreen property
     */
    public Collection<DrugCheckVo> getDrugsToScreen() {
        return drugsToScreen;
    }
    
    /**
     * True if {@link #drugsToScreen} is not null as is not empty.
     * 
     * @return boolean true if there are drugs to screen
     */
    public boolean hasDrugsToScreen() {
        return drugsToScreen != null && !drugsToScreen.isEmpty();
    }
    /**
     * 
     * @param drugsToScreen drugsToScreen property
     */
    public void setDrugsToScreen(Collection<DrugCheckVo> drugsToScreen) {
        this.drugsToScreen = drugsToScreen;
    }
    /**
     * 
     * @return duplicateAllowance property
     */
    public boolean isDuplicateAllowance() {
        return duplicateAllowance;
    }
    /**
     * 
     * @param duplicateAllowance duplicateAllowance property
     */
    public void setDuplicateAllowance(boolean duplicateAllowance) {
        this.duplicateAllowance = duplicateAllowance;
    }
    /**
     * 
     * @return header property
     */
    public OrderCheckHeaderVo getHeader() {
        return header;
    }
    /**
     * 
     * @param header header property
     */
    public void setHeader(OrderCheckHeaderVo header) {
        this.header = header;
    }
    /**
     * 
     * @return customTables property
     */
    public boolean isCustomTables() {
        return customTables;
    }
    /**
     * 
     * @param customTables customTables property
     */
    public void setCustomTables(boolean customTables) {
        this.customTables = customTables;
    }
