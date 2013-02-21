/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Represent data returned from a drug information request
 */
public class DrugInfoResultsVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private Collection<DrugInfoVo> drugsNotFound = new ArrayList<DrugInfoVo>();
    private Collection<DrugInfoVo> drugs = new ArrayList<DrugInfoVo>();
    /**
     * 
     * @return drugsNotFound property
     */
    public Collection<DrugInfoVo> getDrugsNotFound() {
        return drugsNotFound;
    }
    /**
     * 
     * @param drugsNotFound drugsNotFound property
     */
    public void setDrugsNotFound(Collection<DrugInfoVo> drugsNotFound) {
        this.drugsNotFound = drugsNotFound;
    }
    /**
     * 
     * @return drugs property
     */
    public Collection<DrugInfoVo> getDrugs() {
        return drugs;
    }
    /**
     * 
     * @param drugs drugs property
     */
    public void setDrugs(Collection<DrugInfoVo> drugs) {
        this.drugs = drugs;
    }
