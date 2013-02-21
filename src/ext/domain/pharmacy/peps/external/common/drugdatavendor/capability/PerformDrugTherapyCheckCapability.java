/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability;
import java.util.Map;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugTherapyCheckResultVo;
import dbank.dif.ScreenDrugs;
/**
 * Perform drug-therapy interaction check
 */
public interface PerformDrugTherapyCheckCapability {
    /**
     * Perform duplicate therapy check
     * 
     * @param screenDrugs FDB ScreenDrugs used as input to therapy check call
     * @param drugMap Map of the combined String of IEN and order number to DrugCheckVo. The combined String of IEN and order
     *            number is stored in the FDB ScreenDrug's custom ID attribute.
     * @param prospectiveOnly boolean true if check should only be done on prospective drugs
     * @param duplicateAllowance boolean true if check should allow for duplicates
     * @param customTables boolean true if customized tables to be used in the check
     * @return DrugCheckResultsVo response from FDB with Collection of DrugTherapyCheckResultVo
     */
    DrugCheckResultsVo<DrugTherapyCheckResultVo> performDrugTherapyCheck(ScreenDrugs screenDrugs,
                                                                         Map<String, DrugCheckVo> drugMap,
                                                                         boolean prospectiveOnly,
                                                                         boolean duplicateAllowance, boolean customTables);
