/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.ArrayList;
import java.util.List;
/**
 * Consumer monograph data
 */
public class ConsumerMonographVo extends ValueObject {
    public static final String MONOGRAPH_TYPE = "FDB-CE";
    public static final String DISCLAIMER_SECTION = "Z";
    public static final String TITLE_SECTION = "T";
    public static final String MEDICAL_WARNING_SECTION = "L";
    public static final String HOW_OCCURS_SECTION = "A";
    public static final String WHAT_MIGHT_HAPPEN_SECTION = "E";
    public static final String WHAT_TO_DO_SECTION = "M";
    public static final String REFERENCE_SECTION = "R";
    private static final long serialVersionUID = 1L;
    private boolean fdbMonographSourceType = true;
    private String disclaimer = "";
    private String monographTitle = "";
    private String medicalWarning = "";
    private String howOccurs = "";
    private String whatMightHappen = "";
    private String whatToDo = "";
    private List<String> references = new ArrayList<String>();
    /**
     * 
     * @return disclaimer property
     */
    public String getDisclaimer() {
        return disclaimer;
    }
    /**
     * 
     * @param disclaimer disclaimer property
     */
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
    /**
     * 
     * @return monographTitle property
     */
    public String getMonographTitle() {
        return monographTitle;
    }
    /**
     * 
     * @param monographTitle monographTitle property
     */
    public void setMonographTitle(String monographTitle) {
        this.monographTitle = monographTitle;
    }
    /**
     * 
     * @return medicalWarning property
     */
    public String getMedicalWarning() {
        return medicalWarning;
    }
    /**
     * 
     * @param medicalWarning medicalWarning property
     */
    public void setMedicalWarning(String medicalWarning) {
        this.medicalWarning = medicalWarning;
    }
    /**
     * 
     * @return howOccurs property
     */
    public String getHowOccurs() {
        return howOccurs;
    }
    /**
     * 
     * @param howOccurs howOccurs property
     */
    public void setHowOccurs(String howOccurs) {
        this.howOccurs = howOccurs;
    }
    /**
     * 
     * @return whatMightHappen property
     */
    public String getWhatMightHappen() {
        return whatMightHappen;
    }
    /**
     * 
     * @param whatMightHappen whatMightHappen property
     */
    public void setWhatMightHappen(String whatMightHappen) {
        this.whatMightHappen = whatMightHappen;
    }
    /**
     * 
     * @return whatToDo property
     */
    public String getWhatToDo() {
        return whatToDo;
    }
    /**
     * 
     * @param whatToDo whatToDo property
     */
    public void setWhatToDo(String whatToDo) {
        this.whatToDo = whatToDo;
    }
    /**
     * 
     * @return references property
     */
    public List<String> getReferences() {
        return references;
    }
    /**
     * 
     * @param references references property
     */
    public void setReferences(List<String> references) {
        this.references = references;
    }
    /**
     * 
     * @return fdbMonographSourceType property
     */
    public boolean isFdbMonographSourceType() {
        return fdbMonographSourceType;
    }
    /**
     * 
     * @param fdbMonographSourceType fdbMonographSourceType property
     */
    public void setFdbMonographSourceType(boolean fdbMonographSourceType) {
        this.fdbMonographSourceType = fdbMonographSourceType;
    }
