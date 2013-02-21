/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.utility;
import EXT.DOMAIN.pharmacy.peps.common.utility.PropertyUtility;
/**
 * Convert an FDB dose type name text value to the FDB dose type ID
 */
public class DoseTypeUtility {
    /**
     * Convert the dose type name to the ID
     * 
     * @param name name of the dose type
     * @return FDB ID of the dose type
     */
    public static String doseTypeNameToId(String name) {
        String value = PropertyUtility.getProperty(DoseTypeUtility.class, name);
        return StringUtility.nullToEmptyString(value);
    }
    /**
     * Cannot instantiate
     */
    private DoseTypeUtility() {
        super();
    }
