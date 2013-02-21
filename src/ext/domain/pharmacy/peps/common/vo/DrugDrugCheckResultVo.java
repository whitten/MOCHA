/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Results from a drug-drug check
 */
public class DrugDrugCheckResultVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private Collection<DrugCheckVo> drugs = new ArrayList<DrugCheckVo>();
    private String severity = "";
    private String interactionDescription = "";
    private String shortText = "";
    private ProfessionalMonographVo professionalMonograph;
    private ConsumerMonographVo consumerMonograph;
    private long id;
    private boolean custom;
    /**
     * 
     */
    public DrugDrugCheckResultVo() {
        super();
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
     * @return interactionDescription property
     */
    public String getInteractionDescription() {
        return interactionDescription;
    }
    /**
     * 
     * @param interactionDescription interactionDescription property
     */
    public void setInteractionDescription(String interactionDescription) {
        this.interactionDescription = interactionDescription;
    }
    /**
     * 
     * @return severity property
     */
    public String getSeverity() {
        return severity;
    }
    /**
     * 
     * @param severity severity property
     */
    public void setSeverity(String severity) {
        this.severity = severity;
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
     * @return professionalMonograph property
     */
    public ProfessionalMonographVo getProfessionalMonograph() {
        return professionalMonograph;
    }
    /**
     * 
     * @param professionalMonograph professionalMonograph property
     */
    public void setProfessionalMonograph(ProfessionalMonographVo professionalMonograph) {
        this.professionalMonograph = professionalMonograph;
    }
    /**
     * 
     * @return consumerMonograph property
     */
    public ConsumerMonographVo getConsumerMonograph() {
        return consumerMonograph;
    }
    /**
     * 
     * @param consumerMonograph consumerMonograph property
     */
    public void setConsumerMonograph(ConsumerMonographVo consumerMonograph) {
        this.consumerMonograph = consumerMonograph;
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
