/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckVo;
/**
 * Call the drug data vendor capabilities to perform the checks.
 */
public interface PerformDrugChecksCapability {
    /**
     * Convert drugs into FDB ScreenDrugs and call the individual
     * drug check capabilities to perform the checks.
     * 
     * @param requestVo OrderCheckVo requesting checks
     * @return OrderCheckResultsVo with results from drug data vendor
     */
    OrderCheckResultsVo performDrugChecks(OrderCheckVo requestVo);
