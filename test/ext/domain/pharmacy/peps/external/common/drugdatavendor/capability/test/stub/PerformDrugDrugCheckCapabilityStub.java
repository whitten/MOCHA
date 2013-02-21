/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.test.stub;
import java.util.Map;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDrugCheckResultVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.PerformDrugDrugCheckCapability;
import dbank.dif.ScreenDrugs;
/**
 * Stub to perform drug-drug interaction check - Return an empty results object
 */
public class PerformDrugDrugCheckCapabilityStub implements PerformDrugDrugCheckCapability {
    /**
     * Empty constructor
     */
    public PerformDrugDrugCheckCapabilityStub() {
        super();
    }
    /**
     * Stub to perform drug-drug interaction check - Return an empty results object
     * 
     * @param screenDrugs FDB ScreenDrugs used as input to drug-drug check call
     * @param idMap Map of GCN Sequence Number to DrugCheckVo
     * @param prospectiveOnly boolean true if check should only be done on prospective drugs
     * @param customTables boolean true if customized tables to be used in the check
     * @return DrugCheckResultsVo contains response from FDB with Collection of DrugDrugCheckResultVo
     */
    public DrugCheckResultsVo<DrugDrugCheckResultVo> performDrugDrugCheck(ScreenDrugs screenDrugs,
                                                                          Map<String, DrugCheckVo> idMap,
                                                                          boolean prospectiveOnly, boolean customTables) {
        return new DrugCheckResultsVo<DrugDrugCheckResultVo>();
    }
