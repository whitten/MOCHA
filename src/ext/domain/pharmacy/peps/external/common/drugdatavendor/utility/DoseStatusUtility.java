/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.utility;
import EXT.DOMAIN.pharmacy.peps.common.utility.PropertyUtility;
import dbank.dif.FDBDOSEStatus;
/**
 * Strip any reference to FDB from the dose status
 */
public class DoseStatusUtility {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DoseStatusUtility.class);
    /**
     * Get the description of the status and strip any reference to FDB
     * 
     * @param status FDBDOSEStatus to convert
     * @return String status without reference to FDB
     */
    public static String convert(FDBDOSEStatus status) {
        String result = PropertyUtility.getProperty(DoseStatusUtility.class, status.toString());
        if (result == null) {
            LOG.error(status.toString() + " FDBDOSEStatus did not map successfully!");
            return status.toString();
        }
        else {
            return result;
        }
    }
    /**
     * Cannot instantiate
     */
    private DoseStatusUtility() {
        super();
    }
