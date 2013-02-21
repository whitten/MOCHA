/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
/**
 * Messages returned by a drug check
 */
public class DrugCheckMessageVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private DrugCheckVo drug;
    private String severity = "";
    private String type = "";
    private String drugName = "";
    private String text = "";
    /**
     * 
     */
    public DrugCheckMessageVo() {
        super();
    }
    /**
     * 
     * @return DrugCheckVo
     */
    public DrugCheckVo getDrug() {
        return drug;
    }
    /**
     * 
     * @param drug DrugCheckVo
     */
    public void setDrug(DrugCheckVo drug) {
        this.drug = drug;
    }
    /**
     * 
     * @return String
     */
    public String getSeverity() {
        return severity;
    }
    /**
     * 
     * @param severity String
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    /**
     * 
     * @return String
     */
    public String getType() {
        return type;
    }
    /**
     * 
     * @param type String
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * 
     * @return String
     */
    public String getDrugName() {
        return drugName;
    }
    /**
     * 
     * @param name String
     */
    public void setDrugName(String name) {
        this.drugName = name;
    }
    /**
     * 
     * @return String
     */
    public String getText() {
        return text;
    }
    /**
     * 
     * @param text String
     */
    public void setText(String text) {
        this.text = text;
    }
