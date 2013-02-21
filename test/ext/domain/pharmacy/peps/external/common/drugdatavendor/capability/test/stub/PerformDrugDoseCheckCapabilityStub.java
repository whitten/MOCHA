/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.test.stub;
import java.util.Map;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDoseCheckResultVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.PerformDrugDoseCheckCapability;
import dbank.dif.ScreenDrugs;
/**
 * Stub to perform a drug dosage check - return an empty results object
 */
public class PerformDrugDoseCheckCapabilityStub implements PerformDrugDoseCheckCapability {
    /**
     * Empty constructor
     */
    public PerformDrugDoseCheckCapabilityStub() {
        super();
    }
    /**
     * Stub to perform a drug dosage check - return an empty results object
     * 
     * @param screenDrugs FDB ScreenDrugs used as input to dose check call
     * @param idMap Map of GCN Sequence Number to DrugCheckVo
     * @param prospectiveOnly boolean true if check should only be done on prospective drugs
     * @param ageInDays long age of patient in days
     * @param weightInKg double weight of patient in kilograms
     * @param bodySurfaceAreaInSqM double body surface area of patient in square meters
     * @param customTables boolean true if customized tables to be used in the check
     * @return DrugCheckResultsVo containing results from FDB with Collection of DrugDoseCheckResultVo
     */
    public DrugCheckResultsVo<DrugDoseCheckResultVo> performDrugDoseCheck(ScreenDrugs screenDrugs,
                                                                          Map<String, DrugCheckVo> idMap,
                                                                          boolean prospectiveOnly, long ageInDays,
                                                                          double weightInKg, double bodySurfaceAreaInSqM,
                                                                          boolean customTables) {
        return new DrugCheckResultsVo<DrugDoseCheckResultVo>();
    }
