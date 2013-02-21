/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability;
import java.util.Collection;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoVo;
/**
 * Lookup the dose routes and types for the given drugs.
 */
public interface DrugInfoCapability {
    /**
     * Lookup the dose routes and types for the given drugs.
     * 
     * @param drugs Collection of DrugInfoVo
     * @return DrugInfoResultsVo containing drugs not found and drugs with does routes and types
     */
    DrugInfoResultsVo processDrugInfoRequest(Collection<DrugInfoVo> drugs);
