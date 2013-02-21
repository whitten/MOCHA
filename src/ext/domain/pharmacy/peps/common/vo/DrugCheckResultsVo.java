/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Results from drug check containing Collection of DrugDoseCheckResultVo, DrugDrugCheckResultVo,
 * or DrugTherapyCheckResultVo instances.
 * 
 * @param <T> Type of drug check (drug-drug, dosing or therapy)
 */
public class DrugCheckResultsVo<T> extends ValueObject {
    private static final long serialVersionUID = 1L;
    private Collection<DrugCheckMessageVo> messages = new ArrayList<DrugCheckMessageVo>();
    private Collection<T> checks = new ArrayList<T>();
    /**
     * Empty constructor
     */
    public DrugCheckResultsVo() {
        super();
    }
    /**
     * 
     * @return messages property
     */
    public Collection<DrugCheckMessageVo> getMessages() {
        return messages;
    }
    /**
     * 
     * @param messages messages property
     */
    public void setMessages(Collection<DrugCheckMessageVo> messages) {
        this.messages = messages;
    }
    /**
     * Collection of DrugDoseCheckResultVo, DrugDrugCheckResultVo, or DrugTherapyCheckResultVo
     * instances, depending on check performed.
     * 
     * @return checks property
     */
    public Collection<T> getChecks() {
        return checks;
    }
    /**
     * Collection of DrugDoseCheckResultVo, DrugDrugCheckResultVo, or DrugTherapyCheckResultVo
     * instances, depending on check performed.
     * 
     * @param checks therapyCheckResults property
     */
    public void setChecks(Collection<T> checks) {
        this.checks = checks;
    }
