/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.ArrayList;
import java.util.List;
/**
 * Professional monograph data
 */
public class ProfessionalMonographVo extends ValueObject {
    public static final String MONOGRAPH_TYPE = "FDB-PE";
    public static final String DISCLAIMER_SECTION = "Z";
    public static final String TITLE_SECTION = "T";
    public static final String SEVERITY_SECTION = "L";
    public static final String MECHANISM_SECTION = "A";
    public static final String CLINICAL_SECTION = "E";
    public static final String PREDISPOSING_SECTION = "P";
    public static final String PATIENT_SECTION = "M";
    public static final String DISCUSSION_SECTION = "D";
    public static final String REFERENCES_SECTION = "R";
    private static final long serialVersionUID = 1L;
    private boolean fdbMonographSourceType = true;
    private String disclaimer = "";
    private String monographTitle = "";
    private String severityLevel = "";
    private String mechanismOfAction = "";
    private String clinicalEffects = "";
    private String predisposingFactors = "";
    private String patientManagement = "";
    private String discussion = "";
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
     * @return severityLevel property
     */
    public String getSeverityLevel() {
        return severityLevel;
    }
    /**
     * 
     * @param severityLevel severityLevel property
     */
    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }
    /**
     * 
     * @return mechanismOfAction property
     */
    public String getMechanismOfAction() {
        return mechanismOfAction;
    }
    /**
     * 
     * @param mechanismOfAction mechanismOfAction property
     */
    public void setMechanismOfAction(String mechanismOfAction) {
        this.mechanismOfAction = mechanismOfAction;
    }
    /**
     * 
     * @return clinicalEffects property
     */
    public String getClinicalEffects() {
        return clinicalEffects;
    }
    /**
     * 
     * @param clinicalEffects clinicalEffects property
     */
    public void setClinicalEffects(String clinicalEffects) {
        this.clinicalEffects = clinicalEffects;
    }
    /**
     * 
     * @return predisposingFactors property
     */
    public String getPredisposingFactors() {
        return predisposingFactors;
    }
    /**
     * 
     * @param predisposingFactors predisposingFactors property
     */
    public void setPredisposingFactors(String predisposingFactors) {
        this.predisposingFactors = predisposingFactors;
    }
    /**
     * 
     * @return patientManagement property
     */
    public String getPatientManagement() {
        return patientManagement;
    }
    /**
     * 
     * @param patientManagement patientManagement property
     */
    public void setPatientManagement(String patientManagement) {
        this.patientManagement = patientManagement;
    }
    /**
     * 
     * @return discussion property
     */
    public String getDiscussion() {
        return discussion;
    }
    /**
     * 
     * @param discussion discussion property
     */
    public void setDiscussion(String discussion) {
        this.discussion = discussion;
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
