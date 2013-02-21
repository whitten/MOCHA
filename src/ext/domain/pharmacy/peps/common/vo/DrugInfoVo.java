/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.vo;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Data representing a request and response for drug information.
 */
public class DrugInfoVo extends ValueObject {
    private static final long serialVersionUID = 1L;
    private String gcnSeqNo = "";
    private String strengthUnit = "";
    private Collection<DoseRouteVo> doseRoutes = new ArrayList<DoseRouteVo>();
    private Collection<DoseTypeVo> doseTypes = new ArrayList<DoseTypeVo>();
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
     * @return doseRoutes property
     */
    public Collection<DoseRouteVo> getDoseRoutes() {
        return doseRoutes;
    }
    /**
     * 
     * @param doseRoutes doseRoutes property
     */
    public void setDoseRoutes(Collection<DoseRouteVo> doseRoutes) {
        this.doseRoutes = doseRoutes;
    }
    /**
     * 
     * @return doseTypes property
     */
    public Collection<DoseTypeVo> getDoseTypes() {
        return doseTypes;
    }
    /**
     * 
     * @param doseTypes doseTypes property
     */
    public void setDoseTypes(Collection<DoseTypeVo> doseTypes) {
        this.doseTypes = doseTypes;
    }
    /**
     * 
     * @return strengthUnit property
     */
    public String getStrengthUnit() {
        return strengthUnit;
    }
    /**
     * 
     * @param strengthUnit strengthUnit property
     */
    public void setStrengthUnit(String strengthUnit) {
        this.strengthUnit = strengthUnit;
    }
