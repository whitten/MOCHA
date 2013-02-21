/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability;
import java.util.Map;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDrugCheckResultVo;
import dbank.dif.ScreenDrugs;
/**
 * Perform drug-drug interaction check
 */
public interface PerformDrugDrugCheckCapability {
    /**
     * Perform drug-drug interaction check
     * 
     * @param screenDrugs FDB ScreenDrugs used as input to drug-drug check call
     * @param drugMap Map of the combined String of IEN and order number to DrugCheckVo. The combined String of IEN and order
     *            number is stored in the FDB ScreenDrug's custom ID attribute.
     * @param prospectiveOnly boolean true if check should only be done on prospective drugs
     * @param customTables boolean true if customized tables to be used in the check
     * @return DrugCheckResultsVo contains response from FDB with Collection of DrugDrugCheckResultVo
     */
    DrugCheckResultsVo<DrugDrugCheckResultVo> performDrugDrugCheck(ScreenDrugs screenDrugs,
                                                                   Map<String, DrugCheckVo> drugMap,
                                                                   boolean prospectiveOnly, boolean customTables);
