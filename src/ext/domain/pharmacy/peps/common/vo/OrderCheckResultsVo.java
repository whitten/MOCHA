/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.Collection;
import java.util.HashSet;
/**
 * Results of requested order checks
 */
public class OrderCheckResultsVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private Collection<DrugCheckVo> drugsNotChecked = new HashSet<DrugCheckVo>(); // we want no duplicates in this collection
    private DrugCheckResultsVo<DrugDrugCheckResultVo> drugDrugCheckResults;
    private DrugCheckResultsVo<DrugDoseCheckResultVo> drugDoseCheckResults;
    private DrugCheckResultsVo<DrugTherapyCheckResultVo> drugTherapyCheckResults;
    private OrderCheckHeaderVo header;
    private DrugDataVendorVersionVo drugDataVendorVersion;
    /**
     * 
     */
    public OrderCheckResultsVo() {
        super();
    }
    /**
     * 
     * @return doseCheckResults property
     */
    public DrugCheckResultsVo<DrugDoseCheckResultVo> getDrugDoseCheckResults() {
        return drugDoseCheckResults;
    }
    /**
     * 
     * @param doseCheckResults doseCheckResults property
     */
    public void setDrugDoseCheckResults(DrugCheckResultsVo<DrugDoseCheckResultVo> doseCheckResults) {
        this.drugDoseCheckResults = doseCheckResults;
    }
    /**
     * 
     * @return drugCheckResults property
     */
    public DrugCheckResultsVo<DrugDrugCheckResultVo> getDrugDrugCheckResults() {
        return drugDrugCheckResults;
    }
    /**
     * 
     * @param drugCheckResults drugCheckResults property
     */
    public void setDrugDrugCheckResults(DrugCheckResultsVo<DrugDrugCheckResultVo> drugCheckResults) {
        this.drugDrugCheckResults = drugCheckResults;
    }
    /**
     * 
     * @return drugsNotChecked property
     */
    public Collection<DrugCheckVo> getDrugsNotChecked() {
        return drugsNotChecked;
    }
    /**
     * 
     * @param drugsNotChecked drugsNotChecked property
     */
    public void setDrugsNotChecked(Collection<DrugCheckVo> drugsNotChecked) {
        this.drugsNotChecked = drugsNotChecked;
    }
    /**
     * 
     * @return therapyCheckResults property
     */
    public DrugCheckResultsVo<DrugTherapyCheckResultVo> getDrugTherapyCheckResults() {
        return drugTherapyCheckResults;
    }
    /**
     * 
     * @param therapyCheckResults therapyCheckResults property
     */
    public void setDrugTherapyCheckResults(DrugCheckResultsVo<DrugTherapyCheckResultVo> therapyCheckResults) {
        this.drugTherapyCheckResults = therapyCheckResults;
    }
    /**
     * 
     * @return drugDataVendorVersion property
     */
    public DrugDataVendorVersionVo getDrugDataVendorVersion() {
        return drugDataVendorVersion;
    }
    /**
     * 
     * @param drugDataVendorVersion drugDataVendorVersion property
     */
    public void setDrugDataVendorVersion(DrugDataVendorVersionVo drugDataVendorVersion) {
        this.drugDataVendorVersion = drugDataVendorVersion;
    }
    /**
     * 
     * @return orderCheckHeader property
     */
    public OrderCheckHeaderVo getHeader() {
        return header;
    }
    /**
     * 
     * @param orderCheckHeader orderCheckHeader property
     */
    public void setHeader(OrderCheckHeaderVo orderCheckHeader) {
        this.header = orderCheckHeader;
    }
