/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.utility;
/**
 * Convert any null string to an empty string
 */
public class StringUtility {
    /**
     * Convert any null string to an empty string
     * 
     * @param string String to set to empty, if needed
     * @return the original string or an empty string if the original is null
     */
    public static String nullToEmptyString(String string) {
        return string == null ? "" : string;
    }
    /**
     * Cannot instantiate
     */
    private StringUtility() {
        super();
    }
