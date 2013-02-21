/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
/**
 * Results from a drug dose check
 */
public class DrugDoseCheckResultVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private DrugCheckVo drug;
    private String singleDoseStatus = "";
    private String singleDoseStatusCode = "";
    private String singleDoseMessage = "";
    private String singleDoseMax = "";
    private String rangeDoseStatus = "";
    private String rangeDoseStatusCode = "";
    private String rangeDoseMessage = "";
    private String rangeDoseLow = "";
    private String rangeDoseHigh = "";
    private String durationStatus = "";
    private String durationStatusCode = "";
    private String durationMessage = "";
    private String frequencyStatus = "";
    private String frequencyStatusCode = "";
    private String frequencyMessage = "";
    private String dailyDoseStatus = "";
    private String dailyDoseStatusCode = "";
    private String dailyDoseMessage = "";
    private String maxDailyDoseStatus = "";
    private String maxDailyDoseStatusCode = "";
    private String maxDailyDoseMessage = "";
    private String maxLifetimeDose = "";
    private String doseLow = "";
    private String doseLowUnit = "";
    private String doseHigh = "";
    private String doseHighUnit = "";
    private String doseRouteDescription = "";
    private String doseFormLow = "";
    private String doseFormLowUnit = "";
    private String doseFormHigh = "";
    private String doseFormHighUnit = "";
    private boolean chemoInjectable = false;
    private boolean custom;
    /**
     * 
     */
    public DrugDoseCheckResultVo() {
        super();
    }
    /**
     * 
     * @return rangeDoseHigh property
     */
    public String getRangeDoseHigh() {
        return rangeDoseHigh;
    }
    /**
     * 
     * @param rangeDoseHigh rangeDoseHigh property
     */
    public void setRangeDoseHigh(String rangeDoseHigh) {
        this.rangeDoseHigh = rangeDoseHigh;
    }
    /**
     * 
     * @return rangeDoseLow property
     */
    public String getRangeDoseLow() {
        return rangeDoseLow;
    }
    /**
     * 
     * @param rangeDoseLow rangeDoseLow property
     */
    public void setRangeDoseLow(String rangeDoseLow) {
        this.rangeDoseLow = rangeDoseLow;
    }
    /**
     * 
     * @return rangeDoseMessage property
     */
    public String getRangeDoseMessage() {
        return rangeDoseMessage;
    }
    /**
     * 
     * @param rangeDoseMessage rangeDoseMessage property
     */
    public void setRangeDoseMessage(String rangeDoseMessage) {
        this.rangeDoseMessage = rangeDoseMessage;
    }
    /**
     * 
     * @return rangeDoseStatus property
     */
    public String getRangeDoseStatus() {
        return rangeDoseStatus;
    }
    /**
     * 
     * @param rangeDoseStatus rangeDoseStatus property
     */
    public void setRangeDoseStatus(String rangeDoseStatus) {
        this.rangeDoseStatus = rangeDoseStatus;
    }
    /**
     * 
     * @return rangeDoseStatusCode property
     */
    public String getRangeDoseStatusCode() {
        return rangeDoseStatusCode;
    }
    /**
     * 
     * @param rangeDoseStatusCode rangeDoseStatusCode property
     */
    public void setRangeDoseStatusCode(String rangeDoseStatusCode) {
        this.rangeDoseStatusCode = rangeDoseStatusCode;
    }
    /**
     * 
     * @return singleDoseMax property
     */
    public String getSingleDoseMax() {
        return singleDoseMax;
    }
    /**
     * 
     * @param singleDoseMax singleDoseMax property
     */
    public void setSingleDoseMax(String singleDoseMax) {
        this.singleDoseMax = singleDoseMax;
    }
    /**
     * 
     * @return singleDoseMessage property
     */
    public String getSingleDoseMessage() {
        return singleDoseMessage;
    }
    /**
     * 
     * @param singleDoseMessage singleDoseMessage property
     */
    public void setSingleDoseMessage(String singleDoseMessage) {
        this.singleDoseMessage = singleDoseMessage;
    }
    /**
     * 
     * @return singleDoseStatus property
     */
    public String getSingleDoseStatus() {
        return singleDoseStatus;
    }
    /**
     * 
     * @param singleDoseStatus singleDoseStatus property
     */
    public void setSingleDoseStatus(String singleDoseStatus) {
        this.singleDoseStatus = singleDoseStatus;
    }
    /**
     * 
     * @return singleDoseStatusCode property
     */
    public String getSingleDoseStatusCode() {
        return singleDoseStatusCode;
    }
    /**
     * 
     * @param singleDoseStatusCode singleDoseStatusCode property
     */
    public void setSingleDoseStatusCode(String singleDoseStatusCode) {
        this.singleDoseStatusCode = singleDoseStatusCode;
    }
    /**
     * 
     * @return DrugCheckVo property
     */
    public DrugCheckVo getDrug() {
        return drug;
    }
    /**
     * 
     * @param DrugCheckVo DrugCheckVo property
     */
    public void setDrug(DrugCheckVo DrugCheckVo) {
        this.drug = DrugCheckVo;
    }
    /**
     * 
     * @return durationMessage property
     */
    public String getDurationMessage() {
        return durationMessage;
    }
    /**
     * 
     * @param durationMessage durationMessage property
     */
    public void setDurationMessage(String durationMessage) {
        this.durationMessage = durationMessage;
    }
    /**
     * 
     * @return durationStatus property
     */
    public String getDurationStatus() {
        return durationStatus;
    }
    /**
     * 
     * @param durationStatus durationStatus property
     */
    public void setDurationStatus(String durationStatus) {
        this.durationStatus = durationStatus;
    }
    /**
     * 
     * @return durationStatusCode property
     */
    public String getDurationStatusCode() {
        return durationStatusCode;
    }
    /**
     * 
     * @param durationStatusCode durationStatusCode property
     */
    public void setDurationStatusCode(String durationStatusCode) {
        this.durationStatusCode = durationStatusCode;
    }
    /**
     * 
     * @return frequencyMessage property
     */
    public String getFrequencyMessage() {
        return frequencyMessage;
    }
    /**
     * 
     * @param frequencyMessage frequencyMessage property
     */
    public void setFrequencyMessage(String frequencyMessage) {
        this.frequencyMessage = frequencyMessage;
    }
    /**
     * 
     * @return frequencyStatus property
     */
    public String getFrequencyStatus() {
        return frequencyStatus;
    }
    /**
     * 
     * @param frequencyStatus frequencyStatus property
     */
    public void setFrequencyStatus(String frequencyStatus) {
        this.frequencyStatus = frequencyStatus;
    }
    /**
     * 
     * @return frequencyStatusCode property
     */
    public String getFrequencyStatusCode() {
        return frequencyStatusCode;
    }
    /**
     * 
     * @param frequencyStatusCode frequencyStatusCode property
     */
    public void setFrequencyStatusCode(String frequencyStatusCode) {
        this.frequencyStatusCode = frequencyStatusCode;
    }
    /**
     * 
     * @return dailyDoseMessage property
     */
    public String getDailyDoseMessage() {
        return dailyDoseMessage;
    }
    /**
     * 
     * @param dailyDoseMessage dailyDoseMessage property
     */
    public void setDailyDoseMessage(String dailyDoseMessage) {
        this.dailyDoseMessage = dailyDoseMessage;
    }
    /**
     * 
     * @return dailyDoseStatus property
     */
    public String getDailyDoseStatus() {
        return dailyDoseStatus;
    }
    /**
     * 
     * @param dailyDoseStatus dailyDoseStatus property
     */
    public void setDailyDoseStatus(String dailyDoseStatus) {
        this.dailyDoseStatus = dailyDoseStatus;
    }
    /**
     * 
     * @return dailyDoseStatusCode property
     */
    public String getDailyDoseStatusCode() {
        return dailyDoseStatusCode;
    }
    /**
     * 
     * @param dailyDoseStatusCode dailyDoseStatusCode property
     */
    public void setDailyDoseStatusCode(String dailyDoseStatusCode) {
        this.dailyDoseStatusCode = dailyDoseStatusCode;
    }
    /**
     * 
     * @return maxDailyDoseMessage property
     */
    public String getMaxDailyDoseMessage() {
        return maxDailyDoseMessage;
    }
    /**
     * 
     * @param maxDailyDoseMessage maxDailyDoseMessage property
     */
    public void setMaxDailyDoseMessage(String maxDailyDoseMessage) {
        this.maxDailyDoseMessage = maxDailyDoseMessage;
    }
    /**
     * 
     * @return maxDailyDoseStatus property
     */
    public String getMaxDailyDoseStatus() {
        return maxDailyDoseStatus;
    }
    /**
     * 
     * @param maxDailyDoseStatus maxDailyDoseStatus property
     */
    public void setMaxDailyDoseStatus(String maxDailyDoseStatus) {
        this.maxDailyDoseStatus = maxDailyDoseStatus;
    }
    /**
     * 
     * @return maxDailyDoseStatusCode property
     */
    public String getMaxDailyDoseStatusCode() {
        return maxDailyDoseStatusCode;
    }
    /**
     * 
     * @param maxDailyDoseStatusCode maxDailyDoseStatusCode property
     */
    public void setMaxDailyDoseStatusCode(String maxDailyDoseStatusCode) {
        this.maxDailyDoseStatusCode = maxDailyDoseStatusCode;
    }
    /**
     * 
     * @return maxLifetimeDose property
     */
    public String getMaxLifetimeDose() {
        return maxLifetimeDose;
    }
    /**
     * 
     * @param maxLifetimeDose maxLifetimeDose property
     */
    public void setMaxLifetimeDose(String maxLifetimeDose) {
        this.maxLifetimeDose = maxLifetimeDose;
    }
    /**
     * 
     * @return doseLow property
     */
    public String getDoseLow() {
        return doseLow;
    }
    /**
     * 
     * @param doseLow doseLow property
     */
    public void setDoseLow(String doseLow) {
        this.doseLow = doseLow;
    }
    /**
     * 
     * @return doseLowUnit property
     */
    public String getDoseLowUnit() {
        return doseLowUnit;
    }
    /**
     * 
     * @param doseLowUnit doseLowUnit property
     */
    public void setDoseLowUnit(String doseLowUnit) {
        this.doseLowUnit = doseLowUnit;
    }
    /**
     * 
     * @return doseHigh property
     */
    public String getDoseHigh() {
        return doseHigh;
    }
    /**
     * 
     * @param doseHigh doseHigh property
     */
    public void setDoseHigh(String doseHigh) {
        this.doseHigh = doseHigh;
    }
    /**
     * 
     * @return doseHighUnit property
     */
    public String getDoseHighUnit() {
        return doseHighUnit;
    }
    /**
     * 
     * @param doseHighUnit doseHighUnit property
     */
    public void setDoseHighUnit(String doseHighUnit) {
        this.doseHighUnit = doseHighUnit;
    }
    /**
     * 
     * @return doseRouteDescription property
     */
    public String getDoseRouteDescription() {
        return doseRouteDescription;
    }
    /**
     * 
     * @param doseRouteDescription doseRouteDescription property
     */
    public void setDoseRouteDescription(String doseRouteDescription) {
        this.doseRouteDescription = doseRouteDescription;
    }
    /**
     * 
     * @return doseFormLow property
     */
    public String getDoseFormLow() {
        return doseFormLow;
    }
    /**
     * 
     * @param doseFormLow doseFormLow property
     */
    public void setDoseFormLow(String doseFormLow) {
        this.doseFormLow = doseFormLow;
    }
    /**
     * 
     * @return doseFormLowUnit property
     */
    public String getDoseFormLowUnit() {
        return doseFormLowUnit;
    }
    /**
     * 
     * @param doseFormLowUnit doseFormLowUnit property
     */
    public void setDoseFormLowUnit(String doseFormLowUnit) {
        this.doseFormLowUnit = doseFormLowUnit;
    }
    /**
     * 
     * @return doseFormHigh property
     */
    public String getDoseFormHigh() {
        return doseFormHigh;
    }
    /**
     * 
     * @param doseFormHigh doseFormHigh property
     */
    public void setDoseFormHigh(String doseFormHigh) {
        this.doseFormHigh = doseFormHigh;
    }
    /**
     * 
     * @return doseFormHighUnit property
     */
    public String getDoseFormHighUnit() {
        return doseFormHighUnit;
    }
    /**
     * 
     * @param doseFormHighUnit doseFormHighUnit property
     */
    public void setDoseFormHighUnit(String doseFormHighUnit) {
        this.doseFormHighUnit = doseFormHighUnit;
    }
    /**
     * 
     * @return chemoInjectable property
     */
    public boolean isChemoInjectable() {
        return chemoInjectable;
    }
    /**
     * 
     * @param chemoInjectable chemoInjectable property
     */
    public void setChemoInjectable(boolean chemoInjectable) {
        this.chemoInjectable = chemoInjectable;
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
