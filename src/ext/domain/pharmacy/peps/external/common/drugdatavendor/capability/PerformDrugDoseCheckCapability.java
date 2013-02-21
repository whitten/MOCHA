/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability;
import java.util.Map;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDoseCheckResultVo;
import dbank.dif.ScreenDrugs;
/**
 * Perform a drug dosage check
 */
public interface PerformDrugDoseCheckCapability {
    /**
     * Perform a drug dosage check
     * 
     * @param screenDrugs FDB ScreenDrugs used as input to dose check call
     * @param drugMap Map of the combined String of IEN and order number to DrugCheckVo. The combined String of IEN and order
     *            number is stored in the FDB ScreenDrug's custom ID attribute.
     * @param prospectiveOnly boolean true if check should only be done on prospective drugs
     * @param ageInDays long age of patient in days
     * @param weightInKg double weight of patient in kilograms
     * @param bodySurfaceAreaInSqM double body surface area of patient in square meters
     * @param customTables boolean true if customized tables used in the check
     * @return DrugCheckResultsVo containing results from FDB with Collection of DrugDoseCheckResultVo
     */
    DrugCheckResultsVo<DrugDoseCheckResultVo> performDrugDoseCheck(ScreenDrugs screenDrugs,
                                                                   Map<String, DrugCheckVo> drugMap,
                                                                   boolean prospectiveOnly, long ageInDays,
                                                                   double weightInKg, double bodySurfaceAreaInSqM,
                                                                   boolean customTables);
