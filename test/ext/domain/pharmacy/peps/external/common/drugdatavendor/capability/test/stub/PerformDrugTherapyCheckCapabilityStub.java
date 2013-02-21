/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.test.stub;
import java.util.Map;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugTherapyCheckResultVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.PerformDrugTherapyCheckCapability;
import dbank.dif.ScreenDrugs;
/**
 * Stub to perform duplicate therapy check - Return empty results object
 */
public class PerformDrugTherapyCheckCapabilityStub implements PerformDrugTherapyCheckCapability {
    /**
     * Empty constructor
     */
    public PerformDrugTherapyCheckCapabilityStub() {
        super();
    }
    /**
     * Stub to perform duplicate therapy check - Return empty results object
     * 
     * @param screenDrugs FDB ScreenDrugs used as input to therapy check call
     * @param idMap Map of GCN Sequence Number to DrugCheckVo
     * @param prospectiveOnly boolean true if check should only be done on prospective drugs
     * @param duplicateAllowance boolean true if check should allow for duplicates
     * @param customTables boolean true if customized tables to be used in the check
     * @return DrugCheckResultsVo response from FDB with Collection of DrugTherapyCheckResultVo
     */
    public DrugCheckResultsVo<DrugTherapyCheckResultVo> performDrugTherapyCheck(ScreenDrugs screenDrugs,
                                                                                Map<String, DrugCheckVo> idMap,
                                                                                boolean prospectiveOnly,
                                                                                boolean duplicateAllowance,
                                                                                boolean customTables) {
        return new DrugCheckResultsVo<DrugTherapyCheckResultVo>();
    }
