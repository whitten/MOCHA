/**
 * Copyright 2010, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.impl.RandomOrderCheckCapabilityImpl.DrugCheckType;
/**
 *
 */
public interface RandomOrderCheckCapability {
    /**
     * Generates a random order check.
     * 
     * @param drugCheckTypeString
     * @return
     */
    String getRandomOrderCheck(DrugCheckType drugCheckType);
