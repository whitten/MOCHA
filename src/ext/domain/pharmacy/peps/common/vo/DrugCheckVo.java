/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
/**
 * Contains data about a single drug
 */
public class DrugCheckVo extends ValueObject {
    public static final String VA_CUSTOM_CATEGORY = "VA";
    private static final long serialVersionUID = 1L;
    private double singleDoseAmount;
    private String doseUnit = "";
    private String doseRate = "";
    private String frequency = "";
    private long duration;
    private String durationRate = "";
    private String doseRoute = "";
    private String doseType = "";
    private String vuid = "";
    private String ien = "";
    private String orderNumber = "";
    private boolean prospective;
    private String gcnSeqNo = "";
    private boolean doseCheck;
    private String drugName = "";
    /**
     * 
     */
    public DrugCheckVo() {
        super();
    }
    /**
     * 
     * @return ien property
     */
    public String getIen() {
        return ien;
    }
    /**
     * 
     * @param ien ien property
     */
    public void setIen(String ien) {
        this.ien = ien;
    }
    /**
     * 
     * @return vuid property
     */
    public String getVuid() {
        return vuid;
    }
    /**
     * 
     * @param vuid vuid property
     */
    public void setVuid(String vuid) {
        this.vuid = vuid;
    }
    /**
     * 
     * @return prospective property
     */
    public boolean isProspective() {
        return prospective;
    }
    /**
     * 
     * @param propsective prospective property
     */
    public void setProspective(boolean propsective) {
        this.prospective = propsective;
    }
    /**
     * 
     * @return orderNumber property
     */
    public String getOrderNumber() {
        return orderNumber;
    }
    /**
     * 
     * @param orderNumber orderNumber property
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    /**
     * 
     * @return doseRate property
     */
    public String getDoseRate() {
        return doseRate;
    }
    /**
     * 
     * @param doseRate doseRate property
     */
    public void setDoseRate(String doseRate) {
        this.doseRate = doseRate;
    }
    /**
     * 
     * @return doseRoute property
     */
    public String getDoseRoute() {
        return doseRoute;
    }
    /**
     * 
     * @param doseRoute doseRoute property
     */
    public void setDoseRoute(String doseRoute) {
        this.doseRoute = doseRoute;
    }
    /**
     * 
     * @return doseUnit property
     */
    public String getDoseUnit() {
        return doseUnit;
    }
    /**
     * 
     * @param doseUnit doseUnit property
     */
    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
    }
    /**
     * 
     * @return duration property
     */
    public long getDuration() {
        return duration;
    }
    /**
     * 
     * @param duration duration property
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }
    /**
     * 
     * @return durationRate property
     */
    public String getDurationRate() {
        return durationRate;
    }
    /**
     * 
     * @param durationRate durationRate property
     */
    public void setDurationRate(String durationRate) {
        this.durationRate = durationRate;
    }
    /**
     * 
     * @return frequency property
     */
    public String getFrequency() {
        return frequency;
    }
    /**
     * 
     * @param frequency frequency property
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    /**
     * 
     * @return singleDoseAmount property
     */
    public double getSingleDoseAmount() {
        return singleDoseAmount;
    }
    /**
     * 
     * @param singleDoseAmount singleDoseAmount property
     */
    public void setSingleDoseAmount(double singleDoseAmount) {
        this.singleDoseAmount = singleDoseAmount;
    }
    /**
     * 
     * @return getDoseType property
     */
    public String getDoseType() {
        return doseType;
    }
    /**
     * 
     * @param type type property
     */
    public void setDoseType(String type) {
        this.doseType = type;
    }
    /**
     * 
     * @return gcnSeqNo property
     */
    public String getGcnSeqNo() {
        return gcnSeqNo;
    }
    /**
     * 
     * @param gcnSeqNo gcnSeqNo property
     */
    public void setGcnSeqNo(String gcnSeqNo) {
        this.gcnSeqNo = gcnSeqNo;
    }
    /**
     * 
     * @return doseCheck property
     */
    public boolean isDoseCheck() {
        return doseCheck;
    }
    /**
     * 
     * @param doseCheck doseCheck property
     */
    public void setDoseCheck(boolean doseCheck) {
        this.doseCheck = doseCheck;
    }
    /**
     * 
     * @return drugName property
     */
    public String getDrugName() {
        return drugName;
    }
    /**
     * 
     * @param drugName drugName property
     */
    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }
