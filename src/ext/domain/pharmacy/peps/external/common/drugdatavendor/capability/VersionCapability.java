/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDataVendorVersionVo;
/**
 * Retrieve drug data vendor version information
 */
public interface VersionCapability {
    /**
     * Retrieve drug data vendor version information.
     * 
     * @return DrugDataVendorVersionVo with version information
     */
    DrugDataVendorVersionVo retrieveDrugDataVendorVersion();
